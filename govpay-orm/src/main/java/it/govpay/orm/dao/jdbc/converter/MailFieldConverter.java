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

import it.govpay.orm.Mail;


/**     
 * MailFieldConverter
 *
 * @author Giovanni Bussu (bussu@link.it)
 * @author Lorenzo Nardi (nardi@link.it)
 * @author $Author$
 * @version $Rev$, $Date$
 */
public class MailFieldConverter extends AbstractSQLFieldConverter {

	private TipiDatabase databaseType;
	
	public MailFieldConverter(String databaseType){
		this.databaseType = TipiDatabase.toEnumConstant(databaseType);
	}
	public MailFieldConverter(TipiDatabase databaseType){
		this.databaseType = databaseType;
	}


	@Override
	public IModel<?> getRootModel() throws ExpressionException {
		return Mail.model();
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
		
		if(field.equals(Mail.model().TIPO_MAIL)){
			if(appendTablePrefix){
				return this.toAliasTable(field)+".tipo_mail";
			}else{
				return "tipo_mail";
			}
		}
		if(field.equals(Mail.model().BUNDLE_KEY)){
			if(appendTablePrefix){
				return this.toAliasTable(field)+".bundle_key";
			}else{
				return "bundle_key";
			}
		}
		if(field.equals(Mail.model().ID_VERSAMENTO)){
			if(appendTablePrefix){
				return this.toAliasTable(field)+".id_versamento";
			}else{
				return "id_versamento";
			}
		}
		if(field.equals(Mail.model().MITTENTE)){
			if(appendTablePrefix){
				return this.toAliasTable(field)+".mittente";
			}else{
				return "mittente";
			}
		}
		if(field.equals(Mail.model().DESTINATARIO)){
			if(appendTablePrefix){
				return this.toAliasTable(field)+".destinatario";
			}else{
				return "destinatario";
			}
		}
		if(field.equals(Mail.model().CC)){
			if(appendTablePrefix){
				return this.toAliasTable(field)+".cc";
			}else{
				return "cc";
			}
		}
		if(field.equals(Mail.model().OGGETTO)){
			if(appendTablePrefix){
				return this.toAliasTable(field)+".oggetto";
			}else{
				return "oggetto";
			}
		}
		if(field.equals(Mail.model().MESSAGGIO)){
			if(appendTablePrefix){
				return this.toAliasTable(field)+".messaggio";
			}else{
				return "messaggio";
			}
		}
		if(field.equals(Mail.model().STATO_SPEDIZIONE)){
			if(appendTablePrefix){
				return this.toAliasTable(field)+".stato_spedizione";
			}else{
				return "stato_spedizione";
			}
		}
		if(field.equals(Mail.model().DETTAGLIO_ERRORE_SPEDIZIONE)){
			if(appendTablePrefix){
				return this.toAliasTable(field)+".dettaglio_errore_spedizione";
			}else{
				return "dettaglio_errore_spedizione";
			}
		}
		if(field.equals(Mail.model().DATA_ORA_ULTIMA_SPEDIZIONE)){
			if(appendTablePrefix){
				return this.toAliasTable(field)+".data_ora_ultima_spedizione";
			}else{
				return "data_ora_ultima_spedizione";
			}
		}
		if(field.equals(Mail.model().TENTATIVI_RISPEDIZIONE)){
			if(appendTablePrefix){
				return this.toAliasTable(field)+".tentativi_rispedizione";
			}else{
				return "tentativi_rispedizione";
			}
		}
		if(field.equals(Mail.model().ID_TRACCIATO_RPT.ID_TRACCIATO)){
			if(appendTablePrefix){
				return this.toAliasTable(field)+".id_tracciato";
			}else{
				return "id_tracciato";
			}
		}
		if(field.equals(Mail.model().ID_TRACCIATO_RT.ID_TRACCIATO)){
			if(appendTablePrefix){
				return this.toAliasTable(field)+".id_tracciato";
			}else{
				return "id_tracciato";
			}
		}


		return super.toColumn(field,returnAlias,appendTablePrefix);
		
	}
	
	@Override
	public String toTable(IField field,boolean returnAlias) throws ExpressionException {
		
		// In the case of table with alias, using parameter returnAlias​​, 
		// it is possible to drive the choice whether to return only the alias or 
		// the full definition of the table containing the alias
		
		if(field.equals(Mail.model().TIPO_MAIL)){
			return this.toTable(Mail.model(), returnAlias);
		}
		if(field.equals(Mail.model().BUNDLE_KEY)){
			return this.toTable(Mail.model(), returnAlias);
		}
		if(field.equals(Mail.model().ID_VERSAMENTO)){
			return this.toTable(Mail.model(), returnAlias);
		}
		if(field.equals(Mail.model().MITTENTE)){
			return this.toTable(Mail.model(), returnAlias);
		}
		if(field.equals(Mail.model().DESTINATARIO)){
			return this.toTable(Mail.model(), returnAlias);
		}
		if(field.equals(Mail.model().CC)){
			return this.toTable(Mail.model(), returnAlias);
		}
		if(field.equals(Mail.model().OGGETTO)){
			return this.toTable(Mail.model(), returnAlias);
		}
		if(field.equals(Mail.model().MESSAGGIO)){
			return this.toTable(Mail.model(), returnAlias);
		}
		if(field.equals(Mail.model().STATO_SPEDIZIONE)){
			return this.toTable(Mail.model(), returnAlias);
		}
		if(field.equals(Mail.model().DETTAGLIO_ERRORE_SPEDIZIONE)){
			return this.toTable(Mail.model(), returnAlias);
		}
		if(field.equals(Mail.model().DATA_ORA_ULTIMA_SPEDIZIONE)){
			return this.toTable(Mail.model(), returnAlias);
		}
		if(field.equals(Mail.model().TENTATIVI_RISPEDIZIONE)){
			return this.toTable(Mail.model(), returnAlias);
		}
		if(field.equals(Mail.model().ID_TRACCIATO_RPT.ID_TRACCIATO)){
			return this.toTable(Mail.model().ID_TRACCIATO_RPT, returnAlias);
		}
		if(field.equals(Mail.model().ID_TRACCIATO_RT.ID_TRACCIATO)){
			return this.toTable(Mail.model().ID_TRACCIATO_RT, returnAlias);
		}


		return super.toTable(field,returnAlias);
		
	}

	@Override
	public String toTable(IModel<?> model,boolean returnAlias) throws ExpressionException {
		
		// In the case of table with alias, using parameter returnAlias​​, 
		// it is possible to drive the choice whether to return only the alias or 
		// the full definition of the table containing the alias
		
		if(model.equals(Mail.model())){
			return "mail";
		}
		if(model.equals(Mail.model().ID_TRACCIATO_RPT)){
			return "tracciatixml";
		}
		if(model.equals(Mail.model().ID_TRACCIATO_RT)){
			return "tracciatixml";
		}


		return super.toTable(model,returnAlias);
		
	}

}
