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
package it.govpay.web.rs.dars.monitoraggio.eventi;

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
import org.openspcoop2.generic_project.exception.ServiceException;
import org.openspcoop2.generic_project.expression.SortOrder;

import it.govpay.bd.BasicBD;
import it.govpay.bd.FilterSortWrapper;
import it.govpay.bd.anagrafica.DominiBD;
import it.govpay.bd.anagrafica.filters.DominioFilter;
import it.govpay.bd.model.Dominio;
import it.govpay.bd.model.Evento;
import it.govpay.bd.model.Evento.TipoEvento;
import it.govpay.bd.model.Operatore;
import it.govpay.bd.model.Operatore.ProfiloOperatore;
import it.govpay.bd.pagamento.EventiBD;
import it.govpay.bd.pagamento.filters.EventiFilter;
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
import it.govpay.web.rs.dars.model.input.base.InputText;
import it.govpay.web.rs.dars.model.input.base.SelectList;
import it.govpay.web.utils.Utils;

public class EventiHandler extends BaseDarsHandler<Evento> implements IDarsHandler<Evento>{

	public static final String SEPARATORE_CSV = "|";

	private static Map<String, ParamField<?>> infoRicercaMap = null;
	private SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");

	public EventiHandler(Logger log, BaseDarsService darsService) { 
		super(log, darsService);
	}

