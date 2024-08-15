/*
 * GovPay - Porta di Accesso al Nodo dei Pagamenti SPC 
 * http://www.gov4j.it/govpay
 * 
 * Copyright (c) 2014-2024 Link.it srl (http://www.link.it).
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
			GenericJDBCParameterUtilities jdbcParameterUtilities =  
					new GenericJDBCParameterUtilities(tipoDatabase);

			if(model.equals(Utenza.model())){
				Utenza object = new Utenza();
				setParameter(object, "setId", Long.class,
					jdbcParameterUtilities.readParameter(rs, "id", Long.class));
				setParameter(object, "setPrincipal", Utenza.model().PRINCIPAL.getFieldType(),
					jdbcParameterUtilities.readParameter(rs, "principal", Utenza.model().PRINCIPAL.getFieldType()));
				setParameter(object, "setPrincipalOriginale", Utenza.model().PRINCIPAL_ORIGINALE.getFieldType(),
					jdbcParameterUtilities.readParameter(rs, "principal_originale", Utenza.model().PRINCIPAL_ORIGINALE.getFieldType()));
				setParameter(object, "setAbilitato", Utenza.model().ABILITATO.getFieldType(),
					jdbcParameterUtilities.readParameter(rs, "abilitato", Utenza.model().ABILITATO.getFieldType()));
				setParameter(object, "setAutorizzazioneDominiStar", Utenza.model().AUTORIZZAZIONE_DOMINI_STAR.getFieldType(),
					jdbcParameterUtilities.readParameter(rs, "autorizzazione_domini_star", Utenza.model().AUTORIZZAZIONE_DOMINI_STAR.getFieldType()));
				setParameter(object, "setAutorizzazioneTipiVersStar", Utenza.model().AUTORIZZAZIONE_TIPI_VERS_STAR.getFieldType(),
					jdbcParameterUtilities.readParameter(rs, "autorizzazione_tipi_vers_star", Utenza.model().AUTORIZZAZIONE_TIPI_VERS_STAR.getFieldType()));
				setParameter(object, "setRuoli", Utenza.model().RUOLI.getFieldType(),
					jdbcParameterUtilities.readParameter(rs, "ruoli", Utenza.model().RUOLI.getFieldType()));
				setParameter(object, "setPassword", Utenza.model().PASSWORD.getFieldType(),
					jdbcParameterUtilities.readParameter(rs, "password", Utenza.model().PASSWORD.getFieldType()));
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
				setParameter(object, "setId", Long.class,
					this.getObjectFromMap(map,"id"));
				setParameter(object, "setPrincipal", Utenza.model().PRINCIPAL.getFieldType(),
					this.getObjectFromMap(map,"principal"));
				setParameter(object, "setPrincipalOriginale", Utenza.model().PRINCIPAL_ORIGINALE.getFieldType(),
					this.getObjectFromMap(map,"principalOriginale"));
				setParameter(object, "setAbilitato", Utenza.model().ABILITATO.getFieldType(),
					this.getObjectFromMap(map,"abilitato"));
				setParameter(object, "setAutorizzazioneDominiStar", Utenza.model().AUTORIZZAZIONE_DOMINI_STAR.getFieldType(),
					this.getObjectFromMap(map,"autorizzazione_domini_star"));
				setParameter(object, "setAutorizzazioneTipiVersStar", Utenza.model().AUTORIZZAZIONE_TIPI_VERS_STAR.getFieldType(),
					this.getObjectFromMap(map,"autorizzazione_tipi_vers_star"));
				setParameter(object, "setRuoli", Utenza.model().RUOLI.getFieldType(),
					this.getObjectFromMap(map,"ruoli"));
				setParameter(object, "setPassword", Utenza.model().PASSWORD.getFieldType(),
					this.getObjectFromMap(map,"password"));
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
