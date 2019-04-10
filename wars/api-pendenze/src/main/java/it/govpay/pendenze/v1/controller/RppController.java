package it.govpay.pendenze.v1.controller;

import java.io.ByteArrayOutputStream;
import java.net.URLDecoder;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;
import javax.ws.rs.core.UriInfo;

import org.slf4j.Logger;
import org.springframework.security.core.Authentication;

import it.gov.digitpa.schemas._2011.pagamenti.CtRicevutaTelematica;
import it.gov.digitpa.schemas._2011.pagamenti.CtRichiestaPagamentoTelematico;
import it.govpay.core.autorizzazione.AuthorizationManager;
import it.govpay.core.dao.pagamenti.RptDAO;
import it.govpay.core.dao.pagamenti.dto.LeggiRicevutaDTO;
import it.govpay.core.dao.pagamenti.dto.LeggiRicevutaDTO.FormatoRicevuta;
import it.govpay.core.dao.pagamenti.dto.LeggiRicevutaDTOResponse;
import it.govpay.core.dao.pagamenti.dto.LeggiRptDTO;
import it.govpay.core.dao.pagamenti.dto.LeggiRptDTOResponse;
import it.govpay.core.dao.pagamenti.dto.ListaRptDTO;
import it.govpay.core.dao.pagamenti.dto.ListaRptDTOResponse;
import it.govpay.core.utils.GovpayConfig;
import org.openspcoop2.utils.service.context.IContext;
import it.govpay.core.utils.GpThreadLocal;
import it.govpay.core.utils.JaxbUtils;
import it.govpay.core.utils.validator.ValidatoreIdentificativi;
import it.govpay.model.Acl.Diritti;
import it.govpay.model.Acl.Servizio;
import it.govpay.model.Rpt.StatoRpt;
import it.govpay.model.Utenza.TIPO_UTENZA;
import it.govpay.pendenze.v1.beans.ListaRpp;
import it.govpay.pendenze.v1.beans.PendenzaIndex;
import it.govpay.pendenze.v1.beans.Rpp;
import it.govpay.pendenze.v1.beans.RppIndex;
import it.govpay.pendenze.v1.beans.converter.PendenzeConverter;
import it.govpay.pendenze.v1.beans.converter.RptConverter;

public class RppController extends BaseController {

	public RppController(String nomeServizio,Logger log) {
		super(nomeServizio,log, GovpayConfig.GOVPAY_BACKOFFICE_OPEN_API_FILE_NAME);
	}

