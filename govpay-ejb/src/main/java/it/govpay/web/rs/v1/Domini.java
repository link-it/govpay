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

import it.govpay.core.dao.anagrafica.DominiDAO;
import it.govpay.core.dao.anagrafica.UtentiDAO;
import it.govpay.core.dao.anagrafica.dto.FindDominiDTO;
import it.govpay.core.dao.anagrafica.dto.FindDominiDTOResponse;
import it.govpay.core.dao.anagrafica.dto.FindIbanDTO;
import it.govpay.core.dao.anagrafica.dto.FindIbanDTOResponse;
import it.govpay.core.dao.anagrafica.dto.FindTributiDTO;
import it.govpay.core.dao.anagrafica.dto.FindTributiDTOResponse;
import it.govpay.core.dao.anagrafica.dto.FindUnitaOperativeDTO;
import it.govpay.core.dao.anagrafica.dto.FindUnitaOperativeDTOResponse;
import it.govpay.core.dao.anagrafica.dto.GetDominioDTO;
import it.govpay.core.dao.anagrafica.dto.GetDominioDTOResponse;
import it.govpay.core.dao.anagrafica.dto.GetIbanDTO;
import it.govpay.core.dao.anagrafica.dto.GetIbanDTOResponse;
import it.govpay.core.dao.anagrafica.dto.GetTributoDTO;
import it.govpay.core.dao.anagrafica.dto.GetTributoDTOResponse;
import it.govpay.core.dao.anagrafica.dto.GetUnitaOperativaDTO;
import it.govpay.core.dao.anagrafica.dto.GetUnitaOperativaDTOResponse;
import it.govpay.core.exceptions.BaseException;
import it.govpay.core.exceptions.NotAuthorizedException;
import it.govpay.core.utils.GpContext;
import it.govpay.core.utils.GpThreadLocal;
import it.govpay.model.IAutorizzato;
import it.govpay.web.rs.v1.beans.Dominio;
import it.govpay.web.rs.v1.beans.Entrata;
import it.govpay.web.rs.v1.beans.Errore;
import it.govpay.web.rs.v1.beans.Iban;
import it.govpay.web.rs.v1.beans.ListaDomini;
import it.govpay.web.rs.v1.beans.ListaEntrate;
import it.govpay.web.rs.v1.beans.ListaIbanAccredito;
import it.govpay.web.rs.v1.beans.ListaUnitaOperative;
import it.govpay.web.rs.v1.beans.UnitaOperativa;


@Path("/v1/domini")
public class Domini extends BaseRsServiceV1 {
	
	public static final String NOME_SERVIZIO = "domini";
	
	@GET
	@Path("/")
	@Produces({MediaType.APPLICATION_JSON})
	public Response findDomini(InputStream is, @Context UriInfo uriInfo, @Context HttpHeaders httpHeaders,
			@QueryParam(value="offset") @DefaultValue(value="0") int offset,
			@QueryParam(value="limit") @DefaultValue(value="25") int limit,
			@QueryParam(value="fields") String fields,
			@QueryParam(value="orderby") String orderby,
			@QueryParam(value="abilitato") Boolean abilitato,
			@QueryParam(value="search") String simpleSearch) {
		
		if(limit > 25) limit = 500;
		
		try{
			setupContext(uriInfo, httpHeaders, "findDomini");
			
			UriBuilder baseUriBuilder = uriInfo.getBaseUriBuilder().path("v1");
			
			IAutorizzato user = new UtentiDAO().getUser(getPrincipal());
			
			FindDominiDTO findDominiDTO = new FindDominiDTO(user);
			findDominiDTO.setOffset(offset);
			findDominiDTO.setLimit(limit);
			findDominiDTO.setSimpleSearch(simpleSearch);
			findDominiDTO.setAbilitato(abilitato);
			findDominiDTO.setOrderBy(orderby);
			FindDominiDTOResponse findDominiDTOResponse = new DominiDAO().findDomini(findDominiDTO);
			
			List<Dominio> domini = new ArrayList<Dominio>();
			for(it.govpay.bd.model.Dominio d : findDominiDTOResponse.getDomini()) {
				domini.add(new Dominio(d, baseUriBuilder));
			}
			
			ListaDomini listaDomini = new ListaDomini(domini, uriInfo.getRequestUri(), findDominiDTOResponse.getTotalResults(), offset, limit);
			return Response.status(Status.OK).entity(listaDomini.toJSON(fields)).build();
		} catch (BaseException e) {
			return Response.status(e.getTransportErrorCode()).entity(new Errore(e)).build();
		} catch (Exception e) {
			log.error("Errore interno durante la ricerca dei domini", e);
			return Response.status(Status.INTERNAL_SERVER_ERROR).build();
		} 
	}
	
