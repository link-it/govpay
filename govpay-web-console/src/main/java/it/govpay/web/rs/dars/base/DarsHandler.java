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
package it.govpay.web.rs.dars.base;

import java.io.InputStream;
import java.net.URI;
import java.util.List;
import java.util.Map;

import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.UriInfo;

import org.apache.logging.log4j.Logger;
import org.jboss.resteasy.plugins.providers.multipart.MultipartFormDataInput;

import it.govpay.bd.BasicBD;
import it.govpay.web.rs.dars.exception.ConsoleException;
import it.govpay.web.rs.dars.exception.DeleteException;
import it.govpay.web.rs.dars.exception.DuplicatedEntryException;
import it.govpay.web.rs.dars.exception.ValidationException;
import it.govpay.web.rs.dars.handler.IDarsHandler;
import it.govpay.web.rs.dars.model.Dettaglio;
import it.govpay.web.rs.dars.model.Elenco;
import it.govpay.web.rs.dars.model.InfoForm;
import it.govpay.web.rs.dars.model.RawParamValue;
import it.govpay.web.utils.Utils;

public abstract class DarsHandler<T> extends BaseDarsHandler<T> implements IDarsHandler<T>{

	public DarsHandler(Logger log, DarsService darsService){
		super(log, darsService);
		this.log = log;
	}

	@Override
	public abstract Elenco getElenco(UriInfo uriInfo, BasicBD bd) throws WebApplicationException,ConsoleException;

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
	public abstract Object getField(UriInfo uriInfo,List<RawParamValue>values, String fieldId,BasicBD bd) throws WebApplicationException,ConsoleException ;
	@Override
	public URI getUriField(UriInfo uriInfo, BasicBD bd, String fieldName) throws ConsoleException {
		try{
			URI uri =  Utils.creaUriConPath(this.pathServizio, DarsService.PATH_FIELD , fieldName);
			return uri;
		}catch(Exception e){
			throw new ConsoleException(e);
		} 
	}
	
	@Override
	public InfoForm getInfoCancellazione(UriInfo uriInfo,BasicBD bd) throws ConsoleException {
		return this.getInfoCancellazione(uriInfo, bd, null);
	}

	@Override
	public abstract InfoForm getInfoCancellazione(UriInfo uriInfo, BasicBD bd, Map<String, String> parameters)	throws ConsoleException ;
	
	@Override
	public URI getUriCancellazione(UriInfo uriInfo, BasicBD bd)throws ConsoleException{
		return this.getUriCancellazione(uriInfo, bd, null);
	}
	
	@Override
	public URI getUriCancellazione(UriInfo uriInfo, BasicBD bd, Map<String, String> parameters)
			throws ConsoleException {
		try{
			URI uri =Utils.creaUriConPathEParametri(this.pathServizio, parameters, DarsService.PATH_CANCELLA);
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
			URI uri = Utils.creaUriConPath(this.pathServizio, id+"" , DarsService.PATH_CANCELLA); 
			return uri;
		}catch(Exception e){
			throw new ConsoleException(e);
		}
	}

	@Override
	public URI getUriUpload(UriInfo uriInfo, BasicBD bd) throws ConsoleException {
		try{
			URI uri = Utils.creaUriConPath(this.pathServizio, DarsService.PATH_UPLOAD);
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
 
}
