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

import it.govpay.orm.VistaVersamento;


/**     
 * VistaVersamentoFieldConverter
 *
 * @author Giovanni Bussu (bussu@link.it)
 * @author Lorenzo Nardi (nardi@link.it)
 * @author $Author$
 * @version $Rev$, $Date$
 */
public class VistaVersamentoFieldConverter extends AbstractSQLFieldConverter {

	private TipiDatabase databaseType;
	
	public VistaVersamentoFieldConverter(String databaseType){
		this.databaseType = TipiDatabase.toEnumConstant(databaseType);
	}
	public VistaVersamentoFieldConverter(TipiDatabase databaseType){
		this.databaseType = databaseType;
	}


	@Override
	public IModel<?> getRootModel() throws ExpressionException {
		return VistaVersamento.model();
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
		
		if(field.equals(VistaVersamento.model().COD_VERSAMENTO_ENTE)){
			if(appendTablePrefix){
				return this.toAliasTable(field)+".cod_versamento_ente";
			}else{
				return "cod_versamento_ente";
			}
		}
		if(field.equals(VistaVersamento.model().NOME)){
			if(appendTablePrefix){
				return this.toAliasTable(field)+".nome";
			}else{
				return "nome";
			}
		}
		if(field.equals(VistaVersamento.model().ID_TIPO_VERSAMENTO_DOMINIO.ID_DOMINIO.COD_DOMINIO)){
			if(appendTablePrefix){
				return this.toAliasTable(field)+".cod_dominio";
			}else{
				return "cod_dominio";
			}
		}
		if(field.equals(VistaVersamento.model().ID_TIPO_VERSAMENTO_DOMINIO.ID_TIPO_VERSAMENTO.COD_TIPO_VERSAMENTO)){
			if(appendTablePrefix){
				return this.toAliasTable(field)+".cod_tipo_versamento";
			}else{
				return "cod_tipo_versamento";
			}
		}
		if(field.equals(VistaVersamento.model().ID_TIPO_VERSAMENTO.COD_TIPO_VERSAMENTO)){
			if(appendTablePrefix){
				return this.toAliasTable(field)+".cod_tipo_versamento";
			}else{
				return "cod_tipo_versamento";
			}
		}
		if(field.equals(VistaVersamento.model().ID_DOMINIO.COD_DOMINIO)){
			if(appendTablePrefix){
				return this.toAliasTable(field)+".cod_dominio";
			}else{
				return "cod_dominio";
			}
		}
		if(field.equals(VistaVersamento.model().ID_UO.COD_UO)){
			if(appendTablePrefix){
				return this.toAliasTable(field)+".cod_uo";
			}else{
				return "cod_uo";
			}
		}
		if(field.equals(VistaVersamento.model().ID_UO.ID_DOMINIO.COD_DOMINIO)){
			if(appendTablePrefix){
				return this.toAliasTable(field)+".cod_dominio";
			}else{
				return "cod_dominio";
			}
		}
		if(field.equals(VistaVersamento.model().ID_APPLICAZIONE.COD_APPLICAZIONE)){
			if(appendTablePrefix){
				return this.toAliasTable(field)+".cod_applicazione";
			}else{
				return "cod_applicazione";
			}
		}
		if(field.equals(VistaVersamento.model().ID_PAGAMENTO_PORTALE.ID_SESSIONE)){
			if(appendTablePrefix){
				return this.toAliasTable(field)+".id_sessione";
			}else{
				return "id_sessione";
			}
		}
		if(field.equals(VistaVersamento.model().ID_PAGAMENTO_PORTALE.ID_APPLICAZIONE.COD_APPLICAZIONE)){
			if(appendTablePrefix){
				return this.toAliasTable(field)+".cod_applicazione";
			}else{
				return "cod_applicazione";
			}
		}
		if(field.equals(VistaVersamento.model().ID_PAGAMENTO_PORTALE.SRC_VERSANTE_IDENTIFICATIVO)){
			if(appendTablePrefix){
				return this.toAliasTable(field)+".src_versante_identificativo";
			}else{
				return "src_versante_identificativo";
			}
		}
		if(field.equals(VistaVersamento.model().IUV.IUV)){
			if(appendTablePrefix){
				return this.toAliasTable(field)+".iuv";
			}else{
				return "iuv";
			}
		}
		if(field.equals(VistaVersamento.model().IMPORTO_TOTALE)){
			if(appendTablePrefix){
				return this.toAliasTable(field)+".importo_totale";
			}else{
				return "importo_totale";
			}
		}
		if(field.equals(VistaVersamento.model().STATO_VERSAMENTO)){
			if(appendTablePrefix){
				return this.toAliasTable(field)+".stato_versamento";
			}else{
				return "stato_versamento";
			}
		}
		if(field.equals(VistaVersamento.model().DESCRIZIONE_STATO)){
			if(appendTablePrefix){
				return this.toAliasTable(field)+".descrizione_stato";
			}else{
				return "descrizione_stato";
			}
		}
		if(field.equals(VistaVersamento.model().AGGIORNABILE)){
			if(appendTablePrefix){
				return this.toAliasTable(field)+".aggiornabile";
			}else{
				return "aggiornabile";
			}
		}
		if(field.equals(VistaVersamento.model().DATA_CREAZIONE)){
			if(appendTablePrefix){
				return this.toAliasTable(field)+".data_creazione";
			}else{
				return "data_creazione";
			}
		}
		if(field.equals(VistaVersamento.model().DATA_VALIDITA)){
			if(appendTablePrefix){
				return this.toAliasTable(field)+".data_validita";
			}else{
				return "data_validita";
			}
		}
		if(field.equals(VistaVersamento.model().DATA_SCADENZA)){
			if(appendTablePrefix){
				return this.toAliasTable(field)+".data_scadenza";
			}else{
				return "data_scadenza";
			}
		}
		if(field.equals(VistaVersamento.model().DATA_ORA_ULTIMO_AGGIORNAMENTO)){
			if(appendTablePrefix){
				return this.toAliasTable(field)+".data_ora_ultimo_aggiornamento";
			}else{
				return "data_ora_ultimo_aggiornamento";
			}
		}
		if(field.equals(VistaVersamento.model().CAUSALE_VERSAMENTO)){
			if(appendTablePrefix){
				return this.toAliasTable(field)+".causale_versamento";
			}else{
				return "causale_versamento";
			}
		}
		if(field.equals(VistaVersamento.model().DEBITORE_TIPO)){
			if(appendTablePrefix){
				return this.toAliasTable(field)+".debitore_tipo";
			}else{
				return "debitore_tipo";
			}
		}
		if(field.equals(VistaVersamento.model().DEBITORE_IDENTIFICATIVO)){
			if(appendTablePrefix){
				return this.toAliasTable(field)+".debitore_identificativo";
			}else{
				return "debitore_identificativo";
			}
		}
		if(field.equals(VistaVersamento.model().DEBITORE_ANAGRAFICA)){
			if(appendTablePrefix){
				return this.toAliasTable(field)+".debitore_anagrafica";
			}else{
				return "debitore_anagrafica";
			}
		}
		if(field.equals(VistaVersamento.model().DEBITORE_INDIRIZZO)){
			if(appendTablePrefix){
				return this.toAliasTable(field)+".debitore_indirizzo";
			}else{
				return "debitore_indirizzo";
			}
		}
		if(field.equals(VistaVersamento.model().DEBITORE_CIVICO)){
			if(appendTablePrefix){
				return this.toAliasTable(field)+".debitore_civico";
			}else{
				return "debitore_civico";
			}
		}
		if(field.equals(VistaVersamento.model().DEBITORE_CAP)){
			if(appendTablePrefix){
				return this.toAliasTable(field)+".debitore_cap";
			}else{
				return "debitore_cap";
			}
		}
		if(field.equals(VistaVersamento.model().DEBITORE_LOCALITA)){
			if(appendTablePrefix){
				return this.toAliasTable(field)+".debitore_localita";
			}else{
				return "debitore_localita";
			}
		}
		if(field.equals(VistaVersamento.model().DEBITORE_PROVINCIA)){
			if(appendTablePrefix){
				return this.toAliasTable(field)+".debitore_provincia";
			}else{
				return "debitore_provincia";
			}
		}
		if(field.equals(VistaVersamento.model().DEBITORE_NAZIONE)){
			if(appendTablePrefix){
				return this.toAliasTable(field)+".debitore_nazione";
			}else{
				return "debitore_nazione";
			}
		}
		if(field.equals(VistaVersamento.model().DEBITORE_EMAIL)){
			if(appendTablePrefix){
				return this.toAliasTable(field)+".debitore_email";
			}else{
				return "debitore_email";
			}
		}
		if(field.equals(VistaVersamento.model().DEBITORE_TELEFONO)){
			if(appendTablePrefix){
				return this.toAliasTable(field)+".debitore_telefono";
			}else{
				return "debitore_telefono";
			}
		}
		if(field.equals(VistaVersamento.model().DEBITORE_CELLULARE)){
			if(appendTablePrefix){
				return this.toAliasTable(field)+".debitore_cellulare";
			}else{
				return "debitore_cellulare";
			}
		}
		if(field.equals(VistaVersamento.model().DEBITORE_FAX)){
			if(appendTablePrefix){
				return this.toAliasTable(field)+".debitore_fax";
			}else{
				return "debitore_fax";
			}
		}
		if(field.equals(VistaVersamento.model().TASSONOMIA_AVVISO)){
			if(appendTablePrefix){
				return this.toAliasTable(field)+".tassonomia_avviso";
			}else{
				return "tassonomia_avviso";
			}
		}
		if(field.equals(VistaVersamento.model().TASSONOMIA)){
			if(appendTablePrefix){
				return this.toAliasTable(field)+".tassonomia";
			}else{
				return "tassonomia";
			}
		}
		if(field.equals(VistaVersamento.model().COD_LOTTO)){
			if(appendTablePrefix){
				return this.toAliasTable(field)+".cod_lotto";
			}else{
				return "cod_lotto";
			}
		}
		if(field.equals(VistaVersamento.model().COD_VERSAMENTO_LOTTO)){
			if(appendTablePrefix){
				return this.toAliasTable(field)+".cod_versamento_lotto";
			}else{
				return "cod_versamento_lotto";
			}
		}
		if(field.equals(VistaVersamento.model().COD_ANNO_TRIBUTARIO)){
			if(appendTablePrefix){
				return this.toAliasTable(field)+".cod_anno_tributario";
			}else{
				return "cod_anno_tributario";
			}
		}
		if(field.equals(VistaVersamento.model().COD_BUNDLEKEY)){
			if(appendTablePrefix){
				return this.toAliasTable(field)+".cod_bundlekey";
			}else{
				return "cod_bundlekey";
			}
		}
		if(field.equals(VistaVersamento.model().DATI_ALLEGATI)){
			if(appendTablePrefix){
				return this.toAliasTable(field)+".dati_allegati";
			}else{
				return "dati_allegati";
			}
		}
		if(field.equals(VistaVersamento.model().INCASSO)){
			if(appendTablePrefix){
				return this.toAliasTable(field)+".incasso";
			}else{
				return "incasso";
			}
		}
		if(field.equals(VistaVersamento.model().ANOMALIE)){
			if(appendTablePrefix){
				return this.toAliasTable(field)+".anomalie";
			}else{
				return "anomalie";
			}
		}
		if(field.equals(VistaVersamento.model().IUV_VERSAMENTO)){
			if(appendTablePrefix){
				return this.toAliasTable(field)+".iuv_versamento";
			}else{
				return "iuv_versamento";
			}
		}
		if(field.equals(VistaVersamento.model().NUMERO_AVVISO)){
			if(appendTablePrefix){
				return this.toAliasTable(field)+".numero_avviso";
			}else{
				return "numero_avviso";
			}
		}
		if(field.equals(VistaVersamento.model().ACK)){
			if(appendTablePrefix){
				return this.toAliasTable(field)+".ack";
			}else{
				return "ack";
			}
		}
		if(field.equals(VistaVersamento.model().ANOMALO)){
			if(appendTablePrefix){
				return this.toAliasTable(field)+".anomalo";
			}else{
				return "anomalo";
			}
		}
		if(field.equals(VistaVersamento.model().DIVISIONE)){
			if(appendTablePrefix){
				return this.toAliasTable(field)+".divisione";
			}else{
				return "divisione";
			}
		}
		if(field.equals(VistaVersamento.model().DIREZIONE)){
			if(appendTablePrefix){
				return this.toAliasTable(field)+".direzione";
			}else{
				return "direzione";
			}
		}
		if(field.equals(VistaVersamento.model().ID_SESSIONE)){
			if(appendTablePrefix){
				return this.toAliasTable(field)+".id_sessione";
			}else{
				return "id_sessione";
			}
		}
		if(field.equals(VistaVersamento.model().DATA_PAGAMENTO)){
			if(appendTablePrefix){
				return this.toAliasTable(field)+".data_pagamento";
			}else{
				return "data_pagamento";
			}
		}
		if(field.equals(VistaVersamento.model().IMPORTO_PAGATO)){
			if(appendTablePrefix){
				return this.toAliasTable(field)+".importo_pagato";
			}else{
				return "importo_pagato";
			}
		}
		if(field.equals(VistaVersamento.model().IMPORTO_INCASSATO)){
			if(appendTablePrefix){
				return this.toAliasTable(field)+".importo_incassato";
			}else{
				return "importo_incassato";
			}
		}
		if(field.equals(VistaVersamento.model().STATO_PAGAMENTO)){
			if(appendTablePrefix){
				return this.toAliasTable(field)+".stato_pagamento";
			}else{
				return "stato_pagamento";
			}
		}
		if(field.equals(VistaVersamento.model().IUV_PAGAMENTO)){
			if(appendTablePrefix){
				return this.toAliasTable(field)+".iuv_pagamento";
			}else{
				return "iuv_pagamento";
			}
		}
		if(field.equals(VistaVersamento.model().SRC_IUV)){
			if(appendTablePrefix){
				return this.toAliasTable(field)+".src_iuv";
			}else{
				return "src_iuv";
			}
		}
		if(field.equals(VistaVersamento.model().SRC_DEBITORE_IDENTIFICATIVO)){
			if(appendTablePrefix){
				return this.toAliasTable(field)+".src_debitore_identificativo";
			}else{
				return "src_debitore_identificativo";
			}
		}
		if(field.equals(VistaVersamento.model().COD_RATA)){
			if(appendTablePrefix){
				return this.toAliasTable(field)+".cod_rata";
			}else{
				return "cod_rata";
			}
		}
		if(field.equals(VistaVersamento.model().ID_DOCUMENTO.COD_DOCUMENTO)){
			if(appendTablePrefix){
				return this.toAliasTable(field)+".cod_documento";
			}else{
				return "cod_documento";
			}
		}
		if(field.equals(VistaVersamento.model().ID_DOCUMENTO.ID_APPLICAZIONE.COD_APPLICAZIONE)){
			if(appendTablePrefix){
				return this.toAliasTable(field)+".cod_applicazione";
			}else{
				return "cod_applicazione";
			}
		}
		if(field.equals(VistaVersamento.model().TIPO)){
			if(appendTablePrefix){
				return this.toAliasTable(field)+".tipo";
			}else{
				return "tipo";
			}
		}
		if(field.equals(VistaVersamento.model().DATA_NOTIFICA_AVVISO)){
			if(appendTablePrefix){
				return this.toAliasTable(field)+".data_notifica_avviso";
			}else{
				return "data_notifica_avviso";
			}
		}
		if(field.equals(VistaVersamento.model().AVVISO_NOTIFICATO)){
			if(appendTablePrefix){
				return this.toAliasTable(field)+".avviso_notificato";
			}else{
				return "avviso_notificato";
			}
		}
		if(field.equals(VistaVersamento.model().AVV_MAIL_DATA_PROM_SCADENZA)){
			if(appendTablePrefix){
				return this.toAliasTable(field)+".avv_mail_data_prom_scadenza";
			}else{
				return "avv_mail_data_prom_scadenza";
			}
		}
		if(field.equals(VistaVersamento.model().AVV_MAIL_PROM_SCAD_NOTIFICATO)){
			if(appendTablePrefix){
				return this.toAliasTable(field)+".avv_mail_prom_scad_notificato";
			}else{
				return "avv_mail_prom_scad_notificato";
			}
		}
		if(field.equals(VistaVersamento.model().AVV_APP_IO_DATA_PROM_SCADENZA)){
			if(appendTablePrefix){
				return this.toAliasTable(field)+".avv_app_io_data_prom_scadenza";
			}else{
				return "avv_app_io_data_prom_scadenza";
			}
		}
		if(field.equals(VistaVersamento.model().AVV_APP_IO_PROM_SCAD_NOTIFICATO)){
			if(appendTablePrefix){
				return this.toAliasTable(field)+".avv_app_io_prom_scad_notificat";
			}else{
				return "avv_app_io_prom_scad_notificat";
			}
		}
		if(field.equals(VistaVersamento.model().ID_OPERAZIONE.ID_TRACCIATO.ID_TRACCIATO)){
			if(appendTablePrefix){
				return this.toAliasTable(field)+".id_tracciato";
			}else{
				return "id_tracciato";
			}
		}
		if(field.equals(VistaVersamento.model().ID_OPERAZIONE.LINEA_ELABORAZIONE)){
			if(appendTablePrefix){
				return this.toAliasTable(field)+".linea_elaborazione";
			}else{
				return "linea_elaborazione";
			}
		}
		if(field.equals(VistaVersamento.model().ID_OPERAZIONE.STATO)){
			if(appendTablePrefix){
				return this.toAliasTable(field)+".stato";
			}else{
				return "stato";
			}
		}
		if(field.equals(VistaVersamento.model().COD_DOCUMENTO)){
			if(appendTablePrefix){
				return this.toAliasTable(field)+".cod_documento";
			}else{
				return "cod_documento";
			}
		}
		if(field.equals(VistaVersamento.model().DOC_DESCRIZIONE)){
			if(appendTablePrefix){
				return this.toAliasTable(field)+".doc_descrizione";
			}else{
				return "doc_descrizione";
			}
		}
		if(field.equals(VistaVersamento.model().PROPRIETA)){
			if(appendTablePrefix){
				return this.toAliasTable(field)+".proprieta";
			}else{
				return "proprieta";
			}
		}


		return super.toColumn(field,returnAlias,appendTablePrefix);
		
	}
	
