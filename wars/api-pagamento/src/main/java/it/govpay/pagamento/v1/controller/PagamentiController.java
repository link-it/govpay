package it.govpay.pagamento.v1.controller;

import java.io.ByteArrayOutputStream;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;
import javax.ws.rs.core.UriInfo;

import org.apache.commons.io.IOUtils;
import org.apache.commons.lang.time.DateUtils;
import org.openspcoop2.utils.serialization.SerializationConfig;
import org.openspcoop2.utils.service.context.IContext;
import org.slf4j.Logger;
import org.springframework.security.core.Authentication;

import it.govpay.bd.model.PagamentoPortale;
import it.govpay.core.autorizzazione.AuthorizationManager;
import it.govpay.core.autorizzazione.beans.GovpayLdapUserDetails;
import it.govpay.core.autorizzazione.utils.AutorizzazioneUtils;
import it.govpay.core.beans.JSONSerializable;
import it.govpay.core.dao.pagamenti.PagamentiPortaleDAO;
import it.govpay.core.dao.pagamenti.dto.LeggiPagamentoPortaleDTO;
import it.govpay.core.dao.pagamenti.dto.LeggiPagamentoPortaleDTOResponse;
import it.govpay.core.dao.pagamenti.dto.LeggiPendenzaDTOResponse;
import it.govpay.core.dao.pagamenti.dto.LeggiRptDTOResponse;
import it.govpay.core.dao.pagamenti.dto.ListaPagamentiPortaleDTO;
import it.govpay.core.dao.pagamenti.dto.ListaPagamentiPortaleDTOResponse;
import it.govpay.core.dao.pagamenti.dto.PagamentiPortaleDTO;
import it.govpay.core.dao.pagamenti.dto.PagamentiPortaleDTOResponse;
import it.govpay.core.exceptions.GovPayException;
import it.govpay.core.utils.GovpayConfig;
import it.govpay.core.utils.GpThreadLocal;
import it.govpay.core.utils.SimpleDateFormatUtils;
import it.govpay.core.utils.UriBuilderUtils;
import it.govpay.model.Acl.Diritti;
import it.govpay.model.Acl.Servizio;
import it.govpay.model.Utenza.TIPO_UTENZA;
import it.govpay.pagamento.utils.validazione.semantica.NuovoPagamentoValidator;
import it.govpay.pagamento.v1.beans.FaultBean;
import it.govpay.pagamento.v1.beans.FaultBeanEsteso;
import it.govpay.pagamento.v1.beans.FaultBeanEsteso.CategoriaEnum;
import it.govpay.pagamento.v1.beans.ListaPagamentiIndex;
import it.govpay.pagamento.v1.beans.ModalitaAvvisaturaDigitale;
import it.govpay.pagamento.v1.beans.PagamentiPortaleResponseOk;
import it.govpay.pagamento.v1.beans.PagamentoPost;
import it.govpay.pagamento.v1.beans.PendenzaIndex;
import it.govpay.pagamento.v1.beans.RppIndex;
import it.govpay.pagamento.v1.beans.converter.PagamentiPortaleConverter;
import it.govpay.pagamento.v1.beans.converter.PendenzeConverter;
import it.govpay.pagamento.v1.beans.converter.RptConverter;



public class PagamentiController extends BaseController {
	
	private SerializationConfig serializationConfig;

     public PagamentiController(String nomeServizio,Logger log) {
		super(nomeServizio,log, GovpayConfig.GOVPAY_PAGAMENTI_OPEN_API_FILE_NAME);
		
		this.serializationConfig = new SerializationConfig();
		this.serializationConfig.setExcludes(Arrays.asList("jsonIdFilter"));
     }


