package it.govpay.backoffice.v1.beans;


import java.util.Objects;

import org.openspcoop2.generic_project.exception.ServiceException;
import org.openspcoop2.utils.json.ValidationException;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

import it.govpay.core.beans.JSONSerializable;

@JsonPropertyOrder({
"idTipoPendenza",
"descrizione",
"tipo",
"codificaIUV",
"pagaTerzi",
"abilitato",
"form",
"validazione",
"trasformazione",
"inoltro",
"promemoriaAvviso",
"promemoriaRicevuta",
"valori",
})
public class TipoPendenzaDominio extends JSONSerializable {
  
  @JsonProperty("idTipoPendenza")
  private String idTipoPendenza = null;
  
  @JsonProperty("descrizione")
  private String descrizione = null;
  
  @JsonProperty("tipo")
  private TipoPendenzaTipologia tipo = null;
  
  @JsonProperty("codificaIUV")
  private String codificaIUV = null;
  
  @JsonProperty("pagaTerzi")
  private Boolean pagaTerzi = false;
  
  @JsonProperty("abilitato")
  private Boolean abilitato = true;
  
  @JsonProperty("form")
  private TipoPendenzaForm form = null;
  
  @JsonProperty("validazione")
  private Object validazione = null;
  
  @JsonProperty("trasformazione")
  private TipoPendenzaTrasformazione trasformazione = null;
  
  @JsonProperty("inoltro")
  private String inoltro = null;
  
  @JsonProperty("promemoriaAvviso")
  private TipoPendenzaPromemoria promemoriaAvviso = null;
  
  @JsonProperty("promemoriaRicevuta")
  private TipoPendenzaPromemoria promemoriaRicevuta = null;
  
  @JsonProperty("valori")
  private TipoPendenzaDominioPost valori = null;
  
  /**
   **/
  public TipoPendenzaDominio idTipoPendenza(String idTipoPendenza) {
    this.idTipoPendenza = idTipoPendenza;
    return this;
  }

  @JsonProperty("idTipoPendenza")
  public String getIdTipoPendenza() {
    return idTipoPendenza;
  }
  public void setIdTipoPendenza(String idTipoPendenza) {
    this.idTipoPendenza = idTipoPendenza;
  }

  /**
   **/
  public TipoPendenzaDominio descrizione(String descrizione) {
    this.descrizione = descrizione;
    return this;
  }

  @JsonProperty("descrizione")
  public String getDescrizione() {
    return descrizione;
  }
  public void setDescrizione(String descrizione) {
    this.descrizione = descrizione;
  }

  /**
   **/
  public TipoPendenzaDominio tipo(TipoPendenzaTipologia tipo) {
    this.tipo = tipo;
    return this;
  }

  @JsonProperty("tipo")
  public TipoPendenzaTipologia getTipo() {
    return tipo;
  }
  public void setTipo(TipoPendenzaTipologia tipo) {
    this.tipo = tipo;
  }

  /**
   * Cifra identificativa negli IUV
   **/
  public TipoPendenzaDominio codificaIUV(String codificaIUV) {
    this.codificaIUV = codificaIUV;
    return this;
  }

  @JsonProperty("codificaIUV")
  public String getCodificaIUV() {
    return codificaIUV;
  }
  public void setCodificaIUV(String codificaIUV) {
    this.codificaIUV = codificaIUV;
  }

  /**
   * Indica se la pendenza e' pagabile da soggetti terzi
   **/
  public TipoPendenzaDominio pagaTerzi(Boolean pagaTerzi) {
    this.pagaTerzi = pagaTerzi;
    return this;
  }

  @JsonProperty("pagaTerzi")
  public Boolean PagaTerzi() {
    return pagaTerzi;
  }
  public void setPagaTerzi(Boolean pagaTerzi) {
    this.pagaTerzi = pagaTerzi;
  }

  /**
   * Indicazione la tipologia pendenza e' abilitata
   **/
  public TipoPendenzaDominio abilitato(Boolean abilitato) {
    this.abilitato = abilitato;
    return this;
  }

  @JsonProperty("abilitato")
  public Boolean Abilitato() {
    return abilitato;
  }
  public void setAbilitato(Boolean abilitato) {
    this.abilitato = abilitato;
  }

  /**
   **/
  public TipoPendenzaDominio form(TipoPendenzaForm form) {
    this.form = form;
    return this;
  }

  @JsonProperty("form")
  public TipoPendenzaForm getForm() {
    return form;
  }
  public void setForm(TipoPendenzaForm form) {
    this.form = form;
  }

  /**
   * JSON Schema da utilizzare per la validazione dell'input
   **/
  public TipoPendenzaDominio validazione(Object validazione) {
    this.validazione = validazione;
    return this;
  }

  @JsonProperty("validazione")
  public Object getValidazione() {
    return validazione;
  }
  public void setValidazione(Object validazione) {
    this.validazione = validazione;
  }

  /**
   **/
  public TipoPendenzaDominio trasformazione(TipoPendenzaTrasformazione trasformazione) {
    this.trasformazione = trasformazione;
    return this;
  }

