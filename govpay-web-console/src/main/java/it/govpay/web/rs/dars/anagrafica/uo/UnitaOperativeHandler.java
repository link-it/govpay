/*
 * GovPay - Porta di Accesso al Nodo dei Pagamenti SPC 
 * http://www.gov4j.it/govpay
 * 
 * Copyright (c) 2014-2016 Link.it srl (http://www.link.it).
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
package it.govpay.web.rs.dars.anagrafica.uo;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.net.URI;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.UriBuilder;
import javax.ws.rs.core.UriInfo;

import org.apache.commons.lang.StringUtils;
import org.apache.logging.log4j.Logger;
import org.openspcoop2.generic_project.exception.NotFoundException;
import org.openspcoop2.generic_project.exception.ServiceException;
import org.openspcoop2.generic_project.expression.SortOrder;

import it.govpay.bd.BasicBD;
import it.govpay.bd.FilterSortWrapper;
import it.govpay.bd.anagrafica.DominiBD;
import it.govpay.bd.anagrafica.OperatoriBD;
import it.govpay.bd.anagrafica.UnitaOperativeBD;
import it.govpay.bd.anagrafica.filters.DominioFilter;
import it.govpay.bd.anagrafica.filters.UnitaOperativaFilter;
import it.govpay.bd.model.Anagrafica;
import it.govpay.bd.model.Dominio;
import it.govpay.bd.model.Operatore;
import it.govpay.bd.model.UnitaOperativa;
import it.govpay.web.rs.dars.BaseDarsHandler;
import it.govpay.web.rs.dars.BaseDarsService;
import it.govpay.web.rs.dars.IDarsHandler;
import it.govpay.web.rs.dars.anagrafica.anagrafica.AnagraficaHandler;
import it.govpay.web.rs.dars.anagrafica.domini.Domini;
import it.govpay.web.rs.dars.anagrafica.operatori.Operatori;
import it.govpay.web.rs.dars.exception.ConsoleException;
import it.govpay.web.rs.dars.exception.DuplicatedEntryException;
import it.govpay.web.rs.dars.exception.ValidationException;
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
import it.govpay.web.rs.dars.model.input.base.InputNumber;
import it.govpay.web.rs.dars.model.input.base.InputText;
import it.govpay.web.rs.dars.model.input.base.SelectList;
import it.govpay.web.utils.Utils;
import net.sf.json.JSONObject;
import net.sf.json.JsonConfig;

public class UnitaOperativeHandler extends BaseDarsHandler<UnitaOperativa> implements IDarsHandler<UnitaOperativa>{

	private static Map<String, ParamField<?>> infoCreazioneMap = null;
	private static Map<String, ParamField<?>> infoRicercaMap = null;
	public static final String ANAGRAFICA_UO = "anagrafica";
	private Long idDominio = null;

	public UnitaOperativeHandler(Logger log, BaseDarsService darsService) {
		super(log,darsService);
	}

	@Override
	public Elenco getElenco(UriInfo uriInfo,BasicBD bd) throws WebApplicationException,ConsoleException {
		String methodName = "getElenco " + this.titoloServizio;
		try{	
			// Operazione consentita solo all'amministratore
			this.darsService.checkOperatoreAdmin(bd);

			Integer offset = this.getOffset(uriInfo);
			//			Integer limit = this.getLimit(uriInfo);
			URI esportazione = null;
			URI cancellazione = null;

			log.info("Esecuzione " + methodName + " in corso..."); 

			UnitaOperativeBD unitaOperativaBD = new UnitaOperativeBD(bd);
			UnitaOperativaFilter filter = unitaOperativaBD.newFilter();
			filter.setOffset(offset);
			//			filter.setLimit(limit);
			FilterSortWrapper fsw = new FilterSortWrapper();
			fsw.setField(it.govpay.orm.Uo.model().COD_UO);
			fsw.setSortOrder(SortOrder.ASC);
			filter.getFilterSortList().add(fsw);

			boolean entitaSenzaUo = false;
			boolean visualizzaRicerca = true;

			// tutte le unita' con codice uo = 'EC' sono nascoste
			filter.setExcludeEC(true); 

			// Ricerca dal dettaglio dominio

			Domini dominiDars = new Domini();
			String codDominioId = Utils.getInstance().getMessageFromResourceBundle(dominiDars.getNomeServizio() + ".codDominio.id");
			String codDominio = this.getParameter(uriInfo, codDominioId, String.class);

			if(StringUtils.isNotEmpty(codDominio)){
				DominiBD dominiBD = new DominiBD(bd);
				Dominio dominio = dominiBD.getDominio(codDominio);
				filter.setDominioFilter(dominio.getId()); 
				visualizzaRicerca = false;
			}

			// Ricerca dal dettaglio Operatore con ruolo USER

			Operatori operatoriDars = new Operatori();
			String principalId = Utils.getInstance().getMessageFromResourceBundle(operatoriDars.getNomeServizio() + ".principal.id");
			String principalOperatore = this.getParameter(uriInfo, principalId, String.class); 

			if(StringUtils.isNotEmpty(principalOperatore)){
				OperatoriBD operatoriBD = new OperatoriBD(bd);
				Operatore operatore = operatoriBD.getOperatore(principalOperatore);
				entitaSenzaUo = Utils.isEmpty(operatore.getIdEnti());
				filter.setListaIdUo(operatore.getIdEnti());
				visualizzaRicerca = false;
			} 

			// Ricerca nella sezione UO

			String codUoId = Utils.getInstance().getMessageFromResourceBundle(this.nomeServizio + ".codUo.id");
			String codUo = this.getParameter(uriInfo, codUoId, String.class);

			if(StringUtils.isNotEmpty(codUo)){
				filter.setCodUo(codUo);
			}

			String idDominioId = Utils.getInstance().getMessageFromResourceBundle(this.nomeServizio + ".idDominio.id");
			String idDominioString = this.getParameter(uriInfo, idDominioId, String.class);
			if(StringUtils.isNotEmpty(idDominioString)){
				this.idDominio = null;
				try{
					this.idDominio = Long.parseLong(idDominioString);
				}catch(Exception e){ this.idDominio = null;	}
				if(this.idDominio != null && this.idDominio > 0){
					filter.setDominioFilter(this.idDominio); 
				}
			}

			long count = entitaSenzaUo ? 0 : unitaOperativaBD.count(filter);

			InfoForm infoRicerca = visualizzaRicerca ? this.getInfoRicerca(uriInfo, bd) : null;

			Elenco elenco = new Elenco(this.titoloServizio, infoRicerca,
					this.getInfoCreazione(uriInfo, bd),
					count, esportazione, cancellazione); 

			UriBuilder uriDettaglioBuilder = uriInfo.getBaseUriBuilder().path(this.pathServizio).path("{id}");

			List<UnitaOperativa> findAll = entitaSenzaUo ? new ArrayList<UnitaOperativa>() : unitaOperativaBD.findAll(filter);

			if(findAll != null && findAll.size() > 0){
				for (UnitaOperativa entry : findAll) {
					elenco.getElenco().add(this.getElemento(entry, entry.getId(), uriDettaglioBuilder,bd));
				}
			}

			log.info("Esecuzione " + methodName + " completata.");

			return elenco;
		}catch(WebApplicationException e){
			throw e;
		}catch(Exception e){
			throw new ConsoleException(e);
		}
	}

	@SuppressWarnings("unchecked")
	@Override
	public InfoForm getInfoRicerca(UriInfo uriInfo, BasicBD bd) throws ConsoleException {
		URI ricerca = this.getUriRicerca(uriInfo, bd);
		InfoForm infoRicerca = new InfoForm(ricerca);

		String codUoId = Utils.getInstance().getMessageFromResourceBundle(this.nomeServizio + ".codUo.id");
		String idDominioId = Utils.getInstance().getMessageFromResourceBundle(this.nomeServizio + ".idDominio.id");

		if(infoRicercaMap == null){
			initInfoRicerca(uriInfo, bd);

		}


		Sezione sezioneRoot = infoRicerca.getSezioneRoot();

		InputText codUnitaOperativa = (InputText) infoRicercaMap.get(codUoId);
		codUnitaOperativa.setDefaultValue(null);
		sezioneRoot.addField(codUnitaOperativa);

		// idDominio
		List<Voce<Long>> domini = new ArrayList<Voce<Long>>();

		DominiBD dominiBD = new DominiBD(bd);
		DominioFilter filter;
		try {
			filter = dominiBD.newFilter();
			FilterSortWrapper fsw = new FilterSortWrapper();
			fsw.setField(it.govpay.orm.Dominio.model().COD_DOMINIO);
			fsw.setSortOrder(SortOrder.ASC);
			filter.getFilterSortList().add(fsw);
			List<Dominio> findAll = dominiBD.findAll(filter );

			domini.add(new Voce<Long>(Utils.getInstance().getMessageFromResourceBundle("commons.label.qualsiasi"), -1L));
			if(findAll != null && findAll.size() > 0){
				for (Dominio dominio : findAll) {
					domini.add(new Voce<Long>(dominio.getCodDominio(), dominio.getId()));  
				}
			}
		} catch (ServiceException e) {
			throw new ConsoleException(e);
		}
		SelectList<Long> idDominio = (SelectList<Long>) infoRicercaMap.get(idDominioId);
		idDominio.setDefaultValue(-1L);
		idDominio.setValues(domini); 
		sezioneRoot.addField(idDominio);

		return infoRicerca;
	}

	private void initInfoRicerca(UriInfo uriInfo, BasicBD bd) throws ConsoleException{
		if(infoRicercaMap == null){
			infoRicercaMap = new HashMap<String, ParamField<?>>();

			String codUoId = Utils.getInstance().getMessageFromResourceBundle(this.nomeServizio + ".codUo.id");
			String idDominioId = Utils.getInstance().getMessageFromResourceBundle(this.nomeServizio + ".idDominio.id");

			// codUO
			String codUnitaOperativaLabel = Utils.getInstance().getMessageFromResourceBundle(this.nomeServizio + ".codUo.label");
			InputText codUnitaOperativa = new InputText(codUoId, codUnitaOperativaLabel, null, false, false, true, 1, 255);
			infoRicercaMap.put(codUoId, codUnitaOperativa);

			List<Voce<Long>> domini = new ArrayList<Voce<Long>>();
			// idDominio
			String idDominioLabel = Utils.getInstance().getMessageFromResourceBundle(this.nomeServizio + ".idDominio.label");
			SelectList<Long> idDominio = new SelectList<Long>(idDominioId, idDominioLabel, null, false, false, true, domini);
			idDominio.setSuggestion(Utils.getInstance().getMessageFromResourceBundle(this.nomeServizio + ".idDominio.suggestion"));
			infoRicercaMap.put(idDominioId, idDominio);

		}
	}

	@SuppressWarnings("unchecked")
	@Override
	public InfoForm getInfoCreazione(UriInfo uriInfo, BasicBD bd) throws ConsoleException {
		URI creazione = this.getUriCreazione(uriInfo, bd);
		InfoForm infoCreazione = new InfoForm(creazione);

		String codUoId = Utils.getInstance().getMessageFromResourceBundle(this.nomeServizio + ".codUo.id");
		String idDominioId = Utils.getInstance().getMessageFromResourceBundle(this.nomeServizio + ".idDominio.id");
		String abilitatoId = Utils.getInstance().getMessageFromResourceBundle(this.nomeServizio + ".abilitato.id");
		String unitaOperativaId = Utils.getInstance().getMessageFromResourceBundle(this.nomeServizio + ".id.id");

		AnagraficaHandler anagraficaHandler = new AnagraficaHandler(ANAGRAFICA_UO,this.nomeServizio,this.pathServizio);
		List<ParamField<?>> infoCreazioneAnagrafica = anagraficaHandler.getInfoCreazioneAnagraficaUO(uriInfo, bd);

		if(infoCreazioneMap == null){
			initInfoCreazione(uriInfo, bd);

		}

		Sezione sezioneRoot = infoCreazione.getSezioneRoot();

		InputNumber idInterm = (InputNumber) infoCreazioneMap.get(unitaOperativaId);
		idInterm.setDefaultValue(null);
		sezioneRoot.addField(idInterm);

		InputText codUnitaOperativa = (InputText) infoCreazioneMap.get(codUoId);
		codUnitaOperativa.setDefaultValue(null);
		sezioneRoot.addField(codUnitaOperativa);

		// idDominio
		List<Voce<Long>> domini = new ArrayList<Voce<Long>>();
		SelectList<Long> idDominio = (SelectList<Long>) infoCreazioneMap.get(idDominioId);
		idDominio.setDefaultValue(this.idDominio);
		if(this.idDominio == null){
			idDominio.setHidden(true);
			DominiBD dominiBD = new DominiBD(bd);
			DominioFilter filter;
			try {
				filter = dominiBD.newFilter();
				FilterSortWrapper fsw = new FilterSortWrapper();
				fsw.setField(it.govpay.orm.Dominio.model().COD_DOMINIO);
				fsw.setSortOrder(SortOrder.ASC);
				filter.getFilterSortList().add(fsw);
				List<Dominio> findAll = dominiBD.findAll(filter );

				if(findAll != null && findAll.size() > 0){
					for (Dominio dominio : findAll) {
						domini.add(new Voce<Long>(dominio.getCodDominio(), dominio.getId()));  
					}
				}
			} catch (ServiceException e) {
				throw new ConsoleException(e);
			}
		} else {
			idDominio.setHidden(false);
		}

		idDominio.setValues(domini); 
		sezioneRoot.addField(idDominio);

		CheckButton abilitato = (CheckButton) infoCreazioneMap.get(abilitatoId);
		abilitato.setDefaultValue(true); 
		sezioneRoot.addField(abilitato);

		Sezione sezioneAnagrafica = infoCreazione.addSezione(Utils.getInstance().getMessageFromResourceBundle(this.nomeServizio + "." + ANAGRAFICA_UO + ".titolo"));

		for (ParamField<?> par : infoCreazioneAnagrafica) { 
			sezioneAnagrafica.addField(par); 	
		}

		return infoCreazione;
	}

	private void initInfoCreazione(UriInfo uriInfo, BasicBD bd) throws ConsoleException{
		if(infoCreazioneMap == null){
			infoCreazioneMap = new HashMap<String, ParamField<?>>();

			String codUoId = Utils.getInstance().getMessageFromResourceBundle(this.nomeServizio + ".codUo.id");
			String idDominioId = Utils.getInstance().getMessageFromResourceBundle(this.nomeServizio + ".idDominio.id");
			String abilitatoId = Utils.getInstance().getMessageFromResourceBundle(this.nomeServizio + ".abilitato.id");
			String unitaOperativaId = Utils.getInstance().getMessageFromResourceBundle(this.nomeServizio + ".id.id");

			// id 
			InputNumber id = new InputNumber(unitaOperativaId, null, null, true, true, false, 1, 20);
			infoCreazioneMap.put(unitaOperativaId, id);

			// codUnitaOperativa
			String codUnitaOperativaLabel = Utils.getInstance().getMessageFromResourceBundle(this.nomeServizio + ".codUo.label");
			InputText codUnitaOperativa = new InputText(codUoId, codUnitaOperativaLabel, null, true, false, true, 1, 255);
			codUnitaOperativa.setSuggestion(Utils.getInstance().getMessageFromResourceBundle(this.nomeServizio + ".codUo.suggestion"));
			codUnitaOperativa.setValidation(null, Utils.getInstance().getMessageFromResourceBundle(this.nomeServizio + ".codUo.errorMessage"));
			infoCreazioneMap.put(codUoId, codUnitaOperativa);

			// idDominio
			List<Voce<Long>> domini = new ArrayList<Voce<Long>>();
			String idDominioLabel = Utils.getInstance().getMessageFromResourceBundle(this.nomeServizio + ".idDominio.label");
			SelectList<Long> idDominio = new SelectList<Long>(idDominioId, idDominioLabel, null, true, false, true, domini);
			idDominio.setSuggestion(Utils.getInstance().getMessageFromResourceBundle(this.nomeServizio + ".idDominio.suggestion"));
			infoCreazioneMap.put(idDominioId, idDominio);

			// abilitato
			String abilitatoLabel = Utils.getInstance().getMessageFromResourceBundle(this.nomeServizio + ".abilitato.label");
			CheckButton abiliato = new CheckButton(abilitatoId, abilitatoLabel, true, false, false, true);
			infoCreazioneMap.put(abilitatoId, abiliato);

		}


	}

	@SuppressWarnings("unchecked")
	@Override
	public InfoForm getInfoModifica(UriInfo uriInfo, BasicBD bd, UnitaOperativa entry) throws ConsoleException {
		URI modifica = this.getUriModifica(uriInfo, bd);
		InfoForm infoModifica = new InfoForm(modifica);

		String codUoId = Utils.getInstance().getMessageFromResourceBundle(this.nomeServizio + ".codUo.id");
		String idDominioId = Utils.getInstance().getMessageFromResourceBundle(this.nomeServizio + ".idDominio.id");
		String abilitatoId = Utils.getInstance().getMessageFromResourceBundle(this.nomeServizio + ".abilitato.id");
		String unitaOperativaId = Utils.getInstance().getMessageFromResourceBundle(this.nomeServizio + ".id.id");


		AnagraficaHandler anagraficaHandler = new AnagraficaHandler(ANAGRAFICA_UO,this.nomeServizio,this.pathServizio);
		List<ParamField<?>> infoCreazioneAnagrafica = anagraficaHandler.getInfoModificaAnagraficaUO(uriInfo, bd,entry.getAnagrafica());

		if(infoCreazioneMap == null){
			initInfoCreazione(uriInfo, bd);
		}

		Sezione sezioneRoot = infoModifica.getSezioneRoot();
		InputNumber idInterm = (InputNumber) infoCreazioneMap.get(unitaOperativaId);
		idInterm.setDefaultValue(entry.getId());
		sezioneRoot.addField(idInterm);

		InputText codUnitaOperativa = (InputText) infoCreazioneMap.get(codUoId);
		codUnitaOperativa.setDefaultValue(entry.getCodUo());
		sezioneRoot.addField(codUnitaOperativa);

		// idDominio
		List<Voce<Long>> domini = new ArrayList<Voce<Long>>();

		DominiBD dominiBD = new DominiBD(bd);
		DominioFilter filter;
		try {
			filter = dominiBD.newFilter();
			FilterSortWrapper fsw = new FilterSortWrapper();
			fsw.setField(it.govpay.orm.Dominio.model().COD_DOMINIO);
			fsw.setSortOrder(SortOrder.ASC);
			filter.getFilterSortList().add(fsw);
			List<Dominio> findAll = dominiBD.findAll(filter );

			if(findAll != null && findAll.size() > 0){
				for (Dominio dominio : findAll) {
					domini.add(new Voce<Long>(dominio.getCodDominio(), dominio.getId()));  
				}
			}
		} catch (ServiceException e) {
			throw new ConsoleException(e);
		}


		SelectList<Long> idDominio = (SelectList<Long>) infoCreazioneMap.get(idDominioId);
		idDominio.setDefaultValue(entry.getIdDominio());
		idDominio.setValues(domini); 
		sezioneRoot.addField(idDominio);

		CheckButton abilitato = (CheckButton) infoCreazioneMap.get(abilitatoId);
		abilitato.setDefaultValue(entry.isAbilitato()); 
		sezioneRoot.addField(abilitato);


		Sezione sezioneAnagrafica = infoModifica.addSezione(Utils.getInstance().getMessageFromResourceBundle(this.nomeServizio + "." + ANAGRAFICA_UO + ".titolo"));

		for (ParamField<?> par : infoCreazioneAnagrafica) { 
			sezioneAnagrafica.addField(par); 	
		}

		return infoModifica;
	}

	@Override
	public Object getField(UriInfo uriInfo,List<RawParamValue>values, String fieldId,BasicBD bd) throws WebApplicationException,ConsoleException {
		this.log.debug("Richiesto field ["+fieldId+"]");
		try{
			// Operazione consentita solo all'amministratore
			this.darsService.checkOperatoreAdmin(bd);

			if(infoCreazioneMap.containsKey(fieldId)){
				RefreshableParamField<?> paramField = (RefreshableParamField<?>) infoCreazioneMap.get(fieldId);

				paramField.aggiornaParametro(values,bd);

				return paramField;

			}

			this.log.debug("Field ["+fieldId+"] non presente.");

		}catch(Exception e){
			throw new ConsoleException(e);
		}
		return null;
	}

	@Override
	public Dettaglio getDettaglio(long id, UriInfo uriInfo, BasicBD bd) throws WebApplicationException,ConsoleException {
		String methodName = "dettaglio " + this.titoloServizio + "."+ id;

		try{
			log.info("Esecuzione " + methodName + " in corso...");
			// Operazione consentita solo all'amministratore
			this.darsService.checkOperatoreAdmin(bd);

			// recupero oggetto
			UnitaOperativeBD unitaOperativaBD = new UnitaOperativeBD(bd);
			UnitaOperativa unitaOperativa = unitaOperativaBD.getUnitaOperativa(id);

			InfoForm infoModifica = this.getInfoModifica(uriInfo, bd,unitaOperativa);
			URI cancellazione = null;
			URI esportazione = null;

			Dettaglio dettaglio = new Dettaglio(this.getTitolo(unitaOperativa), esportazione, cancellazione, infoModifica);

			it.govpay.web.rs.dars.model.Sezione root = dettaglio.getSezioneRoot(); 

			// dati dell'unitaOperativa
			root.addVoce(Utils.getInstance().getMessageFromResourceBundle(this.nomeServizio + ".codUo.label"), unitaOperativa.getCodUo());

			DominiBD dominiBD = new DominiBD(bd);
			Dominio dominio = dominiBD.getDominio(unitaOperativa.getIdDominio());

			root.addVoce(Utils.getInstance().getMessageFromResourceBundle(this.nomeServizio + ".idDominio.label"), dominio.getCodDominio());
			root.addVoce(Utils.getInstance().getMessageFromResourceBundle(this.nomeServizio + ".abilitato.label"), Utils.getSiNoAsLabel(unitaOperativa.isAbilitato()));

			// Sezione Anagrafica

			Anagrafica anagrafica = unitaOperativa.getAnagrafica(); 
			it.govpay.web.rs.dars.model.Sezione sezioneAnagrafica = dettaglio.addSezione(Utils.getInstance().getMessageFromResourceBundle(this.nomeServizio + "." + ANAGRAFICA_UO + ".titolo"));
			AnagraficaHandler anagraficaHandler = new AnagraficaHandler(ANAGRAFICA_UO,this.nomeServizio,this.pathServizio);
			anagraficaHandler.fillSezioneAnagraficaUO(sezioneAnagrafica, anagrafica);


			log.info("Esecuzione " + methodName + " completata.");

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
			log.info("Esecuzione " + methodName + " in corso...");
			// Operazione consentita solo all'amministratore
			this.darsService.checkOperatoreAdmin(bd);

			UnitaOperativa entry = this.creaEntry(is, uriInfo, bd);

			this.checkEntry(entry, null);

			UnitaOperativeBD uoBD = new UnitaOperativeBD(bd);

			try{
				uoBD.getUnitaOperativa(entry.getIdDominio(),entry.getCodUo());
				String msg = Utils.getInstance().getMessageWithParamsFromResourceBundle(this.nomeServizio + ".oggettoEsistente", entry.getCodUo());
				throw new DuplicatedEntryException(msg);
			}catch(NotFoundException e){}

			uoBD.insertUnitaOperativa(entry); 

			log.info("Esecuzione " + methodName + " completata.");

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
			log.info("Esecuzione " + methodName + " in corso...");
			// Operazione consentita solo all'amministratore
			this.darsService.checkOperatoreAdmin(bd);

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

			entry.setAnagrafica(anagrafica);  

			log.info("Esecuzione " + methodName + " completata.");
			return entry;
		}catch(WebApplicationException e){
			throw e;
		}catch(Exception e){
			throw new ConsoleException(e);
		}
	}

	@Override
	public void checkEntry(UnitaOperativa entry, UnitaOperativa oldEntry) throws ValidationException {
		if(entry == null || entry.getCodUo() == null || entry.getCodUo().isEmpty()) throw new ValidationException("Il campo Codice Unit\u00E0 Operativa e' obbligatorio");

		Anagrafica anagrafica = entry.getAnagrafica();
		if(anagrafica != null && anagrafica.getRagioneSociale() != null && anagrafica.getRagioneSociale().length() > 255) 
			throw new ValidationException("Il campo Ragione Sociale non puo' essere piu' lungo di 255 caratteri."); 

		if(anagrafica == null || anagrafica.getCodUnivoco() == null || anagrafica.getCodUnivoco().isEmpty()) throw new ValidationException("Il campo Cod Univoco e' obbligatorio");
		if(entry.getIdDominio() == 0) throw new ValidationException("Il campo Dominio e' obbligatorio");
		if(oldEntry != null) {
			if(!entry.getCodUo().equals(oldEntry.getCodUo())) throw new ValidationException("Il campo Codice Unit\u00E0 Operativa non e' modificabile");
			if(entry.getIdDominio() != oldEntry.getIdDominio()) throw new ValidationException("Il campo Dominio non e' modificabile");
		}
	}

	@Override
	public Dettaglio update(InputStream is, UriInfo uriInfo, BasicBD bd) throws WebApplicationException, ConsoleException, ValidationException {
		String methodName = "Update " + this.titoloServizio;

		try{
			log.info("Esecuzione " + methodName + " in corso...");
			// Operazione consentita solo all'amministratore
			this.darsService.checkOperatoreAdmin(bd);

			UnitaOperativa entry = this.creaEntry(is, uriInfo, bd);

			UnitaOperativeBD uoBD = new UnitaOperativeBD(bd);
			UnitaOperativa oldEntry = uoBD.getUnitaOperativa(entry.getIdDominio(),entry.getCodUo());

			this.checkEntry(entry, oldEntry);

			uoBD.updateUnitaOperativa(entry); 

			log.info("Esecuzione " + methodName + " completata.");
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
	public void delete(List<Long> idsToDelete, UriInfo uriInfo, BasicBD bd) throws ConsoleException {
		// operazione non prevista
	}

	@Override
	public String getTitolo(UnitaOperativa entry) {
		StringBuilder sb = new StringBuilder();

		String ragioneSociale = entry.getAnagrafica().getRagioneSociale();
		if(ragioneSociale != null){
			sb.append(ragioneSociale);
			sb.append(" (").append(entry.getCodUo()).append(")");
		} else 
			sb.append(entry.getCodUo());

		return sb.toString();
	}

	public Elemento getElemento(UnitaOperativa entry, Long id, UriBuilder uriDettaglioBuilder, BasicBD bd) throws Exception{
		String titolo = this.getTitolo(entry);

		StringBuilder sb = new StringBuilder();

		sb.append(Utils.getAbilitatoAsLabel(entry.isAbilitato()));
		sb.append(", Dominio: ").append(entry.getDominio(bd).getCodDominio());

		String sottotitolo = sb.toString();
		URI urlDettaglio = id != null ?  uriDettaglioBuilder.build(id) : null;
		Elemento elemento = new Elemento(id, titolo, sottotitolo, urlDettaglio);
		return elemento;
	}

	@Override
	public String getSottotitolo(UnitaOperativa entry) {
		StringBuilder sb = new StringBuilder();

		sb.append(Utils.getAbilitatoAsLabel(entry.isAbilitato()));

		return sb.toString();
	}


}
