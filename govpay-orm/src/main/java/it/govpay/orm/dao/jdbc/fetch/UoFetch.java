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

import it.govpay.orm.Uo;


/**     
 * UoFetch
 *
 * @author Giovanni Bussu (bussu@link.it)
 * @author Lorenzo Nardi (nardi@link.it)
 * @author $Author$
 * @version $Rev$, $Date$
 */
public class UoFetch extends AbstractJDBCFetch {

	@Override
	public Object fetch(TipiDatabase tipoDatabase, IModel<?> model , ResultSet rs) throws ServiceException {
		
		try{
			JDBCParameterUtilities jdbcParameterUtilities =  
					new JDBCParameterUtilities(tipoDatabase);

			if(model.equals(Uo.model())){
				Uo object = new Uo();
				setParameter(object, "setId", Long.class,
					jdbcParameterUtilities.readParameter(rs, "id", Long.class));
				setParameter(object, "setCodUo", Uo.model().COD_UO.getFieldType(),
					jdbcParameterUtilities.readParameter(rs, "cod_uo", Uo.model().COD_UO.getFieldType()));
				setParameter(object, "setAbilitato", Uo.model().ABILITATO.getFieldType(),
					jdbcParameterUtilities.readParameter(rs, "abilitato", Uo.model().ABILITATO.getFieldType()));
				setParameter(object, "setUoCodiceIdentificativo", Uo.model().UO_CODICE_IDENTIFICATIVO.getFieldType(),
					jdbcParameterUtilities.readParameter(rs, "uo_codice_identificativo", Uo.model().UO_CODICE_IDENTIFICATIVO.getFieldType()));
				setParameter(object, "setUoDenominazione", Uo.model().UO_DENOMINAZIONE.getFieldType(),
					jdbcParameterUtilities.readParameter(rs, "uo_denominazione", Uo.model().UO_DENOMINAZIONE.getFieldType()));
				setParameter(object, "setUoIndirizzo", Uo.model().UO_INDIRIZZO.getFieldType(),
					jdbcParameterUtilities.readParameter(rs, "uo_indirizzo", Uo.model().UO_INDIRIZZO.getFieldType()));
				setParameter(object, "setUoCivico", Uo.model().UO_CIVICO.getFieldType(),
					jdbcParameterUtilities.readParameter(rs, "uo_civico", Uo.model().UO_CIVICO.getFieldType()));
				setParameter(object, "setUoCap", Uo.model().UO_CAP.getFieldType(),
					jdbcParameterUtilities.readParameter(rs, "uo_cap", Uo.model().UO_CAP.getFieldType()));
				setParameter(object, "setUoLocalita", Uo.model().UO_LOCALITA.getFieldType(),
					jdbcParameterUtilities.readParameter(rs, "uo_localita", Uo.model().UO_LOCALITA.getFieldType()));
				setParameter(object, "setUoProvincia", Uo.model().UO_PROVINCIA.getFieldType(),
					jdbcParameterUtilities.readParameter(rs, "uo_provincia", Uo.model().UO_PROVINCIA.getFieldType()));
				setParameter(object, "setUoNazione", Uo.model().UO_NAZIONE.getFieldType(),
					jdbcParameterUtilities.readParameter(rs, "uo_nazione", Uo.model().UO_NAZIONE.getFieldType()));
				setParameter(object, "setUoArea", Uo.model().UO_AREA.getFieldType(),
					jdbcParameterUtilities.readParameter(rs, "uo_area", Uo.model().UO_AREA.getFieldType()));
				setParameter(object, "setUoUrlSitoWeb", Uo.model().UO_URL_SITO_WEB.getFieldType(),
					jdbcParameterUtilities.readParameter(rs, "uo_url_sito_web", Uo.model().UO_URL_SITO_WEB.getFieldType()));
				setParameter(object, "setUoEmail", Uo.model().UO_EMAIL.getFieldType(),
					jdbcParameterUtilities.readParameter(rs, "uo_email", Uo.model().UO_EMAIL.getFieldType()));
				setParameter(object, "setUoPec", Uo.model().UO_PEC.getFieldType(),
					jdbcParameterUtilities.readParameter(rs, "uo_pec", Uo.model().UO_PEC.getFieldType()));
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

			if(model.equals(Uo.model())){
				Uo object = new Uo();
				setParameter(object, "setId", Long.class,
					this.getObjectFromMap(map,"id"));
				setParameter(object, "setCodUo", Uo.model().COD_UO.getFieldType(),
					this.getObjectFromMap(map,"codUo"));
				setParameter(object, "setAbilitato", Uo.model().ABILITATO.getFieldType(),
					this.getObjectFromMap(map,"abilitato"));
				setParameter(object, "setUoCodiceIdentificativo", Uo.model().UO_CODICE_IDENTIFICATIVO.getFieldType(),
					this.getObjectFromMap(map,"uoCodiceIdentificativo"));
				setParameter(object, "setUoDenominazione", Uo.model().UO_DENOMINAZIONE.getFieldType(),
					this.getObjectFromMap(map,"uoDenominazione"));
				setParameter(object, "setUoIndirizzo", Uo.model().UO_INDIRIZZO.getFieldType(),
					this.getObjectFromMap(map,"uoIndirizzo"));
				setParameter(object, "setUoCivico", Uo.model().UO_CIVICO.getFieldType(),
					this.getObjectFromMap(map,"uoCivico"));
				setParameter(object, "setUoCap", Uo.model().UO_CAP.getFieldType(),
					this.getObjectFromMap(map,"uoCap"));
				setParameter(object, "setUoLocalita", Uo.model().UO_LOCALITA.getFieldType(),
					this.getObjectFromMap(map,"uoLocalita"));
				setParameter(object, "setUoProvincia", Uo.model().UO_PROVINCIA.getFieldType(),
					this.getObjectFromMap(map,"uoProvincia"));
				setParameter(object, "setUoNazione", Uo.model().UO_NAZIONE.getFieldType(),
					this.getObjectFromMap(map,"uoNazione"));
				setParameter(object, "setUoArea", Uo.model().UO_AREA.getFieldType(),
					this.getObjectFromMap(map,"uoArea"));
				setParameter(object, "setUoUrlSitoWeb", Uo.model().UO_URL_SITO_WEB.getFieldType(),
					this.getObjectFromMap(map,"uoUrlSitoWeb"));
				setParameter(object, "setUoEmail", Uo.model().UO_EMAIL.getFieldType(),
					this.getObjectFromMap(map,"uoEmail"));
				setParameter(object, "setUoPec", Uo.model().UO_PEC.getFieldType(),
					this.getObjectFromMap(map,"uoPec"));
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

			if(model.equals(Uo.model())){
				return new org.openspcoop2.utils.jdbc.CustomKeyGeneratorObject("uo","id","seq_uo","uo_init_seq");
			}
			
			else{
				throw new ServiceException("Model ["+model.toString()+"] not supported by getKeyGeneratorObject: "+this.getClass().getName());
			}

		}catch(Exception e){
			throw new ServiceException("Model ["+model.toString()+"] occurs error in getKeyGeneratorObject: "+e.getMessage(),e);
		}
		
	}

}
