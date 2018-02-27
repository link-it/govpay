package it.govpay.web.rs.dars.anagrafica.applicazioni.input;

import java.net.URI;
import java.util.List;
import java.util.Locale;

import org.apache.commons.lang.StringUtils;

import it.govpay.bd.BasicBD;
import it.govpay.bd.anagrafica.ApplicazioniBD;
import it.govpay.bd.model.Applicazione;
import it.govpay.web.rs.dars.model.RawParamValue;
import it.govpay.web.rs.dars.model.input.dinamic.CheckButton;
import it.govpay.web.utils.Utils;

public class Trusted extends CheckButton<Boolean>{
	
	private String versamentiId= null;
	private String applicazioneId = null;
	private String nomeServizio = null;

	public Trusted(String nomeServizio,String id, String label, URI refreshUri, List<RawParamValue> paramValues,Locale locale) {
		super(id, label, refreshUri, paramValues);
		this.nomeServizio = nomeServizio;
		this.versamentiId = Utils.getInstance(locale).getMessageFromResourceBundle(this.nomeServizio + ".versamenti.id");
		this.applicazioneId = Utils.getInstance(locale).getMessageFromResourceBundle(this.nomeServizio + ".id.id");
	}

	@Override
	protected Boolean getDefaultValue(List<RawParamValue> values, Object... objects) {
		
		String idPortale = Utils.getValue(values, this.applicazioneId);
		
		try {
			BasicBD bd = (BasicBD) objects[0];
			ApplicazioniBD applicazioniBD = new ApplicazioniBD(bd);
			Applicazione applicazione = applicazioniBD.getApplicazione(Long.parseLong(idPortale));
			return applicazione.isTrusted();
		} catch (Exception e) {
		}
		
		return false;
	}

	@Override
	protected boolean isRequired(List<RawParamValue> values, Object... objects) {
		return false;
	}

	@Override
	protected boolean isHidden(List<RawParamValue> values, Object... objects) {
		String versamentiValue = Utils.getValue(values, this.versamentiId);
		if(StringUtils.isNotEmpty(versamentiValue) && versamentiValue.equalsIgnoreCase("false")){
			return true;
		}

		return false;
	}
	@Override
	protected boolean isEditable(List<RawParamValue> values, Object... objects) {
		String versamentiValue = Utils.getValue(values, this.versamentiId);
		if(StringUtils.isNotEmpty(versamentiValue) && versamentiValue.equalsIgnoreCase("false")){
			return false;
		}
		return true;
	}
}
