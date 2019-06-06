package it.govpay.core.dao.anagrafica.dto;

import it.govpay.bd.model.TipoVersamentoDominio;

public class GetTipoPendenzaDominioDTOResponse {
	
	private TipoVersamentoDominio tipoVersamentoDominio;
	
	public GetTipoPendenzaDominioDTOResponse(TipoVersamentoDominio tipoVersamentoDominio) {
		this.tipoVersamentoDominio = tipoVersamentoDominio;
	}

	public TipoVersamentoDominio getTipoVersamento() {
		return this.tipoVersamentoDominio;
	}

}
