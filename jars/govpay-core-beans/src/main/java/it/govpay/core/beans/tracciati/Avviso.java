package it.govpay.core.beans.tracciati;

import java.math.BigDecimal;
import java.util.Date;
import java.util.Objects;

import org.openspcoop2.utils.json.ValidationException;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonValue;

@com.fasterxml.jackson.annotation.JsonPropertyOrder({
"stato",
"importo",
"idDominio",
"numeroAvviso",
"dataValidita",
"dataScadenza",
"descrizione",
"tassonomiaAvviso",
"qrcode",
"barcode",
})
public class Avviso extends it.govpay.core.beans.JSONSerializable {
  
    
  /**
   * Stato dell'avviso
   */
  public enum StatoEnum {
    
    
        
            
    PAGATO("Pagato"),
    
            
    NON_PAGATO("Non pagato"),
    
            
    SCADUTO("Scaduto"),
    
            
    ANNULLATO("Annullato");
            
        
    

    private String value;

    StatoEnum(String value) {
      this.value = value;
    }

    @Override
    @JsonValue
    public String toString() {
      return String.valueOf(this.value);
    }

    public static StatoEnum fromValue(String text) {
      for (StatoEnum b : StatoEnum.values()) {
        if (String.valueOf(b.value).equals(text)) {
          return b;
        }
      }
      return null;
    }
  }

    
    
  @JsonProperty("stato")
  private StatoEnum stato = null;
  
  @JsonProperty("importo")
  private BigDecimal importo = null;
  
  @JsonProperty("idDominio")
  private String idDominio = null;
  
  @JsonProperty("numeroAvviso")
  private String numeroAvviso = null;
  
  @JsonProperty("dataValidita")
  private Date dataValidita = null;
  
  @JsonProperty("dataScadenza")
  private Date dataScadenza = null;
  
  @JsonProperty("descrizione")
  private String descrizione = null;
  
  @JsonProperty("tassonomiaAvviso")
  private String tassonomiaAvviso = null;
  
  @JsonProperty("qrcode")
  private String qrcode = null;
  
  @JsonProperty("barcode")
  private String barcode = null;
  
  /**
   * Stato dell'avviso
   **/
  public Avviso stato(StatoEnum stato) {
    this.stato = stato;
    return this;
  }

  @JsonIgnore
  public StatoEnum getStatoEnum() {
    return this.stato;
  }
  public void setStato(StatoEnum stato) {
    this.stato = stato;
  }
  
  @JsonProperty("stato")
  public String getStato() {
	  if(this.stato != null) {
		  return this.stato.value;
	  } else {
		  return null;
	  }
    
  }
  public void setStato(String stato) throws Exception{
	  if(stato != null) {
		  this.stato = StatoEnum.fromValue(stato);
		  if(this.stato == null)
			  throw new Exception("valore ["+stato+"] non ammesso per la property stato");
	  }
  }

  /**
   * Importo della pendenza. Deve corrispondere alla somma delle singole voci.
   **/
  public Avviso importo(BigDecimal importo) {
    this.importo = importo;
    return this;
  }

  @JsonProperty("importo")
  public BigDecimal getImporto() {
    return this.importo;
  }
  public void setImporto(BigDecimal importo) {
    this.importo = importo;
  }

  /**
   * Identificativo del creditore dell'avviso
   **/
  public Avviso idDominio(String idDominio) {
    this.idDominio = idDominio;
    return this;
  }

  @JsonProperty("idDominio")
  public String getIdDominio() {
    return this.idDominio;
  }
  public void setIdDominio(String idDominio) {
    this.idDominio = idDominio;
  }

  /**
   * Identificativo univoco versamento, assegnato se pagabile da psp
   **/
  public Avviso numeroAvviso(String numeroAvviso) {
    this.numeroAvviso = numeroAvviso;
    return this;
  }

  @JsonProperty("numeroAvviso")
  public String getNumeroAvviso() {
    return this.numeroAvviso;
  }
  public void setNumeroAvviso(String numeroAvviso) {
    this.numeroAvviso = numeroAvviso;
  }

  /**
   * Data di validita dei dati della pendenza, decorsa la quale la pendenza può subire variazioni.
   **/
  public Avviso dataValidita(Date dataValidita) {
    this.dataValidita = dataValidita;
    return this;
  }

  @JsonProperty("dataValidita")
  public Date getDataValidita() {
    return this.dataValidita;
  }
  public void setDataValidita(Date dataValidita) {
    this.dataValidita = dataValidita;
  }

  /**
   * Data di scadenza della pendenza, decorsa la quale non è più pagabile.
   **/
  public Avviso dataScadenza(Date dataScadenza) {
    this.dataScadenza = dataScadenza;
    return this;
  }

