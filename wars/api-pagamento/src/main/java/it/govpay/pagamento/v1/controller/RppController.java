package it.govpay.pagamento.v1.controller;

import java.net.URLDecoder;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;
import javax.ws.rs.core.UriInfo;

import org.apache.commons.lang.ArrayUtils;
import org.openspcoop2.generic_project.exception.NotFoundException;
import org.openspcoop2.generic_project.exception.ServiceException;
import org.openspcoop2.utils.json.ValidationException;
import org.openspcoop2.utils.service.context.ContextThreadLocal;
import org.slf4j.Logger;
import org.springframework.security.core.Authentication;

import it.gov.digitpa.schemas._2011.pagamenti.CtRicevutaTelematica;
import it.gov.digitpa.schemas._2011.pagamenti.CtRichiestaPagamentoTelematico;
import it.govpay.bd.model.PagamentoPortale;
import it.govpay.bd.model.Rpt;
import it.govpay.bd.model.Versamento;
import it.govpay.core.autorizzazione.AuthorizationManager;
import it.govpay.core.autorizzazione.beans.GovpayLdapUserDetails;
import it.govpay.core.autorizzazione.utils.AutorizzazioneUtils;
import it.govpay.core.dao.pagamenti.RptDAO;
import it.govpay.core.dao.pagamenti.dto.LeggiRicevutaDTO;
import it.govpay.core.dao.pagamenti.dto.LeggiRicevutaDTO.FormatoRicevuta;
import it.govpay.core.dao.pagamenti.dto.LeggiRicevutaDTOResponse;
import it.govpay.core.dao.pagamenti.dto.LeggiRptDTO;
import it.govpay.core.dao.pagamenti.dto.LeggiRptDTOResponse;
import it.govpay.core.dao.pagamenti.dto.ListaRptDTO;
import it.govpay.core.dao.pagamenti.dto.ListaRptDTOResponse;
import it.govpay.core.exceptions.NotAuthorizedException;
import it.govpay.core.utils.GovpayConfig;
import it.govpay.core.utils.JaxbUtils;
import it.govpay.core.utils.SimpleDateFormatUtils;
import it.govpay.core.utils.validator.ValidatoreIdentificativi;
import it.govpay.model.Acl.Diritti;
import it.govpay.model.Acl.Servizio;
import it.govpay.model.Rpt.EsitoPagamento;
import it.govpay.model.Utenza.TIPO_UTENZA;
import it.govpay.pagamento.v1.beans.EsitoRpt;
import it.govpay.pagamento.v1.beans.ListaRppIndex;
import it.govpay.pagamento.v1.beans.Rpp;
import it.govpay.pagamento.v1.beans.RppIndex;
import it.govpay.pagamento.v1.beans.converter.RptConverter;

public class RppController extends BaseController {

	public RppController(String nomeServizio,Logger log) {
		super(nomeServizio,log);
	}

