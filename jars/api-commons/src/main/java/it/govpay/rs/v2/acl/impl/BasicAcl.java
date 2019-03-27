package it.govpay.rs.v2.acl.impl;

import java.util.Map;
import java.util.Properties;

import javax.ws.rs.core.MultivaluedMap;

import org.apache.commons.lang.ArrayUtils;
import org.apache.commons.lang.NotImplementedException;
import org.openspcoop2.utils.service.context.IContext;
import org.openspcoop2.utils.transport.http.HttpRequestMethod;
import org.springframework.security.core.GrantedAuthority;

import it.govpay.rs.v2.acl.Acl;


public class BasicAcl implements Acl {

	String[] principals;
	String[] roles;
	Map<HttpRequestMethod, String[]> resources;
	Properties pathParams;
	Properties queryParams;
	Properties roleParams;

	public BasicAcl(String[] principals, String[] roles, Map<HttpRequestMethod, String[]> resources, Properties pathParams, Properties queryParams, Properties roleParams) {
		this.principals = principals;
		this.roles = roles;
		this.resources = resources;
		this.pathParams = pathParams;
		this.queryParams = queryParams;
		this.roleParams = roleParams;
	}

	@Override
	public boolean isSatisfied(IContext context) {

		// Se il principal e' impostato controllo che 
		// - Ho un principal
		// - Il mio principal e' tra quelli dell'ACL
		if(principals != null && (context.getAuthentication().getPrincipal() == null || !ArrayUtils.contains(principals, context.getAuthentication().getPrincipal()))) {
			return false;
		}

		// Se il ruolo e' impostato controllo che 
		// - Ho una lista di ruoli
		// - Ho almeno uno dei ruoli dell'ACL
		if(roles != null) {
			if(context.getAuthentication().getAuthorities() == null) return false;
			boolean hasRole = false;
			for(GrantedAuthority gaut : context.getAuthentication().getAuthorities()) {
				if(ArrayUtils.contains(roles, gaut.getAuthority())) hasRole = true;
			}
			if(!hasRole) return false;
		}

		// Controllo che la risorsa richiesta sia tra quelle dell'ACL, se impostate
		if(this.resources != null) {
			
			// Prendo la lista dei path del Method usato nella richiesta
			String[] resourcePaths = this.resources.get(HttpRequestMethod.valueOf(context.getServletRequest().getMethod().toUpperCase()));
			
			// Se la lista e' nulla, ho finito
			if(resourcePaths == null) return false;
			
			// Ciclo la lista dei path per trovarne uno uguale o subset con wildcard
			boolean resourcePathFound = false;
			for(String resourcePath : resourcePaths) {
				if(resourcePath.endsWith(WILDCARD)) {
					String patchCheckWitoutStar = resourcePath.substring(0, (resourcePath.length()-WILDCARD.length()));
					if(normalizePath(context.getRestPath()).startsWith(normalizePath(patchCheckWitoutStar))) {
						resourcePathFound = true;				
					}
				}
				else {
					if(!normalizePath(context.getRestPath()).equals(normalizePath(resourcePath))) {
						resourcePathFound = true;		
					}
				}
				// Trovato, non importa continuare a ciclare
				if(resourcePathFound) break;
			}
			// Non l'ho trovato, ho finito
			if(!resourcePathFound) return false;
		}

		// Controllo che tutti i pathParams corrispondano a quelli valorizzati nella richiesta
		if(pathParams != null) {
			MultivaluedMap<String, String> pathParameters = context.getUriInfo().getPathParameters();
			for(Object key : pathParams.keySet()) {
				if(!pathParameters.containsKey(key) || !pathParameters.getFirst((String) key).equals(this.pathParams.get(key)))
					return false;
			}
		}

		// Controllo che tutti i queryParams corrispondano a quelli valorizzati nella richiesta
		if(this.queryParams != null) {
			MultivaluedMap<String, String> queryParameters = context.getUriInfo().getQueryParameters();
			for(Object key : this.queryParams.keySet()) {
				if(!queryParameters.containsKey(key) || !queryParameters.getFirst((String) key).equals(this.queryParams.get(key)))
					return false;
			}
		}

		// Controllo che tutti i queryParams corrispondano a quelli valorizzati nella richiesta
		if(this.roleParams != null) {
			throw new NotImplementedException();
		}

		return true;
	}


	private static String normalizePath(String path) {
		if(path.endsWith("/")) {
			return path.substring(0, path.length()-1);
		}
		else {
			return path;
		}
	}

}