	@GET
	@Path("/{idDominio}")
	@Produces({MediaType.APPLICATION_JSON})
	public Response getDominio(InputStream is, @Context UriInfo uriInfo, @Context HttpHeaders httpHeaders,
			@PathParam(value="idDominio") String codDominio,
			@QueryParam(value="fields") String fields) {
		
		try{
			setupContext(uriInfo, httpHeaders, "findDomini");
			
			UriBuilder baseUriBuilder = uriInfo.getBaseUriBuilder().path("v1");
			
			IAutorizzato user = new UtentiDAO().getUser(getPrincipal());
			
			GetDominioDTO getDominioDTO = new GetDominioDTO(user, codDominio);
			
			GetDominioDTOResponse getDominioDTOResponse = new DominiDAO().getDominio(getDominioDTO);
			Dominio dominio = new Dominio(getDominioDTOResponse.getDominio(), baseUriBuilder);
			
			return Response.status(Status.OK).entity(dominio.toJSON(fields)).build();
		} catch (NotAuthorizedException e) {
			return Response.status(Status.UNAUTHORIZED).build();
		} catch (Exception e) {
			log.error("Errore interno durante la lettura del dominio", e);
			return Response.status(Status.INTERNAL_SERVER_ERROR).build();
		} 
	}
	
	@GET
	@Path("/{codDominio}/unita")
	@Produces({MediaType.APPLICATION_JSON})
	public Response findUnitaOperative(InputStream is, @Context UriInfo uriInfo, @Context HttpHeaders httpHeaders,
			@PathParam(value="codDominio") String codDominio,
			@QueryParam(value="offset") @DefaultValue(value="0") int offset,
			@QueryParam(value="limit") @DefaultValue(value="25") int limit,
			@QueryParam(value="fields") String fields,
			@QueryParam(value="abilitato") Boolean abilitato,
			@QueryParam(value="simpleSearch") String simpleSearch) {
		
		String methodName = "findUnitaOperative"; 
		GpContext ctx = null; 
		try{
			this.logRequest(uriInfo, httpHeaders, methodName, new ByteArrayOutputStream());
			ctx =  GpThreadLocal.get();
			
			UriBuilder baseUriBuilder = uriInfo.getBaseUriBuilder().path("v1");
			
			IAutorizzato user = new UtentiDAO().getUser(getPrincipal());
			
			FindUnitaOperativeDTO findUnitaOperativeDTO = new FindUnitaOperativeDTO(user, codDominio);
			findUnitaOperativeDTO.setLimit(limit);
			findUnitaOperativeDTO.setOffset(offset);
			findUnitaOperativeDTO.setSimpleSearch(simpleSearch);
			FindUnitaOperativeDTOResponse findUnitaOperativeDTOResponse = new DominiDAO().findUnitaOperative(findUnitaOperativeDTO);
			
			List<UnitaOperativa> unitaOperative = new ArrayList<UnitaOperativa>();
			for(it.govpay.bd.model.UnitaOperativa uo : findUnitaOperativeDTOResponse.getUnitaOperative()) {
				unitaOperative.add(new UnitaOperativa(uo, codDominio, baseUriBuilder));
			}
			ListaUnitaOperative listaUnitaOperative = new ListaUnitaOperative(unitaOperative, uriInfo.getRequestUri(), findUnitaOperativeDTOResponse.getTotalResults(), offset, limit);
			return Response.status(Status.OK).entity(listaUnitaOperative.toJSON(fields)).build();
		} catch (NotAuthorizedException e) {
			this.logResponse(uriInfo, httpHeaders, methodName, new byte[0],401);
			return Response.status(Status.UNAUTHORIZED).build();
		} catch (Exception e) {
			log.error("Errore interno durante il processo di incasso", e);
			this.logResponse(uriInfo, httpHeaders, methodName, new byte[0], 500);
			return Response.status(Status.INTERNAL_SERVER_ERROR).build();
		} finally {
			if(ctx != null) ctx.log();
		}
	}
	
