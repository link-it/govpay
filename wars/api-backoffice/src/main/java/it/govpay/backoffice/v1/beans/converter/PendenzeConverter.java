package it.govpay.backoffice.v1.beans.converter;

import java.io.UnsupportedEncodingException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.commons.lang.ArrayUtils;
import org.openspcoop2.generic_project.exception.ServiceException;
import org.openspcoop2.utils.jaxrs.RawObject;
import org.openspcoop2.utils.json.ValidationException;
import org.openspcoop2.utils.serialization.IOException;

import it.govpay.backoffice.v1.beans.Avviso;
import it.govpay.backoffice.v1.beans.Avviso.StatoEnum;
import it.govpay.backoffice.v1.beans.Pendenza;
import it.govpay.backoffice.v1.beans.PendenzaIndex;
import it.govpay.backoffice.v1.beans.PendenzaPut;
import it.govpay.backoffice.v1.beans.Riscossione;
import it.govpay.backoffice.v1.beans.Rpp;
import it.govpay.backoffice.v1.beans.Segnalazione;
import it.govpay.backoffice.v1.beans.Soggetto;
import it.govpay.backoffice.v1.beans.StatoPendenza;
import it.govpay.backoffice.v1.beans.TassonomiaAvviso;
import it.govpay.backoffice.v1.beans.TipoContabilita;
import it.govpay.backoffice.v1.beans.VocePendenza;
import it.govpay.bd.model.Pagamento;
import it.govpay.bd.model.PagamentoPortale;
import it.govpay.bd.model.Rendicontazione;
import it.govpay.bd.model.Rpt;
import it.govpay.bd.model.SingoloVersamento;
import it.govpay.bd.model.Tributo;
import it.govpay.core.dao.pagamenti.dto.LeggiPendenzaDTOResponse;
import it.govpay.core.utils.GovpayConfig;
import it.govpay.core.utils.UriBuilderUtils;
import it.govpay.core.utils.rawutils.ConverterUtils;

public class PendenzeConverter {
	
	
	public static Pendenza toRsModelConInfoIncasso(LeggiPendenzaDTOResponse dto) throws ServiceException, IOException {
		return toRsModelConInfoIncasso(dto.getVersamentoIncasso(),
				dto.getUnitaOperativa(), dto.getApplicazione(), dto.getDominio(), dto.getLstSingoliVersamenti(), 
				dto.getPagamenti(), dto.getRpts());
	}
	
	
	public static Pendenza toRsModelConInfoIncasso(it.govpay.bd.viste.model.VersamentoIncasso versamento, it.govpay.bd.model.UnitaOperativa unitaOperativa, it.govpay.bd.model.Applicazione applicazione, 
			it.govpay.bd.model.Dominio dominio, List<SingoloVersamento> singoliVersamenti,List<PagamentoPortale> pagamenti, List<Rpt> rpts) throws ServiceException, IOException {
		Pendenza rsModel = toRsModel(versamento, unitaOperativa, applicazione, dominio, singoliVersamenti,pagamenti,rpts,true);
		
		StatoPendenza statoPendenza = null;

		switch(versamento.getStatoVersamento()) {
		case ANNULLATO: statoPendenza = StatoPendenza.ANNULLATA;
			break;
		case ESEGUITO: 
		case ESEGUITO_ALTRO_CANALE:  
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
		case NON_ESEGUITO: if(versamento.getDataScadenza() != null && versamento.getDataScadenza().before(new Date())) {statoPendenza = StatoPendenza.SCADUTA;} else { statoPendenza = StatoPendenza.NON_ESEGUITA;}
			break;
		case PARZIALMENTE_ESEGUITO:  statoPendenza = StatoPendenza.ESEGUITA_PARZIALE;
			break;
		default:
			break;
		
		}

		rsModel.setStato(statoPendenza);
		rsModel.setDataPagamento(versamento.getDataPagamento());
		rsModel.setImportoIncassato(versamento.getImportoIncassato());
		rsModel.setImportoPagato(versamento.getImportoPagato()); 
		rsModel.setIuvPagamento(versamento.getIuvPagamento());
		rsModel.setIuvAvviso(versamento.getIuvVersamento());
		return rsModel;
	}
	
	public static Pendenza toRsModel(LeggiPendenzaDTOResponse dto) throws ServiceException, IOException {
		return toRsModel(dto.getVersamentoIncasso(),
				dto.getUnitaOperativa(), dto.getApplicazione(), dto.getDominio(), dto.getLstSingoliVersamenti(), 
				dto.getPagamenti(), dto.getRpts());
	}
	
