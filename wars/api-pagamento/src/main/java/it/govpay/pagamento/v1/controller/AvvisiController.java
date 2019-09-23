package it.govpay.pagamento.v1.controller;

import java.text.MessageFormat;
import java.util.Arrays;

import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;
import javax.ws.rs.core.UriInfo;

import org.slf4j.Logger;
import org.springframework.security.core.Authentication;

import it.govpay.core.autorizzazione.AuthorizationManager;
import it.govpay.core.dao.anagrafica.dto.GetAvvisoDTO;
import it.govpay.core.dao.anagrafica.dto.GetAvvisoDTO.FormatoAvviso;
import it.govpay.core.dao.anagrafica.dto.GetAvvisoDTOResponse;
import it.govpay.core.dao.pagamenti.AvvisiDAO;
import it.govpay.core.dao.pagamenti.exception.PendenzaNonTrovataException;
import it.govpay.core.exceptions.NotAuthorizedException;
import it.govpay.core.utils.validator.ValidatoreIdentificativi;
import it.govpay.model.Acl.Diritti;
import it.govpay.model.Acl.Servizio;
import it.govpay.model.Utenza.TIPO_UTENZA;
import it.govpay.pagamento.v1.beans.Avviso;
import it.govpay.pagamento.v1.beans.Avviso.StatoEnum;
import it.govpay.pagamento.v1.beans.converter.PendenzeConverter;

public class AvvisiController extends BaseController {

     public AvvisiController(String nomeServizio,Logger log) {
		super(nomeServizio,log);
     }



    public Response avvisiIdDominioNumeroAvvisoGET(Authentication user, UriInfo uriInfo, HttpHeaders httpHeaders , String idDominio, String numeroAvviso, String idDebitore) {
    	String methodName = "avvisiIdDominioIuvGET";  
		String transactionId = this.context.getTransactionId();

		this.log.debug(MessageFormat.format(BaseController.LOG_MSG_ESECUZIONE_METODO_IN_CORSO, methodName)); 

		try{
			// autorizzazione sulla API
			this.isAuthorized(user, Arrays.asList(TIPO_UTENZA.ANONIMO, TIPO_UTENZA.CITTADINO, TIPO_UTENZA.APPLICAZIONE), Arrays.asList(Servizio.API_PAGAMENTI), Arrays.asList(Diritti.LETTURA));
			
			ValidatoreIdentificativi validatoreId = ValidatoreIdentificativi.newInstance();
			validatoreId.validaIdDominio("idDominio", idDominio);

			GetAvvisoDTO getAvvisoDTO = new GetAvvisoDTO(user, idDominio, numeroAvviso);
			getAvvisoDTO.setAccessoAnonimo(true);
			getAvvisoDTO.setCfDebitore(idDebitore);
			
			String accept = MediaType.APPLICATION_JSON;
			if(httpHeaders.getRequestHeaders().containsKey("Accept")) {
				accept = httpHeaders.getRequestHeaders().get("Accept").get(0).toLowerCase();
			}
			
			if(!AuthorizationManager.isDominioAuthorized(getAvvisoDTO.getUser(), getAvvisoDTO.getCodDominio())) {
				throw AuthorizationManager.toNotAuthorizedException(getAvvisoDTO.getUser(), getAvvisoDTO.getCodDominio(),null);
			}
			
			AvvisiDAO avvisiDAO = new AvvisiDAO();
			
			if(accept.toLowerCase().contains("application/pdf")) {
				getAvvisoDTO.setFormato(FormatoAvviso.PDF);
				GetAvvisoDTOResponse getAvvisoDTOResponse = avvisiDAO.getAvviso(getAvvisoDTO);
				this.log.debug(MessageFormat.format(BaseController.LOG_MSG_ESECUZIONE_METODO_COMPLETATA, methodName)); 
				return this.handleResponseOk(Response.status(Status.OK).type("application/pdf").entity(getAvvisoDTOResponse.getAvvisoPdf()).header("content-disposition", "attachment; filename=\""+getAvvisoDTOResponse.getFilenameAvviso()+"\""),transactionId).build();
			} else if(accept.toLowerCase().contains("application/json")) {
				getAvvisoDTO.setFormato(FormatoAvviso.JSON);
				Avviso avviso = null;
				try {
					GetAvvisoDTOResponse getAvvisoDTOResponse = avvisiDAO.getAvviso(getAvvisoDTO);
					avviso = PendenzeConverter.toAvvisoRsModel(getAvvisoDTOResponse.getVersamento(), getAvvisoDTOResponse.getDominio(), getAvvisoDTOResponse.getBarCode(), getAvvisoDTOResponse.getQrCode());
				} catch (PendenzaNonTrovataException pnte) {
					avviso = new Avviso();
					avviso.setStato(StatoEnum.SCONOSCIUTO);
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
			this.log(this.context);
		}
    }

}


