package it.govpay.pendenze.v2.beans;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Objects;

import org.openspcoop2.utils.json.ValidationException;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

import it.govpay.core.beans.JSONSerializable;
import it.govpay.core.utils.validator.IValidable;
import it.govpay.core.utils.validator.ValidatorFactory;
import it.govpay.core.utils.validator.ValidatoreIdentificativi;
@com.fasterxml.jackson.annotation.JsonPropertyOrder({
"idTipoPendenza",
"idDominio",
"idUnitaOperativa",
"causale",
"soggettoPagatore",
"importo",
"numeroAvviso",
"tassonomia",
"tassonomiaAvviso",
"direzione",
"divisione",
"dataValidita",
"dataScadenza",
"annoRiferimento",
"cartellaPagamento",
"datiAllegati",
"voci",
})
public class NuovaPendenza extends JSONSerializable implements IValidable {
  
  @JsonProperty("idTipoPendenza")
  private String idTipoPendenza = null;
  
  @JsonProperty("idDominio")
  private String idDominio = null;
  
  @JsonProperty("idUnitaOperativa")
  private String idUnitaOperativa = null;
  
  @JsonProperty("causale")
  private String causale = null;
  
  @JsonProperty("soggettoPagatore")
  private Soggetto soggettoPagatore = null;
  
  @JsonProperty("importo")
  private BigDecimal importo = null;
  
  @JsonProperty("numeroAvviso")
  private String numeroAvviso = null;
  
  @JsonProperty("tassonomia")
  private String tassonomia = null;
  
  @JsonIgnore
  private TassonomiaAvviso tassonomiaAvvisoEnum = null;

  @JsonProperty("tassonomiaAvviso")
  private String tassonomiaAvviso = null;
  
  @JsonProperty("direzione")
  private String direzione = null;
  
  @JsonProperty("divisione")
  private String divisione = null;
  
  @JsonProperty("dataValidita")
  private Date dataValidita = null;
  
  @JsonProperty("dataScadenza")
  private Date dataScadenza = null;
  
  @JsonProperty("annoRiferimento")
  private BigDecimal annoRiferimento = null;
  
  @JsonProperty("cartellaPagamento")
  private String cartellaPagamento = null;
  
  @JsonProperty("datiAllegati")
  private Object datiAllegati = null;
  
  @JsonProperty("voci")
  private List<NuovaVocePendenza> voci = new ArrayList<>();
  