	@GET
	@Path("/{codDominio}/unita/{idUnita}")
	@Produces({MediaType.APPLICATION_JSON})
	public Response getUnitaOperativa(InputStream is, @Context UriInfo uriInfo, @Context HttpHeaders httpHeaders,
			@PathParam(value="codDominio") String codDominio, 
			@PathParam(value="idUnita") String codUnivoco,
			@QueryParam(value="fields") String fields) {
		
		String methodName = "getUnitaOperativa"; 
		GpContext ctx = null; 
		
		try{
			this.logRequest(uriInfo, httpHeaders, methodName, new ByteArrayOutputStream());
			ctx =  GpThreadLocal.get();
			
			UriBuilder baseUriBuilder = uriInfo.getBaseUriBuilder().path("v1");
			
			IAutorizzato user = new UtentiDAO().getUser(getPrincipal());
			
			GetUnitaOperativaDTO getUnitaOperativaDTO = new GetUnitaOperativaDTO(user, codDominio, codUnivoco);
			GetUnitaOperativaDTOResponse getUnitaOperativaDTOResponse = new DominiDAO().getUnitaOperativa(getUnitaOperativaDTO);
			
			UnitaOperativa unitaOperativa = new UnitaOperativa(getUnitaOperativaDTOResponse.getUnitaOperativa(), codDominio, baseUriBuilder);
			
			return Response.status(Status.OK).entity(unitaOperativa.toJSON(fields)).build();
		} catch (NotAuthorizedException e) {
			this.logResponse(uriInfo, httpHeaders, methodName, new byte[0],401);
			return Response.status(Status.UNAUTHORIZED).build();
		} catch (Exception e) {
			log.error("Errore interno durante il processo di incasso", e);
			this.logResponse(uriInfo, httpHeaders, methodName, new byte[0], 500);
			return Response.status(Status.INTERNAL_SERVER_ERROR).build();
		} finally {
			if(ctx != null) ctx.log();
		}
	}
	
