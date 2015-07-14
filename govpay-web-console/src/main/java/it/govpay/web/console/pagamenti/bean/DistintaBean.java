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

import it.govpay.ejb.core.model.DistintaModel;
import it.govpay.ejb.core.model.DistintaModel.EnumTipoAutenticazioneSoggetto;
import it.govpay.ejb.core.model.SoggettoModel;
import it.govpay.web.console.pagamenti.model.PagamentoModel;
import it.govpay.web.console.pagamenti.model.PagamentoModel.DettaglioPagamento;
import it.govpay.web.console.utils.Utils;

import java.io.ByteArrayInputStream;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.servlet.http.HttpServletResponse;

import org.openspcoop2.generic_project.web.bean.IBean;
import org.openspcoop2.generic_project.web.factory.Costanti;
import org.openspcoop2.generic_project.web.factory.FactoryException;
import org.openspcoop2.generic_project.web.impl.jsf1.bean.BaseBean;
import org.openspcoop2.generic_project.web.impl.jsf1.utils.MessageUtils;
import org.openspcoop2.generic_project.web.output.Button;
import org.openspcoop2.generic_project.web.output.DateTime;
import org.openspcoop2.generic_project.web.output.OutputGroup;
import org.openspcoop2.generic_project.web.output.OutputNumber;
import org.openspcoop2.generic_project.web.output.Text;

public class DistintaBean extends BaseBean<DistintaModel, Long> implements IBean<DistintaModel, Long> {   

	// Field Che visualizzo nella maschera di ricerca
	private Text identificativoFiscaleCreditore = null; 
	private Text iuv = null;
	private Text codiceTributo = null;
	private Text cfDebitore = null;
	private DateTime data = null;
	private Text stato = null;
	private OutputNumber importoTotale = null; 


	private Text gateway = null;
	private Text creditore = null;
	private Text tributo = null;
	private Text tipoAutenticazioneSoggetto = null;
	private Text ccp = null;
	private Text tipoFirma = null;
	private Text ibanAddebito = null;

	private Text soggettoVersante = null;
	private Text soggettoDebitore = null;

	private Text idDistinta = null;

	private List<PagamentoBean> listaPagamenti = null;

	private it.govpay.web.console.pagamenti.model.PagamentoModel dettaglioPagamento = null;
	
	private PagamentoBean totale = null;

	// Link download
	private Button rpt = null;
	private Button rt = null;

	// Gruppo Informazioni Dati Genareli
	private OutputGroup fieldsDatiGenerali = null;

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
	
	@Override
	public Long getId() {
		 return this.getDTO().getIdDistinta();
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
				totale.setIdentificativo(it.govpay.web.console.utils.Utils.getInstance().getMessageFromResourceBundle("pagamento.dettaglio.totale.label"));
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
		try {
			initFields();
		} catch (FactoryException e) {
		}
	}

