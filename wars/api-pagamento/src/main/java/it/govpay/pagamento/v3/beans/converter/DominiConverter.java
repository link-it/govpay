package it.govpay.pagamento.v3.beans.converter;

import it.govpay.pagamento.v3.beans.Dominio;
import it.govpay.pagamento.v3.beans.UnitaOperativa;

public class DominiConverter {


	public static Dominio toRsModelIndex(it.govpay.bd.model.Dominio dominio) {
		if(dominio == null)
			return null;

		Dominio rsModel = new Dominio();
		rsModel.setIdDominio(dominio.getCodDominio());
		rsModel.setRagioneSociale(dominio.getRagioneSociale());
		return rsModel;
	}

	public static UnitaOperativa toRsModelIndex(it.govpay.bd.model.UnitaOperativa uo) {
		if(uo == null)
			return null;

		UnitaOperativa rsModel = new UnitaOperativa();
		rsModel.setIdUnita(uo.getCodUo());
		rsModel.setRagioneSociale(uo.getAnagrafica().getRagioneSociale());
		return rsModel;
	}
}
