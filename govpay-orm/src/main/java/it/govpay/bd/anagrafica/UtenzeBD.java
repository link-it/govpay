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
import org.openspcoop2.utils.UtilsException;

import it.govpay.bd.BasicBD;
import it.govpay.bd.anagrafica.filters.AclFilter;
import it.govpay.bd.model.Utenza;
import it.govpay.bd.model.converter.UtenzaConverter;
import it.govpay.orm.IdDominio;
import it.govpay.orm.IdTributo;
import it.govpay.orm.IdUtenza;
import it.govpay.orm.UtenzaDominio;
import it.govpay.orm.UtenzaTributo;
import it.govpay.orm.dao.jdbc.JDBCUtenzaServiceSearch;
import it.govpay.orm.dao.jdbc.converter.UtenzaDominioFieldConverter;
import it.govpay.orm.dao.jdbc.converter.UtenzaTributoFieldConverter;

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
			return getUtenza(utenzaVO);
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
			return this.getUtenzaService().exists(this.getUtenzaService().convertToId(UtenzaConverter.toVO(utenza)));
		} catch (NotImplementedException e) {
			throw new ServiceException(e);
		} catch (MultipleResultException e) {
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
		return getUtenza(principal, false);
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
				expr.ilike(it.govpay.orm.Utenza.model().PRINCIPAL, principal, LikeMode.EXACT);
			else 
				expr.equals(it.govpay.orm.Utenza.model().PRINCIPAL, principal);
			
			it.govpay.orm.Utenza utenzaVO = this.getUtenzaService().find(expr);
			return getUtenza(utenzaVO);
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
		List<Long> utenzaTributoLst = this.getUtenzeTributo(utenzaVO.getId());
		Utenza utenza = UtenzaConverter.toDTO(utenzaVO, utenzaDominioLst, utenzaTributoLst, this);
		AclBD aclDB = new AclBD(this);
		AclFilter filter = aclDB.newFilter();
		filter.setPrincipal(utenza.getPrincipal());
		
		utenza.setAclPrincipal(aclDB.findAll(filter));
		return utenza;
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
			if(!this.getUtenzaService().exists(idUtenza)) {
				throw new NotFoundException("Utenza con id ["+idUtenza.toJson()+"] non trovato");
			}
			this.getUtenzaService().update(idUtenza, vo);
			this.updateUtenzeDominio(vo.getId(), utenza.getIdDomini());
			this.updateUtenzeTributo(vo.getId(), utenza.getIdTributi());
			utenza.setId(vo.getId());
			emitAudit(utenza);
		} catch (NotImplementedException e) {
			throw new ServiceException(e);
		} catch (MultipleResultException e) {
			throw new ServiceException(e);
		} catch (UtilsException e) {
			throw new ServiceException(e);
		}
	}

	private void updateUtenzeDominio(Long utenza, List<Long> idDomini) throws ServiceException {
		try {
			deleteUtenzeDominio(utenza);

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

	private void updateUtenzeTributo(Long utenza, List<Long> idTributi) throws ServiceException {
		try {
			deleteUtenzeTributo(utenza);
			
			if(idTributi != null) {
				for(Long tributo: idTributi) {
					UtenzaTributo utenzaTributo = new UtenzaTributo();
					IdTributo idDominio = new IdTributo();
					idDominio.setId(tributo);
					utenzaTributo.setIdTributo(idDominio);
					IdUtenza idUtenza = new IdUtenza();
					idUtenza.setId(utenza);
					utenzaTributo.setIdUtenza(idUtenza);
					this.getUtenzaTributoService().create(utenzaTributo);
				}
			}
		} catch (NotImplementedException e) {
			throw new ServiceException(e);
		} 
	}

	private void deleteUtenzeTributo(Long utenza) throws ServiceException{
		try {
			IExpression exp = this.getUtenzaTributoService().newExpression();
			UtenzaTributoFieldConverter converter = new UtenzaTributoFieldConverter(this.getJdbcProperties().getDatabase());
			CustomField field = new CustomField("id_utenza", Long.class, "id_utenza", converter.toTable(UtenzaTributo.model()));
			exp.equals(field, utenza);
			this.getUtenzaTributoService().deleteAll(exp);
		} catch (ExpressionNotImplementedException e) {
			throw new ServiceException(e);
		} catch (NotImplementedException e) {
			throw new ServiceException(e);
		} catch(ExpressionException e) {
			throw new ServiceException(e);
		}
	}

	private List<Long> getUtenzeTributo(Long utenza) throws ServiceException {
		try {
			IPaginatedExpression exp = this.getUtenzaTributoService().newPaginatedExpression();
			UtenzaTributoFieldConverter converter = new UtenzaTributoFieldConverter(this.getJdbcProperties().getDatabase());
			CustomField field = new CustomField("id_utenza", Long.class, "id_utenza", converter.toTable(UtenzaTributo.model()));
			exp.equals(field, utenza);
			return this.getUtenzaTributoService().findAll(exp).stream().map(a -> a.getIdTributo().getId()).collect(Collectors.toList());
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
			this.updateUtenzeTributo(utenza.getId(), utenza.getIdTributi());
			emitAudit(utenza);
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
			if(!this.getUtenzaService().exists(idUtenza)) {
				throw new NotFoundException("Utenza con id ["+idUtenza.toJson()+"] non trovato");
			}
			this.deleteUtenzeDominio(vo.getId());
			this.deleteUtenzeTributo(vo.getId());
			this.getUtenzaService().delete(vo);
			if(!commitParent)
				this.commit();
			
			emitAudit(utenza);
		} catch (NotImplementedException | MultipleResultException | UtilsException  e) {
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
