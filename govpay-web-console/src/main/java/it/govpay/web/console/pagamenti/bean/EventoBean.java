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

import it.govpay.ejb.ndp.model.EventiInterfacciaModel.Categoria;
import it.govpay.ejb.ndp.model.EventiInterfacciaModel.Componente;
import it.govpay.ejb.ndp.model.EventiInterfacciaModel.Evento;
import it.govpay.ejb.ndp.model.EventiInterfacciaModel.SottoTipo;
import it.govpay.web.console.utils.Utils;

import java.util.Date;

import org.openspcoop2.generic_project.web.bean.BaseBean;
import org.openspcoop2.generic_project.web.presentation.field.OutputDate;
import org.openspcoop2.generic_project.web.presentation.field.OutputField;
import org.openspcoop2.generic_project.web.presentation.field.OutputGroup;
import org.openspcoop2.generic_project.web.presentation.field.OutputText;

public class EventoBean extends BaseBean<Evento, Long>{


	private OutputField<Date> data;
	private OutputField<String> dominio;
	private OutputField<String> iuv;
	private OutputField<String> ccp;
	private OutputField<String> psp;
	private OutputField<String> tipoVersamento;
	private OutputField<String> componente;
	private OutputField<String> categoria;
	private OutputField<String> tipo;
	private OutputField<String> sottoTipo;
	private OutputField<String> fruitore;
	private OutputField<String> erogatore;
	private OutputField<String> stazioneIntermediarioPA;
	private OutputField<String> canalePagamento;
	private OutputField<String> parametri;
	private OutputField<String> esito;
	private OutputField<String> idEgov;
	private Long id;

	private InfospcoopBean infospcoop = null;
	//	private boolean esitoOk = false;

	// Gruppo Informazioni Dati Genareli
	private OutputGroup fieldsDatiGenerali = new OutputGroup();

	public EventoBean(){
		initFields();
	}

