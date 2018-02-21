///*
// * GovPay - Porta di Accesso al Nodo dei Pagamenti SPC 
// * http://www.gov4j.it/govpay
// * 
// * Copyright (c) 2014-2017 Link.it srl (http://www.link.it).
// * 
// * This program is free software: you can redistribute it and/or modify
// * it under the terms of the GNU General Public License version 3, as published by
// * the Free Software Foundation.
// *
// * This program is distributed in the hope that it will be useful,
// * but WITHOUT ANY WARRANTY; without even the implied warranty of
// * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
// * GNU General Public License for more details.
// *
// * You should have received a copy of the GNU General Public License
// * along with this program.  If not, see <http://www.gnu.org/licenses/>.
// *
// */
//package it.govpay.pagamento.api.rs.v1.pagamenti;
//
//import java.io.ByteArrayInputStream;
//import java.io.ByteArrayOutputStream;
//import java.io.IOException;
//import java.io.InputStream;
//import java.util.ArrayList;
//import java.util.List;
//
//import javax.ws.rs.Consumes;
//import javax.ws.rs.DefaultValue;
//import javax.ws.rs.GET;
//import javax.ws.rs.POST;
//import javax.ws.rs.Path;
//import javax.ws.rs.PathParam;
//import javax.ws.rs.Produces;
//import javax.ws.rs.QueryParam;
//import javax.ws.rs.core.Context;
//import javax.ws.rs.core.HttpHeaders;
//import javax.ws.rs.core.MediaType;
//import javax.ws.rs.core.Response;
//import javax.ws.rs.core.Response.Status;
//import javax.ws.rs.core.UriInfo;
//
//import it.govpay.bd.BasicBD;
//import it.govpay.bd.anagrafica.AnagraficaManager;
//import it.govpay.core.business.model.LeggiRiscossioneDTO;
//import it.govpay.core.business.model.LeggiRiscossioneDTOResponse;
//import it.govpay.core.business.model.ListaRiscossioniDTO;
//import it.govpay.core.business.model.ListaRiscossioniDTOResponse;
//import it.govpay.core.business.model.RichiestaRiscossioneDTO;
//import it.govpay.core.business.model.RichiestaRiscossioneDTOResponse;
//import it.govpay.core.exceptions.RiscossioniException;
//import it.govpay.core.exceptions.NotAuthorizedException;
//import it.govpay.core.utils.GpContext;
//import it.govpay.core.utils.GpThreadLocal;
//import it.govpay.model.Applicazione;
//import it.govpay.rs.BaseRsService;
//import it.govpay.rs.v1.BaseRsServiceV1;
//import it.govpay.rs.v1.beans.Errore;
//import it.govpay.rs.v1.beans.RiscossionePost;
//import it.govpay.rs.v1.beans.Riscossione;
//import it.govpay.rs.v1.beans.ListaRiscossioni;
//import net.sf.json.JsonConfig;
//
//@Path("/incassi")
//public class Riscossioni extends BaseRsServiceV1 {
//	
//	public static final String NOME_SERVIZIO = "incassi";
//	
//	public Riscossioni() {
//		super(NOME_SERVIZIO);
//	}
//	
//	@POST
//	@Path("/")
//	@Consumes({MediaType.APPLICATION_JSON})
//	@Produces({MediaType.APPLICATION_JSON})
//	public Response inserisciRiscossione(InputStream is, @Context UriInfo uriInfo, @Context HttpHeaders httpHeaders){
//		String methodName = "inserisciRiscossione"; 
//		
//		BasicBD bd = null;
//		GpContext ctx = null; 
//		ByteArrayOutputStream baos = null, baosResponse = null;
//		ByteArrayInputStream bais = null;
//		
//		try{
//			baos = new ByteArrayOutputStream();
//			BaseRsService.copy(is, baos);
//
//			this.logRequest(uriInfo, httpHeaders, methodName, baos);
//
//			bd = BasicBD.newInstance(GpThreadLocal.get().getTransactionId());
//			ctx =  GpThreadLocal.get();
//			
//			it.govpay.rs.v1.beans.RiscossionePost incasso = (RiscossionePost) it.govpay.rs.v1.beans.RiscossionePost.parse(baos.toString(), it.govpay.rs.v1.beans.RiscossionePost.class, new JsonConfig());
//			RichiestaRiscossioneDTO richiestaRiscossioneDTO = incasso.toRichiestaRiscossioneDTO();
//			Applicazione applicazione = AnagraficaManager.getApplicazioneByPrincipal(bd, getPrincipal());
//			richiestaRiscossioneDTO.setApplicazione(applicazione);
//			
//			it.govpay.core.business.Riscossioni incassi = new it.govpay.core.business.Riscossioni(bd);
//			RichiestaRiscossioneDTOResponse richiestaRiscossioneDTOResponse = incassi.richiestaRiscossione(richiestaRiscossioneDTO);
//			
//			Riscossione incassoExt = new Riscossione(richiestaRiscossioneDTOResponse.getRiscossione());
//			
//			this.logResponse(uriInfo, httpHeaders, methodName, incassoExt.toJSON(null));
//
//			Status status = richiestaRiscossioneDTOResponse.isCreato() ? Status.CREATED : Status.OK;
//			return Response.status(status).entity(incassoExt.toJSON(null)).build();
//		} catch (NotAuthorizedException e) {
//			this.logResponse(uriInfo, httpHeaders, methodName, new byte[0],401);
//			return Response.status(Status.UNAUTHORIZED).build();
//		} catch (RiscossioniException e) {
//			Errore errore = new Errore(e);
//			try { this.logResponse(uriInfo, httpHeaders, methodName, errore); } catch (Exception e2) { log.error(e2);}
//			return Response.status(422).entity(errore).build();
//		} catch (Exception e) {
//			log.error("Errore interno durante il processo di incasso", e);
//			this.logResponse(uriInfo, httpHeaders, methodName, new byte[0], 500);
//			return Response.status(Status.INTERNAL_SERVER_ERROR).build();
//		} finally {
//			if(bd != null) bd.closeConnection();
//			if(ctx != null) ctx.log();
//			if(bais != null) try { bais.close();} catch (IOException e) {}
//			if(baos != null) try { baos.flush(); baos.close();} catch (IOException e) {}
//			if(baosResponse != null) try { baosResponse.flush(); baosResponse.close();} catch (IOException e) {}
//		}
//	}
//	
//	
//	@GET
//	@Path("/")
//	@Produces({MediaType.APPLICATION_JSON})
//	public Response cercaRiscossioni(InputStream is, @Context UriInfo uriInfo, @Context HttpHeaders httpHeaders,
//			@QueryParam(value="offset") @DefaultValue(value="0") int offset,
//			@QueryParam(value="limit") @DefaultValue(value="25") int limit) {
//		
//		String methodName = "cercaRiscossioni"; 
//		if(limit > 25) limit = 25;
//		
//		BasicBD bd = null;
//		GpContext ctx = null; 
//		
//		try{
//
//			this.logRequest(uriInfo, httpHeaders, methodName, new ByteArrayOutputStream());
//			bd = BasicBD.newInstance(GpThreadLocal.get().getTransactionId());
//			ctx =  GpThreadLocal.get();
//			
//			ListaRiscossioniDTO listaRiscossioneDTO = new ListaRiscossioniDTO();
//			listaRiscossioneDTO.setOffset(offset);
//			listaRiscossioneDTO.setLimit(limit);
//			listaRiscossioneDTO.setPrincipal(getPrincipal());
//			
//			it.govpay.core.business.Riscossioni incassi = new it.govpay.core.business.Riscossioni(bd);
//			ListaRiscossioniDTOResponse listaRiscossioniDTOResponse = incassi.listaRiscossioni(listaRiscossioneDTO);
//			
//			List<Riscossione> listaRiscossioni = new ArrayList<Riscossione>();
//			for(it.govpay.bd.model.Riscossione i : listaRiscossioniDTOResponse.getResults()) {
//				listaRiscossioni.add(new Riscossione(i));
//			}
//			
//			return Response.status(Status.OK).entity(new ListaRiscossioni(listaRiscossioni, uriInfo.getBaseUri(), listaRiscossioniDTOResponse.getTotalResults(), offset, limit).toJSON(null)).build();
//		} catch (NotAuthorizedException e) {
//			this.logResponse(uriInfo, httpHeaders, methodName, new byte[0],401);
//			return Response.status(Status.UNAUTHORIZED).build();
//		} catch (Exception e) {
//			log.error("Errore interno durante il processo di incasso", e);
//			this.logResponse(uriInfo, httpHeaders, methodName, new byte[0], 500);
//			return Response.status(Status.INTERNAL_SERVER_ERROR).build();
//		} finally {
//			if(bd != null) bd.closeConnection();
//			if(ctx != null) ctx.log();
//		}
//	}
//	
//	@GET
//	@Path("/{id}")
//	@Consumes({MediaType.APPLICATION_JSON})
//	@Produces({MediaType.APPLICATION_JSON})
//	public Response leggiRiscossione(InputStream is, @Context UriInfo uriInfo, @Context HttpHeaders httpHeaders,
//			@PathParam(value="id") Long id) {
//		
//		String methodName = "leggiRiscossione"; 
//		BasicBD bd = null;
//		GpContext ctx = null; 
//		
//		try{
//			this.logRequest(uriInfo, httpHeaders, methodName, new ByteArrayOutputStream());
//			bd = BasicBD.newInstance(GpThreadLocal.get().getTransactionId());
//			ctx =  GpThreadLocal.get();
//			
//			LeggiRiscossioneDTO leggiRiscossioneDTO = new LeggiRiscossioneDTO();
//			leggiRiscossioneDTO.setId(id);
//			leggiRiscossioneDTO.setPrincipal(getPrincipal());
//			
//			it.govpay.core.business.Riscossioni incassi = new it.govpay.core.business.Riscossioni(bd);
//			LeggiRiscossioneDTOResponse leggiRiscossioneDTOResponse = incassi.leggiRiscossione(leggiRiscossioneDTO);
//			
//			return Response.status(Status.OK).entity(new Riscossione(leggiRiscossioneDTOResponse.getRiscossione()).toJSON(null)).build();
//		} catch (NotAuthorizedException e) {
//			this.logResponse(uriInfo, httpHeaders, methodName, new byte[0],401);
//			return Response.status(Status.UNAUTHORIZED).build();
//		} catch (Exception e) {
//			log.error("Errore interno durante il processo di incasso", e);
//			this.logResponse(uriInfo, httpHeaders, methodName, new byte[0], 500);
//			return Response.status(Status.INTERNAL_SERVER_ERROR).build();
//		} finally {
//			if(bd != null) bd.closeConnection();
//			if(ctx != null) ctx.log();
//		}
//	}
//}
