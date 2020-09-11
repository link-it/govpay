package it.govpay.bd.reportistica.statistiche;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.Date;
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
import it.govpay.bd.model.Fr;
import it.govpay.bd.reportistica.statistiche.filters.StatisticaRendicontazioniFilter;
import it.govpay.bd.reportistica.statistiche.model.StatisticaRendicontazione;
import it.govpay.model.Fr.StatoFr;
import it.govpay.orm.Rendicontazione;
import it.govpay.orm.dao.jdbc.converter.RendicontazioneFieldConverter;

public class StatisticaRendicontazioniBD  extends BasicBD {


	public StatisticaRendicontazioniBD(BasicBD basicBD) {
		super(basicBD);
	}
	
	public StatisticaRendicontazioniBD(String idTransaction) {
		super(idTransaction);
	}
	
	public StatisticaRendicontazioniBD(String idTransaction, boolean useCache) {
		super(idTransaction, useCache);
	}
	
	public StatisticaRendicontazioniBD(BDConfigWrapper configWrapper) {
		super(configWrapper.getTransactionID(), configWrapper.isUseCache());
	}

	public StatisticaRendicontazioniFilter newFilter() throws ServiceException {
		return new StatisticaRendicontazioniFilter(this.getRendicontazioneService());
	}
	
