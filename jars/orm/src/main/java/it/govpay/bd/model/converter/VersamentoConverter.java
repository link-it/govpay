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

import org.apache.commons.lang.StringUtils;
import org.openspcoop2.generic_project.exception.ServiceException;

import it.govpay.bd.model.Versamento;
import it.govpay.model.Anagrafica;
import it.govpay.model.Anagrafica.TIPO;
import it.govpay.model.Versamento.StatoPagamento;
import it.govpay.model.Versamento.StatoVersamento;
import it.govpay.model.Versamento.TipoSogliaVersamento;
import it.govpay.model.Versamento.TipologiaTipoVersamento;
import it.govpay.orm.IdApplicazione;
import it.govpay.orm.IdDocumento;
import it.govpay.orm.IdDominio;
import it.govpay.orm.IdTipoVersamento;
import it.govpay.orm.IdTipoVersamentoDominio;
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
			
			if(vo.getIdTipoVersamento() != null)
				dto.setIdTipoVersamento(vo.getIdTipoVersamento().getId());
			
			if(vo.getIdTipoVersamentoDominio() != null)
				dto.setIdTipoVersamentoDominio(vo.getIdTipoVersamentoDominio().getId());
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
			dto.setAvvisaturaAbilitata(vo.isAvvisaturaAbilitata());
			dto.setAvvisaturaDaInviare(vo.isAvvisaturaDaInviare());
			dto.setAvvisaturaCodAvvisatura(vo.getAvvisaturaCodAvvisatura());
			dto.setAvvisaturaModalita(vo.getAvvisaturaModalita());
			dto.setAvvisaturaOperazione(vo.getAvvisaturaOperazione());
			dto.setAvvisaturaTipoPagamento(vo.getAvvisaturaTipoPagamento());
			if(vo.getIdTracciatoAvvisatura()!=null)
				dto.setIdTracciatoAvvisatura(vo.getIdTracciatoAvvisatura().getId());
			
			// se il numero avviso e' impostato lo iuv proposto deve coincidere con quello inserito a partire dall'avviso
			// TODO controllare
			if(dto.getNumeroAvviso() !=  null) {
				dto.setIuvProposto(dto.getIuvVersamento());
			}
			
			dto.setAck(vo.isAck());
			dto.setAnomalo(vo.isAnomalo());
			
			dto.setDirezione(vo.getDirezione());
			dto.setDivisione(vo.getDivisione());
			dto.setIdSessione(vo.getIdSessione());
			
			dto.setDataPagamento(vo.getDataPagamento());
			if(vo.getImportoPagato() != null)
				dto.setImportoPagato(BigDecimal.valueOf(vo.getImportoPagato()));
			if(vo.getImportoIncassato() != null)
			dto.setImportoIncassato(BigDecimal.valueOf(vo.getImportoIncassato()));
			if(vo.getStatoPagamento() != null)
				dto.setStatoPagamento(StatoPagamento.valueOf(vo.getStatoPagamento())); 
			dto.setIuvPagamento(vo.getIuvPagamento());
			
			dto.setDataPagamento(vo.getDataPagamento());
			if(vo.getImportoPagato() != null)
				dto.setImportoPagato(BigDecimal.valueOf(vo.getImportoPagato()));
			if(vo.getImportoIncassato() != null)
			dto.setImportoIncassato(BigDecimal.valueOf(vo.getImportoIncassato()));
			if(vo.getStatoPagamento() != null)
				dto.setStatoPagamento(StatoPagamento.valueOf(vo.getStatoPagamento())); 
			dto.setIuvPagamento(vo.getIuvPagamento());
			
			if(vo.getIdDocumento() != null)
				dto.setIdDocumento(vo.getIdDocumento().getId());
			if(vo.getCodRata() != null) {
				if(vo.getCodRata().startsWith(TipoSogliaVersamento.ENTRO.toString())) {
					dto.setTipoSoglia(TipoSogliaVersamento.ENTRO);
					String gg = vo.getCodRata().substring(vo.getCodRata().indexOf(TipoSogliaVersamento.ENTRO.toString())+ TipoSogliaVersamento.ENTRO.toString().length());
					dto.setGiorniSoglia(Integer.parseInt(gg));
				} else if(vo.getCodRata().startsWith(TipoSogliaVersamento.OLTRE.toString())) {
					dto.setTipoSoglia(TipoSogliaVersamento.OLTRE);
					String gg = vo.getCodRata().substring(vo.getCodRata().indexOf(TipoSogliaVersamento.OLTRE.toString())+ TipoSogliaVersamento.OLTRE.toString().length());
					dto.setGiorniSoglia(Integer.parseInt(gg));
				} else {
					dto.setNumeroRata(Integer.parseInt(vo.getCodRata()));
				}
			}
			
			if(vo.getTipo() != null)
				dto.setTipo(TipologiaTipoVersamento.toEnum(vo.getTipo()));
			
			dto.setDataNotificaAvviso(vo.getDataNotificaAvviso());
			dto.setAvvisoNotificato(vo.getAvvisoNotificato());
			dto.setAvvMailDataPromemoriaScadenza(vo.getAvvMailDataPromScadenza()); 
			dto.setAvvMailPromemoriaScadenzaNotificato(vo.getAvvMailPromScadNotificato());
			dto.setAvvAppIODataPromemoriaScadenza(vo.getAvvAppIoDataPromScadenza()); 
			dto.setAvvAppIOPromemoriaScadenzaNotificato(vo.getAvvAppIoPromScadNotificato());
			
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
			
			if(dto.getIdTipoVersamento() > 0) {
				IdTipoVersamento idTipoVersamento = new IdTipoVersamento();
				idTipoVersamento.setId(dto.getIdTipoVersamento());
				vo.setIdTipoVersamento(idTipoVersamento);
			}
			
			if(dto.getIdTipoVersamentoDominio() > 0) {
				IdTipoVersamentoDominio idTipoVersamento = new IdTipoVersamentoDominio();
				idTipoVersamento.setId(dto.getIdTipoVersamentoDominio());
				vo.setIdTipoVersamentoDominio(idTipoVersamento);
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
			vo.setSrcDebitoreIdentificativo(anagraficaDebitore.getCodUnivoco().toUpperCase()); // per le ricerche
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
			vo.setAvvisaturaAbilitata(dto.isAvvisaturaAbilitata());
			vo.setAvvisaturaDaInviare(dto.isAvvisaturaDaInviare());
			vo.setAvvisaturaCodAvvisatura(dto.getAvvisaturaCodAvvisatura());
			vo.setAvvisaturaModalita(dto.getAvvisaturaModalita());
			vo.setAvvisaturaOperazione(dto.getAvvisaturaOperazione());
			vo.setAvvisaturaTipoPagamento(dto.getAvvisaturaTipoPagamento());
			
			if(dto.getIdTracciatoAvvisatura()!=null) {
				IdTracciato idTracciato = new IdTracciato();
				idTracciato.setId(dto.getIdTracciatoAvvisatura());
				idTracciato.setIdTracciato(dto.getIdTracciatoAvvisatura());
				vo.setIdTracciatoAvvisatura(idTracciato);
			}
			
			vo.setAck(dto.isAck());
			vo.setAnomalo(dto.isAnomalo());
			
			vo.setDirezione(dto.getDirezione());
			vo.setDivisione(dto.getDivisione());
			vo.setIdSessione(dto.getIdSessione());
			
			vo.setDataPagamento(dto.getDataPagamento());
			if(dto.getImportoPagato() != null)
				vo.setImportoPagato(dto.getImportoPagato().doubleValue());
			if(dto.getImportoIncassato() != null)
				vo.setImportoIncassato(dto.getImportoIncassato().doubleValue());
			if(dto.getStatoPagamento() != null)
				vo.setStatoPagamento(dto.getStatoPagamento().toString()); 
			vo.setIuvPagamento(dto.getIuvPagamento());
			
			// src_iuv
			if(StringUtils.isNotEmpty(dto.getIuvPagamento())) {
				vo.setSrcIuv(dto.getIuvPagamento().toUpperCase());
			} else if(StringUtils.isNotEmpty(dto.getIuvVersamento())) {
				vo.setSrcIuv(dto.getIuvVersamento().toUpperCase());
			} else{
				// donothing
			}
			
			if(dto.getIdDocumento() != null &&  dto.getIdDocumento() > 0) {
				IdDocumento idDocumento = new IdDocumento();
				idDocumento.setId(dto.getIdDocumento());
				vo.setIdDocumento(idDocumento);
			}
			
			if(dto.getNumeroRata() != null)
				vo.setCodRata(dto.getNumeroRata().toString());
			if(dto.getTipoSoglia() != null && dto.getGiorniSoglia() != null)
				vo.setCodRata(dto.getTipoSoglia().toString() + dto.getGiorniSoglia());
			
			if(dto.getTipo() != null)
				vo.setTipo(dto.getTipo().getCodifica());
			
			vo.setDataNotificaAvviso(dto.getDataNotificaAvviso());
			vo.setAvvisoNotificato(dto.getAvvisoNotificato());
			vo.setAvvMailDataPromScadenza(dto.getAvvMailDataPromemoriaScadenza()); 
			vo.setAvvMailPromScadNotificato(dto.getAvvMailPromemoriaScadenzaNotificato());
			vo.setAvvAppIoDataPromScadenza(dto.getAvvAppIODataPromemoriaScadenza()); 
			vo.setAvvAppIoPromScadNotificato(dto.getAvvAppIOPromemoriaScadenzaNotificato());
			
			return vo;
		} catch (UnsupportedEncodingException e) {
			throw new ServiceException(e);
		}
	}
}
