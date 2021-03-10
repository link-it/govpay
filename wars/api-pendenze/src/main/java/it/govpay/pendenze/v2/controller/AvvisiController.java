package it.govpay.pendenze.v2.controller;


import java.text.MessageFormat;
import java.util.Arrays;

import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;
import javax.ws.rs.core.UriInfo;

import org.apache.commons.lang.ArrayUtils;
import org.openspcoop2.utils.json.ValidationException;
import org.openspcoop2.utils.service.context.ContextThreadLocal;
import org.slf4j.Logger;
import org.springframework.security.core.Authentication;

import it.govpay.core.autorizzazione.AuthorizationManager;
import it.govpay.core.autorizzazione.utils.AutorizzazioneUtils;
import it.govpay.core.dao.anagrafica.dto.GetAvvisoDTO;
import it.govpay.core.dao.anagrafica.dto.GetAvvisoDTOResponse;
import it.govpay.core.dao.anagrafica.dto.GetAvvisoDTO.FormatoAvviso;
import it.govpay.core.dao.pagamenti.AvvisiDAO;
import it.govpay.core.dao.pagamenti.exception.PendenzaNonTrovataException;
import it.govpay.core.exceptions.NotAuthorizedException;
import it.govpay.core.utils.GpContext;
import it.govpay.core.utils.validator.ValidatoreIdentificativi;
import it.govpay.model.Acl.Diritti;
import it.govpay.model.Acl.Servizio;
import it.govpay.model.Utenza.TIPO_UTENZA;
import it.govpay.pendenze.v2.beans.Avviso;
import it.govpay.pendenze.v2.beans.LinguaSecondaria;
import it.govpay.pendenze.v2.beans.StatoAvviso;
import it.govpay.pendenze.v2.beans.converter.PendenzeConverter;

public class AvvisiController extends BaseController {

     public AvvisiController(String nomeServizio,Logger log) {
		super(nomeServizio,log);
     }



