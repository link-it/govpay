package it.govpay.pagamento.test;

import it.govpay.pagamento.v2.beans.Lista;
import it.govpay.pagamento.v2.beans.RppIndex;
import java.util.ArrayList;
import java.util.List;
import javax.validation.constraints.*;

import io.swagger.v3.oas.annotations.media.Schema;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlType;
import javax.xml.bind.annotation.XmlEnum;
import javax.xml.bind.annotation.XmlEnumValue;
import com.fasterxml.jackson.annotation.JsonProperty;

public class Rpps extends Lista {
  
  @Schema(description = "")
  private List<RppIndex> items = null;
 /**
   * Get items
   * @return items
  **/
  @JsonProperty("items")
  public List<RppIndex> getItems() {
    return items;
  }

  public void setItems(List<RppIndex> items) {
    this.items = items;
  }

  public Rpps items(List<RppIndex> items) {
    this.items = items;
    return this;
  }

  public Rpps addItemsItem(RppIndex itemsItem) {
    this.items.add(itemsItem);
    return this;
  }


  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class Rpps {\n");
    sb.append("    ").append(toIndentedString(super.toString())).append("\n");
    sb.append("    items: ").append(toIndentedString(items)).append("\n");
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