	private void initFields() throws FactoryException{ 

		this.idDistinta = this.getWebGenericProjectFactory().getOutputFieldFactory().createText();
		this.idDistinta.setLabel(Utils.getInstance().getMessageFromResourceBundle("distinta.idDistinta"));
		this.idDistinta.setName("idDistinta");

		this.codiceTributo = this.getWebGenericProjectFactory().getOutputFieldFactory().createText();
		this.codiceTributo.setLabel(Utils.getInstance().getMessageFromResourceBundle("distinta.codiceTributo"));
		this.codiceTributo.setName("codiceTributo");

		this.cfDebitore = this.getWebGenericProjectFactory().getOutputFieldFactory().createText();
		this.cfDebitore.setLabel(Utils.getInstance().getMessageFromResourceBundle("distinta.cfDebitore"));
		this.cfDebitore.setName("cfDebitore");

		this.soggettoVersante = this.getWebGenericProjectFactory().getOutputFieldFactory().createText();
		this.soggettoVersante.setLabel(Utils.getInstance().getMessageFromResourceBundle("distinta.soggettoVersante"));
		this.soggettoVersante.setName("soggettoVersante");
		this.soggettoVersante.setEscape(false);

		this.stato = this.getWebGenericProjectFactory().getOutputFieldFactory().createText();
		this.stato.setLabel(Utils.getInstance().getMessageFromResourceBundle("distinta.stato"));
		this.stato.setName("stato");

		this.creditore = this.getWebGenericProjectFactory().getOutputFieldFactory().createText();
		this.creditore.setLabel(Utils.getInstance().getMessageFromResourceBundle("distinta.creditore"));
		this.creditore.setName("creditore");

		this.data = this.getWebGenericProjectFactory().getOutputFieldFactory().createDateTime();
		this.data.setLabel(Utils.getInstance().getMessageFromResourceBundle("distinta.data"));
		this.data.setName("data");

		this.importoTotale = this.getWebGenericProjectFactory().getOutputFieldFactory().createNumber();
		this.importoTotale.setLabel(Utils.getInstance().getMessageFromResourceBundle("distinta.importoDovuto"));
		this.importoTotale.setName("importoTotale");
		this.importoTotale.setConverterType(Costanti.CONVERT_TYPE_CURRENCY);
		this.importoTotale.setCurrencySymbol(Costanti.CURRENCY_SYMBOL_EURO);

		this.ibanAddebito = this.getWebGenericProjectFactory().getOutputFieldFactory().createText();
		this.ibanAddebito.setLabel(Utils.getInstance().getMessageFromResourceBundle("distinta.ibanAddebito"));
		this.ibanAddebito.setName("ibanAddebito");

		this.tipoAutenticazioneSoggetto = this.getWebGenericProjectFactory().getOutputFieldFactory().createText();
		this.tipoAutenticazioneSoggetto.setLabel(Utils.getInstance().getMessageFromResourceBundle("distinta.tipoAutenticazioneSoggetto"));
		this.tipoAutenticazioneSoggetto.setName("tipoAutenticazioneSoggetto");

		this.gateway = this.getWebGenericProjectFactory().getOutputFieldFactory().createText();
		this.gateway.setLabel(Utils.getInstance().getMessageFromResourceBundle("distinta.gateway"));
		this.gateway.setName("gateway");

		this.iuv = this.getWebGenericProjectFactory().getOutputFieldFactory().createText();
		this.iuv.setLabel(Utils.getInstance().getMessageFromResourceBundle("distinta.iuv"));
		this.iuv.setName("iuv");

		this.identificativoFiscaleCreditore = this.getWebGenericProjectFactory().getOutputFieldFactory().createText();
		this.identificativoFiscaleCreditore.setLabel(Utils.getInstance().getMessageFromResourceBundle("distinta.identificativoFiscaleCreditore"));
		this.identificativoFiscaleCreditore.setName("identificativoFiscaleCreditore");

		this.tributo = this.getWebGenericProjectFactory().getOutputFieldFactory().createText();
		this.tributo.setLabel(Utils.getInstance().getMessageFromResourceBundle("distinta.tributo"));
		this.tributo.setName("tributo");


		this.ccp = this.getWebGenericProjectFactory().getOutputFieldFactory().createText();
		this.ccp.setLabel(Utils.getInstance().getMessageFromResourceBundle("distinta.ccp"));
		this.ccp.setName("ccp");

		this.tipoFirma = this.getWebGenericProjectFactory().getOutputFieldFactory().createText();
		this.tipoFirma.setLabel(Utils.getInstance().getMessageFromResourceBundle("distinta.tipoFirma"));
		this.tipoFirma.setName("tipoFirma");

		this.soggettoDebitore = this.getWebGenericProjectFactory().getOutputFieldFactory().createText();
		this.soggettoDebitore.setLabel(Utils.getInstance().getMessageFromResourceBundle("distinta.soggettoDebitore"));
		this.soggettoDebitore.setName("soggettoDebitore");
		this.soggettoDebitore.setEscape(false);

		this.fieldsDatiGenerali = this.getWebGenericProjectFactory().getOutputFieldFactory().createOutputGroup();
		this.fieldsDatiGenerali.setId("datiGenerali");
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

		this.rpt = this.getWebGenericProjectFactory().getOutputFieldFactory().createButton();
		this.rpt.setLabel( ("distinta.link.rpt"));
		this.rpt.setName("rpt");
		this.rpt.setImage("download.png");
		this.rpt.setTitle( ("distinta.link.rpt.iconTitle"));

		this.rt = this.getWebGenericProjectFactory().getOutputFieldFactory().createButton();
		this.rt.setLabel( ("distinta.link.rt"));
		this.rt.setName("rt");
		this.rt.setImage("download.png");
		this.rt.setTitle( ("distinta.link.rt.iconTitle"));
		
		this.totale = new PagamentoBean("tot_");
		DettaglioPagamento totale = new PagamentoModel().new DettaglioPagamento();
		totale.setImportoDovuto(BigDecimal.ZERO);
		totale.setImportoVersato(BigDecimal.ZERO);
		totale.setIdentificativo(Utils.getInstance().getMessageFromResourceBundle("pagamento.dettaglio.totale.label"));
		totale.setStato(" "); 
		this.totale.setDTO(totale); 
	}

