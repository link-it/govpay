package it.govpay.backoffice.v1.beans;

import java.util.Objects;

import org.openspcoop2.generic_project.exception.ServiceException;
import org.openspcoop2.utils.json.ValidationException;

import com.fasterxml.jackson.annotation.JsonProperty;

import it.govpay.core.beans.JSONSerializable;
import it.govpay.core.utils.validator.IValidable;
import it.govpay.core.utils.validator.ValidatorFactory;
@com.fasterxml.jackson.annotation.JsonPropertyOrder({
"abilitato",
"mailserver",
})
public class MailBatch extends JSONSerializable implements IValidable{
  
  @JsonProperty("abilitato")
  private Boolean abilitato = null;
  
  @JsonProperty("mailserver")
  private Mailserver mailserver = null;
  
  /**
   * Indica lo stato di abilitazione
   **/
  public MailBatch abilitato(Boolean abilitato) {
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
   **/
  public MailBatch mailserver(Mailserver mailserver) {
    this.mailserver = mailserver;
    return this;
  }

  @JsonProperty("mailserver")
  public Mailserver getMailserver() {
    return mailserver;
  }
  public void setMailserver(Mailserver mailserver) {
    this.mailserver = mailserver;
  }

  @Override
  public boolean equals(java.lang.Object o) { 
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    MailBatch mailBatch = (MailBatch) o;
    return Objects.equals(abilitato, mailBatch.abilitato) &&
        Objects.equals(mailserver, mailBatch.mailserver);
  }

  @Override
  public int hashCode() {
    return Objects.hash(abilitato, mailserver);
  }

  public static MailBatch parse(String json) throws ServiceException, ValidationException { 
    return (MailBatch) parse(json, MailBatch.class);
  }

  @Override
  public String getJsonIdFilter() {
    return "mailBatch";
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class MailBatch {\n");
    
    sb.append("    abilitato: ").append(toIndentedString(abilitato)).append("\n");
    sb.append("    mailserver: ").append(toIndentedString(mailserver)).append("\n");
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
		vf.getValidator("mailserver", this.mailserver).notNull().validateFields();
	}
  }
}



