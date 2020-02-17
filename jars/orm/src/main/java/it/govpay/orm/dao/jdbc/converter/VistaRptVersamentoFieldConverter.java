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

import it.govpay.orm.VistaRptVersamento;


/**     
 * VistaRptVersamentoFieldConverter
 *
 * @author Giovanni Bussu (bussu@link.it)
 * @author Lorenzo Nardi (nardi@link.it)
 * @author $Author$
 * @version $Rev$, $Date$
 */
public class VistaRptVersamentoFieldConverter extends AbstractSQLFieldConverter {

	private TipiDatabase databaseType;
	
	public VistaRptVersamentoFieldConverter(String databaseType){
		this.databaseType = TipiDatabase.toEnumConstant(databaseType);
	}
	public VistaRptVersamentoFieldConverter(TipiDatabase databaseType){
		this.databaseType = databaseType;
	}


	@Override
	public IModel<?> getRootModel() throws ExpressionException {
		return VistaRptVersamento.model();
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
		
		if(field.equals(VistaRptVersamento.model().ID_PAGAMENTO_PORTALE.ID_SESSIONE)){
			if(appendTablePrefix){
				return this.toAliasTable(field)+".id_sessione";
			}else{
				return "id_sessione";
			}
		}
		if(field.equals(VistaRptVersamento.model().ID_PAGAMENTO_PORTALE.ID_APPLICAZIONE.COD_APPLICAZIONE)){
			if(appendTablePrefix){
				return this.toAliasTable(field)+".cod_applicazione";
			}else{
				return "cod_applicazione";
			}
		}
		if(field.equals(VistaRptVersamento.model().ID_PAGAMENTO_PORTALE.VERSANTE_IDENTIFICATIVO)){
			if(appendTablePrefix){
				return this.toAliasTable(field)+".versante_identificativo";
			}else{
				return "versante_identificativo";
			}
		}
		if(field.equals(VistaRptVersamento.model().COD_CARRELLO)){
			if(appendTablePrefix){
				return this.toAliasTable(field)+".cod_carrello";
			}else{
				return "cod_carrello";
			}
		}
		if(field.equals(VistaRptVersamento.model().IUV)){
			if(appendTablePrefix){
				return this.toAliasTable(field)+".iuv";
			}else{
				return "iuv";
			}
		}
		if(field.equals(VistaRptVersamento.model().CCP)){
			if(appendTablePrefix){
				return this.toAliasTable(field)+".ccp";
			}else{
				return "ccp";
			}
		}
		if(field.equals(VistaRptVersamento.model().COD_DOMINIO)){
			if(appendTablePrefix){
				return this.toAliasTable(field)+".cod_dominio";
			}else{
				return "cod_dominio";
			}
		}
		if(field.equals(VistaRptVersamento.model().COD_MSG_RICHIESTA)){
			if(appendTablePrefix){
				return this.toAliasTable(field)+".cod_msg_richiesta";
			}else{
				return "cod_msg_richiesta";
			}
		}
		if(field.equals(VistaRptVersamento.model().DATA_MSG_RICHIESTA)){
			if(appendTablePrefix){
				return this.toAliasTable(field)+".data_msg_richiesta";
			}else{
				return "data_msg_richiesta";
			}
		}
		if(field.equals(VistaRptVersamento.model().STATO)){
			if(appendTablePrefix){
				return this.toAliasTable(field)+".stato";
			}else{
				return "stato";
			}
		}
		if(field.equals(VistaRptVersamento.model().DESCRIZIONE_STATO)){
			if(appendTablePrefix){
				return this.toAliasTable(field)+".descrizione_stato";
			}else{
				return "descrizione_stato";
			}
		}
		if(field.equals(VistaRptVersamento.model().COD_SESSIONE)){
			if(appendTablePrefix){
				return this.toAliasTable(field)+".cod_sessione";
			}else{
				return "cod_sessione";
			}
		}
		if(field.equals(VistaRptVersamento.model().COD_SESSIONE_PORTALE)){
			if(appendTablePrefix){
				return this.toAliasTable(field)+".cod_sessione_portale";
			}else{
				return "cod_sessione_portale";
			}
		}
		if(field.equals(VistaRptVersamento.model().PSP_REDIRECT_URL)){
			if(appendTablePrefix){
				return this.toAliasTable(field)+".psp_redirect_url";
			}else{
				return "psp_redirect_url";
			}
		}
		if(field.equals(VistaRptVersamento.model().XML_RPT)){
			if(appendTablePrefix){
				return this.toAliasTable(field)+".xml_rpt";
			}else{
				return "xml_rpt";
			}
		}
		if(field.equals(VistaRptVersamento.model().DATA_AGGIORNAMENTO_STATO)){
			if(appendTablePrefix){
				return this.toAliasTable(field)+".data_aggiornamento_stato";
			}else{
				return "data_aggiornamento_stato";
			}
		}
		if(field.equals(VistaRptVersamento.model().CALLBACK_URL)){
			if(appendTablePrefix){
				return this.toAliasTable(field)+".callback_url";
			}else{
				return "callback_url";
			}
		}
		if(field.equals(VistaRptVersamento.model().MODELLO_PAGAMENTO)){
			if(appendTablePrefix){
				return this.toAliasTable(field)+".modello_pagamento";
			}else{
				return "modello_pagamento";
			}
		}
		if(field.equals(VistaRptVersamento.model().COD_MSG_RICEVUTA)){
			if(appendTablePrefix){
				return this.toAliasTable(field)+".cod_msg_ricevuta";
			}else{
				return "cod_msg_ricevuta";
			}
		}
		if(field.equals(VistaRptVersamento.model().DATA_MSG_RICEVUTA)){
			if(appendTablePrefix){
				return this.toAliasTable(field)+".data_msg_ricevuta";
			}else{
				return "data_msg_ricevuta";
			}
		}
		if(field.equals(VistaRptVersamento.model().COD_ESITO_PAGAMENTO)){
			if(appendTablePrefix){
				return this.toAliasTable(field)+".cod_esito_pagamento";
			}else{
				return "cod_esito_pagamento";
			}
		}
		if(field.equals(VistaRptVersamento.model().IMPORTO_TOTALE_PAGATO)){
			if(appendTablePrefix){
				return this.toAliasTable(field)+".importo_totale_pagato";
			}else{
				return "importo_totale_pagato";
			}
		}
		if(field.equals(VistaRptVersamento.model().XML_RT)){
			if(appendTablePrefix){
				return this.toAliasTable(field)+".xml_rt";
			}else{
				return "xml_rt";
			}
		}
		if(field.equals(VistaRptVersamento.model().COD_CANALE)){
			if(appendTablePrefix){
				return this.toAliasTable(field)+".cod_canale";
			}else{
				return "cod_canale";
			}
		}
		if(field.equals(VistaRptVersamento.model().COD_PSP)){
			if(appendTablePrefix){
				return this.toAliasTable(field)+".cod_psp";
			}else{
				return "cod_psp";
			}
		}
		if(field.equals(VistaRptVersamento.model().COD_INTERMEDIARIO_PSP)){
			if(appendTablePrefix){
				return this.toAliasTable(field)+".cod_intermediario_psp";
			}else{
				return "cod_intermediario_psp";
			}
		}
		if(field.equals(VistaRptVersamento.model().TIPO_VERSAMENTO)){
			if(appendTablePrefix){
				return this.toAliasTable(field)+".tipo_versamento";
			}else{
				return "tipo_versamento";
			}
		}
		if(field.equals(VistaRptVersamento.model().TIPO_IDENTIFICATIVO_ATTESTANTE)){
			if(appendTablePrefix){
				return this.toAliasTable(field)+".tipo_identificativo_attestante";
			}else{
				return "tipo_identificativo_attestante";
			}
		}
		if(field.equals(VistaRptVersamento.model().IDENTIFICATIVO_ATTESTANTE)){
			if(appendTablePrefix){
				return this.toAliasTable(field)+".identificativo_attestante";
			}else{
				return "identificativo_attestante";
			}
		}
		if(field.equals(VistaRptVersamento.model().DENOMINAZIONE_ATTESTANTE)){
			if(appendTablePrefix){
				return this.toAliasTable(field)+".denominazione_attestante";
			}else{
				return "denominazione_attestante";
			}
		}
		if(field.equals(VistaRptVersamento.model().COD_STAZIONE)){
			if(appendTablePrefix){
				return this.toAliasTable(field)+".cod_stazione";
			}else{
				return "cod_stazione";
			}
		}
		if(field.equals(VistaRptVersamento.model().COD_TRANSAZIONE_RPT)){
			if(appendTablePrefix){
				return this.toAliasTable(field)+".cod_transazione_rpt";
			}else{
				return "cod_transazione_rpt";
			}
		}
		if(field.equals(VistaRptVersamento.model().COD_TRANSAZIONE_RT)){
			if(appendTablePrefix){
				return this.toAliasTable(field)+".cod_transazione_rt";
			}else{
				return "cod_transazione_rt";
			}
		}
		if(field.equals(VistaRptVersamento.model().STATO_CONSERVAZIONE)){
			if(appendTablePrefix){
				return this.toAliasTable(field)+".stato_conservazione";
			}else{
				return "stato_conservazione";
			}
		}
		if(field.equals(VistaRptVersamento.model().DESCRIZIONE_STATO_CONS)){
			if(appendTablePrefix){
				return this.toAliasTable(field)+".descrizione_stato_cons";
			}else{
				return "descrizione_stato_cons";
			}
		}
		if(field.equals(VistaRptVersamento.model().DATA_CONSERVAZIONE)){
			if(appendTablePrefix){
				return this.toAliasTable(field)+".data_conservazione";
			}else{
				return "data_conservazione";
			}
		}
		if(field.equals(VistaRptVersamento.model().BLOCCANTE)){
			if(appendTablePrefix){
				return this.toAliasTable(field)+".bloccante";
			}else{
				return "bloccante";
			}
		}
		if(field.equals(VistaRptVersamento.model().VRS_ID)){
			if(appendTablePrefix){
				return this.toAliasTable(field)+".vrs_id";
			}else{
				return "vrs_id";
			}
		}
		if(field.equals(VistaRptVersamento.model().VRS_COD_VERSAMENTO_ENTE)){
			if(appendTablePrefix){
				return this.toAliasTable(field)+".vrs_cod_versamento_ente";
			}else{
				return "vrs_cod_versamento_ente";
			}
		}
		if(field.equals(VistaRptVersamento.model().VRS_NOME)){
			if(appendTablePrefix){
				return this.toAliasTable(field)+".vrs_nome";
			}else{
				return "vrs_nome";
			}
		}
		if(field.equals(VistaRptVersamento.model().VRS_ID_TIPO_VERSAMENTO_DOMINIO.ID_DOMINIO.COD_DOMINIO)){
			if(appendTablePrefix){
				return this.toAliasTable(field)+".cod_dominio";
			}else{
				return "cod_dominio";
			}
		}
		if(field.equals(VistaRptVersamento.model().VRS_ID_TIPO_VERSAMENTO_DOMINIO.ID_TIPO_VERSAMENTO.COD_TIPO_VERSAMENTO)){
			if(appendTablePrefix){
				return this.toAliasTable(field)+".cod_tipo_versamento";
			}else{
				return "cod_tipo_versamento";
			}
		}
		if(field.equals(VistaRptVersamento.model().VRS_ID_TIPO_VERSAMENTO_DOMINIO.ID_TIPO_VERSAMENTO.TIPO)){
			if(appendTablePrefix){
				return this.toAliasTable(field)+".tipo";
			}else{
				return "tipo";
			}
		}
		if(field.equals(VistaRptVersamento.model().VRS_ID_TIPO_VERSAMENTO.COD_TIPO_VERSAMENTO)){
			if(appendTablePrefix){
				return this.toAliasTable(field)+".cod_tipo_versamento";
			}else{
				return "cod_tipo_versamento";
			}
		}
		if(field.equals(VistaRptVersamento.model().VRS_ID_TIPO_VERSAMENTO.TIPO)){
			if(appendTablePrefix){
				return this.toAliasTable(field)+".tipo";
			}else{
				return "tipo";
			}
		}
		if(field.equals(VistaRptVersamento.model().VRS_ID_DOMINIO.COD_DOMINIO)){
			if(appendTablePrefix){
				return this.toAliasTable(field)+".cod_dominio";
			}else{
				return "cod_dominio";
			}
		}
		if(field.equals(VistaRptVersamento.model().VRS_ID_UO.COD_UO)){
			if(appendTablePrefix){
				return this.toAliasTable(field)+".cod_uo";
			}else{
				return "cod_uo";
			}
		}
		if(field.equals(VistaRptVersamento.model().VRS_ID_UO.ID_DOMINIO.COD_DOMINIO)){
			if(appendTablePrefix){
				return this.toAliasTable(field)+".cod_dominio";
			}else{
				return "cod_dominio";
			}
		}
		if(field.equals(VistaRptVersamento.model().VRS_ID_APPLICAZIONE.COD_APPLICAZIONE)){
			if(appendTablePrefix){
				return this.toAliasTable(field)+".cod_applicazione";
			}else{
				return "cod_applicazione";
			}
		}
		if(field.equals(VistaRptVersamento.model().VRS_IMPORTO_TOTALE)){
			if(appendTablePrefix){
				return this.toAliasTable(field)+".vrs_importo_totale";
			}else{
				return "vrs_importo_totale";
			}
		}
		if(field.equals(VistaRptVersamento.model().VRS_STATO_VERSAMENTO)){
			if(appendTablePrefix){
				return this.toAliasTable(field)+".vrs_stato_versamento";
			}else{
				return "vrs_stato_versamento";
			}
		}
		if(field.equals(VistaRptVersamento.model().VRS_DESCRIZIONE_STATO)){
			if(appendTablePrefix){
				return this.toAliasTable(field)+".vrs_descrizione_stato";
			}else{
				return "vrs_descrizione_stato";
			}
		}
		if(field.equals(VistaRptVersamento.model().VRS_AGGIORNABILE)){
			if(appendTablePrefix){
				return this.toAliasTable(field)+".vrs_aggiornabile";
			}else{
				return "vrs_aggiornabile";
			}
		}
		if(field.equals(VistaRptVersamento.model().VRS_DATA_CREAZIONE)){
			if(appendTablePrefix){
				return this.toAliasTable(field)+".vrs_data_creazione";
			}else{
				return "vrs_data_creazione";
			}
		}
		if(field.equals(VistaRptVersamento.model().VRS_DATA_VALIDITA)){
			if(appendTablePrefix){
				return this.toAliasTable(field)+".vrs_data_validita";
			}else{
				return "vrs_data_validita";
			}
		}
		if(field.equals(VistaRptVersamento.model().VRS_DATA_SCADENZA)){
			if(appendTablePrefix){
				return this.toAliasTable(field)+".vrs_data_scadenza";
			}else{
				return "vrs_data_scadenza";
			}
		}
		if(field.equals(VistaRptVersamento.model().VRS_DATA_ORA_ULTIMO_AGG)){
			if(appendTablePrefix){
				return this.toAliasTable(field)+".vrs_data_ora_ultimo_agg";
			}else{
				return "vrs_data_ora_ultimo_agg";
			}
		}
		if(field.equals(VistaRptVersamento.model().VRS_CAUSALE_VERSAMENTO)){
			if(appendTablePrefix){
				return this.toAliasTable(field)+".vrs_causale_versamento";
			}else{
				return "vrs_causale_versamento";
			}
		}
		if(field.equals(VistaRptVersamento.model().VRS_DEBITORE_TIPO)){
			if(appendTablePrefix){
				return this.toAliasTable(field)+".vrs_debitore_tipo";
			}else{
				return "vrs_debitore_tipo";
			}
		}
		if(field.equals(VistaRptVersamento.model().VRS_DEBITORE_IDENTIFICATIVO)){
			if(appendTablePrefix){
				return this.toAliasTable(field)+".vrs_debitore_identificativo";
			}else{
				return "vrs_debitore_identificativo";
			}
		}
		if(field.equals(VistaRptVersamento.model().VRS_DEBITORE_ANAGRAFICA)){
			if(appendTablePrefix){
				return this.toAliasTable(field)+".vrs_debitore_anagrafica";
			}else{
				return "vrs_debitore_anagrafica";
			}
		}
		if(field.equals(VistaRptVersamento.model().VRS_DEBITORE_INDIRIZZO)){
			if(appendTablePrefix){
				return this.toAliasTable(field)+".vrs_debitore_indirizzo";
			}else{
				return "vrs_debitore_indirizzo";
			}
		}
		if(field.equals(VistaRptVersamento.model().VRS_DEBITORE_CIVICO)){
			if(appendTablePrefix){
				return this.toAliasTable(field)+".vrs_debitore_civico";
			}else{
				return "vrs_debitore_civico";
			}
		}
		if(field.equals(VistaRptVersamento.model().VRS_DEBITORE_CAP)){
			if(appendTablePrefix){
				return this.toAliasTable(field)+".vrs_debitore_cap";
			}else{
				return "vrs_debitore_cap";
			}
		}
		if(field.equals(VistaRptVersamento.model().VRS_DEBITORE_LOCALITA)){
			if(appendTablePrefix){
				return this.toAliasTable(field)+".vrs_debitore_localita";
			}else{
				return "vrs_debitore_localita";
			}
		}
		if(field.equals(VistaRptVersamento.model().VRS_DEBITORE_PROVINCIA)){
			if(appendTablePrefix){
				return this.toAliasTable(field)+".vrs_debitore_provincia";
			}else{
				return "vrs_debitore_provincia";
			}
		}
		if(field.equals(VistaRptVersamento.model().VRS_DEBITORE_NAZIONE)){
			if(appendTablePrefix){
				return this.toAliasTable(field)+".vrs_debitore_nazione";
			}else{
				return "vrs_debitore_nazione";
			}
		}
		if(field.equals(VistaRptVersamento.model().VRS_DEBITORE_EMAIL)){
			if(appendTablePrefix){
				return this.toAliasTable(field)+".vrs_debitore_email";
			}else{
				return "vrs_debitore_email";
			}
		}
		if(field.equals(VistaRptVersamento.model().VRS_DEBITORE_TELEFONO)){
			if(appendTablePrefix){
				return this.toAliasTable(field)+".vrs_debitore_telefono";
			}else{
				return "vrs_debitore_telefono";
			}
		}
		if(field.equals(VistaRptVersamento.model().VRS_DEBITORE_CELLULARE)){
			if(appendTablePrefix){
				return this.toAliasTable(field)+".vrs_debitore_cellulare";
			}else{
				return "vrs_debitore_cellulare";
			}
		}
		if(field.equals(VistaRptVersamento.model().VRS_DEBITORE_FAX)){
			if(appendTablePrefix){
				return this.toAliasTable(field)+".vrs_debitore_fax";
			}else{
				return "vrs_debitore_fax";
			}
		}
		if(field.equals(VistaRptVersamento.model().VRS_TASSONOMIA_AVVISO)){
			if(appendTablePrefix){
				return this.toAliasTable(field)+".vrs_tassonomia_avviso";
			}else{
				return "vrs_tassonomia_avviso";
			}
		}
		if(field.equals(VistaRptVersamento.model().VRS_TASSONOMIA)){
			if(appendTablePrefix){
				return this.toAliasTable(field)+".vrs_tassonomia";
			}else{
				return "vrs_tassonomia";
			}
		}
		if(field.equals(VistaRptVersamento.model().VRS_COD_LOTTO)){
			if(appendTablePrefix){
				return this.toAliasTable(field)+".vrs_cod_lotto";
			}else{
				return "vrs_cod_lotto";
			}
		}
		if(field.equals(VistaRptVersamento.model().VRS_COD_VERSAMENTO_LOTTO)){
			if(appendTablePrefix){
				return this.toAliasTable(field)+".vrs_cod_versamento_lotto";
			}else{
				return "vrs_cod_versamento_lotto";
			}
		}
		if(field.equals(VistaRptVersamento.model().VRS_COD_ANNO_TRIBUTARIO)){
			if(appendTablePrefix){
				return this.toAliasTable(field)+".vrs_cod_anno_tributario";
			}else{
				return "vrs_cod_anno_tributario";
			}
		}
		if(field.equals(VistaRptVersamento.model().VRS_COD_BUNDLEKEY)){
			if(appendTablePrefix){
				return this.toAliasTable(field)+".vrs_cod_bundlekey";
			}else{
				return "vrs_cod_bundlekey";
			}
		}
		if(field.equals(VistaRptVersamento.model().VRS_DATI_ALLEGATI)){
			if(appendTablePrefix){
				return this.toAliasTable(field)+".vrs_dati_allegati";
			}else{
				return "vrs_dati_allegati";
			}
		}
		if(field.equals(VistaRptVersamento.model().VRS_INCASSO)){
			if(appendTablePrefix){
				return this.toAliasTable(field)+".vrs_incasso";
			}else{
				return "vrs_incasso";
			}
		}
		if(field.equals(VistaRptVersamento.model().VRS_ANOMALIE)){
			if(appendTablePrefix){
				return this.toAliasTable(field)+".vrs_anomalie";
			}else{
				return "vrs_anomalie";
			}
		}
		if(field.equals(VistaRptVersamento.model().VRS_IUV_VERSAMENTO)){
			if(appendTablePrefix){
				return this.toAliasTable(field)+".vrs_iuv_versamento";
			}else{
				return "vrs_iuv_versamento";
			}
		}
		if(field.equals(VistaRptVersamento.model().VRS_NUMERO_AVVISO)){
			if(appendTablePrefix){
				return this.toAliasTable(field)+".vrs_numero_avviso";
			}else{
				return "vrs_numero_avviso";
			}
		}
		if(field.equals(VistaRptVersamento.model().VRS_ACK)){
			if(appendTablePrefix){
				return this.toAliasTable(field)+".vrs_ack";
			}else{
				return "vrs_ack";
			}
		}
		if(field.equals(VistaRptVersamento.model().VRS_ANOMALO)){
			if(appendTablePrefix){
				return this.toAliasTable(field)+".vrs_anomalo";
			}else{
				return "vrs_anomalo";
			}
		}
		if(field.equals(VistaRptVersamento.model().VRS_DIVISIONE)){
			if(appendTablePrefix){
				return this.toAliasTable(field)+".vrs_divisione";
			}else{
				return "vrs_divisione";
			}
		}
		if(field.equals(VistaRptVersamento.model().VRS_DIREZIONE)){
			if(appendTablePrefix){
				return this.toAliasTable(field)+".vrs_direzione";
			}else{
				return "vrs_direzione";
			}
		}
		if(field.equals(VistaRptVersamento.model().VRS_ID_SESSIONE)){
			if(appendTablePrefix){
				return this.toAliasTable(field)+".vrs_id_sessione";
			}else{
				return "vrs_id_sessione";
			}
		}
		if(field.equals(VistaRptVersamento.model().VRS_DATA_PAGAMENTO)){
			if(appendTablePrefix){
				return this.toAliasTable(field)+".vrs_data_pagamento";
			}else{
				return "vrs_data_pagamento";
			}
		}
		if(field.equals(VistaRptVersamento.model().VRS_IMPORTO_PAGATO)){
			if(appendTablePrefix){
				return this.toAliasTable(field)+".vrs_importo_pagato";
			}else{
				return "vrs_importo_pagato";
			}
		}
		if(field.equals(VistaRptVersamento.model().VRS_IMPORTO_INCASSATO)){
			if(appendTablePrefix){
				return this.toAliasTable(field)+".vrs_importo_incassato";
			}else{
				return "vrs_importo_incassato";
			}
		}
		if(field.equals(VistaRptVersamento.model().VRS_STATO_PAGAMENTO)){
			if(appendTablePrefix){
				return this.toAliasTable(field)+".vrs_stato_pagamento";
			}else{
				return "vrs_stato_pagamento";
			}
		}
		if(field.equals(VistaRptVersamento.model().VRS_IUV_PAGAMENTO)){
			if(appendTablePrefix){
				return this.toAliasTable(field)+".vrs_iuv_pagamento";
			}else{
				return "vrs_iuv_pagamento";
			}
		}


		return super.toColumn(field,returnAlias,appendTablePrefix);
		
	}
	
