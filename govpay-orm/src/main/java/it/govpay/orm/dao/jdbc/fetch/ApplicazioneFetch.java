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

import it.govpay.orm.Applicazione;


/**     
 * ApplicazioneFetch
 *
 * @author Giovanni Bussu (bussu@link.it)
 * @author Lorenzo Nardi (nardi@link.it)
 * @author $Author$
 * @version $Rev$, $Date$
 */
public class ApplicazioneFetch extends AbstractJDBCFetch {

	@Override
	public Object fetch(TipiDatabase tipoDatabase, IModel<?> model , ResultSet rs) throws ServiceException {
		
		try{
			JDBCParameterUtilities jdbcParameterUtilities =  
					new JDBCParameterUtilities(tipoDatabase);

			if(model.equals(Applicazione.model())){
				Applicazione object = new Applicazione();
				setParameter(object, "setId", Long.class,
					jdbcParameterUtilities.readParameter(rs, "id", Long.class));
				setParameter(object, "setCodApplicazione", Applicazione.model().COD_APPLICAZIONE.getFieldType(),
					jdbcParameterUtilities.readParameter(rs, "cod_applicazione", Applicazione.model().COD_APPLICAZIONE.getFieldType()));
				setParameter(object, "setAutoIUV", Applicazione.model().AUTO_IUV.getFieldType(),
					jdbcParameterUtilities.readParameter(rs, "auto_iuv", Applicazione.model().AUTO_IUV.getFieldType()));
				setParameter(object, "setFirmaRicevuta", Applicazione.model().FIRMA_RICEVUTA.getFieldType(),
					jdbcParameterUtilities.readParameter(rs, "firma_ricevuta", Applicazione.model().FIRMA_RICEVUTA.getFieldType()));
				setParameter(object, "setCodConnettoreEsito", Applicazione.model().COD_CONNETTORE_ESITO.getFieldType(),
					jdbcParameterUtilities.readParameter(rs, "cod_connettore_esito", Applicazione.model().COD_CONNETTORE_ESITO.getFieldType()));
				setParameter(object, "setCodConnettoreVerifica", Applicazione.model().COD_CONNETTORE_VERIFICA.getFieldType(),
					jdbcParameterUtilities.readParameter(rs, "cod_connettore_verifica", Applicazione.model().COD_CONNETTORE_VERIFICA.getFieldType()));
				setParameter(object, "setTrusted", Applicazione.model().TRUSTED.getFieldType(),
					jdbcParameterUtilities.readParameter(rs, "trusted", Applicazione.model().TRUSTED.getFieldType()));
				setParameter(object, "setCodApplicazioneIuv", Applicazione.model().COD_APPLICAZIONE_IUV.getFieldType(),
					jdbcParameterUtilities.readParameter(rs, "cod_applicazione_iuv", Applicazione.model().COD_APPLICAZIONE_IUV.getFieldType()));
				setParameter(object, "setRegExp", Applicazione.model().REG_EXP.getFieldType(),
					jdbcParameterUtilities.readParameter(rs, "reg_exp", Applicazione.model().REG_EXP.getFieldType()));
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

			if(model.equals(Applicazione.model())){
				Applicazione object = new Applicazione();
				setParameter(object, "setId", Long.class,
					this.getObjectFromMap(map,"id"));
				setParameter(object, "setCodApplicazione", Applicazione.model().COD_APPLICAZIONE.getFieldType(),
					this.getObjectFromMap(map,"codApplicazione"));
				setParameter(object, "setAutoIUV", Applicazione.model().AUTO_IUV.getFieldType(),
					this.getObjectFromMap(map,"autoIUV"));
				setParameter(object, "setFirmaRicevuta", Applicazione.model().FIRMA_RICEVUTA.getFieldType(),
					this.getObjectFromMap(map,"firmaRicevuta"));
				setParameter(object, "setCodConnettoreEsito", Applicazione.model().COD_CONNETTORE_ESITO.getFieldType(),
					this.getObjectFromMap(map,"codConnettoreEsito"));
				setParameter(object, "setCodConnettoreVerifica", Applicazione.model().COD_CONNETTORE_VERIFICA.getFieldType(),
					this.getObjectFromMap(map,"codConnettoreVerifica"));
				setParameter(object, "setTrusted", Applicazione.model().TRUSTED.getFieldType(),
					this.getObjectFromMap(map,"trusted"));
				setParameter(object, "setCodApplicazioneIuv", Applicazione.model().COD_APPLICAZIONE_IUV.getFieldType(),
					this.getObjectFromMap(map,"codApplicazioneIuv"));
				setParameter(object, "setRegExp", Applicazione.model().REG_EXP.getFieldType(),
					this.getObjectFromMap(map,"regExp"));
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

			if(model.equals(Applicazione.model())){
				return new org.openspcoop2.utils.jdbc.CustomKeyGeneratorObject("applicazioni","id","seq_applicazioni","applicazioni_init_seq");
			}
			
			else{
				throw new ServiceException("Model ["+model.toString()+"] not supported by getKeyGeneratorObject: "+this.getClass().getName());
			}

		}catch(Exception e){
			throw new ServiceException("Model ["+model.toString()+"] occurs error in getKeyGeneratorObject: "+e.getMessage(),e);
		}
		
	}

}
