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

import it.govpay.orm.Evento;


/**     
 * EventoFieldConverter
 *
 * @author Giovanni Bussu (bussu@link.it)
 * @author Lorenzo Nardi (nardi@link.it)
 * @author $Author$
 * @version $Rev$, $Date$
 */
public class EventoFieldConverter extends AbstractSQLFieldConverter {

	private TipiDatabase databaseType;
	
	public EventoFieldConverter(String databaseType){
		this.databaseType = TipiDatabase.toEnumConstant(databaseType);
	}
	public EventoFieldConverter(TipiDatabase databaseType){
		this.databaseType = databaseType;
	}


	@Override
	public IModel<?> getRootModel() throws ExpressionException {
		return Evento.model();
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
		
		if(field.equals(Evento.model().COD_DOMINIO)){
			if(appendTablePrefix){
				return this.toAliasTable(field)+".cod_dominio";
			}else{
				return "cod_dominio";
			}
		}
		if(field.equals(Evento.model().IUV)){
			if(appendTablePrefix){
				return this.toAliasTable(field)+".iuv";
			}else{
				return "iuv";
			}
		}
		if(field.equals(Evento.model().CCP)){
			if(appendTablePrefix){
				return this.toAliasTable(field)+".ccp";
			}else{
				return "ccp";
			}
		}
		if(field.equals(Evento.model().CATEGORIA_EVENTO)){
			if(appendTablePrefix){
				return this.toAliasTable(field)+".categoria_evento";
			}else{
				return "categoria_evento";
			}
		}
		if(field.equals(Evento.model().TIPO_EVENTO)){
			if(appendTablePrefix){
				return this.toAliasTable(field)+".tipo_evento";
			}else{
				return "tipo_evento";
			}
		}
		if(field.equals(Evento.model().SOTTOTIPO_EVENTO)){
			if(appendTablePrefix){
				return this.toAliasTable(field)+".sottotipo_evento";
			}else{
				return "sottotipo_evento";
			}
		}
		if(field.equals(Evento.model().DATA)){
			if(appendTablePrefix){
				return this.toAliasTable(field)+".data";
			}else{
				return "data";
			}
		}
		if(field.equals(Evento.model().INTERVALLO)){
			if(appendTablePrefix){
				return this.toAliasTable(field)+".intervallo";
			}else{
				return "intervallo";
			}
		}
		if(field.equals(Evento.model().CLASSNAME_DETTAGLIO)){
			if(appendTablePrefix){
				return this.toAliasTable(field)+".classname_dettaglio";
			}else{
				return "classname_dettaglio";
			}
		}
		if(field.equals(Evento.model().DETTAGLIO)){
			if(appendTablePrefix){
				return this.toAliasTable(field)+".dettaglio";
			}else{
				return "dettaglio";
			}
		}
		if(field.equals(Evento.model().ID_VERSAMENTO.COD_VERSAMENTO_ENTE)){
			if(appendTablePrefix){
				return this.toAliasTable(field)+".cod_versamento_ente";
			}else{
				return "cod_versamento_ente";
			}
		}
		if(field.equals(Evento.model().ID_VERSAMENTO.ID_APPLICAZIONE.COD_APPLICAZIONE)){
			if(appendTablePrefix){
				return this.toAliasTable(field)+".cod_applicazione";
			}else{
				return "cod_applicazione";
			}
		}
		if(field.equals(Evento.model().ID_VERSAMENTO.DEBITORE_IDENTIFICATIVO)){
			if(appendTablePrefix){
				return this.toAliasTable(field)+".debitore_identificativo";
			}else{
				return "debitore_identificativo";
			}
		}
		if(field.equals(Evento.model().ID_VERSAMENTO.DEBITORE_ANAGRAFICA)){
			if(appendTablePrefix){
				return this.toAliasTable(field)+".debitore_anagrafica";
			}else{
				return "debitore_anagrafica";
			}
		}
		if(field.equals(Evento.model().ID_VERSAMENTO.COD_VERSAMENTO_LOTTO)){
			if(appendTablePrefix){
				return this.toAliasTable(field)+".cod_versamento_lotto";
			}else{
				return "cod_versamento_lotto";
			}
		}
		if(field.equals(Evento.model().ID_VERSAMENTO.COD_ANNO_TRIBUTARIO)){
			if(appendTablePrefix){
				return this.toAliasTable(field)+".cod_anno_tributario";
			}else{
				return "cod_anno_tributario";
			}
		}
		if(field.equals(Evento.model().ID_VERSAMENTO.IMPORTO_TOTALE)){
			if(appendTablePrefix){
				return this.toAliasTable(field)+".importo_totale";
			}else{
				return "importo_totale";
			}
		}
		if(field.equals(Evento.model().ID_VERSAMENTO.CAUSALE_VERSAMENTO)){
			if(appendTablePrefix){
				return this.toAliasTable(field)+".causale_versamento";
			}else{
				return "causale_versamento";
			}
		}
		if(field.equals(Evento.model().ID_VERSAMENTO.STATO_VERSAMENTO)){
			if(appendTablePrefix){
				return this.toAliasTable(field)+".stato_versamento";
			}else{
				return "stato_versamento";
			}
		}
		if(field.equals(Evento.model().ID_PAGAMENTO_PORTALE.ID_SESSIONE)){
			if(appendTablePrefix){
				return this.toAliasTable(field)+".id_sessione";
			}else{
				return "id_sessione";
			}
		}
		if(field.equals(Evento.model().ID_PAGAMENTO_PORTALE.ID_APPLICAZIONE.COD_APPLICAZIONE)){
			if(appendTablePrefix){
				return this.toAliasTable(field)+".cod_applicazione";
			}else{
				return "cod_applicazione";
			}
		}
		if(field.equals(Evento.model().ID_PAGAMENTO_PORTALE.VERSANTE_IDENTIFICATIVO)){
			if(appendTablePrefix){
				return this.toAliasTable(field)+".versante_identificativo";
			}else{
				return "versante_identificativo";
			}
		}


		return super.toColumn(field,returnAlias,appendTablePrefix);
		
	}
	