	private void initFields(){
		this.data = new OutputDate();
		this.data.setLabel(Utils.getMessageFromResourceBundle("evento.data"));
		this.data.setName("data");

		this.idEgov = new OutputText();
		this.idEgov.setLabel(Utils.getMessageFromResourceBundle("evento.idEgov"));
		this.idEgov.setName("idEgov");

		this.dominio = new OutputText();
		this.dominio.setLabel(Utils.getMessageFromResourceBundle("evento.dominio"));
		this.dominio.setName("dominio");

		this.iuv = new OutputText();
		this.iuv.setLabel(Utils.getMessageFromResourceBundle("evento.iuv"));
		this.iuv.setName("iuv");

		this.ccp = new OutputText();
		this.ccp.setLabel(Utils.getMessageFromResourceBundle("evento.ccp"));
		this.ccp.setName("ccp");

		this.psp = new OutputText();
		this.psp.setLabel(Utils.getMessageFromResourceBundle("evento.psp"));
		this.psp.setName("psp");

		this.tipoVersamento = new OutputText();
		this.tipoVersamento.setLabel(Utils.getMessageFromResourceBundle("evento.tipoVersamento"));
		this.tipoVersamento.setName("tipoVersamento");

		this.componente = new OutputText();
		this.componente.setLabel(Utils.getMessageFromResourceBundle("evento.componente"));
		this.componente.setName("componente");

		this.categoria = new OutputText();
		this.categoria.setLabel(Utils.getMessageFromResourceBundle("evento.categoria"));
		this.categoria.setName("categoria");

		this.tipo = new OutputText();
		this.tipo.setLabel(Utils.getMessageFromResourceBundle("evento.tipo"));
		this.tipo.setName("tipo");

		this.sottoTipo = new OutputText();
		this.sottoTipo.setLabel(Utils.getMessageFromResourceBundle("evento.sottoTipo"));
		this.sottoTipo.setName("sottoTipo");

		this.fruitore = new OutputText();
		this.fruitore.setLabel(Utils.getMessageFromResourceBundle("evento.fruitore"));
		this.fruitore.setName("fruitore");

		this.erogatore = new OutputText();
		this.erogatore.setLabel(Utils.getMessageFromResourceBundle("evento.erogatore"));
		this.erogatore.setName("erogatore");

		this.stazioneIntermediarioPA = new OutputText();
		this.stazioneIntermediarioPA.setLabel(Utils.getMessageFromResourceBundle("evento.stazioneIntermediarioPA"));
		this.stazioneIntermediarioPA.setName("stazioneIntermediarioPA");

		this.canalePagamento = new OutputText();
		this.canalePagamento.setLabel(Utils.getMessageFromResourceBundle("evento.canalePagamento"));
		this.canalePagamento.setName("canalePagamento");

		this.parametri = new OutputText();
		this.parametri.setLabel(Utils.getMessageFromResourceBundle("evento.parametri"));
		this.parametri.setName("parametri");

		this.esito = new OutputText();
		this.esito.setLabel(Utils.getMessageFromResourceBundle("evento.esito"));
		this.esito.setName("esito");

		this.fieldsDatiGenerali = new OutputGroup();
		this.fieldsDatiGenerali.setIdGroup("datiGenerali");
		this.fieldsDatiGenerali.setColumns(2);
		this.fieldsDatiGenerali.setRendered(true);
		this.fieldsDatiGenerali.setStyleClass("beanTable"); 
		this.fieldsDatiGenerali.setColumnClasses("labelAllineataDx,valueAllineataSx");

		this.fieldsDatiGenerali.addField(this.data);
		this.fieldsDatiGenerali.addField(this.idEgov);
		this.fieldsDatiGenerali.addField(this.dominio);
		this.fieldsDatiGenerali.addField(this.iuv);
		this.fieldsDatiGenerali.addField(this.ccp);
		this.fieldsDatiGenerali.addField(this.psp);
		this.fieldsDatiGenerali.addField(this.tipoVersamento);
		this.fieldsDatiGenerali.addField(this.componente);
		this.fieldsDatiGenerali.addField(this.categoria);
		this.fieldsDatiGenerali.addField(this.tipo);
		this.fieldsDatiGenerali.addField(this.sottoTipo);
		this.fieldsDatiGenerali.addField(this.fruitore);
		this.fieldsDatiGenerali.addField(this.erogatore);
		this.fieldsDatiGenerali.addField(this.stazioneIntermediarioPA);
		this.fieldsDatiGenerali.addField(this.canalePagamento);
		this.fieldsDatiGenerali.addField(this.esito);
		this.fieldsDatiGenerali.addField(this.parametri);
	}


	public InfospcoopBean getInfospcoop() {
		return infospcoop;
	}

	public void setInfospcoop(InfospcoopBean infospcoop) {
		this.infospcoop = infospcoop;
	}

	@Override
	public void setDTO(Evento dto) {
		super.setDTO(dto);

		this.data.setValue(this.getDTO().getData());
		this.idEgov.setValue(this.getDTO().getIdEgov());
		this.dominio.setValue(this.getDTO().getDominio());
		this.iuv.setValue(this.getDTO().getIuv());
		this.ccp.setValue(this.getDTO().getCcp());
		this.psp.setValue(this.getDTO().getPsp());
		this.tipoVersamento.setValue(this.getDTO().getTipoVersamento());
		Componente componente2 = this.getDTO().getComponente();
		if(componente2!=null)
			this.componente.setValue(componente2.toString());
		Categoria categoria2 = this.getDTO().getCategoria();
		if(categoria2!=null)
			this.categoria.setValue(categoria2.toString());
		this.tipo.setValue(this.getDTO().getTipo());
		SottoTipo sottoTipo2 = this.getDTO().getSottoTipo();
		if(sottoTipo2!=null)
			this.sottoTipo.setValue(sottoTipo2.toString());
		this.fruitore.setValue(this.getDTO().getFruitore());
		this.erogatore.setValue(this.getDTO().getErogatore());
		this.stazioneIntermediarioPA.setValue(this.getDTO().getStazioneIntermediarioPA());
		this.canalePagamento.setValue(this.getDTO().getCanalePagamento());
		this.parametri.setValue(this.getDTO().getParametri());

		// Esito
		this.esito.setValue(this.getDTO().getEsito());

		if(this.dto != null && this.dto.getEsito() != null){
			if(this.dto.getEsito().contains("OK")){
				this.esito.setStyleClass("esito_ok");
			}else{
				this.esito.setStyleClass("esito_ko");
			}

		}
	}

