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

import org.openspcoop2.generic_project.beans.CustomField;
import org.openspcoop2.generic_project.dao.IExpressionConstructor;
import org.openspcoop2.generic_project.exception.ExpressionException;
import org.openspcoop2.generic_project.exception.ExpressionNotImplementedException;
import org.openspcoop2.generic_project.exception.NotImplementedException;
import org.openspcoop2.generic_project.exception.ServiceException;
import org.openspcoop2.generic_project.expression.IExpression;
import org.openspcoop2.generic_project.expression.LikeMode;
import org.openspcoop2.generic_project.expression.SortOrder;

import it.govpay.bd.AbstractFilter;
import it.govpay.bd.ConnectionManager;
import it.govpay.bd.FilterSortWrapper;
import it.govpay.model.Versamento.StatoVersamento;
import it.govpay.orm.Versamento;
import it.govpay.orm.dao.jdbc.converter.VersamentoFieldConverter;

public class VersamentoFilter extends AbstractFilter {

	private List<StatoVersamento> statiVersamento =  null;
	private String codUnivocoDebitore;
	private List<Long> idDomini;
	private List<Long> idVersamento= null;
	private String codVersamento = null;
	private List<String> codVersamentoEnte = null;
	private List<Long> idApplicazione = null;
	private Long idPagamentoPortale = null;
	private String codPagamentoPortale = null;
	private Date dataInizio;
	private Date dataFine;
	private String codApplicazione = null;
	private String codDominio = null;
	private Long idTracciato; 
	private Boolean tracciatoNull; 
	private Boolean avvisaturaDainviare; 
	private String cfCittadino;
	private String modoAvvisatura = null;
	private Boolean avvisaturaAbilitata;
	private List<Long> idTipiVersamento = null;
	private String codTipoVersamento = null;
	
	public enum SortFields {
		STATO_ASC, STATO_DESC, SCADENZA_ASC, SCADENZA_DESC, AGGIORNAMENTO_ASC, AGGIORNAMENTO_DESC, CARICAMENTO_ASC, CARICAMENTO_DESC
	}

	public VersamentoFilter(IExpressionConstructor expressionConstructor) {
		this(expressionConstructor,false);
	}
	
	public VersamentoFilter(IExpressionConstructor expressionConstructor, boolean simpleSearch) {
		super(expressionConstructor, simpleSearch);
		this.listaFieldSimpleSearch.add(Versamento.model().DEBITORE_IDENTIFICATIVO);
		this.listaFieldSimpleSearch.add(Versamento.model().COD_VERSAMENTO_ENTE);
		this.listaFieldSimpleSearch.add(Versamento.model().IUV.IUV);
	}

