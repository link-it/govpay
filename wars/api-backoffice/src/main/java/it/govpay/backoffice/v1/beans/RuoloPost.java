package it.govpay.backoffice.v1.beans;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import com.fasterxml.jackson.annotation.JsonProperty;

import it.govpay.core.beans.JSONSerializable;
import it.govpay.core.exceptions.IOException;
import it.govpay.core.exceptions.ValidationException;
import it.govpay.core.utils.validator.IValidable;
import it.govpay.core.utils.validator.ValidatorFactory;
@com.fasterxml.jackson.annotation.JsonPropertyOrder({
"acl",
})
public class RuoloPost extends JSONSerializable  implements IValidable {

  @JsonProperty("acl")
  private List<AclPost> acl = new ArrayList<>();

  /**
   **/
  public RuoloPost acl(List<AclPost> acl) {
    this.acl = acl;
    return this;
  }

  @JsonProperty("acl")
  public List<AclPost> getAcl() {
    return this.acl;
  }
  public void setAcl(List<AclPost> acl) {
    this.acl = acl;
  }

  @Override
  public boolean equals(java.lang.Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || this.getClass() != o.getClass()) {
      return false;
    }
    RuoloPost ruoloPost = (RuoloPost) o;
    return Objects.equals(this.acl, ruoloPost.acl);
  }

  @Override
  public int hashCode() {
    return Objects.hash(this.acl);
  }

  public static RuoloPost parse(String json) throws IOException {
    return parse(json, RuoloPost.class);
  }

  @Override
  public String getJsonIdFilter() {
    return "ruoloPost";
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class RuoloPost {\n");

    sb.append("    acl: ").append(this.toIndentedString(this.acl)).append("\n");
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
		vf.getValidator("acl", this.acl).notEmpty().minItems(1).validateObjects();
	}
}



