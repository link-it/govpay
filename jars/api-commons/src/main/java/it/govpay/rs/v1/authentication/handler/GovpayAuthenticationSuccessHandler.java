package it.govpay.rs.v1.authentication.handler;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.core.Response;

import org.springframework.security.core.Authentication;

import it.govpay.bd.model.Operatore;
import it.govpay.core.autorizzazione.beans.GovpayLdapUserDetails;
import it.govpay.core.autorizzazione.utils.AutorizzazioneUtils;

public class GovpayAuthenticationSuccessHandler extends DefaultAuthenticationSuccessHandler{

	@Override
	public Response getPayload(HttpServletRequest request, HttpServletResponse res, Authentication authentication) {
		GovpayLdapUserDetails userDetails = AutorizzazioneUtils.getAuthenticationDetails(authentication);

		Operatore operatore = userDetails.getOperatore();

		return Response.status(HttpServletResponse.SC_OK).header("Content-Type", "application/json").entity(operatore).build();
	}
}
