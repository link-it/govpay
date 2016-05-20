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

import it.govpay.orm.Uo;


/**     
 * UoFieldConverter
 *
 * @author Giovanni Bussu (bussu@link.it)
 * @author Lorenzo Nardi (nardi@link.it)
 * @author $Author$
 * @version $Rev$, $Date$
 */
public class UoFieldConverter extends AbstractSQLFieldConverter {

	private TipiDatabase databaseType;
	
	public UoFieldConverter(String databaseType){
		this.databaseType = TipiDatabase.toEnumConstant(databaseType);
	}
	public UoFieldConverter(TipiDatabase databaseType){
		this.databaseType = databaseType;
	}


	@Override
	public IModel<?> getRootModel() throws ExpressionException {
		return Uo.model();
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
		
		if(field.equals(Uo.model().COD_UO)){
			if(appendTablePrefix){
				return this.toAliasTable(field)+".cod_uo";
			}else{
				return "cod_uo";
			}
		}
		if(field.equals(Uo.model().ID_DOMINIO.COD_DOMINIO)){
			if(appendTablePrefix){
				return this.toAliasTable(field)+".cod_dominio";
			}else{
				return "cod_dominio";
			}
		}
		if(field.equals(Uo.model().ABILITATO)){
			if(appendTablePrefix){
				return this.toAliasTable(field)+".abilitato";
			}else{
				return "abilitato";
			}
		}
		if(field.equals(Uo.model().UO_CODICE_IDENTIFICATIVO)){
			if(appendTablePrefix){
				return this.toAliasTable(field)+".uo_codice_identificativo";
			}else{
				return "uo_codice_identificativo";
			}
		}
		if(field.equals(Uo.model().UO_DENOMINAZIONE)){
			if(appendTablePrefix){
				return this.toAliasTable(field)+".uo_denominazione";
			}else{
				return "uo_denominazione";
			}
		}
		if(field.equals(Uo.model().UO_INDIRIZZO)){
			if(appendTablePrefix){
				return this.toAliasTable(field)+".uo_indirizzo";
			}else{
				return "uo_indirizzo";
			}
		}
		if(field.equals(Uo.model().UO_CIVICO)){
			if(appendTablePrefix){
				return this.toAliasTable(field)+".uo_civico";
			}else{
				return "uo_civico";
			}
		}
		if(field.equals(Uo.model().UO_CAP)){
			if(appendTablePrefix){
				return this.toAliasTable(field)+".uo_cap";
			}else{
				return "uo_cap";
			}
		}
		if(field.equals(Uo.model().UO_LOCALITA)){
			if(appendTablePrefix){
				return this.toAliasTable(field)+".uo_localita";
			}else{
				return "uo_localita";
			}
		}
		if(field.equals(Uo.model().UO_PROVINCIA)){
			if(appendTablePrefix){
				return this.toAliasTable(field)+".uo_provincia";
			}else{
				return "uo_provincia";
			}
		}
		if(field.equals(Uo.model().UO_NAZIONE)){
			if(appendTablePrefix){
				return this.toAliasTable(field)+".uo_nazione";
			}else{
				return "uo_nazione";
			}
		}


		return super.toColumn(field,returnAlias,appendTablePrefix);
		
	}
	
	@Override
	public String toTable(IField field,boolean returnAlias) throws ExpressionException {
		
		// In the case of table with alias, using parameter returnAlias​​, 
		// it is possible to drive the choice whether to return only the alias or 
		// the full definition of the table containing the alias
		
		if(field.equals(Uo.model().COD_UO)){
			return this.toTable(Uo.model(), returnAlias);
		}
		if(field.equals(Uo.model().ID_DOMINIO.COD_DOMINIO)){
			return this.toTable(Uo.model().ID_DOMINIO, returnAlias);
		}
		if(field.equals(Uo.model().ABILITATO)){
			return this.toTable(Uo.model(), returnAlias);
		}
		if(field.equals(Uo.model().UO_CODICE_IDENTIFICATIVO)){
			return this.toTable(Uo.model(), returnAlias);
		}
		if(field.equals(Uo.model().UO_DENOMINAZIONE)){
			return this.toTable(Uo.model(), returnAlias);
		}
		if(field.equals(Uo.model().UO_INDIRIZZO)){
			return this.toTable(Uo.model(), returnAlias);
		}
		if(field.equals(Uo.model().UO_CIVICO)){
			return this.toTable(Uo.model(), returnAlias);
		}
		if(field.equals(Uo.model().UO_CAP)){
			return this.toTable(Uo.model(), returnAlias);
		}
		if(field.equals(Uo.model().UO_LOCALITA)){
			return this.toTable(Uo.model(), returnAlias);
		}
		if(field.equals(Uo.model().UO_PROVINCIA)){
			return this.toTable(Uo.model(), returnAlias);
		}
		if(field.equals(Uo.model().UO_NAZIONE)){
			return this.toTable(Uo.model(), returnAlias);
		}


		return super.toTable(field,returnAlias);
		
	}

	@Override
	public String toTable(IModel<?> model,boolean returnAlias) throws ExpressionException {
		
		// In the case of table with alias, using parameter returnAlias​​, 
		// it is possible to drive the choice whether to return only the alias or 
		// the full definition of the table containing the alias
		
		if(model.equals(Uo.model())){
			return "uo";
		}
		if(model.equals(Uo.model().ID_DOMINIO)){
			return "domini";
		}


		return super.toTable(model,returnAlias);
		
	}

}
