/*
 * GovPay - Porta di Accesso al Nodo dei Pagamenti SPC 
 * http://www.gov4j.it/govpay
 * 
 * Copyright (c) 2014-2024 Link.it srl (http://www.link.it).
 * 
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License version 3, as published by
 * the Free Software Foundation.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 *
 */
package it.govpay.web.ws;

import java.io.UnsupportedEncodingException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import jakarta.annotation.Resource;
import jakarta.jws.WebService;
import jakarta.xml.ws.WebServiceContext;

import org.apache.cxf.annotations.SchemaValidation.SchemaValidationType;
import org.openspcoop2.generic_project.exception.NotAuthorizedException;
import org.openspcoop2.generic_project.exception.NotFoundException;
import org.openspcoop2.generic_project.exception.ServiceException;
import org.openspcoop2.utils.LoggerWrapperFactory;
import org.openspcoop2.utils.UtilsException;
import org.openspcoop2.utils.logger.beans.Property;
import org.openspcoop2.utils.logger.beans.context.core.Actor;
import org.openspcoop2.utils.service.context.ContextThreadLocal;
import org.openspcoop2.utils.service.context.IContext;
import org.slf4j.Logger;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

import gov.telematici.pagamenti.ws.ccp.CtSpezzoneStrutturatoCausaleVersamento;
import gov.telematici.pagamenti.ws.ccp.CtSpezzoniCausaleVersamento;
import gov.telematici.pagamenti.ws.ccp.EsitoAttivaRPT;
import gov.telematici.pagamenti.ws.ccp.EsitoVerificaRPT;
import gov.telematici.pagamenti.ws.ccp.FaultBean;
import gov.telematici.pagamenti.ws.ccp.PaaAttivaRPT;
import gov.telematici.pagamenti.ws.ccp.PaaAttivaRPTRisposta;
import gov.telematici.pagamenti.ws.ccp.PaaInviaRT;
import gov.telematici.pagamenti.ws.ccp.PaaInviaRTRisposta;
import gov.telematici.pagamenti.ws.ccp.PaaTipoDatiPagamentoPA;
import gov.telematici.pagamenti.ws.ccp.PaaVerificaRPT;
import gov.telematici.pagamenti.ws.ccp.PaaVerificaRPTRisposta;
import gov.telematici.pagamenti.ws.ppthead.IntestazionePPT;
import it.gov.pagopa.pagopa_api.pa.pafornode.CtPaymentOptionDescriptionPA;
import it.gov.pagopa.pagopa_api.pa.pafornode.CtPaymentOptionsDescriptionListPA;
import it.gov.pagopa.pagopa_api.pa.pafornode.CtQrCode;
import it.gov.pagopa.pagopa_api.pa.pafornode.CtReceipt;
import it.gov.pagopa.pagopa_api.pa.pafornode.CtReceiptV2;
import it.gov.pagopa.pagopa_api.pa.pafornode.PaDemandPaymentNoticeRequest;
import it.gov.pagopa.pagopa_api.pa.pafornode.PaDemandPaymentNoticeResponse;
import it.gov.pagopa.pagopa_api.pa.pafornode.PaGetPaymentReq;
import it.gov.pagopa.pagopa_api.pa.pafornode.PaGetPaymentRes;
import it.gov.pagopa.pagopa_api.pa.pafornode.PaGetPaymentV2Request;
import it.gov.pagopa.pagopa_api.pa.pafornode.PaGetPaymentV2Response;
import it.gov.pagopa.pagopa_api.pa.pafornode.PaSendRTReq;
import it.gov.pagopa.pagopa_api.pa.pafornode.PaSendRTRes;
import it.gov.pagopa.pagopa_api.pa.pafornode.PaSendRTV2Request;
import it.gov.pagopa.pagopa_api.pa.pafornode.PaSendRTV2Response;
import it.gov.pagopa.pagopa_api.pa.pafornode.PaVerifyPaymentNoticeReq;
import it.gov.pagopa.pagopa_api.pa.pafornode.PaVerifyPaymentNoticeRes;
import it.gov.pagopa.pagopa_api.pa.pafornode.StAmountOption;
import it.gov.pagopa.pagopa_api.xsd.common_types.v1_0.CtFaultBean;
import it.gov.pagopa.pagopa_api.xsd.common_types.v1_0.StOutcome;
import it.gov.spcoop.nodopagamentispc.servizi.pagamentitelematiciccp.PagamentiTelematiciCCP;
import it.govpay.bd.BDConfigWrapper;
import it.govpay.bd.anagrafica.AnagraficaManager;
import it.govpay.bd.model.Dominio;
import it.govpay.bd.model.Notifica;
import it.govpay.bd.model.Pagamento;
import it.govpay.bd.model.PagamentoPortale;
import it.govpay.bd.model.PagamentoPortale.CODICE_STATO;
import it.govpay.bd.model.PagamentoPortale.STATO;
import it.govpay.bd.model.Rpt;
import it.govpay.bd.model.SingoloVersamento;
import it.govpay.bd.model.Stazione;
import it.govpay.bd.model.Versamento;
import it.govpay.bd.pagamento.PagamentiBD;
import it.govpay.bd.pagamento.PagamentiPortaleBD;
import it.govpay.bd.pagamento.RptBD;
import it.govpay.bd.pagamento.VersamentiBD;
import it.govpay.bd.pagamento.filters.RptFilter;
import it.govpay.core.autorizzazione.AuthorizationManager;
import it.govpay.core.autorizzazione.beans.GovpayLdapUserDetails;
import it.govpay.core.autorizzazione.utils.AutorizzazioneUtils;
import it.govpay.core.beans.EsitoOperazione;
import it.govpay.core.beans.EventoContext.Esito;
import it.govpay.core.business.Applicazione;
import it.govpay.core.exceptions.GovPayException;
import it.govpay.core.exceptions.NdpException;
import it.govpay.core.exceptions.NdpException.FaultPa;
import it.govpay.core.exceptions.VersamentoAnnullatoException;
import it.govpay.core.exceptions.VersamentoDuplicatoException;
import it.govpay.core.exceptions.VersamentoNonValidoException;
import it.govpay.core.exceptions.VersamentoScadutoException;
import it.govpay.core.exceptions.VersamentoSconosciutoException;
import it.govpay.core.utils.CtPaymentPABuilder;
import it.govpay.core.utils.CtPaymentPAV2Builder;
import it.govpay.core.utils.CtReceiptUtils;
import it.govpay.core.utils.CtReceiptV2Utils;
import it.govpay.core.utils.DateUtils;
import it.govpay.core.utils.GovpayConfig;
import it.govpay.core.utils.GpContext;
import it.govpay.core.utils.IuvUtils;
import it.govpay.core.utils.RptBuilder;
import it.govpay.core.utils.RptUtils;
import it.govpay.core.utils.VersamentoUtils;
import it.govpay.core.utils.client.exception.ClientException;
import it.govpay.core.utils.thread.InviaNotificaThread;
import it.govpay.core.utils.thread.ThreadExecutorManager;
import it.govpay.model.Canale.ModelloPagamento;
import it.govpay.model.Canale.TipoVersamento;
import it.govpay.model.Evento.TipoEventoCooperazione;
import it.govpay.model.IbanAccredito;
import it.govpay.model.Intermediario;
import it.govpay.model.Notifica.TipoNotifica;
import it.govpay.model.Rpt.StatoRpt;
import it.govpay.model.Utenza.TIPO_UTENZA;
import it.govpay.model.Versamento.CausaleSemplice;
import it.govpay.model.Versamento.CausaleSpezzoni;
import it.govpay.model.Versamento.CausaleSpezzoniStrutturati;
import it.govpay.model.Versamento.StatoVersamento;
import it.govpay.model.Versamento.TipologiaTipoVersamento;
import it.govpay.model.eventi.DatiPagoPA;
import it.govpay.orm.IdVersamento;
import it.govpay.pagopa.beans.utils.JaxbUtils;
import it.govpay.web.ws.converter.PaaInviaRTConverter;
import it.govpay.web.ws.converter.PaaInviaRTRispostaConverter;


@WebService(serviceName = "PagamentiTelematiciCCPservice",
endpointInterface = "it.gov.spcoop.nodopagamentispc.servizi.pagamentitelematiciccp.PagamentiTelematiciCCP",
targetNamespace = "http://NodoPagamentiSPC.spcoop.gov.it/servizi/PagamentiTelematiciCCP",
portName = "PPTPort",
wsdlLocation="/wsdl/PaPerNodoPagamentoPsp.wsdl")

@org.apache.cxf.annotations.SchemaValidation(type = SchemaValidationType.IN)
public class PagamentiTelematiciCCPImpl implements PagamentiTelematiciCCP {

	@Resource
	WebServiceContext wsCtxt;

	private static Logger log = LoggerWrapperFactory.getLogger(PagamentiTelematiciCCPImpl.class);

