package it.govpay.core.business;

import java.util.Properties;

import org.apache.commons.lang.StringUtils;
import org.openspcoop2.generic_project.exception.ServiceException;
import org.openspcoop2.utils.LoggerWrapperFactory;
import org.openspcoop2.utils.serialization.IOException;
import org.slf4j.Logger;

import it.govpay.bd.BasicBD;
import it.govpay.bd.anagrafica.AnagraficaManager;
import it.govpay.bd.configurazione.ConfigurazioneBD;
import it.govpay.bd.configurazione.model.GdeEvento;
import it.govpay.bd.configurazione.model.GdeEvento.DumpEnum;
import it.govpay.bd.configurazione.model.GdeEvento.LogEnum;
import it.govpay.bd.configurazione.model.GdeInterfaccia;
import it.govpay.bd.configurazione.model.Giornale;
import it.govpay.bd.configurazione.model.GoogleCaptcha;
import it.govpay.bd.configurazione.model.Hardening;
import it.govpay.bd.configurazione.model.Mail;
import it.govpay.bd.configurazione.model.MailBatch;
import it.govpay.bd.configurazione.model.MailServer;
import it.govpay.bd.configurazione.model.TracciatoCsv;
import it.govpay.core.utils.GovpayConfig;

public class Configurazione extends BasicBD {

	private static Logger log = LoggerWrapperFactory.getLogger(Configurazione.class);

	public Configurazione(BasicBD bd) {
		super(bd);
	}

	public it.govpay.bd.model.Configurazione getConfigurazione() throws ServiceException{
		it.govpay.bd.model.Configurazione configurazione = null;
		
		try {
			configurazione = AnagraficaManager.getConfigurazione(this);
			this.validaConfigurazione(configurazione);
		}catch(Exception e) {
			log.error("Impossibile leggere la configurazione di sistema, verra' caricata quella di default! Errore: "+ e.getMessage(),e); 
			configurazione = this.getConfigurazioneDefault();
		}
		
		return configurazione; 
	}
	
	public void salvaConfigurazione(it.govpay.bd.model.Configurazione configurazione) throws ServiceException {
		ConfigurazioneBD configurazioneBD = new ConfigurazioneBD(this);
		configurazioneBD.salvaConfigurazione(configurazione);
	}
	
	
	public void validaConfigurazione(it.govpay.bd.model.Configurazione configurazione) throws IOException {
		it.govpay.bd.model.Configurazione configurazioneDefault = this.getConfigurazioneDefault();
		
		if(configurazione.getGiornale() == null) {
			configurazione.setGiornale(configurazioneDefault.getGiornale());
		}
		
		if(configurazione.getTracciatoCsv() == null) {
			configurazione.setTracciatoCsv(configurazioneDefault.getTracciatoCsv());
		}
		
		if(configurazione.getHardening() == null) {
			configurazione.setHardening(configurazioneDefault.getHardening());
		}
		
		if(configurazione.getBatchSpedizioneEmail() == null) {
			configurazione.setBatchSpedizioneEmail(configurazioneDefault.getBatchSpedizioneEmail());
		}
		
		if(configurazione.getPromemoriaMail() == null) {
			configurazione.setPromemoriaEmail(configurazioneDefault.getPromemoriaMail());
		}
		
		if(configurazione.getRicevutaMail() == null) {
			configurazione.setRicevutaEmail(configurazioneDefault.getRicevutaMail());
		}
	}
	
