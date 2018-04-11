package it.govpay.core.rs.v1.beans.base;

import java.util.Objects;

import org.codehaus.jackson.annotate.JsonProperty;
@org.codehaus.jackson.annotate.JsonPropertyOrder({
"nome",
"location",
})
public class OperazioneIndex extends it.govpay.core.rs.v1.beans.JSONSerializable {
  
    
  @JsonProperty("nome")
  private String nome = null;
  
  @JsonProperty("location")
  private String location = null;
  
  /**
   * Location di errore
   **/
  public OperazioneIndex nome(String nome) {
    this.nome = nome;
    return this;
  }

  @JsonProperty("nome")
  public String getNome() {
    return nome;
  }
  public void setNome(String nome) {
    this.nome = nome;
  }

  /**
   * Location di errore
   **/
  public OperazioneIndex location(String location) {
    this.location = location;
    return this;
  }

  @JsonProperty("location")
  public String getLocation() {
    return location;
  }
  public void setLocation(String location) {
    this.location = location;
  }

  @Override
  public boolean equals(java.lang.Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    OperazioneIndex faultBean = (OperazioneIndex) o;
    return Objects.equals(nome, faultBean.nome) &&
        Objects.equals(location, faultBean.location) ;
  }

  @Override
  public int hashCode() {
    return Objects.hash(nome, location);
  }

  public static OperazioneIndex parse(String json) {
    return (OperazioneIndex) parse(json, OperazioneIndex.class);
  }

  @Override
  public String getJsonIdFilter() {
    return "faultBean";
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class Operazione {\n");
    
    sb.append("    nome: ").append(toIndentedString(nome)).append("\n");
    sb.append("    location: ").append(toIndentedString(location)).append("\n");
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



