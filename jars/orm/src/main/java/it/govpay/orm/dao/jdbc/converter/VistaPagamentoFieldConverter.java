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

import it.govpay.orm.VistaPagamento;


/**     
 * VistaPagamentoFieldConverter
 *
 * @author Giovanni Bussu (bussu@link.it)
 * @author Lorenzo Nardi (nardi@link.it)
 * @author $Author$
 * @version $Rev$, $Date$
 */
public class VistaPagamentoFieldConverter extends AbstractSQLFieldConverter {

	private TipiDatabase databaseType;
	
	public VistaPagamentoFieldConverter(String databaseType){
		this.databaseType = TipiDatabase.toEnumConstant(databaseType);
	}
	public VistaPagamentoFieldConverter(TipiDatabase databaseType){
		this.databaseType = databaseType;
	}


	@Override
	public IModel<?> getRootModel() throws ExpressionException {
		return VistaPagamento.model();
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
		
		if(field.equals(VistaPagamento.model().COD_DOMINIO)){
			if(appendTablePrefix){
				return this.toAliasTable(field)+".cod_dominio";
			}else{
				return "cod_dominio";
			}
		}
		if(field.equals(VistaPagamento.model().IUV)){
			if(appendTablePrefix){
				return this.toAliasTable(field)+".iuv";
			}else{
				return "iuv";
			}
		}
		if(field.equals(VistaPagamento.model().INDICE_DATI)){
			if(appendTablePrefix){
				return this.toAliasTable(field)+".indice_dati";
			}else{
				return "indice_dati";
			}
		}
		if(field.equals(VistaPagamento.model().IMPORTO_PAGATO)){
			if(appendTablePrefix){
				return this.toAliasTable(field)+".importo_pagato";
			}else{
				return "importo_pagato";
			}
		}
		if(field.equals(VistaPagamento.model().DATA_ACQUISIZIONE)){
			if(appendTablePrefix){
				return this.toAliasTable(field)+".data_acquisizione";
			}else{
				return "data_acquisizione";
			}
		}
		if(field.equals(VistaPagamento.model().IUR)){
			if(appendTablePrefix){
				return this.toAliasTable(field)+".iur";
			}else{
				return "iur";
			}
		}
		if(field.equals(VistaPagamento.model().DATA_PAGAMENTO)){
			if(appendTablePrefix){
				return this.toAliasTable(field)+".data_pagamento";
			}else{
				return "data_pagamento";
			}
		}
		if(field.equals(VistaPagamento.model().COMMISSIONI_PSP)){
			if(appendTablePrefix){
				return this.toAliasTable(field)+".commissioni_psp";
			}else{
				return "commissioni_psp";
			}
		}
		if(field.equals(VistaPagamento.model().TIPO_ALLEGATO)){
			if(appendTablePrefix){
				return this.toAliasTable(field)+".tipo_allegato";
			}else{
				return "tipo_allegato";
			}
		}
		if(field.equals(VistaPagamento.model().ALLEGATO)){
			if(appendTablePrefix){
				return this.toAliasTable(field)+".allegato";
			}else{
				return "allegato";
			}
		}
		if(field.equals(VistaPagamento.model().DATA_ACQUISIZIONE_REVOCA)){
			if(appendTablePrefix){
				return this.toAliasTable(field)+".data_acquisizione_revoca";
			}else{
				return "data_acquisizione_revoca";
			}
		}
		if(field.equals(VistaPagamento.model().CAUSALE_REVOCA)){
			if(appendTablePrefix){
				return this.toAliasTable(field)+".causale_revoca";
			}else{
				return "causale_revoca";
			}
		}
		if(field.equals(VistaPagamento.model().DATI_REVOCA)){
			if(appendTablePrefix){
				return this.toAliasTable(field)+".dati_revoca";
			}else{
				return "dati_revoca";
			}
		}
		if(field.equals(VistaPagamento.model().IMPORTO_REVOCATO)){
			if(appendTablePrefix){
				return this.toAliasTable(field)+".importo_revocato";
			}else{
				return "importo_revocato";
			}
		}
		if(field.equals(VistaPagamento.model().ESITO_REVOCA)){
			if(appendTablePrefix){
				return this.toAliasTable(field)+".esito_revoca";
			}else{
				return "esito_revoca";
			}
		}
		if(field.equals(VistaPagamento.model().DATI_ESITO_REVOCA)){
			if(appendTablePrefix){
				return this.toAliasTable(field)+".dati_esito_revoca";
			}else{
				return "dati_esito_revoca";
			}
		}
		if(field.equals(VistaPagamento.model().STATO)){
			if(appendTablePrefix){
				return this.toAliasTable(field)+".stato";
			}else{
				return "stato";
			}
		}
		if(field.equals(VistaPagamento.model().TIPO)){
			if(appendTablePrefix){
				return this.toAliasTable(field)+".tipo";
			}else{
				return "tipo";
			}
		}
		if(field.equals(VistaPagamento.model().VRS_ID)){
			if(appendTablePrefix){
				return this.toAliasTable(field)+".vrs_id";
			}else{
				return "vrs_id";
			}
		}
		if(field.equals(VistaPagamento.model().VRS_COD_VERSAMENTO_ENTE)){
			if(appendTablePrefix){
				return this.toAliasTable(field)+".vrs_cod_versamento_ente";
			}else{
				return "vrs_cod_versamento_ente";
			}
		}
		if(field.equals(VistaPagamento.model().VRS_ID_TIPO_VERSAMENTO_DOMINIO.ID_DOMINIO.COD_DOMINIO)){
			if(appendTablePrefix){
				return this.toAliasTable(field)+".cod_dominio";
			}else{
				return "cod_dominio";
			}
		}
		if(field.equals(VistaPagamento.model().VRS_ID_TIPO_VERSAMENTO_DOMINIO.ID_TIPO_VERSAMENTO.COD_TIPO_VERSAMENTO)){
			if(appendTablePrefix){
				return this.toAliasTable(field)+".cod_tipo_versamento";
			}else{
				return "cod_tipo_versamento";
			}
		}
		if(field.equals(VistaPagamento.model().VRS_ID_TIPO_VERSAMENTO.COD_TIPO_VERSAMENTO)){
			if(appendTablePrefix){
				return this.toAliasTable(field)+".cod_tipo_versamento";
			}else{
				return "cod_tipo_versamento";
			}
		}
		if(field.equals(VistaPagamento.model().VRS_ID_DOMINIO.COD_DOMINIO)){
			if(appendTablePrefix){
				return this.toAliasTable(field)+".cod_dominio";
			}else{
				return "cod_dominio";
			}
		}
		if(field.equals(VistaPagamento.model().VRS_ID_UO.COD_UO)){
			if(appendTablePrefix){
				return this.toAliasTable(field)+".cod_uo";
			}else{
				return "cod_uo";
			}
		}
		if(field.equals(VistaPagamento.model().VRS_ID_UO.ID_DOMINIO.COD_DOMINIO)){
			if(appendTablePrefix){
				return this.toAliasTable(field)+".cod_dominio";
			}else{
				return "cod_dominio";
			}
		}
		if(field.equals(VistaPagamento.model().VRS_ID_APPLICAZIONE.COD_APPLICAZIONE)){
			if(appendTablePrefix){
				return this.toAliasTable(field)+".cod_applicazione";
			}else{
				return "cod_applicazione";
			}
		}
		if(field.equals(VistaPagamento.model().VRS_TASSONOMIA)){
			if(appendTablePrefix){
				return this.toAliasTable(field)+".vrs_tassonomia";
			}else{
				return "vrs_tassonomia";
			}
		}
		if(field.equals(VistaPagamento.model().VRS_DIVISIONE)){
			if(appendTablePrefix){
				return this.toAliasTable(field)+".vrs_divisione";
			}else{
				return "vrs_divisione";
			}
		}
		if(field.equals(VistaPagamento.model().VRS_DIREZIONE)){
			if(appendTablePrefix){
				return this.toAliasTable(field)+".vrs_direzione";
			}else{
				return "vrs_direzione";
			}
		}
		if(field.equals(VistaPagamento.model().SNG_COD_SING_VERS_ENTE)){
			if(appendTablePrefix){
				return this.toAliasTable(field)+".sng_cod_sing_vers_ente";
			}else{
				return "sng_cod_sing_vers_ente";
			}
		}
		if(field.equals(VistaPagamento.model().RPT_IUV)){
			if(appendTablePrefix){
				return this.toAliasTable(field)+".rpt_iuv";
			}else{
				return "rpt_iuv";
			}
		}
		if(field.equals(VistaPagamento.model().RPT_CCP)){
			if(appendTablePrefix){
				return this.toAliasTable(field)+".rpt_ccp";
			}else{
				return "rpt_ccp";
			}
		}
		if(field.equals(VistaPagamento.model().RNC_TRN)){
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
		
		if(field.equals(VistaPagamento.model().COD_DOMINIO)){
			return this.toTable(VistaPagamento.model(), returnAlias);
		}
		if(field.equals(VistaPagamento.model().IUV)){
			return this.toTable(VistaPagamento.model(), returnAlias);
		}
		if(field.equals(VistaPagamento.model().INDICE_DATI)){
			return this.toTable(VistaPagamento.model(), returnAlias);
		}
		if(field.equals(VistaPagamento.model().IMPORTO_PAGATO)){
			return this.toTable(VistaPagamento.model(), returnAlias);
		}
		if(field.equals(VistaPagamento.model().DATA_ACQUISIZIONE)){
			return this.toTable(VistaPagamento.model(), returnAlias);
		}
		if(field.equals(VistaPagamento.model().IUR)){
			return this.toTable(VistaPagamento.model(), returnAlias);
		}
		if(field.equals(VistaPagamento.model().DATA_PAGAMENTO)){
			return this.toTable(VistaPagamento.model(), returnAlias);
		}
		if(field.equals(VistaPagamento.model().COMMISSIONI_PSP)){
			return this.toTable(VistaPagamento.model(), returnAlias);
		}
		if(field.equals(VistaPagamento.model().TIPO_ALLEGATO)){
			return this.toTable(VistaPagamento.model(), returnAlias);
		}
		if(field.equals(VistaPagamento.model().ALLEGATO)){
			return this.toTable(VistaPagamento.model(), returnAlias);
		}
		if(field.equals(VistaPagamento.model().DATA_ACQUISIZIONE_REVOCA)){
			return this.toTable(VistaPagamento.model(), returnAlias);
		}
		if(field.equals(VistaPagamento.model().CAUSALE_REVOCA)){
			return this.toTable(VistaPagamento.model(), returnAlias);
		}
		if(field.equals(VistaPagamento.model().DATI_REVOCA)){
			return this.toTable(VistaPagamento.model(), returnAlias);
		}
		if(field.equals(VistaPagamento.model().IMPORTO_REVOCATO)){
			return this.toTable(VistaPagamento.model(), returnAlias);
		}
		if(field.equals(VistaPagamento.model().ESITO_REVOCA)){
			return this.toTable(VistaPagamento.model(), returnAlias);
		}
		if(field.equals(VistaPagamento.model().DATI_ESITO_REVOCA)){
			return this.toTable(VistaPagamento.model(), returnAlias);
		}
		if(field.equals(VistaPagamento.model().STATO)){
			return this.toTable(VistaPagamento.model(), returnAlias);
		}
		if(field.equals(VistaPagamento.model().TIPO)){
			return this.toTable(VistaPagamento.model(), returnAlias);
		}
		if(field.equals(VistaPagamento.model().VRS_ID)){
			return this.toTable(VistaPagamento.model(), returnAlias);
		}
		if(field.equals(VistaPagamento.model().VRS_COD_VERSAMENTO_ENTE)){
			return this.toTable(VistaPagamento.model(), returnAlias);
		}
		if(field.equals(VistaPagamento.model().VRS_ID_TIPO_VERSAMENTO_DOMINIO.ID_DOMINIO.COD_DOMINIO)){
			return this.toTable(VistaPagamento.model().VRS_ID_TIPO_VERSAMENTO_DOMINIO.ID_DOMINIO, returnAlias);
		}
		if(field.equals(VistaPagamento.model().VRS_ID_TIPO_VERSAMENTO_DOMINIO.ID_TIPO_VERSAMENTO.COD_TIPO_VERSAMENTO)){
			return this.toTable(VistaPagamento.model().VRS_ID_TIPO_VERSAMENTO_DOMINIO.ID_TIPO_VERSAMENTO, returnAlias);
		}
		if(field.equals(VistaPagamento.model().VRS_ID_TIPO_VERSAMENTO.COD_TIPO_VERSAMENTO)){
			return this.toTable(VistaPagamento.model().VRS_ID_TIPO_VERSAMENTO, returnAlias);
		}
		if(field.equals(VistaPagamento.model().VRS_ID_DOMINIO.COD_DOMINIO)){
			return this.toTable(VistaPagamento.model().VRS_ID_DOMINIO, returnAlias);
		}
		if(field.equals(VistaPagamento.model().VRS_ID_UO.COD_UO)){
			return this.toTable(VistaPagamento.model().VRS_ID_UO, returnAlias);
		}
		if(field.equals(VistaPagamento.model().VRS_ID_UO.ID_DOMINIO.COD_DOMINIO)){
			return this.toTable(VistaPagamento.model().VRS_ID_UO.ID_DOMINIO, returnAlias);
		}
		if(field.equals(VistaPagamento.model().VRS_ID_APPLICAZIONE.COD_APPLICAZIONE)){
			return this.toTable(VistaPagamento.model().VRS_ID_APPLICAZIONE, returnAlias);
		}
		if(field.equals(VistaPagamento.model().VRS_TASSONOMIA)){
			return this.toTable(VistaPagamento.model(), returnAlias);
		}
		if(field.equals(VistaPagamento.model().VRS_DIVISIONE)){
			return this.toTable(VistaPagamento.model(), returnAlias);
		}
		if(field.equals(VistaPagamento.model().VRS_DIREZIONE)){
			return this.toTable(VistaPagamento.model(), returnAlias);
		}
		if(field.equals(VistaPagamento.model().SNG_COD_SING_VERS_ENTE)){
			return this.toTable(VistaPagamento.model(), returnAlias);
		}
		if(field.equals(VistaPagamento.model().RPT_IUV)){
			return this.toTable(VistaPagamento.model(), returnAlias);
		}
		if(field.equals(VistaPagamento.model().RPT_CCP)){
			return this.toTable(VistaPagamento.model(), returnAlias);
		}
		if(field.equals(VistaPagamento.model().RNC_TRN)){
			return this.toTable(VistaPagamento.model(), returnAlias);
		}


		return super.toTable(field,returnAlias);
		
	}

	@Override
	public String toTable(IModel<?> model,boolean returnAlias) throws ExpressionException {
		
		// In the case of table with alias, using parameter returnAlias​​, 
		// it is possible to drive the choice whether to return only the alias or 
		// the full definition of the table containing the alias
		
		if(model.equals(VistaPagamento.model())){
			return "v_pagamenti";
		}
		if(model.equals(VistaPagamento.model().VRS_ID_TIPO_VERSAMENTO_DOMINIO)){
			return "tipi_vers_domini";
		}
		if(model.equals(VistaPagamento.model().VRS_ID_TIPO_VERSAMENTO_DOMINIO.ID_DOMINIO)){
			return "domini";
		}
		if(model.equals(VistaPagamento.model().VRS_ID_TIPO_VERSAMENTO_DOMINIO.ID_TIPO_VERSAMENTO)){
			return "tipi_versamento";
		}
		if(model.equals(VistaPagamento.model().VRS_ID_TIPO_VERSAMENTO)){
			return "tipi_versamento";
		}
		if(model.equals(VistaPagamento.model().VRS_ID_DOMINIO)){
			return "domini";
		}
		if(model.equals(VistaPagamento.model().VRS_ID_UO)){
			return "uo";
		}
		if(model.equals(VistaPagamento.model().VRS_ID_UO.ID_DOMINIO)){
			return "domini";
		}
		if(model.equals(VistaPagamento.model().VRS_ID_APPLICAZIONE)){
			return "applicazioni";
		}


		return super.toTable(model,returnAlias);
		
	}

}
