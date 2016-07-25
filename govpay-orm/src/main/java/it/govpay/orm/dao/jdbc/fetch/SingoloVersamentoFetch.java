/*
 * GovPay - Porta di Accesso al Nodo dei Pagamenti SPC 
 * http://www.gov4j.it/govpay
 * 
 * Copyright (c) 2014-2016 Link.it srl (http://www.link.it).
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
				setParameter(object, "setCodSingoloVersamentoEnte", SingoloVersamento.model().COD_SINGOLO_VERSAMENTO_ENTE.getFieldType(),
					jdbcParameterUtilities.readParameter(rs, "cod_singolo_versamento_ente", SingoloVersamento.model().COD_SINGOLO_VERSAMENTO_ENTE.getFieldType()));
				setParameter(object, "setStatoSingoloVersamento", SingoloVersamento.model().STATO_SINGOLO_VERSAMENTO.getFieldType(),
					jdbcParameterUtilities.readParameter(rs, "stato_singolo_versamento", SingoloVersamento.model().STATO_SINGOLO_VERSAMENTO.getFieldType()));
				setParameter(object, "setImportoSingoloVersamento", SingoloVersamento.model().IMPORTO_SINGOLO_VERSAMENTO.getFieldType(),
					jdbcParameterUtilities.readParameter(rs, "importo_singolo_versamento", SingoloVersamento.model().IMPORTO_SINGOLO_VERSAMENTO.getFieldType()));
				setParameter(object, "setAnnoRiferimento", SingoloVersamento.model().ANNO_RIFERIMENTO.getFieldType(),
					jdbcParameterUtilities.readParameter(rs, "anno_riferimento", SingoloVersamento.model().ANNO_RIFERIMENTO.getFieldType()));
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
				setParameter(object, "setNote", SingoloVersamento.model().NOTE.getFieldType(),
					jdbcParameterUtilities.readParameter(rs, "note", SingoloVersamento.model().NOTE.getFieldType()));
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
				setParameter(object, "setAnnoRiferimento", SingoloVersamento.model().ANNO_RIFERIMENTO.getFieldType(),
					this.getObjectFromMap(map,"annoRiferimento"));
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
				setParameter(object, "setNote", SingoloVersamento.model().NOTE.getFieldType(),
					this.getObjectFromMap(map,"note"));
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
