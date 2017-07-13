package it.govpay.web.rs.dars.caricamenti.tracciati;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.net.URI;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.UriInfo;

import org.apache.commons.codec.binary.Base64;
import org.apache.commons.lang.StringUtils;
import org.apache.logging.log4j.Logger;
import org.jboss.resteasy.plugins.providers.multipart.MultipartFormDataInput;
import org.openspcoop2.generic_project.exception.ServiceException;
import org.openspcoop2.generic_project.expression.SortOrder;

import it.govpay.bd.BasicBD;
import it.govpay.bd.FilterSortWrapper;
import it.govpay.bd.anagrafica.OperatoriBD;
import it.govpay.bd.model.Tracciato;
import it.govpay.bd.pagamento.TracciatiBD;
import it.govpay.bd.pagamento.filters.TracciatoFilter;
import it.govpay.core.business.model.InserisciTracciatoDTO;
import it.govpay.core.business.model.InserisciTracciatoDTOResponse;
import it.govpay.core.business.Tracciati;
import it.govpay.model.Operatore;
import it.govpay.model.Tracciato.StatoTracciatoType;
import it.govpay.web.rs.dars.base.BaseDarsService;
import it.govpay.web.rs.dars.base.DarsHandler;
import it.govpay.web.rs.dars.base.DarsService;
import it.govpay.web.rs.dars.exception.ConsoleException;
import it.govpay.web.rs.dars.exception.DeleteException;
import it.govpay.web.rs.dars.exception.DuplicatedEntryException;
import it.govpay.web.rs.dars.exception.ExportException;
import it.govpay.web.rs.dars.exception.ValidationException;
import it.govpay.web.rs.dars.handler.IDarsHandler;
import it.govpay.web.rs.dars.manutenzione.strumenti.StrumentiHandler;
import it.govpay.web.rs.dars.model.Dettaglio;
import it.govpay.web.rs.dars.model.Elemento;
import it.govpay.web.rs.dars.model.Elenco;
import it.govpay.web.rs.dars.model.InfoForm;
import it.govpay.web.rs.dars.model.InfoForm.Sezione;
import it.govpay.web.rs.dars.model.RawParamValue;
import it.govpay.web.rs.dars.model.Voce;
import it.govpay.web.rs.dars.model.input.ParamField;
import it.govpay.web.rs.dars.model.input.RefreshableParamField;
import it.govpay.web.rs.dars.model.input.base.InputFile;
import it.govpay.web.rs.dars.model.input.base.SelectList;
import it.govpay.web.utils.ConsoleProperties;
import it.govpay.web.utils.Utils;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

public class TracciatiHandler extends DarsHandler<Tracciato> implements IDarsHandler<Tracciato>{

	private SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm");  

	public TracciatiHandler(Logger log, DarsService darsService) { 
		super(log, darsService);
	}

