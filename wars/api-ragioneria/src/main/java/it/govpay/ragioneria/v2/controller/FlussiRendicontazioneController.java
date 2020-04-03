package it.govpay.ragioneria.v2.controller;

import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;
import javax.ws.rs.core.UriInfo;

import org.openspcoop2.utils.service.context.ContextThreadLocal;
import org.slf4j.Logger;
import org.springframework.security.core.Authentication;

import it.govpay.bd.model.IdUnitaOperativa;
import it.govpay.core.autorizzazione.AuthorizationManager;
import it.govpay.core.dao.pagamenti.RendicontazioniDAO;
import it.govpay.core.dao.pagamenti.dto.LeggiFrDTO;
import it.govpay.core.dao.pagamenti.dto.LeggiFrDTOResponse;
import it.govpay.core.dao.pagamenti.dto.ListaFrDTO;
import it.govpay.core.dao.pagamenti.dto.ListaFrDTOResponse;
import it.govpay.core.utils.SimpleDateFormatUtils;
import it.govpay.model.Acl.Diritti;
import it.govpay.model.Acl.Servizio;
import it.govpay.model.Fr.StatoFr;
import it.govpay.model.Utenza.TIPO_UTENZA;
import it.govpay.ragioneria.v2.beans.FlussiRendicontazione;
import it.govpay.ragioneria.v2.beans.FlussoRendicontazione;
import it.govpay.ragioneria.v2.beans.FlussoRendicontazioneIndex;
import it.govpay.ragioneria.v2.beans.StatoFlussoRendicontazione;
import it.govpay.ragioneria.v2.beans.converter.FlussiRendicontazioneConverter;


public class FlussiRendicontazioneController extends BaseController {

     public FlussiRendicontazioneController(String nomeServizio,Logger log) {
		super(nomeServizio,log);
     }



    public Response getFlussoRendicontazione(Authentication user, UriInfo uriInfo, HttpHeaders httpHeaders , String idFlusso) {
    	String methodName = "getFlussoRendicontazione";  
		String transactionId = ContextThreadLocal.get().getTransactionId();
		this.log.debug(MessageFormat.format(BaseController.LOG_MSG_ESECUZIONE_METODO_IN_CORSO, methodName)); 
		try{
			// autorizzazione sulla API
			this.isAuthorized(user, Arrays.asList(TIPO_UTENZA.APPLICAZIONE), Arrays.asList(Servizio.API_RAGIONERIA), Arrays.asList(Diritti.LETTURA));
			
			String accept = MediaType.APPLICATION_JSON;
			if(httpHeaders.getRequestHeaders().containsKey("Accept")) {
				accept = httpHeaders.getRequestHeaders().get("Accept").get(0).toLowerCase();
			}
			
			// Parametri - > DTO Input
			
			LeggiFrDTO leggiRendicontazioneDTO = new LeggiFrDTO(user, idFlusso);
			
			// INIT DAO
			
			RendicontazioniDAO rendicontazioniDAO = new RendicontazioniDAO();
			
			// CHIAMATA AL DAO
			
			LeggiFrDTOResponse leggiRendicontazioneDTOResponse = rendicontazioniDAO.leggiFlussoRendicontazione(leggiRendicontazioneDTO);
					
			// controllo che il dominio sia autorizzato
			if(leggiRendicontazioneDTOResponse.getDominio() != null && !AuthorizationManager.isDominioAuthorized(user, leggiRendicontazioneDTOResponse.getDominio().getCodDominio())) {
				throw AuthorizationManager.toNotAuthorizedException(user,leggiRendicontazioneDTOResponse.getDominio().getCodDominio(), null);
			}
			// controllo uo
			List<IdUnitaOperativa> uo = AuthorizationManager.getUoAutorizzate(user);
			leggiRendicontazioneDTO = new LeggiFrDTO(user, idFlusso);
			leggiRendicontazioneDTO.setUnitaOperative(uo);
			LeggiFrDTOResponse checkAutorizzazioneRendicontazioneDTOResponse = rendicontazioniDAO.checkAutorizzazioneFlussoRendicontazione(leggiRendicontazioneDTO);
			
			// controllo che il dominio sia autorizzato
			if(!checkAutorizzazioneRendicontazioneDTOResponse.isAuthorized()) {
				throw AuthorizationManager.toNotAuthorizedException(user,"Il flusso non contiente dei pagamenti associati a Unita' Operative autorizzate.");
			}
			
			// CONVERT TO JSON DELLA RISPOSTA
			if(accept.toLowerCase().contains(MediaType.APPLICATION_XML)) {
				byte[] response = leggiRendicontazioneDTOResponse.getFr().getXml();
				this.log.debug(MessageFormat.format(BaseController.LOG_MSG_ESECUZIONE_METODO_COMPLETATA, methodName)); 
				return this.handleResponseOk(Response.status(Status.OK).entity(new String(response)).type(MediaType.APPLICATION_XML),transactionId).build();
			} else {
				FlussoRendicontazione response = FlussiRendicontazioneConverter.toRsModel(leggiRendicontazioneDTOResponse.getFr()); 
				this.log.debug(MessageFormat.format(BaseController.LOG_MSG_ESECUZIONE_METODO_COMPLETATA, methodName)); 
				return this.handleResponseOk(Response.status(Status.OK).entity(response.toJSON(null)).type(MediaType.APPLICATION_JSON),transactionId).build();
			}
			
		}catch (Exception e) {
			return this.handleException(uriInfo, httpHeaders, methodName, e, transactionId);
		} finally {
			this.log(ContextThreadLocal.get());
		}
    }



