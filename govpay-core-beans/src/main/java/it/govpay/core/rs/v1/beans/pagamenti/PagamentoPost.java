package it.govpay.core.rs.v1.beans.pagamenti;

import java.time.Duration;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Objects;

import org.openspcoop2.generic_project.exception.ValidationException;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import it.govpay.core.rs.v1.beans.JSONSerializable;
import it.govpay.core.utils.validator.IValidable;
import it.govpay.core.utils.validator.ValidatorFactory;
@com.fasterxml.jackson.annotation.JsonPropertyOrder({
	"pendenze",
	"urlRitorno",
	"contoAddebito",
	"dataEsecuzionePagamento",
	"credenzialiPagatore",
	"soggettoVersante",
	"autenticazioneSoggetto",
	"lingua",
})
@JsonIgnoreProperties(ignoreUnknown = true)
public class PagamentoPost extends JSONSerializable implements IValidable {

	@JsonProperty("pendenze")
	private List<PendenzaPost> pendenze = new ArrayList<>();

	@JsonProperty("urlRitorno")
	private String urlRitorno = null;

	@JsonProperty("contoAddebito")
	private ContoAddebito contoAddebito = null;

	@JsonProperty("dataEsecuzionePagamento")
	private Date dataEsecuzionePagamento = null;

	@JsonProperty("credenzialiPagatore")
	private String credenzialiPagatore = null;

	@JsonProperty("soggettoVersante")
	private Soggetto soggettoVersante = null;


	/**
	 * modalita' di autenticazione del soggetto versante
	 */
	public enum AutenticazioneSoggettoEnum {




		CNS("CNS"),


		USR("USR"),


		OTH("OTH"),


		N_A("N/A");




		private String value;

		AutenticazioneSoggettoEnum(String value) {
			this.value = value;
		}

		@Override
		@com.fasterxml.jackson.annotation.JsonValue
		public String toString() {
			return String.valueOf(value);
		}

		public static AutenticazioneSoggettoEnum fromValue(String text) {
			for (AutenticazioneSoggettoEnum b : AutenticazioneSoggettoEnum.values()) {
				if (String.valueOf(b.value).equals(text)) {
					return b;
				}
			}
			return null;
		}
	}



	@JsonProperty("autenticazioneSoggetto")
	private AutenticazioneSoggettoEnum autenticazioneSoggetto = AutenticazioneSoggettoEnum.N_A;


	/**
	 * Indica il codice della lingua da utilizzare per l’esposizione delle pagine web.
	 */
	public enum LinguaEnum {




		IT("IT"),


		EN("EN"),


		FR("FR"),


		DE("DE"),


		SL("SL");




		private String value;

		LinguaEnum(String value) {
			this.value = value;
		}

		@Override
		@com.fasterxml.jackson.annotation.JsonValue
		public String toString() {
			return String.valueOf(value);
		}

		public static LinguaEnum fromValue(String text) {
			for (LinguaEnum b : LinguaEnum.values()) {
				if (String.valueOf(b.value).equals(text)) {
					return b;
				}
			}
			return null;
		}
	}



	@JsonProperty("lingua")
	private LinguaEnum lingua = LinguaEnum.IT;

	/**
	 * pendenze o riferimenti alle pendenze oggetto del pagamento
	 **/
	public PagamentoPost pendenze(List<PendenzaPost> pendenze) {
		this.pendenze = pendenze;
		return this;
	}

	@JsonProperty("pendenze")
	public List<PendenzaPost> getPendenze() {
		return pendenze;
	}
	public void setPendenze(List<PendenzaPost> pendenze) {
		this.pendenze = pendenze;
	}

	/**
	 * url di ritorno al portale al termine della sessione di pagamento
	 **/
	public PagamentoPost urlRitorno(String urlRitorno) {
		this.urlRitorno = urlRitorno;
		return this;
	}

