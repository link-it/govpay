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
package it.govpay.web.rs.dars.anagrafica.stazioni;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.net.URI;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.zip.ZipOutputStream;

import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.UriBuilder;
import javax.ws.rs.core.UriInfo;

import org.apache.commons.lang.StringUtils;
import org.apache.logging.log4j.Logger;
import org.jboss.resteasy.plugins.providers.multipart.MultipartFormDataInput;
import org.openspcoop2.generic_project.exception.NotFoundException;
import org.openspcoop2.generic_project.expression.SortOrder;

import it.govpay.bd.BasicBD;
import it.govpay.bd.FilterSortWrapper;
import it.govpay.bd.anagrafica.IntermediariBD;
import it.govpay.bd.anagrafica.StazioniBD;
import it.govpay.bd.anagrafica.filters.StazioneFilter;
import it.govpay.bd.model.Stazione;
import it.govpay.model.Intermediario;
import it.govpay.web.rs.BaseRsService;
import it.govpay.web.rs.dars.BaseDarsHandler;
import it.govpay.web.rs.dars.BaseDarsService;
import it.govpay.web.rs.dars.IDarsHandler;
import it.govpay.web.rs.dars.anagrafica.intermediari.Intermediari;
import it.govpay.web.rs.dars.exception.ConsoleException;
import it.govpay.web.rs.dars.exception.DuplicatedEntryException;
import it.govpay.web.rs.dars.exception.ValidationException;
import it.govpay.web.rs.dars.model.Dettaglio;
import it.govpay.web.rs.dars.model.Elenco;
import it.govpay.web.rs.dars.model.InfoForm;
import it.govpay.web.rs.dars.model.InfoForm.Sezione;
import it.govpay.web.rs.dars.model.RawParamValue;
import it.govpay.web.rs.dars.model.input.ParamField;
import it.govpay.web.rs.dars.model.input.base.CheckButton;
import it.govpay.web.rs.dars.model.input.base.InputNumber;
import it.govpay.web.rs.dars.model.input.base.InputSecret;
import it.govpay.web.rs.dars.model.input.base.InputText;
import it.govpay.web.utils.Utils;
import net.sf.json.JSONObject;
import net.sf.json.JsonConfig;

public class StazioniHandler extends BaseDarsHandler<Stazione> implements IDarsHandler<Stazione> {

	private static Map<String, ParamField<?>> infoCreazioneMap = null;
	private String codIntermediario = null;

	public StazioniHandler(Logger log, BaseDarsService darsService) {
		super(log,darsService);
	}

