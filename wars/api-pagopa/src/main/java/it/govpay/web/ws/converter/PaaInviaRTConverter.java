package it.govpay.web.ws.converter;

import gov.telematici.pagamenti.ws.rt.PaaInviaRT;

public class PaaInviaRTConverter {

	public static PaaInviaRT toPaaInviaRT_RT(gov.telematici.pagamenti.ws.ccp.PaaInviaRT paaInviaRT_CCP) {
		if(paaInviaRT_CCP == null) return null;
		
		PaaInviaRT paaInviaRT_RT = new PaaInviaRT();
		
		paaInviaRT_RT.setRt(paaInviaRT_CCP.getRt());
		paaInviaRT_RT.setTipoFirma(paaInviaRT_CCP.getTipoFirma());
		
		return paaInviaRT_RT;
	}
}
