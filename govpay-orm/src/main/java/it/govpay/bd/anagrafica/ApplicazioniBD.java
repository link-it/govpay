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

import it.govpay.bd.BasicBD;
import it.govpay.bd.anagrafica.filters.ApplicazioneFilter;
import it.govpay.bd.model.converter.AclConverter;
import it.govpay.bd.model.converter.ApplicazioneConverter;
import it.govpay.bd.model.converter.ConnettoreConverter;
import it.govpay.model.Acl;
import it.govpay.model.Applicazione;
import it.govpay.model.Connettore;
import it.govpay.orm.ACL;
import it.govpay.orm.IdApplicazione;
import it.govpay.orm.dao.jdbc.JDBCApplicazioneServiceSearch;

import java.util.ArrayList;
import java.util.List;

import org.openspcoop2.generic_project.exception.ExpressionException;
import org.openspcoop2.generic_project.exception.ExpressionNotImplementedException;
import org.openspcoop2.generic_project.exception.MultipleResultException;
import org.openspcoop2.generic_project.exception.NotFoundException;
import org.openspcoop2.generic_project.exception.NotImplementedException;
import org.openspcoop2.generic_project.exception.ServiceException;
import org.openspcoop2.generic_project.expression.IExpression;
import org.openspcoop2.generic_project.expression.IPaginatedExpression;

public class ApplicazioniBD extends BasicBD {