	@GET
	@Path("/{codDominio}/iban")
	@Produces({MediaType.APPLICATION_JSON})
	public Response findIban(InputStream is, @Context UriInfo uriInfo, @Context HttpHeaders httpHeaders,
			@PathParam(value="codDominio") String codDominio,
			@QueryParam(value="offset") @DefaultValue(value="0") int offset,
			@QueryParam(value="limit") @DefaultValue(value="25") int limit,
			@QueryParam(value="fields") String fields,
			@QueryParam(value="simpleSearch") String simpleSearch,
			@QueryParam(value="abilitato") Boolean abilitato,
			@QueryParam(value="iban") String iban) {
		
		String methodName = "findIban"; 
		GpContext ctx = null; 
		
		try{
			this.logRequest(uriInfo, httpHeaders, methodName, new ByteArrayOutputStream());
			ctx =  GpThreadLocal.get();
			
			UriBuilder baseUriBuilder = uriInfo.getBaseUriBuilder().path("v1");
			
			IAutorizzato user = new UtentiDAO().getUser(getPrincipal());
			
			FindIbanDTO findIbanDTO = new FindIbanDTO(user, codDominio);
			findIbanDTO.setIban(iban);
			findIbanDTO.setLimit(limit);
			findIbanDTO.setOffset(offset);
			findIbanDTO.setAbilitato(abilitato);
			FindIbanDTOResponse findIbanDTOResponse = new DominiDAO().findIban(findIbanDTO);
			
			List<Iban> ibans = new ArrayList<Iban>();
			for(it.govpay.bd.model.IbanAccredito ibanAccredito : findIbanDTOResponse.getIban()) {
				ibans.add(new Iban(ibanAccredito, codDominio, baseUriBuilder));
			}
			ListaIbanAccredito listaUnitaOperative = new ListaIbanAccredito(ibans, uriInfo.getRequestUri(), findIbanDTOResponse.getTotalResults(), offset, limit);
			return Response.status(Status.OK).entity(listaUnitaOperative.toJSON(fields)).build();
		} catch (NotAuthorizedException e) {
			this.logResponse(uriInfo, httpHeaders, methodName, new byte[0],401);
			return Response.status(Status.UNAUTHORIZED).build();
		} catch (Exception e) {
			log.error("Errore interno durante il processo di incasso", e);
			this.logResponse(uriInfo, httpHeaders, methodName, new byte[0], 500);
			return Response.status(Status.INTERNAL_SERVER_ERROR).build();
		} finally {
			if(ctx != null) ctx.log();
		}
	}
	
	@GET
	@Path("/{codDominio}/iban/{iban}")
	@Produces({MediaType.APPLICATION_JSON})
	public Response getIban(InputStream is, @Context UriInfo uriInfo, @Context HttpHeaders httpHeaders,
			@PathParam(value="codDominio") String codDominio, 
			@PathParam(value="iban") String codIbanAccredito,
			@QueryParam(value="fields") String fields) {
		
		String methodName = "getIban"; 
		GpContext ctx = null; 
		
		try{
			this.logRequest(uriInfo, httpHeaders, methodName, new ByteArrayOutputStream());
			ctx =  GpThreadLocal.get();
			
			UriBuilder baseUriBuilder = uriInfo.getBaseUriBuilder().path("v1");
			
			IAutorizzato user = new UtentiDAO().getUser(getPrincipal());
			
			GetIbanDTO getIbanDTO = new GetIbanDTO(user, codDominio, codIbanAccredito);
			GetIbanDTOResponse getIbanDTOResponse = new DominiDAO().getIban(getIbanDTO);
			
			Iban unitaOperativa = new Iban(getIbanDTOResponse.getIbanAccredito(), codDominio, baseUriBuilder);
			
			return Response.status(Status.OK).entity(unitaOperativa.toJSON(fields)).build();
		} catch (NotAuthorizedException e) {
			this.logResponse(uriInfo, httpHeaders, methodName, new byte[0],401);
			return Response.status(Status.UNAUTHORIZED).build();
		} catch (Exception e) {
			log.error("Errore interno durante il processo di incasso", e);
			this.logResponse(uriInfo, httpHeaders, methodName, new byte[0], 500);
			return Response.status(Status.INTERNAL_SERVER_ERROR).build();
		} finally {
			if(ctx != null) ctx.log();
		}
	}
	
