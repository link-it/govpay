package it.govpay.rs.v1.beans;

import java.util.HashMap;
import java.util.Map;
import org.codehaus.jackson.annotate.JsonAnyGetter;
import org.codehaus.jackson.annotate.JsonAnySetter;
import org.codehaus.jackson.annotate.JsonCreator;
import org.codehaus.jackson.annotate.JsonIgnore;
import org.codehaus.jackson.annotate.JsonProperty;
import org.codehaus.jackson.annotate.JsonPropertyOrder;
import org.codehaus.jackson.annotate.JsonValue;
import org.codehaus.jackson.map.annotate.JsonSerialize;

@JsonSerialize(include = JsonSerialize.Inclusion.NON_NULL)
@JsonPropertyOrder({
	"href",
	"idDominio",
	"idUnita",
	"iuv",
	"numeroAvviso",
	"stato",
	"pagatore",
	"causale",
	"importo",
	"ibanAccredito",
	"tipoContabilita",
	"codiceContabilita",
	"scadenzaPagamento",
	"scadenzaAvviso"
})
public class PagamentoInAttesa {

	/**
	 * URL dove reperire il pagamento in attesa completo
	 * (Required)
	 * 
	 */
	@JsonProperty("href")
	private String href;
	/**
	 * Codice fiscale del beneficiario
	 * (Required)
	 * 
	 */
	@JsonProperty("idDominio")
	private String idDominio;
	/**
	 * Codice fiscale dell'unità operativa beneficiaria
	 * 
	 */
	@JsonProperty("idUnita")
	private String idUnita;
	/**
	 * Identificativo Univoco Versamento assegnato
	 * (Required)
	 * 
	 */
	@JsonProperty("iuv")
	private String iuv;
	/**
	 * 
	 * (Required)
	 * 
	 */
	@JsonProperty("numeroAvviso")
	private String numeroAvviso;
	/**
	 * Stato di pagamento del versamento
	 * (Required)
	 * 
	 */
	@JsonProperty("stato")
	private PagamentoInAttesa.Stato stato;
	/**
	 * 
	 * (Required)
	 * 
	 */
	@JsonProperty("pagatore")
	private Anagrafica pagatore;
	/**
	 * 
	 * (Required)
	 * 
	 */
	@JsonProperty("causale")
	private String causale;
	/**
	 * Importo della somma da versare
	 * (Required)
	 * 
	 */
	@JsonProperty("importo")
	private Double importo;
	/**
	 * Conto di accredito dell'importo versato
	 * (Required)
	 * 
	 */
	@JsonProperty("ibanAccredito")
	private String ibanAccredito;
	/**
	 * Tipologia di codifica della contabilità
	 * - 0: Capitolo e articolo di Entrata del Bilancio dello Stato
	 * - 1: Numero della contabilità speciale
	 * - 2: Codice SIOPE
	 * - 9: Altro codice ad uso dell'Ente beneficiario
	 * 
	 * (Required)
	 * 
	 */
	@JsonProperty("tipoContabilita")
	private PagamentoInAttesa.TipoContabilita tipoContabilita;
	/**
	 * Codice di contabilità nella tipologia scelta
	 * (Required)
	 * 
	 */
	@JsonProperty("codiceContabilita")
	private String codiceContabilita;
	/**
	 * Data di scadenza del pagamento, decorsa la quale l'importo può subire variazioni
	 * (Required)
	 * 
	 */
	@JsonProperty("scadenzaPagamento")
	private String scadenzaPagamento;
	/**
	 * Data di scadenza dell'Avviso, decorsa la quale il pagamento non può essere effettuato
	 * (Required)
	 * 
	 */
	@JsonProperty("scadenzaAvviso")
	private String scadenzaAvviso;
	@JsonIgnore
	private Map<String, Object> additionalProperties = new HashMap<String, Object>();

	/**
	 * URL dove reperire il pagamento in attesa completo
	 * (Required)
	 * 
	 */
	@JsonProperty("href")
	public String getHref() {
		return href;
	}

	/**
	 * URL dove reperire il pagamento in attesa completo
	 * (Required)
	 * 
	 */
	@JsonProperty("href")
	public void setHref(String href) {
		this.href = href;
	}