    public Response getAvviso(Authentication user, UriInfo uriInfo, HttpHeaders httpHeaders , String idDominio, String numeroAvviso, String linguaSecondaria) {
    	String methodName = "avvisiIdDominioIuvGET";  
		String transactionId = ContextThreadLocal.get().getTransactionId();

		this.log.debug(MessageFormat.format(BaseController.LOG_MSG_ESECUZIONE_METODO_IN_CORSO, methodName)); 

		try{
			// autorizzazione sulla API
			this.isAuthorized(user, Arrays.asList(TIPO_UTENZA.APPLICAZIONE), Arrays.asList(Servizio.API_PENDENZE), Arrays.asList(Diritti.LETTURA));
			
			ValidatoreIdentificativi validatoreId = ValidatoreIdentificativi.newInstance();
			validatoreId.validaIdDominio("idDominio", idDominio);

			GetAvvisoDTO getAvvisoDTO = new GetAvvisoDTO(user, idDominio, numeroAvviso);
			
			String accept = MediaType.APPLICATION_JSON;
			if(httpHeaders.getRequestHeaders().containsKey("Accept")) {
				accept = httpHeaders.getRequestHeaders().get("Accept").get(0).toLowerCase();
			}
			
//			if(!AuthorizationManager.isDominioAuthorized(getAvvisoDTO.getUser(), getAvvisoDTO.getCodDominio())) {
//				throw AuthorizationManager.toNotAuthorizedException(getAvvisoDTO.getUser(), getAvvisoDTO.getCodDominio(),null);
//			}
			
			if(linguaSecondaria != null) {
				LinguaSecondaria linguaSecondariaEnum = LinguaSecondaria.fromValue(linguaSecondaria);
				if(linguaSecondariaEnum != null) {
					switch(linguaSecondariaEnum) {
					case DE:
						getAvvisoDTO.setLinguaSecondariaAbilitata(true);
						getAvvisoDTO.setLinguaSecondaria(it.govpay.core.business.model.PrintAvvisoVersamentoDTO.LinguaSecondaria.DE);
						break;
					case EN:
						getAvvisoDTO.setLinguaSecondariaAbilitata(true);
						getAvvisoDTO.setLinguaSecondaria(it.govpay.core.business.model.PrintAvvisoVersamentoDTO.LinguaSecondaria.EN);
						break;
					case FALSE:
						getAvvisoDTO.setLinguaSecondariaAbilitata(false);
						getAvvisoDTO.setLinguaSecondaria(null); 
						break;
					case FR:
						getAvvisoDTO.setLinguaSecondariaAbilitata(true);
						getAvvisoDTO.setLinguaSecondaria(it.govpay.core.business.model.PrintAvvisoVersamentoDTO.LinguaSecondaria.FR);
						break;
					case SL:
						getAvvisoDTO.setLinguaSecondariaAbilitata(true);
						getAvvisoDTO.setLinguaSecondaria(it.govpay.core.business.model.PrintAvvisoVersamentoDTO.LinguaSecondaria.SL);
						break;
					}				
				} else {
					throw new ValidationException("Codifica inesistente per linguaSecondaria. Valore fornito [" + linguaSecondaria + "] valori possibili " + ArrayUtils.toString(LinguaSecondaria.values()));
				}
			}
			
			AvvisiDAO avvisiDAO = new AvvisiDAO();
			
			if(accept.toLowerCase().contains("application/pdf")) {
				getAvvisoDTO.setFormato(FormatoAvviso.PDF);
				GetAvvisoDTOResponse getAvvisoDTOResponse = avvisiDAO.getAvviso(getAvvisoDTO);
				
				((GpContext) (ContextThreadLocal.get()).getApplicationContext()).getEventoCtx().setIdPendenza(getAvvisoDTOResponse.getVersamento().getCodVersamentoEnte());
				((GpContext) (ContextThreadLocal.get()).getApplicationContext()).getEventoCtx().setIdA2A(getAvvisoDTOResponse.getApplicazione().getCodApplicazione());
				
				// filtro sull'applicazione			
				if(!AutorizzazioneUtils.getAuthenticationDetails(user).getApplicazione().getCodApplicazione().equals(getAvvisoDTOResponse.getApplicazione().getCodApplicazione())) {
					throw AuthorizationManager.toNotAuthorizedException(user);
				}
				
				this.log.debug(MessageFormat.format(BaseController.LOG_MSG_ESECUZIONE_METODO_COMPLETATA, methodName)); 
				return this.handleResponseOk(Response.status(Status.OK).type("application/pdf").entity(getAvvisoDTOResponse.getAvvisoPdf()).header("content-disposition", "attachment; filename=\""+getAvvisoDTOResponse.getFilenameAvviso()+"\""),transactionId).build();
			} else if(accept.toLowerCase().contains("application/json")) {
				getAvvisoDTO.setFormato(FormatoAvviso.JSON);
				Avviso avviso = null;
				try {
					GetAvvisoDTOResponse getAvvisoDTOResponse = avvisiDAO.getAvviso(getAvvisoDTO);
					
					((GpContext) (ContextThreadLocal.get()).getApplicationContext()).getEventoCtx().setIdPendenza(getAvvisoDTOResponse.getVersamento().getCodVersamentoEnte());
					((GpContext) (ContextThreadLocal.get()).getApplicationContext()).getEventoCtx().setIdA2A(getAvvisoDTOResponse.getApplicazione().getCodApplicazione());
					
					// filtro sull'applicazione			
					if(!AutorizzazioneUtils.getAuthenticationDetails(user).getApplicazione().getCodApplicazione().equals(getAvvisoDTOResponse.getApplicazione().getCodApplicazione())) {
						throw AuthorizationManager.toNotAuthorizedException(user);
					}
					
					avviso = PendenzeConverter.toAvvisoRsModel(getAvvisoDTOResponse.getVersamento(), getAvvisoDTOResponse.getDominio(), getAvvisoDTOResponse.getBarCode(), getAvvisoDTOResponse.getQrCode());
				} catch (PendenzaNonTrovataException pnte) {
					avviso = new Avviso();
					avviso.setStato(StatoAvviso.SCONOSCIUTA);
					avviso.setNumeroAvviso(numeroAvviso);
					avviso.setIdDominio(idDominio);
				}
				this.log.debug(MessageFormat.format(BaseController.LOG_MSG_ESECUZIONE_METODO_COMPLETATA, methodName)); 
				return this.handleResponseOk(Response.status(Status.OK).entity(avviso.toJSON(null)),transactionId).build();
			} else {
				// formato non accettato
				throw new NotAuthorizedException("Avviso di pagamento non disponibile nel formato richiesto");
			}
		}catch (Exception e) {
			return this.handleException(uriInfo, httpHeaders, methodName, e, transactionId);
		} finally {
			this.log(ContextThreadLocal.get());
		}
    }


}


