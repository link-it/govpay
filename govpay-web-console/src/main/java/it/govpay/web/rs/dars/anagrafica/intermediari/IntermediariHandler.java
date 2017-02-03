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
package it.govpay.web.rs.dars.anagrafica.intermediari;

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
import org.openspcoop2.generic_project.expression.SortOrder;

import it.govpay.bd.BasicBD;
import it.govpay.bd.FilterSortWrapper;
import it.govpay.bd.anagrafica.IntermediariBD;
import it.govpay.bd.anagrafica.filters.IntermediarioFilter;
import it.govpay.model.Connettore;
import it.govpay.model.Connettore.EnumSslType;
import it.govpay.model.Intermediario;
import it.govpay.web.rs.dars.BaseDarsHandler;
import it.govpay.web.rs.dars.BaseDarsService;
import it.govpay.web.rs.dars.IDarsHandler;
import it.govpay.web.rs.dars.anagrafica.connettori.ConnettoreHandler;
import it.govpay.web.rs.dars.anagrafica.stazioni.Stazioni;
import it.govpay.web.rs.dars.exception.ConsoleException;
import it.govpay.web.rs.dars.exception.DuplicatedEntryException;
import it.govpay.web.rs.dars.exception.ValidationException;
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

public class IntermediariHandler extends BaseDarsHandler<Intermediario> implements IDarsHandler<Intermediario>{

	public static final String CONNETTORE_PDD = ConnettoreHandler.CONNETTORE_PDD;
	private Map<String, ParamField<?>> infoCreazioneMap = null;
	private Map<String, ParamField<?>> infoRicercaMap = null;

	public IntermediariHandler(Logger log, BaseDarsService darsService) {
		super(log,darsService);
	}

