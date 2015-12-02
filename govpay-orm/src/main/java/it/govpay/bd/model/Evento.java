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
package it.govpay.bd.model;

import it.govpay.bd.model.Rpt.TipoVersamento;

import java.util.Date;

import org.apache.commons.lang.ArrayUtils;
import org.openspcoop2.generic_project.exception.ServiceException;

public class Evento extends BasicModel {
	private static final long serialVersionUID = 1L;
	
	public enum TipoEvento {
		nodoInviaRPT("nodoInviaRPT"),
		nodoInviaCarrelloRPT("nodoInviaCarrelloRPT"), 
		nodoChiediStatoRPT("nodoChiediStatoRPT"), 
		nodoChiediListaPendentiRPT("nodoChiediListaPendentiRPT"), 
		paaInviaRT("paaInviaRT"), 
		nodoChiediCopiaRT("nodoChiediCopiaRT"), 
		paaVerificaRPT("paaVerificaRPT"), 
		paaAttivaRPT("paaAttivaRPT"), 
		nodoChiediInformativaPSP("nodoChiediInformativaPSP"),
		nodoInviaRichiestaStorno("nodoInviaRichiestaStorno"), 
		nodoInviaEsitoStorno("nodoInviaEsitoStorno"), 
		nodoChiediElencoFlussiRendicontazione("nodoChiediElencoFlussiRendicontazione"),
		nodoChiediFlussoRendicontazione("nodoChiediFlussoRendicontazione"), 
		nodoChiediElencoQuadraturePA("nodoChiediElencoQuadraturePA"), 
		nodoChiediQuadraturaPA("nodoChiediQuadraturaPA"),
		SCONOSCIUTO("sconosciuto");
		
		private String codifica;

		TipoEvento(String codifica) {
			this.codifica = codifica;
		}
		
		public String getCodifica() {
			return codifica;
		}
		
		public static TipoEvento toEnum(String codifica) throws ServiceException {
			for(TipoEvento t : TipoEvento.values()){
				if(t.getCodifica().equals(codifica))
					return t;
			}
			throw new ServiceException("Codifica inesistente per TipoEvento. Valore fornito [" + codifica + "] valori possibili " + ArrayUtils.toString(TipoEvento.values()));
		}
		
	}
	
	public enum CategoriaEvento {
		INTERNO, INTERFACCIA
	}
	
	
	private static final String EVENTO_PREFIX = "it.govpay.eventi.";
	public static final String ID_PSP = EVENTO_PREFIX + "id_psp";
	public static final String TIPO_VERSAMENTO = EVENTO_PREFIX + "tipo_versamento"; 
	public static final String COMPONENTE = EVENTO_PREFIX + "componente";
	public static final String ID_STAZIONE = EVENTO_PREFIX + "id_stazione_intermediario_PA";
	public static final String ID_APPLICAZIONE = EVENTO_PREFIX + "id_applicazione";
	public static final String CANALE = EVENTO_PREFIX + "canale";
	public static final String PARAMATERI_SPECIFICI_INTERFACCIA = EVENTO_PREFIX + "parametri_specifici_interfaccia";
	public static final String ID_DOMINIO = EVENTO_PREFIX + "id_dominio";
	public static final String IUV = EVENTO_PREFIX + "iuv";
	public static final String CCP = EVENTO_PREFIX + "ccp";
	public static final String ESITO = EVENTO_PREFIX + "esito";
	public static final String SOAP_ACTION = EVENTO_PREFIX + "SOAP_ACTION";
	public static final String NDP_SOGGETTO = "NodoDeiPagamentiSPC";
	public static final String GP_SOGGETTO = EVENTO_PREFIX + "GP_SOGGETTO";
	public static final String VALUES_SEPARATOR = ";";
	public static final String COMPONENTE_WGovPay = "WGOVPAY";
	
	
	private Long id;
	private Date dataOraEvento;
	private String codDominio;
	private String iuv;
	private String ccp;
	private Long idApplicazione;
	private String codPsp;
	private TipoVersamento tipoVersamento;
	private String componente;
	private CategoriaEvento categoriaEvento;
	private TipoEvento tipoEvento;
	private String sottotipoEvento;
	private String codFruitore;
	private String codErogatore;
	private String codStazione;
	private String canalePagamento;
	private String altriParametri;
	private String esito;
	
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public Date getDataOraEvento() {
		return dataOraEvento;
	}
	public void setDataOraEvento(Date dataOraEvento) {
		this.dataOraEvento = dataOraEvento;
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
	public String getCodFruitore() {
		return codFruitore;
	}
	public void setCodFruitore(String codFruitore) {
		this.codFruitore = codFruitore;
	}
	public String getCodErogatore() {
		return codErogatore;
	}
	public void setCodErogatore(String codErogatore) {
		this.codErogatore = codErogatore;
	}
	public String getCodStazione() {
		return codStazione;
	}
	public void setCodStazione(String codStazione) {
		this.codStazione = codStazione;
	}
	public String getCanalePagamento() {
		return canalePagamento;
	}
	public void setCanalePagamento(String canalePagamento) {
		this.canalePagamento = canalePagamento;
	}
	public String getAltriParametri() {
		return altriParametri;
	}
	public void setAltriParametri(String altriParametri) {
		this.altriParametri = altriParametri;
	}
	public String getEsito() {
		return esito;
	}
	public void setEsito(String esito) {
		this.esito = esito;
	}
	public Long getIdApplicazione() {
		return idApplicazione;
	}
	public void setIdApplicazione(Long idApplicazione) {
		this.idApplicazione = idApplicazione;
	}
	public String getCcp() {
		return ccp;
	}
	public void setCcp(String ccp) {
		this.ccp = ccp;
	}
	
	@Override
	public boolean equals(Object obj) {
		Evento evento = null;
		if(obj instanceof Evento) {
			evento = (Evento) obj;
		} else {
			return false;
		}
		
		boolean equal = 
				equals(dataOraEvento, evento.getDataOraEvento()) &&
				equals(codDominio, evento.getCodDominio()) &&
				equals(iuv, evento.getIuv()) &&
				equals(ccp, evento.getCcp()) &&
				equals(idApplicazione, evento.getIdApplicazione()) &&
				equals(codPsp, evento.getCodPsp()) &&
				equals(tipoVersamento, evento.getTipoVersamento()) &&
				equals(componente, evento.getComponente()) &&
				equals(categoriaEvento, evento.getCategoriaEvento()) &&
				equals(tipoEvento, evento.getTipoEvento()) &&
				equals(sottotipoEvento, evento.getSottotipoEvento()) &&
				equals(codFruitore, evento.getCodFruitore()) &&
				equals(codErogatore, evento.getCodErogatore()) &&
				equals(codStazione, evento.getCodStazione()) &&
				equals(canalePagamento, evento.getCanalePagamento()) &&
				equals(altriParametri, evento.getAltriParametri()) &&
				equals(esito, evento.getEsito());
		
		return equal;
	}


}
