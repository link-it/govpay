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

import java.io.ByteArrayOutputStream;
import java.net.URI;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.UriInfo;

import org.apache.commons.lang.StringUtils;
import org.apache.logging.log4j.Logger;
import org.openspcoop2.generic_project.exception.ServiceException;
import org.openspcoop2.generic_project.expression.SortOrder;
import org.openspcoop2.utils.csv.Printer;

import it.gov.digitpa.schemas._2011.pagamenti.CtRicevutaTelematica;
import it.gov.digitpa.schemas._2011.pagamenti.revoche.CtEsitoRevoca;
import it.govpay.bd.BasicBD;
import it.govpay.bd.FilterSortWrapper;
import it.govpay.bd.anagrafica.AclBD;
import it.govpay.bd.anagrafica.AnagraficaManager;
import it.govpay.bd.anagrafica.DominiBD;
import it.govpay.bd.anagrafica.PspBD;
import it.govpay.bd.anagrafica.filters.DominioFilter;
import it.govpay.bd.anagrafica.filters.PspFilter;
import it.govpay.bd.model.Dominio;
import it.govpay.bd.model.Psp;
import it.govpay.bd.model.Rpt;
import it.govpay.bd.model.Rr;
import it.govpay.bd.model.UnitaOperativa;
import it.govpay.bd.model.Versamento;
import it.govpay.bd.pagamento.EventiBD;
import it.govpay.bd.pagamento.IuvBD;
import it.govpay.bd.pagamento.RptBD;
import it.govpay.bd.pagamento.RrBD;
import it.govpay.bd.pagamento.VersamentiBD;
import it.govpay.bd.pagamento.filters.EventiFilter;
import it.govpay.bd.pagamento.filters.IuvFilter;
import it.govpay.bd.pagamento.filters.RptFilter;
import it.govpay.bd.pagamento.filters.RrFilter;
import it.govpay.bd.pagamento.filters.VersamentoFilter;
import it.govpay.bd.reportistica.EstrattiContoBD;
import it.govpay.bd.reportistica.filters.EstrattoContoFilter;
import it.govpay.core.utils.CSVUtils;
import it.govpay.core.utils.JaxbUtils;
import it.govpay.core.utils.RtUtils;
import it.govpay.model.Acl;
import it.govpay.model.Acl.Tipo;
import it.govpay.model.EstrattoConto;
import it.govpay.model.Evento;
import it.govpay.model.Iuv;
import it.govpay.model.Operatore;
import it.govpay.model.Operatore.ProfiloOperatore;
import it.govpay.model.Versamento.StatoVersamento;
import it.govpay.model.comparator.EstrattoContoComparator;
import it.govpay.stampe.pdf.er.ErPdf;
import it.govpay.stampe.pdf.rt.utils.RicevutaPagamentoUtils;
import it.govpay.web.rs.dars.anagrafica.domini.Domini;
import it.govpay.web.rs.dars.anagrafica.domini.DominiHandler;
import it.govpay.web.rs.dars.anagrafica.psp.PspHandler;
import it.govpay.web.rs.dars.base.BaseDarsService;
import it.govpay.web.rs.dars.base.DarsService;
import it.govpay.web.rs.dars.base.StatisticaDarsHandler;
import it.govpay.web.rs.dars.exception.ConsoleException;
import it.govpay.web.rs.dars.exception.ExportException;
import it.govpay.web.rs.dars.handler.IStatisticaDarsHandler;
import it.govpay.web.rs.dars.model.DarsResponse.EsitoOperazione;
import it.govpay.web.rs.dars.model.InfoForm;
import it.govpay.web.rs.dars.model.InfoForm.Sezione;
import it.govpay.web.rs.dars.model.RawParamValue;
import it.govpay.web.rs.dars.model.Voce;
import it.govpay.web.rs.dars.model.input.ParamField;
import it.govpay.web.rs.dars.model.input.base.SelectList;
import it.govpay.web.rs.dars.model.statistiche.Colori;
import it.govpay.web.rs.dars.model.statistiche.Grafico;
import it.govpay.web.rs.dars.model.statistiche.Grafico.TipoGrafico;
import it.govpay.web.rs.dars.model.statistiche.PaginaGrafico;
import it.govpay.web.rs.dars.model.statistiche.Serie;
import it.govpay.web.rs.dars.monitoraggio.eventi.Eventi;
import it.govpay.web.rs.dars.monitoraggio.eventi.EventiHandler;
import it.govpay.web.utils.ConsoleProperties;
import it.govpay.web.utils.Utils;

