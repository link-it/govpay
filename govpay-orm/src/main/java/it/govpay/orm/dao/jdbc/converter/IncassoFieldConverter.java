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
package it.govpay.orm.dao.jdbc.converter;

import org.openspcoop2.generic_project.beans.IField;
import org.openspcoop2.generic_project.beans.IModel;
import org.openspcoop2.generic_project.exception.ExpressionException;
import org.openspcoop2.generic_project.expression.impl.sql.AbstractSQLFieldConverter;
import org.openspcoop2.utils.TipiDatabase;

import it.govpay.orm.Incasso;


/**     
 * IncassoFieldConverter
 *
 * @author Giovanni Bussu (bussu@link.it)
 * @author Lorenzo Nardi (nardi@link.it)
 * @author $Author$
 * @version $Rev$, $Date$
 */
public class IncassoFieldConverter extends AbstractSQLFieldConverter {

	private TipiDatabase databaseType;
	
	public IncassoFieldConverter(String databaseType){
		this.databaseType = TipiDatabase.toEnumConstant(databaseType);
	}
	public IncassoFieldConverter(TipiDatabase databaseType){
		this.databaseType = databaseType;
	}


	@Override
	public IModel<?> getRootModel() throws ExpressionException {
		return Incasso.model();
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
		
		if(field.equals(Incasso.model().TRN)){
			if(appendTablePrefix){
				return this.toAliasTable(field)+".trn";
			}else{
				return "trn";
			}
		}
		if(field.equals(Incasso.model().COD_DOMINIO)){
			if(appendTablePrefix){
				return this.toAliasTable(field)+".cod_dominio";
			}else{
				return "cod_dominio";
			}
		}
		if(field.equals(Incasso.model().CAUSALE)){
			if(appendTablePrefix){
				return this.toAliasTable(field)+".causale";
			}else{
				return "causale";
			}
		}
		if(field.equals(Incasso.model().IMPORTO)){
			if(appendTablePrefix){
				return this.toAliasTable(field)+".importo";
			}else{
				return "importo";
			}
		}
		if(field.equals(Incasso.model().DATA_VALUTA)){
			if(appendTablePrefix){
				return this.toAliasTable(field)+".data_valuta";
			}else{
				return "data_valuta";
			}
		}
		if(field.equals(Incasso.model().DATA_CONTABILE)){
			if(appendTablePrefix){
				return this.toAliasTable(field)+".data_contabile";
			}else{
				return "data_contabile";
			}
		}
		if(field.equals(Incasso.model().DATA_ORA_INCASSO)){
			if(appendTablePrefix){
				return this.toAliasTable(field)+".data_ora_incasso";
			}else{
				return "data_ora_incasso";
			}
		}
		if(field.equals(Incasso.model().NOME_DISPOSITIVO)){
			if(appendTablePrefix){
				return this.toAliasTable(field)+".nome_dispositivo";
			}else{
				return "nome_dispositivo";
			}
		}
		if(field.equals(Incasso.model().ID_APPLICAZIONE.COD_APPLICAZIONE)){
			if(appendTablePrefix){
				return this.toAliasTable(field)+".cod_applicazione";
			}else{
				return "cod_applicazione";
			}
		}
		if(field.equals(Incasso.model().ID_OPERATORE.PRINCIPAL)){
			if(appendTablePrefix){
				return this.toAliasTable(field)+".principal";
			}else{
				return "principal";
			}
		}


		return super.toColumn(field,returnAlias,appendTablePrefix);
		
	}
	
	@Override
	public String toTable(IField field,boolean returnAlias) throws ExpressionException {
		
		// In the case of table with alias, using parameter returnAlias​​, 
		// it is possible to drive the choice whether to return only the alias or 
		// the full definition of the table containing the alias
		
		if(field.equals(Incasso.model().TRN)){
			return this.toTable(Incasso.model(), returnAlias);
		}
		if(field.equals(Incasso.model().COD_DOMINIO)){
			return this.toTable(Incasso.model(), returnAlias);
		}
		if(field.equals(Incasso.model().CAUSALE)){
			return this.toTable(Incasso.model(), returnAlias);
		}
		if(field.equals(Incasso.model().IMPORTO)){
			return this.toTable(Incasso.model(), returnAlias);
		}
		if(field.equals(Incasso.model().DATA_VALUTA)){
			return this.toTable(Incasso.model(), returnAlias);
		}
		if(field.equals(Incasso.model().DATA_CONTABILE)){
			return this.toTable(Incasso.model(), returnAlias);
		}
		if(field.equals(Incasso.model().DATA_ORA_INCASSO)){
			return this.toTable(Incasso.model(), returnAlias);
		}
		if(field.equals(Incasso.model().NOME_DISPOSITIVO)){
			return this.toTable(Incasso.model(), returnAlias);
		}
		if(field.equals(Incasso.model().ID_APPLICAZIONE.COD_APPLICAZIONE)){
			return this.toTable(Incasso.model().ID_APPLICAZIONE, returnAlias);
		}
		if(field.equals(Incasso.model().ID_OPERATORE.PRINCIPAL)){
			return this.toTable(Incasso.model().ID_OPERATORE, returnAlias);
		}


		return super.toTable(field,returnAlias);
		
	}

	@Override
	public String toTable(IModel<?> model,boolean returnAlias) throws ExpressionException {
		
		// In the case of table with alias, using parameter returnAlias​​, 
		// it is possible to drive the choice whether to return only the alias or 
		// the full definition of the table containing the alias
		
		if(model.equals(Incasso.model())){
			return "incassi";
		}
		if(model.equals(Incasso.model().ID_APPLICAZIONE)){
			return "applicazioni";
		}
		if(model.equals(Incasso.model().ID_OPERATORE)){
			return "operatori";
		}


		return super.toTable(model,returnAlias);
		
	}

}
