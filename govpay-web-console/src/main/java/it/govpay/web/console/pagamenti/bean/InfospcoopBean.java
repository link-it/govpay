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
package it.govpay.web.console.pagamenti.bean;

import it.govpay.ndp.model.EventiInterfacciaModel.Infospcoop;
import it.govpay.web.console.utils.Utils;

import org.openspcoop2.generic_project.web.bean.BaseBean;
import org.openspcoop2.generic_project.web.presentation.field.OutputField;
import org.openspcoop2.generic_project.web.presentation.field.OutputGroup;
import org.openspcoop2.generic_project.web.presentation.field.OutputText;

public class InfospcoopBean extends BaseBean<Infospcoop, String>{

	private OutputField<String> idEgov;
//	private OutputField<String> tipoSoggettoErogatore;
	private OutputField<String> soggettoErogatore;
//	private OutputField<String> tipoSoggettoFruitore;
	private OutputField<String> soggettoFruitore;
//	private OutputField<String> tipoServizio;
	private OutputField<String> servizio;
	private OutputField<String> azione;

	// Gruppo Informazioni Dati Genareli
	private OutputGroup fieldsDatiGenerali = new OutputGroup();

	public  InfospcoopBean(){
		initFields();
	}

	private void initFields(){
		this.idEgov = new OutputText();
		this.idEgov.setLabel(Utils.getMessageFromResourceBundle("infospcoop.idEgov"));
		this.idEgov.setName("infospcoop_idEgov");
		
//		this.tipoSoggettoErogatore = new OutputText();
//		this.tipoSoggettoErogatore.setLabel(Utils.getMessageFromResourceBundle("infospcoop.tipoSoggettoErogatore"));
//		this.tipoSoggettoErogatore.setName("infospcoop_tipoSoggettoErogatore");
		
		this.soggettoErogatore = new OutputText();
		this.soggettoErogatore.setLabel(Utils.getMessageFromResourceBundle("infospcoop.soggettoErogatore"));
		this.soggettoErogatore.setName("infospcoop_soggettoErogatore");
		
//		this.tipoSoggettoFruitore = new OutputText();
//		this.tipoSoggettoFruitore.setLabel(Utils.getMessageFromResourceBundle("infospcoop.tipoSoggettoFruitore"));
//		this.tipoSoggettoFruitore.setName("infospcoop_tipoSoggettoFruitore");
		
		this.soggettoFruitore = new OutputText();
		this.soggettoFruitore.setLabel(Utils.getMessageFromResourceBundle("infospcoop.soggettoFruitore"));
		this.soggettoFruitore.setName("infospcoop_soggettoFruitore");
		
//		this.tipoServizio = new OutputText();
//		this.tipoServizio.setLabel(Utils.getMessageFromResourceBundle("infospcoop.tipoServizio"));
//		this.tipoServizio.setName("infospcoop_tipoServizio");
		
		this.servizio = new OutputText();
		this.servizio.setLabel(Utils.getMessageFromResourceBundle("infospcoop.servizio"));
		this.servizio.setName("infospcoop_servizio");
		
		this.azione = new OutputText();
		this.azione.setLabel(Utils.getMessageFromResourceBundle("infospcoop.azione"));
		this.azione.setName("infospcoop_azione");
		
		
		this.fieldsDatiGenerali = new OutputGroup();
		this.fieldsDatiGenerali.setIdGroup("infospcoop_datiGenerali");
		this.fieldsDatiGenerali.setColumns(2);
		this.fieldsDatiGenerali.setRendered(true);
		this.fieldsDatiGenerali.setStyleClass("beanTable"); 
		this.fieldsDatiGenerali.setColumnClasses("labelAllineataDx,valueAllineataSx");

		this.fieldsDatiGenerali.addField(this.idEgov);
//		this.fieldsDatiGenerali.addField(this.tipoSoggettoFruitore);
		this.fieldsDatiGenerali.addField(this.soggettoFruitore);
//		this.fieldsDatiGenerali.addField(this.tipoSoggettoErogatore);
		this.fieldsDatiGenerali.addField(this.soggettoErogatore);
//		this.fieldsDatiGenerali.addField(this.tipoServizio);
		this.fieldsDatiGenerali.addField(this.servizio);
		this.fieldsDatiGenerali.addField(this.azione);

	}
	
	@Override
	public void setDTO(Infospcoop dto) {
		super.setDTO(dto);
		
		this.idEgov.setValue(this.getDTO().getIdEgov());
//		this.tipoSoggettoFruitore.setValue(this.getDTO().getTipoSoggettoFruitore());
		this.soggettoFruitore.setValue(this.getDTO().getTipoSoggettoFruitore() + "/" + this.getDTO().getSoggettoFruitore());
//		this.tipoSoggettoErogatore.setValue(this.getDTO().getTipoSoggettoErogatore());
		this.soggettoErogatore.setValue(this.getDTO().getTipoSoggettoErogatore() +"/"+this.getDTO().getSoggettoFruitore());
//		this.tipoServizio.setValue(this.getDTO().getTipoServizio());
		this.servizio.setValue(this.getDTO().getTipoServizio() + "/" + this.getDTO().getServizio());
		this.azione.setValue(this.getDTO().getAzione());
	}

	public OutputField<String> getIdEgov() {
		return idEgov;
	}

	public void setIdEgov(OutputField<String> idEgov) {
		this.idEgov = idEgov;
	}

	public OutputField<String> getSoggettoErogatore() {
		return soggettoErogatore;
	}

	public void setSoggettoErogatore(OutputField<String> soggettoErogatore) {
		this.soggettoErogatore = soggettoErogatore;
	}

	public OutputField<String> getSoggettoFruitore() {
		return soggettoFruitore;
	}

	public void setSoggettoFruitore(OutputField<String> soggettoFruitore) {
		this.soggettoFruitore = soggettoFruitore;
	}

	public OutputField<String> getServizio() {
		return servizio;
	}

	public void setServizio(OutputField<String> servizio) {
		this.servizio = servizio;
	}

	public OutputField<String> getAzione() {
		return azione;
	}

	public void setAzione(OutputField<String> azione) {
		this.azione = azione;
	}

	public OutputGroup getFieldsDatiGenerali() {
		return fieldsDatiGenerali;
	}

	public void setFieldsDatiGenerali(OutputGroup fieldsDatiGenerali) {
		this.fieldsDatiGenerali = fieldsDatiGenerali;
	}

}
