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

import it.govpay.orm.Operazione;


/**     
 * OperazioneFieldConverter
 *
 * @author Giovanni Bussu (bussu@link.it)
 * @author Lorenzo Nardi (nardi@link.it)
 * @author $Author$
 * @version $Rev$, $Date$
 */
public class OperazioneFieldConverter extends AbstractSQLFieldConverter {

	private TipiDatabase databaseType;
	
	public OperazioneFieldConverter(String databaseType){
		this.databaseType = TipiDatabase.toEnumConstant(databaseType);
	}
	public OperazioneFieldConverter(TipiDatabase databaseType){
		this.databaseType = databaseType;
	}


	@Override
	public IModel<?> getRootModel() throws ExpressionException {
		return Operazione.model();
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
		
		if(field.equals(Operazione.model().ID_TRACCIATO.ID_TRACCIATO)){
			if(appendTablePrefix){
				return this.toAliasTable(field)+".id_tracciato";
			}else{
				return "id_tracciato";
			}
		}
		if(field.equals(Operazione.model().TIPO_OPERAZIONE)){
			if(appendTablePrefix){
				return this.toAliasTable(field)+".tipo_operazione";
			}else{
				return "tipo_operazione";
			}
		}
		if(field.equals(Operazione.model().LINEA_ELABORAZIONE)){
			if(appendTablePrefix){
				return this.toAliasTable(field)+".linea_elaborazione";
			}else{
				return "linea_elaborazione";
			}
		}
		if(field.equals(Operazione.model().STATO)){
			if(appendTablePrefix){
				return this.toAliasTable(field)+".stato";
			}else{
				return "stato";
			}
		}
		if(field.equals(Operazione.model().DATI_RICHIESTA)){
			if(appendTablePrefix){
				return this.toAliasTable(field)+".dati_richiesta";
			}else{
				return "dati_richiesta";
			}
		}
		if(field.equals(Operazione.model().DATI_RISPOSTA)){
			if(appendTablePrefix){
				return this.toAliasTable(field)+".dati_risposta";
			}else{
				return "dati_risposta";
			}
		}
		if(field.equals(Operazione.model().DETTAGLIO_ESITO)){
			if(appendTablePrefix){
				return this.toAliasTable(field)+".dettaglio_esito";
			}else{
				return "dettaglio_esito";
			}
		}
		if(field.equals(Operazione.model().ID_APPLICAZIONE.COD_APPLICAZIONE)){
			if(appendTablePrefix){
				return this.toAliasTable(field)+".cod_applicazione";
			}else{
				return "cod_applicazione";
			}
		}
		if(field.equals(Operazione.model().COD_VERSAMENTO_ENTE)){
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
		
		if(field.equals(Operazione.model().ID_TRACCIATO.ID_TRACCIATO)){
			return this.toTable(Operazione.model().ID_TRACCIATO, returnAlias);
		}
		if(field.equals(Operazione.model().TIPO_OPERAZIONE)){
			return this.toTable(Operazione.model(), returnAlias);
		}
		if(field.equals(Operazione.model().LINEA_ELABORAZIONE)){
			return this.toTable(Operazione.model(), returnAlias);
		}
		if(field.equals(Operazione.model().STATO)){
			return this.toTable(Operazione.model(), returnAlias);
		}
		if(field.equals(Operazione.model().DATI_RICHIESTA)){
			return this.toTable(Operazione.model(), returnAlias);
		}
		if(field.equals(Operazione.model().DATI_RISPOSTA)){
			return this.toTable(Operazione.model(), returnAlias);
		}
		if(field.equals(Operazione.model().DETTAGLIO_ESITO)){
			return this.toTable(Operazione.model(), returnAlias);
		}
		if(field.equals(Operazione.model().ID_APPLICAZIONE.COD_APPLICAZIONE)){
			return this.toTable(Operazione.model().ID_APPLICAZIONE, returnAlias);
		}
		if(field.equals(Operazione.model().COD_VERSAMENTO_ENTE)){
			return this.toTable(Operazione.model(), returnAlias);
		}


		return super.toTable(field,returnAlias);
		
	}

	@Override
	public String toTable(IModel<?> model,boolean returnAlias) throws ExpressionException {
		
		// In the case of table with alias, using parameter returnAlias​​, 
		// it is possible to drive the choice whether to return only the alias or 
		// the full definition of the table containing the alias
		
		if(model.equals(Operazione.model())){
			return "operazioni";
		}
		if(model.equals(Operazione.model().ID_TRACCIATO)){
			return "tracciati";
		}
		if(model.equals(Operazione.model().ID_APPLICAZIONE)){
			return "applicazioni";
		}


		return super.toTable(model,returnAlias);
		
	}

}