	public Response rppGET(Authentication user, UriInfo uriInfo, HttpHeaders httpHeaders , Integer pagina, Integer risultatiPerPagina, String ordinamento, String campi, String idDominio, String iuv, String ccp, String idA2A, String idPendenza, String esito, String idPagamento) {
		String methodName = "rppGET";  
		IContext ctx = null;
		String transactionId = null;
		ByteArrayOutputStream baos= null;
		this.log.info(MessageFormat.format(BaseController.LOG_MSG_ESECUZIONE_METODO_IN_CORSO, methodName)); 
		try{
			baos = new ByteArrayOutputStream();
			this.logRequest(uriInfo, httpHeaders, methodName, baos);

			ctx =  GpThreadLocal.get();
			transactionId = ctx.getTransactionId();

			// autorizzazione sulla API
			this.isAuthorized(user, Arrays.asList(TIPO_UTENZA.APPLICAZIONE), Arrays.asList(Servizio.API_PENDENZE), Arrays.asList(Diritti.LETTURA));
						
			// Parametri - > DTO Input

			ListaRptDTO listaRptDTO = new ListaRptDTO(user);
			listaRptDTO.setLimit(risultatiPerPagina);
			listaRptDTO.setPagina(pagina);
			
			ValidatoreIdentificativi validatoreId = ValidatoreIdentificativi.newInstance();

			if(esito != null)
				listaRptDTO.setStato(StatoRpt.valueOf(esito));

			if(idDominio != null) {
				validatoreId.validaIdDominio("idDominio", idDominio);
				listaRptDTO.setIdDominio(idDominio);
			}
			if(iuv != null)
				listaRptDTO.setIuv(iuv);
			if(ccp != null)
				listaRptDTO.setCcp(ccp);
			
			if(idA2A != null) {
				validatoreId.validaIdApplicazione("idA2A", idA2A);
				listaRptDTO.setIdA2A(idA2A);
			}
			
			if(idPendenza != null) {
				validatoreId.validaIdPendenza("idPendenza", idPendenza);
				listaRptDTO.setIdPendenza(idPendenza);
			}

			if(idPagamento != null)
				listaRptDTO.setIdPagamento(idPagamento);

			if(ordinamento != null)
				listaRptDTO.setOrderBy(ordinamento);
			
			// Autorizzazione sui domini
			List<String> domini = AuthorizationManager.getDominiAutorizzati(user);
			if(domini == null) {
				throw AuthorizationManager.toNotAuthorizedExceptionNessunDominioAutorizzato(user);
			}
			listaRptDTO.setCodDomini(domini);

			RptDAO rptDAO = new RptDAO();

			// CHIAMATA AL DAO

			ListaRptDTOResponse listaRptDTOResponse = rptDAO.listaRpt(listaRptDTO);

			// CONVERT TO JSON DELLA RISPOSTA
			List<RppIndex> results = new ArrayList<>();
			for(LeggiRptDTOResponse leggiRptDtoResponse: listaRptDTOResponse.getResults()) {
				results.add(RptConverter.toRsModelIndex(leggiRptDtoResponse.getRpt(),leggiRptDtoResponse.getVersamento(),leggiRptDtoResponse.getApplicazione()));
			}
			ListaRpp response = new ListaRpp(results, this.getServicePath(uriInfo), listaRptDTOResponse.getTotalResults(), pagina, risultatiPerPagina);

			this.logResponse(uriInfo, httpHeaders, methodName, response.toJSON(campi), 200);
			this.log.info(MessageFormat.format(BaseController.LOG_MSG_ESECUZIONE_METODO_COMPLETATA, methodName)); 
			return this.handleResponseOk(Response.status(Status.OK).entity(response.toJSON(campi)),transactionId).build();
		}catch (Exception e) {
			return this.handleException(uriInfo, httpHeaders, methodName, e, transactionId);
		} finally {
			this.log(ctx);
		}
	}