	@Override
	public PaaAttivaRPTRisposta paaAttivaRPT(PaaAttivaRPT bodyrichiesta, IntestazionePPT header) {

		SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
		
		String codIntermediario = header.getIdentificativoIntermediarioPA();
		String codStazione = header.getIdentificativoStazioneIntermediarioPA();
		String codDominio = header.getIdentificativoDominio();
		String iuv = header.getIdentificativoUnivocoVersamento();
		String ccp = header.getCodiceContestoPagamento();
		
		IContext ctx = ContextThreadLocal.get();
		GpContext appContext = (GpContext) ctx.getApplicationContext();
		appContext.setCorrelationId(codDominio + iuv + ccp);
		
		appContext.getEventoCtx().setCodDominio(codDominio);
		appContext.getEventoCtx().setIuv(iuv);
		appContext.getEventoCtx().setCcp(ccp);

		Actor from = new Actor();
		from.setName(GpContext.NodoDeiPagamentiSPC);
		from.setType(GpContext.TIPO_SOGGETTO_NDP);
		appContext.getTransaction().setFrom(from);

		Actor to = new Actor();
		to.setName(header.getIdentificativoStazioneIntermediarioPA());
		from.setType(GpContext.TIPO_SOGGETTO_STAZIONE);
		appContext.getTransaction().setTo(to);

		appContext.getRequest().addGenericProperty(new Property("ccp", ccp));
		appContext.getRequest().addGenericProperty(new Property("codDominio", codDominio));
		appContext.getRequest().addGenericProperty(new Property("iuv", iuv));
		appContext.getRequest().addGenericProperty(new Property("codPsp", bodyrichiesta.getIdentificativoPSP()));
		appContext.getRequest().addGenericProperty(new Property("codCanale", bodyrichiesta.getIdentificativoCanalePSP()));
		try {
			ctx.getApplicationLogger().log("ccp.ricezioneAttiva");
		} catch (UtilsException e) {
			log.error("Errore durante il log dell'operazione: " + e.getMessage(),e);
		}

//		BasicBD bd = null;
		PaaAttivaRPTRisposta response = new PaaAttivaRPTRisposta();
		log.info("Ricevuta richiesta di attiva RPT [" + codIntermediario + "][" + codStazione + "][" + codDominio + "][" + iuv + "][" + ccp + "]");

		DatiPagoPA datiPagoPA = new DatiPagoPA();
		datiPagoPA.setCodStazione(codStazione);
		datiPagoPA.setFruitore(GpContext.NodoDeiPagamentiSPC);
		datiPagoPA.setErogatore(codIntermediario);
		datiPagoPA.setCodIntermediario(codIntermediario);
//		appContext.getEventoCtx().setTipoEvento(TipoEventoCooperazione.paaAttivaRPT.name());
		datiPagoPA.setCodPsp(bodyrichiesta.getIdentificativoPSP());
		datiPagoPA.setCodIntermediarioPsp(bodyrichiesta.getIdentificativoIntermediarioPSP());
		datiPagoPA.setCodPsp(bodyrichiesta.getIdentificativoPSP());
		datiPagoPA.setCodCanale(bodyrichiesta.getIdentificativoCanalePSP());
		datiPagoPA.setTipoVersamento(TipoVersamento.ATTIVATO_PRESSO_PSP);
		datiPagoPA.setModelloPagamento(ModelloPagamento.ATTIVATO_PRESSO_PSP);
		datiPagoPA.setCodDominio(codDominio);
		
		appContext.getEventoCtx().setDatiPagoPA(datiPagoPA);
		
		log.debug("Impostati dati pagoPA nel contesto");

		try {
			BDConfigWrapper configWrapper = new BDConfigWrapper(ContextThreadLocal.get().getTransactionId(), true);

			log.debug("Autenticazione richiesta.");
			
			Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
			appContext.getEventoCtx().setPrincipal(AutorizzazioneUtils.getPrincipal(authentication));
			if(GovpayConfig.getInstance().isPddAuthEnable() && authentication == null) {
				ctx.getApplicationLogger().log("ccp.erroreNoAutorizzazione");
				throw new NotAuthorizedException("Autorizzazione fallita: principal non fornito");
			}
			
			log.debug("Richiesta autenticata.");
			
			log.debug("Verifica intermediario.");
			Intermediario intermediario = null;
			try {
				intermediario = AnagraficaManager.getIntermediario(configWrapper, codIntermediario);

				// Controllo autorizzazione
				if(GovpayConfig.getInstance().isPddAuthEnable()){
					boolean authOk = AuthorizationManager.checkPrincipal(authentication, intermediario.getPrincipal()); 
					
					if(!authOk) {
						GovpayLdapUserDetails details = AutorizzazioneUtils.getAuthenticationDetails(authentication);
						String principal = details.getIdentificativo(); 
						ctx.getApplicationLogger().log("ccp.erroreAutorizzazione", principal);
						throw new NotAuthorizedException("Autorizzazione fallita: principal fornito (" + principal + ") non valido per l'intermediario (" + codIntermediario + ").");
					}
				}
			} catch (NotFoundException e) {
				throw new NdpException(FaultPa.PAA_ID_INTERMEDIARIO_ERRATO, codDominio);
			}
			log.debug("Intermediario verificato: " + intermediario.getCodIntermediario());

			try {
				AnagraficaManager.getStazione(configWrapper, codStazione);
			} catch (NotFoundException e) {
				throw new NdpException(FaultPa.PAA_STAZIONE_INT_ERRATA, codDominio);
			}

			log.debug("Verifica dominio.");
			Dominio dominio;
			try {
				dominio = AnagraficaManager.getDominio(configWrapper, codDominio);
			} catch (NotFoundException e) {
				throw new NdpException(FaultPa.PAA_ID_DOMINIO_ERRATO, codDominio);
			}
			log.debug("Dominio verificato: " + dominio.getCodDominio());

			log.debug("Verifica versamento.");
			VersamentiBD versamentiBD = new VersamentiBD(configWrapper);
			Versamento versamento = null;
			it.govpay.bd.model.Applicazione applicazioneGestisceIuv = null;
			try {
				versamento = versamentiBD.getVersamentoByDominioIuv(dominio.getId(), iuv, true);
				appContext.getEventoCtx().setIdA2A(versamento.getApplicazione(configWrapper).getCodApplicazione());
				appContext.getEventoCtx().setIdPendenza(versamento.getCodVersamentoEnte());
			}catch (NotFoundException e) {
				applicazioneGestisceIuv = new Applicazione().getApplicazioneDominio(configWrapper,dominio,iuv,false); 
				
				if(applicazioneGestisceIuv == null) {
					throw new NdpException(FaultPa.PAA_PAGAMENTO_SCONOSCIUTO, codDominio);
				}
				appContext.getEventoCtx().setIdA2A(applicazioneGestisceIuv.getCodApplicazione());
			}
			try {
				try {
					// Se non ho lo iuv, vado direttamente a chiedere all'applicazione di default
					if(versamento == null) throw new NotFoundException();
					
					log.debug("Versamento acquisito");

					// Versamento trovato, gestisco un'eventuale scadenza
					versamento = VersamentoUtils.aggiornaVersamento(versamento, log);
					log.debug("Verifica stato versamento dopo procedura di aggiornamento: " + versamento.getStatoVersamento());
					if(versamento.getStatoVersamento().equals(StatoVersamento.ANNULLATO))
						throw new NdpException(FaultPa.PAA_PAGAMENTO_ANNULLATO, codDominio);

					if(!versamento.getStatoVersamento().equals(StatoVersamento.NON_ESEGUITO)) {
						
						if(versamento.getStatoVersamento().equals(StatoVersamento.ESEGUITO) || versamento.getStatoVersamento().equals(StatoVersamento.ESEGUITO_ALTRO_CANALE)) {
							PagamentiBD pagamentiBD = new PagamentiBD(configWrapper);
							List<Pagamento> pagamenti = pagamentiBD.getPagamentiBySingoloVersamento(versamento.getSingoliVersamenti().get(0).getId());
							if(pagamenti.isEmpty())
								throw new NdpException(FaultPa.PAA_PAGAMENTO_DUPLICATO, codDominio);
							else {
								Pagamento pagamento = pagamenti.get(0);
								throw new NdpException(FaultPa.PAA_PAGAMENTO_DUPLICATO, "Il pagamento risulta gi\u00E0 effettuato in data " + sdf.format(pagamento.getDataPagamento()) + " [Iur:" + pagamento.getIur() + "]", codDominio);
							}
						}
					}
				} catch (NotFoundException e) {
					log.debug("Versamento non presente in base dati");
					//Versamento non trovato, devo interrogare l'applicazione.
					// prendo tutte le applicazioni che gestiscono il dominio, tra queste cerco la prima che match la regexpr dello iuv la utilizzo per far acquisire il versamento
					if(applicazioneGestisceIuv == null) {
						applicazioneGestisceIuv = new Applicazione().getApplicazioneDominio(configWrapper, dominio,iuv); 
						appContext.getEventoCtx().setIdA2A(applicazioneGestisceIuv.getCodApplicazione());
					}
					
					ctx.getApplicationLogger().log("ccp.versamentoIuvNonPresente", applicazioneGestisceIuv.getCodApplicazione(), dominio.getCodDominio(), iuv);

					log.debug("Versamento non presente in base dati. Procedo all'acquisizione [CodApplicazione:" + applicazioneGestisceIuv.getCodApplicazione()+"]");
					versamento = VersamentoUtils.acquisisciVersamento(AnagraficaManager.getApplicazione(configWrapper, applicazioneGestisceIuv.getCodApplicazione()), null, null, null, codDominio, iuv, TipologiaTipoVersamento.DOVUTO, log);
					appContext.getEventoCtx().setIdA2A(versamento.getApplicazione(configWrapper).getCodApplicazione());
					appContext.getEventoCtx().setIdPendenza(versamento.getCodVersamentoEnte());
					log.debug("Versamento acquisito da Ente. Aggiorno per verifica scadenza [CodApplicazione:" + applicazioneGestisceIuv.getCodApplicazione()+"]");
					// Versamento trovato, gestisco un'eventuale scadenza
					versamento = VersamentoUtils.aggiornaVersamento(versamento, log);
					log.debug("Versamento aggiornato da Ente. Verifica stato " + versamento.getStatoVersamento());
					
					if(versamento.getStatoVersamento().equals(StatoVersamento.ANNULLATO))
						throw new NdpException(FaultPa.PAA_PAGAMENTO_ANNULLATO, codDominio);

					if(!versamento.getStatoVersamento().equals(StatoVersamento.NON_ESEGUITO)) {
						
						if(versamento.getStatoVersamento().equals(StatoVersamento.ESEGUITO) || versamento.getStatoVersamento().equals(StatoVersamento.ESEGUITO_ALTRO_CANALE)) {
							PagamentiBD pagamentiBD = new PagamentiBD(configWrapper);
							List<Pagamento> pagamenti = pagamentiBD.getPagamentiBySingoloVersamento(versamento.getSingoliVersamenti().get(0).getId());
							if(pagamenti.isEmpty())
								throw new NdpException(FaultPa.PAA_PAGAMENTO_DUPLICATO, codDominio);
							else {
								Pagamento pagamento = pagamenti.get(0);
								throw new NdpException(FaultPa.PAA_PAGAMENTO_DUPLICATO, "Il pagamento risulta gi\u00E0 effettuato in data " + sdf.format(pagamento.getDataPagamento()) + " [Iur:" + pagamento.getIur() + "]", codDominio);
							}
						}
					}
					log.debug("Versamento pagabile.");
					ctx.getApplicationLogger().log("ccp.versamentoIuvNonPresenteOk", applicazioneGestisceIuv.getCodApplicazione(), dominio.getCodDominio(), iuv);
				}
			} catch (VersamentoScadutoException e1) {
				appContext.getEventoCtx().setIdA2A(e1.getCodApplicazione());
				appContext.getEventoCtx().setIdPendenza(e1.getCodVersamentoEnte());
				throw new NdpException(FaultPa.PAA_PAGAMENTO_SCADUTO, e1.getMessage(), codDominio, e1);
			} catch (VersamentoAnnullatoException e1) {
				appContext.getEventoCtx().setIdA2A(e1.getCodApplicazione());
				appContext.getEventoCtx().setIdPendenza(e1.getCodVersamentoEnte());
				throw new NdpException(FaultPa.PAA_PAGAMENTO_ANNULLATO, e1.getMessage(), codDominio, e1);
			} catch (VersamentoDuplicatoException e1) {
				appContext.getEventoCtx().setIdA2A(e1.getCodApplicazione());
				appContext.getEventoCtx().setIdPendenza(e1.getCodVersamentoEnte());
				throw new NdpException(FaultPa.PAA_PAGAMENTO_DUPLICATO, e1.getMessage(), codDominio, e1);
			} catch (VersamentoSconosciutoException e1) {
				appContext.getEventoCtx().setIdA2A(e1.getCodApplicazione());
				appContext.getEventoCtx().setIdPendenza(e1.getCodVersamentoEnte());
				throw new NdpException(FaultPa.PAA_PAGAMENTO_SCONOSCIUTO, e1.getMessage(), codDominio, e1);
			} catch (VersamentoNonValidoException e1) {
				appContext.getEventoCtx().setIdA2A(e1.getCodApplicazione());
				appContext.getEventoCtx().setIdPendenza(e1.getCodVersamentoEnte());
				throw new NdpException(FaultPa.PAA_SYSTEM_ERROR, e1.getMessage(), codDominio, e1);
			} catch (ClientException e1) {
				throw new NdpException(FaultPa.PAA_SYSTEM_ERROR, "Riscontrato errore durante l'acquisizione del versamento dall'applicazione gestore del debito: " + e1, codDominio, e1);
			} catch (GovPayException e1) {
				throw new NdpException(FaultPa.PAA_SYSTEM_ERROR, "Riscontrato errore durante l'attivazione del versamento: " + e1, codDominio, e1);
			}
			
			// controllo se la pendenza e' multibeneficiario, se lo e' allora non puo' essere pagata col modello3 SANP 2.3
			if(VersamentoUtils.isPendenzaMultibeneficiario(versamento, configWrapper)) {
				String messaggio = "La pendenza e' di tipo multibeneficiario, non ammesso per pagamenti ad iniziativa psp con specifica SANP 2.3.0.";
				throw new NdpException(FaultPa.PAA_PAGAMENTO_MULTIBENEFICIARIO_NON_CONSENTITO, messaggio, codDominio);
			}
			
			log.debug("Verifica pagamenti in corso.");
			RptBD rptBD = new RptBD(configWrapper);
			if(GovpayConfig.getInstance().isTimeoutPendentiModello3()) {
				// Controllo che non ci sia un pagamento in corso
				// Prendo tutte le RPT pendenti
				RptFilter filter = rptBD.newFilter();
				filter.setStato(Rpt.getStatiPendenti());
				filter.setIdVersamento(versamento.getId());
				List<Rpt> rpt_pendenti = rptBD.findAll(filter);
				
				// Per tutte quelle in corso controllo se hanno passato la soglia di timeout
				// Altrimenti lancio il fault
				Date dataSoglia = new Date(new Date().getTime() - GovpayConfig.getInstance().getTimeoutPendentiModello3Mins() * 60000);
				
				for(Rpt rpt_pendente : rpt_pendenti) {
					Date dataMsgRichiesta = rpt_pendente.getDataMsgRichiesta();
					
					// se l'RPT e' bloccata allora controllo che il blocco sia indefinito oppure definito, altrimenti passo
					if(rpt_pendente.isBloccante() && (GovpayConfig.getInstance().getTimeoutPendentiModello3Mins() == 0 || dataSoglia.before(dataMsgRichiesta))) {
						throw new NdpException(FaultPa.PAA_PAGAMENTO_IN_CORSO, codDominio, "Pagamento in corso [CCP:" + rpt_pendente.getCcp() + "].");
					}
				}
			}
			log.debug("Verifica pagamenti in corso completata.");
			// Verifico che abbia un solo singolo versamento
			if(versamento.getSingoliVersamenti().size() != 1) {
				throw new NdpException(FaultPa.PAA_SEMANTICA, "Il versamento contiene piu' di un singolo versamento, non ammesso per pagamenti ad iniziativa psp.", codDominio);
			}
			
			// controllo che la pendenza non contenga una MBT
			if(VersamentoUtils.isPendenzaMBT(versamento, configWrapper)) {
				throw new NdpException(FaultPa.PAA_SEMANTICA, "Il versamento contiene una marca da bollo telematica, non ammessa per pagamenti ad iniziativa psp.", codDominio);
			}

			log.debug("Costruzione della RPT.");
			// Creazione dell'RPT
			Rpt rpt = new RptBuilder().buildRptAttivata(bodyrichiesta.getIdentificativoIntermediarioPSP(), bodyrichiesta.getIdentificativoPSP(), bodyrichiesta.getIdentificativoCanalePSP(), versamento, iuv, ccp, bodyrichiesta.getDatiPagamentoPSP());
			log.debug("RPT predisposta.");
			ctx.getApplicationLogger().log("ccp.attivazione", rpt.getCodMsgRichiesta());

			rptBD = null;
			try {
				log.debug("Transazione di avvio pagamento attivata");
				rptBD = new RptBD(configWrapper);
				
				rptBD.setupConnection(configWrapper.getTransactionID());
				
				rptBD.setAtomica(false);
				
				rptBD.setAutoCommit(false);
				
				rptBD.enableSelectForUpdate();
	
				// Controllo se gia' non esiste la RPT (lo devo fare solo adesso per essere in transazione con l'inserimento)
				try {
					log.debug("Verifica RPT precedente per eventuale transazione concorrente.");
					Rpt oldrpt = rptBD.getRpt(codDominio, iuv, ccp, true);
					if(oldrpt.getPagamentoPortale() != null)
						appContext.getEventoCtx().setIdPagamento(oldrpt.getPagamentoPortale().getIdSessione());
					throw new NdpException(FaultPa.PAA_PAGAMENTO_IN_CORSO, "RTP attivata in data " + oldrpt.getDataMsgRichiesta() + " [idMsgRichiesta: " + oldrpt.getCodMsgRichiesta() + "]" , codDominio);
				} catch (NotFoundException e2) {
	
					log.debug("Creazione PagamentoPortale.");
					PagamentoPortale pagamentoPortale = new PagamentoPortale();
					Versamento versamento2 = rpt.getVersamento();
					it.govpay.bd.model.Applicazione applicazione = AnagraficaManager.getApplicazione(configWrapper, versamento2.getIdApplicazione());
					pagamentoPortale.setPrincipal(applicazione.getPrincipal());
					pagamentoPortale.setTipoUtenza(TIPO_UTENZA.APPLICAZIONE);
					pagamentoPortale.setCodCanale(rpt.getCodCanale());
					pagamentoPortale.setCodiceStato(CODICE_STATO.PAGAMENTO_IN_CORSO_AL_PSP);
					pagamentoPortale.setCodPsp(rpt.getCodPsp());
					pagamentoPortale.setDataRichiesta(rpt.getDataMsgRichiesta());
					pagamentoPortale.setIdSessione(ctx.getTransactionId().replaceAll("-", ""));
					
					appContext.getEventoCtx().setIdPagamento(pagamentoPortale.getIdSessione());
	
					List<IdVersamento> idVersamentoList = new ArrayList<>();
	
					IdVersamento idVersamento = new IdVersamento();
					idVersamento.setCodVersamentoEnte(versamento2.getCodVersamentoEnte());
					idVersamento.setId(versamento2.getId());
					
					idVersamentoList.add(idVersamento);
					pagamentoPortale.setIdVersamento(idVersamentoList);
					
					pagamentoPortale.setImporto(versamento2.getImportoTotale());
					pagamentoPortale.setMultiBeneficiario(rpt.getCodDominio());
					
					if(versamento2.getNome()!=null) {
						pagamentoPortale.setNome(versamento2.getNome());
					} else {
						try {
							pagamentoPortale.setNome(versamento2.getCausaleVersamento().getSimple());
						} catch(UnsupportedEncodingException e) {}
					}
	
					pagamentoPortale.setStato(STATO.IN_CORSO);
					pagamentoPortale.setTipo(3);
					
					if(bodyrichiesta.getDatiPagamentoPSP().getSoggettoVersante() != null)
						pagamentoPortale.setVersanteIdentificativo(bodyrichiesta.getDatiPagamentoPSP().getSoggettoVersante().getIdentificativoUnivocoVersante().getCodiceIdentificativoUnivoco());
	
					PagamentiPortaleBD ppbd = new PagamentiPortaleBD(rptBD);
					ppbd.setAtomica(false);
										
					ppbd.insertPagamento(pagamentoPortale, true);
					
					// imposto l'id pagamento all'rpt
					rpt.setIdPagamentoPortale(pagamentoPortale.getId());
					rpt.setPagamentoPortale(pagamentoPortale);
					log.debug("Creazione RPT.");
					try {
						// 	L'RPT non esiste, procedo
						rptBD.insertRpt(rpt);
					}catch(ServiceException e) {
						rptBD.rollback();
						rptBD.disableSelectForUpdate();
	
						// update della entry pagamento portale
						pagamentoPortale.setCodiceStato(CODICE_STATO.PAGAMENTO_FALLITO);
						pagamentoPortale.setStato(STATO.FALLITO);
						pagamentoPortale.setDescrizioneStato(e.getMessage());
						pagamentoPortale.setAck(false);
						ppbd.updatePagamento(pagamentoPortale, false, true);
						
						ppbd.commit();
						throw e;
					}
					log.debug("Schedulazione nodoInviaRPT.");
					RptUtils.inviaRPTAsync(rpt, ctx);
					log.debug("Schedulazione nodoInviaRPT completata.");
				} 
	
				rptBD.commit();
				log.debug("Transazione di avvio pagamento conclusa");
				rptBD.disableSelectForUpdate();
			} catch (NdpException | ServiceException e) {
				if(rptBD != null && !rptBD.isAutoCommit()) {
					rptBD.rollback();
				}
				throw e;
			} finally {
				if(rptBD != null) {
					rptBD.closeConnection();
				}
			}

			log.debug("Predisposizione esito");
			EsitoAttivaRPT esito = new EsitoAttivaRPT();
			esito.setEsito("OK");
			PaaTipoDatiPagamentoPA datiPagamento =  new PaaTipoDatiPagamentoPA();
			datiPagamento.setImportoSingoloVersamento(versamento.getImportoTotale());
			if(versamento.getCausaleVersamento() != null) {
				if(versamento.getCausaleVersamento() instanceof CausaleSemplice) {
					datiPagamento.setCausaleVersamento(((CausaleSemplice) versamento.getCausaleVersamento()).getCausale());
				}

				if(versamento.getCausaleVersamento() instanceof CausaleSpezzoni) {
					datiPagamento.setSpezzoniCausaleVersamento(new CtSpezzoniCausaleVersamento());
					datiPagamento.getSpezzoniCausaleVersamento().getSpezzoneCausaleVersamentoOrSpezzoneStrutturatoCausaleVersamento().addAll(((CausaleSpezzoni) versamento.getCausaleVersamento()).getSpezzoni());
				}

				if(versamento.getCausaleVersamento() instanceof CausaleSpezzoniStrutturati) {
					datiPagamento.setSpezzoniCausaleVersamento(new CtSpezzoniCausaleVersamento());
					CausaleSpezzoniStrutturati causale = (CausaleSpezzoniStrutturati) versamento.getCausaleVersamento();
					for(int i=0; i < causale.getSpezzoni().size(); i++) {
						CtSpezzoneStrutturatoCausaleVersamento spezzone = new CtSpezzoneStrutturatoCausaleVersamento();
						spezzone.setCausaleSpezzone(causale.getSpezzoni().get(i));
						spezzone.setImportoSpezzone(causale.getImporti().get(i));
						datiPagamento.getSpezzoniCausaleVersamento().getSpezzoneCausaleVersamentoOrSpezzoneStrutturatoCausaleVersamento().add(spezzone);
					}
				}
			} else {
				datiPagamento.setCausaleVersamento(" ");
			}
			
			datiPagamento.setEnteBeneficiario(RptUtils.buildEnteBeneficiario(dominio, versamento.getUo(configWrapper)));
			
			SingoloVersamento singoloVersamento = versamento.getSingoliVersamenti().get(0);
			IbanAccredito ibanAccredito = singoloVersamento.getIbanAccredito(configWrapper);
			
			if(ibanAccredito != null) {
				datiPagamento.setBicAccredito(ibanAccredito.getCodBic());
				datiPagamento.setIbanAccredito(ibanAccredito.getCodIban());
			}
			esito.setDatiPagamentoPA(datiPagamento);
			response.setPaaAttivaRPTRisposta(esito);
			ctx.getApplicationLogger().log("ccp.ricezioneAttivaOk", datiPagamento.getImportoSingoloVersamento().toString(), datiPagamento.getIbanAccredito(), versamento.getCausaleVersamento() != null ? versamento.getCausaleVersamento().toString() : "[-- Nessuna causale --]");
			appContext.getEventoCtx().setEsito(Esito.OK);
			log.debug("Attivazione RPT completata con successo");
		} catch (NdpException e) {
			response = this.buildRisposta(e, response);
			String faultDescription = response.getPaaAttivaRPTRisposta().getFault().getDescription() == null ? "<Nessuna descrizione>" : response.getPaaAttivaRPTRisposta().getFault().getDescription(); 
			try {
				ctx.getApplicationLogger().log("ccp.ricezioneAttivaKo", response.getPaaAttivaRPTRisposta().getFault().getFaultCode(), response.getPaaAttivaRPTRisposta().getFault().getFaultString(), faultDescription);
			} catch (UtilsException e1) {
				log.error("Errore durante il log dell'operazione: " + e1.getMessage(),e1);
			}
			appContext.getEventoCtx().setDescrizioneEsito(faultDescription);
			appContext.getEventoCtx().setSottotipoEsito(e.getFaultCode());
			if(e.getFaultCode().equals(FaultPa.PAA_SYSTEM_ERROR.name()))
				appContext.getEventoCtx().setEsito(Esito.FAIL);
			else 
				appContext.getEventoCtx().setEsito(Esito.KO);
			
			log.debug("Attivazione RPT completata con esito " + appContext.getEventoCtx().getEsito());
		} catch (Exception e) {
			response = this.buildRisposta(e, codDominio, response);
			String faultDescription = response.getPaaAttivaRPTRisposta().getFault().getDescription() == null ? "<Nessuna descrizione>" : response.getPaaAttivaRPTRisposta().getFault().getDescription(); 
			try {
				ctx.getApplicationLogger().log("ccp.ricezioneAttivaKo", response.getPaaAttivaRPTRisposta().getFault().getFaultCode(), response.getPaaAttivaRPTRisposta().getFault().getFaultString(), faultDescription);
			} catch (UtilsException e1) {
				log.error("Errore durante il log dell'operazione: " + e1.getMessage(),e1);
			}
			appContext.getEventoCtx().setSottotipoEsito(response.getPaaAttivaRPTRisposta().getFault().getFaultCode());
			appContext.getEventoCtx().setDescrizioneEsito(faultDescription);
			appContext.getEventoCtx().setEsito(Esito.FAIL);
			log.debug("Attivazione RPT completata con esito FAIL");
		} finally {
			GpContext.setResult(appContext.getTransaction(), response.getPaaAttivaRPTRisposta().getFault() == null ? null : response.getPaaAttivaRPTRisposta().getFault().getFaultCode());
		}
		return response;
	}