  /**
   * Identificativo della tipologia pendenza
   **/
  public NuovaPendenza idTipoPendenza(String idTipoPendenza) {
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
   * Identificativo del dominio creditore
   **/
  public NuovaPendenza idDominio(String idDominio) {
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
   * Identificativo dell'unita' operativa
   **/
  public NuovaPendenza idUnitaOperativa(String idUnitaOperativa) {
    this.idUnitaOperativa = idUnitaOperativa;
    return this;
  }

  @JsonProperty("idUnitaOperativa")
  public String getIdUnitaOperativa() {
    return idUnitaOperativa;
  }
  public void setIdUnitaOperativa(String idUnitaOperativa) {
    this.idUnitaOperativa = idUnitaOperativa;
  }

  /**
   * Descrizione della pendenza
   **/
  public NuovaPendenza causale(String causale) {
    this.causale = causale;
    return this;
  }

  @JsonProperty("causale")
  public String getCausale() {
    return causale;
  }
  public void setCausale(String causale) {
    this.causale = causale;
  }

  /**
   **/
  public NuovaPendenza soggettoPagatore(Soggetto soggettoPagatore) {
    this.soggettoPagatore = soggettoPagatore;
    return this;
  }

  @JsonProperty("soggettoPagatore")
  public Soggetto getSoggettoPagatore() {
    return soggettoPagatore;
  }
  public void setSoggettoPagatore(Soggetto soggettoPagatore) {
    this.soggettoPagatore = soggettoPagatore;
  }

  /**
   * Importo della pendenza. Deve corrispondere alla somma delle singole voci.
   **/
  public NuovaPendenza importo(BigDecimal importo) {
    this.importo = importo;
    return this;
  }

  @JsonProperty("importo")
  public BigDecimal getImporto() {
    return importo;
  }
  public void setImporto(BigDecimal importo) {
    this.importo = importo;
  }

  /**
   * Identificativo univoco versamento, assegnato se pagabile da psp
   **/
  public NuovaPendenza numeroAvviso(String numeroAvviso) {
    this.numeroAvviso = numeroAvviso;
    return this;
  }

  @JsonProperty("numeroAvviso")
  public String getNumeroAvviso() {
    return numeroAvviso;
  }
  public void setNumeroAvviso(String numeroAvviso) {
    this.numeroAvviso = numeroAvviso;
  }

  /**
   * Macro categoria della pendenza secondo la classificazione del creditore
   **/
  public NuovaPendenza tassonomia(String tassonomia) {
    this.tassonomia = tassonomia;
    return this;
  }

  @JsonProperty("tassonomia")
  public String getTassonomia() {
    return tassonomia;
  }
  public void setTassonomia(String tassonomia) {
    this.tassonomia = tassonomia;
  }

  /**
   **/
  public NuovaPendenza tassonomiaAvvisoEnum(TassonomiaAvviso tassonomiaAvviso) {
   this.tassonomiaAvvisoEnum = tassonomiaAvviso;
   return this;
  }

  public TassonomiaAvviso getTassonomiaAvvisoEnum() {
    return this.tassonomiaAvvisoEnum;
  }
  public void setTassonomiaAvvisoEnum(TassonomiaAvviso tassonomiaAvviso) {
    this.tassonomiaAvvisoEnum = tassonomiaAvviso;
  }

  /**
   **/
  public NuovaPendenza tassonomiaAvviso(String tassonomiaAvviso) {
    this.setTassonomiaAvviso(tassonomiaAvviso);
    return this;
  }

  @JsonProperty("tassonomiaAvviso")
  public String getTassonomiaAvviso() {
    return this.tassonomiaAvviso;
  }
  public void setTassonomiaAvviso(String tassonomiaAvviso) {
    this.tassonomiaAvviso = tassonomiaAvviso;
  }

  /**
   * Identificativo della direzione interna all'ente creditore
   **/
  public NuovaPendenza direzione(String direzione) {
    this.direzione = direzione;
    return this;
  }

  @JsonProperty("direzione")
  public String getDirezione() {
    return direzione;
  }
  public void setDirezione(String direzione) {
    this.direzione = direzione;
  }

  /**
   * Identificativo della divisione interna all'ente creditore
   **/
  public NuovaPendenza divisione(String divisione) {
    this.divisione = divisione;
    return this;
  }

  @JsonProperty("divisione")
  public String getDivisione() {
    return divisione;
  }
  public void setDivisione(String divisione) {
    this.divisione = divisione;
  }

  /**
   * Data di validita dei dati della pendenza, decorsa la quale la pendenza può subire variazioni.
   **/
  public NuovaPendenza dataValidita(Date dataValidita) {
    this.dataValidita = dataValidita;
    return this;
  }

  @JsonProperty("dataValidita")
  public Date getDataValidita() {
    return dataValidita;
  }
  public void setDataValidita(Date dataValidita) {
    this.dataValidita = dataValidita;
  }

  /**
   * Data di scadenza della pendenza, decorsa la quale non è più pagabile.
   **/
  public NuovaPendenza dataScadenza(Date dataScadenza) {
    this.dataScadenza = dataScadenza;
    return this;
  }

  @JsonProperty("dataScadenza")
  public Date getDataScadenza() {
    return dataScadenza;
  }
  public void setDataScadenza(Date dataScadenza) {
    this.dataScadenza = dataScadenza;
  }

  /**
   * Anno di riferimento della pendenza
   **/
  public NuovaPendenza annoRiferimento(BigDecimal annoRiferimento) {
    this.annoRiferimento = annoRiferimento;
    return this;
  }

  @JsonProperty("annoRiferimento")
  public BigDecimal getAnnoRiferimento() {
    return annoRiferimento;
  }
  public void setAnnoRiferimento(BigDecimal annoRiferimento) {
    this.annoRiferimento = annoRiferimento;
  }

  /**
   * Identificativo della cartella di pagamento a cui afferisce la pendenza
   **/
  public NuovaPendenza cartellaPagamento(String cartellaPagamento) {
    this.cartellaPagamento = cartellaPagamento;
    return this;
  }

  @JsonProperty("cartellaPagamento")
  public String getCartellaPagamento() {
    return cartellaPagamento;
  }
  public void setCartellaPagamento(String cartellaPagamento) {
    this.cartellaPagamento = cartellaPagamento;
  }

  /**
   * Dati applicativi allegati dal gestionale secondo un formato proprietario.
   **/
  public NuovaPendenza datiAllegati(Object datiAllegati) {
    this.datiAllegati = datiAllegati;
    return this;
  }

  @JsonProperty("datiAllegati")
  public Object getDatiAllegati() {
    return datiAllegati;
  }
  public void setDatiAllegati(Object datiAllegati) {
    this.datiAllegati = datiAllegati;
  }

  /**
   **/
  public NuovaPendenza voci(List<NuovaVocePendenza> voci) {
    this.voci = voci;
    return this;
  }

  @JsonProperty("voci")
  public List<NuovaVocePendenza> getVoci() {
    return voci;
  }
  public void setVoci(List<NuovaVocePendenza> voci) {
    this.voci = voci;
  }

  @Override
  public boolean equals(java.lang.Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    NuovaPendenza nuovaPendenza = (NuovaPendenza) o;
    return Objects.equals(idTipoPendenza, nuovaPendenza.idTipoPendenza) &&
        Objects.equals(idDominio, nuovaPendenza.idDominio) &&
        Objects.equals(idUnitaOperativa, nuovaPendenza.idUnitaOperativa) &&
        Objects.equals(causale, nuovaPendenza.causale) &&
        Objects.equals(soggettoPagatore, nuovaPendenza.soggettoPagatore) &&
        Objects.equals(importo, nuovaPendenza.importo) &&
        Objects.equals(numeroAvviso, nuovaPendenza.numeroAvviso) &&
        Objects.equals(tassonomia, nuovaPendenza.tassonomia) &&
        Objects.equals(tassonomiaAvviso, nuovaPendenza.tassonomiaAvviso) &&
        Objects.equals(direzione, nuovaPendenza.direzione) &&
        Objects.equals(divisione, nuovaPendenza.divisione) &&
        Objects.equals(dataValidita, nuovaPendenza.dataValidita) &&
        Objects.equals(dataScadenza, nuovaPendenza.dataScadenza) &&
        Objects.equals(annoRiferimento, nuovaPendenza.annoRiferimento) &&
        Objects.equals(cartellaPagamento, nuovaPendenza.cartellaPagamento) &&
        Objects.equals(datiAllegati, nuovaPendenza.datiAllegati) &&
        Objects.equals(voci, nuovaPendenza.voci);
  }

  @Override
  public int hashCode() {
    return Objects.hash(idTipoPendenza, idDominio, idUnitaOperativa, causale, soggettoPagatore, importo, numeroAvviso, tassonomia, tassonomiaAvviso, direzione, divisione, dataValidita, dataScadenza, annoRiferimento, cartellaPagamento, datiAllegati, voci);
  }

  public static NuovaPendenza parse(String json) throws org.openspcoop2.generic_project.exception.ServiceException, ValidationException {
    return parse(json, NuovaPendenza.class);
  }

  @Override
  public String getJsonIdFilter() {
    return "nuovaPendenza";
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class NuovaPendenza {\n");
    
    sb.append("    idTipoPendenza: ").append(toIndentedString(idTipoPendenza)).append("\n");
    sb.append("    idDominio: ").append(toIndentedString(idDominio)).append("\n");
    sb.append("    idUnitaOperativa: ").append(toIndentedString(idUnitaOperativa)).append("\n");
    sb.append("    causale: ").append(toIndentedString(causale)).append("\n");
    sb.append("    soggettoPagatore: ").append(toIndentedString(soggettoPagatore)).append("\n");
    sb.append("    importo: ").append(toIndentedString(importo)).append("\n");
    sb.append("    numeroAvviso: ").append(toIndentedString(numeroAvviso)).append("\n");
    sb.append("    tassonomia: ").append(toIndentedString(tassonomia)).append("\n");
    sb.append("    tassonomiaAvviso: ").append(toIndentedString(tassonomiaAvviso)).append("\n");
    sb.append("    direzione: ").append(toIndentedString(direzione)).append("\n");
    sb.append("    divisione: ").append(toIndentedString(divisione)).append("\n");
    sb.append("    dataValidita: ").append(toIndentedString(dataValidita)).append("\n");
    sb.append("    dataScadenza: ").append(toIndentedString(dataScadenza)).append("\n");
    sb.append("    annoRiferimento: ").append(toIndentedString(annoRiferimento)).append("\n");
    sb.append("    cartellaPagamento: ").append(toIndentedString(cartellaPagamento)).append("\n");
    sb.append("    datiAllegati: ").append(toIndentedString(datiAllegati)).append("\n");
    sb.append("    voci: ").append(toIndentedString(voci)).append("\n");
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
		
		ValidatoreIdentificativi validatoreId = ValidatoreIdentificativi.newInstance();

		validatoreId.validaIdDominio("idDominio", this.idDominio);
		if(this.idUnitaOperativa != null)
			validatoreId.validaIdUO("idUnitaOperativa", this.idUnitaOperativa);
		
		validatoreId.validaIdTipoVersamento("idTipoPendenza", this.idTipoPendenza);
		vf.getValidator("causale", this.causale).notNull().minLength(1).maxLength(140);
		vf.getValidator("soggettoPagatore", this.soggettoPagatore).notNull().validateFields();
		vf.getValidator("importo", this.importo).notNull().minOrEquals(BigDecimal.ZERO).maxOrEquals(BigDecimal.valueOf(999999.99)).checkDecimalDigits();
		vf.getValidator("numeroAvviso", this.numeroAvviso).pattern("[0-9]{18}");
		vf.getValidator("dataValidita", this.dataValidita);
		vf.getValidator("dataScadenza", this.dataScadenza);
		if(this.annoRiferimento != null)
			vf.getValidator("annoRiferimento", this.annoRiferimento.toBigInteger().toString()).pattern("[0-9]{4}");
		vf.getValidator("cartellaPagamento", this.cartellaPagamento).minLength(1).maxLength(35);
		vf.getValidator("direzione", this.direzione).minLength(1).maxLength(35);
		vf.getValidator("divisione", this.divisione).minLength(1).maxLength(35);
		vf.getValidator("tassonomia", this.tassonomia).minLength(1).maxLength(35);
		vf.getValidator("voci", this.voci).notNull().minItems(1).maxItems(5).validateObjects();
	}
}