public class StatisticheTransazioniHandler extends StatisticaDarsHandler<Versamento> implements IStatisticaDarsHandler<Versamento>{

	public static final String ANAGRAFICA_DEBITORE = "anagrafica";
	private SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm");  

	public StatisticheTransazioniHandler(Logger log, BaseDarsService darsService) { 
		super(log, darsService);
	}


	@Override
	public PaginaGrafico getGrafico(UriInfo uriInfo, BasicBD bd) throws WebApplicationException, ConsoleException {
		String methodName = "getGrafico " + this.titoloServizio;
		try{	
			// Operazione consentita agli utenti registrati
			Operatore operatore = this.darsService.getOperatoreByPrincipal(bd); 

			Integer offset = this.getOffset(uriInfo);
			Integer limit = this.getLimit(uriInfo);
			URI esportazione = this.getUriEsportazione(uriInfo, bd); 

			this.log.info("Esecuzione " + methodName + " in corso..."); 

			VersamentiBD versamentiBD = new VersamentiBD(bd);

			VersamentoFilter filter = versamentiBD.newFilter();
			filter.setOffset(offset);
			filter.setLimit(limit);
			FilterSortWrapper fsw = new FilterSortWrapper();
			fsw.setField(it.govpay.orm.Versamento.model().DATA_ORA_ULTIMO_AGGIORNAMENTO);
			fsw.setSortOrder(SortOrder.DESC);
			filter.getFilterSortList().add(fsw);

			boolean eseguiRicerca = this.popolaFiltroRicerca(uriInfo, versamentiBD, operatore, filter);

			// visualizza la ricerca solo se i risultati sono > del limit
			InfoForm infoRicerca = this.getInfoRicerca(uriInfo, bd);

			this.log.info("Esecuzione " + methodName + " completata.");

			PaginaGrafico paginaGrafico = new PaginaGrafico(this.titoloServizio, esportazione, infoRicerca);


			Grafico grafico = new Grafico(TipoGrafico.bar);

			grafico.setLabelX("Data");
			grafico.setLabelY("Numero Transazioni");
			grafico.setTitolo("Numero Transazioni");
			grafico.setSottotitolo("Dal 23 Maggio al 28 Maggio"); 
			grafico.getColori().addAll(Colori.getColoriTransazioni());
			grafico.getCategorie().add("Transazioni completate senza Pagamento");
			grafico.getCategorie().add("Transazioni completate con un Pagamento");
			grafico.getCategorie().add("Transazioni rifiutate");
			grafico.getCategorie().add("Transazioni in corso");
			grafico.getValoriX().add("23 Maggio");
			grafico.getValoriX().add("24 Maggio");
			grafico.getValoriX().add("25 Maggio");
			grafico.getValoriX().add("26 Maggio");
			grafico.getValoriX().add("27 Maggio");
			grafico.getValoriX().add("28 Maggio");

			Serie<Integer> serie1 = new Serie<Integer>();
			serie1.getDati().add(10);
			serie1.getDati().add(3);
			serie1.getDati().add(4);

			serie1.getDati().add(10);
			serie1.getDati().add(3);
			serie1.getDati().add(4);

			serie1.getTooltip().add("10 Transazioni completate senza Pagamento");
			serie1.getTooltip().add("3 Transazioni completate senza Pagamento");
			serie1.getTooltip().add("4 Transazioni completate senza Pagamento");

			serie1.getTooltip().add("10 Transazioni completate senza Pagamento");
			serie1.getTooltip().add("3 Transazioni completate senza Pagamento");
			serie1.getTooltip().add("4 Transazioni completate senza Pagamento");


			grafico.getSerie().add(serie1);

			Serie<Integer> serie2 = new Serie<Integer>(); 
			serie2.getDati().add(1);
			serie2.getDati().add(4);
			serie2.getDati().add(6);

			serie2.getDati().add(1);
			serie2.getDati().add(4);
			serie2.getDati().add(6);

			serie2.getTooltip().add("1 Transazioni completate con un Pagamento");
			serie2.getTooltip().add("4 Transazioni completate con un Pagamento");
			serie2.getTooltip().add("6 Transazioni completate con un Pagamento");

			serie2.getTooltip().add("1 Transazioni completate con un Pagamento");
			serie2.getTooltip().add("4 Transazioni completate con un Pagamento");
			serie2.getTooltip().add("6 Transazioni completate con un Pagamento");


			grafico.getSerie().add(serie2);

			Serie<Integer> serie3 = new Serie<Integer>(); 

			serie3.getDati().add(5);
			serie3.getDati().add(13);
			serie3.getDati().add(1);

			serie3.getDati().add(5);
			serie3.getDati().add(13);
			serie3.getDati().add(1);

			serie3.getTooltip().add("5 Transazioni rifiutate");
			serie3.getTooltip().add("13 Transazioni rifiutate");
			serie3.getTooltip().add("1 Transazioni rifiutate");

			serie3.getTooltip().add("5 Transazioni rifiutate");
			serie3.getTooltip().add("13 Transazioni rifiutate");
			serie3.getTooltip().add("1 Transazioni rifiutate");

			grafico.getSerie().add(serie3);

			Serie<Integer> serie4 = new Serie<Integer>(); 

			serie4.getDati().add(2);
			serie4.getDati().add(6);
			serie4.getDati().add(8);

			serie4.getDati().add(2);
			serie4.getDati().add(6);
			serie4.getDati().add(8);

			serie4.getTooltip().add("2 Transazioni in corso");
			serie4.getTooltip().add("6 Transazioni in corso");
			serie4.getTooltip().add("8 Transazioni in corso");

			serie4.getTooltip().add("2 Transazioni in corso");
			serie4.getTooltip().add("6 Transazioni in corso");
			serie4.getTooltip().add("8 Transazioni in corso");

			grafico.getSerie().add(serie4);

			paginaGrafico.setGrafico(grafico );

			return paginaGrafico;
		}catch(WebApplicationException e){
			throw e;
		}catch(Exception e){
			throw new ConsoleException(e);
		}
	}

