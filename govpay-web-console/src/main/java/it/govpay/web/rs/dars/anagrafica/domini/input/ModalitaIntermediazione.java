package it.govpay.web.rs.dars.anagrafica.domini.input;

import java.net.URI;
import java.util.ArrayList;
import java.util.List;

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
		this.nomeServizio = nomeServizio;
		this.dominioId = Utils.getInstance().getMessageFromResourceBundle(this.nomeServizio + ".id.id");
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
		List<Voce<Integer>> lst = new ArrayList<Voce<Integer>>();
		lst.add(new Voce<Integer>(Utils.getInstance().getMessageFromResourceBundle(this.nomeServizio + ".modalitaIntermediazione.0"), 0));
		lst.add(new Voce<Integer>(Utils.getInstance().getMessageFromResourceBundle(this.nomeServizio + ".modalitaIntermediazione.3"), 3));
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
