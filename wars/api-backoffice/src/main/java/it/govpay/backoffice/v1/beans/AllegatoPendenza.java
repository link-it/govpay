package it.govpay.backoffice.v1.beans;


import java.util.Objects;

import org.openspcoop2.generic_project.exception.ServiceException;
import org.openspcoop2.utils.json.ValidationException;

import com.fasterxml.jackson.annotation.JsonProperty;

import it.govpay.core.beans.JSONSerializable;
import it.govpay.core.utils.validator.IValidable;
import it.govpay.core.utils.validator.ValidatorFactory;

@com.fasterxml.jackson.annotation.JsonPropertyOrder({
"nome",
"tipo",
"contenuto",
})
public class AllegatoPendenza extends JSONSerializable implements IValidable {
  
  @JsonProperty("nome")
  private String nome = null;
  
  @JsonProperty("tipo")
  private String tipo = "application/octet-stream";
  
  @JsonProperty("contenuto")
  private Object contenuto = null;
  
  /**
   * nome del file
   **/
  public AllegatoPendenza nome(String nome) {
    this.nome = nome;
    return this;
  }

  @JsonProperty("nome")
  public String getNome() {
    return nome;
  }
  public void setNome(String nome) {
    this.nome = nome;
  }

  /**
   * mime type del file
   **/
  public AllegatoPendenza tipo(String tipo) {
    this.tipo = tipo;
    return this;
  }

  @JsonProperty("tipo")
  public String getTipo() {
    return tipo;
  }
  public void setTipo(String tipo) {
    this.tipo = tipo;
  }

  /**
   **/
  public AllegatoPendenza contenuto(Object contenuto) {
    this.contenuto = contenuto;
    return this;
  }

  @JsonProperty("contenuto")
  public Object getContenuto() {
    return contenuto;
  }
  public void setContenuto(Object contenuto) {
    this.contenuto = contenuto;
  }

  @Override
  public boolean equals(java.lang.Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    AllegatoPendenza allegatoPendenza = (AllegatoPendenza) o;
    return Objects.equals(nome, allegatoPendenza.nome) &&
        Objects.equals(tipo, allegatoPendenza.tipo) &&
        Objects.equals(contenuto, allegatoPendenza.contenuto);
  }

  @Override
  public int hashCode() {
    return Objects.hash(nome, tipo, contenuto);
  }

  public static AllegatoPendenza parse(String json) throws ServiceException, ValidationException {
    return (AllegatoPendenza) parse(json, AllegatoPendenza.class);
  }

  @Override
  public String getJsonIdFilter() {
    return "allegatoPendenza";
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
  private String toIndentedString(java.lang.Object o) {
    if (o == null) {
      return "null";
    }
    return o.toString().replace("\n", "\n    ");
  }
  
  @Override
	public void validate() throws ValidationException {
		ValidatorFactory vf = ValidatorFactory.newInstance();
		vf.getValidator("nome", this.nome).notNull().minLength(1).maxLength(255);
		vf.getValidator("tipo", this.tipo).minLength(1).maxLength(255);
		
		if(this.contenuto == null)
			throw new ValidationException("Il campo " + "contenuto" + " non deve essere vuoto.");
	}
}



