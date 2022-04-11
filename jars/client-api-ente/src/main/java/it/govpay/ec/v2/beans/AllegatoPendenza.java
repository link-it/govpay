package it.govpay.ec.v2.beans;


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
  
  @Schema(required = true, description = "path per accedere al file, nella forma /allegati/{id}")
 /**
   * path per accedere al file, nella forma /allegati/{id}  
  **/
  private Object contenuto = null;
 /**
   * nome del file
   * @return nome
  **/
  @JsonProperty("nome")
  public String getNome() {
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
  public String getTipo() {
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
   * path per accedere al file, nella forma /allegati/{id}
   * @return contenuto
  **/
  @JsonProperty("contenuto")
  public Object getContenuto() {
    return contenuto;
  }

  public void setContenuto(Object contenuto) {
    this.contenuto = contenuto;
  }

  public AllegatoPendenza contenuto(Object contenuto) {
    this.contenuto = contenuto;
    return this;
  }


  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class AllegatoPendenza {\n");
    
    sb.append("    nome: ").append(toIndentedString(nome)).append("\n");
    sb.append("    tipo: ").append(toIndentedString(tipo)).append("\n");
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
