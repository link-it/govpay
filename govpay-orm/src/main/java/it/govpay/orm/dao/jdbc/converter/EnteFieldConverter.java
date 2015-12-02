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

import it.govpay.orm.Ente;


/**     
 * EnteFieldConverter
 *
 * @author Giovanni Bussu (bussu@link.it)
 * @author Lorenzo Nardi (nardi@link.it)
 * @author $Author$
 * @version $Rev$, $Date$
 */
public class EnteFieldConverter extends AbstractSQLFieldConverter {

	private TipiDatabase databaseType;
	
	public EnteFieldConverter(String databaseType){
		this.databaseType = TipiDatabase.toEnumConstant(databaseType);
	}
	public EnteFieldConverter(TipiDatabase databaseType){
		this.databaseType = databaseType;
	}


	@Override
	public IModel<?> getRootModel() throws ExpressionException {
		return Ente.model();
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
		
		if(field.equals(Ente.model().ID_ANAGRAFICA_ENTE.ID_ANAGRAFICA)){
			if(appendTablePrefix){
				return this.toAliasTable(field)+".id_anagrafica";
			}else{
				return "id_anagrafica";
			}
		}
		if(field.equals(Ente.model().COD_ENTE)){
			if(appendTablePrefix){
				return this.toAliasTable(field)+".cod_ente";
			}else{
				return "cod_ente";
			}
		}
		if(field.equals(Ente.model().ID_DOMINIO.COD_DOMINIO)){
			if(appendTablePrefix){
				return this.toAliasTable(field)+".cod_dominio";
			}else{
				return "cod_dominio";
			}
		}
		if(field.equals(Ente.model().ABILITATO)){
			if(appendTablePrefix){
				return this.toAliasTable(field)+".abilitato";
			}else{
				return "abilitato";
			}
		}
		if(field.equals(Ente.model().ID_TEMPLATE_RPT.ID_MAIL_TEMPLATE)){
			if(appendTablePrefix){
				return this.toAliasTable(field)+".id_mail_template";
			}else{
				return "id_mail_template";
			}
		}
		if(field.equals(Ente.model().ID_TEMPLATE_RT.ID_MAIL_TEMPLATE)){
			if(appendTablePrefix){
				return this.toAliasTable(field)+".id_mail_template";
			}else{
				return "id_mail_template";
			}
		}
		if(field.equals(Ente.model().INVIO_MAIL_RPTABILITATO)){
			if(appendTablePrefix){
				return this.toAliasTable(field)+".invio_mail_rptabilitato";
			}else{
				return "invio_mail_rptabilitato";
			}
		}
		if(field.equals(Ente.model().INVIO_MAIL_RTABILITATO)){
			if(appendTablePrefix){
				return this.toAliasTable(field)+".invio_mail_rtabilitato";
			}else{
				return "invio_mail_rtabilitato";
			}
		}


		return super.toColumn(field,returnAlias,appendTablePrefix);
		
	}
	
	@Override
	public String toTable(IField field,boolean returnAlias) throws ExpressionException {
		
		// In the case of table with alias, using parameter returnAlias​​, 
		// it is possible to drive the choice whether to return only the alias or 
		// the full definition of the table containing the alias
		
		if(field.equals(Ente.model().ID_ANAGRAFICA_ENTE.ID_ANAGRAFICA)){
			return this.toTable(Ente.model().ID_ANAGRAFICA_ENTE, returnAlias);
		}
		if(field.equals(Ente.model().COD_ENTE)){
			return this.toTable(Ente.model(), returnAlias);
		}
		if(field.equals(Ente.model().ID_DOMINIO.COD_DOMINIO)){
			return this.toTable(Ente.model().ID_DOMINIO, returnAlias);
		}
		if(field.equals(Ente.model().ABILITATO)){
			return this.toTable(Ente.model(), returnAlias);
		}
		if(field.equals(Ente.model().ID_TEMPLATE_RPT.ID_MAIL_TEMPLATE)){
			return this.toTable(Ente.model().ID_TEMPLATE_RPT, returnAlias);
		}
		if(field.equals(Ente.model().ID_TEMPLATE_RT.ID_MAIL_TEMPLATE)){
			return this.toTable(Ente.model().ID_TEMPLATE_RT, returnAlias);
		}
		if(field.equals(Ente.model().INVIO_MAIL_RPTABILITATO)){
			return this.toTable(Ente.model(), returnAlias);
		}
		if(field.equals(Ente.model().INVIO_MAIL_RTABILITATO)){
			return this.toTable(Ente.model(), returnAlias);
		}


		return super.toTable(field,returnAlias);
		
	}

	@Override
	public String toTable(IModel<?> model,boolean returnAlias) throws ExpressionException {
		
		// In the case of table with alias, using parameter returnAlias​​, 
		// it is possible to drive the choice whether to return only the alias or 
		// the full definition of the table containing the alias
		
		if(model.equals(Ente.model())){
			return "enti";
		}
		if(model.equals(Ente.model().ID_ANAGRAFICA_ENTE)){
			return "anagrafiche";
		}
		if(model.equals(Ente.model().ID_DOMINIO)){
			return "domini";
		}
		if(model.equals(Ente.model().ID_TEMPLATE_RPT)){
			return "mail_template";
		}
		if(model.equals(Ente.model().ID_TEMPLATE_RT)){
			return "mail_template";
		}


		return super.toTable(model,returnAlias);
		
	}

}
