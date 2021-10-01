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

import it.govpay.orm.FR;


/**     
 * FRFieldConverter
 *
 * @author Giovanni Bussu (bussu@link.it)
 * @author Lorenzo Nardi (nardi@link.it)
 * @author $Author$
 * @version $Rev$, $Date$
 */
public class FRFieldConverter extends AbstractSQLFieldConverter {

	private TipiDatabase databaseType;
	
	public FRFieldConverter(String databaseType){
		this.databaseType = TipiDatabase.toEnumConstant(databaseType);
	}
	public FRFieldConverter(TipiDatabase databaseType){
		this.databaseType = databaseType;
	}


	@Override
	public IModel<?> getRootModel() throws ExpressionException {
		return FR.model();
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
		
		if(field.equals(FR.model().COD_PSP)){
			if(appendTablePrefix){
				return this.toAliasTable(field)+".cod_psp";
			}else{
				return "cod_psp";
			}
		}
		if(field.equals(FR.model().COD_DOMINIO)){
			if(appendTablePrefix){
				return this.toAliasTable(field)+".cod_dominio";
			}else{
				return "cod_dominio";
			}
		}
		if(field.equals(FR.model().COD_FLUSSO)){
			if(appendTablePrefix){
				return this.toAliasTable(field)+".cod_flusso";
			}else{
				return "cod_flusso";
			}
		}
		if(field.equals(FR.model().STATO)){
			if(appendTablePrefix){
				return this.toAliasTable(field)+".stato";
			}else{
				return "stato";
			}
		}
		if(field.equals(FR.model().DESCRIZIONE_STATO)){
			if(appendTablePrefix){
				return this.toAliasTable(field)+".descrizione_stato";
			}else{
				return "descrizione_stato";
			}
		}
		if(field.equals(FR.model().IUR)){
			if(appendTablePrefix){
				return this.toAliasTable(field)+".iur";
			}else{
				return "iur";
			}
		}
		if(field.equals(FR.model().DATA_ORA_FLUSSO)){
			if(appendTablePrefix){
				return this.toAliasTable(field)+".data_ora_flusso";
			}else{
				return "data_ora_flusso";
			}
		}
		if(field.equals(FR.model().DATA_REGOLAMENTO)){
			if(appendTablePrefix){
				return this.toAliasTable(field)+".data_regolamento";
			}else{
				return "data_regolamento";
			}
		}
		if(field.equals(FR.model().DATA_ACQUISIZIONE)){
			if(appendTablePrefix){
				return this.toAliasTable(field)+".data_acquisizione";
			}else{
				return "data_acquisizione";
			}
		}
		if(field.equals(FR.model().NUMERO_PAGAMENTI)){
			if(appendTablePrefix){
				return this.toAliasTable(field)+".numero_pagamenti";
			}else{
				return "numero_pagamenti";
			}
		}
		if(field.equals(FR.model().IMPORTO_TOTALE_PAGAMENTI)){
			if(appendTablePrefix){
				return this.toAliasTable(field)+".importo_totale_pagamenti";
			}else{
				return "importo_totale_pagamenti";
			}
		}
		if(field.equals(FR.model().COD_BIC_RIVERSAMENTO)){
			if(appendTablePrefix){
				return this.toAliasTable(field)+".cod_bic_riversamento";
			}else{
				return "cod_bic_riversamento";
			}
		}
		if(field.equals(FR.model().XML)){
			if(appendTablePrefix){
				return this.toAliasTable(field)+".xml";
			}else{
				return "xml";
			}
		}
		if(field.equals(FR.model().ID_SINGOLO_VERSAMENTO.ID_VERSAMENTO.COD_VERSAMENTO_ENTE)){
			if(appendTablePrefix){
				return this.toAliasTable(field)+".cod_versamento_ente";
			}else{
				return "cod_versamento_ente";
			}
		}
		if(field.equals(FR.model().ID_SINGOLO_VERSAMENTO.ID_VERSAMENTO.ID_APPLICAZIONE.COD_APPLICAZIONE)){
			if(appendTablePrefix){
				return this.toAliasTable(field)+".cod_applicazione";
			}else{
				return "cod_applicazione";
			}
		}
		if(field.equals(FR.model().ID_SINGOLO_VERSAMENTO.ID_VERSAMENTO.SRC_DEBITORE_IDENTIFICATIVO)){
			if(appendTablePrefix){
				return this.toAliasTable(field)+".src_debitore_identificativo";
			}else{
				return "src_debitore_identificativo";
			}
		}
		if(field.equals(FR.model().ID_SINGOLO_VERSAMENTO.ID_VERSAMENTO.DEBITORE_ANAGRAFICA)){
			if(appendTablePrefix){
				return this.toAliasTable(field)+".debitore_anagrafica";
			}else{
				return "debitore_anagrafica";
			}
		}
		if(field.equals(FR.model().ID_SINGOLO_VERSAMENTO.ID_VERSAMENTO.COD_VERSAMENTO_LOTTO)){
			if(appendTablePrefix){
				return this.toAliasTable(field)+".cod_versamento_lotto";
			}else{
				return "cod_versamento_lotto";
			}
		}
		if(field.equals(FR.model().ID_SINGOLO_VERSAMENTO.ID_VERSAMENTO.COD_ANNO_TRIBUTARIO)){
			if(appendTablePrefix){
				return this.toAliasTable(field)+".cod_anno_tributario";
			}else{
				return "cod_anno_tributario";
			}
		}
		if(field.equals(FR.model().ID_SINGOLO_VERSAMENTO.ID_VERSAMENTO.IMPORTO_TOTALE)){
			if(appendTablePrefix){
				return this.toAliasTable(field)+".importo_totale";
			}else{
				return "importo_totale";
			}
		}
		if(field.equals(FR.model().ID_SINGOLO_VERSAMENTO.ID_VERSAMENTO.CAUSALE_VERSAMENTO)){
			if(appendTablePrefix){
				return this.toAliasTable(field)+".causale_versamento";
			}else{
				return "causale_versamento";
			}
		}
		if(field.equals(FR.model().ID_SINGOLO_VERSAMENTO.ID_VERSAMENTO.STATO_VERSAMENTO)){
			if(appendTablePrefix){
				return this.toAliasTable(field)+".stato_versamento";
			}else{
				return "stato_versamento";
			}
		}
		if(field.equals(FR.model().ID_SINGOLO_VERSAMENTO.ID_VERSAMENTO.ID_UO.COD_UO)){
			if(appendTablePrefix){
				return this.toAliasTable(field)+".cod_uo";
			}else{
				return "cod_uo";
			}
		}
		if(field.equals(FR.model().ID_SINGOLO_VERSAMENTO.ID_VERSAMENTO.ID_UO.ID_DOMINIO.COD_DOMINIO)){
			if(appendTablePrefix){
				return this.toAliasTable(field)+".cod_dominio";
			}else{
				return "cod_dominio";
			}
		}
		if(field.equals(FR.model().ID_SINGOLO_VERSAMENTO.ID_VERSAMENTO.ID_TIPO_VERSAMENTO.COD_TIPO_VERSAMENTO)){
			if(appendTablePrefix){
				return this.toAliasTable(field)+".cod_tipo_versamento";
			}else{
				return "cod_tipo_versamento";
			}
		}
		if(field.equals(FR.model().ID_SINGOLO_VERSAMENTO.ID_VERSAMENTO.DIVISIONE)){
			if(appendTablePrefix){
				return this.toAliasTable(field)+".divisione";
			}else{
				return "divisione";
			}
		}
		if(field.equals(FR.model().ID_SINGOLO_VERSAMENTO.ID_VERSAMENTO.DIREZIONE)){
			if(appendTablePrefix){
				return this.toAliasTable(field)+".direzione";
			}else{
				return "direzione";
			}
		}
		if(field.equals(FR.model().ID_SINGOLO_VERSAMENTO.ID_VERSAMENTO.TASSONOMIA)){
			if(appendTablePrefix){
				return this.toAliasTable(field)+".tassonomia";
			}else{
				return "tassonomia";
			}
		}
		if(field.equals(FR.model().ID_SINGOLO_VERSAMENTO.ID_VERSAMENTO.TIPO)){
			if(appendTablePrefix){
				return this.toAliasTable(field)+".tipo";
			}else{
				return "tipo";
			}
		}
		if(field.equals(FR.model().ID_SINGOLO_VERSAMENTO.COD_SINGOLO_VERSAMENTO_ENTE)){
			if(appendTablePrefix){
				return this.toAliasTable(field)+".cod_singolo_versamento_ente";
			}else{
				return "cod_singolo_versamento_ente";
			}
		}
		if(field.equals(FR.model().ID_SINGOLO_VERSAMENTO.INDICE_DATI)){
			if(appendTablePrefix){
				return this.toAliasTable(field)+".indice_dati";
			}else{
				return "indice_dati";
			}
		}
		if(field.equals(FR.model().ID_SINGOLO_VERSAMENTO.ID_TRIBUTO.ID_DOMINIO.COD_DOMINIO)){
			if(appendTablePrefix){
				return this.toAliasTable(field)+".cod_dominio";
			}else{
				return "cod_dominio";
			}
		}
		if(field.equals(FR.model().ID_SINGOLO_VERSAMENTO.ID_TRIBUTO.ID_TIPO_TRIBUTO.COD_TRIBUTO)){
			if(appendTablePrefix){
				return this.toAliasTable(field)+".cod_tributo";
			}else{
				return "cod_tributo";
			}
		}
		if(field.equals(FR.model().ID_SINGOLO_VERSAMENTO.NOTE)){
			if(appendTablePrefix){
				return this.toAliasTable(field)+".note";
			}else{
				return "note";
			}
		}
		if(field.equals(FR.model().ID_SINGOLO_VERSAMENTO.STATO_SINGOLO_VERSAMENTO)){
			if(appendTablePrefix){
				return this.toAliasTable(field)+".stato_singolo_versamento";
			}else{
				return "stato_singolo_versamento";
			}
		}
		if(field.equals(FR.model().ID_SINGOLO_VERSAMENTO.IMPORTO_SINGOLO_VERSAMENTO)){
			if(appendTablePrefix){
				return this.toAliasTable(field)+".importo_singolo_versamento";
			}else{
				return "importo_singolo_versamento";
			}
		}
		if(field.equals(FR.model().ID_INCASSO.TRN)){
			if(appendTablePrefix){
				return this.toAliasTable(field)+".trn";
			}else{
				return "trn";
			}
		}
		if(field.equals(FR.model().ID_INCASSO.COD_DOMINIO)){
			if(appendTablePrefix){
				return this.toAliasTable(field)+".cod_dominio";
			}else{
				return "cod_dominio";
			}
		}
		if(field.equals(FR.model().ID_RENDICONTAZIONE.ID_RENDICONTAZIONE)){
			if(appendTablePrefix){
				return this.toAliasTable(field)+".id";
			}else{
				return "id";
			}
		}
		if(field.equals(FR.model().ID_RENDICONTAZIONE.IUV)){
			if(appendTablePrefix){
				return this.toAliasTable(field)+".iuv";
			}else{
				return "iuv";
			}
		}
		if(field.equals(FR.model().RAGIONE_SOCIALE_PSP)){
			if(appendTablePrefix){
				return this.toAliasTable(field)+".ragione_sociale_psp";
			}else{
				return "ragione_sociale_psp";
			}
		}
		if(field.equals(FR.model().RAGIONE_SOCIALE_DOMINIO)){
			if(appendTablePrefix){
				return this.toAliasTable(field)+".ragione_sociale_dominio";
			}else{
				return "ragione_sociale_dominio";
			}
		}
		if(field.equals(FR.model().OBSOLETO)){
			if(appendTablePrefix){
				return this.toAliasTable(field)+".obsoleto";
			}else{
				return "obsoleto";
			}
		}


		return super.toColumn(field,returnAlias,appendTablePrefix);
		
	}
	
