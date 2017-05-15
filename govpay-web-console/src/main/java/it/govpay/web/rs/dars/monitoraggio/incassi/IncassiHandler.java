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
package it.govpay.web.rs.dars.monitoraggio.incassi;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.math.BigDecimal;
import java.net.URI;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.UriInfo;

import org.apache.commons.lang.StringUtils;
import org.apache.logging.log4j.Logger;
import org.jboss.resteasy.plugins.providers.multipart.MultipartFormDataInput;
import org.openspcoop2.generic_project.exception.ServiceException;
import org.openspcoop2.generic_project.expression.SortOrder;
import org.openspcoop2.utils.csv.Printer;

import it.gov.digitpa.schemas._2011.pagamenti.CtRicevutaTelematica;
import it.gov.digitpa.schemas._2011.pagamenti.revoche.CtEsitoRevoca;
import it.govpay.bd.BasicBD;
import it.govpay.bd.FilterSortWrapper;
import it.govpay.bd.anagrafica.AclBD;
import it.govpay.bd.anagrafica.AnagraficaManager;
import it.govpay.bd.anagrafica.DominiBD;
import it.govpay.bd.anagrafica.filters.DominioFilter;
import it.govpay.bd.exception.VersamentoException;
import it.govpay.bd.model.Dominio;
import it.govpay.bd.model.Incasso;
import it.govpay.bd.model.Rpt;
import it.govpay.bd.model.Rr;
import it.govpay.bd.model.UnitaOperativa;
import it.govpay.bd.model.Versamento;
import it.govpay.bd.pagamento.EventiBD;
import it.govpay.bd.pagamento.IncassiBD;
import it.govpay.bd.pagamento.RptBD;
import it.govpay.bd.pagamento.RrBD;
import it.govpay.bd.pagamento.VersamentiBD;
import it.govpay.bd.pagamento.filters.EventiFilter;
import it.govpay.bd.pagamento.filters.IncassoFilter;
import it.govpay.bd.pagamento.filters.RptFilter;
import it.govpay.bd.pagamento.filters.RrFilter;
import it.govpay.bd.pagamento.filters.VersamentoFilter;
import it.govpay.bd.reportistica.EstrattiContoBD;
import it.govpay.bd.reportistica.filters.EstrattoContoFilter;
import it.govpay.core.utils.CSVUtils;
import it.govpay.core.utils.JaxbUtils;
import it.govpay.core.utils.RtUtils;
import it.govpay.model.Acl;
import it.govpay.model.Acl.Tipo;
import it.govpay.model.Applicazione;
import it.govpay.model.EstrattoConto;
import it.govpay.model.Evento;
import it.govpay.model.Operatore;
import it.govpay.model.Operatore.ProfiloOperatore;
import it.govpay.model.comparator.EstrattoContoComparator;
import it.govpay.stampe.pdf.er.ErPdf;
import it.govpay.stampe.pdf.rt.utils.RicevutaPagamentoUtils;
import it.govpay.web.rs.dars.BaseDarsHandler;
import it.govpay.web.rs.dars.BaseDarsService;
import it.govpay.web.rs.dars.IDarsHandler;
import it.govpay.web.rs.dars.anagrafica.domini.Domini;
import it.govpay.web.rs.dars.anagrafica.domini.DominiHandler;
import it.govpay.web.rs.dars.exception.ConsoleException;
import it.govpay.web.rs.dars.exception.DeleteException;
import it.govpay.web.rs.dars.exception.DuplicatedEntryException;
import it.govpay.web.rs.dars.exception.ValidationException;
import it.govpay.web.rs.dars.model.DarsResponse.EsitoOperazione;
import it.govpay.web.rs.dars.model.Dettaglio;
import it.govpay.web.rs.dars.model.Elemento;
import it.govpay.web.rs.dars.model.Elenco;
import it.govpay.web.rs.dars.model.InfoForm;
import it.govpay.web.rs.dars.model.InfoForm.Sezione;
import it.govpay.web.rs.dars.model.RawParamValue;
import it.govpay.web.rs.dars.model.Voce;
import it.govpay.web.rs.dars.model.input.ParamField;
import it.govpay.web.rs.dars.model.input.base.InputDate;
import it.govpay.web.rs.dars.model.input.base.InputText;
import it.govpay.web.rs.dars.model.input.base.SelectList;
import it.govpay.web.rs.dars.monitoraggio.eventi.Eventi;
import it.govpay.web.rs.dars.monitoraggio.eventi.EventiHandler;
import it.govpay.web.rs.dars.monitoraggio.pagamenti.Pagamenti;
import it.govpay.web.utils.ConsoleProperties;
import it.govpay.web.utils.Utils;

public class IncassiHandler extends BaseDarsHandler<Incasso> implements IDarsHandler<Incasso>{

	public static final String ANAGRAFICA_DEBITORE = "anagrafica";
	private Map<String, ParamField<?>> infoRicercaMap = null;
	private SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm");  

	public IncassiHandler(Logger log, BaseDarsService darsService) { 
		super(log, darsService);
	}

