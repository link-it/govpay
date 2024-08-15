/*
 * GovPay - Porta di Accesso al Nodo dei Pagamenti SPC 
 * http://www.gov4j.it/govpay
 * 
 * Copyright (c) 2014-2024 Link.it srl (http://www.link.it).
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
			GenericJDBCParameterUtilities jdbcParameterUtilities =  
					new GenericJDBCParameterUtilities(tipoDatabase);

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
				setParameter(object, "setAuxDigit", Dominio.model().AUX_DIGIT.getFieldType(),
					jdbcParameterUtilities.readParameter(rs, "aux_digit", Dominio.model().AUX_DIGIT.getFieldType()));
				setParameter(object, "setIuvPrefix", Dominio.model().IUV_PREFIX.getFieldType(),
					jdbcParameterUtilities.readParameter(rs, "iuv_prefix", Dominio.model().IUV_PREFIX.getFieldType()));
				setParameter(object, "setSegregationCode", Dominio.model().SEGREGATION_CODE.getFieldType(),
					jdbcParameterUtilities.readParameter(rs, "segregation_code", Dominio.model().SEGREGATION_CODE.getFieldType()));
				setParameter(object, "setLogo", Dominio.model().LOGO.getFieldType(),
					jdbcParameterUtilities.readParameter(rs, "logo", Dominio.model().LOGO.getFieldType()));
				setParameter(object, "setCbill", Dominio.model().CBILL.getFieldType(),
					jdbcParameterUtilities.readParameter(rs, "cbill", Dominio.model().CBILL.getFieldType()));
				setParameter(object, "setAutStampaPoste", Dominio.model().AUT_STAMPA_POSTE.getFieldType(),
					jdbcParameterUtilities.readParameter(rs, "aut_stampa_poste", Dominio.model().AUT_STAMPA_POSTE.getFieldType()));
				setParameter(object, "setCodConnettoreMyPivot", Dominio.model().COD_CONNETTORE_MY_PIVOT.getFieldType(),
					jdbcParameterUtilities.readParameter(rs, "cod_connettore_my_pivot", Dominio.model().COD_CONNETTORE_MY_PIVOT.getFieldType()));
				setParameter(object, "setCodConnettoreSecim", Dominio.model().COD_CONNETTORE_SECIM.getFieldType(),
					jdbcParameterUtilities.readParameter(rs, "cod_connettore_secim", Dominio.model().COD_CONNETTORE_SECIM.getFieldType()));
				setParameter(object, "setCodConnettoreGovPay", Dominio.model().COD_CONNETTORE_GOV_PAY.getFieldType(),
					jdbcParameterUtilities.readParameter(rs, "cod_connettore_gov_pay", Dominio.model().COD_CONNETTORE_GOV_PAY.getFieldType()));
				setParameter(object, "setCodConnettoreHyperSicAPK", Dominio.model().COD_CONNETTORE_HYPER_SIC_APK.getFieldType(),
					jdbcParameterUtilities.readParameter(rs, "cod_connettore_hyper_sic_apk", Dominio.model().COD_CONNETTORE_HYPER_SIC_APK.getFieldType()));
				setParameter(object, "setCodConnettoreMaggioliJPPA", Dominio.model().COD_CONNETTORE_MAGGIOLI_JPPA.getFieldType(),
					jdbcParameterUtilities.readParameter(rs, "cod_connettore_maggioli_jppa", Dominio.model().COD_CONNETTORE_MAGGIOLI_JPPA.getFieldType()));
				setParameter(object, "setIntermediato", Dominio.model().INTERMEDIATO.getFieldType(),
					jdbcParameterUtilities.readParameter(rs, "intermediato", Dominio.model().INTERMEDIATO.getFieldType()));
				setParameter(object, "setTassonomiaPagoPA", Dominio.model().TASSONOMIA_PAGO_PA.getFieldType(),
					jdbcParameterUtilities.readParameter(rs, "tassonomia_pago_pa", Dominio.model().TASSONOMIA_PAGO_PA.getFieldType()));
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
				setParameter(object, "setAuxDigit", Dominio.model().AUX_DIGIT.getFieldType(),
					this.getObjectFromMap(map,"auxDigit"));
				setParameter(object, "setIuvPrefix", Dominio.model().IUV_PREFIX.getFieldType(),
					this.getObjectFromMap(map,"iuvPrefix"));
				setParameter(object, "setSegregationCode", Dominio.model().SEGREGATION_CODE.getFieldType(),
					this.getObjectFromMap(map,"segregationCode"));
				setParameter(object, "setLogo", Dominio.model().LOGO.getFieldType(),
					this.getObjectFromMap(map,"logo"));
				setParameter(object, "setCbill", Dominio.model().CBILL.getFieldType(),
					this.getObjectFromMap(map,"cbill"));
				setParameter(object, "setAutStampaPoste", Dominio.model().AUT_STAMPA_POSTE.getFieldType(),
					this.getObjectFromMap(map,"autStampaPoste"));
				setParameter(object, "setCodConnettoreMyPivot", Dominio.model().COD_CONNETTORE_MY_PIVOT.getFieldType(),
					this.getObjectFromMap(map,"codConnettoreMyPivot"));
				setParameter(object, "setCodConnettoreSecim", Dominio.model().COD_CONNETTORE_SECIM.getFieldType(),
					this.getObjectFromMap(map,"codConnettoreSecim"));
				setParameter(object, "setCodConnettoreGovPay", Dominio.model().COD_CONNETTORE_GOV_PAY.getFieldType(),
					this.getObjectFromMap(map,"codConnettoreGovPay"));
				setParameter(object, "setCodConnettoreHyperSicAPK", Dominio.model().COD_CONNETTORE_HYPER_SIC_APK.getFieldType(),
					this.getObjectFromMap(map,"codConnettoreHyperSicAPK"));
				setParameter(object, "setCodConnettoreMaggioliJPPA", Dominio.model().COD_CONNETTORE_MAGGIOLI_JPPA.getFieldType(),
					this.getObjectFromMap(map,"codConnettoreMaggioliJPPA"));
				setParameter(object, "setIntermediato", Dominio.model().INTERMEDIATO.getFieldType(),
					this.getObjectFromMap(map,"intermediato"));
				setParameter(object, "setTassonomiaPagoPA", Dominio.model().TASSONOMIA_PAGO_PA.getFieldType(),
					this.getObjectFromMap(map,"tassonomiaPagoPA"));
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
