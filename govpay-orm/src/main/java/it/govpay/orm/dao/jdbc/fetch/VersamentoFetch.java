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

import it.govpay.orm.Versamento;


/**     
 * VersamentoFetch
 *
 * @author Giovanni Bussu (bussu@link.it)
 * @author Lorenzo Nardi (nardi@link.it)
 * @author $Author$
 * @version $Rev$, $Date$
 */
public class VersamentoFetch extends AbstractJDBCFetch {

	@Override
	public Object fetch(TipiDatabase tipoDatabase, IModel<?> model , ResultSet rs) throws ServiceException {
		
		try{
			JDBCParameterUtilities jdbcParameterUtilities =  
					new JDBCParameterUtilities(tipoDatabase);

			if(model.equals(Versamento.model())){
				Versamento object = new Versamento();
				setParameter(object, "setId", Long.class,
					jdbcParameterUtilities.readParameter(rs, "id", Long.class));
				setParameter(object, "setCodVersamentoEnte", Versamento.model().COD_VERSAMENTO_ENTE.getFieldType(),
					jdbcParameterUtilities.readParameter(rs, "cod_versamento_ente", Versamento.model().COD_VERSAMENTO_ENTE.getFieldType()));
				setParameter(object, "setImportoTotale", Versamento.model().IMPORTO_TOTALE.getFieldType(),
					jdbcParameterUtilities.readParameter(rs, "importo_totale", Versamento.model().IMPORTO_TOTALE.getFieldType()));
				setParameter(object, "setStatoVersamento", Versamento.model().STATO_VERSAMENTO.getFieldType(),
					jdbcParameterUtilities.readParameter(rs, "stato_versamento", Versamento.model().STATO_VERSAMENTO.getFieldType()));
				setParameter(object, "setDescrizioneStato", Versamento.model().DESCRIZIONE_STATO.getFieldType(),
					jdbcParameterUtilities.readParameter(rs, "descrizione_stato", Versamento.model().DESCRIZIONE_STATO.getFieldType()));
				setParameter(object, "setAggiornabile", Versamento.model().AGGIORNABILE.getFieldType(),
					jdbcParameterUtilities.readParameter(rs, "aggiornabile", Versamento.model().AGGIORNABILE.getFieldType()));
				setParameter(object, "setDataCreazione", Versamento.model().DATA_CREAZIONE.getFieldType(),
					jdbcParameterUtilities.readParameter(rs, "data_creazione", Versamento.model().DATA_CREAZIONE.getFieldType()));
				setParameter(object, "setDataScadenza", Versamento.model().DATA_SCADENZA.getFieldType(),
					jdbcParameterUtilities.readParameter(rs, "data_scadenza", Versamento.model().DATA_SCADENZA.getFieldType()));
				setParameter(object, "setDataOraUltimoAggiornamento", Versamento.model().DATA_ORA_ULTIMO_AGGIORNAMENTO.getFieldType(),
					jdbcParameterUtilities.readParameter(rs, "data_ora_ultimo_aggiornamento", Versamento.model().DATA_ORA_ULTIMO_AGGIORNAMENTO.getFieldType()));
				setParameter(object, "setCausaleVersamento", Versamento.model().CAUSALE_VERSAMENTO.getFieldType(),
					jdbcParameterUtilities.readParameter(rs, "causale_versamento", Versamento.model().CAUSALE_VERSAMENTO.getFieldType()));
				setParameter(object, "setDebitoreIdentificativo", Versamento.model().DEBITORE_IDENTIFICATIVO.getFieldType(),
					jdbcParameterUtilities.readParameter(rs, "debitore_identificativo", Versamento.model().DEBITORE_IDENTIFICATIVO.getFieldType()));
				setParameter(object, "setDebitoreAnagrafica", Versamento.model().DEBITORE_ANAGRAFICA.getFieldType(),
					jdbcParameterUtilities.readParameter(rs, "debitore_anagrafica", Versamento.model().DEBITORE_ANAGRAFICA.getFieldType()));
				setParameter(object, "setDebitoreIndirizzo", Versamento.model().DEBITORE_INDIRIZZO.getFieldType(),
					jdbcParameterUtilities.readParameter(rs, "debitore_indirizzo", Versamento.model().DEBITORE_INDIRIZZO.getFieldType()));
				setParameter(object, "setDebitoreCivico", Versamento.model().DEBITORE_CIVICO.getFieldType(),
					jdbcParameterUtilities.readParameter(rs, "debitore_civico", Versamento.model().DEBITORE_CIVICO.getFieldType()));
				setParameter(object, "setDebitoreCap", Versamento.model().DEBITORE_CAP.getFieldType(),
					jdbcParameterUtilities.readParameter(rs, "debitore_cap", Versamento.model().DEBITORE_CAP.getFieldType()));
				setParameter(object, "setDebitoreLocalita", Versamento.model().DEBITORE_LOCALITA.getFieldType(),
					jdbcParameterUtilities.readParameter(rs, "debitore_localita", Versamento.model().DEBITORE_LOCALITA.getFieldType()));
				setParameter(object, "setDebitoreProvincia", Versamento.model().DEBITORE_PROVINCIA.getFieldType(),
					jdbcParameterUtilities.readParameter(rs, "debitore_provincia", Versamento.model().DEBITORE_PROVINCIA.getFieldType()));
				setParameter(object, "setDebitoreNazione", Versamento.model().DEBITORE_NAZIONE.getFieldType(),
					jdbcParameterUtilities.readParameter(rs, "debitore_nazione", Versamento.model().DEBITORE_NAZIONE.getFieldType()));
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

			if(model.equals(Versamento.model())){
				Versamento object = new Versamento();
				setParameter(object, "setId", Long.class,
					this.getObjectFromMap(map,"id"));
				setParameter(object, "setCodVersamentoEnte", Versamento.model().COD_VERSAMENTO_ENTE.getFieldType(),
					this.getObjectFromMap(map,"codVersamentoEnte"));
				setParameter(object, "setImportoTotale", Versamento.model().IMPORTO_TOTALE.getFieldType(),
					this.getObjectFromMap(map,"importoTotale"));
				setParameter(object, "setStatoVersamento", Versamento.model().STATO_VERSAMENTO.getFieldType(),
					this.getObjectFromMap(map,"statoVersamento"));
				setParameter(object, "setDescrizioneStato", Versamento.model().DESCRIZIONE_STATO.getFieldType(),
					this.getObjectFromMap(map,"descrizioneStato"));
				setParameter(object, "setAggiornabile", Versamento.model().AGGIORNABILE.getFieldType(),
					this.getObjectFromMap(map,"aggiornabile"));
				setParameter(object, "setDataCreazione", Versamento.model().DATA_CREAZIONE.getFieldType(),
					this.getObjectFromMap(map,"dataCreazione"));
				setParameter(object, "setDataScadenza", Versamento.model().DATA_SCADENZA.getFieldType(),
					this.getObjectFromMap(map,"dataScadenza"));
				setParameter(object, "setDataOraUltimoAggiornamento", Versamento.model().DATA_ORA_ULTIMO_AGGIORNAMENTO.getFieldType(),
					this.getObjectFromMap(map,"dataOraUltimoAggiornamento"));
				setParameter(object, "setCausaleVersamento", Versamento.model().CAUSALE_VERSAMENTO.getFieldType(),
					this.getObjectFromMap(map,"causaleVersamento"));
				setParameter(object, "setDebitoreIdentificativo", Versamento.model().DEBITORE_IDENTIFICATIVO.getFieldType(),
					this.getObjectFromMap(map,"debitoreIdentificativo"));
				setParameter(object, "setDebitoreAnagrafica", Versamento.model().DEBITORE_ANAGRAFICA.getFieldType(),
					this.getObjectFromMap(map,"debitoreAnagrafica"));
				setParameter(object, "setDebitoreIndirizzo", Versamento.model().DEBITORE_INDIRIZZO.getFieldType(),
					this.getObjectFromMap(map,"debitoreIndirizzo"));
				setParameter(object, "setDebitoreCivico", Versamento.model().DEBITORE_CIVICO.getFieldType(),
					this.getObjectFromMap(map,"debitoreCivico"));
				setParameter(object, "setDebitoreCap", Versamento.model().DEBITORE_CAP.getFieldType(),
					this.getObjectFromMap(map,"debitoreCap"));
				setParameter(object, "setDebitoreLocalita", Versamento.model().DEBITORE_LOCALITA.getFieldType(),
					this.getObjectFromMap(map,"debitoreLocalita"));
				setParameter(object, "setDebitoreProvincia", Versamento.model().DEBITORE_PROVINCIA.getFieldType(),
					this.getObjectFromMap(map,"debitoreProvincia"));
				setParameter(object, "setDebitoreNazione", Versamento.model().DEBITORE_NAZIONE.getFieldType(),
					this.getObjectFromMap(map,"debitoreNazione"));
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

			if(model.equals(Versamento.model())){
				return new org.openspcoop2.utils.jdbc.CustomKeyGeneratorObject("versamenti","id","seq_versamenti","versamenti_init_seq");
			}
			
			else{
				throw new ServiceException("Model ["+model.toString()+"] not supported by getKeyGeneratorObject: "+this.getClass().getName());
			}

		}catch(Exception e){
			throw new ServiceException("Model ["+model.toString()+"] occurs error in getKeyGeneratorObject: "+e.getMessage(),e);
		}
		
	}

}
