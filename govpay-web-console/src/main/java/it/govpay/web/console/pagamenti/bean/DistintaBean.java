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

import it.govpay.ejb.model.DistintaModel;
import it.govpay.ejb.model.SoggettoModel;
import it.govpay.ejb.model.DistintaModel.EnumTipoAutenticazioneSoggetto;
import it.govpay.web.console.pagamenti.model.PagamentoModel;
import it.govpay.web.console.pagamenti.model.PagamentoModel.DettaglioPagamento;
import it.govpay.web.console.utils.Utils;

import java.io.ByteArrayInputStream;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.servlet.http.HttpServletResponse;

import org.openspcoop2.generic_project.web.bean.BaseBean;
import org.openspcoop2.generic_project.web.core.MessageUtils;
import org.openspcoop2.generic_project.web.presentation.field.OutputButton;
import org.openspcoop2.generic_project.web.presentation.field.OutputDate;
import org.openspcoop2.generic_project.web.presentation.field.OutputField;
import org.openspcoop2.generic_project.web.presentation.field.OutputGroup;
import org.openspcoop2.generic_project.web.presentation.field.OutputNumber;
import org.openspcoop2.generic_project.web.presentation.field.OutputText;

public class DistintaBean extends BaseBean<DistintaModel, Long> {  

	// Field Che visualizzo nella maschera di ricerca
	private OutputField<String> identificativoFiscaleCreditore = null; 
	private OutputField<String> iuv = null;
	private OutputField<String> codiceTributo = null;
	private OutputField<String> cfDebitore = null;
	private OutputField<Date> data = null;
	private OutputField<String> stato = null;
	private OutputField<Number> importoTotale = null; 


	private OutputField<String> gateway = null;
	private OutputField<String> creditore = null;
	private OutputField<String> tributo = null;
	private OutputField<String> tipoAutenticazioneSoggetto = null;
	private OutputField<String> ccp = null;
	private OutputField<String> tipoFirma = null;
	private OutputField<String> ibanAddebito = null;

	private OutputField<String> soggettoVersante = null;
	private OutputField<String> soggettoDebitore = null;

	private OutputField<String> idDistinta = null;

	private List<PagamentoBean> listaPagamenti = null;

	private it.govpay.web.console.pagamenti.model.PagamentoModel dettaglioPagamento = null;
	
	private PagamentoBean totale = null;

	// Link download
	private OutputField<String> rpt = null;
	private OutputField<String> rt = null;

	// Gruppo Informazioni Dati Genareli
	private OutputGroup fieldsDatiGenerali = new OutputGroup();

	@Override
	public void setDTO(DistintaModel dto) {
		super.setDTO(dto);

		// Fields Tabella Risultati
		this.idDistinta.setValue(this.getDTO().getIdDistinta() + "");
		this.identificativoFiscaleCreditore.setValue(this.getDTO().getIdentificativoFiscaleCreditore());
		this.iuv.setValue(this.getDTO().getIuv());
		this.codiceTributo.setValue(null);
		this.cfDebitore.setValue(null); 
		this.data.setValue(this.getDTO().getDataOraRichiesta());
		this.importoTotale.setValue(this.getDTO().getImportoTotale());
		if(this.getDTO().getStato() != null){
			this.stato.setValue(this.getDTO().getStato().getDescrizione());
		}
	}