	@Override
	public String toTable(IField field,boolean returnAlias) throws ExpressionException {
		
		// In the case of table with alias, using parameter returnAlias​​, 
		// it is possible to drive the choice whether to return only the alias or 
		// the full definition of the table containing the alias
		
		if(field.equals(VistaRptVersamento.model().ID_PAGAMENTO_PORTALE.ID_SESSIONE)){
			return this.toTable(VistaRptVersamento.model().ID_PAGAMENTO_PORTALE, returnAlias);
		}
		if(field.equals(VistaRptVersamento.model().ID_PAGAMENTO_PORTALE.ID_APPLICAZIONE.COD_APPLICAZIONE)){
			return this.toTable(VistaRptVersamento.model().ID_PAGAMENTO_PORTALE.ID_APPLICAZIONE, returnAlias);
		}
		if(field.equals(VistaRptVersamento.model().ID_PAGAMENTO_PORTALE.VERSANTE_IDENTIFICATIVO)){
			return this.toTable(VistaRptVersamento.model().ID_PAGAMENTO_PORTALE, returnAlias);
		}
		if(field.equals(VistaRptVersamento.model().COD_CARRELLO)){
			return this.toTable(VistaRptVersamento.model(), returnAlias);
		}
		if(field.equals(VistaRptVersamento.model().IUV)){
			return this.toTable(VistaRptVersamento.model(), returnAlias);
		}
		if(field.equals(VistaRptVersamento.model().CCP)){
			return this.toTable(VistaRptVersamento.model(), returnAlias);
		}
		if(field.equals(VistaRptVersamento.model().COD_DOMINIO)){
			return this.toTable(VistaRptVersamento.model(), returnAlias);
		}
		if(field.equals(VistaRptVersamento.model().COD_MSG_RICHIESTA)){
			return this.toTable(VistaRptVersamento.model(), returnAlias);
		}
		if(field.equals(VistaRptVersamento.model().DATA_MSG_RICHIESTA)){
			return this.toTable(VistaRptVersamento.model(), returnAlias);
		}
		if(field.equals(VistaRptVersamento.model().STATO)){
			return this.toTable(VistaRptVersamento.model(), returnAlias);
		}
		if(field.equals(VistaRptVersamento.model().DESCRIZIONE_STATO)){
			return this.toTable(VistaRptVersamento.model(), returnAlias);
		}
		if(field.equals(VistaRptVersamento.model().COD_SESSIONE)){
			return this.toTable(VistaRptVersamento.model(), returnAlias);
		}
		if(field.equals(VistaRptVersamento.model().COD_SESSIONE_PORTALE)){
			return this.toTable(VistaRptVersamento.model(), returnAlias);
		}
		if(field.equals(VistaRptVersamento.model().PSP_REDIRECT_URL)){
			return this.toTable(VistaRptVersamento.model(), returnAlias);
		}
		if(field.equals(VistaRptVersamento.model().XML_RPT)){
			return this.toTable(VistaRptVersamento.model(), returnAlias);
		}
		if(field.equals(VistaRptVersamento.model().DATA_AGGIORNAMENTO_STATO)){
			return this.toTable(VistaRptVersamento.model(), returnAlias);
		}
		if(field.equals(VistaRptVersamento.model().CALLBACK_URL)){
			return this.toTable(VistaRptVersamento.model(), returnAlias);
		}
		if(field.equals(VistaRptVersamento.model().MODELLO_PAGAMENTO)){
			return this.toTable(VistaRptVersamento.model(), returnAlias);
		}
		if(field.equals(VistaRptVersamento.model().COD_MSG_RICEVUTA)){
			return this.toTable(VistaRptVersamento.model(), returnAlias);
		}
		if(field.equals(VistaRptVersamento.model().DATA_MSG_RICEVUTA)){
			return this.toTable(VistaRptVersamento.model(), returnAlias);
		}
		if(field.equals(VistaRptVersamento.model().COD_ESITO_PAGAMENTO)){
			return this.toTable(VistaRptVersamento.model(), returnAlias);
		}
		if(field.equals(VistaRptVersamento.model().IMPORTO_TOTALE_PAGATO)){
			return this.toTable(VistaRptVersamento.model(), returnAlias);
		}
		if(field.equals(VistaRptVersamento.model().XML_RT)){
			return this.toTable(VistaRptVersamento.model(), returnAlias);
		}
		if(field.equals(VistaRptVersamento.model().COD_CANALE)){
			return this.toTable(VistaRptVersamento.model(), returnAlias);
		}
		if(field.equals(VistaRptVersamento.model().COD_PSP)){
			return this.toTable(VistaRptVersamento.model(), returnAlias);
		}
		if(field.equals(VistaRptVersamento.model().COD_INTERMEDIARIO_PSP)){
			return this.toTable(VistaRptVersamento.model(), returnAlias);
		}
		if(field.equals(VistaRptVersamento.model().TIPO_VERSAMENTO)){
			return this.toTable(VistaRptVersamento.model(), returnAlias);
		}
		if(field.equals(VistaRptVersamento.model().TIPO_IDENTIFICATIVO_ATTESTANTE)){
			return this.toTable(VistaRptVersamento.model(), returnAlias);
		}
		if(field.equals(VistaRptVersamento.model().IDENTIFICATIVO_ATTESTANTE)){
			return this.toTable(VistaRptVersamento.model(), returnAlias);
		}
		if(field.equals(VistaRptVersamento.model().DENOMINAZIONE_ATTESTANTE)){
			return this.toTable(VistaRptVersamento.model(), returnAlias);
		}
		if(field.equals(VistaRptVersamento.model().COD_STAZIONE)){
			return this.toTable(VistaRptVersamento.model(), returnAlias);
		}
		if(field.equals(VistaRptVersamento.model().COD_TRANSAZIONE_RPT)){
			return this.toTable(VistaRptVersamento.model(), returnAlias);
		}
		if(field.equals(VistaRptVersamento.model().COD_TRANSAZIONE_RT)){
			return this.toTable(VistaRptVersamento.model(), returnAlias);
		}
		if(field.equals(VistaRptVersamento.model().STATO_CONSERVAZIONE)){
			return this.toTable(VistaRptVersamento.model(), returnAlias);
		}
		if(field.equals(VistaRptVersamento.model().DESCRIZIONE_STATO_CONS)){
			return this.toTable(VistaRptVersamento.model(), returnAlias);
		}
		if(field.equals(VistaRptVersamento.model().DATA_CONSERVAZIONE)){
			return this.toTable(VistaRptVersamento.model(), returnAlias);
		}
		if(field.equals(VistaRptVersamento.model().BLOCCANTE)){
			return this.toTable(VistaRptVersamento.model(), returnAlias);
		}
		if(field.equals(VistaRptVersamento.model().VRS_ID)){
			return this.toTable(VistaRptVersamento.model(), returnAlias);
		}
		if(field.equals(VistaRptVersamento.model().VRS_COD_VERSAMENTO_ENTE)){
			return this.toTable(VistaRptVersamento.model(), returnAlias);
		}
		if(field.equals(VistaRptVersamento.model().VRS_NOME)){
			return this.toTable(VistaRptVersamento.model(), returnAlias);
		}
		if(field.equals(VistaRptVersamento.model().VRS_ID_TIPO_VERSAMENTO_DOMINIO.ID_DOMINIO.COD_DOMINIO)){
			return this.toTable(VistaRptVersamento.model().VRS_ID_TIPO_VERSAMENTO_DOMINIO.ID_DOMINIO, returnAlias);
		}
		if(field.equals(VistaRptVersamento.model().VRS_ID_TIPO_VERSAMENTO_DOMINIO.ID_TIPO_VERSAMENTO.COD_TIPO_VERSAMENTO)){
			return this.toTable(VistaRptVersamento.model().VRS_ID_TIPO_VERSAMENTO_DOMINIO.ID_TIPO_VERSAMENTO, returnAlias);
		}
		if(field.equals(VistaRptVersamento.model().VRS_ID_TIPO_VERSAMENTO_DOMINIO.ID_TIPO_VERSAMENTO.TIPO)){
			return this.toTable(VistaRptVersamento.model().VRS_ID_TIPO_VERSAMENTO_DOMINIO.ID_TIPO_VERSAMENTO, returnAlias);
		}
		if(field.equals(VistaRptVersamento.model().VRS_ID_TIPO_VERSAMENTO.COD_TIPO_VERSAMENTO)){
			return this.toTable(VistaRptVersamento.model().VRS_ID_TIPO_VERSAMENTO, returnAlias);
		}
		if(field.equals(VistaRptVersamento.model().VRS_ID_TIPO_VERSAMENTO.TIPO)){
			return this.toTable(VistaRptVersamento.model().VRS_ID_TIPO_VERSAMENTO, returnAlias);
		}
		if(field.equals(VistaRptVersamento.model().VRS_ID_DOMINIO.COD_DOMINIO)){
			return this.toTable(VistaRptVersamento.model().VRS_ID_DOMINIO, returnAlias);
		}
		if(field.equals(VistaRptVersamento.model().VRS_ID_UO.COD_UO)){
			return this.toTable(VistaRptVersamento.model().VRS_ID_UO, returnAlias);
		}
		if(field.equals(VistaRptVersamento.model().VRS_ID_UO.ID_DOMINIO.COD_DOMINIO)){
			return this.toTable(VistaRptVersamento.model().VRS_ID_UO.ID_DOMINIO, returnAlias);
		}
		if(field.equals(VistaRptVersamento.model().VRS_ID_APPLICAZIONE.COD_APPLICAZIONE)){
			return this.toTable(VistaRptVersamento.model().VRS_ID_APPLICAZIONE, returnAlias);
		}
		if(field.equals(VistaRptVersamento.model().VRS_IMPORTO_TOTALE)){
			return this.toTable(VistaRptVersamento.model(), returnAlias);
		}
		if(field.equals(VistaRptVersamento.model().VRS_STATO_VERSAMENTO)){
			return this.toTable(VistaRptVersamento.model(), returnAlias);
		}
		if(field.equals(VistaRptVersamento.model().VRS_DESCRIZIONE_STATO)){
			return this.toTable(VistaRptVersamento.model(), returnAlias);
		}
		if(field.equals(VistaRptVersamento.model().VRS_AGGIORNABILE)){
			return this.toTable(VistaRptVersamento.model(), returnAlias);
		}
		if(field.equals(VistaRptVersamento.model().VRS_DATA_CREAZIONE)){
			return this.toTable(VistaRptVersamento.model(), returnAlias);
		}
		if(field.equals(VistaRptVersamento.model().VRS_DATA_VALIDITA)){
			return this.toTable(VistaRptVersamento.model(), returnAlias);
		}
		if(field.equals(VistaRptVersamento.model().VRS_DATA_SCADENZA)){
			return this.toTable(VistaRptVersamento.model(), returnAlias);
		}
		if(field.equals(VistaRptVersamento.model().VRS_DATA_ORA_ULTIMO_AGG)){
			return this.toTable(VistaRptVersamento.model(), returnAlias);
		}
		if(field.equals(VistaRptVersamento.model().VRS_CAUSALE_VERSAMENTO)){
			return this.toTable(VistaRptVersamento.model(), returnAlias);
		}
		if(field.equals(VistaRptVersamento.model().VRS_DEBITORE_TIPO)){
			return this.toTable(VistaRptVersamento.model(), returnAlias);
		}
		if(field.equals(VistaRptVersamento.model().VRS_DEBITORE_IDENTIFICATIVO)){
			return this.toTable(VistaRptVersamento.model(), returnAlias);
		}
		if(field.equals(VistaRptVersamento.model().VRS_DEBITORE_ANAGRAFICA)){
			return this.toTable(VistaRptVersamento.model(), returnAlias);
		}
		if(field.equals(VistaRptVersamento.model().VRS_DEBITORE_INDIRIZZO)){
			return this.toTable(VistaRptVersamento.model(), returnAlias);
		}
		if(field.equals(VistaRptVersamento.model().VRS_DEBITORE_CIVICO)){
			return this.toTable(VistaRptVersamento.model(), returnAlias);
		}
		if(field.equals(VistaRptVersamento.model().VRS_DEBITORE_CAP)){
			return this.toTable(VistaRptVersamento.model(), returnAlias);
		}
		if(field.equals(VistaRptVersamento.model().VRS_DEBITORE_LOCALITA)){
			return this.toTable(VistaRptVersamento.model(), returnAlias);
		}
		if(field.equals(VistaRptVersamento.model().VRS_DEBITORE_PROVINCIA)){
			return this.toTable(VistaRptVersamento.model(), returnAlias);
		}
		if(field.equals(VistaRptVersamento.model().VRS_DEBITORE_NAZIONE)){
			return this.toTable(VistaRptVersamento.model(), returnAlias);
		}
		if(field.equals(VistaRptVersamento.model().VRS_DEBITORE_EMAIL)){
			return this.toTable(VistaRptVersamento.model(), returnAlias);
		}
		if(field.equals(VistaRptVersamento.model().VRS_DEBITORE_TELEFONO)){
			return this.toTable(VistaRptVersamento.model(), returnAlias);
		}
		if(field.equals(VistaRptVersamento.model().VRS_DEBITORE_CELLULARE)){
			return this.toTable(VistaRptVersamento.model(), returnAlias);
		}
		if(field.equals(VistaRptVersamento.model().VRS_DEBITORE_FAX)){
			return this.toTable(VistaRptVersamento.model(), returnAlias);
		}
		if(field.equals(VistaRptVersamento.model().VRS_TASSONOMIA_AVVISO)){
			return this.toTable(VistaRptVersamento.model(), returnAlias);
		}
		if(field.equals(VistaRptVersamento.model().VRS_TASSONOMIA)){
			return this.toTable(VistaRptVersamento.model(), returnAlias);
		}
		if(field.equals(VistaRptVersamento.model().VRS_COD_LOTTO)){
			return this.toTable(VistaRptVersamento.model(), returnAlias);
		}
		if(field.equals(VistaRptVersamento.model().VRS_COD_VERSAMENTO_LOTTO)){
			return this.toTable(VistaRptVersamento.model(), returnAlias);
		}
		if(field.equals(VistaRptVersamento.model().VRS_COD_ANNO_TRIBUTARIO)){
			return this.toTable(VistaRptVersamento.model(), returnAlias);
		}
		if(field.equals(VistaRptVersamento.model().VRS_COD_BUNDLEKEY)){
			return this.toTable(VistaRptVersamento.model(), returnAlias);
		}
		if(field.equals(VistaRptVersamento.model().VRS_DATI_ALLEGATI)){
			return this.toTable(VistaRptVersamento.model(), returnAlias);
		}
		if(field.equals(VistaRptVersamento.model().VRS_INCASSO)){
			return this.toTable(VistaRptVersamento.model(), returnAlias);
		}
		if(field.equals(VistaRptVersamento.model().VRS_ANOMALIE)){
			return this.toTable(VistaRptVersamento.model(), returnAlias);
		}
		if(field.equals(VistaRptVersamento.model().VRS_IUV_VERSAMENTO)){
			return this.toTable(VistaRptVersamento.model(), returnAlias);
		}
		if(field.equals(VistaRptVersamento.model().VRS_NUMERO_AVVISO)){
			return this.toTable(VistaRptVersamento.model(), returnAlias);
		}
		if(field.equals(VistaRptVersamento.model().VRS_ACK)){
			return this.toTable(VistaRptVersamento.model(), returnAlias);
		}
		if(field.equals(VistaRptVersamento.model().VRS_ANOMALO)){
			return this.toTable(VistaRptVersamento.model(), returnAlias);
		}
		if(field.equals(VistaRptVersamento.model().VRS_DIVISIONE)){
			return this.toTable(VistaRptVersamento.model(), returnAlias);
		}
		if(field.equals(VistaRptVersamento.model().VRS_DIREZIONE)){
			return this.toTable(VistaRptVersamento.model(), returnAlias);
		}
		if(field.equals(VistaRptVersamento.model().VRS_ID_SESSIONE)){
			return this.toTable(VistaRptVersamento.model(), returnAlias);
		}
		if(field.equals(VistaRptVersamento.model().VRS_DATA_PAGAMENTO)){
			return this.toTable(VistaRptVersamento.model(), returnAlias);
		}
		if(field.equals(VistaRptVersamento.model().VRS_IMPORTO_PAGATO)){
			return this.toTable(VistaRptVersamento.model(), returnAlias);
		}
		if(field.equals(VistaRptVersamento.model().VRS_IMPORTO_INCASSATO)){
			return this.toTable(VistaRptVersamento.model(), returnAlias);
		}
		if(field.equals(VistaRptVersamento.model().VRS_STATO_PAGAMENTO)){
			return this.toTable(VistaRptVersamento.model(), returnAlias);
		}
		if(field.equals(VistaRptVersamento.model().VRS_IUV_PAGAMENTO)){
			return this.toTable(VistaRptVersamento.model(), returnAlias);
		}


		return super.toTable(field,returnAlias);
		
	}

