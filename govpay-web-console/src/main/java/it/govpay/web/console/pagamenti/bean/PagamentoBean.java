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
import it.govpay.web.console.utils.Utils;

import org.openspcoop2.generic_project.web.bean.IBean;
import org.openspcoop2.generic_project.web.factory.Costanti;
import org.openspcoop2.generic_project.web.factory.FactoryException;
import org.openspcoop2.generic_project.web.impl.jsf1.bean.BaseBean;
import org.openspcoop2.generic_project.web.output.DateTime;
import org.openspcoop2.generic_project.web.output.OutputGroup;
import org.openspcoop2.generic_project.web.output.OutputNumber;
import org.openspcoop2.generic_project.web.output.Text;

public class PagamentoBean  extends BaseBean<DettaglioPagamento, String> implements IBean<DettaglioPagamento, String>{ 

	private Text identificativoPagamento = null;
	private OutputNumber importoPagato = null;
	private OutputNumber importoDovuto = null;
	private Text statoPagamento = null;

	private Text causale =  null;
	private Text annoRiferimento =  null;
	private Text ibanAccredito = null;
	private DateTime dataPagamento = null;
	private Text idRiscossionePSP =  null;
	
	private String htmlId = null;
	
	// Gruppo Informazioni Dati Genareli
	private OutputGroup fieldsDatiGenerali = null;
	
	public PagamentoBean(String id){
		this.htmlId = id;
		try {
			init();
		} catch (FactoryException e) {
		}
	}
	
	public PagamentoBean(){
		this.htmlId = "pag_";
		try {
			init();
		} catch (FactoryException e) {
		}
	}

