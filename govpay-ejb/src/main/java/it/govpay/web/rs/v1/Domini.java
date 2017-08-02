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

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import javax.ws.rs.DefaultValue;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;
import javax.ws.rs.core.UriBuilder;
import javax.ws.rs.core.UriInfo;

import it.govpay.bd.BasicBD;
import it.govpay.core.business.model.GetDominioDTO;
import it.govpay.core.business.model.GetDominioDTOResponse;
import it.govpay.core.business.model.FindDominiDTO;
import it.govpay.core.business.model.FindDominiDTOResponse;
import it.govpay.core.exceptions.NotAuthorizedException;
import it.govpay.core.utils.GpContext;
import it.govpay.core.utils.GpThreadLocal;
import it.govpay.web.rs.v1.beans.Dominio;
import it.govpay.web.rs.v1.beans.Entrata;
import it.govpay.web.rs.v1.beans.Iban;
import it.govpay.web.rs.v1.beans.ListaDomini;
import it.govpay.web.rs.v1.beans.UnitaOperativa;


@Path("/v1/domini")
public class Domini extends BaseRsServiceV1 {
	
	public static final String NOME_SERVIZIO = "incassi";
	
	@GET
	@Path("/")
	@Produces({MediaType.APPLICATION_JSON})
	public Response findDomini(InputStream is, @Context UriInfo uriInfo, @Context HttpHeaders httpHeaders,
			@QueryParam(value="offset") @DefaultValue(value="0") int offset,
			@QueryParam(value="limit") @DefaultValue(value="25") int limit,
			@QueryParam(value="fields") String fields,
			@QueryParam(value="reagioneSociale") String ragioneSociale) {
		
		String methodName = "findDomini"; 
		if(limit > 25) limit = 500;
		
		BasicBD bd = null;
		GpContext ctx = null; 
		
		try{
			this.logRequest(uriInfo, httpHeaders, methodName, new ByteArrayOutputStream());
			bd = BasicBD.newInstance(GpThreadLocal.get().getTransactionId());
			ctx =  GpThreadLocal.get();
			
			UriBuilder baseUriBuilder = uriInfo.getBaseUriBuilder().path("v1");
			
			FindDominiDTO listaDominiDTO = new FindDominiDTO(bd);
			
			listaDominiDTO.setPrincipal(getPrincipal());
			listaDominiDTO.getFilter().setOffset(offset);
			listaDominiDTO.getFilter().setLimit(limit);
			listaDominiDTO.getFilter().setRagioneSociale(ragioneSociale);
			
			it.govpay.core.business.Domini dominiBusiness = new it.govpay.core.business.Domini(bd);
			FindDominiDTOResponse listaDominiDTOResponse = dominiBusiness.listaDomini(listaDominiDTO);
			
			List<Dominio> domini = new ArrayList<Dominio>();
			for(it.govpay.bd.model.Dominio d : listaDominiDTOResponse.getDomini()) {
				domini.add(new Dominio(d, baseUriBuilder, bd));
			}
			
			ListaDomini listaDomini = new ListaDomini(domini, uriInfo.getRequestUri(), listaDominiDTOResponse.getTotalCount(), offset, limit);
			return Response.status(Status.OK).entity(listaDomini.toJSON(fields)).build();
		} catch (NotAuthorizedException e) {
			this.logResponse(uriInfo, httpHeaders, methodName, new byte[0],401);
			return Response.status(Status.UNAUTHORIZED).build();
		} catch (Exception e) {
			log.error("Errore interno durante il processo di incasso", e);
			this.logResponse(uriInfo, httpHeaders, methodName, new byte[0], 500);
			return Response.status(Status.INTERNAL_SERVER_ERROR).build();
		} finally {
			if(bd != null) bd.closeConnection();
			if(ctx != null) ctx.log();
		}
	}
	