	@JsonProperty("urlRitorno")
	public String getUrlRitorno() {
		return urlRitorno;
	}
	public void setUrlRitorno(String urlRitorno) {
		this.urlRitorno = urlRitorno;
	}

	/**
	 **/
	public PagamentoPost contoAddebito(ContoAddebito contoAddebito) {
		this.contoAddebito = contoAddebito;
		return this;
	}

	@JsonProperty("contoAddebito")
	public ContoAddebito getContoAddebito() {
		return contoAddebito;
	}
	public void setContoAddebito(ContoAddebito contoAddebito) {
		this.contoAddebito = contoAddebito;
	}

	/**
	 * data in cui si richiede che venga effettuato il pagamento, se diversa dalla data corrente.
	 **/
	public PagamentoPost dataEsecuzionePagamento(Date dataEsecuzionePagamento) {
		this.dataEsecuzionePagamento = dataEsecuzionePagamento;
		return this;
	}

	@JsonProperty("dataEsecuzionePagamento")
	public Date getDataEsecuzionePagamento() {
		return dataEsecuzionePagamento;
	}
	public void setDataEsecuzionePagamento(Date dataEsecuzionePagamento) {
		this.dataEsecuzionePagamento = dataEsecuzionePagamento;
	}

	/**
	 * Eventuali credenziali richieste dal PSP necessarie per completare l'operazione (ad esempio un codice bilaterale utilizzabile una sola volta).
	 **/
	public PagamentoPost credenzialiPagatore(String credenzialiPagatore) {
		this.credenzialiPagatore = credenzialiPagatore;
		return this;
	}

	@JsonProperty("credenzialiPagatore")
	public String getCredenzialiPagatore() {
		return credenzialiPagatore;
	}
	public void setCredenzialiPagatore(String credenzialiPagatore) {
		this.credenzialiPagatore = credenzialiPagatore;
	}

	/**
	 **/
	public PagamentoPost soggettoVersante(Soggetto soggettoVersante) {
		this.soggettoVersante = soggettoVersante;
		return this;
	}

	@JsonProperty("soggettoVersante")
	public Soggetto getSoggettoVersante() {
		return soggettoVersante;
	}
	public void setSoggettoVersante(Soggetto soggettoVersante) {
		this.soggettoVersante = soggettoVersante;
	}

	/**
	 * modalita' di autenticazione del soggetto versante
	 **/
	public PagamentoPost autenticazioneSoggettoEnum(AutenticazioneSoggettoEnum autenticazioneSoggetto) {
		this.autenticazioneSoggetto = autenticazioneSoggetto;
		return this;
	}

	public void setAutenticazioneSoggettoEnum(AutenticazioneSoggettoEnum autenticazioneSoggetto) {
		this.autenticazioneSoggetto = autenticazioneSoggetto;
	}

	public AutenticazioneSoggettoEnum getAutenticazioneSoggettoEnum() {
		return autenticazioneSoggetto;
	}
	public void setAutenticazioneSoggetto(AutenticazioneSoggettoEnum autenticazioneSoggetto) {
		this.autenticazioneSoggetto = autenticazioneSoggetto;
	}

	/**
	 * modalita' di autenticazione del soggetto versante
	 **/
	public PagamentoPost autenticazioneSoggetto(String autenticazioneSoggetto) throws Exception{
		this.setAutenticazioneSoggetto(autenticazioneSoggetto);
		return this;
	}

	@JsonProperty("autenticazioneSoggetto")
	public String getAutenticazioneSoggetto() {
		if(autenticazioneSoggetto != null) {
			return autenticazioneSoggetto.value;
		} else {
			return null;
		}

	}
	public void setAutenticazioneSoggetto(String autenticazioneSoggetto) throws Exception{
		if(autenticazioneSoggetto != null) {
			this.autenticazioneSoggetto = AutenticazioneSoggettoEnum.fromValue(autenticazioneSoggetto);
			if(this.autenticazioneSoggetto == null)
				throw new Exception("valore ["+autenticazioneSoggetto+"] non ammesso per la property autenticazioneSoggetto");
		}
	}

