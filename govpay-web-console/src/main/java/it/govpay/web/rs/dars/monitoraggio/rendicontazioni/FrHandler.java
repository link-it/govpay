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
package it.govpay.web.rs.dars.monitoraggio.rendicontazioni;

import java.io.InputStream;
import java.net.URI;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
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
import org.slf4j.Logger;
import org.jboss.resteasy.plugins.providers.multipart.MultipartFormDataInput;
import org.openspcoop2.generic_project.exception.NotFoundException;
import org.openspcoop2.generic_project.exception.ServiceException;
import org.openspcoop2.generic_project.expression.SortOrder;

import it.govpay.bd.BasicBD;
import it.govpay.bd.FilterSortWrapper;
import it.govpay.bd.anagrafica.AnagraficaManager;
import it.govpay.bd.anagrafica.DominiBD;
import it.govpay.bd.anagrafica.filters.DominioFilter;
import it.govpay.bd.model.Dominio;
import it.govpay.bd.model.Fr;
import it.govpay.bd.model.Psp;
import it.govpay.bd.pagamento.FrBD;
import it.govpay.bd.pagamento.filters.FrFilter;
import it.govpay.model.Fr.Anomalia;
import it.govpay.model.Fr.StatoFr;
import it.govpay.web.rs.dars.anagrafica.domini.Domini;
import it.govpay.web.rs.dars.anagrafica.domini.DominiHandler;
import it.govpay.web.rs.dars.anagrafica.psp.PspHandler;
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
import it.govpay.web.rs.dars.model.input.base.CheckButton;
import it.govpay.web.rs.dars.model.input.base.InputText;
import it.govpay.web.rs.dars.model.input.base.SelectList;
import it.govpay.web.utils.ConsoleProperties;
import it.govpay.web.utils.Utils;

public class FrHandler extends DarsHandler<Fr> implements IDarsHandler<Fr>{

	private SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm");  
	//	private SimpleDateFormat simpleDateFormatAnno = new SimpleDateFormat("yyyy");

