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
package it.govpay.web.rs.dars.monitoraggio.pagamenti;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.math.BigDecimal;
import java.net.URI;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.UriInfo;

import org.apache.commons.lang.StringUtils;
import org.apache.logging.log4j.Logger;
import org.jboss.resteasy.plugins.providers.multipart.MultipartFormDataInput;
import org.openspcoop2.generic_project.exception.NotFoundException;
import org.openspcoop2.generic_project.exception.ServiceException;
import org.openspcoop2.generic_project.expression.SortOrder;
import org.openspcoop2.utils.csv.Printer;

import it.gov.digitpa.schemas._2011.pagamenti.CtRicevutaTelematica;
import it.govpay.bd.BasicBD;
import it.govpay.bd.FilterSortWrapper;
import it.govpay.bd.anagrafica.AnagraficaManager;
import it.govpay.bd.anagrafica.DominiBD;
import it.govpay.bd.anagrafica.filters.DominioFilter;
import it.govpay.bd.model.Dominio;
import it.govpay.bd.model.Pagamento;
import it.govpay.bd.model.Rpt;
import it.govpay.bd.model.Rr;
import it.govpay.bd.model.SingoloVersamento;
import it.govpay.bd.model.UnitaOperativa;
import it.govpay.bd.model.Versamento;
import it.govpay.bd.pagamento.PagamentiBD;
import it.govpay.bd.pagamento.SingoliVersamentiBD;
import it.govpay.bd.pagamento.filters.PagamentoFilter;
import it.govpay.bd.reportistica.EstrattiContoBD;
import it.govpay.bd.reportistica.filters.EstrattoContoFilter;
import it.govpay.core.utils.CSVUtils;
import it.govpay.core.utils.JaxbUtils;
import it.govpay.core.utils.RtUtils;
import it.govpay.model.EstrattoConto;
import it.govpay.model.Pagamento.Stato;
import it.govpay.model.comparator.EstrattoContoComparator;
import it.govpay.stampe.pdf.rt.utils.RicevutaPagamentoUtils;
import it.govpay.web.rs.dars.anagrafica.domini.Domini;
import it.govpay.web.rs.dars.anagrafica.domini.DominiHandler;
import it.govpay.web.rs.dars.base.DarsHandler;
import it.govpay.web.rs.dars.base.DarsService;
import it.govpay.web.rs.dars.exception.ConsoleException;
import it.govpay.web.rs.dars.exception.DeleteException;
import it.govpay.web.rs.dars.exception.DuplicatedEntryException;
import it.govpay.web.rs.dars.exception.ExportException;
import it.govpay.web.rs.dars.exception.ValidationException;
import it.govpay.web.rs.dars.handler.IDarsHandler;
import it.govpay.web.rs.dars.model.DarsResponse.EsitoOperazione;
import it.govpay.web.rs.dars.model.Dettaglio;
import it.govpay.web.rs.dars.model.Elemento;
import it.govpay.web.rs.dars.model.Elenco;
import it.govpay.web.rs.dars.model.InfoForm;
import it.govpay.web.rs.dars.model.InfoForm.Sezione;
import it.govpay.web.rs.dars.model.RawParamValue;
import it.govpay.web.rs.dars.model.Voce;
import it.govpay.web.rs.dars.model.input.ParamField;
import it.govpay.web.rs.dars.model.input.RefreshableParamField;
import it.govpay.web.rs.dars.model.input.base.CheckButton;
import it.govpay.web.rs.dars.model.input.base.InputDate;
import it.govpay.web.rs.dars.model.input.base.InputText;
import it.govpay.web.rs.dars.model.input.base.SelectList;
import it.govpay.web.rs.dars.monitoraggio.pagamenti.input.EsportaRtPdf;
import it.govpay.web.rs.dars.monitoraggio.versamenti.Revoche;
import it.govpay.web.rs.dars.monitoraggio.versamenti.RevocheHandler;
import it.govpay.web.rs.dars.monitoraggio.versamenti.Transazioni;
import it.govpay.web.rs.dars.monitoraggio.versamenti.TransazioniHandler;
import it.govpay.web.rs.dars.monitoraggio.versamenti.Versamenti;
import it.govpay.web.rs.dars.monitoraggio.versamenti.VersamentiHandler;
import it.govpay.web.utils.ConsoleProperties;
import it.govpay.web.utils.Utils;

public class PagamentiHandler extends DarsHandler<Pagamento> implements IDarsHandler<Pagamento>{

	public static final String BOLLO = "BOLLO";
	public static final String STATO_RITARDO_INCASSO = "RITARDO_INCASSO";
	public static final String ANAGRAFICA_DEBITORE = "anagrafica";
	private SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");  
	
	private Integer sogliaGiorniRitardoPagamenti = null;

	public PagamentiHandler(Logger log, DarsService darsService) { 
		super(log, darsService);
		this.sogliaGiorniRitardoPagamenti = ConsoleProperties.getInstance().getSogliaGiorniRitardoPagamenti();
	}

	@Override
	public Elenco getElenco(UriInfo uriInfo, BasicBD bd) throws WebApplicationException, ConsoleException {
		String methodName = "getElenco " + this.titoloServizio;
		try{	
			// Operazione consentita solo agli utenti che hanno almeno un ruolo consentito per la funzionalita'
			this.darsService.checkDirittiServizio(bd, this.funzionalita);
			// Operazione consentita agli utenti registrati
			Integer offset = this.getOffset(uriInfo);
			Integer limit = this.getLimit(uriInfo);

			this.log.info("Esecuzione " + methodName + " in corso...");

			PagamentiBD pagamentiBD = new PagamentiBD(bd);
			Map<String, String> params = new HashMap<String, String>();

			boolean simpleSearch = this.containsParameter(uriInfo, DarsService.SIMPLE_SEARCH_PARAMETER_ID);
			PagamentoFilter filter = pagamentiBD.newFilter(simpleSearch);
			filter.setSogliaRitardo(ConsoleProperties.getInstance().getSogliaGiorniRitardoPagamenti());
			filter.setOffset(offset);
			filter.setLimit(limit);
			FilterSortWrapper fsw = new FilterSortWrapper();
			fsw.setField(it.govpay.orm.Pagamento.model().DATA_PAGAMENTO);
			fsw.setSortOrder(SortOrder.DESC);
			filter.getFilterSortList().add(fsw);

			boolean eseguiRicerca = popolaFiltroPagamenti(uriInfo, bd, params, simpleSearch, filter);

			String formatter = Utils.getInstance(this.getLanguage()).getMessageFromResourceBundle(this.nomeServizio+".elenco.formatter");
			long count = eseguiRicerca ? pagamentiBD.count(filter) : 0;
			eseguiRicerca = eseguiRicerca && count > 0;
			boolean visualizzaRicerca = true;

			if(params.size() > 0) {
				// se elemento correlato visualizza la ricerca solo se i risultati sono > del limit
				visualizzaRicerca = this.visualizzaRicerca(count, limit);
			}

			String simpleSearchPlaceholder = Utils.getInstance(this.getLanguage()).getMessageFromResourceBundle(this.nomeServizio+".simpleSearch.placeholder");
			Elenco elenco = new Elenco(this.titoloServizio, this.getInfoRicerca(uriInfo, bd,visualizzaRicerca,params),
					this.getInfoCreazione(uriInfo, bd), count, this.getInfoEsportazione(uriInfo, bd,params),
					this.getInfoCancellazione(uriInfo, bd,params),simpleSearchPlaceholder); 
			elenco.setNumeroMassimoElementiExport(null); 

			List<Pagamento> pagamenti = eseguiRicerca ? pagamentiBD.findAll(filter) : new ArrayList<Pagamento>();

			if(pagamenti != null && pagamenti.size() > 0){
				for (Pagamento entry : pagamenti) {
					Elemento elemento = this.getElemento(entry, entry.getId(), this.pathServizio,bd);
					elemento.setFormatter(formatter);
					elenco.getElenco().add(elemento);

					// aggiungo una copia per la revoca
					//					if(entry.getIdRr() != null){
					//						Elemento elementoRevoca = this.getElementoRevoca(entry, entry.getId(), this.pathServizio,bd);
					//						elementoRevoca.setFormatter(formatter);
					//						elenco.getElenco().add(elementoRevoca);	
					//					}
				}
			}

			this.log.info("Esecuzione " + methodName + " completata.");

			return elenco;
		}catch(WebApplicationException e){
			throw e;
		}catch(Exception e){
			throw new ConsoleException(e);
		}
	}

