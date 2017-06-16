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
import org.openspcoop2.generic_project.expression.SortOrder;

import it.govpay.bd.BasicBD;
import it.govpay.bd.FilterSortWrapper;
import it.govpay.bd.anagrafica.DominiBD;
import it.govpay.bd.anagrafica.OperatoriBD;
import it.govpay.bd.anagrafica.filters.DominioFilter;
import it.govpay.bd.anagrafica.filters.OperatoreFilter;
import it.govpay.bd.model.Dominio;
import it.govpay.model.Acl;
import it.govpay.model.Acl.Servizio;
import it.govpay.model.Acl.Tipo;
import it.govpay.model.Operatore;
import it.govpay.model.Operatore.ProfiloOperatore;
import it.govpay.web.rs.dars.anagrafica.domini.DominiHandler;
import it.govpay.web.rs.dars.anagrafica.operatori.input.Domini;
import it.govpay.web.rs.dars.base.DarsHandler;
import it.govpay.web.rs.dars.base.DarsService;
import it.govpay.web.rs.dars.exception.ConsoleException;
import it.govpay.web.rs.dars.exception.DeleteException;
import it.govpay.web.rs.dars.exception.DuplicatedEntryException;
import it.govpay.web.rs.dars.exception.ExportException;
import it.govpay.web.rs.dars.exception.ValidationException;
import it.govpay.web.rs.dars.handler.IDarsHandler;
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

public class OperatoriHandler extends DarsHandler<Operatore> implements IDarsHandler<Operatore>{

	private static final Servizio TIPO_SERVIZIO = Servizio.CRUSCOTTO;

	public static final String PROFILO_OPERATORE_VALUE_ADMIN = "ADMIN";
	public static final String PROFILO_OPERATORE_VALUE_OPERATORE = "ENTE";

	public OperatoriHandler(Logger log, DarsService darsService) {
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

			boolean simpleSearch = this.containsParameter(uriInfo, DarsService.SIMPLE_SEARCH_PARAMETER_ID);

			OperatoriBD operatoriBD = new OperatoriBD(bd);
			OperatoreFilter filter = operatoriBD.newFilter(simpleSearch);
			filter.setOffset(offset);
			filter.setLimit(limit);
			FilterSortWrapper fsw = new FilterSortWrapper();
			fsw.setField(it.govpay.orm.Operatore.model().PRINCIPAL);
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
				String profiloId = Utils.getInstance(this.getLanguage()).getMessageFromResourceBundle(this.nomeServizio + ".profilo.id");
				String profiloValue = this.getParameter(uriInfo, profiloId, String.class);

				if(StringUtils.isNotEmpty(principal)){
					filter.setPrincipal(principal);
				}

				if(StringUtils.isNotEmpty(profiloValue)){
					ProfiloOperatore profilo = ProfiloOperatore.valueOf(profiloValue);
					filter.setProfilo(profilo);
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
			String profiloId = Utils.getInstance(this.getLanguage()).getMessageFromResourceBundle(this.nomeServizio + ".profilo.id");

			if(this.infoRicercaMap == null){
				this.initInfoRicerca(uriInfo, bd);

			}


			Sezione sezioneRoot = infoRicerca.getSezioneRoot();
			InputText principal  = (InputText) this.infoRicercaMap.get(principalId);
			sezioneRoot.addField(principal);

			SelectList<String> profilo = (SelectList<String>) this.infoRicercaMap.get(profiloId);
			String profiloOperatoreValue = "";
			profilo.setDefaultValue(profiloOperatoreValue);
			sezioneRoot.addField(profilo); 

		}
		return infoRicerca;
	}

