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
import org.jboss.resteasy.plugins.providers.multipart.MultipartFormDataInput;
import org.openspcoop2.generic_project.exception.NotFoundException;
import org.openspcoop2.generic_project.expression.SortOrder;

import it.govpay.bd.BasicBD;
import it.govpay.bd.FilterSortWrapper;
import it.govpay.bd.anagrafica.DominiBD;
import it.govpay.bd.anagrafica.OperatoriBD;
import it.govpay.bd.anagrafica.TipiTributoBD;
import it.govpay.bd.anagrafica.filters.DominioFilter;
import it.govpay.bd.anagrafica.filters.OperatoreFilter;
import it.govpay.bd.anagrafica.filters.TipoTributoFilter;
import it.govpay.bd.model.Dominio;
import it.govpay.bd.model.Operatore;
import it.govpay.bd.model.Operatore.ProfiloOperatore;
import it.govpay.bd.model.TipoTributo;
import it.govpay.bd.model.Acl;
import it.govpay.bd.model.Acl.Servizio;
import it.govpay.bd.model.Acl.Tipo;
import it.govpay.web.rs.BaseRsService;
import it.govpay.web.rs.dars.BaseDarsHandler;
import it.govpay.web.rs.dars.BaseDarsService;
import it.govpay.web.rs.dars.IDarsHandler;
import it.govpay.web.rs.dars.anagrafica.domini.DominiHandler;
import it.govpay.web.rs.dars.anagrafica.operatori.input.Domini;
import it.govpay.web.rs.dars.anagrafica.operatori.input.TipiTributo;
import it.govpay.web.rs.dars.anagrafica.tributi.TipiTributoHandler;
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

public class OperatoriHandler extends BaseDarsHandler<Operatore> implements IDarsHandler<Operatore>{

	private static final Servizio TIPO_SERVIZIO = Servizio.CRUSCOTTO;
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
		InfoForm infoCreazione = new InfoForm(creazione,Utils.getInstance().getMessageFromResourceBundle(this.nomeServizio + ".creazione.titolo"));

		String operatoreId = Utils.getInstance().getMessageFromResourceBundle(this.nomeServizio + ".id.id");
		String principalId = Utils.getInstance().getMessageFromResourceBundle(this.nomeServizio + ".principal.id");
		String nomeId = Utils.getInstance().getMessageFromResourceBundle(this.nomeServizio + ".nome.id");
		String profiloId = Utils.getInstance().getMessageFromResourceBundle(this.nomeServizio + ".profilo.id");
		String abilitatoId = Utils.getInstance().getMessageFromResourceBundle(this.nomeServizio + ".abilitato.id");
		String dominiId = Utils.getInstance().getMessageFromResourceBundle(this.nomeServizio + ".domini.id");
		String tipiTributoId = Utils.getInstance().getMessageFromResourceBundle(this.nomeServizio + ".tipiTributo.id");


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

		TipiTributo tipiTributo = (TipiTributo) infoCreazioneMap.get(tipiTributoId);
		tipiTributo.init(tipoAutenticazioneValues, bd); 
		sezioneRoot.addField(tipiTributo);

		Domini domini = (Domini) infoCreazioneMap.get(dominiId);
		domini.init(tipoAutenticazioneValues, bd); 
		sezioneRoot.addField(domini); 

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
			String dominiId = Utils.getInstance().getMessageFromResourceBundle(this.nomeServizio + ".domini.id");
			String tipiTributoId = Utils.getInstance().getMessageFromResourceBundle(this.nomeServizio + ".tipiTributo.id");

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

			String tipiTributoLabel = Utils.getInstance().getMessageFromResourceBundle(this.nomeServizio + ".tipiTributo.label");
			URI tipiTributoRefreshUri = this.getUriField(uriInfo, bd, tipiTributoId); 
			TipiTributo tipiTributo = new TipiTributo(this.nomeServizio, tipiTributoId, tipiTributoLabel, tipiTributoRefreshUri , tipoAutenticazioneValues, bd);
			tipiTributo.addDependencyField(profilo);
			tipiTributo.init(tipoAutenticazioneValues, bd); 
			infoCreazioneMap.put(tipiTributoId, tipiTributo);