	private boolean popolaFiltroPagamenti(UriInfo uriInfo, BasicBD bd,  
			Map<String, String> params,   boolean simpleSearch,
			PagamentoFilter filter) throws ConsoleException, ServiceException, NotFoundException, Exception {
		Set<Long> setDomini = this.darsService.getIdDominiAbilitatiLetturaServizio(bd, this.funzionalita);
		boolean eseguiRicerca = !setDomini.isEmpty();
		List<Long> idDomini = new ArrayList<Long>();
		boolean elementoCorrelato = false;

		String versamentoId = Utils.getInstance(this.getLanguage()).getMessageFromResourceBundle(this.nomeServizio + ".idVersamento.id");
		String idVersamento = this.getParameter(uriInfo, versamentoId, String.class);

		String idRptId = Utils.getInstance(this.getLanguage()).getMessageFromResourceBundle(this.nomeServizio + ".idRpt.id");
		String idRpt = this.getParameter(uriInfo, idRptId, String.class);

		String idRrId = Utils.getInstance(this.getLanguage()).getMessageFromResourceBundle(this.nomeServizio + ".idRr.id");
		String idRr= this.getParameter(uriInfo, idRrId, String.class);

		String idIncassoId = Utils.getInstance(this.getLanguage()).getMessageFromResourceBundle(this.nomeServizio + ".idIncasso.id");
		String idIncasso= this.getParameter(uriInfo, idIncassoId, String.class);




		// elemento correlato al versamento.
		if(StringUtils.isNotEmpty(idVersamento)){
			params.put(versamentoId, idVersamento);
			elementoCorrelato = true;
			List<Long> idVersamentoL = new ArrayList<Long>();
			idVersamentoL.add(Long.parseLong(idVersamento));

			// Ricerca pagamenti associati 
			filter.setIdVersamenti(idVersamentoL);
		}

		// elemento correlato rpt
		if(StringUtils.isNotEmpty(idRpt)){
			params.put(idRptId, idRpt);
			elementoCorrelato = true;
			filter.setIdRpt(Long.parseLong(idRpt));
		}

		// elemento correlato rr
		if(StringUtils.isNotEmpty(idRr)){
			params.put(idRrId, idRr);
			elementoCorrelato = true;
			filter.setIdRpt(Long.parseLong(idRr)); 
		}

		// elemento correlato incassi			
		if(StringUtils.isNotEmpty(idIncasso)){
			params.put(idIncassoId, idIncasso);
			elementoCorrelato = true;
			filter.setIdIncasso(Long.parseLong(idIncasso)); 
		}

		if(simpleSearch) {
			// simplesearch
			String simpleSearchString = this.getParameter(uriInfo, DarsService.SIMPLE_SEARCH_PARAMETER_ID, String.class);
			if(StringUtils.isNotEmpty(simpleSearchString)) {
				filter.setSimpleSearchString(simpleSearchString);
				if(elementoCorrelato)
					params.put(DarsService.SIMPLE_SEARCH_PARAMETER_ID, simpleSearchString);
			}
		} else {
			String idDominioId = Utils.getInstance(this.getLanguage()).getMessageFromResourceBundle(this.nomeServizio + ".idDominio.id");
			String idDominio = this.getParameter(uriInfo, idDominioId, String.class);
			if(StringUtils.isNotEmpty(idDominio)){
				long idDom = -1l;
				try{
					idDom = Long.parseLong(idDominio);
				}catch(Exception e){ idDom = -1l;	}
				if(idDom > 0){
					idDomini.add(idDom);
					filter.setIdDomini(toListCodDomini(idDomini, bd));
					if(elementoCorrelato)
						params.put(idDominioId,idDominio);
				}
			}

			String statoId = Utils.getInstance(this.getLanguage()).getMessageFromResourceBundle(this.nomeServizio + ".stato.id");
			String stato = this.getParameter(uriInfo, statoId, String.class);
			if(StringUtils.isNotEmpty(stato)){
				List<String> stati = new ArrayList<String>();

				if(stato.equals(PagamentiHandler.STATO_RITARDO_INCASSO)) {
					if(this.sogliaGiorniRitardoPagamenti != null && this.sogliaGiorniRitardoPagamenti.intValue() > 0){
						Calendar tempo = Calendar.getInstance();
						tempo.setTime(new Date());
						tempo.add(Calendar.DAY_OF_YEAR, - this.sogliaGiorniRitardoPagamenti);
						filter.setDataPagamentoRitardoIncasso(tempo.getTime());
					}
				} else {
					if(stato.equals(Stato.PAGATO.name())) {
						stati.add(Stato.PAGATO.name());
						stati.add(Stato.PAGATO_SENZA_RPT.name());
					} else {
						stati.add(stato);
					}
					filter.setStati(stati);
				}

				if(elementoCorrelato)
					params.put(statoId,stato);
			}

			String dataInizioId = Utils.getInstance(this.getLanguage()).getMessageFromResourceBundle(this.nomeServizio + ".dataInizio.id");
			String dataInizio = this.getParameter(uriInfo, dataInizioId, String.class);
			if(StringUtils.isNotEmpty(dataInizio)){
				filter.setDataInizio(this.convertJsonStringToDataInizio(dataInizio));
				if(elementoCorrelato)
					params.put(dataInizioId,dataInizio);
			}

			String dataFineId = Utils.getInstance(this.getLanguage()).getMessageFromResourceBundle(this.nomeServizio + ".dataFine.id");
			String dataFine = this.getParameter(uriInfo, dataFineId, String.class);
			if(StringUtils.isNotEmpty(dataFine)){
				filter.setDataFine(this.convertJsonStringToDataFine(dataFine));
				if(elementoCorrelato)
					params.put(dataFineId,dataFine);
			}

			String iurId = Utils.getInstance(this.getLanguage()).getMessageFromResourceBundle(this.nomeServizio + ".iur.id");
			String iur = this.getParameter(uriInfo, iurId, String.class);
			if(StringUtils.isNotEmpty(iur)){
				filter.setIur(iur);
				if(elementoCorrelato)
					params.put(iurId,iur);
			}

			String iuvId = Utils.getInstance(this.getLanguage()).getMessageFromResourceBundle(this.nomeServizio + ".iuv.id");
			String iuv = this.getParameter(uriInfo, iuvId, String.class);
			if(StringUtils.isNotEmpty(iuv)){
				filter.setIuv(iuv);
				if(elementoCorrelato)
					params.put(iuvId,iuv);
			}

			String codSingoloVersamentoEnteId = Utils.getInstance(this.getLanguage()).getMessageFromResourceBundle(this.nomeServizio + ".codSingoloVersamentoEnte.id");
			String codSingoloVersamentoEnte = this.getParameter(uriInfo, codSingoloVersamentoEnteId, String.class);
			if(StringUtils.isNotEmpty(codSingoloVersamentoEnte)){
				filter.setCodSingoloVersamentoEnte(codSingoloVersamentoEnte);
				if(elementoCorrelato)
					params.put(codSingoloVersamentoEnteId,codSingoloVersamentoEnte);
			}
		}

		if(eseguiRicerca &&!setDomini.contains(-1L)){
			List<Long> lstCodDomini = new ArrayList<Long>();
			lstCodDomini.addAll(setDomini);
			idDomini.addAll(setDomini);
			filter.setIdDomini(toListCodDomini(idDomini, bd));
		}

		return eseguiRicerca;
	}

	private boolean popolaFiltroPagamenti(List<RawParamValue> rawValues,UriInfo uriInfo, BasicBD bd,  
			Map<String, String> params,   boolean simpleSearch,
			PagamentoFilter filter) throws ConsoleException, ServiceException, NotFoundException, Exception {
		Set<Long> setDomini = this.darsService.getIdDominiAbilitatiLetturaServizio(bd, this.funzionalita);
		boolean eseguiRicerca = !setDomini.isEmpty();
		List<Long> idDomini = new ArrayList<Long>();
		boolean elementoCorrelato = false;

		String versamentoId = Utils.getInstance(this.getLanguage()).getMessageFromResourceBundle(this.nomeServizio + ".idVersamento.id");
		String idVersamento = this.getParameter(uriInfo, versamentoId, String.class);

		String idRptId = Utils.getInstance(this.getLanguage()).getMessageFromResourceBundle(this.nomeServizio + ".idRpt.id");
		String idRpt = this.getParameter(uriInfo, idRptId, String.class);

		String idRrId = Utils.getInstance(this.getLanguage()).getMessageFromResourceBundle(this.nomeServizio + ".idRr.id");
		String idRr= this.getParameter(uriInfo, idRrId, String.class);

		String idIncassoId = Utils.getInstance(this.getLanguage()).getMessageFromResourceBundle(this.nomeServizio + ".idIncasso.id");
		String idIncasso= this.getParameter(uriInfo, idIncassoId, String.class);

		// elemento correlato al versamento.
		if(StringUtils.isNotEmpty(idVersamento)){
			params.put(versamentoId, idVersamento);
			elementoCorrelato = true;
			List<Long> idVersamentoL = new ArrayList<Long>();
			idVersamentoL.add(Long.parseLong(idVersamento));

			// Ricerca pagamenti associati 
			filter.setIdVersamenti(idVersamentoL);
		}

		// elemento correlato rpt
		if(StringUtils.isNotEmpty(idRpt)){
			params.put(idRptId, idRpt);
			elementoCorrelato = true;
			filter.setIdRpt(Long.parseLong(idRpt));
		}

		// elemento correlato rr
		if(StringUtils.isNotEmpty(idRr)){
			params.put(idRrId, idRr);
			elementoCorrelato = true;
			filter.setIdRpt(Long.parseLong(idRr)); 
		}

		// elemento correlato incassi			
		if(StringUtils.isNotEmpty(idIncasso)){
			params.put(idIncassoId, idIncasso);
			elementoCorrelato = true;
			filter.setIdIncasso(Long.parseLong(idIncasso)); 
		}

		if(simpleSearch) {
			// simplesearch
			String simpleSearchString = Utils.getValue(rawValues, DarsService.SIMPLE_SEARCH_PARAMETER_ID);
			if(StringUtils.isNotEmpty(simpleSearchString)) {
				filter.setSimpleSearchString(simpleSearchString);
				if(elementoCorrelato)
					params.put(DarsService.SIMPLE_SEARCH_PARAMETER_ID, simpleSearchString);
			}
		} else {
			String idDominioId = Utils.getInstance(this.getLanguage()).getMessageFromResourceBundle(this.nomeServizio + ".idDominio.id");
			String idDominio = Utils.getValue(rawValues, idDominioId);
			if(StringUtils.isNotEmpty(idDominio)){
				long idDom = -1l;
				try{
					idDom = Long.parseLong(idDominio);
				}catch(Exception e){ idDom = -1l;	}
				if(idDom > 0){
					idDomini.add(idDom);
					filter.setIdDomini(toListCodDomini(idDomini, bd));
					if(elementoCorrelato)
						params.put(idDominioId,idDominio);
				}
			}

			String statoId = Utils.getInstance(this.getLanguage()).getMessageFromResourceBundle(this.nomeServizio + ".stato.id");
			String stato = Utils.getValue(rawValues, statoId);
			if(StringUtils.isNotEmpty(stato)){
				List<String> stati = new ArrayList<String>();

				if(stato.equals(PagamentiHandler.STATO_RITARDO_INCASSO)) {
					if(this.sogliaGiorniRitardoPagamenti != null && this.sogliaGiorniRitardoPagamenti.intValue() > 0){
						Calendar tempo = Calendar.getInstance();
						tempo.setTime(new Date());
						tempo.add(Calendar.DAY_OF_YEAR, - this.sogliaGiorniRitardoPagamenti);
						filter.setDataPagamentoRitardoIncasso(tempo.getTime());
					}
				} else {
					if(stato.equals(Stato.PAGATO.name())) {
						stati.add(Stato.PAGATO.name());
						stati.add(Stato.PAGATO_SENZA_RPT.name());
					} else {
						stati.add(stato);
					}
					filter.setStati(stati);
				}
				if(elementoCorrelato)
					params.put(statoId,stato);
			}

			String dataInizioId = Utils.getInstance(this.getLanguage()).getMessageFromResourceBundle(this.nomeServizio + ".dataInizio.id");
			String dataInizio = Utils.getValue(rawValues, dataInizioId);
			if(StringUtils.isNotEmpty(dataInizio)){
				filter.setDataInizio(this.convertJsonStringToDataInizio(dataInizio));
				if(elementoCorrelato)
					params.put(dataInizioId,dataInizio);
			}

			String dataFineId = Utils.getInstance(this.getLanguage()).getMessageFromResourceBundle(this.nomeServizio + ".dataFine.id");
			String dataFine = Utils.getValue(rawValues, dataFineId);
			if(StringUtils.isNotEmpty(dataFine)){
				filter.setDataFine(this.convertJsonStringToDataFine(dataFine));
				if(elementoCorrelato)
					params.put(dataFineId,dataFine);
			}

			String iurId = Utils.getInstance(this.getLanguage()).getMessageFromResourceBundle(this.nomeServizio + ".iur.id");
			String iur = Utils.getValue(rawValues, iurId);
			if(StringUtils.isNotEmpty(iur)){
				filter.setIur(iur);
				if(elementoCorrelato)
					params.put(iurId,iur);
			}

			String iuvId = Utils.getInstance(this.getLanguage()).getMessageFromResourceBundle(this.nomeServizio + ".iuv.id");
			String iuv = Utils.getValue(rawValues, iuvId);
			if(StringUtils.isNotEmpty(iuv)){
				filter.setIuv(iuv);
				if(elementoCorrelato)
					params.put(iuvId,iuv);
			}

			String codSingoloVersamentoEnteId = Utils.getInstance(this.getLanguage()).getMessageFromResourceBundle(this.nomeServizio + ".codSingoloVersamentoEnte.id");
			String codSingoloVersamentoEnte = Utils.getValue(rawValues, codSingoloVersamentoEnteId);
			if(StringUtils.isNotEmpty(codSingoloVersamentoEnte)){
				filter.setCodSingoloVersamentoEnte(codSingoloVersamentoEnte);
				if(elementoCorrelato)
					params.put(codSingoloVersamentoEnteId,codSingoloVersamentoEnte);
			}
		}

		if(eseguiRicerca &&!setDomini.contains(-1L)){
			List<Long> lstCodDomini = new ArrayList<Long>();
			lstCodDomini.addAll(setDomini);
			idDomini.addAll(setDomini);
			filter.setIdDomini(toListCodDomini(idDomini, bd));
		}

		return eseguiRicerca;
	}