	public static Pendenza toRsModel(it.govpay.bd.model.Versamento versamento, it.govpay.bd.model.UnitaOperativa unitaOperativa, it.govpay.bd.model.Applicazione applicazione, 
			it.govpay.bd.model.Dominio dominio, List<SingoloVersamento> singoliVersamenti,List<PagamentoPortale> pagamenti, List<Rpt> rpts) throws ServiceException, IOException {
		return toRsModel(versamento, unitaOperativa, applicazione, dominio, singoliVersamenti, pagamenti,rpts, false);
	}
	
	public static Pendenza toRsModel(it.govpay.bd.model.Versamento versamento, it.govpay.bd.model.UnitaOperativa unitaOperativa, it.govpay.bd.model.Applicazione applicazione, 
			it.govpay.bd.model.Dominio dominio, List<SingoloVersamento> singoliVersamenti,List<PagamentoPortale> pagamenti, List<Rpt> rpts , boolean addInfoIncasso) throws ServiceException, IOException {
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
		rsModel.setDataUltimoAggiornamento(versamento.getDataUltimoAggiornamento());
		rsModel.setDominio(DominiConverter.toRsModelIndex(dominio));
		
		rsModel.setIdA2A(applicazione.getCodApplicazione());
		rsModel.setIdPendenza(versamento.getCodVersamentoEnte());
		rsModel.setImporto(versamento.getImportoTotale());
		rsModel.setNome(versamento.getNome());
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

		rsModel.setStato(statoPendenza);
		rsModel.setDescrizioneStato(versamento.getDescrizioneStato());
		rsModel.setTassonomia(versamento.getTassonomia());
		rsModel.setTassonomiaAvviso(TassonomiaAvviso.fromValue(versamento.getTassonomiaAvviso()));
		rsModel.setNumeroAvviso(versamento.getNumeroAvviso());

		rsModel.setSegnalazioni(unmarshall(versamento.getAnomalie()));
		
		if(unitaOperativa != null && !unitaOperativa.getCodUo().equals(it.govpay.model.Dominio.EC))
			rsModel.setUnitaOperativa(DominiConverter.toUnitaOperativaRsModel(unitaOperativa));

		List<VocePendenza> v = new ArrayList<>();
		for(SingoloVersamento s: singoliVersamenti) {
			v.add(toVocePendenzaRsModel(s,addInfoIncasso));
		}
		rsModel.setVoci(v);
		rsModel.setVerificato(versamento.isAck());
		rsModel.setAnomalo(versamento.isAnomalo());
		
		List<it.govpay.backoffice.v1.beans.Pagamento> listaPagamentoIndex = new ArrayList<>();
		
		if(pagamenti != null && pagamenti.size() > 0) {
			for (PagamentoPortale pagamento : pagamenti) {
				listaPagamentoIndex.add(PagamentiPortaleConverter.toRsModel(pagamento,null,null));
			}
		}
		
		rsModel.setPagamenti(listaPagamentoIndex);
		
		List<Rpp> rpps = new ArrayList<>();
		if(rpts != null && rpts.size() > 0) {
			for (Rpt rpt : rpts) {
				rpps.add(RptConverter.toRsModel(rpt));
			} 
		}
		rsModel.setRpp(rpps); 

		return rsModel;
	}
	
