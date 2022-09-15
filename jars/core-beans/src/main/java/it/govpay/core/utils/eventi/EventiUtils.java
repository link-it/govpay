package it.govpay.core.utils.eventi;

import java.util.Date;

import org.apache.commons.lang.StringUtils;

import it.govpay.core.beans.EventoContext;
import it.govpay.core.beans.EventoContext.Categoria;
import it.govpay.core.beans.EventoContext.Componente;
import it.govpay.core.beans.EventoContext.Esito;
import it.govpay.core.utils.client.HttpMethod;
import it.govpay.model.Evento.RuoloEvento;
import it.govpay.model.configurazione.GdeEvento;
import it.govpay.model.configurazione.GdeEvento.DumpEnum;
import it.govpay.model.configurazione.GdeEvento.LogEnum;
import it.govpay.model.configurazione.GdeInterfaccia;
import it.govpay.model.configurazione.Giornale;

public class EventiUtils {

	public static GdeInterfaccia getConfigurazioneComponente(Componente componente, Giornale giornale) {
		switch(componente) {
		case API_BACKOFFICE:
			return giornale.getApiBackoffice();
		case API_ENTE:
			return giornale.getApiEnte();
		case API_PAGAMENTO:
			return giornale.getApiPagamento();
		case API_PAGOPA:
			return giornale.getApiPagoPA();
		case API_RAGIONERIA:
			return giornale.getApiRagioneria();
		case API_PENDENZE:
			return giornale.getApiPendenze();
		case API_BACKEND_IO:
			return giornale.getApiBackendIO();
		case API_USER:
		case API_WC: 
			return null;
		case API_MAGGIOLI_JPPA:
			return giornale.getApiMaggioliJPPA();
		case API_MYPIVOT:
		case API_SECIM:
		case API_GOVPAY:
		case API_HYPERSIC_APK:
			return EventiUtils.getConfigurazioneTracciatiNotificaPagamenti();
		}
		
		return null;
	}

	public static GdeInterfaccia getConfigurazioneTracciatiNotificaPagamenti() {
		
		GdeInterfaccia apiTracciatiNotificaPagamenti = new GdeInterfaccia();
		GdeEvento apiBackendAppIOLetture = new GdeEvento();
		apiBackendAppIOLetture.setDump(DumpEnum.SEMPRE);
		apiBackendAppIOLetture.setLog(LogEnum.SEMPRE);
		apiTracciatiNotificaPagamenti.setLetture(apiBackendAppIOLetture);
		GdeEvento apiBackendAppIOScritture = new GdeEvento();
		apiBackendAppIOScritture.setDump(DumpEnum.SEMPRE);
		apiBackendAppIOScritture.setLog(LogEnum.SEMPRE);
		apiTracciatiNotificaPagamenti.setScritture(apiBackendAppIOScritture);
		
		return apiTracciatiNotificaPagamenti;
	}

	public static boolean dumpEvento(GdeEvento evento, Integer responseCode) {
		switch (evento.getDump()) {
		case MAI:
			return false;
		case SEMPRE:
			return true;
		case SOLO_ERRORE:
			return responseCode > 399;
		}
		
		return false;
	}

	public static boolean logEvento(GdeEvento evento, Integer responseCode) {
		switch (evento.getLog()) {
		case MAI:
			return false;
		case SEMPRE:
			return true;
		case SOLO_ERRORE:
			return responseCode > 399;
		}
		
		return false;
	}

	public static boolean dumpEvento(GdeEvento evento, EventoContext.Esito esito) {
		switch (evento.getDump()) {
		case MAI:
			return false;
		case SEMPRE:
			return true;
		case SOLO_ERRORE:
			return !esito.equals(Esito.OK);
		}
		
		return false;
	}

	public static boolean logEvento(GdeEvento evento,  EventoContext.Esito esito) {
		switch (evento.getLog()) {
		case MAI:
			return false;
		case SEMPRE:
			return true;
		case SOLO_ERRORE:
			return !esito.equals(Esito.OK);
		}
		
		return false;
	}

