package it.govpay.pagamento.v2.controller;

import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.ResponseBuilder;
import javax.ws.rs.core.Response.Status;
import javax.ws.rs.core.UriInfo;

import org.openspcoop2.utils.service.context.ContextThreadLocal;
import org.slf4j.Logger;
import org.springframework.security.core.Authentication;

import eu.medsea.mimeutil.MimeUtil;
import it.govpay.core.autorizzazione.AuthorizationManager;
import it.govpay.core.beans.Costanti;
import it.govpay.core.dao.anagrafica.DominiDAO;
import it.govpay.core.dao.anagrafica.dto.FindDominiDTO;
import it.govpay.core.dao.anagrafica.dto.FindDominiDTOResponse;
import it.govpay.core.dao.anagrafica.dto.FindTipiPendenzaDominioDTO;
import it.govpay.core.dao.anagrafica.dto.FindTipiPendenzaDominioDTOResponse;
import it.govpay.core.dao.anagrafica.dto.FindUnitaOperativeDTO;
import it.govpay.core.dao.anagrafica.dto.FindUnitaOperativeDTOResponse;
import it.govpay.core.dao.anagrafica.dto.GetDominioDTO;
import it.govpay.core.dao.anagrafica.dto.GetDominioDTOResponse;
import it.govpay.core.dao.anagrafica.dto.GetTipoPendenzaDominioDTO;
import it.govpay.core.dao.anagrafica.dto.GetTipoPendenzaDominioDTOResponse;
import it.govpay.core.dao.anagrafica.dto.GetUnitaOperativaDTO;
import it.govpay.core.dao.anagrafica.dto.GetUnitaOperativaDTOResponse;
import it.govpay.core.utils.GovpayConfig;
import it.govpay.core.utils.validator.ValidatorFactory;
import it.govpay.core.utils.validator.ValidatoreIdentificativi;
import it.govpay.core.utils.validator.ValidatoreUtils;
import it.govpay.model.Acl.Diritti;
import it.govpay.model.Acl.Servizio;
import it.govpay.model.Utenza.TIPO_UTENZA;
import it.govpay.pagamento.v2.beans.ListaDomini;
import it.govpay.pagamento.v2.beans.ListaTipiPendenza;
import it.govpay.pagamento.v2.beans.ListaUnitaOperative;
import it.govpay.pagamento.v2.beans.TipoPendenza;
import it.govpay.pagamento.v2.beans.UnitaOperativa;
import it.govpay.pagamento.v2.beans.converter.DominiConverter;



public class DominiController extends BaseController {

     public DominiController(String nomeServizio,Logger log) {
		super(nomeServizio,log);
     }

    public Response dominiIdDominioLogoGET(Authentication user, UriInfo uriInfo, HttpHeaders httpHeaders, String idDominio) {
    	String methodName = "getLogo";  
		String transactionId = ContextThreadLocal.get().getTransactionId();
		this.log.debug(MessageFormat.format(BaseController.LOG_MSG_ESECUZIONE_METODO_IN_CORSO, methodName)); 
		try{
			// autorizzazione sulla API
			this.isAuthorized(user, Arrays.asList(TIPO_UTENZA.ANONIMO, TIPO_UTENZA.CITTADINO, TIPO_UTENZA.APPLICAZIONE), Arrays.asList(Servizio.API_PAGAMENTI), Arrays.asList(Diritti.LETTURA));
			
			ValidatoreIdentificativi validatoreId = ValidatoreIdentificativi.newInstance();
			validatoreId.validaIdDominio("idDominio", idDominio);
			
			// Parametri - > DTO Input
			
			GetDominioDTO getDominioDTO = new GetDominioDTO(user, idDominio);

			// INIT DAO
			
			DominiDAO dominiDAO = new DominiDAO();
			
			// CHIAMATA AL DAO
			
			byte[] logo = dominiDAO.getLogo(getDominioDTO);
			
			MimeUtil.registerMimeDetector(eu.medsea.mimeutil.detector.MagicMimeMimeDetector.class.getName());
			
			Collection<?> mimeTypes = MimeUtil.getMimeTypes(logo);
			
			String mimeType = MimeUtil.getFirstMimeType(mimeTypes.toString()).toString();

			this.log.debug(MessageFormat.format(BaseController.LOG_MSG_ESECUZIONE_METODO_COMPLETATA, methodName)); 
			ResponseBuilder entity = Response.status(Status.OK).entity(logo);
			entity.header("CacheControl", "max-age: "+ GovpayConfig.getInstance().getCacheLogo().intValue());
			entity.header("Content-Type", mimeType);
			return this.handleResponseOk(entity,transactionId).build();
			
		}catch (Exception e) {
			return this.handleException(uriInfo, httpHeaders, methodName, e, transactionId);
		} finally {
			this.log(ContextThreadLocal.get());
		}
    }


