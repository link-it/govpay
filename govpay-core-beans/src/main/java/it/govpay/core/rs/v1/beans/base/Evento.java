package it.govpay.core.rs.v1.beans.base;

import java.util.Objects;
import org.codehaus.jackson.annotate.JsonProperty;
import org.codehaus.jackson.annotate.JsonValue;
import java.util.Date;
@org.codehaus.jackson.annotate.JsonPropertyOrder({
"idDominio",
"iuv",
"ccp",
"idPsp",
"tipoVersamento",
"componente",
"categoriaEvento",
"tipoEvento",
"identificativoFruitore",
"identificativoErogatore",
"idCanale",
"idStazione",
"parametriRichiesta",
"parametriRisposta",
"dataOraRichiesta",
"dataOraRisposta",
"esito",
})
public class Evento extends it.govpay.core.rs.v1.beans.JSONSerializable {
  
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
      return String.valueOf(value);
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
  
  /**
   * Identificativo ente creditore
   **/
  public Evento idDominio(String idDominio) {
    this.idDominio = idDominio;
    return this;
  }

  @JsonProperty("idDominio")
  public String getIdDominio() {
    return idDominio;
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
    return iuv;
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
    return ccp;
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
    return idPsp;
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
    return tipoVersamento;
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
    return componente;
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
    return categoriaEvento;
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
    return tipoEvento;
  }
  public void setTipoEvento(String tipoEvento) {
    this.tipoEvento = tipoEvento;
  }

  /**
   **/
  public Evento identificativoFruitore(String identificativoFruitore) {
    this.identificativoFruitore = identificativoFruitore;
    return this;
  }

  @JsonProperty("identificativoFruitore")
  public String getIdentificativoFruitore() {
    return identificativoFruitore;
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
    return identificativoErogatore;
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
    return idCanale;
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
    return idStazione;
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
    return parametriRichiesta;
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
    return parametriRisposta;
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
    return dataOraRichiesta;
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
    return dataOraRisposta;
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
    return esito;
  }
  public void setEsito(String esito) {
    this.esito = esito;
  }

  @Override
  public boolean equals(java.lang.Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    Evento evento = (Evento) o;
    return Objects.equals(idDominio, evento.idDominio) &&
        Objects.equals(iuv, evento.iuv) &&
        Objects.equals(ccp, evento.ccp) &&
        Objects.equals(idPsp, evento.idPsp) &&
        Objects.equals(tipoVersamento, evento.tipoVersamento) &&
        Objects.equals(componente, evento.componente) &&
        Objects.equals(categoriaEvento, evento.categoriaEvento) &&
        Objects.equals(tipoEvento, evento.tipoEvento) &&
        Objects.equals(identificativoFruitore, evento.identificativoFruitore) &&
        Objects.equals(identificativoErogatore, evento.identificativoErogatore) &&
        Objects.equals(idCanale, evento.idCanale) &&
        Objects.equals(idStazione, evento.idStazione) &&
        Objects.equals(parametriRichiesta, evento.parametriRichiesta) &&
        Objects.equals(parametriRisposta, evento.parametriRisposta) &&
        Objects.equals(dataOraRichiesta, evento.dataOraRichiesta) &&
        Objects.equals(dataOraRisposta, evento.dataOraRisposta) &&
        Objects.equals(esito, evento.esito);
  }

  @Override
  public int hashCode() {
    return Objects.hash(idDominio, iuv, ccp, idPsp, tipoVersamento, componente, categoriaEvento, tipoEvento, identificativoFruitore, identificativoErogatore, idCanale, idStazione, parametriRichiesta, parametriRisposta, dataOraRichiesta, dataOraRisposta, esito);
  }

  public static Evento parse(String json) throws org.openspcoop2.generic_project.exception.ServiceException {
    return (Evento) parse(json, Evento.class);
  }

  @Override
  public String getJsonIdFilter() {
    return "evento";
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class Evento {\n");
    
    sb.append("    idDominio: ").append(toIndentedString(idDominio)).append("\n");
    sb.append("    iuv: ").append(toIndentedString(iuv)).append("\n");
    sb.append("    ccp: ").append(toIndentedString(ccp)).append("\n");
    sb.append("    idPsp: ").append(toIndentedString(idPsp)).append("\n");
    sb.append("    tipoVersamento: ").append(toIndentedString(tipoVersamento)).append("\n");
    sb.append("    componente: ").append(toIndentedString(componente)).append("\n");
    sb.append("    categoriaEvento: ").append(toIndentedString(categoriaEvento)).append("\n");
    sb.append("    tipoEvento: ").append(toIndentedString(tipoEvento)).append("\n");
    sb.append("    identificativoFruitore: ").append(toIndentedString(identificativoFruitore)).append("\n");
    sb.append("    identificativoErogatore: ").append(toIndentedString(identificativoErogatore)).append("\n");
    sb.append("    idCanale: ").append(toIndentedString(idCanale)).append("\n");
    sb.append("    idStazione: ").append(toIndentedString(idStazione)).append("\n");
    sb.append("    parametriRichiesta: ").append(toIndentedString(parametriRichiesta)).append("\n");
    sb.append("    parametriRisposta: ").append(toIndentedString(parametriRisposta)).append("\n");
    sb.append("    dataOraRichiesta: ").append(toIndentedString(dataOraRichiesta)).append("\n");
    sb.append("    dataOraRisposta: ").append(toIndentedString(dataOraRisposta)).append("\n");
    sb.append("    esito: ").append(toIndentedString(esito)).append("\n");
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



