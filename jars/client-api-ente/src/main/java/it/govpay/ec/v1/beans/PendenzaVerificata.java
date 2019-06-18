package it.govpay.ec.v1.beans;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

//import io.swagger.v3.oas.annotations.media.Schema;

public class PendenzaVerificata  {
  
  // @Schema(example = "A2A_12345", required = true, description = "Identificativo del gestionale responsabile della pendenza")
 /**
   * Identificativo del gestionale responsabile della pendenza  
  **/
  private String idA2A = null;
  
  // @Schema(example = "abcdef12345", required = true, description = "Identificativo della pendenza nel gestionale responsabile")
 /**
   * Identificativo della pendenza nel gestionale responsabile  
  **/
  private String idPendenza = null;
  
  // @Schema(required = true, description = "")
  private StatoPendenzaVerificata stato = null;
  
  // @Schema(description = "Informazioni addizionali sullo stato della pendenza")
 /**
   * Informazioni addizionali sullo stato della pendenza  
  **/
  private String descrizioneStato = null;
  
  // @Schema(example = "1234567890", required = true, description = "Identificativo del dominio creditore")
 /**
   * Identificativo del dominio creditore  
  **/
  private String idDominio = null;
  
  // @Schema(example = "UO33132", description = "Identificativo dell'unita' operativa")
 /**
   * Identificativo dell'unita' operativa  
  **/
  private String idUnitaOperativa = null;
  
  // @Schema(example = "IMU", description = "Identificativo della tipologia pendenza")
 /**
   * Identificativo della tipologia pendenza
  **/
  private String idTipoPendenza = null;
  
  // @Schema(example = "Immatricolazione AA 2017/2018", description = "Nome della pendenza da visualizzare sui portali di pagamento e console di gestione.")
 /**
   * Nome della pendenza da visualizzare sui portali di pagamento e console di gestione.  
  **/
  private String nome = null;
  
  // @Schema(example = "Sanzione CdS n. abc00000", required = true, description = "Descrizione da inserire nell'avviso di pagamento")
 /**
   * Descrizione da inserire nell'avviso di pagamento  
  **/
  private String causale = null;
  
  // @Schema(required = true, description = "")
  private Soggetto soggettoPagatore = null;
  
  // @Schema(example = "10.01", required = true, description = "Importo della pendenza. Deve corrispondere alla somma delle singole voci.")
 /**
   * Importo della pendenza. Deve corrispondere alla somma delle singole voci.  
  **/
  private BigDecimal importo = null;
  
  // @Schema(example = "123456789012345678", required = true, description = "Identificativo univoco versamento, assegnato se pagabile da psp")
 /**
   * Identificativo univoco versamento, assegnato se pagabile da psp  
  **/
  private String numeroAvviso = null;
  
  // @Schema(description = "Data di emissione della pendenza")
 /**
   * Data di emissione della pendenza  
  **/
  private Date dataCaricamento = null;
  
  // @Schema(description = "Data di validita dei dati della pendenza, decorsa la quale la pendenza può subire variazioni.")
 /**
   * Data di validita dei dati della pendenza, decorsa la quale la pendenza può subire variazioni.  
  **/
  private Date dataValidita = null;
  
  // @Schema(description = "Data di scadenza della pendenza, decorsa la quale non è più pagabile.")
 /**
   * Data di scadenza della pendenza, decorsa la quale non è più pagabile.  
  **/
  private Date dataScadenza = null;
  
  // @Schema(example = "2020", description = "Anno di riferimento della pendenza")
 /**
   * Anno di riferimento della pendenza  
  **/
  private BigDecimal annoRiferimento = null;
  
  // @Schema(example = "ABC00000001", description = "Identificativo della cartella di pagamento a cui afferisce la pendenza")
 /**
   * Identificativo della cartella di pagamento a cui afferisce la pendenza  
  **/
  private String cartellaPagamento = null;
  
