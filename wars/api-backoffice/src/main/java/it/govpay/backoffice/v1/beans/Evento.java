package it.govpay.backoffice.v1.beans;

import java.util.Objects;

import org.openspcoop2.utils.json.ValidationException;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonValue;
import java.util.Date;
@com.fasterxml.jackson.annotation.JsonPropertyOrder({
"idDominio",
"iuv",
"ccp",
"idPsp",
"tipoVersamento",
"componente",
"categoriaEvento",
"tipoEvento",
"sottotipoEvento",
"identificativoFruitore",
"identificativoErogatore",
"idCanale",
"idStazione",
"parametriRichiesta",
"parametriRisposta",
"dataOraRichiesta",
"dataOraRisposta",
"esito",
"descrizioneEsito",
})
public class Evento extends it.govpay.core.beans.JSONSerializable {
  
  @JsonProperty("idDominio")
  private String idDominio = null;
  
  @JsonProperty("iuv")
  private String iuv = null;
  
  @JsonProperty("ccp")
  private String ccp = null;
  
  @JsonProperty("idPsp")
  private String idPsp = null;
  
  @JsonProperty("tipoVersamento")
  private String tipoVersamento = null;
  
  @JsonProperty("componente")
  private String componente = null;
  
    
  /**
   * Gets or Sets categoriaEvento
   */
  public enum CategoriaEventoEnum {
    
    
        
            
    INTERNO("INTERNO"),
    
            
    INTERFACCIA("INTERFACCIA");
            
        
    

    private String value;

    CategoriaEventoEnum(String value) {
      this.value = value;
    }

    @Override
    @JsonValue
    public String toString() {
      return String.valueOf(this.value);
    }

    public static CategoriaEventoEnum fromValue(String text) {
      for (CategoriaEventoEnum b : CategoriaEventoEnum.values()) {
        if (String.valueOf(b.value).equals(text)) {
          return b;
        }
      }
      return null;
    }
  }

    
    
  @JsonProperty("categoriaEvento")
  private CategoriaEventoEnum categoriaEvento = null;
  
  @JsonProperty("tipoEvento")
  private String tipoEvento = null;
  
  @JsonProperty("sottotipoEvento")
  private String sottotipoEvento = null;
  
  @JsonProperty("identificativoFruitore")
  private String identificativoFruitore = null;
  
  @JsonProperty("identificativoErogatore")
  private String identificativoErogatore = null;
  
  @JsonProperty("idCanale")
  private String idCanale = null;
  
  @JsonProperty("idStazione")
  private String idStazione = null;
  
  @JsonProperty("parametriRichiesta")
  private String parametriRichiesta = null;
  
  @JsonProperty("parametriRisposta")
  private String parametriRisposta = null;
  
  @JsonProperty("dataOraRichiesta")
  private Date dataOraRichiesta = null;
  
  @JsonProperty("dataOraRisposta")
  private Date dataOraRisposta = null;
  
  @JsonProperty("esito")
  private String esito = null;
  
  @JsonProperty("descrizioneEsito")
  private String descrizioneEsito = null;
  
