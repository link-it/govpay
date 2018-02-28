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
package it.govpay.web.rs.dars.monitoraggio.incassi;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.net.URI;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.UriInfo;

import org.apache.commons.lang.StringUtils;
import org.jboss.resteasy.plugins.providers.multipart.MultipartFormDataInput;
import org.openspcoop2.generic_project.exception.ServiceException;
import org.openspcoop2.generic_project.expression.SortOrder;
import org.slf4j.Logger;

import it.govpay.bd.BasicBD;
import it.govpay.bd.FilterSortWrapper;
import it.govpay.bd.anagrafica.DominiBD;
import it.govpay.bd.anagrafica.filters.DominioFilter;
import it.govpay.bd.model.Applicazione;
import it.govpay.bd.model.Dominio;
import it.govpay.bd.model.Incasso;
import it.govpay.bd.model.Pagamento;
import it.govpay.bd.pagamento.IncassiBD;
import it.govpay.bd.pagamento.filters.IncassoFilter;
import it.govpay.stampe.pdf.incasso.IncassoPdf;
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
import it.govpay.web.rs.dars.model.input.base.InputDate;
import it.govpay.web.rs.dars.model.input.base.InputText;
import it.govpay.web.rs.dars.model.input.base.SelectList;
import it.govpay.web.rs.dars.monitoraggio.pagamenti.Pagamenti;
import it.govpay.web.utils.ConsoleProperties;
import it.govpay.web.utils.Utils;

public class IncassiHandler extends DarsHandler<Incasso> implements IDarsHandler<Incasso>{

	public static final String ANAGRAFICA_DEBITORE = "anagrafica";
	private SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm");  

	public IncassiHandler(Logger log, DarsService darsService) { 
		super(log, darsService);
	}

