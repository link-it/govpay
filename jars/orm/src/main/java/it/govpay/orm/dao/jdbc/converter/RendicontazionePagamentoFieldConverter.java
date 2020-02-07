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

import it.govpay.orm.RendicontazionePagamento;


/**     
 * RendicontazionePagamentoFieldConverter
 *
 * @author Giovanni Bussu (bussu@link.it)
 * @author Lorenzo Nardi (nardi@link.it)
 * @author $Author$
 * @version $Rev$, $Date$
 */
public class RendicontazionePagamentoFieldConverter extends AbstractSQLFieldConverter {

	private TipiDatabase databaseType;
	
	public RendicontazionePagamentoFieldConverter(String databaseType){
		this.databaseType = TipiDatabase.toEnumConstant(databaseType);
	}
	public RendicontazionePagamentoFieldConverter(TipiDatabase databaseType){
		this.databaseType = databaseType;
	}


	@Override
	public IModel<?> getRootModel() throws ExpressionException {
		return RendicontazionePagamento.model().FR;
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
		
		if(field.equals(RendicontazionePagamento.model().FR.COD_PSP)){
			if(appendTablePrefix){
				return this.toAliasTable(field)+".cod_psp";
			}else{
				return "cod_psp";
			}
		}
		if(field.equals(RendicontazionePagamento.model().FR.COD_DOMINIO)){
			if(appendTablePrefix){
				return this.toAliasTable(field)+".cod_dominio";
			}else{
				return "cod_dominio";
			}
		}
		if(field.equals(RendicontazionePagamento.model().FR.COD_FLUSSO)){
			if(appendTablePrefix){
				return this.toAliasTable(field)+".cod_flusso";
			}else{
				return "cod_flusso";
			}
		}
		if(field.equals(RendicontazionePagamento.model().FR.STATO)){
			if(appendTablePrefix){
				return this.toAliasTable(field)+".stato";
			}else{
				return "stato";
			}
		}
		if(field.equals(RendicontazionePagamento.model().FR.DESCRIZIONE_STATO)){
			if(appendTablePrefix){
				return this.toAliasTable(field)+".descrizione_stato";
			}else{
				return "descrizione_stato";
			}
		}
		if(field.equals(RendicontazionePagamento.model().FR.IUR)){
			if(appendTablePrefix){
				return this.toAliasTable(field)+".iur";
			}else{
				return "iur";
			}
		}
		if(field.equals(RendicontazionePagamento.model().FR.DATA_ORA_FLUSSO)){
			if(appendTablePrefix){
				return this.toAliasTable(field)+".data_ora_flusso";
			}else{
				return "data_ora_flusso";
			}
		}
		if(field.equals(RendicontazionePagamento.model().FR.DATA_REGOLAMENTO)){
			if(appendTablePrefix){
				return this.toAliasTable(field)+".data_regolamento";
			}else{
				return "data_regolamento";
			}
		}
		if(field.equals(RendicontazionePagamento.model().FR.DATA_ACQUISIZIONE)){
			if(appendTablePrefix){
				return this.toAliasTable(field)+".data_acquisizione";
			}else{
				return "data_acquisizione";
			}
		}
		if(field.equals(RendicontazionePagamento.model().FR.NUMERO_PAGAMENTI)){
			if(appendTablePrefix){
				return this.toAliasTable(field)+".numero_pagamenti";
			}else{
				return "numero_pagamenti";
			}
		}
		if(field.equals(RendicontazionePagamento.model().FR.IMPORTO_TOTALE_PAGAMENTI)){
			if(appendTablePrefix){
				return this.toAliasTable(field)+".importo_totale_pagamenti";
			}else{
				return "importo_totale_pagamenti";
			}
		}
		if(field.equals(RendicontazionePagamento.model().FR.COD_BIC_RIVERSAMENTO)){
			if(appendTablePrefix){
				return this.toAliasTable(field)+".cod_bic_riversamento";
			}else{
				return "cod_bic_riversamento";
			}
		}
		if(field.equals(RendicontazionePagamento.model().FR.XML)){
			if(appendTablePrefix){
				return this.toAliasTable(field)+".xml";
			}else{
				return "xml";
			}
		}
		if(field.equals(RendicontazionePagamento.model().FR.ID_SINGOLO_VERSAMENTO.ID_VERSAMENTO.COD_VERSAMENTO_ENTE)){
			if(appendTablePrefix){
				return this.toAliasTable(field)+".cod_versamento_ente";
			}else{
				return "cod_versamento_ente";
			}
		}
		if(field.equals(RendicontazionePagamento.model().FR.ID_SINGOLO_VERSAMENTO.ID_VERSAMENTO.ID_APPLICAZIONE.COD_APPLICAZIONE)){
			if(appendTablePrefix){
				return this.toAliasTable(field)+".cod_applicazione";
			}else{
				return "cod_applicazione";
			}
		}
		if(field.equals(RendicontazionePagamento.model().FR.ID_SINGOLO_VERSAMENTO.ID_VERSAMENTO.DEBITORE_IDENTIFICATIVO)){
			if(appendTablePrefix){
				return this.toAliasTable(field)+".debitore_identificativo";
			}else{
				return "debitore_identificativo";
			}
		}
		if(field.equals(RendicontazionePagamento.model().FR.ID_SINGOLO_VERSAMENTO.ID_VERSAMENTO.DEBITORE_ANAGRAFICA)){
			if(appendTablePrefix){
				return this.toAliasTable(field)+".debitore_anagrafica";
			}else{
				return "debitore_anagrafica";
			}
		}
		if(field.equals(RendicontazionePagamento.model().FR.ID_SINGOLO_VERSAMENTO.ID_VERSAMENTO.COD_VERSAMENTO_LOTTO)){
			if(appendTablePrefix){
				return this.toAliasTable(field)+".cod_versamento_lotto";
			}else{
				return "cod_versamento_lotto";
			}
		}
		if(field.equals(RendicontazionePagamento.model().FR.ID_SINGOLO_VERSAMENTO.ID_VERSAMENTO.COD_ANNO_TRIBUTARIO)){
			if(appendTablePrefix){
				return this.toAliasTable(field)+".cod_anno_tributario";
			}else{
				return "cod_anno_tributario";
			}
		}
		if(field.equals(RendicontazionePagamento.model().FR.ID_SINGOLO_VERSAMENTO.ID_VERSAMENTO.IMPORTO_TOTALE)){
			if(appendTablePrefix){
				return this.toAliasTable(field)+".importo_totale";
			}else{
				return "importo_totale";
			}
		}
		if(field.equals(RendicontazionePagamento.model().FR.ID_SINGOLO_VERSAMENTO.ID_VERSAMENTO.CAUSALE_VERSAMENTO)){
			if(appendTablePrefix){
				return this.toAliasTable(field)+".causale_versamento";
			}else{
				return "causale_versamento";
			}
		}
		if(field.equals(RendicontazionePagamento.model().FR.ID_SINGOLO_VERSAMENTO.ID_VERSAMENTO.STATO_VERSAMENTO)){
			if(appendTablePrefix){
				return this.toAliasTable(field)+".stato_versamento";
			}else{
				return "stato_versamento";
			}
		}
		if(field.equals(RendicontazionePagamento.model().FR.ID_SINGOLO_VERSAMENTO.ID_VERSAMENTO.ID_UO.COD_UO)){
			if(appendTablePrefix){
				return this.toAliasTable(field)+".cod_uo";
			}else{
				return "cod_uo";
			}
		}
		if(field.equals(RendicontazionePagamento.model().FR.ID_SINGOLO_VERSAMENTO.ID_VERSAMENTO.ID_UO.ID_DOMINIO.COD_DOMINIO)){
			if(appendTablePrefix){
				return this.toAliasTable(field)+".cod_dominio";
			}else{
				return "cod_dominio";
			}
		}
		if(field.equals(RendicontazionePagamento.model().FR.ID_SINGOLO_VERSAMENTO.ID_VERSAMENTO.ID_TIPO_VERSAMENTO.COD_TIPO_VERSAMENTO)){
			if(appendTablePrefix){
				return this.toAliasTable(field)+".cod_tipo_versamento";
			}else{
				return "cod_tipo_versamento";
			}
		}
		if(field.equals(RendicontazionePagamento.model().FR.ID_SINGOLO_VERSAMENTO.ID_VERSAMENTO.ID_TIPO_VERSAMENTO.TIPO)){
			if(appendTablePrefix){
				return this.toAliasTable(field)+".tipo";
			}else{
				return "tipo";
			}
		}
		if(field.equals(RendicontazionePagamento.model().FR.ID_SINGOLO_VERSAMENTO.ID_VERSAMENTO.DIVISIONE)){
			if(appendTablePrefix){
				return this.toAliasTable(field)+".divisione";
			}else{
				return "divisione";
			}
		}
		if(field.equals(RendicontazionePagamento.model().FR.ID_SINGOLO_VERSAMENTO.ID_VERSAMENTO.DIREZIONE)){
			if(appendTablePrefix){
				return this.toAliasTable(field)+".direzione";
			}else{
				return "direzione";
			}
		}
		if(field.equals(RendicontazionePagamento.model().FR.ID_SINGOLO_VERSAMENTO.ID_VERSAMENTO.TASSONOMIA)){
			if(appendTablePrefix){
				return this.toAliasTable(field)+".tassonomia";
			}else{
				return "tassonomia";
			}
		}
		if(field.equals(RendicontazionePagamento.model().FR.ID_SINGOLO_VERSAMENTO.COD_SINGOLO_VERSAMENTO_ENTE)){
			if(appendTablePrefix){
				return this.toAliasTable(field)+".cod_singolo_versamento_ente";
			}else{
				return "cod_singolo_versamento_ente";
			}
		}
		if(field.equals(RendicontazionePagamento.model().FR.ID_SINGOLO_VERSAMENTO.INDICE_DATI)){
			if(appendTablePrefix){
				return this.toAliasTable(field)+".indice_dati";
			}else{
				return "indice_dati";
			}
		}
		if(field.equals(RendicontazionePagamento.model().FR.ID_SINGOLO_VERSAMENTO.ID_TRIBUTO.ID_DOMINIO.COD_DOMINIO)){
			if(appendTablePrefix){
				return this.toAliasTable(field)+".cod_dominio";
			}else{
				return "cod_dominio";
			}
		}
		if(field.equals(RendicontazionePagamento.model().FR.ID_SINGOLO_VERSAMENTO.ID_TRIBUTO.ID_TIPO_TRIBUTO.COD_TRIBUTO)){
			if(appendTablePrefix){
				return this.toAliasTable(field)+".cod_tributo";
			}else{
				return "cod_tributo";
			}
		}
		if(field.equals(RendicontazionePagamento.model().FR.ID_SINGOLO_VERSAMENTO.NOTE)){
			if(appendTablePrefix){
				return this.toAliasTable(field)+".note";
			}else{
				return "note";
			}
		}
		if(field.equals(RendicontazionePagamento.model().FR.ID_SINGOLO_VERSAMENTO.STATO_SINGOLO_VERSAMENTO)){
			if(appendTablePrefix){
				return this.toAliasTable(field)+".stato_singolo_versamento";
			}else{
				return "stato_singolo_versamento";
			}
		}
		if(field.equals(RendicontazionePagamento.model().FR.ID_SINGOLO_VERSAMENTO.IMPORTO_SINGOLO_VERSAMENTO)){
			if(appendTablePrefix){
				return this.toAliasTable(field)+".importo_singolo_versamento";
			}else{
				return "importo_singolo_versamento";
			}
		}
		if(field.equals(RendicontazionePagamento.model().FR.ID_INCASSO.TRN)){
			if(appendTablePrefix){
				return this.toAliasTable(field)+".trn";
			}else{
				return "trn";
			}
		}
		if(field.equals(RendicontazionePagamento.model().FR.ID_INCASSO.COD_DOMINIO)){
			if(appendTablePrefix){
				return this.toAliasTable(field)+".cod_dominio";
			}else{
				return "cod_dominio";
			}
		}
		if(field.equals(RendicontazionePagamento.model().RENDICONTAZIONE.IUV)){
			if(appendTablePrefix){
				return this.toAliasTable(field)+".iuv";
			}else{
				return "iuv";
			}
		}
		if(field.equals(RendicontazionePagamento.model().RENDICONTAZIONE.IUR)){
			if(appendTablePrefix){
				return this.toAliasTable(field)+".iur";
			}else{
				return "iur";
			}
		}
		if(field.equals(RendicontazionePagamento.model().RENDICONTAZIONE.INDICE_DATI)){
			if(appendTablePrefix){
				return this.toAliasTable(field)+".indice_dati";
			}else{
				return "indice_dati";
			}
		}
		if(field.equals(RendicontazionePagamento.model().RENDICONTAZIONE.IMPORTO_PAGATO)){
			if(appendTablePrefix){
				return this.toAliasTable(field)+".importo_pagato";
			}else{
				return "importo_pagato";
			}
		}
		if(field.equals(RendicontazionePagamento.model().RENDICONTAZIONE.ESITO)){
			if(appendTablePrefix){
				return this.toAliasTable(field)+".esito";
			}else{
				return "esito";
			}
		}
		if(field.equals(RendicontazionePagamento.model().RENDICONTAZIONE.DATA)){
			if(appendTablePrefix){
				return this.toAliasTable(field)+".data";
			}else{
				return "data";
			}
		}
		if(field.equals(RendicontazionePagamento.model().RENDICONTAZIONE.STATO)){
			if(appendTablePrefix){
				return this.toAliasTable(field)+".stato";
			}else{
				return "stato";
			}
		}
		if(field.equals(RendicontazionePagamento.model().RENDICONTAZIONE.ANOMALIE)){
			if(appendTablePrefix){
				return this.toAliasTable(field)+".anomalie";
			}else{
				return "anomalie";
			}
		}
		if(field.equals(RendicontazionePagamento.model().RENDICONTAZIONE.ID_FR.COD_FLUSSO)){
			if(appendTablePrefix){
				return this.toAliasTable(field)+".cod_flusso";
			}else{
				return "cod_flusso";
			}
		}
		if(field.equals(RendicontazionePagamento.model().RENDICONTAZIONE.ID_FR.COD_DOMINIO)){
			if(appendTablePrefix){
				return this.toAliasTable(field)+".cod_dominio";
			}else{
				return "cod_dominio";
			}
		}
		if(field.equals(RendicontazionePagamento.model().RENDICONTAZIONE.ID_FR.DATA_ORA_FLUSSO)){
			if(appendTablePrefix){
				return this.toAliasTable(field)+".data_ora_flusso";
			}else{
				return "data_ora_flusso";
			}
		}
		if(field.equals(RendicontazionePagamento.model().RENDICONTAZIONE.ID_PAGAMENTO.ID_PAGAMENTO)){
			if(appendTablePrefix){
				return this.toAliasTable(field)+".id_pagamento";
			}else{
				return "id_pagamento";
			}
		}
		if(field.equals(RendicontazionePagamento.model().RENDICONTAZIONE.ID_PAGAMENTO.INDICE_DATI)){
			if(appendTablePrefix){
				return this.toAliasTable(field)+".indice_dati";
			}else{
				return "indice_dati";
			}
		}
		if(field.equals(RendicontazionePagamento.model().RENDICONTAZIONE.ID_PAGAMENTO.IUV)){
			if(appendTablePrefix){
				return this.toAliasTable(field)+".iuv";
			}else{
				return "iuv";
			}
		}
		if(field.equals(RendicontazionePagamento.model().RENDICONTAZIONE.ID_SINGOLO_VERSAMENTO.ID_VERSAMENTO.COD_VERSAMENTO_ENTE)){
			if(appendTablePrefix){
				return this.toAliasTable(field)+".cod_versamento_ente";
			}else{
				return "cod_versamento_ente";
			}
		}
		if(field.equals(RendicontazionePagamento.model().RENDICONTAZIONE.ID_SINGOLO_VERSAMENTO.ID_VERSAMENTO.ID_APPLICAZIONE.COD_APPLICAZIONE)){
			if(appendTablePrefix){
				return this.toAliasTable(field)+".cod_applicazione";
			}else{
				return "cod_applicazione";
			}
		}
		if(field.equals(RendicontazionePagamento.model().RENDICONTAZIONE.ID_SINGOLO_VERSAMENTO.ID_VERSAMENTO.DEBITORE_IDENTIFICATIVO)){
			if(appendTablePrefix){
				return this.toAliasTable(field)+".debitore_identificativo";
			}else{
				return "debitore_identificativo";
			}
		}
		if(field.equals(RendicontazionePagamento.model().RENDICONTAZIONE.ID_SINGOLO_VERSAMENTO.ID_VERSAMENTO.DEBITORE_ANAGRAFICA)){
			if(appendTablePrefix){
				return this.toAliasTable(field)+".debitore_anagrafica";
			}else{
				return "debitore_anagrafica";
			}
		}
		if(field.equals(RendicontazionePagamento.model().RENDICONTAZIONE.ID_SINGOLO_VERSAMENTO.ID_VERSAMENTO.COD_VERSAMENTO_LOTTO)){
			if(appendTablePrefix){
				return this.toAliasTable(field)+".cod_versamento_lotto";
			}else{
				return "cod_versamento_lotto";
			}
		}
		if(field.equals(RendicontazionePagamento.model().RENDICONTAZIONE.ID_SINGOLO_VERSAMENTO.ID_VERSAMENTO.COD_ANNO_TRIBUTARIO)){
			if(appendTablePrefix){
				return this.toAliasTable(field)+".cod_anno_tributario";
			}else{
				return "cod_anno_tributario";
			}
		}
		if(field.equals(RendicontazionePagamento.model().RENDICONTAZIONE.ID_SINGOLO_VERSAMENTO.ID_VERSAMENTO.IMPORTO_TOTALE)){
			if(appendTablePrefix){
				return this.toAliasTable(field)+".importo_totale";
			}else{
				return "importo_totale";
			}
		}
		if(field.equals(RendicontazionePagamento.model().RENDICONTAZIONE.ID_SINGOLO_VERSAMENTO.ID_VERSAMENTO.CAUSALE_VERSAMENTO)){
			if(appendTablePrefix){
				return this.toAliasTable(field)+".causale_versamento";
			}else{
				return "causale_versamento";
			}
		}
		if(field.equals(RendicontazionePagamento.model().RENDICONTAZIONE.ID_SINGOLO_VERSAMENTO.ID_VERSAMENTO.STATO_VERSAMENTO)){
			if(appendTablePrefix){
				return this.toAliasTable(field)+".stato_versamento";
			}else{
				return "stato_versamento";
			}
		}
		if(field.equals(RendicontazionePagamento.model().RENDICONTAZIONE.ID_SINGOLO_VERSAMENTO.ID_VERSAMENTO.ID_UO.COD_UO)){
			if(appendTablePrefix){
				return this.toAliasTable(field)+".cod_uo";
			}else{
				return "cod_uo";
			}
		}
		if(field.equals(RendicontazionePagamento.model().RENDICONTAZIONE.ID_SINGOLO_VERSAMENTO.ID_VERSAMENTO.ID_UO.ID_DOMINIO.COD_DOMINIO)){
			if(appendTablePrefix){
				return this.toAliasTable(field)+".cod_dominio";
			}else{
				return "cod_dominio";
			}
		}
		if(field.equals(RendicontazionePagamento.model().RENDICONTAZIONE.ID_SINGOLO_VERSAMENTO.ID_VERSAMENTO.ID_TIPO_VERSAMENTO.COD_TIPO_VERSAMENTO)){
			if(appendTablePrefix){
				return this.toAliasTable(field)+".cod_tipo_versamento";
			}else{
				return "cod_tipo_versamento";
			}
		}
		if(field.equals(RendicontazionePagamento.model().RENDICONTAZIONE.ID_SINGOLO_VERSAMENTO.ID_VERSAMENTO.ID_TIPO_VERSAMENTO.TIPO)){
			if(appendTablePrefix){
				return this.toAliasTable(field)+".tipo";
			}else{
				return "tipo";
			}
		}
		if(field.equals(RendicontazionePagamento.model().RENDICONTAZIONE.ID_SINGOLO_VERSAMENTO.ID_VERSAMENTO.DIVISIONE)){
			if(appendTablePrefix){
				return this.toAliasTable(field)+".divisione";
			}else{
				return "divisione";
			}
		}
		if(field.equals(RendicontazionePagamento.model().RENDICONTAZIONE.ID_SINGOLO_VERSAMENTO.ID_VERSAMENTO.DIREZIONE)){
			if(appendTablePrefix){
				return this.toAliasTable(field)+".direzione";
			}else{
				return "direzione";
			}
		}
		if(field.equals(RendicontazionePagamento.model().RENDICONTAZIONE.ID_SINGOLO_VERSAMENTO.ID_VERSAMENTO.TASSONOMIA)){
			if(appendTablePrefix){
				return this.toAliasTable(field)+".tassonomia";
			}else{
				return "tassonomia";
			}
		}
		if(field.equals(RendicontazionePagamento.model().RENDICONTAZIONE.ID_SINGOLO_VERSAMENTO.COD_SINGOLO_VERSAMENTO_ENTE)){
			if(appendTablePrefix){
				return this.toAliasTable(field)+".cod_singolo_versamento_ente";
			}else{
				return "cod_singolo_versamento_ente";
			}
		}
		if(field.equals(RendicontazionePagamento.model().RENDICONTAZIONE.ID_SINGOLO_VERSAMENTO.INDICE_DATI)){
			if(appendTablePrefix){
				return this.toAliasTable(field)+".indice_dati";
			}else{
				return "indice_dati";
			}
		}
		if(field.equals(RendicontazionePagamento.model().RENDICONTAZIONE.ID_SINGOLO_VERSAMENTO.ID_TRIBUTO.ID_DOMINIO.COD_DOMINIO)){
			if(appendTablePrefix){
				return this.toAliasTable(field)+".cod_dominio";
			}else{
				return "cod_dominio";
			}
		}
		if(field.equals(RendicontazionePagamento.model().RENDICONTAZIONE.ID_SINGOLO_VERSAMENTO.ID_TRIBUTO.ID_TIPO_TRIBUTO.COD_TRIBUTO)){
			if(appendTablePrefix){
				return this.toAliasTable(field)+".cod_tributo";
			}else{
				return "cod_tributo";
			}
		}
		if(field.equals(RendicontazionePagamento.model().RENDICONTAZIONE.ID_SINGOLO_VERSAMENTO.NOTE)){
			if(appendTablePrefix){
				return this.toAliasTable(field)+".note";
			}else{
				return "note";
			}
		}
		if(field.equals(RendicontazionePagamento.model().RENDICONTAZIONE.ID_SINGOLO_VERSAMENTO.STATO_SINGOLO_VERSAMENTO)){
			if(appendTablePrefix){
				return this.toAliasTable(field)+".stato_singolo_versamento";
			}else{
				return "stato_singolo_versamento";
			}
		}
		if(field.equals(RendicontazionePagamento.model().RENDICONTAZIONE.ID_SINGOLO_VERSAMENTO.IMPORTO_SINGOLO_VERSAMENTO)){
			if(appendTablePrefix){
				return this.toAliasTable(field)+".importo_singolo_versamento";
			}else{
				return "importo_singolo_versamento";
			}
		}
		if(field.equals(RendicontazionePagamento.model().PAGAMENTO.ID_RPT.COD_MSG_RICHIESTA)){
			if(appendTablePrefix){
				return this.toAliasTable(field)+".cod_msg_richiesta";
			}else{
				return "cod_msg_richiesta";
			}
		}
		if(field.equals(RendicontazionePagamento.model().PAGAMENTO.ID_RPT.COD_DOMINIO)){
			if(appendTablePrefix){
				return this.toAliasTable(field)+".cod_dominio";
			}else{
				return "cod_dominio";
			}
		}
		if(field.equals(RendicontazionePagamento.model().PAGAMENTO.ID_RPT.IUV)){
			if(appendTablePrefix){
				return this.toAliasTable(field)+".iuv";
			}else{
				return "iuv";
			}
		}
		if(field.equals(RendicontazionePagamento.model().PAGAMENTO.ID_SINGOLO_VERSAMENTO.ID_VERSAMENTO.COD_VERSAMENTO_ENTE)){
			if(appendTablePrefix){
				return this.toAliasTable(field)+".cod_versamento_ente";
			}else{
				return "cod_versamento_ente";
			}
		}
		if(field.equals(RendicontazionePagamento.model().PAGAMENTO.ID_SINGOLO_VERSAMENTO.ID_VERSAMENTO.ID_APPLICAZIONE.COD_APPLICAZIONE)){
			if(appendTablePrefix){
				return this.toAliasTable(field)+".cod_applicazione";
			}else{
				return "cod_applicazione";
			}
		}
		if(field.equals(RendicontazionePagamento.model().PAGAMENTO.ID_SINGOLO_VERSAMENTO.ID_VERSAMENTO.DEBITORE_IDENTIFICATIVO)){
			if(appendTablePrefix){
				return this.toAliasTable(field)+".debitore_identificativo";
			}else{
				return "debitore_identificativo";
			}
		}
		if(field.equals(RendicontazionePagamento.model().PAGAMENTO.ID_SINGOLO_VERSAMENTO.ID_VERSAMENTO.DEBITORE_ANAGRAFICA)){
			if(appendTablePrefix){
				return this.toAliasTable(field)+".debitore_anagrafica";
			}else{
				return "debitore_anagrafica";
			}
		}
		if(field.equals(RendicontazionePagamento.model().PAGAMENTO.ID_SINGOLO_VERSAMENTO.ID_VERSAMENTO.COD_VERSAMENTO_LOTTO)){
			if(appendTablePrefix){
				return this.toAliasTable(field)+".cod_versamento_lotto";
			}else{
				return "cod_versamento_lotto";
			}
		}
		if(field.equals(RendicontazionePagamento.model().PAGAMENTO.ID_SINGOLO_VERSAMENTO.ID_VERSAMENTO.COD_ANNO_TRIBUTARIO)){
			if(appendTablePrefix){
				return this.toAliasTable(field)+".cod_anno_tributario";
			}else{
				return "cod_anno_tributario";
			}
		}
		if(field.equals(RendicontazionePagamento.model().PAGAMENTO.ID_SINGOLO_VERSAMENTO.ID_VERSAMENTO.IMPORTO_TOTALE)){
			if(appendTablePrefix){
				return this.toAliasTable(field)+".importo_totale";
			}else{
				return "importo_totale";
			}
		}
		if(field.equals(RendicontazionePagamento.model().PAGAMENTO.ID_SINGOLO_VERSAMENTO.ID_VERSAMENTO.CAUSALE_VERSAMENTO)){
			if(appendTablePrefix){
				return this.toAliasTable(field)+".causale_versamento";
			}else{
				return "causale_versamento";
			}
		}
		if(field.equals(RendicontazionePagamento.model().PAGAMENTO.ID_SINGOLO_VERSAMENTO.ID_VERSAMENTO.STATO_VERSAMENTO)){
			if(appendTablePrefix){
				return this.toAliasTable(field)+".stato_versamento";
			}else{
				return "stato_versamento";
			}
		}
		if(field.equals(RendicontazionePagamento.model().PAGAMENTO.ID_SINGOLO_VERSAMENTO.ID_VERSAMENTO.ID_UO.COD_UO)){
			if(appendTablePrefix){
				return this.toAliasTable(field)+".cod_uo";
			}else{
				return "cod_uo";
			}
		}
		if(field.equals(RendicontazionePagamento.model().PAGAMENTO.ID_SINGOLO_VERSAMENTO.ID_VERSAMENTO.ID_UO.ID_DOMINIO.COD_DOMINIO)){
			if(appendTablePrefix){
				return this.toAliasTable(field)+".cod_dominio";
			}else{
				return "cod_dominio";
			}
		}
		if(field.equals(RendicontazionePagamento.model().PAGAMENTO.ID_SINGOLO_VERSAMENTO.ID_VERSAMENTO.ID_TIPO_VERSAMENTO.COD_TIPO_VERSAMENTO)){
			if(appendTablePrefix){
				return this.toAliasTable(field)+".cod_tipo_versamento";
			}else{
				return "cod_tipo_versamento";
			}
		}
		if(field.equals(RendicontazionePagamento.model().PAGAMENTO.ID_SINGOLO_VERSAMENTO.ID_VERSAMENTO.ID_TIPO_VERSAMENTO.TIPO)){
			if(appendTablePrefix){
				return this.toAliasTable(field)+".tipo";
			}else{
				return "tipo";
			}
		}
		if(field.equals(RendicontazionePagamento.model().PAGAMENTO.ID_SINGOLO_VERSAMENTO.ID_VERSAMENTO.DIVISIONE)){
			if(appendTablePrefix){
				return this.toAliasTable(field)+".divisione";
			}else{
				return "divisione";
			}
		}
		if(field.equals(RendicontazionePagamento.model().PAGAMENTO.ID_SINGOLO_VERSAMENTO.ID_VERSAMENTO.DIREZIONE)){
			if(appendTablePrefix){
				return this.toAliasTable(field)+".direzione";
			}else{
				return "direzione";
			}
		}
		if(field.equals(RendicontazionePagamento.model().PAGAMENTO.ID_SINGOLO_VERSAMENTO.ID_VERSAMENTO.TASSONOMIA)){
			if(appendTablePrefix){
				return this.toAliasTable(field)+".tassonomia";
			}else{
				return "tassonomia";
			}
		}
		if(field.equals(RendicontazionePagamento.model().PAGAMENTO.ID_SINGOLO_VERSAMENTO.COD_SINGOLO_VERSAMENTO_ENTE)){
			if(appendTablePrefix){
				return this.toAliasTable(field)+".cod_singolo_versamento_ente";
			}else{
				return "cod_singolo_versamento_ente";
			}
		}
		if(field.equals(RendicontazionePagamento.model().PAGAMENTO.ID_SINGOLO_VERSAMENTO.INDICE_DATI)){
			if(appendTablePrefix){
				return this.toAliasTable(field)+".indice_dati";
			}else{
				return "indice_dati";
			}
		}
		if(field.equals(RendicontazionePagamento.model().PAGAMENTO.ID_SINGOLO_VERSAMENTO.ID_TRIBUTO.ID_DOMINIO.COD_DOMINIO)){
			if(appendTablePrefix){
				return this.toAliasTable(field)+".cod_dominio";
			}else{
				return "cod_dominio";
			}
		}
		if(field.equals(RendicontazionePagamento.model().PAGAMENTO.ID_SINGOLO_VERSAMENTO.ID_TRIBUTO.ID_TIPO_TRIBUTO.COD_TRIBUTO)){
			if(appendTablePrefix){
				return this.toAliasTable(field)+".cod_tributo";
			}else{
				return "cod_tributo";
			}
		}
		if(field.equals(RendicontazionePagamento.model().PAGAMENTO.ID_SINGOLO_VERSAMENTO.NOTE)){
			if(appendTablePrefix){
				return this.toAliasTable(field)+".note";
			}else{
				return "note";
			}
		}
		if(field.equals(RendicontazionePagamento.model().PAGAMENTO.ID_SINGOLO_VERSAMENTO.STATO_SINGOLO_VERSAMENTO)){
			if(appendTablePrefix){
				return this.toAliasTable(field)+".stato_singolo_versamento";
			}else{
				return "stato_singolo_versamento";
			}
		}
		if(field.equals(RendicontazionePagamento.model().PAGAMENTO.ID_SINGOLO_VERSAMENTO.IMPORTO_SINGOLO_VERSAMENTO)){
			if(appendTablePrefix){
				return this.toAliasTable(field)+".importo_singolo_versamento";
			}else{
				return "importo_singolo_versamento";
			}
		}
		if(field.equals(RendicontazionePagamento.model().PAGAMENTO.COD_DOMINIO)){
			if(appendTablePrefix){
				return this.toAliasTable(field)+".cod_dominio";
			}else{
				return "cod_dominio";
			}
		}
		if(field.equals(RendicontazionePagamento.model().PAGAMENTO.IUV)){
			if(appendTablePrefix){
				return this.toAliasTable(field)+".iuv";
			}else{
				return "iuv";
			}
		}
		if(field.equals(RendicontazionePagamento.model().PAGAMENTO.INDICE_DATI)){
			if(appendTablePrefix){
				return this.toAliasTable(field)+".indice_dati";
			}else{
				return "indice_dati";
			}
		}
		if(field.equals(RendicontazionePagamento.model().PAGAMENTO.IMPORTO_PAGATO)){
			if(appendTablePrefix){
				return this.toAliasTable(field)+".importo_pagato";
			}else{
				return "importo_pagato";
			}
		}
		if(field.equals(RendicontazionePagamento.model().PAGAMENTO.DATA_ACQUISIZIONE)){
			if(appendTablePrefix){
				return this.toAliasTable(field)+".data_acquisizione";
			}else{
				return "data_acquisizione";
			}
		}
		if(field.equals(RendicontazionePagamento.model().PAGAMENTO.IUR)){
			if(appendTablePrefix){
				return this.toAliasTable(field)+".iur";
			}else{
				return "iur";
			}
		}
		if(field.equals(RendicontazionePagamento.model().PAGAMENTO.DATA_PAGAMENTO)){
			if(appendTablePrefix){
				return this.toAliasTable(field)+".data_pagamento";
			}else{
				return "data_pagamento";
			}
		}
		if(field.equals(RendicontazionePagamento.model().PAGAMENTO.COMMISSIONI_PSP)){
			if(appendTablePrefix){
				return this.toAliasTable(field)+".commissioni_psp";
			}else{
				return "commissioni_psp";
			}
		}
		if(field.equals(RendicontazionePagamento.model().PAGAMENTO.TIPO_ALLEGATO)){
			if(appendTablePrefix){
				return this.toAliasTable(field)+".tipo_allegato";
			}else{
				return "tipo_allegato";
			}
		}
		if(field.equals(RendicontazionePagamento.model().PAGAMENTO.ALLEGATO)){
			if(appendTablePrefix){
				return this.toAliasTable(field)+".allegato";
			}else{
				return "allegato";
			}
		}
		if(field.equals(RendicontazionePagamento.model().PAGAMENTO.ID_RR.COD_MSG_REVOCA)){
			if(appendTablePrefix){
				return this.toAliasTable(field)+".cod_msg_revoca";
			}else{
				return "cod_msg_revoca";
			}
		}
		if(field.equals(RendicontazionePagamento.model().PAGAMENTO.DATA_ACQUISIZIONE_REVOCA)){
			if(appendTablePrefix){
				return this.toAliasTable(field)+".data_acquisizione_revoca";
			}else{
				return "data_acquisizione_revoca";
			}
		}
		if(field.equals(RendicontazionePagamento.model().PAGAMENTO.CAUSALE_REVOCA)){
			if(appendTablePrefix){
				return this.toAliasTable(field)+".causale_revoca";
			}else{
				return "causale_revoca";
			}
		}
		if(field.equals(RendicontazionePagamento.model().PAGAMENTO.DATI_REVOCA)){
			if(appendTablePrefix){
				return this.toAliasTable(field)+".dati_revoca";
			}else{
				return "dati_revoca";
			}
		}
		if(field.equals(RendicontazionePagamento.model().PAGAMENTO.IMPORTO_REVOCATO)){
			if(appendTablePrefix){
				return this.toAliasTable(field)+".importo_revocato";
			}else{
				return "importo_revocato";
			}
		}
		if(field.equals(RendicontazionePagamento.model().PAGAMENTO.ESITO_REVOCA)){
			if(appendTablePrefix){
				return this.toAliasTable(field)+".esito_revoca";
			}else{
				return "esito_revoca";
			}
		}
		if(field.equals(RendicontazionePagamento.model().PAGAMENTO.DATI_ESITO_REVOCA)){
			if(appendTablePrefix){
				return this.toAliasTable(field)+".dati_esito_revoca";
			}else{
				return "dati_esito_revoca";
			}
		}
		if(field.equals(RendicontazionePagamento.model().PAGAMENTO.STATO)){
			if(appendTablePrefix){
				return this.toAliasTable(field)+".stato";
			}else{
				return "stato";
			}
		}
		if(field.equals(RendicontazionePagamento.model().PAGAMENTO.ID_INCASSO.TRN)){
			if(appendTablePrefix){
				return this.toAliasTable(field)+".trn";
			}else{
				return "trn";
			}
		}
		if(field.equals(RendicontazionePagamento.model().PAGAMENTO.ID_INCASSO.COD_DOMINIO)){
			if(appendTablePrefix){
				return this.toAliasTable(field)+".cod_dominio";
			}else{
				return "cod_dominio";
			}
		}
		if(field.equals(RendicontazionePagamento.model().PAGAMENTO.TIPO)){
			if(appendTablePrefix){
				return this.toAliasTable(field)+".tipo";
			}else{
				return "tipo";
			}
		}
		if(field.equals(RendicontazionePagamento.model().SINGOLO_VERSAMENTO.ID_VERSAMENTO.COD_VERSAMENTO_ENTE)){
			if(appendTablePrefix){
				return this.toAliasTable(field)+".cod_versamento_ente";
			}else{
				return "cod_versamento_ente";
			}
		}
		if(field.equals(RendicontazionePagamento.model().SINGOLO_VERSAMENTO.ID_VERSAMENTO.ID_APPLICAZIONE.COD_APPLICAZIONE)){
			if(appendTablePrefix){
				return this.toAliasTable(field)+".cod_applicazione";
			}else{
				return "cod_applicazione";
			}
		}
		if(field.equals(RendicontazionePagamento.model().SINGOLO_VERSAMENTO.ID_VERSAMENTO.DEBITORE_IDENTIFICATIVO)){
			if(appendTablePrefix){
				return this.toAliasTable(field)+".debitore_identificativo";
			}else{
				return "debitore_identificativo";
			}
		}
		if(field.equals(RendicontazionePagamento.model().SINGOLO_VERSAMENTO.ID_VERSAMENTO.DEBITORE_ANAGRAFICA)){
			if(appendTablePrefix){
				return this.toAliasTable(field)+".debitore_anagrafica";
			}else{
				return "debitore_anagrafica";
			}
		}
		if(field.equals(RendicontazionePagamento.model().SINGOLO_VERSAMENTO.ID_VERSAMENTO.COD_VERSAMENTO_LOTTO)){
			if(appendTablePrefix){
				return this.toAliasTable(field)+".cod_versamento_lotto";
			}else{
				return "cod_versamento_lotto";
			}
		}
		if(field.equals(RendicontazionePagamento.model().SINGOLO_VERSAMENTO.ID_VERSAMENTO.COD_ANNO_TRIBUTARIO)){
			if(appendTablePrefix){
				return this.toAliasTable(field)+".cod_anno_tributario";
			}else{
				return "cod_anno_tributario";
			}
		}
		if(field.equals(RendicontazionePagamento.model().SINGOLO_VERSAMENTO.ID_VERSAMENTO.IMPORTO_TOTALE)){
			if(appendTablePrefix){
				return this.toAliasTable(field)+".importo_totale";
			}else{
				return "importo_totale";
			}
		}
		if(field.equals(RendicontazionePagamento.model().SINGOLO_VERSAMENTO.ID_VERSAMENTO.CAUSALE_VERSAMENTO)){
			if(appendTablePrefix){
				return this.toAliasTable(field)+".causale_versamento";
			}else{
				return "causale_versamento";
			}
		}
		if(field.equals(RendicontazionePagamento.model().SINGOLO_VERSAMENTO.ID_VERSAMENTO.STATO_VERSAMENTO)){
			if(appendTablePrefix){
				return this.toAliasTable(field)+".stato_versamento";
			}else{
				return "stato_versamento";
			}
		}
		if(field.equals(RendicontazionePagamento.model().SINGOLO_VERSAMENTO.ID_VERSAMENTO.ID_UO.COD_UO)){
			if(appendTablePrefix){
				return this.toAliasTable(field)+".cod_uo";
			}else{
				return "cod_uo";
			}
		}
		if(field.equals(RendicontazionePagamento.model().SINGOLO_VERSAMENTO.ID_VERSAMENTO.ID_UO.ID_DOMINIO.COD_DOMINIO)){
			if(appendTablePrefix){
				return this.toAliasTable(field)+".cod_dominio";
			}else{
				return "cod_dominio";
			}
		}
		if(field.equals(RendicontazionePagamento.model().SINGOLO_VERSAMENTO.ID_VERSAMENTO.ID_TIPO_VERSAMENTO.COD_TIPO_VERSAMENTO)){
			if(appendTablePrefix){
				return this.toAliasTable(field)+".cod_tipo_versamento";
			}else{
				return "cod_tipo_versamento";
			}
		}
		if(field.equals(RendicontazionePagamento.model().SINGOLO_VERSAMENTO.ID_VERSAMENTO.ID_TIPO_VERSAMENTO.TIPO)){
			if(appendTablePrefix){
				return this.toAliasTable(field)+".tipo";
			}else{
				return "tipo";
			}
		}
		if(field.equals(RendicontazionePagamento.model().SINGOLO_VERSAMENTO.ID_VERSAMENTO.DIVISIONE)){
			if(appendTablePrefix){
				return this.toAliasTable(field)+".divisione";
			}else{
				return "divisione";
			}
		}
		if(field.equals(RendicontazionePagamento.model().SINGOLO_VERSAMENTO.ID_VERSAMENTO.DIREZIONE)){
			if(appendTablePrefix){
				return this.toAliasTable(field)+".direzione";
			}else{
				return "direzione";
			}
		}
		if(field.equals(RendicontazionePagamento.model().SINGOLO_VERSAMENTO.ID_VERSAMENTO.TASSONOMIA)){
			if(appendTablePrefix){
				return this.toAliasTable(field)+".tassonomia";
			}else{
				return "tassonomia";
			}
		}
		if(field.equals(RendicontazionePagamento.model().SINGOLO_VERSAMENTO.ID_TRIBUTO.ID_DOMINIO.COD_DOMINIO)){
			if(appendTablePrefix){
				return this.toAliasTable(field)+".cod_dominio";
			}else{
				return "cod_dominio";
			}
		}
		if(field.equals(RendicontazionePagamento.model().SINGOLO_VERSAMENTO.ID_TRIBUTO.ID_TIPO_TRIBUTO.COD_TRIBUTO)){
			if(appendTablePrefix){
				return this.toAliasTable(field)+".cod_tributo";
			}else{
				return "cod_tributo";
			}
		}
		if(field.equals(RendicontazionePagamento.model().SINGOLO_VERSAMENTO.COD_SINGOLO_VERSAMENTO_ENTE)){
			if(appendTablePrefix){
				return this.toAliasTable(field)+".cod_singolo_versamento_ente";
			}else{
				return "cod_singolo_versamento_ente";
			}
		}
		if(field.equals(RendicontazionePagamento.model().SINGOLO_VERSAMENTO.STATO_SINGOLO_VERSAMENTO)){
			if(appendTablePrefix){
				return this.toAliasTable(field)+".stato_singolo_versamento";
			}else{
				return "stato_singolo_versamento";
			}
		}
		if(field.equals(RendicontazionePagamento.model().SINGOLO_VERSAMENTO.IMPORTO_SINGOLO_VERSAMENTO)){
			if(appendTablePrefix){
				return this.toAliasTable(field)+".importo_singolo_versamento";
			}else{
				return "importo_singolo_versamento";
			}
		}
		if(field.equals(RendicontazionePagamento.model().SINGOLO_VERSAMENTO.TIPO_BOLLO)){
			if(appendTablePrefix){
				return this.toAliasTable(field)+".tipo_bollo";
			}else{
				return "tipo_bollo";
			}
		}
		if(field.equals(RendicontazionePagamento.model().SINGOLO_VERSAMENTO.HASH_DOCUMENTO)){
			if(appendTablePrefix){
				return this.toAliasTable(field)+".hash_documento";
			}else{
				return "hash_documento";
			}
		}
		if(field.equals(RendicontazionePagamento.model().SINGOLO_VERSAMENTO.PROVINCIA_RESIDENZA)){
			if(appendTablePrefix){
				return this.toAliasTable(field)+".provincia_residenza";
			}else{
				return "provincia_residenza";
			}
		}
		if(field.equals(RendicontazionePagamento.model().SINGOLO_VERSAMENTO.ID_IBAN_ACCREDITO.COD_IBAN)){
			if(appendTablePrefix){
				return this.toAliasTable(field)+".cod_iban";
			}else{
				return "cod_iban";
			}
		}
		if(field.equals(RendicontazionePagamento.model().SINGOLO_VERSAMENTO.ID_IBAN_ACCREDITO.ID_DOMINIO.COD_DOMINIO)){
			if(appendTablePrefix){
				return this.toAliasTable(field)+".cod_dominio";
			}else{
				return "cod_dominio";
			}
		}
		if(field.equals(RendicontazionePagamento.model().SINGOLO_VERSAMENTO.ID_IBAN_APPOGGIO.COD_IBAN)){
			if(appendTablePrefix){
				return this.toAliasTable(field)+".cod_iban";
			}else{
				return "cod_iban";
			}
		}
		if(field.equals(RendicontazionePagamento.model().SINGOLO_VERSAMENTO.ID_IBAN_APPOGGIO.ID_DOMINIO.COD_DOMINIO)){
			if(appendTablePrefix){
				return this.toAliasTable(field)+".cod_dominio";
			}else{
				return "cod_dominio";
			}
		}
		if(field.equals(RendicontazionePagamento.model().SINGOLO_VERSAMENTO.TIPO_CONTABILITA)){
			if(appendTablePrefix){
				return this.toAliasTable(field)+".tipo_contabilita";
			}else{
				return "tipo_contabilita";
			}
		}
		if(field.equals(RendicontazionePagamento.model().SINGOLO_VERSAMENTO.CODICE_CONTABILITA)){
			if(appendTablePrefix){
				return this.toAliasTable(field)+".codice_contabilita";
			}else{
				return "codice_contabilita";
			}
		}
		if(field.equals(RendicontazionePagamento.model().SINGOLO_VERSAMENTO.DESCRIZIONE)){
			if(appendTablePrefix){
				return this.toAliasTable(field)+".descrizione";
			}else{
				return "descrizione";
			}
		}
		if(field.equals(RendicontazionePagamento.model().SINGOLO_VERSAMENTO.DATI_ALLEGATI)){
			if(appendTablePrefix){
				return this.toAliasTable(field)+".dati_allegati";
			}else{
				return "dati_allegati";
			}
		}
		if(field.equals(RendicontazionePagamento.model().SINGOLO_VERSAMENTO.INDICE_DATI)){
			if(appendTablePrefix){
				return this.toAliasTable(field)+".indice_dati";
			}else{
				return "indice_dati";
			}
		}
		if(field.equals(RendicontazionePagamento.model().SINGOLO_VERSAMENTO.DESCRIZIONE_CAUSALE_RPT)){
			if(appendTablePrefix){
				return this.toAliasTable(field)+".descrizione_causale_rpt";
			}else{
				return "descrizione_causale_rpt";
			}
		}
		if(field.equals(RendicontazionePagamento.model().VERSAMENTO.COD_VERSAMENTO_ENTE)){
			if(appendTablePrefix){
				return this.toAliasTable(field)+".cod_versamento_ente";
			}else{
				return "cod_versamento_ente";
			}
		}
		if(field.equals(RendicontazionePagamento.model().VERSAMENTO.NOME)){
			if(appendTablePrefix){
				return this.toAliasTable(field)+".nome";
			}else{
				return "nome";
			}
		}
		if(field.equals(RendicontazionePagamento.model().VERSAMENTO.ID_TIPO_VERSAMENTO_DOMINIO.ID_DOMINIO.COD_DOMINIO)){
			if(appendTablePrefix){
				return this.toAliasTable(field)+".cod_dominio";
			}else{
				return "cod_dominio";
			}
		}
		if(field.equals(RendicontazionePagamento.model().VERSAMENTO.ID_TIPO_VERSAMENTO_DOMINIO.ID_TIPO_VERSAMENTO.COD_TIPO_VERSAMENTO)){
			if(appendTablePrefix){
				return this.toAliasTable(field)+".cod_tipo_versamento";
			}else{
				return "cod_tipo_versamento";
			}
		}
		if(field.equals(RendicontazionePagamento.model().VERSAMENTO.ID_TIPO_VERSAMENTO_DOMINIO.ID_TIPO_VERSAMENTO.TIPO)){
			if(appendTablePrefix){
				return this.toAliasTable(field)+".tipo";
			}else{
				return "tipo";
			}
		}
		if(field.equals(RendicontazionePagamento.model().VERSAMENTO.ID_TIPO_VERSAMENTO.COD_TIPO_VERSAMENTO)){
			if(appendTablePrefix){
				return this.toAliasTable(field)+".cod_tipo_versamento";
			}else{
				return "cod_tipo_versamento";
			}
		}
		if(field.equals(RendicontazionePagamento.model().VERSAMENTO.ID_TIPO_VERSAMENTO.TIPO)){
			if(appendTablePrefix){
				return this.toAliasTable(field)+".tipo";
			}else{
				return "tipo";
			}
		}
		if(field.equals(RendicontazionePagamento.model().VERSAMENTO.ID_DOMINIO.COD_DOMINIO)){
			if(appendTablePrefix){
				return this.toAliasTable(field)+".cod_dominio";
			}else{
				return "cod_dominio";
			}
		}
		if(field.equals(RendicontazionePagamento.model().VERSAMENTO.ID_UO.COD_UO)){
			if(appendTablePrefix){
				return this.toAliasTable(field)+".cod_uo";
			}else{
				return "cod_uo";
			}
		}
		if(field.equals(RendicontazionePagamento.model().VERSAMENTO.ID_UO.ID_DOMINIO.COD_DOMINIO)){
			if(appendTablePrefix){
				return this.toAliasTable(field)+".cod_dominio";
			}else{
				return "cod_dominio";
			}
		}
		if(field.equals(RendicontazionePagamento.model().VERSAMENTO.ID_APPLICAZIONE.COD_APPLICAZIONE)){
			if(appendTablePrefix){
				return this.toAliasTable(field)+".cod_applicazione";
			}else{
				return "cod_applicazione";
			}
		}
		if(field.equals(RendicontazionePagamento.model().VERSAMENTO.ID_PAGAMENTO_PORTALE.ID_SESSIONE)){
			if(appendTablePrefix){
				return this.toAliasTable(field)+".id_sessione";
			}else{
				return "id_sessione";
			}
		}
		if(field.equals(RendicontazionePagamento.model().VERSAMENTO.ID_PAGAMENTO_PORTALE.ID_APPLICAZIONE.COD_APPLICAZIONE)){
			if(appendTablePrefix){
				return this.toAliasTable(field)+".cod_applicazione";
			}else{
				return "cod_applicazione";
			}
		}
		if(field.equals(RendicontazionePagamento.model().VERSAMENTO.ID_PAGAMENTO_PORTALE.VERSANTE_IDENTIFICATIVO)){
			if(appendTablePrefix){
				return this.toAliasTable(field)+".versante_identificativo";
			}else{
				return "versante_identificativo";
			}
		}
		if(field.equals(RendicontazionePagamento.model().VERSAMENTO.IUV.IUV)){
			if(appendTablePrefix){
				return this.toAliasTable(field)+".iuv";
			}else{
				return "iuv";
			}
		}
		if(field.equals(RendicontazionePagamento.model().VERSAMENTO.IMPORTO_TOTALE)){
			if(appendTablePrefix){
				return this.toAliasTable(field)+".importo_totale";
			}else{
				return "importo_totale";
			}
		}
		if(field.equals(RendicontazionePagamento.model().VERSAMENTO.STATO_VERSAMENTO)){
			if(appendTablePrefix){
				return this.toAliasTable(field)+".stato_versamento";
			}else{
				return "stato_versamento";
			}
		}
		if(field.equals(RendicontazionePagamento.model().VERSAMENTO.DESCRIZIONE_STATO)){
			if(appendTablePrefix){
				return this.toAliasTable(field)+".descrizione_stato";
			}else{
				return "descrizione_stato";
			}
		}
		if(field.equals(RendicontazionePagamento.model().VERSAMENTO.AGGIORNABILE)){
			if(appendTablePrefix){
				return this.toAliasTable(field)+".aggiornabile";
			}else{
				return "aggiornabile";
			}
		}
		if(field.equals(RendicontazionePagamento.model().VERSAMENTO.DATA_CREAZIONE)){
			if(appendTablePrefix){
				return this.toAliasTable(field)+".data_creazione";
			}else{
				return "data_creazione";
			}
		}
		if(field.equals(RendicontazionePagamento.model().VERSAMENTO.DATA_VALIDITA)){
			if(appendTablePrefix){
				return this.toAliasTable(field)+".data_validita";
			}else{
				return "data_validita";
			}
		}
		if(field.equals(RendicontazionePagamento.model().VERSAMENTO.DATA_SCADENZA)){
			if(appendTablePrefix){
				return this.toAliasTable(field)+".data_scadenza";
			}else{
				return "data_scadenza";
			}
		}
		if(field.equals(RendicontazionePagamento.model().VERSAMENTO.DATA_ORA_ULTIMO_AGGIORNAMENTO)){
			if(appendTablePrefix){
				return this.toAliasTable(field)+".data_ora_ultimo_aggiornamento";
			}else{
				return "data_ora_ultimo_aggiornamento";
			}
		}
		if(field.equals(RendicontazionePagamento.model().VERSAMENTO.CAUSALE_VERSAMENTO)){
			if(appendTablePrefix){
				return this.toAliasTable(field)+".causale_versamento";
			}else{
				return "causale_versamento";
			}
		}
		if(field.equals(RendicontazionePagamento.model().VERSAMENTO.DEBITORE_TIPO)){
			if(appendTablePrefix){
				return this.toAliasTable(field)+".debitore_tipo";
			}else{
				return "debitore_tipo";
			}
		}
		if(field.equals(RendicontazionePagamento.model().VERSAMENTO.DEBITORE_IDENTIFICATIVO)){
			if(appendTablePrefix){
				return this.toAliasTable(field)+".debitore_identificativo";
			}else{
				return "debitore_identificativo";
			}
		}
		if(field.equals(RendicontazionePagamento.model().VERSAMENTO.DEBITORE_ANAGRAFICA)){
			if(appendTablePrefix){
				return this.toAliasTable(field)+".debitore_anagrafica";
			}else{
				return "debitore_anagrafica";
			}
		}
		if(field.equals(RendicontazionePagamento.model().VERSAMENTO.DEBITORE_INDIRIZZO)){
			if(appendTablePrefix){
				return this.toAliasTable(field)+".debitore_indirizzo";
			}else{
				return "debitore_indirizzo";
			}
		}
		if(field.equals(RendicontazionePagamento.model().VERSAMENTO.DEBITORE_CIVICO)){
			if(appendTablePrefix){
				return this.toAliasTable(field)+".debitore_civico";
			}else{
				return "debitore_civico";
			}
		}
		if(field.equals(RendicontazionePagamento.model().VERSAMENTO.DEBITORE_CAP)){
			if(appendTablePrefix){
				return this.toAliasTable(field)+".debitore_cap";
			}else{
				return "debitore_cap";
			}
		}
		if(field.equals(RendicontazionePagamento.model().VERSAMENTO.DEBITORE_LOCALITA)){
			if(appendTablePrefix){
				return this.toAliasTable(field)+".debitore_localita";
			}else{
				return "debitore_localita";
			}
		}
		if(field.equals(RendicontazionePagamento.model().VERSAMENTO.DEBITORE_PROVINCIA)){
			if(appendTablePrefix){
				return this.toAliasTable(field)+".debitore_provincia";
			}else{
				return "debitore_provincia";
			}
		}
		if(field.equals(RendicontazionePagamento.model().VERSAMENTO.DEBITORE_NAZIONE)){
			if(appendTablePrefix){
				return this.toAliasTable(field)+".debitore_nazione";
			}else{
				return "debitore_nazione";
			}
		}
		if(field.equals(RendicontazionePagamento.model().VERSAMENTO.DEBITORE_EMAIL)){
			if(appendTablePrefix){
				return this.toAliasTable(field)+".debitore_email";
			}else{
				return "debitore_email";
			}
		}
		if(field.equals(RendicontazionePagamento.model().VERSAMENTO.DEBITORE_TELEFONO)){
			if(appendTablePrefix){
				return this.toAliasTable(field)+".debitore_telefono";
			}else{
				return "debitore_telefono";
			}
		}
		if(field.equals(RendicontazionePagamento.model().VERSAMENTO.DEBITORE_CELLULARE)){
			if(appendTablePrefix){
				return this.toAliasTable(field)+".debitore_cellulare";
			}else{
				return "debitore_cellulare";
			}
		}
		if(field.equals(RendicontazionePagamento.model().VERSAMENTO.DEBITORE_FAX)){
			if(appendTablePrefix){
				return this.toAliasTable(field)+".debitore_fax";
			}else{
				return "debitore_fax";
			}
		}
		if(field.equals(RendicontazionePagamento.model().VERSAMENTO.TASSONOMIA_AVVISO)){
			if(appendTablePrefix){
				return this.toAliasTable(field)+".tassonomia_avviso";
			}else{
				return "tassonomia_avviso";
			}
		}
		if(field.equals(RendicontazionePagamento.model().VERSAMENTO.TASSONOMIA)){
			if(appendTablePrefix){
				return this.toAliasTable(field)+".tassonomia";
			}else{
				return "tassonomia";
			}
		}
		if(field.equals(RendicontazionePagamento.model().VERSAMENTO.COD_LOTTO)){
			if(appendTablePrefix){
				return this.toAliasTable(field)+".cod_lotto";
			}else{
				return "cod_lotto";
			}
		}
		if(field.equals(RendicontazionePagamento.model().VERSAMENTO.COD_VERSAMENTO_LOTTO)){
			if(appendTablePrefix){
				return this.toAliasTable(field)+".cod_versamento_lotto";
			}else{
				return "cod_versamento_lotto";
			}
		}
		if(field.equals(RendicontazionePagamento.model().VERSAMENTO.COD_ANNO_TRIBUTARIO)){
			if(appendTablePrefix){
				return this.toAliasTable(field)+".cod_anno_tributario";
			}else{
				return "cod_anno_tributario";
			}
		}
		if(field.equals(RendicontazionePagamento.model().VERSAMENTO.COD_BUNDLEKEY)){
			if(appendTablePrefix){
				return this.toAliasTable(field)+".cod_bundlekey";
			}else{
				return "cod_bundlekey";
			}
		}
		if(field.equals(RendicontazionePagamento.model().VERSAMENTO.DATI_ALLEGATI)){
			if(appendTablePrefix){
				return this.toAliasTable(field)+".dati_allegati";
			}else{
				return "dati_allegati";
			}
		}
		if(field.equals(RendicontazionePagamento.model().VERSAMENTO.INCASSO)){
			if(appendTablePrefix){
				return this.toAliasTable(field)+".incasso";
			}else{
				return "incasso";
			}
		}
		if(field.equals(RendicontazionePagamento.model().VERSAMENTO.ANOMALIE)){
			if(appendTablePrefix){
				return this.toAliasTable(field)+".anomalie";
			}else{
				return "anomalie";
			}
		}
		if(field.equals(RendicontazionePagamento.model().VERSAMENTO.IUV_VERSAMENTO)){
			if(appendTablePrefix){
				return this.toAliasTable(field)+".iuv_versamento";
			}else{
				return "iuv_versamento";
			}
		}
		if(field.equals(RendicontazionePagamento.model().VERSAMENTO.NUMERO_AVVISO)){
			if(appendTablePrefix){
				return this.toAliasTable(field)+".numero_avviso";
			}else{
				return "numero_avviso";
			}
		}
		if(field.equals(RendicontazionePagamento.model().VERSAMENTO.AVVISATURA_ABILITATA)){
			if(appendTablePrefix){
				return this.toAliasTable(field)+".avvisatura_abilitata";
			}else{
				return "avvisatura_abilitata";
			}
		}
		if(field.equals(RendicontazionePagamento.model().VERSAMENTO.AVVISATURA_DA_INVIARE)){
			if(appendTablePrefix){
				return this.toAliasTable(field)+".avvisatura_da_inviare";
			}else{
				return "avvisatura_da_inviare";
			}
		}
		if(field.equals(RendicontazionePagamento.model().VERSAMENTO.AVVISATURA_OPERAZIONE)){
			if(appendTablePrefix){
				return this.toAliasTable(field)+".avvisatura_operazione";
			}else{
				return "avvisatura_operazione";
			}
		}
		if(field.equals(RendicontazionePagamento.model().VERSAMENTO.AVVISATURA_MODALITA)){
			if(appendTablePrefix){
				return this.toAliasTable(field)+".avvisatura_modalita";
			}else{
				return "avvisatura_modalita";
			}
		}
		if(field.equals(RendicontazionePagamento.model().VERSAMENTO.AVVISATURA_TIPO_PAGAMENTO)){
			if(appendTablePrefix){
				return this.toAliasTable(field)+".avvisatura_tipo_pagamento";
			}else{
				return "avvisatura_tipo_pagamento";
			}
		}
		if(field.equals(RendicontazionePagamento.model().VERSAMENTO.AVVISATURA_COD_AVVISATURA)){
			if(appendTablePrefix){
				return this.toAliasTable(field)+".avvisatura_cod_avvisatura";
			}else{
				return "avvisatura_cod_avvisatura";
			}
		}
		if(field.equals(RendicontazionePagamento.model().VERSAMENTO.ID_TRACCIATO_AVVISATURA.ID_TRACCIATO)){
			if(appendTablePrefix){
				return this.toAliasTable(field)+".id_tracciato";
			}else{
				return "id_tracciato";
			}
		}
		if(field.equals(RendicontazionePagamento.model().VERSAMENTO.ACK)){
			if(appendTablePrefix){
				return this.toAliasTable(field)+".ack";
			}else{
				return "ack";
			}
		}
		if(field.equals(RendicontazionePagamento.model().VERSAMENTO.ANOMALO)){
			if(appendTablePrefix){
				return this.toAliasTable(field)+".anomalo";
			}else{
				return "anomalo";
			}
		}
		if(field.equals(RendicontazionePagamento.model().VERSAMENTO.DIVISIONE)){
			if(appendTablePrefix){
				return this.toAliasTable(field)+".divisione";
			}else{
				return "divisione";
			}
		}
		if(field.equals(RendicontazionePagamento.model().VERSAMENTO.DIREZIONE)){
			if(appendTablePrefix){
				return this.toAliasTable(field)+".direzione";
			}else{
				return "direzione";
			}
		}
		if(field.equals(RendicontazionePagamento.model().VERSAMENTO.ID_SESSIONE)){
			if(appendTablePrefix){
				return this.toAliasTable(field)+".id_sessione";
			}else{
				return "id_sessione";
			}
		}
		if(field.equals(RendicontazionePagamento.model().VERSAMENTO.DATA_PAGAMENTO)){
			if(appendTablePrefix){
				return this.toAliasTable(field)+".data_pagamento";
			}else{
				return "data_pagamento";
			}
		}
		if(field.equals(RendicontazionePagamento.model().VERSAMENTO.IMPORTO_PAGATO)){
			if(appendTablePrefix){
				return this.toAliasTable(field)+".importo_pagato";
			}else{
				return "importo_pagato";
			}
		}
		if(field.equals(RendicontazionePagamento.model().VERSAMENTO.IMPORTO_INCASSATO)){
			if(appendTablePrefix){
				return this.toAliasTable(field)+".importo_incassato";
			}else{
				return "importo_incassato";
			}
		}
		if(field.equals(RendicontazionePagamento.model().VERSAMENTO.STATO_PAGAMENTO)){
			if(appendTablePrefix){
				return this.toAliasTable(field)+".stato_pagamento";
			}else{
				return "stato_pagamento";
			}
		}
		if(field.equals(RendicontazionePagamento.model().VERSAMENTO.IUV_PAGAMENTO)){
			if(appendTablePrefix){
				return this.toAliasTable(field)+".iuv_pagamento";
			}else{
				return "iuv_pagamento";
			}
		}


		return super.toColumn(field,returnAlias,appendTablePrefix);
		
	}
	
