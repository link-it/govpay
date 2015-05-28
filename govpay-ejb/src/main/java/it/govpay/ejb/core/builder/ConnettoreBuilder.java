/*
 * GovPay - Porta di Accesso al Nodo dei Pagamenti SPC 
 * http://www.gov4j.it/govpay
 * 
 * Copyright (c) 2014-2015 Link.it srl (http://www.link.it).
 * Copyright (c) 2014-2015 TAS S.p.A. (http://www.tasgroup.it).
 * 
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
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
package it.govpay.ejb.builder;

import it.govpay.ejb.model.ConnettoreModel;
import it.govpay.ejb.model.ConnettoreModel.EnumAuthType;
import it.govpay.ejb.model.ConnettoreModel.EnumSslType;
import it.govpay.ejb.model.ConnettorePddModel;
import it.govpay.orm.profilazione.ProprietaConnettore;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;


public class ConnettoreBuilder {
	
	
	public static ConnettoreModel fromIdConnettore(Long idConnettore, EntityManager entityManager) {
		if(idConnettore == null) {
			return null;
		}
		TypedQuery<ProprietaConnettore> query = entityManager.createNamedQuery("listaProprietaConnettore", ProprietaConnettore.class);
		query.setParameter("idConnettore", idConnettore);
		List<ProprietaConnettore> proprietaConnettore = query.getResultList();
		return fromProprietaConnettore(proprietaConnettore);
	}

	/**
	 * Da una lista di ProprietaConnettore restituisce una map che ha come chiave l'idConnettore e come valore la
	 * lista delle proprietaConnettore omogenee per idConnettore.
	 * Raggruppa le proprietà per ID_CONNETTORE
	 * 
	 * @param proprietaConnettore
	 * @return Mappa dei connettori: ID_CONNETTORE, lista ProprietaConnettore (omogenea per id)
	 */
	public static Map<Long, List<ProprietaConnettore>> mappaProprietaConnettori(List<ProprietaConnettore> proprietaConnettore) {
		
		// Mappa dei connettori: ID_CONNETTORE, lista ProprietaConnettore 
		Map<Long, List<ProprietaConnettore>> mappaConnettori = new HashMap<Long, List<ProprietaConnettore>>();

		if(proprietaConnettore != null) {
			for (ProprietaConnettore p : proprietaConnettore) {
				// Mappa delle proprieta: nome, valore
				List<ProprietaConnettore> listaProprieta = mappaConnettori.get(p.getIdConnettore());
				if(listaProprieta == null) {
					listaProprieta = new ArrayList<ProprietaConnettore>();
					mappaConnettori.put(p.getIdConnettore(), listaProprieta);
				}
				listaProprieta.add(p);
			}
		}
		
		return mappaConnettori;
	}

	
	/**
	 * Da una lista di ProprietaConnettore (omogenee per idConnettore) restituisce una map che ha come chiave il nome della proprieta e come valore la proprietà
	 * stessa
	 * 
	 * @param proprietaConnettore
	 *            N.B: devono essere riferite allo STESSO connettore (stesso idConnettore)
	 * @return Mappa dei connettori: NOME, Proprietà del connettore (nome,proprieta')
	 */
	public static Map<String, ProprietaConnettore> mappaProprietaConnettore(List<ProprietaConnettore> proprietaConnettore) {
		
		if(proprietaConnettore == null || proprietaConnettore.isEmpty()) {
			return null;
		}
		
		// Mappa dei connettori: ID_CONNETTORE, Proprietà del connettore (nome,proprieta')
		Map<Long, Map<String, ProprietaConnettore>> mappaConnettori = new HashMap<Long, Map<String,ProprietaConnettore>>();
		
		for (ProprietaConnettore p : proprietaConnettore) {
			// Mappa delle proprieta: nome, valore
			Map<String, ProprietaConnettore> mappaProprieta = mappaConnettori.get(p.getIdConnettore());
			if(mappaProprieta == null) {
				mappaProprieta = new HashMap<String, ProprietaConnettore>();
				mappaConnettori.put(p.getIdConnettore(), mappaProprieta);
			}
			mappaProprieta.put(p.getNomeProperieta(), p);
		}
		
		if(mappaConnettori.size() != 1) {
			throw new RuntimeException("non posso costruire un connettore da proprieta con id differenti " + mappaConnettori.keySet());
		} 
		return mappaConnettori.values().iterator().next();
	}
	
	
	/**
	 * Da una lista di ProprietaConnettore (omogenee per idConnettore) restituisce una map che ha come chiave il nome della proprieta e come valore il valore
	 * della proprietà
	 * 
	 * @param proprietaConnettore
	 *            N.B: devono essere riferite allo STESSO connettore (stesso idConnettore)
	 * @return Mappa dei connettori: NOME, VALORE
	 */
	public static Map<String, String> mappaValoriProprietaConnettore(List<ProprietaConnettore> proprietaConnettore) {
		
		if(proprietaConnettore == null || proprietaConnettore.isEmpty()) {
			return null;
		}
		
		// Mappa dei connettori: ID_CONNETTORE, Proprietà del connettore (nome,valore)
		Map<Long, Map<String, String>> mappaConnettori = new HashMap<Long, Map<String,String>>();
		
		for (ProprietaConnettore p : proprietaConnettore) {
			// Mappa delle proprieta: nome, valore
			Map<String, String> mappaProprieta = mappaConnettori.get(p.getIdConnettore());
			if(mappaProprieta == null) {
				mappaProprieta = new HashMap<String, String>();
				mappaConnettori.put(p.getIdConnettore(), mappaProprieta);
			}
			mappaProprieta.put(p.getNomeProperieta(), p.getValoreProprieta());
		}
		
		if(mappaConnettori.size() != 1) {
			throw new RuntimeException("non posso costruire un connettore da proprieta con id differenti " + mappaConnettori.keySet());
		} 
		
		return mappaConnettori.values().iterator().next();
		
	}
	
	public static ConnettoreModel fromProprietaConnettore(List<ProprietaConnettore> proprietaConnettore) {
		
		Map<String, String> mappaProprieta = mappaValoriProprietaConnettore(proprietaConnettore);
		
		ConnettoreModel connettoreModel;
		if(mappaProprieta.get(ConnettorePddModel.P_AZIONEINURL_NAME) == null) {
			connettoreModel = new ConnettoreModel();
		} else {
			connettoreModel = new ConnettorePddModel();
			((ConnettorePddModel) connettoreModel).setAzioneInUrl(Boolean.valueOf(mappaProprieta.get(ConnettorePddModel.P_AZIONEINURL_NAME)));
		}
		
		// le proprietaConnettore sono omogenee per idConnettore. quindi va bene la prima della lista 
		connettoreModel.setIdConnettore(proprietaConnettore.get(0).getIdConnettore());
		
		connettoreModel.setSslKsType(mappaProprieta.get(ConnettoreModel.P_SSLKSTYPE_NAME));
		connettoreModel.setSslKsLocation(mappaProprieta.get(ConnettoreModel.P_SSLKSLOCATION_NAME));
		connettoreModel.setSslKsPasswd(mappaProprieta.get(ConnettoreModel.P_SSLKSPASSWD_NAME));
		connettoreModel.setSslPKeyPasswd(mappaProprieta.get(ConnettoreModel.P_SSLPKEYPASSWD_NAME));
		connettoreModel.setSslTsType(mappaProprieta.get(ConnettoreModel.P_SSLTSTYPE_NAME));
		connettoreModel.setSslTsLocation(mappaProprieta.get(ConnettoreModel.P_SSLTSLOCATION_NAME));
		connettoreModel.setSslTsPasswd(mappaProprieta.get(ConnettoreModel.P_SSLTSPASSWD_NAME));
		connettoreModel.setSslType(mappaProprieta.get(ConnettoreModel.P_SSLTYPE_NAME));
		connettoreModel.setHttpUser(mappaProprieta.get(ConnettoreModel.P_HTTPUSER_NAME));
		connettoreModel.setHttpPassw(mappaProprieta.get(ConnettoreModel.P_HTTPPASSW_NAME));
		connettoreModel.setUrl(mappaProprieta.get(ConnettoreModel.P_URL_NAME));
		
		if(mappaProprieta.get(ConnettoreModel.P_TIPOAUTENTICAZIONE_NAME) != null)
			connettoreModel.setTipoAutenticazione(EnumAuthType.valueOf(mappaProprieta.get(ConnettoreModel.P_TIPOAUTENTICAZIONE_NAME)));
		if(mappaProprieta.get(ConnettoreModel.P_TIPOSSL_NAME) != null)
			connettoreModel.setTipoSsl(EnumSslType.valueOf(mappaProprieta.get(ConnettoreModel.P_TIPOSSL_NAME)));
		
		return connettoreModel;

	}
	
}