	public Response rppIdDominioIuvCcpRtGET(Authentication user, UriInfo uriInfo, HttpHeaders httpHeaders , String idDominio, String iuv, String ccp, Boolean visualizzaSoggettoDebitore) {
		String methodName = "rppIdDominioIuvCcpRtGET";  
		IContext ctx = null;
		String transactionId = null;
		ByteArrayOutputStream baos= null;
		this.log.info(MessageFormat.format(BaseController.LOG_MSG_ESECUZIONE_METODO_IN_CORSO, methodName)); 


		String accept = "";
		if(httpHeaders.getRequestHeaders().containsKey("Accept")) {
			accept = httpHeaders.getRequestHeaders().get("Accept").get(0).toLowerCase();
		}

		try{
			baos = new ByteArrayOutputStream();
			this.logRequest(uriInfo, httpHeaders, methodName, baos);

			ctx =  GpThreadLocal.get();
			transactionId = ctx.getTransactionId();
			
			// autorizzazione sulla API
			this.isAuthorized(user, Arrays.asList(TIPO_UTENZA.APPLICAZIONE), Arrays.asList(Servizio.API_PENDENZE), Arrays.asList(Diritti.LETTURA));
						
			ValidatoreIdentificativi validatoreId = ValidatoreIdentificativi.newInstance();
			validatoreId.validaIdDominio("idDominio", idDominio);

			LeggiRicevutaDTO leggiPagamentoPortaleDTO = new LeggiRicevutaDTO(user);
			leggiPagamentoPortaleDTO.setIdDominio(idDominio);
			leggiPagamentoPortaleDTO.setIuv(iuv);
			ccp = ccp.contains("%") ? URLDecoder.decode(ccp,"UTF-8") : ccp;
			leggiPagamentoPortaleDTO.setCcp(ccp);
			
			if(visualizzaSoggettoDebitore != null)
				leggiPagamentoPortaleDTO.setVisualizzaSoggettoDebitore(visualizzaSoggettoDebitore.booleanValue()); 
			
			// controllo che il dominio sia autorizzato
			if(!AuthorizationManager.isDominioAuthorized(leggiPagamentoPortaleDTO.getUser(), idDominio)) {
				throw AuthorizationManager.toNotAuthorizedException(leggiPagamentoPortaleDTO.getUser(),idDominio, null);
			}

			RptDAO ricevuteDAO = new RptDAO(); 

			LeggiRicevutaDTOResponse ricevutaDTOResponse = null; 

			if(accept.toLowerCase().contains(MediaType.APPLICATION_OCTET_STREAM)) {
				leggiPagamentoPortaleDTO.setFormato(FormatoRicevuta.RAW);
				ricevutaDTOResponse = ricevuteDAO.leggiRt(leggiPagamentoPortaleDTO);
				
				// controllo che il dominio sia autorizzato
				if(!AuthorizationManager.isDominioAuthorized(user, ricevutaDTOResponse.getDominio().getCodDominio())) {
					throw AuthorizationManager.toNotAuthorizedException(user, ricevutaDTOResponse.getDominio().getCodDominio(), null);
				}
				
				this.logResponse(uriInfo, httpHeaders, methodName, ricevutaDTOResponse.getRpt().getXmlRt(), 200);
				this.log.info(MessageFormat.format(BaseController.LOG_MSG_ESECUZIONE_METODO_COMPLETATA, methodName)); 
				return this.handleResponseOk(Response.status(Status.OK).type(MediaType.APPLICATION_OCTET_STREAM).entity(new String(ricevutaDTOResponse.getRpt().getXmlRt())),transactionId).build();
			} else {
				if(accept.toLowerCase().contains("application/pdf")) {
					leggiPagamentoPortaleDTO.setFormato(FormatoRicevuta.PDF);
					ricevutaDTOResponse = ricevuteDAO.leggiRt(leggiPagamentoPortaleDTO);
					String rtPdfEntryName = idDominio +"_"+ iuv + "_"+ ccp + ".pdf";
					byte[] b = ricevutaDTOResponse.getPdf(); 
					
					// controllo che il dominio sia autorizzato
					if(!AuthorizationManager.isDominioAuthorized(user, ricevutaDTOResponse.getDominio().getCodDominio())) {
						throw AuthorizationManager.toNotAuthorizedException(user, ricevutaDTOResponse.getDominio().getCodDominio(), null);
					}

					this.logResponse(uriInfo, httpHeaders, methodName, b, 200);
					this.log.info(MessageFormat.format(BaseController.LOG_MSG_ESECUZIONE_METODO_COMPLETATA, methodName)); 
					return this.handleResponseOk(Response.status(Status.OK).type("application/pdf").entity(b).header("content-disposition", "attachment; filename=\""+rtPdfEntryName+"\""),transactionId).build();
				} else if(accept.toLowerCase().contains(MediaType.APPLICATION_JSON)) {
					leggiPagamentoPortaleDTO.setFormato(FormatoRicevuta.JSON);
					ricevutaDTOResponse = ricevuteDAO.leggiRt(leggiPagamentoPortaleDTO);
					
					// controllo che il dominio sia autorizzato
					if(!AuthorizationManager.isDominioAuthorized(user, ricevutaDTOResponse.getDominio().getCodDominio())) {
						throw AuthorizationManager.toNotAuthorizedException(user, ricevutaDTOResponse.getDominio().getCodDominio(), null);
					}
					
					CtRicevutaTelematica rt = JaxbUtils.toRT(ricevutaDTOResponse.getRpt().getXmlRt(), false);
					this.logResponse(uriInfo, httpHeaders, methodName, ricevutaDTOResponse.getRpt().getXmlRt(), 200);
					this.log.info(MessageFormat.format(BaseController.LOG_MSG_ESECUZIONE_METODO_COMPLETATA, methodName)); 
					return this.handleResponseOk(Response.status(Status.OK).type(MediaType.APPLICATION_JSON).entity(rt),transactionId).build();
				} else {
					leggiPagamentoPortaleDTO.setFormato(FormatoRicevuta.XML);
					ricevutaDTOResponse = ricevuteDAO.leggiRt(leggiPagamentoPortaleDTO);
					
					// controllo che il dominio sia autorizzato
					if(!AuthorizationManager.isDominioAuthorized(user, ricevutaDTOResponse.getDominio().getCodDominio())) {
						throw AuthorizationManager.toNotAuthorizedException(user, ricevutaDTOResponse.getDominio().getCodDominio(), null);
					}
					
					this.logResponse(uriInfo, httpHeaders, methodName, ricevutaDTOResponse.getRpt().getXmlRt(), 200);
					this.log.info(MessageFormat.format(BaseController.LOG_MSG_ESECUZIONE_METODO_COMPLETATA, methodName)); 
					return this.handleResponseOk(Response.status(Status.OK).type(MediaType.TEXT_XML).entity(ricevutaDTOResponse.getRpt().getXmlRt()),transactionId).build();
				}
			}
		}catch (Exception e) {
			return this.handleException(uriInfo, httpHeaders, methodName, e, transactionId);
		} finally {
			this.log(ctx);
		}    
	}



