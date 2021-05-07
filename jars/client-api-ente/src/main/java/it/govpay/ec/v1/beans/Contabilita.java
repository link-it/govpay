package it.govpay.ec.v1.beans;

import java.math.BigDecimal;
import java.util.Objects;

import com.fasterxml.jackson.annotation.JsonProperty;

public class Contabilita {

	private String ufficio = null;

	private String capitolo = null;

	private BigDecimal annoEsercizio = null;

	private String accertamento = null;

	private BigDecimal annoAccertamento = null;

	private String subAccertamento = null;

	private String siope = null;

	private BigDecimal importo = null;

	private String codGestionaleEnte = null;

	/**
	 * Codice dell’ufficio
	 **/
	 public Contabilita ufficio(String ufficio) {
		 this.ufficio = ufficio;
		 return this;
	 }

	 @JsonProperty("ufficio")
	 public String getUfficio() {
		 return ufficio;
	 }
	 public void setUfficio(String ufficio) {
		 this.ufficio = ufficio;
	 }

	 /**
	  * Codice del capitolo
	  **/
	 public Contabilita capitolo(String capitolo) {
		 this.capitolo = capitolo;
		 return this;
	 }

	 @JsonProperty("capitolo")
	 public String getCapitolo() {
		 return capitolo;
	 }
	 public void setCapitolo(String capitolo) {
		 this.capitolo = capitolo;
	 }

	 /**
	  * Anno di esercizio
	  **/
	 public Contabilita annoEsercizio(BigDecimal annoEsercizio) {
		 this.annoEsercizio = annoEsercizio;
		 return this;
	 }

	 @JsonProperty("annoEsercizio")
	 public BigDecimal getAnnoEsercizio() {
		 return annoEsercizio;
	 }
	 public void setAnnoEsercizio(BigDecimal annoEsercizio) {
		 this.annoEsercizio = annoEsercizio;
	 }

	 /**
	  * Codice dell’accertamento
	  **/
	 public Contabilita accertamento(String accertamento) {
		 this.accertamento = accertamento;
		 return this;
	 }

	 @JsonProperty("accertamento")
	 public String getAccertamento() {
		 return accertamento;
	 }
	 public void setAccertamento(String accertamento) {
		 this.accertamento = accertamento;
	 }

	 /**
	  * Anno accertamento
	  **/
	 public Contabilita annoAccertamento(BigDecimal annoAccertamento) {
		 this.annoAccertamento = annoAccertamento;
		 return this;
	 }

	 @JsonProperty("annoAccertamento")
	 public BigDecimal getAnnoAccertamento() {
		 return annoAccertamento;
	 }
	 public void setAnnoAccertamento(BigDecimal annoAccertamento) {
		 this.annoAccertamento = annoAccertamento;
	 }

	 /**
	  * Codice dell’accertamento se contabilita' di terze parti
	  **/
	 public Contabilita subAccertamento(String subAccertamento) {
		 this.subAccertamento = subAccertamento;
		 return this;
	 }

	 @JsonProperty("subAccertamento")
	 public String getSubAccertamento() {
		 return subAccertamento;
	 }
	 public void setSubAccertamento(String subAccertamento) {
		 this.subAccertamento = subAccertamento;
	 }

	 /**
	  * Codice siope dell'entrata
	  **/
	 public Contabilita siope(String siope) {
		 this.siope = siope;
		 return this;
	 }

	 @JsonProperty("siope")
	 public String getSiope() {
		 return siope;
	 }
	 public void setSiope(String siope) {
		 this.siope = siope;
	 }

	 /**
	  * Importo della voce di contabilita'
	  **/
	 public Contabilita importo(BigDecimal importo) {
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
	  * Codice contabilita' utilizzato dal gestionale dell'ente
	  **/
	 public Contabilita codGestionaleEnte(String codGestionaleEnte) {
		 this.codGestionaleEnte = codGestionaleEnte;
		 return this;
	 }

	 @JsonProperty("codGestionaleEnte")
	 public String getCodGestionaleEnte() {
		 return codGestionaleEnte;
	 }
	 public void setCodGestionaleEnte(String codGestionaleEnte) {
		 this.codGestionaleEnte = codGestionaleEnte;
	 }

	 @Override
	 public boolean equals(java.lang.Object o) {
		 if (this == o) {
			 return true;
		 }
		 if (o == null || getClass() != o.getClass()) {
			 return false;
		 }
		 Contabilita contabilita = (Contabilita) o;
		 return Objects.equals(ufficio, contabilita.ufficio) &&
				 Objects.equals(capitolo, contabilita.capitolo) &&
				 Objects.equals(annoEsercizio, contabilita.annoEsercizio) &&
				 Objects.equals(accertamento, contabilita.accertamento) &&
				 Objects.equals(annoAccertamento, contabilita.annoAccertamento) &&
				 Objects.equals(subAccertamento, contabilita.subAccertamento) &&
				 Objects.equals(siope, contabilita.siope) &&
				 Objects.equals(importo, contabilita.importo) &&
				 Objects.equals(codGestionaleEnte, contabilita.codGestionaleEnte);
	 }

	 @Override
	 public int hashCode() {
		 return Objects.hash(ufficio, capitolo, annoEsercizio, accertamento, annoAccertamento, subAccertamento, siope, importo, codGestionaleEnte);
	 }

	 @Override
	 public String toString() {
		 StringBuilder sb = new StringBuilder();
		 sb.append("class Contabilita {\n");

		 sb.append("    ufficio: ").append(toIndentedString(ufficio)).append("\n");
		 sb.append("    capitolo: ").append(toIndentedString(capitolo)).append("\n");
		 sb.append("    annoEsercizio: ").append(toIndentedString(annoEsercizio)).append("\n");
		 sb.append("    accertamento: ").append(toIndentedString(accertamento)).append("\n");
		 sb.append("    annoAccertamento: ").append(toIndentedString(annoAccertamento)).append("\n");
		 sb.append("    subAccertamento: ").append(toIndentedString(subAccertamento)).append("\n");
		 sb.append("    siope: ").append(toIndentedString(siope)).append("\n");
		 sb.append("    importo: ").append(toIndentedString(importo)).append("\n");
		 sb.append("    codGestionaleEnte: ").append(toIndentedString(codGestionaleEnte)).append("\n");
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
