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

import it.govpay.orm.PagamentoPortale;


/**     
 * PagamentoPortaleFieldConverter
 *
 * @author Giovanni Bussu (bussu@link.it)
 * @author Lorenzo Nardi (nardi@link.it)
 * @author $Author$
 * @version $Rev$, $Date$
 */
public class PagamentoPortaleFieldConverter extends AbstractSQLFieldConverter {

	private TipiDatabase databaseType;
	
	public PagamentoPortaleFieldConverter(String databaseType){
		this.databaseType = TipiDatabase.toEnumConstant(databaseType);
	}
	public PagamentoPortaleFieldConverter(TipiDatabase databaseType){
		this.databaseType = databaseType;
	}


	@Override
	public IModel<?> getRootModel() throws ExpressionException {
		return PagamentoPortale.model();
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
		
		if(field.equals(PagamentoPortale.model().COD_APPLICAZIONE)){
			if(appendTablePrefix){
				return this.toAliasTable(field)+".cod_applicazione";
			}else{
				return "cod_applicazione";
			}
		}
		if(field.equals(PagamentoPortale.model().COD_CANALE)){
			if(appendTablePrefix){
				return this.toAliasTable(field)+".cod_canale";
			}else{
				return "cod_canale";
			}
		}
		if(field.equals(PagamentoPortale.model().NOME)){
			if(appendTablePrefix){
				return this.toAliasTable(field)+".nome";
			}else{
				return "nome";
			}
		}
		if(field.equals(PagamentoPortale.model().IMPORTO)){
			if(appendTablePrefix){
				return this.toAliasTable(field)+".importo";
			}else{
				return "importo";
			}
		}
		if(field.equals(PagamentoPortale.model().VERSANTE_IDENTIFICATIVO)){
			if(appendTablePrefix){
				return this.toAliasTable(field)+".versante_identificativo";
			}else{
				return "versante_identificativo";
			}
		}
		if(field.equals(PagamentoPortale.model().ID_SESSIONE)){
			if(appendTablePrefix){
				return this.toAliasTable(field)+".id_sessione";
			}else{
				return "id_sessione";
			}
		}
		if(field.equals(PagamentoPortale.model().ID_SESSIONE_PORTALE)){
			if(appendTablePrefix){
				return this.toAliasTable(field)+".id_sessione_portale";
			}else{
				return "id_sessione_portale";
			}
		}
		if(field.equals(PagamentoPortale.model().ID_SESSIONE_PSP)){
			if(appendTablePrefix){
				return this.toAliasTable(field)+".id_sessione_psp";
			}else{
				return "id_sessione_psp";
			}
		}
		if(field.equals(PagamentoPortale.model().STATO)){
			if(appendTablePrefix){
				return this.toAliasTable(field)+".stato";
			}else{
				return "stato";
			}
		}
		if(field.equals(PagamentoPortale.model().CODICE_STATO)){
			if(appendTablePrefix){
				return this.toAliasTable(field)+".codice_stato";
			}else{
				return "codice_stato";
			}
		}
		if(field.equals(PagamentoPortale.model().DESCRIZIONE_STATO)){
			if(appendTablePrefix){
				return this.toAliasTable(field)+".descrizione_stato";
			}else{
				return "descrizione_stato";
			}
		}
		if(field.equals(PagamentoPortale.model().PSP_REDIRECT_URL)){
			if(appendTablePrefix){
				return this.toAliasTable(field)+".psp_redirect_url";
			}else{
				return "psp_redirect_url";
			}
		}
		if(field.equals(PagamentoPortale.model().PSP_ESITO)){
			if(appendTablePrefix){
				return this.toAliasTable(field)+".psp_esito";
			}else{
				return "psp_esito";
			}
		}
		if(field.equals(PagamentoPortale.model().JSON_REQUEST)){
			if(appendTablePrefix){
				return this.toAliasTable(field)+".json_request";
			}else{
				return "json_request";
			}
		}
		if(field.equals(PagamentoPortale.model().WISP_ID_DOMINIO)){
			if(appendTablePrefix){
				return this.toAliasTable(field)+".wisp_id_dominio";
			}else{
				return "wisp_id_dominio";
			}
		}
		if(field.equals(PagamentoPortale.model().WISP_KEY_PA)){
			if(appendTablePrefix){
				return this.toAliasTable(field)+".wisp_key_pa";
			}else{
				return "wisp_key_pa";
			}
		}
		if(field.equals(PagamentoPortale.model().WISP_KEY_WISP)){
			if(appendTablePrefix){
				return this.toAliasTable(field)+".wisp_key_wisp";
			}else{
				return "wisp_key_wisp";
			}
		}
		if(field.equals(PagamentoPortale.model().WISP_HTML)){
			if(appendTablePrefix){
				return this.toAliasTable(field)+".wisp_html";
			}else{
				return "wisp_html";
			}
		}
		if(field.equals(PagamentoPortale.model().DATA_RICHIESTA)){
			if(appendTablePrefix){
				return this.toAliasTable(field)+".data_richiesta";
			}else{
				return "data_richiesta";
			}
		}
		if(field.equals(PagamentoPortale.model().URL_RITORNO)){
			if(appendTablePrefix){
				return this.toAliasTable(field)+".url_ritorno";
			}else{
				return "url_ritorno";
			}
		}
		if(field.equals(PagamentoPortale.model().COD_PSP)){
			if(appendTablePrefix){
				return this.toAliasTable(field)+".cod_psp";
			}else{
				return "cod_psp";
			}
		}
		if(field.equals(PagamentoPortale.model().TIPO_VERSAMENTO)){
			if(appendTablePrefix){
				return this.toAliasTable(field)+".tipo_versamento";
			}else{
				return "tipo_versamento";
			}
		}
		if(field.equals(PagamentoPortale.model().MULTI_BENEFICIARIO)){
			if(appendTablePrefix){
				return this.toAliasTable(field)+".multi_beneficiario";
			}else{
				return "multi_beneficiario";
			}
		}


		return super.toColumn(field,returnAlias,appendTablePrefix);
		
	}
	
