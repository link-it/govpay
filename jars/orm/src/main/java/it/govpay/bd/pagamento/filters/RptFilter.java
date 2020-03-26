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
package it.govpay.bd.pagamento.filters;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import org.openspcoop2.generic_project.beans.CustomField;
import org.openspcoop2.generic_project.dao.IExpressionConstructor;
import org.openspcoop2.generic_project.exception.ExpressionException;
import org.openspcoop2.generic_project.exception.ExpressionNotImplementedException;
import org.openspcoop2.generic_project.exception.NotImplementedException;
import org.openspcoop2.generic_project.exception.ServiceException;
import org.openspcoop2.generic_project.expression.IExpression;
import org.openspcoop2.generic_project.expression.LikeMode;
import org.openspcoop2.utils.sql.ISQLQueryObject;
import org.openspcoop2.utils.sql.SQLQueryObjectException;

import it.govpay.bd.AbstractFilter;
import it.govpay.bd.ConnectionManager;
import it.govpay.model.Rpt.EsitoPagamento;
import it.govpay.model.Rpt.StatoConservazione;
import it.govpay.model.Rpt.StatoRpt;
import it.govpay.orm.RPT;
import it.govpay.orm.dao.jdbc.converter.RPTFieldConverter;
import it.govpay.orm.model.RPTModel;

public class RptFilter extends AbstractFilter {

	private List<Long> idVersamento;
	private String iuv;
	private String ccp;
	private String codDominio;
	private List<String> idDomini;
	private Boolean conservato;
	private List<String> stato;
	private List<Long> idRpt= null;
	private Long idPagamentoPortale = null;
	private String codPagamentoPortale = null;
	private Date dataInizio;
	private Date dataFine;
	private String codApplicazione = null;
	private String idPendenza = null;
	private EsitoPagamento esitoPagamento = null;
	
	private String cfCittadinoPagamentoPortale = null;
	private String codApplicazionePagamentoPortale = null;
	
	private Date dataRtA;
	private Date dataRtDa;
	private String idDebitore;
	private String idUnita;
	private String idTipoPendenza;
	private List<String> direzione;
	private List<String> divisione;
	private String tassonomia;
	private String anagraficaDebitore;

	public RptFilter(IExpressionConstructor expressionConstructor) {
		this(expressionConstructor,false);
	}

	public RptFilter(IExpressionConstructor expressionConstructor, boolean simpleSearch) {
		super(expressionConstructor, simpleSearch);
		this.listaFieldSimpleSearch.add(RPT.model().IUV);
		this.listaFieldSimpleSearch.add(RPT.model().CCP);
	}