	@GET
	@Path("/{codDominio}")
	@Produces({MediaType.APPLICATION_JSON})
	public Response getDominio(InputStream is, @Context UriInfo uriInfo, @Context HttpHeaders httpHeaders,
			@PathParam(value="codDominio") String codDominio,
			@QueryParam(value="fields") String fields) {
		
		String methodName = "getDominio"; 
		BasicBD bd = null;
		GpContext ctx = null; 
		UriBuilder baseUriBuilder = uriInfo.getBaseUriBuilder().path("v1");
		try{
			this.logRequest(uriInfo, httpHeaders, methodName, new ByteArrayOutputStream());
			bd = BasicBD.newInstance(GpThreadLocal.get().getTransactionId());
			ctx =  GpThreadLocal.get();
			
			GetDominioDTO leggiDominioDTO = new GetDominioDTO();
			leggiDominioDTO.setCodDominio(codDominio);
			leggiDominioDTO.setPrincipal(getPrincipal());
			
			it.govpay.core.business.Domini domini = new it.govpay.core.business.Domini(bd);
			GetDominioDTOResponse leggiDominioDTOResponse = domini.leggiDominio(leggiDominioDTO);
			Dominio dominio = new Dominio(leggiDominioDTOResponse.getDominio(), baseUriBuilder, bd);
			
			return Response.status(Status.OK).entity(dominio.toJSON(fields)).build();
		} catch (NotAuthorizedException e) {
			this.logResponse(uriInfo, httpHeaders, methodName, new byte[0],401);
			return Response.status(Status.UNAUTHORIZED).build();
		} catch (Exception e) {
			log.error("Errore interno durante il processo di incasso", e);
			this.logResponse(uriInfo, httpHeaders, methodName, new byte[0], 500);
			return Response.status(Status.INTERNAL_SERVER_ERROR).build();
		} finally {
			if(bd != null) bd.closeConnection();
			if(ctx != null) ctx.log();
		}
	}
	
	@GET
	@Path("/{codDominio}/unita_operative")
	@Produces({MediaType.APPLICATION_JSON})
	public Response findUnitaOperative(InputStream is, @Context UriInfo uriInfo, @Context HttpHeaders httpHeaders,
			@QueryParam(value="offset") @DefaultValue(value="0") int offset,
			@QueryParam(value="limit") @DefaultValue(value="25") int limit,
			@QueryParam(value="fields") String fields,
			@PathParam(value="codDominio") String codDominio) {
		
		String methodName = "findUnitaOperative"; 
		BasicBD bd = null;
		GpContext ctx = null; 
		UriBuilder baseUriBuilder = uriInfo.getBaseUriBuilder().path("v1");
		try{
			this.logRequest(uriInfo, httpHeaders, methodName, new ByteArrayOutputStream());
			bd = BasicBD.newInstance(GpThreadLocal.get().getTransactionId());
			ctx =  GpThreadLocal.get();
			
			GetDominioDTO leggiDominioDTO = new GetDominioDTO();
			leggiDominioDTO.setCodDominio(codDominio);
			leggiDominioDTO.setPrincipal(getPrincipal());
			
			it.govpay.core.business.Domini domini = new it.govpay.core.business.Domini(bd);
			GetDominioDTOResponse leggiDominioDTOResponse = domini.leggiDominio(leggiDominioDTO);
			
			List<UnitaOperativa> unitaOperative = new ArrayList<UnitaOperativa>();
			for(it.govpay.bd.model.UnitaOperativa uo : leggiDominioDTOResponse.getDominio().getUnitaOperative(bd)) {
				if(uo.getCodUo().equals(it.govpay.model.Dominio.EC)) continue;
				unitaOperative.add(new UnitaOperativa(uo, baseUriBuilder, bd));
			}
			
			return Response.status(Status.OK).entity(unitaOperative).build();
		} catch (NotAuthorizedException e) {
			this.logResponse(uriInfo, httpHeaders, methodName, new byte[0],401);
			return Response.status(Status.UNAUTHORIZED).build();
		} catch (Exception e) {
			log.error("Errore interno durante il processo di incasso", e);
			this.logResponse(uriInfo, httpHeaders, methodName, new byte[0], 500);
			return Response.status(Status.INTERNAL_SERVER_ERROR).build();
		} finally {
			if(bd != null) bd.closeConnection();
			if(ctx != null) ctx.log();
		}
	}
	
