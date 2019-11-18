package it.govpay.pagamento.v2.beans.converter;

import java.util.List;

import org.openspcoop2.generic_project.exception.ServiceException;
import org.openspcoop2.utils.jaxrs.RawObject;

import it.govpay.core.dao.anagrafica.dto.GetTipoPendenzaDominioDTOResponse;
import it.govpay.core.utils.UriBuilderUtils;
import it.govpay.pagamento.v2.beans.Dominio;
import it.govpay.pagamento.v2.beans.TipoPendenza;
import it.govpay.pagamento.v2.beans.TipoPendenzaForm;
import it.govpay.pagamento.v2.beans.UnitaOperativa;

public class DominiConverter {
	
	public static Dominio toRsModel(it.govpay.bd.model.Dominio dominio) throws ServiceException {
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
		if(dominio.getLogo() != null) {
			rsModel.setLogo(UriBuilderUtils.getLogoDominio(dominio.getCodDominio()));
		}
		rsModel.setUnitaOperative(UriBuilderUtils.getListUoByDominio(dominio.getCodDominio()));
		rsModel.setTipiPendenza(UriBuilderUtils.getTipiPendenzaByDominio(dominio.getCodDominio()));
		
		return rsModel;
	}
	
//	public static Dominio toRsModel(it.govpay.bd.model.Dominio dominio) throws ServiceException {
//		return toRsModel(dominio, null, null, null);
//	}
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
		if(dominio.getLogo() != null) {
			rsModel.setLogo(UriBuilderUtils.getLogoDominio(dominio.getCodDominio()));
		}
		
//		if(uoLst != null) {
//			List<UnitaOperativa> unitaOperative = new ArrayList<>();
//			
//			for(it.govpay.bd.model.UnitaOperativa uo: uoLst) {
//				unitaOperative.add(toUnitaOperativaRsModel(uo));
//			}
//			rsModel.setUnitaOperative(unitaOperative);
//		}
//
//		if(ibanAccreditoLst != null) {
//			List<ContiAccredito> contiAccredito = new ArrayList<>();
//			
//			for(it.govpay.bd.model.IbanAccredito iban: ibanAccreditoLst) {
//				contiAccredito.add(toIbanRsModel(iban));
//			}
//			rsModel.setContiAccredito(contiAccredito);
//		}
//
//		if(tributoLst != null) {
//			List<Entrata> entrate = new ArrayList<>();
//			
//			for(Tributo tributo: tributoLst) {
//				entrate.add(toEntrataRsModel(tributo, tributo.getIbanAccredito()));
//			}
//			rsModel.setEntrate(entrate);
//		}
		
		return rsModel;
	}
	
	public static UnitaOperativa toUnitaOperativaRsModel(it.govpay.bd.model.UnitaOperativa uo) throws IllegalArgumentException, ServiceException {
		UnitaOperativa rsModel = new UnitaOperativa();
		
		rsModel.setCap(uo.getAnagrafica().getRagioneSociale());
		rsModel.setCivico(uo.getAnagrafica().getCivico());
		rsModel.setIdUnitaOperativa(uo.getCodUo());
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
	
	public static TipoPendenza toTipoPendenzaRsModel(GetTipoPendenzaDominioDTOResponse response) throws ServiceException {
		return toTipoPendenzaRsModel(response.getTipoVersamento());
	}
	
	public static TipoPendenza toTipoPendenzaRsModel(it.govpay.bd.model.TipoVersamentoDominio tipoVersamentoDominio) throws ServiceException {
		TipoPendenza rsModel = new TipoPendenza();
		
		rsModel.descrizione(tipoVersamentoDominio.getDescrizione())
		.idTipoPendenza(tipoVersamentoDominio.getCodTipoVersamento());
		
		if(tipoVersamentoDominio.getTipo() != null) {
			switch (tipoVersamentoDominio.getTipo()) {
			case DOVUTO:
				rsModel.setTipo(it.govpay.pagamento.v2.beans.TipoPendenzaTipologia.DOVUTO);
				break;
			case SPONTANEO:
				rsModel.setTipo(it.govpay.pagamento.v2.beans.TipoPendenzaTipologia.SPONTANEO);
				break;
			}
		}
		
		if(tipoVersamentoDominio.getFormDefinizione() != null && tipoVersamentoDominio.getFormDefinizione() != null) {
			TipoPendenzaForm form = new TipoPendenzaForm();
			form.setTipo(tipoVersamentoDominio.getFormTipo());
			form.setDefinizione(new RawObject(tipoVersamentoDominio.getFormDefinizione())); 
			rsModel.setForm(form);
		}
		
		rsModel.setVisualizzazione(new RawObject(tipoVersamentoDominio.getVisualizzazioneDefinizione()));
		
		return rsModel;
	}
	
}
