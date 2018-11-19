package it.govpay.core.rs.v2.beans.pagamenti;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Objects;

import org.openspcoop2.utils.json.ValidationException;

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
			return String.valueOf(this.value);
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
			return String.valueOf(this.value);
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
		return this.pendenze;
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
		return this.urlRitorno;
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
		return this.contoAddebito;
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
		return this.dataEsecuzionePagamento;
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
		return this.credenzialiPagatore;
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
		return this.soggettoVersante;
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
		return this.autenticazioneSoggetto;
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
		if(this.autenticazioneSoggetto != null) {
			return this.autenticazioneSoggetto.value;
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
		return this.lingua;
	}
	public void setLingua(LinguaEnum lingua) {
		this.lingua = lingua;
	}

	@Override
	public boolean equals(java.lang.Object o) {
		if (this == o) {
			return true;
		}
		if (o == null || this.getClass() != o.getClass()) {
			return false;
		}
		PagamentoPost pagamentoPost = (PagamentoPost) o;
		return Objects.equals(this.pendenze, pagamentoPost.pendenze) &&
				Objects.equals(this.urlRitorno, pagamentoPost.urlRitorno) &&
				Objects.equals(this.contoAddebito, pagamentoPost.contoAddebito) &&
				Objects.equals(this.dataEsecuzionePagamento, pagamentoPost.dataEsecuzionePagamento) &&
				Objects.equals(this.credenzialiPagatore, pagamentoPost.credenzialiPagatore) &&
				Objects.equals(this.soggettoVersante, pagamentoPost.soggettoVersante) &&
				Objects.equals(this.autenticazioneSoggetto, pagamentoPost.autenticazioneSoggetto) &&
				Objects.equals(this.lingua, pagamentoPost.lingua);
	}

	@Override
	public int hashCode() {
		return Objects.hash(this.pendenze, this.urlRitorno, this.contoAddebito, this.dataEsecuzionePagamento, this.credenzialiPagatore, this.soggettoVersante, this.autenticazioneSoggetto, this.lingua);
	}

	public static PagamentoPost parse(String json) throws org.openspcoop2.generic_project.exception.ServiceException, org.openspcoop2.utils.json.ValidationException {
		return parse(json, PagamentoPost.class);
	}

	@Override
	public String getJsonIdFilter() {
		return "pagamentoPost";
	}

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append("class PagamentoPost {\n");

		sb.append("    pendenze: ").append(this.toIndentedString(this.pendenze)).append("\n");
		sb.append("    urlRitorno: ").append(this.toIndentedString(this.urlRitorno)).append("\n");
		sb.append("    contoAddebito: ").append(this.toIndentedString(this.contoAddebito)).append("\n");
		sb.append("    dataEsecuzionePagamento: ").append(this.toIndentedString(this.dataEsecuzionePagamento)).append("\n");
		sb.append("    credenzialiPagatore: ").append(this.toIndentedString(this.credenzialiPagatore)).append("\n");
		sb.append("    soggettoVersante: ").append(this.toIndentedString(this.soggettoVersante)).append("\n");
		sb.append("    autenticazioneSoggetto: ").append(this.toIndentedString(this.autenticazioneSoggetto)).append("\n");
		sb.append("    lingua: ").append(this.toIndentedString(this.lingua)).append("\n");
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
		vf.getValidator("pendenze", this.pendenze).notNull().minItems(1).validateObjects();
		vf.getValidator("urlRitorno", this.urlRitorno).pattern("https?:\\/\\/(www\\.)?[-a-zA-Z0-9@:%._\\+~#=]{2,256}\\b([-a-zA-Z0-9@:%_\\+.~#?&//=]*)");
		vf.getValidator("contoAddebito", this.contoAddebito).validateFields();
		vf.getValidator("dataEsecuzionePagamento", this.dataEsecuzionePagamento).after(LocalDate.now()).insideDays(30);
		vf.getValidator("credenzialiPagatore", this.credenzialiPagatore).minLength(1).maxLength(35);
		vf.getValidator("soggettoVersante", this.soggettoVersante).validateFields();
		vf.getValidator("autenticazioneSoggetto", this.autenticazioneSoggetto).notNull();
	}
}