  @JsonProperty("trasformazione")
  public TipoPendenzaTrasformazione getTrasformazione() {
    return trasformazione;
  }
  public void setTrasformazione(TipoPendenzaTrasformazione trasformazione) {
    this.trasformazione = trasformazione;
  }

  /**
   * Identificativo dell'applicazione verso cui fare l'inoltro della pendenza
   **/
  public TipoPendenzaDominio inoltro(String inoltro) {
    this.inoltro = inoltro;
    return this;
  }

  @JsonProperty("inoltro")
  public String getInoltro() {
    return inoltro;
  }
  public void setInoltro(String inoltro) {
    this.inoltro = inoltro;
  }

  /**
   **/
  public TipoPendenzaDominio promemoriaAvviso(TipoPendenzaPromemoria promemoriaAvviso) {
    this.promemoriaAvviso = promemoriaAvviso;
    return this;
  }

  @JsonProperty("promemoriaAvviso")
  public TipoPendenzaPromemoria getPromemoriaAvviso() {
    return promemoriaAvviso;
  }
  public void setPromemoriaAvviso(TipoPendenzaPromemoria promemoriaAvviso) {
    this.promemoriaAvviso = promemoriaAvviso;
  }

  /**
   **/
  public TipoPendenzaDominio promemoriaRicevuta(TipoPendenzaPromemoria promemoriaRicevuta) {
    this.promemoriaRicevuta = promemoriaRicevuta;
    return this;
  }

  @JsonProperty("promemoriaRicevuta")
  public TipoPendenzaPromemoria getPromemoriaRicevuta() {
    return promemoriaRicevuta;
  }
  public void setPromemoriaRicevuta(TipoPendenzaPromemoria promemoriaRicevuta) {
    this.promemoriaRicevuta = promemoriaRicevuta;
  }

  /**
   **/
  public TipoPendenzaDominio valori(TipoPendenzaDominioPost valori) {
    this.valori = valori;
    return this;
  }

  @JsonProperty("valori")
  public TipoPendenzaDominioPost getValori() {
    return valori;
  }
  public void setValori(TipoPendenzaDominioPost valori) {
    this.valori = valori;
  }

  @Override
  public boolean equals(java.lang.Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    TipoPendenzaDominio tipoPendenzaDominio = (TipoPendenzaDominio) o;
    return Objects.equals(idTipoPendenza, tipoPendenzaDominio.idTipoPendenza) &&
        Objects.equals(descrizione, tipoPendenzaDominio.descrizione) &&
        Objects.equals(tipo, tipoPendenzaDominio.tipo) &&
        Objects.equals(codificaIUV, tipoPendenzaDominio.codificaIUV) &&
        Objects.equals(pagaTerzi, tipoPendenzaDominio.pagaTerzi) &&
        Objects.equals(abilitato, tipoPendenzaDominio.abilitato) &&
        Objects.equals(form, tipoPendenzaDominio.form) &&
        Objects.equals(validazione, tipoPendenzaDominio.validazione) &&
        Objects.equals(trasformazione, tipoPendenzaDominio.trasformazione) &&
        Objects.equals(inoltro, tipoPendenzaDominio.inoltro) &&
        Objects.equals(promemoriaAvviso, tipoPendenzaDominio.promemoriaAvviso) &&
        Objects.equals(promemoriaRicevuta, tipoPendenzaDominio.promemoriaRicevuta) &&
        Objects.equals(valori, tipoPendenzaDominio.valori);
  }

  @Override
  public int hashCode() {
    return Objects.hash(idTipoPendenza, descrizione, tipo, codificaIUV, pagaTerzi, abilitato, form, validazione, trasformazione, inoltro, promemoriaAvviso, promemoriaRicevuta, valori);
  }

  public static TipoPendenzaDominio parse(String json) throws ServiceException, ValidationException{
    return (TipoPendenzaDominio) parse(json, TipoPendenzaDominio.class);
  }

  @Override
  public String getJsonIdFilter() {
    return "tipoPendenzaDominio";
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class TipoPendenzaDominio {\n");
    
    sb.append("    idTipoPendenza: ").append(toIndentedString(idTipoPendenza)).append("\n");
    sb.append("    descrizione: ").append(toIndentedString(descrizione)).append("\n");
    sb.append("    tipo: ").append(toIndentedString(tipo)).append("\n");
    sb.append("    codificaIUV: ").append(toIndentedString(codificaIUV)).append("\n");
    sb.append("    pagaTerzi: ").append(toIndentedString(pagaTerzi)).append("\n");
    sb.append("    abilitato: ").append(toIndentedString(abilitato)).append("\n");
    sb.append("    form: ").append(toIndentedString(form)).append("\n");
    sb.append("    validazione: ").append(toIndentedString(validazione)).append("\n");
    sb.append("    trasformazione: ").append(toIndentedString(trasformazione)).append("\n");
    sb.append("    inoltro: ").append(toIndentedString(inoltro)).append("\n");
    sb.append("    promemoriaAvviso: ").append(toIndentedString(promemoriaAvviso)).append("\n");
    sb.append("    promemoriaRicevuta: ").append(toIndentedString(promemoriaRicevuta)).append("\n");
    sb.append("    valori: ").append(toIndentedString(valori)).append("\n");
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



