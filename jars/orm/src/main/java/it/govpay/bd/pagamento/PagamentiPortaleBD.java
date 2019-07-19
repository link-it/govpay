package it.govpay.bd.pagamento;

import java.util.ArrayList;
import java.util.List;

import org.openspcoop2.generic_project.beans.CustomField;
import org.openspcoop2.generic_project.beans.UpdateField;
import org.openspcoop2.generic_project.exception.ExpressionException;
import org.openspcoop2.generic_project.exception.ExpressionNotImplementedException;
import org.openspcoop2.generic_project.exception.MultipleResultException;
import org.openspcoop2.generic_project.exception.NotFoundException;
import org.openspcoop2.generic_project.exception.NotImplementedException;
import org.openspcoop2.generic_project.exception.ServiceException;
import org.openspcoop2.generic_project.expression.IExpression;
import org.openspcoop2.generic_project.expression.IPaginatedExpression;

import it.govpay.bd.BasicBD;
import it.govpay.bd.model.PagamentoPortale;
import it.govpay.bd.model.converter.PagamentoPortaleConverter;
import it.govpay.bd.pagamento.filters.PagamentoPortaleFilter;
import it.govpay.orm.IdPagamentoPortale;
import it.govpay.orm.IdVersamento;
import it.govpay.orm.PagamentoPortaleVersamento;
import it.govpay.orm.dao.IDBPagamentoPortaleService;
import it.govpay.orm.dao.jdbc.converter.PagamentoPortaleVersamentoFieldConverter;

public class PagamentiPortaleBD extends BasicBD{


	public PagamentiPortaleBD(BasicBD basicBD) {
		super(basicBD);
	}

	public PagamentoPortaleFilter newFilter() throws ServiceException {
		return new PagamentoPortaleFilter(this.getPagamentoPortaleService());
	}
	
	public PagamentoPortaleFilter newFilter(boolean simpleSearch) throws ServiceException {
		return new PagamentoPortaleFilter(this.getPagamentoPortaleService(),simpleSearch);
	}

	public List<PagamentoPortale> findAll(PagamentoPortaleFilter filter)
			throws ServiceException {
		try {
			List<it.govpay.orm.PagamentoPortale> pagamentoVOLst = this
					.getPagamentoPortaleService().findAll(
							filter.toPaginatedExpression());
			return PagamentoPortaleConverter.toDTO(pagamentoVOLst);
		} catch (NotImplementedException e) {
			throw new ServiceException(e);
		}
	}

	public long count(PagamentoPortaleFilter filter) throws ServiceException {
		try {
			return this.getPagamentoPortaleService().count(filter.toExpression())
					.longValue();
		} catch (NotImplementedException e) {
			throw new ServiceException(e);
		}
	}

	public void insertPagamento(PagamentoPortale pagamentoPortale) throws ServiceException {
		this.insertPagamento(pagamentoPortale,false); 
	}

	public void insertPagamento(PagamentoPortale pagamentoPortale, boolean commitParent) throws ServiceException {
		boolean oldAutocomit = this.isAutoCommit();
		try {
			if(!commitParent)
				this.setAutoCommit(false);
			
			it.govpay.orm.PagamentoPortale vo = PagamentoPortaleConverter.toVO(pagamentoPortale);
			try {
				this.getPagamentoPortaleService().create(vo);
				pagamentoPortale.setId(vo.getId());

				this.insertPagPortVers(pagamentoPortale);
			} catch (NotImplementedException e) {
				throw new ServiceException();
			}
			if(!commitParent)
				this.commit();
		} catch (ServiceException e) {
			if(!commitParent)
				this.rollback();
			throw e;
		} finally {
			if(!commitParent)
				this.setAutoCommit(oldAutocomit);
		}

	}

