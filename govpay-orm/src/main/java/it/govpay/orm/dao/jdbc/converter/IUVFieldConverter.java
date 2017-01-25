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

import it.govpay.orm.IUV;


/**     
 * IUVFieldConverter
 *
 * @author Giovanni Bussu (bussu@link.it)
 * @author Lorenzo Nardi (nardi@link.it)
 * @author $Author$
 * @version $Rev$, $Date$
 */
public class IUVFieldConverter extends AbstractSQLFieldConverter {

	private TipiDatabase databaseType;
	
	public IUVFieldConverter(String databaseType){
		this.databaseType = TipiDatabase.toEnumConstant(databaseType);
	}
	public IUVFieldConverter(TipiDatabase databaseType){
		this.databaseType = databaseType;
	}


	@Override
	public IModel<?> getRootModel() throws ExpressionException {
		return IUV.model();
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
		
		if(field.equals(IUV.model().PRG)){
			if(appendTablePrefix){
				return this.toAliasTable(field)+".prg";
			}else{
				return "prg";
			}
		}
		if(field.equals(IUV.model().IUV)){
			if(appendTablePrefix){
				return this.toAliasTable(field)+".iuv";
			}else{
				return "iuv";
			}
		}
		if(field.equals(IUV.model().APPLICATION_CODE)){
			if(appendTablePrefix){
				return this.toAliasTable(field)+".application_code";
			}else{
				return "application_code";
			}
		}
		if(field.equals(IUV.model().DATA_GENERAZIONE)){
			if(appendTablePrefix){
				return this.toAliasTable(field)+".data_generazione";
			}else{
				return "data_generazione";
			}
		}
		if(field.equals(IUV.model().ID_APPLICAZIONE.COD_APPLICAZIONE)){
			if(appendTablePrefix){
				return this.toAliasTable(field)+".cod_applicazione";
			}else{
				return "cod_applicazione";
			}
		}
		if(field.equals(IUV.model().TIPO_IUV)){
			if(appendTablePrefix){
				return this.toAliasTable(field)+".tipo_iuv";
			}else{
				return "tipo_iuv";
			}
		}
		if(field.equals(IUV.model().ID_DOMINIO.COD_DOMINIO)){
			if(appendTablePrefix){
				return this.toAliasTable(field)+".cod_dominio";
			}else{
				return "cod_dominio";
			}
		}
		if(field.equals(IUV.model().COD_VERSAMENTO_ENTE)){
			if(appendTablePrefix){
				return this.toAliasTable(field)+".cod_versamento_ente";
			}else{
				return "cod_versamento_ente";
			}
		}


		return super.toColumn(field,returnAlias,appendTablePrefix);
		
	}
	
	@Override
	public String toTable(IField field,boolean returnAlias) throws ExpressionException {
		
		// In the case of table with alias, using parameter returnAlias​​, 
		// it is possible to drive the choice whether to return only the alias or 
		// the full definition of the table containing the alias
		
		if(field.equals(IUV.model().PRG)){
			return this.toTable(IUV.model(), returnAlias);
		}
		if(field.equals(IUV.model().IUV)){
			return this.toTable(IUV.model(), returnAlias);
		}
		if(field.equals(IUV.model().APPLICATION_CODE)){
			return this.toTable(IUV.model(), returnAlias);
		}
		if(field.equals(IUV.model().DATA_GENERAZIONE)){
			return this.toTable(IUV.model(), returnAlias);
		}
		if(field.equals(IUV.model().ID_APPLICAZIONE.COD_APPLICAZIONE)){
			return this.toTable(IUV.model().ID_APPLICAZIONE, returnAlias);
		}
		if(field.equals(IUV.model().TIPO_IUV)){
			return this.toTable(IUV.model(), returnAlias);
		}
		if(field.equals(IUV.model().ID_DOMINIO.COD_DOMINIO)){
			return this.toTable(IUV.model().ID_DOMINIO, returnAlias);
		}
		if(field.equals(IUV.model().COD_VERSAMENTO_ENTE)){
			return this.toTable(IUV.model(), returnAlias);
		}


		return super.toTable(field,returnAlias);
		
	}

	@Override
	public String toTable(IModel<?> model,boolean returnAlias) throws ExpressionException {
		
		// In the case of table with alias, using parameter returnAlias​​, 
		// it is possible to drive the choice whether to return only the alias or 
		// the full definition of the table containing the alias
		
		if(model.equals(IUV.model())){
			return "iuv";
		}
		if(model.equals(IUV.model().ID_APPLICAZIONE)){
			return "applicazioni";
		}
		if(model.equals(IUV.model().ID_DOMINIO)){
			return "domini";
		}


		return super.toTable(model,returnAlias);
		
	}

}
