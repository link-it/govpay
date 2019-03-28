package it.govpay.model;

import org.apache.commons.lang.ArrayUtils;
import org.openspcoop2.generic_project.exception.ServiceException;

public class TipoVersamento extends BasicModel {
	private static final long serialVersionUID = 1L;
	
	public enum Tipo {
	    SPONTANEO("SPONTANEO"),
	    DOVUTO("DOVUTO");
	    
		private String codifica;

		Tipo(String codifica) {
			this.codifica = codifica;
		}
		public String getCodifica() {
			return this.codifica;
		}
		
		public static Tipo toEnum(String codifica) throws ServiceException {
			for(Tipo p : Tipo.values()){
				if(p.getCodifica().equals(codifica))
					return p;
			}
			throw new ServiceException("Codifica inesistente per Tipo. Valore fornito [" + codifica + "] valori possibili " + ArrayUtils.toString(Tipo.values()));
		}
	}
	
	
	
	private Long id; 
	private String codTipoVersamento;
	private String descrizione;
	private String codificaIuvDefault;
	private Tipo tipoDefault;
	private boolean pagaTerziDefault;
	private boolean abilitatoDefault;
	
	public String getCodificaIuvDefault() {
		return codificaIuvDefault;
	}
	public void setCodificaIuvDefault(String codificaIuvDefault) {
		this.codificaIuvDefault = codificaIuvDefault;
	}
	public Tipo getTipoDefault() {
		return tipoDefault;
	}
	public void setTipoDefault(Tipo tipoDefault) {
		this.tipoDefault = tipoDefault;
	}
	public boolean getPagaTerziDefault() {
		return pagaTerziDefault;
	}
	public void setPagaTerziDefault(boolean pagaTerziDefault) {
		this.pagaTerziDefault = pagaTerziDefault;
	}
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
	public boolean isAbilitatoDefault() {
		return abilitatoDefault;
	}
	public void setAbilitatoDefault(boolean abilitatoDefault) {
		this.abilitatoDefault = abilitatoDefault;
	}
}
