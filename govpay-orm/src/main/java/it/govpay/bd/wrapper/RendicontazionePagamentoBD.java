package it.govpay.bd.wrapper;

import java.io.UnsupportedEncodingException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.openspcoop2.generic_project.exception.NotFoundException;
import org.openspcoop2.generic_project.exception.NotImplementedException;
import org.openspcoop2.generic_project.exception.ServiceException;
import org.openspcoop2.utils.LoggerWrapperFactory;

import it.govpay.bd.BasicBD;
import it.govpay.bd.model.Fr;
import it.govpay.bd.model.RendicontazionePagamento;
import it.govpay.bd.nativequeries.NativeQueries;
import it.govpay.bd.wrapper.filters.RendicontazionePagamentoFilter;
import it.govpay.model.Anagrafica;
import it.govpay.model.Fr.StatoFr;
import it.govpay.model.Pagamento.Stato;
import it.govpay.model.Pagamento.TipoAllegato;
import it.govpay.model.Rendicontazione.EsitoRendicontazione;
import it.govpay.model.Rendicontazione.StatoRendicontazione;
import it.govpay.model.SingoloVersamento.StatoSingoloVersamento;
import it.govpay.model.SingoloVersamento.TipoBollo;
import it.govpay.model.Tributo.TipoContabilita;
import it.govpay.model.Versamento.StatoVersamento;
import it.govpay.orm.FR;
import it.govpay.orm.Incasso;
import it.govpay.orm.Pagamento;
import it.govpay.orm.Rendicontazione;
import it.govpay.orm.SingoloVersamento;
import it.govpay.orm.Versamento;
import it.govpay.orm.dao.jdbc.JDBCServiceManager;

public class RendicontazionePagamentoBD extends BasicBD {

	public RendicontazionePagamentoBD(BasicBD basicBD) {
		super(basicBD);
	}
	
	public RendicontazionePagamentoFilter newFilter() throws ServiceException {
		return new RendicontazionePagamentoFilter(this.getRendicontazionePagamentoServiceSearch());
	}

	public long count(RendicontazionePagamentoFilter filter) throws ServiceException {
		try {
			List<Class<?>> lstReturnType = new ArrayList<Class<?>>();
			lstReturnType.add(Long.class);
			String nativeCount = NativeQueries.getInstance().getRendicontazionePagamentoCountQuery();
			String sqlFilterString = filter.getSQLFilterString(nativeCount);
			Object[] fields = filter.getFields(true).toArray(new Object[]{});
			List<List<Object>> count = this.getRendicontazionePagamentoServiceSearch().nativeQuery(sqlFilterString, lstReturnType, fields);
			
			if(count.size() > 0) {
				return ((Long) count.get(0).get(0)).longValue();
			}
			
			return 0;
			
		} catch (NotImplementedException e) {
			throw new ServiceException(e);
		} catch (NotFoundException e) {
			return 0;
		}
	}

