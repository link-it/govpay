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
package it.govpay.web.rs.dars.anagrafica.operatori;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.net.URI;
import java.util.ArrayList;
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
import it.govpay.bd.anagrafica.AnagraficaManager;
import it.govpay.bd.anagrafica.OperatoriBD;
import it.govpay.bd.anagrafica.RuoliBD;
import it.govpay.bd.anagrafica.filters.OperatoreFilter;
import it.govpay.bd.anagrafica.filters.RuoloFilter;
import it.govpay.bd.model.Operatore;
import it.govpay.model.Ruolo;
import it.govpay.web.rs.dars.anagrafica.ruoli.Ruoli;
import it.govpay.web.rs.dars.anagrafica.ruoli.RuoliHandler;
import it.govpay.web.rs.dars.base.DarsHandler;
import it.govpay.web.rs.dars.base.DarsService;
import it.govpay.web.rs.dars.exception.ConsoleException;
import it.govpay.web.rs.dars.exception.DeleteException;
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
import it.govpay.web.rs.dars.model.input.base.SelectList;
import it.govpay.web.utils.Utils;
import net.sf.json.JSONObject;
import net.sf.json.JsonConfig;

public class OperatoriHandler extends DarsHandler<Operatore> implements IDarsHandler<Operatore>{

	public OperatoriHandler(Logger log, DarsService darsService) {
		super(log,darsService);
	}

