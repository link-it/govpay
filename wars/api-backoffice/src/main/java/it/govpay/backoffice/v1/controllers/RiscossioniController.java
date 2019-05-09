package it.govpay.backoffice.v1.controllers;

import java.io.ByteArrayOutputStream;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;
import javax.ws.rs.core.UriInfo;

import org.apache.commons.lang.time.DateUtils;
import org.openspcoop2.utils.service.context.IContext;
import org.slf4j.Logger;
import org.springframework.security.core.Authentication;

import it.govpay.backoffice.v1.beans.ListaRiscossioni;
import it.govpay.backoffice.v1.beans.Riscossione;
import it.govpay.backoffice.v1.beans.StatoRiscossione;
import it.govpay.backoffice.v1.beans.TipoRiscossione;
import it.govpay.backoffice.v1.beans.converter.RiscossioniConverter;
import it.govpay.bd.pagamento.filters.PagamentoFilter.TIPO_PAGAMENTO;
import it.govpay.core.autorizzazione.AuthorizationManager;
import it.govpay.core.dao.pagamenti.RiscossioniDAO;
import it.govpay.core.dao.pagamenti.dto.LeggiRiscossioneDTO;
import it.govpay.core.dao.pagamenti.dto.LeggiRiscossioneDTOResponse;
import it.govpay.core.dao.pagamenti.dto.ListaRiscossioniDTO;
import it.govpay.core.dao.pagamenti.dto.ListaRiscossioniDTOResponse;
import it.govpay.core.utils.GovpayConfig;
import it.govpay.core.utils.GpThreadLocal;
import it.govpay.core.utils.SimpleDateFormatUtils;
import it.govpay.core.utils.validator.ValidatoreIdentificativi;
import it.govpay.model.Acl.Diritti;
import it.govpay.model.Acl.Servizio;
import it.govpay.model.Pagamento.Stato;
import it.govpay.model.Utenza.TIPO_UTENZA;



public class RiscossioniController extends BaseController {

     public RiscossioniController(String nomeServizio,Logger log) {
		super(nomeServizio,log, GovpayConfig.GOVPAY_BACKOFFICE_OPEN_API_FILE_NAME);
     }



    public Response riscossioniIdDominioIuvIurIndiceGET(Authentication user, UriInfo uriInfo, HttpHeaders httpHeaders , String idDominio, String iuv, String iur, Integer indice) {
    	String methodName = "riscossioniIdDominioIuvIurIndiceGET";  
		IContext ctx = null;
		String transactionId = null;
		ByteArrayOutputStream baos= null;
		this.log.debug(MessageFormat.format(BaseController.LOG_MSG_ESECUZIONE_METODO_IN_CORSO, methodName)); 
		try{
			baos = new ByteArrayOutputStream();
			this.logRequest(uriInfo, httpHeaders, methodName, baos);
			
			ctx =  GpThreadLocal.get();
			transactionId = ctx.getTransactionId();
			
			// autorizzazione sulla API
			this.isAuthorized(user, Arrays.asList(TIPO_UTENZA.OPERATORE, TIPO_UTENZA.APPLICAZIONE), Arrays.asList(Servizio.RENDICONTAZIONI_E_INCASSI), Arrays.asList(Diritti.LETTURA));
			
			ValidatoreIdentificativi validatoreId = ValidatoreIdentificativi.newInstance();
			validatoreId.validaIdDominio("idDominio", idDominio);
			
			// Parametri - > DTO Input
			
			LeggiRiscossioneDTO getRiscossioneDTO = new LeggiRiscossioneDTO(user, idDominio, iuv, iur, Integer.valueOf(indice));
			
			// INIT DAO
			
			RiscossioniDAO applicazioniDAO = new RiscossioniDAO();
			
			// CHIAMATA AL DAO
			
			LeggiRiscossioneDTOResponse getRiscossioneDTOResponse = applicazioniDAO.leggiRiscossione(getRiscossioneDTO);
			
			// controllo che il dominio sia autorizzato
			if(!AuthorizationManager.isDominioAuthorized(user, getRiscossioneDTOResponse.getDominio().getCodDominio())) {
				throw AuthorizationManager.toNotAuthorizedException(user,getRiscossioneDTOResponse.getDominio().getCodDominio(), null);
			}

			
			// CONVERT TO JSON DELLA RISPOSTA
			
			Riscossione response = RiscossioniConverter.toRsModel(getRiscossioneDTOResponse.getPagamento());
			
			this.logResponse(uriInfo, httpHeaders, methodName, response.toJSON(null), 200);
			this.log.debug(MessageFormat.format(BaseController.LOG_MSG_ESECUZIONE_METODO_COMPLETATA, methodName)); 
			return this.handleResponseOk(Response.status(Status.OK).entity(response.toJSON(null)),transactionId).build();
			
		}catch (Exception e) {
			return this.handleException(uriInfo, httpHeaders, methodName, e, transactionId);
		} finally {
			this.log(ctx);
		}
    }



