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
				setParameter(object, "setDebitoreAnagrafica", VistaRiscossioni.model().DEBITORE_ANAGRAFICA.getFieldType(),
					jdbcParameterUtilities.readParameter(rs, "debitore_anagrafica", VistaRiscossioni.model().DEBITORE_ANAGRAFICA.getFieldType()));
				setParameter(object, "setAnno", VistaRiscossioni.model().ANNO.getFieldType(),
					jdbcParameterUtilities.readParameter(rs, "anno", VistaRiscossioni.model().ANNO.getFieldType()));
				setParameter(object, "setDescrTipoVersamento", VistaRiscossioni.model().DESCR_TIPO_VERSAMENTO.getFieldType(),
					jdbcParameterUtilities.readParameter(rs, "descr_tipo_versamento", VistaRiscossioni.model().DESCR_TIPO_VERSAMENTO.getFieldType()));
				setParameter(object, "setCodPsp", VistaRiscossioni.model().COD_PSP.getFieldType(),
					jdbcParameterUtilities.readParameter(rs, "cod_psp", VistaRiscossioni.model().COD_PSP.getFieldType()));
				setParameter(object, "setRagioneSocialePsp", VistaRiscossioni.model().RAGIONE_SOCIALE_PSP.getFieldType(),
					jdbcParameterUtilities.readParameter(rs, "ragione_sociale_psp", VistaRiscossioni.model().RAGIONE_SOCIALE_PSP.getFieldType()));
				setParameter(object, "setCodRata", VistaRiscossioni.model().COD_RATA.getFieldType(),
					jdbcParameterUtilities.readParameter(rs, "cod_rata", VistaRiscossioni.model().COD_RATA.getFieldType()));
				setParameter(object, "setCausaleVersamento", VistaRiscossioni.model().CAUSALE_VERSAMENTO.getFieldType(),
					jdbcParameterUtilities.readParameter(rs, "causale_versamento", VistaRiscossioni.model().CAUSALE_VERSAMENTO.getFieldType()));
				setParameter(object, "setImportoVersamento", VistaRiscossioni.model().IMPORTO_VERSAMENTO.getFieldType(),
					jdbcParameterUtilities.readParameter(rs, "importo_versamento", VistaRiscossioni.model().IMPORTO_VERSAMENTO.getFieldType()));
				setParameter(object, "setNumeroAvviso", VistaRiscossioni.model().NUMERO_AVVISO.getFieldType(),
					jdbcParameterUtilities.readParameter(rs, "numero_avviso", VistaRiscossioni.model().NUMERO_AVVISO.getFieldType()));
				setParameter(object, "setIuvPagamento", VistaRiscossioni.model().IUV_PAGAMENTO.getFieldType(),
					jdbcParameterUtilities.readParameter(rs, "iuv_pagamento", VistaRiscossioni.model().IUV_PAGAMENTO.getFieldType()));
				setParameter(object, "setDataScadenza", VistaRiscossioni.model().DATA_SCADENZA.getFieldType(),
					jdbcParameterUtilities.readParameter(rs, "data_scadenza", VistaRiscossioni.model().DATA_SCADENZA.getFieldType()));
				setParameter(object, "setContabilita", VistaRiscossioni.model().CONTABILITA.getFieldType(),
					jdbcParameterUtilities.readParameter(rs, "contabilita", VistaRiscossioni.model().CONTABILITA.getFieldType()));
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
				setParameter(object, "setDebitoreAnagrafica", VistaRiscossioni.model().DEBITORE_ANAGRAFICA.getFieldType(),
					this.getObjectFromMap(map,"debitoreAnagrafica"));
				setParameter(object, "setAnno", VistaRiscossioni.model().ANNO.getFieldType(),
					this.getObjectFromMap(map,"anno"));
				setParameter(object, "setDescrTipoVersamento", VistaRiscossioni.model().DESCR_TIPO_VERSAMENTO.getFieldType(),
					this.getObjectFromMap(map,"descrTipoVersamento"));
				setParameter(object, "setCodPsp", VistaRiscossioni.model().COD_PSP.getFieldType(),
					this.getObjectFromMap(map,"codPsp"));
				setParameter(object, "setRagioneSocialePsp", VistaRiscossioni.model().RAGIONE_SOCIALE_PSP.getFieldType(),
					this.getObjectFromMap(map,"ragioneSocialePsp"));
				setParameter(object, "setCodRata", VistaRiscossioni.model().COD_RATA.getFieldType(),
					this.getObjectFromMap(map,"codRata"));
				setParameter(object, "setCausaleVersamento", VistaRiscossioni.model().CAUSALE_VERSAMENTO.getFieldType(),
					this.getObjectFromMap(map,"causaleVersamento"));
				setParameter(object, "setImportoVersamento", VistaRiscossioni.model().IMPORTO_VERSAMENTO.getFieldType(),
					this.getObjectFromMap(map,"importoVersamento"));
				setParameter(object, "setNumeroAvviso", VistaRiscossioni.model().NUMERO_AVVISO.getFieldType(),
					this.getObjectFromMap(map,"numeroAvviso"));
				setParameter(object, "setIuvPagamento", VistaRiscossioni.model().IUV_PAGAMENTO.getFieldType(),
					this.getObjectFromMap(map,"iuvPagamento"));
				setParameter(object, "setDataScadenza", VistaRiscossioni.model().DATA_SCADENZA.getFieldType(),
					this.getObjectFromMap(map,"dataScadenza"));
				setParameter(object, "setContabilita", VistaRiscossioni.model().CONTABILITA.getFieldType(),
					this.getObjectFromMap(map,"contabilita"));
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