    public Response dominiIdDominioUnitaOperativeIdUnitaOperativaGET(Authentication user, UriInfo uriInfo, HttpHeaders httpHeaders , String idDominio, String idUnitaOperativa) {
    	String methodName = "dominiIdDominioUnitaOperativeIdUnitaOperativaGET";  
		String transactionId = ContextThreadLocal.get().getTransactionId();
		this.log.debug(MessageFormat.format(BaseController.LOG_MSG_ESECUZIONE_METODO_IN_CORSO, methodName)); 
		try{
			// autorizzazione sulla API
			this.isAuthorized(user, Arrays.asList(TIPO_UTENZA.ANONIMO, TIPO_UTENZA.CITTADINO, TIPO_UTENZA.APPLICAZIONE), Arrays.asList(Servizio.API_PAGAMENTI), Arrays.asList(Diritti.LETTURA));
			
			ValidatoreIdentificativi validatoreId = ValidatoreIdentificativi.newInstance();
			validatoreId.validaIdDominio("idDominio", idDominio);
			validatoreId.validaIdUO("idUnitaOperativa", idUnitaOperativa);
			
			// Parametri - > DTO Input
			
			GetUnitaOperativaDTO getDominioUoDTO = new GetUnitaOperativaDTO(user, idDominio, idUnitaOperativa);
			
			// INIT DAO
			
			DominiDAO dominiDAO = new DominiDAO();
			
			// CHIAMATA AL DAO
			
			GetUnitaOperativaDTOResponse listaDominiUoDTOResponse = dominiDAO.getUnitaOperativa(getDominioUoDTO);
			
			// CONVERT TO JSON DELLA RISPOSTA
			
			UnitaOperativa response = DominiConverter.toUnitaOperativaRsModel(listaDominiUoDTOResponse.getUnitaOperativa());
			
			this.log.debug(MessageFormat.format(BaseController.LOG_MSG_ESECUZIONE_METODO_COMPLETATA, methodName)); 
			return this.handleResponseOk(Response.status(Status.OK).entity(response.toJSON(null)),transactionId).build();
			
		}catch (Exception e) {
			return this.handleException(uriInfo, httpHeaders, methodName, e, transactionId);
		} finally {
			this.log(ContextThreadLocal.get());
		}
    }



