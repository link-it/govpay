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

import it.govpay.orm.ACL;


/**     
 * ACLFieldConverter
 *
 * @author Giovanni Bussu (bussu@link.it)
 * @author Lorenzo Nardi (nardi@link.it)
 * @author $Author$
 * @version $Rev$, $Date$
 */
public class ACLFieldConverter extends AbstractSQLFieldConverter {

	private TipiDatabase databaseType;
	
	public ACLFieldConverter(String databaseType){
		this.databaseType = TipiDatabase.toEnumConstant(databaseType);
	}
	public ACLFieldConverter(TipiDatabase databaseType){
		this.databaseType = databaseType;
	}


	@Override
	public IModel<?> getRootModel() throws ExpressionException {
		return ACL.model();
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
		
		if(field.equals(ACL.model().RUOLO)){
			if(appendTablePrefix){
				return this.toAliasTable(field)+".ruolo";
			}else{
				return "ruolo";
			}
		}
		if(field.equals(ACL.model().ID_UTENZA.PRINCIPAL)){
			if(appendTablePrefix){
				return this.toAliasTable(field)+".principal";
			}else{
				return "principal";
			}
		}
		if(field.equals(ACL.model().ID_UTENZA.PRINCIPAL_ORIGINALE)){
			if(appendTablePrefix){
				return this.toAliasTable(field)+".principal_originale";
			}else{
				return "principal_originale";
			}
		}
		if(field.equals(ACL.model().ID_UTENZA.ABILITATO)){
			if(appendTablePrefix){
				return this.toAliasTable(field)+".abilitato";
			}else{
				return "abilitato";
			}
		}
		if(field.equals(ACL.model().SERVIZIO)){
			if(appendTablePrefix){
				return this.toAliasTable(field)+".servizio";
			}else{
				return "servizio";
			}
		}
		if(field.equals(ACL.model().DIRITTI)){
			if(appendTablePrefix){
				return this.toAliasTable(field)+".diritti";
			}else{
				return "diritti";
			}
		}


		return super.toColumn(field,returnAlias,appendTablePrefix);
		
	}
	
	@Override
	public String toTable(IField field,boolean returnAlias) throws ExpressionException {
		
		// In the case of table with alias, using parameter returnAlias​​, 
		// it is possible to drive the choice whether to return only the alias or 
		// the full definition of the table containing the alias
		
		if(field.equals(ACL.model().RUOLO)){
			return this.toTable(ACL.model(), returnAlias);
		}
		if(field.equals(ACL.model().ID_UTENZA.PRINCIPAL)){
			return this.toTable(ACL.model().ID_UTENZA, returnAlias);
		}
		if(field.equals(ACL.model().ID_UTENZA.PRINCIPAL_ORIGINALE)){
			return this.toTable(ACL.model().ID_UTENZA, returnAlias);
		}
		if(field.equals(ACL.model().ID_UTENZA.ABILITATO)){
			return this.toTable(ACL.model().ID_UTENZA, returnAlias);
		}
		if(field.equals(ACL.model().SERVIZIO)){
			return this.toTable(ACL.model(), returnAlias);
		}
		if(field.equals(ACL.model().DIRITTI)){
			return this.toTable(ACL.model(), returnAlias);
		}


		return super.toTable(field,returnAlias);
		
	}

	@Override
	public String toTable(IModel<?> model,boolean returnAlias) throws ExpressionException {
		
		// In the case of table with alias, using parameter returnAlias​​, 
		// it is possible to drive the choice whether to return only the alias or 
		// the full definition of the table containing the alias
		
		if(model.equals(ACL.model())){
			return "acl";
		}
		if(model.equals(ACL.model().ID_UTENZA)){
			return "utenze";
		}


		return super.toTable(model,returnAlias);
		
	}

}