  /**
   * Identificativo ente creditore
   **/
  public Evento idDominio(String idDominio) {
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
   * Identificativo univoco di versamento
   **/
  public Evento iuv(String iuv) {
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
   * Codice contesto di pagamento
   **/
  public Evento ccp(String ccp) {
    this.ccp = ccp;
    return this;
  }

  @JsonProperty("ccp")
  public String getCcp() {
    return this.ccp;
  }
  public void setCcp(String ccp) {
    this.ccp = ccp;
  }

  /**
   * Identificativo del PSP
   **/
  public Evento idPsp(String idPsp) {
    this.idPsp = idPsp;
    return this;
  }

  @JsonProperty("idPsp")
  public String getIdPsp() {
    return this.idPsp;
  }
  public void setIdPsp(String idPsp) {
    this.idPsp = idPsp;
  }

  /**
   * Tipologia di versamento realizzato
   **/
  public Evento tipoVersamento(String tipoVersamento) {
    this.tipoVersamento = tipoVersamento;
    return this;
  }

  @JsonProperty("tipoVersamento")
  public String getTipoVersamento() {
    return this.tipoVersamento;
  }
  public void setTipoVersamento(String tipoVersamento) {
    this.tipoVersamento = tipoVersamento;
  }

  /**
   * Modulo interno che ha emesso l'evento
   **/
  public Evento componente(String componente) {
    this.componente = componente;
    return this;
  }

  @JsonProperty("componente")
  public String getComponente() {
    return this.componente;
  }
  public void setComponente(String componente) {
    this.componente = componente;
  }

  /**
   **/
  public Evento categoriaEvento(CategoriaEventoEnum categoriaEvento) {
    this.categoriaEvento = categoriaEvento;
    return this;
  }

  @JsonProperty("categoriaEvento")
  public CategoriaEventoEnum getCategoriaEvento() {
    return this.categoriaEvento;
  }
  public void setCategoriaEvento(CategoriaEventoEnum categoriaEvento) {
    this.categoriaEvento = categoriaEvento;
  }

  /**
   **/
  public Evento tipoEvento(String tipoEvento) {
    this.tipoEvento = tipoEvento;
    return this;
  }

  @JsonProperty("tipoEvento")
  public String getTipoEvento() {
    return this.tipoEvento;
  }
  public void setTipoEvento(String tipoEvento) {
    this.tipoEvento = tipoEvento;
  }

  /**
   **/
  public Evento sottotipoEvento(String sottotipoEvento) {
    this.sottotipoEvento = sottotipoEvento;
    return this;
  }

  @JsonProperty("sottotipoEvento")
  public String getSottotipoEvento() {
    return sottotipoEvento;
  }
  public void setSottotipoEvento(String sottotipoEvento) {
    this.sottotipoEvento = sottotipoEvento;
  }

  /**
   **/
  public Evento identificativoFruitore(String identificativoFruitore) {
    this.identificativoFruitore = identificativoFruitore;
    return this;
  }

  @JsonProperty("identificativoFruitore")
  public String getIdentificativoFruitore() {
    return this.identificativoFruitore;
  }
  public void setIdentificativoFruitore(String identificativoFruitore) {
    this.identificativoFruitore = identificativoFruitore;
  }

  /**
   **/
  public Evento identificativoErogatore(String identificativoErogatore) {
    this.identificativoErogatore = identificativoErogatore;
    return this;
  }

  @JsonProperty("identificativoErogatore")
  public String getIdentificativoErogatore() {
    return this.identificativoErogatore;
  }
  public void setIdentificativoErogatore(String identificativoErogatore) {
    this.identificativoErogatore = identificativoErogatore;
  }

  /**
   **/
  public Evento idCanale(String idCanale) {
    this.idCanale = idCanale;
    return this;
  }

  @JsonProperty("idCanale")
  public String getIdCanale() {
    return this.idCanale;
  }
  public void setIdCanale(String idCanale) {
    this.idCanale = idCanale;
  }

  /**
   **/
  public Evento idStazione(String idStazione) {
    this.idStazione = idStazione;
    return this;
  }

  @JsonProperty("idStazione")
  public String getIdStazione() {
    return this.idStazione;
  }
  public void setIdStazione(String idStazione) {
    this.idStazione = idStazione;
  }

  /**
   **/
  public Evento parametriRichiesta(String parametriRichiesta) {
    this.parametriRichiesta = parametriRichiesta;
    return this;
  }

  @JsonProperty("parametriRichiesta")
  public String getParametriRichiesta() {
    return this.parametriRichiesta;
  }
  public void setParametriRichiesta(String parametriRichiesta) {
    this.parametriRichiesta = parametriRichiesta;
  }

  /**
   **/
  public Evento parametriRisposta(String parametriRisposta) {
    this.parametriRisposta = parametriRisposta;
    return this;
  }

  @JsonProperty("parametriRisposta")
  public String getParametriRisposta() {
    return this.parametriRisposta;
  }
  public void setParametriRisposta(String parametriRisposta) {
    this.parametriRisposta = parametriRisposta;
  }

  /**
   * Data emissione del messaggio di richiesta
   **/
  public Evento dataOraRichiesta(Date dataOraRichiesta) {
    this.dataOraRichiesta = dataOraRichiesta;
    return this;
  }

  @JsonProperty("dataOraRichiesta")
  public Date getDataOraRichiesta() {
    return this.dataOraRichiesta;
  }
  public void setDataOraRichiesta(Date dataOraRichiesta) {
    this.dataOraRichiesta = dataOraRichiesta;
  }

  /**
   * Data emissione del messaggio di risposta
   **/
  public Evento dataOraRisposta(Date dataOraRisposta) {
    this.dataOraRisposta = dataOraRisposta;
    return this;
  }

  @JsonProperty("dataOraRisposta")
  public Date getDataOraRisposta() {
    return this.dataOraRisposta;
  }
  public void setDataOraRisposta(Date dataOraRisposta) {
    this.dataOraRisposta = dataOraRisposta;
  }

  /**
   **/
  public Evento esito(String esito) {
    this.esito = esito;
    return this;
  }

  @JsonProperty("esito")
  public String getEsito() {
    return this.esito;
  }
  public void setEsito(String esito) {
    this.esito = esito;
  }

  /**
   **/
  public Evento descrizioneEsito(String descrizioneEsito) {
    this.descrizioneEsito = descrizioneEsito;
    return this;
  }

  @JsonProperty("descrizioneEsito")
  public String getDescrizioneEsito() {
    return descrizioneEsito;
  }
  public void setDescrizioneEsito(String descrizioneEsito) {
    this.descrizioneEsito = descrizioneEsito;
  }

  @Override
  public boolean equals(java.lang.Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || this.getClass() != o.getClass()) {
      return false;
    }
    Evento evento = (Evento) o;
    return Objects.equals(this.idDominio, evento.idDominio) &&
        Objects.equals(this.iuv, evento.iuv) &&
        Objects.equals(this.ccp, evento.ccp) &&
        Objects.equals(this.idPsp, evento.idPsp) &&
        Objects.equals(this.tipoVersamento, evento.tipoVersamento) &&
        Objects.equals(this.componente, evento.componente) &&
        Objects.equals(this.categoriaEvento, evento.categoriaEvento) &&
        Objects.equals(this.tipoEvento, evento.tipoEvento) &&
        Objects.equals(this.sottotipoEvento, evento.sottotipoEvento) &&
        Objects.equals(this.identificativoFruitore, evento.identificativoFruitore) &&
        Objects.equals(this.identificativoErogatore, evento.identificativoErogatore) &&
        Objects.equals(this.idCanale, evento.idCanale) &&
        Objects.equals(this.idStazione, evento.idStazione) &&
        Objects.equals(this.parametriRichiesta, evento.parametriRichiesta) &&
        Objects.equals(this.parametriRisposta, evento.parametriRisposta) &&
        Objects.equals(this.dataOraRichiesta, evento.dataOraRichiesta) &&
        Objects.equals(this.dataOraRisposta, evento.dataOraRisposta) &&
        Objects.equals(this.esito, evento.esito) &&
        Objects.equals(descrizioneEsito, evento.descrizioneEsito);
  }

  @Override
  public int hashCode() {
    return Objects.hash(this.idDominio, this.iuv, this.ccp, this.idPsp, this.tipoVersamento, this.componente, this.categoriaEvento, this.tipoEvento, this.sottotipoEvento, this.identificativoFruitore, this.identificativoErogatore, this.idCanale, this.idStazione, this.parametriRichiesta, this.parametriRisposta, this.dataOraRichiesta, this.dataOraRisposta, this.esito, this.descrizioneEsito);
  }

  public static Evento parse(String json) throws org.openspcoop2.generic_project.exception.ServiceException, ValidationException {
    return parse(json, Evento.class);
  }

  @Override
  public String getJsonIdFilter() {
    return "evento";
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class Evento {\n");
    
    sb.append("    idDominio: ").append(this.toIndentedString(this.idDominio)).append("\n");
    sb.append("    iuv: ").append(this.toIndentedString(this.iuv)).append("\n");
    sb.append("    ccp: ").append(this.toIndentedString(this.ccp)).append("\n");
    sb.append("    idPsp: ").append(this.toIndentedString(this.idPsp)).append("\n");
    sb.append("    tipoVersamento: ").append(this.toIndentedString(this.tipoVersamento)).append("\n");
    sb.append("    componente: ").append(this.toIndentedString(this.componente)).append("\n");
    sb.append("    categoriaEvento: ").append(this.toIndentedString(this.categoriaEvento)).append("\n");
    sb.append("    tipoEvento: ").append(this.toIndentedString(this.tipoEvento)).append("\n");
    sb.append("    sottotipoEvento: ").append(toIndentedString(sottotipoEvento)).append("\n");
    sb.append("    identificativoFruitore: ").append(this.toIndentedString(this.identificativoFruitore)).append("\n");
    sb.append("    identificativoErogatore: ").append(this.toIndentedString(this.identificativoErogatore)).append("\n");
    sb.append("    idCanale: ").append(this.toIndentedString(this.idCanale)).append("\n");
    sb.append("    idStazione: ").append(this.toIndentedString(this.idStazione)).append("\n");
    sb.append("    parametriRichiesta: ").append(this.toIndentedString(this.parametriRichiesta)).append("\n");
    sb.append("    parametriRisposta: ").append(this.toIndentedString(this.parametriRisposta)).append("\n");
    sb.append("    dataOraRichiesta: ").append(this.toIndentedString(this.dataOraRichiesta)).append("\n");
    sb.append("    dataOraRisposta: ").append(this.toIndentedString(this.dataOraRisposta)).append("\n");
    sb.append("    esito: ").append(this.toIndentedString(this.esito)).append("\n");
    sb.append("    descrizioneEsito: ").append(toIndentedString(descrizioneEsito)).append("\n");
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



