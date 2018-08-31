package it.govpay.core.rs.v1.beans.base;

import java.util.List;
import java.util.Objects;

import org.openspcoop2.generic_project.exception.ServiceException;
import org.openspcoop2.utils.json.ValidationException;

import com.fasterxml.jackson.annotation.JsonProperty;

import it.govpay.core.rs.v1.beans.JSONSerializable;
@com.fasterxml.jackson.annotation.JsonPropertyOrder({
"idTracciato",
"inserimenti",
"annullamenti",
})
public class TracciatoPendenzePost extends JSONSerializable {
  
  @JsonProperty("idTracciato")
  private String idTracciato = null;
  
  @JsonProperty("inserimenti")
  private List<PendenzaPost> inserimenti = null;
  
  @JsonProperty("annullamenti")
  private List<AnnullamentoPendenza> annullamenti = null;
  
  /**
   * Identificativo del tracciato
   **/
  public TracciatoPendenzePost idTracciato(String idTracciato) {
    this.idTracciato = idTracciato;
    return this;
  }

  @JsonProperty("idTracciato")
  public String getIdTracciato() {
    return idTracciato;
  }
  public void setIdTracciato(String idTracciato) {
    this.idTracciato = idTracciato;
  }

  /**
   **/
  public TracciatoPendenzePost inserimenti(List<PendenzaPost> inserimenti) {
    this.inserimenti = inserimenti;
    return this;
  }

  @JsonProperty("inserimenti")
  public List<PendenzaPost> getInserimenti() {
    return inserimenti;
  }
  public void setInserimenti(List<PendenzaPost> inserimenti) {
    this.inserimenti = inserimenti;
  }

  /**
   **/
  public TracciatoPendenzePost annullamenti(List<AnnullamentoPendenza> annullamenti) {
    this.annullamenti = annullamenti;
    return this;
  }

  @JsonProperty("annullamenti")
  public List<AnnullamentoPendenza> getAnnullamenti() {
    return annullamenti;
  }
  public void setAnnullamenti(List<AnnullamentoPendenza> annullamenti) {
    this.annullamenti = annullamenti;
  }

  @Override
  public boolean equals(java.lang.Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    TracciatoPendenzePost tracciatoPendenzePost = (TracciatoPendenzePost) o;
    return Objects.equals(idTracciato, tracciatoPendenzePost.idTracciato) &&
        Objects.equals(inserimenti, tracciatoPendenzePost.inserimenti) &&
        Objects.equals(annullamenti, tracciatoPendenzePost.annullamenti);
  }

  @Override
  public int hashCode() {
    return Objects.hash(idTracciato, inserimenti, annullamenti);
  }

  public static TracciatoPendenzePost parse(String json) throws ServiceException, ValidationException {
    return (TracciatoPendenzePost) parse(json, TracciatoPendenzePost.class);
  }

  @Override
  public String getJsonIdFilter() {
    return "tracciatoPendenzePost";
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class TracciatoPendenzePost {\n");
    
    sb.append("    idTracciato: ").append(toIndentedString(idTracciato)).append("\n");
    sb.append("    inserimenti: ").append(toIndentedString(inserimenti)).append("\n");
    sb.append("    annullamenti: ").append(toIndentedString(annullamenti)).append("\n");
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



