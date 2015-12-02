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

import it.govpay.orm.Anagrafica;


/**     
 * AnagraficaFetch
 *
 * @author Giovanni Bussu (bussu@link.it)
 * @author Lorenzo Nardi (nardi@link.it)
 * @author $Author$
 * @version $Rev$, $Date$
 */
public class AnagraficaFetch extends AbstractJDBCFetch {

	@Override
	public Object fetch(TipiDatabase tipoDatabase, IModel<?> model , ResultSet rs) throws ServiceException {
		
		try{
			JDBCParameterUtilities jdbcParameterUtilities =  
					new JDBCParameterUtilities(tipoDatabase);

			if(model.equals(Anagrafica.model())){
				Anagrafica object = new Anagrafica();
				setParameter(object, "setId", Long.class,
					jdbcParameterUtilities.readParameter(rs, "id", Long.class));
				setParameter(object, "setRagioneSociale", Anagrafica.model().RAGIONE_SOCIALE.getFieldType(),
					jdbcParameterUtilities.readParameter(rs, "ragione_sociale", Anagrafica.model().RAGIONE_SOCIALE.getFieldType()));
				setParameter(object, "setCodUnivoco", Anagrafica.model().COD_UNIVOCO.getFieldType(),
					jdbcParameterUtilities.readParameter(rs, "cod_univoco", Anagrafica.model().COD_UNIVOCO.getFieldType()));
				setParameter(object, "setIndirizzo", Anagrafica.model().INDIRIZZO.getFieldType(),
					jdbcParameterUtilities.readParameter(rs, "indirizzo", Anagrafica.model().INDIRIZZO.getFieldType()));
				setParameter(object, "setCivico", Anagrafica.model().CIVICO.getFieldType(),
					jdbcParameterUtilities.readParameter(rs, "civico", Anagrafica.model().CIVICO.getFieldType()));
				setParameter(object, "setCap", Anagrafica.model().CAP.getFieldType(),
					jdbcParameterUtilities.readParameter(rs, "cap", Anagrafica.model().CAP.getFieldType()));
				setParameter(object, "setLocalita", Anagrafica.model().LOCALITA.getFieldType(),
					jdbcParameterUtilities.readParameter(rs, "localita", Anagrafica.model().LOCALITA.getFieldType()));
				setParameter(object, "setProvincia", Anagrafica.model().PROVINCIA.getFieldType(),
					jdbcParameterUtilities.readParameter(rs, "provincia", Anagrafica.model().PROVINCIA.getFieldType()));
				setParameter(object, "setNazione", Anagrafica.model().NAZIONE.getFieldType(),
					jdbcParameterUtilities.readParameter(rs, "nazione", Anagrafica.model().NAZIONE.getFieldType()));
				setParameter(object, "setEmail", Anagrafica.model().EMAIL.getFieldType(),
					jdbcParameterUtilities.readParameter(rs, "email", Anagrafica.model().EMAIL.getFieldType()));
				setParameter(object, "setTelefono", Anagrafica.model().TELEFONO.getFieldType(),
					jdbcParameterUtilities.readParameter(rs, "telefono", Anagrafica.model().TELEFONO.getFieldType()));
				setParameter(object, "setCellulare", Anagrafica.model().CELLULARE.getFieldType(),
					jdbcParameterUtilities.readParameter(rs, "cellulare", Anagrafica.model().CELLULARE.getFieldType()));
				setParameter(object, "setFax", Anagrafica.model().FAX.getFieldType(),
					jdbcParameterUtilities.readParameter(rs, "fax", Anagrafica.model().FAX.getFieldType()));
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

			if(model.equals(Anagrafica.model())){
				Anagrafica object = new Anagrafica();
				setParameter(object, "setId", Long.class,
					this.getObjectFromMap(map,"id"));
				setParameter(object, "setRagioneSociale", Anagrafica.model().RAGIONE_SOCIALE.getFieldType(),
					this.getObjectFromMap(map,"ragioneSociale"));
				setParameter(object, "setCodUnivoco", Anagrafica.model().COD_UNIVOCO.getFieldType(),
					this.getObjectFromMap(map,"codUnivoco"));
				setParameter(object, "setIndirizzo", Anagrafica.model().INDIRIZZO.getFieldType(),
					this.getObjectFromMap(map,"indirizzo"));
				setParameter(object, "setCivico", Anagrafica.model().CIVICO.getFieldType(),
					this.getObjectFromMap(map,"civico"));
				setParameter(object, "setCap", Anagrafica.model().CAP.getFieldType(),
					this.getObjectFromMap(map,"cap"));
				setParameter(object, "setLocalita", Anagrafica.model().LOCALITA.getFieldType(),
					this.getObjectFromMap(map,"localita"));
				setParameter(object, "setProvincia", Anagrafica.model().PROVINCIA.getFieldType(),
					this.getObjectFromMap(map,"provincia"));
				setParameter(object, "setNazione", Anagrafica.model().NAZIONE.getFieldType(),
					this.getObjectFromMap(map,"nazione"));
				setParameter(object, "setEmail", Anagrafica.model().EMAIL.getFieldType(),
					this.getObjectFromMap(map,"email"));
				setParameter(object, "setTelefono", Anagrafica.model().TELEFONO.getFieldType(),
					this.getObjectFromMap(map,"telefono"));
				setParameter(object, "setCellulare", Anagrafica.model().CELLULARE.getFieldType(),
					this.getObjectFromMap(map,"cellulare"));
				setParameter(object, "setFax", Anagrafica.model().FAX.getFieldType(),
					this.getObjectFromMap(map,"fax"));
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

			if(model.equals(Anagrafica.model())){
				return new org.openspcoop2.utils.jdbc.CustomKeyGeneratorObject("anagrafiche","id","seq_anagrafiche","anagrafiche_init_seq");
			}
			
			else{
				throw new ServiceException("Model ["+model.toString()+"] not supported by getKeyGeneratorObject: "+this.getClass().getName());
			}

		}catch(Exception e){
			throw new ServiceException("Model ["+model.toString()+"] occurs error in getKeyGeneratorObject: "+e.getMessage(),e);
		}
		
	}

}
