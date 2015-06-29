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
package it.govpay.web.console.anagrafica.bean;

import it.govpay.ejb.core.model.EnteCreditoreModel;
import it.govpay.ejb.core.model.EnteCreditoreModel.EnumStato;
import it.govpay.ejb.core.model.TributoModel;
import it.govpay.ejb.ndp.model.DominioEnteModel;
import it.govpay.web.console.utils.Utils;

import java.util.ArrayList;
import java.util.List;

import org.openspcoop2.generic_project.web.bean.IBean;
import org.openspcoop2.generic_project.web.factory.FactoryException;
import org.openspcoop2.generic_project.web.impl.jsf1.bean.BaseBean;
import org.openspcoop2.generic_project.web.output.OutputGroup;
import org.openspcoop2.generic_project.web.output.Text;


public class EnteBean extends BaseBean<EnteCreditoreModel, Long> implements IBean<EnteCreditoreModel, Long> {  

	private Text idEnteCreditore; 
	private Text identificativoUnivoco;
	private Text idFiscale; 
	private Text denominazione; 
	private Text indirizzo;
	private Text civico;
	private Text localita;
	private Text cap;
	private Text provincia;
	private Text stato;
	private Text nodoPagamentoAbilitato;

	private OutputGroup fieldsDatiGenerali = null;

	private Long id = null;

	private List<TributoBean> listaTributi = null;

	private DominioEnteModel dominioEnte = null;

	public EnteBean(){

		try {
			this.idEnteCreditore = this.getWebGenericProjectFactory().getOutputFieldFactory().createText();

			this.idEnteCreditore.setLabel(Utils.getInstance().getMessageFromResourceBundle("ente.idEnteCreditore"));
			this.idEnteCreditore.setName("idEnteCreditore");

			this.identificativoUnivoco = this.getWebGenericProjectFactory().getOutputFieldFactory().createText();
			this.identificativoUnivoco.setLabel(Utils.getInstance().getMessageFromResourceBundle("ente.identificativoUnivoco"));
			this.identificativoUnivoco.setName("identificativoUnivoco");

			this.nodoPagamentoAbilitato = this.getWebGenericProjectFactory().getOutputFieldFactory().createText();
			this.nodoPagamentoAbilitato.setLabel(Utils.getInstance().getMessageFromResourceBundle("ente.nodoPagamentoAbilitato"));
			this.nodoPagamentoAbilitato.setName("nodoPagamentoAbilitato");

			this.idFiscale = this.getWebGenericProjectFactory().getOutputFieldFactory().createText();
			this.idFiscale.setLabel(Utils.getInstance().getMessageFromResourceBundle("ente.idFiscale"));
			this.idFiscale.setName("idFiscale");

			this.denominazione = this.getWebGenericProjectFactory().getOutputFieldFactory().createText();
			this.denominazione.setLabel(Utils.getInstance().getMessageFromResourceBundle("ente.denominazione"));
			this.denominazione.setName("denominazione");

			this.indirizzo = this.getWebGenericProjectFactory().getOutputFieldFactory().createText();
			this.indirizzo.setLabel(Utils.getInstance().getMessageFromResourceBundle("ente.indirizzo"));
			this.indirizzo.setName("indirizzo");

			this.civico = this.getWebGenericProjectFactory().getOutputFieldFactory().createText();
			this.civico.setLabel(Utils.getInstance().getMessageFromResourceBundle("ente.civico"));
			this.civico.setName("civico");

			this.localita = this.getWebGenericProjectFactory().getOutputFieldFactory().createText();
			this.localita.setLabel(Utils.getInstance().getMessageFromResourceBundle("ente.localita"));
			this.localita.setName("localita");

			this.cap = this.getWebGenericProjectFactory().getOutputFieldFactory().createText();
			this.cap.setLabel(Utils.getInstance().getMessageFromResourceBundle("ente.cap"));
			this.cap.setName("cap");

			this.provincia = this.getWebGenericProjectFactory().getOutputFieldFactory().createText();
			this.provincia.setLabel(Utils.getInstance().getMessageFromResourceBundle("ente.provincia"));
			this.provincia.setName("provincia");

			this.stato = this.getWebGenericProjectFactory().getOutputFieldFactory().createText();
			this.stato.setLabel(Utils.getInstance().getMessageFromResourceBundle("ente.stato"));
			this.stato.setName("stato");

			this.fieldsDatiGenerali = this.getWebGenericProjectFactory().getOutputFieldFactory().createOutputGroup();
			this.fieldsDatiGenerali.setId("datiGenerali");
			this.fieldsDatiGenerali.setColumns(4);
			this.fieldsDatiGenerali.setRendered(true);
			this.fieldsDatiGenerali.setStyleClass("beanTable"); 
			this.fieldsDatiGenerali.setColumnClasses("labelAllineataDx,valueAllineataSx,labelAllineataDx,valueAllineataSx");

			this.fieldsDatiGenerali.addField(this.stato);
			this.fieldsDatiGenerali.addField(this.denominazione);

			this.fieldsDatiGenerali.addField(this.identificativoUnivoco);
			this.fieldsDatiGenerali.addField(this.indirizzo);

			this.fieldsDatiGenerali.addField(this.idFiscale);
			this.fieldsDatiGenerali.addField(this.localita);

			this.fieldsDatiGenerali.addField(this.nodoPagamentoAbilitato);
			this.fieldsDatiGenerali.addField(this.provincia);

			this.listaTributi = new ArrayList<TributoBean>();
		} catch (FactoryException e) {
		}
	}

