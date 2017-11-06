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

import it.govpay.orm.Avviso;


/**     
 * AvvisoFieldConverter
 *
 * @author Giovanni Bussu (bussu@link.it)
 * @author Lorenzo Nardi (nardi@link.it)
 * @author $Author$
 * @version $Rev$, $Date$
 */
public class AvvisoFieldConverter extends AbstractSQLFieldConverter {

	private TipiDatabase databaseType;
	
	public AvvisoFieldConverter(String databaseType){
		this.databaseType = TipiDatabase.toEnumConstant(databaseType);
	}
	public AvvisoFieldConverter(TipiDatabase databaseType){
		this.databaseType = databaseType;
	}


	@Override
	public IModel<?> getRootModel() throws ExpressionException {
		return Avviso.model();
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
		
		if(field.equals(Avviso.model().COD_DOMINIO)){
			if(appendTablePrefix){
				return this.toAliasTable(field)+".cod_dominio";
			}else{
				return "cod_dominio";
			}
		}
		if(field.equals(Avviso.model().IUV)){
			if(appendTablePrefix){
				return this.toAliasTable(field)+".iuv";
			}else{
				return "iuv";
			}
		}
		if(field.equals(Avviso.model().DATA_CREAZIONE)){
			if(appendTablePrefix){
				return this.toAliasTable(field)+".data_creazione";
			}else{
				return "data_creazione";
			}
		}
		if(field.equals(Avviso.model().STATO)){
			if(appendTablePrefix){
				return this.toAliasTable(field)+".stato";
			}else{
				return "stato";
			}
		}
		if(field.equals(Avviso.model().PDF)){
			if(appendTablePrefix){
				return this.toAliasTable(field)+".pdf";
			}else{
				return "pdf";
			}
		}
		if(field.equals(Avviso.model().OPERAZIONE.ID_TRACCIATO.ID_TRACCIATO)){
			if(appendTablePrefix){
				return this.toAliasTable(field)+".id_tracciato";
			}else{
				return "id_tracciato";
			}
		}
		if(field.equals(Avviso.model().OPERAZIONE.TIPO_OPERAZIONE)){
			if(appendTablePrefix){
				return this.toAliasTable(field)+".tipo_operazione";
			}else{
				return "tipo_operazione";
			}
		}
		if(field.equals(Avviso.model().OPERAZIONE.LINEA_ELABORAZIONE)){
			if(appendTablePrefix){
				return this.toAliasTable(field)+".linea_elaborazione";
			}else{
				return "linea_elaborazione";
			}
		}
		if(field.equals(Avviso.model().OPERAZIONE.STATO)){
			if(appendTablePrefix){
				return this.toAliasTable(field)+".stato";
			}else{
				return "stato";
			}
		}
		if(field.equals(Avviso.model().OPERAZIONE.DATI_RICHIESTA)){
			if(appendTablePrefix){
				return this.toAliasTable(field)+".dati_richiesta";
			}else{
				return "dati_richiesta";
			}
		}
		if(field.equals(Avviso.model().OPERAZIONE.DATI_RISPOSTA)){
			if(appendTablePrefix){
				return this.toAliasTable(field)+".dati_risposta";
			}else{
				return "dati_risposta";
			}
		}
		if(field.equals(Avviso.model().OPERAZIONE.DETTAGLIO_ESITO)){
			if(appendTablePrefix){
				return this.toAliasTable(field)+".dettaglio_esito";
			}else{
				return "dettaglio_esito";
			}
		}
		if(field.equals(Avviso.model().OPERAZIONE.ID_APPLICAZIONE.COD_APPLICAZIONE)){
			if(appendTablePrefix){
				return this.toAliasTable(field)+".cod_applicazione";
			}else{
				return "cod_applicazione";
			}
		}
		if(field.equals(Avviso.model().OPERAZIONE.COD_VERSAMENTO_ENTE)){
			if(appendTablePrefix){
				return this.toAliasTable(field)+".cod_versamento_ente";
			}else{
				return "cod_versamento_ente";
			}
		}
		if(field.equals(Avviso.model().OPERAZIONE.COD_DOMINIO)){
			if(appendTablePrefix){
				return this.toAliasTable(field)+".cod_dominio";
			}else{
				return "cod_dominio";
			}
		}
		if(field.equals(Avviso.model().OPERAZIONE.IUV)){
			if(appendTablePrefix){
				return this.toAliasTable(field)+".iuv";
			}else{
				return "iuv";
			}
		}
		if(field.equals(Avviso.model().OPERAZIONE.TRN)){
			if(appendTablePrefix){
				return this.toAliasTable(field)+".trn";
			}else{
				return "trn";
			}
		}


		return super.toColumn(field,returnAlias,appendTablePrefix);
		
	}
	