    public Response dominiGET(Authentication user, UriInfo uriInfo, HttpHeaders httpHeaders , Integer pagina, Integer risultatiPerPagina, String ordinamento, String campi, Boolean abilitato, String idStazione) {
    	String methodName = "dominiGET";  
		String transactionId = ContextThreadLocal.get().getTransactionId();
		this.log.debug(MessageFormat.format(BaseController.LOG_MSG_ESECUZIONE_METODO_IN_CORSO, methodName)); 
		try{
			// autorizzazione sulla API
			this.isAuthorized(user, Arrays.asList(TIPO_UTENZA.ANONIMO, TIPO_UTENZA.CITTADINO, TIPO_UTENZA.APPLICAZIONE), Arrays.asList(Servizio.API_PAGAMENTI), Arrays.asList(Diritti.LETTURA));

			ValidatorFactory vf = ValidatorFactory.newInstance();
			ValidatoreUtils.validaRisultatiPerPagina(vf, Costanti.PARAMETRO_RISULTATI_PER_PAGINA, risultatiPerPagina);
			
			// Parametri - > DTO Input
			
			FindDominiDTO listaDominiDTO = new FindDominiDTO(user);
			
			listaDominiDTO.setLimit(risultatiPerPagina);
			listaDominiDTO.setPagina(pagina);
			listaDominiDTO.setOrderBy(ordinamento);
			listaDominiDTO.setAbilitato(abilitato);
			listaDominiDTO.setCodStazione(idStazione);
			
			// INIT DAO
			
			DominiDAO dominiDAO = new DominiDAO();
			
			// CHIAMATA AL DAO
			
			FindDominiDTOResponse listaDominiDTOResponse = dominiDAO.findDomini(listaDominiDTO);
			
			// CONVERT TO JSON DELLA RISPOSTA
			
			List<it.govpay.pagamento.v2.beans.Dominio> results = new ArrayList<>();
			for(it.govpay.bd.model.Dominio dominio: listaDominiDTOResponse.getResults()) {
				results.add(DominiConverter.toRsModel(dominio));
			}
			
			ListaDomini response = new ListaDomini(results, this.getServicePath(uriInfo),
					listaDominiDTOResponse.getTotalResults(), pagina, risultatiPerPagina);
			
			this.log.debug(MessageFormat.format(BaseController.LOG_MSG_ESECUZIONE_METODO_COMPLETATA, methodName)); 
			return this.handleResponseOk(Response.status(Status.OK).entity(response.toJSON(campi)),transactionId).build();
			
		}catch (Exception e) {
			return this.handleException(uriInfo, httpHeaders, methodName, e, transactionId);
		} finally {
			this.log(ContextThreadLocal.get());
		}

    }



    public Response dominiIdDominioGET(Authentication user, UriInfo uriInfo, HttpHeaders httpHeaders , String idDominio) {
    	String methodName = "dominiIdDominioGET";  
		String transactionId = ContextThreadLocal.get().getTransactionId();
		this.log.debug(MessageFormat.format(BaseController.LOG_MSG_ESECUZIONE_METODO_IN_CORSO, methodName)); 
		try{
			// autorizzazione sulla API
			this.isAuthorized(user, Arrays.asList(TIPO_UTENZA.ANONIMO, TIPO_UTENZA.CITTADINO, TIPO_UTENZA.APPLICAZIONE), Arrays.asList(Servizio.API_PAGAMENTI), Arrays.asList(Diritti.LETTURA));

			ValidatoreIdentificativi validatoreId = ValidatoreIdentificativi.newInstance();
			validatoreId.validaIdDominio("idDominio", idDominio);
			
			// Parametri - > DTO Input
			
			GetDominioDTO getDominioDTO = new GetDominioDTO(user, idDominio);

			// INIT DAO
			
			DominiDAO dominiDAO = new DominiDAO();
			
			// CHIAMATA AL DAO
			
			GetDominioDTOResponse listaDominiDTOResponse = dominiDAO.getDominio(getDominioDTO);
			
			// CONVERT TO JSON DELLA RISPOSTA
			
			it.govpay.pagamento.v2.beans.Dominio response = DominiConverter.toRsModel(listaDominiDTOResponse.getDominio(), listaDominiDTOResponse.getUo(), listaDominiDTOResponse.getTributi(), listaDominiDTOResponse.getIban());
			
			this.log.debug(MessageFormat.format(BaseController.LOG_MSG_ESECUZIONE_METODO_COMPLETATA, methodName)); 
			return this.handleResponseOk(Response.status(Status.OK).entity(response.toJSON(null)),transactionId).build();
			
		}catch (Exception e) {
			return this.handleException(uriInfo, httpHeaders, methodName, e, transactionId);
		} finally {
			this.log(ContextThreadLocal.get());
		}
    }

