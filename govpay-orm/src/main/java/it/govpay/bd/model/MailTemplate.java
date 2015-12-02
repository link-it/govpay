/*
 * GovPay - Porta di Accesso al Nodo dei Pagamenti SPC 
 * http://www.gov4j.it/govpay
 * 
 * Copyright (c) 2014-2015 Link.it srl (http://www.link.it).
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
package it.govpay.bd.model;

import java.util.List;

public class MailTemplate extends BasicModel {

	private static final long serialVersionUID = 1L;
	
	public enum TipoAllegati {RPT, RT_XML, RT_PDF}
	
	private Long id;
	private String mittente;
	private String templateOggetto;
	private String templateMessaggio;
	private List<TipoAllegati> allegati;

	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	
	@Override
	public boolean equals(Object obj) {
		MailTemplate mail = null;
		if(obj instanceof MailTemplate) {
			mail = (MailTemplate) obj;
		} else {
			return false;
		}
		
		boolean equal =
				equals(mittente, mail.getMittente()) &&
				equals(templateOggetto, mail.getTemplateOggetto()) &&
				equals(templateMessaggio, mail.getTemplateMessaggio()) &&
				equals(allegati, mail.getAllegati());
		
		return equal;
	}
	public List<TipoAllegati> getAllegati() {
		return allegati;
	}
	public void setAllegati(List<TipoAllegati> allegati) {
		this.allegati = allegati;
	}
	public String getMittente() {
		return mittente;
	}
	public void setMittente(String mittente) {
		this.mittente = mittente;
	}
	public String getTemplateOggetto() {
		return templateOggetto;
	}
	public void setTemplateOggetto(String templateOggetto) {
		this.templateOggetto = templateOggetto;
	}
	public String getTemplateMessaggio() {
		return templateMessaggio;
	}
	public void setTemplateMessaggio(String templateMessaggio) {
		this.templateMessaggio = templateMessaggio;
	}
	
	

}
