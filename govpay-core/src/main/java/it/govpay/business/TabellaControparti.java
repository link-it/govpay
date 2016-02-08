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

import it.gov.digitpa.schemas._2011.psp.CtContiDiAccredito;
import it.gov.digitpa.schemas._2011.psp.CtErogazione;
import it.gov.digitpa.schemas._2011.psp.CtErogazioneServizio;
import it.gov.digitpa.schemas._2011.psp.CtFasciaOraria;
import it.gov.digitpa.schemas._2011.psp.CtInformativaContoAccredito;
import it.gov.digitpa.schemas._2011.psp.CtListaDisponibilita;
import it.gov.digitpa.schemas._2011.psp.CtListaIndisponibilita;
import it.gov.digitpa.schemas._2011.psp.InformativaControparte;
import it.gov.digitpa.schemas._2011.psp.StTipoPeriodo;
import it.govpay.bd.BasicBD;
import it.govpay.bd.anagrafica.AnagraficaManager;
import it.govpay.bd.anagrafica.ContiAccreditoBD;
import it.govpay.bd.anagrafica.DominiBD;
import it.govpay.bd.anagrafica.TabellaContropartiBD;
import it.govpay.bd.model.Disponibilita;
import it.govpay.bd.model.Periodo;
import it.govpay.bd.model.Dominio;
import it.govpay.bd.model.IbanAccredito;
import it.govpay.exception.GovPayException;
import it.govpay.exception.GovPayException.GovPayExceptionEnum;
import it.govpay.utils.JaxbUtils;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

import javax.xml.bind.JAXBException;
import javax.xml.stream.XMLStreamException;

import org.openspcoop2.generic_project.exception.MultipleResultException;
import org.openspcoop2.generic_project.exception.NotFoundException;
import org.openspcoop2.generic_project.exception.ServiceException;
import org.xml.sax.SAXException;

public class TabellaControparti extends BasicBD {
	
	public TabellaControparti(BasicBD basicBD) {
		super(basicBD);
	}
	
	public it.govpay.bd.model.TabellaControparti getInformativa(long idDominio, Date inizioValidita) throws GovPayException {
		
		try {
			DominiBD dominiBd = new DominiBD(this);
			Dominio dominio = AnagraficaManager.getDominio(this, idDominio);
			List<IbanAccredito> lstIban = dominiBd.getIbanAccreditoByIdDominio(idDominio);
			InformativaControparte informativaControparte = getInformativaControparte(dominio, filtraIbanAbilitati(lstIban), inizioValidita);
			it.govpay.bd.model.TabellaControparti tabellaControparti = new it.govpay.bd.model.TabellaControparti();
			tabellaControparti.setDataOraInizioValidita(informativaControparte.getDataInizioValidita());
			tabellaControparti.setDataOraPubblicazione(informativaControparte.getDataPubblicazione());
			tabellaControparti.setIdDominio(idDominio);
			tabellaControparti.setIdFlusso(informativaControparte.getIdentificativoFlusso());
			tabellaControparti.setXml(JaxbUtils.toByte(informativaControparte));
			
			
			TabellaContropartiBD tabellaContropartiBD = new TabellaContropartiBD(this);
			tabellaContropartiBD.insertTabellaControparti(tabellaControparti);
			
			return tabellaControparti;

		} catch (NotFoundException e) {
			throw new GovPayException(GovPayExceptionEnum.ERRORE_INTERNO, "Il dominio ["+idDominio+"] non esiste.");
		} catch (MultipleResultException e) {
			throw new GovPayException(GovPayExceptionEnum.ERRORE_INTERNO, "Trovati piu' domini con id ["+idDominio+"].");
		} catch (ServiceException e) {
			throw new GovPayException(GovPayExceptionEnum.ERRORE_INTERNO, e);
		} catch (JAXBException e) {
			throw new GovPayException(GovPayExceptionEnum.ERRORE_INTERNO, e);
		} catch (SAXException e) {
			throw new GovPayException(GovPayExceptionEnum.ERRORE_INTERNO, e);
		} catch (XMLStreamException e) {
			throw new GovPayException(GovPayExceptionEnum.ERRORE_INTERNO, e);
		} catch (IOException e) {
			throw new GovPayException(GovPayExceptionEnum.ERRORE_INTERNO, e);
		}
		
	}

	public it.govpay.bd.model.ContoAccredito getContoAccredito(long idDominio, Date inizioValidita) throws GovPayException {
		
		try {
			DominiBD dominiBd = new DominiBD(this);
			Dominio dominio = AnagraficaManager.getDominio(this, idDominio);
			List<IbanAccredito> lstIban = dominiBd.getIbanAccreditoByIdDominio(idDominio);
			Date dataPubblicazione = new Date();
			CtInformativaContoAccredito informativaContoAccredito = getInformativaContoAccredito(dominio, UUID.randomUUID().toString().replaceAll("-", ""), dataPubblicazione, dataPubblicazione, filtraIbanAbilitati(lstIban));
			it.govpay.bd.model.ContoAccredito contoAccredito = new it.govpay.bd.model.ContoAccredito();
			contoAccredito.setDataOraInizioValidita(informativaContoAccredito.getDataInizioValidita());
			contoAccredito.setDataOraPubblicazione(informativaContoAccredito.getDataPubblicazione());
			contoAccredito.setIdDominio(idDominio);
			contoAccredito.setIdFlusso(informativaContoAccredito.getIdentificativoFlusso());
			contoAccredito.setXml(JaxbUtils.toByte(informativaContoAccredito));
			
			
			ContiAccreditoBD contiAccreditoBD = new ContiAccreditoBD(this);
			contiAccreditoBD.insertContoAccredito(contoAccredito);
			
			return contoAccredito;

		} catch (NotFoundException e) {
			throw new GovPayException(GovPayExceptionEnum.ERRORE_INTERNO, "Il dominio ["+idDominio+"] non esiste.");
		} catch (MultipleResultException e) {
			throw new GovPayException(GovPayExceptionEnum.ERRORE_INTERNO, "Trovati piu' domini con id ["+idDominio+"].");
		} catch (ServiceException e) {
			throw new GovPayException(GovPayExceptionEnum.ERRORE_INTERNO, e);
		} catch (JAXBException e) {
			throw new GovPayException(GovPayExceptionEnum.ERRORE_INTERNO, e);
		} catch (SAXException e) {
			throw new GovPayException(GovPayExceptionEnum.ERRORE_INTERNO, e);
		} catch (XMLStreamException e) {
			throw new GovPayException(GovPayExceptionEnum.ERRORE_INTERNO, e);
		} catch (IOException e) {
			throw new GovPayException(GovPayExceptionEnum.ERRORE_INTERNO, e);
		}
		
	}

