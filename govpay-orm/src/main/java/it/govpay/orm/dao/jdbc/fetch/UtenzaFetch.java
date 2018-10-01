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

import it.govpay.orm.Utenza;


/**     
 * UtenzaFetch
 *
 * @author Giovanni Bussu (bussu@link.it)
 * @author Lorenzo Nardi (nardi@link.it)
 * @author $Author$
 * @version $Rev$, $Date$
 */
public class UtenzaFetch extends AbstractJDBCFetch {

	@Override
	public Object fetch(TipiDatabase tipoDatabase, IModel<?> model , ResultSet rs) throws ServiceException {
		
		try{
			JDBCParameterUtilities jdbcParameterUtilities =  
					new JDBCParameterUtilities(tipoDatabase);

			if(model.equals(Utenza.model())){
				Utenza object = new Utenza();
				this.setParameter(object, "setId", Long.class,
					jdbcParameterUtilities.readParameter(rs, "id", Long.class));
				this.setParameter(object, "setPrincipal", Utenza.model().PRINCIPAL.getFieldType(),
					jdbcParameterUtilities.readParameter(rs, "principal", Utenza.model().PRINCIPAL.getFieldType()));
				this.setParameter(object, "setPrincipalOriginale", Utenza.model().PRINCIPAL_ORIGINALE.getFieldType(),
					jdbcParameterUtilities.readParameter(rs, "principal_originale", Utenza.model().PRINCIPAL_ORIGINALE.getFieldType()));
				this.setParameter(object, "setAbilitato", Utenza.model().ABILITATO.getFieldType(),
					jdbcParameterUtilities.readParameter(rs, "abilitato", Utenza.model().ABILITATO.getFieldType()));
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

			if(model.equals(Utenza.model())){
				Utenza object = new Utenza();
				this.setParameter(object, "setId", Long.class,
					this.getObjectFromMap(map,"id"));
				this.setParameter(object, "setPrincipal", Utenza.model().PRINCIPAL.getFieldType(),
					this.getObjectFromMap(map,"principal"));
				this.setParameter(object, "setPrincipalOriginale", Utenza.model().PRINCIPAL_ORIGINALE.getFieldType(),
					this.getObjectFromMap(map,"principalOriginale"));
				this.setParameter(object, "setAbilitato", Utenza.model().ABILITATO.getFieldType(),
					this.getObjectFromMap(map,"abilitato"));
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

			if(model.equals(Utenza.model())){
				return new org.openspcoop2.utils.jdbc.CustomKeyGeneratorObject("utenze","id","seq_utenze","utenze_init_seq");
			}
			
			else{
				throw new ServiceException("Model ["+model.toString()+"] not supported by getKeyGeneratorObject: "+this.getClass().getName());
			}

		}catch(Exception e){
			throw new ServiceException("Model ["+model.toString()+"] occurs error in getKeyGeneratorObject: "+e.getMessage(),e);
		}
		
	}

}
