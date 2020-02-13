package it.govpay.pagamento.v2.beans;

import java.math.BigDecimal;
import java.util.Objects;

import org.openspcoop2.utils.json.ValidationException;

import com.fasterxml.jackson.annotation.JsonProperty;

import it.govpay.core.beans.JSONSerializable;
import it.govpay.core.utils.validator.CostantiValidazione;
import it.govpay.core.utils.validator.IValidable;
import it.govpay.core.utils.validator.ValidatorFactory;
import it.govpay.core.utils.validator.ValidatoreUtils;
@com.fasterxml.jackson.annotation.JsonPropertyOrder({
"idVocePendenza",
"importo",
"descrizione",
"datiAllegati",
"descrizioneCausaleRPT",
"tipoBollo",
"hashDocumento",
"provinciaResidenza",
"codEntrata",
"codiceContabilita",
"ibanAccredito",
"ibanAppoggio",
"tipoContabilita",
})
public class NuovaVocePendenza extends JSONSerializable implements IValidable {
  
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
  
  /**
   * Identificativo della voce di pendenza nel gestionale proprietario
   **/
  public NuovaVocePendenza idVocePendenza(String idVocePendenza) {
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
  public NuovaVocePendenza importo(BigDecimal importo) {
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
  public NuovaVocePendenza descrizione(String descrizione) {
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
  public NuovaVocePendenza datiAllegati(Object datiAllegati) {
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
  public NuovaVocePendenza descrizioneCausaleRPT(String descrizioneCausaleRPT) {
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
  public NuovaVocePendenza tipoBollo(TipoBolloEnum tipoBollo) {
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
  public NuovaVocePendenza hashDocumento(String hashDocumento) {
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
  public NuovaVocePendenza provinciaResidenza(String provinciaResidenza) {
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
  public NuovaVocePendenza codEntrata(String codEntrata) {
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
  public NuovaVocePendenza ibanAccredito(String ibanAccredito) {
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
  public NuovaVocePendenza ibanAppoggio(String ibanAppoggio) {
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
  public NuovaVocePendenza tipoContabilita(TipoContabilita tipoContabilita) {
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
  public NuovaVocePendenza codiceContabilita(String codiceContabilita) {
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
    NuovaVocePendenza nuovaVocePendenza = (NuovaVocePendenza) o;
    return Objects.equals(idVocePendenza, nuovaVocePendenza.idVocePendenza) &&
        Objects.equals(importo, nuovaVocePendenza.importo) &&
        Objects.equals(descrizione, nuovaVocePendenza.descrizione) &&
        Objects.equals(datiAllegati, nuovaVocePendenza.datiAllegati) &&
        Objects.equals(descrizioneCausaleRPT, nuovaVocePendenza.descrizioneCausaleRPT) &&
        Objects.equals(tipoBollo, nuovaVocePendenza.tipoBollo) &&
        Objects.equals(hashDocumento, nuovaVocePendenza.hashDocumento) &&
        Objects.equals(provinciaResidenza, nuovaVocePendenza.provinciaResidenza) &&
        Objects.equals(codEntrata, nuovaVocePendenza.codEntrata) &&
        Objects.equals(ibanAccredito, nuovaVocePendenza.ibanAccredito) &&
        Objects.equals(ibanAppoggio, nuovaVocePendenza.ibanAppoggio) &&
        Objects.equals(tipoContabilita, nuovaVocePendenza.tipoContabilita) &&
        Objects.equals(codiceContabilita, nuovaVocePendenza.codiceContabilita);
  }

  @Override
  public int hashCode() {
    return Objects.hash(idVocePendenza, importo, descrizione, datiAllegati, descrizioneCausaleRPT, tipoBollo, hashDocumento, provinciaResidenza, codEntrata, ibanAccredito, ibanAppoggio, tipoContabilita, codiceContabilita);
  }

  public static NuovaVocePendenza parse(String json) throws org.openspcoop2.generic_project.exception.ServiceException, org.openspcoop2.utils.json.ValidationException {
    return parse(json, NuovaVocePendenza.class);
  }

  @Override
  public String getJsonIdFilter() {
    return "nuovaVocePendenza";
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class NuovaVocePendenza {\n");
    
    sb.append("    idVocePendenza: ").append(toIndentedString(idVocePendenza)).append("\n");
    sb.append("    importo: ").append(toIndentedString(importo)).append("\n");
    sb.append("    descrizione: ").append(toIndentedString(descrizione)).append("\n");
    sb.append("    datiAllegati: ").append(toIndentedString(datiAllegati)).append("\n");
    sb.append("    descrizioneCausaleRPT: ").append(toIndentedString(descrizioneCausaleRPT)).append("\n");
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

	@Override
	public void validate() throws ValidationException {
		ValidatorFactory vf = ValidatorFactory.newInstance();

		vf.getValidator("idVocePendenza", this.idVocePendenza).notNull().minLength(1).maxLength(35);
		vf.getValidator("importo", this.importo).notNull().minOrEquals(BigDecimal.ZERO).maxOrEquals(BigDecimal.valueOf(999999.99)).checkDecimalDigits();
		vf.getValidator("descrizione", this.descrizione).notNull().minLength(1).maxLength(255);
		vf.getValidator("descrizioneCausaleRPT", this.descrizioneCausaleRPT).minLength(1).maxLength(140);
		
		if(this.codEntrata != null) {
			vf.getValidator("codEntrata", this.codEntrata).notNull().minLength(1).maxLength(35);
			try {
				vf.getValidator("tipoBollo", this.tipoBollo).isNull();
				vf.getValidator("hashDocumento", this.hashDocumento).isNull();
				vf.getValidator("provinciaResidenza", this.provinciaResidenza).isNull();
				vf.getValidator("ibanAccredito", this.ibanAccredito).isNull();
				vf.getValidator("ibanAppoggio", this.ibanAppoggio).isNull();
				vf.getValidator("tipoContabilita", this.tipoContabilita).isNull();
				vf.getValidator("codiceContabilita", this.codiceContabilita).isNull();
			} catch (ValidationException ve) {
				throw new ValidationException("Valorizzato codEntrata. " + ve.getMessage());
			}

			return;
		}

		else if(this.tipoBollo != null) {
			vf.getValidator("tipoBollo", this.tipoBollo).notNull();
			vf.getValidator("hashDocumento", this.hashDocumento).notNull().minLength(1).maxLength(70);
			vf.getValidator("provinciaResidenza", this.provinciaResidenza).notNull().pattern(CostantiValidazione.PATTERN_PROVINCIA);

			try {
				vf.getValidator("ibanAccredito", this.ibanAccredito).isNull();
				vf.getValidator("ibanAppoggio", this.ibanAppoggio).isNull();
				vf.getValidator("tipoContabilita", this.tipoContabilita).isNull();
				vf.getValidator("codiceContabilita", this.codiceContabilita).isNull();
			} catch (ValidationException ve) {
				throw new ValidationException("Valorizzato tipoBollo. " + ve.getMessage());
			}

			return;
		}


		else if(this.ibanAccredito != null) {
			vf.getValidator("ibanAccredito", this.ibanAccredito).notNull().pattern(CostantiValidazione.PATTERN_IBAN_ACCREDITO);
			vf.getValidator("ibanAppoggio", this.ibanAppoggio).pattern(CostantiValidazione.PATTERN_IBAN_ACCREDITO);
			vf.getValidator("tipoContabilita", this.tipoContabilita).notNull();
			ValidatoreUtils.validaCodiceContabilita(vf, "codiceContabilita", this.codiceContabilita);

			try {
				vf.getValidator("hashDocumento", this.hashDocumento).isNull();
				vf.getValidator("provinciaResidenza", this.provinciaResidenza).isNull();
			} catch (ValidationException ve) {
				throw new ValidationException("Valorizzato ibanAccredito. " + ve.getMessage());
			}
		}
		
		else {
			throw new ValidationException("Nella voce di pendenza deve essere valorizzato uno tra codEntrata, tipoBollo o ibanAccredito.");
		}

	}
}



