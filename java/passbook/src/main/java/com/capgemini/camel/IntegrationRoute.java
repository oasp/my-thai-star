package com.capgemini.camel;

import javax.ws.rs.core.MediaType;

import org.apache.camel.Exchange;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.model.dataformat.JsonLibrary;
import org.apache.camel.model.rest.RestBindingMode;
import org.apache.camel.model.rest.RestParamType;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import io.swagger.annotations.ApiResponse;

@Component
public class IntegrationRoute extends RouteBuilder {

  @Value("${verbose}")
  private Boolean isVerbose;

  private static String logger = "log:com.capgemini.camel.IntegrationRoute";

  @Override
  public void configure() throws Exception {

    if (this.isVerbose) {
      logger += "?showAll=true&multiline=true";
    }

    restConfiguration().component("jetty").host("{{server.host}}").port("{{server.port}}")
        .bindingMode(RestBindingMode.json).dataFormatProperty("prettyPrint", "true")
        // Enable swagger endpoint; yaml file will be served at /passbook/api-doc/swagger.yaml
        .apiContextPath("/passbook/api-doc") // swagger endpoint path
        .apiContextRouteId("api-doc") // id of route providing the swagger endpoint
        // Swagger properties
        // .contextPath("/passbook") // base.path swagger property; use the mapping path set for Passbook API
        .apiProperty("api.title", "Passbook Microservice API").apiProperty("api.version", "1.0").enableCORS(true)
        .corsHeaderProperty("Access-Control-Allow-Headers", "*")
        .corsHeaderProperty("Access-Control-Allow-Methods", "POST, GET, OPTIONS, HEAD, PUT, DELETE, PATCH")
        .corsHeaderProperty("Access-Control-Allow-Origin", "*")
        .corsHeaderProperty("Access-Control-Allow-Credentials", "true");

    onException(Exception.class).handled(true).setHeader(Exchange.HTTP_RESPONSE_CODE, constant(500))
        .setBody(simple("${exception.message}\n"));

    rest().description("Passbook Microservice API")
        // create-pass API
        .post("/mythaistar/services/rest/bookingmanagement/v1/booking")
        .description("Creates and sends a booking pass, then routes the original request to the MTS java backend")
        .produces(MediaType.APPLICATION_JSON).consumes(MediaType.APPLICATION_JSON).param().name("bookingDate")
        .description("The target date for the booking").type(RestParamType.body).dataType("date").endParam().param()
        .name("name").description("The name for the booking").type(RestParamType.body).dataType("string").endParam()
        .param().name("email").description("The eMail address to send the booking to").type(RestParamType.body)
        .dataType("string").endParam().param().name("bookingType")
        .description("The booking type. Either 0=COMMON or 1=INVITED").type(RestParamType.body).dataType("int")
        .endParam().param().name("assistants").description("The number of assistants for the booking")
        .type(RestParamType.body).dataType("int").endParam().responseMessage().code(200)
        .responseModel(ApiResponse.class).message("Ok. The pass was successfully created.").endResponseMessage()
        .responseMessage().code(400).responseModel(ApiResponse.class).message("Unexpected body").endResponseMessage()
        .responseMessage().code(500).responseModel(ApiResponse.class)
        .message("Internal Server Error. The cat is dead, and isn't!").endResponseMessage().to("direct:create")
        // health check
        .get("/passbook/health").description("Basic health check. Returns OK (200) if API is reachable")
        .responseMessage().code(200).responseModel(ApiResponse.class).message("ok").endResponseMessage()
        .to("direct:health");

    from("direct:create").log("Post call recieved:").to(logger).setProperty("name")
        .spel("#{request.body['booking']['name']}").setProperty("bookingDate")
        .spel("#{request.body['booking']['bookingDate']}").setProperty("email")
        .spel("#{request.body['booking']['email']}").marshal().json(JsonLibrary.Jackson).choice()
        .when(simple("{{verbose}}")).setHeader("Trace", constant("verbose")).end()
        .log("send to MTS server {{mts.backend.uri}}:").to(logger).to("{{mts.backend.uri}}?bridgeEndpoint=true")
        .convertBodyTo(String.class).log("Answer from MTS server:").to(logger).transform()
        .simple("{ \"name\":\"${exchangeProperty.name}\",\"date\":\"${exchangeProperty.bookingDate}\" }")
        .setHeader("Content-type").simple(MediaType.APPLICATION_JSON).setHeader("Authorization")
        .simple("{{passbook.auth.key}}")
        .setHeader(Exchange.HTTP_METHOD, constant(org.apache.camel.component.http4.HttpMethods.POST))
        .log("send to passbook http4://api.passslot.com:80/v1/templates/{{passbook.template.no}}/pass:").to(logger)
        .to("http4://api.passslot.com:80/v1/templates/{{passbook.template.no}}/pass?bridgeEndpoint=true")
        .convertBodyTo(String.class).log("Answer from passbook API 1:").to(logger).unmarshal().json(JsonLibrary.Jackson)
        .setProperty("serialNumber").spel("#{request.body['serialNumber']}").transform()
        .simple("{ \"email\":\"${exchangeProperty.email}\" }")
        .log(
            "send to passbook https4://api.passslot.com/v1/passes/pass.slot.developer1/${exchangeProperty.serialNumber}/email:")
        .to(logger)
        .toD("https4://api.passslot.com/v1/passes/pass.slot.developer1/${exchangeProperty.serialNumber}/email")
        .convertBodyTo(String.class).log("Answer from passbook API 2:").to(logger);

    from("direct:health").setHeader(Exchange.HTTP_RESPONSE_CODE, constant(200)).setHeader("Content-type")
        .simple(MediaType.APPLICATION_JSON)
        .setHeader(Exchange.HTTP_METHOD, constant(org.apache.camel.component.http4.HttpMethods.GET)).transform()
        .simple("ok");

    from("jetty:http://{{server.host}}:{{server.port}}/passbook/api-test").description("Simple Website to test the API")
        .convertBodyTo(String.class).to(logger).setBody().simple("resource:classpath:PassSlotTest.html").to(logger);

  }
}