package it.govpay.core.rs.v1.beans.base;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import org.openspcoop2.utils.json.ValidationException;

import com.fasterxml.jackson.annotation.JsonProperty;

import it.govpay.core.rs.v1.beans.JSONSerializable;
@com.fasterxml.jackson.annotation.JsonPropertyOrder({
"acl",
})
public class RuoloPost extends JSONSerializable {
  
  @JsonProperty("acl")
  private List<AclPost> acl = new ArrayList<AclPost>();
  
  /**
   **/
  public RuoloPost acl(List<AclPost> acl) {
    this.acl = acl;
    return this;
  }

  @JsonProperty("acl")
  public List<AclPost> getAcl() {
    return acl;
  }
  public void setAcl(List<AclPost> acl) {
    this.acl = acl;
  }

  @Override
  public boolean equals(java.lang.Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    RuoloPost ruoloPost = (RuoloPost) o;
    return Objects.equals(acl, ruoloPost.acl);
  }

  @Override
  public int hashCode() {
    return Objects.hash(acl);
  }

  public static RuoloPost parse(String json) throws org.openspcoop2.generic_project.exception.ServiceException, ValidationException {
    return (RuoloPost) parse(json, RuoloPost.class);
  }

  @Override
  public String getJsonIdFilter() {
    return "ruoloPost";
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class RuoloPost {\n");
    
    sb.append("    acl: ").append(toIndentedString(acl)).append("\n");
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



