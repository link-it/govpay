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
package it.govpay.bd.anagrafica;

import java.util.ArrayList;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.List;

import org.openspcoop2.generic_project.beans.UpdateField;
import org.openspcoop2.generic_project.exception.ExpressionException;
import org.openspcoop2.generic_project.exception.ExpressionNotImplementedException;
import org.openspcoop2.generic_project.exception.MultipleResultException;
import org.openspcoop2.generic_project.exception.NotFoundException;
import org.openspcoop2.generic_project.exception.NotImplementedException;
import org.openspcoop2.generic_project.exception.ServiceException;
import org.openspcoop2.generic_project.expression.IExpression;
import org.openspcoop2.generic_project.expression.IPaginatedExpression;
import org.openspcoop2.generic_project.expression.LikeMode;
import org.openspcoop2.generic_project.expression.SortOrder;
import org.openspcoop2.utils.UtilsException;
import org.openspcoop2.utils.certificate.CertificateUtils;
import org.openspcoop2.utils.certificate.PrincipalType;

import it.govpay.bd.BDConfigWrapper;
import it.govpay.bd.BasicBD;
import it.govpay.bd.anagrafica.filters.AclFilter;
import it.govpay.bd.anagrafica.filters.ApplicazioneFilter;
import it.govpay.bd.model.Acl;
import it.govpay.bd.model.Applicazione;
import it.govpay.bd.model.Utenza;
import it.govpay.bd.model.UtenzaApplicazione;
import it.govpay.bd.model.converter.ApplicazioneConverter;
import it.govpay.bd.model.converter.ConnettoreConverter;
import it.govpay.model.Connettore;
import it.govpay.orm.IdApplicazione;
import it.govpay.orm.dao.jdbc.JDBCApplicazioneServiceSearch;

public class ApplicazioniBD extends BasicBD {

	public ApplicazioniBD(BasicBD basicBD) {
		super(basicBD);
	}

	public ApplicazioniBD(String idTransaction) {
		super(idTransaction);
	}
	
	public ApplicazioniBD(String idTransaction, boolean useCache) {
		super(idTransaction, useCache);
	}
	
	public ApplicazioniBD(BDConfigWrapper configWrapper) {
		super(configWrapper.getTransactionID(), configWrapper.isUseCache());
	}
	

	/**
	 * Recupera l'applicazione tramite l'id fisico
	 * 
	 * @param idApplicazione
	 * @return
	 * @throws NotFoundException se non esiste.
	 * @throws MultipleResultException in caso di duplicati.
	 * @throws ServiceException in caso di errore DB.
	 */
	public Applicazione getApplicazione(Long idApplicazione) throws NotFoundException, ServiceException {
		if(idApplicazione == null) {
			throw new ServiceException("Parameter 'id' cannot be NULL");
		}

		try {
			if(this.isAtomica()) {
				this.setupConnection(this.getIdTransaction());
			}
			long id = idApplicazione.longValue();

			it.govpay.orm.Applicazione applicazioneVO = ((JDBCApplicazioneServiceSearch)this.getApplicazioneService()).get(id);
			Applicazione applicazione = this.getApplicazione(applicazioneVO);

			return applicazione;
		} catch (NotImplementedException | MultipleResultException e) {
			throw new ServiceException(e);
		} finally {
			if(this.isAtomica()) {
				this.closeConnection();
			}
		}
	}

	/**
	 * Recupera l'applicazione tramite l'id logico
	 * 
	 * @param codEnte
	 * @return
	 * @throws NotFoundException se l'ente non esiste.
	 * @throws MultipleResultException in caso di duplicati.
	 * @throws ServiceException in caso di errore DB.
	 */
	public Applicazione getApplicazione(String codApplicazione) throws NotFoundException, ServiceException {
		try {
			IdApplicazione id = new IdApplicazione();
			id.setCodApplicazione(codApplicazione);
			
			if(this.isAtomica()) {
				this.setupConnection(this.getIdTransaction());
			}
			
			it.govpay.orm.Applicazione applicazioneVO = this.getApplicazioneService().get(id);
			Applicazione applicazione = this.getApplicazione(applicazioneVO);
			return applicazione;
		} catch (NotImplementedException | MultipleResultException e) {
			throw new ServiceException(e);
		} finally {
			if(this.isAtomica()) {
				this.closeConnection();
			}
		}
	}

