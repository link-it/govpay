package it.govpay.rs.v1.controllers.base;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;
import javax.ws.rs.core.UriInfo;

import org.slf4j.Logger;

import it.govpay.core.dao.commons.Versamento;
import it.govpay.core.dao.pagamenti.PendenzeDAO;
import it.govpay.core.dao.pagamenti.dto.LeggiPendenzaDTO;
import it.govpay.core.dao.pagamenti.dto.LeggiPendenzaDTOResponse;
import it.govpay.core.dao.pagamenti.dto.ListaPendenzeDTO;
import it.govpay.core.dao.pagamenti.dto.ListaPendenzeDTOResponse;
import it.govpay.core.dao.pagamenti.dto.PatchPendenzaDTO;
import it.govpay.core.dao.pagamenti.dto.PutPendenzaDTO;
import it.govpay.core.dao.pagamenti.dto.PutPendenzaDTOResponse;
import it.govpay.core.exceptions.GovPayException;
import it.govpay.core.rs.v1.beans.Avviso;
import it.govpay.core.rs.v1.beans.ListaPendenze;
import it.govpay.core.rs.v1.beans.Pendenza;
import it.govpay.core.rs.v1.beans.base.FaultBean;
import it.govpay.core.rs.v1.beans.base.FaultBean.CategoriaEnum;
import it.govpay.core.rs.v1.beans.base.PendenzaPost;
import it.govpay.core.rs.v1.beans.base.VocePendenza;
import it.govpay.core.utils.GpContext;
import it.govpay.core.utils.GpThreadLocal;
import it.govpay.model.IAutorizzato;
import it.govpay.model.Versamento.StatoVersamento;
import it.govpay.rs.BaseRsService;
import it.govpay.rs.v1.beans.converter.PagamentiPortaleConverter;
import it.govpay.rs.v1.beans.converter.PendenzeConverter;
import net.sf.json.JsonConfig;



public class PendenzeController extends it.govpay.rs.BaseController {

     public PendenzeController(String nomeServizio,Logger log) {
		super(nomeServizio,log);
     }
     
     public Response pendenzeIdA2AIdPendenzaGET(IAutorizzato user, UriInfo uriInfo, HttpHeaders httpHeaders , String idA2A, String idPendenza) {
    	 return pendenzeIdA2AIdPendenzaGET(user, uriInfo, httpHeaders, idA2A, idPendenza, true);
     }

    public Response pendenzeIdA2AIdPendenzaGET(IAutorizzato user, UriInfo uriInfo, HttpHeaders httpHeaders , String idA2A, String idPendenza, boolean linkListe) {
		String methodName = "getByIda2aIdPendenza";  
		GpContext ctx = null;
		String transactionId = null;
		ByteArrayOutputStream baos= null;
		this.log.info("Esecuzione " + methodName + " in corso..."); 

		try{
			baos = new ByteArrayOutputStream();
			this.logRequest(uriInfo, httpHeaders, methodName, baos);
			
			ctx =  GpThreadLocal.get();
			transactionId = ctx.getTransactionId();
			
			LeggiPendenzaDTO leggiPendenzaDTO = new LeggiPendenzaDTO(user);
			
			leggiPendenzaDTO.setCodA2A(idA2A);
			leggiPendenzaDTO.setCodPendenza(idPendenza);
			
			PendenzeDAO pendenzeDAO = new PendenzeDAO(); 
			
			LeggiPendenzaDTOResponse ricevutaDTOResponse = pendenzeDAO.leggiPendenza(leggiPendenzaDTO);

			Pendenza pendenza = PendenzeConverter.toRsModel(ricevutaDTOResponse.getVersamento(), ricevutaDTOResponse.getUnitaOperativa(), ricevutaDTOResponse.getApplicazione(), ricevutaDTOResponse.getDominio(), ricevutaDTOResponse.getLstSingoliVersamenti(),true);
			return this.handleResponseOk(Response.status(Status.OK).entity(pendenza.toJSON(null)),transactionId).build();
		}catch (Exception e) {
			return handleException(uriInfo, httpHeaders, methodName, e, transactionId);
		} finally {
			if(ctx != null) ctx.log();
		}
    }
    
    public Response pendenzeGET(IAutorizzato user, UriInfo uriInfo, HttpHeaders httpHeaders , Integer pagina, Integer risultatiPerPagina, String ordinamento, String campi, String idDominio, String idA2A, String idDebitore, String stato, String idPagamento) {
    	   return pendenzeGET(user, uriInfo, httpHeaders, pagina, risultatiPerPagina, ordinamento, campi, idDominio, idA2A, idDebitore, stato, idPagamento, true);
    }