	@Override
	public String toTable(IModel<?> model,boolean returnAlias) throws ExpressionException {
		
		// In the case of table with alias, using parameter returnAlias​​, 
		// it is possible to drive the choice whether to return only the alias or 
		// the full definition of the table containing the alias
		
		if(model.equals(VistaRptVersamento.model())){
			return "v_rpt_versamenti";
		}
		if(model.equals(VistaRptVersamento.model().ID_PAGAMENTO_PORTALE)){
			return "pagamenti_portale";
		}
		if(model.equals(VistaRptVersamento.model().ID_PAGAMENTO_PORTALE.ID_APPLICAZIONE)){
			return "applicazioni";
		}
		if(model.equals(VistaRptVersamento.model().VRS_ID_TIPO_VERSAMENTO_DOMINIO)){
			return "tipi_vers_domini";
		}
		if(model.equals(VistaRptVersamento.model().VRS_ID_TIPO_VERSAMENTO_DOMINIO.ID_DOMINIO)){
			return "domini";
		}
		if(model.equals(VistaRptVersamento.model().VRS_ID_TIPO_VERSAMENTO_DOMINIO.ID_TIPO_VERSAMENTO)){
			return "tipo_versamento";
		}
		if(model.equals(VistaRptVersamento.model().VRS_ID_TIPO_VERSAMENTO)){
			return "tipi_versamento";
		}
		if(model.equals(VistaRptVersamento.model().VRS_ID_DOMINIO)){
			return "domini";
		}
		if(model.equals(VistaRptVersamento.model().VRS_ID_UO)){
			return "uo";
		}
		if(model.equals(VistaRptVersamento.model().VRS_ID_UO.ID_DOMINIO)){
			return "domini";
		}
		if(model.equals(VistaRptVersamento.model().VRS_ID_APPLICAZIONE)){
			return "applicazioni";
		}


		return super.toTable(model,returnAlias);
		
	}

}
