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

import it.govpay.orm.NotificaAppIO;


/**     
 * NotificaAppIOFetch
 *
 * @author Giovanni Bussu (bussu@link.it)
 * @author Lorenzo Nardi (nardi@link.it)
 * @author $Author$
 * @version $Rev$, $Date$
 */
public class NotificaAppIOFetch extends AbstractJDBCFetch {

	@Override
	public Object fetch(TipiDatabase tipoDatabase, IModel<?> model , ResultSet rs) throws ServiceException {
		
		try{
			JDBCParameterUtilities jdbcParameterUtilities =  
					new JDBCParameterUtilities(tipoDatabase);

			if(model.equals(NotificaAppIO.model())){
				NotificaAppIO object = new NotificaAppIO();
				setParameter(object, "setId", Long.class,
					jdbcParameterUtilities.readParameter(rs, "id", Long.class));
				setParameter(object, "setDebitoreIdentificativo", NotificaAppIO.model().DEBITORE_IDENTIFICATIVO.getFieldType(),
					jdbcParameterUtilities.readParameter(rs, "debitore_identificativo", NotificaAppIO.model().DEBITORE_IDENTIFICATIVO.getFieldType()));
				setParameter(object, "setCodVersamentoEnte", NotificaAppIO.model().COD_VERSAMENTO_ENTE.getFieldType(),
					jdbcParameterUtilities.readParameter(rs, "cod_versamento_ente", NotificaAppIO.model().COD_VERSAMENTO_ENTE.getFieldType()));
				setParameter(object, "setCodApplicazione", NotificaAppIO.model().COD_APPLICAZIONE.getFieldType(),
					jdbcParameterUtilities.readParameter(rs, "cod_applicazione", NotificaAppIO.model().COD_APPLICAZIONE.getFieldType()));
				setParameter(object, "setCodDominio", NotificaAppIO.model().COD_DOMINIO.getFieldType(),
					jdbcParameterUtilities.readParameter(rs, "cod_dominio", NotificaAppIO.model().COD_DOMINIO.getFieldType()));
				setParameter(object, "setIuv", NotificaAppIO.model().IUV.getFieldType(),
					jdbcParameterUtilities.readParameter(rs, "iuv", NotificaAppIO.model().IUV.getFieldType()));
				setParameter(object, "setTipoEsito", NotificaAppIO.model().TIPO_ESITO.getFieldType(),
					jdbcParameterUtilities.readParameter(rs, "tipo_esito", NotificaAppIO.model().TIPO_ESITO.getFieldType()));
				setParameter(object, "setDataCreazione", NotificaAppIO.model().DATA_CREAZIONE.getFieldType(),
					jdbcParameterUtilities.readParameter(rs, "data_creazione", NotificaAppIO.model().DATA_CREAZIONE.getFieldType()));
				setParameter(object, "setStato", NotificaAppIO.model().STATO.getFieldType(),
					jdbcParameterUtilities.readParameter(rs, "stato", NotificaAppIO.model().STATO.getFieldType()));
				setParameter(object, "setDescrizioneStato", NotificaAppIO.model().DESCRIZIONE_STATO.getFieldType(),
					jdbcParameterUtilities.readParameter(rs, "descrizione_stato", NotificaAppIO.model().DESCRIZIONE_STATO.getFieldType()));
				setParameter(object, "setDataAggiornamentoStato", NotificaAppIO.model().DATA_AGGIORNAMENTO_STATO.getFieldType(),
					jdbcParameterUtilities.readParameter(rs, "data_aggiornamento_stato", NotificaAppIO.model().DATA_AGGIORNAMENTO_STATO.getFieldType()));
				setParameter(object, "setDataProssimaSpedizione", NotificaAppIO.model().DATA_PROSSIMA_SPEDIZIONE.getFieldType(),
					jdbcParameterUtilities.readParameter(rs, "data_prossima_spedizione", NotificaAppIO.model().DATA_PROSSIMA_SPEDIZIONE.getFieldType()));
				setParameter(object, "setTentativiSpedizione", NotificaAppIO.model().TENTATIVI_SPEDIZIONE.getFieldType(),
					jdbcParameterUtilities.readParameter(rs, "tentativi_spedizione", NotificaAppIO.model().TENTATIVI_SPEDIZIONE.getFieldType()));
				setParameter(object, "setIdMessaggio", NotificaAppIO.model().ID_MESSAGGIO.getFieldType(),
					jdbcParameterUtilities.readParameter(rs, "id_messaggio", NotificaAppIO.model().ID_MESSAGGIO.getFieldType()));
				setParameter(object, "setStatoMessaggio", NotificaAppIO.model().STATO_MESSAGGIO.getFieldType(),
					jdbcParameterUtilities.readParameter(rs, "stato_messaggio", NotificaAppIO.model().STATO_MESSAGGIO.getFieldType()));
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

			if(model.equals(NotificaAppIO.model())){
				NotificaAppIO object = new NotificaAppIO();
				setParameter(object, "setId", Long.class,
					this.getObjectFromMap(map,"id"));
				setParameter(object, "setDebitoreIdentificativo", NotificaAppIO.model().DEBITORE_IDENTIFICATIVO.getFieldType(),
					this.getObjectFromMap(map,"debitoreIdentificativo"));
				setParameter(object, "setCodVersamentoEnte", NotificaAppIO.model().COD_VERSAMENTO_ENTE.getFieldType(),
					this.getObjectFromMap(map,"codVersamentoEnte"));
				setParameter(object, "setCodApplicazione", NotificaAppIO.model().COD_APPLICAZIONE.getFieldType(),
					this.getObjectFromMap(map,"codApplicazione"));
				setParameter(object, "setCodDominio", NotificaAppIO.model().COD_DOMINIO.getFieldType(),
					this.getObjectFromMap(map,"codDominio"));
				setParameter(object, "setIuv", NotificaAppIO.model().IUV.getFieldType(),
					this.getObjectFromMap(map,"iuv"));
				setParameter(object, "setTipoEsito", NotificaAppIO.model().TIPO_ESITO.getFieldType(),
					this.getObjectFromMap(map,"tipoEsito"));
				setParameter(object, "setDataCreazione", NotificaAppIO.model().DATA_CREAZIONE.getFieldType(),
					this.getObjectFromMap(map,"dataCreazione"));
				setParameter(object, "setStato", NotificaAppIO.model().STATO.getFieldType(),
					this.getObjectFromMap(map,"stato"));
				setParameter(object, "setDescrizioneStato", NotificaAppIO.model().DESCRIZIONE_STATO.getFieldType(),
					this.getObjectFromMap(map,"descrizioneStato"));
				setParameter(object, "setDataAggiornamentoStato", NotificaAppIO.model().DATA_AGGIORNAMENTO_STATO.getFieldType(),
					this.getObjectFromMap(map,"dataAggiornamentoStato"));
				setParameter(object, "setDataProssimaSpedizione", NotificaAppIO.model().DATA_PROSSIMA_SPEDIZIONE.getFieldType(),
					this.getObjectFromMap(map,"dataProssimaSpedizione"));
				setParameter(object, "setTentativiSpedizione", NotificaAppIO.model().TENTATIVI_SPEDIZIONE.getFieldType(),
					this.getObjectFromMap(map,"tentativiSpedizione"));
				setParameter(object, "setIdMessaggio", NotificaAppIO.model().ID_MESSAGGIO.getFieldType(),
					this.getObjectFromMap(map,"idMessaggio"));
				setParameter(object, "setStatoMessaggio", NotificaAppIO.model().STATO_MESSAGGIO.getFieldType(),
					this.getObjectFromMap(map,"statoMessaggio"));
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

			if(model.equals(NotificaAppIO.model())){
				return new org.openspcoop2.utils.jdbc.CustomKeyGeneratorObject("notifiche_app_io","id","seq_notifiche_app_io","notifiche_app_io_init_seq");
			}
			
			else{
				throw new ServiceException("Model ["+model.toString()+"] not supported by getKeyGeneratorObject: "+this.getClass().getName());
			}

		}catch(Exception e){
			throw new ServiceException("Model ["+model.toString()+"] occurs error in getKeyGeneratorObject: "+e.getMessage(),e);
		}
		
	}

}
