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
package it.govpay.model;

import org.apache.commons.lang.ArrayUtils;
import org.openspcoop2.generic_project.exception.ServiceException;

public class Canale extends BasicModel {
	
	public enum ModelloPagamento {
		IMMEDIATO(0), 
		IMMEDIATO_MULTIBENEFICIARIO(1), 
		DIFFERITO(2), 
		ATTIVATO_PRESSO_PSP(4);
		
		private int codifica;

		ModelloPagamento(int codifica) {
			this.codifica = codifica;
		}
		
		public int getCodifica() {
			return this.codifica;
		}
		
		public static ModelloPagamento toEnum(int codifica) throws ServiceException {
			for(ModelloPagamento p : ModelloPagamento.values()){
				if(p.getCodifica() == codifica)
					return p;
			}
			throw new ServiceException("Codifica inesistente per ModelloPagamento. Valore fornito [" + codifica + "] valori possibili " + ArrayUtils.toString(ModelloPagamento.values()));
		}
	}
	
	public enum TipoVersamento {
		BONIFICO_BANCARIO_TESORERIA("BBT"), 
		BOLLETTINO_POSTALE("BP"), 
		ADDEBITO_DIRETTO("AD"), 
		CARTA_PAGAMENTO("CP"), 
		MYBANK("OBEP"), 
		ATTIVATO_PRESSO_PSP("PO"),
		OTHER("OTH");
		
		private String codifica;

		TipoVersamento(String codifica) {
			this.codifica = codifica;
		}
		public String getCodifica() {
			return this.codifica;
		}
		
		public static TipoVersamento toEnum(String codifica) throws ServiceException {
			for(TipoVersamento p : TipoVersamento.values()){
				if(p.getCodifica().equals(codifica))
					return p;
			}
			throw new ServiceException("Codifica inesistente per TipoVersamento. Valore fornito [" + codifica + "] valori possibili " + ArrayUtils.toString(TipoVersamento.values()));
		}
	}
	
	public Canale(String codCanale, TipoVersamento tipoVersamento) {
		this.codCanale = codCanale;
		this.tipoVersamento = tipoVersamento;
	}
	
	private static final long serialVersionUID = 1L;
	private String codCanale;
	private TipoVersamento tipoVersamento;
	
	public TipoVersamento getTipoVersamento() {
		return this.tipoVersamento;
	}
	public void setTipoVersamento(TipoVersamento tipoVersamento) {
		this.tipoVersamento = tipoVersamento;
	}
	public String getCodCanale() {
		return this.codCanale;
	}
	public void setCodCanale(String codCanale) {
		this.codCanale = codCanale;
	}
}
