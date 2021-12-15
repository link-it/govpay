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

import it.govpay.orm.VistaVersamentoNonRendicontato;


/**     
 * VistaVersamentoNonRendicontatoFieldConverter
 *
 * @author Giovanni Bussu (bussu@link.it)
 * @author Lorenzo Nardi (nardi@link.it)
 * @author $Author$
 * @version $Rev$, $Date$
 */
public class VistaVersamentoNonRendicontatoFieldConverter extends AbstractSQLFieldConverter {

	private TipiDatabase databaseType;
	
	public VistaVersamentoNonRendicontatoFieldConverter(String databaseType){
		this.databaseType = TipiDatabase.toEnumConstant(databaseType);
	}
	public VistaVersamentoNonRendicontatoFieldConverter(TipiDatabase databaseType){
		this.databaseType = databaseType;
	}


	@Override
	public IModel<?> getRootModel() throws ExpressionException {
		return VistaVersamentoNonRendicontato.model();
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
		
		if(field.equals(VistaVersamentoNonRendicontato.model().SNG_ID_TRIBUTO.ID_DOMINIO.COD_DOMINIO)){
			if(appendTablePrefix){
				return this.toAliasTable(field)+".cod_dominio";
			}else{
				return "cod_dominio";
			}
		}
		if(field.equals(VistaVersamentoNonRendicontato.model().SNG_ID_TRIBUTO.ID_TIPO_TRIBUTO.COD_TRIBUTO)){
			if(appendTablePrefix){
				return this.toAliasTable(field)+".cod_tributo";
			}else{
				return "cod_tributo";
			}
		}
		if(field.equals(VistaVersamentoNonRendicontato.model().SNG_COD_SING_VERS_ENTE)){
			if(appendTablePrefix){
				return this.toAliasTable(field)+".sng_cod_sing_vers_ente";
			}else{
				return "sng_cod_sing_vers_ente";
			}
		}
		if(field.equals(VistaVersamentoNonRendicontato.model().SNG_STATO_SINGOLO_VERSAMENTO)){
			if(appendTablePrefix){
				return this.toAliasTable(field)+".sng_stato_singolo_versamento";
			}else{
				return "sng_stato_singolo_versamento";
			}
		}
		if(field.equals(VistaVersamentoNonRendicontato.model().SNG_IMPORTO_SINGOLO_VERSAMENTO)){
			if(appendTablePrefix){
				return this.toAliasTable(field)+".sng_importo_singolo_versamento";
			}else{
				return "sng_importo_singolo_versamento";
			}
		}
		if(field.equals(VistaVersamentoNonRendicontato.model().SNG_DESCRIZIONE)){
			if(appendTablePrefix){
				return this.toAliasTable(field)+".sng_descrizione";
			}else{
				return "sng_descrizione";
			}
		}
		if(field.equals(VistaVersamentoNonRendicontato.model().SNG_DATI_ALLEGATI)){
			if(appendTablePrefix){
				return this.toAliasTable(field)+".sng_dati_allegati";
			}else{
				return "sng_dati_allegati";
			}
		}
		if(field.equals(VistaVersamentoNonRendicontato.model().SNG_INDICE_DATI)){
			if(appendTablePrefix){
				return this.toAliasTable(field)+".sng_indice_dati";
			}else{
				return "sng_indice_dati";
			}
		}
		if(field.equals(VistaVersamentoNonRendicontato.model().SNG_DESCRIZIONE_CAUSALE_RPT)){
			if(appendTablePrefix){
				return this.toAliasTable(field)+".sng_descrizione_causale_rpt";
			}else{
				return "sng_descrizione_causale_rpt";
			}
		}
		if(field.equals(VistaVersamentoNonRendicontato.model().SNG_CONTABILITA)){
			if(appendTablePrefix){
				return this.toAliasTable(field)+".sng_contabilita";
			}else{
				return "sng_contabilita";
			}
		}
		if(field.equals(VistaVersamentoNonRendicontato.model().VRS_ID)){
			if(appendTablePrefix){
				return this.toAliasTable(field)+".vrs_id";
			}else{
				return "vrs_id";
			}
		}
		if(field.equals(VistaVersamentoNonRendicontato.model().VRS_COD_VERSAMENTO_ENTE)){
			if(appendTablePrefix){
				return this.toAliasTable(field)+".vrs_cod_versamento_ente";
			}else{
				return "vrs_cod_versamento_ente";
			}
		}
		if(field.equals(VistaVersamentoNonRendicontato.model().VRS_NOME)){
			if(appendTablePrefix){
				return this.toAliasTable(field)+".vrs_nome";
			}else{
				return "vrs_nome";
			}
		}
		if(field.equals(VistaVersamentoNonRendicontato.model().VRS_ID_TIPO_VERSAMENTO_DOMINIO.ID_DOMINIO.COD_DOMINIO)){
			if(appendTablePrefix){
				return this.toAliasTable(field)+".cod_dominio";
			}else{
				return "cod_dominio";
			}
		}
		if(field.equals(VistaVersamentoNonRendicontato.model().VRS_ID_TIPO_VERSAMENTO_DOMINIO.ID_TIPO_VERSAMENTO.COD_TIPO_VERSAMENTO)){
			if(appendTablePrefix){
				return this.toAliasTable(field)+".cod_tipo_versamento";
			}else{
				return "cod_tipo_versamento";
			}
		}
		if(field.equals(VistaVersamentoNonRendicontato.model().VRS_ID_TIPO_VERSAMENTO.COD_TIPO_VERSAMENTO)){
			if(appendTablePrefix){
				return this.toAliasTable(field)+".cod_tipo_versamento";
			}else{
				return "cod_tipo_versamento";
			}
		}
		if(field.equals(VistaVersamentoNonRendicontato.model().VRS_ID_DOMINIO.COD_DOMINIO)){
			if(appendTablePrefix){
				return this.toAliasTable(field)+".cod_dominio";
			}else{
				return "cod_dominio";
			}
		}
		if(field.equals(VistaVersamentoNonRendicontato.model().VRS_ID_UO.COD_UO)){
			if(appendTablePrefix){
				return this.toAliasTable(field)+".cod_uo";
			}else{
				return "cod_uo";
			}
		}
		if(field.equals(VistaVersamentoNonRendicontato.model().VRS_ID_UO.ID_DOMINIO.COD_DOMINIO)){
			if(appendTablePrefix){
				return this.toAliasTable(field)+".cod_dominio";
			}else{
				return "cod_dominio";
			}
		}
		if(field.equals(VistaVersamentoNonRendicontato.model().VRS_ID_APPLICAZIONE.COD_APPLICAZIONE)){
			if(appendTablePrefix){
				return this.toAliasTable(field)+".cod_applicazione";
			}else{
				return "cod_applicazione";
			}
		}
		if(field.equals(VistaVersamentoNonRendicontato.model().VRS_IMPORTO_TOTALE)){
			if(appendTablePrefix){
				return this.toAliasTable(field)+".vrs_importo_totale";
			}else{
				return "vrs_importo_totale";
			}
		}
		if(field.equals(VistaVersamentoNonRendicontato.model().VRS_STATO_VERSAMENTO)){
			if(appendTablePrefix){
				return this.toAliasTable(field)+".vrs_stato_versamento";
			}else{
				return "vrs_stato_versamento";
			}
		}
		if(field.equals(VistaVersamentoNonRendicontato.model().VRS_DESCRIZIONE_STATO)){
			if(appendTablePrefix){
				return this.toAliasTable(field)+".vrs_descrizione_stato";
			}else{
				return "vrs_descrizione_stato";
			}
		}
		if(field.equals(VistaVersamentoNonRendicontato.model().VRS_AGGIORNABILE)){
			if(appendTablePrefix){
				return this.toAliasTable(field)+".vrs_aggiornabile";
			}else{
				return "vrs_aggiornabile";
			}
		}
		if(field.equals(VistaVersamentoNonRendicontato.model().VRS_DATA_CREAZIONE)){
			if(appendTablePrefix){
				return this.toAliasTable(field)+".vrs_data_creazione";
			}else{
				return "vrs_data_creazione";
			}
		}
		if(field.equals(VistaVersamentoNonRendicontato.model().VRS_DATA_VALIDITA)){
			if(appendTablePrefix){
				return this.toAliasTable(field)+".vrs_data_validita";
			}else{
				return "vrs_data_validita";
			}
		}
		if(field.equals(VistaVersamentoNonRendicontato.model().VRS_DATA_SCADENZA)){
			if(appendTablePrefix){
				return this.toAliasTable(field)+".vrs_data_scadenza";
			}else{
				return "vrs_data_scadenza";
			}
		}
		if(field.equals(VistaVersamentoNonRendicontato.model().VRS_DATA_ORA_ULTIMO_AGG)){
			if(appendTablePrefix){
				return this.toAliasTable(field)+".vrs_data_ora_ultimo_agg";
			}else{
				return "vrs_data_ora_ultimo_agg";
			}
		}
		if(field.equals(VistaVersamentoNonRendicontato.model().VRS_CAUSALE_VERSAMENTO)){
			if(appendTablePrefix){
				return this.toAliasTable(field)+".vrs_causale_versamento";
			}else{
				return "vrs_causale_versamento";
			}
		}
		if(field.equals(VistaVersamentoNonRendicontato.model().VRS_DEBITORE_TIPO)){
			if(appendTablePrefix){
				return this.toAliasTable(field)+".vrs_debitore_tipo";
			}else{
				return "vrs_debitore_tipo";
			}
		}
		if(field.equals(VistaVersamentoNonRendicontato.model().VRS_DEBITORE_IDENTIFICATIVO)){
			if(appendTablePrefix){
				return this.toAliasTable(field)+".vrs_debitore_identificativo";
			}else{
				return "vrs_debitore_identificativo";
			}
		}
		if(field.equals(VistaVersamentoNonRendicontato.model().VRS_DEBITORE_ANAGRAFICA)){
			if(appendTablePrefix){
				return this.toAliasTable(field)+".vrs_debitore_anagrafica";
			}else{
				return "vrs_debitore_anagrafica";
			}
		}
		if(field.equals(VistaVersamentoNonRendicontato.model().VRS_DEBITORE_INDIRIZZO)){
			if(appendTablePrefix){
				return this.toAliasTable(field)+".vrs_debitore_indirizzo";
			}else{
				return "vrs_debitore_indirizzo";
			}
		}
		if(field.equals(VistaVersamentoNonRendicontato.model().VRS_DEBITORE_CIVICO)){
			if(appendTablePrefix){
				return this.toAliasTable(field)+".vrs_debitore_civico";
			}else{
				return "vrs_debitore_civico";
			}
		}
		if(field.equals(VistaVersamentoNonRendicontato.model().VRS_DEBITORE_CAP)){
			if(appendTablePrefix){
				return this.toAliasTable(field)+".vrs_debitore_cap";
			}else{
				return "vrs_debitore_cap";
			}
		}
		if(field.equals(VistaVersamentoNonRendicontato.model().VRS_DEBITORE_LOCALITA)){
			if(appendTablePrefix){
				return this.toAliasTable(field)+".vrs_debitore_localita";
			}else{
				return "vrs_debitore_localita";
			}
		}
		if(field.equals(VistaVersamentoNonRendicontato.model().VRS_DEBITORE_PROVINCIA)){
			if(appendTablePrefix){
				return this.toAliasTable(field)+".vrs_debitore_provincia";
			}else{
				return "vrs_debitore_provincia";
			}
		}
		if(field.equals(VistaVersamentoNonRendicontato.model().VRS_DEBITORE_NAZIONE)){
			if(appendTablePrefix){
				return this.toAliasTable(field)+".vrs_debitore_nazione";
			}else{
				return "vrs_debitore_nazione";
			}
		}
		if(field.equals(VistaVersamentoNonRendicontato.model().VRS_DEBITORE_EMAIL)){
			if(appendTablePrefix){
				return this.toAliasTable(field)+".vrs_debitore_email";
			}else{
				return "vrs_debitore_email";
			}
		}
		if(field.equals(VistaVersamentoNonRendicontato.model().VRS_DEBITORE_TELEFONO)){
			if(appendTablePrefix){
				return this.toAliasTable(field)+".vrs_debitore_telefono";
			}else{
				return "vrs_debitore_telefono";
			}
		}
		if(field.equals(VistaVersamentoNonRendicontato.model().VRS_DEBITORE_CELLULARE)){
			if(appendTablePrefix){
				return this.toAliasTable(field)+".vrs_debitore_cellulare";
			}else{
				return "vrs_debitore_cellulare";
			}
		}
		if(field.equals(VistaVersamentoNonRendicontato.model().VRS_DEBITORE_FAX)){
			if(appendTablePrefix){
				return this.toAliasTable(field)+".vrs_debitore_fax";
			}else{
				return "vrs_debitore_fax";
			}
		}
		if(field.equals(VistaVersamentoNonRendicontato.model().VRS_TASSONOMIA_AVVISO)){
			if(appendTablePrefix){
				return this.toAliasTable(field)+".vrs_tassonomia_avviso";
			}else{
				return "vrs_tassonomia_avviso";
			}
		}
		if(field.equals(VistaVersamentoNonRendicontato.model().VRS_TASSONOMIA)){
			if(appendTablePrefix){
				return this.toAliasTable(field)+".vrs_tassonomia";
			}else{
				return "vrs_tassonomia";
			}
		}
		if(field.equals(VistaVersamentoNonRendicontato.model().VRS_COD_LOTTO)){
			if(appendTablePrefix){
				return this.toAliasTable(field)+".vrs_cod_lotto";
			}else{
				return "vrs_cod_lotto";
			}
		}
		if(field.equals(VistaVersamentoNonRendicontato.model().VRS_COD_VERSAMENTO_LOTTO)){
			if(appendTablePrefix){
				return this.toAliasTable(field)+".vrs_cod_versamento_lotto";
			}else{
				return "vrs_cod_versamento_lotto";
			}
		}
		if(field.equals(VistaVersamentoNonRendicontato.model().VRS_COD_ANNO_TRIBUTARIO)){
			if(appendTablePrefix){
				return this.toAliasTable(field)+".vrs_cod_anno_tributario";
			}else{
				return "vrs_cod_anno_tributario";
			}
		}
		if(field.equals(VistaVersamentoNonRendicontato.model().VRS_COD_BUNDLEKEY)){
			if(appendTablePrefix){
				return this.toAliasTable(field)+".vrs_cod_bundlekey";
			}else{
				return "vrs_cod_bundlekey";
			}
		}
		if(field.equals(VistaVersamentoNonRendicontato.model().VRS_DATI_ALLEGATI)){
			if(appendTablePrefix){
				return this.toAliasTable(field)+".vrs_dati_allegati";
			}else{
				return "vrs_dati_allegati";
			}
		}
		if(field.equals(VistaVersamentoNonRendicontato.model().VRS_INCASSO)){
			if(appendTablePrefix){
				return this.toAliasTable(field)+".vrs_incasso";
			}else{
				return "vrs_incasso";
			}
		}
		if(field.equals(VistaVersamentoNonRendicontato.model().VRS_ANOMALIE)){
			if(appendTablePrefix){
				return this.toAliasTable(field)+".vrs_anomalie";
			}else{
				return "vrs_anomalie";
			}
		}
		if(field.equals(VistaVersamentoNonRendicontato.model().VRS_IUV_VERSAMENTO)){
			if(appendTablePrefix){
				return this.toAliasTable(field)+".vrs_iuv_versamento";
			}else{
				return "vrs_iuv_versamento";
			}
		}
		if(field.equals(VistaVersamentoNonRendicontato.model().VRS_NUMERO_AVVISO)){
			if(appendTablePrefix){
				return this.toAliasTable(field)+".vrs_numero_avviso";
			}else{
				return "vrs_numero_avviso";
			}
		}
		if(field.equals(VistaVersamentoNonRendicontato.model().VRS_ACK)){
			if(appendTablePrefix){
				return this.toAliasTable(field)+".vrs_ack";
			}else{
				return "vrs_ack";
			}
		}
		if(field.equals(VistaVersamentoNonRendicontato.model().VRS_ANOMALO)){
			if(appendTablePrefix){
				return this.toAliasTable(field)+".vrs_anomalo";
			}else{
				return "vrs_anomalo";
			}
		}
		if(field.equals(VistaVersamentoNonRendicontato.model().VRS_DIVISIONE)){
			if(appendTablePrefix){
				return this.toAliasTable(field)+".vrs_divisione";
			}else{
				return "vrs_divisione";
			}
		}
		if(field.equals(VistaVersamentoNonRendicontato.model().VRS_DIREZIONE)){
			if(appendTablePrefix){
				return this.toAliasTable(field)+".vrs_direzione";
			}else{
				return "vrs_direzione";
			}
		}
		if(field.equals(VistaVersamentoNonRendicontato.model().VRS_ID_SESSIONE)){
			if(appendTablePrefix){
				return this.toAliasTable(field)+".vrs_id_sessione";
			}else{
				return "vrs_id_sessione";
			}
		}
		if(field.equals(VistaVersamentoNonRendicontato.model().VRS_DATA_PAGAMENTO)){
			if(appendTablePrefix){
				return this.toAliasTable(field)+".vrs_data_pagamento";
			}else{
				return "vrs_data_pagamento";
			}
		}
		if(field.equals(VistaVersamentoNonRendicontato.model().VRS_IMPORTO_PAGATO)){
			if(appendTablePrefix){
				return this.toAliasTable(field)+".vrs_importo_pagato";
			}else{
				return "vrs_importo_pagato";
			}
		}
		if(field.equals(VistaVersamentoNonRendicontato.model().VRS_IMPORTO_INCASSATO)){
			if(appendTablePrefix){
				return this.toAliasTable(field)+".vrs_importo_incassato";
			}else{
				return "vrs_importo_incassato";
			}
		}
		if(field.equals(VistaVersamentoNonRendicontato.model().VRS_STATO_PAGAMENTO)){
			if(appendTablePrefix){
				return this.toAliasTable(field)+".vrs_stato_pagamento";
			}else{
				return "vrs_stato_pagamento";
			}
		}
		if(field.equals(VistaVersamentoNonRendicontato.model().VRS_IUV_PAGAMENTO)){
			if(appendTablePrefix){
				return this.toAliasTable(field)+".vrs_iuv_pagamento";
			}else{
				return "vrs_iuv_pagamento";
			}
		}
		if(field.equals(VistaVersamentoNonRendicontato.model().VRS_COD_RATA)){
			if(appendTablePrefix){
				return this.toAliasTable(field)+".vrs_cod_rata";
			}else{
				return "vrs_cod_rata";
			}
		}
		if(field.equals(VistaVersamentoNonRendicontato.model().VRS_ID_DOCUMENTO.COD_DOCUMENTO)){
			if(appendTablePrefix){
				return this.toAliasTable(field)+".cod_documento";
			}else{
				return "cod_documento";
			}
		}
		if(field.equals(VistaVersamentoNonRendicontato.model().VRS_ID_DOCUMENTO.ID_APPLICAZIONE.COD_APPLICAZIONE)){
			if(appendTablePrefix){
				return this.toAliasTable(field)+".cod_applicazione";
			}else{
				return "cod_applicazione";
			}
		}
		if(field.equals(VistaVersamentoNonRendicontato.model().VRS_ID_DOCUMENTO.ID_DOMINIO.COD_DOMINIO)){
			if(appendTablePrefix){
				return this.toAliasTable(field)+".cod_dominio";
			}else{
				return "cod_dominio";
			}
		}
		if(field.equals(VistaVersamentoNonRendicontato.model().VRS_TIPO)){
			if(appendTablePrefix){
				return this.toAliasTable(field)+".vrs_tipo";
			}else{
				return "vrs_tipo";
			}
		}
		if(field.equals(VistaVersamentoNonRendicontato.model().VRS_PROPRIETA)){
			if(appendTablePrefix){
				return this.toAliasTable(field)+".vrs_proprieta";
			}else{
				return "vrs_proprieta";
			}
		}
		if(field.equals(VistaVersamentoNonRendicontato.model().PAG_COD_DOMINIO)){
			if(appendTablePrefix){
				return this.toAliasTable(field)+".pag_cod_dominio";
			}else{
				return "pag_cod_dominio";
			}
		}
		if(field.equals(VistaVersamentoNonRendicontato.model().PAG_IUV)){
			if(appendTablePrefix){
				return this.toAliasTable(field)+".pag_iuv";
			}else{
				return "pag_iuv";
			}
		}
		if(field.equals(VistaVersamentoNonRendicontato.model().PAG_INDICE_DATI)){
			if(appendTablePrefix){
				return this.toAliasTable(field)+".pag_indice_dati";
			}else{
				return "pag_indice_dati";
			}
		}
		if(field.equals(VistaVersamentoNonRendicontato.model().PAG_IMPORTO_PAGATO)){
			if(appendTablePrefix){
				return this.toAliasTable(field)+".pag_importo_pagato";
			}else{
				return "pag_importo_pagato";
			}
		}
		if(field.equals(VistaVersamentoNonRendicontato.model().PAG_DATA_ACQUISIZIONE)){
			if(appendTablePrefix){
				return this.toAliasTable(field)+".pag_data_acquisizione";
			}else{
				return "pag_data_acquisizione";
			}
		}
		if(field.equals(VistaVersamentoNonRendicontato.model().PAG_IUR)){
			if(appendTablePrefix){
				return this.toAliasTable(field)+".pag_iur";
			}else{
				return "pag_iur";
			}
		}
		if(field.equals(VistaVersamentoNonRendicontato.model().PAG_DATA_PAGAMENTO)){
			if(appendTablePrefix){
				return this.toAliasTable(field)+".pag_data_pagamento";
			}else{
				return "pag_data_pagamento";
			}
		}
		if(field.equals(VistaVersamentoNonRendicontato.model().PAG_COMMISSIONI_PSP)){
			if(appendTablePrefix){
				return this.toAliasTable(field)+".pag_commissioni_psp";
			}else{
				return "pag_commissioni_psp";
			}
		}
		if(field.equals(VistaVersamentoNonRendicontato.model().PAG_TIPO_ALLEGATO)){
			if(appendTablePrefix){
				return this.toAliasTable(field)+".pag_tipo_allegato";
			}else{
				return "pag_tipo_allegato";
			}
		}
		if(field.equals(VistaVersamentoNonRendicontato.model().PAG_ALLEGATO)){
			if(appendTablePrefix){
				return this.toAliasTable(field)+".pag_allegato";
			}else{
				return "pag_allegato";
			}
		}
		if(field.equals(VistaVersamentoNonRendicontato.model().PAG_DATA_ACQUISIZIONE_REVOCA)){
			if(appendTablePrefix){
				return this.toAliasTable(field)+".pag_data_acquisizione_revoca";
			}else{
				return "pag_data_acquisizione_revoca";
			}
		}
		if(field.equals(VistaVersamentoNonRendicontato.model().PAG_CAUSALE_REVOCA)){
			if(appendTablePrefix){
				return this.toAliasTable(field)+".pag_causale_revoca";
			}else{
				return "pag_causale_revoca";
			}
		}
		if(field.equals(VistaVersamentoNonRendicontato.model().PAG_DATI_REVOCA)){
			if(appendTablePrefix){
				return this.toAliasTable(field)+".pag_dati_revoca";
			}else{
				return "pag_dati_revoca";
			}
		}
		if(field.equals(VistaVersamentoNonRendicontato.model().PAG_IMPORTO_REVOCATO)){
			if(appendTablePrefix){
				return this.toAliasTable(field)+".pag_importo_revocato";
			}else{
				return "pag_importo_revocato";
			}
		}
		if(field.equals(VistaVersamentoNonRendicontato.model().PAG_ESITO_REVOCA)){
			if(appendTablePrefix){
				return this.toAliasTable(field)+".pag_esito_revoca";
			}else{
				return "pag_esito_revoca";
			}
		}
		if(field.equals(VistaVersamentoNonRendicontato.model().PAG_DATI_ESITO_REVOCA)){
			if(appendTablePrefix){
				return this.toAliasTable(field)+".pag_dati_esito_revoca";
			}else{
				return "pag_dati_esito_revoca";
			}
		}
		if(field.equals(VistaVersamentoNonRendicontato.model().PAG_STATO)){
			if(appendTablePrefix){
				return this.toAliasTable(field)+".pag_stato";
			}else{
				return "pag_stato";
			}
		}
		if(field.equals(VistaVersamentoNonRendicontato.model().PAG_TIPO)){
			if(appendTablePrefix){
				return this.toAliasTable(field)+".pag_tipo";
			}else{
				return "pag_tipo";
			}
		}
		if(field.equals(VistaVersamentoNonRendicontato.model().PAG_ID_INCASSO.IDENTIFICATIVO)){
			if(appendTablePrefix){
				return this.toAliasTable(field)+".identificativo";
			}else{
				return "identificativo";
			}
		}
		if(field.equals(VistaVersamentoNonRendicontato.model().PAG_ID_INCASSO.COD_DOMINIO)){
			if(appendTablePrefix){
				return this.toAliasTable(field)+".cod_dominio";
			}else{
				return "cod_dominio";
			}
		}
		if(field.equals(VistaVersamentoNonRendicontato.model().RPT_IUV)){
			if(appendTablePrefix){
				return this.toAliasTable(field)+".rpt_iuv";
			}else{
				return "rpt_iuv";
			}
		}
		if(field.equals(VistaVersamentoNonRendicontato.model().RPT_CCP)){
			if(appendTablePrefix){
				return this.toAliasTable(field)+".rpt_ccp";
			}else{
				return "rpt_ccp";
			}
		}
		if(field.equals(VistaVersamentoNonRendicontato.model().RNC_TRN)){
			if(appendTablePrefix){
				return this.toAliasTable(field)+".rnc_trn";
			}else{
				return "rnc_trn";
			}
		}


		return super.toColumn(field,returnAlias,appendTablePrefix);
		
	}
	
