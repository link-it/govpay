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

import it.govpay.orm.RR;


/**     
 * RRFieldConverter
 *
 * @author Giovanni Bussu (bussu@link.it)
 * @author Lorenzo Nardi (nardi@link.it)
 * @author $Author$
 * @version $Rev$, $Date$
 */
public class RRFieldConverter extends AbstractSQLFieldConverter {

	private TipiDatabase databaseType;
	
	public RRFieldConverter(String databaseType){
		this.databaseType = TipiDatabase.toEnumConstant(databaseType);
	}
	public RRFieldConverter(TipiDatabase databaseType){
		this.databaseType = databaseType;
	}


	@Override
	public IModel<?> getRootModel() throws ExpressionException {
		return RR.model();
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
		
		if(field.equals(RR.model().ID_RPT.COD_MSG_RICHIESTA)){
			if(appendTablePrefix){
				return this.toAliasTable(field)+".cod_msg_richiesta";
			}else{
				return "cod_msg_richiesta";
			}
		}
		if(field.equals(RR.model().ID_RPT.COD_DOMINIO)){
			if(appendTablePrefix){
				return this.toAliasTable(field)+".cod_dominio";
			}else{
				return "cod_dominio";
			}
		}
		if(field.equals(RR.model().ID_RPT.IUV)){
			if(appendTablePrefix){
				return this.toAliasTable(field)+".iuv";
			}else{
				return "iuv";
			}
		}
		if(field.equals(RR.model().COD_DOMINIO)){
			if(appendTablePrefix){
				return this.toAliasTable(field)+".cod_dominio";
			}else{
				return "cod_dominio";
			}
		}
		if(field.equals(RR.model().IUV)){
			if(appendTablePrefix){
				return this.toAliasTable(field)+".iuv";
			}else{
				return "iuv";
			}
		}
		if(field.equals(RR.model().CCP)){
			if(appendTablePrefix){
				return this.toAliasTable(field)+".ccp";
			}else{
				return "ccp";
			}
		}
		if(field.equals(RR.model().COD_MSG_REVOCA)){
			if(appendTablePrefix){
				return this.toAliasTable(field)+".cod_msg_revoca";
			}else{
				return "cod_msg_revoca";
			}
		}
		if(field.equals(RR.model().DATA_MSG_REVOCA)){
			if(appendTablePrefix){
				return this.toAliasTable(field)+".data_msg_revoca";
			}else{
				return "data_msg_revoca";
			}
		}
		if(field.equals(RR.model().DATA_MSG_ESITO)){
			if(appendTablePrefix){
				return this.toAliasTable(field)+".data_msg_esito";
			}else{
				return "data_msg_esito";
			}
		}
		if(field.equals(RR.model().STATO)){
			if(appendTablePrefix){
				return this.toAliasTable(field)+".stato";
			}else{
				return "stato";
			}
		}
		if(field.equals(RR.model().DESCRIZIONE_STATO)){
			if(appendTablePrefix){
				return this.toAliasTable(field)+".descrizione_stato";
			}else{
				return "descrizione_stato";
			}
		}
		if(field.equals(RR.model().IMPORTO_TOTALE_RICHIESTO)){
			if(appendTablePrefix){
				return this.toAliasTable(field)+".importo_totale_richiesto";
			}else{
				return "importo_totale_richiesto";
			}
		}
		if(field.equals(RR.model().COD_MSG_ESITO)){
			if(appendTablePrefix){
				return this.toAliasTable(field)+".cod_msg_esito";
			}else{
				return "cod_msg_esito";
			}
		}
		if(field.equals(RR.model().IMPORTO_TOTALE_REVOCATO)){
			if(appendTablePrefix){
				return this.toAliasTable(field)+".importo_totale_revocato";
			}else{
				return "importo_totale_revocato";
			}
		}
		if(field.equals(RR.model().XML_RR)){
			if(appendTablePrefix){
				return this.toAliasTable(field)+".xml_rr";
			}else{
				return "xml_rr";
			}
		}
		if(field.equals(RR.model().XML_ER)){
			if(appendTablePrefix){
				return this.toAliasTable(field)+".xml_er";
			}else{
				return "xml_er";
			}
		}
		if(field.equals(RR.model().COD_TRANSAZIONE_RR)){
			if(appendTablePrefix){
				return this.toAliasTable(field)+".cod_transazione_rr";
			}else{
				return "cod_transazione_rr";
			}
		}
		if(field.equals(RR.model().COD_TRANSAZIONE_ER)){
			if(appendTablePrefix){
				return this.toAliasTable(field)+".cod_transazione_er";
			}else{
				return "cod_transazione_er";
			}
		}


		return super.toColumn(field,returnAlias,appendTablePrefix);
		
	}
	
