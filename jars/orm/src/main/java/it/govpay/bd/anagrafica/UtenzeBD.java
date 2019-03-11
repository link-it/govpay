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
import java.util.stream.Collectors;

import org.openspcoop2.generic_project.beans.CustomField;
import org.openspcoop2.generic_project.exception.ExpressionException;
import org.openspcoop2.generic_project.exception.ExpressionNotImplementedException;
import org.openspcoop2.generic_project.exception.MultipleResultException;
import org.openspcoop2.generic_project.exception.NotFoundException;
import org.openspcoop2.generic_project.exception.NotImplementedException;
import org.openspcoop2.generic_project.exception.ServiceException;
import org.openspcoop2.generic_project.expression.IExpression;
import org.openspcoop2.generic_project.expression.IPaginatedExpression;
import org.openspcoop2.generic_project.expression.LikeMode;
import org.openspcoop2.utils.Utilities;
import org.openspcoop2.utils.UtilsException;

import it.govpay.bd.BasicBD;
import it.govpay.bd.anagrafica.filters.AclFilter;
import it.govpay.bd.model.Utenza;
import it.govpay.bd.model.converter.UtenzaConverter;
import it.govpay.model.Acl;
import it.govpay.orm.IdDominio;
import it.govpay.orm.IdTipoTributo;
import it.govpay.orm.IdUtenza;
import it.govpay.orm.UtenzaDominio;
import it.govpay.orm.UtenzaTipoTributo;
import it.govpay.orm.dao.jdbc.JDBCUtenzaServiceSearch;
import it.govpay.orm.dao.jdbc.converter.UtenzaDominioFieldConverter;
import it.govpay.orm.dao.jdbc.converter.UtenzaTipoTributoFieldConverter;

public class UtenzeBD extends BasicBD {

	public UtenzeBD(BasicBD basicBD) {
		super(basicBD);
	}

	/**
	 * Recupera l'utenza identificata dalla chiave fisica
	 * 
	 * @param idUtenza
	 * @return
	 * @throws NotFoundException
	 * @throws MultipleResultException
	 * @throws ServiceException
	 */
	public Utenza getUtenza(Long idUtenza) throws NotFoundException, MultipleResultException, ServiceException {

		if(idUtenza== null) {
			throw new ServiceException("Parameter 'id' cannot be NULL");
		}

		long id = idUtenza.longValue();


		try {
			it.govpay.orm.Utenza utenzaVO = ((JDBCUtenzaServiceSearch)this.getUtenzaService()).get(id);
			return this.getUtenza(utenzaVO);
		} catch (NotImplementedException e) {
			throw new ServiceException(e);
		}
	}
	/**
	 * Recupera l'utenza identificata dalla chiave fisica
	 * 
	 * @param idUtenza
	 * @return
	 * @throws NotFoundException
	 * @throws MultipleResultException
	 * @throws ServiceException
	 */
	public boolean exists(Utenza utenza) throws ServiceException {
		try {
			IExpression expr = this.getUtenzaService().newExpression();
			expr.equals(it.govpay.orm.Utenza.model().PRINCIPAL_ORIGINALE, utenza.getPrincipalOriginale());
			return this.getUtenzaService().count(expr).longValue() > 0 ;
		} catch (NotImplementedException e) {
			throw new ServiceException(e);
		} catch (ExpressionNotImplementedException | ExpressionException  e) {
			throw new ServiceException(e);
		}
	}
	
	/**
	 *  check esistenza dell'utenza identificata dalla chiave fisica
	 * 
	 * @param principal
	 * @return
	 * @throws ServiceException
	 */
	public boolean existsByPrincipalOriginale(String principal) throws ServiceException {
		try {
			IExpression expr = this.getUtenzaService().newExpression();
			expr.equals(it.govpay.orm.Utenza.model().PRINCIPAL_ORIGINALE, principal);
			return  this.getUtenzaService().count(expr).longValue() > 0 ;
		} catch (NotImplementedException | ExpressionNotImplementedException | ExpressionException  e) {
			throw new ServiceException(e);
		}
	}

	/**
	 * Recupera l'utenza identificato dalla chiave logica
	 * 
	 * @param principal
	 * @return
	 * @throws NotFoundException
	 * @throws MultipleResultException
	 * @throws ServiceException
	 */
	public Utenza getUtenza(String principal) throws NotFoundException, MultipleResultException, ServiceException {
		return this.getUtenza(principal, false);
	}

