/*
 * GovPay - Porta di Accesso al Nodo dei Pagamenti SPC 
 * http://www.gov4j.it/govpay
 * 
 * Copyright (c) 2014-2016 Link.it srl (http://www.link.it).
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

import it.govpay.orm.Canale;


/**     
 * CanaleFetch
 *
 * @author Giovanni Bussu (bussu@link.it)
 * @author Lorenzo Nardi (nardi@link.it)
 * @author $Author$
 * @version $Rev$, $Date$
 */
public class CanaleFetch extends AbstractJDBCFetch {

	@Override
	public Object fetch(TipiDatabase tipoDatabase, IModel<?> model , ResultSet rs) throws ServiceException {
		
		try{
			JDBCParameterUtilities jdbcParameterUtilities =  
					new JDBCParameterUtilities(tipoDatabase);

			if(model.equals(Canale.model())){
				Canale object = new Canale();
				setParameter(object, "setId", Long.class,
					jdbcParameterUtilities.readParameter(rs, "id", Long.class));
				setParameter(object, "setCodCanale", Canale.model().COD_CANALE.getFieldType(),
					jdbcParameterUtilities.readParameter(rs, "cod_canale", Canale.model().COD_CANALE.getFieldType()));
				setParameter(object, "setCodIntermediario", Canale.model().COD_INTERMEDIARIO.getFieldType(),
					jdbcParameterUtilities.readParameter(rs, "cod_intermediario", Canale.model().COD_INTERMEDIARIO.getFieldType()));
				setParameter(object, "setTipoVersamento", Canale.model().TIPO_VERSAMENTO.getFieldType(),
					jdbcParameterUtilities.readParameter(rs, "tipo_versamento", Canale.model().TIPO_VERSAMENTO.getFieldType()));
				setParameter(object, "setModelloPagamento", Canale.model().MODELLO_PAGAMENTO.getFieldType(),
					jdbcParameterUtilities.readParameter(rs, "modello_pagamento", Canale.model().MODELLO_PAGAMENTO.getFieldType()));
				setParameter(object, "setDisponibilita", Canale.model().DISPONIBILITA.getFieldType(),
					jdbcParameterUtilities.readParameter(rs, "disponibilita", Canale.model().DISPONIBILITA.getFieldType()));
				setParameter(object, "setDescrizione", Canale.model().DESCRIZIONE.getFieldType(),
					jdbcParameterUtilities.readParameter(rs, "descrizione", Canale.model().DESCRIZIONE.getFieldType()));
				setParameter(object, "setCondizioni", Canale.model().CONDIZIONI.getFieldType(),
					jdbcParameterUtilities.readParameter(rs, "condizioni", Canale.model().CONDIZIONI.getFieldType()));
				setParameter(object, "setUrlInfo", Canale.model().URL_INFO.getFieldType(),
					jdbcParameterUtilities.readParameter(rs, "url_info", Canale.model().URL_INFO.getFieldType()));
				setParameter(object, "setAbilitato", Canale.model().ABILITATO.getFieldType(),
					jdbcParameterUtilities.readParameter(rs, "abilitato", Canale.model().ABILITATO.getFieldType()));
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

			if(model.equals(Canale.model())){
				Canale object = new Canale();
				setParameter(object, "setId", Long.class,
					this.getObjectFromMap(map,"id"));
				setParameter(object, "setCodCanale", Canale.model().COD_CANALE.getFieldType(),
					this.getObjectFromMap(map,"codCanale"));
				setParameter(object, "setCodIntermediario", Canale.model().COD_INTERMEDIARIO.getFieldType(),
					this.getObjectFromMap(map,"codIntermediario"));
				setParameter(object, "setTipoVersamento", Canale.model().TIPO_VERSAMENTO.getFieldType(),
					this.getObjectFromMap(map,"tipoVersamento"));
				setParameter(object, "setModelloPagamento", Canale.model().MODELLO_PAGAMENTO.getFieldType(),
					this.getObjectFromMap(map,"modelloPagamento"));
				setParameter(object, "setDisponibilita", Canale.model().DISPONIBILITA.getFieldType(),
					this.getObjectFromMap(map,"disponibilita"));
				setParameter(object, "setDescrizione", Canale.model().DESCRIZIONE.getFieldType(),
					this.getObjectFromMap(map,"descrizione"));
				setParameter(object, "setCondizioni", Canale.model().CONDIZIONI.getFieldType(),
					this.getObjectFromMap(map,"condizioni"));
				setParameter(object, "setUrlInfo", Canale.model().URL_INFO.getFieldType(),
					this.getObjectFromMap(map,"urlInfo"));
				setParameter(object, "setAbilitato", Canale.model().ABILITATO.getFieldType(),
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

			if(model.equals(Canale.model())){
				return new org.openspcoop2.utils.jdbc.CustomKeyGeneratorObject("canali","id","seq_canali","canali_init_seq");
			}
			
			else{
				throw new ServiceException("Model ["+model.toString()+"] not supported by getKeyGeneratorObject: "+this.getClass().getName());
			}

		}catch(Exception e){
			throw new ServiceException("Model ["+model.toString()+"] occurs error in getKeyGeneratorObject: "+e.getMessage(),e);
		}
		
	}

}
