/*
 * GovPay - Porta di Accesso al Nodo dei Pagamenti SPC 
 * http://www.gov4j.it/govpay
 * 
 * Copyright (c) 2014-2025 Link.it srl (http://www.link.it).
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
import org.openspcoop2.generic_project.dao.jdbc.utils.GenericJDBCParameterUtilities;
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
			GenericJDBCParameterUtilities jdbcParameterUtilities =  
					new GenericJDBCParameterUtilities(tipoDatabase);

			if(model.equals(SingoloVersamento.model())){
				SingoloVersamento object = new SingoloVersamento();
				setParameter(object, "setId", Long.class,
					jdbcParameterUtilities.readParameter(rs, "id", Long.class));
				setParameter(object, "setCodSingoloVersamentoEnte", SingoloVersamento.model().COD_SINGOLO_VERSAMENTO_ENTE.getFieldType(),
					jdbcParameterUtilities.readParameter(rs, "cod_singolo_versamento_ente", SingoloVersamento.model().COD_SINGOLO_VERSAMENTO_ENTE.getFieldType()));
				setParameter(object, "setStatoSingoloVersamento", SingoloVersamento.model().STATO_SINGOLO_VERSAMENTO.getFieldType(),
					jdbcParameterUtilities.readParameter(rs, "stato_singolo_versamento", SingoloVersamento.model().STATO_SINGOLO_VERSAMENTO.getFieldType()));
				setParameter(object, "setImportoSingoloVersamento", SingoloVersamento.model().IMPORTO_SINGOLO_VERSAMENTO.getFieldType(),
					jdbcParameterUtilities.readParameter(rs, "importo_singolo_versamento", SingoloVersamento.model().IMPORTO_SINGOLO_VERSAMENTO.getFieldType()));
				setParameter(object, "setTipoBollo", SingoloVersamento.model().TIPO_BOLLO.getFieldType(),
					jdbcParameterUtilities.readParameter(rs, "tipo_bollo", SingoloVersamento.model().TIPO_BOLLO.getFieldType()));
				setParameter(object, "setHashDocumento", SingoloVersamento.model().HASH_DOCUMENTO.getFieldType(),
					jdbcParameterUtilities.readParameter(rs, "hash_documento", SingoloVersamento.model().HASH_DOCUMENTO.getFieldType()));
				setParameter(object, "setProvinciaResidenza", SingoloVersamento.model().PROVINCIA_RESIDENZA.getFieldType(),
					jdbcParameterUtilities.readParameter(rs, "provincia_residenza", SingoloVersamento.model().PROVINCIA_RESIDENZA.getFieldType()));
				setParameter(object, "setTipoContabilita", SingoloVersamento.model().TIPO_CONTABILITA.getFieldType(),
					jdbcParameterUtilities.readParameter(rs, "tipo_contabilita", SingoloVersamento.model().TIPO_CONTABILITA.getFieldType()));
				setParameter(object, "setCodiceContabilita", SingoloVersamento.model().CODICE_CONTABILITA.getFieldType(),
					jdbcParameterUtilities.readParameter(rs, "codice_contabilita", SingoloVersamento.model().CODICE_CONTABILITA.getFieldType()));
				setParameter(object, "setDescrizione", SingoloVersamento.model().DESCRIZIONE.getFieldType(),
					jdbcParameterUtilities.readParameter(rs, "descrizione", SingoloVersamento.model().DESCRIZIONE.getFieldType()));
				setParameter(object, "setDatiAllegati", SingoloVersamento.model().DATI_ALLEGATI.getFieldType(),
					jdbcParameterUtilities.readParameter(rs, "dati_allegati", SingoloVersamento.model().DATI_ALLEGATI.getFieldType()));
				setParameter(object, "setIndiceDati", SingoloVersamento.model().INDICE_DATI.getFieldType(),
					jdbcParameterUtilities.readParameter(rs, "indice_dati", SingoloVersamento.model().INDICE_DATI.getFieldType(), org.openspcoop2.utils.jdbc.JDBCDefaultForXSDType.FORCE_ZERO_AS_NULL));
				setParameter(object, "setDescrizioneCausaleRPT", SingoloVersamento.model().DESCRIZIONE_CAUSALE_RPT.getFieldType(),
					jdbcParameterUtilities.readParameter(rs, "descrizione_causale_rpt", SingoloVersamento.model().DESCRIZIONE_CAUSALE_RPT.getFieldType()));
				setParameter(object, "setContabilita", SingoloVersamento.model().CONTABILITA.getFieldType(),
					jdbcParameterUtilities.readParameter(rs, "contabilita", SingoloVersamento.model().CONTABILITA.getFieldType()));
				setParameter(object, "setMetadata", SingoloVersamento.model().METADATA.getFieldType(),
					jdbcParameterUtilities.readParameter(rs, "metadata", SingoloVersamento.model().METADATA.getFieldType()));
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
				setParameter(object, "setCodSingoloVersamentoEnte", SingoloVersamento.model().COD_SINGOLO_VERSAMENTO_ENTE.getFieldType(),
					this.getObjectFromMap(map,"codSingoloVersamentoEnte"));
				setParameter(object, "setStatoSingoloVersamento", SingoloVersamento.model().STATO_SINGOLO_VERSAMENTO.getFieldType(),
					this.getObjectFromMap(map,"statoSingoloVersamento"));
				setParameter(object, "setImportoSingoloVersamento", SingoloVersamento.model().IMPORTO_SINGOLO_VERSAMENTO.getFieldType(),
					this.getObjectFromMap(map,"importoSingoloVersamento"));
				setParameter(object, "setTipoBollo", SingoloVersamento.model().TIPO_BOLLO.getFieldType(),
					this.getObjectFromMap(map,"tipoBollo"));
				setParameter(object, "setHashDocumento", SingoloVersamento.model().HASH_DOCUMENTO.getFieldType(),
					this.getObjectFromMap(map,"hashDocumento"));
				setParameter(object, "setProvinciaResidenza", SingoloVersamento.model().PROVINCIA_RESIDENZA.getFieldType(),
					this.getObjectFromMap(map,"provinciaResidenza"));
				setParameter(object, "setTipoContabilita", SingoloVersamento.model().TIPO_CONTABILITA.getFieldType(),
					this.getObjectFromMap(map,"tipoContabilita"));
				setParameter(object, "setCodiceContabilita", SingoloVersamento.model().CODICE_CONTABILITA.getFieldType(),
					this.getObjectFromMap(map,"codiceContabilita"));
				setParameter(object, "setDescrizione", SingoloVersamento.model().DESCRIZIONE.getFieldType(),
					this.getObjectFromMap(map,"descrizione"));
				setParameter(object, "setDatiAllegati", SingoloVersamento.model().DATI_ALLEGATI.getFieldType(),
					this.getObjectFromMap(map,"datiAllegati"));
				setParameter(object, "setIndiceDati", SingoloVersamento.model().INDICE_DATI.getFieldType(),
					this.getObjectFromMap(map,"indiceDati"));
				setParameter(object, "setDescrizioneCausaleRPT", SingoloVersamento.model().DESCRIZIONE_CAUSALE_RPT.getFieldType(),
					this.getObjectFromMap(map,"descrizioneCausaleRPT"));
				setParameter(object, "setContabilita", SingoloVersamento.model().CONTABILITA.getFieldType(),
					this.getObjectFromMap(map,"contabilita"));
				setParameter(object, "setMetadata", SingoloVersamento.model().METADATA.getFieldType(),
					this.getObjectFromMap(map,"metadata"));
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
