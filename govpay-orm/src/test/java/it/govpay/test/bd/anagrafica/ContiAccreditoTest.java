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

import it.govpay.bd.anagrafica.ContiAccreditoBD;
import it.govpay.bd.model.BasicModel;
import it.govpay.bd.model.ContoAccredito;
import it.govpay.test.BasicTest;

import java.util.Date;

import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

@Test(dependsOnGroups = {"dominio"})
public class ContiAccreditoTest extends BasicTest {

	@BeforeClass
	public void setupDB() throws Exception {
		reInitDataBase();
	}
	
	@Test(groups = {"anagrafica", "contoAccredito"})
	public void inserisciContoAccredito() throws Exception {
		setupDominio();

		ContoAccredito contoAccredito = new ContoAccredito();
		contoAccredito.setIdDominio(dominioA.getId());
		contoAccredito.setDataOraInizioValidita(new Date());
		contoAccredito.setDataOraPubblicazione(new Date());
		contoAccredito.setIdFlusso("idFlusso");
		contoAccredito.setXml("<contoAccredito />".getBytes());
		
		ContiAccreditoBD contoAccreditoBD = new ContiAccreditoBD(bd);
		contoAccreditoBD.insertContoAccredito(contoAccredito);
		
		Assert.assertTrue(contoAccredito.getId() != null, "La tabella controparti inserita non ha l'Id valorizzato.");
		
		ContoAccredito contoAccreditoLetta = contoAccreditoBD.getContoAccredito(contoAccredito.getId());
		Assert.assertTrue(contoAccredito.equals(contoAccreditoLetta), "La tabella controparti letta da database con Id e' diversa da quella inserita: "+ BasicModel.diff(contoAccredito,contoAccreditoLetta));
		
	}
	
	@Test(groups = {"anagrafica", "contoAccredito"}, dependsOnMethods = { "inserisciContoAccredito" })
	public void aggiornaContoAccredito() throws Exception {
		ContoAccredito contoAccredito = new ContoAccredito();
		contoAccredito.setIdDominio(dominioA.getId());
		contoAccredito.setDataOraInizioValidita(new Date());
		contoAccredito.setDataOraPubblicazione(new Date());
		contoAccredito.setIdFlusso("idFlusso");
		contoAccredito.setXml("<contoAccreditoUpd />".getBytes());

		
		ContiAccreditoBD contoAccreditoiBD = new ContiAccreditoBD(bd);
		contoAccreditoiBD.updateContoAccredito(contoAccredito);
		
		Assert.assertTrue(contoAccredito.getId() != null, "L'contoAccredito aggiornato non ha l'Id valorizzato.");
		
		ContoAccredito contoAccreditoLetto = contoAccreditoiBD.getContoAccredito(contoAccredito.getId());
		Assert.assertTrue(contoAccredito.equals(contoAccreditoLetto), "L'contoAccredito letto da database con Id e' diverso da quello inserito");
	}
	
	@Test(groups = {"anagrafica", "contoAccredito"} , dependsOnMethods = { "aggiornaContoAccredito" })
	public void cercaContoAccredito() throws Exception {
		ContoAccredito contoAccredito = new ContoAccredito();
		contoAccredito.setIdDominio(dominioA.getId());
		contoAccredito.setDataOraInizioValidita(new Date());
		contoAccredito.setDataOraPubblicazione(new Date());
		contoAccredito.setIdFlusso("idFlusso2");
		contoAccredito.setXml("<contoAccredito />".getBytes());
		
		ContiAccreditoBD contoAccreditoBD = new ContiAccreditoBD(bd);
		contoAccreditoBD.insertContoAccredito(contoAccredito);

		
		ContoAccredito tabellaLetta = contoAccreditoBD.getLastContoAccredito(contoAccredito.getIdDominio());
		Assert.assertTrue(contoAccredito.equals(tabellaLetta), "La contoAccredito restituita dalla getLastContoAccredito non corrisponde alla tabella controparti inserita");
	}
	
}
