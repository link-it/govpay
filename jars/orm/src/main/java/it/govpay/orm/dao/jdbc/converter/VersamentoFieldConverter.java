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

import it.govpay.orm.Versamento;


/**     
 * VersamentoFieldConverter
 *
 * @author Giovanni Bussu (bussu@link.it)
 * @author Lorenzo Nardi (nardi@link.it)
 * @author $Author$
 * @version $Rev$, $Date$
 */
public class VersamentoFieldConverter extends AbstractSQLFieldConverter {

	private TipiDatabase databaseType;
	
	public VersamentoFieldConverter(String databaseType){
		this.databaseType = TipiDatabase.toEnumConstant(databaseType);
	}
	public VersamentoFieldConverter(TipiDatabase databaseType){
		this.databaseType = databaseType;
	}


	@Override
	public IModel<?> getRootModel() throws ExpressionException {
		return Versamento.model();
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
		
		if(field.equals(Versamento.model().COD_VERSAMENTO_ENTE)){
			if(appendTablePrefix){
				return this.toAliasTable(field)+".cod_versamento_ente";
			}else{
				return "cod_versamento_ente";
			}
		}
		if(field.equals(Versamento.model().NOME)){
			if(appendTablePrefix){
				return this.toAliasTable(field)+".nome";
			}else{
				return "nome";
			}
		}
		if(field.equals(Versamento.model().ID_DOMINIO.COD_DOMINIO)){
			if(appendTablePrefix){
				return this.toAliasTable(field)+".cod_dominio";
			}else{
				return "cod_dominio";
			}
		}
		if(field.equals(Versamento.model().ID_UO.COD_UO)){
			if(appendTablePrefix){
				return this.toAliasTable(field)+".cod_uo";
			}else{
				return "cod_uo";
			}
		}
		if(field.equals(Versamento.model().ID_UO.ID_DOMINIO.COD_DOMINIO)){
			if(appendTablePrefix){
				return this.toAliasTable(field)+".cod_dominio";
			}else{
				return "cod_dominio";
			}
		}
		if(field.equals(Versamento.model().ID_APPLICAZIONE.COD_APPLICAZIONE)){
			if(appendTablePrefix){
				return this.toAliasTable(field)+".cod_applicazione";
			}else{
				return "cod_applicazione";
			}
		}
		if(field.equals(Versamento.model().ID_PAGAMENTO_PORTALE.ID_SESSIONE)){
			if(appendTablePrefix){
				return this.toAliasTable(field)+".id_sessione";
			}else{
				return "id_sessione";
			}
		}
		if(field.equals(Versamento.model().IUV.IUV)){
			if(appendTablePrefix){
				return this.toAliasTable(field)+".iuv";
			}else{
				return "iuv";
			}
		}
		if(field.equals(Versamento.model().IMPORTO_TOTALE)){
			if(appendTablePrefix){
				return this.toAliasTable(field)+".importo_totale";
			}else{
				return "importo_totale";
			}
		}
		if(field.equals(Versamento.model().STATO_VERSAMENTO)){
			if(appendTablePrefix){
				return this.toAliasTable(field)+".stato_versamento";
			}else{
				return "stato_versamento";
			}
		}
		if(field.equals(Versamento.model().DESCRIZIONE_STATO)){
			if(appendTablePrefix){
				return this.toAliasTable(field)+".descrizione_stato";
			}else{
				return "descrizione_stato";
			}
		}
		if(field.equals(Versamento.model().AGGIORNABILE)){
			if(appendTablePrefix){
				return this.toAliasTable(field)+".aggiornabile";
			}else{
				return "aggiornabile";
			}
		}
		if(field.equals(Versamento.model().DATA_CREAZIONE)){
			if(appendTablePrefix){
				return this.toAliasTable(field)+".data_creazione";
			}else{
				return "data_creazione";
			}
		}
		if(field.equals(Versamento.model().DATA_VALIDITA)){
			if(appendTablePrefix){
				return this.toAliasTable(field)+".data_validita";
			}else{
				return "data_validita";
			}
		}
		if(field.equals(Versamento.model().DATA_SCADENZA)){
			if(appendTablePrefix){
				return this.toAliasTable(field)+".data_scadenza";
			}else{
				return "data_scadenza";
			}
		}
		if(field.equals(Versamento.model().DATA_ORA_ULTIMO_AGGIORNAMENTO)){
			if(appendTablePrefix){
				return this.toAliasTable(field)+".data_ora_ultimo_aggiornamento";
			}else{
				return "data_ora_ultimo_aggiornamento";
			}
		}
		if(field.equals(Versamento.model().CAUSALE_VERSAMENTO)){
			if(appendTablePrefix){
				return this.toAliasTable(field)+".causale_versamento";
			}else{
				return "causale_versamento";
			}
		}
		if(field.equals(Versamento.model().DEBITORE_TIPO)){
			if(appendTablePrefix){
				return this.toAliasTable(field)+".debitore_tipo";
			}else{
				return "debitore_tipo";
			}
		}
		if(field.equals(Versamento.model().DEBITORE_IDENTIFICATIVO)){
			if(appendTablePrefix){
				return this.toAliasTable(field)+".debitore_identificativo";
			}else{
				return "debitore_identificativo";
			}
		}
		if(field.equals(Versamento.model().DEBITORE_ANAGRAFICA)){
			if(appendTablePrefix){
				return this.toAliasTable(field)+".debitore_anagrafica";
			}else{
				return "debitore_anagrafica";
			}
		}
		if(field.equals(Versamento.model().DEBITORE_INDIRIZZO)){
			if(appendTablePrefix){
				return this.toAliasTable(field)+".debitore_indirizzo";
			}else{
				return "debitore_indirizzo";
			}
		}
		if(field.equals(Versamento.model().DEBITORE_CIVICO)){
			if(appendTablePrefix){
				return this.toAliasTable(field)+".debitore_civico";
			}else{
				return "debitore_civico";
			}
		}
		if(field.equals(Versamento.model().DEBITORE_CAP)){
			if(appendTablePrefix){
				return this.toAliasTable(field)+".debitore_cap";
			}else{
				return "debitore_cap";
			}
		}
		if(field.equals(Versamento.model().DEBITORE_LOCALITA)){
			if(appendTablePrefix){
				return this.toAliasTable(field)+".debitore_localita";
			}else{
				return "debitore_localita";
			}
		}
		if(field.equals(Versamento.model().DEBITORE_PROVINCIA)){
			if(appendTablePrefix){
				return this.toAliasTable(field)+".debitore_provincia";
			}else{
				return "debitore_provincia";
			}
		}
		if(field.equals(Versamento.model().DEBITORE_NAZIONE)){
			if(appendTablePrefix){
				return this.toAliasTable(field)+".debitore_nazione";
			}else{
				return "debitore_nazione";
			}
		}
		if(field.equals(Versamento.model().DEBITORE_EMAIL)){
			if(appendTablePrefix){
				return this.toAliasTable(field)+".debitore_email";
			}else{
				return "debitore_email";
			}
		}
		if(field.equals(Versamento.model().DEBITORE_TELEFONO)){
			if(appendTablePrefix){
				return this.toAliasTable(field)+".debitore_telefono";
			}else{
				return "debitore_telefono";
			}
		}
		if(field.equals(Versamento.model().DEBITORE_CELLULARE)){
			if(appendTablePrefix){
				return this.toAliasTable(field)+".debitore_cellulare";
			}else{
				return "debitore_cellulare";
			}
		}
		if(field.equals(Versamento.model().DEBITORE_FAX)){
			if(appendTablePrefix){
				return this.toAliasTable(field)+".debitore_fax";
			}else{
				return "debitore_fax";
			}
		}
		if(field.equals(Versamento.model().TASSONOMIA_AVVISO)){
			if(appendTablePrefix){
				return this.toAliasTable(field)+".tassonomia_avviso";
			}else{
				return "tassonomia_avviso";
			}
		}
		if(field.equals(Versamento.model().TASSONOMIA)){
			if(appendTablePrefix){
				return this.toAliasTable(field)+".tassonomia";
			}else{
				return "tassonomia";
			}
		}
		if(field.equals(Versamento.model().COD_LOTTO)){
			if(appendTablePrefix){
				return this.toAliasTable(field)+".cod_lotto";
			}else{
				return "cod_lotto";
			}
		}
		if(field.equals(Versamento.model().COD_VERSAMENTO_LOTTO)){
			if(appendTablePrefix){
				return this.toAliasTable(field)+".cod_versamento_lotto";
			}else{
				return "cod_versamento_lotto";
			}
		}
		if(field.equals(Versamento.model().COD_ANNO_TRIBUTARIO)){
			if(appendTablePrefix){
				return this.toAliasTable(field)+".cod_anno_tributario";
			}else{
				return "cod_anno_tributario";
			}
		}
		if(field.equals(Versamento.model().COD_BUNDLEKEY)){
			if(appendTablePrefix){
				return this.toAliasTable(field)+".cod_bundlekey";
			}else{
				return "cod_bundlekey";
			}
		}
		if(field.equals(Versamento.model().DATI_ALLEGATI)){
			if(appendTablePrefix){
				return this.toAliasTable(field)+".dati_allegati";
			}else{
				return "dati_allegati";
			}
		}
		if(field.equals(Versamento.model().INCASSO)){
			if(appendTablePrefix){
				return this.toAliasTable(field)+".incasso";
			}else{
				return "incasso";
			}
		}
		if(field.equals(Versamento.model().ANOMALIE)){
			if(appendTablePrefix){
				return this.toAliasTable(field)+".anomalie";
			}else{
				return "anomalie";
			}
		}
		if(field.equals(Versamento.model().IUV_VERSAMENTO)){
			if(appendTablePrefix){
				return this.toAliasTable(field)+".iuv_versamento";
			}else{
				return "iuv_versamento";
			}
		}
		if(field.equals(Versamento.model().NUMERO_AVVISO)){
			if(appendTablePrefix){
				return this.toAliasTable(field)+".numero_avviso";
			}else{
				return "numero_avviso";
			}
		}
		if(field.equals(Versamento.model().AVVISATURA_ABILITATA)){
			if(appendTablePrefix){
				return this.toAliasTable(field)+".avvisatura_abilitata";
			}else{
				return "avvisatura_abilitata";
			}
		}
		if(field.equals(Versamento.model().AVVISATURA_DA_INVIARE)){
			if(appendTablePrefix){
				return this.toAliasTable(field)+".avvisatura_da_inviare";
			}else{
				return "avvisatura_da_inviare";
			}
		}
		if(field.equals(Versamento.model().AVVISATURA_OPERAZIONE)){
			if(appendTablePrefix){
				return this.toAliasTable(field)+".avvisatura_operazione";
			}else{
				return "avvisatura_operazione";
			}
		}
		if(field.equals(Versamento.model().AVVISATURA_MODALITA)){
			if(appendTablePrefix){
				return this.toAliasTable(field)+".avvisatura_modalita";
			}else{
				return "avvisatura_modalita";
			}
		}
		if(field.equals(Versamento.model().AVVISATURA_TIPO_PAGAMENTO)){
			if(appendTablePrefix){
				return this.toAliasTable(field)+".avvisatura_tipo_pagamento";
			}else{
				return "avvisatura_tipo_pagamento";
			}
		}
		if(field.equals(Versamento.model().AVVISATURA_COD_AVVISATURA)){
			if(appendTablePrefix){
				return this.toAliasTable(field)+".avvisatura_cod_avvisatura";
			}else{
				return "avvisatura_cod_avvisatura";
			}
		}
		if(field.equals(Versamento.model().ID_TRACCIATO_AVVISATURA.ID_TRACCIATO)){
			if(appendTablePrefix){
				return this.toAliasTable(field)+".id_tracciato";
			}else{
				return "id_tracciato";
			}
		}
		if(field.equals(Versamento.model().ACK)){
			if(appendTablePrefix){
				return this.toAliasTable(field)+".ack";
			}else{
				return "ack";
			}
		}
		if(field.equals(Versamento.model().ANOMALO)){
			if(appendTablePrefix){
				return this.toAliasTable(field)+".anomalo";
			}else{
				return "anomalo";
			}
		}


		return super.toColumn(field,returnAlias,appendTablePrefix);
		
	}
	
