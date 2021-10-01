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
package it.govpay.bd.viste.filters;

import java.util.ArrayList;
import java.util.Calendar;
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
import org.openspcoop2.generic_project.expression.SortOrder;
import org.openspcoop2.utils.sql.ISQLQueryObject;
import org.openspcoop2.utils.sql.SQLQueryObjectException;

import it.govpay.bd.AbstractFilter;
import it.govpay.bd.ConnectionManager;
import it.govpay.bd.FilterSortWrapper;
import it.govpay.bd.model.IdUnitaOperativa;
import it.govpay.model.Versamento.StatoPagamento;
import it.govpay.model.Versamento.StatoVersamento;
import it.govpay.model.Versamento.TipologiaTipoVersamento;
import it.govpay.orm.Versamento;
import it.govpay.orm.VersamentoIncasso;
import it.govpay.orm.dao.jdbc.converter.VersamentoFieldConverter;
import it.govpay.orm.dao.jdbc.converter.VersamentoIncassoFieldConverter;
import it.govpay.orm.model.VersamentoIncassoModel;

public class VersamentoIncassoFilter extends AbstractFilter {

	private List<StatoVersamento> statiVersamento =  null;
	private String codUnivocoDebitore;
	private List<Long> idDomini;
	private List<Long> idVersamento= null;
	private List<IdUnitaOperativa> idUo;
	private String codVersamento = null;
	private Long idPagamentoPortale = null;
	private String codPagamentoPortale = null;
	private Date dataInizio;
	private Date dataFine;
	private String codApplicazione = null;
	private String codDominio = null;
	private String cfCittadino;
	private List<Long> idTipiVersamento = null;
	private String codTipoVersamento = null;
	private boolean abilitaFiltroCittadino = false;
	private String divisione;
	private String direzione;
	private String idSessione;
	private String iuv;
	private boolean abilitaFiltroNonScaduto = false;
	private boolean abilitaFiltroScaduto = false;
	private Boolean mostraSpontaneiNonPagati = null;
	
	public enum SortFields {
		STATO_ASC, STATO_DESC, SCADENZA_ASC, SCADENZA_DESC, AGGIORNAMENTO_ASC, AGGIORNAMENTO_DESC, CARICAMENTO_ASC, CARICAMENTO_DESC
	}

	public VersamentoIncassoFilter(IExpressionConstructor expressionConstructor) {
		this(expressionConstructor,false);
	}
	
	public VersamentoIncassoFilter(IExpressionConstructor expressionConstructor, boolean simpleSearch) {
		super(expressionConstructor, simpleSearch);
		this.listaFieldSimpleSearch.add(VersamentoIncasso.model().DEBITORE_IDENTIFICATIVO);
		this.listaFieldSimpleSearch.add(VersamentoIncasso.model().COD_VERSAMENTO_ENTE);
		this.listaFieldSimpleSearch.add(VersamentoIncasso.model().IUV.IUV);
	}

	@Override
	public IExpression _toSimpleSearchExpression() throws ServiceException {
		try {
			IExpression newExpressionOr = super._toSimpleSearchExpression();
			
			if(this.idDomini != null){
				IExpression newExpressionDomini = this.newExpression();

				this.idDomini.removeAll(Collections.singleton(null));
				VersamentoFieldConverter converter = new VersamentoFieldConverter(ConnectionManager.getJDBCServiceManagerProperties().getDatabase()); 
				CustomField cf = new CustomField("id_dominio", Long.class, "id_dominio", converter.toTable(VersamentoIncasso.model().ID_UO));
				newExpressionDomini.in(cf, this.idDomini);
				newExpressionDomini.isNotNull(VersamentoIncasso.model().ID_UO.COD_UO); //Sempre not null, solo per forzare la join
				
				newExpressionOr.and(newExpressionDomini);
			}
			
			if(this.codPagamentoPortale != null) {
				newExpressionOr.equals(VersamentoIncasso.model().ID_PAGAMENTO_PORTALE.ID_SESSIONE, this.codPagamentoPortale);
			}

			return newExpressionOr;
		} catch (ExpressionNotImplementedException e) {
			throw new ServiceException(e);
		} catch (ExpressionException e) {
			throw new ServiceException(e);
		} catch (NotImplementedException e) {
			throw new ServiceException(e);
		}
	}
	
