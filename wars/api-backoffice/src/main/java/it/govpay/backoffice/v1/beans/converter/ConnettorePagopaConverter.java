package it.govpay.backoffice.v1.beans.converter;

import org.openspcoop2.generic_project.exception.ServiceException;

import it.govpay.backoffice.v1.beans.ConnettorePagopa;
import it.govpay.backoffice.v1.beans.TipoAutenticazione.TipoEnum;
import it.govpay.model.Connettore;
import it.govpay.model.Connettore.EnumAuthType;
import it.govpay.model.Connettore.EnumSslType;

public class ConnettorePagopaConverter {
	
	public static Connettore getConnettore(it.govpay.backoffice.v1.beans.ConnettorePagopa connector) throws ServiceException {
		Connettore connettore = new Connettore();
		
		if(connector.getAuth() != null) {
			connettore.setHttpUser(connector.getAuth().getUsername());
			connettore.setHttpPassw(connector.getAuth().getPassword());
			connettore.setSslKsLocation(connector.getAuth().getKsLocation());
			connettore.setSslTsLocation(connector.getAuth().getTsLocation());
			connettore.setSslKsPasswd(connector.getAuth().getKsPassword());
			connettore.setSslTsPasswd(connector.getAuth().getTsPassword());
			
			if(connector.getAuth().getTipo() != null) {
				connettore.setTipoAutenticazione(EnumAuthType.SSL);
				connettore.setTipoSsl(EnumSslType.valueOf(connector.getAuth().getTipo().toString()));
			} else {
				connettore.setTipoAutenticazione(EnumAuthType.HTTPBasic);
			}
		} else {
			connettore.setTipoAutenticazione(EnumAuthType.NONE);
		}	
		
		connettore.setUrl(connector.getUrl());
		
		return connettore;
	}

	public static ConnettorePagopa toRsModel(it.govpay.model.Connettore connettore) throws ServiceException {
		ConnettorePagopa rsModel = new ConnettorePagopa();
		if(!connettore.getTipoAutenticazione().equals(EnumAuthType.NONE))
			rsModel.setAuth(toTipoAutenticazioneRsModel(connettore));
		rsModel.setUrl(connettore.getUrl());
		
		return rsModel;
	}
	
	public static it.govpay.backoffice.v1.beans.TipoAutenticazione toTipoAutenticazioneRsModel(it.govpay.model.Connettore connettore) {
		it.govpay.backoffice.v1.beans.TipoAutenticazione rsModel = new it.govpay.backoffice.v1.beans.TipoAutenticazione();
		
		rsModel.username(connettore.getHttpUser())
		.password(connettore.getHttpPassw())
		.ksLocation(connettore.getSslKsLocation())
		.ksPassword(connettore.getSslKsPasswd())
		.tsLocation(connettore.getSslTsLocation())
		.tsPassword(connettore.getSslTsPasswd());
		
		if(connettore.getSslType() != null)
			rsModel.tipo(TipoEnum.fromValue(connettore.getSslType().toString()));
		
		return rsModel;
	}
}
