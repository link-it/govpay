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

import it.govpay.orm.Rilevamento;


/**     
 * RilevamentoFieldConverter
 *
 * @author Giovanni Bussu (bussu@link.it)
 * @author Lorenzo Nardi (nardi@link.it)
 * @author $Author$
 * @version $Rev$, $Date$
 */
public class RilevamentoFieldConverter extends AbstractSQLFieldConverter {

	private TipiDatabase databaseType;
	
	public RilevamentoFieldConverter(String databaseType){
		this.databaseType = TipiDatabase.toEnumConstant(databaseType);
	}
	public RilevamentoFieldConverter(TipiDatabase databaseType){
		this.databaseType = databaseType;
	}


	@Override
	public IModel<?> getRootModel() throws ExpressionException {
		return Rilevamento.model();
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
		
		if(field.equals(Rilevamento.model().ID_SLA.ID_SLA)){
			if(appendTablePrefix){
				return this.toAliasTable(field)+".id_sla";
			}else{
				return "id_sla";
			}
		}
		if(field.equals(Rilevamento.model().ID_APPLICAZIONE)){
			if(appendTablePrefix){
				return this.toAliasTable(field)+".id_applicazione";
			}else{
				return "id_applicazione";
			}
		}
		if(field.equals(Rilevamento.model().ID_EVENTO_INIZIALE.ID_EVENTO)){
			if(appendTablePrefix){
				return this.toAliasTable(field)+".id_evento";
			}else{
				return "id_evento";
			}
		}
		if(field.equals(Rilevamento.model().ID_EVENTO_FINALE.ID_EVENTO)){
			if(appendTablePrefix){
				return this.toAliasTable(field)+".id_evento";
			}else{
				return "id_evento";
			}
		}
		if(field.equals(Rilevamento.model().DATA_RILEVAMENTO)){
			if(appendTablePrefix){
				return this.toAliasTable(field)+".data_rilevamento";
			}else{
				return "data_rilevamento";
			}
		}
		if(field.equals(Rilevamento.model().DURATA)){
			if(appendTablePrefix){
				return this.toAliasTable(field)+".durata";
			}else{
				return "durata";
			}
		}


		return super.toColumn(field,returnAlias,appendTablePrefix);
		
	}
	
	@Override
	public String toTable(IField field,boolean returnAlias) throws ExpressionException {
		
		// In the case of table with alias, using parameter returnAlias​​, 
		// it is possible to drive the choice whether to return only the alias or 
		// the full definition of the table containing the alias
		
		if(field.equals(Rilevamento.model().ID_SLA.ID_SLA)){
			return this.toTable(Rilevamento.model().ID_SLA, returnAlias);
		}
		if(field.equals(Rilevamento.model().ID_APPLICAZIONE)){
			return this.toTable(Rilevamento.model(), returnAlias);
		}
		if(field.equals(Rilevamento.model().ID_EVENTO_INIZIALE.ID_EVENTO)){
			return this.toTable(Rilevamento.model().ID_EVENTO_INIZIALE, returnAlias);
		}
		if(field.equals(Rilevamento.model().ID_EVENTO_FINALE.ID_EVENTO)){
			return this.toTable(Rilevamento.model().ID_EVENTO_FINALE, returnAlias);
		}
		if(field.equals(Rilevamento.model().DATA_RILEVAMENTO)){
			return this.toTable(Rilevamento.model(), returnAlias);
		}
		if(field.equals(Rilevamento.model().DURATA)){
			return this.toTable(Rilevamento.model(), returnAlias);
		}


		return super.toTable(field,returnAlias);
		
	}

	@Override
	public String toTable(IModel<?> model,boolean returnAlias) throws ExpressionException {
		
		// In the case of table with alias, using parameter returnAlias​​, 
		// it is possible to drive the choice whether to return only the alias or 
		// the full definition of the table containing the alias
		
		if(model.equals(Rilevamento.model())){
			return "rilevamenti";
		}
		if(model.equals(Rilevamento.model().ID_SLA)){
			return "sla";
		}
		if(model.equals(Rilevamento.model().ID_EVENTO_INIZIALE)){
			return "eventi";
		}
		if(model.equals(Rilevamento.model().ID_EVENTO_FINALE)){
			return "eventi";
		}


		return super.toTable(model,returnAlias);
		
	}

}
