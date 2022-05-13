package it.govpay.ragioneria.v3.beans;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import com.fasterxml.jackson.annotation.JsonProperty;

import io.swagger.v3.oas.annotations.media.Schema;

public class AllegatoPendenza   {
  
  @Schema(required = true, description = "nome del file")
 /**
   * nome del file  
  **/
  private String nome = null;
  
  @Schema(description = "mime type del file")
 /**
   * mime type del file  
  **/
  private String tipo = "application/octet-stream";
  
  @Schema(description = "descrizione del file")
 /**
   * descrizione del file  
  **/
  private String descrizione = null;
  
  @Schema(required = true, description = "path per accedere al file, nella forma /allegati/{id}")
 /**
   * path per accedere al file, nella forma /allegati/{id}  
  **/
  private String contenuto = null;
 /**
   * nome del file
   * @return nome
  **/
  @JsonProperty("nome")
  @NotNull
 @Size(min=1,max=255)  public String getNome() {
    return nome;
  }

  public void setNome(String nome) {
    this.nome = nome;
  }

  public AllegatoPendenza nome(String nome) {
    this.nome = nome;
    return this;
  }

 /**
   * mime type del file
   * @return tipo
  **/
  @JsonProperty("tipo")
 @Size(min=1,max=255)  public String getTipo() {
    return tipo;
  }

  public void setTipo(String tipo) {
    this.tipo = tipo;
  }

  public AllegatoPendenza tipo(String tipo) {
    this.tipo = tipo;
    return this;
  }

 /**
   * descrizione del file
   * @return descrizione
  **/
  @JsonProperty("descrizione")
 @Size(min=1,max=255)  public String getDescrizione() {
    return descrizione;
  }

  public void setDescrizione(String descrizione) {
    this.descrizione = descrizione;
  }

  public AllegatoPendenza descrizione(String descrizione) {
    this.descrizione = descrizione;
    return this;
  }

 /**
   * path per accedere al file, nella forma /allegati/{id}
   * @return contenuto
  **/
  @JsonProperty("contenuto")
  @NotNull
  public String getContenuto() {
    return contenuto;
  }

  public void setContenuto(String contenuto) {
    this.contenuto = contenuto;
  }

  public AllegatoPendenza contenuto(String contenuto) {
    this.contenuto = contenuto;
    return this;
  }


  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class AllegatoPendenza {\n");
    
    sb.append("    nome: ").append(toIndentedString(nome)).append("\n");
    sb.append("    tipo: ").append(toIndentedString(tipo)).append("\n");
    sb.append("    descrizione: ").append(toIndentedString(descrizione)).append("\n");
    sb.append("    contenuto: ").append(toIndentedString(contenuto)).append("\n");
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
