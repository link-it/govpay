package it.govpay.ragioneria.v3.beans;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;

import io.swagger.v3.oas.annotations.media.Schema;

public class FlussoRendicontazione extends FlussoRendicontazioneIndex  {
  
  @Schema(description = "")
  private List<Rendicontazione> rendicontazioni = null;
 /**
   * Get rendicontazioni
   * @return rendicontazioni
  **/
  @JsonProperty("rendicontazioni")
  public List<Rendicontazione> getRendicontazioni() {
    return rendicontazioni;
  }

  public void setRendicontazioni(List<Rendicontazione> rendicontazioni) {
    this.rendicontazioni = rendicontazioni;
  }

  public FlussoRendicontazione rendicontazioni(List<Rendicontazione> rendicontazioni) {
    this.rendicontazioni = rendicontazioni;
    return this;
  }

  public FlussoRendicontazione addRendicontazioniItem(Rendicontazione rendicontazioniItem) {
    this.rendicontazioni.add(rendicontazioniItem);
    return this;
  }


  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class FlussoRendicontazione {\n");
    sb.append("    ").append(toIndentedString(super.toString())).append("\n");
    sb.append("    rendicontazioni: ").append(toIndentedString(rendicontazioni)).append("\n");
    sb.append("}");
    return sb.toString();
  }

  /**
   * Convert the given object to string with each line indented by 4 spaces
   * (except the first line).
   */
  private static String toIndentedString(java.lang.Object o) {
    if (o == null) {
      return "null";
    }
    return o.toString().replace("\n", "\n    ");
  }
}
