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
package it.govpay.web.rs.dars.anagrafica.domini;

import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

import javax.imageio.ImageIO;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.UriInfo;

import org.apache.commons.codec.binary.Base64;
import org.apache.commons.lang.StringUtils;
import org.apache.logging.log4j.Logger;
import org.apache.tika.Tika;
import org.jboss.resteasy.plugins.providers.multipart.MultipartFormDataInput;
import org.openspcoop2.generic_project.exception.NotFoundException;
import org.openspcoop2.generic_project.exception.ServiceException;
import org.openspcoop2.generic_project.expression.SortOrder;

import it.govpay.bd.BasicBD;
import it.govpay.bd.FilterSortWrapper;
import it.govpay.bd.anagrafica.ApplicazioniBD;
import it.govpay.bd.anagrafica.DominiBD;
import it.govpay.bd.anagrafica.IbanAccreditoBD;
import it.govpay.bd.anagrafica.StazioniBD;
import it.govpay.bd.anagrafica.TipiTributoBD;
import it.govpay.bd.anagrafica.TributiBD;
import it.govpay.bd.anagrafica.UnitaOperativeBD;
import it.govpay.bd.anagrafica.filters.ApplicazioneFilter;
import it.govpay.bd.anagrafica.filters.DominioFilter;
import it.govpay.bd.anagrafica.filters.IbanAccreditoFilter;
import it.govpay.bd.anagrafica.filters.StazioneFilter;
import it.govpay.bd.model.Dominio;
import it.govpay.bd.model.Stazione;
import it.govpay.bd.model.Tributo;
import it.govpay.bd.model.UnitaOperativa;
import it.govpay.core.utils.DominioUtils;
import it.govpay.model.Anagrafica;
import it.govpay.model.Applicazione;
import it.govpay.model.IbanAccredito;
import it.govpay.model.Intermediario;
import it.govpay.model.TipoTributo;
import it.govpay.web.rs.dars.anagrafica.anagrafica.AnagraficaHandler;
import it.govpay.web.rs.dars.anagrafica.domini.input.Logo;
import it.govpay.web.rs.dars.anagrafica.domini.input.ModalitaIntermediazione;
import it.govpay.web.rs.dars.anagrafica.iban.Iban;
import it.govpay.web.rs.dars.anagrafica.tributi.Tributi;
import it.govpay.web.rs.dars.anagrafica.uo.UnitaOperative;
import it.govpay.web.rs.dars.base.DarsHandler;
import it.govpay.web.rs.dars.base.DarsService;
import it.govpay.web.rs.dars.exception.ConsoleException;
import it.govpay.web.rs.dars.exception.DeleteException;
import it.govpay.web.rs.dars.exception.DuplicatedEntryException;
import it.govpay.web.rs.dars.exception.ExportException;
import it.govpay.web.rs.dars.exception.ValidationException;
import it.govpay.web.rs.dars.handler.IDarsHandler;
import it.govpay.web.rs.dars.model.DarsResponse.EsitoOperazione;
import it.govpay.web.rs.dars.model.Dettaglio;
import it.govpay.web.rs.dars.model.Elemento;
import it.govpay.web.rs.dars.model.Elenco;
import it.govpay.web.rs.dars.model.InfoForm;
import it.govpay.web.rs.dars.model.InfoForm.Sezione;
import it.govpay.web.rs.dars.model.RawParamValue;
import it.govpay.web.rs.dars.model.Voce;
import it.govpay.web.rs.dars.model.VoceImage;
import it.govpay.web.rs.dars.model.input.ParamField;
import it.govpay.web.rs.dars.model.input.RefreshableParamField;
import it.govpay.web.rs.dars.model.input.base.CheckButton;
import it.govpay.web.rs.dars.model.input.base.InputFile;
import it.govpay.web.rs.dars.model.input.base.InputNumber;
import it.govpay.web.rs.dars.model.input.base.InputText;
import it.govpay.web.rs.dars.model.input.base.SelectList;
import it.govpay.web.utils.Utils;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import net.sf.json.JsonConfig;

public class DominiHandler extends DarsHandler<Dominio> implements IDarsHandler<Dominio>{

	public static final String ANAGRAFICA_DOMINI = "anagrafica";

	public DominiHandler(Logger log, DarsService darsService) {
		super(log,darsService);
	}

