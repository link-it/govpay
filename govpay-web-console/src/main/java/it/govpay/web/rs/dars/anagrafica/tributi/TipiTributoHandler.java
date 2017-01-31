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

import org.apache.commons.lang.StringUtils;
import org.apache.logging.log4j.Logger;
import org.jboss.resteasy.plugins.providers.multipart.MultipartFormDataInput;
import org.openspcoop2.generic_project.exception.NotFoundException;
import org.openspcoop2.generic_project.expression.SortOrder;

import it.govpay.bd.BasicBD;
import it.govpay.bd.FilterSortWrapper;
import it.govpay.bd.anagrafica.TipiTributoBD;
import it.govpay.bd.anagrafica.filters.TipoTributoFilter;
import it.govpay.model.TipoTributo;
import it.govpay.model.Tributo;
import it.govpay.model.Tributo.TipoContabilta;
import it.govpay.web.rs.BaseRsService;
import it.govpay.web.rs.dars.BaseDarsHandler;
import it.govpay.web.rs.dars.BaseDarsService;
import it.govpay.web.rs.dars.IDarsHandler;
import it.govpay.web.rs.dars.exception.ConsoleException;
import it.govpay.web.rs.dars.exception.DuplicatedEntryException;
import it.govpay.web.rs.dars.exception.ValidationException;
import it.govpay.web.rs.dars.model.Dettaglio;
import it.govpay.web.rs.dars.model.Elenco;
import it.govpay.web.rs.dars.model.InfoForm;
import it.govpay.web.rs.dars.model.InfoForm.Sezione;
import it.govpay.web.rs.dars.model.RawParamValue;
import it.govpay.web.rs.dars.model.Voce;
import it.govpay.web.rs.dars.model.input.ParamField;
import it.govpay.web.rs.dars.model.input.RefreshableParamField;
import it.govpay.web.rs.dars.model.input.base.InputNumber;
import it.govpay.web.rs.dars.model.input.base.InputText;
import it.govpay.web.rs.dars.model.input.base.SelectList;
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
			fsw.setField(it.govpay.orm.TipoTributo.model().DESCRIZIONE);
			fsw.setSortOrder(SortOrder.ASC);
			filter.getFilterSortList().add(fsw);

			String codTributoId = Utils.getInstance().getMessageFromResourceBundle(this.nomeServizio + ".codTributo.id");
			String codTributo = this.getParameter(uriInfo, codTributoId, String.class);
			if(StringUtils.isNotEmpty(codTributo))
				filter.setCodTributo(codTributo); 

			String tipoContabilitaId = Utils.getInstance().getMessageFromResourceBundle(this.nomeServizio + ".tipoContabilita.id");
			String tipocontabilitaS = this.getParameter(uriInfo, tipoContabilitaId, String.class);
			if(StringUtils.isNotEmpty(tipocontabilitaS)){
				filter.setCodificaTipoContabilita(tipocontabilitaS);
			}

			String codContabilitaId = Utils.getInstance().getMessageFromResourceBundle(this.nomeServizio + ".codContabilita.id");
			String codContabilita = this.getParameter(uriInfo, codContabilitaId, String.class);
			if(StringUtils.isNotEmpty(codContabilita))
				filter.setCodContabilita(codContabilita);

			String descrizioneId = Utils.getInstance().getMessageFromResourceBundle(this.nomeServizio + ".descrizione.id");
			String descrizione = this.getParameter(uriInfo, descrizioneId, String.class);
			if(StringUtils.isNotEmpty(descrizione))
				filter.setDescrizione(descrizione);


			long count = tipiTributoBD.count(filter);

			// visualizza la ricerca solo se i risultati sono > del limit
			visualizzaRicerca = visualizzaRicerca && this.visualizzaRicerca(count, limit);
			InfoForm infoRicerca = this.getInfoRicerca(uriInfo, bd, visualizzaRicerca);

			Elenco elenco = new Elenco(this.titoloServizio, infoRicerca, this.getInfoCreazione(uriInfo, bd), count, esportazione, cancellazione);  

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

	@SuppressWarnings("unchecked")
	@Override
	public InfoForm getInfoRicerca(UriInfo uriInfo, BasicBD bd, boolean visualizzaRicerca, Map<String,String> parameters) throws ConsoleException {
		URI ricerca =  this.getUriRicerca(uriInfo, bd);
		InfoForm infoRicerca = new InfoForm(ricerca);

		if(visualizzaRicerca) {

			String codTributoId = Utils.getInstance().getMessageFromResourceBundle(this.nomeServizio + ".codTributo.id");
			String tipoContabilitaId = Utils.getInstance().getMessageFromResourceBundle(this.nomeServizio + ".tipoContabilita.id");
			String codContabilitaId = Utils.getInstance().getMessageFromResourceBundle(this.nomeServizio + ".codContabilita.id");
			String descrizioneId = Utils.getInstance().getMessageFromResourceBundle(this.nomeServizio + ".descrizione.id");

			if(infoRicercaMap == null){
				this.initInfoRicerca(uriInfo, bd);
			}

			Sezione sezioneRoot = infoRicerca.getSezioneRoot();

			InputText descrizione = (InputText) infoRicercaMap.get(descrizioneId);
			descrizione.setDefaultValue(null);
			sezioneRoot.addField(descrizione);

			InputText codTributo = (InputText) infoRicercaMap.get(codTributoId);
			codTributo.setDefaultValue(null);
			sezioneRoot.addField(codTributo);

			SelectList<String> tipoContabilita = (SelectList<String>) infoRicercaMap.get(tipoContabilitaId);
			tipoContabilita.setDefaultValue("");
			sezioneRoot.addField(tipoContabilita);

			InputText codContabilita = (InputText) infoRicercaMap.get(codContabilitaId);
			codContabilita.setDefaultValue(null);
			sezioneRoot.addField(codContabilita);

		}
		return infoRicerca;
	}

	private void initInfoRicerca(UriInfo uriInfo, BasicBD bd) throws ConsoleException{
		if(infoRicercaMap == null){
			infoRicercaMap = new HashMap<String, ParamField<?>>();

			String codTributoId = Utils.getInstance().getMessageFromResourceBundle(this.nomeServizio + ".codTributo.id");
			String tipoContabilitaId = Utils.getInstance().getMessageFromResourceBundle(this.nomeServizio + ".tipoContabilita.id");
			String codContabilitaId = Utils.getInstance().getMessageFromResourceBundle(this.nomeServizio + ".codContabilita.id");
			String descrizioneId = Utils.getInstance().getMessageFromResourceBundle(this.nomeServizio + ".descrizione.id");

			//descrizione
			String descrizioneLabel = Utils.getInstance().getMessageFromResourceBundle(this.nomeServizio + ".descrizione.label");
			InputText descrizione = new InputText(descrizioneId, descrizioneLabel, null, false, false, true, 1, 255);
			infoRicercaMap.put(descrizioneId, descrizione);

			// codTributo
			String codTributoLabel = Utils.getInstance().getMessageFromResourceBundle(this.nomeServizio + ".codTributo.label");
			InputText codTributo = new InputText(codTributoId, codTributoLabel, null, false, false, true, 1, 255);
			infoRicercaMap.put(codTributoId, codTributo);

			// tipoContabilita
			String tipoContabilitaLabel = Utils.getInstance().getMessageFromResourceBundle(this.nomeServizio + ".tipoContabilita.label");
			List<Voce<String>> tipoContabilitaValues = new ArrayList<Voce<String>>();
			tipoContabilitaValues.add(new Voce<String>(Utils.getInstance().getMessageFromResourceBundle("commons.label.qualsiasi"), ""));
			tipoContabilitaValues.add(new Voce<String>(Utils.getInstance().getMessageFromResourceBundle(this.nomeServizio + ".tipoContabilita.capitolo"), TipoContabilta.CAPITOLO.getCodifica()));
			tipoContabilitaValues.add(new Voce<String>(Utils.getInstance().getMessageFromResourceBundle(this.nomeServizio + ".tipoContabilita.speciale"), TipoContabilta.SPECIALE.getCodifica()));
			tipoContabilitaValues.add(new Voce<String>(Utils.getInstance().getMessageFromResourceBundle(this.nomeServizio + ".tipoContabilita.siope"), TipoContabilta.SIOPE.getCodifica()));
			tipoContabilitaValues.add(new Voce<String>(Utils.getInstance().getMessageFromResourceBundle(this.nomeServizio + ".tipoContabilita.altro"), TipoContabilta.ALTRO.getCodifica()));
			SelectList<String> tipoContabilita = new SelectList<String>(tipoContabilitaId, tipoContabilitaLabel, null, false, false, true, tipoContabilitaValues );
			infoRicercaMap.put(tipoContabilitaId, tipoContabilita);

			// codContabilita
			String codContabilitaLabel = Utils.getInstance().getMessageFromResourceBundle(this.nomeServizio + ".codContabilita.label");
			InputText codContabilita = new InputText(codContabilitaId, codContabilitaLabel, null, false, false, true, 1, 255);
			infoRicercaMap.put(codContabilitaId, codContabilita);
		}
	}

	@SuppressWarnings("unchecked")
	@Override
	public InfoForm getInfoCreazione(UriInfo uriInfo, BasicBD bd) throws ConsoleException {
		URI creazione = this.getUriCreazione(uriInfo, bd);
		InfoForm infoCreazione = new InfoForm(creazione,Utils.getInstance().getMessageFromResourceBundle(this.nomeServizio + ".creazione.titolo"));

		try{
			String codTributoId = Utils.getInstance().getMessageFromResourceBundle(this.nomeServizio + ".codTributo.id");
			String tributoId = Utils.getInstance().getMessageFromResourceBundle(this.nomeServizio + ".id.id");
			String descrizioneId = Utils.getInstance().getMessageFromResourceBundle(this.nomeServizio + ".descrizione.id");
			String codificaTributoInIuvId = Utils.getInstance().getMessageFromResourceBundle(this.nomeServizio + ".codificaTributoInIuv.id");
			String tipoContabilitaId = Utils.getInstance().getMessageFromResourceBundle(this.nomeServizio + ".tipoContabilita.id");
			String codContabilitaId = Utils.getInstance().getMessageFromResourceBundle(this.nomeServizio + ".codContabilita.id");

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

			SelectList<String> tipoContabilita = (SelectList<String>) infoCreazioneMap.get(tipoContabilitaId);
			tipoContabilita.setDefaultValue(null);
			sezioneRoot.addField(tipoContabilita);

			InputText codContabilita = (InputText) infoCreazioneMap.get(codContabilitaId);
			codContabilita.setDefaultValue(null);
			sezioneRoot.addField(codContabilita);

			InputText codificaTributoInIuv = (InputText) infoCreazioneMap.get(codificaTributoInIuvId);
			codificaTributoInIuv.setDefaultValue(null);
			sezioneRoot.addField(codificaTributoInIuv);

		}catch (Exception e) {
			throw new ConsoleException(e);
		}

		return infoCreazione;
	}

	private void initInfoCreazione(UriInfo uriInfo, BasicBD bd) throws ConsoleException{
		if(infoCreazioneMap == null){
			infoCreazioneMap = new HashMap<String, ParamField<?>>();

			String codTributoId = Utils.getInstance().getMessageFromResourceBundle(this.nomeServizio + ".codTributo.id");
			String tributoId = Utils.getInstance().getMessageFromResourceBundle(this.nomeServizio + ".id.id");
			String descrizioneId = Utils.getInstance().getMessageFromResourceBundle(this.nomeServizio + ".descrizione.id");
			String codificaTributoInIuvId = Utils.getInstance().getMessageFromResourceBundle(this.nomeServizio + ".codificaTributoInIuv.id");
			String tipoContabilitaId = Utils.getInstance().getMessageFromResourceBundle(this.nomeServizio + ".tipoContabilita.id");
			String codContabilitaId = Utils.getInstance().getMessageFromResourceBundle(this.nomeServizio + ".codContabilita.id");


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

			// tipoContabilita
			String tipoContabilitaLabel = Utils.getInstance().getMessageFromResourceBundle(this.nomeServizio + ".tipoContabilita.label");
			List<Voce<String>> tipoContabilitaValues = new ArrayList<Voce<String>>();
			tipoContabilitaValues.add(new Voce<String>(Utils.getInstance().getMessageFromResourceBundle(this.nomeServizio + ".tipoContabilita.capitolo"), TipoContabilta.CAPITOLO.getCodifica()));
			tipoContabilitaValues.add(new Voce<String>(Utils.getInstance().getMessageFromResourceBundle(this.nomeServizio + ".tipoContabilita.speciale"), TipoContabilta.SPECIALE.getCodifica()));
			tipoContabilitaValues.add(new Voce<String>(Utils.getInstance().getMessageFromResourceBundle(this.nomeServizio + ".tipoContabilita.siope"), TipoContabilta.SIOPE.getCodifica()));
			tipoContabilitaValues.add(new Voce<String>(Utils.getInstance().getMessageFromResourceBundle(this.nomeServizio + ".tipoContabilita.altro"), TipoContabilta.ALTRO.getCodifica()));
			SelectList<String> tipoContabilita = new SelectList<String>(tipoContabilitaId, tipoContabilitaLabel, null, true, false, true, tipoContabilitaValues );
			tipoContabilita.setSuggestion(Utils.getInstance().getMessageFromResourceBundle(this.nomeServizio + ".tipoContabilita.suggestion"));
			infoCreazioneMap.put(tipoContabilitaId, tipoContabilita);

			// codContabilita
			String codContabilitaLabel = Utils.getInstance().getMessageFromResourceBundle(this.nomeServizio + ".codContabilita.label");
			InputText codContabilita = new InputText(codContabilitaId, codContabilitaLabel, null, true, false, true, 1, 255);
			codContabilita.setValidation("^\\S+$", Utils.getInstance().getMessageFromResourceBundle(this.nomeServizio + ".codContabilita.errorMessage"));
			infoCreazioneMap.put(codContabilitaId, codContabilita);


			// codificaTributoInIuv
			String codificaTributoInIuvLabel = Utils.getInstance().getMessageFromResourceBundle(this.nomeServizio + ".codificaTributoInIuv.label");
			InputText codificaTributoInIuv = new InputText(codificaTributoInIuvId, codificaTributoInIuvLabel, null, false, false, true, 1,4);
			codificaTributoInIuv.setValidation("[0-9]{4}", Utils.getInstance().getMessageFromResourceBundle(this.nomeServizio + ".codificaTributoInIuv.errorMessage"));
			codificaTributoInIuv.setAvanzata(true); 
			infoCreazioneMap.put(codificaTributoInIuvId, codificaTributoInIuv);
		}
	}

	@SuppressWarnings("unchecked")
	@Override
	public InfoForm getInfoModifica(UriInfo uriInfo, BasicBD bd, TipoTributo entry) throws ConsoleException {
		URI modifica = this.getUriModifica(uriInfo, bd);
		InfoForm infoModifica = new InfoForm(modifica,Utils.getInstance().getMessageFromResourceBundle(this.nomeServizio + ".modifica.titolo"));

		String codTributoId = Utils.getInstance().getMessageFromResourceBundle(this.nomeServizio + ".codTributo.id");
		String tributoId = Utils.getInstance().getMessageFromResourceBundle(this.nomeServizio + ".id.id");
		String descrizioneId = Utils.getInstance().getMessageFromResourceBundle(this.nomeServizio + ".descrizione.id");
		String codificaTributoInIuvId = Utils.getInstance().getMessageFromResourceBundle(this.nomeServizio + ".codificaTributoInIuv.id");
		String tipoContabilitaId = Utils.getInstance().getMessageFromResourceBundle(this.nomeServizio + ".tipoContabilita.id");
		String codContabilitaId = Utils.getInstance().getMessageFromResourceBundle(this.nomeServizio + ".codContabilita.id");

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

		SelectList<String> tipoContabilita = (SelectList<String>) infoCreazioneMap.get(tipoContabilitaId);
		tipoContabilita.setDefaultValue(entry.getTipoContabilitaDefault() != null ? entry.getTipoContabilitaDefault().getCodifica() : null);
		sezioneRoot.addField(tipoContabilita);

		InputText codContabilita = (InputText) infoCreazioneMap.get(codContabilitaId);
		codContabilita.setDefaultValue(entry.getCodContabilitaDefault());
		sezioneRoot.addField(codContabilita);

		InputText codificaTributoInIuv = (InputText) infoCreazioneMap.get(codificaTributoInIuvId);
		codificaTributoInIuv.setDefaultValue(entry.getCodTributoIuvDefault());
		sezioneRoot.addField(codificaTributoInIuv);

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
			TipoContabilta tipoContabilita = tributo.getTipoContabilitaDefault() != null ? tributo.getTipoContabilitaDefault() : TipoContabilta.CAPITOLO;
			String tipoContabilitaValue = null;
			switch (tipoContabilita) {
			case ALTRO:
				tipoContabilitaValue = Utils.getInstance().getMessageFromResourceBundle(this.nomeServizio + ".tipoContabilita.altro");
				break;
			case SPECIALE:
				tipoContabilitaValue = Utils.getInstance().getMessageFromResourceBundle(this.nomeServizio + ".tipoContabilita.speciale");
				break;
			case SIOPE:
				tipoContabilitaValue = Utils.getInstance().getMessageFromResourceBundle(this.nomeServizio + ".tipoContabilita.siope");
				break;
			case CAPITOLO:
			default:
				tipoContabilitaValue = Utils.getInstance().getMessageFromResourceBundle(this.nomeServizio + ".tipoContabilita.capitolo");				
				break;
			}

			root.addVoce(Utils.getInstance().getMessageFromResourceBundle(this.nomeServizio + ".tipoContabilita.label"), tipoContabilitaValue);
			if(StringUtils.isNotEmpty(tributo.getCodContabilitaDefault()))
				root.addVoce(Utils.getInstance().getMessageFromResourceBundle(this.nomeServizio + ".codContabilita.label"), tributo.getCodContabilitaDefault());
			if(StringUtils.isNotEmpty(tributo.getCodTributoIuvDefault()))
				root.addVoce(Utils.getInstance().getMessageFromResourceBundle(this.nomeServizio + ".codificaTributoInIuv.label"), tributo.getCodTributoIuvDefault(),true);

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

			String tipoContabilitaId = Utils.getInstance().getMessageFromResourceBundle(this.nomeServizio + ".tipoContabilita.id");
			String tipocontabilitaS = jsonObject.getString(tipoContabilitaId);
			jsonObject.remove(tipoContabilitaId);
			jsonConfig.setRootClass(TipoTributo.class);
			entry = (TipoTributo) JSONObject.toBean( jsonObject, jsonConfig );

			TipoContabilta tipoContabilita =  TipoContabilta.toEnum(tipocontabilitaS);
			entry.setTipoContabilitaDefault(tipoContabilita); 

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
		if(entry == null || entry.getTipoContabilitaDefault() == null) throw new ValidationException(Utils.getInstance().getMessageFromResourceBundle(this.nomeServizio + ".creazione.erroreTipoContabilitaObbligatorio"));
		if(entry == null || entry.getCodContabilitaDefault() == null || entry.getCodContabilitaDefault().isEmpty()) throw new ValidationException(Utils.getInstance().getMessageFromResourceBundle(this.nomeServizio + ".creazione.erroreCodContabilitaObbligatorio"));
		if(StringUtils.contains(entry.getCodContabilitaDefault()," ")) throw new ValidationException(Utils.getInstance().getMessageFromResourceBundle(this.nomeServizio + ".creazione.erroreCodContabilitaNoSpazi"));

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
		sb.append(Utils.getInstance().getMessageWithParamsFromResourceBundle(this.nomeServizio + ".label.titolo", entry.getDescrizione()));
		return sb.toString();
	}

	@Override
	public String getSottotitolo(TipoTributo entry, BasicBD bd) throws ConsoleException {
		StringBuilder sb = new StringBuilder();

		TipoContabilta tipoContabilita = entry.getTipoContabilitaDefault() != null ? entry.getTipoContabilitaDefault() : TipoContabilta.CAPITOLO;
		String tipoContabilitaValue = null;
		switch (tipoContabilita) {
		case ALTRO:
			tipoContabilitaValue = Utils.getInstance().getMessageFromResourceBundle(this.nomeServizio + ".tipoContabilita.altro");
			break;
		case SPECIALE:
			tipoContabilitaValue = Utils.getInstance().getMessageFromResourceBundle(this.nomeServizio + ".tipoContabilita.speciale");
			break;
		case SIOPE:
			tipoContabilitaValue = Utils.getInstance().getMessageFromResourceBundle(this.nomeServizio + ".tipoContabilita.siope");
			break;
		case CAPITOLO:
		default:
			tipoContabilitaValue = Utils.getInstance().getMessageFromResourceBundle(this.nomeServizio + ".tipoContabilita.capitolo");				
			break;
		}

		String codContabilitaValue = StringUtils.isNotEmpty(entry.getCodContabilitaDefault()) ? entry.getCodContabilitaDefault() : "--";

		sb.append(Utils.getInstance().getMessageWithParamsFromResourceBundle(this.nomeServizio + ".label.sottotitolo",entry.getCodTributo(), tipoContabilitaValue,codContabilitaValue));

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

	@Override
	public Object uplaod(MultipartFormDataInput input, UriInfo uriInfo, BasicBD bd)	throws WebApplicationException, ConsoleException, ValidationException { return null;}
}
