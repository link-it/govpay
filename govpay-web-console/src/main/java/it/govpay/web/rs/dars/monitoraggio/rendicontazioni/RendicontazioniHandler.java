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
package it.govpay.web.rs.dars.monitoraggio.rendicontazioni;

import java.io.InputStream;
import java.math.BigDecimal;
import java.net.URI;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
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
import it.govpay.bd.anagrafica.AclBD;
import it.govpay.bd.anagrafica.AnagraficaManager;
import it.govpay.bd.model.Dominio;
import it.govpay.bd.model.Fr;
import it.govpay.bd.model.Pagamento;
import it.govpay.bd.model.Rendicontazione;
import it.govpay.bd.model.SingoloVersamento;
import it.govpay.bd.pagamento.RendicontazioniBD;
import it.govpay.bd.pagamento.filters.RendicontazioneFilter;
import it.govpay.model.Acl;
import it.govpay.model.Acl.Tipo;
import it.govpay.model.Operatore;
import it.govpay.model.Operatore.ProfiloOperatore;
import it.govpay.model.Rendicontazione.Anomalia;
import it.govpay.model.Rendicontazione.EsitoRendicontazione;
import it.govpay.model.Rendicontazione.StatoRendicontazione;
import it.govpay.web.rs.dars.anagrafica.domini.Domini;
import it.govpay.web.rs.dars.anagrafica.domini.DominiHandler;
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
import it.govpay.web.rs.dars.model.input.base.InputText;
import it.govpay.web.rs.dars.model.input.base.SelectList;
import it.govpay.web.rs.dars.monitoraggio.pagamenti.Pagamenti;
import it.govpay.web.rs.dars.monitoraggio.pagamenti.PagamentiHandler;
import it.govpay.web.utils.Utils;

public class RendicontazioniHandler extends DarsHandler<Rendicontazione> implements IDarsHandler<Rendicontazione>{

	private SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy"); //  HH:mm  
	//private SimpleDateFormat simpleDateFormatAnno = new SimpleDateFormat("yyyy");