	@Override
	public String toTable(IField field,boolean returnAlias) throws ExpressionException {
		
		// In the case of table with alias, using parameter returnAlias​​, 
		// it is possible to drive the choice whether to return only the alias or 
		// the full definition of the table containing the alias
		
		if(field.equals(VistaVersamentoNonRendicontato.model().SNG_ID_TRIBUTO.ID_DOMINIO.COD_DOMINIO)){
			return this.toTable(VistaVersamentoNonRendicontato.model().SNG_ID_TRIBUTO.ID_DOMINIO, returnAlias);
		}
		if(field.equals(VistaVersamentoNonRendicontato.model().SNG_ID_TRIBUTO.ID_TIPO_TRIBUTO.COD_TRIBUTO)){
			return this.toTable(VistaVersamentoNonRendicontato.model().SNG_ID_TRIBUTO.ID_TIPO_TRIBUTO, returnAlias);
		}
		if(field.equals(VistaVersamentoNonRendicontato.model().SNG_COD_SING_VERS_ENTE)){
			return this.toTable(VistaVersamentoNonRendicontato.model(), returnAlias);
		}
		if(field.equals(VistaVersamentoNonRendicontato.model().SNG_STATO_SINGOLO_VERSAMENTO)){
			return this.toTable(VistaVersamentoNonRendicontato.model(), returnAlias);
		}
		if(field.equals(VistaVersamentoNonRendicontato.model().SNG_IMPORTO_SINGOLO_VERSAMENTO)){
			return this.toTable(VistaVersamentoNonRendicontato.model(), returnAlias);
		}
		if(field.equals(VistaVersamentoNonRendicontato.model().SNG_DESCRIZIONE)){
			return this.toTable(VistaVersamentoNonRendicontato.model(), returnAlias);
		}
		if(field.equals(VistaVersamentoNonRendicontato.model().SNG_DATI_ALLEGATI)){
			return this.toTable(VistaVersamentoNonRendicontato.model(), returnAlias);
		}
		if(field.equals(VistaVersamentoNonRendicontato.model().SNG_INDICE_DATI)){
			return this.toTable(VistaVersamentoNonRendicontato.model(), returnAlias);
		}
		if(field.equals(VistaVersamentoNonRendicontato.model().SNG_DESCRIZIONE_CAUSALE_RPT)){
			return this.toTable(VistaVersamentoNonRendicontato.model(), returnAlias);
		}
		if(field.equals(VistaVersamentoNonRendicontato.model().SNG_CONTABILITA)){
			return this.toTable(VistaVersamentoNonRendicontato.model(), returnAlias);
		}
		if(field.equals(VistaVersamentoNonRendicontato.model().VRS_ID)){
			return this.toTable(VistaVersamentoNonRendicontato.model(), returnAlias);
		}
		if(field.equals(VistaVersamentoNonRendicontato.model().VRS_COD_VERSAMENTO_ENTE)){
			return this.toTable(VistaVersamentoNonRendicontato.model(), returnAlias);
		}
		if(field.equals(VistaVersamentoNonRendicontato.model().VRS_NOME)){
			return this.toTable(VistaVersamentoNonRendicontato.model(), returnAlias);
		}
		if(field.equals(VistaVersamentoNonRendicontato.model().VRS_ID_TIPO_VERSAMENTO_DOMINIO.ID_DOMINIO.COD_DOMINIO)){
			return this.toTable(VistaVersamentoNonRendicontato.model().VRS_ID_TIPO_VERSAMENTO_DOMINIO.ID_DOMINIO, returnAlias);
		}
		if(field.equals(VistaVersamentoNonRendicontato.model().VRS_ID_TIPO_VERSAMENTO_DOMINIO.ID_TIPO_VERSAMENTO.COD_TIPO_VERSAMENTO)){
			return this.toTable(VistaVersamentoNonRendicontato.model().VRS_ID_TIPO_VERSAMENTO_DOMINIO.ID_TIPO_VERSAMENTO, returnAlias);
		}
		if(field.equals(VistaVersamentoNonRendicontato.model().VRS_ID_TIPO_VERSAMENTO.COD_TIPO_VERSAMENTO)){
			return this.toTable(VistaVersamentoNonRendicontato.model().VRS_ID_TIPO_VERSAMENTO, returnAlias);
		}
		if(field.equals(VistaVersamentoNonRendicontato.model().VRS_ID_DOMINIO.COD_DOMINIO)){
			return this.toTable(VistaVersamentoNonRendicontato.model().VRS_ID_DOMINIO, returnAlias);
		}
		if(field.equals(VistaVersamentoNonRendicontato.model().VRS_ID_UO.COD_UO)){
			return this.toTable(VistaVersamentoNonRendicontato.model().VRS_ID_UO, returnAlias);
		}
		if(field.equals(VistaVersamentoNonRendicontato.model().VRS_ID_UO.ID_DOMINIO.COD_DOMINIO)){
			return this.toTable(VistaVersamentoNonRendicontato.model().VRS_ID_UO.ID_DOMINIO, returnAlias);
		}
		if(field.equals(VistaVersamentoNonRendicontato.model().VRS_ID_APPLICAZIONE.COD_APPLICAZIONE)){
			return this.toTable(VistaVersamentoNonRendicontato.model().VRS_ID_APPLICAZIONE, returnAlias);
		}
		if(field.equals(VistaVersamentoNonRendicontato.model().VRS_IMPORTO_TOTALE)){
			return this.toTable(VistaVersamentoNonRendicontato.model(), returnAlias);
		}
		if(field.equals(VistaVersamentoNonRendicontato.model().VRS_STATO_VERSAMENTO)){
			return this.toTable(VistaVersamentoNonRendicontato.model(), returnAlias);
		}
		if(field.equals(VistaVersamentoNonRendicontato.model().VRS_DESCRIZIONE_STATO)){
			return this.toTable(VistaVersamentoNonRendicontato.model(), returnAlias);
		}
		if(field.equals(VistaVersamentoNonRendicontato.model().VRS_AGGIORNABILE)){
			return this.toTable(VistaVersamentoNonRendicontato.model(), returnAlias);
		}
		if(field.equals(VistaVersamentoNonRendicontato.model().VRS_DATA_CREAZIONE)){
			return this.toTable(VistaVersamentoNonRendicontato.model(), returnAlias);
		}
		if(field.equals(VistaVersamentoNonRendicontato.model().VRS_DATA_VALIDITA)){
			return this.toTable(VistaVersamentoNonRendicontato.model(), returnAlias);
		}
		if(field.equals(VistaVersamentoNonRendicontato.model().VRS_DATA_SCADENZA)){
			return this.toTable(VistaVersamentoNonRendicontato.model(), returnAlias);
		}
		if(field.equals(VistaVersamentoNonRendicontato.model().VRS_DATA_ORA_ULTIMO_AGG)){
			return this.toTable(VistaVersamentoNonRendicontato.model(), returnAlias);
		}
		if(field.equals(VistaVersamentoNonRendicontato.model().VRS_CAUSALE_VERSAMENTO)){
			return this.toTable(VistaVersamentoNonRendicontato.model(), returnAlias);
		}
		if(field.equals(VistaVersamentoNonRendicontato.model().VRS_DEBITORE_TIPO)){
			return this.toTable(VistaVersamentoNonRendicontato.model(), returnAlias);
		}
		if(field.equals(VistaVersamentoNonRendicontato.model().VRS_DEBITORE_IDENTIFICATIVO)){
			return this.toTable(VistaVersamentoNonRendicontato.model(), returnAlias);
		}
		if(field.equals(VistaVersamentoNonRendicontato.model().VRS_DEBITORE_ANAGRAFICA)){
			return this.toTable(VistaVersamentoNonRendicontato.model(), returnAlias);
		}
		if(field.equals(VistaVersamentoNonRendicontato.model().VRS_DEBITORE_INDIRIZZO)){
			return this.toTable(VistaVersamentoNonRendicontato.model(), returnAlias);
		}
		if(field.equals(VistaVersamentoNonRendicontato.model().VRS_DEBITORE_CIVICO)){
			return this.toTable(VistaVersamentoNonRendicontato.model(), returnAlias);
		}
		if(field.equals(VistaVersamentoNonRendicontato.model().VRS_DEBITORE_CAP)){
			return this.toTable(VistaVersamentoNonRendicontato.model(), returnAlias);
		}
		if(field.equals(VistaVersamentoNonRendicontato.model().VRS_DEBITORE_LOCALITA)){
			return this.toTable(VistaVersamentoNonRendicontato.model(), returnAlias);
		}
		if(field.equals(VistaVersamentoNonRendicontato.model().VRS_DEBITORE_PROVINCIA)){
			return this.toTable(VistaVersamentoNonRendicontato.model(), returnAlias);
		}
		if(field.equals(VistaVersamentoNonRendicontato.model().VRS_DEBITORE_NAZIONE)){
			return this.toTable(VistaVersamentoNonRendicontato.model(), returnAlias);
		}
		if(field.equals(VistaVersamentoNonRendicontato.model().VRS_DEBITORE_EMAIL)){
			return this.toTable(VistaVersamentoNonRendicontato.model(), returnAlias);
		}
		if(field.equals(VistaVersamentoNonRendicontato.model().VRS_DEBITORE_TELEFONO)){
			return this.toTable(VistaVersamentoNonRendicontato.model(), returnAlias);
		}
		if(field.equals(VistaVersamentoNonRendicontato.model().VRS_DEBITORE_CELLULARE)){
			return this.toTable(VistaVersamentoNonRendicontato.model(), returnAlias);
		}
		if(field.equals(VistaVersamentoNonRendicontato.model().VRS_DEBITORE_FAX)){
			return this.toTable(VistaVersamentoNonRendicontato.model(), returnAlias);
		}
		if(field.equals(VistaVersamentoNonRendicontato.model().VRS_TASSONOMIA_AVVISO)){
			return this.toTable(VistaVersamentoNonRendicontato.model(), returnAlias);
		}
		if(field.equals(VistaVersamentoNonRendicontato.model().VRS_TASSONOMIA)){
			return this.toTable(VistaVersamentoNonRendicontato.model(), returnAlias);
		}
		if(field.equals(VistaVersamentoNonRendicontato.model().VRS_COD_LOTTO)){
			return this.toTable(VistaVersamentoNonRendicontato.model(), returnAlias);
		}
		if(field.equals(VistaVersamentoNonRendicontato.model().VRS_COD_VERSAMENTO_LOTTO)){
			return this.toTable(VistaVersamentoNonRendicontato.model(), returnAlias);
		}
		if(field.equals(VistaVersamentoNonRendicontato.model().VRS_COD_ANNO_TRIBUTARIO)){
			return this.toTable(VistaVersamentoNonRendicontato.model(), returnAlias);
		}
		if(field.equals(VistaVersamentoNonRendicontato.model().VRS_COD_BUNDLEKEY)){
			return this.toTable(VistaVersamentoNonRendicontato.model(), returnAlias);
		}
		if(field.equals(VistaVersamentoNonRendicontato.model().VRS_DATI_ALLEGATI)){
			return this.toTable(VistaVersamentoNonRendicontato.model(), returnAlias);
		}
		if(field.equals(VistaVersamentoNonRendicontato.model().VRS_INCASSO)){
			return this.toTable(VistaVersamentoNonRendicontato.model(), returnAlias);
		}
		if(field.equals(VistaVersamentoNonRendicontato.model().VRS_ANOMALIE)){
			return this.toTable(VistaVersamentoNonRendicontato.model(), returnAlias);
		}
		if(field.equals(VistaVersamentoNonRendicontato.model().VRS_IUV_VERSAMENTO)){
			return this.toTable(VistaVersamentoNonRendicontato.model(), returnAlias);
		}
		if(field.equals(VistaVersamentoNonRendicontato.model().VRS_NUMERO_AVVISO)){
			return this.toTable(VistaVersamentoNonRendicontato.model(), returnAlias);
		}
		if(field.equals(VistaVersamentoNonRendicontato.model().VRS_ACK)){
			return this.toTable(VistaVersamentoNonRendicontato.model(), returnAlias);
		}
		if(field.equals(VistaVersamentoNonRendicontato.model().VRS_ANOMALO)){
			return this.toTable(VistaVersamentoNonRendicontato.model(), returnAlias);
		}
		if(field.equals(VistaVersamentoNonRendicontato.model().VRS_DIVISIONE)){
			return this.toTable(VistaVersamentoNonRendicontato.model(), returnAlias);
		}
		if(field.equals(VistaVersamentoNonRendicontato.model().VRS_DIREZIONE)){
			return this.toTable(VistaVersamentoNonRendicontato.model(), returnAlias);
		}
		if(field.equals(VistaVersamentoNonRendicontato.model().VRS_ID_SESSIONE)){
			return this.toTable(VistaVersamentoNonRendicontato.model(), returnAlias);
		}
		if(field.equals(VistaVersamentoNonRendicontato.model().VRS_DATA_PAGAMENTO)){
			return this.toTable(VistaVersamentoNonRendicontato.model(), returnAlias);
		}
		if(field.equals(VistaVersamentoNonRendicontato.model().VRS_IMPORTO_PAGATO)){
			return this.toTable(VistaVersamentoNonRendicontato.model(), returnAlias);
		}
		if(field.equals(VistaVersamentoNonRendicontato.model().VRS_IMPORTO_INCASSATO)){
			return this.toTable(VistaVersamentoNonRendicontato.model(), returnAlias);
		}
		if(field.equals(VistaVersamentoNonRendicontato.model().VRS_STATO_PAGAMENTO)){
			return this.toTable(VistaVersamentoNonRendicontato.model(), returnAlias);
		}
		if(field.equals(VistaVersamentoNonRendicontato.model().VRS_IUV_PAGAMENTO)){
			return this.toTable(VistaVersamentoNonRendicontato.model(), returnAlias);
		}
		if(field.equals(VistaVersamentoNonRendicontato.model().VRS_COD_RATA)){
			return this.toTable(VistaVersamentoNonRendicontato.model(), returnAlias);
		}
		if(field.equals(VistaVersamentoNonRendicontato.model().VRS_ID_DOCUMENTO.COD_DOCUMENTO)){
			return this.toTable(VistaVersamentoNonRendicontato.model().VRS_ID_DOCUMENTO, returnAlias);
		}
		if(field.equals(VistaVersamentoNonRendicontato.model().VRS_ID_DOCUMENTO.ID_APPLICAZIONE.COD_APPLICAZIONE)){
			return this.toTable(VistaVersamentoNonRendicontato.model().VRS_ID_DOCUMENTO.ID_APPLICAZIONE, returnAlias);
		}
		if(field.equals(VistaVersamentoNonRendicontato.model().VRS_ID_DOCUMENTO.ID_DOMINIO.COD_DOMINIO)){
			return this.toTable(VistaVersamentoNonRendicontato.model().VRS_ID_DOCUMENTO.ID_DOMINIO, returnAlias);
		}
		if(field.equals(VistaVersamentoNonRendicontato.model().VRS_TIPO)){
			return this.toTable(VistaVersamentoNonRendicontato.model(), returnAlias);
		}
		if(field.equals(VistaVersamentoNonRendicontato.model().VRS_PROPRIETA)){
			return this.toTable(VistaVersamentoNonRendicontato.model(), returnAlias);
		}
		if(field.equals(VistaVersamentoNonRendicontato.model().PAG_COD_DOMINIO)){
			return this.toTable(VistaVersamentoNonRendicontato.model(), returnAlias);
		}
		if(field.equals(VistaVersamentoNonRendicontato.model().PAG_IUV)){
			return this.toTable(VistaVersamentoNonRendicontato.model(), returnAlias);
		}
		if(field.equals(VistaVersamentoNonRendicontato.model().PAG_INDICE_DATI)){
			return this.toTable(VistaVersamentoNonRendicontato.model(), returnAlias);
		}
		if(field.equals(VistaVersamentoNonRendicontato.model().PAG_IMPORTO_PAGATO)){
			return this.toTable(VistaVersamentoNonRendicontato.model(), returnAlias);
		}
		if(field.equals(VistaVersamentoNonRendicontato.model().PAG_DATA_ACQUISIZIONE)){
			return this.toTable(VistaVersamentoNonRendicontato.model(), returnAlias);
		}
		if(field.equals(VistaVersamentoNonRendicontato.model().PAG_IUR)){
			return this.toTable(VistaVersamentoNonRendicontato.model(), returnAlias);
		}
		if(field.equals(VistaVersamentoNonRendicontato.model().PAG_DATA_PAGAMENTO)){
			return this.toTable(VistaVersamentoNonRendicontato.model(), returnAlias);
		}
		if(field.equals(VistaVersamentoNonRendicontato.model().PAG_COMMISSIONI_PSP)){
			return this.toTable(VistaVersamentoNonRendicontato.model(), returnAlias);
		}
		if(field.equals(VistaVersamentoNonRendicontato.model().PAG_TIPO_ALLEGATO)){
			return this.toTable(VistaVersamentoNonRendicontato.model(), returnAlias);
		}
		if(field.equals(VistaVersamentoNonRendicontato.model().PAG_ALLEGATO)){
			return this.toTable(VistaVersamentoNonRendicontato.model(), returnAlias);
		}
		if(field.equals(VistaVersamentoNonRendicontato.model().PAG_DATA_ACQUISIZIONE_REVOCA)){
			return this.toTable(VistaVersamentoNonRendicontato.model(), returnAlias);
		}
		if(field.equals(VistaVersamentoNonRendicontato.model().PAG_CAUSALE_REVOCA)){
			return this.toTable(VistaVersamentoNonRendicontato.model(), returnAlias);
		}
		if(field.equals(VistaVersamentoNonRendicontato.model().PAG_DATI_REVOCA)){
			return this.toTable(VistaVersamentoNonRendicontato.model(), returnAlias);
		}
		if(field.equals(VistaVersamentoNonRendicontato.model().PAG_IMPORTO_REVOCATO)){
			return this.toTable(VistaVersamentoNonRendicontato.model(), returnAlias);
		}
		if(field.equals(VistaVersamentoNonRendicontato.model().PAG_ESITO_REVOCA)){
			return this.toTable(VistaVersamentoNonRendicontato.model(), returnAlias);
		}
		if(field.equals(VistaVersamentoNonRendicontato.model().PAG_DATI_ESITO_REVOCA)){
			return this.toTable(VistaVersamentoNonRendicontato.model(), returnAlias);
		}
		if(field.equals(VistaVersamentoNonRendicontato.model().PAG_STATO)){
			return this.toTable(VistaVersamentoNonRendicontato.model(), returnAlias);
		}
		if(field.equals(VistaVersamentoNonRendicontato.model().PAG_TIPO)){
			return this.toTable(VistaVersamentoNonRendicontato.model(), returnAlias);
		}
		if(field.equals(VistaVersamentoNonRendicontato.model().PAG_ID_INCASSO.IDENTIFICATIVO)){
			return this.toTable(VistaVersamentoNonRendicontato.model().PAG_ID_INCASSO, returnAlias);
		}
		if(field.equals(VistaVersamentoNonRendicontato.model().PAG_ID_INCASSO.COD_DOMINIO)){
			return this.toTable(VistaVersamentoNonRendicontato.model().PAG_ID_INCASSO, returnAlias);
		}
		if(field.equals(VistaVersamentoNonRendicontato.model().RPT_IUV)){
			return this.toTable(VistaVersamentoNonRendicontato.model(), returnAlias);
		}
		if(field.equals(VistaVersamentoNonRendicontato.model().RPT_CCP)){
			return this.toTable(VistaVersamentoNonRendicontato.model(), returnAlias);
		}
		if(field.equals(VistaVersamentoNonRendicontato.model().RNC_TRN)){
			return this.toTable(VistaVersamentoNonRendicontato.model(), returnAlias);
		}


		return super.toTable(field,returnAlias);
		
	}