	public void setDTO(it.govpay.web.console.pagamenti.model.PagamentoModel dettaglioPagamento){

		this.dettaglioPagamento = dettaglioPagamento;

		if(dettaglioPagamento.getTributo()!= null)
			this.codiceTributo.setValue(dettaglioPagamento.getTributo().getCodiceTributo());
		if(dettaglioPagamento.getDebitore()!= null)
			this.cfDebitore.setValue(dettaglioPagamento.getDebitore().getIdFiscale()); 

		if(dettaglioPagamento.getEnteCreditore() != null)
			this.creditore.setValue(dettaglioPagamento.getEnteCreditore().getDenominazione());

		if(dettaglioPagamento.getGatewayPagamento() != null)
			this.gateway.setValue(dettaglioPagamento.getGatewayPagamento().getDescrizioneGateway());

		if(dettaglioPagamento.getEnteCreditore() != null)
			this.creditore.setValue(dettaglioPagamento.getEnteCreditore().getDenominazione());

		if(dettaglioPagamento.getTributo() != null)
			this.tributo.setValue(dettaglioPagamento.getTributo().getDescrizione());
		this.iuv.setValue(dettaglioPagamento.getIuv());
		this.ccp.setValue(dettaglioPagamento.getCcp());
		this.stato.setValue(dettaglioPagamento.getStato());
		this.tipoAutenticazioneSoggetto.setValue(dettaglioPagamento.getAutenticazione());
		this.tipoFirma.setValue(dettaglioPagamento.getTipoFirma());
		this.ibanAddebito.setValue(dettaglioPagamento.getIbanAddebito());
		if(dettaglioPagamento.getDebitore() != null)
			this.soggettoDebitore.setValue(getDescrizioneSoggetto(dettaglioPagamento.getDebitore()));
		if(dettaglioPagamento.getVersante() != null)
			this.soggettoVersante.setValue(getDescrizioneSoggetto(dettaglioPagamento.getVersante()));

		if(this.dettaglioPagamento.getDettagliPagamento() != null){
			if(this.dettaglioPagamento.getDettagliPagamento().size() > 0){
				this.listaPagamenti.clear();
				BigDecimal importoDovutoTotale = BigDecimal.ZERO;
				BigDecimal importoVersatoTotale = BigDecimal.ZERO;
				for (DettaglioPagamento dettaglio : this.dettaglioPagamento.getDettagliPagamento()) {
					importoDovutoTotale = importoDovutoTotale.add(dettaglio.getImportoDovuto());
					importoVersatoTotale = importoVersatoTotale.add(dettaglio.getImportoVersato());
					PagamentoBean bean = new PagamentoBean();
					bean.setDTO(dettaglio);
					this.listaPagamenti.add(bean);
				}
				DettaglioPagamento totale = new PagamentoModel().new DettaglioPagamento();
				totale.setImportoDovuto(importoDovutoTotale);
				totale.setImportoVersato(importoVersatoTotale);
				totale.setIdentificativo(it.govpay.web.console.utils.Utils.getMessageFromResourceBundle("pagamento.dettaglio.totale.label"));
				totale.setStato(" "); 
				this.totale.setDTO(totale);
			}
		}

		prepareUrls();
	}
	
	public PagamentoBean getTotale(){
		return this.totale;
	}

	public DistintaBean(){
		initFields();
	}

