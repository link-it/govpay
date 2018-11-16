package it.govpay.rs.v1.controllers.base;

import java.io.ByteArrayOutputStream;
import java.net.URLDecoder;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.List;

import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;
import javax.ws.rs.core.UriInfo;

import org.slf4j.Logger;

import it.gov.digitpa.schemas._2011.pagamenti.CtRicevutaTelematica;
import it.gov.digitpa.schemas._2011.pagamenti.CtRichiestaPagamentoTelematico;
import it.govpay.core.dao.pagamenti.RptDAO;
import it.govpay.core.dao.pagamenti.dto.LeggiRicevutaDTO;
import it.govpay.core.dao.pagamenti.dto.LeggiRicevutaDTO.FormatoRicevuta;
import it.govpay.core.dao.pagamenti.dto.LeggiRicevutaDTOResponse;
import it.govpay.core.dao.pagamenti.dto.LeggiRptDTO;
import it.govpay.core.dao.pagamenti.dto.LeggiRptDTOResponse;
import it.govpay.core.dao.pagamenti.dto.ListaRptDTO;
import it.govpay.core.dao.pagamenti.dto.ListaRptDTOResponse;
import it.govpay.core.rs.v1.beans.base.ListaRpp;
import it.govpay.core.rs.v1.beans.base.PendenzaIndex;
import it.govpay.core.rs.v1.beans.base.Rpp;
import it.govpay.core.rs.v1.beans.base.RppIndex;
import it.govpay.core.utils.GovpayConfig;
import it.govpay.core.utils.GpContext;
import it.govpay.core.utils.GpThreadLocal;
import it.govpay.core.utils.JaxbUtils;
import it.govpay.model.IAutorizzato;
import it.govpay.model.Rpt.StatoRpt;
import it.govpay.rs.BaseController;
import it.govpay.rs.v1.beans.converter.PendenzeConverter;
import it.govpay.rs.v1.beans.converter.RptConverter;



public class RppController extends BaseController {

	public RppController(String nomeServizio,Logger log) {
		super(nomeServizio,log, GovpayConfig.GOVPAY_BACKOFFICE_OPEN_API_FILE_NAME);
	}

	public Response rppGET(IAutorizzato user, UriInfo uriInfo, HttpHeaders httpHeaders , Integer pagina, Integer risultatiPerPagina, String ordinamento, String campi, String idDominio, String iuv, String ccp, String idA2A, String idPendenza, String esito, String idPagamento) {
		String methodName = "rppGET";  
		GpContext ctx = null;
		String transactionId = null;
		ByteArrayOutputStream baos= null;
		this.log.info(MessageFormat.format(it.govpay.rs.BaseController.LOG_MSG_ESECUZIONE_METODO_IN_CORSO, methodName)); 
		try{
			baos = new ByteArrayOutputStream();
			this.logRequest(uriInfo, httpHeaders, methodName, baos);

			ctx =  GpThreadLocal.get();
			transactionId = ctx.getTransactionId();

			// Parametri - > DTO Input

			ListaRptDTO listaRptDTO = new ListaRptDTO(user);
			listaRptDTO.setPagina(pagina);
			listaRptDTO.setLimit(risultatiPerPagina);

			if(esito != null)
				listaRptDTO.setStato(StatoRpt.valueOf(esito));

			if(idDominio != null)
				listaRptDTO.setIdDominio(idDominio);
			if(iuv != null)
				listaRptDTO.setIuv(iuv);
			if(ccp != null)
				listaRptDTO.setCcp(ccp);
			if(idA2A != null)
				listaRptDTO.setIdA2A(idA2A);
			if(idPendenza != null)
				listaRptDTO.setIdPendenza(idPendenza);

			if(idPagamento != null)
				listaRptDTO.setIdPagamento(idPagamento);

			if(ordinamento != null)
				listaRptDTO.setOrderBy(ordinamento);
			// INIT DAO

			RptDAO rptDAO = new RptDAO();

			// CHIAMATA AL DAO

			ListaRptDTOResponse listaRptDTOResponse = rptDAO.listaRpt(listaRptDTO);

			// CONVERT TO JSON DELLA RISPOSTA
			List<RppIndex> results = new ArrayList<>();
			for(LeggiRptDTOResponse leggiRptDtoResponse: listaRptDTOResponse.getResults()) {
				results.add(RptConverter.toRsModelIndex(leggiRptDtoResponse.getRpt()));
			}
			ListaRpp response = new ListaRpp(results, this.getServicePath(uriInfo), listaRptDTOResponse.getTotalResults(), pagina, risultatiPerPagina);

			this.logResponse(uriInfo, httpHeaders, methodName, response.toJSON(campi), 200);
			this.log.info(MessageFormat.format(it.govpay.rs.BaseController.LOG_MSG_ESECUZIONE_METODO_COMPLETATA, methodName)); 
			return this.handleResponseOk(Response.status(Status.OK).entity(response.toJSON(campi)),transactionId).build();
		}catch (Exception e) {
			return this.handleException(uriInfo, httpHeaders, methodName, e, transactionId);
		} finally {
			if(ctx != null) ctx.log();
		}
	}