	private boolean popolaFiltroRicerca(UriInfo uriInfo, BasicBD bd, Operatore operatore , VersamentoFilter filter) throws ConsoleException, Exception {
		ProfiloOperatore profilo = operatore.getProfilo();
		boolean isAdmin = profilo.equals(ProfiloOperatore.ADMIN);
		AclBD aclBD = new AclBD(bd);
		List<Acl> aclOperatore = aclBD.getAclOperatore(operatore.getId());
		List<Long> idDomini = new ArrayList<Long>();
		List<Long> idPsps = new ArrayList<Long>();
		boolean eseguiRicerca = true; // isAdmin;

		String idDominioId = Utils.getInstance(this.getLanguage()).getMessageFromResourceBundle(this.nomeServizio + ".idDominio.id");
		String idDominio = this.getParameter(uriInfo, idDominioId, String.class);
		if(StringUtils.isNotEmpty(idDominio)){
			long idDom = -1l;
			try{
				idDom = Long.parseLong(idDominio);
			}catch(Exception e){ idDom = -1l;	}
			if(idDom > 0){
				idDomini.add(idDom);
				filter.setIdDomini(idDomini);
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
				idPsps.add(idPspL);
				//	filter.setIdPsp(idPsps);
			}
		}

		String dataInizioId = Utils.getInstance(this.getLanguage()).getMessageFromResourceBundle(this.nomeServizio + ".dataInizio.id");
		String dataInizio = this.getParameter(uriInfo, dataInizioId, String.class);
		if(StringUtils.isNotEmpty(dataInizio)){
			//			filter.setDataInizio(this.convertJsonStringToDate(dataInizio));
		}

		String dataFineId = Utils.getInstance(this.getLanguage()).getMessageFromResourceBundle(this.nomeServizio + ".dataFine.id");
		String dataFine = this.getParameter(uriInfo, dataFineId, String.class);
		if(StringUtils.isNotEmpty(dataFine)){
			//			filter.setDataFine(this.convertJsonStringToDate(dataFine));
		}

		if(!isAdmin && idDomini.isEmpty()){
			boolean vediTuttiDomini = false;

			for(Acl acl: aclOperatore) {
				if(Tipo.DOMINIO.equals(acl.getTipo())) {
					if(acl.getIdDominio() == null) {
						vediTuttiDomini = true;
						break;
					} else {
						idDomini.add(acl.getIdDominio());
					}
				}
			}
			if(!vediTuttiDomini) {
				if(idDomini.isEmpty()) {
					eseguiRicerca = false;
				} else {
					filter.setIdDomini(idDomini);
				}
			}
		}

		return eseguiRicerca ;
	}