	public it.govpay.bd.model.Configurazione getConfigurazioneDefault() {
		it.govpay.bd.model.Configurazione configurazione = new it.govpay.bd.model.Configurazione();
		
		Properties configurazioniDefault = GovpayConfig.getInstance().getConfigurazioniDefault();
		
		
		// leggo la configurazione da properties
		String configurazioneGiornaleEventi = configurazioniDefault.getProperty(it.govpay.bd.model.Configurazione.GIORNALE_EVENTI);
		if(StringUtils.isNotEmpty(configurazioneGiornaleEventi)) {
			configurazione.setGiornaleEventi(configurazioneGiornaleEventi);
		} else {
			configurazione.setGiornale(this.getGiornaleDefault()); 
		}
		
		String configurazioneTracciatoCsv = configurazioniDefault.getProperty(it.govpay.bd.model.Configurazione.TRACCIATO_CSV);
		if(StringUtils.isNotEmpty(configurazioneTracciatoCsv)) {
			configurazione.setTracciatoCSV(configurazioneTracciatoCsv);
		} else {
			configurazione.setTracciatoCsv(this.getTracciatoCsvDefault()); 
		}
		
		String configurazioneHardening = configurazioniDefault.getProperty(it.govpay.bd.model.Configurazione.HARDENING);
		if(StringUtils.isNotEmpty(configurazioneHardening)) {
			configurazione.setConfHardening(configurazioneHardening);
		} else {
			configurazione.setHardening(this.getHardeningDefault()); 
		}
		
		String configurazioneMailBatch = configurazioniDefault.getProperty(it.govpay.bd.model.Configurazione.MAIL_BATCH);
		if(StringUtils.isNotEmpty(configurazioneMailBatch)) {
			configurazione.setMailBatch(configurazioneMailBatch);
		} else {
			configurazione.setBatchSpedizioneEmail(this.getBatchSpedizioneEmailDefault()); 
		}
		
		String configurazionePromemoriaEmail = configurazioniDefault.getProperty(it.govpay.bd.model.Configurazione.MAIL_PROMEMORIA);
		if(StringUtils.isNotEmpty(configurazionePromemoriaEmail)) {
			configurazione.setMailPromemoria(configurazionePromemoriaEmail);
		} else {
			configurazione.setPromemoriaEmail(this.getPromemoriaEmailDefault()); 
		}
		
		String configurazioneRicevutaEmail = configurazioniDefault.getProperty(it.govpay.bd.model.Configurazione.MAIL_RICEVUTA);
		if(StringUtils.isNotEmpty(configurazioneRicevutaEmail)) {
			configurazione.setMailRicevuta(configurazioneRicevutaEmail);
		} else {
			configurazione.setRicevutaEmail(this.getRicevutaEmailDefault()); 
		}
		
		return configurazione;
	}
	
	public Mail getRicevutaEmailDefault() {
		Mail mail = new Mail();
		return mail;
	}

	public Mail getPromemoriaEmailDefault() {
		Mail mail = new Mail();
		return mail;
	}

	public MailBatch getBatchSpedizioneEmailDefault() {
		MailBatch mailBatch = new MailBatch();
		mailBatch.setAbilitato(false);
		mailBatch.setMailserver(new MailServer());
		return mailBatch;
	}

	public Hardening getHardeningDefault() {
		Hardening hardening = new Hardening();
		hardening.setAbilitato(true);
		hardening.setGoogleCatpcha(new GoogleCaptcha());
		hardening.getGoogleCatpcha().setSecretKey(null);
		hardening.getGoogleCatpcha().setSiteKey(null);
		hardening.getGoogleCatpcha().setServerURL(null);
		hardening.getGoogleCatpcha().setResponseParameter(null);
		hardening.getGoogleCatpcha().setDenyOnFail(true);
		hardening.getGoogleCatpcha().setConnectionTimeout(5000);
		hardening.getGoogleCatpcha().setReadTimeout(5000);
		hardening.getGoogleCatpcha().setSoglia(0.7d);
		
		return hardening;
	}

	public TracciatoCsv getTracciatoCsvDefault() {
		TracciatoCsv tracciato = new TracciatoCsv();
		return tracciato;
	}

