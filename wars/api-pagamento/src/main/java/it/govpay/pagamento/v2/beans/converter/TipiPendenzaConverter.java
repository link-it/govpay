package it.govpay.pagamento.v2.beans.converter;

import org.openspcoop2.utils.jaxrs.RawObject;

import it.govpay.pagamento.v2.beans.TipoPendenza;
import it.govpay.pagamento.v2.beans.TipoPendenzaForm;

public class TipiPendenzaConverter {

	public static TipoPendenza toTipoPendenzaRsModel(it.govpay.model.TipoVersamento tipoVersamento) {
		TipoPendenza rsModel = new TipoPendenza();
		
		rsModel.descrizione(tipoVersamento.getDescrizione())
		.idTipoPendenza(tipoVersamento.getCodTipoVersamento());
		
		if(tipoVersamento.getCaricamentoPendenzePortalePagamentoFormTipoDefault() != null && tipoVersamento.getCaricamentoPendenzePortalePagamentoFormDefinizioneDefault() != null) {
			TipoPendenzaForm form = new TipoPendenzaForm();
			form.setTipo(tipoVersamento.getCaricamentoPendenzePortalePagamentoFormTipoDefault());
			form.setDefinizione(new RawObject(tipoVersamento.getCaricamentoPendenzePortalePagamentoFormDefinizioneDefault())); 
			if(tipoVersamento.getCaricamentoPendenzePortalePagamentoFormImpaginazioneDefault() !=null)
				form.setImpaginazione(new RawObject(tipoVersamento.getCaricamentoPendenzePortalePagamentoFormImpaginazioneDefault()));
			rsModel.setForm(form);
		}
		
		rsModel.setVisualizzazione(new RawObject(tipoVersamento.getVisualizzazioneDefinizioneDefault()));
		
		
		return rsModel;
	}
}
