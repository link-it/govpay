package it.govpay.rs.v1.beans.pagamenti.converter;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.openspcoop2.generic_project.exception.ServiceException;

import it.govpay.core.dao.anagrafica.dto.PostAclDTO;
import it.govpay.core.rs.v1.beans.pagamenti.AclPost.ServizioEnum;
import it.govpay.core.rs.v1.beans.pagamenti.AclPost;
import it.govpay.core.rs.v1.beans.pagamenti.AclPost.AutorizzazioniEnum;
import it.govpay.model.Acl;
import it.govpay.model.Acl.Diritti;
import it.govpay.model.Acl.Servizio;
import it.govpay.model.IAutorizzato;

public class AclConverter {
	
	public static PostAclDTO getPostAclDTO(AclPost aclPost, IAutorizzato user) throws ServiceException {
		
		PostAclDTO aclDTO = new PostAclDTO(user);
		Acl acl = new Acl();
		
		List<Diritti> lst = new ArrayList<>();
		for(String authS: aclPost.getAutorizzazioni()) {
			AutorizzazioniEnum auth = AutorizzazioniEnum.fromValue(authS);
			switch(auth) {
			case ESECUZIONE: lst.add(Acl.Diritti.ESECUZIONE);
				break;
			case LETTURA: lst.add(Acl.Diritti.LETTURA);
				break;
			case SCRITTURA: lst.add(Acl.Diritti.SCRITTURA);
				break;
			default:
				break;
			}
		}

		acl.setListaDiritti(lst);
		acl.setPrincipal(aclPost.getPrincipal());
		acl.setRuolo(aclPost.getRuolo());
		acl.setServizio(Servizio.toEnum(aclPost.getServizio().toString()));
		aclDTO.setAcl(acl);

		return aclDTO;		
	}
	
	public static it.govpay.core.rs.v1.beans.pagamenti.AclPost toRsModel(it.govpay.model.Acl acl) {
		it.govpay.core.rs.v1.beans.pagamenti.AclPost rsModel = new it.govpay.core.rs.v1.beans.pagamenti.AclPost();
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
			default:
				break;
			}
		}
		
		if(acl.getListaDiritti() != null)
			rsModel.autorizzazioni(acl.getListaDiritti().stream().map(a -> a.toString()).collect(Collectors.toList()));
		
		return rsModel;
	}
	
}