	public FrHandler(Logger log, DarsService darsService) { 
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
			boolean simpleSearch = this.containsParameter(uriInfo, DarsService.SIMPLE_SEARCH_PARAMETER_ID);

			FrBD frBD = new FrBD(bd);
			FrFilter filter = frBD.newFilter(simpleSearch);
			filter.setOffset((offset != null) ? offset: 0);
			filter.setLimit(limit);
			FilterSortWrapper fsw = new FilterSortWrapper();
			fsw.setField(it.govpay.orm.FR.model().DATA_ORA_FLUSSO);
			fsw.setSortOrder(SortOrder.DESC);
			filter.getFilterSortList().add(fsw);


			boolean eseguiRicerca  = popolaFiltroRicerca(uriInfo, bd, simpleSearch, filter);


			long count = eseguiRicerca ? frBD.countExt(filter) : 0;

			// visualizza la ricerca solo se i risultati sono > del limit
			boolean visualizzaRicerca = this.visualizzaRicerca(count, limit);
			InfoForm infoRicerca = this.getInfoRicerca(uriInfo, bd, visualizzaRicerca);

			String formatter = Utils.getInstance(this.getLanguage()).getMessageFromResourceBundle(this.nomeServizio+".elenco.formatter");
			String simpleSearchPlaceholder = Utils.getInstance(this.getLanguage()).getMessageFromResourceBundle(this.nomeServizio+".simpleSearch.placeholder");
			Elenco elenco = new Elenco(this.titoloServizio, infoRicerca,
					this.getInfoCreazione(uriInfo, bd),
					count, this.getInfoEsportazione(uriInfo, bd), this.getInfoCancellazione(uriInfo, bd),simpleSearchPlaceholder); 

			List<Fr> findAll = eseguiRicerca ? frBD.findAllExt(filter) : new ArrayList<Fr>(); 

			if(findAll != null && findAll.size() > 0){
				for (Fr entry : findAll) {
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

	private boolean popolaFiltroRicerca(UriInfo uriInfo, BasicBD bd,  
			boolean simpleSearch, FrFilter filter) throws ServiceException, NotFoundException, ConsoleException {
		List<String> listaCodDomini =  new ArrayList<String>();
		Set<Long> setDomini = this.darsService.getIdDominiAbilitatiLetturaServizio(bd, this.funzionalita);
		boolean eseguiRicerca = !setDomini.isEmpty();
		List<Long> idDomini = new ArrayList<Long>();

		if(simpleSearch){
			// simplesearch
			String simpleSearchString = this.getParameter(uriInfo, DarsService.SIMPLE_SEARCH_PARAMETER_ID, String.class);
			if(StringUtils.isNotEmpty(simpleSearchString)) {
				filter.setSimpleSearchString(simpleSearchString);
			}
		}else{
			String codFlussoId = Utils.getInstance(this.getLanguage()).getMessageFromResourceBundle(this.nomeServizio + ".codFlusso.id");
			String codFlusso = this.getParameter(uriInfo, codFlussoId, String.class);

			if(StringUtils.isNotEmpty(codFlusso))
				filter.setCodFlusso(codFlusso); 


			String idDominioId = Utils.getInstance(this.getLanguage()).getMessageFromResourceBundle(this.nomeServizio + ".idDominio.id");
			Long idDominio = this.getParameter(uriInfo, idDominioId, Long.class);

			if(idDominio != null && idDominio > 0){
				Dominio dominio = AnagraficaManager.getDominio(bd, idDominio);
				listaCodDomini = Arrays.asList(dominio.getCodDominio());
				filter.setCodDominio(listaCodDomini); 
			}

			String statoId = Utils.getInstance().getMessageFromResourceBundle(this.nomeServizio + ".stato.id");
			String stato = this.getParameter(uriInfo, statoId, String.class);

			if(StringUtils.isNotEmpty(stato)){
				filter.setStato(stato);
			}

			String trnId = Utils.getInstance().getMessageFromResourceBundle(this.nomeServizio + ".trn.id");
			String trn = this.getParameter(uriInfo, trnId, String.class);

			if(StringUtils.isNotEmpty(trn))
				filter.setTnr(trn);
			
			String iuvId = Utils.getInstance().getMessageFromResourceBundle(this.nomeServizio + ".iuv.id");
			String iuv = this.getParameter(uriInfo, iuvId, String.class);

			if(StringUtils.isNotEmpty(iuv))
				filter.setIuv(iuv);


			String nascondiAltriIntermediariId = Utils.getInstance().getMessageFromResourceBundle(this.nomeServizio + ".nascondiAltriIntermediari.id");
			String nascondiAltriIntermediariS = this.getParameter(uriInfo, nascondiAltriIntermediariId, String.class);

			Boolean nascondiAltriIntermediari = false;
			if(StringUtils.isNotEmpty(nascondiAltriIntermediariS)){
				if(StringUtils.equalsIgnoreCase(nascondiAltriIntermediariS, "on") || StringUtils.equalsIgnoreCase(nascondiAltriIntermediariS, "yes"))
					nascondiAltriIntermediari = true;
			}

			filter.setNascondiSeSoloDiAltriIntermediari(nascondiAltriIntermediari);

		}

		if(eseguiRicerca &&!setDomini.contains(-1L)){
			List<Long> lstCodDomini = new ArrayList<Long>();
			lstCodDomini.addAll(setDomini);
			idDomini.addAll(setDomini);
			filter.setCodDominio(toListCodDomini(idDomini, bd));
		}

		return eseguiRicerca;
	}

	private boolean popolaFiltroRicerca(List<RawParamValue> rawValues, BasicBD bd,  
			boolean simpleSearch, FrFilter filter) throws ServiceException, NotFoundException, ConsoleException {
		List<String> listaCodDomini =  new ArrayList<String>();
		Set<Long> setDomini = this.darsService.getIdDominiAbilitatiLetturaServizio(bd, this.funzionalita);
		boolean eseguiRicerca = !setDomini.isEmpty();
		List<Long> idDomini = new ArrayList<Long>();

		if(simpleSearch){
			// simplesearch
			String simpleSearchString = Utils.getValue(rawValues, DarsService.SIMPLE_SEARCH_PARAMETER_ID);
			if(StringUtils.isNotEmpty(simpleSearchString)) {
				filter.setSimpleSearchString(simpleSearchString);
			}
		}else{
			String codFlussoId = Utils.getInstance(this.getLanguage()).getMessageFromResourceBundle(this.nomeServizio + ".codFlusso.id");
			String codFlusso = Utils.getValue(rawValues, codFlussoId);

			if(StringUtils.isNotEmpty(codFlusso))
				filter.setCodFlusso(codFlusso); 


			String idDominioId = Utils.getInstance(this.getLanguage()).getMessageFromResourceBundle(this.nomeServizio + ".idDominio.id");
			String idDominio = Utils.getValue(rawValues, idDominioId);

			if(StringUtils.isNotEmpty(idDominio)){
				long idDom = -1l;
				try{
					idDom = Long.parseLong(idDominio);
				}catch(Exception e){ idDom = -1l;	}
				if(idDom > 0){
					Dominio dominio = AnagraficaManager.getDominio(bd, idDom);
					listaCodDomini = Arrays.asList(dominio.getCodDominio());
					filter.setCodDominio(listaCodDomini);
				}

			}

			String statoId = Utils.getInstance().getMessageFromResourceBundle(this.nomeServizio + ".stato.id");
			String stato = Utils.getValue(rawValues, statoId);

			if(StringUtils.isNotEmpty(stato)){
				filter.setStato(stato);
			}

			String trnId = Utils.getInstance().getMessageFromResourceBundle(this.nomeServizio + ".trn.id");
			String trn = Utils.getValue(rawValues, trnId);

			if(StringUtils.isNotEmpty(trn))
				filter.setTnr(trn);
			
			String iuvId = Utils.getInstance().getMessageFromResourceBundle(this.nomeServizio + ".iuv.id");
			String iuv = Utils.getValue(rawValues, iuvId);

			if(StringUtils.isNotEmpty(iuv))
				filter.setIuv(iuv);


			String nascondiAltriIntermediariId = Utils.getInstance().getMessageFromResourceBundle(this.nomeServizio + ".nascondiAltriIntermediari.id");
			String nascondiAltriIntermediariS = Utils.getValue(rawValues, nascondiAltriIntermediariId);

			Boolean nascondiAltriIntermediari = false;
			if(StringUtils.isNotEmpty(nascondiAltriIntermediariS)){
				if(StringUtils.equalsIgnoreCase(nascondiAltriIntermediariS, "true") || StringUtils.equalsIgnoreCase(nascondiAltriIntermediariS, "on") || StringUtils.equalsIgnoreCase(nascondiAltriIntermediariS, "yes"))
					nascondiAltriIntermediari = true;
			}

			filter.setNascondiSeSoloDiAltriIntermediari(nascondiAltriIntermediari);

		}

		if(eseguiRicerca &&!setDomini.contains(-1L)){
			List<Long> lstCodDomini = new ArrayList<Long>();
			lstCodDomini.addAll(setDomini);
			idDomini.addAll(setDomini);
			filter.setCodDominio(toListCodDomini(idDomini, bd));
		}

		return eseguiRicerca;
	}

	@SuppressWarnings("unchecked")
	@Override
	public InfoForm getInfoRicerca(UriInfo uriInfo, BasicBD bd, boolean visualizzaRicerca, Map<String,String> parameters) throws ConsoleException {
		URI ricerca = this.getUriRicerca(uriInfo, bd);
		InfoForm infoRicerca = new InfoForm(ricerca);

		if(visualizzaRicerca) {
			String idDominioId = Utils.getInstance(this.getLanguage()).getMessageFromResourceBundle(this.nomeServizio + ".idDominio.id");
			String codFlussoId = Utils.getInstance(this.getLanguage()).getMessageFromResourceBundle(this.nomeServizio + ".codFlusso.id");
			String statoId = Utils.getInstance(this.getLanguage()).getMessageFromResourceBundle(this.nomeServizio + ".stato.id");
			String nascondiAltriIntermediariId = Utils.getInstance(this.getLanguage()).getMessageFromResourceBundle(this.nomeServizio + ".nascondiAltriIntermediari.id");
			String trnId = Utils.getInstance(this.getLanguage()).getMessageFromResourceBundle(this.nomeServizio + ".trn.id");
			String iuvId = Utils.getInstance(this.getLanguage()).getMessageFromResourceBundle(this.nomeServizio + ".iuv.id");
			
			if(this.infoRicercaMap == null){
				this.initInfoRicerca(uriInfo, bd);
			}
			Sezione sezioneRoot = infoRicerca.getSezioneRoot();

			// stato
			List<Voce<String>> stati = new ArrayList<Voce<String>>();
			SelectList<String> stato = (SelectList<String>) infoRicercaMap.get(statoId);
			stati.add(new Voce<String>(Utils.getInstance(this.getLanguage()).getMessageFromResourceBundle("commons.label.qualsiasi"), ""));
			stati.add(new Voce<String>(Utils.getInstance(this.getLanguage()).getMessageFromResourceBundle(this.nomeServizio + ".stato."+StatoFr.ACCETTATA), StatoFr.ACCETTATA.toString()));
			stati.add(new Voce<String>(Utils.getInstance(this.getLanguage()).getMessageFromResourceBundle(this.nomeServizio + ".stato."+StatoFr.ANOMALA), StatoFr.ANOMALA.toString()));
			stato.setDefaultValue("");
			stato.setValues(stati); 
			sezioneRoot.addField(stato);

			// idDominio
			List<Voce<Long>> domini = new ArrayList<Voce<Long>>();
			try{
				Set<Long> setDomini = this.darsService.getIdDominiAbilitatiLetturaServizio(bd, this.funzionalita);
				boolean eseguiRicerca = !setDomini.isEmpty();
				List<Long> idDomini = new ArrayList<Long>();
	
				DominiBD dominiBD = new DominiBD(bd);
				DominioFilter filter;
				try {
					filter = dominiBD.newFilter();
					
					if(eseguiRicerca) {
						if(!setDomini.contains(-1L)) {
							idDomini.addAll(setDomini);	
							filter.setIdDomini(idDomini);
						}
						
						FilterSortWrapper fsw = new FilterSortWrapper();
						fsw.setField(it.govpay.orm.Dominio.model().COD_DOMINIO);
						fsw.setSortOrder(SortOrder.ASC);
						filter.getFilterSortList().add(fsw);
						List<Dominio> findAll = dominiBD.findAll(filter );
		
						domini.add(new Voce<Long>(Utils.getInstance(this.getLanguage()).getMessageFromResourceBundle("commons.label.qualsiasi"), -1L));
		
						Domini dominiDars = new Domini();
						DominiHandler dominiHandler = (DominiHandler) dominiDars.getDarsHandler();
		
						if(findAll != null && findAll.size() > 0){
							for (Dominio dominio : findAll) {
								domini.add(new Voce<Long>(dominiHandler.getTitolo(dominio,bd), dominio.getId()));  
							}
						}
					}
				} catch (ServiceException e) {
					throw new ConsoleException(e);
				}
				SelectList<Long> idDominio = (SelectList<Long>) infoRicercaMap.get(idDominioId);
				idDominio.setDefaultValue(-1L);
				idDominio.setValues(domini); 
				sezioneRoot.addField(idDominio);
			}catch(Exception e){
				throw new ConsoleException(e);
			}

			// codFlusso
			InputText codFlusso = (InputText) infoRicercaMap.get(codFlussoId);
			codFlusso.setDefaultValue(null);
			sezioneRoot.addField(codFlusso);

			// trn
			InputText trn = (InputText) infoRicercaMap.get(trnId);
			trn.setDefaultValue(null);
			sezioneRoot.addField(trn);
			
			// iuv
			InputText iuv = (InputText) infoRicercaMap.get(iuvId);
			iuv.setDefaultValue(null);
			sezioneRoot.addField(iuv);

			// nascondi altri intermediari
			CheckButton nascondiAltriIntermediari = (CheckButton) infoRicercaMap.get(nascondiAltriIntermediariId);
			nascondiAltriIntermediari.setDefaultValue(false);
			sezioneRoot.addField(nascondiAltriIntermediari);

		}
		return infoRicerca;
	}

	private void initInfoRicerca(UriInfo uriInfo, BasicBD bd) throws ConsoleException{
		if(this.infoRicercaMap == null){
			this.infoRicercaMap = new HashMap<String, ParamField<?>>();

			String idDominioId = Utils.getInstance(this.getLanguage()).getMessageFromResourceBundle(this.nomeServizio + ".idDominio.id");
			String codFlussoId = Utils.getInstance(this.getLanguage()).getMessageFromResourceBundle(this.nomeServizio + ".codFlusso.id");
			String statoId = Utils.getInstance(this.getLanguage()).getMessageFromResourceBundle(this.nomeServizio + ".stato.id");
			String nascondiAltriIntermediariId = Utils.getInstance(this.getLanguage()).getMessageFromResourceBundle(this.nomeServizio + ".nascondiAltriIntermediari.id");
			String trnId = Utils.getInstance(this.getLanguage()).getMessageFromResourceBundle(this.nomeServizio + ".trn.id");
			String iuvId = Utils.getInstance(this.getLanguage()).getMessageFromResourceBundle(this.nomeServizio + ".iuv.id");

			// codFlusso
			String codFlussoLabel = Utils.getInstance(this.getLanguage()).getMessageFromResourceBundle(this.nomeServizio + ".codFlusso.label");
			InputText codFlusso = new InputText(codFlussoId, codFlussoLabel, null, false, false, true, 0, 35);
			this.infoRicercaMap.put(codFlussoId, codFlusso);

			// trn
			String trnLabel = Utils.getInstance().getMessageFromResourceBundle(this.nomeServizio + ".trn.label");
			InputText trn = new InputText(trnId, trnLabel, null, false, false, true, 0, 35);
			infoRicercaMap.put(trnId, trn);
			
			// iuv
			String iuvLabel = Utils.getInstance().getMessageFromResourceBundle(this.nomeServizio + ".iuv.label");
			InputText iuv = new InputText(iuvId, iuvLabel, null, false, false, true, 0, 35);
			infoRicercaMap.put(iuvId, iuv);

			List<Voce<Long>> domini = new ArrayList<Voce<Long>>();
			// idDominio
			String idDominioLabel = Utils.getInstance(this.getLanguage()).getMessageFromResourceBundle(this.nomeServizio + ".idDominio.label");
			SelectList<Long> idDominio = new SelectList<Long>(idDominioId, idDominioLabel, null, false, false, true, domini);
			infoRicercaMap.put(idDominioId, idDominio);


			List<Voce<String>> stati = new ArrayList<Voce<String>>();
			// stato
			String statoLabel = Utils.getInstance(this.getLanguage()).getMessageFromResourceBundle(this.nomeServizio + ".stato.label");
			SelectList<String> stato = new SelectList<String>(statoId, statoLabel, null, false, false, true, stati);
			infoRicercaMap.put(statoId, stato);

			// nascondiAltriIntermediari
			String nascondiAltriIntermediariLabel = Utils.getInstance(this.getLanguage()).getMessageFromResourceBundle(this.nomeServizio + ".nascondiAltriIntermediari.label");
			CheckButton nascondiAltriIntermediari = new CheckButton(nascondiAltriIntermediariId, nascondiAltriIntermediariLabel, false, false, false, true);
			infoRicercaMap.put(nascondiAltriIntermediariId, nascondiAltriIntermediari);
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
			FrBD frBD = new FrBD(bd);
			// Operazione consentita solo ai ruoli con diritto di lettura
			this.darsService.checkDirittiServizioLettura(bd, this.funzionalita);

			Set<Long> setDomini = this.darsService.getIdDominiAbilitatiLetturaServizio(bd, this.funzionalita);
			boolean eseguiRicerca = !setDomini.isEmpty();

			if(eseguiRicerca && !setDomini.contains(-1L)){
				List<Long> idDomini = new ArrayList<Long>();
				FrFilter filter = frBD.newFilter();
				List<Long> lstCodDomini = new ArrayList<Long>();
				lstCodDomini.addAll(setDomini);
				idDomini.addAll(setDomini);
				filter.setCodDominio(toListCodDomini(idDomini, bd));
				List<Long> idFrL = new ArrayList<Long>();
				idFrL.add(id);
				filter.setIdFr(idFrL);

				long count = eseguiRicerca ? frBD.count(filter) : 0;
				eseguiRicerca = eseguiRicerca && count > 0;
			}

			// recupero oggetto
			Fr fr = eseguiRicerca ? frBD.getFrExt(id) : null;

			InfoForm infoModifica = null;
			InfoForm infoCancellazione = fr != null ? this.getInfoCancellazioneDettaglio(uriInfo, bd, fr) : null;
			InfoForm infoEsportazione = fr != null ? this.getInfoEsportazioneDettaglio(uriInfo, bd, fr) : null;
			
			String titolo = Utils.getInstance(this.getLanguage()).getMessageFromResourceBundle(this.nomeServizio + ".label.dettaglio");
			Dettaglio dettaglio = new Dettaglio(titolo, infoEsportazione, infoCancellazione, infoModifica);

			it.govpay.web.rs.dars.model.Sezione root = dettaglio.getSezioneRoot();

			if(fr != null){

				// campi da visualizzare flusso, dominio, psp, trn, bic, data flusso, data regolamento, importo totale, sezione anomalie

				if(StringUtils.isNotEmpty(fr.getCodFlusso())) {
					root.addVoce(Utils.getInstance(this.getLanguage()).getMessageFromResourceBundle(this.nomeServizio + ".codFlusso.label"), fr.getCodFlusso());
				}

				// dominio
				Dominio dominio = null;
				try{
					dominio = fr.getDominio(bd);
				}catch (Exception e) {
					// dominio non censito 
				}
				if(dominio != null) {
					Domini dominiDars = new Domini();
					DominiHandler dominiDarsHandler =  (DominiHandler) dominiDars.getDarsHandler();
					Elemento elemento = dominiDarsHandler.getElemento(dominio, dominio.getId(), dominiDars.getPathServizio(), bd);
					root.addVoce(Utils.getInstance(this.getLanguage()).getMessageFromResourceBundle(this.nomeServizio + ".dominio.label"), elemento.getTitolo(),elemento.getUri());
				} else {
					root.addVoce(Utils.getInstance(this.getLanguage()).getMessageFromResourceBundle(this.nomeServizio + ".dominio.label"), fr.getCodDominio());
				}

				Psp psp = null;
				try{
					psp =fr.getPsp(bd);
				}catch (Exception e) {
					// psp non censito 
				}

				if(psp != null) {
					root.addVoce(Utils.getInstance(this.getLanguage()).getMessageFromResourceBundle(this.nomeServizio + ".psp.label"),psp.getCodPsp());
				} else {
					root.addVoce(Utils.getInstance(this.getLanguage()).getMessageFromResourceBundle(this.nomeServizio + ".psp.label"),fr.getCodPsp());
				}

				if(StringUtils.isNotEmpty(fr.getIur())) {
					root.addVoce(Utils.getInstance(this.getLanguage()).getMessageFromResourceBundle(this.nomeServizio + ".trn.label"), fr.getIur());
				}

				if(StringUtils.isNotEmpty(fr.getCodBicRiversamento())) {
					root.addVoce(Utils.getInstance(this.getLanguage()).getMessageFromResourceBundle(this.nomeServizio + ".codBicRiversamento.label"), fr.getCodBicRiversamento());
				}

				if(fr.getDataFlusso() != null) {
					root.addVoce(Utils.getInstance(this.getLanguage()).getMessageFromResourceBundle(this.nomeServizio + ".dataFlusso.label"), this.sdf.format(fr.getDataFlusso()));
				}
				if(fr.getDataRegolamento() != null) {
					root.addVoce(Utils.getInstance(this.getLanguage()).getMessageFromResourceBundle(this.nomeServizio + ".dataRegolamento.label"), this.sdf.format(fr.getDataRegolamento()));
				}

				root.addVoce(Utils.getInstance(this.getLanguage()).getMessageFromResourceBundle(this.nomeServizio + ".importoTotalePagamenti.label"),
						this.currencyUtils.getCurrencyAsEuro(fr.getImportoTotalePagamenti()));

				StatoFr stato = fr.getStato();
				if(stato!= null) {
					root.addVoce(Utils.getInstance(this.getLanguage()).getMessageFromResourceBundle(this.nomeServizio + ".stato.label"),
							Utils.getInstance(this.getLanguage()).getMessageFromResourceBundle(this.nomeServizio + ".stato."+stato.name()));
				}

				if(fr.getAnomalie()!= null && fr.getAnomalie().size() > 0) {
					String etichettaSezioneAnomalie = Utils.getInstance(this.getLanguage()).getMessageFromResourceBundle(this.nomeServizio + ".sezioneAnomalie.label");
					it.govpay.web.rs.dars.model.Sezione sezioneAnomalie = dettaglio.addSezione(etichettaSezioneAnomalie );

					for (Anomalia anomalia : fr.getAnomalie()) {
						sezioneAnomalie.addVoce(anomalia.getCodice(),anomalia.getDescrizione());
					}
				}

				Rendicontazioni rendicontazioniDars = new Rendicontazioni();
				String etichettaPagamenti = Utils.getInstance(this.getLanguage()).getMessageFromResourceBundle(this.nomeServizio + ".elementoCorrelato.rendicontazioni.titolo");
				String idFrApplicazioneId = Utils.getInstance(this.getLanguage()).getMessageFromResourceBundle(rendicontazioniDars.getNomeServizio() + ".idFr.id");

				Map<String, String> params = new HashMap<String, String>();
				params.put(idFrApplicazioneId, fr.getId()+ "");
				URI rendicontazioneDettaglio = Utils.creaUriConParametri(rendicontazioniDars.getPathServizio(), params );
				dettaglio.addElementoCorrelato(etichettaPagamenti, rendicontazioneDettaglio); 
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
	public String getTitolo(Fr entry,BasicBD bd) {
		StringBuilder sb = new StringBuilder();

		String codFlusso = entry.getCodFlusso();
		Date dataFlusso = entry.getDataFlusso();

		sb.append(
				Utils.getInstance(this.getLanguage()).getMessageWithParamsFromResourceBundle(this.nomeServizio + ".label.titolo",
						codFlusso,this.sdf.format(dataFlusso)));

		return sb.toString();
	}

	@Override
	public String getSottotitolo(Fr entry,BasicBD bd) {
		StringBuilder sb = new StringBuilder();
		StatoFr stato = entry.getStato();
		long numeroPagamenti = entry.getNumeroPagamenti();

		switch (stato) {
		case ACCETTATA:
			sb.append(
					Utils.getInstance(this.getLanguage()).getMessageWithParamsFromResourceBundle(this.nomeServizio + ".label.sottotitolo.accettata",
							Utils.getInstance(this.getLanguage()).getMessageFromResourceBundle(this.nomeServizio + ".stato.ACCETTATA"),
							numeroPagamenti,this.currencyUtils.getCurrencyAsEuro(entry.getImportoTotalePagamenti())));
			break;
		case ANOMALA:
		default:
			sb.append(
					Utils.getInstance(this.getLanguage()).getMessageWithParamsFromResourceBundle(this.nomeServizio + ".label.sottotitolo.anomala",
							Utils.getInstance(this.getLanguage()).getMessageFromResourceBundle(this.nomeServizio + ".stato.ANOMALA")
							)
					);
			break;
		}

		return sb.toString();
	} 

	@Override
	public Map<String, Voce<String>> getVoci(Fr entry, BasicBD bd) throws ConsoleException { 
		Map<String, Voce<String>> voci = new HashMap<String, Voce<String>>();


		voci.put(Utils.getInstance(this.getLanguage()).getMessageFromResourceBundle(this.nomeServizio + ".stato.id"),
				new Voce<String>(Utils.getInstance(this.getLanguage()).getMessageFromResourceBundle(this.nomeServizio + ".stato."+entry.getStato().name()),
						entry.getStato().name()));
		
		voci.put(Utils.getInstance(this.getLanguage()).getMessageFromResourceBundle(this.nomeServizio + ".statoText.id"),
				new Voce<String>(Utils.getInstance(this.getLanguage()).getMessageFromResourceBundle(this.nomeServizio + ".statoText.label"),
						Utils.getInstance(this.getLanguage()).getMessageFromResourceBundle(this.nomeServizio + ".stato."+entry.getStato().name())));

		if(StringUtils.isNotEmpty(entry.getCodFlusso())){
			voci.put(Utils.getInstance(this.getLanguage()).getMessageFromResourceBundle(this.nomeServizio + ".codFlusso.id"),
					new Voce<String>(Utils.getInstance(this.getLanguage()).getMessageFromResourceBundle(this.nomeServizio + ".codFlusso.label"),
							entry.getCodFlusso()));
		}

		if(StringUtils.isNotEmpty(entry.getIur())){
			voci.put(Utils.getInstance(this.getLanguage()).getMessageFromResourceBundle(this.nomeServizio + ".trn.id"),
					new Voce<String>(Utils.getInstance(this.getLanguage()).getMessageFromResourceBundle(this.nomeServizio + ".trn.label"),
							entry.getIur()));
		}

		voci.put(Utils.getInstance(this.getLanguage()).getMessageFromResourceBundle(this.nomeServizio + ".numeroPagamenti.id"),
				new Voce<String>(Utils.getInstance(this.getLanguage()).getMessageFromResourceBundle(this.nomeServizio + ".numeroPagamenti.label"),
						entry.getNumeroPagamenti()+""));
		voci.put(Utils.getInstance(this.getLanguage()).getMessageFromResourceBundle(this.nomeServizio + ".numRendicontazioniOk.id"),
				new Voce<String>(Utils.getInstance(this.getLanguage()).getMessageFromResourceBundle(this.nomeServizio + ".numRendicontazioniOk.label"),
						entry.getNumOk()+""));
		voci.put(Utils.getInstance(this.getLanguage()).getMessageFromResourceBundle(this.nomeServizio + ".numRendicontazioniAnomale.id"),
				new Voce<String>(Utils.getInstance(this.getLanguage()).getMessageFromResourceBundle(this.nomeServizio + ".numRendicontazioniAnomale.label"),
						entry.getNumAnomale()+""));
		voci.put(Utils.getInstance(this.getLanguage()).getMessageFromResourceBundle(this.nomeServizio + ".numRendicontazioniAltroIntermediario.id"),
				new Voce<String>(Utils.getInstance(this.getLanguage()).getMessageFromResourceBundle(this.nomeServizio + ".numRendicontazioniAltroIntermediario.label"),
						entry.getNumAltroIntermediario()+""));


		try{		
			// voci disponibili logo1, logo2, id flusso, idDominio, psp, trn, numero pagamenti, numero pagamenti anomali , numero pagamenti altri intermediari
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
			} catch (NotFoundException e) {
				// dominio non censito metto solo il coddominio
				voci.put(Utils.getInstance(this.getLanguage()).getMessageFromResourceBundle(this.nomeServizio + ".idDominio.id"),
						new Voce<String>(Utils.getInstance(this.getLanguage()).getMessageFromResourceBundle(this.nomeServizio + ".idDominio.label"),
								entry.getCodDominio()));
			}

			try{
				Psp psp = entry.getPsp(bd);
				if(psp != null) {
					it.govpay.web.rs.dars.anagrafica.psp.Psp _psp = new it.govpay.web.rs.dars.anagrafica.psp.Psp();
					PspHandler pspHandler = (PspHandler) _psp.getDarsHandler();
					voci.put(Utils.getInstance(this.getLanguage()).getMessageFromResourceBundle(this.nomeServizio + ".psp.id"),
							new Voce<String>(Utils.getInstance(this.getLanguage()).getMessageFromResourceBundle(this.nomeServizio + ".psp.label"),
									pspHandler.getTitolo(psp, bd)));
				}
			} catch (NotFoundException e) {
				// psp non censito metto solo il codice
				voci.put(Utils.getInstance(this.getLanguage()).getMessageFromResourceBundle(this.nomeServizio + ".psp.id"),
						new Voce<String>(Utils.getInstance(this.getLanguage()).getMessageFromResourceBundle(this.nomeServizio + ".psp.label"),
								entry.getCodPsp()));
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

		String methodName = "esporta " + this.titoloServizio + "[" + sb.toString() + "]";

		String fileName = "Rendicontazioni.zip";
		try{
			this.log.info("Esecuzione " + methodName + " in corso...");
			int limit = ConsoleProperties.getInstance().getNumeroMassimoElementiExport();
			FrBD frBD = new FrBD(bd);
			boolean simpleSearch = Utils.containsParameter(rawValues, DarsService.SIMPLE_SEARCH_PARAMETER_ID);
			FrFilter filter = frBD.newFilter(simpleSearch);

			// se ho ricevuto anche gli id li utilizzo per fare il check della count
			if(idsToExport != null && idsToExport.size() > 0) 
				filter.setIdFr(idsToExport);

			//1. eseguo una count per verificare che il numero dei risultati da esportare sia <= sogliamassimaexport massivo
			boolean eseguiRicerca = this.popolaFiltroRicerca(rawValues, frBD,  simpleSearch, filter); 

			if(!eseguiRicerca){
				List<String> msg = new ArrayList<String>();
				msg.add(Utils.getInstance(this.getLanguage()).getMessageFromResourceBundle(this.nomeServizio+".esporta.operazioneNonPermessa"));
				throw new ExportException(msg, EsitoOperazione.ERRORE);
			}

			long count = frBD.countExt(filter);

			if(count < 1){
				List<String> msg = new ArrayList<String>();
				msg.add(Utils.getInstance(this.getLanguage()).getMessageFromResourceBundle(this.nomeServizio+".esporta.nessunElementoDaEsportare"));
				throw new ExportException(msg, EsitoOperazione.ERRORE);
			} 

//			if(count > ConsoleProperties.getInstance().getNumeroMassimoElementiExport()){
//				List<String> msg = new ArrayList<String>();
//				msg.add(Utils.getInstance(this.getLanguage()).getMessageFromResourceBundle(this.nomeServizio+".esporta.numeroElementiDaEsportareSopraSogliaMassima"));
//				throw new ExportException(msg, EsitoOperazione.ERRORE);
//			}

			filter.setOffset(0);
			filter.setLimit(limit);
			FilterSortWrapper fsw = new FilterSortWrapper();
			fsw.setField(it.govpay.orm.FR.model().DATA_ORA_FLUSSO);
			fsw.setSortOrder(SortOrder.DESC);
			filter.getFilterSortList().add(fsw);

			List<Fr> findAllExt = frBD.findAllExt(filter);

			for (Fr fr : findAllExt) {
				String folderName = "Rendicontazione_" + fr.getIur();

				ZipEntry frXml = new ZipEntry(folderName +"/fr.xml");
				zout.putNextEntry(frXml);
				zout.write(fr.getXml());
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


		try{
			this.log.info("Esecuzione " + methodName + " in corso...");
			this.darsService.getOperatoreByPrincipal(bd); 


			Set<Long> setDomini = this.darsService.getIdDominiAbilitatiLetturaServizio(bd, this.funzionalita);
			boolean eseguiRicerca = !setDomini.isEmpty();
			FrBD frBD = new FrBD(bd);
			FrFilter filter = frBD.newFilter();
			List<String> idDomini = new ArrayList<String>();
			List<Long> idsToExport = new ArrayList<Long>();
			idsToExport.add(idToExport);

			if(!setDomini.contains(-1L)){
				List<Long> lstCodDomini = new ArrayList<Long>();
				lstCodDomini.addAll(setDomini);
				idDomini.addAll(this.toListCodDomini(lstCodDomini, bd));
				filter.setCodDominio(idDomini);

				// l'operatore puo' vedere i domini associati, controllo se c'e' un versamento con Id nei domini concessi.
				if(eseguiRicerca){
					filter.setIdFr(idsToExport);
					eseguiRicerca = eseguiRicerca && frBD.count(filter) > 0;
				}
			}
			String fileName = "Rendicontazione.zip";

			if(eseguiRicerca ){
				Fr fr = frBD.getFr(idToExport);	
				fileName = "Rendicontazione_" + fr.getIur()+".zip";
				ZipEntry frXml = new ZipEntry("fr.xml");
				zout.putNextEntry(frXml);
				zout.write(fr.getXml());
				zout.closeEntry();
			} else {
				String noEntriesTxt = "/README";
				ZipEntry entryTxt = new ZipEntry(noEntriesTxt);
				zout.putNextEntry(entryTxt);
				zout.write("Non sono state trovate informazioni sui flussi selezionati.".getBytes());
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

	/* Creazione/Update non consentiti**/

	@Override
	public InfoForm getInfoCancellazione(UriInfo uriInfo, BasicBD bd, Map<String, String> parameters) throws ConsoleException {
		return null;
	}

	@Override
	public InfoForm getInfoCancellazioneDettaglio(UriInfo uriInfo, BasicBD bd, Fr entry) throws ConsoleException {
		return null;
	}

	@Override
	public InfoForm getInfoEsportazione(UriInfo uriInfo, BasicBD bd, Map<String, String> parameters) throws ConsoleException { 
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
	public InfoForm getInfoEsportazioneDettaglio(UriInfo uriInfo, BasicBD bd, Fr entry)	throws ConsoleException {
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

	@Override
	public InfoForm getInfoCreazione(UriInfo uriInfo, BasicBD bd) throws ConsoleException { return null; }

	@Override
	public InfoForm getInfoModifica(UriInfo uriInfo, BasicBD bd, Fr entry) throws ConsoleException { return null; }

	@Override
	public Elenco delete(List<Long> idsToDelete, List<RawParamValue> rawValues, UriInfo uriInfo, BasicBD bd) throws WebApplicationException, ConsoleException, DeleteException {	return null; 	}

	@Override
	public Fr creaEntry(InputStream is, UriInfo uriInfo, BasicBD bd) throws WebApplicationException, ConsoleException { return null; }

	@Override
	public Dettaglio insert(InputStream is, UriInfo uriInfo, BasicBD bd) throws WebApplicationException, ConsoleException, ValidationException, DuplicatedEntryException { return null; }

	@Override
	public void checkEntry(Fr entry, Fr oldEntry) throws ValidationException { }

	@Override
	public Dettaglio update(InputStream is, UriInfo uriInfo, BasicBD bd) throws WebApplicationException, ConsoleException, ValidationException { return null; }

	@Override
	public Object uplaod(MultipartFormDataInput input, UriInfo uriInfo, BasicBD bd)	throws WebApplicationException, ConsoleException, ValidationException { return null;}
}
