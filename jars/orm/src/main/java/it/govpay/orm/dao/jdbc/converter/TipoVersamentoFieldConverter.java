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

import it.govpay.orm.TipoVersamento;


/**     
 * TipoVersamentoFieldConverter
 *
 * @author Giovanni Bussu (bussu@link.it)
 * @author Lorenzo Nardi (nardi@link.it)
 * @author $Author$
 * @version $Rev$, $Date$
 */
public class TipoVersamentoFieldConverter extends AbstractSQLFieldConverter {

	private TipiDatabase databaseType;
	
	public TipoVersamentoFieldConverter(String databaseType){
		this.databaseType = TipiDatabase.toEnumConstant(databaseType);
	}
	public TipoVersamentoFieldConverter(TipiDatabase databaseType){
		this.databaseType = databaseType;
	}


	@Override
	public IModel<?> getRootModel() throws ExpressionException {
		return TipoVersamento.model();
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
		
		if(field.equals(TipoVersamento.model().COD_TIPO_VERSAMENTO)){
			if(appendTablePrefix){
				return this.toAliasTable(field)+".cod_tipo_versamento";
			}else{
				return "cod_tipo_versamento";
			}
		}
		if(field.equals(TipoVersamento.model().DESCRIZIONE)){
			if(appendTablePrefix){
				return this.toAliasTable(field)+".descrizione";
			}else{
				return "descrizione";
			}
		}
		if(field.equals(TipoVersamento.model().CODIFICA_IUV)){
			if(appendTablePrefix){
				return this.toAliasTable(field)+".codifica_iuv";
			}else{
				return "codifica_iuv";
			}
		}
		if(field.equals(TipoVersamento.model().TIPO)){
			if(appendTablePrefix){
				return this.toAliasTable(field)+".tipo";
			}else{
				return "tipo";
			}
		}
		if(field.equals(TipoVersamento.model().PAGA_TERZI)){
			if(appendTablePrefix){
				return this.toAliasTable(field)+".paga_terzi";
			}else{
				return "paga_terzi";
			}
		}
		if(field.equals(TipoVersamento.model().ABILITATO)){
			if(appendTablePrefix){
				return this.toAliasTable(field)+".abilitato";
			}else{
				return "abilitato";
			}
		}
		if(field.equals(TipoVersamento.model().FORM_TIPO)){
			if(appendTablePrefix){
				return this.toAliasTable(field)+".form_tipo";
			}else{
				return "form_tipo";
			}
		}
		if(field.equals(TipoVersamento.model().FORM_DEFINIZIONE)){
			if(appendTablePrefix){
				return this.toAliasTable(field)+".form_definizione";
			}else{
				return "form_definizione";
			}
		}
		if(field.equals(TipoVersamento.model().VALIDAZIONE_DEFINIZIONE)){
			if(appendTablePrefix){
				return this.toAliasTable(field)+".validazione_definizione";
			}else{
				return "validazione_definizione";
			}
		}
		if(field.equals(TipoVersamento.model().TRASFORMAZIONE_TIPO)){
			if(appendTablePrefix){
				return this.toAliasTable(field)+".trasformazione_tipo";
			}else{
				return "trasformazione_tipo";
			}
		}
		if(field.equals(TipoVersamento.model().TRASFORMAZIONE_DEFINIZIONE)){
			if(appendTablePrefix){
				return this.toAliasTable(field)+".trasformazione_definizione";
			}else{
				return "trasformazione_definizione";
			}
		}
		if(field.equals(TipoVersamento.model().COD_APPLICAZIONE)){
			if(appendTablePrefix){
				return this.toAliasTable(field)+".cod_applicazione";
			}else{
				return "cod_applicazione";
			}
		}
		if(field.equals(TipoVersamento.model().PROMEMORIA_AVVISO_ABILITATO)){
			if(appendTablePrefix){
				return this.toAliasTable(field)+".promemoria_avviso_abilitato";
			}else{
				return "promemoria_avviso_abilitato";
			}
		}
		if(field.equals(TipoVersamento.model().PROMEMORIA_AVVISO_PDF)){
			if(appendTablePrefix){
				return this.toAliasTable(field)+".promemoria_avviso_pdf";
			}else{
				return "promemoria_avviso_pdf";
			}
		}
		if(field.equals(TipoVersamento.model().PROMEMORIA_AVVISO_TIPO)){
			if(appendTablePrefix){
				return this.toAliasTable(field)+".promemoria_avviso_tipo";
			}else{
				return "promemoria_avviso_tipo";
			}
		}
		if(field.equals(TipoVersamento.model().PROMEMORIA_AVVISO_OGGETTO)){
			if(appendTablePrefix){
				return this.toAliasTable(field)+".promemoria_avviso_oggetto";
			}else{
				return "promemoria_avviso_oggetto";
			}
		}
		if(field.equals(TipoVersamento.model().PROMEMORIA_AVVISO_MESSAGGIO)){
			if(appendTablePrefix){
				return this.toAliasTable(field)+".promemoria_avviso_messaggio";
			}else{
				return "promemoria_avviso_messaggio";
			}
		}
		if(field.equals(TipoVersamento.model().PROMEMORIA_RICEVUTA_ABILITATO)){
			if(appendTablePrefix){
				return this.toAliasTable(field)+".promemoria_ricevuta_abilitato";
			}else{
				return "promemoria_ricevuta_abilitato";
			}
		}
		if(field.equals(TipoVersamento.model().PROMEMORIA_RICEVUTA_TIPO)){
			if(appendTablePrefix){
				return this.toAliasTable(field)+".promemoria_ricevuta_tipo";
			}else{
				return "promemoria_ricevuta_tipo";
			}
		}
		if(field.equals(TipoVersamento.model().PROMEMORIA_RICEVUTA_PDF)){
			if(appendTablePrefix){
				return this.toAliasTable(field)+".promemoria_ricevuta_pdf";
			}else{
				return "promemoria_ricevuta_pdf";
			}
		}
		if(field.equals(TipoVersamento.model().PROMEMORIA_RICEVUTA_OGGETTO)){
			if(appendTablePrefix){
				return this.toAliasTable(field)+".promemoria_ricevuta_oggetto";
			}else{
				return "promemoria_ricevuta_oggetto";
			}
		}
		if(field.equals(TipoVersamento.model().PROMEMORIA_RICEVUTA_MESSAGGIO)){
			if(appendTablePrefix){
				return this.toAliasTable(field)+".promemoria_ricevuta_messaggio";
			}else{
				return "promemoria_ricevuta_messaggio";
			}
		}
		if(field.equals(TipoVersamento.model().VISUALIZZAZIONE_DEFINIZIONE)){
			if(appendTablePrefix){
				return this.toAliasTable(field)+".visualizzazione_definizione";
			}else{
				return "visualizzazione_definizione";
			}
		}
		if(field.equals(TipoVersamento.model().TRAC_CSV_TIPO)){
			if(appendTablePrefix){
				return this.toAliasTable(field)+".trac_csv_tipo";
			}else{
				return "trac_csv_tipo";
			}
		}
		if(field.equals(TipoVersamento.model().TRAC_CSV_HEADER_RISPOSTA)){
			if(appendTablePrefix){
				return this.toAliasTable(field)+".trac_csv_header_risposta";
			}else{
				return "trac_csv_header_risposta";
			}
		}
		if(field.equals(TipoVersamento.model().TRAC_CSV_TEMPLATE_RICHIESTA)){
			if(appendTablePrefix){
				return this.toAliasTable(field)+".trac_csv_template_richiesta";
			}else{
				return "trac_csv_template_richiesta";
			}
		}
		if(field.equals(TipoVersamento.model().TRAC_CSV_TEMPLATE_RISPOSTA)){
			if(appendTablePrefix){
				return this.toAliasTable(field)+".trac_csv_template_risposta";
			}else{
				return "trac_csv_template_risposta";
			}
		}


		return super.toColumn(field,returnAlias,appendTablePrefix);
		
	}
	
