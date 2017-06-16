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
package it.govpay.web.rs.dars.handler;

import java.net.URI;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.zip.ZipOutputStream;

import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.UriInfo;

import org.openspcoop2.utils.csv.Format;

import it.govpay.bd.BasicBD;
import it.govpay.web.rs.dars.exception.ConsoleException;
import it.govpay.web.rs.dars.exception.ExportException;
import it.govpay.web.rs.dars.model.InfoForm;
import it.govpay.web.rs.dars.model.RawParamValue;
import it.govpay.web.rs.dars.model.Voce;

public interface IBaseDarsHandler<T> {
	
	public InfoForm getInfoRicerca(UriInfo uriInfo, BasicBD bd) throws ConsoleException;
	public InfoForm getInfoRicerca(UriInfo uriInfo, BasicBD bd, boolean visualizzaRicerca) throws ConsoleException;
	public InfoForm getInfoRicerca(UriInfo uriInfo, BasicBD bd, Map<String, String> parameters) throws ConsoleException;
	public InfoForm getInfoRicerca(UriInfo uriInfo, BasicBD bd, boolean visualizzaRicerca, Map<String, String> parameters) throws ConsoleException;
	public URI getUriRicerca(UriInfo uriInfo, BasicBD bd) throws ConsoleException;
	public URI getUriRicerca(UriInfo uriInfo, BasicBD bd, Map<String, String> parameters) throws ConsoleException;
	
	public URI getUriEsportazione(UriInfo uriInfo, BasicBD bd) throws ConsoleException;
	public URI getUriEsportazione(UriInfo uriInfo, BasicBD bd, Map<String, String> parameters) throws ConsoleException;
	public URI getUriEsportazioneDettaglio(UriInfo uriInfo, BasicBD bd,long id) throws ConsoleException;
	
	public String esporta(List<Long> idsToExport, List<RawParamValue> rawValues, UriInfo uriInfo, BasicBD bd, ZipOutputStream zout) throws WebApplicationException,ConsoleException,ExportException;
	public String esporta(Long idToExport, UriInfo uriInfo, BasicBD bd, ZipOutputStream zout) throws WebApplicationException,ConsoleException,ExportException;
	
	public Object getSearchField(UriInfo uriInfo,List<RawParamValue>values, String fieldId, BasicBD bd) throws WebApplicationException,ConsoleException;
	public URI getUriSearchField(UriInfo uriInfo, BasicBD bd, String fieldName) throws ConsoleException;
	
	public String getTitolo(T entry, BasicBD bd) throws ConsoleException;
	public String getSottotitolo(T entry, BasicBD bd) throws ConsoleException;
	public Map<String, Voce<String>> getVoci(T entry, BasicBD bd) throws ConsoleException;

	public Format getFormat();
	public Locale getLanguage();
}
