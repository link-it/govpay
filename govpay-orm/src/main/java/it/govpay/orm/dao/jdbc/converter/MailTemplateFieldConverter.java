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

import it.govpay.orm.MailTemplate;


/**     
 * MailTemplateFieldConverter
 *
 * @author Giovanni Bussu (bussu@link.it)
 * @author Lorenzo Nardi (nardi@link.it)
 * @author $Author$
 * @version $Rev$, $Date$
 */
public class MailTemplateFieldConverter extends AbstractSQLFieldConverter {

	private TipiDatabase databaseType;
	
	public MailTemplateFieldConverter(String databaseType){
		this.databaseType = TipiDatabase.toEnumConstant(databaseType);
	}
	public MailTemplateFieldConverter(TipiDatabase databaseType){
		this.databaseType = databaseType;
	}


	@Override
	public IModel<?> getRootModel() throws ExpressionException {
		return MailTemplate.model();
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
		
		if(field.equals(MailTemplate.model().MITTENTE)){
			if(appendTablePrefix){
				return this.toAliasTable(field)+".mittente";
			}else{
				return "mittente";
			}
		}
		if(field.equals(MailTemplate.model().TEMPLATE_OGGETTO)){
			if(appendTablePrefix){
				return this.toAliasTable(field)+".template_oggetto";
			}else{
				return "template_oggetto";
			}
		}
		if(field.equals(MailTemplate.model().TEMPLATE_MESSAGGIO)){
			if(appendTablePrefix){
				return this.toAliasTable(field)+".template_messaggio";
			}else{
				return "template_messaggio";
			}
		}
		if(field.equals(MailTemplate.model().ALLEGATI)){
			if(appendTablePrefix){
				return this.toAliasTable(field)+".allegati";
			}else{
				return "allegati";
			}
		}


		return super.toColumn(field,returnAlias,appendTablePrefix);
		
	}
	
	@Override
	public String toTable(IField field,boolean returnAlias) throws ExpressionException {
		
		// In the case of table with alias, using parameter returnAlias​​, 
		// it is possible to drive the choice whether to return only the alias or 
		// the full definition of the table containing the alias
		
		if(field.equals(MailTemplate.model().MITTENTE)){
			return this.toTable(MailTemplate.model(), returnAlias);
		}
		if(field.equals(MailTemplate.model().TEMPLATE_OGGETTO)){
			return this.toTable(MailTemplate.model(), returnAlias);
		}
		if(field.equals(MailTemplate.model().TEMPLATE_MESSAGGIO)){
			return this.toTable(MailTemplate.model(), returnAlias);
		}
		if(field.equals(MailTemplate.model().ALLEGATI)){
			return this.toTable(MailTemplate.model(), returnAlias);
		}


		return super.toTable(field,returnAlias);
		
	}

	@Override
	public String toTable(IModel<?> model,boolean returnAlias) throws ExpressionException {
		
		// In the case of table with alias, using parameter returnAlias​​, 
		// it is possible to drive the choice whether to return only the alias or 
		// the full definition of the table containing the alias
		
		if(model.equals(MailTemplate.model())){
			return "mail_template";
		}


		return super.toTable(model,returnAlias);
		
	}

}
