package it.govpay.pagamento.v2.beans;

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
import it.govpay.core.utils.validator.ValidatoreUtils;
@com.fasterxml.jackson.annotation.JsonPropertyOrder({
"idA2A",
"idPendenza",
"idDominio",
"idUnitaOperativa",
"idTipoPendenza",
"causale",
"soggettoPagatore",
"importo",
"numeroAvviso",
"tassonomia",
"tassonomiaAvviso",
"dataValidita",
"dataScadenza",
"annoRiferimento",
"cartellaPagamento",
"datiAllegati",
"direzione",
"divisione",
"voci",
"idDebitore",
"dati",
})
public class NuovaPendenza extends JSONSerializable implements IValidable {
  
  @JsonProperty("idA2A")
  private String idA2A = null;
  
  @JsonProperty("idPendenza")
  private String idPendenza = null;
  
  @JsonProperty("idDominio")
  private String idDominio = null;
  
  @JsonProperty("idUnitaOperativa")
  private String idUnitaOperativa = null;
  
  @JsonProperty("idTipoPendenza")
  private String idTipoPendenza = null;
  
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
  
  private TassonomiaAvviso tassonomiaAvvisoEnum = null;
  
  @JsonProperty("tassonomiaAvviso")
  private String tassonomiaAvviso = null;
  
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
  
  @JsonProperty("direzione")
  private String direzione = null;
  
  @JsonProperty("divisione")
  private String divisione = null;
  
  @JsonProperty("voci")
  private List<NuovaVocePendenza> voci = new ArrayList<>();

  @JsonProperty("idDebitore")
  private String idDebitore = null;

  @JsonProperty("dati")
  private Object dati = null;

  
  /**
   * Identificativo del gestionale responsabile della pendenza
   **/
  public NuovaPendenza idA2A(String idA2A) {
    this.idA2A = idA2A;
    return this;
  }

  @JsonProperty("idA2A")
  public String getIdA2A() {
    return idA2A;
  }
  public void setIdA2A(String idA2A) {
    this.idA2A = idA2A;
  }

  /**
   * Identificativo della pendenza nel gestionale responsabile
   **/
  public NuovaPendenza idPendenza(String idPendenza) {
    this.idPendenza = idPendenza;
    return this;
  }

