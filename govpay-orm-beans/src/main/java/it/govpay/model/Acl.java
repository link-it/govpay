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

public class Acl {

	public enum Servizio {
		PAGAMENTI_ATTESA("A"),
		PAGAMENTI_ONLINE("O"),
		VERSAMENTI("V"),
		NOTIFICHE("N"),
		RENDICONTAZIONE("R"),
		INCASSI("I"),
		CRUSCOTTO("C");
		
		private String codifica;

		Servizio(String codifica) {
			this.codifica = codifica;
		}
		
		public String getCodifica() {
			return codifica;
		}
		
		public static Servizio toEnum(String codifica) throws ServiceException {
			for(Servizio p : Servizio.values()){
				if(p.getCodifica().equals(codifica))
					return p;
			}
			throw new ServiceException("Codifica inesistente per Servizio. Valore fornito [" + codifica + "] valori possibili " + ArrayUtils.toString(Servizio.values()));
		}
	}
	
	public enum Tipo {
		DOMINIO("D"), TRIBUTO("T");
		
		private String codifica;

		Tipo(String codifica) {
			this.codifica = codifica;
		}
		
		public String getCodifica() {
			return codifica;
		}
		
		public static Tipo toEnum(String codifica) throws ServiceException {
			for(Tipo p : Tipo.values()){
				if(p.getCodifica().equals(codifica))
					return p;
			}
			throw new ServiceException("Codifica inesistente per Tipo. Valore fornito [" + codifica + "] valori possibili " + ArrayUtils.toString(Tipo.values()));
		}
	}
	
	private Tipo tipo;
	private Servizio servizio;
	private String codDominio;
	private String codTributo;
	private Long idDominio;
	private Long idTributo;
	
	public Tipo getTipo() {
		return tipo;
	}
	public void setTipo(Tipo tipo) {
		this.tipo = tipo;
	}
	public Servizio getServizio() {
		return servizio;
	}
	public void setServizio(Servizio servizio) {
		this.servizio = servizio;
	}
	public String getCodDominio() {
		return codDominio;
	}
	public void setCodDominio(String codDominio) {
		this.codDominio = codDominio;
	}
	public String getCodTributo() {
		return codTributo;
	}
	public void setCodTributo(String codTributo) {
		this.codTributo = codTributo;
	}
	public Long getIdDominio() {
		return idDominio;
	}
	public void setIdDominio(Long idDominio) {
		this.idDominio = idDominio;
	}
	public Long getIdTributo() {
		return idTributo;
	}
	public void setIdTributo(Long idTributo) {
		this.idTributo = idTributo;
	}
}