	public Response rppIdDominioIuvCcpRtGET(IAutorizzato user, UriInfo uriInfo, HttpHeaders httpHeaders , String idDominio, String iuv, String ccp) {
		String methodName = "rppIdDominioIuvCcpRtGET";  
		GpContext ctx = null;
		String transactionId = null;
		ByteArrayOutputStream baos= null;
		this.log.info(MessageFormat.format(it.govpay.rs.BaseController.LOG_MSG_ESECUZIONE_METODO_IN_CORSO, methodName)); 


		String accept = "";
		if(httpHeaders.getRequestHeaders().containsKey("Accept")) {
			accept = httpHeaders.getRequestHeaders().get("Accept").get(0).toLowerCase();
		}

		try{
			baos = new ByteArrayOutputStream();
			this.logRequest(uriInfo, httpHeaders, methodName, baos);

			ctx =  GpThreadLocal.get();
			transactionId = ctx.getTransactionId();

			LeggiRicevutaDTO leggiPagamentoPortaleDTO = new LeggiRicevutaDTO(user);
			leggiPagamentoPortaleDTO.setIdDominio(idDominio);
			leggiPagamentoPortaleDTO.setIuv(iuv);
			ccp = ccp.contains("%") ? URLDecoder.decode(ccp,"UTF-8") : ccp;
			leggiPagamentoPortaleDTO.setCcp(ccp);

			RptDAO ricevuteDAO = new RptDAO(); 

			LeggiRicevutaDTOResponse ricevutaDTOResponse = null; 

			if(accept.toLowerCase().contains(MediaType.APPLICATION_OCTET_STREAM)) {
				leggiPagamentoPortaleDTO.setFormato(FormatoRicevuta.RAW);
				ricevutaDTOResponse = ricevuteDAO.leggiRt(leggiPagamentoPortaleDTO);
				
				this.logResponse(uriInfo, httpHeaders, methodName, ricevutaDTOResponse.getRpt().getXmlRt(), 200);
				this.log.info(MessageFormat.format(it.govpay.rs.BaseController.LOG_MSG_ESECUZIONE_METODO_COMPLETATA, methodName)); 
				return this.handleResponseOk(Response.status(Status.OK).type(MediaType.APPLICATION_OCTET_STREAM).entity(new String(ricevutaDTOResponse.getRpt().getXmlRt())),transactionId).build();
			} else {
				if(accept.toLowerCase().contains("application/pdf")) {
					leggiPagamentoPortaleDTO.setFormato(FormatoRicevuta.PDF);
					ricevutaDTOResponse = ricevuteDAO.leggiRt(leggiPagamentoPortaleDTO);
					String rtPdfEntryName = idDominio +"_"+ iuv + "_"+ ccp + ".pdf";
					byte[] b = ricevutaDTOResponse.getPdf(); 

					this.logResponse(uriInfo, httpHeaders, methodName, b, 200);
					this.log.info(MessageFormat.format(it.govpay.rs.BaseController.LOG_MSG_ESECUZIONE_METODO_COMPLETATA, methodName)); 
					return this.handleResponseOk(Response.status(Status.OK).type("application/pdf").entity(b).header("content-disposition", "attachment; filename=\""+rtPdfEntryName+"\""),transactionId).build();
				} else if(accept.toLowerCase().contains(MediaType.APPLICATION_JSON)) {
					leggiPagamentoPortaleDTO.setFormato(FormatoRicevuta.JSON);
					ricevutaDTOResponse = ricevuteDAO.leggiRt(leggiPagamentoPortaleDTO);
					CtRicevutaTelematica rt = JaxbUtils.toRT(ricevutaDTOResponse.getRpt().getXmlRt());
					this.logResponse(uriInfo, httpHeaders, methodName, ricevutaDTOResponse.getRpt().getXmlRt(), 200);
					this.log.info(MessageFormat.format(it.govpay.rs.BaseController.LOG_MSG_ESECUZIONE_METODO_COMPLETATA, methodName)); 
					return this.handleResponseOk(Response.status(Status.OK).type(MediaType.APPLICATION_JSON).entity(rt),transactionId).build();
				} else {
					leggiPagamentoPortaleDTO.setFormato(FormatoRicevuta.XML);
					ricevutaDTOResponse = ricevuteDAO.leggiRt(leggiPagamentoPortaleDTO);
					CtRicevutaTelematica rt = JaxbUtils.toRT(ricevutaDTOResponse.getRpt().getXmlRt());
					this.logResponse(uriInfo, httpHeaders, methodName, ricevutaDTOResponse.getRpt().getXmlRt(), 200);
					this.log.info(MessageFormat.format(it.govpay.rs.BaseController.LOG_MSG_ESECUZIONE_METODO_COMPLETATA, methodName)); 
					return this.handleResponseOk(Response.status(Status.OK).type(MediaType.TEXT_XML).entity(rt),transactionId).build();
				}
			}
		}catch (Exception e) {
			return this.handleException(uriInfo, httpHeaders, methodName, e, transactionId);
		} finally {
			if(ctx != null) ctx.log();
		}    
	}



