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

import it.govpay.orm.Rendicontazione;


/**     
 * RendicontazioneFieldConverter
 *
 * @author Giovanni Bussu (bussu@link.it)
 * @author Lorenzo Nardi (nardi@link.it)
 * @author $Author$
 * @version $Rev$, $Date$
 */
public class RendicontazioneFieldConverter extends AbstractSQLFieldConverter {

	private TipiDatabase databaseType;
	
	public RendicontazioneFieldConverter(String databaseType){
		this.databaseType = TipiDatabase.toEnumConstant(databaseType);
	}
	public RendicontazioneFieldConverter(TipiDatabase databaseType){
		this.databaseType = databaseType;
	}


	@Override
	public IModel<?> getRootModel() throws ExpressionException {
		return Rendicontazione.model();
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
		
		if(field.equals(Rendicontazione.model().IUV)){
			if(appendTablePrefix){
				return this.toAliasTable(field)+".iuv";
			}else{
				return "iuv";
			}
		}
		if(field.equals(Rendicontazione.model().IUR)){
			if(appendTablePrefix){
				return this.toAliasTable(field)+".iur";
			}else{
				return "iur";
			}
		}
		if(field.equals(Rendicontazione.model().IMPORTO_PAGATO)){
			if(appendTablePrefix){
				return this.toAliasTable(field)+".importo_pagato";
			}else{
				return "importo_pagato";
			}
		}
		if(field.equals(Rendicontazione.model().ESITO)){
			if(appendTablePrefix){
				return this.toAliasTable(field)+".esito";
			}else{
				return "esito";
			}
		}
		if(field.equals(Rendicontazione.model().DATA)){
			if(appendTablePrefix){
				return this.toAliasTable(field)+".data";
			}else{
				return "data";
			}
		}
		if(field.equals(Rendicontazione.model().STATO)){
			if(appendTablePrefix){
				return this.toAliasTable(field)+".stato";
			}else{
				return "stato";
			}
		}
		if(field.equals(Rendicontazione.model().ANOMALIE)){
			if(appendTablePrefix){
				return this.toAliasTable(field)+".anomalie";
			}else{
				return "anomalie";
			}
		}
		if(field.equals(Rendicontazione.model().ID_FR.COD_FLUSSO)){
			if(appendTablePrefix){
				return this.toAliasTable(field)+".cod_flusso";
			}else{
				return "cod_flusso";
			}
		}
		if(field.equals(Rendicontazione.model().ID_FR.ANNO_RIFERIMENTO)){
			if(appendTablePrefix){
				return this.toAliasTable(field)+".anno_riferimento";
			}else{
				return "anno_riferimento";
			}
		}
		if(field.equals(Rendicontazione.model().ID_PAGAMENTO.ID_PAGAMENTO)){
			if(appendTablePrefix){
				return this.toAliasTable(field)+".id_pagamento";
			}else{
				return "id_pagamento";
			}
		}


		return super.toColumn(field,returnAlias,appendTablePrefix);
		
	}
	
	@Override
	public String toTable(IField field,boolean returnAlias) throws ExpressionException {
		
		// In the case of table with alias, using parameter returnAlias​​, 
		// it is possible to drive the choice whether to return only the alias or 
		// the full definition of the table containing the alias
		
		if(field.equals(Rendicontazione.model().IUV)){
			return this.toTable(Rendicontazione.model(), returnAlias);
		}
		if(field.equals(Rendicontazione.model().IUR)){
			return this.toTable(Rendicontazione.model(), returnAlias);
		}
		if(field.equals(Rendicontazione.model().IMPORTO_PAGATO)){
			return this.toTable(Rendicontazione.model(), returnAlias);
		}
		if(field.equals(Rendicontazione.model().ESITO)){
			return this.toTable(Rendicontazione.model(), returnAlias);
		}
		if(field.equals(Rendicontazione.model().DATA)){
			return this.toTable(Rendicontazione.model(), returnAlias);
		}
		if(field.equals(Rendicontazione.model().STATO)){
			return this.toTable(Rendicontazione.model(), returnAlias);
		}
		if(field.equals(Rendicontazione.model().ANOMALIE)){
			return this.toTable(Rendicontazione.model(), returnAlias);
		}
		if(field.equals(Rendicontazione.model().ID_FR.COD_FLUSSO)){
			return this.toTable(Rendicontazione.model().ID_FR, returnAlias);
		}
		if(field.equals(Rendicontazione.model().ID_FR.ANNO_RIFERIMENTO)){
			return this.toTable(Rendicontazione.model().ID_FR, returnAlias);
		}
		if(field.equals(Rendicontazione.model().ID_PAGAMENTO.ID_PAGAMENTO)){
			return this.toTable(Rendicontazione.model().ID_PAGAMENTO, returnAlias);
		}


		return super.toTable(field,returnAlias);
		
	}

	@Override
	public String toTable(IModel<?> model,boolean returnAlias) throws ExpressionException {
		
		// In the case of table with alias, using parameter returnAlias​​, 
		// it is possible to drive the choice whether to return only the alias or 
		// the full definition of the table containing the alias
		
		if(model.equals(Rendicontazione.model())){
			return "rendicontazioni";
		}
		if(model.equals(Rendicontazione.model().ID_FR)){
			return "fr";
		}
		if(model.equals(Rendicontazione.model().ID_PAGAMENTO)){
			return "pagamenti";
		}


		return super.toTable(model,returnAlias);
		
	}

}
