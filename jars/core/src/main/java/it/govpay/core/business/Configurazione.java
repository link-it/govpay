package it.govpay.core.business;

import org.openspcoop2.generic_project.exception.NotFoundException;
import org.openspcoop2.generic_project.exception.ServiceException;
import org.openspcoop2.utils.LoggerWrapperFactory;
import org.openspcoop2.utils.serialization.IOException;
import org.openspcoop2.utils.service.context.ContextThreadLocal;
import org.slf4j.Logger;

import it.govpay.bd.BDConfigWrapper;
import it.govpay.bd.anagrafica.AnagraficaManager;
import it.govpay.bd.configurazione.ConfigurazioneBD;
import it.govpay.bd.configurazione.model.AppIOBatch;
import it.govpay.bd.configurazione.model.AvvisaturaViaAppIo;
import it.govpay.bd.configurazione.model.AvvisaturaViaMail;
import it.govpay.bd.configurazione.model.GdeEvento;
import it.govpay.bd.configurazione.model.GdeEvento.DumpEnum;
import it.govpay.bd.configurazione.model.GdeEvento.LogEnum;
import it.govpay.bd.configurazione.model.GdeInterfaccia;
import it.govpay.bd.configurazione.model.Giornale;
import it.govpay.bd.configurazione.model.GoogleCaptcha;
import it.govpay.bd.configurazione.model.Hardening;
import it.govpay.bd.configurazione.model.MailBatch;
import it.govpay.bd.configurazione.model.MailServer;
import it.govpay.bd.configurazione.model.TracciatoCsv;

public class Configurazione {

	private static Logger log = LoggerWrapperFactory.getLogger(Configurazione.class);

	public Configurazione() {
	}

	public it.govpay.bd.model.Configurazione getConfigurazione() throws ServiceException{
		return this.getConfigurazione(new BDConfigWrapper(ContextThreadLocal.get().getTransactionId(), true));
	}
	
	public it.govpay.bd.model.Configurazione getConfigurazione(BDConfigWrapper configWrapper) throws ServiceException{
		it.govpay.bd.model.Configurazione configurazione = null;
		try {
			configurazione = AnagraficaManager.getConfigurazione(configWrapper);
			this.validaConfigurazione(configurazione);
		}catch(IOException | NotFoundException e) {
			log.error("Impossibile leggere la configurazione di sistema: "+ e.getMessage(), e); 
			throw new ServiceException(e);
		}

		return configurazione; 
	}

	public void salvaConfigurazione(it.govpay.bd.model.Configurazione configurazione) throws ServiceException {
		BDConfigWrapper configWrapper = new BDConfigWrapper(ContextThreadLocal.get().getTransactionId(), true);
		ConfigurazioneBD configurazioneBD = new ConfigurazioneBD(configWrapper);
		configurazioneBD.salvaConfigurazione(configurazione);
	}


	public void validaConfigurazione(it.govpay.bd.model.Configurazione configurazione) throws IOException, ServiceException {
		it.govpay.bd.model.Configurazione configurazioneDefault = this.getConfigurazioneDefault();

		validaConfigurazioneGiornaleEventi(configurazione, configurazioneDefault);

		if(configurazione.getTracciatoCsv() == null) {
			configurazione.setTracciatoCsv(configurazioneDefault.getTracciatoCsv());
		}

		if(configurazione.getHardening() == null) {
			configurazione.setHardening(configurazioneDefault.getHardening());
		}

		if(configurazione.getBatchSpedizioneEmail() == null) {
			configurazione.setBatchSpedizioneEmail(configurazioneDefault.getBatchSpedizioneEmail());
		}

		if(configurazione.getAvvisaturaViaMail() == null) {
			configurazione.setAvvisaturaViaMail(configurazioneDefault.getAvvisaturaViaMail());
		}

		if(configurazione.getAvvisaturaViaAppIo() == null) {
			configurazione.setAvvisaturaViaAppIo(configurazioneDefault.getAvvisaturaViaAppIo());
		}

		if(configurazione.getBatchSpedizioneAppIo() == null) {
			configurazione.setBatchSpedizioneAppIo(configurazioneDefault.getBatchSpedizioneAppIo());
		}
	}

