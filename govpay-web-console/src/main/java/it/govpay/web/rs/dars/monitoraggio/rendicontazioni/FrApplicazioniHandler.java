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
import java.math.BigDecimal;
import java.net.URI;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.zip.ZipOutputStream;

import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.UriBuilder;
import javax.ws.rs.core.UriInfo;

import org.apache.commons.lang.StringUtils;
import org.apache.logging.log4j.Logger;

import it.govpay.bd.BasicBD;
import it.govpay.bd.model.Applicazione;
import it.govpay.bd.model.Fr;
import it.govpay.bd.model.FrApplicazione;
import it.govpay.bd.pagamento.FrBD;
import it.govpay.bd.pagamento.filters.FrApplicazioneFilter;
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
import it.govpay.web.rs.dars.model.input.ParamField;
import it.govpay.web.rs.dars.model.input.base.InputText;
import it.govpay.web.rs.dars.monitoraggio.versamenti.Pagamenti;
import it.govpay.web.utils.Utils;

public class FrApplicazioniHandler extends BaseDarsHandler<FrApplicazione> implements IDarsHandler<FrApplicazione>{

	private static Map<String, ParamField<?>> infoRicercaMap = null;

	public FrApplicazioniHandler(Logger log, BaseDarsService darsService) { 
		super(log, darsService);
	}

