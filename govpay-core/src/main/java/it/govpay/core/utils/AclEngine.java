package it.govpay.core.utils;

import java.util.HashSet;
import java.util.Set;

import it.govpay.model.Acl;
import it.govpay.model.Applicazione;
import it.govpay.model.Operatore;
import it.govpay.model.Portale;
import it.govpay.model.Acl.Servizio;
import it.govpay.model.Acl.Tipo;

public class AclEngine {

	public static boolean isAuthorized(Applicazione applicazione, Servizio servizio, String codDominio, String codTributo) {
		
		// Controllo se ho il dominio
		boolean isDominioAbilitato = false;
		boolean isTributoAbilitato = (codTributo == null); // Se non ho indicato un codTributo, non ne controllo l'abilitazione.
		
		for(Acl acl : applicazione.getAcls()) {
			
			if(!isDominioAbilitato && acl.getServizio().equals(servizio) && acl.getTipo().equals(Tipo.DOMINIO) && (acl.getCodDominio() == null || acl.getCodDominio().equals(codDominio))) {
				isDominioAbilitato = true;
			}
			
			if(!isTributoAbilitato && acl.getServizio().equals(servizio) && acl.getTipo().equals(Tipo.TRIBUTO) && (acl.getCodTributo() == null || acl.getCodTributo().equals(codTributo))) {
				isTributoAbilitato = true;
			}
		}
		
		return isDominioAbilitato && isTributoAbilitato;
	}
	
	public static boolean isAuthorized(Portale portale, Servizio servizio, String codDominio, String codTributo) {
		
		// Controllo se ho il dominio
		boolean isDominioAbilitato = false;
		boolean isTributoAbilitato = (codTributo == null); // Se non ho indicato un codTributo, non ne controllo l'abilitazione.
		
		for(Acl acl : portale.getAcls()) {
			// Se il controllo e' per Servizio.PAGAMENTI_ATTESA allora va bene anche l'abilitazione a PAGAMENTI_ONLINE
			
			if(servizio.equals(Servizio.PAGAMENTI_ONLINE)) {
				if(!isDominioAbilitato && acl.getServizio().equals(servizio) && acl.getTipo().equals(Tipo.DOMINIO) && (acl.getCodDominio() == null || acl.getCodDominio().equals(codDominio))) {
					isDominioAbilitato = true;
				}
				
				if(!isTributoAbilitato && acl.getServizio().equals(servizio) && acl.getTipo().equals(Tipo.TRIBUTO) && (acl.getCodTributo() == null || acl.getCodTributo().equals(codTributo))) {
					isTributoAbilitato = true;
				}
			} else {
				if(!isDominioAbilitato && acl.getTipo().equals(Tipo.DOMINIO) && (acl.getCodDominio() == null || acl.getCodDominio().equals(codDominio))) {
					isDominioAbilitato = true;
				}
				
				if(!isTributoAbilitato && acl.getTipo().equals(Tipo.TRIBUTO) && (acl.getCodTributo() == null || acl.getCodTributo().equals(codTributo))) {
					isTributoAbilitato = true;
				}
			}
		}
		
		return isDominioAbilitato && isTributoAbilitato;
	}
	
	public static boolean isAuthorized(Operatore operatore, Servizio servizio, String codDominio, String codTributo) {
		
		// Controllo se ho il dominio
		boolean isDominioAbilitato = false;
		boolean isTributoAbilitato = false;
		
		for(Acl acl : operatore.getAcls()) {
			
			if(!isDominioAbilitato && acl.getServizio().equals(servizio) && acl.getTipo().equals(Tipo.DOMINIO) && (acl.getCodDominio() == null || acl.getCodDominio().equals(codDominio))) {
				isDominioAbilitato = true;
			}
			
			if(!isTributoAbilitato && acl.getServizio().equals(servizio) && acl.getTipo().equals(Tipo.TRIBUTO) && (acl.getCodTributo() == null || acl.getCodTributo().equals(codTributo))) {
				isTributoAbilitato = true;
				break;
			}
		}
		
		return isDominioAbilitato && isTributoAbilitato;
	}
	
	/** 
	 * Ritorna la lista dei domini autorizzati al servizio Rendicontazione per l'applicazione indicata
	 * 
	 * @param applicazione
	 * @param servizio
	 * @return
	 */
	public static Set<String> getAuthorizedRnd(Applicazione applicazione) {
		Set<String> domini = new HashSet<String>();
		for(Acl acl : applicazione.getAcls()) {
			if(acl.getServizio().equals(Servizio.RENDICONTAZIONE))
				domini.add(acl.getCodDominio());
		}
		return domini;
	}
	
	/** 
	 * Ritorna la lista dei domini autorizzati al servizio Rendicontazione per l'applicazione indicata
	 * 
	 * Se ritorna NULL indica che tutti i domini sono autorizzati
	 * 
	 * @param applicazione
	 * @param servizio
	 * @return
	 */
	public static Set<Long> getAuthorizedPagamenti(Portale portale) {
		Set<Long> domini = new HashSet<Long>();
		for(Acl acl : portale.getAcls()) {
			if(acl.getServizio().equals(Servizio.PAGAMENTI_ONLINE) || acl.getServizio().equals(Servizio.PAGAMENTI_ATTESA)) {
				if(acl.getIdDominio() != null)
					domini.add(acl.getIdDominio());
				else 
					return null;
			}
		}
		return domini;
	}

}
