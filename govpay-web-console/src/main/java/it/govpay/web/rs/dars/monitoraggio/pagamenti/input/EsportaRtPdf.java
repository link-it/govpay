package it.govpay.web.rs.dars.monitoraggio.pagamenti.input;

import java.net.URI;
import java.util.List;
import java.util.Locale;

import it.govpay.web.rs.dars.model.RawParamValue;
import it.govpay.web.rs.dars.model.input.dinamic.CheckButton;
import it.govpay.web.utils.ConsoleProperties;
import it.govpay.web.utils.Utils;

public class EsportaRtPdf extends CheckButton<Boolean>{
	
	private String esportaRtPdfId = null;
	private String nomeServizio = null;

	public EsportaRtPdf(String nomeServizio,String id, String label, URI refreshUri, List<RawParamValue> paramValues,Locale locale) {
		super(id, label, refreshUri, paramValues);
		this.nomeServizio = nomeServizio;
		this.esportaRtPdfId = Utils.getInstance(locale).getMessageFromResourceBundle(this.nomeServizio + ".esportaRtPdf.id");
	}

	@Override
	protected Boolean getDefaultValue(List<RawParamValue> values, Object... objects) {
		Locale locale = objects[1] != null ? (Locale) objects[1] : null;
		String esportaRtPdf = Utils.getValue(values, this.esportaRtPdfId);
		boolean toRet = false;
		try {
			toRet = Boolean.parseBoolean(esportaRtPdf);
		} catch (Exception e) {
		}
		
		if(toRet){
			int numeroMassimoElementiExport = ConsoleProperties.getInstance().getNumeroMassimoElementiExport();
			this.setNote(Utils.getInstance(locale).getMessageWithParamsFromResourceBundle(this.nomeServizio + ".esportaRtPdf.note",""+numeroMassimoElementiExport));
		} else {
			this.setNote(null); 
		}
		
		return toRet;
	}

	@Override
	protected boolean isRequired(List<RawParamValue> values, Object... objects) {
		return false;
	}

	@Override
	protected boolean isHidden(List<RawParamValue> values, Object... objects) {
		return false;
	}
	@Override
	protected boolean isEditable(List<RawParamValue> values, Object... objects) {
		return true;
	}

}
