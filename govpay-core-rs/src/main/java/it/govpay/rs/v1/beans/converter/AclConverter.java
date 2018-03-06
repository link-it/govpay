package it.govpay.rs.v1.beans.converter;

import java.util.ArrayList;
import java.util.List;

import org.openspcoop2.generic_project.exception.ServiceException;

import it.govpay.core.dao.anagrafica.dto.PostAclDTO;
import it.govpay.model.Acl;
import it.govpay.model.Acl.Diritti;
import it.govpay.model.Acl.Servizio;
import it.govpay.model.IAutorizzato;
import it.govpay.rs.v1.beans.base.AclPost;
import it.govpay.rs.v1.beans.base.AclPost.AutorizzazioniEnum;

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
}