	private void initInfoRicerca(UriInfo uriInfo, BasicBD bd) throws ConsoleException{
		if(this.infoRicercaMap == null){
			this.infoRicercaMap = new HashMap<String, ParamField<?>>();

			String principalId = Utils.getInstance(this.getLanguage()).getMessageFromResourceBundle(this.nomeServizio + ".principal.id");
			String profiloId = Utils.getInstance(this.getLanguage()).getMessageFromResourceBundle(this.nomeServizio + ".profilo.id");

			// principal
			String principalLabel = Utils.getInstance(this.getLanguage()).getMessageFromResourceBundle(this.nomeServizio + ".principal.label");
			InputText principal = new InputText(principalId, principalLabel, null, false, false, true, 1, 50);
			this.infoRicercaMap.put(principalId, principal);

			// profilo
			String profiloLabel = Utils.getInstance(this.getLanguage()).getMessageFromResourceBundle(this.nomeServizio + ".profilo.label");
			List<Voce<String>> tipiProfili = new ArrayList<Voce<String>>();
			tipiProfili.add(new Voce<String>(Utils.getInstance(this.getLanguage()).getMessageFromResourceBundle("commons.label.qualsiasi"),""));
			tipiProfili.add(new Voce<String>(Utils.getInstance(this.getLanguage()).getMessageFromResourceBundle(this.nomeServizio + ".profilo.admin"),PROFILO_OPERATORE_VALUE_ADMIN));
			tipiProfili.add(new Voce<String>(Utils.getInstance(this.getLanguage()).getMessageFromResourceBundle(this.nomeServizio + ".profilo.ente"),PROFILO_OPERATORE_VALUE_OPERATORE));

			SelectList<String> profilo = new SelectList<String>(profiloId, profiloLabel, PROFILO_OPERATORE_VALUE_ADMIN, false, false, true, tipiProfili);
			this.infoRicercaMap.put(profiloId, profilo);

		}
	}

	@SuppressWarnings("unchecked")
	@Override
	public InfoForm getInfoCreazione(UriInfo uriInfo, BasicBD bd) throws ConsoleException {
		URI creazione = this.getUriCreazione(uriInfo, bd);
		InfoForm infoCreazione = new InfoForm(creazione,Utils.getInstance(this.getLanguage()).getMessageFromResourceBundle(this.nomeServizio + ".creazione.titolo"));

		String operatoreId = Utils.getInstance(this.getLanguage()).getMessageFromResourceBundle(this.nomeServizio + ".id.id");
		String principalId = Utils.getInstance(this.getLanguage()).getMessageFromResourceBundle(this.nomeServizio + ".principal.id");
		String nomeId = Utils.getInstance(this.getLanguage()).getMessageFromResourceBundle(this.nomeServizio + ".nome.id");
		String profiloId = Utils.getInstance(this.getLanguage()).getMessageFromResourceBundle(this.nomeServizio + ".profilo.id");
		String abilitatoId = Utils.getInstance(this.getLanguage()).getMessageFromResourceBundle(this.nomeServizio + ".abilitato.id");
		String dominiId = Utils.getInstance(this.getLanguage()).getMessageFromResourceBundle(this.nomeServizio + ".domini.id");
		//String tipiTributoId = Utils.getInstance(this.getLanguage()).getMessageFromResourceBundle(this.nomeServizio + ".tipiTributo.id");


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

		SelectList<String> profilo = (SelectList<String>) this.infoCreazioneMap.get(profiloId);
		String profiloOperatoreValue = PROFILO_OPERATORE_VALUE_ADMIN;
		profilo.setDefaultValue(profiloOperatoreValue);
		sezioneRoot.addField(profilo); 

		List<RawParamValue> tipoAutenticazioneValues = new ArrayList<RawParamValue>();
		tipoAutenticazioneValues.add(new RawParamValue(operatoreId, null));
		tipoAutenticazioneValues.add(new RawParamValue(profiloId, PROFILO_OPERATORE_VALUE_ADMIN));

		// GP-348
		//		TipiTributo tipiTributo = (TipiTributo) infoCreazioneMap.get(tipiTributoId);
		//		tipiTributo.init(tipoAutenticazioneValues, bd,this.getLanguage()); 
		//		sezioneRoot.addField(tipiTributo);

		Domini domini = (Domini) this.infoCreazioneMap.get(dominiId);
		domini.init(tipoAutenticazioneValues, bd,this.getLanguage()); 
		sezioneRoot.addField(domini); 

		CheckButton abilitato = (CheckButton) this.infoCreazioneMap.get(abilitatoId);
		abilitato.setDefaultValue(true); 
		sezioneRoot.addField(abilitato);

		return infoCreazione;
	}