	private void prepareUrls(){
		this.rpt.setHref(this.dettaglioPagamento.getRpt() != null ?  "downloadLink" : null);
		this.rt.setHref( this.dettaglioPagamento.getRt() != null ? "downloadLink" : null);
	}

	public Text getSoggettoVersante() {
		return this.soggettoVersante;
	}

	public void setSoggettoVersante(Text soggettoVersante) {
		this.soggettoVersante = soggettoVersante;
	}

	public Text getStato() {
		return this.stato;
	}

	public void setStato(Text stato) {
		this.stato = stato;
	}

	public DateTime getData() {
		return this.data;
	}

	public void setData(DateTime data) {
		this.data = data;
	}

	public OutputNumber getImportoTotale() {
		return this.importoTotale;
	}

	public void setImportoTotale(OutputNumber importoTotale) {
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

	public Text getIbanAddebito() {
		return ibanAddebito;
	}

	public void setIbanAddebito(Text ibanAddebito) {
		this.ibanAddebito = ibanAddebito;
	}

	public Text getTipoAutenticazioneSoggetto() {
		return tipoAutenticazioneSoggetto;
	}

	public void setTipoAutenticazioneSoggetto(
			Text tipoAutenticazioneSoggetto) {
		this.tipoAutenticazioneSoggetto = tipoAutenticazioneSoggetto;
	}

	public Text getIuv() {
		return iuv;
	}

	public void setIuv(Text iuv) {
		this.iuv = iuv;
	}

	public Text getIdentificativoFiscaleCreditore() {
		return identificativoFiscaleCreditore;
	}

	public void setIdentificativoFiscaleCreditore(
			Text identificativoFiscaleCreditore) {
		this.identificativoFiscaleCreditore = identificativoFiscaleCreditore;
	}


	public Text getIdDistinta() {
		return idDistinta;
	}

	public void setIdDistinta(Text idDistinta) {
		this.idDistinta = idDistinta;
	}

	public static String getTipoAutenticazione (EnumTipoAutenticazioneSoggetto tipoAuth){
		if(tipoAuth != null)
			return tipoAuth.toString();

		return null;
	}

	public Text getCodiceTributo() {
		return codiceTributo;
	}

	public void setCodiceTributo(Text codiceTributo) {
		this.codiceTributo = codiceTributo;
	}

	public Text getCfDebitore() {
		return cfDebitore;
	}

	public void setCfDebitore(Text cfDebitore) {
		this.cfDebitore = cfDebitore;
	}

	public Text getGateway() {
		return gateway;
	}

	public void setGateway(Text gateway) {
		this.gateway = gateway;
	}

	public Text getCreditore() {
		return creditore;
	}

	public void setCreditore(Text creditore) {
		this.creditore = creditore;
	}

	public Text getTributo() {
		return tributo;
	}

	public void setTributo(Text tributo) {
		this.tributo = tributo;
	}

	public Text getCcp() {
		return ccp;
	}

	public void setCcp(Text ccp) {
		this.ccp = ccp;
	}

	public Text getTipoFirma() {
		return tipoFirma;
	}

	public void setTipoFirma(Text tipoFirma) {
		this.tipoFirma = tipoFirma;
	}

	public Text getSoggettoDebitore() {
		return soggettoDebitore;
	}

	public void setSoggettoDebitore(Text soggettoDebitore) {
		this.soggettoDebitore = soggettoDebitore;
	}

	public it.govpay.web.console.pagamenti.model.PagamentoModel getDettaglioPagamento() {
		return dettaglioPagamento;
	}

	public void setDettaglioPagamento(
			it.govpay.web.console.pagamenti.model.PagamentoModel dettaglioPagamento) {
		this.dettaglioPagamento = dettaglioPagamento;
	}

	public Button getRpt() {
		return rpt;
	}

	public void setRpt(Button rpt) {
		this.rpt = rpt;
	}

	public Button getRt() {
		return rt;
	}

	public void setRt(Button rt) {
		this.rt = rt;
	}

	public String downloadRpt() {


		FacesContext fc = FacesContext.getCurrentInstance();
		ExternalContext ec = fc.getExternalContext();

		ec.responseReset(); 
		ec.setResponseContentType("text/plain");
		String filename = "rpt_" + this.getCreditore().getValue() + "_" + this.getIuv().getValue() + ".xml";
		ec.setResponseHeader("Content-Disposition", "attachment; filename=\""+filename +"\""); // The Save As popup magic is done here. You can give it any file name you want, this only won't work in MSIE, it will use current request URL as file name instead.
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
		String filename = "rt_" + this.getCreditore().getValue() + "_" + this.getIuv().getValue() + ".xml";
		response.setHeader("Content-Disposition", "attachment; filename=\""+filename +"\"");
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
