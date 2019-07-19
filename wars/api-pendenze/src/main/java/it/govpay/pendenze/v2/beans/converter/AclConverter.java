package it.govpay.pendenze.v2.beans.converter;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import it.govpay.pendenze.v2.beans.Acl;
import it.govpay.pendenze.v2.beans.TipoServizio;

public class AclConverter {
	
	public static Acl toRsModel(it.govpay.bd.model.Acl acl) {
		Acl rsModel = new Acl();
		rsModel.principal(acl.getUtenzaPrincipalOriginale())
		.ruolo(acl.getRuolo());
		
		TipoServizio serv = null;
		if(acl.getServizio() != null) {
			switch(acl.getServizio()) {
			case ANAGRAFICA_APPLICAZIONI:
				serv = TipoServizio.ANAGRAFICA_APPLICAZIONI;
				break;
			case ANAGRAFICA_CREDITORE:
				serv = TipoServizio.ANAGRAFICA_CREDITORE;
				break;
			case ANAGRAFICA_PAGOPA:
				serv = TipoServizio.ANAGRAFICA_PAGOPA;
				break;
			case ANAGRAFICA_RUOLI:
				serv = TipoServizio.ANAGRAFICA_RUOLI;
				break;
			case CONFIGURAZIONE_E_MANUTENZIONE:
				serv = TipoServizio.CONFIGURAZIONE_E_MANUTENZIONE;
				break;
			case GIORNALE_DEGLI_EVENTI:
				serv = TipoServizio.GIORNALE_DEGLI_EVENTI;
				break;
			case PAGAMENTI:
				serv = TipoServizio.PAGAMENTI;
				break;
			case PENDENZE:
				serv = TipoServizio.PENDENZE;
				break;
			case RENDICONTAZIONI_E_INCASSI:
				serv = TipoServizio.RENDICONTAZIONI_E_INCASSI;
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
		
		rsModel.setServizio(serv);
		
		if(acl.getListaDiritti() != null) {
			List<String> autorizzazioni = acl.getListaDiritti().stream().map(a -> a.getCodifica()).collect(Collectors.toList());
			Collections.sort(autorizzazioni);
			rsModel.autorizzazioni(autorizzazioni);
		}
		
		return rsModel;
	}
}
