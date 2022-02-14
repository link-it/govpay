package it.govpay.ragioneria.v1.controller;

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

import it.govpay.bd.viste.model.Pagamento;
import it.govpay.core.autorizzazione.AuthorizationManager;
import it.govpay.core.beans.Costanti;
import it.govpay.core.dao.pagamenti.RiscossioniDAO;
import it.govpay.core.dao.pagamenti.dto.LeggiRiscossioneDTO;
import it.govpay.core.dao.pagamenti.dto.LeggiRiscossioneDTOResponse;
import it.govpay.core.dao.pagamenti.dto.ListaRiscossioniDTO;
import it.govpay.core.dao.pagamenti.dto.ListaRiscossioniDTOResponse;
import it.govpay.core.utils.SimpleDateFormatUtils;
import it.govpay.core.utils.validator.ValidatorFactory;
import it.govpay.core.utils.validator.ValidatoreIdentificativi;
import it.govpay.core.utils.validator.ValidatoreUtils;
import it.govpay.model.Acl.Diritti;
import it.govpay.model.Acl.Servizio;
import it.govpay.model.Pagamento.Stato;
import it.govpay.model.Pagamento.TipoPagamento;
import it.govpay.model.Utenza.TIPO_UTENZA;
import it.govpay.ragioneria.v1.beans.ListaRiscossioni;
import it.govpay.ragioneria.v1.beans.Riscossione;
import it.govpay.ragioneria.v1.beans.RiscossioneIndex;
import it.govpay.ragioneria.v1.beans.StatoRiscossione;
import it.govpay.ragioneria.v1.beans.TipoRiscossione;
import it.govpay.ragioneria.v1.beans.converter.RiscossioniConverter;



public class RiscossioniController extends BaseController {

     public RiscossioniController(String nomeServizio,Logger log) {
		super(nomeServizio,log);
     }



    public Response riscossioniIdDominioIuvIurIndiceGET(Authentication user, UriInfo uriInfo, HttpHeaders httpHeaders , String idDominio, String iuv, String iur, Integer indice) {
    	String methodName = "riscossioniIdDominioIuvIurIndiceGET";  
		String transactionId = ContextThreadLocal.get().getTransactionId();
		this.log.debug(MessageFormat.format(BaseController.LOG_MSG_ESECUZIONE_METODO_IN_CORSO, methodName)); 
		try{
			// autorizzazione sulla API
			this.isAuthorized(user, Arrays.asList(TIPO_UTENZA.APPLICAZIONE), Arrays.asList(Servizio.API_RAGIONERIA), Arrays.asList(Diritti.LETTURA));

			ValidatoreIdentificativi validatoreId = ValidatoreIdentificativi.newInstance();
			validatoreId.validaIdDominio("idDominio", idDominio);
			
			// Parametri - > DTO Input
			
			LeggiRiscossioneDTO getRiscossioneDTO = new LeggiRiscossioneDTO(user, idDominio, iuv, iur, Integer.valueOf(indice));
			
			// INIT DAO
			
			RiscossioniDAO riscossioniDAO = new RiscossioniDAO();
			
			// CHIAMATA AL DAO
			
			LeggiRiscossioneDTOResponse getRiscossioneDTOResponse = riscossioniDAO.leggiRiscossione(getRiscossioneDTO);
			
			// controllo che il dominio sia autorizzato
			if(!AuthorizationManager.isDominioAuthorized(user, getRiscossioneDTOResponse.getDominio().getCodDominio())) {
				throw AuthorizationManager.toNotAuthorizedException(user,getRiscossioneDTOResponse.getDominio().getCodDominio(), null);
			}
			
			// CONVERT TO JSON DELLA RISPOSTA
			
			Riscossione response = RiscossioniConverter.toRsModel(getRiscossioneDTOResponse.getPagamento());
			
			this.log.debug(MessageFormat.format(BaseController.LOG_MSG_ESECUZIONE_METODO_COMPLETATA, methodName)); 
			return this.handleResponseOk(Response.status(Status.OK).entity(response.toJSON(null)),transactionId).build();
			
		}catch (Exception e) {
			return this.handleException(uriInfo, httpHeaders, methodName, e, transactionId);
		} finally {
			this.log(ContextThreadLocal.get());
		}
    }



