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
package it.govpay.test.bd.anagrafica;

import it.govpay.bd.anagrafica.PortaliBD;
import it.govpay.bd.model.Portale;
import it.govpay.test.BasicTest;

import java.util.ArrayList;
import java.util.List;

import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;
@Test(groups = {"portale", "anagrafica"}, dependsOnGroups = {"ente", "stazione"})
public class PortaliTest extends BasicTest {
	
	@BeforeClass
	public void setupDB() throws Exception {
		reInitDataBase();
	}
	
	@Test
	public void inserisciPortale() throws Exception {
		setupApplicazioni();
		setupStazione();
		
		Portale portale = new Portale();
		portale.setAbilitato(true);
		portale.setCodPortale("CodPortale");
		portale.setDefaultCallbackURL("http://defaultCallbackURL");
		portale.setIdStazione(stazione.getId());
		List<Long> idApplicazioni = new ArrayList<Long>();
		idApplicazioni.add(applicazioneAA.getId());
		idApplicazioni.add(applicazioneAB.getId());
		portale.setIdApplicazioni(idApplicazioni);
		portale.setPrincipal("Principal");
		
		PortaliBD portaliBD = new PortaliBD(bd);
		portaliBD.insertPortale(portale);
		
		Assert.assertTrue(portale.getId() != null, "Il portale inserito non ha l'Id valorizzato.");
		
		Portale portaleLetto = portaliBD.getPortale(portale.getCodPortale());
		Assert.assertTrue(portale.equals(portaleLetto), "Il portale letto da database con CodPortale e' diverso da quello inserito");
		
		portaleLetto = portaliBD.getPortale(portale.getId());
		Assert.assertTrue(portale.equals(portaleLetto), "Il portale letto da database con Id e' diverso da quello inserito");
		
		portaleLetto = portaliBD.getPortaleByPrincipal(portale.getPrincipal());
		Assert.assertTrue(portale.equals(portaleLetto), "Il portale letto da database con Principal e' diverso da quello inserito");
	}
	
	@Test(dependsOnMethods = { "inserisciPortale" })
	public void aggiornaPortale() throws Exception {
		Portale portale = new Portale();
		portale.setAbilitato(true);
		portale.setCodPortale("CodPortale");
		portale.setIdStazione(stazione.getId());
		portale.setDefaultCallbackURL("http://defaultCallbackURL");
		List<Long> idApplicazioni = new ArrayList<Long>();
		idApplicazioni.add(applicazioneAB.getId());
		idApplicazioni.add(applicazioneBB.getId());
		portale.setIdApplicazioni(idApplicazioni);
		portale.setPrincipal("PrincipalUpd");
		
		PortaliBD portaliBD = new PortaliBD(bd);
		portaliBD.updatePortale(portale);
		
		Assert.assertTrue(portale.getId() != null, "Il portale inserito non ha l'Id valorizzato.");
		
		Portale portaleLetto = portaliBD.getPortale(portale.getCodPortale());
		Assert.assertTrue(portale.equals(portaleLetto), "Il portale letto da database con CodPortale e' diverso da quello inserito");
	}
	
	@Test(groups = {"anagrafica", "portale"}, dependsOnMethods = { "inserisciPortale" })
	public void cercaPortale() throws Exception {
		PortaliBD portaliBD = new PortaliBD(bd);
		
		Portale portale = new Portale();
		portale.setAbilitato(true);
		portale.setCodPortale("CodPortale2");
		portale.setIdStazione(stazione.getId());
		portale.setDefaultCallbackURL("http://defaultCallbackURL");
		List<Long> idApplicazioni = new ArrayList<Long>();
		idApplicazioni.add(applicazioneAB.getId());
		idApplicazioni.add(applicazioneBB.getId());
		portale.setIdApplicazioni(idApplicazioni);
		portale.setPrincipal("Principal2");
		portaliBD.insertPortale(portale);
		
		portale = new Portale();
		portale.setAbilitato(true);
		portale.setCodPortale("CodPortale3");
		portale.setDefaultCallbackURL("http://defaultCallbackURL");
		portale.setIdStazione(stazione.getId());
		idApplicazioni = new ArrayList<Long>();
		idApplicazioni.add(applicazioneBB.getId());
		portale.setIdApplicazioni(idApplicazioni);
		portale.setPrincipal("Principal3");
		portaliBD.insertPortale(portale);
		
		List<Portale> portali = portaliBD.getPortali();
		Assert.assertTrue(portali.size() == 3, "Trovate " + portali.size() + " portali. Attesi 3 portali.");
		Assert.assertTrue(portali.contains(portale), "La lista restituita dalla findAll non contiene il portale precedentemente inserito");
	}
}
