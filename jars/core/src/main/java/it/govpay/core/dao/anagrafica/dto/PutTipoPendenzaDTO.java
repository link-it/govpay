package it.govpay.core.dao.anagrafica.dto;

import org.springframework.security.core.Authentication;

import it.govpay.model.TipoVersamento;

public class PutTipoPendenzaDTO extends BasicCreateRequestDTO  {
	
	
	private TipoVersamento tipoVersamento;
	private String codTipoVersamento;
	
	public PutTipoPendenzaDTO(Authentication user) {
		super(user);
	}

	public TipoVersamento getTipoVersamento() {
		return tipoVersamento;
	}

	public void setTipoVersamento(TipoVersamento tipoVersamento) {
		this.tipoVersamento = tipoVersamento;
	}

	public String getCodTipoVersamento() {
		return codTipoVersamento;
	}

	public void setCodTipoVersamento(String codTipoVersamento) {
		this.codTipoVersamento = codTipoVersamento;
	}


}
