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
package it.govpay.web.rs.dars.anagrafica.tributi;

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

import org.apache.logging.log4j.Logger;
import org.openspcoop2.generic_project.exception.NotFoundException;
import org.openspcoop2.generic_project.expression.SortOrder;

import it.govpay.bd.BasicBD;
import it.govpay.bd.FilterSortWrapper;
import it.govpay.bd.anagrafica.TipiTributoBD;
import it.govpay.bd.anagrafica.filters.TipoTributoFilter;
import it.govpay.bd.model.TipoTributo;
import it.govpay.bd.model.Tributo;
import it.govpay.web.rs.BaseRsService;
import it.govpay.web.rs.dars.BaseDarsHandler;
import it.govpay.web.rs.dars.BaseDarsService;
import it.govpay.web.rs.dars.IDarsHandler;
import it.govpay.web.rs.dars.exception.ConsoleException;
import it.govpay.web.rs.dars.exception.DuplicatedEntryException;
import it.govpay.web.rs.dars.exception.ValidationException;
import it.govpay.web.rs.dars.model.Dettaglio;
import it.govpay.web.rs.dars.model.Elemento;
import it.govpay.web.rs.dars.model.Elenco;
import it.govpay.web.rs.dars.model.InfoForm;
import it.govpay.web.rs.dars.model.InfoForm.Sezione;
import it.govpay.web.rs.dars.model.RawParamValue;
import it.govpay.web.rs.dars.model.input.ParamField;
import it.govpay.web.rs.dars.model.input.RefreshableParamField;
import it.govpay.web.rs.dars.model.input.base.InputNumber;
import it.govpay.web.rs.dars.model.input.base.InputText;
import it.govpay.web.utils.Utils;
import net.sf.json.JSONObject;
import net.sf.json.JsonConfig;

public class TipiTributoHandler extends BaseDarsHandler<TipoTributo> implements IDarsHandler<TipoTributo>{

	private static Map<String, ParamField<?>> infoCreazioneMap = null;
	private static Map<String, ParamField<?>> infoRicercaMap = null;
	public static final String ANAGRAFICA_UO = "anagrafica";

