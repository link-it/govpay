package it.govpay.pendenze.v1.beans;

import java.util.Objects;

import org.openspcoop2.utils.json.ValidationException;

import com.fasterxml.jackson.annotation.JsonProperty;

import it.govpay.core.ec.v1.validator.SoggettoPagatoreValidator;
import it.govpay.core.utils.validator.IValidable;

/**
 * dati anagrafici di un versante o pagatore.
 **/@com.fasterxml.jackson.annotation.JsonPropertyOrder({
	 "tipo",
	 "identificativo",
	 "anagrafica",
	 "indirizzo",
	 "civico",
	 "cap",
	 "localita",
	 "provincia",
	 "nazione",
	 "email",
	 "cellulare",
 })
 public class Soggetto extends it.govpay.core.beans.JSONSerializable implements IValidable {


	 /**
	  * tipologia di soggetto, se persona fisica (F) o giuridica (G)
	  */
	 public enum TipoEnum {




		 G("G"),


		 F("F");




		 private String value;

		 TipoEnum(String value) {
			 this.value = value;
		 }

		 @Override
		 @com.fasterxml.jackson.annotation.JsonValue
		 public String toString() {
			 return String.valueOf(this.value);
		 }

		 public static TipoEnum fromValue(String text) {
			 for (TipoEnum b : TipoEnum.values()) {
				 if (String.valueOf(b.value).equals(text)) {
					 return b;
				 }
			 }
			 return null;
		 }
	 }



	 @JsonProperty("tipo")
	 private TipoEnum tipo = null;

	 @JsonProperty("identificativo")
	 private String identificativo = null;

	 @JsonProperty("anagrafica")
	 private String anagrafica = null;

	 @JsonProperty("indirizzo")
	 private String indirizzo = null;

	 @JsonProperty("civico")
	 private String civico = null;

	 @JsonProperty("cap")
	 private String cap = null;

	 @JsonProperty("localita")
	 private String localita = null;

	 @JsonProperty("provincia")
	 private String provincia = null;

	 @JsonProperty("nazione")
	 private String nazione = null;

	 @JsonProperty("email")
	 private String email = null;

	 @JsonProperty("cellulare")
	 private String cellulare = null;

	 /**
	  * tipologia di soggetto, se persona fisica (F) o giuridica (G)
	  **/
	 public Soggetto tipo(TipoEnum tipo) {
		 this.tipo = tipo;
		 return this;
	 }

	 @JsonProperty("tipo")
	 public TipoEnum getTipo() {
		 return this.tipo;
	 }
	 public void setTipo(TipoEnum tipo) {
		 this.tipo = tipo;
	 }

	 /**
	  * codice fiscale o partita iva del soggetto
	  **/
	 public Soggetto identificativo(String identificativo) {
		 this.identificativo = identificativo;
		 return this;
	 }

	 @JsonProperty("identificativo")
	 public String getIdentificativo() {
		 return this.identificativo;
	 }
	 public void setIdentificativo(String identificativo) {
		 this.identificativo = identificativo;
	 }

	 /**
	  * nome e cognome o altra ragione sociale del soggetto
	  **/
	 public Soggetto anagrafica(String anagrafica) {
		 this.anagrafica = anagrafica;
		 return this;
	 }

	 @JsonProperty("anagrafica")
	 public String getAnagrafica() {
		 return this.anagrafica;
	 }
	 public void setAnagrafica(String anagrafica) {
		 this.anagrafica = anagrafica;
	 }

	 /**
	  **/
	 public Soggetto indirizzo(String indirizzo) {
		 this.indirizzo = indirizzo;
		 return this;
	 }

	 @JsonProperty("indirizzo")
	 public String getIndirizzo() {
		 return this.indirizzo;
	 }
	 public void setIndirizzo(String indirizzo) {
		 this.indirizzo = indirizzo;
	 }

	 /**
	  **/
	 public Soggetto civico(String civico) {
		 this.civico = civico;
		 return this;
	 }

	 @JsonProperty("civico")
	 public String getCivico() {
		 return this.civico;
	 }
	 public void setCivico(String civico) {
		 this.civico = civico;
	 }

	 /**
	  **/
	 public Soggetto cap(String cap) {
		 this.cap = cap;
		 return this;
	 }

	 @JsonProperty("cap")
	 public String getCap() {
		 return this.cap;
	 }
	 public void setCap(String cap) {
		 this.cap = cap;
	 }

	 /**
	  **/
	 public Soggetto localita(String localita) {
		 this.localita = localita;
		 return this;
	 }

	 @JsonProperty("localita")
	 public String getLocalita() {
		 return this.localita;
	 }
	 public void setLocalita(String localita) {
		 this.localita = localita;
	 }

	 /**
	  **/
	 public Soggetto provincia(String provincia) {
		 this.provincia = provincia;
		 return this;
	 }

	 @JsonProperty("provincia")
	 public String getProvincia() {
		 return this.provincia;
	 }
	 public void setProvincia(String provincia) {
		 this.provincia = provincia;
	 }

	 /**
	  **/
	 public Soggetto nazione(String nazione) {
		 this.nazione = nazione;
		 return this;
	 }

	 @JsonProperty("nazione")
	 public String getNazione() {
		 return this.nazione;
	 }
	 public void setNazione(String nazione) {
		 this.nazione = nazione;
	 }

	 /**
	  **/
	 public Soggetto email(String email) {
		 this.email = email;
		 return this;
	 }

	 @JsonProperty("email")
	 public String getEmail() {
		 return this.email;
	 }
	 public void setEmail(String email) {
		 this.email = email;
	 }

	 /**
	  **/
	 public Soggetto cellulare(String cellulare) {
		 this.cellulare = cellulare;
		 return this;
	 }

	 @JsonProperty("cellulare")
	 public String getCellulare() {
		 return this.cellulare;
	 }
	 public void setCellulare(String cellulare) {
		 this.cellulare = cellulare;
	 }

	 @Override
	 public boolean equals(java.lang.Object o) {
		 if (this == o) {
			 return true;
		 }
		 if (o == null || this.getClass() != o.getClass()) {
			 return false;
		 }
		 Soggetto soggetto = (Soggetto) o;
		 return Objects.equals(this.tipo, soggetto.tipo) &&
				 Objects.equals(this.identificativo, soggetto.identificativo) &&
				 Objects.equals(this.anagrafica, soggetto.anagrafica) &&
				 Objects.equals(this.indirizzo, soggetto.indirizzo) &&
				 Objects.equals(this.civico, soggetto.civico) &&
				 Objects.equals(this.cap, soggetto.cap) &&
				 Objects.equals(this.localita, soggetto.localita) &&
				 Objects.equals(this.provincia, soggetto.provincia) &&
				 Objects.equals(this.nazione, soggetto.nazione) &&
				 Objects.equals(this.email, soggetto.email) &&
				 Objects.equals(this.cellulare, soggetto.cellulare);
	 }

	 @Override
	 public int hashCode() {
		 return Objects.hash(this.tipo, this.identificativo, this.anagrafica, this.indirizzo, this.civico, this.cap, this.localita, this.provincia, this.nazione, this.email, this.cellulare);
	 }

	 public static Soggetto parse(String json) throws org.openspcoop2.generic_project.exception.ServiceException, ValidationException {
		 return parse(json, Soggetto.class);
	 }

	 @Override
	 public String getJsonIdFilter() {
		 return "soggetto";
	 }

	 @Override
	 public String toString() {
		 StringBuilder sb = new StringBuilder();
		 sb.append("class Soggetto {\n");

		 sb.append("    tipo: ").append(this.toIndentedString(this.tipo)).append("\n");
		 sb.append("    identificativo: ").append(this.toIndentedString(this.identificativo)).append("\n");
		 sb.append("    anagrafica: ").append(this.toIndentedString(this.anagrafica)).append("\n");
		 sb.append("    indirizzo: ").append(this.toIndentedString(this.indirizzo)).append("\n");
		 sb.append("    civico: ").append(this.toIndentedString(this.civico)).append("\n");
		 sb.append("    cap: ").append(this.toIndentedString(this.cap)).append("\n");
		 sb.append("    localita: ").append(this.toIndentedString(this.localita)).append("\n");
		 sb.append("    provincia: ").append(this.toIndentedString(this.provincia)).append("\n");
		 sb.append("    nazione: ").append(this.toIndentedString(this.nazione)).append("\n");
		 sb.append("    email: ").append(this.toIndentedString(this.email)).append("\n");
		 sb.append("    cellulare: ").append(this.toIndentedString(this.cellulare)).append("\n");
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
		SoggettoPagatoreValidator soggettoPagatoreValidator = SoggettoPagatoreValidator.newInstance();
		
		soggettoPagatoreValidator.validaTipo("tipo", this.getTipo() != null ? this.getTipo().toString() : null);
		soggettoPagatoreValidator.validaIdentificativo("identificativo", this.getIdentificativo());
		soggettoPagatoreValidator.validaAnagrafica("anagrafica", this.getAnagrafica());
		soggettoPagatoreValidator.validaIndirizzo("indirizzo", this.getIndirizzo());
		soggettoPagatoreValidator.validaCivico("civico", this.getCivico());
		soggettoPagatoreValidator.validaCap("cap", this.getCap());
		soggettoPagatoreValidator.validaLocalita("localita", this.getLocalita());
		soggettoPagatoreValidator.validaProvincia("provincia", this.getProvincia());
		soggettoPagatoreValidator.validaNazione("nazione", this.getNazione());
		soggettoPagatoreValidator.validaEmail("email", this.getEmail());
		soggettoPagatoreValidator.validaCellulare("cellulare", this.getCellulare());
	 }
 }