	@Override
	public String toTable(IField field,boolean returnAlias) throws ExpressionException {
		
		// In the case of table with alias, using parameter returnAlias​​, 
		// it is possible to drive the choice whether to return only the alias or 
		// the full definition of the table containing the alias
		
		if(field.equals(RR.model().ID_RPT.COD_MSG_RICHIESTA)){
			return this.toTable(RR.model().ID_RPT, returnAlias);
		}
		if(field.equals(RR.model().ID_RPT.COD_DOMINIO)){
			return this.toTable(RR.model().ID_RPT, returnAlias);
		}
		if(field.equals(RR.model().ID_RPT.IUV)){
			return this.toTable(RR.model().ID_RPT, returnAlias);
		}
		if(field.equals(RR.model().COD_DOMINIO)){
			return this.toTable(RR.model(), returnAlias);
		}
		if(field.equals(RR.model().IUV)){
			return this.toTable(RR.model(), returnAlias);
		}
		if(field.equals(RR.model().CCP)){
			return this.toTable(RR.model(), returnAlias);
		}
		if(field.equals(RR.model().COD_MSG_REVOCA)){
			return this.toTable(RR.model(), returnAlias);
		}
		if(field.equals(RR.model().DATA_MSG_REVOCA)){
			return this.toTable(RR.model(), returnAlias);
		}
		if(field.equals(RR.model().DATA_MSG_ESITO)){
			return this.toTable(RR.model(), returnAlias);
		}
		if(field.equals(RR.model().STATO)){
			return this.toTable(RR.model(), returnAlias);
		}
		if(field.equals(RR.model().DESCRIZIONE_STATO)){
			return this.toTable(RR.model(), returnAlias);
		}
		if(field.equals(RR.model().IMPORTO_TOTALE_RICHIESTO)){
			return this.toTable(RR.model(), returnAlias);
		}
		if(field.equals(RR.model().COD_MSG_ESITO)){
			return this.toTable(RR.model(), returnAlias);
		}
		if(field.equals(RR.model().IMPORTO_TOTALE_REVOCATO)){
			return this.toTable(RR.model(), returnAlias);
		}
		if(field.equals(RR.model().XML_RR)){
			return this.toTable(RR.model(), returnAlias);
		}
		if(field.equals(RR.model().XML_ER)){
			return this.toTable(RR.model(), returnAlias);
		}
		if(field.equals(RR.model().COD_TRANSAZIONE_RR)){
			return this.toTable(RR.model(), returnAlias);
		}
		if(field.equals(RR.model().COD_TRANSAZIONE_ER)){
			return this.toTable(RR.model(), returnAlias);
		}


		return super.toTable(field,returnAlias);
		
	}

	@Override
	public String toTable(IModel<?> model,boolean returnAlias) throws ExpressionException {
		
		// In the case of table with alias, using parameter returnAlias​​, 
		// it is possible to drive the choice whether to return only the alias or 
		// the full definition of the table containing the alias
		
		if(model.equals(RR.model())){
			return "rr";
		}
		if(model.equals(RR.model().ID_RPT)){
			return "rpt";
		}


		return super.toTable(model,returnAlias);
		
	}

}