	private boolean popolaFiltroRicerca(List<RawParamValue> rawValues, BasicBD bd, Operatore operatore , VersamentoFilter filter) throws ConsoleException, Exception {
		ProfiloOperatore profilo = operatore.getProfilo();
		boolean isAdmin = profilo.equals(ProfiloOperatore.ADMIN);
		AclBD aclBD = new AclBD(bd);
		List<Acl> aclOperatore = aclBD.getAclOperatore(operatore.getId());
		List<Long> idDomini = new ArrayList<Long>();
		List<Long> idPsps = new ArrayList<Long>();
		boolean eseguiRicerca = true; // isAdmin;

		String idPspId = Utils.getInstance(this.getLanguage()).getMessageFromResourceBundle(this.nomeServizio + ".idPsp.id");
		String idPsp = Utils.getValue(rawValues, idPspId);
		if(StringUtils.isNotEmpty(idPsp)){
			long idPspL = -1l;
			try{
				idPspL = Long.parseLong(idPsp);
			}catch(Exception e){ idPspL = -1l;	}
			if(idPspL > 0){
				idPsps.add(idPspL);
				//	filter.setIdPsp(idPsps);
			}
		}

		String idDominioId = Utils.getInstance(this.getLanguage()).getMessageFromResourceBundle(this.nomeServizio + ".idDominio.id");
		String idDominio = Utils.getValue(rawValues, idDominioId);
		if(StringUtils.isNotEmpty(idDominio)){
			long idDom = -1l;
			try{
				idDom = Long.parseLong(idDominio);
			}catch(Exception e){ idDom = -1l;	}
			if(idDom > 0){
				idDomini.add(idDom);
				filter.setIdDomini(idDomini);
			}
		}

		String dataInizioId = Utils.getInstance(this.getLanguage()).getMessageFromResourceBundle(this.nomeServizio + ".dataInizio.id");
		String dataInizio = Utils.getValue(rawValues, dataInizioId);
		if(StringUtils.isNotEmpty(dataInizio)){
			//				filter.setDataInizio(this.convertJsonStringToDate(dataInizio));
		}

		String dataFineId = Utils.getInstance(this.getLanguage()).getMessageFromResourceBundle(this.nomeServizio + ".dataFine.id");
		String dataFine = Utils.getValue(rawValues, dataFineId);
		if(StringUtils.isNotEmpty(dataFine)){
			//				filter.setDataFine(this.convertJsonStringToDate(dataFine));
		}

		if(!isAdmin && idDomini.isEmpty()){
			boolean vediTuttiDomini = false;

			for(Acl acl: aclOperatore) {
				if(Tipo.DOMINIO.equals(acl.getTipo())) {
					if(acl.getIdDominio() == null) {
						vediTuttiDomini = true;
						break;
					} else {
						idDomini.add(acl.getIdDominio());
					}
				}
			}
			if(!vediTuttiDomini) {
				if(idDomini.isEmpty()) {
					eseguiRicerca = false;
				} else {
					filter.setIdDomini(idDomini);
				}
			}
		}

		return eseguiRicerca  ;
	}

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

				Operatore operatore = this.darsService.getOperatoreByPrincipal(bd); 
				ProfiloOperatore profilo = operatore.getProfilo();
				boolean isAdmin = profilo.equals(ProfiloOperatore.ADMIN);

				// idDominio
				List<Voce<Long>> domini = new ArrayList<Voce<Long>>();

