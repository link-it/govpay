package it.govpay.core.dao.pagamenti.dto;

import java.util.Map;

import javax.ws.rs.core.MultivaluedMap;

import org.springframework.security.core.Authentication;

import it.govpay.core.dao.anagrafica.dto.BasicCreateRequestDTO;
import it.govpay.core.dao.commons.Versamento;
import it.govpay.model.Versamento.ModoAvvisatura;

public class PutPendenzaDTO extends BasicCreateRequestDTO  {
	
	private String customReq;
	private String codDominio;
	private String codTipoVersamento;
	private String codUo;
	
	private Versamento versamento;
	private Boolean stampaAvviso;
	private Boolean avvisaturaDigitale;
	private ModoAvvisatura avvisaturaModalita;
	private MultivaluedMap<String, String> queryParameters;
	private MultivaluedMap<String, String> pathParameters;
	private Map<String, String> headers;

	public PutPendenzaDTO(Authentication user) {
		super(user);
	}

	public Versamento getVersamento() {
		return this.versamento;
	}

	public void setVersamento(Versamento versamento) {
		this.versamento = versamento;
	}

	public boolean isStampaAvviso() {
		return this.stampaAvviso != null ? this.stampaAvviso : false;
	}

	public void setStampaAvviso(Boolean stampaAvviso) {
		this.stampaAvviso = stampaAvviso;
	}

	public Boolean getAvvisaturaDigitale() {
		return this.avvisaturaDigitale;
	}

	public void setAvvisaturaDigitale(Boolean avvisaturaDigitale) {
		this.avvisaturaDigitale = avvisaturaDigitale;
	}

	public ModoAvvisatura getAvvisaturaModalita() {
		return avvisaturaModalita;
	}

	public void setAvvisaturaModalita(ModoAvvisatura avvisaturaModalita) {
		this.avvisaturaModalita = avvisaturaModalita;
	}

	public String getCustomReq() {
		return customReq;
	}

	public void setCustomReq(String customReq) {
		this.customReq = customReq;
	}

	public String getCodDominio() {
		return codDominio;
	}

	public void setCodDominio(String codDominio) {
		this.codDominio = codDominio;
	}

	public String getCodTipoVersamento() {
		return codTipoVersamento;
	}

	public void setCodTipoVersamento(String codTipoVersamento) {
		this.codTipoVersamento = codTipoVersamento;
	}

	public MultivaluedMap<String, String> getQueryParameters() {
		return queryParameters;
	}

	public void setQueryParameters(MultivaluedMap<String, String> queryParameters) {
		this.queryParameters = queryParameters;
	}

	public MultivaluedMap<String, String> getPathParameters() {
		return pathParameters;
	}

	public void setPathParameters(MultivaluedMap<String, String> pathParameters) {
		this.pathParameters = pathParameters;
	}

	public Map<String, String> getHeaders() {
		return headers;
	}

	public void setHeaders(Map<String, String> headers) {
		this.headers = headers;
	}

	public String getCodUo() {
		return codUo;
	}

	public void setCodUo(String codUo) {
		this.codUo = codUo;
	}
}
