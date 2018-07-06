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

import it.govpay.orm.EsitoAvvisatura;


/**     
 * EsitoAvvisaturaFetch
 *
 * @author Giovanni Bussu (bussu@link.it)
 * @author Lorenzo Nardi (nardi@link.it)
 * @author $Author$
 * @version $Rev$, $Date$
 */
public class EsitoAvvisaturaFetch extends AbstractJDBCFetch {

	@Override
	public Object fetch(TipiDatabase tipoDatabase, IModel<?> model , ResultSet rs) throws ServiceException {
		
		try{
			JDBCParameterUtilities jdbcParameterUtilities =  
					new JDBCParameterUtilities(tipoDatabase);

			if(model.equals(EsitoAvvisatura.model())){
				EsitoAvvisatura object = new EsitoAvvisatura();
				setParameter(object, "setId", Long.class,
					jdbcParameterUtilities.readParameter(rs, "id", Long.class));
				setParameter(object, "setCodDominio", EsitoAvvisatura.model().COD_DOMINIO.getFieldType(),
					jdbcParameterUtilities.readParameter(rs, "cod_dominio", EsitoAvvisatura.model().COD_DOMINIO.getFieldType()));
				setParameter(object, "setIdentificativoAvvisatura", EsitoAvvisatura.model().IDENTIFICATIVO_AVVISATURA.getFieldType(),
					jdbcParameterUtilities.readParameter(rs, "identificativo_avvisatura", EsitoAvvisatura.model().IDENTIFICATIVO_AVVISATURA.getFieldType()));
				setParameter(object, "setTipoCanale", EsitoAvvisatura.model().TIPO_CANALE.getFieldType(),
					jdbcParameterUtilities.readParameter(rs, "tipo_canale", EsitoAvvisatura.model().TIPO_CANALE.getFieldType()));
				setParameter(object, "setCodCanale", EsitoAvvisatura.model().COD_CANALE.getFieldType(),
					jdbcParameterUtilities.readParameter(rs, "cod_canale", EsitoAvvisatura.model().COD_CANALE.getFieldType()));
				setParameter(object, "setData", EsitoAvvisatura.model().DATA.getFieldType(),
					jdbcParameterUtilities.readParameter(rs, "data", EsitoAvvisatura.model().DATA.getFieldType()));
				setParameter(object, "setCodEsito", EsitoAvvisatura.model().COD_ESITO.getFieldType(),
					jdbcParameterUtilities.readParameter(rs, "cod_esito", EsitoAvvisatura.model().COD_ESITO.getFieldType()));
				setParameter(object, "setDescrizioneEsito", EsitoAvvisatura.model().DESCRIZIONE_ESITO.getFieldType(),
					jdbcParameterUtilities.readParameter(rs, "descrizione_esito", EsitoAvvisatura.model().DESCRIZIONE_ESITO.getFieldType()));
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

			if(model.equals(EsitoAvvisatura.model())){
				EsitoAvvisatura object = new EsitoAvvisatura();
				setParameter(object, "setId", Long.class,
					this.getObjectFromMap(map,"id"));
				setParameter(object, "setCodDominio", EsitoAvvisatura.model().COD_DOMINIO.getFieldType(),
					this.getObjectFromMap(map,"codDominio"));
				setParameter(object, "setIdentificativoAvvisatura", EsitoAvvisatura.model().IDENTIFICATIVO_AVVISATURA.getFieldType(),
					this.getObjectFromMap(map,"identificativoAvvisatura"));
				setParameter(object, "setTipoCanale", EsitoAvvisatura.model().TIPO_CANALE.getFieldType(),
					this.getObjectFromMap(map,"tipoCanale"));
				setParameter(object, "setCodCanale", EsitoAvvisatura.model().COD_CANALE.getFieldType(),
					this.getObjectFromMap(map,"codCanale"));
				setParameter(object, "setData", EsitoAvvisatura.model().DATA.getFieldType(),
					this.getObjectFromMap(map,"data"));
				setParameter(object, "setCodEsito", EsitoAvvisatura.model().COD_ESITO.getFieldType(),
					this.getObjectFromMap(map,"codEsito"));
				setParameter(object, "setDescrizioneEsito", EsitoAvvisatura.model().DESCRIZIONE_ESITO.getFieldType(),
					this.getObjectFromMap(map,"descrizioneEsito"));
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

			if(model.equals(EsitoAvvisatura.model())){
				return new org.openspcoop2.utils.jdbc.CustomKeyGeneratorObject("esiti_avvisatura","id","seq_esiti_avvisatura","esiti_avvisatura_init_seq");
			}
			
			else{
				throw new ServiceException("Model ["+model.toString()+"] not supported by getKeyGeneratorObject: "+this.getClass().getName());
			}

		}catch(Exception e){
			throw new ServiceException("Model ["+model.toString()+"] occurs error in getKeyGeneratorObject: "+e.getMessage(),e);
		}
		
	}

}
