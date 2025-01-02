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
package it.govpay.core.ec.v2.converter;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;
import org.openspcoop2.utils.serialization.SerializationConfig;

import it.govpay.core.beans.Costanti;
import it.govpay.core.exceptions.IOException;
import it.govpay.core.exceptions.ValidationException;
import it.govpay.core.utils.SimpleDateFormatUtils;
import it.govpay.core.utils.rawutils.ConverterUtils;
import it.govpay.ec.v2.beans.Contabilita;
import it.govpay.ec.v2.beans.NuovaPendenza;
import it.govpay.ec.v2.beans.NuovaVocePendenza;
import it.govpay.ec.v2.beans.NuovoAllegatoPendenza;
import it.govpay.ec.v2.beans.PendenzaVerificata;
import it.govpay.ec.v2.beans.ProprietaPendenza;
import it.govpay.ec.v2.beans.QuotaContabilita;
import it.govpay.ec.v2.beans.Soggetto;
import it.govpay.ec.v2.beans.TipoSogliaVincoloPagamento;
import it.govpay.ec.v2.beans.VoceDescrizioneImporto;
import it.govpay.model.Versamento.StatoVersamento;

public class VerificaConverter {
	
	private VerificaConverter() {}
	
	public static it.govpay.core.beans.commons.Versamento getVersamentoFromPendenzaVerificata(PendenzaVerificata pendenzaVerificata) throws ValidationException, IOException {
		it.govpay.core.beans.commons.Versamento versamento = new it.govpay.core.beans.commons.Versamento();
		
		NuovaPendenza pendenza = pendenzaVerificata.getPendenza();
		
		if(pendenza.getAnnoRiferimento() != null)
			versamento.setAnnoTributario(pendenza.getAnnoRiferimento().intValue());

		versamento.setCausale(pendenza.getCausale());
		versamento.setCodApplicazione(pendenza.getIdA2A());

		versamento.setCodDominio(pendenza.getIdDominio());
		versamento.setCodUnitaOperativa(pendenza.getIdUnitaOperativa());
		versamento.setCodVersamentoEnte(pendenza.getIdPendenza());
		versamento.setDataScadenza(pendenza.getDataScadenza()); 
		versamento.setDataValidita(pendenza.getDataValidita());
		versamento.setDebitore(toAnagraficaCommons(pendenza.getSoggettoPagatore()));
		versamento.setImportoTotale(pendenza.getImporto());
		versamento.setCodVersamentoLotto(pendenza.getCartellaPagamento());
		
		if(pendenza.getDatiAllegati() != null)
			versamento.setDatiAllegati(ConverterUtils.toJSON(pendenza.getDatiAllegati()));
		
		versamento.setTassonomia(pendenza.getTassonomia());
		
		versamento.setStatoVersamento(StatoVersamento.NON_ESEGUITO);
		versamento.setNumeroAvviso(pendenza.getNumeroAvviso());
		
		// voci pagamento
		fillSingoliVersamentiFromVociPendenzaBase(versamento, pendenza.getVoci());
		
		// tipo Pendenza
		versamento.setCodTipoVersamento(pendenza.getIdTipoPendenza());
		
		// documento
		if(pendenza.getDocumento() != null) {
			it.govpay.core.beans.commons.Versamento.Documento documento = new it.govpay.core.beans.commons.Versamento.Documento();
			

			documento.setCodDocumento(pendenza.getDocumento().getIdentificativo());
			if(pendenza.getDocumento().getRata() != null) {
				documento.setCodRata(pendenza.getDocumento().getRata().intValue());
			}
			documento.setDescrizione(pendenza.getDocumento().getDescrizione());
			if(pendenza.getDocumento().getSoglia() != null) {
				// valore tassonomia avviso non valido
				if(TipoSogliaVincoloPagamento.fromValue(pendenza.getDocumento().getSoglia().getTipo()) == null) {
					throw new ValidationException("Codifica inesistente per tipo. Valore fornito [" 
								+ pendenza.getDocumento().getSoglia().getTipo() + "] valori possibili " + ArrayUtils.toString(TipoSogliaVincoloPagamento.values()));
				}
				
				if(pendenza.getDocumento().getSoglia().getGiorni() != null) {
					documento.setGiorniSoglia(pendenza.getDocumento().getSoglia().getGiorni().intValue());
				}
				documento.setTipoSoglia(pendenza.getDocumento().getSoglia().getTipo());
			}

			versamento.setDocumento(documento );
		}
		
		versamento.setProprieta(toProprietaPendenzaDTO(pendenza.getProprieta()));
		
		versamento.setAllegati(toAllegatiPendenzaDTO(pendenza.getAllegati()));
		
		return versamento;
	}
	
