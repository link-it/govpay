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

import java.io.UnsupportedEncodingException;
import java.math.BigDecimal;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Objects;

import org.apache.commons.codec.binary.Base64;
import org.apache.commons.lang.ArrayUtils;
import org.apache.commons.lang.StringUtils;
import org.openspcoop2.generic_project.exception.ServiceException;

public class Versamento extends BasicModel {

	public static final String TIPO_VERSAMENTO_LIBERO = "LIBERO";
	
	public enum TipologiaTipoVersamento {
		SPONTANEO("SPONTANEO"),
		DOVUTO("DOVUTO");

		private String codifica;

		TipologiaTipoVersamento(String codifica) {
			this.codifica = codifica;
		}
		public String getCodifica() {
			return this.codifica;
		}

		public static TipologiaTipoVersamento toEnum(String codifica) throws ServiceException {
			for(TipologiaTipoVersamento p : TipologiaTipoVersamento.values()){
				if(p.getCodifica().equals(codifica))
					return p;
			}
			throw new ServiceException("Codifica inesistente per TipologiaTipoVersamento. Valore fornito [" + codifica + "] valori possibili " + ArrayUtils.toString(TipologiaTipoVersamento.values()));
		}
	}
	
	
	public static final String INCASSO_FALSE = "f";
	public static final String INCASSO_TRUE = "t";
	
	public enum StatoPagamento { PAGATO, INCASSATO, NON_PAGATO }

	public enum StatoVersamento {
		NON_ESEGUITO,
		ESEGUITO,
		PARZIALMENTE_ESEGUITO,
		ANNULLATO,
		ESEGUITO_ALTRO_CANALE,
		ANOMALO,
		ESEGUITO_SENZA_RPT,
		INCASSATO;
	}

	public enum AvvisaturaOperazione {
		CREATE("C"), UPDATE("U"), DELETE ("D");

		private String value;

		AvvisaturaOperazione(String value) {
			this.value = value;
		}
		
		public String getValue() {
			return this.value;
		}

		@Override
		public String toString() {
			return String.valueOf(this.value);
		}

		public static AvvisaturaOperazione fromValue(String text) {
			for (AvvisaturaOperazione m : AvvisaturaOperazione.values()) {
				if (String.valueOf(m.value).equals(text)) {
					return m;
				}
			}
			return null;
		}
	}
	
	public enum TipoSogliaVersamento { 
		
		ENTRO ("ENTRO"), OLTRE ("OLTRE");
		
		private String codifica;

		TipoSogliaVersamento(String codifica) {
			this.codifica = codifica;
		}
		public String getCodifica() {
			return this.codifica;
		}

		public static TipoSogliaVersamento toEnum(String codifica) throws ServiceException {
			for(TipoSogliaVersamento p : TipoSogliaVersamento.values()){
				if(p.getCodifica().equals(codifica))
					return p;
			}
			throw new ServiceException("Codifica inesistente per TipoSogliaVersamento. Valore fornito [" + codifica + "] valori possibili " + ArrayUtils.toString(TipoSogliaVersamento.values()));
		}
	}

	private static final long serialVersionUID = 1L;

	private Long id;
	private Long idUo;
	private long idDominio;
	private long idApplicazione;
	private long idTipoVersamento;
	private long idTipoVersamentoDominio;
	private Long idDocumento;
	
	private String nome;
	private String codVersamentoEnte;
	private StatoVersamento statoVersamento;
	private String descrizioneStato;
	private BigDecimal importoTotale;
	private boolean aggiornabile;
	private Date dataCreazione;
	private Date dataValidita;
	private Date dataScadenza;
	private Date dataUltimoAggiornamento;
	private Causale causaleVersamento;
	private Anagrafica anagraficaDebitore;
	private String iuvProposto;
	private String codLotto;
	private String codVersamentoLotto;
	private Integer codAnnoTributario;
	private String datiAllegati;
	private Boolean incasso;
	private String anomalie;
	private String tassonomiaAvviso;
	private String tassonomia;
	private String iuvVersamento;
	private String numeroAvviso;
	private boolean ack;
	private boolean anomalo;
	
	private String divisione;
	private String direzione;
	private String idSessione;
	private Date dataPagamento;
	private BigDecimal importoPagato;
	private BigDecimal importoIncassato;
	private StatoPagamento statoPagamento;
	private String iuvPagamento;
	private Integer numeroRata;
	private String codDocumento;
	private TipologiaTipoVersamento tipo;
	private TipoSogliaVersamento tipoSoglia;
	private Integer giorniSoglia;
	
