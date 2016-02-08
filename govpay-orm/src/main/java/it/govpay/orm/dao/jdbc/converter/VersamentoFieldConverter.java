/*
 * GovPay - Porta di Accesso al Nodo dei Pagamenti SPC 
 * http://www.gov4j.it/govpay
 * 
 * Copyright (c) 2014-2015 Link.it srl (http://www.link.it).
 * 
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
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
package it.govpay.orm.dao.jdbc.converter;

import org.openspcoop2.generic_project.beans.IField;
import org.openspcoop2.generic_project.beans.IModel;
import org.openspcoop2.generic_project.exception.ExpressionException;
import org.openspcoop2.generic_project.expression.impl.sql.AbstractSQLFieldConverter;
import org.openspcoop2.utils.TipiDatabase;

import it.govpay.orm.Versamento;


/**     
 * VersamentoFieldConverter
 *
 * @author Giovanni Bussu (bussu@link.it)
 * @author Lorenzo Nardi (nardi@link.it)
 * @author $Author$
 * @version $Rev$, $Date$
 */
public class VersamentoFieldConverter extends AbstractSQLFieldConverter {

	private TipiDatabase databaseType;
	
	public VersamentoFieldConverter(String databaseType){
		this.databaseType = TipiDatabase.toEnumConstant(databaseType);
	}
	public VersamentoFieldConverter(TipiDatabase databaseType){
		this.databaseType = databaseType;
	}


	@Override
	public IModel<?> getRootModel() throws ExpressionException {
		return Versamento.model();
	}
	
	@Override
	public TipiDatabase getDatabaseType() throws ExpressionException {
		return this.databaseType;
	}
	


	@Override
	public String toColumn(IField field,boolean returnAlias,boolean appendTablePrefix) throws ExpressionException {
		
		// In the case of columns with alias, using parameter returnAlias​​, 
		// it is possible to drive the choice whether to return only the alias or 
		// the full definition of the column containing the alias
		
		if(field.equals(Versamento.model().COD_VERSAMENTO_ENTE)){
			if(appendTablePrefix){
				return this.toAliasTable(field)+".cod_versamento_ente";
			}else{
				return "cod_versamento_ente";
			}
		}
		if(field.equals(Versamento.model().COD_DOMINIO)){
			if(appendTablePrefix){
				return this.toAliasTable(field)+".cod_dominio";
			}else{
				return "cod_dominio";
			}
		}
		if(field.equals(Versamento.model().IUV)){
			if(appendTablePrefix){
				return this.toAliasTable(field)+".iuv";
			}else{
				return "iuv";
			}
		}
		if(field.equals(Versamento.model().ID_ENTE.COD_ENTE)){
			if(appendTablePrefix){
				return this.toAliasTable(field)+".cod_ente";
			}else{
				return "cod_ente";
			}
		}
		if(field.equals(Versamento.model().ID_APPLICAZIONE.COD_APPLICAZIONE)){
			if(appendTablePrefix){
				return this.toAliasTable(field)+".cod_applicazione";
			}else{
				return "cod_applicazione";
			}
		}
		if(field.equals(Versamento.model().ID_ANAGRAFICA_DEBITORE.ID_ANAGRAFICA)){
			if(appendTablePrefix){
				return this.toAliasTable(field)+".id_anagrafica";
			}else{
				return "id_anagrafica";
			}
		}
		if(field.equals(Versamento.model().ID_ANAGRAFICA_DEBITORE.COD_UNIVOCO)){
			if(appendTablePrefix){
				return this.toAliasTable(field)+".cod_univoco";
			}else{
				return "cod_univoco";
			}
		}
		if(field.equals(Versamento.model().IMPORTO_TOTALE)){
			if(appendTablePrefix){
				return this.toAliasTable(field)+".importo_totale";
			}else{
				return "importo_totale";
			}
		}
		if(field.equals(Versamento.model().STATO_VERSAMENTO)){
			if(appendTablePrefix){
				return this.toAliasTable(field)+".stato_versamento";
			}else{
				return "stato_versamento";
			}
		}
		if(field.equals(Versamento.model().DESCRIZIONE_STATO)){
			if(appendTablePrefix){
				return this.toAliasTable(field)+".descrizione_stato";
			}else{
				return "descrizione_stato";
			}
		}
		if(field.equals(Versamento.model().STATO_RENDICONTAZIONE)){
			if(appendTablePrefix){
				return this.toAliasTable(field)+".stato_rendicontazione";
			}else{
				return "stato_rendicontazione";
			}
		}
		if(field.equals(Versamento.model().IMPORTO_PAGATO)){
			if(appendTablePrefix){
				return this.toAliasTable(field)+".importo_pagato";
			}else{
				return "importo_pagato";
			}
		}
		if(field.equals(Versamento.model().DATA_SCADENZA)){
			if(appendTablePrefix){
				return this.toAliasTable(field)+".data_scadenza";
			}else{
				return "data_scadenza";
			}
		}
		if(field.equals(Versamento.model().DATA_ORA_ULTIMO_AGGIORNAMENTO)){
			if(appendTablePrefix){
				return this.toAliasTable(field)+".data_ora_ultimo_aggiornamento";
			}else{
				return "data_ora_ultimo_aggiornamento";
			}
		}


		return super.toColumn(field,returnAlias,appendTablePrefix);
		
	}
	
