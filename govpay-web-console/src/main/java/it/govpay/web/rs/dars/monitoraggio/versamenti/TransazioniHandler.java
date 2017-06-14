package it.govpay.web.rs.dars.monitoraggio.versamenti;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.math.BigDecimal;
import java.net.URI;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
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
import org.openspcoop2.generic_project.exception.NotFoundException;
import org.openspcoop2.generic_project.exception.ServiceException;
import org.openspcoop2.generic_project.expression.SortOrder;

import it.govpay.bd.BasicBD;
import it.govpay.bd.FilterSortWrapper;
import it.govpay.bd.anagrafica.AclBD;
import it.govpay.bd.anagrafica.AnagraficaManager;
import it.govpay.bd.anagrafica.DominiBD;
import it.govpay.bd.anagrafica.filters.DominioFilter;
import it.govpay.bd.model.Canale;
import it.govpay.bd.model.Dominio;
import it.govpay.bd.model.Psp;
import it.govpay.bd.model.Rpt;
import it.govpay.bd.model.Rr;
import it.govpay.bd.model.Versamento;
import it.govpay.bd.pagamento.EventiBD;
import it.govpay.bd.pagamento.RptBD;
import it.govpay.bd.pagamento.RrBD;
import it.govpay.bd.pagamento.filters.EventiFilter;
import it.govpay.bd.pagamento.filters.RptFilter;
import it.govpay.bd.pagamento.filters.RrFilter;
import it.govpay.model.Acl;
import it.govpay.model.Evento;
import it.govpay.model.Acl.Tipo;
import it.govpay.model.Canale.ModelloPagamento;
import it.govpay.model.Canale.TipoVersamento;
import it.govpay.model.Operatore;
import it.govpay.model.Operatore.ProfiloOperatore;
import it.govpay.model.Rpt.EsitoPagamento;
import it.govpay.model.Rpt.FirmaRichiesta;
import it.govpay.model.Rpt.StatoRpt;
import it.govpay.web.rs.dars.BaseDarsHandler;
import it.govpay.web.rs.dars.BaseDarsService;
import it.govpay.web.rs.dars.IDarsHandler;
import it.govpay.web.rs.dars.anagrafica.domini.Domini;
import it.govpay.web.rs.dars.anagrafica.domini.DominiHandler;
import it.govpay.web.rs.dars.anagrafica.psp.Canali;
import it.govpay.web.rs.dars.exception.ConsoleException;
import it.govpay.web.rs.dars.exception.DeleteException;
import it.govpay.web.rs.dars.exception.DuplicatedEntryException;
import it.govpay.web.rs.dars.exception.ExportException;
import it.govpay.web.rs.dars.exception.ValidationException;
import it.govpay.web.rs.dars.model.DarsResponse.EsitoOperazione;
import it.govpay.web.rs.dars.model.InfoForm.Sezione;
import it.govpay.web.rs.dars.model.input.ParamField;
import it.govpay.web.rs.dars.model.input.base.InputText;
import it.govpay.web.rs.dars.model.input.base.SelectList;
import it.govpay.web.rs.dars.model.Dettaglio;
import it.govpay.web.rs.dars.model.Elemento;
import it.govpay.web.rs.dars.model.Elenco;
import it.govpay.web.rs.dars.model.InfoForm;
import it.govpay.web.rs.dars.model.RawParamValue;
import it.govpay.web.rs.dars.model.Voce;
import it.govpay.web.rs.dars.monitoraggio.eventi.Eventi;
import it.govpay.web.rs.dars.monitoraggio.eventi.EventiHandler;
import it.govpay.web.rs.dars.monitoraggio.pagamenti.Pagamenti;
import it.govpay.web.utils.Utils;

public class TransazioniHandler extends BaseDarsHandler<Rpt> implements IDarsHandler<Rpt>{

	private SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm"); 
	private Map<String, ParamField<?>> infoRicercaMap = null;

	public TransazioniHandler(Logger log, BaseDarsService darsService) {
		super(log, darsService);
	}

