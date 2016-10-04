/*
 * GovPay - Porta di Accesso al Nodo dei Pagamenti SPC 
 * http://www.gov4j.it/govpay
 * 
 * Copyright (c) 2014-2016 Link.it srl (http://www.link.it).
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
package it.govpay.bd.pagamento;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.openspcoop2.generic_project.beans.CustomField;
import org.openspcoop2.generic_project.exception.ExpressionException;
import org.openspcoop2.generic_project.exception.ExpressionNotImplementedException;
import org.openspcoop2.generic_project.exception.MultipleResultException;
import org.openspcoop2.generic_project.exception.NotFoundException;
import org.openspcoop2.generic_project.exception.NotImplementedException;
import org.openspcoop2.generic_project.exception.ServiceException;
import org.openspcoop2.generic_project.expression.IExpression;
import org.openspcoop2.generic_project.expression.IPaginatedExpression;

import it.govpay.bd.BasicBD;
import it.govpay.bd.IFilter;
import it.govpay.bd.model.converter.FrApplicazioneConverter;
import it.govpay.bd.model.converter.FrConverter;
import it.govpay.bd.model.converter.RendicontazioneSenzaRptConverter;
import it.govpay.bd.pagamento.filters.FrApplicazioneFilter;
import it.govpay.bd.pagamento.filters.FrFilter;
import it.govpay.model.Fr;
import it.govpay.model.FrApplicazione;
import it.govpay.model.RendicontazioneSenzaRpt;
import it.govpay.orm.FR;
import it.govpay.orm.FrFiltroApp;
import it.govpay.orm.IdFr;
import it.govpay.orm.dao.jdbc.JDBCFRServiceSearch;
import it.govpay.orm.dao.jdbc.JDBCFrApplicazioneServiceSearch;
import it.govpay.orm.dao.jdbc.converter.FrApplicazioneFieldConverter;
import it.govpay.orm.dao.jdbc.converter.FrFiltroAppFieldConverter;

public class FrBD extends BasicBD {

	public FrBD(BasicBD basicBD) {
		super(basicBD);
	}

	/**
	 * Recupera l'Fr identificato dalla chiave fisica
	 * 
	 * @param idFr
	 * @return
	 * @throws NotFoundException
	 * @throws MultipleResultException
	 * @throws ServiceException
	 */
	public Fr getFr(long idFr) throws ServiceException {
		try {
			FR vo = ((JDBCFRServiceSearch)this.getFrService()).get(idFr);
			return FrConverter.toDTO(vo);
		} catch (NotImplementedException e) {
			throw new ServiceException(e);
		} catch (NotFoundException e) {
			throw new ServiceException(e);
		} catch (MultipleResultException e) {
			throw new ServiceException(e);
		} 
	}

	/**
	 * Recupera l'Fr identificato dalla chiave logica
	 * 
	 * @param idFr
	 * @return
	 * @throws NotFoundException
	 * @throws MultipleResultException
	 * @throws ServiceException
	 */
	public Fr getFr(int annoRiferimento, String codFlusso) throws NotFoundException, ServiceException {
		try {
			IdFr id = new IdFr();
			id.setAnnoRiferimento(annoRiferimento);
			id.setCodFlusso(codFlusso);
			FR vo = this.getFrService().get(id);
			return FrConverter.toDTO(vo);
		} catch (NotImplementedException e) {
			throw new ServiceException(e);
		} catch (MultipleResultException e) {
			throw new ServiceException(e);
		} 
	}

	public boolean exists(int annoRiferimento, String codFlusso) throws ServiceException {
		try {
			IdFr id = new IdFr();
			id.setAnnoRiferimento(annoRiferimento);
			id.setCodFlusso(codFlusso);
			return this.getFrService().exists(id);
		} catch (NotImplementedException e) {
			throw new ServiceException(e);
		} catch (MultipleResultException e) {
			throw new ServiceException(e);
		}
	}

	/**
	 * Inserisce un nuovo fr
	 * 
	 * @param codMsgRicevuta
	 * @return
	 * @throws NotFoundException
	 * @throws MultipleResultException
	 * @throws ServiceException
	 */
	public void insertFr(Fr fr) throws ServiceException, NotFoundException {
		try {
			it.govpay.orm.FR vo = FrConverter.toVO(fr);
			this.getFrService().create(vo);
			fr.setId(vo.getId());
		} catch (NotImplementedException e) {
			throw new ServiceException(e);
		}
	}

	public FrFilter newFilter() throws ServiceException {
		return new FrFilter(this.getFrService());
	}

	public long count(IFilter filter) throws ServiceException {
		try {
			return this.getFrService().count(filter.toExpression()).longValue();
		} catch (NotImplementedException e) {
			throw new ServiceException(e);
		}
	}

	public List<Fr> findAll(IFilter filter) throws ServiceException {
		try {
			List<Fr> frLst = new ArrayList<Fr>();
			List<it.govpay.orm.FR> frVOLst = this.getFrService().findAll(filter.toPaginatedExpression()); 
			for(it.govpay.orm.FR frVO: frVOLst) {
				frLst.add(FrConverter.toDTO(frVO));
			}
			return frLst;
		} catch (NotImplementedException e) {
			throw new ServiceException(e);
		}
	}
	
	public List<Fr> findAll(Long idDominio, Long idApplicazione, Date da, Date a) throws ServiceException {
		try {
			List<Fr> frLst = new ArrayList<Fr>();
			
			FrFiltroAppFieldConverter conv = new FrFiltroAppFieldConverter(this.getJdbcProperties().getDatabase()); 
			IPaginatedExpression exp = this.getFrFiltroAppService().newPaginatedExpression();
			
			if(da != null)
				exp.greaterEquals(FrFiltroApp.model().FR.DATA_ACQUISIZIONE, da);
			if(a != null)
				exp.lessEquals(FrFiltroApp.model().FR.DATA_ACQUISIZIONE, a);
			if(idDominio != null)
				exp.equals(new CustomField("id_dominio", Long.class, "id_dominio", conv.toTable(FrFiltroApp.model().FR)), idDominio);
			if(idApplicazione != null)
				exp.equals(new CustomField("id_applicazione", Long.class, "id_applicazione", conv.toTable(FrFiltroApp.model().FR_APPLICAZIONE)), idApplicazione);
			
			List<it.govpay.orm.FrFiltroApp> frVOLst = this.getFrFiltroAppService().findAll(exp);
			for(it.govpay.orm.FrFiltroApp frVO: frVOLst) {
				frLst.add(FrConverter.toDTO(frVO.getFr()));
			}
			return frLst;
		} catch (NotImplementedException e) {
			throw new ServiceException(e);
		} catch (ExpressionNotImplementedException e) {
			throw new ServiceException(e);
		} catch (ExpressionException e) {
			throw new ServiceException(e);
		}
	}




	public void insertFrApplicazione(FrApplicazione frApplicazione) throws ServiceException {
		try {
			it.govpay.orm.FrApplicazione vo = FrApplicazioneConverter.toVO(frApplicazione);
			this.getFrApplicazioneService().create(vo);
			frApplicazione.setId(vo.getId());
		} catch (NotImplementedException e) {
			throw new ServiceException(e);
		}
	}

	public List<FrApplicazione> getFrApplicazioni(Long idApplicazione) throws ServiceException {
		try {
			IPaginatedExpression exp = this.getFrApplicazioneService().newPaginatedExpression();
			FrApplicazioneFieldConverter conv = new FrApplicazioneFieldConverter(this.getJdbcProperties().getDatabase());
			CustomField customField = new CustomField("id_applicazione", Long.class, "id_applicazione", conv.toTable(it.govpay.orm.FrApplicazione.model()));
			exp.equals(customField, idApplicazione);
			List<it.govpay.orm.FrApplicazione> frApplicazioniVOlst = this.getFrApplicazioneService().findAll(exp);
			return FrApplicazioneConverter.toDTOList(frApplicazioniVOlst);
		} catch (NotImplementedException e) {
			throw new ServiceException(e);
		} catch (ExpressionNotImplementedException e) {
			throw new ServiceException(e);
		} catch (ExpressionException e) {
			throw new ServiceException(e);
		}
	}
	
	public List<Long> getIdFlussi(Long idApplicazione) throws ServiceException {
		try {
			IPaginatedExpression exp = this.getFrApplicazioneService().newPaginatedExpression();
			FrApplicazioneFieldConverter conv = new FrApplicazioneFieldConverter(this.getJdbcProperties().getDatabase());
			CustomField customField = new CustomField("id_applicazione", Long.class, "id_applicazione", conv.toTable(it.govpay.orm.FrApplicazione.model()));
			exp.equals(customField, idApplicazione);
			List<it.govpay.orm.FrApplicazione> frApplicazioniVOlst = this.getFrApplicazioneService().findAll(exp);
			List<Long> idFlussi = new ArrayList<Long>();
			
			for (it.govpay.orm.FrApplicazione frApp : frApplicazioniVOlst) {
				idFlussi.add(frApp.getId());
			}
			return idFlussi;
		} catch (NotImplementedException e) {
			throw new ServiceException(e);
		} catch (ExpressionNotImplementedException e) {
			throw new ServiceException(e);
		} catch (ExpressionException e) {
			throw new ServiceException(e);
		}
	}

	public FrApplicazione getFrApplicazione(Long idApplicazione, int anno, String codFlusso) throws ServiceException, NotFoundException {
		try {
			IExpression exp = this.getFrApplicazioneService().newExpression();
			FrApplicazioneFieldConverter conv = new FrApplicazioneFieldConverter(this.getJdbcProperties().getDatabase());
			CustomField customField = new CustomField("id_applicazione", Long.class, "id_applicazione", conv.toTable(it.govpay.orm.FrApplicazione.model()));
			exp.equals(customField, idApplicazione);
			exp.equals(it.govpay.orm.FrApplicazione.model().ID_FR.ANNO_RIFERIMENTO, anno);
			exp.equals(it.govpay.orm.FrApplicazione.model().ID_FR.COD_FLUSSO, codFlusso);
			return FrApplicazioneConverter.toDTO(this.getFrApplicazioneService().find(exp));
		} catch (NotImplementedException e) {
			throw new ServiceException(e);
		} catch (ExpressionNotImplementedException e) {
			throw new ServiceException(e);
		} catch (ExpressionException e) {
			throw new ServiceException(e);
		} catch (MultipleResultException e) {
			throw new ServiceException(e);
		}
	}
	
	/**
	 * Recupera l'Fr identificato dalla chiave fisica
	 * 
	 * @param idFr
	 * @return
	 * @throws NotFoundException
	 * @throws MultipleResultException
	 * @throws ServiceException
	 */
	public FrApplicazione getFrApplicazione(long idFrApplicazione) throws ServiceException {
		try {
			return FrApplicazioneConverter.toDTO(((JDBCFrApplicazioneServiceSearch)this.getFrApplicazioneService()).get(idFrApplicazione));
		} catch (NotImplementedException e) {
			throw new ServiceException(e);
		} catch (NotFoundException e) {
			throw new ServiceException(e);
		} catch (MultipleResultException e) {
			throw new ServiceException(e);
		} 
	}
	
	public void insertRendicontazioneSenzaRpt(RendicontazioneSenzaRpt rendicontazione) throws ServiceException {
		try {
			it.govpay.orm.RendicontazioneSenzaRPT vo = RendicontazioneSenzaRptConverter.toVO(rendicontazione);
			this.getRendicontazioneSenzaRPTService().create(vo);
			rendicontazione.setId(vo.getId());
		} catch (NotImplementedException e) {
			throw new ServiceException(e);
		}
	}
	
	public FrApplicazioneFilter newFrApplicazioneFilter() throws ServiceException {
		return new FrApplicazioneFilter(this.getFrApplicazioneService());
	}

	public long countFrApplicazione(FrApplicazioneFilter filter) throws ServiceException {
		try {
			return this.getFrApplicazioneService().count(filter.toExpression()).longValue();
		} catch (NotImplementedException e) {
			throw new ServiceException(e);
		}
	}

	public List<FrApplicazione> findAllFrApplicazione(FrApplicazioneFilter filter) throws ServiceException {
		try {
			List<FrApplicazione> frLst = new ArrayList<FrApplicazione>();
			List<it.govpay.orm.FrApplicazione> frVOLst = this.getFrApplicazioneService().findAll(filter.toPaginatedExpression()); 
			for(it.govpay.orm.FrApplicazione frVO: frVOLst) {
				frLst.add(FrApplicazioneConverter.toDTO(frVO));
			}
			return frLst;
		} catch (NotImplementedException e) {
			throw new ServiceException(e);
		}
	}

}
