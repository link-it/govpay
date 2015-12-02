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

import java.util.List;

import it.govpay.bd.anagrafica.IntermediariBD;
import it.govpay.bd.model.Connettore;
import it.govpay.bd.model.Connettore.EnumAuthType;
import it.govpay.bd.model.Connettore.EnumSslType;
import it.govpay.bd.model.Intermediario;
import it.govpay.test.BasicTest;

import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

public class IntermediariTest extends BasicTest {
	
	@BeforeClass
	public void setupDB() throws Exception {
		reInitDataBase();
	}

	@Test(groups = {"anagrafica", "intermediario"})
	public void inserisciIntermediario() throws Exception {
		IntermediariBD intermediariBD = new IntermediariBD(bd);

		Intermediario intermediario = new Intermediario();
		intermediario.setCodIntermediario("CodIntemediario");
		intermediario.setDenominazione("Denominazione");
		Connettore connettorePdd = new Connettore();
		connettorePdd.setAzioneInUrl(false);
		connettorePdd.setTipoAutenticazione(EnumAuthType.HTTPBasic);
		connettorePdd.setHttpPassw("HttpPassw");
		connettorePdd.setHttpUser("HttpUser");
		intermediario.setConnettorePdd(connettorePdd);
		intermediariBD.insertIntermediario(intermediario);

		Assert.assertTrue(intermediario.getId() != null, "L'intermediario inserito non ha l'Id valorizzato.");
		
		Intermediario intermediarioLetto = intermediariBD.getIntermediario("CodIntemediario");
		Assert.assertTrue(intermediario.equals(intermediarioLetto), "L'intermediario letto da database con CodIntermediario e' diverso da quello inserito");

		intermediarioLetto = intermediariBD.getIntermediario(intermediario.getId());
		Assert.assertTrue(intermediario.equals(intermediarioLetto), "L'intermediario letto da database con Id e' diverso da quello inserito");
	}
	
	@Test(groups = {"anagrafica", "intermediario"}, dependsOnMethods = { "inserisciIntermediario" })
	public void aggiornaIntermediario() throws Exception {
		IntermediariBD intermediariBD = new IntermediariBD(bd);

		Intermediario intermediario = new Intermediario();
		intermediario.setDenominazione("Denominazione");
		intermediario.setCodIntermediario("CodIntemediario");
		Connettore connettorePdd = new Connettore();
		connettorePdd.setAzioneInUrl(true);
		connettorePdd.setTipoAutenticazione(EnumAuthType.SSL);
		connettorePdd.setTipoSsl(EnumSslType.SERVER);
		connettorePdd.setSslTsLocation("sslTsLocation");
		connettorePdd.setSslTsPasswd("sslTsPasswd");
		connettorePdd.setSslTsType("sslTsType");
		
		intermediario.setConnettorePdd(connettorePdd);
		intermediariBD.updateIntermediario(intermediario);

		Assert.assertTrue(intermediario.getId() != null, "L'intermediario aggiornato non ha l'Id valorizzato.");
		
		Intermediario intermediarioLetto = intermediariBD.getIntermediario("CodIntemediario");
		
		Assert.assertTrue(intermediario.equals(intermediarioLetto), "L'intermediario letto da database con CodIntermediario e' diverso da quello inserito");

		intermediarioLetto = intermediariBD.getIntermediario(intermediario.getId());
		Assert.assertTrue(intermediario.equals(intermediarioLetto), "L'intermediario letto da database con Id e' diverso da quello inserito");
	}
	
	@Test(groups = {"anagrafica", "intermediario"} , dependsOnMethods = { "aggiornaIntermediario" })
	public void cercaIntermediario() throws Exception {
		IntermediariBD intermediariBD = new IntermediariBD(bd);

		Intermediario intermediario = new Intermediario();
		intermediario.setCodIntermediario("CodIntemediario2");
		intermediario.setDenominazione("Denominazione");
		Connettore connettorePdd = new Connettore();
		connettorePdd.setAzioneInUrl(false);
		connettorePdd.setTipoAutenticazione(EnumAuthType.HTTPBasic);
		connettorePdd.setHttpPassw("HttpPassw");
		connettorePdd.setHttpUser("HttpUser");
		intermediario.setConnettorePdd(connettorePdd);
		
		intermediariBD.insertIntermediario(intermediario);
		
		List<Intermediario> intermediari = intermediariBD.getIntermediari();
		Assert.assertTrue(intermediari.size() == 2, "Letti da database " + intermediari.size() + " intermediari, ma ne erano attesi 2");
		Assert.assertTrue(intermediari.contains(intermediario), "La lista di intermediari restituiti dalla findAll non contiene l'intermediario precedentemente inserito");
	}
	
}
