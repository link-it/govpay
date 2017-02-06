package it.govpay.web.rs.dars.anagrafica.domini.input;

import java.net.URI;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import org.apache.commons.lang.StringUtils;
import org.openspcoop2.generic_project.exception.ServiceException;

import it.govpay.bd.BasicBD;
import it.govpay.bd.anagrafica.DominiBD;
import it.govpay.bd.model.Dominio;
import it.govpay.web.rs.dars.model.RawParamValue;
import it.govpay.web.rs.dars.model.Voce;
import it.govpay.web.rs.dars.model.input.dinamic.SelectList;
import it.govpay.web.utils.Utils;

public class ModalitaIntermediazione extends SelectList<Integer>{

	private String dominioId = null;
	private String nomeServizio = null;

	public ModalitaIntermediazione(String nomeServizio,String id, String label, URI refreshUri, List<RawParamValue> paramValues,
			Object... objects) {
		super(id, label, refreshUri, paramValues, objects);
		Locale locale = objects[1] != null ? (Locale) objects[1] : null;
		this.nomeServizio = nomeServizio;
//		this.idStazioneId = Utils.getInstance(locale).getMessageFromResourceBundle(this.nomeServizio + ".idStazione.id");
		this.dominioId = Utils.getInstance(locale).getMessageFromResourceBundle(this.nomeServizio + ".id.id");
	}

	@Override
	protected Integer getDefaultValue(List<RawParamValue> values, Object... objects) {
		String idDominio = Utils.getValue(values, this.dominioId);

		if(StringUtils.isEmpty(idDominio)){
			return 0;
		}

		try {
			BasicBD bd = (BasicBD) objects[0];
			DominiBD dominiBD = new DominiBD(bd);
			Dominio dominio= dominiBD.getDominio(Long.parseLong(idDominio));
			return dominio.getAuxDigit(); 
		} catch (Exception e) {
			//throw new ServiceException(e);
		}

		return 0;
	}
	
	@Override
	protected List<Voce<Integer>> getValues(List<RawParamValue> paramValues, Object... objects) throws ServiceException {
		Locale locale = objects[1] != null ? (Locale) objects[1] : null;
		List<Voce<Integer>> lst = new ArrayList<Voce<Integer>>();
		lst.add(new Voce<Integer>(Utils.getInstance(locale).getMessageFromResourceBundle(this.nomeServizio + ".modalitaIntermediazione.0"), 0));
		lst.add(new Voce<Integer>(Utils.getInstance(locale).getMessageFromResourceBundle(this.nomeServizio + ".modalitaIntermediazione.3"), 3));
//		String idStazioneValue = Utils.getValue(paramValues, this.idStazioneId);
//
//		Locale locale = objects[1] != null ? (Locale) objects[1] : null;
//		List<Voce<Integer>> lst = new ArrayList<Voce<Integer>>();
//		lst.add(new Voce<Integer>(Utils.getInstance(locale).getMessageFromResourceBundle(this.nomeServizio + ".modalitaIntermediazione.0"), 0));
//
//		if(StringUtils.isEmpty(idStazioneValue)){
//			return lst;
//		}
//
//		try{
//			BasicBD bd = (BasicBD) objects[0];
//			StazioniBD stazioniBD = new StazioniBD(bd);
//			Stazione stazione = stazioniBD.getStazione(Long.parseLong(idStazioneValue));
//			Intermediario intermediario = stazione.getIntermediario(bd);
//
//			if(intermediario.getSegregationCode()!= null)
//				lst.add(new Voce<Integer>(Utils.getInstance(locale).getMessageFromResourceBundle(this.nomeServizio + ".modalitaIntermediazione.3"), 3));
//
//		}catch(Exception e){return lst;}
		return lst;
	}


	@Override
	protected boolean isHidden(List<RawParamValue> values, Object... objects) {
		return false;
	}


	@Override
	protected boolean isRequired(List<RawParamValue> values, Object... objects) {
		return false;
	}

	@Override
	protected boolean isEditable(List<RawParamValue> values, Object... objects) {
		return true;
	}
}
