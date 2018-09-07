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
package it.govpay.bd.model.converter;

import java.io.UnsupportedEncodingException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import org.openspcoop2.generic_project.exception.ServiceException;

import it.govpay.bd.model.Versamento;
import it.govpay.model.Anagrafica;
import it.govpay.model.Anagrafica.TIPO;
import it.govpay.model.Versamento.StatoVersamento;
import it.govpay.orm.IdApplicazione;
import it.govpay.orm.IdDominio;
import it.govpay.orm.IdTracciato;
import it.govpay.orm.IdUo;

public class VersamentoConverter {

	public static List<Versamento> toDTOList(List<it.govpay.orm.Versamento> versamenti) throws ServiceException {
		List<Versamento> lstDTO = new ArrayList<>();
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
			
			if(vo.getIdUo() != null)
				dto.setIdUo(vo.getIdUo().getId());
			
			if(vo.getIdDominio() != null)
				dto.setIdDominio(vo.getIdDominio().getId());
			dto.setNome(vo.getNome());
			dto.setCodVersamentoEnte(vo.getCodVersamentoEnte());
			dto.setStatoVersamento(StatoVersamento.valueOf(vo.getStatoVersamento()));
			dto.setDescrizioneStato(vo.getDescrizioneStato());
			dto.setImportoTotale(BigDecimal.valueOf(vo.getImportoTotale()));
			dto.setAggiornabile(vo.isAggiornabile());
			dto.setDataCreazione(vo.getDataCreazione());
			dto.setDataValidita(vo.getDataValidita());
			dto.setDataScadenza(vo.getDataScadenza());
			dto.setDataUltimoAggiornamento(vo.getDataOraUltimoAggiornamento());
			dto.setCausaleVersamento(vo.getCausaleVersamento());
			Anagrafica debitore = new Anagrafica();
			if(vo.getDebitoreTipo()!=null)
				debitore.setTipo(TIPO.valueOf(vo.getDebitoreTipo()));
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
			
			dto.setTassonomiaAvviso(vo.getTassonomiaAvviso()); 
			dto.setTassonomia(vo.getTassonomia());
			
			dto.setCodVersamentoLotto(vo.getCodVersamentoLotto()); 
			dto.setCodBundlekey(vo.getCodBundlekey()); 
			dto.setDatiAllegati(vo.getDatiAllegati());
			if(vo.getIncasso() != null) {
				dto.setIncasso(vo.getIncasso().equals(it.govpay.model.Versamento.INCASSO_TRUE) ? true : false);
			}
			dto.setAnomalie(vo.getAnomalie());
			
			dto.setIuvVersamento(vo.getIuvVersamento());
			dto.setNumeroAvviso(vo.getNumeroAvviso());
			dto.setAvvisatura(vo.getAvvisatura());
			dto.setTipoPagamento(vo.getTipoPagamento());
			
			// se il numero avviso e' impostato lo iuv proposto deve coincidere con quello inserito a partire dall'avviso
			// TODO controllare
			if(dto.getNumeroAvviso() !=  null) {
				dto.setIuvProposto(dto.getIuvVersamento());
			}
			
			dto.setDaAvvisare(vo.isDaAvvisare());
			dto.setCodAvvisatura(vo.getCodAvvisatura());
			if(vo.getIdTracciatoAvvisatura()!=null)
				dto.setIdTracciatoAvvisatura(vo.getIdTracciatoAvvisatura().getId());
			
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
			
			if(dto.getIdUo() > 0) {
				IdUo idUo = new IdUo();
				idUo.setId(dto.getIdUo());
				vo.setIdUo(idUo);
			}

			if(dto.getIdDominio() > 0) {
				IdDominio idDominio = new IdDominio();
				idDominio.setId(dto.getIdDominio());
				vo.setIdDominio(idDominio);
			}

			vo.setNome(dto.getNome());
			vo.setCodVersamentoEnte(dto.getCodVersamentoEnte());
			
			vo.setStatoVersamento(dto.getStatoVersamento().toString());
			vo.setDescrizioneStato(dto.getDescrizioneStato());
			vo.setImportoTotale(dto.getImportoTotale().doubleValue());
			vo.setAggiornabile(dto.isAggiornabile());
			vo.setDataCreazione(dto.getDataCreazione());
			vo.setDataValidita(dto.getDataValidita());
			vo.setDataScadenza(dto.getDataScadenza());
			vo.setDataOraUltimoAggiornamento(dto.getDataUltimoAggiornamento());
			if(dto.getCausaleVersamento() != null)
			vo.setCausaleVersamento(dto.getCausaleVersamento().encode());
			Anagrafica anagraficaDebitore = dto.getAnagraficaDebitore();
			if(anagraficaDebitore.getTipo()!=null)
				vo.setDebitoreTipo(anagraficaDebitore.getTipo().toString());
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
			
			vo.setTassonomiaAvviso(dto.getTassonomiaAvviso()); 
			vo.setTassonomia(dto.getTassonomia()); 
			vo.setCodVersamentoLotto(dto.getCodVersamentoLotto()); 
			vo.setCodBundlekey(dto.getCodBundlekey());
			vo.setDatiAllegati(dto.getDatiAllegati());
			
			if(dto.getIncasso()!=null) {
				vo.setIncasso(dto.getIncasso() ? it.govpay.model.Versamento.INCASSO_TRUE : it.govpay.model.Versamento.INCASSO_FALSE);
			}
			vo.setAnomalie(dto.getAnomalie());
			
			vo.setIuvVersamento(dto.getIuvVersamento());
			vo.setNumeroAvviso(dto.getNumeroAvviso());
			vo.setAvvisatura(dto.getAvvisatura());
			vo.setTipoPagamento(dto.getTipoPagamento());

			vo.setDaAvvisare(dto.isDaAvvisare());
			vo.setCodAvvisatura(dto.getCodAvvisatura());
			if(dto.getIdTracciatoAvvisatura()!=null) {
				IdTracciato idTracciato = new IdTracciato();
				idTracciato.setId(dto.getIdTracciatoAvvisatura());
				idTracciato.setIdTracciato(dto.getIdTracciatoAvvisatura());
				vo.setIdTracciatoAvvisatura(idTracciato);
			}

			return vo;
		} catch (UnsupportedEncodingException e) {
			throw new ServiceException(e);
		}
	}
}
