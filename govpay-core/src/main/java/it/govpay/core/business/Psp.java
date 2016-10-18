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
package it.govpay.core.business;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.activation.DataHandler;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.Unmarshaller;

import org.apache.commons.lang.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openspcoop2.generic_project.exception.ServiceException;
import org.openspcoop2.utils.logger.beans.Property;

import it.gov.digitpa.schemas._2011.psp.CtInformativaDetail;
import it.gov.digitpa.schemas._2011.psp.CtInformativaMaster;
import it.gov.digitpa.schemas._2011.psp.InformativaPSP;
import it.gov.digitpa.schemas._2011.psp.ListaInformativePSP;
import it.gov.digitpa.schemas._2011.psp.ObjectFactory;
import it.gov.digitpa.schemas._2011.ws.paa.NodoChiediInformativaPSP;
import it.gov.digitpa.schemas._2011.ws.paa.NodoChiediInformativaPSPRisposta;
import it.govpay.bd.BasicBD;
import it.govpay.bd.anagrafica.DominiBD;
import it.govpay.bd.anagrafica.PspBD;
import it.govpay.servizi.commons.EsitoOperazione;
import it.govpay.servizi.gpprt.GpChiediListaPspResponse;
import it.govpay.core.exceptions.GovPayException;
import it.govpay.core.utils.GpContext;
import it.govpay.core.utils.GpThreadLocal;
import it.govpay.core.utils.PspUtils;
import it.govpay.core.utils.client.NodoClient;
import it.govpay.core.utils.client.NodoClient.Azione;
import it.govpay.bd.model.Canale;
import it.govpay.bd.model.Dominio;
import it.govpay.model.Intermediario;
import it.govpay.model.Portale;
import it.govpay.bd.model.Stazione;

public class Psp extends BasicBD {

	private static Logger log = LogManager.getLogger();

	public Psp(BasicBD basicBD) {
		super(basicBD);
	}

	public GpChiediListaPspResponse chiediListaPsp(Portale portaleAutenticato) throws ServiceException {

		PspBD pspBD = new PspBD(this);
		List<it.govpay.bd.model.Psp> psps = pspBD.getPsp(true);

		GpChiediListaPspResponse response = new GpChiediListaPspResponse();
		for(it.govpay.bd.model.Psp pspModel : psps) {
			GpChiediListaPspResponse.Psp psp = new GpChiediListaPspResponse.Psp();
			psp.setBollo(pspModel.isBolloGestito());
			psp.setCodPsp(pspModel.getCodPsp());
			psp.setLogo(PspUtils.getLogo160(pspModel.getCodPsp()));
			psp.setRagioneSociale(pspModel.getRagioneSociale());
			psp.setStorno(pspModel.isStornoGestito());
			psp.setUrlInfo(pspModel.getUrlInfo());
			if(pspModel.getCanalis() != null) {
				for(it.govpay.bd.model.Canale canaleModel : pspModel.getCanalis()) {
					GpChiediListaPspResponse.Psp.Canale canale = new GpChiediListaPspResponse.Psp.Canale();
					canale.setCodCanale(canaleModel.getCodCanale());
					canale.setCondizioni(canaleModel.getCondizioni());
					canale.setDescrizione(canaleModel.getDescrizione());
					canale.setDisponibilita(canaleModel.getDisponibilita());
					canale.setLogoServizio(PspUtils.getLogo(canaleModel.getModelloPagamento()));
					canale.setModelloPagamento(PspUtils.toWeb(canaleModel.getModelloPagamento()));
					canale.setTipoVersamento(PspUtils.toWeb(canaleModel.getTipoVersamento()));
					canale.setUrlInfo(canaleModel.getUrlInfo());
					psp.getCanale().add(canale);
				}
			}
			response.getPsp().add(psp);
		}
		return response;
	}