	@Override
	public Dettaglio getDettaglio(long id, UriInfo uriInfo, BasicBD bd)
			throws WebApplicationException, ConsoleException {
		String methodName = "dettaglio " + this.titoloServizio + "."+ id;

		try{
			this.log.info("Esecuzione " + methodName + " in corso...");
			// Operazione consentita solo ai ruoli con diritto di lettura
			this.darsService.checkDirittiServizioLettura(bd, this.funzionalita);

			Set<Long> setDomini = this.darsService.getIdDominiAbilitatiLetturaServizio(bd, this.funzionalita);
			boolean eseguiRicerca = !setDomini.isEmpty();

			PagamentiBD pagamentiBD = new PagamentiBD(bd);

			if(eseguiRicerca && !setDomini.contains(-1L)){
				List<Long> idDomini = new ArrayList<Long>();
				PagamentoFilter filter = pagamentiBD.newFilter();
				List<Long> lstCodDomini = new ArrayList<Long>();
				lstCodDomini.addAll(setDomini);
				idDomini.addAll(setDomini);
				filter.setIdDomini(toListCodDomini(idDomini, bd));
				List<Long> idPagamentiL = new ArrayList<Long>();
				idPagamentiL.add(id);
				filter.setIdPagamenti(idPagamentiL);

				long count = eseguiRicerca ? pagamentiBD.count(filter) : 0;
				eseguiRicerca = eseguiRicerca && count > 0;
			}


			Pagamento pagamento = eseguiRicerca ?  pagamentiBD.getPagamento(id) : null;

			InfoForm infoModifica = null;
			InfoForm infoCancellazione = pagamento != null ? this.getInfoCancellazioneDettaglio(uriInfo, bd, pagamento): null;
			InfoForm infoEsportazione = pagamento != null ? this.getInfoEsportazioneDettaglio(uriInfo, bd, pagamento): null;

			String titolo = Utils.getInstance(this.getLanguage()).getMessageFromResourceBundle(this.nomeServizio + ".dettaglio.label.titolo");
			Dettaglio dettaglio = new Dettaglio(titolo, infoEsportazione, infoCancellazione, infoModifica);

			if(pagamento != null){
				// Sezione root coi dati del pagamento
				it.govpay.web.rs.dars.model.Sezione sezioneRoot = dettaglio.getSezioneRoot();

				Date dataPagamento = pagamento.getDataPagamento();
				Stato stato = pagamento.getStato();
				String ibanAccredito = pagamento.getIbanAccredito();
				boolean bollo = ibanAccredito == null;

				String statoPagamento = Utils.getInstance(this.getLanguage()).getMessageFromResourceBundle(this.nomeServizio + ".stato."+Stato.INCASSATO.name());

				if(!bollo) {
					if(!stato.equals(Stato.INCASSATO)) {
						boolean inRitardo = false;
						if(this.sogliaGiorniRitardoPagamenti != null && this.sogliaGiorniRitardoPagamenti.intValue() > 0) {
							Calendar c = Calendar.getInstance();
							c.setTime(new Date());
							c.add(Calendar.DAY_OF_YEAR, - this.sogliaGiorniRitardoPagamenti.intValue()); 
							inRitardo = dataPagamento.getTime() < c.getTime().getTime();
						}

						if(inRitardo ) {
							statoPagamento = Utils.getInstance(this.getLanguage()).getMessageFromResourceBundle(this.nomeServizio + ".stato."+PagamentiHandler.STATO_RITARDO_INCASSO);
						} else {
							statoPagamento = Utils.getInstance(this.getLanguage()).getMessageFromResourceBundle(this.nomeServizio + ".stato."+stato.name());
						}
					}
				} else {
					// bollo e' sempre pagato
					statoPagamento = Utils.getInstance(this.getLanguage()).getMessageFromResourceBundle(this.nomeServizio + ".stato."+Stato.PAGATO.name());
				}

				sezioneRoot.addVoce(Utils.getInstance(this.getLanguage()).getMessageFromResourceBundle(this.nomeServizio + ".stato.label"),statoPagamento);

				if(StringUtils.isNotEmpty(pagamento.getCodDominio())){
					try{
						Dominio dominio = pagamento.getDominio(bd);
						Domini dominiDars = new Domini();
						Elemento elemento = ((DominiHandler)dominiDars.getDarsHandler()).getElemento(dominio, dominio.getId(), dominiDars.getPathServizio(), bd);
						sezioneRoot.addVoce(Utils.getInstance(this.getLanguage()).getMessageFromResourceBundle(this.nomeServizio + ".idDominio.label"), elemento.getTitolo());
					}catch(Exception e){
						sezioneRoot.addVoce(Utils.getInstance(this.getLanguage()).getMessageFromResourceBundle(this.nomeServizio + ".idDominio.label"), pagamento.getCodDominio());
					}
				}

				if(StringUtils.isNotEmpty(pagamento.getIuv()))
					sezioneRoot.addVoce(Utils.getInstance(this.getLanguage()).getMessageFromResourceBundle(this.nomeServizio + ".iuv.label"),pagamento.getIuv());
				if(StringUtils.isNotEmpty(pagamento.getIur()))
					sezioneRoot.addVoce(Utils.getInstance(this.getLanguage()).getMessageFromResourceBundle(this.nomeServizio + ".iur.label"),pagamento.getIur());

				SingoloVersamento singoloVersamento = pagamento.getSingoloVersamento(bd);

				if(singoloVersamento != null){
					sezioneRoot.addVoce(Utils.getInstance(this.getLanguage()).getMessageFromResourceBundle(this.nomeServizio + ".importoDovuto.label"),
							this.currencyUtils.getCurrencyAsEuro(singoloVersamento.getImportoSingoloVersamento()));
				}

				if(pagamento.getImportoPagato() != null)
					sezioneRoot.addVoce(Utils.getInstance(this.getLanguage()).getMessageFromResourceBundle(this.nomeServizio + ".importoPagato.label"),this.currencyUtils.getCurrencyAsEuro(pagamento.getImportoPagato()));

				if(pagamento.getCommissioniPsp() != null)
					sezioneRoot.addVoce(Utils.getInstance(this.getLanguage()).getMessageFromResourceBundle(this.nomeServizio + ".commissioniPsp.label"), this.currencyUtils.getCurrencyAsEuro((pagamento.getCommissioniPsp())));


				if(StringUtils.isNotEmpty(ibanAccredito))
					sezioneRoot.addVoce(Utils.getInstance(this.getLanguage()).getMessageFromResourceBundle(this.nomeServizio + ".ibanAccredito.label"),ibanAccredito);

				if(dataPagamento != null)
					sezioneRoot.addVoce(Utils.getInstance(this.getLanguage()).getMessageFromResourceBundle(this.nomeServizio + ".dataPagamento.label"),this.sdf.format(dataPagamento)); 

				//			TipoAllegato tipoAllegato = pagamento.getTipoAllegato();
				//			if(tipoAllegato!= null)
				//				sezioneRoot.addVoce(Utils.getInstance(this.getLanguage()).getMessageFromResourceBundle(this.nomeServizio + ".tipoAllegato.label"),
				//						Utils.getInstance(this.getLanguage()).getMessageFromResourceBundle(this.nomeServizio + ".tipoAllegato."+tipoAllegato.name()));

				if(singoloVersamento != null){
					Versamento versamento = singoloVersamento.getVersamento(pagamentiBD);
					Versamenti versamentiDars = new Versamenti();
					VersamentiHandler versamentiDarsHandler = (VersamentiHandler) versamentiDars.getDarsHandler();
					Elemento elemento = versamentiDarsHandler.getElemento(versamento, versamento.getId(), versamentiDars.getPathServizio(), bd);				
					sezioneRoot.addVoce(Utils.getInstance(this.getLanguage()).getMessageFromResourceBundle(this.nomeServizio + ".idVersamento.label"),
							Utils.getInstance(this.getLanguage()).getMessageFromResourceBundle("commons.label.visualizza"),elemento.getUri());

					sezioneRoot.addVoce(Utils.getInstance(this.getLanguage()).getMessageFromResourceBundle(this.nomeServizio + ".codSingoloVersamentoEnte.label"),singoloVersamento.getCodSingoloVersamentoEnte());
				}

				Rpt rpt = pagamento.getRpt(bd);
				if(rpt!= null){
					Transazioni transazioniDars = new Transazioni();
					TransazioniHandler transazioniDarsHandler = (TransazioniHandler) transazioniDars.getDarsHandler();
					Elemento elemento = transazioniDarsHandler.getElemento(rpt, rpt.getId(), transazioniDars.getPathServizio(),bd);
					sezioneRoot.addVoce(Utils.getInstance(this.getLanguage()).getMessageFromResourceBundle(this.nomeServizio + ".rpt.label"),
							Utils.getInstance(this.getLanguage()).getMessageFromResourceBundle("commons.label.visualizza"),elemento.getUri());
				}

				if(pagamento.getIdRr() != null){
					String etichettaRevoca = Utils.getInstance(this.getLanguage()).getMessageFromResourceBundle(this.nomeServizio + ".sezioneRevoca.titolo");
					it.govpay.web.rs.dars.model.Sezione sezioneRevoca = dettaglio.addSezione(etichettaRevoca);

					if(pagamento.getDataAcquisizioneRevoca()!= null)
						sezioneRoot.addVoce(Utils.getInstance(this.getLanguage()).getMessageFromResourceBundle(this.nomeServizio + ".dataAcquisizioneRevoca.label"),this.sdf.format(pagamento.getDataAcquisizioneRevoca())); 

					if(StringUtils.isNotEmpty(pagamento.getCausaleRevoca()))
						sezioneRevoca.addVoce(Utils.getInstance(this.getLanguage()).getMessageFromResourceBundle(this.nomeServizio + ".causaleRevoca.label"),pagamento.getCausaleRevoca());
					if(StringUtils.isNotEmpty(pagamento.getDatiRevoca()))
						sezioneRevoca.addVoce(Utils.getInstance(this.getLanguage()).getMessageFromResourceBundle(this.nomeServizio + ".datiRevoca.label"),pagamento.getDatiRevoca());
					if(StringUtils.isNotEmpty(pagamento.getEsitoRevoca()))
						sezioneRevoca.addVoce(Utils.getInstance(this.getLanguage()).getMessageFromResourceBundle(this.nomeServizio + ".esitoRevoca.label"),pagamento.getEsitoRevoca());
					if(StringUtils.isNotEmpty(pagamento.getDatiEsitoRevoca()))
						sezioneRevoca.addVoce(Utils.getInstance(this.getLanguage()).getMessageFromResourceBundle(this.nomeServizio + ".datiEsitoRevoca.label"),pagamento.getDatiEsitoRevoca());

					if(pagamento.getImportoRevocato() != null)
						sezioneRevoca.addVoce(Utils.getInstance(this.getLanguage()).getMessageFromResourceBundle(this.nomeServizio + ".importoRevocato.label"),
								this.currencyUtils.getCurrencyAsEuro(pagamento.getImportoRevocato()));

					Rr rr = pagamento.getRr(bd);
					if(rr != null){
						Revoche revocheDars = new Revoche();
						RevocheHandler revocheDarsHandler = (RevocheHandler) revocheDars.getDarsHandler();
						Elemento elemento = revocheDarsHandler.getElemento(rr, rr.getId(), revocheDars.getPathServizio(),bd);
						sezioneRevoca.addVoce(Utils.getInstance(this.getLanguage()).getMessageFromResourceBundle(this.nomeServizio + ".rr.label"),
								Utils.getInstance(this.getLanguage()).getMessageFromResourceBundle("commons.label.visualizza"),elemento.getUri());
					}
				}

			}
			this.log.info("Esecuzione " + methodName + " completata.");

			return dettaglio;
		}catch(WebApplicationException e){
			throw e;
		}catch(Exception e){
			throw new ConsoleException(e);
		}
	}

