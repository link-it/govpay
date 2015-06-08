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

import java.util.ArrayList;
import java.util.List;

import org.openspcoop2.generic_project.web.bean.BaseBean;
import org.openspcoop2.generic_project.web.core.Utils;
import org.openspcoop2.generic_project.web.presentation.field.OutputField;
import org.openspcoop2.generic_project.web.presentation.field.OutputGroup;
import org.openspcoop2.generic_project.web.presentation.field.OutputText;

public class EnteBean extends BaseBean<EnteCreditoreModel, Long> { 

	private OutputField<String> idEnteCreditore; 
	private OutputField<String> identificativoUnivoco;
	private OutputField<String> idFiscale; 
	private OutputField<String> denominazione; 
	private OutputField<String> indirizzo;
	private OutputField<String> civico;
	private OutputField<String> localita;
	private OutputField<String> cap;
	private OutputField<String> provincia;
	private OutputField<String> stato;
	private OutputField<String> nodoPagamentoAbilitato;

	private OutputGroup fieldsDatiGenerali = null;

	private Long id = null;

	private List<TributoBean> listaTributi = null;

	private DominioEnteModel dominioEnte = null;

	public EnteBean(){

		this.idEnteCreditore = new OutputText();
		this.idEnteCreditore.setLabel(Utils.getMessageFromResourceBundle("ente.idEnteCreditore"));
		this.idEnteCreditore.setName("idEnteCreditore");

		this.identificativoUnivoco = new OutputText();
		this.identificativoUnivoco.setLabel(Utils.getMessageFromResourceBundle("ente.identificativoUnivoco"));
		this.identificativoUnivoco.setName("identificativoUnivoco");

		this.nodoPagamentoAbilitato = new OutputText();
		this.nodoPagamentoAbilitato.setLabel(Utils.getMessageFromResourceBundle("ente.nodoPagamentoAbilitato"));
		this.nodoPagamentoAbilitato.setName("nodoPagamentoAbilitato");

		this.idFiscale = new OutputText();
		this.idFiscale.setLabel(Utils.getMessageFromResourceBundle("ente.idFiscale"));
		this.idFiscale.setName("idFiscale");

		this.denominazione = new OutputText();
		this.denominazione.setLabel(Utils.getMessageFromResourceBundle("ente.denominazione"));
		this.denominazione.setName("denominazione");

		this.indirizzo = new OutputText();
		this.indirizzo.setLabel(Utils.getMessageFromResourceBundle("ente.indirizzo"));
		this.indirizzo.setName("indirizzo");

		this.civico = new OutputText();
		this.civico.setLabel(Utils.getMessageFromResourceBundle("ente.civico"));
		this.civico.setName("civico");

		this.localita = new OutputText();
		this.localita.setLabel(Utils.getMessageFromResourceBundle("ente.localita"));
		this.localita.setName("localita");

		this.cap = new OutputText();
		this.cap.setLabel(Utils.getMessageFromResourceBundle("ente.cap"));
		this.cap.setName("cap");

		this.provincia = new OutputText();
		this.provincia.setLabel(Utils.getMessageFromResourceBundle("ente.provincia"));
		this.provincia.setName("provincia");

		this.stato = new OutputText();
		this.stato.setLabel(Utils.getMessageFromResourceBundle("ente.stato"));
		this.stato.setName("stato");

		this.fieldsDatiGenerali = new OutputGroup();
		this.fieldsDatiGenerali.setIdGroup("datiGenerali");
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
		String stato = this.getDTO().getStato().equals(EnumStato.A) ? Utils.getMessageFromResourceBundle("commons.label.attivo") : 
			Utils.getMessageFromResourceBundle("commons.label.nonAttivo");
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

	public OutputField<String> getIdEnteCreditore() {
		return idEnteCreditore;
	}

	public void setIdEnteCreditore(OutputField<String> idEnteCreditore) {
		this.idEnteCreditore = idEnteCreditore;
	}

	public OutputField<String> getIdentificativoUnivoco() {
		return identificativoUnivoco;
	}

	public void setIdentificativoUnivoco(OutputField<String> identificativoUnivoco) {
		this.identificativoUnivoco = identificativoUnivoco;
	}

	public OutputField<String> getIdFiscale() {
		return idFiscale;
	}

	public void setIdFiscale(OutputField<String> idFiscale) {
		this.idFiscale = idFiscale;
	}

	public OutputField<String> getDenominazione() {
		return denominazione;
	}

	public void setDenominazione(OutputField<String> denominazione) {
		this.denominazione = denominazione;
	}

	public OutputField<String> getIndirizzo() {
		return indirizzo;
	}

	public void setIndirizzo(OutputField<String> indirizzo) {
		this.indirizzo = indirizzo;
	}

	public OutputField<String> getLocalita() {
		return localita;
	}

	public void setLocalita(OutputField<String> localita) {
		this.localita = localita;
	}

	public OutputField<String> getCap() {
		return cap;
	}

	public void setCap(OutputField<String> cap) {
		this.cap = cap;
	}

	public OutputField<String> getProvincia() {
		return provincia;
	}

	public void setProvincia(OutputField<String> provincia) {
		this.provincia = provincia;
	}

	public OutputField<String> getStato() {
		return stato;
	}

	public void setStato(OutputField<String> stato) {
		this.stato = stato;
	}

	public OutputGroup getFieldsDatiGenerali() {
		return fieldsDatiGenerali;
	}

	public void setFieldsDatiGenerali(OutputGroup fieldsDatiGenerali) {
		this.fieldsDatiGenerali = fieldsDatiGenerali;
	}

	public OutputField<String> getIdIntermediarioPA() {
		return nodoPagamentoAbilitato;
	}

	public void setIdIntermediarioPA(OutputField<String> idIntermediarioPA) {
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

		String value = Utils.getMessageFromResourceBundle("commons.label.disabilitato");
		if(this.dominioEnte != null){
			value = Utils.getMessageFromResourceBundle("commons.label.abilitato");			
		}
		this.nodoPagamentoAbilitato.setValue(value);
	}

}
