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

import it.govpay.orm.Dominio;


/**     
 * DominioFetch
 *
 * @author Giovanni Bussu (bussu@link.it)
 * @author Lorenzo Nardi (nardi@link.it)
 * @author $Author$
 * @version $Rev$, $Date$
 */
public class DominioFetch extends AbstractJDBCFetch {

	@Override
	public Object fetch(TipiDatabase tipoDatabase, IModel<?> model , ResultSet rs) throws ServiceException {
		
		try{
			JDBCParameterUtilities jdbcParameterUtilities =  
					new JDBCParameterUtilities(tipoDatabase);

			if(model.equals(Dominio.model())){
				Dominio object = new Dominio();
				setParameter(object, "setId", Long.class,
					jdbcParameterUtilities.readParameter(rs, "id", Long.class));
				setParameter(object, "setCodDominio", Dominio.model().COD_DOMINIO.getFieldType(),
					jdbcParameterUtilities.readParameter(rs, "cod_dominio", Dominio.model().COD_DOMINIO.getFieldType()));
				setParameter(object, "setGln", Dominio.model().GLN.getFieldType(),
					jdbcParameterUtilities.readParameter(rs, "gln", Dominio.model().GLN.getFieldType()));
				setParameter(object, "setAbilitato", Dominio.model().ABILITATO.getFieldType(),
					jdbcParameterUtilities.readParameter(rs, "abilitato", Dominio.model().ABILITATO.getFieldType()));
				setParameter(object, "setRagioneSociale", Dominio.model().RAGIONE_SOCIALE.getFieldType(),
					jdbcParameterUtilities.readParameter(rs, "ragione_sociale", Dominio.model().RAGIONE_SOCIALE.getFieldType()));
				setParameter(object, "setXmlContiAccredito", Dominio.model().XML_CONTI_ACCREDITO.getFieldType(),
					jdbcParameterUtilities.readParameter(rs, "xml_conti_accredito", Dominio.model().XML_CONTI_ACCREDITO.getFieldType()));
				setParameter(object, "setXmlTabellaControparti", Dominio.model().XML_TABELLA_CONTROPARTI.getFieldType(),
					jdbcParameterUtilities.readParameter(rs, "xml_tabella_controparti", Dominio.model().XML_TABELLA_CONTROPARTI.getFieldType()));
				setParameter(object, "setRiusoIUV", Dominio.model().RIUSO_IUV.getFieldType(),
					jdbcParameterUtilities.readParameter(rs, "riuso_iuv", Dominio.model().RIUSO_IUV.getFieldType()));
				setParameter(object, "setCustomIUV", Dominio.model().CUSTOM_IUV.getFieldType(),
					jdbcParameterUtilities.readParameter(rs, "custom_iuv", Dominio.model().CUSTOM_IUV.getFieldType()));
				setParameter(object, "setAuxDigit", Dominio.model().AUX_DIGIT.getFieldType(),
					jdbcParameterUtilities.readParameter(rs, "aux_digit", Dominio.model().AUX_DIGIT.getFieldType()));
				setParameter(object, "setIuvPrefix", Dominio.model().IUV_PREFIX.getFieldType(),
					jdbcParameterUtilities.readParameter(rs, "iuv_prefix", Dominio.model().IUV_PREFIX.getFieldType()));
				setParameter(object, "setIuvPrefixStrict", Dominio.model().IUV_PREFIX_STRICT.getFieldType(),
					jdbcParameterUtilities.readParameter(rs, "iuv_prefix_strict", Dominio.model().IUV_PREFIX_STRICT.getFieldType()));
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

			if(model.equals(Dominio.model())){
				Dominio object = new Dominio();
				setParameter(object, "setId", Long.class,
					this.getObjectFromMap(map,"id"));
				setParameter(object, "setCodDominio", Dominio.model().COD_DOMINIO.getFieldType(),
					this.getObjectFromMap(map,"codDominio"));
				setParameter(object, "setGln", Dominio.model().GLN.getFieldType(),
					this.getObjectFromMap(map,"gln"));
				setParameter(object, "setAbilitato", Dominio.model().ABILITATO.getFieldType(),
					this.getObjectFromMap(map,"abilitato"));
				setParameter(object, "setRagioneSociale", Dominio.model().RAGIONE_SOCIALE.getFieldType(),
					this.getObjectFromMap(map,"ragioneSociale"));
				setParameter(object, "setXmlContiAccredito", Dominio.model().XML_CONTI_ACCREDITO.getFieldType(),
					this.getObjectFromMap(map,"xmlContiAccredito"));
				setParameter(object, "setXmlTabellaControparti", Dominio.model().XML_TABELLA_CONTROPARTI.getFieldType(),
					this.getObjectFromMap(map,"xmlTabellaControparti"));
				setParameter(object, "setRiusoIUV", Dominio.model().RIUSO_IUV.getFieldType(),
					this.getObjectFromMap(map,"riusoIUV"));
				setParameter(object, "setCustomIUV", Dominio.model().CUSTOM_IUV.getFieldType(),
					this.getObjectFromMap(map,"customIUV"));
				setParameter(object, "setAuxDigit", Dominio.model().AUX_DIGIT.getFieldType(),
					this.getObjectFromMap(map,"auxDigit"));
				setParameter(object, "setIuvPrefix", Dominio.model().IUV_PREFIX.getFieldType(),
					this.getObjectFromMap(map,"iuvPrefix"));
				setParameter(object, "setIuvPrefixStrict", Dominio.model().IUV_PREFIX_STRICT.getFieldType(),
					this.getObjectFromMap(map,"iuvPrefixStrict"));
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

			if(model.equals(Dominio.model())){
				return new org.openspcoop2.utils.jdbc.CustomKeyGeneratorObject("domini","id","seq_domini","domini_init_seq");
			}
			
			else{
				throw new ServiceException("Model ["+model.toString()+"] not supported by getKeyGeneratorObject: "+this.getClass().getName());
			}

		}catch(Exception e){
			throw new ServiceException("Model ["+model.toString()+"] occurs error in getKeyGeneratorObject: "+e.getMessage(),e);
		}
		
	}

}
