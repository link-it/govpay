/*
 * GovPay - Porta di Accesso al Nodo dei Pagamenti SPC 
 * http://www.govpay.it
 * 
 * Copyright (c) 2014-2015 Link.it srl (http://www.link.it).
 * Copyright (c) 2014-2015 TAS S.p.A. (http://www.tasgroup.it).
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
package it.govpay.test.web.v2.ws;

import it.govpay.servizi.pa.Anagrafica;
import it.govpay.servizi.pa.Autenticazione;
import it.govpay.servizi.pa.Canale;
import it.govpay.servizi.pa.CodEsito;
import it.govpay.servizi.pa.GpGeneraRpt;
import it.govpay.servizi.pa.GpGeneraRptResponse;
import it.govpay.servizi.pa.PagamentiTelematiciGPPrt;
import it.govpay.servizi.pa.PagamentiTelematiciGPPrtservice;
import it.govpay.servizi.pa.Pagamento;
import it.govpay.servizi.pa.SingoloPagamento;
import it.govpay.servizi.pa.TipoIuv;
import it.govpay.servizi.pa.TipoVersamento;
import it.govpay.test.web.utils.ConfigurationUtils;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.Date;
import java.util.UUID;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.xml.soap.MessageFactory;
import javax.xml.soap.SOAPMessage;
import javax.xml.ws.BindingProvider;

import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.servlet.ServletContextHandler;
import org.eclipse.jetty.servlet.ServletHolder;
import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

@Test
public class GPPrt {

	private PagamentiTelematiciGPPrt port;
	private long sleep4Result = 4000;
	private static Server server;
	boolean received = false;
	
	@BeforeClass
	public void setup() throws Exception {
		String username = ConfigurationUtils.getProperty("string.portale.username");
		String password = ConfigurationUtils.getProperty("string.portale.password");
		
		String url = ConfigurationUtils.getProperty("url.govpay.servizioPortali");
		port = new PagamentiTelematiciGPPrtservice().getGPPrtPort();
		((BindingProvider)port).getRequestContext().put(BindingProvider.USERNAME_PROPERTY, username);
		((BindingProvider)port).getRequestContext().put(BindingProvider.PASSWORD_PROPERTY, password);
		((BindingProvider)port).getRequestContext().put(BindingProvider.ENDPOINT_ADDRESS_PROPERTY, url);
		server = new Server(Integer.parseInt(ConfigurationUtils.getProperty("int.applicazione.port")));
		
		ServletContextHandler context = new ServletContextHandler();
        context.setContextPath("/");
        server.setHandler(context);
        server.start();
        // Serve capture servlet
        context.addServlet(new ServletHolder(new NotificaServlet()),ConfigurationUtils.getProperty("string.applicazione.path"));
	}
	
	class NotificaServlet extends HttpServlet {
		private static final long serialVersionUID = 1L;
		@Override
		protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
			try {
				SOAPMessage sm = MessageFactory.newInstance().createMessage(null, req.getInputStream());
				sm.writeTo(System.out);
				received = true;
			} catch (Exception e) {
				resp.sendError(500);
			}
		}
	}
	
	private GpGeneraRpt creaGpGeneraRpt() throws IOException{
		String codPsp = ConfigurationUtils.getProperty("string.codPsp");
		String codPortale = ConfigurationUtils.getProperty("string.codPortale");
		String codApplicazione = ConfigurationUtils.getProperty("string.codApplicazione");
		String codEnte = ConfigurationUtils.getProperty("string.codEnte");
		String codTributo = ConfigurationUtils.getProperty("string.codTributo");
		String backurl = ConfigurationUtils.getProperty("url.portale.back");
		
		GpGeneraRpt req = new GpGeneraRpt();
		req.setAutenticazione(Autenticazione.N_A);
		
		req.setCallbackUrl(backurl);
		Canale canale = new Canale();
		canale.setCodPsp(codPsp);
		canale.setTipoVersamento(TipoVersamento.CP);
		req.setCanale(canale);
		req.setCodPortale(codPortale);
		Pagamento p = new Pagamento();
		p.setCausale("Causale");
		p.setCodApplicazione(codApplicazione);
		p.setCodEnte(codEnte);
		p.setTipoIuv(TipoIuv.IUV_INIZIATIVA_ENTE);
		p.setDataScadenza(new Date());
		
		Anagrafica anagraficaDebitore = new Anagrafica();
		anagraficaDebitore.setCodUnivoco("");
		anagraficaDebitore.setRagioneSociale("Mario Rossi");
		anagraficaDebitore.setEmail("nardi@link.it");
		anagraficaDebitore.setCodUnivoco("RSSMRA85T10A562S");
		anagraficaDebitore.setIndirizzo("Via Roma");
		anagraficaDebitore.setCivico("1");
		anagraficaDebitore.setLocalita("Roma");
		anagraficaDebitore.setProvincia("Roma");
		anagraficaDebitore.setNazione("IT");
		anagraficaDebitore.setCap("00000");
		p.setDebitore(anagraficaDebitore);
		p.setImportoTotale(new BigDecimal(1000));
		{
			SingoloPagamento sp = new SingoloPagamento();
			sp.setAnnoRiferimento(2015);
			sp.setCodTributo(codTributo);
			sp.setCodVersamentoEnte(UUID.randomUUID().toString());
			sp.setImporto(new BigDecimal(1000));
			p.getSingoloPagamento().add(sp);
		}
		req.getPagamento().add(p);
		return req;
	}

	public void richiestaSenzaIUV_ESEGUITO() throws Exception {
		
		System.out.println(">>>>>>>>>> Invio richiesta di pagamento senza IUV");
		GpGeneraRpt request = creaGpGeneraRpt();
		GpGeneraRptResponse response = port.gpGeneraRpt(request);
		
		System.out.println(">>>>>>>>>> Ritornato esito con codice " + response.getCodEsito());
		Assert.assertEquals(response.getCodEsito(), CodEsito.OK);
		
		System.out.println(">>>>>>>>>> IUV associato al pagamento: " + response.getIdPagamento().get(0).getIuv());
		
		if(response.getUrl() != null) {
			System.out.println();
			System.out.println(">>>>>>>>>> Procedere al pagamento utilizzando un browser alla seguente URL:");
			System.out.println(response.getUrl());
		}
		
		System.out.println(">>>>>>>>>> Sistema in attesa della notifica da GovPay....");

		//Attendo la consegna della ricevuta
		while (!received)
			Thread.sleep(sleep4Result);
		
		 server.stop();
	}
}