    public Response pendenzeGET(IAutorizzato user, UriInfo uriInfo, HttpHeaders httpHeaders , Integer pagina, Integer risultatiPerPagina, String ordinamento, String campi, String idDominio, String idA2A, String idDebitore, String stato, String idPagamento, boolean linkListe) {
    	GpContext ctx = null;
    	String transactionId = null;
		ByteArrayOutputStream baos= null;
		String methodName = "pendenzeGET";
		try{
			this.log.info("Esecuzione " + methodName + " in corso...");
			baos = new ByteArrayOutputStream();
			this.logRequest(uriInfo, httpHeaders, methodName, baos);
			
			ctx =  GpThreadLocal.get();
			transactionId = ctx.getTransactionId();
			
			// Parametri - > DTO Input
			
			ListaPendenzeDTO listaPendenzeDTO = new ListaPendenzeDTO(user);
			
			listaPendenzeDTO.setPagina(pagina);
			listaPendenzeDTO.setLimit(risultatiPerPagina);
			
			if(stato != null)
				listaPendenzeDTO.setStato(StatoVersamento.valueOf(stato));
			
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
			// INIT DAO
			
			PendenzeDAO pendenzeDAO = new PendenzeDAO(); 
			
			// CHIAMATA AL DAO
			
			ListaPendenzeDTOResponse listaPendenzeDTOResponse = pendenzeDAO.listaPendenze(listaPendenzeDTO);
			
			// CONVERT TO JSON DELLA RISPOSTA
			
			List<it.govpay.core.rs.v1.beans.Pendenza> results = new ArrayList<it.govpay.core.rs.v1.beans.Pendenza>();
			for(LeggiPendenzaDTOResponse ricevutaDTOResponse: listaPendenzeDTOResponse.getResults()) {
				Pendenza rsModel = PendenzeConverter.toRsModel(ricevutaDTOResponse.getVersamento(), ricevutaDTOResponse.getUnitaOperativa(), ricevutaDTOResponse.getApplicazione(), ricevutaDTOResponse.getDominio(), ricevutaDTOResponse.getLstSingoliVersamenti(),true);
				results.add(rsModel);
			}
			
			ListaPendenze response = new ListaPendenze(results, this.getServicePath(uriInfo),
					listaPendenzeDTOResponse.getTotalResults(), pagina, risultatiPerPagina);
			
			this.logResponse(uriInfo, httpHeaders, methodName, response.toJSON(campi), 200);
			this.log.info("Esecuzione " + methodName + " completata."); 
			return this.handleResponseOk(Response.status(Status.OK).entity(response.toJSON(campi)),transactionId).build();
			
		}catch (Exception e) {
			return handleException(uriInfo, httpHeaders, methodName, e, transactionId);
		} finally {
			if(ctx != null) ctx.log();
		}
    }

    public Response pendenzeIdA2AIdPendenzaPATCH(IAutorizzato user, UriInfo uriInfo, HttpHeaders httpHeaders , String idA2A, String idPendenza, java.io.InputStream is) {
    	String methodName = "pendenzeIdA2AIdPendenzaPATCH";  
		GpContext ctx = null;
		String transactionId = null;
		ByteArrayOutputStream baos= null;
		this.log.info("Esecuzione " + methodName + " in corso..."); 
		try{
			baos = new ByteArrayOutputStream();
			// salvo il json ricevuto
			BaseRsService.copy(is, baos);
			this.logRequest(uriInfo, httpHeaders, methodName, baos);
			
			ctx =  GpThreadLocal.get();
			transactionId = ctx.getTransactionId();
			
//			String jsonRequest = baos.toString();
//			JsonConfig jsonConfig = new JsonConfig();
//			Map<String, Class<?>> classMap = new HashMap<String, Class<?>>();
//			jsonConfig.setClassMap(classMap);
//			PendenzaPost pendenzaPost= (PendenzaPost) PendenzaPost.parse(jsonRequest, PendenzaPost.class, jsonConfig);
			
			PendenzeDAO pendenzeDAO = new PendenzeDAO(); 
			
			PatchPendenzaDTO patchPendenzaDTO = new PatchPendenzaDTO(user);
			patchPendenzaDTO.setIdA2a(idA2A);
			patchPendenzaDTO.setIdPendenza(idPendenza);
//			patchPendenzaDTO.setStato(stato);
//			patchPendenzaDTO.setDescrizioneStato(descrizioneStato); //TODO model
			
			pendenzeDAO.cambioStato(patchPendenzaDTO);
			
			Status responseStatus = Status.OK;
			
			this.logResponse(uriInfo, httpHeaders, methodName, new byte[0], responseStatus.getStatusCode());

			this.log.info("Esecuzione " + methodName + " completata."); 
			return this.handleResponseOk(Response.status(responseStatus),transactionId).build();
		} catch(GovPayException e) {
			log.error("Errore durante il processo di pagamento", e);
			FaultBean respKo = new FaultBean();
			respKo.setCategoria(CategoriaEnum.OPERAZIONE);
			respKo.setCodice(e.getCodEsito().name());
			respKo.setDescrizione(e.getDescrizioneEsito());
			respKo.setDettaglio(e.getMessage());
			try {
				this.logResponse(uriInfo, httpHeaders, methodName, respKo.toJSON(null), 500);
			}catch(Exception e1) {
				log.error("Errore durante il log della risposta", e1);
			}
			return this.handleResponseOk(Response.status(Status.INTERNAL_SERVER_ERROR).entity(respKo.toJSON(null)),transactionId).build();
		}catch (Exception e) {
			return handleException(uriInfo, httpHeaders, methodName, e, transactionId);
		} finally {
			if(ctx != null) ctx.log();
		}
    }



