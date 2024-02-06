/*
 * GovPay - Porta di Accesso al Nodo dei Pagamenti SPC 
 * http://www.gov4j.it/govpay
 * 
 * Copyright (c) 2014-2024 Link.it srl (http://www.link.it).
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

import it.govpay.orm.VistaVersamentoAca;


/**     
 * VistaVersamentoAcaFieldConverter
 *
 * @author Giovanni Bussu (bussu@link.it)
 * @author Lorenzo Nardi (nardi@link.it)
 * @author $Author$
 * @version $Rev$, $Date$
 */
public class VistaVersamentoAcaFieldConverter extends AbstractSQLFieldConverter {

	private TipiDatabase databaseType;
	
	public VistaVersamentoAcaFieldConverter(String databaseType){
		this.databaseType = TipiDatabase.toEnumConstant(databaseType);
	}
	public VistaVersamentoAcaFieldConverter(TipiDatabase databaseType){
		this.databaseType = databaseType;
	}


	@Override
	public IModel<?> getRootModel() throws ExpressionException {
		return VistaVersamentoAca.model();
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
		
		if(field.equals(VistaVersamentoAca.model().COD_VERSAMENTO_ENTE)){
			if(appendTablePrefix){
				return this.toAliasTable(field)+".cod_versamento_ente";
			}else{
				return "cod_versamento_ente";
			}
		}
		if(field.equals(VistaVersamentoAca.model().COD_DOMINIO)){
			if(appendTablePrefix){
				return this.toAliasTable(field)+".cod_dominio";
			}else{
				return "cod_dominio";
			}
		}
		if(field.equals(VistaVersamentoAca.model().COD_APPLICAZIONE)){
			if(appendTablePrefix){
				return this.toAliasTable(field)+".cod_applicazione";
			}else{
				return "cod_applicazione";
			}
		}
		if(field.equals(VistaVersamentoAca.model().IMPORTO_TOTALE)){
			if(appendTablePrefix){
				return this.toAliasTable(field)+".importo_totale";
			}else{
				return "importo_totale";
			}
		}
		if(field.equals(VistaVersamentoAca.model().STATO_VERSAMENTO)){
			if(appendTablePrefix){
				return this.toAliasTable(field)+".stato_versamento";
			}else{
				return "stato_versamento";
			}
		}
		if(field.equals(VistaVersamentoAca.model().DATA_VALIDITA)){
			if(appendTablePrefix){
				return this.toAliasTable(field)+".data_validita";
			}else{
				return "data_validita";
			}
		}
		if(field.equals(VistaVersamentoAca.model().DATA_SCADENZA)){
			if(appendTablePrefix){
				return this.toAliasTable(field)+".data_scadenza";
			}else{
				return "data_scadenza";
			}
		}
		if(field.equals(VistaVersamentoAca.model().CAUSALE_VERSAMENTO)){
			if(appendTablePrefix){
				return this.toAliasTable(field)+".causale_versamento";
			}else{
				return "causale_versamento";
			}
		}
		if(field.equals(VistaVersamentoAca.model().DEBITORE_TIPO)){
			if(appendTablePrefix){
				return this.toAliasTable(field)+".debitore_tipo";
			}else{
				return "debitore_tipo";
			}
		}
		if(field.equals(VistaVersamentoAca.model().DEBITORE_IDENTIFICATIVO)){
			if(appendTablePrefix){
				return this.toAliasTable(field)+".debitore_identificativo";
			}else{
				return "debitore_identificativo";
			}
		}
		if(field.equals(VistaVersamentoAca.model().DEBITORE_ANAGRAFICA)){
			if(appendTablePrefix){
				return this.toAliasTable(field)+".debitore_anagrafica";
			}else{
				return "debitore_anagrafica";
			}
		}
		if(field.equals(VistaVersamentoAca.model().IUV_VERSAMENTO)){
			if(appendTablePrefix){
				return this.toAliasTable(field)+".iuv_versamento";
			}else{
				return "iuv_versamento";
			}
		}
		if(field.equals(VistaVersamentoAca.model().NUMERO_AVVISO)){
			if(appendTablePrefix){
				return this.toAliasTable(field)+".numero_avviso";
			}else{
				return "numero_avviso";
			}
		}
		if(field.equals(VistaVersamentoAca.model().DATA_ULTIMA_MODIFICA_ACA)){
			if(appendTablePrefix){
				return this.toAliasTable(field)+".data_ultima_modifica_aca";
			}else{
				return "data_ultima_modifica_aca";
			}
		}
		if(field.equals(VistaVersamentoAca.model().DATA_ULTIMA_COMUNICAZIONE_ACA)){
			if(appendTablePrefix){
				return this.toAliasTable(field)+".data_ultima_comunicazione_aca";
			}else{
				return "data_ultima_comunicazione_aca";
			}
		}


		return super.toColumn(field,returnAlias,appendTablePrefix);
		
	}
	
