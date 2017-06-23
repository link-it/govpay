package it.govpay.web.rs.dars.anagrafica.ruoli.input;

import java.net.URI;
import java.util.List;
import java.util.Locale;

import org.apache.commons.lang.StringUtils;

import it.govpay.bd.BasicBD;
import it.govpay.bd.anagrafica.RuoliBD;
import it.govpay.model.Acl;
import it.govpay.model.Acl.Servizio;
import it.govpay.model.Acl.Tipo;
import it.govpay.model.Ruolo;
import it.govpay.web.rs.dars.model.RawParamValue;
import it.govpay.web.rs.dars.model.input.dinamic.CheckButton;
import it.govpay.web.utils.Utils;

public class AdminFunzionalita_G_PAG extends CheckButton<Boolean>{
	
	private String admin_G_PAGId= null;
	private String ruoloId = null;
	private String nomeServizio = null;
	private Servizio servizio = Servizio.Gestione_Pagamenti;
	private Tipo tipo = Tipo.DOMINIO;

	public AdminFunzionalita_G_PAG(String nomeServizio,String id, String label, URI refreshUri, List<RawParamValue> paramValues,Locale locale) {
		super(id, label, refreshUri, paramValues);
		this.nomeServizio = nomeServizio;
		this.admin_G_PAGId = Utils.getInstance(locale).getMessageFromResourceBundle(this.nomeServizio + ".admin_G_PAG.id");
		this.ruoloId = Utils.getInstance(locale).getMessageFromResourceBundle(this.nomeServizio + ".id.id");
	}

	@Override
	protected Boolean getDefaultValue(List<RawParamValue> values, Object... objects) {
		String admin_G_PAGVAlue = Utils.getValue(values, this.admin_G_PAGId);
		String idRuolo = Utils.getValue(values, this.ruoloId);

		if(StringUtils.isNotEmpty(admin_G_PAGVAlue) && admin_G_PAGVAlue.equalsIgnoreCase("false")){
			return false;
		}
		if(StringUtils.isEmpty(idRuolo)){
			return false;
		}

		try {
			BasicBD bd = (BasicBD) objects[0];
			RuoliBD ruoliBD = new RuoliBD(bd);
			Ruolo portale = ruoliBD.getRuolo(Long.parseLong(idRuolo));
			List<Acl> acls = portale.getAcls();
			
			for (Acl acl : acls) {
				Tipo tipo = acl.getTipo();
				if(acl.getServizio().equals(this.servizio) && tipo.equals(this.tipo)){
					if(acl.isAdmin())
						return true;
				}
			}
			
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
		String admin_G_PAGVAlue = Utils.getValue(values, this.admin_G_PAGId);
		if(StringUtils.isNotEmpty(admin_G_PAGVAlue) && admin_G_PAGVAlue.equalsIgnoreCase("false")){
			return true;
		}

		return false;
	}
	@Override
	protected boolean isEditable(List<RawParamValue> values, Object... objects) {
		String admin_G_PAGVAlue = Utils.getValue(values, this.admin_G_PAGId);
		if(StringUtils.isNotEmpty(admin_G_PAGVAlue) && admin_G_PAGVAlue.equalsIgnoreCase("false")){
			return false;
		}
		return true;
	}

}