	public Giornale getGiornaleDefault() {
		Giornale giornale = new Giornale();
		
		GdeInterfaccia apiBackoffice = new GdeInterfaccia();
		GdeEvento apiBackofficeLetture = new GdeEvento();
		apiBackofficeLetture.setDump(DumpEnum.SEMPRE);
		apiBackofficeLetture.setLog(LogEnum.SEMPRE);
		apiBackoffice.setLetture(apiBackofficeLetture);
		GdeEvento apiBackofficeScritture = new GdeEvento();
		apiBackofficeScritture.setDump(DumpEnum.SEMPRE);
		apiBackofficeScritture.setLog(LogEnum.SEMPRE);
		apiBackoffice.setScritture(apiBackofficeScritture);
		giornale.setApiBackoffice(apiBackoffice);
		
		GdeInterfaccia apiEnte = new GdeInterfaccia();
		GdeEvento apiEnteLetture = new GdeEvento();
		apiEnteLetture.setDump(DumpEnum.SEMPRE);
		apiEnteLetture.setLog(LogEnum.SEMPRE);
		apiEnte.setLetture(apiEnteLetture);
		GdeEvento apiEnteScritture = new GdeEvento();
		apiEnteScritture.setDump(DumpEnum.SEMPRE);
		apiEnteScritture.setLog(LogEnum.SEMPRE);
		apiEnte.setScritture(apiEnteScritture);
		giornale.setApiEnte(apiEnte);
		
		GdeInterfaccia apiPagamento = new GdeInterfaccia();
		GdeEvento apiPagamentoLetture = new GdeEvento();
		apiPagamentoLetture.setDump(DumpEnum.SEMPRE);
		apiPagamentoLetture.setLog(LogEnum.SEMPRE);
		apiPagamento.setLetture(apiPagamentoLetture);
		GdeEvento apiPagamentoScritture = new GdeEvento();
		apiPagamentoScritture.setDump(DumpEnum.SEMPRE);
		apiPagamentoScritture.setLog(LogEnum.SEMPRE);
		apiPagamento.setScritture(apiPagamentoScritture);
		giornale.setApiPagamento(apiPagamento);
		
		GdeInterfaccia apiPagoPA = new GdeInterfaccia();
		GdeEvento apiPagoPALetture = new GdeEvento();
		apiPagoPALetture.setDump(DumpEnum.SEMPRE);
		apiPagoPALetture.setLog(LogEnum.SEMPRE);
		apiPagoPA.setLetture(apiPagoPALetture);
		GdeEvento apiPagoPAScritture = new GdeEvento();
		apiPagoPAScritture.setDump(DumpEnum.SEMPRE);
		apiPagoPAScritture.setLog(LogEnum.SEMPRE);
		apiPagoPA.setScritture(apiPagoPAScritture);
		giornale.setApiPagoPA(apiPagoPA);
		
		GdeInterfaccia apiPendenze = new GdeInterfaccia();
		GdeEvento apiPendenzeLetture = new GdeEvento();
		apiPendenzeLetture.setDump(DumpEnum.SEMPRE);
		apiPendenzeLetture.setLog(LogEnum.SEMPRE);
		apiPendenze.setLetture(apiPendenzeLetture);
		GdeEvento apiPendenzeScritture = new GdeEvento();
		apiPendenzeScritture.setDump(DumpEnum.SEMPRE);
		apiPendenzeScritture.setLog(LogEnum.SEMPRE);
		apiPendenze.setScritture(apiPendenzeScritture);
		giornale.setApiPendenze(apiPendenze);
		
		GdeInterfaccia apiRagioneria = new GdeInterfaccia();
		GdeEvento apiRagioneriaLetture = new GdeEvento();
		apiRagioneriaLetture.setDump(DumpEnum.SEMPRE);
		apiRagioneriaLetture.setLog(LogEnum.SEMPRE);
		apiRagioneria.setLetture(apiRagioneriaLetture);
		GdeEvento apiRagioneriaScritture = new GdeEvento();
		apiRagioneriaScritture.setDump(DumpEnum.SEMPRE);
		apiRagioneriaScritture.setLog(LogEnum.SEMPRE);
		apiRagioneria.setScritture(apiRagioneriaScritture);
		giornale.setApiRagioneria(apiRagioneria);
		
		return giornale;
	}
}