	@Override
	public String toTable(IField field,boolean returnAlias) throws ExpressionException {
		
		// In the case of table with alias, using parameter returnAlias​​, 
		// it is possible to drive the choice whether to return only the alias or 
		// the full definition of the table containing the alias
		
		if(field.equals(VistaVersamentoAca.model().COD_VERSAMENTO_ENTE)){
			return this.toTable(VistaVersamentoAca.model(), returnAlias);
		}
		if(field.equals(VistaVersamentoAca.model().COD_DOMINIO)){
			return this.toTable(VistaVersamentoAca.model(), returnAlias);
		}
		if(field.equals(VistaVersamentoAca.model().COD_APPLICAZIONE)){
			return this.toTable(VistaVersamentoAca.model(), returnAlias);
		}
		if(field.equals(VistaVersamentoAca.model().IMPORTO_TOTALE)){
			return this.toTable(VistaVersamentoAca.model(), returnAlias);
		}
		if(field.equals(VistaVersamentoAca.model().STATO_VERSAMENTO)){
			return this.toTable(VistaVersamentoAca.model(), returnAlias);
		}
		if(field.equals(VistaVersamentoAca.model().DATA_VALIDITA)){
			return this.toTable(VistaVersamentoAca.model(), returnAlias);
		}
		if(field.equals(VistaVersamentoAca.model().DATA_SCADENZA)){
			return this.toTable(VistaVersamentoAca.model(), returnAlias);
		}
		if(field.equals(VistaVersamentoAca.model().CAUSALE_VERSAMENTO)){
			return this.toTable(VistaVersamentoAca.model(), returnAlias);
		}
		if(field.equals(VistaVersamentoAca.model().DEBITORE_TIPO)){
			return this.toTable(VistaVersamentoAca.model(), returnAlias);
		}
		if(field.equals(VistaVersamentoAca.model().DEBITORE_IDENTIFICATIVO)){
			return this.toTable(VistaVersamentoAca.model(), returnAlias);
		}
		if(field.equals(VistaVersamentoAca.model().DEBITORE_ANAGRAFICA)){
			return this.toTable(VistaVersamentoAca.model(), returnAlias);
		}
		if(field.equals(VistaVersamentoAca.model().IUV_VERSAMENTO)){
			return this.toTable(VistaVersamentoAca.model(), returnAlias);
		}
		if(field.equals(VistaVersamentoAca.model().NUMERO_AVVISO)){
			return this.toTable(VistaVersamentoAca.model(), returnAlias);
		}
		if(field.equals(VistaVersamentoAca.model().DATA_ULTIMA_MODIFICA_ACA)){
			return this.toTable(VistaVersamentoAca.model(), returnAlias);
		}
		if(field.equals(VistaVersamentoAca.model().DATA_ULTIMA_COMUNICAZIONE_ACA)){
			return this.toTable(VistaVersamentoAca.model(), returnAlias);
		}


		return super.toTable(field,returnAlias);
		
	}

	@Override
	public String toTable(IModel<?> model,boolean returnAlias) throws ExpressionException {
		
		// In the case of table with alias, using parameter returnAlias​​, 
		// it is possible to drive the choice whether to return only the alias or 
		// the full definition of the table containing the alias
		
		if(model.equals(VistaVersamentoAca.model())){
			return "v_versamenti_aca";
		}


		return super.toTable(model,returnAlias);
		
	}

}
