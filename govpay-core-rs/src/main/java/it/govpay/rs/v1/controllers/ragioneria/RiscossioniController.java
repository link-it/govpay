package it.govpay.rs.v1.controllers.ragioneria;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;
import javax.ws.rs.core.UriInfo;

import org.slf4j.Logger;

import it.govpay.bd.pagamento.filters.PagamentoFilter.TIPO_PAGAMENTO;
import it.govpay.core.dao.pagamenti.RiscossioniDAO;
import it.govpay.core.dao.pagamenti.dto.LeggiRiscossioneDTO;
import it.govpay.core.dao.pagamenti.dto.LeggiRiscossioneDTOResponse;
import it.govpay.core.dao.pagamenti.dto.ListaRiscossioniDTO;
import it.govpay.core.dao.pagamenti.dto.ListaRiscossioniDTOResponse;
import it.govpay.core.rs.v1.beans.ragioneria.ListaRiscossioni;
import it.govpay.core.rs.v1.beans.ragioneria.Riscossione;
import it.govpay.core.rs.v1.beans.ragioneria.RiscossioneIndex;
import it.govpay.core.rs.v1.beans.ragioneria.StatoRiscossione;
import it.govpay.core.rs.v1.beans.ragioneria.TipoRiscossione;
import it.govpay.core.utils.GovpayConfig;
import it.govpay.core.utils.GpContext;
import it.govpay.core.utils.GpThreadLocal;
import it.govpay.model.IAutorizzato;
import it.govpay.model.Pagamento.Stato;
import it.govpay.rs.v1.beans.ragioneria.converter.RiscossioniConverter;



public class RiscossioniController extends it.govpay.rs.BaseController {

     public RiscossioniController(String nomeServizio,Logger log) {
		super(nomeServizio,log, GovpayConfig.GOVPAY_BACKOFFICE_OPEN_API_FILE_NAME);
     }



    public Response riscossioniIdDominioIuvIurIndiceGET(IAutorizzato user, UriInfo uriInfo, HttpHeaders httpHeaders , String idDominio, String iuv, String iur, Integer indice) {
    	String methodName = "riscossioniIdDominioIuvIurIndiceGET";  
		GpContext ctx = null;
		String transactionId = null;
		ByteArrayOutputStream baos= null;
		this.log.info("Esecuzione " + methodName + " in corso..."); 
		try{
			baos = new ByteArrayOutputStream();
			this.logRequest(uriInfo, httpHeaders, methodName, baos);
			
			ctx =  GpThreadLocal.get();
			transactionId = ctx.getTransactionId();
			
			// Parametri - > DTO Input
			
			LeggiRiscossioneDTO getRiscossioneDTO = new LeggiRiscossioneDTO(user, idDominio, iuv, iur, Integer.valueOf(indice));
			
			// INIT DAO
			
			RiscossioniDAO riscossioniDAO = new RiscossioniDAO();
			
			// CHIAMATA AL DAO
			
			LeggiRiscossioneDTOResponse getRiscossioneDTOResponse = riscossioniDAO.leggiRiscossione(getRiscossioneDTO);
			
			// CONVERT TO JSON DELLA RISPOSTA
			
			Riscossione response = RiscossioniConverter.toRsModel(getRiscossioneDTOResponse.getPagamento());
			
			this.logResponse(uriInfo, httpHeaders, methodName, response.toJSON(null), 200);
			this.log.info("Esecuzione " + methodName + " completata."); 
			return this.handleResponseOk(Response.status(Status.OK).entity(response.toJSON(null)),transactionId).build();
			
		}catch (Exception e) {
			return handleException(uriInfo, httpHeaders, methodName, e, transactionId);
		} finally {
			if(ctx != null) ctx.log();
		}
    }



    public Response riscossioniGET(IAutorizzato user, UriInfo uriInfo, HttpHeaders httpHeaders , Integer pagina, Integer risultatiPerPagina, String ordinamento, String campi, String idDominio, String idA2A, String idPendenza, String stato, Date dataRiscossioneDa, Date dataRiscossioneA, String tipo) {
    	String methodName = "riscossioniGET";  
		GpContext ctx = null;
		String transactionId = null;
		ByteArrayOutputStream baos= null;
		this.log.info("Esecuzione " + methodName + " in corso..."); 
		try{
			baos = new ByteArrayOutputStream();
			this.logRequest(uriInfo, httpHeaders, methodName, baos);
			
			ctx =  GpThreadLocal.get();
			transactionId = ctx.getTransactionId();
			
			// Parametri - > DTO Input
			
			ListaRiscossioniDTO findRiscossioniDTO = new ListaRiscossioniDTO(user);
			findRiscossioniDTO.setIdDominio(idDominio);
			findRiscossioniDTO.setPagina(pagina);
			findRiscossioniDTO.setLimit(risultatiPerPagina);
			findRiscossioniDTO.setDataRiscossioneA(dataRiscossioneA);
			findRiscossioniDTO.setDataRiscossioneDa(dataRiscossioneDa);
			findRiscossioniDTO.setIdA2A(idA2A);
			findRiscossioniDTO.setIdPendenza(idPendenza);
			findRiscossioniDTO.setOrderBy(ordinamento);
			if(stato !=null) {
				StatoRiscossione statoRisc = StatoRiscossione.fromValue(stato);
				switch(statoRisc) {
				case INCASSATA: findRiscossioniDTO.setStato(Stato.INCASSATO);
					break;
				case RISCOSSA: findRiscossioniDTO.setStato(Stato.PAGATO);
					break;
				default:
					break;
				
				}
			}

			if(tipo !=null)
				findRiscossioniDTO.setTipo(TIPO_PAGAMENTO.valueOf(TipoRiscossione.fromValue(tipo).toString()));
			
			
			
			// INIT DAO
			
			RiscossioniDAO riscossioniDAO = new RiscossioniDAO();
			
			// CHIAMATA AL DAO
			
			ListaRiscossioniDTOResponse findRiscossioniDTOResponse = riscossioniDAO.listaRiscossioni(findRiscossioniDTO);
			
			// CONVERT TO JSON DELLA RISPOSTA
			
			List<RiscossioneIndex> lst = new ArrayList<>();
			
			for(LeggiRiscossioneDTOResponse result: findRiscossioniDTOResponse.getResults()) {
				lst.add(RiscossioniConverter.toRsModelIndex(result.getPagamento()));
			}
			

			ListaRiscossioni response = new ListaRiscossioni(lst, 
					uriInfo.getRequestUri(), findRiscossioniDTOResponse.getTotalResults(), pagina, risultatiPerPagina);
			
			this.logResponse(uriInfo, httpHeaders, methodName, response.toJSON(campi), 200);
			this.log.info("Esecuzione " + methodName + " completata."); 
			return this.handleResponseOk(Response.status(Status.OK).entity(response.toJSON(campi)),transactionId).build();
			
		}catch (Exception e) {
			return handleException(uriInfo, httpHeaders, methodName, e, transactionId);
		} finally {
			if(ctx != null) ctx.log();
		}
    }


}


