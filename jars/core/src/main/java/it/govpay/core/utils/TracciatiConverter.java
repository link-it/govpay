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
package it.govpay.core.utils;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import org.apache.commons.lang.ArrayUtils;
import org.openspcoop2.generic_project.exception.ServiceException;
import org.openspcoop2.utils.json.ValidationException;
import org.openspcoop2.utils.serialization.SerializationConfig;

import it.govpay.core.beans.EsitoOperazione;
import it.govpay.core.beans.tracciati.Contabilita;
import it.govpay.core.beans.tracciati.NuovoAllegatoPendenza;
import it.govpay.core.beans.tracciati.PendenzaPost;
import it.govpay.core.beans.tracciati.QuotaContabilita;
import it.govpay.core.beans.tracciati.Soggetto;
import it.govpay.core.beans.tracciati.TassonomiaAvviso;
import it.govpay.core.beans.tracciati.TipoSogliaVincoloPagamento;
import it.govpay.core.beans.tracciati.VocePendenza;
import it.govpay.core.exceptions.GovPayException;
import it.govpay.core.utils.rawutils.ConverterUtils;

public class TracciatiConverter {
	
	public static it.govpay.core.dao.commons.Versamento getVersamentoFromPendenza(PendenzaPost pendenza) throws ServiceException, ValidationException, GovPayException { 
		it.govpay.core.dao.commons.Versamento versamento = new it.govpay.core.dao.commons.Versamento();

		if(pendenza.getAnnoRiferimento() != null)
			versamento.setAnnoTributario(pendenza.getAnnoRiferimento().intValue());

		versamento.setCausale(pendenza.getCausale());
		versamento.setCodApplicazione(pendenza.getIdA2A());

		versamento.setCodDominio(pendenza.getIdDominio());
		versamento.setCodUnitaOperativa(pendenza.getIdUnitaOperativa());
		versamento.setCodVersamentoEnte(pendenza.getIdPendenza());
		versamento.setDataScadenza(pendenza.getDataScadenza());
		versamento.setDataValidita(pendenza.getDataValidita());
//		versamento.setDataCaricamento(pendenza.getDataCaricamento() != null ? pendenza.getDataCaricamento() : new Date());
		versamento.setDataCaricamento(new Date());
		versamento.setDebitore(toAnagraficaCommons(pendenza.getSoggettoPagatore()));
	
		versamento.setNome(pendenza.getNome());
		versamento.setTassonomia(pendenza.getTassonomia());
		
		if(pendenza.getTassonomiaAvviso() != null) {
			// valore tassonomia avviso non valido
			if(TassonomiaAvviso.fromValue(pendenza.getTassonomiaAvviso()) == null) {
				throw new ValidationException("Codifica inesistente per tassonomiaAvviso. Valore fornito [" + pendenza.getTassonomiaAvviso() + "] valori possibili " + ArrayUtils.toString(TassonomiaAvviso.values()));
			}

			versamento.setTassonomiaAvviso(pendenza.getTassonomiaAvviso());
		}
		
		versamento.setNumeroAvviso(pendenza.getNumeroAvviso());
		if(pendenza.getDatiAllegati() != null)
			versamento.setDatiAllegati(ConverterUtils.toJSON(pendenza.getDatiAllegati(),null));
 
		BigDecimal importoVociPendenza = fillSingoliVersamentiFromVociPendenza(versamento, pendenza.getVoci());
		
		// importo pendenza puo' essere null
		versamento.setImportoTotale(pendenza.getImporto() != null ? pendenza.getImporto() : importoVociPendenza); 
		
		// tipo Pendenza
		versamento.setCodTipoVersamento(pendenza.getIdTipoPendenza());
			
		// documento
		if(pendenza.getDocumento() != null) {
			it.govpay.core.dao.commons.Versamento.Documento documento = new it.govpay.core.dao.commons.Versamento.Documento();
			
			documento.setCodDocumento(pendenza.getDocumento().getIdentificativo());
			if(pendenza.getDocumento().getRata() != null) {
				documento.setCodRata(pendenza.getDocumento().getRata().intValue());
			}
			if(pendenza.getDocumento().getSoglia() != null) {
				// valore tassonomia avviso non valido
				if(TipoSogliaVincoloPagamento.fromValue(pendenza.getDocumento().getSoglia().getTipo()) == null) {
					throw new ValidationException("Codifica inesistente per tipo. Valore fornito [" 
								+ pendenza.getDocumento().getSoglia().getTipo() + "] valori possibili " + ArrayUtils.toString(TipoSogliaVincoloPagamento.values()));
				}
				
				if(pendenza.getDocumento().getSoglia().getGiorni() != null)
					documento.setGiorniSoglia(pendenza.getDocumento().getSoglia().getGiorni().intValue());
				documento.setTipoSoglia(pendenza.getDocumento().getSoglia().getTipo());
			}
			
			documento.setDescrizione(pendenza.getDocumento().getDescrizione());

			versamento.setDocumento(documento );
		}
		
		versamento.setProprieta(pendenza.getProprieta());
		
		versamento.setAllegati(toAllegatiPendenzaDTO(pendenza.getAllegati()));

		return versamento;
		
		
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
	
	public static BigDecimal fillSingoliVersamentiFromVociPendenza(it.govpay.core.dao.commons.Versamento versamento, List<VocePendenza> voci) throws ServiceException, GovPayException {

		BigDecimal importoTotale = BigDecimal.ZERO;
		
		if(voci != null && voci.size() > 0) {
			for (VocePendenza vocePendenza : voci) {
				it.govpay.core.dao.commons.Versamento.SingoloVersamento sv = new it.govpay.core.dao.commons.Versamento.SingoloVersamento();

				//sv.setCodTributo(value); ??

				sv.setCodSingoloVersamentoEnte(vocePendenza.getIdVocePendenza());
				if(vocePendenza.getDatiAllegati() != null)
					sv.setDatiAllegati(ConverterUtils.toJSON(vocePendenza.getDatiAllegati(),null));
				sv.setDescrizione(vocePendenza.getDescrizione());
				sv.setImporto(vocePendenza.getImporto());
				sv.setDescrizioneCausaleRPT(vocePendenza.getDescrizioneCausaleRPT());
				sv.setCodDominio(vocePendenza.getIdDominio());
				
				importoTotale = importoTotale.add(vocePendenza.getImporto());

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
					tributo.setIbanAppoggio(vocePendenza.getIbanAppoggio());
					tributo.setTipoContabilita(it.govpay.core.dao.commons.Versamento.SingoloVersamento.TipoContabilita.valueOf(vocePendenza.getTipoContabilita().name()));
					sv.setTributo(tributo);
				}
				
				sv.setContabilita(toStringDTO(vocePendenza.getContabilita()));
				
				if(vocePendenza.getContabilita() != null) {
					if(vocePendenza.getContabilita().getQuote() != null) {
						BigDecimal somma = BigDecimal.ZERO;
						for (QuotaContabilita voceContabilita : vocePendenza.getContabilita().getQuote()) {
							somma = somma.add(voceContabilita.getImporto());
						}
						
						if(somma.compareTo(vocePendenza.getImporto()) != 0) {
							throw new GovPayException(EsitoOperazione.VER_035, vocePendenza.getIdVocePendenza(),  versamento.getCodApplicazione(), versamento.getCodVersamentoEnte(),
								Double.toString(sv.getImporto().doubleValue()), Double.toString(somma.doubleValue()));
						}
					}
				}

				versamento.getSingoloVersamento().add(sv);
			}
		}
		
		return importoTotale;
	}
	