    public Response pagamentiPOST(Authentication user, UriInfo uriInfo, HttpHeaders httpHeaders , java.io.InputStream is, String idSessionePortale, Boolean avvisaturaDigitale, ModalitaAvvisaturaDigitale modalitaAvvisaturaDigitale) {
    	String methodName = "pagamentiPOST";  
		IContext ctx = null;
		String transactionId = null;
		ByteArrayOutputStream baos= null;
		this.log.info(MessageFormat.format(BaseController.LOG_MSG_ESECUZIONE_METODO_IN_CORSO, methodName)); 
		try{
			baos = new ByteArrayOutputStream();
			// salvo il json ricevuto
			IOUtils.copy(is, baos);
			this.logRequest(uriInfo, httpHeaders, methodName, baos);
			
			ctx =  GpThreadLocal.get();
			transactionId = ctx.getTransactionId();
			
			// autorizzazione sulla API
			this.isAuthorized(user, Arrays.asList(TIPO_UTENZA.ANONIMO, TIPO_UTENZA.CITTADINO, TIPO_UTENZA.APPLICAZIONE), Arrays.asList(Servizio.API_PAGAMENTI), Arrays.asList(Diritti.SCRITTURA));

			String jsonRequest = baos.toString();
			PagamentoPost pagamentiPortaleRequest= JSONSerializable.parse(jsonRequest, PagamentoPost.class);
			pagamentiPortaleRequest.validate();
			
			
			
			String idSession = transactionId.replace("-", "");
			PagamentiPortaleDTO pagamentiPortaleDTO = PagamentiPortaleConverter.getPagamentiPortaleDTO(pagamentiPortaleRequest, jsonRequest, user,idSession, idSessionePortale, avvisaturaDigitale,modalitaAvvisaturaDigitale);
			
			new NuovoPagamentoValidator().valida(pagamentiPortaleDTO);
			
			PagamentiPortaleDAO pagamentiPortaleDAO = new PagamentiPortaleDAO(); 
			
			PagamentiPortaleDTOResponse pagamentiPortaleDTOResponse = pagamentiPortaleDAO.inserisciPagamenti(pagamentiPortaleDTO);
						
			PagamentiPortaleResponseOk responseOk = PagamentiPortaleConverter.getPagamentiPortaleResponseOk(pagamentiPortaleDTOResponse);
			
			this.logResponse(uriInfo, httpHeaders, methodName, responseOk.toJSON(null), Status.CREATED.getStatusCode());
			this.log.info(MessageFormat.format(BaseController.LOG_MSG_ESECUZIONE_METODO_COMPLETATA, methodName)); 
			return this.handleResponseOk(Response.status(Status.CREATED).entity(responseOk.toJSON(null)),transactionId).build();
		} catch (Exception e) {
			Response response = this.handleException(uriInfo, httpHeaders, methodName, e,transactionId);
			if(e instanceof GovPayException && (PagamentoPortale) ((GovPayException) e).getParam() != null) {
				try {
					FaultBean fb = JSONSerializable.parse((String) response.getEntity(), FaultBean.class);
					FaultBeanEsteso fbe = new FaultBeanEsteso();
					fbe.setCodice(fb.getCodice());
					fbe.setCategoria(CategoriaEnum.fromValue(fb.getCategoria().toString()));
					fbe.setDescrizione(fb.getDescrizione());
					fbe.setDettaglio(fb.getDettaglio());
					fbe.setId(((PagamentoPortale) ((GovPayException) e).getParam()).getIdSessione());
					fbe.setLocation(UriBuilderUtils.getFromPagamenti(fbe.getId()));
					return Response.fromResponse(response).entity(fbe.toJSON(null)).build();
				} catch (Exception e1) {
					return Response.fromResponse(response).build();
				}
			}
			return response;
		} finally {
			this.log(ctx);
		}
    }
    
