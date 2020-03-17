package it.govpay.pagamento.v2.controller;

import java.io.ByteArrayOutputStream;
import java.math.BigDecimal;
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
import org.apache.commons.lang.ArrayUtils;
import org.openspcoop2.utils.json.ValidationException;
import org.openspcoop2.utils.serialization.SerializationConfig;
import org.openspcoop2.utils.service.context.ContextThreadLocal;
import org.slf4j.Logger;
import org.springframework.security.core.Authentication;

import it.govpay.bd.model.PagamentoPortale;
import it.govpay.bd.model.PagamentoPortale.STATO;
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
import it.govpay.core.utils.SimpleDateFormatUtils;
import it.govpay.core.utils.UriBuilderUtils;
import it.govpay.model.Acl.Diritti;
import it.govpay.model.Acl.Servizio;
import it.govpay.model.Utenza.TIPO_UTENZA;
import it.govpay.pagamento.utils.validazione.semantica.NuovoPagamentoValidator;
import it.govpay.pagamento.v2.beans.FaultBean;
import it.govpay.pagamento.v2.beans.FaultBeanEsteso;
import it.govpay.pagamento.v2.beans.FaultBeanEsteso.CategoriaEnum;
import it.govpay.pagamento.v2.beans.ListaPagamentiIndex;
import it.govpay.pagamento.v2.beans.ModalitaAvvisaturaDigitale;
import it.govpay.pagamento.v2.beans.NuovoPagamento;
import it.govpay.pagamento.v2.beans.PagamentoCreato;
import it.govpay.pagamento.v2.beans.PendenzaIndex;
import it.govpay.pagamento.v2.beans.RppIndex;
import it.govpay.pagamento.v2.beans.StatoPagamento;
import it.govpay.pagamento.v2.beans.converter.PagamentiPortaleConverter;
import it.govpay.pagamento.v2.beans.converter.PendenzeConverter;
import it.govpay.pagamento.v2.beans.converter.RptConverter;



public class PagamentiController extends BaseController {
	
	private SerializationConfig serializationConfig;

     public PagamentiController(String nomeServizio,Logger log) {
		super(nomeServizio,log);
		
		this.serializationConfig = new SerializationConfig();
		this.serializationConfig.setExcludes(Arrays.asList("jsonIdFilter"));
     }


    public Response pagamentiPOST(Authentication user, UriInfo uriInfo, HttpHeaders httpHeaders , java.io.InputStream is, String idSessionePortale, Boolean avvisaturaDigitale, ModalitaAvvisaturaDigitale modalitaAvvisaturaDigitale, String gRecaptchaResponse) {
    	String methodName = "pagamentiPOST";  
		String transactionId = ContextThreadLocal.get().getTransactionId();
		this.log.debug(MessageFormat.format(BaseController.LOG_MSG_ESECUZIONE_METODO_IN_CORSO, methodName)); 
		try(ByteArrayOutputStream baos = new ByteArrayOutputStream();){
			// salvo il json ricevuto
			IOUtils.copy(is, baos);

			// autorizzazione sulla API
			this.isAuthorized(user, Arrays.asList(TIPO_UTENZA.ANONIMO, TIPO_UTENZA.CITTADINO, TIPO_UTENZA.APPLICAZIONE), Arrays.asList(Servizio.API_PAGAMENTI), Arrays.asList(Diritti.SCRITTURA));

			String jsonRequest = baos.toString();
			NuovoPagamento pagamentiPortaleRequest= JSONSerializable.parse(jsonRequest, NuovoPagamento.class);
			pagamentiPortaleRequest.validate();
			
			
			
			String idSession = transactionId.replace("-", "");
			PagamentiPortaleDTO pagamentiPortaleDTO = PagamentiPortaleConverter.getPagamentiPortaleDTO(pagamentiPortaleRequest, jsonRequest, user,idSession, idSessionePortale, avvisaturaDigitale,modalitaAvvisaturaDigitale);
			
			new NuovoPagamentoValidator().valida(pagamentiPortaleDTO);
			
			pagamentiPortaleDTO.setHeaders(this.getHeaders(getRequest()));
			pagamentiPortaleDTO.setPathParameters(uriInfo.getPathParameters());
			pagamentiPortaleDTO.setQueryParameters(uriInfo.getQueryParameters());
			pagamentiPortaleDTO.setReCaptcha(gRecaptchaResponse);
			
			PagamentiPortaleDAO pagamentiPortaleDAO = new PagamentiPortaleDAO(); 
			
			PagamentiPortaleDTOResponse pagamentiPortaleDTOResponse = pagamentiPortaleDAO.inserisciPagamenti(pagamentiPortaleDTO);
						
			PagamentoCreato responseOk = PagamentiPortaleConverter.getPagamentiPortaleResponseOk(pagamentiPortaleDTOResponse);
			
			this.log.debug(MessageFormat.format(BaseController.LOG_MSG_ESECUZIONE_METODO_COMPLETATA, methodName)); 
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
			this.log(ContextThreadLocal.get());
		}
    }
    
