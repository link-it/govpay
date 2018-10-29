/**
 * 
 */
package it.govpay.rs.v1.beans.converter;

import java.util.ArrayList;
import java.util.List;

import org.openspcoop2.generic_project.exception.ServiceException;

import it.govpay.core.dao.anagrafica.dto.PutRuoloDTO;
import it.govpay.core.rs.v1.beans.base.AclPost;
import it.govpay.core.rs.v1.beans.base.Ruolo;
import it.govpay.core.rs.v1.beans.base.RuoloIndex;
import it.govpay.model.Acl;
import it.govpay.model.IAutorizzato;

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
	public static PutRuoloDTO getPutRuoloDTO(List<AclPost> listaAcl, String idRuolo, IAutorizzato user) throws ServiceException {
		PutRuoloDTO putRuoloDTO = new PutRuoloDTO(user);
		List<Acl> acls = new ArrayList<>();
		for(AclPost acl: listaAcl) {
			acls.add(AclConverter.getPostAclDTO(acl, user).getAcl());
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
