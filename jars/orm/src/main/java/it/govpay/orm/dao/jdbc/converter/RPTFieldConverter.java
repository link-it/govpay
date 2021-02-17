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
		if(field.equals(RPT.model().ID_VERSAMENTO.ID_APPLICAZIONE.COD_APPLICAZIONE)){
			if(appendTablePrefix){
				return this.toAliasTable(field)+".cod_applicazione";
			}else{
				return "cod_applicazione";
			}
		}
		if(field.equals(RPT.model().ID_VERSAMENTO.SRC_DEBITORE_IDENTIFICATIVO)){
			if(appendTablePrefix){
				return this.toAliasTable(field)+".src_debitore_identificativo";
			}else{
				return "src_debitore_identificativo";
			}
		}
		if(field.equals(RPT.model().ID_VERSAMENTO.DEBITORE_ANAGRAFICA)){
			if(appendTablePrefix){
				return this.toAliasTable(field)+".debitore_anagrafica";
			}else{
				return "debitore_anagrafica";
			}
		}
		if(field.equals(RPT.model().ID_VERSAMENTO.COD_VERSAMENTO_LOTTO)){
			if(appendTablePrefix){
				return this.toAliasTable(field)+".cod_versamento_lotto";
			}else{
				return "cod_versamento_lotto";
			}
		}
		if(field.equals(RPT.model().ID_VERSAMENTO.COD_ANNO_TRIBUTARIO)){
			if(appendTablePrefix){
				return this.toAliasTable(field)+".cod_anno_tributario";
			}else{
				return "cod_anno_tributario";
			}
		}
		if(field.equals(RPT.model().ID_VERSAMENTO.IMPORTO_TOTALE)){
			if(appendTablePrefix){
				return this.toAliasTable(field)+".importo_totale";
			}else{
				return "importo_totale";
			}
		}
		if(field.equals(RPT.model().ID_VERSAMENTO.CAUSALE_VERSAMENTO)){
			if(appendTablePrefix){
				return this.toAliasTable(field)+".causale_versamento";
			}else{
				return "causale_versamento";
			}
		}
		if(field.equals(RPT.model().ID_VERSAMENTO.STATO_VERSAMENTO)){
			if(appendTablePrefix){
				return this.toAliasTable(field)+".stato_versamento";
			}else{
				return "stato_versamento";
			}
		}
		if(field.equals(RPT.model().ID_VERSAMENTO.ID_UO.COD_UO)){
			if(appendTablePrefix){
				return this.toAliasTable(field)+".cod_uo";
			}else{
				return "cod_uo";
			}
		}
		if(field.equals(RPT.model().ID_VERSAMENTO.ID_UO.ID_DOMINIO.COD_DOMINIO)){
			if(appendTablePrefix){
				return this.toAliasTable(field)+".cod_dominio";
			}else{
				return "cod_dominio";
			}
		}
		if(field.equals(RPT.model().ID_VERSAMENTO.ID_TIPO_VERSAMENTO.COD_TIPO_VERSAMENTO)){
			if(appendTablePrefix){
				return this.toAliasTable(field)+".cod_tipo_versamento";
			}else{
				return "cod_tipo_versamento";
			}
		}
		if(field.equals(RPT.model().ID_VERSAMENTO.DIVISIONE)){
			if(appendTablePrefix){
				return this.toAliasTable(field)+".divisione";
			}else{
				return "divisione";
			}
		}
		if(field.equals(RPT.model().ID_VERSAMENTO.DIREZIONE)){
			if(appendTablePrefix){
				return this.toAliasTable(field)+".direzione";
			}else{
				return "direzione";
			}
		}
		if(field.equals(RPT.model().ID_VERSAMENTO.TASSONOMIA)){
			if(appendTablePrefix){
				return this.toAliasTable(field)+".tassonomia";
			}else{
				return "tassonomia";
			}
		}
		if(field.equals(RPT.model().ID_VERSAMENTO.TIPO)){
			if(appendTablePrefix){
				return this.toAliasTable(field)+".tipo";
			}else{
				return "tipo";
			}
		}
		if(field.equals(RPT.model().ID_PAGAMENTO_PORTALE.ID_SESSIONE)){
			if(appendTablePrefix){
				return this.toAliasTable(field)+".id_sessione";
			}else{
				return "id_sessione";
			}
		}
		if(field.equals(RPT.model().ID_PAGAMENTO_PORTALE.ID_APPLICAZIONE.COD_APPLICAZIONE)){
			if(appendTablePrefix){
				return this.toAliasTable(field)+".cod_applicazione";
			}else{
				return "cod_applicazione";
			}
		}
		if(field.equals(RPT.model().ID_PAGAMENTO_PORTALE.SRC_VERSANTE_IDENTIFICATIVO)){
			if(appendTablePrefix){
				return this.toAliasTable(field)+".src_versante_identificativo";
			}else{
				return "src_versante_identificativo";
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
		if(field.equals(RPT.model().COD_MSG_RICHIESTA)){
			if(appendTablePrefix){
				return this.toAliasTable(field)+".cod_msg_richiesta";
			}else{
				return "cod_msg_richiesta";
			}
		}
		if(field.equals(RPT.model().DATA_MSG_RICHIESTA)){
			if(appendTablePrefix){
				return this.toAliasTable(field)+".data_msg_richiesta";
			}else{
				return "data_msg_richiesta";
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
		if(field.equals(RPT.model().COD_SESSIONE)){
			if(appendTablePrefix){
				return this.toAliasTable(field)+".cod_sessione";
			}else{
				return "cod_sessione";
			}
		}
		if(field.equals(RPT.model().COD_SESSIONE_PORTALE)){
			if(appendTablePrefix){
				return this.toAliasTable(field)+".cod_sessione_portale";
			}else{
				return "cod_sessione_portale";
			}
		}
		if(field.equals(RPT.model().PSP_REDIRECT_URL)){
			if(appendTablePrefix){
				return this.toAliasTable(field)+".psp_redirect_url";
			}else{
				return "psp_redirect_url";
			}
		}
		if(field.equals(RPT.model().XML_RPT)){
			if(appendTablePrefix){
				return this.toAliasTable(field)+".xml_rpt";
			}else{
				return "xml_rpt";
			}
		}
		if(field.equals(RPT.model().DATA_AGGIORNAMENTO_STATO)){
			if(appendTablePrefix){
				return this.toAliasTable(field)+".data_aggiornamento_stato";
			}else{
				return "data_aggiornamento_stato";
			}
		}
		if(field.equals(RPT.model().CALLBACK_URL)){
			if(appendTablePrefix){
				return this.toAliasTable(field)+".callback_url";
			}else{
				return "callback_url";
			}
		}
		if(field.equals(RPT.model().MODELLO_PAGAMENTO)){
			if(appendTablePrefix){
				return this.toAliasTable(field)+".modello_pagamento";
			}else{
				return "modello_pagamento";
			}
		}
		if(field.equals(RPT.model().COD_MSG_RICEVUTA)){
			if(appendTablePrefix){
				return this.toAliasTable(field)+".cod_msg_ricevuta";
			}else{
				return "cod_msg_ricevuta";
			}
		}
		if(field.equals(RPT.model().DATA_MSG_RICEVUTA)){
			if(appendTablePrefix){
				return this.toAliasTable(field)+".data_msg_ricevuta";
			}else{
				return "data_msg_ricevuta";
			}
		}
		if(field.equals(RPT.model().COD_ESITO_PAGAMENTO)){
			if(appendTablePrefix){
				return this.toAliasTable(field)+".cod_esito_pagamento";
			}else{
				return "cod_esito_pagamento";
			}
		}
		if(field.equals(RPT.model().IMPORTO_TOTALE_PAGATO)){
			if(appendTablePrefix){
				return this.toAliasTable(field)+".importo_totale_pagato";
			}else{
				return "importo_totale_pagato";
			}
		}
		if(field.equals(RPT.model().XML_RT)){
			if(appendTablePrefix){
				return this.toAliasTable(field)+".xml_rt";
			}else{
				return "xml_rt";
			}
		}
		if(field.equals(RPT.model().COD_CANALE)){
			if(appendTablePrefix){
				return this.toAliasTable(field)+".cod_canale";
			}else{
				return "cod_canale";
			}
		}
		if(field.equals(RPT.model().COD_PSP)){
			if(appendTablePrefix){
				return this.toAliasTable(field)+".cod_psp";
			}else{
				return "cod_psp";
			}
		}
		if(field.equals(RPT.model().COD_INTERMEDIARIO_PSP)){
			if(appendTablePrefix){
				return this.toAliasTable(field)+".cod_intermediario_psp";
			}else{
				return "cod_intermediario_psp";
			}
		}
		if(field.equals(RPT.model().TIPO_VERSAMENTO)){
			if(appendTablePrefix){
				return this.toAliasTable(field)+".tipo_versamento";
			}else{
				return "tipo_versamento";
			}
		}
		if(field.equals(RPT.model().TIPO_IDENTIFICATIVO_ATTESTANTE)){
			if(appendTablePrefix){
				return this.toAliasTable(field)+".tipo_identificativo_attestante";
			}else{
				return "tipo_identificativo_attestante";
			}
		}
		if(field.equals(RPT.model().IDENTIFICATIVO_ATTESTANTE)){
			if(appendTablePrefix){
				return this.toAliasTable(field)+".identificativo_attestante";
			}else{
				return "identificativo_attestante";
			}
		}
		if(field.equals(RPT.model().DENOMINAZIONE_ATTESTANTE)){
			if(appendTablePrefix){
				return this.toAliasTable(field)+".denominazione_attestante";
			}else{
				return "denominazione_attestante";
			}
		}
		if(field.equals(RPT.model().COD_STAZIONE)){
			if(appendTablePrefix){
				return this.toAliasTable(field)+".cod_stazione";
			}else{
				return "cod_stazione";
			}
		}
		if(field.equals(RPT.model().COD_TRANSAZIONE_RPT)){
			if(appendTablePrefix){
				return this.toAliasTable(field)+".cod_transazione_rpt";
			}else{
				return "cod_transazione_rpt";
			}
		}
		if(field.equals(RPT.model().COD_TRANSAZIONE_RT)){
			if(appendTablePrefix){
				return this.toAliasTable(field)+".cod_transazione_rt";
			}else{
				return "cod_transazione_rt";
			}
		}
		if(field.equals(RPT.model().STATO_CONSERVAZIONE)){
			if(appendTablePrefix){
				return this.toAliasTable(field)+".stato_conservazione";
			}else{
				return "stato_conservazione";
			}
		}
		if(field.equals(RPT.model().DESCRIZIONE_STATO_CONS)){
			if(appendTablePrefix){
				return this.toAliasTable(field)+".descrizione_stato_cons";
			}else{
				return "descrizione_stato_cons";
			}
		}
		if(field.equals(RPT.model().DATA_CONSERVAZIONE)){
			if(appendTablePrefix){
				return this.toAliasTable(field)+".data_conservazione";
			}else{
				return "data_conservazione";
			}
		}
		if(field.equals(RPT.model().BLOCCANTE)){
			if(appendTablePrefix){
				return this.toAliasTable(field)+".bloccante";
			}else{
				return "bloccante";
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
		if(field.equals(RPT.model().ID_VERSAMENTO.ID_APPLICAZIONE.COD_APPLICAZIONE)){
			return this.toTable(RPT.model().ID_VERSAMENTO.ID_APPLICAZIONE, returnAlias);
		}
		if(field.equals(RPT.model().ID_VERSAMENTO.SRC_DEBITORE_IDENTIFICATIVO)){
			return this.toTable(RPT.model().ID_VERSAMENTO, returnAlias);
		}
		if(field.equals(RPT.model().ID_VERSAMENTO.DEBITORE_ANAGRAFICA)){
			return this.toTable(RPT.model().ID_VERSAMENTO, returnAlias);
		}
		if(field.equals(RPT.model().ID_VERSAMENTO.COD_VERSAMENTO_LOTTO)){
			return this.toTable(RPT.model().ID_VERSAMENTO, returnAlias);
		}
		if(field.equals(RPT.model().ID_VERSAMENTO.COD_ANNO_TRIBUTARIO)){
			return this.toTable(RPT.model().ID_VERSAMENTO, returnAlias);
		}
		if(field.equals(RPT.model().ID_VERSAMENTO.IMPORTO_TOTALE)){
			return this.toTable(RPT.model().ID_VERSAMENTO, returnAlias);
		}
		if(field.equals(RPT.model().ID_VERSAMENTO.CAUSALE_VERSAMENTO)){
			return this.toTable(RPT.model().ID_VERSAMENTO, returnAlias);
		}
		if(field.equals(RPT.model().ID_VERSAMENTO.STATO_VERSAMENTO)){
			return this.toTable(RPT.model().ID_VERSAMENTO, returnAlias);
		}
		if(field.equals(RPT.model().ID_VERSAMENTO.ID_UO.COD_UO)){
			return this.toTable(RPT.model().ID_VERSAMENTO.ID_UO, returnAlias);
		}
		if(field.equals(RPT.model().ID_VERSAMENTO.ID_UO.ID_DOMINIO.COD_DOMINIO)){
			return this.toTable(RPT.model().ID_VERSAMENTO.ID_UO.ID_DOMINIO, returnAlias);
		}
		if(field.equals(RPT.model().ID_VERSAMENTO.ID_TIPO_VERSAMENTO.COD_TIPO_VERSAMENTO)){
			return this.toTable(RPT.model().ID_VERSAMENTO.ID_TIPO_VERSAMENTO, returnAlias);
		}
		if(field.equals(RPT.model().ID_VERSAMENTO.DIVISIONE)){
			return this.toTable(RPT.model().ID_VERSAMENTO, returnAlias);
		}
		if(field.equals(RPT.model().ID_VERSAMENTO.DIREZIONE)){
			return this.toTable(RPT.model().ID_VERSAMENTO, returnAlias);
		}
		if(field.equals(RPT.model().ID_VERSAMENTO.TASSONOMIA)){
			return this.toTable(RPT.model().ID_VERSAMENTO, returnAlias);
		}
		if(field.equals(RPT.model().ID_VERSAMENTO.TIPO)){
			return this.toTable(RPT.model().ID_VERSAMENTO, returnAlias);
		}
		if(field.equals(RPT.model().ID_PAGAMENTO_PORTALE.ID_SESSIONE)){
			return this.toTable(RPT.model().ID_PAGAMENTO_PORTALE, returnAlias);
		}
		if(field.equals(RPT.model().ID_PAGAMENTO_PORTALE.ID_APPLICAZIONE.COD_APPLICAZIONE)){
			return this.toTable(RPT.model().ID_PAGAMENTO_PORTALE.ID_APPLICAZIONE, returnAlias);
		}
		if(field.equals(RPT.model().ID_PAGAMENTO_PORTALE.SRC_VERSANTE_IDENTIFICATIVO)){
			return this.toTable(RPT.model().ID_PAGAMENTO_PORTALE, returnAlias);
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
		if(field.equals(RPT.model().COD_MSG_RICHIESTA)){
			return this.toTable(RPT.model(), returnAlias);
		}
		if(field.equals(RPT.model().DATA_MSG_RICHIESTA)){
			return this.toTable(RPT.model(), returnAlias);
		}
		if(field.equals(RPT.model().STATO)){
			return this.toTable(RPT.model(), returnAlias);
		}
		if(field.equals(RPT.model().DESCRIZIONE_STATO)){
			return this.toTable(RPT.model(), returnAlias);
		}
		if(field.equals(RPT.model().COD_SESSIONE)){
			return this.toTable(RPT.model(), returnAlias);
		}
		if(field.equals(RPT.model().COD_SESSIONE_PORTALE)){
			return this.toTable(RPT.model(), returnAlias);
		}
		if(field.equals(RPT.model().PSP_REDIRECT_URL)){
			return this.toTable(RPT.model(), returnAlias);
		}
		if(field.equals(RPT.model().XML_RPT)){
			return this.toTable(RPT.model(), returnAlias);
		}
		if(field.equals(RPT.model().DATA_AGGIORNAMENTO_STATO)){
			return this.toTable(RPT.model(), returnAlias);
		}
		if(field.equals(RPT.model().CALLBACK_URL)){
			return this.toTable(RPT.model(), returnAlias);
		}
		if(field.equals(RPT.model().MODELLO_PAGAMENTO)){
			return this.toTable(RPT.model(), returnAlias);
		}
		if(field.equals(RPT.model().COD_MSG_RICEVUTA)){
			return this.toTable(RPT.model(), returnAlias);
		}
		if(field.equals(RPT.model().DATA_MSG_RICEVUTA)){
			return this.toTable(RPT.model(), returnAlias);
		}
		if(field.equals(RPT.model().COD_ESITO_PAGAMENTO)){
			return this.toTable(RPT.model(), returnAlias);
		}
		if(field.equals(RPT.model().IMPORTO_TOTALE_PAGATO)){
			return this.toTable(RPT.model(), returnAlias);
		}
		if(field.equals(RPT.model().XML_RT)){
			return this.toTable(RPT.model(), returnAlias);
		}
		if(field.equals(RPT.model().COD_CANALE)){
			return this.toTable(RPT.model(), returnAlias);
		}
		if(field.equals(RPT.model().COD_PSP)){
			return this.toTable(RPT.model(), returnAlias);
		}
		if(field.equals(RPT.model().COD_INTERMEDIARIO_PSP)){
			return this.toTable(RPT.model(), returnAlias);
		}
		if(field.equals(RPT.model().TIPO_VERSAMENTO)){
			return this.toTable(RPT.model(), returnAlias);
		}
		if(field.equals(RPT.model().TIPO_IDENTIFICATIVO_ATTESTANTE)){
			return this.toTable(RPT.model(), returnAlias);
		}
		if(field.equals(RPT.model().IDENTIFICATIVO_ATTESTANTE)){
			return this.toTable(RPT.model(), returnAlias);
		}
		if(field.equals(RPT.model().DENOMINAZIONE_ATTESTANTE)){
			return this.toTable(RPT.model(), returnAlias);
		}
		if(field.equals(RPT.model().COD_STAZIONE)){
			return this.toTable(RPT.model(), returnAlias);
		}
		if(field.equals(RPT.model().COD_TRANSAZIONE_RPT)){
			return this.toTable(RPT.model(), returnAlias);
		}
		if(field.equals(RPT.model().COD_TRANSAZIONE_RT)){
			return this.toTable(RPT.model(), returnAlias);
		}
		if(field.equals(RPT.model().STATO_CONSERVAZIONE)){
			return this.toTable(RPT.model(), returnAlias);
		}
		if(field.equals(RPT.model().DESCRIZIONE_STATO_CONS)){
			return this.toTable(RPT.model(), returnAlias);
		}
		if(field.equals(RPT.model().DATA_CONSERVAZIONE)){
			return this.toTable(RPT.model(), returnAlias);
		}
		if(field.equals(RPT.model().BLOCCANTE)){
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
		if(model.equals(RPT.model().ID_VERSAMENTO.ID_APPLICAZIONE)){
			return "applicazioni";
		}
		if(model.equals(RPT.model().ID_VERSAMENTO.ID_UO)){
			return "uo";
		}
		if(model.equals(RPT.model().ID_VERSAMENTO.ID_UO.ID_DOMINIO)){
			return "domini";
		}
		if(model.equals(RPT.model().ID_VERSAMENTO.ID_TIPO_VERSAMENTO)){
			return "tipi_versamento";
		}
		if(model.equals(RPT.model().ID_PAGAMENTO_PORTALE)){
			return "pagamenti_portale";
		}
		if(model.equals(RPT.model().ID_PAGAMENTO_PORTALE.ID_APPLICAZIONE)){
			return "applicazioni";
		}


		return super.toTable(model,returnAlias);
		
	}

}
