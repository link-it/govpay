package it.govpay.backoffice.v1.controllers;

import java.io.ByteArrayOutputStream;
import java.net.URLDecoder;
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

import org.apache.commons.io.IOUtils;
import org.apache.commons.lang.ArrayUtils;
import org.apache.commons.lang.StringUtils;
import org.openspcoop2.generic_project.exception.ServiceException;
import org.openspcoop2.utils.json.ValidationException;
import org.openspcoop2.utils.service.context.ContextThreadLocal;
import org.slf4j.Logger;
import org.springframework.security.core.Authentication;

import it.gov.digitpa.schemas._2011.pagamenti.CtRicevutaTelematica;
import it.gov.digitpa.schemas._2011.pagamenti.CtRichiestaPagamentoTelematico;
import it.govpay.backoffice.v1.beans.EsitoRpt;
import it.govpay.backoffice.v1.beans.ListaRpp;
import it.govpay.backoffice.v1.beans.PatchOp;
import it.govpay.backoffice.v1.beans.PatchOp.OpEnum;
import it.govpay.backoffice.v1.beans.PendenzaIndex;
import it.govpay.backoffice.v1.beans.Rpp;
import it.govpay.backoffice.v1.beans.RppIndex;
import it.govpay.backoffice.v1.beans.converter.PatchOpConverter;
import it.govpay.backoffice.v1.beans.converter.PendenzeConverter;
import it.govpay.backoffice.v1.beans.converter.RptConverter;
import it.govpay.bd.model.IdUnitaOperativa;
import it.govpay.core.autorizzazione.AuthorizationManager;
import it.govpay.core.beans.JSONSerializable;
import it.govpay.core.dao.pagamenti.RptDAO;
import it.govpay.core.dao.pagamenti.dto.LeggiRicevutaDTO;
import it.govpay.core.dao.pagamenti.dto.LeggiRicevutaDTO.FormatoRicevuta;
import it.govpay.core.dao.pagamenti.dto.LeggiRicevutaDTOResponse;
import it.govpay.core.dao.pagamenti.dto.LeggiRptDTO;
import it.govpay.core.dao.pagamenti.dto.LeggiRptDTOResponse;
import it.govpay.core.dao.pagamenti.dto.ListaRptDTO;
import it.govpay.core.dao.pagamenti.dto.ListaRptDTOResponse;
import it.govpay.core.dao.pagamenti.dto.PatchRptDTO;
import it.govpay.core.dao.pagamenti.dto.PatchRptDTOResponse;
import it.govpay.core.utils.JaxbUtils;
import it.govpay.core.utils.SimpleDateFormatUtils;
import it.govpay.core.utils.validator.ValidatoreIdentificativi;
import it.govpay.model.Acl.Diritti;
import it.govpay.model.Acl.Servizio;
import it.govpay.model.Rpt.EsitoPagamento;
import it.govpay.model.Utenza.TIPO_UTENZA;

public class RppController extends BaseController {

	public RppController(String nomeServizio,Logger log) {
		super(nomeServizio,log);
	}

