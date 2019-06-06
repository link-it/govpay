package it.govpay.core.beans.tracciati;

import java.util.Objects;

import org.openspcoop2.utils.json.ValidationException;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonValue;
@com.fasterxml.jackson.annotation.JsonPropertyOrder({
	"tipo",
	"testo",
	"contenuto",
})
public class Allegato extends it.govpay.core.beans.JSONSerializable {


	/**
	 * Tipologia di allegato
	 */
	public enum TipoEnum {




		ESITO_PAGAMENTO("Esito pagamento"),


		MARCA_DA_BOLLO("Marca da bollo");




		private String value;

		TipoEnum(String value) {
			this.value = value;
		}

		@Override
		@JsonValue
		public String toString() {
			return String.valueOf(this.value);
		}

		public static TipoEnum fromCodifica(String codifica) {
			switch (codifica) {
			case "BD":
				return MARCA_DA_BOLLO;
			case "ES":
				return ESITO_PAGAMENTO;

			}
			return null;
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

	@JsonProperty("testo")
	private String testo = null;

	@JsonProperty("contenuto")
	private Object contenuto = null;

	/**
	 * Tipologia di allegato
	 **/
	public Allegato tipo(TipoEnum tipo) {
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
	 * allegato codificato in base64
	 **/
	public Allegato testo(String testo) {
		this.testo = testo;
		return this;
	}

	@JsonProperty("testo")
	public String getTesto() {
		return this.testo;
	}
	public void setTesto(String testo) {
		this.testo = testo;
	}

	public Allegato contenuto(Object contenuto) {
		this.contenuto = contenuto;
		return this;
	}

	@JsonProperty("contenuto")
	public Object getContenuto() {
		return this.contenuto;
	}
	public void setContenuto(Object contenuto) {
		this.contenuto = contenuto;
	}

	@Override
	public boolean equals(java.lang.Object o) {
		if (this == o) {
			return true;
		}
		if (o == null || this.getClass() != o.getClass()) {
			return false;
		}
		Allegato allegato = (Allegato) o;
		return Objects.equals(this.tipo, allegato.tipo) &&
				Objects.equals(this.testo, allegato.testo) &&
				Objects.equals(this.contenuto, allegato.contenuto);
	}

	@Override
	public int hashCode() {
		return Objects.hash(this.tipo, this.testo, this.contenuto);
	}

	public static Allegato parse(String json) throws org.openspcoop2.generic_project.exception.ServiceException, ValidationException {
		return parse(json, Allegato.class);
	}

	@Override
	public String getJsonIdFilter() {
		return "allegato";
	}

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append("class Allegato {\n");

		sb.append("    tipo: ").append(this.toIndentedString(this.tipo)).append("\n");
		sb.append("    testo: ").append(this.toIndentedString(this.testo)).append("\n");
		sb.append("    contenuto: ").append(this.toIndentedString(this.contenuto)).append("\n");
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