	private void validaConfigurazioneGiornaleEventi(it.govpay.bd.model.Configurazione configurazione, it.govpay.bd.model.Configurazione configurazioneDefault) throws ServiceException {
		if(configurazione.getGiornale() == null) {
			configurazione.setGiornale(configurazioneDefault.getGiornale());
		} 
		
		if(configurazione.getGiornale().getApiBackendIO() == null) {
			configurazione.getGiornale().setApiBackendIO(configurazioneDefault.getGiornale().getApiBackendIO());
		}
		
		if(configurazione.getGiornale().getApiBackoffice() == null) {
			configurazione.getGiornale().setApiBackoffice(configurazioneDefault.getGiornale().getApiBackoffice());
		}
		
		if(configurazione.getGiornale().getApiEnte() == null) {
			configurazione.getGiornale().setApiEnte(configurazioneDefault.getGiornale().getApiEnte());
		}
		
		if(configurazione.getGiornale().getApiPagamento() == null) {
			configurazione.getGiornale().setApiPagamento(configurazioneDefault.getGiornale().getApiPagamento());
		}
		
		if(configurazione.getGiornale().getApiPagoPA() == null) {
			configurazione.getGiornale().setApiPagoPA(configurazioneDefault.getGiornale().getApiPagoPA());
		}
		
		if(configurazione.getGiornale().getApiPendenze() == null) {
			configurazione.getGiornale().setApiPendenze(configurazioneDefault.getGiornale().getApiPendenze());
		}
		
		if(configurazione.getGiornale().getApiRagioneria() == null) {
			configurazione.getGiornale().setApiRagioneria(configurazioneDefault.getGiornale().getApiRagioneria());
		}
		
		if(configurazione.getGiornale().getApiMaggioliJPPA() == null) {
			configurazione.getGiornale().setApiMaggioliJPPA(configurazioneDefault.getGiornale().getApiMaggioliJPPA());
		}
	}

	public it.govpay.bd.model.Configurazione getConfigurazioneDefault() {
		it.govpay.bd.model.Configurazione configurazione = new it.govpay.bd.model.Configurazione();

		configurazione.setGiornale(this.getGiornaleDefault()); 
		configurazione.setTracciatoCsv(this.getTracciatoCsvDefault()); 
		configurazione.setHardening(this.getHardeningDefault()); 
		configurazione.setBatchSpedizioneEmail(this.getBatchSpedizioneEmailDefault()); 
		configurazione.setAvvisaturaViaMail(this.getAvvisaturaViaMailDefault()); 
		configurazione.setAvvisaturaViaAppIo(this.getAvvisaturaViaAppIoDefault()); 
		configurazione.setBatchSpedizioneAppIo(this.getAppIoBatchDefault()); 

		return configurazione;
	}

	public AvvisaturaViaMail getAvvisaturaViaMailDefault() {
		AvvisaturaViaMail avvisaturaMail = new AvvisaturaViaMail();
		return avvisaturaMail;
	}

	public AvvisaturaViaAppIo getAvvisaturaViaAppIoDefault() {
		AvvisaturaViaAppIo avvisaturaAppIo = new AvvisaturaViaAppIo();
		return avvisaturaAppIo;
	}

	public MailBatch getBatchSpedizioneEmailDefault() {
		MailBatch mailBatch = new MailBatch();
		mailBatch.setAbilitato(false);
		mailBatch.setMailserver(new MailServer());
		mailBatch.getMailserver().setConnectionTimeout(12000);
		mailBatch.getMailserver().setReadTimeout(10000);
		mailBatch.getMailserver().setStartTls(false);
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
		
		GdeInterfaccia apiBackendAppIO = new GdeInterfaccia();
		GdeEvento apiBackendAppIOLetture = new GdeEvento();
		apiBackendAppIOLetture.setDump(DumpEnum.SEMPRE);
		apiBackendAppIOLetture.setLog(LogEnum.SEMPRE);
		apiBackendAppIO.setLetture(apiBackendAppIOLetture);
		GdeEvento apiBackendAppIOScritture = new GdeEvento();
		apiBackendAppIOScritture.setDump(DumpEnum.SEMPRE);
		apiBackendAppIOScritture.setLog(LogEnum.SEMPRE);
		apiBackendAppIO.setScritture(apiBackendAppIOScritture);
		giornale.setApiBackendIO(apiBackendAppIO);
		
		GdeInterfaccia apiMaggioliJPPA = new GdeInterfaccia();
		GdeEvento apiMaggioliJPPALetture = new GdeEvento();
		apiMaggioliJPPALetture.setDump(DumpEnum.SEMPRE);
		apiMaggioliJPPALetture.setLog(LogEnum.SEMPRE);
		apiMaggioliJPPA.setLetture(apiMaggioliJPPALetture);
		GdeEvento apiMaggioliJPPAScritture = new GdeEvento();
		apiMaggioliJPPAScritture.setDump(DumpEnum.SEMPRE);
		apiMaggioliJPPAScritture.setLog(LogEnum.SEMPRE);
		apiMaggioliJPPA.setScritture(apiMaggioliJPPAScritture);
		giornale.setApiMaggioliJPPA(apiMaggioliJPPA);

		return giornale;
	}

	public AppIOBatch getAppIoBatchDefault() {
		AppIOBatch appIO = new AppIOBatch();
		appIO.setAbilitato(false);
		return appIO;
	}
}
