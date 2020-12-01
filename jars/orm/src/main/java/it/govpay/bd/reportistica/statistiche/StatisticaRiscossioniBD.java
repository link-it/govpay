package it.govpay.bd.reportistica.statistiche;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.openspcoop2.generic_project.beans.CustomField;
import org.openspcoop2.generic_project.beans.Function;
import org.openspcoop2.generic_project.beans.FunctionField;
import org.openspcoop2.generic_project.beans.IField;
import org.openspcoop2.generic_project.dao.jdbc.utils.JDBCUtilities;
import org.openspcoop2.generic_project.exception.ExpressionException;
import org.openspcoop2.generic_project.exception.ExpressionNotImplementedException;
import org.openspcoop2.generic_project.exception.NotFoundException;
import org.openspcoop2.generic_project.exception.NotImplementedException;
import org.openspcoop2.generic_project.exception.ServiceException;
import org.openspcoop2.generic_project.expression.IExpression;
import org.openspcoop2.generic_project.expression.IPaginatedExpression;
import org.openspcoop2.generic_project.expression.SortOrder;

import it.govpay.bd.BDConfigWrapper;
import it.govpay.bd.BasicBD;
import it.govpay.bd.reportistica.statistiche.filters.StatisticaRiscossioniFilter;
import it.govpay.bd.reportistica.statistiche.model.StatisticaRiscossione;
import it.govpay.orm.Pagamento;
import it.govpay.orm.dao.jdbc.converter.PagamentoFieldConverter;

public class StatisticaRiscossioniBD  extends BasicBD {


	public StatisticaRiscossioniBD(BasicBD basicBD) {
		super(basicBD);
	}
	
	public StatisticaRiscossioniBD(String idTransaction) {
		super(idTransaction);
	}
	
	public StatisticaRiscossioniBD(String idTransaction, boolean useCache) {
		super(idTransaction, useCache);
	}
	
	public StatisticaRiscossioniBD(BDConfigWrapper configWrapper) {
		super(configWrapper.getTransactionID(), configWrapper.isUseCache());
	}

	public StatisticaRiscossioniFilter newFilter() throws ServiceException {
		return new StatisticaRiscossioniFilter(this.getPagamentoService());
	}
	
	public long count(StatisticaRiscossioniFilter filter, List<IField> gruppiDaFare) throws ServiceException {
		try {
			if(this.isAtomica()) {
				this.setupConnection(this.getIdTransaction());
				filter.setExpressionConstructor(this.getPagamentoService());
			}
			
			IExpression expression = filter.toExpression();

			for (IField iField : gruppiDaFare) {
				expression.addGroupBy(iField);
			}
			
			return this.getPagamentoService().count(expression).longValue();
		} catch (ExpressionException | ExpressionNotImplementedException | NotImplementedException e) {
			throw new ServiceException(e);
		} finally {
			if(this.isAtomica()) {
				this.closeConnection();
			}
		}
	}

