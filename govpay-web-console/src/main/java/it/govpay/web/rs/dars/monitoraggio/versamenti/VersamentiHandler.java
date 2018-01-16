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
package it.govpay.web.rs.dars.monitoraggio.versamenti;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.math.BigDecimal;
import java.net.URI;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
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
import org.openspcoop2.generic_project.exception.ServiceException;
import org.openspcoop2.generic_project.expression.SortOrder;
import org.openspcoop2.utils.csv.Printer;

import it.govpay.bd.BasicBD;
import it.govpay.bd.FilterSortWrapper;
import it.govpay.bd.anagrafica.AnagraficaManager;
import it.govpay.bd.anagrafica.DominiBD;
import it.govpay.bd.anagrafica.filters.DominioFilter;
import it.govpay.bd.exception.VersamentoException;
import it.govpay.bd.model.Dominio;
import it.govpay.bd.model.SingoloVersamento;
import it.govpay.bd.model.Tributo;
import it.govpay.bd.model.UnitaOperativa;
import it.govpay.bd.model.Versamento;
import it.govpay.bd.pagamento.IuvBD;
import it.govpay.bd.pagamento.VersamentiBD;
import it.govpay.bd.pagamento.filters.IuvFilter;
import it.govpay.bd.pagamento.filters.VersamentoFilter;
import it.govpay.bd.reportistica.EstrattiContoBD;
import it.govpay.bd.reportistica.filters.EstrattoContoFilter;
import it.govpay.core.utils.CSVUtils;
import it.govpay.model.Anagrafica;
import it.govpay.model.Applicazione;
import it.govpay.model.EstrattoConto;
import it.govpay.model.IbanAccredito;
import it.govpay.model.Iuv;
import it.govpay.model.Tributo.TipoContabilta;
import it.govpay.model.Versamento.StatoVersamento;
import it.govpay.model.comparator.EstrattoContoComparator;
import it.govpay.web.rs.dars.anagrafica.anagrafica.AnagraficaHandler;
import it.govpay.web.rs.dars.anagrafica.domini.Domini;
import it.govpay.web.rs.dars.anagrafica.domini.DominiHandler;
import it.govpay.web.rs.dars.anagrafica.tributi.Tributi;
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
import it.govpay.web.rs.dars.model.input.base.InputText;
import it.govpay.web.rs.dars.model.input.base.InputTextArea;
import it.govpay.web.rs.dars.model.input.base.SelectList;
import it.govpay.web.utils.ConsoleProperties;
import it.govpay.web.utils.Utils;

public class VersamentiHandler extends DarsHandler<Versamento> implements IDarsHandler<Versamento>{

	public static final String ANAGRAFICA_DEBITORE = "anagrafica";
	private SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm");  

