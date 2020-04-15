package it.govpay.backoffice.v1.beans.converter;

import org.apache.commons.lang.ArrayUtils;
import org.openspcoop2.generic_project.exception.ServiceException;
import org.openspcoop2.utils.json.ValidationException;

import it.govpay.backoffice.v1.beans.GdeEvento;
import it.govpay.backoffice.v1.beans.GdeEvento.DumpEnum;
import it.govpay.backoffice.v1.beans.GdeEvento.LogEnum;
import it.govpay.backoffice.v1.beans.GdeInterfacce;
import it.govpay.backoffice.v1.beans.GdeInterfaccia;
import it.govpay.backoffice.v1.beans.Giornale;

public class GiornaleConverter {

	
	public static it.govpay.bd.configurazione.model.Giornale getGiornaleDTO(Giornale giornalePost) throws ServiceException, ValidationException {
		it.govpay.bd.configurazione.model.Giornale giornale = new it.govpay.bd.configurazione.model.Giornale();
		
		GdeInterfacce interfacce = giornalePost.getInterfacce();
		
		giornale.setApiBackoffice(getGdeInterfaccia(interfacce.getApiBackoffice()));
		giornale.setApiEnte(getGdeInterfaccia(interfacce.getApiEnte()));
		giornale.setApiPagamento(getGdeInterfaccia(interfacce.getApiPagamento()));
		giornale.setApiPagoPA(getGdeInterfaccia(interfacce.getApiPagoPA()));
		giornale.setApiRagioneria(getGdeInterfaccia(interfacce.getApiRagioneria()));
		giornale.setApiPendenze(getGdeInterfaccia(interfacce.getApiPendenze()));
		giornale.setApiBackendIO(getGdeInterfaccia(interfacce.getApiBackendIO()));
		
		return giornale;
	}
	
	
	
	private static it.govpay.bd.configurazione.model.GdeInterfaccia getGdeInterfaccia(GdeInterfaccia gdeInterfaccia) throws ValidationException {
		
		it.govpay.bd.configurazione.model.GdeInterfaccia interfaccia = new it.govpay.bd.configurazione.model.GdeInterfaccia();
		
		interfaccia.setLetture(getGdeEvento(gdeInterfaccia.getLetture()));
		interfaccia.setScritture(getGdeEvento(gdeInterfaccia.getScritture()));
		
		return interfaccia;
	}

	private static it.govpay.bd.configurazione.model.GdeEvento getGdeEvento(GdeEvento gdeEvento) throws ValidationException {
		 
		it.govpay.bd.configurazione.model.GdeEvento evento = new it.govpay.bd.configurazione.model.GdeEvento();
		
		if(gdeEvento.getLog() != null) {
			
			// valore log non valido
			if(LogEnum.fromValue(gdeEvento.getLog()) == null) {
				throw new ValidationException("Codifica inesistente per log. Valore fornito [" + gdeEvento.getLog() + "] valori possibili " + ArrayUtils.toString(LogEnum.values()));
			}
			
			gdeEvento.setLog(LogEnum.fromValue(gdeEvento.getLog()));
			
			switch(gdeEvento.getLogEnum()) {
			case MAI:
				evento.setLog(it.govpay.bd.configurazione.model.GdeEvento.LogEnum.MAI);
				break;
			case SEMPRE:
				evento.setLog(it.govpay.bd.configurazione.model.GdeEvento.LogEnum.SEMPRE);
				break;
			case SOLO_ERRORE:
				evento.setLog(it.govpay.bd.configurazione.model.GdeEvento.LogEnum.SOLO_ERRORE);
				break;
			}
		}

		if(gdeEvento.getDump() != null) {
			
			// valore dumo non valido
			if(DumpEnum.fromValue(gdeEvento.getDump()) == null) {
				throw new ValidationException("Codifica inesistente per dump. Valore fornito [" + gdeEvento.getDump() + "] valori possibili " + ArrayUtils.toString(DumpEnum.values()));
			}
			
			gdeEvento.setDump(DumpEnum.fromValue(gdeEvento.getDump()));
			
			switch(gdeEvento.getDumpEnum()) {
			case MAI:
				evento.setDump(it.govpay.bd.configurazione.model.GdeEvento.DumpEnum.MAI);
				break;
			case SEMPRE:
				evento.setDump(it.govpay.bd.configurazione.model.GdeEvento.DumpEnum.SEMPRE);
				break;
			case SOLO_ERRORE:
				evento.setDump(it.govpay.bd.configurazione.model.GdeEvento.DumpEnum.SOLO_ERRORE);
				break;
			}
		}
		
		return evento;
	}

	public static Giornale toRsModel(it.govpay.bd.configurazione.model.Giornale giornale) throws ServiceException {
		Giornale rsModel = new Giornale();

		GdeInterfacce interfacce = new GdeInterfacce();
		
		interfacce.setApiBackoffice(toInterfacciaRsModel(giornale.getApiBackoffice()));
		interfacce.setApiEnte(toInterfacciaRsModel(giornale.getApiEnte()));
		interfacce.setApiPagamento(toInterfacciaRsModel(giornale.getApiPagamento()));
		interfacce.setApiPagoPA(toInterfacciaRsModel(giornale.getApiPagoPA()));
		interfacce.setApiRagioneria(toInterfacciaRsModel(giornale.getApiRagioneria()));
		interfacce.setApiPendenze(toInterfacciaRsModel(giornale.getApiPendenze()));
		interfacce.setApiBackendIO(toInterfacciaRsModel(giornale.getApiBackendIO()));
		
		rsModel.setInterfacce(interfacce);
		
		return rsModel;
	}

	private static GdeInterfaccia toInterfacciaRsModel(it.govpay.bd.configurazione.model.GdeInterfaccia interaccia) {
		GdeInterfaccia rsModel = new GdeInterfaccia();
		
		rsModel.setLetture(toEventoRsModel(interaccia.getLetture()));
		rsModel.setScritture(toEventoRsModel(interaccia.getScritture()));
		
		return rsModel;
	}

	private static GdeEvento toEventoRsModel(it.govpay.bd.configurazione.model.GdeEvento evento) {
		GdeEvento rsModel = new GdeEvento();
		
		switch(evento.getLog()) {
		case MAI:
			rsModel.setLog(LogEnum.MAI);
			break;
		case SEMPRE:
			rsModel.setLog(LogEnum.SEMPRE);
			break;
		case SOLO_ERRORE:
			rsModel.setLog(LogEnum.SOLO_ERRORE);
			break;
		}

		switch(evento.getDump()) {
		case MAI:
			rsModel.setDump(DumpEnum.MAI);
			break;
		case SEMPRE:
			rsModel.setDump(DumpEnum.SEMPRE);
			break;
		case SOLO_ERRORE:
			rsModel.setDump(DumpEnum.SOLO_ERRORE);
			break;
		}
		
		return rsModel;
	}
}
