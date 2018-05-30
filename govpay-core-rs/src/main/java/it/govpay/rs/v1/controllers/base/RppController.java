package it.govpay.rs.v1.controllers.base;

import java.io.ByteArrayOutputStream;
import java.net.URLDecoder;
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
import it.govpay.core.dao.pagamenti.dto.LeggiRicevutaDTOResponse;
import it.govpay.core.dao.pagamenti.dto.LeggiRptDTO;
import it.govpay.core.dao.pagamenti.dto.LeggiRptDTOResponse;
import it.govpay.core.dao.pagamenti.dto.ListaRptDTO;
import it.govpay.core.dao.pagamenti.dto.ListaRptDTOResponse;
import it.govpay.core.rs.v1.beans.ListaRpp;
import it.govpay.core.rs.v1.beans.Pendenza;
import it.govpay.core.rs.v1.beans.Rpp;
import it.govpay.core.utils.GovpayConfig;
import it.govpay.core.utils.GpContext;
import it.govpay.core.utils.GpThreadLocal;
import it.govpay.core.utils.JaxbUtils;
import it.govpay.model.IAutorizzato;
import it.govpay.model.Rpt.StatoRpt;
import it.govpay.rs.BaseController;
import it.govpay.rs.v1.beans.converter.PendenzeConverter;
import it.govpay.rs.v1.beans.converter.RptConverter;
import it.govpay.stampe.pdf.rt.utils.RicevutaPagamentoUtils;



public class RppController extends BaseController {

	public RppController(String nomeServizio,Logger log) {
		super(nomeServizio,log, GovpayConfig.GOVPAY_BACKOFFICE_OPEN_API_FILE);
	}

