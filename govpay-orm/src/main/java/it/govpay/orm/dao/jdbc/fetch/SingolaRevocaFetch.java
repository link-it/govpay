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

import it.govpay.orm.SingolaRevoca;


/**     
 * SingolaRevocaFetch
 *
 * @author Giovanni Bussu (bussu@link.it)
 * @author Lorenzo Nardi (nardi@link.it)
 * @author $Author$
 * @version $Rev$, $Date$
 */
public class SingolaRevocaFetch extends AbstractJDBCFetch {

	@Override
	public Object fetch(TipiDatabase tipoDatabase, IModel<?> model , ResultSet rs) throws ServiceException {
		
		try{
			JDBCParameterUtilities jdbcParameterUtilities =  
					new JDBCParameterUtilities(tipoDatabase);

			if(model.equals(SingolaRevoca.model())){
				SingolaRevoca object = new SingolaRevoca();
				setParameter(object, "setId", Long.class,
					jdbcParameterUtilities.readParameter(rs, "id", Long.class));
				setParameter(object, "setIdER", SingolaRevoca.model().ID_ER.getFieldType(),
					jdbcParameterUtilities.readParameter(rs, "id_er", SingolaRevoca.model().ID_ER.getFieldType()));
				setParameter(object, "setCausaleRevoca", SingolaRevoca.model().CAUSALE_REVOCA.getFieldType(),
					jdbcParameterUtilities.readParameter(rs, "causale_revoca", SingolaRevoca.model().CAUSALE_REVOCA.getFieldType()));
				setParameter(object, "setDatiAggiuntiviRevoca", SingolaRevoca.model().DATI_AGGIUNTIVI_REVOCA.getFieldType(),
					jdbcParameterUtilities.readParameter(rs, "dati_aggiuntivi_revoca", SingolaRevoca.model().DATI_AGGIUNTIVI_REVOCA.getFieldType()));
				setParameter(object, "setSingoloImporto", SingolaRevoca.model().SINGOLO_IMPORTO.getFieldType(),
					jdbcParameterUtilities.readParameter(rs, "singolo_importo", SingolaRevoca.model().SINGOLO_IMPORTO.getFieldType()));
				setParameter(object, "setSingoloImportoRevocato", SingolaRevoca.model().SINGOLO_IMPORTO_REVOCATO.getFieldType(),
					jdbcParameterUtilities.readParameter(rs, "singolo_importo_revocato", SingolaRevoca.model().SINGOLO_IMPORTO_REVOCATO.getFieldType()));
				setParameter(object, "setCausaleEsito", SingolaRevoca.model().CAUSALE_ESITO.getFieldType(),
					jdbcParameterUtilities.readParameter(rs, "causale_esito", SingolaRevoca.model().CAUSALE_ESITO.getFieldType()));
				setParameter(object, "setDatiAggiuntiviEsito", SingolaRevoca.model().DATI_AGGIUNTIVI_ESITO.getFieldType(),
					jdbcParameterUtilities.readParameter(rs, "dati_aggiuntivi_esito", SingolaRevoca.model().DATI_AGGIUNTIVI_ESITO.getFieldType()));
				setParameter(object, "setStato", SingolaRevoca.model().STATO.getFieldType(),
					jdbcParameterUtilities.readParameter(rs, "stato", SingolaRevoca.model().STATO.getFieldType()));
				setParameter(object, "setDescrizioneStato", SingolaRevoca.model().DESCRIZIONE_STATO.getFieldType(),
					jdbcParameterUtilities.readParameter(rs, "descrizione_stato", SingolaRevoca.model().DESCRIZIONE_STATO.getFieldType()));
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

			if(model.equals(SingolaRevoca.model())){
				SingolaRevoca object = new SingolaRevoca();
				setParameter(object, "setId", Long.class,
					this.getObjectFromMap(map,"id"));
				setParameter(object, "setIdER", SingolaRevoca.model().ID_ER.getFieldType(),
					this.getObjectFromMap(map,"idER"));
				setParameter(object, "setCausaleRevoca", SingolaRevoca.model().CAUSALE_REVOCA.getFieldType(),
					this.getObjectFromMap(map,"causaleRevoca"));
				setParameter(object, "setDatiAggiuntiviRevoca", SingolaRevoca.model().DATI_AGGIUNTIVI_REVOCA.getFieldType(),
					this.getObjectFromMap(map,"datiAggiuntiviRevoca"));
				setParameter(object, "setSingoloImporto", SingolaRevoca.model().SINGOLO_IMPORTO.getFieldType(),
					this.getObjectFromMap(map,"singoloImporto"));
				setParameter(object, "setSingoloImportoRevocato", SingolaRevoca.model().SINGOLO_IMPORTO_REVOCATO.getFieldType(),
					this.getObjectFromMap(map,"singoloImportoRevocato"));
				setParameter(object, "setCausaleEsito", SingolaRevoca.model().CAUSALE_ESITO.getFieldType(),
					this.getObjectFromMap(map,"causaleEsito"));
				setParameter(object, "setDatiAggiuntiviEsito", SingolaRevoca.model().DATI_AGGIUNTIVI_ESITO.getFieldType(),
					this.getObjectFromMap(map,"datiAggiuntiviEsito"));
				setParameter(object, "setStato", SingolaRevoca.model().STATO.getFieldType(),
					this.getObjectFromMap(map,"stato"));
				setParameter(object, "setDescrizioneStato", SingolaRevoca.model().DESCRIZIONE_STATO.getFieldType(),
					this.getObjectFromMap(map,"descrizioneStato"));
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

			if(model.equals(SingolaRevoca.model())){
				return new org.openspcoop2.utils.jdbc.CustomKeyGeneratorObject("singole_revoche","id","seq_singole_revoche","singole_revoche_init_seq");
			}
			
			else{
				throw new ServiceException("Model ["+model.toString()+"] not supported by getKeyGeneratorObject: "+this.getClass().getName());
			}

		}catch(Exception e){
			throw new ServiceException("Model ["+model.toString()+"] occurs error in getKeyGeneratorObject: "+e.getMessage(),e);
		}
		
	}

}