	public RendicontazioniHandler(Logger log, DarsService darsService) { 
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

			Integer offset = this.getOffset(uriInfo);
			Integer limit = this.getLimit(uriInfo);

			this.log.info("Esecuzione " + methodName + " in corso..."); 

			boolean simpleSearch = this.containsParameter(uriInfo, DarsService.SIMPLE_SEARCH_PARAMETER_ID);
			RendicontazioniBD frBD = new RendicontazioniBD(bd);
			AclBD aclBD = new AclBD(bd);
			List<Acl> aclOperatore = aclBD.getAclOperatore(operatore.getId());
			List<String> listaCodDomini =  new ArrayList<String>();
			RendicontazioneFilter filter = frBD.newFilter(simpleSearch);
			filter.setOffset(offset);
			filter.setLimit(limit);
			FilterSortWrapper fsw = new FilterSortWrapper();
			fsw.setField(it.govpay.orm.Rendicontazione.model().DATA);
			fsw.setSortOrder(SortOrder.DESC);
			filter.getFilterSortList().add(fsw);

			boolean eseguiRicerca = true;
			Map<String, String> params = new HashMap<String, String>();
			String idFlussoId = Utils.getInstance(this.getLanguage()).getMessageFromResourceBundle(this.nomeServizio + ".idFr.id");
			String idFlusso = this.getParameter(uriInfo, idFlussoId, String.class);
			params.put(idFlussoId, idFlusso);

			if(StringUtils.isNotEmpty(idFlusso)){
				try{
					filter.setIdFr(Long.parseLong(idFlusso));
				}catch(Exception e){
					filter.setIdFr(-1L);
					eseguiRicerca = false;
				}
			}

			if(simpleSearch) {
				// simplesearch
				String simpleSearchString = this.getParameter(uriInfo, DarsService.SIMPLE_SEARCH_PARAMETER_ID, String.class);
				params.put(DarsService.SIMPLE_SEARCH_PARAMETER_ID, simpleSearchString);

				if(StringUtils.isNotEmpty(simpleSearchString)) {
					filter.setSimpleSearchString(simpleSearchString);
				}
			} else {
				String statoId = Utils.getInstance(this.getLanguage()).getMessageFromResourceBundle(this.nomeServizio + ".stato.id");
				String stato = this.getParameter(uriInfo, statoId, String.class);

				if(StringUtils.isNotEmpty(stato))
					filter.setStato(StatoRendicontazione.valueOf(stato)); 

				String tipoId = Utils.getInstance(this.getLanguage()).getMessageFromResourceBundle(this.nomeServizio + ".tipo.id");
				String tipo = this.getParameter(uriInfo, tipoId, String.class);

				if(StringUtils.isNotEmpty(tipo))
					filter.setTipo(tipo); 

				String iuvId = Utils.getInstance(this.getLanguage()).getMessageFromResourceBundle(this.nomeServizio + ".iuv.id");
				String iuv = this.getParameter(uriInfo, iuvId, String.class);

				if(StringUtils.isNotEmpty(iuv))
					filter.setIuv(iuv);
			}
			
			if(!isAdmin && listaCodDomini.isEmpty()){
				boolean vediTuttiDomini = false;

				for(Acl acl: aclOperatore) {
					if(Tipo.DOMINIO.equals(acl.getTipo())) {
						if(acl.getIdDominio() == null) {
							vediTuttiDomini = true;
							break;
						} else {
							listaCodDomini.add(acl.getCodDominio());
						}
					}
				}
				if(!vediTuttiDomini) {
					if(listaCodDomini.isEmpty()) {
						eseguiRicerca = false;
					} else {
						filter.setIdDomini(listaCodDomini);
					}
				}
			}

			long count = eseguiRicerca ? frBD.count(filter) : 0;

			// visualizza la ricerca solo se i risultati sono > del limit
			boolean visualizzaRicerca = this.visualizzaRicerca(count, limit);
			InfoForm infoRicerca = this.getInfoRicerca(uriInfo, bd, visualizzaRicerca,params); 

			String formatter = Utils.getInstance(this.getLanguage()).getMessageFromResourceBundle(this.nomeServizio+".elenco.formatter");
			String simpleSearchPlaceholder = Utils.getInstance(this.getLanguage()).getMessageFromResourceBundle(this.nomeServizio+".simpleSearch.placeholder");
			Elenco elenco = new Elenco(this.titoloServizio, infoRicerca,
					this.getInfoCreazione(uriInfo, bd),
					count, this.getInfoEsportazione(uriInfo, bd), this.getInfoCancellazione(uriInfo, bd),simpleSearchPlaceholder); 

			List<Rendicontazione> findAll = eseguiRicerca ? frBD.findAll(filter) : new ArrayList<Rendicontazione>(); 

			if(findAll != null && findAll.size() > 0){
				for (Rendicontazione entry : findAll) {
					Elemento elemento = this.getElemento(entry, entry.getId(), null,bd);
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
		URI ricerca = this.getUriRicerca(uriInfo, bd,parameters);
		InfoForm infoRicerca = new InfoForm(ricerca);

		if(visualizzaRicerca) {
			String statoId = Utils.getInstance(this.getLanguage()).getMessageFromResourceBundle(this.nomeServizio + ".stato.id");
			String tipoId = Utils.getInstance(this.getLanguage()).getMessageFromResourceBundle(this.nomeServizio + ".tipo.id");
			String iuvId = Utils.getInstance(this.getLanguage()).getMessageFromResourceBundle(this.nomeServizio + ".iuv.id");

			if(this.infoRicercaMap == null){
				this.initInfoRicerca(uriInfo, bd);
			}
			Sezione sezioneRoot = infoRicerca.getSezioneRoot();

			// stato
			List<Voce<String>> stati = new ArrayList<Voce<String>>();
			SelectList<String> stato = (SelectList<String>) infoRicercaMap.get(statoId);
			stati.add(new Voce<String>(Utils.getInstance(this.getLanguage()).getMessageFromResourceBundle("commons.label.qualsiasi"), ""));
			stati.add(new Voce<String>(Utils.getInstance(this.getLanguage()).getMessageFromResourceBundle(this.nomeServizio + ".stato."+StatoRendicontazione.OK), StatoRendicontazione.OK.toString()));
			stati.add(new Voce<String>(Utils.getInstance(this.getLanguage()).getMessageFromResourceBundle(this.nomeServizio + ".stato."+StatoRendicontazione.ANOMALA), StatoRendicontazione.ANOMALA.toString()));
			stati.add(new Voce<String>(Utils.getInstance(this.getLanguage()).getMessageFromResourceBundle(this.nomeServizio + ".stato."+StatoRendicontazione.ALTRO_INTERMEDIARIO), StatoRendicontazione.ALTRO_INTERMEDIARIO.toString()));
			stato.setDefaultValue("");
			stato.setValues(stati); 
			sezioneRoot.addField(stato);

			// tipo
			List<Voce<String>> tipi = new ArrayList<Voce<String>>();
			SelectList<String> tipo = (SelectList<String>) infoRicercaMap.get(tipoId);
			tipi.add(new Voce<String>(Utils.getInstance(this.getLanguage()).getMessageFromResourceBundle("commons.label.qualsiasi"), ""));
			tipi.add(new Voce<String>(Utils.getInstance(this.getLanguage()).getMessageFromResourceBundle(this.nomeServizio + ".esito."+EsitoRendicontazione.ESEGUITO), EsitoRendicontazione.ESEGUITO.toString()));
			tipi.add(new Voce<String>(Utils.getInstance(this.getLanguage()).getMessageFromResourceBundle(this.nomeServizio + ".esito."+EsitoRendicontazione.ESEGUITO_SENZA_RPT), EsitoRendicontazione.ESEGUITO_SENZA_RPT.toString()));
			tipi.add(new Voce<String>(Utils.getInstance(this.getLanguage()).getMessageFromResourceBundle(this.nomeServizio + ".esito."+EsitoRendicontazione.REVOCATO), EsitoRendicontazione.REVOCATO.toString()));
			tipo.setDefaultValue("");
			tipo.setValues(tipi); 
			sezioneRoot.addField(tipo);


			// iuv
			InputText iuv = (InputText) infoRicercaMap.get(iuvId);
			iuv.setDefaultValue(null);
			sezioneRoot.addField(iuv);

		}
		return infoRicerca;
	}

	private void initInfoRicerca(UriInfo uriInfo, BasicBD bd) throws ConsoleException{
		if(this.infoRicercaMap == null){
			this.infoRicercaMap = new HashMap<String, ParamField<?>>();

			String statoId = Utils.getInstance(this.getLanguage()).getMessageFromResourceBundle(this.nomeServizio + ".stato.id");
			String tipoId = Utils.getInstance(this.getLanguage()).getMessageFromResourceBundle(this.nomeServizio + ".tipo.id");
			String iuvId = Utils.getInstance(this.getLanguage()).getMessageFromResourceBundle(this.nomeServizio + ".iuv.id");

			// iuv
			String iuvLabel = Utils.getInstance(this.getLanguage()).getMessageFromResourceBundle(this.nomeServizio + ".iuv.label");
			InputText iuv = new InputText(iuvId, iuvLabel, null, false, false, true, 0, 35);
			this.infoRicercaMap.put(iuvId, iuv);

			List<Voce<String>> stati = new ArrayList<Voce<String>>();
			// stato
			String statoLabel = Utils.getInstance(this.getLanguage()).getMessageFromResourceBundle(this.nomeServizio + ".stato.label");
			SelectList<String> stato = new SelectList<String>(statoId, statoLabel, null, false, false, true, stati);
			infoRicercaMap.put(statoId, stato);


			List<Voce<String>> tipi = new ArrayList<Voce<String>>();
			// tipo
			String tipoLabel = Utils.getInstance(this.getLanguage()).getMessageFromResourceBundle(this.nomeServizio + ".tipo.label");
			SelectList<String> tipo = new SelectList<String>(tipoId, tipoLabel, null, false, false, true, tipi);
			infoRicercaMap.put(tipoId, tipo);
		}
	}

	@Override
	public Object getField(UriInfo uriInfo, List<RawParamValue> values, String fieldId, BasicBD bd) throws WebApplicationException, ConsoleException {
		return null;
	}

	@Override
	public Object getSearchField(UriInfo uriInfo, List<RawParamValue> values, String fieldId, BasicBD bd)	throws WebApplicationException, ConsoleException { 	return null; }

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
			
			RendicontazioniBD frBD = new RendicontazioniBD(bd);
			boolean eseguiRicerca = true; //isAdmin;
			// SE l'operatore non e' admin vede solo i versamenti associati alle sue UO ed applicazioni
			// controllo se l'operatore ha fatto una richiesta di visualizzazione di un versamento che puo' vedere
			if(!isAdmin){
				//				eseguiRicerca = !Utils.isEmpty(operatore.getIdApplicazioni()) || !Utils.isEmpty(operatore.getIdEnti());
				RendicontazioneFilter filter = frBD.newFilter();
				
				List<Long> idRendL = new ArrayList<Long>();
				idRendL.add(id);
				filter.setIdRendicontazione(idRendL);

				List<String> idDomini = new ArrayList<String>();
				boolean vediTuttiDomini = false;

				AclBD aclBD = new AclBD(bd);
				List<Acl> aclOperatore = aclBD.getAclOperatore(operatore.getId());

				for(Acl acl: aclOperatore) {
					if(Tipo.DOMINIO.equals(acl.getTipo())) {
						if(acl.getIdDominio() == null) {
							vediTuttiDomini = true;
							break;
						} else {
							idDomini.add(acl.getCodDominio());
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

				long count = eseguiRicerca ? frBD.count(filter) : 0;
				eseguiRicerca = eseguiRicerca && count > 0;
			}

			// recupero oggetto
			Rendicontazione rendicontazione = eseguiRicerca ? frBD.getRendicontazione(id) : null;

			InfoForm infoModifica = null;
			InfoForm infoCancellazione = rendicontazione != null ? this.getInfoCancellazioneDettaglio(uriInfo, bd, rendicontazione) : null;
			InfoForm infoEsportazione = rendicontazione != null ? this.getInfoEsportazioneDettaglio(uriInfo, bd, rendicontazione) : null;

			String titolo = rendicontazione != null ? this.getTitolo(rendicontazione,bd) : "";
			Dettaglio dettaglio = new Dettaglio(titolo, infoEsportazione, infoCancellazione, infoModifica);

			it.govpay.web.rs.dars.model.Sezione root = dettaglio.getSezioneRoot();

			String codDominio = null;
			if(rendicontazione != null){
				Pagamento pagamento = rendicontazione.getPagamento(bd);
				if(pagamento != null) {
					codDominio = pagamento.getCodDominio();
				}  else {
					Fr fr = rendicontazione.getFr(bd);
					codDominio = fr.getCodDominio();
				}

				// dominio
				if(StringUtils.isNotEmpty(codDominio)){
					try{
						Dominio dominio = AnagraficaManager.getDominio(bd, codDominio);
						Domini dominiDars = new Domini();
						DominiHandler dominiDarsHandler =  (DominiHandler) dominiDars.getDarsHandler();
						Elemento elemento = dominiDarsHandler.getElemento(dominio, dominio.getId(), dominiDars.getPathServizio(), bd);
						root.addVoce(Utils.getInstance(this.getLanguage()).getMessageFromResourceBundle(this.nomeServizio + ".dominio.label"), elemento.getTitolo(),elemento.getUri());
					}catch(NotFoundException e){}
				}

				// iur
				if(StringUtils.isNotEmpty(rendicontazione.getIur())) {
					root.addVoce(Utils.getInstance(this.getLanguage()).getMessageFromResourceBundle(this.nomeServizio + ".iur.label"), rendicontazione.getIur());
				}

				if(StringUtils.isNotEmpty(rendicontazione.getIuv())) {
					root.addVoce(Utils.getInstance(this.getLanguage()).getMessageFromResourceBundle(this.nomeServizio + ".iuv.label"), rendicontazione.getIuv());
				}

				if(rendicontazione.getData() != null) {
					root.addVoce(Utils.getInstance(this.getLanguage()).getMessageFromResourceBundle(this.nomeServizio + ".data.label"), this.sdf.format(rendicontazione.getData()));
				}

				BigDecimal importoPagato = rendicontazione.getImporto() != null ? rendicontazione.getImporto() : BigDecimal.ZERO;  
				root.addVoce(Utils.getInstance(this.getLanguage()).getMessageFromResourceBundle(this.nomeServizio + ".importo.label"), importoPagato.doubleValue()+ "€");

				StatoRendicontazione stato = rendicontazione.getStato();
				if(stato!= null) {
					root.addVoce(Utils.getInstance(this.getLanguage()).getMessageFromResourceBundle(this.nomeServizio + ".stato.label"),
							Utils.getInstance(this.getLanguage()).getMessageFromResourceBundle(this.nomeServizio + ".stato."+stato.name()));
				}

				EsitoRendicontazione esito = rendicontazione.getEsito();
				if(esito!= null) {
					root.addVoce(Utils.getInstance(this.getLanguage()).getMessageFromResourceBundle(this.nomeServizio + ".esito.label"),
							Utils.getInstance(this.getLanguage()).getMessageFromResourceBundle(this.nomeServizio + ".esito."+esito.name()));
				}

				if(pagamento != null) {
					Pagamenti pagamentiDars = new Pagamenti();
					PagamentiHandler pagamentiDarsHandler = (PagamentiHandler) pagamentiDars.getDarsHandler();

					SingoloVersamento singoloVersamento = pagamento.getSingoloVersamento(bd);
					if(singoloVersamento != null){
						Elemento elemento = pagamentiDarsHandler.getElemento(pagamento, pagamento.getId(), pagamentiDars.getPathServizio(), bd);
						root.addVoce(Utils.getInstance(this.getLanguage()).getMessageFromResourceBundle(this.nomeServizio + ".idPagamento.label"), singoloVersamento.getCodSingoloVersamentoEnte(),elemento.getUri());
					}
				}

				if(rendicontazione.getAnomalie()!= null && rendicontazione.getAnomalie().size() > 0) {
					String etichettaSezioneAnomalie = Utils.getInstance(this.getLanguage()).getMessageFromResourceBundle(this.nomeServizio + ".sezioneAnomalie.label");
					it.govpay.web.rs.dars.model.Sezione sezioneAnomalie = dettaglio.addSezione(etichettaSezioneAnomalie );

					for (Anomalia anomalia : rendicontazione.getAnomalie()) {
						sezioneAnomalie.addVoce(anomalia.getCodice(),anomalia.getDescrizione());
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
	public String getTitolo(Rendicontazione entry,BasicBD bd) {
		StringBuilder sb = new StringBuilder();

		String codFlusso = entry.getIur();
		Date dataFlusso = entry.getData();

		sb.append(
				Utils.getInstance(this.getLanguage()).getMessageWithParamsFromResourceBundle(this.nomeServizio + ".label.titolo",
						codFlusso,this.sdf.format(dataFlusso)));

		return sb.toString();
	}

	@Override
	public String getSottotitolo(Rendicontazione entry,BasicBD bd) {
		StringBuilder sb = new StringBuilder();
		StatoRendicontazione stato = entry.getStato();
		EsitoRendicontazione esito = entry.getEsito();

		sb.append(
				Utils.getInstance(this.getLanguage()).getMessageWithParamsFromResourceBundle(this.nomeServizio + ".label.sottotitolo",
						Utils.getInstance(this.getLanguage()).getMessageFromResourceBundle(this.nomeServizio + ".esito.label."+esito.name()),
						Utils.getInstance(this.getLanguage()).getMessageFromResourceBundle(this.nomeServizio + ".stato."+stato.name())));
		return sb.toString();
	} 

	@Override
	public Map<String, Voce<String>> getVoci(Rendicontazione entry, BasicBD bd) throws ConsoleException { 
		Map<String, Voce<String>> voci = new HashMap<String, Voce<String>>();

		if(StringUtils.isNotEmpty(entry.getIuv())){
			voci.put(Utils.getInstance(this.getLanguage()).getMessageFromResourceBundle(this.nomeServizio + ".iuv.id"),
					new Voce<String>(Utils.getInstance(this.getLanguage()).getMessageFromResourceBundle(this.nomeServizio + ".iuv.label"),
							entry.getIuv()));
		}

		if(StringUtils.isNotEmpty(entry.getIur())){
			voci.put(Utils.getInstance(this.getLanguage()).getMessageFromResourceBundle(this.nomeServizio + ".iur.id"),
					new Voce<String>(Utils.getInstance(this.getLanguage()).getMessageFromResourceBundle(this.nomeServizio + ".iur.label"),
							entry.getIur()));
		}

		StatoRendicontazione stato = entry.getStato();
		if(stato!= null) {
			voci.put(Utils.getInstance(this.getLanguage()).getMessageFromResourceBundle(this.nomeServizio + ".stato.id"),
					new Voce<String>(this.getSottotitolo(entry, bd),
							stato.name()));
		}

		EsitoRendicontazione esito = entry.getEsito();
		if(stato!= null) {
			voci.put(Utils.getInstance(this.getLanguage()).getMessageFromResourceBundle(this.nomeServizio + ".esito.id"),
					new Voce<String>(esito.name(),esito.getCodifica()+""));
		}

		if(entry.getData() != null) {
			voci.put(Utils.getInstance(this.getLanguage()).getMessageFromResourceBundle(this.nomeServizio + ".data.id"),
					new Voce<String>(Utils.getInstance(this.getLanguage()).getMessageFromResourceBundle(this.nomeServizio + ".data.label"),
							this.sdf.format(entry.getData())));
		}

		BigDecimal importoPagato = entry.getImporto() != null ? entry.getImporto() : BigDecimal.ZERO;  
		voci.put(Utils.getInstance(this.getLanguage()).getMessageFromResourceBundle(this.nomeServizio + ".importo.id"),
				new Voce<String>(Utils.getInstance(this.getLanguage()).getMessageFromResourceBundle(this.nomeServizio + ".importo.label"),
						importoPagato.doubleValue()+ "€"));

		if(entry.getAnomalie() != null && entry.getAnomalie().size() > 0){
			voci.put(Utils.getInstance(this.getLanguage()).getMessageFromResourceBundle(this.nomeServizio + ".anomalie.id"),
					new Voce<String>(Utils.getInstance(this.getLanguage()).getMessageFromResourceBundle(this.nomeServizio + ".anomalie.label"),
							""));

			for (int i = 0 ; i < entry.getAnomalie().size() ; i ++) {
				Anomalia anomalia = entry.getAnomalie().get(i);
				voci.put(Utils.getInstance(this.getLanguage()).getMessageFromResourceBundle(this.nomeServizio + ".anomalie.id") + "_"+i,
						new Voce<String>(anomalia.getCodice(),anomalia.getDescrizione()));
			}
		}
		try {
			Pagamento pagamento = entry.getPagamento(bd);
			String codDominio = null;
			if(pagamento != null) {
				codDominio = pagamento.getCodDominio();

				SingoloVersamento singoloVersamento = pagamento.getSingoloVersamento(bd);
				if(singoloVersamento != null){
					voci.put(Utils.getInstance(this.getLanguage()).getMessageFromResourceBundle(this.nomeServizio + ".codSingoloVersamento.id"),
							new Voce<String>(Utils.getInstance(this.getLanguage()).getMessageFromResourceBundle(this.nomeServizio + ".codSingoloVersamento.label"),
									singoloVersamento.getCodSingoloVersamentoEnte()));
				}

			} else {
				Fr fr = entry.getFr(bd);
				codDominio = fr.getCodDominio();
			}

			if(StringUtils.isNotEmpty(codDominio)){
				try{
					Dominio dominio = AnagraficaManager.getDominio(bd, codDominio);
					Domini dominiDars = new Domini();
					DominiHandler dominiDarsHandler =  (DominiHandler) dominiDars.getDarsHandler();
					String dominioTitolo = dominiDarsHandler.getTitolo(dominio, bd);
					voci.put(Utils.getInstance(this.getLanguage()).getMessageFromResourceBundle(this.nomeServizio + ".dominio.id"),
							new Voce<String>(Utils.getInstance(this.getLanguage()).getMessageFromResourceBundle(this.nomeServizio + ".dominio.label"),
									dominioTitolo));
				}catch(NotFoundException e){}
			}
		}catch(ServiceException e){

		}

		return voci; 

	}

	@Override
	public String esporta(List<Long> idsToExport, List<RawParamValue> rawValues, UriInfo uriInfo, BasicBD bd, ZipOutputStream zout)
			throws WebApplicationException, ConsoleException,ExportException {
		return null;
	}

	@Override
	public String esporta(Long idToExport, List<RawParamValue> rawValues, UriInfo uriInfo, BasicBD bd, ZipOutputStream zout)
			throws WebApplicationException, ConsoleException,ExportException {
		return null;
	}

	/* Creazione/Update non consentiti**/


	@Override
	public InfoForm getInfoCancellazione(UriInfo uriInfo, BasicBD bd) throws ConsoleException {
		return null;
	}

	@Override
	public InfoForm getInfoCancellazioneDettaglio(UriInfo uriInfo, BasicBD bd, Rendicontazione entry) throws ConsoleException {
		return null;
	}
	@Override
	public InfoForm getInfoEsportazione(UriInfo uriInfo, BasicBD bd) throws ConsoleException { 
		URI esportazione = this.getUriCancellazione(uriInfo, bd);
		InfoForm infoEsportazione = new InfoForm(esportazione);
		return infoEsportazione; 
	}
	
	@Override
	public InfoForm getInfoEsportazioneDettaglio(UriInfo uriInfo, BasicBD bd, Rendicontazione entry)	throws ConsoleException {	
		URI esportazione = this.getUriEsportazioneDettaglio(uriInfo, bd, entry.getId());
		InfoForm infoEsportazione = new InfoForm(esportazione);
		return infoEsportazione;		
	}
	
	@Override
	public InfoForm getInfoCreazione(UriInfo uriInfo, BasicBD bd) throws ConsoleException { return null; }

	@Override
	public InfoForm getInfoModifica(UriInfo uriInfo, BasicBD bd, Rendicontazione entry) throws ConsoleException { return null; }

	@Override
	public Elenco delete(List<Long> idsToDelete, List<RawParamValue> rawValues, UriInfo uriInfo, BasicBD bd) throws WebApplicationException, ConsoleException, DeleteException {	return null; 	}

	@Override
	public Rendicontazione creaEntry(InputStream is, UriInfo uriInfo, BasicBD bd) throws WebApplicationException, ConsoleException { return null; }

	@Override
	public Dettaglio insert(InputStream is, UriInfo uriInfo, BasicBD bd) throws WebApplicationException, ConsoleException, ValidationException, DuplicatedEntryException { return null; }

	@Override
	public void checkEntry(Rendicontazione entry, Rendicontazione oldEntry) throws ValidationException { }

	@Override
	public Dettaglio update(InputStream is, UriInfo uriInfo, BasicBD bd) throws WebApplicationException, ConsoleException, ValidationException { return null; }

	@Override
	public Object uplaod(MultipartFormDataInput input, UriInfo uriInfo, BasicBD bd)	throws WebApplicationException, ConsoleException, ValidationException { return null;}
}