	@Override
	public Elenco getElenco(UriInfo uriInfo, BasicBD bd) throws WebApplicationException, ConsoleException {
		String methodName = "getElenco " + this.titoloServizio;
		try{	
			// Operazione consentita solo agli utenti che hanno almeno un ruolo consentito per la funzionalita'
			this.darsService.checkDirittiServizio(bd, this.funzionalita);

			Integer offset = this.getOffset(uriInfo);
			Integer limit = this.getLimit(uriInfo);

			this.log.info("Esecuzione " + methodName + " in corso..."); 

			IncassiBD incassiBD = new IncassiBD(bd);
			boolean simpleSearch = this.containsParameter(uriInfo, DarsService.SIMPLE_SEARCH_PARAMETER_ID);

			IncassoFilter filter = incassiBD.newFilter(simpleSearch);
			filter.setOffset(offset);
			filter.setLimit(limit);
			FilterSortWrapper fsw = new FilterSortWrapper();
			fsw.setField(it.govpay.orm.Incasso.model().DATA_ORA_INCASSO);
			fsw.setSortOrder(SortOrder.DESC);
			filter.getFilterSortList().add(fsw);

			boolean eseguiRicerca = popolaFiltroRicerca(uriInfo, bd,  simpleSearch, filter);

			long count = eseguiRicerca ? incassiBD.count(filter) : 0;

			// visualizza la ricerca solo se i risultati sono > del limit
			boolean visualizzaRicerca = this.visualizzaRicerca(count, limit);
			InfoForm infoRicerca = this.getInfoRicerca(uriInfo, bd, visualizzaRicerca);

			// Indico la visualizzazione custom
			String formatter = Utils.getInstance(this.getLanguage()).getMessageFromResourceBundle(this.nomeServizio+".elenco.formatter");
			String simpleSearchPlaceholder = Utils.getInstance(this.getLanguage()).getMessageFromResourceBundle(this.nomeServizio+".simpleSearch.placeholder");
			Elenco elenco = new Elenco(this.titoloServizio, infoRicerca,
					this.getInfoCreazione(uriInfo, bd),
					count, this.getInfoEsportazione(uriInfo, bd), this.getInfoCancellazione(uriInfo, bd),simpleSearchPlaceholder); 

			List<Incasso> findAll = eseguiRicerca ? incassiBD.findAll(filter) : new ArrayList<Incasso>(); 

			if(findAll != null && findAll.size() > 0){
				for (Incasso entry : findAll) {
					Elemento elemento = this.getElemento(entry, entry.getId(), this.pathServizio,bd);
					elemento.setFormatter(formatter); 
					elenco.getElenco().add(elemento);
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

	private boolean popolaFiltroRicerca(UriInfo uriInfo, BasicBD bd,   boolean simpleSearch, IncassoFilter filter) throws ConsoleException, Exception {
		List<Long> setDomini = this.darsService.getIdDominiAbilitatiLetturaServizio(bd, this.funzionalita);
		boolean eseguiRicerca = !setDomini.isEmpty();
		List<String> idDomini = new ArrayList<String>();

		if(simpleSearch){
			// simplesearch
			String simpleSearchString = this.getParameter(uriInfo, DarsService.SIMPLE_SEARCH_PARAMETER_ID, String.class);
			if(StringUtils.isNotEmpty(simpleSearchString)) {
				filter.setSimpleSearchString(simpleSearchString);
			}
		}else{
			String dataInizioId = Utils.getInstance(this.getLanguage()).getMessageFromResourceBundle(this.nomeServizio + ".dataInizio.id");

			String dataInizio = this.getParameter(uriInfo, dataInizioId, String.class);
			if(StringUtils.isNotEmpty(dataInizio)){
				filter.setDataInizio(this.convertJsonStringToDataInizio(dataInizio));
			}

			String dataFineId = Utils.getInstance(this.getLanguage()).getMessageFromResourceBundle(this.nomeServizio + ".dataFine.id");
			String dataFine = this.getParameter(uriInfo, dataFineId, String.class);
			if(StringUtils.isNotEmpty(dataFine)){
				filter.setDataFine(this.convertJsonStringToDataFine(dataFine));
			}

			String trnId = Utils.getInstance(this.getLanguage()).getMessageFromResourceBundle(this.nomeServizio + ".trn.id");
			String trn = this.getParameter(uriInfo, trnId, String.class);
			if(StringUtils.isNotEmpty(trn)) {
				filter.setTrn(trn);
			} 

			String dispositivoId = Utils.getInstance(this.getLanguage()).getMessageFromResourceBundle(this.nomeServizio + ".dispositivo.id");
			String dispositivo = this.getParameter(uriInfo, dispositivoId, String.class);
			if(StringUtils.isNotEmpty(dispositivo)) {
				filter.setDispositivo(dispositivo);
			}

			String causaleId = Utils.getInstance(this.getLanguage()).getMessageFromResourceBundle(this.nomeServizio + ".causale.id");
			String causale = this.getParameter(uriInfo, causaleId, String.class);
			if(StringUtils.isNotEmpty(causale)) {
				filter.setCausale(dispositivo);
			}

			String idDominioId = Utils.getInstance(this.getLanguage()).getMessageFromResourceBundle(this.nomeServizio + ".idDominio.id");
			String idDominio = this.getParameter(uriInfo, idDominioId, String.class);
			if(StringUtils.isNotEmpty(idDominio)){
				long idDom = -1l;
				try{
					idDom = Long.parseLong(idDominio);
				}catch(Exception e){ idDom = -1l;	}
				if(idDom > 0){
					idDomini.add(idDominio);
					filter.setCodDomini(idDomini);
				}
			}
		}

		if(!setDomini.contains(-1L)){
			List<Long> lstCodDomini = new ArrayList<Long>();
			lstCodDomini.addAll(setDomini);
			idDomini.addAll(this.toListCodDomini(lstCodDomini, bd));
			filter.setCodDomini(idDomini);
		}

		return eseguiRicerca;
	}

	private boolean popolaFiltroRicerca(List<RawParamValue> rawValues, BasicBD bd,   boolean simpleSearch, IncassoFilter filter) throws ConsoleException, Exception {
		List<Long> setDomini = this.darsService.getIdDominiAbilitatiLetturaServizio(bd, this.funzionalita);
		boolean eseguiRicerca = !setDomini.isEmpty();
		List<String> idDomini = new ArrayList<String>();

		if(simpleSearch){
			// simplesearch
			String simpleSearchString = Utils.getValue(rawValues, DarsService.SIMPLE_SEARCH_PARAMETER_ID);
			if(StringUtils.isNotEmpty(simpleSearchString)) {
				filter.setSimpleSearchString(simpleSearchString);
			}
		}else{
			String dataInizioId = Utils.getInstance(this.getLanguage()).getMessageFromResourceBundle(this.nomeServizio + ".dataInizio.id");

			String dataInizio = Utils.getValue(rawValues, dataInizioId);
			if(StringUtils.isNotEmpty(dataInizio)){
				filter.setDataInizio(this.convertJsonStringToDataInizio(dataInizio));
			}

			String dataFineId = Utils.getInstance(this.getLanguage()).getMessageFromResourceBundle(this.nomeServizio + ".dataFine.id");
			String dataFine = Utils.getValue(rawValues, dataFineId);
			if(StringUtils.isNotEmpty(dataFine)){
				filter.setDataFine(this.convertJsonStringToDataFine(dataFine));
			}

			String trnId = Utils.getInstance(this.getLanguage()).getMessageFromResourceBundle(this.nomeServizio + ".trn.id");
			String trn = Utils.getValue(rawValues, trnId);
			if(StringUtils.isNotEmpty(trn)) {
				filter.setTrn(trn);
			} 

			String dispositivoId = Utils.getInstance(this.getLanguage()).getMessageFromResourceBundle(this.nomeServizio + ".dispositivo.id");
			String dispositivo = Utils.getValue(rawValues, dispositivoId);
			if(StringUtils.isNotEmpty(dispositivo)) {
				filter.setDispositivo(dispositivo);
			}

			String causaleId = Utils.getInstance(this.getLanguage()).getMessageFromResourceBundle(this.nomeServizio + ".causale.id");
			String causale = Utils.getValue(rawValues, causaleId);
			if(StringUtils.isNotEmpty(causale)) {
				filter.setCausale(dispositivo);
			}

			String idDominioId = Utils.getInstance(this.getLanguage()).getMessageFromResourceBundle(this.nomeServizio + ".idDominio.id");
			String idDominio = Utils.getValue(rawValues, idDominioId);
			if(StringUtils.isNotEmpty(idDominio)){
				long idDom = -1l;
				try{
					idDom = Long.parseLong(idDominio);
				}catch(Exception e){ idDom = -1l;	}
				if(idDom > 0){
					idDomini.add(idDominio);
					filter.setCodDomini(idDomini);
				}
			}
		}

		if(!setDomini.contains(-1L)){
			List<Long> lstCodDomini = new ArrayList<Long>();
			lstCodDomini.addAll(setDomini);
			idDomini.addAll(this.toListCodDomini(lstCodDomini, bd));
			filter.setCodDomini(idDomini);
		}
		return eseguiRicerca;
	}

	@SuppressWarnings("unchecked")
	@Override
	public InfoForm getInfoRicerca(UriInfo uriInfo, BasicBD bd, boolean visualizzaRicerca, Map<String,String> parameters) throws ConsoleException {
		URI ricerca = this.getUriRicerca(uriInfo, bd);
		InfoForm infoRicerca = new InfoForm(ricerca);

		if(visualizzaRicerca) {
			String dataInizioId = Utils.getInstance(this.getLanguage()).getMessageFromResourceBundle(this.nomeServizio + ".dataInizio.id");
			String dataFineId = Utils.getInstance(this.getLanguage()).getMessageFromResourceBundle(this.nomeServizio + ".dataFine.id");
			String trnId = Utils.getInstance(this.getLanguage()).getMessageFromResourceBundle(this.nomeServizio + ".trn.id");
			String dispositivoId = Utils.getInstance(this.getLanguage()).getMessageFromResourceBundle(this.nomeServizio + ".dispositivo.id");
			String causaleId = Utils.getInstance(this.getLanguage()).getMessageFromResourceBundle(this.nomeServizio + ".causale.id");
			String idDominioId = Utils.getInstance(this.getLanguage()).getMessageFromResourceBundle(this.nomeServizio + ".idDominio.id");

			if(this.infoRicercaMap == null){
				this.initInfoRicerca(uriInfo, bd);
			}

			Sezione sezioneRoot = infoRicerca.getSezioneRoot();

			try{
				List<Long> setDomini = this.darsService.getIdDominiAbilitatiLetturaServizio(bd, this.funzionalita);

				// idDominio
				List<Voce<Long>> domini = new ArrayList<Voce<Long>>();
				List<Long> idDomini = new ArrayList<Long>();

				DominiBD dominiBD = new DominiBD(bd);
				DominioFilter filter;
				try {
					filter = dominiBD.newFilter();

					boolean eseguiRicerca = !setDomini.isEmpty();

					if(eseguiRicerca) {
						if(!setDomini.contains(-1L)) {
							idDomini.addAll(setDomini);	
							filter.setIdDomini(idDomini);
						}					

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

			/// datainizio
			InputDate dataInizio = (InputDate) this.infoRicercaMap.get(dataInizioId);
			dataInizio.setDefaultValue(null);
			sezioneRoot.addField(dataInizio);

			/// dataFine
			InputDate dataFine = (InputDate) this.infoRicercaMap.get(dataFineId);
			dataFine.setDefaultValue(null);
			sezioneRoot.addField(dataFine);	

			InputText trn = (InputText) this.infoRicercaMap.get(trnId);
			trn.setDefaultValue(null);
			sezioneRoot.addField(trn);

			InputText causale = (InputText) this.infoRicercaMap.get(causaleId);
			causale.setDefaultValue(null);
			sezioneRoot.addField(causale);

			InputText dispositivo = (InputText) this.infoRicercaMap.get(dispositivoId);
			dispositivo.setDefaultValue(null);
			sezioneRoot.addField(dispositivo);

		}
		return infoRicerca;
	}

	private void initInfoRicerca(UriInfo uriInfo, BasicBD bd) throws ConsoleException{
		if(this.infoRicercaMap == null){
			this.infoRicercaMap = new HashMap<String, ParamField<?>>();

			String dataInizioId = Utils.getInstance(this.getLanguage()).getMessageFromResourceBundle(this.nomeServizio + ".dataInizio.id");
			String dataFineId = Utils.getInstance(this.getLanguage()).getMessageFromResourceBundle(this.nomeServizio + ".dataFine.id");
			String trnId = Utils.getInstance(this.getLanguage()).getMessageFromResourceBundle(this.nomeServizio + ".trn.id");
			String dispositivoId = Utils.getInstance(this.getLanguage()).getMessageFromResourceBundle(this.nomeServizio + ".dispositivo.id");
			String causaleId = Utils.getInstance(this.getLanguage()).getMessageFromResourceBundle(this.nomeServizio + ".causale.id");
			String idDominioId = Utils.getInstance(this.getLanguage()).getMessageFromResourceBundle(this.nomeServizio + ".idDominio.id");

			// trn
			String trnLabel = Utils.getInstance(this.getLanguage()).getMessageFromResourceBundle(this.nomeServizio + ".trn.label");
			InputText trn = new InputText(trnId, trnLabel, null, false, false, true, 1, 35);
			this.infoRicercaMap.put(trnId, trn);

			// causale
			String causaleLabel = Utils.getInstance(this.getLanguage()).getMessageFromResourceBundle(this.nomeServizio + ".causale.label");
			InputText causale = new InputText(causaleId, causaleLabel, null, false, false, true, 1, 35);
			this.infoRicercaMap.put(causaleId, causale);

			// dispsitivo
			String dispositivoLabel = Utils.getInstance(this.getLanguage()).getMessageFromResourceBundle(this.nomeServizio + ".dispositivo.label");
			InputText dispositivo = new InputText(dispositivoId, dispositivoLabel, null, false, false, true, 1, 35);
			this.infoRicercaMap.put(dispositivoId, dispositivo);			

			List<Voce<Long>> domini = new ArrayList<Voce<Long>>();
			// idDominio
			String idDominioLabel = Utils.getInstance(this.getLanguage()).getMessageFromResourceBundle(this.nomeServizio + ".idDominio.label");
			SelectList<Long> idDominio = new SelectList<Long>(idDominioId, idDominioLabel, null, false, false, true, domini);
			this.infoRicercaMap.put(idDominioId, idDominio);

			// dataInizio
			String dataInizioLabel = Utils.getInstance(this.getLanguage()).getMessageFromResourceBundle(this.nomeServizio + ".dataInizio.label");
			InputDate dataInizio = new InputDate(dataInizioId, dataInizioLabel, null, false, false, true, null, null);
			this.infoRicercaMap.put(dataInizioId, dataInizio);

			// dataFine
			String dataFineLabel = Utils.getInstance(this.getLanguage()).getMessageFromResourceBundle(this.nomeServizio + ".dataFine.label");
			InputDate dataFine = new InputDate(dataFineId, dataFineLabel, null, false, false, true, null, null);
			this.infoRicercaMap.put(dataFineId, dataFine);

		}
	}

	@Override
	public Object getField(UriInfo uriInfo, List<RawParamValue> values, String fieldId, BasicBD bd) throws WebApplicationException, ConsoleException {
		return null;
	}

	@Override
	public Object getSearchField(UriInfo uriInfo, List<RawParamValue> values, String fieldId, BasicBD bd)	throws WebApplicationException, ConsoleException { 	return null; }

	@Override
	public Object getDeleteField(UriInfo uriInfo, List<RawParamValue> values, String fieldId, BasicBD bd) throws WebApplicationException, ConsoleException { return null; }

	@Override
	public Object getExportField(UriInfo uriInfo, List<RawParamValue> values, String fieldId, BasicBD bd) throws WebApplicationException, ConsoleException { return null; }

	@Override
	public Dettaglio getDettaglio(long id, UriInfo uriInfo, BasicBD bd)
			throws WebApplicationException, ConsoleException {
		String methodName = "dettaglio " + this.titoloServizio + "."+ id;
	
		try{
			this.log.info("Esecuzione " + methodName + " in corso...");
			// Operazione consentita solo ai ruoli con diritto di lettura
			this.darsService.checkDirittiServizioLettura(bd, this.funzionalita);

			List<Long> setDomini = this.darsService.getIdDominiAbilitatiLetturaServizio(bd, this.funzionalita);
			IncassiBD incassiBD = new IncassiBD(bd);

			boolean eseguiRicerca = !setDomini.isEmpty();

			if(eseguiRicerca && !setDomini.contains(-1L)){
				List<String> idDomini = new ArrayList<String>();
				IncassoFilter filter = incassiBD.newFilter();
				List<Long> lstCodDomini = new ArrayList<Long>();
				lstCodDomini.addAll(setDomini);
				idDomini.addAll(this.toListCodDomini(lstCodDomini, bd));
				filter.setCodDomini(idDomini);
				List<Long> idIncassoL = new ArrayList<Long>();
				idIncassoL.add(id);
				filter.setIdIncasso(idIncassoL);

				long count = eseguiRicerca ? incassiBD.count(filter) : 0;
				eseguiRicerca = eseguiRicerca && count > 0;
			}

			// recupero oggetto
			Incasso incasso = eseguiRicerca ? incassiBD.getIncasso(id) : null;

			InfoForm infoModifica = null;
			InfoForm infoCancellazione = null;
			InfoForm infoEsportazione =  incasso != null ? this.getInfoEsportazioneDettaglio(uriInfo, incassiBD, incasso) : null;

			String titolo = Utils.getInstance(this.getLanguage()).getMessageFromResourceBundle(this.nomeServizio + ".dettaglioIncasso") ;
			Dettaglio dettaglio = new Dettaglio(titolo, infoEsportazione, infoCancellazione, infoModifica);

			it.govpay.web.rs.dars.model.Sezione root = dettaglio.getSezioneRoot();

			if(incasso != null){

				if(StringUtils.isNotEmpty(incasso.getTrn())) {
					root.addVoce(Utils.getInstance(this.getLanguage()).getMessageFromResourceBundle(this.nomeServizio + ".trn.label"), incasso.getTrn());
				}
				// Uo
				if(StringUtils.isNotEmpty(incasso.getCodDominio())){
					try{
						Dominio dominio = incasso.getDominio(bd);
						Domini dominiDars = new Domini();
						Elemento elemento = ((DominiHandler)dominiDars.getDarsHandler()).getElemento(dominio, dominio.getId(), dominiDars.getPathServizio(), bd);
						root.addVoce(Utils.getInstance(this.getLanguage()).getMessageFromResourceBundle(this.nomeServizio + ".idDominio.label"), elemento.getTitolo()); 
					}catch(Exception e){
						root.addVoce(Utils.getInstance(this.getLanguage()).getMessageFromResourceBundle(this.nomeServizio + ".idDominio.label"), incasso.getCodDominio());
					}
				}

				// Applicazione
				Applicazione applicazione = incasso.getApplicazione(bd);
				if(applicazione != null) {
					root.addVoce(Utils.getInstance(this.getLanguage()).getMessageFromResourceBundle(this.nomeServizio + ".idApplicazione.label"), applicazione.getCodApplicazione());
				} 

				if(incasso.getImporto() != null) {
					root.addVoce(Utils.getInstance(this.getLanguage()).getMessageFromResourceBundle(this.nomeServizio + ".importo.label"), 
							this.currencyUtils.getCurrencyAsEuro(incasso.getImporto()));
				}
				if(incasso.getDataIncasso() != null) {
					root.addVoce(Utils.getInstance(this.getLanguage()).getMessageFromResourceBundle(this.nomeServizio + ".dataIncasso.label"), this.sdf.format(incasso.getDataIncasso()));
				}
				if(incasso.getDataContabile() != null) {
					root.addVoce(Utils.getInstance(this.getLanguage()).getMessageFromResourceBundle(this.nomeServizio + ".dataContabile.label"), this.sdf.format(incasso.getDataContabile()));
				}
				if(incasso.getDataValuta()!= null) {
					root.addVoce(Utils.getInstance(this.getLanguage()).getMessageFromResourceBundle(this.nomeServizio + ".dataValuta.label"), this.sdf.format(incasso.getDataValuta()));
				}

				if(incasso.getCausale() != null) {
					root.addVoce(Utils.getInstance(this.getLanguage()).getMessageFromResourceBundle(this.nomeServizio + ".causale.label"), incasso.getCausale().toString());
				}

				if(incasso.getDispositivo() != null) {
					root.addVoce(Utils.getInstance(this.getLanguage()).getMessageFromResourceBundle(this.nomeServizio + ".dispositivo.label"), incasso.getDispositivo().toString());
				}

				Pagamenti pagamentiDars = new Pagamenti();
				String incassoId = Utils.getInstance(this.getLanguage()).getMessageFromResourceBundle(pagamentiDars.getNomeServizio() + ".idIncasso.id");
				String etichettaPagamenti = Utils.getInstance(this.getLanguage()).getMessageFromResourceBundle(this.nomeServizio + ".elementoCorrelato.pagamenti.titolo");
				Map<String, String> params = new HashMap<String, String>();
				params.put(incassoId, incasso.getId()+ "");
				URI pagamentoDettaglio = Utils.creaUriConParametri(pagamentiDars.getPathServizio(), params );
				dettaglio.addElementoCorrelato(etichettaPagamenti, pagamentoDettaglio); 

			}

			this.log.info("Esecuzione " + methodName + " completata.");

			return dettaglio;
		}catch(WebApplicationException e){
			throw e;
		}catch(Exception e){
			throw new ConsoleException(e);
		}
	}

	@Override
	public String getTitolo(Incasso entry,BasicBD bd) {
		StringBuilder sb = new StringBuilder();

		String dispositivo = entry.getDispositivo();
		Date dataValuta = entry.getDataValuta();

		String dataValutaS = dataValuta != null ? this.sdf.format(dataValuta) : "";
		Utils.getInstance(this.getLanguage()).getMessageWithParamsFromResourceBundle(this.nomeServizio + ".label.titolo", dispositivo,dataValutaS);

		return sb.toString();
	}

	@Override
	public String getSottotitolo(Incasso entry,BasicBD bd) {
		StringBuilder sb = new StringBuilder();

		String causale = entry.getCausale();
		sb.append(
				Utils.getInstance(this.getLanguage()).getMessageWithParamsFromResourceBundle(this.nomeServizio + ".label.sottotitolo", causale, this.currencyUtils.getCurrencyAsEuro(entry.getImporto())));

		return sb.toString();
	} 

	@Override
	public Map<String, Voce<String>> getVoci(Incasso entry, BasicBD bd) throws ConsoleException {
		Map<String, Voce<String>> voci = new HashMap<String, Voce<String>>();

		// voci da inserire nella visualizzazione personalizzata 
		// stato, dispositivo, causale, importo, datavaluta, dataacquisizione
		if(StringUtils.isNotEmpty(entry.getCausale())) {
			voci.put(Utils.getInstance(this.getLanguage()).getMessageFromResourceBundle(this.nomeServizio + ".causale.id"),
					new Voce<String>(Utils.getInstance(this.getLanguage()).getMessageFromResourceBundle(this.nomeServizio + ".causale.label"), entry.getCausale()));
		}

		if(entry.getDataIncasso() != null) {
			voci.put(Utils.getInstance(this.getLanguage()).getMessageFromResourceBundle(this.nomeServizio + ".dataIncasso.id"),
					new Voce<String>(Utils.getInstance(this.getLanguage()).getMessageFromResourceBundle(this.nomeServizio + ".dataIncasso.label"), this.sdf.format(entry.getDataIncasso())));
		}

		if(entry.getDataValuta() != null) {
			voci.put(Utils.getInstance(this.getLanguage()).getMessageFromResourceBundle(this.nomeServizio + ".dataValuta.id"),
					new Voce<String>(Utils.getInstance(this.getLanguage()).getMessageFromResourceBundle(this.nomeServizio + ".dataValuta.label"), this.sdf.format(entry.getDataValuta())));
		}

		if(StringUtils.isNotEmpty(entry.getDispositivo())) {
			voci.put(Utils.getInstance(this.getLanguage()).getMessageFromResourceBundle(this.nomeServizio + ".dispositivo.id"),
					new Voce<String>(Utils.getInstance(this.getLanguage()).getMessageFromResourceBundle(this.nomeServizio + ".dispositivo.label"), entry.getDispositivo()));
		}

		if(StringUtils.isNotEmpty(entry.getTrn())) {
			voci.put(Utils.getInstance(this.getLanguage()).getMessageFromResourceBundle(this.nomeServizio + ".trn.id"),
					new Voce<String>(Utils.getInstance(this.getLanguage()).getMessageFromResourceBundle(this.nomeServizio + ".trn.label"), entry.getTrn()));
		}

		voci.put(Utils.getInstance(this.getLanguage()).getMessageFromResourceBundle(this.nomeServizio + ".stato.id"),
				new Voce<String>(Utils.getInstance(this.getLanguage()).getMessageFromResourceBundle(this.nomeServizio + ".stato.ok"),
						"ok"));
		
		voci.put(Utils.getInstance(this.getLanguage()).getMessageFromResourceBundle(this.nomeServizio + ".statoText.id"),
				new Voce<String>(Utils.getInstance(this.getLanguage()).getMessageFromResourceBundle(this.nomeServizio + ".statoText.label"),
						Utils.getInstance(this.getLanguage()).getMessageFromResourceBundle(this.nomeServizio + ".stato.OK")));

		if(entry.getImporto() != null) {
			voci.put(Utils.getInstance(this.getLanguage()).getMessageFromResourceBundle(this.nomeServizio + ".importo.id"),
					new Voce<String>(Utils.getInstance(this.getLanguage()).getMessageFromResourceBundle(this.nomeServizio + ".importo.label"), this.currencyUtils.getCurrencyAsEuro(entry.getImporto())));
		}

		try{		
			Dominio dominio = entry.getDominio(bd);
			if(dominio != null){
				Domini dominiDars = new Domini();
				DominiHandler dominiDarsHandler =  (DominiHandler) dominiDars.getDarsHandler();
				String dominioTitolo = dominiDarsHandler.getTitolo(dominio, bd);
				voci.put(Utils.getInstance(this.getLanguage()).getMessageFromResourceBundle(this.nomeServizio + ".idDominio.id"),
						new Voce<String>(Utils.getInstance(this.getLanguage()).getMessageFromResourceBundle(this.nomeServizio + ".idDominio.label"),
								dominioTitolo));
			}
		} catch (ServiceException e) {
			// dominio non censito metto solo il coddominio
			voci.put(Utils.getInstance(this.getLanguage()).getMessageFromResourceBundle(this.nomeServizio + ".idDominio.id"),
					new Voce<String>(Utils.getInstance(this.getLanguage()).getMessageFromResourceBundle(this.nomeServizio + ".idDominio.label"),
							entry.getCodDominio()));
		}

		return voci; 
	}

	@Override
	public String esporta(List<Long> idsToExport, List<RawParamValue> rawValues, UriInfo uriInfo, BasicBD bd, ZipOutputStream zout)
			throws WebApplicationException, ConsoleException ,ExportException{
		StringBuffer sb = new StringBuffer();
		if(idsToExport != null && idsToExport.size() > 0) {
			for (Long long1 : idsToExport) {

				if(sb.length() > 0) {
					sb.append(", ");
				}

				sb.append(long1);
			}
		}

		String methodName = "esporta " + this.titoloServizio + "[" + sb.toString() + "]";

		int numeroZipEntries = 0;
		String pathLoghi = ConsoleProperties.getInstance().getPathEstrattoContoPdfLoghi();

		//		if(idsToExport.size() == 1) {
		//			return this.esporta(idsToExport.get(0), uriInfo, bd, zout);
		//		} 

		String fileName = "Export.zip";
		try{
			this.log.info("Esecuzione " + methodName + " in corso...");
			// Operazione consentita solo ai ruoli con diritto di lettura
			this.darsService.checkDirittiServizioLettura(bd, this.funzionalita);
			int limit = ConsoleProperties.getInstance().getNumeroMassimoElementiExport();
			boolean simpleSearch = Utils.containsParameter(rawValues, DarsService.SIMPLE_SEARCH_PARAMETER_ID);
			IncassiBD incassiBD = new IncassiBD(bd);
			IncassoFilter filter = incassiBD.newFilter(simpleSearch);

			// se ho ricevuto anche gli id li utilizzo per fare il check della count
			if(idsToExport != null && idsToExport.size() > 0) 
				filter.setIdIncasso(idsToExport);

			boolean eseguiRicerca = this.popolaFiltroRicerca(rawValues, bd, simpleSearch, filter);

			if(!eseguiRicerca){
				List<String> msg = new ArrayList<String>();
				msg.add(Utils.getInstance(this.getLanguage()).getMessageFromResourceBundle(this.nomeServizio+".esporta.operazioneNonPermessa"));
				throw new ExportException(msg, EsitoOperazione.ERRORE);
			}

			long count = incassiBD.count(filter);

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
			fsw.setField(it.govpay.orm.Incasso.model().DATA_ORA_INCASSO);
			fsw.setSortOrder(SortOrder.DESC);
			filter.getFilterSortList().add(fsw);

			List<Incasso> findAll = incassiBD.findAll(filter);

			for (Incasso incasso : findAll) {
				Applicazione applicazione = incasso.getApplicazione(incassiBD);
				List<Pagamento> pagamenti = incasso.getPagamenti(incassiBD);
				List<it.govpay.model.Pagamento> pagamentiList = new ArrayList<it.govpay.model.Pagamento>();
				pagamentiList.addAll(pagamenti);
				ByteArrayOutputStream baos = new ByteArrayOutputStream();
				IncassoPdf.getPdfIncasso(pathLoghi, incasso, pagamentiList, applicazione, baos,this.log);
				String incassoPdfEntryName = incasso.getTrn() + ".pdf";
				numeroZipEntries ++;
				ZipEntry rtPdf = new ZipEntry(incassoPdfEntryName);
				zout.putNextEntry(rtPdf);
				zout.write(baos.toByteArray());
				zout.closeEntry();
			}

			// se non ho inserito nessuna entry
			if(numeroZipEntries == 0){
				String noEntriesTxt = "/README";
				ZipEntry entryTxt = new ZipEntry(noEntriesTxt);
				zout.putNextEntry(entryTxt);
				zout.write("Non sono state trovate informazioni sugli incassi selezionati.".getBytes());
				zout.closeEntry();
			}


			zout.flush();
			zout.close();

			this.log.info("Esecuzione " + methodName + " completata.");

			return fileName;
		}catch(ExportException e){
			throw e;
		}catch(WebApplicationException e){
			throw e;
		}catch(Exception e){
			throw new ConsoleException(e);
		}
	}

	@Override
	public String esporta(Long idToExport, List<RawParamValue> rawValues, UriInfo uriInfo, BasicBD bd, ZipOutputStream zout)
			throws WebApplicationException, ConsoleException,ExportException {
		String methodName = "esporta " + this.titoloServizio + "[" + idToExport + "]";  

		try{
			int numeroZipEntries = 0;
			this.log.info("Esecuzione " + methodName + " in corso...");
			List<Long> setDomini = this.darsService.getIdDominiAbilitatiLetturaServizio(bd, this.funzionalita);
			boolean eseguiRicerca = !setDomini.isEmpty();
			List<String> idDomini = new ArrayList<String>();
			IncassiBD incassiBD = new IncassiBD(bd);
			IncassoFilter filter = incassiBD.newFilter();

			List<Long> ids = new ArrayList<Long>();
			ids.add(idToExport);

			if(!setDomini.contains(-1L)){
				List<Long> lstCodDomini = new ArrayList<Long>();
				lstCodDomini.addAll(setDomini);
				idDomini.addAll(this.toListCodDomini(lstCodDomini, bd));
				filter.setCodDomini(idDomini);
			}

			if(eseguiRicerca){
				filter.setIdIncasso(ids);
				eseguiRicerca = eseguiRicerca && incassiBD.count(filter) > 0;
			}

			Incasso incasso = eseguiRicerca ? incassiBD.getIncasso(idToExport) : null;
			String fileName = "Export.zip";  

			if(incasso != null){
				String pathLoghi = ConsoleProperties.getInstance().getPathEstrattoContoPdfLoghi();
				ByteArrayOutputStream baos = new ByteArrayOutputStream();
				Applicazione applicazione = incasso.getApplicazione(incassiBD);
				List<Pagamento> pagamenti = incasso.getPagamenti(incassiBD);
				List<it.govpay.model.Pagamento> pagamentiList = new ArrayList<it.govpay.model.Pagamento>();
				pagamentiList.addAll(pagamenti);
				IncassoPdf.getPdfIncasso(pathLoghi, incasso, pagamentiList , applicazione, baos,this.log);
				String incassoPdfEntryName = incasso.getTrn() + ".pdf";
				numeroZipEntries ++;
				ZipEntry rtPdf = new ZipEntry(incassoPdfEntryName);
				zout.putNextEntry(rtPdf);
				zout.write(baos.toByteArray());
				zout.closeEntry();
			}

			// se non ho inserito nessuna entry
			if(numeroZipEntries == 0){
				String noEntriesTxt = "/README";
				ZipEntry entryTxt = new ZipEntry(noEntriesTxt);
				zout.putNextEntry(entryTxt);
				zout.write("Non sono state trovate informazioni sugli incassi selezionati.".getBytes());
				zout.closeEntry();
			}

			zout.flush();
			zout.close();

			this.log.info("Esecuzione " + methodName + " completata.");

			return fileName;
		}catch(ExportException e){
			throw e;
		}catch(WebApplicationException e){
			throw e;
		}catch(Exception e){
			throw new ConsoleException(e);
		}
	}

	@Override
	public InfoForm getInfoCancellazione(UriInfo uriInfo, BasicBD bd, Map<String,String> parameters) throws ConsoleException {
		return null;
	}

	@Override
	public InfoForm getInfoCancellazioneDettaglio(UriInfo uriInfo, BasicBD bd, Incasso entry) throws ConsoleException {
		return null;
	}

	@Override
	public InfoForm getInfoEsportazione(UriInfo uriInfo, BasicBD bd, Map<String,String> parameters) throws ConsoleException { 
		InfoForm infoEsportazione = null;
		try{
			if(this.darsService.isServizioAbilitatoLettura(bd, this.funzionalita)){
				URI esportazione = this.getUriEsportazione(uriInfo, bd);
				infoEsportazione = new InfoForm(esportazione);
			}
		}catch(ServiceException e){
			throw new ConsoleException(e);
		}
		return infoEsportazione;	
	}

	@Override
	public InfoForm getInfoEsportazioneDettaglio(UriInfo uriInfo, BasicBD bd, Incasso entry)	throws ConsoleException {	
		InfoForm infoEsportazione = null;
		try{
			if(this.darsService.isServizioAbilitatoLettura(bd, this.funzionalita)){
				URI esportazione = this.getUriEsportazioneDettaglio(uriInfo, bd, entry.getId());
				infoEsportazione = new InfoForm(esportazione);
			}
		}catch(ServiceException e){
			throw new ConsoleException(e);
		}
		return infoEsportazione;
	}

	/* Creazione/Update non consentiti**/

	@Override
	public InfoForm getInfoCreazione(UriInfo uriInfo, BasicBD bd) throws ConsoleException { return null; }

	@Override
	public InfoForm getInfoModifica(UriInfo uriInfo, BasicBD bd, Incasso entry) throws ConsoleException { return null; }

	@Override
	public Elenco delete(List<Long> idsToDelete, List<RawParamValue> rawValues, UriInfo uriInfo, BasicBD bd) throws WebApplicationException, ConsoleException, DeleteException {
		return null;
	}

	@Override
	public Incasso creaEntry(InputStream is, UriInfo uriInfo, BasicBD bd) throws WebApplicationException, ConsoleException { return null; }

	@Override
	public Dettaglio insert(InputStream is, UriInfo uriInfo, BasicBD bd) throws WebApplicationException, ConsoleException, ValidationException, DuplicatedEntryException { return null; }

	@Override
	public void checkEntry(Incasso entry, Incasso oldEntry) throws ValidationException { }

	@Override
	public Dettaglio update(InputStream is, UriInfo uriInfo, BasicBD bd) throws WebApplicationException, ConsoleException, ValidationException { return null; }

	@Override
	public Object uplaod(MultipartFormDataInput input, UriInfo uriInfo, BasicBD bd)	throws WebApplicationException, ConsoleException, ValidationException { return null;}
}
