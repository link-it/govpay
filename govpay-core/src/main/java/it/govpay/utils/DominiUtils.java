package it.govpay.utils;

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
import it.govpay.bd.anagrafica.TabellaContropartiBD;
import it.govpay.bd.model.ContoAccredito;
import it.govpay.bd.model.Disponibilita;
import it.govpay.bd.model.Dominio;
import it.govpay.bd.model.IbanAccredito;
import it.govpay.bd.model.Periodo;
import it.govpay.bd.model.TabellaControparti;
import it.govpay.dars.bd.DominiBD;
import it.govpay.exception.GovPayException;
import it.govpay.exception.GovPayException.GovPayExceptionEnum;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.UUID;

import javax.xml.bind.JAXBException;
import javax.xml.stream.XMLStreamException;

import org.openspcoop2.generic_project.exception.MultipleResultException;
import org.openspcoop2.generic_project.exception.NotFoundException;
import org.openspcoop2.generic_project.exception.ServiceException;
import org.xml.sax.SAXException;

public class DominiUtils {

	/** Tabella controparti **/

	public static void updateTabellaControparti(BasicBD bd, long idDominio) throws GovPayException {
		try {

			TabellaContropartiBD tabellaContropartiBD = new TabellaContropartiBD(bd);
			Dominio dominio = AnagraficaManager.getDominio(bd, idDominio);
			
			TabellaControparti lastTabella = null;
			try{
				lastTabella = tabellaContropartiBD.getLastTabellaControparti(idDominio);
			} catch (NotFoundException e) {
			} catch (MultipleResultException e) {}
			
			String idFlusso = lastTabella != null ? lastTabella.getIdFlusso() : UUID.randomUUID().toString().replaceAll("-", "");
			Date inizioValidita = lastTabella != null ? lastTabella.getDataOraInizioValidita() : new Date();
			Date inizioPubblicazione = lastTabella != null ? lastTabella.getDataOraPubblicazione() : new Date();
			InformativaControparte newInfo = buildInformativaControparte(dominio, idFlusso, inizioValidita, inizioPubblicazione);
			
			//se non esiste una tabellaControparti, oppure i dati sono cambiati dall'ultima inserita, ne inserisco una nuova
			if(lastTabella == null || !equalsTabellaControparti(newInfo, lastTabella.getXml())) {
				if(lastTabella != null) {
						//Aggiorno l'id flusso (prima lo avevo impostato uguale a quello dell'ultima tabella generata per verificarne l'uguaglianza)
					newInfo.setIdentificativoFlusso(UUID.randomUUID().toString().replaceAll("-", ""));
					Date dataInizioValidita = new Date();
					newInfo.setDataInizioValidita(dataInizioValidita);
					newInfo.setDataPubblicazione(dataInizioValidita);
				}
				
				it.govpay.bd.model.TabellaControparti tabellaControparti = new it.govpay.bd.model.TabellaControparti();
				tabellaControparti.setDataOraInizioValidita(newInfo.getDataInizioValidita());
				tabellaControparti.setDataOraPubblicazione(newInfo.getDataPubblicazione());
				tabellaControparti.setIdDominio(idDominio);
				tabellaControparti.setIdFlusso(newInfo.getIdentificativoFlusso());
				tabellaControparti.setXml(JaxbUtils.toByte(newInfo));
				
				tabellaContropartiBD.insertTabellaControparti(tabellaControparti);

			}
			
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
		} catch (Exception e) {
			throw new GovPayException(GovPayExceptionEnum.ERRORE_INTERNO, e);
		}

	}

	private static boolean equalsTabellaControparti(InformativaControparte actual, byte[] expected) throws JAXBException, SAXException, XMLStreamException, GovPayException, IOException {

		byte[] byteActual = JaxbUtils.toByte(actual);
		return Arrays.equals(byteActual, expected);
	}
	