	public static void fillSingoliVersamentiFromVociPendenzaBase(it.govpay.core.beans.commons.Versamento versamento, List<NuovaVocePendenza> voci) throws ValidationException, IOException {

		if(voci != null && !voci.isEmpty()) {
			for (NuovaVocePendenza vocePendenza : voci) {
				it.govpay.core.beans.commons.Versamento.SingoloVersamento sv = new it.govpay.core.beans.commons.Versamento.SingoloVersamento();

				sv.setCodSingoloVersamentoEnte(vocePendenza.getIdVocePendenza());
				if(vocePendenza.getDatiAllegati() != null)
					sv.setDatiAllegati(ConverterUtils.toJSON(vocePendenza.getDatiAllegati()));
				sv.setDescrizione(vocePendenza.getDescrizione());
				sv.setImporto(vocePendenza.getImporto());
				sv.setDescrizioneCausaleRPT(vocePendenza.getDescrizioneCausaleRPT());
				sv.setContabilita(contabilitaToStringDTO(vocePendenza.getContabilita()));
				sv.setCodDominio(vocePendenza.getIdDominio());
				sv.setMetadata(PendenzeConverter.toMetadataDTO(vocePendenza.getMetadata()));

				// Definisce i dati di un bollo telematico
				if(vocePendenza.getHashDocumento() != null && vocePendenza.getTipoBollo() != null && vocePendenza.getProvinciaResidenza() != null) {
					it.govpay.core.beans.commons.Versamento.SingoloVersamento.BolloTelematico bollo = new it.govpay.core.beans.commons.Versamento.SingoloVersamento.BolloTelematico();
					bollo.setHash(vocePendenza.getHashDocumento());
					bollo.setProvincia(vocePendenza.getProvinciaResidenza());
					bollo.setTipo(vocePendenza.getTipoBollo());
					 
					String codiceTassonomicoPagoPA = vocePendenza.getCodiceTassonomicoPagoPA();
					String[] split = codiceTassonomicoPagoPA.split("/");
					bollo.setTipoContabilita(it.govpay.core.beans.commons.Versamento.SingoloVersamento.TipoContabilita.toEnum(split[0]));
					bollo.setCodContabilita(split[1]);
					
					sv.setBolloTelematico(bollo);
				} else if(vocePendenza.getCodEntrata() != null) { // Definisce i dettagli di incasso tramite riferimento in anagrafica GovPay.
					sv.setCodTributo(vocePendenza.getCodEntrata());

				} else { // Definisce i dettagli di incasso della singola entrata.
					it.govpay.core.beans.commons.Versamento.SingoloVersamento.Tributo tributo = new it.govpay.core.beans.commons.Versamento.SingoloVersamento.Tributo();
					
					String codiceTassonomicoPagoPA = vocePendenza.getCodiceTassonomicoPagoPA();
					String[] split = codiceTassonomicoPagoPA.split("/");
					tributo.setTipoContabilita(it.govpay.core.beans.commons.Versamento.SingoloVersamento.TipoContabilita.toEnum(split[0]));
					tributo.setCodContabilita(split[1]);
					tributo.setIbanAccredito(vocePendenza.getIbanAccredito());
					tributo.setIbanAppoggio(vocePendenza.getIbanAppoggio());
					sv.setTributo(tributo);
				}

				versamento.getSingoloVersamento().add(sv);
			}
		}
	}
	
	public static it.govpay.core.beans.commons.Anagrafica toAnagraficaCommons(Soggetto anagraficaRest) {
		it.govpay.core.beans.commons.Anagrafica anagraficaCommons = new it.govpay.core.beans.commons.Anagrafica();
		if(anagraficaRest != null) {
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
			if(anagraficaRest.getTipo() != null) {
				anagraficaCommons.setTipo(anagraficaRest.getTipo().name());
			}
		}
		
		// Il vincolo di obbligatorieta' del soggetto pagatore e' stato eliminato per consentire di acquisire pendenze senza indicare il debitore.
		// in questo caso impostiamo i valori di default per gli identificativi
		if(StringUtils.isBlank(anagraficaCommons.getCodUnivoco())) {
			anagraficaCommons.setCodUnivoco(Costanti.IDENTIFICATIVO_DEBITORE_ANONIMO);
		}
		if(StringUtils.isBlank(anagraficaCommons.getRagioneSociale())) {
			anagraficaCommons.setRagioneSociale(Costanti.IDENTIFICATIVO_DEBITORE_ANONIMO); 
		}

		return anagraficaCommons;
	} 
	
