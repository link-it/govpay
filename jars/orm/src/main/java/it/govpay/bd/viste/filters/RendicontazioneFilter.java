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
import it.govpay.model.Rendicontazione.EsitoRendicontazione;
import it.govpay.model.Rendicontazione.StatoRendicontazione;
import it.govpay.orm.VistaRendicontazione;
import it.govpay.orm.dao.jdbc.converter.VistaRendicontazioneFieldConverter;
import it.govpay.orm.model.VistaRendicontazioneModel;

public class RendicontazioneFilter extends AbstractFilter {
	
	private Date dataFlussoDa;
	private Date dataFlussoA;
	private Date dataRendicontazioneDa;
	private Date dataRendicontazioneA;
	private String codFlusso;
	private String iuv;
	private List<String> direzione;
	private List<String> divisione;
	private Boolean frObsoleto;
	
	// Interni alla tabella
	private String iur;
	private EsitoRendicontazione esito;
	private StatoRendicontazione stato;
	private Long idFr;
	private Long idPagamento;
	private String tipo;
	private Integer indiceDati;
	private List<Long> idRendicontazione;
	
	// Esterni alla tabella
	private String codDominio; // fr 
	private String tnr;
	
	private String codSingoloVersamentoEnte = null; // SV
	
	private Long idApplicazione; // versamenti
	private String codUnivocoDebitore;
	private String codVersamento = null;
	private List<Long> idDomini;
	private List<Long> idVersamento= null;
	private List<Long> idUo;
	private List<Long> idTipiVersamento = null;

	public RendicontazioneFilter(IExpressionConstructor expressionConstructor) {
		this(expressionConstructor,false);
	}
	
	public RendicontazioneFilter(IExpressionConstructor expressionConstructor, boolean simpleSearch) {
		super(expressionConstructor, simpleSearch);
		this.listaFieldSimpleSearch.add(VistaRendicontazione.model().RND_IUV);
		this.listaFieldSimpleSearch.add(VistaRendicontazione.model().RND_IUR);
		this.listaFieldSimpleSearch.add(VistaRendicontazione.model().FR_COD_DOMINIO);
	}
	
