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

import it.govpay.orm.IbanAccredito;


/**     
 * IbanAccreditoFetch
 *
 * @author Giovanni Bussu (bussu@link.it)
 * @author Lorenzo Nardi (nardi@link.it)
 * @author $Author$
 * @version $Rev$, $Date$
 */
public class IbanAccreditoFetch extends AbstractJDBCFetch {

	@Override
	public Object fetch(TipiDatabase tipoDatabase, IModel<?> model , ResultSet rs) throws ServiceException {
		
		try{
			JDBCParameterUtilities jdbcParameterUtilities =  
					new JDBCParameterUtilities(tipoDatabase);

			if(model.equals(IbanAccredito.model())){
				IbanAccredito object = new IbanAccredito();
				setParameter(object, "setId", Long.class,
					jdbcParameterUtilities.readParameter(rs, "id", Long.class));
				setParameter(object, "setCodIban", IbanAccredito.model().COD_IBAN.getFieldType(),
					jdbcParameterUtilities.readParameter(rs, "cod_iban", IbanAccredito.model().COD_IBAN.getFieldType()));
				setParameter(object, "setIdSellerBank", IbanAccredito.model().ID_SELLER_BANK.getFieldType(),
					jdbcParameterUtilities.readParameter(rs, "id_seller_bank", IbanAccredito.model().ID_SELLER_BANK.getFieldType()));
				setParameter(object, "setIdNegozio", IbanAccredito.model().ID_NEGOZIO.getFieldType(),
					jdbcParameterUtilities.readParameter(rs, "id_negozio", IbanAccredito.model().ID_NEGOZIO.getFieldType()));
				setParameter(object, "setBicAccredito", IbanAccredito.model().BIC_ACCREDITO.getFieldType(),
					jdbcParameterUtilities.readParameter(rs, "bic_accredito", IbanAccredito.model().BIC_ACCREDITO.getFieldType()));
				setParameter(object, "setIbanAppoggio", IbanAccredito.model().IBAN_APPOGGIO.getFieldType(),
					jdbcParameterUtilities.readParameter(rs, "iban_appoggio", IbanAccredito.model().IBAN_APPOGGIO.getFieldType()));
				setParameter(object, "setBicAppoggio", IbanAccredito.model().BIC_APPOGGIO.getFieldType(),
					jdbcParameterUtilities.readParameter(rs, "bic_appoggio", IbanAccredito.model().BIC_APPOGGIO.getFieldType()));
				setParameter(object, "setPostale", IbanAccredito.model().POSTALE.getFieldType(),
					jdbcParameterUtilities.readParameter(rs, "postale", IbanAccredito.model().POSTALE.getFieldType()));
				setParameter(object, "setAttivato", IbanAccredito.model().ATTIVATO.getFieldType(),
					jdbcParameterUtilities.readParameter(rs, "attivato", IbanAccredito.model().ATTIVATO.getFieldType()));
				setParameter(object, "setAbilitato", IbanAccredito.model().ABILITATO.getFieldType(),
					jdbcParameterUtilities.readParameter(rs, "abilitato", IbanAccredito.model().ABILITATO.getFieldType()));
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

			if(model.equals(IbanAccredito.model())){
				IbanAccredito object = new IbanAccredito();
				setParameter(object, "setId", Long.class,
					this.getObjectFromMap(map,"id"));
				setParameter(object, "setCodIban", IbanAccredito.model().COD_IBAN.getFieldType(),
					this.getObjectFromMap(map,"codIban"));
				setParameter(object, "setIdSellerBank", IbanAccredito.model().ID_SELLER_BANK.getFieldType(),
					this.getObjectFromMap(map,"idSellerBank"));
				setParameter(object, "setIdNegozio", IbanAccredito.model().ID_NEGOZIO.getFieldType(),
					this.getObjectFromMap(map,"idNegozio"));
				setParameter(object, "setBicAccredito", IbanAccredito.model().BIC_ACCREDITO.getFieldType(),
					this.getObjectFromMap(map,"bicAccredito"));
				setParameter(object, "setIbanAppoggio", IbanAccredito.model().IBAN_APPOGGIO.getFieldType(),
					this.getObjectFromMap(map,"ibanAppoggio"));
				setParameter(object, "setBicAppoggio", IbanAccredito.model().BIC_APPOGGIO.getFieldType(),
					this.getObjectFromMap(map,"bicAppoggio"));
				setParameter(object, "setPostale", IbanAccredito.model().POSTALE.getFieldType(),
					this.getObjectFromMap(map,"postale"));
				setParameter(object, "setAttivato", IbanAccredito.model().ATTIVATO.getFieldType(),
					this.getObjectFromMap(map,"attivato"));
				setParameter(object, "setAbilitato", IbanAccredito.model().ABILITATO.getFieldType(),
					this.getObjectFromMap(map,"abilitato"));
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

			if(model.equals(IbanAccredito.model())){
				return new org.openspcoop2.utils.jdbc.CustomKeyGeneratorObject("iban_accredito","id","seq_iban_accredito","iban_accredito_init_seq");
			}
			
			else{
				throw new ServiceException("Model ["+model.toString()+"] not supported by getKeyGeneratorObject: "+this.getClass().getName());
			}

		}catch(Exception e){
			throw new ServiceException("Model ["+model.toString()+"] occurs error in getKeyGeneratorObject: "+e.getMessage(),e);
		}
		
	}

}