	public Response rppIdDominioIuvCcpRptGET(Authentication user, UriInfo uriInfo, HttpHeaders httpHeaders , String idDominio, String iuv, String ccp) {
		String methodName = "rppIdDominioIuvCcpRtGET";  
		IContext ctx = null;
		String transactionId = null;
		ByteArrayOutputStream baos= null;
		this.log.info(MessageFormat.format(BaseController.LOG_MSG_ESECUZIONE_METODO_IN_CORSO, methodName)); 


		try{
			baos = new ByteArrayOutputStream();
			this.logRequest(uriInfo, httpHeaders, methodName, baos);
			
			String accept = "";
			if(httpHeaders.getRequestHeaders().containsKey("Accept")) {
				accept = httpHeaders.getRequestHeaders().get("Accept").get(0).toLowerCase();
			}

			ctx =  GpThreadLocal.get();
			transactionId = ctx.getTransactionId();

			// autorizzazione sulla API
			this.isAuthorized(user, Arrays.asList(TIPO_UTENZA.APPLICAZIONE), Arrays.asList(Servizio.API_PENDENZE), Arrays.asList(Diritti.LETTURA));
			
			ValidatoreIdentificativi validatoreId = ValidatoreIdentificativi.newInstance();
			validatoreId.validaIdDominio("idDominio", idDominio);

			LeggiRptDTO leggiRptDTO = new LeggiRptDTO(user);
			leggiRptDTO.setIdDominio(idDominio);
			leggiRptDTO.setIuv(iuv);
			ccp = ccp.contains("%") ? URLDecoder.decode(ccp,"UTF-8") : ccp;
			leggiRptDTO.setCcp(ccp);
			
			// controllo che il dominio sia autorizzato
			if(!AuthorizationManager.isDominioAuthorized(leggiRptDTO.getUser(), idDominio)) {
				throw AuthorizationManager.toNotAuthorizedException(leggiRptDTO.getUser(),idDominio, null);
			}

			RptDAO ricevuteDAO = new RptDAO(); 

			LeggiRptDTOResponse leggiRptDTOResponse = ricevuteDAO.leggiRpt(leggiRptDTO);
			
			// controllo che il dominio sia autorizzato
			if(!AuthorizationManager.isDominioAuthorized(user, leggiRptDTOResponse.getDominio().getCodDominio())) {
				throw AuthorizationManager.toNotAuthorizedException(user, leggiRptDTOResponse.getDominio().getCodDominio(), null);
			}

			if(accept.toLowerCase().contains(MediaType.APPLICATION_JSON)) {
				CtRichiestaPagamentoTelematico rpt = JaxbUtils.toRPT(leggiRptDTOResponse.getRpt().getXmlRpt(), false);
				return this.handleResponseOk(Response.status(Status.OK).type(MediaType.APPLICATION_JSON).entity(rpt),transactionId).build();
			}else {
				return this.handleResponseOk(Response.status(Status.OK).type(MediaType.TEXT_XML).entity(leggiRptDTOResponse.getRpt().getXmlRpt()),transactionId).build();
			}
		}catch (Exception e) {
			return this.handleException(uriInfo, httpHeaders, methodName, e, transactionId);
		} finally {
			this.log(ctx);
		} 
	}



