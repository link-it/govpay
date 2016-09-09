package it.govpay.bd.reportistica;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.openspcoop2.generic_project.beans.CustomField;
import org.openspcoop2.generic_project.beans.Function;
import org.openspcoop2.generic_project.beans.FunctionField;
import org.openspcoop2.generic_project.exception.ExpressionException;
import org.openspcoop2.generic_project.exception.ExpressionNotImplementedException;
import org.openspcoop2.generic_project.exception.NotFoundException;
import org.openspcoop2.generic_project.exception.NotImplementedException;
import org.openspcoop2.generic_project.exception.ServiceException;
import org.openspcoop2.generic_project.expression.IPaginatedExpression;

import it.govpay.bd.BasicBD;
import it.govpay.bd.ConnectionManager;
import it.govpay.bd.model.reportistica.Pagamento;
import it.govpay.bd.reportistica.filters.PagamentoFilter;
import it.govpay.orm.dao.IPagamentoService;
import it.govpay.orm.dao.jdbc.converter.PagamentoFieldConverter;

public class PagamentiBD extends BasicBD{

	public PagamentiBD(BasicBD basicBD) {
		super(basicBD);
	}

	public PagamentoFilter newFilter() throws ServiceException {
		return new PagamentoFilter(this.getPagamentoService());
	}

	public List<Pagamento> findAll(PagamentoFilter filter) throws ServiceException {
		try {

			IPagamentoService pagamentoService = this.getPagamentoService();
			IPaginatedExpression pagExpr = filter.toPaginatedExpression();

			PagamentoFieldConverter pagamentoFieldConverter = new PagamentoFieldConverter(ConnectionManager.getJDBCServiceManagerProperties().getDatabase()); 

			pagExpr.addGroupBy(new CustomField("id", Long.class, "id", pagamentoFieldConverter.toTable(it.govpay.orm.Pagamento.model().ID_SINGOLO_VERSAMENTO.ID_VERSAMENTO)));

			List<FunctionField> lstFunction = new ArrayList<FunctionField>();

			lstFunction.add(new FunctionField(it.govpay.orm.Pagamento.model().DATA_PAGAMENTO, Function.MAX, "dataPagamento"));
			lstFunction.add(new FunctionField(it.govpay.orm.Pagamento.model().ID_SINGOLO_VERSAMENTO.ID_VERSAMENTO.COD_VERSAMENTO_ENTE, Function.MAX, "codVersamentoEnte"));
			lstFunction.add(new FunctionField(it.govpay.orm.Pagamento.model().ID_SINGOLO_VERSAMENTO.ID_VERSAMENTO.DEBITORE_IDENTIFICATIVO, Function.MAX, "codiceFiscaleDebitore"));
			//			lstFunction.add(new FunctionField(it.govpay.orm.Pagamento.model().ID_SINGOLO_VERSAMENTO.ID_VERSAMENTO., Function.MAX, "causale"));
			//			lstFunction.add(new FunctionField(it.govpay.orm.Pagamento.model().ID_SINGOLO_VERSAMENTO.ID_VERSAMENTO.STATO_VERSAMENTO, Function.MAX, "statoVersamento"));
			lstFunction.add(new FunctionField(it.govpay.orm.Pagamento.model().ID_SINGOLO_VERSAMENTO.ID_VERSAMENTO.IMPORTO_TOTALE, Function.MAX, "importoTotale"));
			lstFunction.add(new FunctionField(it.govpay.orm.Pagamento.model().IMPORTO_PAGATO, Function.SUM, "importoPagato"));
			lstFunction.add(new FunctionField(it.govpay.orm.Pagamento.model().ID_RPT.IUV, Function.MAX, "iuv"));

			List<Map<String,Object>> groupBy = pagamentoService.groupBy(pagExpr, lstFunction.toArray(new FunctionField[lstFunction.size()])); 





			return new ArrayList<Pagamento>();
			//			List<it.govpay.orm.Pagamento>pagamentoVOLst = this.getPagamentoService().findAll(filter.toPaginatedExpression()); 
			//			return PagamentoConverter.toDTO(pagamentoVOLst);
		} catch (NotFoundException e) {
			return new ArrayList<Pagamento>();
		}catch (NotImplementedException e) {
			throw new ServiceException(e);
		} catch (ExpressionException e) {
			throw new ServiceException(e);
		} catch (ExpressionNotImplementedException e) {
			throw new ServiceException(e);
		}
	}
	public long count(PagamentoFilter filter) throws ServiceException {
		//		try {
		return 0; //return this.getPagamentoService().count(filter.toExpression()).longValue();
		//		} catch (NotImplementedException e) {
		//			throw new ServiceException(e);
		//		}
	}

	public Pagamento getPagamento(long id) throws ServiceException {
		return null;
	}

}
