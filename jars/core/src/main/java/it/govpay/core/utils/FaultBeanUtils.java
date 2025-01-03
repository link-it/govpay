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
package it.govpay.core.utils;

import it.govpay.core.exceptions.GovPayException;
import it.govpay.core.exceptions.GovPayException.FaultBean;


public class FaultBeanUtils {

	public static FaultBean toFaultBean(gov.telematici.pagamenti.ws.rpt.FaultBean faultBean) {
		if(faultBean == null) return null;
		
		FaultBean toRet = GovPayException.createFaultBean();
		
		toRet.setDescription(faultBean.getDescription());
		toRet.setFaultCode(faultBean.getFaultCode());
		toRet.setFaultString(faultBean.getFaultString());
		toRet.setId(faultBean.getId());
		toRet.setOriginalDescription(faultBean.getOriginalDescription());
		toRet.setOriginalFaultCode(faultBean.getOriginalFaultCode());
		toRet.setOriginalFaultString(faultBean.getOriginalFaultString());
		toRet.setSerial(faultBean.getSerial());
		return toRet;
	}
}
