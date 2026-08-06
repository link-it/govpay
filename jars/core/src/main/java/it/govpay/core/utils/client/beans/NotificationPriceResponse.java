/*
 * GovPay - Porta di Accesso al Nodo dei Pagamenti SPC
 * http://www.gov4j.it/govpay
 *
 * Copyright (c) 2014-2026 Link.it srl (http://www.link.it).
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
package it.govpay.core.utils.client.beans;

import com.fasterxml.jackson.annotation.JsonProperty;

import it.govpay.core.beans.JSONSerializable;

/**
 * Risposta del servizio SEND NotificationPriceV23 (GET /delivery/v2.3/price/{paTaxId}/{noticeCode}).
 * L'importo e' espresso in eurocent, coerentemente con la chiave di metadata NOTIFICATION_FEE
 * documentata su https://developer.pagopa.it/pago-pa/guides/metadata/spese-di-notifica-send
 */
public class NotificationPriceResponse extends JSONSerializable {

	@JsonProperty("amount")
	private Long amount;

	public Long getAmount() {
		return this.amount;
	}

	public void setAmount(Long amount) {
		this.amount = amount;
	}

	@Override
	public String getJsonIdFilter() {
		return "notificationPriceResponse";
	}
}
