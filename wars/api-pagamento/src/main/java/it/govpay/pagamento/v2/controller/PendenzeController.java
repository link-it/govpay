package it.govpay.pagamento.v2.controller;

import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;
import javax.ws.rs.core.UriInfo;

import org.apache.commons.lang.ArrayUtils;
import org.openspcoop2.utils.json.ValidationException;
import org.openspcoop2.utils.service.context.ContextThreadLocal;
import org.slf4j.Logger;
import org.springframework.security.core.Authentication;

import it.govpay.bd.model.Dominio;
import it.govpay.bd.model.IdUnitaOperativa;
import it.govpay.bd.model.UnitaOperativa;
import it.govpay.core.autorizzazione.AuthorizationManager;
import it.govpay.core.autorizzazione.beans.GovpayLdapUserDetails;
import it.govpay.core.autorizzazione.utils.AutorizzazioneUtils;
import it.govpay.core.dao.pagamenti.PendenzeDAO;
import it.govpay.core.dao.pagamenti.dto.LeggiPendenzaDTO;
import it.govpay.core.dao.pagamenti.dto.LeggiPendenzaDTOResponse;
import it.govpay.core.dao.pagamenti.dto.ListaPendenzeDTO;
import it.govpay.core.dao.pagamenti.dto.ListaPendenzeDTOResponse;
import it.govpay.core.dao.pagamenti.dto.ListaPendenzeSmartOrderDTO;
import it.govpay.core.utils.GpContext;
import it.govpay.core.utils.SimpleDateFormatUtils;
import it.govpay.core.utils.validator.ValidatoreIdentificativi;
import it.govpay.model.Acl.Diritti;
import it.govpay.model.Acl.Servizio;
import it.govpay.model.TipoVersamento;
import it.govpay.model.Utenza.TIPO_UTENZA;
import it.govpay.pagamento.v2.beans.ListaPendenzeIndex;
import it.govpay.pagamento.v2.beans.Pendenza;
import it.govpay.pagamento.v2.beans.PendenzaIndex;
import it.govpay.pagamento.v2.beans.StatoPendenza;
import it.govpay.pagamento.v2.beans.converter.PendenzeConverter;



public class PendenzeController extends BaseController {

     public PendenzeController(String nomeServizio,Logger log) {
		super(nomeServizio,log);
     }
     
     public Response pendenzeIdA2AIdPendenzaGET(Authentication user, UriInfo uriInfo, HttpHeaders httpHeaders , String idA2A, String idPendenza) {
		String methodName = "getByIda2aIdPendenza";  
		String transactionId = ContextThreadLocal.get().getTransactionId();
		this.log.debug(MessageFormat.format(BaseController.LOG_MSG_ESECUZIONE_METODO_IN_CORSO, methodName)); 

		try{
			((GpContext) (ContextThreadLocal.get()).getApplicationContext()).getEventoCtx().setIdPendenza(idPendenza);
			((GpContext) (ContextThreadLocal.get()).getApplicationContext()).getEventoCtx().setIdA2A(idA2A);
			
			// autorizzazione sulla API
			this.isAuthorized(user, Arrays.asList(TIPO_UTENZA.CITTADINO, TIPO_UTENZA.APPLICAZIONE), Arrays.asList(Servizio.API_PAGAMENTI), Arrays.asList(Diritti.LETTURA));
			
			ValidatoreIdentificativi validatoreId = ValidatoreIdentificativi.newInstance();
			validatoreId.validaIdApplicazione("idA2A", idA2A);
			validatoreId.validaIdPendenza("idPendenza", idPendenza);
			
			LeggiPendenzaDTO leggiPendenzaDTO = new LeggiPendenzaDTO(user);
			
			leggiPendenzaDTO.setCodA2A(idA2A);
			leggiPendenzaDTO.setCodPendenza(idPendenza);
			
			PendenzeDAO pendenzeDAO = new PendenzeDAO(); 
			
			LeggiPendenzaDTOResponse ricevutaDTOResponse = pendenzeDAO.leggiPendenza(leggiPendenzaDTO);
			
 			Dominio dominio = ricevutaDTOResponse.getDominio();
			TipoVersamento tipoVersamento = ricevutaDTOResponse.getTipoVersamento();
			UnitaOperativa unitaOperativa = ricevutaDTOResponse.getUnitaOperativa();
			
			// controllo che il dominio, uo e tipo versamento siano autorizzati
			if(!AuthorizationManager.isTipoVersamentoUOAuthorized(leggiPendenzaDTO.getUser(), dominio.getCodDominio(), unitaOperativa.getCodUo(), tipoVersamento.getCodTipoVersamento())) {
				throw AuthorizationManager.toNotAuthorizedException(leggiPendenzaDTO.getUser(), dominio.getCodDominio(), unitaOperativa.getCodUo(), tipoVersamento.getCodTipoVersamento());
			}
			GovpayLdapUserDetails userDetails = AutorizzazioneUtils.getAuthenticationDetails(leggiPendenzaDTO.getUser());
			if(userDetails.getTipoUtenza().equals(TIPO_UTENZA.CITTADINO)) {
				if(!ricevutaDTOResponse.getVersamento().getAnagraficaDebitore().getCodUnivoco().equals(userDetails.getIdentificativo())) {
					throw AuthorizationManager.toNotAuthorizedException(leggiPendenzaDTO.getUser(), "la pendenza non appartiene al cittadino chiamante.");
				}
			}
			
			Pendenza pendenza =  PendenzeConverter.toRsModel(ricevutaDTOResponse,user);
			
			return this.handleResponseOk(Response.status(Status.OK).entity(pendenza.toJSON(null)),transactionId).build();
		}catch (Exception e) {
			return this.handleException(uriInfo, httpHeaders, methodName, e, transactionId);
		} finally {
			this.log(ContextThreadLocal.get());
		}
    }
    
