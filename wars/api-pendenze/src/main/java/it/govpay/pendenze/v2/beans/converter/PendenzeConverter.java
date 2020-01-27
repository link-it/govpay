package it.govpay.pendenze.v2.beans.converter;

import java.io.UnsupportedEncodingException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.commons.lang.ArrayUtils;
import org.openspcoop2.generic_project.exception.ServiceException;
import org.openspcoop2.utils.jaxrs.RawObject;
import org.openspcoop2.utils.json.ValidationException;

import it.govpay.bd.model.Rpt;
import it.govpay.bd.model.SingoloVersamento;
import it.govpay.core.utils.UriBuilderUtils;
import it.govpay.core.utils.rawutils.ConverterUtils;
import it.govpay.pendenze.v2.beans.Pendenza;
import it.govpay.pendenze.v2.beans.PendenzaIndex;
import it.govpay.pendenze.v2.beans.NuovaPendenza;
import it.govpay.pendenze.v2.beans.NuovaVocePendenza;
import it.govpay.pendenze.v2.beans.RppIndex;
import it.govpay.pendenze.v2.beans.Segnalazione;
import it.govpay.pendenze.v2.beans.Soggetto;
import it.govpay.pendenze.v2.beans.StatoPendenza;
import it.govpay.pendenze.v2.beans.StatoVocePendenza;
import it.govpay.pendenze.v2.beans.TassonomiaAvviso;
import it.govpay.pendenze.v2.beans.TipoContabilita;
import it.govpay.pendenze.v2.beans.VocePendenza;
import it.govpay.pendenze.v2.beans.VocePendenza.TipoBolloEnum;

public class PendenzeConverter {

