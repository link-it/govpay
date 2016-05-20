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
package it.govpay.web.rs.dars.anagrafica.portali;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.net.URI;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.zip.ZipOutputStream;

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
import it.govpay.bd.anagrafica.ApplicazioniBD;
import it.govpay.bd.anagrafica.PortaliBD;
import it.govpay.bd.anagrafica.filters.ApplicazioneFilter;
import it.govpay.bd.anagrafica.filters.PortaleFilter;
import it.govpay.bd.model.Applicazione;
import it.govpay.bd.model.Portale;
import it.govpay.web.rs.BaseRsService;
import it.govpay.web.rs.dars.BaseDarsHandler;
import it.govpay.web.rs.dars.BaseDarsService;
import it.govpay.web.rs.dars.IDarsHandler;
import it.govpay.web.rs.dars.anagrafica.applicazioni.ApplicazioniHandler;
import it.govpay.web.rs.dars.anagrafica.portali.input.Applicazioni;
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
import it.govpay.web.utils.Utils;
import net.sf.json.JSONObject;
import net.sf.json.JsonConfig;

public class PortaliHandler extends BaseDarsHandler<Portale> implements IDarsHandler<Portale>{

	private static Map<String, ParamField<?>> infoCreazioneMap = null;
	private static Map<String, ParamField<?>> infoRicercaMap = null;

