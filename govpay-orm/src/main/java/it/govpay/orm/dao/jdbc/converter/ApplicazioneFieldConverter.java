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

import it.govpay.orm.Applicazione;


/**     
 * ApplicazioneFieldConverter
 *
 * @author Giovanni Bussu (bussu@link.it)
 * @author Lorenzo Nardi (nardi@link.it)
 * @author $Author$
 * @version $Rev$, $Date$
 */
public class ApplicazioneFieldConverter extends AbstractSQLFieldConverter {

	private TipiDatabase databaseType;
	
	public ApplicazioneFieldConverter(String databaseType){
		this.databaseType = TipiDatabase.toEnumConstant(databaseType);
	}
	public ApplicazioneFieldConverter(TipiDatabase databaseType){
		this.databaseType = databaseType;
	}


	@Override
	public IModel<?> getRootModel() throws ExpressionException {
		return Applicazione.model();
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
		
		if(field.equals(Applicazione.model().COD_APPLICAZIONE)){
			if(appendTablePrefix){
				return this.toAliasTable(field)+".cod_applicazione";
			}else{
				return "cod_applicazione";
			}
		}
		if(field.equals(Applicazione.model().ABILITATO)){
			if(appendTablePrefix){
				return this.toAliasTable(field)+".abilitato";
			}else{
				return "abilitato";
			}
		}
		if(field.equals(Applicazione.model().PRINCIPAL)){
			if(appendTablePrefix){
				return this.toAliasTable(field)+".principal";
			}else{
				return "principal";
			}
		}
		if(field.equals(Applicazione.model().FIRMA_RICEVUTA)){
			if(appendTablePrefix){
				return this.toAliasTable(field)+".firma_ricevuta";
			}else{
				return "firma_ricevuta";
			}
		}
		if(field.equals(Applicazione.model().COD_CONNETTORE_ESITO)){
			if(appendTablePrefix){
				return this.toAliasTable(field)+".cod_connettore_esito";
			}else{
				return "cod_connettore_esito";
			}
		}
		if(field.equals(Applicazione.model().COD_CONNETTORE_VERIFICA)){
			if(appendTablePrefix){
				return this.toAliasTable(field)+".cod_connettore_verifica";
			}else{
				return "cod_connettore_verifica";
			}
		}
		if(field.equals(Applicazione.model().VERSIONE)){
			if(appendTablePrefix){
				return this.toAliasTable(field)+".versione";
			}else{
				return "versione";
			}
		}
		if(field.equals(Applicazione.model().TRUSTED)){
			if(appendTablePrefix){
				return this.toAliasTable(field)+".trusted";
			}else{
				return "trusted";
			}
		}
		if(field.equals(Applicazione.model().COD_APPLICAZIONE_IUV)){
			if(appendTablePrefix){
				return this.toAliasTable(field)+".cod_applicazione_iuv";
			}else{
				return "cod_applicazione_iuv";
			}
		}


		return super.toColumn(field,returnAlias,appendTablePrefix);
		
	}
	
	@Override
	public String toTable(IField field,boolean returnAlias) throws ExpressionException {
		
		// In the case of table with alias, using parameter returnAlias​​, 
		// it is possible to drive the choice whether to return only the alias or 
		// the full definition of the table containing the alias
		
		if(field.equals(Applicazione.model().COD_APPLICAZIONE)){
			return this.toTable(Applicazione.model(), returnAlias);
		}
		if(field.equals(Applicazione.model().ABILITATO)){
			return this.toTable(Applicazione.model(), returnAlias);
		}
		if(field.equals(Applicazione.model().PRINCIPAL)){
			return this.toTable(Applicazione.model(), returnAlias);
		}
		if(field.equals(Applicazione.model().FIRMA_RICEVUTA)){
			return this.toTable(Applicazione.model(), returnAlias);
		}
		if(field.equals(Applicazione.model().COD_CONNETTORE_ESITO)){
			return this.toTable(Applicazione.model(), returnAlias);
		}
		if(field.equals(Applicazione.model().COD_CONNETTORE_VERIFICA)){
			return this.toTable(Applicazione.model(), returnAlias);
		}
		if(field.equals(Applicazione.model().VERSIONE)){
			return this.toTable(Applicazione.model(), returnAlias);
		}
		if(field.equals(Applicazione.model().TRUSTED)){
			return this.toTable(Applicazione.model(), returnAlias);
		}
		if(field.equals(Applicazione.model().COD_APPLICAZIONE_IUV)){
			return this.toTable(Applicazione.model(), returnAlias);
		}


		return super.toTable(field,returnAlias);
		
	}

	@Override
	public String toTable(IModel<?> model,boolean returnAlias) throws ExpressionException {
		
		// In the case of table with alias, using parameter returnAlias​​, 
		// it is possible to drive the choice whether to return only the alias or 
		// the full definition of the table containing the alias
		
		if(model.equals(Applicazione.model())){
			return "applicazioni";
		}


		return super.toTable(model,returnAlias);
		
	}

}