	@Override
	public PaaVerificaRPTRisposta paaVerificaRPT(PaaVerificaRPT bodyrichiesta, IntestazionePPT header) {
		SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
		String codIntermediario = header.getIdentificativoIntermediarioPA();
		String codStazione = header.getIdentificativoStazioneIntermediarioPA();
		String codDominio = header.getIdentificativoDominio();
		String iuv = header.getIdentificativoUnivocoVersamento();
		String ccp = header.getCodiceContestoPagamento();
		String psp = bodyrichiesta.getIdentificativoPSP();
		
		IContext ctx = ContextThreadLocal.get();
		GpContext appContext = (GpContext) ctx.getApplicationContext();
		appContext.setCorrelationId(codDominio + iuv + ccp);
		
		appContext.getEventoCtx().setCodDominio(codDominio);
		appContext.getEventoCtx().setIuv(iuv);
		appContext.getEventoCtx().setCcp(ccp);

		Actor from = new Actor();
		from.setName(GpContext.NodoDeiPagamentiSPC);
		from.setType(GpContext.TIPO_SOGGETTO_NDP);
		appContext.getTransaction().setFrom(from);

		Actor to = new Actor();
		to.setName(header.getIdentificativoStazioneIntermediarioPA());
		from.setType(GpContext.TIPO_SOGGETTO_STAZIONE);
		appContext.getTransaction().setTo(to);

		appContext.getRequest().addGenericProperty(new Property("ccp", ccp));
		appContext.getRequest().addGenericProperty(new Property("codDominio", codDominio));
		appContext.getRequest().addGenericProperty(new Property("iuv", iuv));
		appContext.getRequest().addGenericProperty(new Property("codPsp", psp));
		try {
			ctx.getApplicationLogger().log("ccp.ricezioneVerifica");
		} catch (UtilsException e) {
			log.error("Errore durante il log dell'operazione: " + e.getMessage(),e);
		}


		log.info("Ricevuta richiesta di verifica RPT [" + codIntermediario + "][" + codStazione + "][" + codDominio + "][" + iuv + "][" + ccp + "]");
		PaaVerificaRPTRisposta response = new PaaVerificaRPTRisposta();

		DatiPagoPA datiPagoPA = new DatiPagoPA();
		datiPagoPA.setCodStazione(codStazione);
		datiPagoPA.setFruitore(GpContext.NodoDeiPagamentiSPC);
		appContext.getEventoCtx().setDatiPagoPA(datiPagoPA);
		datiPagoPA.setCodDominio(codDominio);
//		appContext.getEventoCtx().setTipoEvento(TipoEventoCooperazione.paaVerificaRPT.name());
		datiPagoPA.setCodPsp(psp);
		datiPagoPA.setTipoVersamento(TipoVersamento.ATTIVATO_PRESSO_PSP);
		datiPagoPA.setModelloPagamento(ModelloPagamento.ATTIVATO_PRESSO_PSP);
		datiPagoPA.setErogatore(codIntermediario);
		datiPagoPA.setCodIntermediario(codIntermediario);
		
		try {
			BDConfigWrapper configWrapper = new BDConfigWrapper(ContextThreadLocal.get().getTransactionId(), true);
			log.debug("Autenticazione richiesta.");

			Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
			appContext.getEventoCtx().setPrincipal(AutorizzazioneUtils.getPrincipal(authentication));
			if(GovpayConfig.getInstance().isPddAuthEnable() && authentication == null) {
				ctx.getApplicationLogger().log("ccp.erroreNoAutorizzazione");
				throw new NotAuthorizedException("Autorizzazione fallita: principal non fornito");
			}
			
			log.debug("Richiesta autenticata.");
			
			log.debug("Verifica intermediario.");
			Intermediario intermediario = null;
			try {
				intermediario = AnagraficaManager.getIntermediario(configWrapper, codIntermediario);

				// Controllo autorizzazione
				if(GovpayConfig.getInstance().isPddAuthEnable()){
					boolean authOk = AuthorizationManager.checkPrincipal(authentication, intermediario.getPrincipal()); 
					
					if(!authOk) {
						GovpayLdapUserDetails details = AutorizzazioneUtils.getAuthenticationDetails(authentication);
						String principal = details.getIdentificativo(); 
						ctx.getApplicationLogger().log("ccp.erroreAutorizzazione", principal);
						throw new NotAuthorizedException("Autorizzazione fallita: principal fornito (" + principal + ") non valido per l'intermediario (" + codIntermediario + ").");
					}
				}
			} catch (NotFoundException e) {
				throw new NdpException(FaultPa.PAA_ID_INTERMEDIARIO_ERRATO, codDominio);
			}
			log.debug("Intermediario verificato: " + intermediario.getCodIntermediario());

			try {
				AnagraficaManager.getStazione(configWrapper, codStazione);
			} catch (NotFoundException e) {
				throw new NdpException(FaultPa.PAA_STAZIONE_INT_ERRATA, codDominio);
			}

			log.debug("Verifica dominio.");
			Dominio dominio;
			try {
				dominio = AnagraficaManager.getDominio(configWrapper, codDominio);
			} catch (NotFoundException e) {
				throw new NdpException(FaultPa.PAA_ID_DOMINIO_ERRATO, codDominio);
			}
			log.debug("Dominio verificato: " + dominio.getCodDominio());

			log.debug("Verifica versamento.");
			VersamentiBD versamentiBD = new VersamentiBD(configWrapper);
			Versamento versamento = null;
			it.govpay.bd.model.Applicazione applicazioneGestisceIuv = null;
			try {
				versamento = versamentiBD.getVersamentoByDominioIuv(dominio.getId(), iuv, true);
				appContext.getEventoCtx().setIdA2A(versamento.getApplicazione(configWrapper).getCodApplicazione());
				appContext.getEventoCtx().setIdPendenza(versamento.getCodVersamentoEnte());
				ctx.getApplicationLogger().log("ccp.iuvPresente", versamento.getCodVersamentoEnte());
			}catch (NotFoundException e) {
				applicazioneGestisceIuv = new Applicazione().getApplicazioneDominio(configWrapper, dominio,iuv,false); 
				
				if(applicazioneGestisceIuv == null) {
					ctx.getApplicationLogger().log("ccp.iuvNonPresenteNoAppGestireIuv");
					throw new NdpException(FaultPa.PAA_PAGAMENTO_SCONOSCIUTO, codDominio);
				}
				appContext.getEventoCtx().setIdA2A(applicazioneGestisceIuv.getCodApplicazione());
				ctx.getApplicationLogger().log("ccp.iuvNonPresente", applicazioneGestisceIuv.getCodApplicazione());
			}
			
			try {
				try {
					// Se non ho lo iuv, vado direttamente a chiedere all'applicazione di default
					if(versamento == null) throw new NotFoundException();
					
					log.debug("Versamento acquisito");

					// Versamento trovato, gestisco un'eventuale scadenza
					versamento = VersamentoUtils.aggiornaVersamento(versamento, log);
					log.debug("Verifica stato versamento dopo procedura di aggiornamento: " + versamento.getStatoVersamento());
					if(versamento.getStatoVersamento().equals(StatoVersamento.ANNULLATO))
						throw new NdpException(FaultPa.PAA_PAGAMENTO_ANNULLATO, codDominio);

					if(!versamento.getStatoVersamento().equals(StatoVersamento.NON_ESEGUITO)) {
						
						if(versamento.getStatoVersamento().equals(StatoVersamento.ESEGUITO) || versamento.getStatoVersamento().equals(StatoVersamento.ESEGUITO_ALTRO_CANALE)) {
							PagamentiBD pagamentiBD = new PagamentiBD(configWrapper);
							List<Pagamento> pagamenti = pagamentiBD.getPagamentiBySingoloVersamento(versamento.getSingoliVersamenti().get(0).getId());
							if(pagamenti.isEmpty())
								throw new NdpException(FaultPa.PAA_PAGAMENTO_DUPLICATO, codDominio);
							else {
								Pagamento pagamento = pagamenti.get(0);
								throw new NdpException(FaultPa.PAA_PAGAMENTO_DUPLICATO, "Il pagamento risulta gi\u00E0 effettuato in data " + sdf.format(pagamento.getDataPagamento()) + " [Iur:" + pagamento.getIur() + "]", codDominio);
							}
								
						}
					}
				} catch (NotFoundException e) {
					log.debug("Versamento non presente in base dati");
					//Versamento non trovato, devo interrogare l'applicazione.
					// prendo tutte le applicazioni che gestiscono il dominio, tra queste cerco la prima che match la regexpr dello iuv la utilizzo per far acquisire il versamento
					if(applicazioneGestisceIuv == null) {
						applicazioneGestisceIuv = new Applicazione().getApplicazioneDominio(configWrapper, dominio,iuv); 
						appContext.getEventoCtx().setIdA2A(applicazioneGestisceIuv.getCodApplicazione());
					}
					
					// Versamento non trovato, devo interrogare l'applicazione.
					ctx.getApplicationLogger().log("ccp.versamentoIuvNonPresente", applicazioneGestisceIuv.getCodApplicazione(), dominio.getCodDominio(), iuv);

					log.debug("Versamento non presente in base dati. Procedo all'acquisizione [CodApplicazione:" + applicazioneGestisceIuv.getCodApplicazione()+"]");
					versamento = VersamentoUtils.acquisisciVersamento(applicazioneGestisceIuv, null, null, null, codDominio, iuv,  TipologiaTipoVersamento.DOVUTO, log);
					
					appContext.getEventoCtx().setIdA2A(versamento.getApplicazione(configWrapper).getCodApplicazione());
					appContext.getEventoCtx().setIdPendenza(versamento.getCodVersamentoEnte());
					log.debug("Versamento acquisito da Ente. Aggiorno per verifica scadenza [CodApplicazione:" + applicazioneGestisceIuv.getCodApplicazione()+"]");
					// Versamento trovato, gestisco un'eventuale scadenza
					versamento = VersamentoUtils.aggiornaVersamento(versamento, log);
					log.debug("Versamento aggiornato da Ente. Verifica stato " + versamento.getStatoVersamento());
					
					if(versamento.getStatoVersamento().equals(StatoVersamento.ANNULLATO))
						throw new NdpException(FaultPa.PAA_PAGAMENTO_ANNULLATO, codDominio);

					if(!versamento.getStatoVersamento().equals(StatoVersamento.NON_ESEGUITO)) {
						
						if(versamento.getStatoVersamento().equals(StatoVersamento.ESEGUITO) || versamento.getStatoVersamento().equals(StatoVersamento.ESEGUITO_ALTRO_CANALE)) {
							PagamentiBD pagamentiBD = new PagamentiBD(configWrapper);
							List<Pagamento> pagamenti = pagamentiBD.getPagamentiBySingoloVersamento(versamento.getSingoliVersamenti().get(0).getId());
							if(pagamenti.isEmpty())
								throw new NdpException(FaultPa.PAA_PAGAMENTO_DUPLICATO, codDominio);
							else {
								Pagamento pagamento = pagamenti.get(0);
								throw new NdpException(FaultPa.PAA_PAGAMENTO_DUPLICATO, "Il pagamento risulta gi\u00E0 effettuato in data " + sdf.format(pagamento.getDataPagamento()) + " [Iur:" + pagamento.getIur() + "]", codDominio);
							}
								
						}
					}
					log.debug("Versamento pagabile.");
					ctx.getApplicationLogger().log("ccp.versamentoIuvNonPresenteOk",applicazioneGestisceIuv.getCodApplicazione(), dominio.getCodDominio(), iuv);
				}
			} catch (VersamentoScadutoException e1) {
				appContext.getEventoCtx().setIdA2A(e1.getCodApplicazione());
				appContext.getEventoCtx().setIdPendenza(e1.getCodVersamentoEnte());
				throw new NdpException(FaultPa.PAA_PAGAMENTO_SCADUTO, e1.getMessage(), codDominio, e1);
			} catch (VersamentoAnnullatoException e1) {
				appContext.getEventoCtx().setIdA2A(e1.getCodApplicazione());
				appContext.getEventoCtx().setIdPendenza(e1.getCodVersamentoEnte());
				throw new NdpException(FaultPa.PAA_PAGAMENTO_ANNULLATO, e1.getMessage(), codDominio, e1);
			} catch (VersamentoDuplicatoException e1) {
				appContext.getEventoCtx().setIdA2A(e1.getCodApplicazione());
				appContext.getEventoCtx().setIdPendenza(e1.getCodVersamentoEnte());
				throw new NdpException(FaultPa.PAA_PAGAMENTO_DUPLICATO, e1.getMessage(), codDominio, e1);
			} catch (VersamentoSconosciutoException e1) {
				appContext.getEventoCtx().setIdA2A(e1.getCodApplicazione());
				appContext.getEventoCtx().setIdPendenza(e1.getCodVersamentoEnte());
				throw new NdpException(FaultPa.PAA_PAGAMENTO_SCONOSCIUTO, e1.getMessage(), codDominio, e1);
			} catch (VersamentoNonValidoException e1) {
				appContext.getEventoCtx().setIdA2A(e1.getCodApplicazione());
				appContext.getEventoCtx().setIdPendenza(e1.getCodVersamentoEnte());
				throw new NdpException(FaultPa.PAA_SYSTEM_ERROR, e1.getMessage(), codDominio, e1);
			} catch (ClientException e1) {
				throw new NdpException(FaultPa.PAA_SYSTEM_ERROR, "Riscontrato errore durante l'acquisizione del versamento dall'applicazione gestore del debito: " + e1, codDominio, e1);
			} catch (GovPayException e1) {
				throw new NdpException(FaultPa.PAA_SYSTEM_ERROR, "Riscontrato errore durante la verifica del versamento: " + e1, codDominio, e1);
			}
			
			log.debug("Verifica pagamenti in corso.");
			RptBD rptBD = new RptBD(configWrapper);
			if(GovpayConfig.getInstance().isTimeoutPendentiModello3()) {
				// Controllo che non ci sia un pagamento in corso
				// Prendo tutte le RPT pendenti
				RptFilter filter = rptBD.newFilter();
				filter.setStato(Rpt.getStatiPendenti());
				filter.setIdVersamento(versamento.getId());
				List<Rpt> rpt_pendenti = rptBD.findAll(filter);
				
				// Per tutte quelle in corso controllo se hanno passato la soglia di timeout
				// Altrimenti lancio il fault
				Date dataSoglia = new Date(new Date().getTime() - GovpayConfig.getInstance().getTimeoutPendentiModello3Mins() * 60000);
				
				for(Rpt rpt_pendente : rpt_pendenti) {
					if(rpt_pendente.getPagamentoPortale() != null)
						appContext.getEventoCtx().setIdPagamento(rpt_pendente.getPagamentoPortale().getIdSessione());
					Date dataMsgRichiesta = rpt_pendente.getDataMsgRichiesta();
					// se l'RPT e' bloccata allora controllo che il blocco sia indefinito oppure definito, altrimenti passo
					if(rpt_pendente.isBloccante() && (GovpayConfig.getInstance().getTimeoutPendentiModello3Mins() == 0 || dataSoglia.before(dataMsgRichiesta))) {
						throw new NdpException(FaultPa.PAA_PAGAMENTO_IN_CORSO, "Pagamento in corso [CCP:" + rpt_pendente.getCcp() + "].", codDominio);
					}
				}
			}
			log.debug("Verifica pagamenti in corso completata.");
			// Verifico che abbia un solo singolo versamento
			if(versamento.getSingoliVersamenti().size() != 1) {
				throw new NdpException(FaultPa.PAA_SEMANTICA, "Il versamento contiene piu' di un singolo versamento, non ammesso per pagamenti ad iniziativa psp.", codDominio);
			}

			EsitoVerificaRPT esito = new EsitoVerificaRPT();
			esito.setEsito("OK");
			PaaTipoDatiPagamentoPA datiPagamento =  new PaaTipoDatiPagamentoPA();
			datiPagamento.setImportoSingoloVersamento(versamento.getImportoTotale());

			if(versamento.getCausaleVersamento() != null) {
				if(versamento.getCausaleVersamento() instanceof CausaleSemplice) {
					datiPagamento.setCausaleVersamento(((CausaleSemplice) versamento.getCausaleVersamento()).getCausale());
				}

				if(versamento.getCausaleVersamento() instanceof CausaleSpezzoni) {
					datiPagamento.setSpezzoniCausaleVersamento(new CtSpezzoniCausaleVersamento());
					datiPagamento.getSpezzoniCausaleVersamento().getSpezzoneCausaleVersamentoOrSpezzoneStrutturatoCausaleVersamento().addAll(((CausaleSpezzoni) versamento.getCausaleVersamento()).getSpezzoni());
				}

				if(versamento.getCausaleVersamento() instanceof CausaleSpezzoniStrutturati) {
					datiPagamento.setSpezzoniCausaleVersamento(new CtSpezzoniCausaleVersamento());
					CausaleSpezzoniStrutturati causale = (CausaleSpezzoniStrutturati) versamento.getCausaleVersamento();
					for(int i=0; i < causale.getSpezzoni().size(); i++) {
						CtSpezzoneStrutturatoCausaleVersamento spezzone = new CtSpezzoneStrutturatoCausaleVersamento();
						spezzone.setCausaleSpezzone(causale.getSpezzoni().get(i));
						spezzone.setImportoSpezzone(causale.getImporti().get(i));
						datiPagamento.getSpezzoniCausaleVersamento().getSpezzoneCausaleVersamentoOrSpezzoneStrutturatoCausaleVersamento().add(spezzone);
					}
				}
			} else {
				datiPagamento.setCausaleVersamento(" ");
			}

			datiPagamento.setEnteBeneficiario(RptUtils.buildEnteBeneficiario(dominio, versamento.getUo(configWrapper)));
			SingoloVersamento singoloVersamento = versamento.getSingoliVersamenti().get(0);
			
			IbanAccredito ibanAccredito = singoloVersamento.getIbanAccredito(configWrapper);
			
			if(ibanAccredito != null) {
				datiPagamento.setBicAccredito(ibanAccredito.getCodBic());
				datiPagamento.setIbanAccredito(ibanAccredito.getCodIban());
			}
			esito.setDatiPagamentoPA(datiPagamento);
			response.setPaaVerificaRPTRisposta(esito);
			ctx.getApplicationLogger().log("ccp.ricezioneVerificaOk", datiPagamento.getImportoSingoloVersamento().toString(), datiPagamento.getIbanAccredito(), versamento.getCausaleVersamento() != null ? versamento.getCausaleVersamento().toString() : "[-- Nessuna causale --]");
			appContext.getEventoCtx().setEsito(Esito.OK);
			log.debug("Verifica RPT completata con successo");
		} catch (NdpException e) {
//			if(bd != null) bd.rollback();
			response = this.buildRisposta(e, response);
			String faultDescription = response.getPaaVerificaRPTRisposta().getFault().getDescription() == null ? "<Nessuna descrizione>" : response.getPaaVerificaRPTRisposta().getFault().getDescription(); 
			try {
				ctx.getApplicationLogger().log("ccp.ricezioneVerificaKo", response.getPaaVerificaRPTRisposta().getFault().getFaultCode(), response.getPaaVerificaRPTRisposta().getFault().getFaultString(), faultDescription);
			} catch (UtilsException e1) {
				log.error("Errore durante il log dell'operazione: " + e1.getMessage(),e1);
			}
			appContext.getEventoCtx().setDescrizioneEsito(faultDescription);
			appContext.getEventoCtx().setSottotipoEsito(e.getFaultCode());
			if(e.getFaultCode().equals(FaultPa.PAA_SYSTEM_ERROR.name()))
				appContext.getEventoCtx().setEsito(Esito.FAIL);
			else 
				appContext.getEventoCtx().setEsito(Esito.KO);
			log.debug("Verifica RPT completata con esito " + appContext.getEventoCtx().getEsito());			
		} catch (Exception e) {
//			if(bd != null) bd.rollback();
			response = this.buildRisposta(e, codDominio, response);
			String faultDescription = response.getPaaVerificaRPTRisposta().getFault().getDescription() == null ? "<Nessuna descrizione>" : response.getPaaVerificaRPTRisposta().getFault().getDescription(); 
			try {
				ctx.getApplicationLogger().log("ccp.ricezioneVerificaKo", response.getPaaVerificaRPTRisposta().getFault().getFaultCode(), response.getPaaVerificaRPTRisposta().getFault().getFaultString(), faultDescription);
			} catch (UtilsException e1) {
				log.error("Errore durante il log dell'operazione: " + e1.getMessage(),e1);
			}
			appContext.getEventoCtx().setDescrizioneEsito(faultDescription);
			appContext.getEventoCtx().setSottotipoEsito(response.getPaaVerificaRPTRisposta().getFault().getFaultCode());
			appContext.getEventoCtx().setEsito(Esito.FAIL);
		} finally {
			GpContext.setResult(appContext.getTransaction(), response.getPaaVerificaRPTRisposta().getFault() == null ? null : response.getPaaVerificaRPTRisposta().getFault().getFaultCode());
		}
		return response;
	}
	
