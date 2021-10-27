package it.govpay.ragioneria.v3.beans;

import java.net.URI;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;

import io.swagger.v3.oas.annotations.media.Schema;

public class Riconciliazioni extends Lista  {

	public Riconciliazioni() {
		super();
	}
	
	public Riconciliazioni(URI requestUri, Long count, Integer pagina, Integer limit) {
		super(requestUri, count, pagina, limit);
	}
  
  @Schema(description = "")
  private List<RiconciliazioneIndex> risultati = null;
 /**
   * Get risultati
   * @return risultati
  **/
  @JsonProperty("risultati")
  public List<RiconciliazioneIndex> getRisultati() {
    return risultati;
  }

  public void setRisultati(List<RiconciliazioneIndex> risultati) {
    this.risultati = risultati;
  }

  public Riconciliazioni risultati(List<RiconciliazioneIndex> risultati) {
    this.risultati = risultati;
    return this;
  }

  public Riconciliazioni addRisultatiItem(RiconciliazioneIndex risultatiItem) {
    this.risultati.add(risultatiItem);
    return this;
  }


  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class Riconciliazioni {\n");
    sb.append("    ").append(toIndentedString(super.toString())).append("\n");
    sb.append("    risultati: ").append(toIndentedString(risultati)).append("\n");
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
