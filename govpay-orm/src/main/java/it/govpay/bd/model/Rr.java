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

import it.govpay.bd.BasicBD;
import it.govpay.bd.pagamento.PagamentiBD;
import it.govpay.bd.pagamento.RptBD;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

import org.openspcoop2.generic_project.exception.NotFoundException;
import org.openspcoop2.generic_project.exception.ServiceException;

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
	
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public long getIdRpt() {
		return idRpt;
	}

	public void setIdRpt(long idRpt) {
		this.idRpt = idRpt;
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

	public String getCodMsgRevoca() {
		return codMsgRevoca;
	}

	public void setCodMsgRevoca(String codMsgRevoca) {
		this.codMsgRevoca = codMsgRevoca;
	}

	public Date getDataMsgRevoca() {
		return dataMsgRevoca;
	}

	public void setDataMsgRevoca(Date dataMsgRevoca) {
		this.dataMsgRevoca = dataMsgRevoca;
	}

	public BigDecimal getImportoTotaleRichiesto() {
		return importoTotaleRichiesto;
	}

	public void setImportoTotaleRichiesto(BigDecimal importoTotaleRichiesto) {
		this.importoTotaleRichiesto = importoTotaleRichiesto;
	}

	public StatoRr getStato() {
		return stato;
	}

	public void setStato(StatoRr stato) {
		this.stato = stato;
	}

	public String getDescrizioneStato() {
		return descrizioneStato;
	}

	public void setDescrizioneStato(String descrizioneStato) {
		this.descrizioneStato = descrizioneStato;
	}

	public String getCodMsgEsito() {
		return codMsgEsito;
	}

	public void setCodMsgEsito(String codMsgEsito) {
		this.codMsgEsito = codMsgEsito;
	}

	public Date getDataMsgEsito() {
		return dataMsgEsito;
	}

	public void setDataMsgEsito(Date dataMsgEsito) {
		this.dataMsgEsito = dataMsgEsito;
	}

	public BigDecimal getImportoTotaleRevocato() {
		return importoTotaleRevocato;
	}

	public void setImportoTotaleRevocato(BigDecimal importoTotaleRevocato) {
		this.importoTotaleRevocato = importoTotaleRevocato;
	}

	public byte[] getXmlRr() {
		return xmlRr;
	}

	public void setXmlRr(byte[] xmlRr) {
		this.xmlRr = xmlRr;
	}

	public byte[] getXmlEr() {
		return xmlEr;
	}

	public void setXmlEr(byte[] xmlEr) {
		this.xmlEr = xmlEr;
	}
	
	public String getIdTransazioneRr() {
		return idTransazioneRr;
	}

	public void setIdTransazioneRr(String idTransazioneRr) {
		this.idTransazioneRr = idTransazioneRr;
	}

	public String getIdTransazioneEr() {
		return idTransazioneEr;
	}

	public void setIdTransazioneEr(String idTransazioneEr) {
		this.idTransazioneEr = idTransazioneEr;
	}
	
	// Business
	
	private Rpt rpt;
	private List<Pagamento> pagamenti;
	
	public Rpt getRpt(BasicBD bd) throws ServiceException {
		if(this.rpt == null) {
			RptBD rptBD = new RptBD(bd);
			this.rpt = rptBD.getRpt(this.idRpt);
		}
		return this.rpt;
	}
	
	public void setRpt(Rpt rpt) {
		this.rpt = rpt;
		this.idRpt = rpt.getId();
	}
	
	public List<Pagamento> getPagamenti(BasicBD bd) throws ServiceException {
		if(pagamenti == null) {
			PagamentiBD pagamentiBD = new PagamentiBD(bd);
			pagamenti = pagamentiBD.getPagamentiByRr(this.id);
		}
		return pagamenti;
	}
	
	public void setPagamenti(List<Pagamento> pagamenti) {
		this.pagamenti = pagamenti;
	}
	
	public Pagamento getPagamento(String iur, BasicBD bd) throws ServiceException, NotFoundException {
		List<Pagamento> pagamenti = getPagamenti(bd);
		for(Pagamento pagamento : pagamenti) {
			if(pagamento.getIur().equals(iur))
				return pagamento;
		}
		throw new NotFoundException();
	}


}
