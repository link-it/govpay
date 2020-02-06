package it.govpay.backoffice.v1.beans;

import java.math.BigDecimal;
import java.util.List;
import java.util.Objects;

import org.openspcoop2.utils.json.ValidationException;

import com.fasterxml.jackson.annotation.JsonProperty;

import it.govpay.core.utils.validator.CostantiValidazione;
import it.govpay.core.utils.validator.IValidable;
import it.govpay.core.utils.validator.ValidatorFactory;
@com.fasterxml.jackson.annotation.JsonPropertyOrder({
"indice",
"idVocePendenza",
"importo",
"descrizione",
"stato",
"descrizioneCausaleRPT",
"datiAllegati",
"hashDocumento",
"tipoBollo",
"provinciaResidenza",
"codEntrata",
"codiceContabilita",
"ibanAccredito",
"ibanAppoggio",
"tipoContabilita",
"riscossioni",
"rendicontazioni",
})
public class VocePendenza extends it.govpay.core.beans.JSONSerializable implements IValidable{
	
	private static final String FIELD_ID_VOCE_PENDENZA = "idVocePendenza";
	private static final String FIELD_IMPORTO = "importo";
	private static final String FIELD_COD_ENTRATA = "codEntrata";
	private static final String FIELD_TIPO_BOLLO = "tipoBollo";
	private static final String FIELD_HASH_DOCUMENTO = "hashDocumento";
	private static final String FIELD_PROVINCIA_RESIDENZA = "provinciaResidenza";
	private static final String FIELD_IBAN_ACCREDITO = "ibanAccredito";
	private static final String FIELD_IBAN_APPOGGIO = "ibanAppoggio";
	private static final String FIELD_TIPO_CONTABILITA = "tipoContabilita";
	private static final String FIELD_CODICE_CONTABILITA = "codiceContabilita";
  
  @JsonProperty("indice")
  private BigDecimal indice = null;
  
  @JsonProperty("idVocePendenza")
  private String idVocePendenza = null;
  
  @JsonProperty("importo")
  private BigDecimal importo = null;
  
  @JsonProperty("descrizione")
  private String descrizione = null;
  
  @JsonProperty("stato")
  private StatoVocePendenza stato = null;
  
  @JsonProperty("descrizioneCausaleRPT")
  private String descrizioneCausaleRPT = null;
  
  @JsonProperty("datiAllegati")
  private Object datiAllegati = null;
  
  @JsonProperty("hashDocumento")
  private String hashDocumento= null;
  
  @JsonProperty("tipoBollo")
  private String tipoBollo= null;
  
  @JsonProperty("provinciaResidenza")
  private String provinciaResidenza= null;
  
  @JsonProperty("codEntrata")
  private String codEntrata= null;
  
  @JsonProperty("codiceContabilita")
  private String codiceContabilita= null;
  
  @JsonProperty("ibanAccredito")
  private String ibanAccredito= null;
  
  @JsonProperty("ibanAppoggio")
  private String ibanAppoggio= null;
  
  @JsonProperty("tipoContabilita")
  private TipoContabilita tipoContabilita= null;

  @JsonProperty("riscossioni")
  private List<Riscossione> riscossioni = null;
  
  @JsonProperty("rendicontazioni")
  private List<Rendicontazione> rendicontazioni = null;
  
  /**
   * indice di voce all'interno della pendenza
   **/
  public VocePendenza indice(BigDecimal indice) {
    this.indice = indice;
    return this;
  }

  @JsonProperty("indice")
  public BigDecimal getIndice() {
    return this.indice;
  }
  public void setIndice(BigDecimal indice) {
    this.indice = indice;
  }

  /**
   * Identificativo della voce di pedenza nel gestionale proprietario
   **/
  public VocePendenza idVocePendenza(String idVocePendenza) {
    this.idVocePendenza = idVocePendenza;
    return this;
  }

