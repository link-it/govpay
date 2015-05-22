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

import java.util.Date;

import org.openspcoop2.generic_project.web.bean.BaseBean;
import org.openspcoop2.generic_project.web.core.Utils;
import org.openspcoop2.generic_project.web.presentation.field.OutputDate;
import org.openspcoop2.generic_project.web.presentation.field.OutputField;
import org.openspcoop2.generic_project.web.presentation.field.OutputGroup;
import org.openspcoop2.generic_project.web.presentation.field.OutputNumber;
import org.openspcoop2.generic_project.web.presentation.field.OutputText;

public class SingoloVersamentoBean extends BaseBean<DatiSingoloPagamento, String>{


	private OutputField<String> iusv = null;
	private OutputField<Number> importoPagato = null;
	private OutputField<String> esito =  null;
	private OutputField<Date> data = null;
	private OutputField<String> iur =  null;

	// Gruppo Informazioni Dati Genareli
	private OutputGroup fieldsDatiGenerali = new OutputGroup();

	public SingoloVersamentoBean(){
		this.iusv = new OutputText();
		this.iusv.setLabel(Utils.getMessageFromResourceBundle("distinta.pagamento.iusv"));
		this.iusv.setName("pag_iusv");

		this.iur = new OutputText();
		this.iur.setLabel(Utils.getMessageFromResourceBundle("distinta.pagamento.iur"));
		this.iur.setName("pag_iur");

		this.esito = new OutputText();
		this.esito.setLabel(Utils.getMessageFromResourceBundle("distinta.pagamento.esito"));
		this.esito.setName("pag_esito");

		this.data = new OutputDate();
		this.data.setLabel(Utils.getMessageFromResourceBundle("distinta.pagamento.data"));
		this.data.setName("pag_data");

		this.importoPagato = new OutputNumber<Double>();
		this.importoPagato.setType("valuta"); 
		this.importoPagato.setLabel(Utils.getMessageFromResourceBundle("distinta.pagamento.importoPagato"));
		this.importoPagato.setName("pag_importoPagato");
		((OutputNumber<Number>)this.importoPagato).setConverterType(OutputNumber.CONVERT_TYPE_CURRENCY);
		((OutputNumber<Number>)this.importoPagato).setCurrencySymbol(OutputNumber.CURRENCY_SYMBOL_EURO);


		this.fieldsDatiGenerali = new OutputGroup();
		this.fieldsDatiGenerali.setIdGroup("dett_singoloPag");
		this.fieldsDatiGenerali.setColumns(2);
		this.fieldsDatiGenerali.setRendered(true);
		this.fieldsDatiGenerali.setStyleClass("beanTable"); 
		this.fieldsDatiGenerali.setColumnClasses("labelAllineataDx,valueAllineataSx");

		this.fieldsDatiGenerali.addField(this.iusv);
		this.fieldsDatiGenerali.addField(this.importoPagato);
		this.fieldsDatiGenerali.addField(this.esito);
		this.fieldsDatiGenerali.addField(this.data);
		this.fieldsDatiGenerali.addField(this.iur);
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

	public OutputField<String> getIusv() {
		return iusv;
	}

	public void setIusv(OutputField<String> iusv) {
		this.iusv = iusv;
	}

	public OutputField<Number> getImportoPagato() {
		return importoPagato;
	}

	public void setImportoPagato(OutputField<Number> importoPagato) {
		this.importoPagato = importoPagato;
	}

	public OutputField<String> getEsito() {
		return esito;
	}

	public void setEsito(OutputField<String> esito) {
		this.esito = esito;
	}

	public OutputField<Date> getData() {
		return data;
	}

	public void setData(OutputField<Date> data) {
		this.data = data;
	}

	public OutputField<String> getIur() {
		return iur;
	}

	public void setIur(OutputField<String> iur) {
		this.iur = iur;
	}

	public OutputGroup getFieldsDatiGenerali() {
		return fieldsDatiGenerali;
	}

	public void setFieldsDatiGenerali(OutputGroup fieldsDatiGenerali) {
		this.fieldsDatiGenerali = fieldsDatiGenerali;
	}
	
	
}