	public ApplicazioniBD(BasicBD basicBD) {
		super(basicBD);
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
	public Applicazione getApplicazione(Long idApplicazione) throws NotFoundException, ServiceException, MultipleResultException {

		if(idApplicazione == null) {
			throw new ServiceException("Parameter 'id' cannot be NULL");
		}

		long id = idApplicazione.longValue();

		try {
			it.govpay.orm.Applicazione applicazioneVO = ((JDBCApplicazioneServiceSearch)this.getApplicazioneService()).get(id);
			Applicazione applicazione = getApplicazione(applicazioneVO);

			return applicazione;
		} catch (NotImplementedException e) {
			throw new ServiceException(e);
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
	public Applicazione getApplicazione(String codApplicazione) throws NotFoundException, MultipleResultException, ServiceException {
		try {
			IdApplicazione id = new IdApplicazione();
			id.setCodApplicazione(codApplicazione);
			it.govpay.orm.Applicazione applicazioneVO = this.getApplicazioneService().get(id);
			Applicazione applicazione = getApplicazione(applicazioneVO);
			return applicazione;
		} catch (NotImplementedException e) {
			throw new ServiceException(e);
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
			IExpression exp = this.getApplicazioneService().newExpression();
			exp.equals(it.govpay.orm.Applicazione.model().PRINCIPAL, principal);
			it.govpay.orm.Applicazione applicazioneVO = this.getApplicazioneService().find(exp);
			Applicazione applicazione = getApplicazione(applicazioneVO);

			return applicazione;
		} catch (NotImplementedException e) {
			throw new ServiceException(e);
		} catch (ExpressionNotImplementedException e) {
			throw new ServiceException(e);
		} catch (ExpressionException e) {
			throw new ServiceException(e);
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
			it.govpay.orm.Applicazione vo = ApplicazioneConverter.toVO(applicazione);
			IdApplicazione id = this.getApplicazioneService().convertToId(vo);

			if(!this.getApplicazioneService().exists(id)) {
				throw new NotFoundException("Applicazione con id ["+id+"] non esiste.");
			}

			this.getApplicazioneService().update(id, vo);
			applicazione.setId(vo.getId());

			if(applicazione.getConnettoreNotifica() != null) {
				List<it.govpay.orm.Connettore> voConnettoreEsitoLst = ConnettoreConverter.toVOList(applicazione.getConnettoreNotifica());

				IExpression expDelete = this.getConnettoreService().newExpression();
				expDelete.equals(it.govpay.orm.Connettore.model().COD_CONNETTORE, applicazione.getConnettoreNotifica().getIdConnettore());
				this.getConnettoreService().deleteAll(expDelete);


				for(it.govpay.orm.Connettore connettore: voConnettoreEsitoLst) {

					this.getConnettoreService().create(connettore);
				}
			}

			if(applicazione.getConnettoreVerifica() != null) {
				List<it.govpay.orm.Connettore> voConnettoreVerificaLst = ConnettoreConverter.toVOList(applicazione.getConnettoreVerifica());

				IExpression expDelete = this.getConnettoreService().newExpression();
				expDelete.equals(it.govpay.orm.Connettore.model().COD_CONNETTORE, applicazione.getConnettoreVerifica().getIdConnettore());
				this.getConnettoreService().deleteAll(expDelete);

				for(it.govpay.orm.Connettore connettore: voConnettoreVerificaLst) {

					this.getConnettoreService().create(connettore);
				}
			}
			
			AclBD aclBD = new AclBD(this);
			aclBD.deleteAclApplicazione(applicazione.getId());
			
			if(applicazione.getAcls() != null && !applicazione.getAcls().isEmpty()) {
				 
				for(Acl acl: applicazione.getAcls()) {
					try{
						ACL aclVo = AclConverter.toVO(acl, this);
						IdApplicazione idApplicazione = new IdApplicazione();
						idApplicazione.setId(applicazione.getId());
						aclVo.setIdApplicazione(idApplicazione);
						this.getAclService().create(aclVo);
					} catch(NotFoundException e) {
						throw new ServiceException(e);
					}
				}
			}

			AnagraficaManager.removeFromCache(applicazione);

		} catch (NotImplementedException e) {
			throw new ServiceException(e);
		} catch (MultipleResultException e) {
			throw new ServiceException(e);
		} catch (ExpressionNotImplementedException e) {
			throw new ServiceException(e);
		} catch (ExpressionException e) {
			throw new ServiceException(e);
		}

	}

	/**
	 * Crea una nuova applicazione con i dati forniti
	 * @param applicazione
	 * @throws ServiceException in caso di errore DB.
	 */
	public void insertApplicazione(Applicazione applicazione) throws ServiceException{
		try {
			it.govpay.orm.Applicazione vo = ApplicazioneConverter.toVO(applicazione);
			this.getApplicazioneService().create(vo);
			applicazione.setId(vo.getId());

			if(applicazione.getConnettoreNotifica() != null) {
				List<it.govpay.orm.Connettore> voConnettoreEsitoLst = ConnettoreConverter.toVOList(applicazione.getConnettoreNotifica());

				IExpression expDelete = this.getConnettoreService().newExpression();
				expDelete.equals(it.govpay.orm.Connettore.model().COD_CONNETTORE, applicazione.getConnettoreNotifica().getIdConnettore());
				this.getConnettoreService().deleteAll(expDelete);

				for(it.govpay.orm.Connettore connettore: voConnettoreEsitoLst) {
					this.getConnettoreService().create(connettore);
				}
			}

			if(applicazione.getConnettoreVerifica() != null) {
				List<it.govpay.orm.Connettore> voConnettoreVerificaLst = ConnettoreConverter.toVOList(applicazione.getConnettoreVerifica());

				IExpression expDelete = this.getConnettoreService().newExpression();
				expDelete.equals(it.govpay.orm.Connettore.model().COD_CONNETTORE, applicazione.getConnettoreVerifica().getIdConnettore());
				this.getConnettoreService().deleteAll(expDelete);

				for(it.govpay.orm.Connettore connettore: voConnettoreVerificaLst) {
					this.getConnettoreService().create(connettore);
				}
			}
			
			if(applicazione.getAcls() != null && !applicazione.getAcls().isEmpty()) {
				for(Acl acl: applicazione.getAcls()) {
					try{
						ACL aclVo = AclConverter.toVO(acl, this);
						IdApplicazione idApplicazione = new IdApplicazione();
						idApplicazione.setId(applicazione.getId());
						aclVo.setIdApplicazione(idApplicazione);
						this.getAclService().create(aclVo);
					} catch(NotFoundException e) {
						throw new ServiceException(e);
					}
				}
			}

		} catch (NotImplementedException e) {
			throw new ServiceException(e);
		} catch (ExpressionNotImplementedException e) {
			throw new ServiceException(e);
		} catch (ExpressionException e) {
			throw new ServiceException(e);
		}
	}



	public ApplicazioneFilter newFilter() throws ServiceException {
		return new ApplicazioneFilter(this.getApplicazioneService());
	}

	public long count(ApplicazioneFilter filter) throws ServiceException {
		try {
			return this.getApplicazioneService().count(filter.toExpression()).longValue();
		} catch (NotImplementedException e) {
			throw new ServiceException(e);
		}
	}

	public List<Applicazione> findAll(ApplicazioneFilter filter) throws ServiceException {
		try {

			List<Applicazione> dtoList = new ArrayList<Applicazione>();
			for(it.govpay.orm.Applicazione vo: this.getApplicazioneService().findAll(filter.toPaginatedExpression())) {
				dtoList.add(getApplicazione(vo));
			}
			return dtoList;
		} catch (NotImplementedException e) {
			throw new ServiceException(e);
		}
	}

	private Applicazione getApplicazione(it.govpay.orm.Applicazione applicazioneVO) throws ServiceException {
		try {
			Connettore connettoreNotifica = null;
			if(applicazioneVO.getCodConnettoreEsito()!= null) {
				IPaginatedExpression expEsito = this.getConnettoreService().newPaginatedExpression();
				expEsito.equals(it.govpay.orm.Connettore.model().COD_CONNETTORE, applicazioneVO.getCodConnettoreEsito());
				connettoreNotifica = ConnettoreConverter.toDTO(this.getConnettoreService().findAll(expEsito));
			}
	
			Connettore connettoreVerifica = null;
			if(applicazioneVO.getCodConnettoreVerifica()!= null) {
				IPaginatedExpression expVerifica = this.getConnettoreService().newPaginatedExpression();
				expVerifica.equals(it.govpay.orm.Connettore.model().COD_CONNETTORE, applicazioneVO.getCodConnettoreVerifica());
				connettoreVerifica = ConnettoreConverter.toDTO(this.getConnettoreService().findAll(expVerifica));
			}
			
			AclBD aclBD = new AclBD(this);
			try{
				List<Acl> acls = aclBD.getAclApplicazione(applicazioneVO.getId());
				
				Applicazione applicazione = ApplicazioneConverter.toDTO(applicazioneVO, connettoreNotifica, connettoreVerifica, acls);
				return applicazione;
			} catch(NotFoundException e) {
				throw new ServiceException(e);
			}
		} catch (ExpressionNotImplementedException e) {
			throw new ServiceException(e);
		} catch (ExpressionException e) {
			throw new ServiceException(e);
		} catch (NotImplementedException e) {
			throw new ServiceException(e);
		}
	}
}
