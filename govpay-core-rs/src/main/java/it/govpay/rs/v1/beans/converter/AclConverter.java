package it.govpay.rs.v1.beans.converter;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.openspcoop2.generic_project.exception.ServiceException;

import it.govpay.core.dao.anagrafica.dto.PostAclDTO;
import it.govpay.core.rs.v1.beans.base.AclPost;
import it.govpay.core.rs.v1.beans.base.AclPost.AutorizzazioniEnum;
import it.govpay.core.rs.v1.beans.base.ServizioEnum;
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
		acl.setServizio(Servizio.toEnum(aclPost.getServizio().toString()));
		aclDTO.setAcl(acl);

		return aclDTO;		
	}
	
	public static AclPost toRsModel(it.govpay.model.Acl acl) {
		AclPost rsModel = new AclPost();
		
		if(acl.getServizio() != null) {
			ServizioEnum serv = null;
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
			case PAGAMENTI_E_PENDENZE:
				serv = ServizioEnum.PAGAMENTI_E_PENDENZE;
				break;
			case RENDICONTAZIONI_E_INCASSI:
				serv = ServizioEnum.RENDICONTAZIONI_E_INCASSI;
				break;
			case STATISTICHE:
				break;
			}
			
			rsModel.setServizio(serv.toString());
		}
		
		if(acl.getListaDiritti() != null)
			rsModel.autorizzazioni(acl.getListaDiritti().stream().map(a -> a.getCodifica()).collect(Collectors.toList()));
		
		return rsModel;
	}
}
