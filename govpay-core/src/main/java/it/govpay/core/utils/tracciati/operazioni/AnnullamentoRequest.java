package it.govpay.core.utils.tracciati.operazioni;

import it.govpay.model.Operazione.TipoOperazioneType;


public class AnnullamentoRequest extends AbstractOperazioneRequest {

	private String motivoAnnullamento;

	public AnnullamentoRequest(){
		super(TipoOperazioneType.DEL);
	}

	public String getMotivoAnnullamento() {
		return motivoAnnullamento;
	}

	public void setMotivoAnnullamento(String motivoAnnullamento) {
		this.motivoAnnullamento = motivoAnnullamento;
	}
}
