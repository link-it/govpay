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

import it.govpay.bd.anagrafica.EntiBD;
import it.govpay.bd.model.Anagrafica;
import it.govpay.bd.model.BasicModel;
import it.govpay.bd.model.Ente;
import it.govpay.test.BasicTest;

import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

@Test(dependsOnGroups = {"dominio", "mailTemplate"})
public class EntiTest extends BasicTest {

	@BeforeClass
	public void setupDB() throws Exception {
		reInitDataBase();
	}
	
	@Test(groups = {"anagrafica", "ente"})
	public void inserisciEnte() throws Exception {
		setupDominio();
		setupMailTemplate();
		
		Ente ente = new Ente();
		ente.setAttivo(true);
		ente.setIdDominio(dominioA.getId());
//		ente.setIdMailTemplateRPT(mailTemplateRPT.getId());
//		ente.setIdMailTemplateRT(mailTemplateRT.getId());
		ente.setIdDominio(dominioA.getId());
		ente.setCodEnte("CodEnte");
		Anagrafica anagraficaEnte = new Anagrafica();
		anagraficaEnte.setCap("Cap");
		anagraficaEnte.setCellulare("Cellulare");
		anagraficaEnte.setCivico("Civico");
		anagraficaEnte.setCodUnivoco("CodUnivoco");
		anagraficaEnte.setEmail("Email");
		anagraficaEnte.setFax("Fax");
		anagraficaEnte.setIndirizzo("Indirizzo");
		anagraficaEnte.setLocalita("Località");
		anagraficaEnte.setNazione("Nazione");
		anagraficaEnte.setProvincia("Provincia");
		anagraficaEnte.setRagioneSociale("RagioneSociale");
		anagraficaEnte.setTelefono("Telefono");
		
		ente.setAnagraficaEnte(anagraficaEnte);
		EntiBD entiBD = new EntiBD(bd);
		entiBD.insertEnte(ente);
		
		Assert.assertTrue(ente.getId() != null, "L'ente inserito non ha l'Id valorizzato.");
		
		Ente enteLetto = entiBD.getEnte(ente.getId());
		Assert.assertTrue(ente.equals(enteLetto), "L'ente letto da database con Id e' diverso da quello inserito");
		
		enteLetto = entiBD.getEnte(ente.getCodEnte());
		Assert.assertTrue(ente.equals(enteLetto), "L'ente letto da database con CodEnte e' diverso da quello inserito");
	}
	
	@Test(groups = {"anagrafica", "ente"}, dependsOnMethods = { "inserisciEnte" })
	public void aggiornaEnte() throws Exception {
		Ente ente = new Ente();
		ente.setAttivo(true);
		ente.setIdDominio(dominioA.getId());
		ente.setCodEnte("CodEnte");
		ente.setIdMailTemplateRPT(mailTemplateRPT.getId());
		ente.setIdMailTemplateRT(mailTemplateRT.getId());

		
		Anagrafica anagraficaEnte = new Anagrafica();
		anagraficaEnte.setCap("Cap");
		anagraficaEnte.setCellulare("Cellulare");
		anagraficaEnte.setCivico("Civico");
		anagraficaEnte.setCodUnivoco("CodUnivoco");
		anagraficaEnte.setEmail(null);
		anagraficaEnte.setFax(null);
		anagraficaEnte.setIndirizzo("Indirizzo");
		anagraficaEnte.setLocalita("Località");
		anagraficaEnte.setNazione("Nazione");
		anagraficaEnte.setProvincia(null);
		anagraficaEnte.setRagioneSociale("ÀÁÂÃÄÅĀÆÇÈÉÊËĒÌÍÎÏĪÐÑÒÓÔÕÖØŌŒŠþÙÚÛÜŪÝŸàáâãäåæāçèéêëēìíîïīðñòóôõöøōœšÞùúûüūýÿ¢ß¥£™©®ª×÷±²³¼½¾µ¿¶·¸º°¯§…¤¦‡¬ˆ¨‰♥╚╝┌╩ð~«♦£▲");
		anagraficaEnte.setTelefono(null);
		
		ente.setAnagraficaEnte(anagraficaEnte);
		EntiBD entiBD = new EntiBD(bd);
		entiBD.updateEnte(ente);
		
		Assert.assertTrue(ente.getId() != null, "L'ente aggiornato non ha l'Id valorizzato.");
		
		Ente enteLetto = entiBD.getEnte(ente.getId());
		Assert.assertTrue(ente.equals(enteLetto), "L'ente letto da database con Id e' diverso da quello inserito. Diff: " + BasicModel.diff(ente, enteLetto));
	}
	
	@Test(groups = {"anagrafica", "ente"} , dependsOnMethods = { "aggiornaEnte" })
	public void cercaEnte() throws Exception {
		Ente ente = new Ente();
		ente.setAttivo(false);
		ente.setIdDominio(dominioA.getId());
		ente.setCodEnte("CodEnte2");
		
		Anagrafica anagraficaEnte = new Anagrafica();
		anagraficaEnte.setCodUnivoco("CodUnivoco2");
		anagraficaEnte.setCap("Cap");
		anagraficaEnte.setCellulare("Cellulare");
		anagraficaEnte.setCivico("Civico");
		anagraficaEnte.setEmail(null);
		anagraficaEnte.setFax(null);
		anagraficaEnte.setIndirizzo("Indirizzo");
		anagraficaEnte.setLocalita("Località");
		anagraficaEnte.setNazione("Nazione");
		anagraficaEnte.setProvincia(null);
		anagraficaEnte.setRagioneSociale("ÀÁÂÃÄÅĀÆÇÈÉÊËĒÌÍÎÏĪÐÑÒÓÔÕÖØŌŒŠþÙÚÛÜŪÝŸàáâãäåæāçèéêëēìíîïīðñòóôõöøōœšÞùúûüūýÿ¢ß¥£™©®ª×÷±²³¼½¾µ¿¶·¸º°¯§…¤¦‡¬ˆ¨‰♥╚╝┌╩ð~«♦£▲");
		anagraficaEnte.setTelefono(null);
		
		ente.setAnagraficaEnte(anagraficaEnte);
		EntiBD entiBD = new EntiBD(bd);
		entiBD.insertEnte(ente);
		
		List<Ente> enti = entiBD.getEnti();
		Assert.assertTrue(enti.size() == 2, "Letti da database " + enti.size() + " enti, ma ne erano attesi 2");
		Assert.assertTrue(enti.contains(ente), "La lista di Enti restituita dalla findAll non contiene l'ente precedentemente inserito");
	}
	
}