	public Elemento getElementoRevoca(Pagamento entry, Long id, String uriDettaglio, BasicBD bd) throws ConsoleException{
		try{
			String titolo = this.getTitolo(entry,bd);
			String sottotitolo = this.getSottotitolo(entry,bd);
			URI urlDettaglio = (id != null && uriDettaglio != null) ? Utils.creaUriConPath(uriDettaglio , id+"") : null;
			Elemento elemento = new Elemento(id, titolo, sottotitolo, urlDettaglio);
			elemento.setVoci(this.getVociRevoca(entry, bd)); 
			return elemento;
		}catch(Exception e) {throw new ConsoleException(e);}
	}

	@Override
	public String getTitolo(Pagamento entry,BasicBD bd) {
		Date dataPagamento = entry.getDataPagamento();
		BigDecimal importoPagato = entry.getImportoPagato();
		StringBuilder sb = new StringBuilder();

		String pagamentoString = 
				Utils.getInstance(this.getLanguage()).getMessageWithParamsFromResourceBundle(this.nomeServizio + ".label.titolo", this.currencyUtils.getCurrencyAsEuro(importoPagato) , this.sdf.format(dataPagamento)); 
		sb.append(pagamentoString);	
		return sb.toString();
	}

	@Override
	public String getSottotitolo(Pagamento entry,BasicBD bd) {
		StringBuilder sb = new StringBuilder();

		if(entry.getIdRr() != null){
			Date dataRevoca = entry.getDataPagamento();
			sb.append(Utils.getInstance(this.getLanguage()).getMessageWithParamsFromResourceBundle(this.nomeServizio + ".label.sottotitolo.revocato", this.sdf.format(dataRevoca)));
		}

		return sb.toString();
	}

	@Override
	public Map<String, Voce<String>> getVoci(Pagamento entry, BasicBD bd) throws ConsoleException { 
		Map<String, Voce<String>> valori = new HashMap<String, Voce<String>>();
		Date dataPagamento = entry.getDataPagamento();
		String ibanAccredito = entry.getIbanAccredito();
		boolean bollo = ibanAccredito == null;

		String statoPagamento = Stato.INCASSATO.name();
		String statoPagamentoLabel = Utils.getInstance(this.getLanguage()).getMessageWithParamsFromResourceBundle(this.nomeServizio + ".label.sottotitolo."+Stato.INCASSATO.name(), this.sdf.format(dataPagamento));

		Stato stato = entry.getStato();

		if(!bollo) {
			if(!stato.equals(Stato.INCASSATO)) {
				boolean inRitardo = false;
				Integer sogliaGiorniRitardoPagamenti = ConsoleProperties.getInstance().getSogliaGiorniRitardoPagamenti();
				if(sogliaGiorniRitardoPagamenti != null && sogliaGiorniRitardoPagamenti.intValue() > 0) {
					Calendar c = Calendar.getInstance();
					c.setTime(new Date());
					c.add(Calendar.DAY_OF_YEAR, - sogliaGiorniRitardoPagamenti.intValue()); 
					inRitardo = dataPagamento.getTime() < c.getTime().getTime();
				}

				if(inRitardo ) {
					statoPagamento = PagamentiHandler.STATO_RITARDO_INCASSO;
					if(sogliaGiorniRitardoPagamenti.intValue() > 1)
						statoPagamentoLabel = Utils.getInstance(this.getLanguage()).getMessageWithParamsFromResourceBundle(this.nomeServizio + ".label.sottotitolo."+PagamentiHandler.STATO_RITARDO_INCASSO+".sogliaGiorni",	sogliaGiorniRitardoPagamenti);
					else
						statoPagamentoLabel = Utils.getInstance(this.getLanguage()).getMessageWithParamsFromResourceBundle(this.nomeServizio + ".label.sottotitolo."+PagamentiHandler.STATO_RITARDO_INCASSO+".sogliaGiorno");
				} else {
					statoPagamento = stato.name();
					statoPagamentoLabel = Utils.getInstance(this.getLanguage()).getMessageWithParamsFromResourceBundle(this.nomeServizio + ".label.sottotitolo."+stato.name(), this.sdf.format(dataPagamento));

					//pagamento senza rpt
					if(stato.equals(Stato.PAGATO_SENZA_RPT)) {
						valori.put(Utils.getInstance(this.getLanguage()).getMessageFromResourceBundle(this.nomeServizio + ".pagatoSenzaRpt.id"),
								new Voce<String>(statoPagamentoLabel,Utils.getInstance(this.getLanguage()).getMessageFromResourceBundle(this.nomeServizio + ".stato."+stato.name())));
					}
				}
			}
		} else {
			// bollo
			statoPagamento = PagamentiHandler.BOLLO;
			statoPagamentoLabel = Utils.getInstance(this.getLanguage()).getMessageWithParamsFromResourceBundle(this.nomeServizio + ".label.sottotitolo.BOLLO."+ stato.name(), this.sdf.format(dataPagamento));
			
			//pagamento senza rpt
			if(stato.equals(Stato.PAGATO_SENZA_RPT)) {
				valori.put(Utils.getInstance(this.getLanguage()).getMessageFromResourceBundle(this.nomeServizio + ".pagatoSenzaRpt.id"),
						new Voce<String>(statoPagamentoLabel,Utils.getInstance(this.getLanguage()).getMessageFromResourceBundle(this.nomeServizio + ".stato."+stato.name())));
			}
		}

		BigDecimal importo = entry.getImportoPagato() != null ? entry.getImportoPagato() : BigDecimal.ZERO;


		valori.put(Utils.getInstance(this.getLanguage()).getMessageFromResourceBundle(this.nomeServizio + ".stato.id"),
				new Voce<String>(statoPagamentoLabel,statoPagamento));

		valori.put(Utils.getInstance(this.getLanguage()).getMessageFromResourceBundle(this.nomeServizio + ".importoPagato.id"),
				new Voce<String>(Utils.getInstance(this.getLanguage()).getMessageFromResourceBundle(this.nomeServizio + ".importoPagato.label"),
						this.currencyUtils.getCurrencyAsEuro(importo)));

		if(dataPagamento!= null){
			valori.put(Utils.getInstance(this.getLanguage()).getMessageFromResourceBundle(this.nomeServizio + ".dataPagamento.id"),
					new Voce<String>(Utils.getInstance(this.getLanguage()).getMessageFromResourceBundle(this.nomeServizio + ".dataPagamento.label"),
							this.sdf.format(dataPagamento)));	 
		}

		if(entry.getIur() != null){
			valori.put(Utils.getInstance(this.getLanguage()).getMessageFromResourceBundle(this.nomeServizio + ".iur.id"),
					new Voce<String>(Utils.getInstance(this.getLanguage()).getMessageFromResourceBundle(this.nomeServizio + ".iur.label"),entry.getIur()));
		}

		if(entry.getIuv() != null){
			valori.put(Utils.getInstance(this.getLanguage()).getMessageFromResourceBundle(this.nomeServizio + ".iuv.id"),
					new Voce<String>(Utils.getInstance(this.getLanguage()).getMessageFromResourceBundle(this.nomeServizio + ".iuv.label"),entry.getIuv()));
		}

		if(StringUtils.isNotEmpty(entry.getCodDominio())){
			try{
				Dominio dominio = entry.getDominio(bd);
				Domini dominiDars = new Domini();
				Elemento elemento = ((DominiHandler)dominiDars.getDarsHandler()).getElemento(dominio, dominio.getId(), dominiDars.getPathServizio(), bd);
				valori.put(Utils.getInstance(this.getLanguage()).getMessageFromResourceBundle(this.nomeServizio + ".idDominio.id"),
						new Voce<String>(Utils.getInstance(this.getLanguage()).getMessageFromResourceBundle(this.nomeServizio + ".idDominio.label"),elemento.getTitolo()));

			}catch(Exception e){
				valori.put(Utils.getInstance(this.getLanguage()).getMessageFromResourceBundle(this.nomeServizio + ".idDominio.id"),
						new Voce<String>(Utils.getInstance(this.getLanguage()).getMessageFromResourceBundle(this.nomeServizio + ".idDominio.label"),entry.getCodDominio()));
			}
		}

		try{
			SingoloVersamento singoloVersamento = entry.getSingoloVersamento(bd);
			if(singoloVersamento != null){
				valori.put(Utils.getInstance(this.getLanguage()).getMessageFromResourceBundle(this.nomeServizio + ".codSingoloVersamentoEnte.id"),
						new Voce<String>(Utils.getInstance(this.getLanguage()).getMessageFromResourceBundle(this.nomeServizio + ".codSingoloVersamentoEnte.label"),singoloVersamento.getCodSingoloVersamentoEnte()));
			}
		}catch(ServiceException e){
			throw new ConsoleException(e);
		}
		return valori; 
	}

