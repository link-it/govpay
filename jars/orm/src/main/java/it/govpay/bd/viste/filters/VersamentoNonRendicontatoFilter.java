package it.govpay.bd.viste.filters;

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
import it.govpay.bd.model.IdUnitaOperativa;
import it.govpay.orm.VistaVersamentoNonRendicontato;
import it.govpay.orm.dao.jdbc.converter.VistaVersamentoNonRendicontatoFieldConverter;
import it.govpay.orm.model.VistaVersamentoNonRendicontatoModel;

public class VersamentoNonRendicontatoFilter extends AbstractFilter {
	
	private Date dataPagamentoDa;
	private Date dataPagamentoA;
	private String iuv;
	private List<String> direzione;
	private List<String> divisione;
	
	// Interni alla tabella
	private String iur;
	private Long idPagamento;
	private Integer indiceDati;
	
	// Esterni alla tabella
	private String codDominio; // fr 
	private String tnr;
	
	private String codSingoloVersamentoEnte = null; // SV
	
	private Long idApplicazione; // versamenti
	private String codUnivocoDebitore;
	private String codVersamento = null;
	private List<Long> idVersamento= null;
	private List<Long> idTipiVersamento = null;
	
	private boolean ricercaIdFlussoCaseInsensitive = false;
	
	private Boolean incassato;
	private boolean searchModeEquals = true; 
	private List<IdUnitaOperativa> dominiUOAutorizzati;
	
	private VistaVersamentoNonRendicontatoFieldConverter converter = null;

	public VersamentoNonRendicontatoFilter(IExpressionConstructor expressionConstructor) throws ServiceException {
		this(expressionConstructor,false);
	}
	
	public VersamentoNonRendicontatoFilter(IExpressionConstructor expressionConstructor, boolean simpleSearch) throws ServiceException {
		super(expressionConstructor, simpleSearch);
		this.listaFieldSimpleSearch.add(VistaVersamentoNonRendicontato.model().PAG_IUV);
		this.listaFieldSimpleSearch.add(VistaVersamentoNonRendicontato.model().PAG_IUR);
		this.listaFieldSimpleSearch.add(VistaVersamentoNonRendicontato.model().VRS_ID_DOMINIO.COD_DOMINIO);
		this.converter = new VistaVersamentoNonRendicontatoFieldConverter(ConnectionManager.getJDBCServiceManagerProperties().getDatabase()); 
	}
	
