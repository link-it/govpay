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

import it.govpay.orm.Notifica;


/**     
 * NotificaFieldConverter
 *
 * @author Giovanni Bussu (bussu@link.it)
 * @author Lorenzo Nardi (nardi@link.it)
 * @author $Author$
 * @version $Rev$, $Date$
 */
public class NotificaFieldConverter extends AbstractSQLFieldConverter {

	private TipiDatabase databaseType;
	
	public NotificaFieldConverter(String databaseType){
		this.databaseType = TipiDatabase.toEnumConstant(databaseType);
	}
	public NotificaFieldConverter(TipiDatabase databaseType){
		this.databaseType = databaseType;
	}


	@Override
	public IModel<?> getRootModel() throws ExpressionException {
		return Notifica.model();
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
		
		if(field.equals(Notifica.model().ID_APPLICAZIONE.COD_APPLICAZIONE)){
			if(appendTablePrefix){
				return this.toAliasTable(field)+".cod_applicazione";
			}else{
				return "cod_applicazione";
			}
		}
		if(field.equals(Notifica.model().ID_RPT.COD_MSG_RICHIESTA)){
			if(appendTablePrefix){
				return this.toAliasTable(field)+".cod_msg_richiesta";
			}else{
				return "cod_msg_richiesta";
			}
		}
		if(field.equals(Notifica.model().ID_RPT.COD_DOMINIO)){
			if(appendTablePrefix){
				return this.toAliasTable(field)+".cod_dominio";
			}else{
				return "cod_dominio";
			}
		}
		if(field.equals(Notifica.model().ID_RPT.IUV)){
			if(appendTablePrefix){
				return this.toAliasTable(field)+".iuv";
			}else{
				return "iuv";
			}
		}
		if(field.equals(Notifica.model().ID_RR.COD_MSG_REVOCA)){
			if(appendTablePrefix){
				return this.toAliasTable(field)+".cod_msg_revoca";
			}else{
				return "cod_msg_revoca";
			}
		}
		if(field.equals(Notifica.model().TIPO_ESITO)){
			if(appendTablePrefix){
				return this.toAliasTable(field)+".tipo_esito";
			}else{
				return "tipo_esito";
			}
		}
		if(field.equals(Notifica.model().DATA_CREAZIONE)){
			if(appendTablePrefix){
				return this.toAliasTable(field)+".data_creazione";
			}else{
				return "data_creazione";
			}
		}
		if(field.equals(Notifica.model().STATO)){
			if(appendTablePrefix){
				return this.toAliasTable(field)+".stato";
			}else{
				return "stato";
			}
		}
		if(field.equals(Notifica.model().DESCRIZIONE_STATO)){
			if(appendTablePrefix){
				return this.toAliasTable(field)+".descrizione_stato";
			}else{
				return "descrizione_stato";
			}
		}
		if(field.equals(Notifica.model().DATA_AGGIORNAMENTO_STATO)){
			if(appendTablePrefix){
				return this.toAliasTable(field)+".data_aggiornamento_stato";
			}else{
				return "data_aggiornamento_stato";
			}
		}
		if(field.equals(Notifica.model().DATA_PROSSIMA_SPEDIZIONE)){
			if(appendTablePrefix){
				return this.toAliasTable(field)+".data_prossima_spedizione";
			}else{
				return "data_prossima_spedizione";
			}
		}
		if(field.equals(Notifica.model().TENTATIVI_SPEDIZIONE)){
			if(appendTablePrefix){
				return this.toAliasTable(field)+".tentativi_spedizione";
			}else{
				return "tentativi_spedizione";
			}
		}


		return super.toColumn(field,returnAlias,appendTablePrefix);
		
	}
	
	@Override
	public String toTable(IField field,boolean returnAlias) throws ExpressionException {
		
		// In the case of table with alias, using parameter returnAlias​​, 
		// it is possible to drive the choice whether to return only the alias or 
		// the full definition of the table containing the alias
		
		if(field.equals(Notifica.model().ID_APPLICAZIONE.COD_APPLICAZIONE)){
			return this.toTable(Notifica.model().ID_APPLICAZIONE, returnAlias);
		}
		if(field.equals(Notifica.model().ID_RPT.COD_MSG_RICHIESTA)){
			return this.toTable(Notifica.model().ID_RPT, returnAlias);
		}
		if(field.equals(Notifica.model().ID_RPT.COD_DOMINIO)){
			return this.toTable(Notifica.model().ID_RPT, returnAlias);
		}
		if(field.equals(Notifica.model().ID_RPT.IUV)){
			return this.toTable(Notifica.model().ID_RPT, returnAlias);
		}
		if(field.equals(Notifica.model().ID_RR.COD_MSG_REVOCA)){
			return this.toTable(Notifica.model().ID_RR, returnAlias);
		}
		if(field.equals(Notifica.model().TIPO_ESITO)){
			return this.toTable(Notifica.model(), returnAlias);
		}
		if(field.equals(Notifica.model().DATA_CREAZIONE)){
			return this.toTable(Notifica.model(), returnAlias);
		}
		if(field.equals(Notifica.model().STATO)){
			return this.toTable(Notifica.model(), returnAlias);
		}
		if(field.equals(Notifica.model().DESCRIZIONE_STATO)){
			return this.toTable(Notifica.model(), returnAlias);
		}
		if(field.equals(Notifica.model().DATA_AGGIORNAMENTO_STATO)){
			return this.toTable(Notifica.model(), returnAlias);
		}
		if(field.equals(Notifica.model().DATA_PROSSIMA_SPEDIZIONE)){
			return this.toTable(Notifica.model(), returnAlias);
		}
		if(field.equals(Notifica.model().TENTATIVI_SPEDIZIONE)){
			return this.toTable(Notifica.model(), returnAlias);
		}


		return super.toTable(field,returnAlias);
		
	}

	@Override
	public String toTable(IModel<?> model,boolean returnAlias) throws ExpressionException {
		
		// In the case of table with alias, using parameter returnAlias​​, 
		// it is possible to drive the choice whether to return only the alias or 
		// the full definition of the table containing the alias
		
		if(model.equals(Notifica.model())){
			return "notifiche";
		}
		if(model.equals(Notifica.model().ID_APPLICAZIONE)){
			return "applicazioni";
		}
		if(model.equals(Notifica.model().ID_RPT)){
			return "rpt";
		}
		if(model.equals(Notifica.model().ID_RR)){
			return "rr";
		}


		return super.toTable(model,returnAlias);
		
	}

}