	/**
	 * Recupera l'utenza identificato dalla chiave logica
	 * 
	 * @param principal
	 * @return
	 * @throws NotFoundException
	 * @throws MultipleResultException
	 * @throws ServiceException
	 */
	public Utenza getUtenza(String principal, boolean checkIgnoreCase) throws NotFoundException, MultipleResultException, ServiceException {
		try {
			IExpression expr = this.getUtenzaService().newExpression();
			if(checkIgnoreCase)
				expr.ilike(it.govpay.orm.Utenza.model().PRINCIPAL_ORIGINALE, principal, LikeMode.EXACT);
			else 
				expr.equals(it.govpay.orm.Utenza.model().PRINCIPAL_ORIGINALE, principal);

			it.govpay.orm.Utenza utenzaVO = this.getUtenzaService().find(expr);
			return this.getUtenza(utenzaVO);
		} catch (NotImplementedException e) {
			throw new ServiceException(e);
		} catch (ExpressionNotImplementedException e) {
			throw new ServiceException(e);
		} catch (ExpressionException e) {
			throw new ServiceException(e);
		}
	}


	private Utenza getUtenza(it.govpay.orm.Utenza utenzaVO) throws ServiceException, NotFoundException, MultipleResultException, NotImplementedException {

		List<Long> utenzaDominioLst = this.getUtenzeDominio(utenzaVO.getId());
		List<Long> utenzaTributoLst = this.getUtenzeTipoTributo(utenzaVO.getId());
		Utenza utenza = UtenzaConverter.toDTO(utenzaVO, utenzaDominioLst, utenzaTributoLst, this);
		AclBD aclDB = new AclBD(this);
		AclFilter filter = aclDB.newFilter();
		filter.setPrincipal(utenza.getPrincipalOriginale());

		utenza.setAclPrincipal(aclDB.findAll(filter));
		return utenza;
	}
	
	/**
	 * Recupera l'utenza identificato dalla chiave logica
	 * 
	 * @param principal
	 * @return
	 * @throws NotFoundException
	 * @throws MultipleResultException
	 * @throws ServiceException
	 */
	public Utenza getUtenzaByPrincipal(String principal) throws NotFoundException, MultipleResultException, ServiceException {
		try {
			IExpression expr = this.getUtenzaService().newExpression();
			expr.equals(it.govpay.orm.Utenza.model().PRINCIPAL, principal);
			
			it.govpay.orm.Utenza utenzaVO = this.getUtenzaService().find(expr);
			return this.getUtenza(utenzaVO);
		} catch (NotImplementedException | MultipleResultException | ExpressionNotImplementedException | ExpressionException e) {
			throw new ServiceException(e);
		}
	}

	/**
	 * Recupera l'utenza identificato dalla chiave logica
	 * 
	 * @param principal
	 * @return
	 * @throws NotFoundException
	 * @throws MultipleResultException
	 * @throws ServiceException
	 */
	public Utenza getUtenzaBySubject(String principal) throws NotFoundException, MultipleResultException, ServiceException {
		try {
			IExpression expr = this.getUtenzaService().newExpression();

			Hashtable<String, String> hashSubject = null;
			try {
			  hashSubject = Utilities.getSubjectIntoHashtable(principal);
			}catch(UtilsException e) {
				throw new NotFoundException("Utenza" + principal + "non autorizzata");
			}
			Enumeration<String> keys = hashSubject.keys();
			while(keys.hasMoreElements()){
				String key = keys.nextElement();
				String value = hashSubject.get(key);
				expr.like(it.govpay.orm.Utenza.model().PRINCIPAL, "/"+Utilities.formatKeySubject(key)+"="+Utilities.formatValueSubject(value)+"/", LikeMode.ANYWHERE);
			}
			
			it.govpay.orm.Utenza utenzaVO = this.getUtenzaService().find(expr);
			return this.getUtenza(utenzaVO);
		} catch (NotImplementedException | MultipleResultException | ExpressionNotImplementedException | ExpressionException e) {
			throw new ServiceException(e);
		}
	}
	