	private static InformativaControparte buildInformativaControparte(Dominio dominio, String idFlusso, Date inizioValidita, Date dataPubblicazione) {

		SimpleDateFormat hourFormat = new SimpleDateFormat("hh:mm:ss");
		InformativaControparte informativaControparte = new InformativaControparte();
		informativaControparte.setDataInizioValidita(inizioValidita);
		informativaControparte.setDataPubblicazione(dataPubblicazione);
		informativaControparte.setIdentificativoDominio(dominio.getCodDominio());
		informativaControparte.setIdentificativoFlusso(idFlusso);
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

		return informativaControparte;
	}

	/** Conti accredito **/  

	public static void updateContiAccredito(BasicBD bd, long idDominio) throws GovPayException {
		try {

			ContiAccreditoBD contiAccreditoBD = new ContiAccreditoBD(bd);
			DominiBD dominiBD = new DominiBD(bd);
			Dominio dominio = AnagraficaManager.getDominio(bd, idDominio);
			
			ContoAccredito lastContoAccredito = null;
			try{
				lastContoAccredito = contiAccreditoBD.getLastContoAccredito(idDominio);
			} catch (NotFoundException e) {
			} catch (MultipleResultException e) {}
			List<IbanAccredito> lstIban = dominiBD.getIbanAccreditoByIdDominio(idDominio);
			
			String idFlusso = lastContoAccredito != null ? lastContoAccredito.getIdFlusso() : UUID.randomUUID().toString().replaceAll("-", "");
			Date inizioValidita = lastContoAccredito != null ? lastContoAccredito.getDataOraInizioValidita() : new Date();
			Date inizioPubblicazione = lastContoAccredito != null ? lastContoAccredito.getDataOraPubblicazione() : new Date();
			CtInformativaContoAccredito newInfo = buildInformativaContoAccredito(dominio, idFlusso, inizioValidita, inizioPubblicazione, filtraIbanAbilitati(lstIban));
			
			//se non esiste una tabella conti accredito, oppure i dati sono cambiati dall'ultima inserita, ne inserisco una nuova
			if(lastContoAccredito == null || !equalsContoAccredito(newInfo, lastContoAccredito.getXml())) {
				if(lastContoAccredito != null) {
					//Aggiorno l'id flusso (prima lo avevo impostato uguale a quello dell'ultima tabella generata per verificarne l'uguaglianza)
					newInfo.setIdentificativoFlusso(UUID.randomUUID().toString().replaceAll("-", ""));
					Date dataInizioValidita = new Date();
					newInfo.setDataInizioValidita(dataInizioValidita);
					newInfo.setDataPubblicazione(dataInizioValidita);
				}
				it.govpay.bd.model.ContoAccredito contoAccredito = new it.govpay.bd.model.ContoAccredito();
				contoAccredito.setDataOraInizioValidita(newInfo.getDataInizioValidita());
				contoAccredito.setDataOraPubblicazione(newInfo.getDataPubblicazione());
				contoAccredito.setIdDominio(idDominio);
				contoAccredito.setIdFlusso(newInfo.getIdentificativoFlusso());
				contoAccredito.setXml(JaxbUtils.toByte(newInfo));
				
				contiAccreditoBD.insertContoAccredito(contoAccredito);

			}
			
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
		} catch (Exception e) {
			throw new GovPayException(GovPayExceptionEnum.ERRORE_INTERNO, e);
		}

	}

	private static boolean equalsContoAccredito(CtInformativaContoAccredito actual, byte[] expected) throws JAXBException, SAXException, XMLStreamException, GovPayException, IOException {

		byte[] byteActual = JaxbUtils.toByte(actual);
		return Arrays.equals(byteActual, expected);
	}

	private static List<IbanAccredito> filtraIbanAbilitati(List<IbanAccredito> lstIban) {
		List<IbanAccredito> newList = new ArrayList<IbanAccredito>();
		if(lstIban != null && !lstIban.isEmpty()) {
			for(IbanAccredito iban: lstIban) {
				if(iban.isAbilitato())
					newList.add(iban);
			}
		}
		return newList;
	} 


	private static CtInformativaContoAccredito buildInformativaContoAccredito(Dominio dominio, String idFlusso, Date inizioValidita, Date pubblicazione, List<IbanAccredito> lstIban) {
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
