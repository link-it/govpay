package it.govpay.web.rs.dars.anagrafica.portali.input;

import java.net.URI;
import java.util.List;

import org.apache.commons.lang.StringUtils;

import it.govpay.bd.BasicBD;
import it.govpay.bd.anagrafica.PortaliBD;
import it.govpay.model.Portale;
import it.govpay.web.rs.dars.model.RawParamValue;
import it.govpay.web.rs.dars.model.input.dinamic.CheckButton;
import it.govpay.web.utils.Utils;

public class Trusted extends CheckButton<Boolean>{
	
	private String pagamentiOnlineId= null;
	private String portaleId = null;
	private String nomeServizio = null;

	public Trusted(String nomeServizio,String id, String label, URI refreshUri, List<RawParamValue> paramValues) {
		super(id, label, refreshUri, paramValues);
		this.nomeServizio = nomeServizio;
		this.pagamentiOnlineId = Utils.getInstance().getMessageFromResourceBundle(this.nomeServizio + ".pagamentiOnline.id");
		this.portaleId = Utils.getInstance().getMessageFromResourceBundle(this.nomeServizio + ".id.id");
	}

	@Override
	protected Boolean getDefaultValue(List<RawParamValue> values, Object... objects) {
		
		String idPortale = Utils.getValue(values, this.portaleId);
		
		try {
			BasicBD bd = (BasicBD) objects[0];
			PortaliBD portaliBD = new PortaliBD(bd);
			Portale portale = portaliBD.getPortale(Long.parseLong(idPortale));
		
			return portale.isTrusted();
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
		String pagamentiOnlineValue = Utils.getValue(values, this.pagamentiOnlineId);
		if(StringUtils.isNotEmpty(pagamentiOnlineValue) && pagamentiOnlineValue.equalsIgnoreCase("false")){
			return true;
		}

		return false;
	}
	@Override
	protected boolean isEditable(List<RawParamValue> values, Object... objects) {
		String pagamentiOnlineValue = Utils.getValue(values, this.pagamentiOnlineId);
		if(StringUtils.isNotEmpty(pagamentiOnlineValue) && pagamentiOnlineValue.equalsIgnoreCase("false")){
			return false;
		}
		return true;
	}
}
