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

import it.govpay.orm.Batch;


/**     
 * BatchFetch
 *
 * @author Giovanni Bussu (bussu@link.it)
 * @author Lorenzo Nardi (nardi@link.it)
 * @author $Author$
 * @version $Rev$, $Date$
 */
public class BatchFetch extends AbstractJDBCFetch {

	@Override
	public Object fetch(TipiDatabase tipoDatabase, IModel<?> model , ResultSet rs) throws ServiceException {
		
		try{
			GenericJDBCParameterUtilities jdbcParameterUtilities =  
					new GenericJDBCParameterUtilities(tipoDatabase);

			if(model.equals(Batch.model())){
				Batch object = new Batch();
				setParameter(object, "setId", Long.class,
					jdbcParameterUtilities.readParameter(rs, "id", Long.class));
				setParameter(object, "setCodBatch", Batch.model().COD_BATCH.getFieldType(),
					jdbcParameterUtilities.readParameter(rs, "cod_batch", Batch.model().COD_BATCH.getFieldType()));
				setParameter(object, "setNodo", Batch.model().NODO.getFieldType(),
					jdbcParameterUtilities.readParameter(rs, "nodo", Batch.model().NODO.getFieldType()));
				setParameter(object, "setInizio", Batch.model().INIZIO.getFieldType(),
					jdbcParameterUtilities.readParameter(rs, "inizio", Batch.model().INIZIO.getFieldType()));
				setParameter(object, "setAggiornamento", Batch.model().AGGIORNAMENTO.getFieldType(),
					jdbcParameterUtilities.readParameter(rs, "aggiornamento", Batch.model().AGGIORNAMENTO.getFieldType()));
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

			if(model.equals(Batch.model())){
				Batch object = new Batch();
				setParameter(object, "setId", Long.class,
					this.getObjectFromMap(map,"id"));
				setParameter(object, "setCodBatch", Batch.model().COD_BATCH.getFieldType(),
					this.getObjectFromMap(map,"codBatch"));
				setParameter(object, "setNodo", Batch.model().NODO.getFieldType(),
					this.getObjectFromMap(map,"nodo"));
				setParameter(object, "setInizio", Batch.model().INIZIO.getFieldType(),
					this.getObjectFromMap(map,"inizio"));
				setParameter(object, "setAggiornamento", Batch.model().AGGIORNAMENTO.getFieldType(),
					this.getObjectFromMap(map,"aggiornamento"));
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

			if(model.equals(Batch.model())){
				return new org.openspcoop2.utils.jdbc.CustomKeyGeneratorObject("batch","id","seq_batch","batch_init_seq");
			}
			
			else{
				throw new ServiceException("Model ["+model.toString()+"] not supported by getKeyGeneratorObject: "+this.getClass().getName());
			}

		}catch(Exception e){
			throw new ServiceException("Model ["+model.toString()+"] occurs error in getKeyGeneratorObject: "+e.getMessage(),e);
		}
		
	}

}
