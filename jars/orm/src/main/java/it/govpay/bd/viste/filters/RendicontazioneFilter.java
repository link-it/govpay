package it.govpay.bd.viste.filters;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import org.apache.commons.lang.StringUtils;
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
import it.govpay.model.Fr;
import it.govpay.model.Rendicontazione.EsitoRendicontazione;
import it.govpay.model.Rendicontazione.StatoRendicontazione;
import it.govpay.orm.VistaRendicontazione;
import it.govpay.orm.dao.jdbc.converter.VistaRendicontazioneFieldConverter;
import it.govpay.orm.model.VistaRendicontazioneModel;

public class RendicontazioneFilter extends AbstractFilter {
	
	private Date dataAcquisizioneFlussoDa;
	private Date dataAcquisizioneFlussoA;
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
	private List<Long> idVersamento= null;
	private List<Long> idTipiVersamento = null;
	
	private boolean ricercaIdFlussoCaseInsensitive = false;
	
	private Boolean incassato;
	private Fr.StatoFr statoFlusso;
	private boolean ricercaFR = true;
	private boolean searchModeEquals = true; 
	private List<IdUnitaOperativa> dominiUOAutorizzati;
	
	private VistaRendicontazioneFieldConverter converter = null;

	public RendicontazioneFilter(IExpressionConstructor expressionConstructor) throws ServiceException {
		this(expressionConstructor,false);
	}
	
