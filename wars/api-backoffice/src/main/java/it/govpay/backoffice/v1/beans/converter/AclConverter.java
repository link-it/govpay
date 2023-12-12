/*
 * GovPay - Porta di Accesso al Nodo dei Pagamenti SPC
 * http://www.gov4j.it/govpay
 *
 * Copyright (c) 2014-2023 Link.it srl (http://www.link.it).
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

import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.security.core.Authentication;

import it.govpay.backoffice.v1.beans.AclPost;
import it.govpay.backoffice.v1.beans.AclPost.AutorizzazioniEnum;
import it.govpay.backoffice.v1.beans.AclPost.ServizioEnum;
import it.govpay.bd.model.Acl;
import it.govpay.bd.model.Utenza;
import it.govpay.core.autorizzazione.beans.GovpayLdapUserDetails;
import it.govpay.core.autorizzazione.utils.AutorizzazioneUtils;
import it.govpay.model.Acl.Diritti;
import it.govpay.model.Acl.Servizio;
import it.govpay.model.exception.CodificaInesistenteException;

public class AclConverter {

	public static Acl getAclUtenza(AclPost aclPost, Authentication user) throws CodificaInesistenteException {

		Acl acl = new Acl();

		Set<Diritti> lst = new HashSet<>();
		for(String authS: aclPost.getAutorizzazioni()) {
			AutorizzazioniEnum auth = AutorizzazioniEnum.fromValue(authS);
			switch(auth) {
			case LETTURA: lst.add(Diritti.LETTURA);
				break;
			case SCRITTURA: lst.add(Diritti.SCRITTURA);
				break;
			default:
				break;
			}
		}

		acl.setListaDiritti(lst);
		acl.setServizio(Servizio.toEnum(aclPost.getServizio().toString()));
		GovpayLdapUserDetails authenticationDetails = AutorizzazioneUtils.getAuthenticationDetails(user);
		acl.setUtenza(authenticationDetails.getUtenza());
		return acl;
	}

	public static Acl getAclUtenza(AclPost aclPost, Utenza utenza) throws CodificaInesistenteException {

		Acl acl = new Acl();

		Set<Diritti> lst = new HashSet<>();
		for(String authS: aclPost.getAutorizzazioni()) {
			AutorizzazioniEnum auth = AutorizzazioniEnum.fromValue(authS);
			switch(auth) {
			case LETTURA: lst.add(Diritti.LETTURA);
				break;
			case SCRITTURA: lst.add(Diritti.SCRITTURA);
				break;
			default:
				break;
			}
		}

		acl.setListaDiritti(lst);
		acl.setServizio(Servizio.toEnum(aclPost.getServizio().toString()));
		acl.setUtenza(utenza);
		return acl;
	}

	public static Acl getAclAPI(Servizio apiServizio, Authentication user) {

		Acl acl = new Acl();

		Set<Diritti> lst = new HashSet<>();
		lst.add(Diritti.LETTURA);
		lst.add(Diritti.SCRITTURA);
		acl.setListaDiritti(lst);
		acl.setServizio(apiServizio);
		GovpayLdapUserDetails authenticationDetails = AutorizzazioneUtils.getAuthenticationDetails(user);
		acl.setUtenza(authenticationDetails.getUtenza());
		return acl;
	}

	public static Acl getAclRuolo(AclPost aclPost, String ruolo) throws CodificaInesistenteException {

		Acl acl = new Acl();

		Set<Diritti> lst = new HashSet<>();
		for(String authS: aclPost.getAutorizzazioni()) {
			AutorizzazioniEnum auth = AutorizzazioniEnum.fromValue(authS);
			switch(auth) {
			case LETTURA: lst.add(Diritti.LETTURA);
				break;
			case SCRITTURA: lst.add(Diritti.SCRITTURA);
				break;
			default:
				break;
			}
		}

		acl.setListaDiritti(lst);
		acl.setServizio(Servizio.toEnum(aclPost.getServizio().toString()));
		acl.setRuolo(ruolo);
		return acl;
	}

	public static AclPost toRsModel(it.govpay.bd.model.Acl acl) {
		AclPost rsModel = new AclPost();

		ServizioEnum serv = null;
		if(acl.getServizio() != null) {
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
			case PAGAMENTI:
				serv = ServizioEnum.PAGAMENTI;
				break;
			case PENDENZE:
				serv = ServizioEnum.PENDENZE;
				break;
			case RENDICONTAZIONI_E_INCASSI:
				serv = ServizioEnum.RENDICONTAZIONI_E_INCASSI;
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

		rsModel.setServizio(serv.toString());

		if(acl.getListaDiritti() != null) {
			List<String> autorizzazioni = acl.getListaDiritti().stream().map(a -> a.getCodifica()).collect(Collectors.toList());
			Collections.sort(autorizzazioni);
			rsModel.autorizzazioni(autorizzazioni);
		}

		return rsModel;
	}
}
