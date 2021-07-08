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
package it.govpay.core.dao.pagamenti.dto;

import java.math.BigDecimal;
import java.util.Date;

import org.springframework.security.core.Authentication;

import it.govpay.bd.model.Applicazione;
import it.govpay.bd.model.Incasso;
import it.govpay.bd.model.Operatore;
import it.govpay.core.dao.anagrafica.dto.BasicFindRequestDTO;
import it.govpay.core.exceptions.IncassiException;
import it.govpay.core.exceptions.IncassiException.FaultType;
import it.govpay.core.utils.GovpayConfig;
import it.govpay.core.utils.IncassoUtils;
import it.govpay.model.Incasso.StatoIncasso;

public class RichiestaIncassoDTO extends BasicFindRequestDTO {
	
	public RichiestaIncassoDTO(Authentication user) {
		super(user);
		
		// imposto valore di default per la ricerca dell'idflusso rendicontazione
		this.ricercaIdFlussoCaseInsensitive = GovpayConfig.getInstance().isRicercaRiconciliazioniIdFlussoCaseInsensitive();
	}
	
	private Applicazione applicazione;
	private Operatore operatore;
	private String codDominio;
	private String trn;
	private String causale;
	private BigDecimal importo;
	private Date dataValuta;
	private Date dataContabile;
	private String dispositivo;
	private String ibanAccredito;
	private String iuv;
	private String idFlusso;
	private String sct;
	private boolean ricercaIdFlussoCaseInsensitive = false;
	private String idRiconciliazione;
	
	public String getTrn() {
		return this.trn;
	}
	public void setTrn(String trn) {
		this.trn = trn;
	}
	public String getCausale() {
		return this.causale;
	}
	public void setCausale(String causale) {
		this.causale = causale;
	}
	public BigDecimal getImporto() {
		return this.importo;
	}
	public void setImporto(BigDecimal importo) {
		this.importo = importo;
	}
	public String getDispositivo() {
		return this.dispositivo;
	}
	public void setDispositivo(String dispositivo) {
		this.dispositivo = dispositivo;
	}
	public String getCodDominio() {
		return this.codDominio;
	}
	public void setCodDominio(String codDominio) {
		this.codDominio = codDominio;
	}
	public Date getDataContabile() {
		return this.dataContabile;
	}
	public void setDataContabile(Date dataContabile) {
		this.dataContabile = dataContabile;
	}
	public Date getDataValuta() {
		return this.dataValuta;
	}
	public void setDataValuta(Date dataValuta) {
		this.dataValuta = dataValuta;
	}
	public Applicazione getApplicazione() {
		return this.applicazione;
	}
	public void setApplicazione(Applicazione applicazione) {
		this.applicazione = applicazione;
	}
	public Operatore getOperatore() {
		return this.operatore;
	}
	public void setOperatore(Operatore operatore) {
		this.operatore = operatore;
	}
	public String getIbanAccredito() {
		return this.ibanAccredito;
	}
	public void setIbanAccredito(String ibanAccredito) {
		this.ibanAccredito = ibanAccredito;
	}
	public String getIdFlusso() {
		return idFlusso;
	}
	public void setIdFlusso(String idFlusso) {
		this.idFlusso = idFlusso;
	}
	public String getIuv() {
		return iuv;
	}
	public void setIuv(String iuv) {
		this.iuv = iuv;
	}
	public String getSct() {
		return sct;
	}
	public void setSct(String sct) {
		this.sct = sct;
	}
	public boolean isRicercaIdFlussoCaseInsensitive() {
		return ricercaIdFlussoCaseInsensitive;
	}
	public void setRicercaIdFlussoCaseInsensitive(boolean ricercaIdFlussoCaseInsensitive) {
		this.ricercaIdFlussoCaseInsensitive = ricercaIdFlussoCaseInsensitive;
	}
	public String getIdRiconciliazione() {
		return idRiconciliazione;
	}
	public void setIdRiconciliazione(String idRiconciliazione) {
		this.idRiconciliazione = idRiconciliazione;
	}
	
	public Incasso toIncassoModel() throws IncassiException {
		// Validazione della causale
		boolean iuvIdFlussoSet = this.getIuv() != null || this.getIdFlusso() != null;
		String causale = this.getCausale();
		String iuv = null;
		String idf = null;
		
		// Se non mi e' stato passato uno IUV o un idFlusso, lo cerco nella causale
		if(!iuvIdFlussoSet) {
			try {
				if(causale != null) {
					iuv = IncassoUtils.getRiferimentoIncassoSingolo(causale);
					idf = IncassoUtils.getRiferimentoIncassoCumulativo(causale);
				} 
			} catch (Throwable e) {
				throw new IncassiException(FaultType.CAUSALE_NON_VALIDA,"Riscontrato errore durante il parsing della causale: " + causale);
			} finally {
				if(iuv == null && idf==null) {
					throw new IncassiException(FaultType.CAUSALE_NON_VALIDA, "La causale dell'operazione di incasso non e' conforme alle specifiche AgID (SACIV 1.2.1): " + causale);
				}
			} 
		} else {
			iuv = this.getIuv();
			idf = this.getIdFlusso();
			// causale puo' essere null
		//	causale = iuv != null ? iuv : idf;
		//	this.setCausale(causale);
		}
		
		// OVERRIDE TRN NUOVA GESTIONE
		this.setTrn(iuv != null ? iuv : idf);
		
		Incasso incasso = new Incasso();
		incasso.setCausale(this.getCausale());
		incasso.setCodDominio(this.getCodDominio());
		incasso.setDataIncasso(new Date());
		incasso.setDataValuta(this.getDataValuta());
		incasso.setDataContabile(this.getDataContabile());
		incasso.setDispositivo(this.getDispositivo());
		incasso.setImporto(this.getImporto());
		incasso.setTrn(this.getTrn());
		incasso.setIbanAccredito(this.getIbanAccredito());
		incasso.setApplicazione(this.getApplicazione());
		incasso.setOperatore(this.getOperatore());
		incasso.setSct(this.getSct());
		incasso.setIuv(this.getIuv());
		incasso.setIdFlussoRendicontazione(this.getIdFlusso());
		incasso.setIdRiconciliazione(this.getIdRiconciliazione());
		incasso.setStato(StatoIncasso.NUOVO);
		
		return incasso;
	}
}
