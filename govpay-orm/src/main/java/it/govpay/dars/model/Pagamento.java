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
package it.govpay.dars.model;

import java.util.List;

import it.govpay.bd.model.Ente;
import it.govpay.bd.model.Esito;
import it.govpay.bd.model.Evento;
import it.govpay.bd.model.Mail;
import it.govpay.bd.model.Psp;
import it.govpay.bd.model.Psp.Canale;
import it.govpay.bd.model.Rpt;
import it.govpay.bd.model.Rt;
import it.govpay.bd.model.Versamento;

public class Pagamento {
	private Versamento versamento;
	private Canale canale;
	private Psp psp;
	private Rpt rpt;
	private Rt rt;
	private Ente ente;
	private ListaApplicazioniEntry applicazione;
	private List<Evento> eventi;
	private List<Esito> esiti;
	private List<Mail> mail;
	
	public Versamento getVersamento() {
		return versamento;
	}
	public void setVersamento(Versamento versamento) {
		this.versamento = versamento;
	}
	public Rpt getRpt() {
		return rpt;
	}
	public void setRpt(Rpt rpt) {
		this.rpt = rpt;
	}
	public Rt getRt() {
		return rt;
	}
	public void setRt(Rt rt) {
		this.rt = rt;
	}
	public Ente getEnte() {
		return ente;
	}
	public void setEnte(Ente ente) {
		this.ente = ente;
	}
	public Canale getCanale() {
		return canale;
	}
	public void setCanale(Canale canale) {
		this.canale = canale;
	}
	public List<Evento> getEventi() {
		return eventi;
	}
	public void setEventi(List<Evento> eventi) {
		this.eventi = eventi;
	}
	public List<Mail> getMail() {
		return mail;
	}
	public void setMail(List<Mail> mail) {
		this.mail = mail;
	}
	public List<Esito> getEsiti() {
		return esiti;
	}
	public void setEsiti(List<Esito> esiti) {
		this.esiti = esiti;
	}
	public Psp getPsp() {
		return psp;
	}
	public void setPsp(Psp psp) {
		this.psp = psp;
	}
	public ListaApplicazioniEntry getApplicazione() {
		return applicazione;
	}
	public void setApplicazione(ListaApplicazioniEntry applicazione) {
		this.applicazione = applicazione;
	}
}