	private List<IbanAccredito> filtraIbanAbilitati(List<IbanAccredito> lstIban) {
		List<IbanAccredito> newList = new ArrayList<IbanAccredito>();
		if(lstIban != null && !lstIban.isEmpty()) {
			for(IbanAccredito iban: lstIban) {
				if(iban.isAbilitato())
					newList.add(iban);
			}
		}
		return newList;
	} 
	
	private InformativaControparte getInformativaControparte(Dominio dominio, List<IbanAccredito> lstIban, Date inizioValidita) {
		
		SimpleDateFormat hourFormat = new SimpleDateFormat("hh:mm:ss");
		InformativaControparte informativaControparte = new InformativaControparte();
		informativaControparte.setDataInizioValidita(inizioValidita);
		informativaControparte.setDataPubblicazione(new Date());
		informativaControparte.setIdentificativoDominio(dominio.getCodDominio());
		informativaControparte.setIdentificativoFlusso(UUID.randomUUID().toString().replaceAll("-", ""));
		informativaControparte.setRagioneSociale(dominio.getRagioneSociale());
		CtErogazioneServizio erogazioneServizio = new CtErogazioneServizio();
		
		CtListaDisponibilita listaDisponibilita = new CtListaDisponibilita();
		CtListaIndisponibilita listaIndisponibilita = new CtListaIndisponibilita();

		if(dominio.getDisponibilita() != null && !dominio.getDisponibilita().isEmpty()) {
			for(Disponibilita disponibilita: dominio.getDisponibilita()) {
				CtErogazione erogazione = new CtErogazione();
				erogazione.setGiorno(disponibilita.getGiorno());
				switch(disponibilita.getTipoPeriodo()) {
				case ANNUALE: erogazione.setTipoPeriodo(StTipoPeriodo.ANNUALE);
					break;
				case GIORNALIERA: erogazione.setTipoPeriodo(StTipoPeriodo.GIORNALIERA);
					break;
				case MENSILE: erogazione.setTipoPeriodo(StTipoPeriodo.MENSILE);
					break;
				case SETTIMANALE: erogazione.setTipoPeriodo(StTipoPeriodo.SETTIMANALE);
					break;
				default:
					break;
				
				}

				if(disponibilita.getFasceOrarieLst() != null && !disponibilita.getFasceOrarieLst().isEmpty()) {
					for(Periodo fascia: disponibilita.getFasceOrarieLst()) {
						try {
							CtFasciaOraria fasciaOraria = new CtFasciaOraria();
							fasciaOraria.setDa(hourFormat.parse(fascia.getDa()));
							fasciaOraria.setA(hourFormat.parse(fascia.getA()));
							erogazione.getFasciaOrarias().add(fasciaOraria);
						} catch(ParseException e) {}
					}
				}
				
				switch(disponibilita.getTipoDisponibilita()) {
				case DISPONIBILE: listaDisponibilita.getDisponibilitas().add(erogazione);
					break;
				case NON_DISPONIBILE: listaIndisponibilita.getIndisponibilitas().add(erogazione);
					break;
				}
			}
		}
		erogazioneServizio.setListaDisponibilita(listaDisponibilita);
		erogazioneServizio.setListaIndisponibilita(listaIndisponibilita);
		informativaControparte.setErogazioneServizio(erogazioneServizio);
		
		informativaControparte.setInformativaContoAccredito(getInformativaContoAccredito(dominio, informativaControparte.getIdentificativoFlusso(), informativaControparte.getDataInizioValidita(), informativaControparte.getDataPubblicazione(), lstIban));
		
		return informativaControparte;
	}

	private CtInformativaContoAccredito getInformativaContoAccredito(Dominio dominio, String idFlusso, Date inizioValidita, Date pubblicazione, List<IbanAccredito> lstIban) {
		CtInformativaContoAccredito informativaContoAccredito = new CtInformativaContoAccredito();
		if(lstIban != null && !lstIban.isEmpty()) {
			CtContiDiAccredito contiAccredito = new CtContiDiAccredito();
			for(IbanAccredito iban: lstIban) {
				contiAccredito.getIbanAccreditos().add(iban.getCodIban());
			}
			informativaContoAccredito.setContiDiAccredito(contiAccredito);
		}
		informativaContoAccredito.setDataInizioValidita(inizioValidita);
		informativaContoAccredito.setDataPubblicazione(pubblicazione);
		informativaContoAccredito.setIdentificativoDominio(dominio.getCodDominio());
		informativaContoAccredito.setIdentificativoFlusso(idFlusso);
		informativaContoAccredito.setRagioneSociale(dominio.getRagioneSociale());
		return informativaContoAccredito;
	}

}