	@Override
	public String toTable(IField field,boolean returnAlias) throws ExpressionException {
		
		// In the case of table with alias, using parameter returnAlias​​, 
		// it is possible to drive the choice whether to return only the alias or 
		// the full definition of the table containing the alias
		
		if(field.equals(PagamentoPortale.model().COD_APPLICAZIONE)){
			return this.toTable(PagamentoPortale.model(), returnAlias);
		}
		if(field.equals(PagamentoPortale.model().COD_CANALE)){
			return this.toTable(PagamentoPortale.model(), returnAlias);
		}
		if(field.equals(PagamentoPortale.model().NOME)){
			return this.toTable(PagamentoPortale.model(), returnAlias);
		}
		if(field.equals(PagamentoPortale.model().IMPORTO)){
			return this.toTable(PagamentoPortale.model(), returnAlias);
		}
		if(field.equals(PagamentoPortale.model().VERSANTE_IDENTIFICATIVO)){
			return this.toTable(PagamentoPortale.model(), returnAlias);
		}
		if(field.equals(PagamentoPortale.model().ID_SESSIONE)){
			return this.toTable(PagamentoPortale.model(), returnAlias);
		}
		if(field.equals(PagamentoPortale.model().ID_SESSIONE_PORTALE)){
			return this.toTable(PagamentoPortale.model(), returnAlias);
		}
		if(field.equals(PagamentoPortale.model().ID_SESSIONE_PSP)){
			return this.toTable(PagamentoPortale.model(), returnAlias);
		}
		if(field.equals(PagamentoPortale.model().STATO)){
			return this.toTable(PagamentoPortale.model(), returnAlias);
		}
		if(field.equals(PagamentoPortale.model().CODICE_STATO)){
			return this.toTable(PagamentoPortale.model(), returnAlias);
		}
		if(field.equals(PagamentoPortale.model().DESCRIZIONE_STATO)){
			return this.toTable(PagamentoPortale.model(), returnAlias);
		}
		if(field.equals(PagamentoPortale.model().PSP_REDIRECT_URL)){
			return this.toTable(PagamentoPortale.model(), returnAlias);
		}
		if(field.equals(PagamentoPortale.model().PSP_ESITO)){
			return this.toTable(PagamentoPortale.model(), returnAlias);
		}
		if(field.equals(PagamentoPortale.model().JSON_REQUEST)){
			return this.toTable(PagamentoPortale.model(), returnAlias);
		}
		if(field.equals(PagamentoPortale.model().WISP_ID_DOMINIO)){
			return this.toTable(PagamentoPortale.model(), returnAlias);
		}
		if(field.equals(PagamentoPortale.model().WISP_KEY_PA)){
			return this.toTable(PagamentoPortale.model(), returnAlias);
		}
		if(field.equals(PagamentoPortale.model().WISP_KEY_WISP)){
			return this.toTable(PagamentoPortale.model(), returnAlias);
		}
		if(field.equals(PagamentoPortale.model().WISP_HTML)){
			return this.toTable(PagamentoPortale.model(), returnAlias);
		}
		if(field.equals(PagamentoPortale.model().DATA_RICHIESTA)){
			return this.toTable(PagamentoPortale.model(), returnAlias);
		}
		if(field.equals(PagamentoPortale.model().URL_RITORNO)){
			return this.toTable(PagamentoPortale.model(), returnAlias);
		}
		if(field.equals(PagamentoPortale.model().COD_PSP)){
			return this.toTable(PagamentoPortale.model(), returnAlias);
		}
		if(field.equals(PagamentoPortale.model().TIPO_VERSAMENTO)){
			return this.toTable(PagamentoPortale.model(), returnAlias);
		}
		if(field.equals(PagamentoPortale.model().MULTI_BENEFICIARIO)){
			return this.toTable(PagamentoPortale.model(), returnAlias);
		}


		return super.toTable(field,returnAlias);
		
	}

	@Override
	public String toTable(IModel<?> model,boolean returnAlias) throws ExpressionException {
		
		// In the case of table with alias, using parameter returnAlias​​, 
		// it is possible to drive the choice whether to return only the alias or 
		// the full definition of the table containing the alias
		
		if(model.equals(PagamentoPortale.model())){
			return "pagamenti_portale";
		}


		return super.toTable(model,returnAlias);
		
	}

}