    public Response riscossioniGET(Authentication user, UriInfo uriInfo, HttpHeaders httpHeaders , Integer pagina, Integer risultatiPerPagina, String ordinamento, String campi, String idDominio, String idA2A, String idPendenza, String stato, String dataDa, String dataA, String tipo, String iuv) {
    	String methodName = "riscossioniGET";  
		IContext ctx = null;
		String transactionId = null;
		ByteArrayOutputStream baos= null;
		this.log.debug(MessageFormat.format(BaseController.LOG_MSG_ESECUZIONE_METODO_IN_CORSO, methodName)); 
		try{
			baos = new ByteArrayOutputStream();
			this.logRequest(uriInfo, httpHeaders, methodName, baos);
			
			ctx =  GpThreadLocal.get();
			transactionId = ctx.getTransactionId();
			
			// autorizzazione sulla API
			this.isAuthorized(user, Arrays.asList(TIPO_UTENZA.OPERATORE, TIPO_UTENZA.APPLICAZIONE), Arrays.asList(Servizio.RENDICONTAZIONI_E_INCASSI), Arrays.asList(Diritti.LETTURA));
			
			// Parametri - > DTO Input
			
			ListaRiscossioniDTO findRiscossioniDTO = new ListaRiscossioniDTO(user);
			findRiscossioniDTO.setIdDominio(idDominio);
			findRiscossioniDTO.setLimit(risultatiPerPagina);
			findRiscossioniDTO.setPagina(pagina);
			
			if(dataDa != null) {
				Date dataDaDate = DateUtils.parseDate(dataDa, SimpleDateFormatUtils.datePatternsRest.toArray(new String[0]));
				findRiscossioniDTO.setDataRiscossioneDa(dataDaDate);
			}
			if(dataA != null) {
				Date dataADate = DateUtils.parseDate(dataA, SimpleDateFormatUtils.datePatternsRest.toArray(new String[0]));
				findRiscossioniDTO.setDataRiscossioneA(dataADate);
			}
			
			findRiscossioniDTO.setIdA2A(idA2A);
			findRiscossioniDTO.setIdPendenza(idPendenza);
			findRiscossioniDTO.setOrderBy(ordinamento);
			if(stato != null) {
				StatoRiscossione statoRisc = StatoRiscossione.fromValue(stato);
				if(statoRisc != null) {
					switch(statoRisc) {
					case INCASSATA: findRiscossioniDTO.setStato(Stato.INCASSATO);
						break;
					case RISCOSSA: findRiscossioniDTO.setStato(Stato.PAGATO);
						break;
					default:
						break;
					}				
				}
			}

			if(tipo!=null) {
				TipoRiscossione tipoRiscossione = TipoRiscossione.fromValue(tipo);
				if(tipoRiscossione != null) {
					findRiscossioniDTO.setTipo(TIPO_PAGAMENTO.valueOf(tipoRiscossione.toString()));
				}
			}
			
			findRiscossioniDTO.setIuv(iuv);
			
			// Autorizzazione sui domini
			List<String> domini = AuthorizationManager.getDominiAutorizzati(user);
//			if(domini == null) {
//				throw AuthorizationManager.toNotAuthorizedExceptionNessunDominioAutorizzato(user);
//			}
			findRiscossioniDTO.setCodDomini(domini);
			
			// INIT DAO
			
			RiscossioniDAO rendicontazioniDAO = new RiscossioniDAO();
			
			// CHIAMATA AL DAO
			
			ListaRiscossioniDTOResponse findRiscossioniDTOResponse = domini != null ? rendicontazioniDAO.listaRiscossioni(findRiscossioniDTO) : new ListaRiscossioniDTOResponse(0, new ArrayList<>());
			
			// CONVERT TO JSON DELLA RISPOSTA
			
			ListaRiscossioni response = new ListaRiscossioni(findRiscossioniDTOResponse.getResults().stream().map(t -> RiscossioniConverter.toRsModel(t.getPagamento())).collect(Collectors.toList()), 
					 this.getServicePath(uriInfo), findRiscossioniDTOResponse.getTotalResults(), pagina, risultatiPerPagina);
			
			this.logResponse(uriInfo, httpHeaders, methodName, response.toJSON(null), 200);
			this.log.debug(MessageFormat.format(BaseController.LOG_MSG_ESECUZIONE_METODO_COMPLETATA, methodName)); 
			return this.handleResponseOk(Response.status(Status.OK).entity(response.toJSON(null)),transactionId).build();
			
		}catch (Exception e) {
			return this.handleException(uriInfo, httpHeaders, methodName, e, transactionId);
		} finally {
			this.log(ctx);
		}
    }


}


