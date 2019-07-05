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

import it.govpay.orm.Promemoria;


/**     
 * PromemoriaFetch
 *
 * @author Giovanni Bussu (bussu@link.it)
 * @author Lorenzo Nardi (nardi@link.it)
 * @author $Author$
 * @version $Rev$, $Date$
 */
public class PromemoriaFetch extends AbstractJDBCFetch {

	@Override
	public Object fetch(TipiDatabase tipoDatabase, IModel<?> model , ResultSet rs) throws ServiceException {
		
		try{
			JDBCParameterUtilities jdbcParameterUtilities =  
					new JDBCParameterUtilities(tipoDatabase);

			if(model.equals(Promemoria.model())){
				Promemoria object = new Promemoria();
				setParameter(object, "setId", Long.class,
					jdbcParameterUtilities.readParameter(rs, "id", Long.class));
				setParameter(object, "setTipo", Promemoria.model().TIPO.getFieldType(),
					jdbcParameterUtilities.readParameter(rs, "tipo", Promemoria.model().TIPO.getFieldType()));
				setParameter(object, "setDataCreazione", Promemoria.model().DATA_CREAZIONE.getFieldType(),
					jdbcParameterUtilities.readParameter(rs, "data_creazione", Promemoria.model().DATA_CREAZIONE.getFieldType()));
				setParameter(object, "setStato", Promemoria.model().STATO.getFieldType(),
					jdbcParameterUtilities.readParameter(rs, "stato", Promemoria.model().STATO.getFieldType()));
				setParameter(object, "setDescrizioneStato", Promemoria.model().DESCRIZIONE_STATO.getFieldType(),
					jdbcParameterUtilities.readParameter(rs, "descrizione_stato", Promemoria.model().DESCRIZIONE_STATO.getFieldType()));
				setParameter(object, "setDebitoreEmail", Promemoria.model().DEBITORE_EMAIL.getFieldType(),
					jdbcParameterUtilities.readParameter(rs, "debitore_email", Promemoria.model().DEBITORE_EMAIL.getFieldType()));
				setParameter(object, "setMessaggioContentType", Promemoria.model().MESSAGGIO_CONTENT_TYPE.getFieldType(),
					jdbcParameterUtilities.readParameter(rs, "messaggio_content_type", Promemoria.model().MESSAGGIO_CONTENT_TYPE.getFieldType()));
				setParameter(object, "setOggetto", Promemoria.model().OGGETTO.getFieldType(),
					jdbcParameterUtilities.readParameter(rs, "oggetto", Promemoria.model().OGGETTO.getFieldType()));
				setParameter(object, "setMessaggio", Promemoria.model().MESSAGGIO.getFieldType(),
					jdbcParameterUtilities.readParameter(rs, "messaggio", Promemoria.model().MESSAGGIO.getFieldType()));
				setParameter(object, "setAllegaPdf", Promemoria.model().ALLEGA_PDF.getFieldType(),
					jdbcParameterUtilities.readParameter(rs, "allega_pdf", Promemoria.model().ALLEGA_PDF.getFieldType()));
				setParameter(object, "setDataAggiornamentoStato", Promemoria.model().DATA_AGGIORNAMENTO_STATO.getFieldType(),
					jdbcParameterUtilities.readParameter(rs, "data_aggiornamento_stato", Promemoria.model().DATA_AGGIORNAMENTO_STATO.getFieldType()));
				setParameter(object, "setDataProssimaSpedizione", Promemoria.model().DATA_PROSSIMA_SPEDIZIONE.getFieldType(),
					jdbcParameterUtilities.readParameter(rs, "data_prossima_spedizione", Promemoria.model().DATA_PROSSIMA_SPEDIZIONE.getFieldType()));
				setParameter(object, "setTentativiSpedizione", Promemoria.model().TENTATIVI_SPEDIZIONE.getFieldType(),
					jdbcParameterUtilities.readParameter(rs, "tentativi_spedizione", Promemoria.model().TENTATIVI_SPEDIZIONE.getFieldType()));
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

			if(model.equals(Promemoria.model())){
				Promemoria object = new Promemoria();
				setParameter(object, "setId", Long.class,
					this.getObjectFromMap(map,"id"));
				setParameter(object, "setTipo", Promemoria.model().TIPO.getFieldType(),
					this.getObjectFromMap(map,"tipo"));
				setParameter(object, "setDataCreazione", Promemoria.model().DATA_CREAZIONE.getFieldType(),
					this.getObjectFromMap(map,"dataCreazione"));
				setParameter(object, "setStato", Promemoria.model().STATO.getFieldType(),
					this.getObjectFromMap(map,"stato"));
				setParameter(object, "setDescrizioneStato", Promemoria.model().DESCRIZIONE_STATO.getFieldType(),
					this.getObjectFromMap(map,"descrizioneStato"));
				setParameter(object, "setDebitoreEmail", Promemoria.model().DEBITORE_EMAIL.getFieldType(),
					this.getObjectFromMap(map,"debitoreEmail"));
				setParameter(object, "setMessaggioContentType", Promemoria.model().MESSAGGIO_CONTENT_TYPE.getFieldType(),
					this.getObjectFromMap(map,"messaggioContentType"));
				setParameter(object, "setOggetto", Promemoria.model().OGGETTO.getFieldType(),
					this.getObjectFromMap(map,"oggetto"));
				setParameter(object, "setMessaggio", Promemoria.model().MESSAGGIO.getFieldType(),
					this.getObjectFromMap(map,"messaggio"));
				setParameter(object, "setAllegaPdf", Promemoria.model().ALLEGA_PDF.getFieldType(),
					this.getObjectFromMap(map,"allegaPdf"));
				setParameter(object, "setDataAggiornamentoStato", Promemoria.model().DATA_AGGIORNAMENTO_STATO.getFieldType(),
					this.getObjectFromMap(map,"dataAggiornamentoStato"));
				setParameter(object, "setDataProssimaSpedizione", Promemoria.model().DATA_PROSSIMA_SPEDIZIONE.getFieldType(),
					this.getObjectFromMap(map,"dataProssimaSpedizione"));
				setParameter(object, "setTentativiSpedizione", Promemoria.model().TENTATIVI_SPEDIZIONE.getFieldType(),
					this.getObjectFromMap(map,"tentativiSpedizione"));
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

			if(model.equals(Promemoria.model())){
				return new org.openspcoop2.utils.jdbc.CustomKeyGeneratorObject("promemoria","id","seq_promemoria","promemoria_init_seq");
			}
			
			else{
				throw new ServiceException("Model ["+model.toString()+"] not supported by getKeyGeneratorObject: "+this.getClass().getName());
			}

		}catch(Exception e){
			throw new ServiceException("Model ["+model.toString()+"] occurs error in getKeyGeneratorObject: "+e.getMessage(),e);
		}
		
	}

}
