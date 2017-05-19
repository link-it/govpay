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
package it.govpay.web.rs.dars;

import java.io.InputStream;
import java.lang.reflect.Constructor;
import java.net.URI;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.zip.ZipOutputStream;

import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.MultivaluedMap;
import javax.ws.rs.core.UriInfo;

import org.apache.logging.log4j.Logger;
import org.jboss.resteasy.plugins.providers.multipart.MultipartFormDataInput;
import org.openspcoop2.generic_project.exception.ServiceException;
import org.openspcoop2.utils.csv.Format;
import org.openspcoop2.utils.csv.FormatReader;

import it.govpay.bd.BasicBD;
import it.govpay.core.utils.CSVSerializerProperties;
import it.govpay.model.Versionabile.Versione;
import it.govpay.web.rs.dars.exception.ConsoleException;
import it.govpay.web.rs.dars.exception.DeleteException;
import it.govpay.web.rs.dars.exception.DuplicatedEntryException;
import it.govpay.web.rs.dars.exception.ExportException;
import it.govpay.web.rs.dars.exception.ValidationException;
import it.govpay.web.rs.dars.model.Dettaglio;
import it.govpay.web.rs.dars.model.Elemento;
import it.govpay.web.rs.dars.model.Elenco;
import it.govpay.web.rs.dars.model.InfoForm;
import it.govpay.web.rs.dars.model.RawParamValue;
import it.govpay.web.rs.dars.model.Voce;
import it.govpay.web.rs.dars.model.input.base.SelectList;
import it.govpay.web.utils.ConsoleProperties;
import it.govpay.web.utils.Utils;
import net.sf.json.JSONObject;

public abstract class BaseDarsHandler<T> implements IDarsHandler<T>{

	protected Logger log ;

	protected String nomeServizio = null;
	protected String pathServizio = null;
	protected String titoloServizio = null;
	protected BaseDarsService darsService = null;
	private Integer limit = null;
	private Format formatW= null;

	public BaseDarsHandler(Logger log, BaseDarsService darsService){
		this.log = log;
		this.darsService = darsService;
		this.nomeServizio = this.darsService.getNomeServizio();
		this.pathServizio = this.darsService.getPathServizio();
		this.titoloServizio = Utils.getInstance(this.getLanguage()).getMessageFromResourceBundle(this.nomeServizio + ".titolo");
		this.limit = ConsoleProperties.getInstance().getNumeroRisultatiPerPagina();

		try{
			// Setto le properties di scrittura
			FormatReader formatWriter = new FormatReader(CSVSerializerProperties.getInstance(log).getProperties());
			this.formatW = formatWriter.getFormat();
		}catch(Exception e){
			log.error("Errore durante l'inizializzazione di EstrattoConto: " + e.getMessage(),e);
		}
	}

	@Override
	public abstract Elenco getElenco(UriInfo uriInfo, BasicBD bd) throws WebApplicationException,ConsoleException;
	@Override
	public InfoForm getInfoRicerca(UriInfo uriInfo, BasicBD bd ) throws ConsoleException{
		return this.getInfoRicerca(uriInfo, bd, true);
	}

	@Override
	public InfoForm getInfoRicerca(UriInfo uriInfo, BasicBD bd, boolean visualizzaRicerca) throws ConsoleException{
		return this.getInfoRicerca(uriInfo, bd, visualizzaRicerca, null);
	}

	@Override
	public InfoForm getInfoRicerca(UriInfo uriInfo, BasicBD bd, Map<String, String> parameters)
			throws ConsoleException {
		return this.getInfoRicerca(uriInfo, bd, true, parameters);
	}

	@Override
	public abstract InfoForm getInfoRicerca(UriInfo uriInfo, BasicBD bd, boolean visualizzaRicerca, Map<String,String> parameters ) throws ConsoleException;

	@Override
	public URI getUriRicerca(UriInfo uriInfo, BasicBD bd) throws ConsoleException{
		try{
			URI uri = new URI(this.pathServizio);
			return uri;
		}catch(Exception e){
			throw new ConsoleException(e);
		}
	}

	@Override
	public URI getUriRicerca(UriInfo uriInfo, BasicBD bd, Map<String, String> parameters) throws ConsoleException {
		return Utils.creaUriConParametri(this.pathServizio,parameters);
	}

	@Override
	public abstract InfoForm getInfoCreazione(UriInfo uriInfo,BasicBD bd) throws ConsoleException;
	@Override
	public URI getUriCreazione(UriInfo uriInfo, BasicBD bd) throws ConsoleException{
		try{
			URI uri = new URI(this.pathServizio);
			return uri;
		}catch(Exception e){
			throw new ConsoleException(e);
		}
	}
	@Override
	public abstract InfoForm getInfoModifica(UriInfo uriInfo,BasicBD bd, T entry) throws ConsoleException;
	@Override
	public URI getUriModifica(UriInfo uriInfo, BasicBD bd) throws ConsoleException{
		try{
			URI uri = new URI(this.pathServizio);
			return uri;
		}catch(Exception e){
			throw new ConsoleException(e);
		}
	}
	
