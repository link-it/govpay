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

import it.govpay.rs.DatiSingoloPagamento;
import it.govpay.web.console.utils.Utils;

import org.openspcoop2.generic_project.web.bean.IBean;
import org.openspcoop2.generic_project.web.factory.Costanti;
import org.openspcoop2.generic_project.web.factory.FactoryException;
import org.openspcoop2.generic_project.web.impl.jsf1.bean.BaseBean;
import org.openspcoop2.generic_project.web.output.DateTime;
import org.openspcoop2.generic_project.web.output.OutputGroup;
import org.openspcoop2.generic_project.web.output.OutputNumber;
import org.openspcoop2.generic_project.web.output.Text;

public class SingoloVersamentoBean extends BaseBean<DatiSingoloPagamento, String> implements IBean<DatiSingoloPagamento, String>{ 


	private Text iusv = null;
	private OutputNumber importoPagato = null;
	private Text esito =  null;
	private DateTime data = null;
	private Text iur =  null;

	// Gruppo Informazioni Dati Genareli
	private OutputGroup fieldsDatiGenerali =  null;

	public SingoloVersamentoBean(){
		try {
			this.iusv = this.getWebGenericProjectFactory().getOutputFieldFactory().createText();
			this.iusv.setLabel(Utils.getInstance().getMessageFromResourceBundle("distinta.pagamento.iusv"));
			this.iusv.setName("pag_iusv");

			this.iur = this.getWebGenericProjectFactory().getOutputFieldFactory().createText();
			this.iur.setLabel(Utils.getInstance().getMessageFromResourceBundle("distinta.pagamento.iur"));
			this.iur.setName("pag_iur");

			this.esito = this.getWebGenericProjectFactory().getOutputFieldFactory().createText();
			this.esito.setLabel(Utils.getInstance().getMessageFromResourceBundle("distinta.pagamento.esito"));
			this.esito.setName("pag_esito");

			this.data = this.getWebGenericProjectFactory().getOutputFieldFactory().createDateTime();
			this.data.setLabel(Utils.getInstance().getMessageFromResourceBundle("distinta.pagamento.data"));
			this.data.setName("pag_data");

			this.importoPagato = this.getWebGenericProjectFactory().getOutputFieldFactory().createNumber();
			this.importoPagato.setLabel(Utils.getInstance().getMessageFromResourceBundle("distinta.pagamento.importoPagato"));
			this.importoPagato.setName("pag_importoPagato");
			this.importoPagato.setConverterType(Costanti.CONVERT_TYPE_CURRENCY);
			this.importoPagato.setCurrencySymbol(Costanti.CURRENCY_SYMBOL_EURO);


			this.fieldsDatiGenerali = this.getWebGenericProjectFactory().getOutputFieldFactory().createOutputGroup();
			this.fieldsDatiGenerali.setId("dett_singoloPag");
			this.fieldsDatiGenerali.setColumns(2);
			this.fieldsDatiGenerali.setRendered(true);
			this.fieldsDatiGenerali.setStyleClass("beanTable"); 
			this.fieldsDatiGenerali.setColumnClasses("labelAllineataDx,valueAllineataSx");

			this.fieldsDatiGenerali.addField(this.iusv);
			this.fieldsDatiGenerali.addField(this.importoPagato);
			this.fieldsDatiGenerali.addField(this.esito);
			this.fieldsDatiGenerali.addField(this.data);
			this.fieldsDatiGenerali.addField(this.iur);

		} catch (FactoryException e) {
		}
	}

	@Override
	public void setDTO(DatiSingoloPagamento dto) { 
		super.setDTO(dto);

		this.iusv.setValue(this.getDTO().getIusv()); 
		this.importoPagato.setValue(this.getDTO().getImportoPagato());
		this.esito.setValue(this.getDTO().getEsito());
		this.iur.setValue(this.getDTO().getIur());
		if(this.getDTO().getData() != null)
			this.data.setValue(this.getDTO().getData().toGregorianCalendar().getTime());

	}

	public Text getIusv() {
		return iusv;
	}

	public void setIusv(Text iusv) {
		this.iusv = iusv;
	}

	public OutputNumber getImportoPagato() {
		return importoPagato;
	}

	public void setImportoPagato(OutputNumber importoPagato) {
		this.importoPagato = importoPagato;
	}

	public Text getEsito() {
		return esito;
	}

	public void setEsito(Text esito) {
		this.esito = esito;
	}

	public DateTime getData() {
		return data;
	}

	public void setData(DateTime data) {
		this.data = data;
	}

	public Text getIur() {
		return iur;
	}

	public void setIur(Text iur) {
		this.iur = iur;
	}

	public OutputGroup getFieldsDatiGenerali() {
		return fieldsDatiGenerali;
	}

	public void setFieldsDatiGenerali(OutputGroup fieldsDatiGenerali) {
		this.fieldsDatiGenerali = fieldsDatiGenerali;
	}

	@Override
	public String getId() {
		return this.getDTO().getIusv();
	}


}
