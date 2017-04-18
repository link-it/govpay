package it.govpay.web.rs.dars.reportistica.pagamenti;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.net.URI;
import java.util.ArrayList;
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

import it.govpay.bd.BasicBD;
import it.govpay.bd.FilterSortWrapper;
import it.govpay.bd.anagrafica.AclBD;
import it.govpay.bd.anagrafica.AnagraficaManager;
import it.govpay.bd.anagrafica.DominiBD;
import it.govpay.bd.anagrafica.filters.DominioFilter;
import it.govpay.bd.model.Dominio;
import it.govpay.bd.reportistica.filters.EstrattoContoFilter;
import it.govpay.model.Acl;
import it.govpay.model.Acl.Tipo;
import it.govpay.model.Operatore;
import it.govpay.model.Operatore.ProfiloOperatore;
import it.govpay.web.business.reportistica.EstrattiContoMetadata;
import it.govpay.web.rs.dars.BaseDarsHandler;
import it.govpay.web.rs.dars.BaseDarsService;
import it.govpay.web.rs.dars.IDarsHandler;
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
import it.govpay.web.rs.dars.model.input.base.SelectList;
import it.govpay.web.utils.Utils;

public class EstrattiContoHandler   extends BaseDarsHandler<it.govpay.model.reportistica.EstrattoContoMetadata> implements IDarsHandler<it.govpay.model.reportistica.EstrattoContoMetadata>{

	private Map<String, ParamField<?>> infoRicercaMap = null;

	public EstrattiContoHandler(Logger log, BaseDarsService darsService) {
		super(log,darsService);
	}

