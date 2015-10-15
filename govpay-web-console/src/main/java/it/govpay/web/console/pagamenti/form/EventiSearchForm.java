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
package it.govpay.web.console.pagamenti.form;

import it.govpay.web.console.pagamenti.mbean.EventiMBean;
import it.govpay.web.console.utils.Utils;

import java.io.Serializable;

import javax.annotation.PostConstruct;
import javax.enterprise.context.SessionScoped;
import javax.inject.Named;

import org.apache.commons.lang.StringUtils;
import org.openspcoop2.generic_project.web.factory.FactoryException;
import org.openspcoop2.generic_project.web.form.CostantiForm;
import org.openspcoop2.generic_project.web.form.SearchForm;
import org.openspcoop2.generic_project.web.impl.jsf1.form.BaseSearchForm;
import org.openspcoop2.generic_project.web.impl.jsf1.input.SelectItem;
import org.openspcoop2.generic_project.web.input.DateTime;
import org.openspcoop2.generic_project.web.input.SelectList;
import org.openspcoop2.generic_project.web.input.Text;


@Named("eventiSearchForm") @SessionScoped
public class EventiSearchForm  extends BaseSearchForm implements SearchForm,Cloneable,Serializable
{

	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L; 
	
	private DateTime data = null;
	private Text dominio = null;
	private Text iuv = null;
	private SelectList<SelectItem> categoria = null;
	private SelectList<SelectItem> tipo = null;
	private SelectList<SelectItem> sottoTipo = null;
	private Text ccp = null;
	
	private Boolean filtroSet = true;
	private EventiMBean mBean = null ;
	
	@PostConstruct
	@Override 
	public void init() throws FactoryException{
		// Properties del form
		this.setId("formEventi");
		this.setNomeForm("Filtri di Ricerca");
		this.setClosable(true);
		this.setRendered(true); 
		
		this.data = this.getWebGenericProjectFactory().getInputFieldFactory().createDateTime();
		this.data.setName("data");
		this.data.setDefaultValue(null);
		this.data.setDefaultValue2(null);
		this.data.setInterval(true);
		this.data.setLabel(Utils.getInstance().getMessageFromResourceBundle("eventi.search.data"));
		this.data.setRendered(false); 
		
		this.dominio = this.getWebGenericProjectFactory().getInputFieldFactory().createText();
		this.dominio.setLabel(Utils.getInstance().getMessageFromResourceBundle("eventi.search.dominio"));
		this.dominio.setName("dominio");
		this.dominio.setDefaultValue(null);
		this.dominio.setRendered(false); 
		
		this.iuv = this.getWebGenericProjectFactory().getInputFieldFactory().createText();
		this.iuv.setLabel(Utils.getInstance().getMessageFromResourceBundle("eventi.search.iuv"));
		this.iuv.setName("iuv");
		this.iuv.setDefaultValue(null);
		
		this.ccp = this.getWebGenericProjectFactory().getInputFieldFactory().createText();
		this.ccp.setLabel(Utils.getInstance().getMessageFromResourceBundle("eventi.search.ccp"));
		this.ccp.setName("ccp");
		this.ccp.setDefaultValue(null);
		this.ccp.setRendered(false); 
		
		this.categoria = this.getWebGenericProjectFactory().getInputFieldFactory().createSelectList();
		this.categoria.setName("categoria");
		this.categoria.setValue(null);
		this.categoria.setLabel(Utils.getInstance().getMessageFromResourceBundle("eventi.search.categoria"));
		this.categoria.setRendered(false); 
		
		this.tipo = this.getWebGenericProjectFactory().getInputFieldFactory().createSelectList();
		this.tipo.setName("tipo");
		this.tipo.setValue(null);
		this.tipo.setLabel(Utils.getInstance().getMessageFromResourceBundle("eventi.search.tipo"));
		this.tipo.setRendered(false); 
		
		this.sottoTipo = this.getWebGenericProjectFactory().getInputFieldFactory().createSelectList();
		this.sottoTipo.setName("sottoTipo");
		this.sottoTipo.setValue(null);
		this.sottoTipo.setLabel(Utils.getInstance().getMessageFromResourceBundle("eventi.search.sottoTipo"));
		this.sottoTipo.setRendered(false); 
		
		
	}
	
	
	
