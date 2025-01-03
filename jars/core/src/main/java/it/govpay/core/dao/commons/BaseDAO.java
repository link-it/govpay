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
package it.govpay.core.dao.commons;

import org.apache.commons.lang3.StringUtils;
import org.openspcoop2.utils.LoggerWrapperFactory;
import org.slf4j.Logger;
import org.springframework.security.core.Authentication;

import it.govpay.core.exceptions.NotAuthorizedException;
import it.govpay.core.utils.GovpayConfig;

public class BaseDAO {
	
	protected boolean useCacheData;
	protected Logger log = LoggerWrapperFactory.getLogger(BaseDAO.class);
	
	public BaseDAO() {
		this(true);
	}
	
	public BaseDAO(boolean useCacheData) {
		this.useCacheData = useCacheData;
	}
	
	public void checkCFDebitoreVersamento(Authentication authentication, String cfToCheck, String idDebitore) throws NotAuthorizedException{
		if(GovpayConfig.getInstance().isCheckCfDebitore()) {
			if(StringUtils.isEmpty(cfToCheck) || !cfToCheck.equalsIgnoreCase(idDebitore)) {
				throw new NotAuthorizedException("Utenza non autorizzata, il codice fiscale del debitore non corrisponde a quello indicato nella pendenza.");
			}
		}
	}
	
	public void logError(String message, Throwable t) {
		this.log.error(message,t);
	}
}
