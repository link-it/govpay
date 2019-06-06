package it.govpay.backoffice.v1.beans.converter;

import org.openspcoop2.generic_project.exception.ServiceException;

import it.govpay.backoffice.v1.beans.ConnettoreFtp;
import it.govpay.backoffice.v1.beans.ServizioFtp;
import it.govpay.model.ConnettoreSftp;

public class ConnettoreSftpConverter {
	
	public static ConnettoreSftp getConnettore(it.govpay.backoffice.v1.beans.ServizioFtp connector, String idIntermediario) throws ServiceException {
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

	public static ServizioFtp toRsModel(it.govpay.model.ConnettoreSftp connettore) throws ServiceException {
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
