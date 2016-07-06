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
import it.govpay.bd.anagrafica.AnagraficaManager;
import it.govpay.bd.pagamento.RptBD;
import it.govpay.bd.pagamento.VersamentiBD;
import it.govpay.bd.pagamento.filters.RptFilter;

import java.io.UnsupportedEncodingException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;

import org.apache.commons.codec.binary.Base64;
import org.apache.commons.lang.StringUtils;
import org.openspcoop2.generic_project.exception.NotFoundException;
import org.openspcoop2.generic_project.exception.ServiceException;



public class Versamento extends BasicModel {

	public enum StatoVersamento {
		NON_ESEGUITO,
		ESEGUITO,
		PARZIALMENTE_ESEGUITO,
		ANNULLATO,
		ANOMALO,
		ESEGUITO_SENZA_RPT;
	}
	
	private static final long serialVersionUID = 1L;
	
	private Long id;
	private long idUo;
	private long idApplicazione;
	private String codVersamentoEnte; 
	private StatoVersamento statoVersamento;
	private String descrizioneStato;
	private BigDecimal importoTotale;
	private boolean aggiornabile;
	private Date dataCreazione;
	private Date dataScadenza;
	private Date dataUltimoAggiornamento;
	private Causale causaleVersamento;
	private Anagrafica anagraficaDebitore;
	private String codLotto;
	private String codVersamentoLotto;
	private String codAnnoTributario;
	private String codBundlekey;
	
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public long getIdUo() {
		return idUo;
	}

	public void setIdUo(long idUo) {
		this.idUo = idUo;
	}

	public long getIdApplicazione() {
		return idApplicazione;
	}

	public void setIdApplicazione(long idApplicazione) {
		this.idApplicazione = idApplicazione;
	}

	public String getCodVersamentoEnte() {
		return codVersamentoEnte;
	}

	public void setCodVersamentoEnte(String codVersamentoEnte) {
		this.codVersamentoEnte = codVersamentoEnte;
	}

	public StatoVersamento getStatoVersamento() {
		return statoVersamento;
	}

	public void setStatoVersamento(StatoVersamento statoVersamento) {
		this.statoVersamento = statoVersamento;
	}

	public String getDescrizioneStato() {
		return descrizioneStato;
	}

	public void setDescrizioneStato(String descrizioneStato) {
		this.descrizioneStato = descrizioneStato;
	}

	public BigDecimal getImportoTotale() {
		return importoTotale;
	}

	public void setImportoTotale(BigDecimal importoTotale) {
		this.importoTotale = importoTotale;
	}

	public boolean isAggiornabile() {
		return aggiornabile;
	}

	public void setAggiornabile(boolean aggiornabile) {
		this.aggiornabile = aggiornabile;
	}

	public Date getDataCreazione() {
		return dataCreazione;
	}

	public void setDataCreazione(Date dataCreazione) {
		this.dataCreazione = dataCreazione;
	}

	public Date getDataScadenza() {
		return dataScadenza;
	}

	public void setDataScadenza(Date dataScadenza) {
		this.dataScadenza = dataScadenza;
	}

	public Date getDataUltimoAggiornamento() {
		return dataUltimoAggiornamento;
	}

	public void setDataUltimoAggiornamento(Date dataUltimoAggiornamento) {
		this.dataUltimoAggiornamento = dataUltimoAggiornamento;
	}

	public Anagrafica getAnagraficaDebitore() {
		return anagraficaDebitore;
	}

	public void setAnagraficaDebitore(Anagrafica anagraficaDebitore) {
		this.anagraficaDebitore = anagraficaDebitore;
	}
	
	public String getCodBundlekey() {
		return codBundlekey;
	}

	public void setCodBundlekey(String codBundlekey) {
		this.codBundlekey = codBundlekey;
	}

	public Causale getCausaleVersamento() {
		return causaleVersamento;
	}

	public void setCausaleVersamento(Causale causaleVersamento) {
		this.causaleVersamento = causaleVersamento;
	}
	
