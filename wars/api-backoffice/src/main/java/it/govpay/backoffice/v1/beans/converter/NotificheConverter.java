package it.govpay.backoffice.v1.beans.converter;

import java.math.BigDecimal;

import org.openspcoop2.generic_project.exception.ServiceException;

import it.govpay.backoffice.v1.beans.NotificaIndex;
import it.govpay.backoffice.v1.beans.StatoNotifica;
import it.govpay.backoffice.v1.beans.TipoNotifica;
import it.govpay.bd.model.Notifica;

public class NotificheConverter {

	public static NotificaIndex toRsModelIndex(Notifica notifica) throws ServiceException {
		NotificaIndex rsModel = new NotificaIndex();
		
		rsModel.setDataCreazione(notifica.getDataCreazione());
		rsModel.setDataProssimaSpedizione(notifica.getDataProssimaSpedizione());
		rsModel.setDataUltimoAggiornamento(notifica.getDataAggiornamento());
		rsModel.setDescrizioneStato(notifica.getDescrizioneStato());
		rsModel.setNumeroTentativi(new BigDecimal(notifica.getTentativiSpedizione()));
		if(notifica.getStato() != null) {
			switch (notifica.getStato()) {
			case ANNULLATA:
				rsModel.setStato(StatoNotifica.ANNULLATA);
				break;
			case DA_SPEDIRE:
				rsModel.setStato(StatoNotifica.DA_SPEDIRE);
				break;
			case SPEDITO:
				rsModel.setStato(StatoNotifica.SPEDITA);
				break;
			default:
				break;
			}
		}
		
		if(notifica.getTipo() != null) {
			switch (notifica.getTipo()) {
			case ANNULLAMENTO:
				rsModel.setTipo(TipoNotifica.ANNULLAMENTO);
				break;
			case ATTIVAZIONE:
				rsModel.setTipo(TipoNotifica.ATTIVAZIONE);
				break;
			case FALLIMENTO:
				rsModel.setTipo(TipoNotifica.FALLIMENTO);
				break;
			case RICEVUTA:
				rsModel.setTipo(TipoNotifica.RICEVUTA);
				break;
			}
		}
		
		rsModel.setIdA2A(notifica.getApplicazione(null).getCodApplicazione());
		
		return rsModel;
	}

}