  // @Schema(description = "Dati applicativi allegati dal gestionale secondo un formato proprietario.")
 /**
   * Dati applicativi allegati dal gestionale secondo un formato proprietario.  
  **/
  private String datiAllegati = null;
  
  // @Schema(example = "Sanzioni", description = "Macro categoria della pendenza secondo la classificazione del creditore")
 /**
   * Macro categoria della pendenza secondo la classificazione del creditore  
  **/
  private String tassonomia = null;
  
  // @Schema(description = "")
  @JsonIgnore
  private TassonomiaAvviso tassonomiaAvvisoEnum = null;
	
  private String tassonomiaAvviso = null;
  
  // @Schema(description = "")
  private List<VocePendenza> voci = null;
 /**
   * Identificativo del gestionale responsabile della pendenza
   * @return idA2A
  **/
  @JsonProperty("idA2A")
  @NotNull
  @Valid
  public String getIdA2A() {
    return idA2A;
  }

  public void setIdA2A(String idA2A) {
    this.idA2A = idA2A;
  }

  public PendenzaVerificata idA2A(String idA2A) {
    this.idA2A = idA2A;
    return this;
  }

 /**
   * Identificativo della pendenza nel gestionale responsabile
   * @return idPendenza
  **/
  @JsonProperty("idPendenza")
  @NotNull
  @Valid
  public String getIdPendenza() {
    return idPendenza;
  }

  public void setIdPendenza(String idPendenza) {
    this.idPendenza = idPendenza;
  }

  public PendenzaVerificata idPendenza(String idPendenza) {
    this.idPendenza = idPendenza;
    return this;
  }

 /**
   * Get stato
   * @return stato
  **/
  @JsonProperty("stato")
  @NotNull
  @Valid
  public StatoPendenzaVerificata getStato() {
    return stato;
  }

  public void setStato(StatoPendenzaVerificata stato) {
    this.stato = stato;
  }

  public PendenzaVerificata stato(StatoPendenzaVerificata stato) {
    this.stato = stato;
    return this;
  }

 /**
   * Informazioni addizionali sullo stato della pendenza
   * @return descrizioneStato
  **/
  @JsonProperty("descrizioneStato")
  @Valid
  public String getDescrizioneStato() {
    return descrizioneStato;
  }

  public void setDescrizioneStato(String descrizioneStato) {
    this.descrizioneStato = descrizioneStato;
  }

  public PendenzaVerificata descrizioneStato(String descrizioneStato) {
    this.descrizioneStato = descrizioneStato;
    return this;
  }

 /**
   * Identificativo del dominio creditore
   * @return idDominio
  **/
  @JsonProperty("idDominio")
  @NotNull
  @Valid
  public String getIdDominio() {
    return idDominio;
  }

  public void setIdDominio(String idDominio) {
    this.idDominio = idDominio;
  }

  public PendenzaVerificata idDominio(String idDominio) {
    this.idDominio = idDominio;
    return this;
  }

 /**
   * Identificativo dell&#x27;unita&#x27; operativa
   * @return idUnitaOperativa
  **/
  @JsonProperty("idUnitaOperativa")
  @Valid
  public String getIdUnitaOperativa() {
    return idUnitaOperativa;
  }

  public void setIdUnitaOperativa(String idUnitaOperativa) {
    this.idUnitaOperativa = idUnitaOperativa;
  }

  public PendenzaVerificata idUnitaOperativa(String idUnitaOperativa) {
    this.idUnitaOperativa = idUnitaOperativa;
    return this;
  }
  
  
  /**
   * Identificativo della tipologia pendenza
   **/
  @JsonProperty("idTipoPendenza")
  @Valid
  public String getIdTipoPendenza() {
    return idTipoPendenza;
  }
  
  public void setIdTipoPendenza(String idTipoPendenza) {
    this.idTipoPendenza = idTipoPendenza;
  }
  
