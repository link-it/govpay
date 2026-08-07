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
 * Schema NotificationPriceResponseV23, rif. https://raw.githubusercontent.com/pagopa/pn-delivery/main/docs/openapi/api-external-b2b-pa-bundle.yaml
 * Tutti gli importi sono espressi in eurocent, coerentemente con la chiave di metadata NOTIFICATION_FEE
 * documentata su https://developer.pagopa.it/pago-pa/guides/metadata/spese-di-notifica-send
 */
public class NotificationPriceResponse extends JSONSerializable {

	@JsonProperty("iun")
	private String iun;

	// Costo parziale di notificazione (non include paFee e vat)
	@JsonProperty("partialPrice")
	private Long partialPrice;

	// Costo totale di notificazione, comprensivo di paFee e vat applicato ai costi cartacei:
	// e' l'importo da attualizzare sulla pendenza.
	@JsonProperty("totalPrice")
	private Long totalPrice;

	@JsonProperty("vat")
	private Object vat;

	@JsonProperty("paFee")
	private Object paFee;

	@JsonProperty("refinementDate")
	private String refinementDate;

	@JsonProperty("notificationViewDate")
	private String notificationViewDate;

	// Costo base di SEND per la notificazione
	@JsonProperty("sendFee")
	private Long sendFee;

	// Costo totale dei prodotti postali
	@JsonProperty("analogCost")
	private Long analogCost;

	public String getIun() {
		return this.iun;
	}

	public void setIun(String iun) {
		this.iun = iun;
	}

	public Long getPartialPrice() {
		return this.partialPrice;
	}

	public void setPartialPrice(Long partialPrice) {
		this.partialPrice = partialPrice;
	}

	public Long getTotalPrice() {
		return this.totalPrice;
	}

	public void setTotalPrice(Long totalPrice) {
		this.totalPrice = totalPrice;
	}

	public Object getVat() {
		return this.vat;
	}

	public void setVat(Object vat) {
		this.vat = vat;
	}

	public Object getPaFee() {
		return this.paFee;
	}

	public void setPaFee(Object paFee) {
		this.paFee = paFee;
	}

	public String getRefinementDate() {
		return this.refinementDate;
	}

	public void setRefinementDate(String refinementDate) {
		this.refinementDate = refinementDate;
	}

	public String getNotificationViewDate() {
		return this.notificationViewDate;
	}

	public void setNotificationViewDate(String notificationViewDate) {
		this.notificationViewDate = notificationViewDate;
	}

	public Long getSendFee() {
		return this.sendFee;
	}

	public void setSendFee(Long sendFee) {
		this.sendFee = sendFee;
	}

	public Long getAnalogCost() {
		return this.analogCost;
	}

	public void setAnalogCost(Long analogCost) {
		this.analogCost = analogCost;
	}

	@Override
	public String getJsonIdFilter() {
		return "notificationPriceResponse";
	}
}
