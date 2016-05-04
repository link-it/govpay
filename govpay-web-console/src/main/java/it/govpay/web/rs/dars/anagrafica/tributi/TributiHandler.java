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
import org.openspcoop2.generic_project.exception.NotFoundException;
import org.openspcoop2.generic_project.expression.SortOrder;

import it.govpay.bd.BasicBD;
import it.govpay.bd.FilterSortWrapper;
import it.govpay.bd.anagrafica.DominiBD;
import it.govpay.bd.anagrafica.IbanAccreditoBD;
import it.govpay.bd.anagrafica.TributiBD;
import it.govpay.bd.anagrafica.filters.IbanAccreditoFilter;
import it.govpay.bd.anagrafica.filters.TributoFilter;
import it.govpay.bd.model.Dominio;
import it.govpay.bd.model.IbanAccredito;
import it.govpay.bd.model.Tributo;
import it.govpay.bd.model.Tributo.TipoContabilta;
import it.govpay.web.rs.BaseRsService;
import it.govpay.web.rs.dars.BaseDarsHandler;
import it.govpay.web.rs.dars.BaseDarsService;
import it.govpay.web.rs.dars.IDarsHandler;
import it.govpay.web.rs.dars.anagrafica.domini.Domini;
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

public class TributiHandler extends BaseDarsHandler<Tributo> implements IDarsHandler<Tributo>{

	private static Map<String, ParamField<?>> infoCreazioneMap = null;
	private static Map<String, ParamField<?>> infoRicercaMap = null;
	public static final String ANAGRAFICA_UO = "anagrafica";
	private Long idDominio = null;

