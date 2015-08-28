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
package it.govpay.web.console.pagamenti.service;

import it.govpay.ejb.core.ejb.AnagraficaEJB;
import it.govpay.ejb.core.ejb.DistintaEJB;
import it.govpay.ejb.core.ejb.PendenzaEJB;
import it.govpay.ejb.core.filter.DistintaFilter;
import it.govpay.ejb.core.model.DistintaModel;
import it.govpay.ejb.core.model.DistintaModel.EnumStatoDistinta;
import it.govpay.ejb.core.model.EnteCreditoreModel;
import it.govpay.ejb.core.model.EsitoPagamentoDistinta;
import it.govpay.ejb.core.model.PagamentoModel.EnumStatoPagamento;
import it.govpay.ejb.core.model.PendenzaModel;
import it.govpay.ejb.ndp.ejb.AnagraficaDominioEJB;
import it.govpay.ejb.ndp.ejb.DocumentiEJB;
import it.govpay.ejb.ndp.model.impl.RPTModel;
import it.govpay.ejb.ndp.model.impl.RTModel;
import it.govpay.web.console.pagamenti.bean.DistintaBean;
import it.govpay.web.console.pagamenti.form.DistintaSearchForm;
import it.govpay.web.console.pagamenti.iservice.IDistintaService;
import it.govpay.web.console.pagamenti.model.PagamentoModel;
import it.govpay.web.console.pagamenti.model.PagamentoModel.DettaglioPagamento;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.inject.Inject;
import javax.inject.Named;
import javax.inject.Singleton;

import org.apache.commons.lang.StringUtils;
import org.apache.logging.log4j.Logger;
import org.openspcoop2.generic_project.exception.ServiceException;
import org.openspcoop2.generic_project.web.form.CostantiForm;
import org.openspcoop2.generic_project.web.impl.jsf1.input.SelectItem;
import org.openspcoop2.generic_project.web.input.DateTime;
import org.openspcoop2.generic_project.web.input.SelectList;
import org.openspcoop2.generic_project.web.service.BaseService;

@Named("distintaService") @Singleton
public class DistintaService extends BaseService<DistintaSearchForm> implements IDistintaService,Serializable{


	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Inject  
	private transient Logger log;

	@Inject
	DistintaEJB distintaEjb; 

	@Inject
	PendenzaEJB pendenzaEjb; 

	@Inject
	AnagraficaEJB anagraficaEjb; 

	@Inject
	AnagraficaDominioEJB anagraficaDominioEjb; 

	@Inject
	DocumentiEJB documentiEjb; 


	@PostConstruct
	private void init(){
		log.debug("Init DistintaService completato. ID["+this.toString()+"]"); 
	}

	@Override @Inject @Named("distintaSearchForm")
	public void setForm(DistintaSearchForm form) {
		super.setForm(form);
		log.debug("Set distintaSearchForm completato. IDForm["+form.hashCode()+"]"); 
	}

	@Override
	public List<DistintaBean> findAll(int start, int limit)
			throws ServiceException {
		List<DistintaBean> lst = new ArrayList<DistintaBean>();
		try{
			log.debug("findAll Distinte Offset["+start+"] Limit["+limit+"] in corso...");
			DistintaFilter filtro = getFiltro(start,limit,this.form);
			lst = _findAll(filtro);
			log.debug("findAll Distinte Offset["+start+"] Limit["+limit+"] completata.");

		}catch(Exception e){
			log.error("Si e' verificato un errore durante la findAll Distinte Offset["+start+"] Limit["+limit+"]: "+ e.getMessage(), e);
		}
		return lst;
	}

	@Override
	public int totalCount() throws ServiceException {
		try{
			log.debug("Count Distinte in corso...");
			DistintaFilter filtro = getFiltro(null,null,this.form);
			int cnt = this.distintaEjb.countAllDistinte(filtro);
			log.debug("Count Distinte completata trovate["+cnt+"].");
			return cnt;
		}catch(Exception e){
			log.error("Si e' verificato un errore durante la count delle distinte: "+ e.getMessage(), e);
		}

		return 0;
	}

