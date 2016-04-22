/*
 * GovPay - Porta di Accesso al Nodo dei Pagamenti SPC 
 * http://www.gov4j.it/govpay
 * 
 * Copyright (c) 2014-2016 Link.it srl (http://www.link.it).
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

import it.govpay.orm.Psp;


/**     
 * PspFieldConverter
 *
 * @author Giovanni Bussu (bussu@link.it)
 * @author Lorenzo Nardi (nardi@link.it)
 * @author $Author$
 * @version $Rev$, $Date$
 */
public class PspFieldConverter extends AbstractSQLFieldConverter {

	private TipiDatabase databaseType;
	
	public PspFieldConverter(String databaseType){
		this.databaseType = TipiDatabase.toEnumConstant(databaseType);
	}
	public PspFieldConverter(TipiDatabase databaseType){
		this.databaseType = databaseType;
	}


	@Override
	public IModel<?> getRootModel() throws ExpressionException {
		return Psp.model();
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
		
		if(field.equals(Psp.model().COD_PSP)){
			if(appendTablePrefix){
				return this.toAliasTable(field)+".cod_psp";
			}else{
				return "cod_psp";
			}
		}
		if(field.equals(Psp.model().RAGIONE_SOCIALE)){
			if(appendTablePrefix){
				return this.toAliasTable(field)+".ragione_sociale";
			}else{
				return "ragione_sociale";
			}
		}
		if(field.equals(Psp.model().URL_INFO)){
			if(appendTablePrefix){
				return this.toAliasTable(field)+".url_info";
			}else{
				return "url_info";
			}
		}
		if(field.equals(Psp.model().ABILITATO)){
			if(appendTablePrefix){
				return this.toAliasTable(field)+".abilitato";
			}else{
				return "abilitato";
			}
		}
		if(field.equals(Psp.model().STORNO)){
			if(appendTablePrefix){
				return this.toAliasTable(field)+".storno";
			}else{
				return "storno";
			}
		}
		if(field.equals(Psp.model().MARCA_BOLLO)){
			if(appendTablePrefix){
				return this.toAliasTable(field)+".marca_bollo";
			}else{
				return "marca_bollo";
			}
		}


		return super.toColumn(field,returnAlias,appendTablePrefix);
		
	}
	
	@Override
	public String toTable(IField field,boolean returnAlias) throws ExpressionException {
		
		// In the case of table with alias, using parameter returnAlias​​, 
		// it is possible to drive the choice whether to return only the alias or 
		// the full definition of the table containing the alias
		
		if(field.equals(Psp.model().COD_PSP)){
			return this.toTable(Psp.model(), returnAlias);
		}
		if(field.equals(Psp.model().RAGIONE_SOCIALE)){
			return this.toTable(Psp.model(), returnAlias);
		}
		if(field.equals(Psp.model().URL_INFO)){
			return this.toTable(Psp.model(), returnAlias);
		}
		if(field.equals(Psp.model().ABILITATO)){
			return this.toTable(Psp.model(), returnAlias);
		}
		if(field.equals(Psp.model().STORNO)){
			return this.toTable(Psp.model(), returnAlias);
		}
		if(field.equals(Psp.model().MARCA_BOLLO)){
			return this.toTable(Psp.model(), returnAlias);
		}


		return super.toTable(field,returnAlias);
		
	}

	@Override
	public String toTable(IModel<?> model,boolean returnAlias) throws ExpressionException {
		
		// In the case of table with alias, using parameter returnAlias​​, 
		// it is possible to drive the choice whether to return only the alias or 
		// the full definition of the table containing the alias
		
		if(model.equals(Psp.model())){
			return "psp";
		}


		return super.toTable(model,returnAlias);
		
	}

}
