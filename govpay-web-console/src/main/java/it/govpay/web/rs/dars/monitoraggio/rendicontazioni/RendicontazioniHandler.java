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
package it.govpay.web.rs.dars.monitoraggio.rendicontazioni;

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
import javax.ws.rs.core.UriBuilder;
import javax.ws.rs.core.UriInfo;

import org.apache.commons.lang.StringUtils;
import org.apache.logging.log4j.Logger;
import org.jboss.resteasy.plugins.providers.multipart.MultipartFormDataInput;
import org.openspcoop2.generic_project.exception.ServiceException;
import org.openspcoop2.generic_project.expression.SortOrder;

import it.govpay.bd.BasicBD;
import it.govpay.bd.FilterSortWrapper;
import it.govpay.bd.anagrafica.ApplicazioniBD;
import it.govpay.bd.anagrafica.filters.ApplicazioneFilter;
import it.govpay.bd.model.Applicazione;
import it.govpay.bd.model.Dominio;
import it.govpay.bd.model.Fr;
import it.govpay.bd.model.Fr.StatoFr;
import it.govpay.bd.model.Psp;
import it.govpay.bd.pagamento.FrBD;
import it.govpay.bd.pagamento.filters.FrFilter;
import it.govpay.web.rs.BaseRsService;
import it.govpay.web.rs.dars.BaseDarsHandler;
import it.govpay.web.rs.dars.BaseDarsService;
import it.govpay.web.rs.dars.IDarsHandler;
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
import it.govpay.web.rs.dars.monitoraggio.versamenti.Pagamenti;
import it.govpay.web.utils.Utils;

public class RendicontazioniHandler extends BaseDarsHandler<Fr> implements IDarsHandler<Fr>{

	private static Map<String, ParamField<?>> infoRicercaMap = null;
	private SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm");  

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

			FrBD frBD = new FrBD(bd);
			FrFilter filter = frBD.newFilter();
			filter.setOffset(offset);
			filter.setLimit(limit);
			FilterSortWrapper fsw = new FilterSortWrapper();
			fsw.setField(it.govpay.orm.FR.model().DATA_ORA_FLUSSO);
			fsw.setSortOrder(SortOrder.DESC);
			filter.getFilterSortList().add(fsw);

			String codFlussoId = Utils.getInstance().getMessageFromResourceBundle(this.nomeServizio + ".codFlusso.id");
			String codFlusso = this.getParameter(uriInfo, codFlussoId, String.class);
			if(StringUtils.isNotEmpty(codFlusso))
				filter.setCodFlusso(codFlusso); 

			boolean eseguiRicerca = true;
			String applicazioneId = Utils.getInstance().getMessageFromResourceBundle(this.nomeServizio + ".idApplicazione.id");
			Long idApplicazione = this.getParameter(uriInfo, applicazioneId, Long.class);
			if(idApplicazione != null && idApplicazione > 0){
				List<Long> idFlussi = frBD.getIdFlussi(idApplicazione);
				filter.setIdFlussi(idFlussi);
				eseguiRicerca = eseguiRicerca && !Utils.isEmpty(idFlussi);
			}

			long count = eseguiRicerca ? frBD.count(filter) : 0;

			// visualizza la ricerca solo se i risultati sono > del limit
			boolean visualizzaRicerca = this.visualizzaRicerca(count, limit);
			InfoForm infoRicerca = visualizzaRicerca ? this.getInfoRicerca(uriInfo, bd) : null;

			Elenco elenco = new Elenco(this.titoloServizio, infoRicerca,
					this.getInfoCreazione(uriInfo, bd),
					count, esportazione, cancellazione); 

			UriBuilder uriDettaglioBuilder = BaseRsService.checkDarsURI(uriInfo).path(this.pathServizio).path("{id}");

			List<Fr> findAll = eseguiRicerca ? frBD.findAll(filter) : new ArrayList<Fr>(); 

