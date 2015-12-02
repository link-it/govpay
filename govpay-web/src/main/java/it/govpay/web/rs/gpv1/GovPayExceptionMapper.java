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
package it.govpay.web.rs.gpv1;

import it.govpay.exception.GovPayException;
import it.govpay.exception.GovPayException.GovPayExceptionEnum;
import it.govpay.exception.GovPayNdpException;
import it.govpay.rs.Errore;

import javax.ws.rs.core.Context;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;

@Provider
public class GovPayExceptionMapper implements ExceptionMapper<GovPayException>{

    @Context
    private HttpHeaders headers;
    
    public Response toResponse(GovPayException e) {
    	
    	if(e instanceof GovPayNdpException) {
    		Errore errore = new Errore();
        	errore.setCodiceErrore(GovPayExceptionEnum.ERRORE_NDP.name());
        	errore.setDescrizione(((GovPayNdpException) e).getFaultCode().name() + ":" + ((GovPayNdpException) e).getDescrizione());
        	
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).
                    entity(errore).
                    type(headers.getMediaType()).
                    build();
    	}
    	
    	Errore errore = new Errore();
    	switch (e.getTipoException()) {
	    	case BENEFICIARIO_NON_TROVATO:
	    	case ERRORE_CONFIGURAZIONE:
	    	case IUV_DUPLICATO:
	    	case PSP_NON_TROVATO:
	    	case ERRORE_ENTE_WEB:
	    	case ENTE_NON_TROVATO:
	    	case STORNO_NON_CONSENTITO:
	    	case IUV_NON_TROVATO:
	    	case APPLICAZIONE_DISABILITATA:
	    	case APPLICAZIONE_NON_TROVATA:
	    	case ERRORE_AUTENTICAZIONE:
	    	case ERRORE_AUTORIZZAZIONE:
	    	case IUSV_DUPLICATO:
	    	case TRIBUTO_NON_TROVATO:
	    	case PSP_NON_ATTIVO:
	    		errore.setCodiceErrore(e.getTipoException().name());
			break;
	    	case ERRORE_INTERNO:
	    		errore.setCodiceErrore(GovPayExceptionEnum.ERRORE_INTERNO.name());
	    	break;	
	    	case ERRORE_NDP:
	    	case ERRORE_NDP_WEB:
	    		errore.setCodiceErrore(GovPayExceptionEnum.ERRORE_NDP.name());
	    	break;	
	    	case ERRORE_VALIDAZIONE_NDP:
	    	case ERRORE_VALIDAZIONE:
	    		errore.setCodiceErrore(GovPayExceptionEnum.ERRORE_VALIDAZIONE.name());
	    	break;
    	}    	
    	if(e.getDescrizione() != null)
    		errore.setDescrizione(e.getDescrizione());
    	else
    		errore.setDescrizione(e.getMessage());
    	
    	if(e.getCause() != null)
    		errore.setDescrizioneCausa(e.getCause().getMessage());
    	
        return Response.status(Response.Status.INTERNAL_SERVER_ERROR).
                entity(errore).
                type(headers.getMediaType()).
                build();
    }

}