	public List<RendicontazionePagamento> findAll(RendicontazionePagamentoFilter filter) throws ServiceException {
		try {
			
			List<Class<?>> lstReturnType = new ArrayList<Class<?>>();
			
			lstReturnType.add(FR.model().COD_FLUSSO.getFieldType());
			lstReturnType.add(FR.model().STATO.getFieldType());
			lstReturnType.add(FR.model().DESCRIZIONE_STATO.getFieldType());
			lstReturnType.add(FR.model().IUR.getFieldType());
			lstReturnType.add(FR.model().DATA_ORA_FLUSSO.getFieldType());
			lstReturnType.add(FR.model().DATA_REGOLAMENTO.getFieldType());
			lstReturnType.add(FR.model().DATA_ACQUISIZIONE.getFieldType());
			lstReturnType.add(FR.model().NUMERO_PAGAMENTI.getFieldType());
			lstReturnType.add(FR.model().IMPORTO_TOTALE_PAGAMENTI.getFieldType());
			lstReturnType.add(FR.model().COD_BIC_RIVERSAMENTO.getFieldType());
			lstReturnType.add(FR.model().XML.getFieldType());
			lstReturnType.add(Long.class); //id
			lstReturnType.add(FR.model().COD_PSP.getFieldType());
			lstReturnType.add(FR.model().COD_DOMINIO.getFieldType());
			
			
			lstReturnType.add(Versamento.model().COD_VERSAMENTO_ENTE.getFieldType());
			lstReturnType.add(Versamento.model().IMPORTO_TOTALE.getFieldType());
			lstReturnType.add(Versamento.model().STATO_VERSAMENTO.getFieldType());
			lstReturnType.add(Versamento.model().DESCRIZIONE_STATO.getFieldType());
			lstReturnType.add(Versamento.model().AGGIORNABILE.getFieldType());
			lstReturnType.add(Versamento.model().DATA_CREAZIONE.getFieldType());
			lstReturnType.add(Versamento.model().DATA_SCADENZA.getFieldType());
			lstReturnType.add(Versamento.model().DATA_ORA_ULTIMO_AGGIORNAMENTO.getFieldType());
			lstReturnType.add(Versamento.model().CAUSALE_VERSAMENTO.getFieldType());

			lstReturnType.add(Versamento.model().DEBITORE_IDENTIFICATIVO.getFieldType());
			lstReturnType.add(Versamento.model().DEBITORE_ANAGRAFICA.getFieldType());
			lstReturnType.add(Versamento.model().DEBITORE_INDIRIZZO.getFieldType());
			lstReturnType.add(Versamento.model().DEBITORE_CIVICO.getFieldType());
			lstReturnType.add(Versamento.model().DEBITORE_CAP.getFieldType());
			lstReturnType.add(Versamento.model().DEBITORE_LOCALITA.getFieldType());
			lstReturnType.add(Versamento.model().DEBITORE_PROVINCIA.getFieldType());
			lstReturnType.add(Versamento.model().DEBITORE_NAZIONE.getFieldType());
			lstReturnType.add(Versamento.model().DEBITORE_TELEFONO.getFieldType());
			lstReturnType.add(Versamento.model().DEBITORE_CELLULARE.getFieldType());
			lstReturnType.add(Versamento.model().DEBITORE_FAX.getFieldType());
			lstReturnType.add(Versamento.model().DEBITORE_EMAIL.getFieldType());

			lstReturnType.add(Versamento.model().COD_LOTTO.getFieldType());
			lstReturnType.add(Versamento.model().COD_VERSAMENTO_LOTTO.getFieldType());
			lstReturnType.add(Versamento.model().COD_ANNO_TRIBUTARIO.getFieldType());
			lstReturnType.add(Versamento.model().COD_BUNDLEKEY.getFieldType());
			lstReturnType.add(Long.class); //id
			lstReturnType.add(Long.class); //id_uo
			lstReturnType.add(Long.class); //id_applicazione

			lstReturnType.add(Rendicontazione.model().IUV.getFieldType());
			lstReturnType.add(Rendicontazione.model().IUR.getFieldType());
			lstReturnType.add(Rendicontazione.model().IMPORTO_PAGATO.getFieldType());
			lstReturnType.add(Rendicontazione.model().ESITO.getFieldType());
			lstReturnType.add(Rendicontazione.model().DATA.getFieldType());
			lstReturnType.add(Rendicontazione.model().STATO.getFieldType());
			lstReturnType.add(Rendicontazione.model().ANOMALIE.getFieldType());
			lstReturnType.add(Long.class);//id
			lstReturnType.add(Long.class);//id_fr
			lstReturnType.add(Long.class);//id_pagamento
			
			lstReturnType.add(Pagamento.model().IMPORTO_PAGATO.getFieldType());
			lstReturnType.add(Pagamento.model().DATA_ACQUISIZIONE.getFieldType());
			lstReturnType.add(Pagamento.model().IUR.getFieldType());
			lstReturnType.add(Pagamento.model().DATA_PAGAMENTO.getFieldType());
			lstReturnType.add(Pagamento.model().COMMISSIONI_PSP.getFieldType());
			lstReturnType.add(Pagamento.model().TIPO_ALLEGATO.getFieldType());
			lstReturnType.add(Pagamento.model().ALLEGATO.getFieldType());
			lstReturnType.add(Pagamento.model().DATA_ACQUISIZIONE_REVOCA.getFieldType());
			lstReturnType.add(Pagamento.model().CAUSALE_REVOCA.getFieldType());
			lstReturnType.add(Pagamento.model().DATI_REVOCA.getFieldType());
			lstReturnType.add(Pagamento.model().IMPORTO_REVOCATO.getFieldType());
			lstReturnType.add(Pagamento.model().ESITO_REVOCA.getFieldType());
			lstReturnType.add(Pagamento.model().DATI_ESITO_REVOCA.getFieldType());
			lstReturnType.add(Long.class); //id
			lstReturnType.add(Long.class); //id_rpt
			lstReturnType.add(Long.class); //id_singolo_versamento
			lstReturnType.add(Long.class); //id_rr
			lstReturnType.add(Long.class); //id_incasso
//			lstReturnType.add(Pagamento.model().IBAN_ACCREDITO.getFieldType());
			lstReturnType.add(Pagamento.model().COD_DOMINIO.getFieldType());
			lstReturnType.add(Pagamento.model().IUV.getFieldType());
			lstReturnType.add(Pagamento.model().STATO.getFieldType());

			lstReturnType.add(SingoloVersamento.model().COD_SINGOLO_VERSAMENTO_ENTE.getFieldType());
			lstReturnType.add(SingoloVersamento.model().STATO_SINGOLO_VERSAMENTO.getFieldType());
			lstReturnType.add(SingoloVersamento.model().IMPORTO_SINGOLO_VERSAMENTO.getFieldType());
			lstReturnType.add(SingoloVersamento.model().TIPO_BOLLO.getFieldType());
			lstReturnType.add(SingoloVersamento.model().HASH_DOCUMENTO.getFieldType());
			lstReturnType.add(SingoloVersamento.model().PROVINCIA_RESIDENZA.getFieldType());
			lstReturnType.add(SingoloVersamento.model().TIPO_CONTABILITA.getFieldType());
			lstReturnType.add(SingoloVersamento.model().CODICE_CONTABILITA.getFieldType());
			lstReturnType.add(SingoloVersamento.model().DATI_ALLEGATI.getFieldType());
			lstReturnType.add(Long.class); //id
			lstReturnType.add(Long.class); //id_versamento
			lstReturnType.add(Long.class); //id_tributo
			lstReturnType.add(Long.class); //id_iban_accredito
			
			
			lstReturnType.add(String.class); //tipo
			
			lstReturnType.add(Incasso.model().DATA_ORA_INCASSO.getFieldType()); // data ora incasso
			
			String initialNativeQuery = NativeQueries.getInstance().getRendicontazionePagamentoQuery();
			String nativeQueryString = filter.getSQLFilterString(initialNativeQuery);
			
			LoggerWrapperFactory.getLogger(JDBCServiceManager.class).debug(nativeQueryString);
			Object[] array = filter.getFields(false).toArray(new Object[]{});
			LoggerWrapperFactory.getLogger(JDBCServiceManager.class).debug("Params: ");
			for(Object obj: array) {
				LoggerWrapperFactory.getLogger(JDBCServiceManager.class).debug(obj.toString());
			}

			List<List<Object>> lstRecords = this.getRendicontazionePagamentoServiceSearch().nativeQuery(nativeQueryString, lstReturnType, array);
			List<RendicontazionePagamento> lstNonFiltrata = new ArrayList<RendicontazionePagamento>();

			for(List<Object> record: lstRecords) {
				lstNonFiltrata.add(getRendicontazionePagamento(record));
			}
			return lstNonFiltrata;
		} catch (NotImplementedException e) {
			throw new ServiceException(e);
		} catch (NotFoundException e) {
			return new ArrayList<RendicontazionePagamento>();
		}
	}