	public RendicontazioneFilter(IExpressionConstructor expressionConstructor, boolean simpleSearch) throws ServiceException {
		super(expressionConstructor, simpleSearch);
		this.listaFieldSimpleSearch.add(VistaRendicontazione.model().RND_IUV);
		this.listaFieldSimpleSearch.add(VistaRendicontazione.model().RND_IUR);
		this.listaFieldSimpleSearch.add(VistaRendicontazione.model().FR_COD_DOMINIO);
		this.converter = new VistaRendicontazioneFieldConverter(ConnectionManager.getJDBCServiceManagerProperties().getDatabase()); 
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
			if(this.getStatoFlusso() != null){
				newExpression.equals(VistaRendicontazione.model().FR_STATO, this.getStatoFlusso().toString());
				addAnd = true;
			}
			
			if(this.dataAcquisizioneFlussoDa != null) {
				if(addAnd)
					newExpression.and();
				
				newExpression.greaterEquals(VistaRendicontazione.model().FR_DATA_ACQUISIZIONE, this.dataAcquisizioneFlussoDa);
				addAnd = true;
			}
			
			if(this.dataAcquisizioneFlussoA != null) {
				if(addAnd)
					newExpression.and();
				
				newExpression.lessEquals(VistaRendicontazione.model().FR_DATA_ACQUISIZIONE, this.dataAcquisizioneFlussoA);
				addAnd = true;
			}
			
			if(this.codFlusso != null && StringUtils.isNotEmpty(this.codFlusso)) {
				if(addAnd)
					newExpression.and();
				
				if(!this.searchModeEquals) {
					if(this.ricercaIdFlussoCaseInsensitive) {
						newExpression.ilike(VistaRendicontazione.model().FR_COD_FLUSSO, this.codFlusso, LikeMode.ANYWHERE);
					} else {
						newExpression.like(VistaRendicontazione.model().FR_COD_FLUSSO, this.codFlusso, LikeMode.ANYWHERE);
					}
				}else {
					if(this.ricercaIdFlussoCaseInsensitive) {
						IExpression newExpressionIngnoreCase = this.newExpression();
						
						newExpressionIngnoreCase.equals(VistaRendicontazione.model().FR_COD_FLUSSO, this.codFlusso).or()
						.equals(VistaRendicontazione.model().FR_COD_FLUSSO, this.codFlusso.toUpperCase()).or()
						.equals(VistaRendicontazione.model().FR_COD_FLUSSO, this.codFlusso.toLowerCase());
						
						newExpression.and(newExpressionIngnoreCase);
					} else {
						newExpression.equals(VistaRendicontazione.model().FR_COD_FLUSSO, this.codFlusso);
					}
				}
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
			
			if(this.dominiUOAutorizzati != null && this.dominiUOAutorizzati.size() > 0) {
				if(addAnd)
					newExpression.and();
				
				IExpression newExpressionUO = this.newExpression();
				List<IExpression> listExpressionSingolaUO = new ArrayList<>();
				
				for (IdUnitaOperativa idUnita : this.dominiUOAutorizzati) {
					if(idUnita.getCodDominio() != null) {
						IExpression newExpressionSingolaUO = this.newExpression();
						
						newExpressionSingolaUO.equals(VistaRendicontazione.model().FR_COD_DOMINIO, idUnita.getCodDominio());
						
						if(idUnita.getIdUnita() != null ) {
							CustomField iduoCustomField = new CustomField("vrs_id_uo", Long.class, "vrs_id_uo", converter.toTable(VistaRendicontazione.model()));
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
				String [] idsRendicontazione = this.idRendicontazione.stream().map(e -> e.toString()).collect(Collectors.toList()).toArray(new String[this.idRendicontazione.size()]);
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
			if(this.getStatoFlusso() != null){
				sqlQueryObject.addWhereCondition(true,converter.toColumn(model.FR_STATO, true) + " = ? ");
			}
			
			if(this.dataAcquisizioneFlussoDa != null) {
				sqlQueryObject.addWhereCondition(true,converter.toColumn(model.FR_DATA_ACQUISIZIONE, true) + " >= ? ");
			}
			
			if(this.dataAcquisizioneFlussoA != null) {
				sqlQueryObject.addWhereCondition(true,converter.toColumn(model.FR_DATA_ACQUISIZIONE, true) + " <= ? ");
			}
			
			if(this.codFlusso != null && StringUtils.isNotEmpty(this.codFlusso)) {
				if(!this.searchModeEquals) {
					if(this.ricercaIdFlussoCaseInsensitive) {
						sqlQueryObject.addWhereLikeCondition(converter.toColumn(model.FR_COD_FLUSSO, true), this.codFlusso, true, true);
					} else {
						sqlQueryObject.addWhereLikeCondition(converter.toColumn(model.FR_COD_FLUSSO, true), this.codFlusso, true, false);
					}
				}else {
					if(this.ricercaIdFlussoCaseInsensitive) {
						sqlQueryObject.addWhereCondition(false,converter.toColumn(model.FR_COD_FLUSSO, true) + " = ? ",converter.toColumn(model.FR_COD_FLUSSO, true) + " = ? ",converter.toColumn(model.FR_COD_FLUSSO, true) + " = ? ");
					} else {
						sqlQueryObject.addWhereCondition(true,converter.toColumn(model.FR_COD_FLUSSO, true) + " = ? ");
					}
				}
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
			
			if(this.dominiUOAutorizzati != null && this.dominiUOAutorizzati.size() > 0) {
				List<String> uoConditions = new ArrayList<>();
				for (IdUnitaOperativa idUnita : this.dominiUOAutorizzati) {
					if(idUnita.getIdDominio() != null) {
						
						StringBuilder sb = new StringBuilder();
						sb.append(converter.toColumn(model.FR_COD_DOMINIO, true) + " = ? ");
						
						if(idUnita.getIdUnita() != null ) {
							sb.append(" and ");
							sb.append(converter.toTable(model.RND_IUV, true) + ".vrs_id_uo = ? ");
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
		if(this.getStatoFlusso() != null){
			lst.add(this.getStatoFlusso().toString());
		}
		
		if(this.dataAcquisizioneFlussoDa != null) {
			lst.add(this.dataAcquisizioneFlussoDa);
		}
		
		if(this.dataAcquisizioneFlussoA != null) {
			lst.add(this.dataAcquisizioneFlussoA);
		}
		
		if(this.codFlusso != null && StringUtils.isNotEmpty(this.codFlusso)) {
			if(this.searchModeEquals) {
				if(this.ricercaIdFlussoCaseInsensitive) {
					lst.add(this.codFlusso);
					lst.add(this.codFlusso.toUpperCase());
					lst.add(this.codFlusso.toLowerCase());
				}else {
					lst.add(this.codFlusso);
				}
			}
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
			try {
				lst = this.setValoreFiltroBoolean(lst, converter, this.frObsoleto);
			} catch (ExpressionException e) {
				throw new ServiceException(e);
			}
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

	public Boolean getFrObsoleto() {
		return frObsoleto;
	}

	public void setFrObsoleto(Boolean frObsoleto) {
		this.frObsoleto = frObsoleto;
	}

	public boolean isRicercaIdFlussoCaseInsensitive() {
		return ricercaIdFlussoCaseInsensitive;
	}

	public void setRicercaIdFlussoCaseInsensitive(boolean ricercaIdFlussoCaseInsensitive) {
		this.ricercaIdFlussoCaseInsensitive = ricercaIdFlussoCaseInsensitive;
	}

	public Date getDataAcquisizioneFlussoDa() {
		return dataAcquisizioneFlussoDa;
	}

	public void setDataAcquisizioneFlussoDa(Date dataAcquisizioneFlussoDa) {
		this.dataAcquisizioneFlussoDa = dataAcquisizioneFlussoDa;
	}

	public Date getDataAcquisizioneFlussoA() {
		return dataAcquisizioneFlussoA;
	}

	public void setDataAcquisizioneFlussoA(Date dataAcquisizioneFlussoA) {
		this.dataAcquisizioneFlussoA = dataAcquisizioneFlussoA;
	}

	public Boolean getIncassato() {
		return incassato;
	}

	public void setIncassato(Boolean incassato) {
		this.incassato = incassato;
	}

	public Fr.StatoFr getStatoFlusso() {
		return statoFlusso;
	}

	public void setStatoFlusso(Fr.StatoFr statoFlusso) {
		this.statoFlusso = statoFlusso;
	}

	public boolean isRicercaFR() {
		return ricercaFR;
	}

	public void setRicercaFR(boolean ricercaFR) {
		this.ricercaFR = ricercaFR;
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
