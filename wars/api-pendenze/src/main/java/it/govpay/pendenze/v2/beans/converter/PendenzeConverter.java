/*
 * GovPay - Porta di Accesso al Nodo dei Pagamenti SPC
 * http://www.gov4j.it/govpay
 *
 * Copyright (c) 2014-2024 Link.it srl (http://www.link.it).
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
package it.govpay.pendenze.v2.beans.converter;

import java.io.UnsupportedEncodingException;
import java.math.BigDecimal;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.commons.lang.ArrayUtils;
import org.apache.commons.lang.StringUtils;
import org.openspcoop2.generic_project.exception.ServiceException;
import org.openspcoop2.utils.jaxrs.RawObject;
import org.openspcoop2.utils.service.context.ContextThreadLocal;

import it.govpay.bd.BDConfigWrapper;
import it.govpay.bd.model.Allegato;
import it.govpay.bd.model.Rpt;
import it.govpay.bd.model.SingoloVersamento;
import it.govpay.bd.model.UnitaOperativa;
import it.govpay.core.beans.Costanti;
import it.govpay.core.beans.EsitoOperazione;
import it.govpay.core.exceptions.GovPayException;
import it.govpay.core.exceptions.IOException;
import it.govpay.core.exceptions.ValidationException;
import it.govpay.core.utils.DateUtils;
import it.govpay.core.utils.UriBuilderUtils;
import it.govpay.core.utils.rawutils.ConverterUtils;
import it.govpay.pendenze.v2.Allegati;
import it.govpay.pendenze.v2.beans.AllegatoPendenza;
import it.govpay.pendenze.v2.beans.Avviso;
import it.govpay.pendenze.v2.beans.Documento;
import it.govpay.pendenze.v2.beans.LinguaSecondaria;
import it.govpay.pendenze.v2.beans.NuovaPendenza;
import it.govpay.pendenze.v2.beans.NuovaVocePendenza;
import it.govpay.pendenze.v2.beans.NuovoAllegatoPendenza;
import it.govpay.pendenze.v2.beans.Pendenza;
import it.govpay.pendenze.v2.beans.PendenzaIndex;
import it.govpay.pendenze.v2.beans.ProprietaPendenza;
import it.govpay.pendenze.v2.beans.QuotaContabilita;
import it.govpay.pendenze.v2.beans.RppIndex;
import it.govpay.pendenze.v2.beans.Segnalazione;
import it.govpay.pendenze.v2.beans.Soggetto;
import it.govpay.pendenze.v2.beans.StatoAvviso;
import it.govpay.pendenze.v2.beans.StatoPendenza;
import it.govpay.pendenze.v2.beans.StatoVocePendenza;
import it.govpay.pendenze.v2.beans.TassonomiaAvviso;
import it.govpay.pendenze.v2.beans.TipoContabilita;
import it.govpay.pendenze.v2.beans.TipoSogliaVincoloPagamento;
import it.govpay.pendenze.v2.beans.VincoloPagamento;
import it.govpay.pendenze.v2.beans.VoceDescrizioneImporto;
import it.govpay.pendenze.v2.beans.VocePendenza;
import it.govpay.pendenze.v2.beans.VocePendenza.TipoBolloEnum;

public class PendenzeConverter {

	public static Pendenza toRsModel(it.govpay.bd.model.Versamento versamento, List<Rpt> rpts, List<Allegato> allegati) throws ServiceException, IOException, ValidationException {
		BDConfigWrapper configWrapper = new BDConfigWrapper(ContextThreadLocal.get().getTransactionId(), true);
		Pendenza rsModel = new Pendenza();

		if(versamento.getCodAnnoTributario()!= null)
			rsModel.setAnnoRiferimento(new BigDecimal(versamento.getCodAnnoTributario()));
		
		rsModel.setCartellaPagamento(versamento.getCodLotto());

		if(versamento.getCausaleVersamento()!= null)
			try {
				rsModel.setCausale(versamento.getCausaleVersamento().getSimple());
			} catch (UnsupportedEncodingException e) {
				throw new ServiceException(e);
			}

		rsModel.setDataCaricamento(versamento.getDataCreazione());
		rsModel.setDataPagamento(versamento.getDataPagamento());
		rsModel.setDataScadenza(versamento.getDataScadenza());
		rsModel.setDataValidita(versamento.getDataValidita());
		rsModel.setDominio(DominiConverter.toRsModel(versamento.getDominio(configWrapper)));
		rsModel.setIdTipoPendenza(versamento.getTipoVersamentoDominio(configWrapper).getCodTipoVersamento());
		rsModel.setIdA2A(versamento.getApplicazione(configWrapper).getCodApplicazione());
		rsModel.setIdPendenza(versamento.getCodVersamentoEnte());
		rsModel.setImporto(versamento.getImportoTotale());
		rsModel.setNumeroAvviso(versamento.getNumeroAvviso());
		rsModel.setIuvPagamento(versamento.getIuvPagamento());
		rsModel.setIuvAvviso(versamento.getIuvVersamento());
		rsModel.setSoggettoPagatore(AnagraficaConverter.toSoggettoRsModel(versamento.getAnagraficaDebitore()));
		if(versamento.getDatiAllegati() != null)
			rsModel.setDatiAllegati(new RawObject(versamento.getDatiAllegati()));

		StatoPendenza statoPendenza = null;

		switch(versamento.getStatoVersamento()) {
		case ANNULLATO: statoPendenza = StatoPendenza.ANNULLATA;
		break;
		case ESEGUITO_SENZA_RPT:
		case ESEGUITO: statoPendenza = StatoPendenza.ESEGUITA;
			statoPendenza = StatoPendenza.ESEGUITA;
			if(versamento.getStatoPagamento() != null) {
				switch (versamento.getStatoPagamento()) {
				case INCASSATO:
					statoPendenza = StatoPendenza.INCASSATA;
					break;
				case NON_PAGATO:
				case PAGATO:
				default:
					break;
				}
			}
		break;
		case ESEGUITO_ALTRO_CANALE:  statoPendenza = StatoPendenza.ESEGUITA;
		break;
		case NON_ESEGUITO: if(versamento.getDataScadenza() != null && DateUtils.isDataDecorsa(versamento.getDataScadenza(), DateUtils.CONTROLLO_SCADENZA)) {statoPendenza = StatoPendenza.SCADUTA;} else { statoPendenza = StatoPendenza.NON_ESEGUITA;}
		break;
		case PARZIALMENTE_ESEGUITO:  statoPendenza = StatoPendenza.ESEGUITA_PARZIALE;
		break;
		default:
			break;
		}
		
		if(versamento.isAnomalo())
			statoPendenza = StatoPendenza.ANOMALA;

		rsModel.setStato(statoPendenza);
		if(versamento.getTassonomiaAvviso() != null)
			rsModel.setTassonomiaAvviso(TassonomiaAvviso.fromValue(versamento.getTassonomiaAvviso()));
		rsModel.setNumeroAvviso(versamento.getNumeroAvviso());

		UnitaOperativa uo = versamento.getUo(configWrapper);
		if(uo != null && !uo.getCodUo().equals(it.govpay.model.Dominio.EC))
			rsModel.setUnitaOperativa(UnitaOperativaConverter.toRsModel(uo));

		List<VocePendenza> v = new ArrayList<>();
		int indice = 1;
		for(SingoloVersamento s: versamento.getSingoliVersamenti()) {
			v.add(toVocePendenzaRsModel(s, indice++, configWrapper));
		}
		rsModel.setVoci(v);

		List<RppIndex> rpps = new ArrayList<>();
		if(rpts != null && !rpts.isEmpty()) {
			for (Rpt rpt : rpts) {
				rpps.add(RptConverter.toRsModelIndex(rpt, rpt.getVersamento(), rpt.getVersamento().getApplicazione(configWrapper)));
			} 
		}
		rsModel.setRpp(rpps); 

		rsModel.setDescrizioneStato(versamento.getDescrizioneStato());
		rsModel.setSegnalazioni(unmarshall(versamento.getAnomalie()));
		
		rsModel.setDirezione(versamento.getDirezione());
		rsModel.setDivisione(versamento.getDivisione()); 
		rsModel.setTassonomia(versamento.getTassonomia());
		
		if(versamento.getDocumento(configWrapper) != null) {
			rsModel.setDocumento(toDocumentoRsModel(versamento, versamento.getDocumento(configWrapper)));
		}
		
		if(versamento.getTipo() != null) {
			switch (versamento.getTipo()) {
			case DOVUTO:
				rsModel.setTipo(it.govpay.pendenze.v2.beans.TipoPendenzaTipologia.DOVUTO);
				break;
			case SPONTANEO:
				rsModel.setTipo(it.govpay.pendenze.v2.beans.TipoPendenzaTipologia.SPONTANEO);
				break;
			}
		}
		
		rsModel.setUUID(versamento.getIdSessione());
		rsModel.setProprieta(toProprietaPendenzaRsModel(versamento.getProprietaPendenza()));
		
		rsModel.setAllegati(toAllegatiRsModel(allegati));

		return rsModel;
	}

	private static List<Segnalazione> unmarshall(String anomalie) {
		List<Segnalazione> list = new ArrayList<>();

		if(anomalie == null || anomalie.isEmpty()) return null;

		String[] split = anomalie.split("\\|");
		for(String s : split){
			String[] split2 = s.split("#");
			Segnalazione a = new Segnalazione();
			a.setCodice(split2[0]);;
			a.setDescrizione(split2[1]);
			list.add(a);
		}
		return list;
	}
	
	public static Documento toDocumentoRsModel(it.govpay.bd.model.Versamento versamento, it.govpay.bd.model.Documento documento ) throws ServiceException {
		Documento rsModel = new Documento();
		
		rsModel.setDescrizione(documento.getDescrizione());
		rsModel.setIdentificativo(documento.getCodDocumento());
		if(versamento.getNumeroRata() != null)
			rsModel.setRata(new BigDecimal(versamento.getNumeroRata()));
		if(versamento.getTipoSoglia() != null) {
			VincoloPagamento soglia = new VincoloPagamento();
			if(versamento.getGiorniSoglia() != null)
				soglia.setGiorni(new BigDecimal(versamento.getGiorniSoglia()));
			
			switch(versamento.getTipoSoglia()) {
			case ENTRO:
				soglia.setTipo(TipoSogliaVincoloPagamento.ENTRO.toString());
				break;
			case OLTRE:
				soglia.setTipo(TipoSogliaVincoloPagamento.OLTRE.toString());
				break;
			case RIDOTTO:
				soglia.setTipo(TipoSogliaVincoloPagamento.RIDOTTO.toString());
				break;
			case SCONTATO:
				soglia.setTipo(TipoSogliaVincoloPagamento.SCONTATO.toString());
				break;
			}
			
			rsModel.setSoglia(soglia );
		}
		
		return rsModel;
	}

	public static PendenzaIndex toRsIndexModel(it.govpay.bd.model.Versamento versamento) throws ServiceException {
		BDConfigWrapper configWrapper = new BDConfigWrapper(ContextThreadLocal.get().getTransactionId(), true);
		PendenzaIndex rsModel = new PendenzaIndex();

		if(versamento.getCodAnnoTributario()!= null)
			rsModel.setAnnoRiferimento(new BigDecimal(versamento.getCodAnnoTributario()));
		
		rsModel.setCartellaPagamento(versamento.getCodLotto());

		if(versamento.getCausaleVersamento()!= null)
			try {
				rsModel.setCausale(versamento.getCausaleVersamento().getSimple());
			} catch (UnsupportedEncodingException e) {
				throw new ServiceException(e);
			}

		rsModel.setDataCaricamento(versamento.getDataCreazione());
		rsModel.setDataPagamento(versamento.getDataPagamento());
		rsModel.setDataScadenza(versamento.getDataScadenza());
		rsModel.setDataValidita(versamento.getDataValidita());
		rsModel.setDominio(DominiConverter.toRsModel(versamento.getDominio(configWrapper)));
		rsModel.setIdTipoPendenza(versamento.getTipoVersamentoDominio(configWrapper).getCodTipoVersamento());
		rsModel.setIdA2A(versamento.getApplicazione(configWrapper).getCodApplicazione());
		rsModel.setIdPendenza(versamento.getCodVersamentoEnte());
		rsModel.setImporto(versamento.getImportoTotale());
		rsModel.setNumeroAvviso(versamento.getNumeroAvviso());
		rsModel.setIuvPagamento(versamento.getIuvPagamento());
		rsModel.setIuvAvviso(versamento.getIuvVersamento());
		rsModel.setSoggettoPagatore(AnagraficaConverter.toSoggettoRsModel(versamento.getAnagraficaDebitore()));
		if(versamento.getDatiAllegati() != null)
			rsModel.setDatiAllegati(new RawObject(versamento.getDatiAllegati())); 

		StatoPendenza statoPendenza = null;

		switch(versamento.getStatoVersamento()) {
		case ANNULLATO: statoPendenza = StatoPendenza.ANNULLATA;
			break;
		case ESEGUITO_SENZA_RPT:
		case ESEGUITO: statoPendenza = StatoPendenza.ESEGUITA;
			if(versamento.getStatoPagamento() != null) {
				switch (versamento.getStatoPagamento()) {
				case INCASSATO:
					statoPendenza = StatoPendenza.INCASSATA;
					break;
				case NON_PAGATO:
				case PAGATO:
				default:
					break;
				}
			}
			break;
		case ESEGUITO_ALTRO_CANALE:  statoPendenza = StatoPendenza.ESEGUITA;
			break;
		case NON_ESEGUITO: if(versamento.getDataScadenza() != null && DateUtils.isDataDecorsa(versamento.getDataScadenza(), DateUtils.CONTROLLO_SCADENZA)) {statoPendenza = StatoPendenza.SCADUTA;} else { statoPendenza = StatoPendenza.NON_ESEGUITA;}
			break;
		case PARZIALMENTE_ESEGUITO:  statoPendenza = StatoPendenza.ESEGUITA_PARZIALE;
			break;
		default:
			break;
		}
		
		if(versamento.isAnomalo())
			statoPendenza = StatoPendenza.ANOMALA;

		rsModel.setStato(statoPendenza);
		rsModel.setTassonomiaAvviso(TassonomiaAvviso.fromValue(versamento.getTassonomiaAvviso()));
		rsModel.setNumeroAvviso(versamento.getNumeroAvviso());

		UnitaOperativa uo = versamento.getUo(configWrapper);
		if(uo != null && !uo.getCodUo().equals(it.govpay.model.Dominio.EC))
			rsModel.setUnitaOperativa(UnitaOperativaConverter.toRsModel(uo));

		rsModel.setRpp(UriBuilderUtils.getRppsByIdA2AIdPendenza(versamento.getApplicazione(configWrapper).getCodApplicazione(),versamento.getCodVersamentoEnte()));
		rsModel.setDescrizioneStato(versamento.getDescrizioneStato());
		rsModel.setDirezione(versamento.getDirezione());
		rsModel.setDivisione(versamento.getDivisione()); 
		rsModel.setTassonomia(versamento.getTassonomia());

		if(versamento.getDocumento(configWrapper) != null) {
			rsModel.setDocumento(toDocumentoRsModel(versamento, versamento.getDocumento(configWrapper)));
		}
		
		if(versamento.getTipo() != null) {
			switch (versamento.getTipo()) {
			case DOVUTO:
				rsModel.setTipo(it.govpay.pendenze.v2.beans.TipoPendenzaTipologia.DOVUTO);
				break;
			case SPONTANEO:
				rsModel.setTipo(it.govpay.pendenze.v2.beans.TipoPendenzaTipologia.SPONTANEO);
				break;
			}
		}
		
		rsModel.setUUID(versamento.getIdSessione());
		rsModel.setProprieta(toProprietaPendenzaRsModel(versamento.getProprietaPendenza()));
		
		return rsModel;
	}

	public static VocePendenza toVocePendenzaRsModel(it.govpay.bd.model.SingoloVersamento singoloVersamento, int indice, BDConfigWrapper configWrapper) throws ServiceException, IOException, ValidationException {
		VocePendenza rsModel = new VocePendenza();

		if(singoloVersamento.getDatiAllegati() != null)
			rsModel.setDatiAllegati(new RawObject(singoloVersamento.getDatiAllegati()));
		rsModel.setDescrizione(singoloVersamento.getDescrizione());
		rsModel.setDescrizioneCausaleRPT(singoloVersamento.getDescrizioneCausaleRPT());

		rsModel.setIdVocePendenza(singoloVersamento.getCodSingoloVersamentoEnte());
		rsModel.setImporto(singoloVersamento.getImportoSingoloVersamento());
		rsModel.setIndice(new BigDecimal(indice));


		switch(singoloVersamento.getStatoSingoloVersamento()) {
		case ESEGUITO:rsModel.setStato(StatoVocePendenza.ESEGUITO);
		break;
		case NON_ESEGUITO:rsModel.setStato(StatoVocePendenza.NON_ESEGUITO);
		break;
		default:
			break;}

		// Definisce i dati di un bollo telematico
		if(singoloVersamento.getHashDocumento() != null && singoloVersamento.getTipoBollo() != null && singoloVersamento.getProvinciaResidenza() != null) {
			rsModel.setHashDocumento(singoloVersamento.getHashDocumento());
			switch(singoloVersamento.getTipoBollo()) {
			case IMPOSTA_BOLLO:
				rsModel.setTipoBollo(TipoBolloEnum._01);
				break;
			}
			rsModel.setProvinciaResidenza(singoloVersamento.getProvinciaResidenza());
		} else if(singoloVersamento.getTributo(configWrapper) != null && singoloVersamento.getTributo(configWrapper).getCodTributo() != null) { // Definisce i dettagli di incasso tramite riferimento in anagrafica GovPay.
			rsModel.setCodEntrata(singoloVersamento.getTributo(configWrapper).getCodTributo());
		} else { // Definisce i dettagli di incasso della singola entrata.
			rsModel.setCodiceContabilita(singoloVersamento.getCodContabilita());
			rsModel.setIbanAccredito(singoloVersamento.getIbanAccredito(configWrapper).getCodIban());
			if(singoloVersamento.getTipoContabilita() != null)
				rsModel.setTipoContabilita(TipoContabilita.valueOf(singoloVersamento.getTipoContabilita().name()));
		}
		
		rsModel.setContabilita(ContabilitaConverter.toRsModel(singoloVersamento.getContabilita()));
		if(singoloVersamento.getDominio(configWrapper) != null) {
			rsModel.setDominio(DominiConverter.toRsModel(singoloVersamento.getDominio(configWrapper)));
		}

		return rsModel;
	}

	public static it.govpay.core.beans.commons.Versamento getVersamentoFromPendenza(NuovaPendenza pendenza, String ida2a, String idPendenza) throws ValidationException, ServiceException, GovPayException, IOException {
		it.govpay.core.beans.commons.Versamento versamento = new it.govpay.core.beans.commons.Versamento();

		if(pendenza.getAnnoRiferimento() != null)
			versamento.setAnnoTributario(pendenza.getAnnoRiferimento().intValue());

		versamento.setCodLotto(pendenza.getCartellaPagamento());
		versamento.setCausale(pendenza.getCausale());
		versamento.setCodApplicazione(ida2a);

		versamento.setCodDominio(pendenza.getIdDominio());
		versamento.setCodUnitaOperativa(pendenza.getIdUnitaOperativa());
		versamento.setCodVersamentoEnte(idPendenza);
		versamento.setDataScadenza(pendenza.getDataScadenza());
		versamento.setDataValidita(pendenza.getDataValidita());
		versamento.setDataCaricamento(new Date());
		versamento.setDebitore(toAnagraficaCommons(pendenza.getSoggettoPagatore()));
		versamento.setImportoTotale(pendenza.getImporto());
		if(pendenza.getDatiAllegati() != null)
			versamento.setDatiAllegati(ConverterUtils.toJSON(pendenza.getDatiAllegati()));

		if(pendenza.getTassonomiaAvviso() != null) {
			// valore tassonomia avviso non valido
			if(TassonomiaAvviso.fromValue(pendenza.getTassonomiaAvviso()) == null) {
				throw new ValidationException("Codifica inesistente per tassonomiaAvviso. Valore fornito [" + pendenza.getTassonomiaAvviso() + "] valori possibili " + ArrayUtils.toString(TassonomiaAvviso.values()));
			}

			versamento.setTassonomiaAvviso(pendenza.getTassonomiaAvviso());
		}

		versamento.setTassonomia(pendenza.getTassonomia());
		versamento.setNumeroAvviso(pendenza.getNumeroAvviso()); 

		// voci pagamento
		fillSingoliVersamentiFromVociPendenza(versamento, pendenza.getVoci());

		// tipo Pendenza
		versamento.setCodTipoVersamento(pendenza.getIdTipoPendenza());
		versamento.setDirezione(pendenza.getDirezione());
		versamento.setDivisione(pendenza.getDivisione()); 
		
		if(pendenza.getDocumento() != null) {
			it.govpay.core.beans.commons.Versamento.Documento documento = new it.govpay.core.beans.commons.Versamento.Documento();
			
			documento.setCodDocumento(pendenza.getDocumento().getIdentificativo());
			if(pendenza.getDocumento().getRata() != null)
				documento.setCodRata(pendenza.getDocumento().getRata().intValue());
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
		
		versamento.setDataNotificaAvviso(pendenza.getDataNotificaAvviso());
		versamento.setDataPromemoriaScadenza(pendenza.getDataPromemoriaScadenza());
		
		versamento.setProprieta(PendenzeConverter.toProprietaPendenzaDTO(pendenza.getProprieta()));
		
		versamento.setAllegati(toAllegatiPendenzaDTO(pendenza.getAllegati()));
		
		return versamento;
	}

	public static void fillSingoliVersamentiFromVociPendenza(it.govpay.core.beans.commons.Versamento versamento, List<NuovaVocePendenza> voci) throws ServiceException, GovPayException, IOException {

		if(voci != null && !voci.isEmpty()) {
			for (NuovaVocePendenza vocePendenza : voci) {
				it.govpay.core.beans.commons.Versamento.SingoloVersamento sv = new it.govpay.core.beans.commons.Versamento.SingoloVersamento();

				//sv.setCodTributo(value); ??

				sv.setCodSingoloVersamentoEnte(vocePendenza.getIdVocePendenza());
				if(vocePendenza.getDatiAllegati() != null)
					sv.setDatiAllegati(ConverterUtils.toJSON(vocePendenza.getDatiAllegati()));
				sv.setDescrizione(vocePendenza.getDescrizione());
				sv.setDescrizioneCausaleRPT(vocePendenza.getDescrizioneCausaleRPT());
				sv.setImporto(vocePendenza.getImporto());
				sv.setCodDominio(vocePendenza.getIdDominio());

				// Definisce i dati di un bollo telematico
				if(vocePendenza.getHashDocumento() != null && vocePendenza.getTipoBollo() != null && vocePendenza.getProvinciaResidenza() != null) {
					it.govpay.core.beans.commons.Versamento.SingoloVersamento.BolloTelematico bollo = new it.govpay.core.beans.commons.Versamento.SingoloVersamento.BolloTelematico();
					bollo.setHash(vocePendenza.getHashDocumento());
					bollo.setProvincia(vocePendenza.getProvinciaResidenza());
					bollo.setTipo(vocePendenza.getTipoBollo().toString());
					sv.setBolloTelematico(bollo);
				} else if(vocePendenza.getCodEntrata() != null) { // Definisce i dettagli di incasso tramite riferimento in anagrafica GovPay.
					sv.setCodTributo(vocePendenza.getCodEntrata());

				} else { // Definisce i dettagli di incasso della singola entrata.
					it.govpay.core.beans.commons.Versamento.SingoloVersamento.Tributo tributo = new it.govpay.core.beans.commons.Versamento.SingoloVersamento.Tributo();
					tributo.setCodContabilita(vocePendenza.getCodiceContabilita());
					tributo.setIbanAccredito(vocePendenza.getIbanAccredito());
					tributo.setIbanAppoggio(vocePendenza.getIbanAppoggio());
					tributo.setTipoContabilita(it.govpay.core.beans.commons.Versamento.SingoloVersamento.TipoContabilita.valueOf(vocePendenza.getTipoContabilita().name()));
					sv.setTributo(tributo);
				}
				
				sv.setContabilita(ContabilitaConverter.toStringDTO(vocePendenza.getContabilita()));
				
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
	
	public static Avviso toAvvisoRsModel(it.govpay.bd.model.Versamento versamento, it.govpay.bd.model.Dominio dominio, String barCode, String qrCode) throws ServiceException {
		Avviso rsModel = new Avviso();
		
		if(versamento.getCausaleVersamento()!= null)
			try {
				rsModel.setDescrizione(versamento.getCausaleVersamento().getSimple());
			} catch (UnsupportedEncodingException e) {
				throw new ServiceException(e);
			}
		
		rsModel.setDataScadenza(versamento.getDataScadenza());
		rsModel.setDataPagamento(versamento.getDataPagamento());
		rsModel.setDataValidita(versamento.getDataValidita());
		rsModel.setIdDominio(dominio.getCodDominio());
		rsModel.setImporto(versamento.getImportoTotale());
		rsModel.setNumeroAvviso(versamento.getNumeroAvviso());
		rsModel.setTassonomiaAvviso(TassonomiaAvviso.fromValue(versamento.getTassonomiaAvviso()));
		rsModel.setBarcode(barCode);
		rsModel.setQrcode(qrCode);
		
		StatoAvviso statoPendenza = null;

		switch(versamento.getStatoVersamento()) {
		case ANNULLATO: statoPendenza = StatoAvviso.ANNULLATA;
			break;
		case ESEGUITO: statoPendenza = StatoAvviso.DUPLICATA;
			break;
		case ESEGUITO_ALTRO_CANALE:  statoPendenza = StatoAvviso.DUPLICATA;
			break;
		case NON_ESEGUITO: if(versamento.getDataScadenza() != null && DateUtils.isDataDecorsa(versamento.getDataScadenza(), DateUtils.CONTROLLO_SCADENZA)) {statoPendenza = StatoAvviso.SCADUTA;} else { statoPendenza = StatoAvviso.NON_ESEGUITA;}
			break;
		case PARZIALMENTE_ESEGUITO:  statoPendenza = StatoAvviso.DUPLICATA;
			break;
		default:
			break;
		
		}

		rsModel.setStato(statoPendenza);

		return rsModel;
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
				switch(LinguaSecondaria.fromValue(proprieta.getLinguaSecondaria())) {
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
			dto.setDataScandenzaAvviso(proprieta.getDataScandenzaAvviso());
		}
		
		return dto;
	}
	
	public static ProprietaPendenza toProprietaPendenzaRsModel(it.govpay.core.beans.tracciati.ProprietaPendenza proprieta) {
		ProprietaPendenza rsModel = null;
		if(proprieta != null) {
			rsModel = new ProprietaPendenza();
			
			if(proprieta.getDescrizioneImporto() != null && !proprieta.getDescrizioneImporto().isEmpty()) {
				List<VoceDescrizioneImporto> descrizioneImporto = new ArrayList<>();
				for (it.govpay.core.beans.tracciati.VoceDescrizioneImporto vdI : proprieta.getDescrizioneImporto()) {
					VoceDescrizioneImporto voce = new VoceDescrizioneImporto();
					
					voce.setVoce(vdI.getVoce());
					voce.setImporto(vdI.getImporto());
					
					descrizioneImporto.add(voce);
				}
				rsModel.setDescrizioneImporto(descrizioneImporto);
			}
			rsModel.setLineaTestoRicevuta1(proprieta.getLineaTestoRicevuta1());
			rsModel.setLineaTestoRicevuta2(proprieta.getLineaTestoRicevuta2());
			if(proprieta.getLinguaSecondaria() != null) {
				switch(proprieta.getLinguaSecondaria()) {
				case DE:
					rsModel.setLinguaSecondariaEnum(LinguaSecondaria.DE);
					break;
				case EN:
					rsModel.setLinguaSecondariaEnum(LinguaSecondaria.EN);
					break;
				case FALSE:
					rsModel.setLinguaSecondariaEnum(LinguaSecondaria.FALSE);
					break;
				case FR:
					rsModel.setLinguaSecondariaEnum(LinguaSecondaria.FR);
					break;
				case SL:
					rsModel.setLinguaSecondariaEnum(LinguaSecondaria.SL);
					break;
				}	
				
				if(rsModel.getLinguaSecondariaEnum() != null)
					rsModel.setLinguaSecondaria(rsModel.getLinguaSecondariaEnum().toString());
			}
			rsModel.setLinguaSecondariaCausale(proprieta.getLinguaSecondariaCausale());
			rsModel.setInformativaImportoAvviso(proprieta.getInformativaImportoAvviso());
			rsModel.setLinguaSecondariaInformativaImportoAvviso(proprieta.getLinguaSecondariaInformativaImportoAvviso());
			rsModel.setDataScandenzaAvviso(proprieta.getDataScandenzaAvviso());
		}
		
		return rsModel;
	}
	
	private static List<AllegatoPendenza> toAllegatiRsModel(List<Allegato> allegati) { 
		List<AllegatoPendenza> rsModel = null;
		
		if(allegati != null && !allegati.isEmpty()) {
			rsModel = new ArrayList<>();
			
			for (Allegato allegato : allegati) {
				AllegatoPendenza allegatoRsModel = new AllegatoPendenza();
				
				allegatoRsModel.setNome(allegato.getNome());
				allegatoRsModel.setTipo(allegato.getTipo());
				allegatoRsModel.setDescrizione(allegato.getDescrizione());
				allegatoRsModel.setContenuto(MessageFormat.format(Allegati.DETTAGLIO_PATH_PATTERN, allegato.getId()));
				
				rsModel.add(allegatoRsModel);
			}
		}
		
		return rsModel;
	}
	
	private static List<it.govpay.core.beans.commons.Versamento.AllegatoPendenza> toAllegatiPendenzaDTO(List<NuovoAllegatoPendenza> allegati) {
		List<it.govpay.core.beans.commons.Versamento.AllegatoPendenza> allegatiDTO = null;
		
		if(allegati != null && !allegati.isEmpty()) {
			allegatiDTO = new ArrayList<>();
			
			for (NuovoAllegatoPendenza allegato : allegati) {
				it.govpay.core.beans.commons.Versamento.AllegatoPendenza allegatoDTO = new it.govpay.core.beans.commons.Versamento.AllegatoPendenza();
				
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
