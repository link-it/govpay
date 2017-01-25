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

import it.govpay.orm.FrFiltroApp;


/**     
 * FrFiltroAppFieldConverter
 *
 * @author Giovanni Bussu (bussu@link.it)
 * @author Lorenzo Nardi (nardi@link.it)
 * @author $Author$
 * @version $Rev$, $Date$
 */
public class FrFiltroAppFieldConverter extends AbstractSQLFieldConverter {

	private TipiDatabase databaseType;
	
	public FrFiltroAppFieldConverter(String databaseType){
		this.databaseType = TipiDatabase.toEnumConstant(databaseType);
	}
	public FrFiltroAppFieldConverter(TipiDatabase databaseType){
		this.databaseType = databaseType;
	}


	@Override
	public IModel<?> getRootModel() throws ExpressionException {
		return FrFiltroApp.model();
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
		
		if(field.equals(FrFiltroApp.model().FR.ID_PSP.COD_PSP)){
			if(appendTablePrefix){
				return this.toAliasTable(field)+".cod_psp";
			}else{
				return "cod_psp";
			}
		}
		if(field.equals(FrFiltroApp.model().FR.ID_DOMINIO.COD_DOMINIO)){
			if(appendTablePrefix){
				return this.toAliasTable(field)+".cod_dominio";
			}else{
				return "cod_dominio";
			}
		}
		if(field.equals(FrFiltroApp.model().FR.COD_FLUSSO)){
			if(appendTablePrefix){
				return this.toAliasTable(field)+".cod_flusso";
			}else{
				return "cod_flusso";
			}
		}
		if(field.equals(FrFiltroApp.model().FR.STATO)){
			if(appendTablePrefix){
				return this.toAliasTable(field)+".stato";
			}else{
				return "stato";
			}
		}
		if(field.equals(FrFiltroApp.model().FR.DESCRIZIONE_STATO)){
			if(appendTablePrefix){
				return this.toAliasTable(field)+".descrizione_stato";
			}else{
				return "descrizione_stato";
			}
		}
		if(field.equals(FrFiltroApp.model().FR.IUR)){
			if(appendTablePrefix){
				return this.toAliasTable(field)+".iur";
			}else{
				return "iur";
			}
		}
		if(field.equals(FrFiltroApp.model().FR.ANNO_RIFERIMENTO)){
			if(appendTablePrefix){
				return this.toAliasTable(field)+".anno_riferimento";
			}else{
				return "anno_riferimento";
			}
		}
		if(field.equals(FrFiltroApp.model().FR.DATA_ORA_FLUSSO)){
			if(appendTablePrefix){
				return this.toAliasTable(field)+".data_ora_flusso";
			}else{
				return "data_ora_flusso";
			}
		}
		if(field.equals(FrFiltroApp.model().FR.DATA_REGOLAMENTO)){
			if(appendTablePrefix){
				return this.toAliasTable(field)+".data_regolamento";
			}else{
				return "data_regolamento";
			}
		}
		if(field.equals(FrFiltroApp.model().FR.DATA_ACQUISIZIONE)){
			if(appendTablePrefix){
				return this.toAliasTable(field)+".data_acquisizione";
			}else{
				return "data_acquisizione";
			}
		}
		if(field.equals(FrFiltroApp.model().FR.NUMERO_PAGAMENTI)){
			if(appendTablePrefix){
				return this.toAliasTable(field)+".numero_pagamenti";
			}else{
				return "numero_pagamenti";
			}
		}
		if(field.equals(FrFiltroApp.model().FR.IMPORTO_TOTALE_PAGAMENTI)){
			if(appendTablePrefix){
				return this.toAliasTable(field)+".importo_totale_pagamenti";
			}else{
				return "importo_totale_pagamenti";
			}
		}
		if(field.equals(FrFiltroApp.model().FR.COD_BIC_RIVERSAMENTO)){
			if(appendTablePrefix){
				return this.toAliasTable(field)+".cod_bic_riversamento";
			}else{
				return "cod_bic_riversamento";
			}
		}
		if(field.equals(FrFiltroApp.model().FR.XML)){
			if(appendTablePrefix){
				return this.toAliasTable(field)+".xml";
			}else{
				return "xml";
			}
		}
		if(field.equals(FrFiltroApp.model().FR_APPLICAZIONE.ID_APPLICAZIONE.COD_APPLICAZIONE)){
			if(appendTablePrefix){
				return this.toAliasTable(field)+".cod_applicazione";
			}else{
				return "cod_applicazione";
			}
		}
		if(field.equals(FrFiltroApp.model().FR_APPLICAZIONE.ID_FR.COD_FLUSSO)){
			if(appendTablePrefix){
				return this.toAliasTable(field)+".cod_flusso";
			}else{
				return "cod_flusso";
			}
		}
		if(field.equals(FrFiltroApp.model().FR_APPLICAZIONE.ID_FR.ANNO_RIFERIMENTO)){
			if(appendTablePrefix){
				return this.toAliasTable(field)+".anno_riferimento";
			}else{
				return "anno_riferimento";
			}
		}
		if(field.equals(FrFiltroApp.model().FR_APPLICAZIONE.NUMERO_PAGAMENTI)){
			if(appendTablePrefix){
				return this.toAliasTable(field)+".numero_pagamenti";
			}else{
				return "numero_pagamenti";
			}
		}
		if(field.equals(FrFiltroApp.model().FR_APPLICAZIONE.IMPORTO_TOTALE_PAGAMENTI)){
			if(appendTablePrefix){
				return this.toAliasTable(field)+".importo_totale_pagamenti";
			}else{
				return "importo_totale_pagamenti";
			}
		}


		return super.toColumn(field,returnAlias,appendTablePrefix);
		
	}
	
