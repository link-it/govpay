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
package it.govpay.core.ec.v2.converter;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.openspcoop2.generic_project.exception.ServiceException;
import org.openspcoop2.utils.json.ValidationException;
import org.openspcoop2.utils.serialization.SerializationConfig;

import it.govpay.core.utils.SimpleDateFormatUtils;
import it.govpay.core.utils.rawutils.ConverterUtils;
import it.govpay.ec.v2.beans.Contabilita;
import it.govpay.ec.v2.beans.NuovaVocePendenza;
import it.govpay.ec.v2.beans.NuovoAllegatoPendenza;
import it.govpay.ec.v2.beans.PendenzaVerificata;
import it.govpay.ec.v2.beans.ProprietaPendenza;
import it.govpay.ec.v2.beans.QuotaContabilita;
import it.govpay.ec.v2.beans.Soggetto;
import it.govpay.ec.v2.beans.VoceDescrizioneImporto;
import it.govpay.model.Versamento.StatoVersamento;

public class VerificaConverter {
	
	public static it.govpay.core.dao.commons.Versamento getVersamentoFromPendenzaVerificata(PendenzaVerificata pendenzaVerificata) throws ValidationException, ServiceException {
		it.govpay.core.dao.commons.Versamento versamento = new it.govpay.core.dao.commons.Versamento();
		
		if(pendenzaVerificata.getAnnoRiferimento() != null)
			versamento.setAnnoTributario(pendenzaVerificata.getAnnoRiferimento().intValue());

		versamento.setCausale(pendenzaVerificata.getCausale());
		versamento.setCodApplicazione(pendenzaVerificata.getIdA2A());

		versamento.setCodDominio(pendenzaVerificata.getIdDominio());
		versamento.setCodUnitaOperativa(pendenzaVerificata.getIdUnitaOperativa());
		versamento.setCodVersamentoEnte(pendenzaVerificata.getIdPendenza());
		versamento.setDataScadenza(pendenzaVerificata.getDataScadenza()); 
		versamento.setDataValidita(pendenzaVerificata.getDataValidita());
		versamento.setDebitore(toAnagraficaCommons(pendenzaVerificata.getSoggettoPagatore()));;
		versamento.setImportoTotale(pendenzaVerificata.getImporto());
		versamento.setCodVersamentoLotto(pendenzaVerificata.getCartellaPagamento());
		
		if(pendenzaVerificata.getDatiAllegati() != null)
			versamento.setDatiAllegati(ConverterUtils.toJSON(pendenzaVerificata.getDatiAllegati(),null));
		
		versamento.setTassonomia(pendenzaVerificata.getTassonomia());
		
		versamento.setStatoVersamento(StatoVersamento.NON_ESEGUITO);
		versamento.setNumeroAvviso(pendenzaVerificata.getNumeroAvviso());
		
		// voci pagamento
		fillSingoliVersamentiFromVociPendenzaBase(versamento, pendenzaVerificata.getVoci());
		
		// tipo Pendenza
		versamento.setCodTipoVersamento(pendenzaVerificata.getIdTipoPendenza());
		
		// documento
		if(pendenzaVerificata.getDocumento() != null) {
			it.govpay.core.dao.commons.Versamento.Documento documento = new it.govpay.core.dao.commons.Versamento.Documento();
			
			documento.setCodDocumento(pendenzaVerificata.getDocumento().getIdentificativo());
			if(pendenzaVerificata.getDocumento().getRata() != null)
			documento.setCodRata(pendenzaVerificata.getDocumento().getRata().intValue());
			documento.setDescrizione(pendenzaVerificata.getDocumento().getDescrizione());

			versamento.setDocumento(documento );
		}
		
		versamento.setProprieta(toProprietaPendenzaDTO(pendenzaVerificata.getProprieta()));
		
		versamento.setAllegati(toAllegatiPendenzaDTO(pendenzaVerificata.getAllegati()));
		
		return versamento;
	}
	
