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

import it.govpay.ejb.core.model.PagamentoModel.EnumStatoPagamento;
import it.govpay.web.console.pagamenti.model.PagamentoModel.DettaglioPagamento;

import java.util.Date;

import org.openspcoop2.generic_project.web.bean.BaseBean;
import org.openspcoop2.generic_project.web.core.Utils;
import org.openspcoop2.generic_project.web.presentation.field.OutputDate;
import org.openspcoop2.generic_project.web.presentation.field.OutputField;
import org.openspcoop2.generic_project.web.presentation.field.OutputGroup;
import org.openspcoop2.generic_project.web.presentation.field.OutputNumber;
import org.openspcoop2.generic_project.web.presentation.field.OutputText;

public class PagamentoBean  extends BaseBean<DettaglioPagamento, String>{

	private OutputField<String> identificativoPagamento = null;
	private OutputField<Number> importoPagato = null;
	private OutputField<Number> importoDovuto = null;
	private OutputField<String> statoPagamento = null;

	private OutputField<String> causale =  null;
	private OutputField<String> annoRiferimento =  null;
	private OutputField<String> ibanAccredito = null;
	private OutputField<Date> dataPagamento = null;
	private OutputField<String> idRiscossionePSP =  null;
	
	private String htmlId = null;
	
	// Gruppo Informazioni Dati Genareli
	private OutputGroup fieldsDatiGenerali = new OutputGroup();
	
	public PagamentoBean(String id){
		this.htmlId = id;
		init();
	}
	
	public PagamentoBean(){
		this.htmlId = "pag_";
		init();
	}

	private void init() {
		this.statoPagamento = new OutputText();
		this.statoPagamento.setLabel(Utils.getMessageFromResourceBundle("distinta.pagamento.statoPagamento"));
		this.statoPagamento.setName(this.htmlId + "statoPagamento");
		
		this.identificativoPagamento = new OutputText();
		this.identificativoPagamento.setLabel(Utils.getMessageFromResourceBundle("distinta.pagamento.identificativoPagamento"));
		this.identificativoPagamento.setName(this.htmlId + "identificativoPagamento");
		
		this.idRiscossionePSP = new OutputText();
		this.idRiscossionePSP.setLabel(Utils.getMessageFromResourceBundle("distinta.pagamento.idRiscossionePSP"));
		this.idRiscossionePSP.setName(this.htmlId + "idRiscossionePSP");
		
		this.causale = new OutputText();
		this.causale.setLabel(Utils.getMessageFromResourceBundle("distinta.pagamento.causale"));
		this.causale.setName(this.htmlId + "causale");
		
		this.annoRiferimento = new OutputText();
		this.annoRiferimento.setLabel(Utils.getMessageFromResourceBundle("distinta.pagamento.annoRiferimento"));
		this.annoRiferimento.setName(this.htmlId + "annoRiferimento");
		
		this.ibanAccredito = new OutputText();
		this.ibanAccredito.setLabel(Utils.getMessageFromResourceBundle("distinta.pagamento.ibanAccredito"));
		this.ibanAccredito.setName(this.htmlId + "ibanAccredito");

		this.dataPagamento = new OutputDate();
		this.dataPagamento.setLabel(Utils.getMessageFromResourceBundle("distinta.pagamento.dataRicevuta"));
		this.dataPagamento.setName(this.htmlId + "dataPagamento");

		this.importoPagato = new OutputNumber<Double>();
		this.importoPagato.setType("valuta"); 
		this.importoPagato.setLabel(Utils.getMessageFromResourceBundle("distinta.pagamento.importoPagato"));
		this.importoPagato.setName(this.htmlId + "importoPagato");
		((OutputNumber<Number>)this.importoPagato).setConverterType(OutputNumber.CONVERT_TYPE_CURRENCY);
		((OutputNumber<Number>)this.importoPagato).setCurrencySymbol(OutputNumber.CURRENCY_SYMBOL_EURO);
		
		this.importoDovuto = new OutputNumber<Double>();
		this.importoDovuto.setType("valuta"); 
		this.importoDovuto.setLabel(Utils.getMessageFromResourceBundle("distinta.pagamento.importoDovuto"));
		this.importoDovuto.setName(this.htmlId + "importoDovuto");
		((OutputNumber<Number>)this.importoDovuto).setConverterType(OutputNumber.CONVERT_TYPE_CURRENCY);
		((OutputNumber<Number>)this.importoDovuto).setCurrencySymbol(OutputNumber.CURRENCY_SYMBOL_EURO);
		
		
		this.fieldsDatiGenerali = new OutputGroup();
		this.fieldsDatiGenerali.setIdGroup("dett_singoloPag");
		this.fieldsDatiGenerali.setColumns(2);
		this.fieldsDatiGenerali.setRendered(true);
		this.fieldsDatiGenerali.setStyleClass("beanTable"); 
		this.fieldsDatiGenerali.setColumnClasses("labelAllineataDx,valueAllineataSx");

		this.fieldsDatiGenerali.addField(this.identificativoPagamento);
		this.fieldsDatiGenerali.addField(this.importoPagato);
		this.fieldsDatiGenerali.addField(this.causale);
		this.fieldsDatiGenerali.addField(this.annoRiferimento);
		this.fieldsDatiGenerali.addField(this.ibanAccredito);
		this.fieldsDatiGenerali.addField(this.dataPagamento);
		this.fieldsDatiGenerali.addField(this.idRiscossionePSP);
	}
	
