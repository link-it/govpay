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

import it.govpay.orm.Applicazione;
import it.govpay.orm.ApplicazioneTributo;


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
				setParameter(object, "setAbilitato", Applicazione.model().ABILITATO.getFieldType(),
					jdbcParameterUtilities.readParameter(rs, "abilitato", Applicazione.model().ABILITATO.getFieldType()));
				setParameter(object, "setPrincipal", Applicazione.model().PRINCIPAL.getFieldType(),
					jdbcParameterUtilities.readParameter(rs, "principal", Applicazione.model().PRINCIPAL.getFieldType()));
				setParameter(object, "setFirmaRicevuta", Applicazione.model().FIRMA_RICEVUTA.getFieldType(),
					jdbcParameterUtilities.readParameter(rs, "firma_ricevuta", Applicazione.model().FIRMA_RICEVUTA.getFieldType()));
				setParameter(object, "setCodConnettoreEsito", Applicazione.model().COD_CONNETTORE_ESITO.getFieldType(),
					jdbcParameterUtilities.readParameter(rs, "cod_connettore_esito", Applicazione.model().COD_CONNETTORE_ESITO.getFieldType()));
				setParameter(object, "setCodConnettoreVerifica", Applicazione.model().COD_CONNETTORE_VERIFICA.getFieldType(),
					jdbcParameterUtilities.readParameter(rs, "cod_connettore_verifica", Applicazione.model().COD_CONNETTORE_VERIFICA.getFieldType()));
				return object;
			}
			if(model.equals(Applicazione.model().APPLICAZIONE_TRIBUTO)){
				ApplicazioneTributo object = new ApplicazioneTributo();
				setParameter(object, "setId", Long.class,
					jdbcParameterUtilities.readParameter(rs, "id", Long.class));
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
				setParameter(object, "setAbilitato", Applicazione.model().ABILITATO.getFieldType(),
					this.getObjectFromMap(map,"abilitato"));
				setParameter(object, "setPrincipal", Applicazione.model().PRINCIPAL.getFieldType(),
					this.getObjectFromMap(map,"principal"));
				setParameter(object, "setFirmaRicevuta", Applicazione.model().FIRMA_RICEVUTA.getFieldType(),
					this.getObjectFromMap(map,"firmaRicevuta"));
				setParameter(object, "setCodConnettoreEsito", Applicazione.model().COD_CONNETTORE_ESITO.getFieldType(),
					this.getObjectFromMap(map,"codConnettoreEsito"));
				setParameter(object, "setCodConnettoreVerifica", Applicazione.model().COD_CONNETTORE_VERIFICA.getFieldType(),
					this.getObjectFromMap(map,"codConnettoreVerifica"));
				return object;
			}
			if(model.equals(Applicazione.model().APPLICAZIONE_TRIBUTO)){
				ApplicazioneTributo object = new ApplicazioneTributo();
				setParameter(object, "setId", Long.class,
					this.getObjectFromMap(map,"ApplicazioneTributo.id"));
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
			if(model.equals(Applicazione.model().APPLICAZIONE_TRIBUTO)){
				return new org.openspcoop2.utils.jdbc.CustomKeyGeneratorObject("applicazioni_tributi","id","seq_applicazioni_tributi","applicazioni_tributi_init_seq");
			}
			
			else{
				throw new ServiceException("Model ["+model.toString()+"] not supported by getKeyGeneratorObject: "+this.getClass().getName());
			}

		}catch(Exception e){
			throw new ServiceException("Model ["+model.toString()+"] occurs error in getKeyGeneratorObject: "+e.getMessage(),e);
		}
		
	}

}
