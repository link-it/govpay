package it.govpay.rs.v1.beans.pendenze.converter;

import org.apache.commons.codec.binary.Base64;
import org.apache.commons.codec.binary.StringUtils;
import org.openspcoop2.generic_project.exception.ServiceException;

import it.govpay.core.rs.v1.beans.pagamenti.ContiAccredito;
import it.govpay.core.rs.v1.beans.pendenze.DominioIndex;
//import it.govpay.core.rs.v1.beans.pendenze.Entrata;
//import it.govpay.core.rs.v1.beans.pendenze.Iban;
import it.govpay.core.rs.v1.beans.pendenze.UnitaOperativa;
import it.govpay.core.utils.UriBuilderUtils;

public class DominiConverter {

	public static DominioIndex toRsIndexModel(it.govpay.bd.model.Dominio dominio) throws ServiceException {
		DominioIndex rsModel = new DominioIndex();
		rsModel.setWeb(dominio.getAnagrafica().getUrlSitoWeb());
		rsModel.setIdDominio(dominio.getCodDominio()); 
		rsModel.setRagioneSociale(dominio.getRagioneSociale());
		rsModel.setIndirizzo(dominio.getAnagrafica().getIndirizzo());
		rsModel.setCivico(dominio.getAnagrafica().getCivico());
		rsModel.setCap(dominio.getAnagrafica().getCap());
		rsModel.setLocalita(dominio.getAnagrafica().getLocalita());
		rsModel.setProvincia(dominio.getAnagrafica().getProvincia());
		rsModel.setNazione(dominio.getAnagrafica().getNazione());
		rsModel.setEmail(dominio.getAnagrafica().getEmail());
		rsModel.setTel(dominio.getAnagrafica().getTelefono());
		rsModel.setFax(dominio.getAnagrafica().getFax());
		rsModel.setGln(dominio.getGln());
		rsModel.setAuxDigit("" + dominio.getAuxDigit());
		rsModel.setSegregationCode("" + dominio.getSegregationCode());
		if(dominio.getLogo() != null) {
			StringBuilder sb = new StringBuilder();
			sb.append(StringUtils.newStringUtf8(Base64.encodeBase64(dominio.getLogo(), false)));
			rsModel.setLogo(sb.toString());
		}
		rsModel.setIuvPrefix(dominio.getIuvPrefix());
		rsModel.setStazione(dominio.getStazione().getCodStazione());
		rsModel.setUnitaOperative(UriBuilderUtils.getListUoByDominio(dominio.getCodDominio()));
		rsModel.setContiAccredito(UriBuilderUtils.getContiAccreditoByDominio(dominio.getCodDominio()));
		rsModel.setEntrate(UriBuilderUtils.getEntrateByDominio(dominio.getCodDominio()));
		rsModel.setAbilitato(dominio.isAbilitato());
		
		return rsModel;
	}
	
	public static ContiAccredito toIbanRsModel(it.govpay.bd.model.IbanAccredito iban) throws ServiceException {
		ContiAccredito rsModel = new ContiAccredito();
		rsModel.abilitato(iban.isAbilitato())
		.bic(iban.getCodBic())
		.iban(iban.getCodIban())
		.mybank(iban.isAttivatoObep())
		.postale(iban.isPostale());
		
		return rsModel;
	}
	
	
	public static UnitaOperativa toUnitaOperativaRsModel(it.govpay.bd.model.UnitaOperativa uo) throws IllegalArgumentException, ServiceException {
		UnitaOperativa rsModel = new UnitaOperativa();
		
		rsModel.setCap(uo.getAnagrafica().getRagioneSociale());
		rsModel.setCivico(uo.getAnagrafica().getCivico());
		rsModel.setIdUnita(uo.getAnagrafica().getCodUnivoco());
		rsModel.setIndirizzo(uo.getAnagrafica().getIndirizzo());
		rsModel.setLocalita(uo.getAnagrafica().getLocalita());
		rsModel.setRagioneSociale(uo.getAnagrafica().getRagioneSociale());
		
		return rsModel;
	}
	
}
