/*
 * GovPay - Porta di Accesso al Nodo dei Pagamenti SPC 
 * http://www.gov4j.it/govpay
 * 
 * Copyright (c) 2014-2016 Link.it srl (http://www.link.it).
 * 
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
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

import it.govpay.orm.RendicontazionePagamentoSenzaRPT;


/**     
 * RendicontazionePagamentoSenzaRPTFieldConverter
 *
 * @author Giovanni Bussu (bussu@link.it)
 * @author Lorenzo Nardi (nardi@link.it)
 * @author $Author$
 * @version $Rev$, $Date$
 */
public class RendicontazionePagamentoSenzaRPTFieldConverter extends AbstractSQLFieldConverter {

	private TipiDatabase databaseType;
	
	public RendicontazionePagamentoSenzaRPTFieldConverter(String databaseType){
		this.databaseType = TipiDatabase.toEnumConstant(databaseType);
	}
	public RendicontazionePagamentoSenzaRPTFieldConverter(TipiDatabase databaseType){
		this.databaseType = databaseType;
	}


	@Override
	public IModel<?> getRootModel() throws ExpressionException {
		return RendicontazionePagamentoSenzaRPT.model().FR;
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
		
		if(field.equals(RendicontazionePagamentoSenzaRPT.model().FR.ID_PSP.COD_PSP)){
			if(appendTablePrefix){
				return this.toAliasTable(field)+".cod_psp";
			}else{
				return "cod_psp";
			}
		}
		if(field.equals(RendicontazionePagamentoSenzaRPT.model().FR.ID_DOMINIO.COD_DOMINIO)){
			if(appendTablePrefix){
				return this.toAliasTable(field)+".cod_dominio";
			}else{
				return "cod_dominio";
			}
		}
		if(field.equals(RendicontazionePagamentoSenzaRPT.model().FR.COD_FLUSSO)){
			if(appendTablePrefix){
				return this.toAliasTable(field)+".cod_flusso";
			}else{
				return "cod_flusso";
			}
		}
		if(field.equals(RendicontazionePagamentoSenzaRPT.model().FR.STATO)){
			if(appendTablePrefix){
				return this.toAliasTable(field)+".stato";
			}else{
				return "stato";
			}
		}
		if(field.equals(RendicontazionePagamentoSenzaRPT.model().FR.DESCRIZIONE_STATO)){
			if(appendTablePrefix){
				return this.toAliasTable(field)+".descrizione_stato";
			}else{
				return "descrizione_stato";
			}
		}
		if(field.equals(RendicontazionePagamentoSenzaRPT.model().FR.IUR)){
			if(appendTablePrefix){
				return this.toAliasTable(field)+".iur";
			}else{
				return "iur";
			}
		}
		if(field.equals(RendicontazionePagamentoSenzaRPT.model().FR.ANNO_RIFERIMENTO)){
			if(appendTablePrefix){
				return this.toAliasTable(field)+".anno_riferimento";
			}else{
				return "anno_riferimento";
			}
		}
		if(field.equals(RendicontazionePagamentoSenzaRPT.model().FR.DATA_ORA_FLUSSO)){
			if(appendTablePrefix){
				return this.toAliasTable(field)+".data_ora_flusso";
			}else{
				return "data_ora_flusso";
			}
		}
		if(field.equals(RendicontazionePagamentoSenzaRPT.model().FR.DATA_REGOLAMENTO)){
			if(appendTablePrefix){
				return this.toAliasTable(field)+".data_regolamento";
			}else{
				return "data_regolamento";
			}
		}
		if(field.equals(RendicontazionePagamentoSenzaRPT.model().FR.DATA_ACQUISIZIONE)){
			if(appendTablePrefix){
				return this.toAliasTable(field)+".data_acquisizione";
			}else{
				return "data_acquisizione";
			}
		}
		if(field.equals(RendicontazionePagamentoSenzaRPT.model().FR.NUMERO_PAGAMENTI)){
			if(appendTablePrefix){
				return this.toAliasTable(field)+".numero_pagamenti";
			}else{
				return "numero_pagamenti";
			}
		}
		if(field.equals(RendicontazionePagamentoSenzaRPT.model().FR.IMPORTO_TOTALE_PAGAMENTI)){
			if(appendTablePrefix){
				return this.toAliasTable(field)+".importo_totale_pagamenti";
			}else{
				return "importo_totale_pagamenti";
			}
		}
		if(field.equals(RendicontazionePagamentoSenzaRPT.model().FR.COD_BIC_RIVERSAMENTO)){
			if(appendTablePrefix){
				return this.toAliasTable(field)+".cod_bic_riversamento";
			}else{
				return "cod_bic_riversamento";
			}
		}
		if(field.equals(RendicontazionePagamentoSenzaRPT.model().FR.XML)){
			if(appendTablePrefix){
				return this.toAliasTable(field)+".xml";
			}else{
				return "xml";
			}
		}
		if(field.equals(RendicontazionePagamentoSenzaRPT.model().FR_APPLICAZIONE.ID_APPLICAZIONE.COD_APPLICAZIONE)){
			if(appendTablePrefix){
				return this.toAliasTable(field)+".cod_applicazione";
			}else{
				return "cod_applicazione";
			}
		}
		if(field.equals(RendicontazionePagamentoSenzaRPT.model().FR_APPLICAZIONE.ID_FR.COD_FLUSSO)){
			if(appendTablePrefix){
				return this.toAliasTable(field)+".cod_flusso";
			}else{
				return "cod_flusso";
			}
		}
		if(field.equals(RendicontazionePagamentoSenzaRPT.model().FR_APPLICAZIONE.ID_FR.ANNO_RIFERIMENTO)){
			if(appendTablePrefix){
				return this.toAliasTable(field)+".anno_riferimento";
			}else{
				return "anno_riferimento";
			}
		}
		if(field.equals(RendicontazionePagamentoSenzaRPT.model().FR_APPLICAZIONE.NUMERO_PAGAMENTI)){
			if(appendTablePrefix){
				return this.toAliasTable(field)+".numero_pagamenti";
			}else{
				return "numero_pagamenti";
			}
		}
		if(field.equals(RendicontazionePagamentoSenzaRPT.model().FR_APPLICAZIONE.IMPORTO_TOTALE_PAGAMENTI)){
			if(appendTablePrefix){
				return this.toAliasTable(field)+".importo_totale_pagamenti";
			}else{
				return "importo_totale_pagamenti";
			}
		}
		if(field.equals(RendicontazionePagamentoSenzaRPT.model().RENDICONTAZIONE_SENZA_RPT.ID_FR_APPLICAZIONE.ID_FR.COD_FLUSSO)){
			if(appendTablePrefix){
				return this.toAliasTable(field)+".cod_flusso";
			}else{
				return "cod_flusso";
			}
		}
		if(field.equals(RendicontazionePagamentoSenzaRPT.model().RENDICONTAZIONE_SENZA_RPT.ID_FR_APPLICAZIONE.ID_FR.ANNO_RIFERIMENTO)){
			if(appendTablePrefix){
				return this.toAliasTable(field)+".anno_riferimento";
			}else{
				return "anno_riferimento";
			}
		}
		if(field.equals(RendicontazionePagamentoSenzaRPT.model().RENDICONTAZIONE_SENZA_RPT.ID_FR_APPLICAZIONE.ID_APPLICAZIONE.COD_APPLICAZIONE)){
			if(appendTablePrefix){
				return this.toAliasTable(field)+".cod_applicazione";
			}else{
				return "cod_applicazione";
			}
		}
		if(field.equals(RendicontazionePagamentoSenzaRPT.model().RENDICONTAZIONE_SENZA_RPT.IMPORTO_PAGATO)){
			if(appendTablePrefix){
				return this.toAliasTable(field)+".importo_pagato";
			}else{
				return "importo_pagato";
			}
		}
		if(field.equals(RendicontazionePagamentoSenzaRPT.model().RENDICONTAZIONE_SENZA_RPT.ID_IUV.ID_DOMINIO.COD_DOMINIO)){
			if(appendTablePrefix){
				return this.toAliasTable(field)+".cod_dominio";
			}else{
				return "cod_dominio";
			}
		}
		if(field.equals(RendicontazionePagamentoSenzaRPT.model().RENDICONTAZIONE_SENZA_RPT.ID_IUV.PRG)){
			if(appendTablePrefix){
				return this.toAliasTable(field)+".prg";
			}else{
				return "prg";
			}
		}
		if(field.equals(RendicontazionePagamentoSenzaRPT.model().RENDICONTAZIONE_SENZA_RPT.IUR)){
			if(appendTablePrefix){
				return this.toAliasTable(field)+".iur";
			}else{
				return "iur";
			}
		}
		if(field.equals(RendicontazionePagamentoSenzaRPT.model().RENDICONTAZIONE_SENZA_RPT.RENDICONTAZIONE_DATA)){
			if(appendTablePrefix){
				return this.toAliasTable(field)+".rendicontazione_data";
			}else{
				return "rendicontazione_data";
			}
		}
		if(field.equals(RendicontazionePagamentoSenzaRPT.model().RENDICONTAZIONE_SENZA_RPT.ID_SINGOLO_VERSAMENTO.ID_VERSAMENTO.COD_VERSAMENTO_ENTE)){
			if(appendTablePrefix){
				return this.toAliasTable(field)+".cod_versamento_ente";
			}else{
				return "cod_versamento_ente";
			}
		}
		if(field.equals(RendicontazionePagamentoSenzaRPT.model().RENDICONTAZIONE_SENZA_RPT.ID_SINGOLO_VERSAMENTO.ID_VERSAMENTO.ID_APPLICAZIONE.COD_APPLICAZIONE)){
			if(appendTablePrefix){
				return this.toAliasTable(field)+".cod_applicazione";
			}else{
				return "cod_applicazione";
			}
		}
		if(field.equals(RendicontazionePagamentoSenzaRPT.model().RENDICONTAZIONE_SENZA_RPT.ID_SINGOLO_VERSAMENTO.ID_VERSAMENTO.DEBITORE_IDENTIFICATIVO)){
			if(appendTablePrefix){
				return this.toAliasTable(field)+".debitore_identificativo";
			}else{
				return "debitore_identificativo";
			}
		}
		if(field.equals(RendicontazionePagamentoSenzaRPT.model().RENDICONTAZIONE_SENZA_RPT.ID_SINGOLO_VERSAMENTO.ID_VERSAMENTO.DEBITORE_ANAGRAFICA)){
			if(appendTablePrefix){
				return this.toAliasTable(field)+".debitore_anagrafica";
			}else{
				return "debitore_anagrafica";
			}
		}
		if(field.equals(RendicontazionePagamentoSenzaRPT.model().RENDICONTAZIONE_SENZA_RPT.ID_SINGOLO_VERSAMENTO.ID_VERSAMENTO.COD_VERSAMENTO_LOTTO)){
			if(appendTablePrefix){
				return this.toAliasTable(field)+".cod_versamento_lotto";
			}else{
				return "cod_versamento_lotto";
			}
		}
		if(field.equals(RendicontazionePagamentoSenzaRPT.model().RENDICONTAZIONE_SENZA_RPT.ID_SINGOLO_VERSAMENTO.ID_VERSAMENTO.COD_ANNO_TRIBUTARIO)){
			if(appendTablePrefix){
				return this.toAliasTable(field)+".cod_anno_tributario";
			}else{
				return "cod_anno_tributario";
			}
		}
		if(field.equals(RendicontazionePagamentoSenzaRPT.model().RENDICONTAZIONE_SENZA_RPT.ID_SINGOLO_VERSAMENTO.ID_VERSAMENTO.IMPORTO_TOTALE)){
			if(appendTablePrefix){
				return this.toAliasTable(field)+".importo_totale";
			}else{
				return "importo_totale";
			}
		}
		if(field.equals(RendicontazionePagamentoSenzaRPT.model().RENDICONTAZIONE_SENZA_RPT.ID_SINGOLO_VERSAMENTO.COD_SINGOLO_VERSAMENTO_ENTE)){
			if(appendTablePrefix){
				return this.toAliasTable(field)+".cod_singolo_versamento_ente";
			}else{
				return "cod_singolo_versamento_ente";
			}
		}
		if(field.equals(RendicontazionePagamentoSenzaRPT.model().RENDICONTAZIONE_SENZA_RPT.ID_SINGOLO_VERSAMENTO.ID_TRIBUTO.ID_DOMINIO.COD_DOMINIO)){
			if(appendTablePrefix){
				return this.toAliasTable(field)+".cod_dominio";
			}else{
				return "cod_dominio";
			}
		}
		if(field.equals(RendicontazionePagamentoSenzaRPT.model().RENDICONTAZIONE_SENZA_RPT.ID_SINGOLO_VERSAMENTO.ID_TRIBUTO.ID_TIPO_TRIBUTO.COD_TRIBUTO)){
			if(appendTablePrefix){
				return this.toAliasTable(field)+".cod_tributo";
			}else{
				return "cod_tributo";
			}
		}
		if(field.equals(RendicontazionePagamentoSenzaRPT.model().RENDICONTAZIONE_SENZA_RPT.ID_SINGOLO_VERSAMENTO.NOTE)){
			if(appendTablePrefix){
				return this.toAliasTable(field)+".note";
			}else{
				return "note";
			}
		}
		if(field.equals(RendicontazionePagamentoSenzaRPT.model().SINGOLO_VERSAMENTO.ID_VERSAMENTO.COD_VERSAMENTO_ENTE)){
			if(appendTablePrefix){
				return this.toAliasTable(field)+".cod_versamento_ente";
			}else{
				return "cod_versamento_ente";
			}
		}
		if(field.equals(RendicontazionePagamentoSenzaRPT.model().SINGOLO_VERSAMENTO.ID_VERSAMENTO.ID_APPLICAZIONE.COD_APPLICAZIONE)){
			if(appendTablePrefix){
				return this.toAliasTable(field)+".cod_applicazione";
			}else{
				return "cod_applicazione";
			}
		}
		if(field.equals(RendicontazionePagamentoSenzaRPT.model().SINGOLO_VERSAMENTO.ID_VERSAMENTO.DEBITORE_IDENTIFICATIVO)){
			if(appendTablePrefix){
				return this.toAliasTable(field)+".debitore_identificativo";
			}else{
				return "debitore_identificativo";
			}
		}
		if(field.equals(RendicontazionePagamentoSenzaRPT.model().SINGOLO_VERSAMENTO.ID_VERSAMENTO.DEBITORE_ANAGRAFICA)){
			if(appendTablePrefix){
				return this.toAliasTable(field)+".debitore_anagrafica";
			}else{
				return "debitore_anagrafica";
			}
		}
		if(field.equals(RendicontazionePagamentoSenzaRPT.model().SINGOLO_VERSAMENTO.ID_VERSAMENTO.COD_VERSAMENTO_LOTTO)){
			if(appendTablePrefix){
				return this.toAliasTable(field)+".cod_versamento_lotto";
			}else{
				return "cod_versamento_lotto";
			}
		}
		if(field.equals(RendicontazionePagamentoSenzaRPT.model().SINGOLO_VERSAMENTO.ID_VERSAMENTO.COD_ANNO_TRIBUTARIO)){
			if(appendTablePrefix){
				return this.toAliasTable(field)+".cod_anno_tributario";
			}else{
				return "cod_anno_tributario";
			}
		}
		if(field.equals(RendicontazionePagamentoSenzaRPT.model().SINGOLO_VERSAMENTO.ID_VERSAMENTO.IMPORTO_TOTALE)){
			if(appendTablePrefix){
				return this.toAliasTable(field)+".importo_totale";
			}else{
				return "importo_totale";
			}
		}
		if(field.equals(RendicontazionePagamentoSenzaRPT.model().SINGOLO_VERSAMENTO.ID_TRIBUTO.ID_DOMINIO.COD_DOMINIO)){
			if(appendTablePrefix){
				return this.toAliasTable(field)+".cod_dominio";
			}else{
				return "cod_dominio";
			}
		}
		if(field.equals(RendicontazionePagamentoSenzaRPT.model().SINGOLO_VERSAMENTO.ID_TRIBUTO.ID_TIPO_TRIBUTO.COD_TRIBUTO)){
			if(appendTablePrefix){
				return this.toAliasTable(field)+".cod_tributo";
			}else{
				return "cod_tributo";
			}
		}
		if(field.equals(RendicontazionePagamentoSenzaRPT.model().SINGOLO_VERSAMENTO.COD_SINGOLO_VERSAMENTO_ENTE)){
			if(appendTablePrefix){
				return this.toAliasTable(field)+".cod_singolo_versamento_ente";
			}else{
				return "cod_singolo_versamento_ente";
			}
		}
		if(field.equals(RendicontazionePagamentoSenzaRPT.model().SINGOLO_VERSAMENTO.STATO_SINGOLO_VERSAMENTO)){
			if(appendTablePrefix){
				return this.toAliasTable(field)+".stato_singolo_versamento";
			}else{
				return "stato_singolo_versamento";
			}
		}
		if(field.equals(RendicontazionePagamentoSenzaRPT.model().SINGOLO_VERSAMENTO.IMPORTO_SINGOLO_VERSAMENTO)){
			if(appendTablePrefix){
				return this.toAliasTable(field)+".importo_singolo_versamento";
			}else{
				return "importo_singolo_versamento";
			}
		}
		if(field.equals(RendicontazionePagamentoSenzaRPT.model().SINGOLO_VERSAMENTO.ANNO_RIFERIMENTO)){
			if(appendTablePrefix){
				return this.toAliasTable(field)+".anno_riferimento";
			}else{
				return "anno_riferimento";
			}
		}
		if(field.equals(RendicontazionePagamentoSenzaRPT.model().SINGOLO_VERSAMENTO.TIPO_BOLLO)){
			if(appendTablePrefix){
				return this.toAliasTable(field)+".tipo_bollo";
			}else{
				return "tipo_bollo";
			}
		}
		if(field.equals(RendicontazionePagamentoSenzaRPT.model().SINGOLO_VERSAMENTO.HASH_DOCUMENTO)){
			if(appendTablePrefix){
				return this.toAliasTable(field)+".hash_documento";
			}else{
				return "hash_documento";
			}
		}
		if(field.equals(RendicontazionePagamentoSenzaRPT.model().SINGOLO_VERSAMENTO.PROVINCIA_RESIDENZA)){
			if(appendTablePrefix){
				return this.toAliasTable(field)+".provincia_residenza";
			}else{
				return "provincia_residenza";
			}
		}
		if(field.equals(RendicontazionePagamentoSenzaRPT.model().SINGOLO_VERSAMENTO.ID_IBAN_ACCREDITO.COD_IBAN)){
			if(appendTablePrefix){
				return this.toAliasTable(field)+".cod_iban";
			}else{
				return "cod_iban";
			}
		}
		if(field.equals(RendicontazionePagamentoSenzaRPT.model().SINGOLO_VERSAMENTO.ID_IBAN_ACCREDITO.ID_DOMINIO.COD_DOMINIO)){
			if(appendTablePrefix){
				return this.toAliasTable(field)+".cod_dominio";
			}else{
				return "cod_dominio";
			}
		}
		if(field.equals(RendicontazionePagamentoSenzaRPT.model().SINGOLO_VERSAMENTO.TIPO_CONTABILITA)){
			if(appendTablePrefix){
				return this.toAliasTable(field)+".tipo_contabilita";
			}else{
				return "tipo_contabilita";
			}
		}
		if(field.equals(RendicontazionePagamentoSenzaRPT.model().SINGOLO_VERSAMENTO.CODICE_CONTABILITA)){
			if(appendTablePrefix){
				return this.toAliasTable(field)+".codice_contabilita";
			}else{
				return "codice_contabilita";
			}
		}
		if(field.equals(RendicontazionePagamentoSenzaRPT.model().SINGOLO_VERSAMENTO.NOTE)){
			if(appendTablePrefix){
				return this.toAliasTable(field)+".note";
			}else{
				return "note";
			}
		}
		if(field.equals(RendicontazionePagamentoSenzaRPT.model().VERSAMENTO.COD_VERSAMENTO_ENTE)){
			if(appendTablePrefix){
				return this.toAliasTable(field)+".cod_versamento_ente";
			}else{
				return "cod_versamento_ente";
			}
		}
		if(field.equals(RendicontazionePagamentoSenzaRPT.model().VERSAMENTO.ID_UO.COD_UO)){
			if(appendTablePrefix){
				return this.toAliasTable(field)+".cod_uo";
			}else{
				return "cod_uo";
			}
		}
		if(field.equals(RendicontazionePagamentoSenzaRPT.model().VERSAMENTO.ID_UO.ID_DOMINIO.COD_DOMINIO)){
			if(appendTablePrefix){
				return this.toAliasTable(field)+".cod_dominio";
			}else{
				return "cod_dominio";
			}
		}
		if(field.equals(RendicontazionePagamentoSenzaRPT.model().VERSAMENTO.ID_APPLICAZIONE.COD_APPLICAZIONE)){
			if(appendTablePrefix){
				return this.toAliasTable(field)+".cod_applicazione";
			}else{
				return "cod_applicazione";
			}
		}
		if(field.equals(RendicontazionePagamentoSenzaRPT.model().VERSAMENTO.IMPORTO_TOTALE)){
			if(appendTablePrefix){
				return this.toAliasTable(field)+".importo_totale";
			}else{
				return "importo_totale";
			}
		}
		if(field.equals(RendicontazionePagamentoSenzaRPT.model().VERSAMENTO.STATO_VERSAMENTO)){
			if(appendTablePrefix){
				return this.toAliasTable(field)+".stato_versamento";
			}else{
				return "stato_versamento";
			}
		}
		if(field.equals(RendicontazionePagamentoSenzaRPT.model().VERSAMENTO.DESCRIZIONE_STATO)){
			if(appendTablePrefix){
				return this.toAliasTable(field)+".descrizione_stato";
			}else{
				return "descrizione_stato";
			}
		}
		if(field.equals(RendicontazionePagamentoSenzaRPT.model().VERSAMENTO.AGGIORNABILE)){
			if(appendTablePrefix){
				return this.toAliasTable(field)+".aggiornabile";
			}else{
				return "aggiornabile";
			}
		}
		if(field.equals(RendicontazionePagamentoSenzaRPT.model().VERSAMENTO.DATA_CREAZIONE)){
			if(appendTablePrefix){
				return this.toAliasTable(field)+".data_creazione";
			}else{
				return "data_creazione";
			}
		}
		if(field.equals(RendicontazionePagamentoSenzaRPT.model().VERSAMENTO.DATA_SCADENZA)){
			if(appendTablePrefix){
				return this.toAliasTable(field)+".data_scadenza";
			}else{
				return "data_scadenza";
			}
		}
		if(field.equals(RendicontazionePagamentoSenzaRPT.model().VERSAMENTO.DATA_ORA_ULTIMO_AGGIORNAMENTO)){
			if(appendTablePrefix){
				return this.toAliasTable(field)+".data_ora_ultimo_aggiornamento";
			}else{
				return "data_ora_ultimo_aggiornamento";
			}
		}
		if(field.equals(RendicontazionePagamentoSenzaRPT.model().VERSAMENTO.CAUSALE_VERSAMENTO)){
			if(appendTablePrefix){
				return this.toAliasTable(field)+".causale_versamento";
			}else{
				return "causale_versamento";
			}
		}
		if(field.equals(RendicontazionePagamentoSenzaRPT.model().VERSAMENTO.DEBITORE_IDENTIFICATIVO)){
			if(appendTablePrefix){
				return this.toAliasTable(field)+".debitore_identificativo";
			}else{
				return "debitore_identificativo";
			}
		}
		if(field.equals(RendicontazionePagamentoSenzaRPT.model().VERSAMENTO.DEBITORE_ANAGRAFICA)){
			if(appendTablePrefix){
				return this.toAliasTable(field)+".debitore_anagrafica";
			}else{
				return "debitore_anagrafica";
			}
		}
		if(field.equals(RendicontazionePagamentoSenzaRPT.model().VERSAMENTO.DEBITORE_INDIRIZZO)){
			if(appendTablePrefix){
				return this.toAliasTable(field)+".debitore_indirizzo";
			}else{
				return "debitore_indirizzo";
			}
		}
		if(field.equals(RendicontazionePagamentoSenzaRPT.model().VERSAMENTO.DEBITORE_CIVICO)){
			if(appendTablePrefix){
				return this.toAliasTable(field)+".debitore_civico";
			}else{
				return "debitore_civico";
			}
		}
		if(field.equals(RendicontazionePagamentoSenzaRPT.model().VERSAMENTO.DEBITORE_CAP)){
			if(appendTablePrefix){
				return this.toAliasTable(field)+".debitore_cap";
			}else{
				return "debitore_cap";
			}
		}
		if(field.equals(RendicontazionePagamentoSenzaRPT.model().VERSAMENTO.DEBITORE_LOCALITA)){
			if(appendTablePrefix){
				return this.toAliasTable(field)+".debitore_localita";
			}else{
				return "debitore_localita";
			}
		}
		if(field.equals(RendicontazionePagamentoSenzaRPT.model().VERSAMENTO.DEBITORE_PROVINCIA)){
			if(appendTablePrefix){
				return this.toAliasTable(field)+".debitore_provincia";
			}else{
				return "debitore_provincia";
			}
		}
		if(field.equals(RendicontazionePagamentoSenzaRPT.model().VERSAMENTO.DEBITORE_NAZIONE)){
			if(appendTablePrefix){
				return this.toAliasTable(field)+".debitore_nazione";
			}else{
				return "debitore_nazione";
			}
		}
		if(field.equals(RendicontazionePagamentoSenzaRPT.model().VERSAMENTO.COD_LOTTO)){
			if(appendTablePrefix){
				return this.toAliasTable(field)+".cod_lotto";
			}else{
				return "cod_lotto";
			}
		}
		if(field.equals(RendicontazionePagamentoSenzaRPT.model().VERSAMENTO.COD_VERSAMENTO_LOTTO)){
			if(appendTablePrefix){
				return this.toAliasTable(field)+".cod_versamento_lotto";
			}else{
				return "cod_versamento_lotto";
			}
		}
		if(field.equals(RendicontazionePagamentoSenzaRPT.model().VERSAMENTO.COD_ANNO_TRIBUTARIO)){
			if(appendTablePrefix){
				return this.toAliasTable(field)+".cod_anno_tributario";
			}else{
				return "cod_anno_tributario";
			}
		}
		if(field.equals(RendicontazionePagamentoSenzaRPT.model().VERSAMENTO.COD_BUNDLEKEY)){
			if(appendTablePrefix){
				return this.toAliasTable(field)+".cod_bundlekey";
			}else{
				return "cod_bundlekey";
			}
		}


		return super.toColumn(field,returnAlias,appendTablePrefix);
		
	}
	