	public Response rppGET(IAutorizzato user, UriInfo uriInfo, HttpHeaders httpHeaders , Integer pagina, Integer risultatiPerPagina, String ordinamento, String campi, String idDominio, String iuv, String ccp, String idA2A, String idPendenza, String esito, String idPagamento) {
		String methodName = "rppGET";  
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
			List<Rpp> results = new ArrayList<Rpp>();
			for(LeggiRptDTOResponse leggiRptDtoResponse: listaRptDTOResponse.getResults()) {
				results.add(RptConverter.toRsModel(leggiRptDtoResponse.getRpt(),leggiRptDtoResponse.getVersamento(),leggiRptDtoResponse.getApplicazione()));
			}
			ListaRpp response = new ListaRpp(results, this.getServicePath(uriInfo), listaRptDTOResponse.getTotalResults(), pagina, risultatiPerPagina);

			this.logResponse(uriInfo, httpHeaders, methodName, response.toJSON(campi), 200);
			this.log.info("Esecuzione " + methodName + " completata."); 
			return this.handleResponseOk(Response.status(Status.OK).entity(response.toJSON(campi)),transactionId).build();
		}catch (Exception e) {
			return handleException(uriInfo, httpHeaders, methodName, e, transactionId);
		} finally {
			if(ctx != null) ctx.log();
		}
	}



	public Response rppIdDominioIuvCcpRtGET(IAutorizzato user, UriInfo uriInfo, HttpHeaders httpHeaders , String idDominio, String iuv, String ccp) {
		String methodName = "rppIdDominioIuvCcpRtGET";  
		GpContext ctx = null;
		String transactionId = null;
		ByteArrayOutputStream baos= null;
		this.log.info("Esecuzione " + methodName + " in corso..."); 


		String accept = null;
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

			LeggiRicevutaDTOResponse ricevutaDTOResponse = ricevuteDAO.leggiRt(leggiPagamentoPortaleDTO);

			if(accept.toLowerCase().contains(MediaType.APPLICATION_OCTET_STREAM)) {
				this.logResponse(uriInfo, httpHeaders, methodName, ricevutaDTOResponse.getRpt().getXmlRt(), 200);
				this.log.info("Esecuzione " + methodName + " completata."); 
				return this.handleResponseOk(Response.status(Status.OK).type(MediaType.APPLICATION_OCTET_STREAM).entity(new String(ricevutaDTOResponse.getRpt().getXmlRt())),transactionId).build();
			} else {
				CtRicevutaTelematica rt = JaxbUtils.toRT(ricevutaDTOResponse.getRpt().getXmlRt());

				if(accept.toLowerCase().contains("application/pdf")) {
					ByteArrayOutputStream baos1 = new ByteArrayOutputStream();
					String auxDigit = ricevutaDTOResponse.getDominio().getAuxDigit() + "";
					String applicationCode = String.format("%02d", ricevutaDTOResponse.getDominio().getStazione().getApplicationCode());
					RicevutaPagamentoUtils.getPdfRicevutaPagamento(ricevutaDTOResponse.getDominio().getLogo(), ricevutaDTOResponse.getVersamento().getCausaleVersamento(), rt, null, auxDigit, applicationCode, baos1, this.log);
					String rtPdfEntryName = idDominio +"_"+ iuv + "_"+ ccp + ".pdf";

					byte[] b = baos1.toByteArray();

					this.logResponse(uriInfo, httpHeaders, methodName, b, 200);
					this.log.info("Esecuzione " + methodName + " completata."); 
					return this.handleResponseOk(Response.status(Status.OK).type("application/pdf").entity(b).header("content-disposition", "attachment; filename=\""+rtPdfEntryName+"\""),transactionId).build();
				} else if(accept.toLowerCase().contains("application/json")) {
					this.logResponse(uriInfo, httpHeaders, methodName, ricevutaDTOResponse.getRpt().getXmlRt(), 200);
					this.log.info("Esecuzione " + methodName + " completata."); 
					return this.handleResponseOk(Response.status(Status.OK).type(MediaType.APPLICATION_JSON).entity(rt),transactionId).build();
				} else {
					this.logResponse(uriInfo, httpHeaders, methodName, ricevutaDTOResponse.getRpt().getXmlRt(), 200);
					this.log.info("Esecuzione " + methodName + " completata."); 
					return this.handleResponseOk(Response.status(Status.OK).type(MediaType.TEXT_XML).entity(rt),transactionId).build();
				}
			}
		}catch (Exception e) {
			return handleException(uriInfo, httpHeaders, methodName, e, transactionId);
		} finally {
			if(ctx != null) ctx.log();
		}    }



	public Response rppIdDominioIuvCcpRptGET(IAutorizzato user, UriInfo uriInfo, HttpHeaders httpHeaders , String idDominio, String iuv, String ccp) {
		String methodName = "rppIdDominioIuvCcpRtGET";  
		GpContext ctx = null;
		String transactionId = null;
		ByteArrayOutputStream baos= null;
		this.log.info("Esecuzione " + methodName + " in corso..."); 


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

			LeggiRicevutaDTOResponse ricevutaDTOResponse = ricevuteDAO.leggiRt(leggiPagamentoPortaleDTO);

			CtRichiestaPagamentoTelematico rpt = JaxbUtils.toRPT(ricevutaDTOResponse.getRpt().getXmlRpt());
			return this.handleResponseOk(Response.status(Status.OK).entity(rpt),transactionId).build();
		}catch (Exception e) {
			return handleException(uriInfo, httpHeaders, methodName, e, transactionId);
		} finally {
			if(ctx != null) ctx.log();
		} 
	}



	public Response rppIdDominioIuvCcpGET(IAutorizzato user, UriInfo uriInfo, HttpHeaders httpHeaders , String idDominio, String iuv, String ccp) {
		String methodName = "rppIdDominioIuvCcpGET";  
		GpContext ctx = null;
		String transactionId = null;		
		ByteArrayOutputStream baos= null;
		this.log.info("Esecuzione " + methodName + " in corso..."); 

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


			Rpp response =  RptConverter.toRsModel(leggiRptDTOResponse.getRpt(),leggiRptDTOResponse.getVersamento(),leggiRptDTOResponse.getApplicazione());
			
			Pendenza pendenza = PendenzeConverter.toRsModel(leggiRptDTOResponse.getVersamento(), leggiRptDTOResponse.getUnitaOperativa(), leggiRptDTOResponse.getApplicazione(),
					leggiRptDTOResponse.getDominio(), leggiRptDTOResponse.getLstSingoliVersamenti(),true);
			
			response.setPendenza(pendenza);
			
			return this.handleResponseOk(Response.status(Status.OK).entity(response.toJSON(null)),transactionId).build();
		}catch (Exception e) {
			return handleException(uriInfo, httpHeaders, methodName, e, transactionId);
		} finally {
			if(ctx != null) ctx.log();
		}
	}


}


