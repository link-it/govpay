package it.govpay.backoffice.v1.beans;


import java.math.BigDecimal;
import java.util.Objects;

import org.openspcoop2.generic_project.exception.ServiceException;
import org.openspcoop2.utils.json.ValidationException;

import com.fasterxml.jackson.annotation.JsonProperty;

import it.govpay.core.beans.JSONSerializable;
import it.govpay.core.utils.validator.CostantiValidazione;
import it.govpay.core.utils.validator.IValidable;
import it.govpay.core.utils.validator.ValidatorFactory;
@com.fasterxml.jackson.annotation.JsonPropertyOrder({
"abilitato",
"url",
"timeToLive",
})
public class AppIOBatch extends JSONSerializable implements IValidable {
  
  @JsonProperty("abilitato")
  private Boolean abilitato = null;
  
  @JsonProperty("url")
  private String url = null;
  
  @JsonProperty("timeToLive")
  private BigDecimal timeToLive = null;
  
  /**
   * Indica lo stato di abilitazione
   **/
  public AppIOBatch abilitato(Boolean abilitato) {
    this.abilitato = abilitato;
    return this;
  }

  @JsonProperty("abilitato")
  public Boolean Abilitato() {
    return abilitato;
  }
  public void setAbilitato(Boolean abilitato) {
    this.abilitato = abilitato;
  }

  /**
   * URL del servizio di IO
   **/
  public AppIOBatch url(String url) {
    this.url = url;
    return this;
  }

  @JsonProperty("url")
  public String getUrl() {
    return url;
  }
  public void setUrl(String url) {
    this.url = url;
  }

  /**
   * Valore in secondi del retry per la consegna dei messaggi da parte di Agid.
   **/
  public AppIOBatch timeToLive(BigDecimal timeToLive) {
    this.timeToLive = timeToLive;
    return this;
  }

  @JsonProperty("timeToLive")
  public BigDecimal getTimeToLive() {
    return timeToLive;
  }
  public void setTimeToLive(BigDecimal timeToLive) {
    this.timeToLive = timeToLive;
  }

  @Override
  public boolean equals(java.lang.Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    AppIOBatch appIOBatch = (AppIOBatch) o;
    return Objects.equals(abilitato, appIOBatch.abilitato) &&
        Objects.equals(url, appIOBatch.url) &&
        Objects.equals(timeToLive, appIOBatch.timeToLive);
  }

  @Override
  public int hashCode() {
    return Objects.hash(abilitato, url, timeToLive);
  }

  public static AppIOBatch parse(String json) throws ServiceException, ValidationException {
    return (AppIOBatch) parse(json, AppIOBatch.class);
  }

  @Override
  public String getJsonIdFilter() {
    return "appIOBatch";
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class AppIOBatch {\n");
    
    sb.append("    abilitato: ").append(toIndentedString(abilitato)).append("\n");
    sb.append("    url: ").append(toIndentedString(url)).append("\n");
    sb.append("    timeToLive: ").append(toIndentedString(timeToLive)).append("\n");
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

@Override
public void validate() throws ValidationException {
	ValidatorFactory vf = ValidatorFactory.newInstance();
	
	  vf.getValidator("abilitato", abilitato).notNull();
	  if(this.abilitato.booleanValue()) {
		  vf.getValidator("url", this.url).notNull().minLength(1).pattern(CostantiValidazione.PATTERN_NO_WHITE_SPACES);
		  vf.getValidator("timeToLive", this.timeToLive).min(new BigDecimal(3600)).max(new BigDecimal(604800));
	  }
}
}



