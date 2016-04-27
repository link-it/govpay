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
package it.govpay.web.rs.dars.anagrafica.applicazioni;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.net.URI;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.UriBuilder;
import javax.ws.rs.core.UriInfo;

import org.apache.commons.lang.StringUtils;
import org.apache.logging.log4j.Logger;
import org.openspcoop2.generic_project.exception.NotFoundException;
import org.openspcoop2.generic_project.exception.ServiceException;
import org.openspcoop2.generic_project.expression.SortOrder;

import it.govpay.bd.BasicBD;
import it.govpay.bd.FilterSortWrapper;
import it.govpay.bd.anagrafica.ApplicazioniBD;
import it.govpay.bd.anagrafica.OperatoriBD;
import it.govpay.bd.anagrafica.PortaliBD;
import it.govpay.bd.anagrafica.TributiBD;
import it.govpay.bd.anagrafica.filters.ApplicazioneFilter;
import it.govpay.bd.anagrafica.filters.TributoFilter;
import it.govpay.bd.model.Applicazione;
import it.govpay.bd.model.Connettore;
import it.govpay.bd.model.Operatore;
import it.govpay.bd.model.Portale;
import it.govpay.bd.model.Rpt.FirmaRichiesta;
import it.govpay.bd.model.Tributo;
import it.govpay.web.rs.dars.BaseDarsHandler;
import it.govpay.web.rs.dars.BaseDarsService;
import it.govpay.web.rs.dars.IDarsHandler;
import it.govpay.web.rs.dars.anagrafica.applicazioni.input.Tributi;
import it.govpay.web.rs.dars.anagrafica.connettori.ConnettoreHandler;
import it.govpay.web.rs.dars.anagrafica.operatori.Operatori;
import it.govpay.web.rs.dars.anagrafica.portali.Portali;
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
import it.govpay.web.rs.dars.model.input.base.CheckButton;
import it.govpay.web.rs.dars.model.input.base.InputNumber;
import it.govpay.web.rs.dars.model.input.base.InputText;
import it.govpay.web.rs.dars.model.input.base.SelectList;
import it.govpay.web.utils.Utils;
import net.sf.json.JSONObject;
import net.sf.json.JsonConfig;

public class ApplicazioniHandler extends BaseDarsHandler<Applicazione> implements IDarsHandler<Applicazione>{

	private static final String CONNETTORE_VERIFICA = "connettoreVerifica";
	private static final String CONNETTORE_NOTIFICA = "connettoreNotifica"; 
	private static Map<String, ParamField<?>> infoCreazioneMap = null;
	private static Map<String, ParamField<?>> infoRicercaMap = null;