	public PortaliHandler(Logger log, BaseDarsService darsService) {
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

			PortaliBD applicazioniBD = new PortaliBD(bd);
			PortaleFilter filter = applicazioniBD.newFilter();
			filter.setOffset(offset);
			filter.setLimit(limit);
			FilterSortWrapper fsw = new FilterSortWrapper();
			fsw.setField(it.govpay.orm.Portale.model().COD_PORTALE);
			fsw.setSortOrder(SortOrder.ASC);
			filter.getFilterSortList().add(fsw);

			String codPortaleId = Utils.getInstance().getMessageFromResourceBundle(this.nomeServizio + ".codPortale.id");
			String codPortale = this.getParameter(uriInfo, codPortaleId, String.class);

			if(StringUtils.isNotEmpty(codPortale)){
				filter.setCodPortale(codPortale);
			}

			long count = applicazioniBD.count(filter);
			
			// visualizza la ricerca solo se i risultati sono > del limit
			boolean visualizzaRicerca = this.visualizzaRicerca(count, limit);
			InfoForm infoRicerca = visualizzaRicerca ? this.getInfoRicerca(uriInfo, bd) : null;

			Elenco elenco = new Elenco(this.titoloServizio, infoRicerca,
					this.getInfoCreazione(uriInfo, bd),
					count, esportazione, cancellazione); 

			UriBuilder uriDettaglioBuilder = BaseRsService.checkDarsURI(uriInfo).path(this.pathServizio).path("{id}");

			List<Portale> findAll = applicazioniBD.findAll(filter);

			if(findAll != null && findAll.size() > 0){
				for (Portale entry : findAll) {
					elenco.getElenco().add(this.getElemento(entry, entry.getId(), uriDettaglioBuilder,bd));
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
	public InfoForm getInfoRicerca(UriInfo uriInfo, BasicBD bd) throws ConsoleException {
		URI ricerca = this.getUriRicerca(uriInfo, bd);
		InfoForm infoRicerca = new InfoForm(ricerca);

		String codPortaleId = Utils.getInstance().getMessageFromResourceBundle(this.nomeServizio + ".codPortale.id");

		if(infoRicercaMap == null){
			this.initInfoRicerca(uriInfo, bd);

		}
		Sezione sezioneRoot = infoRicerca.getSezioneRoot();

		InputText codPortale= (InputText) infoRicercaMap.get(codPortaleId);
		codPortale.setDefaultValue(null);
		codPortale.setEditable(true); 
		sezioneRoot.addField(codPortale);

		return infoRicerca;
	}

	private void initInfoRicerca(UriInfo uriInfo, BasicBD bd) throws ConsoleException{
		if(infoRicercaMap == null){
			infoRicercaMap = new HashMap<String, ParamField<?>>();

			String codPortaleId = Utils.getInstance().getMessageFromResourceBundle(this.nomeServizio + ".codPortale.id");
			String codPortaleLabel = Utils.getInstance().getMessageFromResourceBundle(this.nomeServizio + ".codPortale.label");
			InputText codPortale = new InputText(codPortaleId, codPortaleLabel, null, false, false, true, 1, 255);
			infoRicercaMap.put(codPortaleId, codPortale);
		}
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
		InfoForm infoCreazione = new InfoForm(creazione);

		String codPortaleId = Utils.getInstance().getMessageFromResourceBundle(this.nomeServizio + ".codPortale.id");
		String principalId = Utils.getInstance().getMessageFromResourceBundle(this.nomeServizio + ".principal.id");
		String abilitatoId = Utils.getInstance().getMessageFromResourceBundle(this.nomeServizio + ".abilitato.id");
		String portaleId = Utils.getInstance().getMessageFromResourceBundle(this.nomeServizio + ".id.id");
		String defaultCallbackURLId = Utils.getInstance().getMessageFromResourceBundle(this.nomeServizio + ".defaultCallbackURL.id");
		String applicazioniId = Utils.getInstance().getMessageFromResourceBundle(this.nomeServizio + ".idApplicazioni.id");

		if(infoCreazioneMap == null){
			this.initInfoCreazione(uriInfo, bd);
		}

		Sezione sezioneRoot = infoCreazione.getSezioneRoot();
		InputNumber idInterm = (InputNumber) infoCreazioneMap.get(portaleId);
		idInterm.setDefaultValue(null);
		sezioneRoot.addField(idInterm);

		InputText codPortale = (InputText) infoCreazioneMap.get(codPortaleId);
		codPortale.setDefaultValue(null);
		codPortale.setEditable(true); 
		sezioneRoot.addField(codPortale);

		InputText principal = (InputText) infoCreazioneMap.get(principalId);
		principal.setDefaultValue(null);
		sezioneRoot.addField(principal);

		InputText defaultCallbackURL = (InputText) infoCreazioneMap.get(defaultCallbackURLId);
		defaultCallbackURL.setDefaultValue(null);
		sezioneRoot.addField(defaultCallbackURL);

		Applicazioni applicazioni = (Applicazioni) infoCreazioneMap.get(applicazioniId);
		ApplicazioniBD applicazioniBD = new ApplicazioniBD(bd);
		List<Voce<Long>> idApplicazioni= new ArrayList<Voce<Long>>();

		try {
			ApplicazioneFilter filter = applicazioniBD.newFilter();
			FilterSortWrapper fsw = new FilterSortWrapper();
			fsw.setField(it.govpay.orm.Applicazione.model().COD_APPLICAZIONE);
			fsw.setSortOrder(SortOrder.ASC);
			filter.getFilterSortList().add(fsw);
			List<Applicazione> applicazioniList = applicazioniBD.findAll(filter);
			if(applicazioniList != null && applicazioniList.size() > 0){
				for (Applicazione tributo : applicazioniList) {
					idApplicazioni.add(new Voce<Long>(tributo.getCodApplicazione(), tributo.getId())); 
				}
			}

		} catch (ServiceException e) {
			throw new ConsoleException(e);
		}

		applicazioni.setValues(idApplicazioni); 
		applicazioni.setDefaultValue(new ArrayList<Long>());
		sezioneRoot.addField(applicazioni);

		CheckButton abilitato = (CheckButton) infoCreazioneMap.get(abilitatoId);
		abilitato.setDefaultValue(true); 
		sezioneRoot.addField(abilitato);

		return infoCreazione;
	}

	private void initInfoCreazione(UriInfo uriInfo, BasicBD bd) throws ConsoleException{
		if(infoCreazioneMap == null){
			infoCreazioneMap = new HashMap<String, ParamField<?>>();

			String codPortaleId = Utils.getInstance().getMessageFromResourceBundle(this.nomeServizio + ".codPortale.id");
			String principalId = Utils.getInstance().getMessageFromResourceBundle(this.nomeServizio + ".principal.id");
			String abilitatoId = Utils.getInstance().getMessageFromResourceBundle(this.nomeServizio + ".abilitato.id");
			String portaleId = Utils.getInstance().getMessageFromResourceBundle(this.nomeServizio + ".id.id");
			String defaultCallbackURLId = Utils.getInstance().getMessageFromResourceBundle(this.nomeServizio + ".defaultCallbackURL.id");
			String applicazioniId = Utils.getInstance().getMessageFromResourceBundle(this.nomeServizio + ".idApplicazioni.id");

			// id 
			InputNumber id = new InputNumber(portaleId, null, null, true, true, false, 1, 20);
			infoCreazioneMap.put(portaleId, id);

			// codPortale
			String codPortaleLabel = Utils.getInstance().getMessageFromResourceBundle(this.nomeServizio + ".codPortale.label");
			InputText codPortale = new InputText(codPortaleId, codPortaleLabel, null, true, false, true, 1, 35);
			codPortale.setSuggestion(Utils.getInstance().getMessageFromResourceBundle(this.nomeServizio + ".codPortale.suggestion"));
			codPortale.setValidation(null, Utils.getInstance().getMessageFromResourceBundle(this.nomeServizio + ".codPortale.errorMessage"));
			infoCreazioneMap.put(codPortaleId, codPortale);

			// principal
			String principalLabel = Utils.getInstance().getMessageFromResourceBundle(this.nomeServizio + ".principal.label");
			InputText principal = new InputText(principalId, principalLabel, null, true, false, true, 1, 255);
			principal.setValidation(null, Utils.getInstance().getMessageFromResourceBundle(this.nomeServizio + ".principal.errorMessage"));
			infoCreazioneMap.put(principalId, principal);

			// abilitato
			String abilitatoLabel = Utils.getInstance().getMessageFromResourceBundle(this.nomeServizio + ".abilitato.label");
			CheckButton abiliato = new CheckButton(abilitatoId, abilitatoLabel, true, false, false, true);
			infoCreazioneMap.put(abilitatoId, abiliato);

			String defaultCallbackURLLabel = Utils.getInstance().getMessageFromResourceBundle(this.nomeServizio + ".defaultCallbackURL.label");
			InputText defaultCallbackURL = new InputText(defaultCallbackURLId, defaultCallbackURLLabel, null, true, false, true, 1,512);
			infoCreazioneMap.put(defaultCallbackURLId, defaultCallbackURL);

			String applicazioniLabel = Utils.getInstance().getMessageFromResourceBundle(this.nomeServizio + ".idApplicazioni.label");
			List<Voce<Long>> idApplicazioni= new ArrayList<Voce<Long>>();
			Applicazioni applicazioni = new Applicazioni(applicazioniId, applicazioniLabel, null, false, false, true, idApplicazioni);
			infoCreazioneMap.put(applicazioniId, applicazioni);
		}
	}

	@Override
	public InfoForm getInfoModifica(UriInfo uriInfo, BasicBD bd, Portale entry) throws ConsoleException {
		URI modifica = this.getUriModifica(uriInfo, bd);
		InfoForm infoModifica = new InfoForm(modifica);

		String codPortaleId = Utils.getInstance().getMessageFromResourceBundle(this.nomeServizio + ".codPortale.id");
		String principalId = Utils.getInstance().getMessageFromResourceBundle(this.nomeServizio + ".principal.id");
		String abilitatoId = Utils.getInstance().getMessageFromResourceBundle(this.nomeServizio + ".abilitato.id");
		String portaleId = Utils.getInstance().getMessageFromResourceBundle(this.nomeServizio + ".id.id");
		String defaultCallbackURLId = Utils.getInstance().getMessageFromResourceBundle(this.nomeServizio + ".defaultCallbackURL.id");
		String applicazioniId = Utils.getInstance().getMessageFromResourceBundle(this.nomeServizio + ".idApplicazioni.id");

		if(infoCreazioneMap == null){
			this.initInfoCreazione(uriInfo, bd);
		}

		Sezione sezioneRoot = infoModifica.getSezioneRoot();
		InputNumber idInterm = (InputNumber) infoCreazioneMap.get(portaleId);
		idInterm.setDefaultValue(entry.getId());
		sezioneRoot.addField(idInterm);

		InputText codPortale = (InputText) infoCreazioneMap.get(codPortaleId);
		codPortale.setDefaultValue(entry.getCodPortale());
		codPortale.setEditable(false); 
		sezioneRoot.addField(codPortale);

		InputText principal = (InputText) infoCreazioneMap.get(principalId);
		principal.setDefaultValue(entry.getPrincipal());
		sezioneRoot.addField(principal);

		InputText defaultCallbackURL = (InputText) infoCreazioneMap.get(defaultCallbackURLId);
		defaultCallbackURL.setDefaultValue(entry.getDefaultCallbackURL());
		sezioneRoot.addField(defaultCallbackURL);

		Applicazioni applicazioni = (Applicazioni) infoCreazioneMap.get(applicazioniId);
		ApplicazioniBD applicazioniBD = new ApplicazioniBD(bd);
		List<Voce<Long>> idApplicazioni= new ArrayList<Voce<Long>>();
		try {
			ApplicazioneFilter filter = applicazioniBD.newFilter();
			FilterSortWrapper fsw = new FilterSortWrapper();
			fsw.setField(it.govpay.orm.Applicazione.model().COD_APPLICAZIONE);
			fsw.setSortOrder(SortOrder.ASC);
			filter.getFilterSortList().add(fsw);
			List<Applicazione> applicazioniList = applicazioniBD.findAll(filter);
			if(applicazioniList != null && applicazioniList.size() > 0){
				for (Applicazione tributo : applicazioniList) {
					idApplicazioni.add(new Voce<Long>(tributo.getCodApplicazione(), tributo.getId())); 
				}
			}

		} catch (ServiceException e) {
			throw new ConsoleException(e);
		}

		applicazioni.setValues(idApplicazioni); 
		applicazioni.setDefaultValue(entry.getIdApplicazioni());
		sezioneRoot.addField(applicazioni);

		CheckButton abilitato = (CheckButton) infoCreazioneMap.get(abilitatoId);
		abilitato.setDefaultValue(entry.isAbilitato()); 
		sezioneRoot.addField(abilitato);

		return infoModifica;
	}

	@Override
	public Object getField(UriInfo uriInfo,List<RawParamValue>values, String fieldId,BasicBD bd) throws WebApplicationException,ConsoleException {
		this.log.debug("Richiesto field ["+fieldId+"]");
		try{
			// Operazione consentita solo all'amministratore
			this.darsService.checkOperatoreAdmin(bd);
			
			if(infoCreazioneMap == null){
				this.initInfoCreazione(uriInfo, bd);
			}

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
			this.log.info("Esecuzione " + methodName + " in corso...");
			// Operazione consentita solo all'amministratore
			this.darsService.checkOperatoreAdmin(bd);

			// recupero oggetto
			PortaliBD portaliBD = new PortaliBD(bd);
			Portale portale = portaliBD.getPortale(id);

			InfoForm infoModifica = this.getInfoModifica(uriInfo, bd,portale);
			URI cancellazione = null;
			URI esportazione = null;

			Dettaglio dettaglio = new Dettaglio(this.getTitolo(portale,bd), esportazione, cancellazione, infoModifica);

			it.govpay.web.rs.dars.model.Sezione root = dettaglio.getSezioneRoot(); 

			// dati dell'intermediario
			root.addVoce(Utils.getInstance().getMessageFromResourceBundle(this.nomeServizio + ".codPortale.label"), portale.getCodPortale());
			root.addVoce(Utils.getInstance().getMessageFromResourceBundle(this.nomeServizio + ".principal.label"), portale.getPrincipal());
			root.addVoce(Utils.getInstance().getMessageFromResourceBundle(this.nomeServizio + ".defaultCallbackURL.label"), portale.getDefaultCallbackURL());
			root.addVoce(Utils.getInstance().getMessageFromResourceBundle(this.nomeServizio + ".abilitato.label"), Utils.getSiNoAsLabel(portale.isAbilitato()));

			// Elementi correlati
			String etichettaApplicazioni = Utils.getInstance().getMessageFromResourceBundle(this.nomeServizio + ".elementoCorrelato.applicazioni.titolo");
			it.govpay.web.rs.dars.model.Sezione sezioneApplicazioni = dettaglio.addSezione(etichettaApplicazioni);
			
			if(!Utils.isEmpty(portale.getIdApplicazioni())){
				ApplicazioniBD applicazioniBD = new ApplicazioniBD(bd);
				ApplicazioneFilter filter = applicazioniBD.newFilter();
				FilterSortWrapper fsw = new FilterSortWrapper();
				fsw.setField(it.govpay.orm.Applicazione.model().COD_APPLICAZIONE);
				fsw.setSortOrder(SortOrder.ASC);
				filter.getFilterSortList().add(fsw);
				filter.setListaIdApplicazioni(portale.getIdApplicazioni());
				
				List<Applicazione> findAll =  applicazioniBD.findAll(filter);

					
				
				it.govpay.web.rs.dars.anagrafica.applicazioni.Applicazioni applicazioniDars = new it.govpay.web.rs.dars.anagrafica.applicazioni.Applicazioni();
				ApplicazioniHandler applicazioniDarsHandler = (ApplicazioniHandler) applicazioniDars.getDarsHandler();
				UriBuilder uriDettaglioApplicazioniBuilder = BaseRsService.checkDarsURI(uriInfo).path(applicazioniDars.getPathServizio()).path("{id}");

				if(findAll != null && findAll.size() > 0){
					for (Applicazione entry : findAll) {
						Elemento elemento = applicazioniDarsHandler.getElemento(entry, entry.getId(), uriDettaglioApplicazioniBuilder,bd);
						sezioneApplicazioni.addVoce(elemento.getTitolo(), elemento.getSottotitolo(), elemento.getUri());
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

	@Override
	public Dettaglio insert(InputStream is, UriInfo uriInfo, BasicBD bd)
			throws WebApplicationException, ConsoleException,ValidationException,DuplicatedEntryException {
		String methodName = "Insert " + this.titoloServizio;

		try{
			this.log.info("Esecuzione " + methodName + " in corso...");
			// Operazione consentita solo all'amministratore
			this.darsService.checkOperatoreAdmin(bd);

			Portale entry = this.creaEntry(is, uriInfo, bd);

			this.checkEntry(entry, null);

			PortaliBD applicazioniBD = new PortaliBD(bd);

			try{
				applicazioniBD.getPortale(entry.getCodPortale());
				String msg = Utils.getInstance().getMessageWithParamsFromResourceBundle(this.nomeServizio + ".oggettoEsistente", entry.getCodPortale());
				throw new DuplicatedEntryException(msg);
			}catch(NotFoundException e){}

			applicazioniBD.insertPortale(entry); 

			this.log.info("Esecuzione " + methodName + " completata.");

			return this.getDettaglio(entry.getId(),uriInfo,bd);
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
	public Portale creaEntry(InputStream is, UriInfo uriInfo, BasicBD bd)
			throws WebApplicationException, ConsoleException {
		String methodName = "creaEntry " + this.titoloServizio;
		Portale entry = null;
		try{
			this.log.info("Esecuzione " + methodName + " in corso...");
			// Operazione consentita solo all'amministratore
			this.darsService.checkOperatoreAdmin(bd);

			JsonConfig jsonConfig = new JsonConfig();
			
			Map<String,Class<?>> classMap = new HashMap<String, Class<?>>();
			classMap.put("idApplicazioni", Long.class); 
			jsonConfig.setClassMap(classMap);
			ByteArrayOutputStream baos = new ByteArrayOutputStream();
			Utils.copy(is, baos);

			baos.flush();
			baos.close();

			JSONObject jsonObjectPortale = JSONObject.fromObject( baos.toString() );  
			jsonConfig.setRootClass(Portale.class);
			entry = (Portale) JSONObject.toBean( jsonObjectPortale, jsonConfig );

			this.log.info("Esecuzione " + methodName + " completata.");
			return entry;
		}catch(WebApplicationException e){
			throw e;
		}catch(Exception e){
			throw new ConsoleException(e);
		}
	}

	@Override
	public void checkEntry(Portale entry, Portale oldEntry) throws ValidationException {
		if(entry == null || StringUtils.isEmpty(entry.getCodPortale())) {
			throw new ValidationException("il campo Cod Portale deve essere valorizzato.");
		}

		if(entry.getPrincipal() == null || entry.getPrincipal().isEmpty()) throw new ValidationException("Il campo Principal deve essere valorizzato.");

		if(oldEntry != null) { //caso update
			if(!oldEntry.getCodPortale().equals(entry.getCodPortale()))
				throw new ValidationException("Cod Portale non deve cambiare in update. Atteso ["+oldEntry.getCodPortale()+"] trovato ["+entry.getCodPortale()+"]");
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

			Portale entry = this.creaEntry(is, uriInfo, bd);

			PortaliBD applicazioniBD = new PortaliBD(bd);
			Portale oldEntry = applicazioniBD.getPortale(entry.getCodPortale());

			this.checkEntry(entry, oldEntry);

			applicazioniBD.updatePortale(entry); 

			this.log.info("Esecuzione " + methodName + " completata.");
			return this.getDettaglio(entry.getId(),uriInfo,bd);
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
	public String getTitolo(Portale entry, BasicBD bd) {
		StringBuilder sb = new StringBuilder();

		sb.append(entry.getCodPortale());
		return sb.toString();
	}

	@Override
	public String getSottotitolo(Portale entry, BasicBD bd) {
		StringBuilder sb = new StringBuilder();

		sb.append(Utils.getAbilitatoAsLabel(entry.isAbilitato()));

		return sb.toString();
	}

	@Override
	public String esporta(List<Long> idsToExport, UriInfo uriInfo, BasicBD bd, ZipOutputStream zout)
			throws WebApplicationException, ConsoleException {
		return null;
	}
	
	@Override
	public String esporta(Long idToExport, UriInfo uriInfo, BasicBD bd, ZipOutputStream zout)	throws WebApplicationException, ConsoleException {
		return null;
	}

}
