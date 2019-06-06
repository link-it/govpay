/*
 * GovPay - Porta di Accesso al Nodo dei Pagamenti SPC 
 * http://www.gov4j.it/govpay
 * 
 * Copyright (c) 2014-2017 Link.it srl (http://www.link.it).
 * 
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License version 3, as published by
 * the Free Software Foundation.
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
package it.govpay.model;

import java.math.BigDecimal;
import java.util.Date;

public class Rr extends BasicModel{
	
	private static final long serialVersionUID = 1L;
	
	public enum StatoRr {
		RR_ATTIVATA,
		RR_ERRORE_INVIO_A_NODO, 
		RR_RIFIUTATA_NODO, 
		RR_ACCETTATA_NODO, 
		ER_ACCETTATA_PA,
		ER_RIFIUTATA_PA;
	}
	
	private Long id;
	private long idRpt;
	private String codDominio;
	private String iuv;
	private String ccp;
	private String codMsgRevoca;
	private Date dataMsgRevoca;
	private BigDecimal importoTotaleRichiesto;
	private StatoRr stato;
	private String descrizioneStato;
	private String codMsgEsito;
	private Date dataMsgEsito;
	private BigDecimal importoTotaleRevocato;
	private byte[] xmlRr;
	private byte[] xmlEr;
	
	private String idTransazioneRr;
	private String idTransazioneEr;
	
	@Override
	public Long getId() {
		return this.id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public long getIdRpt() {
		return this.idRpt;
	}

	public void setIdRpt(long idRpt) {
		this.idRpt = idRpt;
	}

	public String getCodDominio() {
		return this.codDominio;
	}

	public void setCodDominio(String codDominio) {
		this.codDominio = codDominio;
	}

	public String getIuv() {
		return this.iuv;
	}

	public void setIuv(String iuv) {
		this.iuv = iuv;
	}

	public String getCcp() {
		return this.ccp;
	}

	public void setCcp(String ccp) {
		this.ccp = ccp;
	}

	public String getCodMsgRevoca() {
		return this.codMsgRevoca;
	}

	public void setCodMsgRevoca(String codMsgRevoca) {
		this.codMsgRevoca = codMsgRevoca;
	}

	public Date getDataMsgRevoca() {
		return this.dataMsgRevoca;
	}

	public void setDataMsgRevoca(Date dataMsgRevoca) {
		this.dataMsgRevoca = dataMsgRevoca;
	}

	public BigDecimal getImportoTotaleRichiesto() {
		return this.importoTotaleRichiesto;
	}

	public void setImportoTotaleRichiesto(BigDecimal importoTotaleRichiesto) {
		this.importoTotaleRichiesto = importoTotaleRichiesto;
	}

	public StatoRr getStato() {
		return this.stato;
	}

	public void setStato(StatoRr stato) {
		this.stato = stato;
	}

	public String getDescrizioneStato() {
		return this.descrizioneStato;
	}

	public void setDescrizioneStato(String descrizioneStato) {
		this.descrizioneStato = descrizioneStato;
	}

	public String getCodMsgEsito() {
		return this.codMsgEsito;
	}

	public void setCodMsgEsito(String codMsgEsito) {
		this.codMsgEsito = codMsgEsito;
	}

	public Date getDataMsgEsito() {
		return this.dataMsgEsito;
	}

	public void setDataMsgEsito(Date dataMsgEsito) {
		this.dataMsgEsito = dataMsgEsito;
	}

	public BigDecimal getImportoTotaleRevocato() {
		return this.importoTotaleRevocato;
	}

	public void setImportoTotaleRevocato(BigDecimal importoTotaleRevocato) {
		this.importoTotaleRevocato = importoTotaleRevocato;
	}

	public byte[] getXmlRr() {
		return this.xmlRr;
	}

	public void setXmlRr(byte[] xmlRr) {
		this.xmlRr = xmlRr;
	}

	public byte[] getXmlEr() {
		return this.xmlEr;
	}

	public void setXmlEr(byte[] xmlEr) {
		this.xmlEr = xmlEr;
	}
	
	public String getIdTransazioneRr() {
		return this.idTransazioneRr;
	}

	public void setIdTransazioneRr(String idTransazioneRr) {
		this.idTransazioneRr = idTransazioneRr;
	}

	public String getIdTransazioneEr() {
		return this.idTransazioneEr;
	}

	public void setIdTransazioneEr(String idTransazioneEr) {
		this.idTransazioneEr = idTransazioneEr;
	}
}
