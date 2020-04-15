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

import it.govpay.orm.TipoVersamentoDominio;


/**     
 * TipoVersamentoDominioFieldConverter
 *
 * @author Giovanni Bussu (bussu@link.it)
 * @author Lorenzo Nardi (nardi@link.it)
 * @author $Author$
 * @version $Rev$, $Date$
 */
public class TipoVersamentoDominioFieldConverter extends AbstractSQLFieldConverter {

	private TipiDatabase databaseType;
	
	public TipoVersamentoDominioFieldConverter(String databaseType){
		this.databaseType = TipiDatabase.toEnumConstant(databaseType);
	}
	public TipoVersamentoDominioFieldConverter(TipiDatabase databaseType){
		this.databaseType = databaseType;
	}


	@Override
	public IModel<?> getRootModel() throws ExpressionException {
		return TipoVersamentoDominio.model();
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
		
		if(field.equals(TipoVersamentoDominio.model().TIPO_VERSAMENTO.COD_TIPO_VERSAMENTO)){
			if(appendTablePrefix){
				return this.toAliasTable(field)+".cod_tipo_versamento";
			}else{
				return "cod_tipo_versamento";
			}
		}
		if(field.equals(TipoVersamentoDominio.model().TIPO_VERSAMENTO.DESCRIZIONE)){
			if(appendTablePrefix){
				return this.toAliasTable(field)+".descrizione";
			}else{
				return "descrizione";
			}
		}
		if(field.equals(TipoVersamentoDominio.model().TIPO_VERSAMENTO.CODIFICA_IUV)){
			if(appendTablePrefix){
				return this.toAliasTable(field)+".codifica_iuv";
			}else{
				return "codifica_iuv";
			}
		}
		if(field.equals(TipoVersamentoDominio.model().TIPO_VERSAMENTO.TIPO)){
			if(appendTablePrefix){
				return this.toAliasTable(field)+".tipo";
			}else{
				return "tipo";
			}
		}
		if(field.equals(TipoVersamentoDominio.model().TIPO_VERSAMENTO.PAGA_TERZI)){
			if(appendTablePrefix){
				return this.toAliasTable(field)+".paga_terzi";
			}else{
				return "paga_terzi";
			}
		}
		if(field.equals(TipoVersamentoDominio.model().TIPO_VERSAMENTO.ABILITATO)){
			if(appendTablePrefix){
				return this.toAliasTable(field)+".abilitato";
			}else{
				return "abilitato";
			}
		}
		if(field.equals(TipoVersamentoDominio.model().TIPO_VERSAMENTO.BO_FORM_TIPO)){
			if(appendTablePrefix){
				return this.toAliasTable(field)+".bo_form_tipo";
			}else{
				return "bo_form_tipo";
			}
		}
		if(field.equals(TipoVersamentoDominio.model().TIPO_VERSAMENTO.BO_FORM_DEFINIZIONE)){
			if(appendTablePrefix){
				return this.toAliasTable(field)+".bo_form_definizione";
			}else{
				return "bo_form_definizione";
			}
		}
		if(field.equals(TipoVersamentoDominio.model().TIPO_VERSAMENTO.BO_VALIDAZIONE_DEF)){
			if(appendTablePrefix){
				return this.toAliasTable(field)+".bo_validazione_def";
			}else{
				return "bo_validazione_def";
			}
		}
		if(field.equals(TipoVersamentoDominio.model().TIPO_VERSAMENTO.BO_TRASFORMAZIONE_TIPO)){
			if(appendTablePrefix){
				return this.toAliasTable(field)+".bo_trasformazione_tipo";
			}else{
				return "bo_trasformazione_tipo";
			}
		}
		if(field.equals(TipoVersamentoDominio.model().TIPO_VERSAMENTO.BO_TRASFORMAZIONE_DEF)){
			if(appendTablePrefix){
				return this.toAliasTable(field)+".bo_trasformazione_def";
			}else{
				return "bo_trasformazione_def";
			}
		}
		if(field.equals(TipoVersamentoDominio.model().TIPO_VERSAMENTO.BO_COD_APPLICAZIONE)){
			if(appendTablePrefix){
				return this.toAliasTable(field)+".bo_cod_applicazione";
			}else{
				return "bo_cod_applicazione";
			}
		}
		if(field.equals(TipoVersamentoDominio.model().TIPO_VERSAMENTO.BO_ABILITATO)){
			if(appendTablePrefix){
				return this.toAliasTable(field)+".bo_abilitato";
			}else{
				return "bo_abilitato";
			}
		}
		if(field.equals(TipoVersamentoDominio.model().TIPO_VERSAMENTO.PAG_FORM_TIPO)){
			if(appendTablePrefix){
				return this.toAliasTable(field)+".pag_form_tipo";
			}else{
				return "pag_form_tipo";
			}
		}
		if(field.equals(TipoVersamentoDominio.model().TIPO_VERSAMENTO.PAG_FORM_DEFINIZIONE)){
			if(appendTablePrefix){
				return this.toAliasTable(field)+".pag_form_definizione";
			}else{
				return "pag_form_definizione";
			}
		}
		if(field.equals(TipoVersamentoDominio.model().TIPO_VERSAMENTO.PAG_FORM_IMPAGINAZIONE)){
			if(appendTablePrefix){
				return this.toAliasTable(field)+".pag_form_impaginazione";
			}else{
				return "pag_form_impaginazione";
			}
		}
		if(field.equals(TipoVersamentoDominio.model().TIPO_VERSAMENTO.PAG_VALIDAZIONE_DEF)){
			if(appendTablePrefix){
				return this.toAliasTable(field)+".pag_validazione_def";
			}else{
				return "pag_validazione_def";
			}
		}
		if(field.equals(TipoVersamentoDominio.model().TIPO_VERSAMENTO.PAG_TRASFORMAZIONE_TIPO)){
			if(appendTablePrefix){
				return this.toAliasTable(field)+".pag_trasformazione_tipo";
			}else{
				return "pag_trasformazione_tipo";
			}
		}
		if(field.equals(TipoVersamentoDominio.model().TIPO_VERSAMENTO.PAG_TRASFORMAZIONE_DEF)){
			if(appendTablePrefix){
				return this.toAliasTable(field)+".pag_trasformazione_def";
			}else{
				return "pag_trasformazione_def";
			}
		}
		if(field.equals(TipoVersamentoDominio.model().TIPO_VERSAMENTO.PAG_COD_APPLICAZIONE)){
			if(appendTablePrefix){
				return this.toAliasTable(field)+".pag_cod_applicazione";
			}else{
				return "pag_cod_applicazione";
			}
		}
		if(field.equals(TipoVersamentoDominio.model().TIPO_VERSAMENTO.PAG_ABILITATO)){
			if(appendTablePrefix){
				return this.toAliasTable(field)+".pag_abilitato";
			}else{
				return "pag_abilitato";
			}
		}
		if(field.equals(TipoVersamentoDominio.model().TIPO_VERSAMENTO.AVV_MAIL_PROM_AVV_ABILITATO)){
			if(appendTablePrefix){
				return this.toAliasTable(field)+".avv_mail_prom_avv_abilitato";
			}else{
				return "avv_mail_prom_avv_abilitato";
			}
		}
		if(field.equals(TipoVersamentoDominio.model().TIPO_VERSAMENTO.AVV_MAIL_PROM_AVV_PDF)){
			if(appendTablePrefix){
				return this.toAliasTable(field)+".avv_mail_prom_avv_pdf";
			}else{
				return "avv_mail_prom_avv_pdf";
			}
		}
		if(field.equals(TipoVersamentoDominio.model().TIPO_VERSAMENTO.AVV_MAIL_PROM_AVV_TIPO)){
			if(appendTablePrefix){
				return this.toAliasTable(field)+".avv_mail_prom_avv_tipo";
			}else{
				return "avv_mail_prom_avv_tipo";
			}
		}
		if(field.equals(TipoVersamentoDominio.model().TIPO_VERSAMENTO.AVV_MAIL_PROM_AVV_OGGETTO)){
			if(appendTablePrefix){
				return this.toAliasTable(field)+".avv_mail_prom_avv_oggetto";
			}else{
				return "avv_mail_prom_avv_oggetto";
			}
		}
		if(field.equals(TipoVersamentoDominio.model().TIPO_VERSAMENTO.AVV_MAIL_PROM_AVV_MESSAGGIO)){
			if(appendTablePrefix){
				return this.toAliasTable(field)+".avv_mail_prom_avv_messaggio";
			}else{
				return "avv_mail_prom_avv_messaggio";
			}
		}
		if(field.equals(TipoVersamentoDominio.model().TIPO_VERSAMENTO.AVV_MAIL_PROM_RIC_ABILITATO)){
			if(appendTablePrefix){
				return this.toAliasTable(field)+".avv_mail_prom_ric_abilitato";
			}else{
				return "avv_mail_prom_ric_abilitato";
			}
		}
		if(field.equals(TipoVersamentoDominio.model().TIPO_VERSAMENTO.AVV_MAIL_PROM_RIC_PDF)){
			if(appendTablePrefix){
				return this.toAliasTable(field)+".avv_mail_prom_ric_pdf";
			}else{
				return "avv_mail_prom_ric_pdf";
			}
		}
		if(field.equals(TipoVersamentoDominio.model().TIPO_VERSAMENTO.AVV_MAIL_PROM_RIC_TIPO)){
			if(appendTablePrefix){
				return this.toAliasTable(field)+".avv_mail_prom_ric_tipo";
			}else{
				return "avv_mail_prom_ric_tipo";
			}
		}
		if(field.equals(TipoVersamentoDominio.model().TIPO_VERSAMENTO.AVV_MAIL_PROM_RIC_OGGETTO)){
			if(appendTablePrefix){
				return this.toAliasTable(field)+".avv_mail_prom_ric_oggetto";
			}else{
				return "avv_mail_prom_ric_oggetto";
			}
		}
		if(field.equals(TipoVersamentoDominio.model().TIPO_VERSAMENTO.AVV_MAIL_PROM_RIC_MESSAGGIO)){
			if(appendTablePrefix){
				return this.toAliasTable(field)+".avv_mail_prom_ric_messaggio";
			}else{
				return "avv_mail_prom_ric_messaggio";
			}
		}
		if(field.equals(TipoVersamentoDominio.model().TIPO_VERSAMENTO.AVV_MAIL_PROM_RIC_ESEGUITI)){
			if(appendTablePrefix){
				return this.toAliasTable(field)+".avv_mail_prom_ric_eseguiti";
			}else{
				return "avv_mail_prom_ric_eseguiti";
			}
		}
		if(field.equals(TipoVersamentoDominio.model().TIPO_VERSAMENTO.AVV_MAIL_PROM_SCAD_ABILITATO)){
			if(appendTablePrefix){
				return this.toAliasTable(field)+".avv_mail_prom_scad_abilitato";
			}else{
				return "avv_mail_prom_scad_abilitato";
			}
		}
		if(field.equals(TipoVersamentoDominio.model().TIPO_VERSAMENTO.AVV_MAIL_PROM_SCAD_PREAVVISO)){
			if(appendTablePrefix){
				return this.toAliasTable(field)+".avv_mail_prom_scad_preavviso";
			}else{
				return "avv_mail_prom_scad_preavviso";
			}
		}
		if(field.equals(TipoVersamentoDominio.model().TIPO_VERSAMENTO.AVV_MAIL_PROM_SCAD_TIPO)){
			if(appendTablePrefix){
				return this.toAliasTable(field)+".avv_mail_prom_scad_tipo";
			}else{
				return "avv_mail_prom_scad_tipo";
			}
		}
		if(field.equals(TipoVersamentoDominio.model().TIPO_VERSAMENTO.AVV_MAIL_PROM_SCAD_OGGETTO)){
			if(appendTablePrefix){
				return this.toAliasTable(field)+".avv_mail_prom_scad_oggetto";
			}else{
				return "avv_mail_prom_scad_oggetto";
			}
		}
		if(field.equals(TipoVersamentoDominio.model().TIPO_VERSAMENTO.AVV_MAIL_PROM_SCAD_MESSAGGIO)){
			if(appendTablePrefix){
				return this.toAliasTable(field)+".avv_mail_prom_scad_messaggio";
			}else{
				return "avv_mail_prom_scad_messaggio";
			}
		}
		if(field.equals(TipoVersamentoDominio.model().TIPO_VERSAMENTO.VISUALIZZAZIONE_DEFINIZIONE)){
			if(appendTablePrefix){
				return this.toAliasTable(field)+".visualizzazione_definizione";
			}else{
				return "visualizzazione_definizione";
			}
		}
		if(field.equals(TipoVersamentoDominio.model().TIPO_VERSAMENTO.TRAC_CSV_TIPO)){
			if(appendTablePrefix){
				return this.toAliasTable(field)+".trac_csv_tipo";
			}else{
				return "trac_csv_tipo";
			}
		}
		if(field.equals(TipoVersamentoDominio.model().TIPO_VERSAMENTO.TRAC_CSV_HEADER_RISPOSTA)){
			if(appendTablePrefix){
				return this.toAliasTable(field)+".trac_csv_header_risposta";
			}else{
				return "trac_csv_header_risposta";
			}
		}
		if(field.equals(TipoVersamentoDominio.model().TIPO_VERSAMENTO.TRAC_CSV_TEMPLATE_RICHIESTA)){
			if(appendTablePrefix){
				return this.toAliasTable(field)+".trac_csv_template_richiesta";
			}else{
				return "trac_csv_template_richiesta";
			}
		}
		if(field.equals(TipoVersamentoDominio.model().TIPO_VERSAMENTO.TRAC_CSV_TEMPLATE_RISPOSTA)){
			if(appendTablePrefix){
				return this.toAliasTable(field)+".trac_csv_template_risposta";
			}else{
				return "trac_csv_template_risposta";
			}
		}
		if(field.equals(TipoVersamentoDominio.model().TIPO_VERSAMENTO.AVV_APP_IO_PROM_AVV_ABILITATO)){
			if(appendTablePrefix){
				return this.toAliasTable(field)+".avv_app_io_prom_avv_abilitato";
			}else{
				return "avv_app_io_prom_avv_abilitato";
			}
		}
		if(field.equals(TipoVersamentoDominio.model().TIPO_VERSAMENTO.AVV_APP_IO_PROM_AVV_TIPO)){
			if(appendTablePrefix){
				return this.toAliasTable(field)+".avv_app_io_prom_avv_tipo";
			}else{
				return "avv_app_io_prom_avv_tipo";
			}
		}
		if(field.equals(TipoVersamentoDominio.model().TIPO_VERSAMENTO.AVV_APP_IO_PROM_AVV_OGGETTO)){
			if(appendTablePrefix){
				return this.toAliasTable(field)+".avv_app_io_prom_avv_oggetto";
			}else{
				return "avv_app_io_prom_avv_oggetto";
			}
		}
		if(field.equals(TipoVersamentoDominio.model().TIPO_VERSAMENTO.AVV_APP_IO_PROM_AVV_MESSAGGIO)){
			if(appendTablePrefix){
				return this.toAliasTable(field)+".avv_app_io_prom_avv_messaggio";
			}else{
				return "avv_app_io_prom_avv_messaggio";
			}
		}
		if(field.equals(TipoVersamentoDominio.model().TIPO_VERSAMENTO.AVV_APP_IO_PROM_RIC_ABILITATO)){
			if(appendTablePrefix){
				return this.toAliasTable(field)+".avv_app_io_prom_ric_abilitato";
			}else{
				return "avv_app_io_prom_ric_abilitato";
			}
		}
		if(field.equals(TipoVersamentoDominio.model().TIPO_VERSAMENTO.AVV_APP_IO_PROM_RIC_TIPO)){
			if(appendTablePrefix){
				return this.toAliasTable(field)+".avv_app_io_prom_ric_tipo";
			}else{
				return "avv_app_io_prom_ric_tipo";
			}
		}
		if(field.equals(TipoVersamentoDominio.model().TIPO_VERSAMENTO.AVV_APP_IO_PROM_RIC_OGGETTO)){
			if(appendTablePrefix){
				return this.toAliasTable(field)+".avv_app_io_prom_ric_oggetto";
			}else{
				return "avv_app_io_prom_ric_oggetto";
			}
		}
		if(field.equals(TipoVersamentoDominio.model().TIPO_VERSAMENTO.AVV_APP_IO_PROM_RIC_MESSAGGIO)){
			if(appendTablePrefix){
				return this.toAliasTable(field)+".avv_app_io_prom_ric_messaggio";
			}else{
				return "avv_app_io_prom_ric_messaggio";
			}
		}
		if(field.equals(TipoVersamentoDominio.model().TIPO_VERSAMENTO.AVV_APP_IO_PROM_RIC_ESEGUITI)){
			if(appendTablePrefix){
				return this.toAliasTable(field)+".avv_app_io_prom_ric_eseguiti";
			}else{
				return "avv_app_io_prom_ric_eseguiti";
			}
		}
		if(field.equals(TipoVersamentoDominio.model().TIPO_VERSAMENTO.AVV_APP_IO_PROM_SCAD_ABILITATO)){
			if(appendTablePrefix){
				return this.toAliasTable(field)+".avv_app_io_prom_scad_abilitato";
			}else{
				return "avv_app_io_prom_scad_abilitato";
			}
		}
		if(field.equals(TipoVersamentoDominio.model().TIPO_VERSAMENTO.AVV_APP_IO_PROM_SCAD_PREAVVISO)){
			if(appendTablePrefix){
				return this.toAliasTable(field)+".avv_app_io_prom_scad_preavviso";
			}else{
				return "avv_app_io_prom_scad_preavviso";
			}
		}
		if(field.equals(TipoVersamentoDominio.model().TIPO_VERSAMENTO.AVV_APP_IO_PROM_SCAD_TIPO)){
			if(appendTablePrefix){
				return this.toAliasTable(field)+".avv_app_io_prom_scad_tipo";
			}else{
				return "avv_app_io_prom_scad_tipo";
			}
		}
		if(field.equals(TipoVersamentoDominio.model().TIPO_VERSAMENTO.AVV_APP_IO_PROM_SCAD_OGGETTO)){
			if(appendTablePrefix){
				return this.toAliasTable(field)+".avv_app_io_prom_scad_oggetto";
			}else{
				return "avv_app_io_prom_scad_oggetto";
			}
		}
		if(field.equals(TipoVersamentoDominio.model().TIPO_VERSAMENTO.AVV_APP_IO_PROM_SCAD_MESSAGGIO)){
			if(appendTablePrefix){
				return this.toAliasTable(field)+".avv_app_io_prom_scad_messaggio";
			}else{
				return "avv_app_io_prom_scad_messaggio";
			}
		}
		if(field.equals(TipoVersamentoDominio.model().ID_DOMINIO.COD_DOMINIO)){
			if(appendTablePrefix){
				return this.toAliasTable(field)+".cod_dominio";
			}else{
				return "cod_dominio";
			}
		}
		if(field.equals(TipoVersamentoDominio.model().CODIFICA_IUV)){
			if(appendTablePrefix){
				return this.toAliasTable(field)+".codifica_iuv";
			}else{
				return "codifica_iuv";
			}
		}
		if(field.equals(TipoVersamentoDominio.model().TIPO)){
			if(appendTablePrefix){
				return this.toAliasTable(field)+".tipo";
			}else{
				return "tipo";
			}
		}
		if(field.equals(TipoVersamentoDominio.model().PAGA_TERZI)){
			if(appendTablePrefix){
				return this.toAliasTable(field)+".paga_terzi";
			}else{
				return "paga_terzi";
			}
		}
		if(field.equals(TipoVersamentoDominio.model().ABILITATO)){
			if(appendTablePrefix){
				return this.toAliasTable(field)+".abilitato";
			}else{
				return "abilitato";
			}
		}
		if(field.equals(TipoVersamentoDominio.model().BO_FORM_TIPO)){
			if(appendTablePrefix){
				return this.toAliasTable(field)+".bo_form_tipo";
			}else{
				return "bo_form_tipo";
			}
		}
		if(field.equals(TipoVersamentoDominio.model().BO_FORM_DEFINIZIONE)){
			if(appendTablePrefix){
				return this.toAliasTable(field)+".bo_form_definizione";
			}else{
				return "bo_form_definizione";
			}
		}
		if(field.equals(TipoVersamentoDominio.model().BO_VALIDAZIONE_DEF)){
			if(appendTablePrefix){
				return this.toAliasTable(field)+".bo_validazione_def";
			}else{
				return "bo_validazione_def";
			}
		}
		if(field.equals(TipoVersamentoDominio.model().BO_TRASFORMAZIONE_TIPO)){
			if(appendTablePrefix){
				return this.toAliasTable(field)+".bo_trasformazione_tipo";
			}else{
				return "bo_trasformazione_tipo";
			}
		}
		if(field.equals(TipoVersamentoDominio.model().BO_TRASFORMAZIONE_DEF)){
			if(appendTablePrefix){
				return this.toAliasTable(field)+".bo_trasformazione_def";
			}else{
				return "bo_trasformazione_def";
			}
		}
		if(field.equals(TipoVersamentoDominio.model().BO_COD_APPLICAZIONE)){
			if(appendTablePrefix){
				return this.toAliasTable(field)+".bo_cod_applicazione";
			}else{
				return "bo_cod_applicazione";
			}
		}
		if(field.equals(TipoVersamentoDominio.model().BO_ABILITATO)){
			if(appendTablePrefix){
				return this.toAliasTable(field)+".bo_abilitato";
			}else{
				return "bo_abilitato";
			}
		}
		if(field.equals(TipoVersamentoDominio.model().PAG_FORM_TIPO)){
			if(appendTablePrefix){
				return this.toAliasTable(field)+".pag_form_tipo";
			}else{
				return "pag_form_tipo";
			}
		}
		if(field.equals(TipoVersamentoDominio.model().PAG_FORM_DEFINIZIONE)){
			if(appendTablePrefix){
				return this.toAliasTable(field)+".pag_form_definizione";
			}else{
				return "pag_form_definizione";
			}
		}
		if(field.equals(TipoVersamentoDominio.model().PAG_FORM_IMPAGINAZIONE)){
			if(appendTablePrefix){
				return this.toAliasTable(field)+".pag_form_impaginazione";
			}else{
				return "pag_form_impaginazione";
			}
		}
		if(field.equals(TipoVersamentoDominio.model().PAG_VALIDAZIONE_DEF)){
			if(appendTablePrefix){
				return this.toAliasTable(field)+".pag_validazione_def";
			}else{
				return "pag_validazione_def";
			}
		}
		if(field.equals(TipoVersamentoDominio.model().PAG_TRASFORMAZIONE_TIPO)){
			if(appendTablePrefix){
				return this.toAliasTable(field)+".pag_trasformazione_tipo";
			}else{
				return "pag_trasformazione_tipo";
			}
		}
		if(field.equals(TipoVersamentoDominio.model().PAG_TRASFORMAZIONE_DEF)){
			if(appendTablePrefix){
				return this.toAliasTable(field)+".pag_trasformazione_def";
			}else{
				return "pag_trasformazione_def";
			}
		}
		if(field.equals(TipoVersamentoDominio.model().PAG_COD_APPLICAZIONE)){
			if(appendTablePrefix){
				return this.toAliasTable(field)+".pag_cod_applicazione";
			}else{
				return "pag_cod_applicazione";
			}
		}
		if(field.equals(TipoVersamentoDominio.model().PAG_ABILITATO)){
			if(appendTablePrefix){
				return this.toAliasTable(field)+".pag_abilitato";
			}else{
				return "pag_abilitato";
			}
		}
		if(field.equals(TipoVersamentoDominio.model().AVV_MAIL_PROM_AVV_ABILITATO)){
			if(appendTablePrefix){
				return this.toAliasTable(field)+".avv_mail_prom_avv_abilitato";
			}else{
				return "avv_mail_prom_avv_abilitato";
			}
		}
		if(field.equals(TipoVersamentoDominio.model().AVV_MAIL_PROM_AVV_PDF)){
			if(appendTablePrefix){
				return this.toAliasTable(field)+".avv_mail_prom_avv_pdf";
			}else{
				return "avv_mail_prom_avv_pdf";
			}
		}
		if(field.equals(TipoVersamentoDominio.model().AVV_MAIL_PROM_AVV_TIPO)){
			if(appendTablePrefix){
				return this.toAliasTable(field)+".avv_mail_prom_avv_tipo";
			}else{
				return "avv_mail_prom_avv_tipo";
			}
		}
		if(field.equals(TipoVersamentoDominio.model().AVV_MAIL_PROM_AVV_OGGETTO)){
			if(appendTablePrefix){
				return this.toAliasTable(field)+".avv_mail_prom_avv_oggetto";
			}else{
				return "avv_mail_prom_avv_oggetto";
			}
		}
		if(field.equals(TipoVersamentoDominio.model().AVV_MAIL_PROM_AVV_MESSAGGIO)){
			if(appendTablePrefix){
				return this.toAliasTable(field)+".avv_mail_prom_avv_messaggio";
			}else{
				return "avv_mail_prom_avv_messaggio";
			}
		}
		if(field.equals(TipoVersamentoDominio.model().AVV_MAIL_PROM_RIC_ABILITATO)){
			if(appendTablePrefix){
				return this.toAliasTable(field)+".avv_mail_prom_ric_abilitato";
			}else{
				return "avv_mail_prom_ric_abilitato";
			}
		}
		if(field.equals(TipoVersamentoDominio.model().AVV_MAIL_PROM_RIC_PDF)){
			if(appendTablePrefix){
				return this.toAliasTable(field)+".avv_mail_prom_ric_pdf";
			}else{
				return "avv_mail_prom_ric_pdf";
			}
		}
		if(field.equals(TipoVersamentoDominio.model().AVV_MAIL_PROM_RIC_TIPO)){
			if(appendTablePrefix){
				return this.toAliasTable(field)+".avv_mail_prom_ric_tipo";
			}else{
				return "avv_mail_prom_ric_tipo";
			}
		}
		if(field.equals(TipoVersamentoDominio.model().AVV_MAIL_PROM_RIC_OGGETTO)){
			if(appendTablePrefix){
				return this.toAliasTable(field)+".avv_mail_prom_ric_oggetto";
			}else{
				return "avv_mail_prom_ric_oggetto";
			}
		}
		if(field.equals(TipoVersamentoDominio.model().AVV_MAIL_PROM_RIC_MESSAGGIO)){
			if(appendTablePrefix){
				return this.toAliasTable(field)+".avv_mail_prom_ric_messaggio";
			}else{
				return "avv_mail_prom_ric_messaggio";
			}
		}
		if(field.equals(TipoVersamentoDominio.model().AVV_MAIL_PROM_RIC_ESEGUITI)){
			if(appendTablePrefix){
				return this.toAliasTable(field)+".avv_mail_prom_ric_eseguiti";
			}else{
				return "avv_mail_prom_ric_eseguiti";
			}
		}
		if(field.equals(TipoVersamentoDominio.model().AVV_MAIL_PROM_SCAD_ABILITATO)){
			if(appendTablePrefix){
				return this.toAliasTable(field)+".avv_mail_prom_scad_abilitato";
			}else{
				return "avv_mail_prom_scad_abilitato";
			}
		}
		if(field.equals(TipoVersamentoDominio.model().AVV_MAIL_PROM_SCAD_PREAVVISO)){
			if(appendTablePrefix){
				return this.toAliasTable(field)+".avv_mail_prom_scad_preavviso";
			}else{
				return "avv_mail_prom_scad_preavviso";
			}
		}
		if(field.equals(TipoVersamentoDominio.model().AVV_MAIL_PROM_SCAD_TIPO)){
			if(appendTablePrefix){
				return this.toAliasTable(field)+".avv_mail_prom_scad_tipo";
			}else{
				return "avv_mail_prom_scad_tipo";
			}
		}
		if(field.equals(TipoVersamentoDominio.model().AVV_MAIL_PROM_SCAD_OGGETTO)){
			if(appendTablePrefix){
				return this.toAliasTable(field)+".avv_mail_prom_scad_oggetto";
			}else{
				return "avv_mail_prom_scad_oggetto";
			}
		}
		if(field.equals(TipoVersamentoDominio.model().AVV_MAIL_PROM_SCAD_MESSAGGIO)){
			if(appendTablePrefix){
				return this.toAliasTable(field)+".avv_mail_prom_scad_messaggio";
			}else{
				return "avv_mail_prom_scad_messaggio";
			}
		}
		if(field.equals(TipoVersamentoDominio.model().VISUALIZZAZIONE_DEFINIZIONE)){
			if(appendTablePrefix){
				return this.toAliasTable(field)+".visualizzazione_definizione";
			}else{
				return "visualizzazione_definizione";
			}
		}
		if(field.equals(TipoVersamentoDominio.model().TRAC_CSV_TIPO)){
			if(appendTablePrefix){
				return this.toAliasTable(field)+".trac_csv_tipo";
			}else{
				return "trac_csv_tipo";
			}
		}
		if(field.equals(TipoVersamentoDominio.model().TRAC_CSV_HEADER_RISPOSTA)){
			if(appendTablePrefix){
				return this.toAliasTable(field)+".trac_csv_header_risposta";
			}else{
				return "trac_csv_header_risposta";
			}
		}
		if(field.equals(TipoVersamentoDominio.model().TRAC_CSV_TEMPLATE_RICHIESTA)){
			if(appendTablePrefix){
				return this.toAliasTable(field)+".trac_csv_template_richiesta";
			}else{
				return "trac_csv_template_richiesta";
			}
		}
		if(field.equals(TipoVersamentoDominio.model().TRAC_CSV_TEMPLATE_RISPOSTA)){
			if(appendTablePrefix){
				return this.toAliasTable(field)+".trac_csv_template_risposta";
			}else{
				return "trac_csv_template_risposta";
			}
		}
		if(field.equals(TipoVersamentoDominio.model().APP_IO_API_KEY)){
			if(appendTablePrefix){
				return this.toAliasTable(field)+".app_io_api_key";
			}else{
				return "app_io_api_key";
			}
		}
		if(field.equals(TipoVersamentoDominio.model().AVV_APP_IO_PROM_AVV_ABILITATO)){
			if(appendTablePrefix){
				return this.toAliasTable(field)+".avv_app_io_prom_avv_abilitato";
			}else{
				return "avv_app_io_prom_avv_abilitato";
			}
		}
		if(field.equals(TipoVersamentoDominio.model().AVV_APP_IO_PROM_AVV_TIPO)){
			if(appendTablePrefix){
				return this.toAliasTable(field)+".avv_app_io_prom_avv_tipo";
			}else{
				return "avv_app_io_prom_avv_tipo";
			}
		}
		if(field.equals(TipoVersamentoDominio.model().AVV_APP_IO_PROM_AVV_OGGETTO)){
			if(appendTablePrefix){
				return this.toAliasTable(field)+".avv_app_io_prom_avv_oggetto";
			}else{
				return "avv_app_io_prom_avv_oggetto";
			}
		}
		if(field.equals(TipoVersamentoDominio.model().AVV_APP_IO_PROM_AVV_MESSAGGIO)){
			if(appendTablePrefix){
				return this.toAliasTable(field)+".avv_app_io_prom_avv_messaggio";
			}else{
				return "avv_app_io_prom_avv_messaggio";
			}
		}
		if(field.equals(TipoVersamentoDominio.model().AVV_APP_IO_PROM_RIC_ABILITATO)){
			if(appendTablePrefix){
				return this.toAliasTable(field)+".avv_app_io_prom_ric_abilitato";
			}else{
				return "avv_app_io_prom_ric_abilitato";
			}
		}
		if(field.equals(TipoVersamentoDominio.model().AVV_APP_IO_PROM_RIC_TIPO)){
			if(appendTablePrefix){
				return this.toAliasTable(field)+".avv_app_io_prom_ric_tipo";
			}else{
				return "avv_app_io_prom_ric_tipo";
			}
		}
		if(field.equals(TipoVersamentoDominio.model().AVV_APP_IO_PROM_RIC_OGGETTO)){
			if(appendTablePrefix){
				return this.toAliasTable(field)+".avv_app_io_prom_ric_oggetto";
			}else{
				return "avv_app_io_prom_ric_oggetto";
			}
		}
		if(field.equals(TipoVersamentoDominio.model().AVV_APP_IO_PROM_RIC_MESSAGGIO)){
			if(appendTablePrefix){
				return this.toAliasTable(field)+".avv_app_io_prom_ric_messaggio";
			}else{
				return "avv_app_io_prom_ric_messaggio";
			}
		}
		if(field.equals(TipoVersamentoDominio.model().AVV_APP_IO_PROM_RIC_ESEGUITI)){
			if(appendTablePrefix){
				return this.toAliasTable(field)+".avv_app_io_prom_ric_eseguiti";
			}else{
				return "avv_app_io_prom_ric_eseguiti";
			}
		}
		if(field.equals(TipoVersamentoDominio.model().AVV_APP_IO_PROM_SCAD_ABILITATO)){
			if(appendTablePrefix){
				return this.toAliasTable(field)+".avv_app_io_prom_scad_abilitato";
			}else{
				return "avv_app_io_prom_scad_abilitato";
			}
		}
		if(field.equals(TipoVersamentoDominio.model().AVV_APP_IO_PROM_SCAD_PREAVVISO)){
			if(appendTablePrefix){
				return this.toAliasTable(field)+".avv_app_io_prom_scad_preavviso";
			}else{
				return "avv_app_io_prom_scad_preavviso";
			}
		}
		if(field.equals(TipoVersamentoDominio.model().AVV_APP_IO_PROM_SCAD_TIPO)){
			if(appendTablePrefix){
				return this.toAliasTable(field)+".avv_app_io_prom_scad_tipo";
			}else{
				return "avv_app_io_prom_scad_tipo";
			}
		}
		if(field.equals(TipoVersamentoDominio.model().AVV_APP_IO_PROM_SCAD_OGGETTO)){
			if(appendTablePrefix){
				return this.toAliasTable(field)+".avv_app_io_prom_scad_oggetto";
			}else{
				return "avv_app_io_prom_scad_oggetto";
			}
		}
		if(field.equals(TipoVersamentoDominio.model().AVV_APP_IO_PROM_SCAD_MESSAGGIO)){
			if(appendTablePrefix){
				return this.toAliasTable(field)+".avv_app_io_prom_scad_messaggio";
			}else{
				return "avv_app_io_prom_scad_messaggio";
			}
		}


		return super.toColumn(field,returnAlias,appendTablePrefix);
		
	}
	
