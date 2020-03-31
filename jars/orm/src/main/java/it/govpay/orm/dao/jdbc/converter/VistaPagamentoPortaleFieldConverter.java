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

import it.govpay.orm.VistaPagamentoPortale;


/**     
 * VistaPagamentoPortaleFieldConverter
 *
 * @author Giovanni Bussu (bussu@link.it)
 * @author Lorenzo Nardi (nardi@link.it)
 * @author $Author$
 * @version $Rev$, $Date$
 */
public class VistaPagamentoPortaleFieldConverter extends AbstractSQLFieldConverter {

	private TipiDatabase databaseType;
	
	public VistaPagamentoPortaleFieldConverter(String databaseType){
		this.databaseType = TipiDatabase.toEnumConstant(databaseType);
	}
	public VistaPagamentoPortaleFieldConverter(TipiDatabase databaseType){
		this.databaseType = databaseType;
	}


	@Override
	public IModel<?> getRootModel() throws ExpressionException {
		return VistaPagamentoPortale.model();
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
		
		if(field.equals(VistaPagamentoPortale.model().COD_CANALE)){
			if(appendTablePrefix){
				return this.toAliasTable(field)+".cod_canale";
			}else{
				return "cod_canale";
			}
		}
		if(field.equals(VistaPagamentoPortale.model().NOME)){
			if(appendTablePrefix){
				return this.toAliasTable(field)+".nome";
			}else{
				return "nome";
			}
		}
		if(field.equals(VistaPagamentoPortale.model().IMPORTO)){
			if(appendTablePrefix){
				return this.toAliasTable(field)+".importo";
			}else{
				return "importo";
			}
		}
		if(field.equals(VistaPagamentoPortale.model().VERSANTE_IDENTIFICATIVO)){
			if(appendTablePrefix){
				return this.toAliasTable(field)+".versante_identificativo";
			}else{
				return "versante_identificativo";
			}
		}
		if(field.equals(VistaPagamentoPortale.model().ID_SESSIONE)){
			if(appendTablePrefix){
				return this.toAliasTable(field)+".id_sessione";
			}else{
				return "id_sessione";
			}
		}
		if(field.equals(VistaPagamentoPortale.model().ID_SESSIONE_PORTALE)){
			if(appendTablePrefix){
				return this.toAliasTable(field)+".id_sessione_portale";
			}else{
				return "id_sessione_portale";
			}
		}
		if(field.equals(VistaPagamentoPortale.model().ID_SESSIONE_PSP)){
			if(appendTablePrefix){
				return this.toAliasTable(field)+".id_sessione_psp";
			}else{
				return "id_sessione_psp";
			}
		}
		if(field.equals(VistaPagamentoPortale.model().STATO)){
			if(appendTablePrefix){
				return this.toAliasTable(field)+".stato";
			}else{
				return "stato";
			}
		}
		if(field.equals(VistaPagamentoPortale.model().CODICE_STATO)){
			if(appendTablePrefix){
				return this.toAliasTable(field)+".codice_stato";
			}else{
				return "codice_stato";
			}
		}
		if(field.equals(VistaPagamentoPortale.model().DESCRIZIONE_STATO)){
			if(appendTablePrefix){
				return this.toAliasTable(field)+".descrizione_stato";
			}else{
				return "descrizione_stato";
			}
		}
		if(field.equals(VistaPagamentoPortale.model().PSP_REDIRECT_URL)){
			if(appendTablePrefix){
				return this.toAliasTable(field)+".psp_redirect_url";
			}else{
				return "psp_redirect_url";
			}
		}
		if(field.equals(VistaPagamentoPortale.model().PSP_ESITO)){
			if(appendTablePrefix){
				return this.toAliasTable(field)+".psp_esito";
			}else{
				return "psp_esito";
			}
		}
		if(field.equals(VistaPagamentoPortale.model().DATA_RICHIESTA)){
			if(appendTablePrefix){
				return this.toAliasTable(field)+".data_richiesta";
			}else{
				return "data_richiesta";
			}
		}
		if(field.equals(VistaPagamentoPortale.model().URL_RITORNO)){
			if(appendTablePrefix){
				return this.toAliasTable(field)+".url_ritorno";
			}else{
				return "url_ritorno";
			}
		}
		if(field.equals(VistaPagamentoPortale.model().COD_PSP)){
			if(appendTablePrefix){
				return this.toAliasTable(field)+".cod_psp";
			}else{
				return "cod_psp";
			}
		}
		if(field.equals(VistaPagamentoPortale.model().TIPO_VERSAMENTO)){
			if(appendTablePrefix){
				return this.toAliasTable(field)+".tipo_versamento";
			}else{
				return "tipo_versamento";
			}
		}
		if(field.equals(VistaPagamentoPortale.model().MULTI_BENEFICIARIO)){
			if(appendTablePrefix){
				return this.toAliasTable(field)+".multi_beneficiario";
			}else{
				return "multi_beneficiario";
			}
		}
		if(field.equals(VistaPagamentoPortale.model().ACK)){
			if(appendTablePrefix){
				return this.toAliasTable(field)+".ack";
			}else{
				return "ack";
			}
		}
		if(field.equals(VistaPagamentoPortale.model().TIPO)){
			if(appendTablePrefix){
				return this.toAliasTable(field)+".tipo";
			}else{
				return "tipo";
			}
		}
		if(field.equals(VistaPagamentoPortale.model().PRINCIPAL)){
			if(appendTablePrefix){
				return this.toAliasTable(field)+".principal";
			}else{
				return "principal";
			}
		}
		if(field.equals(VistaPagamentoPortale.model().TIPO_UTENZA)){
			if(appendTablePrefix){
				return this.toAliasTable(field)+".tipo_utenza";
			}else{
				return "tipo_utenza";
			}
		}
		if(field.equals(VistaPagamentoPortale.model().DEBITORE_IDENTIFICATIVO)){
			if(appendTablePrefix){
				return this.toAliasTable(field)+".debitore_identificativo";
			}else{
				return "debitore_identificativo";
			}
		}
		if(field.equals(VistaPagamentoPortale.model().ID_APPLICAZIONE.COD_APPLICAZIONE)){
			if(appendTablePrefix){
				return this.toAliasTable(field)+".cod_applicazione";
			}else{
				return "cod_applicazione";
			}
		}
		if(field.equals(VistaPagamentoPortale.model().ID_TIPO_VERSAMENTO.COD_TIPO_VERSAMENTO)){
			if(appendTablePrefix){
				return this.toAliasTable(field)+".cod_tipo_versamento";
			}else{
				return "cod_tipo_versamento";
			}
		}
		if(field.equals(VistaPagamentoPortale.model().ID_TIPO_VERSAMENTO.TIPO)){
			if(appendTablePrefix){
				return this.toAliasTable(field)+".tipo";
			}else{
				return "tipo";
			}
		}
		if(field.equals(VistaPagamentoPortale.model().ID_DOMINIO.COD_DOMINIO)){
			if(appendTablePrefix){
				return this.toAliasTable(field)+".cod_dominio";
			}else{
				return "cod_dominio";
			}
		}
		if(field.equals(VistaPagamentoPortale.model().ID_UO.COD_UO)){
			if(appendTablePrefix){
				return this.toAliasTable(field)+".cod_uo";
			}else{
				return "cod_uo";
			}
		}
		if(field.equals(VistaPagamentoPortale.model().ID_UO.ID_DOMINIO.COD_DOMINIO)){
			if(appendTablePrefix){
				return this.toAliasTable(field)+".cod_dominio";
			}else{
				return "cod_dominio";
			}
		}
		if(field.equals(VistaPagamentoPortale.model().SRC_VERSANTE_IDENTIFICATIVO)){
			if(appendTablePrefix){
				return this.toAliasTable(field)+".src_versante_identificativo";
			}else{
				return "src_versante_identificativo";
			}
		}
		if(field.equals(VistaPagamentoPortale.model().SRC_DEBITORE_IDENTIFICATIVO)){
			if(appendTablePrefix){
				return this.toAliasTable(field)+".src_debitore_identificativo";
			}else{
				return "src_debitore_identificativo";
			}
		}


		return super.toColumn(field,returnAlias,appendTablePrefix);
		
	}
	
