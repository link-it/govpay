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
package it.govpay.ejb.core.utils;

public class GovPayConstants {
	
	public static final String GOVPAY_NAME = "GovPAY";
	public static final String DIVISA = "EUR";
	public static final String OP_INSERIMENTO = "SYSTEM";
	public static final String CATEGORIA_TRIBUTO_DEFAULT = "Categoria000";
	
	// TODO: questi stati potrebbero andare in un enum ...
	public static final String ST_RIGA_NASCOSTO = "H";
	public static final String ST_RIGA_VISIBILE = "V";
	public static final String ST_RIGA_NON_VISIBILE = "N";

}
