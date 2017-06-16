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
package it.govpay.web.rs.dars.monitoraggio.eventi;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.net.URI;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
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
import org.openspcoop2.utils.UtilsException;
import org.openspcoop2.utils.csv.Printer;

import it.govpay.bd.BasicBD;
import it.govpay.bd.FilterSortWrapper;
import it.govpay.bd.anagrafica.DominiBD;
import it.govpay.bd.anagrafica.filters.DominioFilter;
import it.govpay.bd.model.Dominio;
import it.govpay.bd.pagamento.EventiBD;
import it.govpay.bd.pagamento.filters.EventiFilter;
import it.govpay.model.Evento;
import it.govpay.model.Evento.TipoEvento;
import it.govpay.web.rs.dars.anagrafica.domini.Domini;
import it.govpay.web.rs.dars.anagrafica.domini.DominiHandler;
import it.govpay.web.rs.dars.base.DarsHandler;
import it.govpay.web.rs.dars.base.DarsService;
import it.govpay.web.rs.dars.exception.ConsoleException;
import it.govpay.web.rs.dars.exception.DeleteException;
import it.govpay.web.rs.dars.exception.DuplicatedEntryException;
import it.govpay.web.rs.dars.exception.ExportException;
import it.govpay.web.rs.dars.exception.ValidationException;
import it.govpay.web.rs.dars.handler.IDarsHandler;
import it.govpay.web.rs.dars.model.DarsResponse.EsitoOperazione;
import it.govpay.web.rs.dars.model.Dettaglio;
import it.govpay.web.rs.dars.model.Elenco;
import it.govpay.web.rs.dars.model.InfoForm;
import it.govpay.web.rs.dars.model.InfoForm.Sezione;
import it.govpay.web.rs.dars.model.RawParamValue;
import it.govpay.web.rs.dars.model.Voce;
import it.govpay.web.rs.dars.model.input.ParamField;
import it.govpay.web.rs.dars.model.input.base.InputText;
import it.govpay.web.rs.dars.model.input.base.SelectList;
import it.govpay.web.utils.ConsoleProperties;
import it.govpay.web.utils.Utils;

public class EventiHandler extends DarsHandler<Evento> implements IDarsHandler<Evento>{

	public static final String SEPARATORE_CSV = "|";

	private SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");

	public EventiHandler(Logger log, DarsService darsService) { 
		super(log, darsService);
	}

