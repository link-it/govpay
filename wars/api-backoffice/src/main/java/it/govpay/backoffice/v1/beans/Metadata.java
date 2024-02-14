package it.govpay.backoffice.v1.beans;


import java.util.List;
import java.util.Objects;

import com.fasterxml.jackson.annotation.JsonProperty;

import it.govpay.core.beans.JSONSerializable;
import it.govpay.core.exceptions.IOException;
import it.govpay.core.exceptions.ValidationException;
import it.govpay.core.utils.validator.IValidable;
import it.govpay.core.utils.validator.ValidatorFactory;

/**
 * Metadata Custom da inserire nella ricevuta di pagamento
 **/@com.fasterxml.jackson.annotation.JsonPropertyOrder({
"mapEntry",
})
public class Metadata extends JSONSerializable implements IValidable {
  
  @JsonProperty("mapEntry")
  private List<MapEntry> mapEntry = null;
  
  /**
   **/
  public Metadata mapEntry(List<MapEntry> mapEntry) {
    this.mapEntry = mapEntry;
    return this;
  }

  @JsonProperty("mapEntry")
  public List<MapEntry> getMapEntry() {
    return mapEntry;
  }
  public void setMapEntry(List<MapEntry> mapEntry) {
    this.mapEntry = mapEntry;
  }

  @Override
  public boolean equals(java.lang.Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    Metadata metadata = (Metadata) o;
    return Objects.equals(mapEntry, metadata.mapEntry);
  }

  @Override
  public int hashCode() {
    return Objects.hash(mapEntry);
  }

  public static Metadata parse(String json) throws IOException {
    return (Metadata) parse(json, Metadata.class);
  }

  @Override
  public String getJsonIdFilter() {
    return "metadata";
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class Metadata {\n");
    
    sb.append("    mapEntry: ").append(toIndentedString(mapEntry)).append("\n");
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
		
		vf.getValidator("mapEntry", this.mapEntry).notNull().minItems(1).maxItems(15).validateObjects();
  }
}



