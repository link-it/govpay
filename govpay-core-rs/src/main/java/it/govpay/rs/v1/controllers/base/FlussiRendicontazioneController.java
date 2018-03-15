package it.govpay.rs.v1.controllers.base;

import java.io.ByteArrayOutputStream;
import java.util.stream.Collectors;

import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;
import javax.ws.rs.core.UriInfo;

import org.slf4j.Logger;

import it.govpay.core.dao.pagamenti.RendicontazioniDAO;
import it.govpay.core.dao.pagamenti.dto.LeggiRendicontazioneDTO;
import it.govpay.core.dao.pagamenti.dto.LeggiRendicontazioneDTOResponse;
import it.govpay.core.dao.pagamenti.dto.ListaRendicontazioniDTO;
import it.govpay.core.dao.pagamenti.dto.ListaRendicontazioniDTOResponse;
import it.govpay.core.exceptions.BaseExceptionV1;
import it.govpay.core.rs.v1.beans.FlussoRendicontazione;
import it.govpay.core.rs.v1.beans.ListaFlussiRendicontazione;
import it.govpay.core.utils.GpContext;
import it.govpay.core.utils.GpThreadLocal;
import it.govpay.model.IAutorizzato;
import it.govpay.rs.BaseController;
import it.govpay.rs.v1.beans.converter.FlussiRendicontazioneConverter;



public class FlussiRendicontazioneController extends BaseController {

     public FlussiRendicontazioneController(String nomeServizio,Logger log) {
		super(nomeServizio,log);
     }



    public Response flussiRendicontazioneIdFlussoGET(IAutorizzato user, UriInfo uriInfo, HttpHeaders httpHeaders , String idFlusso) {
    	String methodName = "flussiRendicontazioneIdFlussoGET";  
		GpContext ctx = null;
		ByteArrayOutputStream baos= null;
		this.log.info("Esecuzione " + methodName + " in corso..."); 
		try{
			baos = new ByteArrayOutputStream();
			this.logRequest(uriInfo, httpHeaders, methodName, baos);
			
			ctx =  GpThreadLocal.get();
			
			// Parametri - > DTO Input
			
			LeggiRendicontazioneDTO leggiRendicontazioneDTO = new LeggiRendicontazioneDTO(user, idFlusso);
			
			// INIT DAO
			
			RendicontazioniDAO rendicontazioniDAO = new RendicontazioniDAO();
			
			// CHIAMATA AL DAO
			
			LeggiRendicontazioneDTOResponse leggiRendicontazioneDTOResponse = rendicontazioniDAO.leggiRendicontazione(leggiRendicontazioneDTO);
					
			
			// CONVERT TO JSON DELLA RISPOSTA
			
			FlussoRendicontazione response = FlussiRendicontazioneConverter.toRsModel(leggiRendicontazioneDTOResponse.getFr()); 
			
			this.logResponse(uriInfo, httpHeaders, methodName, response.toJSON(null), 200);
			this.log.info("Esecuzione " + methodName + " completata."); 
			return Response.status(Status.OK).entity(response.toJSON(null)).build();
			
		}catch (BaseExceptionV1 e) {
			return handleException(uriInfo, httpHeaders, methodName, e);
		}catch (Exception e) {
			return handleException(uriInfo, httpHeaders, methodName, e);
		} finally {
			if(ctx != null) ctx.log();
		}
    }



    public Response flussiRendicontazioneGET(IAutorizzato user, UriInfo uriInfo, HttpHeaders httpHeaders , Integer pagina, Integer risultatiPerPagina, String idDominio) {
    	String methodName = "flussiRendicontazioneGET";  
		GpContext ctx = null;
		ByteArrayOutputStream baos= null;
		this.log.info("Esecuzione " + methodName + " in corso..."); 
		try{
			baos = new ByteArrayOutputStream();
			this.logRequest(uriInfo, httpHeaders, methodName, baos);
			
			ctx =  GpThreadLocal.get();
			
			// Parametri - > DTO Input
			
			ListaRendicontazioniDTO findRendicontazioniDTO = new ListaRendicontazioniDTO(user);
			findRendicontazioniDTO.setIdDominio(idDominio);
			findRendicontazioniDTO.setPagina(pagina);
			findRendicontazioniDTO.setLimit(risultatiPerPagina);
			findRendicontazioniDTO.setOrderBy("-dataAcquisizione");
			
			// INIT DAO
			
			RendicontazioniDAO rendicontazioniDAO = new RendicontazioniDAO();
			
			// CHIAMATA AL DAO
			
			ListaRendicontazioniDTOResponse findRendicontazioniDTOResponse = rendicontazioniDAO.listaRendicontazioni(findRendicontazioniDTO);
			
			// CONVERT TO JSON DELLA RISPOSTA
			
			ListaFlussiRendicontazione response = new ListaFlussiRendicontazione(findRendicontazioniDTOResponse.getResults().stream().map(t -> FlussiRendicontazioneConverter.toRsIndexModel(t.getFr())).collect(Collectors.toList()), 
					uriInfo.getRequestUri(), findRendicontazioniDTOResponse.getTotalResults(), pagina, risultatiPerPagina);
			
			this.logResponse(uriInfo, httpHeaders, methodName, response.toJSON(null), 200);
			this.log.info("Esecuzione " + methodName + " completata."); 
			return Response.status(Status.OK).entity(response.toJSON(null)).build();
			
		}catch (BaseExceptionV1 e) {
			return handleException(uriInfo, httpHeaders, methodName, e);
		}catch (Exception e) {
			return handleException(uriInfo, httpHeaders, methodName, e);
		} finally {
			if(ctx != null) ctx.log();
		}
    }


}


