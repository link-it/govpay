package it.govpay.bd.model;

import org.openspcoop2.generic_project.exception.ServiceException;

import it.govpay.bd.BasicBD;
import it.govpay.bd.anagrafica.AnagraficaManager;
import it.govpay.model.TipoVersamento;

public class TipoVersamentoDominio extends it.govpay.model.TipoVersamentoDominio{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private transient Dominio dominio;
	private transient TipoVersamento tipoVersamento;
	
	public String getCodificaIuv() {
		if(this.getCodificaIuvCustom() != null)
			return this.getCodificaIuvCustom();
		else 
			return this.getCodificaIuvDefault();
	}
	
	public Tipo getTipo() {
		if(this.getTipoCustom() != null)
			return this.getTipoCustom();
		else 
			return this.getTipoDefault();
	} 
	
	public Boolean getPagaTerzi() {
		if(this.getPagaTerziCustom() != null)
			return this.getPagaTerziCustom();
		else 
			return this.getPagaTerziDefault();
	} 

	public boolean isCodificaIuvCustom(){return this.getCodificaIuvCustom() != null;}
	public boolean isTipoCustom(){return this.getTipoCustom() != null;}
	public boolean isPagaTerziCustom(){return this.getPagaTerziCustom() != null;}
	public boolean isPagaTerzi(){return this.getPagaTerzi();}
	
	public TipoVersamento getTipoVersamento(BasicBD bd) throws ServiceException {
		if(this.tipoVersamento == null) {
			this.tipoVersamento = AnagraficaManager.getTipoVersamento(bd, this.getIdTipoVersamento());
		} 
		return this.tipoVersamento;
	}

	public Dominio getDominio(BasicBD bd) throws ServiceException {
		if(this.dominio == null) {
			this.dominio = AnagraficaManager.getDominio(bd, this.getIdDominio());
		} 
		return this.dominio;
	}
}
