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
				setParameter(object, "setId", Long.class,
					jdbcParameterUtilities.readParameter(rs, "id", Long.class));
				setParameter(object, "setDataOraEvento", Evento.model().DATA_ORA_EVENTO.getFieldType(),
					jdbcParameterUtilities.readParameter(rs, "data_ora_evento", Evento.model().DATA_ORA_EVENTO.getFieldType()));
				setParameter(object, "setCodDominio", Evento.model().COD_DOMINIO.getFieldType(),
					jdbcParameterUtilities.readParameter(rs, "cod_dominio", Evento.model().COD_DOMINIO.getFieldType()));
				setParameter(object, "setIuv", Evento.model().IUV.getFieldType(),
					jdbcParameterUtilities.readParameter(rs, "iuv", Evento.model().IUV.getFieldType()));
				setParameter(object, "setIdApplicazione", Evento.model().ID_APPLICAZIONE.getFieldType(),
					jdbcParameterUtilities.readParameter(rs, "id_applicazione", Evento.model().ID_APPLICAZIONE.getFieldType()));
				setParameter(object, "setCcp", Evento.model().CCP.getFieldType(),
					jdbcParameterUtilities.readParameter(rs, "ccp", Evento.model().CCP.getFieldType()));
				setParameter(object, "setCodPsp", Evento.model().COD_PSP.getFieldType(),
					jdbcParameterUtilities.readParameter(rs, "cod_psp", Evento.model().COD_PSP.getFieldType()));
				setParameter(object, "setTipoVersamento", Evento.model().TIPO_VERSAMENTO.getFieldType(),
					jdbcParameterUtilities.readParameter(rs, "tipo_versamento", Evento.model().TIPO_VERSAMENTO.getFieldType()));
				setParameter(object, "setComponente", Evento.model().COMPONENTE.getFieldType(),
					jdbcParameterUtilities.readParameter(rs, "componente", Evento.model().COMPONENTE.getFieldType()));
				setParameter(object, "setCategoriaEvento", Evento.model().CATEGORIA_EVENTO.getFieldType(),
					jdbcParameterUtilities.readParameter(rs, "categoria_evento", Evento.model().CATEGORIA_EVENTO.getFieldType()));
				setParameter(object, "setTipoEvento", Evento.model().TIPO_EVENTO.getFieldType(),
					jdbcParameterUtilities.readParameter(rs, "tipo_evento", Evento.model().TIPO_EVENTO.getFieldType()));
				setParameter(object, "setSottotipoEvento", Evento.model().SOTTOTIPO_EVENTO.getFieldType(),
					jdbcParameterUtilities.readParameter(rs, "sottotipo_evento", Evento.model().SOTTOTIPO_EVENTO.getFieldType()));
				setParameter(object, "setCodFruitore", Evento.model().COD_FRUITORE.getFieldType(),
					jdbcParameterUtilities.readParameter(rs, "cod_fruitore", Evento.model().COD_FRUITORE.getFieldType()));
				setParameter(object, "setCodErogatore", Evento.model().COD_EROGATORE.getFieldType(),
					jdbcParameterUtilities.readParameter(rs, "cod_erogatore", Evento.model().COD_EROGATORE.getFieldType()));
				setParameter(object, "setCodStazione", Evento.model().COD_STAZIONE.getFieldType(),
					jdbcParameterUtilities.readParameter(rs, "cod_stazione", Evento.model().COD_STAZIONE.getFieldType()));
				setParameter(object, "setCanalePagamento", Evento.model().CANALE_PAGAMENTO.getFieldType(),
					jdbcParameterUtilities.readParameter(rs, "canale_pagamento", Evento.model().CANALE_PAGAMENTO.getFieldType()));
				setParameter(object, "setAltriParametri", Evento.model().ALTRI_PARAMETRI.getFieldType(),
					jdbcParameterUtilities.readParameter(rs, "altri_parametri", Evento.model().ALTRI_PARAMETRI.getFieldType()));
				setParameter(object, "setEsito", Evento.model().ESITO.getFieldType(),
					jdbcParameterUtilities.readParameter(rs, "esito", Evento.model().ESITO.getFieldType()));
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
				setParameter(object, "setId", Long.class,
					this.getObjectFromMap(map,"id"));
				setParameter(object, "setDataOraEvento", Evento.model().DATA_ORA_EVENTO.getFieldType(),
					this.getObjectFromMap(map,"dataOraEvento"));
				setParameter(object, "setCodDominio", Evento.model().COD_DOMINIO.getFieldType(),
					this.getObjectFromMap(map,"codDominio"));
				setParameter(object, "setIuv", Evento.model().IUV.getFieldType(),
					this.getObjectFromMap(map,"iuv"));
				setParameter(object, "setIdApplicazione", Evento.model().ID_APPLICAZIONE.getFieldType(),
					this.getObjectFromMap(map,"idApplicazione"));
				setParameter(object, "setCcp", Evento.model().CCP.getFieldType(),
					this.getObjectFromMap(map,"ccp"));
				setParameter(object, "setCodPsp", Evento.model().COD_PSP.getFieldType(),
					this.getObjectFromMap(map,"codPsp"));
				setParameter(object, "setTipoVersamento", Evento.model().TIPO_VERSAMENTO.getFieldType(),
					this.getObjectFromMap(map,"tipoVersamento"));
				setParameter(object, "setComponente", Evento.model().COMPONENTE.getFieldType(),
					this.getObjectFromMap(map,"componente"));
				setParameter(object, "setCategoriaEvento", Evento.model().CATEGORIA_EVENTO.getFieldType(),
					this.getObjectFromMap(map,"categoriaEvento"));
				setParameter(object, "setTipoEvento", Evento.model().TIPO_EVENTO.getFieldType(),
					this.getObjectFromMap(map,"tipoEvento"));
				setParameter(object, "setSottotipoEvento", Evento.model().SOTTOTIPO_EVENTO.getFieldType(),
					this.getObjectFromMap(map,"sottotipoEvento"));
				setParameter(object, "setCodFruitore", Evento.model().COD_FRUITORE.getFieldType(),
					this.getObjectFromMap(map,"codFruitore"));
				setParameter(object, "setCodErogatore", Evento.model().COD_EROGATORE.getFieldType(),
					this.getObjectFromMap(map,"codErogatore"));
				setParameter(object, "setCodStazione", Evento.model().COD_STAZIONE.getFieldType(),
					this.getObjectFromMap(map,"codStazione"));
				setParameter(object, "setCanalePagamento", Evento.model().CANALE_PAGAMENTO.getFieldType(),
					this.getObjectFromMap(map,"canalePagamento"));
				setParameter(object, "setAltriParametri", Evento.model().ALTRI_PARAMETRI.getFieldType(),
					this.getObjectFromMap(map,"altriParametri"));
				setParameter(object, "setEsito", Evento.model().ESITO.getFieldType(),
					this.getObjectFromMap(map,"esito"));
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