	@Override
	public String toTable(IField field,boolean returnAlias) throws ExpressionException {
		
		// In the case of table with alias, using parameter returnAlias​​, 
		// it is possible to drive the choice whether to return only the alias or 
		// the full definition of the table containing the alias
		
		if(field.equals(Versamento.model().COD_VERSAMENTO_ENTE)){
			return this.toTable(Versamento.model(), returnAlias);
		}
		if(field.equals(Versamento.model().COD_DOMINIO)){
			return this.toTable(Versamento.model(), returnAlias);
		}
		if(field.equals(Versamento.model().IUV)){
			return this.toTable(Versamento.model(), returnAlias);
		}
		if(field.equals(Versamento.model().ID_ENTE.COD_ENTE)){
			return this.toTable(Versamento.model().ID_ENTE, returnAlias);
		}
		if(field.equals(Versamento.model().ID_APPLICAZIONE.COD_APPLICAZIONE)){
			return this.toTable(Versamento.model().ID_APPLICAZIONE, returnAlias);
		}
		if(field.equals(Versamento.model().ID_ANAGRAFICA_DEBITORE.ID_ANAGRAFICA)){
			return this.toTable(Versamento.model().ID_ANAGRAFICA_DEBITORE, returnAlias);
		}
		if(field.equals(Versamento.model().ID_ANAGRAFICA_DEBITORE.COD_UNIVOCO)){
			return this.toTable(Versamento.model().ID_ANAGRAFICA_DEBITORE, returnAlias);
		}
		if(field.equals(Versamento.model().IMPORTO_TOTALE)){
			return this.toTable(Versamento.model(), returnAlias);
		}
		if(field.equals(Versamento.model().STATO_VERSAMENTO)){
			return this.toTable(Versamento.model(), returnAlias);
		}
		if(field.equals(Versamento.model().DESCRIZIONE_STATO)){
			return this.toTable(Versamento.model(), returnAlias);
		}
		if(field.equals(Versamento.model().STATO_RENDICONTAZIONE)){
			return this.toTable(Versamento.model(), returnAlias);
		}
		if(field.equals(Versamento.model().IMPORTO_PAGATO)){
			return this.toTable(Versamento.model(), returnAlias);
		}
		if(field.equals(Versamento.model().DATA_SCADENZA)){
			return this.toTable(Versamento.model(), returnAlias);
		}
		if(field.equals(Versamento.model().DATA_ORA_ULTIMO_AGGIORNAMENTO)){
			return this.toTable(Versamento.model(), returnAlias);
		}


		return super.toTable(field,returnAlias);
		
	}

	@Override
	public String toTable(IModel<?> model,boolean returnAlias) throws ExpressionException {
		
		// In the case of table with alias, using parameter returnAlias​​, 
		// it is possible to drive the choice whether to return only the alias or 
		// the full definition of the table containing the alias
		
		if(model.equals(Versamento.model())){
			return "versamenti";
		}
		if(model.equals(Versamento.model().ID_ENTE)){
			return "enti";
		}
		if(model.equals(Versamento.model().ID_APPLICAZIONE)){
			return "applicazioni";
		}
		if(model.equals(Versamento.model().ID_ANAGRAFICA_DEBITORE)){
			return "anagrafiche";
		}


		return super.toTable(model,returnAlias);
		
	}

}
