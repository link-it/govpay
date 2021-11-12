package it.govpay.ec.v2.beans;

import java.math.BigDecimal;

import javax.validation.constraints.DecimalMin;
import javax.validation.constraints.NotNull;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

import io.swagger.v3.oas.annotations.media.Schema;

public class VincoloPagamento   {
  
  @Schema(example = "5", required = true, description = "numero di giorni vincolo per il pagamento")
 /**
   * numero di giorni vincolo per il pagamento  
  **/
  private BigDecimal giorni = null;
  
  @JsonIgnore
  private TipoSogliaVincoloPagamento tipoEnum = null;
  
  @Schema(required = true, description = "")
  private String tipo = null;
 /**
   * numero di giorni vincolo per il pagamento
   * minimum: 1
   * @return giorni
  **/
  @JsonProperty("giorni")
  @NotNull
 @DecimalMin("1")  public BigDecimal getGiorni() {
    return giorni;
  }

  public void setGiorni(BigDecimal giorni) {
    this.giorni = giorni;
  }

  public VincoloPagamento giorni(BigDecimal giorni) {
    this.giorni = giorni;
    return this;
  }

 /**
   * Get tipo
   * @return tipo
  **/
  @JsonIgnore
  public TipoSogliaVincoloPagamento getTipoEnum() {
    return tipoEnum;
  }

  public void setTipoEnum(TipoSogliaVincoloPagamento tipoEnum) {
    this.tipoEnum = tipoEnum;
  }

  public VincoloPagamento tipoEnum(TipoSogliaVincoloPagamento tipoEnum) {
    this.tipoEnum = tipoEnum;
    return this;
  }
  
  /**
   * Get tipo
   * @return tipo
  **/
  @JsonProperty("tipo")
  @NotNull
  public String getTipo() {
    return tipo;
  }

  public void setTipo(String tipo) {
    this.tipo = tipo;
  }

  public VincoloPagamento tipo(String tipo) {
    this.tipo = tipo;
    return this;
  }


  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class VincoloPagamento {\n");
    
    sb.append("    giorni: ").append(toIndentedString(giorni)).append("\n");
    sb.append("    tipo: ").append(toIndentedString(tipo)).append("\n");
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
