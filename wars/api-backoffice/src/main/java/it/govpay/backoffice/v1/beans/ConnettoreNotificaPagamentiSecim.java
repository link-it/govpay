package it.govpay.backoffice.v1.beans;


import java.util.List;
import java.util.Objects;

import org.apache.commons.lang.StringUtils;
import org.openspcoop2.generic_project.exception.ServiceException;
import org.openspcoop2.utils.json.ValidationException;

import com.fasterxml.jackson.annotation.JsonProperty;

import it.govpay.backoffice.v1.controllers.ApplicazioniController;
import it.govpay.core.beans.JSONSerializable;
import it.govpay.core.utils.validator.CostantiValidazione;
import it.govpay.core.utils.validator.IValidable;
import it.govpay.core.utils.validator.ValidatorFactory;
import it.govpay.core.utils.validator.ValidatoreIdentificativi;
@com.fasterxml.jackson.annotation.JsonPropertyOrder({
"abilitato",
"codiceCliente",
"codiceIstituto",
"tipoConnettore",
"versioneCsv",
"emailIndirizzi",
"fileSystemPath",
"tipiPendenza",
})
public class ConnettoreNotificaPagamentiSecim extends JSONSerializable implements IValidable {
  
  @JsonProperty("abilitato")
  private Boolean abilitato = null;
  
  @JsonProperty("codiceCliente")
  private String codiceCliente = null;
  
  @JsonProperty("codiceIstituto")
  private String codiceIstituto = null;
  
    
  /**
   * Gets or Sets tipoConnettore
   */
  public enum TipoConnettoreEnum {
    
    
        
            
    EMAIL("EMAIL"),
    
            
    FILESYSTEM("FILESYSTEM");
            
        
    

    private String value;

    TipoConnettoreEnum(String value) {
      this.value = value;
    }

    @Override
    @com.fasterxml.jackson.annotation.JsonValue
    public String toString() {
      return String.valueOf(value);
    }

    public static TipoConnettoreEnum fromValue(String text) {
      for (TipoConnettoreEnum b : TipoConnettoreEnum.values()) {
        if (String.valueOf(b.value).equals(text)) {
          return b;
        }
      }
      return null;
    }
  }

    
    
  @JsonProperty("tipoConnettore")
  private TipoConnettoreEnum tipoConnettore = null;
  
  @JsonProperty("versioneCsv")
  private String versioneCsv = null;
  
  @JsonProperty("emailIndirizzi")
  private List<String> emailIndirizzi = null;
  
  @JsonProperty("fileSystemPath")
  private String fileSystemPath = null;
  
  @JsonProperty("tipiPendenza")
  private List<TipoPendenzaProfiloIndex> tipiPendenza = null;
  
  /**
   * Indica se il connettore e' abilitato
   **/
  public ConnettoreNotificaPagamentiSecim abilitato(Boolean abilitato) {
    this.abilitato = abilitato;
    return this;
  }

  @JsonProperty("abilitato")
  public Boolean Abilitato() {
    return abilitato;
  }
  public void setAbilitato(Boolean abilitato) {
    this.abilitato = abilitato;
  }

  /**
   * Codice Cliente
   **/
  public ConnettoreNotificaPagamentiSecim codiceCliente(String codiceCliente) {
    this.codiceCliente = codiceCliente;
    return this;
  }

  @JsonProperty("codiceCliente")
  public String getCodiceCliente() {
    return codiceCliente;
  }
  public void setCodiceCliente(String codiceCliente) {
    this.codiceCliente = codiceCliente;
  }

  /**
   * Codice Istituto
   **/
  public ConnettoreNotificaPagamentiSecim codiceIstituto(String codiceIstituto) {
    this.codiceIstituto = codiceIstituto;
    return this;
  }

  @JsonProperty("codiceIstituto")
  public String getCodiceIstituto() {
    return codiceIstituto;
  }
  public void setCodiceIstituto(String codiceIstituto) {
    this.codiceIstituto = codiceIstituto;
  }

  /**
   **/
  public ConnettoreNotificaPagamentiSecim tipoConnettore(TipoConnettoreEnum tipoConnettore) {
    this.tipoConnettore = tipoConnettore;
    return this;
  }

  @JsonProperty("tipoConnettore")
  public TipoConnettoreEnum getTipoConnettore() {
    return tipoConnettore;
  }
  public void setTipoConnettore(TipoConnettoreEnum tipoConnettore) {
    this.tipoConnettore = tipoConnettore;
  }

  /**
   * Versione del CSV prodotto.
   **/
  public ConnettoreNotificaPagamentiSecim versioneCsv(String versioneCsv) {
    this.versioneCsv = versioneCsv;
    return this;
  }

  @JsonProperty("versioneCsv")
  public String getVersioneCsv() {
    return versioneCsv;
  }
  public void setVersioneCsv(String versioneCsv) {
    this.versioneCsv = versioneCsv;
  }

  /**
   * Indirizzi Email al quale verra' spedito il tracciato
   **/
  public ConnettoreNotificaPagamentiSecim emailIndirizzi(List<String> emailIndirizzi) {
    this.emailIndirizzi = emailIndirizzi;
    return this;
  }

  @JsonProperty("emailIndirizzi")
  public List<String> getEmailIndirizzi() {
    return emailIndirizzi;
  }
  public void setEmailIndirizzi(List<String> emailIndirizzi) {
    this.emailIndirizzi = emailIndirizzi;
  }

