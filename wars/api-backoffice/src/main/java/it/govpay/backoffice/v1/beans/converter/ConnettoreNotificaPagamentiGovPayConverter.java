package it.govpay.backoffice.v1.beans.converter;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;

import org.openspcoop2.generic_project.exception.NotFoundException;
import org.openspcoop2.generic_project.exception.ServiceException;
import org.openspcoop2.utils.service.context.ContextThreadLocal;
import org.springframework.security.core.Authentication;

import it.govpay.backoffice.v1.beans.ConnettoreNotificaPagamentiGovPay.TipoConnettoreEnum;
import it.govpay.backoffice.v1.beans.ConnettoreNotificaPagamentiGovPay.VersioneApiEnum;
import it.govpay.backoffice.v1.beans.TipoPendenzaProfiloIndex;
import it.govpay.backoffice.v1.controllers.ApplicazioniController;
import it.govpay.bd.BDConfigWrapper;
import it.govpay.bd.anagrafica.AnagraficaManager;
import it.govpay.core.autorizzazione.AuthorizationManager;
import it.govpay.core.exceptions.NotAuthorizedException;
import it.govpay.model.ConnettoreNotificaPagamenti.Tipo;
import it.govpay.model.ConnettoreNotificaPagamenti.TipoConnettore;
import it.govpay.model.TipoVersamento;
import it.govpay.model.Versionabile;
import it.govpay.model.Connettore.EnumAuthType;
import it.govpay.model.Connettore.EnumSslType;

public class ConnettoreNotificaPagamentiGovPayConverter {
	
	public static it.govpay.model.ConnettoreNotificaPagamenti getConnettoreDTO(it.govpay.backoffice.v1.beans.ConnettoreNotificaPagamentiGovPay connector, Authentication user, Tipo tipo) throws ServiceException,NotAuthorizedException {
		it.govpay.model.ConnettoreNotificaPagamenti connettore = new it.govpay.model.ConnettoreNotificaPagamenti();
		
		connettore.setAbilitato(connector.Abilitato());
		
		if(connector.Abilitato()) {
			connettore.setTipoTracciato(tipo.name());
			connettore.setVersioneCsv(connector.getVersioneCsv());
			
//			boolean appAuthTipiPendenzaAll = false;
			if(connector.getTipiPendenza() != null) {
				List<String> idTipiVersamento = new ArrayList<>();
				
				for (Object object : connector.getTipiPendenza()) {
					if(object instanceof String) {
						String idTipoPendenza = (String) object;
						
						if(idTipoPendenza.equals(ApplicazioniController.AUTORIZZA_TIPI_PENDENZA_STAR)) {
							List<String> tipiVersamentoAutorizzati = AuthorizationManager.getTipiVersamentoAutorizzati(user);
							
							if(tipiVersamentoAutorizzati == null)
								throw AuthorizationManager.toNotAuthorizedExceptionNessunTipoVersamentoAutorizzato(user);
							
							if(tipiVersamentoAutorizzati.size() > 0) {
								throw AuthorizationManager.toNotAuthorizedException(user, "l'utenza non e' associata a tutti i tipi pendenza, non puo' dunque autorizzare l'applicazione a tutti i tipi pendenza o abilitare l'autodeterminazione dei tipi pendenza");
							}
							
//							appAuthTipiPendenzaAll = true;
							idTipiVersamento.clear();
							break;
						}
						
						idTipiVersamento.add(idTipoPendenza);
						
						
					} else if(object instanceof TipoPendenzaProfiloIndex) {
						TipoPendenzaProfiloIndex tipoPendenzaPost = (TipoPendenzaProfiloIndex) object;
						if(tipoPendenzaPost.getIdTipoPendenza().equals(ApplicazioniController.AUTORIZZA_TIPI_PENDENZA_STAR)) {
							List<String> tipiVersamentoAutorizzati = AuthorizationManager.getTipiVersamentoAutorizzati(user);
							
							if(tipiVersamentoAutorizzati == null)
								throw AuthorizationManager.toNotAuthorizedExceptionNessunTipoVersamentoAutorizzato(user);
							
							if(tipiVersamentoAutorizzati.size() > 0) {
								throw AuthorizationManager.toNotAuthorizedException(user, "l'utenza non e' associata a tutti i tipi pendenza, non puo' dunque autorizzare l'applicazione a tutti i tipi pendenza o abilitare l'autodeterminazione dei tipi pendenza");
							}
							
//							appAuthTipiPendenzaAll = true;
							idTipiVersamento.clear();
							break;
						}
						
						idTipiVersamento.add(tipoPendenzaPost.getIdTipoPendenza());
						
					} else if(object instanceof java.util.LinkedHashMap) {
						java.util.LinkedHashMap<?,?> map = (LinkedHashMap<?,?>) object;
						TipoPendenzaProfiloIndex tipoPendenzaPost = new TipoPendenzaProfiloIndex();
						if(map.containsKey("idTipoPendenza"))
							tipoPendenzaPost.setIdTipoPendenza((String) map.get("idTipoPendenza"));
						if(map.containsKey("descrizione")) {
							tipoPendenzaPost.setDescrizione((String) map.get("descrizione"));
						}
						
						if(tipoPendenzaPost.getIdTipoPendenza().equals(ApplicazioniController.AUTORIZZA_TIPI_PENDENZA_STAR)) {
							List<String> tipiVersamentoAutorizzati = AuthorizationManager.getTipiVersamentoAutorizzati(user);
							
							if(tipiVersamentoAutorizzati == null)
								throw AuthorizationManager.toNotAuthorizedExceptionNessunTipoVersamentoAutorizzato(user);
							
							if(tipiVersamentoAutorizzati.size() > 0) {
								throw AuthorizationManager.toNotAuthorizedException(user, "l'utenza non e' associata a tutti i tipi pendenza, non puo' dunque autorizzare l'applicazione a tutti i tipi pendenza o abilitare l'autodeterminazione dei tipi pendenza");
							}
							
//							appAuthTipiPendenzaAll = true;
							idTipiVersamento.clear();
							break;
						}
						idTipiVersamento.add(tipoPendenzaPost.getIdTipoPendenza());
					}
				}	
				connettore.setTipiPendenza(idTipiVersamento);
			}
			
			switch (connector.getTipoConnettore()) {
			case EMAIL:
				connettore.setTipoConnettore(TipoConnettore.EMAIL);
				connettore.setEmailIndirizzi(connector.getEmailIndirizzi());
				connettore.setEmailSubject(connector.getEmailSubject());
				connettore.setEmailAllegato(connector.EmailAllegato());
				connettore.setDownloadBaseURL(connector.getDownloadBaseUrl());
				break;
			case FILESYSTEM:
				connettore.setTipoConnettore(TipoConnettore.FILE_SYSTEM);
				connettore.setFileSystemPath(connector.getFileSystemPath());
				break;
			case REST:
				connettore.setTipoConnettore(TipoConnettore.REST);
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
				
				connettore.setUrl(connector.getUrl());
				if(connector.getVersioneApi() != null)
					connettore.setVersione(Versionabile.Versione.toEnum(VersioneApiEnum.fromValue(connector.getVersioneApi()).toNameString()));
				
				connettore.setContenuti(connector.getContenuti());
				break;
			}
		}
		
		return connettore;
	}

