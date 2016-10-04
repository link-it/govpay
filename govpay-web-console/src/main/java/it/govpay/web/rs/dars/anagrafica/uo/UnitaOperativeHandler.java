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
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.zip.ZipOutputStream;

import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.UriBuilder;
import javax.ws.rs.core.UriInfo;

import org.apache.logging.log4j.Logger;
import org.jboss.resteasy.plugins.providers.multipart.MultipartFormDataInput;
import org.openspcoop2.generic_project.exception.NotFoundException;
import org.openspcoop2.generic_project.expression.SortOrder;

import it.govpay.bd.BasicBD;
import it.govpay.bd.FilterSortWrapper;
import it.govpay.bd.anagrafica.DominiBD;
import it.govpay.bd.anagrafica.UnitaOperativeBD;
import it.govpay.bd.anagrafica.filters.UnitaOperativaFilter;
import it.govpay.model.Anagrafica;
import it.govpay.model.Dominio;
import it.govpay.model.UnitaOperativa;
import it.govpay.web.rs.BaseRsService;
import it.govpay.web.rs.dars.BaseDarsHandler;
import it.govpay.web.rs.dars.BaseDarsService;
import it.govpay.web.rs.dars.IDarsHandler;
import it.govpay.web.rs.dars.anagrafica.anagrafica.AnagraficaHandler;
import it.govpay.web.rs.dars.exception.ConsoleException;
import it.govpay.web.rs.dars.exception.DuplicatedEntryException;
import it.govpay.web.rs.dars.exception.ValidationException;
import it.govpay.web.rs.dars.model.Dettaglio;
import it.govpay.web.rs.dars.model.Elenco;
import it.govpay.web.rs.dars.model.InfoForm;
import it.govpay.web.rs.dars.model.InfoForm.Sezione;
import it.govpay.web.rs.dars.model.RawParamValue;
import it.govpay.web.rs.dars.model.input.ParamField;
import it.govpay.web.rs.dars.model.input.RefreshableParamField;
import it.govpay.web.rs.dars.model.input.base.CheckButton;
import it.govpay.web.rs.dars.model.input.base.InputNumber;
import it.govpay.web.rs.dars.model.input.base.InputText;
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
			Integer limit = this.getLimit(uriInfo);
			URI esportazione = null;
			URI cancellazione = null;

			this.log.info("Esecuzione " + methodName + " in corso..."); 

			UnitaOperativeBD unitaOperativaBD = new UnitaOperativeBD(bd);
			UnitaOperativaFilter filter = unitaOperativaBD.newFilter();
			filter.setOffset(offset);
			filter.setLimit(limit);
			FilterSortWrapper fsw = new FilterSortWrapper();
			fsw.setField(it.govpay.orm.Uo.model().COD_UO);
			fsw.setSortOrder(SortOrder.ASC);
			filter.getFilterSortList().add(fsw);

			boolean visualizzaRicerca = true;

			// tutte le unita' con codice uo = 'EC' sono nascoste
			filter.setExcludeEC(true); 

			String idDominioId = Utils.getInstance().getMessageFromResourceBundle(this.nomeServizio+ ".idDominio.id");
			this.idDominio = this.getParameter(uriInfo, idDominioId, Long.class);
			filter.setDominioFilter(this.idDominio);

			long count = unitaOperativaBD.count(filter);

			// visualizza la ricerca solo se i risultati sono > del limit
			visualizzaRicerca = visualizzaRicerca && this.visualizzaRicerca(count, limit);

			InfoForm infoRicerca = visualizzaRicerca ? this.getInfoRicerca(uriInfo, bd) : null;

			Elenco elenco = new Elenco(this.titoloServizio, infoRicerca,
					this.getInfoCreazione(uriInfo, bd),
					count, esportazione, cancellazione); 

			UriBuilder uriDettaglioBuilder = BaseRsService.checkDarsURI(uriInfo).path(this.pathServizio).path("{id}");

			List<UnitaOperativa> findAll =  unitaOperativaBD.findAll(filter);

			if(findAll != null && findAll.size() > 0){
				for (UnitaOperativa entry : findAll) {
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
		String idDominioId = Utils.getInstance().getMessageFromResourceBundle(this.nomeServizio + ".idDominio.id");
		URI ricerca =  null;
		try{
			ricerca =  BaseRsService.checkDarsURI(uriInfo).path(this.pathServizio).queryParam(idDominioId, this.idDominio).build();
		}catch(Exception e ){
			throw new ConsoleException(e);
		}
		InfoForm infoRicerca = new InfoForm(ricerca);

		String codUoId = Utils.getInstance().getMessageFromResourceBundle(this.nomeServizio + ".codUo.id");

		if(infoRicercaMap == null){
			this.initInfoRicerca(uriInfo, bd);
		}

		Sezione sezioneRoot = infoRicerca.getSezioneRoot();

		InputText codUnitaOperativa = (InputText) infoRicercaMap.get(codUoId);
		codUnitaOperativa.setDefaultValue(null);
		sezioneRoot.addField(codUnitaOperativa);

		return infoRicerca;
	}

	private void initInfoRicerca(UriInfo uriInfo, BasicBD bd) throws ConsoleException{
		if(infoRicercaMap == null){
			infoRicercaMap = new HashMap<String, ParamField<?>>();

			String codUoId = Utils.getInstance().getMessageFromResourceBundle(this.nomeServizio + ".codUo.id");

			// codUO
			String codUnitaOperativaLabel = Utils.getInstance().getMessageFromResourceBundle(this.nomeServizio + ".codUo.label");
			InputText codUnitaOperativa = new InputText(codUoId, codUnitaOperativaLabel, null, false, false, true, 1, 255);
			infoRicercaMap.put(codUoId, codUnitaOperativa);

		}
	}

	@Override
	public InfoForm getInfoCreazione(UriInfo uriInfo, BasicBD bd) throws ConsoleException {
		URI creazione = this.getUriCreazione(uriInfo, bd);
		InfoForm infoCreazione = new InfoForm(creazione,Utils.getInstance().getMessageFromResourceBundle(this.nomeServizio + ".creazione.titolo"));

		String codUoId = Utils.getInstance().getMessageFromResourceBundle(this.nomeServizio + ".codUo.id");
		String idDominioId = Utils.getInstance().getMessageFromResourceBundle(this.nomeServizio + ".idDominio.id");
		String abilitatoId = Utils.getInstance().getMessageFromResourceBundle(this.nomeServizio + ".abilitato.id");
		String unitaOperativaId = Utils.getInstance().getMessageFromResourceBundle(this.nomeServizio + ".id.id");

		AnagraficaHandler anagraficaHandler = new AnagraficaHandler(ANAGRAFICA_UO,this.nomeServizio,this.pathServizio);
		List<ParamField<?>> infoCreazioneAnagrafica = anagraficaHandler.getInfoCreazioneAnagraficaUO(uriInfo, bd);

		if(infoCreazioneMap == null){
			this.initInfoCreazione(uriInfo, bd);

		}

		Sezione sezioneRoot = infoCreazione.getSezioneRoot();

		InputNumber idInterm = (InputNumber) infoCreazioneMap.get(unitaOperativaId);
		idInterm.setDefaultValue(null);
		sezioneRoot.addField(idInterm);

		InputText codUnitaOperativa = (InputText) infoCreazioneMap.get(codUoId);
		codUnitaOperativa.setDefaultValue(null);
		sezioneRoot.addField(codUnitaOperativa);

		// idDominio
		InputNumber idDominio = (InputNumber) infoCreazioneMap.get(idDominioId);
		idDominio.setDefaultValue(this.idDominio);
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
			InputText codUnitaOperativa = new InputText(codUoId, codUnitaOperativaLabel, null, true, false, true, 1, 35);
			codUnitaOperativa.setSuggestion(Utils.getInstance().getMessageFromResourceBundle(this.nomeServizio + ".codUo.suggestion"));
			codUnitaOperativa.setValidation(null, Utils.getInstance().getMessageFromResourceBundle(this.nomeServizio + ".codUo.errorMessage"));
			infoCreazioneMap.put(codUoId, codUnitaOperativa);

			// idDominio
			InputNumber idDominio = new InputNumber(idDominioId, null, null, true, true, false, 1, 255);
			infoCreazioneMap.put(idDominioId, idDominio);

			// abilitato
			String abilitatoLabel = Utils.getInstance().getMessageFromResourceBundle(this.nomeServizio + ".abilitato.label");
			CheckButton abiliato = new CheckButton(abilitatoId, abilitatoLabel, true, false, false, true);
			infoCreazioneMap.put(abilitatoId, abiliato);

		}


	}

	@Override
	public InfoForm getInfoModifica(UriInfo uriInfo, BasicBD bd, UnitaOperativa entry) throws ConsoleException {
		URI modifica = this.getUriModifica(uriInfo, bd);
		InfoForm infoModifica = new InfoForm(modifica,Utils.getInstance().getMessageFromResourceBundle(this.nomeServizio + ".modifica.titolo"));

		String codUoId = Utils.getInstance().getMessageFromResourceBundle(this.nomeServizio + ".codUo.id");
		String idDominioId = Utils.getInstance().getMessageFromResourceBundle(this.nomeServizio + ".idDominio.id");
		String abilitatoId = Utils.getInstance().getMessageFromResourceBundle(this.nomeServizio + ".abilitato.id");
		String unitaOperativaId = Utils.getInstance().getMessageFromResourceBundle(this.nomeServizio + ".id.id");


		AnagraficaHandler anagraficaHandler = new AnagraficaHandler(ANAGRAFICA_UO,this.nomeServizio,this.pathServizio);
		List<ParamField<?>> infoCreazioneAnagrafica = anagraficaHandler.getInfoModificaAnagraficaUO(uriInfo, bd,entry.getAnagrafica());

		if(infoCreazioneMap == null){
			this.initInfoCreazione(uriInfo, bd);
		}

		Sezione sezioneRoot = infoModifica.getSezioneRoot();
		InputNumber idInterm = (InputNumber) infoCreazioneMap.get(unitaOperativaId);
		idInterm.setDefaultValue(entry.getId());
		sezioneRoot.addField(idInterm);

		InputText codUnitaOperativa = (InputText) infoCreazioneMap.get(codUoId);
		codUnitaOperativa.setDefaultValue(entry.getCodUo());
		sezioneRoot.addField(codUnitaOperativa);

		InputNumber idDominio = (InputNumber) infoCreazioneMap.get(idDominioId);
		idDominio.setDefaultValue(entry.getIdDominio());
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
			UnitaOperativeBD unitaOperativaBD = new UnitaOperativeBD(bd);
			UnitaOperativa unitaOperativa = unitaOperativaBD.getUnitaOperativa(id);

			InfoForm infoModifica = this.getInfoModifica(uriInfo, bd,unitaOperativa);
			URI cancellazione = null;
			URI esportazione = null;

			Dettaglio dettaglio = new Dettaglio(this.getTitolo(unitaOperativa,bd), esportazione, cancellazione, infoModifica);

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

			UnitaOperativa entry = this.creaEntry(is, uriInfo, bd);

			this.checkEntry(entry, null);

			UnitaOperativeBD uoBD = new UnitaOperativeBD(bd);

			try{
				uoBD.getUnitaOperativa(entry.getIdDominio(),entry.getCodUo());
				String msg = Utils.getInstance().getMessageWithParamsFromResourceBundle(this.nomeServizio + ".oggettoEsistente", entry.getCodUo());
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
			this.log.info("Esecuzione " + methodName + " in corso...");
			// Operazione consentita solo all'amministratore
			this.darsService.checkOperatoreAdmin(bd);

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
	public void delete(List<Long> idsToDelete, UriInfo uriInfo, BasicBD bd) throws ConsoleException {
		// operazione non prevista
	}

	@Override
	public String getTitolo(UnitaOperativa entry, BasicBD bd) {
		StringBuilder sb = new StringBuilder();

		String ragioneSociale = entry.getAnagrafica().getRagioneSociale();
		if(ragioneSociale != null){
			sb.append(ragioneSociale);
			sb.append(" (").append(entry.getCodUo()).append(")");
		} else 
			sb.append(entry.getCodUo());

		return sb.toString();
	}

	@Override
	public String getSottotitolo(UnitaOperativa entry, BasicBD bd) throws ConsoleException {
		StringBuilder sb = new StringBuilder();

		try{
		sb.append(Utils.getAbilitatoAsLabel(entry.isAbilitato()));
		sb.append(", Dominio: ").append(entry.getDominio(bd).getCodDominio());
		}catch(Exception e){
			throw new ConsoleException(e);
		}

		return sb.toString();
	}
	
	@Override
	public List<String> getValori(UnitaOperativa entry, BasicBD bd) throws ConsoleException {
		return null;
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
	
	@Override
	public Object uplaod(MultipartFormDataInput input, UriInfo uriInfo, BasicBD bd)	throws WebApplicationException, ConsoleException, ValidationException { return null;}
}