	public OutputField<Date> getData() {
		return data;
	}

	public void setData(OutputField<Date> data) {
		this.data = data;
	}

	public OutputField<String> getDominio() {
		return dominio;
	}

	public void setDominio(OutputField<String> dominio) {
		this.dominio = dominio;
	}

	public OutputField<String> getIuv() {
		return iuv;
	}

	public void setIuv(OutputField<String> iuv) {
		this.iuv = iuv;
	}

	public OutputField<String> getCcp() {
		return ccp;
	}

	public void setCcp(OutputField<String> ccp) {
		this.ccp = ccp;
	}

	public OutputField<String> getPsp() {
		return psp;
	}

	public void setPsp(OutputField<String> psp) {
		this.psp = psp;
	}

	public OutputField<String> getTipoVersamento() {
		return tipoVersamento;
	}

	public void setTipoVersamento(OutputField<String> tipoVersamento) {
		this.tipoVersamento = tipoVersamento;
	}

	public OutputField<String> getComponente() {
		return componente;
	}

	public void setComponente(OutputField<String> componente) {
		this.componente = componente;
	}

	public OutputField<String> getCategoria() {
		return categoria;
	}

	public void setCategoria(OutputField<String> categoria) {
		this.categoria = categoria;
	}

	public OutputField<String> getTipo() {
		return tipo;
	}

	public void setTipo(OutputField<String> tipo) {
		this.tipo = tipo;
	}

	public OutputField<String> getSottoTipo() {
		return sottoTipo;
	}

	public void setSottoTipo(OutputField<String> sottoTipo) {
		this.sottoTipo = sottoTipo;
	}

	public OutputField<String> getFruitore() {
		return fruitore;
	}

	public void setFruitore(OutputField<String> fruitore) {
		this.fruitore = fruitore;
	}

	public OutputField<String> getErogatore() {
		return erogatore;
	}

	public void setErogatore(OutputField<String> erogatore) {
		this.erogatore = erogatore;
	}

	public OutputField<String> getStazioneIntermediarioPA() {
		return stazioneIntermediarioPA;
	}

	public void setStazioneIntermediarioPA(
			OutputField<String> stazioneIntermediarioPA) {
		this.stazioneIntermediarioPA = stazioneIntermediarioPA;
	}

	public OutputField<String> getCanalePagamento() {
		return canalePagamento;
	}

	public void setCanalePagamento(OutputField<String> canalePagamento) {
		this.canalePagamento = canalePagamento;
	}

	public OutputField<String> getParametri() {
		return parametri;
	}

	public void setParametri(OutputField<String> parametri) {
		this.parametri = parametri;
	}

	public OutputField<String> getEsito() {
		return esito;
	}

	public void setEsito(OutputField<String> esito) {
		this.esito = esito;
	}

	public OutputField<String> getIdEgov() {
		return idEgov;
	}

	public void setIdEgov(OutputField<String> idEgov) {
		this.idEgov = idEgov;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public OutputGroup getFieldsDatiGenerali() {
		return fieldsDatiGenerali;
	}

	public void setFieldsDatiGenerali(OutputGroup fieldsDatiGenerali) {
		this.fieldsDatiGenerali = fieldsDatiGenerali;
	}



}