	/**
	 * Recupera l'applicazione identificata dal Principal fornito
	 * 
	 * @param idEnte
	 * @return
	 * @throws NotFoundException se l'ente non esiste.
	 * @throws ServiceException in caso di errore DB.
	 */
	public Applicazione getApplicazioneByPrincipal(String principal) throws NotFoundException, MultipleResultException, ServiceException {
		try {
			if(this.isAtomica()) {
				this.setupConnection(this.getIdTransaction());
			}
			
			IExpression exp = this.getApplicazioneService().newExpression();
			exp.equals(it.govpay.orm.Applicazione.model().ID_UTENZA.PRINCIPAL, principal);
			it.govpay.orm.Applicazione applicazioneVO = this.getApplicazioneService().find(exp);
			Applicazione applicazione = this.getApplicazione(applicazioneVO);

			return applicazione;
		} catch (NotImplementedException e) {
			throw new ServiceException(e);
		} catch (ExpressionNotImplementedException e) {
			throw new ServiceException(e);
		} catch (ExpressionException e) {
			throw new ServiceException(e);
		} finally {
			if(this.isAtomica()) {
				this.closeConnection();
			}
		}

	}
	/**
	 * Recupera l'applicazione identificata dal Principal fornito
	 * 
	 * @param idEnte
	 * @return
	 * @throws NotFoundException se l'ente non esiste.
	 * @throws ServiceException in caso di errore DB.
	 */
	public Applicazione getApplicazioneBySubject(String principal) throws NotFoundException, MultipleResultException, ServiceException {
		try {
			if(this.isAtomica()) {
				this.setupConnection(this.getIdTransaction());
			}
			
			IExpression expr = this.getApplicazioneService().newExpression();
			Hashtable<String,List<String>> hashSubject = null;
			try {
				hashSubject = CertificateUtils.getPrincipalIntoHashtable(principal,PrincipalType.subject);
			}catch(UtilsException e) {
				throw new NotFoundException("Utenza" + principal + "non autorizzata");
			}

			Enumeration<String> keys = hashSubject.keys();
			while(keys.hasMoreElements()){
				String key = keys.nextElement();
				List<String> listValues = hashSubject.get(key);
				for (String value : listValues) {
					expr.like(it.govpay.orm.Applicazione.model().ID_UTENZA.PRINCIPAL, "/"+CertificateUtils.formatKeyPrincipal(key)+"="+CertificateUtils.formatValuePrincipal(value)+"/", LikeMode.ANYWHERE);
				}
			}
			
			it.govpay.orm.Applicazione applicazioneVO = this.getApplicazioneService().find(expr);
			Applicazione applicazione = this.getApplicazione(applicazioneVO);

			return applicazione;
		} catch (NotImplementedException  | ExpressionNotImplementedException | ExpressionException e) { 
			throw new ServiceException(e);
		} finally {
			if(this.isAtomica()) {
				this.closeConnection();
			}
		}

	}


