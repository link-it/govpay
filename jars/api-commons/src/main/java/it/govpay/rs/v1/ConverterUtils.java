package it.govpay.rs.v1;

import java.util.HashMap;
import java.util.Map;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.module.jaxb.JaxbAnnotationModule;

import it.gov.digitpa.schemas._2011.pagamenti.CtRicevutaTelematica;
import it.gov.digitpa.schemas._2011.pagamenti.CtRichiestaPagamentoTelematico;
import it.gov.pagopa.pagopa_api.pa.pafornode.PaGetPaymentRes;
import it.gov.pagopa.pagopa_api.pa.pafornode.PaSendRTReq;
import it.govpay.bd.model.Rpt;
import it.govpay.core.exceptions.IOException;
import it.govpay.core.utils.JaxbUtils;
import it.govpay.model.eventi.DettaglioRichiesta;
import it.govpay.model.eventi.DettaglioRisposta;

public class ConverterUtils {

	private static Map<String, String> map;
	private static ObjectMapper mapper;
	static {
		map = new HashMap<>();
		map.put("http://www.digitpa.gov.it/schemas/2011/Pagamenti/", "");
		mapper = new ObjectMapper();
		mapper.registerModule(new JaxbAnnotationModule());
	}

	public static String getRptJson(Rpt rpt) throws IOException {
		if(rpt.getXmlRpt() == null)
			return null;

		try {
			switch (rpt.getVersione()) {
			case SANP_230:
				CtRichiestaPagamentoTelematico ctRpt = JaxbUtils.toRPT(rpt.getXmlRpt(), false);
				return mapper.writeValueAsString(ctRpt);
			case SANP_240:
				PaGetPaymentRes paGetPaymentRes_RPT = JaxbUtils.toPaGetPaymentRes_RPT(rpt.getXmlRpt(), false);
				return mapper.writeValueAsString(paGetPaymentRes_RPT.getData());
			}
			
			CtRichiestaPagamentoTelematico ctRpt = JaxbUtils.toRPT(rpt.getXmlRpt(), false);
			return mapper.writeValueAsString(ctRpt);
		} catch (Exception e) {
			throw new IOException(e);
		}
	}
	
	public static String getRptJson(CtRichiestaPagamentoTelematico ctRpt) throws IOException {
		if(ctRpt == null)
			return null;

		try {
			return mapper.writeValueAsString(ctRpt);
		} catch (Exception e) {
			throw new IOException(e);
		}
	}
	
	public static String getRptJson(PaGetPaymentRes paGetPaymentRes_RPT) throws IOException {
		if(paGetPaymentRes_RPT == null)
			return null;

		try {
			return mapper.writeValueAsString(paGetPaymentRes_RPT);
		} catch (Exception e) {
			throw new IOException(e);
		}
	}

	public static String getRtJson(Rpt rpt) throws IOException {
		if(rpt.getXmlRt() == null)
			return null;


		try {
			switch (rpt.getVersione()) {
			case SANP_230:
				CtRicevutaTelematica ctRt = JaxbUtils.toRT(rpt.getXmlRt(), false);
				return mapper.writeValueAsString(ctRt);
			case SANP_240:
				PaSendRTReq paSendRTReq_RT = JaxbUtils.toPaSendRTReq_RT(rpt.getXmlRt(), false);
				return mapper.writeValueAsString(paSendRTReq_RT.getReceipt());
			}
			
			CtRicevutaTelematica ctRt = JaxbUtils.toRT(rpt.getXmlRt(), false);
			return mapper.writeValueAsString(ctRt);
		} catch (Exception e) {
			throw new IOException(e);
		}
	}
	
	public static String getRtJson(CtRicevutaTelematica ctRt ) throws IOException {
		if(ctRt == null)
			return null;

		try {
			return mapper.writeValueAsString(ctRt);
		} catch (Exception e) {
			throw new IOException(e);
		}
	}
	
	public static String getParametriRichiestaEvento(DettaglioRichiesta dettaglioRichiesta) throws IOException {
		if(dettaglioRichiesta == null)
			return null;

		try {
			return mapper.writeValueAsString(dettaglioRichiesta);
		} catch (Exception e) {
			throw new IOException(e);
		}
	}
	
	public static String getParametriRispostaEvento(DettaglioRisposta dettaglioRisposta) throws IOException {
		if(dettaglioRisposta == null)
			return null;

		try {
			return mapper.writeValueAsString(dettaglioRisposta);
		} catch (Exception e) {
			throw new IOException(e);
		}
	}
	
}