			String dominiLabel = Utils.getInstance().getMessageFromResourceBundle(this.nomeServizio + ".domini.label");
			URI dominiRefreshUri = this.getUriField(uriInfo, bd, dominiId); 
			Domini domini = new Domini(this.nomeServizio, dominiId, dominiLabel, dominiRefreshUri , tipoAutenticazioneValues, bd);
			domini.addDependencyField(profilo);
			domini.init(tipoAutenticazioneValues, bd); 
			infoCreazioneMap.put(dominiId, domini);

		}
	}

	@SuppressWarnings("unchecked")
	@Override
	public InfoForm getInfoModifica(UriInfo uriInfo, BasicBD bd, Operatore entry) throws ConsoleException {
		URI modifica = this.getUriModifica(uriInfo, bd);
		InfoForm infoModifica = new InfoForm(modifica,Utils.getInstance().getMessageFromResourceBundle(this.nomeServizio + ".modifica.titolo"));

		String abilitatoId = Utils.getInstance().getMessageFromResourceBundle(this.nomeServizio + ".abilitato.id");
		String principalId = Utils.getInstance().getMessageFromResourceBundle(this.nomeServizio + ".principal.id");
		String nomeId = Utils.getInstance().getMessageFromResourceBundle(this.nomeServizio + ".nome.id");
		String profiloId = Utils.getInstance().getMessageFromResourceBundle(this.nomeServizio + ".profilo.id");
		String operatoreId = Utils.getInstance().getMessageFromResourceBundle(this.nomeServizio + ".id.id");
		String dominiId = Utils.getInstance().getMessageFromResourceBundle(this.nomeServizio + ".domini.id");
		String tipiTributoId = Utils.getInstance().getMessageFromResourceBundle(this.nomeServizio + ".tipiTributo.id");

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

		TipiTributo tipiTributo = (TipiTributo) infoCreazioneMap.get(tipiTributoId);
		tipiTributo.init(tipoAutenticazioneValues, bd); 
		sezioneRoot.addField(tipiTributo);

		Domini domini = (Domini) infoCreazioneMap.get(dominiId);
		domini.init(tipoAutenticazioneValues, bd); 
		sezioneRoot.addField(domini); 

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

			Dettaglio dettaglio = new Dettaglio(this.getTitolo(operatore,bd), esportazione, cancellazione, infoModifica);

			it.govpay.web.rs.dars.model.Sezione root = dettaglio.getSezioneRoot(); 

			// dati del psp
			root.addVoce(Utils.getInstance().getMessageFromResourceBundle(this.nomeServizio + ".principal.label"), operatore.getPrincipal());
			root.addVoce(Utils.getInstance().getMessageFromResourceBundle(this.nomeServizio + ".nome.label"), operatore.getNome());
			root.addVoce(Utils.getInstance().getMessageFromResourceBundle(this.nomeServizio + ".profilo.label"), profiloValue);
			root.addVoce(Utils.getInstance().getMessageFromResourceBundle(this.nomeServizio + ".abilitato.label"), Utils.getSiNoAsLabel(operatore.isAbilitato()));

			if(!isAdmin){
				// Elementi correlati dell'operatore UO e Domini
				String etichettaTipiTributo = Utils.getInstance().getMessageFromResourceBundle(this.nomeServizio + ".elementoCorrelato.tipiTributo.titolo");
				it.govpay.web.rs.dars.model.Sezione sezioneTributi = dettaglio.addSezione(etichettaTipiTributo);

				List<Long> idTributi = Utils.getIdsFromAcls(operatore.getAcls(), Tipo.TRIBUTO , Servizio.CRUSCOTTO);
				if(!Utils.isEmpty(idTributi)){
					if(!idTributi.contains(-1L)){
						TipiTributoBD tipiTributoBD = new TipiTributoBD(bd);
						TipoTributoFilter filter = tipiTributoBD.newFilter();
						FilterSortWrapper fsw = new FilterSortWrapper();
						fsw.setField(it.govpay.orm.TipoTributo.model().COD_TRIBUTO);
						fsw.setSortOrder(SortOrder.ASC);
						filter.getFilterSortList().add(fsw);
						filter.setListaIdTributi(idTributi);
						List<TipoTributo> findAll =  tipiTributoBD.findAll(filter);

						it.govpay.web.rs.dars.anagrafica.tributi.TipiTributo tipiTributoDars = new it.govpay.web.rs.dars.anagrafica.tributi.TipiTributo();
						TipiTributoHandler tipiTributoDarsHandler = (TipiTributoHandler) tipiTributoDars.getDarsHandler();
						UriBuilder uriDettaglioUoBuilder = BaseRsService.checkDarsURI(uriInfo).path(tipiTributoDars.getPathServizio()).path("{id}");

						if(findAll != null && findAll.size() > 0){
							for (TipoTributo entry : findAll) {
								Elemento elemento = tipiTributoDarsHandler.getElemento(entry, entry.getId(), uriDettaglioUoBuilder,bd);
								sezioneTributi.addVoce(elemento.getTitolo(), elemento.getSottotitolo(), elemento.getUri());
							}
						}
					} else{
						sezioneTributi.addVoce(Utils.getInstance().getMessageFromResourceBundle("commons.label.tutti"),null);
					}
				}

				String etichettaDomini = Utils.getInstance().getMessageFromResourceBundle(this.nomeServizio + ".elementoCorrelato.domini.titolo");
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
						UriBuilder uriDettaglioDominiBuilder = BaseRsService.checkDarsURI(uriInfo).path(dominiDars.getPathServizio()).path("{id}");

						if(findAll != null && findAll.size() > 0){
							for (Dominio entry : findAll) {
								Elemento elemento = dominiDarsHandler.getElemento(entry, entry.getId(), uriDettaglioDominiBuilder,bd);
								sezioneDomini.addVoce(elemento.getTitolo(), elemento.getSottotitolo(), elemento.getUri());
							}
						}
					}else {
						sezioneDomini.addVoce(Utils.getInstance().getMessageFromResourceBundle("commons.label.tutti"),null);
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
		String dominiId = Utils.getInstance().getMessageFromResourceBundle(this.nomeServizio + ".domini.id");
		String tipiTributoId = Utils.getInstance().getMessageFromResourceBundle(this.nomeServizio + ".tipiTributo.id");
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
			classMap.put(tipiTributoId, Long.class); 
			jsonConfig.setClassMap(classMap);

			JSONObject jsonObject = JSONObject.fromObject( baos.toString() );  

			JSONArray jsonTributi = jsonObject.getJSONArray(tipiTributoId);
			jsonObject.remove(tipiTributoId);

			List<Acl> lstAclTributi = new ArrayList<Acl>();
			for (int i = 0; i < jsonTributi.size(); i++) {
				long idTributo = jsonTributi.getLong(i);

				Acl acl = new Acl();
				acl.setTipo(Tipo.TRIBUTO);
				acl.setServizio(TIPO_SERVIZIO);
				if(idTributo > 0){
					acl.setIdTributo(idTributo);
					lstAclTributi.add(acl);
				}else {
					lstAclTributi.clear();
					lstAclTributi.add(acl);
					break;
				}
			}

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
			
			entry.setAcls(lstAclTributi);
			entry.getAcls().addAll(lstAclDomini);

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
	public String getTitolo(Operatore entry, BasicBD bd) {
		return entry.getPrincipal();
	}

	@Override
	public String getSottotitolo(Operatore entry, BasicBD bd) {
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
	public List<String> getValori(Operatore entry, BasicBD bd) throws ConsoleException {
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
