package it.govpay.rs.v1.beans;

import java.util.HashMap;
import java.util.Map;

import org.openspcoop2.generic_project.exception.ServiceException;
import org.openspcoop2.utils.xml2json.Xml2JsonFactory;

import it.govpay.bd.model.Rpt;

public class ConverterUtils {
	
	private static Map<String, String> map;
	
	static {
		map = new HashMap<String, String>();
		map.put("http://www.digitpa.gov.it/schemas/2011/Pagamenti/", "");
	}
	
	public static String getRptJson(Rpt rpt) throws ServiceException {
		if(rpt.getXmlRpt() == null)
			return null;
		try {
			String s = Xml2JsonFactory.getXml2JsonMapped(map).xml2json(new String(rpt.getXmlRpt()));
			return s.substring(7, s.length() - 1);
		} catch (Exception e) {
			throw new ServiceException(e);
		}
	}
	
	public static String getRtJson(Rpt rpt) throws ServiceException {
		if(rpt.getXmlRt() == null)
			return null;
		try {
			String s = Xml2JsonFactory.getXml2JsonMapped(map).xml2json(new String(rpt.getXmlRt()));
			return s.substring(6, s.length() - 1);
		} catch (Exception e) {
			throw new ServiceException(e);
		}
	}

}
