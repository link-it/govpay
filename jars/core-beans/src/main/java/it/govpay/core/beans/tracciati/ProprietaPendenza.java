/*
 * GovPay - Porta di Accesso al Nodo dei Pagamenti SPC
 * http://www.gov4j.it/govpay
 *
 * Copyright (c) 2014-2024 Link.it srl (http://www.link.it).
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
package it.govpay.core.beans.tracciati;

import java.util.List;
import java.util.Objects;

import com.fasterxml.jackson.annotation.JsonProperty;

import it.govpay.core.beans.JSONSerializable;
import it.govpay.core.exceptions.IOException;

/**
 * Dati supporto per la gestione del ciclo di vita della pendenza.
 **/@com.fasterxml.jackson.annotation.JsonPropertyOrder({
"linguaSecondaria",
"descrizioneImporto",
"lineaTestoRicevuta1",
"lineaTestoRicevuta2",
"linguaSecondariaCausale",
"informativaImportoAvviso",
"linguaSecondariaInformativaImportoAvviso",
})
public class ProprietaPendenza extends JSONSerializable {
  
  @JsonProperty("linguaSecondaria")
  private LinguaSecondaria linguaSecondaria = null;
  
  @JsonProperty("descrizioneImporto")
  private List<VoceDescrizioneImporto> descrizioneImporto = null;
  
  @JsonProperty("lineaTestoRicevuta1")
  private String lineaTestoRicevuta1 = null;
  
  @JsonProperty("lineaTestoRicevuta2")
  private String lineaTestoRicevuta2 = null;
  
  @JsonProperty("linguaSecondariaCausale")
  private String linguaSecondariaCausale = null;
  
  @JsonProperty("informativaImportoAvviso")
  private String informativaImportoAvviso = null;
  
  @JsonProperty("linguaSecondariaInformativaImportoAvviso")
  private String linguaSecondariaInformativaImportoAvviso = null;
  
  /**
   **/
  public ProprietaPendenza linguaSecondaria(LinguaSecondaria linguaSecondaria) {
    this.linguaSecondaria = linguaSecondaria;
    return this;
  }

  @JsonProperty("linguaSecondaria")
  public LinguaSecondaria getLinguaSecondaria() {
    return linguaSecondaria;
  }
  public void setLinguaSecondaria(LinguaSecondaria linguaSecondaria) {
    this.linguaSecondaria = linguaSecondaria;
  }

  /**
   **/
  public ProprietaPendenza descrizioneImporto(List<VoceDescrizioneImporto> descrizioneImporto) {
    this.descrizioneImporto = descrizioneImporto;
    return this;
  }

  @JsonProperty("descrizioneImporto")
  public List<VoceDescrizioneImporto> getDescrizioneImporto() {
    return descrizioneImporto;
  }
  public void setDescrizioneImporto(List<VoceDescrizioneImporto> descrizioneImporto) {
    this.descrizioneImporto = descrizioneImporto;
  }

  /**
   * stringa personalizzata da inserire nella ricevuta
   **/
  public ProprietaPendenza lineaTestoRicevuta1(String lineaTestoRicevuta1) {
    this.lineaTestoRicevuta1 = lineaTestoRicevuta1;
    return this;
  }

  @JsonProperty("lineaTestoRicevuta1")
  public String getLineaTestoRicevuta1() {
    return lineaTestoRicevuta1;
  }
  public void setLineaTestoRicevuta1(String lineaTestoRicevuta1) {
    this.lineaTestoRicevuta1 = lineaTestoRicevuta1;
  }

  /**
   * stringa personalizzata da inserire nella ricevuta
   **/
  public ProprietaPendenza lineaTestoRicevuta2(String lineaTestoRicevuta2) {
    this.lineaTestoRicevuta2 = lineaTestoRicevuta2;
    return this;
  }