	@Override
	public Elenco getElenco(UriInfo uriInfo, BasicBD bd) throws WebApplicationException, ConsoleException {
		String methodName = "getElenco " + this.titoloServizio;

		try{	
			// Operazione consentita solo agli utenti che hanno almeno un ruolo consentito per la funzionalita'
			this.darsService.checkDirittiServizio(bd, this.funzionalita);

			Integer offset = this.getOffset(uriInfo);
			Integer limit = this.getLimit(uriInfo);

			TracciatiBD tracciatiBD = new TracciatiBD(bd);
			boolean simpleSearch = this.containsParameter(uriInfo, DarsService.SIMPLE_SEARCH_PARAMETER_ID); 

			TracciatoFilter filter = tracciatiBD.newFilter(simpleSearch);
			filter.setOffset(offset);
			filter.setLimit(limit);
			FilterSortWrapper fsw = new FilterSortWrapper();
			fsw.setField(it.govpay.orm.Tracciato.model().DATA_CARICAMENTO);
			fsw.setSortOrder(SortOrder.DESC);
			filter.getFilterSortList().add(fsw);

			boolean eseguiRicerca = this.popolaFiltroRicerca(uriInfo, tracciatiBD, simpleSearch, filter);

			long count = eseguiRicerca ? tracciatiBD.count(filter) : 0;
			// visualizza la ricerca solo se i risultati sono > del limit
			boolean visualizzaRicerca = this.visualizzaRicerca(count, limit);
			InfoForm infoRicerca = this.getInfoRicerca(uriInfo, bd, visualizzaRicerca);

			// Indico la visualizzazione custom
			String formatter = Utils.getInstance(this.getLanguage()).getMessageFromResourceBundle(this.nomeServizio+".elenco.formatter");
			String simpleSearchPlaceholder = Utils.getInstance(this.getLanguage()).getMessageFromResourceBundle(this.nomeServizio+".simpleSearch.placeholder");
			Elenco elenco = new Elenco(this.titoloServizio, infoRicerca,
					this.getInfoCreazione(uriInfo, bd),
					count, this.getInfoEsportazione(uriInfo, bd), this.getInfoCancellazione(uriInfo, bd),simpleSearchPlaceholder); 

			List<Tracciato> findAll = eseguiRicerca ? tracciatiBD.findAll(filter) : new ArrayList<Tracciato>(); 

			if(findAll != null && findAll.size() > 0){
				for (Tracciato entry : findAll) {
					Elemento elemento = this.getElemento(entry, entry.getId(), this.pathServizio,bd);
					elemento.setFormatter(formatter); 
					if(!entry.getStato().equals(StatoTracciatoType.CARICAMENTO_KO) && !entry.getStato().equals(StatoTracciatoType.CARICAMENTO_OK)){
						URI refreshUri =  Utils.creaUriConPath(this.pathServizio , entry.getId()+"", BaseDarsService.PATH_REFRESH);
						elemento.setRefreshUri(refreshUri);
					}
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

	public Elemento getElemento(long id, UriInfo uriInfo, BasicBD bd) throws WebApplicationException, ConsoleException {
		String methodName = "elemento " + this.titoloServizio + ".Id "+ id;

		try{
			this.log.info("Esecuzione " + methodName + " in corso...");

			// Operazione consentita solo all'operatore con ruolo autorizzato
			this.darsService.checkDirittiServizioLettura(bd, this.funzionalita); 
			String formatter = Utils.getInstance(this.getLanguage()).getMessageFromResourceBundle(this.nomeServizio+".elenco.formatter");

			// recupero oggetto
			TracciatiBD tracciatiBD = new TracciatiBD(bd);
			Tracciato tracciato = tracciatiBD.getTracciato(id);
			Elemento elemento = this.getElemento(tracciato, tracciato.getId(), this.pathServizio,bd);
			elemento.setFormatter(formatter); 
			if(!tracciato.getStato().equals(StatoTracciatoType.CARICAMENTO_KO) && !tracciato.getStato().equals(StatoTracciatoType.CARICAMENTO_OK)){
				URI refreshUri =  Utils.creaUriConPath(this.pathServizio , id+"", BaseDarsService.PATH_REFRESH);
				elemento.setRefreshUri(refreshUri);
			}

			this.log.info("Esecuzione " + methodName + " completata.");
			return elemento;
		}catch(WebApplicationException e){
			throw e;
		}catch(Exception e){
			throw new ConsoleException(e);
		}
	}	


	private boolean popolaFiltroRicerca(UriInfo uriInfo, BasicBD bd, boolean simpleSearch, TracciatoFilter filter) throws ConsoleException, Exception {
		Set<Long> setDomini = this.darsService.getIdDominiAbilitatiLetturaServizio(bd, this.funzionalita);
		//		List<Long> idDomini = new ArrayList<Long>();
		boolean eseguiRicerca = !setDomini.isEmpty(); // isAdmin;

		if(simpleSearch){
			// simplesearch
			String simpleSearchString = this.getParameter(uriInfo, DarsService.SIMPLE_SEARCH_PARAMETER_ID, String.class);
			if(StringUtils.isNotEmpty(simpleSearchString)) {
				filter.setSimpleSearchString(simpleSearchString);
			}
		}else{
			// stato 
			String statoId = Utils.getInstance(this.getLanguage()).getMessageFromResourceBundle(this.nomeServizio + ".stato.id");
			String stato = this.getParameter(uriInfo, statoId, String.class);

			if(StringUtils.isNotEmpty(stato)){
				filter.addStatoTracciato(StatoTracciatoType.valueOf(stato));
			}
		}

		// l'utente che non e' admin per il servizio vede solo i suoi tracciati
		if(!this.darsService.isOperatoreAdminServizio(bd, this.funzionalita)){
			Operatore operatore = this.darsService.getOperatoreByPrincipal(bd);
			filter.setIdOperatore(operatore.getId());
		}

		return eseguiRicerca;
	}

	@SuppressWarnings("unchecked")
	@Override
	public InfoForm getInfoRicerca(UriInfo uriInfo, BasicBD bd, boolean visualizzaRicerca,
			Map<String, String> parameters) throws ConsoleException {
		URI ricerca = this.getUriRicerca(uriInfo, bd);
		InfoForm infoRicerca = new InfoForm(ricerca);

		if(visualizzaRicerca) {
			String statoId = Utils.getInstance(this.getLanguage()).getMessageFromResourceBundle(this.nomeServizio + ".stato.id");

			if(this.infoRicercaMap == null){
				this.initInfoRicerca(uriInfo, bd);
			}

			Sezione sezioneRoot = infoRicerca.getSezioneRoot();

			SelectList<String> stato = (SelectList<String>) this.infoRicercaMap.get(statoId);
			stato.setDefaultValue(null);
			stato.setEditable(true); 
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
			stati.add(new Voce<String>(Utils.getInstance(this.getLanguage()).getMessageFromResourceBundle(this.nomeServizio + ".stato."+StatoTracciatoType.ANNULLATO.name()), StatoTracciatoType.ANNULLATO.name()));
			stati.add(new Voce<String>(Utils.getInstance(this.getLanguage()).getMessageFromResourceBundle(this.nomeServizio + ".stato."+StatoTracciatoType.NUOVO.name()), StatoTracciatoType.NUOVO.name()));
			//			stati.add(new Voce<String>(Utils.getInstance(this.getLanguage()).getMessageFromResourceBundle(this.nomeServizio + ".stato."+StatoTracciatoType.IN_VALIDAZIONE.name()), StatoTracciatoType.IN_VALIDAZIONE.name()));
			//			stati.add(new Voce<String>(Utils.getInstance(this.getLanguage()).getMessageFromResourceBundle(this.nomeServizio + ".stato."+StatoTracciatoType.VALIDAZIONE_OK.name()), StatoTracciatoType.VALIDAZIONE_OK.name()));
			//			stati.add(new Voce<String>(Utils.getInstance(this.getLanguage()).getMessageFromResourceBundle(this.nomeServizio + ".stato."+StatoTracciatoType.VALIDAZIONE_KO.name()), StatoTracciatoType.VALIDAZIONE_KO.name()));
			stati.add(new Voce<String>(Utils.getInstance(this.getLanguage()).getMessageFromResourceBundle(this.nomeServizio + ".stato."+StatoTracciatoType.IN_CARICAMENTO.name()), StatoTracciatoType.IN_CARICAMENTO.name()));
			stati.add(new Voce<String>(Utils.getInstance(this.getLanguage()).getMessageFromResourceBundle(this.nomeServizio + ".stato."+StatoTracciatoType.CARICAMENTO_OK.name()), StatoTracciatoType.CARICAMENTO_OK.name()));
			stati.add(new Voce<String>(Utils.getInstance(this.getLanguage()).getMessageFromResourceBundle(this.nomeServizio + ".stato."+StatoTracciatoType.CARICAMENTO_KO.name()), StatoTracciatoType.CARICAMENTO_KO.name()));
			SelectList<String> stato  = new SelectList<String>(statoId, statoLabel, null, false, false, true, stati );
			this.infoRicercaMap.put(statoId, stato);

		}
	}

	@Override
	public InfoForm getInfoCreazione(UriInfo uriInfo, BasicBD bd) throws ConsoleException {
		InfoForm infoCreazione =  null;
		try {
			if(this.darsService.isServizioAbilitatoScrittura(bd, this.funzionalita)){

				URI creazione = this.getUriCreazione(uriInfo, bd);
				infoCreazione = new InfoForm(creazione,Utils.getInstance(this.getLanguage()).getMessageFromResourceBundle(this.nomeServizio + ".creazione.titolo"));

				String fileTracciatoId = Utils.getInstance(this.getLanguage()).getMessageFromResourceBundle(this.nomeServizio + ".fileTracciato.id");

				if(this.infoCreazioneMap == null){
					this.initInfoCreazione(uriInfo, bd);
				}

				Sezione sezioneRoot = infoCreazione.getSezioneRoot();

				InputFile fileTracciato = (InputFile) this.infoCreazioneMap.get(fileTracciatoId);
				fileTracciato.setDefaultValue(null);
				sezioneRoot.addField(fileTracciato);
			}
		} catch (ServiceException e) {
			throw new ConsoleException(e);
		}
		return infoCreazione;
	}

	private void initInfoCreazione(UriInfo uriInfo, BasicBD bd) throws ConsoleException{
		if(this.infoCreazioneMap == null){
			this.infoCreazioneMap = new HashMap<String, ParamField<?>>();

			try {
				String fileTracciatoId = Utils.getInstance(this.getLanguage()).getMessageFromResourceBundle(this.nomeServizio + ".fileTracciato.id");
				String fileTracciatoLabel = Utils.getInstance(this.getLanguage()).getMessageFromResourceBundle(this.nomeServizio + ".fileTracciato.label");

				String tracciatoAccTypes = Utils.getInstance(this.getLanguage()).getMessageFromResourceBundle(this.nomeServizio + ".fileTracciato.acceptedMimeTypes");
				List<String> acceptedMimeTypes =new ArrayList<String>();
				acceptedMimeTypes.addAll(Arrays.asList(tracciatoAccTypes.split(",")));  

				Long dimensioneMassimaFileTracciato = ConsoleProperties.getInstance().getDimensioneMassimaFileTracciato();
				String maxByteSizeS =Utils.fileSizeConverter(dimensioneMassimaFileTracciato);
				int numeroFileTracciato = 1;
				String fileTracciatoNote = Utils.getInstance(this.getLanguage()).getMessageWithParamsFromResourceBundle(this.nomeServizio + ".fileTracciato.note", maxByteSizeS);
				InputFile fileTracciato = new InputFile(fileTracciatoId,fileTracciatoLabel, true, false, true, acceptedMimeTypes , dimensioneMassimaFileTracciato ,numeroFileTracciato);
				fileTracciato.setSuggestion(Utils.getInstance(this.getLanguage()).getMessageFromResourceBundle(this.nomeServizio + ".fileTracciato.suggestion"));
				fileTracciato.setValidation(null, Utils.getInstance(this.getLanguage()).getMessageFromResourceBundle(this.nomeServizio + ".fileTracciato.errorMessage"));
				fileTracciato.setErrorMessageFileSize(Utils.getInstance(this.getLanguage()).getMessageFromResourceBundle(this.nomeServizio + ".fileTracciato.errorMessageFileSize"));
				fileTracciato.setErrorMessageFileType(Utils.getInstance(this.getLanguage()).getMessageFromResourceBundle(this.nomeServizio + ".fileTracciato.errorMessageFileType"));
				fileTracciato.setNote(fileTracciatoNote);

				this.infoCreazioneMap.put(fileTracciatoId, fileTracciato);
			} catch(Exception e ){
				throw new ConsoleException(e); 
			}
		}
	}

	@Override
	public InfoForm getInfoModifica(UriInfo uriInfo, BasicBD bd, Tracciato entry) throws ConsoleException {
		return null;
	}

	@Override
	public InfoForm getInfoCancellazione(UriInfo uriInfo, BasicBD bd, Map<String, String> parameters) throws ConsoleException {
		return null;
	}

	@Override
	public InfoForm getInfoCancellazioneDettaglio(UriInfo uriInfo, BasicBD bd, Tracciato entry)
			throws ConsoleException {
		return null;
	}

	@Override
	public InfoForm getInfoEsportazione(UriInfo uriInfo, BasicBD bd, Map<String, String> parameters) throws ConsoleException { return null; }

	@Override
	public InfoForm getInfoEsportazioneDettaglio(UriInfo uriInfo, BasicBD bd, Tracciato entry)	throws ConsoleException {	
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
	public Object getField(UriInfo uriInfo, List<RawParamValue> values, String fieldId, BasicBD bd)
			throws WebApplicationException, ConsoleException {
		this.log.debug("Richiesto field ["+fieldId+"]");
		try{
			// Operazione consentita solo ai ruoli con diritto di scrittura
			this.darsService.checkDirittiServizioScrittura(bd, this.funzionalita); 

			if(this.infoCreazioneMap == null){
				this.initInfoCreazione(uriInfo, bd);
			}

			if(this.infoCreazioneMap.containsKey(fieldId)){
				RefreshableParamField<?> paramField = (RefreshableParamField<?>) this.infoCreazioneMap.get(fieldId);

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

			// recupero oggetto
			TracciatiBD tracciatiBD = new TracciatiBD(bd);
			Tracciato tracciato = tracciatiBD.getTracciato(id);

			InfoForm infoModifica = null;
			InfoForm infoCancellazione = null;
			InfoForm infoEsportazione = this.getInfoEsportazioneDettaglio(uriInfo, tracciatiBD, tracciato);

			String titolo = Utils.getInstance(this.getLanguage()).getMessageFromResourceBundle(this.nomeServizio + ".dettaglioTracciato") ;
			Dettaglio dettaglio = new Dettaglio(titolo, infoEsportazione, infoCancellazione, infoModifica);

			it.govpay.web.rs.dars.model.Sezione root = dettaglio.getSezioneRoot();

			// dati dell'tracciato
			String statoLabel = Utils.getInstance(this.getLanguage()).getMessageFromResourceBundle(this.nomeServizio + ".stato." + tracciato.getStato().name());
			root.addVoce(Utils.getInstance(this.getLanguage()).getMessageFromResourceBundle(this.nomeServizio + ".stato.label"), statoLabel);
			if(StringUtils.isNotEmpty(tracciato.getDescrizioneStato()))
				root.addVoce(Utils.getInstance(this.getLanguage()).getMessageFromResourceBundle(this.nomeServizio + ".descrizioneStato.label"), tracciato.getDescrizioneStato());
			root.addVoce(Utils.getInstance(this.getLanguage()).getMessageFromResourceBundle(this.nomeServizio + ".nomeFile.label"), tracciato.getNomeFile());
			if(tracciato.getDataCaricamento() != null)
				root.addVoce(Utils.getInstance(this.getLanguage()).getMessageFromResourceBundle(this.nomeServizio + ".dataCaricamento.label"), this.sdf.format(tracciato.getDataCaricamento()));
			if(tracciato.getDataUltimoAggiornamento() != null)
				root.addVoce(Utils.getInstance(this.getLanguage()).getMessageFromResourceBundle(this.nomeServizio + ".dataUltimoAggiornamento.label"), this.sdf.format(tracciato.getDataUltimoAggiornamento()));

			//			root.addVoce(Utils.getInstance(this.getLanguage()).getMessageFromResourceBundle(this.nomeServizio + ".lineaElaborazione.label"), tracciato.getLineaElaborazione() + "");
			Long idOperatore = tracciato.getIdOperatore();
			// solo l'amministratore vede chi ha caricato il tracciato, un utente "user" vede solo i suoi.
			if(idOperatore != null && idOperatore > 0){
				OperatoriBD operatoriBD = new OperatoriBD(bd);
				Operatore operatore = operatoriBD.getOperatore(idOperatore);
				root.addVoce(Utils.getInstance(this.getLanguage()).getMessageFromResourceBundle(this.nomeServizio + ".operatore.label"), operatore.getNome());
			}

			//			URI uriTracciatoOriginale = Utils.creaUriConPath(this.pathServizio,tracciato.getId()+"","tracciatoOriginale");
			//			URI uriTracciatoElaborato = Utils.creaUriConPath(this.pathServizio,tracciato.getId()+"","tracciatoElaborato");
			//			URI uriAvvisiPagamento = Utils.creaUriConPath(this.pathServizio,tracciato.getId()+"","avvisiPagamento");
			//
			//			root.addDownloadLink(Utils.getInstance(this.getLanguage()).getMessageFromResourceBundle(this.nomeServizio + ".tracciatoOriginale.label"),
			//					Utils.getInstance(this.getLanguage()).getMessageFromResourceBundle("commons.label.scarica"),uriTracciatoOriginale); 
			//			root.addDownloadLink(Utils.getInstance(this.getLanguage()).getMessageFromResourceBundle(this.nomeServizio + ".tracciatoElaborato.label"), 
			//					Utils.getInstance(this.getLanguage()).getMessageFromResourceBundle("commons.label.scarica"),uriTracciatoElaborato);
			//			root.addDownloadLink(Utils.getInstance(this.getLanguage()).getMessageFromResourceBundle(this.nomeServizio + ".avvisiPagamento.label"), 
			//					Utils.getInstance(this.getLanguage()).getMessageFromResourceBundle("commons.label.scarica"),uriAvvisiPagamento);

			// Elemento correlato
			Operazioni operazioniDars = new Operazioni();
			String etichettaOperazioni = Utils.getInstance(this.getLanguage()).getMessageFromResourceBundle(this.nomeServizio + ".elementoCorrelato.operazioni.titolo");
			String tracciatoId = Utils.getInstance(this.getLanguage()).getMessageFromResourceBundle(operazioniDars.getNomeServizio() + ".idTracciato.id");
			Map<String, String> params = new HashMap<String, String>();
			params.put(tracciatoId, tracciato.getId()+"");
			URI operazioniURI = Utils.creaUriConParametri(operazioniDars.getPathServizio(), params );
			dettaglio.addElementoCorrelato(etichettaOperazioni, operazioniURI); 

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
	public Tracciato creaEntry(InputStream is, UriInfo uriInfo, BasicBD bd)
			throws WebApplicationException, ConsoleException {
		String methodName = "creaEntry " + this.titoloServizio;
		Tracciato entry = null;
		String fileTracciatoId = Utils.getInstance(this.getLanguage()).getMessageFromResourceBundle(this.nomeServizio + ".fileTracciato.id");
		try{
			this.darsService.checkDirittiServizioScrittura(bd, this.funzionalita); 
			Operatore operatore = this.darsService.getOperatoreByPrincipal(bd); 

			//			JsonConfig jsonConfig = new JsonConfig();
			ByteArrayOutputStream baos = new ByteArrayOutputStream();
			Utils.copy(is, baos);

			baos.flush();
			baos.close();

			JSONObject jsonObjectTracciato = JSONObject.fromObject( baos.toString() );

			JSONArray jsonArrayFile = jsonObjectTracciato.getJSONArray(fileTracciatoId);

			if(jsonArrayFile == null || jsonArrayFile.size() != 1){
				String msg = Utils.getInstance(this.getLanguage()).getMessageFromResourceBundle(this.nomeServizio + ".fileRicevutoNonValido");
				throw new ConsoleException(msg);
			}

			JSONObject jsonObjectFile = jsonArrayFile.getJSONObject(0);
			String fileName = jsonObjectFile.getString(InputFile.FILENAME);
			String data64 = jsonObjectFile.getString(InputFile.DATA);

			entry = new Tracciato();
			entry.setDataCaricamento(new Date());
			entry.setDataUltimoAggiornamento(new Date());
			entry.setStato(StatoTracciatoType.NUOVO);
			entry.setIdOperatore(operatore.getId()); 
			entry.setLineaElaborazione(0);
			entry.setNomeFile(fileName);
			entry.setRawDataRichiesta(Base64.decodeBase64(data64));

			//			
			//			jsonConfig.setRootClass(Tracciato.class);
			//			entry = (Tracciato) JSONObject.toBean( jsonObjectTracciato, jsonConfig );
			this.log.info("Esecuzione " + methodName + " completata.");
			return entry;
		}catch(WebApplicationException e){
			throw e;
		}catch(Exception e){
			throw new ConsoleException(e);
		}
	}

	@Override
	public Dettaglio insert(InputStream is, UriInfo uriInfo, BasicBD bd)
			throws WebApplicationException, ConsoleException, ValidationException, DuplicatedEntryException {
		String methodName = "Insert " + this.titoloServizio;

		try{
			this.log.info("Esecuzione " + methodName + " in corso...");

			// Operazione consentita solo ai ruoli con diritto di scrittura
			this.darsService.checkDirittiServizioScrittura(bd, this.funzionalita); 

			Tracciato entry = this.creaEntry(is, uriInfo, bd);

			this.checkEntry(entry, null);

			Tracciati tracciatiBd = new Tracciati(bd);
			InserisciTracciatoDTO inserisciTracciatoDTO = new InserisciTracciatoDTO();

			inserisciTracciatoDTO.setNomeTracciato(entry.getNomeFile());
			inserisciTracciatoDTO.setTracciato(entry.getRawDataRichiesta());
			inserisciTracciatoDTO.setOperatore(entry.getOperatore(bd)); 

			InserisciTracciatoDTOResponse inserisciTracciatoDTOResponse = tracciatiBd.inserisciTracciato(inserisciTracciatoDTO);
			entry = inserisciTracciatoDTOResponse.getTracciato();

			// invocare jmx
			Object invoke = null;
			String nomeMetodo = "attivazioneRecuperoTracciatiPendenti";
			log.debug("Invocazione operazione ["+nomeMetodo +"] in corso..."); 
			Map<String, String> urlJMX = ConsoleProperties.getInstance().getUrlJMX();
			for(String nodo : urlJMX.keySet()) {

				String url = urlJMX.get(nodo);

				try{
					invoke = Utils.invocaOperazioneJMX(nomeMetodo, url, org.apache.log4j.Logger.getLogger(StrumentiHandler.class));

					if(invoke != null && invoke instanceof String){
						String esito = (String) invoke;
						String[] voci = esito.split("\\|");
						if(voci != null && voci.length > 0)
							for (String string : voci) {
								String[] voce = string.split("#");
								if(voce.length == 2)
									this.log.debug(voce[0] +", "+voce[1]);
								else
									if(voce.length == 1)
										this.log.debug(voce[0]);
							}
					}

				} catch(Exception e) {
					log.error("si e' verificato un errore durante l'esecuzione dell'operazione ["+nomeMetodo+"]: " + e.getMessage(),e); 
					throw e;
				}
			}

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
	public void checkEntry(Tracciato entry, Tracciato oldEntry) throws ValidationException {
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
	public String getTitolo(Tracciato entry, BasicBD bd)  throws ConsoleException{
		StringBuilder sb = new StringBuilder();

		String nomeFile = entry.getNomeFile();
		String statoLabel = Utils.getInstance(this.getLanguage()).getMessageFromResourceBundle(this.nomeServizio + ".stato." + entry.getStato().name());

		sb.append(Utils.getInstance(this.getLanguage()).getMessageWithParamsFromResourceBundle(this.nomeServizio + ".label.titolo", nomeFile, statoLabel));


		return sb.toString();
	}

	@Override
	public String getSottotitolo(Tracciato entry, BasicBD bd)  throws ConsoleException {

		StringBuilder sb = new StringBuilder();

		if(entry.getDataCaricamento() != null){
			String dataS = this.sdf.format(entry.getDataCaricamento());
			sb.append(Utils.getInstance(this.getLanguage()).getMessageWithParamsFromResourceBundle((this.nomeServizio + ".label.sottotitolo"), dataS));
		}

		return sb.toString();
	}

	@Override
	public String esporta(List<Long> idsToExport, List<RawParamValue> rawValues, UriInfo uriInfo, BasicBD bd,
			ZipOutputStream zout) throws WebApplicationException, ConsoleException, ExportException {
		return null;
	}

	@Override
	public String esporta(Long idToExport, List<RawParamValue> rawValues, UriInfo uriInfo, BasicBD bd, ZipOutputStream zout)	throws WebApplicationException, ConsoleException, ExportException {
		String methodName = "esporta " + this.titoloServizio + "[" + idToExport + "]";  
		boolean exportTracciatoOriginale = true;
		boolean exportTracciatoElaborato = true;
		int numeroZipEntries = 0;
		try{
			this.log.info("Esecuzione " + methodName + " in corso...");
			// Operazione consentita solo ai ruoli con diritto di lettura
			this.darsService.checkDirittiServizioLettura(bd, this.funzionalita); 

			TracciatiBD tracciatiBD = new TracciatiBD(bd);
			Tracciato tracciato = tracciatiBD.getTracciato(idToExport);

			String f = tracciato.getNomeFile();
			int indexOfCsv = f.indexOf(".csv");
			if(indexOfCsv > -1){
				f = f.substring(0, indexOfCsv);
			}

			String fileName = f+".zip";
			if(exportTracciatoOriginale){
				numeroZipEntries ++;
				ZipEntry tracciatoOriginaleEntry = new ZipEntry(tracciato.getNomeFile());
				zout.putNextEntry(tracciatoOriginaleEntry);
				zout.write(tracciato.getRawDataRichiesta());
				zout.closeEntry();
			}

			if(exportTracciatoElaborato && tracciato.getRawDataRisposta() != null){
				numeroZipEntries ++;
				String nomeTracciatoNoExt = tracciato.getNomeFile() != null ? tracciato.getNomeFile().substring(0, tracciato.getNomeFile().lastIndexOf(".csv")) : "tracciato";
				String fileNameElaborato = nomeTracciatoNoExt +  "_esito.csv";

				ZipEntry tracciatoElaboratoEntry = new ZipEntry(fileNameElaborato);
				zout.putNextEntry(tracciatoElaboratoEntry);
				zout.write(tracciato.getRawDataRisposta());
				zout.closeEntry();
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
	public Map<String, Voce<String>> getVoci(Tracciato entry, BasicBD bd) throws ConsoleException {
		Map<String, Voce<String>> valori = new HashMap<String, Voce<String>>();

		valori.put(Utils.getInstance(this.getLanguage()).getMessageFromResourceBundle(this.nomeServizio + ".nomeFile.id"),
				new Voce<String>(Utils.getInstance(this.getLanguage()).getMessageFromResourceBundle(this.nomeServizio + ".nomeFile.label"),entry.getNomeFile()));
		if(entry.getDataCaricamento() != null){
			String dataS = this.sdf.format(entry.getDataCaricamento());
			valori.put(Utils.getInstance(this.getLanguage()).getMessageFromResourceBundle(this.nomeServizio + ".dataCaricamento.id"),
					new Voce<String>(Utils.getInstance(this.getLanguage()).getMessageFromResourceBundle(this.nomeServizio + ".dataCaricamento.label"),dataS));
		}



		StatoTracciatoType stato = entry.getStato();
		if(stato != null){
			String statoS = stato.name();

			switch (stato) {
			case CARICAMENTO_KO:
			case CARICAMENTO_OK:
			case ANNULLATO:
				break;
			case IN_CARICAMENTO:
			case NUOVO:
			default:
				statoS = "IN_CORSO";
				break;
			}

			valori.put(Utils.getInstance(this.getLanguage()).getMessageFromResourceBundle(this.nomeServizio + ".stato.id"),
					new Voce<String>(Utils.getInstance(this.getLanguage()).getMessageFromResourceBundle(this.nomeServizio + ".stato."+statoS),
							statoS));
		}
		valori.put(Utils.getInstance(this.getLanguage()).getMessageFromResourceBundle(this.nomeServizio + ".numOperazioniOk.id"),
				new Voce<String>(Utils.getInstance(this.getLanguage()).getMessageFromResourceBundle(this.nomeServizio + ".numOperazioniOk.label"),entry.getNumOperazioniOk()+""));

		valori.put(Utils.getInstance(this.getLanguage()).getMessageFromResourceBundle(this.nomeServizio + ".numOperazioniKo.id"),
				new Voce<String>(Utils.getInstance(this.getLanguage()).getMessageFromResourceBundle(this.nomeServizio + ".numOperazioniKo.label"),entry.getNumOperazioniKo()+""));

		return valori;
	}


}
