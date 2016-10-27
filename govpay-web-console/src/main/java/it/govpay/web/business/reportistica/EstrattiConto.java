package it.govpay.web.business.reportistica;

import java.io.ByteArrayOutputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.TreeMap;
import java.util.Map.Entry;

import org.apache.commons.codec.binary.Base64;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.io.IOUtils;
import org.apache.http.HttpEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.log4j.Logger;
import org.openspcoop2.generic_project.exception.NotFoundException;
import org.openspcoop2.generic_project.exception.ServiceException;
import org.openspcoop2.generic_project.expression.SortOrder;

import it.govpay.bd.BasicBD;
import it.govpay.bd.FilterSortWrapper;
import it.govpay.bd.anagrafica.DominiBD;
import it.govpay.bd.anagrafica.filters.DominioFilter;
import it.govpay.bd.model.Dominio;
import it.govpay.bd.reportistica.filters.EstrattoContoFilter;
import it.govpay.model.Operatore;
import it.govpay.model.Operatore.ProfiloOperatore;
import it.govpay.model.reportistica.EstrattoContoMetadata;
import it.govpay.web.business.reportistica.utils.EstrattoContoComparator;
import it.govpay.web.utils.ConsoleProperties;
import net.sf.json.JSONArray;

public class EstrattiConto extends BasicBD{

	private CloseableHttpClient httpClient;
	private Logger log = Logger.getLogger(EstrattiConto.class);
	private SimpleDateFormat f3 = new SimpleDateFormat("yyyy/MM");
	
	public static final String FORMATO_CSV = "csv";
	public static final String FORMATO_PDF = "pdf";
	public static final String FORMATO_STAR = "*";

	private static TreeMap<String, LinkedHashMap<Long, EstrattoContoMetadata>> mapEC = null;  

	public EstrattiConto(BasicBD basicBD) {
		super(basicBD);
		this.httpClient = HttpClientBuilder.create().build();
		if(mapEC == null)
			mapEC = new TreeMap<String, LinkedHashMap<Long, EstrattoContoMetadata>>();
	}


	public EstrattoContoFilter newFilter() throws ServiceException {
		return new EstrattoContoFilter(this.getPagamentoService());
	}


	public List<EstrattoContoMetadata> findAll(EstrattoContoFilter filter) throws ServiceException {
		try{
			if(mapEC == null)
				mapEC = new TreeMap<String, LinkedHashMap<Long, EstrattoContoMetadata>>();

			Operatore operatore = filter.getOperatore();
			ProfiloOperatore ruolo = operatore.getProfilo() ;
			boolean isAdmin = ruolo.equals(ProfiloOperatore.ADMIN);

			DominiBD dominiBd = new DominiBD(this);
			DominioFilter dominioFilter = dominiBd.newFilter(); 
			FilterSortWrapper fsw = new FilterSortWrapper();
			fsw.setField(it.govpay.orm.Dominio.model().COD_DOMINIO);
			fsw.setSortOrder(SortOrder.ASC);
			dominioFilter.getFilterSortList().add(fsw);

			boolean eseguiRicerca = isAdmin;
			
			if(!eseguiRicerca)
				eseguiRicerca = !filter.getIdDomini().isEmpty();				

			List<EstrattoContoMetadata> lst = new ArrayList<EstrattoContoMetadata>();

			if(eseguiRicerca){
				//filtro sui domini disponibili all'utente;
				dominioFilter.setIdDomini(filter.getIdDomini());

				List<Dominio> findAll = dominiBd.findAll(dominioFilter );
				long id = 1;

				TreeMap<Long, EstrattoContoMetadata> mapUtente = new TreeMap<Long, EstrattoContoMetadata>();

				for (Dominio dominio : findAll) {

					this.log.info("Get Lista Estratti Conto per il Dominio:" + dominio.getCodDominio());

					String urlEstrattoContoDominio = ConsoleProperties.getInstance().getUrlEstrattoConto() + "/" + dominio.getCodDominio();
					this.log.info("Govpay URL: " + urlEstrattoContoDominio);
					HttpGet get = new HttpGet(urlEstrattoContoDominio);
					String encoding = Base64.encodeBase64String((ConsoleProperties.getInstance().getUsernameEstrattoConto()+":" +ConsoleProperties.getInstance().getPasswordEstrattoConto()).getBytes());
					get.setHeader("Authorization", "Basic " + encoding);

					CloseableHttpResponse httpResponse = this.httpClient.execute(get);
					this.log.info("Get Lista Estratti Conto per il Dominio:" + dominio.getCodDominio()+ " completato");

					HttpEntity responseEntity = httpResponse.getEntity();
					ByteArrayOutputStream baos = new ByteArrayOutputStream();
					IOUtils.copy(responseEntity.getContent(), baos);
					
					if(httpResponse.getStatusLine().getStatusCode() == 200) {
						JSONArray listaJSON = JSONArray.fromObject( baos.toString());

						for (int i =0 ; i< listaJSON.size() ; i++) {
							Object object = listaJSON.get(i);

							EstrattoContoMetadata estrattoContoFromJson = this.getEstrattoContoFromJson(dominio.getCodDominio(), id, (String) object);
							mapUtente.put(id, estrattoContoFromJson);

							id ++;
						}
					} else {
						log.error(baos.toString()); 
					}
				}

				// aggiorno il contenuto della mappa.
				if(mapEC.containsKey(filter.getOperatore().getPrincipal()))
					mapEC.remove(filter.getOperatore().getPrincipal());

				
				List<Entry<Long, EstrattoContoMetadata>> listOfEntries = new ArrayList<Entry<Long, EstrattoContoMetadata>>(mapUtente.entrySet());
				// sorting HashMap by values using comparator
		        Collections.sort(listOfEntries, new EstrattoContoComparator());

		        LinkedHashMap<Long, EstrattoContoMetadata> orderedMapUtente = new LinkedHashMap<Long, EstrattoContoMetadata>(listOfEntries.size());
		        // copying entries from List to Map
		        for(Entry<Long, EstrattoContoMetadata> entry : listOfEntries){
		        	orderedMapUtente.put(entry.getKey(), entry.getValue());
		        }
				
		        lst = new ArrayList<EstrattoContoMetadata>(orderedMapUtente.values());
				mapEC.put(filter.getOperatore().getPrincipal(), orderedMapUtente);
			}

			return lst;

		}catch (Exception e) {
			throw new ServiceException(e);
		}
	}

