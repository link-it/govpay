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

import it.govpay.ejb.ndp.model.EventiInterfacciaModel.Infospcoop;
import it.govpay.web.console.utils.Utils;

import org.openspcoop2.generic_project.web.bean.IBean;
import org.openspcoop2.generic_project.web.factory.FactoryException;
import org.openspcoop2.generic_project.web.impl.jsf1.bean.BaseBean;
import org.openspcoop2.generic_project.web.output.OutputGroup;
import org.openspcoop2.generic_project.web.output.Text;

public class InfospcoopBean extends BaseBean<Infospcoop, String> implements IBean<Infospcoop, String>{ 

	private Text idEgov;
//	private Text tipoSoggettoErogatore;
	private Text soggettoErogatore;
//	private Text tipoSoggettoFruitore;
	private Text soggettoFruitore;
//	private Text tipoServizio;
	private Text servizio;
	private Text azione;

	// Gruppo Informazioni Dati Genareli
	private OutputGroup fieldsDatiGenerali = null;

	public  InfospcoopBean(){
		try {
			initFields();
		} catch (FactoryException e) {
		}
	}

	private void initFields() throws FactoryException{
		this.idEgov = this.getWebGenericProjectFactory().getOutputFieldFactory().createText();
		this.idEgov.setLabel(Utils.getInstance().getMessageFromResourceBundle("infospcoop.idEgov"));
		this.idEgov.setName("infospcoop_idEgov");
		
//		this.tipoSoggettoErogatore = this.getWebGenericProjectFactory().getOutputFieldFactory().createText();
//		this.tipoSoggettoErogatore.setLabel(Utils.getInstance().getMessageFromResourceBundle("infospcoop.tipoSoggettoErogatore"));
//		this.tipoSoggettoErogatore.setName("infospcoop_tipoSoggettoErogatore");
		
		this.soggettoErogatore = this.getWebGenericProjectFactory().getOutputFieldFactory().createText();
		this.soggettoErogatore.setLabel(Utils.getInstance().getMessageFromResourceBundle("infospcoop.soggettoErogatore"));
		this.soggettoErogatore.setName("infospcoop_soggettoErogatore");
		
//		this.tipoSoggettoFruitore = this.getWebGenericProjectFactory().getOutputFieldFactory().createText();
//		this.tipoSoggettoFruitore.setLabel(Utils.getInstance().getMessageFromResourceBundle("infospcoop.tipoSoggettoFruitore"));
//		this.tipoSoggettoFruitore.setName("infospcoop_tipoSoggettoFruitore");
		
		this.soggettoFruitore = this.getWebGenericProjectFactory().getOutputFieldFactory().createText();
		this.soggettoFruitore.setLabel(Utils.getInstance().getMessageFromResourceBundle("infospcoop.soggettoFruitore"));
		this.soggettoFruitore.setName("infospcoop_soggettoFruitore");
		
//		this.tipoServizio = this.getWebGenericProjectFactory().getOutputFieldFactory().createText();
//		this.tipoServizio.setLabel(Utils.getInstance().getMessageFromResourceBundle("infospcoop.tipoServizio"));
//		this.tipoServizio.setName("infospcoop_tipoServizio");
		
		this.servizio = this.getWebGenericProjectFactory().getOutputFieldFactory().createText();
		this.servizio.setLabel(Utils.getInstance().getMessageFromResourceBundle("infospcoop.servizio"));
		this.servizio.setName("infospcoop_servizio");
		
		this.azione = this.getWebGenericProjectFactory().getOutputFieldFactory().createText();
		this.azione.setLabel(Utils.getInstance().getMessageFromResourceBundle("infospcoop.azione"));
		this.azione.setName("infospcoop_azione");
		
		
		this.fieldsDatiGenerali = this.getWebGenericProjectFactory().getOutputFieldFactory().createOutputGroup();
		this.fieldsDatiGenerali.setId ("infospcoop_datiGenerali");
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

	public Text getIdEgov() {
		return idEgov;
	}

	public void setIdEgov(Text idEgov) {
		this.idEgov = idEgov;
	}

	public Text getSoggettoErogatore() {
		return soggettoErogatore;
	}

	public void setSoggettoErogatore(Text soggettoErogatore) {
		this.soggettoErogatore = soggettoErogatore;
	}

	public Text getSoggettoFruitore() {
		return soggettoFruitore;
	}

	public void setSoggettoFruitore(Text soggettoFruitore) {
		this.soggettoFruitore = soggettoFruitore;
	}

	public Text getServizio() {
		return servizio;
	}

	public void setServizio(Text servizio) {
		this.servizio = servizio;
	}

	public Text getAzione() {
		return azione;
	}

	public void setAzione(Text azione) {
		this.azione = azione;
	}

	public OutputGroup getFieldsDatiGenerali() {
		return fieldsDatiGenerali;
	}

	public void setFieldsDatiGenerali(OutputGroup fieldsDatiGenerali) {
		this.fieldsDatiGenerali = fieldsDatiGenerali;
	}

	@Override
	public String getId() {
		return this.getDTO().getIdEgov();
	}

}
