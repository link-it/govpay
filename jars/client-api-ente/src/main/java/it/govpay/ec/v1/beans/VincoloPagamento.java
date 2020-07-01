package it.govpay.ec.v1.beans;

import java.math.BigDecimal;
import java.util.Objects;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

@com.fasterxml.jackson.annotation.JsonPropertyOrder({
"giorni",
"tipo",
})
public class VincoloPagamento {
  
  @JsonProperty("giorni")
  private BigDecimal giorni = null;
  
  @JsonIgnore
  private TipoSogliaVincoloPagamento tipoEnum = null;
  
  @JsonProperty("tipo")
  private String tipo = null;
  
  /**
   * numero di giorni vincolo per il pagamento
   **/
  public VincoloPagamento giorni(BigDecimal giorni) {
    this.giorni = giorni;
    return this;
  }

  @JsonProperty("giorni")
  public BigDecimal getGiorni() {
    return giorni;
  }
  public void setGiorni(BigDecimal giorni) {
    this.giorni = giorni;
  }

  /**
   **/
  public VincoloPagamento tipoEnum(TipoSogliaVincoloPagamento tipo) {
    this.tipoEnum = tipo;
    return this;
  }

  public TipoSogliaVincoloPagamento getTipoEnum() {
    return tipoEnum;
  }
  public void setTipoEnum(TipoSogliaVincoloPagamento tipo) {
    this.tipoEnum = tipo;
  }
  
  /**
   **/
  public VincoloPagamento tipo(String tipo) {
    this.tipo = tipo;
    return this;
  }

  @JsonProperty("tipo")
  public String getTipo() {
    return tipo;
  }
  public void setTipo(String tipo) {
    this.tipo = tipo;
  }

  @Override
  public boolean equals(java.lang.Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    VincoloPagamento vincoloPagamento = (VincoloPagamento) o;
    return Objects.equals(giorni, vincoloPagamento.giorni) &&
        Objects.equals(tipo, vincoloPagamento.tipo);
  }

  @Override
  public int hashCode() {
    return Objects.hash(giorni, tipo);
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
  private String toIndentedString(java.lang.Object o) {
    if (o == null) {
      return "null";
    }
    return o.toString().replace("\n", "\n    ");
  }
  
}


