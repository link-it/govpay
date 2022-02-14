package it.govpay.pendenze.v2.beans;


import java.util.List;
import java.util.Objects;

import org.apache.commons.lang.ArrayUtils;
import org.openspcoop2.generic_project.exception.ServiceException;
import org.openspcoop2.utils.json.ValidationException;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

import it.govpay.core.beans.JSONSerializable;
import it.govpay.core.utils.validator.IValidable;
import it.govpay.core.utils.validator.ValidatorFactory;

/**
 * Dati supporto per la gestione del ciclo di vita della pendenza.
 **/@com.fasterxml.jackson.annotation.JsonPropertyOrder({
"linguaSecondaria",
"descrizioneImporto",
"lineaTestoRicevuta1",
"lineaTestoRicevuta2",
"linguaSecondariaCausale",
})
public class ProprietaPendenza extends JSONSerializable implements IValidable{
  
  @JsonProperty("linguaSecondaria")
  private String linguaSecondaria = null;
  
  @JsonIgnore
  private LinguaSecondaria linguaSecondariaEnum = null;
  
  @JsonProperty("descrizioneImporto")
  private List<VoceDescrizioneImporto> descrizioneImporto = null;
  
  @JsonProperty("lineaTestoRicevuta1")
  private String lineaTestoRicevuta1 = null;
  
  @JsonProperty("lineaTestoRicevuta2")
  private String lineaTestoRicevuta2 = null;
  
  @JsonProperty("linguaSecondariaCausale")
  private String linguaSecondariaCausale = null;
  
  /**
   **/
  public ProprietaPendenza linguaSecondaria(String linguaSecondaria) {
    this.linguaSecondaria = linguaSecondaria;
    return this;
  }
  
  @JsonProperty("linguaSecondaria")
  public String getLinguaSecondaria() {
    return linguaSecondaria;
  }
  public void setLinguaSecondaria(String linguaSecondaria) {
    this.linguaSecondaria = linguaSecondaria;
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
   * causale della pendenza nella lingua secondaria da inserire nell'avviso
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
        Objects.equals(linguaSecondariaCausale, proprietaPendenza.linguaSecondariaCausale);
  }

  @Override
  public int hashCode() {
    return Objects.hash(linguaSecondaria, descrizioneImporto, lineaTestoRicevuta1, lineaTestoRicevuta2, linguaSecondariaCausale);
  }

  public static ProprietaPendenza parse(String json) throws ServiceException, ValidationException { 
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
	  if(linguaSecondaria != null) {
			LinguaSecondaria linguaSecondariaEnum = LinguaSecondaria.fromValue(linguaSecondaria);
			if(linguaSecondariaEnum == null) {
				throw new ValidationException("Codifica inesistente per linguaSecondaria. Valore fornito [" + linguaSecondaria + "] valori possibili " + ArrayUtils.toString(LinguaSecondaria.values()));
			}
	  }
	  vf.getValidator("descrizioneImporto", descrizioneImporto).validateObjects();
  }
}



