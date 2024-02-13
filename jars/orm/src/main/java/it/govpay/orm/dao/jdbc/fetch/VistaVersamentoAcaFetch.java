/*
 * GovPay - Porta di Accesso al Nodo dei Pagamenti SPC 
 * http://www.gov4j.it/govpay
 * 
 * Copyright (c) 2014-2024 Link.it srl (http://www.link.it).
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

import it.govpay.orm.VistaVersamentoAca;


/**     
 * VistaVersamentoAcaFetch
 *
 * @author Giovanni Bussu (bussu@link.it)
 * @author Lorenzo Nardi (nardi@link.it)
 * @author $Author$
 * @version $Rev$, $Date$
 */
public class VistaVersamentoAcaFetch extends AbstractJDBCFetch {

	@Override
	public Object fetch(TipiDatabase tipoDatabase, IModel<?> model , ResultSet rs) throws ServiceException {
		
		try{
			JDBCParameterUtilities jdbcParameterUtilities =  
					new JDBCParameterUtilities(tipoDatabase);

			if(model.equals(VistaVersamentoAca.model())){
				VistaVersamentoAca object = new VistaVersamentoAca();
				setParameter(object, "setId", Long.class,
					jdbcParameterUtilities.readParameter(rs, "id", Long.class));
				setParameter(object, "setCodVersamentoEnte", VistaVersamentoAca.model().COD_VERSAMENTO_ENTE.getFieldType(),
					jdbcParameterUtilities.readParameter(rs, "cod_versamento_ente", VistaVersamentoAca.model().COD_VERSAMENTO_ENTE.getFieldType()));
				setParameter(object, "setCodDominio", VistaVersamentoAca.model().COD_DOMINIO.getFieldType(),
					jdbcParameterUtilities.readParameter(rs, "cod_dominio", VistaVersamentoAca.model().COD_DOMINIO.getFieldType()));
				setParameter(object, "setCodApplicazione", VistaVersamentoAca.model().COD_APPLICAZIONE.getFieldType(),
					jdbcParameterUtilities.readParameter(rs, "cod_applicazione", VistaVersamentoAca.model().COD_APPLICAZIONE.getFieldType()));
				setParameter(object, "setImportoTotale", VistaVersamentoAca.model().IMPORTO_TOTALE.getFieldType(),
					jdbcParameterUtilities.readParameter(rs, "importo_totale", VistaVersamentoAca.model().IMPORTO_TOTALE.getFieldType()));
				setParameter(object, "setStatoVersamento", VistaVersamentoAca.model().STATO_VERSAMENTO.getFieldType(),
					jdbcParameterUtilities.readParameter(rs, "stato_versamento", VistaVersamentoAca.model().STATO_VERSAMENTO.getFieldType()));
				setParameter(object, "setDataValidita", VistaVersamentoAca.model().DATA_VALIDITA.getFieldType(),
					jdbcParameterUtilities.readParameter(rs, "data_validita", VistaVersamentoAca.model().DATA_VALIDITA.getFieldType()));
				setParameter(object, "setDataScadenza", VistaVersamentoAca.model().DATA_SCADENZA.getFieldType(),
					jdbcParameterUtilities.readParameter(rs, "data_scadenza", VistaVersamentoAca.model().DATA_SCADENZA.getFieldType()));
				setParameter(object, "setCausaleVersamento", VistaVersamentoAca.model().CAUSALE_VERSAMENTO.getFieldType(),
					jdbcParameterUtilities.readParameter(rs, "causale_versamento", VistaVersamentoAca.model().CAUSALE_VERSAMENTO.getFieldType()));
				setParameter(object, "setDebitoreTipo", VistaVersamentoAca.model().DEBITORE_TIPO.getFieldType(),
					jdbcParameterUtilities.readParameter(rs, "debitore_tipo", VistaVersamentoAca.model().DEBITORE_TIPO.getFieldType()));
				setParameter(object, "setDebitoreIdentificativo", VistaVersamentoAca.model().DEBITORE_IDENTIFICATIVO.getFieldType(),
					jdbcParameterUtilities.readParameter(rs, "debitore_identificativo", VistaVersamentoAca.model().DEBITORE_IDENTIFICATIVO.getFieldType()));
				setParameter(object, "setDebitoreAnagrafica", VistaVersamentoAca.model().DEBITORE_ANAGRAFICA.getFieldType(),
					jdbcParameterUtilities.readParameter(rs, "debitore_anagrafica", VistaVersamentoAca.model().DEBITORE_ANAGRAFICA.getFieldType()));
				setParameter(object, "setIuvVersamento", VistaVersamentoAca.model().IUV_VERSAMENTO.getFieldType(),
					jdbcParameterUtilities.readParameter(rs, "iuv_versamento", VistaVersamentoAca.model().IUV_VERSAMENTO.getFieldType()));
				setParameter(object, "setNumeroAvviso", VistaVersamentoAca.model().NUMERO_AVVISO.getFieldType(),
					jdbcParameterUtilities.readParameter(rs, "numero_avviso", VistaVersamentoAca.model().NUMERO_AVVISO.getFieldType()));
				setParameter(object, "setDataUltimaModificaAca", VistaVersamentoAca.model().DATA_ULTIMA_MODIFICA_ACA.getFieldType(),
					jdbcParameterUtilities.readParameter(rs, "data_ultima_modifica_aca", VistaVersamentoAca.model().DATA_ULTIMA_MODIFICA_ACA.getFieldType()));
				setParameter(object, "setDataUltimaComunicazioneAca", VistaVersamentoAca.model().DATA_ULTIMA_COMUNICAZIONE_ACA.getFieldType(),
					jdbcParameterUtilities.readParameter(rs, "data_ultima_comunicazione_aca", VistaVersamentoAca.model().DATA_ULTIMA_COMUNICAZIONE_ACA.getFieldType()));
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

			if(model.equals(VistaVersamentoAca.model())){
				VistaVersamentoAca object = new VistaVersamentoAca();
				setParameter(object, "setId", Long.class,
					this.getObjectFromMap(map,"id"));
				setParameter(object, "setCodVersamentoEnte", VistaVersamentoAca.model().COD_VERSAMENTO_ENTE.getFieldType(),
					this.getObjectFromMap(map,"codVersamentoEnte"));
				setParameter(object, "setCodDominio", VistaVersamentoAca.model().COD_DOMINIO.getFieldType(),
					this.getObjectFromMap(map,"codDominio"));
				setParameter(object, "setCodApplicazione", VistaVersamentoAca.model().COD_APPLICAZIONE.getFieldType(),
					this.getObjectFromMap(map,"codApplicazione"));
				setParameter(object, "setImportoTotale", VistaVersamentoAca.model().IMPORTO_TOTALE.getFieldType(),
					this.getObjectFromMap(map,"importoTotale"));
				setParameter(object, "setStatoVersamento", VistaVersamentoAca.model().STATO_VERSAMENTO.getFieldType(),
					this.getObjectFromMap(map,"statoVersamento"));
				setParameter(object, "setDataValidita", VistaVersamentoAca.model().DATA_VALIDITA.getFieldType(),
					this.getObjectFromMap(map,"dataValidita"));
				setParameter(object, "setDataScadenza", VistaVersamentoAca.model().DATA_SCADENZA.getFieldType(),
					this.getObjectFromMap(map,"dataScadenza"));
				setParameter(object, "setCausaleVersamento", VistaVersamentoAca.model().CAUSALE_VERSAMENTO.getFieldType(),
					this.getObjectFromMap(map,"causaleVersamento"));
				setParameter(object, "setDebitoreTipo", VistaVersamentoAca.model().DEBITORE_TIPO.getFieldType(),
					this.getObjectFromMap(map,"debitoreTipo"));
				setParameter(object, "setDebitoreIdentificativo", VistaVersamentoAca.model().DEBITORE_IDENTIFICATIVO.getFieldType(),
					this.getObjectFromMap(map,"debitoreIdentificativo"));
				setParameter(object, "setDebitoreAnagrafica", VistaVersamentoAca.model().DEBITORE_ANAGRAFICA.getFieldType(),
					this.getObjectFromMap(map,"debitoreAnagrafica"));
				setParameter(object, "setIuvVersamento", VistaVersamentoAca.model().IUV_VERSAMENTO.getFieldType(),
					this.getObjectFromMap(map,"iuvVersamento"));
				setParameter(object, "setNumeroAvviso", VistaVersamentoAca.model().NUMERO_AVVISO.getFieldType(),
					this.getObjectFromMap(map,"numeroAvviso"));
				setParameter(object, "setDataUltimaModificaAca", VistaVersamentoAca.model().DATA_ULTIMA_MODIFICA_ACA.getFieldType(),
					this.getObjectFromMap(map,"dataUltimaModificaAca"));
				setParameter(object, "setDataUltimaComunicazioneAca", VistaVersamentoAca.model().DATA_ULTIMA_COMUNICAZIONE_ACA.getFieldType(),
					this.getObjectFromMap(map,"dataUltimaComunicazioneAca"));
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

			if(model.equals(VistaVersamentoAca.model())){
				return new org.openspcoop2.utils.jdbc.CustomKeyGeneratorObject("v_versamenti_aca","id","seq_v_versamenti_aca","v_versamenti_aca_init_seq");
			}
			
			else{
				throw new ServiceException("Model ["+model.toString()+"] not supported by getKeyGeneratorObject: "+this.getClass().getName());
			}

		}catch(Exception e){
			throw new ServiceException("Model ["+model.toString()+"] occurs error in getKeyGeneratorObject: "+e.getMessage(),e);
		}
		
	}

}