    public Response pagamentiIdSessionGET(Authentication user, UriInfo uriInfo, HttpHeaders httpHeaders , String idSessione) {
    	String methodName = "pagamentiIdSessionGET";  
		IContext ctx = null;
		String transactionId = null;
		ByteArrayOutputStream baos= null;
		this.log.info(MessageFormat.format(BaseController.LOG_MSG_ESECUZIONE_METODO_IN_CORSO, methodName)); 
		try{
			baos = new ByteArrayOutputStream();
			this.logRequest(uriInfo, httpHeaders, methodName, baos);
			
			ctx =  GpThreadLocal.get();
			transactionId = ctx.getTransactionId();
			
			// autorizzazione sulla API
			this.isAuthorized(user, Arrays.asList(TIPO_UTENZA.ANONIMO, TIPO_UTENZA.CITTADINO, TIPO_UTENZA.APPLICAZIONE), Arrays.asList(Servizio.API_PAGAMENTI), Arrays.asList(Diritti.LETTURA));
			
			LeggiPagamentoPortaleDTO leggiPagamentoPortaleDTO = new LeggiPagamentoPortaleDTO(user);
			leggiPagamentoPortaleDTO.setIdSessionePsp(idSessione);
			leggiPagamentoPortaleDTO.setRisolviLink(true); 
			
			PagamentiPortaleDAO pagamentiPortaleDAO = new PagamentiPortaleDAO(); 
			
			LeggiPagamentoPortaleDTOResponse pagamentoPortaleDTOResponse = pagamentiPortaleDAO.leggiPagamentoPortale(leggiPagamentoPortaleDTO);
			PagamentoPortale pagamentoPortale = pagamentoPortaleDTOResponse.getPagamento();
			
			GovpayLdapUserDetails details = AutorizzazioneUtils.getAuthenticationDetails(leggiPagamentoPortaleDTO.getUser());
			if(details.getTipoUtenza().equals(TIPO_UTENZA.CITTADINO)) {
				if(pagamentoPortale.getVersanteIdentificativo() == null || !pagamentoPortale.getVersanteIdentificativo().equals(details.getUtenza().getIdentificativo())) {
					throw AuthorizationManager.toNotAuthorizedException(leggiPagamentoPortaleDTO.getUser());
				}
			}
			
			if(details.getTipoUtenza().equals(TIPO_UTENZA.ANONIMO)) {
				if(pagamentoPortale.getVersanteIdentificativo() == null || !pagamentoPortale.getVersanteIdentificativo().equals(TIPO_UTENZA.ANONIMO.toString())) {
					throw AuthorizationManager.toNotAuthorizedException(leggiPagamentoPortaleDTO.getUser());
				}
				
				// pagamento terminato e' disponibile solo per un numero di minuti definito in configurazione
				if(pagamentoPortale.getDataRichiesta() != null) {
					long dataPagamentoTime = pagamentoPortale.getDataRichiesta().getTime();
					Calendar calendar = Calendar.getInstance();
					calendar.setTime(new Date());
					calendar.add(Calendar.MINUTE, -GovpayConfig.getInstance().getIntervalloDisponibilitaPagamentoUtenzaAnonima());
					long riferimentoTime = calendar.getTimeInMillis();

					// il pagamento e' stato eseguito prima dei minuti precedenti il momento della richiesta.
					if(dataPagamentoTime < riferimentoTime)
						throw AuthorizationManager.toNotAuthorizedException(leggiPagamentoPortaleDTO.getUser());
				}
			}
			
			// se sei una applicazione allora vedi i pagamenti che hai caricato
			if(details.getTipoUtenza().equals(TIPO_UTENZA.APPLICAZIONE)) {
				if(pagamentoPortale.getApplicazione(null) == null || 
						!pagamentoPortale.getApplicazione(null).getCodApplicazione().equals(details.getApplicazione().getCodApplicazione())) {
					throw AuthorizationManager.toNotAuthorizedException(leggiPagamentoPortaleDTO.getUser(), "il pagamento non appartiene all'applicazione chiamante");
				}
			}
			
			it.govpay.bd.model.PagamentoPortale pagamentoPortaleModel = pagamentoPortaleDTOResponse.getPagamento();
			it.govpay.pagamento.v1.beans.Pagamento response = PagamentiPortaleConverter.toRsModel(pagamentoPortaleModel,user);
			
			List<RppIndex> rpp = new ArrayList<>();
			List<PendenzaIndex> pendenze = new ArrayList<>();
			for(LeggiRptDTOResponse leggiRptDtoResponse: pagamentoPortaleDTOResponse.getListaRpp()) {
				rpp.add(RptConverter.toRsModelIndex(leggiRptDtoResponse.getRpt(),leggiRptDtoResponse.getVersamento(),leggiRptDtoResponse.getApplicazione(),user));

				// ordinamento delle pendenze secondo l'ordine delle RPP
				for(LeggiPendenzaDTOResponse leggiPendenzaDtoResponse: pagamentoPortaleDTOResponse.getListaPendenze()) {
					if(leggiRptDtoResponse.getVersamento().getCodVersamentoEnte().equals(leggiPendenzaDtoResponse.getVersamentoIncasso().getCodVersamentoEnte()) &&
							leggiRptDtoResponse.getVersamento().getApplicazione(null).getCodApplicazione().equals(leggiPendenzaDtoResponse.getVersamentoIncasso().getApplicazione(null).getCodApplicazione())) {
						pendenze.add(PendenzeConverter.toRsModelIndex(leggiPendenzaDtoResponse.getVersamentoIncasso(),user));
					}
				}
			}
			response.setRpp(rpp);
			response.setPendenze(pendenze);
			
			this.logResponse(uriInfo, httpHeaders, methodName, response.toJSON(null), 200);
			this.log.info(MessageFormat.format(BaseController.LOG_MSG_ESECUZIONE_METODO_COMPLETATA, methodName)); 
			return this.handleResponseOk(Response.status(Status.OK).entity(response.toJSON(null)),transactionId).build();
		}catch (Exception e) {
			return this.handleException(uriInfo, httpHeaders, methodName, e, transactionId);
		} finally {
			this.log(ctx);
		}
    }
    
