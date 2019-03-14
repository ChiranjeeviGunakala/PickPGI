/**
 * 
 */
package com.oup.ae.integration.pickpgi.transformation;

import org.apache.camel.Exchange;
import org.apache.camel.Processor;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.fusesource.camel.component.sap.SapSynchronousRfcDestinationEndpoint;
import org.fusesource.camel.component.sap.model.rfc.Structure;
import org.fusesource.camel.component.sap.model.rfc.Table;
import org.slf4j.Logger;
import org.springframework.stereotype.Component;

import com.oup.ae.integration.pickpgi.pojo.PGI;

/**
 * @author gunakalc
 *
 */
@Component("pickPGITransformation")
public class PickPGITransformation implements Processor {

	Logger logger = org.slf4j.LoggerFactory.getLogger(this.getClass());

	@Override
	public void process(Exchange exchange) throws Exception {

		logger.info("KN File Transformation has been started");

		final PGI pgi = exchange.getIn().getBody(PGI.class);

		logger.info(ToStringBuilder.reflectionToString(pgi));
		logger.info(ToStringBuilder.reflectionToString(pgi.getTransmissionHeader()));

		final SapSynchronousRfcDestinationEndpoint endpoint = exchange.getContext().getEndpoint(
				"sap-srfc-destination:sapRFCDest:Z_KN_IDOC_INPUT_PICK_PGI", SapSynchronousRfcDestinationEndpoint.class);

		final Structure request = endpoint.createRequest();

		final Table<Structure> I_PICK = request.get("I_PICK", Table.class);

		for (PGI.DeliveryItem item : pgi.getDeliveryItem()) {

			final Structure ZKN_PICK = I_PICK.add();

			ZKN_PICK.put("VBELN", StringUtils.leftPad(pgi.getDeliveryNumber(), 10, "0"));

			final Structure ZKN_E1EDL18 = ZKN_PICK.get("ZE1EDL18", Structure.class);
			ZKN_E1EDL18.put("QUALF", pgi.getPickQualifier()); // ZKN_E1EDL18_QUALF

			final Structure ZKN_E1EDT13 = ZKN_PICK.get("ZE1EDT13", Structure.class);
			ZKN_E1EDT13.put("QUALF", pgi.getIDOCQualifierDeliveryDate());
			ZKN_E1EDT13.put("NTEND", pgi.getPostingDate());
			ZKN_E1EDT13.put("NTENZ", pgi.getPostingTime());

			final Structure ZKN_E1EDL24 = ZKN_PICK.get("ZE1EDL24", Structure.class);

			ZKN_E1EDL24.put("POSNR", StringUtils.leftPad(item.getSDItemNum(), 6, "0"));
			ZKN_E1EDL24.put("MATNR", item.getMaterialNum());
			ZKN_E1EDL24.put("WERKS", item.getPlantCode());
			ZKN_E1EDL24.put("LFIMG", item.getActualQuantityDelivered().toString());
			ZKN_E1EDL24.put("BRGEW", item.getGrossWeight().toString());
			ZKN_E1EDL24.put("NTGEW", item.getNetWeight().toString());

			final Structure ZKN_E1EDL19 = ZKN_PICK.get("ZE1EDL19", Structure.class);
			ZKN_E1EDL19.put("QUALF", pgi.getIDOCQualifier()); // ZKN_E1EDL19_QUALF
		}
		exchange.getIn().setBody(request);
	}

}
