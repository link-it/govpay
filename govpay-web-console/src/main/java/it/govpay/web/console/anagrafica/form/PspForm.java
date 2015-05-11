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

import it.govpay.web.console.anagrafica.bean.CanaleBean;

import java.io.Serializable;

import javax.inject.Named;

import org.openspcoop2.generic_project.web.core.Utils;
import org.openspcoop2.generic_project.web.form.BaseForm;
import org.openspcoop2.generic_project.web.form.field.BooleanField;

@Named("pspForm")
public class PspForm extends BaseForm implements Serializable{


	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private BooleanField stato;

	public PspForm(){
		init();
	}

	/**
	 * Inizializza la form con i valori dell'elemento selezionato.
	 * 
	 * @param bean
	 */
	public void setValues(CanaleBean bean){
		if(bean != null){
			if(bean.getDTO().isAbilitato()){
				this.stato.setDefaultValue(true);
			}else {
				this.stato.setDefaultValue(false);
			}
		} else {
			this.stato.setDefaultValue(false);
		}

		this.reset();
	}

	@Override
	protected void init() {
		this.setClosable(false);
		this.setIdForm("formPsp");
		this.setNomeForm(null); 

		this.stato = new BooleanField();
		this.stato.setRequired(false);
		this.stato.setLabel(Utils.getMessageFromResourceBundle("psp.attivo"));
		this.stato.setName("statoPsp");
		this.stato.setValue(null);
	}

	@Override
	public void reset() {
		this.stato.reset();
	}

	public String valida(){
		return null;
	}


	public BooleanField getStato() {
		return stato;
	}

	public void setStato(BooleanField stato) {
		this.stato = stato;
	}


}