				DominiBD dominiBD = new DominiBD(bd);
				DominioFilter filter;
				try {
					filter = dominiBD.newFilter();
					boolean eseguiRicerca = true;
					if(isAdmin){

					} else {
						AclBD aclBD = new AclBD(bd);
						List<Acl> aclOperatore = aclBD.getAclOperatore(operatore.getId());

						boolean vediTuttiDomini = false;
						List<Long> idDomini = new ArrayList<Long>();
						for(Acl acl: aclOperatore) {
							if(Tipo.DOMINIO.equals(acl.getTipo())) {
								if(acl.getIdDominio() == null) {
									vediTuttiDomini = true;
									break;
								} else {
									idDomini.add(acl.getIdDominio());
								}
							}
						}
						if(!vediTuttiDomini) {
							if(idDomini.isEmpty()) {
								eseguiRicerca = false;
							} else {
								filter.setIdDomini(idDomini);
							}
						}
					}



					if(eseguiRicerca) {
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
					}else {
						domini.add(new Voce<Long>(Utils.getInstance(this.getLanguage()).getMessageFromResourceBundle("commons.label.qualsiasi"), -1L));
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

		}
	}

	@Override
	public Object getSearchField(UriInfo uriInfo, List<RawParamValue> values, String fieldId, BasicBD bd)	throws WebApplicationException, ConsoleException { 	return null; }

	@Override
	public String getTitolo(Versamento entry,BasicBD bd) {
		StringBuilder sb = new StringBuilder();

		sb.append(Utils.getInstance(this.getLanguage()).getMessageWithParamsFromResourceBundle(this.nomeServizio + ".label.titolo"));

		return sb.toString();
	}

	@Override
	public String getSottotitolo(Versamento entry,BasicBD bd) {
		StringBuilder sb = new StringBuilder();

		Date dataUltimoAggiornamento = entry.getDataUltimoAggiornamento();

		sb.append(Utils.getInstance(this.getLanguage()).getMessageWithParamsFromResourceBundle(this.nomeServizio + ".label.sottotitolo", this.sdf.format(dataUltimoAggiornamento), this.sdf.format(dataUltimoAggiornamento)));

		return sb.toString();
	} 

	@Override
	public Map<String, Voce<String>> getVoci(Versamento entry, BasicBD bd) throws ConsoleException {
		Map<String, Voce<String>> voci = new HashMap<String, Voce<String>>();
		return voci; 
	}

