package it.govpay.core.legacy.utils;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.openspcoop2.generic_project.exception.NotFoundException;
import org.openspcoop2.generic_project.exception.ServiceException;
import org.openspcoop2.utils.json.ValidationException;
import org.openspcoop2.utils.serialization.IOException;

import it.govpay.bd.BDConfigWrapper;
import it.govpay.bd.anagrafica.AnagraficaManager;
import it.govpay.bd.model.Applicazione;
import it.govpay.bd.model.Dominio;
import it.govpay.bd.model.Fr;
import it.govpay.bd.model.Pagamento;
import it.govpay.bd.model.Rendicontazione;
import it.govpay.bd.model.Rpt;
import it.govpay.bd.pagamento.filters.VersamentoFilter.SortFields;
import it.govpay.core.exceptions.GovPayException;
import it.govpay.core.utils.IuvUtils;
import it.govpay.ec.v1.beans.PendenzaVerificata;
import it.govpay.ec.v1.beans.Soggetto;
import it.govpay.ec.v1.beans.StatoPendenzaVerificata;
import it.govpay.ec.v1.beans.TipoContabilita;
import it.govpay.ec.v1.beans.VocePendenza;
import it.govpay.servizi.commons.Anagrafica;
import it.govpay.servizi.commons.Canale;
import it.govpay.servizi.commons.EsitoTransazione;
import it.govpay.servizi.commons.EsitoVerificaVersamento;
import it.govpay.servizi.commons.FlussoRendicontazione;
import it.govpay.servizi.commons.IuvGenerato;
import it.govpay.servizi.commons.ModelloPagamento;
import it.govpay.servizi.commons.Pagamento.Allegato;
import it.govpay.servizi.commons.StatoTransazione;
import it.govpay.servizi.commons.StatoVersamento;
import it.govpay.servizi.commons.TipoAllegato;
import it.govpay.servizi.commons.TipoRendicontazione;
import it.govpay.servizi.commons.TipoVersamento;
import it.govpay.servizi.commons.Transazione;
import it.govpay.servizi.commons.Versamento;
import it.govpay.servizi.commons.Versamento.SingoloVersamento;
import it.govpay.servizi.pa.PaVerificaVersamentoResponse;

public class Gp21Utils {

	public static Transazione toTransazione(Rpt rpt, BDConfigWrapper configWrapper) throws ServiceException {
		Transazione t = new Transazione();
		Canale canale = new Canale();
		canale.setCodCanale(rpt.getCodCanale());
		canale.setCodPsp(rpt.getCodPsp());
		canale.setCodIntermediarioPsp(rpt.getCodIntermediarioPsp());
		if(rpt.getTipoVersamento() != null)
			canale.setTipoVersamento(TipoVersamento.valueOf(rpt.getTipoVersamento().getCodifica()));
		t.setCanale(canale);
		t.setCcp(rpt.getCcp());
		t.setCodDominio(rpt.getCodDominio());
		t.setDescrizioneStato(rpt.getDescrizioneStato());
		if(rpt.getEsitoPagamento() != null) {
			t.setEsito(EsitoTransazione.valueOf(rpt.getEsitoPagamento().toString()));
		}
		t.setIuv(rpt.getIuv());
		if(rpt.getModelloPagamento() != null)
			t.setModello(ModelloPagamento.valueOf(rpt.getModelloPagamento().toString()));
		t.setRpt(rpt.getXmlRpt());
		t.setRt(rpt.getXmlRt());

//		if(versione.compareTo(Versione.GP_02_02_00) >=0) { // Versione 2.2
			t.setData(rpt.getDataMsgRichiesta());
//		}

		try {
			t.setStato(StatoTransazione.valueOf(rpt.getStato().toString()));
		} catch (Exception e) {
			t.setStato(StatoTransazione.RPT_ACCETTATA_NODO);
		}
		List<Pagamento> pagamenti = configWrapper != null ? rpt.getPagamenti(configWrapper) : rpt.getPagamenti();
		for(Pagamento pagamento : pagamenti) {
			t.getPagamento().add(toPagamento(pagamento));
		}
		return t;
	}

