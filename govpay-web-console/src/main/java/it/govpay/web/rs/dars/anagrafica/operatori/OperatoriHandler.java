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
import javax.ws.rs.core.UriBuilder;
import javax.ws.rs.core.UriInfo;

import org.apache.commons.lang.StringUtils;
import org.apache.logging.log4j.Logger;
import org.openspcoop2.generic_project.exception.NotFoundException;
import org.openspcoop2.generic_project.expression.SortOrder;

import it.govpay.bd.BasicBD;
import it.govpay.bd.FilterSortWrapper;
import it.govpay.bd.anagrafica.ApplicazioniBD;
import it.govpay.bd.anagrafica.OperatoriBD;
import it.govpay.bd.anagrafica.UnitaOperativeBD;
import it.govpay.bd.anagrafica.filters.ApplicazioneFilter;
import it.govpay.bd.anagrafica.filters.OperatoreFilter;
import it.govpay.bd.anagrafica.filters.UnitaOperativaFilter;
import it.govpay.bd.model.Applicazione;
import it.govpay.bd.model.Operatore;
import it.govpay.bd.model.Operatore.ProfiloOperatore;
import it.govpay.bd.model.UnitaOperativa;
import it.govpay.web.rs.BaseRsService;
import it.govpay.web.rs.dars.BaseDarsHandler;
import it.govpay.web.rs.dars.BaseDarsService;
import it.govpay.web.rs.dars.IDarsHandler;
import it.govpay.web.rs.dars.anagrafica.applicazioni.ApplicazioniHandler;
import it.govpay.web.rs.dars.anagrafica.operatori.input.Applicazioni;
import it.govpay.web.rs.dars.anagrafica.operatori.input.UnitaOperative;
import it.govpay.web.rs.dars.anagrafica.uo.UnitaOperativeHandler;
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

public class OperatoriHandler extends BaseDarsHandler<Operatore> implements IDarsHandler<Operatore>{

	private static Map<String, ParamField<?>> infoCreazioneMap = null;
	private static Map<String, ParamField<?>> infoRicercaMap = null;

	public static final String PROFILO_OPERATORE_VALUE_ADMIN = "ADMIN";
	public static final String PROFILO_OPERATORE_VALUE_OPERATORE = "ENTE";

	public OperatoriHandler(Logger log, BaseDarsService darsService) {
		super(log,darsService);
	}

