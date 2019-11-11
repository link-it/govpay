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
				setParameter(object, "setTipo", TipoVersamentoDominio.model().TIPO.getFieldType(),
					jdbcParameterUtilities.readParameter(rs, "tipo", TipoVersamentoDominio.model().TIPO.getFieldType()));
				setParameter(object, "setPagaTerzi", TipoVersamentoDominio.model().PAGA_TERZI.getFieldType(),
					jdbcParameterUtilities.readParameter(rs, "paga_terzi", TipoVersamentoDominio.model().PAGA_TERZI.getFieldType()));
				setParameter(object, "setAbilitato", TipoVersamentoDominio.model().ABILITATO.getFieldType(),
					jdbcParameterUtilities.readParameter(rs, "abilitato", TipoVersamentoDominio.model().ABILITATO.getFieldType()));
				setParameter(object, "setFormTipo", TipoVersamentoDominio.model().FORM_TIPO.getFieldType(),
					jdbcParameterUtilities.readParameter(rs, "form_tipo", TipoVersamentoDominio.model().FORM_TIPO.getFieldType()));
				setParameter(object, "setFormDefinizione", TipoVersamentoDominio.model().FORM_DEFINIZIONE.getFieldType(),
					jdbcParameterUtilities.readParameter(rs, "form_definizione", TipoVersamentoDominio.model().FORM_DEFINIZIONE.getFieldType()));
				setParameter(object, "setValidazioneDefinizione", TipoVersamentoDominio.model().VALIDAZIONE_DEFINIZIONE.getFieldType(),
					jdbcParameterUtilities.readParameter(rs, "validazione_definizione", TipoVersamentoDominio.model().VALIDAZIONE_DEFINIZIONE.getFieldType()));
				setParameter(object, "setTrasformazioneTipo", TipoVersamentoDominio.model().TRASFORMAZIONE_TIPO.getFieldType(),
					jdbcParameterUtilities.readParameter(rs, "trasformazione_tipo", TipoVersamentoDominio.model().TRASFORMAZIONE_TIPO.getFieldType()));
				setParameter(object, "setTrasformazioneDefinizione", TipoVersamentoDominio.model().TRASFORMAZIONE_DEFINIZIONE.getFieldType(),
					jdbcParameterUtilities.readParameter(rs, "trasformazione_definizione", TipoVersamentoDominio.model().TRASFORMAZIONE_DEFINIZIONE.getFieldType()));
				setParameter(object, "setCodApplicazione", TipoVersamentoDominio.model().COD_APPLICAZIONE.getFieldType(),
					jdbcParameterUtilities.readParameter(rs, "cod_applicazione", TipoVersamentoDominio.model().COD_APPLICAZIONE.getFieldType()));
				setParameter(object, "setPromemoriaAvvisoAbilitato", TipoVersamentoDominio.model().PROMEMORIA_AVVISO_ABILITATO.getFieldType(),
					jdbcParameterUtilities.readParameter(rs, "promemoria_avviso_abilitato", TipoVersamentoDominio.model().PROMEMORIA_AVVISO_ABILITATO.getFieldType()));
				setParameter(object, "setPromemoriaAvvisoTipo", TipoVersamentoDominio.model().PROMEMORIA_AVVISO_TIPO.getFieldType(),
					jdbcParameterUtilities.readParameter(rs, "promemoria_avviso_tipo", TipoVersamentoDominio.model().PROMEMORIA_AVVISO_TIPO.getFieldType()));
				setParameter(object, "setPromemoriaAvvisoPdf", TipoVersamentoDominio.model().PROMEMORIA_AVVISO_PDF.getFieldType(),
					jdbcParameterUtilities.readParameter(rs, "promemoria_avviso_pdf", TipoVersamentoDominio.model().PROMEMORIA_AVVISO_PDF.getFieldType()));
				setParameter(object, "setPromemoriaAvvisoOggetto", TipoVersamentoDominio.model().PROMEMORIA_AVVISO_OGGETTO.getFieldType(),
					jdbcParameterUtilities.readParameter(rs, "promemoria_avviso_oggetto", TipoVersamentoDominio.model().PROMEMORIA_AVVISO_OGGETTO.getFieldType()));
				setParameter(object, "setPromemoriaAvvisoMessaggio", TipoVersamentoDominio.model().PROMEMORIA_AVVISO_MESSAGGIO.getFieldType(),
					jdbcParameterUtilities.readParameter(rs, "promemoria_avviso_messaggio", TipoVersamentoDominio.model().PROMEMORIA_AVVISO_MESSAGGIO.getFieldType()));
				setParameter(object, "setPromemoriaRicevutaAbilitato", TipoVersamentoDominio.model().PROMEMORIA_RICEVUTA_ABILITATO.getFieldType(),
					jdbcParameterUtilities.readParameter(rs, "promemoria_ricevuta_abilitato", TipoVersamentoDominio.model().PROMEMORIA_RICEVUTA_ABILITATO.getFieldType()));
				setParameter(object, "setPromemoriaRicevutaTipo", TipoVersamentoDominio.model().PROMEMORIA_RICEVUTA_TIPO.getFieldType(),
					jdbcParameterUtilities.readParameter(rs, "promemoria_ricevuta_tipo", TipoVersamentoDominio.model().PROMEMORIA_RICEVUTA_TIPO.getFieldType()));
				setParameter(object, "setPromemoriaRicevutaPdf", TipoVersamentoDominio.model().PROMEMORIA_RICEVUTA_PDF.getFieldType(),
					jdbcParameterUtilities.readParameter(rs, "promemoria_ricevuta_pdf", TipoVersamentoDominio.model().PROMEMORIA_RICEVUTA_PDF.getFieldType()));
				setParameter(object, "setPromemoriaRicevutaOggetto", TipoVersamentoDominio.model().PROMEMORIA_RICEVUTA_OGGETTO.getFieldType(),
					jdbcParameterUtilities.readParameter(rs, "promemoria_ricevuta_oggetto", TipoVersamentoDominio.model().PROMEMORIA_RICEVUTA_OGGETTO.getFieldType()));
				setParameter(object, "setPromemoriaRicevutaMessaggio", TipoVersamentoDominio.model().PROMEMORIA_RICEVUTA_MESSAGGIO.getFieldType(),
					jdbcParameterUtilities.readParameter(rs, "promemoria_ricevuta_messaggio", TipoVersamentoDominio.model().PROMEMORIA_RICEVUTA_MESSAGGIO.getFieldType()));
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
				setParameter(object, "setTipo", TipoVersamentoDominio.model().TIPO_VERSAMENTO.TIPO.getFieldType(),
					jdbcParameterUtilities.readParameter(rs, "tipo", TipoVersamentoDominio.model().TIPO_VERSAMENTO.TIPO.getFieldType()));
				setParameter(object, "setPagaTerzi", TipoVersamentoDominio.model().TIPO_VERSAMENTO.PAGA_TERZI.getFieldType(),
					jdbcParameterUtilities.readParameter(rs, "paga_terzi", TipoVersamentoDominio.model().TIPO_VERSAMENTO.PAGA_TERZI.getFieldType()));
				setParameter(object, "setAbilitato", TipoVersamentoDominio.model().TIPO_VERSAMENTO.ABILITATO.getFieldType(),
						jdbcParameterUtilities.readParameter(rs, "abilitato", TipoVersamentoDominio.model().TIPO_VERSAMENTO.ABILITATO.getFieldType()));
				setParameter(object, "setFormTipo", TipoVersamentoDominio.model().TIPO_VERSAMENTO.FORM_TIPO.getFieldType(),
					jdbcParameterUtilities.readParameter(rs, "form_tipo", TipoVersamentoDominio.model().TIPO_VERSAMENTO.FORM_TIPO.getFieldType()));
				setParameter(object, "setFormDefinizione", TipoVersamentoDominio.model().TIPO_VERSAMENTO.FORM_DEFINIZIONE.getFieldType(),
					jdbcParameterUtilities.readParameter(rs, "form_definizione", TipoVersamentoDominio.model().TIPO_VERSAMENTO.FORM_DEFINIZIONE.getFieldType()));
				setParameter(object, "setValidazioneDefinizione", TipoVersamentoDominio.model().TIPO_VERSAMENTO.VALIDAZIONE_DEFINIZIONE.getFieldType(),
					jdbcParameterUtilities.readParameter(rs, "validazione_definizione", TipoVersamentoDominio.model().TIPO_VERSAMENTO.VALIDAZIONE_DEFINIZIONE.getFieldType()));
				setParameter(object, "setTrasformazioneTipo", TipoVersamentoDominio.model().TIPO_VERSAMENTO.TRASFORMAZIONE_TIPO.getFieldType(),
					jdbcParameterUtilities.readParameter(rs, "trasformazione_tipo", TipoVersamentoDominio.model().TIPO_VERSAMENTO.TRASFORMAZIONE_TIPO.getFieldType()));
				setParameter(object, "setTrasformazioneDefinizione", TipoVersamentoDominio.model().TIPO_VERSAMENTO.TRASFORMAZIONE_DEFINIZIONE.getFieldType(),
					jdbcParameterUtilities.readParameter(rs, "trasformazione_definizione", TipoVersamentoDominio.model().TIPO_VERSAMENTO.TRASFORMAZIONE_DEFINIZIONE.getFieldType()));
				setParameter(object, "setCodApplicazione", TipoVersamentoDominio.model().TIPO_VERSAMENTO.COD_APPLICAZIONE.getFieldType(),
					jdbcParameterUtilities.readParameter(rs, "cod_applicazione", TipoVersamentoDominio.model().TIPO_VERSAMENTO.COD_APPLICAZIONE.getFieldType()));
				setParameter(object, "setPromemoriaAvvisoAbilitato", TipoVersamentoDominio.model().TIPO_VERSAMENTO.PROMEMORIA_AVVISO_ABILITATO.getFieldType(),
						jdbcParameterUtilities.readParameter(rs, "promemoria_avviso_abilitato", TipoVersamentoDominio.model().TIPO_VERSAMENTO.PROMEMORIA_AVVISO_ABILITATO.getFieldType()));
				setParameter(object, "setPromemoriaAvvisoTipo", TipoVersamentoDominio.model().TIPO_VERSAMENTO.PROMEMORIA_AVVISO_TIPO.getFieldType(),
					jdbcParameterUtilities.readParameter(rs, "promemoria_avviso_tipo", TipoVersamentoDominio.model().TIPO_VERSAMENTO.PROMEMORIA_AVVISO_TIPO.getFieldType()));				
				setParameter(object, "setPromemoriaAvvisoPdf", TipoVersamentoDominio.model().TIPO_VERSAMENTO.PROMEMORIA_AVVISO_PDF.getFieldType(),
					jdbcParameterUtilities.readParameter(rs, "promemoria_avviso_pdf", TipoVersamentoDominio.model().TIPO_VERSAMENTO.PROMEMORIA_AVVISO_PDF.getFieldType()));
				setParameter(object, "setPromemoriaAvvisoOggetto", TipoVersamentoDominio.model().TIPO_VERSAMENTO.PROMEMORIA_AVVISO_OGGETTO.getFieldType(),
					jdbcParameterUtilities.readParameter(rs, "promemoria_avviso_oggetto", TipoVersamentoDominio.model().TIPO_VERSAMENTO.PROMEMORIA_AVVISO_OGGETTO.getFieldType()));
				setParameter(object, "setPromemoriaAvvisoMessaggio", TipoVersamentoDominio.model().TIPO_VERSAMENTO.PROMEMORIA_AVVISO_MESSAGGIO.getFieldType(),
					jdbcParameterUtilities.readParameter(rs, "promemoria_avviso_messaggio", TipoVersamentoDominio.model().TIPO_VERSAMENTO.PROMEMORIA_AVVISO_MESSAGGIO.getFieldType()));
				setParameter(object, "setPromemoriaRicevutaAbilitato", TipoVersamentoDominio.model().TIPO_VERSAMENTO.PROMEMORIA_RICEVUTA_ABILITATO.getFieldType(),
						jdbcParameterUtilities.readParameter(rs, "promemoria_ricevuta_abilitato", TipoVersamentoDominio.model().TIPO_VERSAMENTO.PROMEMORIA_RICEVUTA_ABILITATO.getFieldType()));
				setParameter(object, "setPromemoriaRicevutaTipo", TipoVersamentoDominio.model().TIPO_VERSAMENTO.PROMEMORIA_RICEVUTA_TIPO.getFieldType(),
					jdbcParameterUtilities.readParameter(rs, "promemoria_ricevuta_tipo", TipoVersamentoDominio.model().TIPO_VERSAMENTO.PROMEMORIA_RICEVUTA_TIPO.getFieldType()));
				setParameter(object, "setPromemoriaRicevutaPdf", TipoVersamentoDominio.model().TIPO_VERSAMENTO.PROMEMORIA_RICEVUTA_PDF.getFieldType(),
					jdbcParameterUtilities.readParameter(rs, "promemoria_ricevuta_pdf", TipoVersamentoDominio.model().TIPO_VERSAMENTO.PROMEMORIA_RICEVUTA_PDF.getFieldType()));
				setParameter(object, "setPromemoriaRicevutaOggetto", TipoVersamentoDominio.model().TIPO_VERSAMENTO.PROMEMORIA_RICEVUTA_OGGETTO.getFieldType(),
					jdbcParameterUtilities.readParameter(rs, "promemoria_ricevuta_oggetto", TipoVersamentoDominio.model().TIPO_VERSAMENTO.PROMEMORIA_RICEVUTA_OGGETTO.getFieldType()));
				setParameter(object, "setPromemoriaRicevutaMessaggio", TipoVersamentoDominio.model().TIPO_VERSAMENTO.PROMEMORIA_RICEVUTA_MESSAGGIO.getFieldType(),
					jdbcParameterUtilities.readParameter(rs, "promemoria_ricevuta_messaggio", TipoVersamentoDominio.model().TIPO_VERSAMENTO.PROMEMORIA_RICEVUTA_MESSAGGIO.getFieldType()));
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
				setParameter(object, "setTipo", TipoVersamentoDominio.model().TIPO.getFieldType(),
					this.getObjectFromMap(map,"tipo"));
				setParameter(object, "setPagaTerzi", TipoVersamentoDominio.model().PAGA_TERZI.getFieldType(),
					this.getObjectFromMap(map,"pagaTerzi"));
				setParameter(object, "setAbilitato", TipoVersamentoDominio.model().ABILITATO.getFieldType(),
					this.getObjectFromMap(map,"abilitato"));
				setParameter(object, "setFormTipo", TipoVersamentoDominio.model().FORM_TIPO.getFieldType(),
					this.getObjectFromMap(map,"formTipo"));
				setParameter(object, "setFormDefinizione", TipoVersamentoDominio.model().FORM_DEFINIZIONE.getFieldType(),
					this.getObjectFromMap(map,"formDefinizione"));
				setParameter(object, "setValidazioneDefinizione", TipoVersamentoDominio.model().VALIDAZIONE_DEFINIZIONE.getFieldType(),
					this.getObjectFromMap(map,"validazioneDefinizione"));
				setParameter(object, "setTrasformazioneTipo", TipoVersamentoDominio.model().TRASFORMAZIONE_TIPO.getFieldType(),
					this.getObjectFromMap(map,"trasformazioneTipo"));
				setParameter(object, "setTrasformazioneDefinizione", TipoVersamentoDominio.model().TRASFORMAZIONE_DEFINIZIONE.getFieldType(),
					this.getObjectFromMap(map,"trasformazioneDefinizione"));
				setParameter(object, "setCodApplicazione", TipoVersamentoDominio.model().COD_APPLICAZIONE.getFieldType(),
					this.getObjectFromMap(map,"codApplicazione"));
				setParameter(object, "setPromemoriaAvvisoAbilitato", TipoVersamentoDominio.model().PROMEMORIA_AVVISO_ABILITATO.getFieldType(),
					this.getObjectFromMap(map,"promemoriaAvvisoAbilitato"));
				setParameter(object, "setPromemoriaAvvisoTipo", TipoVersamentoDominio.model().PROMEMORIA_AVVISO_TIPO.getFieldType(),
					this.getObjectFromMap(map,"promemoriaAvvisoTipo"));
				setParameter(object, "setPromemoriaAvvisoPdf", TipoVersamentoDominio.model().PROMEMORIA_AVVISO_PDF.getFieldType(),
					this.getObjectFromMap(map,"promemoriaAvvisoPdf"));
				setParameter(object, "setPromemoriaAvvisoOggetto", TipoVersamentoDominio.model().PROMEMORIA_AVVISO_OGGETTO.getFieldType(),
					this.getObjectFromMap(map,"promemoriaAvvisoOggetto"));
				setParameter(object, "setPromemoriaAvvisoMessaggio", TipoVersamentoDominio.model().PROMEMORIA_AVVISO_MESSAGGIO.getFieldType(),
					this.getObjectFromMap(map,"promemoriaAvvisoMessaggio"));
				setParameter(object, "setPromemoriaRicevutaAbilitato", TipoVersamentoDominio.model().PROMEMORIA_RICEVUTA_ABILITATO.getFieldType(),
					this.getObjectFromMap(map,"promemoriaRicevutaAbilitato"));
				setParameter(object, "setPromemoriaRicevutaTipo", TipoVersamentoDominio.model().PROMEMORIA_RICEVUTA_TIPO.getFieldType(),
					this.getObjectFromMap(map,"promemoriaRicevutaTipo"));
				setParameter(object, "setPromemoriaRicevutaPdf", TipoVersamentoDominio.model().PROMEMORIA_RICEVUTA_PDF.getFieldType(),
					this.getObjectFromMap(map,"promemoriaRicevutaPdf"));
				setParameter(object, "setPromemoriaRicevutaOggetto", TipoVersamentoDominio.model().PROMEMORIA_RICEVUTA_OGGETTO.getFieldType(),
					this.getObjectFromMap(map,"promemoriaRicevutaOggetto"));
				setParameter(object, "setPromemoriaRicevutaMessaggio", TipoVersamentoDominio.model().PROMEMORIA_RICEVUTA_MESSAGGIO.getFieldType(),
					this.getObjectFromMap(map,"promemoriaRicevutaMessaggio"));
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
				setParameter(object, "setTipo", TipoVersamentoDominio.model().TIPO_VERSAMENTO.TIPO.getFieldType(),
					this.getObjectFromMap(map,"tipoVersamento.tipo"));
				setParameter(object, "setPagaTerzi", TipoVersamentoDominio.model().TIPO_VERSAMENTO.PAGA_TERZI.getFieldType(),
					this.getObjectFromMap(map,"tipoVersamento.pagaTerzi"));
				setParameter(object, "setAbilitato", TipoVersamentoDominio.model().TIPO_VERSAMENTO.ABILITATO.getFieldType(),
						this.getObjectFromMap(map,"tipoVersamento.abilitato"));
				setParameter(object, "setFormTipo", TipoVersamentoDominio.model().TIPO_VERSAMENTO.FORM_TIPO.getFieldType(),
					this.getObjectFromMap(map,"tipoVersamento.formTipo"));
				setParameter(object, "setFormDefinizione", TipoVersamentoDominio.model().TIPO_VERSAMENTO.FORM_DEFINIZIONE.getFieldType(),
					this.getObjectFromMap(map,"tipoVersamento.formDefinizione"));
				setParameter(object, "setValidazioneDefinizione", TipoVersamentoDominio.model().TIPO_VERSAMENTO.VALIDAZIONE_DEFINIZIONE.getFieldType(),
					this.getObjectFromMap(map,"tipoVersamento.validazioneDefinizione"));
				setParameter(object, "setTrasformazioneTipo", TipoVersamentoDominio.model().TIPO_VERSAMENTO.TRASFORMAZIONE_TIPO.getFieldType(),
					this.getObjectFromMap(map,"tipoVersamento.trasformazioneTipo"));
				setParameter(object, "setTrasformazioneDefinizione", TipoVersamentoDominio.model().TIPO_VERSAMENTO.TRASFORMAZIONE_DEFINIZIONE.getFieldType(),
					this.getObjectFromMap(map,"tipoVersamento.trasformazioneDefinizione"));
				setParameter(object, "setCodApplicazione", TipoVersamentoDominio.model().TIPO_VERSAMENTO.COD_APPLICAZIONE.getFieldType(),
					this.getObjectFromMap(map,"tipoVersamento.codApplicazione"));
				setParameter(object, "setPromemoriaAvvisoAbilitato", TipoVersamentoDominio.model().TIPO_VERSAMENTO.PROMEMORIA_AVVISO_ABILITATO.getFieldType(),
						this.getObjectFromMap(map,"tipoVersamento.promemoriaAvvisoAbilitato"));
				setParameter(object, "setPromemoriaAvvisoTipo", TipoVersamentoDominio.model().TIPO_VERSAMENTO.PROMEMORIA_AVVISO_TIPO.getFieldType(),
					this.getObjectFromMap(map,"tipoVersamento.promemoriaAvvisoTipo"));
				setParameter(object, "setPromemoriaAvvisoPdf", TipoVersamentoDominio.model().TIPO_VERSAMENTO.PROMEMORIA_AVVISO_PDF.getFieldType(),
					this.getObjectFromMap(map,"tipoVersamento.promemoriaAvvisoPdf"));
				setParameter(object, "setPromemoriaAvvisoOggetto", TipoVersamentoDominio.model().TIPO_VERSAMENTO.PROMEMORIA_AVVISO_OGGETTO.getFieldType(),
					this.getObjectFromMap(map,"tipoVersamento.promemoriaAvvisoOggetto"));
				setParameter(object, "setPromemoriaAvvisoMessaggio", TipoVersamentoDominio.model().TIPO_VERSAMENTO.PROMEMORIA_AVVISO_MESSAGGIO.getFieldType(),
					this.getObjectFromMap(map,"tipoVersamento.promemoriaAvvisoMessaggio"));
				setParameter(object, "setPromemoriaRicevutaAbilitato", TipoVersamentoDominio.model().TIPO_VERSAMENTO.PROMEMORIA_RICEVUTA_ABILITATO.getFieldType(),
						this.getObjectFromMap(map,"tipoVersamento.promemoriaRicevutaAbilitato"));
				setParameter(object, "setPromemoriaRicevutaTipo", TipoVersamentoDominio.model().TIPO_VERSAMENTO.PROMEMORIA_RICEVUTA_TIPO.getFieldType(),
					this.getObjectFromMap(map,"tipoVersamento.promemoriaRicevutaTipo"));
				setParameter(object, "setPromemoriaRicevutaPdf", TipoVersamentoDominio.model().TIPO_VERSAMENTO.PROMEMORIA_RICEVUTA_PDF.getFieldType(),
					this.getObjectFromMap(map,"tipoVersamento.promemoriaRicevutaPdf"));
				setParameter(object, "setPromemoriaRicevutaOggetto", TipoVersamentoDominio.model().TIPO_VERSAMENTO.PROMEMORIA_RICEVUTA_OGGETTO.getFieldType(),
					this.getObjectFromMap(map,"tipoVersamento.promemoriaRicevutaOggetto"));
				setParameter(object, "setPromemoriaRicevutaMessaggio", TipoVersamentoDominio.model().TIPO_VERSAMENTO.PROMEMORIA_RICEVUTA_MESSAGGIO.getFieldType(),
					this.getObjectFromMap(map,"tipoVersamento.promemoriaRicevutaMessaggio"));
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
