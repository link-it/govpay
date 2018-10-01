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

import it.govpay.orm.EsitoAvvisatura;


/**     
 * EsitoAvvisaturaFieldConverter
 *
 * @author Giovanni Bussu (bussu@link.it)
 * @author Lorenzo Nardi (nardi@link.it)
 * @author $Author$
 * @version $Rev$, $Date$
 */
public class EsitoAvvisaturaFieldConverter extends AbstractSQLFieldConverter {

	private TipiDatabase databaseType;
	
	public EsitoAvvisaturaFieldConverter(String databaseType){
		this.databaseType = TipiDatabase.toEnumConstant(databaseType);
	}
	public EsitoAvvisaturaFieldConverter(TipiDatabase databaseType){
		this.databaseType = databaseType;
	}


	@Override
	public IModel<?> getRootModel() throws ExpressionException {
		return EsitoAvvisatura.model();
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
		
		if(field.equals(EsitoAvvisatura.model().COD_DOMINIO)){
			if(appendTablePrefix){
				return this.toAliasTable(field)+".cod_dominio";
			}else{
				return "cod_dominio";
			}
		}
		if(field.equals(EsitoAvvisatura.model().IDENTIFICATIVO_AVVISATURA)){
			if(appendTablePrefix){
				return this.toAliasTable(field)+".identificativo_avvisatura";
			}else{
				return "identificativo_avvisatura";
			}
		}
		if(field.equals(EsitoAvvisatura.model().TIPO_CANALE)){
			if(appendTablePrefix){
				return this.toAliasTable(field)+".tipo_canale";
			}else{
				return "tipo_canale";
			}
		}
		if(field.equals(EsitoAvvisatura.model().COD_CANALE)){
			if(appendTablePrefix){
				return this.toAliasTable(field)+".cod_canale";
			}else{
				return "cod_canale";
			}
		}
		if(field.equals(EsitoAvvisatura.model().DATA)){
			if(appendTablePrefix){
				return this.toAliasTable(field)+".data";
			}else{
				return "data";
			}
		}
		if(field.equals(EsitoAvvisatura.model().COD_ESITO)){
			if(appendTablePrefix){
				return this.toAliasTable(field)+".cod_esito";
			}else{
				return "cod_esito";
			}
		}
		if(field.equals(EsitoAvvisatura.model().DESCRIZIONE_ESITO)){
			if(appendTablePrefix){
				return this.toAliasTable(field)+".descrizione_esito";
			}else{
				return "descrizione_esito";
			}
		}
		if(field.equals(EsitoAvvisatura.model().ID_TRACCIATO.ID_TRACCIATO)){
			if(appendTablePrefix){
				return this.toAliasTable(field)+".id_tracciato";
			}else{
				return "id_tracciato";
			}
		}


		return super.toColumn(field,returnAlias,appendTablePrefix);
		
	}
	
	@Override
	public String toTable(IField field,boolean returnAlias) throws ExpressionException {
		
		// In the case of table with alias, using parameter returnAlias​​, 
		// it is possible to drive the choice whether to return only the alias or 
		// the full definition of the table containing the alias
		
		if(field.equals(EsitoAvvisatura.model().COD_DOMINIO)){
			return this.toTable(EsitoAvvisatura.model(), returnAlias);
		}
		if(field.equals(EsitoAvvisatura.model().IDENTIFICATIVO_AVVISATURA)){
			return this.toTable(EsitoAvvisatura.model(), returnAlias);
		}
		if(field.equals(EsitoAvvisatura.model().TIPO_CANALE)){
			return this.toTable(EsitoAvvisatura.model(), returnAlias);
		}
		if(field.equals(EsitoAvvisatura.model().COD_CANALE)){
			return this.toTable(EsitoAvvisatura.model(), returnAlias);
		}
		if(field.equals(EsitoAvvisatura.model().DATA)){
			return this.toTable(EsitoAvvisatura.model(), returnAlias);
		}
		if(field.equals(EsitoAvvisatura.model().COD_ESITO)){
			return this.toTable(EsitoAvvisatura.model(), returnAlias);
		}
		if(field.equals(EsitoAvvisatura.model().DESCRIZIONE_ESITO)){
			return this.toTable(EsitoAvvisatura.model(), returnAlias);
		}
		if(field.equals(EsitoAvvisatura.model().ID_TRACCIATO.ID_TRACCIATO)){
			return this.toTable(EsitoAvvisatura.model().ID_TRACCIATO, returnAlias);
		}


		return super.toTable(field,returnAlias);
		
	}

	@Override
	public String toTable(IModel<?> model,boolean returnAlias) throws ExpressionException {
		
		// In the case of table with alias, using parameter returnAlias​​, 
		// it is possible to drive the choice whether to return only the alias or 
		// the full definition of the table containing the alias
		
		if(model.equals(EsitoAvvisatura.model())){
			return "esiti_avvisatura";
		}
		if(model.equals(EsitoAvvisatura.model().ID_TRACCIATO)){
			return "tracciati";
		}


		return super.toTable(model,returnAlias);
		
	}

}
