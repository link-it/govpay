package it.govpay.core.business.model;

import java.util.ArrayList;
import java.util.List;
import it.govpay.bd.model.Pagamento;

public class RichiestaIncassoDTOResponse {

	public RichiestaIncassoDTOResponse() {
		pagamenti = new ArrayList<Pagamento>();
	}

	private List<Pagamento> pagamenti;
	private boolean isCreato;

	public List<Pagamento> getPagamenti() {
		return pagamenti;
	}

	public void setPagamenti(List<Pagamento> pagamenti) {
		this.pagamenti = pagamenti;
	}

	public boolean isCreato() {
		return isCreato;
	}

	public void setCreato(boolean isCreato) {
		this.isCreato = isCreato;
	}
}