	public static it.govpay.servizi.commons.Pagamento toPagamento(Pagamento pagamento) throws ServiceException {
		it.govpay.servizi.commons.Pagamento p = new it.govpay.servizi.commons.Pagamento();

		if(pagamento.getAllegato() != null) {
			Allegato allegato = new Allegato();
			allegato.setTesto(pagamento.getAllegato());
			allegato.setTipo(TipoAllegato.valueOf(pagamento.getTipoAllegato().toString()));
			p.setAllegato(allegato);
		}
		p.setCodSingoloVersamentoEnte(pagamento.getSingoloVersamento(null).getCodSingoloVersamentoEnte());
		p.setCommissioniPsp(pagamento.getCommissioniPsp());
		p.setDataPagamento(pagamento.getDataPagamento());
		p.setImportoPagato(pagamento.getImportoPagato());
		p.setIur(pagamento.getIur());
		p.setCausaleRevoca(pagamento.getCausaleRevoca());
		p.setDatiEsitoRevoca(pagamento.getDatiEsitoRevoca());
		p.setDatiRevoca(pagamento.getDatiRevoca());
		p.setEsitoRevoca(pagamento.getEsitoRevoca());
		p.setImportoRevocato(pagamento.getImportoRevocato());
//		if(versione.compareTo(Versione.GP_02_02_00) >= 0) { // Versione 2.2
			p.setDataAcquisizione(pagamento.getDataAcquisizione());
			p.setDataAcquisizioneRevoca(pagamento.getDataAcquisizioneRevoca());
//		}
//		if(versione.compareTo(Versione.GP_02_05_00) >= 0) { // Versione 2.5
//			if(pagamento.getSingoloVersamento(bd) != null && pagamento.getSingoloVersamento(bd).getIbanAccredito(bd) != null)
//				p.setIbanAccredito(pagamento.getSingoloVersamento(bd).getIbanAccredito(bd).getCodIban());
//			if(pagamento.getSingoloVersamento(bd) != null && pagamento.getSingoloVersamento(bd).getIbanAppoggio(bd) != null)	
//				p.setIbanAppoggio(pagamento.getSingoloVersamento(bd).getIbanAppoggio(bd).getCodIban());
//		}
		return p;
	}

	public static it.govpay.servizi.commons.FlussoRendicontazione.Pagamento toRendicontazionePagamento(Rendicontazione rend, Fr frModel, BDConfigWrapper configWrapper) throws ServiceException {

		if(rend.getVersamento(null) == null) return null;

		FlussoRendicontazione.Pagamento p = new FlussoRendicontazione.Pagamento();
		if(rend.getPagamento(null) != null)
			p.setCodSingoloVersamentoEnte(rend.getPagamento(null).getSingoloVersamento(null).getCodSingoloVersamentoEnte());
		else
			p.setCodSingoloVersamentoEnte(rend.getVersamento(null).getSingoliVersamenti().get(0).getCodSingoloVersamentoEnte());
		p.setImportoRendicontato(rend.getImporto().abs());
		p.setIur(rend.getIur());
		p.setEsitoRendicontazione(TipoRendicontazione.valueOf(rend.getEsito().toString()));
		p.setDataRendicontazione(rend.getData());
//		if(versione.compareTo(Versione.GP_02_02_00) >= 0) { // Versione 2.2
			p.setCodApplicazione(rend.getVersamento(null).getApplicazione(configWrapper).getCodApplicazione());
			p.setIuv(rend.getIuv());
			p.setCodDominio(frModel.getCodDominio());
//		}

		return p;
	}

