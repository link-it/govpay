package it.govpay.backoffice.v1.beans;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Objects;

import org.openspcoop2.utils.json.ValidationException;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

import it.govpay.core.utils.validator.IValidable;
import it.govpay.core.utils.validator.ValidatorFactory;
import it.govpay.core.utils.validator.ValidatoreIdentificativi;
@com.fasterxml.jackson.annotation.JsonPropertyOrder({
	"idDominio",
	"idUnitaOperativa",
	"idTipoPendenza",
	"nome",
	"causale",
	"soggettoPagatore",
	"importo",
	"numeroAvviso",
	"dataCaricamento",
	"dataValidita",
	"dataScadenza",
	"annoRiferimento",
	"cartellaPagamento",
	"datiAllegati",
	"tassonomia",
	"tassonomiaAvviso",
	"direzione",
	"divisione",
	"voci",
	"idA2A",
	"idPendenza",
	"idDebitore",
	"dati",
})
public class PendenzaPost extends it.govpay.core.beans.JSONSerializable implements IValidable {
  
  @JsonProperty("idDominio")
  private String idDominio = null;
  
  @JsonProperty("idUnitaOperativa")
  private String idUnitaOperativa = null;
  
  @JsonProperty("idTipoPendenza")
  private String idTipoPendenza = null;
  
  @JsonProperty("nome")
  private String nome = null;
  
  @JsonProperty("causale")
  private String causale = null;
  
  @JsonProperty("soggettoPagatore")
  private Soggetto soggettoPagatore = null;
  
  @JsonProperty("importo")
  private BigDecimal importo = null;
  
  @JsonProperty("numeroAvviso")
  private String numeroAvviso = null;
  
  @JsonProperty("dataCaricamento")
  private Date dataCaricamento = null;
  
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
  
  @JsonProperty("tassonomia")
  private String tassonomia = null;
  
  @JsonProperty("tassonomiaAvviso")
  private TassonomiaAvviso tassonomiaAvviso = null;
  
  @JsonProperty("direzione")
  private String direzione = null;
  
  @JsonProperty("divisione")
  private String divisione = null;
  
  @JsonProperty("voci")
  private List<VocePendenza> voci = new ArrayList<>();
  
  @JsonProperty("idA2A")
  private String idA2A = null;
  
  @JsonProperty("idPendenza")
  private String idPendenza = null;

	@JsonProperty("idDebitore")
	private String idDebitore = null;
	
	@JsonProperty("dati")
	private Object dati = null;
  
