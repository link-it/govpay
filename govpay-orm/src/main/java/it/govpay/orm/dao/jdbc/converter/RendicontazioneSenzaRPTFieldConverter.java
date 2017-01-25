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

import it.govpay.orm.RendicontazioneSenzaRPT;


/**     
 * RendicontazioneSenzaRPTFieldConverter
 *
 * @author Giovanni Bussu (bussu@link.it)
 * @author Lorenzo Nardi (nardi@link.it)
 * @author $Author$
 * @version $Rev$, $Date$
 */
public class RendicontazioneSenzaRPTFieldConverter extends AbstractSQLFieldConverter {

	private TipiDatabase databaseType;
	
	public RendicontazioneSenzaRPTFieldConverter(String databaseType){
		this.databaseType = TipiDatabase.toEnumConstant(databaseType);
	}
	public RendicontazioneSenzaRPTFieldConverter(TipiDatabase databaseType){
		this.databaseType = databaseType;
	}


	@Override
	public IModel<?> getRootModel() throws ExpressionException {
		return RendicontazioneSenzaRPT.model();
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
		
		if(field.equals(RendicontazioneSenzaRPT.model().ID_FR_APPLICAZIONE.ID_FR.COD_FLUSSO)){
			if(appendTablePrefix){
				return this.toAliasTable(field)+".cod_flusso";
			}else{
				return "cod_flusso";
			}
		}
		if(field.equals(RendicontazioneSenzaRPT.model().ID_FR_APPLICAZIONE.ID_FR.ANNO_RIFERIMENTO)){
			if(appendTablePrefix){
				return this.toAliasTable(field)+".anno_riferimento";
			}else{
				return "anno_riferimento";
			}
		}
		if(field.equals(RendicontazioneSenzaRPT.model().ID_FR_APPLICAZIONE.ID_APPLICAZIONE.COD_APPLICAZIONE)){
			if(appendTablePrefix){
				return this.toAliasTable(field)+".cod_applicazione";
			}else{
				return "cod_applicazione";
			}
		}
		if(field.equals(RendicontazioneSenzaRPT.model().IMPORTO_PAGATO)){
			if(appendTablePrefix){
				return this.toAliasTable(field)+".importo_pagato";
			}else{
				return "importo_pagato";
			}
		}
		if(field.equals(RendicontazioneSenzaRPT.model().ID_IUV.ID_DOMINIO.COD_DOMINIO)){
			if(appendTablePrefix){
				return this.toAliasTable(field)+".cod_dominio";
			}else{
				return "cod_dominio";
			}
		}
		if(field.equals(RendicontazioneSenzaRPT.model().ID_IUV.PRG)){
			if(appendTablePrefix){
				return this.toAliasTable(field)+".prg";
			}else{
				return "prg";
			}
		}
		if(field.equals(RendicontazioneSenzaRPT.model().IUR)){
			if(appendTablePrefix){
				return this.toAliasTable(field)+".iur";
			}else{
				return "iur";
			}
		}
		if(field.equals(RendicontazioneSenzaRPT.model().RENDICONTAZIONE_DATA)){
			if(appendTablePrefix){
				return this.toAliasTable(field)+".rendicontazione_data";
			}else{
				return "rendicontazione_data";
			}
		}
		if(field.equals(RendicontazioneSenzaRPT.model().ID_SINGOLO_VERSAMENTO.ID_VERSAMENTO.COD_VERSAMENTO_ENTE)){
			if(appendTablePrefix){
				return this.toAliasTable(field)+".cod_versamento_ente";
			}else{
				return "cod_versamento_ente";
			}
		}
		if(field.equals(RendicontazioneSenzaRPT.model().ID_SINGOLO_VERSAMENTO.ID_VERSAMENTO.ID_APPLICAZIONE.COD_APPLICAZIONE)){
			if(appendTablePrefix){
				return this.toAliasTable(field)+".cod_applicazione";
			}else{
				return "cod_applicazione";
			}
		}
		if(field.equals(RendicontazioneSenzaRPT.model().ID_SINGOLO_VERSAMENTO.ID_VERSAMENTO.DEBITORE_IDENTIFICATIVO)){
			if(appendTablePrefix){
				return this.toAliasTable(field)+".debitore_identificativo";
			}else{
				return "debitore_identificativo";
			}
		}
		if(field.equals(RendicontazioneSenzaRPT.model().ID_SINGOLO_VERSAMENTO.ID_VERSAMENTO.DEBITORE_ANAGRAFICA)){
			if(appendTablePrefix){
				return this.toAliasTable(field)+".debitore_anagrafica";
			}else{
				return "debitore_anagrafica";
			}
		}
		if(field.equals(RendicontazioneSenzaRPT.model().ID_SINGOLO_VERSAMENTO.ID_VERSAMENTO.COD_VERSAMENTO_LOTTO)){
			if(appendTablePrefix){
				return this.toAliasTable(field)+".cod_versamento_lotto";
			}else{
				return "cod_versamento_lotto";
			}
		}
		if(field.equals(RendicontazioneSenzaRPT.model().ID_SINGOLO_VERSAMENTO.ID_VERSAMENTO.COD_ANNO_TRIBUTARIO)){
			if(appendTablePrefix){
				return this.toAliasTable(field)+".cod_anno_tributario";
			}else{
				return "cod_anno_tributario";
			}
		}
		if(field.equals(RendicontazioneSenzaRPT.model().ID_SINGOLO_VERSAMENTO.ID_VERSAMENTO.IMPORTO_TOTALE)){
			if(appendTablePrefix){
				return this.toAliasTable(field)+".importo_totale";
			}else{
				return "importo_totale";
			}
		}
		if(field.equals(RendicontazioneSenzaRPT.model().ID_SINGOLO_VERSAMENTO.ID_VERSAMENTO.CAUSALE_VERSAMENTO)){
			if(appendTablePrefix){
				return this.toAliasTable(field)+".causale_versamento";
			}else{
				return "causale_versamento";
			}
		}
		if(field.equals(RendicontazioneSenzaRPT.model().ID_SINGOLO_VERSAMENTO.ID_VERSAMENTO.STATO_VERSAMENTO)){
			if(appendTablePrefix){
				return this.toAliasTable(field)+".stato_versamento";
			}else{
				return "stato_versamento";
			}
		}
		if(field.equals(RendicontazioneSenzaRPT.model().ID_SINGOLO_VERSAMENTO.COD_SINGOLO_VERSAMENTO_ENTE)){
			if(appendTablePrefix){
				return this.toAliasTable(field)+".cod_singolo_versamento_ente";
			}else{
				return "cod_singolo_versamento_ente";
			}
		}
		if(field.equals(RendicontazioneSenzaRPT.model().ID_SINGOLO_VERSAMENTO.ID_TRIBUTO.ID_DOMINIO.COD_DOMINIO)){
			if(appendTablePrefix){
				return this.toAliasTable(field)+".cod_dominio";
			}else{
				return "cod_dominio";
			}
		}
		if(field.equals(RendicontazioneSenzaRPT.model().ID_SINGOLO_VERSAMENTO.ID_TRIBUTO.ID_TIPO_TRIBUTO.COD_TRIBUTO)){
			if(appendTablePrefix){
				return this.toAliasTable(field)+".cod_tributo";
			}else{
				return "cod_tributo";
			}
		}
		if(field.equals(RendicontazioneSenzaRPT.model().ID_SINGOLO_VERSAMENTO.NOTE)){
			if(appendTablePrefix){
				return this.toAliasTable(field)+".note";
			}else{
				return "note";
			}
		}
		if(field.equals(RendicontazioneSenzaRPT.model().ID_SINGOLO_VERSAMENTO.STATO_SINGOLO_VERSAMENTO)){
			if(appendTablePrefix){
				return this.toAliasTable(field)+".stato_singolo_versamento";
			}else{
				return "stato_singolo_versamento";
			}
		}
		if(field.equals(RendicontazioneSenzaRPT.model().ID_SINGOLO_VERSAMENTO.IMPORTO_SINGOLO_VERSAMENTO)){
			if(appendTablePrefix){
				return this.toAliasTable(field)+".importo_singolo_versamento";
			}else{
				return "importo_singolo_versamento";
			}
		}


		return super.toColumn(field,returnAlias,appendTablePrefix);
		
	}
	
