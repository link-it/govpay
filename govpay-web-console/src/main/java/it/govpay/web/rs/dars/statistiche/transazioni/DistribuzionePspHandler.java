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
import it.govpay.bd.anagrafica.filters.DominioFilter;
import it.govpay.bd.model.Dominio;
import it.govpay.bd.reportistica.statistiche.TransazioniBD;
import it.govpay.bd.reportistica.statistiche.filters.TransazioniFilter;
import it.govpay.model.reportistica.statistiche.DistribuzionePsp;
import it.govpay.web.rs.dars.anagrafica.domini.Domini;
import it.govpay.web.rs.dars.anagrafica.domini.DominiHandler;
import it.govpay.web.rs.dars.base.BaseDarsService;
import it.govpay.web.rs.dars.base.StatisticaDarsHandler;
import it.govpay.web.rs.dars.exception.ConsoleException;
import it.govpay.web.rs.dars.exception.ExportException;
import it.govpay.web.rs.dars.handler.IStatisticaDarsHandler;
import it.govpay.web.rs.dars.model.InfoForm;
import it.govpay.web.rs.dars.model.InfoForm.Sezione;
import it.govpay.web.rs.dars.model.RawParamValue;
import it.govpay.web.rs.dars.model.Voce;
import it.govpay.web.rs.dars.model.input.ParamField;
import it.govpay.web.rs.dars.model.input.base.SelectList;
import it.govpay.web.rs.dars.model.statistiche.Grafico;
import it.govpay.web.rs.dars.model.statistiche.Grafico.TipoGrafico;
import it.govpay.web.rs.dars.model.statistiche.PaginaGrafico;
import it.govpay.web.rs.dars.model.statistiche.Serie;
import it.govpay.web.utils.Utils;

public class DistribuzionePspHandler extends StatisticaDarsHandler<DistribuzionePsp> implements IStatisticaDarsHandler<DistribuzionePsp>{

	public static final String ANAGRAFICA_DEBITORE = "anagrafica";
	private SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm");  
	private SimpleDateFormat sdfGiorno = new SimpleDateFormat("dd/MM/yyyy");
	private SimpleDateFormat sdfMese = new SimpleDateFormat("MMMMM");  

