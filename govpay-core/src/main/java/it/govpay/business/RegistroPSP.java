/*
 * GovPay - Porta di Accesso al Nodo dei Pagamenti SPC 
 * http://www.gov4j.it/govpay
 * 
 * Copyright (c) 2014-2015 Link.it srl (http://www.link.it).
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
package it.govpay.business;

import it.gov.digitpa.schemas._2011.psp.CtInformativaDetail;
import it.gov.digitpa.schemas._2011.psp.CtInformativaMaster;
import it.gov.digitpa.schemas._2011.psp.InformativaPSP;
import it.gov.digitpa.schemas._2011.psp.ListaInformativePSP;
import it.gov.digitpa.schemas._2011.psp.ObjectFactory;
import it.gov.digitpa.schemas._2011.ws.paa.NodoChiediInformativaPSP;
import it.gov.digitpa.schemas._2011.ws.paa.NodoChiediInformativaPSPRisposta;
import it.govpay.bd.BasicBD;
import it.govpay.bd.anagrafica.AnagraficaManager;
import it.govpay.bd.anagrafica.DominiBD;
import it.govpay.bd.anagrafica.PspBD;
import it.govpay.bd.model.Dominio;
import it.govpay.bd.model.Intermediario;
import it.govpay.bd.model.Psp;
import it.govpay.bd.model.Psp.Canale;
import it.govpay.bd.model.Psp.ModelloPagamento;
import it.govpay.bd.model.Rpt.TipoVersamento;
import it.govpay.bd.model.Stazione;
import it.govpay.exception.GovPayException;
import it.govpay.exception.GovPayException.GovPayExceptionEnum;
import it.govpay.web.wsclient.NodoPerPa;

import java.io.ByteArrayOutputStream;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.activation.DataHandler;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBElement;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;
import javax.xml.namespace.QName;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class RegistroPSP extends BasicBD{

	private static Logger log = LogManager.getLogger();

	public RegistroPSP(BasicBD bd) {
		super(bd);
	}

	/**
	 * L'aggiornamento assume che la lista dei PSP sia sempre la stessa
	 * indipendentemente dalla StazioneIntermediarioPA/Dominio che la richiede.
	 * @throws GovPayException 
	 */
	public void aggiornaRegistro() throws GovPayException{
		log.info("Aggiornamento del Registro PSP");
		try {
			JAXBContext jaxbContext = JAXBContext.newInstance(ObjectFactory.class);
			DominiBD dominiBD = new DominiBD(this);
			List<Dominio> domini = dominiBD.getDomini();
			closeConnection();
			if(domini.size() == 0) return;
			for(Dominio dominio : domini) {
				setupConnection();

				Stazione stazione = AnagraficaManager.getStazione(this, dominio.getIdStazione());
				Intermediario intermediario = AnagraficaManager.getIntermediario(this, stazione.getIdIntermediario());

				closeConnection();
				NodoChiediInformativaPSP richiesta = new NodoChiediInformativaPSP();
				richiesta.setIdentificativoDominio(dominio.getCodDominio());
				richiesta.setIdentificativoIntermediarioPA(intermediario.getCodIntermediario());
				richiesta.setIdentificativoStazioneIntermediarioPA(stazione.getCodStazione());
				richiesta.setPassword(stazione.getPassword());

				ListaInformativePSP informativePsp = null;
				try {
					NodoPerPa client = new NodoPerPa(intermediario);
					NodoChiediInformativaPSPRisposta risposta = client.nodoChiediInformativaPSP(richiesta, intermediario.getDenominazione());

					if(risposta.getFault() != null) {
						throw new GovPayException(GovPayExceptionEnum.ERRORE_NDP, risposta.getFault().getFaultCode() + ": " + risposta.getFault().getFaultString());
					}

					DataHandler dh= risposta.getXmlInformativa();
					ByteArrayOutputStream output = new ByteArrayOutputStream();
					dh.writeTo(output);

					Unmarshaller jaxbUnmarshaller = jaxbContext.createUnmarshaller();
					informativePsp = (ListaInformativePSP) jaxbUnmarshaller.unmarshal(risposta.getXmlInformativa().getDataSource().getInputStream());
				} catch (Exception e) {
					log.warn("Errore di acquisizione del Catalogo dati Informativi [codIntermediario: " + intermediario.getCodIntermediario() + "][codStazione: " + stazione.getCodStazione() + "][codDominio:" + dominio.getCodDominio() + "]: " + e);
					log.error("Errore di acquisizione del Catalogo dati Informativi [codIntermediario: " + intermediario.getCodIntermediario() + "][codStazione: " + stazione.getCodStazione() + "][codDominio:" + dominio.getCodDominio() + "]: " + e, e);
					continue;
				}

				log.info("Catalogo dei dati Informativi acquisito con " + informativePsp.getInformativaPSPs().size() + " informative.");
				List<Psp> catalogoPsp = new ArrayList<Psp>();
				Map<String, byte[]> tracciatiInformativePsp = new HashMap<String, byte[]>();

				Marshaller jaxbMarshaller = jaxbContext.createMarshaller();

				for(InformativaPSP informativaPsp : informativePsp.getInformativaPSPs()) {
					Psp psp = new Psp();

					CtInformativaMaster informativaMaster = informativaPsp.getInformativaMaster();

					boolean isAttivo = informativaMaster.getDataInizioValidita().before(new Date());

					psp.setAttivo(isAttivo);
					psp.setCodFlusso(informativaPsp.getIdentificativoFlusso());
					psp.setCodPsp(informativaPsp.getIdentificativoPSP());
					psp.setRagioneSociale(informativaPsp.getRagioneSociale());
					psp.setStornoGestito(informativaMaster.getStornoPagamento() == 1);
					psp.setBolloGestito(false);
					psp.setUrlInfo(informativaMaster.getUrlInformazioniPSP());

					for(CtInformativaDetail informativaPspDetail : informativaPsp.getListaInformativaDetail().getInformativaDetails()) {
						Canale canale = psp.new Canale();
						canale.setCondizioni(informativaPspDetail.getCondizioniEconomicheMassime());
						canale.setCodCanale(informativaPspDetail.getIdentificativoCanale());
						canale.setDescrizione(informativaPspDetail.getDescrizioneServizio());
						canale.setDisponibilita(informativaPspDetail.getDisponibilitaServizio());
						canale.setModelloPagamento(ModelloPagamento.toEnum(informativaPspDetail.getModelloPagamento()));
						canale.setPsp(psp);
						canale.setTipoVersamento(TipoVersamento.toEnum(informativaPspDetail.getTipoVersamento().name()));
						canale.setUrlInfo(informativaPspDetail.getUrlInformazioniCanale());
						canale.setCodIntermediario(informativaPspDetail.getIdentificativoIntermediario());
						psp.getCanali().add(canale);
					}

					catalogoPsp.add(psp);
					StringWriter sw = new StringWriter();
					jaxbMarshaller.marshal(new JAXBElement<InformativaPSP>(new QName("informativaPSP"), InformativaPSP.class, informativaPsp), sw);
					tracciatiInformativePsp.put(psp.getCodPsp(), sw.toString().getBytes());
					log.debug("Acquisita informativa [codPsp: " + psp.getCodPsp() + "]");
				}

				// Completata acquisizione del Catalogo dal Nodo dei Pagamenti.
				// Disabilito tutti i PSP e li aggiorno o inserisco in base
				// a quello che ho trovato sul catalogo.
				setupConnection();
				setAutoCommit(false);
				PspBD pspBD = new PspBD(this);
				List<Psp> oldPsp = pspBD.getPsp();
				while(!oldPsp.isEmpty()) {
					Psp psp = oldPsp.remove(0);
					// Cerco il psp nel Catalogo
					boolean trovato = false;
					for(int i = 0; i<catalogoPsp.size(); i++ ) {
						if(catalogoPsp.get(i).getCodPsp().equals(psp.getCodPsp())) {
							// Il psp e' nel catalogo, va aggiornato. 
							// Estraggo la versione aggiornata e lo inserisco tra quelli da aggiornare
							log.info("Aggiornamento [codPsp: " + psp.getCodPsp() + "]");
							pspBD.updatePsp(catalogoPsp.get(i), tracciatiInformativePsp.get(catalogoPsp.get(i).getCodPsp()));
							catalogoPsp.remove(i);
							trovato = true;
							break;
						}
					}

					if(!trovato) {
						// Il psp non e' nel catalogo.
						// Se era attivo, lo disattivo e lo metto in aggiornamento.
						if(psp.isAttivo()) {
							log.info("Disabilitazione [codPsp: " + psp.getCodPsp() + "]");
							pspBD.disablePsp(psp.getId());
						}
					}
				}

				for(Psp psp : catalogoPsp) {
					log.info("Inserimento [codPsp: " + psp.getCodPsp() + "]");
					pspBD.insertPsp(psp, tracciatiInformativePsp.get(psp.getCodPsp()));
				}

				commit();
				log.info("Aggiornamento Registro PSP completato.");
				return;
			}
		} catch (Exception se) {
			rollback();
			throw new GovPayException(GovPayExceptionEnum.ERRORE_INTERNO, "Non è stato possibile acquisire il Catalogo dei Psp dal Nodo dei Pagamenti: " + se.getMessage(), se);
		} finally {
			closeConnection();
		}
		throw new GovPayException(GovPayExceptionEnum.ERRORE_INTERNO, "Non è stato possibile acquisire il Catalogo dei Psp dal Nodo dei Pagamenti.");
	}
}
