package it.govpay.core.utils;

import it.govpay.bd.model.Acl;
import it.govpay.bd.model.Acl.Servizio;
import it.govpay.bd.model.Acl.Tipo;
import it.govpay.bd.model.Applicazione;
import it.govpay.bd.model.Operatore;
import it.govpay.bd.model.Portale;

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

}
