package it.govpay.pagamento.v2.beans;

import java.math.BigDecimal;
import java.util.Objects;

import com.fasterxml.jackson.annotation.JsonProperty;

import it.govpay.core.beans.JSONSerializable;
@com.fasterxml.jackson.annotation.JsonPropertyOrder({
"idVocePendenza",
"importo",
"descrizione",
"datiAllegati",
"descrizioneCausaleRPT",
"indice",
"stato",
"dominio",
"tipoBollo",
"hashDocumento",
"provinciaResidenza",
"codEntrata",
"codiceContabilita",
"ibanAccredito",
"ibanAppoggio",
"tipoContabilita",
})
public class VocePendenza extends JSONSerializable {
  
  @JsonProperty("idVocePendenza")
  private String idVocePendenza = null;
  
  @JsonProperty("importo")
  private BigDecimal importo = null;
  
  @JsonProperty("descrizione")
  private String descrizione = null;
  
  @JsonProperty("datiAllegati")
  private Object datiAllegati = null;
  
  @JsonProperty("descrizioneCausaleRPT")
  private String descrizioneCausaleRPT = null;
  
  @JsonProperty("indice")
  private BigDecimal indice = null;
  
  @JsonProperty("stato")
  private StatoVocePendenza stato = null;
  
  @JsonProperty("dominio")
  private Dominio dominio = null;
  
  /**
   * Identificativo della voce di pendenza nel gestionale proprietario
   **/
  public VocePendenza idVocePendenza(String idVocePendenza) {
    this.idVocePendenza = idVocePendenza;
    return this;
  }

  @JsonProperty("idVocePendenza")
  public String getIdVocePendenza() {
    return idVocePendenza;
  }
  public void setIdVocePendenza(String idVocePendenza) {
    this.idVocePendenza = idVocePendenza;
  }