	@Override
	public String toTable(IField field,boolean returnAlias) throws ExpressionException {
		
		// In the case of table with alias, using parameter returnAlias​​, 
		// it is possible to drive the choice whether to return only the alias or 
		// the full definition of the table containing the alias
		
		if(field.equals(RendicontazioneSenzaRPT.model().ID_FR_APPLICAZIONE.ID_FR.COD_FLUSSO)){
			return this.toTable(RendicontazioneSenzaRPT.model().ID_FR_APPLICAZIONE.ID_FR, returnAlias);
		}
		if(field.equals(RendicontazioneSenzaRPT.model().ID_FR_APPLICAZIONE.ID_FR.ANNO_RIFERIMENTO)){
			return this.toTable(RendicontazioneSenzaRPT.model().ID_FR_APPLICAZIONE.ID_FR, returnAlias);
		}
		if(field.equals(RendicontazioneSenzaRPT.model().ID_FR_APPLICAZIONE.ID_APPLICAZIONE.COD_APPLICAZIONE)){
			return this.toTable(RendicontazioneSenzaRPT.model().ID_FR_APPLICAZIONE.ID_APPLICAZIONE, returnAlias);
		}
		if(field.equals(RendicontazioneSenzaRPT.model().IMPORTO_PAGATO)){
			return this.toTable(RendicontazioneSenzaRPT.model(), returnAlias);
		}
		if(field.equals(RendicontazioneSenzaRPT.model().ID_IUV.ID_DOMINIO.COD_DOMINIO)){
			return this.toTable(RendicontazioneSenzaRPT.model().ID_IUV.ID_DOMINIO, returnAlias);
		}
		if(field.equals(RendicontazioneSenzaRPT.model().ID_IUV.PRG)){
			return this.toTable(RendicontazioneSenzaRPT.model().ID_IUV, returnAlias);
		}
		if(field.equals(RendicontazioneSenzaRPT.model().IUR)){
			return this.toTable(RendicontazioneSenzaRPT.model(), returnAlias);
		}
		if(field.equals(RendicontazioneSenzaRPT.model().RENDICONTAZIONE_DATA)){
			return this.toTable(RendicontazioneSenzaRPT.model(), returnAlias);
		}
		if(field.equals(RendicontazioneSenzaRPT.model().ID_SINGOLO_VERSAMENTO.ID_VERSAMENTO.COD_VERSAMENTO_ENTE)){
			return this.toTable(RendicontazioneSenzaRPT.model().ID_SINGOLO_VERSAMENTO.ID_VERSAMENTO, returnAlias);
		}
		if(field.equals(RendicontazioneSenzaRPT.model().ID_SINGOLO_VERSAMENTO.ID_VERSAMENTO.ID_APPLICAZIONE.COD_APPLICAZIONE)){
			return this.toTable(RendicontazioneSenzaRPT.model().ID_SINGOLO_VERSAMENTO.ID_VERSAMENTO.ID_APPLICAZIONE, returnAlias);
		}
		if(field.equals(RendicontazioneSenzaRPT.model().ID_SINGOLO_VERSAMENTO.ID_VERSAMENTO.DEBITORE_IDENTIFICATIVO)){
			return this.toTable(RendicontazioneSenzaRPT.model().ID_SINGOLO_VERSAMENTO.ID_VERSAMENTO, returnAlias);
		}
		if(field.equals(RendicontazioneSenzaRPT.model().ID_SINGOLO_VERSAMENTO.ID_VERSAMENTO.DEBITORE_ANAGRAFICA)){
			return this.toTable(RendicontazioneSenzaRPT.model().ID_SINGOLO_VERSAMENTO.ID_VERSAMENTO, returnAlias);
		}
		if(field.equals(RendicontazioneSenzaRPT.model().ID_SINGOLO_VERSAMENTO.ID_VERSAMENTO.COD_VERSAMENTO_LOTTO)){
			return this.toTable(RendicontazioneSenzaRPT.model().ID_SINGOLO_VERSAMENTO.ID_VERSAMENTO, returnAlias);
		}
		if(field.equals(RendicontazioneSenzaRPT.model().ID_SINGOLO_VERSAMENTO.ID_VERSAMENTO.COD_ANNO_TRIBUTARIO)){
			return this.toTable(RendicontazioneSenzaRPT.model().ID_SINGOLO_VERSAMENTO.ID_VERSAMENTO, returnAlias);
		}
		if(field.equals(RendicontazioneSenzaRPT.model().ID_SINGOLO_VERSAMENTO.ID_VERSAMENTO.IMPORTO_TOTALE)){
			return this.toTable(RendicontazioneSenzaRPT.model().ID_SINGOLO_VERSAMENTO.ID_VERSAMENTO, returnAlias);
		}
		if(field.equals(RendicontazioneSenzaRPT.model().ID_SINGOLO_VERSAMENTO.ID_VERSAMENTO.CAUSALE_VERSAMENTO)){
			return this.toTable(RendicontazioneSenzaRPT.model().ID_SINGOLO_VERSAMENTO.ID_VERSAMENTO, returnAlias);
		}
		if(field.equals(RendicontazioneSenzaRPT.model().ID_SINGOLO_VERSAMENTO.ID_VERSAMENTO.STATO_VERSAMENTO)){
			return this.toTable(RendicontazioneSenzaRPT.model().ID_SINGOLO_VERSAMENTO.ID_VERSAMENTO, returnAlias);
		}
		if(field.equals(RendicontazioneSenzaRPT.model().ID_SINGOLO_VERSAMENTO.COD_SINGOLO_VERSAMENTO_ENTE)){
			return this.toTable(RendicontazioneSenzaRPT.model().ID_SINGOLO_VERSAMENTO, returnAlias);
		}
		if(field.equals(RendicontazioneSenzaRPT.model().ID_SINGOLO_VERSAMENTO.ID_TRIBUTO.ID_DOMINIO.COD_DOMINIO)){
			return this.toTable(RendicontazioneSenzaRPT.model().ID_SINGOLO_VERSAMENTO.ID_TRIBUTO.ID_DOMINIO, returnAlias);
		}
		if(field.equals(RendicontazioneSenzaRPT.model().ID_SINGOLO_VERSAMENTO.ID_TRIBUTO.ID_TIPO_TRIBUTO.COD_TRIBUTO)){
			return this.toTable(RendicontazioneSenzaRPT.model().ID_SINGOLO_VERSAMENTO.ID_TRIBUTO.ID_TIPO_TRIBUTO, returnAlias);
		}
		if(field.equals(RendicontazioneSenzaRPT.model().ID_SINGOLO_VERSAMENTO.NOTE)){
			return this.toTable(RendicontazioneSenzaRPT.model().ID_SINGOLO_VERSAMENTO, returnAlias);
		}
		if(field.equals(RendicontazioneSenzaRPT.model().ID_SINGOLO_VERSAMENTO.STATO_SINGOLO_VERSAMENTO)){
			return this.toTable(RendicontazioneSenzaRPT.model().ID_SINGOLO_VERSAMENTO, returnAlias);
		}
		if(field.equals(RendicontazioneSenzaRPT.model().ID_SINGOLO_VERSAMENTO.IMPORTO_SINGOLO_VERSAMENTO)){
			return this.toTable(RendicontazioneSenzaRPT.model().ID_SINGOLO_VERSAMENTO, returnAlias);
		}


		return super.toTable(field,returnAlias);
		
	}

