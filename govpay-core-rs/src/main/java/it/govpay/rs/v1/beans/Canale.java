package it.govpay.rs.v1.beans;

import org.openspcoop2.generic_project.exception.ServiceException;

import it.govpay.bd.BasicBD;
import it.govpay.core.utils.UriBuilderUtils;
import it.govpay.rs.v1.beans.base.ModelloPagamento;
import it.govpay.rs.v1.beans.base.TipoVersamento;

public class Canale extends it.govpay.rs.v1.beans.base.Canale {

	@Override
	public String getJsonIdFilter() {
		return "canale";
	}
	
	public static Canale parse(String json) {
		return (Canale) parse(json, Canale.class);
	}
	
	public Canale(it.govpay.bd.model.Canale canale, BasicBD bd) throws ServiceException {
		this.setAbilitato(canale.isAbilitato());
		this.setIdCanale(canale.getCodCanale());
		this.setModelloPagamento(ModelloPagamento.fromValue(canale.getModelloPagamento().toString()));
		this.setPsp(UriBuilderUtils.getPsp(canale.getPsp(bd).getCodPsp()));
		this.setTipoVersamento(TipoVersamento.fromValue(canale.getTipoVersamento().toString()));
	}
}
