package it.govpay.model;

import java.util.List;

public class Ruolo extends BasicModel {
	
	public static final int DIRITTI_LETTURA = 1;
	public static final int DIRITTI_SCRITTURA = 2;
	public static final int NO_DIRITTI = 0;
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private Long id;
	private String codRuolo;
	private String descrizione;
	private List<Acl> acls;
	
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public String getCodRuolo() {
		return codRuolo;
	}
	public void setCodRuolo(String codRuolo) {
		this.codRuolo = codRuolo;
	}
	public String getDescrizione() {
		return descrizione;
	}
	public void setDescrizione(String descrizione) {
		this.descrizione = descrizione;
	}
	public List<Acl> getAcls() {
		return acls;
	}
	public void setAcls(List<Acl> acls) {
		this.acls = acls;
	}
	
}
