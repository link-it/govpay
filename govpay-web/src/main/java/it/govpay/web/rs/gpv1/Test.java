/*
 * GovPay - Porta di Accesso al Nodo dei Pagamenti SPC 
 * http://www.gov4j.it/govpay
 * 
 * Copyright (c) 2014-2015 Link.it srl (http://www.link.it).
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
package it.govpay.web.rs.gpv1;

import it.govpay.utils.LoggerConfiguration;

import java.util.Date;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.Response;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openspcoop2.utils.logger.Event;
import org.openspcoop2.utils.logger.ILogger;
import org.openspcoop2.utils.logger.LoggerFactory;
import org.openspcoop2.utils.logger.LowSeverity;
import org.openspcoop2.utils.logger.Severity;

@Path("/test")
public class Test {

	Logger log = LogManager.getLogger(Test.class);

	@Context HttpServletRequest httpServletRequest;

	@GET
	@Path("/test")
	public Response backUrl() {

		try {
			LoggerConfiguration.init();

			ILogger logger = LoggerFactory.newLogger();
			logger.log("Prova altro log con messaggio inserito qua",LowSeverity.DEBUG_HIGH);
			logger.log("Prova altro log con messaggio inserito qua con function",LowSeverity.DEBUG_MEDIUM,"core");

			logger.log("000002");
			logger.log("core.inizializzazione"); 

			logger.log("001001","21 Ottobre 2015","GovPay");

			logger.log();

			Event e = new Event();
			e.setCode("xxxx");
			e.setDate(new Date());
			e.setSeverity(Severity.ERROR);
			logger.log(e);

		} catch (Exception e) {
			log.error("Errore: " + e.getMessage(), e);
			return Response.status(500).entity("NO Bravo: " + e.getMessage()).build();
		}

		return Response.ok("Bravo").build();
	}
}