	@Override
	public PaSendRTRes paSendRT(PaSendRTReq requestBody) {
		
		String codIntermediario = requestBody.getIdBrokerPA();
		String codStazione = requestBody.getIdStation();
//		String idDominio = requestBody.getIdPA();
		
		CtReceipt receipt = requestBody.getReceipt();
		
		String codDominio = receipt.getFiscalCode();
		String iuv = receipt.getCreditorReferenceId();
		String ccp = receipt.getReceiptId();

		IContext ctx = ContextThreadLocal.get();
		GpContext appContext = (GpContext) ctx.getApplicationContext();
		
		appContext.setCorrelationId(codDominio + iuv + ccp);

		Actor from = new Actor();
		from.setName(GpContext.NodoDeiPagamentiSPC);
		from.setType(GpContext.TIPO_SOGGETTO_NDP);
		appContext.getTransaction().setFrom(from);

		Actor to = new Actor();
		to.setName(codStazione);
		from.setType(GpContext.TIPO_SOGGETTO_STAZIONE);
		appContext.getTransaction().setTo(to);

		appContext.getRequest().addGenericProperty(new Property("codDominio", codDominio));
		appContext.getRequest().addGenericProperty(new Property("iuv", iuv));
		appContext.getRequest().addGenericProperty(new Property("ccp", ccp));

		appContext.getEventoCtx().setCodDominio(codDominio);
		appContext.getEventoCtx().setIuv(iuv);
		appContext.getEventoCtx().setCcp(receipt.getReceiptId());

		try {
			ctx.getApplicationLogger().log("pagamento.ricezioneRt");
		} catch (UtilsException e) {
			log.error("Errore durante il log dell'operazione: " + e.getMessage(),e);
		}

		log.info("Ricevuta richiesta paSendRT [" + codDominio + "][" + iuv + "][" + ccp + "]");
		PaSendRTRes response = new PaSendRTRes();

		DatiPagoPA datiPagoPA = new DatiPagoPA();
		datiPagoPA.setCodStazione(codStazione);
		datiPagoPA.setFruitore(GpContext.NodoDeiPagamentiSPC);
		datiPagoPA.setCodDominio(codDominio);
		datiPagoPA.setErogatore(codIntermediario);
		datiPagoPA.setCodIntermediario(codIntermediario);
		appContext.getEventoCtx().setDatiPagoPA(datiPagoPA);

		try {
			BDConfigWrapper configWrapper = new BDConfigWrapper(ContextThreadLocal.get().getTransactionId(), true);

			Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
			appContext.getEventoCtx().setPrincipal(AutorizzazioneUtils.getPrincipal(authentication));
			if(GovpayConfig.getInstance().isPddAuthEnable() && authentication == null) {
				ctx.getApplicationLogger().log("rt.erroreNoAutorizzazione");
				throw new NotAuthorizedException("Autorizzazione fallita: principal non fornito");
			}

			Intermediario intermediario = null;
			try {
				intermediario = AnagraficaManager.getIntermediario(configWrapper, codIntermediario);

				// Controllo autorizzazione
				if(GovpayConfig.getInstance().isPddAuthEnable()){
					boolean authOk = AuthorizationManager.checkPrincipal(authentication, intermediario.getPrincipal()); 

					if(!authOk) {
						GovpayLdapUserDetails details = AutorizzazioneUtils.getAuthenticationDetails(authentication);
						String principal = details.getIdentificativo(); 
						ctx.getApplicationLogger().log("rt.erroreAutorizzazione", principal);
						throw new NotAuthorizedException("Autorizzazione fallita: principal fornito (" + principal + ") non valido per l'intermediario (" + codIntermediario + ").");
					}
				}
			} catch (NotFoundException e) {
				throw new NdpException(FaultPa.PAA_ID_INTERMEDIARIO_ERRATO, codDominio);
			}

			Dominio dominio = null;
			try {
				dominio = AnagraficaManager.getDominio(configWrapper, codDominio);
			} catch (NotFoundException e) {
				throw new NdpException(FaultPa.PAA_ID_DOMINIO_ERRATO, codDominio);
			}

			Stazione stazione = null;
			try {
				stazione = AnagraficaManager.getStazione(configWrapper, codStazione);
			} catch (NotFoundException e) {
				throw new NdpException(FaultPa.PAA_STAZIONE_INT_ERRATA, codDominio);
			}

			if(stazione.getIdIntermediario() != intermediario.getId()) {
				throw new NdpException(FaultPa.PAA_ID_INTERMEDIARIO_ERRATO, codDominio);
			}

			if(dominio.getIdStazione().compareTo(stazione.getId())!=0) {
				throw new NdpException(FaultPa.PAA_STAZIONE_INT_ERRATA, codDominio);
			}

			Rpt rpt = CtReceiptUtils.acquisisciRT(codDominio, iuv, requestBody, false);

			appContext.getEventoCtx().setIdA2A(rpt.getVersamento(configWrapper).getApplicazione(configWrapper).getCodApplicazione());
			appContext.getEventoCtx().setIdPendenza(rpt.getVersamento(configWrapper).getCodVersamentoEnte());
			if(rpt.getIdPagamentoPortale() != null)
				appContext.getEventoCtx().setIdPagamento(rpt.getPagamentoPortale(configWrapper).getIdSessione());

			appContext.getResponse().addGenericProperty(new Property("esitoPagamento", rpt.getEsitoPagamento().toString()));
			ctx.getApplicationLogger().log("pagamento.acquisizioneRtOk");

			datiPagoPA.setCodCanale(rpt.getCodCanale());
			datiPagoPA.setTipoVersamento(rpt.getTipoVersamento());

			response.setOutcome(StOutcome.OK);
			ctx.getApplicationLogger().log("rt.ricezioneOk");
			appContext.getEventoCtx().setDescrizioneEsito("Acquisita ricevuta di pagamento [IUV: " + rpt.getIuv() + " CCP:" + rpt.getCcp() + "] emessa da " + rpt.getDenominazioneAttestante());
			appContext.getEventoCtx().setEsito(Esito.OK);
		} catch (NdpException e) {
			response = this.buildRisposta(e, response);
			String faultDescription = response.getFault().getDescription() == null ? "<Nessuna descrizione>" : response.getFault().getDescription(); 
			try {
				ctx.getApplicationLogger().log("rt.ricezioneKo", response.getFault().getFaultCode(), response.getFault().getFaultString(), faultDescription);
			} catch (UtilsException e1) {
				log.error("Errore durante il log dell'operazione: " + e1.getMessage(),e1);
			}
			if(e.getFaultCode().equals(FaultPa.PAA_SYSTEM_ERROR.name()))
				appContext.getEventoCtx().setEsito(Esito.FAIL);
			else 
				appContext.getEventoCtx().setEsito(Esito.KO);
			appContext.getEventoCtx().setDescrizioneEsito(faultDescription);
			appContext.getEventoCtx().setSottotipoEsito(e.getFaultCode());
		} catch (Exception e) {
			response = this.buildRisposta(new NdpException(FaultPa.PAA_SYSTEM_ERROR, codDominio, e.getMessage(), e), response);
			String faultDescription = response.getFault().getDescription() == null ? "<Nessuna descrizione>" : response.getFault().getDescription(); 
			try {
				ctx.getApplicationLogger().log("rt.ricezioneKo", response.getFault().getFaultCode(), response.getFault().getFaultString(), faultDescription);
			} catch (UtilsException e1) {
				log.error("Errore durante il log dell'operazione: " + e1.getMessage(),e1);
			}
			appContext.getEventoCtx().setSottotipoEsito(response.getFault().getFaultCode());
			appContext.getEventoCtx().setEsito(Esito.FAIL);
			appContext.getEventoCtx().setDescrizioneEsito(faultDescription);
		} finally {
			GpContext.setResult(appContext.getTransaction(), response.getFault() == null ? null : response.getFault().getFaultCode());
		}
		
		return response;
	}