	public static List<it.govpay.bd.model.Versamento.StatoVersamento> toStatiVersamento(List<StatoVersamento> stati) {
		if(stati == null || stati.size() == 0) return null;

		List<it.govpay.bd.model.Versamento.StatoVersamento> statiVersamento = new ArrayList<it.govpay.bd.model.Versamento.StatoVersamento>();
		for(StatoVersamento stato : stati) {
			statiVersamento.add(it.govpay.bd.model.Versamento.StatoVersamento.valueOf(stato.toString()));
		}
		return statiVersamento;
	}

	public static SortFields toFilterSort(String ordinamento) {
		if(ordinamento == null) return null;

		if(ordinamento.equals("DATA_SCADENZA_ASC"))
			return SortFields.SCADENZA_ASC;

		if(ordinamento.equals("DATA_SCADENZA_DES"))
			return SortFields.SCADENZA_DESC;

		if(ordinamento.equals("DATA_CARICAMENTO_ASC"))
			return SortFields.CARICAMENTO_ASC;

		if(ordinamento.equals("DATA_CARICAMENTO_DES"))
			return SortFields.CARICAMENTO_DESC;

		if(ordinamento.equals("DATA_AGGIORNAMENTO_ASC"))
			return SortFields.AGGIORNAMENTO_ASC;

		if(ordinamento.equals("DATA_AGGIORNAMENTO_DES"))
			return SortFields.AGGIORNAMENTO_DESC;

		return null;
	}

	public static List<IuvGenerato> toIuvGenerato(List<it.govpay.core.business.model.Iuv> iuvGeneratiModel, Applicazione applicazione) {
		List<IuvGenerato> iuvGenerati = new ArrayList<IuvGenerato>();
		for (it.govpay.core.business.model.Iuv iuvGeneratoModel : iuvGeneratiModel) {
			iuvGenerati.add(toIuvGenerato(iuvGeneratoModel, applicazione));
		}
		return iuvGenerati;
	}

	public static IuvGenerato toIuvGenerato(it.govpay.core.business.model.Iuv iuvGeneratoModel, Applicazione applicazione) {
		IuvGenerato iuvGenerato = new IuvGenerato();
		iuvGenerato.setBarCode(iuvGeneratoModel.getBarCode());
		iuvGenerato.setCodApplicazione(iuvGeneratoModel.getCodApplicazione());
		iuvGenerato.setCodDominio(iuvGeneratoModel.getCodDominio());
		iuvGenerato.setCodVersamentoEnte(iuvGeneratoModel.getCodVersamentoEnte());
		iuvGenerato.setIuv(iuvGeneratoModel.getIuv());
		iuvGenerato.setQrCode(iuvGeneratoModel.getQrCode());
		//		if(applicazione.getVersione().compareTo(Versione.GP_02_03_00) >= 0) { // Versione 2.3
		//			iuvGenerato.setNumeroAvviso(iuvGeneratoModel.getNumeroAvviso());
		//		}
		return iuvGenerato;
	}