	@Override
	public String toTable(IModel<?> model,boolean returnAlias) throws ExpressionException {
		
		// In the case of table with alias, using parameter returnAlias​​, 
		// it is possible to drive the choice whether to return only the alias or 
		// the full definition of the table containing the alias
		
		if(model.equals(RendicontazioneSenzaRPT.model())){
			return "rendicontazioni_senza_rpt";
		}
		if(model.equals(RendicontazioneSenzaRPT.model().ID_FR_APPLICAZIONE)){
			return "fr_applicazioni";
		}
		if(model.equals(RendicontazioneSenzaRPT.model().ID_FR_APPLICAZIONE.ID_FR)){
			return "fr";
		}
		if(model.equals(RendicontazioneSenzaRPT.model().ID_FR_APPLICAZIONE.ID_APPLICAZIONE)){
			return "applicazioni";
		}
		if(model.equals(RendicontazioneSenzaRPT.model().ID_IUV)){
			return "iuv";
		}
		if(model.equals(RendicontazioneSenzaRPT.model().ID_IUV.ID_DOMINIO)){
			return "domini";
		}
		if(model.equals(RendicontazioneSenzaRPT.model().ID_SINGOLO_VERSAMENTO)){
			return "singoli_versamenti";
		}
		if(model.equals(RendicontazioneSenzaRPT.model().ID_SINGOLO_VERSAMENTO.ID_VERSAMENTO)){
			return "versamenti";
		}
		if(model.equals(RendicontazioneSenzaRPT.model().ID_SINGOLO_VERSAMENTO.ID_VERSAMENTO.ID_APPLICAZIONE)){
			return "applicazioni";
		}
		if(model.equals(RendicontazioneSenzaRPT.model().ID_SINGOLO_VERSAMENTO.ID_TRIBUTO)){
			return "tributi";
		}
		if(model.equals(RendicontazioneSenzaRPT.model().ID_SINGOLO_VERSAMENTO.ID_TRIBUTO.ID_DOMINIO)){
			return "domini";
		}
		if(model.equals(RendicontazioneSenzaRPT.model().ID_SINGOLO_VERSAMENTO.ID_TRIBUTO.ID_TIPO_TRIBUTO)){
			return "tipi_tributo";
		}

		return super.toTable(model,returnAlias);
		
	}

}
