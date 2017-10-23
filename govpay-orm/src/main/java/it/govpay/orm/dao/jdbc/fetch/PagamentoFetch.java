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

import it.govpay.orm.Pagamento;


/**     
 * PagamentoFetch
 *
 * @author Giovanni Bussu (bussu@link.it)
 * @author Lorenzo Nardi (nardi@link.it)
 * @author $Author$
 * @version $Rev$, $Date$
 */
public class PagamentoFetch extends AbstractJDBCFetch {

	@Override
	public Object fetch(TipiDatabase tipoDatabase, IModel<?> model , ResultSet rs) throws ServiceException {
		
		try{
			JDBCParameterUtilities jdbcParameterUtilities =  
					new JDBCParameterUtilities(tipoDatabase);

			if(model.equals(Pagamento.model())){
				Pagamento object = new Pagamento();
				setParameter(object, "setId", Long.class,
					jdbcParameterUtilities.readParameter(rs, "id", Long.class));
				setParameter(object, "setCodDominio", Pagamento.model().COD_DOMINIO.getFieldType(),
					jdbcParameterUtilities.readParameter(rs, "cod_dominio", Pagamento.model().COD_DOMINIO.getFieldType()));
				setParameter(object, "setIuv", Pagamento.model().IUV.getFieldType(),
					jdbcParameterUtilities.readParameter(rs, "iuv", Pagamento.model().IUV.getFieldType()));
				setParameter(object, "setIndiceDati", Pagamento.model().INDICE_DATI.getFieldType(),
					jdbcParameterUtilities.readParameter(rs, "indice_dati", Pagamento.model().INDICE_DATI.getFieldType()));
				setParameter(object, "setImportoPagato", Pagamento.model().IMPORTO_PAGATO.getFieldType(),
					jdbcParameterUtilities.readParameter(rs, "importo_pagato", Pagamento.model().IMPORTO_PAGATO.getFieldType()));
				setParameter(object, "setDataAcquisizione", Pagamento.model().DATA_ACQUISIZIONE.getFieldType(),
					jdbcParameterUtilities.readParameter(rs, "data_acquisizione", Pagamento.model().DATA_ACQUISIZIONE.getFieldType()));
				setParameter(object, "setIur", Pagamento.model().IUR.getFieldType(),
					jdbcParameterUtilities.readParameter(rs, "iur", Pagamento.model().IUR.getFieldType()));
				setParameter(object, "setDataPagamento", Pagamento.model().DATA_PAGAMENTO.getFieldType(),
					jdbcParameterUtilities.readParameter(rs, "data_pagamento", Pagamento.model().DATA_PAGAMENTO.getFieldType()));
				setParameter(object, "setIbanAccredito", Pagamento.model().IBAN_ACCREDITO.getFieldType(),
					jdbcParameterUtilities.readParameter(rs, "iban_accredito", Pagamento.model().IBAN_ACCREDITO.getFieldType()));
				setParameter(object, "setCommissioniPsp", Pagamento.model().COMMISSIONI_PSP.getFieldType(),
					jdbcParameterUtilities.readParameter(rs, "commissioni_psp", Pagamento.model().COMMISSIONI_PSP.getFieldType()));
				setParameter(object, "setTipoAllegato", Pagamento.model().TIPO_ALLEGATO.getFieldType(),
					jdbcParameterUtilities.readParameter(rs, "tipo_allegato", Pagamento.model().TIPO_ALLEGATO.getFieldType()));
				setParameter(object, "setAllegato", Pagamento.model().ALLEGATO.getFieldType(),
					jdbcParameterUtilities.readParameter(rs, "allegato", Pagamento.model().ALLEGATO.getFieldType()));
				setParameter(object, "setDataAcquisizioneRevoca", Pagamento.model().DATA_ACQUISIZIONE_REVOCA.getFieldType(),
					jdbcParameterUtilities.readParameter(rs, "data_acquisizione_revoca", Pagamento.model().DATA_ACQUISIZIONE_REVOCA.getFieldType()));
				setParameter(object, "setCausaleRevoca", Pagamento.model().CAUSALE_REVOCA.getFieldType(),
					jdbcParameterUtilities.readParameter(rs, "causale_revoca", Pagamento.model().CAUSALE_REVOCA.getFieldType()));
				setParameter(object, "setDatiRevoca", Pagamento.model().DATI_REVOCA.getFieldType(),
					jdbcParameterUtilities.readParameter(rs, "dati_revoca", Pagamento.model().DATI_REVOCA.getFieldType()));
				setParameter(object, "setImportoRevocato", Pagamento.model().IMPORTO_REVOCATO.getFieldType(),
					jdbcParameterUtilities.readParameter(rs, "importo_revocato", Pagamento.model().IMPORTO_REVOCATO.getFieldType()));
				setParameter(object, "setEsitoRevoca", Pagamento.model().ESITO_REVOCA.getFieldType(),
					jdbcParameterUtilities.readParameter(rs, "esito_revoca", Pagamento.model().ESITO_REVOCA.getFieldType()));
				setParameter(object, "setDatiEsitoRevoca", Pagamento.model().DATI_ESITO_REVOCA.getFieldType(),
					jdbcParameterUtilities.readParameter(rs, "dati_esito_revoca", Pagamento.model().DATI_ESITO_REVOCA.getFieldType()));
				setParameter(object, "setStato", Pagamento.model().STATO.getFieldType(),
					jdbcParameterUtilities.readParameter(rs, "stato", Pagamento.model().STATO.getFieldType()));
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

			if(model.equals(Pagamento.model())){
				Pagamento object = new Pagamento();
				setParameter(object, "setId", Long.class,
					this.getObjectFromMap(map,"id"));
				setParameter(object, "setCodDominio", Pagamento.model().COD_DOMINIO.getFieldType(),
					this.getObjectFromMap(map,"codDominio"));
				setParameter(object, "setIuv", Pagamento.model().IUV.getFieldType(),
					this.getObjectFromMap(map,"iuv"));
				setParameter(object, "setIndiceDati", Pagamento.model().INDICE_DATI.getFieldType(),
					this.getObjectFromMap(map,"indiceDati"));
				setParameter(object, "setImportoPagato", Pagamento.model().IMPORTO_PAGATO.getFieldType(),
					this.getObjectFromMap(map,"importoPagato"));
				setParameter(object, "setDataAcquisizione", Pagamento.model().DATA_ACQUISIZIONE.getFieldType(),
					this.getObjectFromMap(map,"dataAcquisizione"));
				setParameter(object, "setIur", Pagamento.model().IUR.getFieldType(),
					this.getObjectFromMap(map,"iur"));
				setParameter(object, "setDataPagamento", Pagamento.model().DATA_PAGAMENTO.getFieldType(),
					this.getObjectFromMap(map,"dataPagamento"));
				setParameter(object, "setIbanAccredito", Pagamento.model().IBAN_ACCREDITO.getFieldType(),
					this.getObjectFromMap(map,"ibanAccredito"));
				setParameter(object, "setCommissioniPsp", Pagamento.model().COMMISSIONI_PSP.getFieldType(),
					this.getObjectFromMap(map,"commissioniPsp"));
				setParameter(object, "setTipoAllegato", Pagamento.model().TIPO_ALLEGATO.getFieldType(),
					this.getObjectFromMap(map,"tipoAllegato"));
				setParameter(object, "setAllegato", Pagamento.model().ALLEGATO.getFieldType(),
					this.getObjectFromMap(map,"allegato"));
				setParameter(object, "setDataAcquisizioneRevoca", Pagamento.model().DATA_ACQUISIZIONE_REVOCA.getFieldType(),
					this.getObjectFromMap(map,"dataAcquisizioneRevoca"));
				setParameter(object, "setCausaleRevoca", Pagamento.model().CAUSALE_REVOCA.getFieldType(),
					this.getObjectFromMap(map,"causaleRevoca"));
				setParameter(object, "setDatiRevoca", Pagamento.model().DATI_REVOCA.getFieldType(),
					this.getObjectFromMap(map,"datiRevoca"));
				setParameter(object, "setImportoRevocato", Pagamento.model().IMPORTO_REVOCATO.getFieldType(),
					this.getObjectFromMap(map,"importoRevocato"));
				setParameter(object, "setEsitoRevoca", Pagamento.model().ESITO_REVOCA.getFieldType(),
					this.getObjectFromMap(map,"esitoRevoca"));
				setParameter(object, "setDatiEsitoRevoca", Pagamento.model().DATI_ESITO_REVOCA.getFieldType(),
					this.getObjectFromMap(map,"datiEsitoRevoca"));
				setParameter(object, "setStato", Pagamento.model().STATO.getFieldType(),
					this.getObjectFromMap(map,"stato"));
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

			if(model.equals(Pagamento.model())){
				return new org.openspcoop2.utils.jdbc.CustomKeyGeneratorObject("pagamenti","id","seq_pagamenti","pagamenti_init_seq");
			}
			
			else{
				throw new ServiceException("Model ["+model.toString()+"] not supported by getKeyGeneratorObject: "+this.getClass().getName());
			}

		}catch(Exception e){
			throw new ServiceException("Model ["+model.toString()+"] occurs error in getKeyGeneratorObject: "+e.getMessage(),e);
		}
		
	}

}
