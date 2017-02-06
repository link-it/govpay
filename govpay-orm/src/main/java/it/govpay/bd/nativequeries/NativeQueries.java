package it.govpay.bd.nativequeries;

import org.openspcoop2.generic_project.exception.ServiceException;

import it.govpay.bd.GovpayConfig;

public abstract class NativeQueries {
	
	public abstract String getEstrattiContoQuery();
	public abstract String getEstrattiContoCountQuery();
	
	public abstract String getRendicontazionePagamentoQuery();
	public abstract String getRendicontazionePagamentoCountQuery();
	
	public abstract String getFrQuery();
	public abstract String getFrCountQuery();
	
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
