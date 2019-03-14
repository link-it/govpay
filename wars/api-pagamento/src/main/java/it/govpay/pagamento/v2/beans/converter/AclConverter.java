package it.govpay.pagamento.v2.beans.converter;

import java.util.stream.Collectors;

import it.govpay.pagamento.v2.beans.TipoAutorizzazione;
import it.govpay.pagamento.v2.beans.TipoServizio;

public class AclConverter {

	public static it.govpay.pagamento.v2.beans.Acl toRsModel(it.govpay.bd.model.Acl acl) {
		it.govpay.pagamento.v2.beans.Acl rsModel = new it.govpay.pagamento.v2.beans.Acl();
		rsModel.principal(acl.getUtenzaPrincipalOriginale())
		.ruolo(acl.getRuolo());
		
		if(acl.getServizio() != null) {
			switch(acl.getServizio()) {
			case ANAGRAFICA_APPLICAZIONI:
				rsModel.setServizi(TipoServizio.ANAGRAFICA_APPLICAZIONI);
				break;
			case ANAGRAFICA_CREDITORE:
				rsModel.setServizi(TipoServizio.ANAGRAFICA_CREDITORE);
				break;
			case ANAGRAFICA_PAGOPA:
				rsModel.setServizi(TipoServizio.ANAGRAFICA_PAGOPA);
				break;
			case ANAGRAFICA_RUOLI:
				rsModel.setServizi(TipoServizio.ANAGRAFICA_RUOLI);
				break;
			case CONFIGURAZIONE_E_MANUTENZIONE:
				rsModel.setServizi(TipoServizio.CONFIGURAZIONE_E_MANUTENZIONE);
				break;
			case GIORNALE_DEGLI_EVENTI:
				rsModel.setServizi(TipoServizio.GIORNALE_DEGLI_EVENTI);
				break;
			case PAGAMENTI_E_PENDENZE:
				rsModel.setServizi(TipoServizio.PAGAMENTI_E_PENDENZE);
				break;
			case RENDICONTAZIONI_E_INCASSI:
				rsModel.setServizi(TipoServizio.RENDICONTAZIONI_E_INCASSI);
				break;
			default:
				break;
			}
		}
		
		if(acl.getListaDiritti() != null) {
			TipoAutorizzazione tipoAutorizzazione = new TipoAutorizzazione();
			tipoAutorizzazione.addAll(acl.getListaDiritti().stream().map(a -> a.toString()).collect(Collectors.toList()));
			rsModel.autorizzazioni(tipoAutorizzazione);
		}
		
		return rsModel;
	}
}
