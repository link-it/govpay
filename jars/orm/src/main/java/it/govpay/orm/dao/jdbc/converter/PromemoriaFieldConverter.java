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

import it.govpay.orm.Promemoria;


/**     
 * PromemoriaFieldConverter
 *
 * @author Giovanni Bussu (bussu@link.it)
 * @author Lorenzo Nardi (nardi@link.it)
 * @author $Author$
 * @version $Rev$, $Date$
 */
public class PromemoriaFieldConverter extends AbstractSQLFieldConverter {

	private TipiDatabase databaseType;
	
	public PromemoriaFieldConverter(String databaseType){
		this.databaseType = TipiDatabase.toEnumConstant(databaseType);
	}
	public PromemoriaFieldConverter(TipiDatabase databaseType){
		this.databaseType = databaseType;
	}


	@Override
	public IModel<?> getRootModel() throws ExpressionException {
		return Promemoria.model();
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
		
		if(field.equals(Promemoria.model().ID_VERSAMENTO.COD_VERSAMENTO_ENTE)){
			if(appendTablePrefix){
				return this.toAliasTable(field)+".cod_versamento_ente";
			}else{
				return "cod_versamento_ente";
			}
		}
		if(field.equals(Promemoria.model().ID_VERSAMENTO.ID_APPLICAZIONE.COD_APPLICAZIONE)){
			if(appendTablePrefix){
				return this.toAliasTable(field)+".cod_applicazione";
			}else{
				return "cod_applicazione";
			}
		}
		if(field.equals(Promemoria.model().ID_VERSAMENTO.SRC_DEBITORE_IDENTIFICATIVO)){
			if(appendTablePrefix){
				return this.toAliasTable(field)+".src_debitore_identificativo";
			}else{
				return "src_debitore_identificativo";
			}
		}
		if(field.equals(Promemoria.model().ID_VERSAMENTO.DEBITORE_ANAGRAFICA)){
			if(appendTablePrefix){
				return this.toAliasTable(field)+".debitore_anagrafica";
			}else{
				return "debitore_anagrafica";
			}
		}
		if(field.equals(Promemoria.model().ID_VERSAMENTO.COD_VERSAMENTO_LOTTO)){
			if(appendTablePrefix){
				return this.toAliasTable(field)+".cod_versamento_lotto";
			}else{
				return "cod_versamento_lotto";
			}
		}
		if(field.equals(Promemoria.model().ID_VERSAMENTO.COD_ANNO_TRIBUTARIO)){
			if(appendTablePrefix){
				return this.toAliasTable(field)+".cod_anno_tributario";
			}else{
				return "cod_anno_tributario";
			}
		}
		if(field.equals(Promemoria.model().ID_VERSAMENTO.IMPORTO_TOTALE)){
			if(appendTablePrefix){
				return this.toAliasTable(field)+".importo_totale";
			}else{
				return "importo_totale";
			}
		}
		if(field.equals(Promemoria.model().ID_VERSAMENTO.CAUSALE_VERSAMENTO)){
			if(appendTablePrefix){
				return this.toAliasTable(field)+".causale_versamento";
			}else{
				return "causale_versamento";
			}
		}
		if(field.equals(Promemoria.model().ID_VERSAMENTO.STATO_VERSAMENTO)){
			if(appendTablePrefix){
				return this.toAliasTable(field)+".stato_versamento";
			}else{
				return "stato_versamento";
			}
		}
		if(field.equals(Promemoria.model().ID_VERSAMENTO.ID_UO.COD_UO)){
			if(appendTablePrefix){
				return this.toAliasTable(field)+".cod_uo";
			}else{
				return "cod_uo";
			}
		}
		if(field.equals(Promemoria.model().ID_VERSAMENTO.ID_UO.ID_DOMINIO.COD_DOMINIO)){
			if(appendTablePrefix){
				return this.toAliasTable(field)+".cod_dominio";
			}else{
				return "cod_dominio";
			}
		}
		if(field.equals(Promemoria.model().ID_VERSAMENTO.ID_TIPO_VERSAMENTO.COD_TIPO_VERSAMENTO)){
			if(appendTablePrefix){
				return this.toAliasTable(field)+".cod_tipo_versamento";
			}else{
				return "cod_tipo_versamento";
			}
		}
		if(field.equals(Promemoria.model().ID_VERSAMENTO.DIVISIONE)){
			if(appendTablePrefix){
				return this.toAliasTable(field)+".divisione";
			}else{
				return "divisione";
			}
		}
		if(field.equals(Promemoria.model().ID_VERSAMENTO.DIREZIONE)){
			if(appendTablePrefix){
				return this.toAliasTable(field)+".direzione";
			}else{
				return "direzione";
			}
		}
		if(field.equals(Promemoria.model().ID_VERSAMENTO.TASSONOMIA)){
			if(appendTablePrefix){
				return this.toAliasTable(field)+".tassonomia";
			}else{
				return "tassonomia";
			}
		}
		if(field.equals(Promemoria.model().ID_VERSAMENTO.TIPO)){
			if(appendTablePrefix){
				return this.toAliasTable(field)+".tipo";
			}else{
				return "tipo";
			}
		}
		if(field.equals(Promemoria.model().ID_RPT.COD_MSG_RICHIESTA)){
			if(appendTablePrefix){
				return this.toAliasTable(field)+".cod_msg_richiesta";
			}else{
				return "cod_msg_richiesta";
			}
		}
		if(field.equals(Promemoria.model().ID_RPT.COD_DOMINIO)){
			if(appendTablePrefix){
				return this.toAliasTable(field)+".cod_dominio";
			}else{
				return "cod_dominio";
			}
		}
		if(field.equals(Promemoria.model().ID_RPT.IUV)){
			if(appendTablePrefix){
				return this.toAliasTable(field)+".iuv";
			}else{
				return "iuv";
			}
		}
		if(field.equals(Promemoria.model().TIPO)){
			if(appendTablePrefix){
				return this.toAliasTable(field)+".tipo";
			}else{
				return "tipo";
			}
		}
		if(field.equals(Promemoria.model().DATA_CREAZIONE)){
			if(appendTablePrefix){
				return this.toAliasTable(field)+".data_creazione";
			}else{
				return "data_creazione";
			}
		}
		if(field.equals(Promemoria.model().STATO)){
			if(appendTablePrefix){
				return this.toAliasTable(field)+".stato";
			}else{
				return "stato";
			}
		}
		if(field.equals(Promemoria.model().DESCRIZIONE_STATO)){
			if(appendTablePrefix){
				return this.toAliasTable(field)+".descrizione_stato";
			}else{
				return "descrizione_stato";
			}
		}
		if(field.equals(Promemoria.model().DESTINATARIO_TO)){
			if(appendTablePrefix){
				return this.toAliasTable(field)+".destinatario_to";
			}else{
				return "destinatario_to";
			}
		}
		if(field.equals(Promemoria.model().DESTINATARIO_CC)){
			if(appendTablePrefix){
				return this.toAliasTable(field)+".destinatario_cc";
			}else{
				return "destinatario_cc";
			}
		}
		if(field.equals(Promemoria.model().MESSAGGIO_CONTENT_TYPE)){
			if(appendTablePrefix){
				return this.toAliasTable(field)+".messaggio_content_type";
			}else{
				return "messaggio_content_type";
			}
		}
		if(field.equals(Promemoria.model().OGGETTO)){
			if(appendTablePrefix){
				return this.toAliasTable(field)+".oggetto";
			}else{
				return "oggetto";
			}
		}
		if(field.equals(Promemoria.model().MESSAGGIO)){
			if(appendTablePrefix){
				return this.toAliasTable(field)+".messaggio";
			}else{
				return "messaggio";
			}
		}
		if(field.equals(Promemoria.model().ALLEGA_PDF)){
			if(appendTablePrefix){
				return this.toAliasTable(field)+".allega_pdf";
			}else{
				return "allega_pdf";
			}
		}
		if(field.equals(Promemoria.model().DATA_AGGIORNAMENTO_STATO)){
			if(appendTablePrefix){
				return this.toAliasTable(field)+".data_aggiornamento_stato";
			}else{
				return "data_aggiornamento_stato";
			}
		}
		if(field.equals(Promemoria.model().DATA_PROSSIMA_SPEDIZIONE)){
			if(appendTablePrefix){
				return this.toAliasTable(field)+".data_prossima_spedizione";
			}else{
				return "data_prossima_spedizione";
			}
		}
		if(field.equals(Promemoria.model().TENTATIVI_SPEDIZIONE)){
			if(appendTablePrefix){
				return this.toAliasTable(field)+".tentativi_spedizione";
			}else{
				return "tentativi_spedizione";
			}
		}
		if(field.equals(Promemoria.model().ID_DOCUMENTO.COD_DOCUMENTO)){
			if(appendTablePrefix){
				return this.toAliasTable(field)+".cod_documento";
			}else{
				return "cod_documento";
			}
		}
		if(field.equals(Promemoria.model().ID_DOCUMENTO.ID_APPLICAZIONE.COD_APPLICAZIONE)){
			if(appendTablePrefix){
				return this.toAliasTable(field)+".cod_applicazione";
			}else{
				return "cod_applicazione";
			}
		}


		return super.toColumn(field,returnAlias,appendTablePrefix);
		
	}
	