	@Override
	public String esporta(List<Long> idsToExport, List<RawParamValue> rawValues, UriInfo uriInfo, BasicBD bd, ZipOutputStream zout)
			throws WebApplicationException, ConsoleException,ExportException {
		StringBuffer sb = new StringBuffer();
		if(idsToExport != null && idsToExport.size() > 0) {
			for (Long long1 : idsToExport) {

				if(sb.length() > 0) {
					sb.append(", ");
				}

				sb.append(long1);
			}
		}

		Printer printer  = null;
		String methodName = "esporta " + this.titoloServizio + "[" + sb.toString() + "]";

		int numeroZipEntries = 0;
		String pathLoghi = ConsoleProperties.getInstance().getPathEstrattoContoPdfLoghi();

		//		if(idsToExport.size() == 1) {
		//			return this.esporta(idsToExport.get(0), uriInfo, bd, zout);
		//		} 

		String fileName = "Export.zip";
		try{
			this.log.info("Esecuzione " + methodName + " in corso...");
			Operatore operatore = this.darsService.getOperatoreByPrincipal(bd); 
			int limit = ConsoleProperties.getInstance().getNumeroMassimoElementiExport();
			boolean simpleSearch = Utils.containsParameter(rawValues, DarsService.SIMPLE_SEARCH_PARAMETER_ID);
			VersamentiBD versamentiBD = new VersamentiBD(bd);
			RptBD rptBD = new RptBD(bd);
			EventiBD eventiBd = new EventiBD(bd);
			Eventi eventiDars = new Eventi();
			it.govpay.core.business.EstrattoConto estrattoContoBD = new it.govpay.core.business.EstrattoConto(bd);
			EventiHandler eventiDarsHandler = (EventiHandler) eventiDars.getDarsHandler(); 

			Map<String, List<Long>> mappaInputEstrattoConto = new HashMap<String, List<Long>>();
			Map<String, Dominio> mappaInputDomini = new HashMap<String, Dominio>();

			VersamentoFilter filter = versamentiBD.newFilter(simpleSearch);

			// se ho ricevuto anche gli id li utilizzo per fare il check della count
			if(idsToExport != null && idsToExport.size() > 0) 
				filter.setIdVersamento(idsToExport);

			boolean eseguiRicerca = this.popolaFiltroRicerca(rawValues, bd, operatore, filter);

			if(!eseguiRicerca){
				List<String> msg = new ArrayList<String>();
				msg.add(Utils.getInstance(this.getLanguage()).getMessageFromResourceBundle(this.nomeServizio+".esporta.operazioneNonPermessa"));
				throw new ExportException(msg, EsitoOperazione.ERRORE);
			}

			long count = versamentiBD.count(filter);

			if(count < 1){
				List<String> msg = new ArrayList<String>();
				msg.add(Utils.getInstance(this.getLanguage()).getMessageFromResourceBundle(this.nomeServizio+".esporta.nessunElementoDaEsportare"));
				throw new ExportException(msg, EsitoOperazione.ERRORE);
			} 

			if(count > ConsoleProperties.getInstance().getNumeroMassimoElementiExport()){
				List<String> msg = new ArrayList<String>();
				msg.add(Utils.getInstance(this.getLanguage()).getMessageFromResourceBundle(this.nomeServizio+".esporta.numeroElementiDaEsportareSopraSogliaMassima"));
				throw new ExportException(msg, EsitoOperazione.ERRORE);
			}

			filter.setOffset(0);
			filter.setLimit(limit);
			FilterSortWrapper fsw = new FilterSortWrapper();
			fsw.setField(it.govpay.orm.Versamento.model().DATA_ORA_ULTIMO_AGGIORNAMENTO);
			fsw.setSortOrder(SortOrder.DESC);
			filter.getFilterSortList().add(fsw);

			List<Versamento> findAll = versamentiBD.findAll(filter);


			for (Versamento versamento : findAll) {

				// Prelevo il dominio
				UnitaOperativa uo  = AnagraficaManager.getUnitaOperativa(bd, versamento.getIdUo());
				Dominio dominio  = AnagraficaManager.getDominio(bd, uo.getIdDominio());

				String dirDominio = dominio.getCodDominio();

				// Aggrego i versamenti per dominio per generare gli estratti conto
				List<Long> idVersamentiDominio = null;
				if(mappaInputEstrattoConto.containsKey(dominio.getCodDominio())) {
					idVersamentiDominio = mappaInputEstrattoConto.get(dominio.getCodDominio());
				} else{
					idVersamentiDominio = new ArrayList<Long>();
					mappaInputEstrattoConto.put(dominio.getCodDominio(), idVersamentiDominio);
					mappaInputDomini.put(dominio.getCodDominio(), dominio);
				}
				idVersamentiDominio.add(versamento.getId());

				String dirVersamento = dirDominio + "/" + versamento.getCodVersamentoEnte();

				RptFilter rptFilter = rptBD.newFilter();
				FilterSortWrapper rptFsw = new FilterSortWrapper();
				rptFsw.setField(it.govpay.orm.RPT.model().DATA_MSG_RICHIESTA);
				rptFsw.setSortOrder(SortOrder.DESC);
				rptFilter.getFilterSortList().add(rptFsw);
				rptFilter.setIdVersamento(versamento.getId()); 

				RrBD rrBD = new RrBD(bd);
				FilterSortWrapper rrFsw = new FilterSortWrapper();
				rrFsw.setField(it.govpay.orm.RR.model().DATA_MSG_REVOCA);
				rrFsw.setSortOrder(SortOrder.DESC);

				List<Rpt> listaRpt = rptBD.findAll(rptFilter);
				if(listaRpt != null && listaRpt.size() >0 ) {
					for (Rpt rpt : listaRpt) {
						numeroZipEntries ++;

						String iuv = rpt.getIuv();
						String ccp = rpt.getCcp();

						String iuvCcpDir = dirVersamento + "/" + iuv;

						// non appendo il ccp nel caso sia uguale ad 'n/a' altrimenti crea un nuovo livello di directory;
						if(!StringUtils.equalsIgnoreCase(ccp, "n/a")) {
							iuvCcpDir = iuvCcpDir  + "_" + ccp;
						}

						String rptEntryName = iuvCcpDir + "/rpt_" + rpt.getCodMsgRichiesta() + ".xml"; 


						ZipEntry rptXml = new ZipEntry(rptEntryName);
						zout.putNextEntry(rptXml);
						zout.write(rpt.getXmlRpt());
						zout.closeEntry();

						if(rpt.getXmlRt() != null){
							numeroZipEntries ++;
							String rtEntryName = iuvCcpDir + "/rt_" + rpt.getCodMsgRichiesta() + ".xml";
							ZipEntry rtXml = new ZipEntry(rtEntryName);
							zout.putNextEntry(rtXml);
							zout.write(rpt.getXmlRt());
							zout.closeEntry();

							// RT in formato pdf
							String tipoFirma = rpt.getFirmaRichiesta().getCodifica();
							byte[] rtByteValidato = RtUtils.validaFirma(tipoFirma, rpt.getXmlRt(), dominio.getCodDominio());
							CtRicevutaTelematica rt = JaxbUtils.toRT(rtByteValidato);
							ByteArrayOutputStream baos = new ByteArrayOutputStream();
							String auxDigit = dominio.getAuxDigit() + "";
							String applicationCode = String.format("%02d", dominio.getStazione(bd).getApplicationCode());
							RicevutaPagamentoUtils.getPdfRicevutaPagamento(pathLoghi, rt, versamento, auxDigit, applicationCode, baos, this.log);
							String rtPdfEntryName = iuvCcpDir + "/ricevuta_pagamento.pdf";
							numeroZipEntries ++;
							ZipEntry rtPdf = new ZipEntry(rtPdfEntryName);
							zout.putNextEntry(rtPdf);
							zout.write(baos.toByteArray());
							zout.closeEntry();
						}

						// Eventi
						String entryEventiCSV =  iuvCcpDir + "/eventi.csv";

						EventiFilter eventiFilter = eventiBd.newFilter();
						eventiFilter.setCodDominio(dominio.getCodDominio());
						eventiFilter.setIuv(iuv);
						eventiFilter.setCcp(ccp);
						FilterSortWrapper fswEventi = new FilterSortWrapper();
						fswEventi.setField(it.govpay.orm.Evento.model().DATA_1);
						fswEventi.setSortOrder(SortOrder.ASC);
						eventiFilter.getFilterSortList().add(fswEventi);

						List<Evento> findAllEventi = eventiBd.findAll(eventiFilter);
						ByteArrayOutputStream baos = new ByteArrayOutputStream();
						eventiDarsHandler.scriviCSVEventi(baos, findAllEventi);

						ZipEntry eventiCSV = new ZipEntry(entryEventiCSV);
						zout.putNextEntry(eventiCSV);
						zout.write(baos.toByteArray());
						zout.closeEntry();

						RrFilter rrFilter = rrBD.newFilter();
						rrFilter.getFilterSortList().add(rrFsw);
						rrFilter.setIdRpt(rpt.getId()); 
						List<Rr> findAllRR = rrBD.findAll(rrFilter);
						if(findAllRR != null && findAllRR.size() > 0){
							for (Rr rr : findAllRR) {
								numeroZipEntries ++;
								String rrEntryName = iuvCcpDir + "/rr_" + rr.getCodMsgRevoca() + ".xml"; 

								ZipEntry rrXml = new ZipEntry(rrEntryName);
								zout.putNextEntry(rrXml);
								zout.write(rr.getXmlRr());
								zout.closeEntry();

								if(rr.getXmlEr() != null){
									numeroZipEntries ++;
									String erEntryName = iuvCcpDir + "/er_" + rr.getCodMsgRevoca() + ".xml"; 
									ZipEntry rtXml = new ZipEntry(erEntryName);
									zout.putNextEntry(rtXml);
									zout.write(rr.getXmlEr());
									zout.closeEntry();

									// ER in formato pdf
									CtEsitoRevoca er = JaxbUtils.toER(rr.getXmlEr());
									String causale = versamento.getCausaleVersamento().getSimple();
									baos = new ByteArrayOutputStream();
									Dominio dominio3 = AnagraficaManager.getDominio(bd, er.getDominio().getIdentificativoDominio());
									ErPdf.getPdfEsitoRevoca(pathLoghi, er, dominio3, dominio3.getAnagrafica(bd), causale,baos,this.log);

									String erPdfEntryName = iuvCcpDir + "/esito_revoca.pdf";
									numeroZipEntries ++;
									ZipEntry erPdf = new ZipEntry(erPdfEntryName);
									zout.putNextEntry(erPdf);
									zout.write(baos.toByteArray());
									zout.closeEntry();
								}
							}
						}
					}
				}
			}

			List<it.govpay.core.business.model.EstrattoConto> listInputEstrattoConto = new ArrayList<it.govpay.core.business.model.EstrattoConto>();
			for (String codDominio : mappaInputEstrattoConto.keySet()) {
				it.govpay.core.business.model.EstrattoConto input =  it.govpay.core.business.model.EstrattoConto.creaEstrattoContoVersamentiPDF(mappaInputDomini.get(codDominio), mappaInputEstrattoConto.get(codDominio)); 
				listInputEstrattoConto.add(input);
			}


			List<it.govpay.core.business.model.EstrattoConto> listOutputEstattoConto = estrattoContoBD.getEstrattoContoVersamenti(listInputEstrattoConto,pathLoghi);

			for (it.govpay.core.business.model.EstrattoConto estrattoContoOutput : listOutputEstattoConto) {
				Map<String, ByteArrayOutputStream> estrattoContoVersamenti = estrattoContoOutput.getOutput(); 
				for (String nomeEntry : estrattoContoVersamenti.keySet()) {
					numeroZipEntries ++;
					ByteArrayOutputStream baos = estrattoContoVersamenti.get(nomeEntry);
					ZipEntry estrattoContoEntry = new ZipEntry(estrattoContoOutput.getDominio().getCodDominio() + "/" + nomeEntry);
					zout.putNextEntry(estrattoContoEntry);
					zout.write(baos.toByteArray());
					zout.closeEntry();
				}


			}

			// Estratto Conto in formato CSV
			EstrattiContoBD estrattiContoBD = new EstrattiContoBD(bd);
			EstrattoContoFilter ecFilter = estrattiContoBD.newFilter();
			ecFilter.setIdVersamento(idsToExport); 
			List<EstrattoConto> findAllEstrattoConto =  estrattiContoBD.estrattoContoFromIdVersamenti(ecFilter); 

			if(findAllEstrattoConto != null && findAllEstrattoConto.size() > 0){
				//ordinamento record
				Collections.sort(findAllEstrattoConto, new EstrattoContoComparator());
				numeroZipEntries ++;
				ByteArrayOutputStream baos  = new ByteArrayOutputStream();
				try{
					ZipEntry pagamentoCsv = new ZipEntry("estrattoConto.csv");
					zout.putNextEntry(pagamentoCsv);
					printer = new Printer(this.getFormat() , baos);
					printer.printRecord(CSVUtils.getEstrattoContoCsvHeader());
					for (EstrattoConto pagamento : findAllEstrattoConto) {
						printer.printRecord(CSVUtils.getEstrattoContoAsCsvRow(pagamento,this.sdf));
					}
				}finally {
					try{
						if(printer!=null){
							printer.close();
						}
					}catch (Exception e) {
						throw new Exception("Errore durante la chiusura dello stream ",e);
					}
				}
				zout.write(baos.toByteArray());
				zout.closeEntry();
			}

			// se non ho inserito nessuna entry
			if(numeroZipEntries == 0){
				String noEntriesTxt = "/README";
				ZipEntry entryTxt = new ZipEntry(noEntriesTxt);
				zout.putNextEntry(entryTxt);
				zout.write("Non sono state trovate informazioni sui versamenti selezionati.".getBytes());
				zout.closeEntry();
			}


			zout.flush();
			zout.close();

			this.log.info("Esecuzione " + methodName + " completata.");

			return fileName;
		}catch(WebApplicationException e){
			throw e;
		}catch(Exception e){
			throw new ConsoleException(e);
		}
	}

	@Override
	public String esporta(Long idToExport, UriInfo uriInfo, BasicBD bd, ZipOutputStream zout) throws WebApplicationException, ConsoleException,ExportException { return null; }

}
