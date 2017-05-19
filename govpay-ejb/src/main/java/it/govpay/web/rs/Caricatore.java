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
package it.govpay.web.rs;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;

import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;

import it.govpay.bd.BasicBD;
import it.govpay.core.exceptions.GovPayException;
import it.govpay.core.utils.GpContext;
import it.govpay.core.utils.GpThreadLocal;
import it.govpay.model.Applicazione;
import it.govpay.web.rs.caricatore.ICaricatore;
import it.govpay.web.rs.utils.RestUtils;

@Path("/caricatore")
public class Caricatore extends BaseRsService{

	public static final String NOME_SERVIZIO = "PagamentiTelematiciAPPjson";
	
	public Caricatore() {
		super(NOME_SERVIZIO);
	}

	@POST
	@Path("/caricaVersamento")
	@Consumes({MediaType.APPLICATION_JSON})
	@Produces({MediaType.APPLICATION_JSON})
	public Response caricaVersamento(InputStream is, @Context UriInfo uriInfo, @Context HttpHeaders httpHeaders, @QueryParam("caricatoreImplClass") String caricatoreImplClass){
		String methodName = "CaricaVersamento"; 
		
		Applicazione applicazioneAutenticata = null; 
		BasicBD bd = null;
		GpContext ctx = null; 
		ByteArrayOutputStream baos = null, baosResponse = null;
		ByteArrayInputStream bais = null;
		
		try{
			baos = new ByteArrayOutputStream();
			
			BaseRsService.copy(is, baos);

			this.logRequest(uriInfo, httpHeaders, methodName,baos);

			if(caricatoreImplClass == null) {
				caricatoreImplClass = "it.govpay.web.rs.caricatore.CaricatoreImpl";
			}
			
			Class<?> caricatoreClass = Class.forName(caricatoreImplClass);
			
			Object caricatoreClassObjectImpl = caricatoreClass.newInstance();
			
			if(!(caricatoreClassObjectImpl instanceof ICaricatore)) {
				throw new Exception("La classe ["+caricatoreImplClass+"] dovrebbe implementare l'interfaccia " + ICaricatore.class);
			}

			ICaricatore caricatoreClassImpl = (ICaricatore) caricatoreClassObjectImpl;

			bd = BasicBD.newInstance(GpThreadLocal.get().getTransactionId());
			ctx =  GpThreadLocal.get();
			applicazioneAutenticata = getApplicazioneAutenticata(bd); 

			bais = new ByteArrayInputStream(baos.toByteArray());
			String response = caricatoreClassImpl.caricaVersamento(bais, uriInfo, httpHeaders, bd, applicazioneAutenticata);
			
			baosResponse = new ByteArrayOutputStream();
			baosResponse.write(response.getBytes());
			
			this.logResponse(uriInfo, httpHeaders, methodName, baosResponse);

			return Response.ok(response).build();
		} catch (GovPayException e) {
			e.log(log);
			ByteArrayOutputStream baosGovPayExc = RestUtils.writeGovpayErrorResponse(this, log, e, uriInfo, httpHeaders, bd, methodName);
			if(ctx!=null) ctx.log("rest.versamentoKo",e.getMessage());
			return Response.serverError().entity(baosGovPayExc.toString()).build();
		} catch (WebApplicationException e) {
			GovPayException ge = new GovPayException(e);
			ge.log(log);
			if(ctx!=null) ctx.log("rest.versamentoKo",ge.getMessage());
			return e.getResponse();
		} catch (Exception e) {
			GovPayException ge = new GovPayException(e);
			ge.log(log);
			ByteArrayOutputStream baosExc = RestUtils.writeGovpayErrorResponse(this, log, e, uriInfo, httpHeaders, bd, methodName);
			if(ctx!=null) ctx.log("rest.versamentoKo",ge.getMessage());
			return Response.serverError().entity(baosExc.toString()).build();
		} finally {
			if(bd != null) bd.closeConnection();
			if(ctx != null)ctx.log();
			if(bais != null) try { bais.close();} catch (IOException e) {}
			if(baos != null) try { baos.flush(); baos.close();} catch (IOException e) {}
			if(baosResponse != null) try { baosResponse.flush(); baosResponse.close();} catch (IOException e) {}
		}
	}

}