	@Override
	public String toTable(IField field,boolean returnAlias) throws ExpressionException {
		
		// In the case of table with alias, using parameter returnAlias​​, 
		// it is possible to drive the choice whether to return only the alias or 
		// the full definition of the table containing the alias
		
		if(field.equals(VistaVersamento.model().COD_VERSAMENTO_ENTE)){
			return this.toTable(VistaVersamento.model(), returnAlias);
		}
		if(field.equals(VistaVersamento.model().NOME)){
			return this.toTable(VistaVersamento.model(), returnAlias);
		}
		if(field.equals(VistaVersamento.model().ID_TIPO_VERSAMENTO_DOMINIO.ID_DOMINIO.COD_DOMINIO)){
			return this.toTable(VistaVersamento.model().ID_TIPO_VERSAMENTO_DOMINIO.ID_DOMINIO, returnAlias);
		}
		if(field.equals(VistaVersamento.model().ID_TIPO_VERSAMENTO_DOMINIO.ID_TIPO_VERSAMENTO.COD_TIPO_VERSAMENTO)){
			return this.toTable(VistaVersamento.model().ID_TIPO_VERSAMENTO_DOMINIO.ID_TIPO_VERSAMENTO, returnAlias);
		}
		if(field.equals(VistaVersamento.model().ID_TIPO_VERSAMENTO.COD_TIPO_VERSAMENTO)){
			return this.toTable(VistaVersamento.model().ID_TIPO_VERSAMENTO, returnAlias);
		}
		if(field.equals(VistaVersamento.model().ID_DOMINIO.COD_DOMINIO)){
			return this.toTable(VistaVersamento.model().ID_DOMINIO, returnAlias);
		}
		if(field.equals(VistaVersamento.model().ID_UO.COD_UO)){
			return this.toTable(VistaVersamento.model().ID_UO, returnAlias);
		}
		if(field.equals(VistaVersamento.model().ID_UO.ID_DOMINIO.COD_DOMINIO)){
			return this.toTable(VistaVersamento.model().ID_UO.ID_DOMINIO, returnAlias);
		}
		if(field.equals(VistaVersamento.model().ID_APPLICAZIONE.COD_APPLICAZIONE)){
			return this.toTable(VistaVersamento.model().ID_APPLICAZIONE, returnAlias);
		}
		if(field.equals(VistaVersamento.model().ID_PAGAMENTO_PORTALE.ID_SESSIONE)){
			return this.toTable(VistaVersamento.model().ID_PAGAMENTO_PORTALE, returnAlias);
		}
		if(field.equals(VistaVersamento.model().ID_PAGAMENTO_PORTALE.ID_APPLICAZIONE.COD_APPLICAZIONE)){
			return this.toTable(VistaVersamento.model().ID_PAGAMENTO_PORTALE.ID_APPLICAZIONE, returnAlias);
		}
		if(field.equals(VistaVersamento.model().ID_PAGAMENTO_PORTALE.SRC_VERSANTE_IDENTIFICATIVO)){
			return this.toTable(VistaVersamento.model().ID_PAGAMENTO_PORTALE, returnAlias);
		}
		if(field.equals(VistaVersamento.model().IUV.IUV)){
			return this.toTable(VistaVersamento.model().IUV, returnAlias);
		}
		if(field.equals(VistaVersamento.model().IMPORTO_TOTALE)){
			return this.toTable(VistaVersamento.model(), returnAlias);
		}
		if(field.equals(VistaVersamento.model().STATO_VERSAMENTO)){
			return this.toTable(VistaVersamento.model(), returnAlias);
		}
		if(field.equals(VistaVersamento.model().DESCRIZIONE_STATO)){
			return this.toTable(VistaVersamento.model(), returnAlias);
		}
		if(field.equals(VistaVersamento.model().AGGIORNABILE)){
			return this.toTable(VistaVersamento.model(), returnAlias);
		}
		if(field.equals(VistaVersamento.model().DATA_CREAZIONE)){
			return this.toTable(VistaVersamento.model(), returnAlias);
		}
		if(field.equals(VistaVersamento.model().DATA_VALIDITA)){
			return this.toTable(VistaVersamento.model(), returnAlias);
		}
		if(field.equals(VistaVersamento.model().DATA_SCADENZA)){
			return this.toTable(VistaVersamento.model(), returnAlias);
		}
		if(field.equals(VistaVersamento.model().DATA_ORA_ULTIMO_AGGIORNAMENTO)){
			return this.toTable(VistaVersamento.model(), returnAlias);
		}
		if(field.equals(VistaVersamento.model().CAUSALE_VERSAMENTO)){
			return this.toTable(VistaVersamento.model(), returnAlias);
		}
		if(field.equals(VistaVersamento.model().DEBITORE_TIPO)){
			return this.toTable(VistaVersamento.model(), returnAlias);
		}
		if(field.equals(VistaVersamento.model().DEBITORE_IDENTIFICATIVO)){
			return this.toTable(VistaVersamento.model(), returnAlias);
		}
		if(field.equals(VistaVersamento.model().DEBITORE_ANAGRAFICA)){
			return this.toTable(VistaVersamento.model(), returnAlias);
		}
		if(field.equals(VistaVersamento.model().DEBITORE_INDIRIZZO)){
			return this.toTable(VistaVersamento.model(), returnAlias);
		}
		if(field.equals(VistaVersamento.model().DEBITORE_CIVICO)){
			return this.toTable(VistaVersamento.model(), returnAlias);
		}
		if(field.equals(VistaVersamento.model().DEBITORE_CAP)){
			return this.toTable(VistaVersamento.model(), returnAlias);
		}
		if(field.equals(VistaVersamento.model().DEBITORE_LOCALITA)){
			return this.toTable(VistaVersamento.model(), returnAlias);
		}
		if(field.equals(VistaVersamento.model().DEBITORE_PROVINCIA)){
			return this.toTable(VistaVersamento.model(), returnAlias);
		}
		if(field.equals(VistaVersamento.model().DEBITORE_NAZIONE)){
			return this.toTable(VistaVersamento.model(), returnAlias);
		}
		if(field.equals(VistaVersamento.model().DEBITORE_EMAIL)){
			return this.toTable(VistaVersamento.model(), returnAlias);
		}
		if(field.equals(VistaVersamento.model().DEBITORE_TELEFONO)){
			return this.toTable(VistaVersamento.model(), returnAlias);
		}
		if(field.equals(VistaVersamento.model().DEBITORE_CELLULARE)){
			return this.toTable(VistaVersamento.model(), returnAlias);
		}
		if(field.equals(VistaVersamento.model().DEBITORE_FAX)){
			return this.toTable(VistaVersamento.model(), returnAlias);
		}
		if(field.equals(VistaVersamento.model().TASSONOMIA_AVVISO)){
			return this.toTable(VistaVersamento.model(), returnAlias);
		}
		if(field.equals(VistaVersamento.model().TASSONOMIA)){
			return this.toTable(VistaVersamento.model(), returnAlias);
		}
		if(field.equals(VistaVersamento.model().COD_LOTTO)){
			return this.toTable(VistaVersamento.model(), returnAlias);
		}
		if(field.equals(VistaVersamento.model().COD_VERSAMENTO_LOTTO)){
			return this.toTable(VistaVersamento.model(), returnAlias);
		}
		if(field.equals(VistaVersamento.model().COD_ANNO_TRIBUTARIO)){
			return this.toTable(VistaVersamento.model(), returnAlias);
		}
		if(field.equals(VistaVersamento.model().COD_BUNDLEKEY)){
			return this.toTable(VistaVersamento.model(), returnAlias);
		}
		if(field.equals(VistaVersamento.model().DATI_ALLEGATI)){
			return this.toTable(VistaVersamento.model(), returnAlias);
		}
		if(field.equals(VistaVersamento.model().INCASSO)){
			return this.toTable(VistaVersamento.model(), returnAlias);
		}
		if(field.equals(VistaVersamento.model().ANOMALIE)){
			return this.toTable(VistaVersamento.model(), returnAlias);
		}
		if(field.equals(VistaVersamento.model().IUV_VERSAMENTO)){
			return this.toTable(VistaVersamento.model(), returnAlias);
		}
		if(field.equals(VistaVersamento.model().NUMERO_AVVISO)){
			return this.toTable(VistaVersamento.model(), returnAlias);
		}
		if(field.equals(VistaVersamento.model().ACK)){
			return this.toTable(VistaVersamento.model(), returnAlias);
		}
		if(field.equals(VistaVersamento.model().ANOMALO)){
			return this.toTable(VistaVersamento.model(), returnAlias);
		}
		if(field.equals(VistaVersamento.model().DIVISIONE)){
			return this.toTable(VistaVersamento.model(), returnAlias);
		}
		if(field.equals(VistaVersamento.model().DIREZIONE)){
			return this.toTable(VistaVersamento.model(), returnAlias);
		}
		if(field.equals(VistaVersamento.model().ID_SESSIONE)){
			return this.toTable(VistaVersamento.model(), returnAlias);
		}
		if(field.equals(VistaVersamento.model().DATA_PAGAMENTO)){
			return this.toTable(VistaVersamento.model(), returnAlias);
		}
		if(field.equals(VistaVersamento.model().IMPORTO_PAGATO)){
			return this.toTable(VistaVersamento.model(), returnAlias);
		}
		if(field.equals(VistaVersamento.model().IMPORTO_INCASSATO)){
			return this.toTable(VistaVersamento.model(), returnAlias);
		}
		if(field.equals(VistaVersamento.model().STATO_PAGAMENTO)){
			return this.toTable(VistaVersamento.model(), returnAlias);
		}
		if(field.equals(VistaVersamento.model().IUV_PAGAMENTO)){
			return this.toTable(VistaVersamento.model(), returnAlias);
		}
		if(field.equals(VistaVersamento.model().SRC_IUV)){
			return this.toTable(VistaVersamento.model(), returnAlias);
		}
		if(field.equals(VistaVersamento.model().SRC_DEBITORE_IDENTIFICATIVO)){
			return this.toTable(VistaVersamento.model(), returnAlias);
		}
		if(field.equals(VistaVersamento.model().COD_RATA)){
			return this.toTable(VistaVersamento.model(), returnAlias);
		}
		if(field.equals(VistaVersamento.model().ID_DOCUMENTO.COD_DOCUMENTO)){
			return this.toTable(VistaVersamento.model().ID_DOCUMENTO, returnAlias);
		}
		if(field.equals(VistaVersamento.model().ID_DOCUMENTO.ID_APPLICAZIONE.COD_APPLICAZIONE)){
			return this.toTable(VistaVersamento.model().ID_DOCUMENTO.ID_APPLICAZIONE, returnAlias);
		}
		if(field.equals(VistaVersamento.model().TIPO)){
			return this.toTable(VistaVersamento.model(), returnAlias);
		}
		if(field.equals(VistaVersamento.model().DATA_NOTIFICA_AVVISO)){
			return this.toTable(VistaVersamento.model(), returnAlias);
		}
		if(field.equals(VistaVersamento.model().AVVISO_NOTIFICATO)){
			return this.toTable(VistaVersamento.model(), returnAlias);
		}
		if(field.equals(VistaVersamento.model().AVV_MAIL_DATA_PROM_SCADENZA)){
			return this.toTable(VistaVersamento.model(), returnAlias);
		}
		if(field.equals(VistaVersamento.model().AVV_MAIL_PROM_SCAD_NOTIFICATO)){
			return this.toTable(VistaVersamento.model(), returnAlias);
		}
		if(field.equals(VistaVersamento.model().AVV_APP_IO_DATA_PROM_SCADENZA)){
			return this.toTable(VistaVersamento.model(), returnAlias);
		}
		if(field.equals(VistaVersamento.model().AVV_APP_IO_PROM_SCAD_NOTIFICATO)){
			return this.toTable(VistaVersamento.model(), returnAlias);
		}
		if(field.equals(VistaVersamento.model().ID_OPERAZIONE.ID_TRACCIATO.ID_TRACCIATO)){
			return this.toTable(VistaVersamento.model().ID_OPERAZIONE.ID_TRACCIATO, returnAlias);
		}
		if(field.equals(VistaVersamento.model().ID_OPERAZIONE.LINEA_ELABORAZIONE)){
			return this.toTable(VistaVersamento.model().ID_OPERAZIONE, returnAlias);
		}
		if(field.equals(VistaVersamento.model().ID_OPERAZIONE.STATO)){
			return this.toTable(VistaVersamento.model().ID_OPERAZIONE, returnAlias);
		}
		if(field.equals(VistaVersamento.model().COD_DOCUMENTO)){
			return this.toTable(VistaVersamento.model(), returnAlias);
		}
		if(field.equals(VistaVersamento.model().DOC_DESCRIZIONE)){
			return this.toTable(VistaVersamento.model(), returnAlias);
		}
		if(field.equals(VistaVersamento.model().PROPRIETA)){
			return this.toTable(VistaVersamento.model(), returnAlias);
		}


		return super.toTable(field,returnAlias);
		
	}

