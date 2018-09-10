/*
 * GovPay - Porta di Accesso al Nodo dei Pagamenti SPC 
 * http://www.gov4j.it/govpay
 * 
 * Copyright (c) 2014-2017 Link.it srl (http://www.link.it).
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

import javax.annotation.Resource;
import javax.jws.HandlerChain;
import javax.jws.WebService;
import javax.servlet.http.HttpServletRequest;
import javax.xml.ws.WebServiceContext;
import javax.xml.ws.handler.MessageContext;

import org.apache.cxf.annotations.SchemaValidation.SchemaValidationType;
import org.openspcoop2.generic_project.exception.NotAuthorizedException;
import org.openspcoop2.generic_project.exception.NotFoundException;
import org.openspcoop2.utils.LoggerWrapperFactory;
import org.openspcoop2.utils.logger.beans.Property;
import org.openspcoop2.utils.logger.beans.proxy.Actor;
import org.slf4j.Logger;

import gov.telematici.pagamenti.ws.ppthead.IntestazionePPT;
import it.gov.digitpa.schemas._2011.ws.psp.CtSpezzoneStrutturatoCausaleVersamento;
import it.gov.digitpa.schemas._2011.ws.psp.CtSpezzoniCausaleVersamento;
import it.gov.digitpa.schemas._2011.ws.psp.EsitoAttivaRPT;
import it.gov.digitpa.schemas._2011.ws.psp.EsitoVerificaRPT;
import it.gov.digitpa.schemas._2011.ws.psp.FaultBean;
import it.gov.digitpa.schemas._2011.ws.psp.PaaAttivaRPT;
import it.gov.digitpa.schemas._2011.ws.psp.PaaAttivaRPTRisposta;
import it.gov.digitpa.schemas._2011.ws.psp.PaaTipoDatiPagamentoPA;
import it.gov.digitpa.schemas._2011.ws.psp.PaaVerificaRPT;
import it.gov.digitpa.schemas._2011.ws.psp.PaaVerificaRPTRisposta;
import it.gov.spcoop.nodopagamentispc.servizi.pagamentitelematiciccp.PagamentiTelematiciCCP;
import it.govpay.bd.BasicBD;
import it.govpay.bd.anagrafica.AnagraficaManager;
import it.govpay.bd.model.Dominio;
import it.govpay.bd.model.Pagamento;
import it.govpay.bd.model.PagamentoPortale;
import it.govpay.bd.model.PagamentoPortale.CODICE_STATO;
import it.govpay.bd.model.PagamentoPortale.STATO;
import it.govpay.bd.model.Rpt;
import it.govpay.bd.model.SingoloVersamento;
import it.govpay.bd.model.Utenza;
import it.govpay.bd.model.Versamento;
import it.govpay.bd.pagamento.PagamentiBD;
import it.govpay.bd.pagamento.PagamentiPortaleBD;
import it.govpay.bd.pagamento.RptBD;
import it.govpay.bd.pagamento.VersamentiBD;
import it.govpay.core.business.Applicazione;
import it.govpay.core.business.GiornaleEventi;
import it.govpay.core.exceptions.GovPayException;
import it.govpay.core.exceptions.NdpException;
import it.govpay.core.exceptions.NdpException.FaultPa;
import it.govpay.core.exceptions.VersamentoAnnullatoException;
import it.govpay.core.exceptions.VersamentoDuplicatoException;
import it.govpay.core.exceptions.VersamentoScadutoException;
import it.govpay.core.exceptions.VersamentoSconosciutoException;
import it.govpay.core.rs.v1.costanti.EsitoOperazione;
import it.govpay.core.utils.CredentialUtils;
import it.govpay.core.utils.GovpayConfig;
import it.govpay.core.utils.GpContext;
import it.govpay.core.utils.GpThreadLocal;
import it.govpay.core.utils.RptBuilder;
import it.govpay.core.utils.RptUtils;
import it.govpay.core.utils.VersamentoUtils;
import it.govpay.core.utils.client.BasicClient.ClientException;
import it.govpay.model.Canale.TipoVersamento;
import it.govpay.model.Evento;
import it.govpay.model.Evento.TipoEvento;
import it.govpay.model.IbanAccredito;
import it.govpay.model.Intermediario;
import it.govpay.model.Iuv.TipoIUV;
import it.govpay.model.Versamento.CausaleSemplice;
import it.govpay.model.Versamento.CausaleSpezzoni;
import it.govpay.model.Versamento.CausaleSpezzoniStrutturati;
import it.govpay.model.Versamento.StatoVersamento;
import it.govpay.orm.IdVersamento;


@WebService(serviceName = "PagamentiTelematiciCCPservice",
endpointInterface = "it.gov.spcoop.nodopagamentispc.servizi.pagamentitelematiciccp.PagamentiTelematiciCCP",
targetNamespace = "http://NodoPagamentiSPC.spcoop.gov.it/servizi/PagamentiTelematiciCCP",
portName = "PPTPort",
wsdlLocation="/wsdl/PaPerNodoPagamentoPsp.wsdl")

@HandlerChain(file="../../../../handler-chains/handler-chain-ndp.xml")

@org.apache.cxf.annotations.SchemaValidation(type = SchemaValidationType.IN)
public class PagamentiTelematiciCCPImpl implements PagamentiTelematiciCCP {

	@Resource
	WebServiceContext wsCtxt;

	private static Logger log = LoggerWrapperFactory.getLogger(PagamentiTelematiciCCPImpl.class);
	private static SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");

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
		ctx.getContext().getRequest().addGenericProperty(new Property("codPsp", bodyrichiesta.getIdentificativoPSP()));
		ctx.getContext().getRequest().addGenericProperty(new Property("codCanale", bodyrichiesta.getIdentificativoCanalePSP()));
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

			Utenza user = this.getUtenzaAutenticata();
			if(GovpayConfig.getInstance().isPddAuthEnable() && user.getPrincipal() == null) {
				ctx.log("ccp.erroreNoAutorizzazione");
				throw new NotAuthorizedException("Autorizzazione fallita: principal non fornito");
			}


			Intermediario intermediario = null;
			try {
				intermediario = AnagraficaManager.getIntermediario(bd, codIntermediario);

				// Controllo autorizzazione
				if(GovpayConfig.getInstance().isPddAuthEnable()){
					boolean authOk = false;
					
					if(user.isCheckSubject()) {
						// check tra subject
						authOk = CredentialUtils.checkSubject(intermediario.getConnettorePdd().getPrincipal(), user.getPrincipal());
					} else {
						authOk = user.getPrincipal().equals(intermediario.getConnettorePdd().getPrincipal());
					}
					
					if(!authOk) {
						ctx.log("ccp.erroreAutorizzazione", user.getPrincipal());
						throw new NotAuthorizedException("Autorizzazione fallita: principal fornito non corrisponde all'intermediario " + codIntermediario);
					}
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
			
			VersamentiBD versamentiBD = new VersamentiBD(bd);
			Versamento versamento = null;
			it.govpay.bd.model.Applicazione applicazioneGestisceIuv = null;
			try {
				versamento = versamentiBD.getVersamento(codDominio, iuv);
			}catch (NotFoundException e) {
				applicazioneGestisceIuv = new Applicazione(bd).getApplicazioneDominio(dominio,iuv); 
				
				if(applicazioneGestisceIuv == null) {
					throw new NdpException(FaultPa.PAA_PAGAMENTO_SCONOSCIUTO, codDominio);
				}
			}

			try {
				try {
					// Se non ho lo iuv, vado direttamente a chiedere all'applicazione di default
					if(versamento == null) throw new NotFoundException();

					// Versamento trovato, gestisco un'eventuale scadenza
					versamento = VersamentoUtils.aggiornaVersamento(versamento, bd);

					if(versamento.getStatoVersamento().equals(StatoVersamento.ANNULLATO))
						throw new NdpException(FaultPa.PAA_PAGAMENTO_ANNULLATO, codDominio);

					if(!versamento.getStatoVersamento().equals(StatoVersamento.NON_ESEGUITO)) {
						
						if(versamento.getStatoVersamento().equals(StatoVersamento.ANOMALO))
							throw new NdpException(FaultPa.PAA_PAGAMENTO_DUPLICATO, "Il pagamento risulta gi\u00E0 effettuato, ma si riscontrano anomalie negli importi. Per maggiori informazioni contattare il supporto clienti.", codDominio);
						
						if(versamento.getStatoVersamento().equals(StatoVersamento.ESEGUITO) || versamento.getStatoVersamento().equals(StatoVersamento.ESEGUITO_SENZA_RPT)) {
							PagamentiBD pagamentiBD = new PagamentiBD(bd);
							List<Pagamento> pagamenti = pagamentiBD.getPagamentiBySingoloVersamento(versamento.getSingoliVersamenti(bd).get(0).getId());
							if(pagamenti.isEmpty())
								throw new NdpException(FaultPa.PAA_PAGAMENTO_DUPLICATO, codDominio);
							else {
								Pagamento pagamento = pagamenti.get(0);
								throw new NdpException(FaultPa.PAA_PAGAMENTO_DUPLICATO, "Il pagamento risulta gi\u00E0 effettuato in data " + sdf.format(pagamento.getDataPagamento()) + " [Iur:" + pagamento.getIur() + "]", codDominio);
							}
						}
					}
				} catch (NotFoundException e) {
					//Versamento non trovato, devo interrogare l'applicazione.
					// prendo tutte le applicazioni che gestiscono il dominio, tra queste cerco la prima che match la regexpr dello iuv la utilizzo per far acquisire il versamento
					if(applicazioneGestisceIuv == null) {
						applicazioneGestisceIuv = new Applicazione(bd).getApplicazioneDominio(dominio,iuv); 
					}
					
					ctx.log("ccp.versamentoIuvNonPresente", applicazioneGestisceIuv.getCodApplicazione(), dominio.getCodDominio(), iuv);
					versamento = VersamentoUtils.acquisisciVersamento(AnagraficaManager.getApplicazione(bd, applicazioneGestisceIuv.getCodApplicazione()), null, null, null, codDominio, iuv, bd);
					new it.govpay.core.business.Iuv(bd).caricaIUV(versamento.getApplicazione(bd), dominio, iuv, TipoIUV.NUMERICO, versamento.getCodVersamentoEnte());
					ctx.log("ccp.versamentoIuvNonPresenteOk", applicazioneGestisceIuv.getCodApplicazione(), dominio.getCodDominio(), iuv);
				}
			} catch (VersamentoScadutoException e1) {
				throw new NdpException(FaultPa.PAA_PAGAMENTO_SCADUTO, e1.getMessage(), codDominio);
			} catch (VersamentoAnnullatoException e1) {
				throw new NdpException(FaultPa.PAA_PAGAMENTO_ANNULLATO, e1.getMessage(), codDominio);
			} catch (VersamentoDuplicatoException e1) {
				throw new NdpException(FaultPa.PAA_PAGAMENTO_DUPLICATO, e1.getMessage(), codDominio);
			} catch (VersamentoSconosciutoException e1) {
				throw new NdpException(FaultPa.PAA_PAGAMENTO_SCONOSCIUTO, codDominio);
			} catch (ClientException e1) {
				throw new NdpException(FaultPa.PAA_SYSTEM_ERROR, "Riscontrato errore durante l'acquisizione del versamento dall'applicazione gestore del debito: " + e1, codDominio, e1);
			} catch (GovPayException e1) {
				throw new NdpException(FaultPa.PAA_SYSTEM_ERROR, "Riscontrato errore durante l'attivazione del versamento: " + e1, codDominio, e1);
			}
			
			// Verifico l'importo
			if(bodyrichiesta.getDatiPagamentoPSP().getImportoSingoloVersamento().compareTo(versamento.getImportoTotale()) != 0)
				throw new NdpException(FaultPa.PAA_ATTIVA_RPT_IMPORTO_NON_VALIDO, "L'importo attivato [" + bodyrichiesta.getDatiPagamentoPSP().getImportoSingoloVersamento() + "] non corrisponde all'importo del versamento [" + versamento.getImportoTotale() + "]", codDominio);
			
			// Verifico che abbia un solo singolo versamento
			if(versamento.getSingoliVersamenti(bd).size() != 1) {
				throw new NdpException(FaultPa.PAA_SEMANTICA, "Il versamento contiente piu' di un singolo versamento, non ammesso per pagamenti ad iniziativa psp.", codDominio);
			}

			// Identificazione del Psp e del canale

			evento.setCodPsp(bodyrichiesta.getIdentificativoPSP());
			evento.setCodCanale(bodyrichiesta.getIdentificativoCanalePSP());
			evento.setTipoVersamento(TipoVersamento.ATTIVATO_PRESSO_PSP);

			// Creazione dell'RPT
			Rpt rpt = new RptBuilder().buildRptAttivata(bodyrichiesta.getIdentificativoIntermediarioPSP(), bodyrichiesta.getIdentificativoPSP(), bodyrichiesta.getIdentificativoCanalePSP(), versamento, iuv, ccp, bodyrichiesta.getDatiPagamentoPSP(), bd);

			ctx.log("ccp.attivazione", rpt.getCodMsgRichiesta());

			RptBD rptBD = new RptBD(bd);

			bd.setAutoCommit(false);
			bd.enableSelectForUpdate();

			// Controllo se gia' non esiste la RPT (lo devo fare solo adesso per essere in transazione con l'inserimento)
			try {
				Rpt oldrpt = rptBD.getRpt(codDominio, iuv, ccp);
				throw new NdpException(FaultPa.PAA_PAGAMENTO_IN_CORSO, codDominio, "RTP attivata in data " + oldrpt.getDataMsgRichiesta() + " [idMsgRichiesta: " + oldrpt.getCodMsgRichiesta() + "]");
			} catch (NotFoundException e2) {

				
				PagamentoPortale p = new PagamentoPortale();
				p.setCodApplicazione(AnagraficaManager.getApplicazione(bd, rpt.getVersamento(bd).getIdApplicazione()).getCodApplicazione());
				p.setCodCanale(rpt.getCodCanale());
				p.setCodiceStato(CODICE_STATO.PAGAMENTO_IN_CORSO_AL_PSP);
				p.setCodPsp(rpt.getCodPsp());
				p.setDataRichiesta(rpt.getDataMsgRichiesta());
				p.setIdSessione(ctx.getTransactionId().replaceAll("-", ""));

				List<IdVersamento> idVersamentoList = new ArrayList<>();

				IdVersamento idVersamento = new IdVersamento();
				idVersamento.setCodVersamentoEnte(rpt.getVersamento(bd).getCodVersamentoEnte());
				idVersamento.setId(rpt.getVersamento(bd).getId());
				
				idVersamentoList.add(idVersamento);
				p.setIdVersamento(idVersamentoList);
				
				p.setImporto(rpt.getVersamento(bd).getImportoTotale().doubleValue());
				p.setMultiBeneficiario(rpt.getCodDominio());
				
				if(rpt.getVersamento(bd).getNome()!=null) {
					p.setNome(rpt.getVersamento(bd).getNome());
				} else {
					try {
						p.setNome(rpt.getVersamento(bd).getCausaleVersamento().getSimple());
					} catch(UnsupportedEncodingException e) {}
				}

				p.setStato(STATO.IN_CORSO);
				p.setTipo(3);
				
				if(bodyrichiesta.getDatiPagamentoPSP().getSoggettoVersante() != null)
					p.setVersanteIdentificativo(bodyrichiesta.getDatiPagamentoPSP().getSoggettoVersante().getIdentificativoUnivocoVersante().getCodiceIdentificativoUnivoco());

				PagamentiPortaleBD ppbd = new PagamentiPortaleBD(bd);
				
				ppbd.insertPagamento(p);
				
				// imposto l'id pagamento all'rpt
				rpt.setIdPagamentoPortale(p.getId());
				
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
						CtSpezzoneStrutturatoCausaleVersamento spezzone = new CtSpezzoneStrutturatoCausaleVersamento();
						spezzone.setCausaleSpezzone(causale.getSpezzoni().get(i));
						spezzone.setImportoSpezzone(causale.getImporti().get(i));
						datiPagamento.getSpezzoniCausaleVersamento().getSpezzoneCausaleVersamentoOrSpezzoneStrutturatoCausaleVersamento().add(spezzone);
					}
				}
			} else {
				datiPagamento.setCausaleVersamento(" ");
			}
			
			datiPagamento.setEnteBeneficiario(RptUtils.buildEnteBeneficiario(dominio, versamento.getUo(bd), bd));
			
			SingoloVersamento singoloVersamento = versamento.getSingoliVersamenti(bd).get(0);
			IbanAccredito ibanAccredito = singoloVersamento.getIbanAccredito(bd);
			
			if(ibanAccredito != null) {
				datiPagamento.setBicAccredito(ibanAccredito.getCodBic());
				datiPagamento.setIbanAccredito(ibanAccredito.getCodIban());
			}
			esito.setDatiPagamentoPA(datiPagamento);
			response.setPaaAttivaRPTRisposta(esito);
			ctx.log("ccp.ricezioneAttivaOk", datiPagamento.getImportoSingoloVersamento().toString(), datiPagamento.getIbanAccredito(), versamento.getCausaleVersamento() != null ? versamento.getCausaleVersamento().toString() : "[-- Nessuna causale --]");
		} catch (NdpException e) {
			if(bd != null) bd.rollback();
			response = this.buildRisposta(e, response);
			String faultDescription = response.getPaaAttivaRPTRisposta().getFault().getDescription() == null ? "<Nessuna descrizione>" : response.getPaaAttivaRPTRisposta().getFault().getDescription(); 
			ctx.log("ccp.ricezioneAttivaKo", response.getPaaAttivaRPTRisposta().getFault().getFaultCode(), response.getPaaAttivaRPTRisposta().getFault().getFaultString(), faultDescription);
		} catch (Exception e) {
			if(bd != null) bd.rollback();
			response = this.buildRisposta(e, codDominio, response);
			String faultDescription = response.getPaaAttivaRPTRisposta().getFault().getDescription() == null ? "<Nessuna descrizione>" : response.getPaaAttivaRPTRisposta().getFault().getDescription(); 
			ctx.log("ccp.ricezioneAttivaKo", response.getPaaAttivaRPTRisposta().getFault().getFaultCode(), response.getPaaAttivaRPTRisposta().getFault().getFaultString(), faultDescription);
		} finally {
			try{
				if(bd != null) {
					GiornaleEventi ge = new GiornaleEventi(bd);
					evento.setEsito(response.getPaaAttivaRPTRisposta().getEsito());
					evento.setDataRisposta(new Date());
					ge.registraEvento(evento);
				}
			}catch(Exception e){
				log.error(e.getMessage(),e);
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
		String psp = bodyrichiesta.getIdentificativoPSP();
		
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
		ctx.getContext().getRequest().addGenericProperty(new Property("codPsp", psp));
		ctx.log("ccp.ricezioneVerifica");


		log.info("Ricevuta richiesta di verifica RPT [" + codIntermediario + "][" + codStazione + "][" + codDominio + "][" + iuv + "][" + ccp + "]");
		BasicBD bd = null;
		PaaVerificaRPTRisposta response = new PaaVerificaRPTRisposta();

		Evento evento = new Evento();
		evento.setCodStazione(codStazione);
		evento.setCodDominio(codDominio);
		evento.setIuv(iuv);
		evento.setCcp(ccp);
		evento.setTipoEvento(TipoEvento.paaVerificaRPT);
		evento.setCodPsp(psp);
		evento.setTipoVersamento(TipoVersamento.ATTIVATO_PRESSO_PSP);
		evento.setFruitore("NodoDeiPagamentiSPC");
		try {
			bd = BasicBD.newInstance(GpThreadLocal.get().getTransactionId());

			Utenza user = this.getUtenzaAutenticata();
			if(GovpayConfig.getInstance().isPddAuthEnable() && user.getPrincipal() == null) {
				ctx.log("ccp.erroreNoAutorizzazione");
				throw new NotAuthorizedException("Autorizzazione fallita: principal non fornito");
			}

			Intermediario intermediario = null;
			try {
				intermediario = AnagraficaManager.getIntermediario(bd, codIntermediario);

				// Controllo autorizzazione
				if(GovpayConfig.getInstance().isPddAuthEnable()){
					boolean authOk = false;
					
					if(user.isCheckSubject()) {
						// check tra subject
						authOk = CredentialUtils.checkSubject(intermediario.getConnettorePdd().getPrincipal(), user.getPrincipal());
					} else {
						authOk = user.getPrincipal().equals(intermediario.getConnettorePdd().getPrincipal());
					}
					
					if(!authOk) {
						ctx.log("ccp.erroreAutorizzazione", user.getPrincipal());
						throw new NotAuthorizedException("Autorizzazione fallita: principal fornito non corrisponde all'intermediario " + codIntermediario);
					}
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

			VersamentiBD versamentiBD = new VersamentiBD(bd);
			Versamento versamento = null;
			it.govpay.bd.model.Applicazione applicazioneGestisceIuv = null;
			try {
				versamento = versamentiBD.getVersamento(codDominio, iuv);
				ctx.log("ccp.iuvPresente", versamento.getCodVersamentoEnte());
			}catch (NotFoundException e) {
				applicazioneGestisceIuv = new Applicazione(bd).getApplicazioneDominio(dominio,iuv); 
				
				if(applicazioneGestisceIuv == null) {
					ctx.log("ccp.iuvNonPresenteNoAppGestireIuv");
					throw new NdpException(FaultPa.PAA_PAGAMENTO_SCONOSCIUTO, codDominio);
				}
				ctx.log("ccp.iuvNonPresente", applicazioneGestisceIuv.getCodApplicazione());
			}
			
			try {
				try {
					// Se non ho lo iuv, vado direttamente a chiedere all'applicazione di default
					if(versamento == null) throw new NotFoundException();
					
					// Versamento trovato, gestisco un'eventuale scadenza
					versamento = VersamentoUtils.aggiornaVersamento(versamento, bd);

					if(versamento.getStatoVersamento().equals(StatoVersamento.ANNULLATO))
						throw new NdpException(FaultPa.PAA_PAGAMENTO_ANNULLATO, codDominio);

					if(!versamento.getStatoVersamento().equals(StatoVersamento.NON_ESEGUITO)) {
						
						if(versamento.getStatoVersamento().equals(StatoVersamento.ANOMALO))
							throw new NdpException(FaultPa.PAA_PAGAMENTO_DUPLICATO, codDominio, "Il pagamento risulta gi\u00E0 effettuato, ma si riscontrano anomalie negli importi. Per maggiori informazioni contattare il supporto clienti.");
						
						if(versamento.getStatoVersamento().equals(StatoVersamento.ESEGUITO) || versamento.getStatoVersamento().equals(StatoVersamento.ESEGUITO_SENZA_RPT)) {
							PagamentiBD pagamentiBD = new PagamentiBD(bd);
							List<Pagamento> pagamenti = pagamentiBD.getPagamentiBySingoloVersamento(versamento.getSingoliVersamenti(bd).get(0).getId());
							if(pagamenti.isEmpty())
								throw new NdpException(FaultPa.PAA_PAGAMENTO_DUPLICATO, codDominio);
							else {
								Pagamento pagamento = pagamenti.get(0);
								throw new NdpException(FaultPa.PAA_PAGAMENTO_DUPLICATO, codDominio, "Il pagamento risulta gi\u00E0 effettuato in data " + sdf.format(pagamento.getDataPagamento()) + " [Iur:" + pagamento.getIur() + "]");
							}
								
						}
					}

				} catch (NotFoundException e) {
					// prendo tutte le applicazioni che gestiscono il dominio, tra queste cerco la prima che match la regexpr dello iuv la utilizzo per far acquisire il versamento
					if(applicazioneGestisceIuv == null) {
						applicazioneGestisceIuv = new Applicazione(bd).getApplicazioneDominio(dominio,iuv); 
					}
					
					// Versamento non trovato, devo interrogare l'applicazione.
					ctx.log("ccp.versamentoIuvNonPresente", applicazioneGestisceIuv.getCodApplicazione(), dominio.getCodDominio(), iuv);
					versamento = VersamentoUtils.acquisisciVersamento(applicazioneGestisceIuv, null, null, null, codDominio, iuv, bd);
					new it.govpay.core.business.Iuv(bd).caricaIUV(versamento.getApplicazione(bd), dominio, iuv, TipoIUV.NUMERICO, versamento.getCodVersamentoEnte());
					ctx.log("ccp.versamentoIuvNonPresenteOk",applicazioneGestisceIuv.getCodApplicazione(), dominio.getCodDominio(), iuv);
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
				throw new NdpException(FaultPa.PAA_SYSTEM_ERROR, codDominio, "Riscontrato errore durante l'acquisizione del versamento dall'applicazione gestore del debito: " + e1,e1);
			} catch (GovPayException e1) {
				throw new NdpException(FaultPa.PAA_SYSTEM_ERROR, codDominio, "Riscontrato errore durante la verifica del versamento: " + e1,e1);
			}

			// Verifico che abbia un solo singolo versamento
			if(versamento.getSingoliVersamenti(bd).size() != 1) {
				throw new NdpException(FaultPa.PAA_SEMANTICA, codDominio, "Il versamento contiene piu' di un singolo versamento, non ammesso per pagamenti ad iniziativa psp.");
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

			datiPagamento.setEnteBeneficiario(RptUtils.buildEnteBeneficiario(dominio, versamento.getUo(bd), bd));
			SingoloVersamento singoloVersamento = versamento.getSingoliVersamenti(bd).get(0);
			
			IbanAccredito ibanAccredito = singoloVersamento.getIbanAccredito(bd);
			
			if(ibanAccredito != null) {
				datiPagamento.setBicAccredito(ibanAccredito.getCodBic());
				datiPagamento.setIbanAccredito(ibanAccredito.getCodIban());
			}
			esito.setDatiPagamentoPA(datiPagamento);
			response.setPaaVerificaRPTRisposta(esito);
			ctx.log("ccp.ricezioneVerificaOk", datiPagamento.getImportoSingoloVersamento().toString(), datiPagamento.getIbanAccredito(), versamento.getCausaleVersamento() != null ? versamento.getCausaleVersamento().toString() : "[-- Nessuna causale --]");
		} catch (NdpException e) {
			if(bd != null) bd.rollback();
			response = this.buildRisposta(e, response);
			String faultDescription = response.getPaaVerificaRPTRisposta().getFault().getDescription() == null ? "<Nessuna descrizione>" : response.getPaaVerificaRPTRisposta().getFault().getDescription(); 
			ctx.log("ccp.ricezioneVerificaKo", response.getPaaVerificaRPTRisposta().getFault().getFaultCode(), response.getPaaVerificaRPTRisposta().getFault().getFaultString(), faultDescription);
		} catch (Exception e) {
			if(bd != null) bd.rollback();
			response = this.buildRisposta(e, codDominio, response);
			String faultDescription = response.getPaaVerificaRPTRisposta().getFault().getDescription() == null ? "<Nessuna descrizione>" : response.getPaaVerificaRPTRisposta().getFault().getDescription(); 
			ctx.log("ccp.ricezioneVerificaKo", response.getPaaVerificaRPTRisposta().getFault().getFaultCode(), response.getPaaVerificaRPTRisposta().getFault().getFaultString(), faultDescription);
		} finally {
			try{
				if(bd != null) {
					try{
						GiornaleEventi ge = new GiornaleEventi(bd);
						evento.setEsito(response.getPaaVerificaRPTRisposta().getEsito());
						evento.setDataRisposta(new Date());
						ge.registraEvento(evento);
					}catch(Exception e){log.error(e.getMessage(),e);}
				}

				if(ctx != null) {
					ctx.setResult(response.getPaaVerificaRPTRisposta().getFault() == null ? null : response.getPaaVerificaRPTRisposta().getFault().getFaultCode());
					ctx.log();
				}
			}catch(Exception e1){
				log.error(e1.getMessage(),e1);
			}

			if(bd != null) bd.closeConnection();
		}
		return response;
	}


	private <T> T buildRisposta(Exception e, String codDominio, T risposta) {
		return this.buildRisposta(new NdpException(FaultPa.PAA_SYSTEM_ERROR, codDominio, e.getMessage(), e), risposta);
	}

	private <T> T buildRisposta(NdpException e, T risposta) {
		if(risposta instanceof PaaAttivaRPTRisposta) {
			if(e.getFaultCode().equals(FaultPa.PAA_SYSTEM_ERROR.name())) {
				log.error("Errore interno in Attiva RPT",e);
			} else {
				log.warn("Rifiutata Attiva RPT con Fault " + e.getFaultString() + ( e.getDescrizione() != null ? (": " + e.getDescrizione()) : ""));
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
			if(e.getFaultCode().equals(FaultPa.PAA_SYSTEM_ERROR.name())) {
				log.error("Errore interno in Verifica RPT",e);
			} else {
				log.warn("Rifiutata Verifica RPT con Fault " + e.getFaultString() + ( e.getDescrizione() != null ? (": " + e.getDescrizione()) : ""));
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

		return risposta;
	}

	private Utenza getUtenzaAutenticata() throws GovPayException {
		Utenza user = null;
		try {
			HttpServletRequest request = (HttpServletRequest) this.wsCtxt.getMessageContext().get(MessageContext.SERVLET_REQUEST);  
			user = CredentialUtils.getUser(request, log);
		} catch (Exception e) {
			throw new GovPayException(EsitoOperazione.AUT_001, this.wsCtxt.getUserPrincipal().getName());
		}
		return user;
	}

}
