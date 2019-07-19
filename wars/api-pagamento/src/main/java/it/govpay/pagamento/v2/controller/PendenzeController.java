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

import org.apache.commons.lang.time.DateUtils;
import org.openspcoop2.utils.service.context.ContextThreadLocal;
import org.slf4j.Logger;
import org.springframework.security.core.Authentication;

import it.govpay.bd.model.Dominio;
import it.govpay.core.autorizzazione.AuthorizationManager;
import it.govpay.core.dao.pagamenti.PendenzeDAO;
import it.govpay.core.dao.pagamenti.dto.LeggiPendenzaDTO;
import it.govpay.core.dao.pagamenti.dto.LeggiPendenzaDTOResponse;
import it.govpay.core.dao.pagamenti.dto.ListaPendenzeConInformazioniIncassoDTO;
import it.govpay.core.dao.pagamenti.dto.ListaPendenzeDTOResponse;
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
import it.govpay.pagamento.v2.beans.converter.PendenzeConverter;



public class PendenzeController extends BaseController {

     public PendenzeController(String nomeServizio,Logger log) {
		super(nomeServizio,log);
     }
     
     public Response pendenzeIdA2AIdPendenzaGET(Authentication user, UriInfo uriInfo, HttpHeaders httpHeaders , String idA2A, String idPendenza) {
		String methodName = "getByIda2aIdPendenza";  
		String transactionId = this.context.getTransactionId();
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
			
			LeggiPendenzaDTOResponse ricevutaDTOResponse = pendenzeDAO.leggiPendenzaConInformazioniIncasso(leggiPendenzaDTO);
			
 			Dominio dominio = ricevutaDTOResponse.getDominio();
			TipoVersamento tipoVersamento = ricevutaDTOResponse.getTipoVersamento();
			// controllo che il dominio e tipo versamento siano autorizzati
			if(!AuthorizationManager.isTipoVersamentoDominioAuthorized(leggiPendenzaDTO.getUser(), dominio.getCodDominio(), tipoVersamento.getCodTipoVersamento())) {
				throw AuthorizationManager.toNotAuthorizedException(leggiPendenzaDTO.getUser(), dominio.getCodDominio(), tipoVersamento.getCodTipoVersamento());
			}

			Pendenza pendenza =  PendenzeConverter.toRsModel(ricevutaDTOResponse,user);
			
			return this.handleResponseOk(Response.status(Status.OK).entity(pendenza.toJSON(null)),transactionId).build();
		}catch (Exception e) {
			return this.handleException(uriInfo, httpHeaders, methodName, e, transactionId);
		} finally {
			this.log(this.context);
		}
    }
    
    public Response pendenzeGET(Authentication user, UriInfo uriInfo, HttpHeaders httpHeaders , Integer pagina, Integer risultatiPerPagina, String ordinamento, String campi, String dataDa, String dataA, String idDominio, String idA2A, String idDebitore, String stato, String idPagamento, String direzione, String divisione) {
    	String transactionId = this.context.getTransactionId();
		String methodName = "pendenzeGET"; 
		try{
			this.log.info("Esecuzione " + methodName + " in corso...");
			// autorizzazione sulla API
			this.isAuthorized(user, Arrays.asList(TIPO_UTENZA.ANONIMO, TIPO_UTENZA.CITTADINO, TIPO_UTENZA.APPLICAZIONE), Arrays.asList(Servizio.API_PAGAMENTI), Arrays.asList(Diritti.LETTURA));

			// Parametri - > DTO Input
			
			ListaPendenzeConInformazioniIncassoDTO listaPendenzeDTO = new ListaPendenzeConInformazioniIncassoDTO(user);
			
			listaPendenzeDTO.setLimit(risultatiPerPagina);
			listaPendenzeDTO.setPagina(pagina);
			listaPendenzeDTO.setStato(stato);
			
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
				Date dataDaDate = DateUtils.parseDate(dataDa, SimpleDateFormatUtils.datePatternsRest.toArray(new String[0]));
				listaPendenzeDTO.setDataDa(dataDaDate);
			}
				
			
			if(dataA!=null) {
				Date dataADate = DateUtils.parseDate(dataA, SimpleDateFormatUtils.datePatternsRest.toArray(new String[0]));
				listaPendenzeDTO.setDataA(dataADate);
			}
			
			// Autorizzazione sui domini
			List<Long> idDomini = AuthorizationManager.getIdDominiAutorizzati(user);
			if(idDomini == null) {
				throw AuthorizationManager.toNotAuthorizedExceptionNessunDominioAutorizzato(user);
			}
			listaPendenzeDTO.setIdDomini(idDomini);
			// autorizzazione sui tipi pendenza
			List<Long> idTipiVersamento = AuthorizationManager.getIdTipiVersamentoAutorizzati(user);
			if(idTipiVersamento == null) {
				throw AuthorizationManager.toNotAuthorizedExceptionNessunTipoVersamentoAutorizzato(user);
			}
			listaPendenzeDTO.setIdTipiVersamento(idTipiVersamento);
			listaPendenzeDTO.setDirezione(direzione);
			listaPendenzeDTO.setDivisione(divisione);
			
			PendenzeDAO pendenzeDAO = new PendenzeDAO(); 
			
			// CHIAMATA AL DAO
			
			ListaPendenzeDTOResponse listaPendenzeDTOResponse = pendenzeDAO.listaPendenzeConInformazioniIncasso(listaPendenzeDTO);
			
			// CONVERT TO JSON DELLA RISPOSTA
			
			List<it.govpay.pagamento.v2.beans.PendenzaIndex> results = new ArrayList<>();
			for(LeggiPendenzaDTOResponse ricevutaDTOResponse: listaPendenzeDTOResponse.getResults()) {
				PendenzaIndex rsModel = PendenzeConverter.toRsModelIndex(ricevutaDTOResponse.getVersamentoIncasso(),user);
				results.add(rsModel);
			}
			
			ListaPendenzeIndex response = new ListaPendenzeIndex(results, this.getServicePath(uriInfo),
					listaPendenzeDTOResponse.getTotalResults(), pagina, risultatiPerPagina);
			
			this.log.debug(MessageFormat.format(BaseController.LOG_MSG_ESECUZIONE_METODO_COMPLETATA, methodName)); 
			return this.handleResponseOk(Response.status(Status.OK).entity(response.toJSON(campi)),transactionId).build();
			
		}catch (Exception e) {
			return this.handleException(uriInfo, httpHeaders, methodName, e, transactionId);
		} finally {
			this.log(this.context);
		}
    }

}