	@Override
	public String toTable(IField field,boolean returnAlias) throws ExpressionException {
		
		// In the case of table with alias, using parameter returnAlias​​, 
		// it is possible to drive the choice whether to return only the alias or 
		// the full definition of the table containing the alias
		
		if(field.equals(FrFiltroApp.model().FR.ID_PSP.COD_PSP)){
			return this.toTable(FrFiltroApp.model().FR.ID_PSP, returnAlias);
		}
		if(field.equals(FrFiltroApp.model().FR.ID_DOMINIO.COD_DOMINIO)){
			return this.toTable(FrFiltroApp.model().FR.ID_DOMINIO, returnAlias);
		}
		if(field.equals(FrFiltroApp.model().FR.COD_FLUSSO)){
			return this.toTable(FrFiltroApp.model().FR, returnAlias);
		}
		if(field.equals(FrFiltroApp.model().FR.STATO)){
			return this.toTable(FrFiltroApp.model().FR, returnAlias);
		}
		if(field.equals(FrFiltroApp.model().FR.DESCRIZIONE_STATO)){
			return this.toTable(FrFiltroApp.model().FR, returnAlias);
		}
		if(field.equals(FrFiltroApp.model().FR.IUR)){
			return this.toTable(FrFiltroApp.model().FR, returnAlias);
		}
		if(field.equals(FrFiltroApp.model().FR.ANNO_RIFERIMENTO)){
			return this.toTable(FrFiltroApp.model().FR, returnAlias);
		}
		if(field.equals(FrFiltroApp.model().FR.DATA_ORA_FLUSSO)){
			return this.toTable(FrFiltroApp.model().FR, returnAlias);
		}
		if(field.equals(FrFiltroApp.model().FR.DATA_REGOLAMENTO)){
			return this.toTable(FrFiltroApp.model().FR, returnAlias);
		}
		if(field.equals(FrFiltroApp.model().FR.DATA_ACQUISIZIONE)){
			return this.toTable(FrFiltroApp.model().FR, returnAlias);
		}
		if(field.equals(FrFiltroApp.model().FR.NUMERO_PAGAMENTI)){
			return this.toTable(FrFiltroApp.model().FR, returnAlias);
		}
		if(field.equals(FrFiltroApp.model().FR.IMPORTO_TOTALE_PAGAMENTI)){
			return this.toTable(FrFiltroApp.model().FR, returnAlias);
		}
		if(field.equals(FrFiltroApp.model().FR.COD_BIC_RIVERSAMENTO)){
			return this.toTable(FrFiltroApp.model().FR, returnAlias);
		}
		if(field.equals(FrFiltroApp.model().FR.XML)){
			return this.toTable(FrFiltroApp.model().FR, returnAlias);
		}
		if(field.equals(FrFiltroApp.model().FR_APPLICAZIONE.ID_APPLICAZIONE.COD_APPLICAZIONE)){
			return this.toTable(FrFiltroApp.model().FR_APPLICAZIONE.ID_APPLICAZIONE, returnAlias);
		}
		if(field.equals(FrFiltroApp.model().FR_APPLICAZIONE.ID_FR.COD_FLUSSO)){
			return this.toTable(FrFiltroApp.model().FR_APPLICAZIONE.ID_FR, returnAlias);
		}
		if(field.equals(FrFiltroApp.model().FR_APPLICAZIONE.ID_FR.ANNO_RIFERIMENTO)){
			return this.toTable(FrFiltroApp.model().FR_APPLICAZIONE.ID_FR, returnAlias);
		}
		if(field.equals(FrFiltroApp.model().FR_APPLICAZIONE.NUMERO_PAGAMENTI)){
			return this.toTable(FrFiltroApp.model().FR_APPLICAZIONE, returnAlias);
		}
		if(field.equals(FrFiltroApp.model().FR_APPLICAZIONE.IMPORTO_TOTALE_PAGAMENTI)){
			return this.toTable(FrFiltroApp.model().FR_APPLICAZIONE, returnAlias);
		}


		return super.toTable(field,returnAlias);
		
	}

	@Override
	public String toTable(IModel<?> model,boolean returnAlias) throws ExpressionException {
		
		// In the case of table with alias, using parameter returnAlias​​, 
		// it is possible to drive the choice whether to return only the alias or 
		// the full definition of the table containing the alias
		
		if(model.equals(FrFiltroApp.model().FR)){
			return "fr";
		}
		if(model.equals(FrFiltroApp.model().FR.ID_PSP)){
			return "psp";
		}
		if(model.equals(FrFiltroApp.model().FR.ID_DOMINIO)){
			return "domini";
		}
		if(model.equals(FrFiltroApp.model().FR_APPLICAZIONE)){
			return "fr_applicazioni";
		}
		if(model.equals(FrFiltroApp.model().FR_APPLICAZIONE.ID_APPLICAZIONE)){
			return "applicazioni";
		}
		if(model.equals(FrFiltroApp.model().FR_APPLICAZIONE.ID_FR)){
			return "fr";
		}


		return super.toTable(model,returnAlias);
		
	}

}
