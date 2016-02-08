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

import it.govpay.orm.Anagrafica;


/**     
 * AnagraficaFieldConverter
 *
 * @author Giovanni Bussu (bussu@link.it)
 * @author Lorenzo Nardi (nardi@link.it)
 * @author $Author$
 * @version $Rev$, $Date$
 */
public class AnagraficaFieldConverter extends AbstractSQLFieldConverter {

	private TipiDatabase databaseType;
	
	public AnagraficaFieldConverter(String databaseType){
		this.databaseType = TipiDatabase.toEnumConstant(databaseType);
	}
	public AnagraficaFieldConverter(TipiDatabase databaseType){
		this.databaseType = databaseType;
	}


	@Override
	public IModel<?> getRootModel() throws ExpressionException {
		return Anagrafica.model();
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
		
		if(field.equals(Anagrafica.model().RAGIONE_SOCIALE)){
			if(appendTablePrefix){
				return this.toAliasTable(field)+".ragione_sociale";
			}else{
				return "ragione_sociale";
			}
		}
		if(field.equals(Anagrafica.model().COD_UNIVOCO)){
			if(appendTablePrefix){
				return this.toAliasTable(field)+".cod_univoco";
			}else{
				return "cod_univoco";
			}
		}
		if(field.equals(Anagrafica.model().INDIRIZZO)){
			if(appendTablePrefix){
				return this.toAliasTable(field)+".indirizzo";
			}else{
				return "indirizzo";
			}
		}
		if(field.equals(Anagrafica.model().CIVICO)){
			if(appendTablePrefix){
				return this.toAliasTable(field)+".civico";
			}else{
				return "civico";
			}
		}
		if(field.equals(Anagrafica.model().CAP)){
			if(appendTablePrefix){
				return this.toAliasTable(field)+".cap";
			}else{
				return "cap";
			}
		}
		if(field.equals(Anagrafica.model().LOCALITA)){
			if(appendTablePrefix){
				return this.toAliasTable(field)+".localita";
			}else{
				return "localita";
			}
		}
		if(field.equals(Anagrafica.model().PROVINCIA)){
			if(appendTablePrefix){
				return this.toAliasTable(field)+".provincia";
			}else{
				return "provincia";
			}
		}
		if(field.equals(Anagrafica.model().NAZIONE)){
			if(appendTablePrefix){
				return this.toAliasTable(field)+".nazione";
			}else{
				return "nazione";
			}
		}
		if(field.equals(Anagrafica.model().EMAIL)){
			if(appendTablePrefix){
				return this.toAliasTable(field)+".email";
			}else{
				return "email";
			}
		}
		if(field.equals(Anagrafica.model().TELEFONO)){
			if(appendTablePrefix){
				return this.toAliasTable(field)+".telefono";
			}else{
				return "telefono";
			}
		}
		if(field.equals(Anagrafica.model().CELLULARE)){
			if(appendTablePrefix){
				return this.toAliasTable(field)+".cellulare";
			}else{
				return "cellulare";
			}
		}
		if(field.equals(Anagrafica.model().FAX)){
			if(appendTablePrefix){
				return this.toAliasTable(field)+".fax";
			}else{
				return "fax";
			}
		}


		return super.toColumn(field,returnAlias,appendTablePrefix);
		
	}
	
	@Override
	public String toTable(IField field,boolean returnAlias) throws ExpressionException {
		
		// In the case of table with alias, using parameter returnAlias​​, 
		// it is possible to drive the choice whether to return only the alias or 
		// the full definition of the table containing the alias
		
		if(field.equals(Anagrafica.model().RAGIONE_SOCIALE)){
			return this.toTable(Anagrafica.model(), returnAlias);
		}
		if(field.equals(Anagrafica.model().COD_UNIVOCO)){
			return this.toTable(Anagrafica.model(), returnAlias);
		}
		if(field.equals(Anagrafica.model().INDIRIZZO)){
			return this.toTable(Anagrafica.model(), returnAlias);
		}
		if(field.equals(Anagrafica.model().CIVICO)){
			return this.toTable(Anagrafica.model(), returnAlias);
		}
		if(field.equals(Anagrafica.model().CAP)){
			return this.toTable(Anagrafica.model(), returnAlias);
		}
		if(field.equals(Anagrafica.model().LOCALITA)){
			return this.toTable(Anagrafica.model(), returnAlias);
		}
		if(field.equals(Anagrafica.model().PROVINCIA)){
			return this.toTable(Anagrafica.model(), returnAlias);
		}
		if(field.equals(Anagrafica.model().NAZIONE)){
			return this.toTable(Anagrafica.model(), returnAlias);
		}
		if(field.equals(Anagrafica.model().EMAIL)){
			return this.toTable(Anagrafica.model(), returnAlias);
		}
		if(field.equals(Anagrafica.model().TELEFONO)){
			return this.toTable(Anagrafica.model(), returnAlias);
		}
		if(field.equals(Anagrafica.model().CELLULARE)){
			return this.toTable(Anagrafica.model(), returnAlias);
		}
		if(field.equals(Anagrafica.model().FAX)){
			return this.toTable(Anagrafica.model(), returnAlias);
		}


		return super.toTable(field,returnAlias);
		
	}

	@Override
	public String toTable(IModel<?> model,boolean returnAlias) throws ExpressionException {
		
		// In the case of table with alias, using parameter returnAlias​​, 
		// it is possible to drive the choice whether to return only the alias or 
		// the full definition of the table containing the alias
		
		if(model.equals(Anagrafica.model())){
			return "anagrafiche";
		}


		return super.toTable(model,returnAlias);
		
	}

}
