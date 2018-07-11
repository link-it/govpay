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

import it.govpay.orm.Tracciato;


/**     
 * TracciatoFetch
 *
 * @author Giovanni Bussu (bussu@link.it)
 * @author Lorenzo Nardi (nardi@link.it)
 * @author $Author$
 * @version $Rev$, $Date$
 */
public class TracciatoFetch extends AbstractJDBCFetch {

	@Override
	public Object fetch(TipiDatabase tipoDatabase, IModel<?> model , ResultSet rs) throws ServiceException {
		
		try{
			JDBCParameterUtilities jdbcParameterUtilities =  
					new JDBCParameterUtilities(tipoDatabase);

			if(model.equals(Tracciato.model())){
				Tracciato object = new Tracciato();
				setParameter(object, "setId", Long.class,
					jdbcParameterUtilities.readParameter(rs, "id", Long.class));
				setParameter(object, "setCodDominio", Tracciato.model().COD_DOMINIO.getFieldType(),
					jdbcParameterUtilities.readParameter(rs, "cod_dominio", Tracciato.model().COD_DOMINIO.getFieldType()));
				setParameter(object, "setTipo", Tracciato.model().TIPO.getFieldType(),
					jdbcParameterUtilities.readParameter(rs, "tipo", Tracciato.model().TIPO.getFieldType()));
				setParameter(object, "setStato", Tracciato.model().STATO.getFieldType(),
					jdbcParameterUtilities.readParameter(rs, "stato", Tracciato.model().STATO.getFieldType()));
				setParameter(object, "setDescrizioneStato", Tracciato.model().DESCRIZIONE_STATO.getFieldType(),
					jdbcParameterUtilities.readParameter(rs, "descrizione_stato", Tracciato.model().DESCRIZIONE_STATO.getFieldType()));
				setParameter(object, "setDataCaricamento", Tracciato.model().DATA_CARICAMENTO.getFieldType(),
					jdbcParameterUtilities.readParameter(rs, "data_caricamento", Tracciato.model().DATA_CARICAMENTO.getFieldType()));
				setParameter(object, "setDataCompletamento", Tracciato.model().DATA_COMPLETAMENTO.getFieldType(),
					jdbcParameterUtilities.readParameter(rs, "data_completamento", Tracciato.model().DATA_COMPLETAMENTO.getFieldType()));
				setParameter(object, "setBeanDati", Tracciato.model().BEAN_DATI.getFieldType(),
					jdbcParameterUtilities.readParameter(rs, "bean_dati", Tracciato.model().BEAN_DATI.getFieldType()));
				setParameter(object, "setFileNameRichiesta", Tracciato.model().FILE_NAME_RICHIESTA.getFieldType(),
					jdbcParameterUtilities.readParameter(rs, "file_name_richiesta", Tracciato.model().FILE_NAME_RICHIESTA.getFieldType()));
				setParameter(object, "setRawRichiesta", Tracciato.model().RAW_RICHIESTA.getFieldType(),
					jdbcParameterUtilities.readParameter(rs, "raw_richiesta", Tracciato.model().RAW_RICHIESTA.getFieldType()));
				setParameter(object, "setFileNameEsito", Tracciato.model().FILE_NAME_ESITO.getFieldType(),
					jdbcParameterUtilities.readParameter(rs, "file_name_esito", Tracciato.model().FILE_NAME_ESITO.getFieldType()));
				setParameter(object, "setRawEsito", Tracciato.model().RAW_ESITO.getFieldType(),
					jdbcParameterUtilities.readParameter(rs, "raw_esito", Tracciato.model().RAW_ESITO.getFieldType()));
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

			if(model.equals(Tracciato.model())){
				Tracciato object = new Tracciato();
				setParameter(object, "setId", Long.class,
					this.getObjectFromMap(map,"id"));
				setParameter(object, "setCodDominio", Tracciato.model().COD_DOMINIO.getFieldType(),
					this.getObjectFromMap(map,"codDominio"));
				setParameter(object, "setTipo", Tracciato.model().TIPO.getFieldType(),
					this.getObjectFromMap(map,"tipo"));
				setParameter(object, "setStato", Tracciato.model().STATO.getFieldType(),
					this.getObjectFromMap(map,"stato"));
				setParameter(object, "setDescrizioneStato", Tracciato.model().DESCRIZIONE_STATO.getFieldType(),
					this.getObjectFromMap(map,"descrizioneStato"));
				setParameter(object, "setDataCaricamento", Tracciato.model().DATA_CARICAMENTO.getFieldType(),
					this.getObjectFromMap(map,"dataCaricamento"));
				setParameter(object, "setDataCompletamento", Tracciato.model().DATA_COMPLETAMENTO.getFieldType(),
					this.getObjectFromMap(map,"dataCompletamento"));
				setParameter(object, "setBeanDati", Tracciato.model().BEAN_DATI.getFieldType(),
					this.getObjectFromMap(map,"beanDati"));
				setParameter(object, "setFileNameRichiesta", Tracciato.model().FILE_NAME_RICHIESTA.getFieldType(),
					this.getObjectFromMap(map,"fileNameRichiesta"));
				setParameter(object, "setRawRichiesta", Tracciato.model().RAW_RICHIESTA.getFieldType(),
					this.getObjectFromMap(map,"rawRichiesta"));
				setParameter(object, "setFileNameEsito", Tracciato.model().FILE_NAME_ESITO.getFieldType(),
					this.getObjectFromMap(map,"fileNameEsito"));
				setParameter(object, "setRawEsito", Tracciato.model().RAW_ESITO.getFieldType(),
					this.getObjectFromMap(map,"rawEsito"));
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

			if(model.equals(Tracciato.model())){
				return new org.openspcoop2.utils.jdbc.CustomKeyGeneratorObject("tracciati","id","seq_tracciati","tracciati_init_seq");
			}
			
			else{
				throw new ServiceException("Model ["+model.toString()+"] not supported by getKeyGeneratorObject: "+this.getClass().getName());
			}

		}catch(Exception e){
			throw new ServiceException("Model ["+model.toString()+"] occurs error in getKeyGeneratorObject: "+e.getMessage(),e);
		}
		
	}

}