	@Override
	public String toTable(IField field,boolean returnAlias) throws ExpressionException {
		
		// In the case of table with alias, using parameter returnAlias​​, 
		// it is possible to drive the choice whether to return only the alias or 
		// the full definition of the table containing the alias
		
		if(field.equals(Avviso.model().COD_DOMINIO)){
			return this.toTable(Avviso.model(), returnAlias);
		}
		if(field.equals(Avviso.model().IUV)){
			return this.toTable(Avviso.model(), returnAlias);
		}
		if(field.equals(Avviso.model().DATA_CREAZIONE)){
			return this.toTable(Avviso.model(), returnAlias);
		}
		if(field.equals(Avviso.model().STATO)){
			return this.toTable(Avviso.model(), returnAlias);
		}
		if(field.equals(Avviso.model().PDF)){
			return this.toTable(Avviso.model(), returnAlias);
		}
		if(field.equals(Avviso.model().OPERAZIONE.ID_TRACCIATO.ID_TRACCIATO)){
			return this.toTable(Avviso.model().OPERAZIONE.ID_TRACCIATO, returnAlias);
		}
		if(field.equals(Avviso.model().OPERAZIONE.TIPO_OPERAZIONE)){
			return this.toTable(Avviso.model().OPERAZIONE, returnAlias);
		}
		if(field.equals(Avviso.model().OPERAZIONE.LINEA_ELABORAZIONE)){
			return this.toTable(Avviso.model().OPERAZIONE, returnAlias);
		}
		if(field.equals(Avviso.model().OPERAZIONE.STATO)){
			return this.toTable(Avviso.model().OPERAZIONE, returnAlias);
		}
		if(field.equals(Avviso.model().OPERAZIONE.DATI_RICHIESTA)){
			return this.toTable(Avviso.model().OPERAZIONE, returnAlias);
		}
		if(field.equals(Avviso.model().OPERAZIONE.DATI_RISPOSTA)){
			return this.toTable(Avviso.model().OPERAZIONE, returnAlias);
		}
		if(field.equals(Avviso.model().OPERAZIONE.DETTAGLIO_ESITO)){
			return this.toTable(Avviso.model().OPERAZIONE, returnAlias);
		}
		if(field.equals(Avviso.model().OPERAZIONE.ID_APPLICAZIONE.COD_APPLICAZIONE)){
			return this.toTable(Avviso.model().OPERAZIONE.ID_APPLICAZIONE, returnAlias);
		}
		if(field.equals(Avviso.model().OPERAZIONE.COD_VERSAMENTO_ENTE)){
			return this.toTable(Avviso.model().OPERAZIONE, returnAlias);
		}
		if(field.equals(Avviso.model().OPERAZIONE.COD_DOMINIO)){
			return this.toTable(Avviso.model().OPERAZIONE, returnAlias);
		}
		if(field.equals(Avviso.model().OPERAZIONE.IUV)){
			return this.toTable(Avviso.model().OPERAZIONE, returnAlias);
		}
		if(field.equals(Avviso.model().OPERAZIONE.TRN)){
			return this.toTable(Avviso.model().OPERAZIONE, returnAlias);
		}


		return super.toTable(field,returnAlias);
		
	}

	@Override
	public String toTable(IModel<?> model,boolean returnAlias) throws ExpressionException {
		
		// In the case of table with alias, using parameter returnAlias​​, 
		// it is possible to drive the choice whether to return only the alias or 
		// the full definition of the table containing the alias
		
		if(model.equals(Avviso.model())){
			return "avvisi";
		}
		if(model.equals(Avviso.model().OPERAZIONE)){
			return "operazioni";
		}
		if(model.equals(Avviso.model().OPERAZIONE.ID_TRACCIATO)){
			return "tracciati";
		}
		if(model.equals(Avviso.model().OPERAZIONE.ID_APPLICAZIONE)){
			return "applicazioni";
		}


		return super.toTable(model,returnAlias);
		
	}

}