    public Response riscossioniGET(Authentication user, UriInfo uriInfo, HttpHeaders httpHeaders , Integer pagina, Integer risultatiPerPagina, String ordinamento, String campi, String idDominio, String idA2A, String idPendenza, String stato, String dataRiscossioneDa, String dataRiscossioneA, String tipo, Boolean metadatiPaginazione, Boolean maxRisultati) {
    	String methodName = "riscossioniGET";  
		String transactionId = ContextThreadLocal.get().getTransactionId();
		this.log.debug(MessageFormat.format(BaseController.LOG_MSG_ESECUZIONE_METODO_IN_CORSO, methodName)); 
		try{
			// autorizzazione sulla API
			this.isAuthorized(user, Arrays.asList(TIPO_UTENZA.APPLICAZIONE), Arrays.asList(Servizio.API_RAGIONERIA), Arrays.asList(Diritti.LETTURA));

			ValidatorFactory vf = ValidatorFactory.newInstance();
			ValidatoreUtils.validaRisultatiPerPagina(vf, Costanti.PARAMETRO_RISULTATI_PER_PAGINA, risultatiPerPagina);
			
			// Parametri - > DTO Input
			
			ValidatoreIdentificativi validatoreId = ValidatoreIdentificativi.newInstance();
			if(idDominio != null)
				validatoreId.validaIdDominio("idDominio", idDominio);
			
			ListaRiscossioniDTO findRiscossioniDTO = new ListaRiscossioniDTO(user);
			findRiscossioniDTO.setIdDominio(idDominio);
			findRiscossioniDTO.setLimit(risultatiPerPagina);
			findRiscossioniDTO.setPagina(pagina);
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
				} else {
					throw new ValidationException("Codifica inesistente per stato. Valore fornito [" + stato
							+ "] valori possibili " + ArrayUtils.toString(StatoRiscossione.values()));
				}
			}
			
			if(dataRiscossioneDa!=null) {
				Date dataDaDate = SimpleDateFormatUtils.getDataDaConTimestamp(dataRiscossioneDa, "dataDa");
				findRiscossioniDTO.setDataRiscossioneDa(dataDaDate);
			}
				
			
			if(dataRiscossioneA!=null) {
				Date dataADate = SimpleDateFormatUtils.getDataAConTimestamp(dataRiscossioneA, "dataA");
				findRiscossioniDTO.setDataRiscossioneA(dataADate);
			}

			if(tipo!=null) {
				TipoRiscossione tipoRiscossione = TipoRiscossione.fromValue(tipo);
				if(tipoRiscossione != null) {
					List<TipoPagamento> tipoEnum = new ArrayList<>();
					tipoEnum.add(TipoPagamento.valueOf(tipoRiscossione.toString()));
					findRiscossioniDTO.setTipo(tipoEnum);
				} else {
					throw new ValidationException("Codifica inesistente per tipo. Valore fornito [" + tipo
							+ "] valori possibili " + ArrayUtils.toString(TipoRiscossione.values()));
				}
			}
			
			// Autorizzazione sui domini
			List<String> domini = AuthorizationManager.getDominiAutorizzati(user);
			findRiscossioniDTO.setCodDomini(domini);
			
			findRiscossioniDTO.setEseguiCount(metadatiPaginazione);
			findRiscossioniDTO.setEseguiCountConLimit(maxRisultati);
			
			RiscossioniDAO riscossioniDAO = new RiscossioniDAO();
			
			// CHIAMATA AL DAO
			
			ListaRiscossioniDTOResponse findRiscossioniDTOResponse = domini != null ? riscossioniDAO.listaRiscossioni(findRiscossioniDTO) : new ListaRiscossioniDTOResponse(0L, new ArrayList<>());
			
			// CONVERT TO JSON DELLA RISPOSTA
			
			List<RiscossioneIndex> lst = new ArrayList<>();
			
			for(Pagamento result: findRiscossioniDTOResponse.getResults()) {
				lst.add(RiscossioniConverter.toRsModelIndex(result));
			}
			

			ListaRiscossioni response = new ListaRiscossioni(lst, 
					uriInfo.getRequestUri(), findRiscossioniDTOResponse.getTotalResults(), pagina, risultatiPerPagina);
			
			this.log.debug(MessageFormat.format(BaseController.LOG_MSG_ESECUZIONE_METODO_COMPLETATA, methodName)); 
			return this.handleResponseOk(Response.status(Status.OK).entity(response.toJSON(campi)),transactionId).build();
			
		}catch (Exception e) {
			return this.handleException(uriInfo, httpHeaders, methodName, e, transactionId);
		} finally {
			this.log(ContextThreadLocal.get());
		}
    }


}


