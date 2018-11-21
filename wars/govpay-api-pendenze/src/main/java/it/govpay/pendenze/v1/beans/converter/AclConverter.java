package it.govpay.pendenze.v1.beans.converter;

import java.util.stream.Collectors;

import it.govpay.pendenze.v1.beans.AclPost;
import it.govpay.pendenze.v1.beans.AclPost.ServizioEnum;
import it.govpay.model.Acl.Diritti;

public class AclConverter {
	
	public static AclPost toRsModel(it.govpay.model.Acl acl) {
		AclPost rsModel = new AclPost();
		rsModel.principal(acl.getPrincipal())
		.ruolo(acl.getRuolo());
		
		if(acl.getServizio() != null) {
			switch(acl.getServizio()) {
			case ANAGRAFICA_APPLICAZIONI:
				rsModel.setServizio(ServizioEnum.ANAGRAFICA_APPLICAZIONI);
				break;
			case ANAGRAFICA_CREDITORE:
				rsModel.setServizio(ServizioEnum.ANAGRAFICA_CREDITORE);
				break;
			case ANAGRAFICA_PAGOPA:
				rsModel.setServizio(ServizioEnum.ANAGRAFICA_PAGOPA);
				break;
			case ANAGRAFICA_RUOLI:
				rsModel.setServizio(ServizioEnum.ANAGRAFICA_RUOLI);
				break;
			case CONFIGURAZIONE_E_MANUTENZIONE:
				rsModel.setServizio(ServizioEnum.CONFIGURAZIONE_E_MANUTENZIONE);
				break;
			case GIORNALE_DEGLI_EVENTI:
				rsModel.setServizio(ServizioEnum.GIORNALE_DEGLI_EVENTI);
				break;
			case PAGAMENTI_E_PENDENZE:
				rsModel.setServizio(ServizioEnum.PAGAMENTI_E_PENDENZE);
				break;
			case RENDICONTAZIONI_E_INCASSI:
				rsModel.setServizio(ServizioEnum.RENDICONTAZIONI_E_INCASSI);
				break;
			case STATISTICHE:
				break;
			}
		}
		
		if(acl.getListaDiritti() != null)
			rsModel.autorizzazioni(acl.getListaDiritti().stream().map(a -> toAutorizzazioneEnum(a).toString()).collect(Collectors.toList()));
		
		return rsModel;
	}
	
    public static AclPost.AutorizzazioniEnum toAutorizzazioneEnum(Diritti text) {
    	switch(text) {
		case ESECUZIONE: return AclPost.AutorizzazioniEnum.ESECUZIONE;
		case LETTURA: return AclPost.AutorizzazioniEnum.LETTURA;
		case SCRITTURA: return AclPost.AutorizzazioniEnum.SCRITTURA;
		default:
			break;}
    	return null;
    }
}
