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
				this.setParameter(object, "setId", Long.class,
					jdbcParameterUtilities.readParameter(rs, "id", Long.class));
				this.setParameter(object, "setCodUo", Uo.model().COD_UO.getFieldType(),
					jdbcParameterUtilities.readParameter(rs, "cod_uo", Uo.model().COD_UO.getFieldType()));
				this.setParameter(object, "setAbilitato", Uo.model().ABILITATO.getFieldType(),
					jdbcParameterUtilities.readParameter(rs, "abilitato", Uo.model().ABILITATO.getFieldType()));
				this.setParameter(object, "setUoCodiceIdentificativo", Uo.model().UO_CODICE_IDENTIFICATIVO.getFieldType(),
					jdbcParameterUtilities.readParameter(rs, "uo_codice_identificativo", Uo.model().UO_CODICE_IDENTIFICATIVO.getFieldType()));
				this.setParameter(object, "setUoDenominazione", Uo.model().UO_DENOMINAZIONE.getFieldType(),
					jdbcParameterUtilities.readParameter(rs, "uo_denominazione", Uo.model().UO_DENOMINAZIONE.getFieldType()));
				this.setParameter(object, "setUoIndirizzo", Uo.model().UO_INDIRIZZO.getFieldType(),
					jdbcParameterUtilities.readParameter(rs, "uo_indirizzo", Uo.model().UO_INDIRIZZO.getFieldType()));
				this.setParameter(object, "setUoCivico", Uo.model().UO_CIVICO.getFieldType(),
					jdbcParameterUtilities.readParameter(rs, "uo_civico", Uo.model().UO_CIVICO.getFieldType()));
				this.setParameter(object, "setUoCap", Uo.model().UO_CAP.getFieldType(),
					jdbcParameterUtilities.readParameter(rs, "uo_cap", Uo.model().UO_CAP.getFieldType()));
				this.setParameter(object, "setUoLocalita", Uo.model().UO_LOCALITA.getFieldType(),
					jdbcParameterUtilities.readParameter(rs, "uo_localita", Uo.model().UO_LOCALITA.getFieldType()));
				this.setParameter(object, "setUoProvincia", Uo.model().UO_PROVINCIA.getFieldType(),
					jdbcParameterUtilities.readParameter(rs, "uo_provincia", Uo.model().UO_PROVINCIA.getFieldType()));
				this.setParameter(object, "setUoNazione", Uo.model().UO_NAZIONE.getFieldType(),
					jdbcParameterUtilities.readParameter(rs, "uo_nazione", Uo.model().UO_NAZIONE.getFieldType()));
				this.setParameter(object, "setUoArea", Uo.model().UO_AREA.getFieldType(),
					jdbcParameterUtilities.readParameter(rs, "uo_area", Uo.model().UO_AREA.getFieldType()));
				this.setParameter(object, "setUoUrlSitoWeb", Uo.model().UO_URL_SITO_WEB.getFieldType(),
					jdbcParameterUtilities.readParameter(rs, "uo_url_sito_web", Uo.model().UO_URL_SITO_WEB.getFieldType()));
				this.setParameter(object, "setUoEmail", Uo.model().UO_EMAIL.getFieldType(),
					jdbcParameterUtilities.readParameter(rs, "uo_email", Uo.model().UO_EMAIL.getFieldType()));
				this.setParameter(object, "setUoPec", Uo.model().UO_PEC.getFieldType(),
					jdbcParameterUtilities.readParameter(rs, "uo_pec", Uo.model().UO_PEC.getFieldType()));
				this.setParameter(object, "setUoTel", Uo.model().UO_TEL.getFieldType(),
					jdbcParameterUtilities.readParameter(rs, "uo_tel", Uo.model().UO_TEL.getFieldType()));
				this.setParameter(object, "setUoFax", Uo.model().UO_FAX.getFieldType(),
					jdbcParameterUtilities.readParameter(rs, "uo_fax", Uo.model().UO_FAX.getFieldType()));
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
				this.setParameter(object, "setId", Long.class,
					this.getObjectFromMap(map,"id"));
				this.setParameter(object, "setCodUo", Uo.model().COD_UO.getFieldType(),
					this.getObjectFromMap(map,"codUo"));
				this.setParameter(object, "setAbilitato", Uo.model().ABILITATO.getFieldType(),
					this.getObjectFromMap(map,"abilitato"));
				this.setParameter(object, "setUoCodiceIdentificativo", Uo.model().UO_CODICE_IDENTIFICATIVO.getFieldType(),
					this.getObjectFromMap(map,"uoCodiceIdentificativo"));
				this.setParameter(object, "setUoDenominazione", Uo.model().UO_DENOMINAZIONE.getFieldType(),
					this.getObjectFromMap(map,"uoDenominazione"));
				this.setParameter(object, "setUoIndirizzo", Uo.model().UO_INDIRIZZO.getFieldType(),
					this.getObjectFromMap(map,"uoIndirizzo"));
				this.setParameter(object, "setUoCivico", Uo.model().UO_CIVICO.getFieldType(),
					this.getObjectFromMap(map,"uoCivico"));
				this.setParameter(object, "setUoCap", Uo.model().UO_CAP.getFieldType(),
					this.getObjectFromMap(map,"uoCap"));
				this.setParameter(object, "setUoLocalita", Uo.model().UO_LOCALITA.getFieldType(),
					this.getObjectFromMap(map,"uoLocalita"));
				this.setParameter(object, "setUoProvincia", Uo.model().UO_PROVINCIA.getFieldType(),
					this.getObjectFromMap(map,"uoProvincia"));
				this.setParameter(object, "setUoNazione", Uo.model().UO_NAZIONE.getFieldType(),
					this.getObjectFromMap(map,"uoNazione"));
				this.setParameter(object, "setUoArea", Uo.model().UO_AREA.getFieldType(),
					this.getObjectFromMap(map,"uoArea"));
				this.setParameter(object, "setUoUrlSitoWeb", Uo.model().UO_URL_SITO_WEB.getFieldType(),
					this.getObjectFromMap(map,"uoUrlSitoWeb"));
				this.setParameter(object, "setUoEmail", Uo.model().UO_EMAIL.getFieldType(),
					this.getObjectFromMap(map,"uoEmail"));
				this.setParameter(object, "setUoPec", Uo.model().UO_PEC.getFieldType(),
					this.getObjectFromMap(map,"uoPec"));
				this.setParameter(object, "setUoTel", Uo.model().UO_TEL.getFieldType(),
					this.getObjectFromMap(map,"uoTel"));
				this.setParameter(object, "setUoFax", Uo.model().UO_FAX.getFieldType(),
					this.getObjectFromMap(map,"uoFax"));
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
