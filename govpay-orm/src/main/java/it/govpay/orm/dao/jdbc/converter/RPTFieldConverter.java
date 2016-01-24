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

import it.govpay.orm.RPT;


/**     
 * RPTFieldConverter
 *
 * @author Giovanni Bussu (bussu@link.it)
 * @author Lorenzo Nardi (nardi@link.it)
 * @author $Author$
 * @version $Rev$, $Date$
 */
public class RPTFieldConverter extends AbstractSQLFieldConverter {

	private TipiDatabase databaseType;
	
	public RPTFieldConverter(String databaseType){
		this.databaseType = TipiDatabase.toEnumConstant(databaseType);
	}
	public RPTFieldConverter(TipiDatabase databaseType){
		this.databaseType = databaseType;
	}


	@Override
	public IModel<?> getRootModel() throws ExpressionException {
		return RPT.model();
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
		
		if(field.equals(RPT.model().ID_VERSAMENTO.COD_VERSAMENTO_ENTE)){
			if(appendTablePrefix){
				return this.toAliasTable(field)+".cod_versamento_ente";
			}else{
				return "cod_versamento_ente";
			}
		}
		if(field.equals(RPT.model().ID_VERSAMENTO.COD_DOMINIO)){
			if(appendTablePrefix){
				return this.toAliasTable(field)+".cod_dominio";
			}else{
				return "cod_dominio";
			}
		}
		if(field.equals(RPT.model().ID_PSP.COD_PSP)){
			if(appendTablePrefix){
				return this.toAliasTable(field)+".cod_psp";
			}else{
				return "cod_psp";
			}
		}
		if(field.equals(RPT.model().ID_CANALE.ID_PSP.COD_PSP)){
			if(appendTablePrefix){
				return this.toAliasTable(field)+".cod_psp";
			}else{
				return "cod_psp";
			}
		}
		if(field.equals(RPT.model().ID_CANALE.COD_CANALE)){
			if(appendTablePrefix){
				return this.toAliasTable(field)+".cod_canale";
			}else{
				return "cod_canale";
			}
		}
		if(field.equals(RPT.model().ID_CANALE.TIPO_VERSAMENTO)){
			if(appendTablePrefix){
				return this.toAliasTable(field)+".tipo_versamento";
			}else{
				return "tipo_versamento";
			}
		}
		if(field.equals(RPT.model().ID_PORTALE.COD_PORTALE)){
			if(appendTablePrefix){
				return this.toAliasTable(field)+".cod_portale";
			}else{
				return "cod_portale";
			}
		}
		if(field.equals(RPT.model().ID_TRACCIATO_XML.ID_TRACCIATO)){
			if(appendTablePrefix){
				return this.toAliasTable(field)+".id_tracciato";
			}else{
				return "id_tracciato";
			}
		}
		if(field.equals(RPT.model().ID_STAZIONE.COD_STAZIONE)){
			if(appendTablePrefix){
				return this.toAliasTable(field)+".cod_stazione";
			}else{
				return "cod_stazione";
			}
		}
		if(field.equals(RPT.model().COD_CARRELLO)){
			if(appendTablePrefix){
				return this.toAliasTable(field)+".cod_carrello";
			}else{
				return "cod_carrello";
			}
		}
		if(field.equals(RPT.model().IUV)){
			if(appendTablePrefix){
				return this.toAliasTable(field)+".iuv";
			}else{
				return "iuv";
			}
		}
		if(field.equals(RPT.model().CCP)){
			if(appendTablePrefix){
				return this.toAliasTable(field)+".ccp";
			}else{
				return "ccp";
			}
		}
		if(field.equals(RPT.model().COD_DOMINIO)){
			if(appendTablePrefix){
				return this.toAliasTable(field)+".cod_dominio";
			}else{
				return "cod_dominio";
			}
		}
		if(field.equals(RPT.model().TIPO_VERSAMENTO)){
			if(appendTablePrefix){
				return this.toAliasTable(field)+".tipo_versamento";
			}else{
				return "tipo_versamento";
			}
		}
		if(field.equals(RPT.model().ID_ANAGRAFICA_VERSANTE.ID_ANAGRAFICA)){
			if(appendTablePrefix){
				return this.toAliasTable(field)+".id_anagrafica";
			}else{
				return "id_anagrafica";
			}
		}
		if(field.equals(RPT.model().ID_ANAGRAFICA_VERSANTE.COD_UNIVOCO)){
			if(appendTablePrefix){
				return this.toAliasTable(field)+".cod_univoco";
			}else{
				return "cod_univoco";
			}
		}
		if(field.equals(RPT.model().DATA_ORA_MSG_RICHIESTA)){
			if(appendTablePrefix){
				return this.toAliasTable(field)+".data_ora_msg_richiesta";
			}else{
				return "data_ora_msg_richiesta";
			}
		}
		if(field.equals(RPT.model().DATA_ORA_CREAZIONE)){
			if(appendTablePrefix){
				return this.toAliasTable(field)+".data_ora_creazione";
			}else{
				return "data_ora_creazione";
			}
		}
		if(field.equals(RPT.model().COD_MSG_RICHIESTA)){
			if(appendTablePrefix){
				return this.toAliasTable(field)+".cod_msg_richiesta";
			}else{
				return "cod_msg_richiesta";
			}
		}
		if(field.equals(RPT.model().IBAN_ADDEBITO)){
			if(appendTablePrefix){
				return this.toAliasTable(field)+".iban_addebito";
			}else{
				return "iban_addebito";
			}
		}
		if(field.equals(RPT.model().AUTENTICAZIONE_SOGGETTO)){
			if(appendTablePrefix){
				return this.toAliasTable(field)+".autenticazione_soggetto";
			}else{
				return "autenticazione_soggetto";
			}
		}
		if(field.equals(RPT.model().FIRMA_RT)){
			if(appendTablePrefix){
				return this.toAliasTable(field)+".firma_rt";
			}else{
				return "firma_rt";
			}
		}
		if(field.equals(RPT.model().STATO)){
			if(appendTablePrefix){
				return this.toAliasTable(field)+".stato";
			}else{
				return "stato";
			}
		}
		if(field.equals(RPT.model().DESCRIZIONE_STATO)){
			if(appendTablePrefix){
				return this.toAliasTable(field)+".descrizione_stato";
			}else{
				return "descrizione_stato";
			}
		}
		if(field.equals(RPT.model().COD_FAULT)){
			if(appendTablePrefix){
				return this.toAliasTable(field)+".cod_fault";
			}else{
				return "cod_fault";
			}
		}
		if(field.equals(RPT.model().CALLBACK_URL)){
			if(appendTablePrefix){
				return this.toAliasTable(field)+".callback_url";
			}else{
				return "callback_url";
			}
		}
		if(field.equals(RPT.model().COD_SESSIONE)){
			if(appendTablePrefix){
				return this.toAliasTable(field)+".cod_sessione";
			}else{
				return "cod_sessione";
			}
		}
		if(field.equals(RPT.model().PSP_REDIRECT_URL)){
			if(appendTablePrefix){
				return this.toAliasTable(field)+".psp_redirect_url";
			}else{
				return "psp_redirect_url";
			}
		}


		return super.toColumn(field,returnAlias,appendTablePrefix);
		
	}
	
