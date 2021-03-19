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

import it.govpay.orm.VistaRendicontazione;


/**     
 * VistaRendicontazioneFieldConverter
 *
 * @author Giovanni Bussu (bussu@link.it)
 * @author Lorenzo Nardi (nardi@link.it)
 * @author $Author$
 * @version $Rev$, $Date$
 */
public class VistaRendicontazioneFieldConverter extends AbstractSQLFieldConverter {

	private TipiDatabase databaseType;
	
	public VistaRendicontazioneFieldConverter(String databaseType){
		this.databaseType = TipiDatabase.toEnumConstant(databaseType);
	}
	public VistaRendicontazioneFieldConverter(TipiDatabase databaseType){
		this.databaseType = databaseType;
	}


	@Override
	public IModel<?> getRootModel() throws ExpressionException {
		return VistaRendicontazione.model();
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
		
		if(field.equals(VistaRendicontazione.model().FR_COD_PSP)){
			if(appendTablePrefix){
				return this.toAliasTable(field)+".fr_cod_psp";
			}else{
				return "fr_cod_psp";
			}
		}
		if(field.equals(VistaRendicontazione.model().FR_COD_DOMINIO)){
			if(appendTablePrefix){
				return this.toAliasTable(field)+".fr_cod_dominio";
			}else{
				return "fr_cod_dominio";
			}
		}
		if(field.equals(VistaRendicontazione.model().FR_COD_FLUSSO)){
			if(appendTablePrefix){
				return this.toAliasTable(field)+".fr_cod_flusso";
			}else{
				return "fr_cod_flusso";
			}
		}
		if(field.equals(VistaRendicontazione.model().FR_STATO)){
			if(appendTablePrefix){
				return this.toAliasTable(field)+".fr_stato";
			}else{
				return "fr_stato";
			}
		}
		if(field.equals(VistaRendicontazione.model().FR_DESCRIZIONE_STATO)){
			if(appendTablePrefix){
				return this.toAliasTable(field)+".fr_descrizione_stato";
			}else{
				return "fr_descrizione_stato";
			}
		}
		if(field.equals(VistaRendicontazione.model().FR_IUR)){
			if(appendTablePrefix){
				return this.toAliasTable(field)+".fr_iur";
			}else{
				return "fr_iur";
			}
		}
		if(field.equals(VistaRendicontazione.model().FR_DATA_ORA_FLUSSO)){
			if(appendTablePrefix){
				return this.toAliasTable(field)+".fr_data_ora_flusso";
			}else{
				return "fr_data_ora_flusso";
			}
		}
		if(field.equals(VistaRendicontazione.model().FR_DATA_REGOLAMENTO)){
			if(appendTablePrefix){
				return this.toAliasTable(field)+".fr_data_regolamento";
			}else{
				return "fr_data_regolamento";
			}
		}
		if(field.equals(VistaRendicontazione.model().FR_DATA_ACQUISIZIONE)){
			if(appendTablePrefix){
				return this.toAliasTable(field)+".fr_data_acquisizione";
			}else{
				return "fr_data_acquisizione";
			}
		}
		if(field.equals(VistaRendicontazione.model().FR_NUMERO_PAGAMENTI)){
			if(appendTablePrefix){
				return this.toAliasTable(field)+".fr_numero_pagamenti";
			}else{
				return "fr_numero_pagamenti";
			}
		}
		if(field.equals(VistaRendicontazione.model().FR_IMPORTO_TOTALE_PAGAMENTI)){
			if(appendTablePrefix){
				return this.toAliasTable(field)+".fr_importo_totale_pagamenti";
			}else{
				return "fr_importo_totale_pagamenti";
			}
		}
		if(field.equals(VistaRendicontazione.model().FR_COD_BIC_RIVERSAMENTO)){
			if(appendTablePrefix){
				return this.toAliasTable(field)+".fr_cod_bic_riversamento";
			}else{
				return "fr_cod_bic_riversamento";
			}
		}
		if(field.equals(VistaRendicontazione.model().FR_ID)){
			if(appendTablePrefix){
				return this.toAliasTable(field)+".fr_id";
			}else{
				return "fr_id";
			}
		}
		if(field.equals(VistaRendicontazione.model().FR_ID_INCASSO.TRN)){
			if(appendTablePrefix){
				return this.toAliasTable(field)+".trn";
			}else{
				return "trn";
			}
		}
		if(field.equals(VistaRendicontazione.model().FR_ID_INCASSO.COD_DOMINIO)){
			if(appendTablePrefix){
				return this.toAliasTable(field)+".cod_dominio";
			}else{
				return "cod_dominio";
			}
		}
		if(field.equals(VistaRendicontazione.model().FR_RAGIONE_SOCIALE_PSP)){
			if(appendTablePrefix){
				return this.toAliasTable(field)+".fr_ragione_sociale_psp";
			}else{
				return "fr_ragione_sociale_psp";
			}
		}
		if(field.equals(VistaRendicontazione.model().FR_RAGIONE_SOCIALE_DOMINIO)){
			if(appendTablePrefix){
				return this.toAliasTable(field)+".fr_ragione_sociale_dominio";
			}else{
				return "fr_ragione_sociale_dominio";
			}
		}
		if(field.equals(VistaRendicontazione.model().FR_OBSOLETO)){
			if(appendTablePrefix){
				return this.toAliasTable(field)+".fr_obsoleto";
			}else{
				return "fr_obsoleto";
			}
		}
		if(field.equals(VistaRendicontazione.model().RND_IUV)){
			if(appendTablePrefix){
				return this.toAliasTable(field)+".rnd_iuv";
			}else{
				return "rnd_iuv";
			}
		}
		if(field.equals(VistaRendicontazione.model().RND_IUR)){
			if(appendTablePrefix){
				return this.toAliasTable(field)+".rnd_iur";
			}else{
				return "rnd_iur";
			}
		}
		if(field.equals(VistaRendicontazione.model().RND_INDICE_DATI)){
			if(appendTablePrefix){
				return this.toAliasTable(field)+".rnd_indice_dati";
			}else{
				return "rnd_indice_dati";
			}
		}
		if(field.equals(VistaRendicontazione.model().RND_IMPORTO_PAGATO)){
			if(appendTablePrefix){
				return this.toAliasTable(field)+".rnd_importo_pagato";
			}else{
				return "rnd_importo_pagato";
			}
		}
		if(field.equals(VistaRendicontazione.model().RND_ESITO)){
			if(appendTablePrefix){
				return this.toAliasTable(field)+".rnd_esito";
			}else{
				return "rnd_esito";
			}
		}
		if(field.equals(VistaRendicontazione.model().RND_DATA)){
			if(appendTablePrefix){
				return this.toAliasTable(field)+".rnd_data";
			}else{
				return "rnd_data";
			}
		}
		if(field.equals(VistaRendicontazione.model().RND_STATO)){
			if(appendTablePrefix){
				return this.toAliasTable(field)+".rnd_stato";
			}else{
				return "rnd_stato";
			}
		}
		if(field.equals(VistaRendicontazione.model().RND_ANOMALIE)){
			if(appendTablePrefix){
				return this.toAliasTable(field)+".rnd_anomalie";
			}else{
				return "rnd_anomalie";
			}
		}
		if(field.equals(VistaRendicontazione.model().RND_ID_PAGAMENTO.ID_PAGAMENTO)){
			if(appendTablePrefix){
				return this.toAliasTable(field)+".id_pagamento";
			}else{
				return "id_pagamento";
			}
		}
		if(field.equals(VistaRendicontazione.model().RND_ID_PAGAMENTO.INDICE_DATI)){
			if(appendTablePrefix){
				return this.toAliasTable(field)+".indice_dati";
			}else{
				return "indice_dati";
			}
		}
		if(field.equals(VistaRendicontazione.model().RND_ID_PAGAMENTO.IUV)){
			if(appendTablePrefix){
				return this.toAliasTable(field)+".iuv";
			}else{
				return "iuv";
			}
		}
		if(field.equals(VistaRendicontazione.model().SNG_ID_TRIBUTO.ID_DOMINIO.COD_DOMINIO)){
			if(appendTablePrefix){
				return this.toAliasTable(field)+".cod_dominio";
			}else{
				return "cod_dominio";
			}
		}
		if(field.equals(VistaRendicontazione.model().SNG_ID_TRIBUTO.ID_TIPO_TRIBUTO.COD_TRIBUTO)){
			if(appendTablePrefix){
				return this.toAliasTable(field)+".cod_tributo";
			}else{
				return "cod_tributo";
			}
		}
		if(field.equals(VistaRendicontazione.model().SNG_COD_SING_VERS_ENTE)){
			if(appendTablePrefix){
				return this.toAliasTable(field)+".sng_cod_sing_vers_ente";
			}else{
				return "sng_cod_sing_vers_ente";
			}
		}
		if(field.equals(VistaRendicontazione.model().SNG_STATO_SINGOLO_VERSAMENTO)){
			if(appendTablePrefix){
				return this.toAliasTable(field)+".sng_stato_singolo_versamento";
			}else{
				return "sng_stato_singolo_versamento";
			}
		}
		if(field.equals(VistaRendicontazione.model().SNG_IMPORTO_SINGOLO_VERSAMENTO)){
			if(appendTablePrefix){
				return this.toAliasTable(field)+".sng_importo_singolo_versamento";
			}else{
				return "sng_importo_singolo_versamento";
			}
		}
		if(field.equals(VistaRendicontazione.model().SNG_DESCRIZIONE)){
			if(appendTablePrefix){
				return this.toAliasTable(field)+".sng_descrizione";
			}else{
				return "sng_descrizione";
			}
		}
		if(field.equals(VistaRendicontazione.model().SNG_DATI_ALLEGATI)){
			if(appendTablePrefix){
				return this.toAliasTable(field)+".sng_dati_allegati";
			}else{
				return "sng_dati_allegati";
			}
		}
		if(field.equals(VistaRendicontazione.model().SNG_INDICE_DATI)){
			if(appendTablePrefix){
				return this.toAliasTable(field)+".sng_indice_dati";
			}else{
				return "sng_indice_dati";
			}
		}
		if(field.equals(VistaRendicontazione.model().SNG_DESCRIZIONE_CAUSALE_RPT)){
			if(appendTablePrefix){
				return this.toAliasTable(field)+".sng_descrizione_causale_rpt";
			}else{
				return "sng_descrizione_causale_rpt";
			}
		}
		if(field.equals(VistaRendicontazione.model().VRS_ID)){
			if(appendTablePrefix){
				return this.toAliasTable(field)+".vrs_id";
			}else{
				return "vrs_id";
			}
		}
		if(field.equals(VistaRendicontazione.model().VRS_COD_VERSAMENTO_ENTE)){
			if(appendTablePrefix){
				return this.toAliasTable(field)+".vrs_cod_versamento_ente";
			}else{
				return "vrs_cod_versamento_ente";
			}
		}
		if(field.equals(VistaRendicontazione.model().VRS_NOME)){
			if(appendTablePrefix){
				return this.toAliasTable(field)+".vrs_nome";
			}else{
				return "vrs_nome";
			}
		}
		if(field.equals(VistaRendicontazione.model().VRS_ID_TIPO_VERSAMENTO_DOMINIO.ID_DOMINIO.COD_DOMINIO)){
			if(appendTablePrefix){
				return this.toAliasTable(field)+".cod_dominio";
			}else{
				return "cod_dominio";
			}
		}
		if(field.equals(VistaRendicontazione.model().VRS_ID_TIPO_VERSAMENTO_DOMINIO.ID_TIPO_VERSAMENTO.COD_TIPO_VERSAMENTO)){
			if(appendTablePrefix){
				return this.toAliasTable(field)+".cod_tipo_versamento";
			}else{
				return "cod_tipo_versamento";
			}
		}
		if(field.equals(VistaRendicontazione.model().VRS_ID_TIPO_VERSAMENTO.COD_TIPO_VERSAMENTO)){
			if(appendTablePrefix){
				return this.toAliasTable(field)+".cod_tipo_versamento";
			}else{
				return "cod_tipo_versamento";
			}
		}
		if(field.equals(VistaRendicontazione.model().VRS_ID_DOMINIO.COD_DOMINIO)){
			if(appendTablePrefix){
				return this.toAliasTable(field)+".cod_dominio";
			}else{
				return "cod_dominio";
			}
		}
		if(field.equals(VistaRendicontazione.model().VRS_ID_UO.COD_UO)){
			if(appendTablePrefix){
				return this.toAliasTable(field)+".cod_uo";
			}else{
				return "cod_uo";
			}
		}
		if(field.equals(VistaRendicontazione.model().VRS_ID_UO.ID_DOMINIO.COD_DOMINIO)){
			if(appendTablePrefix){
				return this.toAliasTable(field)+".cod_dominio";
			}else{
				return "cod_dominio";
			}
		}
		if(field.equals(VistaRendicontazione.model().VRS_ID_APPLICAZIONE.COD_APPLICAZIONE)){
			if(appendTablePrefix){
				return this.toAliasTable(field)+".cod_applicazione";
			}else{
				return "cod_applicazione";
			}
		}
		if(field.equals(VistaRendicontazione.model().VRS_IMPORTO_TOTALE)){
			if(appendTablePrefix){
				return this.toAliasTable(field)+".vrs_importo_totale";
			}else{
				return "vrs_importo_totale";
			}
		}
		if(field.equals(VistaRendicontazione.model().VRS_STATO_VERSAMENTO)){
			if(appendTablePrefix){
				return this.toAliasTable(field)+".vrs_stato_versamento";
			}else{
				return "vrs_stato_versamento";
			}
		}
		if(field.equals(VistaRendicontazione.model().VRS_DESCRIZIONE_STATO)){
			if(appendTablePrefix){
				return this.toAliasTable(field)+".vrs_descrizione_stato";
			}else{
				return "vrs_descrizione_stato";
			}
		}
		if(field.equals(VistaRendicontazione.model().VRS_AGGIORNABILE)){
			if(appendTablePrefix){
				return this.toAliasTable(field)+".vrs_aggiornabile";
			}else{
				return "vrs_aggiornabile";
			}
		}
		if(field.equals(VistaRendicontazione.model().VRS_DATA_CREAZIONE)){
			if(appendTablePrefix){
				return this.toAliasTable(field)+".vrs_data_creazione";
			}else{
				return "vrs_data_creazione";
			}
		}
		if(field.equals(VistaRendicontazione.model().VRS_DATA_VALIDITA)){
			if(appendTablePrefix){
				return this.toAliasTable(field)+".vrs_data_validita";
			}else{
				return "vrs_data_validita";
			}
		}
		if(field.equals(VistaRendicontazione.model().VRS_DATA_SCADENZA)){
			if(appendTablePrefix){
				return this.toAliasTable(field)+".vrs_data_scadenza";
			}else{
				return "vrs_data_scadenza";
			}
		}
		if(field.equals(VistaRendicontazione.model().VRS_DATA_ORA_ULTIMO_AGG)){
			if(appendTablePrefix){
				return this.toAliasTable(field)+".vrs_data_ora_ultimo_agg";
			}else{
				return "vrs_data_ora_ultimo_agg";
			}
		}
		if(field.equals(VistaRendicontazione.model().VRS_CAUSALE_VERSAMENTO)){
			if(appendTablePrefix){
				return this.toAliasTable(field)+".vrs_causale_versamento";
			}else{
				return "vrs_causale_versamento";
			}
		}
		if(field.equals(VistaRendicontazione.model().VRS_DEBITORE_TIPO)){
			if(appendTablePrefix){
				return this.toAliasTable(field)+".vrs_debitore_tipo";
			}else{
				return "vrs_debitore_tipo";
			}
		}
		if(field.equals(VistaRendicontazione.model().VRS_DEBITORE_IDENTIFICATIVO)){
			if(appendTablePrefix){
				return this.toAliasTable(field)+".vrs_debitore_identificativo";
			}else{
				return "vrs_debitore_identificativo";
			}
		}
		if(field.equals(VistaRendicontazione.model().VRS_DEBITORE_ANAGRAFICA)){
			if(appendTablePrefix){
				return this.toAliasTable(field)+".vrs_debitore_anagrafica";
			}else{
				return "vrs_debitore_anagrafica";
			}
		}
		if(field.equals(VistaRendicontazione.model().VRS_DEBITORE_INDIRIZZO)){
			if(appendTablePrefix){
				return this.toAliasTable(field)+".vrs_debitore_indirizzo";
			}else{
				return "vrs_debitore_indirizzo";
			}
		}
		if(field.equals(VistaRendicontazione.model().VRS_DEBITORE_CIVICO)){
			if(appendTablePrefix){
				return this.toAliasTable(field)+".vrs_debitore_civico";
			}else{
				return "vrs_debitore_civico";
			}
		}
		if(field.equals(VistaRendicontazione.model().VRS_DEBITORE_CAP)){
			if(appendTablePrefix){
				return this.toAliasTable(field)+".vrs_debitore_cap";
			}else{
				return "vrs_debitore_cap";
			}
		}
		if(field.equals(VistaRendicontazione.model().VRS_DEBITORE_LOCALITA)){
			if(appendTablePrefix){
				return this.toAliasTable(field)+".vrs_debitore_localita";
			}else{
				return "vrs_debitore_localita";
			}
		}
		if(field.equals(VistaRendicontazione.model().VRS_DEBITORE_PROVINCIA)){
			if(appendTablePrefix){
				return this.toAliasTable(field)+".vrs_debitore_provincia";
			}else{
				return "vrs_debitore_provincia";
			}
		}
		if(field.equals(VistaRendicontazione.model().VRS_DEBITORE_NAZIONE)){
			if(appendTablePrefix){
				return this.toAliasTable(field)+".vrs_debitore_nazione";
			}else{
				return "vrs_debitore_nazione";
			}
		}
		if(field.equals(VistaRendicontazione.model().VRS_DEBITORE_EMAIL)){
			if(appendTablePrefix){
				return this.toAliasTable(field)+".vrs_debitore_email";
			}else{
				return "vrs_debitore_email";
			}
		}
		if(field.equals(VistaRendicontazione.model().VRS_DEBITORE_TELEFONO)){
			if(appendTablePrefix){
				return this.toAliasTable(field)+".vrs_debitore_telefono";
			}else{
				return "vrs_debitore_telefono";
			}
		}
		if(field.equals(VistaRendicontazione.model().VRS_DEBITORE_CELLULARE)){
			if(appendTablePrefix){
				return this.toAliasTable(field)+".vrs_debitore_cellulare";
			}else{
				return "vrs_debitore_cellulare";
			}
		}
		if(field.equals(VistaRendicontazione.model().VRS_DEBITORE_FAX)){
			if(appendTablePrefix){
				return this.toAliasTable(field)+".vrs_debitore_fax";
			}else{
				return "vrs_debitore_fax";
			}
		}
		if(field.equals(VistaRendicontazione.model().VRS_TASSONOMIA_AVVISO)){
			if(appendTablePrefix){
				return this.toAliasTable(field)+".vrs_tassonomia_avviso";
			}else{
				return "vrs_tassonomia_avviso";
			}
		}
		if(field.equals(VistaRendicontazione.model().VRS_TASSONOMIA)){
			if(appendTablePrefix){
				return this.toAliasTable(field)+".vrs_tassonomia";
			}else{
				return "vrs_tassonomia";
			}
		}
		if(field.equals(VistaRendicontazione.model().VRS_COD_LOTTO)){
			if(appendTablePrefix){
				return this.toAliasTable(field)+".vrs_cod_lotto";
			}else{
				return "vrs_cod_lotto";
			}
		}
		if(field.equals(VistaRendicontazione.model().VRS_COD_VERSAMENTO_LOTTO)){
			if(appendTablePrefix){
				return this.toAliasTable(field)+".vrs_cod_versamento_lotto";
			}else{
				return "vrs_cod_versamento_lotto";
			}
		}
		if(field.equals(VistaRendicontazione.model().VRS_COD_ANNO_TRIBUTARIO)){
			if(appendTablePrefix){
				return this.toAliasTable(field)+".vrs_cod_anno_tributario";
			}else{
				return "vrs_cod_anno_tributario";
			}
		}
		if(field.equals(VistaRendicontazione.model().VRS_COD_BUNDLEKEY)){
			if(appendTablePrefix){
				return this.toAliasTable(field)+".vrs_cod_bundlekey";
			}else{
				return "vrs_cod_bundlekey";
			}
		}
		if(field.equals(VistaRendicontazione.model().VRS_DATI_ALLEGATI)){
			if(appendTablePrefix){
				return this.toAliasTable(field)+".vrs_dati_allegati";
			}else{
				return "vrs_dati_allegati";
			}
		}
		if(field.equals(VistaRendicontazione.model().VRS_INCASSO)){
			if(appendTablePrefix){
				return this.toAliasTable(field)+".vrs_incasso";
			}else{
				return "vrs_incasso";
			}
		}
		if(field.equals(VistaRendicontazione.model().VRS_ANOMALIE)){
			if(appendTablePrefix){
				return this.toAliasTable(field)+".vrs_anomalie";
			}else{
				return "vrs_anomalie";
			}
		}
		if(field.equals(VistaRendicontazione.model().VRS_IUV_VERSAMENTO)){
			if(appendTablePrefix){
				return this.toAliasTable(field)+".vrs_iuv_versamento";
			}else{
				return "vrs_iuv_versamento";
			}
		}
		if(field.equals(VistaRendicontazione.model().VRS_NUMERO_AVVISO)){
			if(appendTablePrefix){
				return this.toAliasTable(field)+".vrs_numero_avviso";
			}else{
				return "vrs_numero_avviso";
			}
		}
		if(field.equals(VistaRendicontazione.model().VRS_ACK)){
			if(appendTablePrefix){
				return this.toAliasTable(field)+".vrs_ack";
			}else{
				return "vrs_ack";
			}
		}
		if(field.equals(VistaRendicontazione.model().VRS_ANOMALO)){
			if(appendTablePrefix){
				return this.toAliasTable(field)+".vrs_anomalo";
			}else{
				return "vrs_anomalo";
			}
		}
		if(field.equals(VistaRendicontazione.model().VRS_DIVISIONE)){
			if(appendTablePrefix){
				return this.toAliasTable(field)+".vrs_divisione";
			}else{
				return "vrs_divisione";
			}
		}
		if(field.equals(VistaRendicontazione.model().VRS_DIREZIONE)){
			if(appendTablePrefix){
				return this.toAliasTable(field)+".vrs_direzione";
			}else{
				return "vrs_direzione";
			}
		}
		if(field.equals(VistaRendicontazione.model().VRS_ID_SESSIONE)){
			if(appendTablePrefix){
				return this.toAliasTable(field)+".vrs_id_sessione";
			}else{
				return "vrs_id_sessione";
			}
		}
		if(field.equals(VistaRendicontazione.model().VRS_DATA_PAGAMENTO)){
			if(appendTablePrefix){
				return this.toAliasTable(field)+".vrs_data_pagamento";
			}else{
				return "vrs_data_pagamento";
			}
		}
		if(field.equals(VistaRendicontazione.model().VRS_IMPORTO_PAGATO)){
			if(appendTablePrefix){
				return this.toAliasTable(field)+".vrs_importo_pagato";
			}else{
				return "vrs_importo_pagato";
			}
		}
		if(field.equals(VistaRendicontazione.model().VRS_IMPORTO_INCASSATO)){
			if(appendTablePrefix){
				return this.toAliasTable(field)+".vrs_importo_incassato";
			}else{
				return "vrs_importo_incassato";
			}
		}
		if(field.equals(VistaRendicontazione.model().VRS_STATO_PAGAMENTO)){
			if(appendTablePrefix){
				return this.toAliasTable(field)+".vrs_stato_pagamento";
			}else{
				return "vrs_stato_pagamento";
			}
		}
		if(field.equals(VistaRendicontazione.model().VRS_IUV_PAGAMENTO)){
			if(appendTablePrefix){
				return this.toAliasTable(field)+".vrs_iuv_pagamento";
			}else{
				return "vrs_iuv_pagamento";
			}
		}
		if(field.equals(VistaRendicontazione.model().VRS_COD_RATA)){
			if(appendTablePrefix){
				return this.toAliasTable(field)+".vrs_cod_rata";
			}else{
				return "vrs_cod_rata";
			}
		}
		if(field.equals(VistaRendicontazione.model().VRS_ID_DOCUMENTO.COD_DOCUMENTO)){
			if(appendTablePrefix){
				return this.toAliasTable(field)+".cod_documento";
			}else{
				return "cod_documento";
			}
		}
		if(field.equals(VistaRendicontazione.model().VRS_ID_DOCUMENTO.ID_APPLICAZIONE.COD_APPLICAZIONE)){
			if(appendTablePrefix){
				return this.toAliasTable(field)+".cod_applicazione";
			}else{
				return "cod_applicazione";
			}
		}
		if(field.equals(VistaRendicontazione.model().VRS_TIPO)){
			if(appendTablePrefix){
				return this.toAliasTable(field)+".vrs_tipo";
			}else{
				return "vrs_tipo";
			}
		}
		if(field.equals(VistaRendicontazione.model().VRS_PROPRIETA)){
			if(appendTablePrefix){
				return this.toAliasTable(field)+".vrs_proprieta";
			}else{
				return "vrs_proprieta";
			}
		}


		return super.toColumn(field,returnAlias,appendTablePrefix);
		
	}
	
