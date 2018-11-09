/*
 * GovPay - Porta di Accesso al Nodo dei Pagamenti SPC 
 * http://www.gov4j.it/govpay
 * 
 * Copyright (c) 2014-2017 Link.it srl (http://www.link.it).
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
package it.govpay.core.utils;

import java.util.ArrayList;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.openspcoop2.generic_project.exception.NotFoundException;
import org.openspcoop2.generic_project.exception.ServiceException;
import org.openspcoop2.utils.Utilities;
import org.openspcoop2.utils.UtilsException;
import org.openspcoop2.utils.transport.http.HttpServletCredential;
import org.slf4j.Logger;

import it.govpay.bd.BasicBD;
import it.govpay.bd.anagrafica.AnagraficaManager;
import it.govpay.bd.model.Applicazione;
import it.govpay.bd.model.Utenza;
import it.govpay.bd.model.UtenzaAnonima;
import it.govpay.bd.model.UtenzaApplicazione;
import it.govpay.bd.model.UtenzaCittadino;
import it.govpay.model.Acl;
import it.govpay.model.Acl.Diritti;
import it.govpay.model.Acl.Servizio;

public class CredentialUtils {
	
	public static Utenza getUser(HttpServletRequest request, Logger log) {
		HttpServletCredential credential = new HttpServletCredential(request, log);
		
		String headerAuth = GovpayConfig.getInstance().getHeaderAuth();
		if(headerAuth != null && request.getHeader(headerAuth) != null) {
			String cf = request.getHeader(headerAuth);
			Utenza user = new UtenzaCittadino(cf);
			List<Acl> aclPrincipal = new ArrayList<>();
			Acl acl = new Acl();
			acl.setPrincipal(cf);
			acl.setListaDiritti(Diritti.LETTURA.getCodifica() + Diritti.SCRITTURA.getCodifica() + Diritti.ESECUZIONE.getCodifica());
			acl.setServizio(Servizio.PAGAMENTI_E_PENDENZE); 
			acl.getProprieta().put(AclEngine.CODICE_FISCALE_CITTADINO, cf);
			aclPrincipal.add(acl);
			user.setAclPrincipal(aclPrincipal);
			user.setAbilitato(true);
			user.setCheckSubject(false);
			user.setPrincipalOriginale(cf);
			user.setPrincipal(cf);
			return user;
		}
		else {
			Utenza user = new UtenzaApplicazione();
			
			if(credential.getSubject() != null) {
				user.setPrincipal(credential.getSubject());
				user.setCheckSubject(true); 
			} else if(credential.getPrincipal() != null) {
				user.setPrincipal(credential.getPrincipal());
				user.setCheckSubject(false); 
			} else {
				user = new UtenzaAnonima();
				List<Acl> aclPrincipal = new ArrayList<>();
				Acl acl = new Acl();
				acl.setPrincipal(null);
				acl.setListaDiritti(Diritti.LETTURA.getCodifica() + Diritti.SCRITTURA.getCodifica() + Diritti.ESECUZIONE.getCodifica());
				acl.setServizio(Servizio.PAGAMENTI_E_PENDENZE); 
				acl.getProprieta().put(AclEngine.UTENZA_ANONIMA, "true");
				aclPrincipal.add(acl);
				user.setAclPrincipal(aclPrincipal);
				user.setAbilitato(true);
				user.setCheckSubject(false);
				user.setPrincipalOriginale(null);
				user.setPrincipal(null);
			}
			
			return user;
		}
	}

	public static Applicazione getApplicazione(BasicBD bd, Utenza user) throws ServiceException, NotFoundException {
		return  user.isCheckSubject() ? 
				AnagraficaManager.getApplicazioneBySubject(bd, user.getPrincipal())
				: AnagraficaManager.getApplicazioneByPrincipal(bd, user.getPrincipal());
	}
	
	public static boolean checkSubject(String principalToCheck, String principalFromRequest) throws Exception{
		boolean ok = true;
	
		Hashtable<String, String> hashSubject = null;
		try {
			principalToCheck = Utilities.formatSubject(principalToCheck);
		}catch(UtilsException e) {
			throw new NotFoundException("L'utenza registrata non e' un subject valido");
		}
		try {
			principalFromRequest = Utilities.formatSubject(principalFromRequest);
			hashSubject = Utilities.getSubjectIntoHashtable(principalFromRequest);
		}catch(UtilsException e) {
			throw new NotFoundException("Utenza" + principalFromRequest + "non autorizzata");
		}
		Enumeration<String> keys = hashSubject.keys();
		while(keys.hasMoreElements()){
			String key = keys.nextElement();
			String value = hashSubject.get(key);
			ok = ok && principalToCheck.contains("/"+Utilities.formatKeySubject(key)+"="+Utilities.formatValueSubject(value)+"/");
		}
		
		return ok;
	}
}
