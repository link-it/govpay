package it.govpay.pendenze.v2.beans.converter;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import it.govpay.pendenze.v2.beans.AclPost;
import it.govpay.pendenze.v2.beans.AclPost.ServizioEnum;

public class AclConverter {
	
	public static AclPost toRsModel(it.govpay.bd.model.Acl acl) {
		AclPost rsModel = new AclPost();
		rsModel.principal(acl.getUtenzaPrincipalOriginale())
		.ruolo(acl.getRuolo());
		
		ServizioEnum serv = null;
		if(acl.getServizio() != null) {
			switch(acl.getServizio()) {
			case ANAGRAFICA_APPLICAZIONI:
				serv = ServizioEnum.ANAGRAFICA_APPLICAZIONI;
				break;
			case ANAGRAFICA_CREDITORE:
				serv = ServizioEnum.ANAGRAFICA_CREDITORE;
				break;
			case ANAGRAFICA_PAGOPA:
				serv = ServizioEnum.ANAGRAFICA_PAGOPA;
				break;
			case ANAGRAFICA_RUOLI:
				serv = ServizioEnum.ANAGRAFICA_RUOLI;
				break;
			case CONFIGURAZIONE_E_MANUTENZIONE:
				serv = ServizioEnum.CONFIGURAZIONE_E_MANUTENZIONE;
				break;
			case GIORNALE_DEGLI_EVENTI:
				serv = ServizioEnum.GIORNALE_DEGLI_EVENTI;
				break;
			case PAGAMENTI:
				serv = ServizioEnum.PAGAMENTI;
				break;
			case PENDENZE:
				serv = ServizioEnum.PENDENZE;
				break;
			case RENDICONTAZIONI_E_INCASSI:
				serv = ServizioEnum.RENDICONTAZIONI_E_INCASSI;
				break;
			case API_PAGAMENTI:
			case API_PENDENZE:
			case API_RAGIONERIA:
				break;
			}
		}
		
		// se l'acl non deve uscire allora ritorno null
		if(serv ==null)
			return null;
		
		if(acl.getListaDiritti() != null) {
			List<String> autorizzazioni = acl.getListaDiritti().stream().map(a -> a.getCodifica()).collect(Collectors.toList());
			Collections.sort(autorizzazioni);
			rsModel.autorizzazioni(autorizzazioni);
		}
		
		return rsModel;
	}
}