	@Override
	public IExpression _toExpression() throws ServiceException {
		try {
			IExpression newExpression = this.newExpression();
			boolean addAnd = false;

			if(this.idVersamento != null && !this.idVersamento.isEmpty()) {
				this.idVersamento.removeAll(Collections.singleton(null));				
				addAnd = true;
				RPTFieldConverter rptFieldConverter = new RPTFieldConverter(ConnectionManager.getJDBCServiceManagerProperties().getDatabase()); 
				CustomField idRptCustomField = new CustomField("id_versamento",  Long.class, "id_versamento",  rptFieldConverter.toTable(RPT.model()));
				newExpression.in(idRptCustomField, this.idVersamento);
			}

			if(this.iuv != null){
				if(addAnd)
					newExpression.and();

				newExpression.ilike(RPT.model().IUV, this.iuv, LikeMode.ANYWHERE);
				addAnd = true;
			}
			
			if(this.ccp != null){
				if(addAnd)
					newExpression.and();

				newExpression.ilike(RPT.model().CCP, this.ccp, LikeMode.ANYWHERE);
				addAnd = true;
			}
			
			if(this.codDominio != null){
				if(addAnd)
					newExpression.and();
				newExpression.equals(RPT.model().COD_DOMINIO, this.codDominio);
				addAnd = true;
			}

			if(this.idDomini != null  && !this.idDomini.isEmpty()){
				this.idDomini.removeAll(Collections.singleton(null));
				if(addAnd)
					newExpression.and();
				newExpression.in(RPT.model().COD_DOMINIO, this.idDomini);
				addAnd = true;
			}
			
			if(this.idRpt != null && !this.idRpt.isEmpty()){
				if(addAnd)
					newExpression.and();
				RPTFieldConverter converter = new RPTFieldConverter(ConnectionManager.getJDBCServiceManagerProperties().getDatabase()); 
				CustomField cf = new CustomField("id", Long.class, "id", converter.toTable(RPT.model()));
				newExpression.in(cf, this.idRpt);
				addAnd = true;
			}
			
			if(this.conservato != null){
				if(addAnd)
					newExpression.and();
				
				IExpression newExpression2 = this.newExpression();
				if(this.conservato) {
					newExpression2.notEquals(RPT.model().STATO_CONSERVAZIONE, StatoConservazione.ERRORE.name()).and().isNotNull(RPT.model().STATO_CONSERVAZIONE);
				} else {
					newExpression2.equals(RPT.model().STATO_CONSERVAZIONE, StatoConservazione.ERRORE.name()).or().isNull(RPT.model().STATO_CONSERVAZIONE);					
				}
				
				newExpression.and(newExpression2);
				addAnd = true;
			}

			if(this.stato != null && !this.stato.isEmpty()){
				if(addAnd)
					newExpression.and();
				
				newExpression.in(RPT.model().STATO, this.stato);
				addAnd = true;
			}
			
			if(this.idPagamentoPortale != null) {
				if(addAnd)
					newExpression.and();
				
				
				RPTFieldConverter rptFieldConverter = new RPTFieldConverter(ConnectionManager.getJDBCServiceManagerProperties().getDatabase()); 
				CustomField idRptCustomField = new CustomField("id_pagamento_portale",  Long.class, "id_pagamento_portale",  rptFieldConverter.toTable(RPT.model()));
				newExpression.equals(idRptCustomField, this.idPagamentoPortale);
				addAnd = true;
			}
			
			if(this.codPagamentoPortale != null) {
				if(addAnd)
					newExpression.and();
				
				newExpression.equals(RPT.model().ID_PAGAMENTO_PORTALE.ID_SESSIONE, this.codPagamentoPortale);
				addAnd = true;
			}
			
			if(this.codApplicazionePagamentoPortale != null) {
				if(addAnd)
					newExpression.and();
				
				newExpression.equals(RPT.model().ID_PAGAMENTO_PORTALE.ID_APPLICAZIONE.COD_APPLICAZIONE, this.codApplicazionePagamentoPortale);
				addAnd = true;
			}
			
			if(this.cfCittadinoPagamentoPortale != null) {
				if(addAnd)
					newExpression.and();
				
				IExpression newExpression2 = this.newExpression();
				newExpression2.equals(RPT.model().ID_PAGAMENTO_PORTALE.VERSANTE_IDENTIFICATIVO, this.cfCittadinoPagamentoPortale)
					.or().equals(RPT.model().ID_VERSAMENTO.DEBITORE_IDENTIFICATIVO, this.cfCittadinoPagamentoPortale);
				
				
				newExpression.and(newExpression2);
				addAnd = true;
			}
			
			if(this.codApplicazione != null) {
				if(addAnd)
					newExpression.and();
				
				newExpression.equals(RPT.model().ID_VERSAMENTO.ID_APPLICAZIONE.COD_APPLICAZIONE, this.codApplicazione);
				addAnd = true;
			}
			
			if(this.idPendenza != null) {
				if(addAnd)
					newExpression.and();
				
				newExpression.equals(RPT.model().ID_VERSAMENTO.COD_VERSAMENTO_ENTE, this.idPendenza);
				addAnd = true;
			}
			
			if(this.dataInizio != null) {
				if(addAnd)
					newExpression.and();
				
				newExpression.greaterEquals(RPT.model().DATA_MSG_RICHIESTA, this.dataInizio);
				addAnd = true;
			}
			if(this.dataFine != null) {
				if(addAnd)
					newExpression.and();
				
				newExpression.lessEquals(RPT.model().DATA_MSG_RICHIESTA, this.dataFine);
				addAnd = true;
			}
			
			if(this.esitoPagamento != null) {
				if(addAnd)
					newExpression.and();
				
				newExpression.equals(RPT.model().COD_ESITO_PAGAMENTO, this.esitoPagamento.getCodifica());
				addAnd = true;
			}
			
			if(this.idDebitore != null) {
				if(addAnd)
					newExpression.and();
				
				newExpression.equals(RPT.model().ID_VERSAMENTO.DEBITORE_IDENTIFICATIVO, this.idDebitore);
				addAnd = true;
			}
			
			if(this.dataRtDa != null) {
				if(addAnd)
					newExpression.and();
				
				newExpression.greaterEquals(RPT.model().DATA_MSG_RICEVUTA, this.dataRtDa);
				addAnd = true;
			}
			if(this.dataRtA != null) {
				if(addAnd)
					newExpression.and();
				
				newExpression.lessEquals(RPT.model().DATA_MSG_RICEVUTA, this.dataRtA);
				addAnd = true;
			}
			
			if(this.idUnita != null){
				if(addAnd)
					newExpression.and();
 
				newExpression.equals(RPT.model().ID_VERSAMENTO.ID_UO.COD_UO, this.idUnita);
				addAnd = true;
			}
			
			if(this.idTipoPendenza != null){
				if(addAnd)
					newExpression.and();
 
				newExpression.equals(RPT.model().ID_VERSAMENTO.ID_TIPO_VERSAMENTO.COD_TIPO_VERSAMENTO, this.idTipoPendenza);
				addAnd = true;
			}
			
			if(this.direzione != null && this.direzione.size() > 0){
				this.direzione.removeAll(Collections.singleton(null));
				if(addAnd)
					newExpression.and();

				newExpression.in(RPT.model().ID_VERSAMENTO.DIREZIONE, this.direzione);
				addAnd = true;
			}

			if(this.divisione != null && this.divisione.size() > 0){
				this.divisione.removeAll(Collections.singleton(null));
				if(addAnd)
					newExpression.and();

				newExpression.in(RPT.model().ID_VERSAMENTO.DIVISIONE, this.divisione);
				addAnd = true;
			}
			
			if(this.tassonomia != null){
				if(addAnd)
					newExpression.and();
 
				newExpression.equals(RPT.model().ID_VERSAMENTO.TASSONOMIA, this.tassonomia);
				addAnd = true;
			}
			
			if(this.anagraficaDebitore != null) {
				if(addAnd)
					newExpression.and();
				
				newExpression.ilike(RPT.model().ID_VERSAMENTO.DEBITORE_ANAGRAFICA, this.anagraficaDebitore, LikeMode.ANYWHERE);
				addAnd = true;
			}

			return newExpression;
		} catch (NotImplementedException e) {
			throw new ServiceException(e);
		} catch (ExpressionNotImplementedException e) {
			throw new ServiceException(e);
		} catch (ExpressionException e) {
			throw new ServiceException(e);
		}
	}

