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

import it.govpay.orm.TracciatoMyPivot;


/**     
 * TracciatoMyPivotFetch
 *
 * @author Giovanni Bussu (bussu@link.it)
 * @author Lorenzo Nardi (nardi@link.it)
 * @author $Author$
 * @version $Rev$, $Date$
 */
public class TracciatoMyPivotFetch extends AbstractJDBCFetch {

	@Override
	public Object fetch(TipiDatabase tipoDatabase, IModel<?> model , ResultSet rs) throws ServiceException {
		
		try{
			JDBCParameterUtilities jdbcParameterUtilities =  
					new JDBCParameterUtilities(tipoDatabase);

			if(model.equals(TracciatoMyPivot.model())){
				TracciatoMyPivot object = new TracciatoMyPivot();
				setParameter(object, "setId", Long.class,
					jdbcParameterUtilities.readParameter(rs, "id", Long.class));
				setParameter(object, "setNomeFile", TracciatoMyPivot.model().NOME_FILE.getFieldType(),
					jdbcParameterUtilities.readParameter(rs, "nome_file", TracciatoMyPivot.model().NOME_FILE.getFieldType()));
				setParameter(object, "setStato", TracciatoMyPivot.model().STATO.getFieldType(),
					jdbcParameterUtilities.readParameter(rs, "stato", TracciatoMyPivot.model().STATO.getFieldType()));
				setParameter(object, "setDataCreazione", TracciatoMyPivot.model().DATA_CREAZIONE.getFieldType(),
					jdbcParameterUtilities.readParameter(rs, "data_creazione", TracciatoMyPivot.model().DATA_CREAZIONE.getFieldType()));
				setParameter(object, "setDataRtDa", TracciatoMyPivot.model().DATA_RT_DA.getFieldType(),
					jdbcParameterUtilities.readParameter(rs, "data_rt_da", TracciatoMyPivot.model().DATA_RT_DA.getFieldType()));
				setParameter(object, "setDataRtA", TracciatoMyPivot.model().DATA_RT_A.getFieldType(),
					jdbcParameterUtilities.readParameter(rs, "data_rt_a", TracciatoMyPivot.model().DATA_RT_A.getFieldType()));
				setParameter(object, "setDataCaricamento", TracciatoMyPivot.model().DATA_CARICAMENTO.getFieldType(),
					jdbcParameterUtilities.readParameter(rs, "data_caricamento", TracciatoMyPivot.model().DATA_CARICAMENTO.getFieldType()));
				setParameter(object, "setDataCompletamento", TracciatoMyPivot.model().DATA_COMPLETAMENTO.getFieldType(),
					jdbcParameterUtilities.readParameter(rs, "data_completamento", TracciatoMyPivot.model().DATA_COMPLETAMENTO.getFieldType()));
				setParameter(object, "setRequestToken", TracciatoMyPivot.model().REQUEST_TOKEN.getFieldType(),
					jdbcParameterUtilities.readParameter(rs, "request_token", TracciatoMyPivot.model().REQUEST_TOKEN.getFieldType()));
				setParameter(object, "setUploadUrl", TracciatoMyPivot.model().UPLOAD_URL.getFieldType(),
					jdbcParameterUtilities.readParameter(rs, "upload_url", TracciatoMyPivot.model().UPLOAD_URL.getFieldType()));
				setParameter(object, "setAuthorizationToken", TracciatoMyPivot.model().AUTHORIZATION_TOKEN.getFieldType(),
					jdbcParameterUtilities.readParameter(rs, "authorization_token", TracciatoMyPivot.model().AUTHORIZATION_TOKEN.getFieldType()));
				setParameter(object, "setRawContenuto", TracciatoMyPivot.model().RAW_CONTENUTO.getFieldType(),
					jdbcParameterUtilities.readParameter(rs, "raw_contenuto", TracciatoMyPivot.model().RAW_CONTENUTO.getFieldType()));
				setParameter(object, "setBeanDati", TracciatoMyPivot.model().BEAN_DATI.getFieldType(),
					jdbcParameterUtilities.readParameter(rs, "bean_dati", TracciatoMyPivot.model().BEAN_DATI.getFieldType()));
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

			if(model.equals(TracciatoMyPivot.model())){
				TracciatoMyPivot object = new TracciatoMyPivot();
				setParameter(object, "setId", Long.class,
					this.getObjectFromMap(map,"id"));
				setParameter(object, "setNomeFile", TracciatoMyPivot.model().NOME_FILE.getFieldType(),
					this.getObjectFromMap(map,"nomeFile"));
				setParameter(object, "setStato", TracciatoMyPivot.model().STATO.getFieldType(),
					this.getObjectFromMap(map,"stato"));
				setParameter(object, "setDataCreazione", TracciatoMyPivot.model().DATA_CREAZIONE.getFieldType(),
					this.getObjectFromMap(map,"dataCreazione"));
				setParameter(object, "setDataRtDa", TracciatoMyPivot.model().DATA_RT_DA.getFieldType(),
					this.getObjectFromMap(map,"dataRtDa"));
				setParameter(object, "setDataRtA", TracciatoMyPivot.model().DATA_RT_A.getFieldType(),
					this.getObjectFromMap(map,"dataRtA"));
				setParameter(object, "setDataCaricamento", TracciatoMyPivot.model().DATA_CARICAMENTO.getFieldType(),
					this.getObjectFromMap(map,"dataCaricamento"));
				setParameter(object, "setDataCompletamento", TracciatoMyPivot.model().DATA_COMPLETAMENTO.getFieldType(),
					this.getObjectFromMap(map,"dataCompletamento"));
				setParameter(object, "setRequestToken", TracciatoMyPivot.model().REQUEST_TOKEN.getFieldType(),
					this.getObjectFromMap(map,"requestToken"));
				setParameter(object, "setUploadUrl", TracciatoMyPivot.model().UPLOAD_URL.getFieldType(),
					this.getObjectFromMap(map,"uploadUrl"));
				setParameter(object, "setAuthorizationToken", TracciatoMyPivot.model().AUTHORIZATION_TOKEN.getFieldType(),
					this.getObjectFromMap(map,"authorizationToken"));
				setParameter(object, "setRawContenuto", TracciatoMyPivot.model().RAW_CONTENUTO.getFieldType(),
					this.getObjectFromMap(map,"rawContenuto"));
				setParameter(object, "setBeanDati", TracciatoMyPivot.model().BEAN_DATI.getFieldType(),
					this.getObjectFromMap(map,"beanDati"));
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

			if(model.equals(TracciatoMyPivot.model())){
				return new org.openspcoop2.utils.jdbc.CustomKeyGeneratorObject("mypivot_notifiche_pag","id","seq_mypivot_notifiche_pag","mypivot_notifiche_pag_init_seq");
			}
			
			else{
				throw new ServiceException("Model ["+model.toString()+"] not supported by getKeyGeneratorObject: "+this.getClass().getName());
			}

		}catch(Exception e){
			throw new ServiceException("Model ["+model.toString()+"] occurs error in getKeyGeneratorObject: "+e.getMessage(),e);
		}
		
	}

}
