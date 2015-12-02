/*
 * GovPay - Porta di Accesso al Nodo dei Pagamenti SPC 
 * http://www.gov4j.it/govpay
 * 
 * Copyright (c) 2014-2015 Link.it srl (http://www.link.it).
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

import it.govpay.orm.SingoloVersamento;


/**     
 * SingoloVersamentoFetch
 *
 * @author Giovanni Bussu (bussu@link.it)
 * @author Lorenzo Nardi (nardi@link.it)
 * @author $Author$
 * @version $Rev$, $Date$
 */
public class SingoloVersamentoFetch extends AbstractJDBCFetch {

	@Override
	public Object fetch(TipiDatabase tipoDatabase, IModel<?> model , ResultSet rs) throws ServiceException {
		
		try{
			JDBCParameterUtilities jdbcParameterUtilities =  
					new JDBCParameterUtilities(tipoDatabase);

			if(model.equals(SingoloVersamento.model())){
				SingoloVersamento object = new SingoloVersamento();
				setParameter(object, "setId", Long.class,
					jdbcParameterUtilities.readParameter(rs, "id", Long.class));
				setParameter(object, "setIndice", SingoloVersamento.model().INDICE.getFieldType(),
					jdbcParameterUtilities.readParameter(rs, "indice", SingoloVersamento.model().INDICE.getFieldType()));
				setParameter(object, "setCodSingoloVersamentoEnte", SingoloVersamento.model().COD_SINGOLO_VERSAMENTO_ENTE.getFieldType(),
					jdbcParameterUtilities.readParameter(rs, "cod_singolo_versamento_ente", SingoloVersamento.model().COD_SINGOLO_VERSAMENTO_ENTE.getFieldType()));
				setParameter(object, "setAnnoRiferimento", SingoloVersamento.model().ANNO_RIFERIMENTO.getFieldType(),
					jdbcParameterUtilities.readParameter(rs, "anno_riferimento", SingoloVersamento.model().ANNO_RIFERIMENTO.getFieldType()));
				setParameter(object, "setIbanAccredito", SingoloVersamento.model().IBAN_ACCREDITO.getFieldType(),
					jdbcParameterUtilities.readParameter(rs, "iban_accredito", SingoloVersamento.model().IBAN_ACCREDITO.getFieldType()));
				setParameter(object, "setImportoSingoloVersamento", SingoloVersamento.model().IMPORTO_SINGOLO_VERSAMENTO.getFieldType(),
					jdbcParameterUtilities.readParameter(rs, "importo_singolo_versamento", SingoloVersamento.model().IMPORTO_SINGOLO_VERSAMENTO.getFieldType()));
				setParameter(object, "setImportoCommissioniPA", SingoloVersamento.model().IMPORTO_COMMISSIONI_PA.getFieldType(),
					jdbcParameterUtilities.readParameter(rs, "importo_commissioni_pa", SingoloVersamento.model().IMPORTO_COMMISSIONI_PA.getFieldType()));
				setParameter(object, "setSingoloImportoPagato", SingoloVersamento.model().SINGOLO_IMPORTO_PAGATO.getFieldType(),
					jdbcParameterUtilities.readParameter(rs, "singolo_importo_pagato", SingoloVersamento.model().SINGOLO_IMPORTO_PAGATO.getFieldType()));
				setParameter(object, "setCausaleVersamento", SingoloVersamento.model().CAUSALE_VERSAMENTO.getFieldType(),
					jdbcParameterUtilities.readParameter(rs, "causale_versamento", SingoloVersamento.model().CAUSALE_VERSAMENTO.getFieldType()));
				setParameter(object, "setDatiSpecificiRiscossione", SingoloVersamento.model().DATI_SPECIFICI_RISCOSSIONE.getFieldType(),
					jdbcParameterUtilities.readParameter(rs, "dati_specifici_riscossione", SingoloVersamento.model().DATI_SPECIFICI_RISCOSSIONE.getFieldType()));
				setParameter(object, "setStatoSingoloVersamento", SingoloVersamento.model().STATO_SINGOLO_VERSAMENTO.getFieldType(),
					jdbcParameterUtilities.readParameter(rs, "stato_singolo_versamento", SingoloVersamento.model().STATO_SINGOLO_VERSAMENTO.getFieldType()));
				setParameter(object, "setEsitoSingoloPagamento", SingoloVersamento.model().ESITO_SINGOLO_PAGAMENTO.getFieldType(),
					jdbcParameterUtilities.readParameter(rs, "esito_singolo_pagamento", SingoloVersamento.model().ESITO_SINGOLO_PAGAMENTO.getFieldType()));
				setParameter(object, "setDataEsitoSingoloPagamento", SingoloVersamento.model().DATA_ESITO_SINGOLO_PAGAMENTO.getFieldType(),
					jdbcParameterUtilities.readParameter(rs, "data_esito_singolo_pagamento", SingoloVersamento.model().DATA_ESITO_SINGOLO_PAGAMENTO.getFieldType()));
				setParameter(object, "setIur", SingoloVersamento.model().IUR.getFieldType(),
					jdbcParameterUtilities.readParameter(rs, "iur", SingoloVersamento.model().IUR.getFieldType()));
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

			if(model.equals(SingoloVersamento.model())){
				SingoloVersamento object = new SingoloVersamento();
				setParameter(object, "setId", Long.class,
					this.getObjectFromMap(map,"id"));
				setParameter(object, "setIndice", SingoloVersamento.model().INDICE.getFieldType(),
					this.getObjectFromMap(map,"indice"));
				setParameter(object, "setCodSingoloVersamentoEnte", SingoloVersamento.model().COD_SINGOLO_VERSAMENTO_ENTE.getFieldType(),
					this.getObjectFromMap(map,"codSingoloVersamentoEnte"));
				setParameter(object, "setAnnoRiferimento", SingoloVersamento.model().ANNO_RIFERIMENTO.getFieldType(),
					this.getObjectFromMap(map,"annoRiferimento"));
				setParameter(object, "setIbanAccredito", SingoloVersamento.model().IBAN_ACCREDITO.getFieldType(),
					this.getObjectFromMap(map,"ibanAccredito"));
				setParameter(object, "setImportoSingoloVersamento", SingoloVersamento.model().IMPORTO_SINGOLO_VERSAMENTO.getFieldType(),
					this.getObjectFromMap(map,"importoSingoloVersamento"));
				setParameter(object, "setImportoCommissioniPA", SingoloVersamento.model().IMPORTO_COMMISSIONI_PA.getFieldType(),
					this.getObjectFromMap(map,"importoCommissioniPA"));
				setParameter(object, "setSingoloImportoPagato", SingoloVersamento.model().SINGOLO_IMPORTO_PAGATO.getFieldType(),
					this.getObjectFromMap(map,"singoloImportoPagato"));
				setParameter(object, "setCausaleVersamento", SingoloVersamento.model().CAUSALE_VERSAMENTO.getFieldType(),
					this.getObjectFromMap(map,"causaleVersamento"));
				setParameter(object, "setDatiSpecificiRiscossione", SingoloVersamento.model().DATI_SPECIFICI_RISCOSSIONE.getFieldType(),
					this.getObjectFromMap(map,"datiSpecificiRiscossione"));
				setParameter(object, "setStatoSingoloVersamento", SingoloVersamento.model().STATO_SINGOLO_VERSAMENTO.getFieldType(),
					this.getObjectFromMap(map,"statoSingoloVersamento"));
				setParameter(object, "setEsitoSingoloPagamento", SingoloVersamento.model().ESITO_SINGOLO_PAGAMENTO.getFieldType(),
					this.getObjectFromMap(map,"esitoSingoloPagamento"));
				setParameter(object, "setDataEsitoSingoloPagamento", SingoloVersamento.model().DATA_ESITO_SINGOLO_PAGAMENTO.getFieldType(),
					this.getObjectFromMap(map,"dataEsitoSingoloPagamento"));
				setParameter(object, "setIur", SingoloVersamento.model().IUR.getFieldType(),
					this.getObjectFromMap(map,"iur"));
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

			if(model.equals(SingoloVersamento.model())){
				return new org.openspcoop2.utils.jdbc.CustomKeyGeneratorObject("singoli_versamenti","id","seq_singoli_versamenti","singoli_versamenti_init_seq");
			}
			
			else{
				throw new ServiceException("Model ["+model.toString()+"] not supported by getKeyGeneratorObject: "+this.getClass().getName());
			}

		}catch(Exception e){
			throw new ServiceException("Model ["+model.toString()+"] occurs error in getKeyGeneratorObject: "+e.getMessage(),e);
		}
		
	}

}