	public void setCausaleVersamento(String causaleVersamentoEncoded) throws UnsupportedEncodingException {
		this.causaleVersamento = decode(causaleVersamentoEncoded);
	}
	
	public interface Causale {
		public String encode() throws UnsupportedEncodingException;
	}
	
	public class CausaleSemplice implements Causale {
		private String causale;
		
		@Override
		public String encode() throws UnsupportedEncodingException {
			return "01 " + Base64.encodeBase64String(causale.getBytes("UTF-8"));
		}
		
		public void setCausale(String causale) {
			this.causale = causale;
		}
		
		public String getCausale() {
			return causale;
		}
		
		@Override
		public String toString() {
			return causale;
		}
		
	}
	
	public class CausaleSpezzoni implements Causale {
		private List<String> spezzoni;
		
		@Override
		public String encode() throws UnsupportedEncodingException {
			String encoded = "02";
			for(String spezzone : spezzoni) {
				encoded += " " + Base64.encodeBase64String(spezzone.getBytes("UTF-8"));
			}
			return encoded;
		}
		
		public void setSpezzoni(List<String> spezzoni) {
			this.spezzoni = spezzoni;
		}
		
		public List<String> getSpezzoni() {
			return spezzoni;
		}
		
		@Override
		public String toString() {
			return StringUtils.join(spezzoni, "; ");
		}
	}
	
	public class CausaleSpezzoniStrutturati implements Causale {
		private List<String> spezzoni;
		private List<BigDecimal> importi;
		
		@Override
		public String encode() throws UnsupportedEncodingException {
			String encoded = "03";
			for(int i=0; i<spezzoni.size(); i++) {
				encoded += " " + Base64.encodeBase64String(spezzoni.get(i).getBytes("UTF-8")) + " " + Base64.encodeBase64String(Double.toString(importi.get(i).doubleValue()).getBytes("UTF-8"));
			}
			return encoded;
		}
		
		public CausaleSpezzoniStrutturati() {
			spezzoni = new ArrayList<String>();
			importi = new ArrayList<BigDecimal>();
		}
		
		public void setSpezzoni(List<String> spezzoni) {
			this.spezzoni = spezzoni;
		}
		
		public List<String> getSpezzoni() {
			return spezzoni;
		}
		
		public void setImporti(List<BigDecimal> importi) {
			this.importi = importi;
		}
		
		public List<BigDecimal> getImporti() {
			return importi;
		}
		
		public void addSpezzoneStrutturato(String spezzone, BigDecimal importo){
			spezzoni.add(spezzone);
			importi.add(importo);
		}
		
		@Override
		public String toString() {
			StringBuffer sb = new StringBuffer();
			for(int i=0; i<spezzoni.size(); i++) {
				sb.append(importi.get(i).doubleValue() + ": " + spezzoni.get(i) + "; ");
			}
			return sb.toString();
		}
	}
	
	private Causale decode(String encodedCausale) throws UnsupportedEncodingException {
		String[] causaleSplit = encodedCausale.split(" ");
		if(causaleSplit[0].equals("01")) {
			CausaleSemplice causale = new CausaleSemplice();
			causale.setCausale(new String(Base64.decodeBase64(causaleSplit[1].getBytes()), "UTF-8"));
			return causale;
		}
		
		if(causaleSplit[0].equals("02")) {
			List<String> spezzoni = new ArrayList<String>();
			for(int i=1; i<causaleSplit.length; i++) {
				spezzoni.add(new String(Base64.decodeBase64(causaleSplit[i].getBytes()), "UTF-8"));
			}
			CausaleSpezzoni causale = new CausaleSpezzoni();
			causale.setSpezzoni(spezzoni);
			return causale;
		}
		
		if(causaleSplit[0].equals("03")) {
			List<String> spezzoni = new ArrayList<String>();
			List<BigDecimal> importi = new ArrayList<BigDecimal>();
			
			for(int i=1; i<causaleSplit.length; i=i+2) {
				spezzoni.add(new String(Base64.decodeBase64(causaleSplit[i].getBytes()), "UTF-8"));
				importi.add(BigDecimal.valueOf(Double.parseDouble(new String(Base64.decodeBase64(causaleSplit[i+1].getBytes()), "UTF-8"))));
			}
			CausaleSpezzoniStrutturati causale = new CausaleSpezzoniStrutturati();
			causale.setSpezzoni(spezzoni);
			causale.setImporti(importi);
			return causale;
		}
		throw new UnsupportedEncodingException();
	}
	
