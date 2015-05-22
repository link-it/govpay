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

import java.util.ArrayList;
import java.util.List;

import it.govpay.rs.DatiSingoloPagamento;
import it.govpay.rs.VerificaPagamento;

import org.openspcoop2.generic_project.web.bean.BaseBean;
import org.openspcoop2.generic_project.web.core.Utils;
import org.openspcoop2.generic_project.web.presentation.field.OutputField;
import org.openspcoop2.generic_project.web.presentation.field.OutputGroup;
import org.openspcoop2.generic_project.web.presentation.field.OutputNumber;
import org.openspcoop2.generic_project.web.presentation.field.OutputText;

public class EsitoBean extends BaseBean<VerificaPagamento, String>{


	private OutputField<String> statoPagamento = null;
	private OutputField<Number> importoPagato = null;
	private OutputField<String> identificativoBeneficiario = null;
	private OutputField<String> urlPagamento = null;
	private OutputField<String> iuv = null;
	
	// Gruppo Informazioni Dati Genareli
	private OutputGroup fieldsDatiGenerali = new OutputGroup();
	
	private List<SingoloVersamentoBean> listaVersamenti = null;
	
	public EsitoBean(){
		
		
		
		this.iuv = new OutputText();
		this.iuv.setLabel(Utils.getMessageFromResourceBundle("distinta.pagamento.iuv"));
		this.iuv.setName("iuv");

		this.identificativoBeneficiario = new OutputText();
		this.identificativoBeneficiario.setLabel(Utils.getMessageFromResourceBundle("distinta.pagamento.identificativoBeneficiario"));
		this.identificativoBeneficiario.setName("identificativoBeneficiario");

		this.statoPagamento = new OutputText();
		this.statoPagamento.setLabel(Utils.getMessageFromResourceBundle("distinta.pagamento.statoPagamento"));
		this.statoPagamento.setName("statoPagamento");

		this.urlPagamento = new OutputText();
		this.urlPagamento.setLabel(Utils.getMessageFromResourceBundle("distinta.pagamento.urlPagamento"));
		this.urlPagamento.setName("urlPagamento");

		this.importoPagato = new OutputNumber<Double>();
		this.importoPagato.setType("valuta"); 
		this.importoPagato.setLabel(Utils.getMessageFromResourceBundle("distinta.pagamento.importoPagato"));
		this.importoPagato.setName("importoPagato");
		((OutputNumber<Number>)this.importoPagato).setConverterType(OutputNumber.CONVERT_TYPE_CURRENCY);
		((OutputNumber<Number>)this.importoPagato).setCurrencySymbol(OutputNumber.CURRENCY_SYMBOL_EURO);
		
		this.listaVersamenti  = new ArrayList<SingoloVersamentoBean>();
		
		this.fieldsDatiGenerali = new OutputGroup();
		this.fieldsDatiGenerali.setIdGroup("esitoDatiGenerali");
		this.fieldsDatiGenerali.setColumns(2);
		this.fieldsDatiGenerali.setRendered(true);
		this.fieldsDatiGenerali.setStyleClass("beanTable"); 
		this.fieldsDatiGenerali.setColumnClasses("labelAllineataDx,valueAllineataSx");
		
		this.fieldsDatiGenerali.addField(this.statoPagamento);
		this.fieldsDatiGenerali.addField(this.iuv);
		this.fieldsDatiGenerali.addField(this.identificativoBeneficiario);
		this.fieldsDatiGenerali.addField(this.importoPagato);
		this.fieldsDatiGenerali.addField(this.urlPagamento);
	}
	
	
	@Override
	public void setDTO(VerificaPagamento dto) {
		super.setDTO(dto);
		
		 this.statoPagamento.setValue(this.getDTO().getStatoPagamento().toString());
		 this.importoPagato.setValue(this.getDTO().getImportoTotalePagato());
		 this.identificativoBeneficiario.setValue(this.getDTO().getIdentificativoBeneficiario());
		 this.urlPagamento.setValue(this.getDTO().getUrlPagamento());
		 this.iuv.setValue(this.getDTO().getIuv()); 
		 
		 if(this.getDTO().getDatiSingoloPagamentos() != null && this.getDTO().getDatiSingoloPagamentos().size() > 0){
			 for (DatiSingoloPagamento singVers : this.getDTO().getDatiSingoloPagamentos()) {
				 SingoloVersamentoBean bean = new SingoloVersamentoBean();
				 bean.setDTO(singVers);
				 
				 this.listaVersamenti.add(bean);
				
			}
		 }
	}

	public OutputGroup getFieldsDatiGenerali() {
		return fieldsDatiGenerali;
	}

	public void setFieldsDatiGenerali(OutputGroup fieldsDatiGenerali) {
		this.fieldsDatiGenerali = fieldsDatiGenerali;
	}


	public OutputField<String> getStatoPagamento() {
		return statoPagamento;
	}


	public void setStatoPagamento(OutputField<String> statoPagamento) {
		this.statoPagamento = statoPagamento;
	}


	public OutputField<Number> getImportoPagato() {
		return importoPagato;
	}


	public void setImportoPagato(OutputField<Number> importoPagato) {
		this.importoPagato = importoPagato;
	}


	public OutputField<String> getIdentificativoBeneficiario() {
		return identificativoBeneficiario;
	}


	public void setIdentificativoBeneficiario(
			OutputField<String> identificativoBeneficiario) {
		this.identificativoBeneficiario = identificativoBeneficiario;
	}


	public OutputField<String> getUrlPagamento() {
		return urlPagamento;
	}


	public void setUrlPagamento(OutputField<String> urlPagamento) {
		this.urlPagamento = urlPagamento;
	}


	public OutputField<String> getIuv() {
		return iuv;
	}


	public void setIuv(OutputField<String> iuv) {
		this.iuv = iuv;
	}


	public List<SingoloVersamentoBean> getListaVersamenti() {
		return listaVersamenti;
	}


	public void setListaVersamenti(List<SingoloVersamentoBean> listaVersamenti) {
		this.listaVersamenti = listaVersamenti;
	}
	
	
	
	
}
