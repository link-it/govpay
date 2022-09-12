/*
 * GovPay - Porta di Accesso al Nodo dei Pagamenti SPC 
 * http://www.gov4j.it/govpay
 * 
 * Copyright (c) 2014-2017 Link.it srl (http://www.link.it).
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
package it.govpay.core.exceptions;

public abstract class BaseExceptionV1 extends BaseException {

	transient private static final long serialVersionUID = 1L;

	public enum CategoriaEnum {
		AUTORIZZAZIONE("AUTORIZZAZIONE"),
		UNMARSHALL("UNMARSHALL"),
		RICHIESTA("RICHIESTA"),
		OPERAZIONE("OPERAZIONE"),
		PAGOPA("PAGOPA"),
		EC("EC"),
		INTERNO("INTERNO");

		private String value;

		CategoriaEnum(String value) {
			this.value = value;
		}

		@Override
		public String toString() {
			return String.valueOf(this.value);
		}

		public static CategoriaEnum fromValue(String text) {
			for (CategoriaEnum b : CategoriaEnum.values()) {
				if (String.valueOf(b.value).equals(text)) {
					return b;
				}
			}
			return null;
		}
	}

	private CategoriaEnum categoria;

	public BaseExceptionV1(String message, String subCode, String description, CategoriaEnum categoria) {
		super(message, subCode, description);
		this.categoria = categoria;
	}

	public BaseExceptionV1(String message, String subCode, String description, CategoriaEnum categoria, Throwable t) {
		super(message, subCode, description, t);
		this.categoria = categoria;
	}

	public CategoriaEnum getCategoria() {
		return this.categoria;
	}
	public void setCategoria(CategoriaEnum categoria) {
		this.categoria = categoria;
	}


}
