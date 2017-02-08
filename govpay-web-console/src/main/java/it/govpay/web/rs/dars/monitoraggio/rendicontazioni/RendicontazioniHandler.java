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
package it.govpay.web.rs.dars.monitoraggio.rendicontazioni;

import java.io.InputStream;
import java.math.BigDecimal;
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
import org.openspcoop2.generic_project.exception.NotFoundException;
import org.openspcoop2.generic_project.exception.ServiceException;
import org.openspcoop2.generic_project.expression.SortOrder;

import it.govpay.bd.BasicBD;
import it.govpay.bd.FilterSortWrapper;
import it.govpay.bd.anagrafica.AnagraficaManager;
import it.govpay.bd.model.Dominio;
import it.govpay.bd.model.Fr;
import it.govpay.bd.model.Pagamento;
import it.govpay.bd.model.Rendicontazione;
import it.govpay.bd.pagamento.FrBD;
import it.govpay.bd.pagamento.RendicontazioniBD;
import it.govpay.bd.pagamento.filters.RendicontazioneFilter;
import it.govpay.model.Rendicontazione.Anomalia;
import it.govpay.model.Rendicontazione.EsitoRendicontazione;
import it.govpay.model.Rendicontazione.StatoRendicontazione;
import it.govpay.web.rs.dars.BaseDarsHandler;
import it.govpay.web.rs.dars.BaseDarsService;
import it.govpay.web.rs.dars.IDarsHandler;
import it.govpay.web.rs.dars.anagrafica.domini.Domini;
import it.govpay.web.rs.dars.anagrafica.domini.DominiHandler;
import it.govpay.web.rs.dars.exception.ConsoleException;
import it.govpay.web.rs.dars.exception.DuplicatedEntryException;
import it.govpay.web.rs.dars.exception.ValidationException;
import it.govpay.web.rs.dars.model.Dettaglio;
import it.govpay.web.rs.dars.model.Elemento;
import it.govpay.web.rs.dars.model.Elenco;
import it.govpay.web.rs.dars.model.InfoForm;
import it.govpay.web.rs.dars.model.InfoForm.Sezione;
import it.govpay.web.rs.dars.model.RawParamValue;
import it.govpay.web.rs.dars.model.Voce;
import it.govpay.web.rs.dars.model.input.ParamField;
import it.govpay.web.rs.dars.model.input.base.InputText;
import it.govpay.web.rs.dars.model.input.base.SelectList;
import it.govpay.web.rs.dars.monitoraggio.versamenti.Pagamenti;
import it.govpay.web.rs.dars.monitoraggio.versamenti.PagamentiHandler;
import it.govpay.web.utils.Utils;

public class RendicontazioniHandler extends BaseDarsHandler<Rendicontazione> implements IDarsHandler<Rendicontazione>{

	private Map<String, ParamField<?>> infoRicercaMap = null;
	private SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm");  
	//private SimpleDateFormat simpleDateFormatAnno = new SimpleDateFormat("yyyy");