	@Override
	public void store(DistintaBean obj) throws ServiceException {
	}

	@Override
	public void deleteById(Long key) throws ServiceException {
	}

	@Override
	public void delete(DistintaBean obj) throws ServiceException {
	}

	@Override
	public DistintaBean findById(Long key) throws ServiceException {
		try{
			log.debug("findById Distinta in corso...");
			DistintaBean bean = findPagamentoById(key);			
			log.debug("findById Distinta completata.");
			return bean;
		}catch(Exception e){
			log.error("Si e' verificato un errore durante la findById Distinta: "+ e.getMessage(), e);
		}
		return null;
	}

	private DistintaBean findPagamentoById(Long key) throws ServiceException {
		try{
			log.debug("findById Pagamento in corso...");

			DistintaModel distintaById = this.distintaEjb.findDistintaById(key);

			log.debug("findById Pagamento completata.");
			if(distintaById != null){
				List<PendenzaModel> pendenze = pendenzaEjb.getPendenze(key);
				EsitoPagamentoDistinta esitoPagamento = this.distintaEjb.getEsitoPagamentoDistinta(key);
				EnteCreditoreModel enteCreditore = anagraficaEjb.getCreditoreByIdLogico(esitoPagamento.getIdentificativoFiscaleCreditore());
				PagamentoModel pagamento = new PagamentoModel();
				pagamento.setAutenticazione(distintaById.getAutenticazione().toString());
				pagamento.setCcp(distintaById.getCodTransazionePsp());
				pagamento.setDebitore(pendenze.get(0).getDebitore());
				pagamento.setEnteCreditore(enteCreditore);
				pagamento.setGatewayPagamento(anagraficaEjb.getValidGateway(distintaById.getIdGatewayPagamento()));
				pagamento.setIbanAddebito(distintaById.getIbanAddebito());
				pagamento.setImportoDovuto(distintaById.getImportoTotale());
				pagamento.setImportoVersato(esitoPagamento.getImportoTotalePagato());
				pagamento.setIuv(distintaById.getIuv());
				pagamento.setStato(esitoPagamento.getStato().getDescrizione());
				pagamento.setTributo(anagraficaEjb.getTributoById(enteCreditore.getIdEnteCreditore(), pendenze.get(0).getCodiceTributo()));
				pagamento.setVersante(distintaById.getSoggettoVersante());

				List<DettaglioPagamento> dettagliPagamento = new ArrayList<PagamentoModel.DettaglioPagamento>();

				for(it.govpay.ejb.core.model.PagamentoModel pagamentoEjbModel : distintaById.getPagamenti()) {
					PendenzaModel pendenzaModel = null;
					for(PendenzaModel pendenzaModelAct : pendenze) {
						if(pendenzaModelAct.getCondizioniPagamento().get(0).getIdCondizione().equals(pagamentoEjbModel.getIdCondizionePagamento())) {
							pendenzaModel = pendenzaModelAct;
						}
					}
					DettaglioPagamento dettaglio = pagamento.new DettaglioPagamento();
					dettaglio.setAnnoRiferimento(pendenzaModel.getAnnoRiferimento());
					dettaglio.setCausale(pendenzaModel.getCondizioniPagamento().get(0).getCausale());
					dettaglio.setDataRicevuta(pagamentoEjbModel.getDataPagamento());
					dettaglio.setIbanAccredito(pendenzaModel.getCondizioniPagamento().get(0).getIbanBeneficiario());
					dettaglio.setIdentificativo(pendenzaModel.getCondizioniPagamento().get(0).getIdPagamentoEnte());
					dettaglio.setImportoDovuto(pendenzaModel.getCondizioniPagamento().get(0).getImportoTotale());
					if(pagamentoEjbModel.getStato().equals(EnumStatoPagamento.ES))
						dettaglio.setImportoVersato(pagamentoEjbModel.getImportoPagato());
					else
						dettaglio.setImportoVersato(BigDecimal.ZERO);
					dettaglio.setIur(pagamentoEjbModel.getIdRiscossionePSP());
					dettaglio.setStato(pagamentoEjbModel.getStato().getDescrizione());
					dettagliPagamento.add(dettaglio);
				}

				pagamento.setDettagliPagamento(dettagliPagamento); 

				RPTModel rptModel = documentiEjb.recuperaRPT(enteCreditore.getIdFiscale(), distintaById.getIuv(), distintaById.getCodTransazionePsp());
				if(rptModel != null) {
					pagamento.setRpt(rptModel.getBytes());
				}

				RTModel rtModel = documentiEjb.recuperaRT(enteCreditore.getIdFiscale(), distintaById.getIuv(), distintaById.getCodTransazionePsp());
				if(rtModel != null) {
					pagamento.setRt(rtModel.getBytes());
				}

				DistintaBean bean = new DistintaBean();

				bean.setDTO(distintaById);
				bean.setDTO(pagamento); 

				return bean;
			}
		}catch(Exception e){
			log.error("Si e' verificato un errore durante la findById Distinta: "+ e.getMessage(), e);
		}
		return null;
	}

