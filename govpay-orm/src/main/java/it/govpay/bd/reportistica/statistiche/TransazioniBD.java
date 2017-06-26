package it.govpay.bd.reportistica.statistiche;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.log4j.Logger;
import org.openspcoop2.generic_project.exception.NotFoundException;
import org.openspcoop2.generic_project.exception.NotImplementedException;
import org.openspcoop2.generic_project.exception.ServiceException;

import it.govpay.bd.BasicBD;
import it.govpay.bd.nativequeries.NativeQueries;
import it.govpay.bd.reportistica.statistiche.filters.TransazioniFilter;
import it.govpay.orm.dao.jdbc.JDBCServiceManager;

public class TransazioniBD extends BasicBD {

	public class DistribuzioneEsiti {
		private Date data;
		private long eseguiti;
		private long errori;
		private long in_corso;

		public DistribuzioneEsiti(Date data, Long eseguiti, Long errori, Long in_corso) {
			this.data = data;
			this.eseguiti = eseguiti;
			this.errori = errori;
			this.in_corso = in_corso;
		}

		public Date getData() {
			return data;
		}

		public long getEseguiti() {
			return eseguiti;
		}

		public long getErrori() {
			return errori;
		}

		public long getIn_corso() {
			return in_corso;
		}
	}
	
	public class DistribuzionePsp {
		private String psp;
		private long transazioni;
		private double percentuale;

		public DistribuzionePsp(String psp, long transazioni) {
			this.psp = psp;
			this.transazioni = transazioni;
		}

		public String getPsp() {
			return psp;
		}

		public long getTransazioni() {
			return transazioni;
		}

		public double getPercentuale() {
			return percentuale;
		}

		public void setPercentuale(double percentuale) {
			this.percentuale = percentuale;
		}

	}


	public TransazioniBD(BasicBD basicBD) {
		super(basicBD);
	}
	
	public TransazioniFilter newFilter() throws ServiceException {
		return new TransazioniFilter(this.getRptService());
	}

	public List<DistribuzioneEsiti> getDistribuzionePsp(TipoIntervallo tipoIntervallo, Date data, int limit, TransazioniFilter filtro) throws ServiceException {
		try {
			List<Class<?>> lstReturnType = new ArrayList<Class<?>>();

			lstReturnType.add(Date.class);
			lstReturnType.add(Long.class);
			lstReturnType.add(Long.class);
			lstReturnType.add(Long.class);

			String nativeQueryString = NativeQueries.getInstance().getStatisticheTransazioniPerEsitoQuery(tipoIntervallo, data, limit, filtro);
			Logger.getLogger(JDBCServiceManager.class).debug(nativeQueryString);

			Object[] array = NativeQueries.getInstance().getStatisticheTransazioniPerEsitoValues(tipoIntervallo, data, limit, filtro);
			Logger.getLogger(JDBCServiceManager.class).debug("Params: ");
			for(Object obj: array) {
				Logger.getLogger(JDBCServiceManager.class).debug(obj);
			}

			List<List<Object>> lstRecords = this.getRptService().nativeQuery(nativeQueryString, lstReturnType, array);

			List<DistribuzioneEsiti> distribuzioni = new ArrayList<DistribuzioneEsiti>();

			for(List<Object> record: lstRecords) {
				distribuzioni.add(new DistribuzioneEsiti((Date) record.get(0), (Long) record.get(1), (Long) record.get(2), (Long) record.get(3)));
			}
			return distribuzioni;
		} catch (NotImplementedException e) {
			throw new ServiceException(e);
		} catch (NotFoundException e) {
			return new ArrayList<DistribuzioneEsiti>();
		}
	}
	
	public List<DistribuzionePsp> getDistribuzioneEsiti(TipoIntervallo tipoIntervallo, Date data, double soglia, TransazioniFilter filtro) throws ServiceException {
		try {
			List<Class<?>> lstReturnType = new ArrayList<Class<?>>();

			lstReturnType.add(String.class);
			lstReturnType.add(Long.class);

			String nativeQueryString = NativeQueries.getInstance().getStatisticheTransazioniPerPspQuery(tipoIntervallo, data, filtro);
			Logger.getLogger(JDBCServiceManager.class).debug(nativeQueryString);

			Object[] array = NativeQueries.getInstance().getStatisticheTransazioniPerPspValues(tipoIntervallo, data, filtro);
			Logger.getLogger(JDBCServiceManager.class).debug("Params: ");
			for(Object obj: array) {
				Logger.getLogger(JDBCServiceManager.class).debug(obj);
			}

			List<List<Object>> lstRecords = this.getRptService().nativeQuery(nativeQueryString, lstReturnType, array);

			List<DistribuzionePsp> distribuzioniTmp = new ArrayList<DistribuzionePsp>();
			List<DistribuzionePsp> distribuzioniFinal = new ArrayList<DistribuzionePsp>();

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
				
				double percentuale = distribuzione.getTransazioni() / totale;
				if(percentuale >= soglia) {
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
			return new ArrayList<DistribuzionePsp>();
		}
	}
}
