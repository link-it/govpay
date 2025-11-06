/*
 * GovPay - Porta di Accesso al Nodo dei Pagamenti SPC
 * http://www.gov4j.it/govpay
 *
 * Copyright (c) 2014-2025 Link.it srl (http://www.link.it).
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
package it.govpay.core.utils;

import java.math.BigDecimal;
import java.util.List;

import jakarta.xml.bind.JAXBException;

import org.xml.sax.SAXException;

import it.gov.digitpa.schemas._2011.pagamenti.riversamento.CtDatiSingoliPagamenti;
import it.gov.digitpa.schemas._2011.pagamenti.riversamento.CtIdentificativoUnivoco;
import it.gov.digitpa.schemas._2011.pagamenti.riversamento.CtIdentificativoUnivocoPersonaG;
import it.gov.digitpa.schemas._2011.pagamenti.riversamento.CtIstitutoMittente;
import it.gov.digitpa.schemas._2011.pagamenti.riversamento.CtIstitutoRicevente;
import it.gov.digitpa.schemas._2011.pagamenti.riversamento.FlussoRiversamento;
import it.gov.digitpa.schemas._2011.pagamenti.riversamento.StTipoIdentificativoUnivoco;
import it.gov.digitpa.schemas._2011.pagamenti.riversamento.StTipoIdentificativoUnivocoPersG;
import it.govpay.bd.model.Fr;
import it.govpay.bd.viste.model.Rendicontazione;
import it.govpay.core.exceptions.NotFoundException;
import it.govpay.pagopa.beans.utils.JaxbUtils;

public class FrUtils {

	private FrUtils() {}

	/**
	 * Restituisce l'XML del flusso di rendicontazione. Se l'XML originale non e' presente,
	 * tenta di ricostruirlo a partire dai dati dell'FR e dalle rendicontazioni.
	 *
	 * @param fr Flusso di rendicontazione
	 * @param rendicontazioni Lista delle rendicontazioni associate al flusso
	 * @return byte array contenente l'XML del flusso
	 * @throws NotFoundException se l'XML non e' presente e non puo' essere ricostruito
	 */
	public static byte[] getXml(Fr fr, List<Rendicontazione> rendicontazioni) throws NotFoundException {
		if(fr == null) {
			throw new NotFoundException("Flusso di rendicontazione non valorizzato");
		}

		byte[] xml = fr.getXml();

		// Se l'XML originale non e' presente, prova a ricostruirlo
		if(xml == null) {
			if(rendicontazioni != null) {
				try {
					xml = buildXml(fr, rendicontazioni);
				} catch(JAXBException | SAXException e) {
					throw new NotFoundException("Impossibile ricostruire l'XML del flusso di rendicontazione [" + fr.getCodFlusso() + "]: " + e.getMessage(), e);
				}
			} else {
				throw new NotFoundException("XML del flusso di rendicontazione [" + fr.getCodFlusso() + "] non presente e rendicontazioni non fornite");
			}
		}

		return xml;
	}

	/**
	 * Ricostruisce l'XML del flusso di rendicontazione a partire dai dati dell'FR e dalle rendicontazioni
	 *
	 * @param fr Flusso di rendicontazione
	 * @param rendicontazioni Lista delle rendicontazioni associate al flusso
	 * @return byte array contenente l'XML ricostruito del flusso
	 * @throws JAXBException in caso di errore durante il marshalling
	 * @throws SAXException in caso di errore durante la validazione
	 */
	public static byte[] buildXml(Fr fr, List<Rendicontazione> rendicontazioni) throws JAXBException, SAXException {
		FlussoRiversamento flussoRiversamento = new FlussoRiversamento();

		// Valorizzazione campi base del flusso
		flussoRiversamento.setVersioneOggetto("1.0");
		flussoRiversamento.setIdentificativoFlusso(fr.getCodFlusso());
		flussoRiversamento.setDataOraFlusso(DateUtils.toLocalDateTime(fr.getDataFlusso()));
		flussoRiversamento.setIdentificativoUnivocoRegolamento(fr.getIur());
		flussoRiversamento.setDataRegolamento(DateUtils.toLocalDate(fr.getDataRegolamento()));

		// Istituto Mittente (PSP)
		CtIstitutoMittente istitutoMittente = new CtIstitutoMittente();
		CtIdentificativoUnivoco idMittente = new CtIdentificativoUnivoco();
		idMittente.setTipoIdentificativoUnivoco(StTipoIdentificativoUnivoco.B);
		idMittente.setCodiceIdentificativoUnivoco(fr.getCodPsp());
		istitutoMittente.setIdentificativoUnivocoMittente(idMittente);
		istitutoMittente.setDenominazioneMittente(fr.getRagioneSocialePsp());
		flussoRiversamento.setIstitutoMittente(istitutoMittente);

		// Codice BIC Banca di Riversamento (opzionale)
		if(fr.getCodBicRiversamento() != null) {
			flussoRiversamento.setCodiceBicBancaDiRiversamento(fr.getCodBicRiversamento());
		}

		// Istituto Ricevente (Ente Creditore)
		CtIstitutoRicevente istitutoRicevente = new CtIstitutoRicevente();
		CtIdentificativoUnivocoPersonaG idRicevente = new CtIdentificativoUnivocoPersonaG();
		idRicevente.setTipoIdentificativoUnivoco(StTipoIdentificativoUnivocoPersG.G);
		idRicevente.setCodiceIdentificativoUnivoco(fr.getCodDominio());
		istitutoRicevente.setIdentificativoUnivocoRicevente(idRicevente);
		istitutoRicevente.setDenominazioneRicevente(fr.getRagioneSocialeDominio());
		flussoRiversamento.setIstitutoRicevente(istitutoRicevente);

		// Totali
		flussoRiversamento.setNumeroTotalePagamenti(BigDecimal.valueOf(fr.getNumeroPagamenti()));
		flussoRiversamento.setImportoTotalePagamenti(fr.getImportoTotalePagamenti());

		// Dati singoli pagamenti
		if(rendicontazioni != null && !rendicontazioni.isEmpty()) {
			for(Rendicontazione vistaRendicontazione : rendicontazioni) {
				// Accesso all'oggetto rendicontazione annidicato nella vista
				it.govpay.bd.model.Rendicontazione rendicontazione = vistaRendicontazione.getRendicontazione();

				CtDatiSingoliPagamenti datiSingoliPagamenti = new CtDatiSingoliPagamenti();
				datiSingoliPagamenti.setIdentificativoUnivocoVersamento(rendicontazione.getIuv());
				datiSingoliPagamenti.setIdentificativoUnivocoRiscossione(rendicontazione.getIur());

				if(rendicontazione.getIndiceDati() != null) {
					datiSingoliPagamenti.setIndiceDatiSingoloPagamento(rendicontazione.getIndiceDati().intValue());
				}

				datiSingoliPagamenti.setSingoloImportoPagato(rendicontazione.getImporto());
				datiSingoliPagamenti.setCodiceEsitoSingoloPagamento(String.valueOf(rendicontazione.getEsito().getCodifica()));
				datiSingoliPagamenti.setDataEsitoSingoloPagamento(DateUtils.toLocalDate(rendicontazione.getData()));

				flussoRiversamento.getDatiSingoliPagamentis().add(datiSingoliPagamenti);
			}
		}

		// Marshalling dell'oggetto JAXB in byte array
		return JaxbUtils.toByte(flussoRiversamento);
	}
}
