package it.govpay.ragioneria.v2.controller;

import java.io.ByteArrayOutputStream;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;
import javax.ws.rs.core.UriInfo;

import org.apache.commons.io.IOUtils;
import org.openspcoop2.utils.service.context.ContextThreadLocal;
import org.slf4j.Logger;
import org.springframework.security.core.Authentication;

import it.govpay.core.autorizzazione.AuthorizationManager;
import it.govpay.core.autorizzazione.utils.AutorizzazioneUtils;
import it.govpay.core.beans.JSONSerializable;
import it.govpay.core.dao.pagamenti.IncassiDAO;
import it.govpay.core.dao.pagamenti.dto.LeggiIncassoDTO;
import it.govpay.core.dao.pagamenti.dto.LeggiIncassoDTOResponse;
import it.govpay.core.dao.pagamenti.dto.ListaIncassiDTO;
import it.govpay.core.dao.pagamenti.dto.ListaIncassiDTOResponse;
import it.govpay.core.dao.pagamenti.dto.RichiestaIncassoDTO;
import it.govpay.core.dao.pagamenti.dto.RichiestaIncassoDTOResponse;
import it.govpay.core.utils.SimpleDateFormatUtils;
import it.govpay.core.utils.validator.ValidatoreIdentificativi;
import it.govpay.model.Acl.Diritti;
import it.govpay.model.Acl.Servizio;
import it.govpay.model.Utenza.TIPO_UTENZA;
import it.govpay.ragioneria.v2.beans.NuovaRiconciliazione;
import it.govpay.ragioneria.v2.beans.Riconciliazione;
import it.govpay.ragioneria.v2.beans.RiconciliazioneIndex;
import it.govpay.ragioneria.v2.beans.Riconciliazioni;
import it.govpay.ragioneria.v2.beans.converter.RiconciliazioniConverter;



public class RiconciliazioniController extends BaseController {
	
	public RiconciliazioniController(String nomeServizio,Logger log) {
		super(nomeServizio,log);
	}


    public Response findRiconciliazioni(Authentication user, UriInfo uriInfo, HttpHeaders httpHeaders , Integer pagina, Integer risultatiPerPagina, String ordinamento, String dataDa, String dataA, String idDominio) {
    	String methodName = "findRiconciliazioni";  
		String transactionId = ContextThreadLocal.get().getTransactionId();
		this.log.debug(MessageFormat.format(BaseController.LOG_MSG_ESECUZIONE_METODO_IN_CORSO, methodName)); 
		String campi = null;
		try{
			// autorizzazione sulla API
			this.isAuthorized(user, Arrays.asList(TIPO_UTENZA.APPLICAZIONE), Arrays.asList(Servizio.API_RAGIONERIA), Arrays.asList(Diritti.LETTURA));
			
			ListaIncassiDTO listaIncassoDTO = new ListaIncassiDTO(user);
			
			listaIncassoDTO.setLimit(risultatiPerPagina);
			listaIncassoDTO.setPagina(pagina);
			listaIncassoDTO.setIdDominio(idDominio);
		
			if(dataDa != null) {
				Date dataDaDate = SimpleDateFormatUtils.getDataDaConTimestamp(dataDa);
				listaIncassoDTO.setDataDa(dataDaDate);
			}
			if(dataA != null) {
				Date dataADate = SimpleDateFormatUtils.getDataAConTimestamp(dataA);
				listaIncassoDTO.setDataA(dataADate);
			}
			
			// filtro sull'applicazione			
			listaIncassoDTO.setIdA2A(AutorizzazioneUtils.getAuthenticationDetails(user).getApplicazione().getCodApplicazione()); 
			
			// autorizzazione sui domini
			List<String> domini = AuthorizationManager.getDominiAutorizzati(user); 
			listaIncassoDTO.setCodDomini(domini);
			
			IncassiDAO incassiDAO = new IncassiDAO();
			ListaIncassiDTOResponse listaIncassiDTOResponse = domini != null ? incassiDAO.listaIncassi(listaIncassoDTO) : new ListaIncassiDTOResponse(0, new ArrayList<>());
			
			// CONVERT TO JSON DELLA RISPOSTA
			
			List<RiconciliazioneIndex> listaIncassi = new ArrayList<>();
			for(it.govpay.bd.model.Incasso i : listaIncassiDTOResponse.getResults()) {
				listaIncassi.add(RiconciliazioniConverter.toRsIndexModel(i));
			}
			
			Riconciliazioni response = new Riconciliazioni(listaIncassi, this.getServicePath(uriInfo),
					listaIncassiDTOResponse.getTotalResults(), pagina, risultatiPerPagina);
			
			this.log.debug(MessageFormat.format(BaseController.LOG_MSG_ESECUZIONE_METODO_COMPLETATA, methodName)); 
			return this.handleResponseOk(Response.status(Status.OK).entity(response.toJSON(campi)),transactionId).build();
			
		}catch (Exception e) {
			return this.handleException(uriInfo, httpHeaders, methodName, e, transactionId);
		} finally {
			this.log(ContextThreadLocal.get());
		}
    }