  @JsonProperty("idPendenza")
  public String getIdPendenza() {
    return idPendenza;
  }
  public void setIdPendenza(String idPendenza) {
    this.idPendenza = idPendenza;
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

  @JsonIgnore
  public TassonomiaAvviso getTassonomiaAvvisoEnum() {
	return this.tassonomiaAvvisoEnum;
  }
  public void setTassonomiaAvvisoEnum(TassonomiaAvviso tassonomiaAvviso) {
	this.tassonomiaAvvisoEnum = tassonomiaAvviso;
  }

  /**
   **/
  public NuovaPendenza tassonomiaAvviso(String tassonomiaAvviso) {
    this.tassonomiaAvviso = tassonomiaAvviso;
    return this;
  }

  @JsonProperty("tassonomiaAvviso")
  public String getTassonomiaAvviso() {
    return tassonomiaAvviso;
  }
  public void setTassonomiaAvviso(String tassonomiaAvviso) {
    this.tassonomiaAvviso = tassonomiaAvviso;
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

  /**
   * Identificativo del soggetto debitore  della pendenza riferita dall'avviso
   **/
  public NuovaPendenza idDebitore(String idDebitore) {
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
	public NuovaPendenza dati(Object dati) {
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
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    NuovaPendenza nuovaPendenza = (NuovaPendenza) o;
    return Objects.equals(idA2A, nuovaPendenza.idA2A) &&
        Objects.equals(idPendenza, nuovaPendenza.idPendenza) &&
        Objects.equals(idDominio, nuovaPendenza.idDominio) &&
        Objects.equals(idUnitaOperativa, nuovaPendenza.idUnitaOperativa) &&
        Objects.equals(idTipoPendenza, nuovaPendenza.idTipoPendenza) &&
        Objects.equals(causale, nuovaPendenza.causale) &&
        Objects.equals(soggettoPagatore, nuovaPendenza.soggettoPagatore) &&
        Objects.equals(importo, nuovaPendenza.importo) &&
        Objects.equals(numeroAvviso, nuovaPendenza.numeroAvviso) &&
        Objects.equals(tassonomia, nuovaPendenza.tassonomia) &&
        Objects.equals(tassonomiaAvviso, nuovaPendenza.tassonomiaAvviso) &&
        Objects.equals(dataValidita, nuovaPendenza.dataValidita) &&
        Objects.equals(dataScadenza, nuovaPendenza.dataScadenza) &&
        Objects.equals(annoRiferimento, nuovaPendenza.annoRiferimento) &&
        Objects.equals(cartellaPagamento, nuovaPendenza.cartellaPagamento) &&
        Objects.equals(datiAllegati, nuovaPendenza.datiAllegati) &&
        Objects.equals(direzione, nuovaPendenza.direzione) &&
        Objects.equals(divisione, nuovaPendenza.divisione) &&
        Objects.equals(voci, nuovaPendenza.voci) &&
        Objects.equals(idDebitore, nuovaPendenza.idDebitore) &&
	Objects.equals(this.dati, nuovaPendenza.dati);
  }

  @Override
  public int hashCode() {
    return Objects.hash(idA2A, idPendenza, idDominio, idUnitaOperativa, idTipoPendenza, causale, soggettoPagatore, importo, numeroAvviso, tassonomia, tassonomiaAvviso, dataValidita, dataScadenza, annoRiferimento, cartellaPagamento, datiAllegati, direzione, divisione, voci, idDebitore, dati);
  }

  public static NuovaPendenza parse(String json) throws org.openspcoop2.generic_project.exception.ServiceException, org.openspcoop2.utils.json.ValidationException {
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
    sb.append("    ").append(toIndentedString(super.toString())).append("\n");
    sb.append("    idA2A: ").append(toIndentedString(idA2A)).append("\n");
    sb.append("    idPendenza: ").append(toIndentedString(idPendenza)).append("\n");
    sb.append("    idDominio: ").append(toIndentedString(idDominio)).append("\n");
    sb.append("    idUnitaOperativa: ").append(toIndentedString(idUnitaOperativa)).append("\n");
    sb.append("    idTipoPendenza: ").append(toIndentedString(idTipoPendenza)).append("\n");
    sb.append("    causale: ").append(toIndentedString(causale)).append("\n");
    sb.append("    soggettoPagatore: ").append(toIndentedString(soggettoPagatore)).append("\n");
    sb.append("    importo: ").append(toIndentedString(importo)).append("\n");
    sb.append("    numeroAvviso: ").append(toIndentedString(numeroAvviso)).append("\n");
    sb.append("    tassonomia: ").append(toIndentedString(tassonomia)).append("\n");
    sb.append("    tassonomiaAvviso: ").append(toIndentedString(tassonomiaAvviso)).append("\n");
    sb.append("    dataValidita: ").append(toIndentedString(dataValidita)).append("\n");
    sb.append("    dataScadenza: ").append(toIndentedString(dataScadenza)).append("\n");
    sb.append("    annoRiferimento: ").append(toIndentedString(annoRiferimento)).append("\n");
    sb.append("    cartellaPagamento: ").append(toIndentedString(cartellaPagamento)).append("\n");
    sb.append("    datiAllegati: ").append(toIndentedString(datiAllegati)).append("\n");
    sb.append("    direzione: ").append(toIndentedString(direzione)).append("\n");
    sb.append("    divisione: ").append(toIndentedString(divisione)).append("\n");
    sb.append("    voci: ").append(toIndentedString(voci)).append("\n");
    sb.append("    idDebitore: ").append(toIndentedString(idDebitore)).append("\n");
    sb.append("    dati: ").append(this.toIndentedString(this.dati)).append("\n");
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

		// Pendenza passata per riferimento idA2A/idPendenza
		if(this.idA2A != null && this.idDominio == null) {
			validatoreId.validaIdApplicazione("idA2A", this.idA2A);
			validatoreId.validaIdPendenza("idPendenza", this.idPendenza);
			try {
				vf.getValidator("idDominio", this.idDominio).isNull();
				vf.getValidator("idUnitaOperativa", this.idUnitaOperativa).isNull();
				vf.getValidator("causale", this.causale).isNull();
				vf.getValidator("soggettoPagatore", this.soggettoPagatore).isNull();
				vf.getValidator("importo", this.importo).isNull();
				vf.getValidator("numeroAvviso", this.numeroAvviso).isNull();
				vf.getValidator("dataValidita", this.dataValidita).isNull();
				vf.getValidator("dataScadenza", this.dataScadenza).isNull();
				vf.getValidator("annoRiferimento", this.annoRiferimento).isNull();;
				vf.getValidator("cartellaPagamento", this.cartellaPagamento).isNull();
				vf.getValidator("voci", this.voci).isNull();
				vf.getValidator("idTipoPendenza", this.idTipoPendenza).isNull();
				vf.getValidator("direzione", this.direzione).isNull();
				vf.getValidator("divisione", this.divisione).isNull();
				vf.getValidator("tassonomia", this.tassonomia).isNull();
				vf.getValidator("tassonomiaAvviso", this.tassonomiaAvviso).isNull();
				if(this.dati != null)
					throw new ValidationException("Il campo dati deve essere vuoto.");
			} catch (ValidationException ve) {
				throw new ValidationException("Pendenza riferita per identificativo A2A. " + ve.getMessage());
			}
			return;
		}else if(this.idA2A == null && this.idDominio != null) {
			validatoreId.validaIdDominio("idDominio", this.idDominio);
			
			// idDominio e' chiave insieme a numeroAvviso oppure IdTipoPendenza se sono entrambe null ho errore
			if(this.numeroAvviso == null && this.idTipoPendenza == null) {
				throw new ValidationException("Pendenza riferita per numeroAvviso o idTipoPendenza: dati mancanti");
			}
			
			if(this.numeroAvviso != null && this.idTipoPendenza == null) {
				vf.getValidator("numeroAvviso", this.numeroAvviso).notNull().pattern("[0-9]{18}");
				try {
					vf.getValidator("idUnitaOperativa", this.idUnitaOperativa).isNull();
					vf.getValidator("causale", this.causale).isNull();
					vf.getValidator("soggettoPagatore", this.soggettoPagatore).isNull();
					vf.getValidator("importo", this.importo).isNull();
					vf.getValidator("dataValidita", this.dataValidita).isNull();
					vf.getValidator("dataScadenza", this.dataScadenza).isNull();
					vf.getValidator("annoRiferimento", this.annoRiferimento).isNull();;
					vf.getValidator("cartellaPagamento", this.cartellaPagamento).isNull();
					vf.getValidator("voci", this.voci).isNull();
					vf.getValidator("idA2A", this.idA2A).isNull();
					vf.getValidator("idPendenza", this.idPendenza).isNull();
					vf.getValidator("idTipoPendenza", this.idTipoPendenza).isNull();
					vf.getValidator("direzione", this.direzione).isNull();
					vf.getValidator("divisione", this.divisione).isNull();
					vf.getValidator("tassonomia", this.tassonomia).isNull();
					vf.getValidator("tassonomiaAvviso", this.tassonomiaAvviso).isNull();
					if(this.dati != null)
						throw new ValidationException("Il campo dati deve essere vuoto.");
				} catch (ValidationException ve) {
					throw new ValidationException("Pendenza riferita per numero avviso. " + ve.getMessage());
				}
			}
			
			if(this.numeroAvviso == null && this.idTipoPendenza != null) {
				validatoreId.validaIdTipoVersamento("idTipoPendenza", this.idTipoPendenza);
				try {
					if(this.dati == null)
						throw new ValidationException("Il campo dati non deve essere vuoto.");
					vf.getValidator("numeroAvviso", this.numeroAvviso).isNull();
					vf.getValidator("idUnitaOperativa", this.idUnitaOperativa).isNull();
					vf.getValidator("causale", this.causale).isNull();
					vf.getValidator("soggettoPagatore", this.soggettoPagatore).isNull();
					vf.getValidator("importo", this.importo).isNull();
					vf.getValidator("dataValidita", this.dataValidita).isNull();
					vf.getValidator("dataScadenza", this.dataScadenza).isNull();
					vf.getValidator("annoRiferimento", this.annoRiferimento).isNull();;
					vf.getValidator("cartellaPagamento", this.cartellaPagamento).isNull();
					vf.getValidator("voci", this.voci).isNull();
					vf.getValidator("idA2A", this.idA2A).isNull();
					vf.getValidator("idPendenza", this.idPendenza).isNull();
					vf.getValidator("direzione", this.direzione).isNull();
					vf.getValidator("divisione", this.divisione).isNull();
					vf.getValidator("tassonomia", this.tassonomia).isNull();
					vf.getValidator("tassonomiaAvviso", this.tassonomiaAvviso).isNull();
				} catch (ValidationException ve) {
					throw new ValidationException("Pendenza modello 4. " + ve.getMessage());
				}
			}
		} else {
			validatoreId.validaIdApplicazione("idA2A", this.idA2A);
			validatoreId.validaIdPendenza("idPendenza", this.idPendenza);
			validatoreId.validaIdDominio("idDominio", this.idDominio);
			validatoreId.validaIdUO("idUnitaOperativa", this.idUnitaOperativa, false);
			validatoreId.validaIdTipoVersamento("idTipoPendenza", this.idTipoPendenza, false);
			
			ValidatoreUtils.validaCausale(vf, "causale", causale);
			
			vf.getValidator("soggettoPagatore", this.soggettoPagatore).notNull().validateFields();
			
			ValidatoreUtils.validaImporto(vf, "importo", importo);
			ValidatoreUtils.validaNumeroAvviso(vf, "numeroAvviso", numeroAvviso);
			ValidatoreUtils.validaData(vf, "dataValidita", this.dataValidita);
			ValidatoreUtils.validaData(vf, "dataScadenza", this.dataScadenza);
			ValidatoreUtils.validaAnnoRiferimento(vf, "annoRiferimento", annoRiferimento);
			ValidatoreUtils.validaCartellaPagamento(vf, "cartellaPagamento", cartellaPagamento);
			ValidatoreUtils.validaTassonomia(vf, "tassonomia", tassonomia);
			
			vf.getValidator("voci", this.voci).notNull().minItems(1).maxItems(5).validateObjects();
			
			validatoreId.validaIdDirezione("direzione",this.direzione, false);
			validatoreId.validaIdDivisione("divisione",this.divisione, false);
		}
	}
}



