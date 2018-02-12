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
package it.govpay.web.rs.dars.anagrafica.uo;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.net.URI;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.zip.ZipOutputStream;

import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.UriInfo;

import org.apache.commons.lang.StringUtils;
import org.apache.logging.log4j.Logger;
import org.jboss.resteasy.plugins.providers.multipart.MultipartFormDataInput;
import org.openspcoop2.generic_project.exception.NotFoundException;
import org.openspcoop2.generic_project.exception.ServiceException;
import org.openspcoop2.generic_project.expression.SortOrder;

import it.govpay.bd.BasicBD;
import it.govpay.bd.FilterSortWrapper;
import it.govpay.bd.anagrafica.DominiBD;
import it.govpay.bd.anagrafica.UnitaOperativeBD;
import it.govpay.bd.anagrafica.filters.UnitaOperativaFilter;
import it.govpay.bd.model.Dominio;
import it.govpay.bd.model.UnitaOperativa;
import it.govpay.model.Anagrafica;
import it.govpay.web.rs.dars.anagrafica.anagrafica.AnagraficaHandler;
import it.govpay.web.rs.dars.base.DarsHandler;
import it.govpay.web.rs.dars.base.DarsService;
import it.govpay.web.rs.dars.exception.ConsoleException;
import it.govpay.web.rs.dars.exception.DuplicatedEntryException;
import it.govpay.web.rs.dars.exception.ExportException;
import it.govpay.web.rs.dars.exception.ValidationException;
import it.govpay.web.rs.dars.handler.IDarsHandler;
import it.govpay.web.rs.dars.model.Dettaglio;
import it.govpay.web.rs.dars.model.Elenco;
import it.govpay.web.rs.dars.model.InfoForm;
import it.govpay.web.rs.dars.model.InfoForm.Sezione;
import it.govpay.web.rs.dars.model.RawParamValue;
import it.govpay.web.rs.dars.model.Voce;
import it.govpay.web.rs.dars.model.input.ParamField;
import it.govpay.web.rs.dars.model.input.RefreshableParamField;
import it.govpay.web.rs.dars.model.input.base.CheckButton;
import it.govpay.web.rs.dars.model.input.base.InputNumber;
import it.govpay.web.rs.dars.model.input.base.InputText;
import it.govpay.web.utils.Utils;
import net.sf.json.JSONObject;
import net.sf.json.JsonConfig;

public class UnitaOperativeHandler extends DarsHandler<UnitaOperativa> implements IDarsHandler<UnitaOperativa>{

	public static final String ANAGRAFICA_UO = "anagrafica";
	private Long idDominio = null;

	public UnitaOperativeHandler(Logger log, DarsService darsService) {
		super(log,darsService);
	}

