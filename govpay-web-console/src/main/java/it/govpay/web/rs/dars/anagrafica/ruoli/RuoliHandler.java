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
package it.govpay.web.rs.dars.anagrafica.ruoli;

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
import org.openspcoop2.generic_project.exception.ServiceException;
import org.openspcoop2.generic_project.expression.SortOrder;

import it.govpay.bd.BasicBD;
import it.govpay.bd.FilterSortWrapper;
import it.govpay.bd.anagrafica.DominiBD;
import it.govpay.bd.anagrafica.RuoliBD;
import it.govpay.bd.anagrafica.filters.DominioFilter;
import it.govpay.bd.anagrafica.filters.RuoloFilter;
import it.govpay.bd.model.Dominio;
import it.govpay.model.Acl;
import it.govpay.model.Acl.Servizio;
import it.govpay.model.Acl.Tipo;
import it.govpay.model.Ruolo;
import it.govpay.web.rs.dars.anagrafica.ruoli.input.AdminFunzionalita_G_PAG;
import it.govpay.web.rs.dars.anagrafica.ruoli.input.DirittiFunzionalita_A_APP;
import it.govpay.web.rs.dars.anagrafica.ruoli.input.DirittiFunzionalita_A_CON;
import it.govpay.web.rs.dars.anagrafica.ruoli.input.DirittiFunzionalita_A_PPA;
import it.govpay.web.rs.dars.anagrafica.ruoli.input.DirittiFunzionalita_A_USR;
import it.govpay.web.rs.dars.anagrafica.ruoli.input.DirittiFunzionalita_GDE;
import it.govpay.web.rs.dars.anagrafica.ruoli.input.DirittiFunzionalita_G_PAG;
import it.govpay.web.rs.dars.anagrafica.ruoli.input.DirittiFunzionalita_G_RND;
import it.govpay.web.rs.dars.anagrafica.ruoli.input.DominiFunzionalita_A_APP;
import it.govpay.web.rs.dars.anagrafica.ruoli.input.DominiFunzionalita_A_CON;
import it.govpay.web.rs.dars.anagrafica.ruoli.input.DominiFunzionalita_A_PPA;
import it.govpay.web.rs.dars.anagrafica.ruoli.input.DominiFunzionalita_A_USR;
import it.govpay.web.rs.dars.anagrafica.ruoli.input.DominiFunzionalita_GDE;
import it.govpay.web.rs.dars.anagrafica.ruoli.input.DominiFunzionalita_G_PAG;
import it.govpay.web.rs.dars.anagrafica.ruoli.input.DominiFunzionalita_G_RND;
import it.govpay.web.rs.dars.base.DarsHandler;
import it.govpay.web.rs.dars.base.DarsService;
import it.govpay.web.rs.dars.exception.ConsoleException;
import it.govpay.web.rs.dars.exception.DeleteException;
import it.govpay.web.rs.dars.exception.DuplicatedEntryException;
import it.govpay.web.rs.dars.exception.ExportException;
import it.govpay.web.rs.dars.exception.ValidationException;
import it.govpay.web.rs.dars.handler.IDarsHandler;
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
import it.govpay.web.utils.ConsoleProperties;
import it.govpay.web.utils.Utils;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import net.sf.json.JsonConfig;

public class RuoliHandler extends DarsHandler<Ruolo> implements IDarsHandler<Ruolo>{

	public RuoliHandler(Logger log, DarsService darsService) {
		super(log,darsService);
	}

