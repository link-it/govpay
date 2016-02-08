/*
 * GovPay - Porta di Accesso al Nodo dei Pagamenti SPC 
 * http://www.gov4j.it/govpay
 * 
 * Copyright (c) 2014-2015 Link.it srl (http://www.link.it).
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

import it.govpay.orm.Dominio;


/**     
 * DominioFieldConverter
 *
 * @author Giovanni Bussu (bussu@link.it)
 * @author Lorenzo Nardi (nardi@link.it)
 * @author $Author$
 * @version $Rev$, $Date$
 */
public class DominioFieldConverter extends AbstractSQLFieldConverter {

	private TipiDatabase databaseType;
	
	public DominioFieldConverter(String databaseType){
		this.databaseType = TipiDatabase.toEnumConstant(databaseType);
	}
	public DominioFieldConverter(TipiDatabase databaseType){
		this.databaseType = databaseType;
	}


	@Override
	public IModel<?> getRootModel() throws ExpressionException {
		return Dominio.model();
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
		
		if(field.equals(Dominio.model().ID_STAZIONE.COD_STAZIONE)){
			if(appendTablePrefix){
				return this.toAliasTable(field)+".cod_stazione";
			}else{
				return "cod_stazione";
			}
		}
		if(field.equals(Dominio.model().COD_DOMINIO)){
			if(appendTablePrefix){
				return this.toAliasTable(field)+".cod_dominio";
			}else{
				return "cod_dominio";
			}
		}
		if(field.equals(Dominio.model().RAGIONE_SOCIALE)){
			if(appendTablePrefix){
				return this.toAliasTable(field)+".ragione_sociale";
			}else{
				return "ragione_sociale";
			}
		}
		if(field.equals(Dominio.model().GLN)){
			if(appendTablePrefix){
				return this.toAliasTable(field)+".gln";
			}else{
				return "gln";
			}
		}
		if(field.equals(Dominio.model().PLUGIN_CLASS)){
			if(appendTablePrefix){
				return this.toAliasTable(field)+".plugin_class";
			}else{
				return "plugin_class";
			}
		}
		if(field.equals(Dominio.model().ABILITATO)){
			if(appendTablePrefix){
				return this.toAliasTable(field)+".abilitato";
			}else{
				return "abilitato";
			}
		}
		if(field.equals(Dominio.model().DISPONIBILITA.TIPO_PERIODO)){
			if(appendTablePrefix){
				return this.toAliasTable(field)+".tipo_periodo";
			}else{
				return "tipo_periodo";
			}
		}
		if(field.equals(Dominio.model().DISPONIBILITA.GIORNO)){
			if(appendTablePrefix){
				return this.toAliasTable(field)+".giorno";
			}else{
				return "giorno";
			}
		}
		if(field.equals(Dominio.model().DISPONIBILITA.FASCE_ORARIE)){
			if(appendTablePrefix){
				return this.toAliasTable(field)+".fasce_orarie";
			}else{
				return "fasce_orarie";
			}
		}
		if(field.equals(Dominio.model().DISPONIBILITA.TIPO_DISPONIBILITA)){
			if(appendTablePrefix){
				return this.toAliasTable(field)+".tipo_disponibilita";
			}else{
				return "tipo_disponibilita";
			}
		}


		return super.toColumn(field,returnAlias,appendTablePrefix);
		
	}
	
	@Override
	public String toTable(IField field,boolean returnAlias) throws ExpressionException {
		
		// In the case of table with alias, using parameter returnAlias​​, 
		// it is possible to drive the choice whether to return only the alias or 
		// the full definition of the table containing the alias
		
		if(field.equals(Dominio.model().ID_STAZIONE.COD_STAZIONE)){
			return this.toTable(Dominio.model().ID_STAZIONE, returnAlias);
		}
		if(field.equals(Dominio.model().COD_DOMINIO)){
			return this.toTable(Dominio.model(), returnAlias);
		}
		if(field.equals(Dominio.model().RAGIONE_SOCIALE)){
			return this.toTable(Dominio.model(), returnAlias);
		}
		if(field.equals(Dominio.model().GLN)){
			return this.toTable(Dominio.model(), returnAlias);
		}
		if(field.equals(Dominio.model().PLUGIN_CLASS)){
			return this.toTable(Dominio.model(), returnAlias);
		}
		if(field.equals(Dominio.model().ABILITATO)){
			return this.toTable(Dominio.model(), returnAlias);
		}
		if(field.equals(Dominio.model().DISPONIBILITA.TIPO_PERIODO)){
			return this.toTable(Dominio.model().DISPONIBILITA, returnAlias);
		}
		if(field.equals(Dominio.model().DISPONIBILITA.GIORNO)){
			return this.toTable(Dominio.model().DISPONIBILITA, returnAlias);
		}
		if(field.equals(Dominio.model().DISPONIBILITA.FASCE_ORARIE)){
			return this.toTable(Dominio.model().DISPONIBILITA, returnAlias);
		}
		if(field.equals(Dominio.model().DISPONIBILITA.TIPO_DISPONIBILITA)){
			return this.toTable(Dominio.model().DISPONIBILITA, returnAlias);
		}


		return super.toTable(field,returnAlias);
		
	}

	@Override
	public String toTable(IModel<?> model,boolean returnAlias) throws ExpressionException {
		
		// In the case of table with alias, using parameter returnAlias​​, 
		// it is possible to drive the choice whether to return only the alias or 
		// the full definition of the table containing the alias
		
		if(model.equals(Dominio.model())){
			return "domini";
		}
		if(model.equals(Dominio.model().ID_STAZIONE)){
			return "stazioni";
		}
		if(model.equals(Dominio.model().DISPONIBILITA)){
			return "disponibilita";
		}


		return super.toTable(model,returnAlias);
		
	}

}
