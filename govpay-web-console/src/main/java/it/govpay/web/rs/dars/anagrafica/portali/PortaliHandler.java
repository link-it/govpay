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
import javax.ws.rs.core.UriInfo;

import org.apache.commons.lang.StringUtils;
import org.apache.logging.log4j.Logger;
import org.jboss.resteasy.plugins.providers.multipart.MultipartFormDataInput;
import org.openspcoop2.generic_project.exception.NotFoundException;
import org.openspcoop2.generic_project.expression.SortOrder;

import it.govpay.bd.BasicBD;
import it.govpay.bd.FilterSortWrapper;
import it.govpay.bd.anagrafica.DominiBD;
import it.govpay.bd.anagrafica.PortaliBD;
import it.govpay.bd.anagrafica.TipiTributoBD;
import it.govpay.bd.anagrafica.filters.DominioFilter;
import it.govpay.bd.anagrafica.filters.PortaleFilter;
import it.govpay.bd.anagrafica.filters.TipoTributoFilter;
import it.govpay.bd.model.Dominio;
import it.govpay.model.Acl;
import it.govpay.model.Acl.Servizio;
import it.govpay.model.Acl.Tipo;
import it.govpay.model.Portale;
import it.govpay.model.TipoTributo;
import it.govpay.model.Versionabile.Versione;
import it.govpay.web.rs.dars.BaseDarsHandler;
import it.govpay.web.rs.dars.BaseDarsService;
import it.govpay.web.rs.dars.IDarsHandler;
import it.govpay.web.rs.dars.anagrafica.domini.DominiHandler;
import it.govpay.web.rs.dars.anagrafica.portali.input.DominiPA;
import it.govpay.web.rs.dars.anagrafica.portali.input.DominiPO;
import it.govpay.web.rs.dars.anagrafica.portali.input.TipiTributoPA;
import it.govpay.web.rs.dars.anagrafica.portali.input.TipiTributoPO;
import it.govpay.web.rs.dars.anagrafica.portali.input.Trusted;
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
import it.govpay.web.rs.dars.model.VoceRiferimento;
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

public class PortaliHandler extends BaseDarsHandler<Portale> implements IDarsHandler<Portale>{

	private Map<String, ParamField<?>> infoCreazioneMap = null;
	private Map<String, ParamField<?>> infoRicercaMap = null;

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

			PortaliBD portaliBD = new PortaliBD(bd);
			PortaleFilter filter = portaliBD.newFilter();
			filter.setOffset(offset);
			filter.setLimit(limit);
			FilterSortWrapper fsw = new FilterSortWrapper();
			fsw.setField(it.govpay.orm.Portale.model().COD_PORTALE);
			fsw.setSortOrder(SortOrder.ASC);
			filter.getFilterSortList().add(fsw);

			String codPortaleId = Utils.getInstance(this.getLanguage()).getMessageFromResourceBundle(this.nomeServizio + ".codPortale.id");
			String codPortale = this.getParameter(uriInfo, codPortaleId, String.class);

			if(StringUtils.isNotEmpty(codPortale)){
				filter.setCodPortale(codPortale);
			}

			long count = portaliBD.count(filter);

			// visualizza la ricerca solo se i risultati sono > del limit
			boolean visualizzaRicerca = this.visualizzaRicerca(count, limit);
			InfoForm infoRicerca = this.getInfoRicerca(uriInfo, bd, visualizzaRicerca);

			Elenco elenco = new Elenco(this.titoloServizio, infoRicerca,
					this.getInfoCreazione(uriInfo, bd),
					count, esportazione, cancellazione); 

			List<Portale> findAll = portaliBD.findAll(filter);

			if(findAll != null && findAll.size() > 0){
				for (Portale entry : findAll) {
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
			String codPortaleId = Utils.getInstance(this.getLanguage()).getMessageFromResourceBundle(this.nomeServizio + ".codPortale.id");

			if(this.infoRicercaMap == null){
				this.initInfoRicerca(uriInfo, bd);

			}
			Sezione sezioneRoot = infoRicerca.getSezioneRoot();

			InputText codPortale= (InputText) this.infoRicercaMap.get(codPortaleId);
			codPortale.setDefaultValue(null);
			codPortale.setEditable(true); 
			sezioneRoot.addField(codPortale);
		}
		return infoRicerca;
	}