	@Override
	public String toTable(IField field,boolean returnAlias) throws ExpressionException {
		
		// In the case of table with alias, using parameter returnAlias​​, 
		// it is possible to drive the choice whether to return only the alias or 
		// the full definition of the table containing the alias
		
		if(field.equals(RendicontazionePagamentoSenzaRPT.model().FR.ID_PSP.COD_PSP)){
			return this.toTable(RendicontazionePagamentoSenzaRPT.model().FR.ID_PSP, returnAlias);
		}
		if(field.equals(RendicontazionePagamentoSenzaRPT.model().FR.ID_DOMINIO.COD_DOMINIO)){
			return this.toTable(RendicontazionePagamentoSenzaRPT.model().FR.ID_DOMINIO, returnAlias);
		}
		if(field.equals(RendicontazionePagamentoSenzaRPT.model().FR.COD_FLUSSO)){
			return this.toTable(RendicontazionePagamentoSenzaRPT.model().FR, returnAlias);
		}
		if(field.equals(RendicontazionePagamentoSenzaRPT.model().FR.STATO)){
			return this.toTable(RendicontazionePagamentoSenzaRPT.model().FR, returnAlias);
		}
		if(field.equals(RendicontazionePagamentoSenzaRPT.model().FR.DESCRIZIONE_STATO)){
			return this.toTable(RendicontazionePagamentoSenzaRPT.model().FR, returnAlias);
		}
		if(field.equals(RendicontazionePagamentoSenzaRPT.model().FR.IUR)){
			return this.toTable(RendicontazionePagamentoSenzaRPT.model().FR, returnAlias);
		}
		if(field.equals(RendicontazionePagamentoSenzaRPT.model().FR.ANNO_RIFERIMENTO)){
			return this.toTable(RendicontazionePagamentoSenzaRPT.model().FR, returnAlias);
		}
		if(field.equals(RendicontazionePagamentoSenzaRPT.model().FR.DATA_ORA_FLUSSO)){
			return this.toTable(RendicontazionePagamentoSenzaRPT.model().FR, returnAlias);
		}
		if(field.equals(RendicontazionePagamentoSenzaRPT.model().FR.DATA_REGOLAMENTO)){
			return this.toTable(RendicontazionePagamentoSenzaRPT.model().FR, returnAlias);
		}
		if(field.equals(RendicontazionePagamentoSenzaRPT.model().FR.DATA_ACQUISIZIONE)){
			return this.toTable(RendicontazionePagamentoSenzaRPT.model().FR, returnAlias);
		}
		if(field.equals(RendicontazionePagamentoSenzaRPT.model().FR.NUMERO_PAGAMENTI)){
			return this.toTable(RendicontazionePagamentoSenzaRPT.model().FR, returnAlias);
		}
		if(field.equals(RendicontazionePagamentoSenzaRPT.model().FR.IMPORTO_TOTALE_PAGAMENTI)){
			return this.toTable(RendicontazionePagamentoSenzaRPT.model().FR, returnAlias);
		}
		if(field.equals(RendicontazionePagamentoSenzaRPT.model().FR.COD_BIC_RIVERSAMENTO)){
			return this.toTable(RendicontazionePagamentoSenzaRPT.model().FR, returnAlias);
		}
		if(field.equals(RendicontazionePagamentoSenzaRPT.model().FR.XML)){
			return this.toTable(RendicontazionePagamentoSenzaRPT.model().FR, returnAlias);
		}
		if(field.equals(RendicontazionePagamentoSenzaRPT.model().FR_APPLICAZIONE.ID_APPLICAZIONE.COD_APPLICAZIONE)){
			return this.toTable(RendicontazionePagamentoSenzaRPT.model().FR_APPLICAZIONE.ID_APPLICAZIONE, returnAlias);
		}
		if(field.equals(RendicontazionePagamentoSenzaRPT.model().FR_APPLICAZIONE.ID_FR.COD_FLUSSO)){
			return this.toTable(RendicontazionePagamentoSenzaRPT.model().FR_APPLICAZIONE.ID_FR, returnAlias);
		}
		if(field.equals(RendicontazionePagamentoSenzaRPT.model().FR_APPLICAZIONE.ID_FR.ANNO_RIFERIMENTO)){
			return this.toTable(RendicontazionePagamentoSenzaRPT.model().FR_APPLICAZIONE.ID_FR, returnAlias);
		}
		if(field.equals(RendicontazionePagamentoSenzaRPT.model().FR_APPLICAZIONE.NUMERO_PAGAMENTI)){
			return this.toTable(RendicontazionePagamentoSenzaRPT.model().FR_APPLICAZIONE, returnAlias);
		}
		if(field.equals(RendicontazionePagamentoSenzaRPT.model().FR_APPLICAZIONE.IMPORTO_TOTALE_PAGAMENTI)){
			return this.toTable(RendicontazionePagamentoSenzaRPT.model().FR_APPLICAZIONE, returnAlias);
		}
		if(field.equals(RendicontazionePagamentoSenzaRPT.model().RENDICONTAZIONE_SENZA_RPT.ID_FR_APPLICAZIONE.ID_FR.COD_FLUSSO)){
			return this.toTable(RendicontazionePagamentoSenzaRPT.model().RENDICONTAZIONE_SENZA_RPT.ID_FR_APPLICAZIONE.ID_FR, returnAlias);
		}
		if(field.equals(RendicontazionePagamentoSenzaRPT.model().RENDICONTAZIONE_SENZA_RPT.ID_FR_APPLICAZIONE.ID_FR.ANNO_RIFERIMENTO)){
			return this.toTable(RendicontazionePagamentoSenzaRPT.model().RENDICONTAZIONE_SENZA_RPT.ID_FR_APPLICAZIONE.ID_FR, returnAlias);
		}
		if(field.equals(RendicontazionePagamentoSenzaRPT.model().RENDICONTAZIONE_SENZA_RPT.ID_FR_APPLICAZIONE.ID_APPLICAZIONE.COD_APPLICAZIONE)){
			return this.toTable(RendicontazionePagamentoSenzaRPT.model().RENDICONTAZIONE_SENZA_RPT.ID_FR_APPLICAZIONE.ID_APPLICAZIONE, returnAlias);
		}
		if(field.equals(RendicontazionePagamentoSenzaRPT.model().RENDICONTAZIONE_SENZA_RPT.IMPORTO_PAGATO)){
			return this.toTable(RendicontazionePagamentoSenzaRPT.model().RENDICONTAZIONE_SENZA_RPT, returnAlias);
		}
		if(field.equals(RendicontazionePagamentoSenzaRPT.model().RENDICONTAZIONE_SENZA_RPT.ID_IUV.ID_DOMINIO.COD_DOMINIO)){
			return this.toTable(RendicontazionePagamentoSenzaRPT.model().RENDICONTAZIONE_SENZA_RPT.ID_IUV.ID_DOMINIO, returnAlias);
		}
		if(field.equals(RendicontazionePagamentoSenzaRPT.model().RENDICONTAZIONE_SENZA_RPT.ID_IUV.PRG)){
			return this.toTable(RendicontazionePagamentoSenzaRPT.model().RENDICONTAZIONE_SENZA_RPT.ID_IUV, returnAlias);
		}
		if(field.equals(RendicontazionePagamentoSenzaRPT.model().RENDICONTAZIONE_SENZA_RPT.IUR)){
			return this.toTable(RendicontazionePagamentoSenzaRPT.model().RENDICONTAZIONE_SENZA_RPT, returnAlias);
		}
		if(field.equals(RendicontazionePagamentoSenzaRPT.model().RENDICONTAZIONE_SENZA_RPT.RENDICONTAZIONE_DATA)){
			return this.toTable(RendicontazionePagamentoSenzaRPT.model().RENDICONTAZIONE_SENZA_RPT, returnAlias);
		}
		if(field.equals(RendicontazionePagamentoSenzaRPT.model().RENDICONTAZIONE_SENZA_RPT.ID_SINGOLO_VERSAMENTO.ID_VERSAMENTO.COD_VERSAMENTO_ENTE)){
			return this.toTable(RendicontazionePagamentoSenzaRPT.model().RENDICONTAZIONE_SENZA_RPT.ID_SINGOLO_VERSAMENTO.ID_VERSAMENTO, returnAlias);
		}
		if(field.equals(RendicontazionePagamentoSenzaRPT.model().RENDICONTAZIONE_SENZA_RPT.ID_SINGOLO_VERSAMENTO.ID_VERSAMENTO.ID_APPLICAZIONE.COD_APPLICAZIONE)){
			return this.toTable(RendicontazionePagamentoSenzaRPT.model().RENDICONTAZIONE_SENZA_RPT.ID_SINGOLO_VERSAMENTO.ID_VERSAMENTO.ID_APPLICAZIONE, returnAlias);
		}
		if(field.equals(RendicontazionePagamentoSenzaRPT.model().RENDICONTAZIONE_SENZA_RPT.ID_SINGOLO_VERSAMENTO.ID_VERSAMENTO.DEBITORE_IDENTIFICATIVO)){
			return this.toTable(RendicontazionePagamentoSenzaRPT.model().RENDICONTAZIONE_SENZA_RPT.ID_SINGOLO_VERSAMENTO.ID_VERSAMENTO, returnAlias);
		}
		if(field.equals(RendicontazionePagamentoSenzaRPT.model().RENDICONTAZIONE_SENZA_RPT.ID_SINGOLO_VERSAMENTO.ID_VERSAMENTO.DEBITORE_ANAGRAFICA)){
			return this.toTable(RendicontazionePagamentoSenzaRPT.model().RENDICONTAZIONE_SENZA_RPT.ID_SINGOLO_VERSAMENTO.ID_VERSAMENTO, returnAlias);
		}
		if(field.equals(RendicontazionePagamentoSenzaRPT.model().RENDICONTAZIONE_SENZA_RPT.ID_SINGOLO_VERSAMENTO.ID_VERSAMENTO.COD_VERSAMENTO_LOTTO)){
			return this.toTable(RendicontazionePagamentoSenzaRPT.model().RENDICONTAZIONE_SENZA_RPT.ID_SINGOLO_VERSAMENTO.ID_VERSAMENTO, returnAlias);
		}
		if(field.equals(RendicontazionePagamentoSenzaRPT.model().RENDICONTAZIONE_SENZA_RPT.ID_SINGOLO_VERSAMENTO.ID_VERSAMENTO.COD_ANNO_TRIBUTARIO)){
			return this.toTable(RendicontazionePagamentoSenzaRPT.model().RENDICONTAZIONE_SENZA_RPT.ID_SINGOLO_VERSAMENTO.ID_VERSAMENTO, returnAlias);
		}
		if(field.equals(RendicontazionePagamentoSenzaRPT.model().RENDICONTAZIONE_SENZA_RPT.ID_SINGOLO_VERSAMENTO.ID_VERSAMENTO.IMPORTO_TOTALE)){
			return this.toTable(RendicontazionePagamentoSenzaRPT.model().RENDICONTAZIONE_SENZA_RPT.ID_SINGOLO_VERSAMENTO.ID_VERSAMENTO, returnAlias);
		}
		if(field.equals(RendicontazionePagamentoSenzaRPT.model().RENDICONTAZIONE_SENZA_RPT.ID_SINGOLO_VERSAMENTO.COD_SINGOLO_VERSAMENTO_ENTE)){
			return this.toTable(RendicontazionePagamentoSenzaRPT.model().RENDICONTAZIONE_SENZA_RPT.ID_SINGOLO_VERSAMENTO, returnAlias);
		}
		if(field.equals(RendicontazionePagamentoSenzaRPT.model().RENDICONTAZIONE_SENZA_RPT.ID_SINGOLO_VERSAMENTO.ID_TRIBUTO.ID_DOMINIO.COD_DOMINIO)){
			return this.toTable(RendicontazionePagamentoSenzaRPT.model().RENDICONTAZIONE_SENZA_RPT.ID_SINGOLO_VERSAMENTO.ID_TRIBUTO.ID_DOMINIO, returnAlias);
		}
		if(field.equals(RendicontazionePagamentoSenzaRPT.model().RENDICONTAZIONE_SENZA_RPT.ID_SINGOLO_VERSAMENTO.ID_TRIBUTO.ID_TIPO_TRIBUTO.COD_TRIBUTO)){
			return this.toTable(RendicontazionePagamentoSenzaRPT.model().RENDICONTAZIONE_SENZA_RPT.ID_SINGOLO_VERSAMENTO.ID_TRIBUTO.ID_TIPO_TRIBUTO, returnAlias);
		}
		if(field.equals(RendicontazionePagamentoSenzaRPT.model().RENDICONTAZIONE_SENZA_RPT.ID_SINGOLO_VERSAMENTO.NOTE)){
			return this.toTable(RendicontazionePagamentoSenzaRPT.model().RENDICONTAZIONE_SENZA_RPT.ID_SINGOLO_VERSAMENTO, returnAlias);
		}
		if(field.equals(RendicontazionePagamentoSenzaRPT.model().SINGOLO_VERSAMENTO.ID_VERSAMENTO.COD_VERSAMENTO_ENTE)){
			return this.toTable(RendicontazionePagamentoSenzaRPT.model().SINGOLO_VERSAMENTO.ID_VERSAMENTO, returnAlias);
		}
		if(field.equals(RendicontazionePagamentoSenzaRPT.model().SINGOLO_VERSAMENTO.ID_VERSAMENTO.ID_APPLICAZIONE.COD_APPLICAZIONE)){
			return this.toTable(RendicontazionePagamentoSenzaRPT.model().SINGOLO_VERSAMENTO.ID_VERSAMENTO.ID_APPLICAZIONE, returnAlias);
		}
		if(field.equals(RendicontazionePagamentoSenzaRPT.model().SINGOLO_VERSAMENTO.ID_VERSAMENTO.DEBITORE_IDENTIFICATIVO)){
			return this.toTable(RendicontazionePagamentoSenzaRPT.model().SINGOLO_VERSAMENTO.ID_VERSAMENTO, returnAlias);
		}
		if(field.equals(RendicontazionePagamentoSenzaRPT.model().SINGOLO_VERSAMENTO.ID_VERSAMENTO.DEBITORE_ANAGRAFICA)){
			return this.toTable(RendicontazionePagamentoSenzaRPT.model().SINGOLO_VERSAMENTO.ID_VERSAMENTO, returnAlias);
		}
		if(field.equals(RendicontazionePagamentoSenzaRPT.model().SINGOLO_VERSAMENTO.ID_VERSAMENTO.COD_VERSAMENTO_LOTTO)){
			return this.toTable(RendicontazionePagamentoSenzaRPT.model().SINGOLO_VERSAMENTO.ID_VERSAMENTO, returnAlias);
		}
		if(field.equals(RendicontazionePagamentoSenzaRPT.model().SINGOLO_VERSAMENTO.ID_VERSAMENTO.COD_ANNO_TRIBUTARIO)){
			return this.toTable(RendicontazionePagamentoSenzaRPT.model().SINGOLO_VERSAMENTO.ID_VERSAMENTO, returnAlias);
		}
		if(field.equals(RendicontazionePagamentoSenzaRPT.model().SINGOLO_VERSAMENTO.ID_VERSAMENTO.IMPORTO_TOTALE)){
			return this.toTable(RendicontazionePagamentoSenzaRPT.model().SINGOLO_VERSAMENTO.ID_VERSAMENTO, returnAlias);
		}
		if(field.equals(RendicontazionePagamentoSenzaRPT.model().SINGOLO_VERSAMENTO.ID_TRIBUTO.ID_DOMINIO.COD_DOMINIO)){
			return this.toTable(RendicontazionePagamentoSenzaRPT.model().SINGOLO_VERSAMENTO.ID_TRIBUTO.ID_DOMINIO, returnAlias);
		}
		if(field.equals(RendicontazionePagamentoSenzaRPT.model().SINGOLO_VERSAMENTO.ID_TRIBUTO.ID_TIPO_TRIBUTO.COD_TRIBUTO)){
			return this.toTable(RendicontazionePagamentoSenzaRPT.model().SINGOLO_VERSAMENTO.ID_TRIBUTO.ID_TIPO_TRIBUTO, returnAlias);
		}
		if(field.equals(RendicontazionePagamentoSenzaRPT.model().SINGOLO_VERSAMENTO.COD_SINGOLO_VERSAMENTO_ENTE)){
			return this.toTable(RendicontazionePagamentoSenzaRPT.model().SINGOLO_VERSAMENTO, returnAlias);
		}
		if(field.equals(RendicontazionePagamentoSenzaRPT.model().SINGOLO_VERSAMENTO.STATO_SINGOLO_VERSAMENTO)){
			return this.toTable(RendicontazionePagamentoSenzaRPT.model().SINGOLO_VERSAMENTO, returnAlias);
		}
		if(field.equals(RendicontazionePagamentoSenzaRPT.model().SINGOLO_VERSAMENTO.IMPORTO_SINGOLO_VERSAMENTO)){
			return this.toTable(RendicontazionePagamentoSenzaRPT.model().SINGOLO_VERSAMENTO, returnAlias);
		}
		if(field.equals(RendicontazionePagamentoSenzaRPT.model().SINGOLO_VERSAMENTO.ANNO_RIFERIMENTO)){
			return this.toTable(RendicontazionePagamentoSenzaRPT.model().SINGOLO_VERSAMENTO, returnAlias);
		}
		if(field.equals(RendicontazionePagamentoSenzaRPT.model().SINGOLO_VERSAMENTO.TIPO_BOLLO)){
			return this.toTable(RendicontazionePagamentoSenzaRPT.model().SINGOLO_VERSAMENTO, returnAlias);
		}
		if(field.equals(RendicontazionePagamentoSenzaRPT.model().SINGOLO_VERSAMENTO.HASH_DOCUMENTO)){
			return this.toTable(RendicontazionePagamentoSenzaRPT.model().SINGOLO_VERSAMENTO, returnAlias);
		}
		if(field.equals(RendicontazionePagamentoSenzaRPT.model().SINGOLO_VERSAMENTO.PROVINCIA_RESIDENZA)){
			return this.toTable(RendicontazionePagamentoSenzaRPT.model().SINGOLO_VERSAMENTO, returnAlias);
		}
		if(field.equals(RendicontazionePagamentoSenzaRPT.model().SINGOLO_VERSAMENTO.ID_IBAN_ACCREDITO.COD_IBAN)){
			return this.toTable(RendicontazionePagamentoSenzaRPT.model().SINGOLO_VERSAMENTO.ID_IBAN_ACCREDITO, returnAlias);
		}
		if(field.equals(RendicontazionePagamentoSenzaRPT.model().SINGOLO_VERSAMENTO.ID_IBAN_ACCREDITO.ID_DOMINIO.COD_DOMINIO)){
			return this.toTable(RendicontazionePagamentoSenzaRPT.model().SINGOLO_VERSAMENTO.ID_IBAN_ACCREDITO.ID_DOMINIO, returnAlias);
		}
		if(field.equals(RendicontazionePagamentoSenzaRPT.model().SINGOLO_VERSAMENTO.TIPO_CONTABILITA)){
			return this.toTable(RendicontazionePagamentoSenzaRPT.model().SINGOLO_VERSAMENTO, returnAlias);
		}
		if(field.equals(RendicontazionePagamentoSenzaRPT.model().SINGOLO_VERSAMENTO.CODICE_CONTABILITA)){
			return this.toTable(RendicontazionePagamentoSenzaRPT.model().SINGOLO_VERSAMENTO, returnAlias);
		}
		if(field.equals(RendicontazionePagamentoSenzaRPT.model().SINGOLO_VERSAMENTO.NOTE)){
			return this.toTable(RendicontazionePagamentoSenzaRPT.model().SINGOLO_VERSAMENTO, returnAlias);
		}
		if(field.equals(RendicontazionePagamentoSenzaRPT.model().VERSAMENTO.COD_VERSAMENTO_ENTE)){
			return this.toTable(RendicontazionePagamentoSenzaRPT.model().VERSAMENTO, returnAlias);
		}
		if(field.equals(RendicontazionePagamentoSenzaRPT.model().VERSAMENTO.ID_UO.COD_UO)){
			return this.toTable(RendicontazionePagamentoSenzaRPT.model().VERSAMENTO.ID_UO, returnAlias);
		}
		if(field.equals(RendicontazionePagamentoSenzaRPT.model().VERSAMENTO.ID_UO.ID_DOMINIO.COD_DOMINIO)){
			return this.toTable(RendicontazionePagamentoSenzaRPT.model().VERSAMENTO.ID_UO.ID_DOMINIO, returnAlias);
		}
		if(field.equals(RendicontazionePagamentoSenzaRPT.model().VERSAMENTO.ID_APPLICAZIONE.COD_APPLICAZIONE)){
			return this.toTable(RendicontazionePagamentoSenzaRPT.model().VERSAMENTO.ID_APPLICAZIONE, returnAlias);
		}
		if(field.equals(RendicontazionePagamentoSenzaRPT.model().VERSAMENTO.IMPORTO_TOTALE)){
			return this.toTable(RendicontazionePagamentoSenzaRPT.model().VERSAMENTO, returnAlias);
		}
		if(field.equals(RendicontazionePagamentoSenzaRPT.model().VERSAMENTO.STATO_VERSAMENTO)){
			return this.toTable(RendicontazionePagamentoSenzaRPT.model().VERSAMENTO, returnAlias);
		}
		if(field.equals(RendicontazionePagamentoSenzaRPT.model().VERSAMENTO.DESCRIZIONE_STATO)){
			return this.toTable(RendicontazionePagamentoSenzaRPT.model().VERSAMENTO, returnAlias);
		}
		if(field.equals(RendicontazionePagamentoSenzaRPT.model().VERSAMENTO.AGGIORNABILE)){
			return this.toTable(RendicontazionePagamentoSenzaRPT.model().VERSAMENTO, returnAlias);
		}
		if(field.equals(RendicontazionePagamentoSenzaRPT.model().VERSAMENTO.DATA_CREAZIONE)){
			return this.toTable(RendicontazionePagamentoSenzaRPT.model().VERSAMENTO, returnAlias);
		}
		if(field.equals(RendicontazionePagamentoSenzaRPT.model().VERSAMENTO.DATA_SCADENZA)){
			return this.toTable(RendicontazionePagamentoSenzaRPT.model().VERSAMENTO, returnAlias);
		}
		if(field.equals(RendicontazionePagamentoSenzaRPT.model().VERSAMENTO.DATA_ORA_ULTIMO_AGGIORNAMENTO)){
			return this.toTable(RendicontazionePagamentoSenzaRPT.model().VERSAMENTO, returnAlias);
		}
		if(field.equals(RendicontazionePagamentoSenzaRPT.model().VERSAMENTO.CAUSALE_VERSAMENTO)){
			return this.toTable(RendicontazionePagamentoSenzaRPT.model().VERSAMENTO, returnAlias);
		}
		if(field.equals(RendicontazionePagamentoSenzaRPT.model().VERSAMENTO.DEBITORE_IDENTIFICATIVO)){
			return this.toTable(RendicontazionePagamentoSenzaRPT.model().VERSAMENTO, returnAlias);
		}
		if(field.equals(RendicontazionePagamentoSenzaRPT.model().VERSAMENTO.DEBITORE_ANAGRAFICA)){
			return this.toTable(RendicontazionePagamentoSenzaRPT.model().VERSAMENTO, returnAlias);
		}
		if(field.equals(RendicontazionePagamentoSenzaRPT.model().VERSAMENTO.DEBITORE_INDIRIZZO)){
			return this.toTable(RendicontazionePagamentoSenzaRPT.model().VERSAMENTO, returnAlias);
		}
		if(field.equals(RendicontazionePagamentoSenzaRPT.model().VERSAMENTO.DEBITORE_CIVICO)){
			return this.toTable(RendicontazionePagamentoSenzaRPT.model().VERSAMENTO, returnAlias);
		}
		if(field.equals(RendicontazionePagamentoSenzaRPT.model().VERSAMENTO.DEBITORE_CAP)){
			return this.toTable(RendicontazionePagamentoSenzaRPT.model().VERSAMENTO, returnAlias);
		}
		if(field.equals(RendicontazionePagamentoSenzaRPT.model().VERSAMENTO.DEBITORE_LOCALITA)){
			return this.toTable(RendicontazionePagamentoSenzaRPT.model().VERSAMENTO, returnAlias);
		}
		if(field.equals(RendicontazionePagamentoSenzaRPT.model().VERSAMENTO.DEBITORE_PROVINCIA)){
			return this.toTable(RendicontazionePagamentoSenzaRPT.model().VERSAMENTO, returnAlias);
		}
		if(field.equals(RendicontazionePagamentoSenzaRPT.model().VERSAMENTO.DEBITORE_NAZIONE)){
			return this.toTable(RendicontazionePagamentoSenzaRPT.model().VERSAMENTO, returnAlias);
		}
		if(field.equals(RendicontazionePagamentoSenzaRPT.model().VERSAMENTO.COD_LOTTO)){
			return this.toTable(RendicontazionePagamentoSenzaRPT.model().VERSAMENTO, returnAlias);
		}
		if(field.equals(RendicontazionePagamentoSenzaRPT.model().VERSAMENTO.COD_VERSAMENTO_LOTTO)){
			return this.toTable(RendicontazionePagamentoSenzaRPT.model().VERSAMENTO, returnAlias);
		}
		if(field.equals(RendicontazionePagamentoSenzaRPT.model().VERSAMENTO.COD_ANNO_TRIBUTARIO)){
			return this.toTable(RendicontazionePagamentoSenzaRPT.model().VERSAMENTO, returnAlias);
		}
		if(field.equals(RendicontazionePagamentoSenzaRPT.model().VERSAMENTO.COD_BUNDLEKEY)){
			return this.toTable(RendicontazionePagamentoSenzaRPT.model().VERSAMENTO, returnAlias);
		}


		return super.toTable(field,returnAlias);
		
	}