	@Override
	public String toTable(IField field,boolean returnAlias) throws ExpressionException {
		
		// In the case of table with alias, using parameter returnAlias​​, 
		// it is possible to drive the choice whether to return only the alias or 
		// the full definition of the table containing the alias
		
		if(field.equals(FR.model().COD_PSP)){
			return this.toTable(FR.model(), returnAlias);
		}
		if(field.equals(FR.model().COD_DOMINIO)){
			return this.toTable(FR.model(), returnAlias);
		}
		if(field.equals(FR.model().COD_FLUSSO)){
			return this.toTable(FR.model(), returnAlias);
		}
		if(field.equals(FR.model().STATO)){
			return this.toTable(FR.model(), returnAlias);
		}
		if(field.equals(FR.model().DESCRIZIONE_STATO)){
			return this.toTable(FR.model(), returnAlias);
		}
		if(field.equals(FR.model().IUR)){
			return this.toTable(FR.model(), returnAlias);
		}
		if(field.equals(FR.model().DATA_ORA_FLUSSO)){
			return this.toTable(FR.model(), returnAlias);
		}
		if(field.equals(FR.model().DATA_REGOLAMENTO)){
			return this.toTable(FR.model(), returnAlias);
		}
		if(field.equals(FR.model().DATA_ACQUISIZIONE)){
			return this.toTable(FR.model(), returnAlias);
		}
		if(field.equals(FR.model().NUMERO_PAGAMENTI)){
			return this.toTable(FR.model(), returnAlias);
		}
		if(field.equals(FR.model().IMPORTO_TOTALE_PAGAMENTI)){
			return this.toTable(FR.model(), returnAlias);
		}
		if(field.equals(FR.model().COD_BIC_RIVERSAMENTO)){
			return this.toTable(FR.model(), returnAlias);
		}
		if(field.equals(FR.model().XML)){
			return this.toTable(FR.model(), returnAlias);
		}
		if(field.equals(FR.model().ID_SINGOLO_VERSAMENTO.ID_VERSAMENTO.COD_VERSAMENTO_ENTE)){
			return this.toTable(FR.model().ID_SINGOLO_VERSAMENTO.ID_VERSAMENTO, returnAlias);
		}
		if(field.equals(FR.model().ID_SINGOLO_VERSAMENTO.ID_VERSAMENTO.ID_APPLICAZIONE.COD_APPLICAZIONE)){
			return this.toTable(FR.model().ID_SINGOLO_VERSAMENTO.ID_VERSAMENTO.ID_APPLICAZIONE, returnAlias);
		}
		if(field.equals(FR.model().ID_SINGOLO_VERSAMENTO.ID_VERSAMENTO.SRC_DEBITORE_IDENTIFICATIVO)){
			return this.toTable(FR.model().ID_SINGOLO_VERSAMENTO.ID_VERSAMENTO, returnAlias);
		}
		if(field.equals(FR.model().ID_SINGOLO_VERSAMENTO.ID_VERSAMENTO.DEBITORE_ANAGRAFICA)){
			return this.toTable(FR.model().ID_SINGOLO_VERSAMENTO.ID_VERSAMENTO, returnAlias);
		}
		if(field.equals(FR.model().ID_SINGOLO_VERSAMENTO.ID_VERSAMENTO.COD_VERSAMENTO_LOTTO)){
			return this.toTable(FR.model().ID_SINGOLO_VERSAMENTO.ID_VERSAMENTO, returnAlias);
		}
		if(field.equals(FR.model().ID_SINGOLO_VERSAMENTO.ID_VERSAMENTO.COD_ANNO_TRIBUTARIO)){
			return this.toTable(FR.model().ID_SINGOLO_VERSAMENTO.ID_VERSAMENTO, returnAlias);
		}
		if(field.equals(FR.model().ID_SINGOLO_VERSAMENTO.ID_VERSAMENTO.IMPORTO_TOTALE)){
			return this.toTable(FR.model().ID_SINGOLO_VERSAMENTO.ID_VERSAMENTO, returnAlias);
		}
		if(field.equals(FR.model().ID_SINGOLO_VERSAMENTO.ID_VERSAMENTO.CAUSALE_VERSAMENTO)){
			return this.toTable(FR.model().ID_SINGOLO_VERSAMENTO.ID_VERSAMENTO, returnAlias);
		}
		if(field.equals(FR.model().ID_SINGOLO_VERSAMENTO.ID_VERSAMENTO.STATO_VERSAMENTO)){
			return this.toTable(FR.model().ID_SINGOLO_VERSAMENTO.ID_VERSAMENTO, returnAlias);
		}
		if(field.equals(FR.model().ID_SINGOLO_VERSAMENTO.ID_VERSAMENTO.ID_UO.COD_UO)){
			return this.toTable(FR.model().ID_SINGOLO_VERSAMENTO.ID_VERSAMENTO.ID_UO, returnAlias);
		}
		if(field.equals(FR.model().ID_SINGOLO_VERSAMENTO.ID_VERSAMENTO.ID_UO.ID_DOMINIO.COD_DOMINIO)){
			return this.toTable(FR.model().ID_SINGOLO_VERSAMENTO.ID_VERSAMENTO.ID_UO.ID_DOMINIO, returnAlias);
		}
		if(field.equals(FR.model().ID_SINGOLO_VERSAMENTO.ID_VERSAMENTO.ID_TIPO_VERSAMENTO.COD_TIPO_VERSAMENTO)){
			return this.toTable(FR.model().ID_SINGOLO_VERSAMENTO.ID_VERSAMENTO.ID_TIPO_VERSAMENTO, returnAlias);
		}
		if(field.equals(FR.model().ID_SINGOLO_VERSAMENTO.ID_VERSAMENTO.DIVISIONE)){
			return this.toTable(FR.model().ID_SINGOLO_VERSAMENTO.ID_VERSAMENTO, returnAlias);
		}
		if(field.equals(FR.model().ID_SINGOLO_VERSAMENTO.ID_VERSAMENTO.DIREZIONE)){
			return this.toTable(FR.model().ID_SINGOLO_VERSAMENTO.ID_VERSAMENTO, returnAlias);
		}
		if(field.equals(FR.model().ID_SINGOLO_VERSAMENTO.ID_VERSAMENTO.TASSONOMIA)){
			return this.toTable(FR.model().ID_SINGOLO_VERSAMENTO.ID_VERSAMENTO, returnAlias);
		}
		if(field.equals(FR.model().ID_SINGOLO_VERSAMENTO.ID_VERSAMENTO.TIPO)){
			return this.toTable(FR.model().ID_SINGOLO_VERSAMENTO.ID_VERSAMENTO, returnAlias);
		}
		if(field.equals(FR.model().ID_SINGOLO_VERSAMENTO.COD_SINGOLO_VERSAMENTO_ENTE)){
			return this.toTable(FR.model().ID_SINGOLO_VERSAMENTO, returnAlias);
		}
		if(field.equals(FR.model().ID_SINGOLO_VERSAMENTO.INDICE_DATI)){
			return this.toTable(FR.model().ID_SINGOLO_VERSAMENTO, returnAlias);
		}
		if(field.equals(FR.model().ID_SINGOLO_VERSAMENTO.ID_TRIBUTO.ID_DOMINIO.COD_DOMINIO)){
			return this.toTable(FR.model().ID_SINGOLO_VERSAMENTO.ID_TRIBUTO.ID_DOMINIO, returnAlias);
		}
		if(field.equals(FR.model().ID_SINGOLO_VERSAMENTO.ID_TRIBUTO.ID_TIPO_TRIBUTO.COD_TRIBUTO)){
			return this.toTable(FR.model().ID_SINGOLO_VERSAMENTO.ID_TRIBUTO.ID_TIPO_TRIBUTO, returnAlias);
		}
		if(field.equals(FR.model().ID_SINGOLO_VERSAMENTO.NOTE)){
			return this.toTable(FR.model().ID_SINGOLO_VERSAMENTO, returnAlias);
		}
		if(field.equals(FR.model().ID_SINGOLO_VERSAMENTO.STATO_SINGOLO_VERSAMENTO)){
			return this.toTable(FR.model().ID_SINGOLO_VERSAMENTO, returnAlias);
		}
		if(field.equals(FR.model().ID_SINGOLO_VERSAMENTO.IMPORTO_SINGOLO_VERSAMENTO)){
			return this.toTable(FR.model().ID_SINGOLO_VERSAMENTO, returnAlias);
		}
		if(field.equals(FR.model().ID_INCASSO.TRN)){
			return this.toTable(FR.model().ID_INCASSO, returnAlias);
		}
		if(field.equals(FR.model().ID_INCASSO.COD_DOMINIO)){
			return this.toTable(FR.model().ID_INCASSO, returnAlias);
		}
		if(field.equals(FR.model().ID_RENDICONTAZIONE.ID_RENDICONTAZIONE)){
			return this.toTable(FR.model().ID_RENDICONTAZIONE, returnAlias);
		}
		if(field.equals(FR.model().ID_RENDICONTAZIONE.IUV)){
			return this.toTable(FR.model().ID_RENDICONTAZIONE, returnAlias);
		}
		if(field.equals(FR.model().RAGIONE_SOCIALE_PSP)){
			return this.toTable(FR.model(), returnAlias);
		}
		if(field.equals(FR.model().RAGIONE_SOCIALE_DOMINIO)){
			return this.toTable(FR.model(), returnAlias);
		}
		if(field.equals(FR.model().OBSOLETO)){
			return this.toTable(FR.model(), returnAlias);
		}


		return super.toTable(field,returnAlias);
		
	}