	private void initInfoCreazione(UriInfo uriInfo, BasicBD bd) throws ConsoleException{
		if(this.infoCreazioneMap == null){
			this.infoCreazioneMap = new HashMap<String, ParamField<?>>();

			String abilitatoId = Utils.getInstance(this.getLanguage()).getMessageFromResourceBundle(this.nomeServizio + ".abilitato.id");
			String principalId = Utils.getInstance(this.getLanguage()).getMessageFromResourceBundle(this.nomeServizio + ".principal.id");
			String nomeId = Utils.getInstance(this.getLanguage()).getMessageFromResourceBundle(this.nomeServizio + ".nome.id");
			String profiloId = Utils.getInstance(this.getLanguage()).getMessageFromResourceBundle(this.nomeServizio + ".profilo.id");
			String operatoreId = Utils.getInstance(this.getLanguage()).getMessageFromResourceBundle(this.nomeServizio + ".id.id");
			String dominiId = Utils.getInstance(this.getLanguage()).getMessageFromResourceBundle(this.nomeServizio + ".domini.id");
			//String tipiTributoId = Utils.getInstance(this.getLanguage()).getMessageFromResourceBundle(this.nomeServizio + ".tipiTributo.id");

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

			// profilo
			// tipo autenticazione
			String profiloLabel = Utils.getInstance(this.getLanguage()).getMessageFromResourceBundle(this.nomeServizio + ".profilo.label");
			List<Voce<String>> tipiProfili = new ArrayList<Voce<String>>();
			tipiProfili.add(new Voce<String>(Utils.getInstance(this.getLanguage()).getMessageFromResourceBundle(this.nomeServizio + ".profilo.admin"),PROFILO_OPERATORE_VALUE_ADMIN));
			tipiProfili.add(new Voce<String>(Utils.getInstance(this.getLanguage()).getMessageFromResourceBundle(this.nomeServizio + ".profilo.ente"),PROFILO_OPERATORE_VALUE_OPERATORE));

			SelectList<String> profilo = new SelectList<String>(profiloId, profiloLabel, PROFILO_OPERATORE_VALUE_ADMIN, true, false, true, tipiProfili);
			this.infoCreazioneMap.put(profiloId, profilo);

			List<RawParamValue> tipoAutenticazioneValues = new ArrayList<RawParamValue>();
			tipoAutenticazioneValues.add(new RawParamValue(operatoreId, null));
			tipoAutenticazioneValues.add(new RawParamValue(profiloId, PROFILO_OPERATORE_VALUE_ADMIN));

			// abilitato
			String abilitatoLabel = Utils.getInstance(this.getLanguage()).getMessageFromResourceBundle(this.nomeServizio + ".abilitato.label");
			CheckButton abiliato = new CheckButton(abilitatoId, abilitatoLabel, true, false, false, true);
			this.infoCreazioneMap.put(abilitatoId, abiliato);

			// GP-348
			//			String tipiTributoLabel = Utils.getInstance(this.getLanguage()).getMessageFromResourceBundle(this.nomeServizio + ".tipiTributo.label");
			//			URI tipiTributoRefreshUri = this.getUriField(uriInfo, bd, tipiTributoId); 
			//			TipiTributo tipiTributo = new TipiTributo(this.nomeServizio, tipiTributoId, tipiTributoLabel, tipiTributoRefreshUri , tipoAutenticazioneValues, bd,this.getLanguage());
			//			tipiTributo.addDependencyField(profilo);
			//			tipiTributo.init(tipoAutenticazioneValues, bd,this.getLanguage()); 
			//			infoCreazioneMap.put(tipiTributoId, tipiTributo);

			String dominiLabel = Utils.getInstance(this.getLanguage()).getMessageFromResourceBundle(this.nomeServizio + ".domini.label");
			URI dominiRefreshUri = this.getUriField(uriInfo, bd, dominiId); 
			Domini domini = new Domini(this.nomeServizio, dominiId, dominiLabel, dominiRefreshUri , tipoAutenticazioneValues, bd,this.getLanguage());
			domini.addDependencyField(profilo);
			domini.init(tipoAutenticazioneValues, bd,this.getLanguage()); 
			this.infoCreazioneMap.put(dominiId, domini);

		}
	}

