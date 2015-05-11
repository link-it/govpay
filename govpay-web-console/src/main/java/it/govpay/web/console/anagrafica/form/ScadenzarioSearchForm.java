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
package it.govpay.web.console.anagrafica.form;

import it.govpay.web.console.utils.Utils;

import java.io.Serializable;

import javax.enterprise.context.SessionScoped;
import javax.inject.Named;

import org.openspcoop2.generic_project.web.form.BaseSearchForm;
import org.openspcoop2.generic_project.web.form.field.FormField;
import org.openspcoop2.generic_project.web.form.field.TextField;


@Named("scadenzarioSearchForm") @SessionScoped
public class ScadenzarioSearchForm  extends BaseSearchForm implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private FormField<String> idIntermediario = null;
	private FormField<String> idStazioneIntermediario = null;
	private FormField<String> idEnte = null;
	private FormField<String> nome = null;
	
	public ScadenzarioSearchForm(){
		init();
	}
	
	@Override
	protected void init() {
		// Properties del form
		this.setIdForm("searchScadenzariForm");
		this.setNomeForm("Ricerca Scadenzari Nodo dei pagamenti");
		this.setClosable(false);
		
		this.nome = new TextField();
		this.nome.setLabel(Utils.getMessageFromResourceBundle("scadenzario.nome"));
		this.nome.setName("nome");
		this.nome.setValue(null);
		
		this.idIntermediario = new TextField();
		this.idIntermediario.setLabel(Utils.getMessageFromResourceBundle("scadenzario.idIntermediario"));
		this.idIntermediario.setName("idIntermediario");
		this.idIntermediario.setValue(null);
		
		this.idStazioneIntermediario = new TextField();
		this.idStazioneIntermediario.setLabel(Utils.getMessageFromResourceBundle("scadenzario.idStazioneIntermediario"));
		this.idStazioneIntermediario.setName("idStazioneIntermediario");
		this.idStazioneIntermediario.setValue(null);
		
		this.idEnte = new TextField();
		this.idEnte.setLabel(Utils.getMessageFromResourceBundle("scadenzario.idEnte"));
		this.idEnte.setName("idEnte");
		this.idEnte.setValue(null);
	 
		resetParametriPaginazione();
	}

	public FormField<String> getIdEnte() {
		return idEnte;
	}

	public void setIdEnte(FormField<String> idEnte) {
		this.idEnte = idEnte;
	}

	@Override
	public void reset() {
		resetParametriPaginazione();
	}

	public FormField<String> getIdIntermediario() {
		return idIntermediario;
	}

	public void setIdIntermediario(FormField<String> idIntermediario) {
		this.idIntermediario = idIntermediario;
	}

	public FormField<String> getIdStazioneIntermediario() {
		return idStazioneIntermediario;
	}

	public void setIdStazioneIntermediario(FormField<String> idStazioneIntermediario) {
		this.idStazioneIntermediario = idStazioneIntermediario;
	}

	public FormField<String> getNome() {
		return nome;
	}

	public void setNome(FormField<String> nome) {
		this.nome = nome;
	}
	
	
}
