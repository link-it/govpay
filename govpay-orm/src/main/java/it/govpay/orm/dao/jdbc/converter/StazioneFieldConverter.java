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

import it.govpay.orm.Stazione;


/**     
 * StazioneFieldConverter
 *
 * @author Giovanni Bussu (bussu@link.it)
 * @author Lorenzo Nardi (nardi@link.it)
 * @author $Author$
 * @version $Rev$, $Date$
 */
public class StazioneFieldConverter extends AbstractSQLFieldConverter {

	private TipiDatabase databaseType;
	
	public StazioneFieldConverter(String databaseType){
		this.databaseType = TipiDatabase.toEnumConstant(databaseType);
	}
	public StazioneFieldConverter(TipiDatabase databaseType){
		this.databaseType = databaseType;
	}


	@Override
	public IModel<?> getRootModel() throws ExpressionException {
		return Stazione.model();
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
		
		if(field.equals(Stazione.model().ID_INTERMEDIARIO.COD_INTERMEDIARIO)){
			if(appendTablePrefix){
				return this.toAliasTable(field)+".cod_intermediario";
			}else{
				return "cod_intermediario";
			}
		}
		if(field.equals(Stazione.model().COD_STAZIONE)){
			if(appendTablePrefix){
				return this.toAliasTable(field)+".cod_stazione";
			}else{
				return "cod_stazione";
			}
		}
		if(field.equals(Stazione.model().PASSWORD)){
			if(appendTablePrefix){
				return this.toAliasTable(field)+".password";
			}else{
				return "password";
			}
		}
		if(field.equals(Stazione.model().ABILITATO)){
			if(appendTablePrefix){
				return this.toAliasTable(field)+".abilitato";
			}else{
				return "abilitato";
			}
		}
		if(field.equals(Stazione.model().APPLICATION_CODE)){
			if(appendTablePrefix){
				return this.toAliasTable(field)+".application_code";
			}else{
				return "application_code";
			}
		}


		return super.toColumn(field,returnAlias,appendTablePrefix);
		
	}
	
	@Override
	public String toTable(IField field,boolean returnAlias) throws ExpressionException {
		
		// In the case of table with alias, using parameter returnAlias​​, 
		// it is possible to drive the choice whether to return only the alias or 
		// the full definition of the table containing the alias
		
		if(field.equals(Stazione.model().ID_INTERMEDIARIO.COD_INTERMEDIARIO)){
			return this.toTable(Stazione.model().ID_INTERMEDIARIO, returnAlias);
		}
		if(field.equals(Stazione.model().COD_STAZIONE)){
			return this.toTable(Stazione.model(), returnAlias);
		}
		if(field.equals(Stazione.model().PASSWORD)){
			return this.toTable(Stazione.model(), returnAlias);
		}
		if(field.equals(Stazione.model().ABILITATO)){
			return this.toTable(Stazione.model(), returnAlias);
		}
		if(field.equals(Stazione.model().APPLICATION_CODE)){
			return this.toTable(Stazione.model(), returnAlias);
		}


		return super.toTable(field,returnAlias);
		
	}

	@Override
	public String toTable(IModel<?> model,boolean returnAlias) throws ExpressionException {
		
		// In the case of table with alias, using parameter returnAlias​​, 
		// it is possible to drive the choice whether to return only the alias or 
		// the full definition of the table containing the alias
		
		if(model.equals(Stazione.model())){
			return "stazioni";
		}
		if(model.equals(Stazione.model().ID_INTERMEDIARIO)){
			return "intermediari";
		}


		return super.toTable(model,returnAlias);
		
	}

}
