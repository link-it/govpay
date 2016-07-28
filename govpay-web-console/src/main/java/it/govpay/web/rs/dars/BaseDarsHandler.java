/*
 * GovPay - Porta di Accesso al Nodo dei Pagamenti SPC 
 * http://www.gov4j.it/govpay
 * 
 * Copyright (c) 2014-2016 Link.it srl (http://www.link.it).
 * 
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
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
import java.util.List;
import java.util.zip.ZipOutputStream;

import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.MultivaluedMap;
import javax.ws.rs.core.UriBuilder;
import javax.ws.rs.core.UriInfo;

import org.apache.logging.log4j.Logger;
import org.openspcoop2.generic_project.exception.ServiceException;

import it.govpay.bd.BasicBD;
import it.govpay.bd.model.Versionabile.Versione;
import it.govpay.web.rs.BaseRsService;
import it.govpay.web.rs.dars.exception.ConsoleException;
import it.govpay.web.rs.dars.exception.DuplicatedEntryException;
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

	public BaseDarsHandler(Logger log, BaseDarsService darsService){
		this.log = log;
		this.darsService = darsService;
		this.nomeServizio = this.darsService.getNomeServizio();
		this.pathServizio = this.darsService.getPathServizio();
		this.titoloServizio = Utils.getInstance().getMessageFromResourceBundle(this.nomeServizio + ".titolo");
		this.limit = ConsoleProperties.getInstance().getNumeroRisultatiPerPagina();
	}

	@Override
	public abstract Elenco getElenco(UriInfo uriInfo, BasicBD bd) throws WebApplicationException,ConsoleException;
	@Override
	public abstract InfoForm getInfoRicerca(UriInfo uriInfo, BasicBD bd) throws ConsoleException;
	@Override
	public URI getUriRicerca(UriInfo uriInfo, BasicBD bd) throws ConsoleException{
		try{
			URI uri = BaseRsService.checkDarsURI(uriInfo).path(this.pathServizio).build();
			return uri;
		}catch(Exception e){
			throw new ConsoleException(e);
		}
	}

	@Override
	public abstract InfoForm getInfoCreazione(UriInfo uriInfo,BasicBD bd) throws ConsoleException;
	@Override
	public URI getUriCreazione(UriInfo uriInfo, BasicBD bd) throws ConsoleException{
		try{
			URI uri = BaseRsService.checkDarsURI(uriInfo).path(this.pathServizio).build();
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
			URI uri = BaseRsService.checkDarsURI(uriInfo).path(this.pathServizio).build();
			return uri;
		}catch(Exception e){
			throw new ConsoleException(e);
		}
	}

	@Override
	public URI getUriCancellazione(UriInfo uriInfo, BasicBD bd)throws ConsoleException{
		try{
			URI uri = BaseRsService.checkDarsURI(uriInfo).path(this.pathServizio).path(BaseDarsService.PATH_CANCELLA).build();
			return uri;
		}catch(Exception e){
			throw new ConsoleException(e);
		}
	}

	@Override
	public URI getUriEsportazione(UriInfo uriInfo, BasicBD bd)throws ConsoleException{
		try{
			URI uri = BaseRsService.checkDarsURI(uriInfo).path(this.pathServizio).path(BaseDarsService.PATH_ESPORTA).build(); 
			return uri;
		}catch(Exception e){
			throw new ConsoleException(e);
		}
	}
	
	@Override
	public URI getUriCancellazioneDettaglio(UriInfo uriInfo, BasicBD bd, long id)throws ConsoleException{
		try{
			URI uri = BaseRsService.checkDarsURI(uriInfo).path(this.pathServizio).path("{id}").path(BaseDarsService.PATH_CANCELLA).build(id);
			return uri;
		}catch(Exception e){
			throw new ConsoleException(e);
		}
	}

	@Override
	public URI getUriEsportazioneDettaglio(UriInfo uriInfo, BasicBD bd, long id)throws ConsoleException{
		try{
			URI uri = BaseRsService.checkDarsURI(uriInfo).path(this.pathServizio).path("{id}").path(BaseDarsService.PATH_ESPORTA).build(id); 
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
			URI uri = BaseRsService.checkDarsURI(uriInfo).path(this.pathServizio).path(BaseDarsService.PATH_FIELD).path(fieldName).build(); 
			return uri;
		}catch(Exception e){
			throw new ConsoleException(e);
		} 
	}

	@Override
	public abstract Dettaglio getDettaglio(long id, UriInfo uriInfo,BasicBD bd) throws WebApplicationException,ConsoleException;
	@Override
	public abstract void delete(List<Long> idsToDelete, UriInfo uriInfo, BasicBD bd) throws WebApplicationException,ConsoleException;
	@Override
	public abstract T creaEntry(InputStream is, UriInfo uriInfo, BasicBD bd) throws WebApplicationException,ConsoleException;
	@Override
	public abstract Dettaglio insert(InputStream is, UriInfo uriInfo, BasicBD bd) throws WebApplicationException,ConsoleException,ValidationException,DuplicatedEntryException;
	@Override
	public abstract void checkEntry(T entry, T oldEntry) throws ValidationException;
	@Override
	public abstract Dettaglio update(InputStream is, UriInfo uriInfo, BasicBD bd) throws WebApplicationException,ConsoleException,ValidationException;

	@Override
	public  abstract String getTitolo(T entry, BasicBD bd) throws ConsoleException;
	@Override
	public  abstract String getSottotitolo(T entry, BasicBD bd) throws ConsoleException;
	
	@Override
	public abstract String esporta(List<Long> idsToExport, UriInfo uriInfo, BasicBD bd, ZipOutputStream zout) throws WebApplicationException,ConsoleException;
	
	@Override
	public abstract String esporta(Long idToExport, UriInfo uriInfo, BasicBD bd, ZipOutputStream zout)throws WebApplicationException, ConsoleException;

	public Elemento getElemento(T entry, Long id, UriBuilder uriDettaglioBuilder, BasicBD bd) throws ConsoleException{
		String titolo = this.getTitolo(entry,bd);
		String sottotitolo = this.getSottotitolo(entry,bd);
		URI urlDettaglio = (id != null && uriDettaglioBuilder != null) ?  uriDettaglioBuilder.build(id) : null;
		Elemento elemento = new Elemento(id, titolo, sottotitolo, urlDettaglio);
		elemento.setValori(this.getValori(entry, bd)); 
		return elemento;
	}
	
	public abstract List<String> getValori(T entry, BasicBD bd) throws ConsoleException;

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
		if(versioneId == null) versioneId = Utils.getInstance().getMessageFromResourceBundle(this.nomeServizio + ".versione.id");
		
		String firmaRichiestaLabel = Utils.getInstance().getMessageFromResourceBundle(this.nomeServizio + ".versione.label");
		List<Voce<String>> valoriVersione = new ArrayList<Voce<String>>(); 
		valoriVersione.add(new Voce<String>(Versione.GP_02_02_00.getLabel(), Versione.GP_02_02_00.getLabel()));
		valoriVersione.add(new Voce<String>(Versione.GP_02_01_00.getLabel(), Versione.GP_02_01_00.getLabel()));
		SelectList<String> versione = new SelectList<String>(versioneId, firmaRichiestaLabel, null, true, false, true, valoriVersione);
		versione.setAvanzata(true);
		versione.setDefaultValue(Versione.GP_02_02_00.getLabel()); 
		
		return versione;
	}
	
	public Versione getVersioneSelezionata(JSONObject jsonObject, String versioneId, boolean remove) throws ServiceException{
		if(versioneId == null) versioneId = Utils.getInstance().getMessageFromResourceBundle(this.nomeServizio + ".versione.id");
		
		String versioneJson = jsonObject.getString(versioneId);
		
		if(remove)
			jsonObject.remove(versioneId);
		
		return Versione.toEnum(versioneJson);
	}
}
