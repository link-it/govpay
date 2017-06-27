/*
 * GovPay - Porta di Accesso al Nodo dei Pagamenti SPC 
 * http://www.gov4j.it/govpay
 * 
 * Copyright (c) 2014-2017 Link.it srl (http://www.link.it).
 * 
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License version 3, as published by
 * the Free Software Foundation.
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
package it.govpay.web.rs.dars.statistiche.transazioni;

import java.net.URI;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.zip.ZipOutputStream;

import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.UriInfo;

import org.apache.commons.lang.StringUtils;
import org.apache.logging.log4j.Logger;
import org.openspcoop2.generic_project.exception.ServiceException;
import org.openspcoop2.generic_project.expression.SortOrder;

import it.govpay.bd.BasicBD;
import it.govpay.bd.FilterSortWrapper;
import it.govpay.bd.anagrafica.AnagraficaManager;
import it.govpay.bd.anagrafica.DominiBD;
import it.govpay.bd.anagrafica.PspBD;
import it.govpay.bd.anagrafica.filters.DominioFilter;
import it.govpay.bd.anagrafica.filters.PspFilter;
import it.govpay.bd.model.Dominio;
import it.govpay.bd.model.Psp;
import it.govpay.bd.reportistica.statistiche.TransazioniBD;
import it.govpay.bd.reportistica.statistiche.filters.TransazioniFilter;
import it.govpay.model.reportistica.statistiche.DistribuzioneEsiti;
import it.govpay.model.reportistica.statistiche.TipoIntervallo;
import it.govpay.web.rs.dars.anagrafica.domini.Domini;
import it.govpay.web.rs.dars.anagrafica.domini.DominiHandler;
import it.govpay.web.rs.dars.anagrafica.psp.PspHandler;
import it.govpay.web.rs.dars.base.BaseDarsService;
import it.govpay.web.rs.dars.base.StatisticaDarsHandler;
import it.govpay.web.rs.dars.exception.ConsoleException;
import it.govpay.web.rs.dars.exception.ExportException;
import it.govpay.web.rs.dars.handler.IStatisticaDarsHandler;
import it.govpay.web.rs.dars.model.ElementoCorrelato;
import it.govpay.web.rs.dars.model.InfoForm;
import it.govpay.web.rs.dars.model.InfoForm.Sezione;
import it.govpay.web.rs.dars.model.RawParamValue;
import it.govpay.web.rs.dars.model.Voce;
import it.govpay.web.rs.dars.model.input.ParamField;
import it.govpay.web.rs.dars.model.input.base.InputDate;
import it.govpay.web.rs.dars.model.input.base.InputNumber;
import it.govpay.web.rs.dars.model.input.base.SelectList;
import it.govpay.web.rs.dars.model.statistiche.Colori;
import it.govpay.web.rs.dars.model.statistiche.Grafico;
import it.govpay.web.rs.dars.model.statistiche.Grafico.TipoGrafico;
import it.govpay.web.rs.dars.model.statistiche.PaginaGrafico;
import it.govpay.web.rs.dars.model.statistiche.Serie;
import it.govpay.web.utils.Utils;

public class DistribuzioneEsitiHandler extends StatisticaDarsHandler<DistribuzioneEsiti> implements IStatisticaDarsHandler<DistribuzioneEsiti>{

	public static final String ANAGRAFICA_DEBITORE = "anagrafica";
	private SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm");  
	private SimpleDateFormat sdfGiorno = new SimpleDateFormat("dd/MM/yyyy");
	private SimpleDateFormat sdfMese = new SimpleDateFormat("MMMMM");  

	public DistribuzioneEsitiHandler(Logger log, BaseDarsService darsService) { 
		super(log, darsService);
	}


	@Override
	public PaginaGrafico getGrafico(UriInfo uriInfo, BasicBD bd) throws WebApplicationException, ConsoleException {
		String methodName = "getGrafico " + this.titoloServizio;
		try{	
			// Operazione consentita solo agli utenti che hanno almeno un ruolo consentito per la funzionalita'
			this.darsService.checkDirittiServizio(bd, this.funzionalita);

			this.log.info("Esecuzione " + methodName + " in corso..."); 

			TransazioniBD transazioniBD = new TransazioniBD(bd);

			TransazioniFilter filter = transazioniBD.newFilter();
			this.popolaFiltroRicerca(uriInfo, transazioniBD, filter);

			Calendar calendar = Calendar.getInstance();
			calendar.setTime(filter.getData());
			SimpleDateFormat _sdf = null;
			switch (filter.getTipoIntervallo()) {
			case MENSILE:
				calendar.add(Calendar.MONTH, - filter.getLimit());
				_sdf = this.sdfMese;
				break;
			case GIORNALIERO:
				calendar.add(Calendar.DATE, - filter.getLimit());
				_sdf= this.sdfGiorno;
				break;
			case ORARIO:
				calendar.add(Calendar.HOUR, - filter.getLimit());
				_sdf = this.sdf;
				break;
			}
			Date start = calendar.getTime();
			String sottoTitolo = Utils.getInstance(this.getLanguage()).getMessageWithParamsFromResourceBundle(this.nomeServizio + ".label.sottotitolo",_sdf.format(start),_sdf.format(filter.getData()));

			

			// visualizza la ricerca solo se i risultati sono > del limit
			InfoForm infoRicerca = this.getInfoRicerca(uriInfo, bd);
			Map<String, ParamField<?>> infoGrafico = this.getInfoGrafico(uriInfo, bd); 

			List<DistribuzioneEsiti> distribuzioneEsiti = transazioniBD.getDistribuzioneEsiti(filter);

			this.log.info("Esecuzione " + methodName + " completata.");

			PaginaGrafico paginaGrafico = new PaginaGrafico(this.titoloServizio, this.getInfoEsportazione(uriInfo,bd), infoRicerca,infoGrafico); 

			Grafico grafico = new Grafico(TipoGrafico.bar);

			grafico.setLabelX(Utils.getInstance(this.getLanguage()).getMessageFromResourceBundle(this.nomeServizio + ".xAxis.label"));
			grafico.setLabelY(Utils.getInstance(this.getLanguage()).getMessageFromResourceBundle(this.nomeServizio + ".yAxis.label"));
			grafico.setTitolo(Utils.getInstance(this.getLanguage()).getMessageFromResourceBundle(this.nomeServizio + ".label.titolo"));
			grafico.setSottotitolo(sottoTitolo); 
			grafico.getColori().addAll(Colori.getColoriTransazioni());
			grafico.getCategorie().add(Utils.getInstance(this.getLanguage()).getMessageFromResourceBundle(this.nomeServizio + ".transazioniCompletate.label"));
			grafico.getCategorie().add(Utils.getInstance(this.getLanguage()).getMessageFromResourceBundle(this.nomeServizio + ".transazioniRifiutate.label"));
			grafico.getCategorie().add(Utils.getInstance(this.getLanguage()).getMessageFromResourceBundle(this.nomeServizio + ".transazioniInCorso.label"));

			if (distribuzioneEsiti != null && distribuzioneEsiti.size() > 0) {
				Serie<Long> serie1 = new Serie<Long>();
				Serie<Long> serie2 = new Serie<Long>();
				Serie<Long> serie3 = new Serie<Long>();
				for (DistribuzioneEsiti elemento : distribuzioneEsiti) {
					String dataElemento = _sdf.format(elemento.getData());
					long serie1Val = elemento.getEseguiti();
					long serie2Val = elemento.getErrori();
					long serie3Val = elemento.getIn_corso();

					grafico.getValoriX().add(dataElemento);

					serie1.getDati().add(serie1Val);
					serie2.getDati().add(serie2Val);
					serie3.getDati().add(serie3Val);
					serie1.getTooltip().add(Utils.getInstance(this.getLanguage()).getMessageWithParamsFromResourceBundle(this.nomeServizio + ".transazioniCompletate.tooltip", dataElemento, serie1Val));
					serie2.getTooltip().add(Utils.getInstance(this.getLanguage()).getMessageWithParamsFromResourceBundle(this.nomeServizio + ".transazioniCompletate.tooltip", dataElemento, serie2Val));
					serie3.getTooltip().add(Utils.getInstance(this.getLanguage()).getMessageWithParamsFromResourceBundle(this.nomeServizio + ".transazioniCompletate.tooltip", dataElemento, serie3Val));
				}

				grafico.getSerie().add(serie1);
				grafico.getSerie().add(serie2);
				grafico.getSerie().add(serie3);
			}
			
			paginaGrafico.setGrafico(grafico );
			
			DistribuzionePsp distrPsp = new DistribuzionePsp();
			URI grafico2 = new URI(distrPsp.getPathServizio());
			ElementoCorrelato graficoTorta = new ElementoCorrelato(Utils.getInstance(this.getLanguage()).getMessageFromResourceBundle(distrPsp.getNomeServizio() + ".titolo"), grafico2);
			// elementi correlati
			paginaGrafico.getElementiCorrelati().add(graficoTorta );
			

			return paginaGrafico;
		}catch(WebApplicationException e){
			throw e;
		}catch(Exception e){
			throw new ConsoleException(e);
		}
	}

	private boolean popolaFiltroRicerca(UriInfo uriInfo, BasicBD bd, TransazioniFilter filter) throws ConsoleException, Exception {
		Set<Long> setDomini = this.darsService.getIdDominiAbilitatiLetturaServizio(bd, this.funzionalita);
		boolean eseguiRicerca = !setDomini.isEmpty(); // isAdmin;

		String idDominioId = Utils.getInstance(this.getLanguage()).getMessageFromResourceBundle(this.nomeServizio + ".idDominio.id");
		String idDominio = this.getParameter(uriInfo, idDominioId, String.class);


		if(StringUtils.isNotEmpty(idDominio)){
			long idDom = -1l;
			try{
				idDom = Long.parseLong(idDominio);
			}catch(Exception e){ idDom = -1l;	}
			if(idDom > 0){
				filter.setCodDominio(AnagraficaManager.getDominio(bd, idDom).getCodDominio());
			}
		}

		String idPspId = Utils.getInstance(this.getLanguage()).getMessageFromResourceBundle(this.nomeServizio + ".idPsp.id");
		String idPsp = this.getParameter(uriInfo, idPspId, String.class);
		if(StringUtils.isNotEmpty(idPsp)){
			long idPspL = -1l;
			try{
				idPspL = Long.parseLong(idPsp);
			}catch(Exception e){ idPspL = -1l;	}
			if(idPspL > 0){
				filter.setCodPsp(AnagraficaManager.getPsp(bd, idPspL).getCodPsp());
			}
		}
		
		Date data = new Date();
		String dataId = Utils.getInstance(this.getLanguage()).getMessageFromResourceBundle("statistiche.data.id");
		String dataS = this.getParameter(uriInfo, dataId, String.class);
		if(StringUtils.isNotEmpty(dataS)){
			data = this.convertJsonStringToDate(dataS);
		}
		filter.setData(data);

		String colonneId = Utils.getInstance(this.getLanguage()).getMessageFromResourceBundle("statistiche.colonne.id");
		String colonneS = this.getParameter(uriInfo, colonneId, String.class);
		int colonne = 0;
		if(StringUtils.isNotEmpty(colonneS)){
			try{
				colonne = Integer.parseInt(colonneS);
			}catch(Exception e){

			}
		}
		filter.setLimit(colonne);
		
//		String avanzamentoId = Utils.getInstance(this.getLanguage()).getMessageFromResourceBundle("statistiche.avanzamento.id");
//		String avanzamentoS = this.getParameter(uriInfo, avanzamentoId, String.class);
//		int avanzamento = 0;
//		if(StringUtils.isNotEmpty(avanzamentoS)){
//			try{
//				avanzamento = Integer.parseInt(avanzamentoS);
//			}catch(Exception e){
//
//			}
//		}
		
		String tipoIntervalloId = Utils.getInstance(this.getLanguage()).getMessageFromResourceBundle("statistiche.tipoIntervallo.id");
		String tipoIntervalloS = this.getParameter(uriInfo, tipoIntervalloId, String.class);
		TipoIntervallo tipoIntervallo= TipoIntervallo.GIORNALIERO;
		if(StringUtils.isNotEmpty(tipoIntervalloS)){
			tipoIntervallo = TipoIntervallo.valueOf(tipoIntervalloS);
		}
		filter.setTipoIntervallo(tipoIntervallo);

		return eseguiRicerca ;
	}

//	private boolean popolaFiltroRicerca(List<RawParamValue> rawValues, BasicBD bd, VersamentoFilter filter) throws ConsoleException, Exception {
//		//		Set<Long> setDomini = this.darsService.getIdDominiAbilitatiLetturaServizio(bd, this.funzionalita);
//		List<Long> idPsps = new ArrayList<Long>();
//		List<Long> idDomini = new ArrayList<Long>();
//		boolean eseguiRicerca = true;  
//
//		String idPspId = Utils.getInstance(this.getLanguage()).getMessageFromResourceBundle(this.nomeServizio + ".idPsp.id");
//		String idPsp = Utils.getValue(rawValues, idPspId);
//		if(StringUtils.isNotEmpty(idPsp)){
//			long idPspL = -1l;
//			try{
//				idPspL = Long.parseLong(idPsp);
//			}catch(Exception e){ idPspL = -1l;	}
//			if(idPspL > 0){
//				idPsps.add(idPspL);
//				//	filter.setIdPsp(idPsps);
//			}
//		}
//
//		String idDominioId = Utils.getInstance(this.getLanguage()).getMessageFromResourceBundle(this.nomeServizio + ".idDominio.id");
//		String idDominio = Utils.getValue(rawValues, idDominioId);
//		if(StringUtils.isNotEmpty(idDominio)){
//			long idDom = -1l;
//			try{
//				idDom = Long.parseLong(idDominio);
//			}catch(Exception e){ idDom = -1l;	}
//			if(idDom > 0){
//				idDomini.add(idDom);
//			}
//		}
//
//		return eseguiRicerca  ;
//	}

	@SuppressWarnings("unchecked")
	@Override
	public InfoForm getInfoRicerca(UriInfo uriInfo, BasicBD bd, boolean visualizzaRicerca, Map<String,String> parameters) throws ConsoleException {
		URI ricerca = this.getUriRicerca(uriInfo, bd);
		InfoForm infoRicerca = new InfoForm(ricerca);

		if(visualizzaRicerca) {
			String idPspId = Utils.getInstance(this.getLanguage()).getMessageFromResourceBundle(this.nomeServizio + ".idPsp.id");
			String idDominioId = Utils.getInstance(this.getLanguage()).getMessageFromResourceBundle(this.nomeServizio + ".idDominio.id");

			if(this.infoRicercaMap == null){
				this.initInfoRicerca(uriInfo, bd);
			}

			Sezione sezioneRoot = infoRicerca.getSezioneRoot();

			try{
				// Operazione consentita solo agli utenti che hanno almeno un ruolo consentito per la funzionalita'
				this.darsService.checkDirittiServizio(bd, this.funzionalita);

				// idDominio
				List<Voce<Long>> domini = new ArrayList<Voce<Long>>();

				DominiBD dominiBD = new DominiBD(bd);
				DominioFilter filter;
				try {
					filter = dominiBD.newFilter();
					domini.add(new Voce<Long>(Utils.getInstance(this.getLanguage()).getMessageFromResourceBundle("commons.label.qualsiasi"), -1L));
					FilterSortWrapper fsw = new FilterSortWrapper();
					fsw.setField(it.govpay.orm.Dominio.model().COD_DOMINIO);
					fsw.setSortOrder(SortOrder.ASC);
					filter.getFilterSortList().add(fsw);
					List<Dominio> findAll = dominiBD.findAll(filter );

					Domini dominiDars = new Domini();
					DominiHandler dominiHandler = (DominiHandler) dominiDars.getDarsHandler();

					if(findAll != null && findAll.size() > 0){
						for (Dominio dominio : findAll) {
							domini.add(new Voce<Long>(dominiHandler.getTitolo(dominio,bd), dominio.getId()));  
						}
					}
				} catch (ServiceException e) {
					throw new ConsoleException(e);
				}
				SelectList<Long> idDominio = (SelectList<Long>) this.infoRicercaMap.get(idDominioId);
				idDominio.setDefaultValue(-1L);
				idDominio.setValues(domini); 
				sezioneRoot.addField(idDominio);

			}catch(Exception e){
				throw new ConsoleException(e);
			}

			try{
				// idDominio
				List<Voce<Long>> psp  = new ArrayList<Voce<Long>>();

				PspBD pspBD = new PspBD(bd);
				PspFilter filter;
				try {
					filter = pspBD.newFilter();
					psp.add(new Voce<Long>(Utils.getInstance(this.getLanguage()).getMessageFromResourceBundle("commons.label.qualsiasi"), -1L));
					FilterSortWrapper fsw = new FilterSortWrapper();
					fsw.setField(it.govpay.orm.Psp.model().RAGIONE_SOCIALE);
					fsw.setSortOrder(SortOrder.ASC);
					filter.getFilterSortList().add(fsw);
					List<Psp> findAll = pspBD.findAll(filter );

					it.govpay.web.rs.dars.anagrafica.psp.Psp pspDars = new it.govpay.web.rs.dars.anagrafica.psp.Psp();
					PspHandler pspHandler = (PspHandler) pspDars.getDarsHandler();

					if(findAll != null && findAll.size() > 0){
						for (Psp _psp : findAll) {
							psp.add(new Voce<Long>(pspHandler.getTitolo(_psp,bd), _psp.getId()));  
						}
					}
				} catch (ServiceException e) {
					throw new ConsoleException(e);
				}
				SelectList<Long> idPsp = (SelectList<Long>) this.infoRicercaMap.get(idPspId);
				idPsp.setDefaultValue(-1L);
				idPsp.setValues(psp); 
				sezioneRoot.addField(idPsp);

			}catch(Exception e){
				throw new ConsoleException(e);
			}

		}
		return infoRicerca;
	}

	private void initInfoRicerca(UriInfo uriInfo, BasicBD bd) throws ConsoleException{
		if(this.infoRicercaMap == null){
			this.infoRicercaMap = new HashMap<String, ParamField<?>>();

			String idPspId = Utils.getInstance(this.getLanguage()).getMessageFromResourceBundle(this.nomeServizio + ".idPsp.id");
			String idDominioId = Utils.getInstance(this.getLanguage()).getMessageFromResourceBundle(this.nomeServizio + ".idDominio.id");
			String dataId = Utils.getInstance(this.getLanguage()).getMessageFromResourceBundle("statistiche.data.id");
			String colonneId = Utils.getInstance(this.getLanguage()).getMessageFromResourceBundle("statistiche.colonne.id");
			String avanzamentoId = Utils.getInstance(this.getLanguage()).getMessageFromResourceBundle("statistiche.avanzamento.id");
			String tipoIntervalloId = Utils.getInstance(this.getLanguage()).getMessageFromResourceBundle("statistiche.tipoIntervallo.id");

			List<Voce<Long>> psp  = new ArrayList<Voce<Long>>();
			// idDominio
			String idPspLabel = Utils.getInstance(this.getLanguage()).getMessageFromResourceBundle(this.nomeServizio + ".idPsp.label");
			SelectList<Long> idPsp = new SelectList<Long>(idPspId, idPspLabel, null, false, false, true, psp);
			this.infoRicercaMap.put(idPspId, idPsp);

			List<Voce<Long>> domini = new ArrayList<Voce<Long>>();
			// idDominio
			String idDominioLabel = Utils.getInstance(this.getLanguage()).getMessageFromResourceBundle(this.nomeServizio + ".idDominio.label");
			SelectList<Long> idDominio = new SelectList<Long>(idDominioId, idDominioLabel, null, false, false, true, domini);
			this.infoRicercaMap.put(idDominioId, idDominio);

			InputNumber colonne = new InputNumber(colonneId, null, null, true, true, false, 1, 20);
			this.infoRicercaMap.put(colonneId, colonne);

			InputNumber avanzamento = new InputNumber(avanzamentoId, null, null, true, true, false, 1, 20);
			this.infoRicercaMap.put(avanzamentoId, avanzamento);

			InputDate data = new InputDate(dataId, null, new Date(), false, false, true, null, null);
			this.infoRicercaMap.put(dataId, data);

			List<Voce<String>> tipiIntervallo = new ArrayList<Voce<String>>(); //tipoIntervallo.ORARIO.label
			tipiIntervallo.add(new Voce<String>(Utils.getInstance(this.getLanguage()).getMessageFromResourceBundle("statistiche.tipoIntervallo."+TipoIntervallo.ORARIO.name()+".label"),TipoIntervallo.ORARIO.name()));
			tipiIntervallo.add(new Voce<String>(Utils.getInstance(this.getLanguage()).getMessageFromResourceBundle("statistiche.tipoIntervallo."+TipoIntervallo.GIORNALIERO.name()+".label"),TipoIntervallo.GIORNALIERO.name()));
			tipiIntervallo.add(new Voce<String>(Utils.getInstance(this.getLanguage()).getMessageFromResourceBundle("statistiche.tipoIntervallo."+TipoIntervallo.MENSILE.name()+".label"),TipoIntervallo.MENSILE.name()));
			String tipoIntervalloLabel = Utils.getInstance(this.getLanguage()).getMessageFromResourceBundle("statistiche.tipoIntervallo.label");
			SelectList<String> tipoIntervallo = new SelectList<String>(tipoIntervalloId, tipoIntervalloLabel, TipoIntervallo.GIORNALIERO.name(), false, false, true, tipiIntervallo );
			this.infoRicercaMap.put(tipoIntervalloId, tipoIntervallo);
		}
	}

	@SuppressWarnings("unchecked")
	@Override
	public Map<String, ParamField<?>> getInfoGrafico(UriInfo uriInfo, BasicBD bd, boolean visualizzaRicerca,
			Map<String, String> parameters) throws ConsoleException {
		Map<String, ParamField<?>> infoGrafico = new HashMap<String, ParamField<?>>();

		if(this.infoRicercaMap == null){
			this.initInfoRicerca(uriInfo, bd);
		}

		String dataId = Utils.getInstance(this.getLanguage()).getMessageFromResourceBundle("statistiche.data.id");
		String colonneId = Utils.getInstance(this.getLanguage()).getMessageFromResourceBundle("statistiche.colonne.id");
		String avanzamentoId = Utils.getInstance(this.getLanguage()).getMessageFromResourceBundle("statistiche.avanzamento.id");
		String tipoIntervalloId = Utils.getInstance(this.getLanguage()).getMessageFromResourceBundle("statistiche.tipoIntervallo.id");

		InputNumber colonne = (InputNumber) this.infoRicercaMap.get(colonneId);
		colonne.setDefaultValue(null);
		infoGrafico.put(colonneId, colonne);

		InputNumber avanzamento = (InputNumber) this.infoRicercaMap.get(avanzamentoId);
		avanzamento.setDefaultValue(null);
		infoGrafico.put(avanzamentoId,avanzamento);

		InputDate data = (InputDate) this.infoRicercaMap.get(dataId);
		data.setDefaultValue(new Date());
		infoGrafico.put(dataId,data);

		SelectList<String> tipoIntervallo = (SelectList<String>) this.infoRicercaMap.get(tipoIntervalloId);
		tipoIntervallo.setDefaultValue(TipoIntervallo.GIORNALIERO.name());
		infoGrafico.put(tipoIntervalloId,tipoIntervallo); 

		return infoGrafico;
	}


	@Override
	public Object getSearchField(UriInfo uriInfo, List<RawParamValue> values, String fieldId, BasicBD bd)	throws WebApplicationException, ConsoleException { 	return null; }

	@Override
	public Object getExportField(UriInfo uriInfo, List<RawParamValue> values, String fieldId, BasicBD bd) throws WebApplicationException, ConsoleException { return null; }

	@Override
	public String getTitolo(DistribuzioneEsiti entry,BasicBD bd) {
		StringBuilder sb = new StringBuilder();

		sb.append(Utils.getInstance(this.getLanguage()).getMessageWithParamsFromResourceBundle(this.nomeServizio + ".label.titolo"));

		return sb.toString();
	}

	@Override
	public String getSottotitolo(DistribuzioneEsiti entry,BasicBD bd) {
		StringBuilder sb = new StringBuilder();
		return sb.toString();
	} 

	@Override
	public Map<String, Voce<String>> getVoci(DistribuzioneEsiti entry, BasicBD bd) throws ConsoleException {
		Map<String, Voce<String>> voci = new HashMap<String, Voce<String>>();
		return voci; 
	}

	@Override
	public InfoForm getInfoEsportazione(UriInfo uriInfo, BasicBD bd, Map<String, String> parameters) throws ConsoleException { 
		InfoForm infoEsportazione = null;
//		try{
//			if(this.darsService.isServizioAbilitatoLettura(bd, this.funzionalita)){
//				URI esportazione = this.getUriEsportazione(uriInfo, bd);
//				infoEsportazione = new InfoForm(esportazione);
//			}
//		}catch(ServiceException e){
//			throw new ConsoleException(e);
//		}
		return infoEsportazione;
	}

	@Override
	public InfoForm getInfoEsportazioneDettaglio(UriInfo uriInfo, BasicBD bd, DistribuzioneEsiti entry)	throws ConsoleException {	
		InfoForm infoEsportazione = null;
		return infoEsportazione;		
	}

	@Override
	public String esporta(List<Long> idsToExport, List<RawParamValue> rawValues, UriInfo uriInfo, BasicBD bd, ZipOutputStream zout)
			throws WebApplicationException, ConsoleException,ExportException {
		return null;
	}

	@Override
	public String esporta(Long idToExport, List<RawParamValue> rawValues, UriInfo uriInfo, BasicBD bd, ZipOutputStream zout) throws WebApplicationException, ConsoleException,ExportException { return null; }

}
