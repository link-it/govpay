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
		
		if(field.equals(Tracciato.model().ID_OPERATORE.PRINCIPAL)){
			if(appendTablePrefix){
				return this.toAliasTable(field)+".principal";
			}else{
				return "principal";
			}
		}
		if(field.equals(Tracciato.model().ID_APPLICAZIONE.COD_APPLICAZIONE)){
			if(appendTablePrefix){
				return this.toAliasTable(field)+".cod_applicazione";
			}else{
				return "cod_applicazione";
			}
		}
		if(field.equals(Tracciato.model().DATA_CARICAMENTO)){
			if(appendTablePrefix){
				return this.toAliasTable(field)+".data_caricamento";
			}else{
				return "data_caricamento";
			}
		}
		if(field.equals(Tracciato.model().DATA_ULTIMO_AGGIORNAMENTO)){
			if(appendTablePrefix){
				return this.toAliasTable(field)+".data_ultimo_aggiornamento";
			}else{
				return "data_ultimo_aggiornamento";
			}
		}
		if(field.equals(Tracciato.model().STATO)){
			if(appendTablePrefix){
				return this.toAliasTable(field)+".stato";
			}else{
				return "stato";
			}
		}
		if(field.equals(Tracciato.model().LINEA_ELABORAZIONE)){
			if(appendTablePrefix){
				return this.toAliasTable(field)+".linea_elaborazione";
			}else{
				return "linea_elaborazione";
			}
		}
		if(field.equals(Tracciato.model().DESCRIZIONE_STATO)){
			if(appendTablePrefix){
				return this.toAliasTable(field)+".descrizione_stato";
			}else{
				return "descrizione_stato";
			}
		}
		if(field.equals(Tracciato.model().NUM_LINEE_TOTALI)){
			if(appendTablePrefix){
				return this.toAliasTable(field)+".num_linee_totali";
			}else{
				return "num_linee_totali";
			}
		}
		if(field.equals(Tracciato.model().NUM_OPERAZIONI_OK)){
			if(appendTablePrefix){
				return this.toAliasTable(field)+".num_operazioni_ok";
			}else{
				return "num_operazioni_ok";
			}
		}
		if(field.equals(Tracciato.model().NUM_OPERAZIONI_KO)){
			if(appendTablePrefix){
				return this.toAliasTable(field)+".num_operazioni_ko";
			}else{
				return "num_operazioni_ko";
			}
		}
		if(field.equals(Tracciato.model().NOME_FILE)){
			if(appendTablePrefix){
				return this.toAliasTable(field)+".nome_file";
			}else{
				return "nome_file";
			}
		}
		if(field.equals(Tracciato.model().RAW_DATA_RICHIESTA)){
			if(appendTablePrefix){
				return this.toAliasTable(field)+".raw_data_richiesta";
			}else{
				return "raw_data_richiesta";
			}
		}
		if(field.equals(Tracciato.model().RAW_DATA_RISPOSTA)){
			if(appendTablePrefix){
				return this.toAliasTable(field)+".raw_data_risposta";
			}else{
				return "raw_data_risposta";
			}
		}
		if(field.equals(Tracciato.model().TIPO_TRACCIATO)){
			if(appendTablePrefix){
				return this.toAliasTable(field)+".tipo_tracciato";
			}else{
				return "tipo_tracciato";
			}
		}


		return super.toColumn(field,returnAlias,appendTablePrefix);
		
	}
	
	@Override
	public String toTable(IField field,boolean returnAlias) throws ExpressionException {
		
		// In the case of table with alias, using parameter returnAlias​​, 
		// it is possible to drive the choice whether to return only the alias or 
		// the full definition of the table containing the alias
		
		if(field.equals(Tracciato.model().ID_OPERATORE.PRINCIPAL)){
			return this.toTable(Tracciato.model().ID_OPERATORE, returnAlias);
		}
		if(field.equals(Tracciato.model().ID_APPLICAZIONE.COD_APPLICAZIONE)){
			return this.toTable(Tracciato.model().ID_APPLICAZIONE, returnAlias);
		}
		if(field.equals(Tracciato.model().DATA_CARICAMENTO)){
			return this.toTable(Tracciato.model(), returnAlias);
		}
		if(field.equals(Tracciato.model().DATA_ULTIMO_AGGIORNAMENTO)){
			return this.toTable(Tracciato.model(), returnAlias);
		}
		if(field.equals(Tracciato.model().STATO)){
			return this.toTable(Tracciato.model(), returnAlias);
		}
		if(field.equals(Tracciato.model().LINEA_ELABORAZIONE)){
			return this.toTable(Tracciato.model(), returnAlias);
		}
		if(field.equals(Tracciato.model().DESCRIZIONE_STATO)){
			return this.toTable(Tracciato.model(), returnAlias);
		}
		if(field.equals(Tracciato.model().NUM_LINEE_TOTALI)){
			return this.toTable(Tracciato.model(), returnAlias);
		}
		if(field.equals(Tracciato.model().NUM_OPERAZIONI_OK)){
			return this.toTable(Tracciato.model(), returnAlias);
		}
		if(field.equals(Tracciato.model().NUM_OPERAZIONI_KO)){
			return this.toTable(Tracciato.model(), returnAlias);
		}
		if(field.equals(Tracciato.model().NOME_FILE)){
			return this.toTable(Tracciato.model(), returnAlias);
		}
		if(field.equals(Tracciato.model().RAW_DATA_RICHIESTA)){
			return this.toTable(Tracciato.model(), returnAlias);
		}
		if(field.equals(Tracciato.model().RAW_DATA_RISPOSTA)){
			return this.toTable(Tracciato.model(), returnAlias);
		}
		if(field.equals(Tracciato.model().TIPO_TRACCIATO)){
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
		if(model.equals(Tracciato.model().ID_OPERATORE)){
			return "operatori";
		}
		if(model.equals(Tracciato.model().ID_APPLICAZIONE)){
			return "applicazioni";
		}


		return super.toTable(model,returnAlias);
		
	}

}
