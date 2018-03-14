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

import java.sql.ResultSet;
import java.util.Map;

import org.openspcoop2.generic_project.beans.IModel;
import org.openspcoop2.generic_project.dao.jdbc.utils.AbstractJDBCFetch;
import org.openspcoop2.generic_project.dao.jdbc.utils.JDBCParameterUtilities;
import org.openspcoop2.generic_project.exception.ServiceException;
import org.openspcoop2.utils.TipiDatabase;
import org.openspcoop2.utils.jdbc.IKeyGeneratorObject;

import it.govpay.orm.TipoTributo;
import it.govpay.orm.Tributo;


/**     
 * TributoFetch
 *
 * @author Giovanni Bussu (bussu@link.it)
 * @author Lorenzo Nardi (nardi@link.it)
 * @author $Author$
 * @version $Rev$, $Date$
 */
public class TributoFetch extends AbstractJDBCFetch {

	@Override
	public Object fetch(TipiDatabase tipoDatabase, IModel<?> model , ResultSet rs) throws ServiceException {
		
		try{
			JDBCParameterUtilities jdbcParameterUtilities =  
					new JDBCParameterUtilities(tipoDatabase);

			if(model.equals(Tributo.model())){
				Tributo object = new Tributo();
				setParameter(object, "setId", Long.class,
					jdbcParameterUtilities.readParameter(rs, "id", Long.class));
				setParameter(object, "setAbilitato", Tributo.model().ABILITATO.getFieldType(),
					jdbcParameterUtilities.readParameter(rs, "abilitato", Tributo.model().ABILITATO.getFieldType()));
				setParameter(object, "setTipoContabilita", Tributo.model().TIPO_CONTABILITA.getFieldType(),
					jdbcParameterUtilities.readParameter(rs, "tipo_contabilita", Tributo.model().TIPO_CONTABILITA.getFieldType()));
				setParameter(object, "setCodiceContabilita", Tributo.model().CODICE_CONTABILITA.getFieldType(),
					jdbcParameterUtilities.readParameter(rs, "codice_contabilita", Tributo.model().CODICE_CONTABILITA.getFieldType()));
				setParameter(object, "setCodTributoIuv", Tributo.model().COD_TRIBUTO_IUV.getFieldType(),
					jdbcParameterUtilities.readParameter(rs, "cod_tributo_iuv", Tributo.model().COD_TRIBUTO_IUV.getFieldType()));
				return object;
			 } else if(model.equals(Tributo.model().TIPO_TRIBUTO)){
		         TipoTributo object = new TipoTributo();
		         setParameter(object, "setId", Long.class,
		                 jdbcParameterUtilities.readParameter(rs, "id", Long.class));
		         setParameter(object, "setCodTributo", Tributo.model().TIPO_TRIBUTO.COD_TRIBUTO.getFieldType(),
		                 jdbcParameterUtilities.readParameter(rs, "cod_tributo", Tributo.model().TIPO_TRIBUTO.COD_TRIBUTO.getFieldType()));
		         setParameter(object, "setDescrizione", Tributo.model().TIPO_TRIBUTO.DESCRIZIONE.getFieldType(),
		                 jdbcParameterUtilities.readParameter(rs, "descrizione", Tributo.model().TIPO_TRIBUTO.DESCRIZIONE.getFieldType()));
		         setParameter(object, "setCodContabilita", Tributo.model().TIPO_TRIBUTO.COD_CONTABILITA.getFieldType(),
		                 jdbcParameterUtilities.readParameter(rs, "cod_contabilita", Tributo.model().TIPO_TRIBUTO.COD_CONTABILITA.getFieldType()));
		         setParameter(object, "setCodTributoIuv", Tributo.model().TIPO_TRIBUTO.COD_TRIBUTO_IUV.getFieldType(),
		                 jdbcParameterUtilities.readParameter(rs, "cod_tributo_iuv", Tributo.model().TIPO_TRIBUTO.COD_TRIBUTO_IUV.getFieldType()));
		         setParameter(object, "setTipoContabilita", Tributo.model().TIPO_TRIBUTO.TIPO_CONTABILITA.getFieldType(),
		                 jdbcParameterUtilities.readParameter(rs, "tipo_contabilita", Tributo.model().TIPO_TRIBUTO.TIPO_CONTABILITA.getFieldType()));
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

			if(model.equals(Tributo.model())){
				Tributo object = new Tributo();
				setParameter(object, "setId", Long.class,
					this.getObjectFromMap(map,"id"));
				setParameter(object, "setAbilitato", Tributo.model().ABILITATO.getFieldType(),
					this.getObjectFromMap(map,"abilitato"));
				setParameter(object, "setTipoContabilita", Tributo.model().TIPO_CONTABILITA.getFieldType(),
					this.getObjectFromMap(map,"tipoContabilita"));
				setParameter(object, "setCodiceContabilita", Tributo.model().CODICE_CONTABILITA.getFieldType(),
					this.getObjectFromMap(map,"codiceContabilita"));
				setParameter(object, "setCodTributoIuv", Tributo.model().COD_TRIBUTO_IUV.getFieldType(),
					this.getObjectFromMap(map,"codTributoIuv"));
				return object;
			} else if(model.equals(Tributo.model().TIPO_TRIBUTO)){
		        TipoTributo object = new TipoTributo();
		        setParameter(object, "setId", Long.class,
		                this.getObjectFromMap(map,"tipoTributo.id"));
		        setParameter(object, "setCodTributo", Tributo.model().TIPO_TRIBUTO.COD_TRIBUTO.getFieldType(),
		                this.getObjectFromMap(map,"tipoTributo.codTributo"));
		        setParameter(object, "setDescrizione", Tributo.model().TIPO_TRIBUTO.DESCRIZIONE.getFieldType(),
		                this.getObjectFromMap(map,"tipoTributo.descrizione"));
		        setParameter(object, "setCodContabilita", Tributo.model().TIPO_TRIBUTO.COD_CONTABILITA.getFieldType(),
		                        this.getObjectFromMap(map,"tipoTributo.codContabilita"));
		        setParameter(object, "setCodTributoIuv", Tributo.model().TIPO_TRIBUTO.COD_TRIBUTO_IUV.getFieldType(),
		                        this.getObjectFromMap(map,"tipoTributo.codTributoIuv"));
		        setParameter(object, "setTipoContabilita", Tributo.model().TIPO_TRIBUTO.TIPO_CONTABILITA.getFieldType(),
		                        this.getObjectFromMap(map,"tipoTributo.tipoContabilita"));
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

			if(model.equals(Tributo.model())){
				return new org.openspcoop2.utils.jdbc.CustomKeyGeneratorObject("tributi","id","seq_tributi","tributi_init_seq");
			}
			
			else{
				throw new ServiceException("Model ["+model.toString()+"] not supported by getKeyGeneratorObject: "+this.getClass().getName());
			}

		}catch(Exception e){
			throw new ServiceException("Model ["+model.toString()+"] occurs error in getKeyGeneratorObject: "+e.getMessage(),e);
		}
		
	}

}