	@Override
	public Elenco getElenco(UriInfo uriInfo,BasicBD bd) throws WebApplicationException,ConsoleException {
		String methodName = "getElenco " + this.titoloServizio;

		try{
			this.log.info("Esecuzione " + methodName + " in corso..."); 
			// Operazione consentita solo agli utenti che hanno almeno un ruolo consentito per la funzionalita'
			this.darsService.checkDirittiServizio(bd, this.funzionalita);

			Integer offset = this.getOffset(uriInfo);
			Integer limit = this.getLimit(uriInfo);

			boolean simpleSearch = this.containsParameter(uriInfo, DarsService.SIMPLE_SEARCH_PARAMETER_ID);

			OperatoriBD operatoriBD = new OperatoriBD(bd);
			OperatoreFilter filter = operatoriBD.newFilter(simpleSearch);
			filter.setOffset(offset);
			filter.setLimit(limit);
			FilterSortWrapper fsw = new FilterSortWrapper();
			fsw.setField(it.govpay.orm.Operatore.model().NOME);
			fsw.setSortOrder(SortOrder.ASC);
			filter.getFilterSortList().add(fsw);


			if(simpleSearch){
				// simplesearch
				String simpleSearchString = this.getParameter(uriInfo, DarsService.SIMPLE_SEARCH_PARAMETER_ID, String.class);
				if(StringUtils.isNotEmpty(simpleSearchString)) {
					filter.setSimpleSearchString(simpleSearchString);
				}
			}else{
				String principalId = Utils.getInstance(this.getLanguage()).getMessageFromResourceBundle(this.nomeServizio + ".principal.id");
				String principal = this.getParameter(uriInfo, principalId, String.class);
				String ruoloId = Utils.getInstance(this.getLanguage()).getMessageFromResourceBundle(this.nomeServizio + ".ruolo.id");
				String ruolo = this.getParameter(uriInfo, ruoloId, String.class);

				if(StringUtils.isNotEmpty(principal)){
					filter.setPrincipal(principal);
				}

				if(StringUtils.isNotEmpty(ruolo)){
					filter.setRuolo(ruolo);
				}
			}
			long count = operatoriBD.count(filter);

			// visualizza la ricerca solo se i risultati sono > del limit
			boolean visualizzaRicerca = this.visualizzaRicerca(count, limit);
			InfoForm infoRicerca = this.getInfoRicerca(uriInfo, bd, visualizzaRicerca);
			String simpleSearchPlaceholder = Utils.getInstance(this.getLanguage()).getMessageFromResourceBundle(this.nomeServizio+".simpleSearch.placeholder");
			Elenco elenco = new Elenco(this.titoloServizio, infoRicerca,
					this.getInfoCreazione(uriInfo, bd),
					count, this.getInfoEsportazione(uriInfo, bd), this.getInfoCancellazione(uriInfo, bd),simpleSearchPlaceholder); 

			List<Operatore> findAll = operatoriBD.findAll(filter);

			if(findAll != null && findAll.size() > 0){
				for (Operatore entry : findAll) {
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

	@SuppressWarnings("unchecked")
	@Override
	public InfoForm getInfoRicerca(UriInfo uriInfo, BasicBD bd, boolean visualizzaRicerca, Map<String,String> parameters) throws ConsoleException {
		URI ricerca = this.getUriRicerca(uriInfo, bd);
		InfoForm infoRicerca = new InfoForm(ricerca);

		if(visualizzaRicerca){
			String principalId = Utils.getInstance(this.getLanguage()).getMessageFromResourceBundle(this.nomeServizio + ".principal.id");
			String ruoloId = Utils.getInstance(this.getLanguage()).getMessageFromResourceBundle(this.nomeServizio + ".ruoli.id");

			if(this.infoRicercaMap == null){
				this.initInfoRicerca(uriInfo, bd);
			}

			Sezione sezioneRoot = infoRicerca.getSezioneRoot();
			InputText principal  = (InputText) this.infoRicercaMap.get(principalId);
			sezioneRoot.addField(principal);

			SelectList<String> ruolo = (SelectList<String>) this.infoRicercaMap.get(ruoloId);
			List<Voce<String>> listaRuoli = new ArrayList<Voce<String>>();
			try{
				RuoliBD ruoliBD = new RuoliBD(bd);
				RuoloFilter ruoliFilter = ruoliBD.newFilter();
				FilterSortWrapper fsw = new FilterSortWrapper();
				fsw.setField(it.govpay.orm.Ruolo.model().DESCRIZIONE);
				fsw.setSortOrder(SortOrder.ASC);
				ruoliFilter.getFilterSortList().add(fsw);

				List<it.govpay.model.Ruolo> findAll = ruoliBD.findAll(ruoliFilter);

				if(findAll != null && findAll.size() > 0){
					for (it.govpay.model.Ruolo r : findAll) {
						listaRuoli.add(new Voce<String>(r.getDescrizione(), r.getCodRuolo()));  
					}
					listaRuoli.add(new Voce<String>(Utils.getInstance(this.getLanguage()).getMessageFromResourceBundle(this.nomeServizio + ".ruoli."+Operatore.RUOLO_SYSTEM+".label"),
							Operatore.RUOLO_SYSTEM));
				}

			}catch(Exception e){
				throw new ConsoleException(e);
			}
			ruolo.setValues(listaRuoli);
			ruolo.setDefaultValue("");
			sezioneRoot.addField(ruolo); 

		}
		return infoRicerca;
	}

	private void initInfoRicerca(UriInfo uriInfo, BasicBD bd) throws ConsoleException{
		if(this.infoRicercaMap == null){
			this.infoRicercaMap = new HashMap<String, ParamField<?>>();

			String principalId = Utils.getInstance(this.getLanguage()).getMessageFromResourceBundle(this.nomeServizio + ".principal.id");
			String ruoloId = Utils.getInstance(this.getLanguage()).getMessageFromResourceBundle(this.nomeServizio + ".ruoli.id");

			// principal
			String principalLabel = Utils.getInstance(this.getLanguage()).getMessageFromResourceBundle(this.nomeServizio + ".principal.label");
			InputText principal = new InputText(principalId, principalLabel, null, false, false, true, 1, 50);
			this.infoRicercaMap.put(principalId, principal);

			// ruolo
			String ruoloLabel = Utils.getInstance(this.getLanguage()).getMessageFromResourceBundle(this.nomeServizio + ".ruoli.label");
			List<Voce<String>> tipiProfili = new ArrayList<Voce<String>>();
			SelectList<String> ruolo = new SelectList<String>(ruoloId, ruoloLabel, "", false, false, true, tipiProfili);
			this.infoRicercaMap.put(ruoloId, ruolo);

		}
	}

	@Override
	public InfoForm getInfoCreazione(UriInfo uriInfo, BasicBD bd) throws ConsoleException {
		InfoForm infoCreazione =  null;
		try {
			if(this.darsService.isServizioAbilitatoScrittura(bd, this.funzionalita)){
				URI creazione = this.getUriCreazione(uriInfo, bd);
				infoCreazione = new InfoForm(creazione,Utils.getInstance(this.getLanguage()).getMessageFromResourceBundle(this.nomeServizio + ".creazione.titolo"));

				String operatoreId = Utils.getInstance(this.getLanguage()).getMessageFromResourceBundle(this.nomeServizio + ".id.id");
				String principalId = Utils.getInstance(this.getLanguage()).getMessageFromResourceBundle(this.nomeServizio + ".principal.id");
				String nomeId = Utils.getInstance(this.getLanguage()).getMessageFromResourceBundle(this.nomeServizio + ".nome.id");
				String abilitatoId = Utils.getInstance(this.getLanguage()).getMessageFromResourceBundle(this.nomeServizio + ".abilitato.id");
				String ruoliId = Utils.getInstance(this.getLanguage()).getMessageFromResourceBundle(this.nomeServizio + ".ruoli.id");

				if(this.infoCreazioneMap == null){
					this.initInfoCreazione(uriInfo, bd);
				}

				Sezione sezioneRoot = infoCreazione.getSezioneRoot();

				InputNumber idInterm = (InputNumber) this.infoCreazioneMap.get(operatoreId);
				idInterm.setDefaultValue(null);
				sezioneRoot.addField(idInterm);

				InputText principal  = (InputText) this.infoCreazioneMap.get(principalId);
				principal.setDefaultValue(null);
				principal.setEditable(true); 
				sezioneRoot.addField(principal);

				InputText nome = (InputText) this.infoCreazioneMap.get(nomeId);
				nome.setDefaultValue(null);
				sezioneRoot.addField(nome);

				List<RawParamValue> ruoliParamValues = new ArrayList<RawParamValue>();
				ruoliParamValues.add(new RawParamValue(operatoreId, null)); 
				it.govpay.web.rs.dars.anagrafica.operatori.input.Ruoli ruoli = (it.govpay.web.rs.dars.anagrafica.operatori.input.Ruoli) this.infoCreazioneMap.get(ruoliId);
				ruoli.init(ruoliParamValues, bd,this.getLanguage());
				ruoli.aggiornaParametro(ruoliParamValues, bd,this.getLanguage());

				sezioneRoot.addField(ruoli);

				CheckButton abilitato = (CheckButton) this.infoCreazioneMap.get(abilitatoId);
				abilitato.setDefaultValue(true); 
				sezioneRoot.addField(abilitato);

			}
		} catch (ServiceException e) {
			throw new ConsoleException(e);
		}
		return infoCreazione;
	}

	private void initInfoCreazione(UriInfo uriInfo, BasicBD bd) throws ConsoleException{
		if(this.infoCreazioneMap == null){
			this.infoCreazioneMap = new HashMap<String, ParamField<?>>();

			String abilitatoId = Utils.getInstance(this.getLanguage()).getMessageFromResourceBundle(this.nomeServizio + ".abilitato.id");
			String principalId = Utils.getInstance(this.getLanguage()).getMessageFromResourceBundle(this.nomeServizio + ".principal.id");
			String nomeId = Utils.getInstance(this.getLanguage()).getMessageFromResourceBundle(this.nomeServizio + ".nome.id");
			String ruoliId = Utils.getInstance(this.getLanguage()).getMessageFromResourceBundle(this.nomeServizio + ".ruoli.id");
			String operatoreId = Utils.getInstance(this.getLanguage()).getMessageFromResourceBundle(this.nomeServizio + ".id.id");

			// id 
			InputNumber id = new InputNumber(operatoreId, null, null, true, true, false, 1, 20);
			this.infoCreazioneMap.put(operatoreId, id);

			// principal
			String principalLabel = Utils.getInstance(this.getLanguage()).getMessageFromResourceBundle(this.nomeServizio + ".principal.label");
			InputText principal = new InputText(principalId, principalLabel, null, true, false, true, 1, 50);
			principal.setValidation(null, Utils.getInstance(this.getLanguage()).getMessageFromResourceBundle(this.nomeServizio + ".principal.errorMessage"));
			this.infoCreazioneMap.put(principalId, principal);

			// nome
			String nomeLabel = Utils.getInstance(this.getLanguage()).getMessageFromResourceBundle(this.nomeServizio + ".nome.label");
			InputText nome = new InputText(nomeId, nomeLabel, null, true, false, true, 1, 255);
			nome.setValidation(null, Utils.getInstance(this.getLanguage()).getMessageFromResourceBundle(this.nomeServizio + ".nome.errorMessage"));
			this.infoCreazioneMap.put(nomeId, nome);

			// ruoli
			String ruoliLabel = Utils.getInstance(this.getLanguage()).getMessageFromResourceBundle(this.nomeServizio + ".ruoli.label");
			List<RawParamValue> ruoliParamValues = new ArrayList<RawParamValue>();
			URI ruoliRefreshUri = this.getUriField(uriInfo, bd, ruoliId); 
			it.govpay.web.rs.dars.anagrafica.operatori.input.Ruoli ruoli = 
					new it.govpay.web.rs.dars.anagrafica.operatori.input.Ruoli(this.nomeServizio, ruoliId, ruoliLabel, ruoliRefreshUri , ruoliParamValues, bd,this.getLanguage());
			ruoli.init(ruoliParamValues, bd,this.getLanguage());
			ruoli.setValidation(null, Utils.getInstance(this.getLanguage()).getMessageFromResourceBundle(this.nomeServizio + ".ruoli.errorMessage"));
			this.infoCreazioneMap.put(ruoliId, ruoli);

			// abilitato
			String abilitatoLabel = Utils.getInstance(this.getLanguage()).getMessageFromResourceBundle(this.nomeServizio + ".abilitato.label");
			CheckButton abiliato = new CheckButton(abilitatoId, abilitatoLabel, true, false, false, true);
			this.infoCreazioneMap.put(abilitatoId, abiliato);
		}
	}

	@Override
	public InfoForm getInfoModifica(UriInfo uriInfo, BasicBD bd, Operatore entry) throws ConsoleException {
		InfoForm infoModifica = null;
		try {
			if(this.darsService.isServizioAbilitatoScrittura(bd, this.funzionalita)){
				URI modifica = this.getUriModifica(uriInfo, bd);
				infoModifica = new InfoForm(modifica,Utils.getInstance(this.getLanguage()).getMessageFromResourceBundle(this.nomeServizio + ".modifica.titolo"));

				String abilitatoId = Utils.getInstance(this.getLanguage()).getMessageFromResourceBundle(this.nomeServizio + ".abilitato.id");
				String principalId = Utils.getInstance(this.getLanguage()).getMessageFromResourceBundle(this.nomeServizio + ".principal.id");
				String nomeId = Utils.getInstance(this.getLanguage()).getMessageFromResourceBundle(this.nomeServizio + ".nome.id");
				String operatoreId = Utils.getInstance(this.getLanguage()).getMessageFromResourceBundle(this.nomeServizio + ".id.id");
				String ruoliId = Utils.getInstance(this.getLanguage()).getMessageFromResourceBundle(this.nomeServizio + ".ruoli.id");

				if(this.infoCreazioneMap == null){
					this.initInfoCreazione(uriInfo, bd);
				}

				Sezione sezioneRoot = infoModifica.getSezioneRoot();
				InputNumber idInterm = (InputNumber) this.infoCreazioneMap.get(operatoreId);
				idInterm.setDefaultValue(entry.getId());
				sezioneRoot.addField(idInterm);

				InputText principal  = (InputText) this.infoCreazioneMap.get(principalId);
				principal.setDefaultValue(entry.getPrincipal());
				principal.setEditable(false); 
				sezioneRoot.addField(principal);

				InputText nome = (InputText) this.infoCreazioneMap.get(nomeId);
				nome.setDefaultValue(entry.getNome());
				sezioneRoot.addField(nome);

				List<RawParamValue> ruoliParamValues = new ArrayList<RawParamValue>();
				ruoliParamValues.add(new RawParamValue(operatoreId, entry.getId() + "")); 
				it.govpay.web.rs.dars.anagrafica.operatori.input.Ruoli ruoli = (it.govpay.web.rs.dars.anagrafica.operatori.input.Ruoli) this.infoCreazioneMap.get(ruoliId);
				ruoli.init(ruoliParamValues, bd,this.getLanguage());
				ruoli.aggiornaParametro(ruoliParamValues, bd,this.getLanguage());

				sezioneRoot.addField(ruoli);

				CheckButton abilitato = (CheckButton) this.infoCreazioneMap.get(abilitatoId);
				abilitato.setDefaultValue(entry.isAbilitato()); 
				sezioneRoot.addField(abilitato);

			}
		} catch (ServiceException e) {
			throw new ConsoleException(e);
		}
		return infoModifica;
	}


	@Override
	public InfoForm getInfoCancellazione(UriInfo uriInfo, BasicBD bd, Map<String, String> parameters) throws ConsoleException { return null;}

	@Override
	public InfoForm getInfoCancellazioneDettaglio(UriInfo uriInfo, BasicBD bd, Operatore entry) throws ConsoleException {
		return null;
	}

	@Override
	public InfoForm getInfoEsportazione(UriInfo uriInfo, BasicBD bd, Map<String, String> parameters) throws ConsoleException { return null; }

	@Override
	public InfoForm getInfoEsportazioneDettaglio(UriInfo uriInfo, BasicBD bd, Operatore entry)	throws ConsoleException {	return null;	}

	@Override
	public Object getField(UriInfo uriInfo,List<RawParamValue>values, String fieldId,BasicBD bd) throws WebApplicationException,ConsoleException {
		try{
			this.log.debug("Richiesto field ["+fieldId+"]"); 
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
			return null;
		}catch(Exception e){
			throw new ConsoleException(e);
		}
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

			// Operazione consentita solo all'operatore con ruolo autorizzato
			this.darsService.checkDirittiServizio(bd, this.funzionalita); 

			// recupero oggetto
			OperatoriBD operatoriBD = new OperatoriBD(bd);
			Operatore operatore = operatoriBD.getOperatore(id);

			InfoForm infoModifica = this.getInfoModifica(uriInfo, bd,operatore);
			InfoForm infoCancellazione = this.getInfoCancellazioneDettaglio(uriInfo, bd, operatore);
			InfoForm infoEsportazione = null;

			Dettaglio dettaglio = new Dettaglio(this.getTitolo(operatore,bd), infoEsportazione, infoCancellazione, infoModifica);

			it.govpay.web.rs.dars.model.Sezione root = dettaglio.getSezioneRoot(); 

			// dati operatore
			root.addVoce(Utils.getInstance(this.getLanguage()).getMessageFromResourceBundle(this.nomeServizio + ".principal.label"), operatore.getPrincipal());
			root.addVoce(Utils.getInstance(this.getLanguage()).getMessageFromResourceBundle(this.nomeServizio + ".nome.label"), operatore.getNome());
			List<String> ruoli = operatore.getRuoli();
			if(ruoli != null && ruoli.size() > 0){
				StringBuffer sb = new StringBuffer();
				
				Ruoli ruoliDars = new Ruoli();
				RuoliHandler ruoliDarsHandler = (RuoliHandler) ruoliDars.getDarsHandler();
				for (String codRuolo : ruoli) {
					if(sb.length() > 0 )
						sb.append(", ");

					if(!codRuolo.equals(Operatore.RUOLO_SYSTEM)) {
						try{
							Ruolo r = AnagraficaManager.getRuolo(bd, codRuolo);
							sb.append(ruoliDarsHandler.getTitolo(r, bd)); 
						}catch(Exception e) {}
					} else {
						sb.append(Utils.getInstance(this.getLanguage()).getMessageFromResourceBundle(this.nomeServizio + ".ruoli."+Operatore.RUOLO_SYSTEM+".label"));
					}
				}
				root.addVoce(Utils.getInstance(this.getLanguage()).getMessageFromResourceBundle(this.nomeServizio + ".ruoli.label"), sb.toString());
			}
			
			root.addVoce(Utils.getInstance(this.getLanguage()).getMessageFromResourceBundle(this.nomeServizio + ".abilitato.label"), Utils.getSiNoAsLabel(operatore.isAbilitato()));

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

			Operatore entry = this.creaEntry(is, uriInfo, bd);

			this.checkEntry(entry, null);

			OperatoriBD operatoriBD = new OperatoriBD(bd);

			try{
				operatoriBD.getOperatore(entry.getPrincipal());
				String msg = Utils.getInstance(this.getLanguage()).getMessageWithParamsFromResourceBundle(this.nomeServizio + ".oggettoEsistente", entry.getPrincipal());
				throw new DuplicatedEntryException(msg);
			}catch(NotFoundException e){}

			operatoriBD.insertOperatore(entry); 

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
	public Operatore creaEntry(InputStream is, UriInfo uriInfo, BasicBD bd)
			throws WebApplicationException, ConsoleException {
		String methodName = "creaEntry " + this.titoloServizio;
		Operatore entry = null;
		try{
			this.log.info("Esecuzione " + methodName + " in corso...");
			// Operazione consentita solo ai ruoli con diritto di scrittura
			this.darsService.checkDirittiServizioScrittura(bd, this.funzionalita); 

			JsonConfig jsonConfig = new JsonConfig();
			ByteArrayOutputStream baos = new ByteArrayOutputStream();
			Utils.copy(is, baos);

			baos.flush();
			baos.close();

			Map<String,Class<?>> classMap = new HashMap<String, Class<?>>();
			jsonConfig.setClassMap(classMap);

			JSONObject jsonObject = JSONObject.fromObject( baos.toString() );  

			jsonConfig.setRootClass(Operatore.class);
			entry = (Operatore) JSONObject.toBean( jsonObject, jsonConfig );

			this.log.info("Esecuzione " + methodName + " completata.");
			return entry;
		}catch(WebApplicationException e){
			throw e;
		}catch(Exception e){
			throw new ConsoleException(e);
		}
	}

	@Override
	public void checkEntry(Operatore entry, Operatore oldEntry) throws ValidationException {
		if(entry.getPrincipal() == null || entry.getPrincipal().isEmpty()) {
			throw new ValidationException(Utils.getInstance(this.getLanguage()).getMessageFromResourceBundle(this.nomeServizio + ".creazione.errorePrincipalObbligatorio"));
		}
		if(entry.getPrincipal().contains(" ")) {
			throw new ValidationException(Utils.getInstance(this.getLanguage()).getMessageFromResourceBundle(this.nomeServizio + ".creazione.errorePrincipalNoSpazi"));
		}

		if(entry != null && entry.getPrincipal() != null && entry.getPrincipal().length() > 255) {
			throw new ValidationException(Utils.getInstance(this.getLanguage()).getMessageFromResourceBundle(this.nomeServizio + ".creazione.erroreLunghezzaPrincipal"));
		} 

		if(entry != null && entry.getNome() != null && entry.getNome().length() > 255) {
			throw new ValidationException(Utils.getInstance(this.getLanguage()).getMessageFromResourceBundle(this.nomeServizio + ".creazione.erroreLunghezzaNome"));
		} 

		if(oldEntry != null) {
			if(!entry.getPrincipal().equals(oldEntry.getPrincipal())) {
				throw new ValidationException(Utils.getInstance(this.getLanguage()).getMessageWithParamsFromResourceBundle(this.nomeServizio + ".aggiornamento.errorePrincipalNonCoincide",oldEntry.getPrincipal(),entry.getPrincipal()));
			}
		}
	}

	@Override
	public Dettaglio update(InputStream is, UriInfo uriInfo, BasicBD bd)
			throws WebApplicationException, ConsoleException, ValidationException {
		String methodName = "Update " + this.titoloServizio;

		try{
			this.log.info("Esecuzione " + methodName + " in corso...");
			// Operazione consentita solo ai ruoli con diritto di scrittura
			this.darsService.checkDirittiServizioScrittura(bd, this.funzionalita); 

			Operatore entry = this.creaEntry(is, uriInfo, bd);

			OperatoriBD operatoriBD = new OperatoriBD(bd);
			Operatore oldEntry = operatoriBD.getOperatore(entry.getPrincipal());

			this.checkEntry(entry, oldEntry);

			operatoriBD.updateOperatore(entry); 

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
	public Elenco delete(List<Long> idsToDelete, List<RawParamValue> rawValues, UriInfo uriInfo, BasicBD bd) throws WebApplicationException, ConsoleException, DeleteException {	return null; 	}

	@Override
	public String getTitolo(Operatore entry, BasicBD bd) {
		return entry.getNome();
	}

	@Override
	public String getSottotitolo(Operatore entry, BasicBD bd) {
		StringBuilder sb = new StringBuilder();

		try{
			List<String> ruoli = entry.getRuoli();

			if(ruoli != null && ruoli.size() > 0){
				Ruoli ruoliDars = new Ruoli();
				RuoliHandler ruoliDarsHandler = (RuoliHandler) ruoliDars.getDarsHandler();
				for (String codRuolo : ruoli) {
					if(sb.length() > 0 )
						sb.append(", ");

					if(!codRuolo.equals(Operatore.RUOLO_SYSTEM)) {
						try{
							Ruolo r = AnagraficaManager.getRuolo(bd, codRuolo);
							sb.append(ruoliDarsHandler.getTitolo(r, bd)); 
						}catch(Exception e) {}
					} else {
						sb.append(Utils.getInstance(this.getLanguage()).getMessageFromResourceBundle(this.nomeServizio + ".ruoli."+Operatore.RUOLO_SYSTEM+".label"));
					}
				}
			}
		}catch(Exception e){
		}
		return Utils.getInstance(this.getLanguage()).getMessageWithParamsFromResourceBundle(this.nomeServizio + ".sottotitolo.label",Utils.getAbilitatoAsLabel(entry.isAbilitato()), entry.getPrincipal(), sb.toString());
	}

	@Override
	public Map<String, Voce<String>> getVoci(Operatore entry, BasicBD bd) throws ConsoleException { return null; }

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
