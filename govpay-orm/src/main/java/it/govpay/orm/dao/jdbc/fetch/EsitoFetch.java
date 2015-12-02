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

import it.govpay.orm.Esito;


/**     
 * EsitoFetch
 *
 * @author Giovanni Bussu (bussu@link.it)
 * @author Lorenzo Nardi (nardi@link.it)
 * @author $Author$
 * @version $Rev$, $Date$
 */
public class EsitoFetch extends AbstractJDBCFetch {

	@Override
	public Object fetch(TipiDatabase tipoDatabase, IModel<?> model , ResultSet rs) throws ServiceException {
		
		try{
			JDBCParameterUtilities jdbcParameterUtilities =  
					new JDBCParameterUtilities(tipoDatabase);

			if(model.equals(Esito.model())){
				Esito object = new Esito();
				setParameter(object, "setId", Long.class,
					jdbcParameterUtilities.readParameter(rs, "id", Long.class));
				setParameter(object, "setCodDominio", Esito.model().COD_DOMINIO.getFieldType(),
					jdbcParameterUtilities.readParameter(rs, "cod_dominio", Esito.model().COD_DOMINIO.getFieldType()));
				setParameter(object, "setIuv", Esito.model().IUV.getFieldType(),
					jdbcParameterUtilities.readParameter(rs, "iuv", Esito.model().IUV.getFieldType()));
				setParameter(object, "setStatoSpedizione", Esito.model().STATO_SPEDIZIONE.getFieldType(),
					jdbcParameterUtilities.readParameter(rs, "stato_spedizione", Esito.model().STATO_SPEDIZIONE.getFieldType()));
				setParameter(object, "setDettaglioSpedizione", Esito.model().DETTAGLIO_SPEDIZIONE.getFieldType(),
					jdbcParameterUtilities.readParameter(rs, "dettaglio_spedizione", Esito.model().DETTAGLIO_SPEDIZIONE.getFieldType()));
				setParameter(object, "setTentativiSpedizione", Esito.model().TENTATIVI_SPEDIZIONE.getFieldType(),
					jdbcParameterUtilities.readParameter(rs, "tentativi_spedizione", Esito.model().TENTATIVI_SPEDIZIONE.getFieldType()));
				setParameter(object, "setDataOraCreazione", Esito.model().DATA_ORA_CREAZIONE.getFieldType(),
					jdbcParameterUtilities.readParameter(rs, "data_ora_creazione", Esito.model().DATA_ORA_CREAZIONE.getFieldType()));
				setParameter(object, "setDataOraUltimaSpedizione", Esito.model().DATA_ORA_ULTIMA_SPEDIZIONE.getFieldType(),
					jdbcParameterUtilities.readParameter(rs, "data_ora_ultima_spedizione", Esito.model().DATA_ORA_ULTIMA_SPEDIZIONE.getFieldType()));
				setParameter(object, "setDataOraProssimaSpedizione", Esito.model().DATA_ORA_PROSSIMA_SPEDIZIONE.getFieldType(),
					jdbcParameterUtilities.readParameter(rs, "data_ora_prossima_spedizione", Esito.model().DATA_ORA_PROSSIMA_SPEDIZIONE.getFieldType()));
				setParameter(object, "setXml", Esito.model().XML.getFieldType(),
					jdbcParameterUtilities.readParameter(rs, "xml", Esito.model().XML.getFieldType()));
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

			if(model.equals(Esito.model())){
				Esito object = new Esito();
				setParameter(object, "setId", Long.class,
					this.getObjectFromMap(map,"id"));
				setParameter(object, "setCodDominio", Esito.model().COD_DOMINIO.getFieldType(),
					this.getObjectFromMap(map,"codDominio"));
				setParameter(object, "setIuv", Esito.model().IUV.getFieldType(),
					this.getObjectFromMap(map,"iuv"));
				setParameter(object, "setStatoSpedizione", Esito.model().STATO_SPEDIZIONE.getFieldType(),
					this.getObjectFromMap(map,"statoSpedizione"));
				setParameter(object, "setDettaglioSpedizione", Esito.model().DETTAGLIO_SPEDIZIONE.getFieldType(),
					this.getObjectFromMap(map,"dettaglioSpedizione"));
				setParameter(object, "setTentativiSpedizione", Esito.model().TENTATIVI_SPEDIZIONE.getFieldType(),
					this.getObjectFromMap(map,"tentativiSpedizione"));
				setParameter(object, "setDataOraCreazione", Esito.model().DATA_ORA_CREAZIONE.getFieldType(),
					this.getObjectFromMap(map,"dataOraCreazione"));
				setParameter(object, "setDataOraUltimaSpedizione", Esito.model().DATA_ORA_ULTIMA_SPEDIZIONE.getFieldType(),
					this.getObjectFromMap(map,"dataOraUltimaSpedizione"));
				setParameter(object, "setDataOraProssimaSpedizione", Esito.model().DATA_ORA_PROSSIMA_SPEDIZIONE.getFieldType(),
					this.getObjectFromMap(map,"dataOraProssimaSpedizione"));
				setParameter(object, "setXml", Esito.model().XML.getFieldType(),
					this.getObjectFromMap(map,"xml"));
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

			if(model.equals(Esito.model())){
				return new org.openspcoop2.utils.jdbc.CustomKeyGeneratorObject("esiti","id","seq_esiti","esiti_init_seq");
			}
			
			else{
				throw new ServiceException("Model ["+model.toString()+"] not supported by getKeyGeneratorObject: "+this.getClass().getName());
			}

		}catch(Exception e){
			throw new ServiceException("Model ["+model.toString()+"] occurs error in getKeyGeneratorObject: "+e.getMessage(),e);
		}
		
	}

}
