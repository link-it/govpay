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
			JDBCParameterUtilities jdbcParameterUtilities =  
					new JDBCParameterUtilities(tipoDatabase);

			if(model.equals(TipoVersamento.model())){
				TipoVersamento object = new TipoVersamento();
				setParameter(object, "setId", Long.class,
					jdbcParameterUtilities.readParameter(rs, "id", Long.class));
				setParameter(object, "setCodTipoVersamento", TipoVersamento.model().COD_TIPO_VERSAMENTO.getFieldType(),
					jdbcParameterUtilities.readParameter(rs, "cod_tipo_versamento", TipoVersamento.model().COD_TIPO_VERSAMENTO.getFieldType()));
				setParameter(object, "setDescrizione", TipoVersamento.model().DESCRIZIONE.getFieldType(),
					jdbcParameterUtilities.readParameter(rs, "descrizione", TipoVersamento.model().DESCRIZIONE.getFieldType()));
				setParameter(object, "setCodificaIuv", TipoVersamento.model().CODIFICA_IUV.getFieldType(),
					jdbcParameterUtilities.readParameter(rs, "codifica_iuv", TipoVersamento.model().CODIFICA_IUV.getFieldType()));
				setParameter(object, "setTipo", TipoVersamento.model().TIPO.getFieldType(),
					jdbcParameterUtilities.readParameter(rs, "tipo", TipoVersamento.model().TIPO.getFieldType()));
				setParameter(object, "setPagaTerzi", TipoVersamento.model().PAGA_TERZI.getFieldType(),
					jdbcParameterUtilities.readParameter(rs, "paga_terzi", TipoVersamento.model().PAGA_TERZI.getFieldType()));
				setParameter(object, "setAbilitato", TipoVersamento.model().ABILITATO.getFieldType(),
					jdbcParameterUtilities.readParameter(rs, "abilitato", TipoVersamento.model().ABILITATO.getFieldType()));
				setParameter(object, "setFormTipo", TipoVersamento.model().FORM_TIPO.getFieldType(),
					jdbcParameterUtilities.readParameter(rs, "form_tipo", TipoVersamento.model().FORM_TIPO.getFieldType()));
				setParameter(object, "setFormDefinizione", TipoVersamento.model().FORM_DEFINIZIONE.getFieldType(),
					jdbcParameterUtilities.readParameter(rs, "form_definizione", TipoVersamento.model().FORM_DEFINIZIONE.getFieldType()));
				setParameter(object, "setValidazioneDefinizione", TipoVersamento.model().VALIDAZIONE_DEFINIZIONE.getFieldType(),
					jdbcParameterUtilities.readParameter(rs, "validazione_definizione", TipoVersamento.model().VALIDAZIONE_DEFINIZIONE.getFieldType()));
				setParameter(object, "setTrasformazioneTipo", TipoVersamento.model().TRASFORMAZIONE_TIPO.getFieldType(),
					jdbcParameterUtilities.readParameter(rs, "trasformazione_tipo", TipoVersamento.model().TRASFORMAZIONE_TIPO.getFieldType()));
				setParameter(object, "setTrasformazioneDefinizione", TipoVersamento.model().TRASFORMAZIONE_DEFINIZIONE.getFieldType(),
					jdbcParameterUtilities.readParameter(rs, "trasformazione_definizione", TipoVersamento.model().TRASFORMAZIONE_DEFINIZIONE.getFieldType()));
				setParameter(object, "setCodApplicazione", TipoVersamento.model().COD_APPLICAZIONE.getFieldType(),
					jdbcParameterUtilities.readParameter(rs, "cod_applicazione", TipoVersamento.model().COD_APPLICAZIONE.getFieldType()));
				setParameter(object, "setPromemoriaAvvisoAbilitato", TipoVersamento.model().PROMEMORIA_AVVISO_ABILITATO.getFieldType(),
					jdbcParameterUtilities.readParameter(rs, "promemoria_avviso_abilitato", TipoVersamento.model().PROMEMORIA_AVVISO_ABILITATO.getFieldType()));
				setParameter(object, "setPromemoriaAvvisoPdf", TipoVersamento.model().PROMEMORIA_AVVISO_PDF.getFieldType(),
					jdbcParameterUtilities.readParameter(rs, "promemoria_avviso_pdf", TipoVersamento.model().PROMEMORIA_AVVISO_PDF.getFieldType()));
				setParameter(object, "setPromemoriaAvvisoTipo", TipoVersamento.model().PROMEMORIA_AVVISO_TIPO.getFieldType(),
					jdbcParameterUtilities.readParameter(rs, "promemoria_avviso_tipo", TipoVersamento.model().PROMEMORIA_AVVISO_TIPO.getFieldType()));
				setParameter(object, "setPromemoriaAvvisoOggetto", TipoVersamento.model().PROMEMORIA_AVVISO_OGGETTO.getFieldType(),
					jdbcParameterUtilities.readParameter(rs, "promemoria_avviso_oggetto", TipoVersamento.model().PROMEMORIA_AVVISO_OGGETTO.getFieldType()));
				setParameter(object, "setPromemoriaAvvisoMessaggio", TipoVersamento.model().PROMEMORIA_AVVISO_MESSAGGIO.getFieldType(),
					jdbcParameterUtilities.readParameter(rs, "promemoria_avviso_messaggio", TipoVersamento.model().PROMEMORIA_AVVISO_MESSAGGIO.getFieldType()));
				setParameter(object, "setPromemoriaRicevutaAbilitato", TipoVersamento.model().PROMEMORIA_RICEVUTA_ABILITATO.getFieldType(),
					jdbcParameterUtilities.readParameter(rs, "promemoria_ricevuta_abilitato", TipoVersamento.model().PROMEMORIA_RICEVUTA_ABILITATO.getFieldType()));
				setParameter(object, "setPromemoriaRicevutaTipo", TipoVersamento.model().PROMEMORIA_RICEVUTA_TIPO.getFieldType(),
					jdbcParameterUtilities.readParameter(rs, "promemoria_ricevuta_tipo", TipoVersamento.model().PROMEMORIA_RICEVUTA_TIPO.getFieldType()));
				setParameter(object, "setPromemoriaRicevutaPdf", TipoVersamento.model().PROMEMORIA_RICEVUTA_PDF.getFieldType(),
					jdbcParameterUtilities.readParameter(rs, "promemoria_ricevuta_pdf", TipoVersamento.model().PROMEMORIA_RICEVUTA_PDF.getFieldType()));
				setParameter(object, "setPromemoriaRicevutaOggetto", TipoVersamento.model().PROMEMORIA_RICEVUTA_OGGETTO.getFieldType(),
					jdbcParameterUtilities.readParameter(rs, "promemoria_ricevuta_oggetto", TipoVersamento.model().PROMEMORIA_RICEVUTA_OGGETTO.getFieldType()));
				setParameter(object, "setPromemoriaRicevutaMessaggio", TipoVersamento.model().PROMEMORIA_RICEVUTA_MESSAGGIO.getFieldType(),
					jdbcParameterUtilities.readParameter(rs, "promemoria_ricevuta_messaggio", TipoVersamento.model().PROMEMORIA_RICEVUTA_MESSAGGIO.getFieldType()));
				setParameter(object, "setVisualizzazioneDefinizione", TipoVersamento.model().VISUALIZZAZIONE_DEFINIZIONE.getFieldType(),
					jdbcParameterUtilities.readParameter(rs, "visualizzazione_definizione", TipoVersamento.model().VISUALIZZAZIONE_DEFINIZIONE.getFieldType()));
				setParameter(object, "setTracCsvTipo", TipoVersamento.model().TRAC_CSV_TIPO.getFieldType(),
					jdbcParameterUtilities.readParameter(rs, "trac_csv_tipo", TipoVersamento.model().TRAC_CSV_TIPO.getFieldType()));
				setParameter(object, "setTracCsvHeaderRisposta", TipoVersamento.model().TRAC_CSV_HEADER_RISPOSTA.getFieldType(),
					jdbcParameterUtilities.readParameter(rs, "trac_csv_header_risposta", TipoVersamento.model().TRAC_CSV_HEADER_RISPOSTA.getFieldType()));
				setParameter(object, "setTracCsvTemplateRichiesta", TipoVersamento.model().TRAC_CSV_TEMPLATE_RICHIESTA.getFieldType(),
					jdbcParameterUtilities.readParameter(rs, "trac_csv_template_richiesta", TipoVersamento.model().TRAC_CSV_TEMPLATE_RICHIESTA.getFieldType()));
				setParameter(object, "setTracCsvTemplateRisposta", TipoVersamento.model().TRAC_CSV_TEMPLATE_RISPOSTA.getFieldType(),
					jdbcParameterUtilities.readParameter(rs, "trac_csv_template_risposta", TipoVersamento.model().TRAC_CSV_TEMPLATE_RISPOSTA.getFieldType()));
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
				setParameter(object, "setTipo", TipoVersamento.model().TIPO.getFieldType(),
					this.getObjectFromMap(map,"tipo"));
				setParameter(object, "setPagaTerzi", TipoVersamento.model().PAGA_TERZI.getFieldType(),
					this.getObjectFromMap(map,"pagaTerzi"));
				setParameter(object, "setAbilitato", TipoVersamento.model().ABILITATO.getFieldType(),
					this.getObjectFromMap(map,"abilitato"));
				setParameter(object, "setFormTipo", TipoVersamento.model().FORM_TIPO.getFieldType(),
					this.getObjectFromMap(map,"formTipo"));
				setParameter(object, "setFormDefinizione", TipoVersamento.model().FORM_DEFINIZIONE.getFieldType(),
					this.getObjectFromMap(map,"formDefinizione"));
				setParameter(object, "setValidazioneDefinizione", TipoVersamento.model().VALIDAZIONE_DEFINIZIONE.getFieldType(),
					this.getObjectFromMap(map,"validazioneDefinizione"));
				setParameter(object, "setTrasformazioneTipo", TipoVersamento.model().TRASFORMAZIONE_TIPO.getFieldType(),
					this.getObjectFromMap(map,"trasformazioneTipo"));
				setParameter(object, "setTrasformazioneDefinizione", TipoVersamento.model().TRASFORMAZIONE_DEFINIZIONE.getFieldType(),
					this.getObjectFromMap(map,"trasformazioneDefinizione"));
				setParameter(object, "setCodApplicazione", TipoVersamento.model().COD_APPLICAZIONE.getFieldType(),
					this.getObjectFromMap(map,"codApplicazione"));
				setParameter(object, "setPromemoriaAvvisoAbilitato", TipoVersamento.model().PROMEMORIA_AVVISO_ABILITATO.getFieldType(),
					this.getObjectFromMap(map,"promemoriaAvvisoAbilitato"));
				setParameter(object, "setPromemoriaAvvisoPdf", TipoVersamento.model().PROMEMORIA_AVVISO_PDF.getFieldType(),
					this.getObjectFromMap(map,"promemoriaAvvisoPdf"));
				setParameter(object, "setPromemoriaAvvisoTipo", TipoVersamento.model().PROMEMORIA_AVVISO_TIPO.getFieldType(),
					this.getObjectFromMap(map,"promemoriaAvvisoTipo"));
				setParameter(object, "setPromemoriaAvvisoOggetto", TipoVersamento.model().PROMEMORIA_AVVISO_OGGETTO.getFieldType(),
					this.getObjectFromMap(map,"promemoriaAvvisoOggetto"));
				setParameter(object, "setPromemoriaAvvisoMessaggio", TipoVersamento.model().PROMEMORIA_AVVISO_MESSAGGIO.getFieldType(),
					this.getObjectFromMap(map,"promemoriaAvvisoMessaggio"));
				setParameter(object, "setPromemoriaRicevutaAbilitato", TipoVersamento.model().PROMEMORIA_RICEVUTA_ABILITATO.getFieldType(),
					this.getObjectFromMap(map,"promemoriaRicevutaAbilitato"));
				setParameter(object, "setPromemoriaRicevutaTipo", TipoVersamento.model().PROMEMORIA_RICEVUTA_TIPO.getFieldType(),
					this.getObjectFromMap(map,"promemoriaRicevutaTipo"));
				setParameter(object, "setPromemoriaRicevutaPdf", TipoVersamento.model().PROMEMORIA_RICEVUTA_PDF.getFieldType(),
					this.getObjectFromMap(map,"promemoriaRicevutaPdf"));
				setParameter(object, "setPromemoriaRicevutaOggetto", TipoVersamento.model().PROMEMORIA_RICEVUTA_OGGETTO.getFieldType(),
					this.getObjectFromMap(map,"promemoriaRicevutaOggetto"));
				setParameter(object, "setPromemoriaRicevutaMessaggio", TipoVersamento.model().PROMEMORIA_RICEVUTA_MESSAGGIO.getFieldType(),
					this.getObjectFromMap(map,"promemoriaRicevutaMessaggio"));
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
