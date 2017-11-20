package it.govpay.web.rs.dars.caricamenti.tracciati;

import java.net.URI;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.UriInfo;

import org.apache.commons.lang.StringUtils;
import org.apache.logging.log4j.Logger;
import org.openspcoop2.generic_project.exception.ServiceException;
import org.openspcoop2.generic_project.expression.SortOrder;

import it.govpay.bd.BasicBD;
import it.govpay.bd.FilterSortWrapper;
import it.govpay.bd.model.Operazione;
import it.govpay.bd.model.OperazioneCaricamento;
import it.govpay.bd.model.Tracciato;
import it.govpay.bd.pagamento.TracciatiBD;
import it.govpay.bd.pagamento.filters.TracciatoFilter;
import it.govpay.core.business.AvvisoPagamento;
import it.govpay.core.business.Tracciati;
import it.govpay.core.business.model.LeggiAvvisoDTO;
import it.govpay.core.business.model.LeggiAvvisoDTOResponse;
import it.govpay.core.business.model.ListaOperazioniDTO;
import it.govpay.core.business.model.ListaOperazioniDTOResponse;
import it.govpay.model.Operazione.StatoOperazioneType;
import it.govpay.model.Operazione.TipoOperazioneType;
import it.govpay.model.Tracciato.StatoTracciatoType;
import it.govpay.model.Tracciato.TipoTracciatoType;
import it.govpay.model.avvisi.AvvisoPagamento.StatoAvviso;
import it.govpay.web.rs.dars.base.BaseDarsService;
import it.govpay.web.rs.dars.base.DarsService;
import it.govpay.web.rs.dars.caricamenti.BaseTracciatiHandler;
import it.govpay.web.rs.dars.exception.ConsoleException;
import it.govpay.web.rs.dars.exception.ExportException;
import it.govpay.web.rs.dars.handler.IDarsHandler;
import it.govpay.web.rs.dars.model.DarsResponse.EsitoOperazione;
import it.govpay.web.rs.dars.model.Elemento;
import it.govpay.web.rs.dars.model.Elenco;
import it.govpay.web.rs.dars.model.InfoForm;
import it.govpay.web.rs.dars.model.InfoForm.Sezione;
import it.govpay.web.rs.dars.model.RawParamValue;
import it.govpay.web.rs.dars.model.Voce;
import it.govpay.web.rs.dars.model.input.ParamField;
import it.govpay.web.rs.dars.model.input.base.CheckButton;
import it.govpay.web.rs.dars.model.input.base.SelectList;
import it.govpay.web.utils.ConsoleProperties;
import it.govpay.web.utils.Utils;

public class TracciatiHandler extends BaseTracciatiHandler implements IDarsHandler<Tracciato>{

	private boolean abilitaDownloadAvvisiPagamento = false;

