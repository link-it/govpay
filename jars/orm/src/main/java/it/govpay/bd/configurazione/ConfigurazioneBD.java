package it.govpay.bd.configurazione;

import java.util.List;

import org.openspcoop2.generic_project.exception.NotImplementedException;
import org.openspcoop2.generic_project.exception.ServiceException;
import org.openspcoop2.utils.serialization.IOException;

import it.govpay.bd.BasicBD;
import it.govpay.bd.configurazione.model.GdeEvento;
import it.govpay.bd.configurazione.model.GdeEvento.DumpEnum;
import it.govpay.bd.configurazione.model.GdeEvento.LogEnum;
import it.govpay.bd.configurazione.model.GdeInterfaccia;
import it.govpay.bd.configurazione.model.Giornale;
import it.govpay.bd.configurazione.model.TracciatoCsv;
import it.govpay.bd.model.Configurazione;
import it.govpay.bd.model.converter.ConfigurazioneConverter;

public class ConfigurazioneBD extends BasicBD {

	public ConfigurazioneBD(BasicBD basicBD) {
		super(basicBD);
	}

	public Configurazione getConfigurazione() {
		try {
			List<it.govpay.orm.Configurazione> voList = this.getConfigurazioneService().findAll(this.getConfigurazioneService().newPaginatedExpression());
			return ConfigurazioneConverter.toDTO(voList);
		} catch (NotImplementedException | ServiceException e) {
			return this.getConfigurazioneDefault();  
		}
	}
	
	public void salvaConfigurazione(Configurazione configurazione) throws ServiceException {
		try {
			this.getConfigurazioneService().deleteAll();
			
			List<it.govpay.orm.Configurazione> voList = ConfigurazioneConverter.toVOList(configurazione);
			
			for (it.govpay.orm.Configurazione vo : voList) {
				this.getConfigurazioneService().create(vo);
			}
		} catch (NotImplementedException | IOException e) {
			throw new ServiceException(e);
		}
	}
	
	public Configurazione getConfigurazioneDefault() {
		Configurazione configurazione = new Configurazione();
		configurazione.setGiornale(this.getGiornaleDefault());
		configurazione.setTracciatoCsv(this.getTracciatoCsvDefault());
		return configurazione;
	}

	public TracciatoCsv getTracciatoCsvDefault() {
		TracciatoCsv tracciato = new TracciatoCsv();
		return tracciato;
	}

	public Giornale getGiornaleDefault() {
		Giornale giornale = new Giornale();
		
		GdeInterfaccia apiBackoffice = new GdeInterfaccia();
		GdeEvento apiBackofficeLetture = new GdeEvento();
		apiBackofficeLetture.setDump(DumpEnum.MAI);
		apiBackofficeLetture.setLog(LogEnum.MAI);
		apiBackoffice.setLetture(apiBackofficeLetture);
		GdeEvento apiBackofficeScritture = new GdeEvento();
		apiBackofficeScritture.setDump(DumpEnum.SOLO_ERRORE);
		apiBackofficeScritture.setLog(LogEnum.SOLO_ERRORE);
		apiBackoffice.setScritture(apiBackofficeScritture);
		giornale.setApiBackoffice(apiBackoffice);
		
		GdeInterfaccia apiEnte = new GdeInterfaccia();
		GdeEvento apiEnteLetture = new GdeEvento();
		apiEnteLetture.setDump(DumpEnum.MAI);
		apiEnteLetture.setLog(LogEnum.MAI);
		apiEnte.setLetture(apiEnteLetture);
		GdeEvento apiEnteScritture = new GdeEvento();
		apiEnteScritture.setDump(DumpEnum.SOLO_ERRORE);
		apiEnteScritture.setLog(LogEnum.SOLO_ERRORE);
		apiEnte.setScritture(apiEnteScritture);
		giornale.setApiEnte(apiEnte);
		
		GdeInterfaccia apiPagamento = new GdeInterfaccia();
		GdeEvento apiPagamentoLetture = new GdeEvento();
		apiPagamentoLetture.setDump(DumpEnum.MAI);
		apiPagamentoLetture.setLog(LogEnum.MAI);
		apiPagamento.setLetture(apiPagamentoLetture);
		GdeEvento apiPagamentoScritture = new GdeEvento();
		apiPagamentoScritture.setDump(DumpEnum.SOLO_ERRORE);
		apiPagamentoScritture.setLog(LogEnum.SOLO_ERRORE);
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
		apiPendenzeLetture.setDump(DumpEnum.MAI);
		apiPendenzeLetture.setLog(LogEnum.MAI);
		apiPendenze.setLetture(apiPendenzeLetture);
		GdeEvento apiPendenzeScritture = new GdeEvento();
		apiPendenzeScritture.setDump(DumpEnum.SOLO_ERRORE);
		apiPendenzeScritture.setLog(LogEnum.SOLO_ERRORE);
		apiPendenze.setScritture(apiPendenzeScritture);
		giornale.setApiPendenze(apiPendenze);
		
		GdeInterfaccia apiRagioneria = new GdeInterfaccia();
		GdeEvento apiRagioneriaLetture = new GdeEvento();
		apiRagioneriaLetture.setDump(DumpEnum.MAI);
		apiRagioneriaLetture.setLog(LogEnum.MAI);
		apiRagioneria.setLetture(apiRagioneriaLetture);
		GdeEvento apiRagioneriaScritture = new GdeEvento();
		apiRagioneriaScritture.setDump(DumpEnum.SOLO_ERRORE);
		apiRagioneriaScritture.setLog(LogEnum.SOLO_ERRORE);
		apiRagioneria.setScritture(apiRagioneriaScritture);
		giornale.setApiRagioneria(apiRagioneria);
		
		return giornale;
	}
}
