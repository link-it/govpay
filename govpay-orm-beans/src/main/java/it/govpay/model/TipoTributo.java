package it.govpay.model;

import it.govpay.model.Tributo.TipoContabilita;

public class TipoTributo extends BasicModel {
	private static final long serialVersionUID = 1L;
	
	private Long id; 
	private String codTributo;
	private String descrizione;
	private TipoContabilita tipoContabilitaDefault;
	private String codContabilitaDefault;
	private String codTributoIuvDefault;
	
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
	public TipoContabilita getTipoContabilitaDefault() {
		return tipoContabilitaDefault;
	}
	public void setTipoContabilitaDefault(TipoContabilita tipoContabilitaDefault) {
		this.tipoContabilitaDefault = tipoContabilitaDefault;
	}
	public String getCodContabilitaDefault() {
		return codContabilitaDefault;
	}
	public void setCodContabilitaDefault(String codContabilitaDefault) {
		this.codContabilitaDefault = codContabilitaDefault;
	}
	public String getCodTributoIuvDefault() {
		return codTributoIuvDefault;
	}
	public void setCodTributoIuvDefault(String codTributoIuvDefault) {
		this.codTributoIuvDefault = codTributoIuvDefault;
	}


}