	/**
	 * Aggiorna l'applicazione con i dati forniti
	 * @throws NotFoundException se non esiste
	 * @throws ServiceException in caso di errore DB.
	 */
	public void updateApplicazioneTrusted(Long id, String codApplicazione, boolean trusted) throws NotFoundException, ServiceException {
		try {
			if(this.isAtomica()) {
				this.setupConnection(this.getIdTransaction());
			}
			
			IdApplicazione idVO = new IdApplicazione();
			idVO.setId(id);
			idVO.setCodApplicazione(codApplicazione);
			List<UpdateField> lstUpdateFields = new ArrayList<>();
			lstUpdateFields.add(new UpdateField(it.govpay.orm.Applicazione.model().TRUSTED, trusted));

			this.getApplicazioneService().updateFields(idVO, lstUpdateFields.toArray(new UpdateField[]{}));
		} catch (NotImplementedException e) {
			throw new ServiceException(e);
		} catch (NotFoundException e) {
			throw new ServiceException(e);
		} finally {
			if(this.isAtomica()) {
				this.closeConnection();
			}
		}
	}
	/**
	 * Aggiorna l'applicazione con i dati forniti
	 * @param ente
	 * @throws NotFoundException se non esiste
	 * @throws ServiceException in caso di errore DB.
	 */
	public void updateApplicazione(Applicazione applicazione) throws NotFoundException, ServiceException {
		try {
			if(this.isAtomica()) {
				this.setupConnection(this.getIdTransaction());
			}
			
			UtenzeBD utenzeBD = new UtenzeBD(this);
			utenzeBD.setAtomica(false);
			// autocommit false		
			this.setAutoCommit(false);

			// prelevo la vecchia utenza
			Applicazione applicazioneOld = this.getApplicazione(applicazione.getCodApplicazione());

			Utenza utenzaApplicazione = applicazione.getUtenza();
			boolean insertNuovaUtenza = !utenzeBD.exists(utenzaApplicazione);
			if(insertNuovaUtenza) { // se ho cambiato il principal dell'applicazione non trovero' una utenza associata

				// copio ACL
				AclBD aclBD = new AclBD(this);
				AclFilter filter = aclBD.newFilter();
				filter.setIdUtenza(applicazioneOld.getUtenza().getId());
				List<Acl> alcEsistenti = aclBD.findAll(filter);

				List<Acl> listaAclPrincipal =  null;
				if(alcEsistenti != null) {
					listaAclPrincipal = new ArrayList<Acl>();
					for (Acl aclPrincipalOld : alcEsistenti) {
						aclPrincipalOld.setIdUtenza(utenzaApplicazione.getId());
						listaAclPrincipal.add(aclPrincipalOld);
					}
				}

				utenzaApplicazione.setAclPrincipal(listaAclPrincipal);

				// Domini
				utenzaApplicazione.setIdDominiUo(applicazioneOld.getUtenza().getIdDominiUo());

				// TipiVersamento
				utenzaApplicazione.setIdTipiVersamento(applicazioneOld.getUtenza().getIdTipiVersamento());

				utenzeBD.insertUtenza(utenzaApplicazione);
			} else {
				try {
					utenzeBD.updateUtenza(utenzaApplicazione);
				} catch(NotFoundException e) {
					throw new ServiceException(e);
				}
			}

			applicazione.setIdUtenza(utenzaApplicazione.getId());

			it.govpay.orm.Applicazione vo = ApplicazioneConverter.toVO(applicazione);
			IdApplicazione id = this.getApplicazioneService().convertToId(vo);

			if(!this.getApplicazioneService().exists(id)) {
				throw new NotFoundException("Applicazione con id ["+id+"] non esiste.");
			}

			this.getApplicazioneService().update(id, vo);
			applicazione.setId(vo.getId());

			if(applicazione.getConnettoreIntegrazione() != null) {
				List<it.govpay.orm.Connettore> voConnettoreEsitoLst = ConnettoreConverter.toVOList(applicazione.getConnettoreIntegrazione());

				IExpression expDelete = this.getConnettoreService().newExpression();
				expDelete.equals(it.govpay.orm.Connettore.model().COD_CONNETTORE, applicazione.getConnettoreIntegrazione().getIdConnettore());
				this.getConnettoreService().deleteAll(expDelete);


				for(it.govpay.orm.Connettore connettore: voConnettoreEsitoLst) {

					this.getConnettoreService().create(connettore);
				}
			}

			if(insertNuovaUtenza) {
				// elimino la vecchia utenza
				utenzeBD.deleteUtenza(applicazioneOld.getUtenza());
			}


			this.emitAudit(applicazione);
			this.commit();

		} catch (NotImplementedException | MultipleResultException | ExpressionNotImplementedException | ExpressionException e) {
			this.rollback();
			throw new ServiceException(e);
		} finally {
			// ripristino l'autocommit.
			this.setAutoCommit(true); 
			
			if(this.isAtomica()) {
				this.closeConnection();
			}
		}

	}

	/**
	 * Crea una nuova applicazione con i dati forniti
	 * @param applicazione
	 * @throws ServiceException in caso di errore DB.
	 */
	public void insertApplicazione(Applicazione applicazione) throws ServiceException{
		try {
			if(this.isAtomica()) {
				this.setupConnection(this.getIdTransaction());
			}
			
			UtenzeBD utenzeBD = new UtenzeBD(this);
			utenzeBD.setAtomica(false);
			// autocommit false		
			this.setAutoCommit(false);

			if(!utenzeBD.exists(applicazione.getUtenza())) {
				utenzeBD.insertUtenza(applicazione.getUtenza());
			} else {
				try {
					utenzeBD.updateUtenza(applicazione.getUtenza());
				} catch(NotFoundException e) {
					throw new ServiceException(e);
				}
			}
			applicazione.setIdUtenza(applicazione.getUtenza().getId());

			it.govpay.orm.Applicazione vo = ApplicazioneConverter.toVO(applicazione);
			this.getApplicazioneService().create(vo);
			applicazione.setId(vo.getId());

			if(applicazione.getConnettoreIntegrazione() != null) {
				List<it.govpay.orm.Connettore> voConnettoreEsitoLst = ConnettoreConverter.toVOList(applicazione.getConnettoreIntegrazione());

				IExpression expDelete = this.getConnettoreService().newExpression();
				expDelete.equals(it.govpay.orm.Connettore.model().COD_CONNETTORE, applicazione.getConnettoreIntegrazione().getIdConnettore());
				this.getConnettoreService().deleteAll(expDelete);

				for(it.govpay.orm.Connettore connettore: voConnettoreEsitoLst) {
					this.getConnettoreService().create(connettore);
				}
			}

			this.emitAudit(applicazione);

			this.commit();
			// ripristino l'autocommit.
			this.setAutoCommit(true); 

		} catch (NotImplementedException e) {
			throw new ServiceException(e);
		} catch (ExpressionNotImplementedException e) {
			throw new ServiceException(e);
		} catch (ExpressionException e) {
			throw new ServiceException(e);
		} finally {
			if(this.isAtomica()) {
				this.closeConnection();
			}
		}
	}



