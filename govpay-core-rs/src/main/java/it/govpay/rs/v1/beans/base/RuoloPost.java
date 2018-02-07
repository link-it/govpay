package it.govpay.rs.v1.beans.base;

import java.util.Objects;
import org.codehaus.jackson.annotate.JsonProperty;
import java.util.ArrayList;
import java.util.List;
@org.codehaus.jackson.annotate.JsonPropertyOrder({
"acls",
})
public class RuoloPost extends it.govpay.rs.v1.beans.JSONSerializable {
  
  @JsonProperty("acls")
  private List<Object> acls = new ArrayList<Object>();
  
  /**
   **/
  public RuoloPost acls(List<Object> acls) {
    this.acls = acls;
    return this;
  }

  @JsonProperty("acls")
  public List<Object> getAcls() {
    return acls;
  }
  public void setAcls(List<Object> acls) {
    this.acls = acls;
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
    return Objects.equals(acls, ruoloPost.acls);
  }

  @Override
  public int hashCode() {
    return Objects.hash(acls);
  }

  public static RuoloPost parse(String json) {
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
    
    sb.append("    acls: ").append(toIndentedString(acls)).append("\n");
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



