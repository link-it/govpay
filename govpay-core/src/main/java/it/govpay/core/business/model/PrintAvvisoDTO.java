package it.govpay.core.business.model;

import it.govpay.model.avvisi.AvvisoPagamento;
import it.govpay.stampe.model.AvvisoPagamentoInput;

public class PrintAvvisoDTO {

	private AvvisoPagamento avviso;
	private AvvisoPagamentoInput input;
	
	public AvvisoPagamento getAvviso() {
		return this.avviso;
	}
	public void setAvviso(AvvisoPagamento avviso) {
		this.avviso = avviso;
	}
	public AvvisoPagamentoInput getInput() {
		return this.input;
	}
	public void setInput(AvvisoPagamentoInput input) {
		this.input = input;
	}
	
}
