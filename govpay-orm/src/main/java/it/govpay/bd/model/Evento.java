/*
 * GovPay - Porta di Accesso al Nodo dei Pagamenti SPC 
 * http://www.gov4j.it/govpay
 * 
 * Copyright (c) 2014-2016 Link.it srl (http://www.link.it).
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
package it.govpay.bd.model;


import it.govpay.bd.model.Canale.TipoVersamento;

import java.util.Date;

import org.apache.commons.lang.ArrayUtils;
import org.openspcoop2.generic_project.exception.ServiceException;

public class Evento extends BasicModel {
	private static final long serialVersionUID = 1L;
	
	public enum TipoEvento {
		nodoInviaRPT,
		nodoInviaCarrelloRPT, 
		nodoChiediStatoRPT, 
		paaInviaRT, 
		nodoChiediCopiaRT, 
		paaVerificaRPT, 
		paaAttivaRPT,
		nodoInviaRichiestaStorno,
		paaInviaEsitoStorno;
	}
	
	public enum CategoriaEvento {
		INTERNO ("B"), INTERFACCIA ("F");
		
		private String codifica;

		CategoriaEvento(String codifica) {
			this.codifica = codifica;
		}
		public String getCodifica() {
			return codifica;
		}
		
		public static CategoriaEvento toEnum(String codifica) throws ServiceException {
			
			for(CategoriaEvento p : CategoriaEvento.values()){
				if(p.getCodifica().equals(codifica))
					return p;
			}
			
			throw new ServiceException("Codifica inesistente per CategoriaEvento. Valore fornito [" + codifica + "] valori possibili " + ArrayUtils.toString(CategoriaEvento.values()));
		}
	}
	
	public static final String COMPONENTE = "GP";
	public static final String NDP = "NodoDeiPagamentiSPC";
	
	private Long id;
	private Date dataRichiesta;
	private Date dataRisposta;
	private String codDominio;
	private String iuv;
	private String ccp;
	private String codPsp;
	private TipoVersamento tipoVersamento;
	private String componente;
	private CategoriaEvento categoriaEvento;
	private TipoEvento tipoEvento;
	private String sottotipoEvento;
	private String fruitore;
	private String erogatore;
	private String codStazione;
	private String codCanale;
	private String altriParametriRichiesta;
	private String altriParametriRisposta;
	private String esito;
	
	public Evento() {
		categoriaEvento = CategoriaEvento.INTERFACCIA;
		dataRichiesta = new Date();
		componente = COMPONENTE;
	}
	
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public Date getDataRichiesta() {
		return dataRichiesta;
	}
	public void setDataRichiesta(Date data_richiesta) {
		this.dataRichiesta = data_richiesta;
	}
	public Date getDataRisposta() {
		return dataRisposta;
	}
	public void setDataRisposta(Date data_risposta) {
		this.dataRisposta = data_risposta;
	}
	public String getCodDominio() {
		return codDominio;
	}
	public void setCodDominio(String codDominio) {
		this.codDominio = codDominio;
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
	public String getCodPsp() {
		return codPsp;
	}
	public void setCodPsp(String codPsp) {
		this.codPsp = codPsp;
	}
	public TipoVersamento getTipoVersamento() {
		return tipoVersamento;
	}
	public void setTipoVersamento(TipoVersamento tipoVersamento) {
		this.tipoVersamento = tipoVersamento;
	}
	public String getComponente() {
		return componente;
	}
	public void setComponente(String componente) {
		this.componente = componente;
	}
	public CategoriaEvento getCategoriaEvento() {
		return categoriaEvento;
	}
	public void setCategoriaEvento(CategoriaEvento categoriaEvento) {
		this.categoriaEvento = categoriaEvento;
	}
	public TipoEvento getTipoEvento() {
		return tipoEvento;
	}
	public void setTipoEvento(TipoEvento tipoEvento) {
		this.tipoEvento = tipoEvento;
	}
	public String getSottotipoEvento() {
		return sottotipoEvento;
	}
	public void setSottotipoEvento(String sottotipoEvento) {
		this.sottotipoEvento = sottotipoEvento;
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
	public String getCodStazione() {
		return codStazione;
	}
	public void setCodStazione(String codStazione) {
		this.codStazione = codStazione;
	}
	public String getCodCanale() {
		return codCanale;
	}
	public void setCodCanale(String codCanale) {
		this.codCanale = codCanale;
	}
	public String getAltriParametriRichiesta() {
		return altriParametriRichiesta;
	}
	public void setAltriParametriRichiesta(String altriParametriRichiesta) {
		this.altriParametriRichiesta = altriParametriRichiesta;
	}
	public String getAltriParametriRisposta() {
		return altriParametriRisposta;
	}
	public void setAltriParametriRisposta(String altriParametriRisposta) {
		this.altriParametriRisposta = altriParametriRisposta;
	}
	public String getEsito() {
		return esito;
	}
	public void setEsito(String esito) {
		this.esito = esito;
	}

}
