/*
 * GovPay - Porta di Accesso al Nodo dei Pagamenti SPC 
 * http://www.gov4j.it/govpay
 * 
 * Copyright (c) 2014-2015 Link.it srl (http://www.link.it).
 * 
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
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
import it.govpay.bd.model.Applicazione;
import it.govpay.bd.model.Connettore;
import it.govpay.bd.model.converter.ApplicazioneConverter;
import it.govpay.bd.model.converter.ConnettoreConverter;
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
			it.govpay.orm.Applicazione applicazioneVO = ((JDBCApplicazioneServiceSearch)this.getServiceManager().getApplicazioneServiceSearch()).get(id);
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
			it.govpay.orm.Applicazione applicazioneVO = this.getServiceManager().getApplicazioneServiceSearch().get(id);
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

	private Applicazione getApplicazione(it.govpay.orm.Applicazione applicazioneVO) throws ServiceException,
			NotImplementedException, ExpressionNotImplementedException,
			ExpressionException {
		Applicazione applicazione = ApplicazioneConverter.toDTO(applicazioneVO);
		if(applicazioneVO.getCodConnettoreEsito()!= null) {
			IPaginatedExpression expEsito = this.getServiceManager().getConnettoreServiceSearch().newPaginatedExpression();
			expEsito.equals(it.govpay.orm.Connettore.model().COD_CONNETTORE, applicazioneVO.getCodConnettoreEsito());
			Connettore connettoreEsito = ConnettoreConverter.toDTO(this.getServiceManager().getConnettoreServiceSearch().findAll(expEsito));
			applicazione.setConnettoreEsito(connettoreEsito);
		}

		if(applicazioneVO.getCodConnettoreVerifica()!= null) {
			IPaginatedExpression expVerifica = this.getServiceManager().getConnettoreServiceSearch().newPaginatedExpression();
			expVerifica.equals(it.govpay.orm.Connettore.model().COD_CONNETTORE, applicazioneVO.getCodConnettoreVerifica());
			Connettore connettoreVerifica = ConnettoreConverter.toDTO(this.getServiceManager().getConnettoreServiceSearch().findAll(expVerifica));
			applicazione.setConnettoreVerifica(connettoreVerifica);
		}
		return applicazione;
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
			IExpression exp = this.getServiceManager().getApplicazioneServiceSearch().newExpression();
			exp.equals(it.govpay.orm.Applicazione.model().PRINCIPAL, principal);
			it.govpay.orm.Applicazione applicazioneVO = this.getServiceManager().getApplicazioneServiceSearch().find(exp);
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
			IdApplicazione id = this.getServiceManager().getApplicazioneServiceSearch().convertToId(vo);
			
			if(!this.getServiceManager().getApplicazioneServiceSearch().exists(id)) {
				throw new NotFoundException("Applicazione con id ["+id+"] non esiste.");
			}
			
			this.getServiceManager().getApplicazioneService().update(id, vo);
			applicazione.setId(vo.getId());

			if(applicazione.getConnettoreEsito() != null) {
				List<it.govpay.orm.Connettore> voConnettoreEsitoLst = ConnettoreConverter.toVOList(applicazione.getConnettoreEsito());

				IExpression expDelete = this.getServiceManager().getConnettoreServiceSearch().newExpression();
				expDelete.equals(it.govpay.orm.Connettore.model().COD_CONNETTORE, applicazione.getConnettoreEsito().getIdConnettore());
				this.getServiceManager().getConnettoreService().deleteAll(expDelete);


				for(it.govpay.orm.Connettore connettore: voConnettoreEsitoLst) {
					
					this.getServiceManager().getConnettoreService().create(connettore);
				}
			}
			
			if(applicazione.getConnettoreVerifica() != null) {
				List<it.govpay.orm.Connettore> voConnettoreVerificaLst = ConnettoreConverter.toVOList(applicazione.getConnettoreVerifica());
				
				IExpression expDelete = this.getServiceManager().getConnettoreServiceSearch().newExpression();
				expDelete.equals(it.govpay.orm.Connettore.model().COD_CONNETTORE, applicazione.getConnettoreVerifica().getIdConnettore());
				this.getServiceManager().getConnettoreService().deleteAll(expDelete);

				for(it.govpay.orm.Connettore connettore: voConnettoreVerificaLst) {
					
					this.getServiceManager().getConnettoreService().create(connettore);
				}
			}
			
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
			this.getServiceManager().getApplicazioneService().create(vo);
			applicazione.setId(vo.getId());

			if(applicazione.getConnettoreEsito() != null) {
				List<it.govpay.orm.Connettore> voConnettoreEsitoLst = ConnettoreConverter.toVOList(applicazione.getConnettoreEsito());
				
				IExpression expDelete = this.getServiceManager().getConnettoreServiceSearch().newExpression();
				expDelete.equals(it.govpay.orm.Connettore.model().COD_CONNETTORE, applicazione.getConnettoreEsito().getIdConnettore());
				this.getServiceManager().getConnettoreService().deleteAll(expDelete);

				for(it.govpay.orm.Connettore connettore: voConnettoreEsitoLst) {
					this.getServiceManager().getConnettoreService().create(connettore);
				}
			}
			
			if(applicazione.getConnettoreVerifica() != null) {
				List<it.govpay.orm.Connettore> voConnettoreVerificaLst = ConnettoreConverter.toVOList(applicazione.getConnettoreVerifica());

				IExpression expDelete = this.getServiceManager().getConnettoreServiceSearch().newExpression();
				expDelete.equals(it.govpay.orm.Connettore.model().COD_CONNETTORE, applicazione.getConnettoreVerifica().getIdConnettore());
				this.getServiceManager().getConnettoreService().deleteAll(expDelete);
				
				for(it.govpay.orm.Connettore connettore: voConnettoreVerificaLst) {
					this.getServiceManager().getConnettoreService().create(connettore);
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
		try {
			return new ApplicazioneFilter(this.getServiceManager().getApplicazioneServiceSearch());
		} catch (NotImplementedException e) {
			throw new ServiceException(e);
		}
	}

	public long count(ApplicazioneFilter filter) throws ServiceException {
		try {
			return this.getServiceManager().getApplicazioneServiceSearch().count(filter.toExpression()).longValue();
		} catch (NotImplementedException e) {
			throw new ServiceException(e);
		}
	}

	public List<Applicazione> findAll(ApplicazioneFilter filter) throws ServiceException {
		try {
			List<Applicazione> dtoList = new ArrayList<Applicazione>();
			for(it.govpay.orm.Applicazione vo: this.getServiceManager().getApplicazioneServiceSearch().findAll(filter.toPaginatedExpression())) {
				dtoList.add(getApplicazione(vo));
			}
			return dtoList;
		} catch (NotImplementedException e) {
			throw new ServiceException(e);
		} catch (ExpressionNotImplementedException e) {
			throw new ServiceException(e);
		} catch (ExpressionException e) {
			throw new ServiceException(e);
		}
	}

	
}