	/*
	 Da https://github.com/pagopa/pagopa-api/issues/101 Analisi del contenuto della risposta alla primitiva paVerifyPaymentNotice
	  
	Dalle nuove SANP 2.4-RC https://pagopa-specifichepagamenti-docs.readthedocs.io/en/master 
	
	una ctPaymentOptionDescriptionPA non rappresenta una voce di pagamento, ma un intero pagamento.
	
	L'equivoco probabilmente deriva dalla possibilità di specificare fino a 5 opzioni.
	
	In realtà sono una nuova feature che parte dalla considerazione che non sempre una posizione debitoria 
	definisce un singolo pagamento o un importo univocamente determinato 
	e le opzioni di pagamento servono a gestire la possibilità di far scegliere all'utente che si è recato da un PSP 
	l'opzione di pagamento corretta in base al contesto corrente.
	
	Gestisce situazioni come:

	la possibilità di scegliere se pagare in un'unica soluzione o a rate
	il pagamento di una multa in funzione del numero di giorni trascorsi dalla data di notifica (o la mancanza stessa di una data di notifica nota al sistema)
	la possibilità di pagare acconti
	Quindi l'importo e la data di scadenza sono relativi all'intero pagamento che a sua volta potrà essere suddiviso in più parti,
	 ma in questa fase non viene fornita evidenza di come è strutturato il pagamento, 
	 ma consente di scegliere il pagamento adatto in base a dati di contesto, 
	 non necessariamente completamente noti a pagoPA
	
	*/
	@Override
	public PaVerifyPaymentNoticeRes paVerifyPaymentNotice(PaVerifyPaymentNoticeReq requestBody) {
		SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
		String codIntermediario = requestBody.getIdBrokerPA();
		String codStazione = requestBody.getIdStation();
		//String idDominio = requestBody.getIdPA();
		
		CtQrCode qrCode = requestBody.getQrCode();
		String numeroAvviso = qrCode.getNoticeNumber();
		String codDominio = qrCode.getFiscalCode();
		
		IContext ctx = ContextThreadLocal.get();
		GpContext appContext = (GpContext) ctx.getApplicationContext();
		
		PaVerifyPaymentNoticeRes response = new PaVerifyPaymentNoticeRes();
		
		try {
			BDConfigWrapper configWrapper = new BDConfigWrapper(ContextThreadLocal.get().getTransactionId(), true);
			String iuv = IuvUtils.toIuv(numeroAvviso);
			
			appContext.setCorrelationId(codDominio + numeroAvviso);
			
			appContext.getEventoCtx().setCodDominio(codDominio);
			appContext.getEventoCtx().setIuv(iuv);
			
			Actor from = new Actor();
			from.setName(GpContext.NodoDeiPagamentiSPC);
			from.setType(GpContext.TIPO_SOGGETTO_NDP);
			appContext.getTransaction().setFrom(from);

			Actor to = new Actor();
			to.setName(codStazione);
			from.setType(GpContext.TIPO_SOGGETTO_STAZIONE);
			appContext.getTransaction().setTo(to);

			appContext.getRequest().addGenericProperty(new Property("codDominio", codDominio));
			appContext.getRequest().addGenericProperty(new Property("iuv", iuv));
			try {
				ctx.getApplicationLogger().log("ccp.ricezioneVerifica");
			} catch (UtilsException e) {
				log.error("Errore durante il log dell'operazione: " + e.getMessage(),e);
			}
		
			log.info("Ricevuta richiesta paVerifyPaymentNotice [" + codIntermediario + "][" + codStazione + "][" + codDominio + "][" + numeroAvviso + "]");

			DatiPagoPA datiPagoPA = new DatiPagoPA();
			datiPagoPA.setCodStazione(codStazione);
			datiPagoPA.setFruitore(GpContext.NodoDeiPagamentiSPC);
			appContext.getEventoCtx().setDatiPagoPA(datiPagoPA);
			datiPagoPA.setCodDominio(codDominio);
			appContext.getEventoCtx().setTipoEvento(TipoEventoCooperazione.PAVERIFYPAYMENTNOTICE.toString());
			datiPagoPA.setTipoVersamento(TipoVersamento.ATTIVATO_PRESSO_PSP);
			datiPagoPA.setModelloPagamento(ModelloPagamento.ATTIVATO_PRESSO_PSP);
			datiPagoPA.setErogatore(codIntermediario);
			datiPagoPA.setCodIntermediario(codIntermediario);
			
			Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
			appContext.getEventoCtx().setPrincipal(AutorizzazioneUtils.getPrincipal(authentication));
			if(GovpayConfig.getInstance().isPddAuthEnable() && authentication == null) {
				ctx.getApplicationLogger().log("ccp.erroreNoAutorizzazione");
				throw new NotAuthorizedException("Autorizzazione fallita: principal non fornito");
			}
			
			log.debug("Richiesta autenticata.");
			
			log.debug("Verifica intermediario.");
			Intermediario intermediario = null;
			try {
				intermediario = AnagraficaManager.getIntermediario(configWrapper, codIntermediario);

				// Controllo autorizzazione
				if(GovpayConfig.getInstance().isPddAuthEnable()){
					boolean authOk = AuthorizationManager.checkPrincipal(authentication, intermediario.getPrincipal()); 
					
					if(!authOk) {
						GovpayLdapUserDetails details = AutorizzazioneUtils.getAuthenticationDetails(authentication);
						String principal = details.getIdentificativo(); 
						ctx.getApplicationLogger().log("ccp.erroreAutorizzazione", principal);
						throw new NotAuthorizedException("Autorizzazione fallita: principal fornito (" + principal + ") non valido per l'intermediario (" + codIntermediario + ").");
					}
				}
			} catch (NotFoundException e) {
				throw new NdpException(FaultPa.PAA_ID_INTERMEDIARIO_ERRATO, codDominio);
			}
			log.debug("Intermediario verificato: " + intermediario.getCodIntermediario());

			try {
				AnagraficaManager.getStazione(configWrapper, codStazione);
			} catch (NotFoundException e) {
				throw new NdpException(FaultPa.PAA_STAZIONE_INT_ERRATA, codDominio);
			}

			log.debug("Verifica dominio.");
			Dominio dominio;
			try {
				dominio = AnagraficaManager.getDominio(configWrapper, codDominio);
			} catch (NotFoundException e) {
				throw new NdpException(FaultPa.PAA_ID_DOMINIO_ERRATO, codDominio);
			}
			log.debug("Dominio verificato: " + dominio.getCodDominio());

			log.debug("Verifica versamento.");
			VersamentiBD versamentiBD = new VersamentiBD(configWrapper);
			Versamento versamento = null;
			it.govpay.bd.model.Applicazione applicazioneGestisceIuv = null;
			try {
				versamento = versamentiBD.getVersamentoByDominioIuv(dominio.getId(), iuv, true);
				appContext.getEventoCtx().setIdA2A(versamento.getApplicazione(configWrapper).getCodApplicazione());
				appContext.getEventoCtx().setIdPendenza(versamento.getCodVersamentoEnte());
				ctx.getApplicationLogger().log("ccp.iuvPresente", versamento.getCodVersamentoEnte());
			}catch (NotFoundException e) {
				applicazioneGestisceIuv = new Applicazione().getApplicazioneDominio(configWrapper, dominio,iuv,false); 
				
				if(applicazioneGestisceIuv == null) {
					ctx.getApplicationLogger().log("ccp.iuvNonPresenteNoAppGestireIuv");
					throw new NdpException(FaultPa.PAA_PAGAMENTO_SCONOSCIUTO, codDominio);
				}
				appContext.getEventoCtx().setIdA2A(applicazioneGestisceIuv.getCodApplicazione());
				ctx.getApplicationLogger().log("ccp.iuvNonPresente", applicazioneGestisceIuv.getCodApplicazione());
			}
		
			try {
				try {
					// Se non ho lo iuv, vado direttamente a chiedere all'applicazione di default
					if(versamento == null) throw new NotFoundException();
					
					log.debug("Versamento acquisito]");

					// Versamento trovato, gestisco un'eventuale scadenza
					versamento = VersamentoUtils.aggiornaVersamento(versamento, log);
					log.debug("Verifica stato versamento dopo procedura di aggiornamento: " + versamento.getStatoVersamento());
					if(versamento.getStatoVersamento().equals(StatoVersamento.ANNULLATO))
						throw new NdpException(FaultPa.PAA_PAGAMENTO_ANNULLATO, codDominio);

					if(!versamento.getStatoVersamento().equals(StatoVersamento.NON_ESEGUITO)) {
						
						if(versamento.getStatoVersamento().equals(StatoVersamento.ESEGUITO) || versamento.getStatoVersamento().equals(StatoVersamento.ESEGUITO_ALTRO_CANALE)) {
							PagamentiBD pagamentiBD = new PagamentiBD(configWrapper);
							List<Pagamento> pagamenti = pagamentiBD.getPagamentiBySingoloVersamento(versamento.getSingoliVersamenti().get(0).getId());
							if(pagamenti.isEmpty())
								throw new NdpException(FaultPa.PAA_PAGAMENTO_DUPLICATO, codDominio);
							else {
								Pagamento pagamento = pagamenti.get(0);
								throw new NdpException(FaultPa.PAA_PAGAMENTO_DUPLICATO, "Il pagamento risulta gi\u00E0 effettuato in data " + sdf.format(pagamento.getDataPagamento()) + " [Iur:" + pagamento.getIur() + "]", codDominio);
							}
								
						}
					}

				} catch (NotFoundException e) {
					log.debug("Versamento non presente in base dati");
					//Versamento non trovato, devo interrogare l'applicazione.
					// prendo tutte le applicazioni che gestiscono il dominio, tra queste cerco la prima che match la regexpr dello iuv la utilizzo per far acquisire il versamento
					if(applicazioneGestisceIuv == null) {
						applicazioneGestisceIuv = new Applicazione().getApplicazioneDominio(configWrapper, dominio,iuv); 
						appContext.getEventoCtx().setIdA2A(applicazioneGestisceIuv.getCodApplicazione());
					}
					
					// Versamento non trovato, devo interrogare l'applicazione.
					ctx.getApplicationLogger().log("ccp.versamentoIuvNonPresente", applicazioneGestisceIuv.getCodApplicazione(), dominio.getCodDominio(), iuv);
					log.debug("Versamento non presente in base dati. Procedo all'acquisizione [CodApplicazione:" + applicazioneGestisceIuv.getCodApplicazione()+"]");
					versamento = VersamentoUtils.acquisisciVersamento(applicazioneGestisceIuv, null, null, null, codDominio, iuv,  TipologiaTipoVersamento.DOVUTO, log);
					
					appContext.getEventoCtx().setIdA2A(versamento.getApplicazione(configWrapper).getCodApplicazione());
					appContext.getEventoCtx().setIdPendenza(versamento.getCodVersamentoEnte());
					log.debug("Versamento acquisito da Ente. Aggiorno per verifica scadenza [CodApplicazione:" + applicazioneGestisceIuv.getCodApplicazione()+"]");
					// Versamento trovato, gestisco un'eventuale scadenza
					versamento = VersamentoUtils.aggiornaVersamento(versamento, log);
					log.debug("Versamento aggiornato da Ente. Verifica stato " + versamento.getStatoVersamento());
					
					if(versamento.getStatoVersamento().equals(StatoVersamento.ANNULLATO))
						throw new NdpException(FaultPa.PAA_PAGAMENTO_ANNULLATO, codDominio);

					if(!versamento.getStatoVersamento().equals(StatoVersamento.NON_ESEGUITO)) {
						
						if(versamento.getStatoVersamento().equals(StatoVersamento.ESEGUITO) || versamento.getStatoVersamento().equals(StatoVersamento.ESEGUITO_ALTRO_CANALE)) {
							PagamentiBD pagamentiBD = new PagamentiBD(configWrapper);
							List<Pagamento> pagamenti = pagamentiBD.getPagamentiBySingoloVersamento(versamento.getSingoliVersamenti().get(0).getId());
							if(pagamenti.isEmpty())
								throw new NdpException(FaultPa.PAA_PAGAMENTO_DUPLICATO, codDominio);
							else {
								Pagamento pagamento = pagamenti.get(0);
								throw new NdpException(FaultPa.PAA_PAGAMENTO_DUPLICATO, "Il pagamento risulta gi\u00E0 effettuato in data " + sdf.format(pagamento.getDataPagamento()) + " [Iur:" + pagamento.getIur() + "]", codDominio);
							}
								
						}
					}
					log.debug("Versamento pagabile.");
					ctx.getApplicationLogger().log("ccp.versamentoIuvNonPresenteOk", applicazioneGestisceIuv.getCodApplicazione(), dominio.getCodDominio(), iuv);
				}
			} catch (VersamentoScadutoException e1) {
				appContext.getEventoCtx().setIdA2A(e1.getCodApplicazione());
				appContext.getEventoCtx().setIdPendenza(e1.getCodVersamentoEnte());
				throw new NdpException(FaultPa.PAA_PAGAMENTO_SCADUTO, e1.getMessage(), codDominio, e1);
			} catch (VersamentoAnnullatoException e1) {
				appContext.getEventoCtx().setIdA2A(e1.getCodApplicazione());
				appContext.getEventoCtx().setIdPendenza(e1.getCodVersamentoEnte());
				throw new NdpException(FaultPa.PAA_PAGAMENTO_ANNULLATO, e1.getMessage(), codDominio, e1);
			} catch (VersamentoDuplicatoException e1) {
				appContext.getEventoCtx().setIdA2A(e1.getCodApplicazione());
				appContext.getEventoCtx().setIdPendenza(e1.getCodVersamentoEnte());
				throw new NdpException(FaultPa.PAA_PAGAMENTO_DUPLICATO, e1.getMessage(), codDominio, e1);
			} catch (VersamentoSconosciutoException e1) {
				appContext.getEventoCtx().setIdA2A(e1.getCodApplicazione());
				appContext.getEventoCtx().setIdPendenza(e1.getCodVersamentoEnte());
				throw new NdpException(FaultPa.PAA_PAGAMENTO_SCONOSCIUTO, e1.getMessage(), codDominio, e1);
			} catch (VersamentoNonValidoException e1) {
				appContext.getEventoCtx().setIdA2A(e1.getCodApplicazione());
				appContext.getEventoCtx().setIdPendenza(e1.getCodVersamentoEnte());
				throw new NdpException(FaultPa.PAA_SYSTEM_ERROR, e1.getMessage(), codDominio, e1);
			} catch (ClientException e1) {
				throw new NdpException(FaultPa.PAA_SYSTEM_ERROR, "Riscontrato errore durante l'acquisizione del versamento dall'applicazione gestore del debito: " + e1, codDominio, e1);
			} catch (GovPayException e1) {
				throw new NdpException(FaultPa.PAA_SYSTEM_ERROR, "Riscontrato errore durante la verifica del versamento: " + e1, codDominio, e1);
			}
			
			response.setOutcome(StOutcome.OK);
			response.setFiscalCodePA(versamento.getDominio(configWrapper).getCodDominio());
			response.setCompanyName(versamento.getDominio(configWrapper).getRagioneSociale());
			
			if(versamento.getUo(configWrapper) != null && 
					!versamento.getUo(configWrapper).getCodUo().equals(it.govpay.model.Dominio.EC) && versamento.getUo(configWrapper).getAnagrafica() != null) {
				response.setOfficeName(versamento.getUo(configWrapper).getAnagrafica().getRagioneSociale());
			}
			if(versamento.getCausaleVersamento() != null) {
				if(versamento.getCausaleVersamento() instanceof CausaleSemplice) {
					response.setPaymentDescription(((CausaleSemplice) versamento.getCausaleVersamento()).getCausale());
				}

			} else {
				response.setPaymentDescription(" ");
			}
			
			CtPaymentOptionsDescriptionListPA paymentList = new CtPaymentOptionsDescriptionListPA();

			// Inserisco come opzione di pagamento solo quella con importo esatto del versamento
			// 30/05/2022 come indicato in https://github.com/pagopa/pagopa-api/issues/216 il supporto alle opzioni multiple e' stato sospeso
			// TODO capire come proporre eventuali altre soluzioni (es. rate o pagamenti con soglie.)
			CtPaymentOptionDescriptionPA ctPaymentOptionDescriptionPA = new CtPaymentOptionDescriptionPA();
			StAmountOption stAmountOption = StAmountOption.EQ;
			ctPaymentOptionDescriptionPA.setOptions(stAmountOption );
			ctPaymentOptionDescriptionPA.setAmount(versamento.getImportoTotale());
			ctPaymentOptionDescriptionPA.setDetailDescription(versamento.getCausaleVersamento().getSimple());
			ctPaymentOptionDescriptionPA.setDueDate(DateUtils.toLocalDate(versamento.getDataValidita()));
			ctPaymentOptionDescriptionPA.setAllCCP(VersamentoUtils.isAllIBANPostali(versamento, configWrapper));
//			paymentList.getPaymentOptionDescription().add(ctPaymentOptionDescriptionPA);
			paymentList.setPaymentOptionDescription(ctPaymentOptionDescriptionPA);

			response.setPaymentList(paymentList);
			ctx.getApplicationLogger().log("ccp.ricezioneVerificaOk", versamento.getImportoTotale().toString(), "", versamento.getCausaleVersamento() != null ? versamento.getCausaleVersamento().toString() : "[-- Nessuna causale --]");
			appContext.getEventoCtx().setEsito(Esito.OK);
			log.debug("Verifica RPT completata con successo");
		} catch (NdpException e) {
			response = this.buildRisposta(e, response);
			String faultDescription = response.getFault().getDescription() == null ? "<Nessuna descrizione>" : response.getFault().getDescription(); 
			try {
				ctx.getApplicationLogger().log("ccp.ricezioneVerificaKo", response.getFault().getFaultCode(), response.getFault().getFaultString(), faultDescription);
			} catch (UtilsException e1) {
				log.error("Errore durante il log dell'operazione: " + e1.getMessage(),e1);
			}
			appContext.getEventoCtx().setDescrizioneEsito(faultDescription);
			appContext.getEventoCtx().setSottotipoEsito(e.getFaultCode());
			if(e.getFaultCode().equals(FaultPa.PAA_SYSTEM_ERROR.name()))
				appContext.getEventoCtx().setEsito(Esito.FAIL);
			else 
				appContext.getEventoCtx().setEsito(Esito.KO);
			
			log.debug("Verifica RPT completata con esito " + appContext.getEventoCtx().getEsito());
		} catch (Exception e) {
			response = this.buildRisposta(e, codDominio, response);
			String faultDescription = response.getFault().getDescription() == null ? "<Nessuna descrizione>" : response.getFault().getDescription(); 
			try {
				ctx.getApplicationLogger().log("ccp.ricezioneVerificaKo", response.getFault().getFaultCode(), response.getFault().getFaultString(), faultDescription);
			} catch (UtilsException e1) {
				log.error("Errore durante il log dell'operazione: " + e1.getMessage(),e1);
			}
			appContext.getEventoCtx().setDescrizioneEsito(faultDescription);
			appContext.getEventoCtx().setSottotipoEsito(response.getFault().getFaultCode());
			appContext.getEventoCtx().setEsito(Esito.FAIL);
		} finally {
			GpContext.setResult(appContext.getTransaction(), response.getFault() == null ? null : response.getFault().getFaultCode());
		}
		return response;
	}