	public DistribuzionePspHandler(Logger log, BaseDarsService darsService) { 
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

//			Calendar calendar = Calendar.getInstance();
//			calendar.setTime(filter.getData());
			SimpleDateFormat _sdf = null;
			switch (filter.getTipoIntervallo()) {
			case MENSILE:
//				calendar.add(Calendar.MONTH, - (filter.getLimit() -1 ));
				_sdf = this.sdfMese;
				break;
			case GIORNALIERO:
//				calendar.add(Calendar.DATE, - (filter.getLimit() -1 ));
				_sdf= this.sdfGiorno;
				break;
			case ORARIO:
//				calendar.add(Calendar.HOUR, - (filter.getLimit() -1 ));
				_sdf = this.sdf;
				break;
			}
//			Date start = calendar.getTime();
			String sottoTitolo = Utils.getInstance(this.getLanguage()).getMessageWithParamsFromResourceBundle(this.nomeServizio + ".label.sottotitolo",_sdf.format(filter.getData()));


			// visualizza la ricerca solo se i risultati sono > del limit
			InfoForm infoRicerca = this.getInfoRicerca(uriInfo, bd);
			Map<String, ParamField<?>> infoGrafico = this.getInfoGrafico(uriInfo, bd); 
			
			// valorizzo i valori da restitire al client
			infoGrafico = this.valorizzaInfoGrafico(uriInfo, bd, filter, infoGrafico);

			//List<DistribuzionePsp> distribuzioneEsiti = transazioniBD.getDistribuzionePsp(filter);
			
			List<DistribuzionePsp> distribuzioneEsiti = new ArrayList<DistribuzionePsp>();
			DistribuzionePsp d = new DistribuzionePsp("Unicredit SPA", 7420);
			distribuzioneEsiti.add(d);
			DistribuzionePsp d2 = new DistribuzionePsp("Poste Italiane", 3158);
			distribuzioneEsiti.add(d2);
			DistribuzionePsp d3 = new DistribuzionePsp("UBI Banca SPA", 4801);
			distribuzioneEsiti.add(d3);
			DistribuzionePsp d4 = new DistribuzionePsp("Paytipper SPA", 1790);
			distribuzioneEsiti.add(d4);
			DistribuzionePsp daltri = new DistribuzionePsp("Altri", 1126);
			distribuzioneEsiti.add(daltri);

			this.log.info("Esecuzione " + methodName + " completata.");

			PaginaGrafico paginaGrafico = new PaginaGrafico(this.titoloServizio, this.getInfoEsportazione(uriInfo,bd), infoRicerca,infoGrafico); 

			Grafico grafico = new Grafico(TipoGrafico.pie);

			grafico.setTitolo(Utils.getInstance(this.getLanguage()).getMessageFromResourceBundle(this.nomeServizio + ".label.titolo"));
			grafico.setSottotitolo(sottoTitolo); 
			grafico.setColoriAutomatici(true);

			if (distribuzioneEsiti != null && distribuzioneEsiti.size() > 0) {
				Serie<Long> serie1 = new Serie<Long>();
				for (DistribuzionePsp elemento : distribuzioneEsiti) {
					String dataPsp = elemento.getPsp();
					long transazioni = elemento.getTransazioni();

					grafico.getCategorie().add(Utils.getInstance(this.getLanguage()).getMessageWithParamsFromResourceBundle(this.nomeServizio + ".psp.label", dataPsp));
					//					grafico.getValoriX().add(dataElemento);
					serie1.getDati().add(transazioni);
					serie1.getTooltip().add(Utils.getInstance(this.getLanguage()).getMessageWithParamsFromResourceBundle(this.nomeServizio + ".psp.tooltip", dataPsp, transazioni));
				}

				grafico.getSerie().add(serie1);
			}

			paginaGrafico.setGrafico(grafico );

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
				//				filter.setIdDomini(idDomini);
			}
		}
		
		filter = (TransazioniFilter) popoloFiltroStatistiche(uriInfo, bd, filter);
		

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
		}
		return infoRicerca;
	}

	private void initInfoRicerca(UriInfo uriInfo, BasicBD bd) throws ConsoleException{
		if(this.infoRicercaMap == null){
			this.infoRicercaMap = new HashMap<String, ParamField<?>>();

			String idDominioId = Utils.getInstance(this.getLanguage()).getMessageFromResourceBundle(this.nomeServizio + ".idDominio.id");
			
			List<Voce<Long>> domini = new ArrayList<Voce<Long>>();
			// idDominio
			String idDominioLabel = Utils.getInstance(this.getLanguage()).getMessageFromResourceBundle(this.nomeServizio + ".idDominio.label");
			SelectList<Long> idDominio = new SelectList<Long>(idDominioId, idDominioLabel, null, false, false, true, domini);
			this.infoRicercaMap.put(idDominioId, idDominio);
			
			this.initInfoGrafico(uriInfo,bd);
		}
	}

	@Override
	public Object getSearchField(UriInfo uriInfo, List<RawParamValue> values, String fieldId, BasicBD bd)	throws WebApplicationException, ConsoleException { 	return null; }

	@Override
	public Object getExportField(UriInfo uriInfo, List<RawParamValue> values, String fieldId, BasicBD bd) throws WebApplicationException, ConsoleException { return null; }

	@Override
	public String getTitolo(DistribuzionePsp entry,BasicBD bd) {
		StringBuilder sb = new StringBuilder();

		sb.append(Utils.getInstance(this.getLanguage()).getMessageWithParamsFromResourceBundle(this.nomeServizio + ".label.titolo"));

		return sb.toString();
	}

	@Override
	public String getSottotitolo(DistribuzionePsp entry,BasicBD bd) {
		StringBuilder sb = new StringBuilder();
		return sb.toString();
	} 

	@Override
	public Map<String, Voce<String>> getVoci(DistribuzionePsp entry, BasicBD bd) throws ConsoleException {
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
	public InfoForm getInfoEsportazioneDettaglio(UriInfo uriInfo, BasicBD bd, DistribuzionePsp entry)	throws ConsoleException {	
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