	public static it.govpay.backoffice.v1.beans.ConnettoreNotificaPagamentiGovPay toRsModel(it.govpay.model.ConnettoreNotificaPagamenti connettore) throws ServiceException {
		it.govpay.backoffice.v1.beans.ConnettoreNotificaPagamentiGovPay rsModel = new it.govpay.backoffice.v1.beans.ConnettoreNotificaPagamentiGovPay();
		BDConfigWrapper configWrapper = new BDConfigWrapper(ContextThreadLocal.get().getTransactionId(), true);
		
		rsModel.setAbilitato(connettore.isAbilitato());
		if(connettore.isAbilitato()) {
			rsModel.setVersioneCsv(connettore.getVersioneCsv());
			
			switch (connettore.getTipoConnettore()) {
			case EMAIL:
				rsModel.setTipoConnettore(TipoConnettoreEnum.EMAIL);
				rsModel.setEmailIndirizzi(connettore.getEmailIndirizzi());
				rsModel.setEmailSubject(connettore.getEmailSubject());
				rsModel.setEmailAllegato(connettore.isEmailAllegato());
				rsModel.setDownloadBaseUrl(connettore.getDownloadBaseURL());
				break;
			case FILE_SYSTEM:
				rsModel.setTipoConnettore(TipoConnettoreEnum.FILESYSTEM);
				rsModel.setFileSystemPath(connettore.getFileSystemPath());
				break;
			case WEB_SERVICE:
				break;
			case REST:
				if(connettore.getTipoAutenticazione()!=null && !connettore.getTipoAutenticazione().equals(EnumAuthType.NONE))
					rsModel.setAuth(ConnettoriConverter.toTipoAutenticazioneRsModel(connettore));
				rsModel.setUrl(connettore.getUrl());
				if(connettore.getVersione() != null)
					rsModel.setVersioneApi(VersioneApiEnum.fromName(connettore.getVersione().getApiLabel()).toString());
				
				rsModel.setContenuti(connettore.getContenuti());
				break;
			}
			
			List<Object> idTipiPendenza = null;
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
}