  /**
   * Identificativo del dominio creditore
   **/
  public PendenzaPost idDominio(String idDominio) {
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
   * Identificativo dell'unita' operativa
   **/
  public PendenzaPost idUnitaOperativa(String idUnitaOperativa) {
    this.idUnitaOperativa = idUnitaOperativa;
    return this;
  }

  @JsonProperty("idUnitaOperativa")
  public String getIdUnitaOperativa() {
    return this.idUnitaOperativa;
  }
  public void setIdUnitaOperativa(String idUnitaOperativa) {
    this.idUnitaOperativa = idUnitaOperativa;
  }

  /**
   * Identificativo della tipologia pendenza
   **/
  public PendenzaPost idTipoPendenza(String idTipoPendenza) {
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
   * Nome della pendenza da visualizzare sui portali di pagamento e console di gestione.
   **/
  public PendenzaPost nome(String nome) {
    this.nome = nome;
    return this;
  }

  @JsonProperty("nome")
  public String getNome() {
    return this.nome;
  }
  public void setNome(String nome) {
    this.nome = nome;
  }

  /**
   * Descrizione da inserire nell'avviso di pagamento
   **/
  public PendenzaPost causale(String causale) {
    this.causale = causale;
    return this;
  }

  @JsonProperty("causale")
  public String getCausale() {
    return this.causale;
  }
  public void setCausale(String causale) {
    this.causale = causale;
  }

  /**
   **/
  public PendenzaPost soggettoPagatore(Soggetto soggettoPagatore) {
    this.soggettoPagatore = soggettoPagatore;
    return this;
  }

  @JsonProperty("soggettoPagatore")
  public Soggetto getSoggettoPagatore() {
    return this.soggettoPagatore;
  }
  public void setSoggettoPagatore(Soggetto soggettoPagatore) {
    this.soggettoPagatore = soggettoPagatore;
  }

  /**
   * Importo della pendenza. Deve corrispondere alla somma delle singole voci.
   **/
  public PendenzaPost importo(BigDecimal importo) {
    this.importo = importo;
    return this;
  }

  @JsonProperty("importo")
  public BigDecimal getImporto() {
    return this.importo;
  }
  public void setImporto(BigDecimal importo) {
    this.importo = importo;
  }

  /**
   * Identificativo univoco versamento, assegnato se pagabile da psp
   **/
  public PendenzaPost numeroAvviso(String numeroAvviso) {
    this.numeroAvviso = numeroAvviso;
    return this;
  }

  @JsonProperty("numeroAvviso")
  public String getNumeroAvviso() {
    return this.numeroAvviso;
  }
  public void setNumeroAvviso(String numeroAvviso) {
    this.numeroAvviso = numeroAvviso;
  }

  /**
   * Data di emissione della pendenza
   **/
  public PendenzaPost dataCaricamento(Date dataCaricamento) {
    this.dataCaricamento = dataCaricamento;
    return this;
  }

  @JsonProperty("dataCaricamento")
  public Date getDataCaricamento() {
    return this.dataCaricamento;
  }
  public void setDataCaricamento(Date dataCaricamento) {
    this.dataCaricamento = dataCaricamento;
  }

  /**
   * Data di validita dei dati della pendenza, decorsa la quale la pendenza può subire variazioni.
   **/
  public PendenzaPost dataValidita(Date dataValidita) {
    this.dataValidita = dataValidita;
    return this;
  }

  @JsonProperty("dataValidita")
  public Date getDataValidita() {
    return this.dataValidita;
  }
  public void setDataValidita(Date dataValidita) {
    this.dataValidita = dataValidita;
  }

  /**
   * Data di scadenza della pendenza, decorsa la quale non è più pagabile.
   **/
  public PendenzaPost dataScadenza(Date dataScadenza) {
    this.dataScadenza = dataScadenza;
    return this;
  }

  @JsonProperty("dataScadenza")
  public Date getDataScadenza() {
    return this.dataScadenza;
  }
  public void setDataScadenza(Date dataScadenza) {
    this.dataScadenza = dataScadenza;
  }

  /**
   * Anno di riferimento della pendenza
   **/
  public PendenzaPost annoRiferimento(BigDecimal annoRiferimento) {
    this.annoRiferimento = annoRiferimento;
    return this;
  }

  @JsonProperty("annoRiferimento")
  public BigDecimal getAnnoRiferimento() {
    return this.annoRiferimento;
  }
  public void setAnnoRiferimento(BigDecimal annoRiferimento) {
    this.annoRiferimento = annoRiferimento;
  }

  /**
   * Identificativo della cartella di pagamento a cui afferisce la pendenza
   **/
  public PendenzaPost cartellaPagamento(String cartellaPagamento) {
    this.cartellaPagamento = cartellaPagamento;
    return this;
  }

  @JsonProperty("cartellaPagamento")
  public String getCartellaPagamento() {
    return this.cartellaPagamento;
  }
  public void setCartellaPagamento(String cartellaPagamento) {
    this.cartellaPagamento = cartellaPagamento;
  }

  /**
   * Dati applicativi allegati dal gestionale secondo un formato proprietario.
   **/
  public PendenzaPost datiAllegati(Object datiAllegati) {
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
   * Macro categoria della pendenza secondo la classificazione del creditore
   **/
  public PendenzaPost tassonomia(String tassonomia) {
    this.tassonomia = tassonomia;
    return this;
  }

  @JsonProperty("tassonomia")
  public String getTassonomia() {
    return this.tassonomia;
  }
  public void setTassonomia(String tassonomia) {
    this.tassonomia = tassonomia;
  }

  /**
   **/
  public PendenzaPost tassonomiaAvviso(TassonomiaAvviso tassonomiaAvviso) {
    this.tassonomiaAvviso = tassonomiaAvviso;
    return this;
  }

  @JsonProperty("tassonomiaAvviso")
  public String getTassonomiaAvviso() {
	  if(this.tassonomiaAvviso != null)
		  return this.tassonomiaAvviso.toString();
	  
	  return null;
  }
  public void setTassonomiaAvviso(String tassonomiaAvviso) {
	  if(tassonomiaAvviso != null)
		  this.tassonomiaAvviso = TassonomiaAvviso.fromValue(tassonomiaAvviso);
  }
  
  @JsonIgnore
  public TassonomiaAvviso getTassonomiaAvvisoEnum() {
    return this.tassonomiaAvviso;
  }
  public void setTassonomiaAvviso(TassonomiaAvviso tassonomiaAvviso) {
    this.tassonomiaAvviso = tassonomiaAvviso;
  }

  /**
   * Identificativo della direzione interna all'ente creditore
   **/
  public PendenzaPost direzione(String direzione) {
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
  public PendenzaPost divisione(String divisione) {
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
   **/
  public PendenzaPost voci(List<VocePendenza> voci) {
    this.voci = voci;
    return this;
  }

  @JsonProperty("voci")
  public List<VocePendenza> getVoci() {
    return this.voci;
  }
  public void setVoci(List<VocePendenza> voci) {
    this.voci = voci;
  }

  /**
   * Identificativo del gestionale responsabile della pendenza
   **/
  public PendenzaPost idA2A(String idA2A) {
    this.idA2A = idA2A;
    return this;
  }

  @JsonProperty("idA2A")
  public String getIdA2A() {
    return this.idA2A;
  }
  public void setIdA2A(String idA2A) {
    this.idA2A = idA2A;
  }

  /**
   * Identificativo della pendenza nel gestionale responsabile
   **/
  public PendenzaPost idPendenza(String idPendenza) {
    this.idPendenza = idPendenza;
    return this;
  }

  @JsonProperty("idPendenza")
  public String getIdPendenza() {
    return this.idPendenza;
  }
  public void setIdPendenza(String idPendenza) {
    this.idPendenza = idPendenza;
  }

  /**
   * Identificativo del soggetto debitore  della pendenza riferita dall'avviso
   **/
  public PendenzaPost idDebitore(String idDebitore) {
    this.idDebitore = idDebitore;
    return this;
  }

  @JsonProperty("idDebitore")
  public String getIdDebitore() {
    return idDebitore;
  }
  public void setIdDebitore(String idDebitore) {
    this.idDebitore = idDebitore;
  }
  
  /**
	 * Dati applicativi allegati dal gestionale secondo un formato proprietario.
	 **/
	public PendenzaPost dati(Object dati) {
		this.dati = dati;
		return this;
	}

	@JsonProperty("dati")
	public Object getDati() {
		return this.dati;
	}
	public void setDati(Object dati) {
		this.dati = dati;
	}

  @Override
  public boolean equals(java.lang.Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || this.getClass() != o.getClass()) {
      return false;
    }
    PendenzaPost pendenzaPost = (PendenzaPost) o;
    return Objects.equals(idDominio, pendenzaPost.idDominio) &&
        Objects.equals(idUnitaOperativa, pendenzaPost.idUnitaOperativa) &&
        Objects.equals(idTipoPendenza, pendenzaPost.idTipoPendenza) &&
        Objects.equals(nome, pendenzaPost.nome) &&
        Objects.equals(causale, pendenzaPost.causale) &&
        Objects.equals(soggettoPagatore, pendenzaPost.soggettoPagatore) &&
        Objects.equals(importo, pendenzaPost.importo) &&
        Objects.equals(numeroAvviso, pendenzaPost.numeroAvviso) &&
        Objects.equals(dataCaricamento, pendenzaPost.dataCaricamento) &&
        Objects.equals(dataValidita, pendenzaPost.dataValidita) &&
        Objects.equals(dataScadenza, pendenzaPost.dataScadenza) &&
        Objects.equals(annoRiferimento, pendenzaPost.annoRiferimento) &&
        Objects.equals(cartellaPagamento, pendenzaPost.cartellaPagamento) &&
        Objects.equals(datiAllegati, pendenzaPost.datiAllegati) &&
        Objects.equals(tassonomia, pendenzaPost.tassonomia) &&
        Objects.equals(tassonomiaAvviso, pendenzaPost.tassonomiaAvviso) &&
        Objects.equals(direzione, pendenzaPost.direzione) &&
        Objects.equals(divisione, pendenzaPost.divisione) &&
        Objects.equals(voci, pendenzaPost.voci) &&
        Objects.equals(idA2A, pendenzaPost.idA2A) &&
        Objects.equals(idPendenza, pendenzaPost.idPendenza) &&
        Objects.equals(idDebitore, pendenzaPost.idDebitore) &&
        Objects.equals(this.dati, pendenzaPost.dati);
  }

  @Override
  public int hashCode() {
    return Objects.hash(idDominio, idUnitaOperativa, idTipoPendenza, nome, causale, soggettoPagatore, importo, numeroAvviso, dataCaricamento, dataValidita, dataScadenza, annoRiferimento, cartellaPagamento, datiAllegati, tassonomia, tassonomiaAvviso, direzione, divisione, voci, idA2A, idPendenza, this.idDebitore, this.dati);
  }

  public static PendenzaPost parse(String json) throws org.openspcoop2.generic_project.exception.ServiceException, ValidationException {
    return parse(json, PendenzaPost.class);
  }

  @Override
  public String getJsonIdFilter() {
    return "pendenzaPost";
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class PendenzaPost {\n");
    
    sb.append("    idDominio: ").append(toIndentedString(idDominio)).append("\n");
    sb.append("    idUnitaOperativa: ").append(toIndentedString(idUnitaOperativa)).append("\n");
    sb.append("    idTipoPendenza: ").append(toIndentedString(idTipoPendenza)).append("\n");
    sb.append("    nome: ").append(toIndentedString(nome)).append("\n");
    sb.append("    causale: ").append(toIndentedString(causale)).append("\n");
    sb.append("    soggettoPagatore: ").append(toIndentedString(soggettoPagatore)).append("\n");
    sb.append("    importo: ").append(toIndentedString(importo)).append("\n");
    sb.append("    numeroAvviso: ").append(toIndentedString(numeroAvviso)).append("\n");
    sb.append("    dataCaricamento: ").append(toIndentedString(dataCaricamento)).append("\n");
    sb.append("    dataValidita: ").append(toIndentedString(dataValidita)).append("\n");
    sb.append("    dataScadenza: ").append(toIndentedString(dataScadenza)).append("\n");
    sb.append("    annoRiferimento: ").append(toIndentedString(annoRiferimento)).append("\n");
    sb.append("    cartellaPagamento: ").append(toIndentedString(cartellaPagamento)).append("\n");
    sb.append("    datiAllegati: ").append(toIndentedString(datiAllegati)).append("\n");
    sb.append("    tassonomia: ").append(toIndentedString(tassonomia)).append("\n");
    sb.append("    tassonomiaAvviso: ").append(toIndentedString(tassonomiaAvviso)).append("\n");
    sb.append("    direzione: ").append(toIndentedString(direzione)).append("\n");
    sb.append("    divisione: ").append(toIndentedString(divisione)).append("\n");
    sb.append("    voci: ").append(toIndentedString(voci)).append("\n");
    sb.append("    idA2A: ").append(toIndentedString(idA2A)).append("\n");
    sb.append("    idPendenza: ").append(toIndentedString(idPendenza)).append("\n");
    sb.append("    idDebitore: ").append(toIndentedString(idDebitore)).append("\n");
    sb.append("    dati: ").append(this.toIndentedString(dati)).append("\n");
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
  public void validate() throws org.openspcoop2.utils.json.ValidationException {
		validazione(true);
	}
  
  public void validaPendenzaTracciato() throws org.openspcoop2.utils.json.ValidationException {
		validazione(false);
	}

	private void validazione(boolean validaDominio) throws ValidationException {
		ValidatorFactory vf = ValidatorFactory.newInstance();

		ValidatoreIdentificativi validatoreId = ValidatoreIdentificativi.newInstance();
		
		validatoreId.validaIdApplicazione("idA2A", this.idA2A);
		validatoreId.validaIdPendenza("idPendenza", this.idPendenza);
	
		if(validaDominio)
			validatoreId.validaIdDominio("idDominio", this.idDominio);
		
		if(this.idUnitaOperativa != null)
			validatoreId.validaIdUO("idUnitaOperativa", this.idUnitaOperativa);
		
		if(this.idTipoPendenza != null)
			validatoreId.validaIdTipoVersamento("idTipoPendenza", this.idTipoPendenza);
		vf.getValidator("nome", this.nome).minLength(1).maxLength(35);
		vf.getValidator("causale", this.causale).notNull().minLength(1).maxLength(140);
		vf.getValidator("soggettoPagatore", this.soggettoPagatore).notNull().validateFields();
		vf.getValidator("importo", this.importo).minOrEquals(BigDecimal.ZERO).maxOrEquals(BigDecimal.valueOf(999999.99)).checkDecimalDigits();
		vf.getValidator("numeroAvviso", this.numeroAvviso).pattern("[0-9]{18}");
		vf.getValidator("dataValidita", this.dataValidita);
		vf.getValidator("dataScadenza", this.dataScadenza);
		if(this.annoRiferimento != null)
			vf.getValidator("annoRiferimento", this.annoRiferimento.toBigInteger().toString()).pattern("[0-9]{4}");
		vf.getValidator("cartellaPagamento", this.cartellaPagamento).minLength(1).maxLength(35);
		vf.getValidator("voci", this.voci).notNull().minItems(1).maxItems(5).validateObjects();
		
		if(this.direzione != null)
			validatoreId.validaIdDirezione("direzione",this.direzione);
		if(this.divisione != null)
			validatoreId.validaIdDivisione("divisione",this.divisione);
	}
}



