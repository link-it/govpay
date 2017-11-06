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

import it.govpay.orm.Operazione;


/**     
 * OperazioneFetch
 *
 * @author Giovanni Bussu (bussu@link.it)
 * @author Lorenzo Nardi (nardi@link.it)
 * @author $Author$
 * @version $Rev$, $Date$
 */
public class OperazioneFetch extends AbstractJDBCFetch {

	@Override
	public Object fetch(TipiDatabase tipoDatabase, IModel<?> model , ResultSet rs) throws ServiceException {
		
		try{
			JDBCParameterUtilities jdbcParameterUtilities =  
					new JDBCParameterUtilities(tipoDatabase);

			if(model.equals(Operazione.model())){
				Operazione object = new Operazione();
				setParameter(object, "setId", Long.class,
					jdbcParameterUtilities.readParameter(rs, "id", Long.class));
				setParameter(object, "set_value_tipoOperazione", String.class,
					jdbcParameterUtilities.readParameter(rs, "tipo_operazione", Operazione.model().TIPO_OPERAZIONE.getFieldType())+"");
				setParameter(object, "setLineaElaborazione", Operazione.model().LINEA_ELABORAZIONE.getFieldType(),
					jdbcParameterUtilities.readParameter(rs, "linea_elaborazione", Operazione.model().LINEA_ELABORAZIONE.getFieldType()));
				setParameter(object, "set_value_stato", String.class,
					jdbcParameterUtilities.readParameter(rs, "stato", Operazione.model().STATO.getFieldType())+"");
				setParameter(object, "setDatiRichiesta", Operazione.model().DATI_RICHIESTA.getFieldType(),
					jdbcParameterUtilities.readParameter(rs, "dati_richiesta", Operazione.model().DATI_RICHIESTA.getFieldType()));
				setParameter(object, "setDatiRisposta", Operazione.model().DATI_RISPOSTA.getFieldType(),
					jdbcParameterUtilities.readParameter(rs, "dati_risposta", Operazione.model().DATI_RISPOSTA.getFieldType()));
				setParameter(object, "setDettaglioEsito", Operazione.model().DETTAGLIO_ESITO.getFieldType(),
					jdbcParameterUtilities.readParameter(rs, "dettaglio_esito", Operazione.model().DETTAGLIO_ESITO.getFieldType()));
				setParameter(object, "setCodVersamentoEnte", Operazione.model().COD_VERSAMENTO_ENTE.getFieldType(),
					jdbcParameterUtilities.readParameter(rs, "cod_versamento_ente", Operazione.model().COD_VERSAMENTO_ENTE.getFieldType()));
				setParameter(object, "setCodDominio", Operazione.model().COD_DOMINIO.getFieldType(),
					jdbcParameterUtilities.readParameter(rs, "cod_dominio", Operazione.model().COD_DOMINIO.getFieldType()));
				setParameter(object, "setIuv", Operazione.model().IUV.getFieldType(),
					jdbcParameterUtilities.readParameter(rs, "iuv", Operazione.model().IUV.getFieldType()));
				setParameter(object, "setTrn", Operazione.model().TRN.getFieldType(),
					jdbcParameterUtilities.readParameter(rs, "trn", Operazione.model().TRN.getFieldType()));
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

			if(model.equals(Operazione.model())){
				Operazione object = new Operazione();
				setParameter(object, "setId", Long.class,
					this.getObjectFromMap(map,"id"));
				setParameter(object, "set_value_tipoOperazione", String.class,
					this.getObjectFromMap(map,"tipoOperazione"));
				setParameter(object, "setLineaElaborazione", Operazione.model().LINEA_ELABORAZIONE.getFieldType(),
					this.getObjectFromMap(map,"lineaElaborazione"));
				setParameter(object, "set_value_stato", String.class,
					this.getObjectFromMap(map,"stato"));
				setParameter(object, "setDatiRichiesta", Operazione.model().DATI_RICHIESTA.getFieldType(),
					this.getObjectFromMap(map,"datiRichiesta"));
				setParameter(object, "setDatiRisposta", Operazione.model().DATI_RISPOSTA.getFieldType(),
					this.getObjectFromMap(map,"datiRisposta"));
				setParameter(object, "setDettaglioEsito", Operazione.model().DETTAGLIO_ESITO.getFieldType(),
					this.getObjectFromMap(map,"dettaglioEsito"));
				setParameter(object, "setCodVersamentoEnte", Operazione.model().COD_VERSAMENTO_ENTE.getFieldType(),
					this.getObjectFromMap(map,"codVersamentoEnte"));
				setParameter(object, "setCodDominio", Operazione.model().COD_DOMINIO.getFieldType(),
					this.getObjectFromMap(map,"codDominio"));
				setParameter(object, "setIuv", Operazione.model().IUV.getFieldType(),
					this.getObjectFromMap(map,"iuv"));
				setParameter(object, "setTrn", Operazione.model().TRN.getFieldType(),
					this.getObjectFromMap(map,"trn"));
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

			if(model.equals(Operazione.model())){
				return new org.openspcoop2.utils.jdbc.CustomKeyGeneratorObject("operazioni","id","seq_operazioni","operazioni_init_seq");
			}
			
			else{
				throw new ServiceException("Model ["+model.toString()+"] not supported by getKeyGeneratorObject: "+this.getClass().getName());
			}

		}catch(Exception e){
			throw new ServiceException("Model ["+model.toString()+"] occurs error in getKeyGeneratorObject: "+e.getMessage(),e);
		}
		
	}

}
