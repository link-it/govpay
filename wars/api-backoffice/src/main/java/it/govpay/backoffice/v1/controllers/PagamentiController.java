package it.govpay.backoffice.v1.controllers;

import java.io.ByteArrayOutputStream;
import java.math.BigDecimal;
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
import org.apache.commons.lang.ArrayUtils;
import org.apache.commons.lang.StringUtils;
import org.openspcoop2.generic_project.exception.ServiceException;
import org.openspcoop2.utils.json.ValidationException;
import org.openspcoop2.utils.serialization.SerializationConfig;
import org.openspcoop2.utils.service.context.ContextThreadLocal;
import org.slf4j.Logger;
import org.springframework.security.core.Authentication;

import it.govpay.backoffice.v1.beans.ListaPagamentiPortale;
import it.govpay.backoffice.v1.beans.PatchOp;
import it.govpay.backoffice.v1.beans.PatchOp.OpEnum;
import it.govpay.backoffice.v1.beans.StatoPagamento;
import it.govpay.backoffice.v1.beans.converter.PagamentiPortaleConverter;
import it.govpay.backoffice.v1.beans.converter.PatchOpConverter;
import it.govpay.bd.GovpayConfig;
import it.govpay.bd.model.IdUnitaOperativa;
import it.govpay.bd.model.PagamentoPortale.STATO;
import it.govpay.core.autorizzazione.AuthorizationManager;
import it.govpay.core.beans.JSONSerializable;
import it.govpay.core.dao.pagamenti.PagamentiPortaleDAO;
import it.govpay.core.dao.pagamenti.dto.LeggiPagamentoPortaleDTO;
import it.govpay.core.dao.pagamenti.dto.LeggiPagamentoPortaleDTOResponse;
import it.govpay.core.dao.pagamenti.dto.ListaPagamentiPortaleDTO;
import it.govpay.core.dao.pagamenti.dto.ListaPagamentiPortaleDTOResponse;
import it.govpay.core.dao.pagamenti.dto.PagamentoPatchDTO;
import it.govpay.core.utils.EventoContext;
import it.govpay.core.utils.SimpleDateFormatUtils;
import it.govpay.model.Acl.Diritti;
import it.govpay.model.Acl.Servizio;
import it.govpay.model.Utenza.TIPO_UTENZA;

public class PagamentiController extends BaseController {
	
	private SerializationConfig serializationConfig;

     public PagamentiController(String nomeServizio,Logger log) {
		super(nomeServizio,log);
		
		this.serializationConfig = new SerializationConfig();
		this.serializationConfig.setExcludes(Arrays.asList("jsonIdFilter"));
     }



    public Response getPagamento(Authentication user, UriInfo uriInfo, HttpHeaders httpHeaders , String id) {
    	String methodName = "getPagamento";  
		String transactionId = ContextThreadLocal.get().getTransactionId();
		this.log.debug(MessageFormat.format(BaseController.LOG_MSG_ESECUZIONE_METODO_IN_CORSO, methodName)); 
		try{
			// autorizzazione sulla API
			this.isAuthorized(user, Arrays.asList(TIPO_UTENZA.OPERATORE, TIPO_UTENZA.APPLICAZIONE), Arrays.asList(Servizio.PAGAMENTI), Arrays.asList(Diritti.LETTURA));

			LeggiPagamentoPortaleDTO leggiPagamentoPortaleDTO = new LeggiPagamentoPortaleDTO(user);
			leggiPagamentoPortaleDTO.setId(id);
			leggiPagamentoPortaleDTO.setRisolviLink(true);
			// Autorizzazione su uo
			List<IdUnitaOperativa> idUnitaOperativa = AuthorizationManager.getUoAutorizzate(user);
			if(idUnitaOperativa == null) {
				throw AuthorizationManager.toNotAuthorizedExceptionNessunaUOAutorizzata(user);
			}
			leggiPagamentoPortaleDTO.setUnitaOperative(idUnitaOperativa);
			// autorizzazione sui tipi pendenza
			List<Long> idTipiVersamento = AuthorizationManager.getIdTipiVersamentoAutorizzati(user);
			if(idTipiVersamento == null) {
				throw AuthorizationManager.toNotAuthorizedExceptionNessunTipoVersamentoAutorizzato(user);
			}
			leggiPagamentoPortaleDTO.setIdTipiVersamento(idTipiVersamento);
			
			PagamentiPortaleDAO pagamentiPortaleDAO = new PagamentiPortaleDAO(); 
			
			ListaPagamentiPortaleDTO checkAutorizzazioniPagamentoDTO = new ListaPagamentiPortaleDTO(user);
			checkAutorizzazioniPagamentoDTO.setIdSessione(id);
			checkAutorizzazioniPagamentoDTO.setUnitaOperative(idUnitaOperativa);
			checkAutorizzazioniPagamentoDTO.setIdTipiVersamento(idTipiVersamento);
			ListaPagamentiPortaleDTOResponse checkAutorizzazioniPagamentoDTOResponse = pagamentiPortaleDAO.countPagamentiPortale(checkAutorizzazioniPagamentoDTO);
			
			if(checkAutorizzazioniPagamentoDTOResponse.getTotalResults() == 0)
				throw AuthorizationManager.toNotAuthorizedException(user, "Il pagamento riferisce delle pendenze che non sono disponibili per l'utenza.");
			
			LeggiPagamentoPortaleDTOResponse pagamentoPortaleDTOResponse = pagamentiPortaleDAO.leggiPagamentoPortale(leggiPagamentoPortaleDTO);
			
			it.govpay.backoffice.v1.beans.Pagamento response = PagamentiPortaleConverter.toRsModel(pagamentoPortaleDTOResponse);
			
			this.log.debug(MessageFormat.format(BaseController.LOG_MSG_ESECUZIONE_METODO_COMPLETATA, methodName)); 
			return this.handleResponseOk(Response.status(Status.OK).entity(response.toJSON(null)),transactionId).build();
		}catch (Exception e) {
			return this.handleException(uriInfo, httpHeaders, methodName, e, transactionId);
		} finally {
			this.log(ContextThreadLocal.get());
		}
    }

