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

import it.govpay.orm.NotificaAppIO;


/**     
 * NotificaAppIOFieldConverter
 *
 * @author Giovanni Bussu (bussu@link.it)
 * @author Lorenzo Nardi (nardi@link.it)
 * @author $Author$
 * @version $Rev$, $Date$
 */
public class NotificaAppIOFieldConverter extends AbstractSQLFieldConverter {

	private TipiDatabase databaseType;
	
	public NotificaAppIOFieldConverter(String databaseType){
		this.databaseType = TipiDatabase.toEnumConstant(databaseType);
	}
	public NotificaAppIOFieldConverter(TipiDatabase databaseType){
		this.databaseType = databaseType;
	}


	@Override
	public IModel<?> getRootModel() throws ExpressionException {
		return NotificaAppIO.model();
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
		
		if(field.equals(NotificaAppIO.model().ID_VERSAMENTO.COD_VERSAMENTO_ENTE)){
			if(appendTablePrefix){
				return this.toAliasTable(field)+".cod_versamento_ente";
			}else{
				return "cod_versamento_ente";
			}
		}
		if(field.equals(NotificaAppIO.model().ID_VERSAMENTO.ID_APPLICAZIONE.COD_APPLICAZIONE)){
			if(appendTablePrefix){
				return this.toAliasTable(field)+".cod_applicazione";
			}else{
				return "cod_applicazione";
			}
		}
		if(field.equals(NotificaAppIO.model().ID_VERSAMENTO.SRC_DEBITORE_IDENTIFICATIVO)){
			if(appendTablePrefix){
				return this.toAliasTable(field)+".src_debitore_identificativo";
			}else{
				return "src_debitore_identificativo";
			}
		}
		if(field.equals(NotificaAppIO.model().ID_VERSAMENTO.DEBITORE_ANAGRAFICA)){
			if(appendTablePrefix){
				return this.toAliasTable(field)+".debitore_anagrafica";
			}else{
				return "debitore_anagrafica";
			}
		}
		if(field.equals(NotificaAppIO.model().ID_VERSAMENTO.COD_VERSAMENTO_LOTTO)){
			if(appendTablePrefix){
				return this.toAliasTable(field)+".cod_versamento_lotto";
			}else{
				return "cod_versamento_lotto";
			}
		}
		if(field.equals(NotificaAppIO.model().ID_VERSAMENTO.COD_ANNO_TRIBUTARIO)){
			if(appendTablePrefix){
				return this.toAliasTable(field)+".cod_anno_tributario";
			}else{
				return "cod_anno_tributario";
			}
		}
		if(field.equals(NotificaAppIO.model().ID_VERSAMENTO.IMPORTO_TOTALE)){
			if(appendTablePrefix){
				return this.toAliasTable(field)+".importo_totale";
			}else{
				return "importo_totale";
			}
		}
		if(field.equals(NotificaAppIO.model().ID_VERSAMENTO.CAUSALE_VERSAMENTO)){
			if(appendTablePrefix){
				return this.toAliasTable(field)+".causale_versamento";
			}else{
				return "causale_versamento";
			}
		}
		if(field.equals(NotificaAppIO.model().ID_VERSAMENTO.STATO_VERSAMENTO)){
			if(appendTablePrefix){
				return this.toAliasTable(field)+".stato_versamento";
			}else{
				return "stato_versamento";
			}
		}
		if(field.equals(NotificaAppIO.model().ID_VERSAMENTO.ID_UO.COD_UO)){
			if(appendTablePrefix){
				return this.toAliasTable(field)+".cod_uo";
			}else{
				return "cod_uo";
			}
		}
		if(field.equals(NotificaAppIO.model().ID_VERSAMENTO.ID_UO.ID_DOMINIO.COD_DOMINIO)){
			if(appendTablePrefix){
				return this.toAliasTable(field)+".cod_dominio";
			}else{
				return "cod_dominio";
			}
		}
		if(field.equals(NotificaAppIO.model().ID_VERSAMENTO.ID_TIPO_VERSAMENTO.COD_TIPO_VERSAMENTO)){
			if(appendTablePrefix){
				return this.toAliasTable(field)+".cod_tipo_versamento";
			}else{
				return "cod_tipo_versamento";
			}
		}
		if(field.equals(NotificaAppIO.model().ID_VERSAMENTO.DIVISIONE)){
			if(appendTablePrefix){
				return this.toAliasTable(field)+".divisione";
			}else{
				return "divisione";
			}
		}
		if(field.equals(NotificaAppIO.model().ID_VERSAMENTO.DIREZIONE)){
			if(appendTablePrefix){
				return this.toAliasTable(field)+".direzione";
			}else{
				return "direzione";
			}
		}
		if(field.equals(NotificaAppIO.model().ID_VERSAMENTO.TASSONOMIA)){
			if(appendTablePrefix){
				return this.toAliasTable(field)+".tassonomia";
			}else{
				return "tassonomia";
			}
		}
		if(field.equals(NotificaAppIO.model().ID_VERSAMENTO.TIPO)){
			if(appendTablePrefix){
				return this.toAliasTable(field)+".tipo";
			}else{
				return "tipo";
			}
		}
		if(field.equals(NotificaAppIO.model().ID_TIPO_VERSAMENTO_DOMINIO.ID_DOMINIO.COD_DOMINIO)){
			if(appendTablePrefix){
				return this.toAliasTable(field)+".cod_dominio";
			}else{
				return "cod_dominio";
			}
		}
		if(field.equals(NotificaAppIO.model().ID_TIPO_VERSAMENTO_DOMINIO.ID_TIPO_VERSAMENTO.COD_TIPO_VERSAMENTO)){
			if(appendTablePrefix){
				return this.toAliasTable(field)+".cod_tipo_versamento";
			}else{
				return "cod_tipo_versamento";
			}
		}
		if(field.equals(NotificaAppIO.model().DEBITORE_IDENTIFICATIVO)){
			if(appendTablePrefix){
				return this.toAliasTable(field)+".debitore_identificativo";
			}else{
				return "debitore_identificativo";
			}
		}
		if(field.equals(NotificaAppIO.model().COD_VERSAMENTO_ENTE)){
			if(appendTablePrefix){
				return this.toAliasTable(field)+".cod_versamento_ente";
			}else{
				return "cod_versamento_ente";
			}
		}
		if(field.equals(NotificaAppIO.model().COD_APPLICAZIONE)){
			if(appendTablePrefix){
				return this.toAliasTable(field)+".cod_applicazione";
			}else{
				return "cod_applicazione";
			}
		}
		if(field.equals(NotificaAppIO.model().COD_DOMINIO)){
			if(appendTablePrefix){
				return this.toAliasTable(field)+".cod_dominio";
			}else{
				return "cod_dominio";
			}
		}
		if(field.equals(NotificaAppIO.model().IUV)){
			if(appendTablePrefix){
				return this.toAliasTable(field)+".iuv";
			}else{
				return "iuv";
			}
		}
		if(field.equals(NotificaAppIO.model().TIPO_ESITO)){
			if(appendTablePrefix){
				return this.toAliasTable(field)+".tipo_esito";
			}else{
				return "tipo_esito";
			}
		}
		if(field.equals(NotificaAppIO.model().DATA_CREAZIONE)){
			if(appendTablePrefix){
				return this.toAliasTable(field)+".data_creazione";
			}else{
				return "data_creazione";
			}
		}
		if(field.equals(NotificaAppIO.model().STATO)){
			if(appendTablePrefix){
				return this.toAliasTable(field)+".stato";
			}else{
				return "stato";
			}
		}
		if(field.equals(NotificaAppIO.model().DESCRIZIONE_STATO)){
			if(appendTablePrefix){
				return this.toAliasTable(field)+".descrizione_stato";
			}else{
				return "descrizione_stato";
			}
		}
		if(field.equals(NotificaAppIO.model().DATA_AGGIORNAMENTO_STATO)){
			if(appendTablePrefix){
				return this.toAliasTable(field)+".data_aggiornamento_stato";
			}else{
				return "data_aggiornamento_stato";
			}
		}
		if(field.equals(NotificaAppIO.model().DATA_PROSSIMA_SPEDIZIONE)){
			if(appendTablePrefix){
				return this.toAliasTable(field)+".data_prossima_spedizione";
			}else{
				return "data_prossima_spedizione";
			}
		}
		if(field.equals(NotificaAppIO.model().TENTATIVI_SPEDIZIONE)){
			if(appendTablePrefix){
				return this.toAliasTable(field)+".tentativi_spedizione";
			}else{
				return "tentativi_spedizione";
			}
		}
		if(field.equals(NotificaAppIO.model().ID_MESSAGGIO)){
			if(appendTablePrefix){
				return this.toAliasTable(field)+".id_messaggio";
			}else{
				return "id_messaggio";
			}
		}
		if(field.equals(NotificaAppIO.model().STATO_MESSAGGIO)){
			if(appendTablePrefix){
				return this.toAliasTable(field)+".stato_messaggio";
			}else{
				return "stato_messaggio";
			}
		}


		return super.toColumn(field,returnAlias,appendTablePrefix);
		
	}
	
