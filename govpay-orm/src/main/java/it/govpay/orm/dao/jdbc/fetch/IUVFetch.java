/*
 * GovPay - Porta di Accesso al Nodo dei Pagamenti SPC 
 * http://www.gov4j.it/govpay
 * 
 * Copyright (c) 2014-2016 Link.it srl (http://www.link.it).
 * 
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
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

import it.govpay.orm.IUV;


/**     
 * IUVFetch
 *
 * @author Giovanni Bussu (bussu@link.it)
 * @author Lorenzo Nardi (nardi@link.it)
 * @author $Author$
 * @version $Rev$, $Date$
 */
public class IUVFetch extends AbstractJDBCFetch {

	@Override
	public Object fetch(TipiDatabase tipoDatabase, IModel<?> model , ResultSet rs) throws ServiceException {
		
		try{
			JDBCParameterUtilities jdbcParameterUtilities =  
					new JDBCParameterUtilities(tipoDatabase);

			if(model.equals(IUV.model())){
				IUV object = new IUV();
				setParameter(object, "setId", Long.class,
					jdbcParameterUtilities.readParameter(rs, "id", Long.class));
				setParameter(object, "setPrg", IUV.model().PRG.getFieldType(),
					jdbcParameterUtilities.readParameter(rs, "prg", IUV.model().PRG.getFieldType()));
				setParameter(object, "setIuv", IUV.model().IUV.getFieldType(),
					jdbcParameterUtilities.readParameter(rs, "iuv", IUV.model().IUV.getFieldType()));
				setParameter(object, "setApplicationCode", IUV.model().APPLICATION_CODE.getFieldType(),
					jdbcParameterUtilities.readParameter(rs, "application_code", IUV.model().APPLICATION_CODE.getFieldType()));
				setParameter(object, "setDataGenerazione", IUV.model().DATA_GENERAZIONE.getFieldType(),
					jdbcParameterUtilities.readParameter(rs, "data_generazione", IUV.model().DATA_GENERAZIONE.getFieldType()));
				setParameter(object, "setTipoIuv", IUV.model().TIPO_IUV.getFieldType(),
					jdbcParameterUtilities.readParameter(rs, "tipo_iuv", IUV.model().TIPO_IUV.getFieldType()));
				setParameter(object, "setCodVersamentoEnte", IUV.model().COD_VERSAMENTO_ENTE.getFieldType(),
					jdbcParameterUtilities.readParameter(rs, "cod_versamento_ente", IUV.model().COD_VERSAMENTO_ENTE.getFieldType()));
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

			if(model.equals(IUV.model())){
				IUV object = new IUV();
				setParameter(object, "setId", Long.class,
					this.getObjectFromMap(map,"id"));
				setParameter(object, "setPrg", IUV.model().PRG.getFieldType(),
					this.getObjectFromMap(map,"prg"));
				setParameter(object, "setIuv", IUV.model().IUV.getFieldType(),
					this.getObjectFromMap(map,"iuv"));
				setParameter(object, "setApplicationCode", IUV.model().APPLICATION_CODE.getFieldType(),
					this.getObjectFromMap(map,"applicationCode"));
				setParameter(object, "setDataGenerazione", IUV.model().DATA_GENERAZIONE.getFieldType(),
					this.getObjectFromMap(map,"dataGenerazione"));
				setParameter(object, "setTipoIuv", IUV.model().TIPO_IUV.getFieldType(),
					this.getObjectFromMap(map,"tipoIuv"));
				setParameter(object, "setCodVersamentoEnte", IUV.model().COD_VERSAMENTO_ENTE.getFieldType(),
					this.getObjectFromMap(map,"codVersamentoEnte"));
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

			if(model.equals(IUV.model())){
				return new org.openspcoop2.utils.jdbc.CustomKeyGeneratorObject("iuv","id","seq_iuv","iuv_init_seq");
			}
			
			else{
				throw new ServiceException("Model ["+model.toString()+"] not supported by getKeyGeneratorObject: "+this.getClass().getName());
			}

		}catch(Exception e){
			throw new ServiceException("Model ["+model.toString()+"] occurs error in getKeyGeneratorObject: "+e.getMessage(),e);
		}
		
	}

}