	public static Pendenza toRsModel(it.govpay.bd.model.Versamento versamento, List<Rpt> rpts) throws ServiceException {
		Pendenza rsModel = new Pendenza();

		if(versamento.getCodAnnoTributario()!= null)
			rsModel.setAnnoRiferimento(new BigDecimal(versamento.getCodAnnoTributario()));

		if(versamento.getCausaleVersamento()!= null)
			try {
				rsModel.setCausale(versamento.getCausaleVersamento().getSimple());
			} catch (UnsupportedEncodingException e) {
				throw new ServiceException(e);
			}

		rsModel.setDataCaricamento(versamento.getDataCreazione());
		rsModel.setDataScadenza(versamento.getDataScadenza());
		rsModel.setDataValidita(versamento.getDataValidita());
		rsModel.setDominio(DominiConverter.toRsModel(versamento.getDominio(null)));
		rsModel.setIdTipoPendenza(versamento.getTipoVersamentoDominio(null).getCodTipoVersamento());
		rsModel.setIdA2A(versamento.getApplicazione(null).getCodApplicazione());
		rsModel.setIdPendenza(versamento.getCodVersamentoEnte());
		rsModel.setImporto(versamento.getImportoTotale());
		rsModel.setNumeroAvviso(versamento.getNumeroAvviso());
		rsModel.setSoggettoPagatore(AnagraficaConverter.toSoggettoRsModel(versamento.getAnagraficaDebitore()));
		if(versamento.getDatiAllegati() != null)
			rsModel.setDatiAllegati(new RawObject(versamento.getDatiAllegati()));

		StatoPendenza statoPendenza = null;

		switch(versamento.getStatoVersamento()) {
		case ANNULLATO: statoPendenza = StatoPendenza.ANNULLATA;
		break;
		case ESEGUITO: statoPendenza = StatoPendenza.ESEGUITA;
		break;
		case ESEGUITO_ALTRO_CANALE:  statoPendenza = StatoPendenza.ESEGUITA;
		break;
		case NON_ESEGUITO: if(versamento.getDataScadenza() != null && versamento.getDataScadenza().before(new Date())) {statoPendenza = StatoPendenza.SCADUTA;} else { statoPendenza = StatoPendenza.NON_ESEGUITA;}
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

		if(versamento.getUo(null) != null && !versamento.getUo(null).getCodUo().equals(it.govpay.model.Dominio.EC))
			rsModel.setUnitaOperativa(UnitaOperativaConverter.toRsModel(versamento.getUo(null)));

		List<VocePendenza> v = new ArrayList<>();
		int indice = 1;
		for(SingoloVersamento s: versamento.getSingoliVersamenti(null)) {
			v.add(toVocePendenzaRsModel(s, indice++));
		}
		rsModel.setVoci(v);

		List<RppIndex> rpps = new ArrayList<>();
		if(rpts != null && rpts.size() > 0) {
			for (Rpt rpt : rpts) {
				rpps.add(RptConverter.toRsModelIndex(rpt, rpt.getVersamento(null), rpt.getVersamento(null).getApplicazione(null)));
			} 
		}
		rsModel.setRpp(rpps); 

		rsModel.setDescrizioneStato(versamento.getDescrizioneStato());
		rsModel.setSegnalazioni(unmarshall(versamento.getAnomalie()));
		
		rsModel.setDirezione(versamento.getDirezione());
		rsModel.setDivisione(versamento.getDivisione()); 
		rsModel.setTassonomia(versamento.getTassonomia());

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

	public static PendenzaIndex toRsIndexModel(it.govpay.bd.model.Versamento versamento) throws ServiceException {
		PendenzaIndex rsModel = new PendenzaIndex();

		if(versamento.getCodAnnoTributario()!= null)
			rsModel.setAnnoRiferimento(new BigDecimal(versamento.getCodAnnoTributario()));

		if(versamento.getCausaleVersamento()!= null)
			try {
				rsModel.setCausale(versamento.getCausaleVersamento().getSimple());
			} catch (UnsupportedEncodingException e) {
				throw new ServiceException(e);
			}

		rsModel.setDataCaricamento(versamento.getDataCreazione());
		rsModel.setDataScadenza(versamento.getDataScadenza());
		rsModel.setDataValidita(versamento.getDataValidita());
		rsModel.setDominio(DominiConverter.toRsModel(versamento.getDominio(null)));
		rsModel.setIdTipoPendenza(versamento.getTipoVersamentoDominio(null).getCodTipoVersamento());
		rsModel.setIdA2A(versamento.getApplicazione(null).getCodApplicazione());
		rsModel.setIdPendenza(versamento.getCodVersamentoEnte());
		rsModel.setImporto(versamento.getImportoTotale());
		rsModel.setNumeroAvviso(versamento.getNumeroAvviso());
		rsModel.setSoggettoPagatore(AnagraficaConverter.toSoggettoRsModel(versamento.getAnagraficaDebitore()));
		if(versamento.getDatiAllegati() != null)
			rsModel.setDatiAllegati(new RawObject(versamento.getDatiAllegati())); 

		StatoPendenza statoPendenza = null;

		switch(versamento.getStatoVersamento()) {
		case ANNULLATO: statoPendenza = StatoPendenza.ANNULLATA;
		break;
		case ESEGUITO: statoPendenza = StatoPendenza.ESEGUITA;
		break;
		case ESEGUITO_ALTRO_CANALE:  statoPendenza = StatoPendenza.ESEGUITA;
		break;
		case NON_ESEGUITO: if(versamento.getDataScadenza() != null && versamento.getDataScadenza().before(new Date())) {statoPendenza = StatoPendenza.SCADUTA;} else { statoPendenza = StatoPendenza.NON_ESEGUITA;}
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

		if(versamento.getUo(null) != null && !versamento.getUo(null).getCodUo().equals(it.govpay.model.Dominio.EC))
			rsModel.setUnitaOperativa(UnitaOperativaConverter.toRsModel(versamento.getUo(null)));

		rsModel.setRpp(UriBuilderUtils.getRppsByIdA2AIdPendenza(versamento.getApplicazione(null).getCodApplicazione(),versamento.getCodVersamentoEnte()));
		rsModel.setDescrizioneStato(versamento.getDescrizioneStato());
		rsModel.setDirezione(versamento.getDirezione());
		rsModel.setDivisione(versamento.getDivisione()); 
		rsModel.setTassonomia(versamento.getTassonomia());

		return rsModel;
	}

	public static VocePendenza toVocePendenzaRsModel(it.govpay.bd.model.SingoloVersamento singoloVersamento, int indice) throws ServiceException {
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
		} else if(singoloVersamento.getTributo(null) != null && singoloVersamento.getTributo(null).getCodTributo() != null) { // Definisce i dettagli di incasso tramite riferimento in anagrafica GovPay.
			rsModel.setCodEntrata(singoloVersamento.getTributo(null).getCodTributo());
		} else { // Definisce i dettagli di incasso della singola entrata.
			rsModel.setCodiceContabilita(singoloVersamento.getCodContabilita());
			rsModel.setIbanAccredito(singoloVersamento.getIbanAccredito(null).getCodIban());
			if(singoloVersamento.getTipoContabilita() != null)
				rsModel.setTipoContabilita(TipoContabilita.valueOf(singoloVersamento.getTipoContabilita().name()));
		}


		return rsModel;
	}

	public static it.govpay.core.dao.commons.Versamento getVersamentoFromPendenza(NuovaPendenza pendenza, String ida2a, String idPendenza) throws ValidationException, ServiceException {
		it.govpay.core.dao.commons.Versamento versamento = new it.govpay.core.dao.commons.Versamento();

		if(pendenza.getAnnoRiferimento() != null)
			versamento.setAnnoTributario(pendenza.getAnnoRiferimento().intValue());

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
			versamento.setDatiAllegati(ConverterUtils.toJSON(pendenza.getDatiAllegati(),null));

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

		return versamento;
	}

	public static void fillSingoliVersamentiFromVociPendenza(it.govpay.core.dao.commons.Versamento versamento, List<NuovaVocePendenza> voci) throws ServiceException {

		if(voci != null && voci.size() > 0) {
			for (NuovaVocePendenza vocePendenza : voci) {
				it.govpay.core.dao.commons.Versamento.SingoloVersamento sv = new it.govpay.core.dao.commons.Versamento.SingoloVersamento();

				//sv.setCodTributo(value); ??

				sv.setCodSingoloVersamentoEnte(vocePendenza.getIdVocePendenza());
				if(vocePendenza.getDatiAllegati() != null)
					sv.setDatiAllegati(ConverterUtils.toJSON(vocePendenza.getDatiAllegati(),null));
				sv.setDescrizione(vocePendenza.getDescrizione());
				sv.setDescrizioneCausaleRPT(vocePendenza.getDescrizioneCausaleRPT());
				sv.setImporto(vocePendenza.getImporto());

				// Definisce i dati di un bollo telematico
				if(vocePendenza.getHashDocumento() != null && vocePendenza.getTipoBollo() != null && vocePendenza.getProvinciaResidenza() != null) {
					it.govpay.core.dao.commons.Versamento.SingoloVersamento.BolloTelematico bollo = new it.govpay.core.dao.commons.Versamento.SingoloVersamento.BolloTelematico();
					bollo.setHash(vocePendenza.getHashDocumento());
					bollo.setProvincia(vocePendenza.getProvinciaResidenza());
					bollo.setTipo(vocePendenza.getTipoBollo().toString());
					sv.setBolloTelematico(bollo);
				} else if(vocePendenza.getCodEntrata() != null) { // Definisce i dettagli di incasso tramite riferimento in anagrafica GovPay.
					sv.setCodTributo(vocePendenza.getCodEntrata());

				} else { // Definisce i dettagli di incasso della singola entrata.
					it.govpay.core.dao.commons.Versamento.SingoloVersamento.Tributo tributo = new it.govpay.core.dao.commons.Versamento.SingoloVersamento.Tributo();
					tributo.setCodContabilita(vocePendenza.getCodiceContabilita());
					tributo.setIbanAccredito(vocePendenza.getIbanAccredito());
					tributo.setIbanAppoggio(vocePendenza.getIbanAppoggio());
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
