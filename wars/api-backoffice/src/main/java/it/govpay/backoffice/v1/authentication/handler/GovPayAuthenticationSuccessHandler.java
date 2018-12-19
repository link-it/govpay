package it.govpay.backoffice.v1.authentication.handler;

import java.util.UUID;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

import org.springframework.security.core.Authentication;

import it.govpay.backoffice.v1.beans.Profilo;
import it.govpay.backoffice.v1.beans.converter.ProfiloConverter;
import it.govpay.core.beans.Costanti;
import it.govpay.core.dao.anagrafica.UtentiDAO;
import it.govpay.core.dao.anagrafica.dto.LeggiProfiloDTOResponse;
import it.govpay.core.utils.GpContext;
import it.govpay.rs.v1.exception.CodiceEccezione;

public class GovPayAuthenticationSuccessHandler extends org.openspcoop2.utils.jaxrs.impl.authentication.handler.DefaultAuthenticationSuccessHandler{

	@Override
	public Response getPayload(HttpServletRequest request, HttpServletResponse res, Authentication authentication) {
		//String methodName = "GovPayAuthenticationSuccessHandler.getPayload";  
		GpContext ctx = null;
		String transactionId = null;
		try{
			ctx = new GpContext(UUID.randomUUID().toString());
			transactionId = ctx.getTransactionId();
			
			// Parametri - > DTO Input

			// INIT DAO
			
			UtentiDAO utentiDAO = new UtentiDAO();
			
			// CHIAMATA AL DAO
			
			LeggiProfiloDTOResponse leggiProfilo = utentiDAO.getProfilo(authentication);
			
			// CONVERT TO JSON DELLA RISPOSTA

			Profilo profilo = ProfiloConverter.getProfilo(leggiProfilo);
			
			return Response.status(Status.OK).entity(profilo.toJSON(null)).header(Costanti.HEADER_NAME_OUTPUT_TRANSACTION_ID, transactionId).build();
			
		}catch (Exception e) {
			return CodiceEccezione.AUTORIZZAZIONE.toFaultResponse(e);
		} finally {
			if(ctx != null) ctx.log();
		}
	}
}
