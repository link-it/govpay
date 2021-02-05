package it.govpay.backoffice.v1.beans.converter;

import java.util.ArrayList;
import java.util.List;

import org.openspcoop2.generic_project.exception.NotFoundException;
import org.openspcoop2.generic_project.exception.ServiceException;
import org.openspcoop2.utils.service.context.ContextThreadLocal;
import org.springframework.security.core.Authentication;

import it.govpay.backoffice.v1.beans.ConnettoreNotificaPagamenti;
import it.govpay.backoffice.v1.beans.ConnettoreNotificaPagamenti.TipoConnettoreEnum;
import it.govpay.backoffice.v1.beans.ConnettoreNotificaPagamenti.VersioneCsvEnum;
import it.govpay.backoffice.v1.beans.TipoAutenticazione.TipoEnum;
import it.govpay.backoffice.v1.beans.TipoPendenzaProfiloIndex;
import it.govpay.backoffice.v1.controllers.ApplicazioniController;
import it.govpay.bd.BDConfigWrapper;
import it.govpay.bd.anagrafica.AnagraficaManager;
import it.govpay.model.ConnettoreMyPivot;
import it.govpay.core.autorizzazione.AuthorizationManager;
import it.govpay.core.exceptions.NotAuthorizedException;
import it.govpay.model.Connettore.EnumAuthType;
import it.govpay.model.Connettore.EnumSslType;
import it.govpay.model.ConnettoreMyPivot.Tipo;
import it.govpay.model.ConnettoreMyPivot.TipoConnettore;
import it.govpay.model.TipoVersamento;

public class ConnettoreMyPivotConverter {
	
