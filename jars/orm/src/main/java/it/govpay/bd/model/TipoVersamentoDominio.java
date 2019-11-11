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
	
	public Boolean getAbilitato() {
		if(this.getAbilitatoCustom() != null)
			return this.getAbilitatoCustom();
		else 
			return this.isAbilitatoDefault();
	} 

	public boolean isCodificaIuvCustom(){return this.getCodificaIuvCustom() != null;}
	public boolean isTipoCustom(){return this.getTipoCustom() != null;}
	public boolean isPagaTerziCustom(){return this.getPagaTerziCustom() != null;}
	public boolean isPagaTerzi(){return this.getPagaTerzi();}
	public boolean isPromemoriaAvvisoAbilitato(){return this.getPromemoriaAvvisoAbilitato();}
	public boolean isPromemoriaRicevutaAbilitato(){return this.getPromemoriaRicevutaAbilitato();}
	
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
	
	public Boolean getPromemoriaAvvisoAbilitato() {
		if(this.getPromemoriaAvvisoAbilitatoCustom() != null)
			return this.getPromemoriaAvvisoAbilitatoCustom();
		else 
			return this.isPromemoriaAvvisoAbilitatoDefault();
	}
	
	public Boolean getPromemoriaAvvisoPdf() {
		if(this.getPromemoriaAvvisoPdfCustom() != null)
			return this.getPromemoriaAvvisoPdfCustom();
		else 
			return this.getPromemoriaAvvisoPdfDefault();
	} 
	
	public String getPromemoriaAvvisoTipo() {
		if(this.getPromemoriaAvvisoTipoCustom() != null)
			return this.getPromemoriaAvvisoTipoCustom();
		else 
			return this.getPromemoriaAvvisoTipoDefault();
	}
	
	public String getPromemoriaAvvisoOggetto() {
		if(this.getPromemoriaAvvisoOggettoCustom() != null)
			return this.getPromemoriaAvvisoOggettoCustom();
		else 
			return this.getPromemoriaAvvisoOggettoDefault();
	}
	
	public String getPromemoriaAvvisoMessaggio() {
		if(this.getPromemoriaAvvisoMessaggioCustom() != null)
			return this.getPromemoriaAvvisoMessaggioCustom();
		else 
			return this.getPromemoriaAvvisoMessaggioDefault();
	}
	
	public Boolean getPromemoriaRicevutaAbilitato() {
		if(this.getPromemoriaRicevutaAbilitatoCustom() != null)
			return this.getPromemoriaRicevutaAbilitatoCustom();
		else 
			return this.isPromemoriaRicevutaAbilitatoDefault();
	}
	
	public String getPromemoriaRicevutaTipo() {
		if(this.getPromemoriaRicevutaTipoCustom() != null)
			return this.getPromemoriaRicevutaTipoCustom();
		else 
			return this.getPromemoriaRicevutaTipoDefault();
	}
	
	public Boolean getPromemoriaRicevutaPdf() {
		if(this.getPromemoriaRicevutaPdfCustom() != null)
			return this.getPromemoriaRicevutaPdfCustom();
		else 
			return this.getPromemoriaRicevutaPdfDefault();
	} 
	
	public String getPromemoriaRicevutaOggetto() {
		if(this.getPromemoriaRicevutaOggettoCustom() != null)
			return this.getPromemoriaRicevutaOggettoCustom();
		else 
			return this.getPromemoriaRicevutaOggettoDefault();
	}
	
	public String getPromemoriaRicevutaMessaggio() {
		if(this.getPromemoriaRicevutaMessaggioCustom() != null)
			return this.getPromemoriaRicevutaMessaggioCustom();
		else 
			return this.getPromemoriaRicevutaMessaggioDefault();
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
	
	public String getVisualizzazioneDefinizione() {
		if(this.getVisualizzazioneDefinizioneCustom() != null)
			return this.getVisualizzazioneDefinizioneCustom();
		else 
			return this.getVisualizzazioneDefinizioneDefault();
	}
	
	public String getTracciatoCsvTipo() {
		if(this.getTracciatoCsvTipoCustom() != null)
			return this.getTracciatoCsvTipoCustom();
		else 
			return this.getTracciatoCsvTipoDefault();
	}
	
	public String getTracciatoCsvIntestazione() {
		if(this.getTracciatoCsvIntestazioneCustom() != null)
			return this.getTracciatoCsvIntestazioneCustom();
		else 
			return this.getTracciatoCsvIntestazioneDefault();
	}
	
	public String getTracciatoCsvRichiesta() {
		if(this.getTracciatoCsvRichiestaCustom() != null)
			return this.getTracciatoCsvRichiestaCustom();
		else 
			return this.getTracciatoCsvRichiestaDefault();
	}
	
	public String getTracciatoCsvRisposta() {
		if(this.getTracciatoCsvRispostaCustom() != null)
			return this.getTracciatoCsvRispostaCustom();
		else 
			return this.getTracciatoCsvRispostaDefault();
	}
}
