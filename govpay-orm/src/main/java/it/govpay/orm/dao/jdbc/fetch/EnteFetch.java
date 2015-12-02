/*
 * GovPay - Porta di Accesso al Nodo dei Pagamenti SPC 
 * http://www.gov4j.it/govpay
 * 
 * Copyright (c) 2014-2015 Link.it srl (http://www.link.it).
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

import it.govpay.orm.Ente;


/**     
 * EnteFetch
 *
 * @author Giovanni Bussu (bussu@link.it)
 * @author Lorenzo Nardi (nardi@link.it)
 * @author $Author$
 * @version $Rev$, $Date$
 */
public class EnteFetch extends AbstractJDBCFetch {

	@Override
	public Object fetch(TipiDatabase tipoDatabase, IModel<?> model , ResultSet rs) throws ServiceException {
		
		try{
			JDBCParameterUtilities jdbcParameterUtilities =  
					new JDBCParameterUtilities(tipoDatabase);

			if(model.equals(Ente.model())){
				Ente object = new Ente();
				setParameter(object, "setId", Long.class,
					jdbcParameterUtilities.readParameter(rs, "id", Long.class));
				setParameter(object, "setCodEnte", Ente.model().COD_ENTE.getFieldType(),
					jdbcParameterUtilities.readParameter(rs, "cod_ente", Ente.model().COD_ENTE.getFieldType()));
				setParameter(object, "setAbilitato", Ente.model().ABILITATO.getFieldType(),
					jdbcParameterUtilities.readParameter(rs, "abilitato", Ente.model().ABILITATO.getFieldType()));
				setParameter(object, "setInvioMailRPTAbilitato", Ente.model().INVIO_MAIL_RPTABILITATO.getFieldType(),
					jdbcParameterUtilities.readParameter(rs, "invio_mail_rptabilitato", Ente.model().INVIO_MAIL_RPTABILITATO.getFieldType()));
				setParameter(object, "setInvioMailRTAbilitato", Ente.model().INVIO_MAIL_RTABILITATO.getFieldType(),
					jdbcParameterUtilities.readParameter(rs, "invio_mail_rtabilitato", Ente.model().INVIO_MAIL_RTABILITATO.getFieldType()));
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

			if(model.equals(Ente.model())){
				Ente object = new Ente();
				setParameter(object, "setId", Long.class,
					this.getObjectFromMap(map,"id"));
				setParameter(object, "setCodEnte", Ente.model().COD_ENTE.getFieldType(),
					this.getObjectFromMap(map,"codEnte"));
				setParameter(object, "setAbilitato", Ente.model().ABILITATO.getFieldType(),
					this.getObjectFromMap(map,"abilitato"));
				setParameter(object, "setInvioMailRPTAbilitato", Ente.model().INVIO_MAIL_RPTABILITATO.getFieldType(),
					this.getObjectFromMap(map,"invioMailRPTAbilitato"));
				setParameter(object, "setInvioMailRTAbilitato", Ente.model().INVIO_MAIL_RTABILITATO.getFieldType(),
					this.getObjectFromMap(map,"invioMailRTAbilitato"));
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

			if(model.equals(Ente.model())){
				return new org.openspcoop2.utils.jdbc.CustomKeyGeneratorObject("enti","id","seq_enti","enti_init_seq");
			}
			
			else{
				throw new ServiceException("Model ["+model.toString()+"] not supported by getKeyGeneratorObject: "+this.getClass().getName());
			}

		}catch(Exception e){
			throw new ServiceException("Model ["+model.toString()+"] occurs error in getKeyGeneratorObject: "+e.getMessage(),e);
		}
		
	}

}