	public static ConnettoreMyPivot getConnettore(it.govpay.backoffice.v1.beans.ConnettoreNotificaPagamenti connector, Authentication user) throws ServiceException,NotAuthorizedException {
		ConnettoreMyPivot connettore = new ConnettoreMyPivot();
		
		connettore.setAbilitato(connector.Abilitato());
		
		if(connector.Abilitato()) {
			connettore.setCodiceIPA(connector.getCodiceIPA()); 
			connettore.setTipoTracciato(Tipo.MYPIVOT.name());
			
			connettore.setVersioneCsv(connector.getVersioneCsv().toString());
			
			boolean appAuthTipiPendenzaAll = false;
			if(connector.getTipiPendenza() != null) {
				List<String> idTipiVersamento = new ArrayList<>();
				
				for (TipoPendenzaProfiloIndex id : connector.getTipiPendenza()) {
					if(id.getIdTipoPendenza().equals(ApplicazioniController.AUTORIZZA_TIPI_PENDENZA_STAR)) {
						appAuthTipiPendenzaAll = true;
					} else{
						idTipiVersamento.add(id.getIdTipoPendenza());
					}
				}
				
				if(appAuthTipiPendenzaAll) {
					List<String> tipiVersamentoAutorizzati = AuthorizationManager.getTipiVersamentoAutorizzati(user);
					
					if(tipiVersamentoAutorizzati == null)
						throw AuthorizationManager.toNotAuthorizedExceptionNessunTipoVersamentoAutorizzato(user);
					
					if(tipiVersamentoAutorizzati.size() > 0) {
						throw AuthorizationManager.toNotAuthorizedException(user, "l'utenza non e' associata a tutti i tipi pendenza, non puo' dunque autorizzare l'applicazione a tutti i tipi pendenza o abilitare l'autodeterminazione dei tipi pendenza");
					}
					
					connettore.setTipiPendenza(new ArrayList<>());				
				} else {
					connettore.setTipiPendenza(idTipiVersamento);
				}
			}
			
			switch (connector.getTipoConnettore()) {
			case EMAIL:
				connettore.setTipoConnettore(TipoConnettore.EMAIL);
				connettore.setEmailIndirizzo(connector.getEmailIndirizzo());
				break;
			case FILESYSTEM:
				connettore.setTipoConnettore(TipoConnettore.FILE_SYSTEM);
				connettore.setFileSystemPath(connector.getFileSystemPath());
				break;
			case WEBSERVICE:
				connettore.setTipoConnettore(TipoConnettore.WEB_SERVICE);
				if(connector.getWebServiceAuth() != null) {
					connettore.setHttpUser(connector.getWebServiceAuth().getUsername());
					connettore.setHttpPassw(connector.getWebServiceAuth().getPassword());
					connettore.setSslKsLocation(connector.getWebServiceAuth().getKsLocation());
					connettore.setSslTsLocation(connector.getWebServiceAuth().getTsLocation());
					connettore.setSslKsPasswd(connector.getWebServiceAuth().getKsPassword());
					connettore.setSslTsPasswd(connector.getWebServiceAuth().getTsPassword());
					
					if(connector.getWebServiceAuth().getTipo() != null) {
						connettore.setTipoAutenticazione(EnumAuthType.SSL);
						if(connector.getWebServiceAuth().getTipo() != null) {
							switch (connector.getWebServiceAuth().getTipo()) {
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
				
				connettore.setUrl(connector.getWebServiceUrl());
				break;
			}
		}
		
		return connettore;
	}

	public static ConnettoreNotificaPagamenti toRsModel(it.govpay.model.ConnettoreMyPivot connettore) throws ServiceException {
		ConnettoreNotificaPagamenti rsModel = new ConnettoreNotificaPagamenti();
		BDConfigWrapper configWrapper = new BDConfigWrapper(ContextThreadLocal.get().getTransactionId(), true);
		
		rsModel.setAbilitato(connettore.isAbilitato());
		if(connettore.isAbilitato()) {
			rsModel.setCodiceIPA(connettore.getCodiceIPA());
			rsModel.setVersioneCsv(VersioneCsvEnum.fromValue(connettore.getVersioneCsv()));
			
			switch (connettore.getTipoConnettore()) {
			case EMAIL:
				rsModel.setTipoConnettore(TipoConnettoreEnum.EMAIL);
				rsModel.setEmailIndirizzo(connettore.getEmailIndirizzo());
				break;
			case FILE_SYSTEM:
				rsModel.setTipoConnettore(TipoConnettoreEnum.FILESYSTEM);
				rsModel.setFileSystemPath(connettore.getFileSystemPath());
				break;
			case WEB_SERVICE:
				rsModel.setTipoConnettore(TipoConnettoreEnum.WEBSERVICE);
				if(!connettore.getTipoAutenticazione().equals(EnumAuthType.NONE))
					rsModel.setWebServiceAuth(toTipoAutenticazioneRsModel(connettore));
				rsModel.setWebServiceUrl(connettore.getUrl());
				break;
			}
			
			List<TipoPendenzaProfiloIndex> idTipiPendenza = null;
			List<String> tipiPendenza = connettore.getTipiPendenza();
			if(tipiPendenza != null) {
				idTipiPendenza = new ArrayList<>();
				if(tipiPendenza.isEmpty()) {
					TipoPendenzaProfiloIndex tPI = new TipoPendenzaProfiloIndex();
					tPI.setIdTipoPendenza(ApplicazioniController.AUTORIZZA_TIPI_PENDENZA_STAR);
					tPI.setDescrizione(ApplicazioniController.AUTORIZZA_TIPI_PENDENZA_STAR_LABEL);
					idTipiPendenza.add(tPI);
				} else {
					for(String codTipoVersamento: tipiPendenza) {
						try {
							TipoPendenzaProfiloIndex tPI = new TipoPendenzaProfiloIndex();
							TipoVersamento tipoVersamento = AnagraficaManager.getTipoVersamento(configWrapper, codTipoVersamento);
							tPI.setIdTipoPendenza(tipoVersamento.getCodTipoVersamento());
							tPI.setDescrizione(tipoVersamento.getDescrizione());
							idTipiPendenza.add(tPI);
						} catch (NotFoundException e) {
						}
					}
				}
			}
			
			rsModel.setTipiPendenza(idTipiPendenza);
		}
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