	@GET
	@Path("/{codDominio}/entrate")
	@Produces({MediaType.APPLICATION_JSON})
	public Response findTributi(InputStream is, @Context UriInfo uriInfo, @Context HttpHeaders httpHeaders,
			@PathParam(value="codDominio") String codDominio,
			@QueryParam(value="offset") @DefaultValue(value="0") int offset,
			@QueryParam(value="limit") @DefaultValue(value="25") int limit,
			@QueryParam(value="fields") String fields,
			@QueryParam(value="simpleSearch") String simpleSearch,
			@QueryParam(value="codEntrata") String codEntrata,
			@QueryParam(value="descrizione") String descrizione) {
		
		String methodName = "findIban"; 
		GpContext ctx = null; 
		
		try{
			this.logRequest(uriInfo, httpHeaders, methodName, new ByteArrayOutputStream());
			ctx =  GpThreadLocal.get();
			
			UriBuilder baseUriBuilder = uriInfo.getBaseUriBuilder().path("v1");
			
			IAutorizzato user = new UtentiDAO().getUser(getPrincipal());
			
			FindTributiDTO findTributiDTO = new FindTributiDTO(user, codDominio);
			findTributiDTO.setCodTributo(codEntrata);
			findTributiDTO.setLimit(limit);
			findTributiDTO.setOffset(offset);
			findTributiDTO.setDescrizione(descrizione);
			FindTributiDTOResponse findTributiDTOResponse = new DominiDAO().findTributi(findTributiDTO);
			
			List<Entrata> entrate = new ArrayList<Entrata>();
			for(it.govpay.bd.model.Tributo tributo : findTributiDTOResponse.getTributi()) {
				entrate.add(new Entrata(tributo, codDominio, baseUriBuilder));
			}
			ListaEntrate listaUnitaOperative = new ListaEntrate(entrate, uriInfo.getRequestUri(), findTributiDTOResponse.getTotalResults(), offset, limit);
			return Response.status(Status.OK).entity(listaUnitaOperative.toJSON(fields)).build();
		} catch (NotAuthorizedException e) {
			this.logResponse(uriInfo, httpHeaders, methodName, new byte[0],401);
			return Response.status(Status.UNAUTHORIZED).build();
		} catch (Exception e) {
			log.error("Errore interno durante il processo di incasso", e);
			this.logResponse(uriInfo, httpHeaders, methodName, new byte[0], 500);
			return Response.status(Status.INTERNAL_SERVER_ERROR).build();
		} finally {
			if(ctx != null) ctx.log();
		}
	}
	
	@GET
	@Path("/{codDominio}/entrate/{codEntrata}")
	@Produces({MediaType.APPLICATION_JSON})
	public Response getTributo(InputStream is, @Context UriInfo uriInfo, @Context HttpHeaders httpHeaders,
			@PathParam(value="codDominio") String codDominio, 
			@PathParam(value="codEntrata") String codTributo,
			@QueryParam(value="fields") String fields) {
		
		String methodName = "getTributo"; 
		GpContext ctx = null; 
		
		try{
			this.logRequest(uriInfo, httpHeaders, methodName, new ByteArrayOutputStream());
			ctx =  GpThreadLocal.get();
			
			UriBuilder baseUriBuilder = uriInfo.getBaseUriBuilder().path("v1");
			
			IAutorizzato user = new UtentiDAO().getUser(getPrincipal());
			
			GetTributoDTO getTributoDTO = new GetTributoDTO(user, codDominio, codTributo);
			GetTributoDTOResponse getTributoDTOResponse = new DominiDAO().getTributo(getTributoDTO);
			
			Entrata entrata = new Entrata(getTributoDTOResponse.getTributo(), codDominio, baseUriBuilder);
			
			return Response.status(Status.OK).entity(entrata.toJSON(fields)).build();
		} catch (NotAuthorizedException e) {
			this.logResponse(uriInfo, httpHeaders, methodName, new byte[0],401);
			return Response.status(Status.UNAUTHORIZED).build();
		} catch (Exception e) {
			log.error("Errore interno durante il processo di incasso", e);
			this.logResponse(uriInfo, httpHeaders, methodName, new byte[0], 500);
			return Response.status(Status.INTERNAL_SERVER_ERROR).build();
		} finally {
			if(ctx != null) ctx.log();
		}
	}
	
	
}