	public Response findRpps(Authentication user, UriInfo uriInfo, HttpHeaders httpHeaders , Integer pagina, Integer risultatiPerPagina, String ordinamento, String campi, String idDominio, String iuv, String ccp, String idA2A, String idPendenza, String esito, String idPagamento, String idDebitore, String dataRptDa, String dataRptA, String dataRtDa, String dataRtA, List<String> direzione, List<String> divisione, String tassonomia, String idUnita, String idTipoPendenza, String anagraficaDebitore) {
		String methodName = "findRpps";  
		String transactionId = ContextThreadLocal.get().getTransactionId();
		this.log.debug(MessageFormat.format(BaseController.LOG_MSG_ESECUZIONE_METODO_IN_CORSO, methodName)); 
		try{
			// autorizzazione sulla API
			this.isAuthorized(user, Arrays.asList(TIPO_UTENZA.OPERATORE, TIPO_UTENZA.APPLICAZIONE), Arrays.asList(Servizio.PENDENZE), Arrays.asList(Diritti.LETTURA));

			// Parametri - > DTO Input 

			ListaRptDTO listaRptDTO = new ListaRptDTO(user);
			listaRptDTO.setLimit(risultatiPerPagina);
			listaRptDTO.setPagina(pagina);

			if(esito != null) {
				EsitoRpt esitoRPT = EsitoRpt.fromValue(esito);
				
				if (esitoRPT != null) {
					EsitoPagamento esitoPagamento = null;;
					switch(esitoRPT) {
					case DECORENNZA_PARZIALE:
						esitoPagamento = EsitoPagamento.DECORRENZA_TERMINI_PARZIALE;
						break;
					case DECORRENZA:
						esitoPagamento = EsitoPagamento.DECORRENZA_TERMINI;
						break;
					case ESEGUITO:
						esitoPagamento = EsitoPagamento.PAGAMENTO_ESEGUITO;
						break;
					case ESEGUITO_PARZIALE:
						esitoPagamento = EsitoPagamento.PAGAMENTO_PARZIALMENTE_ESEGUITO;
						break;
					case IN_CORSO:
						esitoPagamento = EsitoPagamento.IN_CORSO;
						break;
					case NON_ESEGUITO:
						esitoPagamento = EsitoPagamento.PAGAMENTO_NON_ESEGUITO;
						break;
					case RIFIUTATO:
						esitoPagamento = EsitoPagamento.RIFIUTATO;
						break;
					}
					listaRptDTO.setEsitoPagamento(esitoPagamento);
				} else {
					throw new ValidationException("Codifica inesistente per esito. Valore fornito [" + esito
							+ "] valori possibili " + ArrayUtils.toString(EsitoRpt.values()));
				}
			}
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
			
			if(idDebitore != null)
				listaRptDTO.setIdDebitore(idDebitore);
			
			listaRptDTO.setIdUnita(idUnita);
			listaRptDTO.setIdTipoPendenza(idTipoPendenza);
			listaRptDTO.setDirezione(direzione);
			listaRptDTO.setDivisione(divisione);
			listaRptDTO.setTassonomia(tassonomia);
			listaRptDTO.setAnagraficaDebitore(anagraficaDebitore);
			
			// dat RPT
			if(dataRptDa!=null) {
				Date dataDaDate = SimpleDateFormatUtils.getDataDaConTimestamp(dataRptDa);
				listaRptDTO.setDataDa(dataDaDate);
			}
			
			if(dataRptA!=null) {
				Date dataADate = SimpleDateFormatUtils.getDataAConTimestamp(dataRptA);
				listaRptDTO.setDataA(dataADate);
			}
			
			// data RT
			if(dataRtDa!=null) {
				Date dataDaDate = SimpleDateFormatUtils.getDataDaConTimestamp(dataRtDa);
				listaRptDTO.setDataRtDa(dataDaDate);
			}
				
			
			if(dataRtA!=null) {
				Date dataADate = SimpleDateFormatUtils.getDataAConTimestamp(dataRtA);
				listaRptDTO.setDataRtA(dataADate);
			}
			
			// INIT DAO

			RptDAO rptDAO = new RptDAO();

			// Autorizzazione sui domini
//			List<String> domini = AuthorizationManager.getDominiAutorizzati(user);
//			if(domini == null) {
//				throw AuthorizationManager.toNotAuthorizedExceptionNessunDominioAutorizzato(user);
//			}
//			listaRptDTO.setCodDomini(domini);
			
			// Autorizzazione sulle uo
			List<IdUnitaOperativa> uo = AuthorizationManager.getUoAutorizzate(user);
			if(uo == null) {
				throw AuthorizationManager.toNotAuthorizedExceptionNessunaUOAutorizzata(user);
			}
			listaRptDTO.setUnitaOperative(uo);

			ListaRptDTOResponse listaRptDTOResponse = rptDAO.listaRpt(listaRptDTO);

			// CONVERT TO JSON DELLA RISPOSTA
			List<RppIndex> results = new ArrayList<>();
			for(LeggiRptDTOResponse leggiRptDtoResponse: listaRptDTOResponse.getResults()) {
				results.add(RptConverter.toRsModelIndex(leggiRptDtoResponse.getRpt()));
			}
			ListaRpp response = new ListaRpp(results, this.getServicePath(uriInfo), listaRptDTOResponse.getTotalResults(), pagina, risultatiPerPagina);

			this.log.debug(MessageFormat.format(BaseController.LOG_MSG_ESECUZIONE_METODO_COMPLETATA, methodName)); 
			return this.handleResponseOk(Response.status(Status.OK).entity(response.toJSON(campi)),transactionId).build();
		}catch (Exception e) {
			return this.handleException(uriInfo, httpHeaders, methodName, e, transactionId);
		} finally {
			this.log(ContextThreadLocal.get());
		}
	}

