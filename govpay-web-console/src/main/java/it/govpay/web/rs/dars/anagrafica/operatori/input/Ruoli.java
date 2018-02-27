package it.govpay.web.rs.dars.anagrafica.operatori.input;

import java.net.URI;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import org.apache.commons.lang.StringUtils;
import org.openspcoop2.generic_project.exception.ServiceException;

import it.govpay.bd.BasicBD;
import it.govpay.bd.anagrafica.OperatoriBD;
import it.govpay.bd.anagrafica.RuoliBD;
import it.govpay.bd.anagrafica.filters.RuoloFilter;
import it.govpay.bd.model.Operatore;
import it.govpay.web.rs.dars.anagrafica.ruoli.RuoliHandler;
import it.govpay.web.rs.dars.model.RawParamValue;
import it.govpay.web.rs.dars.model.Voce;
import it.govpay.web.rs.dars.model.input.dinamic.MultiSelectList;
import it.govpay.web.utils.Utils;

public class Ruoli extends MultiSelectList<String, List<String>>{

	private String operatoreId = null;
	private String nomeServizio = null;

	public Ruoli(String nomeServizio,String id, String label, URI refreshUri, List<RawParamValue> paramValues,
			 Object... objects) {
		super(id, label, refreshUri, paramValues, objects);
		Locale locale = objects[1] != null ? (Locale) objects[1] : null;
		this.nomeServizio = nomeServizio;
		this.operatoreId = Utils.getInstance(locale).getMessageFromResourceBundle(this.nomeServizio + ".id.id");
	}

	@Override
	protected List<Voce<String>> getValues(List<RawParamValue> paramValues, Object... objects) throws ServiceException {
		List<Voce<String>> lst = new ArrayList<Voce<String>>();

		try{
			Locale locale = objects[1] != null ? (Locale) objects[1] : null;
			BasicBD bd = (BasicBD) objects[0];
			RuoliBD ruoliBD = new RuoliBD(bd);
			RuoloFilter ruoliFilter = ruoliBD.newFilter();

			List<it.govpay.model.Ruolo> findAll = ruoliBD.findAll(ruoliFilter);

			if(findAll != null && findAll.size() > 0){
				it.govpay.web.rs.dars.anagrafica.ruoli.Ruoli ruoliDars = new it.govpay.web.rs.dars.anagrafica.ruoli.Ruoli();
				RuoliHandler ruoliDarsHandler = (RuoliHandler) ruoliDars.getDarsHandler();
				for (it.govpay.model.Ruolo ruolo : findAll) {
					lst.add(new Voce<String>(ruoliDarsHandler.getTitolo(ruolo, bd), ruolo.getCodRuolo()));   
				}
				lst.add(new Voce<String>(Utils.getInstance(locale).getMessageFromResourceBundle(this.nomeServizio + ".ruoli."+Operatore.RUOLO_SYSTEM+".label"),
						Operatore.RUOLO_SYSTEM));
			}

		}catch(Exception e){
			throw new ServiceException(e);
		}

		return lst;
	}

	@Override
	protected List<String> getDefaultValue(List<RawParamValue> values, Object... objects) {
		String idOperatore = Utils.getValue(values, this.operatoreId);
		List<String> lst = new ArrayList<String>();

		if(StringUtils.isEmpty(idOperatore)){
			return lst;
		}

		try {
			BasicBD bd = (BasicBD) objects[0];
			OperatoriBD operatoriBD = new OperatoriBD(bd);
			Operatore operatore = operatoriBD.getOperatore(Long.parseLong(idOperatore));

			lst = operatore.getRuoli()!= null ? operatore.getRuoli() : new ArrayList<String>();
		} catch (Exception e) {
		}

		return lst;
	}
	@Override
	protected boolean isRequired(List<RawParamValue> values, Object... objects) {
		return true;
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