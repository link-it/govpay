/*
 * GovPay - Porta di Accesso al Nodo dei Pagamenti SPC 
 * http://www.gov4j.it/govpay
 * 
 * Copyright (c) 2014-2016 Link.it srl (http://www.link.it).
 * 
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
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

import java.util.Date;

import gov.telematici.pagamenti.ws.ppthead.IntestazionePPT;
import it.gov.digitpa.schemas._2011.pagamenti.CtSoggettoVersante;
import it.gov.digitpa.schemas._2011.pagamenti.StAutenticazioneSoggetto;
import it.gov.digitpa.schemas._2011.ws.psp.CtSpezzoniCausaleVersamento;
import it.gov.digitpa.schemas._2011.ws.psp.FaultBean;
import it.gov.digitpa.schemas._2011.ws.psp.EsitoAttivaRPT;
import it.gov.digitpa.schemas._2011.ws.psp.EsitoVerificaRPT;
import it.gov.digitpa.schemas._2011.ws.psp.PaaAttivaRPT;
import it.gov.digitpa.schemas._2011.ws.psp.PaaAttivaRPTRisposta;
import it.gov.digitpa.schemas._2011.ws.psp.PaaTipoDatiPagamentoPA;
import it.gov.digitpa.schemas._2011.ws.psp.PaaVerificaRPT;
import it.gov.digitpa.schemas._2011.ws.psp.PaaVerificaRPTRisposta;
import it.gov.spcoop.nodopagamentispc.servizi.pagamentitelematiciccp.PagamentiTelematiciCCP;
import it.govpay.bd.BasicBD;
import it.govpay.bd.anagrafica.AnagraficaManager;
import it.govpay.bd.pagamento.IuvBD;
import it.govpay.bd.pagamento.RptBD;
import it.govpay.bd.pagamento.VersamentiBD;
import it.govpay.core.business.GiornaleEventi;
import it.govpay.core.exceptions.GovPayException;
import it.govpay.core.exceptions.NdpException;
import it.govpay.core.exceptions.VersamentoAnnullatoException;
import it.govpay.core.exceptions.VersamentoDuplicatoException;
import it.govpay.core.exceptions.VersamentoScadutoException;
import it.govpay.core.exceptions.VersamentoSconosciutoException;
import it.govpay.core.exceptions.NdpException.FaultPa;
import it.govpay.core.utils.GovpayConfig;
import it.govpay.core.utils.GpContext;
import it.govpay.core.utils.GpThreadLocal;
import it.govpay.core.utils.RptUtils;
import it.govpay.core.utils.VersamentoUtils;
import it.govpay.core.utils.client.BasicClient.ClientException;
import it.govpay.model.Anagrafica;
import it.govpay.model.Canale;
import it.govpay.model.Dominio;
import it.govpay.model.Evento;
import it.govpay.model.IbanAccredito;
import it.govpay.model.Intermediario;
import it.govpay.model.Iuv;
import it.govpay.model.Psp;
import it.govpay.model.Rpt;
import it.govpay.model.Stazione;
import it.govpay.model.Versamento;
import it.govpay.model.Canale.TipoVersamento;
import it.govpay.model.Evento.TipoEvento;
import it.govpay.model.Iuv.TipoIUV;
import it.govpay.model.Rpt.FirmaRichiesta;
import it.govpay.model.Versamento.CausaleSemplice;
import it.govpay.model.Versamento.CausaleSpezzoni;
import it.govpay.model.Versamento.CausaleSpezzoniStrutturati;
import it.govpay.model.Versamento.StatoVersamento;
import it.govpay.servizi.gpprt.GpChiediListaVersamentiResponse.Versamento.SpezzoneCausaleStrutturata;

import javax.annotation.Resource;
import javax.jws.HandlerChain;
import javax.jws.WebService;
import javax.xml.ws.WebServiceContext;

import org.apache.cxf.annotations.SchemaValidation.SchemaValidationType;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openspcoop2.generic_project.exception.NotAuthorizedException;
import org.openspcoop2.generic_project.exception.NotFoundException;
import org.openspcoop2.generic_project.exception.ServiceException;
import org.openspcoop2.utils.logger.beans.Property;
import org.openspcoop2.utils.logger.beans.proxy.Actor;


@WebService(serviceName = "PagamentiTelematiciCCPservice",
endpointInterface = "it.gov.spcoop.nodopagamentispc.servizi.pagamentitelematiciccp.PagamentiTelematiciCCP",
targetNamespace = "http://NodoPagamentiSPC.spcoop.gov.it/servizi/PagamentiTelematiciCCP",
portName = "PPTPort",
wsdlLocation="classpath:wsdl/PaPerNodoPagamentoPsp.wsdl")

@HandlerChain(file="../../../../handler-chains/handler-chain-ndp.xml")

@org.apache.cxf.annotations.SchemaValidation(type = SchemaValidationType.IN)
public class PagamentiTelematiciCCPImpl implements PagamentiTelematiciCCP {

	@Resource
	WebServiceContext wsCtxt;

	private static Logger log = LogManager.getLogger();

	@Override
	public PaaAttivaRPTRisposta paaAttivaRPT(PaaAttivaRPT bodyrichiesta, IntestazionePPT header) {
		
		String codIntermediario = header.getIdentificativoIntermediarioPA();
		String codStazione = header.getIdentificativoStazioneIntermediarioPA();
		String codDominio = header.getIdentificativoDominio();
		String iuv = header.getIdentificativoUnivocoVersamento();
		String ccp = header.getCodiceContestoPagamento();
		
		GpContext ctx = GpThreadLocal.get();
		ctx.setCorrelationId(codDominio + iuv + ccp);
		
		Actor from = new Actor();
		from.setName("NodoDeiPagamentiSPC");
		from.setType(GpContext.TIPO_SOGGETTO_NDP);
		ctx.getTransaction().setFrom(from);
		
		Actor to = new Actor();
		to.setName(header.getIdentificativoStazioneIntermediarioPA());
		from.setType(GpContext.TIPO_SOGGETTO_STAZIONE);
		ctx.getTransaction().setTo(to);
		
		ctx.getContext().getRequest().addGenericProperty(new Property("ccp", ccp));
		ctx.getContext().getRequest().addGenericProperty(new Property("codDominio", codDominio));
		ctx.getContext().getRequest().addGenericProperty(new Property("iuv", iuv));
		ctx.log("ccp.ricezioneAttiva");
		
		BasicBD bd = null;
		PaaAttivaRPTRisposta response = new PaaAttivaRPTRisposta();
		log.info("Ricevuta richiesta di attiva RPT [" + codIntermediario + "][" + codStazione + "][" + codDominio + "][" + iuv + "][" + ccp + "]");
		
		Evento evento = new Evento();
		evento.setCodStazione(codStazione);
		evento.setCodDominio(codDominio);
		evento.setIuv(iuv);
		evento.setCcp(ccp);
		evento.setTipoEvento(TipoEvento.paaAttivaRPT);
		evento.setCodPsp(bodyrichiesta.getIdentificativoPSP());
		evento.setTipoVersamento(TipoVersamento.ATTIVATO_PRESSO_PSP);
		evento.setFruitore("NodoDeiPagamentiSPC");
		try {
			bd = BasicBD.newInstance(GpThreadLocal.get().getTransactionId());

			String principal = getPrincipal();
			if(principal == null && GovpayConfig.getInstance().isPddAuthEnable()) {
				ctx.log("ccp.erroreNoAutorizzazione");
				throw new NotAuthorizedException("Autorizzazione fallita: principal non fornito");
			}
				
			
			Intermediario intermediario = null;
			try {
				intermediario = AnagraficaManager.getIntermediario(bd, codIntermediario);
				
				// Controllo autorizzazione
				if(GovpayConfig.getInstance().isPddAuthEnable() && !principal.equals(intermediario.getConnettorePdd().getPrincipal())){
					ctx.log("ccp.erroreAutorizzazione", principal);
					throw new NotAuthorizedException("Autorizzazione fallita: principal fornito non corrisponde all'intermediario " + codIntermediario);
				}
				
				evento.setErogatore(intermediario.getDenominazione());
			} catch (NotFoundException e) {
				throw new NdpException(FaultPa.PAA_ID_INTERMEDIARIO_ERRATO, codDominio);
			}

			Stazione stazione = null;
			try {
				stazione = AnagraficaManager.getStazione(bd, codStazione);
			} catch (NotFoundException e) {
				throw new NdpException(FaultPa.PAA_STAZIONE_INT_ERRATA, codDominio);
			}

			Dominio dominio;
			try {
				dominio = AnagraficaManager.getDominio(bd, codDominio);
			} catch (NotFoundException e) {
				throw new NdpException(FaultPa.PAA_ID_DOMINIO_ERRATO, codDominio);
			}

			IuvBD iuvBD = new IuvBD(bd);
			Iuv iuvModel = null;
			try {
				iuvModel = iuvBD.getIuv(dominio.getId(), iuv);
			} catch (NotFoundException e) {
				// iuv non trovato... se non ho una applicazione di default il pagamento e' sconosciuto.
				if(dominio.getIdApplicazioneDefault() == null) {
					throw new NdpException(FaultPa.PAA_PAGAMENTO_SCONOSCIUTO, codDominio);
				}
			}
			
			VersamentiBD versamentiBD = new VersamentiBD(bd);
			Versamento versamento = null;
			try {
				try {
					// Se non ho lo iuv, vado direttamente a chiedere all'applicazione di default
					if(iuvModel == null) throw new NotFoundException();
					
					versamento = versamentiBD.getVersamento(iuvModel.getIdApplicazione(), iuvModel.getCodVersamentoEnte());

					// Versamento trovato, gestisco un'eventuale scadenza
					versamento = VersamentoUtils.aggiornaVersamento(versamento, bd);

					if(versamento.getStatoVersamento().equals(StatoVersamento.ANNULLATO))
						throw new NdpException(FaultPa.PAA_PAGAMENTO_ANNULLATO, codDominio);

					if(!versamento.getStatoVersamento().equals(StatoVersamento.NON_ESEGUITO))
						throw new NdpException(FaultPa.PAA_PAGAMENTO_DUPLICATO, codDominio);

				} catch (NotFoundException e) {
					// Versamento non trovato, devo interrogare l'applicazione.
					if(iuvModel != null)
						versamento = VersamentoUtils.acquisisciVersamento(AnagraficaManager.getApplicazione(bd, iuvModel.getIdApplicazione()), iuvModel.getCodVersamentoEnte(), iuv, bd);
					else {
						versamento = VersamentoUtils.acquisisciVersamento(AnagraficaManager.getApplicazione(bd, dominio.getIdApplicazioneDefault()), null, iuv, bd);
						iuvModel = new it.govpay.core.business.Iuv(bd).caricaIUV(versamento.getApplicazione(bd), dominio, iuv, TipoIUV.NUMERICO, versamento.getCodVersamentoEnte());
					}
				}
			} catch (VersamentoScadutoException e1) {
				throw new NdpException(FaultPa.PAA_PAGAMENTO_SCADUTO, codDominio);
			} catch (VersamentoAnnullatoException e1) {
				throw new NdpException(FaultPa.PAA_PAGAMENTO_ANNULLATO, codDominio);
			} catch (VersamentoDuplicatoException e1) {
				throw new NdpException(FaultPa.PAA_PAGAMENTO_DUPLICATO, codDominio);
			} catch (VersamentoSconosciutoException e1) {
				throw new NdpException(FaultPa.PAA_PAGAMENTO_SCONOSCIUTO, codDominio);
			} catch (ClientException e1) {
				if(iuvModel != null)
					throw new ServiceException("Errore nella attivazione del versamento dell'applicazione (" + AnagraficaManager.getApplicazione(bd, iuvModel.getIdApplicazione()).getCodApplicazione() + ").", e1);
				else
					throw new ServiceException("Errore nella attivazione del versamento dell'applicazione (" + AnagraficaManager.getApplicazione(bd, dominio.getIdApplicazioneDefault()).getCodApplicazione() + ").", e1);
			} catch (GovPayException e1) {
				if(iuvModel != null)
					throw new ServiceException("Errore nella attivazione del versamento dell'applicazione (" + AnagraficaManager.getApplicazione(bd, iuvModel.getIdApplicazione()).getCodApplicazione() + ").", e1);
				else
					throw new ServiceException("Errore nella attivazione del versamento dell'applicazione (" + AnagraficaManager.getApplicazione(bd, dominio.getIdApplicazioneDefault()).getCodApplicazione() + ").", e1);
			}

			// Verifico l'importo
			if(bodyrichiesta.getDatiPagamentoPSP().getImportoSingoloVersamento().compareTo(versamento.getImportoTotale()) != 0)
				throw new NdpException(FaultPa.PAA_ATTIVA_RPT_IMPORTO_NON_VALIDO, codDominio);
			
			// Verifico che abbia un solo singolo versamento
			if(versamento.getSingoliVersamenti(bd).size() != 1) {
				throw new ServiceException("Errore nella attivazione del versamento dell'applicazione (" + AnagraficaManager.getApplicazione(bd, iuvModel.getIdApplicazione()).getCodApplicazione() + "). Il versamento contiente piu' di un singolo versamento, non ammesso per pagamenti ad iniziativa psp.");
			}
			
			// Identificazione del Psp e del canale
			Psp psp;
			try {
				psp = AnagraficaManager.getPsp(bd, bodyrichiesta.getIdentificativoPSP());
			} catch (NotFoundException e) {
				throw new ServiceException("Psp (" + bodyrichiesta.getIdentificativoPSP() + ") non censito in anagrafica.");
			}
			
			Canale canale = null;
			try {
				canale = AnagraficaManager.getCanale(bd, bodyrichiesta.getIdentificativoPSP(), bodyrichiesta.getIdentificativoCanalePSP(), TipoVersamento.ATTIVATO_PRESSO_PSP);
				evento.setCodCanale(canale.getCodCanale());
			}catch (NotFoundException e) {
				throw new ServiceException("Psp (" + bodyrichiesta.getIdentificativoPSP() + ") non ha canali censiti per pagamenti ad iniziativa psp.");
			}
			
			// Creazione dell'RPT
			Anagrafica versante = toOrm(bodyrichiesta.getDatiPagamentoPSP().getSoggettoVersante());
			Rpt rpt = RptUtils.buildRpt(intermediario, stazione, null, versamento, iuvModel, ccp, null, psp, canale, versante, StAutenticazioneSoggetto.N_A.value(), null, null, bd);
			
			ctx.log("ccp.attivazione", rpt.getCodMsgRichiesta());
			
			// Da specifica, le RPT ad iniziativa PSP non possono richiedere firma
			rpt.setFirmaRichiesta(FirmaRichiesta.NESSUNA);

			RptBD rptBD = new RptBD(bd);
			
			bd.setAutoCommit(false);
			bd.enableSelectForUpdate();
			
			// Controllo se gia' non esiste la RPT (lo devo fare solo adesso per essere in transazione con l'inserimento)
			try {
				rptBD.getRpt(codDominio, iuv, ccp);
				throw new NdpException(FaultPa.PAA_PAGAMENTO_IN_CORSO, codDominio);
			} catch (NotFoundException e2) {
				// L'RPT non esiste, procedo
				rptBD.insertRpt(rpt);
				RptUtils.inviaRPTAsync(rpt, bd);
			}
			
			bd.commit();
			
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
						SpezzoneCausaleStrutturata spezzone = new SpezzoneCausaleStrutturata();
						spezzone.setCausale(causale.getSpezzoni().get(i));
						spezzone.setImporto(causale.getImporti().get(i));
						datiPagamento.getSpezzoniCausaleVersamento().getSpezzoneCausaleVersamentoOrSpezzoneStrutturatoCausaleVersamento().add(spezzone);
					}
				}
			}
			datiPagamento.setEnteBeneficiario(RptUtils.buildEnteBeneficiario(dominio, versamento.getUo(bd), bd));
			IbanAccredito ibanAccredito = versamento.getSingoliVersamenti(bd).get(0).getIbanAccredito(bd);
			if(ibanAccredito != null) {
				datiPagamento.setBicAccredito(ibanAccredito.getCodBicAccredito());
				datiPagamento.setIbanAccredito(ibanAccredito.getCodIban());
			}
			esito.setDatiPagamentoPA(datiPagamento);
			response.setPaaAttivaRPTRisposta(esito);
			ctx.log("ccp.attivazioneInfo", datiPagamento.getImportoSingoloVersamento().toString(), datiPagamento.getIbanAccredito(), versamento.getCausaleVersamento().toString());
			ctx.log("ccp.ricezioneAttivaOk");
		} catch (NdpException e) {
			if(bd != null) bd.rollback();
			response = buildRisposta(e, response);
			String faultDescription = response.getPaaAttivaRPTRisposta().getFault().getDescription() == null ? "<Nessuna descrizione>" : response.getPaaAttivaRPTRisposta().getFault().getDescription(); 
			ctx.log("ccp.ricezioneAttivaKo", response.getPaaAttivaRPTRisposta().getFault().getFaultCode(), response.getPaaAttivaRPTRisposta().getFault().getFaultString(), faultDescription);
		} catch (Exception e) {
			if(bd != null) bd.rollback();
			response = buildRisposta(e, codDominio, response);
			String faultDescription = response.getPaaAttivaRPTRisposta().getFault().getDescription() == null ? "<Nessuna descrizione>" : response.getPaaAttivaRPTRisposta().getFault().getDescription(); 
			ctx.log("ccp.ricezioneAttivaKo", response.getPaaAttivaRPTRisposta().getFault().getFaultCode(), response.getPaaAttivaRPTRisposta().getFault().getFaultString(), faultDescription);
		} finally {
			if(bd != null) {
				GiornaleEventi ge = new GiornaleEventi(bd);
				evento.setEsito(response.getPaaAttivaRPTRisposta().getEsito());
				evento.setDataRisposta(new Date());
				ge.registraEvento(evento);
			}
			
			if(ctx != null) {
				ctx.setResult(response.getPaaAttivaRPTRisposta().getFault() == null ? null : response.getPaaAttivaRPTRisposta().getFault().getFaultCode());
				ctx.log();
			}
			
			if(bd != null) bd.closeConnection();
		}
		return response;
	}

	

	@Override
	public PaaVerificaRPTRisposta paaVerificaRPT(PaaVerificaRPT bodyrichiesta, IntestazionePPT header) {
		String codIntermediario = header.getIdentificativoIntermediarioPA();
		String codStazione = header.getIdentificativoStazioneIntermediarioPA();
		String codDominio = header.getIdentificativoDominio();
		String iuv = header.getIdentificativoUnivocoVersamento();
		String ccp = header.getCodiceContestoPagamento();
		
		
		GpContext ctx = GpThreadLocal.get();
		ctx.setCorrelationId(codDominio + iuv + ccp);
		
		Actor from = new Actor();
		from.setName("NodoDeiPagamentiSPC");
		from.setType(GpContext.TIPO_SOGGETTO_NDP);
		ctx.getTransaction().setFrom(from);
		
		Actor to = new Actor();
		to.setName(header.getIdentificativoStazioneIntermediarioPA());
		from.setType(GpContext.TIPO_SOGGETTO_STAZIONE);
		ctx.getTransaction().setTo(to);
		
		ctx.getContext().getRequest().addGenericProperty(new Property("ccp", ccp));
		ctx.getContext().getRequest().addGenericProperty(new Property("codDominio", codDominio));
		ctx.getContext().getRequest().addGenericProperty(new Property("iuv", iuv));
		ctx.log("ccp.ricezioneVerifica");
		
		
		log.info("Ricevuta richiesta di verifica RPT [" + codIntermediario + "][" + codStazione + "][" + codDominio + "][" + iuv + "][" + ccp + "]");
		BasicBD bd = null;
		PaaVerificaRPTRisposta response = new PaaVerificaRPTRisposta();
		
		Evento evento = new Evento();
		evento.setCodStazione(codStazione);
		evento.setCodDominio(codDominio);
		evento.setIuv(iuv);
		evento.setCcp(ccp);
		evento.setTipoEvento(TipoEvento.paaAttivaRPT);
		evento.setCodPsp(bodyrichiesta.getIdentificativoPSP());
		evento.setTipoVersamento(TipoVersamento.ATTIVATO_PRESSO_PSP);
		evento.setFruitore("NodoDeiPagamentiSPC");
		try {
			bd = BasicBD.newInstance(GpThreadLocal.get().getTransactionId());

			String principal = getPrincipal();
			if(GovpayConfig.getInstance().isPddAuthEnable() && principal == null) {
				ctx.log("ccp.erroreNoAutorizzazione");
				throw new NotAuthorizedException("Autorizzazione fallita: principal non fornito");
			}
			
			Intermediario intermediario = null;
			try {
				intermediario = AnagraficaManager.getIntermediario(bd, codIntermediario);
				
				// Controllo autorizzazione
				if(GovpayConfig.getInstance().isPddAuthEnable() && !principal.equals(intermediario.getConnettorePdd().getPrincipal())){
					ctx.log("ccp.erroreAutorizzazione", principal);
					throw new NotAuthorizedException("Autorizzazione fallita: principal fornito non corrisponde all'intermediario " + codIntermediario);
				}

				evento.setErogatore(intermediario.getDenominazione());
			} catch (NotFoundException e) {
				throw new NdpException(FaultPa.PAA_ID_INTERMEDIARIO_ERRATO, codDominio);
			}

			try {
				AnagraficaManager.getStazione(bd, codStazione);
			} catch (NotFoundException e) {
				throw new NdpException(FaultPa.PAA_STAZIONE_INT_ERRATA, codDominio);
			}

			Dominio dominio;
			try {
				dominio = AnagraficaManager.getDominio(bd, codDominio);
			} catch (NotFoundException e) {
				throw new NdpException(FaultPa.PAA_ID_DOMINIO_ERRATO, codDominio);
			}

			IuvBD iuvBD = new IuvBD(bd);
			Iuv iuvModel = null;
			try {
				iuvModel = iuvBD.getIuv(dominio.getId(), iuv);
				ctx.log("ccp.iuvPresente", iuvModel.getCodVersamentoEnte());
			} catch (NotFoundException e) {
				// iuv non trovato... se non ho una applicazione di default il pagamento e' sconosciuto.
				if(dominio.getIdApplicazioneDefault() == null) {
					ctx.log("ccp.iuvNonPresenteNoAppDefault");
					throw new NdpException(FaultPa.PAA_PAGAMENTO_SCONOSCIUTO, codDominio);
				}
				ctx.log("ccp.iuvNonPresente", AnagraficaManager.getApplicazione(bd, dominio.getIdApplicazioneDefault()).getCodApplicazione());
			}
			
			VersamentiBD versamentiBD = new VersamentiBD(bd);
			Versamento versamento = null;
			try {
				try {
					// Se non ho lo iuv, vado direttamente a chiedere all'applicazione di default
					if(iuvModel == null) {
						// Se non ho lo iuv, vado direttamente a chiedere all'applicazione di default
						throw new NotFoundException();
					}
					versamento = versamentiBD.getVersamento(iuvModel.getIdApplicazione(), iuvModel.getCodVersamentoEnte());

					// Versamento trovato, gestisco un'eventuale scadenza
					versamento = VersamentoUtils.aggiornaVersamento(versamento, bd);

					if(versamento.getStatoVersamento().equals(StatoVersamento.ANNULLATO))
						throw new NdpException(FaultPa.PAA_PAGAMENTO_ANNULLATO, codDominio);

					if(!versamento.getStatoVersamento().equals(StatoVersamento.NON_ESEGUITO))
						throw new NdpException(FaultPa.PAA_PAGAMENTO_DUPLICATO, codDominio);

				} catch (NotFoundException e) {
					// Versamento non trovato, devo interrogare l'applicazione.
					ctx.log("ccp.versamentoNonPresente");
					if(iuvModel != null) {
						versamento = VersamentoUtils.acquisisciVersamento(AnagraficaManager.getApplicazione(bd, iuvModel.getIdApplicazione()), iuvModel.getCodVersamentoEnte(), iuv, bd);
					} else {
						versamento = VersamentoUtils.acquisisciVersamento(AnagraficaManager.getApplicazione(bd, dominio.getIdApplicazioneDefault()), null, iuv, bd);
						iuvModel = new it.govpay.core.business.Iuv(bd).caricaIUV(versamento.getApplicazione(bd), dominio, iuv, TipoIUV.NUMERICO, versamento.getCodVersamentoEnte());
					}
					ctx.log("ccp.versamentoAcquisito");
				}
			} catch (VersamentoScadutoException e1) {
				throw new NdpException(FaultPa.PAA_PAGAMENTO_SCADUTO, codDominio);
			} catch (VersamentoAnnullatoException e1) {
				throw new NdpException(FaultPa.PAA_PAGAMENTO_ANNULLATO, codDominio);
			} catch (VersamentoDuplicatoException e1) {
				throw new NdpException(FaultPa.PAA_PAGAMENTO_DUPLICATO, codDominio);
			} catch (VersamentoSconosciutoException e1) {
				throw new NdpException(FaultPa.PAA_PAGAMENTO_SCONOSCIUTO, codDominio);
			} catch (ClientException e1) {
				if(iuvModel != null)
					throw new ServiceException("Errore nella verifica del versamento dell'applicazione (" + AnagraficaManager.getApplicazione(bd, iuvModel.getIdApplicazione()).getCodApplicazione() + ").", e1);
				else
					throw new ServiceException("Errore nella verifica del versamento dell'applicazione (" + AnagraficaManager.getApplicazione(bd, dominio.getIdApplicazioneDefault()).getCodApplicazione() + ").", e1);
			} catch (GovPayException e1) {
				if(iuvModel != null)
					throw new ServiceException("Errore nella verifica del versamento dell'applicazione (" + AnagraficaManager.getApplicazione(bd, iuvModel.getIdApplicazione()).getCodApplicazione() + ").", e1);
				else
					throw new ServiceException("Errore nella verifica del versamento dell'applicazione (" + AnagraficaManager.getApplicazione(bd, dominio.getIdApplicazioneDefault()).getCodApplicazione() + ").", e1);
			}
			
			// Verifico che abbia un solo singolo versamento
			if(versamento.getSingoliVersamenti(bd).size() != 1) {
				throw new ServiceException("Errore nella verifica del versamento dell'applicazione (" + AnagraficaManager.getApplicazione(bd, iuvModel.getIdApplicazione()).getCodApplicazione() + "). Il versamento contiente piu' di un singolo versamento, non ammesso per pagamenti ad iniziativa psp.");
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
						SpezzoneCausaleStrutturata spezzone = new SpezzoneCausaleStrutturata();
						spezzone.setCausale(causale.getSpezzoni().get(i));
						spezzone.setImporto(causale.getImporti().get(i));
						datiPagamento.getSpezzoniCausaleVersamento().getSpezzoneCausaleVersamentoOrSpezzoneStrutturatoCausaleVersamento().add(spezzone);
					}
				}
			}

			datiPagamento.setEnteBeneficiario(RptUtils.buildEnteBeneficiario(dominio, versamento.getUo(bd), bd));
			IbanAccredito ibanAccredito = versamento.getSingoliVersamenti(bd).get(0).getIbanAccredito(bd);
			if(ibanAccredito != null) {
				datiPagamento.setBicAccredito(ibanAccredito.getCodBicAccredito());
				datiPagamento.setIbanAccredito(ibanAccredito.getCodIban());
			}
			esito.setDatiPagamentoPA(datiPagamento);
			response.setPaaVerificaRPTRisposta(esito);
			ctx.log("ccp.verificaInfo", datiPagamento.getImportoSingoloVersamento().toString(), datiPagamento.getIbanAccredito(), versamento.getCausaleVersamento().toString());
			ctx.log("ccp.ricezioneVerificaOk");
		} catch (NdpException e) {
			if(bd != null) bd.rollback();
			response = buildRisposta(e, response);
			String faultDescription = response.getPaaVerificaRPTRisposta().getFault().getDescription() == null ? "<Nessuna descrizione>" : response.getPaaVerificaRPTRisposta().getFault().getDescription(); 
			ctx.log("ccp.ricezioneVerificaKo", response.getPaaVerificaRPTRisposta().getFault().getFaultCode(), response.getPaaVerificaRPTRisposta().getFault().getFaultString(), faultDescription);
		} catch (Exception e) {
			if(bd != null) bd.rollback();
			response = buildRisposta(e, codDominio, response);
			String faultDescription = response.getPaaVerificaRPTRisposta().getFault().getDescription() == null ? "<Nessuna descrizione>" : response.getPaaVerificaRPTRisposta().getFault().getDescription(); 
			ctx.log("ccp.ricezioneVerificaKo", response.getPaaVerificaRPTRisposta().getFault().getFaultCode(), response.getPaaVerificaRPTRisposta().getFault().getFaultString(), faultDescription);
		} finally {
			if(bd != null) {
				GiornaleEventi ge = new GiornaleEventi(bd);
				evento.setEsito(response.getPaaVerificaRPTRisposta().getEsito());
				evento.setDataRisposta(new Date());
				ge.registraEvento(evento);
			}
			
			if(ctx != null) {
				ctx.setResult(response.getPaaVerificaRPTRisposta().getFault() == null ? null : response.getPaaVerificaRPTRisposta().getFault().getFaultCode());
				ctx.log();
			}
			
			if(bd != null) bd.closeConnection();
		}
		return response;
	}


	private Anagrafica toOrm(CtSoggettoVersante soggettoVersante) {
		if(soggettoVersante == null) return null;
		Anagrafica anagrafica = new Anagrafica();
		anagrafica.setCap(soggettoVersante.getCapVersante());
		anagrafica.setCivico(soggettoVersante.getCivicoVersante());
		anagrafica.setCodUnivoco(soggettoVersante.getIdentificativoUnivocoVersante().getCodiceIdentificativoUnivoco());
		anagrafica.setEmail(soggettoVersante.getEMailVersante());
		anagrafica.setIndirizzo(soggettoVersante.getIndirizzoVersante());
		anagrafica.setLocalita(soggettoVersante.getLocalitaVersante());
		anagrafica.setNazione(soggettoVersante.getNazioneVersante());
		anagrafica.setProvincia(soggettoVersante.getProvinciaVersante());
		anagrafica.setRagioneSociale(soggettoVersante.getAnagraficaVersante());
		return anagrafica;
	}

	private <T> T buildRisposta(Exception e, String codDominio, T risposta) {
		log.error("Errore interno in verifica/attivazione RPT.", e);
		return buildRisposta(new NdpException(FaultPa.PAA_SYSTEM_ERROR, codDominio), risposta);
	}

	private <T> T buildRisposta(NdpException e, T risposta) {
		if(risposta instanceof PaaAttivaRPTRisposta) {
			log.error("Rifiutata Attiva RPT con Fault " + e.getFault().toString() + ( e.getDescrizione() != null ? (": " + e.getDescrizione()) : ""));
			PaaAttivaRPTRisposta r = (PaaAttivaRPTRisposta) risposta;
			EsitoAttivaRPT esito = new EsitoAttivaRPT();
			esito.setEsito("KO");
			FaultBean fault = new FaultBean();
			fault.setId(e.getCodDominio());
			fault.setFaultCode(e.getFault().toString());
			fault.setFaultString(e.getFault().getFaultString());
			fault.setDescription(e.getDescrizione());
			esito.setFault(fault);
			r.setPaaAttivaRPTRisposta(esito);
		}

		if(risposta instanceof PaaVerificaRPTRisposta) {
			log.error("Rifiutata Verifica RPT con Fault " + e.getFault().toString() + ( e.getDescrizione() != null ? (": " + e.getDescrizione()) : ""));
			PaaVerificaRPTRisposta r = (PaaVerificaRPTRisposta) risposta;
			EsitoVerificaRPT esito = new EsitoVerificaRPT();
			esito.setEsito("KO");
			FaultBean fault = new FaultBean();
			fault.setId(e.getCodDominio());
			fault.setFaultCode(e.getFault().toString());
			fault.setFaultString(e.getFault().getFaultString());
			fault.setDescription(e.getDescrizione());
			esito.setFault(fault);
			r.setPaaVerificaRPTRisposta(esito);
		}

		return risposta;
	}
	
	private String getPrincipal() throws GovPayException {
		if(wsCtxt.getUserPrincipal() == null) {
			return null;
		}

		return wsCtxt.getUserPrincipal().getName();
	}
}