	@Override
	public PaGetPaymentRes paGetPayment(PaGetPaymentReq requestBody) {
		SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
		String codIntermediario = requestBody.getIdBrokerPA();
		String codStazione = requestBody.getIdStation();
//		String idDominio = requestBody.getIdPA();
		
		CtQrCode qrCode = requestBody.getQrCode();
		String numeroAvviso = qrCode.getNoticeNumber();
		String codDominio = qrCode.getFiscalCode();
		
		IContext ctx = ContextThreadLocal.get();
		GpContext appContext = (GpContext) ctx.getApplicationContext();
		
		PaGetPaymentRes response = new PaGetPaymentRes();
		
		try {
			BDConfigWrapper configWrapper = new BDConfigWrapper(ContextThreadLocal.get().getTransactionId(), true);
			String iuv = IuvUtils.toIuv(numeroAvviso);
			String ccp = System.currentTimeMillis() + "";
		
			appContext.setCorrelationId(codDominio + iuv + ccp);
		
			appContext.getEventoCtx().setCodDominio(codDominio);
			appContext.getEventoCtx().setIuv(iuv);
			appContext.getEventoCtx().setCcp(ccp);

			Actor from = new Actor();
			from.setName(GpContext.NodoDeiPagamentiSPC);
			from.setType(GpContext.TIPO_SOGGETTO_NDP);
			appContext.getTransaction().setFrom(from);
	
			Actor to = new Actor();
			to.setName(codStazione);
			from.setType(GpContext.TIPO_SOGGETTO_STAZIONE);
			appContext.getTransaction().setTo(to);
	
			appContext.getRequest().addGenericProperty(new Property("codDominio", codDominio));
			appContext.getRequest().addGenericProperty(new Property("iuv", iuv));
			appContext.getRequest().addGenericProperty(new Property("ccp", ccp));
			appContext.getRequest().addGenericProperty(new Property("codPsp", "non previsto per paGetPayment"));
			appContext.getRequest().addGenericProperty(new Property("codCanale", "non previsto per paGetPayment"));
			
			try {
				ctx.getApplicationLogger().log("ccp.ricezioneAttiva");
			} catch (UtilsException e) {
				log.error("Errore durante il log dell'operazione: " + e.getMessage(),e);
			}

		
			log.info("Ricevuta richiesta paGetPayment [" + codIntermediario + "][" + codStazione + "][" + codDominio + "][" + iuv + "]["+ ccp +"][" + numeroAvviso + "]");
	
			DatiPagoPA datiPagoPA = new DatiPagoPA();
			datiPagoPA.setCodStazione(codStazione);
			datiPagoPA.setFruitore(GpContext.NodoDeiPagamentiSPC);
			datiPagoPA.setErogatore(codIntermediario);
			datiPagoPA.setCodIntermediario(codIntermediario);
			appContext.getEventoCtx().setTipoEvento(TipoEventoCooperazione.PAGETPAYMENT.toString());
			datiPagoPA.setTipoVersamento(TipoVersamento.ATTIVATO_PRESSO_PSP);
			datiPagoPA.setModelloPagamento(ModelloPagamento.ATTIVATO_PRESSO_PSP);
			datiPagoPA.setCodDominio(codDominio);
			
			appContext.getEventoCtx().setDatiPagoPA(datiPagoPA);
			
			Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
			appContext.getEventoCtx().setPrincipal(AutorizzazioneUtils.getPrincipal(authentication));
			if(GovpayConfig.getInstance().isPddAuthEnable() && authentication == null) {
				ctx.getApplicationLogger().log("ccp.erroreNoAutorizzazione");
				throw new NotAuthorizedException("Autorizzazione fallita: principal non fornito");
			}
			
			log.debug("Richiesta autenticata.");
			
			log.debug("Verifica intermediario.");
			Intermediario intermediario = null;
			try {
				intermediario = AnagraficaManager.getIntermediario(configWrapper, codIntermediario);

				// Controllo autorizzazione
				if(GovpayConfig.getInstance().isPddAuthEnable()){
					boolean authOk = AuthorizationManager.checkPrincipal(authentication, intermediario.getPrincipal()); 
					
					if(!authOk) {
						GovpayLdapUserDetails details = AutorizzazioneUtils.getAuthenticationDetails(authentication);
						String principal = details.getIdentificativo(); 
						ctx.getApplicationLogger().log("ccp.erroreAutorizzazione", principal);
						throw new NotAuthorizedException("Autorizzazione fallita: principal fornito (" + principal + ") non valido per l'intermediario (" + codIntermediario + ").");
					}
				}
			} catch (NotFoundException e) {
				throw new NdpException(FaultPa.PAA_ID_INTERMEDIARIO_ERRATO, codDominio);
			}
			log.debug("Intermediario verificato: " + intermediario.getCodIntermediario());

			try {
				AnagraficaManager.getStazione(configWrapper, codStazione);
			} catch (NotFoundException e) {
				throw new NdpException(FaultPa.PAA_STAZIONE_INT_ERRATA, codDominio);
			}

			log.debug("Verifica dominio.");
			Dominio dominio;
			try {
				dominio = AnagraficaManager.getDominio(configWrapper, codDominio);
			} catch (NotFoundException e) {
				throw new NdpException(FaultPa.PAA_ID_DOMINIO_ERRATO, codDominio);
			}
			log.debug("Dominio verificato: " + dominio.getCodDominio());

			log.debug("Verifica versamento.");
			VersamentiBD versamentiBD = new VersamentiBD(configWrapper);
			Versamento versamento = null;
			it.govpay.bd.model.Applicazione applicazioneGestisceIuv = null;
			try {
				// TODO 
				// 30/05/2022 come indicato in https://github.com/pagopa/pagopa-api/issues/216 il supporto alle opzioni multiple e' stato sospeso
				// Aggiungere la ricerca del versamento corretto, utilizzando anche amount e due date
				// perche' la verify potrebbe aver restituito tra le opzioni di pagamento una rata o un pagamento con soglia.
				versamento = versamentiBD.getVersamentoByDominioIuv(dominio.getId(), iuv, true);
				appContext.getEventoCtx().setIdA2A(versamento.getApplicazione(configWrapper).getCodApplicazione());
				appContext.getEventoCtx().setIdPendenza(versamento.getCodVersamentoEnte());
			}catch (NotFoundException e) {
				applicazioneGestisceIuv = new Applicazione().getApplicazioneDominio(configWrapper,dominio,iuv,false); 
				
				if(applicazioneGestisceIuv == null) {
					throw new NdpException(FaultPa.PAA_PAGAMENTO_SCONOSCIUTO, codDominio);
				}
				appContext.getEventoCtx().setIdA2A(applicazioneGestisceIuv.getCodApplicazione());
			}

			try {
				try {
					// Se non ho lo iuv, vado direttamente a chiedere all'applicazione di default
					if(versamento == null) throw new NotFoundException();
					
					log.debug("Versamento acquisito");

					// Versamento trovato, gestisco un'eventuale scadenza
					versamento = VersamentoUtils.aggiornaVersamento(versamento, log);
					log.debug("Verifica stato versamento dopo procedura di aggiornamento: " + versamento.getStatoVersamento());
					if(versamento.getStatoVersamento().equals(StatoVersamento.ANNULLATO))
						throw new NdpException(FaultPa.PAA_PAGAMENTO_ANNULLATO, codDominio);

					if(!versamento.getStatoVersamento().equals(StatoVersamento.NON_ESEGUITO)) {
						
						if(versamento.getStatoVersamento().equals(StatoVersamento.ESEGUITO) || versamento.getStatoVersamento().equals(StatoVersamento.ESEGUITO_ALTRO_CANALE)) {
							PagamentiBD pagamentiBD = new PagamentiBD(configWrapper);
							List<Pagamento> pagamenti = pagamentiBD.getPagamentiBySingoloVersamento(versamento.getSingoliVersamenti().get(0).getId());
							if(pagamenti.isEmpty())
								throw new NdpException(FaultPa.PAA_PAGAMENTO_DUPLICATO, codDominio);
							else {
								Pagamento pagamento = pagamenti.get(0);
								throw new NdpException(FaultPa.PAA_PAGAMENTO_DUPLICATO, "Il pagamento risulta gi\u00E0 effettuato in data " + sdf.format(pagamento.getDataPagamento()) + " [Iur:" + pagamento.getIur() + "]", codDominio);
							}
						}
					}
				} catch (NotFoundException e) {
					log.debug("Versamento non presente in base dati");
					//Versamento non trovato, devo interrogare l'applicazione.
					// prendo tutte le applicazioni che gestiscono il dominio, tra queste cerco la prima che match la regexpr dello iuv la utilizzo per far acquisire il versamento
					if(applicazioneGestisceIuv == null) {
						applicazioneGestisceIuv = new Applicazione().getApplicazioneDominio(configWrapper, dominio,iuv); 
						appContext.getEventoCtx().setIdA2A(applicazioneGestisceIuv.getCodApplicazione());
					}
					
					ctx.getApplicationLogger().log("ccp.versamentoIuvNonPresente", applicazioneGestisceIuv.getCodApplicazione(), dominio.getCodDominio(), iuv);
					log.debug("Versamento non presente in base dati. Procedo all'acquisizione [CodApplicazione:" + applicazioneGestisceIuv.getCodApplicazione()+"]");
					versamento = VersamentoUtils.acquisisciVersamento(AnagraficaManager.getApplicazione(configWrapper, applicazioneGestisceIuv.getCodApplicazione()), null, null, null, codDominio, iuv, TipologiaTipoVersamento.DOVUTO, log);
					appContext.getEventoCtx().setIdA2A(versamento.getApplicazione(configWrapper).getCodApplicazione());
					appContext.getEventoCtx().setIdPendenza(versamento.getCodVersamentoEnte());
					log.debug("Versamento acquisito da Ente. Aggiorno per verifica scadenza [CodApplicazione:" + applicazioneGestisceIuv.getCodApplicazione()+"]");
					// Versamento trovato, gestisco un'eventuale scadenza
					versamento = VersamentoUtils.aggiornaVersamento(versamento, log);
					log.debug("Versamento aggiornato da Ente. Verifica stato " + versamento.getStatoVersamento());
					
					if(versamento.getStatoVersamento().equals(StatoVersamento.ANNULLATO))
						throw new NdpException(FaultPa.PAA_PAGAMENTO_ANNULLATO, codDominio);

					if(!versamento.getStatoVersamento().equals(StatoVersamento.NON_ESEGUITO)) {
						
						if(versamento.getStatoVersamento().equals(StatoVersamento.ESEGUITO) || versamento.getStatoVersamento().equals(StatoVersamento.ESEGUITO_ALTRO_CANALE)) {
							PagamentiBD pagamentiBD = new PagamentiBD(configWrapper);
							List<Pagamento> pagamenti = pagamentiBD.getPagamentiBySingoloVersamento(versamento.getSingoliVersamenti().get(0).getId());
							if(pagamenti.isEmpty())
								throw new NdpException(FaultPa.PAA_PAGAMENTO_DUPLICATO, codDominio);
							else {
								Pagamento pagamento = pagamenti.get(0);
								throw new NdpException(FaultPa.PAA_PAGAMENTO_DUPLICATO, "Il pagamento risulta gi\u00E0 effettuato in data " + sdf.format(pagamento.getDataPagamento()) + " [Iur:" + pagamento.getIur() + "]", codDominio);
							}
						}
					}
					log.debug("Versamento pagabile.");
					ctx.getApplicationLogger().log("ccp.versamentoIuvNonPresenteOk", applicazioneGestisceIuv.getCodApplicazione(), dominio.getCodDominio(), iuv);
				}
			} catch (VersamentoScadutoException e1) {
				appContext.getEventoCtx().setIdA2A(e1.getCodApplicazione());
				appContext.getEventoCtx().setIdPendenza(e1.getCodVersamentoEnte());
				throw new NdpException(FaultPa.PAA_PAGAMENTO_SCADUTO, e1.getMessage(), codDominio, e1);
			} catch (VersamentoAnnullatoException e1) {
				appContext.getEventoCtx().setIdA2A(e1.getCodApplicazione());
				appContext.getEventoCtx().setIdPendenza(e1.getCodVersamentoEnte());
				throw new NdpException(FaultPa.PAA_PAGAMENTO_ANNULLATO, e1.getMessage(), codDominio, e1);
			} catch (VersamentoDuplicatoException e1) {
				appContext.getEventoCtx().setIdA2A(e1.getCodApplicazione());
				appContext.getEventoCtx().setIdPendenza(e1.getCodVersamentoEnte());
				throw new NdpException(FaultPa.PAA_PAGAMENTO_DUPLICATO, e1.getMessage(), codDominio, e1);
			} catch (VersamentoSconosciutoException e1) {
				appContext.getEventoCtx().setIdA2A(e1.getCodApplicazione());
				appContext.getEventoCtx().setIdPendenza(e1.getCodVersamentoEnte());
				throw new NdpException(FaultPa.PAA_PAGAMENTO_SCONOSCIUTO, e1.getMessage(), codDominio, e1);
			} catch (VersamentoNonValidoException e1) {
				appContext.getEventoCtx().setIdA2A(e1.getCodApplicazione());
				appContext.getEventoCtx().setIdPendenza(e1.getCodVersamentoEnte());
				throw new NdpException(FaultPa.PAA_SYSTEM_ERROR, e1.getMessage(), codDominio, e1);
			} catch (ClientException e1) {
				throw new NdpException(FaultPa.PAA_SYSTEM_ERROR, "Riscontrato errore durante l'acquisizione del versamento dall'applicazione gestore del debito: " + e1, codDominio, e1);
			} catch (GovPayException e1) {
				throw new NdpException(FaultPa.PAA_SYSTEM_ERROR, "Riscontrato errore durante l'attivazione del versamento: " + e1, codDominio, e1);
			}
			
			// controllo che la pendenza non contenga una MBT
			if(VersamentoUtils.isPendenzaMBT(versamento, configWrapper)) {
				throw new NdpException(FaultPa.PAA_SEMANTICA, "Il versamento contiene una marca da bollo telematica, non ammessa per pagamenti ad iniziativa psp.", codDominio);
			}
			
			RptBD rptBD = new RptBD(configWrapper);

			// Creazione dell'RPT
			log.debug("Costruzione della RPT.");
			Rpt rpt = new CtPaymentPABuilder().buildCtPaymentPA(requestBody,versamento, iuv, ccp, numeroAvviso);

			ctx.getApplicationLogger().log("ccp.attivazione", rpt.getCodMsgRichiesta());

			// annullo tutte le RPT esistenti modello 3 in stato pendente per la coppia coddominio/iuv 
			it.govpay.core.business.Rpt rptBusiness = new it.govpay.core.business.Rpt();
			rptBusiness.annullaRPTPendenti(codDominio, iuv, configWrapper);
			
			rptBD = null;
			try {
				log.debug("Transazione di avvio pagamento attivata");
				rptBD = new RptBD(configWrapper);
				
				rptBD.setupConnection(configWrapper.getTransactionID());
				
				rptBD.setAtomica(false);
				
				rptBD.setAutoCommit(false);
				
				try {
					// 	L'RPT non esiste, procedo
					rptBD.insertRpt(rpt);
				}catch(ServiceException e) {
					rptBD.rollback();
					
					throw e;
				}
				
				rptBD.enableSelectForUpdate();
				
				// RPT accettata dal Nodo
				// Invio la notifica e aggiorno lo stato
				Notifica notifica = new Notifica(rpt, TipoNotifica.ATTIVAZIONE, configWrapper);
				it.govpay.core.business.Notifica notificaBD = new it.govpay.core.business.Notifica();
				
				rptBD.updateRpt(rpt.getId(), StatoRpt.RPT_ACCETTATA_NODO, null, null, null,null);
				boolean schedulaThreadInvio = notificaBD.inserisciNotifica(notifica, rptBD);
				
				if(schedulaThreadInvio)
					ThreadExecutorManager.getClientPoolExecutorNotifica().execute(new InviaNotificaThread(notifica, ctx));
				log.info("RPT inviata correttamente al nodo");
				ctx.getApplicationLogger().log("pagamento.invioRptAttivataOk");
	
				rptBD.commit();
				log.debug("Transazione di avvio pagamento conclusa");
				rptBD.disableSelectForUpdate();
			} catch (ServiceException e) {
				if(rptBD != null && !rptBD.isAutoCommit()) {
					rptBD.rollback();
				}
				throw e;
			} finally {
				if(rptBD != null) {
					rptBD.closeConnection();
				}
			}
			
			response.setOutcome(StOutcome.OK);

			PaGetPaymentRes paGetPaymentRes_RPT = JaxbUtils.toPaGetPaymentResRPT(rpt.getXmlRpt(), true);
			response.setData(paGetPaymentRes_RPT.getData()); 
			
			ctx.getApplicationLogger().log("ccp.ricezioneAttivaOk", versamento.getImportoTotale().toString(), "", versamento.getCausaleVersamento() != null ? versamento.getCausaleVersamento().toString() : "[-- Nessuna causale --]");
			appContext.getEventoCtx().setEsito(Esito.OK);
		} catch (NdpException e) {
			response = this.buildRisposta(e, response);
			String faultDescription = response.getFault().getDescription() == null ? "<Nessuna descrizione>" : response.getFault().getDescription(); 
			try {
				ctx.getApplicationLogger().log("ccp.ricezioneAttivaKo", response.getFault().getFaultCode(), response.getFault().getFaultString(), faultDescription);
			} catch (UtilsException e1) {
				log.error("Errore durante il log dell'operazione: " + e1.getMessage(),e1);
			}
			appContext.getEventoCtx().setDescrizioneEsito(faultDescription);
			appContext.getEventoCtx().setSottotipoEsito(e.getFaultCode());
			if(e.getFaultCode().equals(FaultPa.PAA_SYSTEM_ERROR.name()))
				appContext.getEventoCtx().setEsito(Esito.FAIL);
			else 
				appContext.getEventoCtx().setEsito(Esito.KO);
		} catch (Exception e) {
			response = this.buildRisposta(e, codDominio, response);
			String faultDescription = response.getFault().getDescription() == null ? "<Nessuna descrizione>" : response.getFault().getDescription(); 
			try {
				ctx.getApplicationLogger().log("ccp.ricezioneAttivaKo", response.getFault().getFaultCode(), response.getFault().getFaultString(), faultDescription);
			} catch (UtilsException e1) {
				log.error("Errore durante il log dell'operazione: " + e1.getMessage(),e1);
			}
			appContext.getEventoCtx().setSottotipoEsito(response.getFault().getFaultCode());
			appContext.getEventoCtx().setDescrizioneEsito(faultDescription);
			appContext.getEventoCtx().setEsito(Esito.FAIL);
		} finally {
			GpContext.setResult(appContext.getTransaction(), response.getFault() == null ? null : response.getFault().getFaultCode());
		}
		
		return response;
	}


	private <T> T buildRisposta(Exception e, String codDominio, T risposta) {
		return this.buildRisposta(new NdpException(FaultPa.PAA_SYSTEM_ERROR, e.getMessage(), codDominio, e), risposta);
	}