    public Response pagamentiIdGET(Authentication user, UriInfo uriInfo, HttpHeaders httpHeaders , String id) {
    	String methodName = "pagamentiIdGET";  
		IContext ctx = null;
		String transactionId = null;
		ByteArrayOutputStream baos= null;
		this.log.info(MessageFormat.format(BaseController.LOG_MSG_ESECUZIONE_METODO_IN_CORSO, methodName)); 
		try{
			baos = new ByteArrayOutputStream();
			this.logRequest(uriInfo, httpHeaders, methodName, baos);
			
			ctx =  GpThreadLocal.get();
			transactionId = ctx.getTransactionId();
			
			// autorizzazione sulla API
			this.isAuthorized(user, Arrays.asList(TIPO_UTENZA.ANONIMO, TIPO_UTENZA.CITTADINO, TIPO_UTENZA.APPLICAZIONE), Arrays.asList(Servizio.API_PAGAMENTI), Arrays.asList(Diritti.LETTURA));

			LeggiPagamentoPortaleDTO leggiPagamentoPortaleDTO = new LeggiPagamentoPortaleDTO(user);
			leggiPagamentoPortaleDTO.setId(id);
			leggiPagamentoPortaleDTO.setRisolviLink(true); 
			
			PagamentiPortaleDAO pagamentiPortaleDAO = new PagamentiPortaleDAO(); 
			
			LeggiPagamentoPortaleDTOResponse pagamentoPortaleDTOResponse = pagamentiPortaleDAO.leggiPagamentoPortale(leggiPagamentoPortaleDTO);
			PagamentoPortale pagamentoPortale = pagamentoPortaleDTOResponse.getPagamento();
			
			GovpayLdapUserDetails details = AutorizzazioneUtils.getAuthenticationDetails(leggiPagamentoPortaleDTO.getUser());
			if(details.getTipoUtenza().equals(TIPO_UTENZA.CITTADINO)) {
				if(pagamentoPortale.getVersanteIdentificativo() == null || !pagamentoPortale.getVersanteIdentificativo().equals(details.getUtenza().getIdentificativo())) {
					throw AuthorizationManager.toNotAuthorizedException(leggiPagamentoPortaleDTO.getUser());
				}
			}
			
			if(details.getTipoUtenza().equals(TIPO_UTENZA.ANONIMO)) {
				if(pagamentoPortale.getVersanteIdentificativo() == null || !pagamentoPortale.getVersanteIdentificativo().equals(TIPO_UTENZA.ANONIMO.toString())) {
					throw AuthorizationManager.toNotAuthorizedException(leggiPagamentoPortaleDTO.getUser());
				}
				
				// pagamento terminato e' disponibile solo per un numero di minuti definito in configurazione
				if(pagamentoPortale.getDataRichiesta() != null) {
					long dataPagamentoTime = pagamentoPortale.getDataRichiesta().getTime();
					Calendar calendar = Calendar.getInstance();
					calendar.setTime(new Date());
					calendar.add(Calendar.MINUTE, -GovpayConfig.getInstance().getIntervalloDisponibilitaPagamentoUtenzaAnonima());
					long riferimentoTime = calendar.getTimeInMillis();

					// il pagamento e' stato eseguito prima dei minuti precedenti il momento della richiesta.
					if(dataPagamentoTime < riferimentoTime)
						throw AuthorizationManager.toNotAuthorizedException(leggiPagamentoPortaleDTO.getUser());
				}
			}
			
			// se sei una applicazione allora vedi i pagamenti che hai caricato
			if(details.getTipoUtenza().equals(TIPO_UTENZA.APPLICAZIONE)) {
				if(pagamentoPortale.getApplicazione(null) == null || 
						!pagamentoPortale.getApplicazione(null).getCodApplicazione().equals(details.getApplicazione().getCodApplicazione())) {
					throw AuthorizationManager.toNotAuthorizedException(leggiPagamentoPortaleDTO.getUser(), "il pagamento non appartiene all'applicazione chiamante");
				}
			}
			
			it.govpay.bd.model.PagamentoPortale pagamentoPortaleModel = pagamentoPortaleDTOResponse.getPagamento();
			it.govpay.pagamento.v1.beans.Pagamento response = PagamentiPortaleConverter.toRsModel(pagamentoPortaleModel,user);
			
			List<RppIndex> rpp = new ArrayList<>();
			List<PendenzaIndex> pendenze = new ArrayList<>();
			for(LeggiRptDTOResponse leggiRptDtoResponse: pagamentoPortaleDTOResponse.getListaRpp()) {
				rpp.add(RptConverter.toRsModelIndex(leggiRptDtoResponse.getRpt(),leggiRptDtoResponse.getVersamento(),leggiRptDtoResponse.getApplicazione(),user));

				// ordinamento delle pendenze secondo l'ordine delle RPP
				for(LeggiPendenzaDTOResponse leggiPendenzaDtoResponse: pagamentoPortaleDTOResponse.getListaPendenze()) {
					if(leggiRptDtoResponse.getVersamento().getCodVersamentoEnte().equals(leggiPendenzaDtoResponse.getVersamentoIncasso().getCodVersamentoEnte()) &&
							leggiRptDtoResponse.getVersamento().getApplicazione(null).getCodApplicazione().equals(leggiPendenzaDtoResponse.getVersamentoIncasso().getApplicazione(null).getCodApplicazione())) {
						pendenze.add(PendenzeConverter.toRsModelIndex(leggiPendenzaDtoResponse.getVersamentoIncasso(),user)); 
					}
				}
			}
			response.setRpp(rpp);
			response.setPendenze(pendenze);
			
			this.logResponse(uriInfo, httpHeaders, methodName, response.toJSON(null), 200);
			this.log.info(MessageFormat.format(BaseController.LOG_MSG_ESECUZIONE_METODO_COMPLETATA, methodName)); 
			return this.handleResponseOk(Response.status(Status.OK).entity(response.toJSON(null)),transactionId).build();
		}catch (Exception e) {
			return this.handleException(uriInfo, httpHeaders, methodName, e, transactionId);
		} finally {
			this.log(ctx);
		}
    }

