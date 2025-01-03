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
package it.govpay.core.utils.serialization;

/**
 * @author Bussu Giovanni (bussu@link.it)
 * @author Pintori Giuliano (pintori@link.it)
 * @author  $Author$
 * @version $Rev$, $Date$
 * 
 */
public class SerializationConfig extends org.openspcoop2.utils.serialization.SerializationConfig{

	private boolean failOnNumbersForEnums = false; // default
	
	public boolean isFailOnNumbersForEnums() {
		return failOnNumbersForEnums;
	}
	public void setFailOnNumbersForEnums(boolean failOnNumbersForEnums) {
		this.failOnNumbersForEnums = failOnNumbersForEnums;
	}
}
