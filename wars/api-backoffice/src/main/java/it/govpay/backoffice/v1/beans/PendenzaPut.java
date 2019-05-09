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
"idDominio",
"idUnitaOperativa",
"voci",
})
public class PendenzaPut extends it.govpay.core.beans.JSONSerializable implements IValidable {
  
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
  
  @JsonIgnore
  private TassonomiaAvviso tassonomiaAvvisoEnum = null;

  @JsonProperty("tassonomiaAvviso")
  private String tassonomiaAvviso = null;
  
  @JsonProperty("idDominio")
  private String idDominio = null;
  
  @JsonProperty("idUnitaOperativa")
  private String idUnitaOperativa = null;
  
  @JsonProperty("voci")
  private List<VocePendenza> voci = new ArrayList<>();
  
  /**
   * Nome della pendenza da visualizzare sui portali di pagamento e console di gestione.
   **/
  public PendenzaPut nome(String nome) {
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
  public PendenzaPut causale(String causale) {
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
  public PendenzaPut soggettoPagatore(Soggetto soggettoPagatore) {
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
  public PendenzaPut importo(BigDecimal importo) {
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
   * Numero avviso, assegnato se pagabile da psp
   **/
  public PendenzaPut numeroAvviso(String numeroAvviso) {
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
  public PendenzaPut dataCaricamento(Date dataCaricamento) {
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
  public PendenzaPut dataValidita(Date dataValidita) {
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
  public PendenzaPut dataScadenza(Date dataScadenza) {
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
  public PendenzaPut annoRiferimento(BigDecimal annoRiferimento) {
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
  public PendenzaPut cartellaPagamento(String cartellaPagamento) {
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
  public PendenzaPut datiAllegati(Object datiAllegati) {
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
  public PendenzaPut tassonomia(String tassonomia) {
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
	public PendenzaPut tassonomiaAvvisoEnum(TassonomiaAvviso tassonomiaAvviso) {
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
	public PendenzaPut tassonomiaAvviso(String tassonomiaAvviso) {
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
   * Identificativo del dominio creditore
   **/
  public PendenzaPut idDominio(String idDominio) {
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
  public PendenzaPut idUnitaOperativa(String idUnitaOperativa) {
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
   **/
  public PendenzaPut voci(List<VocePendenza> voci) {
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

  @Override
  public boolean equals(java.lang.Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || this.getClass() != o.getClass()) {
      return false;
    }
    PendenzaPut pendenzaPut = (PendenzaPut) o;
    return Objects.equals(this.nome, pendenzaPut.nome) &&
        Objects.equals(this.causale, pendenzaPut.causale) &&
        Objects.equals(this.soggettoPagatore, pendenzaPut.soggettoPagatore) &&
        Objects.equals(this.importo, pendenzaPut.importo) &&
        Objects.equals(this.numeroAvviso, pendenzaPut.numeroAvviso) &&
        Objects.equals(this.dataCaricamento, pendenzaPut.dataCaricamento) &&
        Objects.equals(this.dataValidita, pendenzaPut.dataValidita) &&
        Objects.equals(this.dataScadenza, pendenzaPut.dataScadenza) &&
        Objects.equals(this.annoRiferimento, pendenzaPut.annoRiferimento) &&
        Objects.equals(this.cartellaPagamento, pendenzaPut.cartellaPagamento) &&
        Objects.equals(this.datiAllegati, pendenzaPut.datiAllegati) &&
        Objects.equals(this.tassonomia, pendenzaPut.tassonomia) &&
        Objects.equals(this.tassonomiaAvviso, pendenzaPut.tassonomiaAvviso) &&
        Objects.equals(this.idDominio, pendenzaPut.idDominio) &&
        Objects.equals(this.idUnitaOperativa, pendenzaPut.idUnitaOperativa) &&
        Objects.equals(this.voci, pendenzaPut.voci);
  }

  @Override
  public int hashCode() {
    return Objects.hash(this.nome, this.causale, this.soggettoPagatore, this.importo, this.numeroAvviso, this.dataCaricamento, this.dataValidita, this.dataScadenza, this.annoRiferimento, this.cartellaPagamento, this.datiAllegati, this.tassonomia, this.tassonomiaAvviso, this.idDominio, this.idUnitaOperativa, this.voci);
  }

  public static PendenzaPut parse(String json) throws org.openspcoop2.generic_project.exception.ServiceException, ValidationException {
    return parse(json, PendenzaPut.class);
  }

  @Override
  public String getJsonIdFilter() {
    return "pendenzaPut";
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class PendenzaPut {\n");
    
    sb.append("    nome: ").append(this.toIndentedString(this.nome)).append("\n");
    sb.append("    causale: ").append(this.toIndentedString(this.causale)).append("\n");
    sb.append("    soggettoPagatore: ").append(this.toIndentedString(this.soggettoPagatore)).append("\n");
    sb.append("    importo: ").append(this.toIndentedString(this.importo)).append("\n");
    sb.append("    numeroAvviso: ").append(this.toIndentedString(this.numeroAvviso)).append("\n");
    sb.append("    dataCaricamento: ").append(this.toIndentedString(this.dataCaricamento)).append("\n");
    sb.append("    dataValidita: ").append(this.toIndentedString(this.dataValidita)).append("\n");
    sb.append("    dataScadenza: ").append(this.toIndentedString(this.dataScadenza)).append("\n");
    sb.append("    annoRiferimento: ").append(this.toIndentedString(this.annoRiferimento)).append("\n");
    sb.append("    cartellaPagamento: ").append(this.toIndentedString(this.cartellaPagamento)).append("\n");
    sb.append("    datiAllegati: ").append(this.toIndentedString(this.datiAllegati)).append("\n");
    sb.append("    tassonomia: ").append(this.toIndentedString(this.tassonomia)).append("\n");
    sb.append("    tassonomiaAvviso: ").append(this.toIndentedString(this.tassonomiaAvviso)).append("\n");
    sb.append("    idDominio: ").append(this.toIndentedString(this.idDominio)).append("\n");
    sb.append("    idUnitaOperativa: ").append(this.toIndentedString(this.idUnitaOperativa)).append("\n");
    sb.append("    voci: ").append(this.toIndentedString(this.voci)).append("\n");
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
	}
}



