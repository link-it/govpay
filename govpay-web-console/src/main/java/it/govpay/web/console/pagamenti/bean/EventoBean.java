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

import org.openspcoop2.generic_project.web.bean.IBean;
import org.openspcoop2.generic_project.web.factory.FactoryException;
import org.openspcoop2.generic_project.web.impl.jsf1.bean.BaseBean;
import org.openspcoop2.generic_project.web.output.DateTime;
import org.openspcoop2.generic_project.web.output.OutputGroup;
import org.openspcoop2.generic_project.web.output.Text;

public class EventoBean extends BaseBean<Evento, Long> implements IBean<Evento, Long>{ 


	private DateTime data;
	private Text dominio;
	private Text iuv;
	private Text ccp;
	private Text psp;
	private Text tipoVersamento;
	private Text componente;
	private Text categoria;
	private Text tipo;
	private Text sottoTipo;
	private Text fruitore;
	private Text erogatore;
	private Text stazioneIntermediarioPA;
	private Text canalePagamento;
	private Text parametri;
	private Text esito;
	private Text idEgov;

	private InfospcoopBean infospcoop = null;
	//	private boolean esitoOk = false;

	// Gruppo Informazioni Dati Genareli
	private OutputGroup fieldsDatiGenerali = null;

	public EventoBean(){
		try {
			initFields();
		} catch (FactoryException e) {
		}
	}

