/*
 * GovPay - Porta di Accesso al Nodo dei Pagamenti SPC
 * http://www.gov4j.it/govpay
 *
 * Copyright (c) 2014-2025 Link.it srl (http://www.link.it).
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License version 3, as published by
 * the Free Software Foundation.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 *
 */
package it.govpay.backoffice.v1.beans.converter;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;

import org.openspcoop2.generic_project.exception.NotFoundException;
import org.openspcoop2.generic_project.exception.ServiceException;
import org.openspcoop2.utils.service.context.ContextThreadLocal;
import org.springframework.security.core.Authentication;

import it.govpay.backoffice.v1.beans.ConnettoreNotificaPagamentiHyperSicAPKappa.TipoConnettoreEnum;
import it.govpay.backoffice.v1.beans.TipoPendenzaProfiloIndex;
import it.govpay.backoffice.v1.controllers.ApplicazioniController;
import it.govpay.bd.BDConfigWrapper;
import it.govpay.bd.anagrafica.AnagraficaManager;
import it.govpay.core.autorizzazione.AuthorizationManager;
import it.govpay.core.beans.Costanti;
import it.govpay.core.exceptions.NotAuthorizedException;
import it.govpay.model.ConnettoreNotificaPagamenti.Tipo;
import it.govpay.model.ConnettoreNotificaPagamenti.TipoConnettore;
import it.govpay.model.TipoVersamento;

public class ConnettoreNotificaPagamentiHyperSicAPKappaConverter {
	
	private ConnettoreNotificaPagamentiHyperSicAPKappaConverter() {}

	public static it.govpay.model.ConnettoreNotificaPagamenti getConnettoreDTO(it.govpay.backoffice.v1.beans.ConnettoreNotificaPagamentiHyperSicAPKappa connector, Authentication user, Tipo tipo) throws NotAuthorizedException {
		it.govpay.model.ConnettoreNotificaPagamenti connettore = new it.govpay.model.ConnettoreNotificaPagamenti();

		connettore.setAbilitato(connector.getAbilitato());

		if(Boolean.TRUE.equals(connector.getAbilitato())) {
			connettore.setTipoTracciato(tipo.name());
			connettore.setVersioneCsv(connector.getVersioneCsv());
			connettore.setIntervalloCreazioneTracciato(connector.getIntervalloCreazioneTracciato().intValue());

			if(connector.getTipiPendenza() != null) {
				List<String> idTipiVersamento = new ArrayList<>();

				for (Object object : connector.getTipiPendenza()) {
					if(object instanceof String idTipoPendenza) {
						if(idTipoPendenza.equals(ApplicazioniController.AUTORIZZA_TIPI_PENDENZA_STAR)) {
							List<String> tipiVersamentoAutorizzati = AuthorizationManager.getTipiVersamentoAutorizzati(user);

							if(tipiVersamentoAutorizzati == null)
								throw AuthorizationManager.toNotAuthorizedExceptionNessunTipoVersamentoAutorizzato(user);

							if(!tipiVersamentoAutorizzati.isEmpty()) {
								throw AuthorizationManager.toNotAuthorizedException(user, Costanti.MSG_L_UTENZA_NON_E_ASSOCIATA_A_TUTTI_I_TIPI_PENDENZA_NON_PUO_DUNQUE_AUTORIZZARE_L_APPLICAZIONE_A_TUTTI_I_TIPI_PENDENZA_O_ABILITARE_L_AUTODETERMINAZIONE_DEI_TIPI_PENDENZA);
							}

							idTipiVersamento.clear();
							break;
						}

						idTipiVersamento.add(idTipoPendenza);


					} else if(object instanceof TipoPendenzaProfiloIndex tipoPendenzaPost) {
						if(tipoPendenzaPost.getIdTipoPendenza().equals(ApplicazioniController.AUTORIZZA_TIPI_PENDENZA_STAR)) {
							List<String> tipiVersamentoAutorizzati = AuthorizationManager.getTipiVersamentoAutorizzati(user);

							if(tipiVersamentoAutorizzati == null)
								throw AuthorizationManager.toNotAuthorizedExceptionNessunTipoVersamentoAutorizzato(user);

							if(!tipiVersamentoAutorizzati.isEmpty()) {
								throw AuthorizationManager.toNotAuthorizedException(user, Costanti.MSG_L_UTENZA_NON_E_ASSOCIATA_A_TUTTI_I_TIPI_PENDENZA_NON_PUO_DUNQUE_AUTORIZZARE_L_APPLICAZIONE_A_TUTTI_I_TIPI_PENDENZA_O_ABILITARE_L_AUTODETERMINAZIONE_DEI_TIPI_PENDENZA);
							}

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

							if(!tipiVersamentoAutorizzati.isEmpty()) {
								throw AuthorizationManager.toNotAuthorizedException(user, Costanti.MSG_L_UTENZA_NON_E_ASSOCIATA_A_TUTTI_I_TIPI_PENDENZA_NON_PUO_DUNQUE_AUTORIZZARE_L_APPLICAZIONE_A_TUTTI_I_TIPI_PENDENZA_O_ABILITARE_L_AUTODETERMINAZIONE_DEI_TIPI_PENDENZA);
							}

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
				connettore.setEmailAllegato(connector.getEmailAllegato());
				connettore.setDownloadBaseURL(connector.getDownloadBaseUrl());
				break;
			case FILESYSTEM:
				connettore.setTipoConnettore(TipoConnettore.FILE_SYSTEM);
				connettore.setFileSystemPath(connector.getFileSystemPath());
				break;
			}
		}

		return connettore;
	}

	public static it.govpay.backoffice.v1.beans.ConnettoreNotificaPagamentiHyperSicAPKappa toRsModel(it.govpay.model.ConnettoreNotificaPagamenti connettore) throws ServiceException {
		it.govpay.backoffice.v1.beans.ConnettoreNotificaPagamentiHyperSicAPKappa rsModel = new it.govpay.backoffice.v1.beans.ConnettoreNotificaPagamentiHyperSicAPKappa();
		BDConfigWrapper configWrapper = new BDConfigWrapper(ContextThreadLocal.get().getTransactionId(), true);

		rsModel.setAbilitato(connettore.isAbilitato());
		if(connettore.isAbilitato()) {
			rsModel.setVersioneCsv(connettore.getVersioneCsv());
			rsModel.setIntervalloCreazioneTracciato(new BigDecimal(connettore.getIntervalloCreazioneTracciato()));

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
			case WEB_SERVICE, REST:
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
							//donothing
						}
					}
				}
			}

			rsModel.setTipiPendenza(idTipiPendenza);
		}
		return rsModel;
	}
}