	private EstrattoContoMetadata getEstrattoContoFromJson(String codDominio, long id, String fileName){
		EstrattoContoMetadata estrattoConto = new EstrattoContoMetadata();
		estrattoConto.setCodDominio(codDominio);
		estrattoConto.setNomeFile(fileName);
		estrattoConto.setId(id);

		try{
			// decodificare l'estensione del file
			String extension = FilenameUtils.getExtension(fileName);
			estrattoConto.setFormato(extension);
			fileName = FilenameUtils.removeExtension(fileName);
			
			String[] split = fileName.split("_");

			if(split != null && split.length > 0 ){
				boolean formatoOk = false;
				// formato Nome file codDominio_anno_mese				
				if(split.length == 3){
					estrattoConto.setAnno(Integer.parseInt(split[1]));
					estrattoConto.setMese(Integer.parseInt(split[2]));
					formatoOk = true;
				}

				// formato Nome file codDominio_iban_anno_mese
				if(split.length == 4){
					estrattoConto.setIbanAccredito(split[1]); 
					estrattoConto.setAnno(Integer.parseInt(split[2]));
					estrattoConto.setMese(Integer.parseInt(split[3]));
					formatoOk = true;
				}

				if(formatoOk){
					Calendar cal = Calendar.getInstance();
					cal.set(Calendar.MILLISECOND, 0);
					cal.set(Calendar.SECOND, 0);
					cal.set(Calendar.MINUTE, 0);
					cal.set(Calendar.HOUR_OF_DAY, 0);
					cal.set(Calendar.DAY_OF_MONTH, 1);
					cal.set(Calendar.MONTH, estrattoConto.getMese() -1);
					cal.set(Calendar.YEAR, estrattoConto.getAnno());

					estrattoConto.setMeseAnno(this.f3.format(cal.getTime()));
					
				}else{
					this.log.debug("Formato nome del file ["+fileName+"] non valido, impossibile identificare mese/anno dell'estratto conto.");
				}
			}
		} catch(Exception e){
			this.log.error(e.getMessage(),e);
		}


		return estrattoConto;
	}


	public  EstrattoContoMetadata getEstrattoConto(long id, String operatore) throws ServiceException, NotFoundException {
		if(mapEC == null)
			return null;

		LinkedHashMap<Long, EstrattoContoMetadata> linkedMap = mapEC.get(operatore);
		if(linkedMap == null)
			return null;

		return linkedMap.get(id);
	}

	public String getCSVEstrattoConto(long id, String operatore, ByteArrayOutputStream baos) throws ServiceException, NotFoundException {
		EstrattoContoMetadata ec = this.getEstrattoConto(id, operatore);

		try{

			String urlEstrattoContoDominio = ConsoleProperties.getInstance().getUrlEstrattoConto() + "/" + ec.getCodDominio() + "/" + ec.getNomeFile();
			this.log.info("Govpay URL: " + urlEstrattoContoDominio);
			HttpGet get = new HttpGet(urlEstrattoContoDominio);
			String encoding = Base64.encodeBase64String((ConsoleProperties.getInstance().getUsernameEstrattoConto()+":" +ConsoleProperties.getInstance().getPasswordEstrattoConto()).getBytes());
			get.setHeader("Authorization", "Basic " + encoding);

			CloseableHttpResponse httpResponse = this.httpClient.execute(get);
			HttpEntity responseEntity = httpResponse.getEntity();
			IOUtils.copy(responseEntity.getContent(), baos);
			if(httpResponse.getStatusLine().getStatusCode() == 200) {
				log.info("Get Estratto Conto terminata con codice http: " + httpResponse.getStatusLine().getStatusCode()); 
			} else {
				log.error(baos.toString()); 
			}

			this.log.info("Get Estratto Conto per il file :" + ec.getNomeFile() + " completato");

		}catch (Exception e) {
			this.log.error(e.getMessage(),e);
			throw new ServiceException(e);
		}

		return ec.getNomeFile();
	}

}
