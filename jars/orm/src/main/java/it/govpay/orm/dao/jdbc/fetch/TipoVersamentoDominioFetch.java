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
package it.govpay.orm.dao.jdbc.fetch;

import org.openspcoop2.generic_project.beans.IModel;
import org.openspcoop2.generic_project.dao.jdbc.utils.AbstractJDBCFetch;
import org.openspcoop2.generic_project.dao.jdbc.utils.JDBCParameterUtilities;
import org.openspcoop2.generic_project.exception.ServiceException;

import java.sql.ResultSet;
import java.util.Map;

import org.openspcoop2.utils.TipiDatabase;
import org.openspcoop2.utils.jdbc.IKeyGeneratorObject;

import it.govpay.orm.TipoVersamento;
import it.govpay.orm.TipoVersamentoDominio;


/**     
 * TipoVersamentoDominioFetch
 *
 * @author Giovanni Bussu (bussu@link.it)
 * @author Lorenzo Nardi (nardi@link.it)
 * @author $Author$
 * @version $Rev$, $Date$
 */
public class TipoVersamentoDominioFetch extends AbstractJDBCFetch {

	@Override
	public Object fetch(TipiDatabase tipoDatabase, IModel<?> model , ResultSet rs) throws ServiceException {
		
		try{
			JDBCParameterUtilities jdbcParameterUtilities =  
					new JDBCParameterUtilities(tipoDatabase);

			if(model.equals(TipoVersamentoDominio.model())){
				TipoVersamentoDominio object = new TipoVersamentoDominio();
				setParameter(object, "setId", Long.class,
					jdbcParameterUtilities.readParameter(rs, "id", Long.class));
				setParameter(object, "setCodificaIuv", TipoVersamentoDominio.model().CODIFICA_IUV.getFieldType(),
					jdbcParameterUtilities.readParameter(rs, "codifica_iuv", TipoVersamentoDominio.model().CODIFICA_IUV.getFieldType()));
				setParameter(object, "setPagaTerzi", TipoVersamentoDominio.model().PAGA_TERZI.getFieldType(),
					jdbcParameterUtilities.readParameter(rs, "paga_terzi", TipoVersamentoDominio.model().PAGA_TERZI.getFieldType()));
				setParameter(object, "setAbilitato", TipoVersamentoDominio.model().ABILITATO.getFieldType(),
					jdbcParameterUtilities.readParameter(rs, "abilitato", TipoVersamentoDominio.model().ABILITATO.getFieldType()));
				setParameter(object, "setBoFormTipo", TipoVersamentoDominio.model().BO_FORM_TIPO.getFieldType(),
					jdbcParameterUtilities.readParameter(rs, "bo_form_tipo", TipoVersamentoDominio.model().BO_FORM_TIPO.getFieldType()));
				setParameter(object, "setBoFormDefinizione", TipoVersamentoDominio.model().BO_FORM_DEFINIZIONE.getFieldType(),
					jdbcParameterUtilities.readParameter(rs, "bo_form_definizione", TipoVersamentoDominio.model().BO_FORM_DEFINIZIONE.getFieldType()));
				setParameter(object, "setBoValidazioneDef", TipoVersamentoDominio.model().BO_VALIDAZIONE_DEF.getFieldType(),
					jdbcParameterUtilities.readParameter(rs, "bo_validazione_def", TipoVersamentoDominio.model().BO_VALIDAZIONE_DEF.getFieldType()));
				setParameter(object, "setBoTrasformazioneTipo", TipoVersamentoDominio.model().BO_TRASFORMAZIONE_TIPO.getFieldType(),
					jdbcParameterUtilities.readParameter(rs, "bo_trasformazione_tipo", TipoVersamentoDominio.model().BO_TRASFORMAZIONE_TIPO.getFieldType()));
				setParameter(object, "setBoTrasformazioneDef", TipoVersamentoDominio.model().BO_TRASFORMAZIONE_DEF.getFieldType(),
					jdbcParameterUtilities.readParameter(rs, "bo_trasformazione_def", TipoVersamentoDominio.model().BO_TRASFORMAZIONE_DEF.getFieldType()));
				setParameter(object, "setBoCodApplicazione", TipoVersamentoDominio.model().BO_COD_APPLICAZIONE.getFieldType(),
					jdbcParameterUtilities.readParameter(rs, "bo_cod_applicazione", TipoVersamentoDominio.model().BO_COD_APPLICAZIONE.getFieldType()));
				setParameter(object, "setBoAbilitato", TipoVersamentoDominio.model().BO_ABILITATO.getFieldType(),
					jdbcParameterUtilities.readParameter(rs, "bo_abilitato", TipoVersamentoDominio.model().BO_ABILITATO.getFieldType()));
				setParameter(object, "setPagFormTipo", TipoVersamentoDominio.model().PAG_FORM_TIPO.getFieldType(),
					jdbcParameterUtilities.readParameter(rs, "pag_form_tipo", TipoVersamentoDominio.model().PAG_FORM_TIPO.getFieldType()));
				setParameter(object, "setPagFormDefinizione", TipoVersamentoDominio.model().PAG_FORM_DEFINIZIONE.getFieldType(),
					jdbcParameterUtilities.readParameter(rs, "pag_form_definizione", TipoVersamentoDominio.model().PAG_FORM_DEFINIZIONE.getFieldType()));
				setParameter(object, "setPagFormImpaginazione", TipoVersamentoDominio.model().PAG_FORM_IMPAGINAZIONE.getFieldType(),
					jdbcParameterUtilities.readParameter(rs, "pag_form_impaginazione", TipoVersamentoDominio.model().PAG_FORM_IMPAGINAZIONE.getFieldType()));
				setParameter(object, "setPagValidazioneDef", TipoVersamentoDominio.model().PAG_VALIDAZIONE_DEF.getFieldType(),
					jdbcParameterUtilities.readParameter(rs, "pag_validazione_def", TipoVersamentoDominio.model().PAG_VALIDAZIONE_DEF.getFieldType()));
				setParameter(object, "setPagTrasformazioneTipo", TipoVersamentoDominio.model().PAG_TRASFORMAZIONE_TIPO.getFieldType(),
					jdbcParameterUtilities.readParameter(rs, "pag_trasformazione_tipo", TipoVersamentoDominio.model().PAG_TRASFORMAZIONE_TIPO.getFieldType()));
				setParameter(object, "setPagTrasformazioneDef", TipoVersamentoDominio.model().PAG_TRASFORMAZIONE_DEF.getFieldType(),
					jdbcParameterUtilities.readParameter(rs, "pag_trasformazione_def", TipoVersamentoDominio.model().PAG_TRASFORMAZIONE_DEF.getFieldType()));
				setParameter(object, "setPagCodApplicazione", TipoVersamentoDominio.model().PAG_COD_APPLICAZIONE.getFieldType(),
					jdbcParameterUtilities.readParameter(rs, "pag_cod_applicazione", TipoVersamentoDominio.model().PAG_COD_APPLICAZIONE.getFieldType()));
				setParameter(object, "setPagAbilitato", TipoVersamentoDominio.model().PAG_ABILITATO.getFieldType(),
					jdbcParameterUtilities.readParameter(rs, "pag_abilitato", TipoVersamentoDominio.model().PAG_ABILITATO.getFieldType()));
				setParameter(object, "setAvvMailPromAvvAbilitato", TipoVersamentoDominio.model().AVV_MAIL_PROM_AVV_ABILITATO.getFieldType(),
					jdbcParameterUtilities.readParameter(rs, "avv_mail_prom_avv_abilitato", TipoVersamentoDominio.model().AVV_MAIL_PROM_AVV_ABILITATO.getFieldType()));
				setParameter(object, "setAvvMailPromAvvPdf", TipoVersamentoDominio.model().AVV_MAIL_PROM_AVV_PDF.getFieldType(),
					jdbcParameterUtilities.readParameter(rs, "avv_mail_prom_avv_pdf", TipoVersamentoDominio.model().AVV_MAIL_PROM_AVV_PDF.getFieldType()));
				setParameter(object, "setAvvMailPromAvvTipo", TipoVersamentoDominio.model().AVV_MAIL_PROM_AVV_TIPO.getFieldType(),
					jdbcParameterUtilities.readParameter(rs, "avv_mail_prom_avv_tipo", TipoVersamentoDominio.model().AVV_MAIL_PROM_AVV_TIPO.getFieldType()));
				setParameter(object, "setAvvMailPromAvvOggetto", TipoVersamentoDominio.model().AVV_MAIL_PROM_AVV_OGGETTO.getFieldType(),
					jdbcParameterUtilities.readParameter(rs, "avv_mail_prom_avv_oggetto", TipoVersamentoDominio.model().AVV_MAIL_PROM_AVV_OGGETTO.getFieldType()));
				setParameter(object, "setAvvMailPromAvvMessaggio", TipoVersamentoDominio.model().AVV_MAIL_PROM_AVV_MESSAGGIO.getFieldType(),
					jdbcParameterUtilities.readParameter(rs, "avv_mail_prom_avv_messaggio", TipoVersamentoDominio.model().AVV_MAIL_PROM_AVV_MESSAGGIO.getFieldType()));
				setParameter(object, "setAvvMailPromRicAbilitato", TipoVersamentoDominio.model().AVV_MAIL_PROM_RIC_ABILITATO.getFieldType(),
					jdbcParameterUtilities.readParameter(rs, "avv_mail_prom_ric_abilitato", TipoVersamentoDominio.model().AVV_MAIL_PROM_RIC_ABILITATO.getFieldType()));
				setParameter(object, "setAvvMailPromRicPdf", TipoVersamentoDominio.model().AVV_MAIL_PROM_RIC_PDF.getFieldType(),
					jdbcParameterUtilities.readParameter(rs, "avv_mail_prom_ric_pdf", TipoVersamentoDominio.model().AVV_MAIL_PROM_RIC_PDF.getFieldType()));
				setParameter(object, "setAvvMailPromRicTipo", TipoVersamentoDominio.model().AVV_MAIL_PROM_RIC_TIPO.getFieldType(),
					jdbcParameterUtilities.readParameter(rs, "avv_mail_prom_ric_tipo", TipoVersamentoDominio.model().AVV_MAIL_PROM_RIC_TIPO.getFieldType()));
				setParameter(object, "setAvvMailPromRicOggetto", TipoVersamentoDominio.model().AVV_MAIL_PROM_RIC_OGGETTO.getFieldType(),
					jdbcParameterUtilities.readParameter(rs, "avv_mail_prom_ric_oggetto", TipoVersamentoDominio.model().AVV_MAIL_PROM_RIC_OGGETTO.getFieldType()));
				setParameter(object, "setAvvMailPromRicMessaggio", TipoVersamentoDominio.model().AVV_MAIL_PROM_RIC_MESSAGGIO.getFieldType(),
					jdbcParameterUtilities.readParameter(rs, "avv_mail_prom_ric_messaggio", TipoVersamentoDominio.model().AVV_MAIL_PROM_RIC_MESSAGGIO.getFieldType()));
				setParameter(object, "setAvvMailPromRicEseguiti", TipoVersamentoDominio.model().AVV_MAIL_PROM_RIC_ESEGUITI.getFieldType(),
					jdbcParameterUtilities.readParameter(rs, "avv_mail_prom_ric_eseguiti", TipoVersamentoDominio.model().AVV_MAIL_PROM_RIC_ESEGUITI.getFieldType()));
				setParameter(object, "setAvvMailPromScadAbilitato", TipoVersamentoDominio.model().AVV_MAIL_PROM_SCAD_ABILITATO.getFieldType(),
					jdbcParameterUtilities.readParameter(rs, "avv_mail_prom_scad_abilitato", TipoVersamentoDominio.model().AVV_MAIL_PROM_SCAD_ABILITATO.getFieldType()));
				setParameter(object, "setAvvMailPromScadPreavviso", TipoVersamentoDominio.model().AVV_MAIL_PROM_SCAD_PREAVVISO.getFieldType(),
					jdbcParameterUtilities.readParameter(rs, "avv_mail_prom_scad_preavviso", TipoVersamentoDominio.model().AVV_MAIL_PROM_SCAD_PREAVVISO.getFieldType()));
				setParameter(object, "setAvvMailPromScadTipo", TipoVersamentoDominio.model().AVV_MAIL_PROM_SCAD_TIPO.getFieldType(),
					jdbcParameterUtilities.readParameter(rs, "avv_mail_prom_scad_tipo", TipoVersamentoDominio.model().AVV_MAIL_PROM_SCAD_TIPO.getFieldType()));
				setParameter(object, "setAvvMailPromScadOggetto", TipoVersamentoDominio.model().AVV_MAIL_PROM_SCAD_OGGETTO.getFieldType(),
					jdbcParameterUtilities.readParameter(rs, "avv_mail_prom_scad_oggetto", TipoVersamentoDominio.model().AVV_MAIL_PROM_SCAD_OGGETTO.getFieldType()));
				setParameter(object, "setAvvMailPromScadMessaggio", TipoVersamentoDominio.model().AVV_MAIL_PROM_SCAD_MESSAGGIO.getFieldType(),
					jdbcParameterUtilities.readParameter(rs, "avv_mail_prom_scad_messaggio", TipoVersamentoDominio.model().AVV_MAIL_PROM_SCAD_MESSAGGIO.getFieldType()));
				setParameter(object, "setVisualizzazioneDefinizione", TipoVersamentoDominio.model().VISUALIZZAZIONE_DEFINIZIONE.getFieldType(),
					jdbcParameterUtilities.readParameter(rs, "visualizzazione_definizione", TipoVersamentoDominio.model().VISUALIZZAZIONE_DEFINIZIONE.getFieldType()));
				setParameter(object, "setTracCsvTipo", TipoVersamentoDominio.model().TRAC_CSV_TIPO.getFieldType(),
					jdbcParameterUtilities.readParameter(rs, "trac_csv_tipo", TipoVersamentoDominio.model().TRAC_CSV_TIPO.getFieldType()));
				setParameter(object, "setTracCsvHeaderRisposta", TipoVersamentoDominio.model().TRAC_CSV_HEADER_RISPOSTA.getFieldType(),
					jdbcParameterUtilities.readParameter(rs, "trac_csv_header_risposta", TipoVersamentoDominio.model().TRAC_CSV_HEADER_RISPOSTA.getFieldType()));
				setParameter(object, "setTracCsvTemplateRichiesta", TipoVersamentoDominio.model().TRAC_CSV_TEMPLATE_RICHIESTA.getFieldType(),
					jdbcParameterUtilities.readParameter(rs, "trac_csv_template_richiesta", TipoVersamentoDominio.model().TRAC_CSV_TEMPLATE_RICHIESTA.getFieldType()));
				setParameter(object, "setTracCsvTemplateRisposta", TipoVersamentoDominio.model().TRAC_CSV_TEMPLATE_RISPOSTA.getFieldType(),
					jdbcParameterUtilities.readParameter(rs, "trac_csv_template_risposta", TipoVersamentoDominio.model().TRAC_CSV_TEMPLATE_RISPOSTA.getFieldType()));
				setParameter(object, "setAppIoApiKey", TipoVersamentoDominio.model().APP_IO_API_KEY.getFieldType(),
					jdbcParameterUtilities.readParameter(rs, "app_io_api_key", TipoVersamentoDominio.model().APP_IO_API_KEY.getFieldType()));
				setParameter(object, "setAvvAppIoPromAvvAbilitato", TipoVersamentoDominio.model().AVV_APP_IO_PROM_AVV_ABILITATO.getFieldType(),
					jdbcParameterUtilities.readParameter(rs, "avv_app_io_prom_avv_abilitato", TipoVersamentoDominio.model().AVV_APP_IO_PROM_AVV_ABILITATO.getFieldType()));
				setParameter(object, "setAvvAppIoPromAvvTipo", TipoVersamentoDominio.model().AVV_APP_IO_PROM_AVV_TIPO.getFieldType(),
					jdbcParameterUtilities.readParameter(rs, "avv_app_io_prom_avv_tipo", TipoVersamentoDominio.model().AVV_APP_IO_PROM_AVV_TIPO.getFieldType()));
				setParameter(object, "setAvvAppIoPromAvvOggetto", TipoVersamentoDominio.model().AVV_APP_IO_PROM_AVV_OGGETTO.getFieldType(),
					jdbcParameterUtilities.readParameter(rs, "avv_app_io_prom_avv_oggetto", TipoVersamentoDominio.model().AVV_APP_IO_PROM_AVV_OGGETTO.getFieldType()));
				setParameter(object, "setAvvAppIoPromAvvMessaggio", TipoVersamentoDominio.model().AVV_APP_IO_PROM_AVV_MESSAGGIO.getFieldType(),
					jdbcParameterUtilities.readParameter(rs, "avv_app_io_prom_avv_messaggio", TipoVersamentoDominio.model().AVV_APP_IO_PROM_AVV_MESSAGGIO.getFieldType()));
				setParameter(object, "setAvvAppIoPromRicAbilitato", TipoVersamentoDominio.model().AVV_APP_IO_PROM_RIC_ABILITATO.getFieldType(),
					jdbcParameterUtilities.readParameter(rs, "avv_app_io_prom_ric_abilitato", TipoVersamentoDominio.model().AVV_APP_IO_PROM_RIC_ABILITATO.getFieldType()));
				setParameter(object, "setAvvAppIoPromRicTipo", TipoVersamentoDominio.model().AVV_APP_IO_PROM_RIC_TIPO.getFieldType(),
					jdbcParameterUtilities.readParameter(rs, "avv_app_io_prom_ric_tipo", TipoVersamentoDominio.model().AVV_APP_IO_PROM_RIC_TIPO.getFieldType()));
				setParameter(object, "setAvvAppIoPromRicOggetto", TipoVersamentoDominio.model().AVV_APP_IO_PROM_RIC_OGGETTO.getFieldType(),
					jdbcParameterUtilities.readParameter(rs, "avv_app_io_prom_ric_oggetto", TipoVersamentoDominio.model().AVV_APP_IO_PROM_RIC_OGGETTO.getFieldType()));
				setParameter(object, "setAvvAppIoPromRicMessaggio", TipoVersamentoDominio.model().AVV_APP_IO_PROM_RIC_MESSAGGIO.getFieldType(),
					jdbcParameterUtilities.readParameter(rs, "avv_app_io_prom_ric_messaggio", TipoVersamentoDominio.model().AVV_APP_IO_PROM_RIC_MESSAGGIO.getFieldType()));
				setParameter(object, "setAvvAppIoPromRicEseguiti", TipoVersamentoDominio.model().AVV_APP_IO_PROM_RIC_ESEGUITI.getFieldType(),
					jdbcParameterUtilities.readParameter(rs, "avv_app_io_prom_ric_eseguiti", TipoVersamentoDominio.model().AVV_APP_IO_PROM_RIC_ESEGUITI.getFieldType()));
				setParameter(object, "setAvvAppIoPromScadAbilitato", TipoVersamentoDominio.model().AVV_APP_IO_PROM_SCAD_ABILITATO.getFieldType(),
					jdbcParameterUtilities.readParameter(rs, "avv_app_io_prom_scad_abilitato", TipoVersamentoDominio.model().AVV_APP_IO_PROM_SCAD_ABILITATO.getFieldType()));
				setParameter(object, "setAvvAppIoPromScadPreavviso", TipoVersamentoDominio.model().AVV_APP_IO_PROM_SCAD_PREAVVISO.getFieldType(),
					jdbcParameterUtilities.readParameter(rs, "avv_app_io_prom_scad_preavviso", TipoVersamentoDominio.model().AVV_APP_IO_PROM_SCAD_PREAVVISO.getFieldType()));
				setParameter(object, "setAvvAppIoPromScadTipo", TipoVersamentoDominio.model().AVV_APP_IO_PROM_SCAD_TIPO.getFieldType(),
					jdbcParameterUtilities.readParameter(rs, "avv_app_io_prom_scad_tipo", TipoVersamentoDominio.model().AVV_APP_IO_PROM_SCAD_TIPO.getFieldType()));
				setParameter(object, "setAvvAppIoPromScadOggetto", TipoVersamentoDominio.model().AVV_APP_IO_PROM_SCAD_OGGETTO.getFieldType(),
					jdbcParameterUtilities.readParameter(rs, "avv_app_io_prom_scad_oggetto", TipoVersamentoDominio.model().AVV_APP_IO_PROM_SCAD_OGGETTO.getFieldType()));
				setParameter(object, "setAvvAppIoPromScadMessaggio", TipoVersamentoDominio.model().AVV_APP_IO_PROM_SCAD_MESSAGGIO.getFieldType(),
					jdbcParameterUtilities.readParameter(rs, "avv_app_io_prom_scad_messaggio", TipoVersamentoDominio.model().AVV_APP_IO_PROM_SCAD_MESSAGGIO.getFieldType()));
				return object;
			} else if(model.equals(TipoVersamentoDominio.model().TIPO_VERSAMENTO)) {
				TipoVersamento object = new TipoVersamento();
				setParameter(object, "setId", Long.class,
					jdbcParameterUtilities.readParameter(rs, "id", Long.class));
				setParameter(object, "setCodTipoVersamento", TipoVersamentoDominio.model().TIPO_VERSAMENTO.COD_TIPO_VERSAMENTO.getFieldType(),
					jdbcParameterUtilities.readParameter(rs, "cod_tipo_versamento", TipoVersamentoDominio.model().TIPO_VERSAMENTO.COD_TIPO_VERSAMENTO.getFieldType()));
				setParameter(object, "setDescrizione", TipoVersamentoDominio.model().TIPO_VERSAMENTO.DESCRIZIONE.getFieldType(),
					jdbcParameterUtilities.readParameter(rs, "descrizione", TipoVersamentoDominio.model().TIPO_VERSAMENTO.DESCRIZIONE.getFieldType()));
				setParameter(object, "setCodificaIuv", TipoVersamentoDominio.model().TIPO_VERSAMENTO.CODIFICA_IUV.getFieldType(),
					jdbcParameterUtilities.readParameter(rs, "codifica_iuv", TipoVersamentoDominio.model().TIPO_VERSAMENTO.CODIFICA_IUV.getFieldType()));
				setParameter(object, "setPagaTerzi", TipoVersamentoDominio.model().TIPO_VERSAMENTO.PAGA_TERZI.getFieldType(),
					jdbcParameterUtilities.readParameter(rs, "paga_terzi", TipoVersamentoDominio.model().TIPO_VERSAMENTO.PAGA_TERZI.getFieldType()));
				setParameter(object, "setAbilitato", TipoVersamentoDominio.model().TIPO_VERSAMENTO.ABILITATO.getFieldType(),
					jdbcParameterUtilities.readParameter(rs, "abilitato", TipoVersamentoDominio.model().TIPO_VERSAMENTO.ABILITATO.getFieldType()));
				setParameter(object, "setBoFormTipo", TipoVersamentoDominio.model().TIPO_VERSAMENTO.BO_FORM_TIPO.getFieldType(),
					jdbcParameterUtilities.readParameter(rs, "bo_form_tipo", TipoVersamentoDominio.model().TIPO_VERSAMENTO.BO_FORM_TIPO.getFieldType()));
				setParameter(object, "setBoFormDefinizione", TipoVersamentoDominio.model().TIPO_VERSAMENTO.BO_FORM_DEFINIZIONE.getFieldType(),
					jdbcParameterUtilities.readParameter(rs, "bo_form_definizione", TipoVersamentoDominio.model().TIPO_VERSAMENTO.BO_FORM_DEFINIZIONE.getFieldType()));
				setParameter(object, "setBoValidazioneDef", TipoVersamentoDominio.model().TIPO_VERSAMENTO.BO_VALIDAZIONE_DEF.getFieldType(),
					jdbcParameterUtilities.readParameter(rs, "bo_validazione_def", TipoVersamentoDominio.model().TIPO_VERSAMENTO.BO_VALIDAZIONE_DEF.getFieldType()));
				setParameter(object, "setBoTrasformazioneTipo", TipoVersamentoDominio.model().TIPO_VERSAMENTO.BO_TRASFORMAZIONE_TIPO.getFieldType(),
					jdbcParameterUtilities.readParameter(rs, "bo_trasformazione_tipo", TipoVersamentoDominio.model().TIPO_VERSAMENTO.BO_TRASFORMAZIONE_TIPO.getFieldType()));
				setParameter(object, "setBoTrasformazioneDef", TipoVersamentoDominio.model().TIPO_VERSAMENTO.BO_TRASFORMAZIONE_DEF.getFieldType(),
					jdbcParameterUtilities.readParameter(rs, "bo_trasformazione_def", TipoVersamentoDominio.model().TIPO_VERSAMENTO.BO_TRASFORMAZIONE_DEF.getFieldType()));
				setParameter(object, "setBoCodApplicazione", TipoVersamentoDominio.model().TIPO_VERSAMENTO.BO_COD_APPLICAZIONE.getFieldType(),
					jdbcParameterUtilities.readParameter(rs, "bo_cod_applicazione", TipoVersamentoDominio.model().TIPO_VERSAMENTO.BO_COD_APPLICAZIONE.getFieldType()));
				setParameter(object, "setBoAbilitato", TipoVersamentoDominio.model().TIPO_VERSAMENTO.BO_ABILITATO.getFieldType(),
					jdbcParameterUtilities.readParameter(rs, "bo_abilitato", TipoVersamentoDominio.model().TIPO_VERSAMENTO.BO_ABILITATO.getFieldType()));
				setParameter(object, "setPagFormTipo", TipoVersamentoDominio.model().TIPO_VERSAMENTO.PAG_FORM_TIPO.getFieldType(),
					jdbcParameterUtilities.readParameter(rs, "pag_form_tipo", TipoVersamentoDominio.model().TIPO_VERSAMENTO.PAG_FORM_TIPO.getFieldType()));
				setParameter(object, "setPagFormDefinizione", TipoVersamentoDominio.model().TIPO_VERSAMENTO.PAG_FORM_DEFINIZIONE.getFieldType(),
					jdbcParameterUtilities.readParameter(rs, "pag_form_definizione", TipoVersamentoDominio.model().TIPO_VERSAMENTO.PAG_FORM_DEFINIZIONE.getFieldType()));
				setParameter(object, "setPagFormImpaginazione", TipoVersamentoDominio.model().TIPO_VERSAMENTO.PAG_FORM_IMPAGINAZIONE.getFieldType(),
					jdbcParameterUtilities.readParameter(rs, "pag_form_impaginazione", TipoVersamentoDominio.model().TIPO_VERSAMENTO.PAG_FORM_IMPAGINAZIONE.getFieldType()));
				setParameter(object, "setPagValidazioneDef", TipoVersamentoDominio.model().TIPO_VERSAMENTO.PAG_VALIDAZIONE_DEF.getFieldType(),
					jdbcParameterUtilities.readParameter(rs, "pag_validazione_def", TipoVersamentoDominio.model().TIPO_VERSAMENTO.PAG_VALIDAZIONE_DEF.getFieldType()));
				setParameter(object, "setPagTrasformazioneTipo", TipoVersamentoDominio.model().TIPO_VERSAMENTO.PAG_TRASFORMAZIONE_TIPO.getFieldType(),
					jdbcParameterUtilities.readParameter(rs, "pag_trasformazione_tipo", TipoVersamentoDominio.model().TIPO_VERSAMENTO.PAG_TRASFORMAZIONE_TIPO.getFieldType()));
				setParameter(object, "setPagTrasformazioneDef", TipoVersamentoDominio.model().TIPO_VERSAMENTO.PAG_TRASFORMAZIONE_DEF.getFieldType(),
					jdbcParameterUtilities.readParameter(rs, "pag_trasformazione_def", TipoVersamentoDominio.model().TIPO_VERSAMENTO.PAG_TRASFORMAZIONE_DEF.getFieldType()));
				setParameter(object, "setPagCodApplicazione", TipoVersamentoDominio.model().TIPO_VERSAMENTO.PAG_COD_APPLICAZIONE.getFieldType(),
					jdbcParameterUtilities.readParameter(rs, "pag_cod_applicazione", TipoVersamentoDominio.model().TIPO_VERSAMENTO.PAG_COD_APPLICAZIONE.getFieldType()));
				setParameter(object, "setPagAbilitato", TipoVersamentoDominio.model().TIPO_VERSAMENTO.PAG_ABILITATO.getFieldType(),
					jdbcParameterUtilities.readParameter(rs, "pag_abilitato", TipoVersamentoDominio.model().TIPO_VERSAMENTO.PAG_ABILITATO.getFieldType()));
				setParameter(object, "setAvvMailPromAvvAbilitato", TipoVersamentoDominio.model().TIPO_VERSAMENTO.AVV_MAIL_PROM_AVV_ABILITATO.getFieldType(),
					jdbcParameterUtilities.readParameter(rs, "avv_mail_prom_avv_abilitato", TipoVersamentoDominio.model().TIPO_VERSAMENTO.AVV_MAIL_PROM_AVV_ABILITATO.getFieldType()));
				setParameter(object, "setAvvMailPromAvvPdf", TipoVersamentoDominio.model().TIPO_VERSAMENTO.AVV_MAIL_PROM_AVV_PDF.getFieldType(),
					jdbcParameterUtilities.readParameter(rs, "avv_mail_prom_avv_pdf", TipoVersamentoDominio.model().TIPO_VERSAMENTO.AVV_MAIL_PROM_AVV_PDF.getFieldType()));
				setParameter(object, "setAvvMailPromAvvTipo", TipoVersamentoDominio.model().TIPO_VERSAMENTO.AVV_MAIL_PROM_AVV_TIPO.getFieldType(),
					jdbcParameterUtilities.readParameter(rs, "avv_mail_prom_avv_tipo", TipoVersamentoDominio.model().TIPO_VERSAMENTO.AVV_MAIL_PROM_AVV_TIPO.getFieldType()));
				setParameter(object, "setAvvMailPromAvvOggetto", TipoVersamentoDominio.model().TIPO_VERSAMENTO.AVV_MAIL_PROM_AVV_OGGETTO.getFieldType(),
					jdbcParameterUtilities.readParameter(rs, "avv_mail_prom_avv_oggetto", TipoVersamentoDominio.model().TIPO_VERSAMENTO.AVV_MAIL_PROM_AVV_OGGETTO.getFieldType()));
				setParameter(object, "setAvvMailPromAvvMessaggio", TipoVersamentoDominio.model().TIPO_VERSAMENTO.AVV_MAIL_PROM_AVV_MESSAGGIO.getFieldType(),
					jdbcParameterUtilities.readParameter(rs, "avv_mail_prom_avv_messaggio", TipoVersamentoDominio.model().TIPO_VERSAMENTO.AVV_MAIL_PROM_AVV_MESSAGGIO.getFieldType()));
				setParameter(object, "setAvvMailPromRicAbilitato", TipoVersamentoDominio.model().TIPO_VERSAMENTO.AVV_MAIL_PROM_RIC_ABILITATO.getFieldType(),
					jdbcParameterUtilities.readParameter(rs, "avv_mail_prom_ric_abilitato", TipoVersamentoDominio.model().TIPO_VERSAMENTO.AVV_MAIL_PROM_RIC_ABILITATO.getFieldType()));
				setParameter(object, "setAvvMailPromRicPdf", TipoVersamentoDominio.model().TIPO_VERSAMENTO.AVV_MAIL_PROM_RIC_PDF.getFieldType(),
					jdbcParameterUtilities.readParameter(rs, "avv_mail_prom_ric_pdf", TipoVersamentoDominio.model().TIPO_VERSAMENTO.AVV_MAIL_PROM_RIC_PDF.getFieldType()));
				setParameter(object, "setAvvMailPromRicTipo", TipoVersamentoDominio.model().TIPO_VERSAMENTO.AVV_MAIL_PROM_RIC_TIPO.getFieldType(),
					jdbcParameterUtilities.readParameter(rs, "avv_mail_prom_ric_tipo", TipoVersamentoDominio.model().TIPO_VERSAMENTO.AVV_MAIL_PROM_RIC_TIPO.getFieldType()));
				setParameter(object, "setAvvMailPromRicOggetto", TipoVersamentoDominio.model().TIPO_VERSAMENTO.AVV_MAIL_PROM_RIC_OGGETTO.getFieldType(),
					jdbcParameterUtilities.readParameter(rs, "avv_mail_prom_ric_oggetto", TipoVersamentoDominio.model().TIPO_VERSAMENTO.AVV_MAIL_PROM_RIC_OGGETTO.getFieldType()));
				setParameter(object, "setAvvMailPromRicMessaggio", TipoVersamentoDominio.model().TIPO_VERSAMENTO.AVV_MAIL_PROM_RIC_MESSAGGIO.getFieldType(),
					jdbcParameterUtilities.readParameter(rs, "avv_mail_prom_ric_messaggio", TipoVersamentoDominio.model().TIPO_VERSAMENTO.AVV_MAIL_PROM_RIC_MESSAGGIO.getFieldType()));
				setParameter(object, "setAvvMailPromRicEseguiti", TipoVersamentoDominio.model().TIPO_VERSAMENTO.AVV_MAIL_PROM_RIC_ESEGUITI.getFieldType(),
					jdbcParameterUtilities.readParameter(rs, "avv_mail_prom_ric_eseguiti", TipoVersamentoDominio.model().TIPO_VERSAMENTO.AVV_MAIL_PROM_RIC_ESEGUITI.getFieldType()));
				setParameter(object, "setAvvMailPromScadAbilitato", TipoVersamentoDominio.model().TIPO_VERSAMENTO.AVV_MAIL_PROM_SCAD_ABILITATO.getFieldType(),
					jdbcParameterUtilities.readParameter(rs, "avv_mail_prom_scad_abilitato", TipoVersamentoDominio.model().TIPO_VERSAMENTO.AVV_MAIL_PROM_SCAD_ABILITATO.getFieldType()));
				setParameter(object, "setAvvMailPromScadPreavviso", TipoVersamentoDominio.model().TIPO_VERSAMENTO.AVV_MAIL_PROM_SCAD_PREAVVISO.getFieldType(),
					jdbcParameterUtilities.readParameter(rs, "avv_mail_prom_scad_preavviso", TipoVersamentoDominio.model().TIPO_VERSAMENTO.AVV_MAIL_PROM_SCAD_PREAVVISO.getFieldType()));
				setParameter(object, "setAvvMailPromScadTipo", TipoVersamentoDominio.model().TIPO_VERSAMENTO.AVV_MAIL_PROM_SCAD_TIPO.getFieldType(),
					jdbcParameterUtilities.readParameter(rs, "avv_mail_prom_scad_tipo", TipoVersamentoDominio.model().TIPO_VERSAMENTO.AVV_MAIL_PROM_SCAD_TIPO.getFieldType()));
				setParameter(object, "setAvvMailPromScadOggetto", TipoVersamentoDominio.model().TIPO_VERSAMENTO.AVV_MAIL_PROM_SCAD_OGGETTO.getFieldType(),
					jdbcParameterUtilities.readParameter(rs, "avv_mail_prom_scad_oggetto", TipoVersamentoDominio.model().TIPO_VERSAMENTO.AVV_MAIL_PROM_SCAD_OGGETTO.getFieldType()));
				setParameter(object, "setAvvMailPromScadMessaggio", TipoVersamentoDominio.model().TIPO_VERSAMENTO.AVV_MAIL_PROM_SCAD_MESSAGGIO.getFieldType(),
					jdbcParameterUtilities.readParameter(rs, "avv_mail_prom_scad_messaggio", TipoVersamentoDominio.model().TIPO_VERSAMENTO.AVV_MAIL_PROM_SCAD_MESSAGGIO.getFieldType()));
				setParameter(object, "setVisualizzazioneDefinizione", TipoVersamentoDominio.model().TIPO_VERSAMENTO.VISUALIZZAZIONE_DEFINIZIONE.getFieldType(),
					jdbcParameterUtilities.readParameter(rs, "visualizzazione_definizione", TipoVersamentoDominio.model().TIPO_VERSAMENTO.VISUALIZZAZIONE_DEFINIZIONE.getFieldType()));
				setParameter(object, "setTracCsvTipo", TipoVersamentoDominio.model().TIPO_VERSAMENTO.TRAC_CSV_TIPO.getFieldType(),
					jdbcParameterUtilities.readParameter(rs, "trac_csv_tipo", TipoVersamentoDominio.model().TIPO_VERSAMENTO.TRAC_CSV_TIPO.getFieldType()));
				setParameter(object, "setTracCsvHeaderRisposta", TipoVersamentoDominio.model().TIPO_VERSAMENTO.TRAC_CSV_HEADER_RISPOSTA.getFieldType(),
					jdbcParameterUtilities.readParameter(rs, "trac_csv_header_risposta", TipoVersamentoDominio.model().TIPO_VERSAMENTO.TRAC_CSV_HEADER_RISPOSTA.getFieldType()));
				setParameter(object, "setTracCsvTemplateRichiesta", TipoVersamentoDominio.model().TIPO_VERSAMENTO.TRAC_CSV_TEMPLATE_RICHIESTA.getFieldType(),
					jdbcParameterUtilities.readParameter(rs, "trac_csv_template_richiesta", TipoVersamentoDominio.model().TIPO_VERSAMENTO.TRAC_CSV_TEMPLATE_RICHIESTA.getFieldType()));
				setParameter(object, "setTracCsvTemplateRisposta", TipoVersamentoDominio.model().TIPO_VERSAMENTO.TRAC_CSV_TEMPLATE_RISPOSTA.getFieldType(),
					jdbcParameterUtilities.readParameter(rs, "trac_csv_template_risposta", TipoVersamentoDominio.model().TIPO_VERSAMENTO.TRAC_CSV_TEMPLATE_RISPOSTA.getFieldType()));
				setParameter(object, "setAvvAppIoPromAvvAbilitato", TipoVersamentoDominio.model().TIPO_VERSAMENTO.AVV_APP_IO_PROM_AVV_ABILITATO.getFieldType(),
					jdbcParameterUtilities.readParameter(rs, "avv_app_io_prom_avv_abilitato", TipoVersamentoDominio.model().TIPO_VERSAMENTO.AVV_APP_IO_PROM_AVV_ABILITATO.getFieldType()));
				setParameter(object, "setAvvAppIoPromAvvTipo", TipoVersamentoDominio.model().TIPO_VERSAMENTO.AVV_APP_IO_PROM_AVV_TIPO.getFieldType(),
					jdbcParameterUtilities.readParameter(rs, "avv_app_io_prom_avv_tipo", TipoVersamentoDominio.model().TIPO_VERSAMENTO.AVV_APP_IO_PROM_AVV_TIPO.getFieldType()));
				setParameter(object, "setAvvAppIoPromAvvOggetto", TipoVersamentoDominio.model().TIPO_VERSAMENTO.AVV_APP_IO_PROM_AVV_OGGETTO.getFieldType(),
					jdbcParameterUtilities.readParameter(rs, "avv_app_io_prom_avv_oggetto", TipoVersamentoDominio.model().TIPO_VERSAMENTO.AVV_APP_IO_PROM_AVV_OGGETTO.getFieldType()));
				setParameter(object, "setAvvAppIoPromAvvMessaggio", TipoVersamentoDominio.model().TIPO_VERSAMENTO.AVV_APP_IO_PROM_AVV_MESSAGGIO.getFieldType(),
					jdbcParameterUtilities.readParameter(rs, "avv_app_io_prom_avv_messaggio", TipoVersamentoDominio.model().TIPO_VERSAMENTO.AVV_APP_IO_PROM_AVV_MESSAGGIO.getFieldType()));
				setParameter(object, "setAvvAppIoPromRicAbilitato", TipoVersamentoDominio.model().TIPO_VERSAMENTO.AVV_APP_IO_PROM_RIC_ABILITATO.getFieldType(),
					jdbcParameterUtilities.readParameter(rs, "avv_app_io_prom_ric_abilitato", TipoVersamentoDominio.model().TIPO_VERSAMENTO.AVV_APP_IO_PROM_RIC_ABILITATO.getFieldType()));
				setParameter(object, "setAvvAppIoPromRicTipo", TipoVersamentoDominio.model().TIPO_VERSAMENTO.AVV_APP_IO_PROM_RIC_TIPO.getFieldType(),
					jdbcParameterUtilities.readParameter(rs, "avv_app_io_prom_ric_tipo", TipoVersamentoDominio.model().TIPO_VERSAMENTO.AVV_APP_IO_PROM_RIC_TIPO.getFieldType()));
				setParameter(object, "setAvvAppIoPromRicOggetto", TipoVersamentoDominio.model().TIPO_VERSAMENTO.AVV_APP_IO_PROM_RIC_OGGETTO.getFieldType(),
					jdbcParameterUtilities.readParameter(rs, "avv_app_io_prom_ric_oggetto", TipoVersamentoDominio.model().TIPO_VERSAMENTO.AVV_APP_IO_PROM_RIC_OGGETTO.getFieldType()));
				setParameter(object, "setAvvAppIoPromRicMessaggio", TipoVersamentoDominio.model().TIPO_VERSAMENTO.AVV_APP_IO_PROM_RIC_MESSAGGIO.getFieldType(),
					jdbcParameterUtilities.readParameter(rs, "avv_app_io_prom_ric_messaggio", TipoVersamentoDominio.model().TIPO_VERSAMENTO.AVV_APP_IO_PROM_RIC_MESSAGGIO.getFieldType()));
				setParameter(object, "setAvvAppIoPromRicEseguiti", TipoVersamentoDominio.model().TIPO_VERSAMENTO.AVV_APP_IO_PROM_RIC_ESEGUITI.getFieldType(),
					jdbcParameterUtilities.readParameter(rs, "avv_app_io_prom_ric_eseguiti", TipoVersamentoDominio.model().TIPO_VERSAMENTO.AVV_APP_IO_PROM_RIC_ESEGUITI.getFieldType()));
				setParameter(object, "setAvvAppIoPromScadAbilitato", TipoVersamentoDominio.model().TIPO_VERSAMENTO.AVV_APP_IO_PROM_SCAD_ABILITATO.getFieldType(),
					jdbcParameterUtilities.readParameter(rs, "avv_app_io_prom_scad_abilitato", TipoVersamentoDominio.model().TIPO_VERSAMENTO.AVV_APP_IO_PROM_SCAD_ABILITATO.getFieldType()));
				setParameter(object, "setAvvAppIoPromScadPreavviso", TipoVersamentoDominio.model().TIPO_VERSAMENTO.AVV_APP_IO_PROM_SCAD_PREAVVISO.getFieldType(),
					jdbcParameterUtilities.readParameter(rs, "avv_app_io_prom_scad_preavviso", TipoVersamentoDominio.model().TIPO_VERSAMENTO.AVV_APP_IO_PROM_SCAD_PREAVVISO.getFieldType()));
				setParameter(object, "setAvvAppIoPromScadTipo", TipoVersamentoDominio.model().TIPO_VERSAMENTO.AVV_APP_IO_PROM_SCAD_TIPO.getFieldType(),
					jdbcParameterUtilities.readParameter(rs, "avv_app_io_prom_scad_tipo", TipoVersamentoDominio.model().TIPO_VERSAMENTO.AVV_APP_IO_PROM_SCAD_TIPO.getFieldType()));
				setParameter(object, "setAvvAppIoPromScadOggetto", TipoVersamentoDominio.model().TIPO_VERSAMENTO.AVV_APP_IO_PROM_SCAD_OGGETTO.getFieldType(),
					jdbcParameterUtilities.readParameter(rs, "avv_app_io_prom_scad_oggetto", TipoVersamentoDominio.model().TIPO_VERSAMENTO.AVV_APP_IO_PROM_SCAD_OGGETTO.getFieldType()));
				setParameter(object, "setAvvAppIoPromScadMessaggio", TipoVersamentoDominio.model().TIPO_VERSAMENTO.AVV_APP_IO_PROM_SCAD_MESSAGGIO.getFieldType(),
					jdbcParameterUtilities.readParameter(rs, "avv_app_io_prom_scad_messaggio", TipoVersamentoDominio.model().TIPO_VERSAMENTO.AVV_APP_IO_PROM_SCAD_MESSAGGIO.getFieldType()));
				return object;
			}
			
			else{
				throw new ServiceException("Model ["+model.toString()+"] not supported by fetch: "+this.getClass().getName());
			}	
					
		}catch(Exception e){
			throw new ServiceException("Model ["+model.toString()+"] occurs error in fetch: "+e.getMessage(),e);
		}
		
	}
	
