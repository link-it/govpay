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
package it.govpay.test.bd.registro;

import java.util.Date;
import java.util.List;

import it.govpay.bd.model.Evento;
import it.govpay.bd.model.Evento.CategoriaEvento;
import it.govpay.bd.model.Evento.TipoEvento;
import it.govpay.bd.model.Rpt;
import it.govpay.bd.model.Rpt.TipoVersamento;
import it.govpay.bd.registro.EventiBD;
import it.govpay.test.BasicTest;

import org.testng.Assert;
import org.testng.annotations.Test;

public class EventiTest extends BasicTest {
	
	@Test(groups = {"registro", "eventi"})
	public void inserisciEvento() throws Exception {
		Evento evento = new Evento();
		evento.setAltriParametri("AltriParametri");
		evento.setCanalePagamento("CanalePagamento");
		evento.setCategoriaEvento(CategoriaEvento.INTERFACCIA);
		evento.setCcp(Rpt.CCP_NA);
		evento.setCodDominio("CodDominio");
		evento.setCodErogatore("CodErogatore");
		evento.setCodFruitore("CodFruitore");
		evento.setCodPsp("CodPsp");
		evento.setCodStazione("CodStazione");
		evento.setComponente("Componente");
		evento.setDataOraEvento(new Date());
		evento.setEsito("Esito");
		evento.setIdApplicazione(1l);
		evento.setIuv("Iuv");
		evento.setSottotipoEvento("SottotipoEvento");
		evento.setTipoEvento(TipoEvento.nodoInviaRPT);
		evento.setTipoVersamento(TipoVersamento.ADDEBITO_DIRETTO);
		
		EventiBD eventiBD = new EventiBD(bd);
		eventiBD.insertEvento(evento);
		
		Assert.assertTrue(evento.getId() != null, "L'evento inserito non ha l'id valorizzato");
		
		Evento eventoLetto = eventiBD.getEvento(evento.getId());
		Assert.assertTrue(evento.equals(eventoLetto), "L'evento letto e' diverso da quello inserito");
	}

	@Test(groups = {"registro", "eventi"}, dependsOnMethods ={"inserisciEvento"})
	public void cercaEvento() throws Exception {
		Evento evento = new Evento();
		evento.setAltriParametri("AltriParametri");
		evento.setCanalePagamento("CanalePagamento");
		evento.setCategoriaEvento(CategoriaEvento.INTERFACCIA);
		evento.setCcp(Rpt.CCP_NA);
		evento.setCodDominio("CodDominio");
		evento.setCodErogatore("CodErogatore");
		evento.setCodFruitore("CodFruitore");
		evento.setCodPsp("CodPsp");
		evento.setCodStazione("CodStazione");
		evento.setComponente("Componente");
		evento.setDataOraEvento(new Date());
		evento.setEsito("Esito");
		evento.setIdApplicazione(1l);
		evento.setIuv("Iuv");
		evento.setSottotipoEvento("SottotipoEvento");
		evento.setTipoEvento(TipoEvento.nodoInviaRPT);
		evento.setTipoVersamento(TipoVersamento.ADDEBITO_DIRETTO);
		
		EventiBD eventiBD = new EventiBD(bd);
		eventiBD.insertEvento(evento);
		
		List<Evento> eventi = eventiBD.findAll(eventiBD.newFilter());
		Assert.assertTrue(eventi.contains(evento), "La lista restituita non contiene l'evento precedentemente inserito");
	}
}