	/**
	 * Check esistenza utenza censita per principal
	 * 
	 * @param idUtenza
	 * @return
	 * @throws NotFoundException
	 * @throws MultipleResultException
	 * @throws ServiceException
	 */
	public boolean existsByPrincipal(String principal) throws ServiceException {
		try {
			IExpression expr = this.getUtenzaService().newExpression();
			expr.equals(it.govpay.orm.Utenza.model().PRINCIPAL, principal);
			return this.count(expr) > 0 ;
		} catch (NotImplementedException e) {
			throw new ServiceException(e);
		} catch (ExpressionNotImplementedException | ExpressionException  e) {
			throw new ServiceException(e);
		}
	}
	
	/**
	 * Check esistenza utenza censita col certificato
	 * 
	 * @param idUtenza
	 * @return
	 * @throws NotFoundException
	 * @throws MultipleResultException
	 * @throws ServiceException
	 */
	public boolean existsBySubject(String principal) throws ServiceException {
		try {
			IExpression expr = this.getUtenzaService().newExpression();
			Hashtable<String, String> hashSubject = null;
			try {
			  hashSubject = Utilities.getSubjectIntoHashtable(principal);
			}catch(UtilsException e) {
				throw new ServiceException("Servizio check Utenza non disponibile.");
			}
			Enumeration<String> keys = hashSubject.keys();
			while(keys.hasMoreElements()){
				String key = keys.nextElement();
				String value = hashSubject.get(key);
				expr.like(it.govpay.orm.Utenza.model().PRINCIPAL, "/"+Utilities.formatKeySubject(key)+"="+Utilities.formatValueSubject(value)+"/", LikeMode.ANYWHERE);
			}
			
			return this.count(expr) > 0 ;
		} catch (NotImplementedException e) {
			throw new ServiceException(e);
		} catch (ExpressionNotImplementedException | ExpressionException  e) {
			throw new ServiceException(e);
		}
	}
	

	public long count(IExpression expr) throws ServiceException {
		try {
			return this.getUtenzaService().count(expr).longValue();
		} catch (NotImplementedException e) {
			throw new ServiceException(e);
		}
	}

	/**
	 * Aggiorna il utenza
	 * 
	 * @param utenza
	 * @throws NotFoundException
	 * @throws ServiceException
	 */
	public void updateUtenza(Utenza utenza) throws NotFoundException, ServiceException {
		try {

			it.govpay.orm.Utenza vo = UtenzaConverter.toVO(utenza);
			IdUtenza idUtenza = this.getUtenzaService().convertToId(vo);
			if(!this.exists(utenza)) {
				throw new NotFoundException("Utenza con id ["+idUtenza.toJson()+"] non trovato");
			}
			Utenza utenza2 = this.getUtenza(utenza.getPrincipalOriginale());
			// il valore normalizzato del principal puo' cambiare l'ordine delle proprieta' salvate facendo saltare la tabella acl
			// se il nuovo valore normalizzato coincide con quello vecchio non cambio la chiave
			String pr, prOld;
			try {
				pr = Utilities.formatSubject(utenza.getPrincipal());
			}catch(Exception e) {
				pr= utenza.getPrincipal();
			}
			
			try {
				prOld = Utilities.formatSubject(utenza2.getPrincipal());
			}catch(Exception e) {
				prOld= utenza2.getPrincipal();
			}

			if(pr.equals(prOld)) {
				idUtenza.setPrincipal(utenza2.getPrincipal());
				vo.setPrincipal(utenza2.getPrincipal());
			}


			this.getUtenzaService().update(idUtenza, vo);
			this.updateUtenzeDominio(vo.getId(), utenza.getIdDomini());
			this.updateUtenzeTipoTributo(vo.getId(), utenza.getIdTipiTributo());
			
			AclBD aclBD = new AclBD(this);
			AclFilter filter = aclBD.newFilter();
			filter.setPrincipal(utenza.getPrincipalOriginale());
			List<Acl> alcEsistenti = aclBD.findAll(filter);
			
			// Copio la lista delle ACL nuove
			
			List<Acl> aclNuove = new ArrayList<Acl>();
			if(utenza.getAclPrincipal() != null)
				for(Acl aclNuova : utenza.getAclPrincipal())
					aclNuove.add(aclNuova);
			
			
			for(Acl aclEsistente : alcEsistenti) {
				boolean found = false;
				if(utenza.getAclPrincipal() != null)
					for(Acl aclNuova : utenza.getAclPrincipal()) {
						if(aclNuova.getServizio().equals(aclEsistente.getServizio())) {
							found = true;
							aclNuova.setId(aclEsistente.getId());
							aclBD.updateAcl(aclNuova);
							aclNuove.remove(aclNuova);
							break;
						}
					}
				
				if(!found) {
					aclBD.deleteAcl(aclEsistente);
				}
			}
			
			for(Acl aclNuova : aclNuove)
				aclBD.insertAcl(aclNuova);
			
			
			utenza.setId(vo.getId());
			this.emitAudit(utenza);
		} catch (NotImplementedException | MultipleResultException e) {
			throw new ServiceException(e);
		} catch (UtilsException e) {
			throw new ServiceException(e);
		}
	}

