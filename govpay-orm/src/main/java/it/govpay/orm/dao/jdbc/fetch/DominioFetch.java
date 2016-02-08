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

import it.govpay.orm.Disponibilita;
import it.govpay.orm.Dominio;


/**     
 * DominioFetch
 *
 * @author Giovanni Bussu (bussu@link.it)
 * @author Lorenzo Nardi (nardi@link.it)
 * @author $Author$
 * @version $Rev$, $Date$
 */
public class DominioFetch extends AbstractJDBCFetch {

	@Override
	public Object fetch(TipiDatabase tipoDatabase, IModel<?> model , ResultSet rs) throws ServiceException {
		
		try{
			JDBCParameterUtilities jdbcParameterUtilities =  
					new JDBCParameterUtilities(tipoDatabase);

			if(model.equals(Dominio.model())){
				Dominio object = new Dominio();
				setParameter(object, "setId", Long.class,
					jdbcParameterUtilities.readParameter(rs, "id", Long.class));
				setParameter(object, "setCodDominio", Dominio.model().COD_DOMINIO.getFieldType(),
					jdbcParameterUtilities.readParameter(rs, "cod_dominio", Dominio.model().COD_DOMINIO.getFieldType()));
				setParameter(object, "setRagioneSociale", Dominio.model().RAGIONE_SOCIALE.getFieldType(),
					jdbcParameterUtilities.readParameter(rs, "ragione_sociale", Dominio.model().RAGIONE_SOCIALE.getFieldType()));
				setParameter(object, "setGln", Dominio.model().GLN.getFieldType(),
					jdbcParameterUtilities.readParameter(rs, "gln", Dominio.model().GLN.getFieldType()));
				setParameter(object, "setPluginClass", Dominio.model().PLUGIN_CLASS.getFieldType(),
					jdbcParameterUtilities.readParameter(rs, "plugin_class", Dominio.model().PLUGIN_CLASS.getFieldType()));
				setParameter(object, "setAbilitato", Dominio.model().ABILITATO.getFieldType(),
					jdbcParameterUtilities.readParameter(rs, "abilitato", Dominio.model().ABILITATO.getFieldType()));
				return object;
			}
			if(model.equals(Dominio.model().DISPONIBILITA)){
				Disponibilita object = new Disponibilita();
				setParameter(object, "setId", Long.class,
					jdbcParameterUtilities.readParameter(rs, "id", Long.class));
				setParameter(object, "setTipoPeriodo", Dominio.model().DISPONIBILITA.TIPO_PERIODO.getFieldType(),
					jdbcParameterUtilities.readParameter(rs, "tipo_periodo", Dominio.model().DISPONIBILITA.TIPO_PERIODO.getFieldType()));
				setParameter(object, "setGiorno", Dominio.model().DISPONIBILITA.GIORNO.getFieldType(),
					jdbcParameterUtilities.readParameter(rs, "giorno", Dominio.model().DISPONIBILITA.GIORNO.getFieldType()));
				setParameter(object, "setFasceOrarie", Dominio.model().DISPONIBILITA.FASCE_ORARIE.getFieldType(),
					jdbcParameterUtilities.readParameter(rs, "fasce_orarie", Dominio.model().DISPONIBILITA.FASCE_ORARIE.getFieldType()));
				setParameter(object, "setTipoDisponibilita", Dominio.model().DISPONIBILITA.TIPO_DISPONIBILITA.getFieldType(),
					jdbcParameterUtilities.readParameter(rs, "tipo_disponibilita", Dominio.model().DISPONIBILITA.TIPO_DISPONIBILITA.getFieldType()));
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

			if(model.equals(Dominio.model())){
				Dominio object = new Dominio();
				setParameter(object, "setId", Long.class,
					this.getObjectFromMap(map,"id"));
				setParameter(object, "setCodDominio", Dominio.model().COD_DOMINIO.getFieldType(),
					this.getObjectFromMap(map,"codDominio"));
				setParameter(object, "setRagioneSociale", Dominio.model().RAGIONE_SOCIALE.getFieldType(),
					this.getObjectFromMap(map,"ragioneSociale"));
				setParameter(object, "setGln", Dominio.model().GLN.getFieldType(),
					this.getObjectFromMap(map,"gln"));
				setParameter(object, "setPluginClass", Dominio.model().PLUGIN_CLASS.getFieldType(),
					this.getObjectFromMap(map,"pluginClass"));
				setParameter(object, "setAbilitato", Dominio.model().ABILITATO.getFieldType(),
					this.getObjectFromMap(map,"abilitato"));
				return object;
			}
			if(model.equals(Dominio.model().DISPONIBILITA)){
				Disponibilita object = new Disponibilita();
				setParameter(object, "setId", Long.class,
					this.getObjectFromMap(map,"Disponibilita.id"));
				setParameter(object, "setTipoPeriodo", Dominio.model().DISPONIBILITA.TIPO_PERIODO.getFieldType(),
					this.getObjectFromMap(map,"Disponibilita.tipoPeriodo"));
				setParameter(object, "setGiorno", Dominio.model().DISPONIBILITA.GIORNO.getFieldType(),
					this.getObjectFromMap(map,"Disponibilita.giorno"));
				setParameter(object, "setFasceOrarie", Dominio.model().DISPONIBILITA.FASCE_ORARIE.getFieldType(),
					this.getObjectFromMap(map,"Disponibilita.fasceOrarie"));
				setParameter(object, "setTipoDisponibilita", Dominio.model().DISPONIBILITA.TIPO_DISPONIBILITA.getFieldType(),
					this.getObjectFromMap(map,"Disponibilita.tipoDisponibilita"));
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

			if(model.equals(Dominio.model())){
				return new org.openspcoop2.utils.jdbc.CustomKeyGeneratorObject("domini","id","seq_domini","domini_init_seq");
			}
			if(model.equals(Dominio.model().DISPONIBILITA)){
				return new org.openspcoop2.utils.jdbc.CustomKeyGeneratorObject("disponibilita","id","seq_disponibilita","disponibilita_init_seq");
			}
			
			else{
				throw new ServiceException("Model ["+model.toString()+"] not supported by getKeyGeneratorObject: "+this.getClass().getName());
			}

		}catch(Exception e){
			throw new ServiceException("Model ["+model.toString()+"] occurs error in getKeyGeneratorObject: "+e.getMessage(),e);
		}
		
	}

}