	@SuppressWarnings("unused")
	@Override
	public Elenco getElenco(UriInfo uriInfo, BasicBD bd) throws WebApplicationException, ConsoleException {
		String methodName = "getElenco " + this.titoloServizio;
		try{	
			// Operazione consentita agli utenti registrati
			Operatore operatore = this.darsService.getOperatoreByPrincipal(bd); 
			ProfiloOperatore profilo = operatore.getProfilo();
			boolean isAdmin = profilo.equals(ProfiloOperatore.ADMIN);

			Integer offset = this.getOffset(uriInfo);
			Integer limit = this.getLimit(uriInfo);
			URI esportazione = this.getUriEsportazione(uriInfo, bd); 
			URI cancellazione = null;

			boolean visualizzaRicerca = true;
			this.log.info("Esecuzione " + methodName + " in corso...");

			String idTransazioneId = Utils.getInstance().getMessageFromResourceBundle(this.nomeServizio + ".idTransazione.id");
			String idTransazione = this.getParameter(uriInfo, idTransazioneId, String.class);
			SortOrder sortOrder = SortOrder.DESC;
			// se visualizzo gli eventi nella pagina delle transazioni li ordino in ordine crescente
			if(StringUtils.isNotEmpty(idTransazione)){
				visualizzaRicerca = false;
				sortOrder = SortOrder.ASC;
			}

			EventiBD eventiBD = new EventiBD(bd);
			EventiFilter filter = eventiBD.newFilter();
			filter.setOffset(offset);
			filter.setLimit(limit);
			FilterSortWrapper fsw = new FilterSortWrapper();
			fsw.setField(it.govpay.orm.Evento.model().DATA_1);

			fsw.setSortOrder(sortOrder);
			filter.getFilterSortList().add(fsw);

			String codDominioId = Utils.getInstance().getMessageFromResourceBundle(this.nomeServizio + ".codDominio.id");
			String iuvId = Utils.getInstance().getMessageFromResourceBundle(this.nomeServizio + ".iuv.id");
			String ccpId = Utils.getInstance().getMessageFromResourceBundle(this.nomeServizio + ".ccp.id");



			String codDominio = this.getParameter(uriInfo, codDominioId, String.class);
			if(StringUtils.isNotEmpty(codDominio))
				filter.setCodDominio(codDominio);

			String iuv = this.getParameter(uriInfo, iuvId, String.class);
			if(StringUtils.isNotEmpty(iuv))
				filter.setIuv(iuv); 


			String ccp = this.getParameter(uriInfo, ccpId, String.class);
			if(StringUtils.isNotEmpty(ccp))
				filter.setCcp(ccp); 

			long count = eventiBD.count(filter);

			// visualizza la ricerca solo se i risultati sono > del limit e se non sono nella schermata degli eventi di una transazione.
			visualizzaRicerca = visualizzaRicerca && this.visualizzaRicerca(count, limit);
			InfoForm infoRicerca = visualizzaRicerca ? this.getInfoRicerca(uriInfo, bd) : null;

			Elenco elenco = new Elenco(this.titoloServizio, infoRicerca,
					this.getInfoCreazione(uriInfo, bd),
					count, esportazione, cancellazione); 

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

	@SuppressWarnings("unchecked")
	@Override
	public InfoForm getInfoRicerca(UriInfo uriInfo, BasicBD bd) throws ConsoleException {
		URI ricerca = this.getUriRicerca(uriInfo, bd);
		InfoForm infoRicerca = new InfoForm(ricerca);

		String codDominioId = Utils.getInstance().getMessageFromResourceBundle(this.nomeServizio + ".codDominio.id");
		String iuvId = Utils.getInstance().getMessageFromResourceBundle(this.nomeServizio + ".iuv.id");
		String ccpId = Utils.getInstance().getMessageFromResourceBundle(this.nomeServizio + ".ccp.id");

		if(infoRicercaMap == null){
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

			domini.add(new Voce<String>(Utils.getInstance().getMessageFromResourceBundle("commons.label.qualsiasi"), ""));
			if(findAll != null && findAll.size() > 0){
				for (Dominio dominio : findAll) {
					domini.add(new Voce<String>(dominiHandler.getTitolo(dominio,bd), dominio.getCodDominio()));  
				}
			}
		} catch (ServiceException e) {
			throw new ConsoleException(e);
		}
		SelectList<String> codDominio = (SelectList<String>) infoRicercaMap.get(codDominioId);
		codDominio.setDefaultValue("");
		codDominio.setValues(domini); 
		sezioneRoot.addField(codDominio);

		InputText iuv = (InputText) infoRicercaMap.get(iuvId);
		iuv.setDefaultValue(null);
		sezioneRoot.addField(iuv);

		InputText ccp = (InputText) infoRicercaMap.get(ccpId);
		ccp.setDefaultValue(null);
		sezioneRoot.addField(ccp);

		return infoRicerca;
	}

	private void initInfoRicerca(UriInfo uriInfo, BasicBD bd) throws ConsoleException{
		if(infoRicercaMap == null){
			infoRicercaMap = new HashMap<String, ParamField<?>>();

			String codDominioId = Utils.getInstance().getMessageFromResourceBundle(this.nomeServizio + ".codDominio.id");
			String iuvId = Utils.getInstance().getMessageFromResourceBundle(this.nomeServizio + ".iuv.id");
			String ccpId = Utils.getInstance().getMessageFromResourceBundle(this.nomeServizio + ".ccp.id");

			// iuv
			String iuvLabel = Utils.getInstance().getMessageFromResourceBundle(this.nomeServizio + ".iuv.label");
			InputText iuv = new InputText(iuvId, iuvLabel, null, false, false, true, 0, 35);
			infoRicercaMap.put(iuvId, iuv);

			// ccp
			String ccpLabel = Utils.getInstance().getMessageFromResourceBundle(this.nomeServizio + ".ccp.label");
			InputText ccp = new InputText(ccpId, ccpLabel, null, false, false, true, 0, 35);
			infoRicercaMap.put(ccpId, ccp);

			List<Voce<String>> domini = new ArrayList<Voce<String>>();
			// codDominio
			String codDominioLabel = Utils.getInstance().getMessageFromResourceBundle(this.nomeServizio + ".codDominio.label");
			SelectList<String> codDominio = new SelectList<String>(codDominioId, codDominioLabel, null, false, false, true, domini);
			infoRicercaMap.put(codDominioId, codDominio);

		}
	}

	@Override
	public Object getField(UriInfo uriInfo, List<RawParamValue> values, String fieldId, BasicBD bd) throws WebApplicationException, ConsoleException {
		return null;
	}

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
				Utils.getInstance().getMessageWithParamsFromResourceBundle(this.nomeServizio + ".label.titolo",
						this.sdf.format(dataRichiesta),						
						Utils.getInstance().getMessageFromResourceBundle(this.nomeServizio + ".tipoEvento."+ tipoEvento.name()),
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
		sb.append(Utils.getInstance().getMessageWithParamsFromResourceBundle(this.nomeServizio + ".label.sottotitolo", codDominio,iuv,ccp));

		return sb.toString();
	} 

	@Override
	public String esporta(List<Long> idsToExport, UriInfo uriInfo, BasicBD bd, ZipOutputStream zout)
			throws WebApplicationException, ConsoleException {
		StringBuffer sb = new StringBuffer();
		if(idsToExport != null && idsToExport.size() > 0)
			for (Long long1 : idsToExport) {

				if(sb.length() > 0)
					sb.append(", ");

				sb.append(long1);
			}

		String methodName = "esporta " + this.titoloServizio + "[" + sb.toString() + "]";

		String fileName = "Eventi.zip";
		try{
			this.log.info("Esecuzione " + methodName + " in corso...");
			this.darsService.getOperatoreByPrincipal(bd); 

			EventiBD eventiBD = new EventiBD(bd);
			EventiFilter filter = eventiBD.newFilter();
			filter.setIdEventi(idsToExport );
			List<Evento> list = eventiBD.findAll(filter);
			StringBuilder sbCSV = new StringBuilder();

			sbCSV.append(getCsvHeader());

			for (Evento evento: list) {
				sbCSV.append("\n");
				sbCSV.append(getEventoCsv(evento));
			}

			ZipEntry datiEvento = new ZipEntry("eventi.csv");
			zout.putNextEntry(datiEvento);
			zout.write(sbCSV.toString().getBytes());
			zout.closeEntry();

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

	private String getCsvHeader(){
		StringBuilder sb = new StringBuilder();

		sb.append(Utils.getInstance().getMessageFromResourceBundle(this.nomeServizio + ".codDominio.label")).append(SEPARATORE_CSV);
		sb.append(Utils.getInstance().getMessageFromResourceBundle(this.nomeServizio + ".iuv.label")).append(SEPARATORE_CSV);
		sb.append(Utils.getInstance().getMessageFromResourceBundle(this.nomeServizio + ".ccp.label")).append(SEPARATORE_CSV);
		sb.append(Utils.getInstance().getMessageFromResourceBundle(this.nomeServizio + ".codPsp.label")).append(SEPARATORE_CSV);
		sb.append(Utils.getInstance().getMessageFromResourceBundle(this.nomeServizio + ".tipoEvento.label")).append(SEPARATORE_CSV);
		sb.append(Utils.getInstance().getMessageFromResourceBundle(this.nomeServizio + ".sottotipoEvento.label")).append(SEPARATORE_CSV);
		sb.append(Utils.getInstance().getMessageFromResourceBundle(this.nomeServizio + ".categoriaEvento.label")).append(SEPARATORE_CSV);
		sb.append(Utils.getInstance().getMessageFromResourceBundle(this.nomeServizio + ".componente.label")).append(SEPARATORE_CSV);
		sb.append(Utils.getInstance().getMessageFromResourceBundle(this.nomeServizio + ".erogatore.label")).append(SEPARATORE_CSV);
		sb.append(Utils.getInstance().getMessageFromResourceBundle(this.nomeServizio + ".fruitore.label")).append(SEPARATORE_CSV);
		sb.append(Utils.getInstance().getMessageFromResourceBundle(this.nomeServizio + ".codStazione.label")).append(SEPARATORE_CSV);
		sb.append(Utils.getInstance().getMessageFromResourceBundle(this.nomeServizio + ".codCanale.label")).append(SEPARATORE_CSV);
		sb.append(Utils.getInstance().getMessageFromResourceBundle(this.nomeServizio + ".tipoVersamento.label")).append(SEPARATORE_CSV);
		sb.append(Utils.getInstance().getMessageFromResourceBundle(this.nomeServizio + ".esito.label")).append(SEPARATORE_CSV);
		sb.append(Utils.getInstance().getMessageFromResourceBundle(this.nomeServizio + ".dataRichiesta.label")).append(SEPARATORE_CSV);
		sb.append(Utils.getInstance().getMessageFromResourceBundle(this.nomeServizio + ".dataRisposta.label")).append(SEPARATORE_CSV);
		sb.append(Utils.getInstance().getMessageFromResourceBundle(this.nomeServizio + ".altriParametriRichiesta.label")).append(SEPARATORE_CSV);
		sb.append(Utils.getInstance().getMessageFromResourceBundle(this.nomeServizio + ".altriParametriRisposta.label"));

		return sb.toString();
	}

	private String getEventoCsv(Evento evento){
		StringBuilder sb = new StringBuilder();

		if(StringUtils.isNotEmpty(evento.getCodDominio()))
			sb.append(evento.getCodDominio());
		sb.append(SEPARATORE_CSV);
		if(StringUtils.isNotEmpty(evento.getIuv()))
			sb.append(evento.getIuv());
		sb.append(SEPARATORE_CSV);
		if(StringUtils.isNotEmpty(evento.getCcp()))
			sb.append(evento.getCcp());
		sb.append(SEPARATORE_CSV);
		if(StringUtils.isNotEmpty(evento.getCodPsp()))
			sb.append(evento.getCodPsp());
		sb.append(SEPARATORE_CSV);
		if(evento.getTipoEvento() != null)
			sb.append(Utils.getInstance().getMessageFromResourceBundle(this.nomeServizio + ".tipoEvento."+evento.getTipoEvento().name()));
		sb.append(SEPARATORE_CSV);
		if(StringUtils.isNotEmpty(evento.getSottotipoEvento()))
			sb.append(evento.getSottotipoEvento());
		sb.append(SEPARATORE_CSV);
		if(evento.getCategoriaEvento() != null)
			sb.append(Utils.getInstance().getMessageFromResourceBundle(this.nomeServizio + ".categoriaEvento." +evento.getCategoriaEvento().name()));
		sb.append(SEPARATORE_CSV);
		if(StringUtils.isNotEmpty(evento.getComponente()))
			sb.append(evento.getComponente());
		sb.append(SEPARATORE_CSV);
		if(StringUtils.isNotEmpty(evento.getErogatore()))
			sb.append(evento.getErogatore());
		sb.append(SEPARATORE_CSV);
		if(StringUtils.isNotEmpty(evento.getFruitore()))
			sb.append(evento.getFruitore());
		sb.append(SEPARATORE_CSV);
		if(StringUtils.isNotEmpty(evento.getCodStazione()))
			sb.append(evento.getCodStazione());
		sb.append(SEPARATORE_CSV);
		if(StringUtils.isNotEmpty(evento.getCodCanale()))
			sb.append(evento.getCodCanale());
		sb.append(SEPARATORE_CSV);
		if(evento.getTipoVersamento()!= null)
			sb.append(Utils.getInstance().getMessageFromResourceBundle(this.nomeServizio + ".tipoVersamento." +evento.getTipoVersamento().name()));
		sb.append(SEPARATORE_CSV);
		if(StringUtils.isNotEmpty(evento.getEsito()))
			sb.append(evento.getEsito());
		sb.append(SEPARATORE_CSV);
		if(evento.getDataRichiesta()!= null)
			sb.append( this.sdf.format(evento.getDataRichiesta()));
		sb.append(SEPARATORE_CSV);
		if(evento.getDataRisposta()!= null)
			sb.append(this.sdf.format(evento.getDataRisposta()));
		sb.append(SEPARATORE_CSV);
		if(StringUtils.isNotEmpty(evento.getAltriParametriRichiesta()))
			sb.append(evento.getAltriParametriRichiesta());
		sb.append(SEPARATORE_CSV);
		if(StringUtils.isNotEmpty(evento.getAltriParametriRisposta()))
			sb.append(evento.getAltriParametriRisposta());

		return sb.toString();
	}

	@Override
	public String esporta(Long idToExport, UriInfo uriInfo, BasicBD bd, ZipOutputStream zout)
			throws WebApplicationException, ConsoleException {
		return null;
	}

	/* Creazione/Update non consentiti**/

	@Override
	public InfoForm getInfoCreazione(UriInfo uriInfo, BasicBD bd) throws ConsoleException { return null; }

	@Override
	public InfoForm getInfoModifica(UriInfo uriInfo, BasicBD bd, Evento entry) throws ConsoleException { return null; }

	@Override
	public void delete(List<Long> idsToDelete, UriInfo uriInfo, BasicBD bd) throws WebApplicationException, ConsoleException {	}

	@Override
	public Evento creaEntry(InputStream is, UriInfo uriInfo, BasicBD bd) throws WebApplicationException, ConsoleException { return null; }

	@Override
	public Dettaglio insert(InputStream is, UriInfo uriInfo, BasicBD bd) throws WebApplicationException, ConsoleException, ValidationException, DuplicatedEntryException { return null; }

	@Override
	public void checkEntry(Evento entry, Evento oldEntry) throws ValidationException { }

	@Override
	public Dettaglio update(InputStream is, UriInfo uriInfo, BasicBD bd) throws WebApplicationException, ConsoleException, ValidationException { return null; }
}
