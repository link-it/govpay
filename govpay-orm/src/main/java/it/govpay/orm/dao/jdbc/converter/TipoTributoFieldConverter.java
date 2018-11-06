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

import it.govpay.orm.TipoTributo;


/**     
 * TipoTributoFieldConverter
 *
 * @author Giovanni Bussu (bussu@link.it)
 * @author Lorenzo Nardi (nardi@link.it)
 * @author $Author$
 * @version $Rev$, $Date$
 */
public class TipoTributoFieldConverter extends AbstractSQLFieldConverter {

	private TipiDatabase databaseType;
	
	public TipoTributoFieldConverter(String databaseType){
		this.databaseType = TipiDatabase.toEnumConstant(databaseType);
	}
	public TipoTributoFieldConverter(TipiDatabase databaseType){
		this.databaseType = databaseType;
	}


	@Override
	public IModel<?> getRootModel() throws ExpressionException {
		return TipoTributo.model();
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
		
		if(field.equals(TipoTributo.model().COD_TRIBUTO)){
			if(appendTablePrefix){
				return this.toAliasTable(field)+".cod_tributo";
			}else{
				return "cod_tributo";
			}
		}
		if(field.equals(TipoTributo.model().DESCRIZIONE)){
			if(appendTablePrefix){
				return this.toAliasTable(field)+".descrizione";
			}else{
				return "descrizione";
			}
		}
		if(field.equals(TipoTributo.model().TIPO_CONTABILITA)){
			if(appendTablePrefix){
				return this.toAliasTable(field)+".tipo_contabilita";
			}else{
				return "tipo_contabilita";
			}
		}
		if(field.equals(TipoTributo.model().COD_CONTABILITA)){
			if(appendTablePrefix){
				return this.toAliasTable(field)+".cod_contabilita";
			}else{
				return "cod_contabilita";
			}
		}
		if(field.equals(TipoTributo.model().COD_TRIBUTO_IUV)){
			if(appendTablePrefix){
				return this.toAliasTable(field)+".cod_tributo_iuv";
			}else{
				return "cod_tributo_iuv";
			}
		}
		if(field.equals(TipoTributo.model().ONLINE)){
			if(appendTablePrefix){
				return this.toAliasTable(field)+".online";
			}else{
				return "online";
			}
		}
		if(field.equals(TipoTributo.model().PAGA_TERZI)){
			if(appendTablePrefix){
				return this.toAliasTable(field)+".paga_terzi";
			}else{
				return "paga_terzi";
			}
		}


		return super.toColumn(field,returnAlias,appendTablePrefix);
		
	}
	
	@Override
	public String toTable(IField field,boolean returnAlias) throws ExpressionException {
		
		// In the case of table with alias, using parameter returnAlias​​, 
		// it is possible to drive the choice whether to return only the alias or 
		// the full definition of the table containing the alias
		
		if(field.equals(TipoTributo.model().COD_TRIBUTO)){
			return this.toTable(TipoTributo.model(), returnAlias);
		}
		if(field.equals(TipoTributo.model().DESCRIZIONE)){
			return this.toTable(TipoTributo.model(), returnAlias);
		}
		if(field.equals(TipoTributo.model().TIPO_CONTABILITA)){
			return this.toTable(TipoTributo.model(), returnAlias);
		}
		if(field.equals(TipoTributo.model().COD_CONTABILITA)){
			return this.toTable(TipoTributo.model(), returnAlias);
		}
		if(field.equals(TipoTributo.model().COD_TRIBUTO_IUV)){
			return this.toTable(TipoTributo.model(), returnAlias);
		}
		if(field.equals(TipoTributo.model().ONLINE)){
			return this.toTable(TipoTributo.model(), returnAlias);
		}
		if(field.equals(TipoTributo.model().PAGA_TERZI)){
			return this.toTable(TipoTributo.model(), returnAlias);
		}


		return super.toTable(field,returnAlias);
		
	}

	@Override
	public String toTable(IModel<?> model,boolean returnAlias) throws ExpressionException {
		
		// In the case of table with alias, using parameter returnAlias​​, 
		// it is possible to drive the choice whether to return only the alias or 
		// the full definition of the table containing the alias
		
		if(model.equals(TipoTributo.model())){
			return "tipi_tributo";
		}


		return super.toTable(model,returnAlias);
		
	}

}
