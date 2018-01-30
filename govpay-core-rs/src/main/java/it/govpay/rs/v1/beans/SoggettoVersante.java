
package it.govpay.rs.v1.beans;

import java.util.HashMap;
import java.util.Map;

import org.codehaus.jackson.annotate.JsonCreator;
import org.codehaus.jackson.annotate.JsonProperty;
import org.codehaus.jackson.annotate.JsonPropertyOrder;
import org.codehaus.jackson.annotate.JsonValue;
import org.codehaus.jackson.map.annotate.JsonFilter;

import net.sf.json.JSONObject;
import net.sf.json.JsonConfig;

@JsonPropertyOrder({
    "tipo",
    "identificativo",
    "anagrafica",
    "indirizzo",
    "civico",
    "cap",
    "localita",
    "provincia",
    "nazione",
    "email",
    "cellulare"
})

@JsonFilter(value="soggettoVersante")  
public class SoggettoVersante extends JSONSerializable{

    /**
     * tipologia di soggetto, se persona fisica (F) o giuridica (G)
     * (Required)
     * 
     */
    @JsonProperty("tipo")
    private SoggettoVersante.Tipo tipo;
    /**
     * codice fiscale o partita iva del soggetto
     * (Required)
     * 
     */
    @JsonProperty("identificativo")
    private String identificativo;
    /**
     * nome e cognome o altra ragione sociale del soggetto
     * 
     */
    @JsonProperty("anagrafica")
    private String anagrafica;
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

    /**
     * tipologia di soggetto, se persona fisica (F) o giuridica (G)
     * (Required)
     * 
     */
    @JsonProperty("tipo")
    public SoggettoVersante.Tipo getTipo() {
        return tipo;
    }

    /**
     * tipologia di soggetto, se persona fisica (F) o giuridica (G)
     * (Required)
     * 
     */
    @JsonProperty("tipo")
    public void setTipo(SoggettoVersante.Tipo tipo) {
        this.tipo = tipo;
    }

    /**
     * codice fiscale o partita iva del soggetto
     * (Required)
     * 
     */
    @JsonProperty("identificativo")
    public String getIdentificativo() {
        return identificativo;
    }

    /**
     * codice fiscale o partita iva del soggetto
     * (Required)
     * 
     */
    @JsonProperty("identificativo")
    public void setIdentificativo(String identificativo) {
        this.identificativo = identificativo;
    }

    /**
     * nome e cognome o altra ragione sociale del soggetto
     * 
     */
    @JsonProperty("anagrafica")
    public String getAnagrafica() {
        return anagrafica;
    }

    /**
     * nome e cognome o altra ragione sociale del soggetto
     * 
     */
    @JsonProperty("anagrafica")
    public void setAnagrafica(String anagrafica) {
        this.anagrafica = anagrafica;
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

    public enum Tipo {

        G("G"),
        F("F");
        private final String value;
        private final static Map<String, SoggettoVersante.Tipo> CONSTANTS = new HashMap<String, SoggettoVersante.Tipo>();

        static {
            for (SoggettoVersante.Tipo c: values()) {
                CONSTANTS.put(c.value, c);
            }
        }

        private Tipo(String value) {
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
        public static SoggettoVersante.Tipo fromValue(String value) {
            SoggettoVersante.Tipo constant = CONSTANTS.get(value);
            if (constant == null) {
                throw new IllegalArgumentException(value);
            } else {
                return constant;
            }
        }

    }
    
	private static JsonConfig jsonConfig = new JsonConfig();

	static {
		jsonConfig.setRootClass(SoggettoVersante.class);
	}
	public SoggettoVersante() {}
	
	@Override
	public String getJsonIdFilter() {
		return "soggettoVersante";
	}
	
	public static SoggettoVersante parse(String json) {
		JSONObject jsonObject = JSONObject.fromObject( json );  
		return (SoggettoVersante) JSONObject.toBean( jsonObject, jsonConfig );
	}


}
