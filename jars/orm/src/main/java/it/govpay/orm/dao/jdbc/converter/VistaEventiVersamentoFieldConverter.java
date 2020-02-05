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
 * VistaEventiVersamentoFieldConverter
 *
 * @author Giovanni Bussu (bussu@link.it)
 * @author Lorenzo Nardi (nardi@link.it)
 * @author $Author$
 * @version $Rev$, $Date$
 */
public class VistaEventiVersamentoFieldConverter extends AbstractSQLFieldConverter {

	private TipiDatabase databaseType;
	
	public VistaEventiVersamentoFieldConverter(String databaseType){
		this.databaseType = TipiDatabase.toEnumConstant(databaseType);
	}
	public VistaEventiVersamentoFieldConverter(TipiDatabase databaseType){
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
		
		if(field.equals(Evento.model().COMPONENTE)){
			if(appendTablePrefix){
				return this.toAliasTable(field)+".componente";
			}else{
				return "componente";
			}
		}
		if(field.equals(Evento.model().RUOLO)){
			if(appendTablePrefix){
				return this.toAliasTable(field)+".ruolo";
			}else{
				return "ruolo";
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
		if(field.equals(Evento.model().ESITO)){
			if(appendTablePrefix){
				return this.toAliasTable(field)+".esito";
			}else{
				return "esito";
			}
		}
		if(field.equals(Evento.model().SOTTOTIPO_ESITO)){
			if(appendTablePrefix){
				return this.toAliasTable(field)+".sottotipo_esito";
			}else{
				return "sottotipo_esito";
			}
		}
		if(field.equals(Evento.model().DETTAGLIO_ESITO)){
			if(appendTablePrefix){
				return this.toAliasTable(field)+".dettaglio_esito";
			}else{
				return "dettaglio_esito";
			}
		}
		if(field.equals(Evento.model().PARAMETRI_RICHIESTA)){
			if(appendTablePrefix){
				return this.toAliasTable(field)+".parametri_richiesta";
			}else{
				return "parametri_richiesta";
			}
		}
		if(field.equals(Evento.model().PARAMETRI_RISPOSTA)){
			if(appendTablePrefix){
				return this.toAliasTable(field)+".parametri_risposta";
			}else{
				return "parametri_risposta";
			}
		}
		if(field.equals(Evento.model().DATI_PAGO_PA)){
			if(appendTablePrefix){
				return this.toAliasTable(field)+".dati_pago_pa";
			}else{
				return "dati_pago_pa";
			}
		}
		if(field.equals(Evento.model().COD_VERSAMENTO_ENTE)){
			if(appendTablePrefix){
				return this.toAliasTable(field)+".cod_versamento_ente";
			}else{
				return "cod_versamento_ente";
			}
		}
		if(field.equals(Evento.model().COD_APPLICAZIONE)){
			if(appendTablePrefix){
				return this.toAliasTable(field)+".cod_applicazione";
			}else{
				return "cod_applicazione";
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
		if(field.equals(Evento.model().COD_DOMINIO)){
			if(appendTablePrefix){
				return this.toAliasTable(field)+".cod_dominio";
			}else{
				return "cod_dominio";
			}
		}
		if(field.equals(Evento.model().ID_SESSIONE)){
			if(appendTablePrefix){
				return this.toAliasTable(field)+".id_sessione";
			}else{
				return "id_sessione";
			}
		}
		if(field.equals(Evento.model().ID_FR.COD_FLUSSO)){
			if(appendTablePrefix){
				return this.toAliasTable(field)+".cod_flusso";
			}else{
				return "cod_flusso";
			}
		}
		if(field.equals(Evento.model().ID_FR.COD_DOMINIO)){
			if(appendTablePrefix){
				return this.toAliasTable(field)+".cod_dominio";
			}else{
				return "cod_dominio";
			}
		}
		if(field.equals(Evento.model().ID_INCASSO.TRN)){
			if(appendTablePrefix){
				return this.toAliasTable(field)+".trn";
			}else{
				return "trn";
			}
		}
		if(field.equals(Evento.model().ID_INCASSO.COD_DOMINIO)){
			if(appendTablePrefix){
				return this.toAliasTable(field)+".cod_dominio";
			}else{
				return "cod_dominio";
			}
		}
		if(field.equals(Evento.model().ID_TRACCIATO.ID_TRACCIATO)){
			if(appendTablePrefix){
				return this.toAliasTable(field)+".id_tracciato";
			}else{
				return "id_tracciato";
			}
		}


		return super.toColumn(field,returnAlias,appendTablePrefix);
		
	}
	
	@Override
	public String toTable(IField field,boolean returnAlias) throws ExpressionException {
		
		// In the case of table with alias, using parameter returnAlias​​, 
		// it is possible to drive the choice whether to return only the alias or 
		// the full definition of the table containing the alias
		
		if(field.equals(Evento.model().COMPONENTE)){
			return this.toTable(Evento.model(), returnAlias);
		}
		if(field.equals(Evento.model().RUOLO)){
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
		if(field.equals(Evento.model().ESITO)){
			return this.toTable(Evento.model(), returnAlias);
		}
		if(field.equals(Evento.model().SOTTOTIPO_ESITO)){
			return this.toTable(Evento.model(), returnAlias);
		}
		if(field.equals(Evento.model().DETTAGLIO_ESITO)){
			return this.toTable(Evento.model(), returnAlias);
		}
		if(field.equals(Evento.model().PARAMETRI_RICHIESTA)){
			return this.toTable(Evento.model(), returnAlias);
		}
		if(field.equals(Evento.model().PARAMETRI_RISPOSTA)){
			return this.toTable(Evento.model(), returnAlias);
		}
		if(field.equals(Evento.model().DATI_PAGO_PA)){
			return this.toTable(Evento.model(), returnAlias);
		}
		if(field.equals(Evento.model().COD_VERSAMENTO_ENTE)){
			return this.toTable(Evento.model(), returnAlias);
		}
		if(field.equals(Evento.model().COD_APPLICAZIONE)){
			return this.toTable(Evento.model(), returnAlias);
		}
		if(field.equals(Evento.model().IUV)){
			return this.toTable(Evento.model(), returnAlias);
		}
		if(field.equals(Evento.model().CCP)){
			return this.toTable(Evento.model(), returnAlias);
		}
		if(field.equals(Evento.model().COD_DOMINIO)){
			return this.toTable(Evento.model(), returnAlias);
		}
		if(field.equals(Evento.model().ID_SESSIONE)){
			return this.toTable(Evento.model(), returnAlias);
		}
		if(field.equals(Evento.model().ID_FR.COD_FLUSSO)){
			return this.toTable(Evento.model().ID_FR, returnAlias);
		}
		if(field.equals(Evento.model().ID_FR.COD_DOMINIO)){
			return this.toTable(Evento.model().ID_FR, returnAlias);
		}
		if(field.equals(Evento.model().ID_INCASSO.TRN)){
			return this.toTable(Evento.model().ID_INCASSO, returnAlias);
		}
		if(field.equals(Evento.model().ID_INCASSO.COD_DOMINIO)){
			return this.toTable(Evento.model().ID_INCASSO, returnAlias);
		}
		if(field.equals(Evento.model().ID_TRACCIATO.ID_TRACCIATO)){
			return this.toTable(Evento.model().ID_TRACCIATO, returnAlias);
		}


		return super.toTable(field,returnAlias);
		
	}

	@Override
	public String toTable(IModel<?> model,boolean returnAlias) throws ExpressionException {
		
		// In the case of table with alias, using parameter returnAlias​​, 
		// it is possible to drive the choice whether to return only the alias or 
		// the full definition of the table containing the alias
		
		if(model.equals(Evento.model())){
			return "v_eventi_vers";
		}
		if(model.equals(Evento.model().ID_FR)){
			return "fr";
		}
		if(model.equals(Evento.model().ID_INCASSO)){
			return "incassi";
		}
		if(model.equals(Evento.model().ID_TRACCIATO)){
			return "tracciati";
		}


		return super.toTable(model,returnAlias);
		
	}

}