	@Override
	public String toTable(IField field,boolean returnAlias) throws ExpressionException {
		
		// In the case of table with alias, using parameter returnAlias​​, 
		// it is possible to drive the choice whether to return only the alias or 
		// the full definition of the table containing the alias
		
		if(field.equals(VistaPagamentoPortale.model().COD_CANALE)){
			return this.toTable(VistaPagamentoPortale.model(), returnAlias);
		}
		if(field.equals(VistaPagamentoPortale.model().NOME)){
			return this.toTable(VistaPagamentoPortale.model(), returnAlias);
		}
		if(field.equals(VistaPagamentoPortale.model().IMPORTO)){
			return this.toTable(VistaPagamentoPortale.model(), returnAlias);
		}
		if(field.equals(VistaPagamentoPortale.model().VERSANTE_IDENTIFICATIVO)){
			return this.toTable(VistaPagamentoPortale.model(), returnAlias);
		}
		if(field.equals(VistaPagamentoPortale.model().ID_SESSIONE)){
			return this.toTable(VistaPagamentoPortale.model(), returnAlias);
		}
		if(field.equals(VistaPagamentoPortale.model().ID_SESSIONE_PORTALE)){
			return this.toTable(VistaPagamentoPortale.model(), returnAlias);
		}
		if(field.equals(VistaPagamentoPortale.model().ID_SESSIONE_PSP)){
			return this.toTable(VistaPagamentoPortale.model(), returnAlias);
		}
		if(field.equals(VistaPagamentoPortale.model().STATO)){
			return this.toTable(VistaPagamentoPortale.model(), returnAlias);
		}
		if(field.equals(VistaPagamentoPortale.model().CODICE_STATO)){
			return this.toTable(VistaPagamentoPortale.model(), returnAlias);
		}
		if(field.equals(VistaPagamentoPortale.model().DESCRIZIONE_STATO)){
			return this.toTable(VistaPagamentoPortale.model(), returnAlias);
		}
		if(field.equals(VistaPagamentoPortale.model().PSP_REDIRECT_URL)){
			return this.toTable(VistaPagamentoPortale.model(), returnAlias);
		}
		if(field.equals(VistaPagamentoPortale.model().PSP_ESITO)){
			return this.toTable(VistaPagamentoPortale.model(), returnAlias);
		}
		if(field.equals(VistaPagamentoPortale.model().DATA_RICHIESTA)){
			return this.toTable(VistaPagamentoPortale.model(), returnAlias);
		}
		if(field.equals(VistaPagamentoPortale.model().URL_RITORNO)){
			return this.toTable(VistaPagamentoPortale.model(), returnAlias);
		}
		if(field.equals(VistaPagamentoPortale.model().COD_PSP)){
			return this.toTable(VistaPagamentoPortale.model(), returnAlias);
		}
		if(field.equals(VistaPagamentoPortale.model().TIPO_VERSAMENTO)){
			return this.toTable(VistaPagamentoPortale.model(), returnAlias);
		}
		if(field.equals(VistaPagamentoPortale.model().MULTI_BENEFICIARIO)){
			return this.toTable(VistaPagamentoPortale.model(), returnAlias);
		}
		if(field.equals(VistaPagamentoPortale.model().ACK)){
			return this.toTable(VistaPagamentoPortale.model(), returnAlias);
		}
		if(field.equals(VistaPagamentoPortale.model().TIPO)){
			return this.toTable(VistaPagamentoPortale.model(), returnAlias);
		}
		if(field.equals(VistaPagamentoPortale.model().PRINCIPAL)){
			return this.toTable(VistaPagamentoPortale.model(), returnAlias);
		}
		if(field.equals(VistaPagamentoPortale.model().TIPO_UTENZA)){
			return this.toTable(VistaPagamentoPortale.model(), returnAlias);
		}
		if(field.equals(VistaPagamentoPortale.model().DEBITORE_IDENTIFICATIVO)){
			return this.toTable(VistaPagamentoPortale.model(), returnAlias);
		}
		if(field.equals(VistaPagamentoPortale.model().ID_APPLICAZIONE.COD_APPLICAZIONE)){
			return this.toTable(VistaPagamentoPortale.model().ID_APPLICAZIONE, returnAlias);
		}
		if(field.equals(VistaPagamentoPortale.model().ID_TIPO_VERSAMENTO.COD_TIPO_VERSAMENTO)){
			return this.toTable(VistaPagamentoPortale.model().ID_TIPO_VERSAMENTO, returnAlias);
		}
		if(field.equals(VistaPagamentoPortale.model().ID_TIPO_VERSAMENTO.TIPO)){
			return this.toTable(VistaPagamentoPortale.model().ID_TIPO_VERSAMENTO, returnAlias);
		}
		if(field.equals(VistaPagamentoPortale.model().ID_DOMINIO.COD_DOMINIO)){
			return this.toTable(VistaPagamentoPortale.model().ID_DOMINIO, returnAlias);
		}
		if(field.equals(VistaPagamentoPortale.model().ID_UO.COD_UO)){
			return this.toTable(VistaPagamentoPortale.model().ID_UO, returnAlias);
		}
		if(field.equals(VistaPagamentoPortale.model().ID_UO.ID_DOMINIO.COD_DOMINIO)){
			return this.toTable(VistaPagamentoPortale.model().ID_UO.ID_DOMINIO, returnAlias);
		}
		if(field.equals(VistaPagamentoPortale.model().SRC_VERSANTE_IDENTIFICATIVO)){
			return this.toTable(VistaPagamentoPortale.model(), returnAlias);
		}
		if(field.equals(VistaPagamentoPortale.model().SRC_DEBITORE_IDENTIFICATIVO)){
			return this.toTable(VistaPagamentoPortale.model(), returnAlias);
		}


		return super.toTable(field,returnAlias);
		
	}

	@Override
	public String toTable(IModel<?> model,boolean returnAlias) throws ExpressionException {
		
		// In the case of table with alias, using parameter returnAlias​​, 
		// it is possible to drive the choice whether to return only the alias or 
		// the full definition of the table containing the alias
		
		if(model.equals(VistaPagamentoPortale.model())){
			return "v_pagamenti_portale";
		}
		if(model.equals(VistaPagamentoPortale.model().ID_APPLICAZIONE)){
			return "applicazioni";
		}
		if(model.equals(VistaPagamentoPortale.model().ID_TIPO_VERSAMENTO)){
			return "tipi_versamento";
		}
		if(model.equals(VistaPagamentoPortale.model().ID_DOMINIO)){
			return "domini";
		}
		if(model.equals(VistaPagamentoPortale.model().ID_UO)){
			return "uo";
		}
		if(model.equals(VistaPagamentoPortale.model().ID_UO.ID_DOMINIO)){
			return "domini";
		}


		return super.toTable(model,returnAlias);
		
	}

}
