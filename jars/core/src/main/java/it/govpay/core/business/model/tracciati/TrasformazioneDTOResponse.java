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
package it.govpay.core.business.model.tracciati;

import java.util.Date;
import java.util.Hashtable;
import java.util.Map;

import it.govpay.core.utils.trasformazioni.Costanti;
import it.govpay.model.Operazione.TipoOperazioneType;

public class TrasformazioneDTOResponse {
	private String output;
	private Map<String, Object> dynamicMap;

	public TrasformazioneDTOResponse(String output, Map<String, Object> dynamicMap) {
		this.dynamicMap = dynamicMap;
		this.output = output;
	}

	public String getOutput() {
		return output;
	}

	public Map<String, Object> getDynamicMap() {
		return dynamicMap;
	}

	@SuppressWarnings("unchecked")
	public Boolean getAvvisatura() {
		Object object = this.getDynamicMap().get(Costanti.MAP_CTX_OBJECT);
		if(object != null) {
			Hashtable<String, Object> ctx = (Hashtable<String, Object>) object;
			return (Boolean) ctx.get("avvisatura");
		}

		return null;
	}

	@SuppressWarnings("unchecked")
	public Date getDataAvvisatura() {
		Object object = this.getDynamicMap().get(Costanti.MAP_CTX_OBJECT);
		if(object != null) {
			Hashtable<String, Object> ctx = (Hashtable<String, Object>) object;
			return  (Date) ctx.get("dataAvvisatura");
		}

		return null;
	}
	
	@SuppressWarnings("unchecked")
	public TipoOperazioneType getTipoOperazione() {
		Object object = this.getDynamicMap().get(Costanti.MAP_CTX_OBJECT);
		if(object != null) {
			Hashtable<String, Object> ctx = (Hashtable<String, Object>) object;
			Object object2 = ctx.get("tipoOperazione");
			if(object2 != null)
				return TipoOperazioneType.valueOf((String) object2);
		}
		return null;
	}
}