	private static List<Segnalazione> unmarshall(String anomalie) {
		List<Segnalazione> list = new ArrayList<>();
		
		if(anomalie == null || anomalie.isEmpty()) return list;
		
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

	public static PendenzaIndex toRsModelIndexConInfoIncasso(it.govpay.bd.viste.model.VersamentoIncasso versamento) throws ServiceException {
		PendenzaIndex pIndex = toRsModelIndex(versamento);
		
		StatoPendenza statoPendenza = null;

		switch(versamento.getStatoVersamento()) {
		case ANNULLATO: statoPendenza = StatoPendenza.ANNULLATA;
			break;
		case ESEGUITO: 
		case ESEGUITO_ALTRO_CANALE:  
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
		case NON_ESEGUITO: if(versamento.getDataScadenza() != null && versamento.getDataScadenza().before(new Date())) {statoPendenza = StatoPendenza.SCADUTA;} else { statoPendenza = StatoPendenza.NON_ESEGUITA;}
			break;
		case PARZIALMENTE_ESEGUITO:  statoPendenza = StatoPendenza.ESEGUITA_PARZIALE;
			break;
		default:
			break;
		
		}

		pIndex.setStato(statoPendenza);
		pIndex.setDataPagamento(versamento.getDataPagamento());
		pIndex.setImportoIncassato(versamento.getImportoIncassato());
		pIndex.setImportoPagato(versamento.getImportoPagato()); 
		pIndex.setIuvPagamento(versamento.getIuvPagamento());
		pIndex.setIuvAvviso(versamento.getIuvVersamento());
		pIndex.setIuvPagamento(versamento.getIuvPagamento());
		
		return pIndex;
	}

	public static PendenzaIndex toRsModelIndex(it.govpay.bd.model.Versamento versamento) throws ServiceException {
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
		rsModel.setDominio(DominiConverter.toRsModelIndex(versamento.getDominio(null)));
		
		rsModel.setIdA2A(versamento.getApplicazione(null).getCodApplicazione());
		rsModel.setIdPendenza(versamento.getCodVersamentoEnte());
		rsModel.setImporto(versamento.getImportoTotale());
		rsModel.setNome(versamento.getNome());
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

		rsModel.setStato(statoPendenza);
		rsModel.setTassonomia(versamento.getTassonomia());
		rsModel.setTassonomiaAvviso(TassonomiaAvviso.fromValue(versamento.getTassonomiaAvviso()));
		rsModel.setNumeroAvviso(versamento.getNumeroAvviso());
		
		if(versamento.getUo(null) != null && !versamento.getUo(null).getCodUo().equals(it.govpay.model.Dominio.EC))
			rsModel.setUnitaOperativa(DominiConverter.toUnitaOperativaRsModel(versamento.getUo(null)));
		
		rsModel.setPagamenti(UriBuilderUtils.getPagamentiByIdA2AIdPendenza(versamento.getApplicazione(null).getCodApplicazione(),versamento.getCodVersamentoEnte()));
		rsModel.setRpp(UriBuilderUtils.getRppsByIdA2AIdPendenza(versamento.getApplicazione(null).getCodApplicazione(),versamento.getCodVersamentoEnte()));
		rsModel.setVerificato(versamento.isAck());
		rsModel.setAnomalo(versamento.isAnomalo());
		
		return rsModel;
	}
	
	public static VocePendenza toVocePendenzaRsModel(it.govpay.bd.model.SingoloVersamento singoloVersamento, boolean addInfoIncasso) throws ServiceException {
		VocePendenza rsModel = new VocePendenza();
		
		if(singoloVersamento.getDatiAllegati() != null)
			rsModel.setDatiAllegati(new RawObject(singoloVersamento.getDatiAllegati()));
		rsModel.setDescrizione(singoloVersamento.getDescrizione());
		
		rsModel.setIdVocePendenza(singoloVersamento.getCodSingoloVersamentoEnte());
		rsModel.setImporto(singoloVersamento.getImportoSingoloVersamento());
		rsModel.setIndice(BigDecimal.valueOf(singoloVersamento.getIndiceDati().longValue())); 
		switch(singoloVersamento.getStatoSingoloVersamento()) {
		case ESEGUITO:rsModel.setStato(VocePendenza.StatoEnum.ESEGUITO);
			break;
		case NON_ESEGUITO:rsModel.setStato(VocePendenza.StatoEnum.NON_ESEGUITO);
			break;
		default:
			break;}
		
		
		// Definisce i dati di un bollo telematico
		if(singoloVersamento.getHashDocumento() != null && singoloVersamento.getTipoBollo() != null && singoloVersamento.getProvinciaResidenza() != null) {
			rsModel.setHashDocumento(singoloVersamento.getHashDocumento());
			rsModel.setTipoBollo(singoloVersamento.getTipoBollo().getCodifica());
			rsModel.setProvinciaResidenza(singoloVersamento.getProvinciaResidenza());
		} else if(singoloVersamento.getTributo(null) != null && singoloVersamento.getTributo(null).getCodTributo() != null) { // Definisce i dettagli di incasso tramite riferimento in anagrafica GovPay.
			rsModel.setCodEntrata(singoloVersamento.getTributo(null).getCodTributo());
		} else { // Definisce i dettagli di incasso della singola entrata.
			rsModel.setCodiceContabilita(singoloVersamento.getCodContabilita());
			rsModel.setIbanAccredito(singoloVersamento.getIbanAccredito(null).getCodIban());
			if(singoloVersamento.getTipoContabilita() != null)
				rsModel.setTipoContabilita(TipoContabilita.valueOf(singoloVersamento.getTipoContabilita().name()));
		}
		
		if(addInfoIncasso) {
			List<Rendicontazione> rendicontazioni = singoloVersamento.getRendicontazioni(null);

			
			if(rendicontazioni != null && !rendicontazioni.isEmpty()) {
				List<it.govpay.backoffice.v1.beans.Rendicontazione> rendicontazioniRsModel = new ArrayList<>();
				for (Rendicontazione rendicontazione : rendicontazioni) {
					it.govpay.backoffice.v1.beans.Rendicontazione rendicontazioneRsModel = FlussiRendicontazioneConverter.toRendicontazioneRsModel(rendicontazione);
					rendicontazioniRsModel.add(rendicontazioneRsModel);
				}
				rsModel.setRendicontazioni(rendicontazioniRsModel);
			}
			
			List<Pagamento> riscossioni = singoloVersamento.getPagamenti(null);
			
			if(riscossioni != null && !riscossioni.isEmpty()) {
				List<it.govpay.backoffice.v1.beans.Riscossione> riscossioniRsModel = new ArrayList<>();
				for (Pagamento pagamento : riscossioni) {
					Riscossione riscossioneRsModel = RiscossioniConverter.toRsModel(pagamento);
					riscossioniRsModel.add(riscossioneRsModel);
				}
				rsModel.setRiscossioni(riscossioniRsModel);
			}
		}
		
		
		return rsModel;
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
		rsModel.setDataValidita(versamento.getDataValidita());
		rsModel.setIdDominio(dominio.getCodDominio());
		rsModel.setImporto(versamento.getImportoTotale());
		rsModel.setNumeroAvviso(versamento.getNumeroAvviso());
		rsModel.setTassonomiaAvviso(versamento.getTassonomiaAvviso());
		rsModel.setBarcode(barCode);
		rsModel.setQrcode(qrCode);
		
		StatoEnum statoPendenza = null;

		switch(versamento.getStatoVersamento()) {
		case ANNULLATO: statoPendenza = StatoEnum.ANNULLATO;
			break;
		case ESEGUITO: statoPendenza = StatoEnum.PAGATO;
			break;
		case ESEGUITO_ALTRO_CANALE:  statoPendenza = StatoEnum.PAGATO;
			break;
		case NON_ESEGUITO: if(versamento.getDataScadenza() != null && versamento.getDataScadenza().before(new Date())) {statoPendenza = StatoEnum.SCADUTO;} else { statoPendenza = StatoEnum.NON_PAGATO;}
			break;
		case PARZIALMENTE_ESEGUITO:  statoPendenza = StatoEnum.PAGATO;
			break;
		default:
			break;
		
		}

		rsModel.setStato(statoPendenza);

		return rsModel;
	}
	
	public static it.govpay.core.dao.commons.Versamento getVersamentoFromPendenza(PendenzaPut pendenza, String ida2a, String idPendenza) throws ValidationException, ServiceException {
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
		//		versamento.setDataCaricamento(pendenza.getDataCaricamento() != null ? pendenza.getDataCaricamento() : new Date());
		versamento.setDataCaricamento(new Date());
		versamento.setDebitore(toAnagraficaCommons(pendenza.getSoggettoPagatore()));
		
		versamento.setTassonomia(pendenza.getTassonomia());
		if(pendenza.getDatiAllegati() != null)
			versamento.setDatiAllegati(ConverterUtils.toJSON(pendenza.getDatiAllegati(),null));

		if(pendenza.getTassonomiaAvviso() != null) {
			// valore tassonomia avviso non valido
			if(TassonomiaAvviso.fromValue(pendenza.getTassonomiaAvviso()) == null) {
				throw new ValidationException("Codifica inesistente per tassonomiaAvviso. Valore fornito [" + pendenza.getTassonomiaAvviso() + "] valori possibili " + ArrayUtils.toString(TassonomiaAvviso.values()));
			}

			versamento.setTassonomiaAvviso(pendenza.getTassonomiaAvviso());
		}

		versamento.setNumeroAvviso(pendenza.getNumeroAvviso());

		// voci pagamento
		BigDecimal importoVociPendenza = fillSingoliVersamentiFromVociPendenza(versamento, pendenza.getVoci());
		
		// importo pendenza puo' essere null
		versamento.setImportoTotale(pendenza.getImporto() != null ? pendenza.getImporto() : importoVociPendenza); 
		
		// tipo Pendenza
		if(versamento.getSingoloVersamento() != null && versamento.getSingoloVersamento().size() > 0) {
			it.govpay.core.dao.commons.Versamento.SingoloVersamento sv = versamento.getSingoloVersamento().get(0);
			if(sv.getBolloTelematico() != null) {
				versamento.setCodTipoVersamento(Tributo.BOLLOT);
			} else if(sv.getCodTributo() != null) {
				versamento.setCodTipoVersamento(sv.getCodTributo());
			} else {
				versamento.setCodTipoVersamento(GovpayConfig.getInstance().getCodTipoVersamentoPendenzeLibere());
			}
		}

		return versamento;
	}

	public static BigDecimal fillSingoliVersamentiFromVociPendenza(it.govpay.core.dao.commons.Versamento versamento, List<VocePendenza> voci) throws ServiceException {

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
					tributo.setTipoContabilita(it.govpay.core.dao.commons.Versamento.SingoloVersamento.Tributo.TipoContabilita.valueOf(vocePendenza.getTipoContabilita().name()));
					sv.setTributo(tributo);
				}

				versamento.getSingoloVersamento().add(sv);
			}
		}
		
		return importoTotale;
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
