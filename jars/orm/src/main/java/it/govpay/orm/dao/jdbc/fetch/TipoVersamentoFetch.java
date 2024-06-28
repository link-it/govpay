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
package it.govpay.orm.dao.jdbc.fetch;

import org.openspcoop2.generic_project.beans.IModel;
import org.openspcoop2.generic_project.dao.jdbc.utils.AbstractJDBCFetch;
import org.openspcoop2.generic_project.dao.jdbc.utils.GenericJDBCParameterUtilities;
import org.openspcoop2.generic_project.exception.ServiceException;

import java.sql.ResultSet;
import java.util.Map;

import org.openspcoop2.utils.TipiDatabase;
import org.openspcoop2.utils.jdbc.IKeyGeneratorObject;

import it.govpay.orm.TipoVersamento;


/**     
 * TipoVersamentoFetch
 *
 * @author Giovanni Bussu (bussu@link.it)
 * @author Lorenzo Nardi (nardi@link.it)
 * @author $Author$
 * @version $Rev$, $Date$
 */
public class TipoVersamentoFetch extends AbstractJDBCFetch {

	@Override
	public Object fetch(TipiDatabase tipoDatabase, IModel<?> model , ResultSet rs) throws ServiceException {
		
		try{
			GenericJDBCParameterUtilities GenericJDBCParameterUtilities =  
					new GenericJDBCParameterUtilities(tipoDatabase);

			if(model.equals(TipoVersamento.model())){
				TipoVersamento object = new TipoVersamento();
				setParameter(object, "setId", Long.class,
					GenericJDBCParameterUtilities.readParameter(rs, "id", Long.class));
				setParameter(object, "setCodTipoVersamento", TipoVersamento.model().COD_TIPO_VERSAMENTO.getFieldType(),
					GenericJDBCParameterUtilities.readParameter(rs, "cod_tipo_versamento", TipoVersamento.model().COD_TIPO_VERSAMENTO.getFieldType()));
				setParameter(object, "setDescrizione", TipoVersamento.model().DESCRIZIONE.getFieldType(),
					GenericJDBCParameterUtilities.readParameter(rs, "descrizione", TipoVersamento.model().DESCRIZIONE.getFieldType()));
				setParameter(object, "setCodificaIuv", TipoVersamento.model().CODIFICA_IUV.getFieldType(),
					GenericJDBCParameterUtilities.readParameter(rs, "codifica_iuv", TipoVersamento.model().CODIFICA_IUV.getFieldType()));
				setParameter(object, "setPagaTerzi", TipoVersamento.model().PAGA_TERZI.getFieldType(),
					GenericJDBCParameterUtilities.readParameter(rs, "paga_terzi", TipoVersamento.model().PAGA_TERZI.getFieldType()));
				setParameter(object, "setAbilitato", TipoVersamento.model().ABILITATO.getFieldType(),
					GenericJDBCParameterUtilities.readParameter(rs, "abilitato", TipoVersamento.model().ABILITATO.getFieldType()));
				setParameter(object, "setBoFormTipo", TipoVersamento.model().BO_FORM_TIPO.getFieldType(),
					GenericJDBCParameterUtilities.readParameter(rs, "bo_form_tipo", TipoVersamento.model().BO_FORM_TIPO.getFieldType()));
				setParameter(object, "setBoFormDefinizione", TipoVersamento.model().BO_FORM_DEFINIZIONE.getFieldType(),
					GenericJDBCParameterUtilities.readParameter(rs, "bo_form_definizione", TipoVersamento.model().BO_FORM_DEFINIZIONE.getFieldType()));
				setParameter(object, "setBoValidazioneDef", TipoVersamento.model().BO_VALIDAZIONE_DEF.getFieldType(),
					GenericJDBCParameterUtilities.readParameter(rs, "bo_validazione_def", TipoVersamento.model().BO_VALIDAZIONE_DEF.getFieldType()));
				setParameter(object, "setBoTrasformazioneTipo", TipoVersamento.model().BO_TRASFORMAZIONE_TIPO.getFieldType(),
					GenericJDBCParameterUtilities.readParameter(rs, "bo_trasformazione_tipo", TipoVersamento.model().BO_TRASFORMAZIONE_TIPO.getFieldType()));
				setParameter(object, "setBoTrasformazioneDef", TipoVersamento.model().BO_TRASFORMAZIONE_DEF.getFieldType(),
					GenericJDBCParameterUtilities.readParameter(rs, "bo_trasformazione_def", TipoVersamento.model().BO_TRASFORMAZIONE_DEF.getFieldType()));
				setParameter(object, "setBoCodApplicazione", TipoVersamento.model().BO_COD_APPLICAZIONE.getFieldType(),
					GenericJDBCParameterUtilities.readParameter(rs, "bo_cod_applicazione", TipoVersamento.model().BO_COD_APPLICAZIONE.getFieldType()));
				setParameter(object, "setBoAbilitato", TipoVersamento.model().BO_ABILITATO.getFieldType(),
					GenericJDBCParameterUtilities.readParameter(rs, "bo_abilitato", TipoVersamento.model().BO_ABILITATO.getFieldType()));
				setParameter(object, "setPagFormTipo", TipoVersamento.model().PAG_FORM_TIPO.getFieldType(),
					GenericJDBCParameterUtilities.readParameter(rs, "pag_form_tipo", TipoVersamento.model().PAG_FORM_TIPO.getFieldType()));
				setParameter(object, "setPagFormDefinizione", TipoVersamento.model().PAG_FORM_DEFINIZIONE.getFieldType(),
					GenericJDBCParameterUtilities.readParameter(rs, "pag_form_definizione", TipoVersamento.model().PAG_FORM_DEFINIZIONE.getFieldType()));
				setParameter(object, "setPagFormImpaginazione", TipoVersamento.model().PAG_FORM_IMPAGINAZIONE.getFieldType(),
					GenericJDBCParameterUtilities.readParameter(rs, "pag_form_impaginazione", TipoVersamento.model().PAG_FORM_IMPAGINAZIONE.getFieldType()));
				setParameter(object, "setPagValidazioneDef", TipoVersamento.model().PAG_VALIDAZIONE_DEF.getFieldType(),
					GenericJDBCParameterUtilities.readParameter(rs, "pag_validazione_def", TipoVersamento.model().PAG_VALIDAZIONE_DEF.getFieldType()));
				setParameter(object, "setPagTrasformazioneTipo", TipoVersamento.model().PAG_TRASFORMAZIONE_TIPO.getFieldType(),
					GenericJDBCParameterUtilities.readParameter(rs, "pag_trasformazione_tipo", TipoVersamento.model().PAG_TRASFORMAZIONE_TIPO.getFieldType()));
				setParameter(object, "setPagTrasformazioneDef", TipoVersamento.model().PAG_TRASFORMAZIONE_DEF.getFieldType(),
					GenericJDBCParameterUtilities.readParameter(rs, "pag_trasformazione_def", TipoVersamento.model().PAG_TRASFORMAZIONE_DEF.getFieldType()));
				setParameter(object, "setPagCodApplicazione", TipoVersamento.model().PAG_COD_APPLICAZIONE.getFieldType(),
					GenericJDBCParameterUtilities.readParameter(rs, "pag_cod_applicazione", TipoVersamento.model().PAG_COD_APPLICAZIONE.getFieldType()));
				setParameter(object, "setPagAbilitato", TipoVersamento.model().PAG_ABILITATO.getFieldType(),
					GenericJDBCParameterUtilities.readParameter(rs, "pag_abilitato", TipoVersamento.model().PAG_ABILITATO.getFieldType()));
				setParameter(object, "setAvvMailPromAvvAbilitato", TipoVersamento.model().AVV_MAIL_PROM_AVV_ABILITATO.getFieldType(),
					GenericJDBCParameterUtilities.readParameter(rs, "avv_mail_prom_avv_abilitato", TipoVersamento.model().AVV_MAIL_PROM_AVV_ABILITATO.getFieldType()));
				setParameter(object, "setAvvMailPromAvvPdf", TipoVersamento.model().AVV_MAIL_PROM_AVV_PDF.getFieldType(),
					GenericJDBCParameterUtilities.readParameter(rs, "avv_mail_prom_avv_pdf", TipoVersamento.model().AVV_MAIL_PROM_AVV_PDF.getFieldType()));
				setParameter(object, "setAvvMailPromAvvTipo", TipoVersamento.model().AVV_MAIL_PROM_AVV_TIPO.getFieldType(),
					GenericJDBCParameterUtilities.readParameter(rs, "avv_mail_prom_avv_tipo", TipoVersamento.model().AVV_MAIL_PROM_AVV_TIPO.getFieldType()));
				setParameter(object, "setAvvMailPromAvvOggetto", TipoVersamento.model().AVV_MAIL_PROM_AVV_OGGETTO.getFieldType(),
					GenericJDBCParameterUtilities.readParameter(rs, "avv_mail_prom_avv_oggetto", TipoVersamento.model().AVV_MAIL_PROM_AVV_OGGETTO.getFieldType()));
				setParameter(object, "setAvvMailPromAvvMessaggio", TipoVersamento.model().AVV_MAIL_PROM_AVV_MESSAGGIO.getFieldType(),
					GenericJDBCParameterUtilities.readParameter(rs, "avv_mail_prom_avv_messaggio", TipoVersamento.model().AVV_MAIL_PROM_AVV_MESSAGGIO.getFieldType()));
				setParameter(object, "setAvvMailPromRicAbilitato", TipoVersamento.model().AVV_MAIL_PROM_RIC_ABILITATO.getFieldType(),
					GenericJDBCParameterUtilities.readParameter(rs, "avv_mail_prom_ric_abilitato", TipoVersamento.model().AVV_MAIL_PROM_RIC_ABILITATO.getFieldType()));
				setParameter(object, "setAvvMailPromRicPdf", TipoVersamento.model().AVV_MAIL_PROM_RIC_PDF.getFieldType(),
					GenericJDBCParameterUtilities.readParameter(rs, "avv_mail_prom_ric_pdf", TipoVersamento.model().AVV_MAIL_PROM_RIC_PDF.getFieldType()));
				setParameter(object, "setAvvMailPromRicTipo", TipoVersamento.model().AVV_MAIL_PROM_RIC_TIPO.getFieldType(),
					GenericJDBCParameterUtilities.readParameter(rs, "avv_mail_prom_ric_tipo", TipoVersamento.model().AVV_MAIL_PROM_RIC_TIPO.getFieldType()));
				setParameter(object, "setAvvMailPromRicOggetto", TipoVersamento.model().AVV_MAIL_PROM_RIC_OGGETTO.getFieldType(),
					GenericJDBCParameterUtilities.readParameter(rs, "avv_mail_prom_ric_oggetto", TipoVersamento.model().AVV_MAIL_PROM_RIC_OGGETTO.getFieldType()));
				setParameter(object, "setAvvMailPromRicMessaggio", TipoVersamento.model().AVV_MAIL_PROM_RIC_MESSAGGIO.getFieldType(),
					GenericJDBCParameterUtilities.readParameter(rs, "avv_mail_prom_ric_messaggio", TipoVersamento.model().AVV_MAIL_PROM_RIC_MESSAGGIO.getFieldType()));
				setParameter(object, "setAvvMailPromRicEseguiti", TipoVersamento.model().AVV_MAIL_PROM_RIC_ESEGUITI.getFieldType(),
					GenericJDBCParameterUtilities.readParameter(rs, "avv_mail_prom_ric_eseguiti", TipoVersamento.model().AVV_MAIL_PROM_RIC_ESEGUITI.getFieldType()));
				setParameter(object, "setAvvMailPromScadAbilitato", TipoVersamento.model().AVV_MAIL_PROM_SCAD_ABILITATO.getFieldType(),
					GenericJDBCParameterUtilities.readParameter(rs, "avv_mail_prom_scad_abilitato", TipoVersamento.model().AVV_MAIL_PROM_SCAD_ABILITATO.getFieldType()));
				setParameter(object, "setAvvMailPromScadPreavviso", TipoVersamento.model().AVV_MAIL_PROM_SCAD_PREAVVISO.getFieldType(),
					GenericJDBCParameterUtilities.readParameter(rs, "avv_mail_prom_scad_preavviso", TipoVersamento.model().AVV_MAIL_PROM_SCAD_PREAVVISO.getFieldType()));
				setParameter(object, "setAvvMailPromScadTipo", TipoVersamento.model().AVV_MAIL_PROM_SCAD_TIPO.getFieldType(),
					GenericJDBCParameterUtilities.readParameter(rs, "avv_mail_prom_scad_tipo", TipoVersamento.model().AVV_MAIL_PROM_SCAD_TIPO.getFieldType()));
				setParameter(object, "setAvvMailPromScadOggetto", TipoVersamento.model().AVV_MAIL_PROM_SCAD_OGGETTO.getFieldType(),
					GenericJDBCParameterUtilities.readParameter(rs, "avv_mail_prom_scad_oggetto", TipoVersamento.model().AVV_MAIL_PROM_SCAD_OGGETTO.getFieldType()));
				setParameter(object, "setAvvMailPromScadMessaggio", TipoVersamento.model().AVV_MAIL_PROM_SCAD_MESSAGGIO.getFieldType(),
					GenericJDBCParameterUtilities.readParameter(rs, "avv_mail_prom_scad_messaggio", TipoVersamento.model().AVV_MAIL_PROM_SCAD_MESSAGGIO.getFieldType()));
				setParameter(object, "setVisualizzazioneDefinizione", TipoVersamento.model().VISUALIZZAZIONE_DEFINIZIONE.getFieldType(),
					GenericJDBCParameterUtilities.readParameter(rs, "visualizzazione_definizione", TipoVersamento.model().VISUALIZZAZIONE_DEFINIZIONE.getFieldType()));
				setParameter(object, "setTracCsvTipo", TipoVersamento.model().TRAC_CSV_TIPO.getFieldType(),
					GenericJDBCParameterUtilities.readParameter(rs, "trac_csv_tipo", TipoVersamento.model().TRAC_CSV_TIPO.getFieldType()));
				setParameter(object, "setTracCsvHeaderRisposta", TipoVersamento.model().TRAC_CSV_HEADER_RISPOSTA.getFieldType(),
					GenericJDBCParameterUtilities.readParameter(rs, "trac_csv_header_risposta", TipoVersamento.model().TRAC_CSV_HEADER_RISPOSTA.getFieldType()));
				setParameter(object, "setTracCsvTemplateRichiesta", TipoVersamento.model().TRAC_CSV_TEMPLATE_RICHIESTA.getFieldType(),
					GenericJDBCParameterUtilities.readParameter(rs, "trac_csv_template_richiesta", TipoVersamento.model().TRAC_CSV_TEMPLATE_RICHIESTA.getFieldType()));
				setParameter(object, "setTracCsvTemplateRisposta", TipoVersamento.model().TRAC_CSV_TEMPLATE_RISPOSTA.getFieldType(),
					GenericJDBCParameterUtilities.readParameter(rs, "trac_csv_template_risposta", TipoVersamento.model().TRAC_CSV_TEMPLATE_RISPOSTA.getFieldType()));
				setParameter(object, "setAvvAppIoPromAvvAbilitato", TipoVersamento.model().AVV_APP_IO_PROM_AVV_ABILITATO.getFieldType(),
					GenericJDBCParameterUtilities.readParameter(rs, "avv_app_io_prom_avv_abilitato", TipoVersamento.model().AVV_APP_IO_PROM_AVV_ABILITATO.getFieldType()));
				setParameter(object, "setAvvAppIoPromAvvTipo", TipoVersamento.model().AVV_APP_IO_PROM_AVV_TIPO.getFieldType(),
					GenericJDBCParameterUtilities.readParameter(rs, "avv_app_io_prom_avv_tipo", TipoVersamento.model().AVV_APP_IO_PROM_AVV_TIPO.getFieldType()));
				setParameter(object, "setAvvAppIoPromAvvOggetto", TipoVersamento.model().AVV_APP_IO_PROM_AVV_OGGETTO.getFieldType(),
					GenericJDBCParameterUtilities.readParameter(rs, "avv_app_io_prom_avv_oggetto", TipoVersamento.model().AVV_APP_IO_PROM_AVV_OGGETTO.getFieldType()));
				setParameter(object, "setAvvAppIoPromAvvMessaggio", TipoVersamento.model().AVV_APP_IO_PROM_AVV_MESSAGGIO.getFieldType(),
					GenericJDBCParameterUtilities.readParameter(rs, "avv_app_io_prom_avv_messaggio", TipoVersamento.model().AVV_APP_IO_PROM_AVV_MESSAGGIO.getFieldType()));
				setParameter(object, "setAvvAppIoPromRicAbilitato", TipoVersamento.model().AVV_APP_IO_PROM_RIC_ABILITATO.getFieldType(),
					GenericJDBCParameterUtilities.readParameter(rs, "avv_app_io_prom_ric_abilitato", TipoVersamento.model().AVV_APP_IO_PROM_RIC_ABILITATO.getFieldType()));
				setParameter(object, "setAvvAppIoPromRicTipo", TipoVersamento.model().AVV_APP_IO_PROM_RIC_TIPO.getFieldType(),
					GenericJDBCParameterUtilities.readParameter(rs, "avv_app_io_prom_ric_tipo", TipoVersamento.model().AVV_APP_IO_PROM_RIC_TIPO.getFieldType()));
				setParameter(object, "setAvvAppIoPromRicOggetto", TipoVersamento.model().AVV_APP_IO_PROM_RIC_OGGETTO.getFieldType(),
					GenericJDBCParameterUtilities.readParameter(rs, "avv_app_io_prom_ric_oggetto", TipoVersamento.model().AVV_APP_IO_PROM_RIC_OGGETTO.getFieldType()));
				setParameter(object, "setAvvAppIoPromRicMessaggio", TipoVersamento.model().AVV_APP_IO_PROM_RIC_MESSAGGIO.getFieldType(),
					GenericJDBCParameterUtilities.readParameter(rs, "avv_app_io_prom_ric_messaggio", TipoVersamento.model().AVV_APP_IO_PROM_RIC_MESSAGGIO.getFieldType()));
				setParameter(object, "setAvvAppIoPromRicEseguiti", TipoVersamento.model().AVV_APP_IO_PROM_RIC_ESEGUITI.getFieldType(),
					GenericJDBCParameterUtilities.readParameter(rs, "avv_app_io_prom_ric_eseguiti", TipoVersamento.model().AVV_APP_IO_PROM_RIC_ESEGUITI.getFieldType()));
				setParameter(object, "setAvvAppIoPromScadAbilitato", TipoVersamento.model().AVV_APP_IO_PROM_SCAD_ABILITATO.getFieldType(),
					GenericJDBCParameterUtilities.readParameter(rs, "avv_app_io_prom_scad_abilitato", TipoVersamento.model().AVV_APP_IO_PROM_SCAD_ABILITATO.getFieldType()));
				setParameter(object, "setAvvAppIoPromScadPreavviso", TipoVersamento.model().AVV_APP_IO_PROM_SCAD_PREAVVISO.getFieldType(),
					GenericJDBCParameterUtilities.readParameter(rs, "avv_app_io_prom_scad_preavviso", TipoVersamento.model().AVV_APP_IO_PROM_SCAD_PREAVVISO.getFieldType()));
				setParameter(object, "setAvvAppIoPromScadTipo", TipoVersamento.model().AVV_APP_IO_PROM_SCAD_TIPO.getFieldType(),
					GenericJDBCParameterUtilities.readParameter(rs, "avv_app_io_prom_scad_tipo", TipoVersamento.model().AVV_APP_IO_PROM_SCAD_TIPO.getFieldType()));
				setParameter(object, "setAvvAppIoPromScadOggetto", TipoVersamento.model().AVV_APP_IO_PROM_SCAD_OGGETTO.getFieldType(),
					GenericJDBCParameterUtilities.readParameter(rs, "avv_app_io_prom_scad_oggetto", TipoVersamento.model().AVV_APP_IO_PROM_SCAD_OGGETTO.getFieldType()));
				setParameter(object, "setAvvAppIoPromScadMessaggio", TipoVersamento.model().AVV_APP_IO_PROM_SCAD_MESSAGGIO.getFieldType(),
					GenericJDBCParameterUtilities.readParameter(rs, "avv_app_io_prom_scad_messaggio", TipoVersamento.model().AVV_APP_IO_PROM_SCAD_MESSAGGIO.getFieldType()));
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

			if(model.equals(TipoVersamento.model())){
				TipoVersamento object = new TipoVersamento();
				setParameter(object, "setId", Long.class,
					this.getObjectFromMap(map,"id"));
				setParameter(object, "setCodTipoVersamento", TipoVersamento.model().COD_TIPO_VERSAMENTO.getFieldType(),
					this.getObjectFromMap(map,"codTipoVersamento"));
				setParameter(object, "setDescrizione", TipoVersamento.model().DESCRIZIONE.getFieldType(),
					this.getObjectFromMap(map,"descrizione"));
				setParameter(object, "setCodificaIuv", TipoVersamento.model().CODIFICA_IUV.getFieldType(),
					this.getObjectFromMap(map,"codificaIuv"));
				setParameter(object, "setPagaTerzi", TipoVersamento.model().PAGA_TERZI.getFieldType(),
					this.getObjectFromMap(map,"pagaTerzi"));
				setParameter(object, "setAbilitato", TipoVersamento.model().ABILITATO.getFieldType(),
					this.getObjectFromMap(map,"abilitato"));
				setParameter(object, "setBoFormTipo", TipoVersamento.model().BO_FORM_TIPO.getFieldType(),
					this.getObjectFromMap(map,"boFormTipo"));
				setParameter(object, "setBoFormDefinizione", TipoVersamento.model().BO_FORM_DEFINIZIONE.getFieldType(),
					this.getObjectFromMap(map,"boFormDefinizione"));
				setParameter(object, "setBoValidazioneDef", TipoVersamento.model().BO_VALIDAZIONE_DEF.getFieldType(),
					this.getObjectFromMap(map,"boValidazioneDef"));
				setParameter(object, "setBoTrasformazioneTipo", TipoVersamento.model().BO_TRASFORMAZIONE_TIPO.getFieldType(),
					this.getObjectFromMap(map,"boTrasformazioneTipo"));
				setParameter(object, "setBoTrasformazioneDef", TipoVersamento.model().BO_TRASFORMAZIONE_DEF.getFieldType(),
					this.getObjectFromMap(map,"boTrasformazioneDef"));
				setParameter(object, "setBoCodApplicazione", TipoVersamento.model().BO_COD_APPLICAZIONE.getFieldType(),
					this.getObjectFromMap(map,"boCodApplicazione"));
				setParameter(object, "setBoAbilitato", TipoVersamento.model().BO_ABILITATO.getFieldType(),
					this.getObjectFromMap(map,"boAbilitato"));
				setParameter(object, "setPagFormTipo", TipoVersamento.model().PAG_FORM_TIPO.getFieldType(),
					this.getObjectFromMap(map,"pagFormTipo"));
				setParameter(object, "setPagFormDefinizione", TipoVersamento.model().PAG_FORM_DEFINIZIONE.getFieldType(),
					this.getObjectFromMap(map,"pagFormDefinizione"));
				setParameter(object, "setPagFormImpaginazione", TipoVersamento.model().PAG_FORM_IMPAGINAZIONE.getFieldType(),
					this.getObjectFromMap(map,"pagFormImpaginazione"));
				setParameter(object, "setPagValidazioneDef", TipoVersamento.model().PAG_VALIDAZIONE_DEF.getFieldType(),
					this.getObjectFromMap(map,"pagValidazioneDef"));
				setParameter(object, "setPagTrasformazioneTipo", TipoVersamento.model().PAG_TRASFORMAZIONE_TIPO.getFieldType(),
					this.getObjectFromMap(map,"pagTrasformazioneTipo"));
				setParameter(object, "setPagTrasformazioneDef", TipoVersamento.model().PAG_TRASFORMAZIONE_DEF.getFieldType(),
					this.getObjectFromMap(map,"pagTrasformazioneDef"));
				setParameter(object, "setPagCodApplicazione", TipoVersamento.model().PAG_COD_APPLICAZIONE.getFieldType(),
					this.getObjectFromMap(map,"pagCodApplicazione"));
				setParameter(object, "setPagAbilitato", TipoVersamento.model().PAG_ABILITATO.getFieldType(),
					this.getObjectFromMap(map,"pagAbilitato"));
				setParameter(object, "setAvvMailPromAvvAbilitato", TipoVersamento.model().AVV_MAIL_PROM_AVV_ABILITATO.getFieldType(),
					this.getObjectFromMap(map,"avvMailPromAvvAbilitato"));
				setParameter(object, "setAvvMailPromAvvPdf", TipoVersamento.model().AVV_MAIL_PROM_AVV_PDF.getFieldType(),
					this.getObjectFromMap(map,"avvMailPromAvvPdf"));
				setParameter(object, "setAvvMailPromAvvTipo", TipoVersamento.model().AVV_MAIL_PROM_AVV_TIPO.getFieldType(),
					this.getObjectFromMap(map,"avvMailPromAvvTipo"));
				setParameter(object, "setAvvMailPromAvvOggetto", TipoVersamento.model().AVV_MAIL_PROM_AVV_OGGETTO.getFieldType(),
					this.getObjectFromMap(map,"avvMailPromAvvOggetto"));
				setParameter(object, "setAvvMailPromAvvMessaggio", TipoVersamento.model().AVV_MAIL_PROM_AVV_MESSAGGIO.getFieldType(),
					this.getObjectFromMap(map,"avvMailPromAvvMessaggio"));
				setParameter(object, "setAvvMailPromRicAbilitato", TipoVersamento.model().AVV_MAIL_PROM_RIC_ABILITATO.getFieldType(),
					this.getObjectFromMap(map,"avvMailPromRicAbilitato"));
				setParameter(object, "setAvvMailPromRicPdf", TipoVersamento.model().AVV_MAIL_PROM_RIC_PDF.getFieldType(),
					this.getObjectFromMap(map,"avvMailPromRicPdf"));
				setParameter(object, "setAvvMailPromRicTipo", TipoVersamento.model().AVV_MAIL_PROM_RIC_TIPO.getFieldType(),
					this.getObjectFromMap(map,"avvMailPromRicTipo"));
				setParameter(object, "setAvvMailPromRicOggetto", TipoVersamento.model().AVV_MAIL_PROM_RIC_OGGETTO.getFieldType(),
					this.getObjectFromMap(map,"avvMailPromRicOggetto"));
				setParameter(object, "setAvvMailPromRicMessaggio", TipoVersamento.model().AVV_MAIL_PROM_RIC_MESSAGGIO.getFieldType(),
					this.getObjectFromMap(map,"avvMailPromRicMessaggio"));
				setParameter(object, "setAvvMailPromRicEseguiti", TipoVersamento.model().AVV_MAIL_PROM_RIC_ESEGUITI.getFieldType(),
					this.getObjectFromMap(map,"avvMailPromRicEseguiti"));
				setParameter(object, "setAvvMailPromScadAbilitato", TipoVersamento.model().AVV_MAIL_PROM_SCAD_ABILITATO.getFieldType(),
					this.getObjectFromMap(map,"avvMailPromScadAbilitato"));
				setParameter(object, "setAvvMailPromScadPreavviso", TipoVersamento.model().AVV_MAIL_PROM_SCAD_PREAVVISO.getFieldType(),
					this.getObjectFromMap(map,"avvMailPromScadPreavviso"));
				setParameter(object, "setAvvMailPromScadTipo", TipoVersamento.model().AVV_MAIL_PROM_SCAD_TIPO.getFieldType(),
					this.getObjectFromMap(map,"avvMailPromScadTipo"));
				setParameter(object, "setAvvMailPromScadOggetto", TipoVersamento.model().AVV_MAIL_PROM_SCAD_OGGETTO.getFieldType(),
					this.getObjectFromMap(map,"avvMailPromScadOggetto"));
				setParameter(object, "setAvvMailPromScadMessaggio", TipoVersamento.model().AVV_MAIL_PROM_SCAD_MESSAGGIO.getFieldType(),
					this.getObjectFromMap(map,"avvMailPromScadMessaggio"));
				setParameter(object, "setVisualizzazioneDefinizione", TipoVersamento.model().VISUALIZZAZIONE_DEFINIZIONE.getFieldType(),
					this.getObjectFromMap(map,"visualizzazioneDefinizione"));
				setParameter(object, "setTracCsvTipo", TipoVersamento.model().TRAC_CSV_TIPO.getFieldType(),
					this.getObjectFromMap(map,"tracCsvTipo"));
				setParameter(object, "setTracCsvHeaderRisposta", TipoVersamento.model().TRAC_CSV_HEADER_RISPOSTA.getFieldType(),
					this.getObjectFromMap(map,"tracCsvHeaderRisposta"));
				setParameter(object, "setTracCsvTemplateRichiesta", TipoVersamento.model().TRAC_CSV_TEMPLATE_RICHIESTA.getFieldType(),
					this.getObjectFromMap(map,"tracCsvTemplateRichiesta"));
				setParameter(object, "setTracCsvTemplateRisposta", TipoVersamento.model().TRAC_CSV_TEMPLATE_RISPOSTA.getFieldType(),
					this.getObjectFromMap(map,"tracCsvTemplateRisposta"));
				setParameter(object, "setAvvAppIoPromAvvAbilitato", TipoVersamento.model().AVV_APP_IO_PROM_AVV_ABILITATO.getFieldType(),
					this.getObjectFromMap(map,"avvAppIoPromAvvAbilitato"));
				setParameter(object, "setAvvAppIoPromAvvTipo", TipoVersamento.model().AVV_APP_IO_PROM_AVV_TIPO.getFieldType(),
					this.getObjectFromMap(map,"avvAppIoPromAvvTipo"));
				setParameter(object, "setAvvAppIoPromAvvOggetto", TipoVersamento.model().AVV_APP_IO_PROM_AVV_OGGETTO.getFieldType(),
					this.getObjectFromMap(map,"avvAppIoPromAvvOggetto"));
				setParameter(object, "setAvvAppIoPromAvvMessaggio", TipoVersamento.model().AVV_APP_IO_PROM_AVV_MESSAGGIO.getFieldType(),
					this.getObjectFromMap(map,"avvAppIoPromAvvMessaggio"));
				setParameter(object, "setAvvAppIoPromRicAbilitato", TipoVersamento.model().AVV_APP_IO_PROM_RIC_ABILITATO.getFieldType(),
					this.getObjectFromMap(map,"avvAppIoPromRicAbilitato"));
				setParameter(object, "setAvvAppIoPromRicTipo", TipoVersamento.model().AVV_APP_IO_PROM_RIC_TIPO.getFieldType(),
					this.getObjectFromMap(map,"avvAppIoPromRicTipo"));
				setParameter(object, "setAvvAppIoPromRicOggetto", TipoVersamento.model().AVV_APP_IO_PROM_RIC_OGGETTO.getFieldType(),
					this.getObjectFromMap(map,"avvAppIoPromRicOggetto"));
				setParameter(object, "setAvvAppIoPromRicMessaggio", TipoVersamento.model().AVV_APP_IO_PROM_RIC_MESSAGGIO.getFieldType(),
					this.getObjectFromMap(map,"avvAppIoPromRicMessaggio"));
				setParameter(object, "setAvvAppIoPromRicEseguiti", TipoVersamento.model().AVV_APP_IO_PROM_RIC_ESEGUITI.getFieldType(),
					this.getObjectFromMap(map,"avvAppIoPromRicEseguiti"));
				setParameter(object, "setAvvAppIoPromScadAbilitato", TipoVersamento.model().AVV_APP_IO_PROM_SCAD_ABILITATO.getFieldType(),
					this.getObjectFromMap(map,"avvAppIoPromScadAbilitato"));
				setParameter(object, "setAvvAppIoPromScadPreavviso", TipoVersamento.model().AVV_APP_IO_PROM_SCAD_PREAVVISO.getFieldType(),
					this.getObjectFromMap(map,"avvAppIoPromScadPreavviso"));
				setParameter(object, "setAvvAppIoPromScadTipo", TipoVersamento.model().AVV_APP_IO_PROM_SCAD_TIPO.getFieldType(),
					this.getObjectFromMap(map,"avvAppIoPromScadTipo"));
				setParameter(object, "setAvvAppIoPromScadOggetto", TipoVersamento.model().AVV_APP_IO_PROM_SCAD_OGGETTO.getFieldType(),
					this.getObjectFromMap(map,"avvAppIoPromScadOggetto"));
				setParameter(object, "setAvvAppIoPromScadMessaggio", TipoVersamento.model().AVV_APP_IO_PROM_SCAD_MESSAGGIO.getFieldType(),
					this.getObjectFromMap(map,"avvAppIoPromScadMessaggio"));
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

			if(model.equals(TipoVersamento.model())){
				return new org.openspcoop2.utils.jdbc.CustomKeyGeneratorObject("tipi_versamento","id","seq_tipi_versamento","tipi_versamento_init_seq");
			}
			
			else{
				throw new ServiceException("Model ["+model.toString()+"] not supported by getKeyGeneratorObject: "+this.getClass().getName());
			}

		}catch(Exception e){
			throw new ServiceException("Model ["+model.toString()+"] occurs error in getKeyGeneratorObject: "+e.getMessage(),e);
		}
		
	}

}
