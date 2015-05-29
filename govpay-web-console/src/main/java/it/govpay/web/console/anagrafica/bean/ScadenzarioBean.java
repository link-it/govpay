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
import it.govpay.ejb.core.model.ScadenzarioModelId;
import it.govpay.web.console.anagrafica.model.ScadenzarioModel;
import it.govpay.web.console.utils.Utils;

import java.util.List;

import org.openspcoop2.generic_project.web.bean.BaseBean;
import org.openspcoop2.generic_project.web.presentation.field.OutputField;
import org.openspcoop2.generic_project.web.presentation.field.OutputGroup;
import org.openspcoop2.generic_project.web.presentation.field.OutputText;

public class ScadenzarioBean extends BaseBean<ScadenzarioModel, String>{

	private OutputGroup fieldsDatiGenerali = null;

	private OutputField<String> nome;
	private OutputField<String> idEnte;
	private OutputField<String> cfEnte;
	private OutputField<String> denominazioneEnte;
	private OutputField<String> idIntermediarioPA;
	private OutputField<String> codice;
	private OutputField<String> idStazioneIntermediarioPA;

	private ConnettoreBean connettoreNotifica = null;
	private ConnettoreBean connettoreVerifica = null;

	private Long id = null;
	
	private ScadenzarioModelId idSistemaEnte = null;

	public ScadenzarioBean(){
		init();
	}

	private void init(){
		
		this.idSistemaEnte = new ScadenzarioModelId();

		this.nome = new OutputText();
		this.nome.setLabel(Utils.getMessageFromResourceBundle("scadenzario.nome"));
		this.nome.setName("scad_nome");
		
		this.idEnte = new OutputText();
		this.idEnte.setLabel(Utils.getMessageFromResourceBundle("scadenzario.idEnte"));
		this.idEnte.setName("scad_idEnte");
		
		this.denominazioneEnte = new OutputText();
		this.denominazioneEnte.setLabel(Utils.getMessageFromResourceBundle("scadenzario.idEnte"));
		this.denominazioneEnte.setName("scad_denominazioneEnte");
		
		this.cfEnte = new OutputText();
		this.cfEnte.setLabel(Utils.getMessageFromResourceBundle("scadenzario.idEnte"));
		this.cfEnte.setName("scad_cfEnte");

		this.idIntermediarioPA = new OutputText();
		this.idIntermediarioPA.setLabel(Utils.getMessageFromResourceBundle("scadenzario.idIntermediarioPA"));
		this.idIntermediarioPA.setName("scad_idIntermediarioPA");
		
		this.idStazioneIntermediarioPA = new OutputText();
		this.idStazioneIntermediarioPA.setLabel(Utils.getMessageFromResourceBundle("scadenzario.idStazioneIntermediarioPA"));
		this.idStazioneIntermediarioPA.setName("scad_idStazioneIntermediarioPA");

		this.codice = new OutputText();
		this.codice.setLabel(Utils.getMessageFromResourceBundle("scadenzario.codice"));
		this.codice.setName("scad_codice");

		this.fieldsDatiGenerali = new OutputGroup();
		this.fieldsDatiGenerali.setIdGroup("datiGeneraliScadenzario");
		this.fieldsDatiGenerali.setColumns(4);
		this.fieldsDatiGenerali.setRendered(true);
		this.fieldsDatiGenerali.setStyleClass("beanTable"); 
		this.fieldsDatiGenerali.setColumnClasses("labelAllineataDx,valueAllineataSx,labelAllineataDx,valueAllineataSx");

		this.fieldsDatiGenerali.addField(this.nome);
		this.fieldsDatiGenerali.addField(this.idIntermediarioPA);
		
		this.fieldsDatiGenerali.addField(this.idStazioneIntermediarioPA);
//		this.fieldsDatiGenerali.addField(this.codice);

		this.connettoreNotifica = new ConnettoreBean("connNot");
		this.connettoreNotifica.setConnettorePdd(false);

		this.connettoreVerifica = new ConnettoreBean("connVer");
		this.connettoreVerifica.setConnettorePdd(false);

	}