	public Response rppGET(Authentication user, UriInfo uriInfo, HttpHeaders httpHeaders , Integer pagina, Integer risultatiPerPagina, String ordinamento, String campi,  String dataDa, String dataA, String idDominio, String iuv, String ccp, String idA2A, String idPendenza, String esito, String idPagamento) {
		String methodName = "rppGET";  
		String transactionId = ContextThreadLocal.get().getTransactionId();
		this.log.debug(MessageFormat.format(BaseController.LOG_MSG_ESECUZIONE_METODO_IN_CORSO, methodName)); 
		try{
			// autorizzazione sulla API
			this.isAuthorized(user, Arrays.asList(TIPO_UTENZA.CITTADINO, TIPO_UTENZA.APPLICAZIONE), Arrays.asList(Servizio.API_PAGAMENTI), Arrays.asList(Diritti.LETTURA));

			// Parametri - > DTO Input

			ListaRptDTO listaRptDTO = new ListaRptDTO(user);
			listaRptDTO.setLimit(risultatiPerPagina);
			listaRptDTO.setPagina(pagina);

			if(esito != null) {
				EsitoRpt esitoRPT = EsitoRpt.fromValue(esito);

				EsitoPagamento esitoPagamento = null;
				if(esitoRPT != null) {
					switch (esitoRPT) {
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
				} else {
					throw new ValidationException("Codifica inesistente per esito. Valore fornito [" + esito
							+ "] valori possibili " + ArrayUtils.toString(EsitoRpt.values()));
				}
				
				listaRptDTO.setEsitoPagamento(esitoPagamento);
			}
			
			ValidatoreIdentificativi validatoreId = ValidatoreIdentificativi.newInstance();

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
			
			if(dataDa!=null) {
				Date dataDaDate = SimpleDateFormatUtils.getDataDaConTimestamp(dataDa, "dataDa");
				listaRptDTO.setDataDa(dataDaDate);
			}
				
			
			if(dataA!=null) {
				Date dataADate = SimpleDateFormatUtils.getDataAConTimestamp(dataA, "dataA");
				listaRptDTO.setDataA(dataADate);
			}
			
			GovpayLdapUserDetails userDetails = AutorizzazioneUtils.getAuthenticationDetails(listaRptDTO.getUser());
			if(userDetails.getTipoUtenza().equals(TIPO_UTENZA.CITTADINO)) {
				listaRptDTO.setCfCittadino(userDetails.getIdentificativo()); 
			}
			
			// se sei una applicazione allora vedi i pagamenti che hai caricato
			if(userDetails.getTipoUtenza().equals(TIPO_UTENZA.APPLICAZIONE)) {
				listaRptDTO.setIdA2APagamentoPortale(userDetails.getApplicazione().getCodApplicazione()); 
			}
			
			RptDAO rptDAO = new RptDAO();

			// CHIAMATA AL DAO

			ListaRptDTOResponse listaRptDTOResponse = rptDAO.listaRpt(listaRptDTO);

			// CONVERT TO JSON DELLA RISPOSTA
			List<RppIndex> results = new ArrayList<>();
			for(LeggiRptDTOResponse leggiRptDtoResponse: listaRptDTOResponse.getResults()) {
				results.add(RptConverter.toRsModelIndex(leggiRptDtoResponse.getRpt(),leggiRptDtoResponse.getVersamento(),leggiRptDtoResponse.getApplicazione(),user));
			}
			ListaRppIndex response = new ListaRppIndex(results, this.getServicePath(uriInfo), listaRptDTOResponse.getTotalResults(), pagina, risultatiPerPagina);

			this.log.debug(MessageFormat.format(BaseController.LOG_MSG_ESECUZIONE_METODO_COMPLETATA, methodName)); 
			return this.handleResponseOk(Response.status(Status.OK).entity(response.toJSON(campi)),transactionId).build();
		}catch (Exception e) {
			return this.handleException(uriInfo, httpHeaders, methodName, e, transactionId);
		} finally {
			this.log(ContextThreadLocal.get());
		}
	}



	public Response rppIdDominioIuvCcpRtGET(Authentication user, UriInfo uriInfo, HttpHeaders httpHeaders , String idDominio, String iuv, String ccp, Boolean visualizzaSoggettoDebitore) {
		String methodName = "rppIdDominioIuvCcpRtGET";  
		String transactionId = ContextThreadLocal.get().getTransactionId();
		this.log.debug(MessageFormat.format(BaseController.LOG_MSG_ESECUZIONE_METODO_IN_CORSO, methodName)); 

		String accept = MediaType.APPLICATION_JSON;
		if(httpHeaders.getRequestHeaders().containsKey("Accept")) {
			accept = httpHeaders.getRequestHeaders().get("Accept").get(0).toLowerCase();
		}

		try{
			// autorizzazione sulla API
			this.isAuthorized(user, Arrays.asList(TIPO_UTENZA.ANONIMO, TIPO_UTENZA.CITTADINO, TIPO_UTENZA.APPLICAZIONE), Arrays.asList(Servizio.API_PAGAMENTI), Arrays.asList(Diritti.LETTURA));

			ValidatoreIdentificativi validatoreId = ValidatoreIdentificativi.newInstance();
			validatoreId.validaIdDominio("idDominio", idDominio);

			LeggiRicevutaDTO leggiPagamentoPortaleDTO = new LeggiRicevutaDTO(user);
			leggiPagamentoPortaleDTO.setIdDominio(idDominio);
			leggiPagamentoPortaleDTO.setIuv(iuv);
			ccp = ccp.contains("%") ? URLDecoder.decode(ccp,"UTF-8") : ccp;
			leggiPagamentoPortaleDTO.setCcp(ccp);
			
			if(visualizzaSoggettoDebitore != null)
				leggiPagamentoPortaleDTO.setVisualizzaSoggettoDebitore(visualizzaSoggettoDebitore.booleanValue()); 
			
			RptDAO ricevuteDAO = new RptDAO(); 

			LeggiRicevutaDTOResponse ricevutaDTOResponse = null; 

			if(accept.toLowerCase().contains(MediaType.APPLICATION_OCTET_STREAM)) {
				leggiPagamentoPortaleDTO.setFormato(FormatoRicevuta.RAW);
				ricevutaDTOResponse = ricevuteDAO.leggiRt(leggiPagamentoPortaleDTO);
				
				checkAutorizzazioniUtenza(leggiPagamentoPortaleDTO.getUser(), ricevutaDTOResponse.getRpt());
				this.log.debug(MessageFormat.format(BaseController.LOG_MSG_ESECUZIONE_METODO_COMPLETATA, methodName)); 
				return this.handleResponseOk(Response.status(Status.OK).type(MediaType.APPLICATION_OCTET_STREAM).entity(new String(ricevutaDTOResponse.getRpt().getXmlRt())),transactionId).build();
			} else {
				if(accept.toLowerCase().contains("application/pdf")) {
					leggiPagamentoPortaleDTO.setFormato(FormatoRicevuta.PDF);
					ricevutaDTOResponse = ricevuteDAO.leggiRt(leggiPagamentoPortaleDTO);
					String rtPdfEntryName = idDominio +"_"+ iuv + "_"+ ccp + ".pdf";
					byte[] b = ricevutaDTOResponse.getPdf(); 
					
					checkAutorizzazioniUtenza(leggiPagamentoPortaleDTO.getUser(), ricevutaDTOResponse.getRpt());
					this.log.debug(MessageFormat.format(BaseController.LOG_MSG_ESECUZIONE_METODO_COMPLETATA, methodName)); 
					return this.handleResponseOk(Response.status(Status.OK).type("application/pdf").entity(b).header("content-disposition", "attachment; filename=\""+rtPdfEntryName+"\""),transactionId).build();
				} else if(accept.toLowerCase().contains(MediaType.APPLICATION_JSON)) {
					leggiPagamentoPortaleDTO.setFormato(FormatoRicevuta.JSON);
					ricevutaDTOResponse = ricevuteDAO.leggiRt(leggiPagamentoPortaleDTO);
					
					checkAutorizzazioniUtenza(leggiPagamentoPortaleDTO.getUser(), ricevutaDTOResponse.getRpt());
					CtRicevutaTelematica rt = JaxbUtils.toRT(ricevutaDTOResponse.getRpt().getXmlRt(), false);
					this.log.debug(MessageFormat.format(BaseController.LOG_MSG_ESECUZIONE_METODO_COMPLETATA, methodName)); 
					return this.handleResponseOk(Response.status(Status.OK).type(MediaType.APPLICATION_JSON).entity(rt),transactionId).build();
				} else {
					leggiPagamentoPortaleDTO.setFormato(FormatoRicevuta.XML);
					ricevutaDTOResponse = ricevuteDAO.leggiRt(leggiPagamentoPortaleDTO);
					
					checkAutorizzazioniUtenza(leggiPagamentoPortaleDTO.getUser(), ricevutaDTOResponse.getRpt());
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



	public Response rppIdDominioIuvCcpRptGET(Authentication user, UriInfo uriInfo, HttpHeaders httpHeaders , String idDominio, String iuv, String ccp) {
		String methodName = "rppIdDominioIuvCcpRtGET";  
		String transactionId = ContextThreadLocal.get().getTransactionId();
		this.log.debug(MessageFormat.format(BaseController.LOG_MSG_ESECUZIONE_METODO_IN_CORSO, methodName)); 

		try{
			
			String accept = MediaType.APPLICATION_JSON;
			if(httpHeaders.getRequestHeaders().containsKey("Accept")) {
				accept = httpHeaders.getRequestHeaders().get("Accept").get(0).toLowerCase();
			}
			
			// autorizzazione sulla API
			this.isAuthorized(user, Arrays.asList(TIPO_UTENZA.ANONIMO, TIPO_UTENZA.CITTADINO, TIPO_UTENZA.APPLICAZIONE), Arrays.asList(Servizio.API_PAGAMENTI), Arrays.asList(Diritti.LETTURA));

			ValidatoreIdentificativi validatoreId = ValidatoreIdentificativi.newInstance();
			validatoreId.validaIdDominio("idDominio", idDominio);

			LeggiRptDTO leggiRptDTO = new LeggiRptDTO(user);
			leggiRptDTO.setIdDominio(idDominio);
			leggiRptDTO.setIuv(iuv);
			ccp = ccp.contains("%") ? URLDecoder.decode(ccp,"UTF-8") : ccp;
			leggiRptDTO.setCcp(ccp);
			
			RptDAO ricevuteDAO = new RptDAO(); 

			LeggiRptDTOResponse leggiRptDTOResponse = ricevuteDAO.leggiRpt(leggiRptDTO);
			checkAutorizzazioniUtenza(leggiRptDTO.getUser(), leggiRptDTOResponse.getRpt());

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



	public Response rppIdDominioIuvCcpGET(Authentication user, UriInfo uriInfo, HttpHeaders httpHeaders , String idDominio, String iuv, String ccp) {
		String methodName = "rppIdDominioIuvCcpGET";  
		String transactionId = ContextThreadLocal.get().getTransactionId();		
		this.log.debug(MessageFormat.format(BaseController.LOG_MSG_ESECUZIONE_METODO_IN_CORSO, methodName)); 

		try{
			// autorizzazione sulla API
			this.isAuthorized(user, Arrays.asList(TIPO_UTENZA.ANONIMO, TIPO_UTENZA.CITTADINO, TIPO_UTENZA.APPLICAZIONE), Arrays.asList(Servizio.API_PAGAMENTI), Arrays.asList(Diritti.LETTURA));

			ValidatoreIdentificativi validatoreId = ValidatoreIdentificativi.newInstance();
			validatoreId.validaIdDominio("idDominio", idDominio);

			LeggiRptDTO leggiRptDTO = new LeggiRptDTO(user);
			leggiRptDTO.setIdDominio(idDominio);
			leggiRptDTO.setIuv(iuv);
			ccp = ccp.contains("%") ? URLDecoder.decode(ccp,"UTF-8") : ccp;
			leggiRptDTO.setCcp(ccp);
			
			RptDAO ricevuteDAO = new RptDAO(); 

			LeggiRptDTOResponse leggiRptDTOResponse = ricevuteDAO.leggiRpt(leggiRptDTO);
			checkAutorizzazioniUtenza(leggiRptDTO.getUser(), leggiRptDTOResponse.getRpt());

			Rpp response =  RptConverter.toRsModel(leggiRptDTOResponse.getRpt(),leggiRptDTOResponse.getVersamento(),leggiRptDTOResponse.getApplicazione(),user);
			
			return this.handleResponseOk(Response.status(Status.OK).entity(response.toJSON(null)),transactionId).build();
		}catch (Exception e) {
			return this.handleException(uriInfo, httpHeaders, methodName, e, transactionId);
		} finally {
			this.log(ContextThreadLocal.get());
		}
	}

	private void checkAutorizzazioniUtenza(Authentication user, Rpt rpt) throws ServiceException, NotFoundException, NotAuthorizedException {
		PagamentoPortale pagamentoPortale = rpt.getPagamentoPortale(null);
		Versamento versamento = rpt.getVersamento(null); 
		
		GovpayLdapUserDetails details = AutorizzazioneUtils.getAuthenticationDetails(user);
		if(details.getTipoUtenza().equals(TIPO_UTENZA.CITTADINO)) {
			if((pagamentoPortale.getVersanteIdentificativo() == null || !pagamentoPortale.getVersanteIdentificativo().equals(details.getUtenza().getIdentificativo()))
					&& !versamento.getAnagraficaDebitore().getCodUnivoco().equals(details.getUtenza().getIdentificativo())) {
				throw AuthorizationManager.toNotAuthorizedException(user, "la transazione riferisce un pagamento che non appartiene al cittadino chiamante");
			}
		}
		
		if(details.getTipoUtenza().equals(TIPO_UTENZA.ANONIMO)) {
			if(pagamentoPortale.getVersanteIdentificativo() == null || !pagamentoPortale.getVersanteIdentificativo().equals(TIPO_UTENZA.ANONIMO.toString())) {
				throw AuthorizationManager.toNotAuthorizedException(user);
			}
			
			// pagamento terminato e' disponibile solo per un numero di minuti definito in configurazione
			if(pagamentoPortale.getDataRichiesta() != null) {
				long dataPagamentoTime = pagamentoPortale.getDataRichiesta().getTime();
				Calendar calendar = Calendar.getInstance();
				calendar.setTime(new Date());
				calendar.add(Calendar.MINUTE, -GovpayConfig.getInstance().getIntervalloDisponibilitaPagamentoUtenzaAnonima());
				long riferimentoTime = calendar.getTimeInMillis();

				// il pagamento e' stato eseguito prima dei minuti precedenti il momento della richiesta.
				if(dataPagamentoTime < riferimentoTime)
					throw AuthorizationManager.toNotAuthorizedException(user);
			}
		}
		
		// se sei una applicazione allora vedi i pagamenti che hai caricato
		if(details.getTipoUtenza().equals(TIPO_UTENZA.APPLICAZIONE)) {
			if(pagamentoPortale.getApplicazione(null) == null || 
					!pagamentoPortale.getApplicazione(null).getCodApplicazione().equals(details.getApplicazione().getCodApplicazione())) {
				throw AuthorizationManager.toNotAuthorizedException(user, "la transazione riferisce un pagamento che non appartiene all'applicazione chiamante");
			}
		}
	}


}