	public long count(StatisticaRendicontazioniFilter filter, List<IField> gruppiDaFare) throws ServiceException {
		try {
			if(this.isAtomica()) {
				this.setupConnection(this.getIdTransaction());
				filter.setExpressionConstructor(this.getRendicontazioneService());
			}
			
			IExpression expression = filter.toExpression();

			for (IField iField : gruppiDaFare) {
				expression.addGroupBy(iField);
			}
			
			return this.getRendicontazioneService().count(expression).longValue();
		} catch (ExpressionException | ExpressionNotImplementedException | NotImplementedException e) {
			throw new ServiceException(e);
		} finally {
			if(this.isAtomica()) {
				this.closeConnection();
			}
		}
	}

	
	public List<StatisticaRendicontazione> statisticaNumeroRendicontazioni(StatisticaRendicontazioniFilter filter, List<IField> gruppiDaFare)throws ServiceException {
		List<StatisticaRendicontazione> lista = new ArrayList<>();

		try {
			if(this.isAtomica()) {
				this.setupConnection(this.getIdTransaction());
				filter.setExpressionConstructor(this.getRendicontazioneService());
			}
			
			IExpression expression = filter.toExpression();
			
			RendicontazioneFieldConverter converter = new RendicontazioneFieldConverter(this.getJdbcProperties().getDatabase());
			CustomField cf = new CustomField("id", Long.class, "id", converter.toTable(it.govpay.orm.Rendicontazione.model()));
			FunctionField fieldSommaPagamenti = new FunctionField(cf, Function.COUNT, "numeroPagamenti");
			FunctionField fieldSommaImporti = new FunctionField(Rendicontazione.model().IMPORTO_PAGATO, Function.SUM, "importoTotale");

			for (IField iField : gruppiDaFare) {
				expression.addGroupBy(iField);
			}
			
			IPaginatedExpression pagExpr = this.getRendicontazioneService().toPaginatedExpression(expression);
			
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
				List<Map<String,Object>> groupBy = this.getRendicontazioneService().groupBy(pagExpr, fieldSommaPagamenti, fieldSommaImporti);
				
				for (Map<String, Object> map : groupBy) {
					StatisticaRendicontazione entry = new StatisticaRendicontazione();
					entry.setNumeroPagamenti((Long) map.get("numeroPagamenti"));
					Object importoTotaleObj = map.get("importoTotale");
					if(importoTotaleObj instanceof Double)
						entry.setImporto(new BigDecimal((Double) importoTotaleObj).setScale(2, RoundingMode.HALF_EVEN));
					else
						entry.setImporto(BigDecimal.ZERO);
					
					if(map.containsKey(JDBCUtilities.getAlias(it.govpay.orm.Rendicontazione.model().ID_SINGOLO_VERSAMENTO.ID_VERSAMENTO.DIVISIONE))) {
						Object divisioneObj = map.get(JDBCUtilities.getAlias(it.govpay.orm.Rendicontazione.model().ID_SINGOLO_VERSAMENTO.ID_VERSAMENTO.DIVISIONE));
						if(divisioneObj instanceof String) {
							entry.setDivisione((String) divisioneObj);
						}
					}
					
					if(map.containsKey(JDBCUtilities.getAlias(it.govpay.orm.Rendicontazione.model().ID_SINGOLO_VERSAMENTO.ID_VERSAMENTO.DIREZIONE))) {
						Object direzioneObj = map.get(JDBCUtilities.getAlias(it.govpay.orm.Rendicontazione.model().ID_SINGOLO_VERSAMENTO.ID_VERSAMENTO.DIREZIONE));
						if(direzioneObj instanceof String) {
							entry.setDirezione((String) direzioneObj);
						}
					}
					
					if(map.containsKey(JDBCUtilities.getAlias(it.govpay.orm.Rendicontazione.model().ID_FR.COD_FLUSSO))) {
						Fr fr = new Fr();
						
						Object codFlussoObj = map.get(JDBCUtilities.getAlias(it.govpay.orm.Rendicontazione.model().ID_FR.COD_FLUSSO));
						if(codFlussoObj instanceof String) {
							entry.setCodFlusso((String) codFlussoObj);
							fr.setCodFlusso((String) codFlussoObj);
						}
						
						Object dataOraFlussoObj = map.get(JDBCUtilities.getAlias(it.govpay.orm.Rendicontazione.model().ID_FR.DATA_ORA_FLUSSO));
						if(dataOraFlussoObj instanceof Date) {
							fr.setDataFlusso((Date) dataOraFlussoObj);
						}
						
						Object iurObj = map.get(JDBCUtilities.getAlias(it.govpay.orm.Rendicontazione.model().ID_FR.IUR));
						if(iurObj instanceof String) {
							fr.setIur((String) iurObj);
						}
						
						Object dataRegolamentoObj = map.get(JDBCUtilities.getAlias(it.govpay.orm.Rendicontazione.model().ID_FR.DATA_REGOLAMENTO));
						if(dataRegolamentoObj instanceof Date) {
							fr.setDataRegolamento((Date) dataRegolamentoObj);
						}
						
						Object codPspObj = map.get(JDBCUtilities.getAlias(it.govpay.orm.Rendicontazione.model().ID_FR.COD_PSP));
						if(codPspObj instanceof String) {
							fr.setCodPsp((String) codPspObj);
						}
						
						Object codBicRiversamentoObj = map.get(JDBCUtilities.getAlias(it.govpay.orm.Rendicontazione.model().ID_FR.COD_BIC_RIVERSAMENTO));
						if(codBicRiversamentoObj instanceof String) {
							fr.setCodBicRiversamento((String) codBicRiversamentoObj);
						}
						
						Object codDominioObj = map.get(JDBCUtilities.getAlias(it.govpay.orm.Rendicontazione.model().ID_FR.COD_DOMINIO));
						if(codDominioObj instanceof String) {
							fr.setCodDominio((String) codDominioObj);
						}
						
						Object numeroPagamentiObj = map.get(JDBCUtilities.getAlias(it.govpay.orm.Rendicontazione.model().ID_FR.NUMERO_PAGAMENTI));
						if(numeroPagamentiObj instanceof Long) {
							fr.setNumeroPagamenti((Long) numeroPagamentiObj);
						}
						
						Object importoTotaleFlussoObj = map.get(JDBCUtilities.getAlias(it.govpay.orm.Rendicontazione.model().ID_FR.IMPORTO_TOTALE_PAGAMENTI));
						if(importoTotaleFlussoObj instanceof Double) {
							fr.setImportoTotalePagamenti(new BigDecimal((Double) importoTotaleFlussoObj));
						}
						
						Object statoObj = map.get(JDBCUtilities.getAlias(it.govpay.orm.Rendicontazione.model().ID_FR.STATO));
						if(statoObj instanceof String) {
							fr.setStato(StatoFr.valueOf((String) statoObj));
						}
						
						Object ragioneSocialeDominioObj = map.get(JDBCUtilities.getAlias(it.govpay.orm.Rendicontazione.model().ID_FR.RAGIONE_SOCIALE_DOMINIO));
						if(ragioneSocialeDominioObj instanceof String) {
							fr.setRagioneSocialeDominio((String) ragioneSocialeDominioObj);
						}
						
						Object ragionesSocialePspObj = map.get(JDBCUtilities.getAlias(it.govpay.orm.Rendicontazione.model().ID_FR.RAGIONE_SOCIALE_PSP));
						if(ragionesSocialePspObj instanceof String) {
							fr.setRagioneSocialePsp((String) ragionesSocialePspObj);
						}
						
						
						entry.setFlusso(fr);
					}
					
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