	public TracciatiHandler(Logger log, DarsService darsService) { 
		super(log, darsService,TipoTracciatoType.VERSAMENTI);
		this.abilitaDownloadAvvisiPagamento  = ConsoleProperties.getInstance().isAbilitaDownloadAvvisiPagamento();
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
			filter.setTipoTracciato(this.tipoTracciato);
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
					if(!entry.getStato().equals(StatoTracciatoType.CARICAMENTO_KO) && !entry.getStato().equals(StatoTracciatoType.CARICAMENTO_OK) && !entry.getStato().equals(StatoTracciatoType.STAMPATO)){
						URI refreshUri =  Utils.creaUriConPath(this.pathServizio , entry.getId()+"", BaseDarsService.PATH_REFRESH);
						elemento.setRefreshUri(refreshUri);

						double percentuale = 100;
						// Controllo per evitare divisione per 0
						if(entry.getNumLineeTotali() != 0)
							percentuale = Math.round(((double) entry.getLineaElaborazione() / (double) entry.getNumLineeTotali() ) * 100);

						elemento.getVoci().put(Utils.getInstance(this.getLanguage()).getMessageFromResourceBundle(this.nomeServizio + ".percentuale.id"),
								new Voce<String>(Utils.getInstance(this.getLanguage()).getMessageFromResourceBundle(this.nomeServizio + ".percentuale.label"), percentuale + "" ));

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
			if(!tracciato.getStato().equals(StatoTracciatoType.CARICAMENTO_KO) && !tracciato.getStato().equals(StatoTracciatoType.CARICAMENTO_OK) && !tracciato.getStato().equals(StatoTracciatoType.STAMPATO)){
				URI refreshUri =  Utils.creaUriConPath(this.pathServizio , id+"", BaseDarsService.PATH_REFRESH);
				elemento.setRefreshUri(refreshUri);

				double percentuale = 100;
				// Controllo per evitare divisione per 0
				if(tracciato.getNumLineeTotali() != 0)
					percentuale = Math.round(((double) tracciato.getLineaElaborazione() / (double) tracciato.getNumLineeTotali() ) * 100);

				elemento.getVoci().put(Utils.getInstance(this.getLanguage()).getMessageFromResourceBundle(this.nomeServizio + ".percentuale.id"),
						new Voce<String>(Utils.getInstance(this.getLanguage()).getMessageFromResourceBundle(this.nomeServizio + ".percentuale.label"), percentuale + "" ));
			}

			this.log.info("Esecuzione " + methodName + " completata.");
			return elemento;
		}catch(WebApplicationException e){
			throw e;
		}catch(Exception e){
			throw new ConsoleException(e);
		}
	}	