	private void initInfoRicerca(UriInfo uriInfo, BasicBD bd) throws ConsoleException{
		if(this.infoRicercaMap == null){
			this.infoRicercaMap = new HashMap<String, ParamField<?>>();

			String codPortaleId = Utils.getInstance(this.getLanguage()).getMessageFromResourceBundle(this.nomeServizio + ".codPortale.id");
			String codPortaleLabel = Utils.getInstance(this.getLanguage()).getMessageFromResourceBundle(this.nomeServizio + ".codPortale.label");
			InputText codPortale = new InputText(codPortaleId, codPortaleLabel, null, false, false, true, 1, 255);
			this.infoRicercaMap.put(codPortaleId, codPortale);
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

	@SuppressWarnings("unchecked")
	@Override
	public InfoForm getInfoCreazione(UriInfo uriInfo, BasicBD bd) throws ConsoleException {
		URI creazione = this.getUriCreazione(uriInfo, bd);
		InfoForm infoCreazione = new InfoForm(creazione,Utils.getInstance(this.getLanguage()).getMessageFromResourceBundle(this.nomeServizio + ".creazione.titolo"));

		String codPortaleId = Utils.getInstance(this.getLanguage()).getMessageFromResourceBundle(this.nomeServizio + ".codPortale.id");
		String principalId = Utils.getInstance(this.getLanguage()).getMessageFromResourceBundle(this.nomeServizio + ".principal.id");
		String abilitatoId = Utils.getInstance(this.getLanguage()).getMessageFromResourceBundle(this.nomeServizio + ".abilitato.id");
		String portaleId = Utils.getInstance(this.getLanguage()).getMessageFromResourceBundle(this.nomeServizio + ".id.id");
		String defaultCallbackURLId = Utils.getInstance(this.getLanguage()).getMessageFromResourceBundle(this.nomeServizio + ".defaultCallbackURL.id");
		String versioneId = Utils.getInstance(this.getLanguage()).getMessageFromResourceBundle(this.nomeServizio + ".versione.id");

		String pagamentiAttesaId = Utils.getInstance(this.getLanguage()).getMessageFromResourceBundle(this.nomeServizio + ".pagamentiAttesa.id");
		String pagamentiOnlineId = Utils.getInstance(this.getLanguage()).getMessageFromResourceBundle(this.nomeServizio + ".pagamentiOnline.id");
		String dominiPaId = Utils.getInstance(this.getLanguage()).getMessageFromResourceBundle(this.nomeServizio + ".dominiPa.id");
		String tipiTributoPaId = Utils.getInstance(this.getLanguage()).getMessageFromResourceBundle(this.nomeServizio + ".tipiTributoPa.id");
		String dominiPoId = Utils.getInstance(this.getLanguage()).getMessageFromResourceBundle(this.nomeServizio + ".dominiPo.id");
		String tipiTributoPoId = Utils.getInstance(this.getLanguage()).getMessageFromResourceBundle(this.nomeServizio + ".tipiTributoPo.id");
		String trustedId = Utils.getInstance(this.getLanguage()).getMessageFromResourceBundle(this.nomeServizio + ".trusted.id");

		if(this.infoCreazioneMap == null){
			this.initInfoCreazione(uriInfo, bd);
		}

		Sezione sezioneRoot = infoCreazione.getSezioneRoot();
		InputNumber idInterm = (InputNumber) this.infoCreazioneMap.get(portaleId);
		idInterm.setDefaultValue(null);
		sezioneRoot.addField(idInterm);

		InputText codPortale = (InputText) this.infoCreazioneMap.get(codPortaleId);
		codPortale.setDefaultValue(null);
		codPortale.setEditable(true); 
		sezioneRoot.addField(codPortale);

		InputText principal = (InputText) this.infoCreazioneMap.get(principalId);
		principal.setDefaultValue(null);
		sezioneRoot.addField(principal);

		InputText defaultCallbackURL = (InputText) this.infoCreazioneMap.get(defaultCallbackURLId);
		defaultCallbackURL.setDefaultValue(null);
		sezioneRoot.addField(defaultCallbackURL);

		// versione
		SelectList<String> versione = (SelectList<String>) this.infoCreazioneMap.get(versioneId);
		versione.setDefaultValue(Versione.getUltimaVersione().getLabel());
		sezioneRoot.addField(versione);

		CheckButton abilitato = (CheckButton) this.infoCreazioneMap.get(abilitatoId);
		abilitato.setDefaultValue(true); 
		sezioneRoot.addField(abilitato);

		String etichettaPagamentiAttesa = Utils.getInstance(this.getLanguage()).getMessageFromResourceBundle(this.nomeServizio + ".elementoCorrelato.pagamentiAttesa.titolo");
		Sezione sezionePA = infoCreazione.addSezione(etichettaPagamentiAttesa);

		CheckButton pagamentiAttesa = (CheckButton) this.infoCreazioneMap.get(pagamentiAttesaId);
		pagamentiAttesa.setDefaultValue(false); 
		sezionePA.addField(pagamentiAttesa);


		List<RawParamValue> pagamentiAttesaValues = new ArrayList<RawParamValue>();
		pagamentiAttesaValues.add(new RawParamValue(portaleId, null));
		pagamentiAttesaValues.add(new RawParamValue(pagamentiAttesaId, "false"));

		TipiTributoPA tipiTributoPa = (TipiTributoPA) this.infoCreazioneMap.get(tipiTributoPaId);
		tipiTributoPa.init(pagamentiAttesaValues, bd,this.getLanguage()); 
		sezionePA.addField(tipiTributoPa);

		DominiPA dominiPa = (DominiPA) this.infoCreazioneMap.get(dominiPaId);
		dominiPa.init(pagamentiAttesaValues, bd,this.getLanguage()); 
		sezionePA.addField(dominiPa); 

		String etichettaPagamentiOnline = Utils.getInstance(this.getLanguage()).getMessageFromResourceBundle(this.nomeServizio + ".elementoCorrelato.pagamentiOnline.titolo");
		Sezione sezionePO = infoCreazione.addSezione(etichettaPagamentiOnline);

		CheckButton pagamentiOnline = (CheckButton) this.infoCreazioneMap.get(pagamentiOnlineId);
		pagamentiOnline.setDefaultValue(false); 
		sezionePO.addField(pagamentiOnline);

		List<RawParamValue> pagamentiOnlineValues = new ArrayList<RawParamValue>();
		pagamentiOnlineValues.add(new RawParamValue(portaleId, null));
		pagamentiOnlineValues.add(new RawParamValue(pagamentiOnlineId, "false"));

		Trusted trusted = (Trusted) this.infoCreazioneMap.get(trustedId);
		trusted.init(pagamentiOnlineValues, bd,this.getLanguage()); 
		sezionePO.addField(trusted);

		List<RawParamValue> pagamentiOnlineTrustedValues = new ArrayList<RawParamValue>();
		pagamentiOnlineTrustedValues.addAll(pagamentiOnlineValues);
		pagamentiOnlineTrustedValues.add(new RawParamValue(trustedId, "false"));

		TipiTributoPO tipiTributoPo = (TipiTributoPO) this.infoCreazioneMap.get(tipiTributoPoId);
		tipiTributoPo.init(pagamentiOnlineTrustedValues, bd,this.getLanguage()); 
		sezionePO.addField(tipiTributoPo);

		DominiPO dominiPo = (DominiPO) this.infoCreazioneMap.get(dominiPoId);
		dominiPo.init(pagamentiOnlineValues, bd,this.getLanguage()); 
		sezionePO.addField(dominiPo); 

		return infoCreazione;
	}

	private void initInfoCreazione(UriInfo uriInfo, BasicBD bd) throws ConsoleException{
		if(this.infoCreazioneMap == null){
			this.infoCreazioneMap = new HashMap<String, ParamField<?>>();

			String codPortaleId = Utils.getInstance(this.getLanguage()).getMessageFromResourceBundle(this.nomeServizio + ".codPortale.id");
			String principalId = Utils.getInstance(this.getLanguage()).getMessageFromResourceBundle(this.nomeServizio + ".principal.id");
			String abilitatoId = Utils.getInstance(this.getLanguage()).getMessageFromResourceBundle(this.nomeServizio + ".abilitato.id");
			String portaleId = Utils.getInstance(this.getLanguage()).getMessageFromResourceBundle(this.nomeServizio + ".id.id");
			String defaultCallbackURLId = Utils.getInstance(this.getLanguage()).getMessageFromResourceBundle(this.nomeServizio + ".defaultCallbackURL.id");
			String versioneId = Utils.getInstance(this.getLanguage()).getMessageFromResourceBundle(this.nomeServizio + ".versione.id");

			String pagamentiAttesaId = Utils.getInstance(this.getLanguage()).getMessageFromResourceBundle(this.nomeServizio + ".pagamentiAttesa.id");
			String pagamentiOnlineId = Utils.getInstance(this.getLanguage()).getMessageFromResourceBundle(this.nomeServizio + ".pagamentiOnline.id");
			String dominiPaId = Utils.getInstance(this.getLanguage()).getMessageFromResourceBundle(this.nomeServizio + ".dominiPa.id");
			String tipiTributoPaId = Utils.getInstance(this.getLanguage()).getMessageFromResourceBundle(this.nomeServizio + ".tipiTributoPa.id");
			String dominiPoId = Utils.getInstance(this.getLanguage()).getMessageFromResourceBundle(this.nomeServizio + ".dominiPo.id");
			String tipiTributoPoId = Utils.getInstance(this.getLanguage()).getMessageFromResourceBundle(this.nomeServizio + ".tipiTributoPo.id");
			String trustedId = Utils.getInstance(this.getLanguage()).getMessageFromResourceBundle(this.nomeServizio + ".trusted.id");

			// id 
			InputNumber id = new InputNumber(portaleId, null, null, true, true, false, 1, 20);
			this.infoCreazioneMap.put(portaleId, id);

			// versione
			SelectList<String> versione = this.getSelectListVersione(versioneId);
			this.infoCreazioneMap.put(versioneId, versione);

			// codPortale
			String codPortaleLabel = Utils.getInstance(this.getLanguage()).getMessageFromResourceBundle(this.nomeServizio + ".codPortale.label");
			InputText codPortale = new InputText(codPortaleId, codPortaleLabel, null, true, false, true, 1, 35);
			codPortale.setSuggestion(Utils.getInstance(this.getLanguage()).getMessageFromResourceBundle(this.nomeServizio + ".codPortale.suggestion"));
			codPortale.setValidation(null, Utils.getInstance(this.getLanguage()).getMessageFromResourceBundle(this.nomeServizio + ".codPortale.errorMessage"));
			this.infoCreazioneMap.put(codPortaleId, codPortale);

			// principal
			String principalLabel = Utils.getInstance(this.getLanguage()).getMessageFromResourceBundle(this.nomeServizio + ".principal.label");
			InputText principal = new InputText(principalId, principalLabel, null, true, false, true, 1, 255);
			principal.setValidation(null, Utils.getInstance(this.getLanguage()).getMessageFromResourceBundle(this.nomeServizio + ".principal.errorMessage"));
			this.infoCreazioneMap.put(principalId, principal);

			// abilitato
			String abilitatoLabel = Utils.getInstance(this.getLanguage()).getMessageFromResourceBundle(this.nomeServizio + ".abilitato.label");
			CheckButton abiliato = new CheckButton(abilitatoId, abilitatoLabel, true, false, false, true);
			this.infoCreazioneMap.put(abilitatoId, abiliato);

			String defaultCallbackURLLabel = Utils.getInstance(this.getLanguage()).getMessageFromResourceBundle(this.nomeServizio + ".defaultCallbackURL.label");
			InputText defaultCallbackURL = new InputText(defaultCallbackURLId, defaultCallbackURLLabel, null, true, false, true, 1,512);
			this.infoCreazioneMap.put(defaultCallbackURLId, defaultCallbackURL);

			//seziona pagamenti in attesa
			// abilitato
			String pagamentiAttesaLabel = Utils.getInstance(this.getLanguage()).getMessageFromResourceBundle(this.nomeServizio + ".pagamentiAttesa.label");
			CheckButton pagamentiAttesa = new CheckButton(pagamentiAttesaId, pagamentiAttesaLabel, true, false, false, true);
			this.infoCreazioneMap.put(pagamentiAttesaId, pagamentiAttesa);

			List<RawParamValue> pagamentiAttesaValues = new ArrayList<RawParamValue>();
			pagamentiAttesaValues.add(new RawParamValue(portaleId, null));
			pagamentiAttesaValues.add(new RawParamValue(pagamentiAttesaId, "false"));

			String tipiTributoPaLabel = Utils.getInstance(this.getLanguage()).getMessageFromResourceBundle(this.nomeServizio + ".tipiTributoPa.label");
			URI tipiTributoPaRefreshUri = this.getUriField(uriInfo, bd, tipiTributoPaId); 
			TipiTributoPA tipiTributoPa = new TipiTributoPA(this.nomeServizio, tipiTributoPaId, tipiTributoPaLabel, tipiTributoPaRefreshUri , pagamentiAttesaValues, bd,this.getLanguage());
			tipiTributoPa.addDependencyField(pagamentiAttesa);
			tipiTributoPa.init(pagamentiAttesaValues, bd,this.getLanguage()); 
			this.infoCreazioneMap.put(tipiTributoPaId, tipiTributoPa);

			String dominiPaLabel = Utils.getInstance(this.getLanguage()).getMessageFromResourceBundle(this.nomeServizio + ".dominiPa.label");
			URI dominiPaRefreshUri = this.getUriField(uriInfo, bd, dominiPaId); 
			DominiPA dominiPa = new DominiPA(this.nomeServizio, dominiPaId, dominiPaLabel, dominiPaRefreshUri , pagamentiAttesaValues, bd,this.getLanguage());
			dominiPa.addDependencyField(pagamentiAttesa);
			dominiPa.init(pagamentiAttesaValues, bd,this.getLanguage()); 
			this.infoCreazioneMap.put(dominiPaId, dominiPa);

			//seziona pagamenti online
			// abilitato
			String pagamentiOnlineLabel = Utils.getInstance(this.getLanguage()).getMessageFromResourceBundle(this.nomeServizio + ".pagamentiOnline.label");
			CheckButton pagamentiOnline = new CheckButton(pagamentiOnlineId, pagamentiOnlineLabel, true, false, false, true);
			this.infoCreazioneMap.put(pagamentiOnlineId, pagamentiOnline);

			List<RawParamValue> pagamentiOnlineValues = new ArrayList<RawParamValue>();
			pagamentiOnlineValues.add(new RawParamValue(portaleId, null));
			pagamentiOnlineValues.add(new RawParamValue(pagamentiOnlineId, "false"));

			// trusted
			String trustedLabel = Utils.getInstance(this.getLanguage()).getMessageFromResourceBundle(this.nomeServizio + ".trusted.label");
			URI trustedRefreshUri = this.getUriField(uriInfo, bd, trustedId); 
			Trusted trusted = new Trusted(this.nomeServizio,trustedId, trustedLabel, trustedRefreshUri, pagamentiOnlineValues,this.getLanguage());
			trusted.addDependencyField(pagamentiOnline);
			trusted.init(pagamentiOnlineValues, bd,this.getLanguage());
			this.infoCreazioneMap.put(trustedId, trusted);

			List<RawParamValue> pagamentiOnlineTrustedValues = new ArrayList<RawParamValue>();
			pagamentiOnlineTrustedValues.addAll(pagamentiOnlineValues);
			pagamentiOnlineTrustedValues.add(new RawParamValue(trustedId, "false"));

			String tipiTributoPoLabel = Utils.getInstance(this.getLanguage()).getMessageFromResourceBundle(this.nomeServizio + ".tipiTributoPo.label");
			URI tipiTributoPoRefreshUri = this.getUriField(uriInfo, bd, tipiTributoPoId); 
			TipiTributoPO tipiTributoPo = new TipiTributoPO(this.nomeServizio, tipiTributoPoId, tipiTributoPoLabel, tipiTributoPoRefreshUri , pagamentiOnlineTrustedValues, bd,this.getLanguage());
			tipiTributoPo.addDependencyField(pagamentiOnline);
			tipiTributoPo.addDependencyField(trusted);
			tipiTributoPo.init(pagamentiOnlineTrustedValues, bd,this.getLanguage()); 
			this.infoCreazioneMap.put(tipiTributoPoId, tipiTributoPo);

			String dominiPoLabel = Utils.getInstance(this.getLanguage()).getMessageFromResourceBundle(this.nomeServizio + ".dominiPo.label");
			URI dominiPoRefreshUri = this.getUriField(uriInfo, bd, dominiPoId); 
			DominiPO dominiPo = new DominiPO(this.nomeServizio, dominiPoId, dominiPoLabel, dominiPoRefreshUri , pagamentiOnlineValues, bd,this.getLanguage());
			dominiPo.addDependencyField(pagamentiOnline);
			dominiPo.addDependencyField(trusted);
			dominiPo.init(pagamentiOnlineValues, bd,this.getLanguage()); 
			this.infoCreazioneMap.put(dominiPoId, dominiPo);

		}
	}

	@SuppressWarnings("unchecked")
	@Override
	public InfoForm getInfoModifica(UriInfo uriInfo, BasicBD bd, Portale entry) throws ConsoleException {
		URI modifica = this.getUriModifica(uriInfo, bd);
		InfoForm infoModifica = new InfoForm(modifica,Utils.getInstance(this.getLanguage()).getMessageFromResourceBundle(this.nomeServizio + ".modifica.titolo"));

		String codPortaleId = Utils.getInstance(this.getLanguage()).getMessageFromResourceBundle(this.nomeServizio + ".codPortale.id");
		String principalId = Utils.getInstance(this.getLanguage()).getMessageFromResourceBundle(this.nomeServizio + ".principal.id");
		String abilitatoId = Utils.getInstance(this.getLanguage()).getMessageFromResourceBundle(this.nomeServizio + ".abilitato.id");
		String portaleId = Utils.getInstance(this.getLanguage()).getMessageFromResourceBundle(this.nomeServizio + ".id.id");
		String defaultCallbackURLId = Utils.getInstance(this.getLanguage()).getMessageFromResourceBundle(this.nomeServizio + ".defaultCallbackURL.id");
		String versioneId = Utils.getInstance(this.getLanguage()).getMessageFromResourceBundle(this.nomeServizio + ".versione.id");

		String pagamentiAttesaId = Utils.getInstance(this.getLanguage()).getMessageFromResourceBundle(this.nomeServizio + ".pagamentiAttesa.id");
		String pagamentiOnlineId = Utils.getInstance(this.getLanguage()).getMessageFromResourceBundle(this.nomeServizio + ".pagamentiOnline.id");
		String dominiPaId = Utils.getInstance(this.getLanguage()).getMessageFromResourceBundle(this.nomeServizio + ".dominiPa.id");
		String tipiTributoPaId = Utils.getInstance(this.getLanguage()).getMessageFromResourceBundle(this.nomeServizio + ".tipiTributoPa.id");
		String dominiPoId = Utils.getInstance(this.getLanguage()).getMessageFromResourceBundle(this.nomeServizio + ".dominiPo.id");
		String tipiTributoPoId = Utils.getInstance(this.getLanguage()).getMessageFromResourceBundle(this.nomeServizio + ".tipiTributoPo.id");
		String trustedId = Utils.getInstance(this.getLanguage()).getMessageFromResourceBundle(this.nomeServizio + ".trusted.id");

		if(this.infoCreazioneMap == null){
			this.initInfoCreazione(uriInfo, bd);
		}

		Sezione sezioneRoot = infoModifica.getSezioneRoot();
		InputNumber idInterm = (InputNumber) this.infoCreazioneMap.get(portaleId);
		idInterm.setDefaultValue(entry.getId());
		sezioneRoot.addField(idInterm);

		InputText codPortale = (InputText) this.infoCreazioneMap.get(codPortaleId);
		codPortale.setDefaultValue(entry.getCodPortale());
		codPortale.setEditable(false); 
		sezioneRoot.addField(codPortale);

		InputText principal = (InputText) this.infoCreazioneMap.get(principalId);
		principal.setDefaultValue(entry.getPrincipal());
		sezioneRoot.addField(principal);

		InputText defaultCallbackURL = (InputText) this.infoCreazioneMap.get(defaultCallbackURLId);
		defaultCallbackURL.setDefaultValue(entry.getDefaultCallbackURL());
		sezioneRoot.addField(defaultCallbackURL);

		// versione
		SelectList<String> versione = (SelectList<String>) this.infoCreazioneMap.get(versioneId);
		versione.setDefaultValue(entry.getVersione().getLabel());
		sezioneRoot.addField(versione);

		CheckButton abilitato = (CheckButton) this.infoCreazioneMap.get(abilitatoId);
		abilitato.setDefaultValue(entry.isAbilitato()); 
		sezioneRoot.addField(abilitato);

		String etichettaPagamentiAttesa = Utils.getInstance(this.getLanguage()).getMessageFromResourceBundle(this.nomeServizio + ".elementoCorrelato.pagamentiAttesa.titolo");
		Sezione sezionePA = infoModifica.addSezione(etichettaPagamentiAttesa);

		List<Long> idsAclDominiPA = Utils.getIdsFromAcls(entry.getAcls(), Tipo.DOMINIO, Servizio.PAGAMENTI_ATTESA);
		List<Long> idsAclTributiPA = Utils.getIdsFromAcls(entry.getAcls(), Tipo.TRIBUTO, Servizio.PAGAMENTI_ATTESA);
		boolean visualizzaPA = idsAclDominiPA.size() > 0 || idsAclTributiPA.size() > 0;

		CheckButton pagamentiAttesa = (CheckButton) this.infoCreazioneMap.get(pagamentiAttesaId);
		pagamentiAttesa.setDefaultValue(visualizzaPA); 
		sezionePA.addField(pagamentiAttesa);

		List<RawParamValue> pagamentiAttesaValues = new ArrayList<RawParamValue>();
		pagamentiAttesaValues.add(new RawParamValue(portaleId, entry.getId()+""));
		pagamentiAttesaValues.add(new RawParamValue(pagamentiAttesaId, (visualizzaPA? "true" : "false")));

		TipiTributoPA tipiTributoPa = (TipiTributoPA) this.infoCreazioneMap.get(tipiTributoPaId);
		tipiTributoPa.init(pagamentiAttesaValues, bd,this.getLanguage()); 
		sezionePA.addField(tipiTributoPa);

		DominiPA dominiPa = (DominiPA) this.infoCreazioneMap.get(dominiPaId);
		dominiPa.init(pagamentiAttesaValues, bd,this.getLanguage()); 
		sezionePA.addField(dominiPa); 

		String etichettaPagamentiOnline = Utils.getInstance(this.getLanguage()).getMessageFromResourceBundle(this.nomeServizio + ".elementoCorrelato.pagamentiOnline.titolo");
		Sezione sezionePO = infoModifica.addSezione(etichettaPagamentiOnline);

		List<Long> idsAclDominiPO = Utils.getIdsFromAcls(entry.getAcls(), Tipo.DOMINIO, Servizio.PAGAMENTI_ONLINE);
		List<Long> idsAclTributiPO = Utils.getIdsFromAcls(entry.getAcls(), Tipo.TRIBUTO, Servizio.PAGAMENTI_ONLINE);
		boolean visualizzaPO = idsAclDominiPO.size() > 0 || idsAclTributiPO.size() > 0 || entry.isTrusted(); 

		CheckButton pagamentiOnline = (CheckButton) this.infoCreazioneMap.get(pagamentiOnlineId);
		pagamentiOnline.setDefaultValue(visualizzaPO); 
		sezionePO.addField(pagamentiOnline);

		List<RawParamValue> pagamentiOnlineValues = new ArrayList<RawParamValue>();
		pagamentiOnlineValues.add(new RawParamValue(portaleId, entry.getId()+""));
		pagamentiOnlineValues.add(new RawParamValue(pagamentiOnlineId, (visualizzaPO? "true" : "false")));

		Trusted trusted = (Trusted) this.infoCreazioneMap.get(trustedId);
		trusted.init(pagamentiOnlineValues, bd,this.getLanguage()); 
		sezionePO.addField(trusted);

		List<RawParamValue> pagamentiOnlineTrustedValues = new ArrayList<RawParamValue>();
		pagamentiOnlineTrustedValues.addAll(pagamentiOnlineValues);
		pagamentiOnlineTrustedValues.add(new RawParamValue(trustedId, (entry.isTrusted() ? "true" : "false")));

		TipiTributoPO tipiTributoPo = (TipiTributoPO) this.infoCreazioneMap.get(tipiTributoPoId);
		tipiTributoPo.init(pagamentiOnlineTrustedValues, bd,this.getLanguage()); 
		sezionePO.addField(tipiTributoPo);

		DominiPO dominiPo = (DominiPO) this.infoCreazioneMap.get(dominiPoId);
		dominiPo.init(pagamentiOnlineValues, bd,this.getLanguage()); 
		sezionePO.addField(dominiPo); 


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
			root.addVoce(Utils.getInstance(this.getLanguage()).getMessageFromResourceBundle(this.nomeServizio + ".codPortale.label"), portale.getCodPortale());
			root.addVoce(Utils.getInstance(this.getLanguage()).getMessageFromResourceBundle(this.nomeServizio + ".principal.label"), portale.getPrincipal());
			root.addVoce(Utils.getInstance(this.getLanguage()).getMessageFromResourceBundle(this.nomeServizio + ".defaultCallbackURL.label"), portale.getDefaultCallbackURL());
			root.addVoce(Utils.getInstance(this.getLanguage()).getMessageFromResourceBundle(this.nomeServizio + ".versione.label"), portale.getVersione().getLabel(), true);
			root.addVoce(Utils.getInstance(this.getLanguage()).getMessageFromResourceBundle(this.nomeServizio + ".abilitato.label"), Utils.getSiNoAsLabel(portale.isAbilitato()));

			// Elementi correlati
			String etichettaPagamentiAttesa = Utils.getInstance(this.getLanguage()).getMessageFromResourceBundle(this.nomeServizio + ".elementoCorrelato.pagamentiAttesa.titolo");
			it.govpay.web.rs.dars.model.Sezione sezionePagamentiAttesa = dettaglio.addSezione(etichettaPagamentiAttesa);

			List<Acl> acls = portale.getAcls();

			String etichettaTipiTributo = Utils.getInstance(this.getLanguage()).getMessageFromResourceBundle(this.nomeServizio + ".elementoCorrelato.tipiTributo.titolo");
			String etichettaDomini = Utils.getInstance(this.getLanguage()).getMessageFromResourceBundle(this.nomeServizio + ".elementoCorrelato.domini.titolo");

			List<Long> idTributi = Utils.getIdsFromAcls(acls, Tipo.TRIBUTO , Servizio.PAGAMENTI_ATTESA);
			List<Voce<String>> listaVociTributi = new ArrayList<Voce<String>>();
			String valore = null;
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
					if(findAll != null && findAll.size() > 0){
						for (TipoTributo entry : findAll) {
							Elemento elemento = tipiTributoDarsHandler.getElemento(entry, entry.getId(), tipiTributoDars.getPathServizio(),bd);
							listaVociTributi.add(new VoceRiferimento<String>(elemento.getTitolo(), elemento.getSottotitolo(), elemento.getUri()));
						}
					}
				} else{
					valore = Utils.getInstance(this.getLanguage()).getMessageFromResourceBundle("commons.label.tutti");
				}
			} else {
				valore = Utils.getInstance(this.getLanguage()).getMessageFromResourceBundle("commons.label.nessuno");
			}

			if(Utils.isEmpty(listaVociTributi)){
				sezionePagamentiAttesa.addVoce(etichettaTipiTributo, valore); 
			} else {
				sezionePagamentiAttesa.addVoce(etichettaTipiTributo, null); 
				for (Voce<String> voce : listaVociTributi) {
					sezionePagamentiAttesa.addVoce(voce);
				}
			}


			List<Long> idDomini = Utils.getIdsFromAcls(acls, Tipo.DOMINIO, Servizio.PAGAMENTI_ATTESA);
			List<Voce<String>> listaVociDomini = new ArrayList<Voce<String>>();
			valore = null;
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
							listaVociDomini.add(new VoceRiferimento<String>(elemento.getTitolo(), elemento.getSottotitolo(), elemento.getUri()));
						}
					}
				}else{
					valore = Utils.getInstance(this.getLanguage()).getMessageFromResourceBundle("commons.label.tutti");
				}
			} else {
				valore = Utils.getInstance(this.getLanguage()).getMessageFromResourceBundle("commons.label.nessuno");
			}

			if(Utils.isEmpty(listaVociDomini)){
				sezionePagamentiAttesa.addVoce(etichettaDomini, valore); 
			} else {
				sezionePagamentiAttesa.addVoce(etichettaDomini, null); 
				for (Voce<String> voce : listaVociDomini) {
					sezionePagamentiAttesa.addVoce(voce);
				}
			}

			String etichettaPagamentiOnline = Utils.getInstance(this.getLanguage()).getMessageFromResourceBundle(this.nomeServizio + ".elementoCorrelato.pagamentiOnline.titolo");
			it.govpay.web.rs.dars.model.Sezione sezionePagamentiOnline = dettaglio.addSezione(etichettaPagamentiOnline);



			idTributi = Utils.getIdsFromAcls(acls, Tipo.TRIBUTO , Servizio.PAGAMENTI_ONLINE);
			listaVociTributi = new ArrayList<Voce<String>>();
			valore = null;
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
					if(findAll != null && findAll.size() > 0){
						for (TipoTributo entry : findAll) {
							Elemento elemento = tipiTributoDarsHandler.getElemento(entry, entry.getId(), tipiTributoDars.getPathServizio(),bd);
							listaVociTributi.add(new VoceRiferimento<String>(elemento.getTitolo(), elemento.getSottotitolo(), elemento.getUri()));
						}
					}
				}else{
					valore = Utils.getInstance(this.getLanguage()).getMessageFromResourceBundle("commons.label.tutti");
				}
			} else {
				valore = Utils.getInstance(this.getLanguage()).getMessageFromResourceBundle("commons.label.nessuno");
			}

			sezionePagamentiOnline.addVoce(Utils.getInstance(this.getLanguage()).getMessageFromResourceBundle(this.nomeServizio + ".trusted.label"), Utils.getSiNoAsLabel(portale.isTrusted()));

			if(Utils.isEmpty(listaVociTributi)){
				if(!portale.isTrusted()) {
					sezionePagamentiOnline.addVoce(etichettaTipiTributo, valore);
				} 
			} else {
				sezionePagamentiOnline.addVoce(etichettaTipiTributo, null); 
				for (Voce<String> voce : listaVociTributi) {
					sezionePagamentiOnline.addVoce(voce);
				}
			}

			idDomini = Utils.getIdsFromAcls(acls, Tipo.DOMINIO, Servizio.PAGAMENTI_ONLINE);
			listaVociDomini = new ArrayList<Voce<String>>();
			valore = null;
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
							listaVociDomini.add(new VoceRiferimento<String>(elemento.getTitolo(), elemento.getSottotitolo(), elemento.getUri()));
						}
					}
				}else{
					valore = Utils.getInstance(this.getLanguage()).getMessageFromResourceBundle("commons.label.tutti");
				}
			} else {
				valore = Utils.getInstance(this.getLanguage()).getMessageFromResourceBundle("commons.label.nessuno");
			}

			if(Utils.isEmpty(listaVociDomini)){
				sezionePagamentiOnline.addVoce(etichettaDomini, valore); 
			} else {
				sezionePagamentiOnline.addVoce(etichettaDomini, null); 
				for (Voce<String> voce : listaVociDomini) {
					sezionePagamentiOnline.addVoce(voce);
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
				String msg = Utils.getInstance(this.getLanguage()).getMessageWithParamsFromResourceBundle(this.nomeServizio + ".oggettoEsistente", entry.getCodPortale());
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
		String pagamentiAttesaId = Utils.getInstance(this.getLanguage()).getMessageFromResourceBundle(this.nomeServizio + ".pagamentiAttesa.id");
		String pagamentiOnlineId = Utils.getInstance(this.getLanguage()).getMessageFromResourceBundle(this.nomeServizio + ".pagamentiOnline.id");
		String dominiPaId = Utils.getInstance(this.getLanguage()).getMessageFromResourceBundle(this.nomeServizio + ".dominiPa.id");
		String tipiTributoPaId = Utils.getInstance(this.getLanguage()).getMessageFromResourceBundle(this.nomeServizio + ".tipiTributoPa.id");
		String dominiPoId = Utils.getInstance(this.getLanguage()).getMessageFromResourceBundle(this.nomeServizio + ".dominiPo.id");
		String tipiTributoPoId = Utils.getInstance(this.getLanguage()).getMessageFromResourceBundle(this.nomeServizio + ".tipiTributoPo.id");
		String versioneId = Utils.getInstance(this.getLanguage()).getMessageFromResourceBundle(this.nomeServizio + ".versione.id");
		try{
			this.log.info("Esecuzione " + methodName + " in corso...");
			// Operazione consentita solo all'amministratore
			this.darsService.checkOperatoreAdmin(bd);

			JsonConfig jsonConfig = new JsonConfig();

			Map<String,Class<?>> classMap = new HashMap<String, Class<?>>();
			classMap.put(dominiPaId, Long.class); 
			classMap.put(tipiTributoPaId, Long.class); 
			classMap.put(dominiPoId, Long.class); 
			classMap.put(tipiTributoPoId, Long.class); 
			jsonConfig.setClassMap(classMap);
			ByteArrayOutputStream baos = new ByteArrayOutputStream();
			Utils.copy(is, baos);

			baos.flush();
			baos.close();

			JSONObject jsonObjectPortale = JSONObject.fromObject( baos.toString() );

			List<Acl> lstAclTributiPa = new ArrayList<Acl>();
			List<Acl> lstAclDominiPa = new ArrayList<Acl>();

			if(jsonObjectPortale.getBoolean(pagamentiAttesaId)){
				JSONArray jsonTributi = jsonObjectPortale.getJSONArray(tipiTributoPaId);


				for (int i = 0; i < jsonTributi.size(); i++) {
					long idTributo = jsonTributi.getLong(i);

					Acl acl = new Acl();
					acl.setTipo(Tipo.TRIBUTO);
					acl.setServizio(Servizio.PAGAMENTI_ATTESA);
					if(idTributo > 0){
						acl.setIdTributo(idTributo);
						lstAclTributiPa.add(acl);
					}else {
						lstAclTributiPa.clear();
						lstAclTributiPa.add(acl);
						break;
					}
				}
				JSONArray jsonDomini = jsonObjectPortale.getJSONArray(dominiPaId);

				for (int i = 0; i < jsonDomini.size(); i++) {
					long idDominio = jsonDomini.getLong(i);

					Acl acl = new Acl();
					acl.setTipo(Tipo.DOMINIO);
					acl.setServizio(Servizio.PAGAMENTI_ATTESA);
					if(idDominio > 0){
						acl.setIdDominio(idDominio);
						lstAclDominiPa.add(acl);
					}else {
						lstAclDominiPa.clear();
						lstAclDominiPa.add(acl);
						break;
					}
				}
			}
			// rimuovo gli oggetti della parte PA
			jsonObjectPortale.remove(pagamentiAttesaId);
			jsonObjectPortale.remove(tipiTributoPaId);
			jsonObjectPortale.remove(dominiPaId);


			List<Acl> lstAclTributiPo = new ArrayList<Acl>();
			List<Acl> lstAclDominiPo = new ArrayList<Acl>();

			if(jsonObjectPortale.getBoolean(pagamentiOnlineId)){
				JSONArray jsonTributi = jsonObjectPortale.getJSONArray(tipiTributoPoId);


				for (int i = 0; i < jsonTributi.size(); i++) {
					long idTributo = jsonTributi.getLong(i);

					Acl acl = new Acl();
					acl.setTipo(Tipo.TRIBUTO);
					acl.setServizio(Servizio.PAGAMENTI_ONLINE);
					if(idTributo > 0){
						acl.setIdTributo(idTributo);
						lstAclTributiPo.add(acl);
					}else {
						lstAclTributiPo.clear();
						lstAclTributiPo.add(acl);
						break;
					}
				}
				JSONArray jsonDomini = jsonObjectPortale.getJSONArray(dominiPoId);

				for (int i = 0; i < jsonDomini.size(); i++) {
					long idDominio = jsonDomini.getLong(i);

					Acl acl = new Acl();
					acl.setTipo(Tipo.DOMINIO);
					acl.setServizio(Servizio.PAGAMENTI_ONLINE);
					if(idDominio > 0){
						acl.setIdDominio(idDominio);
						lstAclDominiPo.add(acl);
					}else {
						lstAclDominiPo.clear();
						lstAclDominiPo.add(acl);
						break;
					}
				}
			}
			// rimuovo gli oggetti della parte PA
			jsonObjectPortale.remove(pagamentiOnlineId);
			jsonObjectPortale.remove(tipiTributoPoId);
			jsonObjectPortale.remove(dominiPoId);

			Versione versione = this.getVersioneSelezionata(jsonObjectPortale, versioneId, true); 

			jsonConfig.setRootClass(Portale.class);
			entry = (Portale) JSONObject.toBean( jsonObjectPortale, jsonConfig );

			entry.setVersione(versione); 

			entry.setAcls(lstAclTributiPa);
			entry.getAcls().addAll(lstAclDominiPa);
			entry.getAcls().addAll(lstAclTributiPo);
			entry.getAcls().addAll(lstAclDominiPo);

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

		if(entry.getPrincipal() == null || entry.getPrincipal().isEmpty()) {
			throw new ValidationException("Il campo Principal deve essere valorizzato.");
		}

		if(oldEntry != null) { //caso update
			if(!oldEntry.getCodPortale().equals(entry.getCodPortale())) {
				throw new ValidationException("Cod Portale non deve cambiare in update. Atteso ["+oldEntry.getCodPortale()+"] trovato ["+entry.getCodPortale()+"]");
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
	public List<String> getValori(Portale entry, BasicBD bd) throws ConsoleException {
		return null;
	}
	
	@Override
	public Map<String, Voce<String>> getVoci(Portale entry, BasicBD bd) throws ConsoleException { return null; }

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
