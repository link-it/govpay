/*
 * GovPay - Porta di Accesso al Nodo dei Pagamenti SPC 
 * http://www.gov4j.it/govpay
 * 
 * Copyright (c) 2014-2026 Link.it srl (http://www.link.it).
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

import it.govpay.orm.JppaConfig;


/**     
 * JppaConfigFetch
 *
 * @author Giovanni Bussu (bussu@link.it)
 * @author Lorenzo Nardi (nardi@link.it)
 * @author $Author$
 * @version $Rev$, $Date$
 */
public class JppaConfigFetch extends AbstractJDBCFetch {

	@Override
	public Object fetch(TipiDatabase tipoDatabase, IModel<?> model , ResultSet rs) throws ServiceException {
		
		try{
			GenericJDBCParameterUtilities jdbcParameterUtilities =  
					new GenericJDBCParameterUtilities(tipoDatabase);

			if(model.equals(JppaConfig.model())){
				JppaConfig object = new JppaConfig();
				setParameter(object, "setId", Long.class,
					jdbcParameterUtilities.readParameter(rs, "id", Long.class));
				setParameter(object, "setCodDominio", JppaConfig.model().COD_DOMINIO.getFieldType(),
					jdbcParameterUtilities.readParameter(rs, "cod_dominio", JppaConfig.model().COD_DOMINIO.getFieldType()));
				setParameter(object, "setCodConnettore", JppaConfig.model().COD_CONNETTORE.getFieldType(),
					jdbcParameterUtilities.readParameter(rs, "cod_connettore", JppaConfig.model().COD_CONNETTORE.getFieldType()));
				setParameter(object, "setAbilitato", JppaConfig.model().ABILITATO.getFieldType(),
					jdbcParameterUtilities.readParameter(rs, "abilitato", JppaConfig.model().ABILITATO.getFieldType()));
				setParameter(object, "setDataUltimaRt", JppaConfig.model().DATA_ULTIMA_RT.getFieldType(),
					jdbcParameterUtilities.readParameter(rs, "data_ultima_rt", JppaConfig.model().DATA_ULTIMA_RT.getFieldType()));
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

			if(model.equals(JppaConfig.model())){
				JppaConfig object = new JppaConfig();
				setParameter(object, "setId", Long.class,
					this.getObjectFromMap(map,"id"));
				setParameter(object, "setCodDominio", JppaConfig.model().COD_DOMINIO.getFieldType(),
					this.getObjectFromMap(map,"codDominio"));
				setParameter(object, "setCodConnettore", JppaConfig.model().COD_CONNETTORE.getFieldType(),
					this.getObjectFromMap(map,"codConnettore"));
				setParameter(object, "setAbilitato", JppaConfig.model().ABILITATO.getFieldType(),
					this.getObjectFromMap(map,"abilitato"));
				setParameter(object, "setDataUltimaRt", JppaConfig.model().DATA_ULTIMA_RT.getFieldType(),
					this.getObjectFromMap(map,"dataUltimaRt"));
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

			if(model.equals(JppaConfig.model())){
				return new org.openspcoop2.utils.jdbc.CustomKeyGeneratorObject("jppa_config","id","seq_jppa_config","jppa_config_init_seq");
			}
			
			else{
				throw new ServiceException("Model ["+model.toString()+"] not supported by getKeyGeneratorObject: "+this.getClass().getName());
			}

		}catch(Exception e){
			throw new ServiceException("Model ["+model.toString()+"] occurs error in getKeyGeneratorObject: "+e.getMessage(),e);
		}
		
	}

}
