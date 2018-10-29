package it.govpay.core.rs.v1.beans.pagamenti;

import java.math.BigDecimal;
import java.util.Date;
import java.util.Objects;

import org.openspcoop2.utils.json.ValidationException;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

import it.govpay.core.rs.v1.beans.base.TassonomiaAvviso;
@com.fasterxml.jackson.annotation.JsonPropertyOrder({
"idDominio",
"causale",
"importo",
"numeroAvviso",
"iuv",
"dataValidita",
"dataScadenza",
"tassonomiaAvviso",
"idA2A",
"idPendenza",
"stato"
})
public class PendenzaVerificata extends it.govpay.core.rs.v1.beans.JSONSerializable {
  
  @JsonProperty("idDominio")
  private String idDominio = null;
  
  @JsonProperty("causale")
  private String causale = null;
  
  @JsonProperty("importo")
  private BigDecimal importo = null;
  
  @JsonProperty("numeroAvviso")
  private String numeroAvviso = null;
  
  @JsonProperty("iuv")
  private String iuv = null;
  
  @JsonProperty("dataValidita")
  private Date dataValidita = null;
  
  @JsonProperty("dataScadenza")
  private Date dataScadenza = null;
  
  @JsonProperty("tassonomiaAvviso")
  private TassonomiaAvviso tassonomiaAvviso = null;
  
  @JsonProperty("idA2A")
  private String idA2A = null;
  
  @JsonProperty("idPendenza")
  private String idPendenza = null;
  
  @JsonProperty("stato")
  private StatoPendenzaVerificata stato = null;
  
  /**
   * Identificativo del dominio creditore
   **/
  public PendenzaVerificata idDominio(String idDominio) {
    this.idDominio = idDominio;
    return this;
  }

  @JsonProperty("idDominio")
  public String getIdDominio() {
    return this.idDominio;
  }
  public void setIdDominio(String idDominio) {
    this.idDominio = idDominio;
  }

  /**
   * Descrizione da inserire nell'avviso di pagamento
   **/
  public PendenzaVerificata causale(String causale) {
    this.causale = causale;
    return this;
  }

  @JsonProperty("causale")
  public String getCausale() {
    return this.causale;
  }
  public void setCausale(String causale) {
    this.causale = causale;
  }

  /**
   * Importo della pendenza. Deve corrispondere alla somma delle singole voci.
   **/
  public PendenzaVerificata importo(BigDecimal importo) {
    this.importo = importo;
    return this;
  }

  @JsonProperty("importo")
  public BigDecimal getImporto() {
    return this.importo;
  }
  public void setImporto(BigDecimal importo) {
    this.importo = importo;
  }

  /**
   * Identificativo univoco versamento, assegnato se pagabile da psp
   **/
  public PendenzaVerificata numeroAvviso(String numeroAvviso) {
    this.numeroAvviso = numeroAvviso;
    return this;
  }

  @JsonProperty("numeroAvviso")
  public String getNumeroAvviso() {
    return this.numeroAvviso;
  }
  public void setNumeroAvviso(String numeroAvviso) {
    this.numeroAvviso = numeroAvviso;
  }

  /**
   * Data di emissione della pendenza
   **/
  public PendenzaVerificata iuv(String iuv) {
    this.iuv = iuv;
    return this;
  }

  @JsonProperty("iuv")
  public String getIuv() {
    return this.iuv;
  }
  public void setIuv(String iuv) {
    this.iuv = iuv;
  }

  /**
   * Data di validita dei dati della pendenza, decorsa la quale la pendenza può subire variazioni.
   **/
  public PendenzaVerificata dataValidita(Date dataValidita) {
    this.dataValidita = dataValidita;
    return this;
  }

  @JsonProperty("dataValidita")
  public Date getDataValidita() {
    return this.dataValidita;
  }
  public void setDataValidita(Date dataValidita) {
    this.dataValidita = dataValidita;
  }

  /**
   * Data di scadenza della pendenza, decorsa la quale non è più pagabile.
   **/
  public PendenzaVerificata dataScadenza(Date dataScadenza) {
    this.dataScadenza = dataScadenza;
    return this;
  }

  @JsonProperty("dataScadenza")
  public Date getDataScadenza() {
    return this.dataScadenza;
  }
  public void setDataScadenza(Date dataScadenza) {
    this.dataScadenza = dataScadenza;
  }

  /**
   **/
  public PendenzaVerificata tassonomiaAvviso(TassonomiaAvviso tassonomiaAvviso) {
    this.tassonomiaAvviso = tassonomiaAvviso;
    return this;
  }

