package it.govpay.core.business.model;

import java.math.BigDecimal;
import java.util.Date;

public class RichiestaIncassoDTO {
	
	private String principal;
	private String codDominio;
	private String trn;
	private String causale;
	private BigDecimal importo;
	private Date data_valuta;
	private String dispositivo;
	
	public String getTrn() {
		return trn;
	}
	public void setTrn(String trn) {
		this.trn = trn;
	}
	public String getCausale() {
		return causale;
	}
	public void setCausale(String causale) {
		this.causale = causale;
	}
	public BigDecimal getImporto() {
		return importo;
	}
	public void setImporto(BigDecimal importo) {
		this.importo = importo;
	}
	public Date getData_valuta() {
		return data_valuta;
	}
	public void setData_valuta(Date data_valuta) {
		this.data_valuta = data_valuta;
	}
	public String getDispositivo() {
		return dispositivo;
	}
	public void setDispositivo(String dispositivo) {
		this.dispositivo = dispositivo;
	}
	public String getPrincipal() {
		return principal;
	}
	public void setPrincipal(String principal) {
		this.principal = principal;
	}
	public String getCodDominio() {
		return codDominio;
	}
	public void setCodDominio(String codDominio) {
		this.codDominio = codDominio;
	}
}
