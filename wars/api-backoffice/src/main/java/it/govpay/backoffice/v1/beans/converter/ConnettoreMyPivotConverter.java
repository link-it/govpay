package it.govpay.backoffice.v1.beans.converter;

import org.openspcoop2.generic_project.exception.ServiceException;

import it.govpay.backoffice.v1.beans.ConnettoreNotificaPagamenti;
import it.govpay.backoffice.v1.beans.TipoAutenticazione.TipoEnum;
import it.govpay.model.ConnettoreMyPivot;
import it.govpay.model.Connettore.EnumAuthType;
import it.govpay.model.Connettore.EnumSslType;

public class ConnettoreMyPivotConverter {
	
	public static ConnettoreMyPivot getConnettore(it.govpay.backoffice.v1.beans.ConnettoreNotificaPagamenti connector) throws ServiceException {
		ConnettoreMyPivot connettore = new ConnettoreMyPivot();
		
		if(connector.getAuth() != null) {
			connettore.setHttpUser(connector.getAuth().getUsername());
			connettore.setHttpPassw(connector.getAuth().getPassword());
			connettore.setSslKsLocation(connector.getAuth().getKsLocation());
			connettore.setSslTsLocation(connector.getAuth().getTsLocation());
			connettore.setSslKsPasswd(connector.getAuth().getKsPassword());
			connettore.setSslTsPasswd(connector.getAuth().getTsPassword());
			
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
		
		connettore.setUrl(connector.getUrlRPT());
		connettore.setUrlServiziAvvisatura(connector.getUrlAvvisatura()); 
		
		return connettore;
	}

	public static ConnettoreNotificaPagamenti toRsModel(it.govpay.model.ConnettoreMyPivot connettore) throws ServiceException {
		ConnettoreNotificaPagamenti rsModel = new ConnettoreNotificaPagamenti();
		if(!connettore.getTipoAutenticazione().equals(EnumAuthType.NONE))
			rsModel.setAuth(toTipoAutenticazioneRsModel(connettore));
		rsModel.setUrlRPT(connettore.getUrl());
		rsModel.setUrlAvvisatura(connettore.getUrlServiziAvvisatura());
		
		return rsModel;
	}
	
	public static it.govpay.backoffice.v1.beans.TipoAutenticazione toTipoAutenticazioneRsModel(it.govpay.model.ConnettoreMyPivot connettore) {
		it.govpay.backoffice.v1.beans.TipoAutenticazione rsModel = new it.govpay.backoffice.v1.beans.TipoAutenticazione();
		
		rsModel.username(connettore.getHttpUser())
		.password(connettore.getHttpPassw())
		.ksLocation(connettore.getSslKsLocation())
		.ksPassword(connettore.getSslKsPasswd())
		.tsLocation(connettore.getSslTsLocation())
		.tsPassword(connettore.getSslTsPasswd());
		
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