  @JsonIgnore
  public TassonomiaAvviso getTassonomiaAvvisoEnum() {
    return this.tassonomiaAvviso;
  }
  public void setTassonomiaAvviso(TassonomiaAvviso tassonomiaAvviso) {
    this.tassonomiaAvviso = tassonomiaAvviso;
  }
  
  @JsonProperty("tassonomiaAvviso")
  public String getTassonomiaAvviso() {
	  if(this.tassonomiaAvviso != null)
		  return this.tassonomiaAvviso.toString();
    return null;
  }
  public void setTassonomiaAvviso(String tassonomiaAvviso) {
	if(tassonomiaAvviso!= null)
		this.tassonomiaAvviso = TassonomiaAvviso.fromValue(tassonomiaAvviso);
  }

  /**
   * Identificativo del gestionale responsabile della pendenza
   **/
  public PendenzaVerificata idA2A(String idA2A) {
    this.idA2A = idA2A;
    return this;
  }

  @JsonProperty("idA2A")
  public String getIdA2A() {
    return this.idA2A;
  }
  public void setIdA2A(String idA2A) {
    this.idA2A = idA2A;
  }

  /**
   * Identificativo della pendenza nel gestionale responsabile
   **/
  public PendenzaVerificata idPendenza(String idPendenza) {
    this.idPendenza = idPendenza;
    return this;
  }

  @JsonProperty("idPendenza")
  public String getIdPendenza() {
    return this.idPendenza;
  }
  public void setIdPendenza(String idPendenza) {
    this.idPendenza = idPendenza;
  }

  /**
   **/
  public PendenzaVerificata stato(StatoPendenzaVerificata stato) {
    this.stato = stato;
    return this;
  }

  @JsonProperty("stato")
  public StatoPendenzaVerificata getStato() {
    return this.stato;
  }
  public void setStato(StatoPendenzaVerificata stato) {
    this.stato = stato;
  }

  @Override
  public boolean equals(java.lang.Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || this.getClass() != o.getClass()) {
      return false;
    }
    PendenzaVerificata pendenzaVerificata = (PendenzaVerificata) o;
    return Objects.equals(this.idDominio, pendenzaVerificata.idDominio) &&
        Objects.equals(this.causale, pendenzaVerificata.causale) &&
        Objects.equals(this.importo, pendenzaVerificata.importo) &&
        Objects.equals(this.numeroAvviso, pendenzaVerificata.numeroAvviso) &&
        Objects.equals(this.iuv, pendenzaVerificata.iuv) &&
        Objects.equals(this.dataValidita, pendenzaVerificata.dataValidita) &&
        Objects.equals(this.dataScadenza, pendenzaVerificata.dataScadenza) &&
        Objects.equals(this.tassonomiaAvviso, pendenzaVerificata.tassonomiaAvviso) &&
        Objects.equals(this.idA2A, pendenzaVerificata.idA2A) &&
        Objects.equals(this.idPendenza, pendenzaVerificata.idPendenza) &&
        Objects.equals(this.stato, pendenzaVerificata.stato); 
  }

  @Override
  public int hashCode() {
    return Objects.hash(this.idDominio, this.causale, this.importo, this.numeroAvviso, this.iuv, this.dataValidita, this.dataScadenza,this.tassonomiaAvviso, this.idA2A, this.idPendenza, this.stato);
  }

  public static PendenzaVerificata parse(String json) throws org.openspcoop2.generic_project.exception.ServiceException, ValidationException {
    return parse(json, PendenzaVerificata.class);
  }

  @Override
  public String getJsonIdFilter() {
    return "pendenzaVerificata";
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class PendenzaVerificata {\n");
    
    sb.append("    idDominio: ").append(this.toIndentedString(this.idDominio)).append("\n");
    sb.append("    causale: ").append(this.toIndentedString(this.causale)).append("\n");
    sb.append("    importo: ").append(this.toIndentedString(this.importo)).append("\n");
    sb.append("    numeroAvviso: ").append(this.toIndentedString(this.numeroAvviso)).append("\n");
    sb.append("    iuv: ").append(this.toIndentedString(this.iuv)).append("\n");
    sb.append("    dataValidita: ").append(this.toIndentedString(this.dataValidita)).append("\n");
    sb.append("    dataScadenza: ").append(this.toIndentedString(this.dataScadenza)).append("\n");
    sb.append("    tassonomiaAvviso: ").append(this.toIndentedString(this.tassonomiaAvviso)).append("\n");
    sb.append("    idA2A: ").append(this.toIndentedString(this.idA2A)).append("\n");
    sb.append("    idPendenza: ").append(this.toIndentedString(this.idPendenza)).append("\n");
    sb.append("    stato: ").append(this.toIndentedString(this.stato)).append("\n");
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



