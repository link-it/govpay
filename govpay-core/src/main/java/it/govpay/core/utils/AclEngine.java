package it.govpay.core.utils;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import it.govpay.model.Acl;
import it.govpay.model.IAutorizzato;
import it.govpay.model.Operatore;
import it.govpay.model.Ruolo;
import it.govpay.model.Acl.Servizio;
import it.govpay.model.Acl.Tipo;

public class AclEngine {

	public static boolean isAuthorized(IAutorizzato user, Servizio servizio, String codDominio, String codTributo) {

		// Controllo se ho il dominio
		boolean isDominioAbilitato = false;
		boolean isTributoAbilitato = (codTributo == null); // Se non ho indicato un codTributo, non ne controllo l'abilitazione.

		for(Acl acl : user.getAcls()) {

			// Se il controllo e' per Servizio.PAGAMENTI_ATTESA allora va bene anche l'abilitazione a PAGAMENTI_ONLINE

			if(servizio.equals(Servizio.PAGAMENTI_ATTESA)) {
				if(!isDominioAbilitato && (acl.getServizio().equals(Servizio.PAGAMENTI_ONLINE) || acl.getServizio().equals(Servizio.PAGAMENTI_ATTESA)) && acl.getTipo().equals(Tipo.DOMINIO) && (acl.getCodDominio() == null || acl.getCodDominio().equals(codDominio))) {
					isDominioAbilitato = true;
				}

				if(!isTributoAbilitato && (acl.getServizio().equals(Servizio.PAGAMENTI_ONLINE) || acl.getServizio().equals(Servizio.PAGAMENTI_ATTESA)) && acl.getTipo().equals(Tipo.TRIBUTO) && (acl.getCodTributo() == null || acl.getCodTributo().equals(codTributo))) {
					isTributoAbilitato = true;
				}
			} else {
				if(!isDominioAbilitato && acl.getServizio().equals(servizio) && acl.getTipo().equals(Tipo.DOMINIO) && (acl.getCodDominio() == null || acl.getCodDominio().equals(codDominio))) {
					isDominioAbilitato = true;
				}

				if(!isTributoAbilitato && acl.getServizio().equals(servizio) && acl.getTipo().equals(Tipo.TRIBUTO) && (acl.getCodTributo() == null || acl.getCodTributo().equals(codTributo))) {
					isTributoAbilitato = true;
				}
			}
		}

		return isDominioAbilitato && isTributoAbilitato;
	}

	public static int getTopDirittiOperatore(List<Ruolo> ruoli, Servizio servizio) {
		int diritti = 0;
		for(Ruolo ruolo : ruoli) {
			for(Acl acl : ruolo.getAcls()) {
				if(acl.getServizio().equals(servizio)) {
					if(diritti < acl.getDiritti()) diritti = acl.getDiritti();
					if(diritti == 2) return diritti;
				}
			}
		}
		return diritti;
	}

	public static int getTopDirittiOperatore(List<Ruolo> ruoli, Servizio servizio, String codDominio) {
		int diritti = 0;
		for(Ruolo ruolo : ruoli) {
			for(Acl acl : ruolo.getAcls()) {
				if(acl.getServizio().equals(servizio) && acl.getTipo().equals(Tipo.DOMINIO) && (acl.getCodDominio() == null || acl.getCodDominio().equals(codDominio))) {
					if(diritti < acl.getDiritti()) diritti = acl.getDiritti();
					if(diritti == 2) return diritti;
				}
			}
		}
		return 0;
	}

	/** 
	 * Ritorna la lista dei domini autorizzati al servizio Rendicontazione per l'applicazione indicata
	 * 
	 * @param applicazione
	 * @param servizio
	 * @return
	 */
	public static Set<String> getDominiAutorizzati(IAutorizzato user, Servizio servizio) {
		Set<String> domini = new HashSet<String>();
		for(Acl acl : user.getAcls()) {

			if(servizio.equals(Servizio.PAGAMENTI_ATTESA))
				if(acl.getTipo().equals(Tipo.DOMINIO) && (acl.getServizio().equals(Servizio.PAGAMENTI_ONLINE) || acl.getServizio().equals(Servizio.PAGAMENTI_ATTESA))) {
					if(acl.getIdDominio() != null)
						domini.add(acl.getCodDominio());
					else 
						return null;
				}

			if(acl.getTipo().equals(Tipo.DOMINIO) && acl.getServizio().equals(servizio)) {
				if(acl.getIdDominio() != null)
					domini.add(acl.getCodDominio());
				else 
					return null;
			}
		}
		return domini;
	}

	public static Set<String> getDominiAutorizzati(List<Ruolo> ruoli, Servizio servizio, int diritto) {
		Set<String> domini = new HashSet<String>();
		for(Ruolo ruolo : ruoli) {
			for(Acl acl : ruolo.getAcls()) {

				if(servizio.equals(Servizio.PAGAMENTI_ATTESA))
					if(acl.getDiritti() >= diritto && acl.getTipo().equals(Tipo.DOMINIO) && (acl.getServizio().equals(Servizio.PAGAMENTI_ONLINE) || acl.getServizio().equals(Servizio.PAGAMENTI_ATTESA))) {
						if(acl.getIdDominio() != null)
							domini.add(acl.getCodDominio());
						else 
							return null;
					}

				if(acl.getDiritti() >= diritto && acl.getTipo().equals(Tipo.DOMINIO) && acl.getServizio().equals(servizio)) {
					if(acl.getIdDominio() != null)
						domini.add(acl.getCodDominio());
					else 
						return null;
				}
			}
		}
		return domini;
	}
	
	public static Set<Long> getIdDominiAutorizzati(List<Ruolo> ruoli, Servizio servizio, int diritto) {
		Set<Long> domini = new HashSet<Long>();
		for(Ruolo ruolo : ruoli) {
			for(Acl acl : ruolo.getAcls()) {

				if(servizio.equals(Servizio.PAGAMENTI_ATTESA))
					if(acl.getDiritti() >= diritto && acl.getTipo().equals(Tipo.DOMINIO) && (acl.getServizio().equals(Servizio.PAGAMENTI_ONLINE) || acl.getServizio().equals(Servizio.PAGAMENTI_ATTESA))) {
						if(acl.getIdDominio() != null)
							domini.add(acl.getIdDominio());
						else 
							return null;
					}

				if(acl.getDiritti() >= diritto && acl.getTipo().equals(Tipo.DOMINIO) && acl.getServizio().equals(servizio)) {
					if(acl.getIdDominio() != null)
						domini.add(acl.getIdDominio());
					else 
						return null;
				}
			}
		}
		return domini;
	}

	public static Set<Long> getIdDominiAutorizzati(IAutorizzato user, Servizio servizio) {
		Set<Long> domini = new HashSet<Long>();
		for(Acl acl : user.getAcls()) {

			if(servizio.equals(Servizio.PAGAMENTI_ATTESA))
				if(acl.getTipo().equals(Tipo.DOMINIO) && (acl.getServizio().equals(Servizio.PAGAMENTI_ONLINE) || acl.getServizio().equals(Servizio.PAGAMENTI_ATTESA))) {
					if(acl.getIdDominio() != null)
						domini.add(acl.getIdDominio());
					else 
						return null;
				}

			if(acl.getTipo().equals(Tipo.DOMINIO) && acl.getServizio().equals(servizio)) {
				if(acl.getIdDominio() != null && acl.getDiritti() > 0)
					domini.add(acl.getIdDominio());
				else 
					return null;
			}
		}
		return domini;
	}
}