	@Override
	public String toTable(IModel<?> model,boolean returnAlias) throws ExpressionException {
		
		// In the case of table with alias, using parameter returnAlias​​, 
		// it is possible to drive the choice whether to return only the alias or 
		// the full definition of the table containing the alias
		
		if(model.equals(RendicontazionePagamentoSenzaRPT.model())){
			return null;
		}
		if(model.equals(RendicontazionePagamentoSenzaRPT.model().FR)){
			return "fr";
		}
		if(model.equals(RendicontazionePagamentoSenzaRPT.model().FR.ID_PSP)){
			return "psp";
		}
		if(model.equals(RendicontazionePagamentoSenzaRPT.model().FR.ID_DOMINIO)){
			return "domini";
		}
		if(model.equals(RendicontazionePagamentoSenzaRPT.model().FR_APPLICAZIONE)){
			return "fr_applicazioni";
		}
		if(model.equals(RendicontazionePagamentoSenzaRPT.model().FR_APPLICAZIONE.ID_APPLICAZIONE)){
			return "applicazioni";
		}
		if(model.equals(RendicontazionePagamentoSenzaRPT.model().FR_APPLICAZIONE.ID_FR)){
			return "fr";
		}
		if(model.equals(RendicontazionePagamentoSenzaRPT.model().RENDICONTAZIONE_SENZA_RPT)){
			return "rendicontazioni_senza_rpt";
		}
		if(model.equals(RendicontazionePagamentoSenzaRPT.model().RENDICONTAZIONE_SENZA_RPT.ID_FR_APPLICAZIONE)){
			return "fr_applicazioni";
		}
		if(model.equals(RendicontazionePagamentoSenzaRPT.model().RENDICONTAZIONE_SENZA_RPT.ID_FR_APPLICAZIONE.ID_FR)){
			return "id_fr";
		}
		if(model.equals(RendicontazionePagamentoSenzaRPT.model().RENDICONTAZIONE_SENZA_RPT.ID_FR_APPLICAZIONE.ID_APPLICAZIONE)){
			return "id_applicazione";
		}
		if(model.equals(RendicontazionePagamentoSenzaRPT.model().RENDICONTAZIONE_SENZA_RPT.ID_IUV)){
			return "iuv";
		}
		if(model.equals(RendicontazionePagamentoSenzaRPT.model().RENDICONTAZIONE_SENZA_RPT.ID_IUV.ID_DOMINIO)){
			return "id_dominio";
		}
		if(model.equals(RendicontazionePagamentoSenzaRPT.model().RENDICONTAZIONE_SENZA_RPT.ID_SINGOLO_VERSAMENTO)){
			return "singoli_versamenti";
		}
		if(model.equals(RendicontazionePagamentoSenzaRPT.model().RENDICONTAZIONE_SENZA_RPT.ID_SINGOLO_VERSAMENTO.ID_VERSAMENTO)){
			return "id_versamento";
		}
		if(model.equals(RendicontazionePagamentoSenzaRPT.model().RENDICONTAZIONE_SENZA_RPT.ID_SINGOLO_VERSAMENTO.ID_VERSAMENTO.ID_APPLICAZIONE)){
			return "id_applicazione";
		}
		if(model.equals(RendicontazionePagamentoSenzaRPT.model().RENDICONTAZIONE_SENZA_RPT.ID_SINGOLO_VERSAMENTO.ID_TRIBUTO)){
			return "id_tributo";
		}
		if(model.equals(RendicontazionePagamentoSenzaRPT.model().RENDICONTAZIONE_SENZA_RPT.ID_SINGOLO_VERSAMENTO.ID_TRIBUTO.ID_DOMINIO)){
			return "id_dominio";
		}
		if(model.equals(RendicontazionePagamentoSenzaRPT.model().RENDICONTAZIONE_SENZA_RPT.ID_SINGOLO_VERSAMENTO.ID_TRIBUTO.ID_TIPO_TRIBUTO)){
			return "id_tipo_tributo";
		}
		if(model.equals(RendicontazionePagamentoSenzaRPT.model().SINGOLO_VERSAMENTO)){
			return "singoli_versamenti";
		}
		if(model.equals(RendicontazionePagamentoSenzaRPT.model().SINGOLO_VERSAMENTO.ID_VERSAMENTO)){
			return "versamenti";
		}
		if(model.equals(RendicontazionePagamentoSenzaRPT.model().SINGOLO_VERSAMENTO.ID_VERSAMENTO.ID_APPLICAZIONE)){
			return "id_applicazione";
		}
		if(model.equals(RendicontazionePagamentoSenzaRPT.model().SINGOLO_VERSAMENTO.ID_TRIBUTO)){
			return "tributi";
		}
		if(model.equals(RendicontazionePagamentoSenzaRPT.model().SINGOLO_VERSAMENTO.ID_TRIBUTO.ID_DOMINIO)){
			return "id_dominio";
		}
		if(model.equals(RendicontazionePagamentoSenzaRPT.model().SINGOLO_VERSAMENTO.ID_TRIBUTO.ID_TIPO_TRIBUTO)){
			return "id_tipo_tributo";
		}
		if(model.equals(RendicontazionePagamentoSenzaRPT.model().SINGOLO_VERSAMENTO.ID_IBAN_ACCREDITO)){
			return "iban_accredito";
		}
		if(model.equals(RendicontazionePagamentoSenzaRPT.model().SINGOLO_VERSAMENTO.ID_IBAN_ACCREDITO.ID_DOMINIO)){
			return "id_dominio";
		}
		if(model.equals(RendicontazionePagamentoSenzaRPT.model().VERSAMENTO)){
			return "versamenti";
		}
		if(model.equals(RendicontazionePagamentoSenzaRPT.model().VERSAMENTO.ID_UO)){
			return "uo";
		}
		if(model.equals(RendicontazionePagamentoSenzaRPT.model().VERSAMENTO.ID_UO.ID_DOMINIO)){
			return "id_dominio";
		}
		if(model.equals(RendicontazionePagamentoSenzaRPT.model().VERSAMENTO.ID_APPLICAZIONE)){
			return "applicazioni";
		}


		return super.toTable(model,returnAlias);
		
	}

}
