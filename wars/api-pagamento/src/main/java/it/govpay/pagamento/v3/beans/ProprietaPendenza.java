/*
 * GovPay - Porta di Accesso al Nodo dei Pagamenti SPC
 * http://www.gov4j.it/govpay
 *
 * Copyright (c) 2014-2023 Link.it srl (http://www.link.it).
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License version 3, as published by
 * the Free Software Foundation.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 *
 */
package it.govpay.pagamento.v3.beans;

import java.util.List;

import javax.validation.constraints.Size;

import com.fasterxml.jackson.annotation.JsonProperty;

import io.swagger.v3.oas.annotations.media.Schema;

/**
  * Dati supporto per la gestione del ciclo di vita della pendenza.
 **/
@Schema(description="Dati supporto per la gestione del ciclo di vita della pendenza.")
public class ProprietaPendenza   {

  @Schema(description = "")
  private LinguaSecondaria linguaSecondaria = null;

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

  @Schema(description = "causale della pendenza nella lingua secondaria da inserire nell'avviso")
 /**
   * causale della pendenza nella lingua secondaria da inserire nell'avviso
  **/
  private String linguaSecondariaCausale = null;
  
  @Schema(description = "se valorizzato, sostituisce il testo standard. Se valorizzato con stringa vuota, l'informativa viene omessa.")
 /**
   * se valorizzato, sostituisce il testo standard. Se valorizzato con stringa vuota, l'informativa viene omessa.  
  **/
  private String informativaImportoAvviso = null;
  
  @Schema(description = "se valorizzato, sostituisce il testo standard. Se valorizzato con stringa vuota, l'informativa viene omessa.")
 /**
   * se valorizzato, sostituisce il testo standard. Se valorizzato con stringa vuota, l'informativa viene omessa.  
  **/
  private String linguaSecondariaInformativaImportoAvviso = null;
 /**
   * Get linguaSecondaria
   * @return linguaSecondaria
  **/
  @JsonProperty("linguaSecondaria")
  public LinguaSecondaria getLinguaSecondaria() {
    return linguaSecondaria;
  }

  public void setLinguaSecondaria(LinguaSecondaria linguaSecondaria) {
    this.linguaSecondaria = linguaSecondaria;
  }

  public ProprietaPendenza linguaSecondaria(LinguaSecondaria linguaSecondaria) {
    this.linguaSecondaria = linguaSecondaria;
    return this;
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

 /**
   * se valorizzato, sostituisce il testo standard. Se valorizzato con stringa vuota, l&#x27;informativa viene omessa.
   * @return informativaImportoAvviso
  **/
  @JsonProperty("informativaImportoAvviso")
 @Size(max=255)  public String getInformativaImportoAvviso() {
    return informativaImportoAvviso;
  }

  public void setInformativaImportoAvviso(String informativaImportoAvviso) {
    this.informativaImportoAvviso = informativaImportoAvviso;
  }

  public ProprietaPendenza informativaImportoAvviso(String informativaImportoAvviso) {
    this.informativaImportoAvviso = informativaImportoAvviso;
    return this;
  }

 /**
   * se valorizzato, sostituisce il testo standard. Se valorizzato con stringa vuota, l&#x27;informativa viene omessa.
   * @return linguaSecondariaInformativaImportoAvviso
  **/
  @JsonProperty("linguaSecondariaInformativaImportoAvviso")
 @Size(max=255)  public String getLinguaSecondariaInformativaImportoAvviso() {
    return linguaSecondariaInformativaImportoAvviso;
  }

  public void setLinguaSecondariaInformativaImportoAvviso(String linguaSecondariaInformativaImportoAvviso) {
    this.linguaSecondariaInformativaImportoAvviso = linguaSecondariaInformativaImportoAvviso;
  }

  public ProprietaPendenza linguaSecondariaInformativaImportoAvviso(String linguaSecondariaInformativaImportoAvviso) {
    this.linguaSecondariaInformativaImportoAvviso = linguaSecondariaInformativaImportoAvviso;
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
    sb.append("    informativaImportoAvviso: ").append(toIndentedString(informativaImportoAvviso)).append("\n");
    sb.append("    linguaSecondariaInformativaImportoAvviso: ").append(toIndentedString(linguaSecondariaInformativaImportoAvviso)).append("\n");
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