	/**
	 * Codice fiscale del beneficiario
	 * (Required)
	 * 
	 */
	@JsonProperty("idDominio")
	public String getIdDominio() {
		return idDominio;
	}

	/**
	 * Codice fiscale del beneficiario
	 * (Required)
	 * 
	 */
	@JsonProperty("idDominio")
	public void setIdDominio(String idDominio) {
		this.idDominio = idDominio;
	}

	/**
	 * Codice fiscale dell'unità operativa beneficiaria
	 * 
	 */
	@JsonProperty("idUnita")
	public String getIdUnita() {
		return idUnita;
	}

	/**
	 * Codice fiscale dell'unità operativa beneficiaria
	 * 
	 */
	@JsonProperty("idUnita")
	public void setIdUnita(String idUnita) {
		this.idUnita = idUnita;
	}

	/**
	 * Identificativo Univoco Versamento assegnato
	 * (Required)
	 * 
	 */
	@JsonProperty("iuv")
	public String getIuv() {
		return iuv;
	}

	/**
	 * Identificativo Univoco Versamento assegnato
	 * (Required)
	 * 
	 */
	@JsonProperty("iuv")
	public void setIuv(String iuv) {
		this.iuv = iuv;
	}

	/**
	 * 
	 * (Required)
	 * 
	 */
	@JsonProperty("numeroAvviso")
	public String getNumeroAvviso() {
		return numeroAvviso;
	}

	/**
	 * 
	 * (Required)
	 * 
	 */
	@JsonProperty("numeroAvviso")
	public void setNumeroAvviso(String numeroAvviso) {
		this.numeroAvviso = numeroAvviso;
	}

	/**
	 * Stato di pagamento del versamento
	 * (Required)
	 * 
	 */
	@JsonProperty("stato")
	public PagamentoInAttesa.Stato getStato() {
		return stato;
	}

	/**
	 * Stato di pagamento del versamento
	 * (Required)
	 * 
	 */
	@JsonProperty("stato")
	public void setStato(PagamentoInAttesa.Stato stato) {
		this.stato = stato;
	}

	/**
	 * 
	 * (Required)
	 * 
	 */
	@JsonProperty("pagatore")
	public Anagrafica getPagatore() {
		return pagatore;
	}

	/**
	 * 
	 * (Required)
	 * 
	 */
	@JsonProperty("pagatore")
	public void setPagatore(Anagrafica pagatore) {
		this.pagatore = pagatore;
	}

	/**
	 * 
	 * (Required)
	 * 
	 */
	@JsonProperty("causale")
	public String getCausale() {
		return causale;
	}

	/**
	 * 
	 * (Required)
	 * 
	 */
	@JsonProperty("causale")
	public void setCausale(String causale) {
		this.causale = causale;
	}

	/**
	 * Importo della somma da versare
	 * (Required)
	 * 
	 */
	@JsonProperty("importo")
	public Double getImporto() {
		return importo;
	}

	/**
	 * Importo della somma da versare
	 * (Required)
	 * 
	 */
	@JsonProperty("importo")
	public void setImporto(Double importo) {
		this.importo = importo;
	}

	/**
	 * Conto di accredito dell'importo versato
	 * (Required)
	 * 
	 */
	@JsonProperty("ibanAccredito")
	public String getIbanAccredito() {
		return ibanAccredito;
	}

	/**
	 * Conto di accredito dell'importo versato
	 * (Required)
	 * 
	 */
	@JsonProperty("ibanAccredito")
	public void setIbanAccredito(String ibanAccredito) {
		this.ibanAccredito = ibanAccredito;
	}

	/**
	 * Tipologia di codifica della contabilità
	 * - 0: Capitolo e articolo di Entrata del Bilancio dello Stato
	 * - 1: Numero della contabilità speciale
	 * - 2: Codice SIOPE
	 * - 9: Altro codice ad uso dell'Ente beneficiario
	 * 
	 * (Required)
	 * 
	 */
	@JsonProperty("tipoContabilita")
	public PagamentoInAttesa.TipoContabilita getTipoContabilita() {
		return tipoContabilita;
	}

