package it.govpay.rs.v1.controllers.base;

import java.io.ByteArrayOutputStream;
import java.util.Date;
import java.util.stream.Collectors;

import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;
import javax.ws.rs.core.UriInfo;

import org.slf4j.Logger;

import it.govpay.core.dao.pagamenti.RiscossioniDAO;
import it.govpay.core.dao.pagamenti.dto.LeggiRiscossioneDTO;
import it.govpay.core.dao.pagamenti.dto.LeggiRiscossioneDTOResponse;
import it.govpay.core.dao.pagamenti.dto.ListaRiscossioniDTO;
import it.govpay.core.dao.pagamenti.dto.ListaRiscossioniDTOResponse;
import it.govpay.core.dao.pagamenti.exception.RiscossioneNonTrovataException;
import it.govpay.core.rs.v1.beans.ListaRiscossioni;
import it.govpay.core.rs.v1.beans.Riscossione;
import it.govpay.core.rs.v1.beans.base.FaultBean;
import it.govpay.core.rs.v1.beans.base.FaultBean.CategoriaEnum;
import it.govpay.core.utils.GpContext;
import it.govpay.core.utils.GpThreadLocal;
import it.govpay.model.IAutorizzato;
import it.govpay.model.Pagamento.Stato;
import it.govpay.rs.v1.beans.converter.RiscossioniConverter;



public class RiscossioniController extends it.govpay.rs.BaseController {

     public RiscossioniController(String nomeServizio,Logger log) {
		super(nomeServizio,log);
     }



    public Response riscossioniIdDominioIuvIurIndiceGET(IAutorizzato user, UriInfo uriInfo, HttpHeaders httpHeaders , String idDominio, String iuv, String iur, Integer indice) {
    	String methodName = "riscossioniIdDominioIuvIurIndiceGET";  
		GpContext ctx = null;
		ByteArrayOutputStream baos= null;
		this.log.info("Esecuzione " + methodName + " in corso..."); 
		try{
			baos = new ByteArrayOutputStream();
			this.logRequest(uriInfo, httpHeaders, methodName, baos);
			
			ctx =  GpThreadLocal.get();
			
			// Parametri - > DTO Input
			
			LeggiRiscossioneDTO getRiscossioneDTO = new LeggiRiscossioneDTO(user, idDominio, iuv, iur, Integer.valueOf(indice));
			
			// INIT DAO
			
			RiscossioniDAO applicazioniDAO = new RiscossioniDAO();
			
			// CHIAMATA AL DAO
			
			LeggiRiscossioneDTOResponse getRiscossioneDTOResponse = applicazioniDAO.leggiRiscossione(getRiscossioneDTO);
			
			// CONVERT TO JSON DELLA RISPOSTA
			
			Riscossione response = RiscossioniConverter.toRsModel(getRiscossioneDTOResponse.getPagamento());
			
			this.logResponse(uriInfo, httpHeaders, methodName, response.toJSON(null), 200);
			this.log.info("Esecuzione " + methodName + " completata."); 
			return Response.status(Status.OK).entity(response.toJSON(null)).build();
			
		}catch (RiscossioneNonTrovataException e) {
			log.error(e.getMessage(), e);
			FaultBean respKo = new FaultBean();
			respKo.setCategoria(CategoriaEnum.OPERAZIONE);
			respKo.setCodice("");
			respKo.setDescrizione(e.getMessage());
			try {
				this.logResponse(uriInfo, httpHeaders, methodName, respKo, 500);
			}catch(Exception e1) {
				log.error("Errore durante il log della risposta", e1);
			}
			return Response.status(Status.NOT_FOUND).entity(respKo).build();
		} catch (Exception e) {
			log.error("Errore interno durante la ricerca delle Riscossioni: " + e.getMessage(), e);
			FaultBean respKo = new FaultBean();
			respKo.setCategoria(CategoriaEnum.INTERNO);
			respKo.setCodice("");
			respKo.setDescrizione(e.getMessage());
			try {
				this.logResponse(uriInfo, httpHeaders, methodName, respKo, 500);
			}catch(Exception e1) {
				log.error("Errore durante il log della risposta", e1);
			}
			return Response.status(Status.INTERNAL_SERVER_ERROR).entity(respKo).build();
		} finally {
			if(ctx != null) ctx.log();
		}
    }



    public Response riscossioniGET(IAutorizzato user, UriInfo uriInfo, HttpHeaders httpHeaders , Integer pagina, Integer risultatiPerPagina, String ordinamento, String campi, String idDominio, String idA2A, String idPendenza, String stato, Date dataRiscossioneDa, Date dataRiscossioneA, String tipo) {
    	String methodName = "riscossioniGET";  
		GpContext ctx = null;
		ByteArrayOutputStream baos= null;
		this.log.info("Esecuzione " + methodName + " in corso..."); 
		try{
			baos = new ByteArrayOutputStream();
			this.logRequest(uriInfo, httpHeaders, methodName, baos);
			
			ctx =  GpThreadLocal.get();
			
			// Parametri - > DTO Input
			
			ListaRiscossioniDTO findRiscossioniDTO = new ListaRiscossioniDTO(user);
			findRiscossioniDTO.setIdDominio(idDominio);
			findRiscossioniDTO.setPagina(pagina);
			findRiscossioniDTO.setLimit(risultatiPerPagina);
			findRiscossioniDTO.setDataRiscossioneA(dataRiscossioneA);
			findRiscossioniDTO.setDataRiscossioneDa(dataRiscossioneDa);
			findRiscossioniDTO.setIdA2A(idA2A);
			findRiscossioniDTO.setIdPendenza(idPendenza);
			findRiscossioniDTO.setStato(Stato.valueOf(stato));
			findRiscossioniDTO.setTipo(tipo);
			
			
			
			// INIT DAO
			
			RiscossioniDAO rendicontazioniDAO = new RiscossioniDAO();
			
			// CHIAMATA AL DAO
			
			ListaRiscossioniDTOResponse findRiscossioniDTOResponse = rendicontazioniDAO.listaRiscossioni(findRiscossioniDTO);
			
			// CONVERT TO JSON DELLA RISPOSTA
			
			ListaRiscossioni response = new ListaRiscossioni(findRiscossioniDTOResponse.getResults().stream().map(t -> RiscossioniConverter.toRsModel(t.getPagamento())).collect(Collectors.toList()), 
					uriInfo.getRequestUri(), findRiscossioniDTOResponse.getTotalResults(), pagina, risultatiPerPagina);
			
			this.logResponse(uriInfo, httpHeaders, methodName, response.toJSON(null), 200);
			this.log.info("Esecuzione " + methodName + " completata."); 
			return Response.status(Status.OK).entity(response.toJSON(null)).build();
			
		}catch (Exception e) {
			log.error("Errore interno durante la ricerca delle entrate: " + e.getMessage(), e);
			FaultBean respKo = new FaultBean();
			respKo.setCategoria(CategoriaEnum.INTERNO);
			respKo.setCodice("");
			respKo.setDescrizione(e.getMessage());
			try {
				this.logResponse(uriInfo, httpHeaders, methodName, respKo, 500);
			}catch(Exception e1) {
				log.error("Errore durante il log della risposta", e1);
			}
			return Response.status(Status.INTERNAL_SERVER_ERROR).entity(respKo).build();
		} finally {
			if(ctx != null) ctx.log();
		}
    }


}


