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

import it.govpay.orm.Canale;


/**     
 * CanaleFieldConverter
 *
 * @author Giovanni Bussu (bussu@link.it)
 * @author Lorenzo Nardi (nardi@link.it)
 * @author $Author$
 * @version $Rev$, $Date$
 */
public class CanaleFieldConverter extends AbstractSQLFieldConverter {

	private TipiDatabase databaseType;
	
	public CanaleFieldConverter(String databaseType){
		this.databaseType = TipiDatabase.toEnumConstant(databaseType);
	}
	public CanaleFieldConverter(TipiDatabase databaseType){
		this.databaseType = databaseType;
	}


	@Override
	public IModel<?> getRootModel() throws ExpressionException {
		return Canale.model();
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
		
		if(field.equals(Canale.model().ID_PSP.COD_PSP)){
			if(appendTablePrefix){
				return this.toAliasTable(field)+".cod_psp";
			}else{
				return "cod_psp";
			}
		}
		if(field.equals(Canale.model().COD_CANALE)){
			if(appendTablePrefix){
				return this.toAliasTable(field)+".cod_canale";
			}else{
				return "cod_canale";
			}
		}
		if(field.equals(Canale.model().COD_INTERMEDIARIO)){
			if(appendTablePrefix){
				return this.toAliasTable(field)+".cod_intermediario";
			}else{
				return "cod_intermediario";
			}
		}
		if(field.equals(Canale.model().TIPO_VERSAMENTO)){
			if(appendTablePrefix){
				return this.toAliasTable(field)+".tipo_versamento";
			}else{
				return "tipo_versamento";
			}
		}
		if(field.equals(Canale.model().MODELLO_PAGAMENTO)){
			if(appendTablePrefix){
				return this.toAliasTable(field)+".modello_pagamento";
			}else{
				return "modello_pagamento";
			}
		}
		if(field.equals(Canale.model().DISPONIBILITA)){
			if(appendTablePrefix){
				return this.toAliasTable(field)+".disponibilita";
			}else{
				return "disponibilita";
			}
		}
		if(field.equals(Canale.model().DESCRIZIONE)){
			if(appendTablePrefix){
				return this.toAliasTable(field)+".descrizione";
			}else{
				return "descrizione";
			}
		}
		if(field.equals(Canale.model().CONDIZIONI)){
			if(appendTablePrefix){
				return this.toAliasTable(field)+".condizioni";
			}else{
				return "condizioni";
			}
		}
		if(field.equals(Canale.model().URL_INFO)){
			if(appendTablePrefix){
				return this.toAliasTable(field)+".url_info";
			}else{
				return "url_info";
			}
		}
		if(field.equals(Canale.model().ABILITATO)){
			if(appendTablePrefix){
				return this.toAliasTable(field)+".abilitato";
			}else{
				return "abilitato";
			}
		}


		return super.toColumn(field,returnAlias,appendTablePrefix);
		
	}
	
	@Override
	public String toTable(IField field,boolean returnAlias) throws ExpressionException {
		
		// In the case of table with alias, using parameter returnAlias​​, 
		// it is possible to drive the choice whether to return only the alias or 
		// the full definition of the table containing the alias
		
		if(field.equals(Canale.model().ID_PSP.COD_PSP)){
			return this.toTable(Canale.model().ID_PSP, returnAlias);
		}
		if(field.equals(Canale.model().COD_CANALE)){
			return this.toTable(Canale.model(), returnAlias);
		}
		if(field.equals(Canale.model().COD_INTERMEDIARIO)){
			return this.toTable(Canale.model(), returnAlias);
		}
		if(field.equals(Canale.model().TIPO_VERSAMENTO)){
			return this.toTable(Canale.model(), returnAlias);
		}
		if(field.equals(Canale.model().MODELLO_PAGAMENTO)){
			return this.toTable(Canale.model(), returnAlias);
		}
		if(field.equals(Canale.model().DISPONIBILITA)){
			return this.toTable(Canale.model(), returnAlias);
		}
		if(field.equals(Canale.model().DESCRIZIONE)){
			return this.toTable(Canale.model(), returnAlias);
		}
		if(field.equals(Canale.model().CONDIZIONI)){
			return this.toTable(Canale.model(), returnAlias);
		}
		if(field.equals(Canale.model().URL_INFO)){
			return this.toTable(Canale.model(), returnAlias);
		}
		if(field.equals(Canale.model().ABILITATO)){
			return this.toTable(Canale.model(), returnAlias);
		}


		return super.toTable(field,returnAlias);
		
	}

	@Override
	public String toTable(IModel<?> model,boolean returnAlias) throws ExpressionException {
		
		// In the case of table with alias, using parameter returnAlias​​, 
		// it is possible to drive the choice whether to return only the alias or 
		// the full definition of the table containing the alias
		
		if(model.equals(Canale.model())){
			return "canali";
		}
		if(model.equals(Canale.model().ID_PSP)){
			return "psp";
		}


		return super.toTable(model,returnAlias);
		
	}

}
