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

import it.govpay.bd.model.serializer.BigDecimalSerializer;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;

import org.codehaus.jackson.map.annotate.JsonSerialize;

public class Versamento extends BasicModel {
	private static final long serialVersionUID = 1L;
	
	public enum StatoRendicontazione {
		NON_RENDICONTATO,
		RENDICONTATO,
		RENDICONTATO_PARZIALE; 
	}
	
	public enum StatoVersamento {
		IN_ATTESA, 
		IN_CORSO,
		AUTORIZZATO,
		AUTORIZZATO_IMMEDIATO,
		AUTORIZZATO_DIFFERITO,
		PAGAMENTO_ESEGUITO,
		PAGAMENTO_NON_ESEGUITO,
		PAGAMENTO_PARZIALMENTE_ESEGUITO,
		DECORRENZA_TERMINI,
		DECORRENZA_TERMINI_PARZIALE,
		ANNULLATO, 
		SCADUTO,
		FALLITO; 
	}
	private Long id;
	private String codDominio;
	private String iuv; 
	private String codVersamentoEnte; 
	private long idEnte;
	private long idApplicazione;
	private Anagrafica anagraficaDebitore;
	@JsonSerialize(using = BigDecimalSerializer.class)
	private BigDecimal importoTotale;
	@JsonSerialize(using = BigDecimalSerializer.class)
	private BigDecimal importoPagato;
	private StatoVersamento stato;
	private StatoRendicontazione statoRendicontazione;
	private String descrizioneStato;
	private Date dataScadenza;
	private Date dataOraInserimento;
	private Date dataOraUltimaModifica;
	private List<SingoloVersamento> singoliVersamenti;
	
	public Versamento() {
		singoliVersamenti = new ArrayList<SingoloVersamento>();
		stato = StatoVersamento.IN_ATTESA;
		statoRendicontazione = StatoRendicontazione.NON_RENDICONTATO;
		dataOraInserimento = new Date();
	}
	
	public StatoRendicontazione getStatoRendicontazione() {
		return statoRendicontazione;
	}

	public void setStatoRendicontazione(StatoRendicontazione statoRendicontazione) {
		this.statoRendicontazione = statoRendicontazione;
	}
	
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public long getIdApplicazione() {
		return idApplicazione;
	}
	public void setIdApplicazione(long idApplicazione) {
		this.idApplicazione = idApplicazione;
	}
	public BigDecimal getImportoTotale() {
		return importoTotale;
	}
	public void setImportoTotale(BigDecimal importoTotale) {
		this.importoTotale = importoTotale;
	}
	public StatoVersamento getStato() {
		return stato;
	}
	public void setStato(StatoVersamento stato) {
		this.stato = stato;
	}
	public List<SingoloVersamento> getSingoliVersamenti() {
		Collections.sort(singoliVersamenti);
		return singoliVersamenti;
	}
	public void setSingoliVersamenti(List<SingoloVersamento> singoliVersamenti) {
		this.singoliVersamenti = singoliVersamenti;
	}
	
	@SuppressWarnings("deprecation")
	public Date getDataScadenza() {
		dataScadenza.setHours(23);
		dataScadenza.setMinutes(59);
		return dataScadenza;
	}
	public void setDataScadenza(Date dataScadenza) {
		this.dataScadenza = dataScadenza;
	}
	public Anagrafica getAnagraficaDebitore() {
		return anagraficaDebitore;
	}
	public void setAnagraficaDebitore(Anagrafica anagraficaDebitore) {
		this.anagraficaDebitore = anagraficaDebitore;
	}
	public String getCodDominio() {
		return codDominio;
	}
	public void setCodDominio(String codDominio) {
		this.codDominio = codDominio;
	}
	public long getIdEnte() {
		return idEnte;
	}
	public void setIdEnte(long idEnte) {
		this.idEnte = idEnte;
	}
	
	public BigDecimal getImportoPagato() {
		return importoPagato;
	}

	public void setImportoPagato(BigDecimal importoPagato) {
		this.importoPagato = importoPagato;
	}

	public String getDescrizioneStato() {
		return descrizioneStato;
	}

	public void setDescrizioneStato(String descrizioneStato) {
		this.descrizioneStato = descrizioneStato;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}

	@Override
	public boolean equals(Object obj) {
		Versamento versamento = null;
		if(obj instanceof Versamento) {
			versamento = (Versamento) obj;
		} else {
			return false;
		}
		
		boolean equal =
				equals(codVersamentoEnte, versamento.getCodVersamentoEnte()) &&
				equals(iuv, versamento.getIuv()) &&
				equals(codDominio, versamento.getCodDominio()) &&
				equals(iuv, versamento.getIuv()) &&
				equals(anagraficaDebitore, versamento.getAnagraficaDebitore()) &&
				equals(importoTotale, versamento.getImportoTotale()) &&
				equals(importoPagato, versamento.getImportoPagato()) &&
				equals(stato, versamento.getStato()) &&
				equals(descrizioneStato, versamento.getDescrizioneStato()) &&
				equals(dataScadenza, versamento.getDataScadenza()) &&
				equals(singoliVersamenti, versamento.getSingoliVersamenti()) &&
				idEnte == versamento.getIdEnte() &&
				idApplicazione == versamento.getIdApplicazione();
		
		return equal;
	}
	
	public SingoloVersamento getSingoloVersamento(int indice) {
		for(SingoloVersamento s : singoliVersamenti)
			if(s.getIndice() == indice) return s;
		
		return null;
	}

	public Date getDataOraInserimento() {
		return dataOraInserimento;
	}

	public void setDataOraInserimento(Date dataOraInserimento) {
		this.dataOraInserimento = dataOraInserimento;
	}

	public Date getDataOraUltimaModifica() {
		return dataOraUltimaModifica;
	}

	public void setDataOraUltimaModifica(Date dataOraUltimaModifica) {
		this.dataOraUltimaModifica = dataOraUltimaModifica;
	}

	public String getCodVersamentoEnte() {
		return codVersamentoEnte;
	}

	public void setCodVersamentoEnte(String codVersamentoEnte) {
		this.codVersamentoEnte = codVersamentoEnte;
	}

	public String getIuv() {
		return iuv;
	}

	public void setIuv(String iuv) {
		this.iuv = iuv;
	}
}