	public Response rppIdDominioIuvCcpRptGET(IAutorizzato user, UriInfo uriInfo, HttpHeaders httpHeaders , String idDominio, String iuv, String ccp) {
		String methodName = "rppIdDominioIuvCcpRtGET";  
		GpContext ctx = null;
		String transactionId = null;
		ByteArrayOutputStream baos= null;
		this.log.info(MessageFormat.format(it.govpay.rs.BaseController.LOG_MSG_ESECUZIONE_METODO_IN_CORSO, methodName)); 


		try{
			baos = new ByteArrayOutputStream();
			this.logRequest(uriInfo, httpHeaders, methodName, baos);

			ctx =  GpThreadLocal.get();
			transactionId = ctx.getTransactionId();

			LeggiRptDTO leggiRptDTO = new LeggiRptDTO(user);
			leggiRptDTO.setIdDominio(idDominio);
			leggiRptDTO.setIuv(iuv);
			ccp = ccp.contains("%") ? URLDecoder.decode(ccp,"UTF-8") : ccp;
			leggiRptDTO.setCcp(ccp);

			RptDAO ricevuteDAO = new RptDAO(); 

			LeggiRptDTOResponse leggiRptDTOResponse = ricevuteDAO.leggiRpt(leggiRptDTO);

			CtRichiestaPagamentoTelematico rpt = JaxbUtils.toRPT(leggiRptDTOResponse.getRpt().getXmlRpt());
			return this.handleResponseOk(Response.status(Status.OK).entity(rpt),transactionId).build();
		}catch (Exception e) {
			return this.handleException(uriInfo, httpHeaders, methodName, e, transactionId);
		} finally {
			if(ctx != null) ctx.log();
		} 
	}



	public Response rppIdDominioIuvCcpGET(IAutorizzato user, UriInfo uriInfo, HttpHeaders httpHeaders , String idDominio, String iuv, String ccp) {
		String methodName = "rppIdDominioIuvCcpGET";  
		GpContext ctx = null;
		String transactionId = null;		
		ByteArrayOutputStream baos= null;
		this.log.info(MessageFormat.format(it.govpay.rs.BaseController.LOG_MSG_ESECUZIONE_METODO_IN_CORSO, methodName)); 

		try{
			baos = new ByteArrayOutputStream();
			this.logRequest(uriInfo, httpHeaders, methodName, baos);

			ctx =  GpThreadLocal.get();
			transactionId = ctx.getTransactionId();

			LeggiRptDTO leggiRptDTO = new LeggiRptDTO(user);
			leggiRptDTO.setIdDominio(idDominio);
			leggiRptDTO.setIuv(iuv);
			ccp = ccp.contains("%") ? URLDecoder.decode(ccp,"UTF-8") : ccp;
			leggiRptDTO.setCcp(ccp);

			RptDAO ricevuteDAO = new RptDAO(); 

			LeggiRptDTOResponse leggiRptDTOResponse = ricevuteDAO.leggiRpt(leggiRptDTO);


			Rpp response =  RptConverter.toRsModel(leggiRptDTOResponse.getRpt());
			
			PendenzaIndex pendenza = PendenzeConverter.toRsModelIndex(leggiRptDTOResponse.getVersamento());
			
			response.setPendenza(pendenza);
			
			return this.handleResponseOk(Response.status(Status.OK).entity(response.toJSON(null)),transactionId).build();
		}catch (Exception e) {
			return this.handleException(uriInfo, httpHeaders, methodName, e, transactionId);
		} finally {
			if(ctx != null) ctx.log();
		}
	}


}


