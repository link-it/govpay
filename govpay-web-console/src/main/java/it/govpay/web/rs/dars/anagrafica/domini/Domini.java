/*
 * GovPay - Porta di Accesso al Nodo dei Pagamenti SPC 
 * http://www.gov4j.it/govpay
 * 
 * Copyright (c) 2014-2016 Link.it srl (http://www.link.it).
 * 
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
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
package it.govpay.web.rs.dars.anagrafica.domini;

import java.io.IOException;
import java.io.OutputStream;
import java.util.List;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.StreamingOutput;
import javax.ws.rs.core.UriInfo;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import it.govpay.bd.BasicBD;
import it.govpay.bd.anagrafica.DominiBD;
import it.govpay.bd.anagrafica.IbanAccreditoBD;
import it.govpay.bd.anagrafica.filters.IbanAccreditoFilter;
import it.govpay.bd.model.Dominio;
import it.govpay.bd.model.IbanAccredito;
import it.govpay.core.utils.DominioUtils;
import it.govpay.web.rs.dars.BaseDarsService;
import it.govpay.web.rs.dars.IDarsHandler;
import it.govpay.web.rs.dars.exception.ConsoleException;

@Path("/dars/domini")
public class Domini extends BaseDarsService {

	public Domini() {
		super();
	}

	Logger log = LogManager.getLogger();

	@Override
	public String getNomeServizio() {
		return "domini";
	}

	@Override
	public IDarsHandler<?> getDarsHandler() {
		return new DominiHandler(this.log, this);
	}

	@Override
	public String getPathServizio() {
		return "/dars/" + this.getNomeServizio();
	}

	@GET
	@Path("/{id}/contiAccredito")
	@Produces(MediaType.APPLICATION_OCTET_STREAM)
	public Response getContiAccredito(@Context UriInfo uriInfo, @PathParam("id") long id) throws ConsoleException,WebApplicationException {

		String methodName = "getContiAccredito " + this.getNomeServizio(); 
		this.initLogger(methodName);

		BasicBD bd = null;

		try{
			bd = BasicBD.newInstance(this.codOperazione);
			DominiBD dominiBD = new DominiBD(bd);
			Dominio dominio = dominiBD.getDominio(id);

			IbanAccreditoBD ibanAccreditoDB = new IbanAccreditoBD(bd);
			IbanAccreditoFilter filter = ibanAccreditoDB.newFilter();
			filter.setIdDominio(id);
			List<IbanAccredito> ibans = ibanAccreditoDB.findAll(filter);
			final byte[] contiAccredito = DominioUtils.buildInformativaContoAccredito(dominio, ibans);
			StreamingOutput stream = new StreamingOutput() {

				public void write(OutputStream output) throws IOException, WebApplicationException {
					try {
						output.write(contiAccredito);
					}
					catch (Exception e) {
						throw new WebApplicationException(e);
					}
				}
			};
			this.log.info("Richiesta "+methodName +" evasa con successo");
			return Response.ok(stream, MediaType.APPLICATION_OCTET_STREAM).header("content-disposition", "attachment; filename=\"ContiAccredito.xml\"").build();

		} catch (WebApplicationException e){
			this.log.error("Riscontrato errore di autorizzazione durante l'esecuzione del metodo "+methodName+":" +e.getMessage() , e);
			return Response.serverError().build();
		} catch (Exception e) {
			this.log.error("Riscontrato errore durante l'esecuzione del metodo "+methodName+":" +e.getMessage() , e);
			if(bd != null) 
				bd.rollback();
			return Response.serverError().build();
		}finally {
			this.response.setHeader("Access-Control-Allow-Origin", "*");
			if(bd != null) bd.closeConnection();
		}

	}
	
	@GET
	@Path("/{id}/informativa")
	@Produces(MediaType.APPLICATION_OCTET_STREAM)
	public Response getInformativa(@Context UriInfo uriInfo, @PathParam("id") long id) throws ConsoleException,WebApplicationException {

		String methodName = "getInformativa " + this.getNomeServizio(); 
		this.initLogger(methodName);

		BasicBD bd = null;

		try{
			bd = BasicBD.newInstance(this.codOperazione);
			DominiBD dominiBD = new DominiBD(bd);
			Dominio dominio = dominiBD.getDominio(id);

			final byte[] informativa = DominioUtils.buildInformativaControparte(dominio, true);
			StreamingOutput stream = new StreamingOutput() {

				public void write(OutputStream output) throws IOException, WebApplicationException {
					try {
						output.write(informativa);
					}
					catch (Exception e) {
						throw new WebApplicationException(e);
					}
				}
			};
			this.log.info("Richiesta "+methodName +" evasa con successo");
			return Response.ok(stream, MediaType.APPLICATION_OCTET_STREAM).header("content-disposition", "attachment; filename=\"Informativa.xml\"").build();

		} catch (WebApplicationException e){
			this.log.error("Riscontrato errore di autorizzazione durante l'esecuzione del metodo "+methodName+":" +e.getMessage() , e);
			return Response.serverError().build();
		} catch (Exception e) {
			this.log.error("Riscontrato errore durante l'esecuzione del metodo "+methodName+":" +e.getMessage() , e);
			if(bd != null) 
				bd.rollback();
			return Response.serverError().build();
		}finally {
			this.response.setHeader("Access-Control-Allow-Origin", "*");
			if(bd != null) bd.closeConnection();
		}

	}


}

