/*
 * GovPay - Porta di Accesso al Nodo dei Pagamenti SPC
 * http://www.gov4j.it/govpay
 *
 * Copyright (c) 2014-2026 Link.it srl (http://www.link.it).
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

import org.openspcoop2.generic_project.exception.ServiceException;
import org.springframework.security.core.Authentication;

import it.govpay.core.exceptions.NotAuthorizedException;
import it.govpay.model.Connettore.EnumAuthType;
import it.govpay.model.ConnettoreNotificaPagamenti.Tipo;
import it.govpay.model.ConnettoreNotificaPagamenti.TipoConnettore;
import it.govpay.model.exception.CodificaInesistenteException;

public class ConnettoreNotificaPagamentiMaggioliJPPAConverter {
	
	private ConnettoreNotificaPagamentiMaggioliJPPAConverter() {}

	public static it.govpay.model.ConnettoreNotificaPagamenti getConnettoreDTO(it.govpay.backoffice.v1.beans.ConnettoreNotificaPagamentiMaggioliJPPA connector, Authentication user, Tipo tipo) throws NotAuthorizedException, CodificaInesistenteException {
		it.govpay.model.ConnettoreNotificaPagamenti connettore = new it.govpay.model.ConnettoreNotificaPagamenti();

		connettore.setAbilitato(connector.getAbilitato());
		connettore.setTipoTracciato(tipo.name());
		connettore.setTipoConnettore(TipoConnettore.EMAIL);

		// URL e credenziali vengono sempre salvate indipendentemente dal flag abilitato
		connettore.setUrl(connector.getUrl());
		ConnettoriConverter.setAutenticazione(connettore, connector.getAuth());

		if(Boolean.TRUE.equals(connector.getAbilitato())) {
			connettore.setEmailIndirizzi(connector.getEmailIndirizzi());
			connettore.setEmailSubject(connector.getEmailSubject());
			connettore.setEmailAllegato(connector.getEmailAllegato() != null ? connector.getEmailAllegato() : false);
			connettore.setDownloadBaseURL(connector.getDownloadBaseUrl());
			connettore.setFileSystemPath(connector.getFileSystemPath());
			connettore.setInviaTracciatoEsito(connector.getInviaTracciatoEsito() != null ? connector.getInviaTracciatoEsito() : false);
		}

		return connettore;
	}

	public static it.govpay.backoffice.v1.beans.ConnettoreNotificaPagamentiMaggioliJPPA toRsModel(it.govpay.model.ConnettoreNotificaPagamenti connettore) throws ServiceException {
		it.govpay.backoffice.v1.beans.ConnettoreNotificaPagamentiMaggioliJPPA rsModel = new it.govpay.backoffice.v1.beans.ConnettoreNotificaPagamentiMaggioliJPPA();

		rsModel.setAbilitato(connettore.isAbilitato());
		rsModel.setDataUltimaRT(connettore.getDataUltimaRt());

		// URL e credenziali vengono sempre restituite indipendentemente dal flag abilitato
		rsModel.setUrl(connettore.getUrl());
		if(connettore.getTipoAutenticazione()!=null && !connettore.getTipoAutenticazione().equals(EnumAuthType.NONE))
			rsModel.setAuth(ConnettoriConverter.toTipoAutenticazioneRsModel(connettore));

		if(connettore.isAbilitato()) {
			rsModel.setEmailIndirizzi(connettore.getEmailIndirizzi());
			rsModel.setEmailSubject(connettore.getEmailSubject());
			rsModel.setEmailAllegato(connettore.isEmailAllegato());
			rsModel.setDownloadBaseUrl(connettore.getDownloadBaseURL());
			rsModel.setFileSystemPath(connettore.getFileSystemPath());
			rsModel.setInviaTracciatoEsito(connettore.isInviaTracciatoEsito());
		}
		return rsModel;
	}
}