	@Override
	public String toTable(IField field,boolean returnAlias) throws ExpressionException {
		
		// In the case of table with alias, using parameter returnAlias​​, 
		// it is possible to drive the choice whether to return only the alias or 
		// the full definition of the table containing the alias
		
		if(field.equals(Promemoria.model().ID_VERSAMENTO.COD_VERSAMENTO_ENTE)){
			return this.toTable(Promemoria.model().ID_VERSAMENTO, returnAlias);
		}
		if(field.equals(Promemoria.model().ID_VERSAMENTO.ID_APPLICAZIONE.COD_APPLICAZIONE)){
			return this.toTable(Promemoria.model().ID_VERSAMENTO.ID_APPLICAZIONE, returnAlias);
		}
		if(field.equals(Promemoria.model().ID_VERSAMENTO.SRC_DEBITORE_IDENTIFICATIVO)){
			return this.toTable(Promemoria.model().ID_VERSAMENTO, returnAlias);
		}
		if(field.equals(Promemoria.model().ID_VERSAMENTO.DEBITORE_ANAGRAFICA)){
			return this.toTable(Promemoria.model().ID_VERSAMENTO, returnAlias);
		}
		if(field.equals(Promemoria.model().ID_VERSAMENTO.COD_VERSAMENTO_LOTTO)){
			return this.toTable(Promemoria.model().ID_VERSAMENTO, returnAlias);
		}
		if(field.equals(Promemoria.model().ID_VERSAMENTO.COD_ANNO_TRIBUTARIO)){
			return this.toTable(Promemoria.model().ID_VERSAMENTO, returnAlias);
		}
		if(field.equals(Promemoria.model().ID_VERSAMENTO.IMPORTO_TOTALE)){
			return this.toTable(Promemoria.model().ID_VERSAMENTO, returnAlias);
		}
		if(field.equals(Promemoria.model().ID_VERSAMENTO.CAUSALE_VERSAMENTO)){
			return this.toTable(Promemoria.model().ID_VERSAMENTO, returnAlias);
		}
		if(field.equals(Promemoria.model().ID_VERSAMENTO.STATO_VERSAMENTO)){
			return this.toTable(Promemoria.model().ID_VERSAMENTO, returnAlias);
		}
		if(field.equals(Promemoria.model().ID_VERSAMENTO.ID_UO.COD_UO)){
			return this.toTable(Promemoria.model().ID_VERSAMENTO.ID_UO, returnAlias);
		}
		if(field.equals(Promemoria.model().ID_VERSAMENTO.ID_UO.ID_DOMINIO.COD_DOMINIO)){
			return this.toTable(Promemoria.model().ID_VERSAMENTO.ID_UO.ID_DOMINIO, returnAlias);
		}
		if(field.equals(Promemoria.model().ID_VERSAMENTO.ID_TIPO_VERSAMENTO.COD_TIPO_VERSAMENTO)){
			return this.toTable(Promemoria.model().ID_VERSAMENTO.ID_TIPO_VERSAMENTO, returnAlias);
		}
		if(field.equals(Promemoria.model().ID_VERSAMENTO.DIVISIONE)){
			return this.toTable(Promemoria.model().ID_VERSAMENTO, returnAlias);
		}
		if(field.equals(Promemoria.model().ID_VERSAMENTO.DIREZIONE)){
			return this.toTable(Promemoria.model().ID_VERSAMENTO, returnAlias);
		}
		if(field.equals(Promemoria.model().ID_VERSAMENTO.TASSONOMIA)){
			return this.toTable(Promemoria.model().ID_VERSAMENTO, returnAlias);
		}
		if(field.equals(Promemoria.model().ID_VERSAMENTO.TIPO)){
			return this.toTable(Promemoria.model().ID_VERSAMENTO, returnAlias);
		}
		if(field.equals(Promemoria.model().ID_RPT.COD_MSG_RICHIESTA)){
			return this.toTable(Promemoria.model().ID_RPT, returnAlias);
		}
		if(field.equals(Promemoria.model().ID_RPT.COD_DOMINIO)){
			return this.toTable(Promemoria.model().ID_RPT, returnAlias);
		}
		if(field.equals(Promemoria.model().ID_RPT.IUV)){
			return this.toTable(Promemoria.model().ID_RPT, returnAlias);
		}
		if(field.equals(Promemoria.model().TIPO)){
			return this.toTable(Promemoria.model(), returnAlias);
		}
		if(field.equals(Promemoria.model().DATA_CREAZIONE)){
			return this.toTable(Promemoria.model(), returnAlias);
		}
		if(field.equals(Promemoria.model().STATO)){
			return this.toTable(Promemoria.model(), returnAlias);
		}
		if(field.equals(Promemoria.model().DESCRIZIONE_STATO)){
			return this.toTable(Promemoria.model(), returnAlias);
		}
		if(field.equals(Promemoria.model().DESTINATARIO_TO)){
			return this.toTable(Promemoria.model(), returnAlias);
		}
		if(field.equals(Promemoria.model().DESTINATARIO_CC)){
			return this.toTable(Promemoria.model(), returnAlias);
		}
		if(field.equals(Promemoria.model().MESSAGGIO_CONTENT_TYPE)){
			return this.toTable(Promemoria.model(), returnAlias);
		}
		if(field.equals(Promemoria.model().OGGETTO)){
			return this.toTable(Promemoria.model(), returnAlias);
		}
		if(field.equals(Promemoria.model().MESSAGGIO)){
			return this.toTable(Promemoria.model(), returnAlias);
		}
		if(field.equals(Promemoria.model().ALLEGA_PDF)){
			return this.toTable(Promemoria.model(), returnAlias);
		}
		if(field.equals(Promemoria.model().DATA_AGGIORNAMENTO_STATO)){
			return this.toTable(Promemoria.model(), returnAlias);
		}
		if(field.equals(Promemoria.model().DATA_PROSSIMA_SPEDIZIONE)){
			return this.toTable(Promemoria.model(), returnAlias);
		}
		if(field.equals(Promemoria.model().TENTATIVI_SPEDIZIONE)){
			return this.toTable(Promemoria.model(), returnAlias);
		}
		if(field.equals(Promemoria.model().ID_DOCUMENTO.COD_DOCUMENTO)){
			return this.toTable(Promemoria.model().ID_DOCUMENTO, returnAlias);
		}
		if(field.equals(Promemoria.model().ID_DOCUMENTO.ID_APPLICAZIONE.COD_APPLICAZIONE)){
			return this.toTable(Promemoria.model().ID_DOCUMENTO.ID_APPLICAZIONE, returnAlias);
		}


		return super.toTable(field,returnAlias);
		
	}

