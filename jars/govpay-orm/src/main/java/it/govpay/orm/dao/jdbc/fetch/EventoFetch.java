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

import it.govpay.orm.Evento;


/**     
 * EventoFetch
 *
 * @author Giovanni Bussu (bussu@link.it)
 * @author Lorenzo Nardi (nardi@link.it)
 * @author $Author$
 * @version $Rev$, $Date$
 */
public class EventoFetch extends AbstractJDBCFetch {

	@Override
	public Object fetch(TipiDatabase tipoDatabase, IModel<?> model , ResultSet rs) throws ServiceException {
		
		try{
			JDBCParameterUtilities jdbcParameterUtilities =  
					new JDBCParameterUtilities(tipoDatabase);

			if(model.equals(Evento.model())){
				Evento object = new Evento();
				this.setParameter(object, "setId", Long.class,
					jdbcParameterUtilities.readParameter(rs, "id", Long.class));
				this.setParameter(object, "setCodDominio", Evento.model().COD_DOMINIO.getFieldType(),
					jdbcParameterUtilities.readParameter(rs, "cod_dominio", Evento.model().COD_DOMINIO.getFieldType()));
				this.setParameter(object, "setIuv", Evento.model().IUV.getFieldType(),
					jdbcParameterUtilities.readParameter(rs, "iuv", Evento.model().IUV.getFieldType()));
				this.setParameter(object, "setCcp", Evento.model().CCP.getFieldType(),
					jdbcParameterUtilities.readParameter(rs, "ccp", Evento.model().CCP.getFieldType()));
				this.setParameter(object, "setCodPsp", Evento.model().COD_PSP.getFieldType(),
					jdbcParameterUtilities.readParameter(rs, "cod_psp", Evento.model().COD_PSP.getFieldType()));
				this.setParameter(object, "setTipoVersamento", Evento.model().TIPO_VERSAMENTO.getFieldType(),
					jdbcParameterUtilities.readParameter(rs, "tipo_versamento", Evento.model().TIPO_VERSAMENTO.getFieldType()));
				this.setParameter(object, "setComponente", Evento.model().COMPONENTE.getFieldType(),
					jdbcParameterUtilities.readParameter(rs, "componente", Evento.model().COMPONENTE.getFieldType()));
				this.setParameter(object, "setCategoriaEvento", Evento.model().CATEGORIA_EVENTO.getFieldType(),
					jdbcParameterUtilities.readParameter(rs, "categoria_evento", Evento.model().CATEGORIA_EVENTO.getFieldType()));
				this.setParameter(object, "setTipoEvento", Evento.model().TIPO_EVENTO.getFieldType(),
					jdbcParameterUtilities.readParameter(rs, "tipo_evento", Evento.model().TIPO_EVENTO.getFieldType()));
				this.setParameter(object, "setSottotipoEvento", Evento.model().SOTTOTIPO_EVENTO.getFieldType(),
					jdbcParameterUtilities.readParameter(rs, "sottotipo_evento", Evento.model().SOTTOTIPO_EVENTO.getFieldType()));
				this.setParameter(object, "setErogatore", Evento.model().EROGATORE.getFieldType(),
					jdbcParameterUtilities.readParameter(rs, "erogatore", Evento.model().EROGATORE.getFieldType()));
				this.setParameter(object, "setFruitore", Evento.model().FRUITORE.getFieldType(),
					jdbcParameterUtilities.readParameter(rs, "fruitore", Evento.model().FRUITORE.getFieldType()));
				this.setParameter(object, "setCodStazione", Evento.model().COD_STAZIONE.getFieldType(),
					jdbcParameterUtilities.readParameter(rs, "cod_stazione", Evento.model().COD_STAZIONE.getFieldType()));
				this.setParameter(object, "setCodCanale", Evento.model().COD_CANALE.getFieldType(),
					jdbcParameterUtilities.readParameter(rs, "cod_canale", Evento.model().COD_CANALE.getFieldType()));
				this.setParameter(object, "setParametri1", Evento.model().PARAMETRI_1.getFieldType(),
					jdbcParameterUtilities.readParameter(rs, "parametri_1", Evento.model().PARAMETRI_1.getFieldType()));
				this.setParameter(object, "setParametri2", Evento.model().PARAMETRI_2.getFieldType(),
					jdbcParameterUtilities.readParameter(rs, "parametri_2", Evento.model().PARAMETRI_2.getFieldType()));
				this.setParameter(object, "setEsito", Evento.model().ESITO.getFieldType(),
					jdbcParameterUtilities.readParameter(rs, "esito", Evento.model().ESITO.getFieldType()));
				this.setParameter(object, "setData1", Evento.model().DATA_1.getFieldType(),
					jdbcParameterUtilities.readParameter(rs, "data_1", Evento.model().DATA_1.getFieldType()));
				this.setParameter(object, "setData2", Evento.model().DATA_2.getFieldType(),
					jdbcParameterUtilities.readParameter(rs, "data_2", Evento.model().DATA_2.getFieldType()));
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

			if(model.equals(Evento.model())){
				Evento object = new Evento();
				this.setParameter(object, "setId", Long.class,
					this.getObjectFromMap(map,"id"));
				this.setParameter(object, "setCodDominio", Evento.model().COD_DOMINIO.getFieldType(),
					this.getObjectFromMap(map,"codDominio"));
				this.setParameter(object, "setIuv", Evento.model().IUV.getFieldType(),
					this.getObjectFromMap(map,"iuv"));
				this.setParameter(object, "setCcp", Evento.model().CCP.getFieldType(),
					this.getObjectFromMap(map,"ccp"));
				this.setParameter(object, "setCodPsp", Evento.model().COD_PSP.getFieldType(),
					this.getObjectFromMap(map,"codPsp"));
				this.setParameter(object, "setTipoVersamento", Evento.model().TIPO_VERSAMENTO.getFieldType(),
					this.getObjectFromMap(map,"tipoVersamento"));
				this.setParameter(object, "setComponente", Evento.model().COMPONENTE.getFieldType(),
					this.getObjectFromMap(map,"componente"));
				this.setParameter(object, "setCategoriaEvento", Evento.model().CATEGORIA_EVENTO.getFieldType(),
					this.getObjectFromMap(map,"categoriaEvento"));
				this.setParameter(object, "setTipoEvento", Evento.model().TIPO_EVENTO.getFieldType(),
					this.getObjectFromMap(map,"tipoEvento"));
				this.setParameter(object, "setSottotipoEvento", Evento.model().SOTTOTIPO_EVENTO.getFieldType(),
					this.getObjectFromMap(map,"sottotipoEvento"));
				this.setParameter(object, "setErogatore", Evento.model().EROGATORE.getFieldType(),
					this.getObjectFromMap(map,"erogatore"));
				this.setParameter(object, "setFruitore", Evento.model().FRUITORE.getFieldType(),
					this.getObjectFromMap(map,"fruitore"));
				this.setParameter(object, "setCodStazione", Evento.model().COD_STAZIONE.getFieldType(),
					this.getObjectFromMap(map,"codStazione"));
				this.setParameter(object, "setCodCanale", Evento.model().COD_CANALE.getFieldType(),
					this.getObjectFromMap(map,"codCanale"));
				this.setParameter(object, "setParametri1", Evento.model().PARAMETRI_1.getFieldType(),
					this.getObjectFromMap(map,"parametri1"));
				this.setParameter(object, "setParametri2", Evento.model().PARAMETRI_2.getFieldType(),
					this.getObjectFromMap(map,"parametri2"));
				this.setParameter(object, "setEsito", Evento.model().ESITO.getFieldType(),
					this.getObjectFromMap(map,"esito"));
				this.setParameter(object, "setData1", Evento.model().DATA_1.getFieldType(),
					this.getObjectFromMap(map,"data1"));
				this.setParameter(object, "setData2", Evento.model().DATA_2.getFieldType(),
					this.getObjectFromMap(map,"data2"));
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

			if(model.equals(Evento.model())){
				return new org.openspcoop2.utils.jdbc.CustomKeyGeneratorObject("eventi","id","seq_eventi","eventi_init_seq");
			}
			
			else{
				throw new ServiceException("Model ["+model.toString()+"] not supported by getKeyGeneratorObject: "+this.getClass().getName());
			}

		}catch(Exception e){
			throw new ServiceException("Model ["+model.toString()+"] occurs error in getKeyGeneratorObject: "+e.getMessage(),e);
		}
		
	}

}
