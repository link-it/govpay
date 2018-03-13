package it.govpay.rs.v1.controllers.base;

import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;
import javax.ws.rs.core.UriInfo;

import org.slf4j.Logger;

import it.govpay.model.IAutorizzato;



public class RiscossioniController extends it.govpay.rs.BaseController {

     public RiscossioniController(String nomeServizio,Logger log) {
		super(nomeServizio,log);
     }



    public Response riscossioniIdDominioIuvIurIndiceGET(IAutorizzato user, UriInfo uriInfo, HttpHeaders httpHeaders , String idDominio, String iuv, String iur, String indice) {
        return Response.status(Status.INTERNAL_SERVER_ERROR).entity( "Not implemented" ).build();
    }



    public Response riscossioniGET(IAutorizzato user, UriInfo uriInfo, HttpHeaders httpHeaders , Integer pagina, Integer risultatiPerPagina, String ordinamento, String campi, String idDominio, String idA2A, String idPendenza, String stato, String dataRiscossioneDa, String dataRiscossioneA, String tipo) {
        return Response.status(Status.INTERNAL_SERVER_ERROR).entity( "Not implemented" ).build();
//    	String methodName = "flussiRendicontazioneGET";  
//		GpContext ctx = null;
//		ByteArrayOutputStream baos= null;
//		this.log.info("Esecuzione " + methodName + " in corso..."); 
//		try{
//			baos = new ByteArrayOutputStream();
//			this.logRequest(uriInfo, httpHeaders, methodName, baos);
//			
//			ctx =  GpThreadLocal.get();
//			
//			// Parametri - > DTO Input
//			
//			ListaRiscossioniDTO findRiscossioniDTO = new ListaRiscossioniDTO(user);
//			findRiscossioniDTO.setIdDominio(idDominio);
//			findRiscossioniDTO.setPagina(pagina);
//			findRiscossioniDTO.setLimit(risultatiPerPagina);
//			
//			// INIT DAO
//			
//			RiscossioniDAO rendicontazioniDAO = new RiscossioniDAO();
//			
//			// CHIAMATA AL DAO
//			
//			ListaRiscossioniDTOResponse findRiscossioniDTOResponse = rendicontazioniDAO.listaRiscossioni(findRiscossioniDTO);
//			
//			// CONVERT TO JSON DELLA RISPOSTA
//			
//			ListaRiscossioni response = new ListaRiscossioni(findRiscossioniDTOResponse.getResults().stream().map(t -> RiscossioniConverter.toRsIndexModel(t.getFr())).collect(Collectors.toList()), 
//					uriInfo.getRequestUri(), findRiscossioniDTOResponse.getTotalResults(), pagina, risultatiPerPagina);
//			
//			this.logResponse(uriInfo, httpHeaders, methodName, response.toJSON(null), 200);
//			this.log.info("Esecuzione " + methodName + " completata."); 
//			return Response.status(Status.OK).entity(response.toJSON(null)).build();
//			
//		}catch (Exception e) {
//			log.error("Errore interno durante la ricerca delle entrate: " + e.getMessage(), e);
//			FaultBean respKo = new FaultBean();
//			respKo.setCategoria(CategoriaEnum.INTERNO);
//			respKo.setCodice("");
//			respKo.setDescrizione(e.getMessage());
//			try {
//				this.logResponse(uriInfo, httpHeaders, methodName, respKo, 500);
//			}catch(Exception e1) {
//				log.error("Errore durante il log della risposta", e1);
//			}
//			return Response.status(Status.INTERNAL_SERVER_ERROR).entity(respKo).build();
//		} finally {
//			if(ctx != null) ctx.log();
//		}
    }


}