	@Override
	public String toTable(IField field,boolean returnAlias) throws ExpressionException {
		
		// In the case of table with alias, using parameter returnAlias​​, 
		// it is possible to drive the choice whether to return only the alias or 
		// the full definition of the table containing the alias
		
		if(field.equals(Versamento.model().COD_VERSAMENTO_ENTE)){
			return this.toTable(Versamento.model(), returnAlias);
		}
		if(field.equals(Versamento.model().NOME)){
			return this.toTable(Versamento.model(), returnAlias);
		}
		if(field.equals(Versamento.model().ID_DOMINIO.COD_DOMINIO)){
			return this.toTable(Versamento.model().ID_DOMINIO, returnAlias);
		}
		if(field.equals(Versamento.model().ID_UO.COD_UO)){
			return this.toTable(Versamento.model().ID_UO, returnAlias);
		}
		if(field.equals(Versamento.model().ID_UO.ID_DOMINIO.COD_DOMINIO)){
			return this.toTable(Versamento.model().ID_UO.ID_DOMINIO, returnAlias);
		}
		if(field.equals(Versamento.model().ID_APPLICAZIONE.COD_APPLICAZIONE)){
			return this.toTable(Versamento.model().ID_APPLICAZIONE, returnAlias);
		}
		if(field.equals(Versamento.model().ID_PAGAMENTO_PORTALE.ID_SESSIONE)){
			return this.toTable(Versamento.model().ID_PAGAMENTO_PORTALE, returnAlias);
		}
		if(field.equals(Versamento.model().IUV.IUV)){
			return this.toTable(Versamento.model().IUV, returnAlias);
		}
		if(field.equals(Versamento.model().IMPORTO_TOTALE)){
			return this.toTable(Versamento.model(), returnAlias);
		}
		if(field.equals(Versamento.model().STATO_VERSAMENTO)){
			return this.toTable(Versamento.model(), returnAlias);
		}
		if(field.equals(Versamento.model().DESCRIZIONE_STATO)){
			return this.toTable(Versamento.model(), returnAlias);
		}
		if(field.equals(Versamento.model().AGGIORNABILE)){
			return this.toTable(Versamento.model(), returnAlias);
		}
		if(field.equals(Versamento.model().DATA_CREAZIONE)){
			return this.toTable(Versamento.model(), returnAlias);
		}
		if(field.equals(Versamento.model().DATA_VALIDITA)){
			return this.toTable(Versamento.model(), returnAlias);
		}
		if(field.equals(Versamento.model().DATA_SCADENZA)){
			return this.toTable(Versamento.model(), returnAlias);
		}
		if(field.equals(Versamento.model().DATA_ORA_ULTIMO_AGGIORNAMENTO)){
			return this.toTable(Versamento.model(), returnAlias);
		}
		if(field.equals(Versamento.model().CAUSALE_VERSAMENTO)){
			return this.toTable(Versamento.model(), returnAlias);
		}
		if(field.equals(Versamento.model().DEBITORE_TIPO)){
			return this.toTable(Versamento.model(), returnAlias);
		}
		if(field.equals(Versamento.model().DEBITORE_IDENTIFICATIVO)){
			return this.toTable(Versamento.model(), returnAlias);
		}
		if(field.equals(Versamento.model().DEBITORE_ANAGRAFICA)){
			return this.toTable(Versamento.model(), returnAlias);
		}
		if(field.equals(Versamento.model().DEBITORE_INDIRIZZO)){
			return this.toTable(Versamento.model(), returnAlias);
		}
		if(field.equals(Versamento.model().DEBITORE_CIVICO)){
			return this.toTable(Versamento.model(), returnAlias);
		}
		if(field.equals(Versamento.model().DEBITORE_CAP)){
			return this.toTable(Versamento.model(), returnAlias);
		}
		if(field.equals(Versamento.model().DEBITORE_LOCALITA)){
			return this.toTable(Versamento.model(), returnAlias);
		}
		if(field.equals(Versamento.model().DEBITORE_PROVINCIA)){
			return this.toTable(Versamento.model(), returnAlias);
		}
		if(field.equals(Versamento.model().DEBITORE_NAZIONE)){
			return this.toTable(Versamento.model(), returnAlias);
		}
		if(field.equals(Versamento.model().DEBITORE_EMAIL)){
			return this.toTable(Versamento.model(), returnAlias);
		}
		if(field.equals(Versamento.model().DEBITORE_TELEFONO)){
			return this.toTable(Versamento.model(), returnAlias);
		}
		if(field.equals(Versamento.model().DEBITORE_CELLULARE)){
			return this.toTable(Versamento.model(), returnAlias);
		}
		if(field.equals(Versamento.model().DEBITORE_FAX)){
			return this.toTable(Versamento.model(), returnAlias);
		}
		if(field.equals(Versamento.model().TASSONOMIA_AVVISO)){
			return this.toTable(Versamento.model(), returnAlias);
		}
		if(field.equals(Versamento.model().TASSONOMIA)){
			return this.toTable(Versamento.model(), returnAlias);
		}
		if(field.equals(Versamento.model().COD_LOTTO)){
			return this.toTable(Versamento.model(), returnAlias);
		}
		if(field.equals(Versamento.model().COD_VERSAMENTO_LOTTO)){
			return this.toTable(Versamento.model(), returnAlias);
		}
		if(field.equals(Versamento.model().COD_ANNO_TRIBUTARIO)){
			return this.toTable(Versamento.model(), returnAlias);
		}
		if(field.equals(Versamento.model().COD_BUNDLEKEY)){
			return this.toTable(Versamento.model(), returnAlias);
		}
		if(field.equals(Versamento.model().DATI_ALLEGATI)){
			return this.toTable(Versamento.model(), returnAlias);
		}
		if(field.equals(Versamento.model().INCASSO)){
			return this.toTable(Versamento.model(), returnAlias);
		}
		if(field.equals(Versamento.model().ANOMALIE)){
			return this.toTable(Versamento.model(), returnAlias);
		}
		if(field.equals(Versamento.model().IUV_VERSAMENTO)){
			return this.toTable(Versamento.model(), returnAlias);
		}
		if(field.equals(Versamento.model().NUMERO_AVVISO)){
			return this.toTable(Versamento.model(), returnAlias);
		}
		if(field.equals(Versamento.model().AVVISATURA_ABILITATA)){
			return this.toTable(Versamento.model(), returnAlias);
		}
		if(field.equals(Versamento.model().AVVISATURA_DA_INVIARE)){
			return this.toTable(Versamento.model(), returnAlias);
		}
		if(field.equals(Versamento.model().AVVISATURA_OPERAZIONE)){
			return this.toTable(Versamento.model(), returnAlias);
		}
		if(field.equals(Versamento.model().AVVISATURA_MODALITA)){
			return this.toTable(Versamento.model(), returnAlias);
		}
		if(field.equals(Versamento.model().AVVISATURA_TIPO_PAGAMENTO)){
			return this.toTable(Versamento.model(), returnAlias);
		}
		if(field.equals(Versamento.model().AVVISATURA_COD_AVVISATURA)){
			return this.toTable(Versamento.model(), returnAlias);
		}
		if(field.equals(Versamento.model().ID_TRACCIATO_AVVISATURA.ID_TRACCIATO)){
			return this.toTable(Versamento.model().ID_TRACCIATO_AVVISATURA, returnAlias);
		}
		if(field.equals(Versamento.model().ACK)){
			return this.toTable(Versamento.model(), returnAlias);
		}
		if(field.equals(Versamento.model().ANOMALO)){
			return this.toTable(Versamento.model(), returnAlias);
		}


		return super.toTable(field,returnAlias);
		
	}

	@Override
	public String toTable(IModel<?> model,boolean returnAlias) throws ExpressionException {
		
		// In the case of table with alias, using parameter returnAlias​​, 
		// it is possible to drive the choice whether to return only the alias or 
		// the full definition of the table containing the alias
		
		if(model.equals(Versamento.model())){
			return "versamenti";
		}
		if(model.equals(Versamento.model().ID_DOMINIO)){
			return "domini";
		}
		if(model.equals(Versamento.model().ID_UO)){
			return "uo";
		}
		if(model.equals(Versamento.model().ID_UO.ID_DOMINIO)){
			return "domini";
		}
		if(model.equals(Versamento.model().ID_APPLICAZIONE)){
			return "applicazioni";
		}
		if(model.equals(Versamento.model().ID_PAGAMENTO_PORTALE)){
			return "pagamenti_portale";
		}
		if(model.equals(Versamento.model().IUV)){
			return "iuv";
		}
		if(model.equals(Versamento.model().ID_TRACCIATO_AVVISATURA)){
			return "tracciati";
		}


		return super.toTable(model,returnAlias);
		
	}

}