    public Response pagamentiGET(Authentication user, UriInfo uriInfo, HttpHeaders httpHeaders , Integer pagina, Integer risultatiPerPagina, String ordinamento, String campi, String dataDa, String dataA, String stato, String versante, String idSessionePortale, String idSessionePsp) {
    	String methodName = "getListaPagamenti";  
		IContext ctx = null;
		String transactionId = null;
		ByteArrayOutputStream baos= null;
		this.log.info(MessageFormat.format(BaseController.LOG_MSG_ESECUZIONE_METODO_IN_CORSO, methodName)); 
		try{
			baos = new ByteArrayOutputStream();
			this.logRequest(uriInfo, httpHeaders, methodName, baos);
			
			ctx =  GpThreadLocal.get();
			transactionId = ctx.getTransactionId();
		
			// autorizzazione sulla API
			this.isAuthorized(user, Arrays.asList(TIPO_UTENZA.CITTADINO, TIPO_UTENZA.APPLICAZIONE), Arrays.asList(Servizio.API_PAGAMENTI), Arrays.asList(Diritti.LETTURA));

			// Parametri - > DTO Input
			
			ListaPagamentiPortaleDTO listaPagamentiPortaleDTO = new ListaPagamentiPortaleDTO(user);
			listaPagamentiPortaleDTO.setLimit(risultatiPerPagina);
			listaPagamentiPortaleDTO.setPagina(pagina);
			listaPagamentiPortaleDTO.setStato(stato);
			listaPagamentiPortaleDTO.setIdSessionePortale(idSessionePortale);
			listaPagamentiPortaleDTO.setIdSessionePsp(idSessionePsp);
			if(versante != null)
				listaPagamentiPortaleDTO.setVersante(versante);

			if(ordinamento != null)
				listaPagamentiPortaleDTO.setOrderBy(ordinamento);
			
			if(dataDa!=null) {
				Date dataDaDate = DateUtils.parseDate(dataDa, SimpleDateFormatUtils.datePatternsRest.toArray(new String[0]));
				listaPagamentiPortaleDTO.setDataDa(dataDaDate);
			}
				
			
			if(dataA!=null) {
				Date dataADate = DateUtils.parseDate(dataA, SimpleDateFormatUtils.datePatternsRest.toArray(new String[0]));
				listaPagamentiPortaleDTO.setDataA(dataADate);
			}
			
			// INIT DAO
			
			PagamentiPortaleDAO pagamentiPortaleDAO = new PagamentiPortaleDAO();
			
			GovpayLdapUserDetails userDetails = AutorizzazioneUtils.getAuthenticationDetails(listaPagamentiPortaleDTO.getUser());
			if(userDetails.getTipoUtenza().equals(TIPO_UTENZA.CITTADINO)) {
				listaPagamentiPortaleDTO.setCfCittadino(userDetails.getIdentificativo()); 
			}
			
			// se sei una applicazione allora vedi i pagamenti che hai caricato
			if(userDetails.getTipoUtenza().equals(TIPO_UTENZA.APPLICAZIONE)) {
				listaPagamentiPortaleDTO.setCodApplicazione(userDetails.getApplicazione().getCodApplicazione()); 
			}
			// CHIAMATA AL DAO
			
			ListaPagamentiPortaleDTOResponse pagamentoPortaleDTOResponse = pagamentiPortaleDAO.listaPagamentiPortale(listaPagamentiPortaleDTO);
			
			// CONVERT TO JSON DELLA RISPOSTA
			
			List<it.govpay.pagamento.v1.beans.PagamentoIndex> results = new ArrayList<>();
			for(LeggiPagamentoPortaleDTOResponse pagamentoPortale: pagamentoPortaleDTOResponse.getResults()) {
				this.log.info("get Pagamenti portale: " + pagamentoPortale.getPagamento().getIdSessione());
				results.add(PagamentiPortaleConverter.toRsModelIndex(pagamentoPortale,user));
			}
			
			ListaPagamentiIndex response = new ListaPagamentiIndex(results, this.getServicePath(uriInfo),
					pagamentoPortaleDTOResponse.getTotalResults(), pagina, risultatiPerPagina);
			
			this.logResponse(uriInfo, httpHeaders, methodName, response.toJSON(campi), 200);
			this.log.info(MessageFormat.format(BaseController.LOG_MSG_ESECUZIONE_METODO_COMPLETATA, methodName)); 
			return this.handleResponseOk(Response.status(Status.OK).entity(response.toJSON(campi)),transactionId).build();
		}catch (Exception e) {
			return this.handleException(uriInfo, httpHeaders, methodName, e, transactionId);
		} finally {
			this.log(ctx);
		}
    }

}


