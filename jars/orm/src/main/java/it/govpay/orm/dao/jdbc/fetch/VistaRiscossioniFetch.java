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

import java.sql.ResultSet;
import java.util.Map;

import org.openspcoop2.generic_project.beans.IModel;
import org.openspcoop2.generic_project.dao.jdbc.utils.AbstractJDBCFetch;
import org.openspcoop2.generic_project.dao.jdbc.utils.JDBCParameterUtilities;
import org.openspcoop2.generic_project.exception.ServiceException;
import org.openspcoop2.utils.TipiDatabase;
import org.openspcoop2.utils.jdbc.IKeyGeneratorObject;
import org.openspcoop2.utils.jdbc.JDBCDefaultForXSDType;

import it.govpay.orm.VistaRiscossioni;


/**     
 * VistaRiscossioniFetch
 *
 * @author Giovanni Bussu (bussu@link.it)
 * @author Lorenzo Nardi (nardi@link.it)
 * @author $Author$
 * @version $Rev$, $Date$
 */
public class VistaRiscossioniFetch extends AbstractJDBCFetch {

	@Override
	public Object fetch(TipiDatabase tipoDatabase, IModel<?> model , ResultSet rs) throws ServiceException {
		
		try{
			JDBCParameterUtilities jdbcParameterUtilities =  
					new JDBCParameterUtilities(tipoDatabase);

			if(model.equals(VistaRiscossioni.model())){
				VistaRiscossioni object = new VistaRiscossioni();
				setParameter(object, "setCodDominio", VistaRiscossioni.model().COD_DOMINIO.getFieldType(),
					jdbcParameterUtilities.readParameter(rs, "cod_dominio", VistaRiscossioni.model().COD_DOMINIO.getFieldType()));
				setParameter(object, "setIuv", VistaRiscossioni.model().IUV.getFieldType(),
					jdbcParameterUtilities.readParameter(rs, "iuv", VistaRiscossioni.model().IUV.getFieldType()));
				setParameter(object, "setIur", VistaRiscossioni.model().IUR.getFieldType(),
					jdbcParameterUtilities.readParameter(rs, "iur", VistaRiscossioni.model().IUR.getFieldType()));
				setParameter(object, "setCodFlusso", VistaRiscossioni.model().COD_FLUSSO.getFieldType(),
					jdbcParameterUtilities.readParameter(rs, "cod_flusso", VistaRiscossioni.model().COD_FLUSSO.getFieldType()));
				setParameter(object, "setFrIur", VistaRiscossioni.model().FR_IUR.getFieldType(),
					jdbcParameterUtilities.readParameter(rs, "fr_iur", VistaRiscossioni.model().FR_IUR.getFieldType()));
				setParameter(object, "setDataRegolamento", VistaRiscossioni.model().DATA_REGOLAMENTO.getFieldType(),
					jdbcParameterUtilities.readParameter(rs, "data_regolamento", VistaRiscossioni.model().DATA_REGOLAMENTO.getFieldType()));
				setParameter(object, "setNumeroPagamenti", VistaRiscossioni.model().NUMERO_PAGAMENTI.getFieldType(),
					jdbcParameterUtilities.readParameter(rs, "numero_pagamenti", VistaRiscossioni.model().NUMERO_PAGAMENTI.getFieldType()));
				setParameter(object, "setImportoTotalePagamenti", VistaRiscossioni.model().IMPORTO_TOTALE_PAGAMENTI.getFieldType(),
					jdbcParameterUtilities.readParameter(rs, "importo_totale_pagamenti", VistaRiscossioni.model().IMPORTO_TOTALE_PAGAMENTI.getFieldType()));
				setParameter(object, "setImportoPagato", VistaRiscossioni.model().IMPORTO_PAGATO.getFieldType(),
					jdbcParameterUtilities.readParameter(rs, "importo_pagato", VistaRiscossioni.model().IMPORTO_PAGATO.getFieldType()));
				setParameter(object, "setCodSingoloVersamentoEnte", VistaRiscossioni.model().COD_SINGOLO_VERSAMENTO_ENTE.getFieldType(),
					jdbcParameterUtilities.readParameter(rs, "cod_singolo_versamento_ente", VistaRiscossioni.model().COD_SINGOLO_VERSAMENTO_ENTE.getFieldType()));
				setParameter(object, "setIndiceDati", VistaRiscossioni.model().INDICE_DATI.getFieldType(),
					jdbcParameterUtilities.readParameter(rs, "indice_dati", VistaRiscossioni.model().INDICE_DATI.getFieldType(), JDBCDefaultForXSDType.FORCE_ZERO_AS_NULL));
				setParameter(object, "setCodVersamentoEnte", VistaRiscossioni.model().COD_VERSAMENTO_ENTE.getFieldType(),
					jdbcParameterUtilities.readParameter(rs, "cod_versamento_ente", VistaRiscossioni.model().COD_VERSAMENTO_ENTE.getFieldType()));
				setParameter(object, "setCodApplicazione", VistaRiscossioni.model().COD_APPLICAZIONE.getFieldType(),
					jdbcParameterUtilities.readParameter(rs, "cod_applicazione", VistaRiscossioni.model().COD_APPLICAZIONE.getFieldType()));
				setParameter(object, "setDataPagamento", VistaRiscossioni.model().DATA_PAGAMENTO.getFieldType(),
					jdbcParameterUtilities.readParameter(rs, "data_pagamento", VistaRiscossioni.model().DATA_PAGAMENTO.getFieldType()));
				setParameter(object, "setCodTipoVersamento", VistaRiscossioni.model().COD_TIPO_VERSAMENTO.getFieldType(),
					jdbcParameterUtilities.readParameter(rs, "cod_tipo_versamento", VistaRiscossioni.model().COD_TIPO_VERSAMENTO.getFieldType()));
				setParameter(object, "setCodEntrata", VistaRiscossioni.model().COD_ENTRATA.getFieldType(),
					jdbcParameterUtilities.readParameter(rs, "cod_entrata", VistaRiscossioni.model().COD_ENTRATA.getFieldType()));
				setParameter(object, "setIdentificativoDebitore", VistaRiscossioni.model().IDENTIFICATIVO_DEBITORE.getFieldType(),
					jdbcParameterUtilities.readParameter(rs, "identificativo_debitore", VistaRiscossioni.model().IDENTIFICATIVO_DEBITORE.getFieldType()));
				setParameter(object, "setAnno", VistaRiscossioni.model().ANNO.getFieldType(),
					jdbcParameterUtilities.readParameter(rs, "anno", VistaRiscossioni.model().ANNO.getFieldType()));
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

			if(model.equals(VistaRiscossioni.model())){
				VistaRiscossioni object = new VistaRiscossioni();
				setParameter(object, "setCodDominio", VistaRiscossioni.model().COD_DOMINIO.getFieldType(),
					this.getObjectFromMap(map,"codDominio"));
				setParameter(object, "setIuv", VistaRiscossioni.model().IUV.getFieldType(),
					this.getObjectFromMap(map,"iuv"));
				setParameter(object, "setIur", VistaRiscossioni.model().IUR.getFieldType(),
					this.getObjectFromMap(map,"iur"));
				setParameter(object, "setCodFlusso", VistaRiscossioni.model().COD_FLUSSO.getFieldType(),
					this.getObjectFromMap(map,"cod_flusso"));
				setParameter(object, "setFrIur", VistaRiscossioni.model().FR_IUR.getFieldType(),
					this.getObjectFromMap(map,"fr_iur"));
				setParameter(object, "setDataRegolamento", VistaRiscossioni.model().DATA_REGOLAMENTO.getFieldType(),
					this.getObjectFromMap(map,"dataRegolamento"));
				setParameter(object, "setNumeroPagamenti", VistaRiscossioni.model().NUMERO_PAGAMENTI.getFieldType(),
					this.getObjectFromMap(map,"numeroPagamenti"));
				setParameter(object, "setImportoTotalePagamenti", VistaRiscossioni.model().IMPORTO_TOTALE_PAGAMENTI.getFieldType(),
					this.getObjectFromMap(map,"importoTotalePagamenti"));
				setParameter(object, "setImportoPagato", VistaRiscossioni.model().IMPORTO_PAGATO.getFieldType(),
					this.getObjectFromMap(map,"importoPagato"));
				setParameter(object, "setCodSingoloVersamentoEnte", VistaRiscossioni.model().COD_SINGOLO_VERSAMENTO_ENTE.getFieldType(),
					this.getObjectFromMap(map,"codSingoloVersamentoEnte"));
				setParameter(object, "setIndiceDati", VistaRiscossioni.model().INDICE_DATI.getFieldType(),
					this.getObjectFromMap(map,"indiceDati"));
				setParameter(object, "setCodVersamentoEnte", VistaRiscossioni.model().COD_VERSAMENTO_ENTE.getFieldType(),
					this.getObjectFromMap(map,"codVersamentoEnte"));
				setParameter(object, "setCodApplicazione", VistaRiscossioni.model().COD_APPLICAZIONE.getFieldType(),
					this.getObjectFromMap(map,"codApplicazione"));
				setParameter(object, "setDataPagamento", VistaRiscossioni.model().DATA_PAGAMENTO.getFieldType(),
					this.getObjectFromMap(map,"dataPagamento"));
				setParameter(object, "setCodTipoVersamento", VistaRiscossioni.model().COD_TIPO_VERSAMENTO.getFieldType(),
					this.getObjectFromMap(map,"codTipoVersamento"));
				setParameter(object, "setCodEntrata", VistaRiscossioni.model().COD_ENTRATA.getFieldType(),
					this.getObjectFromMap(map,"codEntrata"));
				setParameter(object, "setIdentificativoDebitore", VistaRiscossioni.model().IDENTIFICATIVO_DEBITORE.getFieldType(),
					this.getObjectFromMap(map,"identificativoDebitore"));
				setParameter(object, "setAnno", VistaRiscossioni.model().ANNO.getFieldType(),
					this.getObjectFromMap(map,"anno"));
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

			if(model.equals(VistaRiscossioni.model())){
				return new org.openspcoop2.utils.jdbc.CustomKeyGeneratorObject("v_riscossioni","id","seq_v_riscossioni","v_riscossioni_init_seq");
			}
			
			else{
				throw new ServiceException("Model ["+model.toString()+"] not supported by getKeyGeneratorObject: "+this.getClass().getName());
			}

		}catch(Exception e){
			throw new ServiceException("Model ["+model.toString()+"] occurs error in getKeyGeneratorObject: "+e.getMessage(),e);
		}
		
	}

}
