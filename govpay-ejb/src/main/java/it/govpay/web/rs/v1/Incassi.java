/*
 * GovPay - Porta di Accesso al Nodo dei Pagamenti SPC 
 * http://www.gov4j.it/govpay
 * 
 * Copyright (c) 2014-2017 Link.it srl (http://www.link.it).
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
package it.govpay.web.rs.v1;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;

import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;
import javax.ws.rs.core.UriInfo;

import it.govpay.bd.BasicBD;
import it.govpay.core.business.model.RichiestaIncassoDTO;
import it.govpay.core.business.model.RichiestaIncassoDTOResponse;
import it.govpay.core.exceptions.GovPayException;
import it.govpay.core.exceptions.IncassiException;
import it.govpay.core.exceptions.NotAuthorizedException;
import it.govpay.core.utils.GpContext;
import it.govpay.core.utils.GpThreadLocal;
import it.govpay.web.rs.BaseRsService;
import it.govpay.web.rs.v1.beans.Incasso;

@Path("/v1/incassi")
public class Incassi extends BaseRsServiceV1 {
	
	public static final String NOME_SERVIZIO = "incassi";
	
	@POST
	@Path("/")
	@Consumes({MediaType.APPLICATION_JSON})
	@Produces({MediaType.APPLICATION_JSON})
	public Response addIncasso(InputStream is, @Context UriInfo uriInfo, @Context HttpHeaders httpHeaders){
		String methodName = "addIncasso"; 
		
		BasicBD bd = null;
		GpContext ctx = null; 
		ByteArrayOutputStream baos = null, baosResponse = null;
		ByteArrayInputStream bais = null;
		
		try{
			baos = new ByteArrayOutputStream();
			BaseRsService.copy(is, baos);

			this.logRequest(uriInfo, httpHeaders, methodName, baos);

			bd = BasicBD.newInstance(GpThreadLocal.get().getTransactionId());
			ctx =  GpThreadLocal.get();
			
			Incasso incasso = Incasso.parse(baos.toString());
			RichiestaIncassoDTO richiestaIncassoDTO = incasso.toRichiestaIncassoDTO();
			richiestaIncassoDTO.setPrincipal(getPrincipal());
			
			it.govpay.core.business.Incassi incassi = new it.govpay.core.business.Incassi(bd);
			RichiestaIncassoDTOResponse richiestaIncassoDTOResponse = incassi.richiestaIncasso(richiestaIncassoDTO);
			
			this.logResponse(uriInfo, httpHeaders, methodName, richiestaIncassoDTOResponse.getPagamenti());

			if(richiestaIncassoDTOResponse.isCreato())
				return Response.status(Status.CREATED).entity(richiestaIncassoDTOResponse.getPagamenti()).build();
			else 
				return Response.status(Status.OK).entity(richiestaIncassoDTOResponse.getPagamenti()).build();
		} catch (WebApplicationException e) {
			GovPayException ge = new GovPayException(e);
			ge.log(log);
			if(ctx!=null) ctx.log("rest.versamentoKo",ge.getMessage());
			return e.getResponse();
		} catch (NotAuthorizedException e) {
			this.logResponse(uriInfo, httpHeaders, methodName, new byte[0],401);
			return Response.status(Status.UNAUTHORIZED).build();
		} catch (IncassiException e) {
			Errore errore = new Errore(e);
			try { this.logResponse(uriInfo, httpHeaders, methodName, errore); } catch (Exception e2) { log.error(e2);}
			return Response.status(422).entity(errore).build();
		} catch (Exception e) {
			log.error(e);
			this.logResponse(uriInfo, httpHeaders, methodName, new byte[0], 500);
			return Response.status(Status.INTERNAL_SERVER_ERROR).build();
		} finally {
			if(bd != null) bd.closeConnection();
			if(ctx != null) ctx.log();
			if(bais != null) try { bais.close();} catch (IOException e) {}
			if(baos != null) try { baos.flush(); baos.close();} catch (IOException e) {}
			if(baosResponse != null) try { baosResponse.flush(); baosResponse.close();} catch (IOException e) {}
		}
	}
}