	public Map<String, Voce<String>> getVociRevoca(Pagamento entry, BasicBD bd) throws ConsoleException { 
		Map<String, Voce<String>> valori = new HashMap<String, Voce<String>>();

		String statoPagamento = Utils.getInstance(this.getLanguage()).getMessageFromResourceBundle(this.nomeServizio + ".statoPagamento.ok");
		String statoPagamentoLabel = Utils.getInstance(this.getLanguage()).getMessageFromResourceBundle(this.nomeServizio + ".statoPagamento.ok");
		BigDecimal importo = entry.getImportoRevocato() != null ? entry.getImportoRevocato() : BigDecimal.ZERO;

		Date dataAcquisizioneRevoca  = entry.getDataAcquisizioneRevoca();
		String dataRevocaFormat = dataAcquisizioneRevoca != null ? this.sdf.format(dataAcquisizioneRevoca) : "--";
		if(entry.getIdRr() != null){
			statoPagamento = Utils.getInstance(this.getLanguage()).getMessageFromResourceBundle(this.nomeServizio + ".statoPagamento.revocato");
			statoPagamentoLabel = Utils.getInstance(this.getLanguage()).getMessageWithParamsFromResourceBundle(this.nomeServizio + ".label.sottotitolo.revocato", dataRevocaFormat);
		} 

		valori.put(Utils.getInstance(this.getLanguage()).getMessageFromResourceBundle(this.nomeServizio + ".statoPagamento.id"),
				new Voce<String>(statoPagamentoLabel,statoPagamento));

		valori.put(Utils.getInstance(this.getLanguage()).getMessageFromResourceBundle(this.nomeServizio + ".importoPagato.id"),
				new Voce<String>(Utils.getInstance(this.getLanguage()).getMessageFromResourceBundle(this.nomeServizio + ".importoRevocato.label"),
						this.currencyUtils.getCurrencyAsEuro(importo)));

		if(dataAcquisizioneRevoca!= null){
			valori.put(Utils.getInstance(this.getLanguage()).getMessageFromResourceBundle(this.nomeServizio + ".dataPagamento.id"),
					new Voce<String>(Utils.getInstance(this.getLanguage()).getMessageFromResourceBundle(this.nomeServizio + ".dataAcquisizioneRevoca.label"),
							dataRevocaFormat));	 
		}

		if(entry.getIur() != null){
			valori.put(Utils.getInstance(this.getLanguage()).getMessageFromResourceBundle(this.nomeServizio + ".iur.id"),
					new Voce<String>(Utils.getInstance(this.getLanguage()).getMessageFromResourceBundle(this.nomeServizio + ".iur.label"),entry.getIur()));
		}

		try{
			SingoloVersamento singoloVersamento = entry.getSingoloVersamento(bd);
			if(singoloVersamento != null){
				valori.put(Utils.getInstance(this.getLanguage()).getMessageFromResourceBundle(this.nomeServizio + ".codSingoloVersamentoEnte.id"),
						new Voce<String>(Utils.getInstance(this.getLanguage()).getMessageFromResourceBundle(this.nomeServizio + ".codSingoloVersamentoEnte.label"),singoloVersamento.getCodSingoloVersamentoEnte()));
			}
		}catch(ServiceException e){
			throw new ConsoleException(e);
		}
		return valori; 
	}

