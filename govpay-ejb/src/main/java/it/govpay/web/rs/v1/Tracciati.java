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
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.ws.rs.Consumes;
import javax.ws.rs.DefaultValue;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;
import javax.ws.rs.core.Response.Status;

import com.sun.istack.Nullable;

import it.govpay.bd.BasicBD;
import it.govpay.core.business.model.InserisciTracciatoDTO;
import it.govpay.core.business.model.InserisciTracciatoDTOResponse;
import it.govpay.core.business.model.LeggiTracciatoDTOResponse;
import it.govpay.core.business.model.ListaTracciatiDTO;
import it.govpay.core.business.model.ListaTracciatiDTOResponse;
import it.govpay.core.business.model.LeggiTracciatoDTO;
import it.govpay.core.exceptions.NotAuthenticatedException;
import it.govpay.core.exceptions.NotAuthorizedException;
import it.govpay.core.utils.GpContext;
import it.govpay.core.utils.GpThreadLocal;
import it.govpay.model.Tracciato.StatoTracciatoType;
import it.govpay.model.Tracciato.TipoTracciatoType;
import it.govpay.rs.v1.BaseRsServiceV1;
import it.govpay.rs.v1.controllers.base.PendenzeController;
import it.govpay.rs.legacy.beans.Tracciato;
import it.govpay.rs.legacy.beans.TracciatoExt;

@Path("/v1/caricamenti")
public class Tracciati extends BaseRsServiceV1 {

	public static final String NOME_SERVIZIO = "caricamenti";
	private PendenzeController controller = null;
	
	public Tracciati() {
		super(NOME_SERVIZIO);
		this.controller = new PendenzeController(NOME_SERVIZIO, this.log);
	}

	@POST
	@Path("/")
	@Consumes("text/plain")
	@Produces(MediaType.APPLICATION_JSON)
	public Response caricamentoTracciato(@Context UriInfo uriInfo, @Context HttpHeaders httpHeaders, @QueryParam(value="nome") String nome, String incomingCsv) throws IOException {
		String methodName = "caricamentoTracciato"; 
		
		BasicBD bd = null;
		GpContext ctx = null; 
		
		try{
			this.controller.logRequest(uriInfo, httpHeaders, methodName, incomingCsv.getBytes());

			bd = BasicBD.newInstance(GpThreadLocal.get().getTransactionId());
			ctx =  GpThreadLocal.get();
			
			it.govpay.core.business.Tracciati tracciati = new it.govpay.core.business.Tracciati(bd);
			InserisciTracciatoDTO inserisciTracciatoDTO = new InserisciTracciatoDTO();
			inserisciTracciatoDTO.setNomeTracciato(nome);
			inserisciTracciatoDTO.setTracciato(incomingCsv.getBytes());
			inserisciTracciatoDTO.setApplicazione(this.getApplicazioneAutenticata(bd));
			inserisciTracciatoDTO.setTipo(TipoTracciatoType.VERSAMENTI); 
			InserisciTracciatoDTOResponse inserisciTracciatoResponse = tracciati.inserisciTracciato(inserisciTracciatoDTO);
			
			Tracciato tracciato = new Tracciato(inserisciTracciatoResponse.getTracciato());
			this.controller.logResponse(uriInfo, httpHeaders, methodName, tracciato);
			
			
			return Response.status(Status.CREATED).entity(tracciato).build();
			
		} catch (NotAuthorizedException e) {
			this.controller.logResponse(uriInfo, httpHeaders, methodName, new byte[0],401);
			return Response.status(Status.UNAUTHORIZED).build();
		} catch (NotAuthenticatedException e) {
			this.controller.logResponse(uriInfo, httpHeaders, methodName, new byte[0],403);
			return Response.status(Status.FORBIDDEN).build();
		} catch (Exception e) {
			log.error("Errore interno durante il caricamento del tracciato", e);
			this.controller.logResponse(uriInfo, httpHeaders, methodName, new byte[0], 500);
			return Response.status(Status.INTERNAL_SERVER_ERROR).build();
		} finally {
			if(bd != null) bd.closeConnection();
			if(ctx != null) ctx.log();
		}
	}
	
