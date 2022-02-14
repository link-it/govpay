package it.govpay.rs.v1;

import java.util.HashMap;
import java.util.Map;

import org.openspcoop2.generic_project.exception.ServiceException;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.module.jaxb.JaxbAnnotationModule;

import it.gov.digitpa.schemas._2011.pagamenti.CtRicevutaTelematica;
import it.gov.digitpa.schemas._2011.pagamenti.CtRichiestaPagamentoTelematico;
import it.gov.pagopa.pagopa_api.pa.pafornode.PaGetPaymentRes;
import it.gov.pagopa.pagopa_api.pa.pafornode.PaSendRTReq;
import it.govpay.bd.model.Rpt;
import it.govpay.bd.model.eventi.DettaglioRichiesta;
import it.govpay.bd.model.eventi.DettaglioRisposta;
import it.govpay.core.utils.JaxbUtils;

public class ConverterUtils {

	private static Map<String, String> map;
	private static ObjectMapper mapper;
	static {
		map = new HashMap<>();
		map.put("http://www.digitpa.gov.it/schemas/2011/Pagamenti/", "");
		mapper = new ObjectMapper();
		mapper.registerModule(new JaxbAnnotationModule());
	}

	public static String getRptJson(Rpt rpt) throws ServiceException {
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
			throw new ServiceException(e);
		}
	}
	
	public static String getRptJson(CtRichiestaPagamentoTelematico ctRpt) throws ServiceException {
		if(ctRpt == null)
			return null;

		try {
			return mapper.writeValueAsString(ctRpt);
		} catch (Exception e) {
			throw new ServiceException(e);
		}
	}
	
	public static String getRptJson(PaGetPaymentRes paGetPaymentRes_RPT) throws ServiceException {
		if(paGetPaymentRes_RPT == null)
			return null;

		try {
			return mapper.writeValueAsString(paGetPaymentRes_RPT);
		} catch (Exception e) {
			throw new ServiceException(e);
		}
	}

	public static String getRtJson(Rpt rpt) throws ServiceException {
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
			throw new ServiceException(e);
		}
	}
	
	public static String getRtJson(CtRicevutaTelematica ctRt ) throws ServiceException {
		if(ctRt == null)
			return null;

		try {
			return mapper.writeValueAsString(ctRt);
		} catch (Exception e) {
			throw new ServiceException(e);
		}
	}
	
	public static String getParametriRichiestaEvento(DettaglioRichiesta dettaglioRichiesta) throws ServiceException {
		if(dettaglioRichiesta == null)
			return null;

		try {
			return mapper.writeValueAsString(dettaglioRichiesta);
		} catch (Exception e) {
			throw new ServiceException(e);
		}
	}
	
	public static String getParametriRispostaEvento(DettaglioRisposta dettaglioRisposta) throws ServiceException {
		if(dettaglioRisposta == null)
			return null;

		try {
			return mapper.writeValueAsString(dettaglioRisposta);
		} catch (Exception e) {
			throw new ServiceException(e);
		}
	}
	
}