	private void initFields(){

		this.idDistinta = new OutputText();
		this.idDistinta.setLabel(Utils.getMessageFromResourceBundle("distinta.idDistinta"));
		this.idDistinta.setName("idDistinta");

		this.codiceTributo = new OutputText();
		this.codiceTributo.setLabel(Utils.getMessageFromResourceBundle("distinta.codiceTributo"));
		this.codiceTributo.setName("codiceTributo");

		this.cfDebitore = new OutputText();
		this.cfDebitore.setLabel(Utils.getMessageFromResourceBundle("distinta.cfDebitore"));
		this.cfDebitore.setName("cfDebitore");

		this.soggettoVersante = new OutputText();
		this.soggettoVersante.setLabel(Utils.getMessageFromResourceBundle("distinta.soggettoVersante"));
		this.soggettoVersante.setName("soggettoVersante");
		this.soggettoVersante.setEscape(false);

		this.stato = new OutputText();
		this.stato.setLabel(Utils.getMessageFromResourceBundle("distinta.stato"));
		this.stato.setName("stato");

		this.creditore = new OutputText();
		this.creditore.setLabel(Utils.getMessageFromResourceBundle("distinta.creditore"));
		this.creditore.setName("creditore");

		this.data = new OutputDate();
		this.data.setLabel(Utils.getMessageFromResourceBundle("distinta.data"));
		this.data.setName("data");

		this.importoTotale = new OutputNumber<Double>();
		this.importoTotale.setType("valuta"); 
		this.importoTotale.setLabel(Utils.getMessageFromResourceBundle("distinta.importoDovuto"));
		this.importoTotale.setName("importoTotale");
		((OutputNumber<Number>)this.importoTotale).setConverterType(OutputNumber.CONVERT_TYPE_CURRENCY);
		((OutputNumber<Number>)this.importoTotale).setCurrencySymbol(OutputNumber.CURRENCY_SYMBOL_EURO);

		this.ibanAddebito = new OutputText();
		this.ibanAddebito.setLabel(Utils.getMessageFromResourceBundle("distinta.ibanAddebito"));
		this.ibanAddebito.setName("ibanAddebito");

		this.tipoAutenticazioneSoggetto = new OutputText();
		this.tipoAutenticazioneSoggetto.setLabel(Utils.getMessageFromResourceBundle("distinta.tipoAutenticazioneSoggetto"));
		this.tipoAutenticazioneSoggetto.setName("tipoAutenticazioneSoggetto");

		this.gateway = new OutputText();
		this.gateway.setLabel(Utils.getMessageFromResourceBundle("distinta.gateway"));
		this.gateway.setName("gateway");

		this.iuv = new OutputText();
		this.iuv.setLabel(Utils.getMessageFromResourceBundle("distinta.iuv"));
		this.iuv.setName("iuv");

		this.identificativoFiscaleCreditore = new OutputText();
		this.identificativoFiscaleCreditore.setLabel(Utils.getMessageFromResourceBundle("distinta.identificativoFiscaleCreditore"));
		this.identificativoFiscaleCreditore.setName("identificativoFiscaleCreditore");

		this.tributo = new OutputText();
		this.tributo.setLabel(Utils.getMessageFromResourceBundle("distinta.tributo"));
		this.tributo.setName("tributo");


		this.ccp = new OutputText();
		this.ccp.setLabel(Utils.getMessageFromResourceBundle("distinta.ccp"));
		this.ccp.setName("ccp");

		this.tipoFirma = new OutputText();
		this.tipoFirma.setLabel(Utils.getMessageFromResourceBundle("distinta.tipoFirma"));
		this.tipoFirma.setName("tipoFirma");

		this.soggettoDebitore = new OutputText();
		this.soggettoDebitore.setLabel(Utils.getMessageFromResourceBundle("distinta.soggettoDebitore"));
		this.soggettoDebitore.setName("soggettoDebitore");
		this.soggettoDebitore.setEscape(false);

		this.fieldsDatiGenerali = new OutputGroup();
		this.fieldsDatiGenerali.setIdGroup("datiGenerali");
		this.fieldsDatiGenerali.setColumns(2);
		this.fieldsDatiGenerali.setRendered(true);
		this.fieldsDatiGenerali.setStyleClass("beanTable"); 
		this.fieldsDatiGenerali.setColumnClasses("labelAllineataDx,valueAllineataSx");

		this.fieldsDatiGenerali.addField(this.gateway);
		this.fieldsDatiGenerali.addField(this.creditore);
		this.fieldsDatiGenerali.addField(this.tributo);
		this.fieldsDatiGenerali.addField(this.iuv);
		this.fieldsDatiGenerali.addField(this.ccp);
		this.fieldsDatiGenerali.addField(this.stato);
		this.fieldsDatiGenerali.addField(this.tipoAutenticazioneSoggetto);
		this.fieldsDatiGenerali.addField(this.tipoFirma);
		this.fieldsDatiGenerali.addField(this.ibanAddebito);
		this.fieldsDatiGenerali.addField(this.soggettoDebitore);
		this.fieldsDatiGenerali.addField(this.soggettoVersante);


		this.listaPagamenti = new ArrayList<PagamentoBean>();

		this.rpt = new OutputButton();
		this.rpt.setLabel( ("distinta.link.rpt"));
		this.rpt.setName("rpt");
		((OutputButton) this.rpt).setIcon("download.png");
		((OutputButton) this.rpt).setIconTitle( ("distinta.link.rpt.iconTitle"));

		this.rt = new OutputButton();
		this.rt.setLabel( ("distinta.link.rt"));
		this.rt.setName("rt");
		((OutputButton) this.rt).setIcon("download.png");
		((OutputButton) this.rt).setIconTitle( ("distinta.link.rt.iconTitle"));
		
		this.totale = new PagamentoBean("tot_");
		DettaglioPagamento totale = new PagamentoModel().new DettaglioPagamento();
		totale.setImportoDovuto(BigDecimal.ZERO);
		totale.setImportoVersato(BigDecimal.ZERO);
		totale.setIdentificativo(it.govpay.web.console.utils.Utils.getMessageFromResourceBundle("pagamento.dettaglio.totale.label"));
		totale.setStato(" "); 
		this.totale.setDTO(totale); 
	}