	// BUSINESS
	
	private List<SingoloVersamento> singoliVersamenti;
	private List<Rpt> rpts;
	private Applicazione applicazione;
	private UnitaOperativa uo;
	private boolean bolloTelematico;
	private String iuvProposto;
	
	public void addSingoloVersamento(it.govpay.bd.model.SingoloVersamento singoloVersamento) throws ServiceException {
		if(this.singoliVersamenti == null) {
			this.singoliVersamenti = new ArrayList<SingoloVersamento>();
		}
		
		this.singoliVersamenti.add(singoloVersamento);
		
		if(singoloVersamento.getTipoBollo() != null) {
			this.setBolloTelematico(true);
		}
	}
	
	public List<it.govpay.bd.model.SingoloVersamento> getSingoliVersamenti(BasicBD bd) throws ServiceException {
		if(this.singoliVersamenti == null && getId() != null) {
			VersamentiBD versamentiBD = new VersamentiBD(bd);
			this.singoliVersamenti = versamentiBD.getSingoliVersamenti(getId());
		}
		
		if(this.singoliVersamenti != null)
			Collections.sort(this.singoliVersamenti);
		
		return this.singoliVersamenti;
	}

	public Applicazione getApplicazione(BasicBD bd) throws ServiceException {
		if(applicazione == null) {
			applicazione = AnagraficaManager.getApplicazione(bd, getIdApplicazione());
		} 
		return applicazione;
	}
	
	public void setApplicazione(String codApplicazione, BasicBD bd) throws ServiceException, NotFoundException {
		applicazione = AnagraficaManager.getApplicazione(bd, codApplicazione);
		idApplicazione = applicazione.getId();
	}

	public UnitaOperativa getUo(BasicBD bd) throws ServiceException {
		if(uo == null) {
			uo = AnagraficaManager.getUnitaOperativa(bd, getIdUo());
		} 
		return uo;
	}
	
	public UnitaOperativa setUo(long idDominio, String codUo, BasicBD bd) throws ServiceException, NotFoundException {
		uo = AnagraficaManager.getUnitaOperativa(bd, idDominio, codUo);
		idUo = uo.getId();
		return uo;
	}
	
	public boolean isBolloTelematico() {
		return bolloTelematico;
	}

	public void setBolloTelematico(boolean bolloTelematico) {
		this.bolloTelematico = bolloTelematico;
	}
	
	public List<Rpt> getRpt(BasicBD bd) throws ServiceException {
		if(rpts == null) {
			RptBD rptBD = new RptBD(bd);
			RptFilter filter = rptBD.newFilter();
			filter.setIdVersamento(id);
			rpts = rptBD.findAll(filter);
		}
		return rpts;
	}

	public String getIuvProposto() {
		return iuvProposto;
	}

	public void setIuvProposto(String iuvProposto) {
		this.iuvProposto = iuvProposto;
	}

	public String getCodLotto() {
		return codLotto;
	}

	public void setCodLotto(String codLotto) {
		this.codLotto = codLotto;
	}

	public String getCodVersamentoLotto() {
		return codVersamentoLotto;
	}

	public void setCodVersamentoLotto(String codVersamentoLotto) {
		this.codVersamentoLotto = codVersamentoLotto;
	}

	public String getCodAnnoTributario() {
		return codAnnoTributario;
	}

	public void setCodAnnoTributario(String codAnnoTributario) {
		this.codAnnoTributario = codAnnoTributario;
	}
}
