package it.govpay.web.business.reportistica;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map.Entry;
import java.util.TreeMap;

import org.apache.commons.codec.binary.Base64;
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
import it.govpay.bd.anagrafica.AnagraficaManager;
import it.govpay.bd.anagrafica.DominiBD;
import it.govpay.bd.anagrafica.filters.DominioFilter;
import it.govpay.bd.model.Dominio;
import it.govpay.bd.reportistica.filters.EstrattoContoFilter;
import it.govpay.model.reportistica.EstrattoContoMetadata;
import it.govpay.model.reportistica.comparator.EstrattoContoMetadataEntryComparator;
import it.govpay.web.utils.ConsoleProperties;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import net.sf.json.JsonConfig;

public class EstrattiContoMetadata extends BasicBD{

	private CloseableHttpClient httpClient;
	private Logger log = Logger.getLogger(EstrattiContoMetadata.class);
	private static TreeMap<String, LinkedHashMap<Long, EstrattoContoMetadata>> mapEC = null;  

	public EstrattiContoMetadata(BasicBD basicBD) {
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

			DominiBD dominiBd = new DominiBD(this);
			DominioFilter dominioFilter = dominiBd.newFilter(); 
			FilterSortWrapper fsw = new FilterSortWrapper();
			fsw.setField(it.govpay.orm.Dominio.model().COD_DOMINIO);
			fsw.setSortOrder(SortOrder.ASC);
			dominioFilter.getFilterSortList().add(fsw);

			boolean eseguiRicerca = !filter.getIdDomini().isEmpty();	

			List<EstrattoContoMetadata> lst = new ArrayList<EstrattoContoMetadata>();

			if(eseguiRicerca){
				List<Long> idDomini = new ArrayList<Long>();
				for(String codDominio: filter.getIdDomini()) {
					idDomini.add(AnagraficaManager.getDominio(dominiBd, codDominio).getId());
				}
				//filtro sui domini disponibili all'utente;
				dominioFilter.setIdDomini(idDomini);

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
						JsonConfig jsonConfig = new JsonConfig();
						jsonConfig.setRootClass(EstrattoContoMetadata.class);
						JSONArray listaJSON = JSONArray.fromObject( baos.toString());

						for (int i =0 ; i< listaJSON.size() ; i++) {
							JSONObject jsonObject = listaJSON.getJSONObject(i);
							EstrattoContoMetadata estrattoContoFromJson = (EstrattoContoMetadata) JSONObject.toBean( jsonObject, jsonConfig );
							estrattoContoFromJson.setId(id); 
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
		        Collections.sort(listOfEntries, new EstrattoContoMetadataEntryComparator());

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