	public List<StatisticaRiscossione> statisticaNumeroPagamenti(StatisticaRiscossioniFilter filter, List<IField> gruppiDaFare)throws ServiceException {
		List<StatisticaRiscossione> lista = new ArrayList<>();

		try {
			if(this.isAtomica()) {
				this.setupConnection(this.getIdTransaction());
				filter.setExpressionConstructor(this.getPagamentoService());
			}
			
			IExpression expression = filter.toExpression();
			
			PagamentoFieldConverter converter = new PagamentoFieldConverter(this.getJdbcProperties().getDatabase());
			CustomField cf = new CustomField("id", Long.class, "id", converter.toTable(it.govpay.orm.Pagamento.model()));
			FunctionField fieldSommaPagamenti = new FunctionField(cf, Function.COUNT, "numeroPagamenti");
			FunctionField fieldSommaImporti = new FunctionField(Pagamento.model().IMPORTO_PAGATO, Function.SUM, "importoTotale");

			for (IField iField : gruppiDaFare) {
				expression.addGroupBy(iField);
			}
			
			IPaginatedExpression pagExpr = this.getPagamentoService().toPaginatedExpression(expression);
			
			if(filter.getOffset() != null) {
				pagExpr.offset(filter.getOffset());
			}
	
			if(filter.getLimit() != null) {
				pagExpr.limit(filter.getLimit());
			}
			
			for (IField iField : gruppiDaFare) {
				pagExpr.addOrder(iField, SortOrder.ASC);
			}

			try {
				List<Map<String,Object>> groupBy = this.getPagamentoService().groupBy(pagExpr, fieldSommaPagamenti, fieldSommaImporti);
				
				for (Map<String, Object> map : groupBy) {
					StatisticaRiscossione entry = new StatisticaRiscossione();
					entry.setNumeroPagamenti((Long) map.get("numeroPagamenti"));
					Object importoTotaleObj = map.get("importoTotale");
					if(importoTotaleObj instanceof Double)
						entry.setImporto(new BigDecimal((Double) importoTotaleObj).setScale(2, RoundingMode.HALF_EVEN));
					else
						entry.setImporto(BigDecimal.ZERO);
					
					if(map.containsKey(JDBCUtilities.getAlias(it.govpay.orm.Pagamento.model().ID_SINGOLO_VERSAMENTO.ID_VERSAMENTO.ID_APPLICAZIONE.COD_APPLICAZIONE))) {
						Object applicazioneObj = map.get(JDBCUtilities.getAlias(it.govpay.orm.Pagamento.model().ID_SINGOLO_VERSAMENTO.ID_VERSAMENTO.ID_APPLICAZIONE.COD_APPLICAZIONE));
						if(applicazioneObj instanceof String) {
							entry.setCodApplicazione((String) applicazioneObj);
						}
					}
					
					if(map.containsKey(JDBCUtilities.getAlias(it.govpay.orm.Pagamento.model().ID_SINGOLO_VERSAMENTO.ID_VERSAMENTO.ID_UO.COD_UO))) {
						Object uoObj = map.get(JDBCUtilities.getAlias(it.govpay.orm.Pagamento.model().ID_SINGOLO_VERSAMENTO.ID_VERSAMENTO.ID_UO.COD_UO));
						if(uoObj instanceof String) {
							entry.setCodUo((String) uoObj);
						}
					}
					
					if(map.containsKey(JDBCUtilities.getAlias(it.govpay.orm.Pagamento.model().ID_SINGOLO_VERSAMENTO.ID_VERSAMENTO.ID_TIPO_VERSAMENTO.COD_TIPO_VERSAMENTO))) {
						Object tipoVersamentoObj = map.get(JDBCUtilities.getAlias(it.govpay.orm.Pagamento.model().ID_SINGOLO_VERSAMENTO.ID_VERSAMENTO.ID_TIPO_VERSAMENTO.COD_TIPO_VERSAMENTO));
						if(tipoVersamentoObj instanceof String) {
							entry.setCodTipoVersamento((String) tipoVersamentoObj);
						}
					}
					
					if(map.containsKey(JDBCUtilities.getAlias(it.govpay.orm.Pagamento.model().ID_SINGOLO_VERSAMENTO.ID_VERSAMENTO.ID_UO.ID_DOMINIO.COD_DOMINIO))) {
						Object dominioObj = map.get(JDBCUtilities.getAlias(it.govpay.orm.Pagamento.model().ID_SINGOLO_VERSAMENTO.ID_VERSAMENTO.ID_UO.ID_DOMINIO.COD_DOMINIO));
						if(dominioObj instanceof String) {
							entry.setCodDominio((String) dominioObj);
						}
					}
					
					if(map.containsKey(JDBCUtilities.getAlias(it.govpay.orm.Pagamento.model().ID_SINGOLO_VERSAMENTO.ID_VERSAMENTO.DIVISIONE))) {
						Object divisioneObj = map.get(JDBCUtilities.getAlias(it.govpay.orm.Pagamento.model().ID_SINGOLO_VERSAMENTO.ID_VERSAMENTO.DIVISIONE));
						if(divisioneObj instanceof String) {
							entry.setDivisione((String) divisioneObj);
						}
					}
					
					if(map.containsKey(JDBCUtilities.getAlias(it.govpay.orm.Pagamento.model().ID_SINGOLO_VERSAMENTO.ID_VERSAMENTO.DIREZIONE))) {
						Object direzioneObj = map.get(JDBCUtilities.getAlias(it.govpay.orm.Pagamento.model().ID_SINGOLO_VERSAMENTO.ID_VERSAMENTO.DIREZIONE));
						if(direzioneObj instanceof String) {
							entry.setDirezione((String) direzioneObj);
						}
					}
					
					if(map.containsKey(JDBCUtilities.getAlias(it.govpay.orm.Pagamento.model().ID_SINGOLO_VERSAMENTO.ID_VERSAMENTO.TASSONOMIA))) {
						Object tassonomiaObj = map.get(JDBCUtilities.getAlias(it.govpay.orm.Pagamento.model().ID_SINGOLO_VERSAMENTO.ID_VERSAMENTO.TASSONOMIA));
						if(tassonomiaObj instanceof String) {
							entry.setTassonomia((String) tassonomiaObj);
						}
					}
					
//					if(map.containsKey(JDBCUtilities.getAlias(it.govpay.orm.Pagamento.model().TIPO))) {
//						Object tipoObj = map.get(JDBCUtilities.getAlias(it.govpay.orm.Pagamento.model().TIPO));
//						if(tipoObj instanceof String) {
//							entry.setTipo(TipoPagamento.valueOf((String) tipoObj));
//						}
//					}
					
					lista.add(entry);
				}
			}catch (NotFoundException e) {

			}

		} catch (ExpressionException | ExpressionNotImplementedException | NotImplementedException e) {
			throw new ServiceException(e);
		} finally {
			if(this.isAtomica()) {
				this.closeConnection();
			}
		}

		return lista;
	}
}
