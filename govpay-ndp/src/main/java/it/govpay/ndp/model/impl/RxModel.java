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
package it.govpay.ndp.model.impl;

import it.govpay.ndp.model.DocumentoModel;

public abstract class RxModel extends DocumentoModel {
	
	public enum TipoDocumento {
		/** Richiesta Pagamento Telematico*/ RP, 
		/** Ricevuta Telematica */           RT,
		/** Richiesta Revoca */              RR,
		/** Esito Revoca */              	 ER;
		}

	protected RxModel(TipoDocumento tipoDocumento, String idDominio,  String iuv, String ccp, byte[] bytes) {
		super(DocumentoModel.TipoDocumento.valueOf(tipoDocumento.name()), idDominio, bytes);
		this.ccp = ccp;
		this.iuv = iuv;
	}
	
	private String iuv; 
	private String ccp;

	@SuppressWarnings("unchecked")
	public static <T extends RxModel> T buildRxDocument(Class<T> e, String idDominio,  String iuv, String ccp, byte[] bytes) {
		if(e.equals(RPTModel.class)){
			return (T) new RPTModel(idDominio, iuv, ccp, bytes);
		} 
		
		if(e.equals(RTModel.class)){
			return (T) new RTModel(idDominio, iuv, ccp, bytes);
		}
		
		if(e.equals(RRModel.class)){
			return (T) new RRModel(idDominio, iuv, ccp, bytes);
		}
		
		if(e.equals(ERModel.class)){
			return (T) new ERModel(idDominio, iuv, ccp, bytes);
		}
		
		throw new ClassCastException();
	}
	
	public String getIuv() {
		return iuv;
	}
	public void setIuv(String iuv) {
		this.iuv = iuv;
	}
	public String getCcp() {
		return ccp;
	}
	public void setCcp(String ccp) {
		this.ccp = ccp;
	}

}
