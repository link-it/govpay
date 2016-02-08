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


import it.govpay.bd.anagrafica.IbanAccreditoBD;
import it.govpay.bd.model.BasicModel;
import it.govpay.bd.model.IbanAccredito;
import it.govpay.test.BasicTest;

import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

@Test(dependsOnGroups = {"ente"})
public class IbanAccreditoTest extends BasicTest {
	
	@BeforeClass
	public void setupDB() throws Exception {
		reInitDataBase();
	}

	@Test(groups = {"anagrafica", "ibanAccredito"})
	public void inserisciIbanAccredito() throws Exception {
		setupEnte();
		IbanAccredito ibanAccredito = new IbanAccredito();
		ibanAccredito = new IbanAccredito();
		ibanAccredito.setCodIban("IT88ABCDEFGHIJKLMNOPQRSTUVWXYZ02");
		ibanAccredito.setIdNegozio("idNegozio");
		ibanAccredito.setIdSellerBank("idSellerBank");
		ibanAccredito.setAbilitato(true);
		ibanAccredito.setAttivatoObep(true);
		ibanAccredito.setIdDominio(dominioA.getId());

		IbanAccreditoBD ibanAccreditoBD = new IbanAccreditoBD(bd);
		ibanAccreditoBD.insertIbanAccredito(ibanAccredito);
		
		Assert.assertTrue(ibanAccredito.getId() != null, "Il ibanAccredito inserito non ha l'Id valorizzato.");
		
		IbanAccredito ibanAccreditoLetto = ibanAccreditoBD.getIbanAccredito(ibanAccredito.getId());
		Assert.assertTrue(ibanAccredito.equals(ibanAccreditoLetto), "Il ibanAccredito letto da database con id e' diverso da quello inserito:" + BasicModel.diff(ibanAccredito, ibanAccreditoLetto));
		
		IbanAccredito ibanAccreditoLettoDaIdLogico = ibanAccreditoBD.getIbanAccredito(ibanAccredito.getCodIban());
		Assert.assertTrue(ibanAccredito.equals(ibanAccreditoLettoDaIdLogico), "Il ibanAccredito letto da database con CodIban e' diverso da quello inserito:" + BasicModel.diff(ibanAccredito, ibanAccreditoLettoDaIdLogico));
	}
	
	@Test(groups = {"anagrafica", "ibanAccredito"}, dependsOnMethods = { "inserisciIbanAccredito" })
	public void aggiornaIbanAccredito() throws Exception {
		IbanAccredito ibanAccredito = new IbanAccredito();
		ibanAccredito.setCodIban("IT88ABCDEFGHIJKLMNOPQRSTUVWXYZ02");
		ibanAccredito.setIdNegozio("idNegozio");
		ibanAccredito.setIdSellerBank("idSellerBank");
		ibanAccredito.setAbilitato(false);
		ibanAccredito.setAttivatoObep(true);
		ibanAccredito.setIdDominio(dominioA.getId());
		
		IbanAccreditoBD ibanAccreditoBD = new IbanAccreditoBD(bd);
		ibanAccreditoBD.updateIbanAccredito(ibanAccredito);
		
		Assert.assertTrue(ibanAccredito.getId() != null, "Il ibanAccredito aggiornato non ha l'Id valorizzato.");
		
		IbanAccredito ibanAccreditoLetto = ibanAccreditoBD.getIbanAccredito(ibanAccredito.getCodIban());
		Assert.assertTrue(ibanAccredito.equals(ibanAccreditoLetto), "Il ibanAccredito letto da database con CodIban e' diverso da quello aggiornato");
	}
	
}