	@Override
	public Elenco getElenco(UriInfo uriInfo,BasicBD bd) throws WebApplicationException,ConsoleException {
		String methodName = "getElenco " + this.titoloServizio;
		try{
			this.log.info("Esecuzione " + methodName + " in corso...");
			// Operazione consentita solo agli utenti che hanno almeno un ruolo consentito per la funzionalita'
			this.darsService.checkDirittiServizio(bd, this.funzionalita);

			Integer offset = this.getOffset(uriInfo);
			Integer limit = this.getLimit(uriInfo);

			boolean simpleSearch = this.containsParameter(uriInfo, DarsService.SIMPLE_SEARCH_PARAMETER_ID);

			DominiBD dominiBD = new DominiBD(bd);
			DominioFilter filter = dominiBD.newFilter(simpleSearch);
			filter.setOffset(offset);
			filter.setLimit(limit);
			FilterSortWrapper fsw = new FilterSortWrapper();
			fsw.setField(it.govpay.orm.Dominio.model().RAGIONE_SOCIALE);
			fsw.setSortOrder(SortOrder.ASC);
			filter.getFilterSortList().add(fsw);

			String codDominioId = Utils.getInstance(this.getLanguage()).getMessageFromResourceBundle(this.nomeServizio + ".codDominio.id");
			String codDominio = this.getParameter(uriInfo, codDominioId, String.class);
			String idStazioneId = Utils.getInstance(this.getLanguage()).getMessageFromResourceBundle(this.nomeServizio + ".idStazione.id");
			String idStazione = this.getParameter(uriInfo, idStazioneId, String.class);

			String ragioneSocialeId = Utils.getInstance(this.getLanguage()).getMessageFromResourceBundle(this.nomeServizio + ".ragioneSociale.id");
			String ragioneSociale = this.getParameter(uriInfo, ragioneSocialeId, String.class);
			
			String abilitatoId = Utils.getInstance(this.getLanguage()).getMessageFromResourceBundle(this.nomeServizio + ".abilitato.id");
			String abilitato = this.getParameter(uriInfo, abilitatoId, String.class);
			
			filter.setSearchAbilitato(this.getMostraDisabilitato(abilitato)); 

			if(simpleSearch){
				// simplesearch
				String simpleSearchString = this.getParameter(uriInfo, DarsService.SIMPLE_SEARCH_PARAMETER_ID, String.class);
				if(StringUtils.isNotEmpty(simpleSearchString)) {
					filter.setSimpleSearchString(simpleSearchString);
				}
			}else{
				if(StringUtils.isNotEmpty(codDominio)){
					filter.setCodDominio(codDominio);
				}

				if(StringUtils.isNotEmpty(ragioneSociale)){
					filter.setRagioneSociale(ragioneSociale);
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
			}

			long count = dominiBD.count(filter);

			// visualizza la ricerca solo se i risultati sono > del limit
			boolean visualizzaRicerca = this.visualizzaRicerca(count, limit);

			InfoForm infoRicerca = this.getInfoRicerca(uriInfo, bd, visualizzaRicerca);

			// Indico la visualizzazione custom
			String formatter = Utils.getInstance(this.getLanguage()).getMessageFromResourceBundle(this.nomeServizio+".elenco.formatter");
			String simpleSearchPlaceholder = Utils.getInstance(this.getLanguage()).getMessageFromResourceBundle(this.nomeServizio+".simpleSearch.placeholder");
			Elenco elenco = new Elenco(this.titoloServizio, infoRicerca,
					this.getInfoCreazione(uriInfo, bd),
					count, this.getInfoEsportazione(uriInfo, bd), this.getInfoCancellazione(uriInfo, bd),simpleSearchPlaceholder); 

			List<Dominio> findAll = dominiBD.findAll(filter);

			if(findAll != null && findAll.size() > 0){
				for (Dominio entry : findAll) {
					Elemento elemento = this.getElemento(entry, entry.getId(), this.pathServizio,bd);
					elemento.setFormatter(formatter);
					elenco.getElenco().add(elemento);
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

		if(visualizzaRicerca) {
			String codDominioId = Utils.getInstance(this.getLanguage()).getMessageFromResourceBundle(this.nomeServizio + ".codDominio.id");
			String idStazioneId = Utils.getInstance(this.getLanguage()).getMessageFromResourceBundle(this.nomeServizio + ".idStazione.id");
			String ragioneSocialeId = Utils.getInstance(this.getLanguage()).getMessageFromResourceBundle(this.nomeServizio + ".ragioneSociale.id");
			String abilitatoId = Utils.getInstance(this.getLanguage()).getMessageFromResourceBundle(this.nomeServizio + ".abilitato.id");
			
			if(this.infoRicercaMap == null){
				this.initInfoRicerca(uriInfo, bd);

			}


			Sezione sezioneRoot = infoRicerca.getSezioneRoot();

			InputText codDominio = (InputText) this.infoRicercaMap.get(codDominioId);
			codDominio.setDefaultValue(null);
			codDominio.setEditable(true); 
			sezioneRoot.addField(codDominio);

			InputText ragioneSociale = (InputText) this.infoRicercaMap.get(ragioneSocialeId);
			ragioneSociale.setDefaultValue(null);
			ragioneSociale.setEditable(true); 
			sezioneRoot.addField(ragioneSociale);

			List<Voce<Long>> stazioni = new ArrayList<Voce<Long>>();

			try{
				StazioniBD stazioniBD = new StazioniBD(bd);
				StazioneFilter filter = stazioniBD.newFilter();
				FilterSortWrapper fsw = new FilterSortWrapper();
				fsw.setField(it.govpay.orm.Stazione.model().COD_STAZIONE);
				fsw.setSortOrder(SortOrder.ASC);
				filter.getFilterSortList().add(fsw);

				List<Stazione> findAll = stazioniBD.findAll(filter);

				stazioni.add(new Voce<Long>(Utils.getInstance(this.getLanguage()).getMessageFromResourceBundle("commons.label.qualsiasi"), -1L));
				if(findAll != null && findAll.size() > 0){
					for (Stazione entry : findAll) {
						stazioni.add(new Voce<Long>(entry.getCodStazione(), entry.getId()));
					}
				}
			}catch(Exception e){
				throw new ConsoleException(e);
			}

			SelectList<Long> stazione = (SelectList<Long>) this.infoRicercaMap.get(idStazioneId);
			stazione.setDefaultValue(-1L);
			stazione.setValues(stazioni);
			sezioneRoot.addField(stazione); 
			
			CheckButton abilitato = (CheckButton) this.infoRicercaMap.get(abilitatoId);
			abilitato.setDefaultValue(false);
			sezioneRoot.addField(abilitato);

		}
		return infoRicerca;
	}

	private void initInfoRicerca(UriInfo uriInfo, BasicBD bd) throws ConsoleException{
		if(this.infoRicercaMap == null){
			this.infoRicercaMap = new HashMap<String, ParamField<?>>();

			String codDominioId = Utils.getInstance(this.getLanguage()).getMessageFromResourceBundle(this.nomeServizio + ".codDominio.id");
			String idStazioneId = Utils.getInstance(this.getLanguage()).getMessageFromResourceBundle(this.nomeServizio + ".idStazione.id");
			String ragioneSocialeId = Utils.getInstance(this.getLanguage()).getMessageFromResourceBundle(this.nomeServizio + ".ragioneSociale.id");
			
			// codDominio
			String codDominioLabel = Utils.getInstance(this.getLanguage()).getMessageFromResourceBundle(this.nomeServizio + ".codDominio.label");
			InputText codDominio = new InputText(codDominioId, codDominioLabel, null, false, false, true, 1, 11);
			this.infoRicercaMap.put(codDominioId, codDominio);

			// nome
			String ragioneSocialeLabel = Utils.getInstance(this.getLanguage()).getMessageFromResourceBundle(this.nomeServizio + ".ragioneSociale.label");
			InputText ragioneSociale = new InputText(ragioneSocialeId, ragioneSocialeLabel, null, false, false, true, 1, 255);
			this.infoRicercaMap.put(ragioneSocialeId, ragioneSociale);

			// idstazione
			String idStazionelabel =Utils.getInstance(this.getLanguage()).getMessageFromResourceBundle(this.nomeServizio + ".idStazione.label");
			List<Voce<Long>> stazioni = new ArrayList<Voce<Long>>();
			SelectList<Long> idStazione = new SelectList<Long>(idStazioneId, idStazionelabel, null, false, false, true, stazioni );
			this.infoRicercaMap.put(idStazioneId, idStazione);
			
			String abilitatoId = Utils.getInstance(this.getLanguage()).getMessageFromResourceBundle(this.nomeServizio + ".abilitato.id");
			CheckButton abilitato = this.creaCheckButtonSearchMostraDisabilitato(abilitatoId);
			this.infoRicercaMap.put(abilitatoId, abilitato);

		}
	}

	@SuppressWarnings("unchecked")
	@Override
	public InfoForm getInfoCreazione(UriInfo uriInfo, BasicBD bd) throws ConsoleException {
		InfoForm infoCreazione =  null;
		try {
			if(this.darsService.isServizioAbilitatoScrittura(bd, this.funzionalita)){
				URI creazione = this.getUriCreazione(uriInfo, bd);
				infoCreazione = new InfoForm(creazione,Utils.getInstance(this.getLanguage()).getMessageFromResourceBundle(this.nomeServizio + ".creazione.titolo"));

				String codDominioId = Utils.getInstance(this.getLanguage()).getMessageFromResourceBundle(this.nomeServizio + ".codDominio.id");
				String ragioneSocialeId = Utils.getInstance(this.getLanguage()).getMessageFromResourceBundle(this.nomeServizio + ".ragioneSociale.id");
				String abilitatoId = Utils.getInstance(this.getLanguage()).getMessageFromResourceBundle(this.nomeServizio + ".abilitato.id");
				String dominioId = Utils.getInstance(this.getLanguage()).getMessageFromResourceBundle(this.nomeServizio + ".id.id");
				String idStazioneId = Utils.getInstance(this.getLanguage()).getMessageFromResourceBundle(this.nomeServizio + ".idStazione.id");
				String glnId = Utils.getInstance(this.getLanguage()).getMessageFromResourceBundle(this.nomeServizio + ".gln.id");
				String uoIdId = Utils.getInstance(this.getLanguage()).getMessageFromResourceBundle(this.nomeServizio + ".uoId.id");
				String idApplicazioneDefaultId = Utils.getInstance(this.getLanguage()).getMessageFromResourceBundle(this.nomeServizio + ".idApplicazioneDefault.id");
				String riusoIuvId = Utils.getInstance(this.getLanguage()).getMessageFromResourceBundle(this.nomeServizio + ".riusoIuv.id");
				String customIuvId = Utils.getInstance(this.getLanguage()).getMessageFromResourceBundle(this.nomeServizio + ".customIuv.id");
				String modalitaIntermediazioneId = Utils.getInstance(this.getLanguage()).getMessageFromResourceBundle(this.nomeServizio + ".modalitaIntermediazione.id");
				String prefissoIuvId = Utils.getInstance(this.getLanguage()).getMessageFromResourceBundle(this.nomeServizio + ".prefissoIuv.id");
				String prefissoIuvRigorosoId = Utils.getInstance(this.getLanguage()).getMessageFromResourceBundle(this.nomeServizio + ".prefissoIuvRigoroso.id");
				String segregationCodeId = Utils.getInstance(this.getLanguage()).getMessageFromResourceBundle(this.nomeServizio + ".segregationCode.id");
				String logoId = Utils.getInstance(this.getLanguage()).getMessageFromResourceBundle(this.nomeServizio + ".logo.id");
				String abilitaModificaLogoId = Utils.getInstance(this.getLanguage()).getMessageFromResourceBundle(this.nomeServizio + ".abilitaModificaLogo.id");
				String cbillId = Utils.getInstance(this.getLanguage()).getMessageFromResourceBundle(this.nomeServizio + ".cbill.id");

				AnagraficaHandler anagraficaHandler = new AnagraficaHandler(ANAGRAFICA_DOMINI,this.nomeServizio,this.pathServizio,this.getLanguage());
				List<ParamField<?>> infoCreazioneAnagrafica = anagraficaHandler.getInfoCreazioneAnagraficaDominio(uriInfo, bd);

				if(this.infoCreazioneMap == null){
					this.initInfoCreazione(uriInfo, bd);
				}

				Sezione sezioneRoot = infoCreazione.getSezioneRoot();
				InputNumber idInterm = (InputNumber) this.infoCreazioneMap.get(dominioId);
				idInterm.setDefaultValue(null);
				sezioneRoot.addField(idInterm);
				InputText codDominio = (InputText) this.infoCreazioneMap.get(codDominioId);
				codDominio.setDefaultValue(null);
				codDominio.setEditable(true); 
				sezioneRoot.addField(codDominio);

				InputNumber uoId = (InputNumber) this.infoCreazioneMap.get(uoIdId);
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

				SelectList<Long> stazione = (SelectList<Long>) this.infoCreazioneMap.get(idStazioneId);
				stazione.setDefaultValue(null);
				stazione.setValues(stazioni);
				sezioneRoot.addField(stazione); 

				InputText ragioneSociale = (InputText) this.infoCreazioneMap.get(ragioneSocialeId);
				ragioneSociale.setDefaultValue(null);
				//sezioneRoot.addField(ragioneSociale);

				InputText gln = (InputText) this.infoCreazioneMap.get(glnId);
				gln.setDefaultValue(null);
				sezioneRoot.addField(gln);
				
				InputText cbill = (InputText) this.infoCreazioneMap.get(cbillId);
				cbill.setDefaultValue(null);
				sezioneRoot.addField(cbill);

				List<Voce<Long>> applicazioni = new ArrayList<Voce<Long>>();

				applicazioni.add(new Voce<Long>(Utils.getInstance(this.getLanguage()).getMessageFromResourceBundle("commons.label.nessuna"), -1L));
				try{
					ApplicazioniBD applicazioniBD = new ApplicazioniBD(bd);
					ApplicazioneFilter filter = applicazioniBD.newFilter();
					FilterSortWrapper fsw = new FilterSortWrapper();
					fsw.setField(it.govpay.orm.Applicazione.model().COD_APPLICAZIONE);
					fsw.setSortOrder(SortOrder.ASC);
					filter.getFilterSortList().add(fsw);

					List<Applicazione> findAll = applicazioniBD.findAll(filter);


					if(findAll != null && findAll.size() > 0){
						for (Applicazione entry : findAll) {
							applicazioni.add(new Voce<Long>(entry.getCodApplicazione(), entry.getId()));
						}
					}
				}catch(Exception e){
					throw new ConsoleException(e);
				}

				SelectList<Long> idApplicazioneDefault = (SelectList<Long>) this.infoCreazioneMap.get(idApplicazioneDefaultId);
				idApplicazioneDefault.setDefaultValue(-1L);
				idApplicazioneDefault.setValues(applicazioni);
				sezioneRoot.addField(idApplicazioneDefault); 

				//				Sezione sezioneLogo = infoCreazione.addSezione(Utils.getInstance(this.getLanguage()).getMessageFromResourceBundle(this.nomeServizio + ".sezioneLogo"));

				CheckButton abilitaModificaLogo  = (CheckButton) this.infoCreazioneMap.get(abilitaModificaLogoId);
				abilitaModificaLogo.setDefaultValue(true);
				abilitaModificaLogo.setHidden(true);
				abilitaModificaLogo.setEditable(false);
				sezioneRoot.addField(abilitaModificaLogo); 

				List<RawParamValue> abilitaModificaLogoValues = new ArrayList<RawParamValue>();
				abilitaModificaLogoValues.add(new RawParamValue(dominioId, null)); 
				abilitaModificaLogoValues.add(new RawParamValue(abilitaModificaLogoId, "true"));

				Logo logo = (Logo) this.infoCreazioneMap.get(logoId);
				logo.init(abilitaModificaLogoValues, bd,this.getLanguage());  
				sezioneRoot.addField(logo);

				CheckButton abilitato = (CheckButton) this.infoCreazioneMap.get(abilitatoId);
				abilitato.setDefaultValue(true); 
				sezioneRoot.addField(abilitato);

				// sezione Gestione IUV

				Sezione sezioneGestioneIuv = infoCreazione.addSezione(Utils.getInstance(this.getLanguage()).getMessageFromResourceBundle(this.nomeServizio + ".sezioneIuv"));

				CheckButton riusoIuv = (CheckButton) this.infoCreazioneMap.get(riusoIuvId);
				riusoIuv.setDefaultValue(true); 
				sezioneGestioneIuv.addField(riusoIuv);

				CheckButton customIuv = (CheckButton) this.infoCreazioneMap.get(customIuvId);
				customIuv.setDefaultValue(false);
				customIuv.setEditable(true);
				sezioneGestioneIuv.addField(customIuv);

				List<RawParamValue> modalitaIntermediazioneValues = new ArrayList<RawParamValue>();
				modalitaIntermediazioneValues.add(new RawParamValue(dominioId, null));
				modalitaIntermediazioneValues.add(new RawParamValue(idStazioneId, null));

				ModalitaIntermediazione modalitaIntermediazione =  (ModalitaIntermediazione) this.infoCreazioneMap.get(modalitaIntermediazioneId);
				modalitaIntermediazione.init(modalitaIntermediazioneValues, bd,this.getLanguage()); 
				sezioneGestioneIuv.addField(modalitaIntermediazione); 

				// prefissoIuv
				InputText prefissoIuv = (InputText) this.infoCreazioneMap.get(prefissoIuvId);
				prefissoIuv.setDefaultValue(null);
				sezioneGestioneIuv.addField(prefissoIuv);

				// prefissoIuvRigoroso
				CheckButton prefissoIuvRigoroso = (CheckButton) this.infoCreazioneMap.get(prefissoIuvRigorosoId);
				prefissoIuvRigoroso.setDefaultValue(false);
				sezioneGestioneIuv.addField(prefissoIuvRigoroso);

				// segregationCode
				InputText segregationCode = (InputText) this.infoCreazioneMap.get(segregationCodeId);
				segregationCode.setDefaultValue(null); 
				sezioneGestioneIuv.addField(segregationCode);

				// sezione anagrafica

				Sezione sezioneAnagrafica = infoCreazione.addSezione(Utils.getInstance(this.getLanguage()).getMessageFromResourceBundle(this.nomeServizio + "." + ANAGRAFICA_DOMINI + ".titolo"));

				for (ParamField<?> par : infoCreazioneAnagrafica) { 
					sezioneAnagrafica.addField(par); 	
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

			// id 
			String dominioId = Utils.getInstance(this.getLanguage()).getMessageFromResourceBundle(this.nomeServizio + ".id.id");
			InputNumber id = new InputNumber(dominioId, null, null, false, true, false, 1, 20);
			this.infoCreazioneMap.put(dominioId, id);

			// uoid 
			String uoIdId = Utils.getInstance(this.getLanguage()).getMessageFromResourceBundle(this.nomeServizio + ".uoId.id");
			InputNumber uoId = new InputNumber(uoIdId, null, null, false, true, false, 1, 20);
			this.infoCreazioneMap.put(uoIdId, uoId);

			String codDominioId = Utils.getInstance(this.getLanguage()).getMessageFromResourceBundle(this.nomeServizio + ".codDominio.id");
			String ragioneSocialeId = Utils.getInstance(this.getLanguage()).getMessageFromResourceBundle(this.nomeServizio + ".ragioneSociale.id");
			String abilitatoId = Utils.getInstance(this.getLanguage()).getMessageFromResourceBundle(this.nomeServizio + ".abilitato.id");
			String idStazioneId = Utils.getInstance(this.getLanguage()).getMessageFromResourceBundle(this.nomeServizio + ".idStazione.id");
			String glnId = Utils.getInstance(this.getLanguage()).getMessageFromResourceBundle(this.nomeServizio + ".gln.id");
			String idApplicazioneDefaultId = Utils.getInstance(this.getLanguage()).getMessageFromResourceBundle(this.nomeServizio + ".idApplicazioneDefault.id");
			String riusoIuvId = Utils.getInstance(this.getLanguage()).getMessageFromResourceBundle(this.nomeServizio + ".riusoIuv.id");
			String customIuvId = Utils.getInstance(this.getLanguage()).getMessageFromResourceBundle(this.nomeServizio + ".customIuv.id");
			String modalitaIntermediazioneId = Utils.getInstance(this.getLanguage()).getMessageFromResourceBundle(this.nomeServizio + ".modalitaIntermediazione.id");
			String prefissoIuvId = Utils.getInstance(this.getLanguage()).getMessageFromResourceBundle(this.nomeServizio + ".prefissoIuv.id");
			String prefissoIuvRigorosoId = Utils.getInstance(this.getLanguage()).getMessageFromResourceBundle(this.nomeServizio + ".prefissoIuvRigoroso.id");
			String segregationCodeId = Utils.getInstance(this.getLanguage()).getMessageFromResourceBundle(this.nomeServizio + ".segregationCode.id");
			String logoId = Utils.getInstance(this.getLanguage()).getMessageFromResourceBundle(this.nomeServizio + ".logo.id");
			String abilitaModificaLogoId = Utils.getInstance(this.getLanguage()).getMessageFromResourceBundle(this.nomeServizio + ".abilitaModificaLogo.id");
			String cbillId = Utils.getInstance(this.getLanguage()).getMessageFromResourceBundle(this.nomeServizio + ".cbill.id");

			// codDominio
			String codDominioLabel = Utils.getInstance(this.getLanguage()).getMessageFromResourceBundle(this.nomeServizio + ".codDominio.label");
			InputText codDominio = new InputText(codDominioId, codDominioLabel, null, true, false, true, 11, 11);
			codDominio.setSuggestion(Utils.getInstance(this.getLanguage()).getMessageFromResourceBundle(this.nomeServizio + ".codDominio.suggestion"));
			codDominio.setValidation("[0-9]{11}", Utils.getInstance(this.getLanguage()).getMessageFromResourceBundle(this.nomeServizio + ".codDominio.errorMessage"));
			this.infoCreazioneMap.put(codDominioId, codDominio);

			// ragioneSociale
			String ragioneSocialeLabel = Utils.getInstance(this.getLanguage()).getMessageFromResourceBundle(this.nomeServizio + ".ragioneSociale.label");
			InputText ragioneSociale = new InputText(ragioneSocialeId, ragioneSocialeLabel, null, false, true, false, 1, 70);
			ragioneSociale.setValidation(null, Utils.getInstance(this.getLanguage()).getMessageFromResourceBundle(this.nomeServizio + ".ragioneSociale.errorMessage"));
			this.infoCreazioneMap.put(ragioneSocialeId, ragioneSociale);

			// gln
			String glnLabel = Utils.getInstance(this.getLanguage()).getMessageFromResourceBundle(this.nomeServizio + ".gln.label");
			InputText gln = new InputText(glnId, glnLabel, null, true, false, true, 13, 13);
			gln.setValidation("[0-9]{13}", Utils.getInstance(this.getLanguage()).getMessageFromResourceBundle(this.nomeServizio + ".gln.errorMessage"));
			this.infoCreazioneMap.put(glnId, gln);
			
			// cbill
			String cbillLabel = Utils.getInstance(this.getLanguage()).getMessageFromResourceBundle(this.nomeServizio + ".cbill.label");
			InputText cbill = new InputText(cbillId, cbillLabel, null, false, false, true, 1, 255);
			this.infoCreazioneMap.put(cbillId, cbill);

			// idstazione
			String idStazionelabel =Utils.getInstance(this.getLanguage()).getMessageFromResourceBundle(this.nomeServizio + ".idStazione.label");
			List<Voce<Long>> stazioni = new ArrayList<Voce<Long>>();
			SelectList<Long> idStazione = new SelectList<Long>(idStazioneId, idStazionelabel, null, true, false, true, stazioni );
			this.infoCreazioneMap.put(idStazioneId, idStazione);

			// abilitato
			String abilitatoLabel = Utils.getInstance(this.getLanguage()).getMessageFromResourceBundle(this.nomeServizio + ".abilitato.label");
			CheckButton abiliato = new CheckButton(abilitatoId, abilitatoLabel, true, false, false, true);
			this.infoCreazioneMap.put(abilitatoId, abiliato);

			// idApplicazioneDefault
			String idApplicazioneDefaultlabel =Utils.getInstance(this.getLanguage()).getMessageFromResourceBundle(this.nomeServizio + ".idApplicazioneDefault.label");
			List<Voce<Long>> applicazioni = new ArrayList<Voce<Long>>();
			SelectList<Long> idApplicazioneDefault = new SelectList<Long>(idApplicazioneDefaultId, idApplicazioneDefaultlabel, -1L, false, false, true, applicazioni );
			idApplicazioneDefault.setAvanzata(true); 
			this.infoCreazioneMap.put(idApplicazioneDefaultId, idApplicazioneDefault);


			// riusoIuv
			String riusoIuvLabel = Utils.getInstance(this.getLanguage()).getMessageFromResourceBundle(this.nomeServizio + ".riusoIuv.label");
			CheckButton riusoIuv = new CheckButton(riusoIuvId, riusoIuvLabel, true, false, false, true);
			riusoIuv.setAvanzata(true); 
			this.infoCreazioneMap.put(riusoIuvId, riusoIuv);

			// customIuv
			String customIuvLabel = Utils.getInstance(this.getLanguage()).getMessageFromResourceBundle(this.nomeServizio + ".customIuv.label");
			CheckButton customIuv = new CheckButton(customIuvId, customIuvLabel, true, false, false, true);
			customIuv.setAvanzata(true); 
			this.infoCreazioneMap.put(customIuvId,customIuv);

			// modalitaIntermediazione
			String modalitaIntermediazionelabel =Utils.getInstance(this.getLanguage()).getMessageFromResourceBundle(this.nomeServizio + ".modalitaIntermediazione.label");
			List<RawParamValue> modalitaIntermediazioneValues = new ArrayList<RawParamValue>();
			modalitaIntermediazioneValues.add(new RawParamValue(dominioId, null));
			modalitaIntermediazioneValues.add(new RawParamValue(idStazioneId, null));
			URI modalitaIntermediazioneRefreshUri = this.getUriField(uriInfo, bd, modalitaIntermediazioneId); 
			ModalitaIntermediazione modalitaIntermediazione = new ModalitaIntermediazione(this.nomeServizio, modalitaIntermediazioneId, modalitaIntermediazionelabel, modalitaIntermediazioneRefreshUri, modalitaIntermediazioneValues, bd,this.getLanguage());
			modalitaIntermediazione.setAvanzata(true); 
			modalitaIntermediazione.addDependencyField(idStazione);
			modalitaIntermediazione.init(modalitaIntermediazioneValues, bd,this.getLanguage()); 
			this.infoCreazioneMap.put(modalitaIntermediazioneId, modalitaIntermediazione);

			// prefissoIuv
			String prefissoIuvLabel = Utils.getInstance(this.getLanguage()).getMessageFromResourceBundle(this.nomeServizio + ".prefissoIuv.label");
			InputText prefissoIuv = new InputText(prefissoIuvId, prefissoIuvLabel, null, false, false, true, 1, 255);
			prefissoIuv.setAvanzata(true); 
			this.infoCreazioneMap.put(prefissoIuvId, prefissoIuv);

			// prefissoIuvRigoroso
			String prefissoIuvRigorosoLabel = Utils.getInstance(this.getLanguage()).getMessageFromResourceBundle(this.nomeServizio + ".prefissoIuvRigoroso.label");
			CheckButton prefissoIuvRigoroso = new CheckButton(prefissoIuvRigorosoId, prefissoIuvRigorosoLabel, false, false, false, true);
			prefissoIuvRigoroso.setAvanzata(true); 
			this.infoCreazioneMap.put(prefissoIuvRigorosoId,prefissoIuvRigoroso);

			// segregationCode
			String segregationCodeLabel = Utils.getInstance(this.getLanguage()).getMessageFromResourceBundle(this.nomeServizio + ".segregationCode.label");
			InputText segregationCode = new InputText(segregationCodeId, segregationCodeLabel, null, false, false, true, 2, 2);
			//segregationCode.setSuggestion(Utils.getInstance(this.getLanguage()).getMessageFromResourceBundle(this.nomeServizio + ".segregationCode.suggestion"));
			segregationCode.setValidation("[0-9]{2}", Utils.getInstance(this.getLanguage()).getMessageFromResourceBundle(this.nomeServizio + ".segregationCode.errorMessage"));
			segregationCode.setAvanzata(true); 
			this.infoCreazioneMap.put(segregationCodeId, segregationCode);


			// abilita modifica logo
			String abilitaModificaLogoLabel = Utils.getInstance(this.getLanguage()).getMessageFromResourceBundle(this.nomeServizio + ".abilitaModificaLogo.label");
			CheckButton abilitaModificaLogo = new CheckButton(abilitaModificaLogoId, abilitaModificaLogoLabel, true, false, false, true);
			this.infoCreazioneMap.put(abilitaModificaLogoId, abilitaModificaLogo);

			List<RawParamValue> abilitaModificaLogoValues = new ArrayList<RawParamValue>();
			abilitaModificaLogoValues.add(new RawParamValue(dominioId, null));
			abilitaModificaLogoValues.add(new RawParamValue(abilitaModificaLogoId, "true"));

			// logo
			URI logoRefreshUri = this.getUriField(uriInfo, bd, logoId); 
			String logoLabel = Utils.getInstance(this.getLanguage()).getMessageFromResourceBundle(this.nomeServizio + ".logo.label");
			String maxWidthS = Utils.getInstance(this.getLanguage()).getMessageFromResourceBundle(this.nomeServizio + ".logo.maxWidth");
			String maxHeightS = Utils.getInstance(this.getLanguage()).getMessageFromResourceBundle(this.nomeServizio + ".logo.maxHeight");
			String logoAccTypes = Utils.getInstance(this.getLanguage()).getMessageFromResourceBundle(this.nomeServizio + ".logo.acceptedMimeTypes");
			List<String> acceptedMimeTypes =new ArrayList<String>();
			acceptedMimeTypes.addAll(Arrays.asList(logoAccTypes.split(",")));  
			String maxByteSizeS = Utils.getInstance(this.getLanguage()).getMessageFromResourceBundle(this.nomeServizio + ".logo.maxByteSize");
			long maxByteSize = Long.parseLong(maxByteSizeS);
			maxByteSizeS = Utils.fileSizeConverter(maxByteSize);
			String logoNote = Utils.getInstance(this.getLanguage()).getMessageWithParamsFromResourceBundle(this.nomeServizio + ".logo.note", (maxWidthS+"x" + maxHeightS),maxByteSizeS);
			Logo logo = new Logo(this.nomeServizio,	logoId, logoLabel, acceptedMimeTypes , maxByteSize , 1, logoRefreshUri,abilitaModificaLogoValues,bd,this.getLanguage());
			logo.setNote(logoNote);
			logo.setErrorMessageFileSize(Utils.getInstance(this.getLanguage()).getMessageFromResourceBundle(this.nomeServizio + ".logo.errorMessageFileSize"));
			logo.setErrorMessageFileType(Utils.getInstance(this.getLanguage()).getMessageFromResourceBundle(this.nomeServizio + ".logo.errorMessageFileType"));
			logo.addDependencyField(abilitaModificaLogo);
			logo.init(abilitaModificaLogoValues, bd,this.getLanguage()); 

			this.infoCreazioneMap.put(logoId, logo);

		}
	}

	@SuppressWarnings("unchecked")
	@Override
	public InfoForm getInfoModifica(UriInfo uriInfo, BasicBD bd, Dominio entry) throws ConsoleException {
		InfoForm infoModifica = null;
		try {
			if(this.darsService.isServizioAbilitatoScrittura(bd, this.funzionalita)){
				URI modifica = this.getUriModifica(uriInfo, bd);
				infoModifica = new InfoForm(modifica,Utils.getInstance(this.getLanguage()).getMessageFromResourceBundle(this.nomeServizio + ".modifica.titolo"));

				String codDominioId = Utils.getInstance(this.getLanguage()).getMessageFromResourceBundle(this.nomeServizio + ".codDominio.id");
				String ragioneSocialeId = Utils.getInstance(this.getLanguage()).getMessageFromResourceBundle(this.nomeServizio + ".ragioneSociale.id");
				String abilitatoId = Utils.getInstance(this.getLanguage()).getMessageFromResourceBundle(this.nomeServizio + ".abilitato.id");
				String dominioId = Utils.getInstance(this.getLanguage()).getMessageFromResourceBundle(this.nomeServizio + ".id.id");
				String idStazioneId = Utils.getInstance(this.getLanguage()).getMessageFromResourceBundle(this.nomeServizio + ".idStazione.id");
				String glnId = Utils.getInstance(this.getLanguage()).getMessageFromResourceBundle(this.nomeServizio + ".gln.id");
				String uoIdId = Utils.getInstance(this.getLanguage()).getMessageFromResourceBundle(this.nomeServizio + ".uoId.id");
				String idApplicazioneDefaultId = Utils.getInstance(this.getLanguage()).getMessageFromResourceBundle(this.nomeServizio + ".idApplicazioneDefault.id");
				String riusoIuvId = Utils.getInstance(this.getLanguage()).getMessageFromResourceBundle(this.nomeServizio + ".riusoIuv.id");
				String customIuvId = Utils.getInstance(this.getLanguage()).getMessageFromResourceBundle(this.nomeServizio + ".customIuv.id");
				String modalitaIntermediazioneId = Utils.getInstance(this.getLanguage()).getMessageFromResourceBundle(this.nomeServizio + ".modalitaIntermediazione.id");
				String prefissoIuvId = Utils.getInstance(this.getLanguage()).getMessageFromResourceBundle(this.nomeServizio + ".prefissoIuv.id");
				String prefissoIuvRigorosoId = Utils.getInstance(this.getLanguage()).getMessageFromResourceBundle(this.nomeServizio + ".prefissoIuvRigoroso.id");
				String segregationCodeId = Utils.getInstance(this.getLanguage()).getMessageFromResourceBundle(this.nomeServizio + ".segregationCode.id");
				String logoId = Utils.getInstance(this.getLanguage()).getMessageFromResourceBundle(this.nomeServizio + ".logo.id");
				String abilitaModificaLogoId = Utils.getInstance(this.getLanguage()).getMessageFromResourceBundle(this.nomeServizio + ".abilitaModificaLogo.id");
				String cbillId = Utils.getInstance(this.getLanguage()).getMessageFromResourceBundle(this.nomeServizio + ".cbill.id");

				UnitaOperativeBD uoBD = new UnitaOperativeBD(bd);
				UnitaOperativa unitaOperativa = null;
				try {
					unitaOperativa = uoBD.getUnitaOperativa(entry.getId(), it.govpay.model.Dominio.EC);
				} catch (Exception e) {
					//throw new ConsoleException(e);
					return null;
				}

				AnagraficaHandler anagraficaHandler = new AnagraficaHandler(ANAGRAFICA_DOMINI,this.nomeServizio,this.pathServizio,this.getLanguage());
				Anagrafica anagrafica = unitaOperativa != null ? unitaOperativa.getAnagrafica() : null;
				List<ParamField<?>> infoCreazioneAnagrafica = anagraficaHandler.getInfoModificaAnagraficaDominio(uriInfo, bd,anagrafica,entry.getRagioneSociale());

				if(this.infoCreazioneMap == null){
					this.initInfoCreazione(uriInfo, bd);
				}

				Sezione sezioneRoot = infoModifica.getSezioneRoot();
				InputNumber idInterm = (InputNumber) this.infoCreazioneMap.get(dominioId);
				idInterm.setDefaultValue(entry.getId());
				sezioneRoot.addField(idInterm);
				InputText codDominio = (InputText) this.infoCreazioneMap.get(codDominioId);
				codDominio.setDefaultValue(entry.getCodDominio());
				codDominio.setEditable(false); 
				sezioneRoot.addField(codDominio);

				if(unitaOperativa != null){
					InputNumber uoId = (InputNumber) this.infoCreazioneMap.get(uoIdId);
					uoId.setDefaultValue(unitaOperativa.getId());
					sezioneRoot.addField(uoId);
				}

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

				SelectList<Long> stazione = (SelectList<Long>) this.infoCreazioneMap.get(idStazioneId);
				stazione.setDefaultValue(entry.getIdStazione());
				stazione.setValues(stazioni); 
				sezioneRoot.addField(stazione); 

				InputText ragioneSociale = (InputText) this.infoCreazioneMap.get(ragioneSocialeId);
				ragioneSociale.setDefaultValue(entry.getRagioneSociale());
				//sezioneRoot.addField(ragioneSociale);

				InputText gln = (InputText) this.infoCreazioneMap.get(glnId);
				gln.setDefaultValue(entry.getGln());
				sezioneRoot.addField(gln);
				
				InputText cbill = (InputText) this.infoCreazioneMap.get(cbillId);
				cbill.setDefaultValue(entry.getCbill());
				sezioneRoot.addField(cbill);

				List<Voce<Long>> applicazioni = new ArrayList<Voce<Long>>();
				applicazioni.add(new Voce<Long>(Utils.getInstance(this.getLanguage()).getMessageFromResourceBundle("commons.label.nessuna"), -1L));
				try{
					ApplicazioniBD applicazioniBD = new ApplicazioniBD(bd);
					ApplicazioneFilter filter = applicazioniBD.newFilter();
					FilterSortWrapper fsw = new FilterSortWrapper();
					fsw.setField(it.govpay.orm.Applicazione.model().COD_APPLICAZIONE);
					fsw.setSortOrder(SortOrder.ASC);
					filter.getFilterSortList().add(fsw);

					List<Applicazione> findAll = applicazioniBD.findAll(filter);


					if(findAll != null && findAll.size() > 0){
						for (Applicazione applicazione : findAll) {
							applicazioni.add(new Voce<Long>(applicazione.getCodApplicazione(), applicazione.getId()));
						}
					}
				}catch(Exception e){
					throw new ConsoleException(e);
				}

				SelectList<Long> idApplicazioneDefault = (SelectList<Long>) this.infoCreazioneMap.get(idApplicazioneDefaultId);
				idApplicazioneDefault.setDefaultValue(entry.getIdApplicazioneDefault() != null ? entry.getIdApplicazioneDefault() : -1L);
				idApplicazioneDefault.setValues(applicazioni);
				sezioneRoot.addField(idApplicazioneDefault); 

				CheckButton abilitato = (CheckButton) this.infoCreazioneMap.get(abilitatoId);
				abilitato.setDefaultValue(entry.isAbilitato()); 
				sezioneRoot.addField(abilitato);

				// sezione logo
				Sezione sezioneLogo = infoModifica.addSezione(Utils.getInstance(this.getLanguage()).getMessageFromResourceBundle(this.nomeServizio + ".sezioneLogo"));

				CheckButton abilitaModificaLogo  = (CheckButton) this.infoCreazioneMap.get(abilitaModificaLogoId);
				abilitaModificaLogo.setDefaultValue(false);
				abilitaModificaLogo.setHidden(false);
				abilitaModificaLogo.setEditable(true);
				sezioneLogo.addField(abilitaModificaLogo); 

				List<RawParamValue> abilitaModificaLogoValues = new ArrayList<RawParamValue>();
				abilitaModificaLogoValues.add(new RawParamValue(dominioId, entry.getId() + "")); 
				abilitaModificaLogoValues.add(new RawParamValue(abilitaModificaLogoId, "false"));

				Logo logo = (Logo) this.infoCreazioneMap.get(logoId);
				logo.init(abilitaModificaLogoValues, bd,this.getLanguage());  
				sezioneLogo.addField(logo);

				// sezione Gestione IUV

				Sezione sezioneGestioneIuv = infoModifica.addSezione(Utils.getInstance(this.getLanguage()).getMessageFromResourceBundle(this.nomeServizio + ".sezioneIuv"));

				CheckButton riusoIuv = (CheckButton) this.infoCreazioneMap.get(riusoIuvId);
				riusoIuv.setDefaultValue(entry.isRiusoIuv()); 
				sezioneGestioneIuv.addField(riusoIuv);

				CheckButton customIuv = (CheckButton) this.infoCreazioneMap.get(customIuvId);
				customIuv.setDefaultValue(entry.isCustomIuv());
				// Se in modifica e' settato il customIuv allora non si puo' modificare
				if(entry.isCustomIuv()) {
					customIuv.setEditable(false);
				}
				sezioneGestioneIuv.addField(customIuv);

				List<RawParamValue> modalitaIntermediazioneValues = new ArrayList<RawParamValue>();
				modalitaIntermediazioneValues.add(new RawParamValue(dominioId, ""+entry.getId()));
				modalitaIntermediazioneValues.add(new RawParamValue(idStazioneId, ""+entry.getIdStazione()));

				ModalitaIntermediazione modalitaIntermediazione =  (ModalitaIntermediazione) this.infoCreazioneMap.get(modalitaIntermediazioneId);
				modalitaIntermediazione.init(modalitaIntermediazioneValues, bd,this.getLanguage()); 
				sezioneGestioneIuv.addField(modalitaIntermediazione); 

				// prefissoIuv
				InputText prefissoIuv = (InputText) this.infoCreazioneMap.get(prefissoIuvId);
				prefissoIuv.setDefaultValue(entry.getIuvPrefix());
				sezioneGestioneIuv.addField(prefissoIuv);

				// prefissoIuvRigoroso
				CheckButton prefissoIuvRigoroso = (CheckButton) this.infoCreazioneMap.get(prefissoIuvRigorosoId);
				prefissoIuvRigoroso.setDefaultValue(entry.isIuvPrefixStrict());
				sezioneGestioneIuv.addField(prefissoIuvRigoroso);

				// segregationCode
				InputText segregationCode = (InputText) this.infoCreazioneMap.get(segregationCodeId);
				String segCode = entry.getSegregationCode() != null ? (entry.getSegregationCode() < 10 ? "0"+entry.getSegregationCode() : entry.getSegregationCode()+"" ) : null;
				segregationCode.setDefaultValue(segCode); 
				sezioneGestioneIuv.addField(segregationCode);

				Sezione sezioneAnagrafica = infoModifica.addSezione(Utils.getInstance(this.getLanguage()).getMessageFromResourceBundle(this.nomeServizio + "." + ANAGRAFICA_DOMINI + ".titolo"));

				for (ParamField<?> par : infoCreazioneAnagrafica) { 
					sezioneAnagrafica.addField(par); 	
				}
			}
		} catch (ServiceException e) {
			throw new ConsoleException(e);
		}
		return infoModifica;
	}

	@Override
	public InfoForm getInfoCancellazione(UriInfo uriInfo, BasicBD bd, Map<String, String> parameters) throws ConsoleException { return null;}

	@Override
	public InfoForm getInfoCancellazioneDettaglio(UriInfo uriInfo, BasicBD bd, Dominio entry) throws ConsoleException {
		return null;
	}

	@Override
	public InfoForm getInfoEsportazione(UriInfo uriInfo, BasicBD bd, Map<String, String> parameters) throws ConsoleException { 
		InfoForm infoEsportazione = null;
		try{
			if(this.darsService.isServizioAbilitatoLettura(bd, this.funzionalita)){
				URI esportazione = this.getUriEsportazione(uriInfo, bd);
				infoEsportazione = new InfoForm(esportazione);
			}
		}catch(ServiceException e){
			throw new ConsoleException(e);
		}
		return infoEsportazione;
	}

	@Override
	public InfoForm getInfoEsportazioneDettaglio(UriInfo uriInfo, BasicBD bd, Dominio entry)	throws ConsoleException {
		InfoForm infoEsportazione = null;
		try{
			if(this.darsService.isServizioAbilitatoLettura(bd, this.funzionalita)){
				URI esportazione = this.getUriEsportazioneDettaglio(uriInfo, bd, entry.getId());
				infoEsportazione = new InfoForm(esportazione);
			}
		}catch(ServiceException e){
			throw new ConsoleException(e);
		}
		return infoEsportazione;
	}

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

				paramField.aggiornaParametro(values,bd,this.getLanguage());

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
		String methodName = "dettaglio " + this.titoloServizio + ".Id "+ id;

		try{
			this.log.info("Esecuzione " + methodName + " in corso...");
			// Operazione consentita solo ai ruoli con diritto di lettura
			this.darsService.checkDirittiServizioLettura(bd, this.funzionalita);

			boolean mostraAnomalia = false;
			// recupero oggetto
			DominiBD dominiBD = new DominiBD(bd);
			Dominio dominio = dominiBD.getDominio(id);

			InfoForm infoModifica = this.getInfoModifica(uriInfo, bd,dominio);
			InfoForm infoCancellazione = this.getInfoCancellazioneDettaglio(uriInfo, bd, dominio);
			InfoForm infoEsportazione = this.getInfoEsportazioneDettaglio(uriInfo, bd,dominio);

			Dettaglio dettaglio = new Dettaglio(this.getTitolo(dominio,bd), infoEsportazione, infoCancellazione, infoModifica);

			it.govpay.web.rs.dars.model.Sezione root = dettaglio.getSezioneRoot(); 

			// dati dell'dominio
			root.addVoce(Utils.getInstance(this.getLanguage()).getMessageFromResourceBundle(this.nomeServizio + ".codDominio.label"), dominio.getCodDominio());
			root.addVoce(Utils.getInstance(this.getLanguage()).getMessageFromResourceBundle(this.nomeServizio + ".idStazione.label"), dominio.getStazione(bd).getCodStazione());
			//root.addVoce(Utils.getInstance(this.getLanguage()).getMessageFromResourceBundle(this.nomeServizio + ".ragioneSociale.label"), dominio.getRagioneSociale());
			root.addVoce(Utils.getInstance(this.getLanguage()).getMessageFromResourceBundle(this.nomeServizio + ".gln.label"), dominio.getGln());
			if(dominio.getIdApplicazioneDefault() != null){
				Applicazione applicazione = dominio.getApplicazioneDefault(bd);
				it.govpay.web.rs.dars.anagrafica.applicazioni.Applicazioni applicazioniDars = new it.govpay.web.rs.dars.anagrafica.applicazioni.Applicazioni();
				URI applicazioneURI = Utils.creaUriConPath(applicazioniDars.getPathServizio(), applicazione.getId()+"");
				root.addVoce(Utils.getInstance(this.getLanguage()).getMessageFromResourceBundle(this.nomeServizio + ".idApplicazioneDefault.label"), applicazione.getCodApplicazione(),applicazioneURI,true); 
			} else {
				root.addVoce(Utils.getInstance(this.getLanguage()).getMessageFromResourceBundle(this.nomeServizio + ".idApplicazioneDefault.label"), Utils.getInstance(this.getLanguage()).getMessageFromResourceBundle("commons.label.nessuna"),true);
			}

			String statoValue = null;
			if(!dominio.isAbilitato()){
				statoValue = Utils.getInstance(this.getLanguage()).getMessageFromResourceBundle(this.nomeServizio + ".stato.disabilitato.label");
			} else {
				if(dominio.getNdpStato() == null){
					statoValue = Utils.getInstance(this.getLanguage()).getMessageFromResourceBundle(this.nomeServizio + ".stato.nonVerificato.label");
				} else {
					if(dominio.getNdpStato().intValue() == 0){
						statoValue = Utils.getInstance(this.getLanguage()).getMessageFromResourceBundle(this.nomeServizio + ".stato.ok.label");
					} else {
						statoValue = Utils.getInstance(this.getLanguage()).getMessageFromResourceBundle(this.nomeServizio + ".stato.errore.label");
						mostraAnomalia = true;
					}
				}
			}

			root.addVoce(Utils.getInstance(this.getLanguage()).getMessageFromResourceBundle(this.nomeServizio + ".stato.label"), statoValue);
			if(StringUtils.isNotEmpty(dominio.getNdpOperazione()))
				root.addVoce(Utils.getInstance(this.getLanguage()).getMessageFromResourceBundle(this.nomeServizio + ".ndpOperazione.label"), dominio.getNdpOperazione());
			root.addVoce(Utils.getInstance(this.getLanguage()).getMessageFromResourceBundle(this.nomeServizio + ".abilitato.label"), Utils.getSiNoAsLabel(dominio.isAbilitato()));

			// Sezione iuv
			it.govpay.web.rs.dars.model.Sezione sezioneIuv = dettaglio.addSezione(Utils.getInstance(this.getLanguage()).getMessageFromResourceBundle(this.nomeServizio + ".sezioneIuv"));
			sezioneIuv.addVoce(Utils.getInstance(this.getLanguage()).getMessageFromResourceBundle(this.nomeServizio + ".riusoIuv.label"), Utils.getSiNoAsLabel(dominio.isRiusoIuv()),true);
			sezioneIuv.addVoce(Utils.getInstance(this.getLanguage()).getMessageFromResourceBundle(this.nomeServizio + ".customIuv.label"), Utils.getSiNoAsLabel(dominio.isCustomIuv()),true);
			int auxDigit = dominio.getAuxDigit();
			String auxDigitS= null;
			switch (auxDigit) {
			case 3:
				auxDigitS = Utils.getInstance(this.getLanguage()).getMessageFromResourceBundle(this.nomeServizio + ".modalitaIntermediazione.3");
				break;
			case 0:
			default:
				auxDigitS = Utils.getInstance(this.getLanguage()).getMessageFromResourceBundle(this.nomeServizio + ".modalitaIntermediazione.0");
				break;
			}
			sezioneIuv.addVoce(Utils.getInstance(this.getLanguage()).getMessageFromResourceBundle(this.nomeServizio + ".modalitaIntermediazione.label"), auxDigitS,true);
			if(StringUtils.isNotEmpty(dominio.getIuvPrefix())) {
				sezioneIuv.addVoce(Utils.getInstance(this.getLanguage()).getMessageFromResourceBundle(this.nomeServizio + ".prefissoIuv.label"), dominio.getIuvPrefix(),true);
			}
			sezioneIuv.addVoce(Utils.getInstance(this.getLanguage()).getMessageFromResourceBundle(this.nomeServizio + ".prefissoIuvRigoroso.label"), Utils.getSiNoAsLabel(dominio.isIuvPrefixStrict()),true);

			if(dominio.getSegregationCode() != null){
				String segCode = dominio.getSegregationCode() < 10 ? "0"+dominio.getSegregationCode() : dominio.getSegregationCode()+"";
				sezioneIuv.addVoce(Utils.getInstance(this.getLanguage()).getMessageFromResourceBundle(this.nomeServizio + ".segregationCode.label"), segCode,true);
			}

			// Sezione Anagrafica

			UnitaOperativeBD uoBD = new UnitaOperativeBD(bd);
			UnitaOperativa unitaOperativa = null;
			try {
				unitaOperativa = uoBD.getUnitaOperativa(dominio.getId(), it.govpay.model.Dominio.EC);
			} catch (Exception e) {
				unitaOperativa = null;
				//				throw new ConsoleException(e);
			}

			Anagrafica anagrafica =  unitaOperativa != null ? unitaOperativa.getAnagrafica() : null; 
			it.govpay.web.rs.dars.model.Sezione sezioneAnagrafica = dettaglio.addSezione(Utils.getInstance(this.getLanguage()).getMessageFromResourceBundle(this.nomeServizio + "." + ANAGRAFICA_DOMINI + ".titolo"));
			AnagraficaHandler anagraficaHandler = new AnagraficaHandler(ANAGRAFICA_DOMINI,this.nomeServizio,this.pathServizio,this.getLanguage());
			anagraficaHandler.fillSezioneAnagraficaDominio(sezioneAnagrafica, anagrafica,dominio.getRagioneSociale()); 

			//			// ContiAccredito 
			//			it.govpay.web.rs.dars.model.Sezione sezioneContiAccredito = dettaglio.addSezione(Utils.getInstance(this.getLanguage()).getMessageFromResourceBundle(this.nomeServizio + "contiAccredito.titolo"));
			//			UriBuilder uriContoAccreditoBuilder = BaseRsService.checkDarsURI(uriInfo).path(this.pathServizio).path("{id}").path("contiAccredito"); 
			//			sezioneContiAccredito.addVoce("Conto Accredito", "scarica", uriContoAccreditoBuilder.build(dominio.getId()));  
			//
			//			// Tabella controparti
			//			it.govpay.web.rs.dars.model.Sezione sezioneTabellaControparti = dettaglio.addSezione(Utils.getInstance(this.getLanguage()).getMessageFromResourceBundle(this.nomeServizio + "tabellaControparti.titolo"));
			//			UriBuilder uriTabellaContropartiBuilder = BaseRsService.checkDarsURI(uriInfo).path(this.pathServizio).path("{id}").path("informativa");
			//			sezioneTabellaControparti.addVoce("Tabella Controparti", "scarica", uriTabellaContropartiBuilder.build(dominio.getId()));


			// sezione anomalie
			if(mostraAnomalia){
				// Sezione iuv
				it.govpay.web.rs.dars.model.Sezione sezioneAnomalia = dettaglio.addSezione(Utils.getInstance(this.getLanguage()).getMessageFromResourceBundle(this.nomeServizio + ".sezioneAnomalia"));
				sezioneAnomalia.addVoce(dominio.getNdpDescrizione() , "");
			}

			if(dominio.getLogo() != null && dominio.getLogo().length > 0){
				//sezione logo 
				it.govpay.web.rs.dars.model.Sezione sezioneLogo = dettaglio.addSezione(Utils.getInstance(this.getLanguage()).getMessageFromResourceBundle(this.nomeServizio + ".sezioneLogo"));
				String etichettaLogo = "";
				String logoBase64 = Base64.encodeBase64String(dominio.getLogo()); 
				BufferedImage image = ImageIO.read(new ByteArrayInputStream(dominio.getLogo()));
				Tika tika = new Tika();
				String mimeTypeFromExt = tika.detect(dominio.getLogo());
				String htmlLogo64 = "data:" + mimeTypeFromExt + ";base64," + logoBase64;
				VoceImage<String> voceLogo = new VoceImage<String>(etichettaLogo, htmlLogo64);
				voceLogo.setWidth(image.getWidth());
				voceLogo.setHeight(image.getHeight());
				sezioneLogo.addVoce(voceLogo);
			}

			// Elementi correlati
			String etichettaUnitaOperative = Utils.getInstance(this.getLanguage()).getMessageFromResourceBundle(this.nomeServizio + ".elementoCorrelato.unitaOperative.titolo");
			String etichettaIban = Utils.getInstance(this.getLanguage()).getMessageFromResourceBundle(this.nomeServizio + ".elementoCorrelato.iban.titolo");
			String etichettaTributi = Utils.getInstance(this.getLanguage()).getMessageFromResourceBundle(this.nomeServizio + ".elementoCorrelato.tributi.titolo");

			UnitaOperative uoDars =new UnitaOperative();
			String idDominioId =  Utils.getInstance(this.getLanguage()).getMessageFromResourceBundle(uoDars.getNomeServizio() + ".idDominio.id");
			Map<String, String> params = new HashMap<String, String>();
			params.put(idDominioId, dominio.getId() + "");
			URI uoDettaglio = Utils.creaUriConParametri(uoDars.getPathServizio(), params );
			dettaglio.addElementoCorrelato(etichettaUnitaOperative, uoDettaglio);

			Iban ibanDars =new Iban();
			idDominioId =  Utils.getInstance(this.getLanguage()).getMessageFromResourceBundle(ibanDars.getNomeServizio() + ".idDominio.id");
			params = new HashMap<String, String>();
			params.put(idDominioId, dominio.getId() + "");
			URI ibanDettaglio = Utils.creaUriConParametri(ibanDars.getPathServizio(), params );
			dettaglio.addElementoCorrelato(etichettaIban, ibanDettaglio);

			Tributi tributiDars =new Tributi();
			idDominioId =  Utils.getInstance(this.getLanguage()).getMessageFromResourceBundle(tributiDars.getNomeServizio() + ".idDominio.id");
			params = new HashMap<String, String>();
			params.put(idDominioId, dominio.getId() + "");
			URI tributoDettaglio = Utils.creaUriConParametri(tributiDars.getPathServizio(), params );
			dettaglio.addElementoCorrelato(etichettaTributi, tributoDettaglio);

			this.log.info("Esecuzione " + methodName + " completata.");

			return dettaglio;
		}catch(WebApplicationException e){
			throw e;
		}catch(Exception e){
			throw new ConsoleException(e);
		}
	}

	@Override
	public Elenco delete(List<Long> idsToDelete, List<RawParamValue> rawValues, UriInfo uriInfo, BasicBD bd) throws WebApplicationException, ConsoleException, DeleteException {	return null; 	}

	@Override
	public Dettaglio insert(InputStream is, UriInfo uriInfo, BasicBD bd) throws WebApplicationException, ConsoleException, ValidationException, DuplicatedEntryException{
		String methodName = "Insert " + this.titoloServizio;

		try{
			this.log.info("Esecuzione " + methodName + " in corso...");
			// Operazione consentita solo ai ruoli con diritto di scrittura
			this.darsService.checkDirittiServizioScrittura(bd, this.funzionalita);

			List<Object> lista = this.creaDominioEAnagrafica(is, uriInfo, bd);


			Dominio entry = (Dominio) lista.get(0);
			UnitaOperativa uo = (UnitaOperativa) lista.get(1); 

			this.checkEntry(entry, null);

			DominiBD dominiBD = new DominiBD(bd);

			try{
				dominiBD.getDominio(entry.getCodDominio());
				String msg = Utils.getInstance(this.getLanguage()).getMessageWithParamsFromResourceBundle(this.nomeServizio + ".oggettoEsistente", entry.getCodDominio());
				throw new DuplicatedEntryException(msg);
			}catch(NotFoundException e){}

			UnitaOperativeBD uoBd = new UnitaOperativeBD(bd);

			Tributi tributiDars = new Tributi();

			TipiTributoBD tipiTributoBD = new TipiTributoBD(bd);
			TipoTributo bolloT = tipiTributoBD.getTipoTributo(it.govpay.model.Tributo.BOLLOT);

			TributiBD tributiBD = new TributiBD(bd);

			Tributo tributo = new Tributo();
			tributo.setCodTributo(it.govpay.model.Tributo.BOLLOT);
			tributo.setAbilitato(false);
			tributo.setDescrizione(Utils.getInstance(this.getLanguage()).getMessageFromResourceBundle(tributiDars.getNomeServizio()+ ".bolloTelematico.descrizione"));

			//TODO controllare il salvataggio

			//			tributo.setCodContabilitaDefault(Utils.getInstance(this.getLanguage()).getMessageFromResourceBundle(tributiDars.getNomeServizio()+ ".bolloTelematico.codContabilita")); 
			//			tributo.setTipoContabilitaDefault(TipoContabilta.toEnum(Utils.getInstance(this.getLanguage()).getMessageFromResourceBundle(tributiDars.getNomeServizio()+ ".bolloTelematico.tipoContabilita")));
			//			tributo.setCodTributoIuvDefault(TipoContabilta.toEnum(Utils.getInstance(this.getLanguage()).getMessageFromResourceBundle(tributiDars.getNomeServizio()+ ".bolloTelematico.tipoContabilita")));
			tributo.setIdTipoTributo(bolloT.getId());

			// Inserimento di Dominio, UO e Tributo BolloTelematico in maniera transazionale.
			bd.setAutoCommit(false); 
			dominiBD.insertDominio(entry);
			uo.setIdDominio(entry.getId());
			uoBd.insertUnitaOperativa(uo);
			tributo.setIdDominio(entry.getId());
			tributiBD.insertTributo(tributo);
			bd.commit();

			// ripristino l'autocommit.
			bd.setAutoCommit(true); 

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
	public Dominio creaEntry(InputStream is, UriInfo uriInfo, BasicBD bd)	throws WebApplicationException, ConsoleException {
		String methodName = "creaEntry " + this.titoloServizio;
		Dominio entry = null;
		String segregationCodeId = Utils.getInstance(this.getLanguage()).getMessageFromResourceBundle(this.nomeServizio + ".segregationCode.id");
		String logoId = Utils.getInstance(this.getLanguage()).getMessageFromResourceBundle(this.nomeServizio + ".logo.id");

		try{
			this.log.info("Esecuzione " + methodName + " in corso...");
			// Operazione consentita solo ai ruoli con diritto di scrittura
			this.darsService.checkDirittiServizioScrittura(bd, this.funzionalita);

			JsonConfig jsonConfig = new JsonConfig();
			ByteArrayOutputStream baos = new ByteArrayOutputStream();
			Utils.copy(is, baos);

			baos.flush();
			baos.close();

			JSONObject jsonObjectDominio = JSONObject.fromObject( baos.toString() );

			String segregationCode = jsonObjectDominio.getString(segregationCodeId);
			jsonObjectDominio.remove(segregationCodeId);

			JSONArray jsonArrayFile = jsonObjectDominio.getJSONArray(logoId);
			jsonObjectDominio.remove(logoId);

			jsonConfig.setRootClass(Dominio.class);
			entry = (Dominio) JSONObject.toBean( jsonObjectDominio, jsonConfig );

			if(StringUtils.isNotEmpty(segregationCode)){
				entry.setSegregationCode(Integer.parseInt(segregationCode)); 
			}

			if(jsonArrayFile != null && jsonArrayFile.size() == 1){
				JSONObject jsonObjectFile = jsonArrayFile.getJSONObject(0);
				//				String fileName = jsonObjectFile.getString(InputFile.FILENAME);
				String data64 = jsonObjectFile.getString(InputFile.DATA);
				entry.setLogo(Base64.decodeBase64(data64));
			}

			entry.setTabellaControparti(DominioUtils.buildInformativaControparte(entry, true));
			entry.setContiAccredito(DominioUtils.buildInformativaContoAccredito(entry, new ArrayList<IbanAccredito>()));
			this.log.info("Esecuzione " + methodName + " completata.");
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
		String segregationCodeId = Utils.getInstance(this.getLanguage()).getMessageFromResourceBundle(this.nomeServizio + ".segregationCode.id");
		String logoId = Utils.getInstance(this.getLanguage()).getMessageFromResourceBundle(this.nomeServizio + ".logo.id");
		String abilitaModificaLogoId = Utils.getInstance(this.getLanguage()).getMessageFromResourceBundle(this.nomeServizio + ".abilitaModificaLogo.id");
		try{
			this.log.info("Esecuzione " + methodName + " in corso...");
			// Operazione consentita solo ai ruoli con diritto di scrittura
			this.darsService.checkDirittiServizioScrittura(bd, this.funzionalita);

			JsonConfig jsonConfig = new JsonConfig();
			ByteArrayOutputStream baos = new ByteArrayOutputStream();
			Utils.copy(is, baos);

			baos.flush();
			baos.close();

			JSONObject jsonObjectDominio = JSONObject.fromObject( baos.toString() );  
			//			String ragioneSocialeId = Utils.getInstance(this.getLanguage()).getMessageFromResourceBundle(this.nomeServizio + ".ragioneSociale.id");
			//			JSONArray jsonArray = jsonObjectDominio.getJSONArray(ragioneSocialeId);
			//
			//			String ragSocDominio = jsonArray.getString(0);
			//			String ragSocAnagrafica = jsonArray.getString(1);
			//
			//			jsonObjectDominio.remove(ragioneSocialeId);


			String segregationCode = jsonObjectDominio.getString(segregationCodeId);
			jsonObjectDominio.remove(segregationCodeId);

			Boolean abilitaModificaLogo = false;
			JSONArray jsonArrayFile =  null;
			try {
				if(jsonObjectDominio.getBoolean(abilitaModificaLogoId)){
					jsonArrayFile = jsonObjectDominio.getJSONArray(logoId);
					abilitaModificaLogo = true;
				}
			}catch(Exception e) {	}

			jsonObjectDominio.remove(logoId);
			jsonObjectDominio.remove(abilitaModificaLogoId);

			jsonConfig.setRootClass(Dominio.class);

			Dominio  entry = (Dominio) JSONObject.toBean( jsonObjectDominio, jsonConfig );
			entry.setTabellaControparti(DominioUtils.buildInformativaControparte(entry, true));
			entry.setContiAccredito(DominioUtils.buildInformativaContoAccredito(entry, new ArrayList<IbanAccredito>()));

			// azzero l'id applicazione default se ho selezionato nessuna.
			if(entry.getIdApplicazioneDefault() != null && entry.getIdApplicazioneDefault().longValue() == -1l) {
				entry.setIdApplicazioneDefault(null);
			} 

			if(StringUtils.isNotEmpty(segregationCode)){
				entry.setSegregationCode(Integer.parseInt(segregationCode)); 
			}

			if(jsonArrayFile != null && jsonArrayFile.size() == 1){
				JSONObject jsonObjectFile = jsonArrayFile.getJSONObject(0);
				//				String fileName = jsonObjectFile.getString(InputFile.FILENAME);
				String data64 = jsonObjectFile.getString(InputFile.DATA);
				entry.setLogo(Base64.decodeBase64(data64));
			}

			jsonConfig.setRootClass(Anagrafica.class);
			Anagrafica anagrafica = (Anagrafica) JSONObject.toBean( jsonObjectDominio, jsonConfig );
			String ragSocAnagrafica = anagrafica.getRagioneSociale();
			entry.setRagioneSociale(ragSocAnagrafica);
			anagrafica.setRagioneSociale(null); 

			String uoIdId = Utils.getInstance(this.getLanguage()).getMessageFromResourceBundle(this.nomeServizio + ".uoId.id");
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
			uo.setCodUo(it.govpay.model.Dominio.EC);
			anagrafica.setCodUnivoco(uo.getCodUo());
			if(entry.getId() != null) {
				uo.setIdDominio(entry.getId());
			} 
			uo.setId(uoIdLong); 

			list.add(entry);
			list.add(uo);
			list.add(abilitaModificaLogo);

			this.log.info("Esecuzione " + methodName + " completata.");
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
			throw new ValidationException(Utils.getInstance(this.getLanguage()).getMessageWithParamsFromResourceBundle(this.nomeServizio + ".creazione.erroreLunghezzaCodDominioErrata", codIntSize));
		}
		try { 
			Long.parseLong(entry.getCodDominio());
		} catch (NumberFormatException e) {
			throw new ValidationException(Utils.getInstance(this.getLanguage()).getMessageWithParamsFromResourceBundle(this.nomeServizio + ".creazione.erroreFormatoCodDominioErrata", entry.getCodDominio()));
		}

		if(entry.getRagioneSociale() == null || entry.getRagioneSociale().isEmpty()) {
			throw new ValidationException(Utils.getInstance(this.getLanguage()).getMessageFromResourceBundle(this.nomeServizio + ".creazione.erroreRagioneSocialeObbligatoria"));
		}

		String maxWidthS = Utils.getInstance(this.getLanguage()).getMessageFromResourceBundle(this.nomeServizio + ".logo.maxWidth");
		String maxHeightS = Utils.getInstance(this.getLanguage()).getMessageFromResourceBundle(this.nomeServizio + ".logo.maxHeight");
		String maxByteSizeS = Utils.getInstance(this.getLanguage()).getMessageFromResourceBundle(this.nomeServizio + ".logo.maxByteSize");
		long maxByteSize = Long.parseLong(maxByteSizeS);
		maxByteSizeS = Utils.fileSizeConverter(maxByteSize);
		int maxWidth = Integer.parseInt(maxWidthS), maxHeight  = Integer.parseInt(maxHeightS);  
		// validazione immagine in creazione
		if(entry.getLogo() != null && entry.getLogo().length > 0) {
			if(entry.getLogo().length > maxByteSize)
				throw new ValidationException(Utils.getInstance(this.getLanguage()).getMessageWithParamsFromResourceBundle(this.nomeServizio + ".creazione.erroreLogoSizeErrata",maxByteSizeS));

			try {
				BufferedImage image = ImageIO.read(new ByteArrayInputStream(entry.getLogo()));

				if(image.getWidth() > maxWidth || image.getHeight() > maxHeight){
					String msg = maxWidth + "x" + maxHeight;
					throw new ValidationException(Utils.getInstance(this.getLanguage()).getMessageWithParamsFromResourceBundle(this.nomeServizio + ".creazione.erroreLogoDimensioniErrate",msg));
				}

			} catch (IOException e) { 
				this.log.error(e.getMessage());
				throw new ValidationException(Utils.getInstance(this.getLanguage()).getMessageFromResourceBundle(this.nomeServizio + ".creazione.erroreDecodificaLogo"));
			}
		}

		if(oldEntry != null) { //caso update
			if(!oldEntry.getCodDominio().equals(entry.getCodDominio())) {
				throw new ValidationException(Utils.getInstance(this.getLanguage()).getMessageWithParamsFromResourceBundle(this.nomeServizio + ".aggiornamento.erroreCodDominioNonCoincide",oldEntry.getCodDominio(),entry.getCodDominio()));
			}

			// validazione immagine in update
			if(entry.getLogo() != null && entry.getLogo().length > 0) {
				if(entry.getLogo().length > maxByteSize)
					throw new ValidationException(Utils.getInstance(this.getLanguage()).getMessageWithParamsFromResourceBundle(this.nomeServizio + ".aggiornamento.erroreLogoSizeErrata",maxByteSizeS));

				try {
					BufferedImage image = ImageIO.read(new ByteArrayInputStream(entry.getLogo()));

					if(image.getWidth() > maxWidth || image.getHeight() > maxHeight){
						String msg = maxWidth + "x" + maxHeight;
						throw new ValidationException(Utils.getInstance(this.getLanguage()).getMessageWithParamsFromResourceBundle(this.nomeServizio + ".aggiornamento.erroreLogoDimensioniErrate",msg));
					}

				} catch (IOException e) { 
					this.log.error(e.getMessage());
					throw new ValidationException(Utils.getInstance(this.getLanguage()).getMessageFromResourceBundle(this.nomeServizio + ".aggiornamento.erroreDecodificaLogo"));
				}
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

			List<Object> lista = this.creaDominioEAnagrafica(is, uriInfo, bd);

			Dominio entry = (Dominio) lista.get(0);
			UnitaOperativa uo = (UnitaOperativa) lista.get(1);
			Boolean abilitaModificaLogo = (Boolean) lista.get(2); 

			DominiBD dominiBD = new DominiBD(bd);
			Dominio oldEntry = dominiBD.getDominio(entry.getCodDominio());

			// se l'utente non ha modificato il logo reimposto quello originale
			if(!abilitaModificaLogo) {
				entry.setLogo(oldEntry.getLogo());
			}

			this.checkEntry(entry, oldEntry);

			UnitaOperativeBD uoBd = new UnitaOperativeBD(bd);

			// Aggiornamento di Dominio e UO in maniera transazionale.
			bd.setAutoCommit(false); 
			dominiBD.updateDominio(entry); 
			uoBd.updateUnitaOperativa(uo);
			bd.commit();

			// ripristino l'autocommit.
			bd.setAutoCommit(true); 

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
	public String getTitolo(Dominio entry, BasicBD bd)  throws ConsoleException{
		StringBuilder sb = new StringBuilder();

		sb.append(entry.getRagioneSociale());
		sb.append(" (").append(entry.getCodDominio()).append(")");
		return sb.toString();
	}

	@Override
	public String getSottotitolo(Dominio entry, BasicBD bd)  throws ConsoleException {

		StringBuilder sb = new StringBuilder();
		try{

			sb.append(Utils.getAbilitatoAsLabel(entry.isAbilitato()));
			sb.append(", Stazione: ").append(entry.getStazione(bd).getCodStazione());

		}catch(Exception e){
			throw new ConsoleException(e);
		}
		return sb.toString();
	}

	@Override
	public Map<String, Voce<String>> getVoci(Dominio entry, BasicBD bd) throws ConsoleException { 
		Map<String, Voce<String>> valori = new HashMap<String, Voce<String>>();

		valori.put(Utils.getInstance(this.getLanguage()).getMessageFromResourceBundle(this.nomeServizio + ".ragioneSociale.id"),
				new Voce<String>(Utils.getInstance(this.getLanguage()).getMessageFromResourceBundle(this.nomeServizio + ".ragioneSociale.label"),entry.getRagioneSociale()));

		valori.put(Utils.getInstance(this.getLanguage()).getMessageFromResourceBundle(this.nomeServizio + ".codDominio.id"),
				new Voce<String>(Utils.getInstance(this.getLanguage()).getMessageFromResourceBundle(this.nomeServizio + ".codDominio.label"),entry.getCodDominio()));

		try {
			Stazione stazione = entry.getStazione(bd);

			if(stazione != null){
				Intermediario intermediario = stazione.getIntermediario(bd);
				if(intermediario != null) {
					valori.put(Utils.getInstance(this.getLanguage()).getMessageFromResourceBundle(this.nomeServizio + ".codIntermediario.id"),
							new Voce<String>(Utils.getInstance(this.getLanguage()).getMessageFromResourceBundle(this.nomeServizio + ".codIntermediario.label"),intermediario.getCodIntermediario()));
				}
			}
		} catch (ServiceException e) {
			throw new ConsoleException(e);
		}

		// stato del dominio
		// 
		if(!entry.isAbilitato()) {
			valori.put(Utils.getInstance(this.getLanguage()).getMessageFromResourceBundle(this.nomeServizio + ".stato.id"),
					new Voce<String>(Utils.getInstance(this.getLanguage()).getMessageFromResourceBundle(this.nomeServizio + ".stato.disabilitato.label"),
							Utils.getInstance(this.getLanguage()).getMessageFromResourceBundle(this.nomeServizio + ".stato.disabilitato")));
		} else {
			if(entry.getNdpStato() == null){
				valori.put(Utils.getInstance(this.getLanguage()).getMessageFromResourceBundle(this.nomeServizio + ".stato.id"),
						new Voce<String>(Utils.getInstance(this.getLanguage()).getMessageFromResourceBundle(this.nomeServizio + ".stato.nonVerificato.label"),
								Utils.getInstance(this.getLanguage()).getMessageFromResourceBundle(this.nomeServizio + ".stato.nonVerificato")));
			} else {
				if(entry.getNdpStato().intValue() == 0){
					valori.put(Utils.getInstance(this.getLanguage()).getMessageFromResourceBundle(this.nomeServizio + ".stato.id"),
							new Voce<String>(Utils.getInstance(this.getLanguage()).getMessageFromResourceBundle(this.nomeServizio + ".stato.ok.label"),
									Utils.getInstance(this.getLanguage()).getMessageFromResourceBundle(this.nomeServizio + ".stato.ok")));
				} else {
					String statoErrore = Utils.getInstance(this.getLanguage()).getMessageFromResourceBundle(this.nomeServizio + ".stato.errore.label");

					if(StringUtils.isNotEmpty(entry.getNdpDescrizione())){
						statoErrore = entry.getNdpDescrizione();
					}
					valori.put(Utils.getInstance(this.getLanguage()).getMessageFromResourceBundle(this.nomeServizio + ".stato.id"),
							new Voce<String>(statoErrore,
									Utils.getInstance(this.getLanguage()).getMessageFromResourceBundle(this.nomeServizio + ".stato.errore")));

					valori.put(Utils.getInstance(this.getLanguage()).getMessageFromResourceBundle(this.nomeServizio + ".anomalia.id"),
							new Voce<String>(Utils.getInstance(this.getLanguage()).getMessageFromResourceBundle(this.nomeServizio + ".anomalia.label"),statoErrore));
				}
			}
		}


		return valori;
	}

	@Override
	public String esporta(List<Long> idsToExport, List<RawParamValue> rawValues, UriInfo uriInfo, BasicBD bd, ZipOutputStream zout)
			throws WebApplicationException, ConsoleException,ExportException {
		StringBuffer sb = new StringBuffer();
		if(idsToExport != null && idsToExport.size() > 0) {
			for (Long long1 : idsToExport) {

				if(sb.length() > 0) {
					sb.append(", ");
				}

				sb.append(long1);
			}
		}

		String methodName = "esporta " + this.titoloServizio + "[" + sb.toString() + "]";

		if(idsToExport == null || idsToExport.size() == 0) {
			List<String> msg = new ArrayList<String>();
			msg.add(Utils.getInstance(this.getLanguage()).getMessageFromResourceBundle(this.nomeServizio+".esporta.erroreSelezioneVuota"));
			throw new ExportException(msg, EsitoOperazione.ERRORE);
		}

		if(idsToExport.size() == 1) {
			return this.esporta(idsToExport.get(0), rawValues, uriInfo, bd, zout);
		} 

		String fileName = "Domini.zip";
		try{
			this.log.info("Esecuzione " + methodName + " in corso...");
			// Operazione consentita solo ai ruoli con diritto di lettura
			this.darsService.checkDirittiServizioLettura(bd, this.funzionalita);

			DominiBD dominiBD = new DominiBD(bd);

			for (Long idDominio : idsToExport) {
				Dominio dominio = dominiBD.getDominio(idDominio);
				String folderName = dominio.getCodDominio();

				IbanAccreditoBD ibanAccreditoDB = new IbanAccreditoBD(bd);
				IbanAccreditoFilter filter = ibanAccreditoDB.newFilter();
				filter.setIdDominio(idDominio);
				List<IbanAccredito> ibans = ibanAccreditoDB.findAll(filter);
				final byte[] contiAccredito = DominioUtils.buildInformativaContoAccredito(dominio, ibans);

				ZipEntry contiAccreditoXml = new ZipEntry(folderName + "/contiAccredito.xml");
				zout.putNextEntry(contiAccreditoXml);
				zout.write(contiAccredito);
				zout.closeEntry();

				final byte[] informativa = DominioUtils.buildInformativaControparte(dominio, true);

				ZipEntry informativaXml = new ZipEntry(folderName + "/informativa.xml");
				zout.putNextEntry(informativaXml);
				zout.write(informativa);
				zout.closeEntry();

			}
			zout.flush();
			zout.close();

			this.log.info("Esecuzione " + methodName + " completata.");

			return fileName;
		}catch(WebApplicationException e){
			throw e;
		}catch(Exception e){
			throw new ConsoleException(e);
		}
	}

	@Override
	public String esporta(Long idToExport, List<RawParamValue> rawValues, UriInfo uriInfo, BasicBD bd, ZipOutputStream zout)	throws WebApplicationException, ConsoleException,ExportException {
		String methodName = "esporta " + this.titoloServizio + "[" + idToExport + "]";  


		try{
			this.log.info("Esecuzione " + methodName + " in corso...");
			// Operazione consentita solo ai ruoli con diritto di lettura
			this.darsService.checkDirittiServizioLettura(bd, this.funzionalita);

			DominiBD dominiBD = new DominiBD(bd);

			Dominio dominio = dominiBD.getDominio(idToExport);
			String fileName = "Dominio_"+dominio.getCodDominio()+".zip";

			IbanAccreditoBD ibanAccreditoDB = new IbanAccreditoBD(bd);
			IbanAccreditoFilter filter = ibanAccreditoDB.newFilter();
			filter.setIdDominio(idToExport);
			List<IbanAccredito> ibans = ibanAccreditoDB.findAll(filter);
			final byte[] contiAccredito = DominioUtils.buildInformativaContoAccredito(dominio, ibans);

			ZipEntry contiAccreditoXml = new ZipEntry("contiAccredito.xml");
			zout.putNextEntry(contiAccreditoXml);
			zout.write(contiAccredito);
			zout.closeEntry();

			final byte[] informativa = DominioUtils.buildInformativaControparte(dominio, true);

			ZipEntry informativaXml = new ZipEntry("informativa.xml");
			zout.putNextEntry(informativaXml);
			zout.write(informativa);
			zout.closeEntry();

			zout.flush();
			zout.close();

			this.log.info("Esecuzione " + methodName + " completata.");

			return fileName;
		}catch(WebApplicationException e){
			throw e;
		}catch(Exception e){
			throw new ConsoleException(e);
		}
	}
	@Override
	public Object uplaod(MultipartFormDataInput input, UriInfo uriInfo, BasicBD bd)	throws WebApplicationException, ConsoleException, ValidationException { return null;}
}
