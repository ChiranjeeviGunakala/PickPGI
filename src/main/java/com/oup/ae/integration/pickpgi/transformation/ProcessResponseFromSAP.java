package com.oup.ae.integration.pickpgi.transformation;

import org.apache.camel.Exchange;
import org.apache.camel.Processor;
import org.fusesource.camel.component.sap.model.rfc.Structure;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Component("responseProcesssor")
public class ProcessResponseFromSAP implements Processor {
	final Logger logger = LoggerFactory.getLogger(this.getClass());

	public void process(Exchange exchange) throws Exception {

		final Structure response = exchange.getIn().getBody(Structure.class);
		final String successResponse = response.get("E_SUCCESS", String.class);
		final String failureResponse = response.get("E_FAILURE", String.class);
		if (null != successResponse && !successResponse.isEmpty()) {
			logger.info("The value of E_SUCCESS:" + successResponse);
		} else if (null != failureResponse && !failureResponse.isEmpty()) {
			logger.error("The value of E_FAILURE:" + failureResponse);
		} else {
			logger.info("Empty response from SAP");
		}
	}
}