	private void insertPagPortVers(PagamentoPortale pagamentoPortale)
			throws ServiceException, NotImplementedException {
		if(pagamentoPortale.getIdVersamento() != null) {
			for(IdVersamento idVersamento: pagamentoPortale.getIdVersamento()) {
				PagamentoPortaleVersamento pagamentoPortaleVersamento = new PagamentoPortaleVersamento();
				IdPagamentoPortale idPagamentoPortale = new IdPagamentoPortale();
				idPagamentoPortale.setId(pagamentoPortale.getId());
				pagamentoPortaleVersamento.setIdPagamentoPortale(idPagamentoPortale);
				pagamentoPortaleVersamento.setIdVersamento(idVersamento);
				this.getPagamentoPortaleVersamentoService().create(pagamentoPortaleVersamento);
			}
		}
	}

	private void deleteAllPagPortVers(PagamentoPortale pagamentoPortale)
			throws ServiceException, NotImplementedException {

		try {
			IExpression exp = this.getPagamentoPortaleVersamentoService().newExpression();
			CustomField field = new CustomField("id_pagamento_portale", Long.class, "id_pagamento_portale", new PagamentoPortaleVersamentoFieldConverter(this.getJdbcProperties().getDatabase()).toTable(it.govpay.orm.PagamentoPortaleVersamento.model()));
			exp.equals(field, pagamentoPortale.getId());
			this.getPagamentoPortaleVersamentoService().deleteAll(exp);
		} catch (ExpressionNotImplementedException e) {
			throw new ServiceException();
		} catch (ExpressionException e) {
			throw new ServiceException();
		}
	}

	private List<PagamentoPortaleVersamento> getAllPagPortVers(PagamentoPortale pagamentoPortale)
			throws ServiceException, NotImplementedException {

		try {
			IPaginatedExpression exp = this.getPagamentoPortaleVersamentoService().newPaginatedExpression();
			CustomField field = new CustomField("id_pagamento_portale", Long.class, "id_pagamento_portale", new PagamentoPortaleVersamentoFieldConverter(this.getJdbcProperties().getDatabase()).toTable(it.govpay.orm.PagamentoPortaleVersamento.model()));
			exp.equals(field, pagamentoPortale.getId());
			return this.getPagamentoPortaleVersamentoService().findAll(exp);
		} catch (ExpressionNotImplementedException e) {
			throw new ServiceException();
		} catch (ExpressionException e) {
			throw new ServiceException();
		}
	}
	
	public List<PagamentoPortaleVersamento> getAllPagPortVers(long idVersamento)
			throws ServiceException {

		try {
			IPaginatedExpression exp = this.getPagamentoPortaleVersamentoService().newPaginatedExpression();
			CustomField field = new CustomField("id_versamento", Long.class, "id_versamento", new PagamentoPortaleVersamentoFieldConverter(this.getJdbcProperties().getDatabase()).toTable(it.govpay.orm.PagamentoPortaleVersamento.model()));
			exp.equals(field, idVersamento);
			return this.getPagamentoPortaleVersamentoService().findAll(exp);
		} catch (ExpressionNotImplementedException| ExpressionException  | NotImplementedException e) {
			throw new ServiceException();
		}
	}

	public void ack(PagamentoPortale pagamento) throws ServiceException {
		it.govpay.orm.PagamentoPortale vo = PagamentoPortaleConverter.toVO(pagamento);
		try {
			UpdateField ackField = new UpdateField(it.govpay.orm.PagamentoPortale.model().ACK, true);
			this.getPagamentoPortaleService().updateFields(this.getPagamentoPortaleService().convertToId(vo), ackField);
		} catch (NotFoundException e) {
			throw new ServiceException();
		} catch (NotImplementedException e) {
			throw new ServiceException();
		}
	}

	public void updatePagamento(PagamentoPortale pagamento) throws ServiceException {
		this.updatePagamento(pagamento, false);
	}

	public void updatePagamento(PagamentoPortale pagamento, boolean updateVersamenti) throws ServiceException {
		boolean oldAutocomit = this.isAutoCommit();
		try {
			this.setAutoCommit(false);
	
			it.govpay.orm.PagamentoPortale vo = PagamentoPortaleConverter.toVO(pagamento);
			try {
				this.getPagamentoPortaleService().update(this.getPagamentoPortaleService().convertToId(vo), vo);
				if(updateVersamenti) {
					this.deleteAllPagPortVers(pagamento);
					this.insertPagPortVers(pagamento);
				}
			} catch (NotFoundException e) {
				throw new ServiceException();
			} catch (NotImplementedException e) {
				throw new ServiceException();
			}
			this.commit();
		} catch (ServiceException e) {
			this.rollback();
			throw e;
		} finally {
			this.setAutoCommit(oldAutocomit);
		}
	}
	