	/**
	 * Tipologia di codifica della contabilità
	 * - 0: Capitolo e articolo di Entrata del Bilancio dello Stato
	 * - 1: Numero della contabilità speciale
	 * - 2: Codice SIOPE
	 * - 9: Altro codice ad uso dell'Ente beneficiario
	 * 
	 * (Required)
	 * 
	 */
	@JsonProperty("tipoContabilita")
	public void setTipoContabilita(PagamentoInAttesa.TipoContabilita tipoContabilita) {
		this.tipoContabilita = tipoContabilita;
	}

	/**
	 * Codice di contabilità nella tipologia scelta
	 * (Required)
	 * 
	 */
	@JsonProperty("codiceContabilita")
	public String getCodiceContabilita() {
		return codiceContabilita;
	}

	/**
	 * Codice di contabilità nella tipologia scelta
	 * (Required)
	 * 
	 */
	@JsonProperty("codiceContabilita")
	public void setCodiceContabilita(String codiceContabilita) {
		this.codiceContabilita = codiceContabilita;
	}

	/**
	 * Data di scadenza del pagamento, decorsa la quale l'importo può subire variazioni
	 * (Required)
	 * 
	 */
	@JsonProperty("scadenzaPagamento")
	public String getScadenzaPagamento() {
		return scadenzaPagamento;
	}

	/**
	 * Data di scadenza del pagamento, decorsa la quale l'importo può subire variazioni
	 * (Required)
	 * 
	 */
	@JsonProperty("scadenzaPagamento")
	public void setScadenzaPagamento(String scadenzaPagamento) {
		this.scadenzaPagamento = scadenzaPagamento;
	}

	/**
	 * Data di scadenza dell'Avviso, decorsa la quale il pagamento non può essere effettuato
	 * (Required)
	 * 
	 */
	@JsonProperty("scadenzaAvviso")
	public String getScadenzaAvviso() {
		return scadenzaAvviso;
	}

	/**
	 * Data di scadenza dell'Avviso, decorsa la quale il pagamento non può essere effettuato
	 * (Required)
	 * 
	 */
	@JsonProperty("scadenzaAvviso")
	public void setScadenzaAvviso(String scadenzaAvviso) {
		this.scadenzaAvviso = scadenzaAvviso;
	}

	@JsonAnyGetter
	public Map<String, Object> getAdditionalProperties() {
		return this.additionalProperties;
	}

	@JsonAnySetter
	public void setAdditionalProperty(String name, Object value) {
		this.additionalProperties.put(name, value);
	}

	public enum Stato {

		NON_ESEGUITO("NON ESEGUITO"),
		ESEGUITO("ESEGUITO"),
		ANNULLATO("ANNULLATO"),
		SCADUTO("SCADUTO");
		private final String value;
		private final static Map<String, PagamentoInAttesa.Stato> CONSTANTS = new HashMap<String, PagamentoInAttesa.Stato>();

		static {
			for (PagamentoInAttesa.Stato c: values()) {
				CONSTANTS.put(c.value, c);
			}
		}

		private Stato(String value) {
			this.value = value;
		}

		@Override
		public String toString() {
			return this.value;
		}

		@JsonValue
		public String value() {
			return this.value;
		}

		@JsonCreator
		public static PagamentoInAttesa.Stato fromValue(String value) {
			PagamentoInAttesa.Stato constant = CONSTANTS.get(value);
			if (constant == null) {
				throw new IllegalArgumentException(value);
			} else {
				return constant;
			}
		}

	}

	public enum TipoContabilita {

		_0("0"),
		_1("1"),
		_2("2"),
		_9("9");
		private final String value;
		private final static Map<String, PagamentoInAttesa.TipoContabilita> CONSTANTS = new HashMap<String, PagamentoInAttesa.TipoContabilita>();

		static {
			for (PagamentoInAttesa.TipoContabilita c: values()) {
				CONSTANTS.put(c.value, c);
			}
		}

		private TipoContabilita(String value) {
			this.value = value;
		}

		@Override
		public String toString() {
			return this.value;
		}

		@JsonValue
		public String value() {
			return this.value;
		}

		@JsonCreator
		public static TipoContabilita fromValue(String value) {
			PagamentoInAttesa.TipoContabilita constant = CONSTANTS.get(value);
			if (constant == null) {
				throw new IllegalArgumentException(value);
			} else {
				return constant;
			}
		}

	}

}