	public static String toStringDTO(Contabilita contabilita) throws ServiceException {
		if(contabilita == null)
			return null;
		
		it.govpay.model.Contabilita dto = toDTO(contabilita);
		
		return getDettaglioAsString(dto);
	}
	
	
	public static List<it.govpay.model.QuotaContabilita> toDTO(List<QuotaContabilita> dto) throws ServiceException {
		if(dto != null) {
			List<it.govpay.model.QuotaContabilita> rsModel = new ArrayList<it.govpay.model.QuotaContabilita>();
			for (QuotaContabilita contabilita : dto) {
				rsModel.add(toDTO(contabilita));
			}
			
			return rsModel;
		}
		
		return null;
	}

	public static it.govpay.model.Contabilita toDTO(Contabilita dto) throws ServiceException {
		it.govpay.model.Contabilita rsModel = new it.govpay.model.Contabilita();
		
		rsModel.setQuote(toDTO(dto.getQuote()));
		rsModel.setProprietaCustom(dto.getProprietaCustom());
		
		
		return rsModel;
	}
	
	public static it.govpay.model.QuotaContabilita toDTO(QuotaContabilita dto) throws ServiceException {
		it.govpay.model.QuotaContabilita rsModel = new it.govpay.model.QuotaContabilita();
		
		rsModel.setAccertamento(dto.getAccertamento());
		rsModel.setAnnoEsercizio(dto.getAnnoEsercizio().intValue());
		rsModel.setCapitolo(dto.getCapitolo());
		rsModel.setImporto(dto.getImporto());
		rsModel.setProprietaCustom(dto.getProprietaCustom());
		rsModel.setTitolo(dto.getTitolo());
		rsModel.setTipologia(dto.getTipologia());
		rsModel.setCategoria(dto.getCategoria());
		rsModel.setArticolo(dto.getArticolo());
		
		return rsModel;
	}
	
	private static String getDettaglioAsString(Object obj) throws ServiceException {
		if(obj != null) {
			SerializationConfig serializationConfig = new SerializationConfig();
			serializationConfig.setExcludes(Arrays.asList("jsonIdFilter"));
			serializationConfig.setDf(SimpleDateFormatUtils.newSimpleDateFormatSoloData());
			serializationConfig.setIgnoreNullValues(true);
			return ConverterUtils.toJSON(obj, null, serializationConfig);
		}
		return null;
	}
	
	private static List<it.govpay.core.dao.commons.Versamento.AllegatoPendenza> toAllegatiPendenzaDTO(List<NuovoAllegatoPendenza> allegati) {
		List<it.govpay.core.dao.commons.Versamento.AllegatoPendenza> allegatiDTO = null;
		
		if(allegati != null && allegati.size() > 0) {
			allegatiDTO = new ArrayList<>();
			
			for (NuovoAllegatoPendenza allegato : allegati) {
				it.govpay.core.dao.commons.Versamento.AllegatoPendenza allegatoDTO = new it.govpay.core.dao.commons.Versamento.AllegatoPendenza();
				
				allegatoDTO.setNome(allegato.getNome());
				allegatoDTO.setTipo(allegato.getTipo());
				allegatoDTO.setDescrizione(allegato.getDescrizione());
				allegatoDTO.setContenuto(allegato.getContenuto());
				
				allegatiDTO.add(allegatoDTO);
			}
		}
		
		return allegatiDTO;
	}
}
