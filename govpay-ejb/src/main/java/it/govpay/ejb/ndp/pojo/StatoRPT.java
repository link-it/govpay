/*
 * GovPay - Porta di Accesso al Nodo dei Pagamenti SPC 
 * http://www.gov4j.it/govpay
 * 
 * Copyright (c) 2014-2015 Link.it srl (http://www.link.it).
 * Copyright (c) 2014-2015 TAS S.p.A. (http://www.tasgroup.it).
 * 
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
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
package it.govpay.ejb.ndp.pojo;

public enum StatoRPT {
	
	PPT_RPT_SCONOSCIUTA, // RPT ricevuta dal Nodo
	RPT_RICEVUTA_NODO, // RPT ricevuta dal Nodo
	RPT_RIFIUTATA_NODO, // RPT rifiutata dal Nodo per sintassi o semantica errata (FINALE KO - IUV RIUSABILE)
	RPT_ACCETTATA_NODO, // RPT accettata dal Nodo come valida 
	RPT_RIFIUTATA_PSP, // RPT rifiutata dall'Intermediario PSP per sintassi o semantica errata (FINALE KO)
	RPT_ERRORE_INVIO_A_PSP, // RPT inviata all'Intermediario PSP - indisponibilita' del	ricevente (FINALE KO - IUV RIUSABILE)
	RPT_INVIATA_A_PSP, // RPT inviata all'Intermediario PSP - azione in attesa di risposta
	RPT_ACCETTATA_PSP, // RPT ricevuta ed accettata dall'Intermediario PSP come valida
	RPT_DECORSI_TERMINI, // RPT ha superato il periodo di decorrenza termini nel Nodo
	RT_RICEVUTA_NODO, // RT ricevuta dal Nodo
	RT_RIFIUTATA_NODO, // RT rifiutata dal Nodo per sintassi o semantica errata
	RT_ACCETTATA_NODO, // RT accettata dal Nodo come valida ed in corso di invio all'Intermediario PA
	RT_ACCETTATA_PA, // RT ricevuta dall'Intermediario PA ed accettata (FINALE OK)
	RT_RIFIUTATA_PA, // RT ricevuta dall'Intermediario PA e rifiutata
	RT_ESITO_SCONOSCIUTO_PA; // Esito dell'accettazione RT dell'Intermediario PA non interpretabile

}