	public TributiHandler(Logger log, BaseDarsService darsService) {
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

			TributiBD tributiBD = new TributiBD(bd);
			TributoFilter filter = tributiBD.newFilter();
			filter.setOffset(offset);
			filter.setLimit(limit);
			FilterSortWrapper fsw = new FilterSortWrapper();
			fsw.setField(it.govpay.orm.Tributo.model().COD_TRIBUTO);
			fsw.setSortOrder(SortOrder.ASC);
			filter.getFilterSortList().add(fsw);

			// tributi di un dominio

			Domini dominiDars = new Domini();
			String codDominioId = Utils.getInstance().getMessageFromResourceBundle(dominiDars.getNomeServizio() + ".codDominio.id");
			String codDominio = this.getParameter(uriInfo, codDominioId, String.class); 

			if(StringUtils.isNotEmpty(codDominio)){
				filter.setCodDominio(codDominio);
				DominiBD dominiBD = new DominiBD(bd);
				Dominio dominio = dominiBD.getDominio(codDominio);
				this.idDominio = dominio.getId();
				visualizzaRicerca = false;
			} 

			long count = tributiBD.count(filter);

			// visualizza la ricerca solo se i risultati sono > del limit
			visualizzaRicerca = visualizzaRicerca && this.visualizzaRicerca(count, limit);

			InfoForm infoRicerca = visualizzaRicerca ? this.getInfoRicerca(uriInfo, bd) : null;

			Elenco elenco = new Elenco(this.titoloServizio, infoRicerca, this.getInfoCreazione(uriInfo, bd), count, esportazione, cancellazione); 

			UriBuilder uriDettaglioBuilder = BaseRsService.checkDarsURI(uriInfo).path(this.pathServizio).path("{id}");

			List<Tributo> findAll = tributiBD.findAll(filter);

			if(findAll != null && findAll.size() > 0){
				for (Tributo entry : findAll) {
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
		URI ricerca = this.getUriRicerca(uriInfo, bd);
		InfoForm infoRicerca = new InfoForm(ricerca);

		String codTributoId = Utils.getInstance().getMessageFromResourceBundle(this.nomeServizio + ".codTributo.id");
		Domini dominiDars = new Domini();
		String codDominioId = Utils.getInstance().getMessageFromResourceBundle(dominiDars.getNomeServizio() + ".codDominio.id");

		if(infoRicercaMap == null){
			this.initInfoRicerca(uriInfo, bd);

		}

		Sezione sezioneRoot = infoRicerca.getSezioneRoot();

		InputText codTributo = (InputText) infoRicercaMap.get(codTributoId);
		codTributo.setDefaultValue(null);
		sezioneRoot.addField(codTributo);

		// codDominio
		InputText codDominio = (InputText) infoRicercaMap.get(codDominioId);
		DominiBD dominiBD = new DominiBD(bd);
		try {
			Dominio dominio = dominiBD.getDominio(this.idDominio);
			codDominio.setDefaultValue(dominio.getCodDominio());
		} catch (Exception e) {
			throw new ConsoleException(e);
		}
		sezioneRoot.addField(codDominio);

		return infoRicerca;
	}

	private void initInfoRicerca(UriInfo uriInfo, BasicBD bd) throws ConsoleException{
		if(infoRicercaMap == null){
			infoRicercaMap = new HashMap<String, ParamField<?>>();

			String codTributoId = Utils.getInstance().getMessageFromResourceBundle(this.nomeServizio + ".codTributo.id");
			Domini dominiDars = new Domini();
			String codDominioId = Utils.getInstance().getMessageFromResourceBundle(dominiDars.getNomeServizio() + ".codDominio.id");

			// codTributo
			String codTributoLabel = Utils.getInstance().getMessageFromResourceBundle(this.nomeServizio + ".codTributo.label");
			InputText codTributo = new InputText(codTributoId, codTributoLabel, null, false, false, true, 1, 255);
			infoRicercaMap.put(codTributoId, codTributo);

			// idDominio
			InputText codDominio = new InputText(codDominioId, null, null, true, true, false, 1, 255);
			infoRicercaMap.put(codDominioId, codDominio);

		}
	}

	@SuppressWarnings("unchecked")
	@Override
	public InfoForm getInfoCreazione(UriInfo uriInfo, BasicBD bd) throws ConsoleException {
		URI creazione = this.getUriCreazione(uriInfo, bd);
		InfoForm infoCreazione = new InfoForm(creazione);

		String codTributoId = Utils.getInstance().getMessageFromResourceBundle(this.nomeServizio + ".codTributo.id");
		String idDominioId = Utils.getInstance().getMessageFromResourceBundle(this.nomeServizio + ".idDominio.id");
		String idIbanAccreditoId = Utils.getInstance().getMessageFromResourceBundle(this.nomeServizio + ".idIbanAccredito.id");
		String abilitatoId = Utils.getInstance().getMessageFromResourceBundle(this.nomeServizio + ".abilitato.id");
		String tributoId = Utils.getInstance().getMessageFromResourceBundle(this.nomeServizio + ".id.id");
		String descrizioneId = Utils.getInstance().getMessageFromResourceBundle(this.nomeServizio + ".descrizione.id");
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
		sezioneRoot.addField(descrizione);

		InputNumber idDominio = (InputNumber) infoCreazioneMap.get(idDominioId);
		idDominio.setDefaultValue(this.idDominio);
		sezioneRoot.addField(idDominio);

		SelectList<Long> idIbanAccredito  = (SelectList<Long>) infoCreazioneMap.get(idIbanAccreditoId);
		List<Voce<Long>> listaIban = new ArrayList<Voce<Long>>();

		try{
			DominiBD dominiBD = new DominiBD(bd);
			IbanAccreditoBD ibanAccreditoBD = new IbanAccreditoBD(bd);
			IbanAccreditoFilter filterIban = ibanAccreditoBD.newFilter();
			FilterSortWrapper fsw = new FilterSortWrapper();
			fsw.setField(it.govpay.orm.IbanAccredito.model().COD_IBAN);
			fsw.setSortOrder(SortOrder.ASC);
			filterIban.getFilterSortList().add(fsw);
			filterIban.setCodDominio(dominiBD.getDominio(this.idDominio).getCodDominio());   
			List<it.govpay.bd.model.IbanAccredito> findAll = ibanAccreditoBD.findAll(filterIban);

			if(findAll != null && findAll.size() > 0){
				for (it.govpay.bd.model.IbanAccredito ib : findAll) {
					listaIban.add(new Voce<Long>(ib.getCodIban(), ib.getId()));  
				}
			}

		}catch(Exception e){
			throw new ConsoleException(e);
		}
		idIbanAccredito.setValues(listaIban);
		idIbanAccredito.setDefaultValue(null);
		sezioneRoot.addField(idIbanAccredito);

		SelectList<String> tipoContabilita = (SelectList<String>) infoCreazioneMap.get(tipoContabilitaId);
		tipoContabilita.setDefaultValue(null);
		sezioneRoot.addField(tipoContabilita);

		InputText codContabilita = (InputText) infoCreazioneMap.get(codContabilitaId);
		codContabilita.setDefaultValue(null);
		sezioneRoot.addField(codContabilita);

		CheckButton abilitato = (CheckButton) infoCreazioneMap.get(abilitatoId);
		abilitato.setDefaultValue(true); 
		sezioneRoot.addField(abilitato);


		return infoCreazione;
	}

	private void initInfoCreazione(UriInfo uriInfo, BasicBD bd) throws ConsoleException{
		if(infoCreazioneMap == null){
			infoCreazioneMap = new HashMap<String, ParamField<?>>();

			String codTributoId = Utils.getInstance().getMessageFromResourceBundle(this.nomeServizio + ".codTributo.id");
			String idDominioId = Utils.getInstance().getMessageFromResourceBundle(this.nomeServizio + ".idDominio.id");
			String idIbanAccreditoId = Utils.getInstance().getMessageFromResourceBundle(this.nomeServizio + ".idIbanAccredito.id");
			String abilitatoId = Utils.getInstance().getMessageFromResourceBundle(this.nomeServizio + ".abilitato.id");
			String tributoId = Utils.getInstance().getMessageFromResourceBundle(this.nomeServizio + ".id.id");
			String descrizioneId = Utils.getInstance().getMessageFromResourceBundle(this.nomeServizio + ".descrizione.id");
			String tipoContabilitaId = Utils.getInstance().getMessageFromResourceBundle(this.nomeServizio + ".tipoContabilita.id");
			String codContabilitaId = Utils.getInstance().getMessageFromResourceBundle(this.nomeServizio + ".codContabilita.id");

			// id 
			InputNumber id = new InputNumber(tributoId, null, null, true, true, false, 1, 20);
			infoCreazioneMap.put(tributoId, id);

			// codUnitaOperativa
			String codTributoLabel = Utils.getInstance().getMessageFromResourceBundle(this.nomeServizio + ".codTributo.label");
			InputText codTributo = new InputText(codTributoId, codTributoLabel, null, true, false, true, 1, 255);
			codTributo.setSuggestion(Utils.getInstance().getMessageFromResourceBundle(this.nomeServizio + ".codTributo.suggestion"));
			codTributo.setValidation(null, Utils.getInstance().getMessageFromResourceBundle(this.nomeServizio + ".codTributo.errorMessage"));
			infoCreazioneMap.put(codTributoId, codTributo);

			// idDominio
			InputNumber idDominio = new InputNumber(idDominioId, null, null, true, true, false, 1, 255);
			infoCreazioneMap.put(idDominioId, idDominio);

			String idIbanAccreditoLabel = Utils.getInstance().getMessageFromResourceBundle(this.nomeServizio + ".idIbanAccredito.label");
			List<Voce<Long>> ibanValues = new ArrayList<Voce<Long>>();
			SelectList<Long> idIbanAccredito = new SelectList<Long>(idIbanAccreditoId, idIbanAccreditoLabel, null, true, false, true, ibanValues );
			infoCreazioneMap.put(idIbanAccreditoId, idIbanAccredito);

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
			tipoContabilita.setSuggestion(Utils.getInstance().getMessageFromResourceBundle(this.nomeServizio + ".idDominio.suggestion"));
			infoCreazioneMap.put(tipoContabilitaId, tipoContabilita);

			// codContabilita
			String codContabilitaLabel = Utils.getInstance().getMessageFromResourceBundle(this.nomeServizio + ".codContabilita.label");
			InputText codContabilita = new InputText(codContabilitaId, codContabilitaLabel, null, true, false, true, 1, 255);
			infoCreazioneMap.put(codContabilitaId, codContabilita);

			// abilitato
			String abilitatoLabel = Utils.getInstance().getMessageFromResourceBundle(this.nomeServizio + ".abilitato.label");
			CheckButton abiliato = new CheckButton(abilitatoId, abilitatoLabel, true, false, false, true);
			infoCreazioneMap.put(abilitatoId, abiliato);

		}


	}

	@SuppressWarnings("unchecked")
	@Override
	public InfoForm getInfoModifica(UriInfo uriInfo, BasicBD bd, Tributo entry) throws ConsoleException {
		URI modifica = this.getUriModifica(uriInfo, bd);
		InfoForm infoModifica = new InfoForm(modifica);

		String codTributoId = Utils.getInstance().getMessageFromResourceBundle(this.nomeServizio + ".codTributo.id");
		String idDominioId = Utils.getInstance().getMessageFromResourceBundle(this.nomeServizio + ".idDominio.id");
		String idIbanAccreditoId = Utils.getInstance().getMessageFromResourceBundle(this.nomeServizio + ".idIbanAccredito.id");
		String abilitatoId = Utils.getInstance().getMessageFromResourceBundle(this.nomeServizio + ".abilitato.id");
		String tributoId = Utils.getInstance().getMessageFromResourceBundle(this.nomeServizio + ".id.id");
		String descrizioneId = Utils.getInstance().getMessageFromResourceBundle(this.nomeServizio + ".descrizione.id");
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
		sezioneRoot.addField(descrizione);

		InputNumber idDominio = (InputNumber) infoCreazioneMap.get(idDominioId);
		idDominio.setDefaultValue(entry.getIdDominio());
		sezioneRoot.addField(idDominio);

		SelectList<Long> idIbanAccredito  = (SelectList<Long>) infoCreazioneMap.get(idIbanAccreditoId);
		List<Voce<Long>> listaIban = new ArrayList<Voce<Long>>();

		try{
			DominiBD dominiBD = new DominiBD(bd);
			IbanAccreditoBD ibanAccreditoBD = new IbanAccreditoBD(bd);
			IbanAccreditoFilter filterIban = ibanAccreditoBD.newFilter();
			FilterSortWrapper fsw = new FilterSortWrapper();
			fsw.setField(it.govpay.orm.IbanAccredito.model().COD_IBAN);
			fsw.setSortOrder(SortOrder.ASC);
			filterIban.getFilterSortList().add(fsw);
			filterIban.setCodDominio(dominiBD.getDominio(entry.getIdDominio()).getCodDominio());   
			List<it.govpay.bd.model.IbanAccredito> findAll = ibanAccreditoBD.findAll(filterIban);

			if(findAll != null && findAll.size() > 0){
				for (it.govpay.bd.model.IbanAccredito ib : findAll) {
					listaIban.add(new Voce<Long>(ib.getCodIban(), ib.getId()));  
				}
			}

		}catch(Exception e){
			throw new ConsoleException(e);
		}
		idIbanAccredito.setValues(listaIban);
		idIbanAccredito.setDefaultValue(entry.getIdIbanAccredito());
		sezioneRoot.addField(idIbanAccredito);

		SelectList<String> tipoContabilita = (SelectList<String>) infoCreazioneMap.get(tipoContabilitaId);
		tipoContabilita.setDefaultValue(entry.getTipoContabilita().getCodifica());
		sezioneRoot.addField(tipoContabilita);

		InputText codContabilita = (InputText) infoCreazioneMap.get(codContabilitaId);
		codContabilita.setDefaultValue(entry.getCodContabilita());
		sezioneRoot.addField(codContabilita);

		CheckButton abilitato = (CheckButton) infoCreazioneMap.get(abilitatoId);
		abilitato.setDefaultValue(entry.isAbilitato()); 
		sezioneRoot.addField(abilitato);

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
		String methodName = "dettaglio " + this.titoloServizio + "."+ id;

		try{
			this.log.info("Esecuzione " + methodName + " in corso...");
			// Operazione consentita solo all'amministratore
			this.darsService.checkOperatoreAdmin(bd);

			// recupero oggetto
			TributiBD tributiBD = new TributiBD(bd);
			Tributo tributo = tributiBD.getTributo(id);

			InfoForm infoModifica = this.getInfoModifica(uriInfo, bd,tributo);
			URI cancellazione = null;
			URI esportazione = null;

			Dettaglio dettaglio = new Dettaglio(this.getTitolo(tributo), esportazione, cancellazione, infoModifica);

			it.govpay.web.rs.dars.model.Sezione root = dettaglio.getSezioneRoot(); 

			// dati del tributo
			root.addVoce(Utils.getInstance().getMessageFromResourceBundle(this.nomeServizio + ".codTributo.label"), tributo.getCodTributo());
			root.addVoce(Utils.getInstance().getMessageFromResourceBundle(this.nomeServizio + ".descrizione.label"), tributo.getDescrizione());


			DominiBD dominiBD = new DominiBD(bd);
			Dominio dominio = dominiBD.getDominio(tributo.getIdDominio());

			root.addVoce(Utils.getInstance().getMessageFromResourceBundle(this.nomeServizio + ".idDominio.label"), dominio.getCodDominio());

			IbanAccredito ibanAccredito = tributo.getIbanAccredito(bd); 
			root.addVoce(Utils.getInstance().getMessageFromResourceBundle(this.nomeServizio + ".idIbanAccredito.label"), ibanAccredito.getCodIban());

			TipoContabilta tipoContabilita = tributo.getTipoContabilita() != null ? tributo.getTipoContabilita() : TipoContabilta.CAPITOLO;
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
			root.addVoce(Utils.getInstance().getMessageFromResourceBundle(this.nomeServizio + ".codContabilita.label"), tributo.getCodContabilita());
			root.addVoce(Utils.getInstance().getMessageFromResourceBundle(this.nomeServizio + ".abilitato.label"), Utils.getSiNoAsLabel(tributo.isAbilitato()));

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

			Tributo entry = this.creaEntry(is, uriInfo, bd);

			this.checkEntry(entry, null);

			TributiBD tributiBD = new TributiBD(bd);

			try{
				tributiBD.getTributo(entry.getIdDominio(),entry.getCodTributo());
				String msg = Utils.getInstance().getMessageWithParamsFromResourceBundle(this.nomeServizio + ".oggettoEsistente", entry.getCodTributo());
				throw new DuplicatedEntryException(msg);
			}catch(NotFoundException e){}

			tributiBD.insertTributo(entry); 

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
	public Tributo creaEntry(InputStream is, UriInfo uriInfo, BasicBD bd)
			throws WebApplicationException, ConsoleException {
		String methodName = "creaEntry " + this.titoloServizio;
		Tributo entry = null;
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
			jsonConfig.setRootClass(Tributo.class);
			entry = (Tributo) JSONObject.toBean( jsonObject, jsonConfig );
			
			TipoContabilta tipoContabilita =  TipoContabilta.toEnum(tipocontabilitaS);
			entry.setTipoContabilita(tipoContabilita); 
			

			this.log.info("Esecuzione " + methodName + " completata.");
			return entry;
		}catch(WebApplicationException e){
			throw e;
		}catch(Exception e){
			throw new ConsoleException(e);
		}
	}

	@Override
	public void checkEntry(Tributo entry, Tributo oldEntry) throws ValidationException {
		if(entry == null || entry.getCodTributo() == null || entry.getCodTributo().isEmpty()) throw new ValidationException("Il campo Cod Tributo e' obbligatorio");
		if(entry == null || entry.getDescrizione() == null || entry.getDescrizione().isEmpty()) throw new ValidationException("Il campo Descrizione e' obbligatorio");
		if(entry == null || entry.getIdDominio() == 0) throw new ValidationException("Il campo Dominio e' obbligatorio");
		if(entry == null || entry.getIdIbanAccredito() == 0 ) throw new ValidationException("Il campo Iban Accredito e' obbligatorio");
		if(entry == null || entry.getTipoContabilita() == null || entry.getDescrizione().isEmpty()) throw new ValidationException("Il campo Tipo Contabilita' e' obbligatorio");
		if(entry == null || entry.getCodContabilita() == null || entry.getCodContabilita().isEmpty()) throw new ValidationException("Il campo Cod Contabilita e' obbligatorio");

		if(oldEntry != null) {
			if(!entry.getCodTributo().equals(oldEntry.getCodTributo())) throw new ValidationException("Il campo Cod Tributo non e' modificabile");
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

			Tributo entry = this.creaEntry(is, uriInfo, bd);

			TributiBD tributiBD = new TributiBD(bd);
			Tributo oldEntry = tributiBD.getTributo(entry.getIdDominio(),entry.getCodTributo());

			this.checkEntry(entry, oldEntry);

			tributiBD.updateTributo(entry); 

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
	public String getTitolo(Tributo entry) {
		StringBuilder sb = new StringBuilder();

		sb.append( entry.getDescrizione());
		sb.append(" (").append(entry.getCodTributo()).append(")");

		return sb.toString();
	}

	public Elemento getElemento(Tributo entry, Long id, UriBuilder uriDettaglioBuilder, BasicBD bd) throws Exception{
		String titolo = this.getTitolo(entry);

		StringBuilder sb = new StringBuilder();
		DominiBD dominiBD = new DominiBD(bd);

		sb.append(Utils.getAbilitatoAsLabel(entry.isAbilitato()));
		sb.append(", Dominio: ").append(dominiBD.getDominio(entry.getIdDominio()).getCodDominio());

		String sottotitolo = sb.toString();
		URI urlDettaglio = id != null ?  uriDettaglioBuilder.build(id) : null;
		Elemento elemento = new Elemento(id, titolo, sottotitolo, urlDettaglio);
		return elemento;
	}

	@Override
	public String getSottotitolo(Tributo entry) {
		StringBuilder sb = new StringBuilder();

		sb.append(Utils.getAbilitatoAsLabel(entry.isAbilitato()));

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