	@Override
	public String toTable(IField field,boolean returnAlias) throws ExpressionException {
		
		// In the case of table with alias, using parameter returnAlias​​, 
		// it is possible to drive the choice whether to return only the alias or 
		// the full definition of the table containing the alias
		
		if(field.equals(VistaRendicontazione.model().FR_COD_PSP)){
			return this.toTable(VistaRendicontazione.model(), returnAlias);
		}
		if(field.equals(VistaRendicontazione.model().FR_COD_DOMINIO)){
			return this.toTable(VistaRendicontazione.model(), returnAlias);
		}
		if(field.equals(VistaRendicontazione.model().FR_COD_FLUSSO)){
			return this.toTable(VistaRendicontazione.model(), returnAlias);
		}
		if(field.equals(VistaRendicontazione.model().FR_STATO)){
			return this.toTable(VistaRendicontazione.model(), returnAlias);
		}
		if(field.equals(VistaRendicontazione.model().FR_DESCRIZIONE_STATO)){
			return this.toTable(VistaRendicontazione.model(), returnAlias);
		}
		if(field.equals(VistaRendicontazione.model().FR_IUR)){
			return this.toTable(VistaRendicontazione.model(), returnAlias);
		}
		if(field.equals(VistaRendicontazione.model().FR_DATA_ORA_FLUSSO)){
			return this.toTable(VistaRendicontazione.model(), returnAlias);
		}
		if(field.equals(VistaRendicontazione.model().FR_DATA_REGOLAMENTO)){
			return this.toTable(VistaRendicontazione.model(), returnAlias);
		}
		if(field.equals(VistaRendicontazione.model().FR_DATA_ACQUISIZIONE)){
			return this.toTable(VistaRendicontazione.model(), returnAlias);
		}
		if(field.equals(VistaRendicontazione.model().FR_NUMERO_PAGAMENTI)){
			return this.toTable(VistaRendicontazione.model(), returnAlias);
		}
		if(field.equals(VistaRendicontazione.model().FR_IMPORTO_TOTALE_PAGAMENTI)){
			return this.toTable(VistaRendicontazione.model(), returnAlias);
		}
		if(field.equals(VistaRendicontazione.model().FR_COD_BIC_RIVERSAMENTO)){
			return this.toTable(VistaRendicontazione.model(), returnAlias);
		}
		if(field.equals(VistaRendicontazione.model().FR_ID)){
			return this.toTable(VistaRendicontazione.model(), returnAlias);
		}
		if(field.equals(VistaRendicontazione.model().FR_ID_INCASSO.TRN)){
			return this.toTable(VistaRendicontazione.model().FR_ID_INCASSO, returnAlias);
		}
		if(field.equals(VistaRendicontazione.model().FR_ID_INCASSO.COD_DOMINIO)){
			return this.toTable(VistaRendicontazione.model().FR_ID_INCASSO, returnAlias);
		}
		if(field.equals(VistaRendicontazione.model().FR_RAGIONE_SOCIALE_PSP)){
			return this.toTable(VistaRendicontazione.model(), returnAlias);
		}
		if(field.equals(VistaRendicontazione.model().FR_RAGIONE_SOCIALE_DOMINIO)){
			return this.toTable(VistaRendicontazione.model(), returnAlias);
		}
		if(field.equals(VistaRendicontazione.model().FR_OBSOLETO)){
			return this.toTable(VistaRendicontazione.model(), returnAlias);
		}
		if(field.equals(VistaRendicontazione.model().RND_IUV)){
			return this.toTable(VistaRendicontazione.model(), returnAlias);
		}
		if(field.equals(VistaRendicontazione.model().RND_IUR)){
			return this.toTable(VistaRendicontazione.model(), returnAlias);
		}
		if(field.equals(VistaRendicontazione.model().RND_INDICE_DATI)){
			return this.toTable(VistaRendicontazione.model(), returnAlias);
		}
		if(field.equals(VistaRendicontazione.model().RND_IMPORTO_PAGATO)){
			return this.toTable(VistaRendicontazione.model(), returnAlias);
		}
		if(field.equals(VistaRendicontazione.model().RND_ESITO)){
			return this.toTable(VistaRendicontazione.model(), returnAlias);
		}
		if(field.equals(VistaRendicontazione.model().RND_DATA)){
			return this.toTable(VistaRendicontazione.model(), returnAlias);
		}
		if(field.equals(VistaRendicontazione.model().RND_STATO)){
			return this.toTable(VistaRendicontazione.model(), returnAlias);
		}
		if(field.equals(VistaRendicontazione.model().RND_ANOMALIE)){
			return this.toTable(VistaRendicontazione.model(), returnAlias);
		}
		if(field.equals(VistaRendicontazione.model().RND_ID_PAGAMENTO.ID_PAGAMENTO)){
			return this.toTable(VistaRendicontazione.model().RND_ID_PAGAMENTO, returnAlias);
		}
		if(field.equals(VistaRendicontazione.model().RND_ID_PAGAMENTO.INDICE_DATI)){
			return this.toTable(VistaRendicontazione.model().RND_ID_PAGAMENTO, returnAlias);
		}
		if(field.equals(VistaRendicontazione.model().RND_ID_PAGAMENTO.IUV)){
			return this.toTable(VistaRendicontazione.model().RND_ID_PAGAMENTO, returnAlias);
		}
		if(field.equals(VistaRendicontazione.model().SNG_ID_TRIBUTO.ID_DOMINIO.COD_DOMINIO)){
			return this.toTable(VistaRendicontazione.model().SNG_ID_TRIBUTO.ID_DOMINIO, returnAlias);
		}
		if(field.equals(VistaRendicontazione.model().SNG_ID_TRIBUTO.ID_TIPO_TRIBUTO.COD_TRIBUTO)){
			return this.toTable(VistaRendicontazione.model().SNG_ID_TRIBUTO.ID_TIPO_TRIBUTO, returnAlias);
		}
		if(field.equals(VistaRendicontazione.model().SNG_COD_SING_VERS_ENTE)){
			return this.toTable(VistaRendicontazione.model(), returnAlias);
		}
		if(field.equals(VistaRendicontazione.model().SNG_STATO_SINGOLO_VERSAMENTO)){
			return this.toTable(VistaRendicontazione.model(), returnAlias);
		}
		if(field.equals(VistaRendicontazione.model().SNG_IMPORTO_SINGOLO_VERSAMENTO)){
			return this.toTable(VistaRendicontazione.model(), returnAlias);
		}
		if(field.equals(VistaRendicontazione.model().SNG_DESCRIZIONE)){
			return this.toTable(VistaRendicontazione.model(), returnAlias);
		}
		if(field.equals(VistaRendicontazione.model().SNG_DATI_ALLEGATI)){
			return this.toTable(VistaRendicontazione.model(), returnAlias);
		}
		if(field.equals(VistaRendicontazione.model().SNG_INDICE_DATI)){
			return this.toTable(VistaRendicontazione.model(), returnAlias);
		}
		if(field.equals(VistaRendicontazione.model().SNG_DESCRIZIONE_CAUSALE_RPT)){
			return this.toTable(VistaRendicontazione.model(), returnAlias);
		}
		if(field.equals(VistaRendicontazione.model().VRS_ID)){
			return this.toTable(VistaRendicontazione.model(), returnAlias);
		}
		if(field.equals(VistaRendicontazione.model().VRS_COD_VERSAMENTO_ENTE)){
			return this.toTable(VistaRendicontazione.model(), returnAlias);
		}
		if(field.equals(VistaRendicontazione.model().VRS_NOME)){
			return this.toTable(VistaRendicontazione.model(), returnAlias);
		}
		if(field.equals(VistaRendicontazione.model().VRS_ID_TIPO_VERSAMENTO_DOMINIO.ID_DOMINIO.COD_DOMINIO)){
			return this.toTable(VistaRendicontazione.model().VRS_ID_TIPO_VERSAMENTO_DOMINIO.ID_DOMINIO, returnAlias);
		}
		if(field.equals(VistaRendicontazione.model().VRS_ID_TIPO_VERSAMENTO_DOMINIO.ID_TIPO_VERSAMENTO.COD_TIPO_VERSAMENTO)){
			return this.toTable(VistaRendicontazione.model().VRS_ID_TIPO_VERSAMENTO_DOMINIO.ID_TIPO_VERSAMENTO, returnAlias);
		}
		if(field.equals(VistaRendicontazione.model().VRS_ID_TIPO_VERSAMENTO.COD_TIPO_VERSAMENTO)){
			return this.toTable(VistaRendicontazione.model().VRS_ID_TIPO_VERSAMENTO, returnAlias);
		}
		if(field.equals(VistaRendicontazione.model().VRS_ID_DOMINIO.COD_DOMINIO)){
			return this.toTable(VistaRendicontazione.model().VRS_ID_DOMINIO, returnAlias);
		}
		if(field.equals(VistaRendicontazione.model().VRS_ID_UO.COD_UO)){
			return this.toTable(VistaRendicontazione.model().VRS_ID_UO, returnAlias);
		}
		if(field.equals(VistaRendicontazione.model().VRS_ID_UO.ID_DOMINIO.COD_DOMINIO)){
			return this.toTable(VistaRendicontazione.model().VRS_ID_UO.ID_DOMINIO, returnAlias);
		}
		if(field.equals(VistaRendicontazione.model().VRS_ID_APPLICAZIONE.COD_APPLICAZIONE)){
			return this.toTable(VistaRendicontazione.model().VRS_ID_APPLICAZIONE, returnAlias);
		}
		if(field.equals(VistaRendicontazione.model().VRS_IMPORTO_TOTALE)){
			return this.toTable(VistaRendicontazione.model(), returnAlias);
		}
		if(field.equals(VistaRendicontazione.model().VRS_STATO_VERSAMENTO)){
			return this.toTable(VistaRendicontazione.model(), returnAlias);
		}
		if(field.equals(VistaRendicontazione.model().VRS_DESCRIZIONE_STATO)){
			return this.toTable(VistaRendicontazione.model(), returnAlias);
		}
		if(field.equals(VistaRendicontazione.model().VRS_AGGIORNABILE)){
			return this.toTable(VistaRendicontazione.model(), returnAlias);
		}
		if(field.equals(VistaRendicontazione.model().VRS_DATA_CREAZIONE)){
			return this.toTable(VistaRendicontazione.model(), returnAlias);
		}
		if(field.equals(VistaRendicontazione.model().VRS_DATA_VALIDITA)){
			return this.toTable(VistaRendicontazione.model(), returnAlias);
		}
		if(field.equals(VistaRendicontazione.model().VRS_DATA_SCADENZA)){
			return this.toTable(VistaRendicontazione.model(), returnAlias);
		}
		if(field.equals(VistaRendicontazione.model().VRS_DATA_ORA_ULTIMO_AGG)){
			return this.toTable(VistaRendicontazione.model(), returnAlias);
		}
		if(field.equals(VistaRendicontazione.model().VRS_CAUSALE_VERSAMENTO)){
			return this.toTable(VistaRendicontazione.model(), returnAlias);
		}
		if(field.equals(VistaRendicontazione.model().VRS_DEBITORE_TIPO)){
			return this.toTable(VistaRendicontazione.model(), returnAlias);
		}
		if(field.equals(VistaRendicontazione.model().VRS_DEBITORE_IDENTIFICATIVO)){
			return this.toTable(VistaRendicontazione.model(), returnAlias);
		}
		if(field.equals(VistaRendicontazione.model().VRS_DEBITORE_ANAGRAFICA)){
			return this.toTable(VistaRendicontazione.model(), returnAlias);
		}
		if(field.equals(VistaRendicontazione.model().VRS_DEBITORE_INDIRIZZO)){
			return this.toTable(VistaRendicontazione.model(), returnAlias);
		}
		if(field.equals(VistaRendicontazione.model().VRS_DEBITORE_CIVICO)){
			return this.toTable(VistaRendicontazione.model(), returnAlias);
		}
		if(field.equals(VistaRendicontazione.model().VRS_DEBITORE_CAP)){
			return this.toTable(VistaRendicontazione.model(), returnAlias);
		}
		if(field.equals(VistaRendicontazione.model().VRS_DEBITORE_LOCALITA)){
			return this.toTable(VistaRendicontazione.model(), returnAlias);
		}
		if(field.equals(VistaRendicontazione.model().VRS_DEBITORE_PROVINCIA)){
			return this.toTable(VistaRendicontazione.model(), returnAlias);
		}
		if(field.equals(VistaRendicontazione.model().VRS_DEBITORE_NAZIONE)){
			return this.toTable(VistaRendicontazione.model(), returnAlias);
		}
		if(field.equals(VistaRendicontazione.model().VRS_DEBITORE_EMAIL)){
			return this.toTable(VistaRendicontazione.model(), returnAlias);
		}
		if(field.equals(VistaRendicontazione.model().VRS_DEBITORE_TELEFONO)){
			return this.toTable(VistaRendicontazione.model(), returnAlias);
		}
		if(field.equals(VistaRendicontazione.model().VRS_DEBITORE_CELLULARE)){
			return this.toTable(VistaRendicontazione.model(), returnAlias);
		}
		if(field.equals(VistaRendicontazione.model().VRS_DEBITORE_FAX)){
			return this.toTable(VistaRendicontazione.model(), returnAlias);
		}
		if(field.equals(VistaRendicontazione.model().VRS_TASSONOMIA_AVVISO)){
			return this.toTable(VistaRendicontazione.model(), returnAlias);
		}
		if(field.equals(VistaRendicontazione.model().VRS_TASSONOMIA)){
			return this.toTable(VistaRendicontazione.model(), returnAlias);
		}
		if(field.equals(VistaRendicontazione.model().VRS_COD_LOTTO)){
			return this.toTable(VistaRendicontazione.model(), returnAlias);
		}
		if(field.equals(VistaRendicontazione.model().VRS_COD_VERSAMENTO_LOTTO)){
			return this.toTable(VistaRendicontazione.model(), returnAlias);
		}
		if(field.equals(VistaRendicontazione.model().VRS_COD_ANNO_TRIBUTARIO)){
			return this.toTable(VistaRendicontazione.model(), returnAlias);
		}
		if(field.equals(VistaRendicontazione.model().VRS_COD_BUNDLEKEY)){
			return this.toTable(VistaRendicontazione.model(), returnAlias);
		}
		if(field.equals(VistaRendicontazione.model().VRS_DATI_ALLEGATI)){
			return this.toTable(VistaRendicontazione.model(), returnAlias);
		}
		if(field.equals(VistaRendicontazione.model().VRS_INCASSO)){
			return this.toTable(VistaRendicontazione.model(), returnAlias);
		}
		if(field.equals(VistaRendicontazione.model().VRS_ANOMALIE)){
			return this.toTable(VistaRendicontazione.model(), returnAlias);
		}
		if(field.equals(VistaRendicontazione.model().VRS_IUV_VERSAMENTO)){
			return this.toTable(VistaRendicontazione.model(), returnAlias);
		}
		if(field.equals(VistaRendicontazione.model().VRS_NUMERO_AVVISO)){
			return this.toTable(VistaRendicontazione.model(), returnAlias);
		}
		if(field.equals(VistaRendicontazione.model().VRS_ACK)){
			return this.toTable(VistaRendicontazione.model(), returnAlias);
		}
		if(field.equals(VistaRendicontazione.model().VRS_ANOMALO)){
			return this.toTable(VistaRendicontazione.model(), returnAlias);
		}
		if(field.equals(VistaRendicontazione.model().VRS_DIVISIONE)){
			return this.toTable(VistaRendicontazione.model(), returnAlias);
		}
		if(field.equals(VistaRendicontazione.model().VRS_DIREZIONE)){
			return this.toTable(VistaRendicontazione.model(), returnAlias);
		}
		if(field.equals(VistaRendicontazione.model().VRS_ID_SESSIONE)){
			return this.toTable(VistaRendicontazione.model(), returnAlias);
		}
		if(field.equals(VistaRendicontazione.model().VRS_DATA_PAGAMENTO)){
			return this.toTable(VistaRendicontazione.model(), returnAlias);
		}
		if(field.equals(VistaRendicontazione.model().VRS_IMPORTO_PAGATO)){
			return this.toTable(VistaRendicontazione.model(), returnAlias);
		}
		if(field.equals(VistaRendicontazione.model().VRS_IMPORTO_INCASSATO)){
			return this.toTable(VistaRendicontazione.model(), returnAlias);
		}
		if(field.equals(VistaRendicontazione.model().VRS_STATO_PAGAMENTO)){
			return this.toTable(VistaRendicontazione.model(), returnAlias);
		}
		if(field.equals(VistaRendicontazione.model().VRS_IUV_PAGAMENTO)){
			return this.toTable(VistaRendicontazione.model(), returnAlias);
		}
		if(field.equals(VistaRendicontazione.model().VRS_COD_RATA)){
			return this.toTable(VistaRendicontazione.model(), returnAlias);
		}
		if(field.equals(VistaRendicontazione.model().VRS_ID_DOCUMENTO.COD_DOCUMENTO)){
			return this.toTable(VistaRendicontazione.model().VRS_ID_DOCUMENTO, returnAlias);
		}
		if(field.equals(VistaRendicontazione.model().VRS_ID_DOCUMENTO.ID_APPLICAZIONE.COD_APPLICAZIONE)){
			return this.toTable(VistaRendicontazione.model().VRS_ID_DOCUMENTO.ID_APPLICAZIONE, returnAlias);
		}
		if(field.equals(VistaRendicontazione.model().VRS_TIPO)){
			return this.toTable(VistaRendicontazione.model(), returnAlias);
		}
		if(field.equals(VistaRendicontazione.model().VRS_PROPRIETA)){
			return this.toTable(VistaRendicontazione.model(), returnAlias);
		}


		return super.toTable(field,returnAlias);
		
	}

