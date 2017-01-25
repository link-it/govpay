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

import it.govpay.orm.FrApplicazione;
import it.govpay.orm.FR;
import it.govpay.orm.FrFiltroApp;


/**     
 * FrFiltroAppFetch
 *
 * @author Giovanni Bussu (bussu@link.it)
 * @author Lorenzo Nardi (nardi@link.it)
 * @author $Author$
 * @version $Rev$, $Date$
 */
public class FrFiltroAppFetch extends AbstractJDBCFetch {

	@Override
	public Object fetch(TipiDatabase tipoDatabase, IModel<?> model , ResultSet rs) throws ServiceException {
		
		try{
			JDBCParameterUtilities jdbcParameterUtilities =  
					new JDBCParameterUtilities(tipoDatabase);

			if(model.equals(FrFiltroApp.model().FR)){
				FR object = new FR();
				setParameter(object, "setId", Long.class,
					jdbcParameterUtilities.readParameter(rs, "id", Long.class));
				setParameter(object, "setCodFlusso", FrFiltroApp.model().FR.COD_FLUSSO.getFieldType(),
					jdbcParameterUtilities.readParameter(rs, "cod_flusso", FrFiltroApp.model().FR.COD_FLUSSO.getFieldType()));
				setParameter(object, "setStato", FrFiltroApp.model().FR.STATO.getFieldType(),
					jdbcParameterUtilities.readParameter(rs, "stato", FrFiltroApp.model().FR.STATO.getFieldType()));
				setParameter(object, "setDescrizioneStato", FrFiltroApp.model().FR.DESCRIZIONE_STATO.getFieldType(),
					jdbcParameterUtilities.readParameter(rs, "descrizione_stato", FrFiltroApp.model().FR.DESCRIZIONE_STATO.getFieldType()));
				setParameter(object, "setIur", FrFiltroApp.model().FR.IUR.getFieldType(),
					jdbcParameterUtilities.readParameter(rs, "iur", FrFiltroApp.model().FR.IUR.getFieldType()));
				setParameter(object, "setAnnoRiferimento", FrFiltroApp.model().FR.ANNO_RIFERIMENTO.getFieldType(),
					jdbcParameterUtilities.readParameter(rs, "anno_riferimento", FrFiltroApp.model().FR.ANNO_RIFERIMENTO.getFieldType()));
				setParameter(object, "setDataOraFlusso", FrFiltroApp.model().FR.DATA_ORA_FLUSSO.getFieldType(),
					jdbcParameterUtilities.readParameter(rs, "data_ora_flusso", FrFiltroApp.model().FR.DATA_ORA_FLUSSO.getFieldType()));
				setParameter(object, "setDataRegolamento", FrFiltroApp.model().FR.DATA_REGOLAMENTO.getFieldType(),
					jdbcParameterUtilities.readParameter(rs, "data_regolamento", FrFiltroApp.model().FR.DATA_REGOLAMENTO.getFieldType()));
				setParameter(object, "setDataAcquisizione", FrFiltroApp.model().FR.DATA_ACQUISIZIONE.getFieldType(),
					jdbcParameterUtilities.readParameter(rs, "data_acquisizione", FrFiltroApp.model().FR.DATA_ACQUISIZIONE.getFieldType()));
				setParameter(object, "setNumeroPagamenti", FrFiltroApp.model().FR.NUMERO_PAGAMENTI.getFieldType(),
					jdbcParameterUtilities.readParameter(rs, "numero_pagamenti", FrFiltroApp.model().FR.NUMERO_PAGAMENTI.getFieldType()));
				setParameter(object, "setImportoTotalePagamenti", FrFiltroApp.model().FR.IMPORTO_TOTALE_PAGAMENTI.getFieldType(),
					jdbcParameterUtilities.readParameter(rs, "importo_totale_pagamenti", FrFiltroApp.model().FR.IMPORTO_TOTALE_PAGAMENTI.getFieldType()));
				setParameter(object, "setCodBicRiversamento", FrFiltroApp.model().FR.COD_BIC_RIVERSAMENTO.getFieldType(),
					jdbcParameterUtilities.readParameter(rs, "cod_bic_riversamento", FrFiltroApp.model().FR.COD_BIC_RIVERSAMENTO.getFieldType()));
				setParameter(object, "setXml", FrFiltroApp.model().FR.XML.getFieldType(),
					jdbcParameterUtilities.readParameter(rs, "xml", FrFiltroApp.model().FR.XML.getFieldType()));
				return object;
			}
			if(model.equals(FrFiltroApp.model().FR_APPLICAZIONE)){
				FrApplicazione object = new FrApplicazione();
				setParameter(object, "setId", Long.class,
					jdbcParameterUtilities.readParameter(rs, "id", Long.class));
				setParameter(object, "setNumeroPagamenti", FrFiltroApp.model().FR_APPLICAZIONE.NUMERO_PAGAMENTI.getFieldType(),
					jdbcParameterUtilities.readParameter(rs, "numero_pagamenti", FrFiltroApp.model().FR_APPLICAZIONE.NUMERO_PAGAMENTI.getFieldType()));
				setParameter(object, "setImportoTotalePagamenti", FrFiltroApp.model().FR_APPLICAZIONE.IMPORTO_TOTALE_PAGAMENTI.getFieldType(),
					jdbcParameterUtilities.readParameter(rs, "importo_totale_pagamenti", FrFiltroApp.model().FR_APPLICAZIONE.IMPORTO_TOTALE_PAGAMENTI.getFieldType()));
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

			if(model.equals(FrFiltroApp.model().FR)){
				FR object = new FR();
				setParameter(object, "setId", Long.class,
					this.getObjectFromMap(map,"FR.id"));
				setParameter(object, "setCodFlusso", FrFiltroApp.model().FR.COD_FLUSSO.getFieldType(),
					this.getObjectFromMap(map,"FR.codFlusso"));
				setParameter(object, "setStato", FrFiltroApp.model().FR.STATO.getFieldType(),
					this.getObjectFromMap(map,"FR.stato"));
				setParameter(object, "setDescrizioneStato", FrFiltroApp.model().FR.DESCRIZIONE_STATO.getFieldType(),
					this.getObjectFromMap(map,"FR.descrizioneStato"));
				setParameter(object, "setIur", FrFiltroApp.model().FR.IUR.getFieldType(),
					this.getObjectFromMap(map,"FR.iur"));
				setParameter(object, "setAnnoRiferimento", FrFiltroApp.model().FR.ANNO_RIFERIMENTO.getFieldType(),
					this.getObjectFromMap(map,"FR.annoRiferimento"));
				setParameter(object, "setDataOraFlusso", FrFiltroApp.model().FR.DATA_ORA_FLUSSO.getFieldType(),
					this.getObjectFromMap(map,"FR.dataOraFlusso"));
				setParameter(object, "setDataRegolamento", FrFiltroApp.model().FR.DATA_REGOLAMENTO.getFieldType(),
					this.getObjectFromMap(map,"FR.dataRegolamento"));
				setParameter(object, "setDataAcquisizione", FrFiltroApp.model().FR.DATA_ACQUISIZIONE.getFieldType(),
					this.getObjectFromMap(map,"FR.dataAcquisizione"));
				setParameter(object, "setNumeroPagamenti", FrFiltroApp.model().FR.NUMERO_PAGAMENTI.getFieldType(),
					this.getObjectFromMap(map,"FR.numeroPagamenti"));
				setParameter(object, "setImportoTotalePagamenti", FrFiltroApp.model().FR.IMPORTO_TOTALE_PAGAMENTI.getFieldType(),
					this.getObjectFromMap(map,"FR.importoTotalePagamenti"));
				setParameter(object, "setCodBicRiversamento", FrFiltroApp.model().FR.COD_BIC_RIVERSAMENTO.getFieldType(),
					this.getObjectFromMap(map,"FR.codBicRiversamento"));
				setParameter(object, "setXml", FrFiltroApp.model().FR.XML.getFieldType(),
					this.getObjectFromMap(map,"FR.xml"));
				return object;
			}
			if(model.equals(FrFiltroApp.model().FR_APPLICAZIONE)){
				FrApplicazione object = new FrApplicazione();
				setParameter(object, "setId", Long.class,
					this.getObjectFromMap(map,"FrApplicazione.id"));
				setParameter(object, "setNumeroPagamenti", FrFiltroApp.model().FR_APPLICAZIONE.NUMERO_PAGAMENTI.getFieldType(),
					this.getObjectFromMap(map,"FrApplicazione.numeroPagamenti"));
				setParameter(object, "setImportoTotalePagamenti", FrFiltroApp.model().FR_APPLICAZIONE.IMPORTO_TOTALE_PAGAMENTI.getFieldType(),
					this.getObjectFromMap(map,"FrApplicazione.importoTotalePagamenti"));
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

			if(model.equals(FrFiltroApp.model().FR)){
				return new org.openspcoop2.utils.jdbc.CustomKeyGeneratorObject("fr","id","seq_fr","fr_init_seq");
			}
			if(model.equals(FrFiltroApp.model().FR_APPLICAZIONE)){
				return new org.openspcoop2.utils.jdbc.CustomKeyGeneratorObject("fr_applicazioni","id","seq_fr_applicazioni","fr_applicazioni_init_seq");
			}
			
			else{
				throw new ServiceException("Model ["+model.toString()+"] not supported by getKeyGeneratorObject: "+this.getClass().getName());
			}

		}catch(Exception e){
			throw new ServiceException("Model ["+model.toString()+"] occurs error in getKeyGeneratorObject: "+e.getMessage(),e);
		}
		
	}

}
