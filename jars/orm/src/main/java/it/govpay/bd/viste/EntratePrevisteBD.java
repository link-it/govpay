package it.govpay.bd.viste;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;

import org.openspcoop2.generic_project.beans.NonNegativeNumber;
import org.openspcoop2.generic_project.exception.ExpressionException;
import org.openspcoop2.generic_project.exception.ExpressionNotImplementedException;
import org.openspcoop2.generic_project.exception.NotImplementedException;
import org.openspcoop2.generic_project.exception.ServiceException;
import org.openspcoop2.generic_project.expression.IExpression;
import org.openspcoop2.generic_project.expression.IPaginatedExpression;
import org.openspcoop2.generic_project.expression.SortOrder;

import it.govpay.bd.BDConfigWrapper;
import it.govpay.bd.BasicBD;
import it.govpay.bd.viste.filters.EntrataPrevistaFilter;
import it.govpay.bd.viste.model.EntrataPrevista;
import it.govpay.bd.viste.model.converter.EntrataPrevistaConverter;
import it.govpay.orm.model.VistaRiscossioniModel;

public class EntratePrevisteBD extends BasicBD {

	public EntratePrevisteBD(BasicBD basicBD) {
		super(basicBD);
	}
	
	public EntratePrevisteBD(String idTransaction) {
		super(idTransaction);
	}
	
	public EntratePrevisteBD(String idTransaction, boolean useCache) {
		super(idTransaction, useCache);
	}
	
	public EntratePrevisteBD(BDConfigWrapper configWrapper) {
		super(configWrapper.getTransactionID(), configWrapper.isUseCache());
	}
	
	public EntrataPrevistaFilter newFilter() throws ServiceException {
		return new EntrataPrevistaFilter(this.getVistaRiscossioniServiceSearch());
	}

	public EntrataPrevistaFilter newFilter(boolean simpleSearch) throws ServiceException {
		return new EntrataPrevistaFilter(this.getVistaRiscossioniServiceSearch(),simpleSearch);
	}

	public long count(EntrataPrevistaFilter filter) throws ServiceException {
		try {
			if(this.isAtomica()) {
				this.setupConnection(this.getIdTransaction());
				filter.setExpressionConstructor(this.getVistaRiscossioniServiceSearch());
			}
			
			return this.getVistaRiscossioniServiceSearch().count(filter.toExpression()).longValue();
		} catch (NotImplementedException e) {
			throw new ServiceException(e);
		} finally {
			if(this.isAtomica()) {
				this.closeConnection();
			}
		}
	}
	
	public List<EntrataPrevista> findAll(EntrataPrevistaFilter filter) throws ServiceException {
		try {
			if(this.isAtomica()) {
				this.setupConnection(this.getIdTransaction());
				filter.setExpressionConstructor(this.getVistaRiscossioniServiceSearch());
			}
			
			List<EntrataPrevista> entratePrevisteLst = new ArrayList<>();

			IPaginatedExpression paginatedExpression = filter.toPaginatedExpression();
			List<it.govpay.orm.VistaRiscossioni> riscossioniVOLst = this.getVistaRiscossioniServiceSearch().findAll(paginatedExpression); 
			for(it.govpay.orm.VistaRiscossioni riscossioneVO: riscossioniVOLst) {
				entratePrevisteLst.add(EntrataPrevistaConverter.toDTO(riscossioneVO));
			}
			return entratePrevisteLst;
		} catch (NotImplementedException e) {
			throw new ServiceException(e);
		} finally {
			if(this.isAtomica()) {
				this.closeConnection();
			}
		}
	}
	
	public List<EntrataPrevista> ricercaRiscossioniDominio(String codDominio, Date dataRtDa, Date dataRtA, List<String> listaTipiPendenza, Integer offset, Integer limit) throws ServiceException{
		try {
			if(this.isAtomica()) {
				this.setupConnection(this.getIdTransaction());
			}
			
			VistaRiscossioniModel model = it.govpay.orm.VistaRiscossioni.model();
			IExpression exp = this.getVistaRiscossioniServiceSearch().newExpression();
			exp.equals(model.COD_DOMINIO, codDominio).and();
			if(dataRtDa != null) {
				exp.greaterEquals(model.DATA_PAGAMENTO, dataRtDa);
			}
			exp.lessEquals(model.DATA_PAGAMENTO, dataRtA);
//			exp.equals(model.STATO, Stato.INCASSATO.toString());
			if(listaTipiPendenza != null && !listaTipiPendenza.isEmpty()) {
				listaTipiPendenza.removeAll(Collections.singleton(null));
				exp.in(model.COD_TIPO_VERSAMENTO, listaTipiPendenza);
			}
			exp.isNotNull(model.COD_FLUSSO);
			
			IPaginatedExpression pagExp = this.getPagamentoService().toPaginatedExpression(exp);
			pagExp.offset(offset).limit(limit);
			pagExp.addOrder(model.DATA_PAGAMENTO, SortOrder.ASC);
			
			List<EntrataPrevista> entratePrevisteLst = new ArrayList<>();
			List<it.govpay.orm.VistaRiscossioni> riscossioniVOLst = this.getVistaRiscossioniServiceSearch().findAll(pagExp);
			for(it.govpay.orm.VistaRiscossioni riscossioneVO: riscossioniVOLst) {
				entratePrevisteLst.add(EntrataPrevistaConverter.toDTO(riscossioneVO));
			}
			return entratePrevisteLst;
		} catch(NotImplementedException e) {
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
	
	public long countRiscossioniDominio(String codDominio, Date dataRtDa, Date dataRtA, List<String> listaTipiPendenza) throws ServiceException{
		try {
			if(this.isAtomica()) {
				this.setupConnection(this.getIdTransaction());
			}
			
			VistaRiscossioniModel model = it.govpay.orm.VistaRiscossioni.model();
			IExpression exp = this.getVistaRiscossioniServiceSearch().newExpression();
			exp.equals(model.COD_DOMINIO, codDominio).and();
			if(dataRtDa != null) {
				exp.greaterEquals(model.DATA_PAGAMENTO, dataRtDa);
			}
			exp.lessEquals(model.DATA_PAGAMENTO, dataRtA);
//			exp.equals(model.STATO, Stato.INCASSATO.toString());
			if(listaTipiPendenza != null && !listaTipiPendenza.isEmpty()) {
				listaTipiPendenza.removeAll(Collections.singleton(null));
				exp.in(model.COD_TIPO_VERSAMENTO, listaTipiPendenza);
			}
			exp.isNotNull(model.COD_FLUSSO);
			
			NonNegativeNumber count = this.getVistaRiscossioniServiceSearch().count(exp);
			
			return count.longValue();
		} catch(NotImplementedException e) {
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
}