	@Override
	public String toTable(IField field,boolean returnAlias) throws ExpressionException {
		
		// In the case of table with alias, using parameter returnAlias​​, 
		// it is possible to drive the choice whether to return only the alias or 
		// the full definition of the table containing the alias
		
		if(field.equals(TipoVersamento.model().COD_TIPO_VERSAMENTO)){
			return this.toTable(TipoVersamento.model(), returnAlias);
		}
		if(field.equals(TipoVersamento.model().DESCRIZIONE)){
			return this.toTable(TipoVersamento.model(), returnAlias);
		}
		if(field.equals(TipoVersamento.model().CODIFICA_IUV)){
			return this.toTable(TipoVersamento.model(), returnAlias);
		}
		if(field.equals(TipoVersamento.model().TIPO)){
			return this.toTable(TipoVersamento.model(), returnAlias);
		}
		if(field.equals(TipoVersamento.model().PAGA_TERZI)){
			return this.toTable(TipoVersamento.model(), returnAlias);
		}
		if(field.equals(TipoVersamento.model().ABILITATO)){
			return this.toTable(TipoVersamento.model(), returnAlias);
		}
		if(field.equals(TipoVersamento.model().FORM_TIPO)){
			return this.toTable(TipoVersamento.model(), returnAlias);
		}
		if(field.equals(TipoVersamento.model().FORM_DEFINIZIONE)){
			return this.toTable(TipoVersamento.model(), returnAlias);
		}
		if(field.equals(TipoVersamento.model().VALIDAZIONE_DEFINIZIONE)){
			return this.toTable(TipoVersamento.model(), returnAlias);
		}
		if(field.equals(TipoVersamento.model().TRASFORMAZIONE_TIPO)){
			return this.toTable(TipoVersamento.model(), returnAlias);
		}
		if(field.equals(TipoVersamento.model().TRASFORMAZIONE_DEFINIZIONE)){
			return this.toTable(TipoVersamento.model(), returnAlias);
		}
		if(field.equals(TipoVersamento.model().COD_APPLICAZIONE)){
			return this.toTable(TipoVersamento.model(), returnAlias);
		}
		if(field.equals(TipoVersamento.model().PROMEMORIA_AVVISO_ABILITATO)){
			return this.toTable(TipoVersamento.model(), returnAlias);
		}
		if(field.equals(TipoVersamento.model().PROMEMORIA_AVVISO_PDF)){
			return this.toTable(TipoVersamento.model(), returnAlias);
		}
		if(field.equals(TipoVersamento.model().PROMEMORIA_AVVISO_TIPO)){
			return this.toTable(TipoVersamento.model(), returnAlias);
		}
		if(field.equals(TipoVersamento.model().PROMEMORIA_AVVISO_OGGETTO)){
			return this.toTable(TipoVersamento.model(), returnAlias);
		}
		if(field.equals(TipoVersamento.model().PROMEMORIA_AVVISO_MESSAGGIO)){
			return this.toTable(TipoVersamento.model(), returnAlias);
		}
		if(field.equals(TipoVersamento.model().PROMEMORIA_RICEVUTA_ABILITATO)){
			return this.toTable(TipoVersamento.model(), returnAlias);
		}
		if(field.equals(TipoVersamento.model().PROMEMORIA_RICEVUTA_TIPO)){
			return this.toTable(TipoVersamento.model(), returnAlias);
		}
		if(field.equals(TipoVersamento.model().PROMEMORIA_RICEVUTA_PDF)){
			return this.toTable(TipoVersamento.model(), returnAlias);
		}
		if(field.equals(TipoVersamento.model().PROMEMORIA_RICEVUTA_OGGETTO)){
			return this.toTable(TipoVersamento.model(), returnAlias);
		}
		if(field.equals(TipoVersamento.model().PROMEMORIA_RICEVUTA_MESSAGGIO)){
			return this.toTable(TipoVersamento.model(), returnAlias);
		}
		if(field.equals(TipoVersamento.model().VISUALIZZAZIONE_DEFINIZIONE)){
			return this.toTable(TipoVersamento.model(), returnAlias);
		}
		if(field.equals(TipoVersamento.model().TRAC_CSV_TIPO)){
			return this.toTable(TipoVersamento.model(), returnAlias);
		}
		if(field.equals(TipoVersamento.model().TRAC_CSV_HEADER_RISPOSTA)){
			return this.toTable(TipoVersamento.model(), returnAlias);
		}
		if(field.equals(TipoVersamento.model().TRAC_CSV_TEMPLATE_RICHIESTA)){
			return this.toTable(TipoVersamento.model(), returnAlias);
		}
		if(field.equals(TipoVersamento.model().TRAC_CSV_TEMPLATE_RISPOSTA)){
			return this.toTable(TipoVersamento.model(), returnAlias);
		}


		return super.toTable(field,returnAlias);
		
	}

	@Override
	public String toTable(IModel<?> model,boolean returnAlias) throws ExpressionException {
		
		// In the case of table with alias, using parameter returnAlias​​, 
		// it is possible to drive the choice whether to return only the alias or 
		// the full definition of the table containing the alias
		
		if(model.equals(TipoVersamento.model())){
			return "tipi_versamento";
		}


		return super.toTable(model,returnAlias);
		
	}

}