	public static it.govpay.core.dao.commons.Versamento toVersamentoCommons(it.govpay.servizi.commons.Versamento pendenza) throws ServiceException, IOException, GovPayException {
		it.govpay.core.dao.commons.Versamento versamento = new it.govpay.core.dao.commons.Versamento();

		if(pendenza.getAnnoTributario() != null)
			versamento.setAnnoTributario(pendenza.getAnnoTributario().intValue());

		versamento.setCausale(pendenza.getCausale());
		versamento.setCodApplicazione(pendenza.getCodApplicazione());

		versamento.setCodDominio(pendenza.getCodDominio());
		versamento.setCodUnitaOperativa(pendenza.getCodUnitaOperativa());
		versamento.setCodVersamentoEnte(pendenza.getCodVersamentoEnte());
		versamento.setDataScadenza(pendenza.getDataScadenza());
		//		versamento.setDataValidita(pendenza.getDataValidita());
		//		versamento.setDataCaricamento(pendenza.getDataCaricamento() != null ? pendenza.getDataCaricamento() : new Date());
		versamento.setDataCaricamento(new Date());
		versamento.setDebitore(toAnagraficaCommons(pendenza.getDebitore()));

//		versamento.setTassonomia(pendenza.getTassonomia());
//		if(pendenza.getDatiAllegati() != null)
//			versamento.setDatiAllegati(ConverterUtils.toJSON(pendenza.getDatiAllegati(),null));

//		if(pendenza.getTassonomiaAvviso() != null) {
//			// valore tassonomia avviso non valido
//			if(TassonomiaAvviso.fromValue(pendenza.getTassonomiaAvviso()) == null) {
//				throw new ValidationException("Codifica inesistente per tassonomiaAvviso. Valore fornito [" + pendenza.getTassonomiaAvviso() + "] valori possibili " + ArrayUtils.toString(TassonomiaAvviso.values()));
//			}
//
//			versamento.setTassonomiaAvviso(pendenza.getTassonomiaAvviso());
//		}

		versamento.setIuv(pendenza.getIuv());

		// voci pagamento
		BigDecimal importoVociPendenza = fillSingoliVersamentiFromVociPendenza(versamento, pendenza.getSingoloVersamento());

		// importo pendenza puo' essere null
		versamento.setImportoTotale(pendenza.getImportoTotale() != null ? pendenza.getImportoTotale() : importoVociPendenza); 

		// tipo Pendenza
//		versamento.setCodTipoVersamento(pendenza.getIdTipoPendenza()); TODO

		//		versamento.setDirezione(pendenza.getDirezione());
		//		versamento.setDivisione(pendenza.getDivisione()); 
		versamento.setCodLotto(pendenza.getCodDebito());

		//		if(pendenza.getDocumento() != null) {
		//			it.govpay.core.dao.commons.Versamento.Documento documento = new it.govpay.core.dao.commons.Versamento.Documento();
		//			
		//			documento.setCodDocumento(pendenza.getDocumento().getIdentificativo());
		//			if(pendenza.getDocumento().getRata() != null)
		//				documento.setCodRata(pendenza.getDocumento().getRata().intValue());
		//			if(pendenza.getDocumento().getSoglia() != null) {
		//				// valore tassonomia avviso non valido
		//				if(TipoSogliaVincoloPagamento.fromValue(pendenza.getDocumento().getSoglia().getTipo()) == null) {
		//					throw new ValidationException("Codifica inesistente per tipo. Valore fornito [" 
		//								+ pendenza.getDocumento().getSoglia().getTipo() + "] valori possibili " + ArrayUtils.toString(TipoSogliaVincoloPagamento.values()));
		//				}
		//				
		//				documento.setGiorniSoglia(pendenza.getDocumento().getSoglia().getGiorni().intValue());
		//				documento.setTipoSoglia(pendenza.getDocumento().getSoglia().getTipo());
		//			}
		//			documento.setDescrizione(pendenza.getDocumento().getDescrizione());
		//
		//			versamento.setDocumento(documento );
		//		}

		//		versamento.setDataNotificaAvviso(pendenza.getDataNotificaAvviso());
		//		versamento.setDataPromemoriaScadenza(pendenza.getDataPromemoriaScadenza());

		//		versamento.setProprieta(toProprietaPendenzaDTO(pendenza.getProprieta()));

		return versamento;
	}

