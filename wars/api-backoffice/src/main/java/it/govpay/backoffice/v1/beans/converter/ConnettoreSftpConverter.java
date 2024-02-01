/*
 * GovPay - Porta di Accesso al Nodo dei Pagamenti SPC
 * http://www.gov4j.it/govpay
 *
 * Copyright (c) 2014-2024 Link.it srl (http://www.link.it).
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

import it.govpay.backoffice.v1.beans.ConnettoreFtp;
import it.govpay.backoffice.v1.beans.ServizioFtp;
import it.govpay.model.ConnettoreSftp;

public class ConnettoreSftpConverter {

	public static ConnettoreSftp getConnettore(it.govpay.backoffice.v1.beans.ServizioFtp connector, String idIntermediario) {
		ConnettoreSftp connettoreSftp = new ConnettoreSftp();
		connettoreSftp.setIdConnettore(idIntermediario + "_SFTP");
		connettoreSftp.setHttpPasswIn(connector.getFtpLettura().getPassword());
		connettoreSftp.setHttpUserIn(connector.getFtpLettura().getUsername());
		connettoreSftp.setHostIn(connector.getFtpLettura().getHost());
		connettoreSftp.setPortaIn(connector.getFtpLettura().getPorta());
		connettoreSftp.setHttpPasswOut(connector.getFtpScrittura().getPassword());
		connettoreSftp.setHttpUserOut(connector.getFtpScrittura().getUsername());
		connettoreSftp.setHostOut(connector.getFtpScrittura().getHost());
		connettoreSftp.setPortaOut(connector.getFtpScrittura().getPorta());

		return connettoreSftp;
	}

	public static ServizioFtp toRsModel(it.govpay.model.ConnettoreSftp connettore) {
		ServizioFtp rsModel = new ServizioFtp();
		ConnettoreFtp ftpLettura = new ConnettoreFtp();
		ftpLettura.setHost(connettore.getHostIn());
		ftpLettura.setPorta(connettore.getPortaIn());
		ftpLettura.setUsername(connettore.getHttpUserIn());
		ftpLettura.setPassword(connettore.getHttpPasswIn());
		rsModel.setFtpLettura(ftpLettura);
		ConnettoreFtp ftpScrittura = new ConnettoreFtp();
		ftpScrittura.setHost(connettore.getHostOut());
		ftpScrittura.setPorta(connettore.getPortaOut());
		ftpScrittura.setUsername(connettore.getHttpUserOut());
		ftpScrittura.setPassword(connettore.getHttpPasswOut());
		rsModel.setFtpScrittura(ftpScrittura);


		return rsModel;
	}

}