	@Override
	public void setDTO(ScadenzarioModel dto) {
		super.setDTO(dto);
		
		this.idSistemaEnte.setIdEnte(this.getDTO().getIdEnte());
		this.idSistemaEnte.setIdSystem(this.getDTO().getIdSystem());
		
		this.nome.setValue(this.getDTO().getIdSystem());
		
		
		this.denominazioneEnte.setValue(this.getDTO().getDenominazioneEnte());
		// default label ente
		this.idEnte.setValue(this.getDTO().getIdEnte());
		this.cfEnte.setValue(this.getDTO().getIdEnte());
		
		// stazione
		String idStazione = this.getDTO().getIdStazione();
		
		if(idStazione != null){
			
			this.codice.setValue(idStazione.substring(idStazione.lastIndexOf("_")+ 1));
			this.idIntermediarioPA.setValue(idStazione.substring(0,idStazione.lastIndexOf("_")));
			this.idStazioneIntermediarioPA.setValue(idStazione);
		}
		
		
		// Aggiungere conversione dell'id ente in idfiscale (per ora controllo se e' presente nella lista enti).
		
		List<EnteCreditoreModel> list = Utils.getListaEntiCreditoreGestitiDallOperatore();
		
		if(list!= null && list.size() > 0)
		for (EnteCreditoreModel enteCreditoreModel : list) {
			if(enteCreditoreModel.getIdEnteCreditore().equals(this.getDTO().getIdEnte())){
				this.cfEnte.setValue(enteCreditoreModel.getIdFiscale());
				break;
			}
		}
		
		
		this.connettoreNotifica.setDTO(this.getDTO().getConnettoreNotifica());
		this.connettoreVerifica.setDTO(this.getDTO().getConnettoreVerifica());
	}

	public OutputField<String> getIdStazioneIntermediarioPA() {
		return idStazioneIntermediarioPA;
	}

	public void setIdStazioneIntermediarioPA(
			OutputField<String> idStazioneIntermediarioPA) {
		this.idStazioneIntermediarioPA = idStazioneIntermediarioPA;
	}

	public ScadenzarioModelId getIdSistemaEnte() {
		return idSistemaEnte;
	}

	public void setIdSistemaEnte(ScadenzarioModelId idSistemaEnte) {
		this.idSistemaEnte = idSistemaEnte;
	}

	public OutputGroup getFieldsDatiGenerali() {
		return fieldsDatiGenerali;
	}

	public void setFieldsDatiGenerali(OutputGroup fieldsDatiGenerali) {
		this.fieldsDatiGenerali = fieldsDatiGenerali;
	}

	public OutputField<String> getNome() {
		return nome;
	}

	public void setNome(OutputField<String> nome) {
		this.nome = nome;
	}

	public OutputField<String> getIdIntermediarioPA() {
		return idIntermediarioPA;
	}

	public void setIdIntermediarioPA(OutputField<String> idIntermediarioPA) {
		this.idIntermediarioPA = idIntermediarioPA;
	}

	public OutputField<String> getCodice() {
		return codice;
	}

	public void setCodice(OutputField<String> codice) {
		this.codice = codice;
	}

	public ConnettoreBean getConnettoreNotifica() {
		return connettoreNotifica;
	}

	public void setConnettoreNotifica(ConnettoreBean connettoreNotifica) {
		this.connettoreNotifica = connettoreNotifica;
	}

	public OutputField<String> getIdEnte() {
		return idEnte;
	}

	public void setIdEnte(OutputField<String> idEnte) {
		this.idEnte = idEnte;
	}

	public ConnettoreBean getConnettoreVerifica() {
		return connettoreVerifica;
	}

	public void setConnettoreVerifica(ConnettoreBean connettoreVerifica) {
		this.connettoreVerifica = connettoreVerifica;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public OutputField<String> getCfEnte() {
		return cfEnte;
	}

	public void setCfEnte(OutputField<String> cfEnte) {
		this.cfEnte = cfEnte;
	}

	public OutputField<String> getDenominazioneEnte() {
		return denominazioneEnte;
	}

	public void setDenominazioneEnte(OutputField<String> denominazioneEnte) {
		this.denominazioneEnte = denominazioneEnte;
	}
	
	
}
