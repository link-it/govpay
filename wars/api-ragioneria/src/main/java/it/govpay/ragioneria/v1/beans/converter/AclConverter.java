package it.govpay.ragioneria.v1.beans.converter;

import java.util.stream.Collectors;

import it.govpay.ragioneria.v1.beans.AclPost;
import it.govpay.ragioneria.v1.beans.AclPost.ServizioEnum;

public class AclConverter {
	
	
	public static AclPost toRsModel(it.govpay.bd.model.Acl acl) {
		AclPost rsModel = new AclPost();
		rsModel.principal(acl.getUtenzaPrincipalOriginale())
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
			case PAGAMENTI:
				rsModel.setServizio(ServizioEnum.PAGAMENTI);
				break;
			case PENDENZE:
				rsModel.setServizio(ServizioEnum.PENDENZE);
				break;
			case RENDICONTAZIONI_E_INCASSI:
				rsModel.setServizio(ServizioEnum.RENDICONTAZIONI_E_INCASSI);
				break;
			}
		}
		
		if(acl.getListaDiritti() != null)
			rsModel.autorizzazioni(acl.getListaDiritti().stream().map(a -> a.getCodifica()).collect(Collectors.toList()));
		
		return rsModel;
	}
}