	@Override
	public Elenco getElenco(UriInfo uriInfo, BasicBD bd) throws WebApplicationException, ConsoleException {
		String methodName = "getElenco " + this.titoloServizio;
		try{	
			// Operazione consentita agli utenti registrati
			//Operatore operatore = this.darsService.getOperatoreByPrincipal(bd); 
			//ProfiloOperatore profilo = operatore.getProfilo();
			//boolean isAdmin = profilo.equals(ProfiloOperatore.ADMIN);


			Integer offset = this.getOffset(uriInfo);
			Integer limit = this.getLimit(uriInfo);
			URI esportazione = null; //this.getUriEsportazione(uriInfo, bd); 
			URI cancellazione = null;

			this.log.info("Esecuzione " + methodName + " in corso..."); 

			FrBD frBD = new FrBD(bd);
			FrApplicazioneFilter filter = frBD.newFrApplicazioneFilter();
			filter.setOffset(offset);
			filter.setLimit(limit);
//			FilterSortWrapper fsw = new FilterSortWrapper();
//			fsw.setField(it.govpay.orm.FrApplicazione.model().ID_FR.COD_FLUSSO);
//			fsw.setSortOrder(SortOrder.ASC);
//			filter.getFilterSortList().add(fsw);

			String codFlussoId = Utils.getInstance().getMessageFromResourceBundle(this.nomeServizio + ".codFlusso.id");
			String codFlusso = this.getParameter(uriInfo, codFlussoId, String.class);
			if(StringUtils.isNotEmpty(codFlusso))
				filter.setCodFlusso(codFlusso); 

			boolean eseguiRicerca = true;// isAdmin;
//			if(!isAdmin){
//				filter.setIdApplicazioni(operatore.getIdApplicazioni());
//				eseguiRicerca = !Utils.isEmpty(operatore.getIdApplicazioni());
//			}

			long count = eseguiRicerca ? frBD.countFrApplicazione(filter) : 0;

			// visualizza la ricerca solo se i risultati sono > del limit
			boolean visualizzaRicerca = this.visualizzaRicerca(count, limit);
			InfoForm infoRicerca = visualizzaRicerca ? this.getInfoRicerca(uriInfo, bd) : null;

			Elenco elenco = new Elenco(this.titoloServizio, infoRicerca,
					this.getInfoCreazione(uriInfo, bd),
					count, esportazione, cancellazione); 

			UriBuilder uriDettaglioBuilder = BaseRsService.checkDarsURI(uriInfo).path(this.pathServizio).path("{id}");

			List<FrApplicazione> findAll = eseguiRicerca ? frBD.findAllFrApplicazione(filter) : new ArrayList<FrApplicazione>(); 

			if(findAll != null && findAll.size() > 0){
				for (FrApplicazione entry : findAll) {
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

	@Override
	public InfoForm getInfoRicerca(UriInfo uriInfo, BasicBD bd) throws ConsoleException {
		URI ricerca = this.getUriRicerca(uriInfo, bd);
		InfoForm infoRicerca = new InfoForm(ricerca);

		String codFlussoId = Utils.getInstance().getMessageFromResourceBundle(this.nomeServizio + ".codFlusso.id");

		if(infoRicercaMap == null){
			this.initInfoRicerca(uriInfo, bd);
		}

		Sezione sezioneRoot = infoRicerca.getSezioneRoot();

		InputText codFlusso = (InputText) infoRicercaMap.get(codFlussoId);
		codFlusso.setDefaultValue(null);
		sezioneRoot.addField(codFlusso);

		return infoRicerca;
	}

	private void initInfoRicerca(UriInfo uriInfo, BasicBD bd) throws ConsoleException{
		if(infoRicercaMap == null){
			infoRicercaMap = new HashMap<String, ParamField<?>>();

			String codFlussoId = Utils.getInstance().getMessageFromResourceBundle(this.nomeServizio + ".codFlusso.id");

			// codFlusso
			String codFlussoLabel = Utils.getInstance().getMessageFromResourceBundle(this.nomeServizio + ".codFlusso.label");
			InputText codFlusso = new InputText(codFlussoId, codFlussoLabel, null, false, false, true, 0, 35);
			infoRicercaMap.put(codFlussoId, codFlusso);

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
			FrApplicazione frApplicazione = frBD.getFrApplicazione(id);

			InfoForm infoModifica = null;
			URI cancellazione = null;
			URI esportazione = this.getUriEsportazioneDettaglio(uriInfo, bd, id);

			String titolo = frApplicazione != null ? this.getTitolo(frApplicazione,bd) : "";
			Dettaglio dettaglio = new Dettaglio(titolo, esportazione, cancellazione, infoModifica);

			it.govpay.web.rs.dars.model.Sezione root = dettaglio.getSezioneRoot();

			if(frApplicazione != null){
				root.addVoce(Utils.getInstance().getMessageFromResourceBundle(this.nomeServizio + ".numeroPagamenti.label"), frApplicazione.getNumeroPagamenti()+ "");
				root.addVoce(Utils.getInstance().getMessageFromResourceBundle(this.nomeServizio + ".importoTotalePagamenti.label"), frApplicazione.getImportoTotalePagamenti()+ "€");
				
				Pagamenti pagamentiDars = new Pagamenti();
				String etichettaPagamenti = Utils.getInstance().getMessageFromResourceBundle(this.nomeServizio + ".elementoCorrelato.pagamenti.titolo");
				String idFrApplicazioneId = Utils.getInstance().getMessageFromResourceBundle(pagamentiDars.getNomeServizio() + ".idFrApplicazione.id");
				UriBuilder uriBuilderPagamenti = BaseRsService.checkDarsURI(uriInfo).path(pagamentiDars.getPathServizio()).queryParam(idFrApplicazioneId, frApplicazione.getId());
				
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
	public String getTitolo(FrApplicazione entry, BasicBD bd) throws ConsoleException {
		StringBuilder sb = new StringBuilder();

		try{
			Fr fr = entry.getFr(bd);
			Applicazione applicazione = entry.getApplicazione(bd);

			String codFlusso = fr.getCodFlusso();
			String codApplicazione = applicazione.getCodApplicazione();

			sb.append(
					Utils.getInstance().getMessageWithParamsFromResourceBundle(this.nomeServizio + ".label.titolo",
							codApplicazione,codFlusso));
		}catch (Exception e ){
			throw new ConsoleException(e);
		}

		return sb.toString();
	}

	@Override
	public String getSottotitolo(FrApplicazione entry,BasicBD bd) throws ConsoleException {
		StringBuilder sb = new StringBuilder();
		long numeroPagamenti = entry.getNumeroPagamenti();
		BigDecimal importoTotalePagamenti = entry.getImportoTotalePagamenti(); 

		sb.append(
				Utils.getInstance().getMessageWithParamsFromResourceBundle(this.nomeServizio + ".label.sottotitolo",
						numeroPagamenti,(importoTotalePagamenti + "€")));

		return sb.toString();
	} 
	
	@Override
	public List<String> getValori(FrApplicazione entry, BasicBD bd) throws ConsoleException {
		return null;
	}

	@Override
	public String esporta(List<Long> idsToExport, UriInfo uriInfo, BasicBD bd, ZipOutputStream zout)
			throws WebApplicationException, ConsoleException {
		return null;
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
	public InfoForm getInfoModifica(UriInfo uriInfo, BasicBD bd, FrApplicazione entry) throws ConsoleException { return null; }

	@Override
	public void delete(List<Long> idsToDelete, UriInfo uriInfo, BasicBD bd) throws WebApplicationException, ConsoleException {	}

	@Override
	public FrApplicazione creaEntry(InputStream is, UriInfo uriInfo, BasicBD bd) throws WebApplicationException, ConsoleException { return null; }

	@Override
	public Dettaglio insert(InputStream is, UriInfo uriInfo, BasicBD bd) throws WebApplicationException, ConsoleException, ValidationException, DuplicatedEntryException { return null; }

	@Override
	public void checkEntry(FrApplicazione entry, FrApplicazione oldEntry) throws ValidationException { }

	@Override
	public Dettaglio update(InputStream is, UriInfo uriInfo, BasicBD bd) throws WebApplicationException, ConsoleException, ValidationException { return null; }
}