    public Response dominiIdDominioTipiPendenzaGET(Authentication user, UriInfo uriInfo, HttpHeaders httpHeaders , String idDominio) {
    	String methodName = "dominiIdDominioTipiPendenzaGET";  
		String transactionId = ContextThreadLocal.get().getTransactionId();
		this.log.debug(MessageFormat.format(BaseController.LOG_MSG_ESECUZIONE_METODO_IN_CORSO, methodName)); 
		try{
			// autorizzazione sulla API
			this.isAuthorized(user, Arrays.asList(TIPO_UTENZA.ANONIMO, TIPO_UTENZA.CITTADINO, TIPO_UTENZA.APPLICAZIONE), Arrays.asList(Servizio.API_PAGAMENTI), Arrays.asList(Diritti.LETTURA));
			
			ValidatoreIdentificativi validatoreId = ValidatoreIdentificativi.newInstance();
			validatoreId.validaIdDominio("idDominio", idDominio);
			
			// autorizzazione sui domini
			List<String> codDominiAutorizzati = AuthorizationManager.getDominiAutorizzati(user);
			if(codDominiAutorizzati == null)
				throw AuthorizationManager.toNotAuthorizedExceptionNessunDominioAutorizzato(user);
			if(!codDominiAutorizzati.isEmpty() && !codDominiAutorizzati.contains(idDominio)) {
				throw AuthorizationManager.toNotAuthorizedException(user, idDominio, null);
			}
			
			// Parametri - > DTO Input

			FindTipiPendenzaDominioDTO findTipiPendenzaDominioDTO = new FindTipiPendenzaDominioDTO(user);

			findTipiPendenzaDominioDTO.setCodDominio(idDominio);
			findTipiPendenzaDominioDTO.setAbilitato(true);
//			findTipiPendenzaDominioDTO.setTipo(TipoVersamento.Tipo.SPONTANEO); 
			findTipiPendenzaDominioDTO.setFormPortalePagamento(true);
			
			List<Long> idTipiVersamentoAutorizzati = AuthorizationManager.getIdTipiVersamentoAutorizzati(user);
			if(idTipiVersamentoAutorizzati == null)
				throw AuthorizationManager.toNotAuthorizedExceptionNessunTipoVersamentoAutorizzato(user);

			findTipiPendenzaDominioDTO.setIdTipiVersamento(idTipiVersamentoAutorizzati);
			
			// INIT DAO

			DominiDAO dominiDAO = new DominiDAO(false);

			// CHIAMATA AL DAO

			FindTipiPendenzaDominioDTOResponse findTipiPendenzaDominioDTOResponse = dominiDAO.findTipiPendenzaConPortalePagamento(findTipiPendenzaDominioDTO);

			// CONVERT TO JSON DELLA RISPOSTA

			List<it.govpay.pagamento.v2.beans.TipoPendenza> results = new ArrayList<>();
			for(GetTipoPendenzaDominioDTOResponse tipoVersamentoDominio: findTipiPendenzaDominioDTOResponse.getResults()) {
				results.add(DominiConverter.toTipoPendenzaRsModel(tipoVersamentoDominio));
			}

			ListaTipiPendenza response = new ListaTipiPendenza(results, this.getServicePath(uriInfo),
					findTipiPendenzaDominioDTOResponse.getTotalResults(), 1, findTipiPendenzaDominioDTOResponse.getTotalResults().intValue());

			this.log.debug(MessageFormat.format(BaseController.LOG_MSG_ESECUZIONE_METODO_COMPLETATA, methodName)); 
			return this.handleResponseOk(Response.status(Status.OK).entity(response.toJSON(null)),transactionId).build();

		}catch (Exception e) {
			return this.handleException(uriInfo, httpHeaders, methodName, e, transactionId);
		} finally {
			this.log(ContextThreadLocal.get());
		}
    }



