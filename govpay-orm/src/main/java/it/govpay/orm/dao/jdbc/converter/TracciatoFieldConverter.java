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

import it.govpay.orm.Tracciato;


/**     
 * TracciatoFieldConverter
 *
 * @author Giovanni Bussu (bussu@link.it)
 * @author Lorenzo Nardi (nardi@link.it)
 * @author $Author$
 * @version $Rev$, $Date$
 */
public class TracciatoFieldConverter extends AbstractSQLFieldConverter {

	private TipiDatabase databaseType;
	
	public TracciatoFieldConverter(String databaseType){
		this.databaseType = TipiDatabase.toEnumConstant(databaseType);
	}
	public TracciatoFieldConverter(TipiDatabase databaseType){
		this.databaseType = databaseType;
	}


	@Override
	public IModel<?> getRootModel() throws ExpressionException {
		return Tracciato.model();
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
		
		if(field.equals(Tracciato.model().TIPO)){
			if(appendTablePrefix){
				return this.toAliasTable(field)+".tipo";
			}else{
				return "tipo";
			}
		}
		if(field.equals(Tracciato.model().STATO)){
			if(appendTablePrefix){
				return this.toAliasTable(field)+".stato";
			}else{
				return "stato";
			}
		}
		if(field.equals(Tracciato.model().DESCRIZIONE_STATO)){
			if(appendTablePrefix){
				return this.toAliasTable(field)+".descrizione_stato";
			}else{
				return "descrizione_stato";
			}
		}
		if(field.equals(Tracciato.model().DATA_CARICAMENTO)){
			if(appendTablePrefix){
				return this.toAliasTable(field)+".data_caricamento";
			}else{
				return "data_caricamento";
			}
		}
		if(field.equals(Tracciato.model().DATA_COMPLETAMENTO)){
			if(appendTablePrefix){
				return this.toAliasTable(field)+".data_completamento";
			}else{
				return "data_completamento";
			}
		}
		if(field.equals(Tracciato.model().BEAN_DATI)){
			if(appendTablePrefix){
				return this.toAliasTable(field)+".bean_dati";
			}else{
				return "bean_dati";
			}
		}
		if(field.equals(Tracciato.model().FILE_NAME_RICHIESTA)){
			if(appendTablePrefix){
				return this.toAliasTable(field)+".file_name_richiesta";
			}else{
				return "file_name_richiesta";
			}
		}
		if(field.equals(Tracciato.model().RAW_RICHIESTA)){
			if(appendTablePrefix){
				return this.toAliasTable(field)+".raw_richiesta";
			}else{
				return "raw_richiesta";
			}
		}
		if(field.equals(Tracciato.model().FILE_NAME_ESITO)){
			if(appendTablePrefix){
				return this.toAliasTable(field)+".file_name_esito";
			}else{
				return "file_name_esito";
			}
		}
		if(field.equals(Tracciato.model().RAW_ESITO)){
			if(appendTablePrefix){
				return this.toAliasTable(field)+".raw_esito";
			}else{
				return "raw_esito";
			}
		}


		return super.toColumn(field,returnAlias,appendTablePrefix);
		
	}
	
	@Override
	public String toTable(IField field,boolean returnAlias) throws ExpressionException {
		
		// In the case of table with alias, using parameter returnAlias​​, 
		// it is possible to drive the choice whether to return only the alias or 
		// the full definition of the table containing the alias
		
		if(field.equals(Tracciato.model().TIPO)){
			return this.toTable(Tracciato.model(), returnAlias);
		}
		if(field.equals(Tracciato.model().STATO)){
			return this.toTable(Tracciato.model(), returnAlias);
		}
		if(field.equals(Tracciato.model().DESCRIZIONE_STATO)){
			return this.toTable(Tracciato.model(), returnAlias);
		}
		if(field.equals(Tracciato.model().DATA_CARICAMENTO)){
			return this.toTable(Tracciato.model(), returnAlias);
		}
		if(field.equals(Tracciato.model().DATA_COMPLETAMENTO)){
			return this.toTable(Tracciato.model(), returnAlias);
		}
		if(field.equals(Tracciato.model().BEAN_DATI)){
			return this.toTable(Tracciato.model(), returnAlias);
		}
		if(field.equals(Tracciato.model().FILE_NAME_RICHIESTA)){
			return this.toTable(Tracciato.model(), returnAlias);
		}
		if(field.equals(Tracciato.model().RAW_RICHIESTA)){
			return this.toTable(Tracciato.model(), returnAlias);
		}
		if(field.equals(Tracciato.model().FILE_NAME_ESITO)){
			return this.toTable(Tracciato.model(), returnAlias);
		}
		if(field.equals(Tracciato.model().RAW_ESITO)){
			return this.toTable(Tracciato.model(), returnAlias);
		}


		return super.toTable(field,returnAlias);
		
	}

	@Override
	public String toTable(IModel<?> model,boolean returnAlias) throws ExpressionException {
		
		// In the case of table with alias, using parameter returnAlias​​, 
		// it is possible to drive the choice whether to return only the alias or 
		// the full definition of the table containing the alias
		
		if(model.equals(Tracciato.model())){
			return "tracciati";
		}


		return super.toTable(model,returnAlias);
		
	}

}