	@Override
	public String toTable(IField field,boolean returnAlias) throws ExpressionException {
		
		// In the case of table with alias, using parameter returnAlias​​, 
		// it is possible to drive the choice whether to return only the alias or 
		// the full definition of the table containing the alias
		
		if(field.equals(NotificaAppIO.model().ID_VERSAMENTO.COD_VERSAMENTO_ENTE)){
			return this.toTable(NotificaAppIO.model().ID_VERSAMENTO, returnAlias);
		}
		if(field.equals(NotificaAppIO.model().ID_VERSAMENTO.ID_APPLICAZIONE.COD_APPLICAZIONE)){
			return this.toTable(NotificaAppIO.model().ID_VERSAMENTO.ID_APPLICAZIONE, returnAlias);
		}
		if(field.equals(NotificaAppIO.model().ID_VERSAMENTO.SRC_DEBITORE_IDENTIFICATIVO)){
			return this.toTable(NotificaAppIO.model().ID_VERSAMENTO, returnAlias);
		}
		if(field.equals(NotificaAppIO.model().ID_VERSAMENTO.DEBITORE_ANAGRAFICA)){
			return this.toTable(NotificaAppIO.model().ID_VERSAMENTO, returnAlias);
		}
		if(field.equals(NotificaAppIO.model().ID_VERSAMENTO.COD_VERSAMENTO_LOTTO)){
			return this.toTable(NotificaAppIO.model().ID_VERSAMENTO, returnAlias);
		}
		if(field.equals(NotificaAppIO.model().ID_VERSAMENTO.COD_ANNO_TRIBUTARIO)){
			return this.toTable(NotificaAppIO.model().ID_VERSAMENTO, returnAlias);
		}
		if(field.equals(NotificaAppIO.model().ID_VERSAMENTO.IMPORTO_TOTALE)){
			return this.toTable(NotificaAppIO.model().ID_VERSAMENTO, returnAlias);
		}
		if(field.equals(NotificaAppIO.model().ID_VERSAMENTO.CAUSALE_VERSAMENTO)){
			return this.toTable(NotificaAppIO.model().ID_VERSAMENTO, returnAlias);
		}
		if(field.equals(NotificaAppIO.model().ID_VERSAMENTO.STATO_VERSAMENTO)){
			return this.toTable(NotificaAppIO.model().ID_VERSAMENTO, returnAlias);
		}
		if(field.equals(NotificaAppIO.model().ID_VERSAMENTO.ID_UO.COD_UO)){
			return this.toTable(NotificaAppIO.model().ID_VERSAMENTO.ID_UO, returnAlias);
		}
		if(field.equals(NotificaAppIO.model().ID_VERSAMENTO.ID_UO.ID_DOMINIO.COD_DOMINIO)){
			return this.toTable(NotificaAppIO.model().ID_VERSAMENTO.ID_UO.ID_DOMINIO, returnAlias);
		}
		if(field.equals(NotificaAppIO.model().ID_VERSAMENTO.ID_TIPO_VERSAMENTO.COD_TIPO_VERSAMENTO)){
			return this.toTable(NotificaAppIO.model().ID_VERSAMENTO.ID_TIPO_VERSAMENTO, returnAlias);
		}
		if(field.equals(NotificaAppIO.model().ID_VERSAMENTO.DIVISIONE)){
			return this.toTable(NotificaAppIO.model().ID_VERSAMENTO, returnAlias);
		}
		if(field.equals(NotificaAppIO.model().ID_VERSAMENTO.DIREZIONE)){
			return this.toTable(NotificaAppIO.model().ID_VERSAMENTO, returnAlias);
		}
		if(field.equals(NotificaAppIO.model().ID_VERSAMENTO.TASSONOMIA)){
			return this.toTable(NotificaAppIO.model().ID_VERSAMENTO, returnAlias);
		}
		if(field.equals(NotificaAppIO.model().ID_VERSAMENTO.TIPO)){
			return this.toTable(NotificaAppIO.model().ID_VERSAMENTO, returnAlias);
		}
		if(field.equals(NotificaAppIO.model().ID_TIPO_VERSAMENTO_DOMINIO.ID_DOMINIO.COD_DOMINIO)){
			return this.toTable(NotificaAppIO.model().ID_TIPO_VERSAMENTO_DOMINIO.ID_DOMINIO, returnAlias);
		}
		if(field.equals(NotificaAppIO.model().ID_TIPO_VERSAMENTO_DOMINIO.ID_TIPO_VERSAMENTO.COD_TIPO_VERSAMENTO)){
			return this.toTable(NotificaAppIO.model().ID_TIPO_VERSAMENTO_DOMINIO.ID_TIPO_VERSAMENTO, returnAlias);
		}
		if(field.equals(NotificaAppIO.model().DEBITORE_IDENTIFICATIVO)){
			return this.toTable(NotificaAppIO.model(), returnAlias);
		}
		if(field.equals(NotificaAppIO.model().COD_VERSAMENTO_ENTE)){
			return this.toTable(NotificaAppIO.model(), returnAlias);
		}
		if(field.equals(NotificaAppIO.model().COD_APPLICAZIONE)){
			return this.toTable(NotificaAppIO.model(), returnAlias);
		}
		if(field.equals(NotificaAppIO.model().COD_DOMINIO)){
			return this.toTable(NotificaAppIO.model(), returnAlias);
		}
		if(field.equals(NotificaAppIO.model().IUV)){
			return this.toTable(NotificaAppIO.model(), returnAlias);
		}
		if(field.equals(NotificaAppIO.model().TIPO_ESITO)){
			return this.toTable(NotificaAppIO.model(), returnAlias);
		}
		if(field.equals(NotificaAppIO.model().DATA_CREAZIONE)){
			return this.toTable(NotificaAppIO.model(), returnAlias);
		}
		if(field.equals(NotificaAppIO.model().STATO)){
			return this.toTable(NotificaAppIO.model(), returnAlias);
		}
		if(field.equals(NotificaAppIO.model().DESCRIZIONE_STATO)){
			return this.toTable(NotificaAppIO.model(), returnAlias);
		}
		if(field.equals(NotificaAppIO.model().DATA_AGGIORNAMENTO_STATO)){
			return this.toTable(NotificaAppIO.model(), returnAlias);
		}
		if(field.equals(NotificaAppIO.model().DATA_PROSSIMA_SPEDIZIONE)){
			return this.toTable(NotificaAppIO.model(), returnAlias);
		}
		if(field.equals(NotificaAppIO.model().TENTATIVI_SPEDIZIONE)){
			return this.toTable(NotificaAppIO.model(), returnAlias);
		}
		if(field.equals(NotificaAppIO.model().ID_MESSAGGIO)){
			return this.toTable(NotificaAppIO.model(), returnAlias);
		}
		if(field.equals(NotificaAppIO.model().STATO_MESSAGGIO)){
			return this.toTable(NotificaAppIO.model(), returnAlias);
		}


		return super.toTable(field,returnAlias);
		
	}

