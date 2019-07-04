package it.govpay.pagamento.v2.beans.converter;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import it.govpay.pagamento.v1.beans.AclPost.ServizioEnum;

public class AclConverter {
	
	public static it.govpay.pagamento.v1.beans.AclPost toRsModel(it.govpay.bd.model.Acl acl) {
		it.govpay.pagamento.v1.beans.AclPost rsModel = new it.govpay.pagamento.v1.beans.AclPost();
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
			case API_PAGAMENTI:
			case API_PENDENZE:
			case API_RAGIONERIA:
				return null;
			}
		}
		
		if(acl.getListaDiritti() != null) {
			List<String> diritti = acl.getListaDiritti().stream().map(a -> a.toString()).collect(Collectors.toList());
			Collections.sort(diritti);
			rsModel.autorizzazioni(diritti);
		}
		
		return rsModel;
	}
	
}