	@Override
	public Elenco getElenco(UriInfo uriInfo, BasicBD bd) throws WebApplicationException,ConsoleException {
		String methodName = "getElenco " + this.titoloServizio;
		try{
			this.log.info("Esecuzione " + methodName + " in corso..."); 
			// Operazione consentita solo all'amministratore
			this.darsService.checkOperatoreAdmin(bd);
			Integer offset = this.getOffset(uriInfo);

			Intermediari intermediariDars = new Intermediari();
			String codIntermediarioId = Utils.getInstance(this.getLanguage()).getMessageFromResourceBundle(intermediariDars.getNomeServizio()+ ".codIntermediario.id");
			this.codIntermediario = this.getParameter(uriInfo, codIntermediarioId, String.class);
			URI esportazione = null;
			URI cancellazione = null;

			StazioniBD stazioniBD = new StazioniBD(bd);
			StazioneFilter filter = stazioniBD.newFilter();
			filter.setCodIntermediario(this.codIntermediario);
			FilterSortWrapper fsw = new FilterSortWrapper();
			fsw.setField(it.govpay.orm.Stazione.model().COD_STAZIONE);
			fsw.setSortOrder(SortOrder.ASC);
			filter.getFilterSortList().add(fsw);
			filter.setOffset(offset);

			long count = stazioniBD.count(filter);
			
			Map<String, String> params = new HashMap<String, String>();
			params.put(codIntermediarioId, this.codIntermediario);

			Elenco elenco = new Elenco(this.titoloServizio, this.getInfoRicerca(uriInfo, bd,params),
					this.getInfoCreazione(uriInfo, bd),
					count, esportazione, cancellazione); 

			UriBuilder uriDettaglioBuilder = BaseRsService.checkDarsURI(uriInfo).path(this.pathServizio).path("{id}");

			List<Stazione> findAll = stazioniBD.findAll(filter);

			if(findAll != null && findAll.size() > 0){
				for (Stazione entry : findAll) {
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
	public InfoForm getInfoRicerca(UriInfo uriInfo, BasicBD bd, boolean visualizzaRicerca, Map<String,String> parameters) throws ConsoleException {
		URI ricerca =  this.getUriRicerca(uriInfo, bd, parameters);
		InfoForm infoRicerca = new InfoForm(ricerca);
		return infoRicerca;
	}

	@Override
	public InfoForm getInfoCreazione(UriInfo uriInfo, BasicBD bd) throws ConsoleException {
		URI creazione = this.getUriCreazione(uriInfo, bd);
		InfoForm infoCreazione = new InfoForm(creazione,Utils.getInstance(this.getLanguage()).getMessageFromResourceBundle(this.nomeServizio + ".creazione.titolo"));

		String stazioneId = Utils.getInstance(this.getLanguage()).getMessageFromResourceBundle(this.nomeServizio + ".id.id");
		String codStazioneId = Utils.getInstance(this.getLanguage()).getMessageFromResourceBundle(this.nomeServizio + ".codStazione.id");
		String passwordId = Utils.getInstance(this.getLanguage()).getMessageFromResourceBundle(this.nomeServizio + ".password.id");
		String abilitatoId = Utils.getInstance(this.getLanguage()).getMessageFromResourceBundle(this.nomeServizio + ".abilitato.id");
		String applicationCodeId = Utils.getInstance(this.getLanguage()).getMessageFromResourceBundle(this.nomeServizio + ".applicationCode.id");
		Intermediari intermediariDars = new Intermediari();
		String codIntermediarioId = Utils.getInstance(this.getLanguage()).getMessageFromResourceBundle(intermediariDars.getNomeServizio()+ ".codIntermediario.id");

		if(infoCreazioneMap == null){
			this.initInfoCreazione(uriInfo, bd);
		}

		Sezione sezioneRoot = infoCreazione.getSezioneRoot();
		InputNumber idStaz = (InputNumber) infoCreazioneMap.get(stazioneId);
		idStaz.setDefaultValue(null);
		sezioneRoot.addField(idStaz);

		InputText codIntermediario = (InputText) infoCreazioneMap.get(codIntermediarioId);
		codIntermediario.setDefaultValue(this.codIntermediario);
		sezioneRoot.addField(codIntermediario);

		InputText codStazione = (InputText) infoCreazioneMap.get(codStazioneId);
		String codStazioneSuggestion = this.codIntermediario + "_[0-9]{2}"; 
		codStazione.setDefaultValue(this.codIntermediario + "_");
		String codStazionePattern = this.codIntermediario + "[_]{1,1}[0-9]{2,2}";
		String codStazioneErrorMessage = Utils.getInstance(this.getLanguage()).getMessageFromResourceBundle(this.nomeServizio + ".codStazione.errorMessage") +" "+  codStazioneSuggestion;
		codStazione.setValidation(codStazionePattern, codStazioneErrorMessage); 
		codStazione.setSuggestion(codStazioneSuggestion);
		codStazione.setEditable(true);     
		sezioneRoot.addField(codStazione);

		InputSecret password = (InputSecret) infoCreazioneMap.get(passwordId);
		password.setDefaultValue(null);
		sezioneRoot.addField(password);

		InputNumber applicationCode = (InputNumber) infoCreazioneMap.get(applicationCodeId);
		applicationCode.setDefaultValue(null);
		sezioneRoot.addField(applicationCode);

		CheckButton abilitato = (CheckButton) infoCreazioneMap.get(abilitatoId);
		abilitato.setDefaultValue(true); 
		sezioneRoot.addField(abilitato);

		return infoCreazione;
	}

	private void initInfoCreazione(UriInfo uriInfo, BasicBD bd) throws ConsoleException{
		if(infoCreazioneMap == null){
			infoCreazioneMap = new HashMap<String, ParamField<?>>();

			String stazioneId = Utils.getInstance(this.getLanguage()).getMessageFromResourceBundle(this.nomeServizio + ".id.id");
			String codStazioneId = Utils.getInstance(this.getLanguage()).getMessageFromResourceBundle(this.nomeServizio + ".codStazione.id");
			String passwordId = Utils.getInstance(this.getLanguage()).getMessageFromResourceBundle(this.nomeServizio + ".password.id");
			String abilitatoId = Utils.getInstance(this.getLanguage()).getMessageFromResourceBundle(this.nomeServizio + ".abilitato.id");
			String applicationCodeId = Utils.getInstance(this.getLanguage()).getMessageFromResourceBundle(this.nomeServizio + ".applicationCode.id");
			Intermediari intermediariDars = new Intermediari();
			String codIntermediarioId = Utils.getInstance(this.getLanguage()).getMessageFromResourceBundle(intermediariDars.getNomeServizio()+ ".codIntermediario.id");

			// id 
			InputNumber id = new InputNumber(stazioneId, null, null, true, true, false, 1, 20);
			infoCreazioneMap.put(stazioneId, id);

			// codIntermediario
			InputText codIntermediario = new InputText(codIntermediarioId, "", null, true, true, false, 11, 11);
			infoCreazioneMap.put(codIntermediarioId, codIntermediario);

			// application Code
			String applicationCodeLabel = Utils.getInstance(this.getLanguage()).getMessageFromResourceBundle(this.nomeServizio + ".applicationCode.label");
			InputNumber applicationCode = new InputNumber(applicationCodeId, applicationCodeLabel, null, true, true, false, 2, 2);
			//			applicationCode.setSuggestion(Utils.getInstance(this.getLanguage()).getMessageFromResourceBundle(this.nomeServizio + ".applicationCode.suggestion"));
			//			applicationCode.setValidation("[0-9]", Utils.getInstance(this.getLanguage()).getMessageFromResourceBundle(this.nomeServizio + ".applicationCode.errorMessage"));
			infoCreazioneMap.put(applicationCodeId, applicationCode);

			// codStazione
			String codStazioneLabel =  Utils.getInstance(this.getLanguage()).getMessageFromResourceBundle(this.nomeServizio + ".codStazione.label");
			InputText codStazione = new InputText(codStazioneId, codStazioneLabel, null, true, false, true, 14, 14);
			infoCreazioneMap.put(codStazioneId, codStazione);

			// password
			String passwordLabel =  Utils.getInstance(this.getLanguage()).getMessageFromResourceBundle(this.nomeServizio + ".password.label");
			InputSecret password = new InputSecret(passwordId, passwordLabel, null, true, false, true, 1, 255);
			password.setValidation(null,  Utils.getInstance(this.getLanguage()).getMessageFromResourceBundle(this.nomeServizio + ".password.errorMessage"));
			infoCreazioneMap.put(passwordId,password);

			// abilitato
			String abilitatoLabel = Utils.getInstance(this.getLanguage()).getMessageFromResourceBundle(this.nomeServizio + ".abilitato.label");
			CheckButton abiliato = new CheckButton(abilitatoId, abilitatoLabel, null, false, false, true);
			infoCreazioneMap.put(abilitatoId, abiliato);



		}
	}

	@Override
	public InfoForm getInfoModifica(UriInfo uriInfo, BasicBD bd, Stazione entry) throws ConsoleException {
		URI modifica = this.getUriModifica(uriInfo, bd);
		InfoForm infoModifica = new InfoForm(modifica,Utils.getInstance(this.getLanguage()).getMessageFromResourceBundle(this.nomeServizio + ".modifica.titolo"));

		String stazioneId = Utils.getInstance(this.getLanguage()).getMessageFromResourceBundle(this.nomeServizio + ".id.id");
		String codStazioneId = Utils.getInstance(this.getLanguage()).getMessageFromResourceBundle(this.nomeServizio + ".codStazione.id");
		String passwordId = Utils.getInstance(this.getLanguage()).getMessageFromResourceBundle(this.nomeServizio + ".password.id");
		String abilitatoId = Utils.getInstance(this.getLanguage()).getMessageFromResourceBundle(this.nomeServizio + ".abilitato.id");
		String applicationCodeId = Utils.getInstance(this.getLanguage()).getMessageFromResourceBundle(this.nomeServizio + ".applicationCode.id");
		Intermediari intermediariDars = new Intermediari();
		String codIntermediarioId = Utils.getInstance(this.getLanguage()).getMessageFromResourceBundle(intermediariDars.getNomeServizio()+ ".codIntermediario.id");

		if(infoCreazioneMap == null){
			this.initInfoCreazione(uriInfo, bd);
		}

		Sezione sezioneRoot = infoModifica.getSezioneRoot();
		InputNumber idStaz = (InputNumber) infoCreazioneMap.get(stazioneId);
		idStaz.setDefaultValue(entry.getId());
		sezioneRoot.addField(idStaz);

		InputText codIntermediario = (InputText) infoCreazioneMap.get(codIntermediarioId);
		String codIntermediarioValue = entry.getCodStazione().substring(0, entry.getCodStazione().indexOf("_"));
		codIntermediario.setDefaultValue(codIntermediarioValue);
		sezioneRoot.addField(codIntermediario);

		InputText codStazione = (InputText) infoCreazioneMap.get(codStazioneId);
		codStazione.setDefaultValue(entry.getCodStazione());
		codStazione.setEditable(false);
		sezioneRoot.addField(codStazione);

		InputSecret password = (InputSecret) infoCreazioneMap.get(passwordId);
		password.setDefaultValue(entry.getPassword());
		sezioneRoot.addField(password);

		InputNumber applicationCode = (InputNumber) infoCreazioneMap.get(applicationCodeId);
		applicationCode.setDefaultValue(entry.getApplicationCode());
		sezioneRoot.addField(applicationCode);

		CheckButton abilitato = (CheckButton) infoCreazioneMap.get(abilitatoId);
		abilitato.setDefaultValue(entry.isAbilitato()); 
		sezioneRoot.addField(abilitato);

		return infoModifica;
	}

	@Override
	public Object getField(UriInfo uriInfo,List<RawParamValue>values, String fieldId,BasicBD bd) throws ConsoleException {
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
			StazioniBD stazioniBD = new StazioniBD(bd);
			Stazione stazione = stazioniBD.getStazione(id);

			InfoForm infoModifica = this.getInfoModifica(uriInfo, bd,stazione);
			URI cancellazione = null;
			URI esportazione = null;

			Dettaglio dettaglio = new Dettaglio(this.getTitolo(stazione,bd), esportazione, cancellazione, infoModifica);

			it.govpay.web.rs.dars.model.Sezione root = dettaglio.getSezioneRoot(); 

			// dati dell'intermediario
			root.addVoce(Utils.getInstance(this.getLanguage()).getMessageFromResourceBundle(this.nomeServizio + ".codStazione.label"), stazione.getCodStazione());
			root.addVoce(Utils.getInstance(this.getLanguage()).getMessageFromResourceBundle(this.nomeServizio + ".password.label"), stazione.getPassword());
			root.addVoce(Utils.getInstance(this.getLanguage()).getMessageFromResourceBundle(this.nomeServizio + ".abilitato.label"), Utils.getSiNoAsLabel(stazione.isAbilitato()));

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
			throws WebApplicationException, ConsoleException ,ValidationException,DuplicatedEntryException {
		String methodName = "Insert " + this.titoloServizio;

		try{
			this.log.info("Esecuzione " + methodName + " in corso...");
			// Operazione consentita solo all'amministratore
			this.darsService.checkOperatoreAdmin(bd);

			Stazione entry = this.creaEntry(is, uriInfo, bd);

			this.checkEntry(entry, null);

			StazioniBD stazioniBD = new StazioniBD(bd);
			IntermediariBD intermediari = new IntermediariBD(bd);

			try{
				Intermediario intermediario = intermediari.getIntermediario(this.codIntermediario);
				entry.setIdIntermediario(intermediario.getId());
			}catch(NotFoundException e){}

			try{
				stazioniBD.getStazione(entry.getCodStazione());
				String msg = Utils.getInstance(this.getLanguage()).getMessageWithParamsFromResourceBundle(this.nomeServizio + ".oggettoEsistente", entry.getCodStazione());
				throw new DuplicatedEntryException(msg);
			}catch(NotFoundException e){}

			stazioniBD.insertStazione(entry); 

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
	public Stazione creaEntry(InputStream is, UriInfo uriInfo, BasicBD bd) throws WebApplicationException, ConsoleException {
		String methodName = "creaEntry " + this.titoloServizio;
		Stazione entry = null;
		try{
			this.log.info("Esecuzione " + methodName + " in corso...");
			// Operazione consentita solo all'amministratore
			this.darsService.checkOperatoreAdmin(bd);

			JsonConfig jsonConfig = new JsonConfig();
			ByteArrayOutputStream baos = new ByteArrayOutputStream();
			Utils.copy(is, baos);

			baos.flush();
			baos.close();

			JSONObject jsonObjectStazione = JSONObject.fromObject( baos.toString() );  
			jsonConfig.setRootClass(Stazione.class);
			entry = (Stazione) JSONObject.toBean( jsonObjectStazione, jsonConfig );

			jsonObjectStazione = JSONObject.fromObject( baos.toString() );  
			jsonConfig.setRootClass(Intermediario.class);
			Intermediario intermediario = (Intermediario) JSONObject.toBean( jsonObjectStazione, jsonConfig );
			this.codIntermediario = intermediario.getCodIntermediario();

			this.log.info("Esecuzione " + methodName + " completata.");
			return entry;
		}catch(WebApplicationException e){
			throw e;
		}catch(Exception e){
			throw new ConsoleException(e);
		}
	}
	@Override
	public void checkEntry(Stazione entry, Stazione oldEntry) throws ValidationException {
		//		List<Integer> applicationCodes = new ArrayList<Integer>();

		String codIntermediario2 = entry.getCodStazione().substring(0, entry.getCodStazione().indexOf("_"));
		
		if(StringUtils.isEmpty(codIntermediario2) || !this.codIntermediario.equals(codIntermediario2)){
			throw new ValidationException("Id Stazione non coerente rispetto all'IdIntermediario. Atteso [" + this.codIntermediario + "_" + "], fornito [" + codIntermediario2 + "]");
		}
		
		String applicationCodeAsString = entry.getCodStazione().substring(entry.getCodStazione().indexOf("_") + 1);

		if(StringUtils.isEmpty(applicationCodeAsString))
			throw new ValidationException("Valore di Application Code non calcolabile. Consentiti valori fino a 99.");

		try{
			entry.setApplicationCode(Integer.parseInt(applicationCodeAsString));   
		}catch(Exception e){
			throw new ValidationException("Formato di Application Code non calcolabile. Consentiti valori fino a 99.");
		}

		if(entry.getCodStazione() == null)  throw new ValidationException("Id Stazione nullo");
		if(entry.getApplicationCode() > 99)  throw new ValidationException("Valore di Application Code non ammesso. Consentiti valori fino a 99.");
		//		if(applicationCodes.contains(entry.getApplicationCode())) throw new ValidationException("Non sono consentite stazioni Application Code [" + entry.getApplicationCode() + "].");
		//		applicationCodes.add(entry.getApplicationCode());
		if(!entry.getCodStazione().equals(this.codIntermediario + "_" + String.format("%02d", entry.getApplicationCode()))) 
			throw new ValidationException("Id Stazione non coerente rispetto agli IdIntermediario ed ApplicationCode. Atteso [" + this.codIntermediario + "_" + String.format("%02d", entry.getApplicationCode()) + "], fornito [" + entry.getCodStazione() + "]");
		if(entry.getPassword() == null || entry.getPassword().isEmpty())   throw new ValidationException("E' necessario valorizzare il campo Password");
		if(entry.getPassword().contains(" ")) throw new ValidationException("Password non valida. Caratteri blank non ammessi");

	}

	@Override
	public Dettaglio update(InputStream is, UriInfo uriInfo, BasicBD bd) throws WebApplicationException, ConsoleException, ValidationException {
		String methodName = "Update " + this.titoloServizio;

		try{
			this.log.info("Esecuzione " + methodName + " in corso...");
			// Operazione consentita solo all'amministratore
			this.darsService.checkOperatoreAdmin(bd);

			Stazione entry = this.creaEntry(is, uriInfo, bd);
			
			StazioniBD stazioniBD = new StazioniBD(bd);
			IntermediariBD intermediari = new IntermediariBD(bd);
			
			Stazione oldEntry = stazioniBD.getStazione(entry.getCodStazione());

			this.checkEntry(entry, oldEntry); 

			try{
				Intermediario intermediario = intermediari.getIntermediario(this.codIntermediario);
				entry.setIdIntermediario(intermediario.getId());
			}catch(NotFoundException e){}

			stazioniBD.updateStazione(entry); 

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
	public void delete(List<Long> idsToDelete, UriInfo uriInfo, BasicBD bd) throws ConsoleException {
	}


	@Override
	public String getTitolo(Stazione entry, BasicBD bd) {
		return entry.getCodStazione();
	}

	@Override
	public String getSottotitolo(Stazione entry, BasicBD bd) {
		return Utils.getAbilitatoAsLabel(entry.isAbilitato()); 
	}
	
	@Override
	public List<String> getValori(Stazione entry, BasicBD bd) throws ConsoleException {
		return null;
	}
	
	@Override
	public Map<String, String> getVoci(Stazione entry, BasicBD bd) throws ConsoleException { return null; }

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
