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
package it.govpay.rs.v1.beans;

import java.util.ArrayList;
import java.util.List;

import org.openspcoop2.generic_project.exception.ServiceException;

import it.govpay.bd.BasicBD;
import net.sf.json.JSONObject;
import net.sf.json.JsonConfig;

public class TracciatoExt extends Tracciato {
	
	
	private static JsonConfig jsonConfig = new JsonConfig();
	
	private byte[] esito;
	private List<Operazione> operazioni;
	
	static {
		jsonConfig.setRootClass(TracciatoExt.class);
	}
	
	public TracciatoExt() {
		this.operazioni = new ArrayList<Operazione>();
	}
	
	public TracciatoExt(it.govpay.bd.model.Tracciato tracciato, BasicBD bd) throws ServiceException {
		super(tracciato);
		this.esito = tracciato.getRawDataRisposta();
		this.operazioni = new ArrayList<Operazione>();
		for(it.govpay.bd.model.Operazione o : tracciato.getOperazioni(bd)) {
			getOperazioni().add(new Operazione(o, bd));
		}
	}
	
	public static TracciatoExt parse(String json) {
		JSONObject jsonObject = JSONObject.fromObject( json );  
		return (TracciatoExt) JSONObject.toBean( jsonObject, jsonConfig );
	}

	public static JsonConfig getJsonConfig() {
		return jsonConfig;
	}

	public byte[] getEsito() {
		return esito;
	}

	public List<Operazione> getOperazioni() {
		return operazioni;
	}

	public void setEsito(byte[] esito) {
		this.esito = esito;
	}
	
	
}
