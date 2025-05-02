/*
 * GovPay - Porta di Accesso al Nodo dei Pagamenti SPC
 * http://www.gov4j.it/govpay
 *
 * Copyright (c) 2014-2025 Link.it srl (http://www.link.it).
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
package it.govpay.backoffice.v1.beans.converter;

import java.util.ArrayList;
import java.util.List;

import it.govpay.backoffice.v1.beans.AclPost;
import it.govpay.backoffice.v1.beans.DominioProfiloIndex;
import it.govpay.backoffice.v1.beans.Profilo;
import it.govpay.backoffice.v1.beans.TipoPendenza;
import it.govpay.backoffice.v1.controllers.ApplicazioniController;
import it.govpay.bd.model.Acl;
import it.govpay.bd.model.Dominio;
import it.govpay.bd.model.Utenza;
import it.govpay.bd.model.UtenzaOperatore;
import it.govpay.core.dao.anagrafica.dto.LeggiProfiloDTOResponse;
import it.govpay.model.TipoVersamento;

/**
 * @author Bussu Giovanni (bussu@link.it)
 * @author  $Author: bussu $
 * @version $ Rev: 12563 $, $Date: 12 giu 2018 $
 *
 */
public class ProfiloConverter {
	
	private ProfiloConverter() {}

	/**
	 * @param user
	 * @return
	 */
	public static Profilo getProfilo(LeggiProfiloDTOResponse leggiProfilo) {
		Profilo profilo = new Profilo();

		Utenza user = leggiProfilo.getUtente();
		List<Acl> aclsProfilo = user.getAclsProfilo();
		if(aclsProfilo!=null) {
			List<AclPost> aclLst = new ArrayList<>();
			for(Acl acl: aclsProfilo) {
				AclPost aclRsModel = AclConverter.toRsModel(acl);
				if(aclRsModel != null)
					aclLst.add(aclRsModel);

			}
			profilo.setAcl(aclLst);
		}
		profilo.setNome(leggiProfilo.getNome());

		switch(user.getTipoUtenza()) {
		case ANONIMO, APPLICAZIONE, CITTADINO:
			break;
		case OPERATORE:
			profilo.setNome(((UtenzaOperatore) user).getNome());
			break;
		}

		profilo.setAutenticazione(user.getAutenticazione());

		if(user.isAutorizzazioneDominiStar()) {
			List<DominioProfiloIndex> dominiLst = new ArrayList<>();
			DominioProfiloIndex dominioStar = new DominioProfiloIndex();
			dominioStar.setIdDominio(ApplicazioniController.AUTORIZZA_DOMINI_STAR);
			dominioStar.setRagioneSociale(ApplicazioniController.AUTORIZZA_DOMINI_STAR_LABEL);
			dominiLst.add(dominioStar);
			profilo.setDomini(dominiLst);
		} else {
			if(leggiProfilo.getDomini()!=null) {
				List<DominioProfiloIndex> dominiLst = new ArrayList<>();
				for(Dominio dominio: leggiProfilo.getDomini()) {
					dominiLst.add(DominiConverter.toRsModelProfiloIndex(dominio));
				}
				profilo.setDomini(dominiLst);
			}
		}
		if(user.isAutorizzazioneTipiVersamentoStar()) {
			List<TipoPendenza> tipiPendenzaLst = new ArrayList<>();
			TipoPendenza tipoPendenzaStar = new TipoPendenza();
			tipoPendenzaStar.setIdTipoPendenza(ApplicazioniController.AUTORIZZA_TIPI_PENDENZA_STAR);
			tipoPendenzaStar.setDescrizione(ApplicazioniController.AUTORIZZA_TIPI_PENDENZA_STAR_LABEL);
			tipiPendenzaLst.add(tipoPendenzaStar );
			profilo.setTipiPendenza(tipiPendenzaLst);
		}else {
			if(leggiProfilo.getTipiVersamento()!=null) {
				List<TipoPendenza> tipiPendenzaLst = new ArrayList<>();
				for(TipoVersamento tributo: leggiProfilo.getTipiVersamento()) {
					tipiPendenzaLst.add(TipiPendenzaConverter.toTipoPendenzaRsModel(tributo));
				}
				profilo.setTipiPendenza(tipiPendenzaLst);
			}
		}

		return profilo;
	}

}
