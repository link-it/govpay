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
package it.govpay.test.web;

import it.govpay.bd.anagrafica.AnagraficaManager;
import it.govpay.bd.model.Applicazione;
import it.govpay.bd.model.Dominio;
import it.govpay.bd.model.Ente;
import it.govpay.bd.model.IbanAccredito;
import it.govpay.bd.model.Portale;
import it.govpay.bd.model.Tributo;
import it.govpay.business.RegistroPSP;
import it.govpay.utils.GovPayConfiguration;

import java.net.HttpURLConnection;
import java.net.URL;

import org.subethamail.wiser.Wiser;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;

public class BasicTest extends it.govpay.test.BasicTest {

	protected Portale portale_v1;
	protected Applicazione applicazione_v1;
	protected Ente ente_v1;
	protected Tributo tributo_v1;
	protected Dominio dominio_v1;
	protected IbanAccredito ibanAccredito_v1;

	protected static Wiser wiser;

	@BeforeClass
	public static void setUpMailServer() throws Exception {
		wiser = new Wiser();
		wiser.setPort(GovPayConfiguration.newInstance().getMail_serverPort());
		wiser.setHostname(GovPayConfiguration.newInstance().getMail_serverHost());
		wiser.start();
		
		AnagraficaManager.newInstance();
	}
	
	
	@AfterClass
	public static void tearDownMailServer() throws Exception {
		wiser.stop();
	}
	
	@BeforeClass
	protected void cleanNdpSym() throws Exception {
		URL obj = new URL("http://localhost:8080/govpay-ndp-sym/cleanPagamenti");
		HttpURLConnection con = (HttpURLConnection) obj.openConnection();
		con.setRequestMethod("GET");
		con.getResponseCode();
	}

	protected void setupRegistro() throws Exception {
		setupAnagrafica();
		RegistroPSP registroPSP = new RegistroPSP(bd);
		registroPSP.aggiornaRegistro();
	}


}
