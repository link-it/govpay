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

import it.govpay.orm.Esito;


/**     
 * EsitoFieldConverter
 *
 * @author Giovanni Bussu (bussu@link.it)
 * @author Lorenzo Nardi (nardi@link.it)
 * @author $Author$
 * @version $Rev$, $Date$
 */
public class EsitoFieldConverter extends AbstractSQLFieldConverter {

	private TipiDatabase databaseType;
	
	public EsitoFieldConverter(String databaseType){
		this.databaseType = TipiDatabase.toEnumConstant(databaseType);
	}
	public EsitoFieldConverter(TipiDatabase databaseType){
		this.databaseType = databaseType;
	}


	@Override
	public IModel<?> getRootModel() throws ExpressionException {
		return Esito.model();
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
		
		if(field.equals(Esito.model().COD_DOMINIO)){
			if(appendTablePrefix){
				return this.toAliasTable(field)+".cod_dominio";
			}else{
				return "cod_dominio";
			}
		}
		if(field.equals(Esito.model().IUV)){
			if(appendTablePrefix){
				return this.toAliasTable(field)+".iuv";
			}else{
				return "iuv";
			}
		}
		if(field.equals(Esito.model().ID_APPLICAZIONE.COD_APPLICAZIONE)){
			if(appendTablePrefix){
				return this.toAliasTable(field)+".cod_applicazione";
			}else{
				return "cod_applicazione";
			}
		}
		if(field.equals(Esito.model().STATO_SPEDIZIONE)){
			if(appendTablePrefix){
				return this.toAliasTable(field)+".stato_spedizione";
			}else{
				return "stato_spedizione";
			}
		}
		if(field.equals(Esito.model().DETTAGLIO_SPEDIZIONE)){
			if(appendTablePrefix){
				return this.toAliasTable(field)+".dettaglio_spedizione";
			}else{
				return "dettaglio_spedizione";
			}
		}
		if(field.equals(Esito.model().TENTATIVI_SPEDIZIONE)){
			if(appendTablePrefix){
				return this.toAliasTable(field)+".tentativi_spedizione";
			}else{
				return "tentativi_spedizione";
			}
		}
		if(field.equals(Esito.model().DATA_ORA_CREAZIONE)){
			if(appendTablePrefix){
				return this.toAliasTable(field)+".data_ora_creazione";
			}else{
				return "data_ora_creazione";
			}
		}
		if(field.equals(Esito.model().DATA_ORA_ULTIMA_SPEDIZIONE)){
			if(appendTablePrefix){
				return this.toAliasTable(field)+".data_ora_ultima_spedizione";
			}else{
				return "data_ora_ultima_spedizione";
			}
		}
		if(field.equals(Esito.model().DATA_ORA_PROSSIMA_SPEDIZIONE)){
			if(appendTablePrefix){
				return this.toAliasTable(field)+".data_ora_prossima_spedizione";
			}else{
				return "data_ora_prossima_spedizione";
			}
		}
		if(field.equals(Esito.model().XML)){
			if(appendTablePrefix){
				return this.toAliasTable(field)+".xml";
			}else{
				return "xml";
			}
		}


		return super.toColumn(field,returnAlias,appendTablePrefix);
		
	}
	
	@Override
	public String toTable(IField field,boolean returnAlias) throws ExpressionException {
		
		// In the case of table with alias, using parameter returnAlias​​, 
		// it is possible to drive the choice whether to return only the alias or 
		// the full definition of the table containing the alias
		
		if(field.equals(Esito.model().COD_DOMINIO)){
			return this.toTable(Esito.model(), returnAlias);
		}
		if(field.equals(Esito.model().IUV)){
			return this.toTable(Esito.model(), returnAlias);
		}
		if(field.equals(Esito.model().ID_APPLICAZIONE.COD_APPLICAZIONE)){
			return this.toTable(Esito.model().ID_APPLICAZIONE, returnAlias);
		}
		if(field.equals(Esito.model().STATO_SPEDIZIONE)){
			return this.toTable(Esito.model(), returnAlias);
		}
		if(field.equals(Esito.model().DETTAGLIO_SPEDIZIONE)){
			return this.toTable(Esito.model(), returnAlias);
		}
		if(field.equals(Esito.model().TENTATIVI_SPEDIZIONE)){
			return this.toTable(Esito.model(), returnAlias);
		}
		if(field.equals(Esito.model().DATA_ORA_CREAZIONE)){
			return this.toTable(Esito.model(), returnAlias);
		}
		if(field.equals(Esito.model().DATA_ORA_ULTIMA_SPEDIZIONE)){
			return this.toTable(Esito.model(), returnAlias);
		}
		if(field.equals(Esito.model().DATA_ORA_PROSSIMA_SPEDIZIONE)){
			return this.toTable(Esito.model(), returnAlias);
		}
		if(field.equals(Esito.model().XML)){
			return this.toTable(Esito.model(), returnAlias);
		}


		return super.toTable(field,returnAlias);
		
	}

	@Override
	public String toTable(IModel<?> model,boolean returnAlias) throws ExpressionException {
		
		// In the case of table with alias, using parameter returnAlias​​, 
		// it is possible to drive the choice whether to return only the alias or 
		// the full definition of the table containing the alias
		
		if(model.equals(Esito.model())){
			return "esiti";
		}
		if(model.equals(Esito.model().ID_APPLICAZIONE)){
			return "applicazioni";
		}


		return super.toTable(model,returnAlias);
		
	}

}