	/*
		 Scritture:
			nodoChiediCopiaRT
			nodoChiediStatoRPT
			nodoInviaRPT
			nodoInviaCarrelloRPT
			nodoInviaRichiestaStorno
			nodoInviaRispostaRevoca
			paaVerificaRPT
			paaAttivaRPT
			paaInviaEsitoStorno
			paaInviaRichiestaRevoca
			paaInviaRT
			paSendRT
			paVerifyPaymentNotice
			paGetPayment
		Letture
			tutto il resto
	 * */
	public static boolean isOperazioneScrittura(String operazione) {
		if(EventoContext.Azione.nodoChiediCopiaRT.toString().equals(operazione) 
				|| EventoContext.Azione.nodoChiediStatoRPT.toString().equals(operazione) 
				|| EventoContext.Azione.nodoInviaRPT.toString().equals(operazione) 
				|| EventoContext.Azione.nodoInviaCarrelloRPT.toString().equals(operazione) 
				|| EventoContext.Azione.nodoInviaRichiestaStorno.toString().equals(operazione) 
				|| EventoContext.Azione.nodoInviaRispostaRevoca.toString().equals(operazione) 
				|| EventoContext.APIPAGOPA_TIPOEVENTO_PAAVERIFICARPT.equals(operazione)
				|| EventoContext.APIPAGOPA_TIPOEVENTO_PAAATTIVARPT.equals(operazione)
				|| EventoContext.APIPAGOPA_TIPOEVENTO_PAAINVIAESITOSTORNO.equals(operazione)
				|| EventoContext.APIPAGOPA_TIPOEVENTO_PAAINVIARICHIESTAREVOCA.equals(operazione)
				|| EventoContext.APIPAGOPA_TIPOEVENTO_PAAINVIART.equals(operazione)
				|| EventoContext.APIPAGOPA_TIPOEVENTO_PAVERIFYPAYMENTNOTICE.equals(operazione)
				|| EventoContext.APIPAGOPA_TIPOEVENTO_PAGETPAYMENT.equals(operazione)
				|| EventoContext.APIPAGOPA_TIPOEVENTO_PASENDRT.equals(operazione)
				) {
			return true;
		}
		return false;
	}

	public static boolean isOperazioneScritturaTracciatiNotificaPagamenti(String operazione) {
		if(EventoContext.APIMYPIVOT_TIPOEVENTO_MYPIVOTINVIATRACCIATOEMAIL.equals(operazione)
				|| EventoContext.APIMYPIVOT_TIPOEVENTO_MYPIVOTINVIATRACCIATOFILESYSTEM.equals(operazione)
				|| EventoContext.APIMYPIVOT_TIPOEVENTO_PIVOTSILINVIAFLUSSO.equals(operazione)
				|| EventoContext.APISECIM_TIPOEVENTO_SECIMINVIATRACCIATOEMAIL.equals(operazione)
				|| EventoContext.APISECIM_TIPOEVENTO_SECIMINVIATRACCIATOFILESYSTEM.equals(operazione)
				|| EventoContext.APIGOVPAY_TIPOEVENTO_GOVPAYINVIATRACCIATOEMAIL.equals(operazione)
				|| EventoContext.APIGOVPAY_TIPOEVENTO_GOVPAYINVIATRACCIATOFILESYSTEM.equals(operazione)
				|| EventoContext.APIGOVPAY_TIPOEVENTO_GOVPAYINVIATRACCIATOREST.equals(operazione)
				|| EventoContext.Azione_Ente_Rendicontazioni.inviaFlussoRendicontazione.toString().equals(operazione)
				|| EventoContext.Azione_Ente_Rendicontazioni.inviaRpp.toString().equals(operazione)
				|| EventoContext.Azione_Ente_Rendicontazioni.inviaSintesiFlussiRendicontazione.toString().equals(operazione)
				|| EventoContext.Azione_Ente_Rendicontazioni.inviaSintesiPagamenti.toString().equals(operazione)
				|| EventoContext.APIHYPERSICAPKAPPA_TIPOEVENTO_HYPERSIC_APKINVIATRACCIATOEMAIL.equals(operazione)
				|| EventoContext.APIHYPERSICAPKAPPA_TIPOEVENTO_HYPERSIC_APKINVIATRACCIATOFILESYSTEM.equals(operazione)
				) {
			return true;
		}
		return false;
	}