	@Override
	public String toTable(IField field,boolean returnAlias) throws ExpressionException {
		
		// In the case of table with alias, using parameter returnAlias​​, 
		// it is possible to drive the choice whether to return only the alias or 
		// the full definition of the table containing the alias
		
		if(field.equals(RendicontazionePagamento.model().FR.COD_PSP)){
			return this.toTable(RendicontazionePagamento.model().FR, returnAlias);
		}
		if(field.equals(RendicontazionePagamento.model().FR.COD_DOMINIO)){
			return this.toTable(RendicontazionePagamento.model().FR, returnAlias);
		}
		if(field.equals(RendicontazionePagamento.model().FR.COD_FLUSSO)){
			return this.toTable(RendicontazionePagamento.model().FR, returnAlias);
		}
		if(field.equals(RendicontazionePagamento.model().FR.STATO)){
			return this.toTable(RendicontazionePagamento.model().FR, returnAlias);
		}
		if(field.equals(RendicontazionePagamento.model().FR.DESCRIZIONE_STATO)){
			return this.toTable(RendicontazionePagamento.model().FR, returnAlias);
		}
		if(field.equals(RendicontazionePagamento.model().FR.IUR)){
			return this.toTable(RendicontazionePagamento.model().FR, returnAlias);
		}
		if(field.equals(RendicontazionePagamento.model().FR.DATA_ORA_FLUSSO)){
			return this.toTable(RendicontazionePagamento.model().FR, returnAlias);
		}
		if(field.equals(RendicontazionePagamento.model().FR.DATA_REGOLAMENTO)){
			return this.toTable(RendicontazionePagamento.model().FR, returnAlias);
		}
		if(field.equals(RendicontazionePagamento.model().FR.DATA_ACQUISIZIONE)){
			return this.toTable(RendicontazionePagamento.model().FR, returnAlias);
		}
		if(field.equals(RendicontazionePagamento.model().FR.NUMERO_PAGAMENTI)){
			return this.toTable(RendicontazionePagamento.model().FR, returnAlias);
		}
		if(field.equals(RendicontazionePagamento.model().FR.IMPORTO_TOTALE_PAGAMENTI)){
			return this.toTable(RendicontazionePagamento.model().FR, returnAlias);
		}
		if(field.equals(RendicontazionePagamento.model().FR.COD_BIC_RIVERSAMENTO)){
			return this.toTable(RendicontazionePagamento.model().FR, returnAlias);
		}
		if(field.equals(RendicontazionePagamento.model().FR.XML)){
			return this.toTable(RendicontazionePagamento.model().FR, returnAlias);
		}
		if(field.equals(RendicontazionePagamento.model().FR.ID_SINGOLO_VERSAMENTO.ID_VERSAMENTO.COD_VERSAMENTO_ENTE)){
			return this.toTable(RendicontazionePagamento.model().FR.ID_SINGOLO_VERSAMENTO.ID_VERSAMENTO, returnAlias);
		}
		if(field.equals(RendicontazionePagamento.model().FR.ID_SINGOLO_VERSAMENTO.ID_VERSAMENTO.ID_APPLICAZIONE.COD_APPLICAZIONE)){
			return this.toTable(RendicontazionePagamento.model().FR.ID_SINGOLO_VERSAMENTO.ID_VERSAMENTO.ID_APPLICAZIONE, returnAlias);
		}
		if(field.equals(RendicontazionePagamento.model().FR.ID_SINGOLO_VERSAMENTO.ID_VERSAMENTO.DEBITORE_IDENTIFICATIVO)){
			return this.toTable(RendicontazionePagamento.model().FR.ID_SINGOLO_VERSAMENTO.ID_VERSAMENTO, returnAlias);
		}
		if(field.equals(RendicontazionePagamento.model().FR.ID_SINGOLO_VERSAMENTO.ID_VERSAMENTO.DEBITORE_ANAGRAFICA)){
			return this.toTable(RendicontazionePagamento.model().FR.ID_SINGOLO_VERSAMENTO.ID_VERSAMENTO, returnAlias);
		}
		if(field.equals(RendicontazionePagamento.model().FR.ID_SINGOLO_VERSAMENTO.ID_VERSAMENTO.COD_VERSAMENTO_LOTTO)){
			return this.toTable(RendicontazionePagamento.model().FR.ID_SINGOLO_VERSAMENTO.ID_VERSAMENTO, returnAlias);
		}
		if(field.equals(RendicontazionePagamento.model().FR.ID_SINGOLO_VERSAMENTO.ID_VERSAMENTO.COD_ANNO_TRIBUTARIO)){
			return this.toTable(RendicontazionePagamento.model().FR.ID_SINGOLO_VERSAMENTO.ID_VERSAMENTO, returnAlias);
		}
		if(field.equals(RendicontazionePagamento.model().FR.ID_SINGOLO_VERSAMENTO.ID_VERSAMENTO.IMPORTO_TOTALE)){
			return this.toTable(RendicontazionePagamento.model().FR.ID_SINGOLO_VERSAMENTO.ID_VERSAMENTO, returnAlias);
		}
		if(field.equals(RendicontazionePagamento.model().FR.ID_SINGOLO_VERSAMENTO.ID_VERSAMENTO.CAUSALE_VERSAMENTO)){
			return this.toTable(RendicontazionePagamento.model().FR.ID_SINGOLO_VERSAMENTO.ID_VERSAMENTO, returnAlias);
		}
		if(field.equals(RendicontazionePagamento.model().FR.ID_SINGOLO_VERSAMENTO.ID_VERSAMENTO.STATO_VERSAMENTO)){
			return this.toTable(RendicontazionePagamento.model().FR.ID_SINGOLO_VERSAMENTO.ID_VERSAMENTO, returnAlias);
		}
		if(field.equals(RendicontazionePagamento.model().FR.ID_SINGOLO_VERSAMENTO.ID_VERSAMENTO.ID_UO.COD_UO)){
			return this.toTable(RendicontazionePagamento.model().FR.ID_SINGOLO_VERSAMENTO.ID_VERSAMENTO.ID_UO, returnAlias);
		}
		if(field.equals(RendicontazionePagamento.model().FR.ID_SINGOLO_VERSAMENTO.ID_VERSAMENTO.ID_UO.ID_DOMINIO.COD_DOMINIO)){
			return this.toTable(RendicontazionePagamento.model().FR.ID_SINGOLO_VERSAMENTO.ID_VERSAMENTO.ID_UO.ID_DOMINIO, returnAlias);
		}
		if(field.equals(RendicontazionePagamento.model().FR.ID_SINGOLO_VERSAMENTO.ID_VERSAMENTO.ID_TIPO_VERSAMENTO.COD_TIPO_VERSAMENTO)){
			return this.toTable(RendicontazionePagamento.model().FR.ID_SINGOLO_VERSAMENTO.ID_VERSAMENTO.ID_TIPO_VERSAMENTO, returnAlias);
		}
		if(field.equals(RendicontazionePagamento.model().FR.ID_SINGOLO_VERSAMENTO.ID_VERSAMENTO.ID_TIPO_VERSAMENTO.TIPO)){
			return this.toTable(RendicontazionePagamento.model().FR.ID_SINGOLO_VERSAMENTO.ID_VERSAMENTO.ID_TIPO_VERSAMENTO, returnAlias);
		}
		if(field.equals(RendicontazionePagamento.model().FR.ID_SINGOLO_VERSAMENTO.ID_VERSAMENTO.DIVISIONE)){
			return this.toTable(RendicontazionePagamento.model().FR.ID_SINGOLO_VERSAMENTO.ID_VERSAMENTO, returnAlias);
		}
		if(field.equals(RendicontazionePagamento.model().FR.ID_SINGOLO_VERSAMENTO.ID_VERSAMENTO.DIREZIONE)){
			return this.toTable(RendicontazionePagamento.model().FR.ID_SINGOLO_VERSAMENTO.ID_VERSAMENTO, returnAlias);
		}
		if(field.equals(RendicontazionePagamento.model().FR.ID_SINGOLO_VERSAMENTO.ID_VERSAMENTO.TASSONOMIA)){
			return this.toTable(RendicontazionePagamento.model().FR.ID_SINGOLO_VERSAMENTO.ID_VERSAMENTO, returnAlias);
		}
		if(field.equals(RendicontazionePagamento.model().FR.ID_SINGOLO_VERSAMENTO.COD_SINGOLO_VERSAMENTO_ENTE)){
			return this.toTable(RendicontazionePagamento.model().FR.ID_SINGOLO_VERSAMENTO, returnAlias);
		}
		if(field.equals(RendicontazionePagamento.model().FR.ID_SINGOLO_VERSAMENTO.INDICE_DATI)){
			return this.toTable(RendicontazionePagamento.model().FR.ID_SINGOLO_VERSAMENTO, returnAlias);
		}
		if(field.equals(RendicontazionePagamento.model().FR.ID_SINGOLO_VERSAMENTO.ID_TRIBUTO.ID_DOMINIO.COD_DOMINIO)){
			return this.toTable(RendicontazionePagamento.model().FR.ID_SINGOLO_VERSAMENTO.ID_TRIBUTO.ID_DOMINIO, returnAlias);
		}
		if(field.equals(RendicontazionePagamento.model().FR.ID_SINGOLO_VERSAMENTO.ID_TRIBUTO.ID_TIPO_TRIBUTO.COD_TRIBUTO)){
			return this.toTable(RendicontazionePagamento.model().FR.ID_SINGOLO_VERSAMENTO.ID_TRIBUTO.ID_TIPO_TRIBUTO, returnAlias);
		}
		if(field.equals(RendicontazionePagamento.model().FR.ID_SINGOLO_VERSAMENTO.NOTE)){
			return this.toTable(RendicontazionePagamento.model().FR.ID_SINGOLO_VERSAMENTO, returnAlias);
		}
		if(field.equals(RendicontazionePagamento.model().FR.ID_SINGOLO_VERSAMENTO.STATO_SINGOLO_VERSAMENTO)){
			return this.toTable(RendicontazionePagamento.model().FR.ID_SINGOLO_VERSAMENTO, returnAlias);
		}
		if(field.equals(RendicontazionePagamento.model().FR.ID_SINGOLO_VERSAMENTO.IMPORTO_SINGOLO_VERSAMENTO)){
			return this.toTable(RendicontazionePagamento.model().FR.ID_SINGOLO_VERSAMENTO, returnAlias);
		}
		if(field.equals(RendicontazionePagamento.model().FR.ID_INCASSO.TRN)){
			return this.toTable(RendicontazionePagamento.model().FR.ID_INCASSO, returnAlias);
		}
		if(field.equals(RendicontazionePagamento.model().FR.ID_INCASSO.COD_DOMINIO)){
			return this.toTable(RendicontazionePagamento.model().FR.ID_INCASSO, returnAlias);
		}
		if(field.equals(RendicontazionePagamento.model().RENDICONTAZIONE.IUV)){
			return this.toTable(RendicontazionePagamento.model().RENDICONTAZIONE, returnAlias);
		}
		if(field.equals(RendicontazionePagamento.model().RENDICONTAZIONE.IUR)){
			return this.toTable(RendicontazionePagamento.model().RENDICONTAZIONE, returnAlias);
		}
		if(field.equals(RendicontazionePagamento.model().RENDICONTAZIONE.INDICE_DATI)){
			return this.toTable(RendicontazionePagamento.model().RENDICONTAZIONE, returnAlias);
		}
		if(field.equals(RendicontazionePagamento.model().RENDICONTAZIONE.IMPORTO_PAGATO)){
			return this.toTable(RendicontazionePagamento.model().RENDICONTAZIONE, returnAlias);
		}
		if(field.equals(RendicontazionePagamento.model().RENDICONTAZIONE.ESITO)){
			return this.toTable(RendicontazionePagamento.model().RENDICONTAZIONE, returnAlias);
		}
		if(field.equals(RendicontazionePagamento.model().RENDICONTAZIONE.DATA)){
			return this.toTable(RendicontazionePagamento.model().RENDICONTAZIONE, returnAlias);
		}
		if(field.equals(RendicontazionePagamento.model().RENDICONTAZIONE.STATO)){
			return this.toTable(RendicontazionePagamento.model().RENDICONTAZIONE, returnAlias);
		}
		if(field.equals(RendicontazionePagamento.model().RENDICONTAZIONE.ANOMALIE)){
			return this.toTable(RendicontazionePagamento.model().RENDICONTAZIONE, returnAlias);
		}
		if(field.equals(RendicontazionePagamento.model().RENDICONTAZIONE.ID_FR.COD_FLUSSO)){
			return this.toTable(RendicontazionePagamento.model().RENDICONTAZIONE.ID_FR, returnAlias);
		}
		if(field.equals(RendicontazionePagamento.model().RENDICONTAZIONE.ID_FR.COD_DOMINIO)){
			return this.toTable(RendicontazionePagamento.model().RENDICONTAZIONE.ID_FR, returnAlias);
		}
		if(field.equals(RendicontazionePagamento.model().RENDICONTAZIONE.ID_FR.DATA_ORA_FLUSSO)){
			return this.toTable(RendicontazionePagamento.model().RENDICONTAZIONE.ID_FR, returnAlias);
		}
		if(field.equals(RendicontazionePagamento.model().RENDICONTAZIONE.ID_PAGAMENTO.ID_PAGAMENTO)){
			return this.toTable(RendicontazionePagamento.model().RENDICONTAZIONE.ID_PAGAMENTO, returnAlias);
		}
		if(field.equals(RendicontazionePagamento.model().RENDICONTAZIONE.ID_PAGAMENTO.INDICE_DATI)){
			return this.toTable(RendicontazionePagamento.model().RENDICONTAZIONE.ID_PAGAMENTO, returnAlias);
		}
		if(field.equals(RendicontazionePagamento.model().RENDICONTAZIONE.ID_PAGAMENTO.IUV)){
			return this.toTable(RendicontazionePagamento.model().RENDICONTAZIONE.ID_PAGAMENTO, returnAlias);
		}
		if(field.equals(RendicontazionePagamento.model().RENDICONTAZIONE.ID_SINGOLO_VERSAMENTO.ID_VERSAMENTO.COD_VERSAMENTO_ENTE)){
			return this.toTable(RendicontazionePagamento.model().RENDICONTAZIONE.ID_SINGOLO_VERSAMENTO.ID_VERSAMENTO, returnAlias);
		}
		if(field.equals(RendicontazionePagamento.model().RENDICONTAZIONE.ID_SINGOLO_VERSAMENTO.ID_VERSAMENTO.ID_APPLICAZIONE.COD_APPLICAZIONE)){
			return this.toTable(RendicontazionePagamento.model().RENDICONTAZIONE.ID_SINGOLO_VERSAMENTO.ID_VERSAMENTO.ID_APPLICAZIONE, returnAlias);
		}
		if(field.equals(RendicontazionePagamento.model().RENDICONTAZIONE.ID_SINGOLO_VERSAMENTO.ID_VERSAMENTO.DEBITORE_IDENTIFICATIVO)){
			return this.toTable(RendicontazionePagamento.model().RENDICONTAZIONE.ID_SINGOLO_VERSAMENTO.ID_VERSAMENTO, returnAlias);
		}
		if(field.equals(RendicontazionePagamento.model().RENDICONTAZIONE.ID_SINGOLO_VERSAMENTO.ID_VERSAMENTO.DEBITORE_ANAGRAFICA)){
			return this.toTable(RendicontazionePagamento.model().RENDICONTAZIONE.ID_SINGOLO_VERSAMENTO.ID_VERSAMENTO, returnAlias);
		}
		if(field.equals(RendicontazionePagamento.model().RENDICONTAZIONE.ID_SINGOLO_VERSAMENTO.ID_VERSAMENTO.COD_VERSAMENTO_LOTTO)){
			return this.toTable(RendicontazionePagamento.model().RENDICONTAZIONE.ID_SINGOLO_VERSAMENTO.ID_VERSAMENTO, returnAlias);
		}
		if(field.equals(RendicontazionePagamento.model().RENDICONTAZIONE.ID_SINGOLO_VERSAMENTO.ID_VERSAMENTO.COD_ANNO_TRIBUTARIO)){
			return this.toTable(RendicontazionePagamento.model().RENDICONTAZIONE.ID_SINGOLO_VERSAMENTO.ID_VERSAMENTO, returnAlias);
		}
		if(field.equals(RendicontazionePagamento.model().RENDICONTAZIONE.ID_SINGOLO_VERSAMENTO.ID_VERSAMENTO.IMPORTO_TOTALE)){
			return this.toTable(RendicontazionePagamento.model().RENDICONTAZIONE.ID_SINGOLO_VERSAMENTO.ID_VERSAMENTO, returnAlias);
		}
		if(field.equals(RendicontazionePagamento.model().RENDICONTAZIONE.ID_SINGOLO_VERSAMENTO.ID_VERSAMENTO.CAUSALE_VERSAMENTO)){
			return this.toTable(RendicontazionePagamento.model().RENDICONTAZIONE.ID_SINGOLO_VERSAMENTO.ID_VERSAMENTO, returnAlias);
		}
		if(field.equals(RendicontazionePagamento.model().RENDICONTAZIONE.ID_SINGOLO_VERSAMENTO.ID_VERSAMENTO.STATO_VERSAMENTO)){
			return this.toTable(RendicontazionePagamento.model().RENDICONTAZIONE.ID_SINGOLO_VERSAMENTO.ID_VERSAMENTO, returnAlias);
		}
		if(field.equals(RendicontazionePagamento.model().RENDICONTAZIONE.ID_SINGOLO_VERSAMENTO.ID_VERSAMENTO.ID_UO.COD_UO)){
			return this.toTable(RendicontazionePagamento.model().RENDICONTAZIONE.ID_SINGOLO_VERSAMENTO.ID_VERSAMENTO.ID_UO, returnAlias);
		}
		if(field.equals(RendicontazionePagamento.model().RENDICONTAZIONE.ID_SINGOLO_VERSAMENTO.ID_VERSAMENTO.ID_UO.ID_DOMINIO.COD_DOMINIO)){
			return this.toTable(RendicontazionePagamento.model().RENDICONTAZIONE.ID_SINGOLO_VERSAMENTO.ID_VERSAMENTO.ID_UO.ID_DOMINIO, returnAlias);
		}
		if(field.equals(RendicontazionePagamento.model().RENDICONTAZIONE.ID_SINGOLO_VERSAMENTO.ID_VERSAMENTO.ID_TIPO_VERSAMENTO.COD_TIPO_VERSAMENTO)){
			return this.toTable(RendicontazionePagamento.model().RENDICONTAZIONE.ID_SINGOLO_VERSAMENTO.ID_VERSAMENTO.ID_TIPO_VERSAMENTO, returnAlias);
		}
		if(field.equals(RendicontazionePagamento.model().RENDICONTAZIONE.ID_SINGOLO_VERSAMENTO.ID_VERSAMENTO.ID_TIPO_VERSAMENTO.TIPO)){
			return this.toTable(RendicontazionePagamento.model().RENDICONTAZIONE.ID_SINGOLO_VERSAMENTO.ID_VERSAMENTO.ID_TIPO_VERSAMENTO, returnAlias);
		}
		if(field.equals(RendicontazionePagamento.model().RENDICONTAZIONE.ID_SINGOLO_VERSAMENTO.ID_VERSAMENTO.DIVISIONE)){
			return this.toTable(RendicontazionePagamento.model().RENDICONTAZIONE.ID_SINGOLO_VERSAMENTO.ID_VERSAMENTO, returnAlias);
		}
		if(field.equals(RendicontazionePagamento.model().RENDICONTAZIONE.ID_SINGOLO_VERSAMENTO.ID_VERSAMENTO.DIREZIONE)){
			return this.toTable(RendicontazionePagamento.model().RENDICONTAZIONE.ID_SINGOLO_VERSAMENTO.ID_VERSAMENTO, returnAlias);
		}
		if(field.equals(RendicontazionePagamento.model().RENDICONTAZIONE.ID_SINGOLO_VERSAMENTO.ID_VERSAMENTO.TASSONOMIA)){
			return this.toTable(RendicontazionePagamento.model().RENDICONTAZIONE.ID_SINGOLO_VERSAMENTO.ID_VERSAMENTO, returnAlias);
		}
		if(field.equals(RendicontazionePagamento.model().RENDICONTAZIONE.ID_SINGOLO_VERSAMENTO.COD_SINGOLO_VERSAMENTO_ENTE)){
			return this.toTable(RendicontazionePagamento.model().RENDICONTAZIONE.ID_SINGOLO_VERSAMENTO, returnAlias);
		}
		if(field.equals(RendicontazionePagamento.model().RENDICONTAZIONE.ID_SINGOLO_VERSAMENTO.INDICE_DATI)){
			return this.toTable(RendicontazionePagamento.model().RENDICONTAZIONE.ID_SINGOLO_VERSAMENTO, returnAlias);
		}
		if(field.equals(RendicontazionePagamento.model().RENDICONTAZIONE.ID_SINGOLO_VERSAMENTO.ID_TRIBUTO.ID_DOMINIO.COD_DOMINIO)){
			return this.toTable(RendicontazionePagamento.model().RENDICONTAZIONE.ID_SINGOLO_VERSAMENTO.ID_TRIBUTO.ID_DOMINIO, returnAlias);
		}
		if(field.equals(RendicontazionePagamento.model().RENDICONTAZIONE.ID_SINGOLO_VERSAMENTO.ID_TRIBUTO.ID_TIPO_TRIBUTO.COD_TRIBUTO)){
			return this.toTable(RendicontazionePagamento.model().RENDICONTAZIONE.ID_SINGOLO_VERSAMENTO.ID_TRIBUTO.ID_TIPO_TRIBUTO, returnAlias);
		}
		if(field.equals(RendicontazionePagamento.model().RENDICONTAZIONE.ID_SINGOLO_VERSAMENTO.NOTE)){
			return this.toTable(RendicontazionePagamento.model().RENDICONTAZIONE.ID_SINGOLO_VERSAMENTO, returnAlias);
		}
		if(field.equals(RendicontazionePagamento.model().RENDICONTAZIONE.ID_SINGOLO_VERSAMENTO.STATO_SINGOLO_VERSAMENTO)){
			return this.toTable(RendicontazionePagamento.model().RENDICONTAZIONE.ID_SINGOLO_VERSAMENTO, returnAlias);
		}
		if(field.equals(RendicontazionePagamento.model().RENDICONTAZIONE.ID_SINGOLO_VERSAMENTO.IMPORTO_SINGOLO_VERSAMENTO)){
			return this.toTable(RendicontazionePagamento.model().RENDICONTAZIONE.ID_SINGOLO_VERSAMENTO, returnAlias);
		}
		if(field.equals(RendicontazionePagamento.model().PAGAMENTO.ID_RPT.COD_MSG_RICHIESTA)){
			return this.toTable(RendicontazionePagamento.model().PAGAMENTO.ID_RPT, returnAlias);
		}
		if(field.equals(RendicontazionePagamento.model().PAGAMENTO.ID_RPT.COD_DOMINIO)){
			return this.toTable(RendicontazionePagamento.model().PAGAMENTO.ID_RPT, returnAlias);
		}
		if(field.equals(RendicontazionePagamento.model().PAGAMENTO.ID_RPT.IUV)){
			return this.toTable(RendicontazionePagamento.model().PAGAMENTO.ID_RPT, returnAlias);
		}
		if(field.equals(RendicontazionePagamento.model().PAGAMENTO.ID_SINGOLO_VERSAMENTO.ID_VERSAMENTO.COD_VERSAMENTO_ENTE)){
			return this.toTable(RendicontazionePagamento.model().PAGAMENTO.ID_SINGOLO_VERSAMENTO.ID_VERSAMENTO, returnAlias);
		}
		if(field.equals(RendicontazionePagamento.model().PAGAMENTO.ID_SINGOLO_VERSAMENTO.ID_VERSAMENTO.ID_APPLICAZIONE.COD_APPLICAZIONE)){
			return this.toTable(RendicontazionePagamento.model().PAGAMENTO.ID_SINGOLO_VERSAMENTO.ID_VERSAMENTO.ID_APPLICAZIONE, returnAlias);
		}
		if(field.equals(RendicontazionePagamento.model().PAGAMENTO.ID_SINGOLO_VERSAMENTO.ID_VERSAMENTO.DEBITORE_IDENTIFICATIVO)){
			return this.toTable(RendicontazionePagamento.model().PAGAMENTO.ID_SINGOLO_VERSAMENTO.ID_VERSAMENTO, returnAlias);
		}
		if(field.equals(RendicontazionePagamento.model().PAGAMENTO.ID_SINGOLO_VERSAMENTO.ID_VERSAMENTO.DEBITORE_ANAGRAFICA)){
			return this.toTable(RendicontazionePagamento.model().PAGAMENTO.ID_SINGOLO_VERSAMENTO.ID_VERSAMENTO, returnAlias);
		}
		if(field.equals(RendicontazionePagamento.model().PAGAMENTO.ID_SINGOLO_VERSAMENTO.ID_VERSAMENTO.COD_VERSAMENTO_LOTTO)){
			return this.toTable(RendicontazionePagamento.model().PAGAMENTO.ID_SINGOLO_VERSAMENTO.ID_VERSAMENTO, returnAlias);
		}
		if(field.equals(RendicontazionePagamento.model().PAGAMENTO.ID_SINGOLO_VERSAMENTO.ID_VERSAMENTO.COD_ANNO_TRIBUTARIO)){
			return this.toTable(RendicontazionePagamento.model().PAGAMENTO.ID_SINGOLO_VERSAMENTO.ID_VERSAMENTO, returnAlias);
		}
		if(field.equals(RendicontazionePagamento.model().PAGAMENTO.ID_SINGOLO_VERSAMENTO.ID_VERSAMENTO.IMPORTO_TOTALE)){
			return this.toTable(RendicontazionePagamento.model().PAGAMENTO.ID_SINGOLO_VERSAMENTO.ID_VERSAMENTO, returnAlias);
		}
		if(field.equals(RendicontazionePagamento.model().PAGAMENTO.ID_SINGOLO_VERSAMENTO.ID_VERSAMENTO.CAUSALE_VERSAMENTO)){
			return this.toTable(RendicontazionePagamento.model().PAGAMENTO.ID_SINGOLO_VERSAMENTO.ID_VERSAMENTO, returnAlias);
		}
		if(field.equals(RendicontazionePagamento.model().PAGAMENTO.ID_SINGOLO_VERSAMENTO.ID_VERSAMENTO.STATO_VERSAMENTO)){
			return this.toTable(RendicontazionePagamento.model().PAGAMENTO.ID_SINGOLO_VERSAMENTO.ID_VERSAMENTO, returnAlias);
		}
		if(field.equals(RendicontazionePagamento.model().PAGAMENTO.ID_SINGOLO_VERSAMENTO.ID_VERSAMENTO.ID_UO.COD_UO)){
			return this.toTable(RendicontazionePagamento.model().PAGAMENTO.ID_SINGOLO_VERSAMENTO.ID_VERSAMENTO.ID_UO, returnAlias);
		}
		if(field.equals(RendicontazionePagamento.model().PAGAMENTO.ID_SINGOLO_VERSAMENTO.ID_VERSAMENTO.ID_UO.ID_DOMINIO.COD_DOMINIO)){
			return this.toTable(RendicontazionePagamento.model().PAGAMENTO.ID_SINGOLO_VERSAMENTO.ID_VERSAMENTO.ID_UO.ID_DOMINIO, returnAlias);
		}
		if(field.equals(RendicontazionePagamento.model().PAGAMENTO.ID_SINGOLO_VERSAMENTO.ID_VERSAMENTO.ID_TIPO_VERSAMENTO.COD_TIPO_VERSAMENTO)){
			return this.toTable(RendicontazionePagamento.model().PAGAMENTO.ID_SINGOLO_VERSAMENTO.ID_VERSAMENTO.ID_TIPO_VERSAMENTO, returnAlias);
		}
		if(field.equals(RendicontazionePagamento.model().PAGAMENTO.ID_SINGOLO_VERSAMENTO.ID_VERSAMENTO.ID_TIPO_VERSAMENTO.TIPO)){
			return this.toTable(RendicontazionePagamento.model().PAGAMENTO.ID_SINGOLO_VERSAMENTO.ID_VERSAMENTO.ID_TIPO_VERSAMENTO, returnAlias);
		}
		if(field.equals(RendicontazionePagamento.model().PAGAMENTO.ID_SINGOLO_VERSAMENTO.ID_VERSAMENTO.DIVISIONE)){
			return this.toTable(RendicontazionePagamento.model().PAGAMENTO.ID_SINGOLO_VERSAMENTO.ID_VERSAMENTO, returnAlias);
		}
		if(field.equals(RendicontazionePagamento.model().PAGAMENTO.ID_SINGOLO_VERSAMENTO.ID_VERSAMENTO.DIREZIONE)){
			return this.toTable(RendicontazionePagamento.model().PAGAMENTO.ID_SINGOLO_VERSAMENTO.ID_VERSAMENTO, returnAlias);
		}
		if(field.equals(RendicontazionePagamento.model().PAGAMENTO.ID_SINGOLO_VERSAMENTO.ID_VERSAMENTO.TASSONOMIA)){
			return this.toTable(RendicontazionePagamento.model().PAGAMENTO.ID_SINGOLO_VERSAMENTO.ID_VERSAMENTO, returnAlias);
		}
		if(field.equals(RendicontazionePagamento.model().PAGAMENTO.ID_SINGOLO_VERSAMENTO.COD_SINGOLO_VERSAMENTO_ENTE)){
			return this.toTable(RendicontazionePagamento.model().PAGAMENTO.ID_SINGOLO_VERSAMENTO, returnAlias);
		}
		if(field.equals(RendicontazionePagamento.model().PAGAMENTO.ID_SINGOLO_VERSAMENTO.INDICE_DATI)){
			return this.toTable(RendicontazionePagamento.model().PAGAMENTO.ID_SINGOLO_VERSAMENTO, returnAlias);
		}
		if(field.equals(RendicontazionePagamento.model().PAGAMENTO.ID_SINGOLO_VERSAMENTO.ID_TRIBUTO.ID_DOMINIO.COD_DOMINIO)){
			return this.toTable(RendicontazionePagamento.model().PAGAMENTO.ID_SINGOLO_VERSAMENTO.ID_TRIBUTO.ID_DOMINIO, returnAlias);
		}
		if(field.equals(RendicontazionePagamento.model().PAGAMENTO.ID_SINGOLO_VERSAMENTO.ID_TRIBUTO.ID_TIPO_TRIBUTO.COD_TRIBUTO)){
			return this.toTable(RendicontazionePagamento.model().PAGAMENTO.ID_SINGOLO_VERSAMENTO.ID_TRIBUTO.ID_TIPO_TRIBUTO, returnAlias);
		}
		if(field.equals(RendicontazionePagamento.model().PAGAMENTO.ID_SINGOLO_VERSAMENTO.NOTE)){
			return this.toTable(RendicontazionePagamento.model().PAGAMENTO.ID_SINGOLO_VERSAMENTO, returnAlias);
		}
		if(field.equals(RendicontazionePagamento.model().PAGAMENTO.ID_SINGOLO_VERSAMENTO.STATO_SINGOLO_VERSAMENTO)){
			return this.toTable(RendicontazionePagamento.model().PAGAMENTO.ID_SINGOLO_VERSAMENTO, returnAlias);
		}
		if(field.equals(RendicontazionePagamento.model().PAGAMENTO.ID_SINGOLO_VERSAMENTO.IMPORTO_SINGOLO_VERSAMENTO)){
			return this.toTable(RendicontazionePagamento.model().PAGAMENTO.ID_SINGOLO_VERSAMENTO, returnAlias);
		}
		if(field.equals(RendicontazionePagamento.model().PAGAMENTO.COD_DOMINIO)){
			return this.toTable(RendicontazionePagamento.model().PAGAMENTO, returnAlias);
		}
		if(field.equals(RendicontazionePagamento.model().PAGAMENTO.IUV)){
			return this.toTable(RendicontazionePagamento.model().PAGAMENTO, returnAlias);
		}
		if(field.equals(RendicontazionePagamento.model().PAGAMENTO.INDICE_DATI)){
			return this.toTable(RendicontazionePagamento.model().PAGAMENTO, returnAlias);
		}
		if(field.equals(RendicontazionePagamento.model().PAGAMENTO.IMPORTO_PAGATO)){
			return this.toTable(RendicontazionePagamento.model().PAGAMENTO, returnAlias);
		}
		if(field.equals(RendicontazionePagamento.model().PAGAMENTO.DATA_ACQUISIZIONE)){
			return this.toTable(RendicontazionePagamento.model().PAGAMENTO, returnAlias);
		}
		if(field.equals(RendicontazionePagamento.model().PAGAMENTO.IUR)){
			return this.toTable(RendicontazionePagamento.model().PAGAMENTO, returnAlias);
		}
		if(field.equals(RendicontazionePagamento.model().PAGAMENTO.DATA_PAGAMENTO)){
			return this.toTable(RendicontazionePagamento.model().PAGAMENTO, returnAlias);
		}
		if(field.equals(RendicontazionePagamento.model().PAGAMENTO.COMMISSIONI_PSP)){
			return this.toTable(RendicontazionePagamento.model().PAGAMENTO, returnAlias);
		}
		if(field.equals(RendicontazionePagamento.model().PAGAMENTO.TIPO_ALLEGATO)){
			return this.toTable(RendicontazionePagamento.model().PAGAMENTO, returnAlias);
		}
		if(field.equals(RendicontazionePagamento.model().PAGAMENTO.ALLEGATO)){
			return this.toTable(RendicontazionePagamento.model().PAGAMENTO, returnAlias);
		}
		if(field.equals(RendicontazionePagamento.model().PAGAMENTO.ID_RR.COD_MSG_REVOCA)){
			return this.toTable(RendicontazionePagamento.model().PAGAMENTO.ID_RR, returnAlias);
		}
		if(field.equals(RendicontazionePagamento.model().PAGAMENTO.DATA_ACQUISIZIONE_REVOCA)){
			return this.toTable(RendicontazionePagamento.model().PAGAMENTO, returnAlias);
		}
		if(field.equals(RendicontazionePagamento.model().PAGAMENTO.CAUSALE_REVOCA)){
			return this.toTable(RendicontazionePagamento.model().PAGAMENTO, returnAlias);
		}
		if(field.equals(RendicontazionePagamento.model().PAGAMENTO.DATI_REVOCA)){
			return this.toTable(RendicontazionePagamento.model().PAGAMENTO, returnAlias);
		}
		if(field.equals(RendicontazionePagamento.model().PAGAMENTO.IMPORTO_REVOCATO)){
			return this.toTable(RendicontazionePagamento.model().PAGAMENTO, returnAlias);
		}
		if(field.equals(RendicontazionePagamento.model().PAGAMENTO.ESITO_REVOCA)){
			return this.toTable(RendicontazionePagamento.model().PAGAMENTO, returnAlias);
		}
		if(field.equals(RendicontazionePagamento.model().PAGAMENTO.DATI_ESITO_REVOCA)){
			return this.toTable(RendicontazionePagamento.model().PAGAMENTO, returnAlias);
		}
		if(field.equals(RendicontazionePagamento.model().PAGAMENTO.STATO)){
			return this.toTable(RendicontazionePagamento.model().PAGAMENTO, returnAlias);
		}
		if(field.equals(RendicontazionePagamento.model().PAGAMENTO.ID_INCASSO.TRN)){
			return this.toTable(RendicontazionePagamento.model().PAGAMENTO.ID_INCASSO, returnAlias);
		}
		if(field.equals(RendicontazionePagamento.model().PAGAMENTO.ID_INCASSO.COD_DOMINIO)){
			return this.toTable(RendicontazionePagamento.model().PAGAMENTO.ID_INCASSO, returnAlias);
		}
		if(field.equals(RendicontazionePagamento.model().PAGAMENTO.TIPO)){
			return this.toTable(RendicontazionePagamento.model().PAGAMENTO, returnAlias);
		}
		if(field.equals(RendicontazionePagamento.model().SINGOLO_VERSAMENTO.ID_VERSAMENTO.COD_VERSAMENTO_ENTE)){
			return this.toTable(RendicontazionePagamento.model().SINGOLO_VERSAMENTO.ID_VERSAMENTO, returnAlias);
		}
		if(field.equals(RendicontazionePagamento.model().SINGOLO_VERSAMENTO.ID_VERSAMENTO.ID_APPLICAZIONE.COD_APPLICAZIONE)){
			return this.toTable(RendicontazionePagamento.model().SINGOLO_VERSAMENTO.ID_VERSAMENTO.ID_APPLICAZIONE, returnAlias);
		}
		if(field.equals(RendicontazionePagamento.model().SINGOLO_VERSAMENTO.ID_VERSAMENTO.DEBITORE_IDENTIFICATIVO)){
			return this.toTable(RendicontazionePagamento.model().SINGOLO_VERSAMENTO.ID_VERSAMENTO, returnAlias);
		}
		if(field.equals(RendicontazionePagamento.model().SINGOLO_VERSAMENTO.ID_VERSAMENTO.DEBITORE_ANAGRAFICA)){
			return this.toTable(RendicontazionePagamento.model().SINGOLO_VERSAMENTO.ID_VERSAMENTO, returnAlias);
		}
		if(field.equals(RendicontazionePagamento.model().SINGOLO_VERSAMENTO.ID_VERSAMENTO.COD_VERSAMENTO_LOTTO)){
			return this.toTable(RendicontazionePagamento.model().SINGOLO_VERSAMENTO.ID_VERSAMENTO, returnAlias);
		}
		if(field.equals(RendicontazionePagamento.model().SINGOLO_VERSAMENTO.ID_VERSAMENTO.COD_ANNO_TRIBUTARIO)){
			return this.toTable(RendicontazionePagamento.model().SINGOLO_VERSAMENTO.ID_VERSAMENTO, returnAlias);
		}
		if(field.equals(RendicontazionePagamento.model().SINGOLO_VERSAMENTO.ID_VERSAMENTO.IMPORTO_TOTALE)){
			return this.toTable(RendicontazionePagamento.model().SINGOLO_VERSAMENTO.ID_VERSAMENTO, returnAlias);
		}
		if(field.equals(RendicontazionePagamento.model().SINGOLO_VERSAMENTO.ID_VERSAMENTO.CAUSALE_VERSAMENTO)){
			return this.toTable(RendicontazionePagamento.model().SINGOLO_VERSAMENTO.ID_VERSAMENTO, returnAlias);
		}
		if(field.equals(RendicontazionePagamento.model().SINGOLO_VERSAMENTO.ID_VERSAMENTO.STATO_VERSAMENTO)){
			return this.toTable(RendicontazionePagamento.model().SINGOLO_VERSAMENTO.ID_VERSAMENTO, returnAlias);
		}
		if(field.equals(RendicontazionePagamento.model().SINGOLO_VERSAMENTO.ID_VERSAMENTO.ID_UO.COD_UO)){
			return this.toTable(RendicontazionePagamento.model().SINGOLO_VERSAMENTO.ID_VERSAMENTO.ID_UO, returnAlias);
		}
		if(field.equals(RendicontazionePagamento.model().SINGOLO_VERSAMENTO.ID_VERSAMENTO.ID_UO.ID_DOMINIO.COD_DOMINIO)){
			return this.toTable(RendicontazionePagamento.model().SINGOLO_VERSAMENTO.ID_VERSAMENTO.ID_UO.ID_DOMINIO, returnAlias);
		}
		if(field.equals(RendicontazionePagamento.model().SINGOLO_VERSAMENTO.ID_VERSAMENTO.ID_TIPO_VERSAMENTO.COD_TIPO_VERSAMENTO)){
			return this.toTable(RendicontazionePagamento.model().SINGOLO_VERSAMENTO.ID_VERSAMENTO.ID_TIPO_VERSAMENTO, returnAlias);
		}
		if(field.equals(RendicontazionePagamento.model().SINGOLO_VERSAMENTO.ID_VERSAMENTO.ID_TIPO_VERSAMENTO.TIPO)){
			return this.toTable(RendicontazionePagamento.model().SINGOLO_VERSAMENTO.ID_VERSAMENTO.ID_TIPO_VERSAMENTO, returnAlias);
		}
		if(field.equals(RendicontazionePagamento.model().SINGOLO_VERSAMENTO.ID_VERSAMENTO.DIVISIONE)){
			return this.toTable(RendicontazionePagamento.model().SINGOLO_VERSAMENTO.ID_VERSAMENTO, returnAlias);
		}
		if(field.equals(RendicontazionePagamento.model().SINGOLO_VERSAMENTO.ID_VERSAMENTO.DIREZIONE)){
			return this.toTable(RendicontazionePagamento.model().SINGOLO_VERSAMENTO.ID_VERSAMENTO, returnAlias);
		}
		if(field.equals(RendicontazionePagamento.model().SINGOLO_VERSAMENTO.ID_VERSAMENTO.TASSONOMIA)){
			return this.toTable(RendicontazionePagamento.model().SINGOLO_VERSAMENTO.ID_VERSAMENTO, returnAlias);
		}
		if(field.equals(RendicontazionePagamento.model().SINGOLO_VERSAMENTO.ID_TRIBUTO.ID_DOMINIO.COD_DOMINIO)){
			return this.toTable(RendicontazionePagamento.model().SINGOLO_VERSAMENTO.ID_TRIBUTO.ID_DOMINIO, returnAlias);
		}
		if(field.equals(RendicontazionePagamento.model().SINGOLO_VERSAMENTO.ID_TRIBUTO.ID_TIPO_TRIBUTO.COD_TRIBUTO)){
			return this.toTable(RendicontazionePagamento.model().SINGOLO_VERSAMENTO.ID_TRIBUTO.ID_TIPO_TRIBUTO, returnAlias);
		}
		if(field.equals(RendicontazionePagamento.model().SINGOLO_VERSAMENTO.COD_SINGOLO_VERSAMENTO_ENTE)){
			return this.toTable(RendicontazionePagamento.model().SINGOLO_VERSAMENTO, returnAlias);
		}
		if(field.equals(RendicontazionePagamento.model().SINGOLO_VERSAMENTO.STATO_SINGOLO_VERSAMENTO)){
			return this.toTable(RendicontazionePagamento.model().SINGOLO_VERSAMENTO, returnAlias);
		}
		if(field.equals(RendicontazionePagamento.model().SINGOLO_VERSAMENTO.IMPORTO_SINGOLO_VERSAMENTO)){
			return this.toTable(RendicontazionePagamento.model().SINGOLO_VERSAMENTO, returnAlias);
		}
		if(field.equals(RendicontazionePagamento.model().SINGOLO_VERSAMENTO.TIPO_BOLLO)){
			return this.toTable(RendicontazionePagamento.model().SINGOLO_VERSAMENTO, returnAlias);
		}
		if(field.equals(RendicontazionePagamento.model().SINGOLO_VERSAMENTO.HASH_DOCUMENTO)){
			return this.toTable(RendicontazionePagamento.model().SINGOLO_VERSAMENTO, returnAlias);
		}
		if(field.equals(RendicontazionePagamento.model().SINGOLO_VERSAMENTO.PROVINCIA_RESIDENZA)){
			return this.toTable(RendicontazionePagamento.model().SINGOLO_VERSAMENTO, returnAlias);
		}
		if(field.equals(RendicontazionePagamento.model().SINGOLO_VERSAMENTO.ID_IBAN_ACCREDITO.COD_IBAN)){
			return this.toTable(RendicontazionePagamento.model().SINGOLO_VERSAMENTO.ID_IBAN_ACCREDITO, returnAlias);
		}
		if(field.equals(RendicontazionePagamento.model().SINGOLO_VERSAMENTO.ID_IBAN_ACCREDITO.ID_DOMINIO.COD_DOMINIO)){
			return this.toTable(RendicontazionePagamento.model().SINGOLO_VERSAMENTO.ID_IBAN_ACCREDITO.ID_DOMINIO, returnAlias);
		}
		if(field.equals(RendicontazionePagamento.model().SINGOLO_VERSAMENTO.ID_IBAN_APPOGGIO.COD_IBAN)){
			return this.toTable(RendicontazionePagamento.model().SINGOLO_VERSAMENTO.ID_IBAN_APPOGGIO, returnAlias);
		}
		if(field.equals(RendicontazionePagamento.model().SINGOLO_VERSAMENTO.ID_IBAN_APPOGGIO.ID_DOMINIO.COD_DOMINIO)){
			return this.toTable(RendicontazionePagamento.model().SINGOLO_VERSAMENTO.ID_IBAN_APPOGGIO.ID_DOMINIO, returnAlias);
		}
		if(field.equals(RendicontazionePagamento.model().SINGOLO_VERSAMENTO.TIPO_CONTABILITA)){
			return this.toTable(RendicontazionePagamento.model().SINGOLO_VERSAMENTO, returnAlias);
		}
		if(field.equals(RendicontazionePagamento.model().SINGOLO_VERSAMENTO.CODICE_CONTABILITA)){
			return this.toTable(RendicontazionePagamento.model().SINGOLO_VERSAMENTO, returnAlias);
		}
		if(field.equals(RendicontazionePagamento.model().SINGOLO_VERSAMENTO.DESCRIZIONE)){
			return this.toTable(RendicontazionePagamento.model().SINGOLO_VERSAMENTO, returnAlias);
		}
		if(field.equals(RendicontazionePagamento.model().SINGOLO_VERSAMENTO.DATI_ALLEGATI)){
			return this.toTable(RendicontazionePagamento.model().SINGOLO_VERSAMENTO, returnAlias);
		}
		if(field.equals(RendicontazionePagamento.model().SINGOLO_VERSAMENTO.INDICE_DATI)){
			return this.toTable(RendicontazionePagamento.model().SINGOLO_VERSAMENTO, returnAlias);
		}
		if(field.equals(RendicontazionePagamento.model().SINGOLO_VERSAMENTO.DESCRIZIONE_CAUSALE_RPT)){
			return this.toTable(RendicontazionePagamento.model().SINGOLO_VERSAMENTO, returnAlias);
		}
		if(field.equals(RendicontazionePagamento.model().VERSAMENTO.COD_VERSAMENTO_ENTE)){
			return this.toTable(RendicontazionePagamento.model().VERSAMENTO, returnAlias);
		}
		if(field.equals(RendicontazionePagamento.model().VERSAMENTO.NOME)){
			return this.toTable(RendicontazionePagamento.model().VERSAMENTO, returnAlias);
		}
		if(field.equals(RendicontazionePagamento.model().VERSAMENTO.ID_TIPO_VERSAMENTO_DOMINIO.ID_DOMINIO.COD_DOMINIO)){
			return this.toTable(RendicontazionePagamento.model().VERSAMENTO.ID_TIPO_VERSAMENTO_DOMINIO.ID_DOMINIO, returnAlias);
		}
		if(field.equals(RendicontazionePagamento.model().VERSAMENTO.ID_TIPO_VERSAMENTO_DOMINIO.ID_TIPO_VERSAMENTO.COD_TIPO_VERSAMENTO)){
			return this.toTable(RendicontazionePagamento.model().VERSAMENTO.ID_TIPO_VERSAMENTO_DOMINIO.ID_TIPO_VERSAMENTO, returnAlias);
		}
		if(field.equals(RendicontazionePagamento.model().VERSAMENTO.ID_TIPO_VERSAMENTO_DOMINIO.ID_TIPO_VERSAMENTO.TIPO)){
			return this.toTable(RendicontazionePagamento.model().VERSAMENTO.ID_TIPO_VERSAMENTO_DOMINIO.ID_TIPO_VERSAMENTO, returnAlias);
		}
		if(field.equals(RendicontazionePagamento.model().VERSAMENTO.ID_TIPO_VERSAMENTO.COD_TIPO_VERSAMENTO)){
			return this.toTable(RendicontazionePagamento.model().VERSAMENTO.ID_TIPO_VERSAMENTO, returnAlias);
		}
		if(field.equals(RendicontazionePagamento.model().VERSAMENTO.ID_TIPO_VERSAMENTO.TIPO)){
			return this.toTable(RendicontazionePagamento.model().VERSAMENTO.ID_TIPO_VERSAMENTO, returnAlias);
		}
		if(field.equals(RendicontazionePagamento.model().VERSAMENTO.ID_DOMINIO.COD_DOMINIO)){
			return this.toTable(RendicontazionePagamento.model().VERSAMENTO.ID_DOMINIO, returnAlias);
		}
		if(field.equals(RendicontazionePagamento.model().VERSAMENTO.ID_UO.COD_UO)){
			return this.toTable(RendicontazionePagamento.model().VERSAMENTO.ID_UO, returnAlias);
		}
		if(field.equals(RendicontazionePagamento.model().VERSAMENTO.ID_UO.ID_DOMINIO.COD_DOMINIO)){
			return this.toTable(RendicontazionePagamento.model().VERSAMENTO.ID_UO.ID_DOMINIO, returnAlias);
		}
		if(field.equals(RendicontazionePagamento.model().VERSAMENTO.ID_APPLICAZIONE.COD_APPLICAZIONE)){
			return this.toTable(RendicontazionePagamento.model().VERSAMENTO.ID_APPLICAZIONE, returnAlias);
		}
		if(field.equals(RendicontazionePagamento.model().VERSAMENTO.ID_PAGAMENTO_PORTALE.ID_SESSIONE)){
			return this.toTable(RendicontazionePagamento.model().VERSAMENTO.ID_PAGAMENTO_PORTALE, returnAlias);
		}
		if(field.equals(RendicontazionePagamento.model().VERSAMENTO.ID_PAGAMENTO_PORTALE.ID_APPLICAZIONE.COD_APPLICAZIONE)){
			return this.toTable(RendicontazionePagamento.model().VERSAMENTO.ID_PAGAMENTO_PORTALE.ID_APPLICAZIONE, returnAlias);
		}
		if(field.equals(RendicontazionePagamento.model().VERSAMENTO.ID_PAGAMENTO_PORTALE.VERSANTE_IDENTIFICATIVO)){
			return this.toTable(RendicontazionePagamento.model().VERSAMENTO.ID_PAGAMENTO_PORTALE, returnAlias);
		}
		if(field.equals(RendicontazionePagamento.model().VERSAMENTO.IUV.IUV)){
			return this.toTable(RendicontazionePagamento.model().VERSAMENTO.IUV, returnAlias);
		}
		if(field.equals(RendicontazionePagamento.model().VERSAMENTO.IMPORTO_TOTALE)){
			return this.toTable(RendicontazionePagamento.model().VERSAMENTO, returnAlias);
		}
		if(field.equals(RendicontazionePagamento.model().VERSAMENTO.STATO_VERSAMENTO)){
			return this.toTable(RendicontazionePagamento.model().VERSAMENTO, returnAlias);
		}
		if(field.equals(RendicontazionePagamento.model().VERSAMENTO.DESCRIZIONE_STATO)){
			return this.toTable(RendicontazionePagamento.model().VERSAMENTO, returnAlias);
		}
		if(field.equals(RendicontazionePagamento.model().VERSAMENTO.AGGIORNABILE)){
			return this.toTable(RendicontazionePagamento.model().VERSAMENTO, returnAlias);
		}
		if(field.equals(RendicontazionePagamento.model().VERSAMENTO.DATA_CREAZIONE)){
			return this.toTable(RendicontazionePagamento.model().VERSAMENTO, returnAlias);
		}
		if(field.equals(RendicontazionePagamento.model().VERSAMENTO.DATA_VALIDITA)){
			return this.toTable(RendicontazionePagamento.model().VERSAMENTO, returnAlias);
		}
		if(field.equals(RendicontazionePagamento.model().VERSAMENTO.DATA_SCADENZA)){
			return this.toTable(RendicontazionePagamento.model().VERSAMENTO, returnAlias);
		}
		if(field.equals(RendicontazionePagamento.model().VERSAMENTO.DATA_ORA_ULTIMO_AGGIORNAMENTO)){
			return this.toTable(RendicontazionePagamento.model().VERSAMENTO, returnAlias);
		}
		if(field.equals(RendicontazionePagamento.model().VERSAMENTO.CAUSALE_VERSAMENTO)){
			return this.toTable(RendicontazionePagamento.model().VERSAMENTO, returnAlias);
		}
		if(field.equals(RendicontazionePagamento.model().VERSAMENTO.DEBITORE_TIPO)){
			return this.toTable(RendicontazionePagamento.model().VERSAMENTO, returnAlias);
		}
		if(field.equals(RendicontazionePagamento.model().VERSAMENTO.DEBITORE_IDENTIFICATIVO)){
			return this.toTable(RendicontazionePagamento.model().VERSAMENTO, returnAlias);
		}
		if(field.equals(RendicontazionePagamento.model().VERSAMENTO.DEBITORE_ANAGRAFICA)){
			return this.toTable(RendicontazionePagamento.model().VERSAMENTO, returnAlias);
		}
		if(field.equals(RendicontazionePagamento.model().VERSAMENTO.DEBITORE_INDIRIZZO)){
			return this.toTable(RendicontazionePagamento.model().VERSAMENTO, returnAlias);
		}
		if(field.equals(RendicontazionePagamento.model().VERSAMENTO.DEBITORE_CIVICO)){
			return this.toTable(RendicontazionePagamento.model().VERSAMENTO, returnAlias);
		}
		if(field.equals(RendicontazionePagamento.model().VERSAMENTO.DEBITORE_CAP)){
			return this.toTable(RendicontazionePagamento.model().VERSAMENTO, returnAlias);
		}
		if(field.equals(RendicontazionePagamento.model().VERSAMENTO.DEBITORE_LOCALITA)){
			return this.toTable(RendicontazionePagamento.model().VERSAMENTO, returnAlias);
		}
		if(field.equals(RendicontazionePagamento.model().VERSAMENTO.DEBITORE_PROVINCIA)){
			return this.toTable(RendicontazionePagamento.model().VERSAMENTO, returnAlias);
		}
		if(field.equals(RendicontazionePagamento.model().VERSAMENTO.DEBITORE_NAZIONE)){
			return this.toTable(RendicontazionePagamento.model().VERSAMENTO, returnAlias);
		}
		if(field.equals(RendicontazionePagamento.model().VERSAMENTO.DEBITORE_EMAIL)){
			return this.toTable(RendicontazionePagamento.model().VERSAMENTO, returnAlias);
		}
		if(field.equals(RendicontazionePagamento.model().VERSAMENTO.DEBITORE_TELEFONO)){
			return this.toTable(RendicontazionePagamento.model().VERSAMENTO, returnAlias);
		}
		if(field.equals(RendicontazionePagamento.model().VERSAMENTO.DEBITORE_CELLULARE)){
			return this.toTable(RendicontazionePagamento.model().VERSAMENTO, returnAlias);
		}
		if(field.equals(RendicontazionePagamento.model().VERSAMENTO.DEBITORE_FAX)){
			return this.toTable(RendicontazionePagamento.model().VERSAMENTO, returnAlias);
		}
		if(field.equals(RendicontazionePagamento.model().VERSAMENTO.TASSONOMIA_AVVISO)){
			return this.toTable(RendicontazionePagamento.model().VERSAMENTO, returnAlias);
		}
		if(field.equals(RendicontazionePagamento.model().VERSAMENTO.TASSONOMIA)){
			return this.toTable(RendicontazionePagamento.model().VERSAMENTO, returnAlias);
		}
		if(field.equals(RendicontazionePagamento.model().VERSAMENTO.COD_LOTTO)){
			return this.toTable(RendicontazionePagamento.model().VERSAMENTO, returnAlias);
		}
		if(field.equals(RendicontazionePagamento.model().VERSAMENTO.COD_VERSAMENTO_LOTTO)){
			return this.toTable(RendicontazionePagamento.model().VERSAMENTO, returnAlias);
		}
		if(field.equals(RendicontazionePagamento.model().VERSAMENTO.COD_ANNO_TRIBUTARIO)){
			return this.toTable(RendicontazionePagamento.model().VERSAMENTO, returnAlias);
		}
		if(field.equals(RendicontazionePagamento.model().VERSAMENTO.COD_BUNDLEKEY)){
			return this.toTable(RendicontazionePagamento.model().VERSAMENTO, returnAlias);
		}
		if(field.equals(RendicontazionePagamento.model().VERSAMENTO.DATI_ALLEGATI)){
			return this.toTable(RendicontazionePagamento.model().VERSAMENTO, returnAlias);
		}
		if(field.equals(RendicontazionePagamento.model().VERSAMENTO.INCASSO)){
			return this.toTable(RendicontazionePagamento.model().VERSAMENTO, returnAlias);
		}
		if(field.equals(RendicontazionePagamento.model().VERSAMENTO.ANOMALIE)){
			return this.toTable(RendicontazionePagamento.model().VERSAMENTO, returnAlias);
		}
		if(field.equals(RendicontazionePagamento.model().VERSAMENTO.IUV_VERSAMENTO)){
			return this.toTable(RendicontazionePagamento.model().VERSAMENTO, returnAlias);
		}
		if(field.equals(RendicontazionePagamento.model().VERSAMENTO.NUMERO_AVVISO)){
			return this.toTable(RendicontazionePagamento.model().VERSAMENTO, returnAlias);
		}
		if(field.equals(RendicontazionePagamento.model().VERSAMENTO.AVVISATURA_ABILITATA)){
			return this.toTable(RendicontazionePagamento.model().VERSAMENTO, returnAlias);
		}
		if(field.equals(RendicontazionePagamento.model().VERSAMENTO.AVVISATURA_DA_INVIARE)){
			return this.toTable(RendicontazionePagamento.model().VERSAMENTO, returnAlias);
		}
		if(field.equals(RendicontazionePagamento.model().VERSAMENTO.AVVISATURA_OPERAZIONE)){
			return this.toTable(RendicontazionePagamento.model().VERSAMENTO, returnAlias);
		}
		if(field.equals(RendicontazionePagamento.model().VERSAMENTO.AVVISATURA_MODALITA)){
			return this.toTable(RendicontazionePagamento.model().VERSAMENTO, returnAlias);
		}
		if(field.equals(RendicontazionePagamento.model().VERSAMENTO.AVVISATURA_TIPO_PAGAMENTO)){
			return this.toTable(RendicontazionePagamento.model().VERSAMENTO, returnAlias);
		}
		if(field.equals(RendicontazionePagamento.model().VERSAMENTO.AVVISATURA_COD_AVVISATURA)){
			return this.toTable(RendicontazionePagamento.model().VERSAMENTO, returnAlias);
		}
		if(field.equals(RendicontazionePagamento.model().VERSAMENTO.ID_TRACCIATO_AVVISATURA.ID_TRACCIATO)){
			return this.toTable(RendicontazionePagamento.model().VERSAMENTO.ID_TRACCIATO_AVVISATURA, returnAlias);
		}
		if(field.equals(RendicontazionePagamento.model().VERSAMENTO.ACK)){
			return this.toTable(RendicontazionePagamento.model().VERSAMENTO, returnAlias);
		}
		if(field.equals(RendicontazionePagamento.model().VERSAMENTO.ANOMALO)){
			return this.toTable(RendicontazionePagamento.model().VERSAMENTO, returnAlias);
		}
		if(field.equals(RendicontazionePagamento.model().VERSAMENTO.DIVISIONE)){
			return this.toTable(RendicontazionePagamento.model().VERSAMENTO, returnAlias);
		}
		if(field.equals(RendicontazionePagamento.model().VERSAMENTO.DIREZIONE)){
			return this.toTable(RendicontazionePagamento.model().VERSAMENTO, returnAlias);
		}
		if(field.equals(RendicontazionePagamento.model().VERSAMENTO.ID_SESSIONE)){
			return this.toTable(RendicontazionePagamento.model().VERSAMENTO, returnAlias);
		}
		if(field.equals(RendicontazionePagamento.model().VERSAMENTO.DATA_PAGAMENTO)){
			return this.toTable(RendicontazionePagamento.model().VERSAMENTO, returnAlias);
		}
		if(field.equals(RendicontazionePagamento.model().VERSAMENTO.IMPORTO_PAGATO)){
			return this.toTable(RendicontazionePagamento.model().VERSAMENTO, returnAlias);
		}
		if(field.equals(RendicontazionePagamento.model().VERSAMENTO.IMPORTO_INCASSATO)){
			return this.toTable(RendicontazionePagamento.model().VERSAMENTO, returnAlias);
		}
		if(field.equals(RendicontazionePagamento.model().VERSAMENTO.STATO_PAGAMENTO)){
			return this.toTable(RendicontazionePagamento.model().VERSAMENTO, returnAlias);
		}
		if(field.equals(RendicontazionePagamento.model().VERSAMENTO.IUV_PAGAMENTO)){
			return this.toTable(RendicontazionePagamento.model().VERSAMENTO, returnAlias);
		}


		return super.toTable(field,returnAlias);
		
	}