	@Override
	public Elenco getElenco(UriInfo uriInfo,BasicBD bd) throws WebApplicationException,ConsoleException {
		String methodName = "getElenco " + this.titoloServizio;

		try{
			this.log.info("Esecuzione " + methodName + " in corso..."); 
			// Operazione consentita solo all'amministratore
			this.darsService.checkOperatoreAdmin(bd);

			Integer offset = this.getOffset(uriInfo);
			Integer limit = this.getLimit(uriInfo);
			URI esportazione = null;
			URI cancellazione = null;

			OperatoriBD operatoriBD = new OperatoriBD(bd);
			OperatoreFilter filter = operatoriBD.newFilter();
			filter.setOffset(offset);
			filter.setLimit(limit);
			FilterSortWrapper fsw = new FilterSortWrapper();
			fsw.setField(it.govpay.orm.Operatore.model().PRINCIPAL);
			fsw.setSortOrder(SortOrder.ASC);
			filter.getFilterSortList().add(fsw);
			
			String principalId = Utils.getInstance().getMessageFromResourceBundle(this.nomeServizio + ".principal.id");
			String principal = this.getParameter(uriInfo, principalId, String.class);
			String profiloId = Utils.getInstance().getMessageFromResourceBundle(this.nomeServizio + ".profilo.id");
			String profiloValue = this.getParameter(uriInfo, profiloId, String.class);
			
			if(StringUtils.isNotEmpty(principal)){
				filter.setPrincipal(principal);
			}
			
			if(StringUtils.isNotEmpty(profiloValue)){
				ProfiloOperatore profilo = ProfiloOperatore.valueOf(profiloValue);
				filter.setProfilo(profilo);
			}

			long count = operatoriBD.count(filter);
			
			// visualizza la ricerca solo se i risultati sono > del limit
			boolean visualizzaRicerca = this.visualizzaRicerca(count, limit);
			InfoForm infoRicerca = visualizzaRicerca ? this.getInfoRicerca(uriInfo, bd) : null;

			Elenco elenco = new Elenco(this.titoloServizio, infoRicerca,
					this.getInfoCreazione(uriInfo, bd),
					count, esportazione, cancellazione); 

			UriBuilder uriDettaglioBuilder = BaseRsService.checkDarsURI(uriInfo).path(this.pathServizio).path("{id}");

			List<Operatore> findAll = operatoriBD.findAll(filter);

			if(findAll != null && findAll.size() > 0){
				for (Operatore entry : findAll) {
					elenco.getElenco().add(this.getElemento(entry, entry.getId(), uriDettaglioBuilder));
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
	public InfoForm getInfoRicerca(UriInfo uriInfo, BasicBD bd) throws ConsoleException {
		URI ricerca = this.getUriRicerca(uriInfo, bd);
		InfoForm infoRicerca = new InfoForm(ricerca);

		String principalId = Utils.getInstance().getMessageFromResourceBundle(this.nomeServizio + ".principal.id");
		String profiloId = Utils.getInstance().getMessageFromResourceBundle(this.nomeServizio + ".profilo.id");

		if(infoRicercaMap == null){
			this.initInfoRicerca(uriInfo, bd);

		}


		Sezione sezioneRoot = infoRicerca.getSezioneRoot();
		InputText principal  = (InputText) infoRicercaMap.get(principalId);
		sezioneRoot.addField(principal);
		
		SelectList<String> profilo = (SelectList<String>) infoRicercaMap.get(profiloId);
		String profiloOperatoreValue = "";
		profilo.setDefaultValue(profiloOperatoreValue);
		sezioneRoot.addField(profilo); 
		
		
		return infoRicerca;
	}

	private void initInfoRicerca(UriInfo uriInfo, BasicBD bd) throws ConsoleException{
		if(infoRicercaMap == null){
			infoRicercaMap = new HashMap<String, ParamField<?>>();

			String principalId = Utils.getInstance().getMessageFromResourceBundle(this.nomeServizio + ".principal.id");
			String profiloId = Utils.getInstance().getMessageFromResourceBundle(this.nomeServizio + ".profilo.id");

			// principal
			String principalLabel = Utils.getInstance().getMessageFromResourceBundle(this.nomeServizio + ".principal.label");
			InputText principal = new InputText(principalId, principalLabel, null, false, false, true, 1, 50);
			infoRicercaMap.put(principalId, principal);

			// profilo
			String profiloLabel = Utils.getInstance().getMessageFromResourceBundle(this.nomeServizio + ".profilo.label");
			List<Voce<String>> tipiProfili = new ArrayList<Voce<String>>();
			tipiProfili.add(new Voce<String>(Utils.getInstance().getMessageFromResourceBundle("commons.label.qualsiasi"),""));
			tipiProfili.add(new Voce<String>(Utils.getInstance().getMessageFromResourceBundle(this.nomeServizio + ".profilo.admin"),PROFILO_OPERATORE_VALUE_ADMIN));
			tipiProfili.add(new Voce<String>(Utils.getInstance().getMessageFromResourceBundle(this.nomeServizio + ".profilo.ente"),PROFILO_OPERATORE_VALUE_OPERATORE));

			SelectList<String> profilo = new SelectList<String>(profiloId, profiloLabel, PROFILO_OPERATORE_VALUE_ADMIN, false, false, true, tipiProfili);
			infoRicercaMap.put(profiloId, profilo);

		}
	}

	@SuppressWarnings("unchecked")
	@Override
	public InfoForm getInfoCreazione(UriInfo uriInfo, BasicBD bd) throws ConsoleException {
		URI creazione = this.getUriCreazione(uriInfo, bd);
		InfoForm infoCreazione = new InfoForm(creazione);

		String operatoreId = Utils.getInstance().getMessageFromResourceBundle(this.nomeServizio + ".id.id");
		String principalId = Utils.getInstance().getMessageFromResourceBundle(this.nomeServizio + ".principal.id");
		String nomeId = Utils.getInstance().getMessageFromResourceBundle(this.nomeServizio + ".nome.id");
		String profiloId = Utils.getInstance().getMessageFromResourceBundle(this.nomeServizio + ".profilo.id");
		String abilitatoId = Utils.getInstance().getMessageFromResourceBundle(this.nomeServizio + ".abilitato.id");
		String applicazioniId = Utils.getInstance().getMessageFromResourceBundle(this.nomeServizio + ".applicazioni.id");
		String uoId = Utils.getInstance().getMessageFromResourceBundle(this.nomeServizio + ".uo.id");


		if(infoCreazioneMap == null){
			this.initInfoCreazione(uriInfo, bd);
		}

		Sezione sezioneRoot = infoCreazione.getSezioneRoot();

		InputNumber idInterm = (InputNumber) infoCreazioneMap.get(operatoreId);
		idInterm.setDefaultValue(null);
		sezioneRoot.addField(idInterm);

		InputText principal  = (InputText) infoCreazioneMap.get(principalId);
		principal.setDefaultValue(null);
		principal.setEditable(true); 
		sezioneRoot.addField(principal);

		InputText nome = (InputText) infoCreazioneMap.get(nomeId);
		nome.setDefaultValue(null);
		sezioneRoot.addField(nome);

		SelectList<String> profilo = (SelectList<String>) infoCreazioneMap.get(profiloId);
		String profiloOperatoreValue = PROFILO_OPERATORE_VALUE_ADMIN;
		profilo.setDefaultValue(profiloOperatoreValue);
		sezioneRoot.addField(profilo); 

		List<RawParamValue> tipoAutenticazioneValues = new ArrayList<RawParamValue>();
		tipoAutenticazioneValues.add(new RawParamValue(operatoreId, null));
		tipoAutenticazioneValues.add(new RawParamValue(profiloId, PROFILO_OPERATORE_VALUE_ADMIN));

		UnitaOperative uo = (UnitaOperative) infoCreazioneMap.get(uoId);
		uo.init(tipoAutenticazioneValues, bd); 
		sezioneRoot.addField(uo);

		Applicazioni applicazioni = (Applicazioni) infoCreazioneMap.get(applicazioniId);
		applicazioni.init(tipoAutenticazioneValues, bd); 
		sezioneRoot.addField(applicazioni); 

		CheckButton abilitato = (CheckButton) infoCreazioneMap.get(abilitatoId);
		abilitato.setDefaultValue(true); 
		sezioneRoot.addField(abilitato);


		return infoCreazione;
	}

	private void initInfoCreazione(UriInfo uriInfo, BasicBD bd) throws ConsoleException{
		if(infoCreazioneMap == null){
			infoCreazioneMap = new HashMap<String, ParamField<?>>();

			String abilitatoId = Utils.getInstance().getMessageFromResourceBundle(this.nomeServizio + ".abilitato.id");
			String principalId = Utils.getInstance().getMessageFromResourceBundle(this.nomeServizio + ".principal.id");
			String nomeId = Utils.getInstance().getMessageFromResourceBundle(this.nomeServizio + ".nome.id");
			String profiloId = Utils.getInstance().getMessageFromResourceBundle(this.nomeServizio + ".profilo.id");
			String operatoreId = Utils.getInstance().getMessageFromResourceBundle(this.nomeServizio + ".id.id");
			String applicazioniId = Utils.getInstance().getMessageFromResourceBundle(this.nomeServizio + ".applicazioni.id");
			String uoId = Utils.getInstance().getMessageFromResourceBundle(this.nomeServizio + ".uo.id");

			// id 
			InputNumber id = new InputNumber(operatoreId, null, null, true, true, false, 1, 20);
			infoCreazioneMap.put(operatoreId, id);

			// principal
			String principalLabel = Utils.getInstance().getMessageFromResourceBundle(this.nomeServizio + ".principal.label");
			InputText principal = new InputText(principalId, principalLabel, null, true, false, true, 1, 50);
			principal.setValidation(null, Utils.getInstance().getMessageFromResourceBundle(this.nomeServizio + ".principal.errorMessage"));
			infoCreazioneMap.put(principalId, principal);

			// nome
			String nomeLabel = Utils.getInstance().getMessageFromResourceBundle(this.nomeServizio + ".nome.label");
			InputText nome = new InputText(nomeId, nomeLabel, null, true, false, true, 1, 255);
			nome.setValidation(null, Utils.getInstance().getMessageFromResourceBundle(this.nomeServizio + ".nome.errorMessage"));
			infoCreazioneMap.put(nomeId, nome);

			// profilo
			// tipo autenticazione
			String profiloLabel = Utils.getInstance().getMessageFromResourceBundle(this.nomeServizio + ".profilo.label");
			List<Voce<String>> tipiProfili = new ArrayList<Voce<String>>();
			tipiProfili.add(new Voce<String>(Utils.getInstance().getMessageFromResourceBundle(this.nomeServizio + ".profilo.admin"),PROFILO_OPERATORE_VALUE_ADMIN));
			tipiProfili.add(new Voce<String>(Utils.getInstance().getMessageFromResourceBundle(this.nomeServizio + ".profilo.ente"),PROFILO_OPERATORE_VALUE_OPERATORE));

			SelectList<String> profilo = new SelectList<String>(profiloId, profiloLabel, PROFILO_OPERATORE_VALUE_ADMIN, true, false, true, tipiProfili);
			infoCreazioneMap.put(profiloId, profilo);

			List<RawParamValue> tipoAutenticazioneValues = new ArrayList<RawParamValue>();
			tipoAutenticazioneValues.add(new RawParamValue(operatoreId, null));
			tipoAutenticazioneValues.add(new RawParamValue(profiloId, PROFILO_OPERATORE_VALUE_ADMIN));

			// abilitato
			String abilitatoLabel = Utils.getInstance().getMessageFromResourceBundle(this.nomeServizio + ".abilitato.label");
			CheckButton abiliato = new CheckButton(abilitatoId, abilitatoLabel, true, false, false, true);
			infoCreazioneMap.put(abilitatoId, abiliato);

			String uoLabel = Utils.getInstance().getMessageFromResourceBundle(this.nomeServizio + ".uo.label");
			URI uoRefreshUri = this.getUriField(uriInfo, bd, uoId); 
			UnitaOperative uo = new UnitaOperative(this.nomeServizio, uoId, uoLabel, uoRefreshUri , tipoAutenticazioneValues, bd);
			uo.addDependencyField(profilo);
			uo.init(tipoAutenticazioneValues, bd); 
			infoCreazioneMap.put(uoId, uo);

			String applicazioniLabel = Utils.getInstance().getMessageFromResourceBundle(this.nomeServizio + ".applicazioni.label");
			URI applicazioniRefreshUri = this.getUriField(uriInfo, bd, applicazioniId); 
			Applicazioni applicazioni = new Applicazioni(this.nomeServizio, applicazioniId, applicazioniLabel, applicazioniRefreshUri , tipoAutenticazioneValues, bd);
			applicazioni.addDependencyField(profilo);
			applicazioni.init(tipoAutenticazioneValues, bd); 
			infoCreazioneMap.put(applicazioniId, applicazioni);

		}
	}

	@SuppressWarnings("unchecked")
	@Override
	public InfoForm getInfoModifica(UriInfo uriInfo, BasicBD bd, Operatore entry) throws ConsoleException {
		URI modifica = this.getUriModifica(uriInfo, bd);
		InfoForm infoModifica = new InfoForm(modifica);

		String abilitatoId = Utils.getInstance().getMessageFromResourceBundle(this.nomeServizio + ".abilitato.id");
		String principalId = Utils.getInstance().getMessageFromResourceBundle(this.nomeServizio + ".principal.id");
		String nomeId = Utils.getInstance().getMessageFromResourceBundle(this.nomeServizio + ".nome.id");
		String profiloId = Utils.getInstance().getMessageFromResourceBundle(this.nomeServizio + ".profilo.id");
		String operatoreId = Utils.getInstance().getMessageFromResourceBundle(this.nomeServizio + ".id.id");
		String applicazioniId = Utils.getInstance().getMessageFromResourceBundle(this.nomeServizio + ".applicazioni.id");
		String uoId = Utils.getInstance().getMessageFromResourceBundle(this.nomeServizio + ".uo.id");

		if(infoCreazioneMap == null){
			this.initInfoCreazione(uriInfo, bd);
		}

		Sezione sezioneRoot = infoModifica.getSezioneRoot();
		InputNumber idInterm = (InputNumber) infoCreazioneMap.get(operatoreId);
		idInterm.setDefaultValue(entry.getId());
		sezioneRoot.addField(idInterm);

		InputText principal  = (InputText) infoCreazioneMap.get(principalId);
		principal.setDefaultValue(entry.getPrincipal());
		principal.setEditable(false); 
		sezioneRoot.addField(principal);

		InputText nome = (InputText) infoCreazioneMap.get(nomeId);
		nome.setDefaultValue(entry.getNome());
		sezioneRoot.addField(nome);

		SelectList<String> profilo = (SelectList<String>) infoCreazioneMap.get(profiloId);
		String profiloOperatoreValue = null;
		ProfiloOperatore profiloOperatore = entry.getProfilo();
		switch (profiloOperatore) {
		case ADMIN:
			profiloOperatoreValue = PROFILO_OPERATORE_VALUE_ADMIN;
			break;
		case ENTE:
		default:
			profiloOperatoreValue = PROFILO_OPERATORE_VALUE_OPERATORE;
			break;
		}

		profilo.setDefaultValue(profiloOperatoreValue);
		sezioneRoot.addField(profilo); 

		List<RawParamValue> tipoAutenticazioneValues = new ArrayList<RawParamValue>();
		tipoAutenticazioneValues.add(new RawParamValue(operatoreId, entry.getId()+""));
		tipoAutenticazioneValues.add(new RawParamValue(profiloId, profiloOperatoreValue)); 

		UnitaOperative uo = (UnitaOperative) infoCreazioneMap.get(uoId);
		uo.init(tipoAutenticazioneValues, bd); 
		sezioneRoot.addField(uo);

		Applicazioni applicazioni = (Applicazioni) infoCreazioneMap.get(applicazioniId);
		applicazioni.init(tipoAutenticazioneValues, bd); 
		sezioneRoot.addField(applicazioni); 

		CheckButton abilitato = (CheckButton) infoCreazioneMap.get(abilitatoId);
		abilitato.setDefaultValue(entry.isAbilitato()); 
		sezioneRoot.addField(abilitato);

		return infoModifica;
	}


	@Override
	public Object getField(UriInfo uriInfo,List<RawParamValue>values, String fieldId,BasicBD bd) throws WebApplicationException,ConsoleException {
		try{
			this.log.debug("Richiesto field ["+fieldId+"]"); 
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
			return null;
		}catch(Exception e){
			throw new ConsoleException(e);
		}
	}


	@Override
	public Dettaglio getDettaglio(long id, UriInfo uriInfo, BasicBD bd) throws WebApplicationException,ConsoleException {
		String methodName = "dettaglio " + this.titoloServizio + "."+ id;
		try{
			this.log.info("Esecuzione " + methodName + " in corso...");

			// Operazione consentita solo all'amministratore
			this.darsService.checkOperatoreAdmin(bd);

			// recupero oggetto
			OperatoriBD operatoriBD = new OperatoriBD(bd);
			Operatore operatore = operatoriBD.getOperatore(id);

			ProfiloOperatore profilo = operatore.getProfilo();
			String profiloValue = "";
			boolean isAdmin = false;
			switch(profilo){
			case ADMIN:
				profiloValue = Utils.getInstance().getMessageFromResourceBundle(this.nomeServizio + ".profilo.admin");
				isAdmin = true;
				break;
			case ENTE:
				profiloValue = Utils.getInstance().getMessageFromResourceBundle(this.nomeServizio + ".profilo.ente");
			default: break;
			}

			InfoForm infoModifica = this.getInfoModifica(uriInfo, bd,operatore);
			URI cancellazione = null;
			URI esportazione = null;

			Dettaglio dettaglio = new Dettaglio(this.getTitolo(operatore), esportazione, cancellazione, infoModifica);

			it.govpay.web.rs.dars.model.Sezione root = dettaglio.getSezioneRoot(); 

			// dati del psp
			root.addVoce(Utils.getInstance().getMessageFromResourceBundle(this.nomeServizio + ".principal.label"), operatore.getPrincipal());
			root.addVoce(Utils.getInstance().getMessageFromResourceBundle(this.nomeServizio + ".nome.label"), operatore.getNome());
			root.addVoce(Utils.getInstance().getMessageFromResourceBundle(this.nomeServizio + ".profilo.label"), profiloValue);
			root.addVoce(Utils.getInstance().getMessageFromResourceBundle(this.nomeServizio + ".abilitato.label"), Utils.getAbilitatoAsLabel(operatore.isAbilitato()));

			if(!isAdmin){
				// Elementi correlati dell'operatore UO e Applicazioni
				String etichettaUnitaOperative = Utils.getInstance().getMessageFromResourceBundle(this.nomeServizio + ".elementoCorrelato.uo.titolo");
				it.govpay.web.rs.dars.model.Sezione sezioneUo = dettaglio.addSezione(etichettaUnitaOperative);
				
				if(!Utils.isEmpty(operatore.getIdEnti())){
					UnitaOperativeBD unitaOperativaBD = new UnitaOperativeBD(bd);
					UnitaOperativaFilter filter = unitaOperativaBD.newFilter();
					FilterSortWrapper fsw = new FilterSortWrapper();
					fsw.setField(it.govpay.orm.Uo.model().COD_UO);
					fsw.setSortOrder(SortOrder.ASC);
					filter.getFilterSortList().add(fsw);
					filter.setListaIdUo(operatore.getIdEnti());
					// tutte le unita' con codice uo = 'EC' sono nascoste
					filter.setExcludeEC(true); 
					
					List<UnitaOperativa> findAll =  unitaOperativaBD.findAll(filter);

						
					
					it.govpay.web.rs.dars.anagrafica.uo.UnitaOperative uoDars = new it.govpay.web.rs.dars.anagrafica.uo.UnitaOperative();
					UnitaOperativeHandler uoDarsHandler = (UnitaOperativeHandler) uoDars.getDarsHandler();
					UriBuilder uriDettaglioUoBuilder = BaseRsService.checkDarsURI(uriInfo).path(uoDars.getPathServizio()).path("{id}");

					if(findAll != null && findAll.size() > 0){
						for (UnitaOperativa entry : findAll) {
							Elemento elemento = uoDarsHandler.getElemento(entry, entry.getId(), uriDettaglioUoBuilder);
							sezioneUo.addVoce(elemento.getTitolo(), elemento.getSottotitolo(), elemento.getUri());
						}
					}
					
				}
				
				String etichettaApplicazioni = Utils.getInstance().getMessageFromResourceBundle(this.nomeServizio + ".elementoCorrelato.applicazioni.titolo");
				it.govpay.web.rs.dars.model.Sezione sezioneApplicazioni = dettaglio.addSezione(etichettaApplicazioni);
				
				if(!Utils.isEmpty(operatore.getIdApplicazioni())){
					ApplicazioniBD applicazioniBD = new ApplicazioniBD(bd);
					ApplicazioneFilter filter = applicazioniBD.newFilter();
					FilterSortWrapper fsw = new FilterSortWrapper();
					fsw.setField(it.govpay.orm.Applicazione.model().COD_APPLICAZIONE);
					fsw.setSortOrder(SortOrder.ASC);
					filter.getFilterSortList().add(fsw);
					filter.setListaIdApplicazioni(operatore.getIdApplicazioni());
					
					List<Applicazione> findAll =  applicazioniBD.findAll(filter);

						
					
					it.govpay.web.rs.dars.anagrafica.applicazioni.Applicazioni applicazioniDars = new it.govpay.web.rs.dars.anagrafica.applicazioni.Applicazioni();
					ApplicazioniHandler applicazioniDarsHandler = (ApplicazioniHandler) applicazioniDars.getDarsHandler();
					UriBuilder uriDettaglioApplicazioniBuilder = BaseRsService.checkDarsURI(uriInfo).path(applicazioniDars.getPathServizio()).path("{id}");

					if(findAll != null && findAll.size() > 0){
						for (Applicazione entry : findAll) {
							Elemento elemento = applicazioniDarsHandler.getElemento(entry, entry.getId(), uriDettaglioApplicazioniBuilder);
							sezioneApplicazioni.addVoce(elemento.getTitolo(), elemento.getSottotitolo(), elemento.getUri());
						}
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

			Operatore entry = this.creaEntry(is, uriInfo, bd);

			this.checkEntry(entry, null);

			OperatoriBD operatoriBD = new OperatoriBD(bd);

			try{
				operatoriBD.getOperatore(entry.getPrincipal());
				String msg = Utils.getInstance().getMessageWithParamsFromResourceBundle(this.nomeServizio + ".oggettoEsistente", entry.getPrincipal());
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
			// Operazione consentita solo all'amministratore
			this.darsService.checkOperatoreAdmin(bd);

			JsonConfig jsonConfig = new JsonConfig();
			ByteArrayOutputStream baos = new ByteArrayOutputStream();
			Utils.copy(is, baos);

			baos.flush();
			baos.close();
			
			Map<String,Class<?>> classMap = new HashMap<String, Class<?>>();
			classMap.put("idApplicazioni", Long.class); 
			classMap.put("idEnti", Long.class); 
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
		if(entry.getPrincipal() == null || entry.getPrincipal().isEmpty())   throw new ValidationException("E' necessario valorizzare il campo Principal");
		if(entry.getPrincipal().contains(" ")) throw new ValidationException("Principal non valida. Caratteri blank non ammessi");

		if(entry != null && entry.getPrincipal() != null && entry.getPrincipal().length() > 255) 
			throw new ValidationException("Il campo Principal non puo' essere piu' lungo di 255 caratteri."); 

		if(entry != null && entry.getNome() != null && entry.getNome().length() > 255)  
			throw new ValidationException("Il campo Nome non puo' essere piu' lungo di 255 caratteri."); 

		if(oldEntry != null) {
			if(!entry.getPrincipal().equals(oldEntry.getPrincipal())) throw new ValidationException("Il campo Principal non e' modificabile");
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
	public void delete(List<Long> idsToDelete, UriInfo uriInfo, BasicBD bd) throws ConsoleException {

	}


	@Override
	public String getTitolo(Operatore entry) {
		return entry.getPrincipal();
	}

	@Override
	public String getSottotitolo(Operatore entry) {
		StringBuilder sb = new StringBuilder();

		sb.append(Utils.getAbilitatoAsLabel(entry.isAbilitato()));
		sb.append(", Ruolo: ");
		ProfiloOperatore profilo = entry.getProfilo();
		if(profilo.equals(ProfiloOperatore.ADMIN))
			sb.append("Amministratore");
		else 
			sb.append("Operatore");

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