    public Response dominiIdDominioTipiPendenzaIdTipoPendenzaGET(Authentication user, UriInfo uriInfo, HttpHeaders httpHeaders , String idDominio, String idTipoPendenza) {
    	String methodName = "dominiIdDominioTipiPendenzaIdTipoPendenzaGET";  
		String transactionId = ContextThreadLocal.get().getTransactionId();
		this.log.debug(MessageFormat.format(BaseController.LOG_MSG_ESECUZIONE_METODO_IN_CORSO, methodName)); 
		try{
			// autorizzazione sulla API
			this.isAuthorized(user, Arrays.asList(TIPO_UTENZA.ANONIMO, TIPO_UTENZA.CITTADINO, TIPO_UTENZA.APPLICAZIONE), Arrays.asList(Servizio.API_PAGAMENTI), Arrays.asList(Diritti.LETTURA));

			ValidatoreIdentificativi validatoreId = ValidatoreIdentificativi.newInstance();
			validatoreId.validaIdDominio("idDominio", idDominio);
			validatoreId.validaIdTipoVersamento("idTipoPendenza", idTipoPendenza);

			// Parametri - > DTO Input

			GetTipoPendenzaDominioDTO getTipoPendenzaDominioDTO = new GetTipoPendenzaDominioDTO(user, idDominio, idTipoPendenza);

			// INIT DAO

			DominiDAO dominiDAO = new DominiDAO(false);

			// CHIAMATA AL DAO

			GetTipoPendenzaDominioDTOResponse getTipoPendenzaDominioDTOResponse = dominiDAO.getTipoPendenza(getTipoPendenzaDominioDTO); 

			// CONVERT TO JSON DELLA RISPOSTA

			TipoPendenza response = DominiConverter.toTipoPendenzaRsModel(getTipoPendenzaDominioDTOResponse);

			this.log.debug(MessageFormat.format(BaseController.LOG_MSG_ESECUZIONE_METODO_COMPLETATA, methodName)); 
			return this.handleResponseOk(Response.status(Status.OK).entity(response.toJSON(null)),transactionId).build();

		}catch (Exception e) {
			return this.handleException(uriInfo, httpHeaders, methodName, e, transactionId);
		} finally {
			this.log(ContextThreadLocal.get());
		}
    }



    public Response dominiIdDominioUnitaOperativeGET(Authentication user, UriInfo uriInfo, HttpHeaders httpHeaders , String idDominio, Integer pagina, Integer risultatiPerPagina, String ordinamento, String campi, Boolean abilitato) {    	
    	String methodName = "dominiIdDominioUnitaOperativeGET";  
		String transactionId = ContextThreadLocal.get().getTransactionId();
		this.log.debug(MessageFormat.format(BaseController.LOG_MSG_ESECUZIONE_METODO_IN_CORSO, methodName)); 
		try{
			// autorizzazione sulla API
			this.isAuthorized(user, Arrays.asList(TIPO_UTENZA.ANONIMO, TIPO_UTENZA.CITTADINO, TIPO_UTENZA.APPLICAZIONE), Arrays.asList(Servizio.API_PAGAMENTI), Arrays.asList(Diritti.LETTURA));

			ValidatoreIdentificativi validatoreId = ValidatoreIdentificativi.newInstance();
			validatoreId.validaIdDominio("idDominio", idDominio);
			
			ValidatorFactory vf = ValidatorFactory.newInstance();
			ValidatoreUtils.validaRisultatiPerPagina(vf, Costanti.PARAMETRO_RISULTATI_PER_PAGINA, risultatiPerPagina);
			
			// Parametri - > DTO Input
			
			FindUnitaOperativeDTO listaDominiUoDTO = new FindUnitaOperativeDTO(user, idDominio);
			
			listaDominiUoDTO.setPagina(pagina);
			listaDominiUoDTO.setLimit(risultatiPerPagina);
			listaDominiUoDTO.setOrderBy(ordinamento);
			listaDominiUoDTO.setAbilitato(abilitato);
			
			// INIT DAO
			
			DominiDAO dominiDAO = new DominiDAO();
			
			// CHIAMATA AL DAO
			
			FindUnitaOperativeDTOResponse listaDominiUoDTOResponse = dominiDAO.findUnitaOperative(listaDominiUoDTO);
			
			// CONVERT TO JSON DELLA RISPOSTA
			
			List<it.govpay.pagamento.v2.beans.UnitaOperativa> results = new ArrayList<>();
			for(it.govpay.bd.model.UnitaOperativa uo: listaDominiUoDTOResponse.getResults()) {
				results.add(DominiConverter.toUnitaOperativaRsModel(uo));
			}
			
			ListaUnitaOperative response = new ListaUnitaOperative(results, this.getServicePath(uriInfo),
					listaDominiUoDTOResponse.getTotalResults(), pagina, risultatiPerPagina);
			
			this.log.debug(MessageFormat.format(BaseController.LOG_MSG_ESECUZIONE_METODO_COMPLETATA, methodName)); 
			return this.handleResponseOk(Response.status(Status.OK).entity(response.toJSON(campi)),transactionId).build();
			
		}catch (Exception e) {
			return this.handleException(uriInfo, httpHeaders, methodName, e, transactionId);
		} finally {
			this.log(ContextThreadLocal.get());
		}
    }
}


