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

import it.govpay.orm.Stazione;


/**     
 * StazioneFetch
 *
 * @author Giovanni Bussu (bussu@link.it)
 * @author Lorenzo Nardi (nardi@link.it)
 * @author $Author$
 * @version $Rev$, $Date$
 */
public class StazioneFetch extends AbstractJDBCFetch {

	@Override
	public Object fetch(TipiDatabase tipoDatabase, IModel<?> model , ResultSet rs) throws ServiceException {
		
		try{
			JDBCParameterUtilities jdbcParameterUtilities =  
					new JDBCParameterUtilities(tipoDatabase);

			if(model.equals(Stazione.model())){
				Stazione object = new Stazione();
				setParameter(object, "setId", Long.class,
					jdbcParameterUtilities.readParameter(rs, "id", Long.class));
				setParameter(object, "setCodStazione", Stazione.model().COD_STAZIONE.getFieldType(),
					jdbcParameterUtilities.readParameter(rs, "cod_stazione", Stazione.model().COD_STAZIONE.getFieldType()));
				setParameter(object, "setPassword", Stazione.model().PASSWORD.getFieldType(),
					jdbcParameterUtilities.readParameter(rs, "password", Stazione.model().PASSWORD.getFieldType()));
				setParameter(object, "setAbilitato", Stazione.model().ABILITATO.getFieldType(),
					jdbcParameterUtilities.readParameter(rs, "abilitato", Stazione.model().ABILITATO.getFieldType()));
				setParameter(object, "setApplicationCode", Stazione.model().APPLICATION_CODE.getFieldType(),
					jdbcParameterUtilities.readParameter(rs, "application_code", Stazione.model().APPLICATION_CODE.getFieldType()));
				setParameter(object, "setNdpStato", Stazione.model().NDP_STATO.getFieldType(),
					jdbcParameterUtilities.readParameter(rs, "ndp_stato", Stazione.model().NDP_STATO.getFieldType()));
				setParameter(object, "setNdpOperazione", Stazione.model().NDP_OPERAZIONE.getFieldType(),
					jdbcParameterUtilities.readParameter(rs, "ndp_operazione", Stazione.model().NDP_OPERAZIONE.getFieldType()));
				setParameter(object, "setNdpDescrizione", Stazione.model().NDP_DESCRIZIONE.getFieldType(),
					jdbcParameterUtilities.readParameter(rs, "ndp_descrizione", Stazione.model().NDP_DESCRIZIONE.getFieldType()));
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

			if(model.equals(Stazione.model())){
				Stazione object = new Stazione();
				setParameter(object, "setId", Long.class,
					this.getObjectFromMap(map,"id"));
				setParameter(object, "setCodStazione", Stazione.model().COD_STAZIONE.getFieldType(),
					this.getObjectFromMap(map,"codStazione"));
				setParameter(object, "setPassword", Stazione.model().PASSWORD.getFieldType(),
					this.getObjectFromMap(map,"password"));
				setParameter(object, "setAbilitato", Stazione.model().ABILITATO.getFieldType(),
					this.getObjectFromMap(map,"abilitato"));
				setParameter(object, "setApplicationCode", Stazione.model().APPLICATION_CODE.getFieldType(),
					this.getObjectFromMap(map,"applicationCode"));
				setParameter(object, "setNdpStato", Stazione.model().NDP_STATO.getFieldType(),
					this.getObjectFromMap(map,"ndpStato"));
				setParameter(object, "setNdpOperazione", Stazione.model().NDP_OPERAZIONE.getFieldType(),
					this.getObjectFromMap(map,"ndpOperazione"));
				setParameter(object, "setNdpDescrizione", Stazione.model().NDP_DESCRIZIONE.getFieldType(),
					this.getObjectFromMap(map,"ndpDescrizione"));
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

			if(model.equals(Stazione.model())){
				return new org.openspcoop2.utils.jdbc.CustomKeyGeneratorObject("stazioni","id","seq_stazioni","stazioni_init_seq");
			}
			
			else{
				throw new ServiceException("Model ["+model.toString()+"] not supported by getKeyGeneratorObject: "+this.getClass().getName());
			}

		}catch(Exception e){
			throw new ServiceException("Model ["+model.toString()+"] occurs error in getKeyGeneratorObject: "+e.getMessage(),e);
		}
		
	}

}