	@Override
	public String toTable(IField field,boolean returnAlias) throws ExpressionException {
		
		// In the case of table with alias, using parameter returnAlias​​, 
		// it is possible to drive the choice whether to return only the alias or 
		// the full definition of the table containing the alias
		
		if(field.equals(RPT.model().ID_VERSAMENTO.COD_VERSAMENTO_ENTE)){
			return this.toTable(RPT.model().ID_VERSAMENTO, returnAlias);
		}
		if(field.equals(RPT.model().ID_VERSAMENTO.COD_DOMINIO)){
			return this.toTable(RPT.model().ID_VERSAMENTO, returnAlias);
		}
		if(field.equals(RPT.model().ID_PSP.COD_PSP)){
			return this.toTable(RPT.model().ID_PSP, returnAlias);
		}
		if(field.equals(RPT.model().ID_CANALE.ID_PSP.COD_PSP)){
			return this.toTable(RPT.model().ID_CANALE.ID_PSP, returnAlias);
		}
		if(field.equals(RPT.model().ID_CANALE.COD_CANALE)){
			return this.toTable(RPT.model().ID_CANALE, returnAlias);
		}
		if(field.equals(RPT.model().ID_CANALE.TIPO_VERSAMENTO)){
			return this.toTable(RPT.model().ID_CANALE, returnAlias);
		}
		if(field.equals(RPT.model().ID_PORTALE.COD_PORTALE)){
			return this.toTable(RPT.model().ID_PORTALE, returnAlias);
		}
		if(field.equals(RPT.model().ID_TRACCIATO_XML.ID_TRACCIATO)){
			return this.toTable(RPT.model().ID_TRACCIATO_XML, returnAlias);
		}
		if(field.equals(RPT.model().ID_STAZIONE.COD_STAZIONE)){
			return this.toTable(RPT.model().ID_STAZIONE, returnAlias);
		}
		if(field.equals(RPT.model().COD_CARRELLO)){
			return this.toTable(RPT.model(), returnAlias);
		}
		if(field.equals(RPT.model().IUV)){
			return this.toTable(RPT.model(), returnAlias);
		}
		if(field.equals(RPT.model().CCP)){
			return this.toTable(RPT.model(), returnAlias);
		}
		if(field.equals(RPT.model().COD_DOMINIO)){
			return this.toTable(RPT.model(), returnAlias);
		}
		if(field.equals(RPT.model().TIPO_VERSAMENTO)){
			return this.toTable(RPT.model(), returnAlias);
		}
		if(field.equals(RPT.model().ID_ANAGRAFICA_VERSANTE.ID_ANAGRAFICA)){
			return this.toTable(RPT.model().ID_ANAGRAFICA_VERSANTE, returnAlias);
		}
		if(field.equals(RPT.model().ID_ANAGRAFICA_VERSANTE.COD_UNIVOCO)){
			return this.toTable(RPT.model().ID_ANAGRAFICA_VERSANTE, returnAlias);
		}
		if(field.equals(RPT.model().DATA_ORA_MSG_RICHIESTA)){
			return this.toTable(RPT.model(), returnAlias);
		}
		if(field.equals(RPT.model().DATA_ORA_CREAZIONE)){
			return this.toTable(RPT.model(), returnAlias);
		}
		if(field.equals(RPT.model().COD_MSG_RICHIESTA)){
			return this.toTable(RPT.model(), returnAlias);
		}
		if(field.equals(RPT.model().IBAN_ADDEBITO)){
			return this.toTable(RPT.model(), returnAlias);
		}
		if(field.equals(RPT.model().AUTENTICAZIONE_SOGGETTO)){
			return this.toTable(RPT.model(), returnAlias);
		}
		if(field.equals(RPT.model().FIRMA_RT)){
			return this.toTable(RPT.model(), returnAlias);
		}
		if(field.equals(RPT.model().STATO)){
			return this.toTable(RPT.model(), returnAlias);
		}
		if(field.equals(RPT.model().DESCRIZIONE_STATO)){
			return this.toTable(RPT.model(), returnAlias);
		}
		if(field.equals(RPT.model().COD_FAULT)){
			return this.toTable(RPT.model(), returnAlias);
		}
		if(field.equals(RPT.model().CALLBACK_URL)){
			return this.toTable(RPT.model(), returnAlias);
		}
		if(field.equals(RPT.model().COD_SESSIONE)){
			return this.toTable(RPT.model(), returnAlias);
		}
		if(field.equals(RPT.model().PSP_REDIRECT_URL)){
			return this.toTable(RPT.model(), returnAlias);
		}


		return super.toTable(field,returnAlias);
		
	}

	@Override
	public String toTable(IModel<?> model,boolean returnAlias) throws ExpressionException {
		
		// In the case of table with alias, using parameter returnAlias​​, 
		// it is possible to drive the choice whether to return only the alias or 
		// the full definition of the table containing the alias
		
		if(model.equals(RPT.model())){
			return "rpt";
		}
		if(model.equals(RPT.model().ID_VERSAMENTO)){
			return "versamenti";
		}
		if(model.equals(RPT.model().ID_PSP)){
			return "psp";
		}
		if(model.equals(RPT.model().ID_CANALE)){
			return "canali";
		}
		if(model.equals(RPT.model().ID_CANALE.ID_PSP)){
			return "id_psp";
		}
		if(model.equals(RPT.model().ID_PORTALE)){
			return "portali";
		}
		if(model.equals(RPT.model().ID_TRACCIATO_XML)){
			return "tracciatixml";
		}
		if(model.equals(RPT.model().ID_STAZIONE)){
			return "stazioni";
		}
		if(model.equals(RPT.model().ID_ANAGRAFICA_VERSANTE)){
			return "anagrafiche";
		}


		return super.toTable(model,returnAlias);
		
	}

}