	@Override
	public IExpression _toExpression() throws ServiceException {
		try {
			IExpression newExpression = this.newExpression();
			boolean addAnd = false;
			
			VersamentoIncassoFieldConverter converter = new VersamentoIncassoFieldConverter(ConnectionManager.getJDBCServiceManagerProperties().getDatabase()); 

			// Filtro sullo stato pagamenti
			if(this.statiVersamento != null && this.statiVersamento.size() > 0){
				List<IExpression> orStati = new ArrayList<>();
				for (StatoVersamento statoVersamento : statiVersamento) {
					switch(statoVersamento) {
					case INCASSATO:
						IExpression orStatoIncassato = this.newExpression();
						orStatoIncassato.in(VersamentoIncasso.model().STATO_VERSAMENTO, StatoVersamento.ESEGUITO.toString(),StatoVersamento.ESEGUITO_SENZA_RPT.toString());
						orStatoIncassato.and().equals(VersamentoIncasso.model().STATO_PAGAMENTO, statoVersamento.toString());
						orStati.add(orStatoIncassato);
						break;
					case ESEGUITO:
						IExpression orStatoEseguito = this.newExpression();
						orStatoEseguito.equals(VersamentoIncasso.model().STATO_VERSAMENTO, StatoVersamento.ESEGUITO.toString());
						orStatoEseguito.and().equals(VersamentoIncasso.model().STATO_PAGAMENTO, StatoPagamento.PAGATO.toString());
						orStati.add(orStatoEseguito);
						break;
					case ESEGUITO_SENZA_RPT:
						IExpression orStatoEseguitoSenzaRPT = this.newExpression();
						orStatoEseguitoSenzaRPT.equals(VersamentoIncasso.model().STATO_VERSAMENTO, StatoVersamento.ESEGUITO_SENZA_RPT.toString());
						orStatoEseguitoSenzaRPT.and().equals(VersamentoIncasso.model().STATO_PAGAMENTO, StatoPagamento.PAGATO.toString());
						orStati.add(orStatoEseguitoSenzaRPT);
						break;
					case ANNULLATO:
					case ANOMALO:
					case NON_ESEGUITO:
					case PARZIALMENTE_ESEGUITO:
					default:
						IExpression orStato = this.newExpression();
						orStato.equals(VersamentoIncasso.model().STATO_VERSAMENTO, statoVersamento.toString());
						orStati.add(orStato);
						break;
					}
				}
				
				newExpression.and().or(orStati.toArray(new IExpression[orStati.size()]));
				addAnd = true;
			}
			
			if(this.abilitaFiltroScaduto) {
				if(addAnd)
					newExpression.and();
				
				Calendar calendar = Calendar.getInstance();
				calendar.setTime(new Date());
				calendar.set(Calendar.HOUR_OF_DAY, 0);
				calendar.set(Calendar.MINUTE, 0);
				calendar.set(Calendar.SECOND, 0);
				calendar.set(Calendar.MILLISECOND, 0);
				calendar.add(Calendar.MILLISECOND, -1); // 23:59:59:999 di ieri
				
				newExpression.isNotNull(VersamentoIncasso.model().DATA_SCADENZA).and().lessEquals(VersamentoIncasso.model().DATA_SCADENZA, calendar.getTime());
				
				addAnd = true;
			}
			
			if(this.abilitaFiltroNonScaduto) {
				if(addAnd)
					newExpression.and();
				
				Calendar calendar = Calendar.getInstance();
				calendar.setTime(new Date());
				calendar.set(Calendar.HOUR_OF_DAY, 0);
				calendar.set(Calendar.MINUTE, 0);
				calendar.set(Calendar.SECOND, 0);
				calendar.set(Calendar.MILLISECOND, 0);
				
				newExpression.isNotNull(VersamentoIncasso.model().DATA_SCADENZA).and().greaterEquals(VersamentoIncasso.model().DATA_SCADENZA, calendar.getTime());
				
				addAnd = true;
			}

			if(this.dataInizio != null && this.dataFine != null) {
				if(addAnd)
					newExpression.and();

				newExpression.between(VersamentoIncasso.model().DATA_CREAZIONE, this.dataInizio,this.dataFine);
				addAnd = true;
			} else { 
				if(this.dataInizio != null) {
					if(addAnd)
						newExpression.and();
	
					newExpression.greaterThan(VersamentoIncasso.model().DATA_CREAZIONE, this.dataInizio);
					addAnd = true;
				}
				
				if(this.dataFine != null) {
					if(addAnd)
						newExpression.and();
	
					newExpression.lessThan(VersamentoIncasso.model().DATA_CREAZIONE, this.dataFine);
					addAnd = true;
				} 
			}
			
			if(this.codUnivocoDebitore != null) {
				if(addAnd)
					newExpression.and();
				newExpression.equals(VersamentoIncasso.model().SRC_DEBITORE_IDENTIFICATIVO, this.codUnivocoDebitore.toUpperCase());
				addAnd = true;
			}
			
			if(this.cfCittadino!= null) {
				if(addAnd)
					newExpression.and();
				
				newExpression.equals(VersamentoIncasso.model().SRC_DEBITORE_IDENTIFICATIVO, this.cfCittadino.toUpperCase());
				
				addAnd = true;
			}


			if(this.idVersamento != null && !this.idVersamento.isEmpty()){
				this.idVersamento.removeAll(Collections.singleton(null));
				if(addAnd)
					newExpression.and();
				CustomField cf = new CustomField("id", Long.class, "id", converter.toTable(VersamentoIncasso.model()));
				newExpression.in(cf, this.idVersamento);
				addAnd = true;
			}

			if(this.idDomini != null && !this.idDomini.isEmpty()){
				this.idDomini.removeAll(Collections.singleton(null));
				if(addAnd)
					newExpression.and();
				CustomField cf = new CustomField("id_dominio", Long.class, "id_dominio", converter.toTable(VersamentoIncasso.model()));
				newExpression.in(cf, this.idDomini);
				addAnd = true;
			}
			
			if(this.idUo != null && !this.idUo.isEmpty()){
				this.idUo.removeAll(Collections.singleton(null));
				if(addAnd)
					newExpression.and();
				
				List<IExpression> listaUoExpr = new ArrayList<IExpression>();
				for (IdUnitaOperativa uo : this.idUo) {
					IExpression orExpr = this.newExpression();
					if(uo.getIdDominio() != null) {
						CustomField cf = new CustomField("id_dominio", Long.class, "id_dominio", converter.toTable(VersamentoIncasso.model()));
						orExpr.equals(cf, uo.getIdDominio());
					}

					if(uo.getIdUnita() != null) {
						CustomField cf = new CustomField("id_uo", Long.class, "id_uo", converter.toTable(VersamentoIncasso.model()));
						orExpr.and().equals(cf, uo.getIdUnita());
					}
					
					listaUoExpr.add(orExpr);
				}
				
				newExpression.or(listaUoExpr.toArray(new IExpression[listaUoExpr.size()]));
				addAnd = true;
			}
			
			if(this.codVersamento != null){
				if(addAnd)
					newExpression.and();

				newExpression.equals(VersamentoIncasso.model().COD_VERSAMENTO_ENTE, this.codVersamento);
				addAnd = true;
			}
			
			
			if(this.codApplicazione != null){
				if(addAnd)
					newExpression.and();

				newExpression.equals(VersamentoIncasso.model().ID_APPLICAZIONE.COD_APPLICAZIONE, this.codApplicazione);
				addAnd = true;
			}
			
			if(this.codDominio != null){
				if(addAnd)
					newExpression.and();

				newExpression.equals(VersamentoIncasso.model().ID_DOMINIO.COD_DOMINIO, this.codDominio);
				addAnd = true;
			}
			
			if(this.codPagamentoPortale != null) {
				newExpression.equals(VersamentoIncasso.model().ID_PAGAMENTO_PORTALE.ID_SESSIONE, this.codPagamentoPortale);
			}

			if(this.idTipiVersamento != null && !this.idTipiVersamento.isEmpty()){
				this.idTipiVersamento.removeAll(Collections.singleton(null));
				if(addAnd)
					newExpression.and();
				CustomField cf = new CustomField("id_tipo_versamento", Long.class, "id_tipo_versamento", converter.toTable(VersamentoIncasso.model()));
				newExpression.in(cf, this.idTipiVersamento);
				addAnd = true;
			}
			
			if(this.codTipoVersamento != null){
				if(addAnd)
					newExpression.and();

				newExpression.equals(VersamentoIncasso.model().ID_TIPO_VERSAMENTO.COD_TIPO_VERSAMENTO, this.codTipoVersamento);
				addAnd = true;
			}
			
			if(this.direzione != null){
				if(addAnd)
					newExpression.and();

				newExpression.equals(VersamentoIncasso.model().DIREZIONE, this.direzione);
				addAnd = true;
			}
			
			if(this.divisione != null){
				if(addAnd)
					newExpression.and();

				newExpression.equals(VersamentoIncasso.model().DIVISIONE, this.divisione);
				addAnd = true;
			}
			
			if(this.idSessione != null){
				if(addAnd)
					newExpression.and();

				newExpression.equals(VersamentoIncasso.model().ID_SESSIONE, this.idSessione);
				addAnd = true;
			}
			
			if(this.abilitaFiltroCittadino) {
				if(addAnd)
					newExpression.and();
				
				IExpression orExpr = this.newExpression();
				orExpr.equals(VersamentoIncasso.model().TIPO, TipologiaTipoVersamento.DOVUTO.toString())
					.or().greaterThan(VersamentoIncasso.model().IMPORTO_PAGATO, 0);
				
				newExpression.and(orExpr);
				addAnd = true;
			}
			
			if(this.iuv != null){
				if(addAnd)
					newExpression.and();

				newExpression.equals(VersamentoIncasso.model().SRC_IUV, this.iuv.toUpperCase());
				addAnd = true;
			}
			
			if(this.mostraSpontaneiNonPagati != null) {
				if(!this.mostraSpontaneiNonPagati) {
					if(addAnd)
						newExpression.and();
					
					IExpression orExpr = this.newExpression();
					orExpr.equals(VersamentoIncasso.model().TIPO, TipologiaTipoVersamento.SPONTANEO.toString())
						.and().equals(VersamentoIncasso.model().STATO_VERSAMENTO, StatoVersamento.NON_ESEGUITO.toString());
					
					newExpression.and().not(orExpr);
					addAnd = true;
				}
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

	public void addSortField(SortFields field) {
		FilterSortWrapper filterSortWrapper = new FilterSortWrapper();

		switch (field) {
		case AGGIORNAMENTO_ASC:
			filterSortWrapper.setField(VersamentoIncasso.model().DATA_ORA_ULTIMO_AGGIORNAMENTO); 
			filterSortWrapper.setSortOrder(SortOrder.ASC);
			break;

		case AGGIORNAMENTO_DESC:
			filterSortWrapper.setField(VersamentoIncasso.model().DATA_ORA_ULTIMO_AGGIORNAMENTO); 
			filterSortWrapper.setSortOrder(SortOrder.DESC);
			break;

		case CARICAMENTO_ASC:
			filterSortWrapper.setField(VersamentoIncasso.model().DATA_CREAZIONE); 
			filterSortWrapper.setSortOrder(SortOrder.ASC);
			break;

		case CARICAMENTO_DESC:
			filterSortWrapper.setField(VersamentoIncasso.model().DATA_CREAZIONE); 
			filterSortWrapper.setSortOrder(SortOrder.DESC);
			break;

		case SCADENZA_ASC:
			filterSortWrapper.setField(VersamentoIncasso.model().DATA_SCADENZA); 
			filterSortWrapper.setSortOrder(SortOrder.ASC);
			break;

		case SCADENZA_DESC:
			filterSortWrapper.setField(VersamentoIncasso.model().DATA_SCADENZA); 
			filterSortWrapper.setSortOrder(SortOrder.DESC);
			break;

		case STATO_ASC:
			filterSortWrapper.setField(VersamentoIncasso.model().STATO_VERSAMENTO); 
			filterSortWrapper.setSortOrder(SortOrder.ASC);
			break;

		case STATO_DESC:
			filterSortWrapper.setField(VersamentoIncasso.model().STATO_VERSAMENTO); 
			filterSortWrapper.setSortOrder(SortOrder.DESC);
			break;
		}

		this.filterSortList.add(filterSortWrapper);
	}
	
	@Override
	public ISQLQueryObject toWhereCondition(ISQLQueryObject sqlQueryObject) throws ServiceException {
		try {
			VersamentoIncassoFieldConverter converter = new VersamentoIncassoFieldConverter(ConnectionManager.getJDBCServiceManagerProperties().getDatabase()); 
			VersamentoIncassoModel model = it.govpay.orm.VersamentoIncasso.model();
			
			boolean addTabellaTipiVersamento = false;
			boolean addTabellaDomini = false;
			boolean addTabellaApplicazioni = false;
			boolean addTabellaPagamentiPortale = false;
			
			// Filtro sullo stato pagamenti
			
			if(this.statiVersamento != null && this.statiVersamento.size() > 0){
				for (StatoVersamento statoVersamento : statiVersamento) {
					switch(statoVersamento) {
					case INCASSATO:
						sqlQueryObject.addWhereCondition(false, converter.toColumn(model.STATO_VERSAMENTO, true) + " = ? ",
								converter.toColumn(model.STATO_VERSAMENTO, true) + " = ? ");
						sqlQueryObject.addWhereCondition(true,converter.toColumn(model.STATO_PAGAMENTO, true) + " = ? ");
						break;
					case ESEGUITO:
						sqlQueryObject.addWhereCondition(true,converter.toColumn(model.STATO_VERSAMENTO, true) + " = ? ");
						sqlQueryObject.addWhereCondition(true,converter.toColumn(model.STATO_PAGAMENTO, true) + " = ? ");
						break;
					case ESEGUITO_SENZA_RPT:
						sqlQueryObject.addWhereCondition(true,converter.toColumn(model.STATO_VERSAMENTO, true) + " = ? ");
						sqlQueryObject.addWhereCondition(true,converter.toColumn(model.STATO_PAGAMENTO, true) + " = ? ");
						break;
					case ANNULLATO:
					case ANOMALO:
					case NON_ESEGUITO:
					case PARZIALMENTE_ESEGUITO:
					default:
						sqlQueryObject.addWhereCondition(true,converter.toColumn(model.STATO_VERSAMENTO, true) + " = ? ");
						break;
					}
				}
				
			}
			
			if(this.abilitaFiltroScaduto) {
				sqlQueryObject.addWhereIsNotNullCondition(converter.toColumn(model.DATA_SCADENZA, true));
				sqlQueryObject.addWhereCondition(true,converter.toColumn(model.DATA_SCADENZA, true) + " <= ? ");
			}
			
			if(this.abilitaFiltroNonScaduto) {
				sqlQueryObject.addWhereIsNotNullCondition(converter.toColumn(model.DATA_SCADENZA, true));
				sqlQueryObject.addWhereCondition(true,converter.toColumn(model.DATA_SCADENZA, true) + " >= ? ");
			}

			if(this.dataInizio != null && this.dataFine != null) {
				sqlQueryObject.addWhereCondition(true,converter.toColumn(model.DATA_CREAZIONE, true) + " >= ? ");
				sqlQueryObject.addWhereCondition(true,converter.toColumn(model.DATA_CREAZIONE, true) + " <= ? ");
			} else {
				if(this.dataInizio != null) {
					sqlQueryObject.addWhereCondition(true,converter.toColumn(model.DATA_ORA_ULTIMO_AGGIORNAMENTO, true) + " >= ? ");
				} 
				
				if(this.dataFine != null) {
					sqlQueryObject.addWhereCondition(true,converter.toColumn(model.DATA_ORA_ULTIMO_AGGIORNAMENTO, true) + " <= ? ");
				}
			}

			if(this.codUnivocoDebitore != null) {
				sqlQueryObject.addWhereCondition(true,converter.toColumn(model.SRC_DEBITORE_IDENTIFICATIVO, true) + " = ? ");
			}
			
			if(this.cfCittadino!= null) {
				sqlQueryObject.addWhereCondition(true,converter.toColumn(model.SRC_DEBITORE_IDENTIFICATIVO, true) + " = ? ");
			}

			if(this.idVersamento != null && !this.idVersamento.isEmpty()){
				this.idVersamento.removeAll(Collections.singleton(null));
				
				String [] idsVersamento = this.idVersamento.stream().map(e -> e.toString()).collect(Collectors.toList()).toArray(new String[this.idVersamento.size()]);
				sqlQueryObject.addWhereINCondition(converter.toTable(model.ID_SESSIONE, true) + ".id", false, idsVersamento );
			}

			if(this.idDomini != null && !this.idDomini.isEmpty()){
				this.idDomini.removeAll(Collections.singleton(null));
				
				String [] idsDomini = this.idDomini.stream().map(e -> e.toString()).collect(Collectors.toList()).toArray(new String[this.idDomini.size()]);
				sqlQueryObject.addWhereINCondition(converter.toTable(model.ID_SESSIONE, true) + ".id_dominio", false, idsDomini );
			}
			
			if(this.idUo != null && !this.idUo.isEmpty()){
				this.idUo.removeAll(Collections.singleton(null));
				
				List<String> listaUoExpr = new ArrayList<String>();
				for (IdUnitaOperativa uo : this.idUo) {
					String orExpr = "";
					
					if(uo.getIdDominio() != null) {
						orExpr = converter.toTable(model.ID_SESSIONE, true) + ".id_dominio" + " = ? ";
					}

					if(uo.getIdUnita() != null) {
						orExpr += " and " ;
						orExpr += converter.toTable(model.ID_SESSIONE, true) + ".id_uo" + " = ? ";
					}
					
					listaUoExpr.add(orExpr);
				}
				
				sqlQueryObject.addWhereCondition(false, listaUoExpr.toArray(new String[listaUoExpr.size()]));
			}
			
			if(this.codVersamento != null){
				sqlQueryObject.addWhereCondition(converter.toColumn(model.COD_VERSAMENTO_ENTE, true) + " = ? ");
			}
			
			if(this.codApplicazione != null){
				if(!addTabellaApplicazioni) {
					sqlQueryObject.addFromTable(converter.toTable(model.ID_APPLICAZIONE));
					sqlQueryObject.addWhereCondition(converter.toTable(model.ID_SESSIONE, true) + ".id_applicazione="
							+converter.toTable(model.ID_APPLICAZIONE, true)+".id");

					addTabellaApplicazioni = true;
				}

				sqlQueryObject.addWhereCondition(true,converter.toColumn(model.ID_APPLICAZIONE.COD_APPLICAZIONE, true) + " = ? ");
			}
			
			if(this.codDominio != null){
				if(!addTabellaDomini) {
					sqlQueryObject.addFromTable(converter.toTable(model.ID_DOMINIO));
					sqlQueryObject.addWhereCondition(converter.toTable(model.ID_SESSIONE, true) + ".id_dominio="
							+converter.toTable(model.ID_DOMINIO, true)+".id");

					addTabellaDomini = true;
				}

				sqlQueryObject.addWhereCondition(true,converter.toColumn(model.ID_DOMINIO.COD_DOMINIO, true) + " = ? ");
			}
			
			if(this.codPagamentoPortale != null) {
				if(!addTabellaPagamentiPortale) {
					String versamenti = converter.toAliasTable(Versamento.model());
					String pagPortVers = "pag_port_versamenti";
					String pagPort = converter.toAliasTable(Versamento.model().ID_PAGAMENTO_PORTALE);
					
					sqlQueryObject.addFromTable(pagPortVers);
					sqlQueryObject.addWhereCondition(versamenti+".id="+pagPortVers+".id_versamento");
					sqlQueryObject.addWhereCondition(pagPortVers+".id_pagamento_portale="+pagPort+".id");
					sqlQueryObject.addFromTable(converter.toTable(model.ID_PAGAMENTO_PORTALE));
					addTabellaPagamentiPortale = true;
				}
				
				sqlQueryObject.addWhereCondition(true,converter.toColumn(model.ID_PAGAMENTO_PORTALE.ID_SESSIONE, true) + " = ? ");
			}

			if(this.idTipiVersamento != null && !this.idTipiVersamento.isEmpty()){
				this.idTipiVersamento.removeAll(Collections.singleton(null));
				
				String [] idsTipiVersamento = this.idTipiVersamento.stream().map(e -> e.toString()).collect(Collectors.toList()).toArray(new String[this.idTipiVersamento.size()]);
				sqlQueryObject.addWhereINCondition(converter.toTable(model.ID_SESSIONE, true) + ".id_tipo_versamento", false, idsTipiVersamento );
			}
			
			if(this.codTipoVersamento != null){
				if(!addTabellaTipiVersamento) {
					sqlQueryObject.addFromTable(converter.toTable(model.ID_TIPO_VERSAMENTO));
					sqlQueryObject.addWhereCondition(converter.toTable(model.ID_SESSIONE, true) + ".id_tipo_versamento="
							+converter.toTable(model.ID_TIPO_VERSAMENTO, true)+".id");

					addTabellaTipiVersamento = true;
				}
				
				sqlQueryObject.addWhereCondition(true,converter.toColumn(model.ID_TIPO_VERSAMENTO.COD_TIPO_VERSAMENTO, true) + " = ? ");
			}
			
			if(this.direzione != null){
				sqlQueryObject.addWhereCondition(true,converter.toColumn(model.DIREZIONE, true) + " = ? ");
			}
			
			if(this.divisione != null){
				sqlQueryObject.addWhereCondition(true,converter.toColumn(model.DIVISIONE, true) + " = ? ");
			}
			
			if(this.idSessione != null){
				sqlQueryObject.addWhereCondition(true,converter.toColumn(model.ID_SESSIONE, true) + " = ? ");
			}
			
			if(this.iuv != null){
				sqlQueryObject.addWhereCondition(true,converter.toColumn(model.SRC_IUV, true) + " = ? ");
			}
			
			if(this.abilitaFiltroCittadino) {
				sqlQueryObject.addWhereCondition(false, converter.toColumn(model.TIPO, true)  + " = ? ",
						converter.toColumn(model.IMPORTO_PAGATO, true) + " >= ? ");
			}
			
			if(this.mostraSpontaneiNonPagati != null) {
				if(!this.mostraSpontaneiNonPagati) {
					sqlQueryObject.addWhereCondition(true, true, converter.toColumn(model.TIPO, true)  + " = ? ",
							converter.toColumn(model.STATO_VERSAMENTO, true) + " = ? ");
				}
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
		
		// Filtro sullo stato pagamenti
		if(this.statiVersamento != null && this.statiVersamento.size() > 0){
			for (StatoVersamento statoVersamento : statiVersamento) {
				switch(statoVersamento) {
				case INCASSATO:
					lst.add(StatoVersamento.ESEGUITO.toString());
					lst.add(StatoVersamento.ESEGUITO_SENZA_RPT.toString());
					lst.add(statoVersamento.toString());
					break;
				case ESEGUITO:
					lst.add(StatoVersamento.ESEGUITO.toString());
					lst.add(StatoPagamento.PAGATO.toString());
					break;
				case ESEGUITO_SENZA_RPT:
					lst.add(StatoVersamento.ESEGUITO_SENZA_RPT.toString());
					lst.add(StatoPagamento.PAGATO.toString());
					break;
				case ANNULLATO:
				case ANOMALO:
				case NON_ESEGUITO:
				case PARZIALMENTE_ESEGUITO:
				default:
					lst.add(statoVersamento.toString());
					break;
				}
			}
			
		}
		
		if(this.abilitaFiltroScaduto) {
			Calendar calendar = Calendar.getInstance();
			calendar.setTime(new Date());
			calendar.set(Calendar.HOUR_OF_DAY, 0);
			calendar.set(Calendar.MINUTE, 0);
			calendar.set(Calendar.SECOND, 0);
			calendar.set(Calendar.MILLISECOND, 0);
			calendar.add(Calendar.MILLISECOND, -1); // 23:59:59:999 di ieri
			
			lst.add(calendar.getTime());
		}
		
		if(this.abilitaFiltroNonScaduto) {
			Calendar calendar = Calendar.getInstance();
			calendar.setTime(new Date());
			calendar.set(Calendar.HOUR_OF_DAY, 0);
			calendar.set(Calendar.MINUTE, 0);
			calendar.set(Calendar.SECOND, 0);
			calendar.set(Calendar.MILLISECOND, 0);
			
			lst.add(calendar.getTime());
		}

		if(this.dataInizio != null && this.dataFine != null) {
			lst.add(this.dataInizio);
			lst.add(this.dataFine);
		} else {
			if(this.dataInizio != null) {
				lst.add(this.dataInizio);
			} 
			
			if(this.dataFine != null) {
				lst.add(this.dataFine);
			}
		}

		if(this.codUnivocoDebitore != null) {
			lst.add(this.codUnivocoDebitore.toUpperCase());
		}
		
		if(this.cfCittadino!= null) {
			lst.add(this.cfCittadino.toUpperCase());
		}

		if(this.idVersamento != null && !this.idVersamento.isEmpty()){
			// donothing
		}

		if(this.idDomini != null && !this.idDomini.isEmpty()){
			// donothing	
		}
		
		if(this.idUo != null && !this.idUo.isEmpty()){
			for (IdUnitaOperativa uo : this.idUo) {
				if(uo.getIdDominio() != null) {
					lst.add(uo.getIdDominio());
				}

				if(uo.getIdUnita() != null) {
					lst.add(uo.getIdUnita());
				}
			}
		}

		if(this.codVersamento != null){
			lst.add(this.codVersamento);
		}
		
		if(this.codApplicazione != null){
			lst.add(this.codApplicazione);
		}
		
		if(this.codDominio != null){
			lst.add(this.codDominio);
		}
		
		if(this.codPagamentoPortale != null) {
			lst.add(this.codPagamentoPortale);
		}

		if(this.idTipiVersamento != null && !this.idTipiVersamento.isEmpty()){
			// donothing	
		}
		
		if(this.codTipoVersamento != null){
			lst.add(this.codTipoVersamento);
		}
		
		if(this.direzione != null){
			lst.add(this.direzione);
		}
		
		if(this.divisione != null){
			lst.add(this.divisione);
		}
		
		if(this.idSessione != null){
			lst.add(this.idSessione);
		}
		
		if(this.iuv != null){
			lst.add(this.iuv.toUpperCase());
		}
		
		if(this.abilitaFiltroCittadino) {
			lst.add(TipologiaTipoVersamento.DOVUTO.toString());
			lst.add(0);
		}
		
		if(this.mostraSpontaneiNonPagati != null) {
			if(!this.mostraSpontaneiNonPagati) {
				lst.add(TipologiaTipoVersamento.SPONTANEO.toString());
				lst.add(StatoVersamento.NON_ESEGUITO.toString());
			}
		}
		
		return lst.toArray(new Object[lst.size()]);
	}

	public List<StatoVersamento> getStatiVersamento() {
		return this.statiVersamento;
	}

	public void setStatiPagamento(List<StatoVersamento> statiVersamento) {
		this.statiVersamento = statiVersamento;
	}

	public String getCodUnivocoDebitore() {
		return this.codUnivocoDebitore;
	}

	public void setCodUnivocoDebitore(String codUnivocoDebitore) {
		this.codUnivocoDebitore = codUnivocoDebitore;
	}

	public List<Long> getIdVersamento() {
		return this.idVersamento;
	}

	public void setIdVersamento(List<Long> idVersamento) {
		this.idVersamento = idVersamento;
	}

	public String getCodVersamento() {
		return this.codVersamento;
	}

	public void setCodVersamento(String codVersamento) {
		this.codVersamento = codVersamento;
	}

	public List<Long> getIdDomini() {
		return this.idDomini;
	}

	public void setIdDomini(List<Long> idDomini) {
		this.idDomini = idDomini;
	}

	public void setStatoVersamento(StatoVersamento stato) {
		this.statiVersamento = new ArrayList<>();
		if(stato != null) {
			this.statiVersamento.add(stato);
		}
	}

	public Long getIdPagamentoPortale() {
		return this.idPagamentoPortale;
	}

	public void setIdPagamentoPortale(Long idPagamentoPortale) {
		this.idPagamentoPortale = idPagamentoPortale;
	}

	public String getCodPagamentoPortale() {
		return this.codPagamentoPortale;
	}

	public void setCodPagamentoPortale(String codPagamentoPortale) {
		this.codPagamentoPortale = codPagamentoPortale;
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

	public String getCodApplicazione() {
		return this.codApplicazione;
	}

	public void setCodApplicazione(String codApplicazione) {
		this.codApplicazione = codApplicazione;
	}

	public String getCodDominio() {
		return this.codDominio;
	}

	public void setCodDominio(String codDominio) {
		this.codDominio = codDominio;
	}

	public String getCfCittadino() {
		return cfCittadino;
	}

	public void setCfCittadino(String cfCittadino) {
		this.cfCittadino = cfCittadino;
	}
	
	public List<Long> getIdTipiVersamento() {
		return idTipiVersamento;
	}

	public void setIdTipiVersamento(List<Long> idTipiVersamento) {
		this.idTipiVersamento = idTipiVersamento;
	}

	public String getCodTipoVersamento() {
		return codTipoVersamento;
	}

	public void setCodTipoVersamento(String codTipoVersamento) {
		this.codTipoVersamento = codTipoVersamento;
	}

	public boolean isAbilitaFiltroCittadino() {
		return abilitaFiltroCittadino;
	}

	public void setAbilitaFiltroCittadino(boolean abilitaFiltroCittadino) {
		this.abilitaFiltroCittadino = abilitaFiltroCittadino;
	}

	public String getDivisione() {
		return divisione;
	}

	public void setDivisione(String divisione) {
		this.divisione = divisione;
	}

	public String getDirezione() {
		return direzione;
	}

	public void setDirezione(String direzione) {
		this.direzione = direzione;
	}

	public String getIdSessione() {
		return idSessione;
	}

	public void setIdSessione(String idSessione) {
		this.idSessione = idSessione;
        }

	public List<IdUnitaOperativa> getIdUo() {
		return idUo;
	}

	public void setIdUo(List<IdUnitaOperativa> idUo) {
		this.idUo = idUo;
	}

	public String getIuv() {
		return iuv;
	}

	public void setIuv(String iuv) {
		this.iuv = iuv;
	}

	public boolean isAbilitaFiltroNonScaduto() {
		return abilitaFiltroNonScaduto;
	}

	public void setAbilitaFiltroNonScaduto(boolean abilitaFiltroNonScaduto) {
		this.abilitaFiltroNonScaduto = abilitaFiltroNonScaduto;
	}

	public boolean isAbilitaFiltroScaduto() {
		return abilitaFiltroScaduto;
	}

	public void setAbilitaFiltroScaduto(boolean abilitaFiltroScaduto) {
		this.abilitaFiltroScaduto = abilitaFiltroScaduto;
	}

	public Boolean getMostraSpontaneiNonPagati() {
		return mostraSpontaneiNonPagati;
	}

	public void setMostraSpontaneiNonPagati(Boolean mostraSpontaneiNonPagati) {
		this.mostraSpontaneiNonPagati = mostraSpontaneiNonPagati;
	}
	
}