	@Override
	public Object fetch(TipiDatabase tipoDatabase, IModel<?> model , Map<String,Object> map ) throws ServiceException {
		
		try{

			if(model.equals(TipoVersamentoDominio.model())){
				TipoVersamentoDominio object = new TipoVersamentoDominio();
				setParameter(object, "setId", Long.class,
					this.getObjectFromMap(map,"id"));
				setParameter(object, "setCodificaIuv", TipoVersamentoDominio.model().CODIFICA_IUV.getFieldType(),
					this.getObjectFromMap(map,"codificaIuv"));
				setParameter(object, "setPagaTerzi", TipoVersamentoDominio.model().PAGA_TERZI.getFieldType(),
					this.getObjectFromMap(map,"pagaTerzi"));
				setParameter(object, "setAbilitato", TipoVersamentoDominio.model().ABILITATO.getFieldType(),
					this.getObjectFromMap(map,"abilitato"));
				setParameter(object, "setBoFormTipo", TipoVersamentoDominio.model().BO_FORM_TIPO.getFieldType(),
					this.getObjectFromMap(map,"boFormTipo"));
				setParameter(object, "setBoFormDefinizione", TipoVersamentoDominio.model().BO_FORM_DEFINIZIONE.getFieldType(),
					this.getObjectFromMap(map,"boFormDefinizione"));
				setParameter(object, "setBoValidazioneDef", TipoVersamentoDominio.model().BO_VALIDAZIONE_DEF.getFieldType(),
					this.getObjectFromMap(map,"boValidazioneDef"));
				setParameter(object, "setBoTrasformazioneTipo", TipoVersamentoDominio.model().BO_TRASFORMAZIONE_TIPO.getFieldType(),
					this.getObjectFromMap(map,"boTrasformazioneTipo"));
				setParameter(object, "setBoTrasformazioneDef", TipoVersamentoDominio.model().BO_TRASFORMAZIONE_DEF.getFieldType(),
					this.getObjectFromMap(map,"boTrasformazioneDef"));
				setParameter(object, "setBoCodApplicazione", TipoVersamentoDominio.model().BO_COD_APPLICAZIONE.getFieldType(),
					this.getObjectFromMap(map,"boCodApplicazione"));
				setParameter(object, "setBoAbilitato", TipoVersamentoDominio.model().BO_ABILITATO.getFieldType(),
					this.getObjectFromMap(map,"boAbilitato"));
				setParameter(object, "setPagFormTipo", TipoVersamentoDominio.model().PAG_FORM_TIPO.getFieldType(),
					this.getObjectFromMap(map,"pagFormTipo"));
				setParameter(object, "setPagFormDefinizione", TipoVersamentoDominio.model().PAG_FORM_DEFINIZIONE.getFieldType(),
					this.getObjectFromMap(map,"pagFormDefinizione"));
				setParameter(object, "setPagFormImpaginazione", TipoVersamentoDominio.model().PAG_FORM_IMPAGINAZIONE.getFieldType(),
					this.getObjectFromMap(map,"pagFormImpaginazione"));
				setParameter(object, "setPagValidazioneDef", TipoVersamentoDominio.model().PAG_VALIDAZIONE_DEF.getFieldType(),
					this.getObjectFromMap(map,"pagValidazioneDef"));
				setParameter(object, "setPagTrasformazioneTipo", TipoVersamentoDominio.model().PAG_TRASFORMAZIONE_TIPO.getFieldType(),
					this.getObjectFromMap(map,"pagTrasformazioneTipo"));
				setParameter(object, "setPagTrasformazioneDef", TipoVersamentoDominio.model().PAG_TRASFORMAZIONE_DEF.getFieldType(),
					this.getObjectFromMap(map,"pagTrasformazioneDef"));
				setParameter(object, "setPagCodApplicazione", TipoVersamentoDominio.model().PAG_COD_APPLICAZIONE.getFieldType(),
					this.getObjectFromMap(map,"pagCodApplicazione"));
				setParameter(object, "setPagAbilitato", TipoVersamentoDominio.model().PAG_ABILITATO.getFieldType(),
					this.getObjectFromMap(map,"pagAbilitato"));
				setParameter(object, "setAvvMailPromAvvAbilitato", TipoVersamentoDominio.model().AVV_MAIL_PROM_AVV_ABILITATO.getFieldType(),
					this.getObjectFromMap(map,"avvMailPromAvvAbilitato"));
				setParameter(object, "setAvvMailPromAvvPdf", TipoVersamentoDominio.model().AVV_MAIL_PROM_AVV_PDF.getFieldType(),
					this.getObjectFromMap(map,"avvMailPromAvvPdf"));
				setParameter(object, "setAvvMailPromAvvTipo", TipoVersamentoDominio.model().AVV_MAIL_PROM_AVV_TIPO.getFieldType(),
					this.getObjectFromMap(map,"avvMailPromAvvTipo"));
				setParameter(object, "setAvvMailPromAvvOggetto", TipoVersamentoDominio.model().AVV_MAIL_PROM_AVV_OGGETTO.getFieldType(),
					this.getObjectFromMap(map,"avvMailPromAvvOggetto"));
				setParameter(object, "setAvvMailPromAvvMessaggio", TipoVersamentoDominio.model().AVV_MAIL_PROM_AVV_MESSAGGIO.getFieldType(),
					this.getObjectFromMap(map,"avvMailPromAvvMessaggio"));
				setParameter(object, "setAvvMailPromRicAbilitato", TipoVersamentoDominio.model().AVV_MAIL_PROM_RIC_ABILITATO.getFieldType(),
					this.getObjectFromMap(map,"avvMailPromRicAbilitato"));
				setParameter(object, "setAvvMailPromRicPdf", TipoVersamentoDominio.model().AVV_MAIL_PROM_RIC_PDF.getFieldType(),
					this.getObjectFromMap(map,"avvMailPromRicPdf"));
				setParameter(object, "setAvvMailPromRicTipo", TipoVersamentoDominio.model().AVV_MAIL_PROM_RIC_TIPO.getFieldType(),
					this.getObjectFromMap(map,"avvMailPromRicTipo"));
				setParameter(object, "setAvvMailPromRicOggetto", TipoVersamentoDominio.model().AVV_MAIL_PROM_RIC_OGGETTO.getFieldType(),
					this.getObjectFromMap(map,"avvMailPromRicOggetto"));
				setParameter(object, "setAvvMailPromRicMessaggio", TipoVersamentoDominio.model().AVV_MAIL_PROM_RIC_MESSAGGIO.getFieldType(),
					this.getObjectFromMap(map,"avvMailPromRicMessaggio"));
				setParameter(object, "setAvvMailPromRicEseguiti", TipoVersamentoDominio.model().AVV_MAIL_PROM_RIC_ESEGUITI.getFieldType(),
					this.getObjectFromMap(map,"avvMailPromRicEseguiti"));
				setParameter(object, "setAvvMailPromScadAbilitato", TipoVersamentoDominio.model().AVV_MAIL_PROM_SCAD_ABILITATO.getFieldType(),
					this.getObjectFromMap(map,"avvMailPromScadAbilitato"));
				setParameter(object, "setAvvMailPromScadPreavviso", TipoVersamentoDominio.model().AVV_MAIL_PROM_SCAD_PREAVVISO.getFieldType(),
					this.getObjectFromMap(map,"avvMailPromScadPreavviso"));
				setParameter(object, "setAvvMailPromScadTipo", TipoVersamentoDominio.model().AVV_MAIL_PROM_SCAD_TIPO.getFieldType(),
					this.getObjectFromMap(map,"avvMailPromScadTipo"));
				setParameter(object, "setAvvMailPromScadOggetto", TipoVersamentoDominio.model().AVV_MAIL_PROM_SCAD_OGGETTO.getFieldType(),
					this.getObjectFromMap(map,"avvMailPromScadOggetto"));
				setParameter(object, "setAvvMailPromScadMessaggio", TipoVersamentoDominio.model().AVV_MAIL_PROM_SCAD_MESSAGGIO.getFieldType(),
					this.getObjectFromMap(map,"avvMailPromScadMessaggio"));
				setParameter(object, "setVisualizzazioneDefinizione", TipoVersamentoDominio.model().VISUALIZZAZIONE_DEFINIZIONE.getFieldType(),
					this.getObjectFromMap(map,"visualizzazioneDefinizione"));
				setParameter(object, "setTracCsvTipo", TipoVersamentoDominio.model().TRAC_CSV_TIPO.getFieldType(),
					this.getObjectFromMap(map,"tracCsvTipo"));
				setParameter(object, "setTracCsvHeaderRisposta", TipoVersamentoDominio.model().TRAC_CSV_HEADER_RISPOSTA.getFieldType(),
					this.getObjectFromMap(map,"tracCsvHeaderRisposta"));
				setParameter(object, "setTracCsvTemplateRichiesta", TipoVersamentoDominio.model().TRAC_CSV_TEMPLATE_RICHIESTA.getFieldType(),
					this.getObjectFromMap(map,"tracCsvTemplateRichiesta"));
				setParameter(object, "setTracCsvTemplateRisposta", TipoVersamentoDominio.model().TRAC_CSV_TEMPLATE_RISPOSTA.getFieldType(),
					this.getObjectFromMap(map,"tracCsvTemplateRisposta"));
				setParameter(object, "setAppIoApiKey", TipoVersamentoDominio.model().APP_IO_API_KEY.getFieldType(),
					this.getObjectFromMap(map,"appIoApiKey"));
				setParameter(object, "setAvvAppIoPromAvvAbilitato", TipoVersamentoDominio.model().AVV_APP_IO_PROM_AVV_ABILITATO.getFieldType(),
					this.getObjectFromMap(map,"avvAppIoPromAvvAbilitato"));
				setParameter(object, "setAvvAppIoPromAvvTipo", TipoVersamentoDominio.model().AVV_APP_IO_PROM_AVV_TIPO.getFieldType(),
					this.getObjectFromMap(map,"avvAppIoPromAvvTipo"));
				setParameter(object, "setAvvAppIoPromAvvOggetto", TipoVersamentoDominio.model().AVV_APP_IO_PROM_AVV_OGGETTO.getFieldType(),
					this.getObjectFromMap(map,"avvAppIoPromAvvOggetto"));
				setParameter(object, "setAvvAppIoPromAvvMessaggio", TipoVersamentoDominio.model().AVV_APP_IO_PROM_AVV_MESSAGGIO.getFieldType(),
					this.getObjectFromMap(map,"avvAppIoPromAvvMessaggio"));
				setParameter(object, "setAvvAppIoPromRicAbilitato", TipoVersamentoDominio.model().AVV_APP_IO_PROM_RIC_ABILITATO.getFieldType(),
					this.getObjectFromMap(map,"avvAppIoPromRicAbilitato"));
				setParameter(object, "setAvvAppIoPromRicTipo", TipoVersamentoDominio.model().AVV_APP_IO_PROM_RIC_TIPO.getFieldType(),
					this.getObjectFromMap(map,"avvAppIoPromRicTipo"));
				setParameter(object, "setAvvAppIoPromRicOggetto", TipoVersamentoDominio.model().AVV_APP_IO_PROM_RIC_OGGETTO.getFieldType(),
					this.getObjectFromMap(map,"avvAppIoPromRicOggetto"));
				setParameter(object, "setAvvAppIoPromRicMessaggio", TipoVersamentoDominio.model().AVV_APP_IO_PROM_RIC_MESSAGGIO.getFieldType(),
					this.getObjectFromMap(map,"avvAppIoPromRicMessaggio"));
				setParameter(object, "setAvvAppIoPromRicEseguiti", TipoVersamentoDominio.model().AVV_APP_IO_PROM_RIC_ESEGUITI.getFieldType(),
					this.getObjectFromMap(map,"avvAppIoPromRicEseguiti"));
				setParameter(object, "setAvvAppIoPromScadAbilitato", TipoVersamentoDominio.model().AVV_APP_IO_PROM_SCAD_ABILITATO.getFieldType(),
					this.getObjectFromMap(map,"avvAppIoPromScadAbilitato"));
				setParameter(object, "setAvvAppIoPromScadPreavviso", TipoVersamentoDominio.model().AVV_APP_IO_PROM_SCAD_PREAVVISO.getFieldType(),
					this.getObjectFromMap(map,"avvAppIoPromScadPreavviso"));
				setParameter(object, "setAvvAppIoPromScadTipo", TipoVersamentoDominio.model().AVV_APP_IO_PROM_SCAD_TIPO.getFieldType(),
					this.getObjectFromMap(map,"avvAppIoPromScadTipo"));
				setParameter(object, "setAvvAppIoPromScadOggetto", TipoVersamentoDominio.model().AVV_APP_IO_PROM_SCAD_OGGETTO.getFieldType(),
					this.getObjectFromMap(map,"avvAppIoPromScadOggetto"));
				setParameter(object, "setAvvAppIoPromScadMessaggio", TipoVersamentoDominio.model().AVV_APP_IO_PROM_SCAD_MESSAGGIO.getFieldType(),
					this.getObjectFromMap(map,"avvAppIoPromScadMessaggio"));
				return object;
			} else if(model.equals(TipoVersamentoDominio.model().TIPO_VERSAMENTO)) {
				TipoVersamento object = new TipoVersamento();
				setParameter(object, "setId", Long.class,
					this.getObjectFromMap(map,"tipoVersamento.id"));
				setParameter(object, "setCodTipoVersamento", TipoVersamentoDominio.model().TIPO_VERSAMENTO.COD_TIPO_VERSAMENTO.getFieldType(),
					this.getObjectFromMap(map,"tipoVersamento.codTipoVersamento"));
				setParameter(object, "setDescrizione", TipoVersamentoDominio.model().TIPO_VERSAMENTO.DESCRIZIONE.getFieldType(),
					this.getObjectFromMap(map,"tipoVersamento.descrizione"));
				setParameter(object, "setCodificaIuv", TipoVersamentoDominio.model().TIPO_VERSAMENTO.CODIFICA_IUV.getFieldType(),
					this.getObjectFromMap(map,"tipoVersamento.codificaIuv"));
				setParameter(object, "setPagaTerzi", TipoVersamentoDominio.model().TIPO_VERSAMENTO.PAGA_TERZI.getFieldType(),
					this.getObjectFromMap(map,"tipoVersamento.pagaTerzi"));
				setParameter(object, "setAbilitato", TipoVersamentoDominio.model().TIPO_VERSAMENTO.ABILITATO.getFieldType(),
					this.getObjectFromMap(map,"tipoVersamento.abilitato"));
				setParameter(object, "setBoFormTipo", TipoVersamentoDominio.model().TIPO_VERSAMENTO.BO_FORM_TIPO.getFieldType(),
					this.getObjectFromMap(map,"tipoVersamento.boFormTipo"));
				setParameter(object, "setBoFormDefinizione", TipoVersamentoDominio.model().TIPO_VERSAMENTO.BO_FORM_DEFINIZIONE.getFieldType(),
					this.getObjectFromMap(map,"tipoVersamento.boFormDefinizione"));
				setParameter(object, "setBoValidazioneDef", TipoVersamentoDominio.model().TIPO_VERSAMENTO.BO_VALIDAZIONE_DEF.getFieldType(),
					this.getObjectFromMap(map,"tipoVersamento.boValidazioneDef"));
				setParameter(object, "setBoTrasformazioneTipo", TipoVersamentoDominio.model().TIPO_VERSAMENTO.BO_TRASFORMAZIONE_TIPO.getFieldType(),
					this.getObjectFromMap(map,"tipoVersamento.boTrasformazioneTipo"));
				setParameter(object, "setBoTrasformazioneDef", TipoVersamentoDominio.model().TIPO_VERSAMENTO.BO_TRASFORMAZIONE_DEF.getFieldType(),
					this.getObjectFromMap(map,"tipoVersamento.boTrasformazioneDef"));
				setParameter(object, "setBoCodApplicazione", TipoVersamentoDominio.model().TIPO_VERSAMENTO.BO_COD_APPLICAZIONE.getFieldType(),
					this.getObjectFromMap(map,"tipoVersamento.boCodApplicazione"));
				setParameter(object, "setBoAbilitato", TipoVersamentoDominio.model().TIPO_VERSAMENTO.BO_ABILITATO.getFieldType(),
					this.getObjectFromMap(map,"tipoVersamento.boAbilitato"));
				setParameter(object, "setPagFormTipo", TipoVersamentoDominio.model().TIPO_VERSAMENTO.PAG_FORM_TIPO.getFieldType(),
					this.getObjectFromMap(map,"tipoVersamento.pagFormTipo"));
				setParameter(object, "setPagFormDefinizione", TipoVersamentoDominio.model().TIPO_VERSAMENTO.PAG_FORM_DEFINIZIONE.getFieldType(),
					this.getObjectFromMap(map,"tipoVersamento.pagFormDefinizione"));
				setParameter(object, "setPagFormImpaginazione", TipoVersamentoDominio.model().TIPO_VERSAMENTO.PAG_FORM_IMPAGINAZIONE.getFieldType(),
					this.getObjectFromMap(map,"tipoVersamento.pagFormImpaginazione"));
				setParameter(object, "setPagValidazioneDef", TipoVersamentoDominio.model().TIPO_VERSAMENTO.PAG_VALIDAZIONE_DEF.getFieldType(),
					this.getObjectFromMap(map,"tipoVersamento.pagValidazioneDef"));
				setParameter(object, "setPagTrasformazioneTipo", TipoVersamentoDominio.model().TIPO_VERSAMENTO.PAG_TRASFORMAZIONE_TIPO.getFieldType(),
					this.getObjectFromMap(map,"tipoVersamento.pagTrasformazioneTipo"));
				setParameter(object, "setPagTrasformazioneDef", TipoVersamentoDominio.model().TIPO_VERSAMENTO.PAG_TRASFORMAZIONE_DEF.getFieldType(),
					this.getObjectFromMap(map,"tipoVersamento.pagTrasformazioneDef"));
				setParameter(object, "setPagCodApplicazione", TipoVersamentoDominio.model().TIPO_VERSAMENTO.PAG_COD_APPLICAZIONE.getFieldType(),
					this.getObjectFromMap(map,"tipoVersamento.pagCodApplicazione"));
				setParameter(object, "setPagAbilitato", TipoVersamentoDominio.model().TIPO_VERSAMENTO.PAG_ABILITATO.getFieldType(),
					this.getObjectFromMap(map,"tipoVersamento.pagAbilitato"));
				setParameter(object, "setAvvMailPromAvvAbilitato", TipoVersamentoDominio.model().TIPO_VERSAMENTO.AVV_MAIL_PROM_AVV_ABILITATO.getFieldType(),
					this.getObjectFromMap(map,"tipoVersamento.avvMailPromAvvAbilitato"));
				setParameter(object, "setAvvMailPromAvvPdf", TipoVersamentoDominio.model().TIPO_VERSAMENTO.AVV_MAIL_PROM_AVV_PDF.getFieldType(),
					this.getObjectFromMap(map,"tipoVersamento.avvMailPromAvvPdf"));
				setParameter(object, "setAvvMailPromAvvTipo", TipoVersamentoDominio.model().TIPO_VERSAMENTO.AVV_MAIL_PROM_AVV_TIPO.getFieldType(),
					this.getObjectFromMap(map,"tipoVersamento.avvMailPromAvvTipo"));
				setParameter(object, "setAvvMailPromAvvOggetto", TipoVersamentoDominio.model().TIPO_VERSAMENTO.AVV_MAIL_PROM_AVV_OGGETTO.getFieldType(),
					this.getObjectFromMap(map,"tipoVersamento.avvMailPromAvvOggetto"));
				setParameter(object, "setAvvMailPromAvvMessaggio", TipoVersamentoDominio.model().TIPO_VERSAMENTO.AVV_MAIL_PROM_AVV_MESSAGGIO.getFieldType(),
					this.getObjectFromMap(map,"tipoVersamento.avvMailPromAvvMessaggio"));
				setParameter(object, "setAvvMailPromRicAbilitato", TipoVersamentoDominio.model().TIPO_VERSAMENTO.AVV_MAIL_PROM_RIC_ABILITATO.getFieldType(),
					this.getObjectFromMap(map,"tipoVersamento.avvMailPromRicAbilitato"));
				setParameter(object, "setAvvMailPromRicPdf", TipoVersamentoDominio.model().TIPO_VERSAMENTO.AVV_MAIL_PROM_RIC_PDF.getFieldType(),
					this.getObjectFromMap(map,"tipoVersamento.avvMailPromRicPdf"));
				setParameter(object, "setAvvMailPromRicTipo", TipoVersamentoDominio.model().TIPO_VERSAMENTO.AVV_MAIL_PROM_RIC_TIPO.getFieldType(),
					this.getObjectFromMap(map,"tipoVersamento.avvMailPromRicTipo"));
				setParameter(object, "setAvvMailPromRicOggetto", TipoVersamentoDominio.model().TIPO_VERSAMENTO.AVV_MAIL_PROM_RIC_OGGETTO.getFieldType(),
					this.getObjectFromMap(map,"tipoVersamento.avvMailPromRicOggetto"));
				setParameter(object, "setAvvMailPromRicMessaggio", TipoVersamentoDominio.model().TIPO_VERSAMENTO.AVV_MAIL_PROM_RIC_MESSAGGIO.getFieldType(),
					this.getObjectFromMap(map,"tipoVersamento.avvMailPromRicMessaggio"));
				setParameter(object, "setAvvMailPromRicEseguiti", TipoVersamentoDominio.model().TIPO_VERSAMENTO.AVV_MAIL_PROM_RIC_ESEGUITI.getFieldType(),
					this.getObjectFromMap(map,"tipoVersamento.avvMailPromRicEseguiti"));
				setParameter(object, "setAvvMailPromScadAbilitato", TipoVersamentoDominio.model().TIPO_VERSAMENTO.AVV_MAIL_PROM_SCAD_ABILITATO.getFieldType(),
					this.getObjectFromMap(map,"tipoVersamento.avvMailPromScadAbilitato"));
				setParameter(object, "setAvvMailPromScadPreavviso", TipoVersamentoDominio.model().TIPO_VERSAMENTO.AVV_MAIL_PROM_SCAD_PREAVVISO.getFieldType(),
					this.getObjectFromMap(map,"tipoVersamento.avvMailPromScadPreavviso"));
				setParameter(object, "setAvvMailPromScadTipo", TipoVersamentoDominio.model().TIPO_VERSAMENTO.AVV_MAIL_PROM_SCAD_TIPO.getFieldType(),
					this.getObjectFromMap(map,"tipoVersamento.avvMailPromScadTipo"));
				setParameter(object, "setAvvMailPromScadOggetto", TipoVersamentoDominio.model().TIPO_VERSAMENTO.AVV_MAIL_PROM_SCAD_OGGETTO.getFieldType(),
					this.getObjectFromMap(map,"tipoVersamento.avvMailPromScadOggetto"));
				setParameter(object, "setAvvMailPromScadMessaggio", TipoVersamentoDominio.model().TIPO_VERSAMENTO.AVV_MAIL_PROM_SCAD_MESSAGGIO.getFieldType(),
					this.getObjectFromMap(map,"tipoVersamento.avvMailPromScadMessaggio"));
				setParameter(object, "setVisualizzazioneDefinizione", TipoVersamentoDominio.model().TIPO_VERSAMENTO.VISUALIZZAZIONE_DEFINIZIONE.getFieldType(),
					this.getObjectFromMap(map,"tipoVersamento.visualizzazioneDefinizione"));
				setParameter(object, "setTracCsvTipo", TipoVersamentoDominio.model().TIPO_VERSAMENTO.TRAC_CSV_TIPO.getFieldType(),
					this.getObjectFromMap(map,"tipoVersamento.tracCsvTipo"));
				setParameter(object, "setTracCsvHeaderRisposta", TipoVersamentoDominio.model().TIPO_VERSAMENTO.TRAC_CSV_HEADER_RISPOSTA.getFieldType(),
					this.getObjectFromMap(map,"tipoVersamento.tracCsvHeaderRisposta"));
				setParameter(object, "setTracCsvTemplateRichiesta", TipoVersamentoDominio.model().TIPO_VERSAMENTO.TRAC_CSV_TEMPLATE_RICHIESTA.getFieldType(),
					this.getObjectFromMap(map,"tipoVersamento.tracCsvTemplateRichiesta"));
				setParameter(object, "setTracCsvTemplateRisposta", TipoVersamentoDominio.model().TIPO_VERSAMENTO.TRAC_CSV_TEMPLATE_RISPOSTA.getFieldType(),
					this.getObjectFromMap(map,"tipoVersamento.tracCsvTemplateRisposta"));
				setParameter(object, "setAvvAppIoPromAvvAbilitato", TipoVersamentoDominio.model().TIPO_VERSAMENTO.AVV_APP_IO_PROM_AVV_ABILITATO.getFieldType(),
					this.getObjectFromMap(map,"tipoVersamento.avvAppIoPromAvvAbilitato"));
				setParameter(object, "setAvvAppIoPromAvvTipo", TipoVersamentoDominio.model().TIPO_VERSAMENTO.AVV_APP_IO_PROM_AVV_TIPO.getFieldType(),
					this.getObjectFromMap(map,"tipoVersamento.avvAppIoPromAvvTipo"));
				setParameter(object, "setAvvAppIoPromAvvOggetto", TipoVersamentoDominio.model().TIPO_VERSAMENTO.AVV_APP_IO_PROM_AVV_OGGETTO.getFieldType(),
					this.getObjectFromMap(map,"tipoVersamento.avvAppIoPromAvvOggetto"));
				setParameter(object, "setAvvAppIoPromAvvMessaggio", TipoVersamentoDominio.model().TIPO_VERSAMENTO.AVV_APP_IO_PROM_AVV_MESSAGGIO.getFieldType(),
					this.getObjectFromMap(map,"tipoVersamento.avvAppIoPromAvvMessaggio"));
				setParameter(object, "setAvvAppIoPromRicAbilitato", TipoVersamentoDominio.model().TIPO_VERSAMENTO.AVV_APP_IO_PROM_RIC_ABILITATO.getFieldType(),
					this.getObjectFromMap(map,"tipoVersamento.avvAppIoPromRicAbilitato"));
				setParameter(object, "setAvvAppIoPromRicTipo", TipoVersamentoDominio.model().TIPO_VERSAMENTO.AVV_APP_IO_PROM_RIC_TIPO.getFieldType(),
					this.getObjectFromMap(map,"tipoVersamento.avvAppIoPromRicTipo"));
				setParameter(object, "setAvvAppIoPromRicOggetto", TipoVersamentoDominio.model().TIPO_VERSAMENTO.AVV_APP_IO_PROM_RIC_OGGETTO.getFieldType(),
					this.getObjectFromMap(map,"tipoVersamento.avvAppIoPromRicOggetto"));
				setParameter(object, "setAvvAppIoPromRicMessaggio", TipoVersamentoDominio.model().TIPO_VERSAMENTO.AVV_APP_IO_PROM_RIC_MESSAGGIO.getFieldType(),
					this.getObjectFromMap(map,"tipoVersamento.avvAppIoPromRicMessaggio"));
				setParameter(object, "setAvvAppIoPromRicEseguiti", TipoVersamentoDominio.model().TIPO_VERSAMENTO.AVV_APP_IO_PROM_RIC_ESEGUITI.getFieldType(),
					this.getObjectFromMap(map,"tipoVersamento.avvAppIoPromRicEseguiti"));
				setParameter(object, "setAvvAppIoPromScadAbilitato", TipoVersamentoDominio.model().TIPO_VERSAMENTO.AVV_APP_IO_PROM_SCAD_ABILITATO.getFieldType(),
					this.getObjectFromMap(map,"tipoVersamento.avvAppIoPromScadAbilitato"));
				setParameter(object, "setAvvAppIoPromScadPreavviso", TipoVersamentoDominio.model().TIPO_VERSAMENTO.AVV_APP_IO_PROM_SCAD_PREAVVISO.getFieldType(),
					this.getObjectFromMap(map,"tipoVersamento.avvAppIoPromScadPreavviso"));
				setParameter(object, "setAvvAppIoPromScadTipo", TipoVersamentoDominio.model().TIPO_VERSAMENTO.AVV_APP_IO_PROM_SCAD_TIPO.getFieldType(),
					this.getObjectFromMap(map,"tipoVersamento.avvAppIoPromScadTipo"));
				setParameter(object, "setAvvAppIoPromScadOggetto", TipoVersamentoDominio.model().TIPO_VERSAMENTO.AVV_APP_IO_PROM_SCAD_OGGETTO.getFieldType(),
					this.getObjectFromMap(map,"tipoVersamento.avvAppIoPromScadOggetto"));
				setParameter(object, "setAvvAppIoPromScadMessaggio", TipoVersamentoDominio.model().TIPO_VERSAMENTO.AVV_APP_IO_PROM_SCAD_MESSAGGIO.getFieldType(),
					this.getObjectFromMap(map,"tipoVersamento.avvAppIoPromScadMessaggio"));
				return object;
			}
			
			else{
				throw new ServiceException("Model ["+model.toString()+"] not supported by fetch: "+this.getClass().getName());
			}	
					
		}catch(Exception e){
			throw new ServiceException("Model ["+model.toString()+"] occurs error in fetch: "+e.getMessage(),e);
		}
		
	}
	
	
	@Override
	public IKeyGeneratorObject getKeyGeneratorObject( IModel<?> model )  throws ServiceException {
		
		try{

			if(model.equals(TipoVersamentoDominio.model())){
				return new org.openspcoop2.utils.jdbc.CustomKeyGeneratorObject("tipi_vers_domini","id","seq_tipi_vers_domini","tipi_vers_domini_init_seq");
			}
			
			else{
				throw new ServiceException("Model ["+model.toString()+"] not supported by getKeyGeneratorObject: "+this.getClass().getName());
			}

		}catch(Exception e){
			throw new ServiceException("Model ["+model.toString()+"] occurs error in getKeyGeneratorObject: "+e.getMessage(),e);
		}
		
	}

}
