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

import it.govpay.orm.VistaRiscossioni;


/**     
 * VistaRiscossioniFieldConverter
 *
 * @author Giovanni Bussu (bussu@link.it)
 * @author Lorenzo Nardi (nardi@link.it)
 * @author $Author$
 * @version $Rev$, $Date$
 */
public class VistaRiscossioniFieldConverter extends AbstractSQLFieldConverter {

	private TipiDatabase databaseType;
	
	public VistaRiscossioniFieldConverter(String databaseType){
		this.databaseType = TipiDatabase.toEnumConstant(databaseType);
	}
	public VistaRiscossioniFieldConverter(TipiDatabase databaseType){
		this.databaseType = databaseType;
	}


	@Override
	public IModel<?> getRootModel() throws ExpressionException {
		return VistaRiscossioni.model();
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
		
		if(field.equals(VistaRiscossioni.model().COD_DOMINIO)){
			if(appendTablePrefix){
				return this.toAliasTable(field)+".cod_dominio";
			}else{
				return "cod_dominio";
			}
		}
		if(field.equals(VistaRiscossioni.model().IUV)){
			if(appendTablePrefix){
				return this.toAliasTable(field)+".iuv";
			}else{
				return "iuv";
			}
		}
		if(field.equals(VistaRiscossioni.model().IUR)){
			if(appendTablePrefix){
				return this.toAliasTable(field)+".iur";
			}else{
				return "iur";
			}
		}
		if(field.equals(VistaRiscossioni.model().COD_FLUSSO)){
			if(appendTablePrefix){
				return this.toAliasTable(field)+".cod_flusso";
			}else{
				return "cod_flusso";
			}
		}
		if(field.equals(VistaRiscossioni.model().FR_IUR)){
			if(appendTablePrefix){
				return this.toAliasTable(field)+".fr_iur";
			}else{
				return "fr_iur";
			}
		}
		if(field.equals(VistaRiscossioni.model().DATA_REGOLAMENTO)){
			if(appendTablePrefix){
				return this.toAliasTable(field)+".data_regolamento";
			}else{
				return "data_regolamento";
			}
		}
		if(field.equals(VistaRiscossioni.model().NUMERO_PAGAMENTI)){
			if(appendTablePrefix){
				return this.toAliasTable(field)+".numero_pagamenti";
			}else{
				return "numero_pagamenti";
			}
		}
		if(field.equals(VistaRiscossioni.model().IMPORTO_TOTALE_PAGAMENTI)){
			if(appendTablePrefix){
				return this.toAliasTable(field)+".importo_totale_pagamenti";
			}else{
				return "importo_totale_pagamenti";
			}
		}
		if(field.equals(VistaRiscossioni.model().IMPORTO_PAGATO)){
			if(appendTablePrefix){
				return this.toAliasTable(field)+".importo_pagato";
			}else{
				return "importo_pagato";
			}
		}
		if(field.equals(VistaRiscossioni.model().COD_SINGOLO_VERSAMENTO_ENTE)){
			if(appendTablePrefix){
				return this.toAliasTable(field)+".cod_singolo_versamento_ente";
			}else{
				return "cod_singolo_versamento_ente";
			}
		}
		if(field.equals(VistaRiscossioni.model().INDICE_DATI)){
			if(appendTablePrefix){
				return this.toAliasTable(field)+".indice_dati";
			}else{
				return "indice_dati";
			}
		}
		if(field.equals(VistaRiscossioni.model().COD_VERSAMENTO_ENTE)){
			if(appendTablePrefix){
				return this.toAliasTable(field)+".cod_versamento_ente";
			}else{
				return "cod_versamento_ente";
			}
		}
		if(field.equals(VistaRiscossioni.model().COD_APPLICAZIONE)){
			if(appendTablePrefix){
				return this.toAliasTable(field)+".cod_applicazione";
			}else{
				return "cod_applicazione";
			}
		}
		if(field.equals(VistaRiscossioni.model().DATA_PAGAMENTO)){
			if(appendTablePrefix){
				return this.toAliasTable(field)+".data_pagamento";
			}else{
				return "data_pagamento";
			}
		}
		if(field.equals(VistaRiscossioni.model().COD_TIPO_VERSAMENTO)){
			if(appendTablePrefix){
				return this.toAliasTable(field)+".cod_tipo_versamento";
			}else{
				return "cod_tipo_versamento";
			}
		}
		if(field.equals(VistaRiscossioni.model().COD_ENTRATA)){
			if(appendTablePrefix){
				return this.toAliasTable(field)+".cod_entrata";
			}else{
				return "cod_entrata";
			}
		}
		if(field.equals(VistaRiscossioni.model().IDENTIFICATIVO_DEBITORE)){
			if(appendTablePrefix){
				return this.toAliasTable(field)+".identificativo_debitore";
			}else{
				return "identificativo_debitore";
			}
		}
		if(field.equals(VistaRiscossioni.model().DEBITORE_ANAGRAFICA)){
			if(appendTablePrefix){
				return this.toAliasTable(field)+".debitore_anagrafica";
			}else{
				return "debitore_anagrafica";
			}
		}
		if(field.equals(VistaRiscossioni.model().ANNO)){
			if(appendTablePrefix){
				return this.toAliasTable(field)+".anno";
			}else{
				return "anno";
			}
		}
		if(field.equals(VistaRiscossioni.model().DESCR_TIPO_VERSAMENTO)){
			if(appendTablePrefix){
				return this.toAliasTable(field)+".descr_tipo_versamento";
			}else{
				return "descr_tipo_versamento";
			}
		}
		if(field.equals(VistaRiscossioni.model().COD_PSP)){
			if(appendTablePrefix){
				return this.toAliasTable(field)+".cod_psp";
			}else{
				return "cod_psp";
			}
		}
		if(field.equals(VistaRiscossioni.model().RAGIONE_SOCIALE_PSP)){
			if(appendTablePrefix){
				return this.toAliasTable(field)+".ragione_sociale_psp";
			}else{
				return "ragione_sociale_psp";
			}
		}
		if(field.equals(VistaRiscossioni.model().COD_RATA)){
			if(appendTablePrefix){
				return this.toAliasTable(field)+".cod_rata";
			}else{
				return "cod_rata";
			}
		}
		if(field.equals(VistaRiscossioni.model().ID_DOCUMENTO.COD_DOCUMENTO)){
			if(appendTablePrefix){
				return this.toAliasTable(field)+".cod_documento";
			}else{
				return "cod_documento";
			}
		}
		if(field.equals(VistaRiscossioni.model().ID_DOCUMENTO.ID_APPLICAZIONE.COD_APPLICAZIONE)){
			if(appendTablePrefix){
				return this.toAliasTable(field)+".cod_applicazione";
			}else{
				return "cod_applicazione";
			}
		}
		if(field.equals(VistaRiscossioni.model().ID_DOCUMENTO.ID_DOMINIO.COD_DOMINIO)){
			if(appendTablePrefix){
				return this.toAliasTable(field)+".cod_dominio";
			}else{
				return "cod_dominio";
			}
		}
		if(field.equals(VistaRiscossioni.model().CAUSALE_VERSAMENTO)){
			if(appendTablePrefix){
				return this.toAliasTable(field)+".causale_versamento";
			}else{
				return "causale_versamento";
			}
		}
		if(field.equals(VistaRiscossioni.model().IMPORTO_VERSAMENTO)){
			if(appendTablePrefix){
				return this.toAliasTable(field)+".importo_versamento";
			}else{
				return "importo_versamento";
			}
		}
		if(field.equals(VistaRiscossioni.model().NUMERO_AVVISO)){
			if(appendTablePrefix){
				return this.toAliasTable(field)+".numero_avviso";
			}else{
				return "numero_avviso";
			}
		}
		if(field.equals(VistaRiscossioni.model().IUV_PAGAMENTO)){
			if(appendTablePrefix){
				return this.toAliasTable(field)+".iuv_pagamento";
			}else{
				return "iuv_pagamento";
			}
		}
		if(field.equals(VistaRiscossioni.model().DATA_SCADENZA)){
			if(appendTablePrefix){
				return this.toAliasTable(field)+".data_scadenza";
			}else{
				return "data_scadenza";
			}
		}
		if(field.equals(VistaRiscossioni.model().CONTABILITA)){
			if(appendTablePrefix){
				return this.toAliasTable(field)+".contabilita";
			}else{
				return "contabilita";
			}
		}


		return super.toColumn(field,returnAlias,appendTablePrefix);
		
	}
	