	@Override
	public String toTable(IModel<?> model,boolean returnAlias) throws ExpressionException {
		
		// In the case of table with alias, using parameter returnAlias​​, 
		// it is possible to drive the choice whether to return only the alias or 
		// the full definition of the table containing the alias
		
		if(model.equals(NotificaAppIO.model())){
			return "notifiche_app_io";
		}
		if(model.equals(NotificaAppIO.model().ID_VERSAMENTO)){
			return "versamenti";
		}
		if(model.equals(NotificaAppIO.model().ID_VERSAMENTO.ID_APPLICAZIONE)){
			return "id_applicazione";
		}
		if(model.equals(NotificaAppIO.model().ID_VERSAMENTO.ID_UO)){
			return "id_uo";
		}
		if(model.equals(NotificaAppIO.model().ID_VERSAMENTO.ID_UO.ID_DOMINIO)){
			return "id_dominio";
		}
		if(model.equals(NotificaAppIO.model().ID_VERSAMENTO.ID_TIPO_VERSAMENTO)){
			return "id_tipo_versamento";
		}
		if(model.equals(NotificaAppIO.model().ID_TIPO_VERSAMENTO_DOMINIO)){
			return "tipi_vers_domini";
		}
		if(model.equals(NotificaAppIO.model().ID_TIPO_VERSAMENTO_DOMINIO.ID_DOMINIO)){
			return "id_dominio";
		}
		if(model.equals(NotificaAppIO.model().ID_TIPO_VERSAMENTO_DOMINIO.ID_TIPO_VERSAMENTO)){
			return "id_tipo_versamento";
		}


		return super.toTable(model,returnAlias);
		
	}

}
