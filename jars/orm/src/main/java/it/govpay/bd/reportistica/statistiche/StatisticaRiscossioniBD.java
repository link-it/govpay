package it.govpay.bd.reportistica.statistiche;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.openspcoop2.generic_project.beans.CustomField;
import org.openspcoop2.generic_project.beans.Function;
import org.openspcoop2.generic_project.beans.FunctionField;
import org.openspcoop2.generic_project.beans.IField;
import org.openspcoop2.generic_project.exception.ExpressionException;
import org.openspcoop2.generic_project.exception.ExpressionNotImplementedException;
import org.openspcoop2.generic_project.exception.NotFoundException;
import org.openspcoop2.generic_project.exception.NotImplementedException;
import org.openspcoop2.generic_project.exception.ServiceException;
import org.openspcoop2.generic_project.expression.IExpression;

import it.govpay.bd.BasicBD;
import it.govpay.bd.reportistica.statistiche.filters.StatisticaRiscossioniFilter;
import it.govpay.bd.reportistica.statistiche.model.StatisticaRiscossione;
import it.govpay.orm.Pagamento;
import it.govpay.orm.dao.jdbc.converter.PagamentoFieldConverter;

public class StatisticaRiscossioniBD  extends BasicBD {


	public StatisticaRiscossioniBD(BasicBD basicBD) {
		super(basicBD);
	}

	public StatisticaRiscossioniFilter newFilter() throws ServiceException {
		return new StatisticaRiscossioniFilter(this.getPagamentoService());
	}

	public List<StatisticaRiscossione> statisticaNumeroPagamenti(StatisticaRiscossioniFilter filter, List<IField> gruppiDaFare)throws ServiceException {
		List<StatisticaRiscossione> lista = new ArrayList<>();

		IExpression expression = filter.toExpression();

		try {
			PagamentoFieldConverter converter = new PagamentoFieldConverter(this.getJdbcProperties().getDatabase());
			CustomField cf = new CustomField("id", Long.class, "id", converter.toTable(it.govpay.orm.Pagamento.model()));
			FunctionField fieldSommaPagamenti = new FunctionField(cf, Function.COUNT, "numeroPagamenti");
			FunctionField fieldSommaImporti = new FunctionField(Pagamento.model().IMPORTO_PAGATO, Function.SUM, "importoTotale");

			for (IField iField : gruppiDaFare) {
				expression.addGroupBy(iField);
			}

			try {
				List<Map<String,Object>> groupBy = this.getVersamentoService().groupBy(expression, fieldSommaPagamenti, fieldSommaImporti);
				
				for (Map<String, Object> map : groupBy) {
					StatisticaRiscossione entry = new StatisticaRiscossione(filter.getFiltro());
					entry.setNumeroPagamenti((Long) map.get("numeroPagamenti"));
					entry.setImporto((Double) map.get("importoTotale"));
				}
			}catch (NotFoundException e) {

			}

		} catch (ExpressionException | ExpressionNotImplementedException | NotImplementedException e) {
			throw new ServiceException(e);
		}

		return lista;
	}

}
