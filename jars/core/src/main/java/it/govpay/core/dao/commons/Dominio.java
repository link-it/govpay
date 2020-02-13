package it.govpay.core.dao.commons;

import java.util.List;

public class Dominio {

	private Long id;
	private String codDominio;
	private String ragioneSociale;
	private List<Uo> uo;
	
	public String getCodDominio() {
		return codDominio;
	}
	public void setCodDominio(String codDominio) {
		this.codDominio = codDominio;
	}
	public String getRagioneSociale() {
		return ragioneSociale;
	}
	public void setRagioneSociale(String ragioneSociale) {
		this.ragioneSociale = ragioneSociale;
	}
	public List<Uo> getUo() {
		return uo;
	}
	public void setUo(List<Uo> uo) {
		this.uo = uo;
	}
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}

	public static class Uo {
		
		private Long id;
		private String codUo;
		private String ragioneSociale;
		
		public String getCodUo() {
			return codUo;
		}
		public void setCodUo(String codUo) {
			this.codUo = codUo;
		}
		public String getRagioneSociale() {
			return ragioneSociale;
		}
		public void setRagioneSociale(String ragioneSociale) {
			this.ragioneSociale = ragioneSociale;
		}
		public Long getId() {
			return id;
		}
		public void setId(Long id) {
			this.id = id;
		}
	}
}
