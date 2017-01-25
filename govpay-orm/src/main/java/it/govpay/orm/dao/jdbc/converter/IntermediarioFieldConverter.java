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

import it.govpay.orm.Intermediario;


/**     
 * IntermediarioFieldConverter
 *
 * @author Giovanni Bussu (bussu@link.it)
 * @author Lorenzo Nardi (nardi@link.it)
 * @author $Author$
 * @version $Rev$, $Date$
 */
public class IntermediarioFieldConverter extends AbstractSQLFieldConverter {

	private TipiDatabase databaseType;
	
	public IntermediarioFieldConverter(String databaseType){
		this.databaseType = TipiDatabase.toEnumConstant(databaseType);
	}
	public IntermediarioFieldConverter(TipiDatabase databaseType){
		this.databaseType = databaseType;
	}


	@Override
	public IModel<?> getRootModel() throws ExpressionException {
		return Intermediario.model();
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
		
		if(field.equals(Intermediario.model().COD_INTERMEDIARIO)){
			if(appendTablePrefix){
				return this.toAliasTable(field)+".cod_intermediario";
			}else{
				return "cod_intermediario";
			}
		}
		if(field.equals(Intermediario.model().COD_CONNETTORE_PDD)){
			if(appendTablePrefix){
				return this.toAliasTable(field)+".cod_connettore_pdd";
			}else{
				return "cod_connettore_pdd";
			}
		}
		if(field.equals(Intermediario.model().DENOMINAZIONE)){
			if(appendTablePrefix){
				return this.toAliasTable(field)+".denominazione";
			}else{
				return "denominazione";
			}
		}
		if(field.equals(Intermediario.model().ABILITATO)){
			if(appendTablePrefix){
				return this.toAliasTable(field)+".abilitato";
			}else{
				return "abilitato";
			}
		}
		if(field.equals(Intermediario.model().SEGREGATION_CODE)){
			if(appendTablePrefix){
				return this.toAliasTable(field)+".segregation_code";
			}else{
				return "segregation_code";
			}
		}


		return super.toColumn(field,returnAlias,appendTablePrefix);
		
	}
	
	@Override
	public String toTable(IField field,boolean returnAlias) throws ExpressionException {
		
		// In the case of table with alias, using parameter returnAlias​​, 
		// it is possible to drive the choice whether to return only the alias or 
		// the full definition of the table containing the alias
		
		if(field.equals(Intermediario.model().COD_INTERMEDIARIO)){
			return this.toTable(Intermediario.model(), returnAlias);
		}
		if(field.equals(Intermediario.model().COD_CONNETTORE_PDD)){
			return this.toTable(Intermediario.model(), returnAlias);
		}
		if(field.equals(Intermediario.model().DENOMINAZIONE)){
			return this.toTable(Intermediario.model(), returnAlias);
		}
		if(field.equals(Intermediario.model().ABILITATO)){
			return this.toTable(Intermediario.model(), returnAlias);
		}
		if(field.equals(Intermediario.model().SEGREGATION_CODE)){
			return this.toTable(Intermediario.model(), returnAlias);
		}


		return super.toTable(field,returnAlias);
		
	}

	@Override
	public String toTable(IModel<?> model,boolean returnAlias) throws ExpressionException {
		
		// In the case of table with alias, using parameter returnAlias​​, 
		// it is possible to drive the choice whether to return only the alias or 
		// the full definition of the table containing the alias
		
		if(model.equals(Intermediario.model())){
			return "intermediari";
		}


		return super.toTable(model,returnAlias);
		
	}

}
