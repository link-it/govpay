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
 * VistaEventiVersamentoFetch
 *
 * @author Giovanni Bussu (bussu@link.it)
 * @author Lorenzo Nardi (nardi@link.it)
 * @author $Author$
 * @version $Rev$, $Date$
 */
public class VistaEventiVersamentoFetch extends AbstractJDBCFetch {

	@Override
	public Object fetch(TipiDatabase tipoDatabase, IModel<?> model , ResultSet rs) throws ServiceException {
		
		try{
			JDBCParameterUtilities jdbcParameterUtilities =  
					new JDBCParameterUtilities(tipoDatabase);

			if(model.equals(Evento.model())){
				Evento object = new Evento();
				setParameter(object, "setId", Long.class,
					jdbcParameterUtilities.readParameter(rs, "id", Long.class));
				setParameter(object, "setComponente", Evento.model().COMPONENTE.getFieldType(),
					jdbcParameterUtilities.readParameter(rs, "componente", Evento.model().COMPONENTE.getFieldType()));
				setParameter(object, "setRuolo", Evento.model().RUOLO.getFieldType(),
					jdbcParameterUtilities.readParameter(rs, "ruolo", Evento.model().RUOLO.getFieldType()));
				setParameter(object, "setCategoriaEvento", Evento.model().CATEGORIA_EVENTO.getFieldType(),
					jdbcParameterUtilities.readParameter(rs, "categoria_evento", Evento.model().CATEGORIA_EVENTO.getFieldType()));
				setParameter(object, "setTipoEvento", Evento.model().TIPO_EVENTO.getFieldType(),
					jdbcParameterUtilities.readParameter(rs, "tipo_evento", Evento.model().TIPO_EVENTO.getFieldType()));
				setParameter(object, "setSottotipoEvento", Evento.model().SOTTOTIPO_EVENTO.getFieldType(),
					jdbcParameterUtilities.readParameter(rs, "sottotipo_evento", Evento.model().SOTTOTIPO_EVENTO.getFieldType()));
				setParameter(object, "setData", Evento.model().DATA.getFieldType(),
					jdbcParameterUtilities.readParameter(rs, "data", Evento.model().DATA.getFieldType()));
				setParameter(object, "setIntervallo", Evento.model().INTERVALLO.getFieldType(),
					jdbcParameterUtilities.readParameter(rs, "intervallo", Evento.model().INTERVALLO.getFieldType()));
				setParameter(object, "setEsito", Evento.model().ESITO.getFieldType(),
					jdbcParameterUtilities.readParameter(rs, "esito", Evento.model().ESITO.getFieldType()));
				setParameter(object, "setSottotipoEsito", Evento.model().SOTTOTIPO_ESITO.getFieldType(),
					jdbcParameterUtilities.readParameter(rs, "sottotipo_esito", Evento.model().SOTTOTIPO_ESITO.getFieldType()));
				setParameter(object, "setDettaglioEsito", Evento.model().DETTAGLIO_ESITO.getFieldType(),
					jdbcParameterUtilities.readParameter(rs, "dettaglio_esito", Evento.model().DETTAGLIO_ESITO.getFieldType()));
				setParameter(object, "setParametriRichiesta", Evento.model().PARAMETRI_RICHIESTA.getFieldType(),
					jdbcParameterUtilities.readParameter(rs, "parametri_richiesta", Evento.model().PARAMETRI_RICHIESTA.getFieldType()));
				setParameter(object, "setParametriRisposta", Evento.model().PARAMETRI_RISPOSTA.getFieldType(),
					jdbcParameterUtilities.readParameter(rs, "parametri_risposta", Evento.model().PARAMETRI_RISPOSTA.getFieldType()));
				setParameter(object, "setDatiPagoPA", Evento.model().DATI_PAGO_PA.getFieldType(),
					jdbcParameterUtilities.readParameter(rs, "dati_pago_pa", Evento.model().DATI_PAGO_PA.getFieldType()));
				setParameter(object, "setCodVersamentoEnte", Evento.model().COD_VERSAMENTO_ENTE.getFieldType(),
					jdbcParameterUtilities.readParameter(rs, "cod_versamento_ente", Evento.model().COD_VERSAMENTO_ENTE.getFieldType()));
				setParameter(object, "setCodApplicazione", Evento.model().COD_APPLICAZIONE.getFieldType(),
					jdbcParameterUtilities.readParameter(rs, "cod_applicazione", Evento.model().COD_APPLICAZIONE.getFieldType()));
				setParameter(object, "setIuv", Evento.model().IUV.getFieldType(),
					jdbcParameterUtilities.readParameter(rs, "iuv", Evento.model().IUV.getFieldType()));
				setParameter(object, "setCcp", Evento.model().CCP.getFieldType(),
					jdbcParameterUtilities.readParameter(rs, "ccp", Evento.model().CCP.getFieldType()));
				setParameter(object, "setCodDominio", Evento.model().COD_DOMINIO.getFieldType(),
					jdbcParameterUtilities.readParameter(rs, "cod_dominio", Evento.model().COD_DOMINIO.getFieldType()));
				setParameter(object, "setIdSessione", Evento.model().ID_SESSIONE.getFieldType(),
					jdbcParameterUtilities.readParameter(rs, "id_sessione", Evento.model().ID_SESSIONE.getFieldType()));
				setParameter(object, "setSeverita", Evento.model().SEVERITA.getFieldType(),
					jdbcParameterUtilities.readParameter(rs, "severita", Evento.model().SEVERITA.getFieldType()));
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
				setParameter(object, "setComponente", Evento.model().COMPONENTE.getFieldType(),
					this.getObjectFromMap(map,"componente"));
				setParameter(object, "setRuolo", Evento.model().RUOLO.getFieldType(),
					this.getObjectFromMap(map,"ruolo"));
				setParameter(object, "setCategoriaEvento", Evento.model().CATEGORIA_EVENTO.getFieldType(),
					this.getObjectFromMap(map,"categoriaEvento"));
				setParameter(object, "setTipoEvento", Evento.model().TIPO_EVENTO.getFieldType(),
					this.getObjectFromMap(map,"tipoEvento"));
				setParameter(object, "setSottotipoEvento", Evento.model().SOTTOTIPO_EVENTO.getFieldType(),
					this.getObjectFromMap(map,"sottotipoEvento"));
				setParameter(object, "setData", Evento.model().DATA.getFieldType(),
					this.getObjectFromMap(map,"data"));
				setParameter(object, "setIntervallo", Evento.model().INTERVALLO.getFieldType(),
					this.getObjectFromMap(map,"intervallo"));
				setParameter(object, "setEsito", Evento.model().ESITO.getFieldType(),
					this.getObjectFromMap(map,"esito"));
				setParameter(object, "setSottotipoEsito", Evento.model().SOTTOTIPO_ESITO.getFieldType(),
					this.getObjectFromMap(map,"sottotipoEsito"));
				setParameter(object, "setDettaglioEsito", Evento.model().DETTAGLIO_ESITO.getFieldType(),
					this.getObjectFromMap(map,"dettaglioEsito"));
				setParameter(object, "setParametriRichiesta", Evento.model().PARAMETRI_RICHIESTA.getFieldType(),
					this.getObjectFromMap(map,"parametriRichiesta"));
				setParameter(object, "setParametriRisposta", Evento.model().PARAMETRI_RISPOSTA.getFieldType(),
					this.getObjectFromMap(map,"parametriRisposta"));
				setParameter(object, "setDatiPagoPA", Evento.model().DATI_PAGO_PA.getFieldType(),
					this.getObjectFromMap(map,"datiPagoPA"));
				setParameter(object, "setCodVersamentoEnte", Evento.model().COD_VERSAMENTO_ENTE.getFieldType(),
					this.getObjectFromMap(map,"codVersamentoEnte"));
				setParameter(object, "setCodApplicazione", Evento.model().COD_APPLICAZIONE.getFieldType(),
					this.getObjectFromMap(map,"codApplicazione"));
				setParameter(object, "setIuv", Evento.model().IUV.getFieldType(),
					this.getObjectFromMap(map,"iuv"));
				setParameter(object, "setCcp", Evento.model().CCP.getFieldType(),
					this.getObjectFromMap(map,"ccp"));
				setParameter(object, "setCodDominio", Evento.model().COD_DOMINIO.getFieldType(),
					this.getObjectFromMap(map,"codDominio"));
				setParameter(object, "setIdSessione", Evento.model().ID_SESSIONE.getFieldType(),
					this.getObjectFromMap(map,"idSessione"));
				setParameter(object, "setSeverita", Evento.model().SEVERITA.getFieldType(),
					this.getObjectFromMap(map,"severita"));
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
