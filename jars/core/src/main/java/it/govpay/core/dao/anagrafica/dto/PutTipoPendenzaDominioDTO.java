package it.govpay.core.dao.anagrafica.dto;

import org.springframework.security.core.Authentication;

import it.govpay.bd.model.TipoVersamentoDominio;

public class PutTipoPendenzaDominioDTO extends BasicCreateRequestDTO  {
	
	private String idDominio;
	private TipoVersamentoDominio tipoVersamentoDominio;
	private String codTipoVersamento;
	
	public PutTipoPendenzaDominioDTO(Authentication user) {
		super(user);
	}

	public String getCodTipoVersamento() {
		return codTipoVersamento;
	}

	public void setCodTipoVersamento(String codTipoVersamento) {
		this.codTipoVersamento = codTipoVersamento;
	}

	public String getIdDominio() {
		return idDominio;
	}

	public void setIdDominio(String idDominio) {
		this.idDominio = idDominio;
	}

	public TipoVersamentoDominio getTipoVersamentoDominio() {
		return tipoVersamentoDominio;
	}

	public void setTipoVersamentoDominio(TipoVersamentoDominio tipoVersamentoDominio) {
		this.tipoVersamentoDominio = tipoVersamentoDominio;
	}


}
