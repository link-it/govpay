package it.govpay.backoffice.v1.beans.converter;

import org.openspcoop2.generic_project.exception.ServiceException;

import it.govpay.backoffice.v1.beans.Connector;
import it.govpay.backoffice.v1.beans.TipoAutenticazione.TipoEnum;
import it.govpay.backoffice.v1.beans.Connector.VersioneApiEnum;
import it.govpay.model.Connettore;
import it.govpay.model.Connettore.EnumAuthType;
import it.govpay.model.Connettore.EnumSslType;
import it.govpay.model.Versionabile;

public class ConnettoriConverter {
	
	public static Connettore getConnettore(it.govpay.backoffice.v1.beans.Connector connector) throws ServiceException {
		Connettore connettore = new Connettore();
		
		if(connector.getAuth() != null) {
			connettore.setHttpUser(connector.getAuth().getUsername());
			connettore.setHttpPassw(connector.getAuth().getPassword());
			connettore.setSslKsLocation(connector.getAuth().getKsLocation());
			connettore.setSslTsLocation(connector.getAuth().getTsLocation());
			connettore.setSslKsPasswd(connector.getAuth().getKsPassword());
			connettore.setSslTsPasswd(connector.getAuth().getTsPassword());
			connettore.setSslTsType(connector.getAuth().getTsType());
			connettore.setSslType(connector.getAuth().getSslType());
			connettore.setSslKsType(connector.getAuth().getKsType());
			connettore.setSslPKeyPasswd(connector.getAuth().getKsPKeyPasswd());
			
			if(connector.getAuth().getTipo() != null) {
				connettore.setTipoAutenticazione(EnumAuthType.SSL);
				if(connector.getAuth().getTipo() != null) {
					switch (connector.getAuth().getTipo()) {
					case CLIENT:
						connettore.setTipoSsl(EnumSslType.CLIENT);
						break;
					case SERVER:
					default:
						connettore.setTipoSsl(EnumSslType.SERVER);
						break;
					}
				}
			} else {
				connettore.setTipoAutenticazione(EnumAuthType.HTTPBasic);
			}
		} else {
			connettore.setTipoAutenticazione(EnumAuthType.NONE);
		}	
		
		connettore.setUrl(connector.getUrl());
		if(connector.getVersioneApi() != null)
			connettore.setVersione(Versionabile.Versione.toEnum(VersioneApiEnum.fromValue(connector.getVersioneApi()).toNameString()));
		
		return connettore;
	}

	public static Connector toRsModel(it.govpay.model.Connettore connettore) throws ServiceException {
		Connector rsModel = new Connector();
		if(connettore.getTipoAutenticazione()!=null && !connettore.getTipoAutenticazione().equals(EnumAuthType.NONE))
			rsModel.setAuth(toTipoAutenticazioneRsModel(connettore));
		rsModel.setUrl(connettore.getUrl());
		if(connettore.getVersione() != null)
			rsModel.setVersioneApi(VersioneApiEnum.fromName(connettore.getVersione().getApiLabel()).toString());
		
		return rsModel;
	}
	
	public static it.govpay.backoffice.v1.beans.TipoAutenticazione toTipoAutenticazioneRsModel(it.govpay.model.Connettore connettore) {
		it.govpay.backoffice.v1.beans.TipoAutenticazione rsModel = new it.govpay.backoffice.v1.beans.TipoAutenticazione();
		
		rsModel.username(connettore.getHttpUser())
		.password(connettore.getHttpPassw())
		.ksLocation(connettore.getSslKsLocation())
		.ksPassword(connettore.getSslKsPasswd())
		.tsLocation(connettore.getSslTsLocation())
		.tsPassword(connettore.getSslTsPasswd())
		.tsType(connettore.getSslTsType())
		.sslType(connettore.getSslType())
		.ksType(connettore.getSslKsType())
		.ksPKeyPasswd(connettore.getSslPKeyPasswd());
		
		if(connettore.getTipoSsl() != null) {
			switch (connettore.getTipoSsl() ) {
			case CLIENT:
				rsModel.setTipo(TipoEnum.CLIENT);
				break;
			case SERVER:
			default:
				rsModel.setTipo(TipoEnum.SERVER);
				break;
			}
		}
		
//		if(connettore.getSslType() != null)
//			rsModel.tipo(TipoEnum.fromValue(connettore.getSslType().toString()));
		
		return rsModel;
	}
}
