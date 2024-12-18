/*
 * GovPay - Porta di Accesso al Nodo dei Pagamenti SPC
 * http://www.gov4j.it/govpay
 *
 * Copyright (c) 2014-2024 Link.it srl (http://www.link.it).
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
import org.openspcoop2.generic_project.dao.jdbc.utils.GenericJDBCUtilities;
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

	public StatisticaRendicontazioniFilter newFilter() {
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
						entry.setImporto(BigDecimal.valueOf((Double) importoTotaleObj).setScale(2, RoundingMode.HALF_EVEN));
					else
						entry.setImporto(BigDecimal.ZERO);
					
					if(map.containsKey(GenericJDBCUtilities.getAlias(it.govpay.orm.Rendicontazione.model().ID_SINGOLO_VERSAMENTO.ID_VERSAMENTO.DIVISIONE))) {
						Object divisioneObj = map.get(GenericJDBCUtilities.getAlias(it.govpay.orm.Rendicontazione.model().ID_SINGOLO_VERSAMENTO.ID_VERSAMENTO.DIVISIONE));
						if(divisioneObj instanceof String divisione) {
							entry.setDivisione(divisione);
						}
					}
					
					if(map.containsKey(GenericJDBCUtilities.getAlias(it.govpay.orm.Rendicontazione.model().ID_SINGOLO_VERSAMENTO.ID_VERSAMENTO.DIREZIONE))) {
						Object direzioneObj = map.get(GenericJDBCUtilities.getAlias(it.govpay.orm.Rendicontazione.model().ID_SINGOLO_VERSAMENTO.ID_VERSAMENTO.DIREZIONE));
						if(direzioneObj instanceof String direzione) {
							entry.setDirezione(direzione);
						}
					}
					
					if(map.containsKey(GenericJDBCUtilities.getAlias(it.govpay.orm.Rendicontazione.model().ID_FR.COD_FLUSSO))) {
						Fr fr = new Fr();
						
						Object codFlussoObj = map.get(GenericJDBCUtilities.getAlias(it.govpay.orm.Rendicontazione.model().ID_FR.COD_FLUSSO));
						if(codFlussoObj instanceof String codFlusso) {
							entry.setCodFlusso(codFlusso);
							fr.setCodFlusso(codFlusso);
						}
						
						Object dataOraFlussoObj = map.get(GenericJDBCUtilities.getAlias(it.govpay.orm.Rendicontazione.model().ID_FR.DATA_ORA_FLUSSO));
						if(dataOraFlussoObj instanceof Date dataOraFlusso) {
							fr.setDataFlusso(dataOraFlusso);
						}
						
						Object iurObj = map.get(GenericJDBCUtilities.getAlias(it.govpay.orm.Rendicontazione.model().ID_FR.IUR));
						if(iurObj instanceof String iur) {
							fr.setIur(iur);
						}
						
						Object dataRegolamentoObj = map.get(GenericJDBCUtilities.getAlias(it.govpay.orm.Rendicontazione.model().ID_FR.DATA_REGOLAMENTO));
						if(dataRegolamentoObj instanceof Date dataRegolamento) {
							fr.setDataRegolamento(dataRegolamento);
						}
						
						Object codPspObj = map.get(GenericJDBCUtilities.getAlias(it.govpay.orm.Rendicontazione.model().ID_FR.COD_PSP));
						if(codPspObj instanceof String codPsp) {
							fr.setCodPsp(codPsp);
						}
						
						Object codBicRiversamentoObj = map.get(GenericJDBCUtilities.getAlias(it.govpay.orm.Rendicontazione.model().ID_FR.COD_BIC_RIVERSAMENTO));
						if(codBicRiversamentoObj instanceof String codBicRiversamento) {
							fr.setCodBicRiversamento(codBicRiversamento);
						}
						
						Object codDominioObj = map.get(GenericJDBCUtilities.getAlias(it.govpay.orm.Rendicontazione.model().ID_FR.COD_DOMINIO));
						if(codDominioObj instanceof String codDominio) {
							fr.setCodDominio(codDominio);
						}
						
						Object numeroPagamentiObj = map.get(GenericJDBCUtilities.getAlias(it.govpay.orm.Rendicontazione.model().ID_FR.NUMERO_PAGAMENTI));
						if(numeroPagamentiObj instanceof Long numeroPagamenti) {
							fr.setNumeroPagamenti(numeroPagamenti);
						}
						
						Object importoTotaleFlussoObj = map.get(GenericJDBCUtilities.getAlias(it.govpay.orm.Rendicontazione.model().ID_FR.IMPORTO_TOTALE_PAGAMENTI));
						if(importoTotaleFlussoObj instanceof BigDecimal importoTotaleFlussi) {
							fr.setImportoTotalePagamenti(importoTotaleFlussi);
						}
						
						Object statoObj = map.get(GenericJDBCUtilities.getAlias(it.govpay.orm.Rendicontazione.model().ID_FR.STATO));
						if(statoObj instanceof String stato) {
							fr.setStato(StatoFr.valueOf(stato));
						}
						
						Object ragioneSocialeDominioObj = map.get(GenericJDBCUtilities.getAlias(it.govpay.orm.Rendicontazione.model().ID_FR.RAGIONE_SOCIALE_DOMINIO));
						if(ragioneSocialeDominioObj instanceof String ragioneSocialeDominio) {
							fr.setRagioneSocialeDominio(ragioneSocialeDominio);
						}
						
						Object ragionesSocialePspObj = map.get(GenericJDBCUtilities.getAlias(it.govpay.orm.Rendicontazione.model().ID_FR.RAGIONE_SOCIALE_PSP));
						if(ragionesSocialePspObj instanceof String ragionesSocialePsp) {
							fr.setRagioneSocialePsp(ragionesSocialePsp);
						}
						
						
						entry.setFlusso(fr);
					}
					
					lista.add(entry);
				}
			}catch (NotFoundException e) {
				//donothing
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
