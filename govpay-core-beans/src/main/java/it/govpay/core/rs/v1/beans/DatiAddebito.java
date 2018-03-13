
package it.govpay.core.rs.v1.beans;

import org.codehaus.jackson.annotate.JsonProperty;
import org.codehaus.jackson.annotate.JsonPropertyOrder;
import org.codehaus.jackson.map.annotate.JsonFilter;

import net.sf.json.JSONObject;
import net.sf.json.JsonConfig;



/**
 * Dati necessari alla realizzazione dei pagamenti per Addebito Diretto, se previsto dal profilo del versante.
 * 
 */
@JsonPropertyOrder({
    "ibanAddebito",
    "bicAddebito"
})
@JsonFilter(value="datiAddebito")  
public class DatiAddebito extends JSONSerializable {

    /**
     * Iban di addebito del pagatore.
     * (Required)
     * 
     */
    @JsonProperty("ibanAddebito")
    private String ibanAddebito;
    /**
     * Bic della banca di addebito del pagatore.
     * 
     */
    @JsonProperty("bicAddebito")
    private String bicAddebito;

    /**
     * Iban di addebito del pagatore.
     * (Required)
     * 
     */
    @JsonProperty("ibanAddebito")
    public String getIbanAddebito() {
        return ibanAddebito;
    }

    /**
     * Iban di addebito del pagatore.
     * (Required)
     * 
     */
    @JsonProperty("ibanAddebito")
    public void setIbanAddebito(String ibanAddebito) {
        this.ibanAddebito = ibanAddebito;
    }

    /**
     * Bic della banca di addebito del pagatore.
     * 
     */
    @JsonProperty("bicAddebito")
    public String getBicAddebito() {
        return bicAddebito;
    }

    /**
     * Bic della banca di addebito del pagatore.
     * 
     */
    @JsonProperty("bicAddebito")
    public void setBicAddebito(String bicAddebito) {
        this.bicAddebito = bicAddebito;
    }

	@Override
	public String getJsonIdFilter() {
		return "datiAddebito";
	}
    
	private static JsonConfig jsonConfig = new JsonConfig();

	static {
		jsonConfig.setRootClass(DatiAddebito.class);
	}
	public DatiAddebito() {}

}