  @JsonProperty("dataScadenza")
  public Date getDataScadenza() {
    return this.dataScadenza;
  }
  public void setDataScadenza(Date dataScadenza) {
    this.dataScadenza = dataScadenza;
  }

  /**
   * Descrizione da inserire nell'avviso di pagamento
   **/
  public Avviso descrizione(String descrizione) {
    this.descrizione = descrizione;
    return this;
  }

  @JsonProperty("descrizione")
  public String getDescrizione() {
    return this.descrizione;
  }
  public void setDescrizione(String descrizione) {
    this.descrizione = descrizione;
  }

  /**
   * Macro categoria della pendenza secondo la classificazione AgID
   **/
  public Avviso tassonomiaAvviso(String tassonomiaAvviso) {
    this.tassonomiaAvviso = tassonomiaAvviso;
    return this;
  }

  @JsonProperty("tassonomiaAvviso")
  public String getTassonomiaAvviso() {
    return this.tassonomiaAvviso;
  }
  public void setTassonomiaAvviso(String tassonomiaAvviso) {
    this.tassonomiaAvviso = tassonomiaAvviso;
  }

  /**
   * Testo da codificare nel qr-code dell'avviso
   **/
  public Avviso qrcode(String qrcode) {
    this.qrcode = qrcode;
    return this;
  }

  @JsonProperty("qrcode")
  public String getQrcode() {
    return this.qrcode;
  }
  public void setQrcode(String qrcode) {
    this.qrcode = qrcode;
  }

  /**
   * Testo da codificare nel bar-code dell'avviso
   **/
  public Avviso barcode(String barcode) {
    this.barcode = barcode;
    return this;
  }

  @JsonProperty("barcode")
  public String getBarcode() {
    return this.barcode;
  }
  public void setBarcode(String barcode) {
    this.barcode = barcode;
  }

  @Override
  public boolean equals(java.lang.Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || this.getClass() != o.getClass()) {
      return false;
    }
    Avviso avviso = (Avviso) o;
    return Objects.equals(this.stato, avviso.stato) &&
        Objects.equals(this.importo, avviso.importo) &&
        Objects.equals(this.idDominio, avviso.idDominio) &&
        Objects.equals(this.numeroAvviso, avviso.numeroAvviso) &&
        Objects.equals(this.dataValidita, avviso.dataValidita) &&
        Objects.equals(this.dataScadenza, avviso.dataScadenza) &&
        Objects.equals(this.descrizione, avviso.descrizione) &&
        Objects.equals(this.tassonomiaAvviso, avviso.tassonomiaAvviso) &&
        Objects.equals(this.qrcode, avviso.qrcode) &&
        Objects.equals(this.barcode, avviso.barcode);
  }

  @Override
  public int hashCode() {
    return Objects.hash(this.stato, this.importo, this.idDominio, this.numeroAvviso, this.dataValidita, this.dataScadenza, this.descrizione, this.tassonomiaAvviso, this.qrcode, this.barcode);
  }

  public static Avviso parse(String json) throws org.openspcoop2.generic_project.exception.ServiceException, ValidationException {
    return parse(json, Avviso.class);
  }

  @Override
  public String getJsonIdFilter() {
    return "avviso";
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class Avviso {\n");
    
    sb.append("    stato: ").append(this.toIndentedString(this.stato)).append("\n");
    sb.append("    importo: ").append(this.toIndentedString(this.importo)).append("\n");
    sb.append("    idDominio: ").append(this.toIndentedString(this.idDominio)).append("\n");
    sb.append("    numeroAvviso: ").append(this.toIndentedString(this.numeroAvviso)).append("\n");
    sb.append("    dataValidita: ").append(this.toIndentedString(this.dataValidita)).append("\n");
    sb.append("    dataScadenza: ").append(this.toIndentedString(this.dataScadenza)).append("\n");
    sb.append("    descrizione: ").append(this.toIndentedString(this.descrizione)).append("\n");
    sb.append("    tassonomiaAvviso: ").append(this.toIndentedString(this.tassonomiaAvviso)).append("\n");
    sb.append("    qrcode: ").append(this.toIndentedString(this.qrcode)).append("\n");
    sb.append("    barcode: ").append(this.toIndentedString(this.barcode)).append("\n");
    sb.append("}");
    return sb.toString();
  }

  /**
   * Convert the given object to string with each line indented by 4 spaces
   * (except the first line).
   */
  private String toIndentedString(java.lang.Object o) {
    if (o == null) {
      return "null";
    }
    return o.toString().replace("\n", "\n    ");
  }
}



