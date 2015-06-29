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

import org.openspcoop2.generic_project.web.factory.FactoryException;
import org.openspcoop2.generic_project.web.form.SearchForm;
import org.openspcoop2.generic_project.web.impl.jsf1.form.BaseSearchForm;
import org.openspcoop2.generic_project.web.impl.jsf1.input.SelectItem;
import org.openspcoop2.generic_project.web.input.DateTime;
import org.openspcoop2.generic_project.web.input.SelectList;
import org.openspcoop2.generic_project.web.input.Text;

@Named("distintaSearchForm") @SessionScoped
public class DistintaSearchForm extends BaseSearchForm implements SearchForm, Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	public static final String DATA_PERIODO_PERSONALIZZATO = "3";
	public static final String DATA_PERIODO_ULTIMI_TRE_MESI = "2";
	public static final String DATA_PERIODO_ULTIMO_MESE = "1";
	public static final String DATA_PERIODO_ULTIMA_SETTIMANA = "0";
	private SelectList<SelectItem> dataPeriodo = null;
	private DateTime data = null;
	private SelectList<SelectItem> statoDistinta = null;
	private Text cfVersanteODebitore = null;
	private Text cfEnteCreditore = null;
	
	// lista degli id degli enti che vengono gestiti dall'operatore loggato
	private List<String> identificativiEnteCreditore = null;
	
	private DistintaMBean mBean = null ;

	public DistintaSearchForm(){
		this.setPageSize(10);
		
		try {
			init();
			reset();
		} catch (FactoryException e) {
		}
	}
	
	@Override
	public void init()  throws FactoryException{
		// Properties del form
		this.setId("formDistinta");
		this.setNomeForm("Ricerca Distinte Pagamento");
		this.setClosable(true);
		this.setRendered(true); 
		
		this.identificativiEnteCreditore = Utils.getListaIdFiscaliEnteCreditoreGestitiDallOperatore();
		
		this.dataPeriodo = this.getWebGenericProjectFactory().getInputFieldFactory().createSelectList();
		this.dataPeriodo.setName("dataPeriodo");
		this.dataPeriodo.setDefaultValue(new SelectItem(DATA_PERIODO_ULTIMI_TRE_MESI,
				Utils.getInstance().getMessageFromResourceBundle("distinta.search.data.ultimiTreMesi")));
		this.dataPeriodo.setLabel(Utils.getInstance().getMessageFromResourceBundle("distinta.search.data"));
		this.dataPeriodo.setFieldsToUpdate(this.getId() + "_formPnl");
		this.dataPeriodo.setForm(this);

		this.data = this.getWebGenericProjectFactory().getInputFieldFactory().createDateTime();
		this.data.setName("data");
		this.data.setDefaultValue(null);
		this.data.setDefaultValue2(null);
		this.data.setInterval(true);
		this.data.setLabel(Utils.getInstance().getMessageFromResourceBundle("distinta.search.data.personalizzato"));
		
		_setPeriodo();
		
		this.cfVersanteODebitore = this.getWebGenericProjectFactory().getInputFieldFactory().createText();
		//this.cfVersanteODebitore.setType("textWithSuggestion");
		this.cfVersanteODebitore.setName("cfVersanteODebitore");
		this.cfVersanteODebitore.setDefaultValue(null);
		this.cfVersanteODebitore.setLabel(Utils.getInstance().getMessageFromResourceBundle("distinta.search.cfVersanteODebitore"));
		//this.cfVersanteODebitore.setAutoComplete(true);
		//	this.cfVersanteODebitore.setEnableManualInput(true);
		//this.cfVersanteODebitore.setFieldsToUpdate(this.getIdForm() + "_formPnl");
		//this.cfVersanteODebitore.setForm(this);
		
		this.statoDistinta = this.getWebGenericProjectFactory().getInputFieldFactory().createSelectList();
		this.statoDistinta.setName("statoDistinta");
		this.statoDistinta.setValue(null);
		this.statoDistinta.setLabel(Utils.getInstance().getMessageFromResourceBundle("distinta.search.statoDistinta"));
		
		this.cfEnteCreditore = this.getWebGenericProjectFactory().getInputFieldFactory().createText();
		this.cfEnteCreditore.setLabel(Utils.getInstance().getMessageFromResourceBundle("distinta.search.cfEnteCreditore"));
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

	public SelectList<SelectItem> getDataPeriodo() {
		return dataPeriodo;
	}

	public void setDataPeriodo(SelectList<SelectItem> dataPeriodo) {
		this.dataPeriodo = dataPeriodo;
	}

	public DateTime getData() {
		
		boolean rendered = (this.getDataPeriodo().getValue() != null && this.getDataPeriodo().getValue().getValue()
				.equals(DATA_PERIODO_PERSONALIZZATO)); 

		this.data.setRendered(rendered);
		
		return data;
	}

	public void setData(DateTime data) {
		this.data = data;
	}

	public SelectList<SelectItem> getStatoDistinta() {
		return statoDistinta;
	}

	public void setStatoDistinta(SelectList<SelectItem> statoDistinta) {
		this.statoDistinta = statoDistinta;
	}

	public Text getCfVersanteODebitore() {
		return cfVersanteODebitore;
	}

	public void setCfVersanteODebitore(Text cfVersanteODebitore) {
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

	public Text getCfEnteCreditore() {
		return cfEnteCreditore;
	}

	public void setCfEnteCreditore(Text cfEnteCreditore) {
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