    public Response findPagamenti(Authentication user, UriInfo uriInfo, HttpHeaders httpHeaders , Integer pagina, Integer risultatiPerPagina, String ordinamento, String campi, String stato, String versante, String idSessionePortale, Boolean verificato, String dataDa, String dataA, String idDebitore) {
    	String methodName = "findPagamenti";  
		String transactionId = ContextThreadLocal.get().getTransactionId();
		this.log.debug(MessageFormat.format(BaseController.LOG_MSG_ESECUZIONE_METODO_IN_CORSO, methodName)); 
		try{
			// autorizzazione sulla API
			this.isAuthorized(user, Arrays.asList(TIPO_UTENZA.OPERATORE, TIPO_UTENZA.APPLICAZIONE), Arrays.asList(Servizio.PAGAMENTI), Arrays.asList(Diritti.LETTURA));
			
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
			
			if(dataDa!=null) {
				Date dataDaDate = SimpleDateFormatUtils.getDataDaConTimestamp(dataDa);
				listaPagamentiPortaleDTO.setDataDa(dataDaDate);
			}
			
			if(dataA!=null) {
				Date dataADate = SimpleDateFormatUtils.getDataAConTimestamp(dataA);
				listaPagamentiPortaleDTO.setDataA(dataADate);
			}
			
			listaPagamentiPortaleDTO.setVerificato(verificato);
			
			if(versante != null)
				listaPagamentiPortaleDTO.setVersante(versante);

			if(ordinamento != null)
				listaPagamentiPortaleDTO.setOrderBy(ordinamento);
			
			if(idSessionePortale != null)
				listaPagamentiPortaleDTO.setIdSessionePortale(idSessionePortale);
			
			if(idDebitore != null)
				listaPagamentiPortaleDTO.setIdDebitore(idDebitore);
			
			// Autorizzazione sui domini
			List<IdUnitaOperativa> idUnitaOperativas = AuthorizationManager.getUoAutorizzate(user);
			listaPagamentiPortaleDTO.setUnitaOperative(idUnitaOperativas);

			// autorizzazione sui tipi pendenza
			List<Long> idTipiVersamento = AuthorizationManager.getIdTipiVersamentoAutorizzati(user);
			listaPagamentiPortaleDTO.setIdTipiVersamento(idTipiVersamento);
			
			// INIT DAO
			
			PagamentiPortaleDAO pagamentiPortaleDAO = new PagamentiPortaleDAO();
			
			// CHIAMATA AL DAO
			
			ListaPagamentiPortaleDTOResponse pagamentoPortaleDTOResponse =  (idUnitaOperativas == null || idTipiVersamento == null) ? new ListaPagamentiPortaleDTOResponse(0, new ArrayList<>()) : pagamentiPortaleDAO.listaPagamentiPortale(listaPagamentiPortaleDTO);
			
			// CONVERT TO JSON DELLA RISPOSTA
			
			List<it.govpay.backoffice.v1.beans.PagamentoIndex> results = new ArrayList<>();
			for(LeggiPagamentoPortaleDTOResponse pagamentoPortale: pagamentoPortaleDTOResponse.getResults()) {
				
				results.add(PagamentiPortaleConverter.toRsModelIndex(pagamentoPortale));
			}
			
			Integer maxRisultatiInt = GovpayConfig.getInstance().getMaxRisultati();
			BigDecimal maxRisultati = new BigDecimal(maxRisultatiInt.intValue());
			
			ListaPagamentiPortale response = new ListaPagamentiPortale(results, this.getServicePath(uriInfo),
					pagamentoPortaleDTOResponse.getTotalResults(), pagina, risultatiPerPagina, maxRisultati);
			
			this.log.debug(MessageFormat.format(BaseController.LOG_MSG_ESECUZIONE_METODO_COMPLETATA, methodName)); 
			return this.handleResponseOk(Response.status(Status.OK).entity(response.toJSON(campi,this.serializationConfig)),transactionId).build();
		}catch (Exception e) {
			return this.handleException(uriInfo, httpHeaders, methodName, e, transactionId);
		} finally {
			this.log(ContextThreadLocal.get());
		}
    }

