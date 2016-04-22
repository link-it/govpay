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
package it.govpay.web.rs.dars.anagrafica.domini;

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
import org.openspcoop2.generic_project.expression.SortOrder;

import it.govpay.bd.BasicBD;
import it.govpay.bd.FilterSortWrapper;
import it.govpay.bd.anagrafica.DominiBD;
import it.govpay.bd.anagrafica.StazioniBD;
import it.govpay.bd.anagrafica.UnitaOperativeBD;
import it.govpay.bd.anagrafica.filters.DominioFilter;
import it.govpay.bd.anagrafica.filters.StazioneFilter;
import it.govpay.bd.model.Anagrafica;
import it.govpay.bd.model.Dominio;
import it.govpay.bd.model.Stazione;
import it.govpay.bd.model.UnitaOperativa;
import it.govpay.web.rs.dars.BaseDarsHandler;
import it.govpay.web.rs.dars.BaseDarsService;
import it.govpay.web.rs.dars.IDarsHandler;
import it.govpay.web.rs.dars.anagrafica.anagrafica.AnagraficaHandler;
import it.govpay.web.rs.dars.anagrafica.iban.Iban;
import it.govpay.web.rs.dars.anagrafica.tributi.Tributi;
import it.govpay.web.rs.dars.anagrafica.uo.UnitaOperative;
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
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import net.sf.json.JsonConfig;

public class DominiHandler extends BaseDarsHandler<Dominio> implements IDarsHandler<Dominio>{

	private static Map<String, ParamField<?>> infoCreazioneMap = null;
	private static Map<String, ParamField<?>> infoRicercaMap = null;
	public static final String ANAGRAFICA_DOMINI = "anagrafica";

	public DominiHandler(Logger log, BaseDarsService darsService) {
		super(log,darsService);
	}

