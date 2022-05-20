package it.govpay.backoffice.v1.beans;

import java.util.Date;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonValue;

@com.fasterxml.jackson.annotation.JsonPropertyOrder({
"id",
"nome",
"stato",
"descrizioneStato",
"durataStato",
//"durataStatoValue",
"sogliaWarn",
"sogliaError",
//"sogliaWarnValue",
//"sogliaErrorValue",
"dataUltimoCheck",
"tipo",
})
public class Sonda extends it.govpay.core.beans.JSONSerializable {

	public enum TipoSonda {
		Coda, Batch, Sistema, Sconosciuto
	}

	public enum StatoSonda {
		OK("ok"), WARN("warn"), ERROR("error");

		private String value;

		StatoSonda(String value) {
			this.value = value;
		}

		@Override
		@JsonValue
		public String toString() {
			return String.valueOf(value);
		}

		@JsonCreator
		public static StatoSonda fromValue(String text) {
			for (StatoSonda b : StatoSonda.values()) {
				if (String.valueOf(b.value).equals(text)) {
					return b;
				}
			}
			return null;
		}
	}

	@JsonProperty("id")
	private String id;
	
	@JsonProperty("nome")
	private String nome;
	
	@JsonProperty("stato")
	private StatoSonda stato;
	
	@JsonProperty("descrizioneStato")
	private String descrizioneStato;
	
//	@JsonProperty("durataStatoValue")
//	private Long durataStatoValue;
	
	@JsonProperty("durataStato")
	private String durataStato;
	
	@JsonProperty("sogliaWarn")
	private String sogliaWarn;
	
	@JsonProperty("sogliaError")
	private String sogliaError;
	
//	@JsonProperty("sogliaWarnValue")
//	private Long sogliaWarnValue;
//	
//	@JsonProperty("sogliaErrorValue")
//	private Long sogliaErrorValue;
	
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern="yyyy-MM-dd'T'HH:mm:ss.SSSZ", locale = "it_IT", timezone = "Europe/Rome")
	@JsonProperty("dataUltimoCheck")
	private Date dataUltimoCheck;
	
	@JsonProperty("tipo")
	private TipoSonda tipo;

	public Sonda(TipoSonda tipo) {
		this.tipo = tipo;
	}

	public Sonda(Class<? extends org.openspcoop2.utils.sonde.Sonda> class1) {
		if(class1.equals(org.openspcoop2.utils.sonde.impl.SondaBatch.class)) {
			this.tipo = TipoSonda.Batch;
			return;
		}

		if(class1.equals(org.openspcoop2.utils.sonde.impl.SondaCoda.class)) {
			this.tipo = TipoSonda.Coda;
			return;
		}

		this.tipo = TipoSonda.Sistema;
	}

//	public Long getSogliaWarnValue() {
//		return this.sogliaWarnValue;
//	}

	public String getSogliaWarn() {
		return this.sogliaWarn;
	}

	public void setSogliaWarn(Long sogliaWarn) {
//		this.sogliaWarnValue = sogliaWarn;

		switch (this.tipo) {
		case Batch:
			this.sogliaWarn = "Lasso di tempo senza esecuzioni con successo: " + this.toString(sogliaWarn);
			break;
		case Coda:
			this.sogliaWarn = "Numero di elementi accodati: " + sogliaWarn;
			break;
		default: 
			this.sogliaWarn = (sogliaWarn != null) ? "" + sogliaWarn : null;
			break;
		}
	}

//	public Long getSogliaErrorValue() {
//		return this.sogliaErrorValue;
//	}

	public String getSogliaError() {
		return this.sogliaError;
	}

	public void setSogliaError(Long sogliaError) {
//		this.sogliaErrorValue = sogliaError;

		switch (this.tipo) {
		case Batch:
			this.sogliaError = "Lasso di tempo senza esecuzioni con successo: " + this.toString(sogliaError);
			break;
		case Coda:
			this.sogliaError = "Numero di elementi accodati: " + sogliaError;
			break;
		default:
			this.sogliaError = (sogliaError != null) ? "" + sogliaError : null;
			break;
		}
	}

	public Date getDataUltimoCheck() {
		return this.dataUltimoCheck;
	}
	public void setDataUltimoCheck(Date dataUltimoCheck) {
		this.dataUltimoCheck = dataUltimoCheck;
	}
	public String getNome() {
		return this.nome;
	}
	public void setNome(String nome) {
		this.nome = nome;
	}
	public StatoSonda getStato() {
		return this.stato;
	}
	public void setStato(StatoSonda stato) {
		this.stato = stato;
	}
	public String getDescrizioneStato() {
		return this.descrizioneStato;
	}
	public void setDescrizioneStato(String descrizioneStato) {
		this.descrizioneStato = descrizioneStato;
	}

	public String getDurataStato() {
		return this.durataStato;
	}

	public void setDurataStato(Date inizioStato) {
		if(inizioStato != null) {
			Long durataStatoValue = new Date().getTime() - inizioStato.getTime();
			this.durataStato = this.toString(durataStatoValue);
		} 
	}

	public TipoSonda getTipo() {
		return this.tipo;
	}

	private String toString(Long millis) {
		return millis != null ? org.openspcoop2.utils.Utilities.convertSystemTimeIntoString_millisecondi(millis, false) : "";
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	@Override
	public String getJsonIdFilter() {
		return "sonda";
	}

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append("class Soggetto {\n");

		sb.append("    tipo: ").append(this.toIndentedString(this.tipo)).append("\n");
		sb.append("    id: ").append(this.toIndentedString(this.id)).append("\n");
		sb.append("    stato: ").append(this.toIndentedString(this.stato)).append("\n");
		sb.append("    descrizioneStato: ").append(this.toIndentedString(this.descrizioneStato)).append("\n");
//		sb.append("    durataStatoValue: ").append(this.toIndentedString(this.durataStatoValue)).append("\n");
		sb.append("    durataStato: ").append(this.toIndentedString(this.durataStato)).append("\n");
		sb.append("    sogliaWarn: ").append(this.toIndentedString(this.sogliaWarn)).append("\n");
		sb.append("    sogliaError: ").append(this.toIndentedString(this.sogliaError)).append("\n");
//		sb.append("    sogliaWarnValue: ").append(this.toIndentedString(this.sogliaWarnValue)).append("\n");
//		sb.append("    sogliaErrorValue: ").append(this.toIndentedString(this.sogliaErrorValue)).append("\n");
		sb.append("    dataUltimoCheck: ").append(this.toIndentedString(this.dataUltimoCheck)).append("\n");
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
