package it.govpay.web.rs.dars.caricamenti.tracciati;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
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

import org.apache.commons.codec.binary.Base64;
import org.apache.commons.lang.StringUtils;
import org.apache.logging.log4j.Logger;
import org.jboss.resteasy.plugins.providers.multipart.MultipartFormDataInput;
import org.openspcoop2.generic_project.expression.SortOrder;

import it.govpay.bd.BasicBD;
import it.govpay.bd.FilterSortWrapper;
import it.govpay.bd.anagrafica.OperatoriBD;
import it.govpay.bd.loader.TracciatiBD;
import it.govpay.bd.loader.filters.TracciatoFilter;
import it.govpay.bd.loader.model.Tracciato;
import it.govpay.model.Operatore;
import it.govpay.model.Operatore.ProfiloOperatore;
import it.govpay.model.loader.Tracciato.StatoTracciatoType;
import it.govpay.web.rs.dars.base.DarsHandler;
import it.govpay.web.rs.dars.base.DarsService;
import it.govpay.web.rs.dars.handler.IDarsHandler;
import it.govpay.web.rs.dars.exception.ConsoleException;
import it.govpay.web.rs.dars.exception.DeleteException;
import it.govpay.web.rs.dars.exception.DuplicatedEntryException;
import it.govpay.web.rs.dars.exception.ExportException;
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
import it.govpay.web.rs.dars.model.input.base.InputFile;
import it.govpay.web.rs.dars.model.input.base.SelectList;
import it.govpay.web.utils.ConsoleProperties;
import it.govpay.web.utils.Utils;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

public class TracciatiHandler extends DarsHandler<Tracciato> implements IDarsHandler<Tracciato>{

	private Map<String, ParamField<?>> infoCreazioneMap = null;
	private Map<String, ParamField<?>> infoRicercaMap = null;
	private SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm");  

	public TracciatiHandler(Logger log, DarsService darsService) { 
		super(log, darsService);
	}