	@Override
	public IExpression _toSimpleSearchExpression() throws ServiceException {
		try {
			IExpression newExpression = super._toSimpleSearchExpression();

			if(this.idFr != null) {
				IExpression newExpressionFr = this.newExpression();
				
				CustomField idFrField = new CustomField("id_fr", Long.class, "id_fr", this.getRootTable());
				newExpressionFr.equals(idFrField, this.idFr);
				newExpression.and(newExpressionFr);
			}
			
			if(this.idDomini != null){
				IExpression newExpressionDomini = this.newExpression();
				this.idDomini.removeAll(Collections.singleton(null));
				newExpressionDomini.in(VistaRendicontazione.model().FR_COD_DOMINIO, this.idDomini);
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
	public IExpression _toExpression() throws ServiceException {
		try {
			IExpression newExpression = this.newExpression();
			boolean addAnd = false;
			
			VistaRendicontazioneFieldConverter converter = new VistaRendicontazioneFieldConverter(ConnectionManager.getJDBCServiceManagerProperties().getDatabase()); 
			
			// Rendicontazione
			
			if(this.iuv != null) {
				if(addAnd)
					newExpression.and();
				
				newExpression.ilike(VistaRendicontazione.model().RND_IUV, this.iuv,LikeMode.ANYWHERE);
				addAnd = true;
			}

			if(this.iur != null) {
				if(addAnd)
					newExpression.and();
				
				newExpression.equals(VistaRendicontazione.model().RND_IUR, this.iur);
				addAnd = true;
			}
			
			if(this.indiceDati != null) {
				if(addAnd)
					newExpression.and();
				
				newExpression.equals(VistaRendicontazione.model().RND_INDICE_DATI, this.indiceDati);
				addAnd = true;
			}
			
			if(this.tipo!= null) {
				if(addAnd)
					newExpression.and();
				
				newExpression.equals(VistaRendicontazione.model().RND_ESITO, EsitoRendicontazione.valueOf(this.tipo).getCodifica());
				addAnd = true;
			}
			
			if(this.esito!= null) {
				if(addAnd)
					newExpression.and();
				
				newExpression.equals(VistaRendicontazione.model().RND_ESITO, this.esito.getCodifica());
				addAnd = true;
			}

			if(this.stato!= null) {
				if(addAnd)
					newExpression.and();
				
				newExpression.equals(VistaRendicontazione.model().RND_STATO, this.stato.toString());
				addAnd = true;
			}

			if(this.idPagamento != null) {
				if(addAnd)
					newExpression.and();
				
				CustomField idPagamentoField = new CustomField("rnd_id_pagamento", Long.class, "rnd_id_pagamento", converter.toTable(VistaRendicontazione.model()));
				newExpression.equals(idPagamentoField, this.idPagamento);
				addAnd = true;
			}
			
			if(this.idRendicontazione != null && this.idRendicontazione.size() >0){ 
				if(addAnd)
					newExpression.and();
				
				CustomField cf = new CustomField(ALIAS_ID, Long.class, ALIAS_ID, this.getTable(VistaRendicontazione.model()));
				newExpression.in(cf, this.idRendicontazione);
				addAnd = true;
			}
			
			if(this.dataRendicontazioneDa != null && this.dataRendicontazioneA != null) {
				if(addAnd)
					newExpression.and();

				newExpression.between(VistaRendicontazione.model().RND_DATA, this.dataRendicontazioneDa,this.dataRendicontazioneA);
				addAnd = true;
			} else {
				if(this.dataRendicontazioneDa != null) {
					if(addAnd)
						newExpression.and();

					newExpression.greaterEquals(VistaRendicontazione.model().RND_DATA, this.dataRendicontazioneDa);
					addAnd = true;
				} 

				if(this.dataRendicontazioneA != null) {
					if(addAnd)
						newExpression.and();

					newExpression.lessEquals(VistaRendicontazione.model().RND_DATA, this.dataRendicontazioneA);
					addAnd = true;
				}
			}
			
			// FR
			
			if(this.codFlusso != null) {
				if(addAnd)
					newExpression.and();
				
//				newExpression.ilike(VistaRendicontazione.model().FR_COD_FLUSSO, this.codFlusso, LikeMode.ANYWHERE);
				newExpression.equals(VistaRendicontazione.model().FR_COD_FLUSSO, this.codFlusso);
				addAnd = true;
			}
			
			if(this.idFr != null) {
				if(addAnd)
					newExpression.and();
				
				newExpression.equals(VistaRendicontazione.model().FR_ID, this.idFr);
				addAnd = true;
			}

			if(this.codDominio != null) {
				if(addAnd)
					newExpression.and();
				
				newExpression.equals(VistaRendicontazione.model().FR_COD_DOMINIO, this.codDominio);
				addAnd = true;
			}
			
			if(this.tnr != null) {
				if(addAnd)
					newExpression.and();
				
				newExpression.equals(VistaRendicontazione.model().FR_IUR, this.tnr);
				addAnd = true;
			}
			
			if(this.dataFlussoDa != null && this.dataFlussoA != null) {
				if(addAnd)
					newExpression.and();

				newExpression.between(VistaRendicontazione.model().FR_DATA_ORA_FLUSSO, this.dataFlussoDa,this.dataFlussoA);
				addAnd = true;
			} else {
				if(this.dataFlussoDa != null) {
					if(addAnd)
						newExpression.and();

					newExpression.greaterEquals(VistaRendicontazione.model().FR_DATA_ORA_FLUSSO, this.dataFlussoDa);
					addAnd = true;
				} 

				if(this.dataFlussoA != null) {
					if(addAnd)
						newExpression.and();

					newExpression.lessEquals(VistaRendicontazione.model().FR_DATA_ORA_FLUSSO, this.dataFlussoA);
					addAnd = true;
				}
			}
			
			if(this.frObsoleto != null) {
				if(addAnd)
					newExpression.and();
				
				newExpression.equals(VistaRendicontazione.model().FR_OBSOLETO, this.frObsoleto);
				addAnd = true;
			}
			
			
			// SV
			
			if(this.codSingoloVersamentoEnte != null){
				newExpression.ilike(VistaRendicontazione.model().SNG_COD_SING_VERS_ENTE, this.codSingoloVersamentoEnte, LikeMode.ANYWHERE);
				addAnd = true;
			}
			
			// VERSAMENTI
			
			if(this.idDomini != null && !this.idDomini.isEmpty()){
				this.idDomini.removeAll(Collections.singleton(null));
				if(addAnd)
					newExpression.and();
				
				CustomField cf = new CustomField("vrs_id_dominio", Long.class, "vrs_id_dominio", converter.toTable(VistaRendicontazione.model()));
				newExpression.in(cf, this.idDomini);
				addAnd = true;
			}
			
			if(this.idUo != null && !this.idUo.isEmpty()){
				this.idUo.removeAll(Collections.singleton(null));
				if(addAnd)
					newExpression.and();
				CustomField cf = new CustomField("vrs_id_uo", Long.class, "vrs_id_uo", converter.toTable(VistaRendicontazione.model()));
				newExpression.in(cf, this.idUo);
				addAnd = true;
			}
			
			if(this.idTipiVersamento != null && !this.idTipiVersamento.isEmpty()){
				this.idTipiVersamento.removeAll(Collections.singleton(null));
				if(addAnd)
					newExpression.and();
				CustomField cf = new CustomField("vrs_id_tipo_versamento", Long.class, "vrs_id_tipo_versamento", converter.toTable(VistaRendicontazione.model()));
				newExpression.in(cf, this.idTipiVersamento);
				addAnd = true;
			}

			if(this.codVersamento != null){
				if(addAnd)
					newExpression.and();

				newExpression.ilike(VistaRendicontazione.model().VRS_COD_VERSAMENTO_ENTE, this.codVersamento, LikeMode.ANYWHERE);
				addAnd = true;
			}

			if(this.idApplicazione != null) {
				if(addAnd)
					newExpression.and();
				
				CustomField idApplicazioneField = new CustomField("vrs_id_applicazione", Long.class, "vrs_id_applicazione", converter.toTable(VistaRendicontazione.model()));
				newExpression.equals(idApplicazioneField, this.idApplicazione);
				addAnd = true;
			}
			
			if(this.codUnivocoDebitore != null) { 
				if(addAnd)
					newExpression.and();
				newExpression.ilike(VistaRendicontazione.model().VRS_DEBITORE_IDENTIFICATIVO, this.codUnivocoDebitore,LikeMode.ANYWHERE);
				addAnd = true;
			}
			
			if(this.idVersamento != null && !this.idVersamento.isEmpty()){
				this.idVersamento.removeAll(Collections.singleton(null));
				if(addAnd)
					newExpression.and();
				CustomField cf = new CustomField("vrs_id", Long.class, "vrs_id", converter.toTable(VistaRendicontazione.model()));
				newExpression.in(cf, this.idVersamento);
				addAnd = true;
			}
			
			if(this.direzione != null && this.direzione.size() > 0){
				this.direzione.removeAll(Collections.singleton(null));
				if(addAnd)
					newExpression.and();

				newExpression.in(VistaRendicontazione.model().VRS_DIREZIONE, this.direzione);
				addAnd = true;
			}

			if(this.divisione != null && this.divisione.size() > 0){
				this.divisione.removeAll(Collections.singleton(null));
				if(addAnd)
					newExpression.and();

				newExpression.in(VistaRendicontazione.model().VRS_DIVISIONE, this.divisione);
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
			VistaRendicontazioneFieldConverter converter = new VistaRendicontazioneFieldConverter(ConnectionManager.getJDBCServiceManagerProperties().getDatabase()); 
			VistaRendicontazioneModel model = it.govpay.orm.VistaRendicontazione.model();
			
			if(this.iuv != null) {
				sqlQueryObject.addWhereLikeCondition(converter.toColumn(model.RND_IUV, true), this.iuv, true, true);
			}

			if(this.iur != null) {
				sqlQueryObject.addWhereCondition(true,converter.toColumn(model.RND_IUR, true) + " = ? ");
			}
			
			if(this.indiceDati != null) {
				sqlQueryObject.addWhereCondition(true,converter.toColumn(model.RND_INDICE_DATI, true) + " = ? ");
			}
			
			if(this.tipo!= null) {
				sqlQueryObject.addWhereCondition(true,converter.toColumn(model.RND_ESITO, true) + " = ? ");
			}
			
			if(this.esito!= null) {
				sqlQueryObject.addWhereCondition(true,converter.toColumn(model.RND_ESITO, true) + " = ? ");
			}

			if(this.stato!= null) {
				sqlQueryObject.addWhereCondition(true,converter.toColumn(model.RND_STATO, true) + " = ? ");
			}

			if(this.idPagamento != null) {
				sqlQueryObject.addWhereCondition(true,converter.toTable(model.RND_IUV, true) + ".rnd_id_pagamento" + " = ? ");
			}
			
			if(this.idRendicontazione != null && this.idRendicontazione.size() >0){ 
				String [] idsRendicontazione = this.idUo.stream().map(e -> e.toString()).collect(Collectors.toList()).toArray(new String[this.idUo.size()]);
				sqlQueryObject.addWhereINCondition(converter.toTable(model.RND_IUV, true) + ".id", false, idsRendicontazione );
			}
			
			if(this.dataRendicontazioneDa != null && this.dataRendicontazioneA != null) {
				sqlQueryObject.addWhereCondition(true,converter.toColumn(model.RND_DATA, true) + " >= ? ");
				sqlQueryObject.addWhereCondition(true,converter.toColumn(model.RND_DATA, true) + " <= ? ");
			} else {
				if(this.dataRendicontazioneDa != null) {
					sqlQueryObject.addWhereCondition(true,converter.toColumn(model.RND_DATA, true) + " >= ? ");
				} 

				if(this.dataRendicontazioneA != null) {
					sqlQueryObject.addWhereCondition(true,converter.toColumn(model.RND_DATA, true) + " <= ? ");
				}
			}
			
			// FR
			
			if(this.codFlusso != null) {
				sqlQueryObject.addWhereCondition(true,converter.toColumn(model.FR_COD_FLUSSO, true) + " = ? ");
			}
			
			if(this.idFr != null) {
				sqlQueryObject.addWhereCondition(true,converter.toColumn(model.FR_ID, true) + " = ? ");
			}

			if(this.codDominio != null) {
				sqlQueryObject.addWhereCondition(true,converter.toColumn(model.FR_COD_DOMINIO, true) + " = ? ");
			}
			
			if(this.tnr != null) {
				sqlQueryObject.addWhereCondition(true,converter.toColumn(model.FR_IUR, true) + " = ? ");
			}
			
			if(this.dataFlussoDa != null && this.dataFlussoA != null) {
				sqlQueryObject.addWhereCondition(true,converter.toColumn(model.FR_DATA_ORA_FLUSSO, true) + " >= ? ");
				sqlQueryObject.addWhereCondition(true,converter.toColumn(model.FR_DATA_ORA_FLUSSO, true) + " <= ? ");
			} else {
				if(this.dataFlussoDa != null) {
					sqlQueryObject.addWhereCondition(true,converter.toColumn(model.FR_DATA_ORA_FLUSSO, true) + " >= ? ");
				} 

				if(this.dataFlussoA != null) {
					sqlQueryObject.addWhereCondition(true,converter.toColumn(model.FR_DATA_ORA_FLUSSO, true) + " <= ? ");
				}
			}
			
			if(this.frObsoleto != null) {
				sqlQueryObject.addWhereCondition(true,converter.toColumn(model.FR_OBSOLETO, true) + " = ? ");
			}
			
			
			// SV
			
			if(this.codSingoloVersamentoEnte != null){
				sqlQueryObject.addWhereLikeCondition(converter.toColumn(model.SNG_COD_SING_VERS_ENTE, true), this.codSingoloVersamentoEnte, true, true);
			}
			
			// VERSAMENTI
			
			if(this.idDomini != null && !this.idDomini.isEmpty()){
				this.idDomini.removeAll(Collections.singleton(null));
				
				String [] idsDominio = this.idDomini.stream().map(e -> e.toString()).collect(Collectors.toList()).toArray(new String[this.idDomini.size()]);
				sqlQueryObject.addWhereINCondition(converter.toTable(model.RND_IUV, true) + ".vrs_id_dominio", false, idsDominio );
			}
			
			if(this.idUo != null && !this.idUo.isEmpty()){
				this.idUo.removeAll(Collections.singleton(null));
				
				String [] idsUo = this.idUo.stream().map(e -> e.toString()).collect(Collectors.toList()).toArray(new String[this.idUo.size()]);
				sqlQueryObject.addWhereINCondition(converter.toTable(model.RND_IUV, true) + ".vrs_id_uo", false, idsUo );
			}
			
			if(this.idTipiVersamento != null && !this.idTipiVersamento.isEmpty()){
				this.idTipiVersamento.removeAll(Collections.singleton(null));
				
				String [] idsTipiVersamento = this.idTipiVersamento.stream().map(e -> e.toString()).collect(Collectors.toList()).toArray(new String[this.idTipiVersamento.size()]);
				sqlQueryObject.addWhereINCondition(converter.toTable(model.RND_IUV, true) + ".vrs_id_tipo_versamento", false, idsTipiVersamento );
			}

			if(this.codVersamento != null){
				sqlQueryObject.addWhereLikeCondition(converter.toColumn(model.VRS_COD_VERSAMENTO_ENTE, true), this.codVersamento, true, true);
			}

			if(this.idApplicazione != null) {
				sqlQueryObject.addWhereCondition(true,converter.toTable(model.RND_IUV, true) + ".vrs_id_applicazione" + " = ? ");
			}
			
			if(this.codUnivocoDebitore != null) { 
				sqlQueryObject.addWhereLikeCondition(converter.toColumn(model.VRS_DEBITORE_IDENTIFICATIVO, true), this.codUnivocoDebitore, true, true);
			}
			
			if(this.idVersamento != null && !this.idVersamento.isEmpty()){
				this.idVersamento.removeAll(Collections.singleton(null));
				
				String [] idsVersamento = this.idVersamento.stream().map(e -> e.toString()).collect(Collectors.toList()).toArray(new String[this.idVersamento.size()]);
				sqlQueryObject.addWhereINCondition(converter.toTable(model.RND_IUV, true) + ".vrs_id", false, idsVersamento );
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
		
		if(this.tipo!= null) {
			lst.add(EsitoRendicontazione.valueOf(this.tipo).getCodifica());
		}
		
		if(this.esito!= null) {
			lst.add(this.esito.getCodifica());
		}

		if(this.stato!= null) {
			lst.add(this.stato.toString());
		}

		if(this.idPagamento != null) {
			lst.add(this.idPagamento);
		}
		
		if(this.idRendicontazione != null && this.idRendicontazione.size() >0){ 
			// donothing
		}
		
		if(this.dataRendicontazioneDa != null && this.dataRendicontazioneA != null) {
			lst.add(this.dataRendicontazioneDa);
			lst.add(this.dataRendicontazioneA);
		} else {
			if(this.dataRendicontazioneDa != null) {
				lst.add(this.dataRendicontazioneDa);
			} 

			if(this.dataRendicontazioneA != null) {
				lst.add(this.dataRendicontazioneA);
			}
		}
		
		// FR
		
		if(this.codFlusso != null) {
			lst.add(this.codFlusso);
		}
		
		if(this.idFr != null) {
			lst.add(this.idFr);
		}

		if(this.codDominio != null) {
			lst.add(this.codDominio);
		}
		
		if(this.tnr != null) {
			lst.add(this.tnr);
		}
		
		if(this.dataFlussoDa != null && this.dataFlussoA != null) {
			lst.add(this.dataFlussoDa);
			lst.add(this.dataFlussoA);
		} else {
			if(this.dataFlussoDa != null) {
				lst.add(this.dataFlussoDa);
			} 

			if(this.dataFlussoA != null) {
				lst.add(this.dataFlussoA);
			}
		}
		
		if(this.frObsoleto != null) {
			lst.add(this.frObsoleto);
		}
		
		
		// SV
		
		if(this.codSingoloVersamentoEnte != null){
			// donothing
		}
		
		// VERSAMENTI
		
		if(this.idDomini != null && !this.idDomini.isEmpty()){
			// donothing
		}
		
		if(this.idUo != null && !this.idUo.isEmpty()){
			// donothing
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

	public Date getDataFlussoDa() {
		return dataFlussoDa;
	}

	public void setDataFlussoDa(Date dataFlussoDa) {
		this.dataFlussoDa = dataFlussoDa;
	}

	public Date getDataFlussoA() {
		return dataFlussoA;
	}

	public void setDataFlussoA(Date dataFlussoA) {
		this.dataFlussoA = dataFlussoA;
	}

	public Date getDataRendicontazioneDa() {
		return dataRendicontazioneDa;
	}

	public void setDataRendicontazioneDa(Date dataRendicontazioneDa) {
		this.dataRendicontazioneDa = dataRendicontazioneDa;
	}

	public Date getDataRendicontazioneA() {
		return dataRendicontazioneA;
	}

	public void setDataRendicontazioneA(Date dataRendicontazioneA) {
		this.dataRendicontazioneA = dataRendicontazioneA;
	}

	public String getCodFlusso() {
		return codFlusso;
	}

	public void setCodFlusso(String codFlusso) {
		this.codFlusso = codFlusso;
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

	public EsitoRendicontazione getEsito() {
		return esito;
	}

	public void setEsito(EsitoRendicontazione esito) {
		this.esito = esito;
	}

	public StatoRendicontazione getStato() {
		return stato;
	}

	public void setStato(StatoRendicontazione stato) {
		this.stato = stato;
	}

	public Long getIdFr() {
		return idFr;
	}

	public void setIdFr(Long idFr) {
		this.idFr = idFr;
	}

	public Long getIdPagamento() {
		return idPagamento;
	}

	public void setIdPagamento(Long idPagamento) {
		this.idPagamento = idPagamento;
	}

	public String getTipo() {
		return tipo;
	}

	public void setTipo(String tipo) {
		this.tipo = tipo;
	}

	public Integer getIndiceDati() {
		return indiceDati;
	}

	public void setIndiceDati(Integer indiceDati) {
		this.indiceDati = indiceDati;
	}

	public List<Long> getIdRendicontazione() {
		return idRendicontazione;
	}

	public void setIdRendicontazione(List<Long> idRendicontazione) {
		this.idRendicontazione = idRendicontazione;
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

	public List<Long> getIdDomini() {
		return idDomini;
	}

	public void setIdDomini(List<Long> idDomini) {
		this.idDomini = idDomini;
	}

	public List<Long> getIdVersamento() {
		return idVersamento;
	}

	public void setIdVersamento(List<Long> idVersamento) {
		this.idVersamento = idVersamento;
	}

	public List<Long> getIdUo() {
		return idUo;
	}

	public void setIdUo(List<Long> idUo) {
		this.idUo = idUo;
	}

	public List<Long> getIdTipiVersamento() {
		return idTipiVersamento;
	}

	public void setIdTipiVersamento(List<Long> idTipiVersamento) {
		this.idTipiVersamento = idTipiVersamento;
	}

	public Boolean getFrObsoleto() {
		return frObsoleto;
	}

	public void setFrObsoleto(Boolean frObsoleto) {
		this.frObsoleto = frObsoleto;
	}
	
	
}
