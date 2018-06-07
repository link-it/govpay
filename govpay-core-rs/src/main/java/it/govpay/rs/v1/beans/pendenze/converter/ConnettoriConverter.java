package it.govpay.rs.v1.beans.pendenze.converter;

import org.openspcoop2.generic_project.exception.ServiceException;

import it.govpay.core.rs.v1.beans.Connector;
import it.govpay.core.rs.v1.beans.base.Connector.VersioneApiEnum;
import it.govpay.core.rs.v1.beans.base.TipoAutenticazione.TipoEnum;
import it.govpay.model.Connettore;
import it.govpay.model.Connettore.EnumAuthType;
import it.govpay.model.Connettore.EnumSslType;
import it.govpay.model.Versionabile.Versione;

public class ConnettoriConverter {
	
	public static Connettore getConnettore(it.govpay.core.rs.v1.beans.base.Connector connector) throws ServiceException {
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
		if(connector.getVersioneApi() != null)
			connettore.setVersione(Versione.toEnum(connector.getVersioneApi().toString()));
		
		return connettore;
	}

	public static Connector toRsModel(it.govpay.model.Connettore connettore) throws ServiceException {
		Connector rsModel = new Connector();
		if(!connettore.getTipoAutenticazione().equals(EnumAuthType.NONE))
			rsModel.setAuth(toTipoAutenticazioneRsModel(connettore));
		rsModel.setUrl(connettore.getUrl());
		if(connettore.getVersione() != null)
			rsModel.setVersioneApi(VersioneApiEnum.fromValue(connettore.getVersione().getApiLabel()));
		
		return rsModel;
	}
	
	public static it.govpay.core.rs.v1.beans.TipoAutenticazione toTipoAutenticazioneRsModel(it.govpay.model.Connettore connettore) {
		it.govpay.core.rs.v1.beans.TipoAutenticazione rsModel = new it.govpay.core.rs.v1.beans.TipoAutenticazione();
		
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