	@Override
	public String toTable(IModel<?> model,boolean returnAlias) throws ExpressionException {
		
		// In the case of table with alias, using parameter returnAlias​​, 
		// it is possible to drive the choice whether to return only the alias or 
		// the full definition of the table containing the alias
		
		if(model.equals(VistaVersamento.model())){
			return "v_versamenti";
		}
		if(model.equals(VistaVersamento.model().ID_TIPO_VERSAMENTO_DOMINIO)){
			return "domini";
		}
		if(model.equals(VistaVersamento.model().ID_TIPO_VERSAMENTO_DOMINIO.ID_DOMINIO)){
			return "id_dominio";
		}
		if(model.equals(VistaVersamento.model().ID_TIPO_VERSAMENTO_DOMINIO.ID_TIPO_VERSAMENTO)){
			return "tipi_versamento";
		}
		if(model.equals(VistaVersamento.model().ID_TIPO_VERSAMENTO)){
			return "tipi_versamento";
		}
		if(model.equals(VistaVersamento.model().ID_DOMINIO)){
			return "domini";
		}
		if(model.equals(VistaVersamento.model().ID_UO)){
			return "uo";
		}
		if(model.equals(VistaVersamento.model().ID_UO.ID_DOMINIO)){
			return "domini";
		}
		if(model.equals(VistaVersamento.model().ID_APPLICAZIONE)){
			return "applicazioni";
		}
		if(model.equals(VistaVersamento.model().ID_PAGAMENTO_PORTALE)){
			return "pagamenti_portale";
		}
		if(model.equals(VistaVersamento.model().ID_PAGAMENTO_PORTALE.ID_APPLICAZIONE)){
			return "applicazioni";
		}
		if(model.equals(VistaVersamento.model().IUV)){
			return "iuv";
		}
		if(model.equals(VistaVersamento.model().ID_DOCUMENTO)){
			return "documenti";
		}
		if(model.equals(VistaVersamento.model().ID_DOCUMENTO.ID_APPLICAZIONE)){
			return "applicazioni";
		}
		if(model.equals(VistaVersamento.model().ID_OPERAZIONE)){
			return "operazioni";
		}
		if(model.equals(VistaVersamento.model().ID_OPERAZIONE.ID_TRACCIATO)){
			return "tracciati";
		}


		return super.toTable(model,returnAlias);
		
	}

}