	public Response rppIdDominioIuvCcpGET(Authentication user, UriInfo uriInfo, HttpHeaders httpHeaders , String idDominio, String iuv, String ccp) {
		String methodName = "rppIdDominioIuvCcpGET";  
		IContext ctx = null;
		String transactionId = null;		
		ByteArrayOutputStream baos= null;
		this.log.info(MessageFormat.format(BaseController.LOG_MSG_ESECUZIONE_METODO_IN_CORSO, methodName)); 

		try{
			baos = new ByteArrayOutputStream();
			this.logRequest(uriInfo, httpHeaders, methodName, baos);

			ctx =  GpThreadLocal.get();
			transactionId = ctx.getTransactionId();
			
			// autorizzazione sulla API
			this.isAuthorized(user, Arrays.asList(TIPO_UTENZA.APPLICAZIONE), Arrays.asList(Servizio.API_PENDENZE), Arrays.asList(Diritti.LETTURA));
			
			ValidatoreIdentificativi validatoreId = ValidatoreIdentificativi.newInstance();
			validatoreId.validaIdDominio("idDominio", idDominio);

			LeggiRptDTO leggiRptDTO = new LeggiRptDTO(user);
			leggiRptDTO.setIdDominio(idDominio);
			leggiRptDTO.setIuv(iuv);
			ccp = ccp.contains("%") ? URLDecoder.decode(ccp,"UTF-8") : ccp;
			leggiRptDTO.setCcp(ccp);
			
			// controllo che il dominio sia autorizzato
			if(!AuthorizationManager.isDominioAuthorized(leggiRptDTO.getUser(), idDominio)) {
				throw AuthorizationManager.toNotAuthorizedException(leggiRptDTO.getUser(),idDominio, null);
			}

			RptDAO ricevuteDAO = new RptDAO(); 

			LeggiRptDTOResponse leggiRptDTOResponse = ricevuteDAO.leggiRpt(leggiRptDTO);

			// controllo che il dominio sia autorizzato
			if(!AuthorizationManager.isDominioAuthorized(user, leggiRptDTOResponse.getDominio().getCodDominio())) {
				throw AuthorizationManager.toNotAuthorizedException(user, leggiRptDTOResponse.getDominio().getCodDominio(), null);
			}

			Rpp response =  RptConverter.toRsModel(leggiRptDTOResponse.getRpt(),leggiRptDTOResponse.getVersamento(),leggiRptDTOResponse.getApplicazione());
			
			PendenzaIndex pendenza = PendenzeConverter.toRsIndexModel(leggiRptDTOResponse.getVersamento());
			
			response.setPendenza(pendenza);
			
			return this.handleResponseOk(Response.status(Status.OK).entity(response.toJSON(null)),transactionId).build();
		}catch (Exception e) {
			return this.handleException(uriInfo, httpHeaders, methodName, e, transactionId);
		} finally {
			this.log(ctx);
		}
	}


}