	public static it.govpay.core.beans.tracciati.ProprietaPendenza toProprietaPendenzaDTO(ProprietaPendenza proprieta) {
		it.govpay.core.beans.tracciati.ProprietaPendenza dto = null;
		if(proprieta != null) {
			dto = new it.govpay.core.beans.tracciati.ProprietaPendenza();
			
			if(proprieta.getDescrizioneImporto() != null && !proprieta.getDescrizioneImporto().isEmpty()) {
				List<it.govpay.core.beans.tracciati.VoceDescrizioneImporto> descrizioneImporto = new ArrayList<>();
				for (VoceDescrizioneImporto vdI : proprieta.getDescrizioneImporto()) {
					it.govpay.core.beans.tracciati.VoceDescrizioneImporto voce = new it.govpay.core.beans.tracciati.VoceDescrizioneImporto();
					
					voce.setVoce(vdI.getVoce());
					voce.setImporto(vdI.getImporto());
					
					descrizioneImporto.add(voce);
				}
				dto.setDescrizioneImporto(descrizioneImporto);
			}
			dto.setLineaTestoRicevuta1(proprieta.getLineaTestoRicevuta1());
			dto.setLineaTestoRicevuta2(proprieta.getLineaTestoRicevuta2());
			if(proprieta.getLinguaSecondaria() != null) {
				switch(it.govpay.ec.v2.beans.LinguaSecondaria.fromValue(proprieta.getLinguaSecondaria())) {
				case DE:
					dto.setLinguaSecondaria(it.govpay.core.beans.tracciati.LinguaSecondaria.DE);
					break;
				case EN:
					dto.setLinguaSecondaria(it.govpay.core.beans.tracciati.LinguaSecondaria.EN);
					break;
				case FALSE:
					dto.setLinguaSecondaria(it.govpay.core.beans.tracciati.LinguaSecondaria.FALSE);
					break;
				case FR:
					dto.setLinguaSecondaria(it.govpay.core.beans.tracciati.LinguaSecondaria.FR);
					break;
				case SL:
					dto.setLinguaSecondaria(it.govpay.core.beans.tracciati.LinguaSecondaria.SL);
					break;
				}				
			}
			dto.setLinguaSecondariaCausale(proprieta.getLinguaSecondariaCausale());
			dto.setInformativaImportoAvviso(proprieta.getInformativaImportoAvviso());
			dto.setLinguaSecondariaInformativaImportoAvviso(proprieta.getLinguaSecondariaInformativaImportoAvviso());
		}
		
		return dto;
	}
	
	public static String contabilitaToStringDTO(Contabilita contabilita) throws IOException {
		if(contabilita == null)
			return null;
		
		it.govpay.model.Contabilita dto = toDTO(contabilita);
		
		return getDettaglioAsString(dto);
	}
	
	public static List<it.govpay.model.QuotaContabilita> toDTO(List<QuotaContabilita> dto) {
		if(dto != null) {
			List<it.govpay.model.QuotaContabilita> rsModel = new ArrayList<>();
			for (QuotaContabilita contabilita : dto) {
				rsModel.add(toDTO(contabilita));
			}
			
			return rsModel;
		}
		
		return null;
	}

	public static it.govpay.model.Contabilita toDTO(Contabilita dto) {
		it.govpay.model.Contabilita rsModel = new it.govpay.model.Contabilita();
		
		rsModel.setQuote(toDTO(dto.getQuote()));
		rsModel.setProprietaCustom(dto.getProprietaCustom());
		
		
		return rsModel;
	}
	
	public static it.govpay.model.QuotaContabilita toDTO(QuotaContabilita dto) {
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
	
	private static String getDettaglioAsString(Object obj) throws IOException {
		if(obj != null) {
			SerializationConfig serializationConfig = new SerializationConfig();
			serializationConfig.setExcludes(Arrays.asList("jsonIdFilter"));
			serializationConfig.setDf(SimpleDateFormatUtils.newSimpleDateFormatSoloData());
			serializationConfig.setIgnoreNullValues(true);
			return ConverterUtils.toJSON(obj, null, serializationConfig);
		}
		return null;
	}
	
	private static List<it.govpay.core.beans.commons.Versamento.AllegatoPendenza> toAllegatiPendenzaDTO(List<NuovoAllegatoPendenza> allegati) {
		List<it.govpay.core.beans.commons.Versamento.AllegatoPendenza> allegatiDTO = null;
		
		if(allegati != null && !allegati.isEmpty()) {
			allegatiDTO = new ArrayList<>();
			
			for (NuovoAllegatoPendenza allegato : allegati) {
				it.govpay.core.beans.commons.Versamento.AllegatoPendenza allegatoDTO = new it.govpay.core.beans.commons.Versamento.AllegatoPendenza();
				
				allegatoDTO.setNome(allegato.getNome());
				allegatoDTO.setTipo(allegato.getTipo());
				allegatoDTO.setContenuto(allegato.getContenuto());
				allegatoDTO.setDescrizione(allegato.getDescrizione());
				
				allegatiDTO.add(allegatoDTO);
			}
		}
		
		return allegatiDTO;
	}
}