	private Date dataNotificaAvviso;
	private Boolean avvisoNotificato;
	
	private Date avvMailDataPromemoriaScadenza;
	private Boolean avvMailPromemoriaScadenzaNotificato;
	private Date avvAppIODataPromemoriaScadenza;
	private Boolean avvAppIOPromemoriaScadenzaNotificato;
	
	private String proprieta;
	
	public Date getDataPagamento() {
		return dataPagamento;
	}
	public void setDataPagamento(Date dataPagamento) {
		this.dataPagamento = dataPagamento;
	}
	public BigDecimal getImportoPagato() {
		return importoPagato;
	}
	public void setImportoPagato(BigDecimal importoPagato) {
		this.importoPagato = importoPagato;
	}
	public BigDecimal getImportoIncassato() {
		return importoIncassato;
	}
	public void setImportoIncassato(BigDecimal importoIncassato) {
		this.importoIncassato = importoIncassato;
	}
	public StatoPagamento getStatoPagamento() {
		return statoPagamento;
	}
	public void setStatoPagamento(StatoPagamento statoPagamento) {
		this.statoPagamento = statoPagamento;
	}
	public String getIuvPagamento() {
		return iuvPagamento;
	}
	public void setIuvPagamento(String iuvPagamento) {
		this.iuvPagamento = iuvPagamento;
	}

	public String getDivisione() {
		return divisione;
	}

	public void setDivisione(String divisione) {
		this.divisione = divisione;
	}

	public String getDirezione() {
		return direzione;
	}

	public void setDirezione(String direzione) {
		this.direzione = direzione;
	}

	public String getIuvVersamento() {
		return this.iuvVersamento;
	}

	public void setIuvVersamento(String iuvVersamento) {
		this.iuvVersamento = iuvVersamento;
	}

	public String getNumeroAvviso() {
		return this.numeroAvviso;
	}

	public void setNumeroAvviso(String numeroAvviso) {
		this.numeroAvviso = numeroAvviso;
	}

	public long getIdDominio() {
		return this.idDominio;
	}

	public void setIdDominio(long idDominio) {
		this.idDominio = idDominio;
	}

	public String getNome() {
		return this.nome;
	}

	public void setNome(String nome) {
		this.nome = nome;
	}

	public Date getDataValidita() {
		return this.dataValidita;
	}

	public void setDataValidita(Date dataValidita) {
		this.dataValidita = dataValidita;
	}

	public String getTassonomiaAvviso() {
		return this.tassonomiaAvviso;
	}

	public void setTassonomiaAvviso(String tassonomiaAvviso) {
		this.tassonomiaAvviso = tassonomiaAvviso;
	}

	public String getTassonomia() {
		return this.tassonomia;
	}

	public void setTassonomia(String tassonomia) {
		this.tassonomia = tassonomia;
	}

	private String codBundlekey;
	private boolean bolloTelematico;

