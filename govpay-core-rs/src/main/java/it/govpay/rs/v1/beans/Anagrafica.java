//package it.govpay.rs.v1.beans;
//
//import java.util.HashMap;
//import java.util.Map;
//import org.codehaus.jackson.annotate.JsonCreator;
//import org.codehaus.jackson.annotate.JsonIgnore;
//import org.codehaus.jackson.annotate.JsonProperty;
//import org.codehaus.jackson.annotate.JsonPropertyOrder;
//import org.codehaus.jackson.annotate.JsonValue;
//import org.codehaus.jackson.map.annotate.JsonSerialize;
//
//@JsonSerialize(include = JsonSerialize.Inclusion.NON_NULL)
//@JsonPropertyOrder({
//	"tipo",
//	"identificativo",
//	"anagrafica",
//	"indirizzo",
//	"civico",
//	"cap",
//	"localita",
//	"provincia",
//	"nazione",
//	"email",
//	"cellulare"
//})
//public class Anagrafica {
//
//	/**
//	 * Identificativo del soggetto
//	 * (Required)
//	 * 
//	 */
//	@JsonProperty("identificativo")
//	private String identificativo;
//	/**
//	 * 
//	 * (Required)
//	 * 
//	 */
//	@JsonProperty("tipo")
//	private Anagrafica.TipoIdentificativoUnivoco tipo = Anagrafica.TipoIdentificativoUnivoco.fromValue("F");
//	/**
//	 * Nominativo o ragione sociale del soggetto
//	 * (Required)
//	 * 
//	 */
//	@JsonProperty("anagrafica")
//	private String anagrafica;
//	@JsonProperty("indirizzo")
//	private String indirizzo;
//	@JsonProperty("civico")
//	private String civico;
//	@JsonProperty("cap")
//	private String cap;
//	@JsonProperty("localita")
//	private String localita;
//	@JsonProperty("provincia")
//	private String provincia;
//	@JsonProperty("nazione")
//	private String nazione;
//	@JsonProperty("email")
//	private String email;
//	@JsonProperty("cellulare")
//	private String cellulare;
//	@JsonIgnore
//	private Map<String, Object> additionalProperties = new HashMap<String, Object>();
//
//	/**
//	 * Identificativo del soggetto
//	 * (Required)
//	 * 
//	 */
//	@JsonProperty("identificativo")
//	public String getIdentificativo() {
//		return identificativo;
//	}
//
//	/**
//	 * Identificativo del soggetto
//	 * (Required)
//	 * 
//	 */
//	@JsonProperty("identificativo")
//	public void setIdentificativo(String identificativoUnivoco) {
//		this.identificativo = identificativoUnivoco;
//	}
//
//	/**
//	 * 
//	 * (Required)
//	 * 
//	 */
//	@JsonProperty("tipo")
//	public Anagrafica.TipoIdentificativoUnivoco getTipo() {
//		return tipo;
//	}
//
//	/**
//	 * 
//	 * (Required)
//	 * 
//	 */
//	@JsonProperty("tipo")
//	public void setTipo(Anagrafica.TipoIdentificativoUnivoco tipoIdentificativoUnivoco) {
//		this.tipo = tipoIdentificativoUnivoco;
//	}
//
//	/**
//	 * Nominativo o ragione sociale del soggetto
//	 * (Required)
//	 * 
//	 */
//	@JsonProperty("anagrafica")
//	public String getAnagrafica() {
//		return anagrafica;
//	}
//
//	/**
//	 * Nominativo o ragione sociale del soggetto
//	 * (Required)
//	 * 
//	 */
//	@JsonProperty("anagrafica")
//	public void setAnagrafica(String denominazione) {
//		this.anagrafica = denominazione;
//	}
//
//	@JsonProperty("indirizzo")
//	public String getIndirizzo() {
//		return indirizzo;
//	}
//
//	@JsonProperty("indirizzo")
//	public void setIndirizzo(String indirizzo) {
//		this.indirizzo = indirizzo;
//	}
//
//	@JsonProperty("civico")
//	public String getCivico() {
//		return civico;
//	}
//
//	@JsonProperty("civico")
//	public void setCivico(String civico) {
//		this.civico = civico;
//	}
//
//	@JsonProperty("cap")
//	public String getCap() {
//		return cap;
//	}
//
//	@JsonProperty("cap")
//	public void setCap(String cap) {
//		this.cap = cap;
//	}
//
//	@JsonProperty("localita")
//	public String getLocalita() {
//		return localita;
//	}
//
//	@JsonProperty("localita")
//	public void setLocalita(String localita) {
//		this.localita = localita;
//	}
//
//	@JsonProperty("provincia")
//	public String getProvincia() {
//		return provincia;
//	}
//
//	@JsonProperty("provincia")
//	public void setProvincia(String provincia) {
//		this.provincia = provincia;
//	}
//
//	@JsonProperty("nazione")
//	public String getNazione() {
//		return nazione;
//	}
//
//	@JsonProperty("nazione")
//	public void setNazione(String nazione) {
//		this.nazione = nazione;
//	}
//
//	@JsonProperty("email")
//	public String getEmail() {
//		return email;
//	}
//
//	@JsonProperty("email")
//	public void setEmail(String email) {
//		this.email = email;
//	}
//	
//	@JsonProperty("cellulare")
//	public String getCellulare() {
//		return cellulare;
//	}
//
//	@JsonProperty("cellulare")
//	public void setCellulare(String cellulare) {
//		this.cellulare = cellulare;
//	}
//
//	public enum TipoIdentificativoUnivoco {
//
//		F("F"),
//		G("G");
//		private final String value;
//		private final static Map<String, Anagrafica.TipoIdentificativoUnivoco> CONSTANTS = new HashMap<String, Anagrafica.TipoIdentificativoUnivoco>();
//
//		static {
//			for (Anagrafica.TipoIdentificativoUnivoco c: values()) {
//				CONSTANTS.put(c.value, c);
//			}
//		}
//
//		private TipoIdentificativoUnivoco(String value) {
//			this.value = value;
//		}
//
//		@Override
//		public String toString() {
//			return this.value;
//		}
//
//		@JsonValue
//		public String value() {
//			return this.value;
//		}
//
//		@JsonCreator
//		public static Anagrafica.TipoIdentificativoUnivoco fromValue(String value) {
//			Anagrafica.TipoIdentificativoUnivoco constant = CONSTANTS.get(value);
//			if (constant == null) {
//				throw new IllegalArgumentException(value);
//			} else {
//				return constant;
//			}
//		}
//
//	}
//
//}
