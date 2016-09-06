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
package it.govpay.web.rs;

import java.net.HttpURLConnection;
import java.net.URL;

import it.gov.digitpa.schemas._2011.ws.paa.NodoChiediInformativaPSP;
import it.govpay.bd.BasicBD;
import it.govpay.bd.anagrafica.DominiBD;
import it.govpay.bd.model.Dominio;
import it.govpay.bd.model.Intermediario;
import it.govpay.bd.model.Stazione;
import it.govpay.core.exceptions.GovPayException;
import it.govpay.core.utils.GovpayConfig;
import it.govpay.core.utils.GpContext;
import it.govpay.core.utils.GpThreadLocal;
import it.govpay.core.utils.client.NodoClient;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Response;

import org.apache.commons.io.IOUtils;
import org.apache.logging.log4j.ThreadContext;
import org.openspcoop2.generic_project.exception.NotFoundException;
import org.openspcoop2.utils.logger.beans.proxy.Actor;
import org.openspcoop2.utils.logger.beans.proxy.Operation;
import org.openspcoop2.utils.logger.beans.proxy.Server;
import org.openspcoop2.utils.logger.beans.proxy.Service;
import org.openspcoop2.utils.logger.constants.proxy.FlowMode;

@Path("/check")
public class Check {

	@GET
	@Path("/")
	public Response verifica(
			@QueryParam(value = "matchString") String matchString) {
		BasicBD bd = null;

		try {
			DominiBD dominiBD = null;
			try {
				bd = BasicBD.newInstance(GpThreadLocal.get().getTransactionId());
				dominiBD = new DominiBD(bd);
				dominiBD.count(dominiBD.newFilter());
			} catch(Exception e) {
				throw new Exception("Errore di connessione al database");
			}

			try {
				URL url = GovpayConfig.getInstance().getUrlPddVerifica();
				HttpURLConnection conn = (HttpURLConnection) url.openConnection();
				String checkResult = null;
				conn.connect();

				int responseCode = conn.getResponseCode();

				String bodyResponse = null;

				if(responseCode > 299) {
					checkResult = "Ottenuto response code ["+responseCode+"] durante la connessione a ["+url+"]";
				} 

				if(matchString != null) {
					checkResult = null;

					if(responseCode < 300) {
						if(conn.getInputStream() != null) {
							bodyResponse = new String(conn.getInputStream() != null ? IOUtils.toByteArray(conn.getInputStream()) : new byte[]{});
						}
					} else {
						if(conn.getErrorStream() != null) {
							bodyResponse = new String(conn.getErrorStream() != null ? IOUtils.toByteArray(conn.getErrorStream()) : new byte[]{});
						}
					}

					if(bodyResponse == null || !bodyResponse.contains(matchString))
						checkResult = "Ottenuta risposta che non contiene la matchString ["+matchString+"] durante la connessione a ["+url+"]";
				}

				if(checkResult != null)
					throw new Exception(checkResult);

			} catch(Exception e) {
				throw new Exception("Errore di connessione alla PDD: " + e.getMessage());
			}

			return Response.ok().build();
		} catch (Exception e) {
			return Response.status(500).entity(e.getMessage()).build();
		} finally {
			if(bd!= null) bd.closeConnection();
		}
	}
	
	
	@GET
	@Path("/{codDominio}")
	public Response verificaDominio(@PathParam(value = "codDominio") String codDominio) {
		BasicBD bd = null;
		GpContext ctx = null;
		try {
			ctx = new GpContext();
			Service service = new Service();
			service.setName("Check");
			ctx.getTransaction().setService(service);
			
			Operation operation = new Operation();
			operation.setMode(FlowMode.INPUT_OUTPUT);
			operation.setName("VerificaDominio");
			ctx.getTransaction().setOperation(operation);
			
			Server server = new Server();
			server.setName(GpContext.GovPay);
			ctx.getTransaction().setServer(server);
			
			Actor to = new Actor();
			to.setName(GpContext.GovPay);
			to.setType(GpContext.TIPO_SOGGETTO_GOVPAY);
			ctx.getTransaction().setTo(to);
			
//			HttpServletRequest servletRequest = (HttpServletRequest) msgCtx.get(MessageContext.SERVLET_REQUEST);
//			Client client = new Client();
//			client.setInvocationEndpoint(servletRequest.getRequestURI());
//			client.setInterfaceName(((QName) msgCtx.get(MessageContext.WSDL_INTERFACE)).getLocalPart());
//			if(((HttpServletRequest) msgCtx.get(MessageContext.SERVLET_REQUEST)).getUserPrincipal() != null)
//				client.setPrincipal(((HttpServletRequest) msgCtx.get(MessageContext.SERVLET_REQUEST)).getUserPrincipal().getName());
//			transaction.setClient(client);
			
			ThreadContext.put("op", ctx.getTransactionId());
			GpThreadLocal.set(ctx);
			
			bd = BasicBD.newInstance(GpThreadLocal.get().getTransactionId());
			DominiBD dominiBD = new DominiBD(bd);
			Dominio d = null;
			
			try {
				d = dominiBD.getDominio(codDominio);
				Stazione s = d.getStazione(bd);
				Intermediario i = s.getIntermediario(bd);
				NodoClient c = new NodoClient(i);
				NodoChiediInformativaPSP richiesta = new NodoChiediInformativaPSP();
				richiesta.setIdentificativoDominio(d.getCodDominio());
				richiesta.setIdentificativoIntermediarioPA(i.getCodIntermediario());
				richiesta.setIdentificativoStazioneIntermediarioPA(s.getCodStazione());
				richiesta.setPassword(s.getPassword());
				c.nodoChiediInformativaPSP(richiesta, "");
			} catch(NotFoundException e) {
				throw new Exception("Dominio [" + codDominio + "] non censito in anagrafica.");
			} catch(GovPayException e) {
				throw new Exception(e.getCodEsito().toString() + ": " + e.getMessage());
			} catch(Exception e) {
				throw new Exception("Impossibile invocare il Nodo dei Pagamenti: " + e.getMessage());
			}
			return Response.ok().build();
		} catch (Exception e) {
			return Response.status(500).entity(e.getMessage()).build();
		} finally {
			if(bd!= null) bd.closeConnection();
			if(ctx != null) ctx.log();
		}
	}
}

