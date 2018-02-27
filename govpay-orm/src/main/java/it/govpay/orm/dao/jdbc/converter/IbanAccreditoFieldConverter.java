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

import it.govpay.orm.IbanAccredito;


/**     
 * IbanAccreditoFieldConverter
 *
 * @author Giovanni Bussu (bussu@link.it)
 * @author Lorenzo Nardi (nardi@link.it)
 * @author $Author$
 * @version $Rev$, $Date$
 */
public class IbanAccreditoFieldConverter extends AbstractSQLFieldConverter {

	private TipiDatabase databaseType;
	
	public IbanAccreditoFieldConverter(String databaseType){
		this.databaseType = TipiDatabase.toEnumConstant(databaseType);
	}
	public IbanAccreditoFieldConverter(TipiDatabase databaseType){
		this.databaseType = databaseType;
	}


	@Override
	public IModel<?> getRootModel() throws ExpressionException {
		return IbanAccredito.model();
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
		
		if(field.equals(IbanAccredito.model().COD_IBAN)){
			if(appendTablePrefix){
				return this.toAliasTable(field)+".cod_iban";
			}else{
				return "cod_iban";
			}
		}
		if(field.equals(IbanAccredito.model().BIC_ACCREDITO)){
			if(appendTablePrefix){
				return this.toAliasTable(field)+".bic_accredito";
			}else{
				return "bic_accredito";
			}
		}
		if(field.equals(IbanAccredito.model().IBAN_APPOGGIO)){
			if(appendTablePrefix){
				return this.toAliasTable(field)+".iban_appoggio";
			}else{
				return "iban_appoggio";
			}
		}
		if(field.equals(IbanAccredito.model().BIC_APPOGGIO)){
			if(appendTablePrefix){
				return this.toAliasTable(field)+".bic_appoggio";
			}else{
				return "bic_appoggio";
			}
		}
		if(field.equals(IbanAccredito.model().POSTALE)){
			if(appendTablePrefix){
				return this.toAliasTable(field)+".postale";
			}else{
				return "postale";
			}
		}
		if(field.equals(IbanAccredito.model().ATTIVATO)){
			if(appendTablePrefix){
				return this.toAliasTable(field)+".attivato";
			}else{
				return "attivato";
			}
		}
		if(field.equals(IbanAccredito.model().ABILITATO)){
			if(appendTablePrefix){
				return this.toAliasTable(field)+".abilitato";
			}else{
				return "abilitato";
			}
		}
		if(field.equals(IbanAccredito.model().ID_DOMINIO.COD_DOMINIO)){
			if(appendTablePrefix){
				return this.toAliasTable(field)+".cod_dominio";
			}else{
				return "cod_dominio";
			}
		}


		return super.toColumn(field,returnAlias,appendTablePrefix);
		
	}
	
	@Override
	public String toTable(IField field,boolean returnAlias) throws ExpressionException {
		
		// In the case of table with alias, using parameter returnAlias​​, 
		// it is possible to drive the choice whether to return only the alias or 
		// the full definition of the table containing the alias
		
		if(field.equals(IbanAccredito.model().COD_IBAN)){
			return this.toTable(IbanAccredito.model(), returnAlias);
		}
		if(field.equals(IbanAccredito.model().BIC_ACCREDITO)){
			return this.toTable(IbanAccredito.model(), returnAlias);
		}
		if(field.equals(IbanAccredito.model().IBAN_APPOGGIO)){
			return this.toTable(IbanAccredito.model(), returnAlias);
		}
		if(field.equals(IbanAccredito.model().BIC_APPOGGIO)){
			return this.toTable(IbanAccredito.model(), returnAlias);
		}
		if(field.equals(IbanAccredito.model().POSTALE)){
			return this.toTable(IbanAccredito.model(), returnAlias);
		}
		if(field.equals(IbanAccredito.model().ATTIVATO)){
			return this.toTable(IbanAccredito.model(), returnAlias);
		}
		if(field.equals(IbanAccredito.model().ABILITATO)){
			return this.toTable(IbanAccredito.model(), returnAlias);
		}
		if(field.equals(IbanAccredito.model().ID_DOMINIO.COD_DOMINIO)){
			return this.toTable(IbanAccredito.model().ID_DOMINIO, returnAlias);
		}


		return super.toTable(field,returnAlias);
		
	}

	@Override
	public String toTable(IModel<?> model,boolean returnAlias) throws ExpressionException {
		
		// In the case of table with alias, using parameter returnAlias​​, 
		// it is possible to drive the choice whether to return only the alias or 
		// the full definition of the table containing the alias
		
		if(model.equals(IbanAccredito.model())){
			return "iban_accredito";
		}
		if(model.equals(IbanAccredito.model().ID_DOMINIO)){
			return "domini";
		}


		return super.toTable(model,returnAlias);
		
	}

}