			if(findAll != null && findAll.size() > 0){
				for (Fr entry : findAll) {
					elenco.getElenco().add(this.getElemento(entry, entry.getId(), uriDettaglioBuilder,bd));
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

		String idApplicazioneId = Utils.getInstance().getMessageFromResourceBundle(this.nomeServizio + ".idApplicazione.id");
		String codFlussoId = Utils.getInstance().getMessageFromResourceBundle(this.nomeServizio + ".codFlusso.id");

		if(infoRicercaMap == null){
			this.initInfoRicerca(uriInfo, bd);
		}

		Sezione sezioneRoot = infoRicerca.getSezioneRoot();

		InputText codFlusso = (InputText) infoRicercaMap.get(codFlussoId);
		codFlusso.setDefaultValue(null);
		sezioneRoot.addField(codFlusso);

		// idDominio
		List<Voce<Long>> applicazioni = new ArrayList<Voce<Long>>();

		ApplicazioniBD applicazioniBD = new ApplicazioniBD(bd);
		ApplicazioneFilter filter;
		try {
			filter = applicazioniBD.newFilter();
			FilterSortWrapper fsw = new FilterSortWrapper();
			fsw.setField(it.govpay.orm.Applicazione.model().COD_APPLICAZIONE);
			fsw.setSortOrder(SortOrder.ASC);
			filter.getFilterSortList().add(fsw);
			List<Applicazione> findAll = applicazioniBD.findAll(filter );

			applicazioni.add(new Voce<Long>(Utils.getInstance().getMessageFromResourceBundle("commons.label.qualsiasi"), -1L));
			if(findAll != null && findAll.size() > 0){
				for (Applicazione applicazione : findAll) {
					applicazioni.add(new Voce<Long>(applicazione.getCodApplicazione(), applicazione.getId()));  
				}
			}
		} catch (ServiceException e) {
			throw new ConsoleException(e);
		}
		SelectList<Long> idApplicazione = (SelectList<Long>) infoRicercaMap.get(idApplicazioneId);
		idApplicazione.setDefaultValue(-1L);
		idApplicazione.setValues(applicazioni); 
		sezioneRoot.addField(idApplicazione);

		return infoRicerca;
	}

	private void initInfoRicerca(UriInfo uriInfo, BasicBD bd) throws ConsoleException{
		if(infoRicercaMap == null){
			infoRicercaMap = new HashMap<String, ParamField<?>>();

			String idApplicazioneId = Utils.getInstance().getMessageFromResourceBundle(this.nomeServizio + ".idApplicazione.id");
			String codFlussoId = Utils.getInstance().getMessageFromResourceBundle(this.nomeServizio + ".codFlusso.id");

			// codFlusso
			String codFlussoLabel = Utils.getInstance().getMessageFromResourceBundle(this.nomeServizio + ".codFlusso.label");
			InputText codFlusso = new InputText(codFlussoId, codFlussoLabel, null, false, false, true, 0, 35);
			infoRicercaMap.put(codFlussoId, codFlusso);

			List<Voce<Long>> applicazioni = new ArrayList<Voce<Long>>();
			// idApplicazione
			String idApplicazioneLabel = Utils.getInstance().getMessageFromResourceBundle(this.nomeServizio + ".idApplicazione.label");
			SelectList<Long> idApplicazione = new SelectList<Long>(idApplicazioneId, idApplicazioneLabel, null, false, false, true, applicazioni);
			infoRicercaMap.put(idApplicazioneId, idApplicazione);

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
			FrBD frBD = new FrBD(bd);
			Fr fr = frBD.getFr(id);

			InfoForm infoModifica = null;
			URI cancellazione = null;
			URI esportazione = this.getUriEsportazioneDettaglio(uriInfo, bd, id);

			String titolo = fr != null ? this.getTitolo(fr,bd) : "";
			Dettaglio dettaglio = new Dettaglio(titolo, esportazione, cancellazione, infoModifica);

			it.govpay.web.rs.dars.model.Sezione root = dettaglio.getSezioneRoot();

			if(fr != null){

				if(StringUtils.isNotEmpty(fr.getCodFlusso())) 
					root.addVoce(Utils.getInstance().getMessageFromResourceBundle(this.nomeServizio + ".codFlusso.label"), fr.getCodFlusso());

				StatoFr stato = fr.getStato();
				if(stato!= null)
					root.addVoce(Utils.getInstance().getMessageFromResourceBundle(this.nomeServizio + ".stato.label"),
							Utils.getInstance().getMessageFromResourceBundle(this.nomeServizio + ".stato."+stato.name()));

				if(StringUtils.isNotEmpty(fr.getIur())) 
					root.addVoce(Utils.getInstance().getMessageFromResourceBundle(this.nomeServizio + ".iur.label"), fr.getIur());
				// Uo
				Dominio dominio = fr.getDominio(bd);
				if(dominio != null)
					root.addVoce(Utils.getInstance().getMessageFromResourceBundle(this.nomeServizio + ".dominio.label"), dominio.getCodDominio());  

				if(fr.getDataFlusso() != null)
					root.addVoce(Utils.getInstance().getMessageFromResourceBundle(this.nomeServizio + ".dataFlusso.label"), this.sdf.format(fr.getDataFlusso()));
				if(fr.getDataRegolamento() != null)
					root.addVoce(Utils.getInstance().getMessageFromResourceBundle(this.nomeServizio + ".dataRegolamento.label"), this.sdf.format(fr.getDataRegolamento()));

				root.addVoce(Utils.getInstance().getMessageFromResourceBundle(this.nomeServizio + ".numeroPagamenti.label"), fr.getNumeroPagamenti()+ "");
				root.addVoce(Utils.getInstance().getMessageFromResourceBundle(this.nomeServizio + ".importoTotalePagamenti.label"), fr.getImportoTotalePagamenti()+ "€");
				root.addVoce(Utils.getInstance().getMessageFromResourceBundle(this.nomeServizio + ".annoRiferimento.label"), fr.getAnnoRiferimento()+"");

				Psp psp = fr.getPsp(bd);
				if(psp != null)
					root.addVoce(Utils.getInstance().getMessageFromResourceBundle(this.nomeServizio + ".psp.label"),psp.getCodPsp());

				if(StringUtils.isNotEmpty(fr.getCodBicRiversamento())) 
					root.addVoce(Utils.getInstance().getMessageFromResourceBundle(this.nomeServizio + ".codBicRiversamento.label"), fr.getCodBicRiversamento());

				if(StringUtils.isNotEmpty(fr.getDescrizioneStato())) 
					root.addVoce(Utils.getInstance().getMessageFromResourceBundle(this.nomeServizio + ".descrizioneStato.label"), fr.getDescrizioneStato());
				
				Pagamenti pagamentiDars = new Pagamenti();
				String etichettaPagamenti = Utils.getInstance().getMessageFromResourceBundle(this.nomeServizio + ".elementoCorrelato.pagamenti.titolo");
				String idFrApplicazioneId = Utils.getInstance().getMessageFromResourceBundle(pagamentiDars.getNomeServizio() + ".idFr.id");
				UriBuilder uriBuilderPagamenti = BaseRsService.checkDarsURI(uriInfo).path(pagamentiDars.getPathServizio()).queryParam(idFrApplicazioneId, fr.getId());
				
				dettaglio.addElementoCorrelato(etichettaPagamenti, uriBuilderPagamenti.build()); 
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
	public String getTitolo(Fr entry,BasicBD bd) {
		StringBuilder sb = new StringBuilder();

		String codFlusso = entry.getCodFlusso();
		Date dataFlusso = entry.getDataFlusso();

		sb.append(
				Utils.getInstance().getMessageWithParamsFromResourceBundle(this.nomeServizio + ".label.titolo",
						codFlusso,this.sdf.format(dataFlusso)));

		return sb.toString();
	}

	@Override
	public String getSottotitolo(Fr entry,BasicBD bd) {
		StringBuilder sb = new StringBuilder();
		StatoFr stato = entry.getStato();
		long numeroPagamenti = entry.getNumeroPagamenti();
		double importoTotalePagamenti = entry.getImportoTotalePagamenti();

		switch (stato) {
		case ACCETTATA:
			sb.append(
					Utils.getInstance().getMessageWithParamsFromResourceBundle(this.nomeServizio + ".label.sottotitolo.accettata",
							Utils.getInstance().getMessageFromResourceBundle(this.nomeServizio + ".stato.ACCETTATA"),
							numeroPagamenti,(importoTotalePagamenti + "€")));
			break;
		case RIFIUTATA:
		default:
			sb.append(
					Utils.getInstance().getMessageWithParamsFromResourceBundle(this.nomeServizio + ".label.sottotitolo.rifiutata",
					Utils.getInstance().getMessageFromResourceBundle(this.nomeServizio + ".stato.RIFIUTATA")
							)
					);
			break;
		}

		return sb.toString();
	} 
	
	@Override
	public List<String> getValori(Fr entry, BasicBD bd) throws ConsoleException {
		return null;
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

		if(idsToExport.size() == 1)
			return this.esporta(idsToExport.get(0), uriInfo, bd, zout); 

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
	public InfoForm getInfoModifica(UriInfo uriInfo, BasicBD bd, Fr entry) throws ConsoleException { return null; }

	@Override
	public void delete(List<Long> idsToDelete, UriInfo uriInfo, BasicBD bd) throws WebApplicationException, ConsoleException {	}

	@Override
	public Fr creaEntry(InputStream is, UriInfo uriInfo, BasicBD bd) throws WebApplicationException, ConsoleException { return null; }

	@Override
	public Dettaglio insert(InputStream is, UriInfo uriInfo, BasicBD bd) throws WebApplicationException, ConsoleException, ValidationException, DuplicatedEntryException { return null; }

	@Override
	public void checkEntry(Fr entry, Fr oldEntry) throws ValidationException { }

	@Override
	public Dettaglio update(InputStream is, UriInfo uriInfo, BasicBD bd) throws WebApplicationException, ConsoleException, ValidationException { return null; }
	
	@Override
	public Object uplaod(MultipartFormDataInput input, UriInfo uriInfo, BasicBD bd)	throws WebApplicationException, ConsoleException, ValidationException { return null;}
}
