package it.govpay.ec.v1.beans;

import java.math.BigDecimal;
import java.util.Date;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonValue;

//import io.swagger.v3.oas.annotations.media.Schema;

public class Riscossione  {
  
  // @Schema(example = "1.23456789E+9", required = true, description = "Identificativo ente creditore")
 /**
   * Identificativo ente creditore  
  **/
  private String idDominio = null;
  
  // @Schema(example = "RF23567483937849450550875", required = true, description = "Identificativo univoco di versamento")
 /**
   * Identificativo univoco di versamento  
  **/
  private String iuv = null;
  
  // @Schema(example = "1234acdc", required = true, description = "Identificativo univoco di riscossione.")
 /**
   * Identificativo univoco di riscossione.  
  **/
  private String iur = null;
  
  // @Schema(example = "1", required = true, description = "indice posizionale della voce pendenza riscossa")
 /**
   * indice posizionale della voce pendenza riscossa  
  **/
  private BigDecimal indice = null;
  
  // @Schema(required = true, description = "location dove reperire il dettaglio della pendenza")
 /**
   * location dove reperire il dettaglio della pendenza  
  **/
  private String pendenza = null;
  
  // @Schema(example = "abcdef12345_1", required = true, description = "Identificativo della voce di pedenza,interno alla pendenza, nel gestionale proprietario a cui si riferisce la riscossione")
 /**
   * Identificativo della voce di pedenza,interno alla pendenza, nel gestionale proprietario a cui si riferisce la riscossione  
  **/
  private String idVocePendenza = null;
  
  // @Schema(description = "location dove reperire la richiesta di pagamento che ha determinato la ricossione")
 /**
   * location dove reperire la richiesta di pagamento che ha determinato la ricossione  
  **/
  private String rpp = null;
  public enum StatoEnum {
    RISCOSSA("RISCOSSA"),
    INCASSATA("INCASSATA");

    private String value;

    StatoEnum(String value) {
      this.value = value;
    }
    @JsonValue
    public String getValue() {
      return value;
    }

    @Override
    public String toString() {
      return String.valueOf(value);
    }
    @JsonCreator
    public static StatoEnum fromValue(String text) {
      for (StatoEnum b : StatoEnum.values()) {
        if (String.valueOf(b.value).equals(text)) {
          return b;
        }
      }
      return null;
    }
  }  
  // @Schema(required = true, description = "")
  private StatoEnum stato = null;
  
  // @Schema(required = true, description = "")
  private TipoRiscossione tipo = null;
  
  // @Schema(example = "10.01", required = true, description = "Importo riscosso. ")
 /**
   * Importo riscosso.   
  **/
  private BigDecimal importo = null;
  
  // @Schema(required = true, description = "Data di esecuzione della riscossione")
 /**
   * Data di esecuzione della riscossione  
  **/
  private Date data = null;
  
  // @Schema(example = "1.5", description = "Importo delle commissioni applicate al pagamento dal PSP")
 /**
   * Importo delle commissioni applicate al pagamento dal PSP  
  **/
  private BigDecimal commissioni = null;
  
  // @Schema(description = "")
  private Allegato allegato = null;
 /**
   * Identificativo ente creditore
   * @return idDominio
  **/
  @JsonProperty("idDominio")
  @NotNull
  @Valid
  public String getIdDominio() {
    return idDominio;
  }

  public void setIdDominio(String idDominio) {
    this.idDominio = idDominio;
  }

  public Riscossione idDominio(String idDominio) {
    this.idDominio = idDominio;
    return this;
  }

 /**
   * Identificativo univoco di versamento
   * @return iuv
  **/
  @JsonProperty("iuv")
  @NotNull
  @Valid
  public String getIuv() {
    return iuv;
  }

  public void setIuv(String iuv) {
    this.iuv = iuv;
  }

  public Riscossione iuv(String iuv) {
    this.iuv = iuv;
    return this;
  }

 /**
   * Identificativo univoco di riscossione.
   * @return iur
  **/
  @JsonProperty("iur")
  @NotNull
  @Valid
  public String getIur() {
    return iur;
  }

  public void setIur(String iur) {
    this.iur = iur;
  }

  public Riscossione iur(String iur) {
    this.iur = iur;
    return this;
  }

 /**
   * indice posizionale della voce pendenza riscossa
   * @return indice
  **/
  @JsonProperty("indice")
  @NotNull
  @Valid
  public BigDecimal getIndice() {
    return indice;
  }

  public void setIndice(BigDecimal indice) {
    this.indice = indice;
  }

  public Riscossione indice(BigDecimal indice) {
    this.indice = indice;
    return this;
  }