    public Response findFlussiRendicontazione(Authentication user, UriInfo uriInfo, HttpHeaders httpHeaders , Integer pagina, Integer risultatiPerPagina, String ordinamento, String dataDa, String dataA, String idDominio, String stato) {
    	String methodName = "findFlussiRendicontazione";  
		String transactionId = ContextThreadLocal.get().getTransactionId();
		this.log.debug(MessageFormat.format(BaseController.LOG_MSG_ESECUZIONE_METODO_IN_CORSO, methodName)); 
		try{
			// autorizzazione sulla API
			this.isAuthorized(user, Arrays.asList(TIPO_UTENZA.APPLICAZIONE), Arrays.asList(Servizio.API_RAGIONERIA), Arrays.asList(Diritti.LETTURA));

			// Parametri - > DTO Input
			
			ListaFrDTO findRendicontazioniDTO = new ListaFrDTO(user);
			findRendicontazioniDTO.setIdDominio(idDominio);
			findRendicontazioniDTO.setLimit(risultatiPerPagina);
			findRendicontazioniDTO.setPagina(pagina);
			findRendicontazioniDTO.setOrderBy(ordinamento);
			if(dataDa != null) {
				Date dataDaDate = SimpleDateFormatUtils.getDataDaConTimestamp(dataDa, "dataDa");
				findRendicontazioniDTO.setDataDa(dataDaDate);
			}
			if(dataA != null) {
				Date dataADate = SimpleDateFormatUtils.getDataAConTimestamp(dataA, "dataA");
				findRendicontazioniDTO.setDataA(dataADate);
			}
			if(stato != null) {
				StatoFlussoRendicontazione sfr = StatoFlussoRendicontazione.fromValue(stato);
				
				if(sfr != null) {
					switch (sfr) {
					case ACQUISITO:
						findRendicontazioniDTO.setStato(StatoFr.ACCETTATA);
						break;
					case ANOMALO:
						findRendicontazioniDTO.setStato(StatoFr.ANOMALA);
						break;
					case RIFIUTATO:
						findRendicontazioniDTO.setStato(StatoFr.RIFIUTATA);
						break;
					}
				}
			}
			
			// Autorizzazione sulle uo
			List<IdUnitaOperativa> uo = AuthorizationManager.getUoAutorizzate(user);
			findRendicontazioniDTO.setUnitaOperative(uo);
			
			RendicontazioniDAO rendicontazioniDAO = new RendicontazioniDAO();
			
			// CHIAMATA AL DAO
			
			ListaFrDTOResponse findRendicontazioniDTOResponse = uo != null ? rendicontazioniDAO.listaFlussiRendicontazioni(findRendicontazioniDTO) 
					: new ListaFrDTOResponse(0, new ArrayList<>());
			
			// CONVERT TO JSON DELLA RISPOSTA
			
			List<FlussoRendicontazioneIndex> collect = new ArrayList<>();
			
			for(LeggiFrDTOResponse res: findRendicontazioniDTOResponse.getResults()) {
				collect.add(FlussiRendicontazioneConverter.toRsIndexModel(res.getFr()));
			}
			
			FlussiRendicontazione response = new FlussiRendicontazione(collect, 
					uriInfo.getRequestUri(), findRendicontazioniDTOResponse.getTotalResults(), pagina, risultatiPerPagina);
			
			this.log.debug(MessageFormat.format(BaseController.LOG_MSG_ESECUZIONE_METODO_COMPLETATA, methodName)); 
			return this.handleResponseOk(Response.status(Status.OK).entity(response.toJSON(null)),transactionId).build();
			
		}catch (Exception e) {
			return this.handleException(uriInfo, httpHeaders, methodName, e, transactionId);
		} finally {
			this.log(ContextThreadLocal.get());
		}
    }


}