    public Response pagamentiIdSessionGET(Authentication user, UriInfo uriInfo, HttpHeaders httpHeaders , String idSessione) {
    	String methodName = "pagamentiIdSessionGET";  
		String transactionId = ContextThreadLocal.get().getTransactionId();
		this.log.debug(MessageFormat.format(BaseController.LOG_MSG_ESECUZIONE_METODO_IN_CORSO, methodName)); 
		try{
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
			it.govpay.pagamento.v2.beans.Pagamento response = PagamentiPortaleConverter.toRsModel(pagamentoPortaleModel,user);
			
			List<RppIndex> rpp = new ArrayList<>();
			List<PendenzaIndex> pendenze = new ArrayList<>();
			for(LeggiRptDTOResponse leggiRptDtoResponse: pagamentoPortaleDTOResponse.getListaRpp()) {
				rpp.add(RptConverter.toRsModelIndex(leggiRptDtoResponse.getRpt(),leggiRptDtoResponse.getVersamento(),leggiRptDtoResponse.getApplicazione(),user));

				// ordinamento delle pendenze secondo l'ordine delle RPP
				for(LeggiPendenzaDTOResponse leggiPendenzaDtoResponse: pagamentoPortaleDTOResponse.getListaPendenze()) {
					if(leggiRptDtoResponse.getVersamento().getCodVersamentoEnte().equals(leggiPendenzaDtoResponse.getVersamento().getCodVersamentoEnte()) &&
							leggiRptDtoResponse.getVersamento().getApplicazione(null).getCodApplicazione().equals(leggiPendenzaDtoResponse.getVersamento().getApplicazione(null).getCodApplicazione())) {
						pendenze.add(PendenzeConverter.toRsModelIndex(leggiPendenzaDtoResponse.getVersamento(),user));
					}
				}
			}
			response.setRpp(rpp);
			response.setPendenze(pendenze);
			
			this.log.debug(MessageFormat.format(BaseController.LOG_MSG_ESECUZIONE_METODO_COMPLETATA, methodName)); 
			return this.handleResponseOk(Response.status(Status.OK).entity(response.toJSON(null)),transactionId).build();
		}catch (Exception e) {
			return this.handleException(uriInfo, httpHeaders, methodName, e, transactionId);
		} finally {
			this.log(ContextThreadLocal.get());
		}
    }
    
    public Response pagamentiIdGET(Authentication user, UriInfo uriInfo, HttpHeaders httpHeaders , String id) {
    	String methodName = "pagamentiIdGET";  
		String transactionId = ContextThreadLocal.get().getTransactionId();
		this.log.debug(MessageFormat.format(BaseController.LOG_MSG_ESECUZIONE_METODO_IN_CORSO, methodName)); 
		try{
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
			it.govpay.pagamento.v2.beans.Pagamento response = PagamentiPortaleConverter.toRsModel(pagamentoPortaleModel,user);
			
			List<RppIndex> rpp = new ArrayList<>();
			List<PendenzaIndex> pendenze = new ArrayList<>();
			for(LeggiRptDTOResponse leggiRptDtoResponse: pagamentoPortaleDTOResponse.getListaRpp()) {
				rpp.add(RptConverter.toRsModelIndex(leggiRptDtoResponse.getRpt(),leggiRptDtoResponse.getVersamento(),leggiRptDtoResponse.getApplicazione(),user));

				// ordinamento delle pendenze secondo l'ordine delle RPP
				for(LeggiPendenzaDTOResponse leggiPendenzaDtoResponse: pagamentoPortaleDTOResponse.getListaPendenze()) {
					if(leggiRptDtoResponse.getVersamento().getCodVersamentoEnte().equals(leggiPendenzaDtoResponse.getVersamento().getCodVersamentoEnte()) &&
							leggiRptDtoResponse.getVersamento().getApplicazione(null).getCodApplicazione().equals(leggiPendenzaDtoResponse.getVersamento().getApplicazione(null).getCodApplicazione())) {
						pendenze.add(PendenzeConverter.toRsModelIndex(leggiPendenzaDtoResponse.getVersamento(),user)); 
					}
				}
			}
			response.setRpp(rpp);
			response.setPendenze(pendenze);
			
			this.log.debug(MessageFormat.format(BaseController.LOG_MSG_ESECUZIONE_METODO_COMPLETATA, methodName)); 
			return this.handleResponseOk(Response.status(Status.OK).entity(response.toJSON(null)),transactionId).build();
		}catch (Exception e) {
			return this.handleException(uriInfo, httpHeaders, methodName, e, transactionId);
		} finally {
			this.log(ContextThreadLocal.get());
		}
    }

