package com.capgemini.salesforcepoc;

import javax.ws.rs.core.MediaType;

import org.apache.camel.CamelContext;
import org.apache.camel.Exchange;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.component.salesforce.SalesforceComponent;
import org.apache.camel.component.salesforce.SalesforceEndpointConfig;
import org.apache.camel.component.salesforce.SalesforceLoginConfig;
import org.apache.camel.model.rest.RestBindingMode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.capgemini.salesforcepoc.dto.Lead;

@Component
public class IntegrationRoute extends RouteBuilder {

  @Value("${salesforce.auth.uri}")
  private String salesforceAuthUri;

  @Value("${salesforce.auth.username}")
  private String salesforceAuthUsername;

  @Value("${salesforce.auth.password}")
  private String salesforceAuthPassword;

  @Value("${salesforce.auth.securitytoken}")
  private String salesforceAuthSecurityToken;

  @Value("${salesforce.auth.client.id}")
  private String salesforceAuthClientID;

  @Value("${salesforce.auth.client.secret}")
  private String salesforceAuthClientSecret;

  @Value("${log.uri}")
  private String logger;

  @Autowired
  CamelContext camelContext;

  @Override
  public void configure() throws Exception {

    SalesforceLoginConfig salesforceLoginConfig = new SalesforceLoginConfig();
    salesforceLoginConfig.setUserName(this.salesforceAuthUsername);
    salesforceLoginConfig.setPassword(this.salesforceAuthPassword + this.salesforceAuthSecurityToken);
    salesforceLoginConfig.setLoginUrl(this.salesforceAuthUri);
    salesforceLoginConfig.setClientId(this.salesforceAuthClientID);
    salesforceLoginConfig.setClientSecret(this.salesforceAuthClientSecret);
    salesforceLoginConfig.setLazyLogin(false);
    Lead lead;

    SalesforceEndpointConfig salesforceEndpointConfig = new SalesforceEndpointConfig();
    salesforceEndpointConfig.setRawPayload(false);

    SalesforceComponent salesforceComponent = new SalesforceComponent();
    salesforceComponent.setLoginConfig(salesforceLoginConfig);
    salesforceComponent.setConfig(salesforceEndpointConfig);
    this.camelContext.addComponent("salesforce", salesforceComponent);

    restConfiguration().component("jetty").host("{{server.host}}").port("{{server.port}}")
        .bindingMode(RestBindingMode.json).dataFormatProperty("prettyPrint", "true")
        // Enable swagger endpoint; yaml file will be served at /passbook/api-doc/swagger.yaml
        .apiContextPath("/salesforcepoc/api-doc") // swagger endpoint path
        .apiContextRouteId("api-doc") // id of route providing the swagger endpoint
        // Swagger properties
        .apiProperty("api.title", "Telekom PoC Microservice API").apiProperty("api.version", "1.0").enableCORS(true)
        .corsHeaderProperty("Access-Control-Allow-Headers", "*")
        .corsHeaderProperty("Access-Control-Allow-Methods", "POST, GET, OPTIONS, HEAD, PUT, DELETE, PATCH")
        .corsHeaderProperty("Access-Control-Allow-Origin", "*")
        .corsHeaderProperty("Access-Control-Allow-Credentials", "true");

    onException(Exception.class).handled(true).setHeader(Exchange.HTTP_RESPONSE_CODE, constant(500))
        .setBody(simple("${exception.message}\n"));

    rest().description("Salesforce PoC Microservice API")
        // create new lead
        .post("/mythaistar/services/rest/register").to("direct:createlead")
        // get leads
        .get("/salesforcepoc/leads").to("direct:getleads")
        // health check
        .get("/salesforcepoc/health").to("direct:health");

    // creates new lead in Salesforce with the eMail address from MTS register service call
    from("direct:createlead").log("POST call for /mythaistar/services/rest/register recieved:").to(this.logger)
        // store data from incoming message (from the MTS frontend), and forward the message to the MTS backend
        .setProperty("email").spel("#{request.body['email']}")
        /*
         * //API not implemented in MTS java backend. Uncomment if API is available .marshal().json(JsonLibrary.Jackson)
         * .log("send to MTS server {{mts.backend.uri}}:").to(this.logger)
         * .to("{{mts.backend.uri}}?bridgeEndpoint=true")
         * .convertBodyTo(String.class).log("Answer from MTS server:").to(this.logger)
         */
        // forfeit the response and send the DTO to the Salesforce target uri to create a new Lead instead
        .process(new LeadProcessor()).log("send to salesforce API:").to(this.logger).to("salesforce:createSObject")
        .log("Answer from salesforce API:").to(this.logger);

    // get the list of users (eMail addresses) that have been registered in Salesforce as leads after registering in MTS
    from("direct:getleads").log("GET call for /salesforcepoc/leads received.")
        .to("salesforce:query?sObjectQuery=SELECT email FROM Lead WHERE lastname = 'Unknown'"
            + "&sObjectClass=com.capgemini.salesforcepoc.dto.QueryRecordsLead")
        .log("Response from SOQL query recieved:").to(this.logger);

    // health check
    from("direct:health").setHeader(Exchange.HTTP_RESPONSE_CODE, constant(200)).setHeader("Content-type")
        .simple(MediaType.APPLICATION_JSON)
        .setHeader(Exchange.HTTP_METHOD, constant(org.apache.camel.component.http4.HttpMethods.GET)).transform()
        .simple("ok");

    from("jetty:http://{{server.host}}:{{server.port}}/salesforcepoc/api-test")
        .description("Simple Website to test the API").convertBodyTo(String.class).to(this.logger).setBody()
        .simple("resource:classpath:SalesforcePoCTest.html").to(this.logger);

  }
}