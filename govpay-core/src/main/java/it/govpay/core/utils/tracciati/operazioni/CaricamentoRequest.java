package it.govpay.core.utils.tracciati.operazioni;

import it.govpay.model.Operazione.TipoOperazioneType;


public class CaricamentoRequest extends AbstractOperazioneRequest {

	private String motivoAnnullamento;
	private it.govpay.core.dao.commons.Versamento versamento;

	public CaricamentoRequest(){
		super(TipoOperazioneType.ADD);
	}

	public String getMotivoAnnullamento() {
		return motivoAnnullamento;
	}

	public void setMotivoAnnullamento(String motivoAnnullamento) {
		this.motivoAnnullamento = motivoAnnullamento;
	}

	public it.govpay.core.dao.commons.Versamento getVersamento() {
		return versamento;
	}

	public void setVersamento(it.govpay.core.dao.commons.Versamento versamento) {
		this.versamento = versamento;
	}
	 
}