	private void updateUtenzeDominio(Long utenza, List<Long> idDomini) throws ServiceException {
		try {
			this.deleteUtenzeDominio(utenza);

			if(idDomini != null) {
				for(Long domini: idDomini) {
					UtenzaDominio dominio = new UtenzaDominio();
					IdDominio idDominio = new IdDominio();
					idDominio.setId(domini);
					dominio.setIdDominio(idDominio);
					IdUtenza idUtenza = new IdUtenza();
					idUtenza.setId(utenza);
					dominio.setIdUtenza(idUtenza);
					this.getUtenzaDominioService().create(dominio);
				}
			}
		} catch (NotImplementedException e) {
			throw new ServiceException(e);
		} 
	}

	private void deleteUtenzeDominio(Long utenza) throws ServiceException {
		try {
			IExpression exp = this.getUtenzaDominioService().newExpression();
			UtenzaDominioFieldConverter converter = new UtenzaDominioFieldConverter(this.getJdbcProperties().getDatabase());
			CustomField field = new CustomField("id_utenza", Long.class, "id_utenza", converter.toTable(UtenzaDominio.model()));
			exp.equals(field, utenza);
			this.getUtenzaDominioService().deleteAll(exp);
		} catch (ExpressionNotImplementedException e) {
			throw new ServiceException(e);
		} catch (NotImplementedException e) {
			throw new ServiceException(e);
		} catch(ExpressionException e) {
			throw new ServiceException(e);
		} 
	}

	private void updateUtenzeTipoTributo(Long utenza, List<Long> idTributi) throws ServiceException {
		try {
			this.deleteUtenzeTipoTributo(utenza);

			if(idTributi != null) {
				for(Long tributo: idTributi) {
					UtenzaTipoTributo utenzaTributo = new UtenzaTipoTributo();
					IdTipoTributo idTipoTributo = new IdTipoTributo();
					idTipoTributo.setId(tributo);
					utenzaTributo.setIdTipoTributo(idTipoTributo);
					IdUtenza idUtenza = new IdUtenza();
					idUtenza.setId(utenza);
					utenzaTributo.setIdUtenza(idUtenza);
					this.getUtenzaTipoTributoService().create(utenzaTributo);
				}
			}
		} catch (NotImplementedException e) {
			throw new ServiceException(e);
		} 
	}

	private void deleteUtenzeTipoTributo(Long utenza) throws ServiceException{
		try {
			IExpression exp = this.getUtenzaTipoTributoService().newExpression();
			UtenzaTipoTributoFieldConverter converter = new UtenzaTipoTributoFieldConverter(this.getJdbcProperties().getDatabase());
			CustomField field = new CustomField("id_utenza", Long.class, "id_utenza", converter.toTable(UtenzaTipoTributo.model()));
			exp.equals(field, utenza);
			this.getUtenzaTipoTributoService().deleteAll(exp);
		} catch (ExpressionNotImplementedException e) {
			throw new ServiceException(e);
		} catch (NotImplementedException e) {
			throw new ServiceException(e);
		} catch(ExpressionException e) {
			throw new ServiceException(e);
		}
	}