    @SuppressWarnings("unchecked")
	public Response updatePagamento(Authentication user, UriInfo uriInfo, HttpHeaders httpHeaders , java.io.InputStream is, String id) {
    	String methodName = "updatePagamento";  
		String transactionId = ContextThreadLocal.get().getTransactionId();
		this.log.debug(MessageFormat.format(BaseController.LOG_MSG_ESECUZIONE_METODO_IN_CORSO, methodName)); 
		try(ByteArrayOutputStream baos= new ByteArrayOutputStream();){
			// salvo il json ricevuto
			IOUtils.copy(is, baos);
			
			// autorizzazione sulla API
			this.isAuthorized(user, Arrays.asList(TIPO_UTENZA.OPERATORE, TIPO_UTENZA.APPLICAZIONE), Arrays.asList(Servizio.PAGAMENTI), Arrays.asList(Diritti.SCRITTURA));
			
			String jsonRequest = baos.toString();

			PagamentoPatchDTO verificaPagamentoDTO = new PagamentoPatchDTO(user);
			verificaPagamentoDTO.setIdSessione(id);
			// Autorizzazione sui domini
			List<IdUnitaOperativa> idUnitaOperativa = AuthorizationManager.getUoAutorizzate(user);
			if(idUnitaOperativa == null) {
				throw AuthorizationManager.toNotAuthorizedExceptionNessunaUOAutorizzata(user);
			}
			verificaPagamentoDTO.setUnitaOperative(idUnitaOperativa);
			// autorizzazione sui tipi pendenza
			List<Long> idTipiVersamento = AuthorizationManager.getIdTipiVersamentoAutorizzati(user);
			if(idTipiVersamento == null) {
				throw AuthorizationManager.toNotAuthorizedExceptionNessunTipoVersamentoAutorizzato(user);
			}
			verificaPagamentoDTO.setIdTipiVersamento(idTipiVersamento);
			
			List<PatchOp> lstOp = new ArrayList<>();
			try {
				List<java.util.LinkedHashMap<?,?>> lst = JSONSerializable.parse(jsonRequest, List.class);
				for(java.util.LinkedHashMap<?,?> map: lst) {
					PatchOp op = new PatchOp();
					String opText = (String) map.get("op");
					OpEnum opFromValue = OpEnum.fromValue(opText);

					if(StringUtils.isNotEmpty(opText) && opFromValue == null)
						throw new ValidationException("Il campo op non e' valido.");

					op.setOp(opFromValue);
					op.setPath((String) map.get("path"));
					op.setValue(map.get("value"));
					op.validate();
					lstOp.add(op);
				}
			} catch (ServiceException e) {
				lstOp = JSONSerializable.parse(jsonRequest, List.class);
			}
			
			for(PatchOp op : lstOp) {
				if(op.getPath().contains(PagamentiPortaleDAO.PATH_NOTA)) {
					this.setSottotipoEvento(EventoContext.SOTTOTIPO_EVENTO_NOTA);
					break;
				}
			}
			
			verificaPagamentoDTO.setOp(PatchOpConverter.toModel(lstOp));

			PagamentiPortaleDAO pagamentiPortaleDAO = new PagamentiPortaleDAO();
			
			ListaPagamentiPortaleDTO checkAutorizzazioniPagamentoDTO = new ListaPagamentiPortaleDTO(user);
			checkAutorizzazioniPagamentoDTO.setIdSessione(id);
			checkAutorizzazioniPagamentoDTO.setUnitaOperative(idUnitaOperativa);
			checkAutorizzazioniPagamentoDTO.setIdTipiVersamento(idTipiVersamento);
			ListaPagamentiPortaleDTOResponse checkAutorizzazioniPagamentoDTOResponse = pagamentiPortaleDAO.countPagamentiPortale(checkAutorizzazioniPagamentoDTO);
			
			if(checkAutorizzazioniPagamentoDTOResponse.getTotalResults() == 0)
				throw AuthorizationManager.toNotAuthorizedException(user, "Il pagamento riferisce delle pendenze che non sono disponibili per l'utenza.");
			
			LeggiPagamentoPortaleDTOResponse pagamentoPortaleDTOResponse = pagamentiPortaleDAO.patch(verificaPagamentoDTO);
			
			it.govpay.backoffice.v1.beans.Pagamento response = PagamentiPortaleConverter.toRsModel(pagamentoPortaleDTOResponse);

			this.log.debug(MessageFormat.format(BaseController.LOG_MSG_ESECUZIONE_METODO_COMPLETATA, methodName)); 
			return this.handleResponseOk(Response.status(Status.OK).entity(response.toJSON(null)),transactionId).build();
			
		}catch (Exception e) {
			return this.handleException(uriInfo, httpHeaders, methodName, e, transactionId);
		} finally {
			this.log(ContextThreadLocal.get());
		}
    }

}