	@Override
	public String toTable(IModel<?> model,boolean returnAlias) throws ExpressionException {
		
		// In the case of table with alias, using parameter returnAlias​​, 
		// it is possible to drive the choice whether to return only the alias or 
		// the full definition of the table containing the alias
		
		if(model.equals(VistaVersamentoNonRendicontato.model())){
			return "v_vrs_non_rnd";
		}
		if(model.equals(VistaVersamentoNonRendicontato.model().SNG_ID_TRIBUTO)){
			return "tributi";
		}
		if(model.equals(VistaVersamentoNonRendicontato.model().SNG_ID_TRIBUTO.ID_DOMINIO)){
			return "domini";
		}
		if(model.equals(VistaVersamentoNonRendicontato.model().SNG_ID_TRIBUTO.ID_TIPO_TRIBUTO)){
			return "tipi_tributo";
		}
		if(model.equals(VistaVersamentoNonRendicontato.model().VRS_ID_TIPO_VERSAMENTO_DOMINIO)){
			return "tipi_vers_domini";
		}
		if(model.equals(VistaVersamentoNonRendicontato.model().VRS_ID_TIPO_VERSAMENTO_DOMINIO.ID_DOMINIO)){
			return "domini";
		}
		if(model.equals(VistaVersamentoNonRendicontato.model().VRS_ID_TIPO_VERSAMENTO_DOMINIO.ID_TIPO_VERSAMENTO)){
			return "tipi_versamento";
		}
		if(model.equals(VistaVersamentoNonRendicontato.model().VRS_ID_TIPO_VERSAMENTO)){
			return "tipi_versamento";
		}
		if(model.equals(VistaVersamentoNonRendicontato.model().VRS_ID_DOMINIO)){
			return "domini";
		}
		if(model.equals(VistaVersamentoNonRendicontato.model().VRS_ID_UO)){
			return "uo";
		}
		if(model.equals(VistaVersamentoNonRendicontato.model().VRS_ID_UO.ID_DOMINIO)){
			return "domini";
		}
		if(model.equals(VistaVersamentoNonRendicontato.model().VRS_ID_APPLICAZIONE)){
			return "applicazioni";
		}
		if(model.equals(VistaVersamentoNonRendicontato.model().VRS_ID_DOCUMENTO)){
			return "documenti";
		}
		if(model.equals(VistaVersamentoNonRendicontato.model().VRS_ID_DOCUMENTO.ID_APPLICAZIONE)){
			return "applicazioni";
		}
		if(model.equals(VistaVersamentoNonRendicontato.model().VRS_ID_DOCUMENTO.ID_DOMINIO)){
			return "domini";
		}
		if(model.equals(VistaVersamentoNonRendicontato.model().PAG_ID_INCASSO)){
			return "incassi";
		}


		return super.toTable(model,returnAlias);
		
	}

}
