/*
 * GovPay - Porta di Accesso al Nodo dei Pagamenti SPC 
 * http://www.gov4j.it/govpay
 * 
 * Copyright (c) 2014-2018 Link.it srl (http://www.link.it).
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
package it.govpay.bd.viste.model.converter;

import java.io.UnsupportedEncodingException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import org.openspcoop2.generic_project.exception.ServiceException;

import it.govpay.bd.model.Versamento;
import it.govpay.model.Anagrafica;
import it.govpay.model.Anagrafica.TIPO;
import it.govpay.model.Versamento.StatoPagamento;
import it.govpay.model.Versamento.StatoVersamento;
import it.govpay.model.Versamento.TipologiaTipoVersamento;

public class VersamentoIncassoConverter {

	
	public static List<Versamento> toDTOList(List<it.govpay.orm.VersamentoIncasso> versamenti) throws ServiceException {
		List<Versamento> lstDTO = new ArrayList<>();
		if(versamenti != null && !versamenti.isEmpty()) {
			for(it.govpay.orm.VersamentoIncasso versamento: versamenti){
				lstDTO.add(toDTO(versamento));
			}
		}
		return lstDTO;
	}

	public static Versamento toDTO(it.govpay.orm.VersamentoIncasso vo) throws ServiceException {
		try {
			Versamento dto = new Versamento();
			dto.setId(vo.getId());
			dto.setIdApplicazione(vo.getIdApplicazione().getId());
			
			if(vo.getIdUo() != null)
				dto.setIdUo(vo.getIdUo().getId());
			
			if(vo.getIdDominio() != null)
				dto.setIdDominio(vo.getIdDominio().getId());
			if(vo.getIdTipoVersamento() != null)
				dto.setIdTipoVersamento(vo.getIdTipoVersamento().getId());
			if(vo.getIdTipoVersamentoDominio() != null)
				dto.setIdTipoVersamentoDominio(vo.getIdTipoVersamentoDominio().getId());
			dto.setNome(vo.getNome());
			dto.setCodVersamentoEnte(vo.getCodVersamentoEnte());
			dto.setStatoVersamento(StatoVersamento.valueOf(vo.getStatoVersamento()));
			dto.setDescrizioneStato(vo.getDescrizioneStato());
			dto.setImportoTotale(BigDecimal.valueOf(vo.getImportoTotale()));
			dto.setAggiornabile(vo.getAggiornabile());
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
			dto.setAvvisaturaAbilitata(vo.getAvvisaturaAbilitata());
			dto.setAvvisaturaDaInviare(vo.getAvvisaturaDaInviare());
			dto.setAvvisaturaCodAvvisatura(vo.getAvvisaturaCodAvvisatura());
			dto.setAvvisaturaModalita(vo.getAvvisaturaModalita());
			dto.setAvvisaturaOperazione(vo.getAvvisaturaOperazione());
			dto.setAvvisaturaTipoPagamento(vo.getAvvisaturaTipoPagamento());
			if(vo.getIdTracciatoAvvisatura()!=null)
				dto.setIdTracciatoAvvisatura(vo.getIdTracciatoAvvisatura().getId());
			
			dto.setDataPagamento(vo.getDataPagamento());
			if(vo.getImportoPagato() != null)
				dto.setImportoPagato(BigDecimal.valueOf(vo.getImportoPagato()));
			if(vo.getImportoIncassato() != null)
			dto.setImportoIncassato(BigDecimal.valueOf(vo.getImportoIncassato()));
			if(vo.getStatoPagamento() != null)
				dto.setStatoPagamento(StatoPagamento.valueOf(vo.getStatoPagamento())); 
			
			dto.setAck(vo.getAck());
			dto.setAnomalo(vo.getAnomalo()); 
			
			dto.setIuvPagamento(vo.getIuvPagamento());
			
			dto.setDirezione(vo.getDirezione());
			dto.setDivisione(vo.getDivisione());
			dto.setIdSessione(vo.getIdSessione());
			
			dto.setNumeroRata(vo.getCodRata());
			dto.setCodDocumento(vo.getCodDocumento());
			
			if(vo.getTipo() != null)
				dto.setTipo(TipologiaTipoVersamento.toEnum(vo.getTipo()));
			
			return dto;
		} catch (UnsupportedEncodingException e) {
			throw new ServiceException(e);
		}
	}
}