	@Override
	public void setDTO(EnteCreditoreModel dto) {
		super.setDTO(dto);

		this.idEnteCreditore.setValue(this.getDTO().getIdEnteCreditore());
		this.identificativoUnivoco.setValue(this.getDTO().getIdentificativoUnivoco());
		this.idFiscale.setValue(this.getDTO().getIdFiscale());
		this.denominazione.setValue(this.getDTO().getDenominazione());
		this.indirizzo.setValue(this.getDTO().getIndirizzo() + ((this.getDTO().getCivico() != null && !this.getDTO().getCivico().isEmpty()) ? ", " + this.getDTO().getCivico() : ""));
		this.localita.setValue(this.getDTO().getLocalita() + ((this.getDTO().getCap() != null && !this.getDTO().getCap().isEmpty()) ? " (" + this.getDTO().getCap() + ")" : ""));
		this.provincia.setValue(this.getDTO().getProvincia());
		String stato = this.getDTO().getStato().equals(EnumStato.A) ? Utils.getInstance().getMessageFromResourceBundle("commons.label.attivo") : 
			Utils.getInstance().getMessageFromResourceBundle("commons.label.nonAttivo");
		this.stato.setValue(stato);

		List<TributoModel> tributiGestiti = this.getDTO().getTributiGestiti();

		if(tributiGestiti != null && tributiGestiti.size() > 0){
			for (TributoModel tributoModel : tributiGestiti) {
				TributoBean bean = new TributoBean();
				bean.setDTO(tributoModel);

				if(tributoModel.getIdEnteCreditore() == null){
					bean.getIdEnteCreditore().setValue(this.idEnteCreditore.getValue());
				}

				this.listaTributi.add(bean);
			}
		}
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Text getIdEnteCreditore() {
		return idEnteCreditore;
	}

	public void setIdEnteCreditore(Text idEnteCreditore) {
		this.idEnteCreditore = idEnteCreditore;
	}

	public Text getIdentificativoUnivoco() {
		return identificativoUnivoco;
	}

	public void setIdentificativoUnivoco(Text identificativoUnivoco) {
		this.identificativoUnivoco = identificativoUnivoco;
	}

	public Text getIdFiscale() {
		return idFiscale;
	}

	public void setIdFiscale(Text idFiscale) {
		this.idFiscale = idFiscale;
	}

	public Text getDenominazione() {
		return denominazione;
	}

	public void setDenominazione(Text denominazione) {
		this.denominazione = denominazione;
	}

	public Text getIndirizzo() {
		return indirizzo;
	}

	public void setIndirizzo(Text indirizzo) {
		this.indirizzo = indirizzo;
	}

	public Text getLocalita() {
		return localita;
	}

	public void setLocalita(Text localita) {
		this.localita = localita;
	}

	public Text getCap() {
		return cap;
	}

	public void setCap(Text cap) {
		this.cap = cap;
	}

	public Text getProvincia() {
		return provincia;
	}

	public void setProvincia(Text provincia) {
		this.provincia = provincia;
	}

	public Text getStato() {
		return stato;
	}

	public void setStato(Text stato) {
		this.stato = stato;
	}

	public OutputGroup getFieldsDatiGenerali() {
		return fieldsDatiGenerali;
	}

	public void setFieldsDatiGenerali(OutputGroup fieldsDatiGenerali) {
		this.fieldsDatiGenerali = fieldsDatiGenerali;
	}

	public Text getIdIntermediarioPA() {
		return nodoPagamentoAbilitato;
	}

	public void setIdIntermediarioPA(Text idIntermediarioPA) {
		this.nodoPagamentoAbilitato = idIntermediarioPA;
	}

	public List<TributoBean> getListaTributi() {
		return listaTributi;
	}

	public void setListaTributi(List<TributoBean> listaTributi) {
		this.listaTributi = listaTributi;
	}

	public DominioEnteModel getDominioEnte() {
		return dominioEnte;
	}

	public void setDominioEnte(DominioEnteModel dominioEnte) {
		this.dominioEnte = dominioEnte;

		String value = Utils.getInstance().getMessageFromResourceBundle("commons.label.disabilitato");
		if(this.dominioEnte != null){
			value = Utils.getInstance().getMessageFromResourceBundle("commons.label.abilitato");			
		}
		this.nodoPagamentoAbilitato.setValue(value);
	}

}