	@Override
	public Elenco getElenco(UriInfo uriInfo, BasicBD bd) throws WebApplicationException, ConsoleException {
		String methodName = "getElenco " + this.titoloServizio;
		try{	
			// Operazione consentita agli utenti registrati
			this.darsService.checkOperatoreAdmin(bd); 

			Integer offset = this.getOffset(uriInfo);
			Integer limit = this.getLimit(uriInfo);

			Map<String, String> params = new HashMap<String, String>();
			this.log.info("Esecuzione " + methodName + " in corso...");
			EventiBD eventiBD = new EventiBD(bd);
			boolean simpleSearch = this.containsParameter(uriInfo, DarsService.SIMPLE_SEARCH_PARAMETER_ID);
			EventiFilter filter = eventiBD.newFilter(simpleSearch);
			filter.setOffset(offset);
			filter.setLimit(limit);

			boolean visualizzaRicerca = this.popolaFiltroRicerca(uriInfo, params, simpleSearch, filter);

			long count = eventiBD.count(filter);

			// visualizza la ricerca solo se i risultati sono > del limit e se non sono nella schermata degli eventi di una transazione.
			visualizzaRicerca = visualizzaRicerca && this.visualizzaRicerca(count, limit);
			InfoForm infoRicerca = this.getInfoRicerca(uriInfo, bd, visualizzaRicerca,params);

			String simpleSearchPlaceholder = Utils.getInstance(this.getLanguage()).getMessageFromResourceBundle(this.nomeServizio+".simpleSearch.placeholder");
			Elenco elenco = new Elenco(this.titoloServizio, infoRicerca,
					this.getInfoCreazione(uriInfo, bd),
					count, this.getUriEsportazione(uriInfo, bd, params) , this.getInfoCancellazione(uriInfo, bd),simpleSearchPlaceholder);  

			List<Evento> findAll = eventiBD.findAll(filter); 

			if(findAll != null && findAll.size() > 0){
				for (Evento entry : findAll) {
					elenco.getElenco().add(this.getElemento(entry, entry.getId(), null,bd));
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

	private boolean popolaFiltroRicerca(UriInfo uriInfo, Map<String, String> params,
			boolean simpleSearch, EventiFilter filter) throws ConsoleException {
		String idTransazioneId = Utils.getInstance(this.getLanguage()).getMessageFromResourceBundle(this.nomeServizio + ".idTransazione.id");
		String idTransazione = this.getParameter(uriInfo, idTransazioneId, String.class);
		SortOrder sortOrder = SortOrder.DESC;
		boolean visualizzaRicerca = true; 
		// se visualizzo gli eventi nella pagina delle transazioni li ordino in ordine crescente
		if(StringUtils.isNotEmpty(idTransazione)){
			visualizzaRicerca = false;
			sortOrder = SortOrder.ASC;
			params.put(idTransazioneId, idTransazione);
			simpleSearch = false; 
		}


		FilterSortWrapper fsw = new FilterSortWrapper();
		fsw.setField(it.govpay.orm.Evento.model().DATA_1);
		fsw.setSortOrder(sortOrder);
		filter.getFilterSortList().add(fsw);
		filter.setSimpleSearch(simpleSearch);

		if(simpleSearch) {
			// simplesearch
			String simpleSearchString = this.getParameter(uriInfo, DarsService.SIMPLE_SEARCH_PARAMETER_ID, String.class);
			params.put(DarsService.SIMPLE_SEARCH_PARAMETER_ID, simpleSearchString);

			if(StringUtils.isNotEmpty(simpleSearchString)) {
				filter.setSimpleSearchString(simpleSearchString);
			}
		} else {
			String codDominioId = Utils.getInstance(this.getLanguage()).getMessageFromResourceBundle(this.nomeServizio + ".codDominio.id");
			String iuvId = Utils.getInstance(this.getLanguage()).getMessageFromResourceBundle(this.nomeServizio + ".iuv.id");
			String ccpId = Utils.getInstance(this.getLanguage()).getMessageFromResourceBundle(this.nomeServizio + ".ccp.id");

			String codDominio = this.getParameter(uriInfo, codDominioId, String.class);
			if(StringUtils.isNotEmpty(codDominio)){
				filter.setCodDominio(codDominio);
				params.put(codDominioId, codDominio);
			}

			String iuv = this.getParameter(uriInfo, iuvId, String.class);
			if(StringUtils.isNotEmpty(iuv)){
				filter.setIuv(iuv); 
				params.put(iuvId, iuv);
			}


			String ccp = this.getParameter(uriInfo, ccpId, String.class);
			if(StringUtils.isNotEmpty(ccp)){
				filter.setCcp(ccp); 
				params.put(ccpId, ccp);
			}
		}
		return visualizzaRicerca;
	}

	private boolean popolaFiltroRicerca(List<RawParamValue> rawValues, UriInfo uriInfo,  Map<String, String> params,
			boolean simpleSearch, EventiFilter filter) throws ConsoleException {
		String idTransazioneId = Utils.getInstance(this.getLanguage()).getMessageFromResourceBundle(this.nomeServizio + ".idTransazione.id");
		String idTransazione = this.getParameter(uriInfo, idTransazioneId, String.class);
		SortOrder sortOrder = SortOrder.DESC;
		boolean limitRicerca = true;
		boolean elementoCorrelato = false;

		// se visualizzo gli eventi nella pagina delle transazioni li ordino in ordine crescente
		if(StringUtils.isNotEmpty(idTransazione)){
			sortOrder = SortOrder.ASC;
			params.put(idTransazioneId, idTransazione);
			simpleSearch = false;
			limitRicerca = false;
			elementoCorrelato = true;
		}


		FilterSortWrapper fsw = new FilterSortWrapper();
		fsw.setField(it.govpay.orm.Evento.model().DATA_1);
		fsw.setSortOrder(sortOrder);
		filter.getFilterSortList().add(fsw);
		filter.setSimpleSearch(simpleSearch);

		if(limitRicerca){
			int limit = ConsoleProperties.getInstance().getNumeroMassimoElementiExport();
			filter.setLimit(limit);
		}

		if(simpleSearch) {
			// simplesearch
			String simpleSearchString = Utils.getValue(rawValues, DarsService.SIMPLE_SEARCH_PARAMETER_ID);
			params.put(DarsService.SIMPLE_SEARCH_PARAMETER_ID, simpleSearchString);

			if(StringUtils.isNotEmpty(simpleSearchString)) {
				filter.setSimpleSearchString(simpleSearchString);
			}
		} else {
			String codDominioId = Utils.getInstance(this.getLanguage()).getMessageFromResourceBundle(this.nomeServizio + ".codDominio.id");
			String iuvId = Utils.getInstance(this.getLanguage()).getMessageFromResourceBundle(this.nomeServizio + ".iuv.id");
			String ccpId = Utils.getInstance(this.getLanguage()).getMessageFromResourceBundle(this.nomeServizio + ".ccp.id");

			String codDominio = elementoCorrelato ? this.getParameter(uriInfo, codDominioId, String.class) : Utils.getValue(rawValues, codDominioId);  
			if(StringUtils.isNotEmpty(codDominio)){
				filter.setCodDominio(codDominio);
				if(elementoCorrelato)
					params.put(codDominioId, codDominio);
			}

			String iuv = elementoCorrelato ?  this.getParameter(uriInfo, iuvId, String.class) : Utils.getValue(rawValues, iuvId);
			if(StringUtils.isNotEmpty(iuv)){
				filter.setIuv(iuv); 
				if(elementoCorrelato)
					params.put(iuvId, iuv);
			}


			String ccp = elementoCorrelato ?  this.getParameter(uriInfo, ccpId, String.class) : Utils.getValue(rawValues, ccpId);
			if(StringUtils.isNotEmpty(ccp)){
				filter.setCcp(ccp); 
				if(elementoCorrelato)
					params.put(ccpId, ccp);
			}
		}
		return limitRicerca;
	}

	@SuppressWarnings("unchecked")
	@Override
	public InfoForm getInfoRicerca(UriInfo uriInfo, BasicBD bd, boolean visualizzaRicerca, Map<String,String> parameters) throws ConsoleException {
		URI ricerca = this.getUriRicerca(uriInfo, bd,parameters);
		InfoForm infoRicerca = new InfoForm(ricerca);


		if(visualizzaRicerca) {
			String codDominioId = Utils.getInstance(this.getLanguage()).getMessageFromResourceBundle(this.nomeServizio + ".codDominio.id");
			String iuvId = Utils.getInstance(this.getLanguage()).getMessageFromResourceBundle(this.nomeServizio + ".iuv.id");
			String ccpId = Utils.getInstance(this.getLanguage()).getMessageFromResourceBundle(this.nomeServizio + ".ccp.id");

			if(this.infoRicercaMap == null){
				this.initInfoRicerca(uriInfo, bd);
			}

			Sezione sezioneRoot = infoRicerca.getSezioneRoot();

			// codDominio
			List<Voce<String>> domini = new ArrayList<Voce<String>>();

			DominiBD dominiBD = new DominiBD(bd);
			DominioFilter filter;
			try {
				filter = dominiBD.newFilter();
				FilterSortWrapper fsw = new FilterSortWrapper();
				fsw.setField(it.govpay.orm.Dominio.model().COD_DOMINIO);
				fsw.setSortOrder(SortOrder.ASC);
				filter.getFilterSortList().add(fsw);
				List<Dominio> findAll = dominiBD.findAll(filter );

				Domini dominiDars = new Domini();
				DominiHandler dominiHandler = (DominiHandler) dominiDars.getDarsHandler();

				domini.add(new Voce<String>(Utils.getInstance(this.getLanguage()).getMessageFromResourceBundle("commons.label.qualsiasi"), ""));
				if(findAll != null && findAll.size() > 0){
					for (Dominio dominio : findAll) {
						domini.add(new Voce<String>(dominiHandler.getTitolo(dominio,bd), dominio.getCodDominio()));  
					}
				}
			} catch (ServiceException e) {
				throw new ConsoleException(e);
			}
			SelectList<String> codDominio = (SelectList<String>) this.infoRicercaMap.get(codDominioId);
			codDominio.setDefaultValue("");
			codDominio.setValues(domini); 
			sezioneRoot.addField(codDominio);

			InputText iuv = (InputText) this.infoRicercaMap.get(iuvId);
			iuv.setDefaultValue(null);
			sezioneRoot.addField(iuv);

			InputText ccp = (InputText) this.infoRicercaMap.get(ccpId);
			ccp.setDefaultValue(null);
			sezioneRoot.addField(ccp);

		}
		return infoRicerca;
	}

	private void initInfoRicerca(UriInfo uriInfo, BasicBD bd) throws ConsoleException{
		if(this.infoRicercaMap == null){
			this.infoRicercaMap = new HashMap<String, ParamField<?>>();

			String codDominioId = Utils.getInstance(this.getLanguage()).getMessageFromResourceBundle(this.nomeServizio + ".codDominio.id");
			String iuvId = Utils.getInstance(this.getLanguage()).getMessageFromResourceBundle(this.nomeServizio + ".iuv.id");
			String ccpId = Utils.getInstance(this.getLanguage()).getMessageFromResourceBundle(this.nomeServizio + ".ccp.id");

			// iuv
			String iuvLabel = Utils.getInstance(this.getLanguage()).getMessageFromResourceBundle(this.nomeServizio + ".iuv.label");
			InputText iuv = new InputText(iuvId, iuvLabel, null, false, false, true, 0, 35);
			this.infoRicercaMap.put(iuvId, iuv);

			// ccp
			String ccpLabel = Utils.getInstance(this.getLanguage()).getMessageFromResourceBundle(this.nomeServizio + ".ccp.label");
			InputText ccp = new InputText(ccpId, ccpLabel, null, false, false, true, 0, 35);
			this.infoRicercaMap.put(ccpId, ccp);

			List<Voce<String>> domini = new ArrayList<Voce<String>>();
			// codDominio
			String codDominioLabel = Utils.getInstance(this.getLanguage()).getMessageFromResourceBundle(this.nomeServizio + ".codDominio.label");
			SelectList<String> codDominio = new SelectList<String>(codDominioId, codDominioLabel, null, false, false, true, domini);
			this.infoRicercaMap.put(codDominioId, codDominio);

		}
	}

	@Override
	public Object getField(UriInfo uriInfo, List<RawParamValue> values, String fieldId, BasicBD bd) throws WebApplicationException, ConsoleException {
		return null;
	}
	
	@Override
	public Object getSearchField(UriInfo uriInfo, List<RawParamValue> values, String fieldId, BasicBD bd)	throws WebApplicationException, ConsoleException { 	return null; }

	@Override
	public Dettaglio getDettaglio(long id, UriInfo uriInfo, BasicBD bd)
			throws WebApplicationException, ConsoleException {
		return null;
	}

	@Override
	public String getTitolo(Evento entry,BasicBD bd) {
		StringBuilder sb = new StringBuilder();

		//Nel titolo indicare data tipo evento ed esito 

		TipoEvento tipoEvento = entry.getTipoEvento();
		Date dataRichiesta = entry.getDataRichiesta();
		String esito = entry.getEsito();

		sb.append(
				Utils.getInstance(this.getLanguage()).getMessageWithParamsFromResourceBundle(this.nomeServizio + ".label.titolo",
						this.sdf.format(dataRichiesta),						
						Utils.getInstance(this.getLanguage()).getMessageFromResourceBundle(this.nomeServizio + ".tipoEvento."+ tipoEvento.name()),
						esito						
						));

		return sb.toString();
	}

	@Override
	public String getSottotitolo(Evento entry,BasicBD bd) {
		StringBuilder sb = new StringBuilder();
		String codDominio = entry.getCodDominio();
		String iuv = entry.getIuv();
		String ccp = entry.getCcp();

		//Nel sottotitolo indicare dominio, iuv, ccp 
		sb.append(Utils.getInstance(this.getLanguage()).getMessageWithParamsFromResourceBundle(this.nomeServizio + ".label.sottotitolo", codDominio,iuv,ccp));

		return sb.toString();
	} 

	@Override
	public Map<String, Voce<String>> getVoci(Evento entry, BasicBD bd) throws ConsoleException { return null; }

	@Override
	public String esporta(List<Long> idsToExport, List<RawParamValue> rawValues, UriInfo uriInfo, BasicBD bd, ZipOutputStream zout)
			throws WebApplicationException, ConsoleException ,ExportException {
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
		String fileName = "Eventi.zip";
		try{
			ByteArrayOutputStream baos  = new ByteArrayOutputStream();
			Map<String, String> params = new HashMap<String, String>();
			this.log.info("Esecuzione " + methodName + " in corso...");
			this.darsService.checkOperatoreAdmin(bd); 
			boolean simpleSearch = Utils.containsParameter(rawValues, DarsService.SIMPLE_SEARCH_PARAMETER_ID);
			EventiBD eventiBD = new EventiBD(bd);

			EventiFilter filter = eventiBD.newFilter(simpleSearch);

			// se ho ricevuto anche gli id li utilizzo per fare il check della count
			if(idsToExport != null && idsToExport.size() > 0)
				filter.setIdEventi(idsToExport); 

			boolean checkCount = this.popolaFiltroRicerca(rawValues, uriInfo, params, simpleSearch, filter);

			long count = eventiBD.count(filter);

			if(count < 1){
				List<String> msg = new ArrayList<String>();
				msg.add(Utils.getInstance(this.getLanguage()).getMessageFromResourceBundle(this.nomeServizio+".esporta.nessunElementoDaEsportare"));
				throw new ExportException(msg, EsitoOperazione.ERRORE);
			} 

			if(checkCount && count > ConsoleProperties.getInstance().getNumeroMassimoElementiExport()){
				List<String> msg = new ArrayList<String>();
				msg.add(Utils.getInstance(this.getLanguage()).getMessageFromResourceBundle(this.nomeServizio+".esporta.numeroElementiDaEsportareSopraSogliaMassima"));
				throw new ExportException(msg, EsitoOperazione.ERRORE);
			}

			filter.setOffset(0);
			if(checkCount)
				filter.setLimit(ConsoleProperties.getInstance().getNumeroMassimoElementiExport());

			List<Evento> list = eventiBD.findAll(filter);

			if(list != null && list.size() > 0) {
				this.scriviCSVEventi(baos, list );

				ZipEntry datiEvento = new ZipEntry("eventi.csv");
				zout.putNextEntry(datiEvento);
				zout.write(baos.toByteArray());
				zout.closeEntry();
			} else {
				String noEntriesTxt = "/README";
				ZipEntry entryTxt = new ZipEntry(noEntriesTxt);
				zout.putNextEntry(entryTxt);
				zout.write("Non sono state trovate informazioni sugli eventi selezionati.".getBytes());
				zout.closeEntry();
			}

			zout.flush();
			zout.close();

			this.log.info("Esecuzione " + methodName + " completata.");

			return fileName;
		}catch(WebApplicationException e){
			throw e;
		}catch(Exception e){
			throw new ConsoleException(e);
		}
	}

	public void scriviCSVEventi(ByteArrayOutputStream baos, List<Evento> list) throws UtilsException, Exception {
		Printer printer = null;
		try{
			printer = new Printer(this.getFormat() , baos);
			printer.printRecord(this.getCsvHeader());
			for (Evento evento: list) {
				printer.printRecord(this.getEventoCsv(evento, true));
				if("OK".equals(evento.getEsito()) || "KO".equals(evento.getEsito()))
					printer.printRecord(this.getEventoCsv(evento, false));
			}
		}finally {
			try{
				if(printer!=null){
					printer.close();
				}
			}catch (Exception e) {
				throw new Exception("Errore durante la chiusura dello stream ",e);
			}
		}
	}

	private List<String> getCsvHeader(){
		List<String> header = new ArrayList<String>();

		header.add(Utils.getInstance(this.getLanguage()).getMessageFromResourceBundle(this.nomeServizio + ".id.label"));
		header.add(Utils.getInstance(this.getLanguage()).getMessageFromResourceBundle(this.nomeServizio + ".data.label"));
		header.add(Utils.getInstance(this.getLanguage()).getMessageFromResourceBundle(this.nomeServizio + ".codDominio.label"));
		header.add(Utils.getInstance(this.getLanguage()).getMessageFromResourceBundle(this.nomeServizio + ".iuv.label"));
		header.add(Utils.getInstance(this.getLanguage()).getMessageFromResourceBundle(this.nomeServizio + ".ccp.label"));
		header.add(Utils.getInstance(this.getLanguage()).getMessageFromResourceBundle(this.nomeServizio + ".codPsp.label"));
		header.add(Utils.getInstance(this.getLanguage()).getMessageFromResourceBundle(this.nomeServizio + ".tipoEvento.label"));
		header.add(Utils.getInstance(this.getLanguage()).getMessageFromResourceBundle(this.nomeServizio + ".sottotipoEvento.label"));
		header.add(Utils.getInstance(this.getLanguage()).getMessageFromResourceBundle(this.nomeServizio + ".categoriaEvento.label"));
		header.add(Utils.getInstance(this.getLanguage()).getMessageFromResourceBundle(this.nomeServizio + ".componente.label"));
		header.add(Utils.getInstance(this.getLanguage()).getMessageFromResourceBundle(this.nomeServizio + ".fruitore.label"));
		header.add(Utils.getInstance(this.getLanguage()).getMessageFromResourceBundle(this.nomeServizio + ".erogatore.label"));
		header.add(Utils.getInstance(this.getLanguage()).getMessageFromResourceBundle(this.nomeServizio + ".codStazione.label"));
		header.add(Utils.getInstance(this.getLanguage()).getMessageFromResourceBundle(this.nomeServizio + ".codCanale.label"));
		header.add(Utils.getInstance(this.getLanguage()).getMessageFromResourceBundle(this.nomeServizio + ".tipoVersamento.label"));
		header.add(Utils.getInstance(this.getLanguage()).getMessageFromResourceBundle(this.nomeServizio + ".altriParametri.label"));
		header.add(Utils.getInstance(this.getLanguage()).getMessageFromResourceBundle(this.nomeServizio + ".esito.label"));
		return header;
	}

	private List<String> getEventoCsv(Evento evento, boolean request){ 
		List<String> oneLine = new ArrayList<String>();

		oneLine.add(evento.getId() + (request ? "_REQ" : "_RSP"));

		if(evento.getDataRichiesta()!= null)
			oneLine.add( this.sdf.format(evento.getDataRichiesta()));
		else 
			oneLine.add("");

		if(StringUtils.isNotEmpty(evento.getCodDominio()))
			oneLine.add(evento.getCodDominio());
		else 
			oneLine.add("");

		if(StringUtils.isNotEmpty(evento.getIuv()))
			oneLine.add(evento.getIuv());
		else 
			oneLine.add("");

		if(StringUtils.isNotEmpty(evento.getCcp()))
			oneLine.add(evento.getCcp());
		else 
			oneLine.add("");

		if(StringUtils.isNotEmpty(evento.getCodPsp()))
			oneLine.add(evento.getCodPsp());
		else 
			oneLine.add("");

		if(evento.getTipoEvento() != null)
			oneLine.add(Utils.getInstance(this.getLanguage()).getMessageFromResourceBundle(this.nomeServizio + ".tipoEvento."+evento.getTipoEvento().name()));
		else 
			oneLine.add("");

		oneLine.add(request ? "req" : "rsp");

		if(evento.getCategoriaEvento() != null)
			oneLine.add(Utils.getInstance(this.getLanguage()).getMessageFromResourceBundle(this.nomeServizio + ".categoriaEvento." +evento.getCategoriaEvento().name()));
		else 
			oneLine.add("");

		if(StringUtils.isNotEmpty(evento.getComponente()))
			oneLine.add(evento.getComponente());
		else 
			oneLine.add("");

		if(StringUtils.isNotEmpty(evento.getFruitore()))
			oneLine.add(evento.getFruitore());
		else 
			oneLine.add("");

		if(StringUtils.isNotEmpty(evento.getErogatore()))
			oneLine.add(evento.getErogatore());
		else 
			oneLine.add("");

		if(StringUtils.isNotEmpty(evento.getCodStazione()))
			oneLine.add(evento.getCodStazione());
		else 
			oneLine.add("");

		if(StringUtils.isNotEmpty(evento.getCodCanale()))
			oneLine.add(evento.getCodCanale());
		else 
			oneLine.add("");

		if(evento.getTipoVersamento()!= null)
			oneLine.add(Utils.getInstance(this.getLanguage()).getMessageFromResourceBundle(this.nomeServizio + ".tipoVersamento." +evento.getTipoVersamento().name()));
		else 
			oneLine.add("");

		if(request) {
			if(StringUtils.isNotEmpty(evento.getAltriParametriRichiesta()))
				oneLine.add(evento.getAltriParametriRichiesta());
			else 
				oneLine.add("");
		} else {
			if(StringUtils.isNotEmpty(evento.getAltriParametriRisposta()))
				oneLine.add(evento.getAltriParametriRisposta());
			else 
				oneLine.add("");
		}

		if(StringUtils.isNotEmpty(evento.getEsito()))
			oneLine.add(evento.getEsito());
		else 
			oneLine.add("");

		return oneLine;
	}

	@Override
	public String esporta(Long idToExport, UriInfo uriInfo, BasicBD bd, ZipOutputStream zout)
			throws WebApplicationException, ConsoleException ,ExportException{
		return null;
	}

	/* Creazione/Update non consentiti**/

	@Override
	public InfoForm getInfoCancellazione(UriInfo uriInfo, BasicBD bd) throws ConsoleException { return null;}

	@Override
	public InfoForm getInfoCancellazioneDettaglio(UriInfo uriInfo, BasicBD bd, Evento entry) throws ConsoleException {
		return null;
	}

	@Override
	public InfoForm getInfoCreazione(UriInfo uriInfo, BasicBD bd) throws ConsoleException { return null; }

	@Override
	public InfoForm getInfoModifica(UriInfo uriInfo, BasicBD bd, Evento entry) throws ConsoleException { return null; }

	@Override
	public Elenco delete(List<Long> idsToDelete, List<RawParamValue> rawValues, UriInfo uriInfo, BasicBD bd) throws WebApplicationException, ConsoleException, DeleteException {	return null; 	}

	@Override
	public Evento creaEntry(InputStream is, UriInfo uriInfo, BasicBD bd) throws WebApplicationException, ConsoleException { return null; }

	@Override
	public Dettaglio insert(InputStream is, UriInfo uriInfo, BasicBD bd) throws WebApplicationException, ConsoleException, ValidationException, DuplicatedEntryException { return null; }

	@Override
	public void checkEntry(Evento entry, Evento oldEntry) throws ValidationException { }

	@Override
	public Dettaglio update(InputStream is, UriInfo uriInfo, BasicBD bd) throws WebApplicationException, ConsoleException, ValidationException { return null; }

	@Override
	public Object uplaod(MultipartFormDataInput input, UriInfo uriInfo, BasicBD bd)	throws WebApplicationException, ConsoleException, ValidationException { return null;}
}