	private <T> T buildRisposta(NdpException e, T risposta) {
		if(risposta instanceof PaaAttivaRPTRisposta) {
			if(e.getFaultCode().equals(FaultPa.PAA_SYSTEM_ERROR.name()) || e.getFaultCode().equals(FaultPa.PAA_PAGAMENTO_SCONOSCIUTO.name())) {
				log.warn("Errore in Attiva RPT: " + e.getMessage(), e);
			} else {
				log.info("Rifiutata Attiva RPT con Fault " + e.getFaultString() + ( e.getDescrizione() != null ? (": " + e.getDescrizione()) : ""));
			}
			PaaAttivaRPTRisposta r = (PaaAttivaRPTRisposta) risposta;
			EsitoAttivaRPT esito = new EsitoAttivaRPT();
			esito.setEsito("KO");
			FaultBean fault = new FaultBean();
			fault.setId(e.getCodDominio());
			fault.setFaultCode(e.getFaultCode());
			fault.setFaultString(e.getFaultString());
			fault.setDescription(e.getDescrizione());
			esito.setFault(fault);
			r.setPaaAttivaRPTRisposta(esito);
		}

		if(risposta instanceof PaaVerificaRPTRisposta) {
			if(e.getFaultCode().equals(FaultPa.PAA_SYSTEM_ERROR.name()) || e.getFaultCode().equals(FaultPa.PAA_PAGAMENTO_SCONOSCIUTO.name())) {
				log.warn("Errore in Verifica RPT: " + e.getMessage(), e);
			} else {
				log.info("Rifiutata Verifica RPT con Fault " + e.getFaultString() + ( e.getDescrizione() != null ? (": " + e.getDescrizione()) : ""));
			}
			PaaVerificaRPTRisposta r = (PaaVerificaRPTRisposta) risposta;
			EsitoVerificaRPT esito = new EsitoVerificaRPT();
			esito.setEsito("KO");
			FaultBean fault = new FaultBean();
			fault.setId(e.getCodDominio());
			fault.setFaultCode(e.getFaultCode());
			fault.setFaultString(e.getFaultString());
			fault.setDescription(e.getDescrizione());
			esito.setFault(fault);
			r.setPaaVerificaRPTRisposta(esito);
		}
		
		if(risposta instanceof PaVerifyPaymentNoticeRes) {
			if(e.getFaultCode().equals(FaultPa.PAA_SYSTEM_ERROR.name()) || e.getFaultCode().equals(FaultPa.PAA_PAGAMENTO_SCONOSCIUTO.name())) {
				log.warn("Errore in PaVerifyPaymentNotice: " + e.getMessage(), e);
			} else {
				log.info("Rifiutata PaVerifyPaymentNotice con Fault " + e.getFaultString() + ( e.getDescrizione() != null ? (": " + e.getDescrizione()) : ""));
			}
			PaVerifyPaymentNoticeRes r = (PaVerifyPaymentNoticeRes) risposta;
			r.setOutcome(StOutcome.KO);
			CtFaultBean fault = new CtFaultBean();
			fault.setId(e.getCodDominio());
			fault.setFaultCode(e.getFaultCode());
			fault.setFaultString(e.getFaultString());
			fault.setDescription(e.getDescrizione());
			r.setFault(fault);
		}

		if(risposta instanceof PaGetPaymentRes) {
			if(e.getFaultCode().equals(FaultPa.PAA_SYSTEM_ERROR.name()) || e.getFaultCode().equals(FaultPa.PAA_PAGAMENTO_SCONOSCIUTO.name())) {
				log.warn("Errore in PaGetPayment: " + e.getMessage(), e);
			} else {
				log.info("Rifiutata PaGetPayment con Fault " + e.getFaultString() + ( e.getDescrizione() != null ? (": " + e.getDescrizione()) : ""));
			}
			PaGetPaymentRes r = (PaGetPaymentRes) risposta;
			r.setOutcome(StOutcome.KO);
			CtFaultBean fault = new CtFaultBean();
			fault.setId(e.getCodDominio());
			fault.setFaultCode(e.getFaultCode());
			fault.setFaultString(e.getFaultString());
			fault.setDescription(e.getDescrizione());
			r.setFault(fault);
		}
		
		if(risposta instanceof PaSendRTRes) {
			if(e.getFaultCode().equals(FaultPa.PAA_SYSTEM_ERROR.name()) || e.getFaultCode().equals(FaultPa.PAA_PAGAMENTO_SCONOSCIUTO.name())) {
				log.warn("Errore in PaSendRT: " + e.getMessage(), e);
			} else {
				log.info("Rifiutata PaSendRT con Fault " + e.getFaultString() + ( e.getDescrizione() != null ? (": " + e.getDescrizione()) : ""));
			}
			PaSendRTRes r = (PaSendRTRes) risposta;
			r.setOutcome(StOutcome.KO);
			CtFaultBean fault = new CtFaultBean();
			fault.setId(e.getCodDominio());
			fault.setFaultCode(e.getFaultCode());
			fault.setFaultString(e.getFaultString());
			fault.setDescription(e.getDescrizione());
			r.setFault(fault);
		}
		
		if(risposta instanceof PaSendRTV2Response) {
			if(e.getFaultCode().equals(FaultPa.PAA_SYSTEM_ERROR.name())) {
				log.warn("Errore in PaSendRT: " + e.getMessage(), e);
			} else {
				log.warn("Rifiutata PaSendRT con Fault " + e.getFaultString() + ( e.getDescrizione() != null ? (": " + e.getDescrizione()) : ""));
			}
			PaSendRTV2Response r = (PaSendRTV2Response) risposta;
			r.setOutcome(StOutcome.KO);
			CtFaultBean fault = new CtFaultBean();
			fault.setId(e.getCodDominio());
			fault.setFaultCode(e.getFaultCode());
			fault.setFaultString(e.getFaultString());
			fault.setDescription(e.getDescrizione());
			r.setFault(fault);
		}
		
		if(risposta instanceof PaGetPaymentV2Response) {
			if(e.getFaultCode().equals(FaultPa.PAA_SYSTEM_ERROR.name())) {
				log.warn("Errore in PaSendRT: " + e.getMessage(), e);
			} else {
				log.warn("Rifiutata PaSendRT con Fault " + e.getFaultString() + ( e.getDescrizione() != null ? (": " + e.getDescrizione()) : ""));
			}
			PaGetPaymentV2Response r = (PaGetPaymentV2Response) risposta;
			r.setOutcome(StOutcome.KO);
			CtFaultBean fault = new CtFaultBean();
			fault.setId(e.getCodDominio());
			fault.setFaultCode(e.getFaultCode());
			fault.setFaultString(e.getFaultString());
			fault.setDescription(e.getDescrizione());
			r.setFault(fault);
		}
		
		if(risposta instanceof PaDemandPaymentNoticeResponse) {
			if(e.getFaultCode().equals(FaultPa.PAA_SYSTEM_ERROR.name())) {
				log.warn("Errore in PaSendRT: " + e.getMessage(), e);
			} else {
				log.warn("Rifiutata PaSendRT con Fault " + e.getFaultString() + ( e.getDescrizione() != null ? (": " + e.getDescrizione()) : ""));
			}
			PaDemandPaymentNoticeResponse r = (PaDemandPaymentNoticeResponse) risposta;
			r.setOutcome(StOutcome.KO);
			CtFaultBean fault = new CtFaultBean();
			fault.setId(e.getCodDominio());
			fault.setFaultCode(e.getFaultCode());
			fault.setFaultString(e.getFaultString());
			fault.setDescription(e.getDescrizione());
			r.setFault(fault);
		}
		
		if(risposta instanceof PaSendRTV2Response) {
			if(e.getFaultCode().equals(FaultPa.PAA_SYSTEM_ERROR.name())) {
				log.warn("Errore in PaSendRTV2: " + e.getMessage(), e);
			} else {
				log.warn("Rifiutata PaSendRTV2 con Fault " + e.getFaultString() + ( e.getDescrizione() != null ? (": " + e.getDescrizione()) : ""));
			}
			PaSendRTV2Response r = (PaSendRTV2Response) risposta;
			r.setOutcome(StOutcome.KO);
			CtFaultBean fault = new CtFaultBean();
			fault.setId(e.getCodDominio());
			fault.setFaultCode(e.getFaultCode());
			fault.setFaultString(e.getFaultString());
			fault.setDescription(e.getDescrizione());
			r.setFault(fault);
		}
		
		if(risposta instanceof PaGetPaymentV2Response) {
			if(e.getFaultCode().equals(FaultPa.PAA_SYSTEM_ERROR.name())) {
				log.warn("Errore in PaGetPaymentV2: " + e.getMessage(), e);
			} else {
				log.warn("Rifiutata PaGetPaymentV2 con Fault " + e.getFaultString() + ( e.getDescrizione() != null ? (": " + e.getDescrizione()) : ""));
			}
			PaGetPaymentV2Response r = (PaGetPaymentV2Response) risposta;
			r.setOutcome(StOutcome.KO);
			CtFaultBean fault = new CtFaultBean();
			fault.setId(e.getCodDominio());
			fault.setFaultCode(e.getFaultCode());
			fault.setFaultString(e.getFaultString());
			fault.setDescription(e.getDescrizione());
			r.setFault(fault);
		}
		
		if(risposta instanceof PaDemandPaymentNoticeResponse) {
			if(e.getFaultCode().equals(FaultPa.PAA_SYSTEM_ERROR.name())) {
				log.warn("Errore in PaDemandPaymentNotice: " + e.getMessage(), e);
			} else {
				log.warn("Rifiutata PaDemandPaymentNotice con Fault " + e.getFaultString() + ( e.getDescrizione() != null ? (": " + e.getDescrizione()) : ""));
			}
			PaDemandPaymentNoticeResponse r = (PaDemandPaymentNoticeResponse) risposta;
			r.setOutcome(StOutcome.KO);
			CtFaultBean fault = new CtFaultBean();
			fault.setId(e.getCodDominio());
			fault.setFaultCode(e.getFaultCode());
			fault.setFaultString(e.getFaultString());
			fault.setDescription(e.getDescrizione());
			r.setFault(fault);
		}

		return risposta;
	}



	@Override
	public PaSendRTV2Response paSendRTV2(PaSendRTV2Request requestBody) {
		String codIntermediario = requestBody.getIdBrokerPA();
		String codStazione = requestBody.getIdStation();
//		String idDominio = requestBody.getIdPA();
		
		CtReceiptV2 receipt = requestBody.getReceipt();
		
		String codDominio = receipt.getFiscalCode();
		String iuv = receipt.getCreditorReferenceId();
		String ccp = receipt.getReceiptId();

		IContext ctx = ContextThreadLocal.get();
		GpContext appContext = (GpContext) ctx.getApplicationContext();
		
		appContext.setCorrelationId(codDominio + iuv + ccp);

		Actor from = new Actor();
		from.setName(GpContext.NodoDeiPagamentiSPC);
		from.setType(GpContext.TIPO_SOGGETTO_NDP);
		appContext.getTransaction().setFrom(from);

		Actor to = new Actor();
		to.setName(codStazione);
		from.setType(GpContext.TIPO_SOGGETTO_STAZIONE);
		appContext.getTransaction().setTo(to);

		appContext.getRequest().addGenericProperty(new Property("codDominio", codDominio));
		appContext.getRequest().addGenericProperty(new Property("iuv", iuv));
		appContext.getRequest().addGenericProperty(new Property("ccp", ccp));

		appContext.getEventoCtx().setCodDominio(codDominio);
		appContext.getEventoCtx().setIuv(iuv);
		appContext.getEventoCtx().setCcp(receipt.getReceiptId());

		try {
			ctx.getApplicationLogger().log("pagamento.ricezioneRt");
		} catch (UtilsException e) {
			log.error("Errore durante il log dell'operazione: " + e.getMessage(),e);
		}

		log.info("Ricevuta richiesta paSendRTV2 [" + codDominio + "][" + iuv + "][" + ccp + "]");
		PaSendRTV2Response response = new PaSendRTV2Response();

		DatiPagoPA datiPagoPA = new DatiPagoPA();
		datiPagoPA.setCodStazione(codStazione);
		datiPagoPA.setFruitore(GpContext.NodoDeiPagamentiSPC);
		datiPagoPA.setCodDominio(codDominio);
		datiPagoPA.setErogatore(codIntermediario);
		datiPagoPA.setCodIntermediario(codIntermediario);
		appContext.getEventoCtx().setDatiPagoPA(datiPagoPA);

		try {
			BDConfigWrapper configWrapper = new BDConfigWrapper(ContextThreadLocal.get().getTransactionId(), true);

			Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
			appContext.getEventoCtx().setPrincipal(AutorizzazioneUtils.getPrincipal(authentication));
			if(GovpayConfig.getInstance().isPddAuthEnable() && authentication == null) {
				ctx.getApplicationLogger().log("rt.erroreNoAutorizzazione");
				throw new NotAuthorizedException("Autorizzazione fallita: principal non fornito");
			}

			Intermediario intermediario = null;
			try {
				intermediario = AnagraficaManager.getIntermediario(configWrapper, codIntermediario);

				// Controllo autorizzazione
				if(GovpayConfig.getInstance().isPddAuthEnable()){
					boolean authOk = AuthorizationManager.checkPrincipal(authentication, intermediario.getPrincipal()); 

					if(!authOk) {
						GovpayLdapUserDetails details = AutorizzazioneUtils.getAuthenticationDetails(authentication);
						String principal = details.getIdentificativo(); 
						ctx.getApplicationLogger().log("rt.erroreAutorizzazione", principal);
						throw new NotAuthorizedException("Autorizzazione fallita: principal fornito (" + principal + ") non valido per l'intermediario (" + codIntermediario + ").");
					}
				}
			} catch (NotFoundException e) {
				throw new NdpException(FaultPa.PAA_ID_INTERMEDIARIO_ERRATO, codDominio);
			}

			Dominio dominio = null;
			try {
				dominio = AnagraficaManager.getDominio(configWrapper, codDominio);
			} catch (NotFoundException e) {
				throw new NdpException(FaultPa.PAA_ID_DOMINIO_ERRATO, codDominio);
			}

			Stazione stazione = null;
			try {
				stazione = AnagraficaManager.getStazione(configWrapper, codStazione);
			} catch (NotFoundException e) {
				throw new NdpException(FaultPa.PAA_STAZIONE_INT_ERRATA, codDominio);
			}

			if(stazione.getIdIntermediario() != intermediario.getId()) {
				throw new NdpException(FaultPa.PAA_ID_INTERMEDIARIO_ERRATO, codDominio);
			}

			if(dominio.getIdStazione().compareTo(stazione.getId())!=0) {
				throw new NdpException(FaultPa.PAA_STAZIONE_INT_ERRATA, codDominio);
			}

			Rpt rpt = CtReceiptV2Utils.acquisisciRT(codDominio, iuv, requestBody, false);

			appContext.getEventoCtx().setIdA2A(rpt.getVersamento(configWrapper).getApplicazione(configWrapper).getCodApplicazione());
			appContext.getEventoCtx().setIdPendenza(rpt.getVersamento(configWrapper).getCodVersamentoEnte());
			if(rpt.getIdPagamentoPortale() != null)
				appContext.getEventoCtx().setIdPagamento(rpt.getPagamentoPortale(configWrapper).getIdSessione());

			appContext.getResponse().addGenericProperty(new Property("esitoPagamento", rpt.getEsitoPagamento().toString()));
			ctx.getApplicationLogger().log("pagamento.acquisizioneRtOk");

			datiPagoPA.setCodCanale(rpt.getCodCanale());
			datiPagoPA.setTipoVersamento(rpt.getTipoVersamento());

			response.setOutcome(StOutcome.OK);
			ctx.getApplicationLogger().log("rt.ricezioneOk");
			appContext.getEventoCtx().setDescrizioneEsito("Acquisita ricevuta di pagamento [IUV: " + rpt.getIuv() + " CCP:" + rpt.getCcp() + "] emessa da " + rpt.getDenominazioneAttestante());
			appContext.getEventoCtx().setEsito(Esito.OK);
		} catch (NdpException e) {
			response = this.buildRisposta(e, response);
			String faultDescription = response.getFault().getDescription() == null ? "<Nessuna descrizione>" : response.getFault().getDescription(); 
			try {
				ctx.getApplicationLogger().log("rt.ricezioneKo", response.getFault().getFaultCode(), response.getFault().getFaultString(), faultDescription);
			} catch (UtilsException e1) {
				log.error("Errore durante il log dell'operazione: " + e1.getMessage(),e1);
			}
			if(e.getFaultCode().equals(FaultPa.PAA_SYSTEM_ERROR.name()))
				appContext.getEventoCtx().setEsito(Esito.FAIL);
			else 
				appContext.getEventoCtx().setEsito(Esito.KO);
			appContext.getEventoCtx().setDescrizioneEsito(faultDescription);
			appContext.getEventoCtx().setSottotipoEsito(e.getFaultCode());
		} catch (Exception e) {
			response = this.buildRisposta(new NdpException(FaultPa.PAA_SYSTEM_ERROR, codDominio, e.getMessage(), e), response);
			String faultDescription = response.getFault().getDescription() == null ? "<Nessuna descrizione>" : response.getFault().getDescription(); 
			try {
				ctx.getApplicationLogger().log("rt.ricezioneKo", response.getFault().getFaultCode(), response.getFault().getFaultString(), faultDescription);
			} catch (UtilsException e1) {
				log.error("Errore durante il log dell'operazione: " + e1.getMessage(),e1);
			}
			appContext.getEventoCtx().setSottotipoEsito(response.getFault().getFaultCode());
			appContext.getEventoCtx().setEsito(Esito.FAIL);
			appContext.getEventoCtx().setDescrizioneEsito(faultDescription);
		} finally {
			GpContext.setResult(appContext.getTransaction(), response.getFault() == null ? null : response.getFault().getFaultCode());
		}
		
		return response;
	}