    public Response pagamentiGET(Authentication user, UriInfo uriInfo, HttpHeaders httpHeaders , Integer pagina, Integer risultatiPerPagina, String ordinamento, String campi, String dataDa, String dataA, String stato, String versante, String idDebitore, String idSessionePortale, String idSessionePsp) {
    	String methodName = "getListaPagamenti";  
		String transactionId = ContextThreadLocal.get().getTransactionId();
		this.log.debug(MessageFormat.format(BaseController.LOG_MSG_ESECUZIONE_METODO_IN_CORSO, methodName)); 
		try{
			// autorizzazione sulla API
			this.isAuthorized(user, Arrays.asList(TIPO_UTENZA.CITTADINO, TIPO_UTENZA.APPLICAZIONE), Arrays.asList(Servizio.API_PAGAMENTI), Arrays.asList(Diritti.LETTURA));

			// Parametri - > DTO Input
			
			ListaPagamentiPortaleDTO listaPagamentiPortaleDTO = new ListaPagamentiPortaleDTO(user);
			listaPagamentiPortaleDTO.setLimit(risultatiPerPagina);
			listaPagamentiPortaleDTO.setPagina(pagina);
			if(stato != null) {
				StatoPagamento statoPagamento = StatoPagamento.fromValue(stato);
				if(statoPagamento != null) {
					switch(statoPagamento) {
					case ANNULLATO: listaPagamentiPortaleDTO.setStato(STATO.ANNULLATO); break;
					case FALLITO: listaPagamentiPortaleDTO.setStato(STATO.FALLITO); break;
					case ESEGUITO: listaPagamentiPortaleDTO.setStato(STATO.ESEGUITO); break;
					case ESEGUITO_PARZIALE: listaPagamentiPortaleDTO.setStato(STATO.ESEGUITO_PARZIALE); break;
					case NON_ESEGUITO: listaPagamentiPortaleDTO.setStato(STATO.NON_ESEGUITO); break;
					case IN_CORSO: listaPagamentiPortaleDTO.setStato(STATO.IN_CORSO); break;
					}				
				} else {
					throw new ValidationException("Codifica inesistente per stato. Valore fornito [" + stato
							+ "] valori possibili " + ArrayUtils.toString(StatoPagamento.values()));
				}
			}
			listaPagamentiPortaleDTO.setIdSessionePortale(idSessionePortale);
			listaPagamentiPortaleDTO.setIdSessionePsp(idSessionePsp);
			if(versante != null)
				listaPagamentiPortaleDTO.setVersante(versante);

			if(ordinamento != null)
				listaPagamentiPortaleDTO.setOrderBy(ordinamento);
			
			if(dataDa!=null) {
				Date dataDaDate = SimpleDateFormatUtils.getDataDaConTimestamp(dataDa);
				listaPagamentiPortaleDTO.setDataDa(dataDaDate);
			}
				
			
			if(dataA!=null) {
				Date dataADate = SimpleDateFormatUtils.getDataAConTimestamp(dataA);
				listaPagamentiPortaleDTO.setDataA(dataADate);
			}
			
			listaPagamentiPortaleDTO.setIdDebitore(idDebitore);
			
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
			
			List<it.govpay.pagamento.v2.beans.PagamentoIndex> results = new ArrayList<>();
			for(LeggiPagamentoPortaleDTOResponse pagamentoPortale: pagamentoPortaleDTOResponse.getResults()) {
				this.log.info("get Pagamenti portale: " + pagamentoPortale.getPagamento().getIdSessione());
				results.add(PagamentiPortaleConverter.toRsModelIndex(pagamentoPortale,user));
			}
			
			Integer maxRisultatiInt = it.govpay.bd.GovpayConfig.getInstance().getMaxRisultati();
			BigDecimal maxRisultati = new BigDecimal(maxRisultatiInt.intValue());
			
			ListaPagamentiIndex response = new ListaPagamentiIndex(results, this.getServicePath(uriInfo),
					pagamentoPortaleDTOResponse.getTotalResults(), pagina, risultatiPerPagina, maxRisultati);
			
			this.log.debug(MessageFormat.format(BaseController.LOG_MSG_ESECUZIONE_METODO_COMPLETATA, methodName)); 
			return this.handleResponseOk(Response.status(Status.OK).entity(response.toJSON(campi)),transactionId).build();
		}catch (Exception e) {
			return this.handleException(uriInfo, httpHeaders, methodName, e, transactionId);
		} finally {
			this.log(ContextThreadLocal.get());
		}
    }

}