	@Override
	public List<DistintaBean> findAll() throws ServiceException {
		List<DistintaBean> lst = new ArrayList<DistintaBean>();

		try{
			log.debug("findAll Distinte in corso...");
			DistintaFilter filtro = getFiltro(null,null,this.form);
			lst = _findAll(filtro);
			log.debug("findAll Distinte completata.");

		}catch(Exception e){
			log.error("Si e' verificato un errore durante la findAll Distinte: "+ e.getMessage(), e);
		}


		return lst;
	}

	@Override
	public List<DistintaBean> findAll(DistintaSearchForm form)
			throws ServiceException {
		List<DistintaBean> lst = new ArrayList<DistintaBean>();

		try{
			log.debug("findAll Distinte in corso...");
			DistintaFilter filtro = getFiltro(null,null,form);
			lst = _findAll(filtro);
			log.debug("findAll Distinte completata.");

		}catch(Exception e){
			log.error("Si e' verificato un errore durante la findAll Distinte: "+ e.getMessage(), e);
		}


		return lst;
	}

	private List<DistintaBean> _findAll(DistintaFilter filtro) throws Exception{
		List<DistintaBean> lst = new ArrayList<DistintaBean>();
		List<DistintaModel> distinte = this.distintaEjb.findAllDistinte(filtro);

		if(distinte != null && distinte.size() > 0 ){
			for (DistintaModel distinta : distinte) {
				List<PendenzaModel> pendenze = pendenzaEjb.getPendenze(distinta.getIdDistinta());
				EnteCreditoreModel enteCreditore = anagraficaEjb.getCreditoreByIdLogico(distinta.getIdentificativoFiscaleCreditore());
				PagamentoModel pagamento = new PagamentoModel();
				pagamento.setEnteCreditore(enteCreditore);
				pagamento.setImportoDovuto(distinta.getImportoTotale());
				pagamento.setIuv(distinta.getIuv());
				pagamento.setStato(distinta.getStato().getDescrizione());
				pagamento.setTributo(anagraficaEjb.getTributoById(enteCreditore.getIdEnteCreditore(), pendenze.get(0).getCodiceTributo()));
				pagamento.setDebitore(pendenze.get(0).getDebitore());

				DistintaBean bean = new DistintaBean();
				bean.setDTO(distinta);
				bean.setDTO(pagamento); 
				lst.add(bean);
			}
		}
		return lst;
	}

