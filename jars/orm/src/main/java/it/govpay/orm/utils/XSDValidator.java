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


package it.govpay.orm.utils;

import org.slf4j.Logger;
import org.openspcoop2.utils.xml.AbstractValidatoreXSD;
import org.openspcoop2.generic_project.exception.ServiceException;

import it.govpay.orm.Configurazione;

/** 
 * XSD Validator    
 *
 * @author Giovanni Bussu (bussu@link.it)
 * @author Lorenzo Nardi (nardi@link.it)
 * @author $Author$
 * @version $Rev$, $Date$
 */

public class XSDValidator {

	private static org.openspcoop2.generic_project.utils.XSDValidator validator = null;
	
	private static synchronized void initValidator(Class<?> validatorImpl,Logger log) throws ServiceException{
		if(validator==null){
			validator = new org.openspcoop2.generic_project.utils.XSDValidator(log,Configurazione.class, 
				"/govpay.xsd"
				// elencare in questa posizione altri schemi xsd che vengono inclusi/importati dallo schema /govpay.xsd
			);
		}
	}
	
	public static AbstractValidatoreXSD getXSDValidator(Class<?> validatorImpl,Logger log) throws ServiceException{
		if(validator==null){
			initValidator(validatorImpl,log);
		}
		return validator.getXsdValidator();
	}
	public static AbstractValidatoreXSD getXSDValidator(Logger log) throws ServiceException{
		if(validator==null){
			initValidator(org.openspcoop2.utils.xml.ValidatoreXSD.class,log);
		}
		return validator.getXsdValidator();
	}
	
}
