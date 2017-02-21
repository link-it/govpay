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

import java.net.HttpURLConnection;
import java.net.URL;
import java.util.UUID;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Response;

import org.apache.commons.io.IOUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.ThreadContext;
import org.openspcoop2.generic_project.exception.NotFoundException;
import org.openspcoop2.generic_project.exception.ServiceException;
import org.openspcoop2.utils.logger.beans.proxy.Actor;
import org.openspcoop2.utils.logger.beans.proxy.Operation;
import org.openspcoop2.utils.logger.beans.proxy.Server;
import org.openspcoop2.utils.logger.beans.proxy.Service;
import org.openspcoop2.utils.logger.constants.proxy.FlowMode;

import it.govpay.bd.BasicBD;
import it.govpay.bd.anagrafica.AnagraficaManager;
import it.govpay.bd.anagrafica.DominiBD;
import it.govpay.bd.anagrafica.StazioniBD;
import it.govpay.bd.model.Dominio;
import it.govpay.bd.model.Stazione;
import it.govpay.bd.wrapper.StatoNdP;
import it.govpay.core.utils.GovpayConfig;
import it.govpay.core.utils.GpContext;
import it.govpay.core.utils.GpThreadLocal;

@Path("/check")
public class Check {

	@GET
	@Path("/")
	public Response verifica(
			@QueryParam(value = "matchString") String matchString) {
		BasicBD bd = null;
		Logger log = LogManager.getLogger();
		try {
			DominiBD dominiBD = null;
			try {
				bd = BasicBD.newInstance(UUID.randomUUID().toString());
				dominiBD = new DominiBD(bd);
				dominiBD.count(dominiBD.newFilter());
			} catch(Exception e) {
				log.error("Errore di connessione al database", e);
				throw new Exception("Errore di connessione al database");
			} finally {
				if(bd!= null) bd.closeConnection();
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
				log.error("Errore di connessione alla PDD", e);
				throw new Exception("Errore di connessione alla PDD: " + e.getMessage());
			}

			return Response.ok().build();
		} catch (Exception e) {
			return Response.status(500).entity(e.getMessage()).build();
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
			
			ThreadContext.put("op", ctx.getTransactionId());
			GpThreadLocal.set(ctx);
			
			bd = BasicBD.newInstance(GpThreadLocal.get().getTransactionId());
			
			
			try {
				DominiBD dominiBD = new DominiBD(bd);
				StazioniBD stazioniBD = new StazioniBD(bd);
				
				Dominio d = AnagraficaManager.getDominio(bd, codDominio);
				Stazione stazione = d.getStazione(bd);
				
				StatoNdP statoDominioNdp = dominiBD.getStatoNdp(d.getId());
				StatoNdP statoStazioneNdp = stazioniBD.getStatoNdp(stazione.getId());
				
				if(statoDominioNdp.getCodice() == null)
					return Response.ok().entity("STATO NON VERIFICATO").build();
				
				if(statoDominioNdp.getCodice().intValue() == 0 && statoStazioneNdp.getCodice() == null)
					return Response.ok().entity("DOMINIO OK").build();
				
				if(statoDominioNdp.getCodice().intValue() == 0 && statoStazioneNdp.getCodice().intValue() == 0)
					return Response.ok().entity("DOMINIO OK").build();
				
				if(statoDominioNdp.getCodice().intValue() == 0 && statoStazioneNdp.getCodice().intValue() != 0)
					return Response.status(500).entity("STAZIONE KO (" + statoStazioneNdp.getOperazione() + "): " + statoStazioneNdp.getDescrizione()).build();
				
				if(statoDominioNdp.getCodice().intValue() != 0)
					return Response.status(500).entity("DOMINIO KO (" + statoDominioNdp.getOperazione() + "): " + statoDominioNdp.getDescrizione()).build();

			} catch(NotFoundException e) {
				return Response.status(404).build();
			} 
			return Response.ok().build();
		} catch (ServiceException e) {
			return Response.status(500).entity(e.getMessage()).build();
		} finally {
			if(bd!= null) bd.closeConnection();
			if(ctx != null) ctx.log();
		}
	}
}

