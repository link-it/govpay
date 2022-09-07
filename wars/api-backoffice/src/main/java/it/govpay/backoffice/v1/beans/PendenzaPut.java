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
import it.govpay.core.utils.validator.ValidatoreUtils;
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
"direzione",
"divisione",
"documento",
"dataNotificaAvviso",
"dataPromemoriaScadenza",
"proprieta",
"idDominio",
"idUnitaOperativa",
"idTipoPendenza",
"voci",
"allegati",
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
  
  @JsonProperty("direzione")
  private String direzione = null;
  
  @JsonProperty("divisione")
  private String divisione = null;
  
  @JsonProperty("documento")
  private Documento documento = null;
  
  @JsonProperty("dataNotificaAvviso")
  private Date dataNotificaAvviso = null;
  
  @JsonProperty("dataPromemoriaScadenza")
  private Date dataPromemoriaScadenza = null;
  
  @JsonProperty("proprieta")
  private ProprietaPendenza proprieta = null;
  
  @JsonProperty("idDominio")
  private String idDominio = null;
  
  @JsonProperty("idUnitaOperativa")
  private String idUnitaOperativa = null;
  
  @JsonProperty("idTipoPendenza")
  private String idTipoPendenza = null;
  
  @JsonProperty("voci")
  private List<NuovaVocePendenza> voci = new ArrayList<NuovaVocePendenza>();
  
  @JsonProperty("allegati")
  private List<NuovoAllegatoPendenza> allegati = null;
  
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
  public PendenzaPut divisione(String divisione) {
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
  public PendenzaPut documento(Documento documento) {
    this.documento = documento;
    return this;
  }

  @JsonProperty("documento")
  public Documento getDocumento() {
    return documento;
  }
  public void setDocumento(Documento documento) {
    this.documento = documento;
  }

  /**
   * Data in cui inviare il promemoria di pagamento.
   **/
  public PendenzaPut dataNotificaAvviso(Date dataNotificaAvviso) {
    this.dataNotificaAvviso = dataNotificaAvviso;
    return this;
  }

  @JsonProperty("dataNotificaAvviso")
  public Date getDataNotificaAvviso() {
    return dataNotificaAvviso;
  }
  public void setDataNotificaAvviso(Date dataNotificaAvviso) {
    this.dataNotificaAvviso = dataNotificaAvviso;
  }

  /**
   * Data in cui inviare il promemoria di scadenza della pendenza.
   **/
  public PendenzaPut dataPromemoriaScadenza(Date dataPromemoriaScadenza) {
    this.dataPromemoriaScadenza = dataPromemoriaScadenza;
    return this;
  }

  @JsonProperty("dataPromemoriaScadenza")
  public Date getDataPromemoriaScadenza() {
    return dataPromemoriaScadenza;
  }
  public void setDataPromemoriaScadenza(Date dataPromemoriaScadenza) {
    this.dataPromemoriaScadenza = dataPromemoriaScadenza;
  }

  /**
   **/
  public PendenzaPut proprieta(ProprietaPendenza proprieta) {
    this.proprieta = proprieta;
    return this;
  }

  @JsonProperty("proprieta")
  public ProprietaPendenza getProprieta() {
    return proprieta;
  }
  public void setProprieta(ProprietaPendenza proprieta) {
    this.proprieta = proprieta;
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
   * Identificativo della tipologia pendenza
   **/
  public PendenzaPut idTipoPendenza(String idTipoPendenza) {
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
  public PendenzaPut voci(List<NuovaVocePendenza> voci) {
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

  /**
   **/
  public PendenzaPut allegati(List<NuovoAllegatoPendenza> allegati) {
    this.allegati = allegati;
    return this;
  }

  @JsonProperty("allegati")
  public List<NuovoAllegatoPendenza> getAllegati() {
    return allegati;
  }
  public void setAllegati(List<NuovoAllegatoPendenza> allegati) {
    this.allegati = allegati;
  }

  @Override
  public boolean equals(java.lang.Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    PendenzaPut pendenzaPut = (PendenzaPut) o;
    return Objects.equals(nome, pendenzaPut.nome) &&
        Objects.equals(causale, pendenzaPut.causale) &&
        Objects.equals(soggettoPagatore, pendenzaPut.soggettoPagatore) &&
        Objects.equals(importo, pendenzaPut.importo) &&
        Objects.equals(numeroAvviso, pendenzaPut.numeroAvviso) &&
        Objects.equals(dataCaricamento, pendenzaPut.dataCaricamento) &&
        Objects.equals(dataValidita, pendenzaPut.dataValidita) &&
        Objects.equals(dataScadenza, pendenzaPut.dataScadenza) &&
        Objects.equals(annoRiferimento, pendenzaPut.annoRiferimento) &&
        Objects.equals(cartellaPagamento, pendenzaPut.cartellaPagamento) &&
        Objects.equals(datiAllegati, pendenzaPut.datiAllegati) &&
        Objects.equals(tassonomia, pendenzaPut.tassonomia) &&
        Objects.equals(tassonomiaAvviso, pendenzaPut.tassonomiaAvviso) &&
        Objects.equals(direzione, pendenzaPut.direzione) &&
        Objects.equals(divisione, pendenzaPut.divisione) &&
        Objects.equals(documento, pendenzaPut.documento) &&
        Objects.equals(dataNotificaAvviso, pendenzaPut.dataNotificaAvviso) &&
        Objects.equals(dataPromemoriaScadenza, pendenzaPut.dataPromemoriaScadenza) &&
        Objects.equals(proprieta, pendenzaPut.proprieta) &&
        Objects.equals(idDominio, pendenzaPut.idDominio) &&
        Objects.equals(idUnitaOperativa, pendenzaPut.idUnitaOperativa) &&
        Objects.equals(idTipoPendenza, pendenzaPut.idTipoPendenza) &&
        Objects.equals(voci, pendenzaPut.voci) &&
        Objects.equals(allegati, pendenzaPut.allegati);
  }

  @Override
  public int hashCode() {
    return Objects.hash(nome, causale, soggettoPagatore, importo, numeroAvviso, dataCaricamento, dataValidita, dataScadenza, annoRiferimento, cartellaPagamento, datiAllegati, tassonomia, tassonomiaAvviso, direzione, divisione, documento, dataNotificaAvviso, dataPromemoriaScadenza, proprieta, idDominio, idUnitaOperativa, idTipoPendenza, voci, allegati);
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
    sb.append("    documento: ").append(toIndentedString(documento)).append("\n");
    sb.append("    dataNotificaAvviso: ").append(toIndentedString(dataNotificaAvviso)).append("\n");
    sb.append("    dataPromemoriaScadenza: ").append(toIndentedString(dataPromemoriaScadenza)).append("\n");
    sb.append("    proprieta: ").append(toIndentedString(proprieta)).append("\n");
    sb.append("    idDominio: ").append(toIndentedString(idDominio)).append("\n");
    sb.append("    idUnitaOperativa: ").append(toIndentedString(idUnitaOperativa)).append("\n");
    sb.append("    idTipoPendenza: ").append(toIndentedString(idTipoPendenza)).append("\n");
    sb.append("    voci: ").append(toIndentedString(voci)).append("\n");
    sb.append("    allegati: ").append(toIndentedString(allegati)).append("\n");
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
		validatoreId.validaIdUO("idUnitaOperativa", this.idUnitaOperativa, false);
		validatoreId.validaIdTipoVersamento("idTipoPendenza", this.idTipoPendenza, false);
		
		ValidatoreUtils.validaNomePendenza(vf, "nome", nome);
		ValidatoreUtils.validaCausale(vf, "causale", causale);
		
		vf.getValidator("soggettoPagatore", this.soggettoPagatore).notNull().validateFields();
		
		ValidatoreUtils.validaImportoOpzionale(vf, "importo", importo);
		ValidatoreUtils.validaNumeroAvviso(vf, "numeroAvviso", numeroAvviso);
		ValidatoreUtils.validaData(vf, "dataValidita", this.dataValidita);
		ValidatoreUtils.validaData(vf, "dataScadenza", this.dataScadenza);
		ValidatoreUtils.validaAnnoRiferimento(vf, "annoRiferimento", annoRiferimento);
		ValidatoreUtils.validaCartellaPagamento(vf, "cartellaPagamento", cartellaPagamento);
		ValidatoreUtils.validaTassonomia(vf, "tassonomia", tassonomia);
		
		vf.getValidator("voci", this.voci).notNull().minItems(1).maxItems(5).validateObjects();
		
		validatoreId.validaIdDirezione("direzione",this.direzione, false);
		validatoreId.validaIdDivisione("divisione",this.divisione, false);
		
		vf.getValidator("documento", this.documento).validateFields();
		
		ValidatoreUtils.validaData(vf, "dataNotificaAvviso", this.dataNotificaAvviso);
		ValidatoreUtils.validaData(vf, "dataPromemoriaScadenza", this.dataPromemoriaScadenza);
		
		vf.getValidator("proprieta", this.proprieta).validateFields();
		vf.getValidator("allegati", this.allegati).validateObjects();
	}
}



