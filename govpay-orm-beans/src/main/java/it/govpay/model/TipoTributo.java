package it.govpay.model;

import it.govpay.model.Tributo.TipoContabilta;

public class TipoTributo extends BasicModel {
	private static final long serialVersionUID = 1L;
	
	private Long id; 
	private String codTributo;
	private String descrizione;
	private TipoContabilta tipoContabilitaDefault;
	private String codContabilitaDefault;
	private String codTributoIuv;
	
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public String getCodTributo() {
		return codTributo;
	}
	public void setCodTributo(String codTributo) {
		this.codTributo = codTributo;
	}
	public String getDescrizione() {
		return descrizione;
	}
	public void setDescrizione(String descrizione) {
		this.descrizione = descrizione;
	}
	public TipoContabilta getTipoContabilitaDefault() {
		return tipoContabilitaDefault;
	}
	public void setTipoContabilitaDefault(TipoContabilta tipoContabilitaDefault) {
		this.tipoContabilitaDefault = tipoContabilitaDefault;
	}
	public String getCodContabilitaDefault() {
		return codContabilitaDefault;
	}
	public void setCodContabilitaDefault(String codContabilitaDefault) {
		this.codContabilitaDefault = codContabilitaDefault;
	}
	public String getCodTributoIuv() {
		return codTributoIuv;
	}
	public void setCodTributoIuv(String codTributoIuv) {
		this.codTributoIuv = codTributoIuv;
	}

}
