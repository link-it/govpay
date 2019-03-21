package it.govpay.model;

public class TipoVersamento extends BasicModel {
	private static final long serialVersionUID = 1L;
	
	private Long id; 
	private String codTipoVersamento;
	private String descrizione;
//	private TipoContabilita tipoContabilitaDefault;
//	private String codContabilitaDefault;
//	private String codTributoIuvDefault;
//	private boolean onlineDefault;
//	private boolean pagaTerziDefault;
	
	@Override
	public Long getId() {
		return this.id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public String getCodTipoVersamento() {
		return codTipoVersamento;
	}
	public void setCodTipoVersamento(String codTipoVersamento) {
		this.codTipoVersamento = codTipoVersamento;
	}
	public String getDescrizione() {
		return this.descrizione;
	}
	public void setDescrizione(String descrizione) {
		this.descrizione = descrizione;
	}
}