	@Override
	public String toTable(IModel<?> model,boolean returnAlias) throws ExpressionException {
		
		// In the case of table with alias, using parameter returnAlias​​, 
		// it is possible to drive the choice whether to return only the alias or 
		// the full definition of the table containing the alias
		
		if(model.equals(RendicontazionePagamento.model())){
			return null;
		}
		if(model.equals(RendicontazionePagamento.model().FR)){
			return "fr";
		}
		if(model.equals(RendicontazionePagamento.model().FR.ID_SINGOLO_VERSAMENTO)){
			return "singoli_versamenti";
		}
		if(model.equals(RendicontazionePagamento.model().FR.ID_SINGOLO_VERSAMENTO.ID_VERSAMENTO)){
			return "versamenti";
		}
		if(model.equals(RendicontazionePagamento.model().FR.ID_SINGOLO_VERSAMENTO.ID_VERSAMENTO.ID_APPLICAZIONE)){
			return "applicazioni";
		}
		if(model.equals(RendicontazionePagamento.model().FR.ID_SINGOLO_VERSAMENTO.ID_VERSAMENTO.ID_UO)){
			return "uo";
		}
		if(model.equals(RendicontazionePagamento.model().FR.ID_SINGOLO_VERSAMENTO.ID_VERSAMENTO.ID_UO.ID_DOMINIO)){
			return "domini";
		}
		if(model.equals(RendicontazionePagamento.model().FR.ID_SINGOLO_VERSAMENTO.ID_VERSAMENTO.ID_TIPO_VERSAMENTO)){
			return "tipi_versamento";
		}
		if(model.equals(RendicontazionePagamento.model().FR.ID_SINGOLO_VERSAMENTO.ID_TRIBUTO)){
			return "tributi";
		}
		if(model.equals(RendicontazionePagamento.model().FR.ID_SINGOLO_VERSAMENTO.ID_TRIBUTO.ID_DOMINIO)){
			return "domini";
		}
		if(model.equals(RendicontazionePagamento.model().FR.ID_SINGOLO_VERSAMENTO.ID_TRIBUTO.ID_TIPO_TRIBUTO)){
			return "tipi_tributo";
		}
		if(model.equals(RendicontazionePagamento.model().FR.ID_INCASSO)){
			return "incassi";
		}
		if(model.equals(RendicontazionePagamento.model().RENDICONTAZIONE)){
			return "rendicontazioni";
		}
		if(model.equals(RendicontazionePagamento.model().RENDICONTAZIONE.ID_FR)){
			return "fr";
		}
		if(model.equals(RendicontazionePagamento.model().RENDICONTAZIONE.ID_PAGAMENTO)){
			return "pagamenti";
		}
		if(model.equals(RendicontazionePagamento.model().RENDICONTAZIONE.ID_SINGOLO_VERSAMENTO)){
			return "singoli_versamenti";
		}
		if(model.equals(RendicontazionePagamento.model().RENDICONTAZIONE.ID_SINGOLO_VERSAMENTO.ID_VERSAMENTO)){
			return "versamenti";
		}
		if(model.equals(RendicontazionePagamento.model().RENDICONTAZIONE.ID_SINGOLO_VERSAMENTO.ID_VERSAMENTO.ID_APPLICAZIONE)){
			return "applicazioni";
		}
		if(model.equals(RendicontazionePagamento.model().RENDICONTAZIONE.ID_SINGOLO_VERSAMENTO.ID_VERSAMENTO.ID_UO)){
			return "uo";
		}
		if(model.equals(RendicontazionePagamento.model().RENDICONTAZIONE.ID_SINGOLO_VERSAMENTO.ID_VERSAMENTO.ID_UO.ID_DOMINIO)){
			return "domini";
		}
		if(model.equals(RendicontazionePagamento.model().RENDICONTAZIONE.ID_SINGOLO_VERSAMENTO.ID_VERSAMENTO.ID_TIPO_VERSAMENTO)){
			return "tipi_versamento";
		}
		if(model.equals(RendicontazionePagamento.model().RENDICONTAZIONE.ID_SINGOLO_VERSAMENTO.ID_TRIBUTO)){
			return "tributi";
		}
		if(model.equals(RendicontazionePagamento.model().RENDICONTAZIONE.ID_SINGOLO_VERSAMENTO.ID_TRIBUTO.ID_DOMINIO)){
			return "domini";
		}
		if(model.equals(RendicontazionePagamento.model().RENDICONTAZIONE.ID_SINGOLO_VERSAMENTO.ID_TRIBUTO.ID_TIPO_TRIBUTO)){
			return "tipi_tributo";
		}
		if(model.equals(RendicontazionePagamento.model().PAGAMENTO)){
			return "pagamenti";
		}
		if(model.equals(RendicontazionePagamento.model().PAGAMENTO.ID_RPT)){
			return "rpt";
		}
		if(model.equals(RendicontazionePagamento.model().PAGAMENTO.ID_SINGOLO_VERSAMENTO)){
			return "singoli_versamenti";
		}
		if(model.equals(RendicontazionePagamento.model().PAGAMENTO.ID_SINGOLO_VERSAMENTO.ID_VERSAMENTO)){
			return "versamenti";
		}
		if(model.equals(RendicontazionePagamento.model().PAGAMENTO.ID_SINGOLO_VERSAMENTO.ID_VERSAMENTO.ID_APPLICAZIONE)){
			return "applicazioni";
		}
		if(model.equals(RendicontazionePagamento.model().PAGAMENTO.ID_SINGOLO_VERSAMENTO.ID_VERSAMENTO.ID_UO)){
			return "uo";
		}
		if(model.equals(RendicontazionePagamento.model().PAGAMENTO.ID_SINGOLO_VERSAMENTO.ID_VERSAMENTO.ID_UO.ID_DOMINIO)){
			return "domini";
		}
		if(model.equals(RendicontazionePagamento.model().PAGAMENTO.ID_SINGOLO_VERSAMENTO.ID_VERSAMENTO.ID_TIPO_VERSAMENTO)){
			return "tipi_versamento";
		}
		if(model.equals(RendicontazionePagamento.model().PAGAMENTO.ID_SINGOLO_VERSAMENTO.ID_TRIBUTO)){
			return "tributi";
		}
		if(model.equals(RendicontazionePagamento.model().PAGAMENTO.ID_SINGOLO_VERSAMENTO.ID_TRIBUTO.ID_DOMINIO)){
			return "domini";
		}
		if(model.equals(RendicontazionePagamento.model().PAGAMENTO.ID_SINGOLO_VERSAMENTO.ID_TRIBUTO.ID_TIPO_TRIBUTO)){
			return "tipi_tributo";
		}
		if(model.equals(RendicontazionePagamento.model().PAGAMENTO.ID_RR)){
			return "rr";
		}
		if(model.equals(RendicontazionePagamento.model().PAGAMENTO.ID_INCASSO)){
			return "incassi";
		}
		if(model.equals(RendicontazionePagamento.model().SINGOLO_VERSAMENTO)){
			return "singoli_versamenti";
		}
		if(model.equals(RendicontazionePagamento.model().SINGOLO_VERSAMENTO.ID_VERSAMENTO)){
			return "versamenti";
		}
		if(model.equals(RendicontazionePagamento.model().SINGOLO_VERSAMENTO.ID_VERSAMENTO.ID_APPLICAZIONE)){
			return "applicazioni";
		}
		if(model.equals(RendicontazionePagamento.model().SINGOLO_VERSAMENTO.ID_VERSAMENTO.ID_UO)){
			return "uo";
		}
		if(model.equals(RendicontazionePagamento.model().SINGOLO_VERSAMENTO.ID_VERSAMENTO.ID_UO.ID_DOMINIO)){
			return "domini";
		}
		if(model.equals(RendicontazionePagamento.model().SINGOLO_VERSAMENTO.ID_VERSAMENTO.ID_TIPO_VERSAMENTO)){
			return "tipi_versamento";
		}
		if(model.equals(RendicontazionePagamento.model().SINGOLO_VERSAMENTO.ID_TRIBUTO)){
			return "tributi";
		}
		if(model.equals(RendicontazionePagamento.model().SINGOLO_VERSAMENTO.ID_TRIBUTO.ID_DOMINIO)){
			return "domini";
		}
		if(model.equals(RendicontazionePagamento.model().SINGOLO_VERSAMENTO.ID_TRIBUTO.ID_TIPO_TRIBUTO)){
			return "tipi_tributo";
		}
		if(model.equals(RendicontazionePagamento.model().SINGOLO_VERSAMENTO.ID_IBAN_ACCREDITO)){
			return "iban_accredito";
		}
		if(model.equals(RendicontazionePagamento.model().SINGOLO_VERSAMENTO.ID_IBAN_ACCREDITO.ID_DOMINIO)){
			return "domini";
		}
		if(model.equals(RendicontazionePagamento.model().SINGOLO_VERSAMENTO.ID_IBAN_APPOGGIO)){
			return "iban_accredito";
		}
		if(model.equals(RendicontazionePagamento.model().SINGOLO_VERSAMENTO.ID_IBAN_APPOGGIO.ID_DOMINIO)){
			return "domini";
		}
		if(model.equals(RendicontazionePagamento.model().VERSAMENTO)){
			return "versamenti";
		}
		if(model.equals(RendicontazionePagamento.model().VERSAMENTO.ID_TIPO_VERSAMENTO_DOMINIO)){
			return "tipi_vers_domini";
		}
		if(model.equals(RendicontazionePagamento.model().VERSAMENTO.ID_TIPO_VERSAMENTO_DOMINIO.ID_DOMINIO)){
			return "domini";
		}
		if(model.equals(RendicontazionePagamento.model().VERSAMENTO.ID_TIPO_VERSAMENTO_DOMINIO.ID_TIPO_VERSAMENTO)){
			return "tipi_versamento";
		}
		if(model.equals(RendicontazionePagamento.model().VERSAMENTO.ID_TIPO_VERSAMENTO)){
			return "tipi_versamento";
		}
		if(model.equals(RendicontazionePagamento.model().VERSAMENTO.ID_DOMINIO)){
			return "domini";
		}
		if(model.equals(RendicontazionePagamento.model().VERSAMENTO.ID_UO)){
			return "uo";
		}
		if(model.equals(RendicontazionePagamento.model().VERSAMENTO.ID_UO.ID_DOMINIO)){
			return "domini";
		}
		if(model.equals(RendicontazionePagamento.model().VERSAMENTO.ID_APPLICAZIONE)){
			return "applicazioni";
		}
		if(model.equals(RendicontazionePagamento.model().VERSAMENTO.ID_PAGAMENTO_PORTALE)){
			return "pagamenti_portale";
		}
		if(model.equals(RendicontazionePagamento.model().VERSAMENTO.ID_PAGAMENTO_PORTALE.ID_APPLICAZIONE)){
			return "applicazioni";
		}
		if(model.equals(RendicontazionePagamento.model().VERSAMENTO.IUV)){
			return "iuv";
		}
		if(model.equals(RendicontazionePagamento.model().VERSAMENTO.ID_TRACCIATO_AVVISATURA)){
			return "tracciati";
		}


		return super.toTable(model,returnAlias);
		
	}

}
