/**
 * 
 */
package it.govpay.backoffice.v1.beans.converter;

import java.util.ArrayList;
import java.util.List;

import org.openspcoop2.generic_project.exception.ServiceException;
import org.springframework.security.core.Authentication;

import it.govpay.backoffice.v1.beans.AclPost;
import it.govpay.backoffice.v1.beans.Ruolo;
import it.govpay.backoffice.v1.beans.RuoloIndex;
import it.govpay.core.dao.anagrafica.dto.PutRuoloDTO;
import it.govpay.bd.model.Acl;

/**
 * @author Bussu Giovanni (bussu@link.it)
 * @author  $Author: bussu $
 * @version $ Rev: 12563 $, $Date: 12 giu 2018 $
 * 
 */
public class RuoliConverter {

	public static RuoloIndex toRsModelIndex(String ruoloName) throws ServiceException {
		RuoloIndex ruolo = new RuoloIndex();
		ruolo.setId(ruoloName);
		
		return ruolo;
	}

	/**
	 * @param listaAcl
	 * @param idRuolo
	 * @return
	 * @throws ServiceException 
	 */
	public static PutRuoloDTO getPutRuoloDTO(List<AclPost> listaAcl, String idRuolo, Authentication user) throws ServiceException {
		PutRuoloDTO putRuoloDTO = new PutRuoloDTO(user);
		List<Acl> acls = new ArrayList<>();
		for(AclPost acl: listaAcl) {
			acls.add(AclConverter.getAclRuolo(acl, idRuolo));
		}
		putRuoloDTO.setAcls(acls);
		putRuoloDTO.setIdRuolo(idRuolo);
		return putRuoloDTO;
	}

	
	public static Ruolo toRsModel(String ruoloName, List<Acl> listaAcl) throws ServiceException {
		Ruolo ruolo = new Ruolo();
		ruolo.setId(ruoloName);
		List<AclPost> aclsPost = new ArrayList<>();
		for (Acl acl : listaAcl) {
			aclsPost.add(AclConverter.toRsModel(acl));
		}
		
		ruolo.setAcl(aclsPost );
		
		return ruolo;
	}
}