	@GET
	@Path("/{codDominio}/unita_operative/{codice_univoco}")
	@Produces({MediaType.APPLICATION_JSON})
	public Response getUnitaOperativa(InputStream is, @Context UriInfo uriInfo, @Context HttpHeaders httpHeaders,
			@PathParam(value="codDominio") String codDominio, @PathParam(value="codice_univoco") String codUnivoco) {
		
		String methodName = "getUnitaOperativa"; 
		BasicBD bd = null;
		GpContext ctx = null; 
		
		try{
			this.logRequest(uriInfo, httpHeaders, methodName, new ByteArrayOutputStream());
			bd = BasicBD.newInstance(GpThreadLocal.get().getTransactionId());
			ctx =  GpThreadLocal.get();
			
			GetDominioDTO leggiDominioDTO = new GetDominioDTO();
			leggiDominioDTO.setCodDominio(codDominio);
			leggiDominioDTO.setPrincipal(getPrincipal());
			
			it.govpay.core.business.Domini domini = new it.govpay.core.business.Domini(bd);
			GetDominioDTOResponse leggiDominioDTOResponse = domini.leggiDominio(leggiDominioDTO);
			UnitaOperativa unitaOperativa = new UnitaOperativa(leggiDominioDTOResponse.getDominio().getUnitaOperativa(bd, codUnivoco), uriInfo.getAbsolutePathBuilder(), bd);
			
			return Response.status(Status.OK).entity(unitaOperativa).build();
		} catch (NotAuthorizedException e) {
			this.logResponse(uriInfo, httpHeaders, methodName, new byte[0],401);
			return Response.status(Status.UNAUTHORIZED).build();
		} catch (Exception e) {
			log.error("Errore interno durante il processo di incasso", e);
			this.logResponse(uriInfo, httpHeaders, methodName, new byte[0], 500);
			return Response.status(Status.INTERNAL_SERVER_ERROR).build();
		} finally {
			if(bd != null) bd.closeConnection();
			if(ctx != null) ctx.log();
		}
	}
	
	@GET
	@Path("/{codDominio}/iban")
	@Produces({MediaType.APPLICATION_JSON})
	public Response findIban(InputStream is, @Context UriInfo uriInfo, @Context HttpHeaders httpHeaders,
			@PathParam(value="codDominio") String codDominio) {
		
		String methodName = "findIban"; 
		BasicBD bd = null;
		GpContext ctx = null; 
		
		try{
			this.logRequest(uriInfo, httpHeaders, methodName, new ByteArrayOutputStream());
			bd = BasicBD.newInstance(GpThreadLocal.get().getTransactionId());
			ctx =  GpThreadLocal.get();
			
			GetDominioDTO leggiDominioDTO = new GetDominioDTO();
			leggiDominioDTO.setCodDominio(codDominio);
			leggiDominioDTO.setPrincipal(getPrincipal());
			
			it.govpay.core.business.Domini domini = new it.govpay.core.business.Domini(bd);
			GetDominioDTOResponse leggiDominioDTOResponse = domini.leggiDominio(leggiDominioDTO);
			
			List<Iban> ibans = new ArrayList<Iban>();
			for(it.govpay.bd.model.IbanAccredito iban : leggiDominioDTOResponse.getDominio().getIbanAccredito(bd)) {
				ibans.add(new Iban(iban, uriInfo.getAbsolutePathBuilder(), bd));
			}
			
			return Response.status(Status.OK).entity(ibans).build();
		} catch (NotAuthorizedException e) {
			this.logResponse(uriInfo, httpHeaders, methodName, new byte[0],401);
			return Response.status(Status.UNAUTHORIZED).build();
		} catch (Exception e) {
			log.error("Errore interno durante il processo di incasso", e);
			this.logResponse(uriInfo, httpHeaders, methodName, new byte[0], 500);
			return Response.status(Status.INTERNAL_SERVER_ERROR).build();
		} finally {
			if(bd != null) bd.closeConnection();
			if(ctx != null) ctx.log();
		}
	}
	
	@GET
	@Path("/{codDominio}/iban_accredito/{iban}")
	@Produces({MediaType.APPLICATION_JSON})
	public Response getIban(InputStream is, @Context UriInfo uriInfo, @Context HttpHeaders httpHeaders,
			@PathParam(value="codDominio") String codDominio, @PathParam(value="iban") String iban) {
		
		String methodName = "getIban"; 
		BasicBD bd = null;
		GpContext ctx = null; 
		
		try{
			this.logRequest(uriInfo, httpHeaders, methodName, new ByteArrayOutputStream());
			bd = BasicBD.newInstance(GpThreadLocal.get().getTransactionId());
			ctx =  GpThreadLocal.get();
			
			GetDominioDTO leggiDominioDTO = new GetDominioDTO();
			leggiDominioDTO.setCodDominio(codDominio);
			leggiDominioDTO.setPrincipal(getPrincipal());
			
			it.govpay.core.business.Domini domini = new it.govpay.core.business.Domini(bd);
			GetDominioDTOResponse leggiDominioDTOResponse = domini.leggiDominio(leggiDominioDTO);
			Iban ibanAccredito = new Iban(leggiDominioDTOResponse.getDominio().getIban(bd, iban), uriInfo.getAbsolutePathBuilder(), bd);
			
			return Response.status(Status.OK).entity(ibanAccredito).build();
		} catch (NotAuthorizedException e) {
			this.logResponse(uriInfo, httpHeaders, methodName, new byte[0],401);
			return Response.status(Status.UNAUTHORIZED).build();
		} catch (Exception e) {
			log.error("Errore interno durante il processo di incasso", e);
			this.logResponse(uriInfo, httpHeaders, methodName, new byte[0], 500);
			return Response.status(Status.INTERNAL_SERVER_ERROR).build();
		} finally {
			if(bd != null) bd.closeConnection();
			if(ctx != null) ctx.log();
		}
	}
	
