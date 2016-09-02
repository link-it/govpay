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

import it.govpay.orm.Pagamento;


/**     
 * PagamentoFieldConverter
 *
 * @author Giovanni Bussu (bussu@link.it)
 * @author Lorenzo Nardi (nardi@link.it)
 * @author $Author$
 * @version $Rev$, $Date$
 */
public class PagamentoFieldConverter extends AbstractSQLFieldConverter {

	private TipiDatabase databaseType;
	
	public PagamentoFieldConverter(String databaseType){
		this.databaseType = TipiDatabase.toEnumConstant(databaseType);
	}
	public PagamentoFieldConverter(TipiDatabase databaseType){
		this.databaseType = databaseType;
	}


	@Override
	public IModel<?> getRootModel() throws ExpressionException {
		return Pagamento.model();
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
		
		if(field.equals(Pagamento.model().ID_RPT.COD_MSG_RICHIESTA)){
			if(appendTablePrefix){
				return this.toAliasTable(field)+".cod_msg_richiesta";
			}else{
				return "cod_msg_richiesta";
			}
		}
		if(field.equals(Pagamento.model().ID_RPT.COD_DOMINIO)){
			if(appendTablePrefix){
				return this.toAliasTable(field)+".cod_dominio";
			}else{
				return "cod_dominio";
			}
		}
		if(field.equals(Pagamento.model().ID_RPT.IUV)){
			if(appendTablePrefix){
				return this.toAliasTable(field)+".iuv";
			}else{
				return "iuv";
			}
		}
		if(field.equals(Pagamento.model().ID_SINGOLO_VERSAMENTO.ID_VERSAMENTO.COD_VERSAMENTO_ENTE)){
			if(appendTablePrefix){
				return this.toAliasTable(field)+".cod_versamento_ente";
			}else{
				return "cod_versamento_ente";
			}
		}
		if(field.equals(Pagamento.model().ID_SINGOLO_VERSAMENTO.ID_VERSAMENTO.ID_APPLICAZIONE.COD_APPLICAZIONE)){
			if(appendTablePrefix){
				return this.toAliasTable(field)+".cod_applicazione";
			}else{
				return "cod_applicazione";
			}
		}
		if(field.equals(Pagamento.model().ID_SINGOLO_VERSAMENTO.ID_VERSAMENTO.DEBITORE_IDENTIFICATIVO)){
			if(appendTablePrefix){
				return this.toAliasTable(field)+".debitore_identificativo";
			}else{
				return "debitore_identificativo";
			}
		}
		if(field.equals(Pagamento.model().ID_SINGOLO_VERSAMENTO.ID_VERSAMENTO.DEBITORE_ANAGRAFICA)){
			if(appendTablePrefix){
				return this.toAliasTable(field)+".debitore_anagrafica";
			}else{
				return "debitore_anagrafica";
			}
		}
		if(field.equals(Pagamento.model().ID_SINGOLO_VERSAMENTO.ID_VERSAMENTO.COD_VERSAMENTO_LOTTO)){
			if(appendTablePrefix){
				return this.toAliasTable(field)+".cod_versamento_lotto";
			}else{
				return "cod_versamento_lotto";
			}
		}
		if(field.equals(Pagamento.model().ID_SINGOLO_VERSAMENTO.ID_VERSAMENTO.COD_ANNO_TRIBUTARIO)){
			if(appendTablePrefix){
				return this.toAliasTable(field)+".cod_anno_tributario";
			}else{
				return "cod_anno_tributario";
			}
		}
		if(field.equals(Pagamento.model().ID_SINGOLO_VERSAMENTO.COD_SINGOLO_VERSAMENTO_ENTE)){
			if(appendTablePrefix){
				return this.toAliasTable(field)+".cod_singolo_versamento_ente";
			}else{
				return "cod_singolo_versamento_ente";
			}
		}
		if(field.equals(Pagamento.model().ID_SINGOLO_VERSAMENTO.ID_TRIBUTO.ID_DOMINIO.COD_DOMINIO)){
			if(appendTablePrefix){
				return this.toAliasTable(field)+".cod_dominio";
			}else{
				return "cod_dominio";
			}
		}
		if(field.equals(Pagamento.model().ID_SINGOLO_VERSAMENTO.ID_TRIBUTO.ID_TIPO_TRIBUTO.COD_TRIBUTO)){
			if(appendTablePrefix){
				return this.toAliasTable(field)+".cod_tributo";
			}else{
				return "cod_tributo";
			}
		}
		if(field.equals(Pagamento.model().ID_SINGOLO_VERSAMENTO.NOTE)){
			if(appendTablePrefix){
				return this.toAliasTable(field)+".note";
			}else{
				return "note";
			}
		}
		if(field.equals(Pagamento.model().COD_SINGOLO_VERSAMENTO_ENTE)){
			if(appendTablePrefix){
				return this.toAliasTable(field)+".cod_singolo_versamento_ente";
			}else{
				return "cod_singolo_versamento_ente";
			}
		}
		if(field.equals(Pagamento.model().IMPORTO_PAGATO)){
			if(appendTablePrefix){
				return this.toAliasTable(field)+".importo_pagato";
			}else{
				return "importo_pagato";
			}
		}
		if(field.equals(Pagamento.model().DATA_ACQUISIZIONE)){
			if(appendTablePrefix){
				return this.toAliasTable(field)+".data_acquisizione";
			}else{
				return "data_acquisizione";
			}
		}
		if(field.equals(Pagamento.model().IUR)){
			if(appendTablePrefix){
				return this.toAliasTable(field)+".iur";
			}else{
				return "iur";
			}
		}
		if(field.equals(Pagamento.model().DATA_PAGAMENTO)){
			if(appendTablePrefix){
				return this.toAliasTable(field)+".data_pagamento";
			}else{
				return "data_pagamento";
			}
		}
		if(field.equals(Pagamento.model().IBAN_ACCREDITO)){
			if(appendTablePrefix){
				return this.toAliasTable(field)+".iban_accredito";
			}else{
				return "iban_accredito";
			}
		}
		if(field.equals(Pagamento.model().COMMISSIONI_PSP)){
			if(appendTablePrefix){
				return this.toAliasTable(field)+".commissioni_psp";
			}else{
				return "commissioni_psp";
			}
		}
		if(field.equals(Pagamento.model().TIPO_ALLEGATO)){
			if(appendTablePrefix){
				return this.toAliasTable(field)+".tipo_allegato";
			}else{
				return "tipo_allegato";
			}
		}
		if(field.equals(Pagamento.model().ALLEGATO)){
			if(appendTablePrefix){
				return this.toAliasTable(field)+".allegato";
			}else{
				return "allegato";
			}
		}
		if(field.equals(Pagamento.model().ID_FR_APPLICAZIONE.ID_FR.COD_FLUSSO)){
			if(appendTablePrefix){
				return this.toAliasTable(field)+".cod_flusso";
			}else{
				return "cod_flusso";
			}
		}
		if(field.equals(Pagamento.model().ID_FR_APPLICAZIONE.ID_FR.ANNO_RIFERIMENTO)){
			if(appendTablePrefix){
				return this.toAliasTable(field)+".anno_riferimento";
			}else{
				return "anno_riferimento";
			}
		}
		if(field.equals(Pagamento.model().ID_FR_APPLICAZIONE.ID_APPLICAZIONE.COD_APPLICAZIONE)){
			if(appendTablePrefix){
				return this.toAliasTable(field)+".cod_applicazione";
			}else{
				return "cod_applicazione";
			}
		}
		if(field.equals(Pagamento.model().RENDICONTAZIONE_ESITO)){
			if(appendTablePrefix){
				return this.toAliasTable(field)+".rendicontazione_esito";
			}else{
				return "rendicontazione_esito";
			}
		}
		if(field.equals(Pagamento.model().RENDICONTAZIONE_DATA)){
			if(appendTablePrefix){
				return this.toAliasTable(field)+".rendicontazione_data";
			}else{
				return "rendicontazione_data";
			}
		}
		if(field.equals(Pagamento.model().CODFLUSSO_RENDICONTAZIONE)){
			if(appendTablePrefix){
				return this.toAliasTable(field)+".codflusso_rendicontazione";
			}else{
				return "codflusso_rendicontazione";
			}
		}
		if(field.equals(Pagamento.model().ANNO_RIFERIMENTO)){
			if(appendTablePrefix){
				return this.toAliasTable(field)+".anno_riferimento";
			}else{
				return "anno_riferimento";
			}
		}
		if(field.equals(Pagamento.model().INDICE_SINGOLO_PAGAMENTO)){
			if(appendTablePrefix){
				return this.toAliasTable(field)+".indice_singolo_pagamento";
			}else{
				return "indice_singolo_pagamento";
			}
		}
		if(field.equals(Pagamento.model().ID_RR.COD_MSG_REVOCA)){
			if(appendTablePrefix){
				return this.toAliasTable(field)+".cod_msg_revoca";
			}else{
				return "cod_msg_revoca";
			}
		}
		if(field.equals(Pagamento.model().DATA_ACQUISIZIONE_REVOCA)){
			if(appendTablePrefix){
				return this.toAliasTable(field)+".data_acquisizione_revoca";
			}else{
				return "data_acquisizione_revoca";
			}
		}
		if(field.equals(Pagamento.model().CAUSALE_REVOCA)){
			if(appendTablePrefix){
				return this.toAliasTable(field)+".causale_revoca";
			}else{
				return "causale_revoca";
			}
		}
		if(field.equals(Pagamento.model().DATI_REVOCA)){
			if(appendTablePrefix){
				return this.toAliasTable(field)+".dati_revoca";
			}else{
				return "dati_revoca";
			}
		}
		if(field.equals(Pagamento.model().IMPORTO_REVOCATO)){
			if(appendTablePrefix){
				return this.toAliasTable(field)+".importo_revocato";
			}else{
				return "importo_revocato";
			}
		}
		if(field.equals(Pagamento.model().ESITO_REVOCA)){
			if(appendTablePrefix){
				return this.toAliasTable(field)+".esito_revoca";
			}else{
				return "esito_revoca";
			}
		}
		if(field.equals(Pagamento.model().DATI_ESITO_REVOCA)){
			if(appendTablePrefix){
				return this.toAliasTable(field)+".dati_esito_revoca";
			}else{
				return "dati_esito_revoca";
			}
		}
		if(field.equals(Pagamento.model().ID_FR_APPLICAZIONE_REVOCA.ID_FR.COD_FLUSSO)){
			if(appendTablePrefix){
				return this.toAliasTable(field)+".cod_flusso";
			}else{
				return "cod_flusso";
			}
		}
		if(field.equals(Pagamento.model().ID_FR_APPLICAZIONE_REVOCA.ID_FR.ANNO_RIFERIMENTO)){
			if(appendTablePrefix){
				return this.toAliasTable(field)+".anno_riferimento";
			}else{
				return "anno_riferimento";
			}
		}
		if(field.equals(Pagamento.model().ID_FR_APPLICAZIONE_REVOCA.ID_APPLICAZIONE.COD_APPLICAZIONE)){
			if(appendTablePrefix){
				return this.toAliasTable(field)+".cod_applicazione";
			}else{
				return "cod_applicazione";
			}
		}
		if(field.equals(Pagamento.model().RENDICONTAZIONE_ESITO_REVOCA)){
			if(appendTablePrefix){
				return this.toAliasTable(field)+".rendicontazione_esito_revoca";
			}else{
				return "rendicontazione_esito_revoca";
			}
		}
		if(field.equals(Pagamento.model().RENDICONTAZIONE_DATA_REVOCA)){
			if(appendTablePrefix){
				return this.toAliasTable(field)+".rendicontazione_data_revoca";
			}else{
				return "rendicontazione_data_revoca";
			}
		}
		if(field.equals(Pagamento.model().COD_FLUSSO_RENDICONTAZIONE_REVOCA)){
			if(appendTablePrefix){
				return this.toAliasTable(field)+".cod_flusso_rendicontaz_revoca";
			}else{
				return "cod_flusso_rendicontaz_revoca";
			}
		}
		if(field.equals(Pagamento.model().ANNO_RIFERIMENTO_REVOCA)){
			if(appendTablePrefix){
				return this.toAliasTable(field)+".anno_riferimento_revoca";
			}else{
				return "anno_riferimento_revoca";
			}
		}
		if(field.equals(Pagamento.model().INDICE_SINGOLO_PAGAMENTO_REVOCA)){
			if(appendTablePrefix){
				return this.toAliasTable(field)+".ind_singolo_pagamento_revoca";
			}else{
				return "ind_singolo_pagamento_revoca";
			}
		}


		return super.toColumn(field,returnAlias,appendTablePrefix);
		
	}
	
