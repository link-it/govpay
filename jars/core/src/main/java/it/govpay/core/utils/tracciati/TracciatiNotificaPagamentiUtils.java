package it.govpay.core.utils.tracciati;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.security.SecureRandom;
import java.util.Base64;

import org.openspcoop2.utils.resources.Charset;

import it.govpay.bd.model.TracciatoNotificaPagamenti;
import it.govpay.core.business.TracciatiNotificaPagamenti;
import it.govpay.model.ConnettoreNotificaPagamenti;

public class TracciatiNotificaPagamentiUtils {
	
	private static final SecureRandom random = new SecureRandom();
    private static final Base64.Encoder encoder = Base64.getUrlEncoder().withoutPadding();

    public static String generaIdentificativoTracciato() {
        byte[] buffer = new byte[20];
        random.nextBytes(buffer);
        return encoder.encodeToString(buffer);
    }

	
	public static String encode(String value) {
		try {
			return URLEncoder.encode(value, Charset.UTF_8.getValue());
		} catch (UnsupportedEncodingException e) {
			return "";
		}
	}
	
	public static String getURLDownloadTracciato(ConnettoreNotificaPagamenti connettore, TracciatoNotificaPagamenti tracciato) {
		StringBuilder sb = new StringBuilder();
		
		sb.append(connettore.getDownloadBaseURL());
		
		if(!connettore.getDownloadBaseURL().endsWith("/"))
			sb.append("/");
		
		sb.append(tracciato.getId());
		
		sb.append("?").append("secID").append("=").append(tracciato.getIdentificativo());
		
		return sb.toString();
		
	}
	
	
	public static String creaNomeEntryFlussoRendicontazione(String idFlusso, String dataFlussoS) {
//		return TracciatiNotificaPagamenti.FLUSSI_RENDICONTAZIONE_DIR_PREFIX+idFlusso+".xml";
		return TracciatiNotificaPagamenti.FLUSSI_RENDICONTAZIONE_DIR_PREFIX+idFlusso+"_"+dataFlussoS+".xml";
	}

	public static String creaNomeEntryRT(String idDominio, String iuv, String ccp) {
		return TracciatiNotificaPagamenti.FILE_RT_DIR_PREFIX+idDominio +"_"+ iuv + "_"+ ccp +TracciatiNotificaPagamenti.SUFFIX_FILE_RT_XML;
	}
	
	public static String creaNomeEntryRPT(String idDominio, String iuv, String ccp) {
		return TracciatiNotificaPagamenti.FILE_RT_DIR_PREFIX+idDominio +"_"+ iuv + "_"+ ccp +TracciatiNotificaPagamenti.SUFFIX_FILE_RPT_XML;
	}
	
	
	public static String creaPathFlussoRendicontazione(String fileName) {
		if(fileName.startsWith(TracciatiNotificaPagamenti.FLUSSI_RENDICONTAZIONE_DIR_PREFIX)) {
			fileName = fileName.substring(TracciatiNotificaPagamenti.FLUSSI_RENDICONTAZIONE_DIR_PREFIX.length());
		}
		
		if(fileName.endsWith(".xml")) {
			fileName = fileName.substring(0, fileName.lastIndexOf(".xml"));
		}
		
		fileName = fileName.substring(0, fileName.lastIndexOf("_"));
		
		return fileName;
	}
	
	public static String getDataFlussoRendicontazione(String fileName) {
		if(fileName.startsWith(TracciatiNotificaPagamenti.FLUSSI_RENDICONTAZIONE_DIR_PREFIX)) {
			fileName = fileName.substring(TracciatiNotificaPagamenti.FLUSSI_RENDICONTAZIONE_DIR_PREFIX.length());
		}
		
		if(fileName.endsWith(".xml")) {
			fileName = fileName.substring(0, fileName.lastIndexOf(".xml"));
		}
		
		fileName = fileName.substring(fileName.lastIndexOf("_") + 1);
		
		return fileName;
	}

	public static String creaPathRT(String fileName) {
		if(fileName.startsWith(TracciatiNotificaPagamenti.FILE_RT_DIR_PREFIX)) {
			fileName = fileName.substring(TracciatiNotificaPagamenti.FILE_RT_DIR_PREFIX.length());
		}
		
		if(fileName.endsWith(TracciatiNotificaPagamenti.SUFFIX_FILE_RT_XML)) {
			fileName = fileName.substring(0, fileName.lastIndexOf(TracciatiNotificaPagamenti.SUFFIX_FILE_RT_XML));
		}
		
		return fileName.replace('_', '/');
	}
	
	public static String creaPathRPT(String fileName) {
		if(fileName.startsWith(TracciatiNotificaPagamenti.FILE_RT_DIR_PREFIX)) {
			fileName = fileName.substring(TracciatiNotificaPagamenti.FILE_RT_DIR_PREFIX.length());
		}
		
		if(fileName.endsWith(TracciatiNotificaPagamenti.SUFFIX_FILE_RPT_XML)) {
			fileName = fileName.substring(0, fileName.lastIndexOf(TracciatiNotificaPagamenti.SUFFIX_FILE_RPT_XML));
		}
		
		return fileName.replace('_', '/');
	}
	
	public static boolean isRPT(String fileName) {
		return (fileName.endsWith(TracciatiNotificaPagamenti.SUFFIX_FILE_RPT_XML));
	}
	
	public static boolean isRT(String fileName) {
		return (fileName.endsWith(TracciatiNotificaPagamenti.SUFFIX_FILE_RT_XML));
	}
	
}
