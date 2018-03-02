package it.govpay.rs.v1.beans.converter;

import org.openspcoop2.generic_project.exception.ServiceException;

import it.govpay.model.Connettore;
import it.govpay.model.Connettore.EnumAuthType;
import it.govpay.model.Connettore.EnumSslType;
import it.govpay.model.Versionabile.Versione;

public class ConnettoriConverter {
	
	public static Connettore getConnettore(it.govpay.rs.v1.beans.base.Connector connector) throws ServiceException {
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

}
