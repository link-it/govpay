package it.govpay.pagamento.test;

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

public class Lista  {
  
  @Schema(required = true, description = "")
  private Integer limit = null;
  
  @Schema(required = true, description = "")
  private Integer offset = null;
  
  @Schema(description = "Number of items matching the filter criteria")
 /**
   * Number of items matching the filter criteria  
  **/
  private Long total = null;
  
  @Schema(description = "Relative link to first result page. Null if you are already on first page.")
 /**
   * Relative link to first result page. Null if you are already on first page.  
  **/
  private String first = null;
  
  @Schema(description = "Relative link to next result page. Null if you are on last page.")
 /**
   * Relative link to next result page. Null if you are on last page.  
  **/
  private String next = null;
  
  @Schema(description = "Relative link to previous result page. Null if you are on first page.")
 /**
   * Relative link to previous result page. Null if you are on first page.  
  **/
  private String prev = null;
  
  @Schema(description = "Relative link to last result page. Null if you are already on last page.")
 /**
   * Relative link to last result page. Null if you are already on last page.  
  **/
  private String last = null;
 /**
   * Get limit
   * @return limit
  **/
  @JsonProperty("limit")
  @NotNull
  public Integer getLimit() {
    return limit;
  }

  public void setLimit(Integer limit) {
    this.limit = limit;
  }

  public Lista limit(Integer limit) {
    this.limit = limit;
    return this;
  }

 /**
   * Get offset
   * @return offset
  **/
  @JsonProperty("offset")
  @NotNull
  public Integer getOffset() {
    return offset;
  }

  public void setOffset(Integer offset) {
    this.offset = offset;
  }

  public Lista offset(Integer offset) {
    this.offset = offset;
    return this;
  }

 /**
   * Number of items matching the filter criteria
   * minimum: 0
   * @return total
  **/
  @JsonProperty("total")
 @Min(0L)  public Long getTotal() {
    return total;
  }

  public void setTotal(Long total) {
    this.total = total;
  }

  public Lista total(Long total) {
    this.total = total;
    return this;
  }

 /**
   * Relative link to first result page. Null if you are already on first page.
   * @return first
  **/
  @JsonProperty("first")
  public String getFirst() {
    return first;
  }

  public void setFirst(String first) {
    this.first = first;
  }

  public Lista first(String first) {
    this.first = first;
    return this;
  }

 /**
   * Relative link to next result page. Null if you are on last page.
   * @return next
  **/
  @JsonProperty("next")
  public String getNext() {
    return next;
  }

  public void setNext(String next) {
    this.next = next;
  }

  public Lista next(String next) {
    this.next = next;
    return this;
  }

 /**
   * Relative link to previous result page. Null if you are on first page.
   * @return prev
  **/
  @JsonProperty("prev")
  public String getPrev() {
    return prev;
  }

  public void setPrev(String prev) {
    this.prev = prev;
  }

  public Lista prev(String prev) {
    this.prev = prev;
    return this;
  }

 /**
   * Relative link to last result page. Null if you are already on last page.
   * @return last
  **/
  @JsonProperty("last")
  public String getLast() {
    return last;
  }

  public void setLast(String last) {
    this.last = last;
  }

  public Lista last(String last) {
    this.last = last;
    return this;
  }


  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class Lista {\n");
    
    sb.append("    limit: ").append(toIndentedString(limit)).append("\n");
    sb.append("    offset: ").append(toIndentedString(offset)).append("\n");
    sb.append("    total: ").append(toIndentedString(total)).append("\n");
    sb.append("    first: ").append(toIndentedString(first)).append("\n");
    sb.append("    next: ").append(toIndentedString(next)).append("\n");
    sb.append("    prev: ").append(toIndentedString(prev)).append("\n");
    sb.append("    last: ").append(toIndentedString(last)).append("\n");
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