	public RendicontazioniHandler(Logger log, BaseDarsService darsService) { 
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
			URI esportazione = this.getUriEsportazione(uriInfo, bd); 
			URI cancellazione = null;

			this.log.info("Esecuzione " + methodName + " in corso..."); 

			RendicontazioniBD frBD = new RendicontazioniBD(bd);
			RendicontazioneFilter filter = frBD.newFilter();
			filter.setOffset(offset);
			filter.setLimit(limit);
			FilterSortWrapper fsw = new FilterSortWrapper();
			fsw.setField(it.govpay.orm.Rendicontazione.model().DATA);
			fsw.setSortOrder(SortOrder.DESC);
			filter.getFilterSortList().add(fsw);

			boolean eseguiRicerca = true;
			Map<String, String> params = new HashMap<String, String>();
			String idFlussoId = Utils.getInstance(this.getLanguage()).getMessageFromResourceBundle(this.nomeServizio + ".idFr.id");
			String idFlusso = this.getParameter(uriInfo, idFlussoId, String.class);
			params.put(idFlussoId, idFlusso);

			if(StringUtils.isNotEmpty(idFlusso)){
				try{
					filter.setIdFr(Long.parseLong(idFlusso));
				}catch(Exception e){
					filter.setIdFr(-1L);
					eseguiRicerca = false;
				}
			}

			String statoId = Utils.getInstance(this.getLanguage()).getMessageFromResourceBundle(this.nomeServizio + ".stato.id");
			String stato = this.getParameter(uriInfo, statoId, String.class);

			if(StringUtils.isNotEmpty(stato))
				filter.setStato(StatoRendicontazione.valueOf(stato)); 

			String tipoId = Utils.getInstance(this.getLanguage()).getMessageFromResourceBundle(this.nomeServizio + ".tipo.id");
			String tipo = this.getParameter(uriInfo, tipoId, String.class);

			if(StringUtils.isNotEmpty(tipo))
				filter.setTipo(tipo); 

			String iuvId = Utils.getInstance(this.getLanguage()).getMessageFromResourceBundle(this.nomeServizio + ".iuv.id");
			String iuv = this.getParameter(uriInfo, iuvId, String.class);

			if(StringUtils.isNotEmpty(iuv))
				filter.setIuv(iuv);

			long count = eseguiRicerca ? frBD.count(filter) : 0;

			// visualizza la ricerca solo se i risultati sono > del limit
			boolean visualizzaRicerca = this.visualizzaRicerca(count, limit);
			InfoForm infoRicerca = this.getInfoRicerca(uriInfo, bd, visualizzaRicerca,params); 

			String formatter = Utils.getInstance(this.getLanguage()).getMessageFromResourceBundle(this.nomeServizio+".elenco.formatter");
			Elenco elenco = new Elenco(this.titoloServizio, infoRicerca,
					this.getInfoCreazione(uriInfo, bd),
					count, esportazione, cancellazione); 

			List<Rendicontazione> findAll = eseguiRicerca ? frBD.findAll(filter) : new ArrayList<Rendicontazione>(); 

			if(findAll != null && findAll.size() > 0){
				for (Rendicontazione entry : findAll) {
					Elemento elemento = this.getElemento(entry, entry.getId(), this.pathServizio,bd);
					elemento.setFormatter(formatter); 
					elenco.getElenco().add(elemento);
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
		URI ricerca = this.getUriRicerca(uriInfo, bd,parameters);
		InfoForm infoRicerca = new InfoForm(ricerca);

		if(visualizzaRicerca) {
			String statoId = Utils.getInstance(this.getLanguage()).getMessageFromResourceBundle(this.nomeServizio + ".stato.id");
			String tipoId = Utils.getInstance(this.getLanguage()).getMessageFromResourceBundle(this.nomeServizio + ".tipo.id");
			String iuvId = Utils.getInstance(this.getLanguage()).getMessageFromResourceBundle(this.nomeServizio + ".iuv.id");

			if(this.infoRicercaMap == null){
				this.initInfoRicerca(uriInfo, bd);
			}
			Sezione sezioneRoot = infoRicerca.getSezioneRoot();

			// stato
			List<Voce<String>> stati = new ArrayList<Voce<String>>();
			SelectList<String> stato = (SelectList<String>) infoRicercaMap.get(statoId);
			stati.add(new Voce<String>(Utils.getInstance(this.getLanguage()).getMessageFromResourceBundle("commons.label.qualsiasi"), ""));
			stati.add(new Voce<String>(Utils.getInstance(this.getLanguage()).getMessageFromResourceBundle(this.nomeServizio + ".stato."+StatoRendicontazione.OK), StatoRendicontazione.OK.toString()));
			stati.add(new Voce<String>(Utils.getInstance(this.getLanguage()).getMessageFromResourceBundle(this.nomeServizio + ".stato."+StatoRendicontazione.ANOMALA), StatoRendicontazione.ANOMALA.toString()));
			stati.add(new Voce<String>(Utils.getInstance(this.getLanguage()).getMessageFromResourceBundle(this.nomeServizio + ".stato."+StatoRendicontazione.ALTRO_INTERMEDIARIO), StatoRendicontazione.ALTRO_INTERMEDIARIO.toString()));
			stato.setDefaultValue("");
			stato.setValues(stati); 
			sezioneRoot.addField(stato);

			// tipo
			List<Voce<String>> tipi = new ArrayList<Voce<String>>();
			SelectList<String> tipo = (SelectList<String>) infoRicercaMap.get(tipoId);
			//			tipi.add(new Voce<String>(Utils.getInstance(this.getLanguage()).getMessageFromResourceBundle("commons.label.qualsiasi"), ""));
			//			tipi.add(new Voce<String>(Utils.getInstance(this.getLanguage()).getMessageFromResourceBundle(this.nomeServizio + ".stato."+StatoRendicontazione.OK), StatoRendicontazione.OK.toString()));
			//			tipi.add(new Voce<String>(Utils.getInstance(this.getLanguage()).getMessageFromResourceBundle(this.nomeServizio + ".stato."+StatoRendicontazione.ANOMALA), StatoRendicontazione.ANOMALA.toString()));
			//			tipi.add(new Voce<String>(Utils.getInstance(this.getLanguage()).getMessageFromResourceBundle(this.nomeServizio + ".stato."+StatoRendicontazione.ALTRO_INTERMEDIARIO), StatoRendicontazione.ALTRO_INTERMEDIARIO.toString()));
			tipo.setDefaultValue("");
			tipo.setValues(tipi); 
			sezioneRoot.addField(tipo);


			// iuv
			InputText iuv = (InputText) infoRicercaMap.get(iuvId);
			iuv.setDefaultValue(null);
			sezioneRoot.addField(iuv);

		}
		return infoRicerca;
	}

	private void initInfoRicerca(UriInfo uriInfo, BasicBD bd) throws ConsoleException{
		if(this.infoRicercaMap == null){
			this.infoRicercaMap = new HashMap<String, ParamField<?>>();

			String statoId = Utils.getInstance(this.getLanguage()).getMessageFromResourceBundle(this.nomeServizio + ".stato.id");
			String tipoId = Utils.getInstance(this.getLanguage()).getMessageFromResourceBundle(this.nomeServizio + ".tipo.id");
			String iuvId = Utils.getInstance(this.getLanguage()).getMessageFromResourceBundle(this.nomeServizio + ".iuv.id");

			// iuv
			String iuvLabel = Utils.getInstance(this.getLanguage()).getMessageFromResourceBundle(this.nomeServizio + ".iuv.label");
			InputText iuv = new InputText(iuvId, iuvLabel, null, false, false, true, 0, 35);
			this.infoRicercaMap.put(iuvId, iuv);

			List<Voce<String>> stati = new ArrayList<Voce<String>>();
			// stato
			String statoLabel = Utils.getInstance(this.getLanguage()).getMessageFromResourceBundle(this.nomeServizio + ".stato.label");
			SelectList<String> stato = new SelectList<String>(statoId, statoLabel, null, false, false, true, stati);
			infoRicercaMap.put(statoId, stato);


			List<Voce<String>> tipi = new ArrayList<Voce<String>>();
			// tipo
			String tipoLabel = Utils.getInstance(this.getLanguage()).getMessageFromResourceBundle(this.nomeServizio + ".tipo.label");
			SelectList<String> tipo = new SelectList<String>(tipoId, tipoLabel, null, false, false, true, tipi);
			infoRicercaMap.put(tipoId, tipo);
		}
	}

	@Override
	public Object getField(UriInfo uriInfo, List<RawParamValue> values, String fieldId, BasicBD bd) throws WebApplicationException, ConsoleException {
		return null;
	}

	@Override
	public Dettaglio getDettaglio(long id, UriInfo uriInfo, BasicBD bd)
			throws WebApplicationException, ConsoleException {
		String methodName = "dettaglio " + this.titoloServizio + "."+ id;

		try{
			this.log.info("Esecuzione " + methodName + " in corso...");
			// Operazione consentita agli utenti registrati
			this.darsService.getOperatoreByPrincipal(bd); 


			// recupero oggetto
			RendicontazioniBD frBD = new RendicontazioniBD(bd);
			Rendicontazione rendicontazione = frBD.getRendicontazione(id);

			InfoForm infoModifica = null;
			URI cancellazione = null;
			URI esportazione = this.getUriEsportazioneDettaglio(uriInfo, bd, id);

			String titolo = rendicontazione != null ? this.getTitolo(rendicontazione,bd) : "";
			Dettaglio dettaglio = new Dettaglio(titolo, esportazione, cancellazione, infoModifica);

			it.govpay.web.rs.dars.model.Sezione root = dettaglio.getSezioneRoot();

			String codDominio = null;
			if(rendicontazione != null){
				Pagamento pagamento = rendicontazione.getPagamento(bd);
				if(pagamento != null) {
					codDominio = pagamento.getCodDominio();
				}  else {
					Fr fr = rendicontazione.getFr(bd);
					codDominio = fr.getCodDominio();
				}

				// dominio
				if(StringUtils.isNotEmpty(codDominio)){
					try{
						Dominio dominio = AnagraficaManager.getDominio(bd, codDominio);
						Domini dominiDars = new Domini();
						DominiHandler dominiDarsHandler =  (DominiHandler) dominiDars.getDarsHandler();
						Elemento elemento = dominiDarsHandler.getElemento(dominio, dominio.getId(), dominiDars.getPathServizio(), bd);
						root.addVoce(Utils.getInstance(this.getLanguage()).getMessageFromResourceBundle(this.nomeServizio + ".dominio.label"), elemento.getTitolo(),elemento.getUri());
					}catch(NotFoundException e){}
				}
				
				// iur
				if(StringUtils.isNotEmpty(rendicontazione.getIur())) {
					root.addVoce(Utils.getInstance(this.getLanguage()).getMessageFromResourceBundle(this.nomeServizio + ".iur.label"), rendicontazione.getIur());
				}
				
				if(StringUtils.isNotEmpty(rendicontazione.getIuv())) {
					root.addVoce(Utils.getInstance(this.getLanguage()).getMessageFromResourceBundle(this.nomeServizio + ".iuv.label"), rendicontazione.getIuv());
				}
				
				// [TODO] chiedere nardi
				if(StringUtils.isNotEmpty(rendicontazione.getIuv())) {
					root.addVoce(Utils.getInstance(this.getLanguage()).getMessageFromResourceBundle(this.nomeServizio + ".ccp.label"), rendicontazione.getIuv());
				}
				
				if(rendicontazione.getData() != null) {
					root.addVoce(Utils.getInstance(this.getLanguage()).getMessageFromResourceBundle(this.nomeServizio + ".data.label"), this.sdf.format(rendicontazione.getData()));
				}
				
				BigDecimal importoPagato = rendicontazione.getImportoPagato() != null ? rendicontazione.getImportoPagato() : BigDecimal.ZERO;  
				root.addVoce(Utils.getInstance(this.getLanguage()).getMessageFromResourceBundle(this.nomeServizio + ".importo.label"), importoPagato.doubleValue()+ "€");

				StatoRendicontazione stato = rendicontazione.getStato();
				if(stato!= null) {
					root.addVoce(Utils.getInstance(this.getLanguage()).getMessageFromResourceBundle(this.nomeServizio + ".stato.label"),
							Utils.getInstance(this.getLanguage()).getMessageFromResourceBundle(this.nomeServizio + ".stato."+stato.name()));
				}
				
				EsitoRendicontazione esito = rendicontazione.getEsito();
				if(esito!= null) {
					root.addVoce(Utils.getInstance(this.getLanguage()).getMessageFromResourceBundle(this.nomeServizio + ".esito.label"),
							Utils.getInstance(this.getLanguage()).getMessageFromResourceBundle(this.nomeServizio + ".esito."+esito.name()));
				}
				
				if(pagamento != null) {
					Pagamenti pagamentiDars = new Pagamenti();
					PagamentiHandler pagamentiDarsHandler = (PagamentiHandler) pagamentiDars.getDarsHandler();
					Elemento elemento = pagamentiDarsHandler.getElemento(pagamento, pagamento.getId(), pagamentiDars.getPathServizio(), bd);
					root.addVoce(Utils.getInstance(this.getLanguage()).getMessageFromResourceBundle(this.nomeServizio + ".idPagamento.label"), elemento.getTitolo(),elemento.getUri());
				}

				if(rendicontazione.getAnomalie()!= null && rendicontazione.getAnomalie().size() > 0) {
					String etichettaSezioneAnomalie = Utils.getInstance(this.getLanguage()).getMessageFromResourceBundle(this.nomeServizio + ".sezioneAnomalie.label");
					it.govpay.web.rs.dars.model.Sezione sezioneAnomalie = dettaglio.addSezione(etichettaSezioneAnomalie );

					for (Anomalia anomalia : rendicontazione.getAnomalie()) {
						sezioneAnomalie.addVoce(anomalia.getCodice(),anomalia.getDescrizione());
					}
				}
			}

			this.log.info("Esecuzione " + methodName + " completata.");

			return dettaglio;
		}catch(WebApplicationException e){
			throw e;
		}catch(Exception e){
			throw new ConsoleException(e);
		}
	}

	@Override
	public String getTitolo(Rendicontazione entry,BasicBD bd) {
		StringBuilder sb = new StringBuilder();

		String codFlusso = entry.getIur();
		Date dataFlusso = entry.getData();

		sb.append(
				Utils.getInstance(this.getLanguage()).getMessageWithParamsFromResourceBundle(this.nomeServizio + ".label.titolo",
						codFlusso,this.sdf.format(dataFlusso)));

		return sb.toString();
	}

	@Override
	public String getSottotitolo(Rendicontazione entry,BasicBD bd) {
		StringBuilder sb = new StringBuilder();
		StatoRendicontazione stato = entry.getStato();
		BigDecimal importoPagato = entry.getImportoPagato() != null ? entry.getImportoPagato() : BigDecimal.ZERO;  
		double importoTotalePagamenti = importoPagato.doubleValue();

		boolean storno = importoTotalePagamenti < 0;
		
		String label = storno ? "storno" : "sottotitolo";
		switch (stato) {
		case OK:
			sb.append(
					Utils.getInstance(this.getLanguage()).getMessageWithParamsFromResourceBundle(this.nomeServizio + ".label." + label + ".ok",
							Utils.getInstance(this.getLanguage()).getMessageFromResourceBundle(this.nomeServizio + ".stato.OK"),
							(importoTotalePagamenti + "€")));
			break;
		case ALTRO_INTERMEDIARIO:
			sb.append(
					Utils.getInstance(this.getLanguage()).getMessageWithParamsFromResourceBundle(this.nomeServizio + ".label." + label + ".altroIntermediario",
							Utils.getInstance(this.getLanguage()).getMessageFromResourceBundle(this.nomeServizio + ".stato.ALTRO_INTERMEDIARIO"),
							(importoTotalePagamenti + "€")));
			break;
		case ANOMALA:
		default:
			sb.append(
					Utils.getInstance(this.getLanguage()).getMessageWithParamsFromResourceBundle(this.nomeServizio + ".label." + label + ".anomala",
							Utils.getInstance(this.getLanguage()).getMessageFromResourceBundle(this.nomeServizio + ".stato.ANOMALA"),
							(importoTotalePagamenti + "€")));
			break;
		}

		return sb.toString();
	} 

	@Override
	public List<String> getValori(Rendicontazione entry, BasicBD bd) throws ConsoleException {
		return null;
	}

	@Override
	public Map<String, Voce<String>> getVoci(Rendicontazione entry, BasicBD bd) throws ConsoleException { 
		Map<String, Voce<String>> voci = new HashMap<String, Voce<String>>();


		// [TODO] chiedere nardi
		voci.put(Utils.getInstance(this.getLanguage()).getMessageFromResourceBundle(this.nomeServizio + ".ccp.id"),
				new Voce<String>(Utils.getInstance(this.getLanguage()).getMessageFromResourceBundle(this.nomeServizio + ".ccp.label"),
						""));

		if(StringUtils.isNotEmpty(entry.getIuv())){
			voci.put(Utils.getInstance(this.getLanguage()).getMessageFromResourceBundle(this.nomeServizio + ".iuv.id"),
					new Voce<String>(Utils.getInstance(this.getLanguage()).getMessageFromResourceBundle(this.nomeServizio + ".iuv.label"),
							entry.getIuv()));
		}

		if(StringUtils.isNotEmpty(entry.getIur())){
			voci.put(Utils.getInstance(this.getLanguage()).getMessageFromResourceBundle(this.nomeServizio + ".iur.id"),
					new Voce<String>(Utils.getInstance(this.getLanguage()).getMessageFromResourceBundle(this.nomeServizio + ".iur.label"),
							entry.getIur()));
		}

		StatoRendicontazione stato = entry.getStato();
		if(stato!= null) {
			voci.put(Utils.getInstance(this.getLanguage()).getMessageFromResourceBundle(this.nomeServizio + ".stato.id"),
					new Voce<String>(this.getSottotitolo(entry, bd),
							stato.name()));
		}

		if(entry.getData() != null) {
			voci.put(Utils.getInstance(this.getLanguage()).getMessageFromResourceBundle(this.nomeServizio + ".data.id"),
					new Voce<String>(Utils.getInstance(this.getLanguage()).getMessageFromResourceBundle(this.nomeServizio + ".data.label"),
							this.sdf.format(entry.getData())));
		}

		BigDecimal importoPagato = entry.getImportoPagato() != null ? entry.getImportoPagato() : BigDecimal.ZERO;  
		voci.put(Utils.getInstance(this.getLanguage()).getMessageFromResourceBundle(this.nomeServizio + ".importo.id"),
				new Voce<String>(Utils.getInstance(this.getLanguage()).getMessageFromResourceBundle(this.nomeServizio + ".importo.label"),
						importoPagato.doubleValue()+ "€"));

		if(entry.getAnomalie() != null && entry.getAnomalie().size() > 0){
			voci.put(Utils.getInstance(this.getLanguage()).getMessageFromResourceBundle(this.nomeServizio + ".anomalie.id"),
					new Voce<String>(Utils.getInstance(this.getLanguage()).getMessageFromResourceBundle(this.nomeServizio + ".anomalie.label"),
							""));

			for (int i = 0 ; i < entry.getAnomalie().size() ; i ++) {
				Anomalia anomalia = entry.getAnomalie().get(i);
				voci.put(Utils.getInstance(this.getLanguage()).getMessageFromResourceBundle(this.nomeServizio + ".anomalie.id") + "_"+i,
						new Voce<String>(anomalia.getCodice(),anomalia.getDescrizione()));
			}
		}
		try {
			Pagamento pagamento = entry.getPagamento(bd);
			String codDominio = null;
			if(pagamento != null) {
				codDominio = pagamento.getCodDominio();

			} else {
				Fr fr = entry.getFr(bd);
				codDominio = fr.getCodDominio();
			}

			if(StringUtils.isNotEmpty(codDominio)){
				try{
					Dominio dominio = AnagraficaManager.getDominio(bd, codDominio);
					Domini dominiDars = new Domini();
					DominiHandler dominiDarsHandler =  (DominiHandler) dominiDars.getDarsHandler();
					String dominioTitolo = dominiDarsHandler.getTitolo(dominio, bd);
					voci.put(Utils.getInstance(this.getLanguage()).getMessageFromResourceBundle(this.nomeServizio + ".dominio.id"),
							new Voce<String>(Utils.getInstance(this.getLanguage()).getMessageFromResourceBundle(this.nomeServizio + ".dominio.label"),
									dominioTitolo));
				}catch(NotFoundException e){}
			}
		}catch(ServiceException e){

		}

		return voci; 

	}

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

		String fileName = "Rendicontazioni.zip";
		try{
			this.log.info("Esecuzione " + methodName + " in corso...");
			this.darsService.getOperatoreByPrincipal(bd); 

			FrBD frBD = new FrBD(bd);

			for (Long idVersamento : idsToExport) {
				Fr fr = frBD.getFr(idVersamento);

				String folderName = "Rendicontazione_" + fr.getIur();

				ZipEntry frXml = new ZipEntry(folderName +"/fr.xml");
				zout.putNextEntry(frXml);
				zout.write(fr.getXml());
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

	@Override
	public String esporta(Long idToExport, UriInfo uriInfo, BasicBD bd, ZipOutputStream zout)
			throws WebApplicationException, ConsoleException {
		String methodName = "esporta " + this.titoloServizio + "[" + idToExport + "]";  


		try{
			this.log.info("Esecuzione " + methodName + " in corso...");
			this.darsService.getOperatoreByPrincipal(bd); 

			FrBD frBD = new FrBD(bd);
			Fr fr = frBD.getFr(idToExport);			

			String fileName = "Rendicontazione_" + fr.getIur()+".zip";


			ZipEntry frXml = new ZipEntry("fr.xml");
			zout.putNextEntry(frXml);
			zout.write(fr.getXml());
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

	/* Creazione/Update non consentiti**/

	@Override
	public InfoForm getInfoCreazione(UriInfo uriInfo, BasicBD bd) throws ConsoleException { return null; }

	@Override
	public InfoForm getInfoModifica(UriInfo uriInfo, BasicBD bd, Rendicontazione entry) throws ConsoleException { return null; }

	@Override
	public void delete(List<Long> idsToDelete, UriInfo uriInfo, BasicBD bd) throws WebApplicationException, ConsoleException {	}

	@Override
	public Rendicontazione creaEntry(InputStream is, UriInfo uriInfo, BasicBD bd) throws WebApplicationException, ConsoleException { return null; }

	@Override
	public Dettaglio insert(InputStream is, UriInfo uriInfo, BasicBD bd) throws WebApplicationException, ConsoleException, ValidationException, DuplicatedEntryException { return null; }

	@Override
	public void checkEntry(Rendicontazione entry, Rendicontazione oldEntry) throws ValidationException { }

	@Override
	public Dettaglio update(InputStream is, UriInfo uriInfo, BasicBD bd) throws WebApplicationException, ConsoleException, ValidationException { return null; }

	@Override
	public Object uplaod(MultipartFormDataInput input, UriInfo uriInfo, BasicBD bd)	throws WebApplicationException, ConsoleException, ValidationException { return null;}
}
