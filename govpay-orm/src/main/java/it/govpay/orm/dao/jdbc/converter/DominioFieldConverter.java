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

import it.govpay.orm.Dominio;


/**     
 * DominioFieldConverter
 *
 * @author Giovanni Bussu (bussu@link.it)
 * @author Lorenzo Nardi (nardi@link.it)
 * @author $Author$
 * @version $Rev$, $Date$
 */
public class DominioFieldConverter extends AbstractSQLFieldConverter {

	private TipiDatabase databaseType;
	
	public DominioFieldConverter(String databaseType){
		this.databaseType = TipiDatabase.toEnumConstant(databaseType);
	}
	public DominioFieldConverter(TipiDatabase databaseType){
		this.databaseType = databaseType;
	}


	@Override
	public IModel<?> getRootModel() throws ExpressionException {
		return Dominio.model();
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
		
		if(field.equals(Dominio.model().ID_STAZIONE.COD_STAZIONE)){
			if(appendTablePrefix){
				return this.toAliasTable(field)+".cod_stazione";
			}else{
				return "cod_stazione";
			}
		}
		if(field.equals(Dominio.model().COD_DOMINIO)){
			if(appendTablePrefix){
				return this.toAliasTable(field)+".cod_dominio";
			}else{
				return "cod_dominio";
			}
		}
		if(field.equals(Dominio.model().GLN)){
			if(appendTablePrefix){
				return this.toAliasTable(field)+".gln";
			}else{
				return "gln";
			}
		}
		if(field.equals(Dominio.model().ABILITATO)){
			if(appendTablePrefix){
				return this.toAliasTable(field)+".abilitato";
			}else{
				return "abilitato";
			}
		}
		if(field.equals(Dominio.model().RAGIONE_SOCIALE)){
			if(appendTablePrefix){
				return this.toAliasTable(field)+".ragione_sociale";
			}else{
				return "ragione_sociale";
			}
		}
		if(field.equals(Dominio.model().XML_CONTI_ACCREDITO)){
			if(appendTablePrefix){
				return this.toAliasTable(field)+".xml_conti_accredito";
			}else{
				return "xml_conti_accredito";
			}
		}
		if(field.equals(Dominio.model().XML_TABELLA_CONTROPARTI)){
			if(appendTablePrefix){
				return this.toAliasTable(field)+".xml_tabella_controparti";
			}else{
				return "xml_tabella_controparti";
			}
		}
		if(field.equals(Dominio.model().RIUSO_IUV)){
			if(appendTablePrefix){
				return this.toAliasTable(field)+".riuso_iuv";
			}else{
				return "riuso_iuv";
			}
		}
		if(field.equals(Dominio.model().CUSTOM_IUV)){
			if(appendTablePrefix){
				return this.toAliasTable(field)+".custom_iuv";
			}else{
				return "custom_iuv";
			}
		}
		if(field.equals(Dominio.model().ID_APPLICAZIONE_DEFAULT.COD_APPLICAZIONE)){
			if(appendTablePrefix){
				return this.toAliasTable(field)+".cod_applicazione";
			}else{
				return "cod_applicazione";
			}
		}
		if(field.equals(Dominio.model().AUX_DIGIT)){
			if(appendTablePrefix){
				return this.toAliasTable(field)+".aux_digit";
			}else{
				return "aux_digit";
			}
		}
		if(field.equals(Dominio.model().IUV_PREFIX)){
			if(appendTablePrefix){
				return this.toAliasTable(field)+".iuv_prefix";
			}else{
				return "iuv_prefix";
			}
		}
		if(field.equals(Dominio.model().IUV_PREFIX_STRICT)){
			if(appendTablePrefix){
				return this.toAliasTable(field)+".iuv_prefix_strict";
			}else{
				return "iuv_prefix_strict";
			}
		}
		if(field.equals(Dominio.model().SEGREGATION_CODE)){
			if(appendTablePrefix){
				return this.toAliasTable(field)+".segregation_code";
			}else{
				return "segregation_code";
			}
		}


		return super.toColumn(field,returnAlias,appendTablePrefix);
		
	}
	
	@Override
	public String toTable(IField field,boolean returnAlias) throws ExpressionException {
		
		// In the case of table with alias, using parameter returnAlias​​, 
		// it is possible to drive the choice whether to return only the alias or 
		// the full definition of the table containing the alias
		
		if(field.equals(Dominio.model().ID_STAZIONE.COD_STAZIONE)){
			return this.toTable(Dominio.model().ID_STAZIONE, returnAlias);
		}
		if(field.equals(Dominio.model().COD_DOMINIO)){
			return this.toTable(Dominio.model(), returnAlias);
		}
		if(field.equals(Dominio.model().GLN)){
			return this.toTable(Dominio.model(), returnAlias);
		}
		if(field.equals(Dominio.model().ABILITATO)){
			return this.toTable(Dominio.model(), returnAlias);
		}
		if(field.equals(Dominio.model().RAGIONE_SOCIALE)){
			return this.toTable(Dominio.model(), returnAlias);
		}
		if(field.equals(Dominio.model().XML_CONTI_ACCREDITO)){
			return this.toTable(Dominio.model(), returnAlias);
		}
		if(field.equals(Dominio.model().XML_TABELLA_CONTROPARTI)){
			return this.toTable(Dominio.model(), returnAlias);
		}
		if(field.equals(Dominio.model().RIUSO_IUV)){
			return this.toTable(Dominio.model(), returnAlias);
		}
		if(field.equals(Dominio.model().CUSTOM_IUV)){
			return this.toTable(Dominio.model(), returnAlias);
		}
		if(field.equals(Dominio.model().ID_APPLICAZIONE_DEFAULT.COD_APPLICAZIONE)){
			return this.toTable(Dominio.model().ID_APPLICAZIONE_DEFAULT, returnAlias);
		}
		if(field.equals(Dominio.model().AUX_DIGIT)){
			return this.toTable(Dominio.model(), returnAlias);
		}
		if(field.equals(Dominio.model().IUV_PREFIX)){
			return this.toTable(Dominio.model(), returnAlias);
		}
		if(field.equals(Dominio.model().IUV_PREFIX_STRICT)){
			return this.toTable(Dominio.model(), returnAlias);
		}
		if(field.equals(Dominio.model().SEGREGATION_CODE)){
			return this.toTable(Dominio.model(), returnAlias);
		}


		return super.toTable(field,returnAlias);
		
	}

	@Override
	public String toTable(IModel<?> model,boolean returnAlias) throws ExpressionException {
		
		// In the case of table with alias, using parameter returnAlias​​, 
		// it is possible to drive the choice whether to return only the alias or 
		// the full definition of the table containing the alias
		
		if(model.equals(Dominio.model())){
			return "domini";
		}
		if(model.equals(Dominio.model().ID_STAZIONE)){
			return "stazioni";
		}
		if(model.equals(Dominio.model().ID_APPLICAZIONE_DEFAULT)){
			return "applicazioni";
		}


		return super.toTable(model,returnAlias);
		
	}

}
