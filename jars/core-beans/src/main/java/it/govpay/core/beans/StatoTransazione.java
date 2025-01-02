/*
 * GovPay - Porta di Accesso al Nodo dei Pagamenti SPC
 * http://www.gov4j.it/govpay
 *
 * Copyright (c) 2014-2025 Link.it srl (http://www.link.it).
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

package it.govpay.core.beans;

public enum StatoTransazione {

    RPT_ATTIVATA,
    RPT_ERRORE_INVIO_A_NODO,
    RPT_RICEVUTA_NODO,
    RPT_RIFIUTATA_NODO,
    RPT_ACCETTATA_NODO,
    RPT_RIFIUTATA_PSP,
    RPT_ACCETTATA_PSP,
    RPT_ERRORE_INVIO_A_PSP,
    RPT_INVIATA_A_PSP,
    RPT_DECORSI_TERMINI,
    RT_RICEVUTA_NODO,
    RT_RIFIUTATA_NODO,
    RT_ACCETTATA_NODO,
    RT_ACCETTATA_PA,
    RT_RIFIUTATA_PA,
    RT_ESITO_SCONOSCIUTO_PA;

    public String value() {
        return this.name();
    }

    public static StatoTransazione fromValue(String v) {
        return valueOf(v);
    }

}
