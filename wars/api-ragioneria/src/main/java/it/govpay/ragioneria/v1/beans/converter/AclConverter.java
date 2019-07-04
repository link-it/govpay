package it.govpay.ragioneria.v1.beans.converter;

import java.util.stream.Collectors;

import it.govpay.ragioneria.v1.beans.AclPost;

public class AclConverter {
	
	
	public static AclPost toRsModel(it.govpay.bd.model.Acl acl) {
		AclPost rsModel = new AclPost();
		rsModel.principal(acl.getUtenzaPrincipalOriginale())
		.ruolo(acl.getRuolo());
		
		if(acl.getServizio() != null) {
			switch(acl.getServizio()) {
			case ANAGRAFICA_APPLICAZIONI:
			case ANAGRAFICA_CREDITORE:
			case ANAGRAFICA_PAGOPA:
			case ANAGRAFICA_RUOLI:
			case CONFIGURAZIONE_E_MANUTENZIONE:
			case GIORNALE_DEGLI_EVENTI:
			case PAGAMENTI:
			case PENDENZE:
			case RENDICONTAZIONI_E_INCASSI:
				rsModel.setServizio(acl.getServizio().getCodifica());
				break;
			case API_PAGAMENTI:
			case API_PENDENZE:
			case API_RAGIONERIA:
				return null;
			}
		}
		
		if(acl.getListaDiritti() != null)
			rsModel.autorizzazioni(acl.getListaDiritti().stream().map(a -> a.getCodifica()).collect(Collectors.toList()));
		
		return rsModel;
	}
}
