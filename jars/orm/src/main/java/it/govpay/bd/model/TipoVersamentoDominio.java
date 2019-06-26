package it.govpay.bd.model;

import org.openspcoop2.generic_project.exception.NotFoundException;
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
	public boolean isPromemoriaAvviso(){return this.getPromemoriaAvviso();}
	public boolean isPromemoria(){
		return this.getPromemoriaMessaggio() != null && this.getPromemoriaOggetto() != null;
	}
	
	public TipoVersamento getTipoVersamento(BasicBD bd) throws ServiceException {
		if(this.tipoVersamento == null) {
			try {
				this.tipoVersamento = AnagraficaManager.getTipoVersamento(bd, this.getIdTipoVersamento());
			} catch (NotFoundException e) {
			}
		} 
		return this.tipoVersamento;
	}

	public Dominio getDominio(BasicBD bd) throws ServiceException {
		if(this.dominio == null) {
			try {
				this.dominio = AnagraficaManager.getDominio(bd, this.getIdDominio());
			} catch (NotFoundException e) {
			}
		} 
		return this.dominio;
	}
	
	public Boolean getPromemoriaAvviso() {
		if(this.getPromemoriaAvvisoCustom() != null)
			return this.getPromemoriaAvvisoCustom();
		else 
			return this.getPromemoriaAvvisoDefault();
	} 
	
	public String getPromemoriaOggetto() {
		if(this.getPromemoriaOggettoCustom() != null)
			return this.getPromemoriaOggettoCustom();
		else 
			return this.getPromemoriaOggettoDefault();
	}
	
	public String getPromemoriaMessaggio() {
		if(this.getPromemoriaMessaggioCustom() != null)
			return this.getPromemoriaMessaggioCustom();
		else 
			return this.getPromemoriaMessaggioDefault();
	}
	
	public String getFormDefinizione() {
		if(this.getFormDefinizioneCustom() != null)
			return this.getFormDefinizioneCustom();
		else 
			return this.getFormDefinizioneDefault();
	}
	
	public String getFormTipo() {
		if(this.getFormTipoCustom() != null)
			return this.getFormTipoCustom();
		else 
			return this.getFormTipoDefault();
	}
	
	public String getValidazioneDefinizione() {
		if(this.getValidazioneDefinizioneCustom() != null)
			return this.getValidazioneDefinizioneCustom();
		else 
			return this.getValidazioneDefinizioneDefault();
	}
	
	public String getCodApplicazione() {
		if(this.getCodApplicazioneCustom() != null)
			return this.getCodApplicazioneCustom();
		else 
			return this.getCodApplicazioneDefault();
	}
	
	public String getTrasformazioneDefinizione() {
		if(this.getTrasformazioneDefinizioneCustom() != null)
			return this.getTrasformazioneDefinizioneCustom();
		else 
			return this.getTrasformazioneDefinizioneDefault();
	}
	
	public String getTrasformazioneTipo() {
		if(this.getTrasformazioneTipoCustom() != null)
			return this.getTrasformazioneTipoCustom();
		else 
			return this.getTrasformazioneTipoDefault();
	}
}