	private DistintaFilter getFiltro(Integer start, Integer limit, DistintaSearchForm form) {
		DistintaFilter filtro = new DistintaFilter();

		log.debug("Imposto filtro ricerca distinte"); 

		filtro.setOffset(start);
		filtro.setLimit(limit);

		log.debug("Offset["+start+"], Limit["+limit+"]");

		//Imposto le date
	//	impostaDate(form, filtro);


		// cfentecreditore
		filtro.setCfEnteCreditore(form.getCfEnteCreditore().getValue());
		log.debug("CF Ente Creditore["+form.getCfEnteCreditore().getValue()+"]");

		// cfversanteDebitore
		filtro.setCfVersanteODebitore(form.getCfVersanteODebitore().getValue());
		log.debug("CFVersanteODebitore["+form.getCfVersanteODebitore().getValue()+"]");

		// listaenti
		filtro.setIdentificativiEnteCreditore(form.getIdentificativiEnteCreditore());
		log.debug("IDEnteCreditore["+form.getIdentificativiEnteCreditore()+"]");

		// stato
		SelectList<SelectItem> stato = form.getStatoDistinta();
		String periodo = stato.getValue() != null ? stato.getValue().getValue() : CostantiForm.NON_SELEZIONATO;

		if(!StringUtils.isEmpty(periodo) && !periodo.equals(CostantiForm.NON_SELEZIONATO)){
			EnumStatoDistinta statoD = EnumStatoDistinta.valueOf(periodo);
			filtro.setStato(statoD);
		}

		return filtro;
	}


	private void impostaDate(DistintaSearchForm form,DistintaFilter filtro) {
		SelectList<SelectItem> dataPeriodo = form.getDataPeriodo();
		DateTime data = form.getData();

		Date dataInizio = data.getValue();
		Date dataFine = data.getValue2();

		String periodo = dataPeriodo.getValue() != null ? dataPeriodo.getValue().getValue() : DistintaSearchForm.DATA_PERIODO_ULTIMA_SETTIMANA;

		Calendar today = Calendar.getInstance();
		today.set(Calendar.HOUR_OF_DAY, 23);
		today.set(Calendar.MINUTE, 59);
		today.clear(Calendar.SECOND);
		today.clear(Calendar.MILLISECOND);

		//ultima settimana
		if ( DistintaSearchForm.DATA_PERIODO_ULTIMA_SETTIMANA.equals(periodo)) {
			Calendar lastWeek = (Calendar) today.clone();
			Calendar c = Calendar.getInstance();
			dataFine = c.getTime();
			lastWeek.set(Calendar.HOUR_OF_DAY, 0);
			lastWeek.set(Calendar.MINUTE, 0);
			lastWeek.add(Calendar.DATE, -7);
			dataInizio = lastWeek.getTime();

		} else if ( DistintaSearchForm.DATA_PERIODO_ULTIMO_MESE.equals( periodo)) {
			Calendar lastMonth = (Calendar) today.clone();

			// prendo la data corrente
			dataFine = Calendar.getInstance().getTime();

			// la data inizio rimane uguale sia per giornaliero che per orario
			lastMonth.set(Calendar.HOUR_OF_DAY, 0);
			lastMonth.set(Calendar.MINUTE, 0);
			lastMonth.add(Calendar.DATE, -30);
			dataInizio = lastMonth.getTime();

		} else if ( DistintaSearchForm.DATA_PERIODO_ULTIMI_TRE_MESI.equals( periodo)) {
			Calendar lastyear = (Calendar) today.clone();

			dataFine = Calendar.getInstance().getTime();

			lastyear.set(Calendar.HOUR_OF_DAY, 0);
			lastyear.set(Calendar.MINUTE, 0);
			lastyear.add(Calendar.DATE, -90);
			dataInizio = lastyear.getTime();

		}  else {
			// personalizzato
			dataInizio = data.getValue();
			dataFine = data.getValue2();
		}


		if(dataInizio != null){
			log.debug("Data inizio["+dataInizio+"]");
			filtro.setDataInizio(dataInizio);
		}

		if(dataFine != null){
			log.debug("Data Fine["+dataFine+"]");
			filtro.setDataFine(dataFine);
		}
	}

	@Override
	public boolean exists(DistintaBean arg0) throws ServiceException {
		return false;
	}


}
