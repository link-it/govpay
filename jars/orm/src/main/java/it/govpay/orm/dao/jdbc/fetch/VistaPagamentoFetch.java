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

import it.govpay.orm.VistaPagamento;


/**     
 * VistaPagamentoFetch
 *
 * @author Giovanni Bussu (bussu@link.it)
 * @author Lorenzo Nardi (nardi@link.it)
 * @author $Author$
 * @version $Rev$, $Date$
 */
public class VistaPagamentoFetch extends AbstractJDBCFetch {

	@Override
	public Object fetch(TipiDatabase tipoDatabase, IModel<?> model , ResultSet rs) throws ServiceException {
		
		try{
			JDBCParameterUtilities jdbcParameterUtilities =  
					new JDBCParameterUtilities(tipoDatabase);

			if(model.equals(VistaPagamento.model())){
				VistaPagamento object = new VistaPagamento();
				setParameter(object, "setId", Long.class,
					jdbcParameterUtilities.readParameter(rs, "id", Long.class));
				setParameter(object, "setCodDominio", VistaPagamento.model().COD_DOMINIO.getFieldType(),
					jdbcParameterUtilities.readParameter(rs, "cod_dominio", VistaPagamento.model().COD_DOMINIO.getFieldType()));
				setParameter(object, "setIuv", VistaPagamento.model().IUV.getFieldType(),
					jdbcParameterUtilities.readParameter(rs, "iuv", VistaPagamento.model().IUV.getFieldType()));
				setParameter(object, "setIndiceDati", VistaPagamento.model().INDICE_DATI.getFieldType(),
					jdbcParameterUtilities.readParameter(rs, "indice_dati", VistaPagamento.model().INDICE_DATI.getFieldType()));
				setParameter(object, "setImportoPagato", VistaPagamento.model().IMPORTO_PAGATO.getFieldType(),
					jdbcParameterUtilities.readParameter(rs, "importo_pagato", VistaPagamento.model().IMPORTO_PAGATO.getFieldType()));
				setParameter(object, "setDataAcquisizione", VistaPagamento.model().DATA_ACQUISIZIONE.getFieldType(),
					jdbcParameterUtilities.readParameter(rs, "data_acquisizione", VistaPagamento.model().DATA_ACQUISIZIONE.getFieldType()));
				setParameter(object, "setIur", VistaPagamento.model().IUR.getFieldType(),
					jdbcParameterUtilities.readParameter(rs, "iur", VistaPagamento.model().IUR.getFieldType()));
				setParameter(object, "setDataPagamento", VistaPagamento.model().DATA_PAGAMENTO.getFieldType(),
					jdbcParameterUtilities.readParameter(rs, "data_pagamento", VistaPagamento.model().DATA_PAGAMENTO.getFieldType()));
				setParameter(object, "setCommissioniPsp", VistaPagamento.model().COMMISSIONI_PSP.getFieldType(),
					jdbcParameterUtilities.readParameter(rs, "commissioni_psp", VistaPagamento.model().COMMISSIONI_PSP.getFieldType()));
				setParameter(object, "setTipoAllegato", VistaPagamento.model().TIPO_ALLEGATO.getFieldType(),
					jdbcParameterUtilities.readParameter(rs, "tipo_allegato", VistaPagamento.model().TIPO_ALLEGATO.getFieldType()));
				setParameter(object, "setAllegato", VistaPagamento.model().ALLEGATO.getFieldType(),
					jdbcParameterUtilities.readParameter(rs, "allegato", VistaPagamento.model().ALLEGATO.getFieldType()));
				setParameter(object, "setDataAcquisizioneRevoca", VistaPagamento.model().DATA_ACQUISIZIONE_REVOCA.getFieldType(),
					jdbcParameterUtilities.readParameter(rs, "data_acquisizione_revoca", VistaPagamento.model().DATA_ACQUISIZIONE_REVOCA.getFieldType()));
				setParameter(object, "setCausaleRevoca", VistaPagamento.model().CAUSALE_REVOCA.getFieldType(),
					jdbcParameterUtilities.readParameter(rs, "causale_revoca", VistaPagamento.model().CAUSALE_REVOCA.getFieldType()));
				setParameter(object, "setDatiRevoca", VistaPagamento.model().DATI_REVOCA.getFieldType(),
					jdbcParameterUtilities.readParameter(rs, "dati_revoca", VistaPagamento.model().DATI_REVOCA.getFieldType()));
				setParameter(object, "setImportoRevocato", VistaPagamento.model().IMPORTO_REVOCATO.getFieldType(),
					jdbcParameterUtilities.readParameter(rs, "importo_revocato", VistaPagamento.model().IMPORTO_REVOCATO.getFieldType()));
				setParameter(object, "setEsitoRevoca", VistaPagamento.model().ESITO_REVOCA.getFieldType(),
					jdbcParameterUtilities.readParameter(rs, "esito_revoca", VistaPagamento.model().ESITO_REVOCA.getFieldType()));
				setParameter(object, "setDatiEsitoRevoca", VistaPagamento.model().DATI_ESITO_REVOCA.getFieldType(),
					jdbcParameterUtilities.readParameter(rs, "dati_esito_revoca", VistaPagamento.model().DATI_ESITO_REVOCA.getFieldType()));
				setParameter(object, "setStato", VistaPagamento.model().STATO.getFieldType(),
					jdbcParameterUtilities.readParameter(rs, "stato", VistaPagamento.model().STATO.getFieldType()));
				setParameter(object, "setTipo", VistaPagamento.model().TIPO.getFieldType(),
					jdbcParameterUtilities.readParameter(rs, "tipo", VistaPagamento.model().TIPO.getFieldType()));
				setParameter(object, "setVrsId", VistaPagamento.model().VRS_ID.getFieldType(),
					jdbcParameterUtilities.readParameter(rs, "vrs_id", VistaPagamento.model().VRS_ID.getFieldType()));
				setParameter(object, "setVrsCodVersamentoEnte", VistaPagamento.model().VRS_COD_VERSAMENTO_ENTE.getFieldType(),
					jdbcParameterUtilities.readParameter(rs, "vrs_cod_versamento_ente", VistaPagamento.model().VRS_COD_VERSAMENTO_ENTE.getFieldType()));
				setParameter(object, "setVrsTassonomia", VistaPagamento.model().VRS_TASSONOMIA.getFieldType(),
					jdbcParameterUtilities.readParameter(rs, "vrs_tassonomia", VistaPagamento.model().VRS_TASSONOMIA.getFieldType()));
				setParameter(object, "setVrsDivisione", VistaPagamento.model().VRS_DIVISIONE.getFieldType(),
					jdbcParameterUtilities.readParameter(rs, "vrs_divisione", VistaPagamento.model().VRS_DIVISIONE.getFieldType()));
				setParameter(object, "setVrsDirezione", VistaPagamento.model().VRS_DIREZIONE.getFieldType(),
					jdbcParameterUtilities.readParameter(rs, "vrs_direzione", VistaPagamento.model().VRS_DIREZIONE.getFieldType()));
				setParameter(object, "setSngCodSingVersEnte", VistaPagamento.model().SNG_COD_SING_VERS_ENTE.getFieldType(),
					jdbcParameterUtilities.readParameter(rs, "sng_cod_sing_vers_ente", VistaPagamento.model().SNG_COD_SING_VERS_ENTE.getFieldType()));
				setParameter(object, "setRptIuv", VistaPagamento.model().RPT_IUV.getFieldType(),
					jdbcParameterUtilities.readParameter(rs, "rpt_iuv", VistaPagamento.model().RPT_IUV.getFieldType()));
				setParameter(object, "setRptCcp", VistaPagamento.model().RPT_CCP.getFieldType(),
					jdbcParameterUtilities.readParameter(rs, "rpt_ccp", VistaPagamento.model().RPT_CCP.getFieldType()));
				setParameter(object, "setRncTrn", VistaPagamento.model().RNC_TRN.getFieldType(),
					jdbcParameterUtilities.readParameter(rs, "rnc_trn", VistaPagamento.model().RNC_TRN.getFieldType()));
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

			if(model.equals(VistaPagamento.model())){
				VistaPagamento object = new VistaPagamento();
				setParameter(object, "setId", Long.class,
					this.getObjectFromMap(map,"id"));
				setParameter(object, "setCodDominio", VistaPagamento.model().COD_DOMINIO.getFieldType(),
					this.getObjectFromMap(map,"codDominio"));
				setParameter(object, "setIuv", VistaPagamento.model().IUV.getFieldType(),
					this.getObjectFromMap(map,"iuv"));
				setParameter(object, "setIndiceDati", VistaPagamento.model().INDICE_DATI.getFieldType(),
					this.getObjectFromMap(map,"indiceDati"));
				setParameter(object, "setImportoPagato", VistaPagamento.model().IMPORTO_PAGATO.getFieldType(),
					this.getObjectFromMap(map,"importoPagato"));
				setParameter(object, "setDataAcquisizione", VistaPagamento.model().DATA_ACQUISIZIONE.getFieldType(),
					this.getObjectFromMap(map,"dataAcquisizione"));
				setParameter(object, "setIur", VistaPagamento.model().IUR.getFieldType(),
					this.getObjectFromMap(map,"iur"));
				setParameter(object, "setDataPagamento", VistaPagamento.model().DATA_PAGAMENTO.getFieldType(),
					this.getObjectFromMap(map,"dataPagamento"));
				setParameter(object, "setCommissioniPsp", VistaPagamento.model().COMMISSIONI_PSP.getFieldType(),
					this.getObjectFromMap(map,"commissioniPsp"));
				setParameter(object, "setTipoAllegato", VistaPagamento.model().TIPO_ALLEGATO.getFieldType(),
					this.getObjectFromMap(map,"tipoAllegato"));
				setParameter(object, "setAllegato", VistaPagamento.model().ALLEGATO.getFieldType(),
					this.getObjectFromMap(map,"allegato"));
				setParameter(object, "setDataAcquisizioneRevoca", VistaPagamento.model().DATA_ACQUISIZIONE_REVOCA.getFieldType(),
					this.getObjectFromMap(map,"dataAcquisizioneRevoca"));
				setParameter(object, "setCausaleRevoca", VistaPagamento.model().CAUSALE_REVOCA.getFieldType(),
					this.getObjectFromMap(map,"causaleRevoca"));
				setParameter(object, "setDatiRevoca", VistaPagamento.model().DATI_REVOCA.getFieldType(),
					this.getObjectFromMap(map,"datiRevoca"));
				setParameter(object, "setImportoRevocato", VistaPagamento.model().IMPORTO_REVOCATO.getFieldType(),
					this.getObjectFromMap(map,"importoRevocato"));
				setParameter(object, "setEsitoRevoca", VistaPagamento.model().ESITO_REVOCA.getFieldType(),
					this.getObjectFromMap(map,"esitoRevoca"));
				setParameter(object, "setDatiEsitoRevoca", VistaPagamento.model().DATI_ESITO_REVOCA.getFieldType(),
					this.getObjectFromMap(map,"datiEsitoRevoca"));
				setParameter(object, "setStato", VistaPagamento.model().STATO.getFieldType(),
					this.getObjectFromMap(map,"stato"));
				setParameter(object, "setTipo", VistaPagamento.model().TIPO.getFieldType(),
					this.getObjectFromMap(map,"tipo"));
				setParameter(object, "setVrsId", VistaPagamento.model().VRS_ID.getFieldType(),
					this.getObjectFromMap(map,"vrsId"));
				setParameter(object, "setVrsCodVersamentoEnte", VistaPagamento.model().VRS_COD_VERSAMENTO_ENTE.getFieldType(),
					this.getObjectFromMap(map,"vrsCodVersamentoEnte"));
				setParameter(object, "setVrsTassonomia", VistaPagamento.model().VRS_TASSONOMIA.getFieldType(),
					this.getObjectFromMap(map,"vrsTassonomia"));
				setParameter(object, "setVrsDivisione", VistaPagamento.model().VRS_DIVISIONE.getFieldType(),
					this.getObjectFromMap(map,"vrsDivisione"));
				setParameter(object, "setVrsDirezione", VistaPagamento.model().VRS_DIREZIONE.getFieldType(),
					this.getObjectFromMap(map,"vrsDirezione"));
				setParameter(object, "setSngCodSingVersEnte", VistaPagamento.model().SNG_COD_SING_VERS_ENTE.getFieldType(),
					this.getObjectFromMap(map,"sngCodSingVersEnte"));
				setParameter(object, "setRptIuv", VistaPagamento.model().RPT_IUV.getFieldType(),
					this.getObjectFromMap(map,"rptIuv"));
				setParameter(object, "setRptCcp", VistaPagamento.model().RPT_CCP.getFieldType(),
					this.getObjectFromMap(map,"rptCcp"));
				setParameter(object, "setRncTrn", VistaPagamento.model().RNC_TRN.getFieldType(),
					this.getObjectFromMap(map,"rncTrn"));
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

			if(model.equals(VistaPagamento.model())){
				return new org.openspcoop2.utils.jdbc.CustomKeyGeneratorObject("v_pagamenti","id","seq_v_pagamenti","v_pagamenti_init_seq");
			}
			
			else{
				throw new ServiceException("Model ["+model.toString()+"] not supported by getKeyGeneratorObject: "+this.getClass().getName());
			}

		}catch(Exception e){
			throw new ServiceException("Model ["+model.toString()+"] occurs error in getKeyGeneratorObject: "+e.getMessage(),e);
		}
		
	}

}