    public Response pendenzeGET(Authentication user, UriInfo uriInfo, HttpHeaders httpHeaders , Integer pagina, Integer risultatiPerPagina, String ordinamento, String campi, String dataDa, String dataA, String idDominio, String idA2A, String idDebitore, String stato, String idPagamento, String direzione, String divisione, Boolean mostraSpontaneiNonPagati) {
    	String transactionId = ContextThreadLocal.get().getTransactionId();
		String methodName = "pendenzeGET"; 
		try{
			this.log.info("Esecuzione " + methodName + " in corso...");
			// autorizzazione sulla API
			this.isAuthorized(user, Arrays.asList(TIPO_UTENZA.ANONIMO, TIPO_UTENZA.CITTADINO, TIPO_UTENZA.APPLICAZIONE), Arrays.asList(Servizio.API_PAGAMENTI), Arrays.asList(Diritti.LETTURA));

			// Parametri - > DTO Input
			
			ListaPendenzeDTO listaPendenzeDTO = null;
			
			// solo l'utente cittadino deve visualizzare l'ordinamento smart
			if(AutorizzazioneUtils.getAuthenticationDetails(user).getTipoUtenza().equals(TIPO_UTENZA.CITTADINO)) {
				listaPendenzeDTO = new ListaPendenzeSmartOrderDTO(user);
			} else {
				listaPendenzeDTO = new ListaPendenzeDTO(user);
			}
			
			listaPendenzeDTO.setLimit(risultatiPerPagina);
			listaPendenzeDTO.setPagina(pagina);
			if(stato != null) {
				StatoPendenza statoPendenza = StatoPendenza.fromValue(stato);
				if(statoPendenza != null) {
					switch(statoPendenza) {
					case ANNULLATA: listaPendenzeDTO.setStato(it.govpay.model.StatoPendenza.ANNULLATA); break;
					case ESEGUITA: listaPendenzeDTO.setStato(it.govpay.model.StatoPendenza.ESEGUITA); break;
					case ESEGUITA_PARZIALE: listaPendenzeDTO.setStato(it.govpay.model.StatoPendenza.ESEGUITA_PARZIALE); break;
					case NON_ESEGUITA: listaPendenzeDTO.setStato(it.govpay.model.StatoPendenza.NON_ESEGUITA); break;
					case SCADUTA: listaPendenzeDTO.setStato(it.govpay.model.StatoPendenza.SCADUTA); break;
					}				
				} else {
					throw new ValidationException("Codifica inesistente per stato. Valore fornito [" + stato
							+ "] valori possibili " + ArrayUtils.toString(StatoPendenza.values()));
				}
			}
			
			if(idDominio != null)
				listaPendenzeDTO.setIdDominio(idDominio);
			if(idA2A != null)
				listaPendenzeDTO.setIdA2A(idA2A);
			if(idDebitore != null)
				listaPendenzeDTO.setIdDebitore(idDebitore);
			
			if(idPagamento != null)
				listaPendenzeDTO.setIdPagamento(idPagamento);
			
			if(ordinamento != null)
				listaPendenzeDTO.setOrderBy(ordinamento);
			
			if(dataDa!=null) {
				Date dataDaDate = SimpleDateFormatUtils.getDataDaConTimestamp(dataDa, "dataDa");
				listaPendenzeDTO.setDataDa(dataDaDate);
			}
				
			
			if(dataA!=null) {
				Date dataADate = SimpleDateFormatUtils.getDataAConTimestamp(dataA, "dataA");
				listaPendenzeDTO.setDataA(dataADate);
			}
			
			// Autorizzazione sulle UO
			List<IdUnitaOperativa> uoAutorizzate = AuthorizationManager.getUoAutorizzate(user);
			if(uoAutorizzate == null) {
				throw AuthorizationManager.toNotAuthorizedExceptionNessunaUOAutorizzata(user);
			}
			listaPendenzeDTO.setUnitaOperative(uoAutorizzate);
			
			// autorizzazione sui tipi pendenza
			List<Long> idTipiVersamento = AuthorizationManager.getIdTipiVersamentoAutorizzati(user);
			if(idTipiVersamento == null) {
				throw AuthorizationManager.toNotAuthorizedExceptionNessunTipoVersamentoAutorizzato(user);
			}
			listaPendenzeDTO.setIdTipiVersamento(idTipiVersamento);
			listaPendenzeDTO.setDirezione(direzione);
			listaPendenzeDTO.setDivisione(divisione);
			listaPendenzeDTO.setMostraSpontaneiNonPagati(mostraSpontaneiNonPagati);
			
			GovpayLdapUserDetails userDetails = AutorizzazioneUtils.getAuthenticationDetails(listaPendenzeDTO.getUser());
			if(userDetails.getTipoUtenza().equals(TIPO_UTENZA.CITTADINO)) {
				listaPendenzeDTO.setCfCittadino(userDetails.getIdentificativo()); 
			}
			
			PendenzeDAO pendenzeDAO = new PendenzeDAO(); 
			
			// CHIAMATA AL DAO
			
			ListaPendenzeDTOResponse listaPendenzeDTOResponse = null; 
			if(AutorizzazioneUtils.getAuthenticationDetails(user).getTipoUtenza().equals(TIPO_UTENZA.CITTADINO)) {
				listaPendenzeDTOResponse = pendenzeDAO.listaPendenzeSmartOrder((ListaPendenzeSmartOrderDTO) listaPendenzeDTO);
			} else {
				listaPendenzeDTOResponse = pendenzeDAO.listaPendenze(listaPendenzeDTO);
			}
			
			// CONVERT TO JSON DELLA RISPOSTA
			
			List<it.govpay.pagamento.v2.beans.PendenzaIndex> results = new ArrayList<>();
			for(LeggiPendenzaDTOResponse ricevutaDTOResponse: listaPendenzeDTOResponse.getResults()) {
				PendenzaIndex rsModel = PendenzeConverter.toRsModelIndex(ricevutaDTOResponse.getVersamento(),user);
				results.add(rsModel);
			}
			
			ListaPendenzeIndex response = new ListaPendenzeIndex(results, this.getServicePath(uriInfo),
					listaPendenzeDTOResponse.getTotalResults(), pagina, risultatiPerPagina);
			
			this.log.debug(MessageFormat.format(BaseController.LOG_MSG_ESECUZIONE_METODO_COMPLETATA, methodName)); 
			return this.handleResponseOk(Response.status(Status.OK).entity(response.toJSON(campi)),transactionId).build();
			
		}catch (Exception e) {
			return this.handleException(uriInfo, httpHeaders, methodName, e, transactionId);
		} finally {
			this.log(ContextThreadLocal.get());
		}
    }

}