	@Override
	public Elenco getElenco(UriInfo uriInfo,BasicBD bd) throws WebApplicationException,ConsoleException {
		String methodName = "getElenco " + this.titoloServizio;
		try{	
			// Operazione consentita solo agli utenti che hanno almeno un ruolo consentito per la funzionalita'
			this.darsService.checkDirittiServizio(bd, this.funzionalita);

			Integer offset = this.getOffset(uriInfo);
			Integer limit = this.getLimit(uriInfo);

			this.log.info("Esecuzione " + methodName + " in corso..."); 

			boolean simpleSearch = this.containsParameter(uriInfo, DarsService.SIMPLE_SEARCH_PARAMETER_ID);

			UnitaOperativeBD unitaOperativaBD = new UnitaOperativeBD(bd);
			UnitaOperativaFilter filter = unitaOperativaBD.newFilter(simpleSearch);
			filter.setOffset(offset);
			filter.setLimit(limit);
			FilterSortWrapper fsw = new FilterSortWrapper();
			fsw.setField(it.govpay.orm.Uo.model().COD_UO);
			fsw.setSortOrder(SortOrder.ASC);
			filter.getFilterSortList().add(fsw);

			boolean visualizzaRicerca = true;
			Map<String, String> params = new HashMap<String, String>();

			// tutte le unita' con codice uo = 'EC' sono nascoste
			filter.setExcludeEC(true); 
			
			String abilitatoId = Utils.getInstance(this.getLanguage()).getMessageFromResourceBundle(this.nomeServizio + ".abilitato.id");
			String abilitato = this.getParameter(uriInfo, abilitatoId, String.class);
			
			if(StringUtils.isNotEmpty(abilitato)) {
				params.put(abilitatoId, abilitato);
			}
			
			filter.setSearchAbilitato(this.getMostraDisabilitato(abilitato)); 

			String idDominioId = Utils.getInstance(this.getLanguage()).getMessageFromResourceBundle(this.nomeServizio+ ".idDominio.id");
			this.idDominio = this.getParameter(uriInfo, idDominioId, Long.class);
			filter.setDominioFilter(this.idDominio);

			if(simpleSearch) {
				// simplesearch
				String simpleSearchString = this.getParameter(uriInfo, DarsService.SIMPLE_SEARCH_PARAMETER_ID, String.class);
				params.put(DarsService.SIMPLE_SEARCH_PARAMETER_ID, simpleSearchString);

				if(StringUtils.isNotEmpty(simpleSearchString)) {
					filter.setSimpleSearchString(simpleSearchString);
				}
			} else {
				String codUoId = Utils.getInstance(this.getLanguage()).getMessageFromResourceBundle(this.nomeServizio + ".codUo.id");
				String codUo = this.getParameter(uriInfo, codUoId, String.class);

				if(StringUtils.isNotEmpty(codUo)){
					filter.setCodUo(codUo);
					params.put(codUoId, codUo);
				}
			}

			long count = unitaOperativaBD.count(filter);

			params.put(idDominioId, this.idDominio + "");

			// visualizza la ricerca solo se i risultati sono > del limit
			visualizzaRicerca = visualizzaRicerca && this.visualizzaRicerca(count, limit);
			InfoForm infoRicerca =this.getInfoRicerca(uriInfo, bd, visualizzaRicerca,params);
			String simpleSearchPlaceholder = Utils.getInstance(this.getLanguage()).getMessageFromResourceBundle(this.nomeServizio+".simpleSearch.placeholder");
			
			Elenco elenco = new Elenco(this.titoloServizio, infoRicerca,
					this.getInfoCreazione(uriInfo, bd),
					count, this.getInfoEsportazione(uriInfo, bd), this.getInfoCancellazione(uriInfo, bd),simpleSearchPlaceholder); 

			List<UnitaOperativa> findAll =  unitaOperativaBD.findAll(filter);

			if(findAll != null && findAll.size() > 0){
				for (UnitaOperativa entry : findAll) {
					elenco.getElenco().add(this.getElemento(entry, entry.getId(), this.pathServizio,bd));
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

	@Override
	public InfoForm getInfoRicerca(UriInfo uriInfo, BasicBD bd, boolean visualizzaRicerca, Map<String, String> parameters) throws ConsoleException {
		URI ricerca =  this.getUriRicerca(uriInfo, bd, parameters);
		InfoForm infoRicerca = new InfoForm(ricerca);

		if(visualizzaRicerca) {
			String codUoId = Utils.getInstance(this.getLanguage()).getMessageFromResourceBundle(this.nomeServizio + ".codUo.id");
			String abilitatoId = Utils.getInstance(this.getLanguage()).getMessageFromResourceBundle(this.nomeServizio + ".abilitato.id");
			
			if(this.infoRicercaMap == null){
				this.initInfoRicerca(uriInfo, bd);
			}

			Sezione sezioneRoot = infoRicerca.getSezioneRoot();

			InputText codUnitaOperativa = (InputText) this.infoRicercaMap.get(codUoId);
			codUnitaOperativa.setDefaultValue(null);
			sezioneRoot.addField(codUnitaOperativa);
			
			CheckButton abilitato = (CheckButton) this.infoRicercaMap.get(abilitatoId);
			abilitato.setDefaultValue(false);
			sezioneRoot.addField(abilitato);

		}
		return infoRicerca;
	}

	private void initInfoRicerca(UriInfo uriInfo, BasicBD bd) throws ConsoleException{
		if(this.infoRicercaMap == null){
			this.infoRicercaMap = new HashMap<String, ParamField<?>>();

			String codUoId = Utils.getInstance(this.getLanguage()).getMessageFromResourceBundle(this.nomeServizio + ".codUo.id");

			// codUO
			String codUnitaOperativaLabel = Utils.getInstance(this.getLanguage()).getMessageFromResourceBundle(this.nomeServizio + ".codUo.label");
			InputText codUnitaOperativa = new InputText(codUoId, codUnitaOperativaLabel, null, false, false, true, 1, 255);
			this.infoRicercaMap.put(codUoId, codUnitaOperativa);
			
			String abilitatoId = Utils.getInstance(this.getLanguage()).getMessageFromResourceBundle(this.nomeServizio + ".abilitato.id");
			CheckButton abilitato = this.creaCheckButtonSearchMostraDisabilitato(abilitatoId);
			this.infoRicercaMap.put(abilitatoId, abilitato);

		}
	}

	@Override
	public InfoForm getInfoCreazione(UriInfo uriInfo, BasicBD bd) throws ConsoleException {
		InfoForm infoCreazione =  null;
		try {
			if(this.darsService.isServizioAbilitatoScrittura(bd, this.funzionalita)){
				URI creazione = this.getUriCreazione(uriInfo, bd);
				infoCreazione = new InfoForm(creazione,Utils.getInstance(this.getLanguage()).getMessageFromResourceBundle(this.nomeServizio + ".creazione.titolo"));

				String codUoId = Utils.getInstance(this.getLanguage()).getMessageFromResourceBundle(this.nomeServizio + ".codUo.id");
				String idDominioId = Utils.getInstance(this.getLanguage()).getMessageFromResourceBundle(this.nomeServizio + ".idDominio.id");
				String abilitatoId = Utils.getInstance(this.getLanguage()).getMessageFromResourceBundle(this.nomeServizio + ".abilitato.id");
				String unitaOperativaId = Utils.getInstance(this.getLanguage()).getMessageFromResourceBundle(this.nomeServizio + ".id.id");

				AnagraficaHandler anagraficaHandler = new AnagraficaHandler(ANAGRAFICA_UO,this.nomeServizio,this.pathServizio,this.getLanguage());
				List<ParamField<?>> infoCreazioneAnagrafica = anagraficaHandler.getInfoCreazioneAnagraficaUO(uriInfo, bd);

				if(this.infoCreazioneMap == null){
					this.initInfoCreazione(uriInfo, bd);

				}

				Sezione sezioneRoot = infoCreazione.getSezioneRoot();

				InputNumber idInterm = (InputNumber) this.infoCreazioneMap.get(unitaOperativaId);
				idInterm.setDefaultValue(null);
				sezioneRoot.addField(idInterm);

				InputText codUnitaOperativa = (InputText) this.infoCreazioneMap.get(codUoId);
				codUnitaOperativa.setDefaultValue(null);
				sezioneRoot.addField(codUnitaOperativa);

				// idDominio
				InputNumber idDominio = (InputNumber) this.infoCreazioneMap.get(idDominioId);
				idDominio.setDefaultValue(this.idDominio);
				sezioneRoot.addField(idDominio);

				CheckButton abilitato = (CheckButton) this.infoCreazioneMap.get(abilitatoId);
				abilitato.setDefaultValue(true); 
				sezioneRoot.addField(abilitato);

				Sezione sezioneAnagrafica = infoCreazione.addSezione(Utils.getInstance(this.getLanguage()).getMessageFromResourceBundle(this.nomeServizio + "." + ANAGRAFICA_UO + ".titolo"));

				for (ParamField<?> par : infoCreazioneAnagrafica) { 
					sezioneAnagrafica.addField(par); 	
				}
			}
		} catch (ServiceException e) {
			throw new ConsoleException(e);
		}
		return infoCreazione;
	}

	private void initInfoCreazione(UriInfo uriInfo, BasicBD bd) throws ConsoleException{
		if(this.infoCreazioneMap == null){
			this.infoCreazioneMap = new HashMap<String, ParamField<?>>();

			String codUoId = Utils.getInstance(this.getLanguage()).getMessageFromResourceBundle(this.nomeServizio + ".codUo.id");
			String idDominioId = Utils.getInstance(this.getLanguage()).getMessageFromResourceBundle(this.nomeServizio + ".idDominio.id");
			String abilitatoId = Utils.getInstance(this.getLanguage()).getMessageFromResourceBundle(this.nomeServizio + ".abilitato.id");
			String unitaOperativaId = Utils.getInstance(this.getLanguage()).getMessageFromResourceBundle(this.nomeServizio + ".id.id");

			// id 
			InputNumber id = new InputNumber(unitaOperativaId, null, null, true, true, false, 1, 20);
			this.infoCreazioneMap.put(unitaOperativaId, id);

			// codUnitaOperativa
			String codUnitaOperativaLabel = Utils.getInstance(this.getLanguage()).getMessageFromResourceBundle(this.nomeServizio + ".codUo.label");
			InputText codUnitaOperativa = new InputText(codUoId, codUnitaOperativaLabel, null, true, false, true, 1, 35);
			codUnitaOperativa.setSuggestion(Utils.getInstance(this.getLanguage()).getMessageFromResourceBundle(this.nomeServizio + ".codUo.suggestion"));
			codUnitaOperativa.setValidation(null, Utils.getInstance(this.getLanguage()).getMessageFromResourceBundle(this.nomeServizio + ".codUo.errorMessage"));
			this.infoCreazioneMap.put(codUoId, codUnitaOperativa);

			// idDominio
			InputNumber idDominio = new InputNumber(idDominioId, null, null, true, true, false, 1, 255);
			this.infoCreazioneMap.put(idDominioId, idDominio);

			// abilitato
			String abilitatoLabel = Utils.getInstance(this.getLanguage()).getMessageFromResourceBundle(this.nomeServizio + ".abilitato.label");
			CheckButton abiliato = new CheckButton(abilitatoId, abilitatoLabel, true, false, false, true);
			this.infoCreazioneMap.put(abilitatoId, abiliato);

		}


	}

	@Override
	public InfoForm getInfoModifica(UriInfo uriInfo, BasicBD bd, UnitaOperativa entry) throws ConsoleException {
		InfoForm infoModifica = null;
		try {
			if(this.darsService.isServizioAbilitatoScrittura(bd, this.funzionalita)){
				URI modifica = this.getUriModifica(uriInfo, bd);
				infoModifica = new InfoForm(modifica,Utils.getInstance(this.getLanguage()).getMessageFromResourceBundle(this.nomeServizio + ".modifica.titolo"));

				String codUoId = Utils.getInstance(this.getLanguage()).getMessageFromResourceBundle(this.nomeServizio + ".codUo.id");
				String idDominioId = Utils.getInstance(this.getLanguage()).getMessageFromResourceBundle(this.nomeServizio + ".idDominio.id");
				String abilitatoId = Utils.getInstance(this.getLanguage()).getMessageFromResourceBundle(this.nomeServizio + ".abilitato.id");
				String unitaOperativaId = Utils.getInstance(this.getLanguage()).getMessageFromResourceBundle(this.nomeServizio + ".id.id");


				AnagraficaHandler anagraficaHandler = new AnagraficaHandler(ANAGRAFICA_UO,this.nomeServizio,this.pathServizio,this.getLanguage());
				List<ParamField<?>> infoCreazioneAnagrafica = anagraficaHandler.getInfoModificaAnagraficaUO(uriInfo, bd,entry.getAnagrafica());

				if(this.infoCreazioneMap == null){
					this.initInfoCreazione(uriInfo, bd);
				}

				Sezione sezioneRoot = infoModifica.getSezioneRoot();
				InputNumber idInterm = (InputNumber) this.infoCreazioneMap.get(unitaOperativaId);
				idInterm.setDefaultValue(entry.getId());
				sezioneRoot.addField(idInterm);

				InputText codUnitaOperativa = (InputText) this.infoCreazioneMap.get(codUoId);
				codUnitaOperativa.setDefaultValue(entry.getCodUo());
				sezioneRoot.addField(codUnitaOperativa);

				InputNumber idDominio = (InputNumber) this.infoCreazioneMap.get(idDominioId);
				idDominio.setDefaultValue(entry.getIdDominio());
				sezioneRoot.addField(idDominio);

				CheckButton abilitato = (CheckButton) this.infoCreazioneMap.get(abilitatoId);
				abilitato.setDefaultValue(entry.isAbilitato()); 
				sezioneRoot.addField(abilitato);


				Sezione sezioneAnagrafica = infoModifica.addSezione(Utils.getInstance(this.getLanguage()).getMessageFromResourceBundle(this.nomeServizio + "." + ANAGRAFICA_UO + ".titolo"));

				for (ParamField<?> par : infoCreazioneAnagrafica) { 
					sezioneAnagrafica.addField(par); 	
				}
			}
		} catch (ServiceException e) {
			throw new ConsoleException(e);
		}
		return infoModifica;
	}

	@Override
	public InfoForm getInfoCancellazione(UriInfo uriInfo, BasicBD bd, Map<String, String> parameters) throws ConsoleException { return null;}

	@Override
	public InfoForm getInfoCancellazioneDettaglio(UriInfo uriInfo, BasicBD bd, UnitaOperativa entry) throws ConsoleException {
		return null;
	}

	@Override
	public InfoForm getInfoEsportazione(UriInfo uriInfo, BasicBD bd, Map<String, String> parameters) throws ConsoleException { return null; }

	@Override
	public InfoForm getInfoEsportazioneDettaglio(UriInfo uriInfo, BasicBD bd, UnitaOperativa entry)	throws ConsoleException {	return null;	}

	@Override
	public Object getField(UriInfo uriInfo,List<RawParamValue>values, String fieldId,BasicBD bd) throws WebApplicationException,ConsoleException {
		this.log.debug("Richiesto field ["+fieldId+"]");
		try{
			// Operazione consentita solo ai ruoli con diritto di scrittura
			this.darsService.checkDirittiServizioScrittura(bd, this.funzionalita); 

			if(this.infoCreazioneMap == null){
				this.initInfoCreazione(uriInfo, bd);
			}

			if(this.infoCreazioneMap.containsKey(fieldId)){
				RefreshableParamField<?> paramField = (RefreshableParamField<?>) this.infoCreazioneMap.get(fieldId);

				paramField.aggiornaParametro(values,bd,this.getLanguage());

				return paramField;

			}

			this.log.debug("Field ["+fieldId+"] non presente.");

		}catch(Exception e){
			throw new ConsoleException(e);
		}
		return null;
	}

	@Override
	public Object getSearchField(UriInfo uriInfo, List<RawParamValue> values, String fieldId, BasicBD bd)
			throws WebApplicationException, ConsoleException {
		return null;
	}

	@Override
	public Object getDeleteField(UriInfo uriInfo, List<RawParamValue> values, String fieldId, BasicBD bd) throws WebApplicationException, ConsoleException { return null; }

	@Override
	public Object getExportField(UriInfo uriInfo, List<RawParamValue> values, String fieldId, BasicBD bd) throws WebApplicationException, ConsoleException { return null; }


	@Override
	public Dettaglio getDettaglio(long id, UriInfo uriInfo, BasicBD bd) throws WebApplicationException,ConsoleException {
		String methodName = "dettaglio " + this.titoloServizio + "."+ id;

		try{
			this.log.info("Esecuzione " + methodName + " in corso...");
			// Operazione consentita solo ai ruoli con diritto di lettura
			this.darsService.checkDirittiServizioLettura(bd, this.funzionalita);

			// recupero oggetto
			UnitaOperativeBD unitaOperativaBD = new UnitaOperativeBD(bd);
			UnitaOperativa unitaOperativa = unitaOperativaBD.getUnitaOperativa(id);

			InfoForm infoModifica = this.getInfoModifica(uriInfo, bd,unitaOperativa);
			InfoForm infoCancellazione = this.getInfoCancellazioneDettaglio(uriInfo, bd, unitaOperativa);
			InfoForm infoEsportazione = null;

			Dettaglio dettaglio = new Dettaglio(this.getTitolo(unitaOperativa,bd), infoEsportazione, infoCancellazione, infoModifica);

			it.govpay.web.rs.dars.model.Sezione root = dettaglio.getSezioneRoot(); 

			// dati dell'unitaOperativa
			root.addVoce(Utils.getInstance(this.getLanguage()).getMessageFromResourceBundle(this.nomeServizio + ".codUo.label"), unitaOperativa.getCodUo());

			DominiBD dominiBD = new DominiBD(bd);
			Dominio dominio = dominiBD.getDominio(unitaOperativa.getIdDominio());

			root.addVoce(Utils.getInstance(this.getLanguage()).getMessageFromResourceBundle(this.nomeServizio + ".idDominio.label"), dominio.getCodDominio());
			root.addVoce(Utils.getInstance(this.getLanguage()).getMessageFromResourceBundle(this.nomeServizio + ".abilitato.label"), Utils.getSiNoAsLabel(unitaOperativa.isAbilitato()));

			// Sezione Anagrafica

			Anagrafica anagrafica = unitaOperativa.getAnagrafica(); 
			it.govpay.web.rs.dars.model.Sezione sezioneAnagrafica = dettaglio.addSezione(Utils.getInstance(this.getLanguage()).getMessageFromResourceBundle(this.nomeServizio + "." + ANAGRAFICA_UO + ".titolo"));
			AnagraficaHandler anagraficaHandler = new AnagraficaHandler(ANAGRAFICA_UO,this.nomeServizio,this.pathServizio,this.getLanguage());
			anagraficaHandler.fillSezioneAnagraficaUO(sezioneAnagrafica, anagrafica);

			this.log.info("Esecuzione " + methodName + " completata.");

			return dettaglio;
		}catch(WebApplicationException e){
			throw e;
		}catch(Exception e){
			throw new ConsoleException(e);
		}
	}

	@Override
	public Dettaglio insert(InputStream is, UriInfo uriInfo, BasicBD bd)
			throws WebApplicationException, ConsoleException,ValidationException,DuplicatedEntryException {
		String methodName = "Insert " + this.titoloServizio;

		try{
			this.log.info("Esecuzione " + methodName + " in corso...");
			// Operazione consentita solo ai ruoli con diritto di scrittura
			this.darsService.checkDirittiServizioScrittura(bd, this.funzionalita);

			UnitaOperativa entry = this.creaEntry(is, uriInfo, bd);

			this.checkEntry(entry, null);

			UnitaOperativeBD uoBD = new UnitaOperativeBD(bd);

			try{
				uoBD.getUnitaOperativa(entry.getIdDominio(),entry.getCodUo());
				String msg = Utils.getInstance(this.getLanguage()).getMessageWithParamsFromResourceBundle(this.nomeServizio + ".oggettoEsistente", entry.getCodUo());
				throw new DuplicatedEntryException(msg);
			}catch(NotFoundException e){}

			uoBD.insertUnitaOperativa(entry); 

			this.log.info("Esecuzione " + methodName + " completata.");

			return this.getDettaglio(entry.getId(), uriInfo, bd);
		}catch(DuplicatedEntryException e){
			throw e;
		}catch(ValidationException e){
			throw e;
		}catch(WebApplicationException e){
			throw e;
		}catch(Exception e){
			throw new ConsoleException(e);
		}
	}

	@Override
	public UnitaOperativa creaEntry(InputStream is, UriInfo uriInfo, BasicBD bd)
			throws WebApplicationException, ConsoleException {
		String methodName = "creaEntry " + this.titoloServizio;
		UnitaOperativa entry = null;
		try{
			this.log.info("Esecuzione " + methodName + " in corso...");
			// Operazione consentita solo ai ruoli con diritto di scrittura
			this.darsService.checkDirittiServizioScrittura(bd, this.funzionalita); 

			JsonConfig jsonConfig = new JsonConfig();
			ByteArrayOutputStream baos = new ByteArrayOutputStream();
			Utils.copy(is, baos);

			baos.flush();
			baos.close();

			JSONObject jsonObject = JSONObject.fromObject( baos.toString() );  
			jsonConfig.setRootClass(UnitaOperativa.class);
			entry = (UnitaOperativa) JSONObject.toBean( jsonObject, jsonConfig );

			//jsonObjectIntermediario = JSONObject.fromObject( baos.toString() );  
			jsonConfig.setRootClass(Anagrafica.class);
			Anagrafica anagrafica = (Anagrafica) JSONObject.toBean( jsonObject, jsonConfig );

			anagrafica.setCodUnivoco(entry.getCodUo());
			entry.setAnagrafica(anagrafica);  

			this.log.info("Esecuzione " + methodName + " completata.");
			return entry;
		}catch(WebApplicationException e){
			throw e;
		}catch(Exception e){
			throw new ConsoleException(e);
		}
	}

	@Override
	public void checkEntry(UnitaOperativa entry, UnitaOperativa oldEntry) throws ValidationException {
		if(entry == null || entry.getCodUo() == null || entry.getCodUo().isEmpty()) {
			throw new ValidationException(Utils.getInstance(this.getLanguage()).getMessageFromResourceBundle(this.nomeServizio + ".creazione.erroreCodUoObbligatorio"));
		}

		Anagrafica anagrafica = entry.getAnagrafica();
		if(anagrafica != null && anagrafica.getRagioneSociale() != null && anagrafica.getRagioneSociale().length() > 255) {
			throw new ValidationException(Utils.getInstance(this.getLanguage()).getMessageFromResourceBundle(this.nomeServizio + ".creazione.erroreLunghezzaRagioneSocialeErrata"));
		} 

		if(anagrafica == null || anagrafica.getCodUnivoco() == null || anagrafica.getCodUnivoco().isEmpty()) {
			throw new ValidationException(Utils.getInstance(this.getLanguage()).getMessageFromResourceBundle(this.nomeServizio + ".creazione.erroreCodUnivocoObbligatorio"));
		}
		if(entry.getIdDominio() == 0) {
			throw new ValidationException(Utils.getInstance(this.getLanguage()).getMessageFromResourceBundle(this.nomeServizio + ".creazione.erroreDominioObbligatorio"));
		}
		if(oldEntry != null) {
			if(!entry.getCodUo().equals(oldEntry.getCodUo())) {
				throw new ValidationException(Utils.getInstance(this.getLanguage()).getMessageWithParamsFromResourceBundle(this.nomeServizio + ".aggiornamento.erroreCodUoNonCoincide",oldEntry.getCodUo(),entry.getCodUo()));
			}
			if(entry.getIdDominio() != oldEntry.getIdDominio()) {
				throw new ValidationException(Utils.getInstance(this.getLanguage()).getMessageWithParamsFromResourceBundle(this.nomeServizio + ".aggiornamento.erroreDominioNonCoincide"));
			}
		}
	}

	@Override
	public Dettaglio update(InputStream is, UriInfo uriInfo, BasicBD bd) throws WebApplicationException, ConsoleException, ValidationException {
		String methodName = "Update " + this.titoloServizio;

		try{
			this.log.info("Esecuzione " + methodName + " in corso...");
			// Operazione consentita solo ai ruoli con diritto di scrittura
			this.darsService.checkDirittiServizioScrittura(bd, this.funzionalita);

			UnitaOperativa entry = this.creaEntry(is, uriInfo, bd);

			UnitaOperativeBD uoBD = new UnitaOperativeBD(bd);
			UnitaOperativa oldEntry = uoBD.getUnitaOperativa(entry.getIdDominio(),entry.getCodUo());

			this.checkEntry(entry, oldEntry);

			uoBD.updateUnitaOperativa(entry); 

			this.log.info("Esecuzione " + methodName + " completata.");
			return this.getDettaglio(entry.getId(), uriInfo, bd);
		}catch(ValidationException e){
			throw e;
		}catch(WebApplicationException e){
			throw e;
		}catch(Exception e){
			throw new ConsoleException(e);
		}
	}

	@Override
	public Elenco delete(List<Long> idsToDelete, List<RawParamValue> rawValues, UriInfo uriInfo, BasicBD bd) throws ConsoleException {
		return null;
	}

	@Override
	public String getTitolo(UnitaOperativa entry, BasicBD bd) {
		StringBuilder sb = new StringBuilder();

		String ragioneSociale = entry.getAnagrafica().getRagioneSociale();
		if(ragioneSociale != null){
			sb.append(ragioneSociale);
			sb.append(" (").append(entry.getCodUo()).append(")");
		} else {
			sb.append(entry.getCodUo());
		}

		return sb.toString();
	}

	@Override
	public String getSottotitolo(UnitaOperativa entry, BasicBD bd) throws ConsoleException {
		StringBuilder sb = new StringBuilder();

		try{
			sb.append(Utils.getAbilitatoAsLabel(entry.isAbilitato()));
			//sb.append(", Dominio: ").append(entry.getDominio(bd).getCodDominio());
		}catch(Exception e){
			throw new ConsoleException(e);
		}

		return sb.toString();
	}

	@Override
	public Map<String, Voce<String>> getVoci(UnitaOperativa entry, BasicBD bd) throws ConsoleException { return null; }

	@Override
	public String esporta(List<Long> idsToExport, List<RawParamValue> rawValues, UriInfo uriInfo, BasicBD bd, ZipOutputStream zout)
			throws WebApplicationException, ConsoleException,ExportException {
		return null;
	}

	@Override
	public String esporta(Long idToExport, List<RawParamValue> rawValues, UriInfo uriInfo, BasicBD bd, ZipOutputStream zout)	throws WebApplicationException, ConsoleException ,ExportException{
		return null;
	}

	@Override
	public Object uplaod(MultipartFormDataInput input, UriInfo uriInfo, BasicBD bd)	throws WebApplicationException, ConsoleException, ValidationException { return null;}
}
