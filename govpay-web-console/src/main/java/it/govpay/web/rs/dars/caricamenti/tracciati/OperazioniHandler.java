package it.govpay.web.rs.dars.caricamenti.tracciati;

import java.io.InputStream;
import java.net.URI;
import java.text.SimpleDateFormat;
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
import org.openspcoop2.generic_project.expression.SortOrder;

import it.govpay.bd.BasicBD;
import it.govpay.bd.FilterSortWrapper;
import it.govpay.bd.model.Dominio;
import it.govpay.bd.model.Operazione;
import it.govpay.bd.model.OperazioneAnnullamento;
import it.govpay.bd.model.OperazioneCaricamento;
import it.govpay.bd.model.OperazioneIncasso;
import it.govpay.bd.model.OperazioneIncasso.SingoloIncasso;
import it.govpay.bd.model.Tributo;
import it.govpay.bd.pagamento.OperazioniBD;
import it.govpay.bd.pagamento.filters.OperazioneFilter;
import it.govpay.core.business.Tracciati;
import it.govpay.core.business.model.LeggiOperazioneDTO;
import it.govpay.core.business.model.LeggiOperazioneDTOResponse;
import it.govpay.model.Applicazione;
import it.govpay.model.Operazione.StatoOperazioneType;
import it.govpay.model.Operazione.TipoOperazioneType;
import it.govpay.web.rs.dars.anagrafica.domini.Domini;
import it.govpay.web.rs.dars.anagrafica.domini.DominiHandler;
import it.govpay.web.rs.dars.anagrafica.tributi.Tributi;
import it.govpay.web.rs.dars.anagrafica.tributi.TributiHandler;
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
import it.govpay.web.rs.dars.model.input.base.SelectList;
import it.govpay.web.utils.Utils;

public class OperazioniHandler extends DarsHandler<Operazione> implements IDarsHandler<Operazione>{

	private SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm"); 

	public OperazioniHandler(Logger log, DarsService darsService) { 
		super(log, darsService);
	}