	@Override
	public Long getId() {
		return this.id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Long getIdUo() {
		return this.idUo;
	}

	public void setIdUo(Long idUo) {
		this.idUo = idUo;
	}

	public long getIdApplicazione() {
		return this.idApplicazione;
	}

	public void setIdApplicazione(long idApplicazione) {
		this.idApplicazione = idApplicazione;
	}

	public String getCodVersamentoEnte() {
		return this.codVersamentoEnte;
	}

	public void setCodVersamentoEnte(String codVersamentoEnte) {
		this.codVersamentoEnte = codVersamentoEnte;
	}

	public StatoVersamento getStatoVersamento() {
		return this.statoVersamento;
	}

	public void setStatoVersamento(StatoVersamento statoVersamento) {
		this.statoVersamento = statoVersamento;
	}

	public String getDescrizioneStato() {
		return this.descrizioneStato;
	}

	public void setDescrizioneStato(String descrizioneStato) {
		this.descrizioneStato = descrizioneStato;
	}

	public BigDecimal getImportoTotale() {
		return this.importoTotale;
	}

	public void setImportoTotale(BigDecimal importoTotale) {
		this.importoTotale = importoTotale;
	}

	public boolean isAggiornabile() {
		return this.aggiornabile;
	}

	public void setAggiornabile(boolean aggiornabile) {
		this.aggiornabile = aggiornabile;
	}

	public Date getDataCreazione() {
		return this.dataCreazione;
	}

	public void setDataCreazione(Date dataCreazione) {
		this.dataCreazione = dataCreazione;
	}

	public Date getDataScadenza() {
		return this.dataScadenza;
	}

	public void setDataScadenza(Date dataScadenza) {
		this.dataScadenza = dataScadenza;
	}

	public Date getDataUltimoAggiornamento() {
		return this.dataUltimoAggiornamento;
	}

	public void setDataUltimoAggiornamento(Date dataUltimoAggiornamento) {
		this.dataUltimoAggiornamento = dataUltimoAggiornamento;
	}

	public Anagrafica getAnagraficaDebitore() {
		return this.anagraficaDebitore;
	}

	public void setAnagraficaDebitore(Anagrafica anagraficaDebitore) {
		this.anagraficaDebitore = anagraficaDebitore;
	}

	public String getCodBundlekey() {
		return this.codBundlekey;
	}

	public void setCodBundlekey(String codBundlekey) {
		this.codBundlekey = codBundlekey;
	}

	public Causale getCausaleVersamento() {
		return this.causaleVersamento;
	}

	public void setCausaleVersamento(Causale causaleVersamento) {
		this.causaleVersamento = causaleVersamento;
	}

	public void setCausaleVersamento(String causaleVersamentoEncoded) throws UnsupportedEncodingException {
		if(causaleVersamentoEncoded == null) 
			this.causaleVersamento = null;
		else
			this.causaleVersamento = Versamento.decode(causaleVersamentoEncoded);
	}

	public interface Causale {
		public String encode() throws UnsupportedEncodingException;
		public String getSimple() throws UnsupportedEncodingException;
	}

	public class CausaleSemplice implements Causale {
		private String causale;

		@Override
		public String encode() throws UnsupportedEncodingException {
			if(this.causale == null) return null;
			return "01 " + Base64.encodeBase64String(this.causale.getBytes(StandardCharsets.UTF_8));
		}

		@Override
		public String getSimple() throws UnsupportedEncodingException {
			return this.getCausale();
		}

		public void setCausale(String causale) {
			this.causale = causale;
		}

		public String getCausale() {
			return this.causale;
		}

		@Override
		public String toString() {
			return this.causale;
		}

	}

	public class CausaleSpezzoni implements Causale {
		private List<String> spezzoni;

		@Override
		public String encode() throws UnsupportedEncodingException {
			if(this.spezzoni == null) return null;
			String encoded = "02";
			for(String spezzone : this.spezzoni) {
				encoded += " " + Base64.encodeBase64String(spezzone.getBytes(StandardCharsets.UTF_8));
			}
			return encoded;
		}

		@Override
		public String getSimple() throws UnsupportedEncodingException {
			if(this.spezzoni != null && !this.spezzoni.isEmpty())
				return this.spezzoni.get(0);

			return "";
		}

		public void setSpezzoni(List<String> spezzoni) {
			this.spezzoni = spezzoni;
		}

		public List<String> getSpezzoni() {
			return this.spezzoni;
		}

		@Override
		public String toString() {
			return StringUtils.join(this.spezzoni, "; ");
		}
	}

	public class CausaleSpezzoniStrutturati implements Causale {
		private List<String> spezzoni;
		private List<BigDecimal> importi;

		@Override
		public String encode() throws UnsupportedEncodingException {
			if(this.spezzoni == null) return null;
			String encoded = "03";
			for(int i=0; i<this.spezzoni.size(); i++) {
				encoded += " " + Base64.encodeBase64String(this.spezzoni.get(i).getBytes(StandardCharsets.UTF_8)) + " " + Base64.encodeBase64String(Double.toString(this.importi.get(i).doubleValue()).getBytes(StandardCharsets.UTF_8));
			}
			return encoded;
		}

		@Override
		public String getSimple() throws UnsupportedEncodingException {
			if(this.spezzoni != null && !this.spezzoni.isEmpty()){
				StringBuffer sb = new StringBuffer();
				sb.append(this.importi.get(0).doubleValue() + ": " + this.spezzoni.get(0) );
				return sb.toString();
			}

			return "";
		}

		public CausaleSpezzoniStrutturati() {
			this.spezzoni = new ArrayList<>();
			this.importi = new ArrayList<>();
		}

		public void setSpezzoni(List<String> spezzoni) {
			this.spezzoni = spezzoni;
		}

		public List<String> getSpezzoni() {
			return this.spezzoni;
		}

		public void setImporti(List<BigDecimal> importi) {
			this.importi = importi;
		}

		public List<BigDecimal> getImporti() {
			return this.importi;
		}

		public void addSpezzoneStrutturato(String spezzone, BigDecimal importo){
			this.spezzoni.add(spezzone);
			this.importi.add(importo);
		}

		@Override
		public String toString() {
			StringBuffer sb = new StringBuffer();
			for(int i=0; i<this.spezzoni.size(); i++) {
				sb.append(this.importi.get(i).doubleValue() + ": " + this.spezzoni.get(i) + "; ");
			}
			return sb.toString();
		}
	}

	public static Causale decode(String encodedCausale) throws UnsupportedEncodingException {
		if(encodedCausale == null || encodedCausale.trim().isEmpty())
			return null;

		String[] causaleSplit = encodedCausale.split(" ");
		if(causaleSplit[0].equals("01")) {
			CausaleSemplice causale = new Versamento().new CausaleSemplice();
			if(causaleSplit.length > 1 && causaleSplit[1] != null) {
				causale.setCausale(new String(Base64.decodeBase64(causaleSplit[1].getBytes()), StandardCharsets.UTF_8));
				return causale;
			} else {
				return null;
			}
		}

		if(causaleSplit[0].equals("02")) {
			List<String> spezzoni = new ArrayList<>();
			for(int i=1; i<causaleSplit.length; i++) {
				spezzoni.add(new String(Base64.decodeBase64(causaleSplit[i].getBytes()), StandardCharsets.UTF_8));
			}
			CausaleSpezzoni causale = new Versamento().new CausaleSpezzoni();
			causale.setSpezzoni(spezzoni);
			return causale;
		}

		if(causaleSplit[0].equals("03")) {
			List<String> spezzoni = new ArrayList<>();
			List<BigDecimal> importi = new ArrayList<>();

			for(int i=1; i<causaleSplit.length; i=i+2) {
				spezzoni.add(new String(Base64.decodeBase64(causaleSplit[i].getBytes()), StandardCharsets.UTF_8));
				importi.add(BigDecimal.valueOf(Double.parseDouble(new String(Base64.decodeBase64(causaleSplit[i+1].getBytes()), StandardCharsets.UTF_8))));
			}
			CausaleSpezzoniStrutturati causale = new Versamento().new CausaleSpezzoniStrutturati();
			causale.setSpezzoni(spezzoni);
			causale.setImporti(importi);
			return causale;
		}
		throw new UnsupportedEncodingException();
	}

	public boolean isBolloTelematico() {
		return this.bolloTelematico;
	}

	public void setBolloTelematico(boolean bolloTelematico) {
		this.bolloTelematico = bolloTelematico;
	}

	public String getIuvProposto() {
		return this.iuvProposto;
	}

	public void setIuvProposto(String iuvProposto) {
		this.iuvProposto = iuvProposto;
	}

	public String getCodLotto() {
		return this.codLotto;
	}

	public void setCodLotto(String codLotto) {
		this.codLotto = codLotto;
	}

	public String getCodVersamentoLotto() {
		return this.codVersamentoLotto;
	}

	public void setCodVersamentoLotto(String codVersamentoLotto) {
		this.codVersamentoLotto = codVersamentoLotto;
	}

	public Integer getCodAnnoTributario() {
		return this.codAnnoTributario;
	}

	public void setCodAnnoTributario(Integer codAnnoTributario) {
		this.codAnnoTributario = codAnnoTributario;
	}

	public String getDatiAllegati() {
		return this.datiAllegati;
	}

	public void setDatiAllegati(String datiAllegati) {
		this.datiAllegati = datiAllegati;
	}

	public Boolean getIncasso() {
		return this.incasso;
	}

	public void setIncasso(Boolean incasso) {
		this.incasso = incasso;
	}

	public String getAnomalie() {
		return this.anomalie;
	}

	public void setAnomalie(String anomalie) {
		this.anomalie = anomalie;
	}

	public boolean isAck() {
		return this.ack;
	}
	public void setAck(boolean ack) {
		this.ack = ack;
	}

	public boolean isAnomalo() {
		return anomalo;
	}

	public void setAnomalo(boolean anomalo) {
		this.anomalo = anomalo;
	}
	
	public long getIdTipoVersamento() {
		return idTipoVersamento;
	}

	public void setIdTipoVersamento(long idTipoVersamento) {
		this.idTipoVersamento = idTipoVersamento;
	}

	public boolean checkEsecuzioneUpdate(Versamento oldVersamento) {
		
		boolean equals = 
				Objects.equals(this.getDataScadenza(), oldVersamento.getDataScadenza()) && 
				Objects.equals(this.getDataValidita(), oldVersamento.getDataValidita()) && 
				Objects.equals(this.getStatoVersamento(), oldVersamento.getStatoVersamento()) && 
				Objects.equals(this.getImportoTotale(), oldVersamento.getImportoTotale()) && 
				Objects.equals(this.getNumeroAvviso(), oldVersamento.getNumeroAvviso()) && 
				Objects.equals(this.getCausaleVersamento(), oldVersamento.getCausaleVersamento());
		
		return !equals;
	}

	public long getIdTipoVersamentoDominio() {
		return idTipoVersamentoDominio;
	}

	public void setIdTipoVersamentoDominio(long idTipoVersamentoDominio) {
		this.idTipoVersamentoDominio = idTipoVersamentoDominio;
	}

	public String getIdSessione() {
		return idSessione;
	}

	public void setIdSessione(String idSessione) {
		this.idSessione = idSessione;
	}
	public Integer getNumeroRata() {
		return numeroRata;
	}
	public void setNumeroRata(Integer numeroRata) {
		this.numeroRata = numeroRata;
	}
	public Long getIdDocumento() {
		return idDocumento;
	}
	public void setIdDocumento(Long idDocumento) {
		this.idDocumento = idDocumento;
	}
	public String getCodDocumento() {
		return codDocumento;
	}
	public void setCodDocumento(String codDocumento) {
		this.codDocumento = codDocumento;
	}
	public TipologiaTipoVersamento getTipo() {
		return tipo;
	}
	public void setTipo(TipologiaTipoVersamento tipo) {
		this.tipo = tipo;
	}
	public Date getDataNotificaAvviso() {
		return dataNotificaAvviso;
	}
	public void setDataNotificaAvviso(Date dataNotificaAvviso) {
		this.dataNotificaAvviso = dataNotificaAvviso;
	}
	public Boolean getAvvisoNotificato() {
		return avvisoNotificato;
	}
	public void setAvvisoNotificato(Boolean avvisoNotificato) {
		this.avvisoNotificato = avvisoNotificato;
	}
	public Date getAvvMailDataPromemoriaScadenza() {
		return avvMailDataPromemoriaScadenza;
	}
	public void setAvvMailDataPromemoriaScadenza(Date avvMailDataPromemoriaScadenza) {
		this.avvMailDataPromemoriaScadenza = avvMailDataPromemoriaScadenza;
	}
	public Boolean getAvvMailPromemoriaScadenzaNotificato() {
		return avvMailPromemoriaScadenzaNotificato;
	}
	public void setAvvMailPromemoriaScadenzaNotificato(Boolean avvMailPromemoriaScadenzaNotificato) {
		this.avvMailPromemoriaScadenzaNotificato = avvMailPromemoriaScadenzaNotificato;
	}
	public Date getAvvAppIODataPromemoriaScadenza() {
		return avvAppIODataPromemoriaScadenza;
	}
	public void setAvvAppIODataPromemoriaScadenza(Date avvAppIODataPromemoriaScadenza) {
		this.avvAppIODataPromemoriaScadenza = avvAppIODataPromemoriaScadenza;
	}
	public Boolean getAvvAppIOPromemoriaScadenzaNotificato() {
		return avvAppIOPromemoriaScadenzaNotificato;
	}
	public void setAvvAppIOPromemoriaScadenzaNotificato(Boolean avvAppIOPromemoriaScadenzaNotificato) {
		this.avvAppIOPromemoriaScadenzaNotificato = avvAppIOPromemoriaScadenzaNotificato;
	}
	public TipoSogliaVersamento getTipoSoglia() {
		return tipoSoglia;
	}
	public void setTipoSoglia(TipoSogliaVersamento tipoSoglia) {
		this.tipoSoglia = tipoSoglia;
	}
	public Integer getGiorniSoglia() {
		return giorniSoglia;
	}
	public void setGiorniSoglia(Integer giorniSoglia) {
		this.giorniSoglia = giorniSoglia;
	}
	public String getProprieta() {
		return proprieta;
	}
	public void setProprieta(String proprieta) {
		this.proprieta = proprieta;
	}
}
