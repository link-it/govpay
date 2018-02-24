package it.govpay.rs.v1.beans.base;

import java.util.Objects;
public class EntrataACL extends it.govpay.rs.v1.beans.JSONSerializable {
  
  private String idEntrata = null;
  private String descrizione = null;

  public EntrataACL idEntrata(String idEntrata) {
    this.idEntrata = idEntrata;
    return this;
  }

  public String getIdEntrata() {
    return idEntrata;
  }
  public void setIdEntrata(String idEntrata) {
    this.idEntrata = idEntrata;
  }

  public EntrataACL descrizione(String descrizione) {
    this.descrizione = descrizione;
    return this;
  }

  public String getDescrizione() {
    return descrizione;
  }
  public void setDescrizione(String descrizione) {
    this.descrizione = descrizione;
  }

  @Override
  public boolean equals(java.lang.Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    EntrataACL entrataACL = (EntrataACL) o;
    return Objects.equals(idEntrata, entrataACL.idEntrata) &&
        Objects.equals(descrizione, entrataACL.descrizione);
  }

  @Override
  public int hashCode() {
    return Objects.hash(idEntrata, descrizione);
  }

  public static EntrataACL parse(String json) {
    return (EntrataACL) parse(json, EntrataACL.class);
  }

  @Override
  public String getJsonIdFilter() {
    return "entrataACL";
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class EntrataACL {\n");
    
    sb.append("    idEntrata: ").append(toIndentedString(idEntrata)).append("\n");
    sb.append("    descrizione: ").append(toIndentedString(descrizione)).append("\n");
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