 /**
   * location dove reperire il dettaglio della pendenza
   * @return pendenza
  **/
  @JsonProperty("pendenza")
  @NotNull
  @Valid
  public String getPendenza() {
    return pendenza;
  }

  public void setPendenza(String pendenza) {
    this.pendenza = pendenza;
  }

  public Riscossione pendenza(String pendenza) {
    this.pendenza = pendenza;
    return this;
  }

 /**
   * Identificativo della voce di pedenza,interno alla pendenza, nel gestionale proprietario a cui si riferisce la riscossione
   * @return idVocePendenza
  **/
  @JsonProperty("idVocePendenza")
  @NotNull
  @Valid
  public String getIdVocePendenza() {
    return idVocePendenza;
  }

  public void setIdVocePendenza(String idVocePendenza) {
    this.idVocePendenza = idVocePendenza;
  }

  public Riscossione idVocePendenza(String idVocePendenza) {
    this.idVocePendenza = idVocePendenza;
    return this;
  }

 /**
   * location dove reperire la richiesta di pagamento che ha determinato la ricossione
   * @return rpp
  **/
  @JsonProperty("rpp")
  @Valid
  public String getRpp() {
    return rpp;
  }

  public void setRpp(String rpp) {
    this.rpp = rpp;
  }

  public Riscossione rpp(String rpp) {
    this.rpp = rpp;
    return this;
  }

 /**
   * Get stato
   * @return stato
  **/
  @JsonProperty("stato")
  @NotNull
  @Valid
  public String getStato() {
    if (stato == null) {
      return null;
    }
    return stato.getValue();
  }

  public void setStato(StatoEnum stato) {
    this.stato = stato;
  }

  public Riscossione stato(StatoEnum stato) {
    this.stato = stato;
    return this;
  }

 /**
   * Get tipo
   * @return tipo
  **/
  @JsonProperty("tipo")
  @NotNull
  @Valid
  public TipoRiscossione getTipo() {
    return tipo;
  }

  public void setTipo(TipoRiscossione tipo) {
    this.tipo = tipo;
  }

  public Riscossione tipo(TipoRiscossione tipo) {
    this.tipo = tipo;
    return this;
  }

 /**
   * Importo riscosso. 
   * @return importo
  **/
  @JsonProperty("importo")
  @NotNull
  @Valid
  public BigDecimal getImporto() {
    return importo;
  }

  public void setImporto(BigDecimal importo) {
    this.importo = importo;
  }

  public Riscossione importo(BigDecimal importo) {
    this.importo = importo;
    return this;
  }

 /**
   * Data di esecuzione della riscossione
   * @return data
  **/
  @JsonProperty("data")
  @NotNull
  @Valid
  public Date getData() {
    return data;
  }

  public void setData(Date data) {
    this.data = data;
  }

  public Riscossione data(Date data) {
    this.data = data;
    return this;
  }

 /**
   * Importo delle commissioni applicate al pagamento dal PSP
   * @return commissioni
  **/
  @JsonProperty("commissioni")
  @Valid
  public BigDecimal getCommissioni() {
    return commissioni;
  }

  public void setCommissioni(BigDecimal commissioni) {
    this.commissioni = commissioni;
  }

  public Riscossione commissioni(BigDecimal commissioni) {
    this.commissioni = commissioni;
    return this;
  }

 /**
   * Get allegato
   * @return allegato
  **/
  @JsonProperty("allegato")
  @Valid
  public Allegato getAllegato() {
    return allegato;
  }

  public void setAllegato(Allegato allegato) {
    this.allegato = allegato;
  }

  public Riscossione allegato(Allegato allegato) {
    this.allegato = allegato;
    return this;
  }


  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class Riscossione {\n");
    
    sb.append("    idDominio: ").append(toIndentedString(idDominio)).append("\n");
    sb.append("    iuv: ").append(toIndentedString(iuv)).append("\n");
    sb.append("    iur: ").append(toIndentedString(iur)).append("\n");
    sb.append("    indice: ").append(toIndentedString(indice)).append("\n");
    sb.append("    pendenza: ").append(toIndentedString(pendenza)).append("\n");
    sb.append("    idVocePendenza: ").append(toIndentedString(idVocePendenza)).append("\n");
    sb.append("    rpp: ").append(toIndentedString(rpp)).append("\n");
    sb.append("    stato: ").append(toIndentedString(stato)).append("\n");
    sb.append("    tipo: ").append(toIndentedString(tipo)).append("\n");
    sb.append("    importo: ").append(toIndentedString(importo)).append("\n");
    sb.append("    data: ").append(toIndentedString(data)).append("\n");
    sb.append("    commissioni: ").append(toIndentedString(commissioni)).append("\n");
    sb.append("    allegato: ").append(toIndentedString(allegato)).append("\n");
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