	private void init() throws FactoryException {
		this.statoPagamento = this.getWebGenericProjectFactory().getOutputFieldFactory().createText();
		this.statoPagamento.setLabel(Utils.getInstance().getMessageFromResourceBundle("distinta.pagamento.statoPagamento"));
		this.statoPagamento.setName(this.htmlId + "statoPagamento");
		
		this.identificativoPagamento = this.getWebGenericProjectFactory().getOutputFieldFactory().createText();
		this.identificativoPagamento.setLabel(Utils.getInstance().getMessageFromResourceBundle("distinta.pagamento.identificativoPagamento"));
		this.identificativoPagamento.setName(this.htmlId + "identificativoPagamento");
		
		this.idRiscossionePSP = this.getWebGenericProjectFactory().getOutputFieldFactory().createText();
		this.idRiscossionePSP.setLabel(Utils.getInstance().getMessageFromResourceBundle("distinta.pagamento.idRiscossionePSP"));
		this.idRiscossionePSP.setName(this.htmlId + "idRiscossionePSP");
		
		this.causale = this.getWebGenericProjectFactory().getOutputFieldFactory().createText();
		this.causale.setLabel(Utils.getInstance().getMessageFromResourceBundle("distinta.pagamento.causale"));
		this.causale.setName(this.htmlId + "causale");
		
		this.annoRiferimento = this.getWebGenericProjectFactory().getOutputFieldFactory().createText();
		this.annoRiferimento.setLabel(Utils.getInstance().getMessageFromResourceBundle("distinta.pagamento.annoRiferimento"));
		this.annoRiferimento.setName(this.htmlId + "annoRiferimento");
		
		this.ibanAccredito = this.getWebGenericProjectFactory().getOutputFieldFactory().createText();
		this.ibanAccredito.setLabel(Utils.getInstance().getMessageFromResourceBundle("distinta.pagamento.ibanAccredito"));
		this.ibanAccredito.setName(this.htmlId + "ibanAccredito");

		this.dataPagamento = this.getWebGenericProjectFactory().getOutputFieldFactory().createDateTime();
		this.dataPagamento.setLabel(Utils.getInstance().getMessageFromResourceBundle("distinta.pagamento.dataRicevuta"));
		this.dataPagamento.setName(this.htmlId + "dataPagamento");

		this.importoPagato = this.getWebGenericProjectFactory().getOutputFieldFactory().createNumber();
		this.importoPagato.setLabel(Utils.getInstance().getMessageFromResourceBundle("distinta.pagamento.importoPagato"));
		this.importoPagato.setName(this.htmlId + "importoPagato");
		this.importoPagato.setConverterType(Costanti.CONVERT_TYPE_CURRENCY);
		this.importoPagato.setCurrencySymbol(Costanti.CURRENCY_SYMBOL_EURO);
		
		this.importoDovuto = this.getWebGenericProjectFactory().getOutputFieldFactory().createNumber();
		this.importoDovuto.setLabel(Utils.getInstance().getMessageFromResourceBundle("distinta.pagamento.importoDovuto"));
		this.importoDovuto.setName(this.htmlId + "importoDovuto");
		this.importoDovuto.setConverterType(Costanti.CONVERT_TYPE_CURRENCY);
		this.importoDovuto.setCurrencySymbol(Costanti.CURRENCY_SYMBOL_EURO);
		
		
		this.fieldsDatiGenerali = this.getWebGenericProjectFactory().getOutputFieldFactory().createOutputGroup();
		this.fieldsDatiGenerali.setId("dett_singoloPag");
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
				return Utils.getInstance().getMessageFromResourceBundle("distinta.pagamento.statoPagamento.an");
			case AO:
				return Utils.getInstance().getMessageFromResourceBundle("distinta.pagamento.statoPagamento.ao");
			case EF:
				return Utils.getInstance().getMessageFromResourceBundle("distinta.pagamento.statoPagamento.ef");
			case ES:
				return Utils.getInstance().getMessageFromResourceBundle("distinta.pagamento.statoPagamento.es");
			case IC:
				return Utils.getInstance().getMessageFromResourceBundle("distinta.pagamento.statoPagamento.ic");
			case IE: 
				return Utils.getInstance().getMessageFromResourceBundle("distinta.pagamento.statoPagamento.ie");
			case NE: 
				return Utils.getInstance().getMessageFromResourceBundle("distinta.pagamento.statoPagamento.ne");
			case ST:
				return Utils.getInstance().getMessageFromResourceBundle("distinta.pagamento.statoPagamento.st");
			}
		}

		return null;
	}

	public Text getStatoPagamento() {
		return statoPagamento;
	}

	public void setStatoPagamento(Text statoPagamento) {
		this.statoPagamento = statoPagamento;
	}

	public DateTime getDataPagamento() {
		return dataPagamento;
	}

	public void setDataPagamento(DateTime dataPagamento) {
		this.dataPagamento = dataPagamento;
	}

	public OutputNumber getImportoPagato() {
		return importoPagato;
	}

	public void setImportoPagato(OutputNumber importoPagato) {
		this.importoPagato = importoPagato;
	}

	public OutputNumber getImportoDovuto() {
		return importoDovuto;
	}

	public void setImportoDovuto(OutputNumber importoDovuto) {
		this.importoDovuto = importoDovuto;
	}

	public Text getIdentificativoPagamento() {
		return identificativoPagamento;
	}

	public void setIdentificativoPagamento(
			Text identificativoPagamento) {
		this.identificativoPagamento = identificativoPagamento;
	}

	public Text getIdRiscossionePSP() {
		return idRiscossionePSP;
	}

	public void setIdRiscossionePSP(Text idRiscossionePSP) {
		this.idRiscossionePSP = idRiscossionePSP;
	}

	public OutputGroup getFieldsDatiGenerali() {
		return fieldsDatiGenerali;
	}

	public void setFieldsDatiGenerali(OutputGroup fieldsDatiGenerali) {
		this.fieldsDatiGenerali = fieldsDatiGenerali;
	}

	public Text getCausale() {
		return causale;
	}

	public void setCausale(Text causale) {
		this.causale = causale;
	}

	public Text getAnnoRiferimento() {
		return annoRiferimento;
	}

	public void setAnnoRiferimento(Text annoRiferimento) {
		this.annoRiferimento = annoRiferimento;
	}

	public Text getIbanAccredito() {
		return ibanAccredito;
	}

	public void setIbanAccredito(Text ibanAccredito) {
		this.ibanAccredito = ibanAccredito;
	}

	@Override
	public String getId() {
		return this.getDTO().getIur();
	}
}
