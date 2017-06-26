package it.govpay.bd.nativequeries;

import java.util.Date;

import org.openspcoop2.generic_project.exception.ServiceException;

import it.govpay.bd.GovpayConfig;
import it.govpay.bd.reportistica.statistiche.TipoIntervallo;
import it.govpay.bd.reportistica.statistiche.filters.TransazioniFilter;

public abstract class NativeQueries {
	
	public abstract String getEstrattiContoQuery();
	public abstract String getEstrattiContoCountQuery();
	
	public abstract String getRendicontazionePagamentoQuery();
	public abstract String getRendicontazionePagamentoCountQuery();
	
	public abstract String getPagamentoRendicontazioneQuery();
	public abstract String getPagamentoRendicontazioneCountQuery();
	
	public abstract String getFrQuery();
	public abstract String getFrCountQuery();
	
	public abstract String getStatisticheTransazioniPerEsitoQuery(TipoIntervallo tipoIntervallo, Date data, int limit, TransazioniFilter filtro);
	public abstract Object[] getStatisticheTransazioniPerEsitoValues(TipoIntervallo tipoIntervallo, Date data, int limit, TransazioniFilter filtro);
	
	public abstract String getStatisticheTransazioniPerPspQuery(TipoIntervallo tipoIntervallo, Date data, TransazioniFilter filtro);
	public abstract Object[] getStatisticheTransazioniPerPspValues(TipoIntervallo tipoIntervallo, Date data, TransazioniFilter filtro);
	
	public static NativeQueries getInstance() throws ServiceException {
		
		if(GovpayConfig.getInstance().getDatabaseType().equals("postgresql")) {
			return new PostgresNativeQueries();
		}
		if(GovpayConfig.getInstance().getDatabaseType().equals("mysql")) {
			return new MysqlNativeQueries();
		}
		if(GovpayConfig.getInstance().getDatabaseType().equals("oracle")) {
			return new OracleNativeQueries();
		}
		
		throw new ServiceException("Tipo database " + GovpayConfig.getInstance().getDatabaseType() + " non supportato");
	}
}
