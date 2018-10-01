package it.govpay.core.dao.anagrafica.dto;

import it.govpay.model.IAutorizzato;
import it.govpay.model.TipoTributo;

public class PutEntrataDTO extends BasicCreateRequestDTO  {
	
	
	private TipoTributo tipoTributo;
	private String codTributo;
	
	public PutEntrataDTO(IAutorizzato user) {
		super(user);
	}

	public TipoTributo getTipoTributo() {
		return this.tipoTributo;
	}

	public void setTipoTributo(TipoTributo tipoTributo) {
		this.tipoTributo = tipoTributo;
	}

	public String getCodTributo() {
		return this.codTributo;
	}

	public void setCodTributo(String codTributo) {
		this.codTributo = codTributo;
	}

}