	@Override
	public abstract InfoForm getInfoCancellazione(UriInfo uriInfo,BasicBD bd) throws ConsoleException;

	@Override
	public URI getUriCancellazione(UriInfo uriInfo, BasicBD bd)throws ConsoleException{
		return this.getUriCancellazione(uriInfo, bd, null);
	}
	
	@Override
	public URI getUriCancellazione(UriInfo uriInfo, BasicBD bd, Map<String, String> parameters)
			throws ConsoleException {
		try{
			URI uri =Utils.creaUriConPathEParametri(this.pathServizio, parameters, BaseDarsService.PATH_CANCELLA);
			return uri;
		}catch(Exception e){
			throw new ConsoleException(e);
		}
	}

	@Override
	public URI getUriEsportazione(UriInfo uriInfo, BasicBD bd)throws ConsoleException{
		return this.getUriEsportazione(uriInfo, bd, null);
	}
	
	@Override
	public URI getUriEsportazione(UriInfo uriInfo, BasicBD bd, Map<String, String> parameters) throws ConsoleException {
		try{
			URI uri = Utils.creaUriConPathEParametri(this.pathServizio,parameters, BaseDarsService.PATH_ESPORTA);
			return uri;
		}catch(Exception e){
			throw new ConsoleException(e);
		}
	}
	
	@Override
	public abstract InfoForm getInfoCancellazioneDettaglio(UriInfo uriInfo,BasicBD bd, T entry) throws ConsoleException;

	@Override
	public URI getUriCancellazioneDettaglio(UriInfo uriInfo, BasicBD bd, long id)throws ConsoleException{
		try{
			URI uri = Utils.creaUriConPath(this.pathServizio, id+"" , BaseDarsService.PATH_CANCELLA); 
			return uri;
		}catch(Exception e){
			throw new ConsoleException(e);
		}
	}

	@Override
	public URI getUriEsportazioneDettaglio(UriInfo uriInfo, BasicBD bd, long id)throws ConsoleException{
		try{
			URI uri = Utils.creaUriConPath(this.pathServizio, id+"" , BaseDarsService.PATH_ESPORTA); 
			return uri;
		}catch(Exception e){
			throw new ConsoleException(e);
		}
	}

	@Override
	public URI getUriUpload(UriInfo uriInfo, BasicBD bd) throws ConsoleException {
		try{
			URI uri = Utils.creaUriConPath(this.pathServizio, BaseDarsService.PATH_UPLOAD);
			return uri;
		}catch(Exception e){
			throw new ConsoleException(e);
		}
	}

	@Override
	public abstract Object getField(UriInfo uriInfo,List<RawParamValue>values, String fieldId,BasicBD bd) throws WebApplicationException,ConsoleException ;
	@Override
	public URI getUriField(UriInfo uriInfo, BasicBD bd, String fieldName) throws ConsoleException {
		try{
			URI uri =  Utils.creaUriConPath(this.pathServizio, BaseDarsService.PATH_FIELD , fieldName);
			return uri;
		}catch(Exception e){
			throw new ConsoleException(e);
		} 
	}

	@Override
	public abstract Dettaglio getDettaglio(long id, UriInfo uriInfo,BasicBD bd) throws WebApplicationException,ConsoleException;
	@Override
	public abstract Elenco delete(List<Long> idsToDelete, List<RawParamValue> rawValues, UriInfo uriInfo, BasicBD bd) throws WebApplicationException,ConsoleException,DeleteException;
	@Override
	public abstract T creaEntry(InputStream is, UriInfo uriInfo, BasicBD bd) throws WebApplicationException,ConsoleException;
	@Override
	public abstract Dettaglio insert(InputStream is, UriInfo uriInfo, BasicBD bd) throws WebApplicationException,ConsoleException,ValidationException,DuplicatedEntryException;
	@Override
	public abstract void checkEntry(T entry, T oldEntry) throws ValidationException;
	@Override
	public abstract Dettaglio update(InputStream is, UriInfo uriInfo, BasicBD bd) throws WebApplicationException,ConsoleException,ValidationException;

	@Override
	public abstract Object uplaod(MultipartFormDataInput input, UriInfo uriInfo, BasicBD bd)	throws WebApplicationException, ConsoleException, ValidationException;

