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

import it.govpay.orm.Incasso;


/**     
 * IncassoFetch
 *
 * @author Giovanni Bussu (bussu@link.it)
 * @author Lorenzo Nardi (nardi@link.it)
 * @author $Author$
 * @version $Rev$, $Date$
 */
public class IncassoFetch extends AbstractJDBCFetch {

	@Override
	public Object fetch(TipiDatabase tipoDatabase, IModel<?> model , ResultSet rs) throws ServiceException {
		
		try{
			JDBCParameterUtilities jdbcParameterUtilities =  
					new JDBCParameterUtilities(tipoDatabase);

			if(model.equals(Incasso.model())){
				Incasso object = new Incasso();
				this.setParameter(object, "setId", Long.class,
					jdbcParameterUtilities.readParameter(rs, "id", Long.class));
				this.setParameter(object, "setTrn", Incasso.model().TRN.getFieldType(),
					jdbcParameterUtilities.readParameter(rs, "trn", Incasso.model().TRN.getFieldType()));
				this.setParameter(object, "setCodDominio", Incasso.model().COD_DOMINIO.getFieldType(),
					jdbcParameterUtilities.readParameter(rs, "cod_dominio", Incasso.model().COD_DOMINIO.getFieldType()));
				this.setParameter(object, "setCausale", Incasso.model().CAUSALE.getFieldType(),
					jdbcParameterUtilities.readParameter(rs, "causale", Incasso.model().CAUSALE.getFieldType()));
				this.setParameter(object, "setImporto", Incasso.model().IMPORTO.getFieldType(),
					jdbcParameterUtilities.readParameter(rs, "importo", Incasso.model().IMPORTO.getFieldType()));
				this.setParameter(object, "setDataValuta", Incasso.model().DATA_VALUTA.getFieldType(),
					jdbcParameterUtilities.readParameter(rs, "data_valuta", Incasso.model().DATA_VALUTA.getFieldType()));
				this.setParameter(object, "setDataContabile", Incasso.model().DATA_CONTABILE.getFieldType(),
					jdbcParameterUtilities.readParameter(rs, "data_contabile", Incasso.model().DATA_CONTABILE.getFieldType()));
				this.setParameter(object, "setDataOraIncasso", Incasso.model().DATA_ORA_INCASSO.getFieldType(),
					jdbcParameterUtilities.readParameter(rs, "data_ora_incasso", Incasso.model().DATA_ORA_INCASSO.getFieldType()));
				this.setParameter(object, "setNomeDispositivo", Incasso.model().NOME_DISPOSITIVO.getFieldType(),
					jdbcParameterUtilities.readParameter(rs, "nome_dispositivo", Incasso.model().NOME_DISPOSITIVO.getFieldType()));
				this.setParameter(object, "setIbanAccredito", Incasso.model().IBAN_ACCREDITO.getFieldType(),
					jdbcParameterUtilities.readParameter(rs, "iban_accredito", Incasso.model().IBAN_ACCREDITO.getFieldType()));
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

			if(model.equals(Incasso.model())){
				Incasso object = new Incasso();
				this.setParameter(object, "setId", Long.class,
					this.getObjectFromMap(map,"id"));
				this.setParameter(object, "setTrn", Incasso.model().TRN.getFieldType(),
					this.getObjectFromMap(map,"trn"));
				this.setParameter(object, "setCodDominio", Incasso.model().COD_DOMINIO.getFieldType(),
					this.getObjectFromMap(map,"codDominio"));
				this.setParameter(object, "setCausale", Incasso.model().CAUSALE.getFieldType(),
					this.getObjectFromMap(map,"causale"));
				this.setParameter(object, "setImporto", Incasso.model().IMPORTO.getFieldType(),
					this.getObjectFromMap(map,"importo"));
				this.setParameter(object, "setDataValuta", Incasso.model().DATA_VALUTA.getFieldType(),
					this.getObjectFromMap(map,"dataValuta"));
				this.setParameter(object, "setDataContabile", Incasso.model().DATA_CONTABILE.getFieldType(),
					this.getObjectFromMap(map,"dataContabile"));
				this.setParameter(object, "setDataOraIncasso", Incasso.model().DATA_ORA_INCASSO.getFieldType(),
					this.getObjectFromMap(map,"dataOraIncasso"));
				this.setParameter(object, "setNomeDispositivo", Incasso.model().NOME_DISPOSITIVO.getFieldType(),
					this.getObjectFromMap(map,"nomeDispositivo"));
				this.setParameter(object, "setIbanAccredito", Incasso.model().IBAN_ACCREDITO.getFieldType(),
					this.getObjectFromMap(map,"ibanAccredito"));
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

			if(model.equals(Incasso.model())){
				return new org.openspcoop2.utils.jdbc.CustomKeyGeneratorObject("incassi","id","seq_incassi","incassi_init_seq");
			}
			
			else{
				throw new ServiceException("Model ["+model.toString()+"] not supported by getKeyGeneratorObject: "+this.getClass().getName());
			}

		}catch(Exception e){
			throw new ServiceException("Model ["+model.toString()+"] occurs error in getKeyGeneratorObject: "+e.getMessage(),e);
		}
		
	}

}