	@Override
	public Elenco getElenco(UriInfo uriInfo,BasicBD bd) throws WebApplicationException,ConsoleException {
		String methodName = "getElenco " + this.titoloServizio;
		try{
			this.log.info("Esecuzione " + methodName + " in corso...");
			Operatore operatore = this.darsService.getOperatoreByPrincipal(bd);
			ProfiloOperatore profilo = operatore.getProfilo();
			boolean isAdmin = profilo.equals(ProfiloOperatore.ADMIN);

			Integer offset = this.getOffset(uriInfo);
			Integer limit = this.getLimit(uriInfo);
			URI esportazione = this.getUriEsportazione(uriInfo, bd);
			URI cancellazione = null;

			List<String> idDomini = new ArrayList<String>();
			AclBD aclBD = new AclBD(bd);
			List<Acl> aclOperatore = aclBD.getAclOperatore(operatore.getId());
			EstrattiContoMetadata estrattiContoBD = new EstrattiContoMetadata(bd);
			EstrattoContoFilter filter = estrattiContoBD.newFilter();

			// Operatore per controllo domini che puo' vedere
			filter.setOperatore(operatore);

			filter.setOffset(offset);
			filter.setLimit(limit);

			String idDominioId = Utils.getInstance(this.getLanguage()).getMessageFromResourceBundle(this.nomeServizio + ".idDominio.id");
			String idDominio = this.getParameter(uriInfo, idDominioId, String.class);

			if(StringUtils.isNotEmpty(idDominio)){
				long idDom = -1l;
				try{
					idDom = Long.parseLong(idDominio);
				}catch(Exception e){ idDom = -1l;	}
				if(idDom > 0){
					idDomini.add(AnagraficaManager.getDominio(bd, idDom).getCodDominio());
					filter.setIdDomini(idDomini);
				}
			}

			boolean eseguiRicerca = true; // isAdmin;
			// SE l'operatore non e' admin vede solo i versamenti associati ai domini definiti nelle ACL
			if(!isAdmin && idDomini.isEmpty()){
				boolean vediTuttiDomini = false;

				for(Acl acl: aclOperatore) {
					if(Tipo.DOMINIO.equals(acl.getTipo())) {
						if(acl.getIdDominio() == null) {
							vediTuttiDomini = true;
							break;
						} else {
							idDomini.add(AnagraficaManager.getDominio(bd, acl.getIdDominio()).getCodDominio());
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

			List<it.govpay.model.reportistica.EstrattoContoMetadata> findAll = eseguiRicerca ? estrattiContoBD.findAll(filter) : null;
			long count = findAll != null ? findAll.size() : 0 ;

			// visualizza la ricerca solo se i risultati sono > del limit
			boolean visualizzaRicerca = this.visualizzaRicerca(count, limit);
			InfoForm infoRicerca = this.getInfoRicerca(uriInfo, bd, visualizzaRicerca);

			List<String> titoli = new ArrayList<String>();
			titoli.add(Utils.getInstance(this.getLanguage()).getMessageFromResourceBundle(this.nomeServizio + ".idDominio.label"));
			titoli.add(Utils.getInstance(this.getLanguage()).getMessageFromResourceBundle(this.nomeServizio + ".riferimento.label"));
			titoli.add(Utils.getInstance(this.getLanguage()).getMessageFromResourceBundle(this.nomeServizio + ".formato.label"));
			titoli.add(Utils.getInstance(this.getLanguage()).getMessageFromResourceBundle(this.nomeServizio + ".ibanAccredito.label"));

//			Elemento intestazione = new Elemento(-1, titoli , null);

			Elenco elenco = new Elenco(this.titoloServizio, infoRicerca,
					this.getInfoCreazione(uriInfo, bd),
					count, esportazione, cancellazione); 

			if(findAll != null && findAll.size() > 0){
				for (it.govpay.model.reportistica.EstrattoContoMetadata entry : findAll) {
					elenco.getElenco().add(this.getElemento(entry, entry.getId(), this.pathServizio,bd));
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
		try{
			URI ricerca = this.getUriRicerca(uriInfo, bd);
			InfoForm infoRicerca = new InfoForm(ricerca);

			if(visualizzaRicerca){

				Operatore operatore = this.darsService.getOperatoreByPrincipal(bd);
				ProfiloOperatore ruolo = operatore.getProfilo();
				boolean isAdmin = ruolo.equals(ProfiloOperatore.ADMIN);

				String idDominioId = Utils.getInstance(this.getLanguage()).getMessageFromResourceBundle(this.nomeServizio + ".idDominio.id");

				if(this.infoRicercaMap == null){
					this.initInfoRicerca(uriInfo, bd);
				}

				List<Voce<Long>> domini = new ArrayList<Voce<Long>>();
				Sezione sezioneRoot = infoRicerca.getSezioneRoot();

				try{
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
			}
			return infoRicerca;
		}catch(Exception e){
			throw new ConsoleException(e);
		}
	}

	private void initInfoRicerca(UriInfo uriInfo, BasicBD bd) throws ConsoleException{
		if(this.infoRicercaMap == null){
			this.infoRicercaMap = new HashMap<String, ParamField<?>>();

			String idDominioId = Utils.getInstance(this.getLanguage()).getMessageFromResourceBundle(this.nomeServizio + ".idDominio.id");

			// statoTracciato
			String idDominioLabel = Utils.getInstance(this.getLanguage()).getMessageFromResourceBundle(this.nomeServizio + ".idDominio.label");
			List<Voce<Long>> domini = new ArrayList<Voce<Long>>();

			domini.add(new Voce<Long>(Utils.getInstance(this.getLanguage()).getMessageFromResourceBundle("commons.label.qualsiasi"), null));
			SelectList<Long> idDominio  = new SelectList<Long>(idDominioId, idDominioLabel, null, false, false, true, domini );
			this.infoRicercaMap.put(idDominioId, idDominio);
		}
	}

	@Override
	public InfoForm getInfoCreazione(UriInfo uriInfo, BasicBD bd) throws ConsoleException {
		return null;
	}

	@Override
	public InfoForm getInfoModifica(UriInfo uriInfo, BasicBD bd, it.govpay.model.reportistica.EstrattoContoMetadata entry) throws ConsoleException {
		return null;
	}

	@Override
	public Object getField(UriInfo uriInfo,List<RawParamValue>values, String fieldId,BasicBD bd) throws WebApplicationException,ConsoleException {
		return null;
	}

	@Override
	public Dettaglio getDettaglio(long id, UriInfo uriInfo, BasicBD bd) throws WebApplicationException,ConsoleException {
		String methodName = "dettaglio " + this.titoloServizio + ".Id "+ id;

		try{
			this.log.info("Esecuzione " + methodName + " in corso...");
			Operatore operatore = this.darsService.getOperatoreByPrincipal(bd); 

			// recupero oggetto
			EstrattiContoMetadata estrattiContoBD = new EstrattiContoMetadata(bd);
			it.govpay.model.reportistica.EstrattoContoMetadata estrattoConto = estrattiContoBD.getEstrattoConto(id,operatore.getPrincipal()); 

			InfoForm infoModifica = null;
			URI cancellazione = null;
			URI esportazione = null;

			Dettaglio dettaglio = new Dettaglio(this.getTitolo(estrattoConto,bd), esportazione, cancellazione, infoModifica);

			it.govpay.web.rs.dars.model.Sezione root = dettaglio.getSezioneRoot(); 

			try{
				if(estrattoConto.getCodDominio() != null){
					DominiBD dominiBD = new DominiBD(bd);
					Dominio dominio = dominiBD.getDominio(estrattoConto.getCodDominio());
					Domini dominiDars =new Domini();
					String titolo = ((DominiHandler)dominiDars.getDarsHandler()).getTitolo(dominio, bd);
					root.addVoce(Utils.getInstance(this.getLanguage()).getMessageFromResourceBundle(this.nomeServizio + ".idDominio.label"), titolo);
				}

			}catch(Exception e ){
				root.addVoce(Utils.getInstance(this.getLanguage()).getMessageFromResourceBundle(this.nomeServizio + ".idDominio.label"), estrattoConto.getCodDominio());
			}

			URI uriCsv = Utils.creaUriConPath(this.pathServizio,  estrattoConto.getId() + "", "estrattoConto");
			root.addVoce(Utils.getInstance(this.getLanguage()).getMessageFromResourceBundle(this.nomeServizio + ".riferimento.label"), estrattoConto.getMeseAnno());
			root.addVoce(Utils.getInstance(this.getLanguage()).getMessageFromResourceBundle(this.nomeServizio + ".formato.label"), estrattoConto.getFormato());
			if(StringUtils.isNotEmpty(estrattoConto.getIbanAccredito())) {
				root.addVoce(Utils.getInstance(this.getLanguage()).getMessageFromResourceBundle(this.nomeServizio + ".ibanAccredito.label"), estrattoConto.getIbanAccredito());
			}

			root.addVoce(Utils.getInstance(this.getLanguage()).getMessageFromResourceBundle(this.nomeServizio + ".nomeFile.label"), estrattoConto.getNomeFile());
			root.addDownloadLink(Utils.getInstance(this.getLanguage()).getMessageFromResourceBundle(this.nomeServizio + ".estrattoConto.label"), 
					Utils.getInstance(this.getLanguage()).getMessageFromResourceBundle("commons.label.scarica"),uriCsv); 

			this.log.info("Esecuzione " + methodName + " completata.");

			return dettaglio;
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
	public Dettaglio insert(InputStream is, UriInfo uriInfo, BasicBD bd) throws WebApplicationException, ConsoleException, ValidationException, DuplicatedEntryException{
		return null;
	}

	@Override
	public it.govpay.model.reportistica.EstrattoContoMetadata creaEntry(InputStream is, UriInfo uriInfo, BasicBD bd)	throws WebApplicationException, ConsoleException {
		return null;
	}

	@Override
	public void checkEntry(it.govpay.model.reportistica.EstrattoContoMetadata entry, it.govpay.model.reportistica.EstrattoContoMetadata oldEntry) throws ValidationException {
	}

	@Override
	public Dettaglio update(InputStream is, UriInfo uriInfo, BasicBD bd)
			throws WebApplicationException, ConsoleException, ValidationException {
		// update non prevista
		return null;
	}

	@Override
	public String getTitolo(it.govpay.model.reportistica.EstrattoContoMetadata entry, BasicBD bd)  throws ConsoleException{
		StringBuilder sb = new StringBuilder();

		String dominioLabel = entry.getCodDominio();
		try{
			if(entry.getCodDominio() != null){
				DominiBD dominiBD = new DominiBD(bd);
				Dominio dominio = dominiBD.getDominio(entry.getCodDominio());
				Domini dominiDars =new Domini();
				dominioLabel = ((DominiHandler)dominiDars.getDarsHandler()).getTitolo(dominio, bd);
			}

		}catch(Exception e ){
			dominioLabel = entry.getCodDominio(); 
		}
		String periodoRiferimento = entry.getMeseAnno();
		sb.append(Utils.getInstance(this.getLanguage()).getMessageWithParamsFromResourceBundle(this.nomeServizio + ".label.titolo", dominioLabel,periodoRiferimento));
		return sb.toString();
	}

	@Override
	public String getSottotitolo(it.govpay.model.reportistica.EstrattoContoMetadata entry, BasicBD bd)  throws ConsoleException {
		StringBuilder sb = new StringBuilder();
		return sb.toString();
	}

	@Override
	public List<String> getValori(it.govpay.model.reportistica.EstrattoContoMetadata entry, BasicBD bd) throws ConsoleException {
		List<String> valori = new ArrayList<String>();

		String dominioLabel = entry.getCodDominio();
		try{
			if(entry.getCodDominio() != null){
				DominiBD dominiBD = new DominiBD(bd);
				Dominio dominio = dominiBD.getDominio(entry.getCodDominio());
				Domini dominiDars =new Domini();
				dominioLabel = ((DominiHandler)dominiDars.getDarsHandler()).getTitolo(dominio, bd);
			}

		}catch(Exception e ){
			dominioLabel = entry.getCodDominio(); 
		}

		valori.add(dominioLabel);
		valori.add(entry.getMeseAnno());
		valori.add(entry.getFormato());

		if(StringUtils.isNotEmpty(entry.getIbanAccredito())) {
			valori.add(entry.getIbanAccredito());
		} else {
			valori.add("--");
		} 

		return valori;
	}
	
	@Override
	public Map<String, Voce<String>> getVoci(it.govpay.model.reportistica.EstrattoContoMetadata entry, BasicBD bd) throws ConsoleException { return null; }

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

		String methodName = "esporta " + this.titoloServizio + "[" + sb.toString() + "]";

		if(idsToExport.size() == 1) {
			return this.esporta(idsToExport.get(0), uriInfo, bd, zout);
		} 

		String fileZipName = "EstrattiConto.zip";
		try{
			this.log.info("Esecuzione " + methodName + " in corso...");
			Operatore operatore = this.darsService.getOperatoreByPrincipal(bd); 

			EstrattiContoMetadata estrattiContoBD = new EstrattiContoMetadata(bd);

			for (Long idTracciato : idsToExport) {

				ByteArrayOutputStream baos = new ByteArrayOutputStream();
				String fileName = estrattiContoBD.getCSVEstrattoConto(idTracciato,operatore.getPrincipal(), baos); 

				String folderName = fileName;
				int indexOfCsv = folderName.indexOf(".csv");
				if(indexOfCsv > -1){
					folderName = folderName.substring(0, indexOfCsv);
				}

				ZipEntry estrattoContoEntry = new ZipEntry(folderName + "/" + fileName);
				zout.putNextEntry(estrattoContoEntry);
				zout.write(baos.toByteArray());
				zout.closeEntry();
			}
			zout.flush();
			zout.close();

			this.log.info("Esecuzione " + methodName + " completata.");

			return fileZipName;
		}catch(WebApplicationException e){
			throw e;
		}catch(Exception e){
			throw new ConsoleException(e);
		}
	}

	@Override
	public String esporta(Long idToExport, UriInfo uriInfo, BasicBD bd, ZipOutputStream zout)	throws WebApplicationException, ConsoleException {
		String methodName = "esporta " + this.titoloServizio + "[" + idToExport + "]";  


		try{
			this.log.info("Esecuzione " + methodName + " in corso...");
			Operatore operatore = this.darsService.getOperatoreByPrincipal(bd); 

			// recupero oggetto
			ByteArrayOutputStream baos = new ByteArrayOutputStream();
			EstrattiContoMetadata estrattiContoBD = new EstrattiContoMetadata(bd);
			String fileName = estrattiContoBD.getCSVEstrattoConto(idToExport,operatore.getPrincipal(), baos); 

			String fileZipName = fileName;
			int indexOfCsv = fileName.indexOf(".csv");
			if(indexOfCsv > -1){
				fileZipName = fileName.substring(0, indexOfCsv);
			}

			fileZipName = fileZipName+".zip";

			ZipEntry estrattoContoEntry = new ZipEntry(fileName);
			zout.putNextEntry(estrattoContoEntry);
			zout.write(baos.toByteArray());
			zout.closeEntry();

			zout.flush();
			zout.close();

			this.log.info("Esecuzione " + methodName + " completata.");

			return fileZipName;
		}catch(WebApplicationException e){
			throw e;
		}catch(Exception e){
			throw new ConsoleException(e);
		}
	}

	@Override
	public Object uplaod(MultipartFormDataInput input, UriInfo uriInfo, BasicBD bd)	throws WebApplicationException, ConsoleException, ValidationException {	
		return null;
	}

	public String getFile(Long id, UriInfo uriInfo, BasicBD bd, ByteArrayOutputStream baos)	throws WebApplicationException, ConsoleException {
		String methodName = "getFile Estratto Conto " + this.titoloServizio + ".Id "+ id;

		try{
			this.log.info("Esecuzione " + methodName + " in corso...");
			Operatore operatore = this.darsService.getOperatoreByPrincipal(bd);  

			// recupero oggetto
			EstrattiContoMetadata  estrattiContoBD = new EstrattiContoMetadata(bd);
			String fileName = estrattiContoBD.getCSVEstrattoConto(id,operatore.getPrincipal(), baos); 

			this.log.info("Esecuzione " + methodName + " completata.");

			return fileName;
		}catch(WebApplicationException e){
			throw e;
		}catch(Exception e){
			throw new ConsoleException(e);
		}
	}


}
