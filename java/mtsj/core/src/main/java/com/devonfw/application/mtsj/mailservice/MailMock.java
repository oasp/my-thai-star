package com.devonfw.application.mtsj.mailservice;

import javax.inject.Named;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;

/**
 * Mock class for Mail Service. The mail content is printed as a Log
 *
 */
@Named
@ConditionalOnProperty(prefix = "spring.mail", name = "enabled", havingValue = "false")
public class MailMock implements Mail {

  @Value("${spring.mail.username}")
  private String from;

  /**
   * Logger instance.
   */
  private static final Logger LOG = LoggerFactory.getLogger(MailMock.class);

  @Override
  public boolean sendMail(String to, String subject, String text) {

    StringBuilder sb = new StringBuilder();
    String logMessage = sb.append("From: ").append(this.from).append("|").append("To: ").append(to).append("|").append("Subject: ")
        .append(subject).append("|").append("Text: ").append(text).toString();
    LOG.info(logMessage);
    return true;
  }
}
