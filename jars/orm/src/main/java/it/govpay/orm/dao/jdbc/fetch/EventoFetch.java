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

import it.govpay.orm.Evento;


/**     
 * EventoFetch
 *
 * @author Giovanni Bussu (bussu@link.it)
 * @author Lorenzo Nardi (nardi@link.it)
 * @author $Author$
 * @version $Rev$, $Date$
 */
public class EventoFetch extends AbstractJDBCFetch {

	@Override
	public Object fetch(TipiDatabase tipoDatabase, IModel<?> model , ResultSet rs) throws ServiceException {
		
		try{
			JDBCParameterUtilities jdbcParameterUtilities =  
					new JDBCParameterUtilities(tipoDatabase);

			if(model.equals(Evento.model())){
				Evento object = new Evento();
				this.setParameter(object, "setId", Long.class,
					jdbcParameterUtilities.readParameter(rs, "id", Long.class));
				this.setParameter(object, "setCodDominio", Evento.model().COD_DOMINIO.getFieldType(),
					jdbcParameterUtilities.readParameter(rs, "cod_dominio", Evento.model().COD_DOMINIO.getFieldType()));
				this.setParameter(object, "setIuv", Evento.model().IUV.getFieldType(),
					jdbcParameterUtilities.readParameter(rs, "iuv", Evento.model().IUV.getFieldType()));
				this.setParameter(object, "setCcp", Evento.model().CCP.getFieldType(),
					jdbcParameterUtilities.readParameter(rs, "ccp", Evento.model().CCP.getFieldType()));
				this.setParameter(object, "setCategoriaEvento", Evento.model().CATEGORIA_EVENTO.getFieldType(),
					jdbcParameterUtilities.readParameter(rs, "categoria_evento", Evento.model().CATEGORIA_EVENTO.getFieldType()));
				this.setParameter(object, "setTipoEvento", Evento.model().TIPO_EVENTO.getFieldType(),
					jdbcParameterUtilities.readParameter(rs, "tipo_evento", Evento.model().TIPO_EVENTO.getFieldType()));
				this.setParameter(object, "setSottotipoEvento", Evento.model().SOTTOTIPO_EVENTO.getFieldType(),
					jdbcParameterUtilities.readParameter(rs, "sottotipo_evento", Evento.model().SOTTOTIPO_EVENTO.getFieldType()));
				this.setParameter(object, "setData", Evento.model().DATA.getFieldType(),
					jdbcParameterUtilities.readParameter(rs, "data", Evento.model().DATA.getFieldType()));
				this.setParameter(object, "setIntervallo", Evento.model().INTERVALLO.getFieldType(),
					jdbcParameterUtilities.readParameter(rs, "intervallo", Evento.model().INTERVALLO.getFieldType()));
				this.setParameter(object, "setClassnameDettaglio", Evento.model().CLASSNAME_DETTAGLIO.getFieldType(),
					jdbcParameterUtilities.readParameter(rs, "classname_dettaglio", Evento.model().CLASSNAME_DETTAGLIO.getFieldType()));
				this.setParameter(object, "setDettaglio", Evento.model().DETTAGLIO.getFieldType(),
					jdbcParameterUtilities.readParameter(rs, "dettaglio", Evento.model().DETTAGLIO.getFieldType()));
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

			if(model.equals(Evento.model())){
				Evento object = new Evento();
				this.setParameter(object, "setId", Long.class,
					this.getObjectFromMap(map,"id"));
				this.setParameter(object, "setCodDominio", Evento.model().COD_DOMINIO.getFieldType(),
					this.getObjectFromMap(map,"codDominio"));
				this.setParameter(object, "setIuv", Evento.model().IUV.getFieldType(),
					this.getObjectFromMap(map,"iuv"));
				this.setParameter(object, "setCcp", Evento.model().CCP.getFieldType(),
					this.getObjectFromMap(map,"ccp"));
				this.setParameter(object, "setCategoriaEvento", Evento.model().CATEGORIA_EVENTO.getFieldType(),
					this.getObjectFromMap(map,"categoriaEvento"));
				this.setParameter(object, "setTipoEvento", Evento.model().TIPO_EVENTO.getFieldType(),
					this.getObjectFromMap(map,"tipoEvento"));
				this.setParameter(object, "setSottotipoEvento", Evento.model().SOTTOTIPO_EVENTO.getFieldType(),
					this.getObjectFromMap(map,"sottotipoEvento"));
				this.setParameter(object, "setData", Evento.model().DATA.getFieldType(),
					this.getObjectFromMap(map,"data"));
				this.setParameter(object, "setIntervallo", Evento.model().INTERVALLO.getFieldType(),
					this.getObjectFromMap(map,"intervallo"));
				this.setParameter(object, "setClassnameDettaglio", Evento.model().CLASSNAME_DETTAGLIO.getFieldType(),
					this.getObjectFromMap(map,"classnameDettaglio"));
				this.setParameter(object, "setDettaglio", Evento.model().DETTAGLIO.getFieldType(),
					this.getObjectFromMap(map,"dettaglio"));
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

			if(model.equals(Evento.model())){
				return new org.openspcoop2.utils.jdbc.CustomKeyGeneratorObject("eventi","id","seq_eventi","eventi_init_seq");
			}
			
			else{
				throw new ServiceException("Model ["+model.toString()+"] not supported by getKeyGeneratorObject: "+this.getClass().getName());
			}

		}catch(Exception e){
			throw new ServiceException("Model ["+model.toString()+"] occurs error in getKeyGeneratorObject: "+e.getMessage(),e);
		}
		
	}

}