	public String aggiornaRegistro() throws GovPayException {
		List<String> response = new ArrayList<String>();

		GpContext ctx = GpThreadLocal.get();

		log.info("Aggiornamento del Registro PSP");
		ctx.log("psp.aggiornamentoPsp");
		boolean acquisizioneOk = false;
		String lastError = "[-- No exception found --]";
		String transactionId = null;
		try {
			JAXBContext jaxbContext = JAXBContext.newInstance(ObjectFactory.class);

			DominiBD dominiBD = new DominiBD(this);
			List<Dominio> domini = dominiBD.getDomini();

			if(domini.size() == 0) {
				log.warn("Nessun dominio registrato. Impossibile richiedere il catalogo dei Psp.");
				ctx.log("psp.aggiornamentoPspNoDomini");
				throw new GovPayException(EsitoOperazione.INTERNAL, "Nessun dominio registrato. Impossibile richiedere il catalogo dei Psp.");
			}
			// Finche' non ricevo un catalogo di informativa, provo per tutti i domini.
			ListaInformativePSP informativePsp = null;

			for(Dominio dominio : domini) {
				Stazione stazione = dominio.getStazione(this);
				Intermediario intermediario = stazione.getIntermediario(this);

				transactionId = ctx.openTransaction();
				ctx.getContext().getRequest().addGenericProperty(new Property("codDominio", dominio.getCodDominio()));
				ctx.getContext().getRequest().addGenericProperty(new Property("codStazione", stazione.getCodStazione()));
				ctx.setupNodoClient(stazione.getCodStazione(), dominio.getCodDominio(), Azione.nodoChiediInformativaPSP);
				ctx.log("psp.aggiornamentoPspRichiesta");

				closeConnection();

				NodoChiediInformativaPSP richiesta = new NodoChiediInformativaPSP();
				richiesta.setIdentificativoDominio(dominio.getCodDominio());
				richiesta.setIdentificativoIntermediarioPA(intermediario.getCodIntermediario());
				richiesta.setIdentificativoStazioneIntermediarioPA(stazione.getCodStazione());
				richiesta.setPassword(stazione.getPassword());

				try {

					try { 
						NodoClient client = new NodoClient(intermediario);

						NodoChiediInformativaPSPRisposta risposta = client.nodoChiediInformativaPSP(richiesta, intermediario.getDenominazione());

						if(risposta.getFault() != null) {
							throw new GovPayException(EsitoOperazione.NDP_001, risposta.getFault().getFaultCode() + ": " + risposta.getFault().getFaultString());
						}

						DataHandler dh= risposta.getXmlInformativa();
						ByteArrayOutputStream output = new ByteArrayOutputStream();
						dh.writeTo(output);

						Unmarshaller jaxbUnmarshaller = jaxbContext.createUnmarshaller();
						informativePsp = (ListaInformativePSP) jaxbUnmarshaller.unmarshal(risposta.getXmlInformativa().getDataSource().getInputStream());
					} finally {
						setupConnection(ctx.getTransactionId());
					}

					if(informativePsp == null) {
						log.warn("Catalogo dei psp non acquisito. Impossibile aggiornare il registro.");
						ctx.log("psp.aggiornamentoPspRichiestaKo", "Catalogo dei psp vuoto.");
						throw new GovPayException(EsitoOperazione.INTERNAL, "Catalogo dei psp non acquisito. Impossibile aggiornare il registro.");
					}

					log.info("Ricevuto catalogo dei dati Informativi con " + informativePsp.getInformativaPSPs().size() + " informative.");
					List<it.govpay.bd.model.Psp> catalogoPsp = new ArrayList<it.govpay.bd.model.Psp>();

					// Converto ogni informativa un PSP
					for(InformativaPSP informativaPsp : informativePsp.getInformativaPSPs()) {
						it.govpay.bd.model.Psp psp = new it.govpay.bd.model.Psp();

						CtInformativaMaster informativaMaster = informativaPsp.getInformativaMaster();

						boolean isAttivo = informativaMaster.getDataInizioValidita().before(new Date());

						psp.setAbilitato(isAttivo);
						psp.setCodFlusso(informativaPsp.getIdentificativoFlusso());
						psp.setCodPsp(informativaPsp.getIdentificativoPSP());
						psp.setRagioneSociale(informativaPsp.getRagioneSociale());
						psp.setStornoGestito(informativaMaster.getStornoPagamento() == 1);
						psp.setBolloGestito(informativaMaster.getMarcaBolloDigitale() == 1);
						psp.setUrlInfo(informativaMaster.getUrlInformazioniPSP());

						for(CtInformativaDetail informativaPspDetail : informativaPsp.getListaInformativaDetail().getInformativaDetails()) {
							Canale canale = new Canale();
							canale.setCondizioni(informativaPspDetail.getCondizioniEconomicheMassime());
							canale.setCodCanale(informativaPspDetail.getIdentificativoCanale());
							canale.setDescrizione(informativaPspDetail.getDescrizioneServizio());
							canale.setDisponibilita(informativaPspDetail.getDisponibilitaServizio());
							canale.setModelloPagamento(Canale.ModelloPagamento.toEnum(informativaPspDetail.getModelloPagamento()));
							canale.setPsp(psp);
							canale.setTipoVersamento(Canale.TipoVersamento.toEnum(informativaPspDetail.getTipoVersamento().name()));
							canale.setUrlInfo(informativaPspDetail.getUrlInformazioniCanale());
							canale.setCodIntermediario(informativaPspDetail.getIdentificativoIntermediario());
							psp.getCanalis().add(canale);
						}
						catalogoPsp.add(psp);
						log.debug("Acquisita informativa [codPsp: " + psp.getCodPsp() + "]");
					}

					// Completata acquisizione del Catalogo dal Nodo dei Pagamenti.

					// Disabilito tutti i PSP e li aggiorno o inserisco in base
					// a quello che ho trovato sul catalogo.
					setAutoCommit(false);

					PspBD pspBD = new PspBD(this);
					List<it.govpay.bd.model.Psp> oldPsps = pspBD.getPsp();
					while(!oldPsps.isEmpty()) {
						it.govpay.model.Psp psp = oldPsps.remove(0);
						// Cerco il psp nel Catalogo appena ricevuto
						boolean trovato = false;
						for(int i = 0; i<catalogoPsp.size(); i++ ) {
							if(catalogoPsp.get(i).getCodPsp().equals(psp.getCodPsp())) {

								// Il psp e' nel catalogo, va aggiornato. 
								// Rimuovo la versione aggiornata dal catalogo e lo mando in update
								log.info("Aggiornamento [codPsp: " + psp.getCodPsp() + "]");
								ctx.log("psp.aggiornamentoPspAggiornatoPSP", psp.getCodPsp(), psp.getRagioneSociale());
								response.add(psp.getRagioneSociale() + " (" + psp.getCodPsp() + ")#Acquisita versione aggiornata.");
								pspBD.updatePsp(catalogoPsp.get(i));
								catalogoPsp.remove(i);
								trovato = true;
								break;
							}
						}

						if(!trovato) {
							// Il psp non e' nel catalogo.
							// Se era attivo, lo disattivo.
							if(psp.isAbilitato()) {
								log.info("Disabilitazione [codPsp: " + psp.getCodPsp() + "]");
								ctx.log("psp.aggiornamentoPspDisabilitatoPSP", psp.getCodPsp(), psp.getRagioneSociale());
								response.add(psp.getRagioneSociale() + " (" + psp.getCodPsp() + ")#Disabilitato.");
								pspBD.disablePsp(psp.getId());
							}
						}
					}

					// I psp rimasti nel catalogo, sono nuovi e vanno aggiunti
					for(it.govpay.bd.model.Psp psp : catalogoPsp) {
						log.info("Inserimento [codPsp: " + psp.getCodPsp() + "]");
						ctx.log("psp.aggiornamentoPspInseritoPSP", psp.getCodPsp(), psp.getRagioneSociale());
						response.add(psp.getRagioneSociale() + " (" + psp.getCodPsp() + ")#Aggiunto al registro.");
						pspBD.insertPsp(psp);
					}

					commit();

					log.info("Aggiornamento Registro PSP completato.");
					ctx.log("psp.aggiornamentoPspRichiestaOk");
					acquisizioneOk = true;
					break;
				} catch (Exception e) {
					log.warn("Errore di acquisizione del Catalogo dati Informativi [codIntermediario: " + intermediario.getCodIntermediario() + "][codStazione: " + stazione.getCodStazione() + "][codDominio:" + dominio.getCodDominio() + "]: " + e);
					ctx.log("psp.aggiornamentoPspRichiestaKo", e.getMessage());
					lastError = e.getMessage();
					continue;
				} finally {
					ctx.closeTransaction(transactionId);
				}
			}


			if(acquisizioneOk) {
				ctx.log("psp.aggiornamentoPspOk");
				if(response.isEmpty()) {
					return "Acquisizione completata#Nessun psp acquisito.";
				} else {
					return StringUtils.join(response,"|");
				}
			} else {
				ctx.log("psp.aggiornamentoPspKo", lastError);
				return "Acquisizione fallita#Riscontrato errore:" + lastError;
			}
		} catch (Exception se) {
			rollback();
			ctx.log("psp.aggiornamentoPspKo", se.getMessage());
			throw new GovPayException(EsitoOperazione.INTERNAL, se, "Non è stato possibile acquisire il Catalogo dei Psp dal Nodo dei Pagamenti: " + se.getMessage());
		} finally {
			closeConnection();
			ctx.closeTransaction(transactionId);
		}
	}

}