	@Override
	public Elenco getElenco(UriInfo uriInfo,BasicBD bd) throws WebApplicationException,ConsoleException {
		String methodName = "getElenco " + this.titoloServizio;
		try{	
			// Operazione consentita solo all'amministratore
			this.darsService.checkOperatoreAdmin(bd);

			//			Integer offset = this.getOffset(uriInfo);
			//			Integer limit = this.getLimit(uriInfo);
			URI esportazione = null;
			URI cancellazione = null;

			log.info("Esecuzione " + methodName + " in corso..."); 

			DominiBD dominiBD = new DominiBD(bd);
			DominioFilter filter = dominiBD.newFilter();
			//			filter.setOffset(offset);
			//			filter.setLimit(limit);
			FilterSortWrapper fsw = new FilterSortWrapper();
			fsw.setField(it.govpay.orm.Dominio.model().COD_DOMINIO);
			fsw.setSortOrder(SortOrder.ASC);
			filter.getFilterSortList().add(fsw);

			String codDominioId = Utils.getInstance().getMessageFromResourceBundle(this.nomeServizio + ".codDominio.id");
			String codDominio = this.getParameter(uriInfo, codDominioId, String.class);
			String idStazioneId = Utils.getInstance().getMessageFromResourceBundle(this.nomeServizio + ".idStazione.id");
			String idStazione = this.getParameter(uriInfo, idStazioneId, String.class);

			if(StringUtils.isNotEmpty(codDominio)){
				filter.setCodDominio(codDominio);
			}

			if(StringUtils.isNotEmpty(idStazione)){
				long idStaz = -1l;
				try{
					idStaz = Long.parseLong(idStazione);
				}catch(Exception e){ idStaz = -1l;	}
				if(idStaz > 0){
					StazioniBD stazioniBD = new StazioniBD(bd);
					Stazione stazione = stazioniBD.getStazione(idStaz);
					filter.setCodStazione(stazione.getCodStazione());
				}
			}

			long count = dominiBD.count(filter);

			Elenco elenco = new Elenco(this.titoloServizio, this.getInfoRicerca(uriInfo, bd),
					this.getInfoCreazione(uriInfo, bd),
					count, esportazione, cancellazione); 

			UriBuilder uriDettaglioBuilder = uriInfo.getBaseUriBuilder().path(this.pathServizio).path("{id}");

			List<Dominio> findAll = dominiBD.findAll(filter);

			if(findAll != null && findAll.size() > 0){
				for (Dominio entry : findAll) {
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

		String codDominioId = Utils.getInstance().getMessageFromResourceBundle(this.nomeServizio + ".codDominio.id");
		String idStazioneId = Utils.getInstance().getMessageFromResourceBundle(this.nomeServizio + ".idStazione.id");

		if(infoRicercaMap == null){
			initInfoRicerca(uriInfo, bd);

		}


		Sezione sezioneRoot = infoRicerca.getSezioneRoot();

		InputNumber codDominio = (InputNumber) infoRicercaMap.get(codDominioId);
		codDominio.setDefaultValue(null);
		codDominio.setEditable(true); 
		sezioneRoot.addField(codDominio);

		List<Voce<Long>> stazioni = new ArrayList<Voce<Long>>();

		try{
			StazioniBD stazioniBD = new StazioniBD(bd);
			StazioneFilter filter = stazioniBD.newFilter();
			FilterSortWrapper fsw = new FilterSortWrapper();
			fsw.setField(it.govpay.orm.Stazione.model().COD_STAZIONE);
			fsw.setSortOrder(SortOrder.ASC);
			filter.getFilterSortList().add(fsw);

			List<Stazione> findAll = stazioniBD.findAll(filter);

			stazioni.add(new Voce<Long>(Utils.getInstance().getMessageFromResourceBundle("commons.label.qualsiasi"), -1L));
			if(findAll != null && findAll.size() > 0){
				for (Stazione entry : findAll) {
					stazioni.add(new Voce<Long>(entry.getCodStazione(), entry.getId()));
				}
			}
		}catch(Exception e){
			throw new ConsoleException(e);
		}

		SelectList<Long> stazione = (SelectList<Long>) infoRicercaMap.get(idStazioneId);
		stazione.setDefaultValue(-1L);
		stazione.setValues(stazioni);
		sezioneRoot.addField(stazione); 

		return infoRicerca;
	}

	private void initInfoRicerca(UriInfo uriInfo, BasicBD bd) throws ConsoleException{
		if(infoRicercaMap == null){
			infoRicercaMap = new HashMap<String, ParamField<?>>();

			String codDominioId = Utils.getInstance().getMessageFromResourceBundle(this.nomeServizio + ".codDominio.id");
			String idStazioneId = Utils.getInstance().getMessageFromResourceBundle(this.nomeServizio + ".idStazione.id");

			// codDominio
			String codDominioLabel = Utils.getInstance().getMessageFromResourceBundle(this.nomeServizio + ".codDominio.label");
			InputNumber codDominio = new InputNumber(codDominioId, codDominioLabel, null, false, false, true, 1, 11);
			infoRicercaMap.put(codDominioId, codDominio);

			// idstazione
			String idStazionelabel =Utils.getInstance().getMessageFromResourceBundle(this.nomeServizio + ".idStazione.label");
			List<Voce<Long>> stazioni = new ArrayList<Voce<Long>>();
			SelectList<Long> idStazione = new SelectList<Long>(idStazioneId, idStazionelabel, null, false, false, true, stazioni );
			infoRicercaMap.put(idStazioneId, idStazione);

		}
	}

	@SuppressWarnings("unchecked")
	@Override
	public InfoForm getInfoCreazione(UriInfo uriInfo, BasicBD bd) throws ConsoleException {
		URI creazione = this.getUriCreazione(uriInfo, bd);
		InfoForm infoCreazione = new InfoForm(creazione);

		String codDominioId = Utils.getInstance().getMessageFromResourceBundle(this.nomeServizio + ".codDominio.id");
		String ragioneSocialeId = Utils.getInstance().getMessageFromResourceBundle(this.nomeServizio + ".ragioneSociale.id");
		String abilitatoId = Utils.getInstance().getMessageFromResourceBundle(this.nomeServizio + ".abilitato.id");
		String dominioId = Utils.getInstance().getMessageFromResourceBundle(this.nomeServizio + ".id.id");
		String idStazioneId = Utils.getInstance().getMessageFromResourceBundle(this.nomeServizio + ".idStazione.id");
		String glnId = Utils.getInstance().getMessageFromResourceBundle(this.nomeServizio + ".gln.id");
		String uoIdId = Utils.getInstance().getMessageFromResourceBundle(this.nomeServizio + ".uoId.id");

		AnagraficaHandler anagraficaHandler = new AnagraficaHandler(ANAGRAFICA_DOMINI,this.nomeServizio,this.pathServizio);
		List<ParamField<?>> infoCreazioneAnagrafica = anagraficaHandler.getInfoCreazioneAnagraficaDominio(uriInfo, bd);

		if(infoCreazioneMap == null){
			initInfoCreazione(uriInfo, bd);
		}

		Sezione sezioneRoot = infoCreazione.getSezioneRoot();
		InputNumber idInterm = (InputNumber) infoCreazioneMap.get(dominioId);
		idInterm.setDefaultValue(null);
		sezioneRoot.addField(idInterm);
		InputNumber codDominio = (InputNumber) infoCreazioneMap.get(codDominioId);
		codDominio.setDefaultValue(null);
		codDominio.setEditable(true); 
		sezioneRoot.addField(codDominio);
		
		InputNumber uoId = (InputNumber) infoCreazioneMap.get(uoIdId);
		uoId.setDefaultValue(null);
		sezioneRoot.addField(uoId);

		List<Voce<Long>> stazioni = new ArrayList<Voce<Long>>();

		try{
			StazioniBD stazioniBD = new StazioniBD(bd);
			StazioneFilter filter = stazioniBD.newFilter();
			FilterSortWrapper fsw = new FilterSortWrapper();
			fsw.setField(it.govpay.orm.Stazione.model().COD_STAZIONE);
			fsw.setSortOrder(SortOrder.ASC);
			filter.getFilterSortList().add(fsw);

			List<Stazione> findAll = stazioniBD.findAll(filter);


			if(findAll != null && findAll.size() > 0){
				for (Stazione entry : findAll) {
					stazioni.add(new Voce<Long>(entry.getCodStazione(), entry.getId()));
				}
			}
		}catch(Exception e){
			throw new ConsoleException(e);
		}

		SelectList<Long> stazione = (SelectList<Long>) infoCreazioneMap.get(idStazioneId);
		stazione.setDefaultValue(null);
		stazione.setValues(stazioni);
		sezioneRoot.addField(stazione); 
		InputText ragioneSociale = (InputText) infoCreazioneMap.get(ragioneSocialeId);
		ragioneSociale.setDefaultValue(null);
		sezioneRoot.addField(ragioneSociale);
		InputText gln = (InputText) infoCreazioneMap.get(glnId);
		gln.setDefaultValue(null);
		sezioneRoot.addField(gln);
		CheckButton abilitato = (CheckButton) infoCreazioneMap.get(abilitatoId);
		abilitato.setDefaultValue(true); 
		sezioneRoot.addField(abilitato);

		Sezione sezioneAnagrafica = infoCreazione.addSezione(Utils.getInstance().getMessageFromResourceBundle(this.nomeServizio + "." + ANAGRAFICA_DOMINI + ".titolo"));

		for (ParamField<?> par : infoCreazioneAnagrafica) { 
			sezioneAnagrafica.addField(par); 	
		}

		return infoCreazione;
	}

	private void initInfoCreazione(UriInfo uriInfo, BasicBD bd) throws ConsoleException{
		if(infoCreazioneMap == null){
			infoCreazioneMap = new HashMap<String, ParamField<?>>();

			// id 
			String dominioId = Utils.getInstance().getMessageFromResourceBundle(this.nomeServizio + ".id.id");
			InputNumber id = new InputNumber(dominioId, null, null, true, true, false, 1, 20);
			infoCreazioneMap.put(dominioId, id);

			// uoid 
			String uoIdId = Utils.getInstance().getMessageFromResourceBundle(this.nomeServizio + ".uoId.id");
			InputNumber uoId = new InputNumber(uoIdId, null, null, true, true, false, 1, 20);
			infoCreazioneMap.put(uoIdId, uoId);

			String codDominioId = Utils.getInstance().getMessageFromResourceBundle(this.nomeServizio + ".codDominio.id");
			String ragioneSocialeId = Utils.getInstance().getMessageFromResourceBundle(this.nomeServizio + ".ragioneSociale.id");
			String abilitatoId = Utils.getInstance().getMessageFromResourceBundle(this.nomeServizio + ".abilitato.id");
			String idStazioneId = Utils.getInstance().getMessageFromResourceBundle(this.nomeServizio + ".idStazione.id");
			String glnId = Utils.getInstance().getMessageFromResourceBundle(this.nomeServizio + ".gln.id");

			// codDominio
			String codDominioLabel = Utils.getInstance().getMessageFromResourceBundle(this.nomeServizio + ".codDominio.label");
			InputNumber codDominio = new InputNumber(codDominioId, codDominioLabel, null, true, false, true, 1, 11);
			codDominio.setSuggestion(Utils.getInstance().getMessageFromResourceBundle(this.nomeServizio + ".codDominio.suggestion"));
			codDominio.setValidation(null, Utils.getInstance().getMessageFromResourceBundle(this.nomeServizio + ".codDominio.errorMessage"));
			infoCreazioneMap.put(codDominioId, codDominio);

			// ragioneSociale
			String ragioneSocialeLabel = Utils.getInstance().getMessageFromResourceBundle(this.nomeServizio + ".ragioneSociale.label");
			InputText ragioneSociale = new InputText(ragioneSocialeId, ragioneSocialeLabel, null, true, false, true, 1, 50);
			ragioneSociale.setValidation(null, Utils.getInstance().getMessageFromResourceBundle(this.nomeServizio + ".ragioneSociale.errorMessage"));
			infoCreazioneMap.put(ragioneSocialeId, ragioneSociale);

			// gln
			String glnLabel = Utils.getInstance().getMessageFromResourceBundle(this.nomeServizio + ".gln.label");
			InputText gln = new InputText(glnId, glnLabel, null, true, false, true, 13, 13);
			gln.setValidation("[0-9]{13}", Utils.getInstance().getMessageFromResourceBundle(this.nomeServizio + ".gln.errorMessage"));
			infoCreazioneMap.put(glnId, gln);

			// idstazione
			String idStazionelabel =Utils.getInstance().getMessageFromResourceBundle(this.nomeServizio + ".idStazione.label");
			List<Voce<Long>> stazioni = new ArrayList<Voce<Long>>();
			SelectList<Long> idStazione = new SelectList<Long>(idStazioneId, idStazionelabel, null, true, false, true, stazioni );
			infoCreazioneMap.put(idStazioneId, idStazione);

			// abilitato
			String abilitatoLabel = Utils.getInstance().getMessageFromResourceBundle(this.nomeServizio + ".abilitato.label");
			CheckButton abiliato = new CheckButton(abilitatoId, abilitatoLabel, true, false, false, true);
			infoCreazioneMap.put(abilitatoId, abiliato);

		}
	}

	@SuppressWarnings("unchecked")
	@Override
	public InfoForm getInfoModifica(UriInfo uriInfo, BasicBD bd, Dominio entry) throws ConsoleException {
		URI modifica = this.getUriModifica(uriInfo, bd);
		InfoForm infoModifica = new InfoForm(modifica);

		String codDominioId = Utils.getInstance().getMessageFromResourceBundle(this.nomeServizio + ".codDominio.id");
		String ragioneSocialeId = Utils.getInstance().getMessageFromResourceBundle(this.nomeServizio + ".ragioneSociale.id");
		String abilitatoId = Utils.getInstance().getMessageFromResourceBundle(this.nomeServizio + ".abilitato.id");
		String dominioId = Utils.getInstance().getMessageFromResourceBundle(this.nomeServizio + ".id.id");
		String idStazioneId = Utils.getInstance().getMessageFromResourceBundle(this.nomeServizio + ".idStazione.id");
		String glnId = Utils.getInstance().getMessageFromResourceBundle(this.nomeServizio + ".gln.id");
		String uoIdId = Utils.getInstance().getMessageFromResourceBundle(this.nomeServizio + ".uoId.id");


		UnitaOperativeBD uoBD = new UnitaOperativeBD(bd);
		UnitaOperativa unitaOperativa = null;
		try {
			unitaOperativa = uoBD.getUnitaOperativa(entry.getId(), Dominio.EC);
		} catch (Exception e) {
			throw new ConsoleException(e);
		}

		AnagraficaHandler anagraficaHandler = new AnagraficaHandler(ANAGRAFICA_DOMINI,this.nomeServizio,this.pathServizio);
		List<ParamField<?>> infoCreazioneAnagrafica = anagraficaHandler.getInfoModificaAnagraficaDominio(uriInfo, bd,unitaOperativa.getAnagrafica());

		if(infoCreazioneMap == null){
			initInfoCreazione(uriInfo, bd);
		}

		Sezione sezioneRoot = infoModifica.getSezioneRoot();
		InputNumber idInterm = (InputNumber) infoCreazioneMap.get(dominioId);
		idInterm.setDefaultValue(entry.getId());
		sezioneRoot.addField(idInterm);
		InputNumber codDominio = (InputNumber) infoCreazioneMap.get(codDominioId);
		codDominio.setDefaultValue(Long.parseLong(entry.getCodDominio()));
		codDominio.setEditable(false); 
		sezioneRoot.addField(codDominio);
		
		InputNumber uoId = (InputNumber) infoCreazioneMap.get(uoIdId);
		uoId.setDefaultValue(unitaOperativa.getId());
		sezioneRoot.addField(uoId);

		List<Voce<Long>> stazioni = new ArrayList<Voce<Long>>();
		try{
			StazioniBD stazioniBD = new StazioniBD(bd);
			StazioneFilter filter = stazioniBD.newFilter();
			FilterSortWrapper fsw = new FilterSortWrapper();
			fsw.setField(it.govpay.orm.Stazione.model().COD_STAZIONE);
			fsw.setSortOrder(SortOrder.ASC);
			filter.getFilterSortList().add(fsw);

			List<Stazione> findAll = stazioniBD.findAll(filter);


			if(findAll != null && findAll.size() > 0){
				for (Stazione s : findAll) {
					stazioni.add(new Voce<Long>(s.getCodStazione(), s.getId()));
				}
			}
		}catch(Exception e){
			throw new ConsoleException(e);
		}

		SelectList<Long> stazione = (SelectList<Long>) infoCreazioneMap.get(idStazioneId);
		stazione.setDefaultValue(entry.getIdStazione());
		stazione.setValues(stazioni); 
		sezioneRoot.addField(stazione); 

		InputText ragioneSociale = (InputText) infoCreazioneMap.get(ragioneSocialeId);
		ragioneSociale.setDefaultValue(entry.getRagioneSociale());
		sezioneRoot.addField(ragioneSociale);
		InputText gln = (InputText) infoCreazioneMap.get(glnId);
		gln.setDefaultValue(entry.getGln());
		sezioneRoot.addField(gln);

		CheckButton abilitato = (CheckButton) infoCreazioneMap.get(abilitatoId);
		abilitato.setDefaultValue(entry.isAbilitato()); 
		sezioneRoot.addField(abilitato);

		Sezione sezioneAnagrafica = infoModifica.addSezione(Utils.getInstance().getMessageFromResourceBundle(this.nomeServizio + "." + ANAGRAFICA_DOMINI + ".titolo"));

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
		String methodName = "dettaglio " + this.titoloServizio + ".Id"+ id;

		try{
			log.info("Esecuzione " + methodName + " in corso...");
			// Operazione consentita solo all'amministratore
			this.darsService.checkOperatoreAdmin(bd);

			// recupero oggetto
			DominiBD dominiBD = new DominiBD(bd);
			Dominio dominio = dominiBD.getDominio(id);

			InfoForm infoModifica = this.getInfoModifica(uriInfo, bd,dominio);
			URI cancellazione = null;
			URI esportazione = null;

			Dettaglio dettaglio = new Dettaglio(this.getTitolo(dominio), esportazione, cancellazione, infoModifica);

			it.govpay.web.rs.dars.model.Sezione root = dettaglio.getSezioneRoot(); 

			// dati dell'dominio
			root.addVoce(Utils.getInstance().getMessageFromResourceBundle(this.nomeServizio + ".codDominio.label"), dominio.getCodDominio());
			root.addVoce(Utils.getInstance().getMessageFromResourceBundle(this.nomeServizio + ".idStazione.label"), dominio.getStazione(bd).getCodStazione());
			root.addVoce(Utils.getInstance().getMessageFromResourceBundle(this.nomeServizio + ".ragioneSociale.label"), dominio.getRagioneSociale());
			root.addVoce(Utils.getInstance().getMessageFromResourceBundle(this.nomeServizio + ".gln.label"), dominio.getGln());
			root.addVoce(Utils.getInstance().getMessageFromResourceBundle(this.nomeServizio + ".abilitato.label"), Utils.getSiNoAsLabel(dominio.isAbilitato()));

			// Sezione Anagrafica

			UnitaOperativeBD uoBD = new UnitaOperativeBD(bd);
			UnitaOperativa unitaOperativa = null;
			try {
				unitaOperativa = uoBD.getUnitaOperativa(dominio.getId(), Dominio.EC);
			} catch (Exception e) {
				throw new ConsoleException(e);
			}

			Anagrafica anagrafica = unitaOperativa.getAnagrafica(); 
			it.govpay.web.rs.dars.model.Sezione sezioneAnagrafica = dettaglio.addSezione(Utils.getInstance().getMessageFromResourceBundle(this.nomeServizio + "." + ANAGRAFICA_DOMINI + ".titolo"));
			AnagraficaHandler anagraficaHandler = new AnagraficaHandler(ANAGRAFICA_DOMINI,this.nomeServizio,this.pathServizio);
			anagraficaHandler.fillSezioneAnagraficaDominio(sezioneAnagrafica, anagrafica);

			// Elementi correlati
			String etichettaUnitaOperative = Utils.getInstance().getMessageFromResourceBundle(this.nomeServizio + ".elementoCorrelato.unitaOperative.titolo");
			String etichettaIban = Utils.getInstance().getMessageFromResourceBundle(this.nomeServizio + ".elementoCorrelato.iban.titolo");
			String etichettaTributi = Utils.getInstance().getMessageFromResourceBundle(this.nomeServizio + ".elementoCorrelato.tributi.titolo");
			String codDominioId = Utils.getInstance().getMessageFromResourceBundle(this.nomeServizio + ".codDominio.id");

			UnitaOperative uoDars =new UnitaOperative();
			UriBuilder uriBuilder = uriInfo.getBaseUriBuilder().path(uoDars.getPathServizio()).queryParam(codDominioId, dominio.getCodDominio());
			dettaglio.addElementoCorrelato(etichettaUnitaOperative, uriBuilder.build());

			Iban ibanDars =new Iban();
			UriBuilder uriBuilderIban = uriInfo.getBaseUriBuilder().path(ibanDars.getPathServizio()).queryParam(codDominioId, dominio.getCodDominio());
			dettaglio.addElementoCorrelato(etichettaIban, uriBuilderIban.build());

			Tributi tributiDars =new Tributi();
			UriBuilder uriBuilderTributi = uriInfo.getBaseUriBuilder().path(tributiDars.getPathServizio()).queryParam(codDominioId, dominio.getCodDominio());
			dettaglio.addElementoCorrelato(etichettaTributi, uriBuilderTributi.build());

			log.info("Esecuzione " + methodName + " completata.");

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
			log.info("Esecuzione " + methodName + " in corso...");
			// Operazione consentita solo all'amministratore
			this.darsService.checkOperatoreAdmin(bd);

			List<Object> lista = this.creaDominioEAnagrafica(is, uriInfo, bd);
			
			
			Dominio entry = (Dominio) lista.get(0);
			UnitaOperativa uo = (UnitaOperativa) lista.get(1); 

			this.checkEntry(entry, null);

			DominiBD dominiBD = new DominiBD(bd);

			try{
				dominiBD.getDominio(entry.getCodDominio());
				String msg = Utils.getInstance().getMessageWithParamsFromResourceBundle(this.nomeServizio + ".oggettoEsistente", entry.getCodDominio());
				throw new DuplicatedEntryException(msg);
			}catch(NotFoundException e){}

			UnitaOperativeBD uoBd = new UnitaOperativeBD(bd);
			
			// Inserimento di Dominio e UO in maniera transazionale.
			bd.setAutoCommit(false); 
			dominiBD.insertDominio(entry);
			uo.setIdDominio(entry.getId());
			uoBd.insertUnitaOperativa(uo);
			bd.commit();

			// ripristino l'autocommit.
			bd.setAutoCommit(true); 

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
	public Dominio creaEntry(InputStream is, UriInfo uriInfo, BasicBD bd)	throws WebApplicationException, ConsoleException {
		String methodName = "creaEntry " + this.titoloServizio;
		Dominio entry = null;
		try{
			log.info("Esecuzione " + methodName + " in corso...");
			// Operazione consentita solo all'amministratore
			this.darsService.checkOperatoreAdmin(bd);

			JsonConfig jsonConfig = new JsonConfig();
			ByteArrayOutputStream baos = new ByteArrayOutputStream();
			Utils.copy(is, baos);

			baos.flush();
			baos.close();
			
			JSONObject jsonObjectDominio = JSONObject.fromObject( baos.toString() );
			jsonConfig.setRootClass(Dominio.class);
			entry = (Dominio) JSONObject.toBean( jsonObjectDominio, jsonConfig );
			
			

			//[TODO] Scegliere dei valori corretti per xml_conti_accredito e xml_tabella_controparti
			byte val [] = "".getBytes();
			entry.setTabellaControparti(val);
			entry.setContiAccredito(val);

			log.info("Esecuzione " + methodName + " completata.");
			return entry;
		}catch(WebApplicationException e){
			throw e;
		}catch(Exception e){
			throw new ConsoleException(e);
		}
	}

	public List<Object> creaDominioEAnagrafica(InputStream is, UriInfo uriInfo, BasicBD bd)	throws WebApplicationException, ConsoleException {
		String methodName = "creaDominioEAnagrafica " + this.titoloServizio;
		List<Object> list = new ArrayList<Object>();
		try{
			log.info("Esecuzione " + methodName + " in corso...");
			// Operazione consentita solo all'amministratore
			this.darsService.checkOperatoreAdmin(bd);

			JsonConfig jsonConfig = new JsonConfig();
			ByteArrayOutputStream baos = new ByteArrayOutputStream();
			Utils.copy(is, baos);

			baos.flush();
			baos.close();

			JSONObject jsonObjectDominio = JSONObject.fromObject( baos.toString() );  
			String ragioneSocialeId = Utils.getInstance().getMessageFromResourceBundle(this.nomeServizio + ".ragioneSociale.id");
			JSONArray jsonArray = jsonObjectDominio.getJSONArray(ragioneSocialeId);
			
			String ragSocDominio = jsonArray.getString(0);
			String ragSocAnagrafica = jsonArray.getString(1);
			
			jsonObjectDominio.remove(ragioneSocialeId);
			
			jsonConfig.setRootClass(Dominio.class);
			
			Dominio  entry = (Dominio) JSONObject.toBean( jsonObjectDominio, jsonConfig );
			//[TODO] Scegliere dei valori corretti per xml_conti_accredito e xml_tabella_controparti
			byte val [] = "".getBytes();
			entry.setTabellaControparti(val);
			entry.setContiAccredito(val);
			
			entry.setRagioneSociale(ragSocDominio);
			
			jsonConfig.setRootClass(Anagrafica.class);
			Anagrafica anagrafica = (Anagrafica) JSONObject.toBean( jsonObjectDominio, jsonConfig );
			anagrafica.setRagioneSociale(ragSocAnagrafica);   
			
			String uoIdId = Utils.getInstance().getMessageFromResourceBundle(this.nomeServizio + ".uoId.id");
			String uoId = jsonObjectDominio.getString(uoIdId);
			Long uoIdLong = null;
			if(StringUtils.isNotEmpty(uoId)){
				try{
					uoIdLong = Long.parseLong(uoId);
				}catch(Exception e){ uoIdLong = null;}
			}

			UnitaOperativa uo = new UnitaOperativa();
			uo.setAbilitato(true);
			uo.setAnagrafica(anagrafica);
			uo.setCodUo(Dominio.EC);
			if(entry.getId() != null)
				uo.setIdDominio(entry.getId()); 
			uo.setId(uoIdLong); 

			list.add(entry);
			list.add(uo);

			log.info("Esecuzione " + methodName + " completata.");
			return list;
		}catch(WebApplicationException e){
			throw e;
		}catch(Exception e){
			throw new ConsoleException(e);
		}
	}


	@Override
	public void checkEntry(Dominio entry, Dominio oldEntry) throws ValidationException {
		if(entry == null || entry.getCodDominio() == null || entry.getCodDominio().length() != 11) {
			int codIntSize = (entry != null && entry.getCodDominio() != null) ? entry.getCodDominio().length() : 0;
			throw new ValidationException("Lunghezza del IdDominio errata. Richieste 11 cifre, trovate "+codIntSize);
		}
		try { 
			Long.parseLong(entry.getCodDominio());
		} catch (NumberFormatException e) {
			throw new ValidationException("Formato CodDominio errato. Richieste 11 cifre, trovato "+entry.getCodDominio());
		}

		if(entry.getRagioneSociale() == null || entry.getRagioneSociale().isEmpty()) throw new ValidationException("Il campo Ragione Sociale deve essere valorizzato.");

		if(oldEntry != null) { //caso update
			if(!oldEntry.getCodDominio().equals(entry.getCodDominio())) throw new ValidationException("Cod Dominio non deve cambiare in update. Atteso ["+oldEntry.getCodDominio()+"] trovato ["+entry.getCodDominio()+"]");
		}
	}

	@Override
	public Dettaglio update(InputStream is, UriInfo uriInfo, BasicBD bd)
			throws WebApplicationException, ConsoleException, ValidationException {
		String methodName = "Update " + this.titoloServizio;

		try{
			log.info("Esecuzione " + methodName + " in corso...");
			// Operazione consentita solo all'amministratore
			this.darsService.checkOperatoreAdmin(bd);

			Dominio entry = this.creaEntry(is, uriInfo, bd);

			DominiBD dominiBD = new DominiBD(bd);
			Dominio oldEntry = dominiBD.getDominio(entry.getCodDominio());

			this.checkEntry(entry, oldEntry);

			dominiBD.updateDominio(entry); 

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
	public String getTitolo(Dominio entry) {
		StringBuilder sb = new StringBuilder();

		sb.append(entry.getRagioneSociale());
		sb.append(" (").append(entry.getCodDominio()).append(")");
		return sb.toString();
	}

	public Elemento getElemento(Dominio entry, Long id, UriBuilder uriDettaglioBuilder, BasicBD bd) throws Exception{
		String titolo = this.getTitolo(entry);

		StringBuilder sb = new StringBuilder();

		sb.append(Utils.getAbilitatoAsLabel(entry.isAbilitato()));
		sb.append(", Stazione: ").append(entry.getStazione(bd).getCodStazione());

		String sottotitolo = sb.toString();
		URI urlDettaglio = id != null ?  uriDettaglioBuilder.build(id) : null;
		Elemento elemento = new Elemento(id, titolo, sottotitolo, urlDettaglio);
		return elemento;
	}

	@Override
	public String getSottotitolo(Dominio entry) {
		StringBuilder sb = new StringBuilder();

		sb.append(Utils.getAbilitatoAsLabel(entry.isAbilitato()));

		return sb.toString();
	}


}