	@Override
	public String toTable(IField field,boolean returnAlias) throws ExpressionException {
		
		// In the case of table with alias, using parameter returnAlias​​, 
		// it is possible to drive the choice whether to return only the alias or 
		// the full definition of the table containing the alias
		
		if(field.equals(VistaRiscossioni.model().COD_DOMINIO)){
			return this.toTable(VistaRiscossioni.model(), returnAlias);
		}
		if(field.equals(VistaRiscossioni.model().IUV)){
			return this.toTable(VistaRiscossioni.model(), returnAlias);
		}
		if(field.equals(VistaRiscossioni.model().IUR)){
			return this.toTable(VistaRiscossioni.model(), returnAlias);
		}
		if(field.equals(VistaRiscossioni.model().COD_FLUSSO)){
			return this.toTable(VistaRiscossioni.model(), returnAlias);
		}
		if(field.equals(VistaRiscossioni.model().FR_IUR)){
			return this.toTable(VistaRiscossioni.model(), returnAlias);
		}
		if(field.equals(VistaRiscossioni.model().DATA_REGOLAMENTO)){
			return this.toTable(VistaRiscossioni.model(), returnAlias);
		}
		if(field.equals(VistaRiscossioni.model().NUMERO_PAGAMENTI)){
			return this.toTable(VistaRiscossioni.model(), returnAlias);
		}
		if(field.equals(VistaRiscossioni.model().IMPORTO_TOTALE_PAGAMENTI)){
			return this.toTable(VistaRiscossioni.model(), returnAlias);
		}
		if(field.equals(VistaRiscossioni.model().IMPORTO_PAGATO)){
			return this.toTable(VistaRiscossioni.model(), returnAlias);
		}
		if(field.equals(VistaRiscossioni.model().COD_SINGOLO_VERSAMENTO_ENTE)){
			return this.toTable(VistaRiscossioni.model(), returnAlias);
		}
		if(field.equals(VistaRiscossioni.model().INDICE_DATI)){
			return this.toTable(VistaRiscossioni.model(), returnAlias);
		}
		if(field.equals(VistaRiscossioni.model().COD_VERSAMENTO_ENTE)){
			return this.toTable(VistaRiscossioni.model(), returnAlias);
		}
		if(field.equals(VistaRiscossioni.model().COD_APPLICAZIONE)){
			return this.toTable(VistaRiscossioni.model(), returnAlias);
		}
		if(field.equals(VistaRiscossioni.model().DATA_PAGAMENTO)){
			return this.toTable(VistaRiscossioni.model(), returnAlias);
		}
		if(field.equals(VistaRiscossioni.model().COD_TIPO_VERSAMENTO)){
			return this.toTable(VistaRiscossioni.model(), returnAlias);
		}
		if(field.equals(VistaRiscossioni.model().COD_ENTRATA)){
			return this.toTable(VistaRiscossioni.model(), returnAlias);
		}
		if(field.equals(VistaRiscossioni.model().IDENTIFICATIVO_DEBITORE)){
			return this.toTable(VistaRiscossioni.model(), returnAlias);
		}
		if(field.equals(VistaRiscossioni.model().DEBITORE_ANAGRAFICA)){
			return this.toTable(VistaRiscossioni.model(), returnAlias);
		}
		if(field.equals(VistaRiscossioni.model().ANNO)){
			return this.toTable(VistaRiscossioni.model(), returnAlias);
		}
		if(field.equals(VistaRiscossioni.model().DESCR_TIPO_VERSAMENTO)){
			return this.toTable(VistaRiscossioni.model(), returnAlias);
		}
		if(field.equals(VistaRiscossioni.model().COD_PSP)){
			return this.toTable(VistaRiscossioni.model(), returnAlias);
		}
		if(field.equals(VistaRiscossioni.model().RAGIONE_SOCIALE_PSP)){
			return this.toTable(VistaRiscossioni.model(), returnAlias);
		}
		if(field.equals(VistaRiscossioni.model().COD_RATA)){
			return this.toTable(VistaRiscossioni.model(), returnAlias);
		}
		if(field.equals(VistaRiscossioni.model().ID_DOCUMENTO.COD_DOCUMENTO)){
			return this.toTable(VistaRiscossioni.model().ID_DOCUMENTO, returnAlias);
		}
		if(field.equals(VistaRiscossioni.model().ID_DOCUMENTO.ID_APPLICAZIONE.COD_APPLICAZIONE)){
			return this.toTable(VistaRiscossioni.model().ID_DOCUMENTO.ID_APPLICAZIONE, returnAlias);
		}
		if(field.equals(VistaRiscossioni.model().ID_DOCUMENTO.ID_DOMINIO.COD_DOMINIO)){
			return this.toTable(VistaRiscossioni.model().ID_DOCUMENTO.ID_DOMINIO, returnAlias);
		}
		if(field.equals(VistaRiscossioni.model().CAUSALE_VERSAMENTO)){
			return this.toTable(VistaRiscossioni.model(), returnAlias);
		}
		if(field.equals(VistaRiscossioni.model().IMPORTO_VERSAMENTO)){
			return this.toTable(VistaRiscossioni.model(), returnAlias);
		}
		if(field.equals(VistaRiscossioni.model().NUMERO_AVVISO)){
			return this.toTable(VistaRiscossioni.model(), returnAlias);
		}
		if(field.equals(VistaRiscossioni.model().IUV_PAGAMENTO)){
			return this.toTable(VistaRiscossioni.model(), returnAlias);
		}
		if(field.equals(VistaRiscossioni.model().DATA_SCADENZA)){
			return this.toTable(VistaRiscossioni.model(), returnAlias);
		}
		if(field.equals(VistaRiscossioni.model().CONTABILITA)){
			return this.toTable(VistaRiscossioni.model(), returnAlias);
		}


		return super.toTable(field,returnAlias);
		
	}

	@Override
	public String toTable(IModel<?> model,boolean returnAlias) throws ExpressionException {
		
		// In the case of table with alias, using parameter returnAlias​​, 
		// it is possible to drive the choice whether to return only the alias or 
		// the full definition of the table containing the alias
		
		if(model.equals(VistaRiscossioni.model())){
			return "v_riscossioni";
		}
		if(model.equals(VistaRiscossioni.model().ID_DOCUMENTO)){
			return "documenti";
		}
		if(model.equals(VistaRiscossioni.model().ID_DOCUMENTO.ID_APPLICAZIONE)){
			return "applicazioni";
		}
		if(model.equals(VistaRiscossioni.model().ID_DOCUMENTO.ID_DOMINIO)){
			return "domini";
		}


		return super.toTable(model,returnAlias);
		
	}

}
