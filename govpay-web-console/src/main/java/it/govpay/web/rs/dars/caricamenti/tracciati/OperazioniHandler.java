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
import it.govpay.bd.loader.OperazioniBD;
import it.govpay.bd.model.Operazione;
import it.govpay.bd.operazioni.filters.OperazioneFilter;
import it.govpay.model.Operatore;
import it.govpay.orm.constants.EsitoGovpayType;
import it.govpay.web.rs.dars.BaseDarsHandler;
import it.govpay.web.rs.dars.BaseDarsService;
import it.govpay.web.rs.dars.IDarsHandler;
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
import it.govpay.web.rs.dars.model.input.base.SelectList;
import it.govpay.web.utils.Utils;

public class OperazioniHandler extends BaseDarsHandler<Operazione> implements IDarsHandler<Operazione>{

	private Map<String, ParamField<?>> infoRicercaMap = null;
	private SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm"); 

	public OperazioniHandler(Logger log, BaseDarsService darsService) { 
		super(log, darsService);
	}

	@Override
	public Elenco getElenco(UriInfo uriInfo, BasicBD bd) throws WebApplicationException, ConsoleException {
		String methodName = "getElenco " + this.titoloServizio;
		try{
			this.log.info("Esecuzione " + methodName + " in corso...");
			Operatore operatore = this.darsService.getOperatoreByPrincipal(bd);
			Map<String, String> params = new HashMap<String, String>();
			Integer offset = this.getOffset(uriInfo);
			Integer limit = this.getLimit(uriInfo);
			URI esportazione = null; //this.getUriEsportazione(uriInfo, bd);

			OperazioniBD operazioniBD = new OperazioniBD(bd);
			boolean simpleSearch = this.containsParameter(uriInfo, BaseDarsService.SIMPLE_SEARCH_PARAMETER_ID); 
			
			OperazioneFilter filter = operazioniBD.newFilter(simpleSearch);
			filter.setOffset(offset);
			filter.setLimit(limit);
			FilterSortWrapper fsw = new FilterSortWrapper();
			fsw.setField(it.govpay.orm.Operazione.model().COD_VERSAMENTO_ENTE);
			fsw.setSortOrder(SortOrder.DESC);
			filter.getFilterSortList().add(fsw);
			
			boolean eseguiRicerca = this.popolaFiltroRicerca(uriInfo, operazioniBD, operatore, simpleSearch, filter,params);
			
			long count = eseguiRicerca ? operazioniBD.count(filter) : 0;
			// visualizza la ricerca solo se i risultati sono > del limit
			boolean visualizzaRicerca = this.visualizzaRicerca(count, limit);
			InfoForm infoRicerca = this.getInfoRicerca(uriInfo, bd, visualizzaRicerca,params);
			
			// Indico la visualizzazione custom
			String formatter = Utils.getInstance(this.getLanguage()).getMessageFromResourceBundle(this.nomeServizio+".elenco.formatter");
			String simpleSearchPlaceholder = Utils.getInstance(this.getLanguage()).getMessageFromResourceBundle(this.nomeServizio+".simpleSearch.placeholder");
			Elenco elenco = new Elenco(this.titoloServizio, infoRicerca,
					this.getInfoCreazione(uriInfo, bd),
					count, esportazione, this.getInfoCancellazione(uriInfo, bd),simpleSearchPlaceholder); 
			
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


	private boolean popolaFiltroRicerca(UriInfo uriInfo, BasicBD bd,  Operatore operatore, boolean simpleSearch, OperazioneFilter filter,Map<String, String> params) throws ConsoleException, Exception {
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
			String simpleSearchString = this.getParameter(uriInfo, BaseDarsService.SIMPLE_SEARCH_PARAMETER_ID, String.class);
			if(StringUtils.isNotEmpty(simpleSearchString)) {
				filter.setSimpleSearchString(simpleSearchString);
				if(elementoCorrelato)
					params.put(BaseDarsService.SIMPLE_SEARCH_PARAMETER_ID, simpleSearchString);
			}
		} else {
			String esitoId = Utils.getInstance(this.getLanguage()).getMessageFromResourceBundle(this.nomeServizio + ".esito.id");
			String esito = this.getParameter(uriInfo, esitoId, String.class);

			if(StringUtils.isNotEmpty(esito)){
				filter.setEsito(EsitoGovpayType.toEnumConstant(esito));
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
			String esitoId = Utils.getInstance(this.getLanguage()).getMessageFromResourceBundle(this.nomeServizio + ".esito.id");

			if(this.infoRicercaMap == null){
				this.initInfoRicerca(uriInfo, bd);
			}

			Sezione sezioneRoot = infoRicerca.getSezioneRoot();

			SelectList<String> esito = (SelectList<String>) this.infoRicercaMap.get(esitoId);
			esito.setDefaultValue(null);
			sezioneRoot.addField(esito);

		}
		return infoRicerca;
	}

	private void initInfoRicerca(UriInfo uriInfo, BasicBD bd) throws ConsoleException{
		if(this.infoRicercaMap == null){
			this.infoRicercaMap = new HashMap<String, ParamField<?>>();

			String esitoId = Utils.getInstance(this.getLanguage()).getMessageFromResourceBundle(this.nomeServizio + ".esito.id");

			// esito
			String esitoLabel = Utils.getInstance(this.getLanguage()).getMessageFromResourceBundle(this.nomeServizio + ".esito.label");
			List<Voce<String>> stati = new ArrayList<Voce<String>>();

			stati.add(new Voce<String>(Utils.getInstance(this.getLanguage()).getMessageFromResourceBundle("commons.label.qualsiasi"), ""));
			stati.add(new Voce<String>(Utils.getInstance(this.getLanguage()).getMessageFromResourceBundle(this.nomeServizio + ".esito."+EsitoGovpayType.OK.getValue()), EsitoGovpayType.OK.getValue()));
			stati.add(new Voce<String>(Utils.getInstance(this.getLanguage()).getMessageFromResourceBundle(this.nomeServizio + ".esito."+EsitoGovpayType.KO.getValue()), EsitoGovpayType.KO.getValue()));
			SelectList<String> esito  = new SelectList<String>(esitoId, esitoLabel, null, false, false, true, stati );
			this.infoRicercaMap.put(esitoId, esito);

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
	public InfoForm getInfoCancellazione(UriInfo uriInfo, BasicBD bd) throws ConsoleException {
		return null;
	}

	@Override
	public InfoForm getInfoCancellazioneDettaglio(UriInfo uriInfo, BasicBD bd, Operazione entry)
			throws ConsoleException {
		return null;
	}

	@Override
	public Object getField(UriInfo uriInfo, List<RawParamValue> values, String fieldId, BasicBD bd)
			throws WebApplicationException, ConsoleException {
		return null;
	}

	@Override
	public Dettaglio getDettaglio(long id, UriInfo uriInfo, BasicBD bd)
			throws WebApplicationException, ConsoleException {
		// TODO Auto-generated method stub
		return null;
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
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getSottotitolo(Operazione entry, BasicBD bd) throws ConsoleException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String esporta(List<Long> idsToExport, List<RawParamValue> rawValues, UriInfo uriInfo, BasicBD bd,
			ZipOutputStream zout) throws WebApplicationException, ConsoleException, ExportException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String esporta(Long idToExport, UriInfo uriInfo, BasicBD bd, ZipOutputStream zout)
			throws WebApplicationException, ConsoleException, ExportException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Map<String, Voce<String>> getVoci(Operazione entry, BasicBD bd) throws ConsoleException {
		// TODO Auto-generated method stub
		return null;
	}

}
