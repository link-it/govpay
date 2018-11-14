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
		if(field.equals(VistaRiscossioni.model().DATA)){
			if(appendTablePrefix){
				return this.toAliasTable(field)+".data";
			}else{
				return "data";
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
		if(field.equals(VistaRiscossioni.model().DATA)){
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


		return super.toTable(model,returnAlias);
		
	}

}
