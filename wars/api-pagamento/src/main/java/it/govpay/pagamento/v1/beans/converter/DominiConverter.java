package it.govpay.pagamento.v1.beans.converter;

import java.util.ArrayList;
import java.util.List;

import org.openspcoop2.generic_project.exception.ServiceException;

import it.govpay.bd.model.Tributo;
import it.govpay.core.utils.UriBuilderUtils;
import it.govpay.pagamento.v1.beans.ContiAccredito;
import it.govpay.pagamento.v1.beans.Dominio;
import it.govpay.pagamento.v1.beans.DominioIndex;
import it.govpay.pagamento.v1.beans.Entrata;
import it.govpay.pagamento.v1.beans.TipoContabilita;
import it.govpay.pagamento.v1.beans.UnitaOperativa;

public class DominiConverter {
	
	public static DominioIndex toRsModelIndex(it.govpay.bd.model.Dominio dominio) throws ServiceException {
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
		rsModel.setPec(dominio.getAnagrafica().getPec());
		rsModel.setTel(dominio.getAnagrafica().getTelefono());
		rsModel.setFax(dominio.getAnagrafica().getFax());
		rsModel.setGln(dominio.getGln());
		rsModel.setAuxDigit("" + dominio.getAuxDigit());
		if(dominio.getSegregationCode() != null)
			rsModel.setSegregationCode(String.format("%02d", dominio.getSegregationCode()));
		if(dominio.getLogo() != null) {
			rsModel.setLogo(UriBuilderUtils.getLogoDominio(dominio.getCodDominio()));
		}
		rsModel.setIuvPrefix(dominio.getIuvPrefix());
		rsModel.setStazione(dominio.getStazione().getCodStazione());
		rsModel.setContiAccredito(UriBuilderUtils.getContiAccreditoByDominio(dominio.getCodDominio()));
		rsModel.setUnitaOperative(UriBuilderUtils.getListUoByDominio(dominio.getCodDominio()));
		rsModel.setEntrate(UriBuilderUtils.getEntrateByDominio(dominio.getCodDominio()));
		rsModel.setAbilitato(dominio.isAbilitato());
		
		return rsModel;
	}
	
	public static Dominio toRsModel(it.govpay.bd.model.Dominio dominio, List<it.govpay.bd.model.UnitaOperativa> uoLst, List<it.govpay.bd.model.Tributo> tributoLst, List<it.govpay.bd.model.IbanAccredito> ibanAccreditoLst) throws ServiceException {
		Dominio rsModel = new Dominio();
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
		rsModel.setPec(dominio.getAnagrafica().getPec());
		rsModel.setTel(dominio.getAnagrafica().getTelefono());
		rsModel.setFax(dominio.getAnagrafica().getFax());
		rsModel.setGln(dominio.getGln());
		rsModel.setAuxDigit("" + dominio.getAuxDigit());
		if(dominio.getSegregationCode() != null)
			rsModel.setSegregationCode(String.format("%02d", dominio.getSegregationCode()));
		if(dominio.getLogo() != null) {
			rsModel.setLogo(UriBuilderUtils.getLogoDominio(dominio.getCodDominio()));
		}
		rsModel.setIuvPrefix(dominio.getIuvPrefix());
		rsModel.setStazione(dominio.getStazione().getCodStazione());
		
		if(uoLst != null) {
			List<UnitaOperativa> unitaOperative = new ArrayList<>();
			
			for(it.govpay.bd.model.UnitaOperativa uo: uoLst) {
				unitaOperative.add(toUnitaOperativaRsModel(uo));
			}
			rsModel.setUnitaOperative(unitaOperative);
		}

		if(ibanAccreditoLst != null) {
			List<ContiAccredito> contiAccredito = new ArrayList<>();
			
			for(it.govpay.bd.model.IbanAccredito iban: ibanAccreditoLst) {
				contiAccredito.add(toIbanRsModel(iban));
			}
			rsModel.setContiAccredito(contiAccredito);
		}

		if(tributoLst != null) {
			List<Entrata> entrate = new ArrayList<>();
			
			for(Tributo tributo: tributoLst) {
				entrate.add(toEntrataRsModel(tributo, tributo.getIbanAccredito()));
			}
			rsModel.setEntrate(entrate);
		}
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
		rsModel.setIdUnita(uo.getCodUo());
		rsModel.setIndirizzo(uo.getAnagrafica().getIndirizzo());
		rsModel.setLocalita(uo.getAnagrafica().getLocalita());
		rsModel.setRagioneSociale(uo.getAnagrafica().getRagioneSociale());
		rsModel.setWeb(uo.getAnagrafica().getUrlSitoWeb());
		rsModel.setProvincia(uo.getAnagrafica().getProvincia());
		rsModel.setNazione(uo.getAnagrafica().getNazione());
		rsModel.setEmail(uo.getAnagrafica().getEmail());
		rsModel.setPec(uo.getAnagrafica().getPec());
		rsModel.setTel(uo.getAnagrafica().getTelefono());
		rsModel.setFax(uo.getAnagrafica().getFax());
		rsModel.setArea(uo.getAnagrafica().getArea());
		
		return rsModel;
	}
	
	public static Entrata toEntrataRsModel(it.govpay.bd.model.Tributo tributo, it.govpay.model.IbanAccredito ibanAccredito) throws ServiceException {
		Entrata rsModel = new Entrata();
		rsModel.codiceContabilita(tributo.getCodContabilita())
		.abilitato(tributo.isAbilitato())
		.idEntrata(tributo.getCodTributo())
		.tipoEntrata(EntrateConverter.toTipoEntrataRsModel(tributo));
		
		if(tributo.getTipoContabilita() != null) {
			switch (tributo.getTipoContabilita()) {
			case ALTRO:
				rsModel.tipoContabilita(TipoContabilita.ALTRO);
				break;
			case CAPITOLO:
				rsModel.tipoContabilita(TipoContabilita.CAPITOLO);
				break;
			case SIOPE:
				rsModel.tipoContabilita(TipoContabilita.SIOPE);
				break;
			case SPECIALE:
				rsModel.tipoContabilita(TipoContabilita.SPECIALE);
				break;
			}
		}
		
		if(ibanAccredito != null)
			rsModel.contoAccredito(ibanAccredito.getCodIban());

		if(tributo.getIbanAccredito()!=null)
			rsModel.contoAppoggio(tributo.getIbanAccredito().getCodIban());

		return rsModel;
	}
}