	@Override
	public String toTable(IField field,boolean returnAlias) throws ExpressionException {
		
		// In the case of table with alias, using parameter returnAlias​​, 
		// it is possible to drive the choice whether to return only the alias or 
		// the full definition of the table containing the alias
		
		if(field.equals(Evento.model().COD_DOMINIO)){
			return this.toTable(Evento.model(), returnAlias);
		}
		if(field.equals(Evento.model().IUV)){
			return this.toTable(Evento.model(), returnAlias);
		}
		if(field.equals(Evento.model().CCP)){
			return this.toTable(Evento.model(), returnAlias);
		}
		if(field.equals(Evento.model().CATEGORIA_EVENTO)){
			return this.toTable(Evento.model(), returnAlias);
		}
		if(field.equals(Evento.model().TIPO_EVENTO)){
			return this.toTable(Evento.model(), returnAlias);
		}
		if(field.equals(Evento.model().SOTTOTIPO_EVENTO)){
			return this.toTable(Evento.model(), returnAlias);
		}
		if(field.equals(Evento.model().DATA)){
			return this.toTable(Evento.model(), returnAlias);
		}
		if(field.equals(Evento.model().INTERVALLO)){
			return this.toTable(Evento.model(), returnAlias);
		}
		if(field.equals(Evento.model().CLASSNAME_DETTAGLIO)){
			return this.toTable(Evento.model(), returnAlias);
		}
		if(field.equals(Evento.model().DETTAGLIO)){
			return this.toTable(Evento.model(), returnAlias);
		}
		if(field.equals(Evento.model().ID_VERSAMENTO.COD_VERSAMENTO_ENTE)){
			return this.toTable(Evento.model().ID_VERSAMENTO, returnAlias);
		}
		if(field.equals(Evento.model().ID_VERSAMENTO.ID_APPLICAZIONE.COD_APPLICAZIONE)){
			return this.toTable(Evento.model().ID_VERSAMENTO.ID_APPLICAZIONE, returnAlias);
		}
		if(field.equals(Evento.model().ID_VERSAMENTO.DEBITORE_IDENTIFICATIVO)){
			return this.toTable(Evento.model().ID_VERSAMENTO, returnAlias);
		}
		if(field.equals(Evento.model().ID_VERSAMENTO.DEBITORE_ANAGRAFICA)){
			return this.toTable(Evento.model().ID_VERSAMENTO, returnAlias);
		}
		if(field.equals(Evento.model().ID_VERSAMENTO.COD_VERSAMENTO_LOTTO)){
			return this.toTable(Evento.model().ID_VERSAMENTO, returnAlias);
		}
		if(field.equals(Evento.model().ID_VERSAMENTO.COD_ANNO_TRIBUTARIO)){
			return this.toTable(Evento.model().ID_VERSAMENTO, returnAlias);
		}
		if(field.equals(Evento.model().ID_VERSAMENTO.IMPORTO_TOTALE)){
			return this.toTable(Evento.model().ID_VERSAMENTO, returnAlias);
		}
		if(field.equals(Evento.model().ID_VERSAMENTO.CAUSALE_VERSAMENTO)){
			return this.toTable(Evento.model().ID_VERSAMENTO, returnAlias);
		}
		if(field.equals(Evento.model().ID_VERSAMENTO.STATO_VERSAMENTO)){
			return this.toTable(Evento.model().ID_VERSAMENTO, returnAlias);
		}
		if(field.equals(Evento.model().ID_PAGAMENTO_PORTALE.ID_SESSIONE)){
			return this.toTable(Evento.model().ID_PAGAMENTO_PORTALE, returnAlias);
		}
		if(field.equals(Evento.model().ID_PAGAMENTO_PORTALE.ID_APPLICAZIONE.COD_APPLICAZIONE)){
			return this.toTable(Evento.model().ID_PAGAMENTO_PORTALE.ID_APPLICAZIONE, returnAlias);
		}
		if(field.equals(Evento.model().ID_PAGAMENTO_PORTALE.VERSANTE_IDENTIFICATIVO)){
			return this.toTable(Evento.model().ID_PAGAMENTO_PORTALE, returnAlias);
		}


		return super.toTable(field,returnAlias);
		
	}

	@Override
	public String toTable(IModel<?> model,boolean returnAlias) throws ExpressionException {
		
		// In the case of table with alias, using parameter returnAlias​​, 
		// it is possible to drive the choice whether to return only the alias or 
		// the full definition of the table containing the alias
		
		if(model.equals(Evento.model())){
			return "eventi";
		}
		if(model.equals(Evento.model().ID_VERSAMENTO)){
			return "versamenti";
		}
		if(model.equals(Evento.model().ID_VERSAMENTO.ID_APPLICAZIONE)){
			return "applicazioni";
		}
		if(model.equals(Evento.model().ID_PAGAMENTO_PORTALE)){
			return "pagamenti_portale";
		}
		if(model.equals(Evento.model().ID_PAGAMENTO_PORTALE.ID_APPLICAZIONE)){
			return "applicazioni";
		}


		return super.toTable(model,returnAlias);
		
	}

}
