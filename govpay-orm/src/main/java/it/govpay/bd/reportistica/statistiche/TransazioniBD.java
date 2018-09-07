package it.govpay.bd.reportistica.statistiche;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.openspcoop2.generic_project.exception.NotFoundException;
import org.openspcoop2.generic_project.exception.NotImplementedException;
import org.openspcoop2.generic_project.exception.ServiceException;
import org.openspcoop2.utils.LoggerWrapperFactory;

import it.govpay.bd.BasicBD;
import it.govpay.bd.nativequeries.NativeQueries;
import it.govpay.bd.reportistica.statistiche.filters.TransazioniFilter;
import it.govpay.model.reportistica.statistiche.DistribuzioneEsiti;
import it.govpay.model.reportistica.statistiche.DistribuzionePsp;
import it.govpay.orm.dao.jdbc.JDBCServiceManager;

public class TransazioniBD extends BasicBD {

	
	public TransazioniBD(BasicBD basicBD) {
		super(basicBD);
	}
	
	public TransazioniFilter newFilter() throws ServiceException {
		return new TransazioniFilter(this.getRptService());
	}

	public List<DistribuzioneEsiti> getDistribuzioneEsiti(TransazioniFilter filtro) throws ServiceException {
		try {
			List<Class<?>> lstReturnType = new ArrayList<>();

			lstReturnType.add(Date.class);
			lstReturnType.add(Long.class);
			lstReturnType.add(Long.class);
			lstReturnType.add(Long.class);

			String nativeQueryString = NativeQueries.getInstance().getStatisticheTransazioniPerEsitoQuery(filtro.getTipoIntervallo(), filtro.getData(), filtro.getLimit(), filtro);
			LoggerWrapperFactory.getLogger(JDBCServiceManager.class).debug(nativeQueryString);

			Object[] array = NativeQueries.getInstance().getStatisticheTransazioniPerEsitoValues(filtro.getTipoIntervallo(), filtro.getData(), filtro.getLimit(), filtro);
			LoggerWrapperFactory.getLogger(JDBCServiceManager.class).debug("Params: ");
			for(Object obj: array) {
				LoggerWrapperFactory.getLogger(JDBCServiceManager.class).debug(obj.toString());
			}

			List<List<Object>> lstRecords = this.getRptService().nativeQuery(nativeQueryString, lstReturnType, array);

			List<DistribuzioneEsiti> distribuzioni = new ArrayList<>();

			for(List<Object> record: lstRecords) {
				distribuzioni.add(new DistribuzioneEsiti((Date) record.get(0), (Long) record.get(1), (Long) record.get(2), (Long) record.get(3)));
			}
			return distribuzioni;
		} catch (NotImplementedException e) {
			throw new ServiceException(e);
		} catch (NotFoundException e) {
			return new ArrayList<>();
		}
	}
	
	public List<DistribuzionePsp> getDistribuzionePsp(TransazioniFilter filtro) throws ServiceException {
		try {
			List<Class<?>> lstReturnType = new ArrayList<>();

			lstReturnType.add(String.class);
			lstReturnType.add(Long.class);

			String nativeQueryString = NativeQueries.getInstance().getStatisticheTransazioniPerPspQuery(filtro.getTipoIntervallo(), filtro.getData(), filtro);
			LoggerWrapperFactory.getLogger(JDBCServiceManager.class).debug(nativeQueryString);

			Object[] array = NativeQueries.getInstance().getStatisticheTransazioniPerPspValues(filtro.getTipoIntervallo(), filtro.getData(), filtro);
			LoggerWrapperFactory.getLogger(JDBCServiceManager.class).debug("Params: ");
			for(Object obj: array) {
				LoggerWrapperFactory.getLogger(JDBCServiceManager.class).debug(obj.toString());
			}

			List<List<Object>> lstRecords = this.getRptService().nativeQuery(nativeQueryString, lstReturnType, array);

			List<DistribuzionePsp> distribuzioniTmp = new ArrayList<>();
			List<DistribuzionePsp> distribuzioniFinal = new ArrayList<>();

			long totale = 0;
			for(List<Object> record: lstRecords) {
				distribuzioniTmp.add(new DistribuzionePsp((String) record.get(0), (Long) record.get(1)));
				totale += (Long) record.get(1);
			}
			
			// Calcolo le percentuali
			double percentualeOccupata = 0;
			long totaleOccupato = 0;
			for(int i=0; i<distribuzioniTmp.size(); i++) {
				DistribuzionePsp distribuzione = distribuzioniTmp.get(i);
				
				double percentuale = totale > 0 ? ((double) distribuzione.getTransazioni()) / totale : 0;
				if(percentuale >= filtro.getSoglia()) {
					distribuzione.setPercentuale(percentuale);
					distribuzioniFinal.add(distribuzione);
					percentualeOccupata += percentuale;
					totaleOccupato += distribuzione.getTransazioni();
				} else {
					DistribuzionePsp distribuzioneAltro = new DistribuzionePsp("Altri", totale - totaleOccupato);
					distribuzioneAltro.setPercentuale(1 - percentualeOccupata);
					distribuzioniFinal.add(distribuzioneAltro);
					break;
				}
			}
			
			return distribuzioniFinal;
		} catch (NotImplementedException e) {
			throw new ServiceException(e);
		} catch (NotFoundException e) {
			return new ArrayList<>();
		}
	}
}
