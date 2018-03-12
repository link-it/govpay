package it.govpay.core.rs.v1.beans.base;

import java.util.Objects;
public class DominioACL extends it.govpay.core.rs.v1.beans.JSONSerializable {
  
  private String idDominio = null;
  private String ragioneSociale = null;

  public DominioACL idDominio(String idDominio) {
    this.idDominio = idDominio;
    return this;
  }

  public String getIdDominio() {
    return idDominio;
  }
  public void setIdDominio(String idDominio) {
    this.idDominio = idDominio;
  }

  public DominioACL ragioneSociale(String ragioneSociale) {
    this.ragioneSociale = ragioneSociale;
    return this;
  }

  public String getRagioneSociale() {
    return ragioneSociale;
  }
  public void setRagioneSociale(String ragioneSociale) {
    this.ragioneSociale = ragioneSociale;
  }

  @Override
  public boolean equals(java.lang.Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    DominioACL dominioACL = (DominioACL) o;
    return Objects.equals(idDominio, dominioACL.idDominio) &&
        Objects.equals(ragioneSociale, dominioACL.ragioneSociale);
  }

  @Override
  public int hashCode() {
    return Objects.hash(idDominio, ragioneSociale);
  }

  public static DominioACL parse(String json) {
    return (DominioACL) parse(json, DominioACL.class);
  }

  @Override
  public String getJsonIdFilter() {
    return "dominioACL";
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class DominioACL {\n");
    
    sb.append("    idDominio: ").append(toIndentedString(idDominio)).append("\n");
    sb.append("    ragioneSociale: ").append(toIndentedString(ragioneSociale)).append("\n");
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