  /**
   * Path nel quale verra' salvato il tracciato
   **/
  public ConnettoreNotificaPagamentiSecim fileSystemPath(String fileSystemPath) {
    this.fileSystemPath = fileSystemPath;
    return this;
  }

  @JsonProperty("fileSystemPath")
  public String getFileSystemPath() {
    return fileSystemPath;
  }
  public void setFileSystemPath(String fileSystemPath) {
    this.fileSystemPath = fileSystemPath;
  }

  /**
   **/
  public ConnettoreNotificaPagamentiSecim tipiPendenza(List<TipoPendenzaProfiloIndex> tipiPendenza) {
    this.tipiPendenza = tipiPendenza;
    return this;
  }

  @JsonProperty("tipiPendenza")
  public List<TipoPendenzaProfiloIndex> getTipiPendenza() {
    return tipiPendenza;
  }
  public void setTipiPendenza(List<TipoPendenzaProfiloIndex> tipiPendenza) {
    this.tipiPendenza = tipiPendenza;
  }

  @Override
  public boolean equals(java.lang.Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    ConnettoreNotificaPagamentiSecim connettoreNotificaPagamentiSecim = (ConnettoreNotificaPagamentiSecim) o;
    return Objects.equals(abilitato, connettoreNotificaPagamentiSecim.abilitato) &&
        Objects.equals(codiceCliente, connettoreNotificaPagamentiSecim.codiceCliente) &&
        Objects.equals(codiceIstituto, connettoreNotificaPagamentiSecim.codiceIstituto) &&
        Objects.equals(tipoConnettore, connettoreNotificaPagamentiSecim.tipoConnettore) &&
        Objects.equals(versioneCsv, connettoreNotificaPagamentiSecim.versioneCsv) &&
        Objects.equals(emailIndirizzi, connettoreNotificaPagamentiSecim.emailIndirizzi) &&
        Objects.equals(fileSystemPath, connettoreNotificaPagamentiSecim.fileSystemPath) &&
        Objects.equals(tipiPendenza, connettoreNotificaPagamentiSecim.tipiPendenza);
  }

  @Override
  public int hashCode() {
    return Objects.hash(abilitato, codiceCliente, codiceIstituto, tipoConnettore, versioneCsv, emailIndirizzi, fileSystemPath, tipiPendenza);
  }

  public static ConnettoreNotificaPagamentiSecim parse(String json) throws ServiceException, ValidationException {
    return (ConnettoreNotificaPagamentiSecim) parse(json, ConnettoreNotificaPagamentiSecim.class);
  }

  @Override
  public String getJsonIdFilter() {
    return "connettoreNotificaPagamentiSecim";
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class ConnettoreNotificaPagamentiSecim {\n");
    
    sb.append("    abilitato: ").append(toIndentedString(abilitato)).append("\n");
    sb.append("    codiceCliente: ").append(toIndentedString(codiceCliente)).append("\n");
    sb.append("    codiceIstituto: ").append(toIndentedString(codiceIstituto)).append("\n");
    sb.append("    tipoConnettore: ").append(toIndentedString(tipoConnettore)).append("\n");
    sb.append("    versioneCsv: ").append(toIndentedString(versioneCsv)).append("\n");
    sb.append("    emailIndirizzi: ").append(toIndentedString(emailIndirizzi)).append("\n");
    sb.append("    fileSystemPath: ").append(toIndentedString(fileSystemPath)).append("\n");
    sb.append("    tipiPendenza: ").append(toIndentedString(tipiPendenza)).append("\n");
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
		vf.getValidator("abilitato", this.abilitato).notNull();
		
		if(this.abilitato) {
			vf.getValidator("tipoConnettore", this.tipoConnettore).notNull();
			vf.getValidator("versioneCsv", this.versioneCsv).notNull().minLength(1).maxLength(255);
			vf.getValidator("codiceCliente", this.codiceCliente).notNull().minLength(1).maxLength(7);
			vf.getValidator("codiceIstituto", this.codiceIstituto).minLength(1).maxLength(5);
			
			switch (this.tipoConnettore) {
			case EMAIL:
				if(this.emailIndirizzi != null && !this.emailIndirizzi.isEmpty()) {
					for (String indirizzo : emailIndirizzi) {
						vf.getValidator("emailIndirizzi", indirizzo).minLength(1).pattern(CostantiValidazione.PATTERN_EMAIL);
					}
					String v = StringUtils.join(this.emailIndirizzi, ",");
					vf.getValidator("emailIndirizzi", v).maxLength(4000);
				} else {
					throw new ValidationException("Il campo emailIndirizzi non deve essere vuoto.");
				}
				break;
			case FILESYSTEM:
				vf.getValidator("fileSystemPath", this.fileSystemPath).notNull().minLength(1).maxLength(4000);
				break;
			}
			
			if(this.tipiPendenza != null && !this.tipiPendenza.isEmpty()) {
				ValidatoreIdentificativi validatoreId = ValidatoreIdentificativi.newInstance();
				for (TipoPendenzaProfiloIndex idTipoPendenza : this.tipiPendenza) {
					if(!idTipoPendenza.getIdTipoPendenza().equals(ApplicazioniController.AUTORIZZA_TIPI_PENDENZA_STAR))
						validatoreId.validaIdTipoVersamento("tipiPendenza", idTipoPendenza.getIdTipoPendenza());
				}
			}
		}
	}
}



