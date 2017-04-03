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

import it.govpay.orm.Batch;


/**     
 * BatchFieldConverter
 *
 * @author Giovanni Bussu (bussu@link.it)
 * @author Lorenzo Nardi (nardi@link.it)
 * @author $Author$
 * @version $Rev$, $Date$
 */
public class BatchFieldConverter extends AbstractSQLFieldConverter {

	private TipiDatabase databaseType;
	
	public BatchFieldConverter(String databaseType){
		this.databaseType = TipiDatabase.toEnumConstant(databaseType);
	}
	public BatchFieldConverter(TipiDatabase databaseType){
		this.databaseType = databaseType;
	}


	@Override
	public IModel<?> getRootModel() throws ExpressionException {
		return Batch.model();
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
		
		if(field.equals(Batch.model().COD_BATCH)){
			if(appendTablePrefix){
				return this.toAliasTable(field)+".cod_batch";
			}else{
				return "cod_batch";
			}
		}
		if(field.equals(Batch.model().NODO)){
			if(appendTablePrefix){
				return this.toAliasTable(field)+".nodo";
			}else{
				return "nodo";
			}
		}
		if(field.equals(Batch.model().INIZIO)){
			if(appendTablePrefix){
				return this.toAliasTable(field)+".inizio";
			}else{
				return "inizio";
			}
		}
		if(field.equals(Batch.model().AGGIORNAMENTO)){
			if(appendTablePrefix){
				return this.toAliasTable(field)+".aggiornamento";
			}else{
				return "aggiornamento";
			}
		}


		return super.toColumn(field,returnAlias,appendTablePrefix);
		
	}
	
	@Override
	public String toTable(IField field,boolean returnAlias) throws ExpressionException {
		
		// In the case of table with alias, using parameter returnAlias​​, 
		// it is possible to drive the choice whether to return only the alias or 
		// the full definition of the table containing the alias
		
		if(field.equals(Batch.model().COD_BATCH)){
			return this.toTable(Batch.model(), returnAlias);
		}
		if(field.equals(Batch.model().NODO)){
			return this.toTable(Batch.model(), returnAlias);
		}
		if(field.equals(Batch.model().INIZIO)){
			return this.toTable(Batch.model(), returnAlias);
		}
		if(field.equals(Batch.model().AGGIORNAMENTO)){
			return this.toTable(Batch.model(), returnAlias);
		}


		return super.toTable(field,returnAlias);
		
	}

	@Override
	public String toTable(IModel<?> model,boolean returnAlias) throws ExpressionException {
		
		// In the case of table with alias, using parameter returnAlias​​, 
		// it is possible to drive the choice whether to return only the alias or 
		// the full definition of the table containing the alias
		
		if(model.equals(Batch.model())){
			return "batch";
		}


		return super.toTable(model,returnAlias);
		
	}

}
