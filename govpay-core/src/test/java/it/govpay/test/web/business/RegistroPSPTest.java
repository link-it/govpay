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
package it.govpay.test.web.business;

import java.util.List;

import it.govpay.bd.anagrafica.PspBD;
import it.govpay.bd.model.Psp;
import it.govpay.business.RegistroPSP;
import it.govpay.test.BasicTest;

import org.apache.commons.lang.SerializationUtils;
import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

@Test(groups = {"business", "psp"} )
public class RegistroPSPTest extends BasicTest {
	
	@BeforeClass
	public void setupDB() throws Exception {
		reInitDataBase();
	}
	
	public void aggiornaRegistro() throws Exception {
		setupIntermediario();
		setupStazione();
		setupDominio();
		setupEnte();
		setupTributi();
		setupApplicazioni();
		setupPortali();
		
		RegistroPSP registroPSP = new RegistroPSP(bd);
		registroPSP.aggiornaRegistro();
		
		PspBD pspBD = new PspBD(bd);
		Assert.assertTrue(!pspBD.getPsp().isEmpty(), "Il registro Psp e' vuoto.");
	}
	
	@Test(dependsOnMethods = "aggiornaRegistro" )
	public void aggiornaPsp() throws Exception {
		PspBD pspBD = new PspBD(bd);
		List<Psp> listaPsp = pspBD.getPsp(true);
		
		
		Psp pspModificato = listaPsp.get(0);
		Psp pspOriginale = (Psp) SerializationUtils.clone(pspModificato);
		pspModificato.setAttivo(false);
		pspModificato.getCanali().remove(0);
		pspModificato.setRagioneSociale("AAAA");
		pspBD.updatePsp(pspModificato, "<test />".getBytes());
		
		RegistroPSP registroPSP = new RegistroPSP(bd);
		registroPSP.aggiornaRegistro();
		
		Psp pspLetto = pspBD.getPsp(pspModificato.getCodPsp());
		Assert.assertTrue(pspOriginale.equals(pspLetto), "Il psp non si e' aggiornato correttamente. ATTESO: " + pspOriginale.toString() + "\nTROVATO: " + pspLetto.toString());
	}
}
