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
package it.govpay.bd.model.converter;

import it.govpay.model.Anagrafica;
import it.govpay.bd.model.Versamento;
import it.govpay.model.Versamento.StatoVersamento;
import it.govpay.orm.IdApplicazione;
import it.govpay.orm.IdUo;

import java.io.UnsupportedEncodingException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import org.openspcoop2.generic_project.exception.ServiceException;

public class VersamentoConverter {

	public static List<Versamento> toDTOList(List<it.govpay.orm.Versamento> versamenti) throws ServiceException {
		List<Versamento> lstDTO = new ArrayList<Versamento>();
		if(versamenti != null && !versamenti.isEmpty()) {
			for(it.govpay.orm.Versamento versamento: versamenti){
				lstDTO.add(toDTO(versamento));
			}
		}
		return lstDTO;
	}

	public static Versamento toDTO(it.govpay.orm.Versamento vo) throws ServiceException {
		try {
			Versamento dto = new Versamento();
			dto.setId(vo.getId());
			dto.setIdApplicazione(vo.getIdApplicazione().getId());
			dto.setIdUo(vo.getIdUo().getId());
			dto.setCodVersamentoEnte(vo.getCodVersamentoEnte());
			dto.setStatoVersamento(StatoVersamento.valueOf(vo.getStatoVersamento()));
			dto.setDescrizioneStato(vo.getDescrizioneStato());
			dto.setImportoTotale(BigDecimal.valueOf(vo.getImportoTotale()));
			dto.setAggiornabile(vo.isAggiornabile());
			dto.setDataCreazione(vo.getDataCreazione());
			dto.setDataScadenza(vo.getDataScadenza());
			dto.setDataUltimoAggiornamento(vo.getDataOraUltimoAggiornamento());
			dto.setCausaleVersamento(vo.getCausaleVersamento());
			Anagrafica debitore = new Anagrafica();
			debitore.setRagioneSociale(vo.getDebitoreAnagrafica());
			debitore.setCap(vo.getDebitoreCap());
			debitore.setCellulare(vo.getDebitoreCellulare());
			debitore.setCivico(vo.getDebitoreCivico());
			debitore.setCodUnivoco(vo.getDebitoreIdentificativo());
			debitore.setEmail(vo.getDebitoreEmail());
			debitore.setFax(vo.getDebitoreFax());
			debitore.setIndirizzo(vo.getDebitoreIndirizzo());
			debitore.setLocalita(vo.getDebitoreLocalita());
			debitore.setNazione(vo.getDebitoreNazione());
			debitore.setProvincia(vo.getDebitoreProvincia());
			debitore.setTelefono(vo.getDebitoreTelefono());
			dto.setAnagraficaDebitore(debitore);
			if(vo.getCodAnnoTributario() != null && !vo.getCodAnnoTributario().isEmpty())
				dto.setCodAnnoTributario(Integer.parseInt(vo.getCodAnnoTributario()));
			dto.setCodLotto(vo.getCodLotto());
			dto.setCodVersamentoLotto(vo.getCodVersamentoLotto()); 
			dto.setCodBundlekey(vo.getCodBundlekey()); 
			return dto;
		} catch (UnsupportedEncodingException e) {
			throw new ServiceException(e);
		}
	}

	public static it.govpay.orm.Versamento toVO(Versamento dto) throws ServiceException {
		try {
			it.govpay.orm.Versamento vo = new it.govpay.orm.Versamento();
			vo.setId(dto.getId());
			IdApplicazione idApplicazione = new IdApplicazione();
			idApplicazione.setId(dto.getIdApplicazione());
			vo.setIdApplicazione(idApplicazione);
			IdUo idUo = new IdUo();
			idUo.setId(dto.getIdUo());
			vo.setIdUo(idUo);
			vo.setCodVersamentoEnte(dto.getCodVersamentoEnte());
			vo.setStatoVersamento(dto.getStatoVersamento().toString());
			vo.setDescrizioneStato(dto.getDescrizioneStato());
			vo.setImportoTotale(dto.getImportoTotale().doubleValue());
			vo.setAggiornabile(dto.isAggiornabile());
			vo.setDataCreazione(dto.getDataCreazione());
			vo.setDataScadenza(dto.getDataScadenza());
			vo.setDataOraUltimoAggiornamento(dto.getDataUltimoAggiornamento());
			if(dto.getCausaleVersamento() != null)
			vo.setCausaleVersamento(dto.getCausaleVersamento().encode());
			Anagrafica anagraficaDebitore = dto.getAnagraficaDebitore();
			vo.setDebitoreAnagrafica(anagraficaDebitore.getRagioneSociale());
			vo.setDebitoreCap(anagraficaDebitore.getCap());
			vo.setDebitoreCellulare(anagraficaDebitore.getCellulare());
			vo.setDebitoreCivico(anagraficaDebitore.getCivico());
			vo.setDebitoreEmail(anagraficaDebitore.getEmail());
			vo.setDebitoreFax(anagraficaDebitore.getFax());
			vo.setDebitoreIdentificativo(anagraficaDebitore.getCodUnivoco());
			vo.setDebitoreIndirizzo(anagraficaDebitore.getIndirizzo());
			vo.setDebitoreLocalita(anagraficaDebitore.getLocalita());
			vo.setDebitoreNazione(anagraficaDebitore.getNazione());
			vo.setDebitoreProvincia(anagraficaDebitore.getProvincia());
			vo.setDebitoreTelefono(anagraficaDebitore.getTelefono());
			vo.setCodAnnoTributario(dto.getCodAnnoTributario() != null ? dto.getCodAnnoTributario().toString() : null);
			vo.setCodLotto(dto.getCodLotto());
			vo.setCodVersamentoLotto(dto.getCodVersamentoLotto()); 
			vo.setCodBundlekey(dto.getCodBundlekey());
			return vo;
		} catch (UnsupportedEncodingException e) {
			throw new ServiceException(e);
		}
	}
}
