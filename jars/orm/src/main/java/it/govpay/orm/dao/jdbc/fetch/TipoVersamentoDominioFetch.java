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

import it.govpay.orm.TipoVersamento;
import it.govpay.orm.TipoVersamentoDominio;


/**     
 * TipoVersamentoDominioFetch
 *
 * @author Giovanni Bussu (bussu@link.it)
 * @author Lorenzo Nardi (nardi@link.it)
 * @author $Author$
 * @version $Rev$, $Date$
 */
public class TipoVersamentoDominioFetch extends AbstractJDBCFetch {

	@Override
	public Object fetch(TipiDatabase tipoDatabase, IModel<?> model , ResultSet rs) throws ServiceException {
		
		try{
			JDBCParameterUtilities jdbcParameterUtilities =  
					new JDBCParameterUtilities(tipoDatabase);

			if(model.equals(TipoVersamentoDominio.model())){
				TipoVersamentoDominio object = new TipoVersamentoDominio();
				setParameter(object, "setId", Long.class,
					jdbcParameterUtilities.readParameter(rs, "id", Long.class));
				setParameter(object, "setCodificaIuv", TipoVersamentoDominio.model().CODIFICA_IUV.getFieldType(),
					jdbcParameterUtilities.readParameter(rs, "codifica_iuv", TipoVersamentoDominio.model().CODIFICA_IUV.getFieldType()));
				setParameter(object, "setTipo", TipoVersamentoDominio.model().TIPO.getFieldType(),
					jdbcParameterUtilities.readParameter(rs, "tipo", TipoVersamentoDominio.model().TIPO.getFieldType()));
				setParameter(object, "setPagaTerzi", TipoVersamentoDominio.model().PAGA_TERZI.getFieldType(),
					jdbcParameterUtilities.readParameter(rs, "paga_terzi", TipoVersamentoDominio.model().PAGA_TERZI.getFieldType()));
				setParameter(object, "setAbilitato", TipoVersamentoDominio.model().ABILITATO.getFieldType(),
					jdbcParameterUtilities.readParameter(rs, "abilitato", TipoVersamentoDominio.model().ABILITATO.getFieldType()));
				setParameter(object, "setJsonSchema", TipoVersamentoDominio.model().JSON_SCHEMA.getFieldType(),
					jdbcParameterUtilities.readParameter(rs, "json_schema", TipoVersamentoDominio.model().JSON_SCHEMA.getFieldType()));
				setParameter(object, "setDatiAllegati", TipoVersamentoDominio.model().DATI_ALLEGATI.getFieldType(),
					jdbcParameterUtilities.readParameter(rs, "dati_allegati", TipoVersamentoDominio.model().DATI_ALLEGATI.getFieldType()));
				return object;
			} else if(model.equals(TipoVersamentoDominio.model().TIPO_VERSAMENTO)) {
				TipoVersamento object = new TipoVersamento();
				setParameter(object, "setId", Long.class,
					jdbcParameterUtilities.readParameter(rs, "id", Long.class));
				setParameter(object, "setCodTipoVersamento", TipoVersamentoDominio.model().TIPO_VERSAMENTO.COD_TIPO_VERSAMENTO.getFieldType(),
					jdbcParameterUtilities.readParameter(rs, "cod_tipo_versamento", TipoVersamentoDominio.model().TIPO_VERSAMENTO.COD_TIPO_VERSAMENTO.getFieldType()));
				setParameter(object, "setDescrizione", TipoVersamentoDominio.model().TIPO_VERSAMENTO.DESCRIZIONE.getFieldType(),
					jdbcParameterUtilities.readParameter(rs, "descrizione", TipoVersamentoDominio.model().TIPO_VERSAMENTO.DESCRIZIONE.getFieldType()));
				setParameter(object, "setCodificaIuv", TipoVersamentoDominio.model().TIPO_VERSAMENTO.CODIFICA_IUV.getFieldType(),
					jdbcParameterUtilities.readParameter(rs, "codifica_iuv", TipoVersamentoDominio.model().TIPO_VERSAMENTO.CODIFICA_IUV.getFieldType()));
				setParameter(object, "setTipo", TipoVersamentoDominio.model().TIPO_VERSAMENTO.TIPO.getFieldType(),
					jdbcParameterUtilities.readParameter(rs, "tipo", TipoVersamentoDominio.model().TIPO_VERSAMENTO.TIPO.getFieldType()));
				setParameter(object, "setPagaTerzi", TipoVersamentoDominio.model().TIPO_VERSAMENTO.PAGA_TERZI.getFieldType(),
					jdbcParameterUtilities.readParameter(rs, "paga_terzi", TipoVersamentoDominio.model().TIPO_VERSAMENTO.PAGA_TERZI.getFieldType()));
				setParameter(object, "setAbilitato", TipoVersamentoDominio.model().TIPO_VERSAMENTO.ABILITATO.getFieldType(),
						jdbcParameterUtilities.readParameter(rs, "abilitato", TipoVersamentoDominio.model().TIPO_VERSAMENTO.ABILITATO.getFieldType()));
				setParameter(object, "setJsonSchema", TipoVersamentoDominio.model().TIPO_VERSAMENTO.JSON_SCHEMA.getFieldType(),
					jdbcParameterUtilities.readParameter(rs, "json_schema", TipoVersamentoDominio.model().TIPO_VERSAMENTO.JSON_SCHEMA.getFieldType()));
				setParameter(object, "setDatiAllegati", TipoVersamentoDominio.model().TIPO_VERSAMENTO.DATI_ALLEGATI.getFieldType(),
					jdbcParameterUtilities.readParameter(rs, "dati_allegati", TipoVersamentoDominio.model().TIPO_VERSAMENTO.DATI_ALLEGATI.getFieldType()));
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

			if(model.equals(TipoVersamentoDominio.model())){
				TipoVersamentoDominio object = new TipoVersamentoDominio();
				setParameter(object, "setId", Long.class,
					this.getObjectFromMap(map,"id"));
				setParameter(object, "setCodificaIuv", TipoVersamentoDominio.model().CODIFICA_IUV.getFieldType(),
					this.getObjectFromMap(map,"codificaIuv"));
				setParameter(object, "setTipo", TipoVersamentoDominio.model().TIPO.getFieldType(),
					this.getObjectFromMap(map,"tipo"));
				setParameter(object, "setPagaTerzi", TipoVersamentoDominio.model().PAGA_TERZI.getFieldType(),
					this.getObjectFromMap(map,"pagaTerzi"));
				setParameter(object, "setAbilitato", TipoVersamentoDominio.model().ABILITATO.getFieldType(),
					this.getObjectFromMap(map,"abilitato"));
				setParameter(object, "setJsonSchema", TipoVersamentoDominio.model().JSON_SCHEMA.getFieldType(),
					this.getObjectFromMap(map,"jsonSchema"));
				setParameter(object, "setDatiAllegati", TipoVersamentoDominio.model().DATI_ALLEGATI.getFieldType(),
					this.getObjectFromMap(map,"datiAllegati"));
				return object;
			} else if(model.equals(TipoVersamentoDominio.model().TIPO_VERSAMENTO)) {
				TipoVersamento object = new TipoVersamento();
				setParameter(object, "setId", Long.class,
					this.getObjectFromMap(map,"tipoVersamento.id"));
				setParameter(object, "setCodTipoVersamento", TipoVersamentoDominio.model().TIPO_VERSAMENTO.COD_TIPO_VERSAMENTO.getFieldType(),
					this.getObjectFromMap(map,"tipoVersamento.codTipoVersamento"));
				setParameter(object, "setDescrizione", TipoVersamentoDominio.model().TIPO_VERSAMENTO.DESCRIZIONE.getFieldType(),
					this.getObjectFromMap(map,"tipoVersamento.descrizione"));
				setParameter(object, "setCodificaIuv", TipoVersamentoDominio.model().TIPO_VERSAMENTO.CODIFICA_IUV.getFieldType(),
					this.getObjectFromMap(map,"tipoVersamento.codificaIuv"));
				setParameter(object, "setTipo", TipoVersamentoDominio.model().TIPO_VERSAMENTO.TIPO.getFieldType(),
					this.getObjectFromMap(map,"tipoVersamento.tipo"));
				setParameter(object, "setPagaTerzi", TipoVersamentoDominio.model().TIPO_VERSAMENTO.PAGA_TERZI.getFieldType(),
					this.getObjectFromMap(map,"tipoVersamento.pagaTerzi"));
				setParameter(object, "setAbilitato", TipoVersamentoDominio.model().TIPO_VERSAMENTO.ABILITATO.getFieldType(),
						this.getObjectFromMap(map,"tipoVersamento.abilitato"));
				setParameter(object, "setJsonSchema", TipoVersamentoDominio.model().TIPO_VERSAMENTO.JSON_SCHEMA.getFieldType(),
					this.getObjectFromMap(map,"tipoVersamento.jsonSchema"));
				setParameter(object, "setDatiAllegati", TipoVersamentoDominio.model().TIPO_VERSAMENTO.DATI_ALLEGATI.getFieldType(),
					this.getObjectFromMap(map,"tipoVersamento.datiAllegati"));
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

			if(model.equals(TipoVersamentoDominio.model())){
				return new org.openspcoop2.utils.jdbc.CustomKeyGeneratorObject("tipi_vers_domini","id","seq_tipi_vers_domini","tipi_vers_domini_init_seq");
			}
			
			else{
				throw new ServiceException("Model ["+model.toString()+"] not supported by getKeyGeneratorObject: "+this.getClass().getName());
			}

		}catch(Exception e){
			throw new ServiceException("Model ["+model.toString()+"] occurs error in getKeyGeneratorObject: "+e.getMessage(),e);
		}
		
	}

}