	@GET
	@Path("/{id}")
	@Produces(MediaType.APPLICATION_JSON)
	public Response leggiTracciato(@Context UriInfo uriInfo, @Context HttpHeaders httpHeaders, @PathParam(value="id") int id) throws IOException {
		String methodName = "leggiTracciato"; 
		
		BasicBD bd = null;
		GpContext ctx = null; 
		
		try{
			this.controller.logRequest(uriInfo, httpHeaders, methodName, new byte[0]);

			bd = BasicBD.newInstance(GpThreadLocal.get().getTransactionId());
			ctx =  GpThreadLocal.get();
			
			it.govpay.core.business.Tracciati tracciati = new it.govpay.core.business.Tracciati(bd);
			LeggiTracciatoDTO leggiTracciatoDTO = new LeggiTracciatoDTO();
			leggiTracciatoDTO.setId(id);
			leggiTracciatoDTO.setApplicazione(this.getApplicazioneAutenticata(bd));
			LeggiTracciatoDTOResponse leggiTracciatoDTOResponse = tracciati.leggiTracciato(leggiTracciatoDTO);
			
			TracciatoExt tracciato = new TracciatoExt(leggiTracciatoDTOResponse.getTracciato(), bd);
			this.controller.logResponse(uriInfo, httpHeaders, methodName, tracciato);
			
			return Response.status(Status.OK).entity(tracciato).build();
			
		} catch (NotAuthorizedException e) {
			this.controller.logResponse(uriInfo, httpHeaders, methodName, new byte[0],401);
			return Response.status(Status.UNAUTHORIZED).build();
		} catch (NotAuthenticatedException e) {
			this.controller.logResponse(uriInfo, httpHeaders, methodName, new byte[0],403);
			return Response.status(Status.FORBIDDEN).build();
		} catch (Exception e) {
			log.error("Errore interno durante la lettura del tracciato", e);
			this.controller.logResponse(uriInfo, httpHeaders, methodName, new byte[0], 500);
			return Response.status(Status.INTERNAL_SERVER_ERROR).build();
		} finally {
			if(bd != null) bd.closeConnection();
			if(ctx != null) ctx.log();
		}
	}
	
	@GET
	@Path("/")
	@Produces(MediaType.APPLICATION_JSON)
	public Response cercaTracciati(@Context UriInfo uriInfo, @Context HttpHeaders httpHeaders, 
			@QueryParam(value="data_inizio") @Nullable Date inizio,
			@QueryParam(value="data_fine") @Nullable Date fine,
			@QueryParam(value="stato") @Nullable StatoTracciatoType stato,
			@QueryParam(value="offset") @DefaultValue(value="0") int offset,
			@QueryParam(value="limit") @DefaultValue(value="25") int limit) throws IOException {
		String methodName = "cercaTracciati"; 
		
		BasicBD bd = null;
		GpContext ctx = null; 
		
		try{
			this.controller.logRequest(uriInfo, httpHeaders, methodName, new byte[0]);

			bd = BasicBD.newInstance(GpThreadLocal.get().getTransactionId());
			ctx =  GpThreadLocal.get();
			
			it.govpay.core.business.Tracciati tracciati = new it.govpay.core.business.Tracciati(bd);
			ListaTracciatiDTO listaTracciatiDTO = new ListaTracciatiDTO();
			listaTracciatiDTO.setApplicazione(this.getApplicazioneAutenticata(bd));
			listaTracciatiDTO.setInizio(inizio);
			listaTracciatiDTO.setFine(fine);
			listaTracciatiDTO.setLimit(limit);
			listaTracciatiDTO.setOffset(offset);
			listaTracciatiDTO.setStato(stato);
			ListaTracciatiDTOResponse listaTracciatiDTOResponse = tracciati.listaTracciati(listaTracciatiDTO);
			
			List<Tracciato> listaTracciati = new ArrayList<Tracciato>();
			for(it.govpay.bd.model.Tracciato t : listaTracciatiDTOResponse.getTracciati()) {
				listaTracciati.add(new Tracciato(t));
			}
			
			this.controller.logResponse(uriInfo, httpHeaders, methodName, listaTracciati);
			
			return Response.status(Status.OK).entity(listaTracciati).build();
			
		} catch (NotAuthorizedException e) {
			this.controller.logResponse(uriInfo, httpHeaders, methodName, new byte[0],401);
			return Response.status(Status.UNAUTHORIZED).build();
		} catch (NotAuthenticatedException e) {
			this.controller.logResponse(uriInfo, httpHeaders, methodName, new byte[0],403);
			return Response.status(Status.FORBIDDEN).build();
		} catch (Exception e) {
			log.error("Errore interno durante la lettura del tracciato", e);
			this.controller.logResponse(uriInfo, httpHeaders, methodName, new byte[0], 500);
			return Response.status(Status.INTERNAL_SERVER_ERROR).build();
		} finally {
			if(bd != null) bd.closeConnection();
			if(ctx != null) ctx.log();
		}
	}
}