	@Override
	public Elenco getElenco(UriInfo uriInfo, BasicBD bd) throws WebApplicationException, ConsoleException {
		String methodName = "getElenco " + this.titoloServizio;
		try{	
			// Operazione consentita agli utenti registrati
			Operatore operatore = this.darsService.getOperatoreByPrincipal(bd); 
			ProfiloOperatore profilo = operatore.getProfilo();
			boolean isAdmin = profilo.equals(ProfiloOperatore.ADMIN);
			boolean eseguiRicerca = true; // isAdmin;
			// SE l'operatore non e' admin vede solo gli incassi associati ai domini definiti nelle ACL
			boolean iuvNonEsistente = false;

			Integer offset = this.getOffset(uriInfo);
			Integer limit = this.getLimit(uriInfo);
			URI esportazione = this.getUriEsportazione(uriInfo, bd); 

			this.log.info("Esecuzione " + methodName + " in corso..."); 

			IncassiBD incassiBD = new IncassiBD(bd);
			AclBD aclBD = new AclBD(bd);
			List<Acl> aclOperatore = aclBD.getAclOperatore(operatore.getId());
			List<Long> idDomini = new ArrayList<Long>();

			boolean simpleSearch = false;
			String simpleSearchString = this.getParameter(uriInfo, BaseDarsService.SIMPLE_SEARCH_PARAMETER_ID, String.class);
			if(StringUtils.isNotEmpty(simpleSearchString)) {
				simpleSearch = true;
			} 

			IncassoFilter filter = incassiBD.newFilter(simpleSearch);
			filter.setOffset(offset);
			filter.setLimit(limit);
			FilterSortWrapper fsw = new FilterSortWrapper();
			fsw.setField(it.govpay.orm.Versamento.model().DATA_ORA_ULTIMO_AGGIORNAMENTO);
			fsw.setSortOrder(SortOrder.DESC);
			filter.getFilterSortList().add(fsw);

			if(simpleSearch){
				// simplesearch
				filter.setSimpleSearchString(simpleSearchString); 
			}else{
				String dataInizioId = Utils.getInstance(this.getLanguage()).getMessageFromResourceBundle(this.nomeServizio + ".dataInizio.id");

				String dataInizio = this.getParameter(uriInfo, dataInizioId, String.class);
				if(StringUtils.isNotEmpty(dataInizio)){
					filter.setDataInizio(this.convertJsonStringToDate(dataInizio));
				}

				String dataFineId = Utils.getInstance(this.getLanguage()).getMessageFromResourceBundle(this.nomeServizio + ".dataFine.id");
				String dataFine = this.getParameter(uriInfo, dataFineId, String.class);
				if(StringUtils.isNotEmpty(dataFine)){
					filter.setDataFine(this.convertJsonStringToDate(dataFine));
				}

				String trnId = Utils.getInstance(this.getLanguage()).getMessageFromResourceBundle(this.nomeServizio + ".trn.id");
				String trn = this.getParameter(uriInfo, trnId, String.class);
				if(StringUtils.isNotEmpty(trn)) {
					filter.setTrn(trn);
				} 

				String dispositivoId = Utils.getInstance(this.getLanguage()).getMessageFromResourceBundle(this.nomeServizio + ".dispositivo.id");
				String dispositivo = this.getParameter(uriInfo, dispositivoId, String.class);
				if(StringUtils.isNotEmpty(dispositivo)) {
					filter.setDispositivo(dispositivo);
				}

				String causaleId = Utils.getInstance(this.getLanguage()).getMessageFromResourceBundle(this.nomeServizio + ".causale.id");
				String causale = this.getParameter(uriInfo, causaleId, String.class);
				if(StringUtils.isNotEmpty(causale)) {
					filter.setCausale(dispositivo);
				}

				String idDominioId = Utils.getInstance(this.getLanguage()).getMessageFromResourceBundle(this.nomeServizio + ".idDominio.id");
				String idDominio = this.getParameter(uriInfo, idDominioId, String.class);
				if(StringUtils.isNotEmpty(idDominio)){
					long idDom = -1l;
					try{
						idDom = Long.parseLong(idDominio);
					}catch(Exception e){ idDom = -1l;	}
					if(idDom > 0){
						idDomini.add(idDom);
						filter.setIdDomini(idDomini);
					}
				}
			}

			if(!isAdmin && idDomini.isEmpty()){
				boolean vediTuttiDomini = false;

				for(Acl acl: aclOperatore) {
					if(Tipo.DOMINIO.equals(acl.getTipo())) {
						if(acl.getIdDominio() == null) {
							vediTuttiDomini = true;
							break;
						} else {
							idDomini.add(acl.getIdDominio());
						}
					}
				}
				if(!vediTuttiDomini) {
					if(idDomini.isEmpty()) {
						eseguiRicerca = false;
					} else {
						filter.setIdDomini(idDomini);
					}
				}
			}

			eseguiRicerca = eseguiRicerca && !iuvNonEsistente;

			long count = eseguiRicerca ? incassiBD.count(filter) : 0;

			// visualizza la ricerca solo se i risultati sono > del limit
			boolean visualizzaRicerca = this.visualizzaRicerca(count, limit);
			InfoForm infoRicerca = this.getInfoRicerca(uriInfo, bd, visualizzaRicerca);

			// Indico la visualizzazione custom
			String formatter = Utils.getInstance(this.getLanguage()).getMessageFromResourceBundle(this.nomeServizio+".elenco.formatter");
			String simpleSearchPlaceholder = Utils.getInstance(this.getLanguage()).getMessageFromResourceBundle(this.nomeServizio+".simpleSearch.placeholder");
			Elenco elenco = new Elenco(this.titoloServizio, infoRicerca,
					this.getInfoCreazione(uriInfo, bd),
					count, esportazione, this.getInfoCancellazione(uriInfo, bd),simpleSearchPlaceholder); 

			List<Incasso> findAll = eseguiRicerca ? incassiBD.findAll(filter) : new ArrayList<Incasso>(); 

			if(findAll != null && findAll.size() > 0){
				for (Incasso entry : findAll) {
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
			String dataInizioId = Utils.getInstance(this.getLanguage()).getMessageFromResourceBundle(this.nomeServizio + ".dataInizio.id");
			String dataFineId = Utils.getInstance(this.getLanguage()).getMessageFromResourceBundle(this.nomeServizio + ".dataFine.id");
			String trnId = Utils.getInstance(this.getLanguage()).getMessageFromResourceBundle(this.nomeServizio + ".trn.id");
			String dispositivoId = Utils.getInstance(this.getLanguage()).getMessageFromResourceBundle(this.nomeServizio + ".dispositivo.id");
			String causaleId = Utils.getInstance(this.getLanguage()).getMessageFromResourceBundle(this.nomeServizio + ".causale.id");
			String idDominioId = Utils.getInstance(this.getLanguage()).getMessageFromResourceBundle(this.nomeServizio + ".idDominio.id");

			if(this.infoRicercaMap == null){
				this.initInfoRicerca(uriInfo, bd);
			}

			Sezione sezioneRoot = infoRicerca.getSezioneRoot();

			try{

				Operatore operatore = this.darsService.getOperatoreByPrincipal(bd); 
				ProfiloOperatore profilo = operatore.getProfilo();
				boolean isAdmin = profilo.equals(ProfiloOperatore.ADMIN);

				// idDominio
				List<Voce<Long>> domini = new ArrayList<Voce<Long>>();

				DominiBD dominiBD = new DominiBD(bd);
				DominioFilter filter;
				try {
					filter = dominiBD.newFilter();
					boolean eseguiRicerca = true;
					if(isAdmin){

					} else {
						AclBD aclBD = new AclBD(bd);
						List<Acl> aclOperatore = aclBD.getAclOperatore(operatore.getId());

						boolean vediTuttiDomini = false;
						List<Long> idDomini = new ArrayList<Long>();
						for(Acl acl: aclOperatore) {
							if(Tipo.DOMINIO.equals(acl.getTipo())) {
								if(acl.getIdDominio() == null) {
									vediTuttiDomini = true;
									break;
								} else {
									idDomini.add(acl.getIdDominio());
								}
							}
						}
						if(!vediTuttiDomini) {
							if(idDomini.isEmpty()) {
								eseguiRicerca = false;
							} else {
								filter.setIdDomini(idDomini);
							}
						}
					}



					if(eseguiRicerca) {
						domini.add(new Voce<Long>(Utils.getInstance(this.getLanguage()).getMessageFromResourceBundle("commons.label.qualsiasi"), -1L));
						FilterSortWrapper fsw = new FilterSortWrapper();
						fsw.setField(it.govpay.orm.Dominio.model().COD_DOMINIO);
						fsw.setSortOrder(SortOrder.ASC);
						filter.getFilterSortList().add(fsw);
						List<Dominio> findAll = dominiBD.findAll(filter );

						Domini dominiDars = new Domini();
						DominiHandler dominiHandler = (DominiHandler) dominiDars.getDarsHandler();

						if(findAll != null && findAll.size() > 0){
							for (Dominio dominio : findAll) {
								domini.add(new Voce<Long>(dominiHandler.getTitolo(dominio,bd), dominio.getId()));  
							}
						}
					}else {
						domini.add(new Voce<Long>(Utils.getInstance(this.getLanguage()).getMessageFromResourceBundle("commons.label.qualsiasi"), -1L));
					}
				} catch (ServiceException e) {
					throw new ConsoleException(e);
				}
				SelectList<Long> idDominio = (SelectList<Long>) this.infoRicercaMap.get(idDominioId);
				idDominio.setDefaultValue(-1L);
				idDominio.setValues(domini); 
				sezioneRoot.addField(idDominio);

			}catch(Exception e){
				throw new ConsoleException(e);
			}

			/// datainizio
			InputDate dataInizio = (InputDate) this.infoRicercaMap.get(dataInizioId);
			dataInizio.setDefaultValue(null);
			sezioneRoot.addField(dataInizio);

			/// dataFine
			InputDate dataFine = (InputDate) this.infoRicercaMap.get(dataFineId);
			dataFine.setDefaultValue(null);
			sezioneRoot.addField(dataFine);	

			InputText trn = (InputText) this.infoRicercaMap.get(trnId);
			trn.setDefaultValue(null);
			sezioneRoot.addField(trn);

			InputText causale = (InputText) this.infoRicercaMap.get(causaleId);
			causale.setDefaultValue(null);
			sezioneRoot.addField(causale);

			InputText dispositivo = (InputText) this.infoRicercaMap.get(dispositivoId);
			dispositivo.setDefaultValue(null);
			sezioneRoot.addField(dispositivo);

		}
		return infoRicerca;
	}

	private void initInfoRicerca(UriInfo uriInfo, BasicBD bd) throws ConsoleException{
		if(this.infoRicercaMap == null){
			this.infoRicercaMap = new HashMap<String, ParamField<?>>();

			String dataInizioId = Utils.getInstance(this.getLanguage()).getMessageFromResourceBundle(this.nomeServizio + ".dataInizio.id");
			String dataFineId = Utils.getInstance(this.getLanguage()).getMessageFromResourceBundle(this.nomeServizio + ".dataFine.id");
			String trnId = Utils.getInstance(this.getLanguage()).getMessageFromResourceBundle(this.nomeServizio + ".trn.id");
			String dispositivoId = Utils.getInstance(this.getLanguage()).getMessageFromResourceBundle(this.nomeServizio + ".dispositivo.id");
			String causaleId = Utils.getInstance(this.getLanguage()).getMessageFromResourceBundle(this.nomeServizio + ".causale.id");
			String idDominioId = Utils.getInstance(this.getLanguage()).getMessageFromResourceBundle(this.nomeServizio + ".idDominio.id");

			// trn
			String trnLabel = Utils.getInstance(this.getLanguage()).getMessageFromResourceBundle(this.nomeServizio + ".trn.label");
			InputText trn = new InputText(trnId, trnLabel, null, false, false, true, 1, 35);
			this.infoRicercaMap.put(trnId, trn);

			// causale
			String causaleLabel = Utils.getInstance(this.getLanguage()).getMessageFromResourceBundle(this.nomeServizio + ".causale.label");
			InputText causale = new InputText(causaleId, causaleLabel, null, false, false, true, 1, 35);
			this.infoRicercaMap.put(causaleId, causale);

			// dispsitivo
			String dispositivoLabel = Utils.getInstance(this.getLanguage()).getMessageFromResourceBundle(this.nomeServizio + ".dispositivo.label");
			InputText dispositivo = new InputText(dispositivoId, dispositivoLabel, null, false, false, true, 1, 35);
			this.infoRicercaMap.put(dispositivoId, dispositivo);			

			List<Voce<Long>> domini = new ArrayList<Voce<Long>>();
			// idDominio
			String idDominioLabel = Utils.getInstance(this.getLanguage()).getMessageFromResourceBundle(this.nomeServizio + ".idDominio.label");
			SelectList<Long> idDominio = new SelectList<Long>(idDominioId, idDominioLabel, null, false, false, true, domini);
			this.infoRicercaMap.put(idDominioId, idDominio);

			// dataInizio
			String dataInizioLabel = Utils.getInstance(this.getLanguage()).getMessageFromResourceBundle(this.nomeServizio + ".dataInizio.label");
			InputDate dataInizio = new InputDate(dataInizioId, dataInizioLabel, null, false, false, true, null, null);
			this.infoRicercaMap.put(dataInizioId, dataInizio);

			// dataFine
			String dataFineLabel = Utils.getInstance(this.getLanguage()).getMessageFromResourceBundle(this.nomeServizio + ".dataFine.label");
			InputDate dataFine = new InputDate(dataFineId, dataFineLabel, null, false, false, true, null, null);
			this.infoRicercaMap.put(dataFineId, dataFine);

		}
	}

	@Override
	public Object getField(UriInfo uriInfo, List<RawParamValue> values, String fieldId, BasicBD bd) throws WebApplicationException, ConsoleException {
		return null;
	}

	@Override
	public Dettaglio getDettaglio(long id, UriInfo uriInfo, BasicBD bd)
			throws WebApplicationException, ConsoleException {
		String methodName = "dettaglio " + this.titoloServizio + "."+ id;

		try{
			this.log.info("Esecuzione " + methodName + " in corso...");
			// Operazione consentita agli utenti registrati
			Operatore operatore = this.darsService.getOperatoreByPrincipal(bd); 
			ProfiloOperatore profilo = operatore.getProfilo();
			boolean isAdmin = profilo.equals(ProfiloOperatore.ADMIN);

			IncassiBD incassiBD = new IncassiBD(bd);
			boolean eseguiRicerca = true; //isAdmin;
			// SE l'operatore non e' admin vede solo i versamenti associati alle sue UO ed applicazioni
			// controllo se l'operatore ha fatto una richiesta di visualizzazione di un versamento che puo' vedere
			if(!isAdmin){
				//				eseguiRicerca = !Utils.isEmpty(operatore.getIdApplicazioni()) || !Utils.isEmpty(operatore.getIdEnti());
				IncassoFilter filter = incassiBD.newFilter();
				//				filter.setIdApplicazioni(operatore.getIdApplicazioni());
				//				filter.setIdUo(operatore.getIdEnti()); 

				FilterSortWrapper fsw = new FilterSortWrapper();
				fsw.setField(it.govpay.orm.Versamento.model().DATA_CREAZIONE);
				fsw.setSortOrder(SortOrder.DESC);
				filter.getFilterSortList().add(fsw);

				long count = eseguiRicerca ? incassiBD.count(filter) : 0;
				List<Long> idIncassoL = new ArrayList<Long>();
				idIncassoL.add(id);
				filter.setIdIncasso(idIncassoL);

				eseguiRicerca = eseguiRicerca && count > 0;
			}

			// recupero oggetto
			Incasso incasso = eseguiRicerca ? incassiBD.getIncasso(id) : null;

			InfoForm infoModifica = null;
			InfoForm infoCancellazione = null;
			URI esportazione = this.getUriEsportazioneDettaglio(uriInfo, incassiBD, id);

			String titolo = Utils.getInstance(this.getLanguage()).getMessageFromResourceBundle(this.nomeServizio + ".dettaglioIncasso") ;
			Dettaglio dettaglio = new Dettaglio(titolo, esportazione, infoCancellazione, infoModifica);

			it.govpay.web.rs.dars.model.Sezione root = dettaglio.getSezioneRoot();

			if(incasso != null){

				if(StringUtils.isNotEmpty(incasso.getTrn())) {
					root.addVoce(Utils.getInstance(this.getLanguage()).getMessageFromResourceBundle(this.nomeServizio + ".trn.label"), incasso.getTrn());
				}
				// Uo
				if(StringUtils.isNotEmpty(incasso.getCodDominio())){
					try{
						Dominio dominio = incasso.getDominio(bd);
						Domini dominiDars = new Domini();
						Elemento elemento = ((DominiHandler)dominiDars.getDarsHandler()).getElemento(dominio, dominio.getId(), dominiDars.getPathServizio(), bd);
						root.addVoce(Utils.getInstance(this.getLanguage()).getMessageFromResourceBundle(this.nomeServizio + ".idDominio.label"), elemento.getTitolo()); 
					}catch(Exception e){
						root.addVoce(Utils.getInstance(this.getLanguage()).getMessageFromResourceBundle(this.nomeServizio + ".idDominio.label"), incasso.getCodDominio());
					}
				}

				// Applicazione
				Applicazione applicazione = incasso.getApplicazione(bd);
				if(applicazione != null) {
					root.addVoce(Utils.getInstance(this.getLanguage()).getMessageFromResourceBundle(this.nomeServizio + ".applicazione.label"), applicazione.getCodApplicazione());
				} 

				if(incasso.getImporto() != null) {
					root.addVoce(Utils.getInstance(this.getLanguage()).getMessageFromResourceBundle(this.nomeServizio + ".importo.label"), incasso.getImporto().toString()+ "€");
				}
				//				if(incasso.getDataCreazione() != null) {
				//					root.addVoce(Utils.getInstance(this.getLanguage()).getMessageFromResourceBundle(this.nomeServizio + ".dataCreazione.label"), this.sdf.format(incasso.getDataCreazione()));
				//				}
				//				if(incasso.getDataScadenza() != null) {
				//					root.addVoce(Utils.getInstance(this.getLanguage()).getMessageFromResourceBundle(this.nomeServizio + ".dataScadenza.label"), this.sdf.format(incasso.getDataScadenza()));
				//				}
				//				if(incasso.getDataUltimoAggiornamento() != null) {
				//					root.addVoce(Utils.getInstance(this.getLanguage()).getMessageFromResourceBundle(this.nomeServizio + ".dataUltimoAggiornamento.label"), this.sdf.format(incasso.getDataUltimoAggiornamento()));
				//				}

				if(incasso.getCausale() != null) {
					root.addVoce(Utils.getInstance(this.getLanguage()).getMessageFromResourceBundle(this.nomeServizio + ".causale.label"), incasso.getCausale().toString());
				}

				if(incasso.getDispositivo() != null) {
					root.addVoce(Utils.getInstance(this.getLanguage()).getMessageFromResourceBundle(this.nomeServizio + ".dispositivo.label"), incasso.getDispositivo().toString());
				}

				Pagamenti pagamentiDars = new Pagamenti();
				String incassoId = Utils.getInstance(this.getLanguage()).getMessageFromResourceBundle(pagamentiDars.getNomeServizio() + ".idIncasso.id");
				String etichettaPagamenti = Utils.getInstance(this.getLanguage()).getMessageFromResourceBundle(this.nomeServizio + ".elementoCorrelato.pagamenti.titolo");
				Map<String, String> params = new HashMap<String, String>();
				params.put(incassoId, incasso.getId()+ "");
				URI pagamentoDettaglio = Utils.creaUriConParametri(pagamentiDars.getPathServizio(), params );
				dettaglio.addElementoCorrelato(etichettaPagamenti, pagamentoDettaglio); 

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
	public String getTitolo(Incasso entry,BasicBD bd) {
		StringBuilder sb = new StringBuilder();

		String dispositivo = entry.getDispositivo();
		Date dataValuta = entry.getDataValuta();

		Utils.getInstance(this.getLanguage()).getMessageWithParamsFromResourceBundle(this.nomeServizio + ".label.titolo", dispositivo,this.sdf.format(dataValuta));

		return sb.toString();
	}

	@Override
	public String getSottotitolo(Incasso entry,BasicBD bd) {
		StringBuilder sb = new StringBuilder();

		String causale = entry.getCausale();
		BigDecimal importo = entry.getImporto() != null ? entry.getImporto() : BigDecimal.ZERO;
		sb.append(
				Utils.getInstance(this.getLanguage()).getMessageWithParamsFromResourceBundle(this.nomeServizio + ".label.sottotitolo", causale, importo.toString()+ "€"));

		return sb.toString();
	} 

	@Override
	public List<String> getValori(Incasso entry, BasicBD bd) throws ConsoleException {
		return null;
	}

	@Override
	public Map<String, Voce<String>> getVoci(Incasso entry, BasicBD bd) throws ConsoleException {
		Map<String, Voce<String>> voci = new HashMap<String, Voce<String>>();
	
		// voci da inserire nella visualizzazione personalizzata 
		// stato, dispositivo, causale, importo, datavaluta, dataacquisizione
		if(StringUtils.isNotEmpty(entry.getCausale())) {
			voci.put(Utils.getInstance(this.getLanguage()).getMessageFromResourceBundle(this.nomeServizio + ".causale.id"),
					new Voce<String>(Utils.getInstance(this.getLanguage()).getMessageFromResourceBundle(this.nomeServizio + ".causale.label"), entry.getCausale()));
		}

		if(entry.getDataAcquisizione() != null) {
			voci.put(Utils.getInstance(this.getLanguage()).getMessageFromResourceBundle(this.nomeServizio + ".dataAcquisizione.id"),
					new Voce<String>(Utils.getInstance(this.getLanguage()).getMessageFromResourceBundle(this.nomeServizio + ".dataAcquisizione.label"), this.sdf.format(entry.getDataAcquisizione())));
		}
		
		if(entry.getDataValuta() != null) {
			voci.put(Utils.getInstance(this.getLanguage()).getMessageFromResourceBundle(this.nomeServizio + ".dataValuta.id"),
					new Voce<String>(Utils.getInstance(this.getLanguage()).getMessageFromResourceBundle(this.nomeServizio + ".dataValuta.label"), this.sdf.format(entry.getDataValuta())));
		}

		if(StringUtils.isNotEmpty(entry.getDispositivo())) {
			voci.put(Utils.getInstance(this.getLanguage()).getMessageFromResourceBundle(this.nomeServizio + ".dispositivo.id"),
					new Voce<String>(Utils.getInstance(this.getLanguage()).getMessageFromResourceBundle(this.nomeServizio + ".dispositivo.label"), entry.getDispositivo()));
		}

		voci.put(Utils.getInstance(this.getLanguage()).getMessageFromResourceBundle(this.nomeServizio + ".stato.id"),
				new Voce<String>(Utils.getInstance(this.getLanguage()).getMessageFromResourceBundle(this.nomeServizio + ".stato.ok"),
						"ok"));

		if(entry.getImporto() != null) {
			voci.put(Utils.getInstance(this.getLanguage()).getMessageFromResourceBundle(this.nomeServizio + ".importo.id"),
					new Voce<String>(Utils.getInstance(this.getLanguage()).getMessageFromResourceBundle(this.nomeServizio + ".importo.label"), entry.getImporto().toString()+ "€"));
		}

		return voci; 
	}

	@Override
	public String esporta(List<Long> idsToExport, UriInfo uriInfo, BasicBD bd, ZipOutputStream zout)
			throws WebApplicationException, ConsoleException {
		StringBuffer sb = new StringBuffer();
		if(idsToExport != null && idsToExport.size() > 0) {
			for (Long long1 : idsToExport) {

				if(sb.length() > 0) {
					sb.append(", ");
				}

				sb.append(long1);
			}
		}

		Printer printer  = null;
		String methodName = "esporta " + this.titoloServizio + "[" + sb.toString() + "]";
		int numeroZipEntries = 0;
		String pathLoghi = ConsoleProperties.getInstance().getPathEstrattoContoPdfLoghi();

		if(idsToExport.size() == 1) {
			return this.esporta(idsToExport.get(0), uriInfo, bd, zout);
		} 

		String fileName = "Export.zip";
		try{
			this.log.info("Esecuzione " + methodName + " in corso...");
			Operatore operatore = this.darsService.getOperatoreByPrincipal(bd); 
			ProfiloOperatore profilo = operatore.getProfilo();
			boolean isAdmin = profilo.equals(ProfiloOperatore.ADMIN);
			boolean eseguiRicerca = true;

			VersamentiBD versamentiBD = new VersamentiBD(bd);
			RptBD rptBD = new RptBD(bd);
			EventiBD eventiBd = new EventiBD(bd);
			Eventi eventiDars = new Eventi();
			it.govpay.core.business.EstrattoConto estrattoContoBD = new it.govpay.core.business.EstrattoConto(bd);
			EventiHandler eventiDarsHandler = (EventiHandler) eventiDars.getDarsHandler(); 

			Map<String, List<Long>> mappaInputEstrattoConto = new HashMap<String, List<Long>>();
			Map<String, Dominio> mappaInputDomini = new HashMap<String, Dominio>();

			VersamentoFilter filter = versamentiBD.newFilter();
			List<Long> ids = new ArrayList<Long>();
			ids = idsToExport;

			if(!isAdmin){

				AclBD aclBD = new AclBD(bd);
				List<Acl> aclOperatore = aclBD.getAclOperatore(operatore.getId());

				boolean vediTuttiDomini = false;
				List<Long> idDomini = new ArrayList<Long>();
				for(Acl acl: aclOperatore) {
					if(Tipo.DOMINIO.equals(acl.getTipo())) {
						if(acl.getIdDominio() == null) {
							vediTuttiDomini = true;
							break;
						} else {
							idDomini.add(acl.getIdDominio());
						}
					}
				}
				if(!vediTuttiDomini) {
					if(idDomini.isEmpty()) {
						eseguiRicerca = false;
					} else {
						filter.setIdDomini(idDomini);
					}
				}

				// l'operatore puo' vedere i domini associati, controllo se c'e' un versamento con Id nei domini concessi.
				if(eseguiRicerca){
					filter.setIdVersamento(ids);
					eseguiRicerca = eseguiRicerca && versamentiBD.count(filter) > 0;
				}
			}

			if(eseguiRicerca){
				for (Long idVersamento : idsToExport) {
					Versamento versamento = versamentiBD.getVersamento(idVersamento);

					// Prelevo il dominio
					UnitaOperativa uo  = AnagraficaManager.getUnitaOperativa(bd, versamento.getIdUo());
					Dominio dominio  = AnagraficaManager.getDominio(bd, uo.getIdDominio());

					String dirDominio = dominio.getCodDominio();

					// Aggrego i versamenti per dominio per generare gli estratti conto
					List<Long> idVersamentiDominio = null;
					if(mappaInputEstrattoConto.containsKey(dominio.getCodDominio())) {
						idVersamentiDominio = mappaInputEstrattoConto.get(dominio.getCodDominio());
					} else{
						idVersamentiDominio = new ArrayList<Long>();
						mappaInputEstrattoConto.put(dominio.getCodDominio(), idVersamentiDominio);
						mappaInputDomini.put(dominio.getCodDominio(), dominio);
					}
					idVersamentiDominio.add(idVersamento);

					String dirVersamento = dirDominio + "/" + versamento.getCodVersamentoEnte();

					RptFilter rptFilter = rptBD.newFilter();
					FilterSortWrapper rptFsw = new FilterSortWrapper();
					rptFsw.setField(it.govpay.orm.RPT.model().DATA_MSG_RICHIESTA);
					rptFsw.setSortOrder(SortOrder.DESC);
					rptFilter.getFilterSortList().add(rptFsw);
					rptFilter.setIdVersamento(idVersamento); 

					RrBD rrBD = new RrBD(bd);
					FilterSortWrapper rrFsw = new FilterSortWrapper();
					rrFsw.setField(it.govpay.orm.RR.model().DATA_MSG_REVOCA);
					rrFsw.setSortOrder(SortOrder.DESC);

					List<Rpt> listaRpt = rptBD.findAll(rptFilter);
					if(listaRpt != null && listaRpt.size() >0 ) {
						for (Rpt rpt : listaRpt) {
							numeroZipEntries ++;

							String iuv = rpt.getIuv();
							String ccp = rpt.getCcp();

							String iuvCcpDir = dirVersamento + "/" + iuv;

							// non appendo il ccp nel caso sia uguale ad 'n/a' altrimenti crea un nuovo livello di directory;
							if(!StringUtils.equalsIgnoreCase(ccp, "n/a")) {
								iuvCcpDir = iuvCcpDir  + "_" + ccp;
							}

							String rptEntryName = iuvCcpDir + "/rpt_" + rpt.getCodMsgRichiesta() + ".xml"; 


							ZipEntry rptXml = new ZipEntry(rptEntryName);
							zout.putNextEntry(rptXml);
							zout.write(rpt.getXmlRpt());
							zout.closeEntry();

							if(rpt.getXmlRt() != null){
								numeroZipEntries ++;
								String rtEntryName = iuvCcpDir + "/rt_" + rpt.getCodMsgRichiesta() + ".xml";
								ZipEntry rtXml = new ZipEntry(rtEntryName);
								zout.putNextEntry(rtXml);
								zout.write(rpt.getXmlRt());
								zout.closeEntry();

								// RT in formato pdf
								String tipoFirma = rpt.getFirmaRichiesta().getCodifica();
								byte[] rtByteValidato = RtUtils.validaFirma(tipoFirma, rpt.getXmlRt(), dominio.getCodDominio());
								CtRicevutaTelematica rt = JaxbUtils.toRT(rtByteValidato);
								ByteArrayOutputStream baos = new ByteArrayOutputStream();
								String auxDigit = dominio.getAuxDigit() + "";
								String applicationCode = String.format("%02d", dominio.getStazione(bd).getApplicationCode());
								RicevutaPagamentoUtils.getPdfRicevutaPagamento(pathLoghi, rt, versamento, auxDigit, applicationCode, baos, this.log);
								String rtPdfEntryName = iuvCcpDir + "/ricevuta_pagamento.pdf";
								numeroZipEntries ++;
								ZipEntry rtPdf = new ZipEntry(rtPdfEntryName);
								zout.putNextEntry(rtPdf);
								zout.write(baos.toByteArray());
								zout.closeEntry();
							}

							// Eventi
							String entryEventiCSV =  iuvCcpDir + "/eventi.csv";

							EventiFilter eventiFilter = eventiBd.newFilter();
							eventiFilter.setCodDominio(dominio.getCodDominio());
							eventiFilter.setIuv(iuv);
							eventiFilter.setCcp(ccp);
							FilterSortWrapper fsw = new FilterSortWrapper();
							fsw.setField(it.govpay.orm.Evento.model().DATA_1);
							fsw.setSortOrder(SortOrder.ASC);
							eventiFilter.getFilterSortList().add(fsw);

							List<Evento> findAllEventi = eventiBd.findAll(eventiFilter);
							ByteArrayOutputStream baos = new ByteArrayOutputStream();
							eventiDarsHandler.scriviCSVEventi(baos, findAllEventi);

							ZipEntry eventiCSV = new ZipEntry(entryEventiCSV);
							zout.putNextEntry(eventiCSV);
							zout.write(baos.toByteArray());
							zout.closeEntry();

							RrFilter rrFilter = rrBD.newFilter();
							rrFilter.getFilterSortList().add(rrFsw);
							rrFilter.setIdRpt(rpt.getId()); 
							List<Rr> findAll = rrBD.findAll(rrFilter);
							if(findAll != null && findAll.size() > 0){
								for (Rr rr : findAll) {
									numeroZipEntries ++;
									String rrEntryName = iuvCcpDir + "/rr_" + rr.getCodMsgRevoca() + ".xml"; 

									ZipEntry rrXml = new ZipEntry(rrEntryName);
									zout.putNextEntry(rrXml);
									zout.write(rr.getXmlRr());
									zout.closeEntry();

									if(rr.getXmlEr() != null){
										numeroZipEntries ++;
										String erEntryName = iuvCcpDir + "/er_" + rr.getCodMsgRevoca() + ".xml"; 
										ZipEntry rtXml = new ZipEntry(erEntryName);
										zout.putNextEntry(rtXml);
										zout.write(rr.getXmlEr());
										zout.closeEntry();

										// ER in formato pdf
										CtEsitoRevoca er = JaxbUtils.toER(rr.getXmlEr());
										String causale = versamento.getCausaleVersamento().getSimple();
										baos = new ByteArrayOutputStream();
										Dominio dominio3 = AnagraficaManager.getDominio(bd, er.getDominio().getIdentificativoDominio());
										ErPdf.getPdfEsitoRevoca(pathLoghi, er, dominio3, dominio3.getAnagrafica(bd), causale,baos,this.log);

										String erPdfEntryName = iuvCcpDir + "/esito_revoca.pdf";
										numeroZipEntries ++;
										ZipEntry erPdf = new ZipEntry(erPdfEntryName);
										zout.putNextEntry(erPdf);
										zout.write(baos.toByteArray());
										zout.closeEntry();
									}
								}
							}
						}
					}
				}

				List<it.govpay.core.business.model.EstrattoConto> listInputEstrattoConto = new ArrayList<it.govpay.core.business.model.EstrattoConto>();
				for (String codDominio : mappaInputEstrattoConto.keySet()) {
					it.govpay.core.business.model.EstrattoConto input =  it.govpay.core.business.model.EstrattoConto.creaEstrattoContoVersamentiPDF(mappaInputDomini.get(codDominio), mappaInputEstrattoConto.get(codDominio)); 
					listInputEstrattoConto.add(input);
				}


				List<it.govpay.core.business.model.EstrattoConto> listOutputEstattoConto = estrattoContoBD.getEstrattoContoVersamenti(listInputEstrattoConto,pathLoghi);

				for (it.govpay.core.business.model.EstrattoConto estrattoContoOutput : listOutputEstattoConto) {
					Map<String, ByteArrayOutputStream> estrattoContoVersamenti = estrattoContoOutput.getOutput(); 
					for (String nomeEntry : estrattoContoVersamenti.keySet()) {
						numeroZipEntries ++;
						ByteArrayOutputStream baos = estrattoContoVersamenti.get(nomeEntry);
						ZipEntry estrattoContoEntry = new ZipEntry(estrattoContoOutput.getDominio().getCodDominio() + "/" + nomeEntry);
						zout.putNextEntry(estrattoContoEntry);
						zout.write(baos.toByteArray());
						zout.closeEntry();
					}


				}

				// Estratto Conto in formato CSV
				EstrattiContoBD estrattiContoBD = new EstrattiContoBD(bd);
				EstrattoContoFilter ecFilter = estrattiContoBD.newFilter();
				ecFilter.setIdVersamento(idsToExport); 
				List<EstrattoConto> findAll =  estrattiContoBD.estrattoContoFromIdVersamenti(ecFilter);

				if(findAll != null && findAll.size() > 0){
					//ordinamento record
					Collections.sort(findAll, new EstrattoContoComparator());
					numeroZipEntries ++;
					ByteArrayOutputStream baos  = new ByteArrayOutputStream();
					try{
						ZipEntry pagamentoCsv = new ZipEntry("estrattoConto.csv");
						zout.putNextEntry(pagamentoCsv);
						printer = new Printer(this.getFormat() , baos);
						printer.printRecord(CSVUtils.getEstrattoContoCsvHeader());
						for (EstrattoConto pagamento : findAll) {
							printer.printRecord(CSVUtils.getEstrattoContoAsCsvRow(pagamento,this.sdf));
						}
					}finally {
						try{
							if(printer!=null){
								printer.close();
							}
						}catch (Exception e) {
							throw new Exception("Errore durante la chiusura dello stream ",e);
						}
					}
					zout.write(baos.toByteArray());
					zout.closeEntry();
				}
			}

			// se non ho inserito nessuna entry
			if(numeroZipEntries == 0){
				String noEntriesTxt = "/README";
				ZipEntry entryTxt = new ZipEntry(noEntriesTxt);
				zout.putNextEntry(entryTxt);
				zout.write("Non sono state trovate informazioni sui versamenti selezionati.".getBytes());
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
	public String esporta(Long idToExport, UriInfo uriInfo, BasicBD bd, ZipOutputStream zout)
			throws WebApplicationException, ConsoleException {
		String methodName = "esporta " + this.titoloServizio + "[" + idToExport + "]";  
		Printer printer  = null;

		try{
			int numeroZipEntries = 0;
			this.log.info("Esecuzione " + methodName + " in corso...");
			Operatore operatore = this.darsService.getOperatoreByPrincipal(bd); 
			ProfiloOperatore profilo = operatore.getProfilo();
			boolean isAdmin = profilo.equals(ProfiloOperatore.ADMIN);

			boolean eseguiRicerca = true;
			VersamentiBD versamentiBD = new VersamentiBD(bd);
			RptBD rptBD = new RptBD(bd);
			EventiBD eventiBd = new EventiBD(bd);
			EstrattiContoBD estrattiContoBD = new EstrattiContoBD(bd);
			it.govpay.core.business.EstrattoConto estrattoContoBD = new it.govpay.core.business.EstrattoConto(bd);
			VersamentoFilter filter = versamentiBD.newFilter();


			List<Long> ids = new ArrayList<Long>();
			ids.add(idToExport);

			if(!isAdmin){

				AclBD aclBD = new AclBD(bd);
				List<Acl> aclOperatore = aclBD.getAclOperatore(operatore.getId());

				boolean vediTuttiDomini = false;
				List<Long> idDomini = new ArrayList<Long>();
				for(Acl acl: aclOperatore) {
					if(Tipo.DOMINIO.equals(acl.getTipo())) {
						if(acl.getIdDominio() == null) {
							vediTuttiDomini = true;
							break;
						} else {
							idDomini.add(acl.getIdDominio());
						}
					}
				}
				if(!vediTuttiDomini) {
					if(idDomini.isEmpty()) {
						eseguiRicerca = false;
					} else {
						filter.setIdDomini(idDomini);
					}
				}

				// l'operatore puo' vedere i domini associati, controllo se c'e' un versamento con Id nei domini concessi.
				if(eseguiRicerca){
					filter.setIdVersamento(ids);
					eseguiRicerca = eseguiRicerca && versamentiBD.count(filter) > 0;
				}
			}
			Eventi eventiDars = new Eventi();
			EventiHandler eventiDarsHandler = (EventiHandler) eventiDars.getDarsHandler(); 
			Versamento versamento = eseguiRicerca ? versamentiBD.getVersamento(idToExport) : null;
			String fileName = "Export.zip";  

			if(versamento != null){
				// Prelevo il dominio

				UnitaOperativa uo  = AnagraficaManager.getUnitaOperativa(bd, versamento.getIdUo());
				Dominio dominio  = AnagraficaManager.getDominio(bd, uo.getIdDominio());

				String dirDominio = dominio.getCodDominio();

				// Estratto conto per iban e codiceversamento.
				List<Long> idVersamentiDominio = new ArrayList<Long>();
				idVersamentiDominio.add(idToExport);
				it.govpay.core.business.model.EstrattoConto input =  it.govpay.core.business.model.EstrattoConto.creaEstrattoContoVersamentiPDF(dominio, idVersamentiDominio);
				List<it.govpay.core.business.model.EstrattoConto> listInputEstrattoConto = new ArrayList<it.govpay.core.business.model.EstrattoConto>();
				listInputEstrattoConto.add(input);
				String pathLoghi = ConsoleProperties.getInstance().getPathEstrattoContoPdfLoghi();
				List<it.govpay.core.business.model.EstrattoConto> listOutputEstattoConto = estrattoContoBD.getEstrattoContoVersamenti(listInputEstrattoConto,pathLoghi);

				for (it.govpay.core.business.model.EstrattoConto estrattoContoOutput : listOutputEstattoConto) {
					Map<String, ByteArrayOutputStream> estrattoContoVersamenti = estrattoContoOutput.getOutput(); 
					for (String nomeEntry : estrattoContoVersamenti.keySet()) {
						numeroZipEntries ++;
						ByteArrayOutputStream baos = estrattoContoVersamenti.get(nomeEntry);
						ZipEntry estrattoContoEntry = new ZipEntry(estrattoContoOutput.getDominio().getCodDominio() + "/" + nomeEntry);
						zout.putNextEntry(estrattoContoEntry);
						zout.write(baos.toByteArray());
						zout.closeEntry();
					}
				}

				String dirVersamento = dirDominio + "/" + versamento.getCodVersamentoEnte();

				RptFilter rptFilter = rptBD.newFilter();
				FilterSortWrapper rptFsw = new FilterSortWrapper();
				rptFsw.setField(it.govpay.orm.RPT.model().DATA_MSG_RICHIESTA);
				rptFsw.setSortOrder(SortOrder.DESC);
				rptFilter.getFilterSortList().add(rptFsw);
				rptFilter.setIdVersamento(idToExport); 

				RrBD rrBD = new RrBD(bd);
				FilterSortWrapper rrFsw = new FilterSortWrapper();
				rrFsw.setField(it.govpay.orm.RR.model().DATA_MSG_REVOCA);
				rrFsw.setSortOrder(SortOrder.DESC);

				List<Rpt> listaRpt = rptBD.findAll(rptFilter);
				if(listaRpt != null && listaRpt.size() >0 ) {
					for (Rpt rpt : listaRpt) {
						numeroZipEntries ++;
						String iuv = rpt.getIuv();
						String ccp = rpt.getCcp();
						String iuvCcpDir = dirVersamento + "/" + iuv;

						// non appendo il ccp nel caso sia uguale ad 'n/a' altrimenti crea un nuovo livello di directory;
						if(!StringUtils.equalsIgnoreCase(ccp, "n/a")) {
							iuvCcpDir = iuvCcpDir  + "_" + ccp;
						}

						String rptEntryName = iuvCcpDir + "/rpt_" + rpt.getCodMsgRichiesta() + ".xml"; 

						ZipEntry rptXml = new ZipEntry( rptEntryName);
						zout.putNextEntry(rptXml);
						zout.write(rpt.getXmlRpt());
						zout.closeEntry();

						if(rpt.getXmlRt() != null){
							numeroZipEntries ++;
							String rtEntryName = iuvCcpDir + "/rt_" + rpt.getCodMsgRichiesta() + ".xml";
							ZipEntry rtXml = new ZipEntry(rtEntryName);
							zout.putNextEntry(rtXml);
							zout.write(rpt.getXmlRt());
							zout.closeEntry();

							// RT in formato pdf
							String tipoFirma = rpt.getFirmaRichiesta().getCodifica();
							byte[] rtByteValidato = RtUtils.validaFirma(tipoFirma, rpt.getXmlRt(), dominio.getCodDominio());
							CtRicevutaTelematica rt = JaxbUtils.toRT(rtByteValidato);
							ByteArrayOutputStream baos = new ByteArrayOutputStream();
							String auxDigit = dominio.getAuxDigit() + "";
							String applicationCode = String.format("%02d", dominio.getStazione(bd).getApplicationCode());
							RicevutaPagamentoUtils.getPdfRicevutaPagamento(pathLoghi, rt, versamento, auxDigit, applicationCode, baos, this.log);

							String rtPdfEntryName = iuvCcpDir + "/ricevuta_pagamento.pdf";
							numeroZipEntries ++;
							ZipEntry rtPdf = new ZipEntry(rtPdfEntryName);
							zout.putNextEntry(rtPdf);
							zout.write(baos.toByteArray());
							zout.closeEntry();
						}

						// Eventi
						String entryEventiCSV =  iuvCcpDir + "/eventi.csv";

						EventiFilter eventiFilter = eventiBd.newFilter();
						eventiFilter.setCodDominio(dominio.getCodDominio());
						eventiFilter.setIuv(iuv);
						eventiFilter.setCcp(ccp);
						FilterSortWrapper fsw = new FilterSortWrapper();
						fsw.setField(it.govpay.orm.Evento.model().DATA_1);
						fsw.setSortOrder(SortOrder.ASC);
						eventiFilter.getFilterSortList().add(fsw);

						List<Evento> findAllEventi = eventiBd.findAll(eventiFilter);
						ByteArrayOutputStream baos = new ByteArrayOutputStream();
						eventiDarsHandler.scriviCSVEventi(baos, findAllEventi);

						ZipEntry eventiCSV = new ZipEntry(entryEventiCSV);
						zout.putNextEntry(eventiCSV);
						zout.write(baos.toByteArray());
						zout.closeEntry();


						RrFilter rrFilter = rrBD.newFilter();
						rrFilter.getFilterSortList().add(rrFsw);
						rrFilter.setIdRpt(rpt.getId()); 
						List<Rr> findAll = rrBD.findAll(rrFilter);
						if(findAll != null && findAll.size() > 0){
							for (Rr rr : findAll) {
								numeroZipEntries ++;
								String rrEntryName = iuvCcpDir + "/rr_" + rr.getCodMsgRevoca() + ".xml"; 

								ZipEntry rrXml = new ZipEntry(rrEntryName);
								zout.putNextEntry(rrXml);
								zout.write(rr.getXmlRr());
								zout.closeEntry();

								if(rr.getXmlEr() != null){
									numeroZipEntries ++;
									String erEntryName = iuvCcpDir + "/er_" + rr.getCodMsgRevoca() + ".xml"; 
									ZipEntry rtXml = new ZipEntry(erEntryName);
									zout.putNextEntry(rtXml);
									zout.write(rr.getXmlEr());
									zout.closeEntry();

									// ER in formato pdf
									CtEsitoRevoca er = JaxbUtils.toER(rr.getXmlEr());
									String causale = versamento.getCausaleVersamento().getSimple();
									baos = new ByteArrayOutputStream();
									Dominio dominio3 = AnagraficaManager.getDominio(bd, er.getDominio().getIdentificativoDominio());
									ErPdf.getPdfEsitoRevoca(pathLoghi, er, dominio3, dominio3.getAnagrafica(bd), causale,baos,this.log);

									String erPdfEntryName = iuvCcpDir + "/esito_revoca.pdf";
									numeroZipEntries ++;
									ZipEntry erPdf = new ZipEntry(erPdfEntryName);
									zout.putNextEntry(erPdf);
									zout.write(baos.toByteArray());
									zout.closeEntry();
								}
							}
						}
					}
				}

				//Estratto conto in formato CSV
				EstrattoContoFilter ecFilter = estrattiContoBD.newFilter(true); 
				ecFilter.setIdVersamento(ids);
				List<EstrattoConto> findAll =  estrattiContoBD.estrattoContoFromIdVersamenti(ecFilter);

				if(findAll != null && findAll.size() > 0){
					//ordinamento record
					Collections.sort(findAll, new EstrattoContoComparator());
					numeroZipEntries ++;
					ByteArrayOutputStream baos  = new ByteArrayOutputStream();
					try{
						ZipEntry pagamentoCsv = new ZipEntry("estrattoConto.csv");
						zout.putNextEntry(pagamentoCsv);
						printer = new Printer(this.getFormat() , baos);
						printer.printRecord(CSVUtils.getEstrattoContoCsvHeader());
						for (EstrattoConto pagamento : findAll) {
							printer.printRecord(CSVUtils.getEstrattoContoAsCsvRow(pagamento,this.sdf));
						}
					}finally {
						try{
							if(printer!=null){
								printer.close();
							}
						}catch (Exception e) {
							throw new Exception("Errore durante la chiusura dello stream ",e);
						}
					}
					zout.write(baos.toByteArray());
					zout.closeEntry();
				}
			}

			// se non ho inserito nessuna entry
			if(numeroZipEntries == 0){
				String noEntriesTxt = "/README";
				ZipEntry entryTxt = new ZipEntry(noEntriesTxt);
				zout.putNextEntry(entryTxt);
				zout.write("Non sono state trovate informazioni sui versamenti selezionati.".getBytes());
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
	public InfoForm getInfoCancellazione(UriInfo uriInfo, BasicBD bd) throws ConsoleException {
		return null;
	}

	@Override
	public InfoForm getInfoCancellazioneDettaglio(UriInfo uriInfo, BasicBD bd, Incasso entry) throws ConsoleException {
		return null;
	}

	/* Creazione/Update non consentiti**/

	@Override
	public InfoForm getInfoCreazione(UriInfo uriInfo, BasicBD bd) throws ConsoleException { return null; }

	@Override
	public InfoForm getInfoModifica(UriInfo uriInfo, BasicBD bd, Incasso entry) throws ConsoleException { return null; }

	@Override
	public Elenco delete(List<Long> idsToDelete, List<RawParamValue> rawValues, UriInfo uriInfo, BasicBD bd) throws WebApplicationException, ConsoleException, DeleteException {
		StringBuffer sb = new StringBuffer();
		if(idsToDelete != null && idsToDelete.size() > 0) {
			for (Long long1 : idsToDelete) {

				if(sb.length() > 0) {
					sb.append(", ");
				}

				sb.append(long1);
			}
		}

		String methodName = "delete " + this.titoloServizio + "[" + sb.toString() + "]";
		String motivoCancellazioneId = Utils.getInstance(this.getLanguage()).getMessageFromResourceBundle(this.nomeServizio + ".motivoCancellazione.id");
		String motivoCancellazioneLabel = Utils.getInstance(this.getLanguage()).getMessageFromResourceBundle(this.nomeServizio + ".motivoCancellazione.label");

		try{
			this.log.info("Esecuzione " + methodName + " in corso...");
			// Operazione consentita solo all'amministratore
			this.darsService.checkOperatoreAdmin(bd);
			Operatore operatore = this.darsService.getOperatoreByPrincipal(bd);

			VersamentiBD versamentiBD = new VersamentiBD(bd);
			versamentiBD.setAutoCommit(false);
			versamentiBD.enableSelectForUpdate();

			String motivoCancellazione = Utils.getValue(rawValues, motivoCancellazioneId);
			this.log.debug("Esecuzione " + methodName + ": Letto parametro ["+motivoCancellazioneLabel+"] con valore ["+motivoCancellazione+"]");

			List<String> lstErrori = new ArrayList<String>();
			String dataS = this.sdf.format(new Date());
			try{
				for (Long id : idsToDelete) {
					Versamento versamento = versamentiBD.getVersamento(id);

					String descrizioneStato =  Utils.getInstance(this.getLanguage()).getMessageWithParamsFromResourceBundle(this.nomeServizio + ".cancellazione.descrizioneStato.template",
							dataS,operatore.getPrincipal(),motivoCancellazione);
					this.log.debug("Descrizione motivazione cancellamento: ["+descrizioneStato+"]"); 
					versamento.setDescrizioneStato(descrizioneStato);
					try{
						versamentiBD.annullaVersamento(versamento,descrizioneStato);
					} catch (VersamentoException e) {
						String codEsito = e.getCodEsito();
						String msgEsito = Utils.getInstance(this.getLanguage()).getMessageFromResourceBundle(this.nomeServizio + ".cancellazione.esito." + codEsito);
						String msgErrore = Utils.getInstance(this.getLanguage()).getMessageWithParamsFromResourceBundle(this.nomeServizio + ".cancellazione.esito.messaggioUtente.template",
								e.getCodVersamentoEnte(),msgEsito);
						lstErrori.add(msgErrore);
					}
				}
			}catch (ServiceException e) {
				throw e;
			}finally {
				versamentiBD.commit();
				versamentiBD.disableSelectForUpdate();
			}

			this.log.info("Esecuzione " + methodName + " completata, rilevati ["+lstErrori.size()+"] errori. ");

			if(lstErrori.size() > 0) {
				// sono andati tutti male
				if(lstErrori.size() == idsToDelete.size()){
					// selezionato uno o tutti 
					String msg = idsToDelete.size() == 1 ? 
							Utils.getInstance(this.getLanguage()).getMessageFromResourceBundle(this.nomeServizio + ".cancellazione.esito.messaggioUtente.soloErrore")
							: Utils.getInstance(this.getLanguage()).getMessageFromResourceBundle(this.nomeServizio + ".cancellazione.esito.messaggioUtente.tuttiErrore") ;

							lstErrori.add(0,msg);
							throw new DeleteException(lstErrori, EsitoOperazione.ERRORE);
				}

				lstErrori.add(0,Utils.getInstance(this.getLanguage()).getMessageFromResourceBundle(this.nomeServizio + ".cancellazione.esito.messaggioUtente.alcuniErrore"));
				throw new DeleteException(lstErrori, EsitoOperazione.NONESEGUITA);
			}

			return this.getElenco(uriInfo, bd);

		}catch(DeleteException e){
			throw e;
		}catch(WebApplicationException e){
			throw e;
		}catch(Exception e){
			throw new ConsoleException(e);
		}
	}

	@Override
	public Incasso creaEntry(InputStream is, UriInfo uriInfo, BasicBD bd) throws WebApplicationException, ConsoleException { return null; }

	@Override
	public Dettaglio insert(InputStream is, UriInfo uriInfo, BasicBD bd) throws WebApplicationException, ConsoleException, ValidationException, DuplicatedEntryException { return null; }

	@Override
	public void checkEntry(Incasso entry, Incasso oldEntry) throws ValidationException { }

	@Override
	public Dettaglio update(InputStream is, UriInfo uriInfo, BasicBD bd) throws WebApplicationException, ConsoleException, ValidationException { return null; }

	@Override
	public Object uplaod(MultipartFormDataInput input, UriInfo uriInfo, BasicBD bd)	throws WebApplicationException, ConsoleException, ValidationException { return null;}
}
