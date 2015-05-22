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

import it.govpay.web.console.pagamenti.mbean.DistintaMBean;
import it.govpay.web.console.utils.Utils;

import java.io.Serializable;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import javax.enterprise.context.SessionScoped;
import javax.faces.event.ActionEvent;
import javax.inject.Named;

import org.openspcoop2.generic_project.web.form.BaseSearchForm;
import org.openspcoop2.generic_project.web.form.field.DateField;
import org.openspcoop2.generic_project.web.form.field.FormField;
import org.openspcoop2.generic_project.web.form.field.SelectItem;
import org.openspcoop2.generic_project.web.form.field.SelectListField;
import org.openspcoop2.generic_project.web.form.field.TextField;

@Named("distintaSearchForm") @SessionScoped
public class DistintaSearchForm extends BaseSearchForm implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	public static final String DATA_PERIODO_PERSONALIZZATO = "3";
	public static final String DATA_PERIODO_ULTIMI_TRE_MESI = "2";
	public static final String DATA_PERIODO_ULTIMO_MESE = "1";
	public static final String DATA_PERIODO_ULTIMA_SETTIMANA = "0";
	private FormField<SelectItem> dataPeriodo = null;
	private FormField<Date> data = null;
	private FormField<SelectItem> statoDistinta = null;
	private FormField<String> cfVersanteODebitore = null;
	private FormField<String> cfEnteCreditore = null;
	
	// lista degli id degli enti che vengono gestiti dall'operatore loggato
	private List<String> identificativiEnteCreditore = null;
	
	private DistintaMBean mBean = null ;

	public DistintaSearchForm(){
		this.setPageSize(10);
		
		init();
		
		reset();
	}
	
	@Override
	protected void init() {
		// Properties del form
		this.setIdForm("formDistinta");
		this.setNomeForm("Ricerca Distinte Pagamento");
		this.setClosable(true);
		this.setRendered(true); 
		
		this.identificativiEnteCreditore = Utils.getListaIdFiscaliEnteCreditoreGestitiDallOperatore();
		
		this.dataPeriodo = new SelectListField();
		this.dataPeriodo.setName("dataPeriodo");
		this.dataPeriodo.setDefaultValue(new SelectItem(DATA_PERIODO_ULTIMI_TRE_MESI,
				Utils.getMessageFromResourceBundle("distinta.search.data.ultimiTreMesi")));
		this.dataPeriodo.setLabel(Utils.getMessageFromResourceBundle("distinta.search.data"));
		this.dataPeriodo.setFieldsToUpdate(this.getIdForm() + "_formPnl");
		this.dataPeriodo.setForm(this);

		this.data = new DateField();
		this.data.setName("data");
		this.data.setDefaultValue(null);
		this.data.setDefaultValue2(null);
		this.data.setInterval(true);
		this.data.setLabel(Utils.getMessageFromResourceBundle("distinta.search.data.personalizzato"));
		
		_setPeriodo();
		
		this.cfVersanteODebitore = new TextField();
		//this.cfVersanteODebitore.setType("textWithSuggestion");
		this.cfVersanteODebitore.setName("cfVersanteODebitore");
		this.cfVersanteODebitore.setDefaultValue(null);
		this.cfVersanteODebitore.setLabel(Utils.getMessageFromResourceBundle("distinta.search.cfVersanteODebitore"));
		//this.cfVersanteODebitore.setAutoComplete(true);
		//	this.cfVersanteODebitore.setEnableManualInput(true);
		//this.cfVersanteODebitore.setFieldsToUpdate(this.getIdForm() + "_formPnl");
		//this.cfVersanteODebitore.setForm(this);
		
		this.statoDistinta = new SelectListField();
		this.statoDistinta.setName("statoDistinta");
		this.statoDistinta.setValue(null);
		this.statoDistinta.setLabel(Utils.getMessageFromResourceBundle("distinta.search.statoDistinta"));
		
		this.cfEnteCreditore = new TextField();
		this.cfEnteCreditore.setLabel(Utils.getMessageFromResourceBundle("distinta.search.cfEnteCreditore"));
		this.cfEnteCreditore.setName("cfEnteCreditore");
		this.cfEnteCreditore.setDefaultValue(null);
	}

	@Override
	public void reset() {
		resetParametriPaginazione();

		// reset search fields
		this.cfEnteCreditore.reset();
		this.cfVersanteODebitore.reset();
		this.statoDistinta.reset();
		this.dataPeriodo.reset();
		this.data.reset();
		_setPeriodo();
		
	}
	
	

	/**
	 * Aggiornamento del valore effettivo delle date quando seleziono un valore nella tendina del periodo
	 * 
	 * 0 ultima settimana
	 * 1 ultimo mese
	 * 2 ultimi 3 mesi 
	 * 3 personalizzato
	 */
	private void _setPeriodo() {
		Date dataInizio = this.getData().getValue();
		Date dataFine = this.getData().getValue2();

		String periodo = this.getDataPeriodo().getValue() != null ? this.getDataPeriodo().getValue().getValue() : DATA_PERIODO_ULTIMA_SETTIMANA ;

		Calendar today = Calendar.getInstance();
		today.set(Calendar.HOUR_OF_DAY, 23);
		today.set(Calendar.MINUTE, 59);
		today.clear(Calendar.SECOND);
		today.clear(Calendar.MILLISECOND);

		//ultima settimana
		if (DATA_PERIODO_ULTIMA_SETTIMANA.equals(periodo)) {
			Calendar lastWeek = (Calendar) today.clone();
			Calendar c = Calendar.getInstance();
			dataFine = c.getTime();
			lastWeek.set(Calendar.HOUR_OF_DAY, 0);
			lastWeek.set(Calendar.MINUTE, 0);
			lastWeek.add(Calendar.DATE, -7);
			dataInizio = lastWeek.getTime();

		} else if (DATA_PERIODO_ULTIMO_MESE.equals( periodo)) {
			Calendar lastMonth = (Calendar) today.clone();

			// prendo la data corrente
			dataFine = Calendar.getInstance().getTime();

			// la data inizio rimane uguale sia per giornaliero che per orario
			lastMonth.set(Calendar.HOUR_OF_DAY, 0);
			lastMonth.set(Calendar.MINUTE, 0);
			lastMonth.add(Calendar.DATE, -30);
			dataInizio = lastMonth.getTime();

		} else if (DATA_PERIODO_ULTIMI_TRE_MESI.equals( periodo)) {
			Calendar lastyear = (Calendar) today.clone();

			dataFine = Calendar.getInstance().getTime();

			lastyear.set(Calendar.HOUR_OF_DAY, 0);
			lastyear.set(Calendar.MINUTE, 0);
			lastyear.add(Calendar.DATE, -90);
			dataInizio = lastyear.getTime();

		}  else {
			// personalizzato
			dataInizio = this.getData().getValue();
			dataFine = this.getData().getValue2();
		}

		//aggiorno i valori del campo
		this.getData().setValue(dataInizio);
		this.getData().setValue2(dataFine);
	}

	public FormField<SelectItem> getDataPeriodo() {
		return dataPeriodo;
	}

	public void setDataPeriodo(FormField<SelectItem> dataPeriodo) {
		this.dataPeriodo = dataPeriodo;
	}

	public FormField<Date> getData() {
		
		boolean rendered = (this.getDataPeriodo().getValue() != null && this.getDataPeriodo().getValue().getValue()
				.equals(DATA_PERIODO_PERSONALIZZATO)); 

		this.data.setRendered(rendered);
		
		return data;
	}

	public void setData(FormField<Date> data) {
		this.data = data;
	}

	public FormField<SelectItem> getStatoDistinta() {
		return statoDistinta;
	}

	public void setStatoDistinta(FormField<SelectItem> statoDistinta) {
		this.statoDistinta = statoDistinta;
	}

	public FormField<String> getCfVersanteODebitore() {
		return cfVersanteODebitore;
	}

	public void setCfVersanteODebitore(FormField<String> cfVersanteODebitore) {
		this.cfVersanteODebitore = cfVersanteODebitore;
	}

	public DistintaMBean getmBean() {
		return mBean;
	}

	public void setmBean(DistintaMBean mBean) {
		this.mBean = mBean;
	}
	
	public List<SelectItem> soggettoVersanteAutoComplete(Object val){
		return this.mBean.soggettoVersanteAutoComplete(val);
	}
	
	public void dataPeriodoSelectListener(ActionEvent ae){
		_setPeriodo();
	}
	
	public void soggettoVersanteSelectListener(ActionEvent ae){
		//do something
	}

	public FormField<String> getCfEnteCreditore() {
		return cfEnteCreditore;
	}

	public void setCfEnteCreditore(FormField<String> cfEnteCreditore) {
		this.cfEnteCreditore = cfEnteCreditore;
	}

	public List<String> getIdentificativiEnteCreditore() {
		return identificativiEnteCreditore;
	}

	public void setIdentificativiEnteCreditore(
			List<String> identificativiEnteCreditore) {
		this.identificativiEnteCreditore = identificativiEnteCreditore;
	}
	
	
}