	public Response getRpp(Authentication user, UriInfo uriInfo, HttpHeaders httpHeaders , String idDominio, String iuv, String ccp) {
		String methodName = "getRpp";  
		String transactionId = ContextThreadLocal.get().getTransactionId();		
		this.log.debug(MessageFormat.format(BaseController.LOG_MSG_ESECUZIONE_METODO_IN_CORSO, methodName)); 

		try{
			// autorizzazione sulla API
			this.isAuthorized(user, Arrays.asList(TIPO_UTENZA.OPERATORE, TIPO_UTENZA.APPLICAZIONE), Arrays.asList(Servizio.PENDENZE), Arrays.asList(Diritti.LETTURA));
			
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

			Rpp response =  RptConverter.toRsModel(leggiRptDTOResponse.getRpt());
			
			// controllo che il dominio sia autorizzato
			if(!AuthorizationManager.isDominioAuthorized(user, leggiRptDTOResponse.getDominio().getCodDominio())) {
				throw AuthorizationManager.toNotAuthorizedException(user, leggiRptDTOResponse.getDominio().getCodDominio(), null);
			}

			PendenzaIndex pendenza = PendenzeConverter.toRsModelIndex(leggiRptDTOResponse.getVersamento());

			response.setPendenza(pendenza);

			return this.handleResponseOk(Response.status(Status.OK).entity(response.toJSON(null)),transactionId).build();
		}catch (Exception e) {
			return this.handleException(uriInfo, httpHeaders, methodName, e, transactionId);
		} finally {
			this.log(ContextThreadLocal.get());
		}
	}

	@SuppressWarnings("unchecked")
	public Response updateRpp(Authentication user, UriInfo uriInfo, HttpHeaders httpHeaders , java.io.InputStream is, String idDominio, String iuv, String ccp) {
		String methodName = "updateRpp";  
		String transactionId = ContextThreadLocal.get().getTransactionId();
		this.log.debug(MessageFormat.format(BaseController.LOG_MSG_ESECUZIONE_METODO_IN_CORSO, methodName)); 
		
		try(ByteArrayOutputStream baos= new ByteArrayOutputStream();){
			// salvo il json ricevuto
			IOUtils.copy(is, baos);
			
			// autorizzazione sulla API
			this.isAuthorized(user, Arrays.asList(TIPO_UTENZA.OPERATORE, TIPO_UTENZA.APPLICAZIONE), Arrays.asList(Servizio.PENDENZE), Arrays.asList(Diritti.SCRITTURA));
			
			ValidatoreIdentificativi validatoreId = ValidatoreIdentificativi.newInstance();
			validatoreId.validaIdDominio("idDominio", idDominio);
			
			RptDAO rptDAO = new RptDAO();
			
			PatchRptDTO patchRptDTO = new PatchRptDTO(user);
			patchRptDTO.setIdDominio(idDominio);
			patchRptDTO.setIuv(iuv);
			ccp = ccp.contains("%") ? URLDecoder.decode(ccp,"UTF-8") : ccp;
			patchRptDTO.setCcp(ccp);
			
			String jsonRequest = baos.toString();

			List<PatchOp> lstOp = new ArrayList<>();

			try {
				List<java.util.LinkedHashMap<?,?>> lst = JSONSerializable.parse(jsonRequest, List.class);
				for(java.util.LinkedHashMap<?,?> map: lst) {
					PatchOp op = new PatchOp();
					String opText = (String) map.get("op");
					OpEnum opFromValue = OpEnum.fromValue(opText);

					if(StringUtils.isNotEmpty(opText) && opFromValue == null)
						throw new ValidationException("Il campo op non e' valido.");

					op.setOp(opFromValue);
					op.setPath((String) map.get("path"));
					op.setValue(map.get("value"));
					op.validate();
					lstOp.add(op);
				}
			} catch (ServiceException e) {
				lstOp = JSONSerializable.parse(jsonRequest, List.class);
				//				PatchOp op = PatchOp.parse(jsonRequest);
				//				op.validate();
				//				lstOp.add(op);
			}

			patchRptDTO.setOp(PatchOpConverter.toModel(lstOp));
			
			
			PatchRptDTOResponse patchRptDTOResponse = rptDAO.patch(patchRptDTO);
		
			Rpp response =  RptConverter.toRsModel(patchRptDTOResponse.getRpt());

			PendenzaIndex pendenza = PendenzeConverter.toRsModelIndex(patchRptDTOResponse.getVersamento());

			response.setPendenza(pendenza);

			this.log.debug(MessageFormat.format(BaseController.LOG_MSG_ESECUZIONE_METODO_COMPLETATA, methodName)); 
			return this.handleResponseOk(Response.status(Status.OK).entity(response.toJSON(null)),transactionId).build();
		}catch (Exception e) {
			return this.handleException(uriInfo, httpHeaders, methodName, e, transactionId);
		} finally {
			this.log(ContextThreadLocal.get());
		}
	}