	public VersamentiHandler(Logger log, DarsService darsService) { 
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

			VersamentiBD versamentiBD = new VersamentiBD(bd);
			boolean simpleSearch = this.containsParameter(uriInfo, DarsService.SIMPLE_SEARCH_PARAMETER_ID); 

			VersamentoFilter filter = versamentiBD.newFilter(simpleSearch);
			filter.setOffset(offset);
			filter.setLimit(limit);
			FilterSortWrapper fsw = new FilterSortWrapper();
			fsw.setField(it.govpay.orm.Versamento.model().DATA_ORA_ULTIMO_AGGIORNAMENTO);
			fsw.setSortOrder(SortOrder.DESC);
			filter.getFilterSortList().add(fsw);

			boolean eseguiRicerca = this.popolaFiltroRicerca(uriInfo, versamentiBD, simpleSearch, filter);

			long count = eseguiRicerca ? versamentiBD.count(filter) : 0;

			// visualizza la ricerca solo se i risultati sono > del limit
			boolean visualizzaRicerca = this.visualizzaRicerca(count, limit);
			InfoForm infoRicerca = this.getInfoRicerca(uriInfo, bd, visualizzaRicerca);

			// Indico la visualizzazione custom
			String formatter = Utils.getInstance(this.getLanguage()).getMessageFromResourceBundle(this.nomeServizio+".elenco.formatter");
			String simpleSearchPlaceholder = Utils.getInstance(this.getLanguage()).getMessageFromResourceBundle(this.nomeServizio+".simpleSearch.placeholder");
			Elenco elenco = new Elenco(this.titoloServizio, infoRicerca,
					this.getInfoCreazione(uriInfo, bd),
					count, this.getInfoEsportazione(uriInfo, bd), this.getInfoCancellazione(uriInfo, bd),simpleSearchPlaceholder); 

			List<Versamento> findAll = eseguiRicerca ? versamentiBD.findAll(filter) : new ArrayList<Versamento>(); 

			if(findAll != null && findAll.size() > 0){
				for (Versamento entry : findAll) {
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

	private boolean popolaFiltroRicerca(UriInfo uriInfo, BasicBD bd, boolean simpleSearch, VersamentoFilter filter) throws ConsoleException, Exception {
		Set<Long> setDomini = this.darsService.getIdDominiAbilitatiLetturaServizio(bd, this.funzionalita);
		boolean eseguiRicerca = !setDomini.isEmpty();
		List<Long> idDomini = new ArrayList<Long>();
		// SE l'operatore non e' admin vede solo i versamenti associati ai domini definiti nelle ACL
		boolean iuvNonEsistente = false;

		if(simpleSearch){
			// simplesearch
			String simpleSearchString = this.getParameter(uriInfo, DarsService.SIMPLE_SEARCH_PARAMETER_ID, String.class);
			if(StringUtils.isNotEmpty(simpleSearchString)) {
				filter.setSimpleSearchString(simpleSearchString);
			}
		}else{
			String cfDebitoreId = Utils.getInstance(this.getLanguage()).getMessageFromResourceBundle(this.nomeServizio + ".cfDebitore.id");
			String cfDebitore = this.getParameter(uriInfo, cfDebitoreId, String.class);
			if(StringUtils.isNotEmpty(cfDebitore)) {
				filter.setCodUnivocoDebitore(cfDebitore);
			} 

			String codVersamentoId = Utils.getInstance(this.getLanguage()).getMessageFromResourceBundle(this.nomeServizio + ".codVersamento.id");
			String codVersamento = this.getParameter(uriInfo, codVersamentoId, String.class);
			if(StringUtils.isNotEmpty(codVersamento)) {
				filter.setCodVersamento(codVersamento);
			}


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

			String iuvId = Utils.getInstance(this.getLanguage()).getMessageFromResourceBundle(this.nomeServizio + ".iuv.id");
			String iuv = this.getParameter(uriInfo, iuvId, String.class);

			if(StringUtils.isNotEmpty(iuv)){
				IuvBD iuvBd = new IuvBD(bd);
				IuvFilter newFilter = iuvBd.newFilter();
				newFilter.setIuv(iuv);
				List<Iuv> findAll = iuvBd.findAll(newFilter);
				List<Long> idApplicazioneL = new ArrayList<Long>();
				List<String> codVersamentoEnte = new ArrayList<String>();
				for (Iuv iuv2 : findAll) {
					idApplicazioneL.add(iuv2.getIdApplicazione());
					codVersamentoEnte.add(iuv2.getCodVersamentoEnte());
				}
				// iuv inserito in pagina non corrisponde a nessun rpt
				iuvNonEsistente = findAll.size() == 0;

				filter.setIdApplicazione(idApplicazioneL);
				filter.setCodVersamentoEnte(codVersamentoEnte);
			}

			String statoVersamentoId = Utils.getInstance(this.getLanguage()).getMessageFromResourceBundle(this.nomeServizio + ".statoVersamento.id");
			String statoVersamento = this.getParameter(uriInfo, statoVersamentoId, String.class);

			if(StringUtils.isNotEmpty(statoVersamento)){
				filter.setStatoVersamento(StatoVersamento.valueOf(statoVersamento));
			}
		}

		if(eseguiRicerca &&!setDomini.contains(-1L)){
			List<Long> lstCodDomini = new ArrayList<Long>();
			lstCodDomini.addAll(setDomini);
			idDomini.addAll(setDomini);
			filter.setIdDomini(idDomini);
		}
		return eseguiRicerca && !iuvNonEsistente;
	}

	private boolean popolaFiltroRicerca(List<RawParamValue> rawValues, BasicBD bd , boolean simpleSearch, VersamentoFilter filter) throws ConsoleException, Exception {
		Set<Long> setDomini = this.darsService.getIdDominiAbilitatiLetturaServizio(bd, this.funzionalita);
		boolean eseguiRicerca = !setDomini.isEmpty();
		List<Long> idDomini = new ArrayList<Long>();
		// SE l'operatore non e' admin vede solo i versamenti associati ai domini definiti nelle ACL
		boolean iuvNonEsistente = false;

		if(simpleSearch){
			// simplesearch
			String simpleSearchString = Utils.getValue(rawValues, DarsService.SIMPLE_SEARCH_PARAMETER_ID);
			if(StringUtils.isNotEmpty(simpleSearchString)) {
				filter.setSimpleSearchString(simpleSearchString);
			}
		}else{
			String cfDebitoreId = Utils.getInstance(this.getLanguage()).getMessageFromResourceBundle(this.nomeServizio + ".cfDebitore.id");
			String cfDebitore = Utils.getValue(rawValues, cfDebitoreId);
			if(StringUtils.isNotEmpty(cfDebitore)) {
				filter.setCodUnivocoDebitore(cfDebitore);
			} 

			String codVersamentoId = Utils.getInstance(this.getLanguage()).getMessageFromResourceBundle(this.nomeServizio + ".codVersamento.id");
			String codVersamento = Utils.getValue(rawValues, codVersamentoId);
			if(StringUtils.isNotEmpty(codVersamento)) {
				filter.setCodVersamento(codVersamento);
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

			String iuvId = Utils.getInstance(this.getLanguage()).getMessageFromResourceBundle(this.nomeServizio + ".iuv.id");
			String iuv = Utils.getValue(rawValues, iuvId);

			if(StringUtils.isNotEmpty(iuv)){
				IuvBD iuvBd = new IuvBD(bd);
				IuvFilter newFilter = iuvBd.newFilter();
				newFilter.setIuv(iuv);
				List<Iuv> findAll = iuvBd.findAll(newFilter);
				List<Long> idApplicazioneL = new ArrayList<Long>();
				List<String> codVersamentoEnte = new ArrayList<String>();
				for (Iuv iuv2 : findAll) {
					idApplicazioneL.add(iuv2.getIdApplicazione());
					codVersamentoEnte.add(iuv2.getCodVersamentoEnte());
				}
				// iuv inserito in pagina non corrisponde a nessun rpt
				iuvNonEsistente = findAll.size() == 0;

				filter.setIdApplicazione(idApplicazioneL);
				filter.setCodVersamentoEnte(codVersamentoEnte);
			}

			String statoVersamentoId = Utils.getInstance(this.getLanguage()).getMessageFromResourceBundle(this.nomeServizio + ".statoVersamento.id");
			String statoVersamento = Utils.getValue(rawValues, statoVersamentoId);

			if(StringUtils.isNotEmpty(statoVersamento)){
				filter.setStatoVersamento(StatoVersamento.valueOf(statoVersamento));
			}
		}

		if(eseguiRicerca &&!setDomini.contains(-1L)){
			List<Long> lstCodDomini = new ArrayList<Long>();
			lstCodDomini.addAll(setDomini);
			idDomini.addAll(setDomini);
			filter.setIdDomini(idDomini);
		}

		return eseguiRicerca && !iuvNonEsistente;
	}

	@SuppressWarnings("unchecked")
	@Override
	public InfoForm getInfoRicerca(UriInfo uriInfo, BasicBD bd, boolean visualizzaRicerca, Map<String,String> parameters) throws ConsoleException {
		URI ricerca = this.getUriRicerca(uriInfo, bd);
		InfoForm infoRicerca = new InfoForm(ricerca);

		if(visualizzaRicerca) {
			String cfDebitoreId = Utils.getInstance(this.getLanguage()).getMessageFromResourceBundle(this.nomeServizio + ".cfDebitore.id");
			String codVersamentoId = Utils.getInstance(this.getLanguage()).getMessageFromResourceBundle(this.nomeServizio + ".codVersamento.id");
			String idDominioId = Utils.getInstance(this.getLanguage()).getMessageFromResourceBundle(this.nomeServizio + ".idDominio.id");
			String iuvId = Utils.getInstance(this.getLanguage()).getMessageFromResourceBundle(this.nomeServizio + ".iuv.id");
			String statoVersamentoId = Utils.getInstance(this.getLanguage()).getMessageFromResourceBundle(this.nomeServizio + ".statoVersamento.id");

			if(this.infoRicercaMap == null){
				this.initInfoRicerca(uriInfo, bd);
			}

			Sezione sezioneRoot = infoRicerca.getSezioneRoot();

			InputText codVersamento = (InputText) this.infoRicercaMap.get(codVersamentoId);
			codVersamento.setDefaultValue(null);
			sezioneRoot.addField(codVersamento);

			try{

				Set<Long> setDomini = this.darsService.getIdDominiAbilitatiLetturaServizio(bd, this.funzionalita);
				boolean eseguiRicerca = !setDomini.isEmpty();
				List<Long> idDomini = new ArrayList<Long>();

				// idDominio
				List<Voce<Long>> domini = new ArrayList<Voce<Long>>();

				DominiBD dominiBD = new DominiBD(bd);
				DominioFilter filter;
				try {
					filter = dominiBD.newFilter();
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

			InputText iuv = (InputText) this.infoRicercaMap.get(iuvId);
			iuv.setDefaultValue(null);
			sezioneRoot.addField(iuv);

			InputText cfDebitore = (InputText) this.infoRicercaMap.get(cfDebitoreId);
			cfDebitore.setDefaultValue(null);
			sezioneRoot.addField(cfDebitore);

			SelectList<String> statoVersamento = (SelectList<String>) this.infoRicercaMap.get(statoVersamentoId);
			statoVersamento.setDefaultValue("");
			sezioneRoot.addField(statoVersamento);

		}
		return infoRicerca;
	}

	private void initInfoRicerca(UriInfo uriInfo, BasicBD bd) throws ConsoleException{
		if(this.infoRicercaMap == null){
			this.infoRicercaMap = new HashMap<String, ParamField<?>>();

			String cfDebitoreId = Utils.getInstance(this.getLanguage()).getMessageFromResourceBundle(this.nomeServizio + ".cfDebitore.id");
			String codVersamentoId = Utils.getInstance(this.getLanguage()).getMessageFromResourceBundle(this.nomeServizio + ".codVersamento.id");
			String idDominioId = Utils.getInstance(this.getLanguage()).getMessageFromResourceBundle(this.nomeServizio + ".idDominio.id");
			String iuvId = Utils.getInstance(this.getLanguage()).getMessageFromResourceBundle(this.nomeServizio + ".iuv.id");
			String statoVersamentoId = Utils.getInstance(this.getLanguage()).getMessageFromResourceBundle(this.nomeServizio + ".statoVersamento.id");

			// statoVersamento
			List<Voce<String>> stati = new ArrayList<Voce<String>>();
			stati.add(new Voce<String>(Utils.getInstance(this.getLanguage()).getMessageFromResourceBundle("commons.label.qualsiasi"), ""));
			stati.add(new Voce<String>(Utils.getInstance(this.getLanguage()).getMessageFromResourceBundle(this.nomeServizio + ".statoVersamento."+StatoVersamento.ESEGUITO), StatoVersamento.ESEGUITO.toString()));
			stati.add(new Voce<String>(Utils.getInstance(this.getLanguage()).getMessageFromResourceBundle(this.nomeServizio + ".statoVersamento."+StatoVersamento.NON_ESEGUITO), StatoVersamento.NON_ESEGUITO.toString()));
			stati.add(new Voce<String>(Utils.getInstance(this.getLanguage()).getMessageFromResourceBundle(this.nomeServizio + ".statoVersamento."+StatoVersamento.PARZIALMENTE_ESEGUITO), StatoVersamento.PARZIALMENTE_ESEGUITO.toString()));
			stati.add(new Voce<String>(Utils.getInstance(this.getLanguage()).getMessageFromResourceBundle(this.nomeServizio + ".statoVersamento."+StatoVersamento.ESEGUITO_SENZA_RPT), StatoVersamento.ESEGUITO_SENZA_RPT.toString()));
			stati.add(new Voce<String>(Utils.getInstance(this.getLanguage()).getMessageFromResourceBundle(this.nomeServizio + ".statoVersamento."+StatoVersamento.ANOMALO), StatoVersamento.ANOMALO.toString()));
			stati.add(new Voce<String>(Utils.getInstance(this.getLanguage()).getMessageFromResourceBundle(this.nomeServizio + ".statoVersamento."+StatoVersamento.ANNULLATO), StatoVersamento.ANNULLATO.toString()));

			String statoVersamentoLabel = Utils.getInstance(this.getLanguage()).getMessageFromResourceBundle(this.nomeServizio + ".statoVersamento.label");
			SelectList<String> statoVersamento = new SelectList<String>(statoVersamentoId, statoVersamentoLabel, null, false, false, true, stati);
			this.infoRicercaMap.put(statoVersamentoId, statoVersamento);

			// cfDebitore
			String cfDebitoreLabel = Utils.getInstance(this.getLanguage()).getMessageFromResourceBundle(this.nomeServizio + ".cfDebitore.label");
			InputText cfDebitore = new InputText(cfDebitoreId, cfDebitoreLabel, null, false, false, true, 1, 35);
			this.infoRicercaMap.put(cfDebitoreId, cfDebitore);

			// Id Versamento
			String codVersamentoLabel = Utils.getInstance(this.getLanguage()).getMessageFromResourceBundle(this.nomeServizio + ".codVersamento.label");
			InputText codVersamento = new InputText(codVersamentoId, codVersamentoLabel, null, false, false, true, 1, 35);
			this.infoRicercaMap.put(codVersamentoId, codVersamento);

			// iuv
			String iuvLabel = Utils.getInstance(this.getLanguage()).getMessageFromResourceBundle(this.nomeServizio + ".iuv.label");
			InputText iuv = new InputText(iuvId, iuvLabel, null, false, false, true, 1, 35);
			this.infoRicercaMap.put(iuvId, iuv);			

			List<Voce<Long>> domini = new ArrayList<Voce<Long>>();
			// idDominio
			String idDominioLabel = Utils.getInstance(this.getLanguage()).getMessageFromResourceBundle(this.nomeServizio + ".idDominio.label");
			SelectList<Long> idDominio = new SelectList<Long>(idDominioId, idDominioLabel, null, false, false, true, domini);
			this.infoRicercaMap.put(idDominioId, idDominio);

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

			Set<Long> setDomini = this.darsService.getIdDominiAbilitatiLetturaServizio(bd, this.funzionalita);
			boolean eseguiRicerca = !setDomini.isEmpty();
			List<Long> idDomini = new ArrayList<Long>();

			VersamentiBD versamentiBD = new VersamentiBD(bd);
			if(eseguiRicerca && !setDomini.contains(-1L)){
				VersamentoFilter filter = versamentiBD.newFilter();

				List<Long> lstCodDomini = new ArrayList<Long>();
				lstCodDomini.addAll(setDomini);
				idDomini.addAll(setDomini);
				filter.setIdDomini(idDomini);
				List<Long> idVersamentoL = new ArrayList<Long>();
				idVersamentoL.add(id);
				filter.setIdVersamento(idVersamentoL);

				long count = eseguiRicerca ? versamentiBD.count(filter) : 0;
				eseguiRicerca = eseguiRicerca && count > 0;
			}

			// recupero oggetto
			Versamento versamento = eseguiRicerca ? versamentiBD.getVersamento(id) : null;

			InfoForm infoModifica = null;
			InfoForm infoCancellazione = versamento != null ? this.getInfoCancellazioneDettaglio(uriInfo, bd, versamento) : null;
			InfoForm infoEsportazione = versamento != null ? this.getInfoEsportazioneDettaglio(uriInfo, bd, versamento): null;

			String titolo = Utils.getInstance(this.getLanguage()).getMessageFromResourceBundle(this.nomeServizio + ".dettaglioVersamento") ;
			Dettaglio dettaglio = new Dettaglio(titolo, infoEsportazione, infoCancellazione, infoModifica);

			it.govpay.web.rs.dars.model.Sezione root = dettaglio.getSezioneRoot();

			if(versamento != null){

				// Applicazione
				Applicazione applicazione = versamento.getApplicazione(bd);
				if(applicazione != null) {
					root.addVoce(Utils.getInstance(this.getLanguage()).getMessageFromResourceBundle(this.nomeServizio + ".applicazione.label"), applicazione.getCodApplicazione());
				}

				if(StringUtils.isNotEmpty(versamento.getCodVersamentoEnte())) {
					root.addVoce(Utils.getInstance(this.getLanguage()).getMessageFromResourceBundle(this.nomeServizio + ".codVersamentoEnte.label"), versamento.getCodVersamentoEnte());
				}
				// Uo
				UnitaOperativa uo = versamento.getUo(bd);
				if(uo != null){
					Dominio dominio = uo.getDominio(bd);
					Domini dominiDars = new Domini();
					Elemento elemento = ((DominiHandler)dominiDars.getDarsHandler()).getElemento(dominio, dominio.getId(), dominiDars.getPathServizio(), bd);
					root.addVoce(Utils.getInstance(this.getLanguage()).getMessageFromResourceBundle(this.nomeServizio + ".idDominio.label"), elemento.getTitolo()); 
				}

				// iuv
				Iuv iuv = versamento.getIuv(bd);
				if(iuv != null) {
					root.addVoce(Utils.getInstance(this.getLanguage()).getMessageFromResourceBundle(this.nomeServizio + ".iuv.label"), iuv.getIuv()); 
				}

				if(versamento.getCausaleVersamento() != null) {
					root.addVoce(Utils.getInstance(this.getLanguage()).getMessageFromResourceBundle(this.nomeServizio + ".causaleVersamento.label"), versamento.getCausaleVersamento().toString());
				}

				if(versamento.getImportoTotale() != null) {
					root.addVoce(Utils.getInstance(this.getLanguage()).getMessageFromResourceBundle(this.nomeServizio + ".importoTotale.label"),
							this.currencyUtils.getCurrencyAsEuro(versamento.getImportoTotale()));
				}

				if(versamento.getStatoVersamento() != null) {
					root.addVoce(Utils.getInstance(this.getLanguage()).getMessageFromResourceBundle(this.nomeServizio + ".statoVersamento.label"),
							Utils.getInstance(this.getLanguage()).getMessageFromResourceBundle(this.nomeServizio + ".statoVersamento."+versamento.getStatoVersamento()));
				}

				if(StringUtils.isNotEmpty(versamento.getDescrizioneStato())) {
					root.addVoce(Utils.getInstance(this.getLanguage()).getMessageFromResourceBundle(this.nomeServizio + ".descrizioneStato.label"), versamento.getDescrizioneStato());
				}

				if(versamento.getDataCreazione() != null) {
					root.addVoce(Utils.getInstance(this.getLanguage()).getMessageFromResourceBundle(this.nomeServizio + ".dataCreazione.label"), this.sdf.format(versamento.getDataCreazione()));
				}
				if(versamento.getDataScadenza() != null) {
					root.addVoce(Utils.getInstance(this.getLanguage()).getMessageFromResourceBundle(this.nomeServizio + ".dataScadenza.label"), this.sdf.format(versamento.getDataScadenza()));
				}
				if(versamento.getDataUltimoAggiornamento() != null) {
					root.addVoce(Utils.getInstance(this.getLanguage()).getMessageFromResourceBundle(this.nomeServizio + ".dataUltimoAggiornamento.label"), this.sdf.format(versamento.getDataUltimoAggiornamento()));
				}
				root.addVoce(Utils.getInstance(this.getLanguage()).getMessageFromResourceBundle(this.nomeServizio + ".aggiornabile.label"), Utils.getSiNoAsLabel(versamento.isAggiornabile()));


				// Sezione Anagrafica Debitore

				Anagrafica anagrafica = versamento.getAnagraficaDebitore(); 
				it.govpay.web.rs.dars.model.Sezione sezioneAnagrafica = dettaglio.addSezione(Utils.getInstance(this.getLanguage()).getMessageFromResourceBundle(this.nomeServizio + "." + ANAGRAFICA_DEBITORE + ".titolo"));
				AnagraficaHandler anagraficaHandler = new AnagraficaHandler(ANAGRAFICA_DEBITORE,this.nomeServizio,this.pathServizio,this.getLanguage());
				anagraficaHandler.fillSezioneAnagraficaUO(sezioneAnagrafica, anagrafica);

				// Singoli Versamenti
				List<SingoloVersamento> singoliVersamenti = versamento.getSingoliVersamenti(bd);
				if(!Utils.isEmpty(singoliVersamenti)){
					SingoliVersamenti svDars = new SingoliVersamenti();
					Tributi trDars =  new Tributi();
					if(singoliVersamenti != null && singoliVersamenti.size() > 0){
						for (SingoloVersamento entry : singoliVersamenti) {
							String etichettaSingoliVersamenti = Utils.getInstance(this.getLanguage()).getMessageFromResourceBundle(
									this.nomeServizio + ".elementoCorrelato.singoloVersamento.titolo");
							it.govpay.web.rs.dars.model.Sezione sezioneSingoloVersamento = dettaglio.addSezione(etichettaSingoliVersamenti);

							sezioneSingoloVersamento.addVoce(Utils.getInstance(this.getLanguage()).getMessageFromResourceBundle(svDars.getNomeServizio() + ".codSingoloVersamentoEnte.label"), 
									entry.getCodSingoloVersamentoEnte());

							BigDecimal importoSingoloVersamento = entry.getImportoSingoloVersamento() != null ? entry.getImportoSingoloVersamento() : BigDecimal.ZERO;
							sezioneSingoloVersamento.addVoce(Utils.getInstance(this.getLanguage()).getMessageFromResourceBundle(svDars.getNomeServizio() + ".importoSingoloVersamento.label"), 
									this.currencyUtils.getCurrencyAsEuro(importoSingoloVersamento));

							Tributo tributo = entry.getTributo(bd);
							if(tributo != null){
								sezioneSingoloVersamento.addVoce(Utils.getInstance(this.getLanguage()).getMessageFromResourceBundle(svDars.getNomeServizio() + ".tributo.label"),
										tributo.getDescrizione());


								IbanAccredito ibanAccredito = tributo.getIbanAccredito();
								if(ibanAccredito != null)
									sezioneSingoloVersamento.addVoce(Utils.getInstance(this.getLanguage()).getMessageFromResourceBundle(svDars.getNomeServizio() + ".ibanAccredito.label"),
											ibanAccredito.getCodIban());

								StringBuilder sb = new StringBuilder();

								TipoContabilta tipoContabilita = entry.getTipoContabilita(bd) != null ? entry.getTipoContabilita(bd) : TipoContabilta.CAPITOLO;
								String tipoContabilitaValue = null;
								switch (tipoContabilita) {
								case ALTRO:
									tipoContabilitaValue = Utils.getInstance(this.getLanguage()).getMessageFromResourceBundle(trDars.getNomeServizio() + ".tipoContabilita.altro");
									break;
								case SPECIALE:
									tipoContabilitaValue = Utils.getInstance(this.getLanguage()).getMessageFromResourceBundle(trDars.getNomeServizio() + ".tipoContabilita.speciale");
									break;
								case SIOPE:
									tipoContabilitaValue = Utils.getInstance(this.getLanguage()).getMessageFromResourceBundle(trDars.getNomeServizio() + ".tipoContabilita.siope");
									break;
								case CAPITOLO:
								default:
									tipoContabilitaValue = Utils.getInstance(this.getLanguage()).getMessageFromResourceBundle(trDars.getNomeServizio() + ".tipoContabilita.capitolo");				
									break;
								}

								String codContabilitaValue = StringUtils.isNotEmpty(entry.getCodContabilita(bd)) ? entry.getCodContabilita(bd) : "--";

								sb.append(tipoContabilitaValue).append("/").append(codContabilitaValue);

								sezioneSingoloVersamento.addVoce(Utils.getInstance(this.getLanguage()).getMessageFromResourceBundle(svDars.getNomeServizio() + ".contabilita.label"),
										sb.toString());
							}
						}
					}
				} 

				Transazioni transazioniDars = new Transazioni();
				String etichettaTransazioni = Utils.getInstance(this.getLanguage()).getMessageFromResourceBundle(this.nomeServizio + ".elementoCorrelato.transazioni.titolo");
				String versamentoId = Utils.getInstance(this.getLanguage()).getMessageFromResourceBundle(transazioniDars.getNomeServizio() + ".idVersamento.id");
				Map<String, String> params = new HashMap<String, String>();
				params = new HashMap<String, String>();
				params.put(versamentoId, versamento.getId()+ "");
				URI transazioneDettaglio = Utils.creaUriConParametri(transazioniDars.getPathServizio(), params );

				dettaglio.addElementoCorrelato(etichettaTransazioni, transazioneDettaglio);
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
	public String getTitolo(Versamento entry,BasicBD bd) {
		StringBuilder sb = new StringBuilder();

		String codVersamentoEnte = entry.getCodVersamentoEnte();

		StatoVersamento statoVersamento = entry.getStatoVersamento();

		String dominioLabel =  null;

		try{
			// Uo
			UnitaOperativa uo = entry.getUo(bd);
			if(uo != null){
				Dominio dominio = uo.getDominio(bd);
				Domini dominiDars = new Domini();
				Elemento elemento = ((DominiHandler)dominiDars.getDarsHandler()).getElemento(dominio, dominio.getId(), null, bd);
				dominioLabel = elemento.getTitolo(); 
			}
		}catch(Exception e){this.log.error(e);}

		switch (statoVersamento) {
		case NON_ESEGUITO:
			sb.append(Utils.getInstance(this.getLanguage()).getMessageWithParamsFromResourceBundle(this.nomeServizio + ".label.titolo.nonEseguito", codVersamentoEnte,dominioLabel));
			break;
		case ANNULLATO:
		case ANOMALO:
		case ESEGUITO_SENZA_RPT:
		case PARZIALMENTE_ESEGUITO:
		case ESEGUITO:
		default:
			sb.append(
					Utils.getInstance(this.getLanguage()).getMessageWithParamsFromResourceBundle(this.nomeServizio + ".label.titolo", codVersamentoEnte,dominioLabel,
							Utils.getInstance(this.getLanguage()).getMessageFromResourceBundle(this.nomeServizio + ".statoVersamento."+statoVersamento.name())));
			break;
		}

		return sb.toString();
	}

	@Override
	public String getSottotitolo(Versamento entry,BasicBD bd) {
		StringBuilder sb = new StringBuilder();
		Date dataUltimoAggiornamento = entry.getDataUltimoAggiornamento();


		StatoVersamento statoVersamento = entry.getStatoVersamento();
		Date dataScadenza = entry.getDataScadenza();

		switch (statoVersamento) {
		case NON_ESEGUITO:
			if(dataScadenza != null) {
				sb.append(Utils.getInstance(this.getLanguage()).getMessageWithParamsFromResourceBundle(this.nomeServizio + ".label.sottotitolo.nonEseguito",this.sdf.format(dataScadenza)));
			} else {
				sb.append(Utils.getInstance(this.getLanguage()).getMessageFromResourceBundle(this.nomeServizio + ".label.sottotitolo.nonEseguito.noScadenza"));
			}
			break;
		case ANOMALO:
			sb.append("");
			break;
		case ANNULLATO:
		case ESEGUITO_SENZA_RPT:
		case PARZIALMENTE_ESEGUITO:
		case ESEGUITO:
		default:
			sb.append(
					Utils.getInstance(this.getLanguage()).getMessageWithParamsFromResourceBundle(this.nomeServizio + ".label.sottotitolo",
							Utils.getInstance(this.getLanguage()).getMessageFromResourceBundle(this.nomeServizio + ".statoVersamento."+statoVersamento.name()),this.sdf.format(dataUltimoAggiornamento) ));
			break;
		}

		return sb.toString();
	} 

	@Override
	public Map<String, Voce<String>> getVoci(Versamento entry, BasicBD bd) throws ConsoleException {
		Map<String, Voce<String>> voci = new HashMap<String, Voce<String>>();
		try {



			// voci da inserire nella visualizzazione personalizzata 
			// logo, codversamentoente, iuv, piva/cf, importo, scadenza, stato
			if(StringUtils.isNotEmpty(entry.getCodVersamentoEnte())) {
				voci.put(Utils.getInstance(this.getLanguage()).getMessageFromResourceBundle(this.nomeServizio + ".id.id"),
						new Voce<String>(Utils.getInstance(this.getLanguage()).getMessageFromResourceBundle(this.nomeServizio + ".id.label"), entry.getCodVersamentoEnte()));
			}

			if(entry.getDataScadenza() != null) {
				voci.put(Utils.getInstance(this.getLanguage()).getMessageFromResourceBundle(this.nomeServizio + ".dataScadenza.id"),
						new Voce<String>(Utils.getInstance(this.getLanguage()).getMessageFromResourceBundle(this.nomeServizio + ".dataScadenza.label"), this.sdf.format(entry.getDataScadenza())));
			}


			Iuv iuv  = entry.getIuv(bd);
			if(iuv!= null && StringUtils.isNotEmpty(iuv.getIuv())) {
				voci.put(Utils.getInstance(this.getLanguage()).getMessageFromResourceBundle(this.nomeServizio + ".iuv.id"),
						new Voce<String>(Utils.getInstance(this.getLanguage()).getMessageFromResourceBundle(this.nomeServizio + ".iuv.label"), iuv.getIuv()));
			}

			// Dominio
			UnitaOperativa uo = entry.getUo(bd);
			if(uo != null){
				Dominio dominio = uo.getDominio(bd);
				Domini dominiDars = new Domini();
				String dominioTitolo = ((DominiHandler)dominiDars.getDarsHandler()).getTitolo(dominio, bd);
				voci.put(Utils.getInstance(this.getLanguage()).getMessageFromResourceBundle(this.nomeServizio + ".codDominio.id"),
						new Voce<String>(Utils.getInstance(this.getLanguage()).getMessageFromResourceBundle(this.nomeServizio + ".codDominio.label"),dominioTitolo)); 
			}

			if(entry.getStatoVersamento() != null) {
				voci.put(Utils.getInstance(this.getLanguage()).getMessageFromResourceBundle(this.nomeServizio + ".statoVersamento.id"),
						new Voce<String>(Utils.getInstance(this.getLanguage()).getMessageFromResourceBundle(this.nomeServizio + ".statoVersamento."+entry.getStatoVersamento().name()),
								entry.getStatoVersamento().name()));

				if(entry.getStatoVersamento().equals(StatoVersamento.ANNULLATO)) {
					voci.put(Utils.getInstance(this.getLanguage()).getMessageFromResourceBundle(this.nomeServizio + ".motivazioneAnnullamento.id"),
							new Voce<String>(Utils.getInstance(this.getLanguage()).getMessageFromResourceBundle(this.nomeServizio + ".motivazioneAnnullamento.label"),entry.getDescrizioneStato()));
				}
			}




			if(entry.getImportoTotale() != null) {
				voci.put(Utils.getInstance(this.getLanguage()).getMessageFromResourceBundle(this.nomeServizio + ".importoTotale.id"),
						new Voce<String>(Utils.getInstance(this.getLanguage()).getMessageFromResourceBundle(this.nomeServizio + ".importoTotale.label"), 
								this.currencyUtils.getCurrencyAsEuro(entry.getImportoTotale())));
			}

			Anagrafica anagrafica = entry.getAnagraficaDebitore(); 
			if(StringUtils.isNotEmpty(anagrafica.getCodUnivoco())){
				voci.put(Utils.getInstance(this.getLanguage()).getMessageFromResourceBundle(this.nomeServizio + ".cf.id"),
						new Voce<String>(Utils.getInstance(this.getLanguage()).getMessageFromResourceBundle(this.nomeServizio + ".cf.label"), anagrafica.getCodUnivoco()));
			}

		} catch (ServiceException e) {
			throw new ConsoleException(e);
		}

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
			// Operazione consentita solo ai ruoli con diritto di lettura
			this.darsService.checkDirittiServizioLettura(bd, this.funzionalita);
			int limit = ConsoleProperties.getInstance().getNumeroMassimoElementiExport();
			boolean simpleSearch = Utils.containsParameter(rawValues, DarsService.SIMPLE_SEARCH_PARAMETER_ID);
			VersamentiBD versamentiBD = new VersamentiBD(bd);
			it.govpay.core.business.EstrattoConto estrattoContoBD = new it.govpay.core.business.EstrattoConto(bd);

			Map<String, List<Long>> mappaInputEstrattoConto = new HashMap<String, List<Long>>();
			Map<String, Dominio> mappaInputDomini = new HashMap<String, Dominio>();

			VersamentoFilter filter = versamentiBD.newFilter(simpleSearch);

			// se ho ricevuto anche gli id li utilizzo per fare il check della count
			if(idsToExport != null && idsToExport.size() > 0) 
				filter.setIdVersamento(idsToExport);

			boolean eseguiRicerca = this.popolaFiltroRicerca(rawValues, bd, simpleSearch, filter);

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
			EstrattoContoFilter ecFilter = estrattiContoBD.newFilter(true);
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
		}catch(ExportException e){
			throw e;
		}catch(Exception e){
			throw new ConsoleException(e);
		}
	}

	@Override
	public String esporta(Long idToExport, List<RawParamValue> rawValues, UriInfo uriInfo, BasicBD bd, ZipOutputStream zout)
			throws WebApplicationException, ConsoleException,ExportException {
		String methodName = "esporta " + this.titoloServizio + "[" + idToExport + "]";  
		Printer printer  = null;

		try{
			int numeroZipEntries = 0;
			this.log.info("Esecuzione " + methodName + " in corso...");
			// Operazione consentita solo ai ruoli con diritto di lettura
			this.darsService.checkDirittiServizioLettura(bd, this.funzionalita);

			Set<Long> setDomini = this.darsService.getIdDominiAbilitatiLetturaServizio(bd, this.funzionalita);
			boolean eseguiRicerca = !setDomini.isEmpty();

			VersamentiBD versamentiBD = new VersamentiBD(bd);
			EstrattiContoBD estrattiContoBD = new EstrattiContoBD(bd);
			it.govpay.core.business.EstrattoConto estrattoContoBD = new it.govpay.core.business.EstrattoConto(bd);
			VersamentoFilter filter = versamentiBD.newFilter();

			List<Long> ids = new ArrayList<Long>();
			ids.add(idToExport);

			if(!setDomini.contains(-1L)){
				List<Long> lstCodDomini = new ArrayList<Long>();
				lstCodDomini.addAll(setDomini);
				filter.setIdDomini(lstCodDomini);

				// l'operatore puo' vedere i domini associati, controllo se c'e' un versamento con Id nei domini concessi.
				if(eseguiRicerca){
					filter.setIdVersamento(ids);
					eseguiRicerca = eseguiRicerca && versamentiBD.count(filter) > 0;
				}
			}

			Versamento versamento = eseguiRicerca ? versamentiBD.getVersamento(idToExport) : null;
			String fileName = "Export.zip";  

			if(versamento != null){
				// Prelevo il dominio

				UnitaOperativa uo  = AnagraficaManager.getUnitaOperativa(bd, versamento.getIdUo());
				Dominio dominio  = AnagraficaManager.getDominio(bd, uo.getIdDominio());

				// Estratto conto per iban e codiceversamento.
				List<Long> idVersamentiDominio = new ArrayList<Long>();
				idVersamentiDominio.add(idToExport);
				it.govpay.core.business.model.EstrattoConto input =  it.govpay.core.business.model.EstrattoConto.creaEstrattoContoVersamentiPDF(dominio, idVersamentiDominio);
				List<it.govpay.core.business.model.EstrattoConto> listInputEstrattoConto = new ArrayList<it.govpay.core.business.model.EstrattoConto>();
				listInputEstrattoConto.add(input);
				String pathLoghi = ConsoleProperties.getInstance().getPathEstrattoContoPdfLoghi();
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

				//Estratto conto in formato CSV
				EstrattoContoFilter ecFilter = estrattiContoBD.newFilter(true); 
				ecFilter.setIdVersamento(ids);
				List<EstrattoConto> findAll =  estrattiContoBD.estrattoContoFromIdVersamenti(ecFilter);

				if(findAll != null && findAll.size() > 0){
					//ordinamento record
					Collections.sort(findAll, new EstrattoContoComparator());
					numeroZipEntries ++;
					ByteArrayOutputStream baos  = new ByteArrayOutputStream();
					try{
						ZipEntry pagamentoCsv = new ZipEntry("estrattoConto.csv");
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
		}catch(ExportException e){
			throw e;
		}catch(Exception e){
			throw new ConsoleException(e);
		}
	}

	@Override
	public InfoForm getInfoCancellazione(UriInfo uriInfo, BasicBD bd, Map<String, String> parameters) throws ConsoleException {
		InfoForm infoCancellazione = null;

		try{
			if(this.darsService.isServizioAbilitatoScrittura(bd, this.funzionalita)){
				URI cancellazione = this.getUriCancellazione(uriInfo, bd);
				infoCancellazione = new InfoForm(cancellazione);
				List<String> titoli = new ArrayList<String>();
				titoli.add(Utils.getInstance(this.getLanguage()).getMessageFromResourceBundle(this.nomeServizio + ".cancellazione.singolo.titolo"));
				titoli.add(Utils.getInstance(this.getLanguage()).getMessageFromResourceBundle(this.nomeServizio + ".cancellazione.multiplo.titolo"));
				infoCancellazione.setTitolo(titoli); 

				String motivoCancellazioneId = Utils.getInstance(this.getLanguage()).getMessageFromResourceBundle(this.nomeServizio + ".motivoCancellazione.id");

				if(this.infoCancellazioneMap == null){
					this.initInfoCancellazione(uriInfo, bd);
				}

				Sezione sezioneRoot = infoCancellazione.getSezioneRoot();

				InputTextArea motivoCancellazione = (InputTextArea) this.infoCancellazioneMap.get(motivoCancellazioneId);
				motivoCancellazione.setDefaultValue(null);
				sezioneRoot.addField(motivoCancellazione);
			}
		}catch(ServiceException e){
			throw new ConsoleException(e);
		}
		return infoCancellazione;
	}

	private void initInfoCancellazione(UriInfo uriInfo, BasicBD bd) throws ConsoleException{
		if(this.infoCancellazioneMap == null){
			this.infoCancellazioneMap = new HashMap<String, ParamField<?>>();

			String motivoCancellazioneId = Utils.getInstance(this.getLanguage()).getMessageFromResourceBundle(this.nomeServizio + ".motivoCancellazione.id");

			String motivoCancellazioneLabel = Utils.getInstance(this.getLanguage()).getMessageFromResourceBundle(this.nomeServizio + ".motivoCancellazione.label");
			InputTextArea motivoCancellazione = new InputTextArea(motivoCancellazioneId, motivoCancellazioneLabel, null, true, false, true, 1, 255, 5, 100);
			this.infoCancellazioneMap.put(motivoCancellazioneId, motivoCancellazione);
		}
	}


	@Override
	public InfoForm getInfoCancellazioneDettaglio(UriInfo uriInfo, BasicBD bd, Versamento entry) throws ConsoleException {
		InfoForm infoCancellazione = null;

		try{
			if(this.darsService.isServizioAbilitatoScrittura(bd, this.funzionalita)){
				URI cancellazione = this.getUriCancellazioneDettaglio(uriInfo, bd, entry.getId());
				infoCancellazione = new InfoForm(cancellazione);
				List<String> titoli = new ArrayList<String>();
				titoli.add(Utils.getInstance(this.getLanguage()).getMessageFromResourceBundle(this.nomeServizio + ".cancellazione.singolo.titolo"));
				infoCancellazione.setTitolo(titoli); 

				String motivoCancellazioneId = Utils.getInstance(this.getLanguage()).getMessageFromResourceBundle(this.nomeServizio + ".motivoCancellazione.id");

				if(this.infoCancellazioneMap == null){
					this.initInfoCancellazione(uriInfo, bd);
				}

				Sezione sezioneRoot = infoCancellazione.getSezioneRoot();

				InputTextArea motivoCancellazione = (InputTextArea) this.infoCancellazioneMap.get(motivoCancellazioneId);
				motivoCancellazione.setDefaultValue(null);
				sezioneRoot.addField(motivoCancellazione);

			}
		}catch(ServiceException e){
			throw new ConsoleException(e);
		}
		return infoCancellazione;
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
	public InfoForm getInfoEsportazioneDettaglio(UriInfo uriInfo, BasicBD bd, Versamento entry)	throws ConsoleException {	
		InfoForm infoEsportazione = null;
		//		try{
		//			if(this.darsService.isServizioAbilitatoLettura(bd, this.funzionalita)){
		//				URI esportazione = this.getUriEsportazioneDettaglio(uriInfo, bd, entry.getId());
		//				infoEsportazione = new InfoForm(esportazione);
		//			}
		//		}catch(ServiceException e){
		//			throw new ConsoleException(e);
		//		}
		return infoEsportazione;	
	}

	/* Creazione/Update non consentiti**/

	@Override
	public InfoForm getInfoCreazione(UriInfo uriInfo, BasicBD bd) throws ConsoleException { return null; }

	@Override
	public InfoForm getInfoModifica(UriInfo uriInfo, BasicBD bd, Versamento entry) throws ConsoleException { return null; }

	@Override
	public Elenco delete(List<Long> idsToDelete, List<RawParamValue> rawValues, UriInfo uriInfo, BasicBD bd) throws WebApplicationException, ConsoleException, DeleteException {
		StringBuffer sb = new StringBuffer();
		if(idsToDelete != null && idsToDelete.size() > 0) {
			for (Long long1 : idsToDelete) {

				if(sb.length() > 0) {
					sb.append(", ");
				}

				sb.append(long1);
			}
		}

		String methodName = "delete " + this.titoloServizio + "[" + sb.toString() + "]";
		String motivoCancellazioneId = Utils.getInstance(this.getLanguage()).getMessageFromResourceBundle(this.nomeServizio + ".motivoCancellazione.id");
		String motivoCancellazioneLabel = Utils.getInstance(this.getLanguage()).getMessageFromResourceBundle(this.nomeServizio + ".motivoCancellazione.label");

		try{
			this.log.info("Esecuzione " + methodName + " in corso...");
			// Operazione consentita solo ai ruoli con diritto di scrittura
			this.darsService.checkDirittiServizioScrittura(bd, this.funzionalita); 

			VersamentiBD versamentiBD = new VersamentiBD(bd);
			versamentiBD.setAutoCommit(false);
			versamentiBD.enableSelectForUpdate();

			String motivoCancellazione = Utils.getValue(rawValues, motivoCancellazioneId);
			this.log.debug("Esecuzione " + methodName + ": Letto parametro ["+motivoCancellazioneLabel+"] con valore ["+motivoCancellazione+"]");

			List<String> lstErrori = new ArrayList<String>();
			try{
				for (Long id : idsToDelete) {
					Versamento versamento = versamentiBD.getVersamento(id);

					this.log.debug("Descrizione motivazione cancellamento: ["+motivoCancellazione+"]"); 
					versamento.setDescrizioneStato(motivoCancellazione);
					try{
						versamentiBD.annullaVersamento(versamento,motivoCancellazione);
					} catch (VersamentoException e) {
						String codEsito = e.getCodEsito();
						String msgEsito = Utils.getInstance(this.getLanguage()).getMessageFromResourceBundle(this.nomeServizio + ".cancellazione.esito." + codEsito);
						String msgErrore = Utils.getInstance(this.getLanguage()).getMessageWithParamsFromResourceBundle(this.nomeServizio + ".cancellazione.esito.messaggioUtente.template",
								e.getCodVersamentoEnte(),msgEsito);
						lstErrori.add(msgErrore);
					}
				}
			}catch (ServiceException e) {
				throw e;
			}finally {
				versamentiBD.commit();
				versamentiBD.disableSelectForUpdate();
			}

			this.log.info("Esecuzione " + methodName + " completata, rilevati ["+lstErrori.size()+"] errori. ");

			if(lstErrori.size() > 0) {
				// sono andati tutti male
				if(lstErrori.size() == idsToDelete.size()){
					// selezionato uno o tutti 
					String msg = idsToDelete.size() == 1 ? 
							Utils.getInstance(this.getLanguage()).getMessageFromResourceBundle(this.nomeServizio + ".cancellazione.esito.messaggioUtente.soloErrore")
							: Utils.getInstance(this.getLanguage()).getMessageFromResourceBundle(this.nomeServizio + ".cancellazione.esito.messaggioUtente.tuttiErrore") ;

							lstErrori.add(0,msg);
							throw new DeleteException(lstErrori, EsitoOperazione.ERRORE);
				}

				lstErrori.add(0,Utils.getInstance(this.getLanguage()).getMessageFromResourceBundle(this.nomeServizio + ".cancellazione.esito.messaggioUtente.alcuniErrore"));
				throw new DeleteException(lstErrori, EsitoOperazione.NONESEGUITA);
			}

			return this.getElenco(uriInfo, bd);

		}catch(DeleteException e){
			throw e;
		}catch(WebApplicationException e){
			throw e;
		}catch(Exception e){
			throw new ConsoleException(e);
		}
	}

	@Override
	public Versamento creaEntry(InputStream is, UriInfo uriInfo, BasicBD bd) throws WebApplicationException, ConsoleException { return null; }

	@Override
	public Dettaglio insert(InputStream is, UriInfo uriInfo, BasicBD bd) throws WebApplicationException, ConsoleException, ValidationException, DuplicatedEntryException { return null; }

	@Override
	public void checkEntry(Versamento entry, Versamento oldEntry) throws ValidationException { }

	@Override
	public Dettaglio update(InputStream is, UriInfo uriInfo, BasicBD bd) throws WebApplicationException, ConsoleException, ValidationException { return null; }

	@Override
	public Object uplaod(MultipartFormDataInput input, UriInfo uriInfo, BasicBD bd)	throws WebApplicationException, ConsoleException, ValidationException { return null;}
}
