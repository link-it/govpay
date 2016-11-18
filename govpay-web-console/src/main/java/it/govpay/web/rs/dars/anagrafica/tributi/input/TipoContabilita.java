package it.govpay.web.rs.dars.anagrafica.tributi.input;

import java.net.URI;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.openspcoop2.generic_project.exception.ServiceException;

import it.govpay.bd.BasicBD;
import it.govpay.bd.anagrafica.TipiTributoBD;
import it.govpay.model.TipoTributo;
import it.govpay.model.Tributo.TipoContabilta;
import it.govpay.web.rs.dars.model.RawParamValue;
import it.govpay.web.rs.dars.model.Voce;
import it.govpay.web.rs.dars.model.input.dinamic.SelectList;
import it.govpay.web.utils.Utils;

public class TipoContabilita extends SelectList<String>{

	private String idTipoTributoId= null;
	//	private String tributoId = null;
	private String nomeServizio = null;

	public TipoContabilita(String nomeServizio,String id, String label, URI refreshUri, List<RawParamValue> paramValues,
			Object... objects) {
		super(id, label, refreshUri, paramValues, objects);
		this.nomeServizio = nomeServizio;
		this.idTipoTributoId = Utils.getInstance().getMessageFromResourceBundle(this.nomeServizio + ".idTipoTributo.id");
		//		this.tributoId = Utils.getInstance().getMessageFromResourceBundle(this.nomeServizio + ".id.id");
	}

	@Override
	protected String getDefaultValue(List<RawParamValue> values, Object... objects) {
		String idTipoTributoValue = Utils.getValue(values, this.idTipoTributoId);
		//		String idTributo = Utils.getValue(values, this.tributoId);

		if(StringUtils.isEmpty(idTipoTributoValue)){
			return null;
		}

		try {
			BasicBD bd = (BasicBD) objects[0];
			TipiTributoBD tipiTributoBD = new TipiTributoBD(bd);
			TipoTributo tipoTributo = tipiTributoBD.getTipoTributo(Long.parseLong(idTipoTributoValue));
			TipoContabilta tipoContabilitaDefault = tipoTributo.getTipoContabilitaDefault();

			// prelevo il valore dal parent
			if(tipoContabilitaDefault != null) {
				return tipoContabilitaDefault.getCodifica() + "_p";
			}

		} catch (Exception e) {
			//throw new ServiceException(e);
		}

		return null;
	}

	@Override
	protected List<Voce<String>> getValues(List<RawParamValue> paramValues, Object... objects) throws ServiceException {
		String idTipoTributoValue = Utils.getValue(paramValues, this.idTipoTributoId);

		List<Voce<String>> lst = new ArrayList<Voce<String>>();
		lst.add(new Voce<String>(Utils.getInstance().getMessageFromResourceBundle(this.nomeServizio + ".tipoContabilita.capitolo"), TipoContabilta.CAPITOLO.getCodifica()));
		lst.add(new Voce<String>(Utils.getInstance().getMessageFromResourceBundle(this.nomeServizio + ".tipoContabilita.speciale"), TipoContabilta.SPECIALE.getCodifica()));
		lst.add(new Voce<String>(Utils.getInstance().getMessageFromResourceBundle(this.nomeServizio + ".tipoContabilita.siope"), TipoContabilta.SIOPE.getCodifica()));
		lst.add(new Voce<String>(Utils.getInstance().getMessageFromResourceBundle(this.nomeServizio + ".tipoContabilita.altro"), TipoContabilta.ALTRO.getCodifica()));

		if(StringUtils.isEmpty(idTipoTributoValue)){
			return lst;
		}

		try{
			BasicBD bd = (BasicBD) objects[0];
			TipiTributoBD tipiTributoBD = new TipiTributoBD(bd);
			TipoTributo tipoTributo = tipiTributoBD.getTipoTributo(Long.parseLong(idTipoTributoValue));

			TipoContabilta tipoContabilitaDefault = tipoTributo.getTipoContabilitaDefault();
			if(tipoContabilitaDefault != null) {
				switch(tipoContabilitaDefault){
				case ALTRO:
					lst.add(0,new Voce<String>(Utils.getInstance().getMessageFromResourceBundle(this.nomeServizio + ".tipoContabilita.altro.default"), TipoContabilta.ALTRO.getCodifica() + "_p"));
					break;
				case SIOPE:
					lst.add(0,new Voce<String>(Utils.getInstance().getMessageFromResourceBundle(this.nomeServizio + ".tipoContabilita.siope.default"), TipoContabilta.SIOPE.getCodifica() + "_p"));
					break;
				case SPECIALE:
					lst.add(0,new Voce<String>(Utils.getInstance().getMessageFromResourceBundle(this.nomeServizio + ".tipoContabilita.speciale.default"), TipoContabilta.SPECIALE.getCodifica() + "_p"));
					break;
				case CAPITOLO:
				default:
					lst.add(0,new Voce<String>(Utils.getInstance().getMessageFromResourceBundle(this.nomeServizio + ".tipoContabilita.capitolo.default"), TipoContabilta.CAPITOLO.getCodifica() + "_p"));
					break;
				}
			} 
		}catch(Exception e){return lst;}
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
		String idTipoTributoValue = Utils.getValue(values, this.idTipoTributoId);
		if(StringUtils.isNotEmpty(idTipoTributoValue)){
			return true;
		}

		return false;
	}
}
