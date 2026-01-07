/*
 * GovPay - Porta di Accesso al Nodo dei Pagamenti SPC
 * http://www.gov4j.it/govpay
 *
 * Copyright (c) 2014-2026 Link.it srl (http://www.link.it).
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License version 3, as published by
 * the Free Software Foundation.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 *
 */
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
	
	private RuoliConverter() {}

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