	public ApplicazioniHandler(Logger log, BaseDarsService darsService) {
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

			log.info("Esecuzione " + methodName + " in corso..."); 

			ApplicazioniBD applicazioniBD = new ApplicazioniBD(bd);
			ApplicazioneFilter filter = applicazioniBD.newFilter();
			filter.setOffset(offset);
			filter.setLimit(limit);
			FilterSortWrapper fsw = new FilterSortWrapper();
			fsw.setField(it.govpay.orm.Applicazione.model().COD_APPLICAZIONE);
			fsw.setSortOrder(SortOrder.ASC);
			filter.getFilterSortList().add(fsw);

			Operatori operatoriDars = new Operatori();
			String principalId = Utils.getInstance().getMessageFromResourceBundle(operatoriDars.getNomeServizio() + ".principal.id");
			String principalOperatore = this.getParameter(uriInfo, principalId, String.class); 

			Portali portaliDars = new Portali();
			String codPortaleId = Utils.getInstance().getMessageFromResourceBundle(portaliDars.getNomeServizio() + ".codPortale.id");
			String codPortale = this.getParameter(uriInfo, codPortaleId, String.class);

			boolean entitaSenzaApplicazioni = false;
			if(StringUtils.isNotEmpty(principalOperatore)){
				visualizzaRicerca = false;
				OperatoriBD operatoriBD = new OperatoriBD(bd);
				Operatore operatore = operatoriBD.getOperatore(principalOperatore);
				entitaSenzaApplicazioni = Utils.isEmpty(operatore.getIdApplicazioni());
				filter.setListaIdApplicazioni(operatore.getIdApplicazioni());
			}

			if(StringUtils.isNotEmpty(codPortale)){
				visualizzaRicerca = false;
				PortaliBD portaliBD = new PortaliBD(bd);
				Portale portale = portaliBD.getPortale(codPortale);
				entitaSenzaApplicazioni = Utils.isEmpty(portale.getIdApplicazioni());
				filter.setListaIdApplicazioni(portale.getIdApplicazioni());
			}


			String codApplicazioneId = Utils.getInstance().getMessageFromResourceBundle(this.nomeServizio + ".codApplicazione.id");
			String codApplicazione = this.getParameter(uriInfo, codApplicazioneId, String.class	);
			if(StringUtils.isNotEmpty(codApplicazione)){
				filter.setCodApplicazione(codApplicazione); 
			}

			long count = entitaSenzaApplicazioni ? 0 : applicazioniBD.count(filter);
			
			// visualizza la ricerca solo se i risultati sono > del limit
			visualizzaRicerca = visualizzaRicerca && this.visualizzaRicerca(count, limit);
								
			InfoForm infoRicerca = visualizzaRicerca ? this.getInfoRicerca(uriInfo, bd) : null;

			Elenco elenco = new Elenco(this.titoloServizio, infoRicerca,
					this.getInfoCreazione(uriInfo, bd),
					count, esportazione, cancellazione); 

			UriBuilder uriDettaglioBuilder = uriInfo.getBaseUriBuilder().path(this.pathServizio).path("{id}");

			List<Applicazione> findAll = entitaSenzaApplicazioni ? new ArrayList<Applicazione>() : applicazioniBD.findAll(filter);

			if(findAll != null && findAll.size() > 0){
				for (Applicazione entry : findAll) {
					elenco.getElenco().add(this.getElemento(entry, entry.getId(), uriDettaglioBuilder));
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

	@Override
	public InfoForm getInfoRicerca(UriInfo uriInfo, BasicBD bd) throws ConsoleException {
		URI ricerca = this.getUriRicerca(uriInfo, bd);
		InfoForm infoRicerca = new InfoForm(ricerca);

		String codApplicazioneId = Utils.getInstance().getMessageFromResourceBundle(this.nomeServizio + ".codApplicazione.id");

		if(infoRicercaMap == null){
			initInfoRicerca(uriInfo, bd);

		}
		Sezione sezioneRoot = infoRicerca.getSezioneRoot();

		InputText codApplicazione= (InputText) infoRicercaMap.get(codApplicazioneId);
		codApplicazione.setDefaultValue(null);
		sezioneRoot.addField(codApplicazione);

		return infoRicerca;
	}

	private void initInfoRicerca(UriInfo uriInfo, BasicBD bd) throws ConsoleException{
		if(infoRicercaMap == null){
			infoRicercaMap = new HashMap<String, ParamField<?>>();

			String codApplicazioneId = Utils.getInstance().getMessageFromResourceBundle(this.nomeServizio + ".codApplicazione.id");
			String codApplicazioneLabel = Utils.getInstance().getMessageFromResourceBundle(this.nomeServizio + ".codApplicazione.label");
			InputText codApplicazione = new InputText(codApplicazioneId, codApplicazioneLabel, null, false, false, true, 1, 255);
			infoRicercaMap.put(codApplicazioneId, codApplicazione);
		}
	}

	@SuppressWarnings("unchecked")
	@Override
	public InfoForm getInfoCreazione(UriInfo uriInfo, BasicBD bd) throws ConsoleException {
		URI creazione = this.getUriCreazione(uriInfo, bd);
		InfoForm infoCreazione = new InfoForm(creazione);

		String codApplicazioneId = Utils.getInstance().getMessageFromResourceBundle(this.nomeServizio + ".codApplicazione.id");
		String principalId = Utils.getInstance().getMessageFromResourceBundle(this.nomeServizio + ".principal.id");
		String abilitatoId = Utils.getInstance().getMessageFromResourceBundle(this.nomeServizio + ".abilitato.id");
		String applicazioneId = Utils.getInstance().getMessageFromResourceBundle(this.nomeServizio + ".id.id");
		String firmaRichiestaId = Utils.getInstance().getMessageFromResourceBundle(this.nomeServizio + ".firmaRichiesta.id");
		String tributiId = Utils.getInstance().getMessageFromResourceBundle(this.nomeServizio + ".tributi.id");
		String trustedId = Utils.getInstance().getMessageFromResourceBundle(this.nomeServizio + ".trusted.id");

		ConnettoreHandler connettoreVerificaHandler = new ConnettoreHandler(CONNETTORE_VERIFICA,this.nomeServizio,this.pathServizio);
		List<ParamField<?>> infoCreazioneConnettoreVerifica = connettoreVerificaHandler.getInfoCreazione(uriInfo, bd);

		ConnettoreHandler connettoreNotificaHandler = new ConnettoreHandler(CONNETTORE_NOTIFICA,this.nomeServizio,this.pathServizio);
		List<ParamField<?>> infoCreazioneConnettoreNotifica = connettoreNotificaHandler.getInfoCreazione(uriInfo, bd);

		if(infoCreazioneMap == null){
			initInfoCreazione(uriInfo, bd);
		}

		Sezione sezioneRoot = infoCreazione.getSezioneRoot();
		InputNumber idInterm = (InputNumber) infoCreazioneMap.get(applicazioneId);
		idInterm.setDefaultValue(null);
		sezioneRoot.addField(idInterm);

		InputText codApplicazione = (InputText) infoCreazioneMap.get(codApplicazioneId);
		codApplicazione.setDefaultValue(null);
		codApplicazione.setEditable(true); 
		sezioneRoot.addField(codApplicazione);

		InputText principal = (InputText) infoCreazioneMap.get(principalId);
		principal.setDefaultValue(null);
		sezioneRoot.addField(principal);

		SelectList<String> firmaRichiesta = (SelectList<String>) infoCreazioneMap.get(firmaRichiestaId);
		firmaRichiesta.setDefaultValue(FirmaRichiesta.NESSUNA.getCodifica());
		sezioneRoot.addField(firmaRichiesta);

		CheckButton trusted = (CheckButton) infoCreazioneMap.get(trustedId);
		trusted.setDefaultValue(true); 
		sezioneRoot.addField(trusted);

		Tributi tributi = (Tributi) infoCreazioneMap.get(tributiId);
		TributiBD tributiBD = new TributiBD(bd);
		List<Voce<Long>> idTributi= new ArrayList<Voce<Long>>();

		try {
			TributoFilter filter = tributiBD.newFilter();
			FilterSortWrapper fsw = new FilterSortWrapper();
			fsw.setField(it.govpay.orm.Tributo.model().COD_TRIBUTO);
			fsw.setSortOrder(SortOrder.ASC);
			filter.getFilterSortList().add(fsw);
			List<Tributo> tributiList = tributiBD.findAll(filter);
			if(tributiList != null && tributiList.size() > 0){
				for (Tributo tributo : tributiList) {
					idTributi.add(new Voce<Long>(tributo.getCodTributo(), tributo.getId())); 
				}
			}

		} catch (ServiceException e) {
			throw new ConsoleException(e);
		}

		tributi.setValues(idTributi); 
		tributi.setDefaultValue(new ArrayList<Long>());
		sezioneRoot.addField(tributi);

		CheckButton abilitato = (CheckButton) infoCreazioneMap.get(abilitatoId);
		abilitato.setDefaultValue(true); 
		sezioneRoot.addField(abilitato);

		Sezione sezioneConnettoreVerifica = infoCreazione.addSezione(Utils.getInstance().getMessageFromResourceBundle(this.nomeServizio + "." + CONNETTORE_VERIFICA + ".titolo"));

		for (ParamField<?> par : infoCreazioneConnettoreVerifica) { 
			sezioneConnettoreVerifica.addField(par); 	
		}

		Sezione sezioneConnettoreNotifica = infoCreazione.addSezione(Utils.getInstance().getMessageFromResourceBundle(this.nomeServizio + "." + CONNETTORE_NOTIFICA + ".titolo"));

		for (ParamField<?> par : infoCreazioneConnettoreNotifica) { 
			sezioneConnettoreNotifica.addField(par); 	
		}


		return infoCreazione;
	}

	private void initInfoCreazione(UriInfo uriInfo, BasicBD bd) throws ConsoleException{
		if(infoCreazioneMap == null){
			infoCreazioneMap = new HashMap<String, ParamField<?>>();

			String codApplicazioneId = Utils.getInstance().getMessageFromResourceBundle(this.nomeServizio + ".codApplicazione.id");
			String principalId = Utils.getInstance().getMessageFromResourceBundle(this.nomeServizio + ".principal.id");
			String abilitatoId = Utils.getInstance().getMessageFromResourceBundle(this.nomeServizio + ".abilitato.id");
			String applicazioneId = Utils.getInstance().getMessageFromResourceBundle(this.nomeServizio + ".id.id");
			String firmaRichiestaId = Utils.getInstance().getMessageFromResourceBundle(this.nomeServizio + ".firmaRichiesta.id");
			String tributiId = Utils.getInstance().getMessageFromResourceBundle(this.nomeServizio + ".tributi.id");
			String trustedId = Utils.getInstance().getMessageFromResourceBundle(this.nomeServizio + ".trusted.id");

			// id 
			InputNumber id = new InputNumber(applicazioneId, null, null, true, true, false, 1, 20);
			infoCreazioneMap.put(applicazioneId, id);

			// codApplicazione
			String codApplicazioneLabel = Utils.getInstance().getMessageFromResourceBundle(this.nomeServizio + ".codApplicazione.label");
			InputText codApplicazione = new InputText(codApplicazioneId, codApplicazioneLabel, null, true, false, true, 1, 255);
			codApplicazione.setSuggestion(Utils.getInstance().getMessageFromResourceBundle(this.nomeServizio + ".codApplicazione.suggestion"));
			codApplicazione.setValidation(null, Utils.getInstance().getMessageFromResourceBundle(this.nomeServizio + ".codApplicazione.errorMessage"));
			infoCreazioneMap.put(codApplicazioneId, codApplicazione);

			// principal
			String principalLabel = Utils.getInstance().getMessageFromResourceBundle(this.nomeServizio + ".principal.label");
			InputText principal = new InputText(principalId, principalLabel, null, true, false, true, 1, 255);
			principal.setValidation(null, Utils.getInstance().getMessageFromResourceBundle(this.nomeServizio + ".principal.errorMessage"));
			infoCreazioneMap.put(principalId, principal);

			// abilitato
			String abilitatoLabel = Utils.getInstance().getMessageFromResourceBundle(this.nomeServizio + ".abilitato.label");
			CheckButton abiliato = new CheckButton(abilitatoId, abilitatoLabel, true, false, false, true);
			infoCreazioneMap.put(abilitatoId, abiliato);

			String firmaRichiestaLabel = Utils.getInstance().getMessageFromResourceBundle(this.nomeServizio + ".firmaRichiesta.label");
			List<Voce<String>> valoriFirma = new ArrayList<Voce<String>>(); 
			valoriFirma.add(new Voce<String>(Utils.getInstance().getMessageFromResourceBundle(this.nomeServizio + ".firmaRichiesta.nessuna"), FirmaRichiesta.NESSUNA.getCodifica()));
			valoriFirma.add(new Voce<String>(Utils.getInstance().getMessageFromResourceBundle(this.nomeServizio + ".firmaRichiesta.ca_des"), FirmaRichiesta.CA_DES.getCodifica()));
			valoriFirma.add(new Voce<String>(Utils.getInstance().getMessageFromResourceBundle(this.nomeServizio + ".firmaRichiesta.xa_des"), FirmaRichiesta.XA_DES.getCodifica()));
			valoriFirma.add(new Voce<String>(Utils.getInstance().getMessageFromResourceBundle(this.nomeServizio + ".firmaRichiesta.avanzata"), FirmaRichiesta.AVANZATA.getCodifica()));
			SelectList<String> firmaRichiesta = new SelectList<String>(firmaRichiestaId, firmaRichiestaLabel, null, true, false, true, valoriFirma);
			infoCreazioneMap.put(firmaRichiestaId, firmaRichiesta);

			String tributiLabel = Utils.getInstance().getMessageFromResourceBundle(this.nomeServizio + ".tributi.label");
			List<Voce<Long>> idTributi= new ArrayList<Voce<Long>>();
			Tributi tributi = new Tributi(tributiId, tributiLabel, null, false, false, true, idTributi);
			infoCreazioneMap.put(tributiId, tributi);

			// trusted
			String trustedLabel = Utils.getInstance().getMessageFromResourceBundle(this.nomeServizio + ".trusted.label");
			CheckButton trusted = new CheckButton(trustedId, trustedLabel, true, false, false, true);
			infoCreazioneMap.put(trustedId, trusted);

			ConnettoreHandler connettoreVerificaHandler = new ConnettoreHandler(CONNETTORE_VERIFICA,this.nomeServizio,this.pathServizio);
			List<ParamField<?>> infoCreazioneConnettoreVerifica = connettoreVerificaHandler.getInfoCreazione(uriInfo, bd);

			ConnettoreHandler connettoreNotificaHandler = new ConnettoreHandler(CONNETTORE_NOTIFICA,this.nomeServizio,this.pathServizio);
			List<ParamField<?>> infoCreazioneConnettoreNotifica = connettoreNotificaHandler.getInfoCreazione(uriInfo, bd);

			for (ParamField<?> par : infoCreazioneConnettoreVerifica) { 
				infoCreazioneMap.put(par.getId(),par); 	
			}

			for (ParamField<?> par : infoCreazioneConnettoreNotifica) { 
				infoCreazioneMap.put(par.getId(),par); 	
			}
		}
	}

	@SuppressWarnings("unchecked")
	@Override
	public InfoForm getInfoModifica(UriInfo uriInfo, BasicBD bd, Applicazione entry) throws ConsoleException {
		URI modifica = this.getUriModifica(uriInfo, bd);
		InfoForm infoModifica = new InfoForm(modifica);

		String codApplicazioneId = Utils.getInstance().getMessageFromResourceBundle(this.nomeServizio + ".codApplicazione.id");
		String principalId = Utils.getInstance().getMessageFromResourceBundle(this.nomeServizio + ".principal.id");
		String abilitatoId = Utils.getInstance().getMessageFromResourceBundle(this.nomeServizio + ".abilitato.id");
		String applicazioneId = Utils.getInstance().getMessageFromResourceBundle(this.nomeServizio + ".id.id");
		String firmaRichiestaId = Utils.getInstance().getMessageFromResourceBundle(this.nomeServizio + ".firmaRichiesta.id");
		String tributiId = Utils.getInstance().getMessageFromResourceBundle(this.nomeServizio + ".tributi.id");
		String trustedId = Utils.getInstance().getMessageFromResourceBundle(this.nomeServizio + ".trusted.id");

		ConnettoreHandler connettoreVerificaHandler = new ConnettoreHandler(CONNETTORE_VERIFICA,this.nomeServizio,this.pathServizio);
		List<ParamField<?>> infoModificaConnettoreVerifica = connettoreVerificaHandler.getInfoModifica(uriInfo, bd, entry.getConnettoreVerifica());

		ConnettoreHandler connettoreNotificaHandler = new ConnettoreHandler(CONNETTORE_NOTIFICA,this.nomeServizio,this.pathServizio);
		List<ParamField<?>> infoModificaConnettoreNotifica = connettoreNotificaHandler.getInfoModifica(uriInfo, bd, entry.getConnettoreNotifica());

		if(infoCreazioneMap == null){
			initInfoCreazione(uriInfo, bd);
		}

		Sezione sezioneRoot = infoModifica.getSezioneRoot();
		InputNumber idInterm = (InputNumber) infoCreazioneMap.get(applicazioneId);
		idInterm.setDefaultValue(entry.getId());
		sezioneRoot.addField(idInterm);

		InputText codApplicazione = (InputText) infoCreazioneMap.get(codApplicazioneId);
		codApplicazione.setDefaultValue(entry.getCodApplicazione());
		codApplicazione.setEditable(false); 
		sezioneRoot.addField(codApplicazione);

		InputText principal = (InputText) infoCreazioneMap.get(principalId);
		principal.setDefaultValue(entry.getPrincipal());
		sezioneRoot.addField(principal);

		FirmaRichiesta firmaRichiestaValue = entry.getFirmaRichiesta() != null ? entry.getFirmaRichiesta() : FirmaRichiesta.NESSUNA;
		SelectList<String> firmaRichiesta = (SelectList<String>) infoCreazioneMap.get(firmaRichiestaId);
		firmaRichiesta.setDefaultValue(firmaRichiestaValue.getCodifica());
		sezioneRoot.addField(firmaRichiesta);

		CheckButton trusted = (CheckButton) infoCreazioneMap.get(trustedId);
		trusted.setDefaultValue(true); 
		sezioneRoot.addField(trusted);

		Tributi tributi = (Tributi) infoCreazioneMap.get(tributiId);
		TributiBD tributiBD = new TributiBD(bd);
		List<Voce<Long>> idTributi= new ArrayList<Voce<Long>>();
		try {
			TributoFilter filter = tributiBD.newFilter();
			FilterSortWrapper fsw = new FilterSortWrapper();
			fsw.setField(it.govpay.orm.Tributo.model().COD_TRIBUTO);
			fsw.setSortOrder(SortOrder.ASC);
			filter.getFilterSortList().add(fsw);
			List<Tributo> tributiList = tributiBD.findAll(filter);
			if(tributiList != null && tributiList.size() > 0){
				for (Tributo tributo : tributiList) {
					idTributi.add(new Voce<Long>(tributo.getCodTributo(), tributo.getId())); 
				}
			}

		} catch (ServiceException e) {
			throw new ConsoleException(e);
		}

		tributi.setValues(idTributi); 
		tributi.setDefaultValue(entry.getIdTributi());
		sezioneRoot.addField(tributi);

		CheckButton abilitato = (CheckButton) infoCreazioneMap.get(abilitatoId);
		abilitato.setDefaultValue(entry.isAbilitato()); 
		sezioneRoot.addField(abilitato);

		Sezione sezioneConnettoreVerifica = infoModifica.addSezione(Utils.getInstance().getMessageFromResourceBundle(this.nomeServizio + "." + CONNETTORE_VERIFICA + ".titolo"));

		for (ParamField<?> par : infoModificaConnettoreVerifica) { 
			sezioneConnettoreVerifica.addField(par); 	
		}

		Sezione sezioneConnettoreNotifica = infoModifica.addSezione(Utils.getInstance().getMessageFromResourceBundle(this.nomeServizio + "." + CONNETTORE_NOTIFICA + ".titolo"));

		for (ParamField<?> par : infoModificaConnettoreNotifica) { 
			sezioneConnettoreNotifica.addField(par); 	
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
		String methodName = "dettaglio " + this.titoloServizio + "."+ id;

		try{
			log.info("Esecuzione " + methodName + " in corso...");
			// Operazione consentita solo all'amministratore
			this.darsService.checkOperatoreAdmin(bd);

			// recupero oggetto
			ApplicazioniBD applicazioniBD = new ApplicazioniBD(bd);
			Applicazione applicazione = applicazioniBD.getApplicazione(id);

			InfoForm infoModifica = this.getInfoModifica(uriInfo, bd,applicazione);
			URI cancellazione = null;
			URI esportazione = null;

			Dettaglio dettaglio = new Dettaglio(this.getTitolo(applicazione), esportazione, cancellazione, infoModifica);

			it.govpay.web.rs.dars.model.Sezione root = dettaglio.getSezioneRoot(); 

			// dati dell'intermediario
			root.addVoce(Utils.getInstance().getMessageFromResourceBundle(this.nomeServizio + ".codApplicazione.label"), applicazione.getCodApplicazione());
			root.addVoce(Utils.getInstance().getMessageFromResourceBundle(this.nomeServizio + ".principal.label"), applicazione.getPrincipal());

			FirmaRichiesta firmaRichiestaValue = applicazione.getFirmaRichiesta() != null ? applicazione.getFirmaRichiesta() : FirmaRichiesta.NESSUNA;
			String firmaRichiestaAsString = null;

			switch (firmaRichiestaValue) {
			case AVANZATA:
				firmaRichiestaAsString = Utils.getInstance().getMessageFromResourceBundle(this.nomeServizio + ".firmaRichiesta.avanzata");
				break;
			case CA_DES: 
				firmaRichiestaAsString = Utils.getInstance().getMessageFromResourceBundle(this.nomeServizio + ".firmaRichiesta.ca_des");
				break;
			case XA_DES :
				firmaRichiestaAsString = Utils.getInstance().getMessageFromResourceBundle(this.nomeServizio + ".firmaRichiesta.xa_des");
				break;
			case NESSUNA: 
			default:
				firmaRichiestaAsString = Utils.getInstance().getMessageFromResourceBundle(this.nomeServizio + ".firmaRichiesta.nessuna");
				break;
			}

			root.addVoce(Utils.getInstance().getMessageFromResourceBundle(this.nomeServizio + ".firmaRichiesta.label"), firmaRichiestaAsString);
			root.addVoce(Utils.getInstance().getMessageFromResourceBundle(this.nomeServizio + ".abilitato.label"), Utils.getSiNoAsLabel(applicazione.isAbilitato()));

			// sezione connettore
			Connettore connettoreVerifica = applicazione.getConnettoreVerifica();
			it.govpay.web.rs.dars.model.Sezione sezioneConnettoreVerifica = dettaglio.addSezione(Utils.getInstance().getMessageFromResourceBundle(this.nomeServizio + "." + CONNETTORE_VERIFICA + ".titolo"));
			ConnettoreHandler connettoreVerificaHandler = new ConnettoreHandler(CONNETTORE_VERIFICA,this.nomeServizio,this.pathServizio);
			connettoreVerificaHandler.fillSezione(sezioneConnettoreVerifica, connettoreVerifica);

			// sezione connettore
			Connettore connettoreNotifica = applicazione.getConnettoreNotifica();
			it.govpay.web.rs.dars.model.Sezione sezioneConnettoreNotifica = dettaglio.addSezione(Utils.getInstance().getMessageFromResourceBundle(this.nomeServizio + "." + CONNETTORE_NOTIFICA + ".titolo"));
			ConnettoreHandler connettoreNotificaHandler = new ConnettoreHandler(CONNETTORE_NOTIFICA,this.nomeServizio,this.pathServizio);
			connettoreNotificaHandler.fillSezione(sezioneConnettoreNotifica, connettoreNotifica);

			// Elementi correlati
			String etichettaTributi = Utils.getInstance().getMessageFromResourceBundle(this.nomeServizio + ".elementoCorrelato.tributi.titolo");
			String codApplicazioneId = Utils.getInstance().getMessageFromResourceBundle(this.nomeServizio + ".codApplicazione.id");


			it.govpay.web.rs.dars.anagrafica.tributi.Tributi tributiDars = new it.govpay.web.rs.dars.anagrafica.tributi.Tributi();
			UriBuilder uriBuilder = uriInfo.getBaseUriBuilder().path(tributiDars.getPathServizio()).queryParam(codApplicazioneId, applicazione.getCodApplicazione());
			dettaglio.addElementoCorrelato(etichettaTributi, uriBuilder.build());

			log.info("Esecuzione " + methodName + " completata.");

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
			log.info("Esecuzione " + methodName + " in corso...");
			// Operazione consentita solo all'amministratore
			this.darsService.checkOperatoreAdmin(bd);

			Applicazione entry = this.creaEntry(is, uriInfo, bd);

			this.checkEntry(entry, null);

			ApplicazioniBD applicazioniBD = new ApplicazioniBD(bd);

			try{
				applicazioniBD.getApplicazione(entry.getCodApplicazione());
				String msg = Utils.getInstance().getMessageWithParamsFromResourceBundle(this.nomeServizio + ".oggettoEsistente", entry.getCodApplicazione());
				throw new DuplicatedEntryException(msg);
			}catch(NotFoundException e){}

			applicazioniBD.insertApplicazione(entry); 

			log.info("Esecuzione " + methodName + " completata.");

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

	@SuppressWarnings("unchecked")
	@Override
	public Applicazione creaEntry(InputStream is, UriInfo uriInfo, BasicBD bd)
			throws WebApplicationException, ConsoleException {
		String methodName = "creaEntry " + this.titoloServizio;
		Applicazione entry = null;
		try{
			log.info("Esecuzione " + methodName + " in corso...");
			// Operazione consentita solo all'amministratore
			this.darsService.checkOperatoreAdmin(bd);

			JsonConfig jsonConfig = new JsonConfig();
			ByteArrayOutputStream baos = new ByteArrayOutputStream();
			Utils.copy(is, baos);

			baos.flush();
			baos.close();

			JSONObject jsonObjectApplicazione = JSONObject.fromObject( baos.toString() );  
			jsonConfig.setRootClass(Applicazione.class);
			entry = (Applicazione) JSONObject.toBean( jsonObjectApplicazione, jsonConfig );

			String firmaRichiestaId = Utils.getInstance().getMessageFromResourceBundle(this.nomeServizio + ".firmaRichiesta.id");
			String codFirma = jsonObjectApplicazione.getString(firmaRichiestaId);
			if(codFirma != null){
				FirmaRichiesta enum1 = FirmaRichiesta.toEnum(codFirma);
				entry.setFirmaRichiesta(enum1); 
			}


			String cvPrefix = Utils.getInstance().getMessageFromResourceBundle(this.nomeServizio + "." + CONNETTORE_VERIFICA + ".idPrefix");
			JSONObject jsonObjectCV = new JSONObject();
			String cnPrefix = Utils.getInstance().getMessageFromResourceBundle(this.nomeServizio + "." + CONNETTORE_NOTIFICA + ".idPrefix");
			JSONObject jsonObjectCN = new JSONObject();

			Set<String> keySet = jsonObjectApplicazione.keySet();
			for (String key : keySet) {
				if(key.startsWith(cvPrefix)){
					jsonObjectCV.put(key.substring(key.indexOf(cvPrefix)+cvPrefix.length()), jsonObjectApplicazione.get(key)); 
				}
				if(key.startsWith(cnPrefix)){
					jsonObjectCN.put(key.substring(key.indexOf(cnPrefix)+cnPrefix.length()), jsonObjectApplicazione.get(key));
				}
			}

			jsonConfig.setRootClass(Connettore.class);
			Connettore cv = (Connettore) JSONObject.toBean( jsonObjectCV, jsonConfig );
			entry.setConnettoreVerifica(cv);

			Connettore cn = (Connettore) JSONObject.toBean( jsonObjectCN, jsonConfig );
			entry.setConnettoreNotifica(cn);

			log.info("Esecuzione " + methodName + " completata.");
			return entry;
		}catch(WebApplicationException e){
			throw e;
		}catch(Exception e){
			throw new ConsoleException(e);
		}
	}

	@Override
	public void checkEntry(Applicazione entry, Applicazione oldEntry) throws ValidationException {
		if(entry == null || StringUtils.isEmpty(entry.getCodApplicazione())) {
			throw new ValidationException("il campo Cod Applicazione deve essere valorizzato.");
		}

		if(entry.getPrincipal() == null || entry.getPrincipal().isEmpty()) throw new ValidationException("Il campo Principal deve essere valorizzato.");

		Connettore connettoreNotifica = entry.getConnettoreNotifica();
		ConnettoreHandler connettoreNotificaHandler = new ConnettoreHandler(CONNETTORE_NOTIFICA, titoloServizio, pathServizio);
		connettoreNotificaHandler.valida(connettoreNotifica);

		Connettore connettoreVerifica = entry.getConnettoreVerifica();
		ConnettoreHandler connettoreVerificaHandler = new ConnettoreHandler(CONNETTORE_VERIFICA, titoloServizio, pathServizio);
		connettoreVerificaHandler.valida(connettoreVerifica);

		if(oldEntry != null) { //caso update
			if(!oldEntry.getCodApplicazione().equals(entry.getCodApplicazione()))
				throw new ValidationException("Cod Applicazione non deve cambiare in update. Atteso ["+oldEntry.getCodApplicazione()+"] trovato ["+entry.getCodApplicazione()+"]");
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

			Applicazione entry = this.creaEntry(is, uriInfo, bd);

			ApplicazioniBD applicazioniBD = new ApplicazioniBD(bd);
			Applicazione oldEntry = applicazioniBD.getApplicazione(entry.getCodApplicazione());

			this.checkEntry(entry, oldEntry);

			applicazioniBD.updateApplicazione(entry); 

			log.info("Esecuzione " + methodName + " completata.");
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
	public String getTitolo(Applicazione entry) {
		StringBuilder sb = new StringBuilder();

		sb.append(entry.getCodApplicazione());
		return sb.toString();
	}

	@Override
	public String getSottotitolo(Applicazione entry) {
		StringBuilder sb = new StringBuilder();

		sb.append(Utils.getAbilitatoAsLabel(entry.isAbilitato()));

		return sb.toString();
	}


}