	public static BigDecimal fillSingoliVersamentiFromVociPendenza(it.govpay.core.dao.commons.Versamento versamento, List<SingoloVersamento> voci) throws ServiceException, IOException, GovPayException {

		BigDecimal importoTotale = BigDecimal.ZERO;

		if(voci != null && voci.size() > 0) {
			for (SingoloVersamento vocePendenza : voci) {
				it.govpay.core.dao.commons.Versamento.SingoloVersamento sv = new it.govpay.core.dao.commons.Versamento.SingoloVersamento();

				//sv.setCodTributo(value); ??

				sv.setCodSingoloVersamentoEnte(vocePendenza.getCodSingoloVersamentoEnte());
//				if(vocePendenza.getDatiAllegati() != null)
//					sv.setDatiAllegati(ConverterUtils.toJSON(vocePendenza.getDatiAllegati(),null));
//				sv.setDescrizione(vocePendenza.getDescrizione());
//				sv.setDescrizioneCausaleRPT(vocePendenza.getDescrizioneCausaleRPT());
				sv.setImporto(vocePendenza.getImporto());
				sv.setCodDominio(versamento.getCodDominio());

				importoTotale = importoTotale.add(vocePendenza.getImporto());

				// Definisce i dati di un bollo telematico
				if(vocePendenza.getBolloTelematico() != null) {
					it.govpay.core.dao.commons.Versamento.SingoloVersamento.BolloTelematico bollo = new it.govpay.core.dao.commons.Versamento.SingoloVersamento.BolloTelematico();
					bollo.setHash(vocePendenza.getBolloTelematico().getHash());
					bollo.setProvincia(vocePendenza.getBolloTelematico().getProvincia());
					bollo.setTipo(vocePendenza.getBolloTelematico().getTipo());
					sv.setBolloTelematico(bollo);
				} else if(vocePendenza.getCodTributo() != null) { // Definisce i dettagli di incasso tramite riferimento in anagrafica GovPay.
					sv.setCodTributo(vocePendenza.getCodTributo());

				} else { // Definisce i dettagli di incasso della singola entrata.
					if(vocePendenza.getTributo() != null) {
						it.govpay.core.dao.commons.Versamento.SingoloVersamento.Tributo tributo = new it.govpay.core.dao.commons.Versamento.SingoloVersamento.Tributo();
						tributo.setCodContabilita(vocePendenza.getTributo().getCodContabilita());
						tributo.setIbanAccredito(vocePendenza.getTributo().getIbanAccredito());
						tributo.setIbanAppoggio(vocePendenza.getTributo().getIbanAppoggio());
						tributo.setTipoContabilita(it.govpay.core.dao.commons.Versamento.SingoloVersamento.TipoContabilita.valueOf(vocePendenza.getTributo().getTipoContabilita().name()));
						sv.setTributo(tributo);
					}
				}
				
//				sv.setContabilita(ContabilitaConverter.toStringDTO(vocePendenza.getContabilita()));
				
//				if(vocePendenza.getContabilita() != null) {
//					if(vocePendenza.getContabilita().getQuote() != null) {
//						BigDecimal somma = BigDecimal.ZERO;
//						for (QuotaContabilita voceContabilita : vocePendenza.getContabilita().getQuote()) {
//							somma = somma.add(voceContabilita.getImporto());
//						}
//						
//						if(somma.compareTo(vocePendenza.getImporto()) != 0) {
//							throw new GovPayException(EsitoOperazione.VER_035, vocePendenza.getIdVocePendenza(),  versamento.getCodApplicazione(), versamento.getCodVersamentoEnte(),
//								Double.toString(sv.getImporto().doubleValue()), Double.toString(somma.doubleValue()));
//						}
//					}
//				}

				versamento.getSingoloVersamento().add(sv);
			}
		}

		return importoTotale;
	}

	public static it.govpay.core.dao.commons.Anagrafica toAnagraficaCommons(Anagrafica anagrafica) {
		if(anagrafica == null) return null;
		it.govpay.core.dao.commons.Anagrafica anagraficaModel = new it.govpay.core.dao.commons.Anagrafica();
		anagraficaModel.setCap(anagrafica.getCap());
		anagraficaModel.setCellulare(anagrafica.getCellulare());
		anagraficaModel.setCivico(anagrafica.getCivico());
		anagraficaModel.setCodUnivoco(anagrafica.getCodUnivoco());
		anagraficaModel.setEmail(anagrafica.getEmail());
		anagraficaModel.setFax(anagrafica.getFax());
		anagraficaModel.setIndirizzo(anagrafica.getIndirizzo());
		anagraficaModel.setLocalita(anagrafica.getLocalita());
		anagraficaModel.setNazione(anagrafica.getNazione());
		anagraficaModel.setProvincia(anagrafica.getProvincia());
		anagraficaModel.setRagioneSociale(anagrafica.getRagioneSociale());
		anagraficaModel.setTelefono(anagrafica.getTelefono());
		return anagraficaModel;
	}