	@Override
	public String toTable(IModel<?> model,boolean returnAlias) throws ExpressionException {
		
		// In the case of table with alias, using parameter returnAlias​​, 
		// it is possible to drive the choice whether to return only the alias or 
		// the full definition of the table containing the alias
		
		if(model.equals(FR.model())){
			return "fr";
		}
		if(model.equals(FR.model().ID_SINGOLO_VERSAMENTO)){
			return "singoli_versamenti";
		}
		if(model.equals(FR.model().ID_SINGOLO_VERSAMENTO.ID_VERSAMENTO)){
			return "versamenti";
		}
		if(model.equals(FR.model().ID_SINGOLO_VERSAMENTO.ID_VERSAMENTO.ID_APPLICAZIONE)){
			return "applicazioni";
		}
		if(model.equals(FR.model().ID_SINGOLO_VERSAMENTO.ID_VERSAMENTO.ID_UO)){
			return "uo";
		}
		if(model.equals(FR.model().ID_SINGOLO_VERSAMENTO.ID_VERSAMENTO.ID_UO.ID_DOMINIO)){
			return "domini";
		}
		if(model.equals(FR.model().ID_SINGOLO_VERSAMENTO.ID_VERSAMENTO.ID_TIPO_VERSAMENTO)){
			return "tipi_versamento";
		}
		if(model.equals(FR.model().ID_SINGOLO_VERSAMENTO.ID_TRIBUTO)){
			return "tributi";
		}
		if(model.equals(FR.model().ID_SINGOLO_VERSAMENTO.ID_TRIBUTO.ID_DOMINIO)){
			return "domini";
		}
		if(model.equals(FR.model().ID_SINGOLO_VERSAMENTO.ID_TRIBUTO.ID_TIPO_TRIBUTO)){
			return "tipi_tributo";
		}
		if(model.equals(FR.model().ID_INCASSO)){
			return "incassi";
		}
		if(model.equals(FR.model().ID_RENDICONTAZIONE)){
			return "rendicontazioni";
		}


		return super.toTable(model,returnAlias);
		
	}

}
