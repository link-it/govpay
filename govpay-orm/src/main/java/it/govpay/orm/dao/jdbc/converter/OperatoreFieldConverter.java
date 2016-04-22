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

import it.govpay.orm.Operatore;


/**     
 * OperatoreFieldConverter
 *
 * @author Giovanni Bussu (bussu@link.it)
 * @author Lorenzo Nardi (nardi@link.it)
 * @author $Author$
 * @version $Rev$, $Date$
 */
public class OperatoreFieldConverter extends AbstractSQLFieldConverter {

	private TipiDatabase databaseType;
	
	public OperatoreFieldConverter(String databaseType){
		this.databaseType = TipiDatabase.toEnumConstant(databaseType);
	}
	public OperatoreFieldConverter(TipiDatabase databaseType){
		this.databaseType = databaseType;
	}


	@Override
	public IModel<?> getRootModel() throws ExpressionException {
		return Operatore.model();
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
		
		if(field.equals(Operatore.model().PRINCIPAL)){
			if(appendTablePrefix){
				return this.toAliasTable(field)+".principal";
			}else{
				return "principal";
			}
		}
		if(field.equals(Operatore.model().NOME)){
			if(appendTablePrefix){
				return this.toAliasTable(field)+".nome";
			}else{
				return "nome";
			}
		}
		if(field.equals(Operatore.model().PROFILO)){
			if(appendTablePrefix){
				return this.toAliasTable(field)+".profilo";
			}else{
				return "profilo";
			}
		}
		if(field.equals(Operatore.model().ABILITATO)){
			if(appendTablePrefix){
				return this.toAliasTable(field)+".abilitato";
			}else{
				return "abilitato";
			}
		}
		if(field.equals(Operatore.model().OPERATORE_UO.ID_UO.COD_UO)){
			if(appendTablePrefix){
				return this.toAliasTable(field)+".cod_uo";
			}else{
				return "cod_uo";
			}
		}
		if(field.equals(Operatore.model().OPERATORE_UO.ID_UO.ID_DOMINIO.COD_DOMINIO)){
			if(appendTablePrefix){
				return this.toAliasTable(field)+".cod_dominio";
			}else{
				return "cod_dominio";
			}
		}
		if(field.equals(Operatore.model().OPERATORE_APPLICAZIONE.ID_APPLICAZIONE.COD_APPLICAZIONE)){
			if(appendTablePrefix){
				return this.toAliasTable(field)+".cod_applicazione";
			}else{
				return "cod_applicazione";
			}
		}
		if(field.equals(Operatore.model().OPERATORE_PORTALE.ID_PORTALE.COD_PORTALE)){
			if(appendTablePrefix){
				return this.toAliasTable(field)+".cod_portale";
			}else{
				return "cod_portale";
			}
		}


		return super.toColumn(field,returnAlias,appendTablePrefix);
		
	}
	
	@Override
	public String toTable(IField field,boolean returnAlias) throws ExpressionException {
		
		// In the case of table with alias, using parameter returnAlias​​, 
		// it is possible to drive the choice whether to return only the alias or 
		// the full definition of the table containing the alias
		
		if(field.equals(Operatore.model().PRINCIPAL)){
			return this.toTable(Operatore.model(), returnAlias);
		}
		if(field.equals(Operatore.model().NOME)){
			return this.toTable(Operatore.model(), returnAlias);
		}
		if(field.equals(Operatore.model().PROFILO)){
			return this.toTable(Operatore.model(), returnAlias);
		}
		if(field.equals(Operatore.model().ABILITATO)){
			return this.toTable(Operatore.model(), returnAlias);
		}
		if(field.equals(Operatore.model().OPERATORE_UO.ID_UO.COD_UO)){
			return this.toTable(Operatore.model().OPERATORE_UO.ID_UO, returnAlias);
		}
		if(field.equals(Operatore.model().OPERATORE_UO.ID_UO.ID_DOMINIO.COD_DOMINIO)){
			return this.toTable(Operatore.model().OPERATORE_UO.ID_UO.ID_DOMINIO, returnAlias);
		}
		if(field.equals(Operatore.model().OPERATORE_APPLICAZIONE.ID_APPLICAZIONE.COD_APPLICAZIONE)){
			return this.toTable(Operatore.model().OPERATORE_APPLICAZIONE.ID_APPLICAZIONE, returnAlias);
		}
		if(field.equals(Operatore.model().OPERATORE_PORTALE.ID_PORTALE.COD_PORTALE)){
			return this.toTable(Operatore.model().OPERATORE_PORTALE.ID_PORTALE, returnAlias);
		}


		return super.toTable(field,returnAlias);
		
	}

	@Override
	public String toTable(IModel<?> model,boolean returnAlias) throws ExpressionException {
		
		// In the case of table with alias, using parameter returnAlias​​, 
		// it is possible to drive the choice whether to return only the alias or 
		// the full definition of the table containing the alias
		
		if(model.equals(Operatore.model())){
			return "operatori";
		}
		if(model.equals(Operatore.model().OPERATORE_UO)){
			return "operatori_uo";
		}
		if(model.equals(Operatore.model().OPERATORE_UO.ID_UO)){
			return "uo";
		}
		if(model.equals(Operatore.model().OPERATORE_UO.ID_UO.ID_DOMINIO)){
			return "id_dominio";
		}
		if(model.equals(Operatore.model().OPERATORE_APPLICAZIONE)){
			return "operatori_applicazioni";
		}
		if(model.equals(Operatore.model().OPERATORE_APPLICAZIONE.ID_APPLICAZIONE)){
			return "applicazioni";
		}
		if(model.equals(Operatore.model().OPERATORE_PORTALE)){
			return "operatori_portali";
		}
		if(model.equals(Operatore.model().OPERATORE_PORTALE.ID_PORTALE)){
			return "portali";
		}


		return super.toTable(model,returnAlias);
		
	}

}
