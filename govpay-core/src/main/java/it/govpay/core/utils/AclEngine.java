package it.govpay.core.utils;

import java.util.ArrayList;
import java.util.List;

import it.govpay.model.Acl;
import it.govpay.model.Acl.Diritti;
import it.govpay.model.Acl.Servizio;
import it.govpay.model.IAutorizzato;
import it.govpay.model.Utenza.TIPO_UTENZA;
import it.govpay.bd.model.Utenza;
import it.govpay.core.exceptions.NotAuthenticatedException;
import it.govpay.core.exceptions.NotAuthorizedException;

public class AclEngine {
	
	public static final String CODICE_FISCALE_CITTADINO = "cf_cittadino";
	public static final String UTENZA_ANONIMA = "utenzaAnonima";
	
	public static NotAuthorizedException toNotAuthorizedException(IAutorizzato user) {
		StringBuilder sb = new StringBuilder();
		String utenza = user != null ? user.getIdentificativo() : null;
		if(utenza != null)
			sb.append("Utenza [").append(utenza).append("] non autorizzata.");
		else
			sb.append("Credenziali non fornite.");
		return new NotAuthorizedException(sb.toString());
	}
	
	public static NotAuthenticatedException toNotAuthenticatedException(IAutorizzato user) {
		StringBuilder sb = new StringBuilder();
		String utenza = user != null ? user.getIdentificativo() : null;
		if(utenza != null)
			sb.append("Utenza [").append(utenza).append("] non autorizzata.");
		else
			sb.append("Credenziali non fornite.");
		return new NotAuthenticatedException(sb.toString());
	}
	
	public static NotAuthorizedException toNotAuthorizedException(IAutorizzato user, List<Servizio> servizio, List<Diritti> listaDiritti, boolean accessoAnonimo) {
		StringBuilder sb = new StringBuilder();
		
		StringBuilder sbServizi = new StringBuilder();
		
		for (Diritti diritti : listaDiritti) {
			if(sbServizi.length() >0) 
				sbServizi.append(", ");
			
			sbServizi.append(diritti);
		}
		
		if(user.getTipoUtenza().equals(TIPO_UTENZA.ANONIMO) && !accessoAnonimo) {
			sb.append("Utenza [").append(user != null ? user.getIdentificativo() : "NON RICONOSCIUTA").append("] non puo' accedere in maniera anonima ai Servizi [").append(sbServizi).append("]");
			return new NotAuthorizedException(sb.toString());
		}
		
		StringBuilder sbDiritti = new StringBuilder();
		
		for (Diritti diritti : listaDiritti) {
			if(sbDiritti.length() >0) 
				sbDiritti.append(", ");
			
			sbDiritti.append(diritti);
		}
		
		sb.append("Utenza [").append(user != null ? user.getIdentificativo() : "NON RICONOSCIUTA").append("] non possiede i Diritti [").append(sbDiritti.toString()).append("] necessari per i Servizi [").append(sbServizi).append("]");
		
		return new NotAuthorizedException(sb.toString());

	}
	
	public static NotAuthorizedException toNotAuthorizedException(IAutorizzato user, Servizio servizio, List<Diritti> listaDiritti, boolean accessoAnonimo ) {
		StringBuilder sb = new StringBuilder();
		
		if(user.getTipoUtenza().equals(TIPO_UTENZA.ANONIMO) && !accessoAnonimo) {
			sb.append("Utenza [").append(user != null ? user.getIdentificativo() : "NON RICONOSCIUTA").append("] non puo' accedere in maniera anonima al Servizio [").append(servizio).append("]");
			return new NotAuthorizedException(sb.toString());
		}
		
		StringBuilder sbDiritti = new StringBuilder();
		
		for (Diritti diritti : listaDiritti) {
			if(sbDiritti.length() >0) 
				sbDiritti.append(", ");
			
			sbDiritti.append(diritti);
		}
		
		sb.append("Utenza [").append(user != null ? user.getIdentificativo() : "NON RICONOSCIUTA").append("] non possiede i Diritti [").append(sbDiritti.toString()).append("] necessari per il Servizio [").append(servizio).append("]");
		
		
		return new NotAuthorizedException(sb.toString());
	}
	
