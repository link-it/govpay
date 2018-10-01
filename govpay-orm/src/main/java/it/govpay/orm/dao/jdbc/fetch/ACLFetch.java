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

import it.govpay.orm.ACL;


/**     
 * ACLFetch
 *
 * @author Giovanni Bussu (bussu@link.it)
 * @author Lorenzo Nardi (nardi@link.it)
 * @author $Author$
 * @version $Rev$, $Date$
 */
public class ACLFetch extends AbstractJDBCFetch {

	@Override
	public Object fetch(TipiDatabase tipoDatabase, IModel<?> model , ResultSet rs) throws ServiceException {
		
		try{
			JDBCParameterUtilities jdbcParameterUtilities =  
					new JDBCParameterUtilities(tipoDatabase);

			if(model.equals(ACL.model())){
				ACL object = new ACL();
				this.setParameter(object, "setId", Long.class,
					jdbcParameterUtilities.readParameter(rs, "id", Long.class));
				this.setParameter(object, "setRuolo", ACL.model().RUOLO.getFieldType(),
					jdbcParameterUtilities.readParameter(rs, "ruolo", ACL.model().RUOLO.getFieldType()));
				this.setParameter(object, "setPrincipal", ACL.model().PRINCIPAL.getFieldType(),
					jdbcParameterUtilities.readParameter(rs, "principal", ACL.model().PRINCIPAL.getFieldType()));
				this.setParameter(object, "setServizio", ACL.model().SERVIZIO.getFieldType(),
					jdbcParameterUtilities.readParameter(rs, "servizio", ACL.model().SERVIZIO.getFieldType()));
				this.setParameter(object, "setDiritti", ACL.model().DIRITTI.getFieldType(),
					jdbcParameterUtilities.readParameter(rs, "diritti", ACL.model().DIRITTI.getFieldType()));
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

			if(model.equals(ACL.model())){
				ACL object = new ACL();
				this.setParameter(object, "setId", Long.class,
					this.getObjectFromMap(map,"id"));
				this.setParameter(object, "setRuolo", ACL.model().RUOLO.getFieldType(),
					this.getObjectFromMap(map,"ruolo"));
				this.setParameter(object, "setPrincipal", ACL.model().PRINCIPAL.getFieldType(),
					this.getObjectFromMap(map,"principal"));
				this.setParameter(object, "setServizio", ACL.model().SERVIZIO.getFieldType(),
					this.getObjectFromMap(map,"servizio"));
				this.setParameter(object, "setDiritti", ACL.model().DIRITTI.getFieldType(),
					this.getObjectFromMap(map,"diritti"));
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

			if(model.equals(ACL.model())){
				return new org.openspcoop2.utils.jdbc.CustomKeyGeneratorObject("acl","id","seq_acl","acl_init_seq");
			}
			
			else{
				throw new ServiceException("Model ["+model.toString()+"] not supported by getKeyGeneratorObject: "+this.getClass().getName());
			}

		}catch(Exception e){
			throw new ServiceException("Model ["+model.toString()+"] occurs error in getKeyGeneratorObject: "+e.getMessage(),e);
		}
		
	}

}
