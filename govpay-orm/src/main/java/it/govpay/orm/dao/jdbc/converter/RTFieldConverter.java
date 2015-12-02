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

import it.govpay.orm.RT;


/**     
 * RTFieldConverter
 *
 * @author Giovanni Bussu (bussu@link.it)
 * @author Lorenzo Nardi (nardi@link.it)
 * @author $Author$
 * @version $Rev$, $Date$
 */
public class RTFieldConverter extends AbstractSQLFieldConverter {

	private TipiDatabase databaseType;
	
	public RTFieldConverter(String databaseType){
		this.databaseType = TipiDatabase.toEnumConstant(databaseType);
	}
	public RTFieldConverter(TipiDatabase databaseType){
		this.databaseType = databaseType;
	}


	@Override
	public IModel<?> getRootModel() throws ExpressionException {
		return RT.model();
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
		
		if(field.equals(RT.model().ID_RPT.COD_MSG_RICHIESTA)){
			if(appendTablePrefix){
				return this.toAliasTable(field)+".cod_msg_richiesta";
			}else{
				return "cod_msg_richiesta";
			}
		}
		if(field.equals(RT.model().ID_TRACCIATO.ID_TRACCIATO)){
			if(appendTablePrefix){
				return this.toAliasTable(field)+".id_tracciato";
			}else{
				return "id_tracciato";
			}
		}
		if(field.equals(RT.model().COD_MSG_RICEVUTA)){
			if(appendTablePrefix){
				return this.toAliasTable(field)+".cod_msg_ricevuta";
			}else{
				return "cod_msg_ricevuta";
			}
		}
		if(field.equals(RT.model().DATA_ORA_MSG_RICEVUTA)){
			if(appendTablePrefix){
				return this.toAliasTable(field)+".data_ora_msg_ricevuta";
			}else{
				return "data_ora_msg_ricevuta";
			}
		}
		if(field.equals(RT.model().ID_ANAGRAFICA_ATTESTANTE.ID_ANAGRAFICA)){
			if(appendTablePrefix){
				return this.toAliasTable(field)+".id_anagrafica";
			}else{
				return "id_anagrafica";
			}
		}
		if(field.equals(RT.model().COD_ESITO_PAGAMENTO)){
			if(appendTablePrefix){
				return this.toAliasTable(field)+".cod_esito_pagamento";
			}else{
				return "cod_esito_pagamento";
			}
		}
		if(field.equals(RT.model().IMPORTO_TOTALE_PAGATO)){
			if(appendTablePrefix){
				return this.toAliasTable(field)+".importo_totale_pagato";
			}else{
				return "importo_totale_pagato";
			}
		}
		if(field.equals(RT.model().STATO)){
			if(appendTablePrefix){
				return this.toAliasTable(field)+".stato";
			}else{
				return "stato";
			}
		}
		if(field.equals(RT.model().DESCRIZIONE_STATO)){
			if(appendTablePrefix){
				return this.toAliasTable(field)+".descrizione_stato";
			}else{
				return "descrizione_stato";
			}
		}


		return super.toColumn(field,returnAlias,appendTablePrefix);
		
	}
	
	@Override
	public String toTable(IField field,boolean returnAlias) throws ExpressionException {
		
		// In the case of table with alias, using parameter returnAlias​​, 
		// it is possible to drive the choice whether to return only the alias or 
		// the full definition of the table containing the alias
		
		if(field.equals(RT.model().ID_RPT.COD_MSG_RICHIESTA)){
			return this.toTable(RT.model().ID_RPT, returnAlias);
		}
		if(field.equals(RT.model().ID_TRACCIATO.ID_TRACCIATO)){
			return this.toTable(RT.model().ID_TRACCIATO, returnAlias);
		}
		if(field.equals(RT.model().COD_MSG_RICEVUTA)){
			return this.toTable(RT.model(), returnAlias);
		}
		if(field.equals(RT.model().DATA_ORA_MSG_RICEVUTA)){
			return this.toTable(RT.model(), returnAlias);
		}
		if(field.equals(RT.model().ID_ANAGRAFICA_ATTESTANTE.ID_ANAGRAFICA)){
			return this.toTable(RT.model().ID_ANAGRAFICA_ATTESTANTE, returnAlias);
		}
		if(field.equals(RT.model().COD_ESITO_PAGAMENTO)){
			return this.toTable(RT.model(), returnAlias);
		}
		if(field.equals(RT.model().IMPORTO_TOTALE_PAGATO)){
			return this.toTable(RT.model(), returnAlias);
		}
		if(field.equals(RT.model().STATO)){
			return this.toTable(RT.model(), returnAlias);
		}
		if(field.equals(RT.model().DESCRIZIONE_STATO)){
			return this.toTable(RT.model(), returnAlias);
		}


		return super.toTable(field,returnAlias);
		
	}

	@Override
	public String toTable(IModel<?> model,boolean returnAlias) throws ExpressionException {
		
		// In the case of table with alias, using parameter returnAlias​​, 
		// it is possible to drive the choice whether to return only the alias or 
		// the full definition of the table containing the alias
		
		if(model.equals(RT.model())){
			return "rt";
		}
		if(model.equals(RT.model().ID_RPT)){
			return "rpt";
		}
		if(model.equals(RT.model().ID_TRACCIATO)){
			return "tracciatixml";
		}
		if(model.equals(RT.model().ID_ANAGRAFICA_ATTESTANTE)){
			return "anagrafiche";
		}


		return super.toTable(model,returnAlias);
		
	}

}