	@Override
	public Elenco getElenco(UriInfo uriInfo, BasicBD bd) throws WebApplicationException, ConsoleException {
		String methodName = "getElenco " + this.titoloServizio;
		try{	
			// Operazione consentita agli utenti registrati
			Operatore operatore = this.darsService.getOperatoreByPrincipal(bd); 
			Map<String, String> params = new HashMap<String, String>();
			URI esportazione = null;  
			Integer offset = this.getOffset(uriInfo);
			Integer limit = this.getLimit(uriInfo);

			this.log.info("Esecuzione " + methodName + " in corso...");

			RptBD rptBD = new RptBD(bd);
			boolean simpleSearch = this.containsParameter(uriInfo, BaseDarsService.SIMPLE_SEARCH_PARAMETER_ID); 



			RptFilter filter = rptBD.newFilter(simpleSearch);
			FilterSortWrapper fsw = new FilterSortWrapper();
			fsw.setField(it.govpay.orm.RPT.model().DATA_MSG_RICHIESTA);
			fsw.setSortOrder(SortOrder.DESC);
			filter.getFilterSortList().add(fsw);
			filter.setOffset(offset);
			filter.setLimit(limit);

			boolean eseguiRicerca = popoloFiltroRicerca(uriInfo, bd, operatore, params, simpleSearch, filter);
			boolean visualizzaRicerca = true;
			long count = eseguiRicerca ? rptBD.count(filter) : 0;
			if(params.size() > 0) {
				// se elemento correlato visualizza la ricerca solo se i risultati sono > del limit
				visualizzaRicerca = this.visualizzaRicerca(count, limit);
			}

			String simpleSearchPlaceholder = Utils.getInstance(this.getLanguage()).getMessageFromResourceBundle(this.nomeServizio+".simpleSearch.placeholder");
			Elenco elenco = new Elenco(this.titoloServizio, this.getInfoRicerca(uriInfo, bd,visualizzaRicerca,params),
					this.getInfoCreazione(uriInfo, bd),
					count, esportazione, this.getInfoCancellazione(uriInfo, bd),simpleSearchPlaceholder); 

			List<Rpt> rpt = eseguiRicerca ? rptBD.findAll(filter) : new ArrayList<Rpt>();

			if(rpt != null && rpt.size() > 0){
				RrBD rrBD = new RrBD(bd);
				FilterSortWrapper rrFsw = new FilterSortWrapper();
				rrFsw.setField(it.govpay.orm.RR.model().DATA_MSG_REVOCA);
				rrFsw.setSortOrder(SortOrder.DESC);

				Revoche revocheDars = new Revoche();
				RevocheHandler revocheDarsHandler = (RevocheHandler) revocheDars.getDarsHandler();

				// Indico la visualizzazione custom
				String formatter = Utils.getInstance(this.getLanguage()).getMessageFromResourceBundle(this.nomeServizio+".elenco.formatter");

				for (Rpt entry : rpt) {
					Elemento elementoRpt = this.getElemento(entry, entry.getId(), this.pathServizio,bd);
					elementoRpt.setFormatter(formatter);
					elenco.getElenco().add(elementoRpt);

					RrFilter rrFilter = rrBD.newFilter();
					rrFilter.getFilterSortList().add(rrFsw);
					rrFilter.setIdRpt(entry.getId()); 
					List<Rr> findAll = rrBD.findAll(rrFilter);
					if(findAll != null && findAll.size() > 0){
						for (Rr rr : findAll) {
							Elemento elementoRr = revocheDarsHandler.getElemento(rr, rr.getId(), revocheDars.getPathServizio(),bd);
							elementoRr.setFormatter(formatter);
							elenco.getElenco().add(elementoRr);
						}
					}
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

	private boolean popoloFiltroRicerca(UriInfo uriInfo, BasicBD bd, Operatore operatore, Map<String, String> params,
			boolean simpleSearch, RptFilter filter) throws ServiceException, NotFoundException, ConsoleException {
		ProfiloOperatore profilo = operatore.getProfilo();
		boolean isAdmin = profilo.equals(ProfiloOperatore.ADMIN);
		AclBD aclBD = new AclBD(bd);
		List<Acl> aclOperatore = aclBD.getAclOperatore(operatore.getId());
		boolean elementoCorrelato = false;
		boolean eseguiRicerca = true; // isAdmin;
		List<Long> idDomini = new ArrayList<Long>();

		String versamentoId = Utils.getInstance(this.getLanguage()).getMessageFromResourceBundle(this.nomeServizio + ".idVersamento.id");
		String idVersamento = this.getParameter(uriInfo, versamentoId, String.class);

		if(StringUtils.isNotEmpty(idVersamento)){
			filter.setIdVersamento(Long.parseLong(idVersamento)); 
			elementoCorrelato = true;
			params.put(versamentoId, idVersamento);
		}

		if(simpleSearch) {
			// simplesearch
			String simpleSearchString = this.getParameter(uriInfo, BaseDarsService.SIMPLE_SEARCH_PARAMETER_ID, String.class);
			if(StringUtils.isNotEmpty(simpleSearchString)) {
				filter.setSimpleSearchString(simpleSearchString);
				if(elementoCorrelato)
					params.put(BaseDarsService.SIMPLE_SEARCH_PARAMETER_ID, simpleSearchString);
			}
		} else {
			String idDominioId = Utils.getInstance(this.getLanguage()).getMessageFromResourceBundle(this.nomeServizio + ".idDominio.id");
			String idDominio = this.getParameter(uriInfo, idDominioId, String.class);
			if(StringUtils.isNotEmpty(idDominio)){
				long idDom = -1l;
				try{
					idDom = Long.parseLong(idDominio);
				}catch(Exception e){ idDom = -1l;	}
				if(idDom > 0){
					idDomini.add(idDom);
					filter.setIdDomini(toListCodDomini(idDomini, bd));
					if(elementoCorrelato)
						params.put(idDominioId,idDominio);
				}
			}

		}

		// SE l'operatore non e' admin vede solo le transazioni dei versamenti associati ai suoi domini
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
					filter.setIdDomini(toListCodDomini(idDomini, bd));
				}
			}
		}
		return eseguiRicerca;
	}

	private List<String > toListCodDomini(List<Long> lstCodDomini, BasicBD bd) throws ServiceException, NotFoundException {
		List<String > lst = new ArrayList<String >();
		for(Long codDominio: lstCodDomini) {
			lst.add(AnagraficaManager.getDominio(bd, codDominio).getCodDominio());
		}
		return lst;
	}

	@Override
	public Dettaglio getDettaglio(long id, UriInfo uriInfo, BasicBD bd)
			throws WebApplicationException, ConsoleException {
		String methodName = "dettaglio " + this.titoloServizio + "."+ id;

		try{
			this.log.info("Esecuzione " + methodName + " in corso...");
			// Operazione consentita agli utenti registrati
			this.darsService.getOperatoreByPrincipal(bd); 

			RptBD rptBD = new RptBD(bd);
			Rpt rpt = rptBD.getRpt(id);

			InfoForm infoModifica = null;
			InfoForm infoCancellazione = this.getInfoCancellazioneDettaglio(uriInfo, bd, rpt);
			URI esportazione = this.getUriEsportazioneDettaglio(uriInfo, rptBD, id);

			String titolo = Utils.getInstance(this.getLanguage()).getMessageFromResourceBundle(this.nomeServizio + ".dettaglioTransazione");
			Dettaglio dettaglio = new Dettaglio(titolo, esportazione, infoCancellazione, infoModifica);

			// Sezione Rpt
			it.govpay.web.rs.dars.model.Sezione sezioneRpt = dettaglio.getSezioneRoot();
			String etichettaRpt = Utils.getInstance(this.getLanguage()).getMessageFromResourceBundle(this.nomeServizio + ".sezioneRPT.titolo");
			sezioneRpt.setEtichetta(etichettaRpt); 
			
			Versamento versamento = rpt.getVersamento(bd);
			if(versamento != null){
				Versamenti versamentiDars = new Versamenti();
				VersamentiHandler versamentiDarsHandler = (VersamentiHandler) versamentiDars.getDarsHandler();
				Elemento elemento = versamentiDarsHandler.getElemento(versamento, versamento.getId(), versamentiDars.getPathServizio(), bd);				
				sezioneRpt.addVoce(Utils.getInstance(this.getLanguage()).getMessageFromResourceBundle(this.nomeServizio + ".idVersamento.label"),
						Utils.getInstance(this.getLanguage()).getMessageFromResourceBundle("commons.label.visualizza"),elemento.getUri());
			}
			
			if(StringUtils.isNotEmpty(rpt.getCodDominio())){
				try{
					Dominio dominio = AnagraficaManager.getDominio(bd, rpt.getCodDominio());
					Domini dominiDars = new Domini();
					Elemento elemento = ((DominiHandler)dominiDars.getDarsHandler()).getElemento(dominio, dominio.getId(), dominiDars.getPathServizio(), bd);
					sezioneRpt.addVoce(Utils.getInstance(this.getLanguage()).getMessageFromResourceBundle(this.nomeServizio + ".idDominio.label"),elemento.getTitolo());
					
				}catch(Exception e){
					sezioneRpt.addVoce(Utils.getInstance(this.getLanguage()).getMessageFromResourceBundle(this.nomeServizio + ".idDominio.label"),rpt.getCodDominio());
				}
			}
			if(StringUtils.isNotEmpty(rpt.getIuv()))
				sezioneRpt.addVoce(Utils.getInstance(this.getLanguage()).getMessageFromResourceBundle(this.nomeServizio + ".iuv.label"),rpt.getIuv());
			if(StringUtils.isNotEmpty(rpt.getCcp()))
				sezioneRpt.addVoce(Utils.getInstance(this.getLanguage()).getMessageFromResourceBundle(this.nomeServizio + ".ccp.label"),rpt.getCcp());

			if(rpt.getDataMsgRicevuta() != null)
				sezioneRpt.addVoce(Utils.getInstance(this.getLanguage()).getMessageFromResourceBundle(this.nomeServizio + ".dataMsgRichiesta.label"),this.sdf.format(rpt.getDataMsgRichiesta()));
			
			StatoRpt stato = rpt.getStato(); 
			sezioneRpt.addVoce(Utils.getInstance(this.getLanguage()).getMessageFromResourceBundle(this.nomeServizio + ".stato.label"),
					Utils.getInstance(this.getLanguage()).getMessageFromResourceBundle(this.nomeServizio + ".stato." + stato.name()));
			
			if(StringUtils.isNotEmpty(rpt.getDescrizioneStato()))
				sezioneRpt.addVoce(Utils.getInstance(this.getLanguage()).getMessageFromResourceBundle(this.nomeServizio + ".descrizioneStato.label"),rpt.getDescrizioneStato());

			sezioneRpt.addVoce(Utils.getInstance(this.getLanguage()).getMessageFromResourceBundle(this.nomeServizio + ".codMsgRichiesta.label"),rpt.getCodMsgRichiesta());
			
			if(rpt.getDataAggiornamento() != null)
				sezioneRpt.addVoce(Utils.getInstance(this.getLanguage()).getMessageFromResourceBundle(this.nomeServizio + ".dataAggiornamento.label"),this.sdf.format(rpt.getDataAggiornamento()));
			
			Psp psp = rpt.getPsp(bd);
			if(psp != null)
				sezioneRpt.addVoce(Utils.getInstance(this.getLanguage()).getMessageFromResourceBundle(this.nomeServizio + ".psp.label"),psp.getCodPsp());

			Canale canale = rpt.getCanale(bd);
			if(canale != null)
				sezioneRpt.addVoce(Utils.getInstance(this.getLanguage()).getMessageFromResourceBundle(this.nomeServizio + ".canale.label"),canale.getCodCanale());
			
			
	//			Intermediario intermediario = rpt.getIntermediario(bd);
	//			if(intermediario != null)
	//				sezioneRpt.addVoce(Utils.getInstance(this.getLanguage()).getMessageFromResourceBundle(this.nomeServizio + ".intermediario.label"),intermediario.getCodIntermediario());
	//
	//			Stazione stazione = rpt.getStazione(bd);
	//			if(stazione!= null)
	//				sezioneRpt.addVoce(Utils.getInstance(this.getLanguage()).getMessageFromResourceBundle(this.nomeServizio + ".stazione.label"),	stazione.getCodStazione());
	//
	//
	//
	//			Long idPortale = rpt.getIdPortale();
	//			if(idPortale != null){
	//				PortaliBD portaliBD = new PortaliBD(bd);
	//				Portale portale = portaliBD.getPortale(idPortale);
	//				sezioneRpt.addVoce(Utils.getInstance(this.getLanguage()).getMessageFromResourceBundle(this.nomeServizio + ".portale.label"),	portale.getCodPortale());
	//			}


			ModelloPagamento modelloPagamento = rpt.getModelloPagamento();
			if(modelloPagamento != null){
				String modelloPagamentoString = null;
				switch (modelloPagamento) {
				case ATTIVATO_PRESSO_PSP:
					modelloPagamentoString = Utils.getInstance(this.getLanguage()).getMessageFromResourceBundle(this.nomeServizio + ".modelloPagamento.ATTIVATO_PRESSO_PSP");
					break;
				case DIFFERITO:
					modelloPagamentoString = Utils.getInstance(this.getLanguage()).getMessageFromResourceBundle(this.nomeServizio + ".modelloPagamento.DIFFERITO");
					break;
				case IMMEDIATO:
					modelloPagamentoString = Utils.getInstance(this.getLanguage()).getMessageFromResourceBundle(this.nomeServizio + ".modelloPagamento.IMMEDIATO");
					break;
				case IMMEDIATO_MULTIBENEFICIARIO:
				default:
					modelloPagamentoString = Utils.getInstance(this.getLanguage()).getMessageFromResourceBundle(this.nomeServizio + ".modelloPagamento.IMMEDIATO_MULTIBENEFICIARIO");
					break;
				}

				sezioneRpt.addVoce(Utils.getInstance(this.getLanguage()).getMessageFromResourceBundle(this.nomeServizio + ".modelloPagamento.label"),modelloPagamentoString);
			}
			
			if(canale != null) {
				TipoVersamento tipoVersamento = canale.getTipoVersamento();
				if(tipoVersamento != null) {
					Canali canaliDars = new Canali();
					sezioneRpt.addVoce(Utils.getInstance(this.getLanguage()).getMessageFromResourceBundle(this.nomeServizio + ".tipoVersamento.label"),
							Utils.getInstance(this.getLanguage()).getMessageFromResourceBundle(canaliDars.getNomeServizio() + ".tipoVersamento."+tipoVersamento.name()));
				}
			}
			
			FirmaRichiesta firmaRichiesta = rpt.getFirmaRichiesta();
			if(firmaRichiesta != null){
				String firmaRichiestaAsString = null;

				switch (firmaRichiesta) {
				case AVANZATA:
					firmaRichiestaAsString = Utils.getInstance(this.getLanguage()).getMessageFromResourceBundle(this.nomeServizio + ".firmaRichiesta.avanzata");
					break;
				case CA_DES: 
					firmaRichiestaAsString = Utils.getInstance(this.getLanguage()).getMessageFromResourceBundle(this.nomeServizio + ".firmaRichiesta.ca_des");
					break;
				case XA_DES :
					firmaRichiestaAsString = Utils.getInstance(this.getLanguage()).getMessageFromResourceBundle(this.nomeServizio + ".firmaRichiesta.xa_des");
					break;
				case NESSUNA: 
				default:
					firmaRichiestaAsString = Utils.getInstance(this.getLanguage()).getMessageFromResourceBundle(this.nomeServizio + ".firmaRichiesta.nessuna");
					break;
				}

				sezioneRpt.addVoce(Utils.getInstance(this.getLanguage()).getMessageFromResourceBundle(this.nomeServizio + ".firmaRichiesta.label"),firmaRichiestaAsString);
			}

			if(StringUtils.isNotEmpty(rpt.getCodCarrello()))
				sezioneRpt.addVoce(Utils.getInstance(this.getLanguage()).getMessageFromResourceBundle(this.nomeServizio + ".codCarrello.label"),rpt.getCodCarrello());
			if(StringUtils.isNotEmpty(rpt.getCodSessione()))
				sezioneRpt.addVoce(Utils.getInstance(this.getLanguage()).getMessageFromResourceBundle(this.nomeServizio + ".codSessione.label"),rpt.getCodSessione());


			// Singoli Rt 
			String etichettaRt = Utils.getInstance(this.getLanguage()).getMessageFromResourceBundle(this.nomeServizio + ".sezioneRT.titolo");
			it.govpay.web.rs.dars.model.Sezione sezioneRt = dettaglio.addSezione(etichettaRt);
			if(rpt.getDataMsgRicevuta()!= null){
				sezioneRt.addVoce(Utils.getInstance(this.getLanguage()).getMessageFromResourceBundle(this.nomeServizio + ".dataMsgRicevuta.label"), this.sdf.format(rpt.getDataMsgRicevuta()));

				EsitoPagamento esitoPagamento = rpt.getEsitoPagamento();

				switch (esitoPagamento) {		
				case DECORRENZA_TERMINI:
					sezioneRt.addVoce(Utils.getInstance(this.getLanguage()).getMessageFromResourceBundle(this.nomeServizio + ".esitoPagamento.label"),
							Utils.getInstance(this.getLanguage()).getMessageFromResourceBundle(this.nomeServizio + ".esitoPagamento.DECORRENZA_TERMINI")); 
					break;
				case DECORRENZA_TERMINI_PARZIALE:
					sezioneRt.addVoce(Utils.getInstance(this.getLanguage()).getMessageFromResourceBundle(this.nomeServizio + ".esitoPagamento.label"),
							Utils.getInstance(this.getLanguage()).getMessageFromResourceBundle(this.nomeServizio + ".esitoPagamento.DECORRENZA_TERMINI_PARZIALE"));
					break;
				case PAGAMENTO_ESEGUITO:
					sezioneRt.addVoce(Utils.getInstance(this.getLanguage()).getMessageFromResourceBundle(this.nomeServizio + ".esitoPagamento.label"),
							Utils.getInstance(this.getLanguage()).getMessageFromResourceBundle(this.nomeServizio + ".esitoPagamento.PAGAMENTO_ESEGUITO"));
					break;
				case PAGAMENTO_NON_ESEGUITO:
					sezioneRt.addVoce(Utils.getInstance(this.getLanguage()).getMessageFromResourceBundle(this.nomeServizio + ".esitoPagamento.label"),
							Utils.getInstance(this.getLanguage()).getMessageFromResourceBundle(this.nomeServizio + ".esitoPagamento.PAGAMENTO_NON_ESEGUITO"));
					break;
				case PAGAMENTO_PARZIALMENTE_ESEGUITO:
				default:
					sezioneRt.addVoce(Utils.getInstance(this.getLanguage()).getMessageFromResourceBundle(this.nomeServizio + ".esitoPagamento.label"),
							Utils.getInstance(this.getLanguage()).getMessageFromResourceBundle(this.nomeServizio + ".esitoPagamento.PAGAMENTO_PARZIALMENTE_ESEGUITO"));
					break;
				}

				sezioneRt.addVoce(Utils.getInstance(this.getLanguage()).getMessageFromResourceBundle(this.nomeServizio + ".codMsgRicevuta.label"), rpt.getCodMsgRicevuta());
				sezioneRt.addVoce(Utils.getInstance(this.getLanguage()).getMessageFromResourceBundle(this.nomeServizio + ".importoTotalePagato.label"), rpt.getImportoTotalePagato() + "€");
			}
			else	{
				sezioneRt.addVoce(Utils.getInstance(this.getLanguage()).getMessageFromResourceBundle(this.nomeServizio + ".rtAssente"), null);
			}


			//Eventi correlati
			// Elementi correlati

			Pagamenti pagamentiDars = new Pagamenti();
			String idTransazioneId = Utils.getInstance(this.getLanguage()).getMessageFromResourceBundle(pagamentiDars.getNomeServizio() + ".idRpt.id");
			String etichettaPagamenti = Utils.getInstance(this.getLanguage()).getMessageFromResourceBundle(this.nomeServizio + ".elementoCorrelato.pagamenti.titolo");
			Map<String, String> params = new HashMap<String, String>();
			params.put(idTransazioneId, rpt.getId()+"");
			URI pagamentoDettaglio = Utils.creaUriConParametri(pagamentiDars.getPathServizio(), params );
			dettaglio.addElementoCorrelato(etichettaPagamenti, pagamentoDettaglio); 


			String etichettaEventi = Utils.getInstance(this.getLanguage()).getMessageFromResourceBundle(this.nomeServizio + ".elementoCorrelato.eventi.titolo");
			Eventi eventiDars = new Eventi();
			String idTransazioneIdEventi = Utils.getInstance(this.getLanguage()).getMessageFromResourceBundle(eventiDars.getNomeServizio() + ".idTransazione.id");
			String codDominioId = Utils.getInstance(this.getLanguage()).getMessageFromResourceBundle(eventiDars.getNomeServizio() + ".codDominio.id");
			String iuvId = Utils.getInstance(this.getLanguage()).getMessageFromResourceBundle(eventiDars.getNomeServizio() + ".iuv.id");
			String ccpId = Utils.getInstance(this.getLanguage()).getMessageFromResourceBundle(eventiDars.getNomeServizio() + ".ccp.id");


			Map<String, String> paramsEventi = new HashMap<String, String>();
			paramsEventi.put(codDominioId, rpt.getCodDominio());
			paramsEventi.put(iuvId, rpt.getIuv());
			paramsEventi.put(ccpId, rpt.getCcp());
			paramsEventi.put(idTransazioneIdEventi, rpt.getId()+"");
			URI eventoDettaglio = Utils.creaUriConParametri(eventiDars.getPathServizio(), paramsEventi );
			dettaglio.addElementoCorrelato(etichettaEventi, eventoDettaglio);

			this.log.info("Esecuzione " + methodName + " completata.");

			return dettaglio;
		}catch(WebApplicationException e){
			throw e;
		}catch(Exception e){
			throw new ConsoleException(e);
		}
	}

	@Override
	public String getTitolo(Rpt entry,BasicBD bd) {
		Date dataMsgRichiesta = entry.getDataMsgRichiesta();
		String iuv = entry.getIuv();
		String ccp = entry.getCcp();
		StringBuilder sb = new StringBuilder();

		String statoString = Utils.getInstance(this.getLanguage()).getMessageWithParamsFromResourceBundle(this.nomeServizio + ".label.titolo", iuv , ccp, this.sdf.format(dataMsgRichiesta)); 
		sb.append(statoString);	
		return sb.toString();
	}

	@Override
	public String getSottotitolo(Rpt entry,BasicBD bd) {
		StringBuilder sb = new StringBuilder();
		// ricevuta RT
		if(entry.getDataMsgRicevuta()!= null){
			EsitoPagamento esitoPagamento = entry.getEsitoPagamento();
			String esitoPagamentoString = null;
			switch (esitoPagamento) {		
			case DECORRENZA_TERMINI:
				esitoPagamentoString = Utils.getInstance(this.getLanguage()).getMessageFromResourceBundle(this.nomeServizio + ".esitoPagamento.DECORRENZA_TERMINI"); 
				break;
			case DECORRENZA_TERMINI_PARZIALE:
				esitoPagamentoString = Utils.getInstance(this.getLanguage()).getMessageFromResourceBundle(this.nomeServizio + ".esitoPagamento.DECORRENZA_TERMINI_PARZIALE");
				break;
			case PAGAMENTO_ESEGUITO:
				esitoPagamentoString = Utils.getInstance(this.getLanguage()).getMessageFromResourceBundle(this.nomeServizio + ".esitoPagamento.PAGAMENTO_ESEGUITO");
				break;
			case PAGAMENTO_NON_ESEGUITO:
				esitoPagamentoString = Utils.getInstance(this.getLanguage()).getMessageFromResourceBundle(this.nomeServizio + ".esitoPagamento.PAGAMENTO_NON_ESEGUITO");
				break;
			case PAGAMENTO_PARZIALMENTE_ESEGUITO:
			default:
				esitoPagamentoString = Utils.getInstance(this.getLanguage()).getMessageFromResourceBundle(this.nomeServizio + ".esitoPagamento.PAGAMENTO_PARZIALMENTE_ESEGUITO");
				break;
			}


			BigDecimal importoTotalePagato = entry.getImportoTotalePagato();
			int compareTo = importoTotalePagato.compareTo(BigDecimal.ZERO);
			if(compareTo > 0){
				sb.append(Utils.getInstance(this.getLanguage()).getMessageWithParamsFromResourceBundle(this.nomeServizio + ".label.sottotitolo.rtPresente.importoPositivo",esitoPagamentoString, ( entry.getImportoTotalePagato() + "€")));
			} else{
				sb.append(Utils.getInstance(this.getLanguage()).getMessageWithParamsFromResourceBundle(this.nomeServizio + ".label.sottotitolo.rtPresente",esitoPagamentoString));
			}

		} else {
			StatoRpt stato = entry.getStato();
			sb.append(Utils.getInstance(this.getLanguage()).getMessageWithParamsFromResourceBundle(this.nomeServizio + ".label.sottotitolo.rtAssente", Utils.getInstance(this.getLanguage()).getMessageFromResourceBundle(this.nomeServizio + ".stato." + stato.name())));
		}

		return sb.toString();
	}

	@Override
	public Map<String, Voce<String>> getVoci(Rpt entry, BasicBD bd) throws ConsoleException {
		Map<String, Voce<String>> valori = new HashMap<String, Voce<String>>();

		StatoRpt stato = entry.getStato();

		String statoTransazione = Utils.getInstance(this.getLanguage()).getMessageFromResourceBundle(this.nomeServizio + ".statoTransazione.inCorso");
		switch(stato){
		case RT_ACCETTATA_PA:
			statoTransazione = Utils.getInstance(this.getLanguage()).getMessageFromResourceBundle(this.nomeServizio + ".statoTransazione.finaleOk");
			break;
		case RPT_RIFIUTATA_NODO:
		case RPT_RIFIUTATA_PSP:
		case RPT_ERRORE_INVIO_A_PSP:
			statoTransazione = Utils.getInstance(this.getLanguage()).getMessageFromResourceBundle(this.nomeServizio + ".statoTransazione.finaleKo");
			break;
		case INTERNO_NODO:
		case RPT_ACCETTATA_NODO:
		case RPT_ACCETTATA_PSP:
		case RPT_ATTIVATA:
		case RPT_DECORSI_TERMINI:
		case RPT_ERRORE_INVIO_A_NODO:
		case RPT_INVIATA_A_PSP:
		case RPT_RICEVUTA_NODO:
		case RT_ACCETTATA_NODO:
		case RT_ESITO_SCONOSCIUTO_PA:
		case RT_RICEVUTA_NODO:
		case RT_RIFIUTATA_NODO:
		case RT_RIFIUTATA_PA:
		default:
			statoTransazione = Utils.getInstance(this.getLanguage()).getMessageFromResourceBundle(this.nomeServizio + ".statoTransazione.inCorso");
			break;
		}

		valori.put(Utils.getInstance(this.getLanguage()).getMessageFromResourceBundle(this.nomeServizio + ".statoTransazione.id"),
				new Voce<String>(Utils.getInstance(this.getLanguage()).getMessageFromResourceBundle(this.nomeServizio + ".stato." + stato.name()),
						statoTransazione));

		valori.put(Utils.getInstance(this.getLanguage()).getMessageFromResourceBundle(this.nomeServizio + ".tipo.id"),
				new Voce<String>(Utils.getInstance(this.getLanguage()).getMessageFromResourceBundle(this.nomeServizio + ".tipo.pagamento.label"),
						Utils.getInstance(this.getLanguage()).getMessageFromResourceBundle(this.nomeServizio + ".tipo.pagamento.value")));

		BigDecimal importoTotalePagato = entry.getImportoTotalePagato() != null ? entry.getImportoTotalePagato() : BigDecimal.ZERO;

		valori.put(Utils.getInstance(this.getLanguage()).getMessageFromResourceBundle(this.nomeServizio + ".importo.id"),
				new Voce<String>(Utils.getInstance(this.getLanguage()).getMessageFromResourceBundle(this.nomeServizio + ".importo.label"),
						importoTotalePagato.toString()+ "€"));

		if(entry.getDataMsgRichiesta() != null){
			valori.put(Utils.getInstance(this.getLanguage()).getMessageFromResourceBundle(this.nomeServizio + ".data.id"),
					new Voce<String>(Utils.getInstance(this.getLanguage()).getMessageFromResourceBundle(this.nomeServizio + ".data.label"),
							this.sdf.format(entry.getDataMsgRichiesta())));	 
		}

		if(entry.getIuv() != null){
			valori.put(Utils.getInstance(this.getLanguage()).getMessageFromResourceBundle(this.nomeServizio + ".iuv.id"),
					new Voce<String>(Utils.getInstance(this.getLanguage()).getMessageFromResourceBundle(this.nomeServizio + ".iuv.label"),entry.getIuv()));
		}

		if(entry.getCcp() != null){
			valori.put(Utils.getInstance(this.getLanguage()).getMessageFromResourceBundle(this.nomeServizio + ".ccp.id"),
					new Voce<String>(Utils.getInstance(this.getLanguage()).getMessageFromResourceBundle(this.nomeServizio + ".ccp.label"),entry.getCcp()));
		}

		Psp psp = null;
		try {
			psp = entry.getPsp(bd);
			if(psp != null)
				valori.put(Utils.getInstance(this.getLanguage()).getMessageFromResourceBundle(this.nomeServizio + ".psp.id"),
						new Voce<String>(Utils.getInstance(this.getLanguage()).getMessageFromResourceBundle(this.nomeServizio + ".psp.label"),psp.getCodPsp()));
		} catch (ServiceException e) {
			throw new ConsoleException(e);
		}
		
		if(StringUtils.isNotEmpty(entry.getCodDominio())){
			try{
				Dominio dominio = AnagraficaManager.getDominio(bd, entry.getCodDominio());
				Domini dominiDars = new Domini();
				Elemento elemento = ((DominiHandler)dominiDars.getDarsHandler()).getElemento(dominio, dominio.getId(), dominiDars.getPathServizio(), bd);
				valori.put(Utils.getInstance(this.getLanguage()).getMessageFromResourceBundle(this.nomeServizio + ".idDominio.id"),
						new Voce<String>(Utils.getInstance(this.getLanguage()).getMessageFromResourceBundle(this.nomeServizio + ".idDominio.label"),elemento.getTitolo()));
				
			}catch(Exception e){
				valori.put(Utils.getInstance(this.getLanguage()).getMessageFromResourceBundle(this.nomeServizio + ".idDominio.id"),
						new Voce<String>(Utils.getInstance(this.getLanguage()).getMessageFromResourceBundle(this.nomeServizio + ".idDominio.label"),entry.getCodDominio()));
			}
		}

		return valori; 

	}

	@Override
	public String esporta(List<Long> idsToExport, List<RawParamValue> rawValues, UriInfo uriInfo, BasicBD bd, ZipOutputStream zout)
			throws WebApplicationException, ConsoleException,ExportException {
		StringBuffer sb = new StringBuffer();
		if(idsToExport != null && idsToExport.size() > 0)
			for (Long long1 : idsToExport) {

				if(sb.length() > 0)
					sb.append(", ");

				sb.append(long1);
			}

		String methodName = "esporta " + this.titoloServizio + "[" + sb.toString() + "]";

		if(idsToExport == null || idsToExport.size() == 0) {
			List<String> msg = new ArrayList<String>();
			msg.add(Utils.getInstance(this.getLanguage()).getMessageFromResourceBundle(this.nomeServizio+".esporta.erroreSelezioneVuota"));
			throw new ExportException(msg, EsitoOperazione.ERRORE);
		}

		if(idsToExport.size() == 1)
			return this.esporta(idsToExport.get(0), uriInfo, bd, zout); 

		String fileName = "Transazioni.zip";
		try{
			this.log.info("Esecuzione " + methodName + " in corso...");
			this.darsService.getOperatoreByPrincipal(bd); 

			RptBD rptBD = new RptBD(bd);
			EventiBD eventiBd = new EventiBD(bd);
			Eventi eventiDars = new Eventi();
			EventiHandler eventiDarsHandler = (EventiHandler) eventiDars.getDarsHandler(); 

			for (Long idTransazione : idsToExport) {
				Rpt rpt = rptBD.getRpt(idTransazione);
				String folderName = rpt.getCodMsgRichiesta();
				String iuv = rpt.getIuv();
				String ccp = rpt.getCcp();

				ZipEntry rptXml = new ZipEntry(folderName + "/rpt.xml");
				zout.putNextEntry(rptXml);
				zout.write(rpt.getXmlRpt());
				zout.closeEntry();

				if(rpt.getXmlRt() != null){
					ZipEntry rtXml = new ZipEntry(folderName + "/rt.xml");
					zout.putNextEntry(rtXml);
					zout.write(rpt.getXmlRt());
					zout.closeEntry();
				}
				
				// Eventi
				String entryEventiCSV =  folderName + "/eventi.csv";
				
				EventiFilter eventiFilter = eventiBd.newFilter();
				Dominio dominio = AnagraficaManager.getDominio(bd, rpt.getCodDominio());
				eventiFilter.setCodDominio(dominio.getCodDominio());
				eventiFilter.setIuv(iuv);
				eventiFilter.setCcp(ccp);
				FilterSortWrapper fswEventi = new FilterSortWrapper();
				fswEventi.setField(it.govpay.orm.Evento.model().DATA_1);
				fswEventi.setSortOrder(SortOrder.ASC);
				eventiFilter.getFilterSortList().add(fswEventi);

				List<Evento> findAllEventi = eventiBd.findAll(eventiFilter);
				ByteArrayOutputStream baos = new ByteArrayOutputStream();
				eventiDarsHandler.scriviCSVEventi(baos, findAllEventi);

				ZipEntry eventiCSV = new ZipEntry(entryEventiCSV);
				zout.putNextEntry(eventiCSV);
				zout.write(baos.toByteArray());
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
			throws WebApplicationException, ConsoleException,ExportException {
		String methodName = "esporta " + this.titoloServizio + "[" + idToExport + "]";  


		try{
			this.log.info("Esecuzione " + methodName + " in corso...");
			this.darsService.getOperatoreByPrincipal(bd); 

			RptBD rptBD = new RptBD(bd);
			EventiBD eventiBd = new EventiBD(bd);
			Eventi eventiDars = new Eventi();
			EventiHandler eventiDarsHandler = (EventiHandler) eventiDars.getDarsHandler();
			Rpt rpt = rptBD.getRpt(idToExport);

			String fileName = "Transazione_"+rpt.getCodMsgRichiesta()+".zip";
			String iuv = rpt.getIuv();
			String ccp = rpt.getCcp();

			ZipEntry rptXml = new ZipEntry("rpt.xml");
			zout.putNextEntry(rptXml);
			zout.write(rpt.getXmlRpt());
			zout.closeEntry();

			if(rpt.getXmlRt() != null){
				ZipEntry rtXml = new ZipEntry("rt.xml");
				zout.putNextEntry(rtXml);
				zout.write(rpt.getXmlRt());
				zout.closeEntry();
			}
			
			// Eventi
			String entryEventiCSV =  "eventi.csv";
			
			EventiFilter eventiFilter = eventiBd.newFilter();
			Dominio dominio = AnagraficaManager.getDominio(bd, rpt.getCodDominio());
			eventiFilter.setCodDominio(dominio.getCodDominio());
			eventiFilter.setIuv(iuv);
			eventiFilter.setCcp(ccp);
			FilterSortWrapper fswEventi = new FilterSortWrapper();
			fswEventi.setField(it.govpay.orm.Evento.model().DATA_1);
			fswEventi.setSortOrder(SortOrder.ASC);
			eventiFilter.getFilterSortList().add(fswEventi);

			List<Evento> findAllEventi = eventiBd.findAll(eventiFilter);
			ByteArrayOutputStream baos = new ByteArrayOutputStream();
			eventiDarsHandler.scriviCSVEventi(baos, findAllEventi);

			ZipEntry eventiCSV = new ZipEntry(entryEventiCSV);
			zout.putNextEntry(eventiCSV);
			zout.write(baos.toByteArray());
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

	@SuppressWarnings("unchecked")
	@Override
	public InfoForm getInfoRicerca(UriInfo uriInfo, BasicBD bd, boolean visualizzaRicerca, Map<String,String> parameters) throws ConsoleException { 	
		URI ricerca =  this.getUriRicerca(uriInfo, bd, parameters);
		InfoForm infoRicerca = new InfoForm(ricerca);

		
		if(visualizzaRicerca) {
			String iuvId = Utils.getInstance(this.getLanguage()).getMessageFromResourceBundle(this.nomeServizio + ".iuv.id");
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
			
			InputText iuv = (InputText) this.infoRicercaMap.get(iuvId);
			iuv.setDefaultValue(null);
			sezioneRoot.addField(iuv);
			
		}
		return infoRicerca;
	}

	private void initInfoRicerca(UriInfo uriInfo, BasicBD bd) throws ConsoleException{
		String iuvId = Utils.getInstance(this.getLanguage()).getMessageFromResourceBundle(this.nomeServizio + ".iuv.id");
		String idDominioId = Utils.getInstance(this.getLanguage()).getMessageFromResourceBundle(this.nomeServizio + ".idDominio.id");
		
		
		if(this.infoRicercaMap == null){
			this.infoRicercaMap = new HashMap<String, ParamField<?>>();

			// iuv
			String iuvLabel = Utils.getInstance(this.getLanguage()).getMessageFromResourceBundle(this.nomeServizio + ".iuv.label");
			InputText iuv = new InputText(iuvId, iuvLabel, null, false, false, true, 1, 35);
			this.infoRicercaMap.put(iuvId, iuv);	
			
			List<Voce<Long>> domini = new ArrayList<Voce<Long>>();
			// idDominio
			String idDominioLabel = Utils.getInstance(this.getLanguage()).getMessageFromResourceBundle(this.nomeServizio + ".idDominio.label");
			SelectList<Long> idDominio = new SelectList<Long>(idDominioId, idDominioLabel, null, false, false, true, domini);
			this.infoRicercaMap.put(idDominioId, idDominio);
		}
	}

	/* Operazioni non consentite */

	@Override
	public InfoForm getInfoCancellazione(UriInfo uriInfo, BasicBD bd) throws ConsoleException {
		return null;
	}
	@Override
	public InfoForm getInfoCancellazioneDettaglio(UriInfo uriInfo, BasicBD bd, Rpt entry) throws ConsoleException {
		return null;
	}

	@Override
	public InfoForm getInfoCreazione(UriInfo uriInfo, BasicBD bd) throws ConsoleException {		return null;	}

	@Override
	public InfoForm getInfoModifica(UriInfo uriInfo, BasicBD bd, Rpt entry) throws ConsoleException {	return null;	}

	@Override
	public Object getField(UriInfo uriInfo, List<RawParamValue> values, String fieldId, BasicBD bd)	throws WebApplicationException, ConsoleException {	return null;	}

	@Override
	public Elenco delete(List<Long> idsToDelete, List<RawParamValue> rawValues, UriInfo uriInfo, BasicBD bd) throws WebApplicationException, ConsoleException, DeleteException {	return null; 	}

	@Override
	public Rpt creaEntry(InputStream is, UriInfo uriInfo, BasicBD bd) throws WebApplicationException, ConsoleException { return null;	}

	@Override
	public Dettaglio insert(InputStream is, UriInfo uriInfo, BasicBD bd) 	throws WebApplicationException, ConsoleException, ValidationException, DuplicatedEntryException {	return null;	}

	@Override
	public void checkEntry(Rpt entry, Rpt oldEntry) throws ValidationException {}

	@Override
	public Dettaglio update(InputStream is, UriInfo uriInfo, BasicBD bd) throws WebApplicationException, ConsoleException, ValidationException {		return null;	}

	@Override
	public Object uplaod(MultipartFormDataInput input, UriInfo uriInfo, BasicBD bd)	throws WebApplicationException, ConsoleException, ValidationException { return null;}

}
