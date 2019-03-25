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

import it.govpay.orm.TipoTributo;


/**     
 * TipoTributoFetch
 *
 * @author Giovanni Bussu (bussu@link.it)
 * @author Lorenzo Nardi (nardi@link.it)
 * @author $Author$
 * @version $Rev$, $Date$
 */
public class TipoTributoFetch extends AbstractJDBCFetch {

	@Override
	public Object fetch(TipiDatabase tipoDatabase, IModel<?> model , ResultSet rs) throws ServiceException {
		
		try{
			JDBCParameterUtilities jdbcParameterUtilities =  
					new JDBCParameterUtilities(tipoDatabase);

			if(model.equals(TipoTributo.model())){
				TipoTributo object = new TipoTributo();
				this.setParameter(object, "setId", Long.class,
					jdbcParameterUtilities.readParameter(rs, "id", Long.class));
				this.setParameter(object, "setCodTributo", TipoTributo.model().COD_TRIBUTO.getFieldType(),
					jdbcParameterUtilities.readParameter(rs, "cod_tributo", TipoTributo.model().COD_TRIBUTO.getFieldType()));
				this.setParameter(object, "setDescrizione", TipoTributo.model().DESCRIZIONE.getFieldType(),
					jdbcParameterUtilities.readParameter(rs, "descrizione", TipoTributo.model().DESCRIZIONE.getFieldType()));
				this.setParameter(object, "setTipoContabilita", TipoTributo.model().TIPO_CONTABILITA.getFieldType(),
					jdbcParameterUtilities.readParameter(rs, "tipo_contabilita", TipoTributo.model().TIPO_CONTABILITA.getFieldType()));
				this.setParameter(object, "setCodContabilita", TipoTributo.model().COD_CONTABILITA.getFieldType(),
					jdbcParameterUtilities.readParameter(rs, "cod_contabilita", TipoTributo.model().COD_CONTABILITA.getFieldType()));
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

			if(model.equals(TipoTributo.model())){
				TipoTributo object = new TipoTributo();
				this.setParameter(object, "setId", Long.class,
					this.getObjectFromMap(map,"id"));
				this.setParameter(object, "setCodTributo", TipoTributo.model().COD_TRIBUTO.getFieldType(),
					this.getObjectFromMap(map,"codTributo"));
				this.setParameter(object, "setDescrizione", TipoTributo.model().DESCRIZIONE.getFieldType(),
					this.getObjectFromMap(map,"descrizione"));
				this.setParameter(object, "setTipoContabilita", TipoTributo.model().TIPO_CONTABILITA.getFieldType(),
					this.getObjectFromMap(map,"tipoContabilita"));
				this.setParameter(object, "setCodContabilita", TipoTributo.model().COD_CONTABILITA.getFieldType(),
					this.getObjectFromMap(map,"codContabilita"));
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

			if(model.equals(TipoTributo.model())){
				return new org.openspcoop2.utils.jdbc.CustomKeyGeneratorObject("tipi_tributo","id","seq_tipi_tributo","tipi_tributo_init_seq");
			}
			
			else{
				throw new ServiceException("Model ["+model.toString()+"] not supported by getKeyGeneratorObject: "+this.getClass().getName());
			}

		}catch(Exception e){
			throw new ServiceException("Model ["+model.toString()+"] occurs error in getKeyGeneratorObject: "+e.getMessage(),e);
		}
		
	}

}
