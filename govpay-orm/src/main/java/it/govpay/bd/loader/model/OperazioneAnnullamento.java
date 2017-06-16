package it.govpay.bd.loader.model;

public class OperazioneAnnullamento extends Operazione{

	private String motivoAnnullamento;
	
	public String getMotivoAnnullamento() {
		return motivoAnnullamento;
	}

	public void setMotivoAnnullamento(String motivoAnnullamento) {
		this.motivoAnnullamento = motivoAnnullamento;
	}
}
