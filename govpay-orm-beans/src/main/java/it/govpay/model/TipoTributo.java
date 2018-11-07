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
	private boolean onlineDefault;
	private boolean pagaTerziDefault;
	
	@Override
	public Long getId() {
		return this.id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public String getCodTributo() {
		return this.codTributo;
	}
	public void setCodTributo(String codTributo) {
		this.codTributo = codTributo;
	}
	public String getDescrizione() {
		return this.descrizione;
	}
	public void setDescrizione(String descrizione) {
		this.descrizione = descrizione;
	}
	public TipoContabilita getTipoContabilitaDefault() {
		return this.tipoContabilitaDefault;
	}
	public void setTipoContabilitaDefault(TipoContabilita tipoContabilitaDefault) {
		this.tipoContabilitaDefault = tipoContabilitaDefault;
	}
	public String getCodContabilitaDefault() {
		return this.codContabilitaDefault;
	}
	public void setCodContabilitaDefault(String codContabilitaDefault) {
		this.codContabilitaDefault = codContabilitaDefault;
	}
	public String getCodTributoIuvDefault() {
		return this.codTributoIuvDefault;
	}
	public void setCodTributoIuvDefault(String codTributoIuvDefault) {
		this.codTributoIuvDefault = codTributoIuvDefault;
	}
	public boolean getOnlineDefault() {
		return onlineDefault;
	}
	public void setOnlineDefault(boolean onlineDefault) {
		this.onlineDefault = onlineDefault;
	}
	public boolean getPagaTerziDefault() {
		return pagaTerziDefault;
	}
	public void setPagaTerziDefault(boolean pagaTerziDefault) {
		this.pagaTerziDefault = pagaTerziDefault;
	}
}