	public static PendenzaVerificata toPendenzaVerificata(PaVerificaVersamentoResponse paVerificaVersamentoResponse, BDConfigWrapper configWrapper ) throws ValidationException, ServiceException {
		if(paVerificaVersamentoResponse == null)
			throw new ValidationException("Risposta vuota");
		
		PendenzaVerificata versamento = new PendenzaVerificata();
		
		EsitoVerificaVersamento codEsito = paVerificaVersamentoResponse.getCodEsito();
		String descrizioneEsito = paVerificaVersamentoResponse.getDescrizioneEsito();
		Versamento pendenza = paVerificaVersamentoResponse.getVersamento();
		
		if(codEsito != null) {
			switch (codEsito) {
			case OK:
				versamento.setStato(StatoPendenzaVerificata.NON_ESEGUITA);
				break;
			case PAGAMENTO_ANNULLATO:
				versamento.setStato(StatoPendenzaVerificata.ANNULLATA);
				break;
			case PAGAMENTO_DUPLICATO:
				versamento.setStato(StatoPendenzaVerificata.DUPLICATA);
				break;
			case PAGAMENTO_SCADUTO:
				versamento.setStato(StatoPendenzaVerificata.SCADUTA);
				break;
			case PAGAMENTO_SCONOSCIUTO:
				versamento.setStato(StatoPendenzaVerificata.SCONOSCIUTA);
				break;
			}
		}
		versamento.setDescrizioneStato(descrizioneEsito);
		
		
		if(pendenza.getAnnoTributario() != null)
			versamento.setAnnoRiferimento(new BigDecimal(pendenza.getAnnoTributario().intValue()));

		versamento.setCausale(pendenza.getCausale());
		versamento.setIdA2A(pendenza.getCodApplicazione());

		versamento.setIdDominio(pendenza.getCodDominio());
		versamento.setIdUnitaOperativa(pendenza.getCodUnitaOperativa());
		versamento.setIdPendenza(pendenza.getCodVersamentoEnte());
		versamento.setDataScadenza(pendenza.getDataScadenza());
		versamento.setSoggettoPagatore(toSoggettoPagatore(pendenza.getDebitore()));


		Dominio dominio;
		try {
			dominio = AnagraficaManager.getDominio(configWrapper, pendenza.getCodDominio());
		} catch (NotFoundException e) {
			throw new ValidationException("Dominio ["+pendenza.getCodDominio()+"] inesistente.");
		}
		
		versamento.setNumeroAvviso(IuvUtils.toNumeroAvviso(pendenza.getIuv(), dominio ));

		// voci pagamento
		BigDecimal importoVociPendenza = fillVociPendenzaFromSingoliVersamenti(versamento, pendenza.getSingoloVersamento());

		// importo pendenza puo' essere null
		versamento.setImporto(pendenza.getImportoTotale() != null ? pendenza.getImportoTotale() : importoVociPendenza); 

		// tipo Pendenza
//		versamento.setCodTipoVersamento(pendenza.getIdTipoPendenza()); TODO

		//		versamento.setDirezione(pendenza.getDirezione());
		//		versamento.setDivisione(pendenza.getDivisione()); 
//		versamento.setCodLotto(pendenza.getCodDebito());

		//		if(pendenza.getDocumento() != null) {
		//			it.govpay.core.dao.commons.Versamento.Documento documento = new it.govpay.core.dao.commons.Versamento.Documento();
		//			
		//			documento.setCodDocumento(pendenza.getDocumento().getIdentificativo());
		//			if(pendenza.getDocumento().getRata() != null)
		//				documento.setCodRata(pendenza.getDocumento().getRata().intValue());
		//			if(pendenza.getDocumento().getSoglia() != null) {
		//				// valore tassonomia avviso non valido
		//				if(TipoSogliaVincoloPagamento.fromValue(pendenza.getDocumento().getSoglia().getTipo()) == null) {
		//					throw new ValidationException("Codifica inesistente per tipo. Valore fornito [" 
		//								+ pendenza.getDocumento().getSoglia().getTipo() + "] valori possibili " + ArrayUtils.toString(TipoSogliaVincoloPagamento.values()));
		//				}
		//				
		//				documento.setGiorniSoglia(pendenza.getDocumento().getSoglia().getGiorni().intValue());
		//				documento.setTipoSoglia(pendenza.getDocumento().getSoglia().getTipo());
		//			}
		//			documento.setDescrizione(pendenza.getDocumento().getDescrizione());
		//
		//			versamento.setDocumento(documento );
		//		}

		//		versamento.setDataNotificaAvviso(pendenza.getDataNotificaAvviso());
		//		versamento.setDataPromemoriaScadenza(pendenza.getDataPromemoriaScadenza());

		//		versamento.setProprieta(toProprietaPendenzaDTO(pendenza.getProprieta()));
		
		
		return versamento;
	}

