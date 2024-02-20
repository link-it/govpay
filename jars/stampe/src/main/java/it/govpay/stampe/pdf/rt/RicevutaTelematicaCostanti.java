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
package it.govpay.stampe.pdf.rt;

public class RicevutaTelematicaCostanti {

	// root element elemento di input
	public static final String RICEVUTA_TELEMATICA_ROOT_ELEMENT_NAME= "root";

	public static final String RICEVUTA_TELEMATICA_TEMPLATE_JASPER = "ricevutaTelematica.risorse.template";

	// nomi properties loghi
	public static final String LOGO_ENTE = "ricevutaTelematica.logo.ente";
	public static final String LOGO_PAGOPA = "ricevutaTelematica.logo.pagopa";
	
	
	
	public static final String PAGAMENTO_ESEGUITO = "Eseguito";
	public static final String PAGAMENTO_NON_ESEGUITO = "Non eseguito";
	public static final String PAGAMENTO_PARZIALMENTE_ESEGUITO = "Parzialmente eseguito";
	public static final String DECORRENZA_TERMINI = "Decorrenza termini";
	public static final String DECORRENZA_TERMINI_PARZIALE = "Decorrenza termini parziale";
}
