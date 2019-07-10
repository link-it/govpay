package it.govpay.pagamento.v2.beans;

import java.math.BigDecimal;
import java.time.LocalDate;
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
public class PendenzaPost extends JSONSerializable implements IValidable {
	
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

	@JsonIgnore
	private TassonomiaAvviso tassonomiaAvvisoEnum = null;

	@JsonProperty("tassonomiaAvviso")
	private String tassonomiaAvviso = null;

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
		return this.datiAllegati;
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
	public PendenzaPost tassonomiaAvvisoEnum(TassonomiaAvviso tassonomiaAvviso) {
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
	public PendenzaPost tassonomiaAvviso(String tassonomiaAvviso) {
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
		return Objects.equals(this.idDominio, pendenzaPost.idDominio) &&
				Objects.equals(this.idUnitaOperativa, pendenzaPost.idUnitaOperativa) &&
			    Objects.equals(idTipoPendenza, pendenzaPost.idTipoPendenza) &&				
			    Objects.equals(this.nome, pendenzaPost.nome) &&
				Objects.equals(this.causale, pendenzaPost.causale) &&
				Objects.equals(this.soggettoPagatore, pendenzaPost.soggettoPagatore) &&
				Objects.equals(this.importo, pendenzaPost.importo) &&
				Objects.equals(this.numeroAvviso, pendenzaPost.numeroAvviso) &&
				Objects.equals(this.dataCaricamento, pendenzaPost.dataCaricamento) &&
				Objects.equals(this.dataValidita, pendenzaPost.dataValidita) &&
				Objects.equals(this.dataScadenza, pendenzaPost.dataScadenza) &&
				Objects.equals(this.annoRiferimento, pendenzaPost.annoRiferimento) &&
				Objects.equals(this.cartellaPagamento, pendenzaPost.cartellaPagamento) &&
				Objects.equals(this.datiAllegati, pendenzaPost.datiAllegati) &&
				Objects.equals(this.tassonomia, pendenzaPost.tassonomia) &&
				Objects.equals(this.tassonomiaAvviso, pendenzaPost.tassonomiaAvviso) &&
        Objects.equals(direzione, pendenzaPost.direzione) &&
        Objects.equals(divisione, pendenzaPost.divisione) &&
				Objects.equals(this.voci, pendenzaPost.voci) &&
				Objects.equals(this.idA2A, pendenzaPost.idA2A) &&
				Objects.equals(this.idPendenza, pendenzaPost.idPendenza) &&
				Objects.equals(idDebitore, pendenzaPost.idDebitore) &&
				Objects.equals(this.dati, pendenzaPost.dati);
	}

	@Override
	public int hashCode() {
		return Objects.hash(this.idDominio, this.idUnitaOperativa, idTipoPendenza, this.nome, this.causale, this.soggettoPagatore, this.importo, this.numeroAvviso, this.dataCaricamento, this.dataValidita, this.dataScadenza, this.annoRiferimento, this.cartellaPagamento, this.datiAllegati, this.tassonomia, this.tassonomiaAvviso, direzione, divisione, this.voci, this.idA2A, this.idPendenza, this.idDebitore, this.dati);
	}

	public static PendenzaPost parse(String json) throws org.openspcoop2.generic_project.exception.ServiceException, org.openspcoop2.utils.json.ValidationException {
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

		sb.append("    idDominio: ").append(this.toIndentedString(this.idDominio)).append("\n");
		sb.append("    idUnitaOperativa: ").append(this.toIndentedString(this.idUnitaOperativa)).append("\n");
		sb.append("    idTipoPendenza: ").append(toIndentedString(idTipoPendenza)).append("\n");
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
		sb.append("    direzione: ").append(toIndentedString(direzione)).append("\n");
		sb.append("    divisione: ").append(toIndentedString(divisione)).append("\n");
		sb.append("    voci: ").append(this.toIndentedString(this.voci)).append("\n");
		sb.append("    idA2A: ").append(this.toIndentedString(this.idA2A)).append("\n");
		sb.append("    idPendenza: ").append(this.toIndentedString(this.idPendenza)).append("\n");
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
				vf.getValidator("nome", this.nome).isNull();
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
					vf.getValidator("nome", this.nome).isNull();
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
					vf.getValidator("nome", this.nome).isNull();
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
				} catch (ValidationException ve) {
					throw new ValidationException("Pendenza modello 4. " + ve.getMessage());
				}
			}
		} else {
			validatoreId.validaIdDominio("idDominio", this.idDominio);
			if(this.idUnitaOperativa != null)
				validatoreId.validaIdUO("idUnitaOperativa", this.idUnitaOperativa);
			if(this.idTipoPendenza != null)
				validatoreId.validaIdTipoVersamento("idTipoPendenza", this.idTipoPendenza);
			vf.getValidator("nome", this.nome).minLength(1).maxLength(35);
			vf.getValidator("causale", this.causale).notNull().minLength(1).maxLength(140);
			vf.getValidator("soggettoPagatore", this.soggettoPagatore).notNull().validateFields();
			vf.getValidator("importo", this.importo).notNull().minOrEquals(BigDecimal.ZERO).maxOrEquals(BigDecimal.valueOf(999999.99)).checkDecimalDigits();
			vf.getValidator("numeroAvviso", this.numeroAvviso).pattern("[0-9]{18}");
			vf.getValidator("dataValidita", this.dataValidita).after(LocalDate.now());
			vf.getValidator("dataScadenza", this.dataScadenza).after(LocalDate.now());
			if(this.annoRiferimento != null)
				vf.getValidator("annoRiferimento", this.annoRiferimento.toBigInteger().toString()).pattern("[0-9]{4}");
			vf.getValidator("cartellaPagamento", this.cartellaPagamento).minLength(1).maxLength(35);
			vf.getValidator("idA2A", this.idA2A).notNull().minLength(1).maxLength(35);
			vf.getValidator("voci", this.voci).notNull().minItems(1).maxItems(5).validateObjects();
			
			validatoreId.validaIdPendenza("idPendenza", this.idPendenza);
			
			if(this.direzione != null)
				validatoreId.validaIdDirezione("direzione",this.direzione);
			if(this.divisione != null)
				validatoreId.validaIdDivisione("divisione",this.divisione);
		}
	}
}