    public Response pendenzeIdA2AIdPendenzaPUT(IAutorizzato user, UriInfo uriInfo, HttpHeaders httpHeaders , String idA2A, String idPendenza, java.io.InputStream is) {
    	String methodName = "pendenzeIdA2AIdPendenzaPUT";  
		GpContext ctx = null;
		String transactionId = null;
		ByteArrayOutputStream baos= null;
		this.log.info("Esecuzione " + methodName + " in corso..."); 
		try{
			baos = new ByteArrayOutputStream();
			// salvo il json ricevuto
			BaseRsService.copy(is, baos);
			this.logRequest(uriInfo, httpHeaders, methodName, baos);
			
			ctx =  GpThreadLocal.get();
			transactionId = ctx.getTransactionId();
			
			String jsonRequest = baos.toString();
			JsonConfig jsonConfig = new JsonConfig();
			Map<String, Class<?>> classMap = new HashMap<String, Class<?>>();
			classMap.put("voci", VocePendenza.class);
			jsonConfig.setClassMap(classMap);
			PendenzaPost pendenzaPost= (PendenzaPost) PendenzaPost.parse(jsonRequest, PendenzaPost.class, jsonConfig);
			
			Versamento versamento = PagamentiPortaleConverter.getVersamentoFromPendenza(pendenzaPost, idA2A, idPendenza); 
			
			
			PendenzeDAO pendenzeDAO = new PendenzeDAO(); 

			PutPendenzaDTO putVersamentoDTO = new PutPendenzaDTO(user);
			putVersamentoDTO.setVersamento(versamento);
			PutPendenzaDTOResponse createOrUpdate = pendenzeDAO.createOrUpdate(putVersamentoDTO);
			
			Avviso avviso = PendenzeConverter.toAvvisoRsModel(createOrUpdate.getVersamento(), createOrUpdate.getDominio(), createOrUpdate.getBarCode(), createOrUpdate.getQrCode());
			Status responseStatus = createOrUpdate.isCreated() ?  Status.CREATED : Status.OK;
			this.logResponse(uriInfo, httpHeaders, methodName, avviso.toJSON(null), responseStatus.getStatusCode());
			this.log.info("Esecuzione " + methodName + " completata."); 
			return this.handleResponseOk(Response.status(responseStatus).entity(avviso.toJSON(null)),transactionId).build();
//		} catch(GovPayException e) {
//			log.error("Errore durante il processo di pagamento", e);
//			FaultBean respKo = new FaultBean();
//			respKo.setCategoria(CategoriaEnum.OPERAZIONE);
//			respKo.setCodice(e.getCodEsito().name());
//			respKo.setDescrizione(e.getDescrizioneEsito());
//			respKo.setDettaglio(e.getMessage());
//			try {
//				this.logResponse(uriInfo, httpHeaders, methodName, respKo.toJSON(null), 500);
//			}catch(Exception e1) {
//				log.error("Errore durante il log della risposta", e1);
//			}
//			return this.handleResponseOk(Response.status(Status.INTERNAL_SERVER_ERROR).entity(respKo.toJSON(null)).build();
		}catch (Exception e) {
			return handleException(uriInfo, httpHeaders, methodName, e, transactionId);
		} finally {
			if(ctx != null) ctx.log();
		}
    }



    public Response pendenzeIdDominioIuvGET(IAutorizzato user, UriInfo uriInfo, HttpHeaders httpHeaders , String idDominio, String iuv) {
    	//client
        return this.handleResponseOk(Response.status(Status.INTERNAL_SERVER_ERROR).entity( "Not implemented [CLIENT]" ),null).build();
    }
	
}