	private static BigDecimal fillVociPendenzaFromSingoliVersamenti(PendenzaVerificata versamento, List<SingoloVersamento> voci) {
		BigDecimal importoTotale = BigDecimal.ZERO;

		if(voci != null && voci.size() > 0) {
			for (SingoloVersamento vocePendenza : voci) {
				VocePendenza sv = new VocePendenza();

				sv.setIdVocePendenza(vocePendenza.getCodSingoloVersamentoEnte());
				sv.setImporto(vocePendenza.getImporto());
				sv.setIdDominio(versamento.getIdDominio());

				importoTotale = importoTotale.add(vocePendenza.getImporto());

				// Definisce i dati di un bollo telematico
				if(vocePendenza.getBolloTelematico() != null) {
					sv.setHashDocumento(vocePendenza.getBolloTelematico().getHash());
					sv.setProvinciaResidenza(vocePendenza.getBolloTelematico().getProvincia());
					sv.setTipoBollo(vocePendenza.getBolloTelematico().getTipo());
				} else if(vocePendenza.getCodTributo() != null) { // Definisce i dettagli di incasso tramite riferimento in anagrafica GovPay.
					sv.setCodEntrata(vocePendenza.getCodTributo());

				} else { // Definisce i dettagli di incasso della singola entrata.
					if(vocePendenza.getTributo() != null) {
						sv.setCodiceContabilita(vocePendenza.getTributo().getCodContabilita());
						sv.setIbanAccredito(vocePendenza.getTributo().getIbanAccredito());
						sv.setIbanAppoggio(vocePendenza.getTributo().getIbanAppoggio());
						sv.setTipoContabilita(TipoContabilita.valueOf(vocePendenza.getTributo().getTipoContabilita().name()));
					}
				}

				versamento.getVoci().add(sv);
			}
		}

		return importoTotale;
	}

	private static Soggetto toSoggettoPagatore(Anagrafica anagrafica) {
		if(anagrafica == null) return null;
		Soggetto anagraficaModel = new Soggetto();
		anagraficaModel.setCap(anagrafica.getCap());
		anagraficaModel.setCellulare(anagrafica.getCellulare());
		anagraficaModel.setCivico(anagrafica.getCivico());
		anagraficaModel.setIdentificativo(anagrafica.getCodUnivoco());
		anagraficaModel.setEmail(anagrafica.getEmail());
//		anagraficaModel.setFax(anagrafica.getFax());
		anagraficaModel.setIndirizzo(anagrafica.getIndirizzo());
		anagraficaModel.setLocalita(anagrafica.getLocalita());
		anagraficaModel.setNazione(anagrafica.getNazione());
		anagraficaModel.setProvincia(anagrafica.getProvincia());
		anagraficaModel.setAnagrafica(anagrafica.getRagioneSociale());
//		anagraficaModel.setTelefono(anagrafica.getTelefono());
		return anagraficaModel;
	}
}
