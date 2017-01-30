package it.govpay.bd.wrapper;

import it.govpay.bd.BasicBD;
import it.govpay.bd.model.Fr;
import it.govpay.bd.model.Pagamento;
import it.govpay.bd.model.RendicontazionePagamento;
import it.govpay.bd.model.SingoloVersamento;
import it.govpay.bd.model.Versamento;
import it.govpay.bd.nativequeries.NativeQueries;
import it.govpay.bd.wrapper.filters.RendicontazionePagamentoFilter;
import it.govpay.model.Anagrafica;
import it.govpay.model.Fr.StatoFr;
import it.govpay.model.Pagamento.TipoAllegato;
import it.govpay.model.Rendicontazione;
import it.govpay.model.Rendicontazione.EsitoRendicontazione;
import it.govpay.model.Rendicontazione.StatoRendicontazione;
import it.govpay.model.SingoloVersamento.StatoSingoloVersamento;
import it.govpay.model.SingoloVersamento.TipoBollo;
import it.govpay.model.Tributo.TipoContabilta;
import it.govpay.model.Versamento.StatoVersamento;

import java.io.UnsupportedEncodingException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.openspcoop2.generic_project.exception.NotFoundException;
import org.openspcoop2.generic_project.exception.NotImplementedException;
import org.openspcoop2.generic_project.exception.ServiceException;

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
			List<List<Object>> count = this.getRendicontazionePagamentoServiceSearch().nativeQuery(filter.getSQLFilterString(NativeQueries.getInstance().getRendicontazionePagamentoCountQuery()), lstReturnType, filter.getFields(true).toArray(new Object[]{}));
			
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
			
			lstReturnType.add(String.class);
			lstReturnType.add(String.class);
			lstReturnType.add(String.class);
			lstReturnType.add(String.class);
			lstReturnType.add(Date.class);
			lstReturnType.add(Date.class);
			lstReturnType.add(Date.class);
			lstReturnType.add(Long.class);
			lstReturnType.add(Double.class);
			lstReturnType.add(String.class);
			lstReturnType.add(byte[].class);
			lstReturnType.add(Long.class);
			lstReturnType.add(String.class);
			lstReturnType.add(String.class);
			lstReturnType.add(String.class);
			lstReturnType.add(Double.class);
			lstReturnType.add(String.class);
			lstReturnType.add(String.class);
			lstReturnType.add(Boolean.class);
			lstReturnType.add(Date.class);
			lstReturnType.add(Date.class);
			lstReturnType.add(Date.class);
			lstReturnType.add(String.class);
			lstReturnType.add(String.class);
			lstReturnType.add(String.class);
			lstReturnType.add(String.class);
			lstReturnType.add(String.class);
			lstReturnType.add(String.class);
			lstReturnType.add(String.class);
			lstReturnType.add(String.class);
			lstReturnType.add(String.class);
			lstReturnType.add(String.class);
			lstReturnType.add(String.class);
			lstReturnType.add(String.class);
			lstReturnType.add(String.class);
			lstReturnType.add(String.class);
			lstReturnType.add(String.class);
			lstReturnType.add(Integer.class);
			lstReturnType.add(String.class);
			lstReturnType.add(Long.class);
			lstReturnType.add(Long.class);
			lstReturnType.add(Long.class);
			lstReturnType.add(String.class);
			lstReturnType.add(String.class);
			lstReturnType.add(Double.class);
			lstReturnType.add(Integer.class);
			lstReturnType.add(Date.class);
			lstReturnType.add(String.class);
			lstReturnType.add(String.class);
			lstReturnType.add(Long.class);
			lstReturnType.add(Long.class);
			lstReturnType.add(Long.class);
			lstReturnType.add(Double.class);
			lstReturnType.add(Date.class);
			lstReturnType.add(String.class);
			lstReturnType.add(Date.class);
			lstReturnType.add(Double.class);
			lstReturnType.add(String.class);
			lstReturnType.add(byte[].class);
			lstReturnType.add(Date.class);
			lstReturnType.add(String.class);
			lstReturnType.add(String.class);
			lstReturnType.add(Double.class);
			lstReturnType.add(String.class);
			lstReturnType.add(String.class);
			lstReturnType.add(Long.class);
			lstReturnType.add(Long.class);
			lstReturnType.add(Long.class);
			lstReturnType.add(Long.class);
			lstReturnType.add(String.class);
			lstReturnType.add(String.class);
			lstReturnType.add(String.class);
			lstReturnType.add(String.class);
			lstReturnType.add(String.class);
			lstReturnType.add(Double.class);
			lstReturnType.add(String.class);
			lstReturnType.add(String.class);
			lstReturnType.add(String.class);
			lstReturnType.add(String.class);
			lstReturnType.add(String.class);
			lstReturnType.add(String.class);
			lstReturnType.add(Long.class);
			lstReturnType.add(Long.class);
			lstReturnType.add(Long.class);
			lstReturnType.add(Long.class);
			lstReturnType.add(String.class);

			List<List<Object>> lstRecords = this.getRendicontazionePagamentoServiceSearch().nativeQuery(filter.getSQLFilterString(NativeQueries.getInstance().getRendicontazionePagamentoQuery()), lstReturnType, filter.getFields(false).toArray(new Object[]{}));
			List<RendicontazionePagamento> lstNonFiltrata = new ArrayList<RendicontazionePagamento>();

			for(List<Object> record: lstRecords) {
				lstNonFiltrata.add(getRendicontazionePagamento(record));
			}
			
			//TODO filtrare lista applicativamente
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
		fr.setImportoTotalePagamenti((Double)record.get(i++));
		fr.setCodBicRiversamento((String)record.get(i++));
		fr.setXml((byte[])record.get(i++));
		fr.setId((Long)record.get(i++));
		fr.setCodPsp((String)record.get(i++));
		fr.setCodDominio((String)record.get(i++));

		rp.setFr(fr);

		
		Versamento versamento = new Versamento();
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
		versamento.setCodAnnoTributario((Integer) record.get(i++));
		versamento.setCodBundlekey((String) record.get(i++));
		versamento.setId((Long) record.get(i++));
		versamento.setIdUo((Long) record.get(i++));
		versamento.setIdApplicazione((Long) record.get(i++));

		rp.setVersamento(versamento);
		
		Rendicontazione rendicontazione = new Rendicontazione();
		rendicontazione.setIuv((String) record.get(i++));
		rendicontazione.setIur((String) record.get(i++));
		rendicontazione.setImportoPagato(new BigDecimal((Double) record.get(i++)));
		rendicontazione.setEsito(EsitoRendicontazione.toEnum((Integer) record.get(i++)));
		rendicontazione.setData((Date) record.get(i++));
		rendicontazione.setStato(StatoRendicontazione.valueOf((String) record.get(i++)));
		rendicontazione.getAnomalie().addAll(rendicontazione.unmarshall((String) record.get(i++)));
		rendicontazione.setId((Long) record.get(i++));
		rendicontazione.setIdFr((Long) record.get(i++));
		rendicontazione.setIdPagamento((Long) record.get(i++));
		rp.setRendicontazione(rendicontazione);
		
		
		Pagamento pagamento = new Pagamento();
		pagamento.setImportoPagato(new BigDecimal((Double) record.get(i++)));
		pagamento.setDataAcquisizione((Date) record.get(i++));
		pagamento.setIur((String) record.get(i++));
		pagamento.setDataPagamento((Date) record.get(i++));
		pagamento.setCommissioniPsp(new BigDecimal((Double) record.get(i++)));
		pagamento.setTipoAllegato(TipoAllegato.valueOf((String) record.get(i++)));
		pagamento.setAllegato((byte[]) record.get(i++));
		pagamento.setDataAcquisizioneRevoca((Date) record.get(i++));
		pagamento.setCausaleRevoca((String) record.get(i++));
		pagamento.setDatiRevoca((String) record.get(i++));
		pagamento.setImportoRevocato(new BigDecimal((Double) record.get(i++)));
		pagamento.setEsitoRevoca((String) record.get(i++));
		pagamento.setDatiEsitoRevoca((String) record.get(i++));
		pagamento.setId((Long) record.get(i++));
		pagamento.setIdRpt((Long) record.get(i++));
		pagamento.setIdSingoloVersamento((Long) record.get(i++));
		pagamento.setIdRr((Long) record.get(i++));
		pagamento.setIbanAccredito((String) record.get(i++));
		pagamento.setCodDominio((String) record.get(i++));
		pagamento.setIuv((String) record.get(i++));
		rp.setPagamento(pagamento);
		
		
		SingoloVersamento singoloVersamento = new SingoloVersamento();
		singoloVersamento.setCodSingoloVersamentoEnte((String) record.get(i++));
		singoloVersamento.setStatoSingoloVersamento(StatoSingoloVersamento.valueOf((String) record.get(i++)));
		singoloVersamento.setImportoSingoloVersamento(new BigDecimal((Double) record.get(i++)));
		singoloVersamento.setTipoBollo(TipoBollo.toEnum((String) record.get(i++)));
		singoloVersamento.setHashDocumento((String) record.get(i++));
		singoloVersamento.setProvinciaResidenza((String) record.get(i++));
		singoloVersamento.setTipoContabilita(TipoContabilta.toEnum((String) record.get(i++)));
		singoloVersamento.setCodContabilita((String) record.get(i++));
		singoloVersamento.setNote((String) record.get(i++));
		singoloVersamento.setId((Long) record.get(i++));
		singoloVersamento.setIdVersamento((Long) record.get(i++));
		singoloVersamento.setIdTributo((Long) record.get(i++));
		singoloVersamento.setIdIbanAccredito(((Long) record.get(i++)));
		rp.setSingoloVersamento(singoloVersamento);
		
		rp.setTipo(((String) record.get(i++)));
		
		return rp;
	}

	
}
