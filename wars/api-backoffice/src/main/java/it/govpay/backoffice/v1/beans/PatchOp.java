/*
 * GovPay - Porta di Accesso al Nodo dei Pagamenti SPC
 * http://www.gov4j.it/govpay
 *
 * Copyright (c) 2014-2024 Link.it srl (http://www.link.it).
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License version 3, as published by
 * the Free Software Foundation.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 *
 */
package it.govpay.backoffice.v1.beans;

import java.util.Objects;

import com.fasterxml.jackson.annotation.JsonProperty;

import it.govpay.core.beans.JSONSerializable;
import it.govpay.core.exceptions.IOException;
import it.govpay.core.exceptions.ValidationException;
import it.govpay.core.utils.validator.IValidable;
import it.govpay.core.utils.validator.ValidatorFactory;
@com.fasterxml.jackson.annotation.JsonPropertyOrder({
	"op",
	"path",
	"value",
})
public class PatchOp extends JSONSerializable implements IValidable {


	/**
	 * Operazione da eseguire
	 */
	public enum OpEnum {




		ADD("ADD"),


		DELETE("DELETE"),


		REPLACE("REPLACE");




		private String value;

		OpEnum(String value) {
			this.value = value;
		}

		@Override
		@com.fasterxml.jackson.annotation.JsonValue
		public String toString() {
			return String.valueOf(this.value);
		}

		public static OpEnum fromValue(String text) {
			for (OpEnum b : OpEnum.values()) {
				if (String.valueOf(b.value).equals(text)) {
					return b;
				}
			}
			return null;
		}
	}



	@JsonProperty("op")
	private OpEnum op = null;

	@JsonProperty("path")
	private String path = null;

	@JsonProperty("value")
	private Object value = null;

	/**
	 * Operazione da eseguire
	 **/
	public PatchOp op(OpEnum op) {
		this.op = op;
		return this;
	}

	@JsonProperty("op")
	public OpEnum getOp() {
		return this.op;
	}
	public void setOp(OpEnum op) {
		this.op = op;
	}

	/**
	 * path dell'oggetto dell'operazione
	 **/
	public PatchOp path(String path) {
		this.path = path;
		return this;
	}

	@JsonProperty("path")
	public String getPath() {
		return this.path;
	}
	public void setPath(String path) {
		this.path = path;
	}

	/**
	 * valore del'oggetto dell'operazione
	 **/
	public PatchOp value(Object value) {
		this.value = value;
		return this;
	}

	@JsonProperty("value")
	public Object getValue() {
		return this.value;
	}
	public void setValue(Object value) {
		this.value = value;
	}

	@Override
	public boolean equals(java.lang.Object o) {
		if (this == o) {
			return true;
		}
		if (o == null || this.getClass() != o.getClass()) {
			return false;
		}
		PatchOp patchOp = (PatchOp) o;
		return Objects.equals(this.op, patchOp.op) &&
				Objects.equals(this.path, patchOp.path) &&
				Objects.equals(this.value, patchOp.value);
	}

	@Override
	public int hashCode() {
		return Objects.hash(this.op, this.path, this.value);
	}

	public static PatchOp parse(String json) throws IOException {
		return parse(json, PatchOp.class);
	}

	@Override
	public String getJsonIdFilter() {
		return "patchOp";
	}

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append("class PatchOp {\n");

		sb.append("    op: ").append(this.toIndentedString(this.op)).append("\n");
		sb.append("    path: ").append(this.toIndentedString(this.path)).append("\n");
		sb.append("    value: ").append(this.toIndentedString(this.value)).append("\n");
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
		vf.getValidator("op", this.op).notNull();
		vf.getValidator("path", this.path).notNull();
	}
}



