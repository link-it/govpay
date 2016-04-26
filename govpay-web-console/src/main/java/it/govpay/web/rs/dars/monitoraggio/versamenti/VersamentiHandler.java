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
package it.govpay.web.rs.dars.monitoraggio.versamenti;

import java.io.InputStream;
import java.math.BigDecimal;
import java.net.URI;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.UriBuilder;
import javax.ws.rs.core.UriInfo;

import org.apache.commons.lang.StringUtils;
import org.apache.logging.log4j.Logger;
import org.openspcoop2.generic_project.exception.ServiceException;
import org.openspcoop2.generic_project.expression.SortOrder;

import it.govpay.bd.BasicBD;
import it.govpay.bd.FilterSortWrapper;
import it.govpay.bd.anagrafica.DominiBD;
import it.govpay.bd.anagrafica.UnitaOperativeBD;
import it.govpay.bd.anagrafica.filters.DominioFilter;
import it.govpay.bd.anagrafica.filters.UnitaOperativaFilter;
import it.govpay.bd.model.Anagrafica;
import it.govpay.bd.model.Applicazione;
import it.govpay.bd.model.Dominio;
import it.govpay.bd.model.Operatore;
import it.govpay.bd.model.Operatore.ProfiloOperatore;
import it.govpay.bd.model.UnitaOperativa;
import it.govpay.bd.model.Versamento;
import it.govpay.bd.model.Versamento.StatoVersamento;
import it.govpay.bd.pagamento.VersamentiBD;
import it.govpay.bd.pagamento.filters.VersamentoFilter;
import it.govpay.web.rs.dars.BaseDarsHandler;
import it.govpay.web.rs.dars.BaseDarsService;
import it.govpay.web.rs.dars.IDarsHandler;
import it.govpay.web.rs.dars.anagrafica.anagrafica.AnagraficaHandler;
import it.govpay.web.rs.dars.anagrafica.domini.Domini;
import it.govpay.web.rs.dars.anagrafica.domini.DominiHandler;
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
import it.govpay.web.rs.dars.model.input.base.InputText;
import it.govpay.web.rs.dars.model.input.base.SelectList;
import it.govpay.web.utils.Utils;

public class VersamentiHandler extends BaseDarsHandler<Versamento> implements IDarsHandler<Versamento>{

	public static final String ANAGRAFICA_DEBITORE = "anagrafica";
	private static Map<String, ParamField<?>> infoRicercaMap = null;
	private SimpleDateFormat sdf = new SimpleDateFormat("dd/mm/yyyy HH:mm");  

	public VersamentiHandler(Logger log, BaseDarsService darsService) { 
		super(log, darsService);
	}

