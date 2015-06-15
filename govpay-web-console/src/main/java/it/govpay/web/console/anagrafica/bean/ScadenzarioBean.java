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

import org.openspcoop2.generic_project.web.bean.IBean;
import org.openspcoop2.generic_project.web.factory.FactoryException;
import org.openspcoop2.generic_project.web.impl.jsf1.bean.BaseBean;
import org.openspcoop2.generic_project.web.output.OutputGroup;
import org.openspcoop2.generic_project.web.output.Text;

public class ScadenzarioBean extends BaseBean<ScadenzarioModel, Long> implements IBean<ScadenzarioModel, Long>{

	private OutputGroup fieldsDatiGenerali = null;

	private Text nome;
	private Text idEnte;
	private Text cfEnte;
	private Text denominazioneEnte;
	private Text idIntermediarioPA;
	private Text codice;
	private Text idStazioneIntermediarioPA;

	private ConnettoreBean connettoreNotifica = null;
	private ConnettoreBean connettoreVerifica = null;

	private ScadenzarioModelId idSistemaEnte;
	
	public ScadenzarioBean(){
		try {
			init();
		} catch (FactoryException e) {
		}
	}

	private void init() throws FactoryException{
		
		this.idSistemaEnte = new ScadenzarioModelId();

		this.nome = this.getWebGenericProjectFactory().getOutputFieldFactory().createText();
		this.nome.setLabel(Utils.getInstance().getMessageFromResourceBundle("scadenzario.nome"));
		this.nome.setName("scad_nome");
		
		this.idEnte = this.getWebGenericProjectFactory().getOutputFieldFactory().createText();
		this.idEnte.setLabel(Utils.getInstance().getMessageFromResourceBundle("scadenzario.idEnte"));
		this.idEnte.setName("scad_idEnte");
		
		this.denominazioneEnte = this.getWebGenericProjectFactory().getOutputFieldFactory().createText();
		this.denominazioneEnte.setLabel(Utils.getInstance().getMessageFromResourceBundle("scadenzario.idEnte"));
		this.denominazioneEnte.setName("scad_denominazioneEnte");
		
		this.cfEnte = this.getWebGenericProjectFactory().getOutputFieldFactory().createText();
		this.cfEnte.setLabel(Utils.getInstance().getMessageFromResourceBundle("scadenzario.idEnte"));
		this.cfEnte.setName("scad_cfEnte");

		this.idIntermediarioPA = this.getWebGenericProjectFactory().getOutputFieldFactory().createText();
		this.idIntermediarioPA.setLabel(Utils.getInstance().getMessageFromResourceBundle("scadenzario.idIntermediarioPA"));
		this.idIntermediarioPA.setName("scad_idIntermediarioPA");
		
		this.idStazioneIntermediarioPA = this.getWebGenericProjectFactory().getOutputFieldFactory().createText();
		this.idStazioneIntermediarioPA.setLabel(Utils.getInstance().getMessageFromResourceBundle("scadenzario.idStazioneIntermediarioPA"));
		this.idStazioneIntermediarioPA.setName("scad_idStazioneIntermediarioPA");

		this.codice = this.getWebGenericProjectFactory().getOutputFieldFactory().createText();
		this.codice.setLabel(Utils.getInstance().getMessageFromResourceBundle("scadenzario.codice"));
		this.codice.setName("scad_codice");

		this.fieldsDatiGenerali = this.getWebGenericProjectFactory().getOutputFieldFactory().createOutputGroup();
		this.fieldsDatiGenerali.setId("datiGeneraliScadenzario");
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

	public Text getIdStazioneIntermediarioPA() {
		return idStazioneIntermediarioPA;
	}

	public void setIdStazioneIntermediarioPA(
			Text idStazioneIntermediarioPA) {
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

	public Text getNome() {
		return nome;
	}

	public void setNome(Text nome) {
		this.nome = nome;
	}

	public Text getIdIntermediarioPA() {
		return idIntermediarioPA;
	}

	public void setIdIntermediarioPA(Text idIntermediarioPA) {
		this.idIntermediarioPA = idIntermediarioPA;
	}

	public Text getCodice() {
		return codice;
	}

	public void setCodice(Text codice) {
		this.codice = codice;
	}

	public ConnettoreBean getConnettoreNotifica() {
		return connettoreNotifica;
	}

	public void setConnettoreNotifica(ConnettoreBean connettoreNotifica) {
		this.connettoreNotifica = connettoreNotifica;
	}

	public Text getIdEnte() {
		return idEnte;
	}

	public void setIdEnte(Text idEnte) {
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
	
	public Text getCfEnte() {
		return cfEnte;
	}

	public void setCfEnte(Text cfEnte) {
		this.cfEnte = cfEnte;
	}

	public Text getDenominazioneEnte() {
		return denominazioneEnte;
	}

	public void setDenominazioneEnte(Text denominazioneEnte) {
		this.denominazioneEnte = denominazioneEnte;
	}
	
	
}
