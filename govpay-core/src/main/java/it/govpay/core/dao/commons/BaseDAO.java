package it.govpay.core.dao.commons;

import java.util.ArrayList;
import java.util.List;

import org.openspcoop2.generic_project.exception.ServiceException;

import it.govpay.bd.BasicBD;
import it.govpay.bd.anagrafica.AnagraficaManager;
import it.govpay.bd.model.Applicazione;
import it.govpay.bd.model.Operatore;
import it.govpay.core.exceptions.NotAuthenticatedException;
import it.govpay.core.exceptions.NotAuthorizedException;
import it.govpay.core.utils.AclEngine;
import it.govpay.core.utils.GpThreadLocal;
import it.govpay.model.Acl.Diritti;
import it.govpay.model.Acl.Servizio;
import it.govpay.model.IAutorizzato;

public class BaseDAO {

	public void populateUser(IAutorizzato user, BasicBD bd) throws NotAuthenticatedException, NotAuthorizedException, ServiceException {
		if(user == null)
			throw AclEngine.toNotAuthenticatedException(user);

		try {
			Applicazione applicazione = AnagraficaManager.getApplicazioneByPrincipal(bd, user.getPrincipal(), user.isCheckSubject());
			user.merge(applicazione.getUtenza());
		} catch (org.openspcoop2.generic_project.exception.NotFoundException e) { 
			try {
				Operatore operatore = AnagraficaManager.getOperatore(bd, user.getPrincipal());
				user.merge(operatore.getUtenza());
			} catch (org.openspcoop2.generic_project.exception.NotFoundException ex) {
				throw AclEngine.toNotAuthorizedException(user);					
			}
		}
	}

	public void autorizzaRichiesta(IAutorizzato user,Servizio servizio, Diritti diritti) throws NotAuthenticatedException, NotAuthorizedException, ServiceException {
		List<Diritti> listaDiritti = new ArrayList<Diritti>();
		listaDiritti.add(diritti);
		this.autorizzaRichiesta(user, servizio, listaDiritti);
	}

	public void autorizzaRichiesta(IAutorizzato user, Servizio servizio, Diritti diritti, BasicBD bd) throws NotAuthenticatedException, NotAuthorizedException, ServiceException{
		List<Diritti> listaDiritti = new ArrayList<Diritti>();
		listaDiritti.add(diritti);
		this.autorizzaRichiesta(user, servizio, listaDiritti, bd);
	}

	public void autorizzaRichiesta(IAutorizzato user,Servizio servizio, List<Diritti> diritti) throws NotAuthenticatedException, NotAuthorizedException, ServiceException {
		BasicBD bd = null;

		try {
			bd = BasicBD.newInstance(GpThreadLocal.get().getTransactionId());
			this.autorizzaRichiesta(user, servizio, diritti, bd); 
		} finally {
			if(bd != null)
				bd.closeConnection();
		}
	}

	public void autorizzaRichiesta(IAutorizzato user, Servizio servizio, List<Diritti> diritti, BasicBD bd) throws NotAuthenticatedException, NotAuthorizedException, ServiceException{
		// 1. integro le informazioni dell'utente
		this.populateUser(user, bd);

		// 2. invocazione acl engine
		boolean authorized = AclEngine.isAuthorized(user, servizio, diritti);

		if(!authorized)
			throw AclEngine.toNotAuthorizedException(user, servizio, diritti);
	}



	public void autorizzaRichiesta(IAutorizzato user,Servizio servizio, Diritti diritti, String codDominio, String codTributo) throws NotAuthenticatedException, NotAuthorizedException, ServiceException {
		List<Diritti> listaDiritti = new ArrayList<Diritti>();
		listaDiritti.add(diritti);
		this.autorizzaRichiesta(user, servizio, listaDiritti, codDominio, codTributo);
	}

	public void autorizzaRichiesta(IAutorizzato user, Servizio servizio, Diritti diritti, String codDominio, String codTributo, BasicBD bd) throws NotAuthenticatedException, NotAuthorizedException, ServiceException{
		List<Diritti> listaDiritti = new ArrayList<Diritti>();
		listaDiritti.add(diritti);
		this.autorizzaRichiesta(user, servizio, listaDiritti, codDominio, codTributo, bd); 
	}


	public void autorizzaRichiesta(IAutorizzato user,Servizio servizio, List<Diritti> diritti, String codDominio, String codTributo) throws NotAuthenticatedException, NotAuthorizedException, ServiceException {
		BasicBD bd = null;

		try {
			bd = BasicBD.newInstance(GpThreadLocal.get().getTransactionId());
			this.autorizzaRichiesta(user, servizio, diritti, codDominio, codTributo, bd); 
		} finally {
			if(bd != null)
				bd.closeConnection();
		}
	}

	public void autorizzaRichiesta(IAutorizzato user, Servizio servizio, List<Diritti> diritti, String codDominio, String codTributo, BasicBD bd) throws NotAuthenticatedException, NotAuthorizedException, ServiceException{
		// 1. integro le informazioni dell'utente
		this.populateUser(user, bd);

		// 2. invocazione acl engine
		boolean authorized = AclEngine.isAuthorized(user, servizio, codDominio, codTributo, diritti);

		if(!authorized)
			throw AclEngine.toNotAuthorizedException(user, servizio, diritti, codDominio, codTributo);
	}
}
