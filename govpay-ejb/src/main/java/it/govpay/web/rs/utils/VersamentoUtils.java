package it.govpay.web.rs.utils;

import it.govpay.web.rs.BaseRsService;
import it.govpay.web.rs.model.Versamento;
import it.govpay.web.rs.model.VersamentoResponse;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;

import javax.ws.rs.WebApplicationException;

import net.sf.json.JSONObject;
import net.sf.json.JsonConfig;

import org.apache.commons.lang.time.DateUtils;
import org.apache.logging.log4j.Logger;

public class VersamentoUtils {

	public static Versamento readVersamentoFromRequest(Logger log,InputStream is, String methodName) throws WebApplicationException,Exception{
		String nomeMetodo = "readVersamentoFromRequest";
		Versamento entry = null;
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		try{
			log.info("Esecuzione " + nomeMetodo + " in corso...");

			JsonConfig jsonConfig = new JsonConfig();
		
			BaseRsService.copy(is, baos);

			baos.flush();

			JSONObject jsonObject = JSONObject.fromObject( baos.toString() );  
			jsonConfig.setRootClass(Versamento.class);
			entry = (Versamento) JSONObject.toBean( jsonObject, jsonConfig );
			
			String []datPat = PagamentoUtils.datePatterns.toArray(new String[PagamentoUtils.datePatterns.size()]);
			if(jsonObject.containsKey("dataScadenza") && jsonObject.getString("dataScadenza") != null)
				entry.setDataScadenza(DateUtils.parseDate(jsonObject.getString("dataScadenza"), datPat));

			log.debug("Lettura del versamento dall'inputStream completata.");

			log.info("Esecuzione " + nomeMetodo + " completata.");
			return entry;
		}catch(WebApplicationException e){
			throw e;
		}catch(Exception e){
			throw e;
		}finally {
			if(baos != null)
				baos.close();
		}
	}


	public static ByteArrayOutputStream writeVersamentoResponse(Logger log, VersamentoResponse response,String methodName) throws Exception{
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		String nomeMetodo = "writeVersamentoResponse";

		try{
			log.info("Esecuzione " + nomeMetodo + " in corso...");

			JsonConfig jsonConfig = new JsonConfig();
			jsonConfig.setRootClass(VersamentoResponse.class);
			
			JSONObject jsonObject = JSONObject.fromObject( response , jsonConfig);  

			ByteArrayInputStream bais = new ByteArrayInputStream(jsonObject.toString().getBytes());

			BaseRsService.copy(bais, baos);

			baos.flush();

			log.info("Esecuzione " + nomeMetodo + " completata.");
			return baos;

		}catch(Exception e){
			throw e;
		}finally {
			if(baos != null)
				baos.close();
		}
	}

}