	@Override
	public Elenco getElenco(UriInfo uriInfo, BasicBD bd) throws WebApplicationException, ConsoleException {
		String methodName = "getElenco " + this.titoloServizio;
		try{
			this.log.info("Esecuzione " + methodName + " in corso...");
			// Operazione consentita solo agli utenti che hanno almeno un ruolo consentito per la funzionalita'
			this.darsService.checkDirittiServizio(bd, this.funzionalita);

			Map<String, String> params = new HashMap<String, String>();
			Integer offset = this.getOffset(uriInfo);
			Integer limit = this.getLimit(uriInfo);

			OperazioniBD operazioniBD = new OperazioniBD(bd);
			boolean simpleSearch = this.containsParameter(uriInfo, DarsService.SIMPLE_SEARCH_PARAMETER_ID); 

			OperazioneFilter filter = operazioniBD.newFilter(simpleSearch);
			filter.setOffset(offset);
			filter.setLimit(limit);
			FilterSortWrapper fsw = new FilterSortWrapper();
			fsw.setField(it.govpay.orm.Operazione.model().LINEA_ELABORAZIONE);
			fsw.setSortOrder(SortOrder.ASC);
			filter.getFilterSortList().add(fsw);

			boolean eseguiRicerca = this.popolaFiltroRicerca(uriInfo, operazioniBD, simpleSearch, filter,params);

			long count = eseguiRicerca ? operazioniBD.count(filter) : 0;
			// visualizza la ricerca solo se i risultati sono > del limit
			boolean visualizzaRicerca = this.visualizzaRicerca(count, limit);
			InfoForm infoRicerca = this.getInfoRicerca(uriInfo, bd, visualizzaRicerca,params);

			// Indico la visualizzazione custom
			String formatter = Utils.getInstance(this.getLanguage()).getMessageFromResourceBundle(this.nomeServizio+".elenco.formatter");
			String simpleSearchPlaceholder = Utils.getInstance(this.getLanguage()).getMessageFromResourceBundle(this.nomeServizio+".simpleSearch.placeholder");
			Elenco elenco = new Elenco(this.titoloServizio, infoRicerca,
					this.getInfoCreazione(uriInfo, bd),
					count, this.getInfoEsportazione(uriInfo, bd), this.getInfoCancellazione(uriInfo, bd),simpleSearchPlaceholder); 

			List<Operazione> findAll = eseguiRicerca ? operazioniBD.findAll(filter) : new ArrayList<Operazione>(); 

			if(findAll != null && findAll.size() > 0){
				for (Operazione entry : findAll) {
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


	private boolean popolaFiltroRicerca(UriInfo uriInfo, BasicBD bd,  boolean simpleSearch, OperazioneFilter filter,Map<String, String> params) throws ConsoleException, Exception {
		boolean elementoCorrelato = false;
		boolean eseguiRicerca = true;
		//		List<Long> idDomini = new ArrayList<Long>();
		//		AclBD aclBD = new AclBD(bd);
		//		List<Acl> aclOperatore = aclBD.getAclOperatore(operatore.getId());
		//		ProfiloOperatore profilo = operatore.getProfilo();
		//		boolean isAdmin = profilo.equals(ProfiloOperatore.ADMIN);

		String tracciatoId = Utils.getInstance(this.getLanguage()).getMessageFromResourceBundle(this.nomeServizio + ".idTracciato.id");
		String idTracciato = this.getParameter(uriInfo, tracciatoId, String.class);

		// elemento correlato tracciato
		if(StringUtils.isNotEmpty(idTracciato)){
			params.put(tracciatoId, idTracciato);
			elementoCorrelato = true;
			filter.setIdTracciato(Long.parseLong(idTracciato));
		}

		if(simpleSearch) {
			// simplesearch
			String simpleSearchString = this.getParameter(uriInfo, DarsService.SIMPLE_SEARCH_PARAMETER_ID, String.class);
			if(StringUtils.isNotEmpty(simpleSearchString)) {
				filter.setSimpleSearchString(simpleSearchString);
				if(elementoCorrelato)
					params.put(DarsService.SIMPLE_SEARCH_PARAMETER_ID, simpleSearchString);
			}
		} else {
			String statoId = Utils.getInstance(this.getLanguage()).getMessageFromResourceBundle(this.nomeServizio + ".stato.id");
			String stato = this.getParameter(uriInfo, statoId, String.class);

			if(StringUtils.isNotEmpty(stato)){
				filter.setStato(StatoOperazioneType.valueOf(stato));
			}

			//			String idDominioId = Utils.getInstance(this.getLanguage()).getMessageFromResourceBundle(this.nomeServizio + ".idDominio.id");
			//			String idDominio = this.getParameter(uriInfo, idDominioId, String.class);
			//			if(StringUtils.isNotEmpty(idDominio)){
			//				long idDom = -1l;
			//				try{
			//					idDom = Long.parseLong(idDominio);
			//				}catch(Exception e){ idDom = -1l;	}
			//				if(idDom > 0){
			//					idDomini.add(idDom);
			//					filter.setIdDomini(toListCodDomini(idDomini, bd));
			//					if(elementoCorrelato)
			//						params.put(idDominioId,idDominio);
			//				}
			//			}
		}

		//		if(!isAdmin && idDomini.isEmpty()){
		//			boolean vediTuttiDomini = false;
		//
		//			for(Acl acl: aclOperatore) {
		//				if(Tipo.DOMINIO.equals(acl.getTipo())) {
		//					if(acl.getIdDominio() == null) {
		//						vediTuttiDomini = true;
		//						break;
		//					} else {
		//						idDomini.add(acl.getIdDominio());
		//					}
		//				}
		//			}
		//			if(!vediTuttiDomini) {
		//				if(idDomini.isEmpty()) {
		//					eseguiRicerca = false;
		//				} else {
		//					filter.setIdDomini(toListCodDomini(idDomini, bd));
		//				}
		//			}
		//		}
		return eseguiRicerca;
	}

	//	private List<String > toListCodDomini(List<Long> lstCodDomini, BasicBD bd) throws ServiceException, NotFoundException {
	//		List<String > lst = new ArrayList<String >();
	//		for(Long codDominio: lstCodDomini) {
	//			lst.add(AnagraficaManager.getDominio(bd, codDominio).getCodDominio());
	//		}
	//		return lst;
	//	}

	@SuppressWarnings("unchecked")
	@Override
	public InfoForm getInfoRicerca(UriInfo uriInfo, BasicBD bd, boolean visualizzaRicerca,
			Map<String, String> parameters) throws ConsoleException {
		URI ricerca =  this.getUriRicerca(uriInfo, bd, parameters);
		InfoForm infoRicerca = new InfoForm(ricerca);

		if(visualizzaRicerca) {
			String statoId = Utils.getInstance(this.getLanguage()).getMessageFromResourceBundle(this.nomeServizio + ".stato.id");

			if(this.infoRicercaMap == null){
				this.initInfoRicerca(uriInfo, bd);
			}

			Sezione sezioneRoot = infoRicerca.getSezioneRoot();

			SelectList<String> stato = (SelectList<String>) this.infoRicercaMap.get(statoId);
			stato.setDefaultValue(null);
			sezioneRoot.addField(stato);

		}
		return infoRicerca;
	}

	private void initInfoRicerca(UriInfo uriInfo, BasicBD bd) throws ConsoleException{
		if(this.infoRicercaMap == null){
			this.infoRicercaMap = new HashMap<String, ParamField<?>>();

			String statoId = Utils.getInstance(this.getLanguage()).getMessageFromResourceBundle(this.nomeServizio + ".stato.id");

			// stato
			String statoLabel = Utils.getInstance(this.getLanguage()).getMessageFromResourceBundle(this.nomeServizio + ".stato.label");
			List<Voce<String>> stati = new ArrayList<Voce<String>>();

			stati.add(new Voce<String>(Utils.getInstance(this.getLanguage()).getMessageFromResourceBundle("commons.label.qualsiasi"), ""));
			stati.add(new Voce<String>(Utils.getInstance(this.getLanguage()).getMessageFromResourceBundle(this.nomeServizio + ".stato."+StatoOperazioneType.ESEGUITO_OK.name()), StatoOperazioneType.ESEGUITO_OK.name()));
			stati.add(new Voce<String>(Utils.getInstance(this.getLanguage()).getMessageFromResourceBundle(this.nomeServizio + ".stato."+StatoOperazioneType.ESEGUITO_KO.name()), StatoOperazioneType.ESEGUITO_KO.name()));
			stati.add(new Voce<String>(Utils.getInstance(this.getLanguage()).getMessageFromResourceBundle(this.nomeServizio + ".stato."+StatoOperazioneType.NON_VALIDO.name()), StatoOperazioneType.NON_VALIDO.name()));
			SelectList<String> stato  = new SelectList<String>(statoId, statoLabel, null, false, false, true, stati );
			this.infoRicercaMap.put(statoId, stato);

		}
	}

	@Override
	public InfoForm getInfoCreazione(UriInfo uriInfo, BasicBD bd) throws ConsoleException {
		return null;
	}

	@Override
	public InfoForm getInfoModifica(UriInfo uriInfo, BasicBD bd, Operazione entry) throws ConsoleException {
		return null;
	}

	@Override
	public InfoForm getInfoCancellazione(UriInfo uriInfo, BasicBD bd, Map<String, String> parameters) throws ConsoleException {
		return null;
	}

	@Override
	public InfoForm getInfoCancellazioneDettaglio(UriInfo uriInfo, BasicBD bd, Operazione entry)
			throws ConsoleException {
		return null;
	}
	@Override
	public InfoForm getInfoEsportazione(UriInfo uriInfo, BasicBD bd, Map<String, String> parameters) throws ConsoleException { return null; }

	@Override
	public InfoForm getInfoEsportazioneDettaglio(UriInfo uriInfo, BasicBD bd, Operazione entry)	throws ConsoleException {	
		return null;	}

	@Override
	public Object getField(UriInfo uriInfo, List<RawParamValue> values, String fieldId, BasicBD bd)
			throws WebApplicationException, ConsoleException {
		return null;
	}

	@Override
	public Object getSearchField(UriInfo uriInfo, List<RawParamValue> values, String fieldId, BasicBD bd)	throws WebApplicationException, ConsoleException { 	return null; }

	@Override
	public Object getDeleteField(UriInfo uriInfo, List<RawParamValue> values, String fieldId, BasicBD bd) throws WebApplicationException, ConsoleException { return null; }

	@Override
	public Object getExportField(UriInfo uriInfo, List<RawParamValue> values, String fieldId, BasicBD bd) throws WebApplicationException, ConsoleException { return null; }

	@Override
	public Dettaglio getDettaglio(long id, UriInfo uriInfo, BasicBD bd)
			throws WebApplicationException, ConsoleException {
		String methodName = "dettaglio " + this.titoloServizio + ".Id "+ id;
		try{
			this.log.info("Esecuzione " + methodName + " in corso...");
			// Operazione consentita solo all'operatore con ruolo autorizzato
			this.darsService.checkDirittiServizioLettura(bd, this.funzionalita); 

			Tracciati operazioniBD = new Tracciati(bd);
			LeggiOperazioneDTO leggiOperazioneDTO = new LeggiOperazioneDTO();
			leggiOperazioneDTO.setId(id);
			leggiOperazioneDTO.setOperatore(this.darsService.getOperatoreByPrincipal(bd));
			LeggiOperazioneDTOResponse leggiOperazioneDTOResponse = operazioniBD.leggiOperazione(leggiOperazioneDTO);

			Operazione entry = leggiOperazioneDTOResponse.getOperazione();

			InfoForm infoModifica = null;
			InfoForm infoCancellazione = null;
			InfoForm infoEsportazione = null;

			String titolo = Utils.getInstance(this.getLanguage()).getMessageFromResourceBundle(this.nomeServizio + ".dettaglioOperazione") ;
			Dettaglio dettaglio = new Dettaglio(titolo, infoEsportazione, infoCancellazione, infoModifica);

			if(entry != null){
				it.govpay.web.rs.dars.model.Sezione root = dettaglio.getSezioneRoot();

				if(entry.getStato() != null)
				root.getVoci().add(new Voce<String>(Utils.getInstance(this.getLanguage()).getMessageFromResourceBundle(this.nomeServizio + ".stato.label"),  
						Utils.getInstance(this.getLanguage()).getMessageFromResourceBundle(this.nomeServizio + ".stato."+entry.getStato().name())));

				if(entry.getTipoOperazione() != null)
					root.getVoci().add(new Voce<String>(Utils.getInstance(this.getLanguage()).getMessageFromResourceBundle(this.nomeServizio + ".tipoOperazione.label"), 
							Utils.getInstance(this.getLanguage()).getMessageFromResourceBundle(this.nomeServizio + ".tipoOperazione."+entry.getTipoOperazione())));

				if(StringUtils.isNotEmpty(entry.getCodVersamentoEnte()))
					root.getVoci().add(new Voce<String>(Utils.getInstance(this.getLanguage()).getMessageFromResourceBundle(this.nomeServizio + ".idVersamento.label"), entry.getCodVersamentoEnte()));

				try{
					Applicazione applicazione = entry.getApplicazione(bd);
					if(applicazione != null){
						root.getVoci().add(new Voce<String>(Utils.getInstance(this.getLanguage()).getMessageFromResourceBundle(this.nomeServizio + ".applicazione.label"), applicazione.getCodApplicazione()));
					}
				} catch(Exception e){		}
				if(StringUtils.isNotEmpty(entry.getDettaglioEsito()))
					root.getVoci().add(new Voce<String>(Utils.getInstance(this.getLanguage()).getMessageFromResourceBundle(this.nomeServizio + ".dettaglioEsito.label"), entry.getDettaglioEsito()));

				it.govpay.web.rs.dars.model.Sezione sezioneDatiRichiesta = new it.govpay.web.rs.dars.model.Sezione(Utils.getInstance(this.getLanguage()).getMessageFromResourceBundle(this.nomeServizio + ".sezioneDatiRichiesta.label"));

				List<it.govpay.web.rs.dars.model.Sezione> datiRisposta = new ArrayList<it.govpay.web.rs.dars.model.Sezione>();
				boolean addSezioneRisposta = false;
				if(entry.getTipoOperazione().equals(TipoOperazioneType.ADD)){
					OperazioneCaricamento opCaricamento = (OperazioneCaricamento) entry;

					long idDominio = -1;
					if(StringUtils.isNotEmpty(opCaricamento.getCodDominio())){
						try{
							Dominio dominio = opCaricamento.getDominio(bd);
							idDominio = dominio.getId();
							Domini dominiDars = new Domini();
							Elemento elemento = ((DominiHandler)dominiDars.getDarsHandler()).getElemento(dominio, dominio.getId(), dominiDars.getPathServizio(), bd);
							sezioneDatiRichiesta.addVoce(Utils.getInstance(this.getLanguage()).getMessageFromResourceBundle(this.nomeServizio + ".codDominio.label"), elemento.getTitolo());
						}catch(Exception e){
							sezioneDatiRichiesta.addVoce(Utils.getInstance(this.getLanguage()).getMessageFromResourceBundle(this.nomeServizio + ".codDominio.label"), opCaricamento.getCodDominio());
						}
					}
					if(StringUtils.isNotEmpty(opCaricamento.getCodTributo())){
						try{
							Tributo tributo = opCaricamento.getTributo(operazioniBD, idDominio);
							Tributi trDars =  new Tributi();
							Elemento elemento = ((TributiHandler)trDars.getDarsHandler()).getElemento(tributo, tributo.getId(), trDars.getPathServizio(), bd);
							sezioneDatiRichiesta.addVoce(Utils.getInstance(this.getLanguage()).getMessageFromResourceBundle(this.nomeServizio + ".codTributo.label"), elemento.getTitolo());
						}catch (Exception e) {
							sezioneDatiRichiesta.addVoce(Utils.getInstance(this.getLanguage()).getMessageFromResourceBundle(this.nomeServizio + ".codTributo.label"), opCaricamento.getCodTributo());
						}
					}

					if(StringUtils.isNotEmpty(opCaricamento.getCfDebitore()))
						sezioneDatiRichiesta.addVoce(Utils.getInstance(this.getLanguage()).getMessageFromResourceBundle(this.nomeServizio + ".codiceFiscaleDebitore.label"), opCaricamento.getCfDebitore());
					if(StringUtils.isNotEmpty(opCaricamento.getAnagraficaDebitore()))
						sezioneDatiRichiesta.addVoce(Utils.getInstance(this.getLanguage()).getMessageFromResourceBundle(this.nomeServizio + ".anagraficaDebitore.label"), opCaricamento.getAnagraficaDebitore());
					
					String indirizzoDebitore = StringUtils.isNotEmpty(opCaricamento.getDebitoreIndirizzo()) ? opCaricamento.getDebitoreIndirizzo() : "";
					String civicoDebitore = StringUtils.isNotEmpty(opCaricamento.getDebitoreCivico()) ? opCaricamento.getDebitoreCivico() : "";
					String capDebitore = StringUtils.isNotEmpty(opCaricamento.getDebitoreCap()) ? opCaricamento.getDebitoreCap() : "";
					String localitaDebitore = StringUtils.isNotEmpty(opCaricamento.getDebitoreLocalita()) ? opCaricamento.getDebitoreLocalita() : "";
					String provinciaDebitore = StringUtils.isNotEmpty(opCaricamento.getDebitoreProvincia()) ? (" (" +opCaricamento.getDebitoreProvincia() +")" ) : "";
					String indirizzoCivicoDebitore = indirizzoDebitore + " " + civicoDebitore;
					String capCittaDebitore = capDebitore + " " + localitaDebitore + provinciaDebitore;
					
					if(StringUtils.isNotEmpty(indirizzoCivicoDebitore))
						sezioneDatiRichiesta.addVoce(Utils.getInstance(this.getLanguage()).getMessageFromResourceBundle(this.nomeServizio + ".debitoreIndirizzo1.label"), indirizzoCivicoDebitore);
					if(StringUtils.isNotEmpty(capCittaDebitore))
						sezioneDatiRichiesta.addVoce(Utils.getInstance(this.getLanguage()).getMessageFromResourceBundle(this.nomeServizio + ".debitoreIndirizzo2.label"), capCittaDebitore);
					
					if(opCaricamento.getImporto() != null) {
						sezioneDatiRichiesta.addVoce(Utils.getInstance(this.getLanguage()).getMessageFromResourceBundle(this.nomeServizio + ".importo.label"), this.currencyUtils.getCurrencyAsEuro(opCaricamento.getImporto()));
					}

					if(StringUtils.isNotEmpty(opCaricamento.getCausale()))
						sezioneDatiRichiesta.addVoce(Utils.getInstance(this.getLanguage()).getMessageFromResourceBundle(this.nomeServizio + ".causale.label"), opCaricamento.getCausale());
					if(opCaricamento.getScadenza() != null) {
						sezioneDatiRichiesta.addVoce(Utils.getInstance(this.getLanguage()).getMessageFromResourceBundle(this.nomeServizio + ".dataScadenza.label"), this.sdf.format(opCaricamento.getScadenza()));
					}
					if(StringUtils.isNotEmpty(opCaricamento.getBundleKey()))
						sezioneDatiRichiesta.addVoce(Utils.getInstance(this.getLanguage()).getMessageFromResourceBundle(this.nomeServizio + ".bundleKey.label"), opCaricamento.getBundleKey());
					if(StringUtils.isNotEmpty(opCaricamento.getIdDebito()))
						sezioneDatiRichiesta.addVoce(Utils.getInstance(this.getLanguage()).getMessageFromResourceBundle(this.nomeServizio + ".identificativoDebito.label"), opCaricamento.getIdDebito());
					if(StringUtils.isNotEmpty(opCaricamento.getNote()))
						sezioneDatiRichiesta.addVoce(Utils.getInstance(this.getLanguage()).getMessageFromResourceBundle(this.nomeServizio + ".note.label"), opCaricamento.getNote());
					
					it.govpay.web.rs.dars.model.Sezione sezioneDatiRisposta = new it.govpay.web.rs.dars.model.Sezione(Utils.getInstance(this.getLanguage()).getMessageFromResourceBundle(this.nomeServizio + ".sezioneDatiRisposta.label"));
					datiRisposta.add(sezioneDatiRisposta);
					if(StringUtils.isNotEmpty(opCaricamento.getIuv())){
						sezioneDatiRisposta.addVoce(Utils.getInstance(this.getLanguage()).getMessageFromResourceBundle(this.nomeServizio + ".iuv.label"), opCaricamento.getIuv());
						addSezioneRisposta = true;
					}
					if(opCaricamento.getBarCode()!= null){
						String barCode = new String(opCaricamento.getBarCode()); 
						sezioneDatiRisposta.addVoce(Utils.getInstance(this.getLanguage()).getMessageFromResourceBundle(this.nomeServizio + ".barCode.label"), barCode);
						addSezioneRisposta = true;
					}
					if(opCaricamento.getQrCode() != null){
						String qrCode = new String(opCaricamento.getQrCode()); 
						sezioneDatiRisposta.addVoce(Utils.getInstance(this.getLanguage()).getMessageFromResourceBundle(this.nomeServizio + ".qrCode.label"), qrCode);
						addSezioneRisposta = true;
					}
					
					
				} else if(entry.getTipoOperazione().equals(TipoOperazioneType.DEL)){
					OperazioneAnnullamento opAnnullamento = (OperazioneAnnullamento) entry;

					if(StringUtils.isNotEmpty(opAnnullamento.getMotivoAnnullamento()))
						sezioneDatiRichiesta.addVoce(Utils.getInstance(this.getLanguage()).getMessageFromResourceBundle(this.nomeServizio + ".motivoAnnullamento.label"), opAnnullamento.getMotivoAnnullamento());
				} else if(entry.getTipoOperazione().equals(TipoOperazioneType.INC)){
					OperazioneIncasso opIncasso = (OperazioneIncasso) entry;
					
					if(StringUtils.isNotEmpty(opIncasso.getCodDominio())){
						try{
							Dominio dominio = opIncasso.getDominio(bd);
							Domini dominiDars = new Domini();
							Elemento elemento = ((DominiHandler)dominiDars.getDarsHandler()).getElemento(dominio, dominio.getId(), dominiDars.getPathServizio(), bd);
							sezioneDatiRichiesta.addVoce(Utils.getInstance(this.getLanguage()).getMessageFromResourceBundle(this.nomeServizio + ".codDominio.label"), elemento.getTitolo());
						}catch(Exception e){
							sezioneDatiRichiesta.addVoce(Utils.getInstance(this.getLanguage()).getMessageFromResourceBundle(this.nomeServizio + ".codDominio.label"), opIncasso.getCodDominio());
						}
					}
					
					if(StringUtils.isNotEmpty(opIncasso.getDispositivo()))
						sezioneDatiRichiesta.addVoce(Utils.getInstance(this.getLanguage()).getMessageFromResourceBundle(this.nomeServizio + ".dispositivo.label"), opIncasso.getDispositivo());
					if(StringUtils.isNotEmpty(opIncasso.getTrn()))
						sezioneDatiRichiesta.addVoce(Utils.getInstance(this.getLanguage()).getMessageFromResourceBundle(this.nomeServizio + ".trn.label"), opIncasso.getTrn());
					if(StringUtils.isNotEmpty(opIncasso.getCausale()))
						sezioneDatiRichiesta.addVoce(Utils.getInstance(this.getLanguage()).getMessageFromResourceBundle(this.nomeServizio + ".causale.label"), opIncasso.getCausale());
					if(opIncasso.getImporto() != null) {
						sezioneDatiRichiesta.addVoce(Utils.getInstance(this.getLanguage()).getMessageFromResourceBundle(this.nomeServizio + ".importo.label"), this.currencyUtils.getCurrencyAsEuro(opIncasso.getImporto()));
					}
					if(opIncasso.getDataContabile() != null) {
						sezioneDatiRichiesta.addVoce(Utils.getInstance(this.getLanguage()).getMessageFromResourceBundle(this.nomeServizio + ".dataContabile.label"), this.sdf.format(opIncasso.getDataContabile()));
					}
					if(opIncasso.getDataValuta() != null) {
						sezioneDatiRichiesta.addVoce(Utils.getInstance(this.getLanguage()).getMessageFromResourceBundle(this.nomeServizio + ".dataValuta.label"), this.sdf.format(opIncasso.getDataValuta()));
					}
					
					if(opIncasso.getListaSingoloIncasso() != null && opIncasso.getListaSingoloIncasso().size() > 0 || 
							(StringUtils.isNotEmpty(opIncasso.getFaultCode()) || StringUtils.isNotEmpty(opIncasso.getFaultDescription())
									|| StringUtils.isNotEmpty(opIncasso.getFaultString()))) {
						addSezioneRisposta = true;
						it.govpay.web.rs.dars.model.Sezione sezioneDatiRisposta = 
								new it.govpay.web.rs.dars.model.Sezione(Utils.getInstance(this.getLanguage()).getMessageFromResourceBundle(this.nomeServizio + ".sezioneDatiRisposta.label"));
					
						sezioneDatiRisposta.getVoci().add(new Voce<String>(Utils.getInstance(this.getLanguage()).getMessageFromResourceBundle(this.nomeServizio + ".stato.label"),  
								Utils.getInstance(this.getLanguage()).getMessageFromResourceBundle(this.nomeServizio + ".stato."+opIncasso.getStato().name())));
						
						if(StringUtils.isNotEmpty(opIncasso.getFaultCode()))
							sezioneDatiRisposta.addVoce(Utils.getInstance(this.getLanguage()).getMessageFromResourceBundle(this.nomeServizio + ".faultCode.label"), opIncasso.getFaultCode());
						if(StringUtils.isNotEmpty(opIncasso.getFaultString()))
							sezioneDatiRisposta.addVoce(Utils.getInstance(this.getLanguage()).getMessageFromResourceBundle(this.nomeServizio + ".faultString.label"), opIncasso.getFaultString());
						if(StringUtils.isNotEmpty(opIncasso.getFaultDescription()))
							sezioneDatiRisposta.addVoce(Utils.getInstance(this.getLanguage()).getMessageFromResourceBundle(this.nomeServizio + ".faultDescription.label"), opIncasso.getFaultDescription());
						
						datiRisposta.add(sezioneDatiRisposta);
					}
					
					
					if(opIncasso.getListaSingoloIncasso() != null && opIncasso.getListaSingoloIncasso().size() > 0) {

						for (SingoloIncasso singoloIncasso : opIncasso.getListaSingoloIncasso()) {
							StatoOperazioneType stato = singoloIncasso.getStato();
							
							it.govpay.web.rs.dars.model.Sezione sezioneDatiRisposta = 
									new it.govpay.web.rs.dars.model.Sezione(Utils.getInstance(this.getLanguage()).getMessageWithParamsFromResourceBundle(this.nomeServizio + ".sezioneDatiRispostaSingoloIncasso.label",
											singoloIncasso.getTrn()));
						
							sezioneDatiRisposta.getVoci().add(new Voce<String>(Utils.getInstance(this.getLanguage()).getMessageFromResourceBundle(this.nomeServizio + ".stato.label"),  
									Utils.getInstance(this.getLanguage()).getMessageFromResourceBundle(this.nomeServizio + ".stato."+stato.name())));
							
							switch (stato) {
							case ESEGUITO_KO:
//								if(StringUtils.isNotEmpty(singoloIncasso.getFaultCode()))
//									sezioneDatiRisposta.addVoce(Utils.getInstance(this.getLanguage()).getMessageFromResourceBundle(this.nomeServizio + ".faultCode.label"), singoloIncasso.getFaultCode());
//								if(StringUtils.isNotEmpty(singoloIncasso.getFaultString()))
//									sezioneDatiRisposta.addVoce(Utils.getInstance(this.getLanguage()).getMessageFromResourceBundle(this.nomeServizio + ".faultString.label"), singoloIncasso.getFaultString());
//								if(StringUtils.isNotEmpty(singoloIncasso.getFaultDescription()))
//									sezioneDatiRisposta.addVoce(Utils.getInstance(this.getLanguage()).getMessageFromResourceBundle(this.nomeServizio + ".faultDescription.label"), singoloIncasso.getFaultDescription());
								break;
							case ESEGUITO_OK:
								if(StringUtils.isNotEmpty(singoloIncasso.getCodVersamentoEnte()))
									sezioneDatiRisposta.addVoce(Utils.getInstance(this.getLanguage()).getMessageFromResourceBundle(this.nomeServizio + ".codVersamentoEnte.label"), singoloIncasso.getCodVersamentoEnte());
								if(StringUtils.isNotEmpty(singoloIncasso.getCodSingoloVersamentoEnte()))
									sezioneDatiRisposta.addVoce(Utils.getInstance(this.getLanguage()).getMessageFromResourceBundle(this.nomeServizio + ".codSingoloVersamentoEnte.label"), singoloIncasso.getCodSingoloVersamentoEnte());
								if(StringUtils.isNotEmpty(singoloIncasso.getIuv()))
									sezioneDatiRisposta.addVoce(Utils.getInstance(this.getLanguage()).getMessageFromResourceBundle(this.nomeServizio + ".iuv.label"), singoloIncasso.getIuv());
								if(StringUtils.isNotEmpty(singoloIncasso.getIur()))
									sezioneDatiRisposta.addVoce(Utils.getInstance(this.getLanguage()).getMessageFromResourceBundle(this.nomeServizio + ".iur.label"), singoloIncasso.getIur());
								if(singoloIncasso.getDataPagamento() != null) {
									sezioneDatiRisposta.addVoce(Utils.getInstance(this.getLanguage()).getMessageFromResourceBundle(this.nomeServizio + ".dataPagamento.label"), this.sdf.format(singoloIncasso.getDataPagamento()));
								}
								if(singoloIncasso.getImporto() != null) {
									sezioneDatiRisposta.addVoce(Utils.getInstance(this.getLanguage()).getMessageFromResourceBundle(this.nomeServizio + ".importo.label"), this.currencyUtils.getCurrencyAsEuro(singoloIncasso.getImporto()));
								}
								
								break;
							case NON_VALIDO:
							default:
								break;
							}
							
							datiRisposta.add(sezioneDatiRisposta);
						}
					}
					
				} else {
					// non valido non ha dati
				}

				dettaglio.getSezioni().add(sezioneDatiRichiesta );
				if(addSezioneRisposta)
					dettaglio.getSezioni().addAll(datiRisposta);
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
	public Elenco delete(List<Long> idsToDelete, List<RawParamValue> rawValues, UriInfo uriInfo, BasicBD bd)
			throws WebApplicationException, ConsoleException, DeleteException {
		return null;
	}

	@Override
	public Operazione creaEntry(InputStream is, UriInfo uriInfo, BasicBD bd)
			throws WebApplicationException, ConsoleException {
		return null;
	}

	@Override
	public Dettaglio insert(InputStream is, UriInfo uriInfo, BasicBD bd)
			throws WebApplicationException, ConsoleException, ValidationException, DuplicatedEntryException {
		return null;
	}

	@Override
	public void checkEntry(Operazione entry, Operazione oldEntry) throws ValidationException {
	}

	@Override
	public Dettaglio update(InputStream is, UriInfo uriInfo, BasicBD bd)
			throws WebApplicationException, ConsoleException, ValidationException {
		return null;
	}

	@Override
	public Object uplaod(MultipartFormDataInput input, UriInfo uriInfo, BasicBD bd)
			throws WebApplicationException, ConsoleException, ValidationException {
		return null;
	}

	@Override
	public String getTitolo(Operazione entry, BasicBD bd) throws ConsoleException {
		return null;
	}

	@Override
	public String getSottotitolo(Operazione entry, BasicBD bd) throws ConsoleException {
		return null;
	}

	@Override
	public String esporta(List<Long> idsToExport, List<RawParamValue> rawValues, UriInfo uriInfo, BasicBD bd,
			ZipOutputStream zout) throws WebApplicationException, ConsoleException, ExportException {
		return null;
	}

	@Override
	public String esporta(Long idToExport, List<RawParamValue> rawValues, UriInfo uriInfo, BasicBD bd, ZipOutputStream zout)
			throws WebApplicationException, ConsoleException, ExportException {
		return null;
	}

	@Override
	public Map<String, Voce<String>> getVoci(Operazione entry, BasicBD bd) throws ConsoleException {
		Map<String, Voce<String>> voci = new HashMap<String, Voce<String>>();

		voci.put(Utils.getInstance(this.getLanguage()).getMessageFromResourceBundle(this.nomeServizio + ".stato.id"),
				new Voce<String>(Utils.getInstance(this.getLanguage()).getMessageFromResourceBundle(this.nomeServizio + ".stato."+entry.getStato().name()),
						entry.getStato().name()));

		if(entry.getTipoOperazione()!= null)
			voci.put(Utils.getInstance(this.getLanguage()).getMessageFromResourceBundle(this.nomeServizio + ".tipoOperazione.id"),
					new Voce<String>(Utils.getInstance(this.getLanguage()).getMessageFromResourceBundle(this.nomeServizio + ".tipoOperazione.label"),
							Utils.getInstance(this.getLanguage()).getMessageFromResourceBundle(this.nomeServizio + ".tipoOperazione." + entry.getTipoOperazione())));
		if(StringUtils.isNotEmpty(entry.getCodVersamentoEnte()))
			voci.put(Utils.getInstance(this.getLanguage()).getMessageFromResourceBundle(this.nomeServizio + ".idVersamento.id"),
					new Voce<String>(Utils.getInstance(this.getLanguage()).getMessageFromResourceBundle(this.nomeServizio + ".idVersamento.label"),
							entry.getCodVersamentoEnte()));
		try{
			Applicazione applicazione = entry.getApplicazione(bd);
			if(applicazione != null){
				voci.put(Utils.getInstance(this.getLanguage()).getMessageFromResourceBundle(this.nomeServizio + ".applicazione.id"),
						new Voce<String>(Utils.getInstance(this.getLanguage()).getMessageFromResourceBundle(this.nomeServizio + ".applicazione.label"),
								applicazione.getCodApplicazione()));
			}
		} catch(Exception e){		}


		if(entry.getStato() != null && !entry.getStato().equals(StatoOperazioneType.ESEGUITO_OK) && StringUtils.isNotEmpty(entry.getDettaglioEsito())){ 
			voci.put(Utils.getInstance(this.getLanguage()).getMessageFromResourceBundle(this.nomeServizio + ".anomalie.id"),
					new Voce<String>(Utils.getInstance(this.getLanguage()).getMessageFromResourceBundle(this.nomeServizio + ".anomalie.label"),
							""));

			voci.put(Utils.getInstance(this.getLanguage()).getMessageFromResourceBundle(this.nomeServizio + ".anomalie.id") + "_0",
					new Voce<String>(Utils.getInstance(this.getLanguage()).getMessageFromResourceBundle(this.nomeServizio + ".stato."+entry.getStato().name()),entry.getDettaglioEsito()));
		}

		return voci;
	}

}