  @JsonProperty("lineaTestoRicevuta2")
  public String getLineaTestoRicevuta2() {
    return lineaTestoRicevuta2;
  }
  public void setLineaTestoRicevuta2(String lineaTestoRicevuta2) {
    this.lineaTestoRicevuta2 = lineaTestoRicevuta2;
  }
  
  /**
   * traduzione nella causale 
   **/
  public ProprietaPendenza linguaSecondariaCausale(String linguaSecondariaCausale) {
    this.linguaSecondariaCausale = linguaSecondariaCausale;
    return this;
  }

  @JsonProperty("linguaSecondariaCausale")
  public String getLinguaSecondariaCausale() {
    return linguaSecondariaCausale;
  }
  public void setLinguaSecondariaCausale(String linguaSecondariaCausale) {
    this.linguaSecondariaCausale = linguaSecondariaCausale;
  }

  /**
   * se valorizzato, sostituisce il testo standard. Se valorizzato con stringa vuota, l'informativa viene omessa.
   **/
  public ProprietaPendenza informativaImportoAvviso(String informativaImportoAvviso) {
    this.informativaImportoAvviso = informativaImportoAvviso;
    return this;
  }

  @JsonProperty("informativaImportoAvviso")
  public String getInformativaImportoAvviso() {
    return informativaImportoAvviso;
  }
  public void setInformativaImportoAvviso(String informativaImportoAvviso) {
    this.informativaImportoAvviso = informativaImportoAvviso;
  }

  /**
   * se valorizzato, sostituisce il testo standard. Se valorizzato con stringa vuota, l'informativa viene omessa.
   **/
  public ProprietaPendenza linguaSecondariaInformativaImportoAvviso(String linguaSecondariaInformativaImportoAvviso) {
    this.linguaSecondariaInformativaImportoAvviso = linguaSecondariaInformativaImportoAvviso;
    return this;
  }

  @JsonProperty("linguaSecondariaInformativaImportoAvviso")
  public String getLinguaSecondariaInformativaImportoAvviso() {
    return linguaSecondariaInformativaImportoAvviso;
  }
  public void setLinguaSecondariaInformativaImportoAvviso(String linguaSecondariaInformativaImportoAvviso) {
    this.linguaSecondariaInformativaImportoAvviso = linguaSecondariaInformativaImportoAvviso;
  }

  @Override
  public boolean equals(java.lang.Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    ProprietaPendenza proprietaPendenza = (ProprietaPendenza) o;
    return Objects.equals(linguaSecondaria, proprietaPendenza.linguaSecondaria) &&
        Objects.equals(descrizioneImporto, proprietaPendenza.descrizioneImporto) &&
        Objects.equals(lineaTestoRicevuta1, proprietaPendenza.lineaTestoRicevuta1) &&
        Objects.equals(lineaTestoRicevuta2, proprietaPendenza.lineaTestoRicevuta2) &&
        Objects.equals(linguaSecondariaCausale, proprietaPendenza.linguaSecondariaCausale) &&
        Objects.equals(informativaImportoAvviso, proprietaPendenza.informativaImportoAvviso) &&
        Objects.equals(linguaSecondariaInformativaImportoAvviso, proprietaPendenza.linguaSecondariaInformativaImportoAvviso);
  }

  @Override
  public int hashCode() {
    return Objects.hash(linguaSecondaria, descrizioneImporto, lineaTestoRicevuta1, lineaTestoRicevuta2, linguaSecondariaCausale, informativaImportoAvviso, linguaSecondariaInformativaImportoAvviso);
  }

  public static ProprietaPendenza parse(String json) throws IOException { 
    return (ProprietaPendenza) parse(json, ProprietaPendenza.class);
  }

  @Override
  public String getJsonIdFilter() {
    return "proprietaPendenza";
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
  private String toIndentedString(java.lang.Object o) {
    if (o == null) {
      return "null";
    }
    return o.toString().replace("\n", "\n    ");
  }
}