	@Override
	public String toTable(IField field,boolean returnAlias) throws ExpressionException {
		
		// In the case of table with alias, using parameter returnAlias​​, 
		// it is possible to drive the choice whether to return only the alias or 
		// the full definition of the table containing the alias
		
		if(field.equals(Pagamento.model().ID_RPT.COD_MSG_RICHIESTA)){
			return this.toTable(Pagamento.model().ID_RPT, returnAlias);
		}
		if(field.equals(Pagamento.model().ID_RPT.COD_DOMINIO)){
			return this.toTable(Pagamento.model().ID_RPT, returnAlias);
		}
		if(field.equals(Pagamento.model().ID_RPT.IUV)){
			return this.toTable(Pagamento.model().ID_RPT, returnAlias);
		}
		if(field.equals(Pagamento.model().ID_SINGOLO_VERSAMENTO.ID_VERSAMENTO.COD_VERSAMENTO_ENTE)){
			return this.toTable(Pagamento.model().ID_SINGOLO_VERSAMENTO.ID_VERSAMENTO, returnAlias);
		}
		if(field.equals(Pagamento.model().ID_SINGOLO_VERSAMENTO.ID_VERSAMENTO.ID_APPLICAZIONE.COD_APPLICAZIONE)){
			return this.toTable(Pagamento.model().ID_SINGOLO_VERSAMENTO.ID_VERSAMENTO.ID_APPLICAZIONE, returnAlias);
		}
		if(field.equals(Pagamento.model().ID_SINGOLO_VERSAMENTO.ID_VERSAMENTO.DEBITORE_IDENTIFICATIVO)){
			return this.toTable(Pagamento.model().ID_SINGOLO_VERSAMENTO.ID_VERSAMENTO, returnAlias);
		}
		if(field.equals(Pagamento.model().ID_SINGOLO_VERSAMENTO.ID_VERSAMENTO.DEBITORE_ANAGRAFICA)){
			return this.toTable(Pagamento.model().ID_SINGOLO_VERSAMENTO.ID_VERSAMENTO, returnAlias);
		}
		if(field.equals(Pagamento.model().ID_SINGOLO_VERSAMENTO.ID_VERSAMENTO.COD_VERSAMENTO_LOTTO)){
			return this.toTable(Pagamento.model().ID_SINGOLO_VERSAMENTO.ID_VERSAMENTO, returnAlias);
		}
		if(field.equals(Pagamento.model().ID_SINGOLO_VERSAMENTO.ID_VERSAMENTO.COD_ANNO_TRIBUTARIO)){
			return this.toTable(Pagamento.model().ID_SINGOLO_VERSAMENTO.ID_VERSAMENTO, returnAlias);
		}
		if(field.equals(Pagamento.model().ID_SINGOLO_VERSAMENTO.COD_SINGOLO_VERSAMENTO_ENTE)){
			return this.toTable(Pagamento.model().ID_SINGOLO_VERSAMENTO, returnAlias);
		}
		if(field.equals(Pagamento.model().ID_SINGOLO_VERSAMENTO.ID_TRIBUTO.ID_DOMINIO.COD_DOMINIO)){
			return this.toTable(Pagamento.model().ID_SINGOLO_VERSAMENTO.ID_TRIBUTO.ID_DOMINIO, returnAlias);
		}
		if(field.equals(Pagamento.model().ID_SINGOLO_VERSAMENTO.ID_TRIBUTO.ID_TIPO_TRIBUTO.COD_TRIBUTO)){
			return this.toTable(Pagamento.model().ID_SINGOLO_VERSAMENTO.ID_TRIBUTO.ID_TIPO_TRIBUTO, returnAlias);
		}
		if(field.equals(Pagamento.model().ID_SINGOLO_VERSAMENTO.NOTE)){
			return this.toTable(Pagamento.model().ID_SINGOLO_VERSAMENTO, returnAlias);
		}
		if(field.equals(Pagamento.model().COD_SINGOLO_VERSAMENTO_ENTE)){
			return this.toTable(Pagamento.model(), returnAlias);
		}
		if(field.equals(Pagamento.model().IMPORTO_PAGATO)){
			return this.toTable(Pagamento.model(), returnAlias);
		}
		if(field.equals(Pagamento.model().DATA_ACQUISIZIONE)){
			return this.toTable(Pagamento.model(), returnAlias);
		}
		if(field.equals(Pagamento.model().IUR)){
			return this.toTable(Pagamento.model(), returnAlias);
		}
		if(field.equals(Pagamento.model().DATA_PAGAMENTO)){
			return this.toTable(Pagamento.model(), returnAlias);
		}
		if(field.equals(Pagamento.model().IBAN_ACCREDITO)){
			return this.toTable(Pagamento.model(), returnAlias);
		}
		if(field.equals(Pagamento.model().COMMISSIONI_PSP)){
			return this.toTable(Pagamento.model(), returnAlias);
		}
		if(field.equals(Pagamento.model().TIPO_ALLEGATO)){
			return this.toTable(Pagamento.model(), returnAlias);
		}
		if(field.equals(Pagamento.model().ALLEGATO)){
			return this.toTable(Pagamento.model(), returnAlias);
		}
		if(field.equals(Pagamento.model().ID_FR_APPLICAZIONE.ID_FR.COD_FLUSSO)){
			return this.toTable(Pagamento.model().ID_FR_APPLICAZIONE.ID_FR, returnAlias);
		}
		if(field.equals(Pagamento.model().ID_FR_APPLICAZIONE.ID_FR.ANNO_RIFERIMENTO)){
			return this.toTable(Pagamento.model().ID_FR_APPLICAZIONE.ID_FR, returnAlias);
		}
		if(field.equals(Pagamento.model().ID_FR_APPLICAZIONE.ID_APPLICAZIONE.COD_APPLICAZIONE)){
			return this.toTable(Pagamento.model().ID_FR_APPLICAZIONE.ID_APPLICAZIONE, returnAlias);
		}
		if(field.equals(Pagamento.model().RENDICONTAZIONE_ESITO)){
			return this.toTable(Pagamento.model(), returnAlias);
		}
		if(field.equals(Pagamento.model().RENDICONTAZIONE_DATA)){
			return this.toTable(Pagamento.model(), returnAlias);
		}
		if(field.equals(Pagamento.model().CODFLUSSO_RENDICONTAZIONE)){
			return this.toTable(Pagamento.model(), returnAlias);
		}
		if(field.equals(Pagamento.model().ANNO_RIFERIMENTO)){
			return this.toTable(Pagamento.model(), returnAlias);
		}
		if(field.equals(Pagamento.model().INDICE_SINGOLO_PAGAMENTO)){
			return this.toTable(Pagamento.model(), returnAlias);
		}
		if(field.equals(Pagamento.model().ID_RR.COD_MSG_REVOCA)){
			return this.toTable(Pagamento.model().ID_RR, returnAlias);
		}
		if(field.equals(Pagamento.model().DATA_ACQUISIZIONE_REVOCA)){
			return this.toTable(Pagamento.model(), returnAlias);
		}
		if(field.equals(Pagamento.model().CAUSALE_REVOCA)){
			return this.toTable(Pagamento.model(), returnAlias);
		}
		if(field.equals(Pagamento.model().DATI_REVOCA)){
			return this.toTable(Pagamento.model(), returnAlias);
		}
		if(field.equals(Pagamento.model().IMPORTO_REVOCATO)){
			return this.toTable(Pagamento.model(), returnAlias);
		}
		if(field.equals(Pagamento.model().ESITO_REVOCA)){
			return this.toTable(Pagamento.model(), returnAlias);
		}
		if(field.equals(Pagamento.model().DATI_ESITO_REVOCA)){
			return this.toTable(Pagamento.model(), returnAlias);
		}
		if(field.equals(Pagamento.model().ID_FR_APPLICAZIONE_REVOCA.ID_FR.COD_FLUSSO)){
			return this.toTable(Pagamento.model().ID_FR_APPLICAZIONE_REVOCA.ID_FR, returnAlias);
		}
		if(field.equals(Pagamento.model().ID_FR_APPLICAZIONE_REVOCA.ID_FR.ANNO_RIFERIMENTO)){
			return this.toTable(Pagamento.model().ID_FR_APPLICAZIONE_REVOCA.ID_FR, returnAlias);
		}
		if(field.equals(Pagamento.model().ID_FR_APPLICAZIONE_REVOCA.ID_APPLICAZIONE.COD_APPLICAZIONE)){
			return this.toTable(Pagamento.model().ID_FR_APPLICAZIONE_REVOCA.ID_APPLICAZIONE, returnAlias);
		}
		if(field.equals(Pagamento.model().RENDICONTAZIONE_ESITO_REVOCA)){
			return this.toTable(Pagamento.model(), returnAlias);
		}
		if(field.equals(Pagamento.model().RENDICONTAZIONE_DATA_REVOCA)){
			return this.toTable(Pagamento.model(), returnAlias);
		}
		if(field.equals(Pagamento.model().COD_FLUSSO_RENDICONTAZIONE_REVOCA)){
			return this.toTable(Pagamento.model(), returnAlias);
		}
		if(field.equals(Pagamento.model().ANNO_RIFERIMENTO_REVOCA)){
			return this.toTable(Pagamento.model(), returnAlias);
		}
		if(field.equals(Pagamento.model().INDICE_SINGOLO_PAGAMENTO_REVOCA)){
			return this.toTable(Pagamento.model(), returnAlias);
		}


		return super.toTable(field,returnAlias);
		
	}

