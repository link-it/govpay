package it.govpay.backoffice.v1.controllers;


import java.text.MessageFormat;
import java.util.List;

import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;
import javax.ws.rs.core.StreamingOutput;
import javax.ws.rs.core.UriInfo;

import org.openspcoop2.utils.service.context.ContextThreadLocal;
import org.slf4j.Logger;
import org.springframework.security.core.Authentication;

import it.govpay.bd.model.TracciatoNotificaPagamenti;
import it.govpay.core.dao.commons.exception.NonTrovataException;
import it.govpay.core.dao.pagamenti.TracciatiNotificaPagamentiDAO;
import it.govpay.core.dao.pagamenti.dto.LeggiTracciatoNotificaPagamentiDTO;
import it.govpay.model.TracciatoNotificaPagamenti.STATO_ELABORAZIONE;



public class TracciatiNotificaPagamentiController extends BaseController {

     public TracciatiNotificaPagamentiController(String nomeServizio,Logger log) {
		super(nomeServizio,log);
     }



    public Response getTracciatoNotificaPagamenti(Authentication user, UriInfo uriInfo, HttpHeaders httpHeaders , Long id, String secID) {
    	String methodName = "getTracciatoNotificaPagamenti";  
		String transactionId = ContextThreadLocal.get().getTransactionId();
		this.log.debug(MessageFormat.format(BaseController.LOG_MSG_ESECUZIONE_METODO_IN_CORSO, methodName)); 
//		BDConfigWrapper configWrapper = new BDConfigWrapper(ContextThreadLocal.get().getTransactionId(), true);
		try{
			// l'accesso a questa risorsa e' libero 
			// autorizzazione sulla API 
//			this.isAuthorized(user, Arrays.asList(TIPO_UTENZA.OPERATORE, TIPO_UTENZA.APPLICAZIONE), Arrays.asList(Servizio.PENDENZE), Arrays.asList(Diritti.LETTURA));

			LeggiTracciatoNotificaPagamentiDTO leggiTracciatoDTO = new LeggiTracciatoNotificaPagamentiDTO(user);
			leggiTracciatoDTO.setId(id);
			leggiTracciatoDTO.setIdentificativo(secID);
			leggiTracciatoDTO.setIncludiRaw(false);
			
			List<Long> idDomini = null;
//			List<Long> idDomini = AuthorizationManager.getIdDominiAutorizzati(user);
//			if(idDomini == null) {
//				throw AuthorizationManager.toNotAuthorizedExceptionNessunDominioAutorizzato(user);
//			}

			TracciatiNotificaPagamentiDAO tracciatiDAO = new TracciatiNotificaPagamentiDAO();
			TracciatoNotificaPagamenti tracciato = tracciatiDAO.leggiTracciato(leggiTracciatoDTO);
			
			if(!(tracciato.getStato().equals(STATO_ELABORAZIONE.FILE_CARICATO) || tracciato.getStato().equals(STATO_ELABORAZIONE.FILE_NUOVO)))
				throw new NonTrovataException("Il tracciato richiesto non e' disponibile: elaborazione ancora in corso");
			
			// check dominio
//			if(!AuthorizationManager.isDominioAuthorized(leggiTracciatoDTO.getUser(), tracciato.getDominio(configWrapper).getCodDominio())) {
//				throw AuthorizationManager.toNotAuthorizedException(leggiTracciatoDTO.getUser(), tracciato.getDominio(configWrapper).getCodDominio(),null);
//			}

			String zipFileName = (tracciato.getNomeFile().contains(".") ? tracciato.getNomeFile().substring(0, tracciato.getNomeFile().lastIndexOf(".")) : tracciato.getNomeFile()) + ".zip";

			StreamingOutput zipStream = tracciatiDAO.leggiBlobTracciato(id, secID, idDomini, it.govpay.orm.TracciatoNotificaPagamenti.model().RAW_CONTENUTO);
			
			this.log.debug(MessageFormat.format(BaseController.LOG_MSG_ESECUZIONE_METODO_COMPLETATA, methodName)); 
			return this.handleResponseOk(Response.status(Status.OK).type(MediaType.APPLICATION_OCTET_STREAM).entity(zipStream).header("content-disposition", "attachment; filename=\""+zipFileName+"\""),transactionId).build();
		}catch (Exception e) {
			return this.handleException(uriInfo, httpHeaders, methodName, e, transactionId);
		} finally {
			this.log(ContextThreadLocal.get());
		}
    }


}