	@Override
	public  abstract String getTitolo(T entry, BasicBD bd) throws ConsoleException;
	@Override
	public  abstract String getSottotitolo(T entry, BasicBD bd) throws ConsoleException;

	@Override
	public abstract String esporta(List<Long> idsToExport, UriInfo uriInfo, BasicBD bd, ZipOutputStream zout) throws WebApplicationException,ConsoleException,ExportException;

	@Override
	public abstract String esporta(Long idToExport, UriInfo uriInfo, BasicBD bd, ZipOutputStream zout)throws WebApplicationException, ConsoleException,ExportException;

	public Elemento getElemento(T entry, Long id, String uriDettaglio, BasicBD bd) throws ConsoleException{
		try{
			String titolo = this.getTitolo(entry,bd);
			String sottotitolo = this.getSottotitolo(entry,bd);
			URI urlDettaglio = (id != null && uriDettaglio != null) ?  Utils.creaUriConPath(uriDettaglio , id+"") : null;
			Elemento elemento = new Elemento(id, titolo, sottotitolo, urlDettaglio);
			elemento.setVoci(this.getVoci(entry, bd)); 
			return elemento;
		}catch(Exception e) {throw new ConsoleException(e);}
	}

	@Override
	public abstract Map<String, Voce<String>> getVoci(T entry, BasicBD bd) throws ConsoleException;

	public <P> P getParameter(UriInfo uriInfo, String parameterName, Class<P> type) throws ConsoleException{
		P toReturn = null;
		try{
			MultivaluedMap<String, String> queryParams = uriInfo.getQueryParameters(); 

			String paramAsString = queryParams.getFirst(parameterName);
			if(paramAsString != null){
				log.debug("Trovato Parametro ["+parameterName+"] class["+paramAsString.getClass().getName()+"] provo il cast a ["+type.getName()+"]");
				Constructor<P> constructor = type.getConstructor(String.class);
				if(constructor != null)
					toReturn = constructor.newInstance(paramAsString);
				else
					toReturn = type.cast(paramAsString);
			}
		}catch(Exception e){
			throw new ConsoleException(e);
		}

		return toReturn;
	}
	
	public boolean containsParameter(UriInfo uriInfo, String parameterName) throws ConsoleException{
		boolean toReturn = false;
		try{
			MultivaluedMap<String, String> queryParams = uriInfo.getQueryParameters(); 
			toReturn = queryParams.getFirst(parameterName) != null;
		}catch(Exception e){
			throw new ConsoleException(e);
		}

		return toReturn;
	}

	public Integer getOffset(UriInfo uriInfo) throws ConsoleException{
		return this.getParameter(uriInfo, "offset", Integer.class);
	}

	public Integer getLimit(UriInfo uriInfo) throws ConsoleException{
		Integer parameter = this.getParameter(uriInfo, "limit", Integer.class);
		return parameter != null ? parameter : this.limit;
	}

	public boolean visualizzaRicerca(long count, long limit){
		boolean nascondi = false;
		if(ConsoleProperties.getInstance().isNascondiRicerca()){
			if(count < limit)
				nascondi = true;
		}

		return !nascondi;
	}

	public SelectList<String> getSelectListVersione(String versioneId){
		if(versioneId == null) versioneId = Utils.getInstance(this.getLanguage()).getMessageFromResourceBundle(this.nomeServizio + ".versione.id");

		String firmaRichiestaLabel = Utils.getInstance(this.getLanguage()).getMessageFromResourceBundle(this.nomeServizio + ".versione.label");
		List<Voce<String>> valoriVersione = new ArrayList<Voce<String>>(); 
		
		Versione[] values = Versione.values();
		for (Versione versione : values) {
			valoriVersione.add(new Voce<String>(versione.getLabel(), versione.getLabel()));
		}
		SelectList<String> versione = new SelectList<String>(versioneId, firmaRichiestaLabel, null, true, false, true, valoriVersione);
		versione.setAvanzata(true);
		versione.setDefaultValue(Versione.getUltimaVersione().getLabel()); 

		return versione;
	}

	public Versione getVersioneSelezionata(JSONObject jsonObject, String versioneId, boolean remove) throws ServiceException{
		if(versioneId == null) versioneId = Utils.getInstance(this.getLanguage()).getMessageFromResourceBundle(this.nomeServizio + ".versione.id");

		String versioneJson = jsonObject.getString(versioneId);

		if(remove)
			jsonObject.remove(versioneId);

		return Versione.toEnum(versioneJson);
	}

	public Date convertJsonStringToDate(String dateJson) throws Exception{
		return BaseDarsService.convertJsonStringToDate(dateJson);
	}

	@Override
	public Format getFormat() {
		return this.formatW;
	}
	@Override
	public Locale getLanguage(){
		return this.darsService.getLanguage();
	}
}
