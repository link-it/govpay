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
package it.govpay.web.ws.converter;

import gov.telematici.pagamenti.ws.ccp.EsitoPaaInviaRT;
import gov.telematici.pagamenti.ws.ccp.FaultBean;
import gov.telematici.pagamenti.ws.ccp.PaaInviaRTRisposta;

public class PaaInviaRTRispostaConverter {

	public static PaaInviaRTRisposta toPaaInviaRTRisposta_CCP(gov.telematici.pagamenti.ws.rt.PaaInviaRTRisposta paaInviaRTRisposta_RT) {
		if(paaInviaRTRisposta_RT == null) return null;
		
		PaaInviaRTRisposta paaInviaRTRisposta_CCP = new PaaInviaRTRisposta();
		
		paaInviaRTRisposta_CCP.setPaaInviaRTRisposta(toEsitoPaaInviaRT_CCP(paaInviaRTRisposta_RT.getPaaInviaRTRisposta()));
		
		return paaInviaRTRisposta_CCP;
	}

	private static EsitoPaaInviaRT toEsitoPaaInviaRT_CCP(gov.telematici.pagamenti.ws.rt.EsitoPaaInviaRT paaInviaRTRisposta_RT) {
		if(paaInviaRTRisposta_RT == null) return null;
		
		EsitoPaaInviaRT esitoPaaInviaRT_CCP = new EsitoPaaInviaRT();
		
		esitoPaaInviaRT_CCP.setEsito(paaInviaRTRisposta_RT.getEsito());
		esitoPaaInviaRT_CCP.setFault(toFaultBean_CCP(paaInviaRTRisposta_RT.getFault()));
		
		return esitoPaaInviaRT_CCP;
	}

	private static FaultBean toFaultBean_CCP(gov.telematici.pagamenti.ws.rt.FaultBean fault_RT) {
		if(fault_RT == null) return null;
		
		FaultBean fault_CCP = new FaultBean();
		
		fault_CCP.setDescription(fault_RT.getDescription());
		fault_CCP.setFaultCode(fault_RT.getFaultCode());
		fault_CCP.setFaultString(fault_RT.getFaultString());
		fault_CCP.setId(fault_RT.getId());
		fault_CCP.setSerial(fault_RT.getSerial());
		
		return fault_CCP;
	}
}
