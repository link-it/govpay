package it.govpay.ec.v2.beans;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

import io.swagger.v3.oas.annotations.media.Schema;

/**
  * Dati supporto per la gestione del ciclo di vita della pendenza.
 **/
@Schema(description="Dati supporto per la gestione del ciclo di vita della pendenza.")
public class ProprietaPendenza   {
  
  @JsonIgnore
  private LinguaSecondaria linguaSecondariaEnum = null;
	
  @Schema(description = "")
  private String linguaSecondaria = null;
  
  @Schema(description = "")
  private List<VoceDescrizioneImporto> descrizioneImporto = null;
  
  @Schema(description = "stringa personalizzata da inserire nella ricevuta")
 /**
   * stringa personalizzata da inserire nella ricevuta  
  **/
  private String lineaTestoRicevuta1 = null;
  
  @Schema(description = "stringa personalizzata da inserire nella ricevuta")
 /**
   * stringa personalizzata da inserire nella ricevuta  
  **/
  private String lineaTestoRicevuta2 = null;
  
  @Schema(description = "causale della pendenza nella lingua secondaria da inserire nell'avviso                  ")
 /**
   * causale della pendenza nella lingua secondaria da inserire nell'avviso                    
  **/
  private String linguaSecondariaCausale = null;
 /**
   * Get linguaSecondaria
   * @return linguaSecondaria
  **/
  @JsonProperty("linguaSecondaria")
  public String getLinguaSecondaria() {
    return linguaSecondaria;
  }

  public void setLinguaSecondaria(String linguaSecondaria) {
    this.linguaSecondaria = linguaSecondaria;
  }

  public ProprietaPendenza linguaSecondaria(String linguaSecondaria) {
    this.linguaSecondaria = linguaSecondaria;
    return this;
  }
  
  public ProprietaPendenza linguaSecondariaEnum(LinguaSecondaria linguaSecondaria) {
    this.linguaSecondariaEnum = linguaSecondaria;
    return this;
  }

  @JsonIgnore
  public LinguaSecondaria getLinguaSecondariaEnum() {
    return linguaSecondariaEnum;
  }
  public void setLinguaSecondariaEnum(LinguaSecondaria linguaSecondaria) {
    this.linguaSecondariaEnum = linguaSecondaria;
  }

 /**
   * Get descrizioneImporto
   * @return descrizioneImporto
  **/
  @JsonProperty("descrizioneImporto")
  public List<VoceDescrizioneImporto> getDescrizioneImporto() {
    return descrizioneImporto;
  }

  public void setDescrizioneImporto(List<VoceDescrizioneImporto> descrizioneImporto) {
    this.descrizioneImporto = descrizioneImporto;
  }

  public ProprietaPendenza descrizioneImporto(List<VoceDescrizioneImporto> descrizioneImporto) {
    this.descrizioneImporto = descrizioneImporto;
    return this;
  }

  public ProprietaPendenza addDescrizioneImportoItem(VoceDescrizioneImporto descrizioneImportoItem) {
    this.descrizioneImporto.add(descrizioneImportoItem);
    return this;
  }

 /**
   * stringa personalizzata da inserire nella ricevuta
   * @return lineaTestoRicevuta1
  **/
  @JsonProperty("lineaTestoRicevuta1")
  public String getLineaTestoRicevuta1() {
    return lineaTestoRicevuta1;
  }

  public void setLineaTestoRicevuta1(String lineaTestoRicevuta1) {
    this.lineaTestoRicevuta1 = lineaTestoRicevuta1;
  }

  public ProprietaPendenza lineaTestoRicevuta1(String lineaTestoRicevuta1) {
    this.lineaTestoRicevuta1 = lineaTestoRicevuta1;
    return this;
  }

 /**
   * stringa personalizzata da inserire nella ricevuta
   * @return lineaTestoRicevuta2
  **/
  @JsonProperty("lineaTestoRicevuta2")
  public String getLineaTestoRicevuta2() {
    return lineaTestoRicevuta2;
  }

  public void setLineaTestoRicevuta2(String lineaTestoRicevuta2) {
    this.lineaTestoRicevuta2 = lineaTestoRicevuta2;
  }

  public ProprietaPendenza lineaTestoRicevuta2(String lineaTestoRicevuta2) {
    this.lineaTestoRicevuta2 = lineaTestoRicevuta2;
    return this;
  }

 /**
   * causale della pendenza nella lingua secondaria da inserire nell&#x27;avviso                  
   * @return linguaSecondariaCausale
  **/
  @JsonProperty("linguaSecondariaCausale")
  public String getLinguaSecondariaCausale() {
    return linguaSecondariaCausale;
  }

  public void setLinguaSecondariaCausale(String linguaSecondariaCausale) {
    this.linguaSecondariaCausale = linguaSecondariaCausale;
  }

  public ProprietaPendenza linguaSecondariaCausale(String linguaSecondariaCausale) {
    this.linguaSecondariaCausale = linguaSecondariaCausale;
    return this;
  }


  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class ProprietaPendenza {\n");
    
    sb.append("    linguaSecondaria: ").append(toIndentedString(linguaSecondaria)).append("\n");
    sb.append("    descrizioneImporto: ").append(toIndentedString(descrizioneImporto)).append("\n");
    sb.append("    lineaTestoRicevuta1: ").append(toIndentedString(lineaTestoRicevuta1)).append("\n");
    sb.append("    lineaTestoRicevuta2: ").append(toIndentedString(lineaTestoRicevuta2)).append("\n");
    sb.append("    linguaSecondariaCausale: ").append(toIndentedString(linguaSecondariaCausale)).append("\n");
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