	private void prepareUrls(){
		((OutputButton) this.rpt).setLink(this.dettaglioPagamento.getRpt() != null ?  "downloadLink" : null);
		((OutputButton) this.rt).setLink( this.dettaglioPagamento.getRt() != null ? "downloadLink" : null);
	}

	public OutputField<String> getSoggettoVersante() {
		return this.soggettoVersante;
	}

	public void setSoggettoVersante(OutputField<String> soggettoVersante) {
		this.soggettoVersante = soggettoVersante;
	}

	public OutputField<String> getStato() {
		return this.stato;
	}

	public void setStato(OutputField<String> stato) {
		this.stato = stato;
	}

	public OutputField<Date> getData() {
		return this.data;
	}

	public void setData(OutputField<Date> data) {
		this.data = data;
	}

	public OutputField<Number> getImportoTotale() {
		return this.importoTotale;
	}

	public void setImportoTotale(OutputField<Number> importoTotale) {
		this.importoTotale = importoTotale;
	}

	public OutputGroup getFieldsDatiGenerali() {
		return this.fieldsDatiGenerali;
	}

	public void setFieldsDatiGenerali(OutputGroup fieldsDatiGenerali) {
		this.fieldsDatiGenerali = fieldsDatiGenerali;
	}

	public List<PagamentoBean> getListaPagamenti() {
		return this.listaPagamenti;
	}

	public void setListaPagamenti(List<PagamentoBean> listaPagamenti) {
		this.listaPagamenti = listaPagamenti;
	}

	public OutputField<String> getIbanAddebito() {
		return ibanAddebito;
	}

	public void setIbanAddebito(OutputField<String> ibanAddebito) {
		this.ibanAddebito = ibanAddebito;
	}

	public OutputField<String> getTipoAutenticazioneSoggetto() {
		return tipoAutenticazioneSoggetto;
	}

	public void setTipoAutenticazioneSoggetto(
			OutputField<String> tipoAutenticazioneSoggetto) {
		this.tipoAutenticazioneSoggetto = tipoAutenticazioneSoggetto;
	}

	public OutputField<String> getIuv() {
		return iuv;
	}

	public void setIuv(OutputField<String> iuv) {
		this.iuv = iuv;
	}

	public OutputField<String> getIdentificativoFiscaleCreditore() {
		return identificativoFiscaleCreditore;
	}

	public void setIdentificativoFiscaleCreditore(
			OutputField<String> identificativoFiscaleCreditore) {
		this.identificativoFiscaleCreditore = identificativoFiscaleCreditore;
	}


	public OutputField<String> getIdDistinta() {
		return idDistinta;
	}

	public void setIdDistinta(OutputField<String> idDistinta) {
		this.idDistinta = idDistinta;
	}

	public static String getTipoAutenticazione (EnumTipoAutenticazioneSoggetto tipoAuth){
		if(tipoAuth != null)
			return tipoAuth.toString();

		return null;
	}

	public OutputField<String> getCodiceTributo() {
		return codiceTributo;
	}

	public void setCodiceTributo(OutputField<String> codiceTributo) {
		this.codiceTributo = codiceTributo;
	}

	public OutputField<String> getCfDebitore() {
		return cfDebitore;
	}

	public void setCfDebitore(OutputField<String> cfDebitore) {
		this.cfDebitore = cfDebitore;
	}

	public OutputField<String> getGateway() {
		return gateway;
	}

	public void setGateway(OutputField<String> gateway) {
		this.gateway = gateway;
	}

	public OutputField<String> getCreditore() {
		return creditore;
	}

	public void setCreditore(OutputField<String> creditore) {
		this.creditore = creditore;
	}

	public OutputField<String> getTributo() {
		return tributo;
	}

	public void setTributo(OutputField<String> tributo) {
		this.tributo = tributo;
	}

	public OutputField<String> getCcp() {
		return ccp;
	}

	public void setCcp(OutputField<String> ccp) {
		this.ccp = ccp;
	}

	public OutputField<String> getTipoFirma() {
		return tipoFirma;
	}

	public void setTipoFirma(OutputField<String> tipoFirma) {
		this.tipoFirma = tipoFirma;
	}

	public OutputField<String> getSoggettoDebitore() {
		return soggettoDebitore;
	}