	@Override
	public PaGetPaymentV2Response paGetPaymentV2(PaGetPaymentV2Request requestBody) {
		SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
		String codIntermediario = requestBody.getIdBrokerPA();
		String codStazione = requestBody.getIdStation();
//		String idDominio = requestBody.getIdPA();
		
		CtQrCode qrCode = requestBody.getQrCode();
		String numeroAvviso = qrCode.getNoticeNumber();
		String codDominio = qrCode.getFiscalCode();
		
		IContext ctx = ContextThreadLocal.get();
		GpContext appContext = (GpContext) ctx.getApplicationContext();
		
		PaGetPaymentV2Response response = new PaGetPaymentV2Response();
		
		try {
			BDConfigWrapper configWrapper = new BDConfigWrapper(ContextThreadLocal.get().getTransactionId(), true);
			String iuv = IuvUtils.toIuv(numeroAvviso);
			String ccp = System.currentTimeMillis() + "";
		
			appContext.setCorrelationId(codDominio + iuv + ccp);
		
			appContext.getEventoCtx().setCodDominio(codDominio);
			appContext.getEventoCtx().setIuv(iuv);
			appContext.getEventoCtx().setCcp(ccp);

			Actor from = new Actor();
			from.setName(GpContext.NodoDeiPagamentiSPC);
			from.setType(GpContext.TIPO_SOGGETTO_NDP);
			appContext.getTransaction().setFrom(from);
	
			Actor to = new Actor();
			to.setName(codStazione);
			from.setType(GpContext.TIPO_SOGGETTO_STAZIONE);
			appContext.getTransaction().setTo(to);
	
			appContext.getRequest().addGenericProperty(new Property("codDominio", codDominio));
			appContext.getRequest().addGenericProperty(new Property("iuv", iuv));
			appContext.getRequest().addGenericProperty(new Property("ccp", ccp));
			appContext.getRequest().addGenericProperty(new Property("codPsp", "non previsto per paGetPaymentV2"));
			appContext.getRequest().addGenericProperty(new Property("codCanale", "non previsto per paGetPaymentV2"));
			
			try {
				ctx.getApplicationLogger().log("ccp.ricezioneAttiva");
			} catch (UtilsException e) {
				log.error("Errore durante il log dell'operazione: " + e.getMessage(),e);
			}

		
			log.info("Ricevuta richiesta paGetPaymentV2 [" + codIntermediario + "][" + codStazione + "][" + codDominio + "][" + iuv + "]["+ ccp +"][" + numeroAvviso + "]");
	
			DatiPagoPA datiPagoPA = new DatiPagoPA();
			datiPagoPA.setCodStazione(codStazione);
			datiPagoPA.setFruitore(GpContext.NodoDeiPagamentiSPC);
			datiPagoPA.setErogatore(codIntermediario);
			datiPagoPA.setCodIntermediario(codIntermediario);
			appContext.getEventoCtx().setTipoEvento(TipoEventoCooperazione.PAGETPAYMENTV2.toString());
			datiPagoPA.setTipoVersamento(TipoVersamento.ATTIVATO_PRESSO_PSP);
			datiPagoPA.setModelloPagamento(ModelloPagamento.ATTIVATO_PRESSO_PSP);
			datiPagoPA.setCodDominio(codDominio);
			
			appContext.getEventoCtx().setDatiPagoPA(datiPagoPA);
			
			Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
			appContext.getEventoCtx().setPrincipal(AutorizzazioneUtils.getPrincipal(authentication));
			if(GovpayConfig.getInstance().isPddAuthEnable() && authentication == null) {
				ctx.getApplicationLogger().log("ccp.erroreNoAutorizzazione");
				throw new NotAuthorizedException("Autorizzazione fallita: principal non fornito");
			}
			
			log.debug("Richiesta autenticata.");
			
			log.debug("Verifica intermediario.");
			Intermediario intermediario = null;
			try {
				intermediario = AnagraficaManager.getIntermediario(configWrapper, codIntermediario);

				// Controllo autorizzazione
				if(GovpayConfig.getInstance().isPddAuthEnable()){
					boolean authOk = AuthorizationManager.checkPrincipal(authentication, intermediario.getPrincipal()); 
					
					if(!authOk) {
						GovpayLdapUserDetails details = AutorizzazioneUtils.getAuthenticationDetails(authentication);
						String principal = details.getIdentificativo(); 
						ctx.getApplicationLogger().log("ccp.erroreAutorizzazione", principal);
						throw new NotAuthorizedException("Autorizzazione fallita: principal fornito (" + principal + ") non valido per l'intermediario (" + codIntermediario + ").");
					}
				}
			} catch (NotFoundException e) {
				throw new NdpException(FaultPa.PAA_ID_INTERMEDIARIO_ERRATO, codDominio);
			}
			log.debug("Intermediario verificato: " + intermediario.getCodIntermediario());

			try {
				AnagraficaManager.getStazione(configWrapper, codStazione);
			} catch (NotFoundException e) {
				throw new NdpException(FaultPa.PAA_STAZIONE_INT_ERRATA, codDominio);
			}

			log.debug("Verifica dominio.");
			Dominio dominio;
			try {
				dominio = AnagraficaManager.getDominio(configWrapper, codDominio);
			} catch (NotFoundException e) {
				throw new NdpException(FaultPa.PAA_ID_DOMINIO_ERRATO, codDominio);
			}
			log.debug("Dominio verificato: " + dominio.getCodDominio());

			log.debug("Verifica versamento.");
			VersamentiBD versamentiBD = new VersamentiBD(configWrapper);
			Versamento versamento = null;
			it.govpay.bd.model.Applicazione applicazioneGestisceIuv = null;
			try {
				// TODO 
				// 30/05/2022 come indicato in https://github.com/pagopa/pagopa-api/issues/216 il supporto alle opzioni multiple e' stato sospeso
				// Aggiungere la ricerca del versamento corretto, utilizzando anche amount e due date
				// perche' la verify potrebbe aver restituito tra le opzioni di pagamento una rata o un pagamento con soglia.
				versamento = versamentiBD.getVersamentoByDominioIuv(dominio.getId(), iuv, true);
				appContext.getEventoCtx().setIdA2A(versamento.getApplicazione(configWrapper).getCodApplicazione());
				appContext.getEventoCtx().setIdPendenza(versamento.getCodVersamentoEnte());
			}catch (NotFoundException e) {
				applicazioneGestisceIuv = new Applicazione().getApplicazioneDominio(configWrapper,dominio,iuv,false); 
				
				if(applicazioneGestisceIuv == null) {
					throw new NdpException(FaultPa.PAA_PAGAMENTO_SCONOSCIUTO, codDominio);
				}
				appContext.getEventoCtx().setIdA2A(applicazioneGestisceIuv.getCodApplicazione());
			}

			try {
				try {
					// Se non ho lo iuv, vado direttamente a chiedere all'applicazione di default
					if(versamento == null) throw new NotFoundException();
					
					log.debug("Versamento acquisito");

					// Versamento trovato, gestisco un'eventuale scadenza
					versamento = VersamentoUtils.aggiornaVersamento(versamento, log);
					log.debug("Verifica stato versamento dopo procedura di aggiornamento: " + versamento.getStatoVersamento());
					if(versamento.getStatoVersamento().equals(StatoVersamento.ANNULLATO))
						throw new NdpException(FaultPa.PAA_PAGAMENTO_ANNULLATO, codDominio);

					if(!versamento.getStatoVersamento().equals(StatoVersamento.NON_ESEGUITO)) {
						
						if(versamento.getStatoVersamento().equals(StatoVersamento.ESEGUITO) || versamento.getStatoVersamento().equals(StatoVersamento.ESEGUITO_ALTRO_CANALE)) {
							PagamentiBD pagamentiBD = new PagamentiBD(configWrapper);
							List<Pagamento> pagamenti = pagamentiBD.getPagamentiBySingoloVersamento(versamento.getSingoliVersamenti().get(0).getId());
							if(pagamenti.isEmpty())
								throw new NdpException(FaultPa.PAA_PAGAMENTO_DUPLICATO, codDominio);
							else {
								Pagamento pagamento = pagamenti.get(0);
								throw new NdpException(FaultPa.PAA_PAGAMENTO_DUPLICATO, "Il pagamento risulta gi\u00E0 effettuato in data " + sdf.format(pagamento.getDataPagamento()) + " [Iur:" + pagamento.getIur() + "]", codDominio);
							}
						}
					}
				} catch (NotFoundException e) {
					log.debug("Versamento non presente in base dati");
					//Versamento non trovato, devo interrogare l'applicazione.
					// prendo tutte le applicazioni che gestiscono il dominio, tra queste cerco la prima che match la regexpr dello iuv la utilizzo per far acquisire il versamento
					if(applicazioneGestisceIuv == null) {
						applicazioneGestisceIuv = new Applicazione().getApplicazioneDominio(configWrapper, dominio,iuv); 
						appContext.getEventoCtx().setIdA2A(applicazioneGestisceIuv.getCodApplicazione());
					}
					
					ctx.getApplicationLogger().log("ccp.versamentoIuvNonPresente", applicazioneGestisceIuv.getCodApplicazione(), dominio.getCodDominio(), iuv);
					log.debug("Versamento non presente in base dati. Procedo all'acquisizione [CodApplicazione:" + applicazioneGestisceIuv.getCodApplicazione()+"]");
					versamento = VersamentoUtils.acquisisciVersamento(AnagraficaManager.getApplicazione(configWrapper, applicazioneGestisceIuv.getCodApplicazione()), null, null, null, codDominio, iuv, TipologiaTipoVersamento.DOVUTO, log);
					appContext.getEventoCtx().setIdA2A(versamento.getApplicazione(configWrapper).getCodApplicazione());
					appContext.getEventoCtx().setIdPendenza(versamento.getCodVersamentoEnte());
					log.debug("Versamento acquisito da Ente. Aggiorno per verifica scadenza [CodApplicazione:" + applicazioneGestisceIuv.getCodApplicazione()+"]");
					// Versamento trovato, gestisco un'eventuale scadenza
					versamento = VersamentoUtils.aggiornaVersamento(versamento, log);
					log.debug("Versamento aggiornato da Ente. Verifica stato " + versamento.getStatoVersamento());
					
					if(versamento.getStatoVersamento().equals(StatoVersamento.ANNULLATO))
						throw new NdpException(FaultPa.PAA_PAGAMENTO_ANNULLATO, codDominio);

					if(!versamento.getStatoVersamento().equals(StatoVersamento.NON_ESEGUITO)) {
						
						if(versamento.getStatoVersamento().equals(StatoVersamento.ESEGUITO) || versamento.getStatoVersamento().equals(StatoVersamento.ESEGUITO_ALTRO_CANALE)) {
							PagamentiBD pagamentiBD = new PagamentiBD(configWrapper);
							List<Pagamento> pagamenti = pagamentiBD.getPagamentiBySingoloVersamento(versamento.getSingoliVersamenti().get(0).getId());
							if(pagamenti.isEmpty())
								throw new NdpException(FaultPa.PAA_PAGAMENTO_DUPLICATO, codDominio);
							else {
								Pagamento pagamento = pagamenti.get(0);
								throw new NdpException(FaultPa.PAA_PAGAMENTO_DUPLICATO, "Il pagamento risulta gi\u00E0 effettuato in data " + sdf.format(pagamento.getDataPagamento()) + " [Iur:" + pagamento.getIur() + "]", codDominio);
							}
						}
					}
					log.debug("Versamento pagabile.");
					ctx.getApplicationLogger().log("ccp.versamentoIuvNonPresenteOk", applicazioneGestisceIuv.getCodApplicazione(), dominio.getCodDominio(), iuv);
				}
			} catch (VersamentoScadutoException e1) {
				appContext.getEventoCtx().setIdA2A(e1.getCodApplicazione());
				appContext.getEventoCtx().setIdPendenza(e1.getCodVersamentoEnte());
				throw new NdpException(FaultPa.PAA_PAGAMENTO_SCADUTO, e1.getMessage(), codDominio, e1);
			} catch (VersamentoAnnullatoException e1) {
				appContext.getEventoCtx().setIdA2A(e1.getCodApplicazione());
				appContext.getEventoCtx().setIdPendenza(e1.getCodVersamentoEnte());
				throw new NdpException(FaultPa.PAA_PAGAMENTO_ANNULLATO, e1.getMessage(), codDominio, e1);
			} catch (VersamentoDuplicatoException e1) {
				appContext.getEventoCtx().setIdA2A(e1.getCodApplicazione());
				appContext.getEventoCtx().setIdPendenza(e1.getCodVersamentoEnte());
				throw new NdpException(FaultPa.PAA_PAGAMENTO_DUPLICATO, e1.getMessage(), codDominio, e1);
			} catch (VersamentoSconosciutoException e1) {
				appContext.getEventoCtx().setIdA2A(e1.getCodApplicazione());
				appContext.getEventoCtx().setIdPendenza(e1.getCodVersamentoEnte());
				throw new NdpException(FaultPa.PAA_PAGAMENTO_SCONOSCIUTO, e1.getMessage(), codDominio, e1);
			} catch (VersamentoNonValidoException e1) {
				appContext.getEventoCtx().setIdA2A(e1.getCodApplicazione());
				appContext.getEventoCtx().setIdPendenza(e1.getCodVersamentoEnte());
				throw new NdpException(FaultPa.PAA_SYSTEM_ERROR, e1.getMessage(), codDominio, e1);
			} catch (ClientException e1) {
				throw new NdpException(FaultPa.PAA_SYSTEM_ERROR, "Riscontrato errore durante l'acquisizione del versamento dall'applicazione gestore del debito: " + e1, codDominio, e1);
			} catch (GovPayException e1) {
				throw new NdpException(FaultPa.PAA_SYSTEM_ERROR, "Riscontrato errore durante l'attivazione del versamento: " + e1, codDominio, e1);
			}
			
			
			RptBD rptBD = new RptBD(configWrapper);

			// Creazione dell'RPT
			log.debug("Costruzione della RPT.");
			Rpt rpt = new CtPaymentPAV2Builder().buildCtPaymentPAV2(requestBody,versamento, iuv, ccp, numeroAvviso);

			ctx.getApplicationLogger().log("ccp.attivazione", rpt.getCodMsgRichiesta());

			// annullo tutte le RPT esistenti modello 3 in stato pendente per la coppia coddominio/iuv 
			it.govpay.core.business.Rpt rptBusiness = new it.govpay.core.business.Rpt();
			rptBusiness.annullaRPTPendenti(codDominio, iuv, configWrapper);
			
			rptBD = null;
			try {
				log.debug("Transazione di avvio pagamento attivata");
				rptBD = new RptBD(configWrapper);
				
				rptBD.setupConnection(configWrapper.getTransactionID());
				
				rptBD.setAtomica(false);
				
				rptBD.setAutoCommit(false);
				
				try {
					// 	L'RPT non esiste, procedo
					rptBD.insertRpt(rpt);
				}catch(ServiceException e) {
					rptBD.rollback();
					throw e;
				}
				
				rptBD.enableSelectForUpdate();
				
				// RPT accettata dal Nodo
				// Invio la notifica e aggiorno lo stato
				Notifica notifica = new Notifica(rpt, TipoNotifica.ATTIVAZIONE, configWrapper);
				it.govpay.core.business.Notifica notificaBD = new it.govpay.core.business.Notifica();
				
				rptBD.updateRpt(rpt.getId(), StatoRpt.RPT_ACCETTATA_NODO, null, null, null,null);
				boolean schedulaThreadInvio = notificaBD.inserisciNotifica(notifica, rptBD);
				
				if(schedulaThreadInvio)
					ThreadExecutorManager.getClientPoolExecutorNotifica().execute(new InviaNotificaThread(notifica, ctx));
				log.info("RPT inviata correttamente al nodo");
				ctx.getApplicationLogger().log("pagamento.invioRptAttivataOk");
	
				rptBD.commit();
				log.debug("Transazione di avvio pagamento conclusa");
				rptBD.disableSelectForUpdate();
			} catch (ServiceException e) {
				if(rptBD != null && !rptBD.isAutoCommit()) {
					rptBD.rollback();
				}
				throw e;
			} finally {
				if(rptBD != null) {
					rptBD.closeConnection();
				}
			}
			
			response.setOutcome(StOutcome.OK);

			PaGetPaymentV2Response paGetPaymentRes_RPT = JaxbUtils.toPaGetPaymentV2ResponseRPT(rpt.getXmlRpt(), true);
			response.setData(paGetPaymentRes_RPT.getData()); 
			
			ctx.getApplicationLogger().log("ccp.ricezioneAttivaOk", versamento.getImportoTotale().toString(), "", versamento.getCausaleVersamento() != null ? versamento.getCausaleVersamento().toString() : "[-- Nessuna causale --]");
			appContext.getEventoCtx().setEsito(Esito.OK);
		} catch (NdpException e) {
			response = this.buildRisposta(e, response);
			String faultDescription = response.getFault().getDescription() == null ? "<Nessuna descrizione>" : response.getFault().getDescription(); 
			try {
				ctx.getApplicationLogger().log("ccp.ricezioneAttivaKo", response.getFault().getFaultCode(), response.getFault().getFaultString(), faultDescription);
			} catch (UtilsException e1) {
				log.error("Errore durante il log dell'operazione: " + e1.getMessage(),e1);
			}
			appContext.getEventoCtx().setDescrizioneEsito(faultDescription);
			appContext.getEventoCtx().setSottotipoEsito(e.getFaultCode());
			if(e.getFaultCode().equals(FaultPa.PAA_SYSTEM_ERROR.name()))
				appContext.getEventoCtx().setEsito(Esito.FAIL);
			else 
				appContext.getEventoCtx().setEsito(Esito.KO);
		} catch (Exception e) {
			response = this.buildRisposta(e, codDominio, response);
			String faultDescription = response.getFault().getDescription() == null ? "<Nessuna descrizione>" : response.getFault().getDescription(); 
			try {
				ctx.getApplicationLogger().log("ccp.ricezioneAttivaKo", response.getFault().getFaultCode(), response.getFault().getFaultString(), faultDescription);
			} catch (UtilsException e1) {
				log.error("Errore durante il log dell'operazione: " + e1.getMessage(),e1);
			}
			appContext.getEventoCtx().setSottotipoEsito(response.getFault().getFaultCode());
			appContext.getEventoCtx().setDescrizioneEsito(faultDescription);
			appContext.getEventoCtx().setEsito(Esito.FAIL);
		} finally {
			GpContext.setResult(appContext.getTransaction(), response.getFault() == null ? null : response.getFault().getFaultCode());
		}
		
		return response;
	}



	@Override
	public PaDemandPaymentNoticeResponse paDemandPaymentNotice(PaDemandPaymentNoticeRequest requestBody) {
		String codDominio = requestBody.getIdPA();
		
		IContext ctx = ContextThreadLocal.get();
		GpContext appContext = (GpContext) ctx.getApplicationContext();
		appContext.getEventoCtx().setTipoEvento(TipoEventoCooperazione.PADEMANDPAYMENTNOTICE.toString());
		
		PaDemandPaymentNoticeResponse response = new PaDemandPaymentNoticeResponse();
		
		try {
			throw new GovPayException("Operazione non disponibile.", EsitoOperazione.INTERNAL);
		} catch (Exception e) {
			response = this.buildRisposta(e, codDominio, response);
			String faultDescription = response.getFault().getDescription() == null ? "<Nessuna descrizione>" : response.getFault().getDescription(); 
			try {
				ctx.getApplicationLogger().log("ccp.ricezioneAttivaKo", response.getFault().getFaultCode(), response.getFault().getFaultString(), faultDescription);
			} catch (UtilsException e1) {
				log.error("Errore durante il log dell'operazione: " + e1.getMessage(),e1);
			}
			appContext.getEventoCtx().setSottotipoEsito(response.getFault().getFaultCode());
			appContext.getEventoCtx().setDescrizioneEsito(faultDescription);
			appContext.getEventoCtx().setEsito(Esito.FAIL);
		} finally {
			GpContext.setResult(appContext.getTransaction(), response.getFault() == null ? null : response.getFault().getFaultCode());
		}
		
		return response;
	}



	@Override
	public PaaInviaRTRisposta paaInviaRT(PaaInviaRT bodyrichiesta, IntestazionePPT header) {
		return PaaInviaRTRispostaConverter.toPaaInviaRTRisposta_CCP(PagamentiTelematiciRTImpl.paaInviaRTImpl(PaaInviaRTConverter.toPaaInviaRT_RT(bodyrichiesta), header, log));
	}
}