	@Override
	public String toTable(IModel<?> model,boolean returnAlias) throws ExpressionException {
		
		// In the case of table with alias, using parameter returnAlias​​, 
		// it is possible to drive the choice whether to return only the alias or 
		// the full definition of the table containing the alias
		
		if(model.equals(Promemoria.model())){
			return "promemoria";
		}
		if(model.equals(Promemoria.model().ID_VERSAMENTO)){
			return "versamenti";
		}
		if(model.equals(Promemoria.model().ID_VERSAMENTO.ID_APPLICAZIONE)){
			return "applicazioni";
		}
		if(model.equals(Promemoria.model().ID_VERSAMENTO.ID_UO)){
			return "uo";
		}
		if(model.equals(Promemoria.model().ID_VERSAMENTO.ID_UO.ID_DOMINIO)){
			return "domini";
		}
		if(model.equals(Promemoria.model().ID_VERSAMENTO.ID_TIPO_VERSAMENTO)){
			return "tipi_versamento";
		}
		if(model.equals(Promemoria.model().ID_RPT)){
			return "rpt";
		}
		if(model.equals(Promemoria.model().ID_DOCUMENTO)){
			return "documenti";
		}
		if(model.equals(Promemoria.model().ID_DOCUMENTO.ID_APPLICAZIONE)){
			return "applicazioni";
		}


		return super.toTable(model,returnAlias);
		
	}

}
