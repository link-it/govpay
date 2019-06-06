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

import it.govpay.orm.Audit;


/**     
 * AuditFieldConverter
 *
 * @author Giovanni Bussu (bussu@link.it)
 * @author Lorenzo Nardi (nardi@link.it)
 * @author $Author$
 * @version $Rev$, $Date$
 */
public class AuditFieldConverter extends AbstractSQLFieldConverter {

	private TipiDatabase databaseType;
	
	public AuditFieldConverter(String databaseType){
		this.databaseType = TipiDatabase.toEnumConstant(databaseType);
	}
	public AuditFieldConverter(TipiDatabase databaseType){
		this.databaseType = databaseType;
	}


	@Override
	public IModel<?> getRootModel() throws ExpressionException {
		return Audit.model();
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
		
		if(field.equals(Audit.model().DATA)){
			if(appendTablePrefix){
				return this.toAliasTable(field)+".data";
			}else{
				return "data";
			}
		}
		if(field.equals(Audit.model().ID_OPERATORE.PRINCIPAL)){
			if(appendTablePrefix){
				return this.toAliasTable(field)+".principal";
			}else{
				return "principal";
			}
		}
		if(field.equals(Audit.model().ID_OGGETTO)){
			if(appendTablePrefix){
				return this.toAliasTable(field)+".id_oggetto";
			}else{
				return "id_oggetto";
			}
		}
		if(field.equals(Audit.model().TIPO_OGGETTO)){
			if(appendTablePrefix){
				return this.toAliasTable(field)+".tipo_oggetto";
			}else{
				return "tipo_oggetto";
			}
		}
		if(field.equals(Audit.model().OGGETTO)){
			if(appendTablePrefix){
				return this.toAliasTable(field)+".oggetto";
			}else{
				return "oggetto";
			}
		}


		return super.toColumn(field,returnAlias,appendTablePrefix);
		
	}
	
	@Override
	public String toTable(IField field,boolean returnAlias) throws ExpressionException {
		
		// In the case of table with alias, using parameter returnAlias​​, 
		// it is possible to drive the choice whether to return only the alias or 
		// the full definition of the table containing the alias
		
		if(field.equals(Audit.model().DATA)){
			return this.toTable(Audit.model(), returnAlias);
		}
		if(field.equals(Audit.model().ID_OPERATORE.PRINCIPAL)){
			return this.toTable(Audit.model().ID_OPERATORE, returnAlias);
		}
		if(field.equals(Audit.model().ID_OGGETTO)){
			return this.toTable(Audit.model(), returnAlias);
		}
		if(field.equals(Audit.model().TIPO_OGGETTO)){
			return this.toTable(Audit.model(), returnAlias);
		}
		if(field.equals(Audit.model().OGGETTO)){
			return this.toTable(Audit.model(), returnAlias);
		}


		return super.toTable(field,returnAlias);
		
	}

	@Override
	public String toTable(IModel<?> model,boolean returnAlias) throws ExpressionException {
		
		// In the case of table with alias, using parameter returnAlias​​, 
		// it is possible to drive the choice whether to return only the alias or 
		// the full definition of the table containing the alias
		
		if(model.equals(Audit.model())){
			return "gp_audit";
		}
		if(model.equals(Audit.model().ID_OPERATORE)){
			return "operatori";
		}


		return super.toTable(model,returnAlias);
		
	}

}
