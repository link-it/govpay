package it.govpay.web.rs.v1.beans;

import java.util.HashMap;
import java.util.Map;
import org.codehaus.jackson.annotate.JsonCreator;
import org.codehaus.jackson.annotate.JsonIgnore;
import org.codehaus.jackson.annotate.JsonProperty;
import org.codehaus.jackson.annotate.JsonPropertyOrder;
import org.codehaus.jackson.annotate.JsonValue;
import org.codehaus.jackson.map.annotate.JsonSerialize;

@JsonSerialize(include = JsonSerialize.Inclusion.NON_NULL)
@JsonPropertyOrder({
	"identificativoUnivoco",
	"tipoIdentificativoUnivoco",
	"denominazione",
	"indirizzo",
	"civico",
	"cap",
	"localita",
	"provincia",
	"nazione",
	"email",
	"cellulare"
})
public class Anagrafica {

	/**
	 * Identificativo del soggetto
	 * (Required)
	 * 
	 */
	@JsonProperty("identificativoUnivoco")
	private String identificativoUnivoco;
	/**
	 * 
	 * (Required)
	 * 
	 */
	@JsonProperty("tipoIdentificativoUnivoco")
	private Anagrafica.TipoIdentificativoUnivoco tipoIdentificativoUnivoco = Anagrafica.TipoIdentificativoUnivoco.fromValue("F");
	/**
	 * Nominativo o ragione sociale del soggetto
	 * (Required)
	 * 
	 */
	@JsonProperty("denominazione")
	private String denominazione;
	@JsonProperty("indirizzo")
	private String indirizzo;
	@JsonProperty("civico")
	private String civico;
	@JsonProperty("cap")
	private String cap;
	@JsonProperty("localita")
	private String localita;
	@JsonProperty("provincia")
	private String provincia;
	@JsonProperty("nazione")
	private String nazione;
	@JsonProperty("email")
	private String email;
	@JsonProperty("cellulare")
	private String cellulare;
	@JsonIgnore
	private Map<String, Object> additionalProperties = new HashMap<String, Object>();

	/**
	 * Identificativo del soggetto
	 * (Required)
	 * 
	 */
	@JsonProperty("identificativoUnivoco")
	public String getIdentificativoUnivoco() {
		return identificativoUnivoco;
	}

	/**
	 * Identificativo del soggetto
	 * (Required)
	 * 
	 */
	@JsonProperty("identificativoUnivoco")
	public void setIdentificativoUnivoco(String identificativoUnivoco) {
		this.identificativoUnivoco = identificativoUnivoco;
	}

	/**
	 * 
	 * (Required)
	 * 
	 */
	@JsonProperty("tipoIdentificativoUnivoco")
	public Anagrafica.TipoIdentificativoUnivoco getTipoIdentificativoUnivoco() {
		return tipoIdentificativoUnivoco;
	}

	/**
	 * 
	 * (Required)
	 * 
	 */
	@JsonProperty("tipoIdentificativoUnivoco")
	public void setTipoIdentificativoUnivoco(Anagrafica.TipoIdentificativoUnivoco tipoIdentificativoUnivoco) {
		this.tipoIdentificativoUnivoco = tipoIdentificativoUnivoco;
	}

	/**
	 * Nominativo o ragione sociale del soggetto
	 * (Required)
	 * 
	 */
	@JsonProperty("denominazione")
	public String getDenominazione() {
		return denominazione;
	}

	/**
	 * Nominativo o ragione sociale del soggetto
	 * (Required)
	 * 
	 */
	@JsonProperty("denominazione")
	public void setDenominazione(String denominazione) {
		this.denominazione = denominazione;
	}

	@JsonProperty("indirizzo")
	public String getIndirizzo() {
		return indirizzo;
	}

	@JsonProperty("indirizzo")
	public void setIndirizzo(String indirizzo) {
		this.indirizzo = indirizzo;
	}

	@JsonProperty("civico")
	public String getCivico() {
		return civico;
	}

	@JsonProperty("civico")
	public void setCivico(String civico) {
		this.civico = civico;
	}

	@JsonProperty("cap")
	public String getCap() {
		return cap;
	}

	@JsonProperty("cap")
	public void setCap(String cap) {
		this.cap = cap;
	}

	@JsonProperty("localita")
	public String getLocalita() {
		return localita;
	}

	@JsonProperty("localita")
	public void setLocalita(String localita) {
		this.localita = localita;
	}

	@JsonProperty("provincia")
	public String getProvincia() {
		return provincia;
	}

	@JsonProperty("provincia")
	public void setProvincia(String provincia) {
		this.provincia = provincia;
	}

	@JsonProperty("nazione")
	public String getNazione() {
		return nazione;
	}

	@JsonProperty("nazione")
	public void setNazione(String nazione) {
		this.nazione = nazione;
	}

	@JsonProperty("email")
	public String getEmail() {
		return email;
	}

	@JsonProperty("email")
	public void setEmail(String email) {
		this.email = email;
	}
	
	@JsonProperty("cellulare")
	public String getCellulare() {
		return cellulare;
	}

	@JsonProperty("cellulare")
	public void setCellulare(String cellulare) {
		this.cellulare = cellulare;
	}

	public enum TipoIdentificativoUnivoco {

		F("F"),
		G("G");
		private final String value;
		private final static Map<String, Anagrafica.TipoIdentificativoUnivoco> CONSTANTS = new HashMap<String, Anagrafica.TipoIdentificativoUnivoco>();

		static {
			for (Anagrafica.TipoIdentificativoUnivoco c: values()) {
				CONSTANTS.put(c.value, c);
			}
		}

		private TipoIdentificativoUnivoco(String value) {
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
		public static Anagrafica.TipoIdentificativoUnivoco fromValue(String value) {
			Anagrafica.TipoIdentificativoUnivoco constant = CONSTANTS.get(value);
			if (constant == null) {
				throw new IllegalArgumentException(value);
			} else {
				return constant;
			}
		}

	}

}
