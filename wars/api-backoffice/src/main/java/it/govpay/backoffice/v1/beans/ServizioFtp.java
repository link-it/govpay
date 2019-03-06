package it.govpay.backoffice.v1.beans;

import java.util.Objects;

import org.openspcoop2.generic_project.exception.ServiceException;
import org.openspcoop2.utils.json.ValidationException;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

import it.govpay.core.beans.JSONSerializable;
import it.govpay.core.utils.validator.IValidable;
import it.govpay.core.utils.validator.ValidatorFactory;
@JsonPropertyOrder({
"ftp_lettura",
"ftp_scrittura",
})
public class ServizioFtp extends JSONSerializable implements IValidable{
  
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
    return this.ftpLettura;
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
    return this.ftpScrittura;
  }
  public void setFtpScrittura(ConnettoreFtp ftpScrittura) {
    this.ftpScrittura = ftpScrittura;
  }

  @Override
  public boolean equals(java.lang.Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || this.getClass() != o.getClass()) {
      return false;
    }
    ServizioFtp servizioFtp = (ServizioFtp) o;
    return Objects.equals(this.ftpLettura, servizioFtp.ftpLettura) &&
        Objects.equals(this.ftpScrittura, servizioFtp.ftpScrittura);
  }

  @Override
  public int hashCode() {
    return Objects.hash(this.ftpLettura, this.ftpScrittura);
  }

  public static ServizioFtp parse(String json) throws ServiceException, ValidationException {
    return parse(json, ServizioFtp.class);
  }

  @Override
  public String getJsonIdFilter() {
    return "servizioFtp";
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class ServizioFtp {\n");
    
    sb.append("    ftpLettura: ").append(this.toIndentedString(this.ftpLettura)).append("\n");
    sb.append("    ftpScrittura: ").append(this.toIndentedString(this.ftpScrittura)).append("\n");
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
  
  @Override
	public void validate() throws ValidationException {
		ValidatorFactory vf = ValidatorFactory.newInstance();
		vf.getValidator("ftpLettura", this.ftpLettura).validateFields();
		vf.getValidator("ftpScrittura", this.ftpScrittura).validateFields();
	}
}