	@Override
	public IExpression _toSimpleSearchExpression() throws ServiceException {
		try {
			IExpression newExpression = super._toSimpleSearchExpression();
			
			if(this.idVersamento != null){
				IExpression newExpressionVersamento = this.newExpression();
				RPTFieldConverter rptFieldConverter = new RPTFieldConverter(ConnectionManager.getJDBCServiceManagerProperties().getDatabase()); 
				CustomField idRptCustomField = new CustomField("id_versamento",  Long.class, "id_versamento",  rptFieldConverter.toTable(RPT.model()));
				newExpressionVersamento.equals(idRptCustomField, this.idVersamento);
				newExpression.and(newExpressionVersamento);
			}

			if(this.idDomini != null){
				IExpression newExpressionDomini = this.newExpression();
				this.idDomini.removeAll(Collections.singleton(null));
				newExpressionDomini.in(RPT.model().COD_DOMINIO, this.idDomini);
				newExpression.and(newExpressionDomini);
			}

			return newExpression;
		} catch (ExpressionNotImplementedException e) {
			throw new ServiceException(e);
		} catch (ExpressionException e) {
			throw new ServiceException(e);
		} catch (NotImplementedException e) {
			throw new ServiceException(e);
		}
	}
	
	@Override
	public ISQLQueryObject toWhereCondition(ISQLQueryObject sqlQueryObject) throws ServiceException {
		try {
			RPTFieldConverter converter = new RPTFieldConverter(ConnectionManager.getJDBCServiceManagerProperties().getDatabase()); 
			RPTModel model = it.govpay.orm.RPT.model();
			
			boolean addTabellaPagamentiPortale = false;
			boolean addTabellaVersamenti = false;
			boolean addTabellaUO = false;
			boolean addTabellaTipoVersamento = false;
			boolean addTabellaApplicazioni = false;
			
			String tableNameRPT = converter.toAliasTable(model);
			String tableNameApplicazioni = converter.toAliasTable(model.ID_PAGAMENTO_PORTALE.ID_APPLICAZIONE);
			String tableNamePagamentiPortale = converter.toAliasTable(model.ID_PAGAMENTO_PORTALE);
			String tableNameVersamenti = converter.toAliasTable(model.ID_VERSAMENTO);
			String tableNameUO = converter.toAliasTable(model.ID_VERSAMENTO.ID_UO);
			String tableNameTipiVersamento = converter.toAliasTable(model.ID_VERSAMENTO.ID_TIPO_VERSAMENTO);

			if(this.idVersamento != null && !this.idVersamento.isEmpty()) {
				this.idVersamento.removeAll(Collections.singleton(null));
				
				String [] idsVersamento = this.idVersamento.stream().map(e -> e.toString()).collect(Collectors.toList()).toArray(new String[this.idVersamento.size()]);
				sqlQueryObject.addWhereINCondition(converter.toTable(model.IUV, true) + ".id_versamento", false, idsVersamento );	
			}

			if(this.iuv != null){
				sqlQueryObject.addWhereLikeCondition(converter.toColumn(model.IUV, true), this.iuv, true, true);
			}
			
			if(this.ccp != null){
				sqlQueryObject.addWhereLikeCondition(converter.toColumn(model.CCP, true), this.ccp, true, true);
			}
			
			if(this.codDominio != null){
				sqlQueryObject.addWhereCondition(true,converter.toColumn(model.COD_DOMINIO, true) + " = ? ");
			}

			if(this.idDomini != null  && !this.idDomini.isEmpty()){
				this.idDomini.removeAll(Collections.singleton(null));
				
				String [] codDomini = this.idDomini.toArray(new String[this.idDomini.size()]);
				sqlQueryObject.addWhereINCondition(converter.toColumn(model.COD_DOMINIO, true), true, codDomini );
			}
			
			if(this.idRpt != null && !this.idRpt.isEmpty()){
				this.idRpt.removeAll(Collections.singleton(null));
				
				String [] idsRPT = this.idRpt.stream().map(e -> e.toString()).collect(Collectors.toList()).toArray(new String[this.idRpt.size()]);
				sqlQueryObject.addWhereINCondition(converter.toTable(model.IUV, true) + ".id", false, idsRPT );
			}
			
			if(this.conservato != null){
				
				if(this.conservato) {
					sqlQueryObject.addWhereCondition(true, true, converter.toColumn(model.STATO_CONSERVAZIONE, true) + " = ? ");
					sqlQueryObject.addWhereIsNotNullCondition(converter.toColumn(model.STATO_CONSERVAZIONE, true));
//					newExpression2.notEquals(RPT.model().STATO_CONSERVAZIONE, StatoConservazione.ERRORE.name()).and().isNotNull(RPT.model().STATO_CONSERVAZIONE);
				} else {
					sqlQueryObject.addWhereCondition(false, converter.toColumn(model.STATO_CONSERVAZIONE, true) + " = ? ", 
							converter.toColumn(model.STATO_CONSERVAZIONE, true) + " is null ");
//					newExpression2.equals(RPT.model().STATO_CONSERVAZIONE, StatoConservazione.ERRORE.name()).or().isNull(RPT.model().STATO_CONSERVAZIONE);					
				}
				
			}

			if(this.stato != null && !this.stato.isEmpty()){
				this.stato.removeAll(Collections.singleton(null));
				
				String [] statiS = this.stato.toArray(new String[this.stato.size()]);
				sqlQueryObject.addWhereINCondition(converter.toColumn(model.STATO, true), true, statiS );
			}
			
			if(this.idPagamentoPortale != null) {
				sqlQueryObject.addWhereCondition(converter.toTable(model.IUV, true) + ".id_pagamento_portale" + " = ? ");
			}
			
			if(this.codPagamentoPortale != null) {
				if(!addTabellaPagamentiPortale) {
					// RPT -> PP
					sqlQueryObject.addFromTable(tableNamePagamentiPortale);
					sqlQueryObject.addWhereCondition(tableNamePagamentiPortale+".id="+tableNameRPT+".id_pagamento_portale");
					
					addTabellaPagamentiPortale = true;
				}
				
				sqlQueryObject.addWhereCondition(true,converter.toColumn(model.ID_PAGAMENTO_PORTALE.ID_SESSIONE, true) + " = ? ");
			}
			
			if(this.codApplicazionePagamentoPortale != null) {
				if(!addTabellaApplicazioni) {
					if(!addTabellaPagamentiPortale) {
						// RPT -> PP
						sqlQueryObject.addFromTable(tableNamePagamentiPortale);
						sqlQueryObject.addWhereCondition(tableNamePagamentiPortale+".id="+tableNameRPT+".id_pagamento_portale");
						
						addTabellaPagamentiPortale = true;
					}
					
					// PP -> A
					sqlQueryObject.addFromTable(tableNameApplicazioni);
					
					
					addTabellaApplicazioni = true;
				}
				
				sqlQueryObject.addWhereCondition(tableNameApplicazioni+".id="+tableNamePagamentiPortale+".id_applicazione");
				sqlQueryObject.addWhereCondition(true,converter.toColumn(model.ID_PAGAMENTO_PORTALE.ID_APPLICAZIONE.COD_APPLICAZIONE, true) + " = ? ");
			}
			
			if(this.cfCittadinoPagamentoPortale != null) {
				if(!addTabellaPagamentiPortale) {
					// RPT -> PP
					sqlQueryObject.addFromTable(tableNamePagamentiPortale);
					sqlQueryObject.addWhereCondition(tableNamePagamentiPortale+".id="+tableNameRPT+".id_pagamento_portale");
					
					addTabellaPagamentiPortale = true;
				}
				
				if(!addTabellaVersamenti) {
					// RPT -> V
					sqlQueryObject.addFromTable(tableNameVersamenti);
					sqlQueryObject.addWhereCondition(tableNameVersamenti+".id="+tableNameRPT+".id_versamento");
					
					addTabellaVersamenti = true;
				}
				
				sqlQueryObject.addWhereCondition(false, converter.toColumn(model.ID_PAGAMENTO_PORTALE.VERSANTE_IDENTIFICATIVO, true) + " = ? ", 
						converter.toColumn(model.ID_VERSAMENTO.DEBITORE_IDENTIFICATIVO, true) + " = ? ");
			}
			
			if(this.codApplicazione != null) {
				if(!addTabellaApplicazioni) {
					if(!addTabellaVersamenti) {
						// RPT -> V
						sqlQueryObject.addFromTable(tableNameVersamenti);
						sqlQueryObject.addWhereCondition(tableNameVersamenti+".id="+tableNameRPT+".id_versamento");
						
						addTabellaVersamenti = true;
					}
					
					// PP -> A
					sqlQueryObject.addFromTable(tableNameApplicazioni);
					
					
					addTabellaApplicazioni = true;
				}
				
				sqlQueryObject.addWhereCondition(tableNameApplicazioni+".id="+tableNameVersamenti+".id_applicazione");
				sqlQueryObject.addWhereCondition(true,converter.toColumn(model.ID_VERSAMENTO.ID_APPLICAZIONE.COD_APPLICAZIONE, true) + " = ? ");
			}
			
			if(this.idPendenza != null) {
				if(!addTabellaVersamenti) {
					// RPT -> V
					sqlQueryObject.addFromTable(tableNameVersamenti);
					sqlQueryObject.addWhereCondition(tableNameVersamenti+".id="+tableNameRPT+".id_versamento");
					
					addTabellaVersamenti = true;
				}
				
				sqlQueryObject.addWhereCondition(true,converter.toColumn(model.ID_VERSAMENTO.COD_VERSAMENTO_ENTE, true) + " = ? ");
			}
			
			if(this.dataInizio != null) {
				sqlQueryObject.addWhereCondition(true,converter.toColumn(model.DATA_MSG_RICHIESTA, true) + " >= ? ");
			}
			if(this.dataFine != null) {
				sqlQueryObject.addWhereCondition(true,converter.toColumn(model.DATA_MSG_RICHIESTA, true) + " <= ? ");
			}
			
			if(this.esitoPagamento != null) {
				sqlQueryObject.addWhereCondition(true,converter.toColumn(model.COD_ESITO_PAGAMENTO, true) + " = ? ");
			}
			
			if(this.idDebitore != null) {
				if(!addTabellaVersamenti) {
					// RPT -> V
					sqlQueryObject.addFromTable(tableNameVersamenti);
					sqlQueryObject.addWhereCondition(tableNameVersamenti+".id="+tableNameRPT+".id_versamento");
					
					addTabellaVersamenti = true;
				}
				
				sqlQueryObject.addWhereCondition(true,converter.toColumn(model.ID_VERSAMENTO.DEBITORE_IDENTIFICATIVO, true) + " = ? ");
			}
			
			if(this.dataRtDa != null) {
				sqlQueryObject.addWhereCondition(true,converter.toColumn(model.DATA_MSG_RICEVUTA, true) + " >= ? ");
			}
			if(this.dataRtA != null) {
				sqlQueryObject.addWhereCondition(true,converter.toColumn(model.DATA_MSG_RICEVUTA, true) + " <= ? ");
			}
			
			if(this.idUnita != null){
				if(!addTabellaUO) {
					if(!addTabellaPagamentiPortale) {
						// RPT -> PP
						sqlQueryObject.addFromTable(tableNamePagamentiPortale);
						sqlQueryObject.addWhereCondition(tableNamePagamentiPortale+".id="+tableNameRPT+".id_pagamento_portale");
						
						addTabellaPagamentiPortale = true;
					}
					
					// PP -> UO
					sqlQueryObject.addFromTable(tableNameUO);
					addTabellaUO = true;
				}
				
				sqlQueryObject.addWhereCondition(tableNameUO+".id="+tableNamePagamentiPortale+".id_uo");
				sqlQueryObject.addWhereCondition(true,converter.toColumn(model.ID_VERSAMENTO.ID_UO.COD_UO, true) + " = ? ");
			}
			
			if(this.idTipoPendenza != null){
				if(!addTabellaTipoVersamento) {
					if(!addTabellaPagamentiPortale) {
						// RPT -> PP
						sqlQueryObject.addFromTable(tableNamePagamentiPortale);
						sqlQueryObject.addWhereCondition(tableNamePagamentiPortale+".id="+tableNameRPT+".id_pagamento_portale");
						
						addTabellaPagamentiPortale = true;
					}
					
					// PP -> UO
					sqlQueryObject.addFromTable(tableNameTipiVersamento);
					addTabellaTipoVersamento = true;
				}
				
				sqlQueryObject.addWhereCondition(tableNameTipiVersamento+".id="+tableNamePagamentiPortale+".id_tipo_versamento");
				sqlQueryObject.addWhereCondition(true,converter.toColumn(model.ID_VERSAMENTO.ID_TIPO_VERSAMENTO.COD_TIPO_VERSAMENTO, true) + " = ? ");
			}
			
			if(this.direzione != null && this.direzione.size() > 0){
				if(!addTabellaVersamenti) {
					// RPT -> V
					sqlQueryObject.addFromTable(tableNameVersamenti);
					sqlQueryObject.addWhereCondition(tableNameVersamenti+".id="+tableNameRPT+".id_versamento");
					
					addTabellaVersamenti = true;
				}
				
				this.direzione.removeAll(Collections.singleton(null));
				
				String [] direzioniS = this.direzione.toArray(new String[this.direzione.size()]);
				sqlQueryObject.addWhereINCondition(converter.toColumn(model.ID_VERSAMENTO.DIREZIONE, true), true, direzioniS);
			}

			if(this.divisione != null && this.divisione.size() > 0){
				if(!addTabellaVersamenti) {
					// RPT -> V
					sqlQueryObject.addFromTable(tableNameVersamenti);
					sqlQueryObject.addWhereCondition(tableNameVersamenti+".id="+tableNameRPT+".id_versamento");
					
					addTabellaVersamenti = true;
				}
				
				this.divisione.removeAll(Collections.singleton(null));
				
				String [] divisioniS = this.divisione.toArray(new String[this.divisione.size()]);
				sqlQueryObject.addWhereINCondition(converter.toColumn(model.ID_VERSAMENTO.DIVISIONE, true), true, divisioniS);
			}
			
			if(this.tassonomia != null){
				if(!addTabellaVersamenti) {
					// RPT -> V
					sqlQueryObject.addFromTable(tableNameVersamenti);
					sqlQueryObject.addWhereCondition(tableNameVersamenti+".id="+tableNameRPT+".id_versamento");
					
					addTabellaVersamenti = true;
				}
				
				sqlQueryObject.addWhereCondition(true,converter.toColumn(model.ID_VERSAMENTO.TASSONOMIA, true) + " = ? ");
			}
			
			if(this.anagraficaDebitore != null) {
				if(!addTabellaVersamenti) {
					// RPT -> V
					sqlQueryObject.addFromTable(tableNameVersamenti);
					sqlQueryObject.addWhereCondition(tableNameVersamenti+".id="+tableNameRPT+".id_versamento");
					
					addTabellaVersamenti = true;
				}
				
				sqlQueryObject.addWhereLikeCondition(converter.toColumn(model.ID_VERSAMENTO.DEBITORE_ANAGRAFICA, true), this.anagraficaDebitore, true, true);
			}

			return sqlQueryObject;
		} catch (ExpressionException e) {
			throw new ServiceException(e);
		} catch (SQLQueryObjectException e) {
			throw new ServiceException(e);
		}
	}

