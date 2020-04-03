package it.govpay.backoffice.v1.controllers;


import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;
import javax.ws.rs.core.UriInfo;

import org.openspcoop2.utils.service.context.ContextThreadLocal;
import org.slf4j.Logger;
import org.springframework.security.core.Authentication;

import it.govpay.backoffice.v1.beans.ListaRendicontazioni;
import it.govpay.backoffice.v1.beans.RendicontazioneConFlussoEVocePendenza;
import it.govpay.backoffice.v1.beans.converter.RendicontazioniConverter;
import it.govpay.bd.model.IdUnitaOperativa;
import it.govpay.bd.viste.model.Rendicontazione;
import it.govpay.core.autorizzazione.AuthorizationManager;
import it.govpay.core.dao.pagamenti.RendicontazioniDAO;
import it.govpay.core.dao.pagamenti.dto.ListaRendicontazioniDTO;
import it.govpay.core.dao.pagamenti.dto.ListaRendicontazioniDTOResponse;
import it.govpay.core.utils.SimpleDateFormatUtils;
import it.govpay.model.Acl.Diritti;
import it.govpay.model.Acl.Servizio;
import it.govpay.model.Utenza.TIPO_UTENZA;

public class RendicontazioniController extends BaseController {

     public RendicontazioniController(String nomeServizio,Logger log) {
		super(nomeServizio,log);
     }



    public Response findRendicontazioni(Authentication user, UriInfo uriInfo, HttpHeaders httpHeaders , Integer pagina, Integer risultatiPerPagina, String ordinamento, String campi, 
    		String flussoRendicontazioneDataFlussoDa, String flussoRendicontazioneDataFlussoA, String dataRendicontazioneDa, String dataRendicontazioneA, 
    		String idFlusso, String iuv, List<String> direzione, List<String> divisione) {
    	String methodName = "findRendicontazioni";  
    	String transactionId = ContextThreadLocal.get().getTransactionId();
		this.log.debug(MessageFormat.format(BaseController.LOG_MSG_ESECUZIONE_METODO_IN_CORSO, methodName)); 
		try{
			// autorizzazione sulla API
			this.isAuthorized(user, Arrays.asList(TIPO_UTENZA.OPERATORE, TIPO_UTENZA.APPLICAZIONE), Arrays.asList(Servizio.RENDICONTAZIONI_E_INCASSI), Arrays.asList(Diritti.LETTURA));
			
			// Parametri - > DTO Input
			ListaRendicontazioniDTO findRendicontazioniDTO = new ListaRendicontazioniDTO(user);
			findRendicontazioniDTO.setLimit(risultatiPerPagina);
			findRendicontazioniDTO.setPagina(pagina);
			findRendicontazioniDTO.setOrderBy(ordinamento);
			Date dataFlussoDaDate = null;
			if(flussoRendicontazioneDataFlussoDa!=null) {
				dataFlussoDaDate = SimpleDateFormatUtils.getDataDaConTimestamp(flussoRendicontazioneDataFlussoDa, "flussoRendicontazione.dataFlussoDa", true); 
				findRendicontazioniDTO.setDataFlussoDa(dataFlussoDaDate);
			}

			Date dataFlussoADate = null;
			if(flussoRendicontazioneDataFlussoA!=null) {
				dataFlussoADate = SimpleDateFormatUtils.getDataAConTimestamp(flussoRendicontazioneDataFlussoA, "flussoRendicontazione.dataFlussoA", true);
				findRendicontazioniDTO.setDataFlussoA(dataFlussoADate);
			}
			
			Date dataRendicontazioneDaDate = null;
			if(dataRendicontazioneDa!=null) {
				dataRendicontazioneDaDate = SimpleDateFormatUtils.getDataDaConTimestamp(dataRendicontazioneDa, "dataDa", true); 
				findRendicontazioniDTO.setDataRendicontazioneDa(dataRendicontazioneDaDate);
			}

			Date dataRendicontazioneADate = null;
			if(dataRendicontazioneA!=null) {
				dataRendicontazioneADate =SimpleDateFormatUtils.getDataAConTimestamp(dataRendicontazioneA, "dataA", true); 
				findRendicontazioniDTO.setDataRendicontazioneA(dataRendicontazioneADate);
			}

			findRendicontazioniDTO.setCodFlusso(idFlusso);
			findRendicontazioniDTO.setIuv(iuv);
			findRendicontazioniDTO.setDirezione(direzione);
			findRendicontazioniDTO.setDivisione(divisione);
			
			// Autorizzazione sulle uo
			List<IdUnitaOperativa> uo = AuthorizationManager.getUoAutorizzate(user);
			findRendicontazioniDTO.setUnitaOperative(uo);
			
			RendicontazioniDAO rendicontazioniDAO = new RendicontazioniDAO();
			
			// CHIAMATA AL DAO
			
			ListaRendicontazioniDTOResponse findRendicontazioniDTOResponse =  uo != null ? rendicontazioniDAO.listaRendicontazioni(findRendicontazioniDTO)
					: new ListaRendicontazioniDTOResponse(0, new ArrayList<>());
			
			// CONVERT TO JSON DELLA RISPOSTA
			List<RendicontazioneConFlussoEVocePendenza> risultati = new ArrayList<RendicontazioneConFlussoEVocePendenza>();
			
			for (Rendicontazione rendicontazione : findRendicontazioniDTOResponse.getResults()) {
				risultati.add(RendicontazioniConverter.toRsModel(rendicontazione));
			}
			
			ListaRendicontazioni response = new ListaRendicontazioni(risultati,	this.getServicePath(uriInfo), findRendicontazioniDTOResponse.getTotalResults(), pagina, risultatiPerPagina);
			
			this.log.debug(MessageFormat.format(BaseController.LOG_MSG_ESECUZIONE_METODO_COMPLETATA, methodName)); 
			return this.handleResponseOk(Response.status(Status.OK).entity(response.toJSON(campi)),transactionId).build();
			
		}catch (Exception e) {
			return this.handleException(uriInfo, httpHeaders, methodName, e, transactionId);
		} finally {
			this.log(ContextThreadLocal.get());
		}
    }


}


