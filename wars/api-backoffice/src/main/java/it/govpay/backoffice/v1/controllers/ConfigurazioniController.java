package it.govpay.backoffice.v1.controllers;

import java.io.ByteArrayOutputStream;
import java.text.MessageFormat;
import java.util.Arrays;

import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;
import javax.ws.rs.core.UriInfo;

import org.apache.commons.io.IOUtils;
import org.slf4j.Logger;
import org.springframework.security.core.Authentication;

import it.govpay.backoffice.v1.beans.Giornale;
import it.govpay.backoffice.v1.beans.converter.GiornaleConverter;
import it.govpay.core.beans.JSONSerializable;
import it.govpay.core.dao.configurazione.ConfigurazioneDAO;
import it.govpay.core.dao.configurazione.dto.LeggiConfigurazioneDTO;
import it.govpay.core.dao.configurazione.dto.LeggiConfigurazioneDTOResponse;
import it.govpay.core.dao.configurazione.dto.PutConfigurazioneDTO;
import it.govpay.core.dao.configurazione.dto.PutConfigurazioneDTOResponse;
import it.govpay.model.Acl.Diritti;
import it.govpay.model.Acl.Servizio;
import it.govpay.model.Utenza.TIPO_UTENZA;


public class ConfigurazioniController extends BaseController {

     public ConfigurazioniController(String nomeServizio,Logger log) {
		super(nomeServizio,log);
     }



    public Response configurazioniGiornaleGET(Authentication user, UriInfo uriInfo, HttpHeaders httpHeaders ) {
    	String methodName = "configurazioniGiornaleGET";  
		String transactionId = this.context.getTransactionId();
		this.log.debug(MessageFormat.format(BaseController.LOG_MSG_ESECUZIONE_METODO_IN_CORSO, methodName)); 
		try{
			// autorizzazione sulla API
			this.isAuthorized(user, Arrays.asList(TIPO_UTENZA.OPERATORE, TIPO_UTENZA.APPLICAZIONE), Arrays.asList(Servizio.CONFIGURAZIONE_E_MANUTENZIONE), Arrays.asList(Diritti.LETTURA));

			LeggiConfigurazioneDTO leggiConfigurazioneDTO = new LeggiConfigurazioneDTO(user);
			ConfigurazioneDAO configurazioneDAO = new ConfigurazioneDAO(false);

			LeggiConfigurazioneDTOResponse configurazioneDTOResponse = configurazioneDAO.getConfigurazione(leggiConfigurazioneDTO);

			Giornale response = GiornaleConverter.toRsModel(configurazioneDTOResponse.getConfigurazione().getGiornale());
			this.log.debug(MessageFormat.format(BaseController.LOG_MSG_ESECUZIONE_METODO_COMPLETATA, methodName)); 
			return this.handleResponseOk(Response.status(Status.OK).entity(response.toJSON(null)),transactionId).header(this.transactionIdHeaderName, transactionId).build();

		}catch (Exception e) {
			return this.handleException(uriInfo, httpHeaders, methodName, e, transactionId);
		} finally {
			this.log(this.context);
		}
    }

    public Response configurazioniGiornalePOST(Authentication user, UriInfo uriInfo, HttpHeaders httpHeaders , java.io.InputStream is) {
    	String methodName = "configurazioniGiornalePOST";  
		String transactionId = this.context.getTransactionId();
		this.log.debug(MessageFormat.format(BaseController.LOG_MSG_ESECUZIONE_METODO_IN_CORSO, methodName)); 
		try(ByteArrayOutputStream baos= new ByteArrayOutputStream();){
			// salvo il json ricevuto
			IOUtils.copy(is, baos);

			// autorizzazione sulla API
			this.isAuthorized(user, Arrays.asList(TIPO_UTENZA.OPERATORE, TIPO_UTENZA.APPLICAZIONE), Arrays.asList(Servizio.CONFIGURAZIONE_E_MANUTENZIONE), Arrays.asList(Diritti.SCRITTURA));

			String jsonRequest = baos.toString();
			Giornale giornaleRequest= JSONSerializable.parse(jsonRequest, Giornale.class);

			giornaleRequest.validate();

			PutConfigurazioneDTO putConfigurazioneDTO = GiornaleConverter.getPutConfigurazioneDTO(giornaleRequest, user); 

			ConfigurazioneDAO configurazioneDAO = new ConfigurazioneDAO(false);

			PutConfigurazioneDTOResponse putConfigurazioneDTOResponse = configurazioneDAO.salvaConfigurazioneGiornaleEventi(putConfigurazioneDTO);
			Status responseStatus = putConfigurazioneDTOResponse.isCreated() ?  Status.CREATED : Status.OK;

			this.log.debug(MessageFormat.format(BaseController.LOG_MSG_ESECUZIONE_METODO_COMPLETATA, methodName)); 
			return this.handleResponseOk(Response.status(responseStatus),transactionId).build();
		}catch (Exception e) {
			return this.handleException(uriInfo, httpHeaders, methodName, e, transactionId);
		} finally {
			this.log(this.context);
		}
    }
}