	private List<Long> getUtenzeTipoTributo(Long utenza) throws ServiceException {
		try {
			IPaginatedExpression exp = this.getUtenzaTipoTributoService().newPaginatedExpression();
			UtenzaTipoTributoFieldConverter converter = new UtenzaTipoTributoFieldConverter(this.getJdbcProperties().getDatabase());
			CustomField field = new CustomField("id_utenza", Long.class, "id_utenza", converter.toTable(UtenzaTipoTributo.model()));
			exp.equals(field, utenza);
			return this.getUtenzaTipoTributoService().findAll(exp).stream().map(a -> a.getIdTipoTributo().getId()).collect(Collectors.toList());
		} catch(ExpressionException e) {
			throw new ServiceException(e);
		} catch (NotImplementedException e) {
			throw new ServiceException(e);
		} catch (ExpressionNotImplementedException e) {
			throw new ServiceException(e);
		}
	}

	private List<Long> getUtenzeDominio(Long utenza) throws ServiceException {
		try {
			IPaginatedExpression exp = this.getUtenzaDominioService().newPaginatedExpression();
			UtenzaDominioFieldConverter converter = new UtenzaDominioFieldConverter(this.getJdbcProperties().getDatabase());
			CustomField field = new CustomField("id_utenza", Long.class, "id_utenza", converter.toTable(UtenzaDominio.model()));
			exp.equals(field, utenza);
			return this.getUtenzaDominioService().findAll(exp).stream().map(a -> a.getIdDominio().getId()).collect(Collectors.toList());
		} catch(ExpressionException e) {
			throw new ServiceException(e);
		} catch (NotImplementedException e) {
			throw new ServiceException(e);
		} catch (ExpressionNotImplementedException e) {
			throw new ServiceException(e);
		}
	}

	/**
	 * Crea un nuovo utenza.
	 * 
	 * @param ente
	 * @throws NotPermittedException
	 * @throws ServiceException
	 */
	public void insertUtenza(Utenza utenza) throws  ServiceException{
		try {
			it.govpay.orm.Utenza vo = UtenzaConverter.toVO(utenza);
			this.getUtenzaService().create(vo);
			utenza.setId(vo.getId());
			this.updateUtenzeDominio(utenza.getId(), utenza.getIdDomini());
			this.updateUtenzeTipoTributo(utenza.getId(), utenza.getIdTipiTributo());
			
			if(utenza.getAclPrincipal() != null && utenza.getAclPrincipal().size() > 0) {
				AclBD aclBD = new AclBD(this);
				for(Acl aclNuova : utenza.getAclPrincipal())
					aclBD.insertAcl(aclNuova);
			}
			
			this.emitAudit(utenza);
		} catch (NotImplementedException e) {
			throw new ServiceException(e);
		} 
	}

	public void deleteUtenza(Utenza utenza) throws NotFoundException, ServiceException {
		this.deleteUtenza(utenza, false);
	}
	/**
	 * Aggiorna il utenza
	 * 
	 * @param utenza
	 * @throws NotFoundException
	 * @throws ServiceException
	 */
	public void deleteUtenza(Utenza utenza, boolean commitParent) throws NotFoundException, ServiceException {
		boolean oldAutoCommit = this.isAutoCommit();
		try {
			if(!commitParent)
				this.setAutoCommit(false); 

			it.govpay.orm.Utenza vo = UtenzaConverter.toVO(utenza);

			IdUtenza idUtenza = this.getUtenzaService().convertToId(vo);
			if(!this.exists(utenza)) {
				throw new NotFoundException("Utenza con id ["+idUtenza.toJson()+"] non trovato");
			}
			this.deleteUtenzeDominio(vo.getId());
			this.deleteUtenzeTipoTributo(vo.getId());
			
			AclBD aclDB = new AclBD(this);
			AclFilter filter = aclDB.newFilter();
			filter.setPrincipal(utenza.getPrincipalOriginale());
			List<Acl> findAll = aclDB.findAll(filter);
			
			if(findAll != null) {
				AclBD aclBD = new AclBD(this);
				for (Acl aclToDelete : findAll) {
					aclBD.deleteAcl(aclToDelete);
				}
			}
			
			this.getUtenzaService().delete(vo);
			if(!commitParent)
				this.commit();

			this.emitAudit(utenza);
		} catch (NotImplementedException | UtilsException  e) {
			if(!commitParent)
				this.rollback();
			throw new ServiceException(e);
		} catch (ServiceException e) {
			if(!commitParent)
				this.rollback();
			throw e;
		} finally {
			if(!commitParent)
				this.setAutoCommit(oldAutoCommit); 
		}
	}

}