	public Response getRpt(Authentication user, UriInfo uriInfo, HttpHeaders httpHeaders , String idDominio, String iuv, String ccp) {
		String methodName = "getRpt";  
		String transactionId = ContextThreadLocal.get().getTransactionId();
		this.log.debug(MessageFormat.format(BaseController.LOG_MSG_ESECUZIONE_METODO_IN_CORSO, methodName)); 

		try{
			String accept = MediaType.APPLICATION_JSON;
			if(httpHeaders.getRequestHeaders().containsKey("Accept")) {
				accept = httpHeaders.getRequestHeaders().get("Accept").get(0).toLowerCase();
			}
			
			// autorizzazione sulla API
			this.isAuthorized(user, Arrays.asList(TIPO_UTENZA.OPERATORE, TIPO_UTENZA.APPLICAZIONE), Arrays.asList(Servizio.PENDENZE), Arrays.asList(Diritti.LETTURA));
			
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
			this.log(ContextThreadLocal.get());
		} 
	}

	public Response getRt(Authentication user, UriInfo uriInfo, HttpHeaders httpHeaders , String idDominio, String iuv, String ccp, Boolean visualizzaSoggettoDebitore) {
		String methodName = "getRt";  
		String transactionId = ContextThreadLocal.get().getTransactionId();
		this.log.debug(MessageFormat.format(BaseController.LOG_MSG_ESECUZIONE_METODO_IN_CORSO, methodName)); 

		String accept = MediaType.APPLICATION_JSON;
		if(httpHeaders.getRequestHeaders().containsKey("Accept")) {
			accept = httpHeaders.getRequestHeaders().get("Accept").get(0).toLowerCase();
		}

		try{
			// autorizzazione sulla API
			this.isAuthorized(user, Arrays.asList(TIPO_UTENZA.OPERATORE, TIPO_UTENZA.APPLICAZIONE), Arrays.asList(Servizio.PENDENZE), Arrays.asList(Diritti.LETTURA));
			
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
			if(!AuthorizationManager.isDominioAuthorized(user, idDominio)) {
				throw AuthorizationManager.toNotAuthorizedException(user,idDominio, null);
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

				this.log.debug(MessageFormat.format(BaseController.LOG_MSG_ESECUZIONE_METODO_COMPLETATA, methodName)); 
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

					this.log.debug(MessageFormat.format(BaseController.LOG_MSG_ESECUZIONE_METODO_COMPLETATA, methodName)); 
					return this.handleResponseOk(Response.status(Status.OK).type("application/pdf").entity(b).header("content-disposition", "attachment; filename=\""+rtPdfEntryName+"\""),transactionId).build();
				} else if(accept.toLowerCase().contains(MediaType.APPLICATION_JSON)) {
					leggiPagamentoPortaleDTO.setFormato(FormatoRicevuta.JSON);
					ricevutaDTOResponse = ricevuteDAO.leggiRt(leggiPagamentoPortaleDTO);
				
					// controllo che il dominio sia autorizzato
					if(!AuthorizationManager.isDominioAuthorized(user, ricevutaDTOResponse.getDominio().getCodDominio())) {
						throw AuthorizationManager.toNotAuthorizedException(user, ricevutaDTOResponse.getDominio().getCodDominio(), null);
					}
					
					CtRicevutaTelematica rt = JaxbUtils.toRT(ricevutaDTOResponse.getRpt().getXmlRt(), false);
					this.log.debug(MessageFormat.format(BaseController.LOG_MSG_ESECUZIONE_METODO_COMPLETATA, methodName)); 
					return this.handleResponseOk(Response.status(Status.OK).type(MediaType.APPLICATION_JSON).entity(rt),transactionId).build();
				} else {
					leggiPagamentoPortaleDTO.setFormato(FormatoRicevuta.XML);
					ricevutaDTOResponse = ricevuteDAO.leggiRt(leggiPagamentoPortaleDTO);
					
					// controllo che il dominio sia autorizzato
					if(!AuthorizationManager.isDominioAuthorized(user, ricevutaDTOResponse.getDominio().getCodDominio())) {
						throw AuthorizationManager.toNotAuthorizedException(user, ricevutaDTOResponse.getDominio().getCodDominio(), null);
					}
					
					this.log.debug(MessageFormat.format(BaseController.LOG_MSG_ESECUZIONE_METODO_COMPLETATA, methodName)); 
					return this.handleResponseOk(Response.status(Status.OK).type(MediaType.TEXT_XML).entity(ricevutaDTOResponse.getRpt().getXmlRt()),transactionId).build();
				}
			}
		}catch (Exception e) {
			return this.handleException(uriInfo, httpHeaders, methodName, e, transactionId);
		} finally {
			this.log(ContextThreadLocal.get());
		}    
	}
}