	@SuppressWarnings("unchecked")
	@Override
	public InfoForm getInfoModifica(UriInfo uriInfo, BasicBD bd, Operatore entry) throws ConsoleException {
		URI modifica = this.getUriModifica(uriInfo, bd);
		InfoForm infoModifica = new InfoForm(modifica,Utils.getInstance(this.getLanguage()).getMessageFromResourceBundle(this.nomeServizio + ".modifica.titolo"));

		String abilitatoId = Utils.getInstance(this.getLanguage()).getMessageFromResourceBundle(this.nomeServizio + ".abilitato.id");
		String principalId = Utils.getInstance(this.getLanguage()).getMessageFromResourceBundle(this.nomeServizio + ".principal.id");
		String nomeId = Utils.getInstance(this.getLanguage()).getMessageFromResourceBundle(this.nomeServizio + ".nome.id");
		String profiloId = Utils.getInstance(this.getLanguage()).getMessageFromResourceBundle(this.nomeServizio + ".profilo.id");
		String operatoreId = Utils.getInstance(this.getLanguage()).getMessageFromResourceBundle(this.nomeServizio + ".id.id");
		String dominiId = Utils.getInstance(this.getLanguage()).getMessageFromResourceBundle(this.nomeServizio + ".domini.id");
		//String tipiTributoId = Utils.getInstance(this.getLanguage()).getMessageFromResourceBundle(this.nomeServizio + ".tipiTributo.id");

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

		SelectList<String> profilo = (SelectList<String>) this.infoCreazioneMap.get(profiloId);
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

		// GP-348
		//		TipiTributo tipiTributo = (TipiTributo) infoCreazioneMap.get(tipiTributoId);
		//		tipiTributo.init(tipoAutenticazioneValues, bd,this.getLanguage()); 
		//		sezioneRoot.addField(tipiTributo);

		Domini domini = (Domini) this.infoCreazioneMap.get(dominiId);
		domini.init(tipoAutenticazioneValues, bd,this.getLanguage()); 
		sezioneRoot.addField(domini); 

		CheckButton abilitato = (CheckButton) this.infoCreazioneMap.get(abilitatoId);
		abilitato.setDefaultValue(entry.isAbilitato()); 
		sezioneRoot.addField(abilitato);

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
				profiloValue = Utils.getInstance(this.getLanguage()).getMessageFromResourceBundle(this.nomeServizio + ".profilo.admin");
				isAdmin = true;
				break;
			case ENTE:
				profiloValue = Utils.getInstance(this.getLanguage()).getMessageFromResourceBundle(this.nomeServizio + ".profilo.ente");
			default: break;
			}

			InfoForm infoModifica = this.getInfoModifica(uriInfo, bd,operatore);
			InfoForm infoCancellazione = this.getInfoCancellazioneDettaglio(uriInfo, bd, operatore);
			InfoForm infoEsportazione = null;

			Dettaglio dettaglio = new Dettaglio(this.getTitolo(operatore,bd), infoEsportazione, infoCancellazione, infoModifica);

			it.govpay.web.rs.dars.model.Sezione root = dettaglio.getSezioneRoot(); 