  /**
   * Importo della voce
   **/
  public VocePendenza importo(BigDecimal importo) {
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
   * descrizione della voce di pagamento
   **/
  public VocePendenza descrizione(String descrizione) {
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
   * Dati applicativi allegati dal gestionale secondo un formato proprietario.
   **/
  public VocePendenza datiAllegati(Object datiAllegati) {
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
   * Testo libero per la causale versamento
   **/
  public VocePendenza descrizioneCausaleRPT(String descrizioneCausaleRPT) {
    this.descrizioneCausaleRPT = descrizioneCausaleRPT;
    return this;
  }

  @JsonProperty("descrizioneCausaleRPT")
  public String getDescrizioneCausaleRPT() {
    return descrizioneCausaleRPT;
  }
  public void setDescrizioneCausaleRPT(String descrizioneCausaleRPT) {
    this.descrizioneCausaleRPT = descrizioneCausaleRPT;
  }

  /**
   * indice di voce all'interno della pendenza
   **/
  public VocePendenza indice(BigDecimal indice) {
    this.indice = indice;
    return this;
  }

  @JsonProperty("indice")
  public BigDecimal getIndice() {
    return indice;
  }
  public void setIndice(BigDecimal indice) {
    this.indice = indice;
  }

  /**
   **/
  public VocePendenza stato(StatoVocePendenza stato) {
    this.stato = stato;
    return this;
  }

  @JsonProperty("stato")
  public StatoVocePendenza getStato() {
    return stato;
  }
  public void setStato(StatoVocePendenza stato) {
    this.stato = stato;
  }

  /**
   **/
  public VocePendenza dominio(Dominio dominio) {
    this.dominio = dominio;
    return this;
  }

  @JsonProperty("dominio")
  public Dominio getDominio() {
    return dominio;
  }
  public void setDominio(Dominio dominio) {
    this.dominio = dominio;
  }

  public enum TipoBolloEnum {


	IMPOSTA_DI_BOLLO("Imposta di bollo");


	private String value;


	TipoBolloEnum(String value) {
		this.value = value;
	}

	@Override
	@com.fasterxml.jackson.annotation.JsonValue
	public String toString() {
		return String.valueOf(this.value);
	}

	public String getCodifica() {
		switch (this) {
		case IMPOSTA_DI_BOLLO:
		default:
			return "01";
		}
	}
	
	public static TipoBolloEnum fromCodifica(String codifica) {
		switch (codifica) {
		case "01":
		default:
			return IMPOSTA_DI_BOLLO;
		}
	}

	public static TipoBolloEnum fromValue(String text) {
		for (TipoBolloEnum b : TipoBolloEnum.values()) {
			if (String.valueOf(b.value).equals(text)) {
				return b;
			}
		}
		return null;
	}
  }


    
    
  @JsonProperty("tipoBollo")
  private TipoBolloEnum tipoBollo = null;
  
  @JsonProperty("hashDocumento")
  private String hashDocumento = null;
  
  @JsonProperty("provinciaResidenza")
  private String provinciaResidenza = null;
  
  /**
   * Tipologia di Bollo digitale
   **/
  public VocePendenza tipoBollo(TipoBolloEnum tipoBollo) {
    this.tipoBollo = tipoBollo;
    return this;
  }

  @JsonProperty("tipoBollo")
  public TipoBolloEnum getTipoBollo() {
    return tipoBollo;
  }
  public void setTipoBollo(TipoBolloEnum tipoBollo) {
    this.tipoBollo = tipoBollo;
  }

  /**
   * Digest in base64 del documento informatico associato alla marca da bollo
   **/
  public VocePendenza hashDocumento(String hashDocumento) {
    this.hashDocumento = hashDocumento;
    return this;
  }

  @JsonProperty("hashDocumento")
  public String getHashDocumento() {
    return hashDocumento;
  }
  public void setHashDocumento(String hashDocumento) {
    this.hashDocumento = hashDocumento;
  }

  /**
   * Sigla automobilistica della provincia di residenza del soggetto pagatore
   **/
  public VocePendenza provinciaResidenza(String provinciaResidenza) {
    this.provinciaResidenza = provinciaResidenza;
    return this;
  }

  @JsonProperty("provinciaResidenza")
  public String getProvinciaResidenza() {
    return provinciaResidenza;
  }
  public void setProvinciaResidenza(String provinciaResidenza) {
    this.provinciaResidenza = provinciaResidenza;
  }

  @JsonProperty("codEntrata")
  private String codEntrata = null;
  
  /**
   **/
  public VocePendenza codEntrata(String codEntrata) {
    this.codEntrata = codEntrata;
    return this;
  }

  @JsonProperty("codEntrata")
  public String getCodEntrata() {
    return codEntrata;
  }
  public void setCodEntrata(String codEntrata) {
    this.codEntrata = codEntrata;
  }

  @JsonProperty("ibanAccredito")
  private String ibanAccredito = null;
  
  @JsonProperty("ibanAppoggio")
  private String ibanAppoggio = null;
  
  @JsonProperty("tipoContabilita")
  private TipoContabilita tipoContabilita = null;
  
  @JsonProperty("codiceContabilita")
  private String codiceContabilita = null;
  
  /**
   **/
  public VocePendenza ibanAccredito(String ibanAccredito) {
    this.ibanAccredito = ibanAccredito;
    return this;
  }

  @JsonProperty("ibanAccredito")
  public String getIbanAccredito() {
    return ibanAccredito;
  }
  public void setIbanAccredito(String ibanAccredito) {
    this.ibanAccredito = ibanAccredito;
  }

  /**
   **/
  public VocePendenza ibanAppoggio(String ibanAppoggio) {
    this.ibanAppoggio = ibanAppoggio;
    return this;
  }

  @JsonProperty("ibanAppoggio")
  public String getIbanAppoggio() {
    return ibanAppoggio;
  }
  public void setIbanAppoggio(String ibanAppoggio) {
    this.ibanAppoggio = ibanAppoggio;
  }

  /**
   **/
  public VocePendenza tipoContabilita(TipoContabilita tipoContabilita) {
    this.tipoContabilita = tipoContabilita;
    return this;
  }

  @JsonProperty("tipoContabilita")
  public TipoContabilita getTipoContabilita() {
    return tipoContabilita;
  }
  public void setTipoContabilita(TipoContabilita tipoContabilita) {
    this.tipoContabilita = tipoContabilita;
  }

  /**
   * Codifica del capitolo di bilancio
   **/
  public VocePendenza codiceContabilita(String codiceContabilita) {
    this.codiceContabilita = codiceContabilita;
    return this;
  }

  @JsonProperty("codiceContabilita")
  public String getCodiceContabilita() {
    return codiceContabilita;
  }
  public void setCodiceContabilita(String codiceContabilita) {
    this.codiceContabilita = codiceContabilita;
  }

  @Override
  public boolean equals(java.lang.Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    VocePendenza vocePendenza = (VocePendenza) o;
    return Objects.equals(idVocePendenza, vocePendenza.idVocePendenza) &&
        Objects.equals(importo, vocePendenza.importo) &&
        Objects.equals(descrizione, vocePendenza.descrizione) &&
        Objects.equals(datiAllegati, vocePendenza.datiAllegati) &&
        Objects.equals(descrizioneCausaleRPT, vocePendenza.descrizioneCausaleRPT) &&
        Objects.equals(indice, vocePendenza.indice) &&
        Objects.equals(stato, vocePendenza.stato) &&
        Objects.equals(dominio, vocePendenza.dominio) &&
        Objects.equals(tipoBollo, vocePendenza.tipoBollo) &&
        Objects.equals(hashDocumento, vocePendenza.hashDocumento) &&
        Objects.equals(provinciaResidenza, vocePendenza.provinciaResidenza) &&
        Objects.equals(codEntrata, vocePendenza.codEntrata) &&
        Objects.equals(ibanAccredito, vocePendenza.ibanAccredito) &&
        Objects.equals(ibanAppoggio, vocePendenza.ibanAppoggio) &&
        Objects.equals(tipoContabilita, vocePendenza.tipoContabilita) &&
        Objects.equals(codiceContabilita, vocePendenza.codiceContabilita);
  }

  @Override
  public int hashCode() {
    return Objects.hash(idVocePendenza, importo, descrizione, datiAllegati, descrizioneCausaleRPT, indice, stato, dominio, tipoBollo, hashDocumento, provinciaResidenza, codEntrata, ibanAccredito, ibanAppoggio, tipoContabilita, codiceContabilita);
  }

  public static VocePendenza parse(String json) throws org.openspcoop2.generic_project.exception.ServiceException, org.openspcoop2.utils.json.ValidationException {
    return parse(json, VocePendenza.class);
  }

  @Override
  public String getJsonIdFilter() {
    return "vocePendenza";
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class VocePendenza {\n");
    
    sb.append("    idVocePendenza: ").append(toIndentedString(idVocePendenza)).append("\n");
    sb.append("    importo: ").append(toIndentedString(importo)).append("\n");
    sb.append("    descrizione: ").append(toIndentedString(descrizione)).append("\n");
    sb.append("    datiAllegati: ").append(toIndentedString(datiAllegati)).append("\n");
    sb.append("    descrizioneCausaleRPT: ").append(toIndentedString(descrizioneCausaleRPT)).append("\n");
    sb.append("    indice: ").append(toIndentedString(indice)).append("\n");
    sb.append("    stato: ").append(toIndentedString(stato)).append("\n");
    sb.append("    dominio: ").append(toIndentedString(dominio)).append("\n");
    sb.append("    tipoBollo: ").append(toIndentedString(tipoBollo)).append("\n");
    sb.append("    hashDocumento: ").append(toIndentedString(hashDocumento)).append("\n");
    sb.append("    provinciaResidenza: ").append(toIndentedString(provinciaResidenza)).append("\n");
    sb.append("    codEntrata: ").append(toIndentedString(codEntrata)).append("\n");
    sb.append("    ibanAccredito: ").append(toIndentedString(ibanAccredito)).append("\n");
    sb.append("    ibanAppoggio: ").append(toIndentedString(ibanAppoggio)).append("\n");
    sb.append("    tipoContabilita: ").append(toIndentedString(tipoContabilita)).append("\n");
    sb.append("    codiceContabilita: ").append(toIndentedString(codiceContabilita)).append("\n");
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



