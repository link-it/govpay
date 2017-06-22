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

import java.io.IOException;

import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;
import javax.ws.rs.core.Response.Status;


import it.govpay.bd.BasicBD;
import it.govpay.core.business.Tracciati;
import it.govpay.core.business.model.InserisciTracciatoDTO;
import it.govpay.core.business.model.InserisciTracciatoDTOResponse;
import it.govpay.core.exceptions.NotAuthenticatedException;
import it.govpay.core.exceptions.NotAuthorizedException;
import it.govpay.core.utils.GpContext;
import it.govpay.core.utils.GpThreadLocal;

@Path("/v1/caricamenti")
public class CaricamentiMassivi extends BaseRsServiceV1 {

	public static final String NOME_SERVIZIO = "caricamenti";

	@POST
	@Path("/")
	@Consumes("text/plain")
	@Produces(MediaType.APPLICATION_JSON)
	public Response caricamentoCSV(@Context UriInfo uriInfo, @Context HttpHeaders httpHeaders, @QueryParam(value="nome") String nome, String incomingCsv) throws IOException {
		String methodName = "caricamentoCSV"; 
		
		BasicBD bd = null;
		GpContext ctx = null; 
		
		try{
			this.logRequest(uriInfo, httpHeaders, methodName, incomingCsv.getBytes());

			bd = BasicBD.newInstance(GpThreadLocal.get().getTransactionId());
			ctx =  GpThreadLocal.get();
			
			Tracciati tracciati = new Tracciati(bd);
			InserisciTracciatoDTO inserisciTracciatoDTO = new InserisciTracciatoDTO();
			inserisciTracciatoDTO.setNomeTracciato(nome);
			inserisciTracciatoDTO.setTracciato(incomingCsv.getBytes());
			inserisciTracciatoDTO.setApplicazione(this.getApplicazioneAutenticata(bd));
			InserisciTracciatoDTOResponse inserisciTracciatoResponse = tracciati.inserisciTracciato(inserisciTracciatoDTO);
			
			String jsonResponse = "{ \"id\": " + inserisciTracciatoResponse.getTracciato().getId()+" }";
			this.logResponse(uriInfo, httpHeaders, methodName, jsonResponse);
			
			return Response.status(Status.CREATED).entity(jsonResponse).build();
			
		} catch (NotAuthorizedException e) {
			this.logResponse(uriInfo, httpHeaders, methodName, new byte[0],401);
			return Response.status(Status.UNAUTHORIZED).build();
		} catch (NotAuthenticatedException e) {
			this.logResponse(uriInfo, httpHeaders, methodName, new byte[0],403);
			return Response.status(Status.FORBIDDEN).build();
		} catch (Exception e) {
			log.error("Errore interno durante il caricamento del tracciato", e);
			this.logResponse(uriInfo, httpHeaders, methodName, new byte[0], 500);
			return Response.status(Status.INTERNAL_SERVER_ERROR).build();
		} finally {
			if(bd != null) bd.closeConnection();
			if(ctx != null) ctx.log();
		}
	}
}

