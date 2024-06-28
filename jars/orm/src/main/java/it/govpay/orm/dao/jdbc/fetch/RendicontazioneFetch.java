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

import it.govpay.orm.Rendicontazione;


/**     
 * RendicontazioneFetch
 *
 * @author Giovanni Bussu (bussu@link.it)
 * @author Lorenzo Nardi (nardi@link.it)
 * @author $Author$
 * @version $Rev$, $Date$
 */
public class RendicontazioneFetch extends AbstractJDBCFetch {

	@Override
	public Object fetch(TipiDatabase tipoDatabase, IModel<?> model , ResultSet rs) throws ServiceException {
		
		try{
			GenericJDBCParameterUtilities GenericJDBCParameterUtilities =  
					new GenericJDBCParameterUtilities(tipoDatabase);

			if(model.equals(Rendicontazione.model())){
				Rendicontazione object = new Rendicontazione();
				this.setParameter(object, "setId", Long.class,
					GenericJDBCParameterUtilities.readParameter(rs, "id", Long.class));
				this.setParameter(object, "setIuv", Rendicontazione.model().IUV.getFieldType(),
					GenericJDBCParameterUtilities.readParameter(rs, "iuv", Rendicontazione.model().IUV.getFieldType()));
				this.setParameter(object, "setIur", Rendicontazione.model().IUR.getFieldType(),
					GenericJDBCParameterUtilities.readParameter(rs, "iur", Rendicontazione.model().IUR.getFieldType()));
				this.setParameter(object, "setIndiceDati", Rendicontazione.model().INDICE_DATI.getFieldType(),
					GenericJDBCParameterUtilities.readParameter(rs, "indice_dati", Rendicontazione.model().INDICE_DATI.getFieldType(), org.openspcoop2.utils.jdbc.JDBCDefaultForXSDType.FORCE_ZERO_AS_NULL));
				this.setParameter(object, "setImportoPagato", Rendicontazione.model().IMPORTO_PAGATO.getFieldType(),
					GenericJDBCParameterUtilities.readParameter(rs, "importo_pagato", Rendicontazione.model().IMPORTO_PAGATO.getFieldType()));
				this.setParameter(object, "setEsito", Rendicontazione.model().ESITO.getFieldType(),
					GenericJDBCParameterUtilities.readParameter(rs, "esito", Rendicontazione.model().ESITO.getFieldType(), org.openspcoop2.utils.jdbc.JDBCDefaultForXSDType.FORCE_ZERO_AS_NULL));
				this.setParameter(object, "setData", Rendicontazione.model().DATA.getFieldType(),
					GenericJDBCParameterUtilities.readParameter(rs, "data", Rendicontazione.model().DATA.getFieldType()));
				this.setParameter(object, "setStato", Rendicontazione.model().STATO.getFieldType(),
					GenericJDBCParameterUtilities.readParameter(rs, "stato", Rendicontazione.model().STATO.getFieldType()));
				this.setParameter(object, "setAnomalie", Rendicontazione.model().ANOMALIE.getFieldType(),
					GenericJDBCParameterUtilities.readParameter(rs, "anomalie", Rendicontazione.model().ANOMALIE.getFieldType()));
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

			if(model.equals(Rendicontazione.model())){
				Rendicontazione object = new Rendicontazione();
				this.setParameter(object, "setId", Long.class,
					this.getObjectFromMap(map,"id"));
				this.setParameter(object, "setIuv", Rendicontazione.model().IUV.getFieldType(),
					this.getObjectFromMap(map,"iuv"));
				this.setParameter(object, "setIur", Rendicontazione.model().IUR.getFieldType(),
					this.getObjectFromMap(map,"iur"));
				this.setParameter(object, "setIndiceDati", Rendicontazione.model().INDICE_DATI.getFieldType(),
					this.getObjectFromMap(map,"indiceDati"));
				this.setParameter(object, "setImportoPagato", Rendicontazione.model().IMPORTO_PAGATO.getFieldType(),
					this.getObjectFromMap(map,"importoPagato"));
				this.setParameter(object, "setEsito", Rendicontazione.model().ESITO.getFieldType(),
					this.getObjectFromMap(map,"esito"));
				this.setParameter(object, "setData", Rendicontazione.model().DATA.getFieldType(),
					this.getObjectFromMap(map,"data"));
				this.setParameter(object, "setStato", Rendicontazione.model().STATO.getFieldType(),
					this.getObjectFromMap(map,"stato"));
				this.setParameter(object, "setAnomalie", Rendicontazione.model().ANOMALIE.getFieldType(),
					this.getObjectFromMap(map,"anomalie"));
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

			if(model.equals(Rendicontazione.model())){
				return new org.openspcoop2.utils.jdbc.CustomKeyGeneratorObject("rendicontazioni","id","seq_rendicontazioni","rendicontazioni_init_seq");
			}
			
			else{
				throw new ServiceException("Model ["+model.toString()+"] not supported by getKeyGeneratorObject: "+this.getClass().getName());
			}

		}catch(Exception e){
			throw new ServiceException("Model ["+model.toString()+"] occurs error in getKeyGeneratorObject: "+e.getMessage(),e);
		}
		
	}

}