    public Response getRiconciliazione(Authentication user, UriInfo uriInfo, HttpHeaders httpHeaders , String idDominio, String idIncasso) {
    	String methodName = "getRiconciliazione";  
		String transactionId = ContextThreadLocal.get().getTransactionId();
		this.log.debug(MessageFormat.format(BaseController.LOG_MSG_ESECUZIONE_METODO_IN_CORSO, methodName)); 
		try{
			// autorizzazione sulla API
			this.isAuthorized(user, Arrays.asList(TIPO_UTENZA.APPLICAZIONE), Arrays.asList(Servizio.API_RAGIONERIA), Arrays.asList(Diritti.LETTURA));

			ValidatoreIdentificativi validatoreId = ValidatoreIdentificativi.newInstance();
			validatoreId.validaIdDominio("idDominio", idDominio);
			
			// Parametri - > DTO Input
			
			LeggiIncassoDTO leggiIncassoDTO = new LeggiIncassoDTO(user);
			leggiIncassoDTO.setIdDominio(idDominio);
			leggiIncassoDTO.setIdIncasso(idIncasso);

			if(!AuthorizationManager.isDominioAuthorized(leggiIncassoDTO.getUser(), leggiIncassoDTO.getIdDominio())) {
				throw AuthorizationManager.toNotAuthorizedException(leggiIncassoDTO.getUser(), leggiIncassoDTO.getIdDominio(), null);
			}
			
			IncassiDAO incassiDAO = new IncassiDAO();
			
			// CHIAMATA AL DAO
			
			LeggiIncassoDTOResponse leggiIncassoDTOResponse = incassiDAO.leggiIncasso(leggiIncassoDTO);
			
			// filtro sull'applicazione			
			if(!AutorizzazioneUtils.getAuthenticationDetails(user).getApplicazione().getCodApplicazione().equals(leggiIncassoDTOResponse.getIncasso().getApplicazione(null).getCodApplicazione())) {
				throw AuthorizationManager.toNotAuthorizedException(user);
			}
			
			// CONVERT TO JSON DELLA RISPOSTA
			
			Riconciliazione response = RiconciliazioniConverter.toRsModel(leggiIncassoDTOResponse.getIncasso());
			
			this.log.debug(MessageFormat.format(BaseController.LOG_MSG_ESECUZIONE_METODO_COMPLETATA, methodName)); 
			return this.handleResponseOk(Response.status(Status.OK).entity(response.toJSON(null)),transactionId).build();
			
		}catch (Exception e) {
			return this.handleException(uriInfo, httpHeaders, methodName, e, transactionId);
		} finally {
			this.log(ContextThreadLocal.get());
		}
    }

    public Response addRiconciliazione(Authentication user, UriInfo uriInfo, HttpHeaders httpHeaders , String idDominio, java.io.InputStream is) {
    	String methodName = "addRiconciliazione"; 
		String transactionId = ContextThreadLocal.get().getTransactionId();
		this.log.debug(MessageFormat.format(BaseController.LOG_MSG_ESECUZIONE_METODO_IN_CORSO, methodName)); 
		try(ByteArrayOutputStream baos = new ByteArrayOutputStream();){
			// salvo il json ricevuto
			IOUtils.copy(is, baos);
			
			// autorizzazione sulla API
			this.isAuthorized(user, Arrays.asList(TIPO_UTENZA.APPLICAZIONE), Arrays.asList(Servizio.API_RAGIONERIA), Arrays.asList(Diritti.SCRITTURA));

			ValidatoreIdentificativi validatoreId = ValidatoreIdentificativi.newInstance();
			validatoreId.validaIdDominio("idDominio", idDominio);
			
			NuovaRiconciliazione incasso = JSONSerializable.parse(baos.toString(), NuovaRiconciliazione.class);
			incasso.validate();
			
			RichiestaIncassoDTO richiestaIncassoDTO = RiconciliazioniConverter.toRichiestaIncassoDTO(incasso, idDominio, user);
			
			IncassiDAO incassiDAO = new IncassiDAO();
			
			RichiestaIncassoDTOResponse richiestaIncassoDTOResponse = incassiDAO.richiestaIncasso(richiestaIncassoDTO);
			
			Riconciliazione incassoExt = RiconciliazioniConverter.toRsModel(richiestaIncassoDTOResponse.getIncasso());
			
			Status responseStatus = richiestaIncassoDTOResponse.isCreated() ?  Status.CREATED : Status.OK;
			
			this.log.debug(MessageFormat.format(BaseController.LOG_MSG_ESECUZIONE_METODO_COMPLETATA, methodName)); 
			return this.handleResponseOk(Response.status(responseStatus).entity(incassoExt.toJSON(null)),transactionId).build();
		}catch (Exception e) {
			return this.handleException(uriInfo, httpHeaders, methodName, e, transactionId);
		} finally {
			this.log(ContextThreadLocal.get());
		}
    }
}