	/**
	 * Indica il codice della lingua da utilizzare per l’esposizione delle pagine web.
	 **/
	public PagamentoPost lingua(LinguaEnum lingua) {
		this.lingua = lingua;
		return this;
	}

	@JsonProperty("lingua")
	public LinguaEnum getLingua() {
		return lingua;
	}
	public void setLingua(LinguaEnum lingua) {
		this.lingua = lingua;
	}

	@Override
	public boolean equals(java.lang.Object o) {
		if (this == o) {
			return true;
		}
		if (o == null || getClass() != o.getClass()) {
			return false;
		}
		PagamentoPost pagamentoPost = (PagamentoPost) o;
		return Objects.equals(pendenze, pagamentoPost.pendenze) &&
				Objects.equals(urlRitorno, pagamentoPost.urlRitorno) &&
				Objects.equals(contoAddebito, pagamentoPost.contoAddebito) &&
				Objects.equals(dataEsecuzionePagamento, pagamentoPost.dataEsecuzionePagamento) &&
				Objects.equals(credenzialiPagatore, pagamentoPost.credenzialiPagatore) &&
				Objects.equals(soggettoVersante, pagamentoPost.soggettoVersante) &&
				Objects.equals(autenticazioneSoggetto, pagamentoPost.autenticazioneSoggetto) &&
				Objects.equals(lingua, pagamentoPost.lingua);
	}

	@Override
	public int hashCode() {
		return Objects.hash(pendenze, urlRitorno, contoAddebito, dataEsecuzionePagamento, credenzialiPagatore, soggettoVersante, autenticazioneSoggetto, lingua);
	}

	public static PagamentoPost parse(String json) throws org.openspcoop2.generic_project.exception.ServiceException {
		return (PagamentoPost) parse(json, PagamentoPost.class);
	}

	@Override
	public String getJsonIdFilter() {
		return "pagamentoPost";
	}

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append("class PagamentoPost {\n");

		sb.append("    pendenze: ").append(toIndentedString(pendenze)).append("\n");
		sb.append("    urlRitorno: ").append(toIndentedString(urlRitorno)).append("\n");
		sb.append("    contoAddebito: ").append(toIndentedString(contoAddebito)).append("\n");
		sb.append("    dataEsecuzionePagamento: ").append(toIndentedString(dataEsecuzionePagamento)).append("\n");
		sb.append("    credenzialiPagatore: ").append(toIndentedString(credenzialiPagatore)).append("\n");
		sb.append("    soggettoVersante: ").append(toIndentedString(soggettoVersante)).append("\n");
		sb.append("    autenticazioneSoggetto: ").append(toIndentedString(autenticazioneSoggetto)).append("\n");
		sb.append("    lingua: ").append(toIndentedString(lingua)).append("\n");
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

	public void validate() throws ValidationException {
		ValidatorFactory vf = ValidatorFactory.newInstance();
		vf.getValidator("pendenze", pendenze).notNull().minItems(1).validateObjects();
		vf.getValidator("urlRitorno", urlRitorno).pattern("https?:\\/\\/(www\\.)?[-a-zA-Z0-9@:%._\\+~#=]{2,256}\\.[a-z]{2,6}\\b([-a-zA-Z0-9@:%_\\+.~#?&//=]*)");
		vf.getValidator("contoAddebito", contoAddebito).validateFields();
		vf.getValidator("dataEsecuzionePagamento", dataEsecuzionePagamento).after(LocalDate.now()).inside(Duration.ofDays(30));
		vf.getValidator("credenzialiPagatore", credenzialiPagatore).minLength(1).maxLength(35);
		vf.getValidator("soggettoVersante", soggettoVersante).validateFields();
		vf.getValidator("autenticazioneSoggetto", autenticazioneSoggetto).notNull();
	}
}