	@SuppressWarnings("unused")
	@Override
	public Elenco getElenco(UriInfo uriInfo, BasicBD bd) throws WebApplicationException, ConsoleException {
		String methodName = "getElenco " + this.titoloServizio;
		try{	
			// Operazione consentita agli utenti registrati
			Operatore operatore = this.darsService.getOperatoreByPrincipal(bd); 
			ProfiloOperatore profilo = operatore.getProfilo();
			boolean isAdmin = profilo.equals(ProfiloOperatore.ADMIN);


			Integer offset = this.getOffset(uriInfo);
			//			Integer limit = this.getLimit(uriInfo);
			URI esportazione = null;
			URI cancellazione = null;

			log.info("Esecuzione " + methodName + " in corso..."); 

			VersamentiBD versamentiBD = new VersamentiBD(bd);
			VersamentoFilter filter = versamentiBD.newFilter();
			filter.setOffset(offset);
			//			filter.setLimit(limit);
			FilterSortWrapper fsw = new FilterSortWrapper();
			fsw.setField(it.govpay.orm.Versamento.model().DATA_CREAZIONE);
			fsw.setSortOrder(SortOrder.DESC);
			filter.getFilterSortList().add(fsw);

			String cfDebitoreId = Utils.getInstance().getMessageFromResourceBundle(this.nomeServizio + ".cfDebitore.id");
			String cfDebitore = this.getParameter(uriInfo, cfDebitoreId, String.class);
			if(StringUtils.isNotEmpty(cfDebitore))
				filter.setCodUnivocoDebitore(cfDebitore); 

			String cfVersanteId = Utils.getInstance().getMessageFromResourceBundle(this.nomeServizio + ".cfVersante.id");
			String cfVersante = this.getParameter(uriInfo, cfVersanteId, String.class);

			String idVersamentoId = Utils.getInstance().getMessageFromResourceBundle(this.nomeServizio + ".idVersamento.id");
			String idVersamento = this.getParameter(uriInfo, idVersamentoId, String.class);

			String idDominioId = Utils.getInstance().getMessageFromResourceBundle(this.nomeServizio + ".idDominio.id");
			String idDominio = this.getParameter(uriInfo, idDominioId, String.class);
			if(StringUtils.isNotEmpty(idDominio)){
				long idDom = -1l;
				try{
					idDom = Long.parseLong(idDominio);
				}catch(Exception e){ idDom = -1l;	}
				if(idDom > 0){
					UnitaOperativeBD uoBD = new UnitaOperativeBD(bd);
					UnitaOperativaFilter filtro = uoBD.newFilter();
					filtro.setDominioFilter(idDom);
					List<Long> idUO = new ArrayList<Long>();
					List<UnitaOperativa> findAll = uoBD.findAll(filtro);

					if(findAll != null && findAll.size() > 0){
						for (UnitaOperativa unitaOperativa : findAll) {
							idUO.add(unitaOperativa.getId());
						}
						filter.setIdUo(idUO);	
					}
				}
			}

			String iuvId = Utils.getInstance().getMessageFromResourceBundle(this.nomeServizio + ".iuv.id");
			String iuv = this.getParameter(uriInfo, iuvId, String.class);

			boolean eseguiRicerca = isAdmin;
			// SE l'operatore non e' admin vede solo i versamenti associati alle sue UO ed applicazioni
			if(!isAdmin){
				eseguiRicerca = !Utils.isEmpty(operatore.getIdApplicazioni()) || !Utils.isEmpty(operatore.getIdEnti());
				filter.setIdApplicazioni(operatore.getIdApplicazioni());
				filter.setIdUo(operatore.getIdEnti()); 
			}

			long count = eseguiRicerca ? versamentiBD.count(filter) : 0;

			Elenco elenco = new Elenco(this.titoloServizio, this.getInfoRicerca(uriInfo, bd),
					this.getInfoCreazione(uriInfo, bd),
					count, esportazione, cancellazione); 

			UriBuilder uriDettaglioBuilder = uriInfo.getBaseUriBuilder().path(this.pathServizio).path("{id}");

			List<Versamento> findAll = eseguiRicerca ? versamentiBD.findAll(filter) : new ArrayList<Versamento>(); 

			if(findAll != null && findAll.size() > 0){
				for (Versamento entry : findAll) {
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

	@SuppressWarnings("unchecked")
	@Override
	public InfoForm getInfoRicerca(UriInfo uriInfo, BasicBD bd) throws ConsoleException {
		URI ricerca = this.getUriRicerca(uriInfo, bd);
		InfoForm infoRicerca = new InfoForm(ricerca);

		String cfDebitoreId = Utils.getInstance().getMessageFromResourceBundle(this.nomeServizio + ".cfDebitore.id");
		String cfVersanteId = Utils.getInstance().getMessageFromResourceBundle(this.nomeServizio + ".cfVersante.id");
		String idVersamentoId = Utils.getInstance().getMessageFromResourceBundle(this.nomeServizio + ".idVersamento.id");
		String idDominioId = Utils.getInstance().getMessageFromResourceBundle(this.nomeServizio + ".idDominio.id");
		String iuvId = Utils.getInstance().getMessageFromResourceBundle(this.nomeServizio + ".iuv.id");

		if(infoRicercaMap == null){
			initInfoRicerca(uriInfo, bd);
		}

		Sezione sezioneRoot = infoRicerca.getSezioneRoot();

		InputText cfDebitore = (InputText) infoRicercaMap.get(cfDebitoreId);
		cfDebitore.setDefaultValue(null);
		sezioneRoot.addField(cfDebitore);

		InputText cfVersante = (InputText) infoRicercaMap.get(cfVersanteId);
		cfVersante.setDefaultValue(null);
		sezioneRoot.addField(cfVersante);

		InputText idVersamento = (InputText) infoRicercaMap.get(idVersamentoId);
		idVersamento.setDefaultValue(null);
		sezioneRoot.addField(idVersamento);

		InputText iuv = (InputText) infoRicercaMap.get(iuvId);
		iuv.setDefaultValue(null);
		sezioneRoot.addField(iuv);

		// idDominio
		List<Voce<Long>> domini = new ArrayList<Voce<Long>>();

		DominiBD dominiBD = new DominiBD(bd);
		DominioFilter filter;
		try {
			filter = dominiBD.newFilter();
			FilterSortWrapper fsw = new FilterSortWrapper();
			fsw.setField(it.govpay.orm.Dominio.model().COD_DOMINIO);
			fsw.setSortOrder(SortOrder.ASC);
			filter.getFilterSortList().add(fsw);
			List<Dominio> findAll = dominiBD.findAll(filter );

			Domini dominiDars = new Domini();
			DominiHandler dominiHandler = (DominiHandler) dominiDars.getDarsHandler();

			domini.add(new Voce<Long>(Utils.getInstance().getMessageFromResourceBundle("commons.label.qualsiasi"), -1L));
			if(findAll != null && findAll.size() > 0){
				for (Dominio dominio : findAll) {
					domini.add(new Voce<Long>(dominiHandler.getTitolo(dominio), dominio.getId()));  
				}
			}
		} catch (ServiceException e) {
			throw new ConsoleException(e);
		}
		SelectList<Long> idDominio = (SelectList<Long>) infoRicercaMap.get(idDominioId);
		idDominio.setDefaultValue(-1L);
		idDominio.setValues(domini); 
		sezioneRoot.addField(idDominio);

		return infoRicerca;
	}

	private void initInfoRicerca(UriInfo uriInfo, BasicBD bd) throws ConsoleException{
		if(infoRicercaMap == null){
			infoRicercaMap = new HashMap<String, ParamField<?>>();

			String cfDebitoreId = Utils.getInstance().getMessageFromResourceBundle(this.nomeServizio + ".cfDebitore.id");
			String cfVersanteId = Utils.getInstance().getMessageFromResourceBundle(this.nomeServizio + ".cfVersante.id");
			String idVersamentoId = Utils.getInstance().getMessageFromResourceBundle(this.nomeServizio + ".idVersamento.id");
			String idDominioId = Utils.getInstance().getMessageFromResourceBundle(this.nomeServizio + ".idDominio.id");
			String iuvId = Utils.getInstance().getMessageFromResourceBundle(this.nomeServizio + ".iuv.id");

			// cfDebitore
			String cfDebitoreLabel = Utils.getInstance().getMessageFromResourceBundle(this.nomeServizio + ".cfDebitore.label");
			InputText cfDebitore = new InputText(cfDebitoreId, cfDebitoreLabel, null, false, false, true, 1, 255);
			infoRicercaMap.put(cfDebitoreId, cfDebitore);

			// cfVersante
			String cfVersanteLabel = Utils.getInstance().getMessageFromResourceBundle(this.nomeServizio + ".cfVersante.label");
			InputText cfVersante = new InputText(cfVersanteId, cfVersanteLabel, null, false, false, true, 1, 255);
			cfVersante.setAvanzata(true);
			infoRicercaMap.put(cfVersanteId, cfVersante);

			// Id Versamento
			String idVersamentoLabel = Utils.getInstance().getMessageFromResourceBundle(this.nomeServizio + ".idVersamento.label");
			InputText idVersamento = new InputText(idVersamentoId, idVersamentoLabel, null, false, false, true, 1, 255);
			infoRicercaMap.put(idVersamentoId, idVersamento);

			// iuv
			String iuvLabel = Utils.getInstance().getMessageFromResourceBundle(this.nomeServizio + ".iuv.label");
			InputText iuv = new InputText(iuvId, iuvLabel, null, false, false, true, 1, 255);
			infoRicercaMap.put(iuvId, iuv);			

			List<Voce<Long>> domini = new ArrayList<Voce<Long>>();
			// idDominio
			String idDominioLabel = Utils.getInstance().getMessageFromResourceBundle(this.nomeServizio + ".idDominio.label");
			SelectList<Long> idDominio = new SelectList<Long>(idDominioId, idDominioLabel, null, false, false, true, domini);
			infoRicercaMap.put(idDominioId, idDominio);

		}
	}

	@Override
	public Object getField(UriInfo uriInfo, List<RawParamValue> values, String fieldId, BasicBD bd)
			throws WebApplicationException, ConsoleException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Dettaglio getDettaglio(long id, UriInfo uriInfo, BasicBD bd)
			throws WebApplicationException, ConsoleException {
		String methodName = "dettaglio " + this.titoloServizio + "."+ id;

		try{
			log.info("Esecuzione " + methodName + " in corso...");
			// Operazione consentita agli utenti registrati
			Operatore operatore = this.darsService.getOperatoreByPrincipal(bd); 
			ProfiloOperatore profilo = operatore.getProfilo();
			boolean isAdmin = profilo.equals(ProfiloOperatore.ADMIN);

			boolean eseguiRicerca = isAdmin;
			// SE l'operatore non e' admin vede solo i versamenti associati alle sue UO ed applicazioni
			// controllo se l'operatore ha fatto una richiesta di visualizzazione di un versamento che puo' vedere
			if(!isAdmin){
				eseguiRicerca = !Utils.isEmpty(operatore.getIdApplicazioni()) || !Utils.isEmpty(operatore.getIdEnti());
				VersamentiBD versamentiBD = new VersamentiBD(bd);
				VersamentoFilter filter = versamentiBD.newFilter();
				filter.setIdApplicazioni(operatore.getIdApplicazioni());
				filter.setIdUo(operatore.getIdEnti()); 

				FilterSortWrapper fsw = new FilterSortWrapper();
				fsw.setField(it.govpay.orm.Versamento.model().DATA_CREAZIONE);
				fsw.setSortOrder(SortOrder.DESC);
				filter.getFilterSortList().add(fsw);

				long count = eseguiRicerca ? versamentiBD.count(filter) : 0;
				filter.setIdVersamento(id);

				eseguiRicerca = eseguiRicerca && count > 0;
			}

			// recupero oggetto
			VersamentiBD versamentiBD = new VersamentiBD(bd);
			Versamento versamento = eseguiRicerca ? versamentiBD.getVersamento(id) : null;

			InfoForm infoModifica = null;
			URI cancellazione = null;
			URI esportazione = null;

			String titolo = versamento != null ? this.getTitolo(versamento) : "";
			Dettaglio dettaglio = new Dettaglio(titolo, esportazione, cancellazione, infoModifica);

			it.govpay.web.rs.dars.model.Sezione root = dettaglio.getSezioneRoot();

			if(versamento != null){

				if(StringUtils.isNotEmpty(versamento.getCodVersamentoEnte())) 
					root.addVoce(Utils.getInstance().getMessageFromResourceBundle(this.nomeServizio + ".codVersamentoEnte.label"), versamento.getCodVersamentoEnte());
				// Uo
				UnitaOperativa uo = versamento.getUo(bd);
				if(uo != null)
					root.addVoce(Utils.getInstance().getMessageFromResourceBundle(this.nomeServizio + ".unitaOperativa.label"), uo.getCodUo());  

				// Applicazione
				Applicazione applicazione = versamento.getApplicazione(bd);
				if(applicazione != null)
					root.addVoce(Utils.getInstance().getMessageFromResourceBundle(this.nomeServizio + ".applicazione.label"), applicazione.getCodApplicazione());  
				if(versamento.getStatoVersamento() != null)
					root.addVoce(Utils.getInstance().getMessageFromResourceBundle(this.nomeServizio + ".statoVersamento.label"), versamento.getStatoVersamento().toString());
				if(StringUtils.isNotEmpty(versamento.getDescrizioneStato())) 
					root.addVoce(Utils.getInstance().getMessageFromResourceBundle(this.nomeServizio + ".descrizioneStato.label"), versamento.getDescrizioneStato());
				if(versamento.getImportoTotale() != null)
					root.addVoce(Utils.getInstance().getMessageFromResourceBundle(this.nomeServizio + ".importoTotale.label"), versamento.getImportoTotale().toString()+ " Euro");
				if(versamento.getDataCreazione() != null)
					root.addVoce(Utils.getInstance().getMessageFromResourceBundle(this.nomeServizio + ".dataCreazione.label"), sdf.format(versamento.getDataCreazione()));
				if(versamento.getDataScadenza() != null)
					root.addVoce(Utils.getInstance().getMessageFromResourceBundle(this.nomeServizio + ".dataScadenza.label"), sdf.format(versamento.getDataScadenza()));
				if(versamento.getDataUltimoAggiornamento() != null)
					root.addVoce(Utils.getInstance().getMessageFromResourceBundle(this.nomeServizio + ".dataUltimoAggiornamento.label"), sdf.format(versamento.getDataUltimoAggiornamento()));
				root.addVoce(Utils.getInstance().getMessageFromResourceBundle(this.nomeServizio + ".aggiornabile.label"), Utils.getSiNoAsLabel(versamento.isAggiornabile()));
				if(versamento.getCausaleVersamento() != null)
					root.addVoce(Utils.getInstance().getMessageFromResourceBundle(this.nomeServizio + ".causaleVersamento.label"), versamento.getCausaleVersamento().toString());

				// Sezione Anagrafica Debitore

				Anagrafica anagrafica = versamento.getAnagraficaDebitore(); 
				it.govpay.web.rs.dars.model.Sezione sezioneAnagrafica = dettaglio.addSezione(Utils.getInstance().getMessageFromResourceBundle(this.nomeServizio + "." + ANAGRAFICA_DEBITORE + ".titolo"));
				AnagraficaHandler anagraficaHandler = new AnagraficaHandler(ANAGRAFICA_DEBITORE,this.nomeServizio,this.pathServizio);
				anagraficaHandler.fillSezioneAnagraficaUO(sezioneAnagrafica, anagrafica);

				// Singoli Versamenti
				String etichettaSingoliVersamenti = Utils.getInstance().getMessageFromResourceBundle(this.nomeServizio + ".elementoCorrelato.singoliVersamenti.titolo");
				String versamentoId = Utils.getInstance().getMessageFromResourceBundle(this.nomeServizio + ".idVersamento.id");


				SingoliVersamenti svDars = new SingoliVersamenti();
				UriBuilder uriBuilder = uriInfo.getBaseUriBuilder().path(svDars.getPathServizio()).queryParam(versamentoId, versamento.getId());
				dettaglio.addElementoCorrelato(etichettaSingoliVersamenti, uriBuilder.build());

			}

			log.info("Esecuzione " + methodName + " completata.");

			return dettaglio;
		}catch(WebApplicationException e){
			throw e;
		}catch(Exception e){
			throw new ConsoleException(e);
		}
	}

	@Override
	public String getTitolo(Versamento entry) {
		StringBuilder sb = new StringBuilder();

		BigDecimal importoTotale = entry.getImportoTotale();
		String codVersamentoEnte = entry.getCodVersamentoEnte();

		sb.append("Versamento ").append(codVersamentoEnte).append(" di ").append(importoTotale).append(" Euro");

		return sb.toString();
	}

	@Override
	public String getSottotitolo(Versamento entry) {
		StringBuilder sb = new StringBuilder();

		StatoVersamento statoVersamento = entry.getStatoVersamento();
		Date dataUltimoAggiornamento = entry.getDataUltimoAggiornamento();

		sb.append("Stato ").append(statoVersamento).append(", Data: ").append(sdf.format(dataUltimoAggiornamento)); 

		return sb.toString();
	} 

	/* Creazione/Update non consentiti**/

	@Override
	public InfoForm getInfoCreazione(UriInfo uriInfo, BasicBD bd) throws ConsoleException { return null; }

	@Override
	public InfoForm getInfoModifica(UriInfo uriInfo, BasicBD bd, Versamento entry) throws ConsoleException { return null; }

	@Override
	public void delete(List<Long> idsToDelete, UriInfo uriInfo, BasicBD bd) throws WebApplicationException, ConsoleException {	}

	@Override
	public Versamento creaEntry(InputStream is, UriInfo uriInfo, BasicBD bd) throws WebApplicationException, ConsoleException { return null; }

	@Override
	public Dettaglio insert(InputStream is, UriInfo uriInfo, BasicBD bd) throws WebApplicationException, ConsoleException, ValidationException, DuplicatedEntryException { return null; }

	@Override
	public void checkEntry(Versamento entry, Versamento oldEntry) throws ValidationException { }

	@Override
	public Dettaglio update(InputStream is, UriInfo uriInfo, BasicBD bd) throws WebApplicationException, ConsoleException, ValidationException { return null; }
}
