package com.capgemini.salesforcepoc;

import org.apache.camel.Exchange;
import org.apache.camel.Processor;

import com.capgemini.salesforcepoc.dto.Lead;
import com.capgemini.salesforcepoc.dto.Lead_LeadSourceEnum;
import com.capgemini.salesforcepoc.dto.Lead_SalutationEnum;
import com.capgemini.salesforcepoc.dto.Lead_StatusEnum;

/**
 * @author MARCWEBE
 *
 */
public class LeadProcessor implements Processor {
  public void process(Exchange exchange) throws Exception {

    String email = exchange.getProperty("email", String.class);
    Lead lead = new Lead();
    lead.setEmail(email);
    lead.setLastName("Unknown");
    lead.setCompany("Unknown");
    lead.setLeadSource(Lead_LeadSourceEnum.OTHER);
    lead.setSalutation(Lead_SalutationEnum.MR_);
    lead.setStatus(Lead_StatusEnum.NEW);

    exchange.getOut().setBody(lead, Lead.class);
  }
}