	public static NotAuthorizedException toNotAuthorizedException(IAutorizzato user, Servizio servizio, List<Diritti> listaDiritti, boolean accessoAnonimo, String codDominio, String codTributo) {
		StringBuilder sb = new StringBuilder();
		
		if(user.getTipoUtenza().equals(TIPO_UTENZA.ANONIMO) && !accessoAnonimo) {
			sb.append("Utenza [").append(user != null ? user.getIdentificativo() : "NON RICONOSCIUTA").append("] non puo' accedere in maniera anonima al Servizio [").append(servizio).append("]");
			return new NotAuthorizedException(sb.toString());
		}
		
		StringBuilder sbDiritti = new StringBuilder();
		
		for (Diritti diritti : listaDiritti) {
			if(sbDiritti.length() >0) 
				sbDiritti.append(", ");
			
			sbDiritti.append(diritti);
		}
		
		sb.append("Utenza [").append(user != null ? user.getIdentificativo() : "NON RICONOSCIUTA").append("] non possiede i Diritti [").append(sbDiritti.toString()).append("] necessari per il Servizio [").append(servizio).append("]");
		
		
		return new NotAuthorizedException(sb.toString());
	}
	
	public static boolean isAuthorizedLettura(IAutorizzato user, Servizio servizio, boolean accessoAnonimo) throws NotAuthorizedException {
		return isAuthorized(user, servizio, Diritti.LETTURA, accessoAnonimo);
	}
	
	public static boolean isAuthorizedScrittura(IAutorizzato user, Servizio servizio, boolean accessoAnonimo)  throws NotAuthorizedException {
		return isAuthorized(user, servizio, Diritti.SCRITTURA, accessoAnonimo);
	}
	
	public static boolean isAuthorizedEsecuzione(IAutorizzato user, Servizio servizio, boolean accessoAnonimo)  throws NotAuthorizedException {
		return isAuthorized(user, servizio, Diritti.ESECUZIONE, accessoAnonimo);
	}
	
	public static boolean isAuthorized(IAutorizzato user, Servizio servizio, Diritti diritto, boolean accessoAnonimo) {
		List<Diritti> listaDiritti = new ArrayList<>();
		listaDiritti.add(diritto);
		return isAuthorized(user, servizio, listaDiritti, accessoAnonimo);
	}

	public static boolean isAuthorized(IAutorizzato user, Servizio servizio, List<Diritti> listaDiritti, boolean accessoAnonimo) {
		// controllo se l'utenza anonima e' abilitata per l'operazione richiesta.
		if(user.getTipoUtenza().equals(TIPO_UTENZA.ANONIMO) && !accessoAnonimo) {
			return false;
		}
		
		for(Acl acl : user.getAcls()) {
			
			if(acl.getServizio().equals(servizio)) {
				for (Diritti dirittoTmp : listaDiritti) {
					if(acl.getListaDiritti().contains(dirittoTmp))
						return true;
				}
			}
		}

		return false;
	}
	
	public static boolean isAuthorized(IAutorizzato user, Servizio servizio, String codDominio, String codTributo, List<Diritti> listaDiritti) {
		return isAuthorized(user, servizio, codDominio, codTributo, listaDiritti, false);
	}
	
	public static boolean isAuthorized(IAutorizzato user, Servizio servizio, String codDominio, String codTributo, List<Diritti> listaDiritti, boolean accessoAnonimo) {
		boolean authorized = isAuthorized(user, servizio, listaDiritti, accessoAnonimo); 
		
		if(authorized) {
			switch (user.getTipoUtenza()) {
			case CITTADINO:
				// cittadino autorizzato in maniera diversa rispetto ad applicazioni e operatori.	
				break;
			case ANONIMO:
				break;
			case APPLICAZIONE:
			case OPERATORE:
			default:
				if(codDominio != null) {
					List<String> dominiAutorizzati = getDominiAutorizzati((Utenza) user, servizio, listaDiritti, accessoAnonimo);
					
					if(dominiAutorizzati == null) 
						return false;
					
					if(!dominiAutorizzati.isEmpty())
						authorized = authorized && dominiAutorizzati.contains(codDominio);
				}
				
				if(codTributo != null) {
					List<String> tributiAutorizzati = getTributiAutorizzati((Utenza) user, servizio, listaDiritti, accessoAnonimo);
					
					if(tributiAutorizzati == null) 
						return false;
					
					if(!tributiAutorizzati.isEmpty())
						authorized = authorized && tributiAutorizzati.contains(codTributo);
				}
				break;
			}
		}
		
		return authorized;
	}
	