  public PendenzaVerificata idTipoPendenza(String idTipoPendenza) {
	this.idTipoPendenza = idTipoPendenza;
	return this;
  }

 /**
   * Nome della pendenza da visualizzare sui portali di pagamento e console di gestione.
   * @return nome
  **/
  @JsonProperty("nome")
  @Valid
  public String getNome() {
    return nome;
  }

  public void setNome(String nome) {
    this.nome = nome;
  }

  public PendenzaVerificata nome(String nome) {
    this.nome = nome;
    return this;
  }

 /**
   * Descrizione da inserire nell&#x27;avviso di pagamento
   * @return causale
  **/
  @JsonProperty("causale")
  @NotNull
  @Valid
  public String getCausale() {
    return causale;
  }

  public void setCausale(String causale) {
    this.causale = causale;
  }

  public PendenzaVerificata causale(String causale) {
    this.causale = causale;
    return this;
  }

 /**
   * Get soggettoPagatore
   * @return soggettoPagatore
  **/
  @JsonProperty("soggettoPagatore")
  @NotNull
  @Valid
  public Soggetto getSoggettoPagatore() {
    return soggettoPagatore;
  }

  public void setSoggettoPagatore(Soggetto soggettoPagatore) {
    this.soggettoPagatore = soggettoPagatore;
  }

  public PendenzaVerificata soggettoPagatore(Soggetto soggettoPagatore) {
    this.soggettoPagatore = soggettoPagatore;
    return this;
  }

 /**
   * Importo della pendenza. Deve corrispondere alla somma delle singole voci.
   * @return importo
  **/
  @JsonProperty("importo")
  @NotNull
  @Valid
  public BigDecimal getImporto() {
    return importo;
  }

  public void setImporto(BigDecimal importo) {
    this.importo = importo;
  }

  public PendenzaVerificata importo(BigDecimal importo) {
    this.importo = importo;
    return this;
  }

 /**
   * Identificativo univoco versamento, assegnato se pagabile da psp
   * @return numeroAvviso
  **/
  @JsonProperty("numeroAvviso")
  @NotNull
  @Valid
  public String getNumeroAvviso() {
    return numeroAvviso;
  }

  public void setNumeroAvviso(String numeroAvviso) {
    this.numeroAvviso = numeroAvviso;
  }

  public PendenzaVerificata numeroAvviso(String numeroAvviso) {
    this.numeroAvviso = numeroAvviso;
    return this;
  }

 /**
   * Data di emissione della pendenza
   * @return dataCaricamento
  **/
  @JsonProperty("dataCaricamento")
  @Valid
  public Date getDataCaricamento() {
    return dataCaricamento;
  }

  public void setDataCaricamento(Date dataCaricamento) {
    this.dataCaricamento = dataCaricamento;
  }

  public PendenzaVerificata dataCaricamento(Date dataCaricamento) {
    this.dataCaricamento = dataCaricamento;
    return this;
  }

 /**
   * Data di validita dei dati della pendenza, decorsa la quale la pendenza può subire variazioni.
   * @return dataValidita
  **/
  @JsonProperty("dataValidita")
  @Valid
  public Date getDataValidita() {
    return dataValidita;
  }

  public void setDataValidita(Date dataValidita) {
    this.dataValidita = dataValidita;
  }

  public PendenzaVerificata dataValidita(Date dataValidita) {
    this.dataValidita = dataValidita;
    return this;
  }

 /**
   * Data di scadenza della pendenza, decorsa la quale non è più pagabile.
   * @return dataScadenza
  **/
  @JsonProperty("dataScadenza")
  @Valid
  public Date getDataScadenza() {
    return dataScadenza;
  }

  public void setDataScadenza(Date dataScadenza) {
    this.dataScadenza = dataScadenza;
  }

  public PendenzaVerificata dataScadenza(Date dataScadenza) {
    this.dataScadenza = dataScadenza;
    return this;
  }