	@Override
	public String toTable(IField field,boolean returnAlias) throws ExpressionException {
		
		// In the case of table with alias, using parameter returnAlias​​, 
		// it is possible to drive the choice whether to return only the alias or 
		// the full definition of the table containing the alias
		
		if(field.equals(TipoVersamentoDominio.model().TIPO_VERSAMENTO.COD_TIPO_VERSAMENTO)){
			return this.toTable(TipoVersamentoDominio.model().TIPO_VERSAMENTO, returnAlias);
		}
		if(field.equals(TipoVersamentoDominio.model().TIPO_VERSAMENTO.DESCRIZIONE)){
			return this.toTable(TipoVersamentoDominio.model().TIPO_VERSAMENTO, returnAlias);
		}
		if(field.equals(TipoVersamentoDominio.model().TIPO_VERSAMENTO.CODIFICA_IUV)){
			return this.toTable(TipoVersamentoDominio.model().TIPO_VERSAMENTO, returnAlias);
		}
		if(field.equals(TipoVersamentoDominio.model().TIPO_VERSAMENTO.TIPO)){
			return this.toTable(TipoVersamentoDominio.model().TIPO_VERSAMENTO, returnAlias);
		}
		if(field.equals(TipoVersamentoDominio.model().TIPO_VERSAMENTO.PAGA_TERZI)){
			return this.toTable(TipoVersamentoDominio.model().TIPO_VERSAMENTO, returnAlias);
		}
		if(field.equals(TipoVersamentoDominio.model().TIPO_VERSAMENTO.ABILITATO)){
			return this.toTable(TipoVersamentoDominio.model().TIPO_VERSAMENTO, returnAlias);
		}
		if(field.equals(TipoVersamentoDominio.model().TIPO_VERSAMENTO.BO_FORM_TIPO)){
			return this.toTable(TipoVersamentoDominio.model().TIPO_VERSAMENTO, returnAlias);
		}
		if(field.equals(TipoVersamentoDominio.model().TIPO_VERSAMENTO.BO_FORM_DEFINIZIONE)){
			return this.toTable(TipoVersamentoDominio.model().TIPO_VERSAMENTO, returnAlias);
		}
		if(field.equals(TipoVersamentoDominio.model().TIPO_VERSAMENTO.BO_VALIDAZIONE_DEF)){
			return this.toTable(TipoVersamentoDominio.model().TIPO_VERSAMENTO, returnAlias);
		}
		if(field.equals(TipoVersamentoDominio.model().TIPO_VERSAMENTO.BO_TRASFORMAZIONE_TIPO)){
			return this.toTable(TipoVersamentoDominio.model().TIPO_VERSAMENTO, returnAlias);
		}
		if(field.equals(TipoVersamentoDominio.model().TIPO_VERSAMENTO.BO_TRASFORMAZIONE_DEF)){
			return this.toTable(TipoVersamentoDominio.model().TIPO_VERSAMENTO, returnAlias);
		}
		if(field.equals(TipoVersamentoDominio.model().TIPO_VERSAMENTO.BO_COD_APPLICAZIONE)){
			return this.toTable(TipoVersamentoDominio.model().TIPO_VERSAMENTO, returnAlias);
		}
		if(field.equals(TipoVersamentoDominio.model().TIPO_VERSAMENTO.BO_ABILITATO)){
			return this.toTable(TipoVersamentoDominio.model().TIPO_VERSAMENTO, returnAlias);
		}
		if(field.equals(TipoVersamentoDominio.model().TIPO_VERSAMENTO.PAG_FORM_TIPO)){
			return this.toTable(TipoVersamentoDominio.model().TIPO_VERSAMENTO, returnAlias);
		}
		if(field.equals(TipoVersamentoDominio.model().TIPO_VERSAMENTO.PAG_FORM_DEFINIZIONE)){
			return this.toTable(TipoVersamentoDominio.model().TIPO_VERSAMENTO, returnAlias);
		}
		if(field.equals(TipoVersamentoDominio.model().TIPO_VERSAMENTO.PAG_FORM_IMPAGINAZIONE)){
			return this.toTable(TipoVersamentoDominio.model().TIPO_VERSAMENTO, returnAlias);
		}
		if(field.equals(TipoVersamentoDominio.model().TIPO_VERSAMENTO.PAG_VALIDAZIONE_DEF)){
			return this.toTable(TipoVersamentoDominio.model().TIPO_VERSAMENTO, returnAlias);
		}
		if(field.equals(TipoVersamentoDominio.model().TIPO_VERSAMENTO.PAG_TRASFORMAZIONE_TIPO)){
			return this.toTable(TipoVersamentoDominio.model().TIPO_VERSAMENTO, returnAlias);
		}
		if(field.equals(TipoVersamentoDominio.model().TIPO_VERSAMENTO.PAG_TRASFORMAZIONE_DEF)){
			return this.toTable(TipoVersamentoDominio.model().TIPO_VERSAMENTO, returnAlias);
		}
		if(field.equals(TipoVersamentoDominio.model().TIPO_VERSAMENTO.PAG_COD_APPLICAZIONE)){
			return this.toTable(TipoVersamentoDominio.model().TIPO_VERSAMENTO, returnAlias);
		}
		if(field.equals(TipoVersamentoDominio.model().TIPO_VERSAMENTO.PAG_ABILITATO)){
			return this.toTable(TipoVersamentoDominio.model().TIPO_VERSAMENTO, returnAlias);
		}
		if(field.equals(TipoVersamentoDominio.model().TIPO_VERSAMENTO.AVV_MAIL_PROM_AVV_ABILITATO)){
			return this.toTable(TipoVersamentoDominio.model().TIPO_VERSAMENTO, returnAlias);
		}
		if(field.equals(TipoVersamentoDominio.model().TIPO_VERSAMENTO.AVV_MAIL_PROM_AVV_PDF)){
			return this.toTable(TipoVersamentoDominio.model().TIPO_VERSAMENTO, returnAlias);
		}
		if(field.equals(TipoVersamentoDominio.model().TIPO_VERSAMENTO.AVV_MAIL_PROM_AVV_TIPO)){
			return this.toTable(TipoVersamentoDominio.model().TIPO_VERSAMENTO, returnAlias);
		}
		if(field.equals(TipoVersamentoDominio.model().TIPO_VERSAMENTO.AVV_MAIL_PROM_AVV_OGGETTO)){
			return this.toTable(TipoVersamentoDominio.model().TIPO_VERSAMENTO, returnAlias);
		}
		if(field.equals(TipoVersamentoDominio.model().TIPO_VERSAMENTO.AVV_MAIL_PROM_AVV_MESSAGGIO)){
			return this.toTable(TipoVersamentoDominio.model().TIPO_VERSAMENTO, returnAlias);
		}
		if(field.equals(TipoVersamentoDominio.model().TIPO_VERSAMENTO.AVV_MAIL_PROM_RIC_ABILITATO)){
			return this.toTable(TipoVersamentoDominio.model().TIPO_VERSAMENTO, returnAlias);
		}
		if(field.equals(TipoVersamentoDominio.model().TIPO_VERSAMENTO.AVV_MAIL_PROM_RIC_PDF)){
			return this.toTable(TipoVersamentoDominio.model().TIPO_VERSAMENTO, returnAlias);
		}
		if(field.equals(TipoVersamentoDominio.model().TIPO_VERSAMENTO.AVV_MAIL_PROM_RIC_TIPO)){
			return this.toTable(TipoVersamentoDominio.model().TIPO_VERSAMENTO, returnAlias);
		}
		if(field.equals(TipoVersamentoDominio.model().TIPO_VERSAMENTO.AVV_MAIL_PROM_RIC_OGGETTO)){
			return this.toTable(TipoVersamentoDominio.model().TIPO_VERSAMENTO, returnAlias);
		}
		if(field.equals(TipoVersamentoDominio.model().TIPO_VERSAMENTO.AVV_MAIL_PROM_RIC_MESSAGGIO)){
			return this.toTable(TipoVersamentoDominio.model().TIPO_VERSAMENTO, returnAlias);
		}
		if(field.equals(TipoVersamentoDominio.model().TIPO_VERSAMENTO.AVV_MAIL_PROM_RIC_ESEGUITI)){
			return this.toTable(TipoVersamentoDominio.model().TIPO_VERSAMENTO, returnAlias);
		}
		if(field.equals(TipoVersamentoDominio.model().TIPO_VERSAMENTO.AVV_MAIL_PROM_SCAD_ABILITATO)){
			return this.toTable(TipoVersamentoDominio.model().TIPO_VERSAMENTO, returnAlias);
		}
		if(field.equals(TipoVersamentoDominio.model().TIPO_VERSAMENTO.AVV_MAIL_PROM_SCAD_PREAVVISO)){
			return this.toTable(TipoVersamentoDominio.model().TIPO_VERSAMENTO, returnAlias);
		}
		if(field.equals(TipoVersamentoDominio.model().TIPO_VERSAMENTO.AVV_MAIL_PROM_SCAD_TIPO)){
			return this.toTable(TipoVersamentoDominio.model().TIPO_VERSAMENTO, returnAlias);
		}
		if(field.equals(TipoVersamentoDominio.model().TIPO_VERSAMENTO.AVV_MAIL_PROM_SCAD_OGGETTO)){
			return this.toTable(TipoVersamentoDominio.model().TIPO_VERSAMENTO, returnAlias);
		}
		if(field.equals(TipoVersamentoDominio.model().TIPO_VERSAMENTO.AVV_MAIL_PROM_SCAD_MESSAGGIO)){
			return this.toTable(TipoVersamentoDominio.model().TIPO_VERSAMENTO, returnAlias);
		}
		if(field.equals(TipoVersamentoDominio.model().TIPO_VERSAMENTO.VISUALIZZAZIONE_DEFINIZIONE)){
			return this.toTable(TipoVersamentoDominio.model().TIPO_VERSAMENTO, returnAlias);
		}
		if(field.equals(TipoVersamentoDominio.model().TIPO_VERSAMENTO.TRAC_CSV_TIPO)){
			return this.toTable(TipoVersamentoDominio.model().TIPO_VERSAMENTO, returnAlias);
		}
		if(field.equals(TipoVersamentoDominio.model().TIPO_VERSAMENTO.TRAC_CSV_HEADER_RISPOSTA)){
			return this.toTable(TipoVersamentoDominio.model().TIPO_VERSAMENTO, returnAlias);
		}
		if(field.equals(TipoVersamentoDominio.model().TIPO_VERSAMENTO.TRAC_CSV_TEMPLATE_RICHIESTA)){
			return this.toTable(TipoVersamentoDominio.model().TIPO_VERSAMENTO, returnAlias);
		}
		if(field.equals(TipoVersamentoDominio.model().TIPO_VERSAMENTO.TRAC_CSV_TEMPLATE_RISPOSTA)){
			return this.toTable(TipoVersamentoDominio.model().TIPO_VERSAMENTO, returnAlias);
		}
		if(field.equals(TipoVersamentoDominio.model().TIPO_VERSAMENTO.AVV_APP_IO_PROM_AVV_ABILITATO)){
			return this.toTable(TipoVersamentoDominio.model().TIPO_VERSAMENTO, returnAlias);
		}
		if(field.equals(TipoVersamentoDominio.model().TIPO_VERSAMENTO.AVV_APP_IO_PROM_AVV_TIPO)){
			return this.toTable(TipoVersamentoDominio.model().TIPO_VERSAMENTO, returnAlias);
		}
		if(field.equals(TipoVersamentoDominio.model().TIPO_VERSAMENTO.AVV_APP_IO_PROM_AVV_OGGETTO)){
			return this.toTable(TipoVersamentoDominio.model().TIPO_VERSAMENTO, returnAlias);
		}
		if(field.equals(TipoVersamentoDominio.model().TIPO_VERSAMENTO.AVV_APP_IO_PROM_AVV_MESSAGGIO)){
			return this.toTable(TipoVersamentoDominio.model().TIPO_VERSAMENTO, returnAlias);
		}
		if(field.equals(TipoVersamentoDominio.model().TIPO_VERSAMENTO.AVV_APP_IO_PROM_RIC_ABILITATO)){
			return this.toTable(TipoVersamentoDominio.model().TIPO_VERSAMENTO, returnAlias);
		}
		if(field.equals(TipoVersamentoDominio.model().TIPO_VERSAMENTO.AVV_APP_IO_PROM_RIC_TIPO)){
			return this.toTable(TipoVersamentoDominio.model().TIPO_VERSAMENTO, returnAlias);
		}
		if(field.equals(TipoVersamentoDominio.model().TIPO_VERSAMENTO.AVV_APP_IO_PROM_RIC_OGGETTO)){
			return this.toTable(TipoVersamentoDominio.model().TIPO_VERSAMENTO, returnAlias);
		}
		if(field.equals(TipoVersamentoDominio.model().TIPO_VERSAMENTO.AVV_APP_IO_PROM_RIC_MESSAGGIO)){
			return this.toTable(TipoVersamentoDominio.model().TIPO_VERSAMENTO, returnAlias);
		}
		if(field.equals(TipoVersamentoDominio.model().TIPO_VERSAMENTO.AVV_APP_IO_PROM_RIC_ESEGUITI)){
			return this.toTable(TipoVersamentoDominio.model().TIPO_VERSAMENTO, returnAlias);
		}
		if(field.equals(TipoVersamentoDominio.model().TIPO_VERSAMENTO.AVV_APP_IO_PROM_SCAD_ABILITATO)){
			return this.toTable(TipoVersamentoDominio.model().TIPO_VERSAMENTO, returnAlias);
		}
		if(field.equals(TipoVersamentoDominio.model().TIPO_VERSAMENTO.AVV_APP_IO_PROM_SCAD_PREAVVISO)){
			return this.toTable(TipoVersamentoDominio.model().TIPO_VERSAMENTO, returnAlias);
		}
		if(field.equals(TipoVersamentoDominio.model().TIPO_VERSAMENTO.AVV_APP_IO_PROM_SCAD_TIPO)){
			return this.toTable(TipoVersamentoDominio.model().TIPO_VERSAMENTO, returnAlias);
		}
		if(field.equals(TipoVersamentoDominio.model().TIPO_VERSAMENTO.AVV_APP_IO_PROM_SCAD_OGGETTO)){
			return this.toTable(TipoVersamentoDominio.model().TIPO_VERSAMENTO, returnAlias);
		}
		if(field.equals(TipoVersamentoDominio.model().TIPO_VERSAMENTO.AVV_APP_IO_PROM_SCAD_MESSAGGIO)){
			return this.toTable(TipoVersamentoDominio.model().TIPO_VERSAMENTO, returnAlias);
		}
		if(field.equals(TipoVersamentoDominio.model().ID_DOMINIO.COD_DOMINIO)){
			return this.toTable(TipoVersamentoDominio.model().ID_DOMINIO, returnAlias);
		}
		if(field.equals(TipoVersamentoDominio.model().CODIFICA_IUV)){
			return this.toTable(TipoVersamentoDominio.model(), returnAlias);
		}
		if(field.equals(TipoVersamentoDominio.model().TIPO)){
			return this.toTable(TipoVersamentoDominio.model(), returnAlias);
		}
		if(field.equals(TipoVersamentoDominio.model().PAGA_TERZI)){
			return this.toTable(TipoVersamentoDominio.model(), returnAlias);
		}
		if(field.equals(TipoVersamentoDominio.model().ABILITATO)){
			return this.toTable(TipoVersamentoDominio.model(), returnAlias);
		}
		if(field.equals(TipoVersamentoDominio.model().BO_FORM_TIPO)){
			return this.toTable(TipoVersamentoDominio.model(), returnAlias);
		}
		if(field.equals(TipoVersamentoDominio.model().BO_FORM_DEFINIZIONE)){
			return this.toTable(TipoVersamentoDominio.model(), returnAlias);
		}
		if(field.equals(TipoVersamentoDominio.model().BO_VALIDAZIONE_DEF)){
			return this.toTable(TipoVersamentoDominio.model(), returnAlias);
		}
		if(field.equals(TipoVersamentoDominio.model().BO_TRASFORMAZIONE_TIPO)){
			return this.toTable(TipoVersamentoDominio.model(), returnAlias);
		}
		if(field.equals(TipoVersamentoDominio.model().BO_TRASFORMAZIONE_DEF)){
			return this.toTable(TipoVersamentoDominio.model(), returnAlias);
		}
		if(field.equals(TipoVersamentoDominio.model().BO_COD_APPLICAZIONE)){
			return this.toTable(TipoVersamentoDominio.model(), returnAlias);
		}
		if(field.equals(TipoVersamentoDominio.model().BO_ABILITATO)){
			return this.toTable(TipoVersamentoDominio.model(), returnAlias);
		}
		if(field.equals(TipoVersamentoDominio.model().PAG_FORM_TIPO)){
			return this.toTable(TipoVersamentoDominio.model(), returnAlias);
		}
		if(field.equals(TipoVersamentoDominio.model().PAG_FORM_DEFINIZIONE)){
			return this.toTable(TipoVersamentoDominio.model(), returnAlias);
		}
		if(field.equals(TipoVersamentoDominio.model().PAG_FORM_IMPAGINAZIONE)){
			return this.toTable(TipoVersamentoDominio.model(), returnAlias);
		}
		if(field.equals(TipoVersamentoDominio.model().PAG_VALIDAZIONE_DEF)){
			return this.toTable(TipoVersamentoDominio.model(), returnAlias);
		}
		if(field.equals(TipoVersamentoDominio.model().PAG_TRASFORMAZIONE_TIPO)){
			return this.toTable(TipoVersamentoDominio.model(), returnAlias);
		}
		if(field.equals(TipoVersamentoDominio.model().PAG_TRASFORMAZIONE_DEF)){
			return this.toTable(TipoVersamentoDominio.model(), returnAlias);
		}
		if(field.equals(TipoVersamentoDominio.model().PAG_COD_APPLICAZIONE)){
			return this.toTable(TipoVersamentoDominio.model(), returnAlias);
		}
		if(field.equals(TipoVersamentoDominio.model().PAG_ABILITATO)){
			return this.toTable(TipoVersamentoDominio.model(), returnAlias);
		}
		if(field.equals(TipoVersamentoDominio.model().AVV_MAIL_PROM_AVV_ABILITATO)){
			return this.toTable(TipoVersamentoDominio.model(), returnAlias);
		}
		if(field.equals(TipoVersamentoDominio.model().AVV_MAIL_PROM_AVV_PDF)){
			return this.toTable(TipoVersamentoDominio.model(), returnAlias);
		}
		if(field.equals(TipoVersamentoDominio.model().AVV_MAIL_PROM_AVV_TIPO)){
			return this.toTable(TipoVersamentoDominio.model(), returnAlias);
		}
		if(field.equals(TipoVersamentoDominio.model().AVV_MAIL_PROM_AVV_OGGETTO)){
			return this.toTable(TipoVersamentoDominio.model(), returnAlias);
		}
		if(field.equals(TipoVersamentoDominio.model().AVV_MAIL_PROM_AVV_MESSAGGIO)){
			return this.toTable(TipoVersamentoDominio.model(), returnAlias);
		}
		if(field.equals(TipoVersamentoDominio.model().AVV_MAIL_PROM_RIC_ABILITATO)){
			return this.toTable(TipoVersamentoDominio.model(), returnAlias);
		}
		if(field.equals(TipoVersamentoDominio.model().AVV_MAIL_PROM_RIC_PDF)){
			return this.toTable(TipoVersamentoDominio.model(), returnAlias);
		}
		if(field.equals(TipoVersamentoDominio.model().AVV_MAIL_PROM_RIC_TIPO)){
			return this.toTable(TipoVersamentoDominio.model(), returnAlias);
		}
		if(field.equals(TipoVersamentoDominio.model().AVV_MAIL_PROM_RIC_OGGETTO)){
			return this.toTable(TipoVersamentoDominio.model(), returnAlias);
		}
		if(field.equals(TipoVersamentoDominio.model().AVV_MAIL_PROM_RIC_MESSAGGIO)){
			return this.toTable(TipoVersamentoDominio.model(), returnAlias);
		}
		if(field.equals(TipoVersamentoDominio.model().AVV_MAIL_PROM_RIC_ESEGUITI)){
			return this.toTable(TipoVersamentoDominio.model(), returnAlias);
		}
		if(field.equals(TipoVersamentoDominio.model().AVV_MAIL_PROM_SCAD_ABILITATO)){
			return this.toTable(TipoVersamentoDominio.model(), returnAlias);
		}
		if(field.equals(TipoVersamentoDominio.model().AVV_MAIL_PROM_SCAD_PREAVVISO)){
			return this.toTable(TipoVersamentoDominio.model(), returnAlias);
		}
		if(field.equals(TipoVersamentoDominio.model().AVV_MAIL_PROM_SCAD_TIPO)){
			return this.toTable(TipoVersamentoDominio.model(), returnAlias);
		}
		if(field.equals(TipoVersamentoDominio.model().AVV_MAIL_PROM_SCAD_OGGETTO)){
			return this.toTable(TipoVersamentoDominio.model(), returnAlias);
		}
		if(field.equals(TipoVersamentoDominio.model().AVV_MAIL_PROM_SCAD_MESSAGGIO)){
			return this.toTable(TipoVersamentoDominio.model(), returnAlias);
		}
		if(field.equals(TipoVersamentoDominio.model().VISUALIZZAZIONE_DEFINIZIONE)){
			return this.toTable(TipoVersamentoDominio.model(), returnAlias);
		}
		if(field.equals(TipoVersamentoDominio.model().TRAC_CSV_TIPO)){
			return this.toTable(TipoVersamentoDominio.model(), returnAlias);
		}
		if(field.equals(TipoVersamentoDominio.model().TRAC_CSV_HEADER_RISPOSTA)){
			return this.toTable(TipoVersamentoDominio.model(), returnAlias);
		}
		if(field.equals(TipoVersamentoDominio.model().TRAC_CSV_TEMPLATE_RICHIESTA)){
			return this.toTable(TipoVersamentoDominio.model(), returnAlias);
		}
		if(field.equals(TipoVersamentoDominio.model().TRAC_CSV_TEMPLATE_RISPOSTA)){
			return this.toTable(TipoVersamentoDominio.model(), returnAlias);
		}
		if(field.equals(TipoVersamentoDominio.model().APP_IO_API_KEY)){
			return this.toTable(TipoVersamentoDominio.model(), returnAlias);
		}
		if(field.equals(TipoVersamentoDominio.model().AVV_APP_IO_PROM_AVV_ABILITATO)){
			return this.toTable(TipoVersamentoDominio.model(), returnAlias);
		}
		if(field.equals(TipoVersamentoDominio.model().AVV_APP_IO_PROM_AVV_TIPO)){
			return this.toTable(TipoVersamentoDominio.model(), returnAlias);
		}
		if(field.equals(TipoVersamentoDominio.model().AVV_APP_IO_PROM_AVV_OGGETTO)){
			return this.toTable(TipoVersamentoDominio.model(), returnAlias);
		}
		if(field.equals(TipoVersamentoDominio.model().AVV_APP_IO_PROM_AVV_MESSAGGIO)){
			return this.toTable(TipoVersamentoDominio.model(), returnAlias);
		}
		if(field.equals(TipoVersamentoDominio.model().AVV_APP_IO_PROM_RIC_ABILITATO)){
			return this.toTable(TipoVersamentoDominio.model(), returnAlias);
		}
		if(field.equals(TipoVersamentoDominio.model().AVV_APP_IO_PROM_RIC_TIPO)){
			return this.toTable(TipoVersamentoDominio.model(), returnAlias);
		}
		if(field.equals(TipoVersamentoDominio.model().AVV_APP_IO_PROM_RIC_OGGETTO)){
			return this.toTable(TipoVersamentoDominio.model(), returnAlias);
		}
		if(field.equals(TipoVersamentoDominio.model().AVV_APP_IO_PROM_RIC_MESSAGGIO)){
			return this.toTable(TipoVersamentoDominio.model(), returnAlias);
		}
		if(field.equals(TipoVersamentoDominio.model().AVV_APP_IO_PROM_RIC_ESEGUITI)){
			return this.toTable(TipoVersamentoDominio.model(), returnAlias);
		}
		if(field.equals(TipoVersamentoDominio.model().AVV_APP_IO_PROM_SCAD_ABILITATO)){
			return this.toTable(TipoVersamentoDominio.model(), returnAlias);
		}
		if(field.equals(TipoVersamentoDominio.model().AVV_APP_IO_PROM_SCAD_PREAVVISO)){
			return this.toTable(TipoVersamentoDominio.model(), returnAlias);
		}
		if(field.equals(TipoVersamentoDominio.model().AVV_APP_IO_PROM_SCAD_TIPO)){
			return this.toTable(TipoVersamentoDominio.model(), returnAlias);
		}
		if(field.equals(TipoVersamentoDominio.model().AVV_APP_IO_PROM_SCAD_OGGETTO)){
			return this.toTable(TipoVersamentoDominio.model(), returnAlias);
		}
		if(field.equals(TipoVersamentoDominio.model().AVV_APP_IO_PROM_SCAD_MESSAGGIO)){
			return this.toTable(TipoVersamentoDominio.model(), returnAlias);
		}


		return super.toTable(field,returnAlias);
		
	}

	@Override
	public String toTable(IModel<?> model,boolean returnAlias) throws ExpressionException {
		
		// In the case of table with alias, using parameter returnAlias​​, 
		// it is possible to drive the choice whether to return only the alias or 
		// the full definition of the table containing the alias
		
		if(model.equals(TipoVersamentoDominio.model())){
			return "tipi_vers_domini";
		}
		if(model.equals(TipoVersamentoDominio.model().TIPO_VERSAMENTO)){
			return "tipi_versamento";
		}
		if(model.equals(TipoVersamentoDominio.model().ID_DOMINIO)){
			return "domini";
		}


		return super.toTable(model,returnAlias);
		
	}

}
