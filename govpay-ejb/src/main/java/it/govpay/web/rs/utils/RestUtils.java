package it.govpay.web.rs.utils;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.UriInfo;

import org.apache.logging.log4j.Logger;

import it.govpay.bd.BasicBD;
import it.govpay.core.exceptions.GovPayException;
import it.govpay.web.rs.BaseRsService;
import it.govpay.web.rs.Caricatore;
import net.sf.json.JSONObject;

public class RestUtils {

	public static ByteArrayOutputStream writeGovpayErrorResponse(Caricatore c, Logger log, GovPayException response, UriInfo uriInfo, HttpHeaders httpHeaders,BasicBD bd,String methodName){
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		String nomeMetodo = "writeEstrattoContoResponse";

		try{
			log.info("Esecuzione " + nomeMetodo + " in corso...");

			JSONObject jsonObject = new JSONObject();
			jsonObject.put("esito", response.getCodEsito().name());
			jsonObject.put("descrizione", response.getMessage());

			ByteArrayInputStream bais = new ByteArrayInputStream(jsonObject.toString().getBytes());

			BaseRsService.copy(bais, baos);

			baos.flush();

			c.logResponse(uriInfo, httpHeaders, methodName, baos);
			
			log.info("Esecuzione " + nomeMetodo + " completata.");


		}catch(Exception e){
			log.info("Errore durante l'esecuzione del metodo " + nomeMetodo + ":" + e.getMessage(),e);
		}finally {
			if(baos != null)
				try {
					baos.close();
				} catch (IOException e) {
				}
		}
		return baos;
	}
	
	public static ByteArrayOutputStream writeGovpayErrorResponse(Caricatore c, Logger log, Exception response, UriInfo uriInfo, HttpHeaders httpHeaders,BasicBD bd,String methodName){
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		String nomeMetodo = "writeEstrattoContoResponse";

		try{
			log.info("Esecuzione " + nomeMetodo + " in corso...");

			JSONObject jsonObject = new JSONObject();
			jsonObject.put("esito", "KO");
			jsonObject.put("descrizione", response.getMessage());

			ByteArrayInputStream bais = new ByteArrayInputStream(jsonObject.toString().getBytes());

			BaseRsService.copy(bais, baos);

			baos.flush();

			c.logResponse(uriInfo, httpHeaders, methodName, baos);
			
			log.info("Esecuzione " + nomeMetodo + " completata.");


		}catch(Exception e){
			log.info("Errore durante l'esecuzione del metodo " + nomeMetodo + ":" + e.getMessage(),e);
		}finally {
			if(baos != null)
				try {
					baos.close();
				} catch (IOException e) {
				}
		}
		return baos;
	}
}
