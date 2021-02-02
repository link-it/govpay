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

import it.govpay.orm.TracciatoMyPivot;


/**     
 * TracciatoMyPivotFieldConverter
 *
 * @author Giovanni Bussu (bussu@link.it)
 * @author Lorenzo Nardi (nardi@link.it)
 * @author $Author$
 * @version $Rev$, $Date$
 */
public class TracciatoMyPivotFieldConverter extends AbstractSQLFieldConverter {

	private TipiDatabase databaseType;
	
	public TracciatoMyPivotFieldConverter(String databaseType){
		this.databaseType = TipiDatabase.toEnumConstant(databaseType);
	}
	public TracciatoMyPivotFieldConverter(TipiDatabase databaseType){
		this.databaseType = databaseType;
	}


	@Override
	public IModel<?> getRootModel() throws ExpressionException {
		return TracciatoMyPivot.model();
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
		
		if(field.equals(TracciatoMyPivot.model().ID_DOMINIO.COD_DOMINIO)){
			if(appendTablePrefix){
				return this.toAliasTable(field)+".cod_dominio";
			}else{
				return "cod_dominio";
			}
		}
		if(field.equals(TracciatoMyPivot.model().NOME_FILE)){
			if(appendTablePrefix){
				return this.toAliasTable(field)+".nome_file";
			}else{
				return "nome_file";
			}
		}
		if(field.equals(TracciatoMyPivot.model().STATO)){
			if(appendTablePrefix){
				return this.toAliasTable(field)+".stato";
			}else{
				return "stato";
			}
		}
		if(field.equals(TracciatoMyPivot.model().DATA_CREAZIONE)){
			if(appendTablePrefix){
				return this.toAliasTable(field)+".data_creazione";
			}else{
				return "data_creazione";
			}
		}
		if(field.equals(TracciatoMyPivot.model().DATA_RT_DA)){
			if(appendTablePrefix){
				return this.toAliasTable(field)+".data_rt_da";
			}else{
				return "data_rt_da";
			}
		}
		if(field.equals(TracciatoMyPivot.model().DATA_RT_A)){
			if(appendTablePrefix){
				return this.toAliasTable(field)+".data_rt_a";
			}else{
				return "data_rt_a";
			}
		}
		if(field.equals(TracciatoMyPivot.model().DATA_CARICAMENTO)){
			if(appendTablePrefix){
				return this.toAliasTable(field)+".data_caricamento";
			}else{
				return "data_caricamento";
			}
		}
		if(field.equals(TracciatoMyPivot.model().DATA_COMPLETAMENTO)){
			if(appendTablePrefix){
				return this.toAliasTable(field)+".data_completamento";
			}else{
				return "data_completamento";
			}
		}
		if(field.equals(TracciatoMyPivot.model().REQUEST_TOKEN)){
			if(appendTablePrefix){
				return this.toAliasTable(field)+".request_token";
			}else{
				return "request_token";
			}
		}
		if(field.equals(TracciatoMyPivot.model().UPLOAD_URL)){
			if(appendTablePrefix){
				return this.toAliasTable(field)+".upload_url";
			}else{
				return "upload_url";
			}
		}
		if(field.equals(TracciatoMyPivot.model().AUTHORIZATION_TOKEN)){
			if(appendTablePrefix){
				return this.toAliasTable(field)+".authorization_token";
			}else{
				return "authorization_token";
			}
		}
		if(field.equals(TracciatoMyPivot.model().RAW_CONTENUTO)){
			if(appendTablePrefix){
				return this.toAliasTable(field)+".raw_contenuto";
			}else{
				return "raw_contenuto";
			}
		}
		if(field.equals(TracciatoMyPivot.model().BEAN_DATI)){
			if(appendTablePrefix){
				return this.toAliasTable(field)+".bean_dati";
			}else{
				return "bean_dati";
			}
		}


		return super.toColumn(field,returnAlias,appendTablePrefix);
		
	}
	
	@Override
	public String toTable(IField field,boolean returnAlias) throws ExpressionException {
		
		// In the case of table with alias, using parameter returnAlias​​, 
		// it is possible to drive the choice whether to return only the alias or 
		// the full definition of the table containing the alias
		
		if(field.equals(TracciatoMyPivot.model().ID_DOMINIO.COD_DOMINIO)){
			return this.toTable(TracciatoMyPivot.model().ID_DOMINIO, returnAlias);
		}
		if(field.equals(TracciatoMyPivot.model().NOME_FILE)){
			return this.toTable(TracciatoMyPivot.model(), returnAlias);
		}
		if(field.equals(TracciatoMyPivot.model().STATO)){
			return this.toTable(TracciatoMyPivot.model(), returnAlias);
		}
		if(field.equals(TracciatoMyPivot.model().DATA_CREAZIONE)){
			return this.toTable(TracciatoMyPivot.model(), returnAlias);
		}
		if(field.equals(TracciatoMyPivot.model().DATA_RT_DA)){
			return this.toTable(TracciatoMyPivot.model(), returnAlias);
		}
		if(field.equals(TracciatoMyPivot.model().DATA_RT_A)){
			return this.toTable(TracciatoMyPivot.model(), returnAlias);
		}
		if(field.equals(TracciatoMyPivot.model().DATA_CARICAMENTO)){
			return this.toTable(TracciatoMyPivot.model(), returnAlias);
		}
		if(field.equals(TracciatoMyPivot.model().DATA_COMPLETAMENTO)){
			return this.toTable(TracciatoMyPivot.model(), returnAlias);
		}
		if(field.equals(TracciatoMyPivot.model().REQUEST_TOKEN)){
			return this.toTable(TracciatoMyPivot.model(), returnAlias);
		}
		if(field.equals(TracciatoMyPivot.model().UPLOAD_URL)){
			return this.toTable(TracciatoMyPivot.model(), returnAlias);
		}
		if(field.equals(TracciatoMyPivot.model().AUTHORIZATION_TOKEN)){
			return this.toTable(TracciatoMyPivot.model(), returnAlias);
		}
		if(field.equals(TracciatoMyPivot.model().RAW_CONTENUTO)){
			return this.toTable(TracciatoMyPivot.model(), returnAlias);
		}
		if(field.equals(TracciatoMyPivot.model().BEAN_DATI)){
			return this.toTable(TracciatoMyPivot.model(), returnAlias);
		}


		return super.toTable(field,returnAlias);
		
	}

	@Override
	public String toTable(IModel<?> model,boolean returnAlias) throws ExpressionException {
		
		// In the case of table with alias, using parameter returnAlias​​, 
		// it is possible to drive the choice whether to return only the alias or 
		// the full definition of the table containing the alias
		
		if(model.equals(TracciatoMyPivot.model())){
			return "mypivot_notifiche_pag";
		}
		if(model.equals(TracciatoMyPivot.model().ID_DOMINIO)){
			return "domini";
		}


		return super.toTable(model,returnAlias);
		
	}

}