  @JsonProperty("idVocePendenza")
  public String getIdVocePendenza() {
    return this.idVocePendenza;
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
    return this.importo;
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
    return this.descrizione;
  }
  public void setDescrizione(String descrizione) {
    this.descrizione = descrizione;
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

  public VocePendenza hashDocumento(String hashDocumento) {
    this.hashDocumento = hashDocumento;
    return this;
  }

  @JsonProperty("hashDocumento")
  public String getHashDocumento() {
    return this.hashDocumento;
  }
  public void setHashDocumento(String hashDocumento) {
    this.hashDocumento = hashDocumento;
  }

  public VocePendenza tipoBollo(String tipoBollo) {
    this.tipoBollo = tipoBollo;
    return this;
  }

  @JsonProperty("tipoBollo")
  public String getTipoBollo() {
    return this.tipoBollo;
  }
  public void setTipoBollo(String tipoBollo) {
    this.tipoBollo = tipoBollo;
  }

  public VocePendenza codEntrata(String codEntrata) {
    this.codEntrata = codEntrata;
    return this;
  }

  @JsonProperty("codEntrata")
  public String getCodEntrata() {
    return this.codEntrata;
  }
  public void setCodEntrata(String codEntrata) {
    this.codEntrata= codEntrata;
  }

  public VocePendenza provinciaResidenza(String provinciaResidenza) {
    this.provinciaResidenza = provinciaResidenza;
    return this;
  }

  @JsonProperty("provinciaResidenza")
  public String getProvinciaResidenza() {
    return this.provinciaResidenza;
  }
  public void setProvinciaResidenza(String provinciaResidenza) {
    this.provinciaResidenza = provinciaResidenza;
  }

  public VocePendenza codiceContabilita(String codiceContabilita) {
    this.codiceContabilita= codiceContabilita;
    return this;
  }

  @JsonProperty("codiceContabilita")
  public String getCodiceContabilita() {
    return this.codiceContabilita;
  }
  public void setCodiceContabilita(String CodiceContabilita) {
    this.codiceContabilita = CodiceContabilita;
  }

  public VocePendenza ibanAccredito(String ibanAccredito) {
    this.ibanAccredito= ibanAccredito;
    return this;
  }

  @JsonProperty("ibanAccredito")
  public String getIbanAccredito() {
    return this.ibanAccredito;
  }
  public void setIbanAccredito(String ibanAccredito) {
    this.ibanAccredito = ibanAccredito;
  }

  public VocePendenza tipoContabilita(TipoContabilita tipoContabilita) {
    this.tipoContabilita= tipoContabilita;
    return this;
  }
  
	public VocePendenza ibanAppoggio(String ibanAppoggio) {
		this.ibanAppoggio= ibanAppoggio;
		return this;
	}

	@JsonProperty("ibanAppoggio")
	public String getIbanAppoggio() {
		return this.ibanAppoggio;
	}
	public void setIbanAppoggio(String ibanAppoggio) {
		this.ibanAppoggio = ibanAppoggio;
	}

  @JsonProperty("tipoContabilita")
  public TipoContabilita getTipoContabilita() {
    return this.tipoContabilita;
  }
  public void setTipoContabilita(TipoContabilita tipoContabilita) {
    this.tipoContabilita = tipoContabilita;
  }

 /**
   **/
  public VocePendenza riscossioni(List<Riscossione> riscossioni) {
    this.riscossioni = riscossioni;
    return this;
  }

  @JsonProperty("riscossioni")
  public List<Riscossione> getRiscossioni() {
    return riscossioni;
  }
  public void setRiscossioni(List<Riscossione> riscossioni) {
    this.riscossioni = riscossioni;
  }

  /**
   **/
  public VocePendenza rendicontazioni(List<Rendicontazione> rendicontazioni) {
    this.rendicontazioni = rendicontazioni;
    return this;
  }

  @JsonProperty("rendicontazioni")
  public List<Rendicontazione> getRendicontazioni() {
    return rendicontazioni;
  }
  public void setRendicontazioni(List<Rendicontazione> rendicontazioni) {
    this.rendicontazioni = rendicontazioni;
  }

  @Override
  public boolean equals(java.lang.Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || this.getClass() != o.getClass()) {
      return false;
    }
    VocePendenza vocePendenza = (VocePendenza) o;
    return Objects.equals(this.indice, vocePendenza.indice) &&
        Objects.equals(this.idVocePendenza, vocePendenza.idVocePendenza) &&
        Objects.equals(this.importo, vocePendenza.importo) &&
        Objects.equals(this.descrizione, vocePendenza.descrizione) &&
        Objects.equals(this.stato, vocePendenza.stato) &&
        Objects.equals(descrizioneCausaleRPT, vocePendenza.descrizioneCausaleRPT) &&
        Objects.equals(this.datiAllegati, vocePendenza.datiAllegati) &&
        Objects.equals(this.hashDocumento, vocePendenza.hashDocumento) &&
        Objects.equals(this.tipoBollo, vocePendenza.tipoBollo) &&
        Objects.equals(this.provinciaResidenza, vocePendenza.provinciaResidenza) &&
        Objects.equals(this.codiceContabilita, vocePendenza.codiceContabilita) &&
        Objects.equals(this.ibanAccredito, vocePendenza.ibanAccredito) &&
        Objects.equals(this.ibanAppoggio, vocePendenza.ibanAppoggio) &&
        Objects.equals(this.tipoContabilita, vocePendenza.tipoContabilita) &&
        Objects.equals(riscossioni, vocePendenza.riscossioni) &&
        Objects.equals(rendicontazioni, vocePendenza.rendicontazioni);
  }