 /**
   * Anno di riferimento della pendenza
   * @return annoRiferimento
  **/
  @JsonProperty("annoRiferimento")
  @Valid
  public BigDecimal getAnnoRiferimento() {
    return annoRiferimento;
  }

  public void setAnnoRiferimento(BigDecimal annoRiferimento) {
    this.annoRiferimento = annoRiferimento;
  }

  public PendenzaVerificata annoRiferimento(BigDecimal annoRiferimento) {
    this.annoRiferimento = annoRiferimento;
    return this;
  }

 /**
   * Identificativo della cartella di pagamento a cui afferisce la pendenza
   * @return cartellaPagamento
  **/
  @JsonProperty("cartellaPagamento")
  @Valid
  public String getCartellaPagamento() {
    return cartellaPagamento;
  }

  public void setCartellaPagamento(String cartellaPagamento) {
    this.cartellaPagamento = cartellaPagamento;
  }

  public PendenzaVerificata cartellaPagamento(String cartellaPagamento) {
    this.cartellaPagamento = cartellaPagamento;
    return this;
  }

 /**
   * Dati applicativi allegati dal gestionale secondo un formato proprietario.
   * @return datiAllegati
  **/
  @JsonProperty("datiAllegati")
  @Valid
  public String getDatiAllegati() {
    return datiAllegati;
  }

  public void setDatiAllegati(String datiAllegati) {
    this.datiAllegati = datiAllegati;
  }

  public PendenzaVerificata datiAllegati(String datiAllegati) {
    this.datiAllegati = datiAllegati;
    return this;
  }

 /**
   * Macro categoria della pendenza secondo la classificazione del creditore
   * @return tassonomia
  **/
  @JsonProperty("tassonomia")
  @Valid
  public String getTassonomia() {
    return tassonomia;
  }

  public void setTassonomia(String tassonomia) {
    this.tassonomia = tassonomia;
  }

  public PendenzaVerificata tassonomia(String tassonomia) {
    this.tassonomia = tassonomia;
    return this;
  }

  /**
	 **/
	public PendenzaVerificata tassonomiaAvvisoEnum(TassonomiaAvviso tassonomiaAvviso) {
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
	public PendenzaVerificata tassonomiaAvviso(String tassonomiaAvviso) {
		this.setTassonomiaAvviso(tassonomiaAvviso);
		return this;
	}

	@JsonProperty("tassonomiaAvviso")
	@Valid
	public String getTassonomiaAvviso() {
		return this.tassonomiaAvviso;
	}
	public void setTassonomiaAvviso(String tassonomiaAvviso) {
		this.tassonomiaAvviso = tassonomiaAvviso;
	}

 /**
   * Get voci
   * @return voci
  **/
  @JsonProperty("voci")
  @Valid
 @Size(min=1,max=5)  public List<VocePendenza> getVoci() {
    return voci;
  }

  public void setVoci(List<VocePendenza> voci) {
    this.voci = voci;
  }

  public PendenzaVerificata voci(List<VocePendenza> voci) {
    this.voci = voci;
    return this;
  }

  public PendenzaVerificata addVociItem(VocePendenza vociItem) {
    this.voci.add(vociItem);
    return this;
  }


  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class PendenzaVerificata {\n");
    
    sb.append("    idA2A: ").append(toIndentedString(idA2A)).append("\n");
    sb.append("    idPendenza: ").append(toIndentedString(idPendenza)).append("\n");
    sb.append("    stato: ").append(toIndentedString(stato)).append("\n");
    sb.append("    descrizioneStato: ").append(toIndentedString(descrizioneStato)).append("\n");
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
    sb.append("    voci: ").append(toIndentedString(voci)).append("\n");
    sb.append("}");
    return sb.toString();
  }

  /**
   * Convert the given object to string with each line indented by 4 spaces
   * (except the first line).
   */
  private static String toIndentedString(java.lang.Object o) {
    if (o == null) {
      return "null";
    }
    return o.toString().replace("\n", "\n    ");
  }
}
