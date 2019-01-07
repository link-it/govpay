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
package it.govpay.core.utils.client.v1;

import java.util.List;

import it.govpay.ec.v1.beans.PendenzaVerificata;
import it.govpay.ec.v1.beans.Soggetto;
import it.govpay.ec.v1.beans.VocePendenza;
import it.govpay.model.Versamento.StatoVersamento;

public class VerificaConverter {
	
	public static it.govpay.core.dao.commons.Versamento getVersamentoFromPendenzaVerificata(PendenzaVerificata pendenzaVerificata) {
		it.govpay.core.dao.commons.Versamento versamento = new it.govpay.core.dao.commons.Versamento();
		
		if(pendenzaVerificata.getAnnoRiferimento() != null)
			versamento.setAnnoTributario(pendenzaVerificata.getAnnoRiferimento().intValue());

		versamento.setCausale(pendenzaVerificata.getCausale());
		versamento.setCodApplicazione(pendenzaVerificata.getIdA2A());

		versamento.setCodDominio(pendenzaVerificata.getIdDominio());
		versamento.setCodUnitaOperativa(pendenzaVerificata.getIdUnitaOperativa());
		versamento.setCodVersamentoEnte(pendenzaVerificata.getIdPendenza());
		if(pendenzaVerificata.getDataScadenza() != null)
			versamento.setDataScadenza(pendenzaVerificata.getDataScadenza().toDate());
		if(pendenzaVerificata.getDataValidita() != null)
			versamento.setDataValidita(pendenzaVerificata.getDataValidita().toDate());
		versamento.setDebitore(toAnagraficaCommons(pendenzaVerificata.getSoggettoPagatore()));;
		versamento.setImportoTotale(pendenzaVerificata.getImporto());
		versamento.setCodVersamentoLotto(pendenzaVerificata.getCartellaPagamento());
		versamento.setDatiAllegati(pendenzaVerificata.getDatiAllegati());
		
		versamento.setTassonomia(pendenzaVerificata.getTassonomia());
		if(pendenzaVerificata.getTassonomiaAvviso() != null)
			versamento.setTassonomiaAvviso(pendenzaVerificata.getTassonomiaAvviso().toString());
		versamento.setNome(pendenzaVerificata.getNome());
		
		versamento.setStatoVersamento(StatoVersamento.NON_ESEGUITO);
		versamento.setNumeroAvviso(pendenzaVerificata.getNumeroAvviso());
		
		versamento.setAvvisaturaAbilitata(false);

		// voci pagamento
		fillSingoliVersamentiFromVociPendenzaBase(versamento, pendenzaVerificata.getVoci());
		
		return versamento;
	}
	
	public static void fillSingoliVersamentiFromVociPendenzaBase(it.govpay.core.dao.commons.Versamento versamento, List<VocePendenza> voci) {

		if(voci != null && voci.size() > 0) {
			for (VocePendenza vocePendenza : voci) {
				it.govpay.core.dao.commons.Versamento.SingoloVersamento sv = new it.govpay.core.dao.commons.Versamento.SingoloVersamento();

				//sv.setCodTributo(value); ??

				sv.setCodSingoloVersamentoEnte(vocePendenza.getIdVocePendenza());
				sv.setDatiAllegati(vocePendenza.getDatiAllegati());
				sv.setDescrizione(vocePendenza.getDescrizione());
				sv.setImporto(vocePendenza.getImporto());

				// Definisce i dati di un bollo telematico
				if(vocePendenza.getHashDocumento() != null && vocePendenza.getTipoBollo() != null && vocePendenza.getProvinciaResidenza() != null) {
					it.govpay.core.dao.commons.Versamento.SingoloVersamento.BolloTelematico bollo = new it.govpay.core.dao.commons.Versamento.SingoloVersamento.BolloTelematico();
					bollo.setHash(vocePendenza.getHashDocumento());
					bollo.setProvincia(vocePendenza.getProvinciaResidenza());
					bollo.setTipo(vocePendenza.getTipoBollo());
					sv.setBolloTelematico(bollo);
				} else if(vocePendenza.getCodEntrata() != null) { // Definisce i dettagli di incasso tramite riferimento in anagrafica GovPay.
					sv.setCodTributo(vocePendenza.getCodEntrata());

				} else { // Definisce i dettagli di incasso della singola entrata.
					it.govpay.core.dao.commons.Versamento.SingoloVersamento.Tributo tributo = new it.govpay.core.dao.commons.Versamento.SingoloVersamento.Tributo();
					tributo.setCodContabilita(vocePendenza.getCodiceContabilita());
					tributo.setIbanAccredito(vocePendenza.getIbanAccredito());
					tributo.setTipoContabilita(it.govpay.core.dao.commons.Versamento.SingoloVersamento.Tributo.TipoContabilita.valueOf(vocePendenza.getTipoContabilita().name()));
					sv.setTributo(tributo);
				}

				versamento.getSingoloVersamento().add(sv);
			}
		}
	}
	
	public static it.govpay.core.dao.commons.Anagrafica toAnagraficaCommons(Soggetto anagraficaRest) {
		it.govpay.core.dao.commons.Anagrafica anagraficaCommons = null;
		if(anagraficaRest != null) {
			anagraficaCommons = new it.govpay.core.dao.commons.Anagrafica();
			anagraficaCommons.setCap(anagraficaRest.getCap());
			anagraficaCommons.setCellulare(anagraficaRest.getCellulare());
			anagraficaCommons.setCivico(anagraficaRest.getCivico());
			anagraficaCommons.setCodUnivoco(anagraficaRest.getIdentificativo());
			anagraficaCommons.setEmail(anagraficaRest.getEmail());
			anagraficaCommons.setIndirizzo(anagraficaRest.getIndirizzo());
			anagraficaCommons.setLocalita(anagraficaRest.getLocalita());
			anagraficaCommons.setNazione(anagraficaRest.getNazione());
			anagraficaCommons.setProvincia(anagraficaRest.getProvincia());
			anagraficaCommons.setRagioneSociale(anagraficaRest.getAnagrafica());
			anagraficaCommons.setTipo(anagraficaRest.getTipo().name());
		}

		return anagraficaCommons;
	}
	
}
