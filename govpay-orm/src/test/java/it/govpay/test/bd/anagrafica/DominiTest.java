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


import it.govpay.bd.anagrafica.DominiBD;
import it.govpay.bd.model.BasicModel;
import it.govpay.bd.model.Disponibilita;
import it.govpay.bd.model.Periodo;
import it.govpay.bd.model.Disponibilita.TipoDisponibilita;
import it.govpay.bd.model.Disponibilita.TipoPeriodo;
import it.govpay.bd.model.Dominio;
import it.govpay.test.BasicTest;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

@Test(dependsOnGroups = {"intermediario"})
public class DominiTest extends BasicTest {

	@BeforeClass
	public void setupDB() throws Exception {
		reInitDataBase();
	}

	@Test(groups = {"anagrafica", "dominio"})
	public void inserisciDominio() throws Exception {
		setupStazione();
		Dominio dominio = new Dominio();
		dominio.setCodDominio("CodDominio");
		dominio.setRagioneSociale("ragioneSociale");
		dominio.setGln("Gln");
		dominio.setIdStazione(stazione.getId());
		List<Disponibilita> disponibilita = new ArrayList<Disponibilita>();
		Disponibilita e = new Disponibilita();
		Calendar calendar = Calendar.getInstance();
		calendar.clear();
		calendar.set(Calendar.HOUR, 10);
		calendar.set(Calendar.MINUTE, 30);
		
		List<Periodo> fasceOrarieLst = new ArrayList<Periodo>();
		Periodo periodo = new Periodo();
		periodo.setA(calendar.getTime());
		periodo.setDa(calendar.getTime());
		fasceOrarieLst.add(periodo);
		e.setFasceOrarieLst(fasceOrarieLst);
		e.setGiorno("giorno");
		e.setTipoDisponibilita(TipoDisponibilita.DISPONIBILE);
		e.setTipoPeriodo(TipoPeriodo.ANNUALE);
		disponibilita.add(e);
		dominio.setDisponibilita(disponibilita);
		
		DominiBD dominiBD = new DominiBD(bd);
		dominiBD.insertDominio(dominio);
		
		Assert.assertTrue(dominio.getId() != null, "Il dominio inserito non ha l'Id valorizzato.");
		
		Dominio dominioLetto = dominiBD.getDominio(dominio.getCodDominio());
		Assert.assertTrue(dominio.equals(dominioLetto), "Il dominio letto da database con CodDominio e' diverso da quello inserito:" + BasicModel.diff(dominio, dominioLetto));
	}
	
	@Test(groups = {"anagrafica", "dominio"}, dependsOnMethods = { "inserisciDominio" })
	public void aggiornaDominio() throws Exception {
		Dominio dominio = new Dominio();
		dominio.setCodDominio("CodDominio");
		dominio.setRagioneSociale("ragioneSociale");
		dominio.setGln("Gln2");
		dominio.setIdStazione(stazione.getId());
		
		DominiBD dominiBD = new DominiBD(bd);
		dominiBD.updateDominio(dominio);
		
		Assert.assertTrue(dominio.getId() != null, "Il dominio aggiornato non ha l'Id valorizzato.");
		
		Dominio dominioLetto = dominiBD.getDominio(dominio.getCodDominio());
		Assert.assertTrue(dominio.equals(dominioLetto), "Il dominio letto da database con CodDominio e' diverso da quello aggiornato");
	}
	
	@Test(groups = {"anagrafica", "dominio"}, dependsOnMethods = { "inserisciDominio" })
	public void findAllDomini() throws Exception {

		Dominio dominio = new Dominio();
		dominio.setCodDominio("CodDominio2");
		dominio.setRagioneSociale("ragioneSociale");
		dominio.setGln("Gln");
		dominio.setIdStazione(stazione.getId());
		
		DominiBD dominiBD = new DominiBD(bd);
		dominiBD.insertDominio(dominio);

		List<Dominio> lst = dominiBD.findAll(dominiBD.newFilter());
		
		Assert.assertTrue(lst.contains(dominio), "La findAll non restituisce tra i domini quello precedentemente inserito");
	}
	

}
