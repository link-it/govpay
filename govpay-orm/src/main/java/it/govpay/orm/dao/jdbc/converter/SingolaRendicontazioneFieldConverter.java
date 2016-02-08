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

import it.govpay.orm.SingolaRendicontazione;


/**     
 * SingolaRendicontazioneFieldConverter
 *
 * @author Giovanni Bussu (bussu@link.it)
 * @author Lorenzo Nardi (nardi@link.it)
 * @author $Author$
 * @version $Rev$, $Date$
 */
public class SingolaRendicontazioneFieldConverter extends AbstractSQLFieldConverter {

	private TipiDatabase databaseType;
	
	public SingolaRendicontazioneFieldConverter(String databaseType){
		this.databaseType = TipiDatabase.toEnumConstant(databaseType);
	}
	public SingolaRendicontazioneFieldConverter(TipiDatabase databaseType){
		this.databaseType = databaseType;
	}


	@Override
	public IModel<?> getRootModel() throws ExpressionException {
		return SingolaRendicontazione.model();
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
		
		if(field.equals(SingolaRendicontazione.model().ID_FR.COD_FLUSSO)){
			if(appendTablePrefix){
				return this.toAliasTable(field)+".cod_flusso";
			}else{
				return "cod_flusso";
			}
		}
		if(field.equals(SingolaRendicontazione.model().ID_FR.ANNO_RIFERIMENTO)){
			if(appendTablePrefix){
				return this.toAliasTable(field)+".anno_riferimento";
			}else{
				return "anno_riferimento";
			}
		}
		if(field.equals(SingolaRendicontazione.model().ID_SINGOLO_VERSAMENTO.ID_VERSAMENTO.COD_VERSAMENTO_ENTE)){
			if(appendTablePrefix){
				return this.toAliasTable(field)+".cod_versamento_ente";
			}else{
				return "cod_versamento_ente";
			}
		}
		if(field.equals(SingolaRendicontazione.model().ID_SINGOLO_VERSAMENTO.ID_VERSAMENTO.COD_DOMINIO)){
			if(appendTablePrefix){
				return this.toAliasTable(field)+".cod_dominio";
			}else{
				return "cod_dominio";
			}
		}
		if(field.equals(SingolaRendicontazione.model().ID_SINGOLO_VERSAMENTO.INDICE)){
			if(appendTablePrefix){
				return this.toAliasTable(field)+".indice";
			}else{
				return "indice";
			}
		}
		if(field.equals(SingolaRendicontazione.model().IUV)){
			if(appendTablePrefix){
				return this.toAliasTable(field)+".iuv";
			}else{
				return "iuv";
			}
		}
		if(field.equals(SingolaRendicontazione.model().IUR)){
			if(appendTablePrefix){
				return this.toAliasTable(field)+".iur";
			}else{
				return "iur";
			}
		}
		if(field.equals(SingolaRendicontazione.model().SINGOLO_IMPORTO)){
			if(appendTablePrefix){
				return this.toAliasTable(field)+".singolo_importo";
			}else{
				return "singolo_importo";
			}
		}
		if(field.equals(SingolaRendicontazione.model().CODICE_ESITO)){
			if(appendTablePrefix){
				return this.toAliasTable(field)+".codice_esito";
			}else{
				return "codice_esito";
			}
		}
		if(field.equals(SingolaRendicontazione.model().DATA_ESITO)){
			if(appendTablePrefix){
				return this.toAliasTable(field)+".data_esito";
			}else{
				return "data_esito";
			}
		}


		return super.toColumn(field,returnAlias,appendTablePrefix);
		
	}
	
	@Override
	public String toTable(IField field,boolean returnAlias) throws ExpressionException {
		
		// In the case of table with alias, using parameter returnAlias​​, 
		// it is possible to drive the choice whether to return only the alias or 
		// the full definition of the table containing the alias
		
		if(field.equals(SingolaRendicontazione.model().ID_FR.COD_FLUSSO)){
			return this.toTable(SingolaRendicontazione.model().ID_FR, returnAlias);
		}
		if(field.equals(SingolaRendicontazione.model().ID_FR.ANNO_RIFERIMENTO)){
			return this.toTable(SingolaRendicontazione.model().ID_FR, returnAlias);
		}
		if(field.equals(SingolaRendicontazione.model().ID_SINGOLO_VERSAMENTO.ID_VERSAMENTO.COD_VERSAMENTO_ENTE)){
			return this.toTable(SingolaRendicontazione.model().ID_SINGOLO_VERSAMENTO.ID_VERSAMENTO, returnAlias);
		}
		if(field.equals(SingolaRendicontazione.model().ID_SINGOLO_VERSAMENTO.ID_VERSAMENTO.COD_DOMINIO)){
			return this.toTable(SingolaRendicontazione.model().ID_SINGOLO_VERSAMENTO.ID_VERSAMENTO, returnAlias);
		}
		if(field.equals(SingolaRendicontazione.model().ID_SINGOLO_VERSAMENTO.INDICE)){
			return this.toTable(SingolaRendicontazione.model().ID_SINGOLO_VERSAMENTO, returnAlias);
		}
		if(field.equals(SingolaRendicontazione.model().IUV)){
			return this.toTable(SingolaRendicontazione.model(), returnAlias);
		}
		if(field.equals(SingolaRendicontazione.model().IUR)){
			return this.toTable(SingolaRendicontazione.model(), returnAlias);
		}
		if(field.equals(SingolaRendicontazione.model().SINGOLO_IMPORTO)){
			return this.toTable(SingolaRendicontazione.model(), returnAlias);
		}
		if(field.equals(SingolaRendicontazione.model().CODICE_ESITO)){
			return this.toTable(SingolaRendicontazione.model(), returnAlias);
		}
		if(field.equals(SingolaRendicontazione.model().DATA_ESITO)){
			return this.toTable(SingolaRendicontazione.model(), returnAlias);
		}


		return super.toTable(field,returnAlias);
		
	}

	@Override
	public String toTable(IModel<?> model,boolean returnAlias) throws ExpressionException {
		
		// In the case of table with alias, using parameter returnAlias​​, 
		// it is possible to drive the choice whether to return only the alias or 
		// the full definition of the table containing the alias
		
		if(model.equals(SingolaRendicontazione.model())){
			return "singole_rendicontazioni";
		}
		if(model.equals(SingolaRendicontazione.model().ID_FR)){
			return "fr";
		}
		if(model.equals(SingolaRendicontazione.model().ID_SINGOLO_VERSAMENTO)){
			return "singoli_versamenti";
		}
		if(model.equals(SingolaRendicontazione.model().ID_SINGOLO_VERSAMENTO.ID_VERSAMENTO)){
			return "id_versamento";
		}


		return super.toTable(model,returnAlias);
		
	}

}