	@Override
	public Object[] getParameters(ISQLQueryObject sqlQueryObject) throws ServiceException {
		List<Object> lst = new ArrayList<Object>();
		
		if(this.idVersamento != null && !this.idVersamento.isEmpty()) {
			// donothing
		}

		if(this.iuv != null){
			// donothing
		}
		
		if(this.ccp != null){
			// donothing
		}
		
		if(this.codDominio != null){
			lst.add(this.codDominio);
		}

		if(this.idDomini != null  && !this.idDomini.isEmpty()){
			// donothing
		}
		
		if(this.idRpt != null && !this.idRpt.isEmpty()){
			// donothing
		}
		
		if(this.conservato != null){
			if(this.conservato) {
				lst.add(StatoConservazione.ERRORE.name());
			} else {
				lst.add(StatoConservazione.ERRORE.name());
			}
			
		}

		if(this.stato != null && !this.stato.isEmpty()){
			// donothing
		}
		
		if(this.idPagamentoPortale != null) {
			lst.add(this.idPagamentoPortale);
		}
		
		if(this.codPagamentoPortale != null) {
			lst.add(this.codPagamentoPortale);
		}
		
		if(this.codApplicazionePagamentoPortale != null) {
			lst.add(this.codApplicazionePagamentoPortale);
		}
		
		if(this.cfCittadinoPagamentoPortale != null) {
			lst.add(this.cfCittadinoPagamentoPortale);
			lst.add(this.cfCittadinoPagamentoPortale);
		}
		
		if(this.codApplicazione != null) {
			lst.add(this.codApplicazione);
		}
		
		if(this.idPendenza != null) {
			lst.add(this.idPendenza);
		}
		
		if(this.dataInizio != null) {
			lst.add(this.dataInizio);
		}
		if(this.dataFine != null) {
			lst.add(this.dataFine);
		}
		
		if(this.esitoPagamento != null) {
			lst.add(this.esitoPagamento.getCodifica());
		}
		
		if(this.idDebitore != null) {
			lst.add(this.idDebitore);
		}
		
		if(this.dataRtDa != null) {
			lst.add(this.dataRtDa);
		}
		if(this.dataRtA != null) {
			lst.add(this.dataRtA);
		}
		
		if(this.idUnita != null){
			lst.add(this.idUnita);
		}
		
		if(this.idTipoPendenza != null){
			lst.add(this.idTipoPendenza);
		}
		
		if(this.direzione != null && this.direzione.size() > 0){
			// donothing
		}

		if(this.divisione != null && this.divisione.size() > 0){
			// donothing
		}
		
		if(this.tassonomia != null){
			lst.add(this.tassonomia);
		}
		
		if(this.anagraficaDebitore != null) {
			// donothing
		}
		
		return lst.toArray(new Object[lst.size()]);
	}