	@Override
	public Elenco getElenco(UriInfo uriInfo, BasicBD bd) throws WebApplicationException, ConsoleException {
		String methodName = "getElenco " + this.titoloServizio;

		try{	
			// Operazione consentita agli utenti registrati
			Operatore operatore = this.darsService.getOperatoreByPrincipal(bd); 

			Integer offset = this.getOffset(uriInfo);
			Integer limit = this.getLimit(uriInfo);
			URI esportazione = null; //this.getUriEsportazione(uriInfo, bd);

			TracciatiBD tracciatiBD = new TracciatiBD(bd);
			boolean simpleSearch = this.containsParameter(uriInfo, DarsService.SIMPLE_SEARCH_PARAMETER_ID); 

			TracciatoFilter filter = tracciatiBD.newFilter(simpleSearch);
			filter.setOffset(offset);
			filter.setLimit(limit);
			FilterSortWrapper fsw = new FilterSortWrapper();
			fsw.setField(it.govpay.orm.Tracciato.model().DATA_ULTIMO_AGGIORNAMENTO);
			fsw.setSortOrder(SortOrder.DESC);
			filter.getFilterSortList().add(fsw);

			boolean eseguiRicerca = this.popolaFiltroRicerca(uriInfo, tracciatiBD, operatore, simpleSearch, filter);

			long count = eseguiRicerca ? tracciatiBD.count(filter) : 0;
			// visualizza la ricerca solo se i risultati sono > del limit
			boolean visualizzaRicerca = this.visualizzaRicerca(count, limit);
			InfoForm infoRicerca = this.getInfoRicerca(uriInfo, bd, visualizzaRicerca);

			// Indico la visualizzazione custom
			String formatter = Utils.getInstance(this.getLanguage()).getMessageFromResourceBundle(this.nomeServizio+".elenco.formatter");
			String simpleSearchPlaceholder = Utils.getInstance(this.getLanguage()).getMessageFromResourceBundle(this.nomeServizio+".simpleSearch.placeholder");
			Elenco elenco = new Elenco(this.titoloServizio, infoRicerca,
					this.getInfoCreazione(uriInfo, bd),
					count, esportazione, this.getInfoCancellazione(uriInfo, bd),simpleSearchPlaceholder); 

			List<Tracciato> findAll = eseguiRicerca ? tracciatiBD.findAll(filter) : new ArrayList<Tracciato>(); 

			if(findAll != null && findAll.size() > 0){
				for (Tracciato entry : findAll) {
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

	private boolean popolaFiltroRicerca(UriInfo uriInfo, BasicBD bd, Operatore operatore , boolean simpleSearch, TracciatoFilter filter) throws ConsoleException, Exception {
		ProfiloOperatore profilo = operatore.getProfilo();
		boolean isAdmin = profilo.equals(ProfiloOperatore.ADMIN);
		//		AclBD aclBD = new AclBD(bd);
		//		List<Acl> aclOperatore = aclBD.getAclOperatore(operatore.getId());
		//		List<Long> idDomini = new ArrayList<Long>();
		boolean eseguiRicerca = true; // isAdmin;

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

		// [TODO] momentaneamente vede tutti i domini ma aggiungiamo solo il vincolo sui suoi tracciati.
		// filtro per l'operatore non admin
		if(!isAdmin){
			filter.setIdOperatore(operatore.getId()); 
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
		//					filter.setIdDomini(idDomini);
		//				}
		//			}
		//		}
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
		URI creazione = this.getUriCreazione(uriInfo, bd);
		InfoForm infoCreazione = new InfoForm(creazione,Utils.getInstance(this.getLanguage()).getMessageFromResourceBundle(this.nomeServizio + ".creazione.titolo"));

		String fileTracciatoId = Utils.getInstance(this.getLanguage()).getMessageFromResourceBundle(this.nomeServizio + ".fileTracciato.id");

		if(this.infoCreazioneMap == null){
			this.initInfoCreazione(uriInfo, bd);
		}

		Sezione sezioneRoot = infoCreazione.getSezioneRoot();

		InputFile fileTracciato = (InputFile) this.infoCreazioneMap.get(fileTracciatoId);
		fileTracciato.setDefaultValue(null);
		sezioneRoot.addField(fileTracciato);

		return infoCreazione;
	}

	private void initInfoCreazione(UriInfo uriInfo, BasicBD bd) throws ConsoleException{
		if(this.infoCreazioneMap == null){
			this.infoCreazioneMap = new HashMap<String, ParamField<?>>();

			try {
				String fileTracciatoId = Utils.getInstance(this.getLanguage()).getMessageFromResourceBundle(this.nomeServizio + ".fileTracciato.id");
				String fileTracciatoLabel = Utils.getInstance(this.getLanguage()).getMessageFromResourceBundle(this.nomeServizio + ".fileTracciato.label");

				List<String> mt = new ArrayList<String>();
				mt.add(".csv");

				Long dimensioneMassimaFileTracciato = ConsoleProperties.getInstance().getDimensioneMassimaFileTracciato();
				int numeroFileTracciato = 1;

				InputFile fileTracciato = new InputFile(fileTracciatoId,fileTracciatoLabel, true, false, true, mt , dimensioneMassimaFileTracciato ,numeroFileTracciato,null);
				fileTracciato.setSuggestion(Utils.getInstance(this.getLanguage()).getMessageFromResourceBundle(this.nomeServizio + ".fileTracciato.suggestion"));
				fileTracciato.setValidation(null, Utils.getInstance(this.getLanguage()).getMessageFromResourceBundle(this.nomeServizio + ".fileTracciato.errorMessage"));
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
	public InfoForm getInfoCancellazione(UriInfo uriInfo, BasicBD bd) throws ConsoleException {
		return null;
	}

	@Override
	public InfoForm getInfoCancellazioneDettaglio(UriInfo uriInfo, BasicBD bd, Tracciato entry)
			throws ConsoleException {
		return null;
	}

	@Override
	public Object getField(UriInfo uriInfo, List<RawParamValue> values, String fieldId, BasicBD bd)
			throws WebApplicationException, ConsoleException {
		this.log.debug("Richiesto field ["+fieldId+"]");
		try{

			this.darsService.getOperatoreByPrincipal(bd);

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
	public Dettaglio getDettaglio(long id, UriInfo uriInfo, BasicBD bd)
			throws WebApplicationException, ConsoleException {
		String methodName = "dettaglio " + this.titoloServizio + ".Id "+ id;

		try{
			this.log.info("Esecuzione " + methodName + " in corso...");
			Operatore operatoreLoggato = this.darsService.getOperatoreByPrincipal(bd); 
			ProfiloOperatore ruolo = operatoreLoggato.getProfilo();
			boolean isAdmin = ruolo.equals(ProfiloOperatore.ADMIN);

			// recupero oggetto
			TracciatiBD tracciatiBD = new TracciatiBD(bd);
			Tracciato tracciato = tracciatiBD.getTracciato(id);

			InfoForm infoModifica = null;
			InfoForm infoCancellazione = null;
			URI esportazione = this.getUriEsportazioneDettaglio(uriInfo, bd,id);

			String titolo = Utils.getInstance(this.getLanguage()).getMessageFromResourceBundle(this.nomeServizio + ".dettaglioTracciato") ;
			Dettaglio dettaglio = new Dettaglio(titolo, esportazione, infoCancellazione, infoModifica);

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
			long idOperatore = tracciato.getIdOperatore();
			// solo l'amministratore vede chi ha caricato il tracciato, un utente "user" vede solo i suoi.
			if(idOperatore > 0 && isAdmin){
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
			this.log.info("Esecuzione " + methodName + " in corso...");

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

			this.darsService.getOperatoreByPrincipal(bd);

			Tracciato entry = this.creaEntry(is, uriInfo, bd);

			this.checkEntry(entry, null);

			TracciatiBD tracciatiBD = new TracciatiBD(bd);
			tracciatiBD.insertTracciato(entry);

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
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String esporta(Long idToExport, UriInfo uriInfo, BasicBD bd, ZipOutputStream zout)	throws WebApplicationException, ConsoleException, ExportException {
		String methodName = "esporta " + this.titoloServizio + "[" + idToExport + "]";  
		boolean exportTracciatoOriginale = true;
		boolean exportTracciatoElaborato = true;
		int numeroZipEntries = 0;
		try{
			this.log.info("Esecuzione " + methodName + " in corso...");
			this.darsService.getOperatoreByPrincipal(bd); 

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
				SimpleDateFormat f2 = new SimpleDateFormat("yyyy_MM_dd_HHmm");
				String fileNameElaborato = nomeTracciatoNoExt +  "_" + f2.format(tracciato.getDataCaricamento())  + ".csv";

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

		valori.put(Utils.getInstance(this.getLanguage()).getMessageFromResourceBundle(this.nomeServizio + ".stato.id"),
				new Voce<String>(Utils.getInstance(this.getLanguage()).getMessageFromResourceBundle(this.nomeServizio + ".stato."+entry.getStato().name()),
						entry.getStato().name()));

		valori.put(Utils.getInstance(this.getLanguage()).getMessageFromResourceBundle(this.nomeServizio + ".numOperazioniOk.id"),
				new Voce<String>(Utils.getInstance(this.getLanguage()).getMessageFromResourceBundle(this.nomeServizio + ".numOperazioniOk.label"),entry.getNumOperazioniOk()+""));

		valori.put(Utils.getInstance(this.getLanguage()).getMessageFromResourceBundle(this.nomeServizio + ".numOperazioniKo.id"),
				new Voce<String>(Utils.getInstance(this.getLanguage()).getMessageFromResourceBundle(this.nomeServizio + ".numOperazioniKo.label"),entry.getNumOperazioniKo()+""));

		return valori;
	}


}