	@Override
	public Elenco getElenco(UriInfo uriInfo,BasicBD bd) throws WebApplicationException,ConsoleException {
		String methodName = "getElenco " + this.titoloServizio;
		try{	
			// Operazione consentita solo agli utenti che hanno almeno un ruolo consentito per la funzionalita'
			this.darsService.checkDirittiServizio(bd, this.funzionalita);

			Integer offset = this.getOffset(uriInfo);
			Integer limit = this.getLimit(uriInfo);

			this.log.info("Esecuzione " + methodName + " in corso..."); 

			boolean simpleSearch = this.containsParameter(uriInfo, DarsService.SIMPLE_SEARCH_PARAMETER_ID);

			RuoliBD ruoliBD = new RuoliBD(bd);
			RuoloFilter filter = ruoliBD.newFilter(simpleSearch);
			filter.setOffset(offset);
			filter.setLimit(limit);
			FilterSortWrapper fsw = new FilterSortWrapper();
			fsw.setField(it.govpay.orm.Ruolo.model().DESCRIZIONE);
			fsw.setSortOrder(SortOrder.ASC);
			filter.getFilterSortList().add(fsw);

			if(simpleSearch){
				// simplesearch
				String simpleSearchString = this.getParameter(uriInfo, DarsService.SIMPLE_SEARCH_PARAMETER_ID, String.class);
				if(StringUtils.isNotEmpty(simpleSearchString)) {
					filter.setSimpleSearchString(simpleSearchString);
				}
			}else{
				String codRuoloId = Utils.getInstance(this.getLanguage()).getMessageFromResourceBundle(this.nomeServizio + ".codRuolo.id");
				String codRuolo = this.getParameter(uriInfo, codRuoloId, String.class);

				if(StringUtils.isNotEmpty(codRuolo)){
					filter.setCodRuolo(codRuolo);
				}
			}
			long count = ruoliBD.count(filter);

			// visualizza la ricerca solo se i risultati sono > del limit
			boolean visualizzaRicerca = this.visualizzaRicerca(count, limit);
			InfoForm infoRicerca = this.getInfoRicerca(uriInfo, bd, visualizzaRicerca);
			String simpleSearchPlaceholder = Utils.getInstance(this.getLanguage()).getMessageFromResourceBundle(this.nomeServizio+".simpleSearch.placeholder");
			Elenco elenco = new Elenco(this.titoloServizio, infoRicerca,
					this.getInfoCreazione(uriInfo, bd),
					count, this.getInfoEsportazione(uriInfo, bd), this.getInfoCancellazione(uriInfo, bd),simpleSearchPlaceholder); 

			List<Ruolo> findAll = ruoliBD.findAll(filter);

			if(findAll != null && findAll.size() > 0){
				for (Ruolo entry : findAll) {
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
			String codRuoloId = Utils.getInstance(this.getLanguage()).getMessageFromResourceBundle(this.nomeServizio + ".codRuolo.id");
			String descrizioneId = Utils.getInstance(this.getLanguage()).getMessageFromResourceBundle(this.nomeServizio + ".descrizione.id");
			
			if(this.infoRicercaMap == null){
				this.initInfoRicerca(uriInfo, bd);

			}
			Sezione sezioneRoot = infoRicerca.getSezioneRoot();

			InputText codRuolo= (InputText) this.infoRicercaMap.get(codRuoloId);
			codRuolo.setDefaultValue(null);
			codRuolo.setEditable(true); 
			sezioneRoot.addField(codRuolo);
			
			InputText descrizione= (InputText) this.infoRicercaMap.get(descrizioneId);
			descrizione.setDefaultValue(null);
			descrizione.setEditable(true); 
			sezioneRoot.addField(descrizione);
		}
		return infoRicerca;
	}

	private void initInfoRicerca(UriInfo uriInfo, BasicBD bd) throws ConsoleException{
		if(this.infoRicercaMap == null){
			this.infoRicercaMap = new HashMap<String, ParamField<?>>();

			String codRuoloId = Utils.getInstance(this.getLanguage()).getMessageFromResourceBundle(this.nomeServizio + ".codRuolo.id");
			String codRuoloLabel = Utils.getInstance(this.getLanguage()).getMessageFromResourceBundle(this.nomeServizio + ".codRuolo.label");
			InputText codRuolo = new InputText(codRuoloId, codRuoloLabel, null, false, false, true, 1, 255);
			this.infoRicercaMap.put(codRuoloId, codRuolo);
			
			String descrizioneId = Utils.getInstance(this.getLanguage()).getMessageFromResourceBundle(this.nomeServizio + ".descrizione.id");
			String descrizioneLabel = Utils.getInstance(this.getLanguage()).getMessageFromResourceBundle(this.nomeServizio + ".descrizione.label");
			InputText descrizione = new InputText(descrizioneId, descrizioneLabel, null, false, false, true, 1, 255);
			this.infoRicercaMap.put(descrizioneId, descrizione);
		}
	}

	@Override
	public InfoForm getInfoCreazione(UriInfo uriInfo, BasicBD bd) throws ConsoleException {
		InfoForm infoCreazione =  null;
		try {
			if(this.darsService.isServizioAbilitatoScrittura(bd, this.funzionalita)){
				URI creazione = this.getUriCreazione(uriInfo, bd);
				infoCreazione = new InfoForm(creazione,Utils.getInstance(this.getLanguage()).getMessageFromResourceBundle(this.nomeServizio + ".creazione.titolo"));

				String ruoloId = Utils.getInstance(this.getLanguage()).getMessageFromResourceBundle(this.nomeServizio + ".id.id");
				String codRuoloId = Utils.getInstance(this.getLanguage()).getMessageFromResourceBundle(this.nomeServizio + ".codRuolo.id");
				String descrizioneId = Utils.getInstance(this.getLanguage()).getMessageFromResourceBundle(this.nomeServizio + ".descrizione.id");

				String funzionalita_A_PPAId = Utils.getInstance(this.getLanguage()).getMessageFromResourceBundle(this.nomeServizio + ".funzionalita_A_PPA.id");
				String domini_A_PPAId = Utils.getInstance(this.getLanguage()).getMessageFromResourceBundle(this.nomeServizio + ".domini_A_PPA.id");
				String diritti_A_PPAId = Utils.getInstance(this.getLanguage()).getMessageFromResourceBundle(this.nomeServizio + ".diritti_A_PPA.id");

				String funzionalita_A_CONId = Utils.getInstance(this.getLanguage()).getMessageFromResourceBundle(this.nomeServizio + ".funzionalita_A_CON.id");
				String domini_A_CONId = Utils.getInstance(this.getLanguage()).getMessageFromResourceBundle(this.nomeServizio + ".domini_A_CON.id");
				String diritti_A_CONId = Utils.getInstance(this.getLanguage()).getMessageFromResourceBundle(this.nomeServizio + ".diritti_A_CON.id");

				String funzionalita_A_APPId = Utils.getInstance(this.getLanguage()).getMessageFromResourceBundle(this.nomeServizio + ".funzionalita_A_APP.id");
				String domini_A_APPId = Utils.getInstance(this.getLanguage()).getMessageFromResourceBundle(this.nomeServizio + ".domini_A_APP.id");
				String diritti_A_APPId = Utils.getInstance(this.getLanguage()).getMessageFromResourceBundle(this.nomeServizio + ".diritti_A_APP.id");

				String funzionalita_A_USRId = Utils.getInstance(this.getLanguage()).getMessageFromResourceBundle(this.nomeServizio + ".funzionalita_A_USR.id");
				String domini_A_USRId = Utils.getInstance(this.getLanguage()).getMessageFromResourceBundle(this.nomeServizio + ".domini_A_USR.id");
				String diritti_A_USRId = Utils.getInstance(this.getLanguage()).getMessageFromResourceBundle(this.nomeServizio + ".diritti_A_USR.id");

				String funzionalita_G_PAGId = Utils.getInstance(this.getLanguage()).getMessageFromResourceBundle(this.nomeServizio + ".funzionalita_G_PAG.id");
				String domini_G_PAGId = Utils.getInstance(this.getLanguage()).getMessageFromResourceBundle(this.nomeServizio + ".domini_G_PAG.id");
				String diritti_G_PAGId = Utils.getInstance(this.getLanguage()).getMessageFromResourceBundle(this.nomeServizio + ".diritti_G_PAG.id");
				String admin_G_PAGId = Utils.getInstance(this.getLanguage()).getMessageFromResourceBundle(this.nomeServizio + ".admin_G_PAG.id");

				String funzionalita_G_RNDId = Utils.getInstance(this.getLanguage()).getMessageFromResourceBundle(this.nomeServizio + ".funzionalita_G_RND.id");
				String domini_G_RNDId = Utils.getInstance(this.getLanguage()).getMessageFromResourceBundle(this.nomeServizio + ".domini_G_RND.id");
				String diritti_G_RNDId = Utils.getInstance(this.getLanguage()).getMessageFromResourceBundle(this.nomeServizio + ".diritti_G_RND.id");

				String funzionalita_GDEId = Utils.getInstance(this.getLanguage()).getMessageFromResourceBundle(this.nomeServizio + ".funzionalita_GDE.id");
				String domini_GDEId = Utils.getInstance(this.getLanguage()).getMessageFromResourceBundle(this.nomeServizio + ".domini_GDE.id");
				String diritti_GDEId = Utils.getInstance(this.getLanguage()).getMessageFromResourceBundle(this.nomeServizio + ".diritti_GDE.id");

				String funzionalita_MANId = Utils.getInstance(this.getLanguage()).getMessageFromResourceBundle(this.nomeServizio + ".funzionalita_MAN.id");
				String funzionalita_STATId = Utils.getInstance(this.getLanguage()).getMessageFromResourceBundle(this.nomeServizio + ".funzionalita_STAT.id");

				if(this.infoCreazioneMap == null){
					this.initInfoCreazione(uriInfo, bd);
				}

				Sezione sezioneRoot = infoCreazione.getSezioneRoot();
				InputNumber idInterm = (InputNumber) this.infoCreazioneMap.get(ruoloId);
				idInterm.setDefaultValue(null);
				sezioneRoot.addField(idInterm);

				InputText codRuolo = (InputText) this.infoCreazioneMap.get(codRuoloId);
				codRuolo.setDefaultValue(null);
				codRuolo.setEditable(true); 
				sezioneRoot.addField(codRuolo);

				InputText descrizione = (InputText) this.infoCreazioneMap.get(descrizioneId);
				descrizione.setDefaultValue(null);
				sezioneRoot.addField(descrizione);

				String etichettaFunzionalita_A_PPA = Utils.getInstance(this.getLanguage()).getMessageFromResourceBundle(this.nomeServizio + ".elementoCorrelato.funzionalita_A_PPA.titolo");
				Sezione sezioneFunzionalita_A_PPA = infoCreazione.addSezione(etichettaFunzionalita_A_PPA);

				CheckButton funzionalita_A_PPA = (CheckButton) this.infoCreazioneMap.get(funzionalita_A_PPAId);
				funzionalita_A_PPA.setDefaultValue(false); 
				sezioneFunzionalita_A_PPA.addField(funzionalita_A_PPA);

				List<RawParamValue> funzionalita_A_PPAValues = new ArrayList<RawParamValue>();
				funzionalita_A_PPAValues.add(new RawParamValue(ruoloId, null));
				funzionalita_A_PPAValues.add(new RawParamValue(funzionalita_A_PPAId, "false"));

				DirittiFunzionalita_A_PPA diritti_A_PPA = (DirittiFunzionalita_A_PPA) this.infoCreazioneMap.get(diritti_A_PPAId);
				diritti_A_PPA.init(funzionalita_A_PPAValues, bd,this.getLanguage()); 
				sezioneFunzionalita_A_PPA.addField(diritti_A_PPA);

				DominiFunzionalita_A_PPA domini_A_PPA = (DominiFunzionalita_A_PPA) this.infoCreazioneMap.get(domini_A_PPAId);
				domini_A_PPA.init(funzionalita_A_PPAValues, bd,this.getLanguage()); 
				sezioneFunzionalita_A_PPA.addField(domini_A_PPA); 

				String etichettaFunzionalita_A_CON = Utils.getInstance(this.getLanguage()).getMessageFromResourceBundle(this.nomeServizio + ".elementoCorrelato.funzionalita_A_CON.titolo");
				Sezione sezioneFunzionalita_A_CON = infoCreazione.addSezione(etichettaFunzionalita_A_CON);

				CheckButton funzionalita_A_CON = (CheckButton) this.infoCreazioneMap.get(funzionalita_A_CONId);
				funzionalita_A_CON.setDefaultValue(false); 
				sezioneFunzionalita_A_CON.addField(funzionalita_A_CON);

				List<RawParamValue> funzionalita_A_CONValues = new ArrayList<RawParamValue>();
				funzionalita_A_CONValues.add(new RawParamValue(ruoloId, null));
				funzionalita_A_CONValues.add(new RawParamValue(funzionalita_A_CONId, "false"));

				DirittiFunzionalita_A_CON diritti_A_CON = (DirittiFunzionalita_A_CON) this.infoCreazioneMap.get(diritti_A_CONId);
				diritti_A_CON.init(funzionalita_A_CONValues, bd,this.getLanguage()); 
				sezioneFunzionalita_A_CON.addField(diritti_A_CON);

				DominiFunzionalita_A_CON domini_A_CON = (DominiFunzionalita_A_CON) this.infoCreazioneMap.get(domini_A_CONId);
				domini_A_CON.init(funzionalita_A_CONValues, bd,this.getLanguage()); 
				sezioneFunzionalita_A_CON.addField(domini_A_CON); 

				String etichettaFunzionalita_A_APP = Utils.getInstance(this.getLanguage()).getMessageFromResourceBundle(this.nomeServizio + ".elementoCorrelato.funzionalita_A_APP.titolo");
				Sezione sezioneFunzionalita_A_APP = infoCreazione.addSezione(etichettaFunzionalita_A_APP);

				CheckButton funzionalita_A_APP = (CheckButton) this.infoCreazioneMap.get(funzionalita_A_APPId);
				funzionalita_A_APP.setDefaultValue(false); 
				sezioneFunzionalita_A_APP.addField(funzionalita_A_APP);

				List<RawParamValue> funzionalita_A_APPValues = new ArrayList<RawParamValue>();
				funzionalita_A_APPValues.add(new RawParamValue(ruoloId, null));
				funzionalita_A_APPValues.add(new RawParamValue(funzionalita_A_APPId, "false"));

				DirittiFunzionalita_A_APP diritti_A_APP = (DirittiFunzionalita_A_APP) this.infoCreazioneMap.get(diritti_A_APPId);
				diritti_A_APP.init(funzionalita_A_APPValues, bd,this.getLanguage()); 
				sezioneFunzionalita_A_APP.addField(diritti_A_APP);

				DominiFunzionalita_A_APP domini_A_APP = (DominiFunzionalita_A_APP) this.infoCreazioneMap.get(domini_A_APPId);
				domini_A_APP.init(funzionalita_A_APPValues, bd,this.getLanguage()); 
				sezioneFunzionalita_A_APP.addField(domini_A_APP); 

				String etichettaFunzionalita_A_USR = Utils.getInstance(this.getLanguage()).getMessageFromResourceBundle(this.nomeServizio + ".elementoCorrelato.funzionalita_A_USR.titolo");
				Sezione sezioneFunzionalita_A_USR = infoCreazione.addSezione(etichettaFunzionalita_A_USR);

				CheckButton funzionalita_A_USR = (CheckButton) this.infoCreazioneMap.get(funzionalita_A_USRId);
				funzionalita_A_USR.setDefaultValue(false); 
				sezioneFunzionalita_A_USR.addField(funzionalita_A_USR);

				List<RawParamValue> funzionalita_A_USRValues = new ArrayList<RawParamValue>();
				funzionalita_A_USRValues.add(new RawParamValue(ruoloId, null));
				funzionalita_A_USRValues.add(new RawParamValue(funzionalita_A_USRId, "false"));

				DirittiFunzionalita_A_USR diritti_A_USR = (DirittiFunzionalita_A_USR) this.infoCreazioneMap.get(diritti_A_USRId);
				diritti_A_USR.init(funzionalita_A_USRValues, bd,this.getLanguage()); 
				sezioneFunzionalita_A_USR.addField(diritti_A_USR);

				DominiFunzionalita_A_USR domini_A_USR = (DominiFunzionalita_A_USR) this.infoCreazioneMap.get(domini_A_USRId);
				domini_A_USR.init(funzionalita_A_USRValues, bd,this.getLanguage()); 
				sezioneFunzionalita_A_USR.addField(domini_A_USR); 

				String etichettaFunzionalita_G_PAG = Utils.getInstance(this.getLanguage()).getMessageFromResourceBundle(this.nomeServizio + ".elementoCorrelato.funzionalita_G_PAG.titolo");
				Sezione sezioneFunzionalita_G_PAG = infoCreazione.addSezione(etichettaFunzionalita_G_PAG);

				CheckButton funzionalita_G_PAG = (CheckButton) this.infoCreazioneMap.get(funzionalita_G_PAGId);
				funzionalita_G_PAG.setDefaultValue(false); 
				sezioneFunzionalita_G_PAG.addField(funzionalita_G_PAG);

				List<RawParamValue> funzionalita_G_PAGValues = new ArrayList<RawParamValue>();
				funzionalita_G_PAGValues.add(new RawParamValue(ruoloId, null));
				funzionalita_G_PAGValues.add(new RawParamValue(funzionalita_G_PAGId, "false"));

				AdminFunzionalita_G_PAG admin_G_PAG = (AdminFunzionalita_G_PAG) this.infoCreazioneMap.get(admin_G_PAGId);
				admin_G_PAG.init(funzionalita_G_PAGValues, bd,this.getLanguage());
				sezioneFunzionalita_G_PAG.addField(admin_G_PAG);

				DirittiFunzionalita_G_PAG diritti_G_PAG = (DirittiFunzionalita_G_PAG) this.infoCreazioneMap.get(diritti_G_PAGId);
				diritti_G_PAG.init(funzionalita_G_PAGValues, bd,this.getLanguage()); 
				sezioneFunzionalita_G_PAG.addField(diritti_G_PAG);

				DominiFunzionalita_G_PAG domini_G_PAG = (DominiFunzionalita_G_PAG) this.infoCreazioneMap.get(domini_G_PAGId);
				domini_G_PAG.init(funzionalita_G_PAGValues, bd,this.getLanguage()); 
				sezioneFunzionalita_G_PAG.addField(domini_G_PAG); 

				String etichettaFunzionalita_G_RND = Utils.getInstance(this.getLanguage()).getMessageFromResourceBundle(this.nomeServizio + ".elementoCorrelato.funzionalita_G_RND.titolo");
				Sezione sezioneFunzionalita_G_RND = infoCreazione.addSezione(etichettaFunzionalita_G_RND);

				CheckButton funzionalita_G_RND = (CheckButton) this.infoCreazioneMap.get(funzionalita_G_RNDId);
				funzionalita_G_RND.setDefaultValue(false); 
				sezioneFunzionalita_G_RND.addField(funzionalita_G_RND);

				List<RawParamValue> funzionalita_G_RNDValues = new ArrayList<RawParamValue>();
				funzionalita_G_RNDValues.add(new RawParamValue(ruoloId, null));
				funzionalita_G_RNDValues.add(new RawParamValue(funzionalita_G_RNDId, "false"));

				DirittiFunzionalita_G_RND diritti_G_RND = (DirittiFunzionalita_G_RND) this.infoCreazioneMap.get(diritti_G_RNDId);
				diritti_G_RND.init(funzionalita_G_RNDValues, bd,this.getLanguage()); 
				sezioneFunzionalita_G_RND.addField(diritti_G_RND);

				DominiFunzionalita_G_RND domini_G_RND = (DominiFunzionalita_G_RND) this.infoCreazioneMap.get(domini_G_RNDId);
				domini_G_RND.init(funzionalita_G_RNDValues, bd,this.getLanguage()); 
				sezioneFunzionalita_G_RND.addField(domini_G_RND); 


				String etichettaFunzionalita_GDE = Utils.getInstance(this.getLanguage()).getMessageFromResourceBundle(this.nomeServizio + ".elementoCorrelato.funzionalita_GDE.titolo");
				Sezione sezioneFunzionalita_GDE = infoCreazione.addSezione(etichettaFunzionalita_GDE);

				CheckButton funzionalita_GDE = (CheckButton) this.infoCreazioneMap.get(funzionalita_GDEId);
				funzionalita_GDE.setDefaultValue(false); 
				sezioneFunzionalita_GDE.addField(funzionalita_GDE);

				List<RawParamValue> funzionalita_GDEValues = new ArrayList<RawParamValue>();
				funzionalita_GDEValues.add(new RawParamValue(ruoloId, null));
				funzionalita_GDEValues.add(new RawParamValue(funzionalita_GDEId, "false"));

				DirittiFunzionalita_GDE diritti_GDE = (DirittiFunzionalita_GDE) this.infoCreazioneMap.get(diritti_GDEId);
				diritti_GDE.init(funzionalita_GDEValues, bd,this.getLanguage()); 
				sezioneFunzionalita_GDE.addField(diritti_GDE);

				DominiFunzionalita_GDE domini_GDE = (DominiFunzionalita_GDE) this.infoCreazioneMap.get(domini_GDEId);
				domini_GDE.init(funzionalita_GDEValues, bd,this.getLanguage()); 
				sezioneFunzionalita_GDE.addField(domini_GDE); 

				String etichettaFunzionalita_MAN = Utils.getInstance(this.getLanguage()).getMessageFromResourceBundle(this.nomeServizio + ".elementoCorrelato.funzionalita_MAN.titolo");
				Sezione sezioneFunzionalita_MAN = infoCreazione.addSezione(etichettaFunzionalita_MAN);

				CheckButton funzionalita_MAN = (CheckButton) this.infoCreazioneMap.get(funzionalita_MANId);
				funzionalita_MAN.setDefaultValue(false); 
				sezioneFunzionalita_MAN.addField(funzionalita_MAN);

				CheckButton funzionalita_STAT = (CheckButton) this.infoCreazioneMap.get(funzionalita_STATId);
				funzionalita_STAT.setDefaultValue(false); 
				funzionalita_STAT.setHidden(!ConsoleProperties.getInstance().isAbilitaFunzionalitaStatistiche());

				if(ConsoleProperties.getInstance().isAbilitaFunzionalitaStatistiche()){
					String etichettaFunzionalita_STAT = Utils.getInstance(this.getLanguage()).getMessageFromResourceBundle(this.nomeServizio + ".elementoCorrelato.funzionalita_STAT.titolo");
					Sezione sezioneFunzionalita_STAT = infoCreazione.addSezione(etichettaFunzionalita_STAT);
					sezioneFunzionalita_STAT.addField(funzionalita_STAT);
				}else {
					sezioneRoot.addField(funzionalita_STAT);
				}

			}
		} catch (ServiceException e) {
			throw new ConsoleException(e);
		}
		return infoCreazione;
	}

	private void initInfoCreazione(UriInfo uriInfo, BasicBD bd) throws ConsoleException{
		if(this.infoCreazioneMap == null){
			this.infoCreazioneMap = new HashMap<String, ParamField<?>>();

			String ruoloId = Utils.getInstance(this.getLanguage()).getMessageFromResourceBundle(this.nomeServizio + ".id.id");
			String codRuoloId = Utils.getInstance(this.getLanguage()).getMessageFromResourceBundle(this.nomeServizio + ".codRuolo.id");
			String descrizioneId = Utils.getInstance(this.getLanguage()).getMessageFromResourceBundle(this.nomeServizio + ".descrizione.id");

			String funzionalita_A_PPAId = Utils.getInstance(this.getLanguage()).getMessageFromResourceBundle(this.nomeServizio + ".funzionalita_A_PPA.id");
			String domini_A_PPAId = Utils.getInstance(this.getLanguage()).getMessageFromResourceBundle(this.nomeServizio + ".domini_A_PPA.id");
			String diritti_A_PPAId = Utils.getInstance(this.getLanguage()).getMessageFromResourceBundle(this.nomeServizio + ".diritti_A_PPA.id");

			String funzionalita_A_CONId = Utils.getInstance(this.getLanguage()).getMessageFromResourceBundle(this.nomeServizio + ".funzionalita_A_CON.id");
			String domini_A_CONId = Utils.getInstance(this.getLanguage()).getMessageFromResourceBundle(this.nomeServizio + ".domini_A_CON.id");
			String diritti_A_CONId = Utils.getInstance(this.getLanguage()).getMessageFromResourceBundle(this.nomeServizio + ".diritti_A_CON.id");

			String funzionalita_A_APPId = Utils.getInstance(this.getLanguage()).getMessageFromResourceBundle(this.nomeServizio + ".funzionalita_A_APP.id");
			String domini_A_APPId = Utils.getInstance(this.getLanguage()).getMessageFromResourceBundle(this.nomeServizio + ".domini_A_APP.id");
			String diritti_A_APPId = Utils.getInstance(this.getLanguage()).getMessageFromResourceBundle(this.nomeServizio + ".diritti_A_APP.id");

			String funzionalita_A_USRId = Utils.getInstance(this.getLanguage()).getMessageFromResourceBundle(this.nomeServizio + ".funzionalita_A_USR.id");
			String domini_A_USRId = Utils.getInstance(this.getLanguage()).getMessageFromResourceBundle(this.nomeServizio + ".domini_A_USR.id");
			String diritti_A_USRId = Utils.getInstance(this.getLanguage()).getMessageFromResourceBundle(this.nomeServizio + ".diritti_A_USR.id");

			String funzionalita_G_PAGId = Utils.getInstance(this.getLanguage()).getMessageFromResourceBundle(this.nomeServizio + ".funzionalita_G_PAG.id");
			String domini_G_PAGId = Utils.getInstance(this.getLanguage()).getMessageFromResourceBundle(this.nomeServizio + ".domini_G_PAG.id");
			String diritti_G_PAGId = Utils.getInstance(this.getLanguage()).getMessageFromResourceBundle(this.nomeServizio + ".diritti_G_PAG.id");
			String admin_G_PAGId = Utils.getInstance(this.getLanguage()).getMessageFromResourceBundle(this.nomeServizio + ".admin_G_PAG.id");

			String funzionalita_G_RNDId = Utils.getInstance(this.getLanguage()).getMessageFromResourceBundle(this.nomeServizio + ".funzionalita_G_RND.id");
			String domini_G_RNDId = Utils.getInstance(this.getLanguage()).getMessageFromResourceBundle(this.nomeServizio + ".domini_G_RND.id");
			String diritti_G_RNDId = Utils.getInstance(this.getLanguage()).getMessageFromResourceBundle(this.nomeServizio + ".diritti_G_RND.id");

			String funzionalita_GDEId = Utils.getInstance(this.getLanguage()).getMessageFromResourceBundle(this.nomeServizio + ".funzionalita_GDE.id");
			String domini_GDEId = Utils.getInstance(this.getLanguage()).getMessageFromResourceBundle(this.nomeServizio + ".domini_GDE.id");
			String diritti_GDEId = Utils.getInstance(this.getLanguage()).getMessageFromResourceBundle(this.nomeServizio + ".diritti_GDE.id");

			String funzionalita_MANId = Utils.getInstance(this.getLanguage()).getMessageFromResourceBundle(this.nomeServizio + ".funzionalita_MAN.id");
			String funzionalita_STATId = Utils.getInstance(this.getLanguage()).getMessageFromResourceBundle(this.nomeServizio + ".funzionalita_STAT.id");

			// id 
			InputNumber id = new InputNumber(ruoloId, null, null, true, true, false, 1, 20);
			this.infoCreazioneMap.put(ruoloId, id);

			// codRuolo
			String codRuoloLabel = Utils.getInstance(this.getLanguage()).getMessageFromResourceBundle(this.nomeServizio + ".codRuolo.label");
			InputText codRuolo = new InputText(codRuoloId, codRuoloLabel, null, true, false, true, 1, 35);
			codRuolo.setSuggestion(Utils.getInstance(this.getLanguage()).getMessageFromResourceBundle(this.nomeServizio + ".codRuolo.suggestion"));
			codRuolo.setValidation(null, Utils.getInstance(this.getLanguage()).getMessageFromResourceBundle(this.nomeServizio + ".codRuolo.errorMessage"));
			this.infoCreazioneMap.put(codRuoloId, codRuolo);

			// descrizione
			String descrizioneLabel = Utils.getInstance(this.getLanguage()).getMessageFromResourceBundle(this.nomeServizio + ".descrizione.label");
			InputText descrizione = new InputText(descrizioneId, descrizioneLabel, null, true, false, true, 1, 255);
			descrizione.setValidation(null, Utils.getInstance(this.getLanguage()).getMessageFromResourceBundle(this.nomeServizio + ".descrizione.errorMessage"));
			this.infoCreazioneMap.put(descrizioneId, descrizione);

			//seziona funzionalita_A_PPA
			// abilitato
			String funzionalita_A_PPALabel = Utils.getInstance(this.getLanguage()).getMessageFromResourceBundle(this.nomeServizio + ".funzionalita_A_PPA.label");
			CheckButton funzionalita_A_PPA = new CheckButton(funzionalita_A_PPAId, funzionalita_A_PPALabel, true, false, false, true);
			this.infoCreazioneMap.put(funzionalita_A_PPAId, funzionalita_A_PPA);

			List<RawParamValue> funzionalita_A_PPAValues = new ArrayList<RawParamValue>();
			funzionalita_A_PPAValues.add(new RawParamValue(ruoloId, null));
			funzionalita_A_PPAValues.add(new RawParamValue(funzionalita_A_PPAId, "false"));

			String diritti_A_PPALabel = Utils.getInstance(this.getLanguage()).getMessageFromResourceBundle(this.nomeServizio + ".diritti_A_PPA.label");
			URI diritti_A_PPARefreshUri = this.getUriField(uriInfo, bd, diritti_A_PPAId); 
			DirittiFunzionalita_A_PPA diritti_A_PPA = new DirittiFunzionalita_A_PPA(this.nomeServizio, diritti_A_PPAId, diritti_A_PPALabel, diritti_A_PPARefreshUri , funzionalita_A_PPAValues, bd,this.getLanguage());
			diritti_A_PPA.addDependencyField(funzionalita_A_PPA);
			diritti_A_PPA.init(funzionalita_A_PPAValues, bd,this.getLanguage()); 
			this.infoCreazioneMap.put(diritti_A_PPAId, diritti_A_PPA);

			String domini_A_PPALabel = Utils.getInstance(this.getLanguage()).getMessageFromResourceBundle(this.nomeServizio + ".domini_A_PPA.label");
			URI domini_A_PPARefreshUri = this.getUriField(uriInfo, bd, domini_A_PPAId); 
			DominiFunzionalita_A_PPA domini_A_PPA = new DominiFunzionalita_A_PPA(this.nomeServizio, domini_A_PPAId, domini_A_PPALabel, domini_A_PPARefreshUri , funzionalita_A_PPAValues, bd,this.getLanguage());
			domini_A_PPA.addDependencyField(funzionalita_A_PPA);
			domini_A_PPA.init(funzionalita_A_PPAValues, bd,this.getLanguage()); 
			this.infoCreazioneMap.put(domini_A_PPAId, domini_A_PPA);

			//seziona funzionalita_A_CON
			// abilitato
			String funzionalita_A_CONLabel = Utils.getInstance(this.getLanguage()).getMessageFromResourceBundle(this.nomeServizio + ".funzionalita_A_CON.label");
			CheckButton funzionalita_A_CON = new CheckButton(funzionalita_A_CONId, funzionalita_A_CONLabel, true, false, false, true);
			this.infoCreazioneMap.put(funzionalita_A_CONId, funzionalita_A_CON);

			List<RawParamValue> funzionalita_A_CONValues = new ArrayList<RawParamValue>();
			funzionalita_A_CONValues.add(new RawParamValue(ruoloId, null));
			funzionalita_A_CONValues.add(new RawParamValue(funzionalita_A_CONId, "false"));

			String diritti_A_CONLabel = Utils.getInstance(this.getLanguage()).getMessageFromResourceBundle(this.nomeServizio + ".diritti_A_CON.label");
			URI diritti_A_CONRefreshUri = this.getUriField(uriInfo, bd, diritti_A_CONId); 
			DirittiFunzionalita_A_CON diritti_A_CON = new DirittiFunzionalita_A_CON(this.nomeServizio, diritti_A_CONId, diritti_A_CONLabel, diritti_A_CONRefreshUri , funzionalita_A_CONValues, bd,this.getLanguage());
			diritti_A_CON.addDependencyField(funzionalita_A_CON);
			diritti_A_CON.init(funzionalita_A_CONValues, bd,this.getLanguage()); 
			this.infoCreazioneMap.put(diritti_A_CONId, diritti_A_CON);

			String domini_A_CONLabel = Utils.getInstance(this.getLanguage()).getMessageFromResourceBundle(this.nomeServizio + ".domini_A_CON.label");
			URI domini_A_CONRefreshUri = this.getUriField(uriInfo, bd, domini_A_CONId); 
			DominiFunzionalita_A_CON domini_A_CON = new DominiFunzionalita_A_CON(this.nomeServizio, domini_A_CONId, domini_A_CONLabel, domini_A_CONRefreshUri , funzionalita_A_CONValues, bd,this.getLanguage());
			domini_A_CON.addDependencyField(funzionalita_A_CON);
			domini_A_CON.init(funzionalita_A_CONValues, bd,this.getLanguage()); 
			this.infoCreazioneMap.put(domini_A_CONId, domini_A_CON);

			//seziona funzionalita_A_APP
			// abilitato
			String funzionalita_A_APPLabel = Utils.getInstance(this.getLanguage()).getMessageFromResourceBundle(this.nomeServizio + ".funzionalita_A_APP.label");
			CheckButton funzionalita_A_APP = new CheckButton(funzionalita_A_APPId, funzionalita_A_APPLabel, true, false, false, true);
			this.infoCreazioneMap.put(funzionalita_A_APPId, funzionalita_A_APP);

			List<RawParamValue> funzionalita_A_APPValues = new ArrayList<RawParamValue>();
			funzionalita_A_APPValues.add(new RawParamValue(ruoloId, null));
			funzionalita_A_APPValues.add(new RawParamValue(funzionalita_A_APPId, "false"));

			String diritti_A_APPLabel = Utils.getInstance(this.getLanguage()).getMessageFromResourceBundle(this.nomeServizio + ".diritti_A_APP.label");
			URI diritti_A_APPRefreshUri = this.getUriField(uriInfo, bd, diritti_A_APPId); 
			DirittiFunzionalita_A_APP diritti_A_APP = new DirittiFunzionalita_A_APP(this.nomeServizio, diritti_A_APPId, diritti_A_APPLabel, diritti_A_APPRefreshUri , funzionalita_A_APPValues, bd,this.getLanguage());
			diritti_A_APP.addDependencyField(funzionalita_A_APP);
			diritti_A_APP.init(funzionalita_A_APPValues, bd,this.getLanguage()); 
			this.infoCreazioneMap.put(diritti_A_APPId, diritti_A_APP);

			String domini_A_APPLabel = Utils.getInstance(this.getLanguage()).getMessageFromResourceBundle(this.nomeServizio + ".domini_A_APP.label");
			URI domini_A_APPRefreshUri = this.getUriField(uriInfo, bd, domini_A_APPId); 
			DominiFunzionalita_A_APP domini_A_APP = new DominiFunzionalita_A_APP(this.nomeServizio, domini_A_APPId, domini_A_APPLabel, domini_A_APPRefreshUri , funzionalita_A_APPValues, bd,this.getLanguage());
			domini_A_APP.addDependencyField(funzionalita_A_APP);
			domini_A_APP.init(funzionalita_A_APPValues, bd,this.getLanguage()); 
			this.infoCreazioneMap.put(domini_A_APPId, domini_A_APP);

			//seziona funzionalita_A_USR
			// abilitato
			String funzionalita_A_USRLabel = Utils.getInstance(this.getLanguage()).getMessageFromResourceBundle(this.nomeServizio + ".funzionalita_A_USR.label");
			CheckButton funzionalita_A_USR = new CheckButton(funzionalita_A_USRId, funzionalita_A_USRLabel, true, false, false, true);
			this.infoCreazioneMap.put(funzionalita_A_USRId, funzionalita_A_USR);

			List<RawParamValue> funzionalita_A_USRValues = new ArrayList<RawParamValue>();
			funzionalita_A_USRValues.add(new RawParamValue(ruoloId, null));
			funzionalita_A_USRValues.add(new RawParamValue(funzionalita_A_USRId, "false"));

			String diritti_A_USRLabel = Utils.getInstance(this.getLanguage()).getMessageFromResourceBundle(this.nomeServizio + ".diritti_A_USR.label");
			URI diritti_A_USRRefreshUri = this.getUriField(uriInfo, bd, diritti_A_USRId); 
			DirittiFunzionalita_A_USR diritti_A_USR = new DirittiFunzionalita_A_USR(this.nomeServizio, diritti_A_USRId, diritti_A_USRLabel, diritti_A_USRRefreshUri , funzionalita_A_USRValues, bd,this.getLanguage());
			diritti_A_USR.addDependencyField(funzionalita_A_USR);
			diritti_A_USR.init(funzionalita_A_USRValues, bd,this.getLanguage()); 
			this.infoCreazioneMap.put(diritti_A_USRId, diritti_A_USR);

			String domini_A_USRLabel = Utils.getInstance(this.getLanguage()).getMessageFromResourceBundle(this.nomeServizio + ".domini_A_USR.label");
			URI domini_A_USRRefreshUri = this.getUriField(uriInfo, bd, domini_A_USRId); 
			DominiFunzionalita_A_USR domini_A_USR = new DominiFunzionalita_A_USR(this.nomeServizio, domini_A_USRId, domini_A_USRLabel, domini_A_USRRefreshUri , funzionalita_A_USRValues, bd,this.getLanguage());
			domini_A_USR.addDependencyField(funzionalita_A_USR);
			domini_A_USR.init(funzionalita_A_USRValues, bd,this.getLanguage()); 
			this.infoCreazioneMap.put(domini_A_USRId, domini_A_USR);

			//seziona funzionalita_A_PPA
			// abilitato
			String funzionalita_G_PAGLabel = Utils.getInstance(this.getLanguage()).getMessageFromResourceBundle(this.nomeServizio + ".funzionalita_G_PAG.label");
			CheckButton funzionalita_G_PAG = new CheckButton(funzionalita_G_PAGId, funzionalita_G_PAGLabel, true, false, false, true);
			this.infoCreazioneMap.put(funzionalita_G_PAGId, funzionalita_G_PAG);

			List<RawParamValue> funzionalita_G_PAGValues = new ArrayList<RawParamValue>();
			funzionalita_G_PAGValues.add(new RawParamValue(ruoloId, null));
			funzionalita_G_PAGValues.add(new RawParamValue(funzionalita_G_PAGId, "false"));

			String diritti_G_PAGLabel = Utils.getInstance(this.getLanguage()).getMessageFromResourceBundle(this.nomeServizio + ".diritti_G_PAG.label");
			URI diritti_G_PAGRefreshUri = this.getUriField(uriInfo, bd, diritti_G_PAGId); 
			DirittiFunzionalita_G_PAG diritti_G_PAG = new DirittiFunzionalita_G_PAG(this.nomeServizio, diritti_G_PAGId, diritti_G_PAGLabel, diritti_G_PAGRefreshUri , funzionalita_G_PAGValues, bd,this.getLanguage());
			diritti_G_PAG.addDependencyField(funzionalita_G_PAG);
			diritti_G_PAG.init(funzionalita_G_PAGValues, bd,this.getLanguage()); 
			this.infoCreazioneMap.put(diritti_G_PAGId, diritti_G_PAG);

			String domini_G_PAGLabel = Utils.getInstance(this.getLanguage()).getMessageFromResourceBundle(this.nomeServizio + ".domini_G_PAG.label");
			URI domini_G_PAGRefreshUri = this.getUriField(uriInfo, bd, domini_G_PAGId); 
			DominiFunzionalita_G_PAG domini_G_PAG = new DominiFunzionalita_G_PAG(this.nomeServizio, domini_G_PAGId, domini_G_PAGLabel, domini_G_PAGRefreshUri , funzionalita_G_PAGValues, bd,this.getLanguage());
			domini_G_PAG.addDependencyField(funzionalita_G_PAG);
			domini_G_PAG.init(funzionalita_G_PAGValues, bd,this.getLanguage()); 
			this.infoCreazioneMap.put(domini_G_PAGId, domini_G_PAG);

			String admin_G_PAGLabel = Utils.getInstance(this.getLanguage()).getMessageFromResourceBundle(this.nomeServizio + ".admin_G_PAG.label");
			URI admin_G_PAGRefreshUri = this.getUriField(uriInfo, bd, domini_G_PAGId); 
			AdminFunzionalita_G_PAG admin_G_PAG = new AdminFunzionalita_G_PAG(this.nomeServizio, admin_G_PAGId, admin_G_PAGLabel, admin_G_PAGRefreshUri, funzionalita_G_PAGValues, this.getLanguage());
			admin_G_PAG.addDependencyField(funzionalita_G_PAG);
			admin_G_PAG.init(funzionalita_G_PAGValues, bd,this.getLanguage()); 
			this.infoCreazioneMap.put(admin_G_PAGId, admin_G_PAG);

			//seziona funzionalita_G_RND
			// abilitato
			String funzionalita_G_RNDLabel = Utils.getInstance(this.getLanguage()).getMessageFromResourceBundle(this.nomeServizio + ".funzionalita_G_RND.label");
			CheckButton funzionalita_G_RND = new CheckButton(funzionalita_G_RNDId, funzionalita_G_RNDLabel, true, false, false, true);
			this.infoCreazioneMap.put(funzionalita_G_RNDId, funzionalita_G_RND);

			List<RawParamValue> funzionalita_G_RNDValues = new ArrayList<RawParamValue>();
			funzionalita_G_RNDValues.add(new RawParamValue(ruoloId, null));
			funzionalita_G_RNDValues.add(new RawParamValue(funzionalita_G_RNDId, "false"));

			String diritti_G_RNDLabel = Utils.getInstance(this.getLanguage()).getMessageFromResourceBundle(this.nomeServizio + ".diritti_G_RND.label");
			URI diritti_G_RNDRefreshUri = this.getUriField(uriInfo, bd, diritti_G_RNDId); 
			DirittiFunzionalita_G_RND diritti_G_RND = new DirittiFunzionalita_G_RND(this.nomeServizio, diritti_G_RNDId, diritti_G_RNDLabel, diritti_G_RNDRefreshUri , funzionalita_G_RNDValues, bd,this.getLanguage());
			diritti_G_RND.addDependencyField(funzionalita_G_RND);
			diritti_G_RND.init(funzionalita_G_RNDValues, bd,this.getLanguage()); 
			this.infoCreazioneMap.put(diritti_G_RNDId, diritti_G_RND);

			String domini_G_RNDLabel = Utils.getInstance(this.getLanguage()).getMessageFromResourceBundle(this.nomeServizio + ".domini_G_RND.label");
			URI domini_G_RNDRefreshUri = this.getUriField(uriInfo, bd, domini_G_RNDId); 
			DominiFunzionalita_G_RND domini_G_RND = new DominiFunzionalita_G_RND(this.nomeServizio, domini_G_RNDId, domini_G_RNDLabel, domini_G_RNDRefreshUri , funzionalita_G_RNDValues, bd,this.getLanguage());
			domini_G_RND.addDependencyField(funzionalita_G_RND);
			domini_G_RND.init(funzionalita_G_RNDValues, bd,this.getLanguage()); 
			this.infoCreazioneMap.put(domini_G_RNDId, domini_G_RND);

			//seziona funzionalita_GDE
			// abilitato
			String funzionalita_GDELabel = Utils.getInstance(this.getLanguage()).getMessageFromResourceBundle(this.nomeServizio + ".funzionalita_GDE.label");
			CheckButton funzionalita_GDE = new CheckButton(funzionalita_GDEId, funzionalita_GDELabel, true, false, false, true);
			this.infoCreazioneMap.put(funzionalita_GDEId, funzionalita_GDE);

			List<RawParamValue> funzionalita_GDEValues = new ArrayList<RawParamValue>();
			funzionalita_GDEValues.add(new RawParamValue(ruoloId, null));
			funzionalita_GDEValues.add(new RawParamValue(funzionalita_GDEId, "false"));

			String diritti_GDELabel = Utils.getInstance(this.getLanguage()).getMessageFromResourceBundle(this.nomeServizio + ".diritti_GDE.label");
			URI diritti_GDERefreshUri = this.getUriField(uriInfo, bd, diritti_GDEId); 
			DirittiFunzionalita_GDE diritti_GDE = new DirittiFunzionalita_GDE(this.nomeServizio, diritti_GDEId, diritti_GDELabel, diritti_GDERefreshUri , funzionalita_GDEValues, bd,this.getLanguage());
			diritti_GDE.addDependencyField(funzionalita_GDE);
			diritti_GDE.init(funzionalita_GDEValues, bd,this.getLanguage()); 
			this.infoCreazioneMap.put(diritti_GDEId, diritti_GDE);

			String domini_GDELabel = Utils.getInstance(this.getLanguage()).getMessageFromResourceBundle(this.nomeServizio + ".domini_GDE.label");
			URI domini_GDERefreshUri = this.getUriField(uriInfo, bd, domini_GDEId); 
			DominiFunzionalita_GDE domini_GDE = new DominiFunzionalita_GDE(this.nomeServizio, domini_GDEId, domini_GDELabel, domini_GDERefreshUri , funzionalita_GDEValues, bd,this.getLanguage());
			domini_GDE.addDependencyField(funzionalita_GDE);
			domini_GDE.init(funzionalita_GDEValues, bd,this.getLanguage()); 
			this.infoCreazioneMap.put(domini_GDEId, domini_GDE);

			//seziona funzionalita_MAN
			// abilitato
			String funzionalita_MANLabel = Utils.getInstance(this.getLanguage()).getMessageFromResourceBundle(this.nomeServizio + ".funzionalita_MAN.label");
			CheckButton funzionalita_MAN = new CheckButton(funzionalita_MANId, funzionalita_MANLabel, true, false, false, true);
			this.infoCreazioneMap.put(funzionalita_MANId, funzionalita_MAN);

			//seziona funzionalita_STAT
			// abilitato
			String funzionalita_STATLabel = Utils.getInstance(this.getLanguage()).getMessageFromResourceBundle(this.nomeServizio + ".funzionalita_STAT.label");
			CheckButton funzionalita_STAT = new CheckButton(funzionalita_STATId, funzionalita_STATLabel, true, false, false, true);
			this.infoCreazioneMap.put(funzionalita_STATId, funzionalita_STAT);
		}
	}

	@Override
	public InfoForm getInfoModifica(UriInfo uriInfo, BasicBD bd, Ruolo entry) throws ConsoleException {
		InfoForm infoModifica = null;
		try {
			if(this.darsService.isServizioAbilitatoScrittura(bd, this.funzionalita)){
				URI modifica = this.getUriModifica(uriInfo, bd);
				infoModifica = new InfoForm(modifica,Utils.getInstance(this.getLanguage()).getMessageFromResourceBundle(this.nomeServizio + ".modifica.titolo"));

				String ruoloId = Utils.getInstance(this.getLanguage()).getMessageFromResourceBundle(this.nomeServizio + ".id.id");
				String codRuoloId = Utils.getInstance(this.getLanguage()).getMessageFromResourceBundle(this.nomeServizio + ".codRuolo.id");
				String descrizioneId = Utils.getInstance(this.getLanguage()).getMessageFromResourceBundle(this.nomeServizio + ".descrizione.id");
				String funzionalita_A_PPAId = Utils.getInstance(this.getLanguage()).getMessageFromResourceBundle(this.nomeServizio + ".funzionalita_A_PPA.id");
				String domini_A_PPAId = Utils.getInstance(this.getLanguage()).getMessageFromResourceBundle(this.nomeServizio + ".domini_A_PPA.id");
				String diritti_A_PPAId = Utils.getInstance(this.getLanguage()).getMessageFromResourceBundle(this.nomeServizio + ".diritti_A_PPA.id");

				String funzionalita_A_CONId = Utils.getInstance(this.getLanguage()).getMessageFromResourceBundle(this.nomeServizio + ".funzionalita_A_CON.id");
				String domini_A_CONId = Utils.getInstance(this.getLanguage()).getMessageFromResourceBundle(this.nomeServizio + ".domini_A_CON.id");
				String diritti_A_CONId = Utils.getInstance(this.getLanguage()).getMessageFromResourceBundle(this.nomeServizio + ".diritti_A_CON.id");

				String funzionalita_A_APPId = Utils.getInstance(this.getLanguage()).getMessageFromResourceBundle(this.nomeServizio + ".funzionalita_A_APP.id");
				String domini_A_APPId = Utils.getInstance(this.getLanguage()).getMessageFromResourceBundle(this.nomeServizio + ".domini_A_APP.id");
				String diritti_A_APPId = Utils.getInstance(this.getLanguage()).getMessageFromResourceBundle(this.nomeServizio + ".diritti_A_APP.id");

				String funzionalita_A_USRId = Utils.getInstance(this.getLanguage()).getMessageFromResourceBundle(this.nomeServizio + ".funzionalita_A_USR.id");
				String domini_A_USRId = Utils.getInstance(this.getLanguage()).getMessageFromResourceBundle(this.nomeServizio + ".domini_A_USR.id");
				String diritti_A_USRId = Utils.getInstance(this.getLanguage()).getMessageFromResourceBundle(this.nomeServizio + ".diritti_A_USR.id");

				String funzionalita_G_PAGId = Utils.getInstance(this.getLanguage()).getMessageFromResourceBundle(this.nomeServizio + ".funzionalita_G_PAG.id");
				String domini_G_PAGId = Utils.getInstance(this.getLanguage()).getMessageFromResourceBundle(this.nomeServizio + ".domini_G_PAG.id");
				String diritti_G_PAGId = Utils.getInstance(this.getLanguage()).getMessageFromResourceBundle(this.nomeServizio + ".diritti_G_PAG.id");
				String admin_G_PAGId = Utils.getInstance(this.getLanguage()).getMessageFromResourceBundle(this.nomeServizio + ".admin_G_PAG.id");

				String funzionalita_G_RNDId = Utils.getInstance(this.getLanguage()).getMessageFromResourceBundle(this.nomeServizio + ".funzionalita_G_RND.id");
				String domini_G_RNDId = Utils.getInstance(this.getLanguage()).getMessageFromResourceBundle(this.nomeServizio + ".domini_G_RND.id");
				String diritti_G_RNDId = Utils.getInstance(this.getLanguage()).getMessageFromResourceBundle(this.nomeServizio + ".diritti_G_RND.id");

				String funzionalita_GDEId = Utils.getInstance(this.getLanguage()).getMessageFromResourceBundle(this.nomeServizio + ".funzionalita_GDE.id");
				String domini_GDEId = Utils.getInstance(this.getLanguage()).getMessageFromResourceBundle(this.nomeServizio + ".domini_GDE.id");
				String diritti_GDEId = Utils.getInstance(this.getLanguage()).getMessageFromResourceBundle(this.nomeServizio + ".diritti_GDE.id");

				String funzionalita_MANId = Utils.getInstance(this.getLanguage()).getMessageFromResourceBundle(this.nomeServizio + ".funzionalita_MAN.id");
				String funzionalita_STATId = Utils.getInstance(this.getLanguage()).getMessageFromResourceBundle(this.nomeServizio + ".funzionalita_STAT.id");

				if(this.infoCreazioneMap == null){
					this.initInfoCreazione(uriInfo, bd);
				}

				Sezione sezioneRoot = infoModifica.getSezioneRoot();
				InputNumber idInterm = (InputNumber) this.infoCreazioneMap.get(ruoloId);
				idInterm.setDefaultValue(entry.getId());
				sezioneRoot.addField(idInterm);

				InputText codRuolo = (InputText) this.infoCreazioneMap.get(codRuoloId);
				codRuolo.setDefaultValue(entry.getCodRuolo());
				codRuolo.setEditable(false); 
				sezioneRoot.addField(codRuolo);

				InputText descrizione = (InputText) this.infoCreazioneMap.get(descrizioneId);
				descrizione.setDefaultValue(entry.getDescrizione());
				sezioneRoot.addField(descrizione);

				String etichettaFunzionalita_A_PPA = Utils.getInstance(this.getLanguage()).getMessageFromResourceBundle(this.nomeServizio + ".elementoCorrelato.funzionalita_A_PPA.titolo");
				Sezione sezioneFunzionalita_A_PPA = infoModifica.addSezione(etichettaFunzionalita_A_PPA);
				List<Long> idsAclFunzionalita_A_PPA = Utils.getIdsFromAcls(entry.getAcls(), Tipo.DOMINIO, Servizio.Anagrafica_PagoPa);
				boolean visualizzaA_PPA = idsAclFunzionalita_A_PPA.size() > 0;

				CheckButton funzionalita_A_PPA = (CheckButton) this.infoCreazioneMap.get(funzionalita_A_PPAId);
				funzionalita_A_PPA.setDefaultValue(visualizzaA_PPA); 
				sezioneFunzionalita_A_PPA.addField(funzionalita_A_PPA);

				List<RawParamValue> funzionalita_A_PPAValues = new ArrayList<RawParamValue>();
				funzionalita_A_PPAValues.add(new RawParamValue(ruoloId, entry.getId()+""));
				funzionalita_A_PPAValues.add(new RawParamValue(funzionalita_A_PPAId, (visualizzaA_PPA ? "true" : "false")));

				DirittiFunzionalita_A_PPA diritti_A_PPA = (DirittiFunzionalita_A_PPA) this.infoCreazioneMap.get(diritti_A_PPAId);
				diritti_A_PPA.init(funzionalita_A_PPAValues, bd,this.getLanguage()); 
				sezioneFunzionalita_A_PPA.addField(diritti_A_PPA);

				DominiFunzionalita_A_PPA domini_A_PPA = (DominiFunzionalita_A_PPA) this.infoCreazioneMap.get(domini_A_PPAId);
				domini_A_PPA.init(funzionalita_A_PPAValues, bd,this.getLanguage()); 
				sezioneFunzionalita_A_PPA.addField(domini_A_PPA); 

				String etichettaFunzionalita_A_CON = Utils.getInstance(this.getLanguage()).getMessageFromResourceBundle(this.nomeServizio + ".elementoCorrelato.funzionalita_A_CON.titolo");
				Sezione sezioneFunzionalita_A_CON = infoModifica.addSezione(etichettaFunzionalita_A_CON);
				List<Long> idsAclFunzionalita_A_CON = Utils.getIdsFromAcls(entry.getAcls(), Tipo.DOMINIO, Servizio.Anagrafica_Contabile);
				boolean visualizzaA_CON = idsAclFunzionalita_A_CON.size() > 0;

				CheckButton funzionalita_A_CON = (CheckButton) this.infoCreazioneMap.get(funzionalita_A_CONId);
				funzionalita_A_CON.setDefaultValue(visualizzaA_CON); 
				sezioneFunzionalita_A_CON.addField(funzionalita_A_CON);

				List<RawParamValue> funzionalita_A_CONValues = new ArrayList<RawParamValue>();
				funzionalita_A_CONValues.add(new RawParamValue(ruoloId, entry.getId()+""));
				funzionalita_A_CONValues.add(new RawParamValue(funzionalita_A_CONId, (visualizzaA_CON ? "true" : "false")));

				DirittiFunzionalita_A_CON diritti_A_CON = (DirittiFunzionalita_A_CON) this.infoCreazioneMap.get(diritti_A_CONId);
				diritti_A_CON.init(funzionalita_A_CONValues, bd,this.getLanguage()); 
				sezioneFunzionalita_A_CON.addField(diritti_A_CON);

				DominiFunzionalita_A_CON domini_A_CON = (DominiFunzionalita_A_CON) this.infoCreazioneMap.get(domini_A_CONId);
				domini_A_CON.init(funzionalita_A_CONValues, bd,this.getLanguage()); 
				sezioneFunzionalita_A_CON.addField(domini_A_CON); 

				String etichettaFunzionalita_A_APP = Utils.getInstance(this.getLanguage()).getMessageFromResourceBundle(this.nomeServizio + ".elementoCorrelato.funzionalita_A_APP.titolo");
				Sezione sezioneFunzionalita_A_APP = infoModifica.addSezione(etichettaFunzionalita_A_APP);
				List<Long> idsAclFunzionalita_A_APP = Utils.getIdsFromAcls(entry.getAcls(), Tipo.DOMINIO, Servizio.Anagrafica_Applicazioni);
				boolean visualizzaA_APP = idsAclFunzionalita_A_APP.size() > 0;

				CheckButton funzionalita_A_APP = (CheckButton) this.infoCreazioneMap.get(funzionalita_A_APPId);
				funzionalita_A_APP.setDefaultValue(visualizzaA_APP); 
				sezioneFunzionalita_A_APP.addField(funzionalita_A_APP);

				List<RawParamValue> funzionalita_A_APPValues = new ArrayList<RawParamValue>();
				funzionalita_A_APPValues.add(new RawParamValue(ruoloId, entry.getId()+""));
				funzionalita_A_APPValues.add(new RawParamValue(funzionalita_A_APPId, (visualizzaA_APP ? "true" : "false")));

				DirittiFunzionalita_A_APP diritti_A_APP = (DirittiFunzionalita_A_APP) this.infoCreazioneMap.get(diritti_A_APPId);
				diritti_A_APP.init(funzionalita_A_APPValues, bd,this.getLanguage()); 
				sezioneFunzionalita_A_APP.addField(diritti_A_APP);

				DominiFunzionalita_A_APP domini_A_APP = (DominiFunzionalita_A_APP) this.infoCreazioneMap.get(domini_A_APPId);
				domini_A_APP.init(funzionalita_A_APPValues, bd,this.getLanguage()); 
				sezioneFunzionalita_A_APP.addField(domini_A_APP); 

				String etichettaFunzionalita_A_USR = Utils.getInstance(this.getLanguage()).getMessageFromResourceBundle(this.nomeServizio + ".elementoCorrelato.funzionalita_A_USR.titolo");
				Sezione sezioneFunzionalita_A_USR = infoModifica.addSezione(etichettaFunzionalita_A_USR);
				List<Long> idsAclFunzionalita_A_USR = Utils.getIdsFromAcls(entry.getAcls(), Tipo.DOMINIO, Servizio.Anagrafica_Utenti);
				boolean visualizzaA_USR = idsAclFunzionalita_A_USR.size() > 0;

				CheckButton funzionalita_A_USR = (CheckButton) this.infoCreazioneMap.get(funzionalita_A_USRId);
				funzionalita_A_USR.setDefaultValue(visualizzaA_USR); 
				sezioneFunzionalita_A_USR.addField(funzionalita_A_USR);

				List<RawParamValue> funzionalita_A_USRValues = new ArrayList<RawParamValue>();
				funzionalita_A_USRValues.add(new RawParamValue(ruoloId, entry.getId()+""));
				funzionalita_A_USRValues.add(new RawParamValue(funzionalita_A_USRId, (visualizzaA_USR ? "true" : "false")));

				DirittiFunzionalita_A_USR diritti_A_USR = (DirittiFunzionalita_A_USR) this.infoCreazioneMap.get(diritti_A_USRId);
				diritti_A_USR.init(funzionalita_A_USRValues, bd,this.getLanguage()); 
				sezioneFunzionalita_A_USR.addField(diritti_A_USR);

				DominiFunzionalita_A_USR domini_A_USR = (DominiFunzionalita_A_USR) this.infoCreazioneMap.get(domini_A_USRId);
				domini_A_USR.init(funzionalita_A_USRValues, bd,this.getLanguage()); 
				sezioneFunzionalita_A_USR.addField(domini_A_USR); 

				String etichettaFunzionalita_G_PAG = Utils.getInstance(this.getLanguage()).getMessageFromResourceBundle(this.nomeServizio + ".elementoCorrelato.funzionalita_G_PAG.titolo");
				Sezione sezioneFunzionalita_G_PAG = infoModifica.addSezione(etichettaFunzionalita_G_PAG);
				List<Long> idsAclFunzionalita_G_PAG = Utils.getIdsFromAcls(entry.getAcls(), Tipo.DOMINIO, Servizio.Gestione_Pagamenti);
				boolean visualizzaG_PAG = idsAclFunzionalita_G_PAG.size() > 0;

				CheckButton funzionalita_G_PAG = (CheckButton) this.infoCreazioneMap.get(funzionalita_G_PAGId);
				funzionalita_G_PAG.setDefaultValue(visualizzaG_PAG); 
				sezioneFunzionalita_G_PAG.addField(funzionalita_G_PAG);

				List<RawParamValue> funzionalita_G_PAGValues = new ArrayList<RawParamValue>();
				funzionalita_G_PAGValues.add(new RawParamValue(ruoloId, entry.getId()+""));
				funzionalita_G_PAGValues.add(new RawParamValue(funzionalita_G_PAGId, (visualizzaG_PAG ? "true" : "false")));

				AdminFunzionalita_G_PAG admin_G_PAG = (AdminFunzionalita_G_PAG) this.infoCreazioneMap.get(admin_G_PAGId);
				admin_G_PAG.init(funzionalita_G_PAGValues, bd,this.getLanguage());
				sezioneFunzionalita_G_PAG.addField(admin_G_PAG);

				DirittiFunzionalita_G_PAG diritti_G_PAG = (DirittiFunzionalita_G_PAG) this.infoCreazioneMap.get(diritti_G_PAGId);
				diritti_G_PAG.init(funzionalita_G_PAGValues, bd,this.getLanguage()); 
				sezioneFunzionalita_G_PAG.addField(diritti_G_PAG);

				DominiFunzionalita_G_PAG domini_G_PAG = (DominiFunzionalita_G_PAG) this.infoCreazioneMap.get(domini_G_PAGId);
				domini_G_PAG.init(funzionalita_G_PAGValues, bd,this.getLanguage()); 
				sezioneFunzionalita_G_PAG.addField(domini_G_PAG); 

				String etichettaFunzionalita_G_RND = Utils.getInstance(this.getLanguage()).getMessageFromResourceBundle(this.nomeServizio + ".elementoCorrelato.funzionalita_G_RND.titolo");
				Sezione sezioneFunzionalita_G_RND = infoModifica.addSezione(etichettaFunzionalita_G_RND);
				List<Long> idsAclFunzionalita_G_RND = Utils.getIdsFromAcls(entry.getAcls(), Tipo.DOMINIO, Servizio.Gestione_Rendicontazioni);
				boolean visualizzaG_RND = idsAclFunzionalita_G_RND.size() > 0;

				CheckButton funzionalita_G_RND = (CheckButton) this.infoCreazioneMap.get(funzionalita_G_RNDId);
				funzionalita_G_RND.setDefaultValue(visualizzaG_RND); 
				sezioneFunzionalita_G_RND.addField(funzionalita_G_RND);

				List<RawParamValue> funzionalita_G_RNDValues = new ArrayList<RawParamValue>();
				funzionalita_G_RNDValues.add(new RawParamValue(ruoloId, entry.getId()+""));
				funzionalita_G_RNDValues.add(new RawParamValue(funzionalita_G_RNDId, (visualizzaG_RND ? "true" : "false")));

				DirittiFunzionalita_G_RND diritti_G_RND = (DirittiFunzionalita_G_RND) this.infoCreazioneMap.get(diritti_G_RNDId);
				diritti_G_RND.init(funzionalita_G_RNDValues, bd,this.getLanguage()); 
				sezioneFunzionalita_G_RND.addField(diritti_G_RND);

				DominiFunzionalita_G_RND domini_G_RND = (DominiFunzionalita_G_RND) this.infoCreazioneMap.get(domini_G_RNDId);
				domini_G_RND.init(funzionalita_G_RNDValues, bd,this.getLanguage()); 
				sezioneFunzionalita_G_RND.addField(domini_G_RND); 


				String etichettaFunzionalita_GDE = Utils.getInstance(this.getLanguage()).getMessageFromResourceBundle(this.nomeServizio + ".elementoCorrelato.funzionalita_GDE.titolo");
				Sezione sezioneFunzionalita_GDE = infoModifica.addSezione(etichettaFunzionalita_GDE);
				List<Long> idsAclFunzionalita_GDE = Utils.getIdsFromAcls(entry.getAcls(), Tipo.DOMINIO, Servizio.Giornale_Eventi);
				boolean visualizzaGDE = idsAclFunzionalita_GDE.size() > 0;

				CheckButton funzionalita_GDE = (CheckButton) this.infoCreazioneMap.get(funzionalita_GDEId);
				funzionalita_GDE.setDefaultValue(visualizzaGDE); 
				sezioneFunzionalita_GDE.addField(funzionalita_GDE);

				List<RawParamValue> funzionalita_GDEValues = new ArrayList<RawParamValue>();
				funzionalita_GDEValues.add(new RawParamValue(ruoloId, entry.getId()+""));
				funzionalita_GDEValues.add(new RawParamValue(funzionalita_GDEId, (visualizzaGDE ? "true" : "false")));

				DirittiFunzionalita_GDE diritti_GDE = (DirittiFunzionalita_GDE) this.infoCreazioneMap.get(diritti_GDEId);
				diritti_GDE.init(funzionalita_GDEValues, bd,this.getLanguage()); 
				sezioneFunzionalita_GDE.addField(diritti_GDE);

				DominiFunzionalita_GDE domini_GDE = (DominiFunzionalita_GDE) this.infoCreazioneMap.get(domini_GDEId);
				domini_GDE.init(funzionalita_GDEValues, bd,this.getLanguage()); 
				sezioneFunzionalita_GDE.addField(domini_GDE); 

				String etichettaFunzionalita_MAN = Utils.getInstance(this.getLanguage()).getMessageFromResourceBundle(this.nomeServizio + ".elementoCorrelato.funzionalita_MAN.titolo");
				Sezione sezioneFunzionalita_MAN = infoModifica.addSezione(etichettaFunzionalita_MAN);
				List<Long> idsAclFunzionalita_MAN = Utils.getIdsFromAcls(entry.getAcls(), Tipo.DOMINIO, Servizio.Manutenzione);
				boolean visualizzaMAN = idsAclFunzionalita_MAN.size() > 0;

				CheckButton funzionalita_MAN = (CheckButton) this.infoCreazioneMap.get(funzionalita_MANId);
				funzionalita_MAN.setDefaultValue(visualizzaMAN); 
				sezioneFunzionalita_MAN.addField(funzionalita_MAN);


				List<Long> idsAclFunzionalita_STAT = Utils.getIdsFromAcls(entry.getAcls(), Tipo.DOMINIO, Servizio.Statistiche);
				boolean visualizzaSTAT = idsAclFunzionalita_STAT.size() > 0;
				CheckButton funzionalita_STAT = (CheckButton) this.infoCreazioneMap.get(funzionalita_STATId);
				funzionalita_STAT.setDefaultValue(visualizzaSTAT); 
				funzionalita_STAT.setHidden(!ConsoleProperties.getInstance().isAbilitaFunzionalitaStatistiche()); 

				if(ConsoleProperties.getInstance().isAbilitaFunzionalitaStatistiche()){
					String etichettaFunzionalita_STAT = Utils.getInstance(this.getLanguage()).getMessageFromResourceBundle(this.nomeServizio + ".elementoCorrelato.funzionalita_STAT.titolo");
					Sezione sezioneFunzionalita_STAT = infoModifica.addSezione(etichettaFunzionalita_STAT);
					sezioneFunzionalita_STAT.addField(funzionalita_STAT);
				} else 
					sezioneRoot.addField(funzionalita_STAT);

			}
		} catch (ServiceException e) {
			throw new ConsoleException(e);
		}
		return infoModifica;
	}

	@Override
	public InfoForm getInfoCancellazione(UriInfo uriInfo, BasicBD bd, Map<String, String> parameters) throws ConsoleException { return null;}

	@Override
	public InfoForm getInfoCancellazioneDettaglio(UriInfo uriInfo, BasicBD bd, Ruolo entry) throws ConsoleException {
		return null;
	}

	@Override
	public InfoForm getInfoEsportazione(UriInfo uriInfo, BasicBD bd, Map<String, String> parameters) throws ConsoleException { return null; }

	@Override
	public InfoForm getInfoEsportazioneDettaglio(UriInfo uriInfo, BasicBD bd, Ruolo entry)	throws ConsoleException {	return null;	}

	@Override
	public Object getField(UriInfo uriInfo,List<RawParamValue>values, String fieldId,BasicBD bd) throws WebApplicationException,ConsoleException {
		this.log.debug("Richiesto field ["+fieldId+"]");
		try{
			// Operazione consentita solo ai ruoli con diritto di scrittura
			this.darsService.checkDirittiServizioScrittura(bd, this.funzionalita); 

			if(this.infoCreazioneMap == null){
				this.initInfoCreazione(uriInfo, bd);
			}

			if(this.infoCreazioneMap.containsKey(fieldId)){
				RefreshableParamField<?> paramField = (RefreshableParamField<?>) this.infoCreazioneMap.get(fieldId);

				paramField.aggiornaParametro(values,bd, this.getLanguage());

				return paramField;

			}

			this.log.debug("Field ["+fieldId+"] non presente.");

		}catch(Exception e){
			throw new ConsoleException(e);
		}
		return null;
	}

	@Override
	public Object getSearchField(UriInfo uriInfo, List<RawParamValue> values, String fieldId, BasicBD bd)
			throws WebApplicationException, ConsoleException {
		return null;
	}

	@Override
	public Object getDeleteField(UriInfo uriInfo, List<RawParamValue> values, String fieldId, BasicBD bd) throws WebApplicationException, ConsoleException { return null; }

	@Override
	public Object getExportField(UriInfo uriInfo, List<RawParamValue> values, String fieldId, BasicBD bd) throws WebApplicationException, ConsoleException { return null; }


	@Override
	public Dettaglio getDettaglio(long id, UriInfo uriInfo, BasicBD bd) throws WebApplicationException,ConsoleException {
		String methodName = "dettaglio " + this.titoloServizio + "."+ id;

		try{
			this.log.info("Esecuzione " + methodName + " in corso...");
			// Operazione consentita solo ai ruoli con diritto di lettura
			this.darsService.checkDirittiServizioLettura(bd, this.funzionalita);

			// recupero oggetto
			RuoliBD ruoliBD = new RuoliBD(bd);
			Ruolo ruolo = ruoliBD.getRuolo(id);

			InfoForm infoModifica = this.getInfoModifica(uriInfo, bd,ruolo);
			InfoForm infoCancellazione = this.getInfoCancellazioneDettaglio(uriInfo, bd, ruolo);
			InfoForm infoEsportazione = null;

			Dettaglio dettaglio = new Dettaglio(this.getTitolo(ruolo,bd), infoEsportazione, infoCancellazione, infoModifica);

			it.govpay.web.rs.dars.model.Sezione root = dettaglio.getSezioneRoot(); 

			// dati portale
			if(StringUtils.isNotEmpty(ruolo.getCodRuolo()))
				root.addVoce(Utils.getInstance(this.getLanguage()).getMessageFromResourceBundle(this.nomeServizio + ".codRuolo.label"), ruolo.getCodRuolo());
			if(StringUtils.isNotEmpty(ruolo.getDescrizione()))
				root.addVoce(Utils.getInstance(this.getLanguage()).getMessageFromResourceBundle(this.nomeServizio + ".descrizione.label"), ruolo.getDescrizione());

			List<Acl> acls = ruolo.getAcls();
			String etichettaDomini = Utils.getInstance(this.getLanguage()).getMessageFromResourceBundle(this.nomeServizio + ".elementoCorrelato.domini.titolo");
			String etichettaDiritti = Utils.getInstance(this.getLanguage()).getMessageFromResourceBundle(this.nomeServizio + ".elementoCorrelato.diritti.titolo");
			String etichettaAdmin = Utils.getInstance(this.getLanguage()).getMessageFromResourceBundle(this.nomeServizio + ".elementoCorrelato.admin.titolo");
			String valore = null;
			// Elementi correlati
			String etichettaFunzionalita_A_PPA = Utils.getInstance(this.getLanguage()).getMessageFromResourceBundle(this.nomeServizio + ".elementoCorrelato.funzionalita_A_PPA.titolo");
			it.govpay.web.rs.dars.model.Sezione sezioneFunzionalita_A_PPA = dettaglio.addSezione(etichettaFunzionalita_A_PPA);

			List<Long> idDomini = Utils.getIdsFromAcls(acls, Tipo.DOMINIO, Servizio.Anagrafica_PagoPa);
			StringBuilder listaVociDomini = new StringBuilder();
			valore = null;
			if(!Utils.isEmpty(idDomini)){
				List<Acl> aclsFiltrate = Utils.getAcls(acls, Tipo.DOMINIO, Servizio.Anagrafica_PagoPa);
				int diritti = aclsFiltrate.get(0).getDiritti();
				String dirittiValore = "";
				switch (diritti) {
				case Ruolo.DIRITTI_SCRITTURA:
					dirittiValore = Utils.getInstance(this.getLanguage()).getMessageFromResourceBundle(this.nomeServizio + ".diritto.SCRITTURA.label");
					break;
				case Ruolo.DIRITTI_LETTURA:
					dirittiValore = Utils.getInstance(this.getLanguage()).getMessageFromResourceBundle(this.nomeServizio + ".diritto.LETTURA.label");
					break;
				case Ruolo.NO_DIRITTI:
				default:
					dirittiValore = Utils.getInstance(this.getLanguage()).getMessageFromResourceBundle(this.nomeServizio + ".diritto.NESSUNO.label");
					break;
				}

				sezioneFunzionalita_A_PPA.addVoce(etichettaDiritti, dirittiValore); 

				if(!idDomini.contains(-1L)){
					DominiBD dominiBD = new DominiBD(bd);
					DominioFilter filter = dominiBD.newFilter();
					FilterSortWrapper fsw = new FilterSortWrapper();
					fsw.setField(it.govpay.orm.Dominio.model().COD_DOMINIO);
					fsw.setSortOrder(SortOrder.ASC);
					filter.getFilterSortList().add(fsw);
					filter.setIdDomini(idDomini);
					List<Dominio> findAll =  dominiBD.findAll(filter);

					if(findAll != null && findAll.size() > 0){
						for (Dominio entry : findAll) {
							if(listaVociDomini.length() > 0) {
								listaVociDomini.append(", ");
							}
							listaVociDomini.append(entry.getRagioneSociale());
						}
					}
				}else{
					valore = Utils.getInstance(this.getLanguage()).getMessageFromResourceBundle("commons.label.tutti");
				}
			} else {
				valore = Utils.getInstance(this.getLanguage()).getMessageFromResourceBundle("commons.label.nessuno");
			}

			if(listaVociDomini.length() == 0){
				sezioneFunzionalita_A_PPA.addVoce(etichettaDomini, valore); 
			} else {
				sezioneFunzionalita_A_PPA.addVoce(etichettaDomini, listaVociDomini.toString()); 
			}

			String etichettaFunzionalita_A_CON = Utils.getInstance(this.getLanguage()).getMessageFromResourceBundle(this.nomeServizio + ".elementoCorrelato.funzionalita_A_CON.titolo");
			it.govpay.web.rs.dars.model.Sezione sezioneFunzionalita_A_CON = dettaglio.addSezione(etichettaFunzionalita_A_CON);

			idDomini = Utils.getIdsFromAcls(acls, Tipo.DOMINIO, Servizio.Anagrafica_Contabile);
			listaVociDomini = new StringBuilder();
			valore = null;
			if(!Utils.isEmpty(idDomini)){
				List<Acl> aclsFiltrate = Utils.getAcls(acls, Tipo.DOMINIO, Servizio.Anagrafica_Contabile);
				int diritti = aclsFiltrate.get(0).getDiritti();
				String dirittiValore = "";
				switch (diritti) {
				case Ruolo.DIRITTI_SCRITTURA:
					dirittiValore = Utils.getInstance(this.getLanguage()).getMessageFromResourceBundle(this.nomeServizio + ".diritto.SCRITTURA.label");
					break;
				case Ruolo.DIRITTI_LETTURA:
					dirittiValore = Utils.getInstance(this.getLanguage()).getMessageFromResourceBundle(this.nomeServizio + ".diritto.LETTURA.label");
					break;
				case Ruolo.NO_DIRITTI:
				default:
					dirittiValore = Utils.getInstance(this.getLanguage()).getMessageFromResourceBundle(this.nomeServizio + ".diritto.NESSUNO.label");
					break;
				}

				sezioneFunzionalita_A_CON.addVoce(etichettaDiritti, dirittiValore); 

				if(!idDomini.contains(-1L)){
					DominiBD dominiBD = new DominiBD(bd);
					DominioFilter filter = dominiBD.newFilter();
					FilterSortWrapper fsw = new FilterSortWrapper();
					fsw.setField(it.govpay.orm.Dominio.model().COD_DOMINIO);
					fsw.setSortOrder(SortOrder.ASC);
					filter.getFilterSortList().add(fsw);
					filter.setIdDomini(idDomini);
					List<Dominio> findAll =  dominiBD.findAll(filter);

					if(findAll != null && findAll.size() > 0){
						for (Dominio entry : findAll) {
							if(listaVociDomini.length() > 0) {
								listaVociDomini.append(", ");
							}
							listaVociDomini.append(entry.getRagioneSociale());
						}
					}
				}else{
					valore = Utils.getInstance(this.getLanguage()).getMessageFromResourceBundle("commons.label.tutti");
				}
			} else {
				valore = Utils.getInstance(this.getLanguage()).getMessageFromResourceBundle("commons.label.nessuno");
			}

			if(listaVociDomini.length() == 0){
				sezioneFunzionalita_A_CON.addVoce(etichettaDomini, valore); 
			} else {
				sezioneFunzionalita_A_CON.addVoce(etichettaDomini, listaVociDomini.toString()); 
			}

			String etichettaFunzionalita_A_APP = Utils.getInstance(this.getLanguage()).getMessageFromResourceBundle(this.nomeServizio + ".elementoCorrelato.funzionalita_A_APP.titolo");
			it.govpay.web.rs.dars.model.Sezione sezioneFunzionalita_A_APP = dettaglio.addSezione(etichettaFunzionalita_A_APP);

			idDomini = Utils.getIdsFromAcls(acls, Tipo.DOMINIO, Servizio.Anagrafica_Applicazioni);
			listaVociDomini = new StringBuilder();
			valore = null;
			if(!Utils.isEmpty(idDomini)){
				List<Acl> aclsFiltrate = Utils.getAcls(acls, Tipo.DOMINIO, Servizio.Anagrafica_Applicazioni);
				int diritti = aclsFiltrate.get(0).getDiritti();
				String dirittiValore = "";
				switch (diritti) {
				case Ruolo.DIRITTI_SCRITTURA:
					dirittiValore = Utils.getInstance(this.getLanguage()).getMessageFromResourceBundle(this.nomeServizio + ".diritto.SCRITTURA.label");
					break;
				case Ruolo.DIRITTI_LETTURA:
					dirittiValore = Utils.getInstance(this.getLanguage()).getMessageFromResourceBundle(this.nomeServizio + ".diritto.LETTURA.label");
					break;
				case Ruolo.NO_DIRITTI:
				default:
					dirittiValore = Utils.getInstance(this.getLanguage()).getMessageFromResourceBundle(this.nomeServizio + ".diritto.NESSUNO.label");
					break;
				}

				sezioneFunzionalita_A_APP.addVoce(etichettaDiritti, dirittiValore); 

				if(!idDomini.contains(-1L)){
					DominiBD dominiBD = new DominiBD(bd);
					DominioFilter filter = dominiBD.newFilter();
					FilterSortWrapper fsw = new FilterSortWrapper();
					fsw.setField(it.govpay.orm.Dominio.model().COD_DOMINIO);
					fsw.setSortOrder(SortOrder.ASC);
					filter.getFilterSortList().add(fsw);
					filter.setIdDomini(idDomini);
					List<Dominio> findAll =  dominiBD.findAll(filter);

					if(findAll != null && findAll.size() > 0){
						for (Dominio entry : findAll) {
							if(listaVociDomini.length() > 0) {
								listaVociDomini.append(", ");
							}
							listaVociDomini.append(entry.getRagioneSociale());
						}
					}
				}else{
					valore = Utils.getInstance(this.getLanguage()).getMessageFromResourceBundle("commons.label.tutti");
				}
			} else {
				valore = Utils.getInstance(this.getLanguage()).getMessageFromResourceBundle("commons.label.nessuno");
			}

			if(listaVociDomini.length() == 0){
				sezioneFunzionalita_A_APP.addVoce(etichettaDomini, valore); 
			} else {
				sezioneFunzionalita_A_APP.addVoce(etichettaDomini, listaVociDomini.toString());
			}

			String etichettaFunzionalita_A_USR = Utils.getInstance(this.getLanguage()).getMessageFromResourceBundle(this.nomeServizio + ".elementoCorrelato.funzionalita_A_USR.titolo");
			it.govpay.web.rs.dars.model.Sezione sezioneFunzionalita_A_USR = dettaglio.addSezione(etichettaFunzionalita_A_USR);

			idDomini = Utils.getIdsFromAcls(acls, Tipo.DOMINIO, Servizio.Anagrafica_Utenti);
			listaVociDomini = new StringBuilder();
			valore = null;
			if(!Utils.isEmpty(idDomini)){
				List<Acl> aclsFiltrate = Utils.getAcls(acls, Tipo.DOMINIO, Servizio.Anagrafica_Utenti);
				int diritti = aclsFiltrate.get(0).getDiritti();
				String dirittiValore = "";
				switch (diritti) {
				case Ruolo.DIRITTI_SCRITTURA:
					dirittiValore = Utils.getInstance(this.getLanguage()).getMessageFromResourceBundle(this.nomeServizio + ".diritto.SCRITTURA.label");
					break;
				case Ruolo.DIRITTI_LETTURA:
					dirittiValore = Utils.getInstance(this.getLanguage()).getMessageFromResourceBundle(this.nomeServizio + ".diritto.LETTURA.label");
					break;
				case Ruolo.NO_DIRITTI:
				default:
					dirittiValore = Utils.getInstance(this.getLanguage()).getMessageFromResourceBundle(this.nomeServizio + ".diritto.NESSUNO.label");
					break;
				}

				sezioneFunzionalita_A_USR.addVoce(etichettaDiritti, dirittiValore); 

				if(!idDomini.contains(-1L)){
					DominiBD dominiBD = new DominiBD(bd);
					DominioFilter filter = dominiBD.newFilter();
					FilterSortWrapper fsw = new FilterSortWrapper();
					fsw.setField(it.govpay.orm.Dominio.model().COD_DOMINIO);
					fsw.setSortOrder(SortOrder.ASC);
					filter.getFilterSortList().add(fsw);
					filter.setIdDomini(idDomini);
					List<Dominio> findAll =  dominiBD.findAll(filter);

					if(findAll != null && findAll.size() > 0){
						for (Dominio entry : findAll) {
							if(listaVociDomini.length() > 0) {
								listaVociDomini.append(", ");
							}
							listaVociDomini.append(entry.getRagioneSociale());
						}
					}
				}else{
					valore = Utils.getInstance(this.getLanguage()).getMessageFromResourceBundle("commons.label.tutti");
				}
			} else {
				valore = Utils.getInstance(this.getLanguage()).getMessageFromResourceBundle("commons.label.nessuno");
			}

			if(listaVociDomini.length() == 0){
				sezioneFunzionalita_A_USR.addVoce(etichettaDomini, valore); 
			} else {
				sezioneFunzionalita_A_USR.addVoce(etichettaDomini, listaVociDomini.toString());
			}


			String etichettaFunzionalita_G_PAG = Utils.getInstance(this.getLanguage()).getMessageFromResourceBundle(this.nomeServizio + ".elementoCorrelato.funzionalita_G_PAG.titolo");
			it.govpay.web.rs.dars.model.Sezione sezioneFunzionalita_G_PAG = dettaglio.addSezione(etichettaFunzionalita_G_PAG);

			idDomini = Utils.getIdsFromAcls(acls, Tipo.DOMINIO, Servizio.Gestione_Pagamenti);
			listaVociDomini = new StringBuilder();
			valore = null;
			if(!Utils.isEmpty(idDomini)){
				List<Acl> aclsFiltrate = Utils.getAcls(acls, Tipo.DOMINIO, Servizio.Gestione_Pagamenti);
				int diritti = aclsFiltrate.get(0).getDiritti();
				boolean admin = aclsFiltrate.get(0).isAdmin();
				String dirittiValore = "";
				switch (diritti) {
				case Ruolo.DIRITTI_SCRITTURA:
					dirittiValore = Utils.getInstance(this.getLanguage()).getMessageFromResourceBundle(this.nomeServizio + ".diritto.SCRITTURA.label");
					break;
				case Ruolo.DIRITTI_LETTURA:
					dirittiValore = Utils.getInstance(this.getLanguage()).getMessageFromResourceBundle(this.nomeServizio + ".diritto.LETTURA.label");
					break;
				case Ruolo.NO_DIRITTI:
				default:
					dirittiValore = Utils.getInstance(this.getLanguage()).getMessageFromResourceBundle(this.nomeServizio + ".diritto.NESSUNO.label");
					break;
				}

				sezioneFunzionalita_G_PAG.addVoce(etichettaDiritti, dirittiValore); 
				sezioneFunzionalita_G_PAG.addVoce(etichettaAdmin, Utils.getSiNoAsLabel(admin));  

				if(!idDomini.contains(-1L)){
					DominiBD dominiBD = new DominiBD(bd);
					DominioFilter filter = dominiBD.newFilter();
					FilterSortWrapper fsw = new FilterSortWrapper();
					fsw.setField(it.govpay.orm.Dominio.model().COD_DOMINIO);
					fsw.setSortOrder(SortOrder.ASC);
					filter.getFilterSortList().add(fsw);
					filter.setIdDomini(idDomini);
					List<Dominio> findAll =  dominiBD.findAll(filter);

					if(findAll != null && findAll.size() > 0){
						for (Dominio entry : findAll) {
							if(listaVociDomini.length() > 0) {
								listaVociDomini.append(", ");
							}
							listaVociDomini.append(entry.getRagioneSociale());
						}
					}
				}else{
					valore = Utils.getInstance(this.getLanguage()).getMessageFromResourceBundle("commons.label.tutti");
				}
			} else {
				valore = Utils.getInstance(this.getLanguage()).getMessageFromResourceBundle("commons.label.nessuno");
			}

			if(listaVociDomini.length() == 0){
				sezioneFunzionalita_G_PAG.addVoce(etichettaDomini, valore); 
			} else {
				sezioneFunzionalita_G_PAG.addVoce(etichettaDomini, listaVociDomini.toString());
			}

			String etichettaFunzionalita_G_RND = Utils.getInstance(this.getLanguage()).getMessageFromResourceBundle(this.nomeServizio + ".elementoCorrelato.funzionalita_G_RND.titolo");
			it.govpay.web.rs.dars.model.Sezione sezioneFunzionalita_G_RND = dettaglio.addSezione(etichettaFunzionalita_G_RND);

			idDomini = Utils.getIdsFromAcls(acls, Tipo.DOMINIO, Servizio.Gestione_Rendicontazioni);
			listaVociDomini = new StringBuilder();
			valore = null;
			if(!Utils.isEmpty(idDomini)){
				List<Acl> aclsFiltrate = Utils.getAcls(acls, Tipo.DOMINIO, Servizio.Gestione_Rendicontazioni);
				int diritti = aclsFiltrate.get(0).getDiritti();
				String dirittiValore = "";
				switch (diritti) {
				case Ruolo.DIRITTI_SCRITTURA:
					dirittiValore = Utils.getInstance(this.getLanguage()).getMessageFromResourceBundle(this.nomeServizio + ".diritto.SCRITTURA.label");
					break;
				case Ruolo.DIRITTI_LETTURA:
					dirittiValore = Utils.getInstance(this.getLanguage()).getMessageFromResourceBundle(this.nomeServizio + ".diritto.LETTURA.label");
					break;
				case Ruolo.NO_DIRITTI:
				default:
					dirittiValore = Utils.getInstance(this.getLanguage()).getMessageFromResourceBundle(this.nomeServizio + ".diritto.NESSUNO.label");
					break;
				}

				sezioneFunzionalita_G_RND.addVoce(etichettaDiritti, dirittiValore); 

				if(!idDomini.contains(-1L)){
					DominiBD dominiBD = new DominiBD(bd);
					DominioFilter filter = dominiBD.newFilter();
					FilterSortWrapper fsw = new FilterSortWrapper();
					fsw.setField(it.govpay.orm.Dominio.model().COD_DOMINIO);
					fsw.setSortOrder(SortOrder.ASC);
					filter.getFilterSortList().add(fsw);
					filter.setIdDomini(idDomini);
					List<Dominio> findAll =  dominiBD.findAll(filter);

					if(findAll != null && findAll.size() > 0){
						for (Dominio entry : findAll) {
							if(listaVociDomini.length() > 0) {
								listaVociDomini.append(", ");
							}
							listaVociDomini.append(entry.getRagioneSociale());
						}
					}
				}else{
					valore = Utils.getInstance(this.getLanguage()).getMessageFromResourceBundle("commons.label.tutti");
				}
			} else {
				valore = Utils.getInstance(this.getLanguage()).getMessageFromResourceBundle("commons.label.nessuno");
			}

			if(listaVociDomini.length() == 0){
				sezioneFunzionalita_G_RND.addVoce(etichettaDomini, valore); 
			} else {
				sezioneFunzionalita_G_RND.addVoce(etichettaDomini, listaVociDomini.toString());
			}

			String etichettaFunzionalita_GDE = Utils.getInstance(this.getLanguage()).getMessageFromResourceBundle(this.nomeServizio + ".elementoCorrelato.funzionalita_GDE.titolo");
			it.govpay.web.rs.dars.model.Sezione sezioneFunzionalita_GDE = dettaglio.addSezione(etichettaFunzionalita_GDE);

			idDomini = Utils.getIdsFromAcls(acls, Tipo.DOMINIO, Servizio.Giornale_Eventi);
			listaVociDomini = new StringBuilder();
			valore = null;
			if(!Utils.isEmpty(idDomini)){
				List<Acl> aclsFiltrate = Utils.getAcls(acls, Tipo.DOMINIO, Servizio.Giornale_Eventi);
				int diritti = aclsFiltrate.get(0).getDiritti();
				String dirittiValore = "";
				switch (diritti) {
				case Ruolo.DIRITTI_SCRITTURA:
					dirittiValore = Utils.getInstance(this.getLanguage()).getMessageFromResourceBundle(this.nomeServizio + ".diritto.SCRITTURA.label");
					break;
				case Ruolo.DIRITTI_LETTURA:
					dirittiValore = Utils.getInstance(this.getLanguage()).getMessageFromResourceBundle(this.nomeServizio + ".diritto.LETTURA.label");
					break;
				case Ruolo.NO_DIRITTI:
				default:
					dirittiValore = Utils.getInstance(this.getLanguage()).getMessageFromResourceBundle(this.nomeServizio + ".diritto.NESSUNO.label");
					break;
				}
				sezioneFunzionalita_GDE.addVoce(etichettaDiritti, dirittiValore); 

				if(!idDomini.contains(-1L)){
					DominiBD dominiBD = new DominiBD(bd);
					DominioFilter filter = dominiBD.newFilter();
					FilterSortWrapper fsw = new FilterSortWrapper();
					fsw.setField(it.govpay.orm.Dominio.model().COD_DOMINIO);
					fsw.setSortOrder(SortOrder.ASC);
					filter.getFilterSortList().add(fsw);
					filter.setIdDomini(idDomini);
					List<Dominio> findAll =  dominiBD.findAll(filter);

					if(findAll != null && findAll.size() > 0){
						for (Dominio entry : findAll) {
							if(listaVociDomini.length() > 0) {
								listaVociDomini.append(", ");
							}
							listaVociDomini.append(entry.getRagioneSociale());
						}
					}
				}else{
					valore = Utils.getInstance(this.getLanguage()).getMessageFromResourceBundle("commons.label.tutti");
				}
			} else {
				valore = Utils.getInstance(this.getLanguage()).getMessageFromResourceBundle("commons.label.nessuno");
			}

			if(listaVociDomini.length() == 0){
				sezioneFunzionalita_GDE.addVoce(etichettaDomini, valore); 
			} else {
				sezioneFunzionalita_GDE.addVoce(etichettaDomini, listaVociDomini.toString());
			}

			String etichettaFunzionalita_MAN = Utils.getInstance(this.getLanguage()).getMessageFromResourceBundle(this.nomeServizio + ".elementoCorrelato.funzionalita_MAN.titolo");
			it.govpay.web.rs.dars.model.Sezione sezioneFunzionalita_MAN = dettaglio.addSezione(etichettaFunzionalita_MAN);

			idDomini = Utils.getIdsFromAcls(acls, Tipo.DOMINIO, Servizio.Manutenzione);
			if(idDomini != null && idDomini.size() > 0){
				sezioneFunzionalita_MAN.addVoce(Utils.getInstance(this.getLanguage()).getMessageFromResourceBundle("commons.label.funzionalita"), Utils.getAbilitataAsLabel(true)); 
			}else {
				sezioneFunzionalita_MAN.addVoce(Utils.getInstance(this.getLanguage()).getMessageFromResourceBundle("commons.label.funzionalita"), Utils.getAbilitataAsLabel(false)); 
			}

			if(ConsoleProperties.getInstance().isAbilitaFunzionalitaStatistiche()) {
				String etichettaFunzionalita_STAT = Utils.getInstance(this.getLanguage()).getMessageFromResourceBundle(this.nomeServizio + ".elementoCorrelato.funzionalita_STAT.titolo");
				it.govpay.web.rs.dars.model.Sezione sezioneFunzionalita_STAT = dettaglio.addSezione(etichettaFunzionalita_STAT);

				idDomini = Utils.getIdsFromAcls(acls, Tipo.DOMINIO, Servizio.Statistiche);
				if(idDomini != null && idDomini.size() > 0){
					sezioneFunzionalita_STAT.addVoce(Utils.getInstance(this.getLanguage()).getMessageFromResourceBundle("commons.label.funzionalita"), Utils.getAbilitataAsLabel(true)); 
				}else {
					sezioneFunzionalita_STAT.addVoce(Utils.getInstance(this.getLanguage()).getMessageFromResourceBundle("commons.label.funzionalita"), Utils.getAbilitataAsLabel(false)); 
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
			// Operazione consentita solo ai ruoli con diritto di scrittura
			this.darsService.checkDirittiServizioScrittura(bd, this.funzionalita);

			Ruolo entry = this.creaEntry(is, uriInfo, bd);

			this.checkEntry(entry, null);

			RuoliBD ruoliBD = new RuoliBD(bd);

			try{
				ruoliBD.getRuolo(entry.getCodRuolo());
				String msg = Utils.getInstance(this.getLanguage()).getMessageWithParamsFromResourceBundle(this.nomeServizio + ".oggettoEsistente", entry.getCodRuolo());
				throw new DuplicatedEntryException(msg);
			}catch(NotFoundException e){}

			ruoliBD.insertRuolo(entry); 

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
	public Ruolo creaEntry(InputStream is, UriInfo uriInfo, BasicBD bd)
			throws WebApplicationException, ConsoleException {
		String methodName = "creaEntry " + this.titoloServizio;
		Ruolo entry = null;

		String funzionalita_A_PPAId = Utils.getInstance(this.getLanguage()).getMessageFromResourceBundle(this.nomeServizio + ".funzionalita_A_PPA.id");
		String domini_A_PPAId = Utils.getInstance(this.getLanguage()).getMessageFromResourceBundle(this.nomeServizio + ".domini_A_PPA.id");
		String diritti_A_PPAId = Utils.getInstance(this.getLanguage()).getMessageFromResourceBundle(this.nomeServizio + ".diritti_A_PPA.id");

		String funzionalita_A_CONId = Utils.getInstance(this.getLanguage()).getMessageFromResourceBundle(this.nomeServizio + ".funzionalita_A_CON.id");
		String domini_A_CONId = Utils.getInstance(this.getLanguage()).getMessageFromResourceBundle(this.nomeServizio + ".domini_A_CON.id");
		String diritti_A_CONId = Utils.getInstance(this.getLanguage()).getMessageFromResourceBundle(this.nomeServizio + ".diritti_A_CON.id");

		String funzionalita_A_APPId = Utils.getInstance(this.getLanguage()).getMessageFromResourceBundle(this.nomeServizio + ".funzionalita_A_APP.id");
		String domini_A_APPId = Utils.getInstance(this.getLanguage()).getMessageFromResourceBundle(this.nomeServizio + ".domini_A_APP.id");
		String diritti_A_APPId = Utils.getInstance(this.getLanguage()).getMessageFromResourceBundle(this.nomeServizio + ".diritti_A_APP.id");

		String funzionalita_A_USRId = Utils.getInstance(this.getLanguage()).getMessageFromResourceBundle(this.nomeServizio + ".funzionalita_A_USR.id");
		String domini_A_USRId = Utils.getInstance(this.getLanguage()).getMessageFromResourceBundle(this.nomeServizio + ".domini_A_USR.id");
		String diritti_A_USRId = Utils.getInstance(this.getLanguage()).getMessageFromResourceBundle(this.nomeServizio + ".diritti_A_USR.id");

		String funzionalita_G_PAGId = Utils.getInstance(this.getLanguage()).getMessageFromResourceBundle(this.nomeServizio + ".funzionalita_G_PAG.id");
		String domini_G_PAGId = Utils.getInstance(this.getLanguage()).getMessageFromResourceBundle(this.nomeServizio + ".domini_G_PAG.id");
		String diritti_G_PAGId = Utils.getInstance(this.getLanguage()).getMessageFromResourceBundle(this.nomeServizio + ".diritti_G_PAG.id");
		String admin_G_PAGId = Utils.getInstance(this.getLanguage()).getMessageFromResourceBundle(this.nomeServizio + ".admin_G_PAG.id");

		String funzionalita_G_RNDId = Utils.getInstance(this.getLanguage()).getMessageFromResourceBundle(this.nomeServizio + ".funzionalita_G_RND.id");
		String domini_G_RNDId = Utils.getInstance(this.getLanguage()).getMessageFromResourceBundle(this.nomeServizio + ".domini_G_RND.id");
		String diritti_G_RNDId = Utils.getInstance(this.getLanguage()).getMessageFromResourceBundle(this.nomeServizio + ".diritti_G_RND.id");

		String funzionalita_GDEId = Utils.getInstance(this.getLanguage()).getMessageFromResourceBundle(this.nomeServizio + ".funzionalita_GDE.id");
		String domini_GDEId = Utils.getInstance(this.getLanguage()).getMessageFromResourceBundle(this.nomeServizio + ".domini_GDE.id");
		String diritti_GDEId = Utils.getInstance(this.getLanguage()).getMessageFromResourceBundle(this.nomeServizio + ".diritti_GDE.id");

		String funzionalita_MANId = Utils.getInstance(this.getLanguage()).getMessageFromResourceBundle(this.nomeServizio + ".funzionalita_MAN.id");
		String funzionalita_STATId = Utils.getInstance(this.getLanguage()).getMessageFromResourceBundle(this.nomeServizio + ".funzionalita_STAT.id");

		try{
			this.log.info("Esecuzione " + methodName + " in corso...");
			// Operazione consentita solo ai ruoli con diritto di scrittura
			this.darsService.checkDirittiServizioScrittura(bd, this.funzionalita);

			JsonConfig jsonConfig = new JsonConfig();

			Map<String,Class<?>> classMap = new HashMap<String, Class<?>>();
			classMap.put(domini_A_PPAId, Long.class); 
			classMap.put(diritti_A_PPAId, Long.class); 
			classMap.put(domini_A_CONId, Long.class); 
			classMap.put(diritti_A_CONId, Long.class); 
			classMap.put(domini_A_APPId, Long.class); 
			classMap.put(diritti_A_APPId, Long.class); 
			classMap.put(domini_A_USRId, Long.class); 
			classMap.put(diritti_A_USRId, Long.class); 
			classMap.put(domini_G_PAGId, Long.class); 
			classMap.put(diritti_G_PAGId, Long.class); 
			classMap.put(domini_G_RNDId, Long.class); 
			classMap.put(diritti_G_RNDId, Long.class); 
			classMap.put(domini_GDEId, Long.class); 
			classMap.put(diritti_GDEId, Long.class); 
			jsonConfig.setClassMap(classMap);
			ByteArrayOutputStream baos = new ByteArrayOutputStream();
			Utils.copy(is, baos);

			baos.flush();
			baos.close();

			JSONObject jsonObjectRuolo = JSONObject.fromObject( baos.toString() );

			List<Acl> lstAclDominiFunzionalita_A_PPA = new ArrayList<Acl>();

			if(jsonObjectRuolo.getBoolean(funzionalita_A_PPAId)){
				JSONArray jsonDomini = jsonObjectRuolo.getJSONArray(domini_A_PPAId);
				// diritti
				Long diritti_A_PPA = jsonObjectRuolo.getLong(diritti_A_PPAId);

				for (int i = 0; i < jsonDomini.size(); i++) {
					long idDominio = jsonDomini.getLong(i);

					Acl acl = new Acl();
					acl.setTipo(Tipo.DOMINIO);
					acl.setServizio(Servizio.Anagrafica_PagoPa);
					acl.setDiritti(diritti_A_PPA.intValue()); 
					if(idDominio > 0){
						acl.setIdDominio(idDominio);
						lstAclDominiFunzionalita_A_PPA.add(acl);
					}else {
						lstAclDominiFunzionalita_A_PPA.clear();
						lstAclDominiFunzionalita_A_PPA.add(acl);
						break;
					}
				}
			}
			// rimuovo gli oggetti della parte Funzionalita_A_PPA
			jsonObjectRuolo.remove(funzionalita_A_PPAId);
			jsonObjectRuolo.remove(domini_A_PPAId);
			jsonObjectRuolo.remove(diritti_A_PPAId);

			List<Acl> lstAclDominiFunzionalita_A_CON = new ArrayList<Acl>();

			if(jsonObjectRuolo.getBoolean(funzionalita_A_CONId)){
				JSONArray jsonDomini = jsonObjectRuolo.getJSONArray(domini_A_CONId);
				// diritti
				Long diritti_A_CON = jsonObjectRuolo.getLong(diritti_A_CONId);

				for (int i = 0; i < jsonDomini.size(); i++) {
					long idDominio = jsonDomini.getLong(i);

					Acl acl = new Acl();
					acl.setTipo(Tipo.DOMINIO);
					acl.setServizio(Servizio.Anagrafica_Contabile);
					acl.setDiritti(diritti_A_CON.intValue()); 
					if(idDominio > 0){
						acl.setIdDominio(idDominio);
						lstAclDominiFunzionalita_A_CON.add(acl);
					}else {
						lstAclDominiFunzionalita_A_CON.clear();
						lstAclDominiFunzionalita_A_CON.add(acl);
						break;
					}
				}
			}
			// rimuovo gli oggetti della parte Funzionalita_A_CON
			jsonObjectRuolo.remove(funzionalita_A_CONId);
			jsonObjectRuolo.remove(domini_A_CONId);
			jsonObjectRuolo.remove(diritti_A_CONId);

			List<Acl> lstAclDominiFunzionalita_A_APP = new ArrayList<Acl>();

			if(jsonObjectRuolo.getBoolean(funzionalita_A_APPId)){
				JSONArray jsonDomini = jsonObjectRuolo.getJSONArray(domini_A_APPId);
				// diritti
				Long diritti_A_APP = jsonObjectRuolo.getLong(diritti_A_APPId);

				for (int i = 0; i < jsonDomini.size(); i++) {
					long idDominio = jsonDomini.getLong(i);

					Acl acl = new Acl();
					acl.setTipo(Tipo.DOMINIO);
					acl.setServizio(Servizio.Anagrafica_Applicazioni);
					acl.setDiritti(diritti_A_APP.intValue()); 
					if(idDominio > 0){
						acl.setIdDominio(idDominio);
						lstAclDominiFunzionalita_A_APP.add(acl);
					}else {
						lstAclDominiFunzionalita_A_APP.clear();
						lstAclDominiFunzionalita_A_APP.add(acl);
						break;
					}
				}
			}
			// rimuovo gli oggetti della parte Funzionalita_A_APP
			jsonObjectRuolo.remove(funzionalita_A_APPId);
			jsonObjectRuolo.remove(domini_A_APPId);
			jsonObjectRuolo.remove(diritti_A_APPId);

			List<Acl> lstAclDominiFunzionalita_A_USR = new ArrayList<Acl>();

			if(jsonObjectRuolo.getBoolean(funzionalita_A_USRId)){
				JSONArray jsonDomini = jsonObjectRuolo.getJSONArray(domini_A_USRId);
				// diritti
				Long diritti_A_USR = jsonObjectRuolo.getLong(diritti_A_USRId);

				for (int i = 0; i < jsonDomini.size(); i++) {
					long idDominio = jsonDomini.getLong(i);

					Acl acl = new Acl();
					acl.setTipo(Tipo.DOMINIO);
					acl.setServizio(Servizio.Anagrafica_Utenti);
					acl.setDiritti(diritti_A_USR.intValue()); 
					if(idDominio > 0){
						acl.setIdDominio(idDominio);
						lstAclDominiFunzionalita_A_USR.add(acl);
					}else {
						lstAclDominiFunzionalita_A_USR.clear();
						lstAclDominiFunzionalita_A_USR.add(acl);
						break;
					}
				}
			}
			// rimuovo gli oggetti della parte Funzionalita_A_USR
			jsonObjectRuolo.remove(funzionalita_A_USRId);
			jsonObjectRuolo.remove(domini_A_USRId);
			jsonObjectRuolo.remove(diritti_A_USRId);

			List<Acl> lstAclDominiFunzionalita_G_PAG = new ArrayList<Acl>();

			if(jsonObjectRuolo.getBoolean(funzionalita_G_PAGId)){
				JSONArray jsonDomini = jsonObjectRuolo.getJSONArray(domini_G_PAGId);
				boolean admin = jsonObjectRuolo.getBoolean(admin_G_PAGId);
				// diritti
				Long diritti_G_PAG = jsonObjectRuolo.getLong(diritti_G_PAGId);

				for (int i = 0; i < jsonDomini.size(); i++) {
					long idDominio = jsonDomini.getLong(i);

					Acl acl = new Acl();
					acl.setTipo(Tipo.DOMINIO);
					acl.setServizio(Servizio.Gestione_Pagamenti);
					acl.setDiritti(diritti_G_PAG.intValue());
					acl.setAdmin(admin);
					if(idDominio > 0){
						acl.setIdDominio(idDominio);
						lstAclDominiFunzionalita_G_PAG.add(acl);
					}else {
						lstAclDominiFunzionalita_G_PAG.clear();
						lstAclDominiFunzionalita_G_PAG.add(acl);
						break;
					}
				}
			}
			// rimuovo gli oggetti della parte Funzionalita_G_PAG
			jsonObjectRuolo.remove(funzionalita_G_PAGId);
			jsonObjectRuolo.remove(domini_G_PAGId);
			jsonObjectRuolo.remove(diritti_G_PAGId);
			jsonObjectRuolo.remove(admin_G_PAGId);

			List<Acl> lstAclDominiFunzionalita_G_RND = new ArrayList<Acl>();

			if(jsonObjectRuolo.getBoolean(funzionalita_G_RNDId)){
				JSONArray jsonDomini = jsonObjectRuolo.getJSONArray(domini_G_RNDId);
				// diritti
				Long diritti_G_RND = jsonObjectRuolo.getLong(diritti_G_RNDId);

				for (int i = 0; i < jsonDomini.size(); i++) {
					long idDominio = jsonDomini.getLong(i);

					Acl acl = new Acl();
					acl.setTipo(Tipo.DOMINIO);
					acl.setServizio(Servizio.Gestione_Rendicontazioni);
					acl.setDiritti(diritti_G_RND.intValue()); 
					if(idDominio > 0){
						acl.setIdDominio(idDominio);
						lstAclDominiFunzionalita_G_RND.add(acl);
					}else {
						lstAclDominiFunzionalita_G_RND.clear();
						lstAclDominiFunzionalita_G_RND.add(acl);
						break;
					}
				}
			}
			// rimuovo gli oggetti della parte Funzionalita_G_RND
			jsonObjectRuolo.remove(funzionalita_G_RNDId);
			jsonObjectRuolo.remove(domini_G_RNDId);
			jsonObjectRuolo.remove(diritti_G_RNDId);

			List<Acl> lstAclDominiFunzionalita_GDE = new ArrayList<Acl>();

			if(jsonObjectRuolo.getBoolean(funzionalita_GDEId)){
				JSONArray jsonDomini = jsonObjectRuolo.getJSONArray(domini_GDEId);
				// diritti
				Long diritti_GDE = jsonObjectRuolo.getLong(diritti_GDEId);

				for (int i = 0; i < jsonDomini.size(); i++) {
					long idDominio = jsonDomini.getLong(i);

					Acl acl = new Acl();
					acl.setTipo(Tipo.DOMINIO);
					acl.setServizio(Servizio.Giornale_Eventi);
					acl.setDiritti(diritti_GDE.intValue()); 
					if(idDominio > 0){
						acl.setIdDominio(idDominio);
						lstAclDominiFunzionalita_GDE.add(acl);
					}else {
						lstAclDominiFunzionalita_GDE.clear();
						lstAclDominiFunzionalita_GDE.add(acl);
						break;
					}
				}
			}
			// rimuovo gli oggetti della parte Funzionalita_GDE
			jsonObjectRuolo.remove(funzionalita_GDEId);
			jsonObjectRuolo.remove(domini_GDEId);
			jsonObjectRuolo.remove(diritti_GDEId);


			List<Acl> lstAclDominiFunzionalita_MAN = new ArrayList<Acl>();

			if(jsonObjectRuolo.getBoolean(funzionalita_MANId)){
				int diritti_MAN = Ruolo.DIRITTI_LETTURA;
				Acl acl = new Acl();
				acl.setTipo(Tipo.DOMINIO);
				acl.setServizio(Servizio.Manutenzione);
				acl.setDiritti(diritti_MAN);
				lstAclDominiFunzionalita_MAN.add(acl);
			}
			// rimuovo gli oggetti della parte Funzionalita_MAN
			jsonObjectRuolo.remove(funzionalita_MANId);

			List<Acl> lstAclDominiFunzionalita_STAT = new ArrayList<Acl>();

			if(jsonObjectRuolo.getBoolean(funzionalita_STATId)){
				int diritti_STAT = Ruolo.DIRITTI_LETTURA;
				Acl acl = new Acl();
				acl.setTipo(Tipo.DOMINIO);
				acl.setServizio(Servizio.Statistiche);
				acl.setDiritti(diritti_STAT);
				lstAclDominiFunzionalita_STAT.add(acl);
			}
			// rimuovo gli oggetti della parte Funzionalita_STAT
			jsonObjectRuolo.remove(funzionalita_STATId);

			jsonConfig.setRootClass(Ruolo.class);
			entry = (Ruolo) JSONObject.toBean( jsonObjectRuolo, jsonConfig );

			// colleziono gli acl
			entry.setAcls(lstAclDominiFunzionalita_A_PPA);
			entry.getAcls().addAll(lstAclDominiFunzionalita_A_CON);
			entry.getAcls().addAll(lstAclDominiFunzionalita_A_APP);
			entry.getAcls().addAll(lstAclDominiFunzionalita_A_USR);
			entry.getAcls().addAll(lstAclDominiFunzionalita_G_PAG);
			entry.getAcls().addAll(lstAclDominiFunzionalita_G_RND);
			entry.getAcls().addAll(lstAclDominiFunzionalita_GDE);
			entry.getAcls().addAll(lstAclDominiFunzionalita_MAN);
			entry.getAcls().addAll(lstAclDominiFunzionalita_STAT);

			this.log.info("Esecuzione " + methodName + " completata.");
			return entry;
		}catch(WebApplicationException e){
			throw e;
		}catch(Exception e){
			throw new ConsoleException(e);
		}
	}

	@Override
	public void checkEntry(Ruolo entry, Ruolo oldEntry) throws ValidationException {
		if(entry == null || StringUtils.isEmpty(entry.getCodRuolo())) {
			throw new ValidationException(Utils.getInstance(this.getLanguage()).getMessageFromResourceBundle(this.nomeServizio + ".creazione.erroreCodRuoloObbligatorio"));
		}

		if(entry.getDescrizione() == null || entry.getDescrizione().isEmpty()) {
			throw new ValidationException(Utils.getInstance(this.getLanguage()).getMessageFromResourceBundle(this.nomeServizio + ".creazione.erroreDescrizioneObbligatoria"));
		}

		if(oldEntry != null) { //caso update
			if(!oldEntry.getCodRuolo().equals(entry.getCodRuolo())) {
				throw new ValidationException(Utils.getInstance(this.getLanguage()).getMessageWithParamsFromResourceBundle(this.nomeServizio + ".aggiornamento.erroreCodRuoloNonCoincide",oldEntry.getCodRuolo(),entry.getCodRuolo()));
			}
		}
	}

	@Override
	public Dettaglio update(InputStream is, UriInfo uriInfo, BasicBD bd)
			throws WebApplicationException, ConsoleException, ValidationException {
		String methodName = "Update " + this.titoloServizio;

		try{
			this.log.info("Esecuzione " + methodName + " in corso...");
			// Operazione consentita solo ai ruoli con diritto di scrittura
			this.darsService.checkDirittiServizioScrittura(bd, this.funzionalita);

			Ruolo entry = this.creaEntry(is, uriInfo, bd);

			RuoliBD ruoliBD = new RuoliBD(bd);
			Ruolo oldEntry = ruoliBD.getRuolo(entry.getCodRuolo());

			this.checkEntry(entry, oldEntry);

			ruoliBD.updateRuolo(entry); 

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
	public Elenco delete(List<Long> idsToDelete, List<RawParamValue> rawValues, UriInfo uriInfo, BasicBD bd) throws WebApplicationException, ConsoleException, DeleteException {	return null; 	}

	@Override
	public String getTitolo(Ruolo entry, BasicBD bd) {
		return entry.getDescrizione();
		
//		StringBuilder sb = new StringBuilder();
//		sb.append(Utils.getInstance(this.getLanguage()).getMessageWithParamsFromResourceBundle(this.nomeServizio + ".titolo.label",entry.getDescrizione(),entry.getCodRuolo()));
//		return sb.toString();
	}

	@Override
	public String getSottotitolo(Ruolo entry, BasicBD bd) {
		return entry.getCodRuolo();
//		StringBuilder sb = new StringBuilder();
//
//		List<Acl> acls = entry.getAcls();
//		List<Long> idDomini = Utils.getIdsFromAcls(acls, Tipo.DOMINIO, Servizio.Anagrafica_PagoPa);
//		if(idDomini != null && idDomini.size() > 0){
//			sb.append(Utils.getInstance(this.getLanguage()).getMessageFromResourceBundle("funzionalita."+ Servizio.Anagrafica_PagoPa.getCodifica()));
//		}
//
//		idDomini = Utils.getIdsFromAcls(acls, Tipo.DOMINIO, Servizio.Anagrafica_Contabile);
//		if(idDomini != null && idDomini.size() > 0){
//			if(sb.length() > 0) sb.append(", ");
//
//			sb.append(Utils.getInstance(this.getLanguage()).getMessageFromResourceBundle("funzionalita."+ Servizio.Anagrafica_Contabile.getCodifica()));
//		}
//
//		idDomini = Utils.getIdsFromAcls(acls, Tipo.DOMINIO, Servizio.Anagrafica_Applicazioni);
//		if(idDomini != null && idDomini.size() > 0){
//			if(sb.length() > 0) sb.append(", ");
//
//			sb.append(Utils.getInstance(this.getLanguage()).getMessageFromResourceBundle("funzionalita."+ Servizio.Anagrafica_Applicazioni.getCodifica()));
//		}
//
//		idDomini = Utils.getIdsFromAcls(acls, Tipo.DOMINIO, Servizio.Anagrafica_Utenti);
//		if(idDomini != null && idDomini.size() > 0){
//			if(sb.length() > 0) sb.append(", ");
//
//			sb.append(Utils.getInstance(this.getLanguage()).getMessageFromResourceBundle("funzionalita."+ Servizio.Anagrafica_Utenti.getCodifica()));
//		}
//
//		idDomini = Utils.getIdsFromAcls(acls, Tipo.DOMINIO, Servizio.Gestione_Pagamenti);
//		if(idDomini != null && idDomini.size() > 0){
//			if(sb.length() > 0) sb.append(", ");
//
//			sb.append(Utils.getInstance(this.getLanguage()).getMessageFromResourceBundle("funzionalita."+ Servizio.Gestione_Pagamenti.getCodifica()));
//		}
//
//		idDomini = Utils.getIdsFromAcls(acls, Tipo.DOMINIO, Servizio.Gestione_Rendicontazioni);
//		if(idDomini != null && idDomini.size() > 0){
//			if(sb.length() > 0) sb.append(", ");
//
//			sb.append(Utils.getInstance(this.getLanguage()).getMessageFromResourceBundle("funzionalita."+ Servizio.Gestione_Rendicontazioni.getCodifica()));
//		}
//
//		idDomini = Utils.getIdsFromAcls(acls, Tipo.DOMINIO, Servizio.Giornale_Eventi);
//		if(idDomini != null && idDomini.size() > 0){
//			if(sb.length() > 0) sb.append(", ");
//
//			sb.append(Utils.getInstance(this.getLanguage()).getMessageFromResourceBundle("funzionalita."+ Servizio.Giornale_Eventi.getCodifica()));
//		}
//
//		idDomini = Utils.getIdsFromAcls(acls, Tipo.DOMINIO, Servizio.Manutenzione);
//		if(idDomini != null && idDomini.size() > 0){
//			if(sb.length() > 0) sb.append(", ");
//
//			sb.append(Utils.getInstance(this.getLanguage()).getMessageFromResourceBundle("funzionalita."+ Servizio.Manutenzione.getCodifica()));
//		}
//
//		if(ConsoleProperties.getInstance().isAbilitaFunzionalitaStatistiche()){
//			idDomini = Utils.getIdsFromAcls(acls, Tipo.DOMINIO, Servizio.Statistiche);
//			if(idDomini != null && idDomini.size() > 0){
//				if(sb.length() > 0) sb.append(", ");
//
//				sb.append(Utils.getInstance(this.getLanguage()).getMessageFromResourceBundle("funzionalita."+ Servizio.Statistiche.getCodifica()));
//			}
//		}
//
//		return Utils.getInstance(this.getLanguage()).getMessageWithParamsFromResourceBundle(this.nomeServizio + ".sottotitolo.label",sb.toString());
	}

	@Override
	public Map<String, Voce<String>> getVoci(Ruolo entry, BasicBD bd) throws ConsoleException { return null; }

	@Override
	public String esporta(List<Long> idsToExport, List<RawParamValue> rawValues, UriInfo uriInfo, BasicBD bd, ZipOutputStream zout)
			throws WebApplicationException, ConsoleException,ExportException {
		return null;
	}

	@Override
	public String esporta(Long idToExport, List<RawParamValue> rawValues, UriInfo uriInfo, BasicBD bd, ZipOutputStream zout)	throws WebApplicationException, ConsoleException,ExportException {
		return null;
	}

	@Override
	public Object uplaod(MultipartFormDataInput input, UriInfo uriInfo, BasicBD bd)	throws WebApplicationException, ConsoleException, ValidationException { return null;}
}
