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

import it.govpay.orm.VersamentoIncasso;


/**     
 * VersamentoIncassoFieldConverter
 *
 * @author Giovanni Bussu (bussu@link.it)
 * @author Lorenzo Nardi (nardi@link.it)
 * @author $Author$
 * @version $Rev$, $Date$
 */
public class VersamentoIncassoFieldConverter extends AbstractSQLFieldConverter {

	private TipiDatabase databaseType;
	
	public VersamentoIncassoFieldConverter(String databaseType){
		this.databaseType = TipiDatabase.toEnumConstant(databaseType);
	}
	public VersamentoIncassoFieldConverter(TipiDatabase databaseType){
		this.databaseType = databaseType;
	}


	@Override
	public IModel<?> getRootModel() throws ExpressionException {
		return VersamentoIncasso.model();
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
		
		if(field.equals(VersamentoIncasso.model().COD_VERSAMENTO_ENTE)){
			if(appendTablePrefix){
				return this.toAliasTable(field)+".cod_versamento_ente";
			}else{
				return "cod_versamento_ente";
			}
		}
		if(field.equals(VersamentoIncasso.model().NOME)){
			if(appendTablePrefix){
				return this.toAliasTable(field)+".nome";
			}else{
				return "nome";
			}
		}
		if(field.equals(VersamentoIncasso.model().ID_TIPO_VERSAMENTO_DOMINIO.ID_DOMINIO.COD_DOMINIO)){
			if(appendTablePrefix){
				return this.toAliasTable(field)+".cod_dominio";
			}else{
				return "cod_dominio";
			}
		}
		if(field.equals(VersamentoIncasso.model().ID_TIPO_VERSAMENTO_DOMINIO.ID_TIPO_VERSAMENTO.COD_TIPO_VERSAMENTO)){
			if(appendTablePrefix){
				return this.toAliasTable(field)+".cod_tipo_versamento";
			}else{
				return "cod_tipo_versamento";
			}
		}
		if(field.equals(VersamentoIncasso.model().ID_TIPO_VERSAMENTO_DOMINIO.ID_TIPO_VERSAMENTO.TIPO)){
			if(appendTablePrefix){
				return this.toAliasTable(field)+".tipo";
			}else{
				return "tipo";
			}
		}
		if(field.equals(VersamentoIncasso.model().ID_TIPO_VERSAMENTO.COD_TIPO_VERSAMENTO)){
			if(appendTablePrefix){
				return this.toAliasTable(field)+".cod_tipo_versamento";
			}else{
				return "cod_tipo_versamento";
			}
		}
		if(field.equals(VersamentoIncasso.model().ID_TIPO_VERSAMENTO.TIPO)){
			if(appendTablePrefix){
				return this.toAliasTable(field)+".tipo";
			}else{
				return "tipo";
			}
		}
		if(field.equals(VersamentoIncasso.model().ID_DOMINIO.COD_DOMINIO)){
			if(appendTablePrefix){
				return this.toAliasTable(field)+".cod_dominio";
			}else{
				return "cod_dominio";
			}
		}
		if(field.equals(VersamentoIncasso.model().ID_UO.COD_UO)){
			if(appendTablePrefix){
				return this.toAliasTable(field)+".cod_uo";
			}else{
				return "cod_uo";
			}
		}
		if(field.equals(VersamentoIncasso.model().ID_UO.ID_DOMINIO.COD_DOMINIO)){
			if(appendTablePrefix){
				return this.toAliasTable(field)+".cod_dominio";
			}else{
				return "cod_dominio";
			}
		}
		if(field.equals(VersamentoIncasso.model().ID_APPLICAZIONE.COD_APPLICAZIONE)){
			if(appendTablePrefix){
				return this.toAliasTable(field)+".cod_applicazione";
			}else{
				return "cod_applicazione";
			}
		}
		if(field.equals(VersamentoIncasso.model().ID_PAGAMENTO_PORTALE.ID_SESSIONE)){
			if(appendTablePrefix){
				return this.toAliasTable(field)+".id_sessione";
			}else{
				return "id_sessione";
			}
		}
		if(field.equals(VersamentoIncasso.model().ID_PAGAMENTO_PORTALE.ID_APPLICAZIONE.COD_APPLICAZIONE)){
			if(appendTablePrefix){
				return this.toAliasTable(field)+".cod_applicazione";
			}else{
				return "cod_applicazione";
			}
		}
		if(field.equals(VersamentoIncasso.model().ID_PAGAMENTO_PORTALE.SRC_VERSANTE_IDENTIFICATIVO)){
			if(appendTablePrefix){
				return this.toAliasTable(field)+".src_versante_identificativo";
			}else{
				return "src_versante_identificativo";
			}
		}
		if(field.equals(VersamentoIncasso.model().IUV.IUV)){
			if(appendTablePrefix){
				return this.toAliasTable(field)+".iuv";
			}else{
				return "iuv";
			}
		}
		if(field.equals(VersamentoIncasso.model().IMPORTO_TOTALE)){
			if(appendTablePrefix){
				return this.toAliasTable(field)+".importo_totale";
			}else{
				return "importo_totale";
			}
		}
		if(field.equals(VersamentoIncasso.model().STATO_VERSAMENTO)){
			if(appendTablePrefix){
				return this.toAliasTable(field)+".stato_versamento";
			}else{
				return "stato_versamento";
			}
		}
		if(field.equals(VersamentoIncasso.model().DESCRIZIONE_STATO)){
			if(appendTablePrefix){
				return this.toAliasTable(field)+".descrizione_stato";
			}else{
				return "descrizione_stato";
			}
		}
		if(field.equals(VersamentoIncasso.model().AGGIORNABILE)){
			if(appendTablePrefix){
				return this.toAliasTable(field)+".aggiornabile";
			}else{
				return "aggiornabile";
			}
		}
		if(field.equals(VersamentoIncasso.model().DATA_CREAZIONE)){
			if(appendTablePrefix){
				return this.toAliasTable(field)+".data_creazione";
			}else{
				return "data_creazione";
			}
		}
		if(field.equals(VersamentoIncasso.model().DATA_VALIDITA)){
			if(appendTablePrefix){
				return this.toAliasTable(field)+".data_validita";
			}else{
				return "data_validita";
			}
		}
		if(field.equals(VersamentoIncasso.model().DATA_SCADENZA)){
			if(appendTablePrefix){
				return this.toAliasTable(field)+".data_scadenza";
			}else{
				return "data_scadenza";
			}
		}
		if(field.equals(VersamentoIncasso.model().DATA_ORA_ULTIMO_AGGIORNAMENTO)){
			if(appendTablePrefix){
				return this.toAliasTable(field)+".data_ora_ultimo_aggiornamento";
			}else{
				return "data_ora_ultimo_aggiornamento";
			}
		}
		if(field.equals(VersamentoIncasso.model().CAUSALE_VERSAMENTO)){
			if(appendTablePrefix){
				return this.toAliasTable(field)+".causale_versamento";
			}else{
				return "causale_versamento";
			}
		}
		if(field.equals(VersamentoIncasso.model().DEBITORE_TIPO)){
			if(appendTablePrefix){
				return this.toAliasTable(field)+".debitore_tipo";
			}else{
				return "debitore_tipo";
			}
		}
		if(field.equals(VersamentoIncasso.model().DEBITORE_IDENTIFICATIVO)){
			if(appendTablePrefix){
				return this.toAliasTable(field)+".debitore_identificativo";
			}else{
				return "debitore_identificativo";
			}
		}
		if(field.equals(VersamentoIncasso.model().DEBITORE_ANAGRAFICA)){
			if(appendTablePrefix){
				return this.toAliasTable(field)+".debitore_anagrafica";
			}else{
				return "debitore_anagrafica";
			}
		}
		if(field.equals(VersamentoIncasso.model().DEBITORE_INDIRIZZO)){
			if(appendTablePrefix){
				return this.toAliasTable(field)+".debitore_indirizzo";
			}else{
				return "debitore_indirizzo";
			}
		}
		if(field.equals(VersamentoIncasso.model().DEBITORE_CIVICO)){
			if(appendTablePrefix){
				return this.toAliasTable(field)+".debitore_civico";
			}else{
				return "debitore_civico";
			}
		}
		if(field.equals(VersamentoIncasso.model().DEBITORE_CAP)){
			if(appendTablePrefix){
				return this.toAliasTable(field)+".debitore_cap";
			}else{
				return "debitore_cap";
			}
		}
		if(field.equals(VersamentoIncasso.model().DEBITORE_LOCALITA)){
			if(appendTablePrefix){
				return this.toAliasTable(field)+".debitore_localita";
			}else{
				return "debitore_localita";
			}
		}
		if(field.equals(VersamentoIncasso.model().DEBITORE_PROVINCIA)){
			if(appendTablePrefix){
				return this.toAliasTable(field)+".debitore_provincia";
			}else{
				return "debitore_provincia";
			}
		}
		if(field.equals(VersamentoIncasso.model().DEBITORE_NAZIONE)){
			if(appendTablePrefix){
				return this.toAliasTable(field)+".debitore_nazione";
			}else{
				return "debitore_nazione";
			}
		}
		if(field.equals(VersamentoIncasso.model().DEBITORE_EMAIL)){
			if(appendTablePrefix){
				return this.toAliasTable(field)+".debitore_email";
			}else{
				return "debitore_email";
			}
		}
		if(field.equals(VersamentoIncasso.model().DEBITORE_TELEFONO)){
			if(appendTablePrefix){
				return this.toAliasTable(field)+".debitore_telefono";
			}else{
				return "debitore_telefono";
			}
		}
		if(field.equals(VersamentoIncasso.model().DEBITORE_CELLULARE)){
			if(appendTablePrefix){
				return this.toAliasTable(field)+".debitore_cellulare";
			}else{
				return "debitore_cellulare";
			}
		}
		if(field.equals(VersamentoIncasso.model().DEBITORE_FAX)){
			if(appendTablePrefix){
				return this.toAliasTable(field)+".debitore_fax";
			}else{
				return "debitore_fax";
			}
		}
		if(field.equals(VersamentoIncasso.model().TASSONOMIA_AVVISO)){
			if(appendTablePrefix){
				return this.toAliasTable(field)+".tassonomia_avviso";
			}else{
				return "tassonomia_avviso";
			}
		}
		if(field.equals(VersamentoIncasso.model().TASSONOMIA)){
			if(appendTablePrefix){
				return this.toAliasTable(field)+".tassonomia";
			}else{
				return "tassonomia";
			}
		}
		if(field.equals(VersamentoIncasso.model().COD_LOTTO)){
			if(appendTablePrefix){
				return this.toAliasTable(field)+".cod_lotto";
			}else{
				return "cod_lotto";
			}
		}
		if(field.equals(VersamentoIncasso.model().COD_VERSAMENTO_LOTTO)){
			if(appendTablePrefix){
				return this.toAliasTable(field)+".cod_versamento_lotto";
			}else{
				return "cod_versamento_lotto";
			}
		}
		if(field.equals(VersamentoIncasso.model().COD_ANNO_TRIBUTARIO)){
			if(appendTablePrefix){
				return this.toAliasTable(field)+".cod_anno_tributario";
			}else{
				return "cod_anno_tributario";
			}
		}
		if(field.equals(VersamentoIncasso.model().COD_BUNDLEKEY)){
			if(appendTablePrefix){
				return this.toAliasTable(field)+".cod_bundlekey";
			}else{
				return "cod_bundlekey";
			}
		}
		if(field.equals(VersamentoIncasso.model().DATI_ALLEGATI)){
			if(appendTablePrefix){
				return this.toAliasTable(field)+".dati_allegati";
			}else{
				return "dati_allegati";
			}
		}
		if(field.equals(VersamentoIncasso.model().INCASSO)){
			if(appendTablePrefix){
				return this.toAliasTable(field)+".incasso";
			}else{
				return "incasso";
			}
		}
		if(field.equals(VersamentoIncasso.model().ANOMALIE)){
			if(appendTablePrefix){
				return this.toAliasTable(field)+".anomalie";
			}else{
				return "anomalie";
			}
		}
		if(field.equals(VersamentoIncasso.model().IUV_VERSAMENTO)){
			if(appendTablePrefix){
				return this.toAliasTable(field)+".iuv_versamento";
			}else{
				return "iuv_versamento";
			}
		}
		if(field.equals(VersamentoIncasso.model().NUMERO_AVVISO)){
			if(appendTablePrefix){
				return this.toAliasTable(field)+".numero_avviso";
			}else{
				return "numero_avviso";
			}
		}
		if(field.equals(VersamentoIncasso.model().AVVISATURA_ABILITATA)){
			if(appendTablePrefix){
				return this.toAliasTable(field)+".avvisatura_abilitata";
			}else{
				return "avvisatura_abilitata";
			}
		}
		if(field.equals(VersamentoIncasso.model().AVVISATURA_DA_INVIARE)){
			if(appendTablePrefix){
				return this.toAliasTable(field)+".avvisatura_da_inviare";
			}else{
				return "avvisatura_da_inviare";
			}
		}
		if(field.equals(VersamentoIncasso.model().AVVISATURA_OPERAZIONE)){
			if(appendTablePrefix){
				return this.toAliasTable(field)+".avvisatura_operazione";
			}else{
				return "avvisatura_operazione";
			}
		}
		if(field.equals(VersamentoIncasso.model().AVVISATURA_MODALITA)){
			if(appendTablePrefix){
				return this.toAliasTable(field)+".avvisatura_modalita";
			}else{
				return "avvisatura_modalita";
			}
		}
		if(field.equals(VersamentoIncasso.model().AVVISATURA_TIPO_PAGAMENTO)){
			if(appendTablePrefix){
				return this.toAliasTable(field)+".avvisatura_tipo_pagamento";
			}else{
				return "avvisatura_tipo_pagamento";
			}
		}
		if(field.equals(VersamentoIncasso.model().AVVISATURA_COD_AVVISATURA)){
			if(appendTablePrefix){
				return this.toAliasTable(field)+".avvisatura_cod_avvisatura";
			}else{
				return "avvisatura_cod_avvisatura";
			}
		}
		if(field.equals(VersamentoIncasso.model().ID_TRACCIATO_AVVISATURA.ID_TRACCIATO)){
			if(appendTablePrefix){
				return this.toAliasTable(field)+".id_tracciato";
			}else{
				return "id_tracciato";
			}
		}
		if(field.equals(VersamentoIncasso.model().ACK)){
			if(appendTablePrefix){
				return this.toAliasTable(field)+".ack";
			}else{
				return "ack";
			}
		}
		if(field.equals(VersamentoIncasso.model().ANOMALO)){
			if(appendTablePrefix){
				return this.toAliasTable(field)+".anomalo";
			}else{
				return "anomalo";
			}
		}
		if(field.equals(VersamentoIncasso.model().DATA_PAGAMENTO)){
			if(appendTablePrefix){
				return this.toAliasTable(field)+".data_pagamento";
			}else{
				return "data_pagamento";
			}
		}
		if(field.equals(VersamentoIncasso.model().IMPORTO_PAGATO)){
			if(appendTablePrefix){
				return this.toAliasTable(field)+".importo_pagato";
			}else{
				return "importo_pagato";
			}
		}
		if(field.equals(VersamentoIncasso.model().IMPORTO_INCASSATO)){
			if(appendTablePrefix){
				return this.toAliasTable(field)+".importo_incassato";
			}else{
				return "importo_incassato";
			}
		}
		if(field.equals(VersamentoIncasso.model().STATO_PAGAMENTO)){
			if(appendTablePrefix){
				return this.toAliasTable(field)+".stato_pagamento";
			}else{
				return "stato_pagamento";
			}
		}
		if(field.equals(VersamentoIncasso.model().IUV_PAGAMENTO)){
			if(appendTablePrefix){
				return this.toAliasTable(field)+".iuv_pagamento";
			}else{
				return "iuv_pagamento";
			}
		}
		if(field.equals(VersamentoIncasso.model().DIVISIONE)){
			if(appendTablePrefix){
				return this.toAliasTable(field)+".divisione";
			}else{
				return "divisione";
			}
		}
		if(field.equals(VersamentoIncasso.model().DIREZIONE)){
			if(appendTablePrefix){
				return this.toAliasTable(field)+".direzione";
			}else{
				return "direzione";
			}
		}
		if(field.equals(VersamentoIncasso.model().SMART_ORDER_DATE)){
			if(appendTablePrefix){
				return this.toAliasTable(field)+".smart_order_date";
			}else{
				return "smart_order_date";
			}
		}
		if(field.equals(VersamentoIncasso.model().SMART_ORDER_RANK)){
			if(appendTablePrefix){
				return this.toAliasTable(field)+".smart_order_rank";
			}else{
				return "smart_order_rank";
			}
		}
		if(field.equals(VersamentoIncasso.model().ID_SESSIONE)){
			if(appendTablePrefix){
				return this.toAliasTable(field)+".id_sessione";
			}else{
				return "id_sessione";
			}
		}
		if(field.equals(VersamentoIncasso.model().SRC_IUV)){
			if(appendTablePrefix){
				return this.toAliasTable(field)+".src_iuv";
			}else{
				return "src_iuv";
			}
		}
		if(field.equals(VersamentoIncasso.model().SRC_DEBITORE_IDENTIFICATIVO)){
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
		
		if(field.equals(VersamentoIncasso.model().COD_VERSAMENTO_ENTE)){
			return this.toTable(VersamentoIncasso.model(), returnAlias);
		}
		if(field.equals(VersamentoIncasso.model().NOME)){
			return this.toTable(VersamentoIncasso.model(), returnAlias);
		}
		if(field.equals(VersamentoIncasso.model().ID_TIPO_VERSAMENTO_DOMINIO.ID_DOMINIO.COD_DOMINIO)){
			return this.toTable(VersamentoIncasso.model().ID_TIPO_VERSAMENTO_DOMINIO.ID_DOMINIO, returnAlias);
		}
		if(field.equals(VersamentoIncasso.model().ID_TIPO_VERSAMENTO_DOMINIO.ID_TIPO_VERSAMENTO.COD_TIPO_VERSAMENTO)){
			return this.toTable(VersamentoIncasso.model().ID_TIPO_VERSAMENTO_DOMINIO.ID_TIPO_VERSAMENTO, returnAlias);
		}
		if(field.equals(VersamentoIncasso.model().ID_TIPO_VERSAMENTO_DOMINIO.ID_TIPO_VERSAMENTO.TIPO)){
			return this.toTable(VersamentoIncasso.model().ID_TIPO_VERSAMENTO_DOMINIO.ID_TIPO_VERSAMENTO, returnAlias);
		}
		if(field.equals(VersamentoIncasso.model().ID_TIPO_VERSAMENTO.COD_TIPO_VERSAMENTO)){
			return this.toTable(VersamentoIncasso.model().ID_TIPO_VERSAMENTO, returnAlias);
		}
		if(field.equals(VersamentoIncasso.model().ID_TIPO_VERSAMENTO.TIPO)){
			return this.toTable(VersamentoIncasso.model().ID_TIPO_VERSAMENTO, returnAlias);
		}
		if(field.equals(VersamentoIncasso.model().ID_DOMINIO.COD_DOMINIO)){
			return this.toTable(VersamentoIncasso.model().ID_DOMINIO, returnAlias);
		}
		if(field.equals(VersamentoIncasso.model().ID_UO.COD_UO)){
			return this.toTable(VersamentoIncasso.model().ID_UO, returnAlias);
		}
		if(field.equals(VersamentoIncasso.model().ID_UO.ID_DOMINIO.COD_DOMINIO)){
			return this.toTable(VersamentoIncasso.model().ID_UO.ID_DOMINIO, returnAlias);
		}
		if(field.equals(VersamentoIncasso.model().ID_APPLICAZIONE.COD_APPLICAZIONE)){
			return this.toTable(VersamentoIncasso.model().ID_APPLICAZIONE, returnAlias);
		}
		if(field.equals(VersamentoIncasso.model().ID_PAGAMENTO_PORTALE.ID_SESSIONE)){
			return this.toTable(VersamentoIncasso.model().ID_PAGAMENTO_PORTALE, returnAlias);
		}
		if(field.equals(VersamentoIncasso.model().ID_PAGAMENTO_PORTALE.ID_APPLICAZIONE.COD_APPLICAZIONE)){
			return this.toTable(VersamentoIncasso.model().ID_PAGAMENTO_PORTALE.ID_APPLICAZIONE, returnAlias);
		}
		if(field.equals(VersamentoIncasso.model().ID_PAGAMENTO_PORTALE.SRC_VERSANTE_IDENTIFICATIVO)){
			return this.toTable(VersamentoIncasso.model().ID_PAGAMENTO_PORTALE, returnAlias);
		}
		if(field.equals(VersamentoIncasso.model().IUV.IUV)){
			return this.toTable(VersamentoIncasso.model().IUV, returnAlias);
		}
		if(field.equals(VersamentoIncasso.model().IMPORTO_TOTALE)){
			return this.toTable(VersamentoIncasso.model(), returnAlias);
		}
		if(field.equals(VersamentoIncasso.model().STATO_VERSAMENTO)){
			return this.toTable(VersamentoIncasso.model(), returnAlias);
		}
		if(field.equals(VersamentoIncasso.model().DESCRIZIONE_STATO)){
			return this.toTable(VersamentoIncasso.model(), returnAlias);
		}
		if(field.equals(VersamentoIncasso.model().AGGIORNABILE)){
			return this.toTable(VersamentoIncasso.model(), returnAlias);
		}
		if(field.equals(VersamentoIncasso.model().DATA_CREAZIONE)){
			return this.toTable(VersamentoIncasso.model(), returnAlias);
		}
		if(field.equals(VersamentoIncasso.model().DATA_VALIDITA)){
			return this.toTable(VersamentoIncasso.model(), returnAlias);
		}
		if(field.equals(VersamentoIncasso.model().DATA_SCADENZA)){
			return this.toTable(VersamentoIncasso.model(), returnAlias);
		}
		if(field.equals(VersamentoIncasso.model().DATA_ORA_ULTIMO_AGGIORNAMENTO)){
			return this.toTable(VersamentoIncasso.model(), returnAlias);
		}
		if(field.equals(VersamentoIncasso.model().CAUSALE_VERSAMENTO)){
			return this.toTable(VersamentoIncasso.model(), returnAlias);
		}
		if(field.equals(VersamentoIncasso.model().DEBITORE_TIPO)){
			return this.toTable(VersamentoIncasso.model(), returnAlias);
		}
		if(field.equals(VersamentoIncasso.model().DEBITORE_IDENTIFICATIVO)){
			return this.toTable(VersamentoIncasso.model(), returnAlias);
		}
		if(field.equals(VersamentoIncasso.model().DEBITORE_ANAGRAFICA)){
			return this.toTable(VersamentoIncasso.model(), returnAlias);
		}
		if(field.equals(VersamentoIncasso.model().DEBITORE_INDIRIZZO)){
			return this.toTable(VersamentoIncasso.model(), returnAlias);
		}
		if(field.equals(VersamentoIncasso.model().DEBITORE_CIVICO)){
			return this.toTable(VersamentoIncasso.model(), returnAlias);
		}
		if(field.equals(VersamentoIncasso.model().DEBITORE_CAP)){
			return this.toTable(VersamentoIncasso.model(), returnAlias);
		}
		if(field.equals(VersamentoIncasso.model().DEBITORE_LOCALITA)){
			return this.toTable(VersamentoIncasso.model(), returnAlias);
		}
		if(field.equals(VersamentoIncasso.model().DEBITORE_PROVINCIA)){
			return this.toTable(VersamentoIncasso.model(), returnAlias);
		}
		if(field.equals(VersamentoIncasso.model().DEBITORE_NAZIONE)){
			return this.toTable(VersamentoIncasso.model(), returnAlias);
		}
		if(field.equals(VersamentoIncasso.model().DEBITORE_EMAIL)){
			return this.toTable(VersamentoIncasso.model(), returnAlias);
		}
		if(field.equals(VersamentoIncasso.model().DEBITORE_TELEFONO)){
			return this.toTable(VersamentoIncasso.model(), returnAlias);
		}
		if(field.equals(VersamentoIncasso.model().DEBITORE_CELLULARE)){
			return this.toTable(VersamentoIncasso.model(), returnAlias);
		}
		if(field.equals(VersamentoIncasso.model().DEBITORE_FAX)){
			return this.toTable(VersamentoIncasso.model(), returnAlias);
		}
		if(field.equals(VersamentoIncasso.model().TASSONOMIA_AVVISO)){
			return this.toTable(VersamentoIncasso.model(), returnAlias);
		}
		if(field.equals(VersamentoIncasso.model().TASSONOMIA)){
			return this.toTable(VersamentoIncasso.model(), returnAlias);
		}
		if(field.equals(VersamentoIncasso.model().COD_LOTTO)){
			return this.toTable(VersamentoIncasso.model(), returnAlias);
		}
		if(field.equals(VersamentoIncasso.model().COD_VERSAMENTO_LOTTO)){
			return this.toTable(VersamentoIncasso.model(), returnAlias);
		}
		if(field.equals(VersamentoIncasso.model().COD_ANNO_TRIBUTARIO)){
			return this.toTable(VersamentoIncasso.model(), returnAlias);
		}
		if(field.equals(VersamentoIncasso.model().COD_BUNDLEKEY)){
			return this.toTable(VersamentoIncasso.model(), returnAlias);
		}
		if(field.equals(VersamentoIncasso.model().DATI_ALLEGATI)){
			return this.toTable(VersamentoIncasso.model(), returnAlias);
		}
		if(field.equals(VersamentoIncasso.model().INCASSO)){
			return this.toTable(VersamentoIncasso.model(), returnAlias);
		}
		if(field.equals(VersamentoIncasso.model().ANOMALIE)){
			return this.toTable(VersamentoIncasso.model(), returnAlias);
		}
		if(field.equals(VersamentoIncasso.model().IUV_VERSAMENTO)){
			return this.toTable(VersamentoIncasso.model(), returnAlias);
		}
		if(field.equals(VersamentoIncasso.model().NUMERO_AVVISO)){
			return this.toTable(VersamentoIncasso.model(), returnAlias);
		}
		if(field.equals(VersamentoIncasso.model().AVVISATURA_ABILITATA)){
			return this.toTable(VersamentoIncasso.model(), returnAlias);
		}
		if(field.equals(VersamentoIncasso.model().AVVISATURA_DA_INVIARE)){
			return this.toTable(VersamentoIncasso.model(), returnAlias);
		}
		if(field.equals(VersamentoIncasso.model().AVVISATURA_OPERAZIONE)){
			return this.toTable(VersamentoIncasso.model(), returnAlias);
		}
		if(field.equals(VersamentoIncasso.model().AVVISATURA_MODALITA)){
			return this.toTable(VersamentoIncasso.model(), returnAlias);
		}
		if(field.equals(VersamentoIncasso.model().AVVISATURA_TIPO_PAGAMENTO)){
			return this.toTable(VersamentoIncasso.model(), returnAlias);
		}
		if(field.equals(VersamentoIncasso.model().AVVISATURA_COD_AVVISATURA)){
			return this.toTable(VersamentoIncasso.model(), returnAlias);
		}
		if(field.equals(VersamentoIncasso.model().ID_TRACCIATO_AVVISATURA.ID_TRACCIATO)){
			return this.toTable(VersamentoIncasso.model().ID_TRACCIATO_AVVISATURA, returnAlias);
		}
		if(field.equals(VersamentoIncasso.model().ACK)){
			return this.toTable(VersamentoIncasso.model(), returnAlias);
		}
		if(field.equals(VersamentoIncasso.model().ANOMALO)){
			return this.toTable(VersamentoIncasso.model(), returnAlias);
		}
		if(field.equals(VersamentoIncasso.model().DATA_PAGAMENTO)){
			return this.toTable(VersamentoIncasso.model(), returnAlias);
		}
		if(field.equals(VersamentoIncasso.model().IMPORTO_PAGATO)){
			return this.toTable(VersamentoIncasso.model(), returnAlias);
		}
		if(field.equals(VersamentoIncasso.model().IMPORTO_INCASSATO)){
			return this.toTable(VersamentoIncasso.model(), returnAlias);
		}
		if(field.equals(VersamentoIncasso.model().STATO_PAGAMENTO)){
			return this.toTable(VersamentoIncasso.model(), returnAlias);
		}
		if(field.equals(VersamentoIncasso.model().IUV_PAGAMENTO)){
			return this.toTable(VersamentoIncasso.model(), returnAlias);
		}
		if(field.equals(VersamentoIncasso.model().DIVISIONE)){
			return this.toTable(VersamentoIncasso.model(), returnAlias);
		}
		if(field.equals(VersamentoIncasso.model().DIREZIONE)){
			return this.toTable(VersamentoIncasso.model(), returnAlias);
		}
		if(field.equals(VersamentoIncasso.model().SMART_ORDER_DATE)){
			return this.toTable(VersamentoIncasso.model(), returnAlias);
		}
		if(field.equals(VersamentoIncasso.model().SMART_ORDER_RANK)){
			return this.toTable(VersamentoIncasso.model(), returnAlias);
		}
		if(field.equals(VersamentoIncasso.model().ID_SESSIONE)){
			return this.toTable(VersamentoIncasso.model(), returnAlias);
		}
		if(field.equals(VersamentoIncasso.model().SRC_IUV)){
			return this.toTable(VersamentoIncasso.model(), returnAlias);
		}
		if(field.equals(VersamentoIncasso.model().SRC_DEBITORE_IDENTIFICATIVO)){
			return this.toTable(VersamentoIncasso.model(), returnAlias);
		}


		return super.toTable(field,returnAlias);
		
	}

	@Override
	public String toTable(IModel<?> model,boolean returnAlias) throws ExpressionException {
		
		// In the case of table with alias, using parameter returnAlias​​, 
		// it is possible to drive the choice whether to return only the alias or 
		// the full definition of the table containing the alias
		
		if(model.equals(VersamentoIncasso.model())){
			return "versamenti_incassi";
		}
		if(model.equals(VersamentoIncasso.model().ID_TIPO_VERSAMENTO_DOMINIO)){
			return "tipi_vers_domini";
		}
		if(model.equals(VersamentoIncasso.model().ID_TIPO_VERSAMENTO_DOMINIO.ID_DOMINIO)){
			return "domini";
		}
		if(model.equals(VersamentoIncasso.model().ID_TIPO_VERSAMENTO_DOMINIO.ID_TIPO_VERSAMENTO)){
			return "tipi_versamento";
		}
		if(model.equals(VersamentoIncasso.model().ID_TIPO_VERSAMENTO)){
			return "tipi_versamento";
		}
		if(model.equals(VersamentoIncasso.model().ID_DOMINIO)){
			return "domini";
		}
		if(model.equals(VersamentoIncasso.model().ID_UO)){
			return "uo";
		}
		if(model.equals(VersamentoIncasso.model().ID_UO.ID_DOMINIO)){
			return "domini";
		}
		if(model.equals(VersamentoIncasso.model().ID_APPLICAZIONE)){
			return "applicazioni";
		}
		if(model.equals(VersamentoIncasso.model().ID_PAGAMENTO_PORTALE)){
			return "pagamenti_portale";
		}
		if(model.equals(VersamentoIncasso.model().ID_PAGAMENTO_PORTALE.ID_APPLICAZIONE)){
			return "applicazioni";
		}
		if(model.equals(VersamentoIncasso.model().IUV)){
			return "iuv";
		}
		if(model.equals(VersamentoIncasso.model().ID_TRACCIATO_AVVISATURA)){
			return "tracciati";
		}


		return super.toTable(model,returnAlias);
		
	}

}