	public void updateStatoPagamento(long idPagamento, String statoPagamentoPortale, String descrizioneStato, Boolean ack) throws ServiceException {
		try {
			IdPagamentoPortale idVO = new IdPagamentoPortale();
			idVO.setId(idPagamento);

			List<UpdateField> lstUpdateFields = new ArrayList<>();
			
			if(ack != null)
				lstUpdateFields.add(new UpdateField(it.govpay.orm.PagamentoPortale.model().ACK, ack));
			
			if(statoPagamentoPortale != null)
				lstUpdateFields.add(new UpdateField(it.govpay.orm.PagamentoPortale.model().STATO, statoPagamentoPortale.toString()));
			if(descrizioneStato != null) {
				if(descrizioneStato.length() > 1024)
					descrizioneStato = descrizioneStato.substring(0, 1021)+ "...";
				
				lstUpdateFields.add(new UpdateField(it.govpay.orm.PagamentoPortale.model().DESCRIZIONE_STATO, descrizioneStato));
			}

			this.getPagamentoPortaleService().updateFields(idVO, lstUpdateFields.toArray(new UpdateField[]{}));
		} catch (NotImplementedException e) {
			throw new ServiceException(e);
		} catch (NotFoundException e) {
			throw new ServiceException(e);
		}
	}

	/**
	 * Recupera il pagamento identificato dalla chiave fisica
	 */
	public PagamentoPortale getPagamento(long id) throws ServiceException,NotFoundException {
		try {
			return PagamentoPortaleConverter.toDTO(((IDBPagamentoPortaleService)this.getPagamentoPortaleService()).get(id));
		} catch (MultipleResultException e) {
			throw new ServiceException();
		} catch (NotImplementedException e) {
			throw new ServiceException();
		}
	}

	/**
	 * Recupera il pagamento identificato dal codSessione
	 */
	public PagamentoPortale getPagamentoFromCodSessione(String codSessione) throws ServiceException,NotFoundException {
		try {
			IdPagamentoPortale id = new IdPagamentoPortale();
			id.setIdSessione(codSessione);
			PagamentoPortale dto = PagamentoPortaleConverter.toDTO(this.getPagamentoPortaleService().get(id));

			return this.getPagamentoArricchito(dto);
		} catch (MultipleResultException e) {
			throw new ServiceException();
		} catch (NotImplementedException e) {
			throw new ServiceException();
		}
	}

	private PagamentoPortale getPagamentoArricchito(PagamentoPortale dto) throws ServiceException, NotImplementedException {
		List<PagamentoPortaleVersamento> allPagPortVers = this.getAllPagPortVers(dto);
		List<IdVersamento> idVersamento = new ArrayList<>();
		for(PagamentoPortaleVersamento vers: allPagPortVers) {
			idVersamento.add(vers.getIdVersamento());
		}
		dto.setIdVersamento(idVersamento);
		return dto;
	}

	/**
	 * Recupera il pagamento identificato dal codsessionepsp
	 */
	public PagamentoPortale getPagamentoFromCodSessionePsp(String codSessionePsp) throws ServiceException,NotFoundException {
		try {
			IExpression exp = this.getPagamentoPortaleService().newExpression();
			exp.equals(it.govpay.orm.PagamentoPortale.model().ID_SESSIONE_PSP, codSessionePsp);
			PagamentoPortale dto = PagamentoPortaleConverter.toDTO(this.getPagamentoPortaleService().find(exp));
			return this.getPagamentoArricchito(dto);
		} catch (MultipleResultException e) {
			throw new ServiceException();
		} catch (NotImplementedException e) {
			throw new ServiceException();
		} catch (ExpressionNotImplementedException e) {
			throw new ServiceException();
		} catch (ExpressionException e) {
			throw new ServiceException();
		}
	}
}