	@Override
	public IExpression _toSimpleSearchExpression() throws ServiceException {
		try {
			IExpression newExpression = super._toSimpleSearchExpression();

			if(this.idPagamento != null) {
				IExpression newExpressionFr = this.newExpression();
				
				CustomField idFrField = new CustomField("id", Long.class, "id", this.getRootTable());
				newExpressionFr.equals(idFrField, this.idPagamento);
				newExpression.and(newExpressionFr);
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
	public IExpression _toExpression() throws ServiceException {
		try {
			IExpression newExpression = this.newExpression();
			boolean addAnd = false;
			
			// Rendicontazione
			
			if(this.iuv != null) {
				if(addAnd)
					newExpression.and();
				
				newExpression.ilike(VistaVersamentoNonRendicontato.model().PAG_IUV, this.iuv,LikeMode.ANYWHERE);
				addAnd = true;
			}

			if(this.iur != null) {
				if(addAnd)
					newExpression.and();
				
				newExpression.equals(VistaVersamentoNonRendicontato.model().PAG_IUR, this.iur);
				addAnd = true;
			}
			
			if(this.indiceDati != null) {
				if(addAnd)
					newExpression.and();
				
				newExpression.equals(VistaVersamentoNonRendicontato.model().PAG_INDICE_DATI, this.indiceDati);
				addAnd = true;
			}

			if(this.idPagamento != null) {
				if(addAnd)
					newExpression.and();
				
				CustomField idPagamentoField = new CustomField(ALIAS_ID, Long.class, ALIAS_ID, converter.toTable(VistaVersamentoNonRendicontato.model()));
				newExpression.equals(idPagamentoField, this.idPagamento);
				addAnd = true;
			}
			
			if(this.dataPagamentoDa != null && this.dataPagamentoA != null) {
				if(addAnd)
					newExpression.and();

				newExpression.between(VistaVersamentoNonRendicontato.model().PAG_DATA_PAGAMENTO, this.dataPagamentoDa,this.dataPagamentoA);
				addAnd = true;
			} else {
				if(this.dataPagamentoDa != null) {
					if(addAnd)
						newExpression.and();

					newExpression.greaterEquals(VistaVersamentoNonRendicontato.model().PAG_DATA_PAGAMENTO, this.dataPagamentoDa);
					addAnd = true;
				} 

				if(this.dataPagamentoA != null) {
					if(addAnd)
						newExpression.and();

					newExpression.lessEquals(VistaVersamentoNonRendicontato.model().PAG_DATA_PAGAMENTO, this.dataPagamentoA);
					addAnd = true;
				}
			}
			
			if(this.codDominio != null) {
				if(addAnd)
					newExpression.and();
				
				newExpression.equals(VistaVersamentoNonRendicontato.model().VRS_ID_DOMINIO.COD_DOMINIO, this.codDominio);
				addAnd = true;
			}
			
			if(this.tnr != null) {
				if(addAnd)
					newExpression.and();
				
				newExpression.equals(VistaVersamentoNonRendicontato.model().RNC_TRN, this.tnr);
				addAnd = true;
			}
			
			// SV
			
			if(this.codSingoloVersamentoEnte != null){
				newExpression.ilike(VistaVersamentoNonRendicontato.model().SNG_COD_SING_VERS_ENTE, this.codSingoloVersamentoEnte, LikeMode.ANYWHERE);
				addAnd = true;
			}
			
			// VERSAMENTI
			
			if(this.dominiUOAutorizzati != null && this.dominiUOAutorizzati.size() > 0) {
				if(addAnd)
					newExpression.and();
				
				IExpression newExpressionUO = this.newExpression();
				List<IExpression> listExpressionSingolaUO = new ArrayList<>();
				
				for (IdUnitaOperativa idUnita : this.dominiUOAutorizzati) {
					if(idUnita.getIdDominio() != null) {
						IExpression newExpressionSingolaUO = this.newExpression();
						
						CustomField idDominioCustomField = new CustomField("vrs_id_dominio", Long.class, "vrs_id_dominio", converter.toTable(VistaVersamentoNonRendicontato.model()));
						newExpressionSingolaUO.equals(idDominioCustomField, idUnita.getIdDominio());
						
						if(idUnita.getIdUnita() != null ) {
							CustomField iduoCustomField = new CustomField("vrs_id_uo", Long.class, "vrs_id_uo", converter.toTable(VistaVersamentoNonRendicontato.model()));
							newExpressionSingolaUO.and().equals(iduoCustomField, idUnita.getIdUnita());
						}
						
						listExpressionSingolaUO.add(newExpressionSingolaUO);
					}
				}
				
				newExpressionUO.or(listExpressionSingolaUO.toArray(new IExpression[listExpressionSingolaUO.size()]));
				newExpression.and(newExpressionUO);
				addAnd = true;
			}
			
			if(this.idTipiVersamento != null && !this.idTipiVersamento.isEmpty()){
				this.idTipiVersamento.removeAll(Collections.singleton(null));
				if(addAnd)
					newExpression.and();
				CustomField cf = new CustomField("vrs_id_tipo_versamento", Long.class, "vrs_id_tipo_versamento", converter.toTable(VistaVersamentoNonRendicontato.model()));
				newExpression.in(cf, this.idTipiVersamento);
				addAnd = true;
			}

			if(this.codVersamento != null){
				if(addAnd)
					newExpression.and();

				newExpression.ilike(VistaVersamentoNonRendicontato.model().VRS_COD_VERSAMENTO_ENTE, this.codVersamento, LikeMode.ANYWHERE);
				addAnd = true;
			}

			if(this.idApplicazione != null) {
				if(addAnd)
					newExpression.and();
				
				CustomField idApplicazioneField = new CustomField("vrs_id_applicazione", Long.class, "vrs_id_applicazione", converter.toTable(VistaVersamentoNonRendicontato.model()));
				newExpression.equals(idApplicazioneField, this.idApplicazione);
				addAnd = true;
			}
			
			if(this.codUnivocoDebitore != null) { 
				if(addAnd)
					newExpression.and();
				newExpression.ilike(VistaVersamentoNonRendicontato.model().VRS_DEBITORE_IDENTIFICATIVO, this.codUnivocoDebitore,LikeMode.ANYWHERE);
				addAnd = true;
			}
			
			if(this.idVersamento != null && !this.idVersamento.isEmpty()){
				this.idVersamento.removeAll(Collections.singleton(null));
				if(addAnd)
					newExpression.and();
				CustomField cf = new CustomField("vrs_id", Long.class, "vrs_id", converter.toTable(VistaVersamentoNonRendicontato.model()));
				newExpression.in(cf, this.idVersamento);
				addAnd = true;
			}
			
			if(this.direzione != null && this.direzione.size() > 0){
				this.direzione.removeAll(Collections.singleton(null));
				if(addAnd)
					newExpression.and();

				newExpression.in(VistaVersamentoNonRendicontato.model().VRS_DIREZIONE, this.direzione);
				addAnd = true;
			}

			if(this.divisione != null && this.divisione.size() > 0){
				this.divisione.removeAll(Collections.singleton(null));
				if(addAnd)
					newExpression.and();

				newExpression.in(VistaVersamentoNonRendicontato.model().VRS_DIVISIONE, this.divisione);
				addAnd = true;
			}
			
			return newExpression;
			
			
		} catch(NotImplementedException e) {
			throw new ServiceException(e);
		} catch (ExpressionNotImplementedException e) {
			throw new ServiceException(e);
		} catch (ExpressionException e) {
			throw new ServiceException(e);
		}
	}
	
	@Override
	public ISQLQueryObject toWhereCondition(ISQLQueryObject sqlQueryObject) throws ServiceException {
		try {
			VistaVersamentoNonRendicontatoModel model = it.govpay.orm.VistaVersamentoNonRendicontato.model();
			
			boolean addTabellaDomini = false;
			
			if(this.iuv != null) {
				sqlQueryObject.addWhereLikeCondition(converter.toColumn(model.PAG_IUV, true), this.iuv, true, true);
			}

			if(this.iur != null) {
				sqlQueryObject.addWhereCondition(true,converter.toColumn(model.PAG_IUR, true) + " = ? ");
			}
			
			if(this.indiceDati != null) {
				sqlQueryObject.addWhereCondition(true,converter.toColumn(model.PAG_INDICE_DATI, true) + " = ? ");
			}
			
			if(this.idPagamento != null) {
				sqlQueryObject.addWhereCondition(true,converter.toTable(model.PAG_IUV, true) + ".id" + " = ? ");
			}
			
			if(this.dataPagamentoDa != null && this.dataPagamentoA != null) {
				sqlQueryObject.addWhereCondition(true,converter.toColumn(model.PAG_DATA_PAGAMENTO, true) + " >= ? ");
				sqlQueryObject.addWhereCondition(true,converter.toColumn(model.PAG_DATA_PAGAMENTO, true) + " <= ? ");
			} else {
				if(this.dataPagamentoDa != null) {
					sqlQueryObject.addWhereCondition(true,converter.toColumn(model.PAG_DATA_PAGAMENTO, true) + " >= ? ");
				} 

				if(this.dataPagamentoA != null) {
					sqlQueryObject.addWhereCondition(true,converter.toColumn(model.PAG_DATA_PAGAMENTO, true) + " <= ? ");
				}
			}
			
			if(this.codDominio != null) {
				if(!addTabellaDomini) {
					sqlQueryObject.addFromTable(converter.toTable(model.VRS_ID_DOMINIO));
					sqlQueryObject.addWhereCondition(converter.toTable(model.PAG_IUV, true) + ".vrs_id_dominio="
							+converter.toTable(model.VRS_ID_DOMINIO, true)+".id");

					addTabellaDomini = true;
				}
				
				sqlQueryObject.addWhereCondition(true,converter.toColumn(model.VRS_ID_DOMINIO.COD_DOMINIO, true) + " = ? ");
			}
			
			if(this.tnr != null) {
				sqlQueryObject.addWhereCondition(true,converter.toColumn(model.RNC_TRN, true) + " = ? ");
			}
			
			// SV
			
			if(this.codSingoloVersamentoEnte != null){
				sqlQueryObject.addWhereLikeCondition(converter.toColumn(model.SNG_COD_SING_VERS_ENTE, true), this.codSingoloVersamentoEnte, true, true);
			}
			
			// VERSAMENTI
			
			if(this.dominiUOAutorizzati != null && this.dominiUOAutorizzati.size() > 0) {
				List<String> uoConditions = new ArrayList<>();
				for (IdUnitaOperativa idUnita : this.dominiUOAutorizzati) {
					if(idUnita.getIdDominio() != null) {
						
						StringBuilder sb = new StringBuilder();
						sb.append(converter.toTable(model.PAG_IUV, true) + ".vrs_id_dominio = ? ");
						
						if(idUnita.getIdUnita() != null ) {
							sb.append(" and ");
							sb.append(converter.toTable(model.PAG_IUV, true) + ".vrs_id_uo = ? ");
						}
						
						uoConditions.add(sb.toString());
					}
				}
				if(!uoConditions.isEmpty()) {
					sqlQueryObject.addWhereCondition(false, uoConditions.toArray(new String[uoConditions.size()]));
				}
			}
			
			if(this.idTipiVersamento != null && !this.idTipiVersamento.isEmpty()){
				this.idTipiVersamento.removeAll(Collections.singleton(null));
				
				String [] idsTipiVersamento = this.idTipiVersamento.stream().map(e -> e.toString()).collect(Collectors.toList()).toArray(new String[this.idTipiVersamento.size()]);
				sqlQueryObject.addWhereINCondition(converter.toTable(model.PAG_IUV, true) + ".vrs_id_tipo_versamento", false, idsTipiVersamento );
			}

			if(this.codVersamento != null){
				sqlQueryObject.addWhereLikeCondition(converter.toColumn(model.VRS_COD_VERSAMENTO_ENTE, true), this.codVersamento, true, true);
			}

			if(this.idApplicazione != null) {
				sqlQueryObject.addWhereCondition(true,converter.toTable(model.PAG_IUV, true) + ".vrs_id_applicazione" + " = ? ");
			}
			
			if(this.codUnivocoDebitore != null) { 
				sqlQueryObject.addWhereLikeCondition(converter.toColumn(model.VRS_DEBITORE_IDENTIFICATIVO, true), this.codUnivocoDebitore, true, true);
			}
			
			if(this.idVersamento != null && !this.idVersamento.isEmpty()){
				this.idVersamento.removeAll(Collections.singleton(null));
				
				String [] idsVersamento = this.idVersamento.stream().map(e -> e.toString()).collect(Collectors.toList()).toArray(new String[this.idVersamento.size()]);
				sqlQueryObject.addWhereINCondition(converter.toTable(model.PAG_IUV, true) + ".vrs_id", false, idsVersamento );
			}
			
			if(this.direzione != null && this.direzione.size() > 0){
				this.direzione.removeAll(Collections.singleton(null));
				
				String [] direzioniS = this.direzione.toArray(new String[this.direzione.size()]);
				sqlQueryObject.addWhereINCondition(converter.toColumn(model.VRS_DIREZIONE, true), true, direzioniS);
			}

			if(this.divisione != null && this.divisione.size() > 0){
				this.divisione.removeAll(Collections.singleton(null));
				
				String [] divisioniS = this.divisione.toArray(new String[this.divisione.size()]);
				sqlQueryObject.addWhereINCondition(converter.toColumn(model.VRS_DIVISIONE, true), true, divisioniS);
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
		
		if(this.iuv != null) {
			// donothing
		}

		if(this.iur != null) {
			lst.add(this.iur);
		}
		
		if(this.indiceDati != null) {
			lst.add(this.indiceDati);
		}
		
		if(this.idPagamento != null) {
			lst.add(this.idPagamento);
		}
		
		if(this.dataPagamentoDa != null && this.dataPagamentoA != null) {
			lst.add(this.dataPagamentoDa);
			lst.add(this.dataPagamentoA);
		} else {
			if(this.dataPagamentoDa != null) {
				lst.add(this.dataPagamentoDa);
			} 

			if(this.dataPagamentoA != null) {
				lst.add(this.dataPagamentoA);
			}
		}
		
		if(this.codDominio != null) {
			lst.add(this.codDominio);
		}
		
		if(this.tnr != null) {
			lst.add(this.tnr);
		}
		
		// SV
		
		if(this.codSingoloVersamentoEnte != null){
			// donothing
		}
		
		// VERSAMENTI
		
		if(this.dominiUOAutorizzati != null && this.dominiUOAutorizzati.size() > 0) {
			for (IdUnitaOperativa idUnita : this.dominiUOAutorizzati) {
				if(idUnita.getCodDominio() != null) {
					lst.add(idUnita.getCodDominio());
					if(idUnita.getIdUnita() != null ) {
						lst.add(idUnita.getIdUnita());
					}
				}
			}
		}
		
		if(this.idTipiVersamento != null && !this.idTipiVersamento.isEmpty()){
			// donothing
		}

		if(this.codVersamento != null){
			// donothing
		}

		if(this.idApplicazione != null) {
			lst.add(this.idApplicazione);
		}
		
		if(this.codUnivocoDebitore != null) { 
			// donothing
		}
		
		if(this.idVersamento != null && !this.idVersamento.isEmpty()){
			// donothing
		}
		
		if(this.direzione != null && this.direzione.size() > 0){
			// donothing
		}

		if(this.divisione != null && this.divisione.size() > 0){
			// donothing
		}
		
		return lst.toArray(new Object[lst.size()]);
	}

	public Date getDataPagamentoDa() {
		return dataPagamentoDa;
	}

	public void setDataPagamentoDa(Date dataPagamentoDa) {
		this.dataPagamentoDa = dataPagamentoDa;
	}

	public Date getDataPagamentoA() {
		return dataPagamentoA;
	}

	public void setDataPagamentoA(Date dataPagamentoA) {
		this.dataPagamentoA = dataPagamentoA;
	}

	public String getIuv() {
		return iuv;
	}

	public void setIuv(String iuv) {
		this.iuv = iuv;
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

	public String getIur() {
		return iur;
	}

	public void setIur(String iur) {
		this.iur = iur;
	}

	public Long getIdPagamento() {
		return idPagamento;
	}

	public void setIdPagamento(Long idPagamento) {
		this.idPagamento = idPagamento;
	}

	public Integer getIndiceDati() {
		return indiceDati;
	}

	public void setIndiceDati(Integer indiceDati) {
		this.indiceDati = indiceDati;
	}

	public String getCodDominio() {
		return codDominio;
	}

	public void setCodDominio(String codDominio) {
		this.codDominio = codDominio;
	}

	public String getTnr() {
		return tnr;
	}

	public void setTnr(String tnr) {
		this.tnr = tnr;
	}

	public String getCodSingoloVersamentoEnte() {
		return codSingoloVersamentoEnte;
	}

	public void setCodSingoloVersamentoEnte(String codSingoloVersamentoEnte) {
		this.codSingoloVersamentoEnte = codSingoloVersamentoEnte;
	}

	public Long getIdApplicazione() {
		return idApplicazione;
	}

	public void setIdApplicazione(Long idApplicazione) {
		this.idApplicazione = idApplicazione;
	}

	public String getCodUnivocoDebitore() {
		return codUnivocoDebitore;
	}

	public void setCodUnivocoDebitore(String codUnivocoDebitore) {
		this.codUnivocoDebitore = codUnivocoDebitore;
	}

	public String getCodVersamento() {
		return codVersamento;
	}

	public void setCodVersamento(String codVersamento) {
		this.codVersamento = codVersamento;
	}

	public List<Long> getIdVersamento() {
		return idVersamento;
	}

	public void setIdVersamento(List<Long> idVersamento) {
		this.idVersamento = idVersamento;
	}

	public List<Long> getIdTipiVersamento() {
		return idTipiVersamento;
	}

	public void setIdTipiVersamento(List<Long> idTipiVersamento) {
		this.idTipiVersamento = idTipiVersamento;
	}

	public boolean isRicercaIdFlussoCaseInsensitive() {
		return ricercaIdFlussoCaseInsensitive;
	}

	public void setRicercaIdFlussoCaseInsensitive(boolean ricercaIdFlussoCaseInsensitive) {
		this.ricercaIdFlussoCaseInsensitive = ricercaIdFlussoCaseInsensitive;
	}

	public Boolean getIncassato() {
		return incassato;
	}

	public void setIncassato(Boolean incassato) {
		this.incassato = incassato;
	}

	public boolean isSearchModeEquals() {
		return this.searchModeEquals;
	}

	public void setSearchModeEquals(boolean searchModeEquals) {
		this.searchModeEquals = searchModeEquals;
	}

	public List<IdUnitaOperativa> getDominiUOAutorizzati() {
		return dominiUOAutorizzati;
	}

	public void setDominiUOAutorizzati(List<IdUnitaOperativa> dominiUOAutorizzati) {
		this.dominiUOAutorizzati = dominiUOAutorizzati;
	}
}