	public List<Long> getIdVersamenti() {
		return this.idVersamento;
	}

	public void setIdVersamento(Long idVersamento) {
		this.idVersamento = new ArrayList<>();
		this.idVersamento.add(idVersamento);
	}
	
	public void setIdVersamenti(List<Long> ids) {
		this.idVersamento = new ArrayList<>();
		if(ids != null && !ids.isEmpty())
			this.idVersamento.addAll(ids);
	}

	public String getIuv() {
		return this.iuv;
	}
	public void setIuv(String iuv) {
		this.iuv = iuv;
	}

	public List<String> getIdDomini() {
		return this.idDomini;
	}

	public void setIdDomini(List<String> idDomini) {
		this.idDomini = idDomini;
	}

	public void setStato(StatoRpt stato) {
		this.stato = new ArrayList<>();
		if(stato!= null)
			this.stato.add(stato.name());
	}
	
	public void setStato(List<StatoRpt> stati) {
		this.stato = new ArrayList<String>();
		for(StatoRpt s : stati)
			this.stato.add(s.name());
	}

	public Boolean getConservato() {
		return this.conservato;
	}

	public void setConservato(Boolean conservato) {
		this.conservato = conservato;
	}

	public List<Long> getIdRpt() {
		return this.idRpt;
	}