	@Override
	public IExpression _toSimpleSearchExpression() throws ServiceException {
		try {
			IExpression newExpressionOr = super._toSimpleSearchExpression();
			
			if(this.idDomini != null){
				IExpression newExpressionDomini = this.newExpression();

				this.idDomini.removeAll(Collections.singleton(null));
				VersamentoFieldConverter converter = new VersamentoFieldConverter(ConnectionManager.getJDBCServiceManagerProperties().getDatabase()); 
				CustomField cf = new CustomField("id_dominio", Long.class, "id_dominio", converter.toTable(Versamento.model().ID_UO));
				newExpressionDomini.in(cf, this.idDomini);
				newExpressionDomini.isNotNull(Versamento.model().ID_UO.COD_UO); //Sempre not null, solo per forzare la join
				
				newExpressionOr.and(newExpressionDomini);
			}
			
			if(this.codPagamentoPortale != null) {
				newExpressionOr.equals(Versamento.model().ID_PAGAMENTO_PORTALE.ID_SESSIONE, this.codPagamentoPortale);
			}
			
//			if(this.idApplicazione!= null && this.idApplicazione.size() > 0 && this.codVersamentoEnte!= null && this.codVersamentoEnte.size() > 0) {
//				if(this.idApplicazione.size() == this.codVersamentoEnte.size()){
//					IExpression orExpr = this.newExpression();
//					List<IExpression> lstOrExpr = new ArrayList<IExpression>();
//					
//					VersamentoFieldConverter converter = new VersamentoFieldConverter(ConnectionManager.getJDBCServiceManagerProperties().getDatabase()); 
//					CustomField cf = new CustomField("id_applicazione", Long.class, "id_applicazione", converter.toTable(Versamento.model()));
//					
//					for (int i = 0; i < this.codVersamentoEnte.size(); i++) {
//						String codV = this.codVersamentoEnte.get(i);
//						Long idApp = this.idApplicazione.get(i);
//						
//						IExpression vExpr = this.newExpression();
//						vExpr.equals(Versamento.model().COD_VERSAMENTO_ENTE, codV).and().equals(cf, idApp);
//						
//						lstOrExpr.add(vExpr);
//					}
//					
//					orExpr.or(lstOrExpr.toArray(new IExpression[lstOrExpr.size()]));
//					
//					newExpressionOr.or(orExpr);
//				}
//			}

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
			
			VersamentoFieldConverter converter = new VersamentoFieldConverter(ConnectionManager.getJDBCServiceManagerProperties().getDatabase()); 

			// Filtro sullo stato pagamenti
			if(this.statiVersamento != null && this.statiVersamento.size() > 0){
				newExpression.in(Versamento.model().STATO_VERSAMENTO, this.toString(this.statiVersamento));
				addAnd = true;
			}

			if(this.dataInizio != null && this.dataFine != null) {
				if(addAnd)
					newExpression.and();

				newExpression.between(Versamento.model().DATA_ORA_ULTIMO_AGGIORNAMENTO, this.dataInizio,this.dataFine);
				addAnd = true;
			} else {
				if(this.dataInizio != null) {
					if(addAnd)
						newExpression.and();
	
					newExpression.greaterEquals(Versamento.model().DATA_ORA_ULTIMO_AGGIORNAMENTO, this.dataInizio);
					addAnd = true;
				} 
				
				if(this.dataFine != null) {
					if(addAnd)
						newExpression.and();
	
					newExpression.lessEquals(Versamento.model().DATA_ORA_ULTIMO_AGGIORNAMENTO, this.dataFine);
					addAnd = true;
				}
			}

			if(this.codUnivocoDebitore != null) {
				if(addAnd)
					newExpression.and();
				newExpression.ilike(Versamento.model().DEBITORE_IDENTIFICATIVO, this.codUnivocoDebitore,LikeMode.ANYWHERE);
				addAnd = true;
			}
			
			if(this.cfCittadino!= null) {
				if(addAnd)
					newExpression.and();
				
				newExpression.equals(Versamento.model().DEBITORE_IDENTIFICATIVO, this.cfCittadino);
				
				addAnd = true;
			}

			if(this.idVersamento != null && !this.idVersamento.isEmpty()){
				if(addAnd)
					newExpression.and();
				CustomField cf = new CustomField("id", Long.class, "id", converter.toTable(Versamento.model()));
				newExpression.in(cf, this.idVersamento);
				addAnd = true;
			}

//			if(this.idDomini != null){
//				idDomini.removeAll(Collections.singleton(null));
//				if(addAnd)
//					newExpression.and();
//				VersamentoFieldConverter converter = new VersamentoFieldConverter(ConnectionManager.getJDBCServiceManagerProperties().getDatabase()); 
//				CustomField cf = new CustomField("id_dominio", Long.class, "id_dominio", converter.toTable(Versamento.model().ID_UO));
//				newExpression.in(cf, this.idDomini);
//				newExpression.isNotNull(Versamento.model().ID_UO.COD_UO); //Sempre not null, solo per forzare la join
//				addAnd = true;
//			}
			
			if(this.idDomini != null){
				this.idDomini.removeAll(Collections.singleton(null));
				if(addAnd)
					newExpression.and();
				CustomField cf = new CustomField("id_dominio", Long.class, "id_dominio", converter.toTable(Versamento.model()));
				newExpression.in(cf, this.idDomini);
				addAnd = true;
			}


			if(this.codVersamento != null){
				if(addAnd)
					newExpression.and();

				newExpression.ilike(Versamento.model().COD_VERSAMENTO_ENTE, this.codVersamento, LikeMode.ANYWHERE);
				addAnd = true;
			}
			
			
			if(this.idApplicazione!= null && this.idApplicazione.size() > 0 && this.codVersamentoEnte!= null && this.codVersamentoEnte.size() > 0) {
				if(this.idApplicazione.size() == this.codVersamentoEnte.size()){
					if(addAnd)
						newExpression.and();

					IExpression orExpr = this.newExpression();
					List<IExpression> lstOrExpr = new ArrayList<>();
					
					CustomField cf = new CustomField("id_applicazione", Long.class, "id_applicazione", converter.toTable(Versamento.model()));
					
					for (int i = 0; i < this.codVersamentoEnte.size(); i++) {
						String codV = this.codVersamentoEnte.get(i);
						Long idApp = this.idApplicazione.get(i);
						
						IExpression vExpr = this.newExpression();
						vExpr.equals(Versamento.model().COD_VERSAMENTO_ENTE, codV).and().equals(cf, idApp);
						
						lstOrExpr.add(vExpr);
					}
					
					orExpr.or(lstOrExpr.toArray(new IExpression[lstOrExpr.size()]));
					
					newExpression.and(orExpr);
					
					addAnd = true;
				}
			}
			
			if(this.codApplicazione != null){
				if(addAnd)
					newExpression.and();

				newExpression.equals(Versamento.model().ID_APPLICAZIONE.COD_APPLICAZIONE, this.codApplicazione);
				addAnd = true;
			}
			
			if(this.codDominio != null){
				if(addAnd)
					newExpression.and();

				newExpression.equals(Versamento.model().ID_DOMINIO.COD_DOMINIO, this.codDominio);
				addAnd = true;
			}
			
			if(this.codPagamentoPortale != null) {
				newExpression.equals(Versamento.model().ID_PAGAMENTO_PORTALE.ID_SESSIONE, this.codPagamentoPortale);
			}

			if(this.idTracciato != null) {
				CustomField cf = new CustomField("id_tracciato", Long.class, "id_tracciato", converter.toTable(Versamento.model()));
				newExpression.equals(cf, this.idTracciato);
			}
			
			if(this.tracciatoNull!=null) {
				CustomField cf = new CustomField("id_tracciato", Long.class, "id_tracciato", converter.toTable(Versamento.model()));
				if(this.tracciatoNull) {
					newExpression.isNull(cf);
				} else {
					newExpression.isNotNull(cf);	
				}
			}
			
			if(this.avvisaturaDainviare!=null) {
				newExpression.equals(Versamento.model().AVVISATURA_DA_INVIARE, this.avvisaturaDainviare);
			}
			
			if(this.modoAvvisatura!=null) {
				newExpression.equals(Versamento.model().AVVISATURA_MODALITA, this.modoAvvisatura);
			}
			
			if(this.avvisaturaAbilitata!=null) {
				newExpression.equals(Versamento.model().AVVISATURA_ABILITATA, this.avvisaturaAbilitata);
			}
			
			if(this.idTipiVersamento != null){
				this.idTipiVersamento.removeAll(Collections.singleton(null));
				if(addAnd)
					newExpression.and();
				CustomField cf = new CustomField("id_tipo_versamento", Long.class, "id_tipo_versamento", converter.toTable(Versamento.model()));
				newExpression.in(cf, this.idTipiVersamento);
				addAnd = true;
			}
			
			if(this.codTipoVersamento != null){
				if(addAnd)
					newExpression.and();

				newExpression.ilike(Versamento.model().ID_TIPO_VERSAMENTO.COD_TIPO_VERSAMENTO, this.codTipoVersamento, LikeMode.ANYWHERE);
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

	private List<String> toString(List<StatoVersamento> statiVersamento) {
		List<String> stati = new ArrayList<>();
		for(StatoVersamento stato : statiVersamento)
			stati.add(stato.toString());
		return stati;
	}	

	public void addSortField(SortFields field) {
		FilterSortWrapper filterSortWrapper = new FilterSortWrapper();

		switch (field) {
		case AGGIORNAMENTO_ASC:
			filterSortWrapper.setField(Versamento.model().DATA_ORA_ULTIMO_AGGIORNAMENTO); 
			filterSortWrapper.setSortOrder(SortOrder.ASC);
			break;

		case AGGIORNAMENTO_DESC:
			filterSortWrapper.setField(Versamento.model().DATA_ORA_ULTIMO_AGGIORNAMENTO); 
			filterSortWrapper.setSortOrder(SortOrder.DESC);
			break;

		case CARICAMENTO_ASC:
			filterSortWrapper.setField(Versamento.model().DATA_CREAZIONE); 
			filterSortWrapper.setSortOrder(SortOrder.ASC);
			break;

		case CARICAMENTO_DESC:
			filterSortWrapper.setField(Versamento.model().DATA_CREAZIONE); 
			filterSortWrapper.setSortOrder(SortOrder.DESC);
			break;

		case SCADENZA_ASC:
			filterSortWrapper.setField(Versamento.model().DATA_SCADENZA); 
			filterSortWrapper.setSortOrder(SortOrder.ASC);
			break;

		case SCADENZA_DESC:
			filterSortWrapper.setField(Versamento.model().DATA_SCADENZA); 
			filterSortWrapper.setSortOrder(SortOrder.DESC);
			break;

		case STATO_ASC:
			filterSortWrapper.setField(Versamento.model().STATO_VERSAMENTO); 
			filterSortWrapper.setSortOrder(SortOrder.ASC);
			break;

		case STATO_DESC:
			filterSortWrapper.setField(Versamento.model().STATO_VERSAMENTO); 
			filterSortWrapper.setSortOrder(SortOrder.DESC);
			break;
		}

		this.filterSortList.add(filterSortWrapper);
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

	public List<String> getCodVersamentoEnte() {
		return this.codVersamentoEnte;
	}

	public void setCodVersamentoEnte(List<String> codVersamentoEnte) {
		this.codVersamentoEnte = codVersamentoEnte;
	}

	public List<Long> getIdApplicazione() {
		return this.idApplicazione;
	}

	public void setIdApplicazione(List<Long> idApplicazione) {
		this.idApplicazione = idApplicazione;
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

	public Long getIdTracciato() {
		return this.idTracciato;
	}

	public void setIdTracciato(Long idTracciato) {
		this.idTracciato = idTracciato;
	}

	public Boolean getTracciatoNull() {
		return this.tracciatoNull;
	}

	public void setTracciatoNull(Boolean tracciatoNull) {
		this.tracciatoNull = tracciatoNull;
	}

	public String getCfCittadino() {
		return cfCittadino;
	}

	public void setCfCittadino(String cfCittadino) {
		this.cfCittadino = cfCittadino;
	}

	public Boolean getAvvisaturaDainviare() {
		return avvisaturaDainviare;
	}

	public void setAvvisaturaDainviare(Boolean avvisaturaDainviare) {
		this.avvisaturaDainviare = avvisaturaDainviare;
	}

	public String getModoAvvisatura() {
		return modoAvvisatura;
	}

	public void setModoAvvisatura(String modoAvvisatura) {
		this.modoAvvisatura = modoAvvisatura;
	}

	public Boolean getAvvisaturaAbilitata() {
		return avvisaturaAbilitata;
	}

	public void setAvvisaturaAbilitata(Boolean avvisaturaAbilitata) {
		this.avvisaturaAbilitata = avvisaturaAbilitata;
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
	
}
