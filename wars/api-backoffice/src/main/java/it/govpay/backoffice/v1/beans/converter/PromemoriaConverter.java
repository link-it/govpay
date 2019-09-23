package it.govpay.backoffice.v1.beans.converter;

import java.math.BigDecimal;

import it.govpay.backoffice.v1.beans.PromemoriaIndex;
import it.govpay.backoffice.v1.beans.StatoPromemoria;
import it.govpay.backoffice.v1.beans.TipoPromemoria;
import it.govpay.bd.model.Promemoria;

public class PromemoriaConverter {

	public static PromemoriaIndex toRsModelIndex(Promemoria promemoria) {
		PromemoriaIndex rsModel = new PromemoriaIndex();
		
		rsModel.setDataCreazione(promemoria.getDataCreazione());
		rsModel.setDataProssimaSpedizione(promemoria.getDataProssimaSpedizione());
		rsModel.setDataUltimoAggiornamento(promemoria.getDataAggiornamento());
		rsModel.setDescrizioneStato(promemoria.getDescrizioneStato());
		rsModel.setNumeroTentativi(new BigDecimal(promemoria.getTentativiSpedizione()));
		if(promemoria.getStato() != null) {
			switch (promemoria.getStato()) {
			case ANNULLATO:
				rsModel.setStato(StatoPromemoria.ANNULLATO);
				break;
			case DA_SPEDIRE:
				rsModel.setStato(StatoPromemoria.DA_SPEDIRE);
				break;
			case FALLITO:
				rsModel.setStato(StatoPromemoria.FALLITO);
				break;
			case SPEDITO:
				rsModel.setStato(StatoPromemoria.SPEDITO);
				break;
			}
		}
		
		if(promemoria.getTipo() != null) {
			switch (promemoria.getTipo()) {
			case AVVISO:
				rsModel.setTipo(TipoPromemoria.AVVISO_PAGAMENTO);
				break;
			case RICEVUTA:
				rsModel.setTipo(TipoPromemoria.RICEVUTA_TELEMATICA);
				break;
			}
		}
		
		return rsModel;
	}

}