	public void setIdRpt(List<Long> idRpt) {
		this.idRpt = idRpt;
	}

	public String getCcp() {
		return this.ccp;
	}

	public void setCcp(String ccp) {
		this.ccp = ccp;
	}

	public Long getIdPagamentoPortale() {
		return this.idPagamentoPortale;
	}

	public void setIdPagamentoPortale(Long idPagamentoPortale) {
		this.idPagamentoPortale = idPagamentoPortale;
	}
	public Date getDataInizio() {
		return this.dataInizio;
	}

	public void setDataInizio(Date dataInizio) {
		this.dataInizio = dataInizio;
	}

	public Date getDataFine() {
		return this.dataFine;
	}

	public void setDataFine(Date dataFine) {
		this.dataFine = dataFine;
	}

	public String getCodPagamentoPortale() {
		return this.codPagamentoPortale;
	}

	public void setCodPagamentoPortale(String codPagamentoPortale) {
		this.codPagamentoPortale = codPagamentoPortale;
	}

	public String getCodApplicazione() {
		return this.codApplicazione;
	}

	public void setCodApplicazione(String codApplicazione) {
		this.codApplicazione = codApplicazione;
	}

	public String getIdPendenza() {
		return this.idPendenza;
	}

	public void setIdPendenza(String idPendenza) {
		this.idPendenza = idPendenza;
	}

