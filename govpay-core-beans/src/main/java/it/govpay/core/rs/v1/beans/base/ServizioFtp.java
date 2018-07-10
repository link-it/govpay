package it.govpay.core.rs.v1.beans.base;

import java.util.Objects;

import org.openspcoop2.generic_project.exception.ServiceException;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

import it.govpay.core.rs.v1.beans.JSONSerializable;
@JsonPropertyOrder({
"ftp_lettura",
"ftp_scrittura",
})
public class ServizioFtp extends JSONSerializable {
  
  @JsonProperty("ftp_lettura")
  private ConnettoreFtp ftpLettura = null;
  
  @JsonProperty("ftp_scrittura")
  private ConnettoreFtp ftpScrittura = null;
  
  /**
   **/
  public ServizioFtp ftpLettura(ConnettoreFtp ftpLettura) {
    this.ftpLettura = ftpLettura;
    return this;
  }

  @JsonProperty("ftp_lettura")
  public ConnettoreFtp getFtpLettura() {
    return ftpLettura;
  }
  public void setFtpLettura(ConnettoreFtp ftpLettura) {
    this.ftpLettura = ftpLettura;
  }

  /**
   **/
  public ServizioFtp ftpScrittura(ConnettoreFtp ftpScrittura) {
    this.ftpScrittura = ftpScrittura;
    return this;
  }

  @JsonProperty("ftp_scrittura")
  public ConnettoreFtp getFtpScrittura() {
    return ftpScrittura;
  }
  public void setFtpScrittura(ConnettoreFtp ftpScrittura) {
    this.ftpScrittura = ftpScrittura;
  }

  @Override
  public boolean equals(java.lang.Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    ServizioFtp servizioFtp = (ServizioFtp) o;
    return Objects.equals(ftpLettura, servizioFtp.ftpLettura) &&
        Objects.equals(ftpScrittura, servizioFtp.ftpScrittura);
  }

  @Override
  public int hashCode() {
    return Objects.hash(ftpLettura, ftpScrittura);
  }

  public static ServizioFtp parse(String json) throws ServiceException {
    return (ServizioFtp) parse(json, ServizioFtp.class);
  }

  @Override
  public String getJsonIdFilter() {
    return "servizioFtp";
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class ServizioFtp {\n");
    
    sb.append("    ftpLettura: ").append(toIndentedString(ftpLettura)).append("\n");
    sb.append("    ftpScrittura: ").append(toIndentedString(ftpScrittura)).append("\n");
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