	public TipiTributoHandler(Logger log, BaseDarsService darsService) {
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

			boolean visualizzaRicerca = true;
			this.log.info("Esecuzione " + methodName + " in corso..."); 

			TipiTributoBD tipiTributoBD = new TipiTributoBD(bd);
			TipoTributoFilter filter = tipiTributoBD.newFilter();
			filter.setOffset(offset);
			filter.setLimit(limit);
			FilterSortWrapper fsw = new FilterSortWrapper();
			fsw.setField(it.govpay.orm.TipoTributo.model().COD_TRIBUTO);
			fsw.setSortOrder(SortOrder.ASC);
			filter.getFilterSortList().add(fsw);

			String codTributoId = Utils.getInstance().getMessageFromResourceBundle(this.nomeServizio + ".codTributo.id");
			String codTributo = this.getParameter(uriInfo, codTributoId, String.class);
			filter.setCodTributo(codTributo); 

			long count = tipiTributoBD.count(filter);

			// visualizza la ricerca solo se i risultati sono > del limit
			visualizzaRicerca = visualizzaRicerca && this.visualizzaRicerca(count, limit);

			InfoForm infoRicerca = visualizzaRicerca ? this.getInfoRicerca(uriInfo, bd) : null;

			List<String> titoli = new ArrayList<String>();
			titoli.add("Codice Tributo");
			titoli.add("Descrizione");
			Elemento intestazione = new Elemento(-1, titoli , null);
			Elenco elenco = new Elenco(this.titoloServizio, infoRicerca, this.getInfoCreazione(uriInfo, bd), count, esportazione, cancellazione, true,intestazione);  

			UriBuilder uriDettaglioBuilder = BaseRsService.checkDarsURI(uriInfo).path(this.pathServizio).path("{id}");

			List<TipoTributo> findAll = tipiTributoBD.findAll(filter);

			if(findAll != null && findAll.size() > 0){
				for (TipoTributo entry : findAll) {
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
		URI ricerca =  null;
		try{
			ricerca =  BaseRsService.checkDarsURI(uriInfo).path(this.pathServizio).build();
		}catch(Exception e ){
			throw new ConsoleException(e);
		}
		InfoForm infoRicerca = new InfoForm(ricerca);

		String codTributoId = Utils.getInstance().getMessageFromResourceBundle(this.nomeServizio + ".codTributo.id");

		if(infoRicercaMap == null){
			this.initInfoRicerca(uriInfo, bd);
		}

		Sezione sezioneRoot = infoRicerca.getSezioneRoot();

		InputText codTributo = (InputText) infoRicercaMap.get(codTributoId);
		codTributo.setDefaultValue(null);
		sezioneRoot.addField(codTributo);

		return infoRicerca;
	}

	private void initInfoRicerca(UriInfo uriInfo, BasicBD bd) throws ConsoleException{
		if(infoRicercaMap == null){
			infoRicercaMap = new HashMap<String, ParamField<?>>();

			String codTributoId = Utils.getInstance().getMessageFromResourceBundle(this.nomeServizio + ".codTributo.id");

			// codTributo
			String codTributoLabel = Utils.getInstance().getMessageFromResourceBundle(this.nomeServizio + ".codTributo.label");
			InputText codTributo = new InputText(codTributoId, codTributoLabel, null, false, false, true, 1, 255);
			infoRicercaMap.put(codTributoId, codTributo);
		}
	}

	@Override
	public InfoForm getInfoCreazione(UriInfo uriInfo, BasicBD bd) throws ConsoleException {
		URI creazione = this.getUriCreazione(uriInfo, bd);
		InfoForm infoCreazione = new InfoForm(creazione,Utils.getInstance().getMessageFromResourceBundle(this.nomeServizio + ".creazione.titolo"));

		String codTributoId = Utils.getInstance().getMessageFromResourceBundle(this.nomeServizio + ".codTributo.id");
		String tributoId = Utils.getInstance().getMessageFromResourceBundle(this.nomeServizio + ".id.id");
		String descrizioneId = Utils.getInstance().getMessageFromResourceBundle(this.nomeServizio + ".descrizione.id");

		if(infoCreazioneMap == null){
			this.initInfoCreazione(uriInfo, bd);

		}

		Sezione sezioneRoot = infoCreazione.getSezioneRoot();

		InputNumber idITributo = (InputNumber) infoCreazioneMap.get(tributoId);
		idITributo.setDefaultValue(null);
		sezioneRoot.addField(idITributo);

		InputText codTributo = (InputText) infoCreazioneMap.get(codTributoId);
		codTributo.setDefaultValue(null);
		codTributo.setEditable(true);
		sezioneRoot.addField(codTributo);

		InputText descrizione = (InputText) infoCreazioneMap.get(descrizioneId);
		descrizione.setDefaultValue(null);
		descrizione.setEditable(true);
		sezioneRoot.addField(descrizione);

		return infoCreazione;
	}

	private void initInfoCreazione(UriInfo uriInfo, BasicBD bd) throws ConsoleException{
		if(infoCreazioneMap == null){
			infoCreazioneMap = new HashMap<String, ParamField<?>>();

			String codTributoId = Utils.getInstance().getMessageFromResourceBundle(this.nomeServizio + ".codTributo.id");
			String tributoId = Utils.getInstance().getMessageFromResourceBundle(this.nomeServizio + ".id.id");
			String descrizioneId = Utils.getInstance().getMessageFromResourceBundle(this.nomeServizio + ".descrizione.id");

			// id 
			InputNumber id = new InputNumber(tributoId, null, null, true, true, false, 1, 20);
			infoCreazioneMap.put(tributoId, id);

			// codTributo
			String codTributoLabel = Utils.getInstance().getMessageFromResourceBundle(this.nomeServizio + ".codTributo.label");
			InputText codTributo = new InputText(codTributoId, codTributoLabel, null, true, false, true, 1, 35);
			codTributo.setSuggestion(Utils.getInstance().getMessageFromResourceBundle(this.nomeServizio + ".codTributo.suggestion"));
			codTributo.setValidation(null, Utils.getInstance().getMessageFromResourceBundle(this.nomeServizio + ".codTributo.errorMessage"));
			infoCreazioneMap.put(codTributoId, codTributo);

			//descrizione
			String descrizioneLabel = Utils.getInstance().getMessageFromResourceBundle(this.nomeServizio + ".descrizione.label");
			InputText descrizione = new InputText(descrizioneId, descrizioneLabel, null, true, false, true, 1, 255);
			infoCreazioneMap.put(descrizioneId, descrizione);
		}
	}

	@Override
	public InfoForm getInfoModifica(UriInfo uriInfo, BasicBD bd, TipoTributo entry) throws ConsoleException {
		URI modifica = this.getUriModifica(uriInfo, bd);
		InfoForm infoModifica = new InfoForm(modifica,Utils.getInstance().getMessageFromResourceBundle(this.nomeServizio + ".modifica.titolo"));

		String codTributoId = Utils.getInstance().getMessageFromResourceBundle(this.nomeServizio + ".codTributo.id");
		String tributoId = Utils.getInstance().getMessageFromResourceBundle(this.nomeServizio + ".id.id");
		String descrizioneId = Utils.getInstance().getMessageFromResourceBundle(this.nomeServizio + ".descrizione.id");

		if(infoCreazioneMap == null){
			this.initInfoCreazione(uriInfo, bd);
		}

		Sezione sezioneRoot = infoModifica.getSezioneRoot();
		InputNumber idTributo = (InputNumber) infoCreazioneMap.get(tributoId);
		idTributo.setDefaultValue(entry.getId());
		sezioneRoot.addField(idTributo);

		InputText codTributo = (InputText) infoCreazioneMap.get(codTributoId);
		codTributo.setDefaultValue(entry.getCodTributo());
		codTributo.setEditable(false); 
		sezioneRoot.addField(codTributo);

		InputText descrizione = (InputText) infoCreazioneMap.get(descrizioneId);
		descrizione.setDefaultValue(entry.getDescrizione());

		if(!entry.getCodTributo().equals(Tributo.BOLLOT))
			descrizione.setEditable(false);
		else 
			descrizione.setEditable(true);

		sezioneRoot.addField(descrizione);

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
			TipiTributoBD tributiBD = new TipiTributoBD(bd);
			TipoTributo tributo = tributiBD.getTipoTributo(id);

			InfoForm infoModifica = this.getInfoModifica(uriInfo, bd,tributo);
			URI cancellazione = null;
			URI esportazione = null;

			Dettaglio dettaglio = new Dettaglio(this.getTitolo(tributo,bd), esportazione, cancellazione, infoModifica);

			it.govpay.web.rs.dars.model.Sezione root = dettaglio.getSezioneRoot(); 

			// dati del tributo
			root.addVoce(Utils.getInstance().getMessageFromResourceBundle(this.nomeServizio + ".codTributo.label"), tributo.getCodTributo());
			root.addVoce(Utils.getInstance().getMessageFromResourceBundle(this.nomeServizio + ".descrizione.label"), tributo.getDescrizione());

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

			TipoTributo entry = this.creaEntry(is, uriInfo, bd);

			this.checkEntry(entry, null);

			TipiTributoBD tipiTributoBD = new TipiTributoBD(bd);

			try{
				tipiTributoBD.getTipoTributo(entry.getCodTributo());
				String msg = Utils.getInstance().getMessageWithParamsFromResourceBundle(this.nomeServizio + ".oggettoEsistente", entry.getCodTributo());
				throw new DuplicatedEntryException(msg);
			}catch(NotFoundException e){}

			tipiTributoBD.insertTipoTributo(entry); 

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
	public TipoTributo creaEntry(InputStream is, UriInfo uriInfo, BasicBD bd)
			throws WebApplicationException, ConsoleException {
		String methodName = "creaEntry " + this.titoloServizio;
		TipoTributo entry = null;
		try{
			this.log.info("Esecuzione " + methodName + " in corso...");
			// Operazione consentita solo all'amministratore
			this.darsService.checkOperatoreAdmin(bd);

			JsonConfig jsonConfig = new JsonConfig();

			ByteArrayOutputStream baos = new ByteArrayOutputStream();
			Utils.copy(is, baos);

			baos.flush();
			baos.close();

			JSONObject jsonObject = JSONObject.fromObject( baos.toString() ); 
			jsonConfig.setRootClass(TipoTributo.class);
			entry = (TipoTributo) JSONObject.toBean( jsonObject, jsonConfig );
			this.log.info("Esecuzione " + methodName + " completata.");
			return entry;
		}catch(WebApplicationException e){
			throw e;
		}catch(Exception e){
			throw new ConsoleException(e);
		}
	}

	@Override
	public void checkEntry(TipoTributo entry, TipoTributo oldEntry) throws ValidationException {
		if(entry == null || entry.getCodTributo() == null || entry.getCodTributo().isEmpty()) throw new ValidationException(Utils.getInstance().getMessageFromResourceBundle(this.nomeServizio + ".creazione.erroreCodTributoObbligatorio"));
		if(entry == null || entry.getDescrizione() == null || entry.getDescrizione().isEmpty()) throw new ValidationException(Utils.getInstance().getMessageFromResourceBundle(this.nomeServizio + ".creazione.erroreDecrizioneObbligatoria"));

		if(oldEntry != null) {
			if(!entry.getCodTributo().equals(oldEntry.getCodTributo())) throw new ValidationException(Utils.getInstance().getMessageFromResourceBundle(this.nomeServizio + ".aggiornamento.erroreCodTributoModificato"));
		}
	}

	@Override
	public Dettaglio update(InputStream is, UriInfo uriInfo, BasicBD bd) throws WebApplicationException, ConsoleException, ValidationException {
		String methodName = "Update " + this.titoloServizio;

		try{
			this.log.info("Esecuzione " + methodName + " in corso...");
			// Operazione consentita solo all'amministratore
			this.darsService.checkOperatoreAdmin(bd);

			TipoTributo entry = this.creaEntry(is, uriInfo, bd);

			TipiTributoBD tributiBD = new TipiTributoBD(bd);
			TipoTributo oldEntry = tributiBD.getTipoTributo(entry.getCodTributo());
			this.checkEntry(entry, oldEntry);

			tributiBD.updateTipoTributo(entry); 

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
		// operazione non prevista
	}

	@Override
	public String getTitolo(TipoTributo entry, BasicBD bd) {
		StringBuilder sb = new StringBuilder();

		sb.append( entry.getDescrizione());
		sb.append(" (").append(entry.getCodTributo()).append(")");

		return sb.toString();
	}

	@Override
	public String getSottotitolo(TipoTributo entry, BasicBD bd) throws ConsoleException {
		StringBuilder sb = new StringBuilder();
		return sb.toString();
	}
	
	@Override
	public List<String> getValori(TipoTributo entry, BasicBD bd) throws ConsoleException {
		List<String> valori = new ArrayList<String>();
		
		valori.add(entry.getCodTributo());
		valori.add(entry.getDescrizione());
		
		return valori;
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
