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

import it.govpay.orm.Mail;


/**     
 * MailFetch
 *
 * @author Giovanni Bussu (bussu@link.it)
 * @author Lorenzo Nardi (nardi@link.it)
 * @author $Author$
 * @version $Rev$, $Date$
 */
public class MailFetch extends AbstractJDBCFetch {

	@Override
	public Object fetch(TipiDatabase tipoDatabase, IModel<?> model , ResultSet rs) throws ServiceException {
		
		try{
			JDBCParameterUtilities jdbcParameterUtilities =  
					new JDBCParameterUtilities(tipoDatabase);

			if(model.equals(Mail.model())){
				Mail object = new Mail();
				setParameter(object, "setId", Long.class,
					jdbcParameterUtilities.readParameter(rs, "id", Long.class));
				setParameter(object, "setTipoMail", Mail.model().TIPO_MAIL.getFieldType(),
					jdbcParameterUtilities.readParameter(rs, "tipo_mail", Mail.model().TIPO_MAIL.getFieldType()));
				setParameter(object, "setBundleKey", Mail.model().BUNDLE_KEY.getFieldType(),
					jdbcParameterUtilities.readParameter(rs, "bundle_key", Mail.model().BUNDLE_KEY.getFieldType()));
				setParameter(object, "setIdVersamento", Mail.model().ID_VERSAMENTO.getFieldType(),
					jdbcParameterUtilities.readParameter(rs, "id_versamento", Mail.model().ID_VERSAMENTO.getFieldType()));
				setParameter(object, "setMittente", Mail.model().MITTENTE.getFieldType(),
					jdbcParameterUtilities.readParameter(rs, "mittente", Mail.model().MITTENTE.getFieldType()));
				setParameter(object, "setDestinatario", Mail.model().DESTINATARIO.getFieldType(),
					jdbcParameterUtilities.readParameter(rs, "destinatario", Mail.model().DESTINATARIO.getFieldType()));
				setParameter(object, "setCc", Mail.model().CC.getFieldType(),
					jdbcParameterUtilities.readParameter(rs, "cc", Mail.model().CC.getFieldType()));
				setParameter(object, "setOggetto", Mail.model().OGGETTO.getFieldType(),
					jdbcParameterUtilities.readParameter(rs, "oggetto", Mail.model().OGGETTO.getFieldType()));
				setParameter(object, "setMessaggio", Mail.model().MESSAGGIO.getFieldType(),
					jdbcParameterUtilities.readParameter(rs, "messaggio", Mail.model().MESSAGGIO.getFieldType()));
				setParameter(object, "setStatoSpedizione", Mail.model().STATO_SPEDIZIONE.getFieldType(),
					jdbcParameterUtilities.readParameter(rs, "stato_spedizione", Mail.model().STATO_SPEDIZIONE.getFieldType()));
				setParameter(object, "setDettaglioErroreSpedizione", Mail.model().DETTAGLIO_ERRORE_SPEDIZIONE.getFieldType(),
					jdbcParameterUtilities.readParameter(rs, "dettaglio_errore_spedizione", Mail.model().DETTAGLIO_ERRORE_SPEDIZIONE.getFieldType()));
				setParameter(object, "setDataOraUltimaSpedizione", Mail.model().DATA_ORA_ULTIMA_SPEDIZIONE.getFieldType(),
					jdbcParameterUtilities.readParameter(rs, "data_ora_ultima_spedizione", Mail.model().DATA_ORA_ULTIMA_SPEDIZIONE.getFieldType()));
				setParameter(object, "setTentativiRispedizione", Mail.model().TENTATIVI_RISPEDIZIONE.getFieldType(),
					jdbcParameterUtilities.readParameter(rs, "tentativi_rispedizione", Mail.model().TENTATIVI_RISPEDIZIONE.getFieldType()));
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

			if(model.equals(Mail.model())){
				Mail object = new Mail();
				setParameter(object, "setId", Long.class,
					this.getObjectFromMap(map,"id"));
				setParameter(object, "setTipoMail", Mail.model().TIPO_MAIL.getFieldType(),
					this.getObjectFromMap(map,"tipoMail"));
				setParameter(object, "setBundleKey", Mail.model().BUNDLE_KEY.getFieldType(),
					this.getObjectFromMap(map,"bundleKey"));
				setParameter(object, "setIdVersamento", Mail.model().ID_VERSAMENTO.getFieldType(),
					this.getObjectFromMap(map,"idVersamento"));
				setParameter(object, "setMittente", Mail.model().MITTENTE.getFieldType(),
					this.getObjectFromMap(map,"mittente"));
				setParameter(object, "setDestinatario", Mail.model().DESTINATARIO.getFieldType(),
					this.getObjectFromMap(map,"destinatario"));
				setParameter(object, "setCc", Mail.model().CC.getFieldType(),
					this.getObjectFromMap(map,"cc"));
				setParameter(object, "setOggetto", Mail.model().OGGETTO.getFieldType(),
					this.getObjectFromMap(map,"oggetto"));
				setParameter(object, "setMessaggio", Mail.model().MESSAGGIO.getFieldType(),
					this.getObjectFromMap(map,"messaggio"));
				setParameter(object, "setStatoSpedizione", Mail.model().STATO_SPEDIZIONE.getFieldType(),
					this.getObjectFromMap(map,"statoSpedizione"));
				setParameter(object, "setDettaglioErroreSpedizione", Mail.model().DETTAGLIO_ERRORE_SPEDIZIONE.getFieldType(),
					this.getObjectFromMap(map,"dettaglioErroreSpedizione"));
				setParameter(object, "setDataOraUltimaSpedizione", Mail.model().DATA_ORA_ULTIMA_SPEDIZIONE.getFieldType(),
					this.getObjectFromMap(map,"dataOraUltimaSpedizione"));
				setParameter(object, "setTentativiRispedizione", Mail.model().TENTATIVI_RISPEDIZIONE.getFieldType(),
					this.getObjectFromMap(map,"tentativiRispedizione"));
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

			if(model.equals(Mail.model())){
				return new org.openspcoop2.utils.jdbc.CustomKeyGeneratorObject("mail","id","seq_mail","mail_init_seq");
			}
			
			else{
				throw new ServiceException("Model ["+model.toString()+"] not supported by getKeyGeneratorObject: "+this.getClass().getName());
			}

		}catch(Exception e){
			throw new ServiceException("Model ["+model.toString()+"] occurs error in getKeyGeneratorObject: "+e.getMessage(),e);
		}
		
	}

}