	public static void fillSingoliVersamentiFromVociPendenzaBase(it.govpay.core.dao.commons.Versamento versamento, List<NuovaVocePendenza> voci) throws ServiceException, ValidationException {

		if(voci != null && voci.size() > 0) {
			for (NuovaVocePendenza vocePendenza : voci) {
				it.govpay.core.dao.commons.Versamento.SingoloVersamento sv = new it.govpay.core.dao.commons.Versamento.SingoloVersamento();

				sv.setCodSingoloVersamentoEnte(vocePendenza.getIdVocePendenza());
				if(vocePendenza.getDatiAllegati() != null)
					sv.setDatiAllegati(ConverterUtils.toJSON(vocePendenza.getDatiAllegati(),null));
				sv.setDescrizione(vocePendenza.getDescrizione());
				sv.setImporto(vocePendenza.getImporto());
				sv.setDescrizioneCausaleRPT(vocePendenza.getDescrizioneCausaleRPT());
				sv.setContabilita(contabilitaToStringDTO(vocePendenza.getContabilita()));
				sv.setCodDominio(vocePendenza.getIdDominio());

				// Definisce i dati di un bollo telematico
				if(vocePendenza.getHashDocumento() != null && vocePendenza.getTipoBollo() != null && vocePendenza.getProvinciaResidenza() != null) {
					it.govpay.core.dao.commons.Versamento.SingoloVersamento.BolloTelematico bollo = new it.govpay.core.dao.commons.Versamento.SingoloVersamento.BolloTelematico();
					bollo.setHash(vocePendenza.getHashDocumento());
					bollo.setProvincia(vocePendenza.getProvinciaResidenza());
					bollo.setTipo(vocePendenza.getTipoBollo());
					 
					String codiceTassonomicoPagoPA = vocePendenza.getCodiceTassonomicoPagoPA();
					String[] split = codiceTassonomicoPagoPA.split("/");
					bollo.setTipoContabilita(it.govpay.core.dao.commons.Versamento.SingoloVersamento.TipoContabilita.toEnum(split[0]));
					bollo.setCodContabilita(split[1]);
					
					sv.setBolloTelematico(bollo);
				} else if(vocePendenza.getCodEntrata() != null) { // Definisce i dettagli di incasso tramite riferimento in anagrafica GovPay.
					sv.setCodTributo(vocePendenza.getCodEntrata());

				} else { // Definisce i dettagli di incasso della singola entrata.
					it.govpay.core.dao.commons.Versamento.SingoloVersamento.Tributo tributo = new it.govpay.core.dao.commons.Versamento.SingoloVersamento.Tributo();
					
					String codiceTassonomicoPagoPA = vocePendenza.getCodiceTassonomicoPagoPA();
					String[] split = codiceTassonomicoPagoPA.split("/");
					tributo.setTipoContabilita(it.govpay.core.dao.commons.Versamento.SingoloVersamento.TipoContabilita.toEnum(split[0]));
					tributo.setCodContabilita(split[1]);
					tributo.setIbanAccredito(vocePendenza.getIbanAccredito());
					tributo.setIbanAppoggio(vocePendenza.getIbanAppoggio());
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
	
	public static it.govpay.core.beans.tracciati.ProprietaPendenza toProprietaPendenzaDTO(ProprietaPendenza proprieta) {
		it.govpay.core.beans.tracciati.ProprietaPendenza dto = null;
		if(proprieta != null) {
			dto = new it.govpay.core.beans.tracciati.ProprietaPendenza();
			
			if(proprieta.getDescrizioneImporto() != null && !proprieta.getDescrizioneImporto().isEmpty()) {
				List<it.govpay.core.beans.tracciati.VoceDescrizioneImporto> descrizioneImporto = new ArrayList<it.govpay.core.beans.tracciati.VoceDescrizioneImporto>();
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
		}
		
		return dto;
	}
	
	public static String contabilitaToStringDTO(Contabilita contabilita) throws ServiceException {
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
				allegatoDTO.setContenuto(allegato.getContenuto());
				allegatoDTO.setDescrizione(allegato.getDescrizione());
				
				allegatiDTO.add(allegatoDTO);
			}
		}
		
		return allegatiDTO;
	}
}