	@Override
	public String esporta(List<Long> idsToExport, List<RawParamValue> rawValues, UriInfo uriInfo, BasicBD bd, ZipOutputStream zout)
			throws WebApplicationException, ConsoleException,ExportException {

		Printer printer  = null;
		String methodName = "exportMassivo " + this.titoloServizio ;

		int numeroZipEntries = 0;

		boolean esportaCsv = false, esportaPdf = false, esportaRtPdf = false, esportaRtBase64 = false;

		String fileName = "Pagamenti.zip";
		try{
			String pathLoghi = ConsoleProperties.getInstance().getPathEstrattoContoPdfLoghi();
			this.log.info("Esecuzione " + methodName + " in corso...");
			// Operazione consentita solo ai ruoli con diritto di lettura
			this.darsService.checkDirittiServizioLettura(bd, this.funzionalita);
			int limit = ConsoleProperties.getInstance().getNumeroMassimoElementiExport();
			boolean simpleSearch = Utils.containsParameter(rawValues, DarsService.SIMPLE_SEARCH_PARAMETER_ID);

			PagamentiBD pagamentiBD = new PagamentiBD(bd); 
			PagamentoFilter filter = pagamentiBD.newFilter(simpleSearch); 
			Map<String, String> params = new HashMap<String, String>();

			String esportaCsvId = Utils.getInstance(this.getLanguage()).getMessageFromResourceBundle(this.nomeServizio + ".esportaCsv.id");
			String esportaCsvS = Utils.getValue(rawValues, esportaCsvId);
			if(StringUtils.isNotEmpty(esportaCsvS)){
				esportaCsv = Boolean.parseBoolean(esportaCsvS);
			}

			String esportaPdfId = Utils.getInstance(this.getLanguage()).getMessageFromResourceBundle(this.nomeServizio + ".esportaPdf.id");
			String esportaPdfS = Utils.getValue(rawValues, esportaPdfId);
			if(StringUtils.isNotEmpty(esportaPdfS)){
				esportaPdf = Boolean.parseBoolean(esportaPdfS);
			}

			String esportaRtPdfId = Utils.getInstance(this.getLanguage()).getMessageFromResourceBundle(this.nomeServizio + ".esportaRtPdf.id");
			String esportaRtPdfS = Utils.getValue(rawValues, esportaRtPdfId);
			if(StringUtils.isNotEmpty(esportaRtPdfS)){
				esportaRtPdf = Boolean.parseBoolean(esportaRtPdfS);
			}

			String esportaRtBase64Id = Utils.getInstance(this.getLanguage()).getMessageFromResourceBundle(this.nomeServizio + ".esportaRtBase64.id");
			String esportaRtBase64S = Utils.getValue(rawValues, esportaRtBase64Id);
			if(StringUtils.isNotEmpty(esportaRtBase64S)){
				esportaRtBase64 = Boolean.parseBoolean(esportaRtBase64S);
			}

			// almeno una voce deve essere selezionata
			if(!(esportaCsv || esportaPdf || esportaRtPdf || esportaRtBase64)){
				List<String> msg = new ArrayList<String>();
				msg.add(Utils.getInstance(this.getLanguage()).getMessageFromResourceBundle(this.nomeServizio+".esporta.tipiExportObbligatori"));
				throw new ExportException(msg, EsitoOperazione.ERRORE);
			}

			// se ho ricevuto anche gli id li utilizzo per fare il check della count
			if(idsToExport != null && idsToExport.size() > 0) 
				filter.setIdPagamenti(idsToExport);

			//1. eseguo una count per verificare che il numero dei risultati da esportare sia <= sogliamassimaexport massivo
			boolean eseguiRicerca = this.popolaFiltroPagamenti(rawValues, uriInfo, pagamentiBD, params, simpleSearch, filter);

			if(!eseguiRicerca){
				List<String> msg = new ArrayList<String>();
				msg.add(Utils.getInstance(this.getLanguage()).getMessageFromResourceBundle(this.nomeServizio+".esporta.operazioneNonPermessa"));
				throw new ExportException(msg, EsitoOperazione.ERRORE);
			}

			long count = pagamentiBD.count(filter);

			if(count < 1){
				List<String> msg = new ArrayList<String>();
				msg.add(Utils.getInstance(this.getLanguage()).getMessageFromResourceBundle(this.nomeServizio+".esporta.nessunElementoDaEsportare"));
				throw new ExportException(msg, EsitoOperazione.ERRORE);
			} 

			if(esportaRtPdf && count > ConsoleProperties.getInstance().getNumeroMassimoElementiExport()){
				List<String> msg = new ArrayList<String>();
				msg.add(Utils.getInstance(this.getLanguage()).getMessageFromResourceBundle(this.nomeServizio+".esporta.numeroElementiDaEsportareSopraSogliaMassima"));
				throw new ExportException(msg, EsitoOperazione.ERRORE);
			}

			FilterSortWrapper fsw = new FilterSortWrapper();
			fsw.setField(it.govpay.orm.Pagamento.model().DATA_PAGAMENTO);
			fsw.setSortOrder(SortOrder.DESC);
			filter.getFilterSortList().add(fsw);
			List<Pagamento> findAllPag = new ArrayList<Pagamento>();

			int countIterazione = -1;
			int offset = 0;

			// esecuzione della ricerca di tutti i pagamenti paginata per problemi di performance, nel caso di esporta rt pdf c'e' il limit impostato e si fa solo un ciclo
			//			do{
			//				filter.setOffset(offset);
			//				filter.setLimit(limit);
			//				List<Pagamento> findAllPagTmp = pagamentiBD.findAll(filter);
			//				
			//				findAllPag.addAll(findAllPagTmp);
			//				
			//				offset += limit;
			//				if(findAllPagTmp == null || findAllPagTmp.size() < limit)
			//					break;
			//				
			//			}while(true);
			filter.setOffset(offset);
			filter.setLimit(Integer.MAX_VALUE);
			findAllPag = pagamentiBD.findAll(filter);
			List<Long> idsPagamenti = new ArrayList<Long>();
			for (Pagamento pagamento : findAllPag) {
				idsPagamenti.add(pagamento.getId());

				if(esportaRtPdf || esportaRtBase64){
					// ricevuta pagamento
					try{
						Rpt rpt = pagamento.getRpt(bd);
						Dominio dominio = pagamento.getDominio(bd);

						if(rpt.getXmlRt() != null){
							numeroZipEntries ++;
							String ricevutaFileName = dominio.getCodDominio() + "_" + pagamento.getIuv() + "_" +pagamento.getIur();
							if(esportaRtPdf){
								Versamento versamento = rpt.getVersamento(bd);
								// RT in formato pdf
								String tipoFirma = rpt.getFirmaRichiesta().getCodifica();
								byte[] rtByteValidato = RtUtils.validaFirma(tipoFirma, rpt.getXmlRt(), dominio.getCodDominio());
								CtRicevutaTelematica rt = JaxbUtils.toRT(rtByteValidato);
								ByteArrayOutputStream baos = new ByteArrayOutputStream();
								String auxDigit = dominio.getAuxDigit() + "";
								String applicationCode = String.format("%02d", dominio.getStazione(bd).getApplicationCode());
								RicevutaPagamentoUtils.getPdfRicevutaPagamento(dominio.getLogo(), versamento.getCausaleVersamento(), rt, pagamento, auxDigit, applicationCode, baos, this.log);
								String rtPdfEntryName = ricevutaFileName + ".pdf";
								numeroZipEntries ++;
								ZipEntry rtPdf = new ZipEntry(rtPdfEntryName);
								zout.putNextEntry(rtPdf);
								zout.write(baos.toByteArray());
								zout.closeEntry();
							}

							if(esportaRtBase64){
								// RT in formato Base 64
								String rtBase64EntryName = ricevutaFileName + ".txt";
								numeroZipEntries ++;
								ZipEntry rtPdf = new ZipEntry(rtBase64EntryName);
								zout.putNextEntry(rtPdf);
								zout.write(rpt.getXmlRt());
								zout.closeEntry();
							}
						}
					}catch (Exception e) {	}
				}
			}


			if(esportaCsv){
				EstrattiContoBD estrattiContoBD = new EstrattiContoBD(bd);
				EstrattoContoFilter ecFilter = estrattiContoBD.newFilter(false); 
				ecFilter.setFiltraDuplicati(true); 

				// recupero oggetto
				ecFilter.setIdPagamento(idsPagamenti);
				List<EstrattoConto> findAll = eseguiRicerca ?  estrattiContoBD.estrattoContoFromIdPagamenti(ecFilter) : new ArrayList<EstrattoConto>();

				if(findAll != null && findAll.size() > 0){
					numeroZipEntries ++;
					//ordinamento record
					Collections.sort(findAll, new EstrattoContoComparator());
					ByteArrayOutputStream baos  = new ByteArrayOutputStream();
					try{
						ZipEntry pagamentoCsv = new ZipEntry("pagamenti.csv");
						zout.putNextEntry(pagamentoCsv);
						printer = new Printer(this.getFormat() , baos);
						printer.printRecord(CSVUtils.getEstrattoContoCsvHeader());
						for (EstrattoConto pagamento : findAll) {
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
			}

			if(esportaPdf){
				Map<String, Dominio> mappaInputDomini = new HashMap<String, Dominio>();
				Map<String, List<Long>> mappaInputEstrattoConto = new HashMap<String, List<Long>>();
				SingoliVersamentiBD singoliVersamentiBD = new SingoliVersamentiBD(bd);
				for (Pagamento pagamento : findAllPag) {
					SingoloVersamento singoloVersamento = singoliVersamentiBD.getSingoloVersamento(pagamento.getIdSingoloVersamento());
					Versamento versamento = singoloVersamento.getVersamento(bd);

					// Prelevo il dominio
					UnitaOperativa uo  = AnagraficaManager.getUnitaOperativa(bd, versamento.getIdUo());
					Dominio dominio  = AnagraficaManager.getDominio(bd, uo.getIdDominio());

					// Aggrego i versamenti per dominio per generare gli estratti conto
					List<Long> idPagamentiDominio = null;
					if(mappaInputEstrattoConto.containsKey(dominio.getCodDominio())) {
						idPagamentiDominio = mappaInputEstrattoConto.get(dominio.getCodDominio());
					} else{
						idPagamentiDominio = new ArrayList<Long>();
						mappaInputEstrattoConto.put(dominio.getCodDominio(), idPagamentiDominio);
						mappaInputDomini.put(dominio.getCodDominio(), dominio);
					}
					idPagamentiDominio.add(pagamento.getId());
				}

				List<it.govpay.core.business.model.EstrattoConto> listInputEstrattoConto = new ArrayList<it.govpay.core.business.model.EstrattoConto>();
				for (String codDominio : mappaInputEstrattoConto.keySet()) {
					it.govpay.core.business.model.EstrattoConto input =  it.govpay.core.business.model.EstrattoConto.creaEstrattoContoPagamentiPDF(mappaInputDomini.get(codDominio), mappaInputEstrattoConto.get(codDominio)); 
					listInputEstrattoConto.add(input);
				}

				it.govpay.core.business.EstrattoConto estrattoContoBD = new it.govpay.core.business.EstrattoConto(bd);
				List<it.govpay.core.business.model.EstrattoConto> listOutputEstattoConto = estrattoContoBD.getEstrattoContoPagamenti(listInputEstrattoConto,pathLoghi);

				for (it.govpay.core.business.model.EstrattoConto estrattoContoOutput : listOutputEstattoConto) {
					Map<String, ByteArrayOutputStream> estrattoContoVersamenti = estrattoContoOutput.getOutput(); 
					for (String nomeEntry : estrattoContoVersamenti.keySet()) {
						numeroZipEntries ++;
						ByteArrayOutputStream baos = estrattoContoVersamenti.get(nomeEntry);
						ZipEntry estrattoContoEntry = new ZipEntry(nomeEntry);
						//						ZipEntry estrattoContoEntry = new ZipEntry(estrattoContoOutput.getDominio().getCodDominio() + "/" + nomeEntry);
						zout.putNextEntry(estrattoContoEntry);
						zout.write(baos.toByteArray());
						zout.closeEntry();
					}


				}
			}

			// se non ho inserito nessuna entry
			if(numeroZipEntries == 0){
				String noEntriesTxt = "/README";
				ZipEntry entryTxt = new ZipEntry(noEntriesTxt);
				zout.putNextEntry(entryTxt);
				zout.write("Non sono state trovate informazioni sui pagamenti selezionati.".getBytes());
				zout.closeEntry();
			}

			zout.flush();
			zout.close();

			this.log.info("Esecuzione " + methodName + " completata.");

			return fileName;
		}catch(WebApplicationException e){
			throw e;
		}catch(ExportException e){
			throw e;
		}catch(Exception e){
			this.log.error(e.getMessage(),e); 
			throw new ConsoleException(e);
		}

	}
	@Override
	public String esporta(Long idToExport, List<RawParamValue> rawValues, UriInfo uriInfo, BasicBD bd, ZipOutputStream zout)	throws WebApplicationException, ConsoleException,ExportException {
		String methodName = "esporta " + this.titoloServizio + "[" + idToExport + "]";  
		Printer printer  = null;
		int numeroZipEntries = 0;
		boolean esportaCsv = false, esportaPdf = false, esportaRtPdf = false, esportaRtBase64 = false;

		try{
			String pathLoghi = ConsoleProperties.getInstance().getPathEstrattoContoPdfLoghi();
			String fileName = "Pagamenti.zip";
			this.log.info("Esecuzione " + methodName + " in corso...");
			// Operazione consentita solo ai ruoli con diritto di lettura
			this.darsService.checkDirittiServizioLettura(bd, this.funzionalita);

			String esportaCsvId = Utils.getInstance(this.getLanguage()).getMessageFromResourceBundle(this.nomeServizio + ".esportaCsv.id");
			String esportaCsvS = Utils.getValue(rawValues, esportaCsvId);
			if(StringUtils.isNotEmpty(esportaCsvS)){
				esportaCsv = Boolean.parseBoolean(esportaCsvS);
			}

			String esportaPdfId = Utils.getInstance(this.getLanguage()).getMessageFromResourceBundle(this.nomeServizio + ".esportaPdf.id");
			String esportaPdfS = Utils.getValue(rawValues, esportaPdfId);
			if(StringUtils.isNotEmpty(esportaPdfS)){
				esportaPdf = Boolean.parseBoolean(esportaPdfS);
			}

			String esportaRtPdfId = Utils.getInstance(this.getLanguage()).getMessageFromResourceBundle(this.nomeServizio + ".esportaRtPdf.id");
			String esportaRtPdfS = Utils.getValue(rawValues, esportaRtPdfId);
			if(StringUtils.isNotEmpty(esportaRtPdfS)){
				esportaRtPdf = Boolean.parseBoolean(esportaRtPdfS);
			}

			String esportaRtBase64Id = Utils.getInstance(this.getLanguage()).getMessageFromResourceBundle(this.nomeServizio + ".esportaRtBase64.id");
			String esportaRtBase64S = Utils.getValue(rawValues, esportaRtBase64Id);
			if(StringUtils.isNotEmpty(esportaRtBase64S)){
				esportaRtBase64 = Boolean.parseBoolean(esportaRtBase64S);
			}

			// almeno una voce deve essere selezionata
			if(!(esportaCsv || esportaPdf || esportaRtPdf|| esportaRtBase64)){
				List<String> msg = new ArrayList<String>();
				msg.add(Utils.getInstance(this.getLanguage()).getMessageFromResourceBundle(this.nomeServizio+".esporta.tipiExportObbligatori"));
				throw new ExportException(msg, EsitoOperazione.ERRORE);
			}

			Set<Long> setDomini = this.darsService.getIdDominiAbilitatiLetturaServizio(bd, this.funzionalita);
			boolean eseguiRicerca = !setDomini.isEmpty();

			it.govpay.core.business.EstrattoConto estrattoContoBD = new it.govpay.core.business.EstrattoConto(bd);
			PagamentiBD pagamentiBD = new PagamentiBD(bd); 
			PagamentoFilter filter = pagamentiBD.newFilter(); 
			filter.setSogliaRitardo(ConsoleProperties.getInstance().getSogliaGiorniRitardoPagamenti());

			List<String> idDomini = new ArrayList<String>();
			List<Long> idsToExport = new ArrayList<Long>();
			idsToExport.add(idToExport);

			if(!setDomini.contains(-1L)){
				List<Long> lstCodDomini = new ArrayList<Long>();
				lstCodDomini.addAll(setDomini);
				idDomini.addAll(this.toListCodDomini(lstCodDomini, bd));
				filter.setIdDomini(idDomini);

				// l'operatore puo' vedere i domini associati, controllo se c'e' un versamento con Id nei domini concessi.
				if(eseguiRicerca){
					filter.setIdPagamenti(idsToExport);
					eseguiRicerca = eseguiRicerca && pagamentiBD.count(filter) > 0;
				}
			}

			if(eseguiRicerca ){
				Pagamento pagamento = pagamentiBD.getPagamento(idToExport);

				if(esportaRtPdf || esportaRtBase64){
					// ricevuta pagamento
					try{
						Rpt rpt = pagamento.getRpt(bd);
						Dominio dominio = pagamento.getDominio(bd);

						if(rpt.getXmlRt() != null){
							String ricevutaFileName = dominio.getCodDominio() + "_" + pagamento.getIuv() + "_" +pagamento.getIur();

							if(esportaRtPdf){
								Versamento versamento = rpt.getVersamento(bd);
								// RT in formato pdf
								String tipoFirma = rpt.getFirmaRichiesta().getCodifica();
								byte[] rtByteValidato = RtUtils.validaFirma(tipoFirma, rpt.getXmlRt(), dominio.getCodDominio());
								CtRicevutaTelematica rt = JaxbUtils.toRT(rtByteValidato);
								ByteArrayOutputStream baos = new ByteArrayOutputStream();
								String auxDigit = dominio.getAuxDigit() + "";
								String applicationCode = String.format("%02d", dominio.getStazione(bd).getApplicationCode());
								RicevutaPagamentoUtils.getPdfRicevutaPagamento(dominio.getLogo(), versamento.getCausaleVersamento(), rt, pagamento, auxDigit, applicationCode, baos, this.log);
								String rtPdfEntryName = ricevutaFileName + ".pdf";
								numeroZipEntries ++;
								ZipEntry rtPdf = new ZipEntry(rtPdfEntryName);
								zout.putNextEntry(rtPdf);
								zout.write(baos.toByteArray());
								zout.closeEntry();
							}

							if(esportaRtBase64){
								// RT in formato Base 64
								String rtBase64EntryName = ricevutaFileName + ".txt";
								numeroZipEntries ++;
								ZipEntry rtPdf = new ZipEntry(rtBase64EntryName);
								zout.putNextEntry(rtPdf);
								zout.write(rpt.getXmlRt());
								zout.closeEntry();
							}
						}
					}catch (Exception e) {	}
				}

				List<Long> ids = new ArrayList<Long>();
				ids.add(pagamento.getId());

				SingoliVersamentiBD singoliVersamentiBD = new SingoliVersamentiBD(bd);
				EstrattiContoBD estrattiContoBD = new EstrattiContoBD(bd);
				EstrattoContoFilter ecFilter = estrattiContoBD.newFilter(false);
				ecFilter.setFiltraDuplicati(true); 

				if(esportaCsv){
					// recupero oggetto
					ecFilter.setIdPagamento(ids);
					List<EstrattoConto> findAll = eseguiRicerca ?  estrattiContoBD.estrattoContoFromIdPagamenti(ecFilter) : new ArrayList<EstrattoConto>();

					if(findAll != null && findAll.size() > 0){
						numeroZipEntries ++;
						//ordinamento record
						Collections.sort(findAll, new EstrattoContoComparator());
						ByteArrayOutputStream baos  = new ByteArrayOutputStream();
						try{
							ZipEntry pagamentoCsv = new ZipEntry("pagamenti.csv");
							zout.putNextEntry(pagamentoCsv);
							printer = new Printer(this.getFormat() , baos);
							printer.printRecord(CSVUtils.getEstrattoContoCsvHeader());
							for (EstrattoConto eConto : findAll) {
								printer.printRecord(CSVUtils.getEstrattoContoAsCsvRow(eConto,this.sdf));
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
				}
				if(esportaPdf){
					SingoloVersamento singoloVersamento = singoliVersamentiBD.getSingoloVersamento(pagamento.getIdSingoloVersamento());
					Versamento versamento = singoloVersamento.getVersamento(bd);
					UnitaOperativa uo  = AnagraficaManager.getUnitaOperativa(bd, versamento.getIdUo());
					Dominio dominio  = AnagraficaManager.getDominio(bd, uo.getIdDominio());
					// Estratto conto per iban e codiceversamento.
					List<Long> idPagamentiDominio = new ArrayList<Long>();
					idPagamentiDominio.add(pagamento.getId());

					it.govpay.core.business.model.EstrattoConto input =  it.govpay.core.business.model.EstrattoConto.creaEstrattoContoPagamentiPDF(dominio, idPagamentiDominio);
					List<it.govpay.core.business.model.EstrattoConto> listInputEstrattoConto = new ArrayList<it.govpay.core.business.model.EstrattoConto>();
					listInputEstrattoConto.add(input);
					List<it.govpay.core.business.model.EstrattoConto> listOutputEstattoConto = estrattoContoBD.getEstrattoContoPagamenti(listInputEstrattoConto,pathLoghi);

					for (it.govpay.core.business.model.EstrattoConto estrattoContoOutput : listOutputEstattoConto) {
						Map<String, ByteArrayOutputStream> estrattoContoVersamenti = estrattoContoOutput.getOutput(); 
						for (String nomeEntry : estrattoContoVersamenti.keySet()) {
							numeroZipEntries ++;
							ByteArrayOutputStream baos = estrattoContoVersamenti.get(nomeEntry);
							//						ZipEntry estrattoContoEntry = new ZipEntry(estrattoContoOutput.getDominio().getCodDominio() + "/" + nomeEntry);
							ZipEntry estrattoContoEntry = new ZipEntry(nomeEntry);
							zout.putNextEntry(estrattoContoEntry);
							zout.write(baos.toByteArray());
							zout.closeEntry();
						}
					}
				}
			}
			// se non ho inserito nessuna entry
			if(numeroZipEntries == 0){
				String noEntriesTxt = "/README";
				ZipEntry entryTxt = new ZipEntry(noEntriesTxt);
				zout.putNextEntry(entryTxt);
				zout.write("Non sono state trovate informazioni sui pagamenti selezionati.".getBytes());
				zout.closeEntry();
			}

			zout.flush();
			zout.close();

			this.log.info("Esecuzione " + methodName + " completata.");

			return  fileName;
		}catch(WebApplicationException e){
			throw e;
		}catch(Exception e){
			throw new ConsoleException(e);
		}
	}

	@SuppressWarnings("unchecked")
	@Override
	public InfoForm getInfoRicerca(UriInfo uriInfo, BasicBD bd, boolean visualizzaRicerca, Map<String,String> parameters) throws ConsoleException { 	
		URI ricerca =  this.getUriRicerca(uriInfo, bd, parameters);
		InfoForm infoRicerca = new InfoForm(ricerca);

		if(visualizzaRicerca) {
			String idDominioId = Utils.getInstance(this.getLanguage()).getMessageFromResourceBundle(this.nomeServizio + ".idDominio.id");
			String statoId = Utils.getInstance(this.getLanguage()).getMessageFromResourceBundle(this.nomeServizio + ".stato.id");
			String dataInizioId = Utils.getInstance(this.getLanguage()).getMessageFromResourceBundle(this.nomeServizio + ".dataInizio.id");
			String dataFineId = Utils.getInstance(this.getLanguage()).getMessageFromResourceBundle(this.nomeServizio + ".dataFine.id");
			String iurId = Utils.getInstance(this.getLanguage()).getMessageFromResourceBundle(this.nomeServizio + ".iur.id");
			String iuvId = Utils.getInstance(this.getLanguage()).getMessageFromResourceBundle(this.nomeServizio + ".iuv.id");
			String codSingoloVersamentoEnteId = Utils.getInstance(this.getLanguage()).getMessageFromResourceBundle(this.nomeServizio + ".codSingoloVersamentoEnte.id");

			if(this.infoRicercaMap == null){
				this.initInfoRicerca(uriInfo, bd);
			}

			Sezione sezioneRoot = infoRicerca.getSezioneRoot();

			try{

				Set<Long> setDomini = this.darsService.getIdDominiAbilitatiLetturaServizio(bd, this.funzionalita);
				List<Long> idDomini = new ArrayList<Long>();

				// idDominio
				List<Voce<Long>> domini = new ArrayList<Voce<Long>>();

				DominiBD dominiBD = new DominiBD(bd);
				DominioFilter filter;
				try {
					filter = dominiBD.newFilter();
					boolean eseguiRicerca = !setDomini.isEmpty();

					if(eseguiRicerca) {
						if(!setDomini.contains(-1L))
							idDomini.addAll(setDomini);	

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

				/// datainizio
				InputDate dataInizio = (InputDate) this.infoRicercaMap.get(dataInizioId);
				dataInizio.setDefaultValue(null);
				sezioneRoot.addField(dataInizio);

				/// dataFine
				InputDate dataFine = (InputDate) this.infoRicercaMap.get(dataFineId);
				dataFine.setDefaultValue(null);
				sezioneRoot.addField(dataFine);	

				// Stato pagamento	
				List<Voce<String>> stati = new ArrayList<Voce<String>>();
				stati.add(new Voce<String>(Utils.getInstance(this.getLanguage()).getMessageFromResourceBundle("commons.label.qualsiasi"), ""));
				stati.add(new Voce<String>(Utils.getInstance(this.getLanguage()).getMessageFromResourceBundle(this.nomeServizio + ".stato."+Stato.INCASSATO.name()), Stato.INCASSATO.name()));
				stati.add(new Voce<String>(Utils.getInstance(this.getLanguage()).getMessageFromResourceBundle(this.nomeServizio + ".stato."+Stato.PAGATO.name()), Stato.PAGATO.name()));
				Integer sogliaGiorniRitardoPagamenti = ConsoleProperties.getInstance().getSogliaGiorniRitardoPagamenti();
				if(sogliaGiorniRitardoPagamenti != null && sogliaGiorniRitardoPagamenti.intValue() > 0)
					stati.add(new Voce<String>(Utils.getInstance(this.getLanguage()).getMessageFromResourceBundle(this.nomeServizio + ".stato."+PagamentiHandler.STATO_RITARDO_INCASSO), PagamentiHandler.STATO_RITARDO_INCASSO));

				SelectList<String> stato = (SelectList<String>) this.infoRicercaMap.get(statoId);
				stato.setDefaultValue("");
				stato.setValues(stati); 
				sezioneRoot.addField(stato);

				// iur
				InputText iur = (InputText) infoRicercaMap.get(iurId);
				iur.setDefaultValue(null);
				sezioneRoot.addField(iur);

				// iuv
				InputText iuv = (InputText) infoRicercaMap.get(iuvId);
				iuv.setDefaultValue(null);
				sezioneRoot.addField(iuv);

				// codSingoloVersamentoEnte
				InputText codSingoloVersamentoEnte = (InputText) infoRicercaMap.get(codSingoloVersamentoEnteId);
				codSingoloVersamentoEnte.setDefaultValue(null);
				sezioneRoot.addField(codSingoloVersamentoEnte);
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
			String statoId = Utils.getInstance(this.getLanguage()).getMessageFromResourceBundle(this.nomeServizio + ".stato.id");
			String dataInizioId = Utils.getInstance(this.getLanguage()).getMessageFromResourceBundle(this.nomeServizio + ".dataInizio.id");
			String dataFineId = Utils.getInstance(this.getLanguage()).getMessageFromResourceBundle(this.nomeServizio + ".dataFine.id");
			String iurId = Utils.getInstance(this.getLanguage()).getMessageFromResourceBundle(this.nomeServizio + ".iur.id");
			String iuvId = Utils.getInstance(this.getLanguage()).getMessageFromResourceBundle(this.nomeServizio + ".iuv.id");
			String codSingoloVersamentoEnteId = Utils.getInstance(this.getLanguage()).getMessageFromResourceBundle(this.nomeServizio + ".codSingoloVersamentoEnte.id");

			List<Voce<Long>> domini = new ArrayList<Voce<Long>>();
			// idDominio
			String idDominioLabel = Utils.getInstance(this.getLanguage()).getMessageFromResourceBundle(this.nomeServizio + ".idDominio.label");
			SelectList<Long> idDominio = new SelectList<Long>(idDominioId, idDominioLabel, null, false, false, true, domini);
			this.infoRicercaMap.put(idDominioId, idDominio);

			// statoVersamento
			List<Voce<String>> stati = new ArrayList<Voce<String>>();
			String statoLabel = Utils.getInstance(this.getLanguage()).getMessageFromResourceBundle(this.nomeServizio + ".stato.label");
			SelectList<String> stato = new SelectList<String>(statoId, statoLabel, null, false, false, true, stati);
			this.infoRicercaMap.put(statoId, stato);

			// dataInizio
			String dataInizioLabel = Utils.getInstance(this.getLanguage()).getMessageFromResourceBundle(this.nomeServizio + ".dataInizio.label");
			InputDate dataInizio = new InputDate(dataInizioId, dataInizioLabel, null, false, false, true, null, null);
			this.infoRicercaMap.put(dataInizioId, dataInizio);

			// dataFine
			String dataFineLabel = Utils.getInstance(this.getLanguage()).getMessageFromResourceBundle(this.nomeServizio + ".dataFine.label");
			InputDate dataFine = new InputDate(dataFineId, dataFineLabel, null, false, false, true, null, null);
			this.infoRicercaMap.put(dataFineId, dataFine);

			// iur
			String iurLabel = Utils.getInstance().getMessageFromResourceBundle(this.nomeServizio + ".iur.label");
			InputText iur = new InputText(iurId, iurLabel, null, false, false, true, 0, 35);
			infoRicercaMap.put(iurId, iur);

			// iuv
			String iuvLabel = Utils.getInstance().getMessageFromResourceBundle(this.nomeServizio + ".iuv.label");
			InputText iuv = new InputText(iuvId, iuvLabel, null, false, false, true, 0, 35);
			infoRicercaMap.put(iuvId, iuv);

			// codSingoloVersamentoEnte
			String codSingoloVersamentoEnteLabel = Utils.getInstance().getMessageFromResourceBundle(this.nomeServizio + ".codSingoloVersamentoEnte.label");
			InputText codSingoloVersamentoEnte = new InputText(codSingoloVersamentoEnteId, codSingoloVersamentoEnteLabel, null, false, false, true, 0, 35);
			infoRicercaMap.put(codSingoloVersamentoEnteId, codSingoloVersamentoEnte);			

		}
	}
	/* Operazioni non consentite */


	@Override
	public InfoForm getInfoCancellazione(UriInfo uriInfo, BasicBD bd, Map<String, String> parameters) throws ConsoleException {
		return null;
	}

	@Override
	public InfoForm getInfoCancellazioneDettaglio(UriInfo uriInfo, BasicBD bd, Pagamento entry) throws ConsoleException {
		return null;
	}
	@Override
	public InfoForm getInfoEsportazione(UriInfo uriInfo, BasicBD bd, Map<String, String> parameters) throws ConsoleException { 
		InfoForm infoEsportazione = null;
		try{
			if(this.darsService.isServizioAbilitatoLettura(bd, this.funzionalita)){
				URI esportazione = this.getUriEsportazione(uriInfo, bd, parameters);
				infoEsportazione = new InfoForm(esportazione);


				List<String> titoli = new ArrayList<String>();
				titoli.add(Utils.getInstance(this.getLanguage()).getMessageFromResourceBundle(this.nomeServizio + ".esporta.singolo.titolo"));
				titoli.add(Utils.getInstance(this.getLanguage()).getMessageFromResourceBundle(this.nomeServizio + ".esporta.multiplo.titolo"));
				infoEsportazione.setTitolo(titoli);

				String esportaCsvId = Utils.getInstance(this.getLanguage()).getMessageFromResourceBundle(this.nomeServizio + ".esportaCsv.id");
				String esportaPdfId = Utils.getInstance(this.getLanguage()).getMessageFromResourceBundle(this.nomeServizio + ".esportaPdf.id");
				String esportaRtPdfId = Utils.getInstance(this.getLanguage()).getMessageFromResourceBundle(this.nomeServizio + ".esportaRtPdf.id");
				String esportaRtBase64Id = Utils.getInstance(this.getLanguage()).getMessageFromResourceBundle(this.nomeServizio + ".esportaRtBase64.id");

				if(this.infoEsportazioneMap == null){
					this.initInfoEsportazione(uriInfo, bd);
				}

				Sezione sezioneRoot = infoEsportazione.getSezioneRoot();

				CheckButton esportaCsv = (CheckButton) this.infoEsportazioneMap.get(esportaCsvId);
				esportaCsv.setDefaultValue(true); 
				sezioneRoot.addField(esportaCsv);

				CheckButton esportaPdf = (CheckButton) this.infoEsportazioneMap.get(esportaPdfId);
				esportaPdf.setDefaultValue(false); 
				sezioneRoot.addField(esportaPdf);

				EsportaRtPdf esportaRtPdf = (EsportaRtPdf) this.infoEsportazioneMap.get(esportaRtPdfId);
				List<RawParamValue> esportaRtPdfValues = new ArrayList<RawParamValue>();
				esportaRtPdfValues.add(new RawParamValue(esportaRtPdfId, "false"));
				esportaRtPdf.init(esportaRtPdfValues, bd,this.getLanguage());  
				sezioneRoot.addField(esportaRtPdf);

				CheckButton esportaRtBase64 = (CheckButton) this.infoEsportazioneMap.get(esportaRtBase64Id);
				esportaRtBase64.setDefaultValue(false); 
				sezioneRoot.addField(esportaRtBase64);

			}
		}catch(ServiceException e){
			throw new ConsoleException(e);
		}
		return infoEsportazione; 
	}

	@Override
	public InfoForm getInfoEsportazioneDettaglio(UriInfo uriInfo, BasicBD bd, Pagamento entry)	throws ConsoleException {
		InfoForm infoEsportazione = null;
		try{
			if(this.darsService.isServizioAbilitatoLettura(bd, this.funzionalita)){
				URI esportazione = this.getUriEsportazioneDettaglio(uriInfo, bd, entry.getId());
				infoEsportazione = new InfoForm(esportazione);

				List<String> titoli = new ArrayList<String>();
				titoli.add(Utils.getInstance(this.getLanguage()).getMessageFromResourceBundle(this.nomeServizio + ".esporta.singolo.titolo"));
				infoEsportazione.setTitolo(titoli);

				String esportaCsvId = Utils.getInstance(this.getLanguage()).getMessageFromResourceBundle(this.nomeServizio + ".esportaCsv.id");
				String esportaPdfId = Utils.getInstance(this.getLanguage()).getMessageFromResourceBundle(this.nomeServizio + ".esportaPdf.id");
				String esportaRtPdfId = Utils.getInstance(this.getLanguage()).getMessageFromResourceBundle(this.nomeServizio + ".esportaRtPdf.id");
				String esportaRtBase64Id = Utils.getInstance(this.getLanguage()).getMessageFromResourceBundle(this.nomeServizio + ".esportaRtBase64.id");

				if(this.infoEsportazioneMap == null){
					this.initInfoEsportazione(uriInfo, bd);
				}

				Sezione sezioneRoot = infoEsportazione.getSezioneRoot();

				CheckButton esportaCsv = (CheckButton) this.infoEsportazioneMap.get(esportaCsvId);
				esportaCsv.setDefaultValue(true); 
				sezioneRoot.addField(esportaCsv);

				CheckButton esportaPdf = (CheckButton) this.infoEsportazioneMap.get(esportaPdfId);
				esportaPdf.setDefaultValue(false); 
				sezioneRoot.addField(esportaPdf);

				EsportaRtPdf esportaRtPdf = (EsportaRtPdf) this.infoEsportazioneMap.get(esportaRtPdfId);
				List<RawParamValue> esportaRtPdfValues = new ArrayList<RawParamValue>();
				esportaRtPdfValues.add(new RawParamValue(esportaRtPdfId, "false"));
				esportaRtPdf.init(esportaRtPdfValues, bd,this.getLanguage());  
				sezioneRoot.addField(esportaRtPdf);

				CheckButton esportaRtBase64 = (CheckButton) this.infoEsportazioneMap.get(esportaRtBase64Id);
				esportaRtBase64.setDefaultValue(false); 
				sezioneRoot.addField(esportaRtBase64);
			}
		}catch(ServiceException e){
			throw new ConsoleException(e);
		}
		return infoEsportazione;	
	}

	private void initInfoEsportazione(UriInfo uriInfo, BasicBD bd) throws ConsoleException{
		if(this.infoEsportazioneMap == null){
			this.infoEsportazioneMap = new HashMap<String, ParamField<?>>();

			String esportaCsvId = Utils.getInstance(this.getLanguage()).getMessageFromResourceBundle(this.nomeServizio + ".esportaCsv.id");
			String esportaPdfId = Utils.getInstance(this.getLanguage()).getMessageFromResourceBundle(this.nomeServizio + ".esportaPdf.id");
			String esportaRtPdfId = Utils.getInstance(this.getLanguage()).getMessageFromResourceBundle(this.nomeServizio + ".esportaRtPdf.id");
			String esportaRtBase64Id = Utils.getInstance(this.getLanguage()).getMessageFromResourceBundle(this.nomeServizio + ".esportaRtBase64.id");

			// esportaCsv
			String esportaCsvLabel = Utils.getInstance(this.getLanguage()).getMessageFromResourceBundle(this.nomeServizio + ".esportaCsv.label");
			CheckButton esportaCsv = new CheckButton(esportaCsvId, esportaCsvLabel, true, false, false, true);
			this.infoEsportazioneMap.put(esportaCsvId, esportaCsv);

			// esportaPdf
			String esportaPdfLabel = Utils.getInstance(this.getLanguage()).getMessageFromResourceBundle(this.nomeServizio + ".esportaPdf.label");
			CheckButton esportaPdf = new CheckButton(esportaPdfId, esportaPdfLabel, true, false, false, true);
			this.infoEsportazioneMap.put(esportaPdfId, esportaPdf);

			// esportaRtPdf
			String esportaRtPdfLabel = Utils.getInstance(this.getLanguage()).getMessageFromResourceBundle(this.nomeServizio + ".esportaRtPdf.label");
			URI esportaRtPdfURI = this.getUriExportField(uriInfo, bd, esportaRtPdfId); 
			List<RawParamValue> esportaRtPdfValues = new ArrayList<RawParamValue>();
			esportaRtPdfValues.add(new RawParamValue(esportaRtPdfId, "false"));


			EsportaRtPdf esportaRtPdf = new EsportaRtPdf(this.nomeServizio,	esportaRtPdfId, esportaRtPdfLabel, esportaRtPdfURI, esportaRtPdfValues,this.getLanguage());
			this.infoEsportazioneMap.put(esportaRtPdfId, esportaRtPdf);
			esportaRtPdf.addDependencyField(esportaRtPdf);

			// esportaRtBase64
			String esportaRtBase64Label = Utils.getInstance(this.getLanguage()).getMessageFromResourceBundle(this.nomeServizio + ".esportaRtBase64.label");
			CheckButton esportaRtBase64 = new CheckButton(esportaRtBase64Id, esportaRtBase64Label, true, false, false, true);
			this.infoEsportazioneMap.put(esportaRtBase64Id, esportaRtBase64);
		}
	}

	@Override
	public InfoForm getInfoCreazione(UriInfo uriInfo, BasicBD bd) throws ConsoleException {		return null;	}

	@Override
	public InfoForm getInfoModifica(UriInfo uriInfo, BasicBD bd, Pagamento entry) throws ConsoleException {	return null;	}

	@Override
	public Object getField(UriInfo uriInfo, List<RawParamValue> values, String fieldId, BasicBD bd)	throws WebApplicationException, ConsoleException {	return null;	}

	@Override
	public Object getSearchField(UriInfo uriInfo, List<RawParamValue> values, String fieldId, BasicBD bd)	throws WebApplicationException, ConsoleException { 	return null; }

	@Override
	public Object getDeleteField(UriInfo uriInfo, List<RawParamValue> values, String fieldId, BasicBD bd) throws WebApplicationException, ConsoleException { return null; }

	@Override
	public Object getExportField(UriInfo uriInfo, List<RawParamValue> values, String fieldId, BasicBD bd) throws WebApplicationException, ConsoleException { 
		this.log.debug("Richiesto export field ["+fieldId+"]");
		try{
			// Operazione consentita solo ai ruoli con diritto di lettura
			this.darsService.checkDirittiServizioLettura(bd, this.funzionalita); 

			if(this.infoEsportazioneMap == null){
				this.initInfoEsportazione(uriInfo, bd);
			}

			if(this.infoEsportazioneMap.containsKey(fieldId)){
				RefreshableParamField<?> paramField = (RefreshableParamField<?>) this.infoEsportazioneMap.get(fieldId);

				paramField.aggiornaParametro(values,bd, this.getLanguage());

				return paramField;

			}

			this.log.debug("Field ["+fieldId+"] non presente.");

		}catch(Exception e){
			throw new ConsoleException(e);
		}
		return null;

	}

	@Override
	public Elenco delete(List<Long> idsToDelete, List<RawParamValue> rawValues, UriInfo uriInfo, BasicBD bd) throws WebApplicationException, ConsoleException, DeleteException {	return null; 	}

	@Override
	public Pagamento creaEntry(InputStream is, UriInfo uriInfo, BasicBD bd) throws WebApplicationException, ConsoleException { return null;	}

	@Override
	public Dettaglio insert(InputStream is, UriInfo uriInfo, BasicBD bd) 	throws WebApplicationException, ConsoleException, ValidationException, DuplicatedEntryException {	return null;	}

	@Override
	public void checkEntry(Pagamento entry, Pagamento oldEntry) throws ValidationException {}

	@Override
	public Dettaglio update(InputStream is, UriInfo uriInfo, BasicBD bd) throws WebApplicationException, ConsoleException, ValidationException {		return null;	}

	@Override
	public Object uplaod(MultipartFormDataInput input, UriInfo uriInfo, BasicBD bd)	throws WebApplicationException, ConsoleException, ValidationException { return null;}
}
