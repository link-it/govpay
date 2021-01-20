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

import it.govpay.orm.FR;


/**     
 * FRFetch
 *
 * @author Giovanni Bussu (bussu@link.it)
 * @author Lorenzo Nardi (nardi@link.it)
 * @author $Author$
 * @version $Rev$, $Date$
 */
public class FRFetch extends AbstractJDBCFetch {

	@Override
	public Object fetch(TipiDatabase tipoDatabase, IModel<?> model , ResultSet rs) throws ServiceException {
		
		try{
			JDBCParameterUtilities jdbcParameterUtilities =  
					new JDBCParameterUtilities(tipoDatabase);

			if(model.equals(FR.model())){
				FR object = new FR();
				this.setParameter(object, "setId", Long.class,
					jdbcParameterUtilities.readParameter(rs, "id", Long.class));
				this.setParameter(object, "setCodPsp", FR.model().COD_PSP.getFieldType(),
					jdbcParameterUtilities.readParameter(rs, "cod_psp", FR.model().COD_PSP.getFieldType()));
				this.setParameter(object, "setCodDominio", FR.model().COD_DOMINIO.getFieldType(),
					jdbcParameterUtilities.readParameter(rs, "cod_dominio", FR.model().COD_DOMINIO.getFieldType()));
				this.setParameter(object, "setCodFlusso", FR.model().COD_FLUSSO.getFieldType(),
					jdbcParameterUtilities.readParameter(rs, "cod_flusso", FR.model().COD_FLUSSO.getFieldType()));
				this.setParameter(object, "setStato", FR.model().STATO.getFieldType(),
					jdbcParameterUtilities.readParameter(rs, "stato", FR.model().STATO.getFieldType()));
				this.setParameter(object, "setDescrizioneStato", FR.model().DESCRIZIONE_STATO.getFieldType(),
					jdbcParameterUtilities.readParameter(rs, "descrizione_stato", FR.model().DESCRIZIONE_STATO.getFieldType()));
				this.setParameter(object, "setIur", FR.model().IUR.getFieldType(),
					jdbcParameterUtilities.readParameter(rs, "iur", FR.model().IUR.getFieldType()));
				this.setParameter(object, "setDataOraFlusso", FR.model().DATA_ORA_FLUSSO.getFieldType(),
					jdbcParameterUtilities.readParameter(rs, "data_ora_flusso", FR.model().DATA_ORA_FLUSSO.getFieldType()));
				this.setParameter(object, "setDataRegolamento", FR.model().DATA_REGOLAMENTO.getFieldType(),
					jdbcParameterUtilities.readParameter(rs, "data_regolamento", FR.model().DATA_REGOLAMENTO.getFieldType()));
				this.setParameter(object, "setDataAcquisizione", FR.model().DATA_ACQUISIZIONE.getFieldType(),
					jdbcParameterUtilities.readParameter(rs, "data_acquisizione", FR.model().DATA_ACQUISIZIONE.getFieldType()));
				this.setParameter(object, "setNumeroPagamenti", FR.model().NUMERO_PAGAMENTI.getFieldType(),
					jdbcParameterUtilities.readParameter(rs, "numero_pagamenti", FR.model().NUMERO_PAGAMENTI.getFieldType()));
				this.setParameter(object, "setImportoTotalePagamenti", FR.model().IMPORTO_TOTALE_PAGAMENTI.getFieldType(),
					jdbcParameterUtilities.readParameter(rs, "importo_totale_pagamenti", FR.model().IMPORTO_TOTALE_PAGAMENTI.getFieldType()));
				this.setParameter(object, "setCodBicRiversamento", FR.model().COD_BIC_RIVERSAMENTO.getFieldType(),
					jdbcParameterUtilities.readParameter(rs, "cod_bic_riversamento", FR.model().COD_BIC_RIVERSAMENTO.getFieldType()));
				this.setParameter(object, "setXml", FR.model().XML.getFieldType(),
					jdbcParameterUtilities.readParameter(rs, "xml", FR.model().XML.getFieldType()));
				setParameter(object, "setRagioneSocialePsp", FR.model().RAGIONE_SOCIALE_PSP.getFieldType(),
					jdbcParameterUtilities.readParameter(rs, "ragione_sociale_psp", FR.model().RAGIONE_SOCIALE_PSP.getFieldType()));
				setParameter(object, "setRagioneSocialeDominio", FR.model().RAGIONE_SOCIALE_DOMINIO.getFieldType(),
					jdbcParameterUtilities.readParameter(rs, "ragione_sociale_dominio", FR.model().RAGIONE_SOCIALE_DOMINIO.getFieldType()));
				setParameter(object, "setObsoleto", FR.model().OBSOLETO.getFieldType(),
					jdbcParameterUtilities.readParameter(rs, "obsoleto", FR.model().OBSOLETO.getFieldType()));
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

			if(model.equals(FR.model())){
				FR object = new FR();
				this.setParameter(object, "setId", Long.class,
					this.getObjectFromMap(map,"id"));
				this.setParameter(object, "setCodPsp", FR.model().COD_PSP.getFieldType(),
					this.getObjectFromMap(map,"codPsp"));
				this.setParameter(object, "setCodDominio", FR.model().COD_DOMINIO.getFieldType(),
					this.getObjectFromMap(map,"codDominio"));
				this.setParameter(object, "setCodFlusso", FR.model().COD_FLUSSO.getFieldType(),
					this.getObjectFromMap(map,"codFlusso"));
				this.setParameter(object, "setStato", FR.model().STATO.getFieldType(),
					this.getObjectFromMap(map,"stato"));
				this.setParameter(object, "setDescrizioneStato", FR.model().DESCRIZIONE_STATO.getFieldType(),
					this.getObjectFromMap(map,"descrizioneStato"));
				this.setParameter(object, "setIur", FR.model().IUR.getFieldType(),
					this.getObjectFromMap(map,"iur"));
				this.setParameter(object, "setDataOraFlusso", FR.model().DATA_ORA_FLUSSO.getFieldType(),
					this.getObjectFromMap(map,"dataOraFlusso"));
				this.setParameter(object, "setDataRegolamento", FR.model().DATA_REGOLAMENTO.getFieldType(),
					this.getObjectFromMap(map,"dataRegolamento"));
				this.setParameter(object, "setDataAcquisizione", FR.model().DATA_ACQUISIZIONE.getFieldType(),
					this.getObjectFromMap(map,"dataAcquisizione"));
				this.setParameter(object, "setNumeroPagamenti", FR.model().NUMERO_PAGAMENTI.getFieldType(),
					this.getObjectFromMap(map,"numeroPagamenti"));
				this.setParameter(object, "setImportoTotalePagamenti", FR.model().IMPORTO_TOTALE_PAGAMENTI.getFieldType(),
					this.getObjectFromMap(map,"importoTotalePagamenti"));
				this.setParameter(object, "setCodBicRiversamento", FR.model().COD_BIC_RIVERSAMENTO.getFieldType(),
					this.getObjectFromMap(map,"codBicRiversamento"));
				this.setParameter(object, "setXml", FR.model().XML.getFieldType(),
					this.getObjectFromMap(map,"xml"));
				setParameter(object, "setRagioneSocialePsp", FR.model().RAGIONE_SOCIALE_PSP.getFieldType(),
					this.getObjectFromMap(map,"ragioneSocialePsp"));
				setParameter(object, "setRagioneSocialeDominio", FR.model().RAGIONE_SOCIALE_DOMINIO.getFieldType(),
					this.getObjectFromMap(map,"ragioneSocialeDominio"));
				setParameter(object, "setObsoleto", FR.model().OBSOLETO.getFieldType(),
					this.getObjectFromMap(map,"obsoleto"));
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

			if(model.equals(FR.model())){
				return new org.openspcoop2.utils.jdbc.CustomKeyGeneratorObject("fr","id","seq_fr","fr_init_seq");
			}
			
			else{
				throw new ServiceException("Model ["+model.toString()+"] not supported by getKeyGeneratorObject: "+this.getClass().getName());
			}

		}catch(Exception e){
			throw new ServiceException("Model ["+model.toString()+"] occurs error in getKeyGeneratorObject: "+e.getMessage(),e);
		}
		
	}

}
