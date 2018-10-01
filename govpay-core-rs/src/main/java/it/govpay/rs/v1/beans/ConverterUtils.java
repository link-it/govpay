package it.govpay.rs.v1.beans;

import java.io.ByteArrayOutputStream;
import java.util.HashMap;
import java.util.Map;

import org.openspcoop2.generic_project.exception.ServiceException;
import org.openspcoop2.utils.serialization.JsonJacksonSerializer;
import org.openspcoop2.utils.xml2json.Xml2JsonFactory;

import it.gov.digitpa.schemas._2011.pagamenti.CtRicevutaTelematica;
import it.gov.digitpa.schemas._2011.pagamenti.CtRichiestaPagamentoTelematico;
import it.govpay.bd.model.Rpt;
import it.govpay.core.utils.JaxbUtils;

public class ConverterUtils {
	
	private static Map<String, String> map;
	private static JsonJacksonSerializer serializer; 
	
	static {
		map = new HashMap<>();
		map.put("http://www.digitpa.gov.it/schemas/2011/Pagamenti/", "");
		serializer = new JsonJacksonSerializer();
	}
	
	public static String getRptJson(Rpt rpt) throws ServiceException {
		if(rpt.getXmlRpt() == null)
			return null;
		
		try {
			CtRichiestaPagamentoTelematico ctRpt = JaxbUtils.toRPT(rpt.getXmlRpt(), false);
			ByteArrayOutputStream baos = new ByteArrayOutputStream();
			serializer.writeObject(ctRpt, baos);		
			return baos.toString();
		} catch (Exception e) {
			try {
				String s = Xml2JsonFactory.getXml2JsonMapped(map).xml2json(new String(rpt.getXmlRpt()));
				return s.substring(7, s.length() - 1);
			} catch (Exception ee) {
				throw new ServiceException(ee);
			}
		}
	}
	
	public static String getRtJson(Rpt rpt) throws ServiceException {
		if(rpt.getXmlRt() == null)
			return null;
		
		
		try {
			CtRicevutaTelematica ctRt = JaxbUtils.toRT(rpt.getXmlRt(), false);
			ByteArrayOutputStream baos = new ByteArrayOutputStream();
			serializer.writeObject(ctRt, baos);		
			return baos.toString();
		} catch (Exception e) {
			try {
				String s = Xml2JsonFactory.getXml2JsonMapped(map).xml2json(new String(rpt.getXmlRt()));
				return s.substring(6, s.length() - 1);
			} catch (Exception ee) {
				throw new ServiceException(ee);
			}
		}
	}

}