	public static List<Long> getIdDominiAutorizzati(IAutorizzato utenza, Servizio servizio, Diritti diritto) {
		return getIdDominiAutorizzati(utenza, servizio, diritto, false);
	}
	
	public static List<Long> getIdDominiAutorizzati(IAutorizzato utenza, Servizio servizio, Diritti diritto, boolean accessoAnonimo) {
		List<Diritti> listaDiritti = new ArrayList<>();
		listaDiritti.add(diritto);
		return getIdDominiAutorizzati(utenza, servizio, listaDiritti, accessoAnonimo);
	}
	
	public static List<String> getDominiAutorizzati(IAutorizzato utenza, Servizio servizio, Diritti diritto) {
		return getDominiAutorizzati(utenza,servizio,diritto,false);
	}
	
	public static List<String> getDominiAutorizzati(IAutorizzato utenza, Servizio servizio, Diritti diritto, boolean accessoAnonimo) {
		List<Diritti> listaDiritti = new ArrayList<>();
		listaDiritti.add(diritto);
		return getDominiAutorizzati(utenza, servizio, listaDiritti, accessoAnonimo);
	}

	public static List<String> getTributiAutorizzati(IAutorizzato utenza, Servizio servizio, Diritti diritto, boolean accessoAnonimo) {
		List<Diritti> listaDiritti = new ArrayList<>();
		listaDiritti.add(diritto);
		return getTributiAutorizzati(utenza, servizio, listaDiritti, accessoAnonimo);
	}

	public static List<Long> getIdTributiAutorizzati(Utenza utenza, Servizio servizio, Diritti diritto, boolean accessoAnonimo) {
		List<Diritti> listaDiritti = new ArrayList<>();
		listaDiritti.add(diritto);
		return getIdTributiAutorizzati(utenza, servizio, listaDiritti, accessoAnonimo);
	}
	

	public static List<Long> getIdDominiAutorizzati(IAutorizzato utenza, Servizio servizio, List<Diritti> diritti) {
		return getIdDominiAutorizzati(utenza, servizio, diritti, false);
	}	

	public static List<Long> getIdDominiAutorizzati(IAutorizzato utenza, Servizio servizio, List<Diritti> diritti, boolean accessoAnonimo) {
		if(isAuthorized(utenza, servizio, diritti, accessoAnonimo)) {
			return utenza.getIdDomini();
		} else {
			return null;
		}
	}
	
	public static List<String> getDominiAutorizzati(IAutorizzato utenza, Servizio servizio, List<Diritti> diritti) {
		return getDominiAutorizzati(utenza, servizio, diritti, false);
	}
	
	public static List<String> getDominiAutorizzati(IAutorizzato utenza, Servizio servizio, List<Diritti> diritti, boolean accessoAnonimo) {
		if(isAuthorized(utenza, servizio, diritti, accessoAnonimo)) {
			return utenza.getIdDominio();
		} else {
			return null;
		}
	}

	public static List<String> getTributiAutorizzati(IAutorizzato utenza, Servizio servizio, List<Diritti> diritti, boolean accessoAnonimo) {
		if(isAuthorized(utenza, servizio, diritti, accessoAnonimo)) {
			return utenza.getIdTributo();
		} else {
			return null;
		}
	}

	public static List<Long> getIdTributiAutorizzati(Utenza utenza, Servizio servizio, List<Diritti> diritti, boolean accessoAnonimo) {
		if(isAuthorized(utenza, servizio, diritti, accessoAnonimo)) {
			return utenza.getIdTributi();
		} else {
			return null;
		}
	} 
}