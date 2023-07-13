/**
 *
 */
package it.govpay.backoffice.v1.beans.converter;

import java.util.ArrayList;
import java.util.List;

import org.springframework.security.core.Authentication;

import it.govpay.backoffice.v1.beans.AclPost;
import it.govpay.backoffice.v1.beans.Ruolo;
import it.govpay.backoffice.v1.beans.RuoloIndex;
import it.govpay.bd.model.Acl;
import it.govpay.core.dao.anagrafica.dto.PutRuoloDTO;
import it.govpay.model.exception.CodificaInesistenteException;

/**
 * @author Bussu Giovanni (bussu@link.it)
 * @author  $Author: bussu $
 * @version $ Rev: 12563 $, $Date: 12 giu 2018 $
 *
 */
public class RuoliConverter {

	public static RuoloIndex toRsModelIndex(String ruoloName) {
		RuoloIndex ruolo = new RuoloIndex();
		ruolo.setId(ruoloName);

		return ruolo;
	}

	/**
	 * @param listaAcl
	 * @param idRuolo
	 * @return
	 * @throws CodificaInesistenteException
	 */
	public static PutRuoloDTO getPutRuoloDTO(List<AclPost> listaAcl, String idRuolo, Authentication user) throws CodificaInesistenteException {
		PutRuoloDTO putRuoloDTO = new PutRuoloDTO(user);
		List<Acl> acls = new ArrayList<>();
		for(AclPost acl: listaAcl) {
			acls.add(AclConverter.getAclRuolo(acl, idRuolo));
		}
		putRuoloDTO.setAcls(acls);
		putRuoloDTO.setIdRuolo(idRuolo);
		return putRuoloDTO;
	}


	public static Ruolo toRsModel(String ruoloName, List<Acl> listaAcl) {
		Ruolo ruolo = new Ruolo();
		ruolo.setId(ruoloName);
		List<AclPost> aclsPost = new ArrayList<>();
		for (Acl acl : listaAcl) {
			AclPost aclRsModel = AclConverter.toRsModel(acl);
			if(aclRsModel != null)
				aclsPost.add(aclRsModel);
		}

		ruolo.setAcl(aclsPost );

		return ruolo;
	}
}
