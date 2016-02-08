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

import it.govpay.orm.SLA;


/**     
 * SLAFetch
 *
 * @author Giovanni Bussu (bussu@link.it)
 * @author Lorenzo Nardi (nardi@link.it)
 * @author $Author$
 * @version $Rev$, $Date$
 */
public class SLAFetch extends AbstractJDBCFetch {

	@Override
	public Object fetch(TipiDatabase tipoDatabase, IModel<?> model , ResultSet rs) throws ServiceException {
		
		try{
			JDBCParameterUtilities jdbcParameterUtilities =  
					new JDBCParameterUtilities(tipoDatabase);

			if(model.equals(SLA.model())){
				SLA object = new SLA();
				setParameter(object, "setId", Long.class,
					jdbcParameterUtilities.readParameter(rs, "id", Long.class));
				setParameter(object, "setDescrizione", SLA.model().DESCRIZIONE.getFieldType(),
					jdbcParameterUtilities.readParameter(rs, "descrizione", SLA.model().DESCRIZIONE.getFieldType()));
				setParameter(object, "setTipoEventoIniziale", SLA.model().TIPO_EVENTO_INIZIALE.getFieldType(),
					jdbcParameterUtilities.readParameter(rs, "tipo_evento_iniziale", SLA.model().TIPO_EVENTO_INIZIALE.getFieldType()));
				setParameter(object, "setSottotipoEventoIniziale", SLA.model().SOTTOTIPO_EVENTO_INIZIALE.getFieldType(),
					jdbcParameterUtilities.readParameter(rs, "sottotipo_evento_iniziale", SLA.model().SOTTOTIPO_EVENTO_INIZIALE.getFieldType()));
				setParameter(object, "setTipoEventoFinale", SLA.model().TIPO_EVENTO_FINALE.getFieldType(),
					jdbcParameterUtilities.readParameter(rs, "tipo_evento_finale", SLA.model().TIPO_EVENTO_FINALE.getFieldType()));
				setParameter(object, "setSottotipoEventoFinale", SLA.model().SOTTOTIPO_EVENTO_FINALE.getFieldType(),
					jdbcParameterUtilities.readParameter(rs, "sottotipo_evento_finale", SLA.model().SOTTOTIPO_EVENTO_FINALE.getFieldType()));
				setParameter(object, "setTempoA", SLA.model().TEMPO_A.getFieldType(),
					jdbcParameterUtilities.readParameter(rs, "tempo_a", SLA.model().TEMPO_A.getFieldType()));
				setParameter(object, "setTempoB", SLA.model().TEMPO_B.getFieldType(),
					jdbcParameterUtilities.readParameter(rs, "tempo_b", SLA.model().TEMPO_B.getFieldType()));
				setParameter(object, "setTolleranzaA", SLA.model().TOLLERANZA_A.getFieldType(),
					jdbcParameterUtilities.readParameter(rs, "tolleranza_a", SLA.model().TOLLERANZA_A.getFieldType()));
				setParameter(object, "setTolleranzaB", SLA.model().TOLLERANZA_B.getFieldType(),
					jdbcParameterUtilities.readParameter(rs, "tolleranza_b", SLA.model().TOLLERANZA_B.getFieldType()));
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

			if(model.equals(SLA.model())){
				SLA object = new SLA();
				setParameter(object, "setId", Long.class,
					this.getObjectFromMap(map,"id"));
				setParameter(object, "setDescrizione", SLA.model().DESCRIZIONE.getFieldType(),
					this.getObjectFromMap(map,"descrizione"));
				setParameter(object, "setTipoEventoIniziale", SLA.model().TIPO_EVENTO_INIZIALE.getFieldType(),
					this.getObjectFromMap(map,"tipoEventoIniziale"));
				setParameter(object, "setSottotipoEventoIniziale", SLA.model().SOTTOTIPO_EVENTO_INIZIALE.getFieldType(),
					this.getObjectFromMap(map,"sottotipoEventoIniziale"));
				setParameter(object, "setTipoEventoFinale", SLA.model().TIPO_EVENTO_FINALE.getFieldType(),
					this.getObjectFromMap(map,"tipoEventoFinale"));
				setParameter(object, "setSottotipoEventoFinale", SLA.model().SOTTOTIPO_EVENTO_FINALE.getFieldType(),
					this.getObjectFromMap(map,"sottotipoEventoFinale"));
				setParameter(object, "setTempoA", SLA.model().TEMPO_A.getFieldType(),
					this.getObjectFromMap(map,"tempoA"));
				setParameter(object, "setTempoB", SLA.model().TEMPO_B.getFieldType(),
					this.getObjectFromMap(map,"tempoB"));
				setParameter(object, "setTolleranzaA", SLA.model().TOLLERANZA_A.getFieldType(),
					this.getObjectFromMap(map,"tolleranzaA"));
				setParameter(object, "setTolleranzaB", SLA.model().TOLLERANZA_B.getFieldType(),
					this.getObjectFromMap(map,"tolleranzaB"));
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

			if(model.equals(SLA.model())){
				return new org.openspcoop2.utils.jdbc.CustomKeyGeneratorObject("sla","id","seq_sla","sla_init_seq");
			}
			
			else{
				throw new ServiceException("Model ["+model.toString()+"] not supported by getKeyGeneratorObject: "+this.getClass().getName());
			}

		}catch(Exception e){
			throw new ServiceException("Model ["+model.toString()+"] occurs error in getKeyGeneratorObject: "+e.getMessage(),e);
		}
		
	}

}