	public ApplicazioneFilter newFilter() throws ServiceException {
		return new ApplicazioneFilter(this.getApplicazioneService());
	}

	public ApplicazioneFilter newFilter(boolean simpleSearch) throws ServiceException {
		return new ApplicazioneFilter(this.getApplicazioneService(),simpleSearch);
	}

	public long count(ApplicazioneFilter filter) throws ServiceException {
		try {
			if(this.isAtomica()) {
				this.setupConnection(this.getIdTransaction());
				filter.setExpressionConstructor(this.getApplicazioneService());
			}
			
			return this.getApplicazioneService().count(filter.toExpression()).longValue();
		} catch (NotImplementedException e) {
			throw new ServiceException(e);
		} finally {
			if(this.isAtomica()) {
				this.closeConnection();
			}
		}
	}

	public List<Applicazione> findAll(ApplicazioneFilter filter) throws ServiceException {
		try {
			if(this.isAtomica()) {
				this.setupConnection(this.getIdTransaction());
				filter.setExpressionConstructor(this.getApplicazioneService());
			}
			
			List<Applicazione> dtoList = new ArrayList<>();
			for(it.govpay.orm.Applicazione vo: this.getApplicazioneService().findAll(filter.toPaginatedExpression())) {
				dtoList.add(this.getApplicazione(vo));
			}
			return dtoList;
		} catch (NotImplementedException e) {
			throw new ServiceException(e);
		} finally {
			if(this.isAtomica()) {
				this.closeConnection();
			}
		}
	}

	public List<String> findAllCodApplicazione(ApplicazioneFilter filter) throws ServiceException {
		List<String> lstApplicazioni = new ArrayList<>();

		try {	
			if(this.isAtomica()) {
				this.setupConnection(this.getIdTransaction());
				filter.setExpressionConstructor(this.getApplicazioneService());
			}
			
			IPaginatedExpression exp = filter.toPaginatedExpression();
			exp.addOrder(it.govpay.orm.Applicazione.model().COD_APPLICAZIONE, SortOrder.ASC);
			List<Object> findAll = this.getApplicazioneService().select(exp, it.govpay.orm.Applicazione.model().COD_APPLICAZIONE);

			for (Object object : findAll) {
				if(object instanceof String) {
					lstApplicazioni.add((String) object);
				}
			}

			return lstApplicazioni;
		} catch (NotFoundException e) {
			return lstApplicazioni;
		} catch (NotImplementedException | ExpressionNotImplementedException | ExpressionException e) {
			throw new ServiceException(e);
		} finally {
			if(this.isAtomica()) {
				this.closeConnection();
			}
		}
	}

	private Applicazione getApplicazione(it.govpay.orm.Applicazione applicazioneVO) throws ServiceException {
		try {
			Connettore connettoreIntegrazione = null;
			
			if(applicazioneVO.getCodConnettoreIntegrazione()!= null) {
				IPaginatedExpression expIntegrazione = this.getConnettoreService().newPaginatedExpression();
				expIntegrazione.equals(it.govpay.orm.Connettore.model().COD_CONNETTORE, applicazioneVO.getCodConnettoreIntegrazione());
				connettoreIntegrazione = ConnettoreConverter.toDTO(this.getConnettoreService().findAll(expIntegrazione));
			}

			Applicazione applicazione = ApplicazioneConverter.toDTO(applicazioneVO, connettoreIntegrazione);
			UtenzeBD utenzeBD = new UtenzeBD(this);
			utenzeBD.setAtomica(false); // non deve aprire una nuova connessione
			applicazione.setUtenza(new UtenzaApplicazione(utenzeBD.getUtenza(applicazioneVO.getIdUtenza().getId()), applicazione.getCodApplicazione()));
			return applicazione;
		} catch (ExpressionNotImplementedException | MultipleResultException | NotFoundException e) {
			throw new ServiceException(e);
		} catch (ExpressionException e) {
			throw new ServiceException(e);
		} catch (NotImplementedException e) {
			throw new ServiceException(e);
		} 
	}
}