			// dati operatore
			root.addVoce(Utils.getInstance(this.getLanguage()).getMessageFromResourceBundle(this.nomeServizio + ".principal.label"), operatore.getPrincipal());
			root.addVoce(Utils.getInstance(this.getLanguage()).getMessageFromResourceBundle(this.nomeServizio + ".nome.label"), operatore.getNome());
			root.addVoce(Utils.getInstance(this.getLanguage()).getMessageFromResourceBundle(this.nomeServizio + ".profilo.label"), profiloValue);
			root.addVoce(Utils.getInstance(this.getLanguage()).getMessageFromResourceBundle(this.nomeServizio + ".abilitato.label"), Utils.getSiNoAsLabel(operatore.isAbilitato()));

			if(!isAdmin){
				// Elementi correlati dell'operatore UO e Domini

				// GP-348
				//				String etichettaTipiTributo = Utils.getInstance(this.getLanguage()).getMessageFromResourceBundle(this.nomeServizio + ".elementoCorrelato.tipiTributo.titolo");
				//				it.govpay.web.rs.dars.model.Sezione sezioneTributi = dettaglio.addSezione(etichettaTipiTributo);
				//
				//				List<Long> idTributi = Utils.getIdsFromAcls(operatore.getAcls(), Tipo.TRIBUTO , Servizio.CRUSCOTTO);
				//				if(!Utils.isEmpty(idTributi)){
				//					if(!idTributi.contains(-1L)){
				//						TipiTributoBD tipiTributoBD = new TipiTributoBD(bd);
				//						TipoTributoFilter filter = tipiTributoBD.newFilter();
				//						FilterSortWrapper fsw = new FilterSortWrapper();
				//						fsw.setField(it.govpay.orm.TipoTributo.model().COD_TRIBUTO);
				//						fsw.setSortOrder(SortOrder.ASC);
				//						filter.getFilterSortList().add(fsw);
				//						filter.setListaIdTributi(idTributi);
				//						List<TipoTributo> findAll =  tipiTributoBD.findAll(filter);
				//
				//						it.govpay.web.rs.dars.anagrafica.tributi.TipiTributo tipiTributoDars = new it.govpay.web.rs.dars.anagrafica.tributi.TipiTributo();
				//						TipiTributoHandler tipiTributoDarsHandler = (TipiTributoHandler) tipiTributoDars.getDarsHandler();
				//						UriBuilder uriDettaglioUoBuilder = BaseRsService.checkDarsURI(uriInfo).path(tipiTributoDars.getPathServizio()).path("{id}");
				//
				//						if(findAll != null && findAll.size() > 0){
				//							for (TipoTributo entry : findAll) {
				//								Elemento elemento = tipiTributoDarsHandler.getElemento(entry, entry.getId(), uriDettaglioUoBuilder,bd);
				//								sezioneTributi.addVoce(elemento.getTitolo(), elemento.getSottotitolo(), elemento.getUri());
				//							}
				//						}
				//					} else{
				//						sezioneTributi.addVoce(Utils.getInstance(this.getLanguage()).getMessageFromResourceBundle("commons.label.tutti"),null);
				//					}
				//				}

				String etichettaDomini = Utils.getInstance(this.getLanguage()).getMessageFromResourceBundle(this.nomeServizio + ".elementoCorrelato.domini.titolo");
				it.govpay.web.rs.dars.model.Sezione sezioneDomini = dettaglio.addSezione(etichettaDomini);

				List<Long> idDomini = Utils.getIdsFromAcls(operatore.getAcls(), Tipo.DOMINIO, Servizio.CRUSCOTTO);
				if(!Utils.isEmpty(idDomini)){
					if(!idDomini.contains(-1L)){
						DominiBD dominiBD = new DominiBD(bd);
						DominioFilter filter = dominiBD.newFilter();
						FilterSortWrapper fsw = new FilterSortWrapper();
						fsw.setField(it.govpay.orm.Dominio.model().COD_DOMINIO);
						fsw.setSortOrder(SortOrder.ASC);
						filter.getFilterSortList().add(fsw);
						filter.setIdDomini(idDomini);
						List<Dominio> findAll =  dominiBD.findAll(filter);

						it.govpay.web.rs.dars.anagrafica.domini.Domini dominiDars = new it.govpay.web.rs.dars.anagrafica.domini.Domini();
						DominiHandler dominiDarsHandler = (DominiHandler) dominiDars.getDarsHandler();

						if(findAll != null && findAll.size() > 0){
							for (Dominio entry : findAll) {
								Elemento elemento = dominiDarsHandler.getElemento(entry, entry.getId(), dominiDars.getPathServizio(),bd);
								sezioneDomini.addVoce(elemento.getTitolo(), elemento.getSottotitolo(), elemento.getUri());
							}
						}
					}else {
						sezioneDomini.addVoce(Utils.getInstance(this.getLanguage()).getMessageFromResourceBundle("commons.label.tutti"),null);
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
		String dominiId = Utils.getInstance(this.getLanguage()).getMessageFromResourceBundle(this.nomeServizio + ".domini.id");
		// String tipiTributoId = Utils.getInstance(this.getLanguage()).getMessageFromResourceBundle(this.nomeServizio + ".tipiTributo.id");
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
			classMap.put(dominiId, Long.class); 
			// classMap.put(tipiTributoId, Long.class); 
			jsonConfig.setClassMap(classMap);

			JSONObject jsonObject = JSONObject.fromObject( baos.toString() );  

			// GP-348
			//			JSONArray jsonTributi = jsonObject.getJSONArray(tipiTributoId);
			//			jsonObject.remove(tipiTributoId);
			//
			//			List<Acl> lstAclTributi = new ArrayList<Acl>();
			//			for (int i = 0; i < jsonTributi.size(); i++) {
			//				long idTributo = jsonTributi.getLong(i);
			//
			//				Acl acl = new Acl();
			//				acl.setTipo(Tipo.TRIBUTO);
			//				acl.setServizio(TIPO_SERVIZIO);
			//				if(idTributo > 0){
			//					acl.setIdTributo(idTributo);
			//					lstAclTributi.add(acl);
			//				}else {
			//					lstAclTributi.clear();
			//					lstAclTributi.add(acl);
			//					break;
			//				}
			//			}

			JSONArray jsonDomini = jsonObject.getJSONArray(dominiId);
			jsonObject.remove(dominiId);

			List<Acl> lstAclDomini = new ArrayList<Acl>();
			for (int i = 0; i < jsonDomini.size(); i++) {
				long idDominio = jsonDomini.getLong(i);

				Acl acl = new Acl();
				acl.setTipo(Tipo.DOMINIO);
				acl.setServizio(TIPO_SERVIZIO);
				if(idDominio > 0){
					acl.setIdDominio(idDominio);
					lstAclDomini.add(acl);
				}else {
					lstAclDomini.clear();
					lstAclDomini.add(acl);
					break;
				}
			}

			jsonConfig.setRootClass(Operatore.class);
			entry = (Operatore) JSONObject.toBean( jsonObject, jsonConfig );

			// GP-348
			//			entry.setAcls(lstAclTributi);
			//			entry.getAcls().addAll(lstAclDomini);
			entry.setAcls(lstAclDomini);

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
	public Elenco delete(List<Long> idsToDelete, List<RawParamValue> rawValues, UriInfo uriInfo, BasicBD bd) throws WebApplicationException, ConsoleException, DeleteException {	return null; 	}

	@Override
	public String getTitolo(Operatore entry, BasicBD bd) {
		return entry.getPrincipal();
	}

	@Override
	public String getSottotitolo(Operatore entry, BasicBD bd) {
		StringBuilder sb = new StringBuilder();

		sb.append(Utils.getAbilitatoAsLabel(entry.isAbilitato()));
		sb.append(", Ruolo: ");
		ProfiloOperatore profilo = entry.getProfilo();
		if(profilo.equals(ProfiloOperatore.ADMIN)) {
			sb.append("Amministratore");
		} else {
			sb.append("Operatore");
		}

		return sb.toString();
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
