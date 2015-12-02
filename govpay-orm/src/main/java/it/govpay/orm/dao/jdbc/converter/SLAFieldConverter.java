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

import it.govpay.orm.SLA;


/**     
 * SLAFieldConverter
 *
 * @author Giovanni Bussu (bussu@link.it)
 * @author Lorenzo Nardi (nardi@link.it)
 * @author $Author$
 * @version $Rev$, $Date$
 */
public class SLAFieldConverter extends AbstractSQLFieldConverter {

	private TipiDatabase databaseType;
	
	public SLAFieldConverter(String databaseType){
		this.databaseType = TipiDatabase.toEnumConstant(databaseType);
	}
	public SLAFieldConverter(TipiDatabase databaseType){
		this.databaseType = databaseType;
	}


	@Override
	public IModel<?> getRootModel() throws ExpressionException {
		return SLA.model();
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
		
		if(field.equals(SLA.model().DESCRIZIONE)){
			if(appendTablePrefix){
				return this.toAliasTable(field)+".descrizione";
			}else{
				return "descrizione";
			}
		}
		if(field.equals(SLA.model().TIPO_EVENTO_INIZIALE)){
			if(appendTablePrefix){
				return this.toAliasTable(field)+".tipo_evento_iniziale";
			}else{
				return "tipo_evento_iniziale";
			}
		}
		if(field.equals(SLA.model().SOTTOTIPO_EVENTO_INIZIALE)){
			if(appendTablePrefix){
				return this.toAliasTable(field)+".sottotipo_evento_iniziale";
			}else{
				return "sottotipo_evento_iniziale";
			}
		}
		if(field.equals(SLA.model().TIPO_EVENTO_FINALE)){
			if(appendTablePrefix){
				return this.toAliasTable(field)+".tipo_evento_finale";
			}else{
				return "tipo_evento_finale";
			}
		}
		if(field.equals(SLA.model().SOTTOTIPO_EVENTO_FINALE)){
			if(appendTablePrefix){
				return this.toAliasTable(field)+".sottotipo_evento_finale";
			}else{
				return "sottotipo_evento_finale";
			}
		}
		if(field.equals(SLA.model().TEMPO_A)){
			if(appendTablePrefix){
				return this.toAliasTable(field)+".tempo_a";
			}else{
				return "tempo_a";
			}
		}
		if(field.equals(SLA.model().TEMPO_B)){
			if(appendTablePrefix){
				return this.toAliasTable(field)+".tempo_b";
			}else{
				return "tempo_b";
			}
		}
		if(field.equals(SLA.model().TOLLERANZA_A)){
			if(appendTablePrefix){
				return this.toAliasTable(field)+".tolleranza_a";
			}else{
				return "tolleranza_a";
			}
		}
		if(field.equals(SLA.model().TOLLERANZA_B)){
			if(appendTablePrefix){
				return this.toAliasTable(field)+".tolleranza_b";
			}else{
				return "tolleranza_b";
			}
		}


		return super.toColumn(field,returnAlias,appendTablePrefix);
		
	}
	
	@Override
	public String toTable(IField field,boolean returnAlias) throws ExpressionException {
		
		// In the case of table with alias, using parameter returnAlias​​, 
		// it is possible to drive the choice whether to return only the alias or 
		// the full definition of the table containing the alias
		
		if(field.equals(SLA.model().DESCRIZIONE)){
			return this.toTable(SLA.model(), returnAlias);
		}
		if(field.equals(SLA.model().TIPO_EVENTO_INIZIALE)){
			return this.toTable(SLA.model(), returnAlias);
		}
		if(field.equals(SLA.model().SOTTOTIPO_EVENTO_INIZIALE)){
			return this.toTable(SLA.model(), returnAlias);
		}
		if(field.equals(SLA.model().TIPO_EVENTO_FINALE)){
			return this.toTable(SLA.model(), returnAlias);
		}
		if(field.equals(SLA.model().SOTTOTIPO_EVENTO_FINALE)){
			return this.toTable(SLA.model(), returnAlias);
		}
		if(field.equals(SLA.model().TEMPO_A)){
			return this.toTable(SLA.model(), returnAlias);
		}
		if(field.equals(SLA.model().TEMPO_B)){
			return this.toTable(SLA.model(), returnAlias);
		}
		if(field.equals(SLA.model().TOLLERANZA_A)){
			return this.toTable(SLA.model(), returnAlias);
		}
		if(field.equals(SLA.model().TOLLERANZA_B)){
			return this.toTable(SLA.model(), returnAlias);
		}


		return super.toTable(field,returnAlias);
		
	}

	@Override
	public String toTable(IModel<?> model,boolean returnAlias) throws ExpressionException {
		
		// In the case of table with alias, using parameter returnAlias​​, 
		// it is possible to drive the choice whether to return only the alias or 
		// the full definition of the table containing the alias
		
		if(model.equals(SLA.model())){
			return "sla";
		}


		return super.toTable(model,returnAlias);
		
	}

}