	@Override
	public void setDTO(DettaglioPagamento dto) { 
		super.setDTO(dto);
		
		this.identificativoPagamento.setValue(this.getDTO().getIdentificativo()); 
		this.importoPagato.setValue(this.getDTO().getImportoVersato());
		this.importoDovuto.setValue(this.getDTO().getImportoDovuto());
		this.statoPagamento.setValue(this.getDTO().getStato());
		this.causale.setValue(this.getDTO().getCausale());
		this.dataPagamento.setValue(this.getDTO().getDataRicevuta());
		this.annoRiferimento.setValue(this.getDTO().getAnnoRiferimento() + "");
		this.idRiscossionePSP.setValue(this.getDTO().getIur());
		this.ibanAccredito.setValue(this.getDTO().getIbanAccredito());
	}

	public static String getStatoPagamento(EnumStatoPagamento statoPag){
		if(statoPag != null){
			switch (statoPag) { 
			case AN:
				return Utils.getMessageFromResourceBundle("distinta.pagamento.statoPagamento.an");
			case AO:
				return Utils.getMessageFromResourceBundle("distinta.pagamento.statoPagamento.ao");
			case EF:
				return Utils.getMessageFromResourceBundle("distinta.pagamento.statoPagamento.ef");
			case ES:
				return Utils.getMessageFromResourceBundle("distinta.pagamento.statoPagamento.es");
			case IC:
				return Utils.getMessageFromResourceBundle("distinta.pagamento.statoPagamento.ic");
			case IE: 
				return Utils.getMessageFromResourceBundle("distinta.pagamento.statoPagamento.ie");
			case NE: 
				return Utils.getMessageFromResourceBundle("distinta.pagamento.statoPagamento.ne");
			case ST:
				return Utils.getMessageFromResourceBundle("distinta.pagamento.statoPagamento.st");
			}
		}

		return null;
	}

	public OutputField<String> getStatoPagamento() {
		return statoPagamento;
	}

	public void setStatoPagamento(OutputField<String> statoPagamento) {
		this.statoPagamento = statoPagamento;
	}

	public OutputField<Date> getDataPagamento() {
		return dataPagamento;
	}

	public void setDataPagamento(OutputField<Date> dataPagamento) {
		this.dataPagamento = dataPagamento;
	}

	public OutputField<Number> getImportoPagato() {
		return importoPagato;
	}

	public void setImportoPagato(OutputField<Number> importoPagato) {
		this.importoPagato = importoPagato;
	}

	public OutputField<Number> getImportoDovuto() {
		return importoDovuto;
	}

	public void setImportoDovuto(OutputField<Number> importoDovuto) {
		this.importoDovuto = importoDovuto;
	}

	public OutputField<String> getIdentificativoPagamento() {
		return identificativoPagamento;
	}

	public void setIdentificativoPagamento(
			OutputField<String> identificativoPagamento) {
		this.identificativoPagamento = identificativoPagamento;
	}

	public OutputField<String> getIdRiscossionePSP() {
		return idRiscossionePSP;
	}

	public void setIdRiscossionePSP(OutputField<String> idRiscossionePSP) {
		this.idRiscossionePSP = idRiscossionePSP;
	}

	public OutputGroup getFieldsDatiGenerali() {
		return fieldsDatiGenerali;
	}

	public void setFieldsDatiGenerali(OutputGroup fieldsDatiGenerali) {
		this.fieldsDatiGenerali = fieldsDatiGenerali;
	}

	public OutputField<String> getCausale() {
		return causale;
	}

	public void setCausale(OutputField<String> causale) {
		this.causale = causale;
	}

	public OutputField<String> getAnnoRiferimento() {
		return annoRiferimento;
	}

	public void setAnnoRiferimento(OutputField<String> annoRiferimento) {
		this.annoRiferimento = annoRiferimento;
	}

	public OutputField<String> getIbanAccredito() {
		return ibanAccredito;
	}

	public void setIbanAccredito(OutputField<String> ibanAccredito) {
		this.ibanAccredito = ibanAccredito;
	}
}