	public String getCodDominio() {
		return codDominio;
	}

	public void setCodDominio(String codDominio) {
		this.codDominio = codDominio;
	}

	public String getCfCittadinoPagamentoPortale() {
		return cfCittadinoPagamentoPortale;
	}

	public void setCfCittadinoPagamentoPortale(String cfCittadinoPagamentoPortale) {
		this.cfCittadinoPagamentoPortale = cfCittadinoPagamentoPortale;
	}

	public String getCodApplicazionePagamentoPortale() {
		return codApplicazionePagamentoPortale;
	}

	public void setCodApplicazionePagamentoPortale(String codApplicazionePagamentoPortale) {
		this.codApplicazionePagamentoPortale = codApplicazionePagamentoPortale;
	}

	public EsitoPagamento getEsitoPagamento() {
		return esitoPagamento;
	}

	public void setEsitoPagamento(EsitoPagamento esitoPagamento) {
		this.esitoPagamento = esitoPagamento;
	}

	public Date getDataRtA() {
		return dataRtA;
	}

	public void setDataRtA(Date dataRtA) {
		this.dataRtA = dataRtA;
	}

	public Date getDataRtDa() {
		return dataRtDa;
	}

	public void setDataRtDa(Date dataRtDa) {
		this.dataRtDa = dataRtDa;
	}

	public String getIdDebitore() {
		return idDebitore;
	}

	public void setIdDebitore(String idDebitore) {
		this.idDebitore = idDebitore;
	}

	public String getIdUnita() {
		return idUnita;
	}

	public void setIdUnita(String idUnita) {
		this.idUnita = idUnita;
	}

	public String getIdTipoPendenza() {
		return idTipoPendenza;
	}

	public void setIdTipoPendenza(String idTipoPendenza) {
		this.idTipoPendenza = idTipoPendenza;
	}

	public List<String> getDirezione() {
		return direzione;
	}

	public void setDirezione(List<String> direzione) {
		this.direzione = direzione;
	}

	public List<String> getDivisione() {
		return divisione;
	}

	public void setDivisione(List<String> divisione) {
		this.divisione = divisione;
	}

	public String getTassonomia() {
		return tassonomia;
	}

	public void setTassonomia(String tassonomia) {
		this.tassonomia = tassonomia;
	}

	public String getAnagraficaDebitore() {
		return anagraficaDebitore;
	}

	public void setAnagraficaDebitore(String anagraficaDebitore) {
		this.anagraficaDebitore = anagraficaDebitore;
	}

}
