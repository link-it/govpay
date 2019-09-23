package it.govpay.pagamento.v2.beans.converter;

import org.openspcoop2.utils.jaxrs.RawObject;

import it.govpay.pagamento.v2.beans.TipoPendenza;
import it.govpay.pagamento.v2.beans.TipoPendenzaForm;

public class TipiPendenzaConverter {

	public static TipoPendenza toTipoPendenzaRsModel(it.govpay.model.TipoVersamento tipoVersamento) {
		TipoPendenza rsModel = new TipoPendenza();
		
		rsModel.descrizione(tipoVersamento.getDescrizione())
		.idTipoPendenza(tipoVersamento.getCodTipoVersamento());
		
		if(tipoVersamento.getTipoDefault() != null) {
			switch (tipoVersamento.getTipoDefault()) {
			case DOVUTO:
				rsModel.setTipo(it.govpay.pagamento.v2.beans.TipoPendenzaTipologia.DOVUTO);
				break;
			case SPONTANEO:
				rsModel.setTipo(it.govpay.pagamento.v2.beans.TipoPendenzaTipologia.SPONTANEO);
				break;
			}
		}
		
		if(tipoVersamento.getFormDefinizioneDefault() != null && tipoVersamento.getFormDefinizioneDefault() != null) {
			TipoPendenzaForm form = new TipoPendenzaForm();
			form.setTipo(tipoVersamento.getFormTipoDefault());
			form.setDefinizione(new RawObject(tipoVersamento.getFormDefinizioneDefault())); 
			rsModel.setForm(form);
		}
		
		rsModel.setVisualizzazione(new RawObject(tipoVersamento.getVisualizzazioneDefinizioneDefault()));
		
		
		return rsModel;
	}
}