	@Override
	public String toTable(IModel<?> model,boolean returnAlias) throws ExpressionException {
		
		// In the case of table with alias, using parameter returnAlias​​, 
		// it is possible to drive the choice whether to return only the alias or 
		// the full definition of the table containing the alias
		
		if(model.equals(Pagamento.model())){
			return "pagamenti";
		}
		if(model.equals(Pagamento.model().ID_RPT)){
			return "rpt";
		}
		if(model.equals(Pagamento.model().ID_SINGOLO_VERSAMENTO)){
			return "singoli_versamenti";
		}
		if(model.equals(Pagamento.model().ID_SINGOLO_VERSAMENTO.ID_VERSAMENTO)){
			return "versamenti";
		}
		if(model.equals(Pagamento.model().ID_SINGOLO_VERSAMENTO.ID_VERSAMENTO.ID_APPLICAZIONE)){
			return "applicazioni";
		}
		if(model.equals(Pagamento.model().ID_SINGOLO_VERSAMENTO.ID_TRIBUTO)){
			return "tributi";
		}
		if(model.equals(Pagamento.model().ID_SINGOLO_VERSAMENTO.ID_TRIBUTO.ID_DOMINIO)){
			return "domini";
		}
		if(model.equals(Pagamento.model().ID_FR_APPLICAZIONE)){
			return "fr_applicazioni";
		}
		if(model.equals(Pagamento.model().ID_FR_APPLICAZIONE.ID_FR)){
			return "fr";
		}
		if(model.equals(Pagamento.model().ID_FR_APPLICAZIONE.ID_APPLICAZIONE)){
			return "applicazioni";
		}
		if(model.equals(Pagamento.model().ID_RR)){
			return "rr";
		}
		if(model.equals(Pagamento.model().ID_FR_APPLICAZIONE_REVOCA)){
			return "fr_applicazioni";
		}
		if(model.equals(Pagamento.model().ID_FR_APPLICAZIONE_REVOCA.ID_FR)){
			return "fr";
		}
		if(model.equals(Pagamento.model().ID_FR_APPLICAZIONE_REVOCA.ID_APPLICAZIONE)){
			return "applicazioni";
		}


		return super.toTable(model,returnAlias);
		
	}

}