	protected void initInfoRicerca(UriInfo uriInfo, BasicBD bd) throws ConsoleException{
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
			
			if(this.abilitaDownloadAvvisiPagamento)
				stati.add(new Voce<String>(Utils.getInstance(this.getLanguage()).getMessageFromResourceBundle(this.nomeServizio + ".stato."+StatoTracciatoType.STAMPATO.name()), StatoTracciatoType.STAMPATO.name()));
				
			SelectList<String> stato  = new SelectList<String>(statoId, statoLabel, null, false, false, true, stati );
			this.infoRicercaMap.put(statoId, stato);

		}
	}

	@Override
	public InfoForm getInfoEsportazioneDettaglio(UriInfo uriInfo, BasicBD bd, Tracciato entry)	throws ConsoleException {	
		InfoForm infoEsportazione = null;
		try{
			if(this.darsService.isServizioAbilitatoLettura(bd, this.funzionalita)){
				URI esportazione = this.getUriEsportazioneDettaglio(uriInfo, bd, entry.getId());
				infoEsportazione = new InfoForm(esportazione);

				// se l'utente ha la possibilita' di scaricare gli avvisi e se tutti gli avvisi del tracciato sono stati generati. 
				if(this.abilitaDownloadAvvisiPagamento && entry.getStato().equals(StatoTracciatoType.STAMPATO)) {
					List<String> titoli = new ArrayList<String>();
					titoli.add(Utils.getInstance(this.getLanguage()).getMessageFromResourceBundle(this.nomeServizio + ".esporta.singolo.titolo"));
					infoEsportazione.setTitolo(titoli);
	
					String esportaCsvId = Utils.getInstance(this.getLanguage()).getMessageFromResourceBundle(this.nomeServizio + ".esportaCsv.id");
					String esportaPdfId = Utils.getInstance(this.getLanguage()).getMessageFromResourceBundle(this.nomeServizio + ".esportaPdf.id");
	
					if(this.infoEsportazioneMap == null){
						this.initInfoEsportazione(uriInfo, bd);
					}
	
					Sezione sezioneRoot = infoEsportazione.getSezioneRoot();
	
					CheckButton esportaCsv = (CheckButton) this.infoEsportazioneMap.get(esportaCsvId);
					esportaCsv.setDefaultValue(true); 
					sezioneRoot.addField(esportaCsv);
	
					CheckButton esportaPdf = (CheckButton) this.infoEsportazioneMap.get(esportaPdfId);
					esportaPdf.setDefaultValue(false); 
					sezioneRoot.addField(esportaPdf);
				}
			}
		}catch(ServiceException e){
			throw new ConsoleException(e);
		}
		return infoEsportazione;
	}

	private void initInfoEsportazione(UriInfo uriInfo, BasicBD bd) throws ConsoleException{
		if(this.infoEsportazioneMap == null){
			this.infoEsportazioneMap = new HashMap<String, ParamField<?>>();

			String esportaCsvId = Utils.getInstance(this.getLanguage()).getMessageFromResourceBundle(this.nomeServizio + ".esportaCsv.id");
			String esportaPdfId = Utils.getInstance(this.getLanguage()).getMessageFromResourceBundle(this.nomeServizio + ".esportaPdf.id");

			// esportaCsv
			String esportaCsvLabel = Utils.getInstance(this.getLanguage()).getMessageFromResourceBundle(this.nomeServizio + ".esportaCsv.label");
			CheckButton esportaCsv = new CheckButton(esportaCsvId, esportaCsvLabel, true, false, false, true);
			this.infoEsportazioneMap.put(esportaCsvId, esportaCsv);

			// esportaPdf
			String esportaPdfLabel = Utils.getInstance(this.getLanguage()).getMessageFromResourceBundle(this.nomeServizio + ".esportaPdf.label");
			CheckButton esportaPdf = new CheckButton(esportaPdfId, esportaPdfLabel, true, false, false, true);
			this.infoEsportazioneMap.put(esportaPdfId, esportaPdf);

		}
	}

	@Override
	public String esporta(Long idToExport, List<RawParamValue> rawValues, UriInfo uriInfo, BasicBD bd, ZipOutputStream zout)	throws WebApplicationException, ConsoleException, ExportException {
		String methodName = "esporta " + this.titoloServizio + "[" + idToExport + "]";  
		int numeroZipEntries = 0;
		boolean esportaCsv = false, esportaPdf = false;
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
			
			// se l'utente ha la possibilita' di scaricare gli avvisi e se tutti gli avvisi del tracciato sono stati generati. 
			if(this.abilitaDownloadAvvisiPagamento && tracciato.getStato().equals(StatoTracciatoType.STAMPATO)) { 
				String esportaCsvId = Utils.getInstance(this.getLanguage()).getMessageFromResourceBundle(this.nomeServizio + ".esportaCsv.id");
				String esportaCsvS = Utils.getValue(rawValues, esportaCsvId);
				if(StringUtils.isNotEmpty(esportaCsvS)){
					esportaCsv = Boolean.parseBoolean(esportaCsvS);
				}
	
				String esportaPdfId = Utils.getInstance(this.getLanguage()).getMessageFromResourceBundle(this.nomeServizio + ".esportaPdf.id");
				String esportaPdfS = Utils.getValue(rawValues, esportaPdfId);
				if(StringUtils.isNotEmpty(esportaPdfS)){
					esportaPdf = Boolean.parseBoolean(esportaPdfS);
				}
			} else {
				//solo csv
				esportaCsv = true;
			}

			// almeno una voce deve essere selezionata
			if(!(esportaCsv || esportaPdf)){
				List<String> msg = new ArrayList<String>();
				msg.add(Utils.getInstance(this.getLanguage()).getMessageFromResourceBundle(this.nomeServizio+".esporta.tipiExportObbligatori"));
				throw new ExportException(msg, EsitoOperazione.ERRORE);
			}

			if(esportaCsv){
				numeroZipEntries ++;
				ZipEntry tracciatoOriginaleEntry = new ZipEntry(tracciato.getNomeFile());
				zout.putNextEntry(tracciatoOriginaleEntry);
				zout.write(tracciato.getRawDataRichiesta());
				zout.closeEntry();
			}

			if(esportaCsv && tracciato.getRawDataRisposta() != null){
				numeroZipEntries ++;
				String nomeTracciatoNoExt = tracciato.getNomeFile() != null ? tracciato.getNomeFile().substring(0, tracciato.getNomeFile().lastIndexOf(".csv")) : "tracciato";
				String fileNameElaborato = nomeTracciatoNoExt +  "_esito.csv";

				ZipEntry tracciatoElaboratoEntry = new ZipEntry(fileNameElaborato);
				zout.putNextEntry(tracciatoElaboratoEntry);
				zout.write(tracciato.getRawDataRisposta());
				zout.closeEntry();
			}

			if(esportaPdf) {
				Tracciati tracciati = new Tracciati(bd);
				ListaOperazioniDTO listaOperazioniDTO =new ListaOperazioniDTO();
				// tipo operazione add
				listaOperazioniDTO.setTipo(TipoOperazioneType.ADD);
				// stato operazione completata con successo
				listaOperazioniDTO.setStato(StatoOperazioneType.ESEGUITO_OK); 
				// id Tracciato
				listaOperazioniDTO.setIdTracciato(idToExport);
				ListaOperazioniDTOResponse listaOperazioni = tracciati.listaOperazioni(listaOperazioniDTO);
				List<Operazione> findAll = listaOperazioni.getOperazioni(); 

				if(findAll != null && findAll.size() > 0){
					this.log.debug("Export Tracciato, verranno inclusi " + findAll.size()+ " Avvisi pagamento.");

					AvvisoPagamento avvisoBD = new AvvisoPagamento(bd);
					Set<String> keys = new TreeSet<String>();
					
					for (int i=findAll.size()-1; i>=0; i--) {
						Operazione entry = findAll.get(i);
					
						if(entry.getTipoOperazione().equals(TipoOperazioneType.ADD)) {
							
							// creo una entry per pdf
							OperazioneCaricamento opCaricamento = (OperazioneCaricamento) entry;
							String codDominio = opCaricamento.getCodDominio();
							String iuv = opCaricamento.getIuv();
							String avvisoFilename = codDominio + "_" + iuv;
							
							//Controllo se l'avviso e' gia' stato stampato per questo IUV
							if(keys.contains(avvisoFilename)) {
								continue;
							}
							
							keys.add(avvisoFilename);
							
							this.log.debug("Lettura dell'Avviso [Dominio: " + codDominio+ " | Iuv: "+ iuv +"] in corso...");
							LeggiAvvisoDTO leggiAvviso = new LeggiAvvisoDTO();
							leggiAvviso.setCodDominio(codDominio);
							leggiAvviso.setIuv(iuv);

							LeggiAvvisoDTOResponse leggiAvvisoDTOResponse = avvisoBD.getAvviso(leggiAvviso );
							
							it.govpay.model.avvisi.AvvisoPagamento avvisoPagamento = leggiAvvisoDTOResponse.getAvviso();
							boolean avvisoDisponibile = avvisoPagamento.getStato().equals(StatoAvviso.STAMPATO) && avvisoPagamento.getPdf() != null;
							this.log.debug("Lettura dell'Avviso [Dominio: " + codDominio+ " | Iuv: "+ iuv +"] effettuata, Avviso in formato Pdf "+(avvisoDisponibile ? "" : "non")+" disponibile.");
							
							if(avvisoDisponibile) {
								// inserisco entry
								numeroZipEntries ++;
								ZipEntry tracciatoElaboratoEntry = new ZipEntry(avvisoFilename + ".pdf");
								zout.putNextEntry(tracciatoElaboratoEntry);
								zout.write(avvisoPagamento.getPdf());
								zout.closeEntry();
							}
						}
					}
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
		}catch(ExportException e){
			throw e;
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
			case STAMPATO:
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
