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

import it.govpay.orm.MailTemplate;


/**     
 * MailTemplateFetch
 *
 * @author Giovanni Bussu (bussu@link.it)
 * @author Lorenzo Nardi (nardi@link.it)
 * @author $Author$
 * @version $Rev$, $Date$
 */
public class MailTemplateFetch extends AbstractJDBCFetch {

	@Override
	public Object fetch(TipiDatabase tipoDatabase, IModel<?> model , ResultSet rs) throws ServiceException {
		
		try{
			JDBCParameterUtilities jdbcParameterUtilities =  
					new JDBCParameterUtilities(tipoDatabase);

			if(model.equals(MailTemplate.model())){
				MailTemplate object = new MailTemplate();
				setParameter(object, "setId", Long.class,
					jdbcParameterUtilities.readParameter(rs, "id", Long.class));
				setParameter(object, "setMittente", MailTemplate.model().MITTENTE.getFieldType(),
					jdbcParameterUtilities.readParameter(rs, "mittente", MailTemplate.model().MITTENTE.getFieldType()));
				setParameter(object, "setTemplateOggetto", MailTemplate.model().TEMPLATE_OGGETTO.getFieldType(),
					jdbcParameterUtilities.readParameter(rs, "template_oggetto", MailTemplate.model().TEMPLATE_OGGETTO.getFieldType()));
				setParameter(object, "setTemplateMessaggio", MailTemplate.model().TEMPLATE_MESSAGGIO.getFieldType(),
					jdbcParameterUtilities.readParameter(rs, "template_messaggio", MailTemplate.model().TEMPLATE_MESSAGGIO.getFieldType()));
				setParameter(object, "setAllegati", MailTemplate.model().ALLEGATI.getFieldType(),
					jdbcParameterUtilities.readParameter(rs, "allegati", MailTemplate.model().ALLEGATI.getFieldType()));
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

			if(model.equals(MailTemplate.model())){
				MailTemplate object = new MailTemplate();
				setParameter(object, "setId", Long.class,
					this.getObjectFromMap(map,"id"));
				setParameter(object, "setMittente", MailTemplate.model().MITTENTE.getFieldType(),
					this.getObjectFromMap(map,"mittente"));
				setParameter(object, "setTemplateOggetto", MailTemplate.model().TEMPLATE_OGGETTO.getFieldType(),
					this.getObjectFromMap(map,"templateOggetto"));
				setParameter(object, "setTemplateMessaggio", MailTemplate.model().TEMPLATE_MESSAGGIO.getFieldType(),
					this.getObjectFromMap(map,"templateMessaggio"));
				setParameter(object, "setAllegati", MailTemplate.model().ALLEGATI.getFieldType(),
					this.getObjectFromMap(map,"allegati"));
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

			if(model.equals(MailTemplate.model())){
				return new org.openspcoop2.utils.jdbc.CustomKeyGeneratorObject("mail_template","id","seq_mail_template","mail_template_init_seq");
			}
			
			else{
				throw new ServiceException("Model ["+model.toString()+"] not supported by getKeyGeneratorObject: "+this.getClass().getName());
			}

		}catch(Exception e){
			throw new ServiceException("Model ["+model.toString()+"] occurs error in getKeyGeneratorObject: "+e.getMessage(),e);
		}
		
	}

}
