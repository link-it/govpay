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

import org.openspcoop2.generic_project.web.factory.FactoryException;
import org.openspcoop2.generic_project.web.form.SearchForm;
import org.openspcoop2.generic_project.web.impl.jsf1.form.BaseSearchForm;
import org.openspcoop2.generic_project.web.input.Text;


@Named("scadenzarioSearchForm") @SessionScoped
public class ScadenzarioSearchForm  extends BaseSearchForm implements SearchForm,Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private Text idIntermediario = null;
	private Text idStazioneIntermediario = null;
	private Text idEnte = null;
	private Text nome = null;
	
	public ScadenzarioSearchForm(){
		try {
			init();
		} catch (FactoryException e) {
		}
	}
	
	@Override
	public void init() throws FactoryException {
		// Properties del form
		this.setId("searchScadenzariForm");
		this.setNomeForm("Ricerca Scadenzari Nodo dei pagamenti");
		this.setClosable(false);
		
		this.nome = this.getWebGenericProjectFactory().getInputFieldFactory().createText();
		this.nome.setLabel(Utils.getInstance().getMessageFromResourceBundle("scadenzario.nome"));
		this.nome.setName("nome");
		this.nome.setValue(null);
		
		this.idIntermediario = this.getWebGenericProjectFactory().getInputFieldFactory().createText();
		this.idIntermediario.setLabel(Utils.getInstance().getMessageFromResourceBundle("scadenzario.idIntermediario"));
		this.idIntermediario.setName("idIntermediario");
		this.idIntermediario.setValue(null);
		
		this.idStazioneIntermediario = this.getWebGenericProjectFactory().getInputFieldFactory().createText();
		this.idStazioneIntermediario.setLabel(Utils.getInstance().getMessageFromResourceBundle("scadenzario.idStazioneIntermediario"));
		this.idStazioneIntermediario.setName("idStazioneIntermediario");
		this.idStazioneIntermediario.setValue(null);
		
		this.idEnte = this.getWebGenericProjectFactory().getInputFieldFactory().createText();
		this.idEnte.setLabel(Utils.getInstance().getMessageFromResourceBundle("scadenzario.idEnte"));
		this.idEnte.setName("idEnte");
		this.idEnte.setValue(null);
	 
		resetParametriPaginazione();
	}

	public Text getIdEnte() {
		return idEnte;
	}

	public void setIdEnte(Text idEnte) {
		this.idEnte = idEnte;
	}

	@Override
	public void reset() {
		resetParametriPaginazione();
	}

	public Text getIdIntermediario() {
		return idIntermediario;
	}

	public void setIdIntermediario(Text idIntermediario) {
		this.idIntermediario = idIntermediario;
	}

	public Text getIdStazioneIntermediario() {
		return idStazioneIntermediario;
	}

	public void setIdStazioneIntermediario(Text idStazioneIntermediario) {
		this.idStazioneIntermediario = idStazioneIntermediario;
	}

	public Text getNome() {
		return nome;
	}

	public void setNome(Text nome) {
		this.nome = nome;
	}
	
	
}