	private RendicontazionePagamento getRendicontazionePagamento(List<Object> record) throws ServiceException {
		
		RendicontazionePagamento rp = new RendicontazionePagamento();
		int i =0;
		Fr fr = new Fr();
		fr.setCodFlusso((String)record.get(i++));
		fr.setStato(StatoFr.valueOf((String)record.get(i++)));
		fr.setDescrizioneStato((String)record.get(i++));
		fr.setIur((String)record.get(i++));
		fr.setDataFlusso((Date)record.get(i++));
		fr.setDataRegolamento((Date)record.get(i++));
		fr.setDataAcquisizione((Date)record.get(i++));
		fr.setNumeroPagamenti((Long)record.get(i++));
		fr.setImportoTotalePagamenti(new BigDecimal((Double)record.get(i++)));
		fr.setCodBicRiversamento((String)record.get(i++));
		fr.setXml((byte[])record.get(i++));
		fr.setId((Long)record.get(i++));
		fr.setCodPsp((String)record.get(i++));
		fr.setCodDominio((String)record.get(i++));

		rp.setFr(fr);

		Object idPagamento = record.get(65);
		
		boolean existsPagamento = idPagamento != null;
		
		boolean existsVersamento = false;
		if(existsPagamento) {
			Object idSingoloVersamento = record.get(83);
			existsVersamento = idSingoloVersamento != null;
		}
		
		if(existsVersamento) {
			it.govpay.bd.model.Versamento versamento = new it.govpay.bd.model.Versamento();
			versamento.setCodVersamentoEnte((String) record.get(i++));
			versamento.setImportoTotale(new BigDecimal((Double) record.get(i++)));
			versamento.setStatoVersamento(StatoVersamento.valueOf((String) record.get(i++)));
			versamento.setDescrizioneStato((String) record.get(i++));
			versamento.setAggiornabile((Boolean) record.get(i++));
			versamento.setDataCreazione((Date) record.get(i++));
			versamento.setDataScadenza((Date) record.get(i++));
			versamento.setDataUltimoAggiornamento((Date) record.get(i++));
			try {
				versamento.setCausaleVersamento((String) record.get(i++));
			}catch(UnsupportedEncodingException e) {
				throw new ServiceException(e);
			}
	
			Anagrafica anagraficaDebitore = new Anagrafica();
			
			anagraficaDebitore.setCodUnivoco((String) record.get(i++));
			anagraficaDebitore.setRagioneSociale((String) record.get(i++));
			anagraficaDebitore.setIndirizzo((String) record.get(i++));
			anagraficaDebitore.setCivico((String) record.get(i++));
			anagraficaDebitore.setCap((String) record.get(i++));
			anagraficaDebitore.setLocalita((String) record.get(i++));
			anagraficaDebitore.setProvincia((String) record.get(i++));
			anagraficaDebitore.setNazione((String) record.get(i++));
			anagraficaDebitore.setTelefono((String) record.get(i++));
			anagraficaDebitore.setCellulare((String) record.get(i++));
			anagraficaDebitore.setFax((String) record.get(i++));
			anagraficaDebitore.setEmail((String) record.get(i++));
	
			versamento.setAnagraficaDebitore(anagraficaDebitore);
			versamento.setCodLotto((String) record.get(i++));
			versamento.setCodVersamentoLotto((String) record.get(i++));
			Object codAnnoObj = record.get(i++);
			if(codAnnoObj != null) {
				versamento.setCodAnnoTributario(Integer.parseInt((String) codAnnoObj));
			}
			versamento.setCodBundlekey((String) record.get(i++));
			versamento.setId((Long) record.get(i++));
			versamento.setIdUo((Long) record.get(i++));
			versamento.setIdApplicazione((Long) record.get(i++));
	
			rp.setVersamento(versamento);
		} else {
			i+= 28;
		}
		
		it.govpay.bd.model.Rendicontazione rendicontazione = new it.govpay.bd.model.Rendicontazione();
		rendicontazione.setIuv((String) record.get(i++));
		rendicontazione.setIur((String) record.get(i++));
		rendicontazione.setImporto(new BigDecimal((Double) record.get(i++)));
		rendicontazione.setEsito(EsitoRendicontazione.toEnum((Integer) record.get(i++)));
		rendicontazione.setData((Date) record.get(i++));
		rendicontazione.setStato(StatoRendicontazione.valueOf((String) record.get(i++)));
		rendicontazione.setAnomalie((String) record.get(i++));
		rendicontazione.setId((Long) record.get(i++));
		rendicontazione.setIdFr((Long) record.get(i++));
		rendicontazione.setIdPagamento((Long) record.get(i++));
		rp.setRendicontazione(rendicontazione);
		
		boolean existsIncasso = false;
		
		if(existsPagamento) {
			it.govpay.bd.model.Pagamento pagamento = new it.govpay.bd.model.Pagamento();
			pagamento.setImportoPagato(new BigDecimal((Double) record.get(i++)));
			pagamento.setDataAcquisizione((Date) record.get(i++));
			pagamento.setIur((String) record.get(i++));
			pagamento.setDataPagamento((Date) record.get(i++));
			if(record.get(i) != null) {
				pagamento.setCommissioniPsp(new BigDecimal((Double) record.get(i++)));
			} else {
				i++;
			}
			
			if(record.get(i) != null) {
				pagamento.setTipoAllegato(TipoAllegato.valueOf((String) record.get(i++)));
			} else {
				i++;
			}
			
			pagamento.setAllegato((byte[]) record.get(i++));
			pagamento.setDataAcquisizioneRevoca((Date) record.get(i++));
			pagamento.setCausaleRevoca((String) record.get(i++));
			pagamento.setDatiRevoca((String) record.get(i++));
			if(record.get(i) != null) {
				pagamento.setImportoRevocato(new BigDecimal((Double) record.get(i++)));
			} else {
				i++;
			}
			pagamento.setEsitoRevoca((String) record.get(i++));
			pagamento.setDatiEsitoRevoca((String) record.get(i++));
	
			pagamento.setId((Long) record.get(i++));
			if(record.get(i) != null) {
				pagamento.setIdRpt((Long) record.get(i++));
			} else {
				i++;
			}
			if(record.get(i) != null) {
				pagamento.setIdSingoloVersamento((Long) record.get(i++));
			} else {
				i++;
			}
			if(record.get(i) != null) {
				pagamento.setIdRr((Long) record.get(i++));
			} else {
				i++;
			}
			if(record.get(i) != null) {
				existsIncasso = true;
				pagamento.setIdIncasso((Long) record.get(i++));
			} else {
				i++;
			}
			
//			pagamento.setIbanAccredito((String) record.get(i++));
			pagamento.setCodDominio((String) record.get(i++));
			pagamento.setIuv((String) record.get(i++));
			if(record.get(i) != null) {
				pagamento.setStato(Stato.valueOf((String) record.get(i++)));
			} else {
				i++;
			}
			
			rp.setPagamento(pagamento);
		} else {
			i+=22;
		}
		
		
		if(existsVersamento) {
			it.govpay.bd.model.SingoloVersamento singoloVersamento = new it.govpay.bd.model.SingoloVersamento();
			singoloVersamento.setCodSingoloVersamentoEnte((String) record.get(i++));
			
			singoloVersamento.setStatoSingoloVersamento(StatoSingoloVersamento.valueOf((String) record.get(i++)));
			singoloVersamento.setImportoSingoloVersamento(new BigDecimal((Double) record.get(i++)));
			if(record.get(i) != null) {
				singoloVersamento.setTipoBollo(TipoBollo.toEnum((String) record.get(i++)));
			} else {
				i++;
			}
			singoloVersamento.setHashDocumento((String) record.get(i++));
			singoloVersamento.setProvinciaResidenza((String) record.get(i++));
			if(record.get(i) != null) {
				singoloVersamento.setTipoContabilita(TipoContabilita.toEnum((String) record.get(i++)));
			} else {
				i++;
			}
			singoloVersamento.setCodContabilita((String) record.get(i++));
			singoloVersamento.setDatiAllegati((String) record.get(i++));
			
			singoloVersamento.setId((Long) record.get(i++));
			singoloVersamento.setIdVersamento((Long) record.get(i++));
			singoloVersamento.setIdTributo((Long) record.get(i++));
			singoloVersamento.setIdIbanAccredito(((Long) record.get(i++)));
			rp.setSingoloVersamento(singoloVersamento);
		} else {
			i+= 13;
		}
		
		rp.setTipo(((String) record.get(i++)));
		
		if(existsIncasso) {
			it.govpay.bd.model.Incasso incasso = new it.govpay.bd.model.Incasso();
			incasso.setId(rp.getPagamento().getIdIncasso()); 
			
			if(record.get(i) != null) {
				incasso.setDataIncasso((Date) record.get(i++)); 
			}
			
			rp.setIncasso(incasso);
		}
		
		return rp;
	}

	
}
