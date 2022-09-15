package it.govpay.backoffice.v1.beans;


import java.util.Objects;

import com.fasterxml.jackson.annotation.JsonProperty;

import it.govpay.core.beans.JSONSerializable;
import it.govpay.core.exceptions.IOException;
import it.govpay.core.exceptions.ValidationException;
import it.govpay.core.utils.validator.CostantiValidazione;
import it.govpay.core.utils.validator.IValidable;
import it.govpay.core.utils.validator.ValidatorFactory;
@com.fasterxml.jackson.annotation.JsonPropertyOrder({
"name",
"value",
})
public class Header extends JSONSerializable implements IValidable {
  
  @JsonProperty("name")
  private String name = null;
  
  @JsonProperty("value")
  private String value = null;
  
  /**
   **/
  public Header name(String name) {
    this.name = name;
    return this;
  }

  @JsonProperty("name")
  public String getName() {
    return name;
  }
  public void setName(String name) {
    this.name = name;
  }

  /**
   **/
  public Header value(String value) {
    this.value = value;
    return this;
  }

  @JsonProperty("value")
  public String getValue() {
    return value;
  }
  public void setValue(String value) {
    this.value = value;
  }

  @Override
  public boolean equals(java.lang.Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    Header header = (Header) o;
    return Objects.equals(name, header.name) &&
        Objects.equals(value, header.value);
  }

  @Override
  public int hashCode() {
    return Objects.hash(name, value);
  }

  public static Header parse(String json) throws IOException {
    return (Header) parse(json, Header.class);
  }

  @Override
  public String getJsonIdFilter() {
    return "header";
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class Header {\n");
    
    sb.append("    name: ").append(toIndentedString(name)).append("\n");
    sb.append("    value: ").append(toIndentedString(value)).append("\n");
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
	  vf.getValidator("name", this.name).notNull().minLength(1).maxLength(255).pattern(CostantiValidazione.PATTERN_NOME_HEADER);
	  vf.getValidator("value", this.value).notNull().minLength(1);
  }
}