	@Override
	public Elenco getElenco(UriInfo uriInfo,BasicBD bd) throws WebApplicationException,ConsoleException {
		String methodName = "getElenco " + this.titoloServizio;
		try{	
			// Operazione consentita solo all'amministratore
			this.darsService.checkOperatoreAdmin(bd);

			Integer offset = this.getOffset(uriInfo);
			Integer limit = this.getLimit(uriInfo);
			URI esportazione = null;
			URI cancellazione = null;

			this.log.info("Esecuzione " + methodName + " in corso..."); 

			IntermediariBD intermediariBD = new IntermediariBD(bd);
			IntermediarioFilter filter = intermediariBD.newFilter();
			filter.setOffset(offset);
			filter.setLimit(limit);
			FilterSortWrapper fsw = new FilterSortWrapper();
			fsw.setField(it.govpay.orm.Intermediario.model().COD_INTERMEDIARIO);
			fsw.setSortOrder(SortOrder.ASC);
			filter.getFilterSortList().add(fsw);


			String codIntermediarioId = Utils.getInstance(this.getLanguage()).getMessageFromResourceBundle(this.nomeServizio + ".codIntermediario.id");
			String codIntermediario = this.getParameter(uriInfo, codIntermediarioId, String.class);

			if(StringUtils.isNotEmpty(codIntermediario)){
				filter.setIdIntermediario(codIntermediario); 
			}

			long count = intermediariBD.count(filter);

			// visualizza la ricerca solo se i risultati sono > del limit
			boolean visualizzaRicerca = this.visualizzaRicerca(count, limit);
			InfoForm infoRicerca = this.getInfoRicerca(uriInfo, bd, visualizzaRicerca);

			Elenco elenco = new Elenco(this.titoloServizio, infoRicerca,
					this.getInfoCreazione(uriInfo, bd),
					count, esportazione, cancellazione); 

			//elenco.setFiltro(true);

			List<Intermediario> findAll = intermediariBD.findAll(filter);

			if(findAll != null && findAll.size() > 0){
				for (Intermediario entry : findAll) {
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
	public InfoForm getInfoRicerca(UriInfo uriInfo, BasicBD bd, boolean visualizzaRicerca, Map<String,String> parameters) throws ConsoleException {
		URI ricerca = this.getUriRicerca(uriInfo, bd);
		InfoForm infoRicerca = new InfoForm(ricerca);

		if(visualizzaRicerca){
			String codIntermediarioId = Utils.getInstance(this.getLanguage()).getMessageFromResourceBundle(this.nomeServizio + ".codIntermediario.id");

			if(this.infoRicercaMap == null){
				this.initInfoRicerca(uriInfo, bd);

			}

			Sezione sezioneRoot = infoRicerca.getSezioneRoot();

			InputText codIntermediario = (InputText) this.infoRicercaMap.get(codIntermediarioId);
			codIntermediario.setDefaultValue(null);
			codIntermediario.setEditable(true); 
			sezioneRoot.addField(codIntermediario);
		}
		return infoRicerca;
	}

	/***
	 * Form Creazione 
	 * 
	 * sez ROOT:
	 * codIntermediario
	 * denominazione
	 * 
	 * sez Connettore Pdd
	 * URL
	 * tipo autenticazione
	 * -- auth = basic
	 * username / password
	 * -- auth = ssl
	 * parametri ssl: 
	 * 
	 */

	@Override
	public InfoForm getInfoCreazione(UriInfo uriInfo, BasicBD bd) throws ConsoleException {
		URI creazione = this.getUriCreazione(uriInfo, bd);
		InfoForm infoCreazione = new InfoForm(creazione,Utils.getInstance(this.getLanguage()).getMessageFromResourceBundle(this.nomeServizio + ".creazione.titolo"));

		String codIntermediarioId = Utils.getInstance(this.getLanguage()).getMessageFromResourceBundle(this.nomeServizio + ".codIntermediario.id");
		String denominazioneId = Utils.getInstance(this.getLanguage()).getMessageFromResourceBundle(this.nomeServizio + ".denominazione.id");
		String abilitatoId = Utils.getInstance(this.getLanguage()).getMessageFromResourceBundle(this.nomeServizio + ".abilitato.id");
		String intermediarioId = Utils.getInstance(this.getLanguage()).getMessageFromResourceBundle(this.nomeServizio + ".id.id");
		String principalId = Utils.getInstance(this.getLanguage()).getMessageFromResourceBundle(this.nomeServizio + ".principal.id");
		String segregationCodeId = Utils.getInstance(this.getLanguage()).getMessageFromResourceBundle(this.nomeServizio + ".segregationCode.id");

		ConnettoreHandler connettoreHandler = new ConnettoreHandler(CONNETTORE_PDD,this.nomeServizio,this.pathServizio, this.getLanguage());
		List<ParamField<?>> infoCreazioneConnettore = connettoreHandler.getInfoCreazione(uriInfo, bd,false);

		if(this.infoCreazioneMap == null){
			this.initInfoCreazione(uriInfo, bd);
		}

		Sezione sezioneRoot = infoCreazione.getSezioneRoot();
		InputNumber idInterm = (InputNumber) this.infoCreazioneMap.get(intermediarioId);
		idInterm.setDefaultValue(null);
		sezioneRoot.addField(idInterm);
		InputText codIntermediario = (InputText) this.infoCreazioneMap.get(codIntermediarioId);
		codIntermediario.setDefaultValue(null);
		codIntermediario.setEditable(true); 
		sezioneRoot.addField(codIntermediario);

		InputText denominazione = (InputText) this.infoCreazioneMap.get(denominazioneId);
		denominazione.setDefaultValue(null);
		sezioneRoot.addField(denominazione);

		InputText principal = (InputText) this.infoCreazioneMap.get(principalId);
		principal.setDefaultValue(null);
		sezioneRoot.addField(principal);

		InputText segregationCode = (InputText) this.infoCreazioneMap.get(segregationCodeId);
		segregationCode.setDefaultValue(null); 
		sezioneRoot.addField(segregationCode);

		CheckButton abilitato = (CheckButton) this.infoCreazioneMap.get(abilitatoId);
		abilitato.setDefaultValue(true); 
		sezioneRoot.addField(abilitato);


		Sezione sezioneConnettore = infoCreazione.addSezione(Utils.getInstance(this.getLanguage()).getMessageFromResourceBundle(this.nomeServizio + "." + CONNETTORE_PDD + ".titolo"));

		for (ParamField<?> par : infoCreazioneConnettore) { 
			sezioneConnettore.addField(par); 	
		}

		return infoCreazione;
	}

	private void initInfoRicerca(UriInfo uriInfo, BasicBD bd) throws ConsoleException{
		if(this.infoRicercaMap == null){
			this.infoRicercaMap = new HashMap<String, ParamField<?>>();

			String codIntermediarioId = Utils.getInstance(this.getLanguage()).getMessageFromResourceBundle(this.nomeServizio + ".codIntermediario.id");
			// codIntermediario
			String codIntermediarioLabel = Utils.getInstance(this.getLanguage()).getMessageFromResourceBundle(this.nomeServizio + ".codIntermediario.label");
			InputText codIntermediario = new InputText(codIntermediarioId, codIntermediarioLabel, null, false, false, true, 11, 11);
			this.infoRicercaMap.put(codIntermediarioId, codIntermediario);
		}
	}

	private void initInfoCreazione(UriInfo uriInfo, BasicBD bd) throws ConsoleException{
		if(this.infoCreazioneMap == null){
			this.infoCreazioneMap = new HashMap<String, ParamField<?>>();

			// id 
			String intermediarioId = Utils.getInstance(this.getLanguage()).getMessageFromResourceBundle(this.nomeServizio + ".id.id");
			InputNumber id = new InputNumber(intermediarioId, null, null, true, true, false, 1, 20);
			this.infoCreazioneMap.put(intermediarioId, id);

			String codIntermediarioId = Utils.getInstance(this.getLanguage()).getMessageFromResourceBundle(this.nomeServizio + ".codIntermediario.id");
			String denominazioneId = Utils.getInstance(this.getLanguage()).getMessageFromResourceBundle(this.nomeServizio + ".denominazione.id");
			String abilitatoId = Utils.getInstance(this.getLanguage()).getMessageFromResourceBundle(this.nomeServizio + ".abilitato.id");
			String principalId = Utils.getInstance(this.getLanguage()).getMessageFromResourceBundle(this.nomeServizio + ".principal.id");
			String segregationCodeId = Utils.getInstance(this.getLanguage()).getMessageFromResourceBundle(this.nomeServizio + ".segregationCode.id");

			// codIntermediario
			String codIntermediarioLabel = Utils.getInstance(this.getLanguage()).getMessageFromResourceBundle(this.nomeServizio + ".codIntermediario.label");
			InputText codIntermediario = new InputText(codIntermediarioId, codIntermediarioLabel, null, true, false, true, 11, 11);
			codIntermediario.setSuggestion(Utils.getInstance(this.getLanguage()).getMessageFromResourceBundle(this.nomeServizio + ".codIntermediario.suggestion"));
			codIntermediario.setValidation("[0-9]{11}", Utils.getInstance(this.getLanguage()).getMessageFromResourceBundle(this.nomeServizio + ".codIntermediario.errorMessage"));
			this.infoCreazioneMap.put(codIntermediarioId, codIntermediario);

			// principal
			String principalLabel = Utils.getInstance(this.getLanguage()).getMessageFromResourceBundle(this.nomeServizio + ".principal.label");
			InputText principal = new InputText(principalId, principalLabel, null, true, false, true, 1, 255);
			principal.setValidation(null, Utils.getInstance(this.getLanguage()).getMessageFromResourceBundle(this.nomeServizio + ".principal.errorMessage"));
			this.infoCreazioneMap.put(principalId, principal);

			// denominazione
			String denominazioneLabel = Utils.getInstance(this.getLanguage()).getMessageFromResourceBundle(this.nomeServizio + ".denominazione.label");
			InputText denominazione = new InputText(denominazioneId, denominazioneLabel, null, true, false, true, 1, 50);
			denominazione.setValidation(null, Utils.getInstance(this.getLanguage()).getMessageFromResourceBundle(this.nomeServizio + ".denominazione.errorMessage"));
			this.infoCreazioneMap.put(denominazioneId, denominazione);

			// abilitato
			String abilitatoLabel = Utils.getInstance(this.getLanguage()).getMessageFromResourceBundle(this.nomeServizio + ".abilitato.label");
			CheckButton abiliato = new CheckButton(abilitatoId, abilitatoLabel, true, false, false, true);
			this.infoCreazioneMap.put(abilitatoId, abiliato);

			// segregationCode
			String segregationCodeLabel = Utils.getInstance(this.getLanguage()).getMessageFromResourceBundle(this.nomeServizio + ".segregationCode.label");
			InputText segregationCode = new InputText(segregationCodeId, segregationCodeLabel, null, false, false, true, 2, 2);
			//segregationCode.setSuggestion(Utils.getInstance(this.getLanguage()).getMessageFromResourceBundle(this.nomeServizio + ".segregationCode.suggestion"));
			segregationCode.setValidation("[0-9]{2}", Utils.getInstance(this.getLanguage()).getMessageFromResourceBundle(this.nomeServizio + ".segregationCode.errorMessage"));
			segregationCode.setAvanzata(true); 
			this.infoCreazioneMap.put(segregationCodeId, segregationCode);

			ConnettoreHandler connettoreHandler = new ConnettoreHandler(CONNETTORE_PDD,this.nomeServizio,this.pathServizio, this.getLanguage());
			List<ParamField<?>> infoCreazioneConnettore = connettoreHandler.getInfoCreazione(uriInfo, bd,false);

			for (ParamField<?> par : infoCreazioneConnettore) { 
				this.infoCreazioneMap.put(par.getId(),par); 	
			}
		}
	}

	@Override
	public InfoForm getInfoModifica(UriInfo uriInfo, BasicBD bd, Intermediario entry) throws ConsoleException {
		URI modifica = this.getUriModifica(uriInfo, bd);
		InfoForm infoModifica = new InfoForm(modifica,Utils.getInstance(this.getLanguage()).getMessageFromResourceBundle(this.nomeServizio + ".modifica.titolo"));

		String codIntermediarioId = Utils.getInstance(this.getLanguage()).getMessageFromResourceBundle(this.nomeServizio + ".codIntermediario.id");
		String denominazioneId = Utils.getInstance(this.getLanguage()).getMessageFromResourceBundle(this.nomeServizio + ".denominazione.id");
		String abilitatoId = Utils.getInstance(this.getLanguage()).getMessageFromResourceBundle(this.nomeServizio + ".abilitato.id");
		String intermediarioId = Utils.getInstance(this.getLanguage()).getMessageFromResourceBundle(this.nomeServizio + ".id.id");
		String principalId = Utils.getInstance(this.getLanguage()).getMessageFromResourceBundle(this.nomeServizio + ".principal.id");
		String segregationCodeId = Utils.getInstance(this.getLanguage()).getMessageFromResourceBundle(this.nomeServizio + ".segregationCode.id");

		ConnettoreHandler connettoreHandler = new ConnettoreHandler(CONNETTORE_PDD,this.nomeServizio,this.pathServizio, this.getLanguage());
		List<ParamField<?>> infoModificaConnettore = connettoreHandler.getInfoModifica(uriInfo, bd, entry.getConnettorePdd(),entry.getId(),false);

		if(this.infoCreazioneMap == null){
			this.initInfoCreazione(uriInfo, bd);
		}

		Sezione sezioneRoot = infoModifica.getSezioneRoot();
		InputNumber idInterm = (InputNumber) this.infoCreazioneMap.get(intermediarioId);
		idInterm.setDefaultValue(entry.getId());
		sezioneRoot.addField(idInterm);
		InputText codIntermediario = (InputText) this.infoCreazioneMap.get(codIntermediarioId);
		codIntermediario.setDefaultValue(entry.getCodIntermediario());
		codIntermediario.setEditable(false); 
		sezioneRoot.addField(codIntermediario);

		InputText denominazione = (InputText) this.infoCreazioneMap.get(denominazioneId);
		denominazione.setDefaultValue(entry.getDenominazione());
		sezioneRoot.addField(denominazione);

		InputText principal = (InputText) this.infoCreazioneMap.get(principalId);
		principal.setDefaultValue(entry.getConnettorePdd() == null ? null : entry.getConnettorePdd().getPrincipal());
		sezioneRoot.addField(principal);

		InputText segregationCode = (InputText) this.infoCreazioneMap.get(segregationCodeId);
		String segCode = entry.getSegregationCode()!= null ? ( entry.getSegregationCode() < 10 ? "0"+entry.getSegregationCode() : entry.getSegregationCode()+"" ): null;
		segregationCode.setDefaultValue(segCode); 
		sezioneRoot.addField(segregationCode);

		CheckButton abilitato = (CheckButton) this.infoCreazioneMap.get(abilitatoId);
		abilitato.setDefaultValue(entry.isAbilitato()); 
		sezioneRoot.addField(abilitato);

		Sezione sezioneConnettore = infoModifica.addSezione(Utils.getInstance(this.getLanguage()).getMessageFromResourceBundle(this.nomeServizio + "." + CONNETTORE_PDD + ".titolo"));

		for (ParamField<?> par : infoModificaConnettore) { 
			sezioneConnettore.addField(par); 	
		}

		return infoModifica;
	}

	@Override
	public Object getField(UriInfo uriInfo,List<RawParamValue>values, String fieldId,BasicBD bd) throws WebApplicationException,ConsoleException {
		this.log.debug("Richiesto field ["+fieldId+"]");
		try{
			// Operazione consentita solo all'amministratore
			this.darsService.checkOperatoreAdmin(bd);

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
	public Dettaglio getDettaglio(long id, UriInfo uriInfo, BasicBD bd) throws WebApplicationException,ConsoleException {
		String methodName = "dettaglio " + this.titoloServizio + ".Id"+ id;

		try{
			this.log.info("Esecuzione " + methodName + " in corso...");
			// Operazione consentita solo all'amministratore
			this.darsService.checkOperatoreAdmin(bd);

			// recupero oggetto
			IntermediariBD intermediariBD = new IntermediariBD(bd);
			Intermediario intermediario = intermediariBD.getIntermediario(id);

			InfoForm infoModifica = this.getInfoModifica(uriInfo, bd,intermediario);
			URI cancellazione = null;
			URI esportazione = null;

			Dettaglio dettaglio = new Dettaglio(this.getTitolo(intermediario,bd), esportazione, cancellazione, infoModifica);

			it.govpay.web.rs.dars.model.Sezione root = dettaglio.getSezioneRoot(); 

			Connettore connettore = intermediario.getConnettorePdd();

			// dati dell'intermediario
			root.addVoce(Utils.getInstance(this.getLanguage()).getMessageFromResourceBundle(this.nomeServizio + ".codIntermediario.label"), intermediario.getCodIntermediario());
			root.addVoce(Utils.getInstance(this.getLanguage()).getMessageFromResourceBundle(this.nomeServizio + ".denominazione.label"), intermediario.getDenominazione());
			if(connettore != null && StringUtils.isNotEmpty(connettore.getPrincipal())) {
				root.addVoce(Utils.getInstance(this.getLanguage()).getMessageFromResourceBundle(this.nomeServizio + ".principal.label"), connettore.getPrincipal());
			}
			if(intermediario.getSegregationCode() != null){
				String segCode = intermediario.getSegregationCode() < 10 ? "0"+intermediario.getSegregationCode() : intermediario.getSegregationCode()+"";
				root.addVoce(Utils.getInstance(this.getLanguage()).getMessageFromResourceBundle(this.nomeServizio + ".segregationCode.label"), segCode);
			}
			root.addVoce(Utils.getInstance(this.getLanguage()).getMessageFromResourceBundle(this.nomeServizio + ".abilitato.label"), Utils.getSiNoAsLabel(intermediario.isAbilitato()));

			// sezione connettore
			it.govpay.web.rs.dars.model.Sezione sezioneConnettore = dettaglio.addSezione(Utils.getInstance(this.getLanguage()).getMessageFromResourceBundle(this.nomeServizio + "." + CONNETTORE_PDD + ".titolo"));
			ConnettoreHandler connettoreHandler = new ConnettoreHandler(CONNETTORE_PDD,this.nomeServizio,this.pathServizio, this.getLanguage());
			connettoreHandler.fillSezione(sezioneConnettore, connettore,false);

			// Elementi correlati
			String etichettaStazioni = Utils.getInstance(this.getLanguage()).getMessageFromResourceBundle(this.nomeServizio + ".elementoCorrelato.stazioni.titolo");
			String codIntermediarioId = Utils.getInstance(this.getLanguage()).getMessageFromResourceBundle(this.nomeServizio + ".codIntermediario.id");

			Stazioni stazioniDars = new Stazioni();
			
			Map<String, String> params = new HashMap<String, String>();
			params.put(codIntermediarioId, intermediario.getCodIntermediario());
			URI stazioneDettaglio = Utils.creaUriConParametri(stazioniDars.getPathServizio(), params );
			dettaglio.addElementoCorrelato(etichettaStazioni, stazioneDettaglio);

			this.log.info("Esecuzione " + methodName + " completata.");

			return dettaglio;
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
	public Dettaglio insert(InputStream is, UriInfo uriInfo, BasicBD bd) throws WebApplicationException, ConsoleException, ValidationException, DuplicatedEntryException{
		String methodName = "Insert " + this.titoloServizio;

		try{
			this.log.info("Esecuzione " + methodName + " in corso...");
			// Operazione consentita solo all'amministratore
			this.darsService.checkOperatoreAdmin(bd);

			Intermediario entry = this.creaEntry(is, uriInfo, bd);

			this.checkEntry(entry, null);

			IntermediariBD intermediariBD = new IntermediariBD(bd);

			try{
				intermediariBD.getIntermediario(entry.getCodIntermediario());
				String msg = Utils.getInstance(this.getLanguage()).getMessageWithParamsFromResourceBundle(this.nomeServizio + ".oggettoEsistente", entry.getCodIntermediario());
				throw new DuplicatedEntryException(msg);
			}catch(NotFoundException e){}

			intermediariBD.insertIntermediario(entry); 

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
	public Intermediario creaEntry(InputStream is, UriInfo uriInfo, BasicBD bd)	throws WebApplicationException, ConsoleException {
		String methodName = "creaEntry " + this.titoloServizio;
		Intermediario entry = null;
		String principalId = Utils.getInstance(this.getLanguage()).getMessageFromResourceBundle(this.nomeServizio + ".principal.id");
		String tipoSslId = Utils.getInstance(this.getLanguage()).getMessageFromResourceBundle(this.nomeServizio + "." + CONNETTORE_PDD + ".tipoSsl.id");
		String segregationCodeId = Utils.getInstance(this.getLanguage()).getMessageFromResourceBundle(this.nomeServizio + ".segregationCode.id");
		try{
			this.log.info("Esecuzione " + methodName + " in corso...");
			// Operazione consentita solo all'amministratore
			this.darsService.checkOperatoreAdmin(bd);

			JsonConfig jsonConfig = new JsonConfig();
			ByteArrayOutputStream baos = new ByteArrayOutputStream();
			Utils.copy(is, baos);

			baos.flush();
			baos.close();

			JSONObject jsonObjectIntermediario = JSONObject.fromObject( baos.toString() );  

			String principal = jsonObjectIntermediario.getString(principalId);
			jsonObjectIntermediario.remove(principalId);

			String segregationCode = jsonObjectIntermediario.getString(segregationCodeId);
			jsonObjectIntermediario.remove(segregationCodeId);

			jsonConfig.setRootClass(Intermediario.class);
			entry = (Intermediario) JSONObject.toBean( jsonObjectIntermediario, jsonConfig );

			String tipoSsl = jsonObjectIntermediario.containsKey(tipoSslId) ? jsonObjectIntermediario.getString(tipoSslId) : null;
			if(tipoSsl != null) {
				jsonObjectIntermediario.remove(tipoSslId);
			}

			//jsonObjectIntermediario = JSONObject.fromObject( baos.toString() );  
			jsonConfig.setRootClass(Connettore.class);
			Connettore c = (Connettore) JSONObject.toBean( jsonObjectIntermediario, jsonConfig );

			if(StringUtils.isNotEmpty(tipoSsl)){
				c.setTipoSsl(EnumSslType.valueOf(tipoSsl)); 
			}

			c.setPrincipal(principal);
			entry.setConnettorePdd(c); 

			if(StringUtils.isNotEmpty(segregationCode)){
				entry.setSegregationCode(Integer.parseInt(segregationCode)); 
			}

			this.log.info("Esecuzione " + methodName + " completata.");
			return entry;
		}catch(WebApplicationException e){
			throw e;
		}catch(Exception e){
			throw new ConsoleException(e);
		}
	}


	@Override
	public void checkEntry(Intermediario entry, Intermediario oldEntry) throws ValidationException {
		if(entry == null || entry.getCodIntermediario() == null || entry.getCodIntermediario().length() != 11) {
			int codIntSize = (entry != null && entry.getCodIntermediario() != null) ? entry.getCodIntermediario().length() : 0;
			throw new ValidationException("Lunghezza del IdIntermediario errata. Richieste 11 cifre, trovate "+codIntSize);
		}
		try { 
			Long.parseLong(entry.getCodIntermediario());
		} catch (NumberFormatException e) {
			throw new ValidationException("Formato CodIntermediario errato. Richieste 11 cifre, trovato "+entry.getCodIntermediario());
		}

		if(entry.getDenominazione() == null || entry.getDenominazione().isEmpty()) {
			throw new ValidationException("Il campo Denominazione deve essere valorizzato.");
		}

		Connettore connettore = entry.getConnettorePdd();

		if(connettore.getPrincipal() == null || connettore.getPrincipal().isEmpty()) {
			throw new ValidationException("Il campo Principal deve essere valorizzato.");
		}

		ConnettoreHandler connettoreHandler = new ConnettoreHandler(CONNETTORE_PDD, this.titoloServizio, this.pathServizio, this.getLanguage());
		connettoreHandler.valida(connettore,false); 

		if(oldEntry != null) { //caso update
			if(!oldEntry.getCodIntermediario().equals(entry.getCodIntermediario())) {
				throw new ValidationException("CodIntermediario non deve cambiare in update. Atteso ["+oldEntry.getCodIntermediario()+"] trovato ["+entry.getCodIntermediario()+"]");
			}
		}
	}

	@Override
	public Dettaglio update(InputStream is, UriInfo uriInfo, BasicBD bd)
			throws WebApplicationException, ConsoleException, ValidationException {
		String methodName = "Update " + this.titoloServizio;

		try{
			this.log.info("Esecuzione " + methodName + " in corso...");
			// Operazione consentita solo all'amministratore
			this.darsService.checkOperatoreAdmin(bd);

			Intermediario entry = this.creaEntry(is, uriInfo, bd);

			IntermediariBD intermediariBD = new IntermediariBD(bd);
			Intermediario oldEntry = intermediariBD.getIntermediario(entry.getCodIntermediario());

			this.checkEntry(entry, oldEntry);

			intermediariBD.updateIntermediario(entry); 

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
	public String getTitolo(Intermediario entry, BasicBD bd) {
		StringBuilder sb = new StringBuilder();

		sb.append(entry.getDenominazione());
		sb.append(" (").append(entry.getCodIntermediario()).append(")");
		return sb.toString();
	}

	@Override
	public String getSottotitolo(Intermediario entry, BasicBD bd) {
		StringBuilder sb = new StringBuilder();

		sb.append(Utils.getAbilitatoAsLabel(entry.isAbilitato()));

		return sb.toString();
	}

	@Override
	public List<String> getValori(Intermediario entry, BasicBD bd) throws ConsoleException {
		return null;
	}
	
	@Override
	public Map<String, Voce<String>> getVoci(Intermediario entry, BasicBD bd) throws ConsoleException { return null; }

	@Override
	public String esporta(List<Long> idsToExport, UriInfo uriInfo, BasicBD bd, ZipOutputStream zout)
			throws WebApplicationException, ConsoleException {
		return null;
	}

	@Override
	public String esporta(Long idToExport, UriInfo uriInfo, BasicBD bd, ZipOutputStream zout)	throws WebApplicationException, ConsoleException {
		return null;
	}

	@Override
	public Object uplaod(MultipartFormDataInput input, UriInfo uriInfo, BasicBD bd)	throws WebApplicationException, ConsoleException, ValidationException { return null;}
}