	private void initFields() throws FactoryException{
		this.data = this.getWebGenericProjectFactory().getOutputFieldFactory().createDateTime();
		this.data.setLabel(Utils.getInstance().getMessageFromResourceBundle("evento.data"));
		this.data.setName("data");

		this.idEgov = this.getWebGenericProjectFactory().getOutputFieldFactory().createText();
		this.idEgov.setLabel(Utils.getInstance().getMessageFromResourceBundle("evento.idEgov"));
		this.idEgov.setName("idEgov");

		this.dominio = this.getWebGenericProjectFactory().getOutputFieldFactory().createText();
		this.dominio.setLabel(Utils.getInstance().getMessageFromResourceBundle("evento.dominio"));
		this.dominio.setName("dominio");

		this.iuv = this.getWebGenericProjectFactory().getOutputFieldFactory().createText();
		this.iuv.setLabel(Utils.getInstance().getMessageFromResourceBundle("evento.iuv"));
		this.iuv.setName("iuv");

		this.ccp = this.getWebGenericProjectFactory().getOutputFieldFactory().createText();
		this.ccp.setLabel(Utils.getInstance().getMessageFromResourceBundle("evento.ccp"));
		this.ccp.setName("ccp");

		this.psp = this.getWebGenericProjectFactory().getOutputFieldFactory().createText();
		this.psp.setLabel(Utils.getInstance().getMessageFromResourceBundle("evento.psp"));
		this.psp.setName("psp");

		this.tipoVersamento = this.getWebGenericProjectFactory().getOutputFieldFactory().createText();
		this.tipoVersamento.setLabel(Utils.getInstance().getMessageFromResourceBundle("evento.tipoVersamento"));
		this.tipoVersamento.setName("tipoVersamento");

		this.componente = this.getWebGenericProjectFactory().getOutputFieldFactory().createText();
		this.componente.setLabel(Utils.getInstance().getMessageFromResourceBundle("evento.componente"));
		this.componente.setName("componente");

		this.categoria = this.getWebGenericProjectFactory().getOutputFieldFactory().createText();
		this.categoria.setLabel(Utils.getInstance().getMessageFromResourceBundle("evento.categoria"));
		this.categoria.setName("categoria");

		this.tipo = this.getWebGenericProjectFactory().getOutputFieldFactory().createText();
		this.tipo.setLabel(Utils.getInstance().getMessageFromResourceBundle("evento.tipo"));
		this.tipo.setName("tipo");

		this.sottoTipo = this.getWebGenericProjectFactory().getOutputFieldFactory().createText();
		this.sottoTipo.setLabel(Utils.getInstance().getMessageFromResourceBundle("evento.sottoTipo"));
		this.sottoTipo.setName("sottoTipo");

		this.fruitore = this.getWebGenericProjectFactory().getOutputFieldFactory().createText();
		this.fruitore.setLabel(Utils.getInstance().getMessageFromResourceBundle("evento.fruitore"));
		this.fruitore.setName("fruitore");

		this.erogatore = this.getWebGenericProjectFactory().getOutputFieldFactory().createText();
		this.erogatore.setLabel(Utils.getInstance().getMessageFromResourceBundle("evento.erogatore"));
		this.erogatore.setName("erogatore");

		this.stazioneIntermediarioPA = this.getWebGenericProjectFactory().getOutputFieldFactory().createText();
		this.stazioneIntermediarioPA.setLabel(Utils.getInstance().getMessageFromResourceBundle("evento.stazioneIntermediarioPA"));
		this.stazioneIntermediarioPA.setName("stazioneIntermediarioPA");

		this.canalePagamento = this.getWebGenericProjectFactory().getOutputFieldFactory().createText();
		this.canalePagamento.setLabel(Utils.getInstance().getMessageFromResourceBundle("evento.canalePagamento"));
		this.canalePagamento.setName("canalePagamento");

		this.parametri = this.getWebGenericProjectFactory().getOutputFieldFactory().createText();
		this.parametri.setLabel(Utils.getInstance().getMessageFromResourceBundle("evento.parametri"));
		this.parametri.setName("parametri");

		this.esito = this.getWebGenericProjectFactory().getOutputFieldFactory().createText();
		this.esito.setLabel(Utils.getInstance().getMessageFromResourceBundle("evento.esito"));
		this.esito.setName("esito");

		this.fieldsDatiGenerali = this.getWebGenericProjectFactory().getOutputFieldFactory().createOutputGroup();
		this.fieldsDatiGenerali.setId("datiGenerali");
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

	public DateTime getData() {
		return data;
	}

	public void setData(DateTime data) {
		this.data = data;
	}

	public Text getDominio() {
		return dominio;
	}

	public void setDominio(Text dominio) {
		this.dominio = dominio;
	}

	public Text getIuv() {
		return iuv;
	}

	public void setIuv(Text iuv) {
		this.iuv = iuv;
	}

	public Text getCcp() {
		return ccp;
	}

	public void setCcp(Text ccp) {
		this.ccp = ccp;
	}

	public Text getPsp() {
		return psp;
	}

	public void setPsp(Text psp) {
		this.psp = psp;
	}

	public Text getTipoVersamento() {
		return tipoVersamento;
	}

	public void setTipoVersamento(Text tipoVersamento) {
		this.tipoVersamento = tipoVersamento;
	}

	public Text getComponente() {
		return componente;
	}

	public void setComponente(Text componente) {
		this.componente = componente;
	}

	public Text getCategoria() {
		return categoria;
	}

	public void setCategoria(Text categoria) {
		this.categoria = categoria;
	}

	public Text getTipo() {
		return tipo;
	}

	public void setTipo(Text tipo) {
		this.tipo = tipo;
	}

	public Text getSottoTipo() {
		return sottoTipo;
	}

	public void setSottoTipo(Text sottoTipo) {
		this.sottoTipo = sottoTipo;
	}

	public Text getFruitore() {
		return fruitore;
	}

	public void setFruitore(Text fruitore) {
		this.fruitore = fruitore;
	}

	public Text getErogatore() {
		return erogatore;
	}

	public void setErogatore(Text erogatore) {
		this.erogatore = erogatore;
	}

	public Text getStazioneIntermediarioPA() {
		return stazioneIntermediarioPA;
	}

	public void setStazioneIntermediarioPA(
			Text stazioneIntermediarioPA) {
		this.stazioneIntermediarioPA = stazioneIntermediarioPA;
	}

	public Text getCanalePagamento() {
		return canalePagamento;
	}

	public void setCanalePagamento(Text canalePagamento) {
		this.canalePagamento = canalePagamento;
	}

	public Text getParametri() {
		return parametri;
	}

	public void setParametri(Text parametri) {
		this.parametri = parametri;
	}

	public Text getEsito() {
		return esito;
	}

	public void setEsito(Text esito) {
		this.esito = esito;
	}

	public Text getIdEgov() {
		return idEgov;
	}

	public void setIdEgov(Text idEgov) {
		this.idEgov = idEgov;
	}

	public OutputGroup getFieldsDatiGenerali() {
		return fieldsDatiGenerali;
	}

	public void setFieldsDatiGenerali(OutputGroup fieldsDatiGenerali) {
		this.fieldsDatiGenerali = fieldsDatiGenerali;
	}

	@Override
	public Long getId() {
		return this.getDTO().getId();
	}



}
