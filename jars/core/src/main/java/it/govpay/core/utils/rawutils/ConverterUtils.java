package it.govpay.core.utils.rawutils;

import java.util.HashMap;
import java.util.Map;

import org.openspcoop2.generic_project.exception.ServiceException;

import com.fasterxml.jackson.databind.ObjectMapper;

import it.gov.digitpa.schemas._2011.pagamenti.CtRicevutaTelematica;
import it.gov.digitpa.schemas._2011.pagamenti.CtRichiestaPagamentoTelematico;
import it.govpay.bd.model.Rpt;
import it.govpay.core.utils.JaxbUtils;

public class ConverterUtils {

	private static Map<String, String> map;
	private static ObjectMapper mapper;
	static {
		map = new HashMap<>();
		map.put("http://www.digitpa.gov.it/schemas/2011/Pagamenti/", "");
		mapper = new ObjectMapper();
	}

	public static String getRptJson(Rpt rpt) throws ServiceException {
		if(rpt.getXmlRpt() == null)
			return null;

		try {
			CtRichiestaPagamentoTelematico ctRpt = JaxbUtils.toRPT(rpt.getXmlRpt(), false);
			return mapper.writeValueAsString(ctRpt)
					.replaceAll("emailPagatore", "e-mailPagatore")
					.replaceAll("emailVersante", "e-mailVersante")
					.replaceAll("N_A", "N/A");
		} catch (Exception e) {
			throw new ServiceException(e);
		}
	}

	public static String getRtJson(Rpt rpt) throws ServiceException {
		if(rpt.getXmlRt() == null)
			return null;


		try {
			CtRicevutaTelematica ctRt = JaxbUtils.toRT(rpt.getXmlRt(), false);
			return mapper.writeValueAsString(ctRt)
					.replaceAll("emailPagatore", "e-mailPagatore")
					.replaceAll("emailVersante", "e-mailVersante")
					.replaceAll("N_A", "N/A");
		} catch (Exception e) {
			throw new ServiceException(e);
		}
	}
}