  @Override
  public int hashCode() {
    return Objects.hash(this.indice, this.idVocePendenza, this.importo, this.descrizione, this.stato, descrizioneCausaleRPT, this.datiAllegati, this.hashDocumento, this.tipoBollo, this.provinciaResidenza, this.codiceContabilita, this.ibanAccredito, this.tipoContabilita,riscossioni, rendicontazioni);
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
    
    sb.append("    indice: ").append(this.toIndentedString(this.indice)).append("\n");
    sb.append("    idVocePendenza: ").append(this.toIndentedString(this.idVocePendenza)).append("\n");
    sb.append("    importo: ").append(this.toIndentedString(this.importo)).append("\n");
    sb.append("    descrizione: ").append(this.toIndentedString(this.descrizione)).append("\n");
    sb.append("    stato: ").append(this.toIndentedString(this.stato)).append("\n");
    sb.append("    descrizioneCausaleRPT: ").append(toIndentedString(descrizioneCausaleRPT)).append("\n");
    sb.append("    datiAllegati: ").append(this.toIndentedString(this.datiAllegati)).append("\n");
    sb.append("    hashDocumento: ").append(this.toIndentedString(this.hashDocumento)).append("\n");
    sb.append("    tipoBollo: ").append(this.toIndentedString(this.tipoBollo)).append("\n");
    sb.append("    provinciaResidenza: ").append(this.toIndentedString(this.provinciaResidenza)).append("\n");
    sb.append("    codiceContabilita: ").append(this.toIndentedString(this.codiceContabilita)).append("\n");
    sb.append("    ibanAccredito: ").append(this.toIndentedString(this.ibanAccredito)).append("\n");
    sb.append("    ibanAppoggio: ").append(this.toIndentedString(this.ibanAppoggio)).append("\n");
    sb.append("    tipoContabilita: ").append(this.toIndentedString(this.tipoContabilita)).append("\n");
    sb.append("    riscossioni: ").append(toIndentedString(riscossioni)).append("\n");
    sb.append("    rendicontazioni: ").append(toIndentedString(rendicontazioni)).append("\n");
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
		
		vf.getValidator(FIELD_ID_VOCE_PENDENZA, this.idVocePendenza).notNull().minLength(1).maxLength(35);
		vf.getValidator(FIELD_IMPORTO, this.importo).notNull().minOrEquals(BigDecimal.ZERO).maxOrEquals(BigDecimal.valueOf(999999.99)).checkDecimalDigits();
		vf.getValidator("descrizione", this.descrizione).notNull().minLength(1).maxLength(255);
		vf.getValidator("descrizioneCausaleRPT", this.descrizioneCausaleRPT).minLength(1).maxLength(140);
		
		if(this.codEntrata != null) {
			vf.getValidator(FIELD_COD_ENTRATA, this.codEntrata).notNull().minLength(1).maxLength(35);
			try {
				vf.getValidator(FIELD_TIPO_BOLLO, this.tipoBollo).isNull();
				vf.getValidator(FIELD_HASH_DOCUMENTO, this.hashDocumento).isNull();
				vf.getValidator(FIELD_PROVINCIA_RESIDENZA, this.provinciaResidenza).isNull();
				vf.getValidator(FIELD_IBAN_ACCREDITO, this.ibanAccredito).isNull();
				vf.getValidator(FIELD_IBAN_APPOGGIO, this.ibanAppoggio).isNull();
				vf.getValidator(FIELD_TIPO_CONTABILITA, this.tipoContabilita).isNull();
				vf.getValidator(FIELD_CODICE_CONTABILITA, this.codiceContabilita).isNull();
			} catch (ValidationException ve) {
				throw new ValidationException("Valorizzato codEntrata. " + ve.getMessage());
			}

			return;
		}

		else if(this.tipoBollo != null) {
			vf.getValidator(FIELD_TIPO_BOLLO, this.tipoBollo).notNull();
			vf.getValidator(FIELD_HASH_DOCUMENTO, this.hashDocumento).notNull().minLength(1).maxLength(70);
			vf.getValidator(FIELD_PROVINCIA_RESIDENZA, this.provinciaResidenza).notNull().pattern(CostantiValidazione.PATTERN_PROVINCIA);

			try {
				vf.getValidator(FIELD_IBAN_ACCREDITO, this.ibanAccredito).isNull();
				vf.getValidator(FIELD_IBAN_APPOGGIO, this.ibanAppoggio).isNull();
				vf.getValidator(FIELD_TIPO_CONTABILITA, this.tipoContabilita).isNull();
				vf.getValidator(FIELD_CODICE_CONTABILITA, this.codiceContabilita).isNull();
			} catch (ValidationException ve) {
				throw new ValidationException("Valorizzato tipoBollo. " + ve.getMessage());
			}

			return;
		}


		else if(this.ibanAccredito != null) {
			vf.getValidator(FIELD_IBAN_ACCREDITO, this.ibanAccredito).notNull().pattern(CostantiValidazione.PATTERN_IBAN_ACCREDITO);
			vf.getValidator(FIELD_IBAN_APPOGGIO, this.ibanAppoggio).pattern(CostantiValidazione.PATTERN_IBAN_ACCREDITO);
			vf.getValidator(FIELD_TIPO_CONTABILITA, this.tipoContabilita).notNull();
			vf.getValidator(FIELD_CODICE_CONTABILITA, this.codiceContabilita).notNull().pattern(CostantiValidazione.PATTERN_COD_CONTABILITA).maxLength(255);

			try {
				vf.getValidator(FIELD_HASH_DOCUMENTO, this.hashDocumento).isNull();
				vf.getValidator(FIELD_PROVINCIA_RESIDENZA, this.provinciaResidenza).isNull();
			} catch (ValidationException ve) {
				throw new ValidationException("Valorizzato ibanAccredito. " + ve.getMessage());
			}
		}
		
		else {
			throw new ValidationException("Uno dei campi tra ibanAccredito, tipoBollo o codEntrata deve essere valorizzato");
		}

	}
}



