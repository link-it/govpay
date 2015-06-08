/*
 * GovPay - Porta di Accesso al Nodo dei Pagamenti SPC 
 * http://www.gov4j.it/govpay
 * 
 * Copyright (c) 2014-2015 Link.it srl (http://www.link.it).
 * Copyright (c) 2014-2015 TAS S.p.A. (http://www.tasgroup.it).
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
package it.govpay.ejb.ndp.model;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class EventiInterfacciaModel {
	
	public EventiInterfacciaModel() {
		eventi = new ArrayList<Evento>();
	}
	
	public enum Componente {
		GOVPAY
	}

	public enum SottoTipo {
		REQ, RSP
	}

	public enum Categoria {
		INTERFACCIA
	}
	
	private List<Evento> eventi;
	private Infospcoop infospcoop;

	public Infospcoop getInfospcoop() {
		return infospcoop;
	}

	public void setInfospcoop(Infospcoop infospcoop) {
		this.infospcoop = infospcoop;
	}

	public List<Evento> getEventi() {
		return eventi;
	}

	public void setEventi(List<Evento> eventi) {
		this.eventi = eventi;
	}



	public class Infospcoop {

		private String idEgov;
		private String tipoSoggettoErogatore;
		private String soggettoErogatore;
		private String tipoSoggettoFruitore;
		private String soggettoFruitore;
		private String tipoServizio;
		private String servizio;
		private String azione;
		
		public String getIdEgov() {
			return idEgov;
		}
		public void setIdEgov(String idEgov) {
			this.idEgov = idEgov;
		}
		public String getTipoSoggettoErogatore() {
			return tipoSoggettoErogatore;
		}
		public void setTipoSoggettoErogatore(String tipoSoggettoErogatore) {
			this.tipoSoggettoErogatore = tipoSoggettoErogatore;
		}
		public String getSoggettoErogatore() {
			return soggettoErogatore;
		}
		public void setSoggettoErogatore(String soggettoErogatore) {
			this.soggettoErogatore = soggettoErogatore;
		}
		public String getTipoSoggettoFruitore() {
			return tipoSoggettoFruitore;
		}
		public void setTipoSoggettoFruitore(String tipoSoggettoFruitore) {
			this.tipoSoggettoFruitore = tipoSoggettoFruitore;
		}
		public String getSoggettoFruitore() {
			return soggettoFruitore;
		}
		public void setSoggettoFruitore(String soggettoFruitore) {
			this.soggettoFruitore = soggettoFruitore;
		}
		public String getTipoServizio() {
			return tipoServizio;
		}
		public void setTipoServizio(String tipoServizio) {
			this.tipoServizio = tipoServizio;
		}
		public String getServizio() {
			return servizio;
		}
		public void setServizio(String servizio) {
			this.servizio = servizio;
		}
		public String getAzione() {
			return azione;
		}
		public void setAzione(String azione) {
			this.azione = azione;
		}
	}
	
	public class Evento {

		private Date data;
		private String dominio;
		private String iuv;
		private String ccp;
		private String psp;
		private String tipoVersamento;
		private Componente componente;
		private Categoria categoria;
		private String tipo;
		private SottoTipo sottoTipo;
		private String fruitore;
		private String erogatore;
		private String stazioneIntermediarioPA;
		private String canalePagamento;
		private String parametri;
		private String esito;
		private String idEgov;
		private Long id;
		
		public Date getData() {
			return data;
		}
		public void setData(Date data) {
			this.data = data;
		}
		public String getDominio() {
			return dominio;
		}
		public void setDominio(String dominio) {
			this.dominio = dominio;
		}
		public String getIuv() {
			return iuv;
		}
		public void setIuv(String iuv) {
			this.iuv = iuv;
		}
		public String getCcp() {
			return ccp;
		}
		public void setCcp(String ccp) {
			this.ccp = ccp;
		}
		public Componente getComponente() {
			return componente;
		}
		public void setComponente(Componente componente) {
			this.componente = componente;
		}
		public Categoria getCategoria() {
			return categoria;
		}
		public void setCategoria(Categoria categoria) {
			this.categoria = categoria;
		}
		public String getTipo() {
			return tipo;
		}
		public void setTipo(String tipo) {
			this.tipo = tipo;
		}
		public SottoTipo getSottoTipo() {
			return sottoTipo;
		}
		public void setSottoTipo(SottoTipo sottoTipo) {
			this.sottoTipo = sottoTipo;
		}
		public String getFruitore() {
			return fruitore;
		}
		public void setFruitore(String fruitore) {
			this.fruitore = fruitore;
		}
		public String getErogatore() {
			return erogatore;
		}
		public void setErogatore(String erogatore) {
			this.erogatore = erogatore;
		}
		public String getStazioneIntermediarioPA() {
			return stazioneIntermediarioPA;
		}
		public void setStazioneIntermediarioPA(String stazioneIntermediarioPA) {
			this.stazioneIntermediarioPA = stazioneIntermediarioPA;
		}
		public String getCanalePagamento() {
			return canalePagamento;
		}
		public void setCanalePagamento(String canalePagamento) {
			this.canalePagamento = canalePagamento;
		}
		public String getParametri() {
			return parametri;
		}
		public void setParametri(String parametri) {
			this.parametri = parametri;
		}
		public String getEsito() {
			return esito;
		}
		public void setEsito(String esito) {
			this.esito = esito;
		}
		public String getTipoVersamento() {
			return tipoVersamento;
		}
		public void setTipoVersamento(String tipoVersamento) {
			this.tipoVersamento = tipoVersamento;
		}
		public String getPsp() {
			return psp;
		}
		public void setPsp(String psp) {
			this.psp = psp;
		}
		public String getIdEgov() {
			return idEgov;
		}
		public void setIdEgov(String idEgov) {
			this.idEgov = idEgov;
		}
		public Long getId() {
			return id;
		}
		public void setId(Long id) {
			this.id = id;
		}

	}

	public void buildResponses() {
		List<Evento> risposte = new ArrayList<Evento>();
		for(Evento e : eventi) {
			Evento risposta = new Evento();
			risposta.setCanalePagamento(e.getCanalePagamento());
			risposta.setCategoria(e.getCategoria());
			risposta.setCcp(e.getCcp());
			risposta.setComponente(e.getComponente());
			risposta.setData(e.getData());
			risposta.setDominio(e.getDominio());
			risposta.setErogatore(e.getFruitore());
			risposta.setEsito(e.getEsito());
			risposta.setFruitore(e.getErogatore());
			risposta.setIuv(e.getIuv());
			risposta.setParametri(e.getParametri());
			risposta.setPsp(e.getPsp());
			risposta.setSottoTipo(SottoTipo.RSP);
			risposta.setStazioneIntermediarioPA(e.getStazioneIntermediarioPA());
			risposta.setTipo(e.getTipo());
			risposta.setTipoVersamento(e.getTipoVersamento());
			risposta.setIdEgov(e.getIdEgov());
			risposte.add(risposta);
		} 
		eventi.addAll(risposte);
	}

	public void setEsito(String esito) {
		for(Evento e : eventi) {
			e.setEsito(esito);
		}
	}
}