	public void setSoggettoDebitore(OutputField<String> soggettoDebitore) {
		this.soggettoDebitore = soggettoDebitore;
	}

	public it.govpay.web.console.pagamenti.model.PagamentoModel getDettaglioPagamento() {
		return dettaglioPagamento;
	}

	public void setDettaglioPagamento(
			it.govpay.web.console.pagamenti.model.PagamentoModel dettaglioPagamento) {
		this.dettaglioPagamento = dettaglioPagamento;
	}

	public OutputField<String> getRpt() {
		return rpt;
	}

	public void setRpt(OutputField<String> rpt) {
		this.rpt = rpt;
	}

	public OutputField<String> getRt() {
		return rt;
	}

	public void setRt(OutputField<String> rt) {
		this.rt = rt;
	}

	public String downloadRpt() {


		FacesContext fc = FacesContext.getCurrentInstance();
		ExternalContext ec = fc.getExternalContext();

		ec.responseReset(); 
		ec.setResponseContentType("text/plain");
		ec.setResponseHeader("Content-Disposition", "attachment; filename=\"rpt.xml\""); // The Save As popup magic is done here. You can give it any file name you want, this only won't work in MSIE, it will use current request URL as file name instead.
		ec.addResponseHeader("Cache-Control", "no-cache");
		ec.setResponseStatus(200);
		ec.setResponseBufferSize(1024);
		// committing status and headers
		try {
			ByteArrayInputStream bais = new ByteArrayInputStream(this.dettaglioPagamento.getRpt());

			Utils.copy(bais, ec.getResponseOutputStream());
			ec.responseFlushBuffer();


		} catch (Exception e1) {

			MessageUtils.addErrorMsg(null,"Si e' verificato un errore durante il download dell RPT: " + e1);
			//	return null;
		}


		fc.responseComplete(); // Important! Otherwise JSF will attempt to render the response which obviously will fail since it's already written with a file and closed.
		return null;
	}

	public String downloadRt() {


		FacesContext fc = FacesContext.getCurrentInstance();
		//		ExternalContext ec = fc.getExternalContext();
		//		ec.responseReset();
		HttpServletResponse response = (HttpServletResponse) fc.getExternalContext().getResponse();

		response.reset(); // Some JSF component library or some Filter might have set some headers in the buffer beforehand. We want to get rid of them, else it may collide.
		//	    response.setContentType("text/csv"); // Check http://www.w3schools.com/media/media_mimeref.asp for all types. Use if necessary ServletContext#getMimeType() for auto-detection based on filename.
		//response.setContentLength(contentLength); // Set it with the file size. This header is optional. It will work if it's omitted, but the download progress will be unknown.
		response.setContentType("text/plain");					
		response.setHeader("Content-Disposition", "attachment; rt.txt");
		response.addHeader("Cache-Control", "no-cache");
		response.setStatus(200);
		response.setBufferSize(1024);
		// committing status and headers
		try {
			ByteArrayInputStream bais = new ByteArrayInputStream(this.dettaglioPagamento.getRt());

			Utils.copy(bais, response.getOutputStream());
			response.flushBuffer();

		} catch (Exception e1) {

			MessageUtils.addErrorMsg(null,"Si e' verificato un errore durante il download dell RT: " + e1);
			//	return null;
		}


		fc.responseComplete(); // Important! Otherwise JSF will attempt to render the response which obviously will fail since it's already written with a file and closed.
		return null;
	}

	private String getDescrizioneSoggetto(SoggettoModel sog){
		StringBuilder sb = new StringBuilder();

		if(sog != null ){
			sb.append(sog.getAnagrafica()).append(" - ").append(sog.getIdFiscale()).append("<br/>");
			if(sog.getIndirizzo() != null){
				sb.append(sog.getIndirizzo());
				if(sog.getCivico() != null)
					sb.append(" - ").append(sog.getCivico()).append("<br/>");
				else 
					sb.append("<br/>");
			}

			if(sog.getCap() != null && sog.getLocalita() != null && sog.getProvincia() != null && sog.getNazione() != null )
				sb.append(sog.getCap()).append(" ").append(sog.getLocalita()).append(" - ").append(sog.getProvincia())
				.append(" - ").append(sog.getNazione()).append("<br/>");

			if(sog.geteMail() != null)
				sb.append(sog.geteMail());
		}

		return sb.toString();
	}
}
