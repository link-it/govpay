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

import it.govpay.orm.SingolaRevoca;


/**     
 * SingolaRevocaFieldConverter
 *
 * @author Giovanni Bussu (bussu@link.it)
 * @author Lorenzo Nardi (nardi@link.it)
 * @author $Author$
 * @version $Rev$, $Date$
 */
public class SingolaRevocaFieldConverter extends AbstractSQLFieldConverter {

	private TipiDatabase databaseType;
	
	public SingolaRevocaFieldConverter(String databaseType){
		this.databaseType = TipiDatabase.toEnumConstant(databaseType);
	}
	public SingolaRevocaFieldConverter(TipiDatabase databaseType){
		this.databaseType = databaseType;
	}


	@Override
	public IModel<?> getRootModel() throws ExpressionException {
		return SingolaRevoca.model();
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
		
		if(field.equals(SingolaRevoca.model().ID_RR.COD_MSG_REVOCA)){
			if(appendTablePrefix){
				return this.toAliasTable(field)+".cod_msg_revoca";
			}else{
				return "cod_msg_revoca";
			}
		}
		if(field.equals(SingolaRevoca.model().ID_ER)){
			if(appendTablePrefix){
				return this.toAliasTable(field)+".id_er";
			}else{
				return "id_er";
			}
		}
		if(field.equals(SingolaRevoca.model().ID_SINGOLO_VERSAMENTO.ID_VERSAMENTO.COD_VERSAMENTO_ENTE)){
			if(appendTablePrefix){
				return this.toAliasTable(field)+".cod_versamento_ente";
			}else{
				return "cod_versamento_ente";
			}
		}
		if(field.equals(SingolaRevoca.model().ID_SINGOLO_VERSAMENTO.ID_VERSAMENTO.COD_DOMINIO)){
			if(appendTablePrefix){
				return this.toAliasTable(field)+".cod_dominio";
			}else{
				return "cod_dominio";
			}
		}
		if(field.equals(SingolaRevoca.model().ID_SINGOLO_VERSAMENTO.INDICE)){
			if(appendTablePrefix){
				return this.toAliasTable(field)+".indice";
			}else{
				return "indice";
			}
		}
		if(field.equals(SingolaRevoca.model().CAUSALE_REVOCA)){
			if(appendTablePrefix){
				return this.toAliasTable(field)+".causale_revoca";
			}else{
				return "causale_revoca";
			}
		}
		if(field.equals(SingolaRevoca.model().DATI_AGGIUNTIVI_REVOCA)){
			if(appendTablePrefix){
				return this.toAliasTable(field)+".dati_aggiuntivi_revoca";
			}else{
				return "dati_aggiuntivi_revoca";
			}
		}
		if(field.equals(SingolaRevoca.model().SINGOLO_IMPORTO)){
			if(appendTablePrefix){
				return this.toAliasTable(field)+".singolo_importo";
			}else{
				return "singolo_importo";
			}
		}
		if(field.equals(SingolaRevoca.model().SINGOLO_IMPORTO_REVOCATO)){
			if(appendTablePrefix){
				return this.toAliasTable(field)+".singolo_importo_revocato";
			}else{
				return "singolo_importo_revocato";
			}
		}
		if(field.equals(SingolaRevoca.model().CAUSALE_ESITO)){
			if(appendTablePrefix){
				return this.toAliasTable(field)+".causale_esito";
			}else{
				return "causale_esito";
			}
		}
		if(field.equals(SingolaRevoca.model().DATI_AGGIUNTIVI_ESITO)){
			if(appendTablePrefix){
				return this.toAliasTable(field)+".dati_aggiuntivi_esito";
			}else{
				return "dati_aggiuntivi_esito";
			}
		}
		if(field.equals(SingolaRevoca.model().STATO)){
			if(appendTablePrefix){
				return this.toAliasTable(field)+".stato";
			}else{
				return "stato";
			}
		}
		if(field.equals(SingolaRevoca.model().DESCRIZIONE_STATO)){
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
		
		if(field.equals(SingolaRevoca.model().ID_RR.COD_MSG_REVOCA)){
			return this.toTable(SingolaRevoca.model().ID_RR, returnAlias);
		}
		if(field.equals(SingolaRevoca.model().ID_ER)){
			return this.toTable(SingolaRevoca.model(), returnAlias);
		}
		if(field.equals(SingolaRevoca.model().ID_SINGOLO_VERSAMENTO.ID_VERSAMENTO.COD_VERSAMENTO_ENTE)){
			return this.toTable(SingolaRevoca.model().ID_SINGOLO_VERSAMENTO.ID_VERSAMENTO, returnAlias);
		}
		if(field.equals(SingolaRevoca.model().ID_SINGOLO_VERSAMENTO.ID_VERSAMENTO.COD_DOMINIO)){
			return this.toTable(SingolaRevoca.model().ID_SINGOLO_VERSAMENTO.ID_VERSAMENTO, returnAlias);
		}
		if(field.equals(SingolaRevoca.model().ID_SINGOLO_VERSAMENTO.INDICE)){
			return this.toTable(SingolaRevoca.model().ID_SINGOLO_VERSAMENTO, returnAlias);
		}
		if(field.equals(SingolaRevoca.model().CAUSALE_REVOCA)){
			return this.toTable(SingolaRevoca.model(), returnAlias);
		}
		if(field.equals(SingolaRevoca.model().DATI_AGGIUNTIVI_REVOCA)){
			return this.toTable(SingolaRevoca.model(), returnAlias);
		}
		if(field.equals(SingolaRevoca.model().SINGOLO_IMPORTO)){
			return this.toTable(SingolaRevoca.model(), returnAlias);
		}
		if(field.equals(SingolaRevoca.model().SINGOLO_IMPORTO_REVOCATO)){
			return this.toTable(SingolaRevoca.model(), returnAlias);
		}
		if(field.equals(SingolaRevoca.model().CAUSALE_ESITO)){
			return this.toTable(SingolaRevoca.model(), returnAlias);
		}
		if(field.equals(SingolaRevoca.model().DATI_AGGIUNTIVI_ESITO)){
			return this.toTable(SingolaRevoca.model(), returnAlias);
		}
		if(field.equals(SingolaRevoca.model().STATO)){
			return this.toTable(SingolaRevoca.model(), returnAlias);
		}
		if(field.equals(SingolaRevoca.model().DESCRIZIONE_STATO)){
			return this.toTable(SingolaRevoca.model(), returnAlias);
		}


		return super.toTable(field,returnAlias);
		
	}

	@Override
	public String toTable(IModel<?> model,boolean returnAlias) throws ExpressionException {
		
		// In the case of table with alias, using parameter returnAlias​​, 
		// it is possible to drive the choice whether to return only the alias or 
		// the full definition of the table containing the alias
		
		if(model.equals(SingolaRevoca.model())){
			return "singole_revoche";
		}
		if(model.equals(SingolaRevoca.model().ID_RR)){
			return "rr";
		}
		if(model.equals(SingolaRevoca.model().ID_SINGOLO_VERSAMENTO)){
			return "singoli_versamenti";
		}
		if(model.equals(SingolaRevoca.model().ID_SINGOLO_VERSAMENTO.ID_VERSAMENTO)){
			return "id_versamento";
		}


		return super.toTable(model,returnAlias);
		
	}

}
