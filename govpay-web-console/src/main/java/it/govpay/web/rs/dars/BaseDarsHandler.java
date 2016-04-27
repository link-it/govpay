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
import java.net.URI;
import java.util.List;

import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.MultivaluedMap;
import javax.ws.rs.core.UriBuilder;
import javax.ws.rs.core.UriInfo;

import org.apache.logging.log4j.Logger;

import it.govpay.bd.BasicBD;
import it.govpay.web.rs.dars.exception.ConsoleException;
import it.govpay.web.rs.dars.exception.DuplicatedEntryException;
import it.govpay.web.rs.dars.exception.ValidationException;
import it.govpay.web.rs.dars.model.Dettaglio;
import it.govpay.web.rs.dars.model.Elemento;
import it.govpay.web.rs.dars.model.Elenco;
import it.govpay.web.rs.dars.model.InfoForm;
import it.govpay.web.rs.dars.model.RawParamValue;
import it.govpay.web.utils.ConsoleProperties;
import it.govpay.web.utils.Utils;

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

	public abstract Elenco getElenco(UriInfo uriInfo, BasicBD bd) throws WebApplicationException,ConsoleException;
	public abstract InfoForm getInfoRicerca(UriInfo uriInfo, BasicBD bd) throws ConsoleException;
	public URI getUriRicerca(UriInfo uriInfo, BasicBD bd) throws ConsoleException{
		try{
			URI uri = uriInfo.getBaseUriBuilder().path(this.pathServizio).build();
			return uri;
		}catch(Exception e){
			throw new ConsoleException(e);
		}
	}

	public abstract InfoForm getInfoCreazione(UriInfo uriInfo,BasicBD bd) throws ConsoleException;
	public URI getUriCreazione(UriInfo uriInfo, BasicBD bd) throws ConsoleException{
		try{
			URI uri = uriInfo.getBaseUriBuilder().path(this.pathServizio).build();
			return uri;
		}catch(Exception e){
			throw new ConsoleException(e);
		}
	}
	public abstract InfoForm getInfoModifica(UriInfo uriInfo,BasicBD bd, T entry) throws ConsoleException;
	public URI getUriModifica(UriInfo uriInfo, BasicBD bd) throws ConsoleException{
		try{
			URI uri = uriInfo.getBaseUriBuilder().path(this.pathServizio).build();
			return uri;
		}catch(Exception e){
			throw new ConsoleException(e);
		}
	}

	public URI getUriCancellazione(UriInfo uriInfo, BasicBD bd)throws ConsoleException{
		try{
			URI uri = uriInfo.getBaseUriBuilder().path(this.pathServizio).path(BaseDarsService.PATH_CANCELLA).build();
			return uri;
		}catch(Exception e){
			throw new ConsoleException(e);
		}
	}

	public URI getUriEsportazione(UriInfo uriInfo, BasicBD bd)throws ConsoleException{
		try{
			URI uri = uriInfo.getBaseUriBuilder().path(this.pathServizio).path(BaseDarsService.PATH_ESPORTA).build(); 
			return uri;
		}catch(Exception e){
			throw new ConsoleException(e);
		}
	}

	public abstract Object getField(UriInfo uriInfo,List<RawParamValue>values, String fieldId,BasicBD bd) throws WebApplicationException,ConsoleException ;
	@Override
	public URI getUriField(UriInfo uriInfo, BasicBD bd, String fieldName) throws ConsoleException {
		try{
			URI uri = uriInfo.getBaseUriBuilder().path(this.pathServizio).path(BaseDarsService.PATH_FIELD).path(fieldName).build(); 
			return uri;
		}catch(Exception e){
			throw new ConsoleException(e);
		} 
	}

	public abstract Dettaglio getDettaglio(long id, UriInfo uriInfo,BasicBD bd) throws WebApplicationException,ConsoleException;
	public abstract void delete(List<Long> idsToDelete, UriInfo uriInfo, BasicBD bd) throws WebApplicationException,ConsoleException;
	public abstract T creaEntry(InputStream is, UriInfo uriInfo, BasicBD bd) throws WebApplicationException,ConsoleException;
	public abstract Dettaglio insert(InputStream is, UriInfo uriInfo, BasicBD bd) throws WebApplicationException,ConsoleException,ValidationException,DuplicatedEntryException;
	public abstract void checkEntry(T entry, T oldEntry) throws ValidationException;
	public abstract Dettaglio update(InputStream is, UriInfo uriInfo, BasicBD bd) throws WebApplicationException,ConsoleException,ValidationException;

	public  abstract String getTitolo(T entry) ;
	public  abstract String getSottotitolo(T entry) ;

	public Elemento getElemento(T entry, Long id, UriBuilder uriDettaglioBuilder){
		String titolo = this.getTitolo(entry);
		String sottotitolo = this.getSottotitolo(entry);
		URI urlDettaglio = id != null ?  uriDettaglioBuilder.build(id) : null;
		Elemento elemento = new Elemento(id, titolo, sottotitolo, urlDettaglio);
		return elemento;
	}

	public <P> P getParameter(UriInfo uriInfo, String parameterName, Class<P> type) throws ConsoleException{
		P toReturn = null;
		try{
			MultivaluedMap<String, String> queryParams = uriInfo.getQueryParameters(); 

			String paramAsString = queryParams.getFirst(parameterName);
			if(paramAsString != null)
				toReturn = type.cast(paramAsString);
		}catch(Exception e){
			throw new ConsoleException(e);
		}

		return toReturn;
	}

	public Integer getOffset(UriInfo uriInfo) throws ConsoleException{
		return getParameter(uriInfo, "offset", Integer.class);
	}

	public Integer getLimit(UriInfo uriInfo) throws ConsoleException{
		Integer parameter = getParameter(uriInfo, "limit", Integer.class);
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
}
