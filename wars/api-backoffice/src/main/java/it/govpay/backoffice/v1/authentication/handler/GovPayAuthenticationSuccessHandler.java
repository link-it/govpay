package it.govpay.backoffice.v1.authentication.handler;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

import org.openspcoop2.utils.LoggerWrapperFactory;
import org.openspcoop2.utils.UtilsException;
import org.openspcoop2.utils.service.context.ContextThreadLocal;
import org.openspcoop2.utils.service.context.IContext;
import org.springframework.security.core.Authentication;

import it.govpay.backoffice.v1.beans.Profilo;
import it.govpay.backoffice.v1.beans.converter.ProfiloConverter;
import it.govpay.core.beans.Costanti;
import it.govpay.core.dao.anagrafica.UtentiDAO;
import it.govpay.core.dao.anagrafica.dto.LeggiProfiloDTOResponse;
import it.govpay.core.utils.EventoContext.Componente;
import it.govpay.core.utils.service.context.GpContextFactory;
import it.govpay.rs.v1.exception.CodiceEccezione;

public class GovPayAuthenticationSuccessHandler extends org.openspcoop2.utils.service.authentication.handler.jaxrs.DefaultAuthenticationSuccessHandler{

	@Override
	public Response getPayload(HttpServletRequest request, HttpServletResponse res, Authentication authentication) {
		IContext ctx = null;
		try{
			ctx = ContextThreadLocal.get();
			
			if(ctx == null) {
				GpContextFactory factory  = new GpContextFactory();
				String user = authentication != null ? authentication.getName() : null;
				ctx = factory.newContext(request.getRequestURI(), "profilo", "getProfilo", request.getMethod(), 1, user, Componente.API_BACKOFFICE);
				ContextThreadLocal.set(ctx);
			}
			UtentiDAO utentiDAO = new UtentiDAO();
			
			LeggiProfiloDTOResponse leggiProfilo = utentiDAO.getProfilo(authentication);

			Profilo profilo = ProfiloConverter.getProfilo(leggiProfilo);
			
			return Response.status(Status.OK).entity(profilo.toJSON(null)).header(Costanti.HEADER_NAME_OUTPUT_TRANSACTION_ID, ctx.getTransactionId()).build();
		}catch (Exception e) {
			return CodiceEccezione.AUTORIZZAZIONE.toFaultResponse(e);
		} finally {
			if(ctx != null)
				try {
					ctx.getApplicationLogger().log();
				} catch (UtilsException e) {
					LoggerWrapperFactory.getLogger(getClass()).error("Errore durante il log dell'operazione: "+e.getMessage(), e);
				}
		}
	}
}