	@GET
	@Path("/{codDominio}/entrate")
	@Produces({MediaType.APPLICATION_JSON})
	public Response findTributi(InputStream is, @Context UriInfo uriInfo, @Context HttpHeaders httpHeaders,
			@PathParam(value="codDominio") String codDominio) {
		
		String methodName = "findTributi"; 
		BasicBD bd = null;
		GpContext ctx = null; 
		
		try{
			this.logRequest(uriInfo, httpHeaders, methodName, new ByteArrayOutputStream());
			bd = BasicBD.newInstance(GpThreadLocal.get().getTransactionId());
			ctx =  GpThreadLocal.get();
			
			GetDominioDTO leggiDominioDTO = new GetDominioDTO();
			leggiDominioDTO.setCodDominio(codDominio);
			leggiDominioDTO.setPrincipal(getPrincipal());
			
			it.govpay.core.business.Domini domini = new it.govpay.core.business.Domini(bd);
			GetDominioDTOResponse leggiDominioDTOResponse = domini.leggiDominio(leggiDominioDTO);
			
			List<Entrata> entrate = new ArrayList<Entrata>();
			for(it.govpay.bd.model.Tributo tributo : leggiDominioDTOResponse.getDominio().getTributi(bd)) {
				entrate.add(new Entrata(tributo, uriInfo.getAbsolutePathBuilder(), bd));
			}
			
			return Response.status(Status.OK).entity(entrate).build();
		} catch (NotAuthorizedException e) {
			this.logResponse(uriInfo, httpHeaders, methodName, new byte[0],401);
			return Response.status(Status.UNAUTHORIZED).build();
		} catch (Exception e) {
			log.error("Errore interno durante il processo di incasso", e);
			this.logResponse(uriInfo, httpHeaders, methodName, new byte[0], 500);
			return Response.status(Status.INTERNAL_SERVER_ERROR).build();
		} finally {
			if(bd != null) bd.closeConnection();
			if(ctx != null) ctx.log();
		}
	}
	
	@GET
	@Path("/{codDominio}/entrate/{codTributo}")
	@Produces({MediaType.APPLICATION_JSON})
	public Response getTributo(InputStream is, @Context UriInfo uriInfo, @Context HttpHeaders httpHeaders,
			@PathParam(value="codDominio") String codDominio, @PathParam(value="codTributo") String codTributo) {
		
		String methodName = "getTributo"; 
		BasicBD bd = null;
		GpContext ctx = null; 
		
		try{
			this.logRequest(uriInfo, httpHeaders, methodName, new ByteArrayOutputStream());
			bd = BasicBD.newInstance(GpThreadLocal.get().getTransactionId());
			ctx =  GpThreadLocal.get();
			
			GetDominioDTO leggiDominioDTO = new GetDominioDTO();
			leggiDominioDTO.setCodDominio(codDominio);
			leggiDominioDTO.setPrincipal(getPrincipal());
			
			it.govpay.core.business.Domini domini = new it.govpay.core.business.Domini(bd);
			GetDominioDTOResponse leggiDominioDTOResponse = domini.leggiDominio(leggiDominioDTO);
			Entrata entrata = new Entrata(leggiDominioDTOResponse.getDominio().getTributo(bd, codTributo), uriInfo.getAbsolutePathBuilder(), bd);
			
			return Response.status(Status.OK).entity(entrata).build();
		} catch (NotAuthorizedException e) {
			this.logResponse(uriInfo, httpHeaders, methodName, new byte[0],401);
			return Response.status(Status.UNAUTHORIZED).build();
		} catch (Exception e) {
			log.error("Errore interno durante il processo di incasso", e);
			this.logResponse(uriInfo, httpHeaders, methodName, new byte[0], 500);
			return Response.status(Status.INTERNAL_SERVER_ERROR).build();
		} finally {
			if(bd != null) bd.closeConnection();
			if(ctx != null) ctx.log();
		}
	}
	
	
}