	@Override
	public String toTable(IModel<?> model,boolean returnAlias) throws ExpressionException {
		
		// In the case of table with alias, using parameter returnAlias​​, 
		// it is possible to drive the choice whether to return only the alias or 
		// the full definition of the table containing the alias
		
		if(model.equals(VistaRendicontazione.model())){
			return "v_rendicontazioni_ext";
		}
		if(model.equals(VistaRendicontazione.model().FR_ID_INCASSO)){
			return "incassi";
		}
		if(model.equals(VistaRendicontazione.model().RND_ID_PAGAMENTO)){
			return "pagamenti";
		}
		if(model.equals(VistaRendicontazione.model().SNG_ID_TRIBUTO)){
			return "tributi";
		}
		if(model.equals(VistaRendicontazione.model().SNG_ID_TRIBUTO.ID_DOMINIO)){
			return "domini";
		}
		if(model.equals(VistaRendicontazione.model().SNG_ID_TRIBUTO.ID_TIPO_TRIBUTO)){
			return "tipi_tributo";
		}
		if(model.equals(VistaRendicontazione.model().VRS_ID_TIPO_VERSAMENTO_DOMINIO)){
			return "tipi_vers_domini";
		}
		if(model.equals(VistaRendicontazione.model().VRS_ID_TIPO_VERSAMENTO_DOMINIO.ID_DOMINIO)){
			return "domini";
		}
		if(model.equals(VistaRendicontazione.model().VRS_ID_TIPO_VERSAMENTO_DOMINIO.ID_TIPO_VERSAMENTO)){
			return "tipi_versamento";
		}
		if(model.equals(VistaRendicontazione.model().VRS_ID_TIPO_VERSAMENTO)){
			return "tipi_versamento";
		}
		if(model.equals(VistaRendicontazione.model().VRS_ID_DOMINIO)){
			return "domini";
		}
		if(model.equals(VistaRendicontazione.model().VRS_ID_UO)){
			return "uo";
		}
		if(model.equals(VistaRendicontazione.model().VRS_ID_UO.ID_DOMINIO)){
			return "domini";
		}
		if(model.equals(VistaRendicontazione.model().VRS_ID_APPLICAZIONE)){
			return "applicazioni";
		}
		if(model.equals(VistaRendicontazione.model().VRS_ID_DOCUMENTO)){
			return "documenti";
		}
		if(model.equals(VistaRendicontazione.model().VRS_ID_DOCUMENTO.ID_APPLICAZIONE)){
			return "applicazioni";
		}


		return super.toTable(model,returnAlias);
		
	}

}