	@Override
	public void reset(){
		resetParametriPaginazione();
		
		this.dominio.reset();
		this.iuv.reset();
		this.data.reset();
		this.categoria.reset();
		this.tipo.reset();
		this.sottoTipo.reset();
		this.ccp.reset();
		
	}
	
	@Override
	public Object clone() throws CloneNotSupportedException {
		return super.clone();
	}
	

	private Boolean isFiltroSet(){
		Boolean isSet = true;
		
		
		isSet = isSet && this.data.getValue() == null;
		isSet = isSet && this.data.getValue2() == null;
		isSet = isSet && StringUtils.isEmpty(this.dominio.getValue());
		isSet = isSet && StringUtils.isEmpty(this.iuv.getValue());
		isSet = isSet && StringUtils.isEmpty(this.ccp.getValue());
		
		isSet = isSet && (StringUtils.isEmpty((this.categoria.getValue() != null ? this.categoria.getValue().getValue() : CostantiForm.ALL)) ||
				(this.categoria.getValue() != null ? this.categoria.getValue().getValue() : CostantiForm.ALL).equals(CostantiForm.ALL));
		isSet = isSet && (StringUtils.isEmpty((this.tipo.getValue() != null ? this.tipo.getValue().getValue() : CostantiForm.ALL)) ||
				(this.tipo.getValue() != null ? this.tipo.getValue().getValue() : CostantiForm.ALL).equals(CostantiForm.ALL));
		isSet = isSet && (StringUtils.isEmpty((this.sottoTipo.getValue() != null ? this.sottoTipo.getValue().getValue() : CostantiForm.ALL)) ||
				(this.sottoTipo.getValue() != null ? this.sottoTipo.getValue().getValue() : CostantiForm.ALL).equals(CostantiForm.ALL));
		
		return !isSet;
	}

	public String getFiltroSet() {

		this.filtroSet = this.filtroSet || this.isFiltroSet();
		
		return this.filtroSet ? "true" : "false";
	}
	public void setFiltroSet(String filtroSet) {
		this.filtroSet = filtroSet.equals("true") ? true : false;
	}

	public String getStatoFiltro(){
		StringBuilder sb = new StringBuilder();
		
		sb.append(super.toString());
		
		sb.append("\n").append("-------------------Filtro di ricerca---------").append("\n");
		sb.append("\t").append("Data Inizio[").append(this.data.getValue()).append("]").append("\n");
		sb.append("\t").append("Data Fine[").append(this.data.getValue2()).append("]").append("\n");
		sb.append("\t").append("Id Dominio[").append(this.dominio.getValue()).append("]").append("\n");
		sb.append("\t").append("Codice Contesto Pagamento[").append(this.ccp.getValue()).append("]").append("\n");
		sb.append("\t").append("Id Univoco Versamento[").append(this.iuv.getValue()).append("]").append("\n");
		sb.append("\t").append("Categoria Evento[").append((this.categoria.getValue() != null ? this.categoria.getValue().getLabel() : CostantiForm.ALL)).append("]").append("\n");
		sb.append("\t").append("Tipo Evento[").append((this.tipo.getValue() != null ? this.tipo.getValue().getLabel() : CostantiForm.ALL)).append("]").append("\n");
		sb.append("\t").append("Sottotipo Evento[").append((this.sottoTipo.getValue() != null ? this.sottoTipo.getValue().getLabel() : CostantiForm.ALL)).append("]").append("\n");
		sb.append("-----------------------------------------------------------");
		
		return sb.toString();
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



	public SelectList<SelectItem> getCategoria() {
		return categoria;
	}



	public void setCategoria(SelectList<SelectItem> categoria) {
		this.categoria = categoria;
	}



	public SelectList<SelectItem> getTipo() {
		return tipo;
	}



	public void setTipo(SelectList<SelectItem> tipo) {
		this.tipo = tipo;
	}



	public SelectList<SelectItem> getSottoTipo() {
		return sottoTipo;
	}



	public void setSottoTipo(SelectList<SelectItem> sottoTipo) {
		this.sottoTipo = sottoTipo;
	}



	public Text getCcp() {
		return ccp;
	}



	public void setCcp(Text ccp) {
		this.ccp = ccp;
	}



	public EventiMBean getmBean() {
		return mBean;
	}



	public void setmBean(EventiMBean mBean) {
		this.mBean = mBean;
	}



	public void setFiltroSet(Boolean filtroSet) {
		this.filtroSet = filtroSet;
	}
	
	

}