	public static boolean isOperazioneScritturaConnettoreMaggioliJPPA(String operazione) {
		if(EventoContext.APIMAGGIOLI_JPPA_TIPOEVENTO_INVIAESITOPAGAMENTO.equals(operazione)
				) {
			return true;
		}
		return false;
	}

	public static EventoContext creaEventoContext(Categoria categoriaEvento, RuoloEvento role) {
		EventoContext eventoCtx = new EventoContext();
		eventoCtx.setCategoriaEvento(categoriaEvento);
		eventoCtx.setRole(role);
		eventoCtx.setDataRichiesta(new Date());
		
		return eventoCtx;
	}

	public static HttpMethod getHttpMethod(String httpMethod) {
		if(StringUtils.isNotEmpty(httpMethod)) {
			HttpMethod http = HttpMethod.valueOf(httpMethod);
			return http;
		}
		return null;
	}

	public static boolean isRequestLettura(HttpMethod httpMethod, Componente componente, String operazione) {
		if(componente.equals(Componente.API_PAGOPA)) {
			if(operazione != null)
				return !isOperazioneScrittura(operazione);
			else 
				return false;
		}
		
		if(componente.equals(Componente.API_MAGGIOLI_JPPA)) {
			if(operazione != null)
				return !isOperazioneScritturaConnettoreMaggioliJPPA(operazione);
			else 
				return false;
		}
		
		if(componente.equals(Componente.API_SECIM) || componente.equals(Componente.API_MYPIVOT) || componente.equals(Componente.API_GOVPAY) || componente.equals(Componente.API_HYPERSIC_APK)) {
			if(operazione != null)
				return !isOperazioneScritturaTracciatiNotificaPagamenti(operazione);
			else 
				return false;
		}
		
		return isRequestLettura(httpMethod);
	}

	public static boolean isRequestScrittura(HttpMethod httpMethod, Componente componente, String operazione) {
		if(componente.equals(Componente.API_PAGOPA)) {
			return isOperazioneScrittura(operazione);
		}
		
		if(componente.equals(Componente.API_MAGGIOLI_JPPA)) {
			return isOperazioneScritturaConnettoreMaggioliJPPA(operazione);
		}
		
		if(componente.equals(Componente.API_SECIM) || componente.equals(Componente.API_MYPIVOT) || componente.equals(Componente.API_GOVPAY) || componente.equals(Componente.API_HYPERSIC_APK)) {
			return isOperazioneScritturaTracciatiNotificaPagamenti(operazione);
		}
	
		return isRequestScrittura(httpMethod);
	}

	public static boolean isRequestLettura(HttpMethod httpMethod) {
		if(httpMethod != null ) {
			switch (httpMethod) {
			case GET:
			case OPTIONS:
			case HEAD:
			case TRACE:
				return true;
			case DELETE:
			case LINK:
			case PATCH:
			case POST:
			case PUT:
			case UNLINK:
				return false;
			}
		}
		return false;
	}

	public static boolean isRequestScrittura(HttpMethod httpMethod) {
		if(httpMethod != null ) {
			switch (httpMethod) {
			case PUT:
			case POST:
			case DELETE:
			case PATCH:
			case LINK:
			case UNLINK:
				return true;
			case GET:
			case OPTIONS:
			case HEAD:
			case TRACE:
				return false;
			}
		}
		return false;
	}

}
