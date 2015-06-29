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

import org.openspcoop2.generic_project.web.bean.IBean;
import org.openspcoop2.generic_project.web.factory.Costanti;
import org.openspcoop2.generic_project.web.factory.FactoryException;
import org.openspcoop2.generic_project.web.impl.jsf1.bean.BaseBean;
import org.openspcoop2.generic_project.web.output.OutputGroup;
import org.openspcoop2.generic_project.web.output.OutputNumber;
import org.openspcoop2.generic_project.web.output.Text;

import it.govpay.rs.DatiSingoloPagamento;
import it.govpay.rs.VerificaPagamento;
import it.govpay.web.console.utils.Utils;

public class EsitoBean extends BaseBean<VerificaPagamento, String> implements IBean<VerificaPagamento, String>{ 


	private Text statoPagamento = null;
	private OutputNumber importoPagato = null;
	private Text identificativoBeneficiario = null;
	private Text urlPagamento = null;
	private Text iuv = null;

	// Gruppo Informazioni Dati Genareli
	private OutputGroup fieldsDatiGenerali = null;

	private List<SingoloVersamentoBean> listaVersamenti = null;

	public EsitoBean(){
		try {
			this.iuv = this.getWebGenericProjectFactory().getOutputFieldFactory().createText();
			this.iuv.setLabel(Utils.getInstance().getMessageFromResourceBundle("distinta.pagamento.iuv"));
			this.iuv.setName("iuv");

			this.identificativoBeneficiario = this.getWebGenericProjectFactory().getOutputFieldFactory().createText();
			this.identificativoBeneficiario.setLabel(Utils.getInstance().getMessageFromResourceBundle("distinta.pagamento.identificativoBeneficiario"));
			this.identificativoBeneficiario.setName("identificativoBeneficiario");

			this.statoPagamento = this.getWebGenericProjectFactory().getOutputFieldFactory().createText();
			this.statoPagamento.setLabel(Utils.getInstance().getMessageFromResourceBundle("distinta.pagamento.statoPagamento"));
			this.statoPagamento.setName("statoPagamento");

			this.urlPagamento = this.getWebGenericProjectFactory().getOutputFieldFactory().createText();
			this.urlPagamento.setLabel(Utils.getInstance().getMessageFromResourceBundle("distinta.pagamento.urlPagamento"));
			this.urlPagamento.setName("urlPagamento");

			this.importoPagato =this.getWebGenericProjectFactory().getOutputFieldFactory().createNumber();
			this.importoPagato.setLabel(Utils.getInstance().getMessageFromResourceBundle("distinta.pagamento.importoPagato"));
			this.importoPagato.setName("importoPagato");
			this.importoPagato.setConverterType(Costanti.CONVERT_TYPE_CURRENCY);
			this.importoPagato.setCurrencySymbol(Costanti.CURRENCY_SYMBOL_EURO);

			this.listaVersamenti  = new ArrayList<SingoloVersamentoBean>();

			this.fieldsDatiGenerali = this.getWebGenericProjectFactory().getOutputFieldFactory().createOutputGroup();
			this.fieldsDatiGenerali.setId ("esitoDatiGenerali");
			this.fieldsDatiGenerali.setColumns(2);
			this.fieldsDatiGenerali.setRendered(true);
			this.fieldsDatiGenerali.setStyleClass("beanTable"); 
			this.fieldsDatiGenerali.setColumnClasses("labelAllineataDx,valueAllineataSx");

			this.fieldsDatiGenerali.addField(this.statoPagamento);
			this.fieldsDatiGenerali.addField(this.iuv);
			this.fieldsDatiGenerali.addField(this.identificativoBeneficiario);
			this.fieldsDatiGenerali.addField(this.importoPagato);
			this.fieldsDatiGenerali.addField(this.urlPagamento);
		} catch (FactoryException e) {
		}

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


	public Text getStatoPagamento() {
		return statoPagamento;
	}


	public void setStatoPagamento(Text statoPagamento) {
		this.statoPagamento = statoPagamento;
	}


	public OutputNumber getImportoPagato() {
		return importoPagato;
	}


	public void setImportoPagato(OutputNumber importoPagato) {
		this.importoPagato = importoPagato;
	}


	public Text getIdentificativoBeneficiario() {
		return identificativoBeneficiario;
	}


	public void setIdentificativoBeneficiario(
			Text identificativoBeneficiario) {
		this.identificativoBeneficiario = identificativoBeneficiario;
	}


	public Text getUrlPagamento() {
		return urlPagamento;
	}


	public void setUrlPagamento(Text urlPagamento) {
		this.urlPagamento = urlPagamento;
	}


	public Text getIuv() {
		return iuv;
	}


	public void setIuv(Text iuv) {
		this.iuv = iuv;
	}


	public List<SingoloVersamentoBean> getListaVersamenti() {
		return listaVersamenti;
	}


	public void setListaVersamenti(List<SingoloVersamentoBean> listaVersamenti) {
		this.listaVersamenti = listaVersamenti;
	}


	@Override
	public String getId() {
		return		this.getDTO().getIuv();
	}




}
