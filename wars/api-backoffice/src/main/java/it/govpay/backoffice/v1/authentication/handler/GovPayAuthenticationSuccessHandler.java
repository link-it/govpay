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
package it.govpay.backoffice.v1.authentication.handler;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.Response.Status;

import org.openspcoop2.utils.LoggerWrapperFactory;
import org.openspcoop2.utils.UtilsException;
import org.openspcoop2.utils.service.context.ContextThreadLocal;
import org.openspcoop2.utils.service.context.IContext;
import org.springframework.security.core.Authentication;

import it.govpay.backoffice.v1.beans.Profilo;
import it.govpay.backoffice.v1.beans.converter.ProfiloConverter;
import it.govpay.core.beans.Costanti;
import it.govpay.core.beans.EventoContext.Componente;
import it.govpay.core.beans.EventoContext.Esito;
import it.govpay.core.dao.anagrafica.UtentiDAO;
import it.govpay.core.dao.anagrafica.dto.LeggiProfiloDTOResponse;
import it.govpay.core.utils.GpContext;
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

			GpContext gpContext = (GpContext) ctx.getApplicationContext();

			UtentiDAO utentiDAO = new UtentiDAO();

			LeggiProfiloDTOResponse leggiProfilo = utentiDAO.getProfilo(authentication);

			Profilo profilo = ProfiloConverter.getProfilo(leggiProfilo);

			gpContext.getEventoCtx().setEsito(Esito.OK);
			gpContext.getEventoCtx().setIdTransazione(ctx.getTransactionId());

			return Response.status(Status.OK).entity(profilo.toJSON(null)).header(Costanti.HEADER_NAME_OUTPUT_TRANSACTION_ID, ctx.getTransactionId()).build();
		}catch (Exception e) {
			return CodiceEccezione.AUTENTICAZIONE.toFaultResponse(e);
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
