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

import it.govpay.orm.Tributo;


/**     
 * TributoFieldConverter
 *
 * @author Giovanni Bussu (bussu@link.it)
 * @author Lorenzo Nardi (nardi@link.it)
 * @author $Author$
 * @version $Rev$, $Date$
 */
public class TributoFieldConverter extends AbstractSQLFieldConverter {

	private TipiDatabase databaseType;
	
	public TributoFieldConverter(String databaseType){
		this.databaseType = TipiDatabase.toEnumConstant(databaseType);
	}
	public TributoFieldConverter(TipiDatabase databaseType){
		this.databaseType = databaseType;
	}


	@Override
	public IModel<?> getRootModel() throws ExpressionException {
		return Tributo.model();
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
		
		if(field.equals(Tributo.model().ID_ENTE.COD_ENTE)){
			if(appendTablePrefix){
				return this.toAliasTable(field)+".cod_ente";
			}else{
				return "cod_ente";
			}
		}
		if(field.equals(Tributo.model().COD_TRIBUTO)){
			if(appendTablePrefix){
				return this.toAliasTable(field)+".cod_tributo";
			}else{
				return "cod_tributo";
			}
		}
		if(field.equals(Tributo.model().ABILITATO)){
			if(appendTablePrefix){
				return this.toAliasTable(field)+".abilitato";
			}else{
				return "abilitato";
			}
		}
		if(field.equals(Tributo.model().DESCRIZIONE)){
			if(appendTablePrefix){
				return this.toAliasTable(field)+".descrizione";
			}else{
				return "descrizione";
			}
		}
		if(field.equals(Tributo.model().IBAN_ACCREDITO.COD_IBAN)){
			if(appendTablePrefix){
				return this.toAliasTable(field)+".cod_iban";
			}else{
				return "cod_iban";
			}
		}
		if(field.equals(Tributo.model().TIPO_CONTABILITA)){
			if(appendTablePrefix){
				return this.toAliasTable(field)+".tipo_contabilita";
			}else{
				return "tipo_contabilita";
			}
		}
		if(field.equals(Tributo.model().CODICE_CONTABILITA)){
			if(appendTablePrefix){
				return this.toAliasTable(field)+".codice_contabilita";
			}else{
				return "codice_contabilita";
			}
		}


		return super.toColumn(field,returnAlias,appendTablePrefix);
		
	}
	
	@Override
	public String toTable(IField field,boolean returnAlias) throws ExpressionException {
		
		// In the case of table with alias, using parameter returnAlias​​, 
		// it is possible to drive the choice whether to return only the alias or 
		// the full definition of the table containing the alias
		
		if(field.equals(Tributo.model().ID_ENTE.COD_ENTE)){
			return this.toTable(Tributo.model().ID_ENTE, returnAlias);
		}
		if(field.equals(Tributo.model().COD_TRIBUTO)){
			return this.toTable(Tributo.model(), returnAlias);
		}
		if(field.equals(Tributo.model().ABILITATO)){
			return this.toTable(Tributo.model(), returnAlias);
		}
		if(field.equals(Tributo.model().DESCRIZIONE)){
			return this.toTable(Tributo.model(), returnAlias);
		}
		if(field.equals(Tributo.model().IBAN_ACCREDITO.COD_IBAN)){
			return this.toTable(Tributo.model().IBAN_ACCREDITO, returnAlias);
		}
		if(field.equals(Tributo.model().TIPO_CONTABILITA)){
			return this.toTable(Tributo.model(), returnAlias);
		}
		if(field.equals(Tributo.model().CODICE_CONTABILITA)){
			return this.toTable(Tributo.model(), returnAlias);
		}


		return super.toTable(field,returnAlias);
		
	}

	@Override
	public String toTable(IModel<?> model,boolean returnAlias) throws ExpressionException {
		
		// In the case of table with alias, using parameter returnAlias​​, 
		// it is possible to drive the choice whether to return only the alias or 
		// the full definition of the table containing the alias
		
		if(model.equals(Tributo.model())){
			return "tributi";
		}
		if(model.equals(Tributo.model().ID_ENTE)){
			return "enti";
		}
		if(model.equals(Tributo.model().IBAN_ACCREDITO)){
			return "iban_accredito";
		}


		return super.toTable(model,returnAlias);
		
	}

}
