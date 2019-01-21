package it.govpay.pagamento.v2.beans.converter;

import java.util.HashMap;
import java.util.Map;

import javax.ws.rs.core.UriBuilder;

import org.openspcoop2.generic_project.exception.ServiceException;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.module.jaxb.JaxbAnnotationModule;

import it.gov.digitpa.schemas._2011.pagamenti.CtRicevutaTelematica;
import it.gov.digitpa.schemas._2011.pagamenti.CtRichiestaPagamentoTelematico;
import it.govpay.bd.model.Rpt;
import it.govpay.core.utils.JaxbUtils;
import it.govpay.pagamento.v2.beans.Lista;


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
			CtRichiestaPagamentoTelematico ctRpt = JaxbUtils.toRPT(rpt.getXmlRpt(), false);
			return mapper.writeValueAsString(ctRpt);
		} catch (Exception e) {
			throw new ServiceException(e);
		}
	}

	public static String getRtJson(Rpt rpt) throws ServiceException {
		if(rpt.getXmlRt() == null)
			return null;


		try {
			CtRicevutaTelematica ctRt = JaxbUtils.toRT(rpt.getXmlRt(), false);
			return mapper.writeValueAsString(ctRt);
		} catch (Exception e) {
			throw new ServiceException(e);
		}
	}

	public static void popolaLista(Lista lista, UriBuilder requestURI, int items, int offset, int limit, long total) {
		if(offset > 0)
			lista.setFirst(requestURI.queryParam("offset", 0).build().toString());
		
		if(offset + items < total) 
			lista.setLast(requestURI.queryParam("offset", (total % limit) * limit).build().toString());
		
		if(offset + limit < total)
			lista.setNext(requestURI.queryParam("offset", offset + limit).build().toString());
		
		if(offset - limit > 0)
			lista.setPrev(requestURI.queryParam("offset",offset - limit).build().toString());
		
		lista.setOffset(offset);
		lista.setLimit(limit);
		lista.setTotal(total);
	}
}
