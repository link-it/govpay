package it.govpay.rs.v1.beans.base;

import java.util.Objects;
import org.codehaus.jackson.annotate.JsonProperty;
import org.codehaus.jackson.annotate.JsonValue;
import it.govpay.rs.v1.beans.base.Ruolo;
import java.util.ArrayList;
import java.util.List;
@org.codehaus.jackson.annotate.JsonPropertyOrder({
"id",
"vociMenu",
"ruoli",
})
@javax.annotation.Generated(value = "io.swagger.codegen.languages.java.JavaInflectorServerCodegen", date = "2018-02-05T15:39:23.431+01:00")
public abstract class Profilo extends it.govpay.rs.v1.beans.JSONSerializable {
  
  @JsonProperty("id")
  private String id = null;
  
    
      /**
   * Gets or Sets vociMenu
   */
  public enum VociMenuEnum {
    
    
        
            
    LETTURA("Lettura"),
    
            
    SCRITTURA("Scrittura"),
    
            
    ESECUZIONE("Esecuzione");
            
        
    

    private String value;

    VociMenuEnum(String value) {
      this.value = value;
    }

    @Override
    @JsonValue
    public String toString() {
      return String.valueOf(value);
    }

    public static VociMenuEnum fromValue(String text) {
      for (VociMenuEnum b : VociMenuEnum.values()) {
        if (String.valueOf(b.value).equals(text)) {
          return b;
        }
      }
      return null;
    }
  }

      
  @JsonProperty("vociMenu")
  private List<VociMenuEnum> vociMenu = null;
  
  @JsonProperty("ruoli")
  private List<Ruolo> ruoli = null;
  
  /**
   * Identificativo del profilo dell'operatore di backoffice
   **/
  public Profilo id(String id) {
    this.id = id;
    return this;
  }

  @JsonProperty("id")
  public String getId() {
    return id;
  }
  public void setId(String id) {
    this.id = id;
  }

  /**
   **/
  public Profilo vociMenu(List<VociMenuEnum> vociMenu) {
    this.vociMenu = vociMenu;
    return this;
  }

  @JsonProperty("vociMenu")
  public List<VociMenuEnum> getVociMenu() {
    return vociMenu;
  }
  public void setVociMenu(List<VociMenuEnum> vociMenu) {
    this.vociMenu = vociMenu;
  }

  /**
   **/
  public Profilo ruoli(List<Ruolo> ruoli) {
    this.ruoli = ruoli;
    return this;
  }

  @JsonProperty("ruoli")
  public List<Ruolo> getRuoli() {
    return ruoli;
  }
  public void setRuoli(List<Ruolo> ruoli) {
    this.ruoli = ruoli;
  }

  @Override
  public boolean equals(java.lang.Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    Profilo profilo = (Profilo) o;
    return Objects.equals(id, profilo.id) &&
        Objects.equals(vociMenu, profilo.vociMenu) &&
        Objects.equals(ruoli, profilo.ruoli);
  }

  @Override
  public int hashCode() {
    return Objects.hash(id, vociMenu, ruoli);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class Profilo {\n");
    
    sb.append("    id: ").append(toIndentedString(id)).append("\n");
    sb.append("    vociMenu: ").append(toIndentedString(vociMenu)).append("\n");
    sb.append("    ruoli: ").append(toIndentedString(ruoli)).append("\n");
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



