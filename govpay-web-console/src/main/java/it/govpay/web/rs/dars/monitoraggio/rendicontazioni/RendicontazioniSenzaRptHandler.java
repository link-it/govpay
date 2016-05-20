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
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.zip.ZipOutputStream;

import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.UriBuilder;
import javax.ws.rs.core.UriInfo;

import org.apache.commons.lang.StringUtils;
import org.apache.logging.log4j.Logger;
import org.openspcoop2.generic_project.expression.SortOrder;

import it.govpay.bd.BasicBD;
import it.govpay.bd.FilterSortWrapper;
import it.govpay.bd.model.Iuv;
import it.govpay.bd.model.Operatore;
import it.govpay.bd.model.Operatore.ProfiloOperatore;
import it.govpay.bd.model.RendicontazioneSenzaRpt;
import it.govpay.bd.model.SingoloVersamento;
import it.govpay.bd.pagamento.PagamentiBD;
import it.govpay.bd.pagamento.filters.PagamentoFilter;
import it.govpay.web.rs.BaseRsService;
import it.govpay.web.rs.dars.BaseDarsHandler;
import it.govpay.web.rs.dars.BaseDarsService;
import it.govpay.web.rs.dars.IDarsHandler;
import it.govpay.web.rs.dars.exception.ConsoleException;
import it.govpay.web.rs.dars.exception.DuplicatedEntryException;
import it.govpay.web.rs.dars.exception.ValidationException;
import it.govpay.web.rs.dars.model.Dettaglio;
import it.govpay.web.rs.dars.model.Elemento;
import it.govpay.web.rs.dars.model.Elenco;
import it.govpay.web.rs.dars.model.InfoForm;
import it.govpay.web.rs.dars.model.RawParamValue;
import it.govpay.web.rs.dars.monitoraggio.versamenti.SingoliVersamenti;
import it.govpay.web.rs.dars.monitoraggio.versamenti.SingoliVersamentiHandler;
import it.govpay.web.utils.Utils;

public class RendicontazioniSenzaRptHandler extends BaseDarsHandler<RendicontazioneSenzaRpt> implements IDarsHandler<RendicontazioneSenzaRpt>{

	public static final String ANAGRAFICA_DEBITORE = "anagrafica";
	private SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");  

	public RendicontazioniSenzaRptHandler(Logger log, BaseDarsService darsService) { 
		super(log, darsService);
	}

	@Override
	public Elenco getElenco(UriInfo uriInfo, BasicBD bd) throws WebApplicationException, ConsoleException {
		String methodName = "getElenco " + this.titoloServizio;
		try{	
			// Operazione consentita agli utenti registrati
			Operatore operatore = this.darsService.getOperatoreByPrincipal(bd); 
			ProfiloOperatore profilo = operatore.getProfilo();
			boolean isAdmin = profilo.equals(ProfiloOperatore.ADMIN);
			List<Long> idApplicazioniOperatore = operatore.getIdApplicazioni();

			URI esportazione = null; 
			URI cancellazione = null;

			this.log.info("Esecuzione " + methodName + " in corso...");

			String idFrApplicazioneId = Utils.getInstance().getMessageFromResourceBundle(this.nomeServizio + ".idFrApplicazione.id");
			String idFrApplicazione = this.getParameter(uriInfo, idFrApplicazioneId, String.class);

			boolean eseguiRicerca = isAdmin;

			PagamentiBD pagamentiBD = new PagamentiBD(bd);
			PagamentoFilter filter = pagamentiBD.newFilter();
			FilterSortWrapper fsw = new FilterSortWrapper();
			fsw.setField(it.govpay.orm.Pagamento.model().DATA_PAGAMENTO);
			fsw.setSortOrder(SortOrder.DESC);
			filter.getFilterSortList().add(fsw);

			if(StringUtils.isNotEmpty(idFrApplicazione)){
				if(!isAdmin){
					eseguiRicerca = !Utils.isEmpty(idApplicazioniOperatore);

					if(eseguiRicerca){
						eseguiRicerca = eseguiRicerca && idApplicazioniOperatore.contains(Long.parseLong(idFrApplicazione));
					} 
				}
			}

			long count = eseguiRicerca ? pagamentiBD.countRendicontazioniSenzaRpt(Long.parseLong(idFrApplicazione)) : 0;			

			Elenco elenco = new Elenco(this.titoloServizio, this.getInfoRicerca(uriInfo, bd),this.getInfoCreazione(uriInfo, bd), count, esportazione, cancellazione); 

			UriBuilder uriDettaglioBuilder = BaseRsService.checkDarsURI(uriInfo).path(this.pathServizio).path("{id}");

			List<RendicontazioneSenzaRpt> pagamenti = eseguiRicerca ? pagamentiBD.getRendicontazioniSenzaRpt(Long.parseLong(idFrApplicazione)) : new ArrayList<RendicontazioneSenzaRpt>();

			if(pagamenti != null && pagamenti.size() > 0){
				for (RendicontazioneSenzaRpt entry : pagamenti) {
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
	public Dettaglio getDettaglio(long id, UriInfo uriInfo, BasicBD bd)
			throws WebApplicationException, ConsoleException {
		String methodName = "dettaglio " + this.titoloServizio + "."+ id;

		try{
			this.log.info("Esecuzione " + methodName + " in corso...");
			// Operazione consentita agli utenti registrati
			this.darsService.getOperatoreByPrincipal(bd); 

			PagamentiBD pagamentiBD = new PagamentiBD(bd);
			RendicontazioneSenzaRpt rendicontazioneSenzaRpt = pagamentiBD.getRendicontazioneSenzaRpt(id);

			InfoForm infoModifica = null;
			URI cancellazione = null;
			URI esportazione = null; 

			String titolo = this.getTitolo(rendicontazioneSenzaRpt,bd);
			Dettaglio dettaglio = new Dettaglio(titolo, esportazione, cancellazione, infoModifica);

			// Sezione root coi dati del pagamento
			it.govpay.web.rs.dars.model.Sezione sezioneRoot = dettaglio.getSezioneRoot();

			Iuv iuv = rendicontazioneSenzaRpt.getIuv(bd);
			if(iuv != null)
				sezioneRoot.addVoce(Utils.getInstance().getMessageFromResourceBundle(this.nomeServizio + ".iuv.label"),iuv.getIuv());
			if(StringUtils.isNotEmpty(rendicontazioneSenzaRpt.getIur()))
				sezioneRoot.addVoce(Utils.getInstance().getMessageFromResourceBundle(this.nomeServizio + ".iur.label"),rendicontazioneSenzaRpt.getIur());
			if(rendicontazioneSenzaRpt.getImportoPagato() != null)
				sezioneRoot.addVoce(Utils.getInstance().getMessageFromResourceBundle(this.nomeServizio + ".importoPagato.label"),(rendicontazioneSenzaRpt.getImportoPagato().toString() + "€"));
			if(rendicontazioneSenzaRpt.getDataRendicontazione() != null)
				sezioneRoot.addVoce(Utils.getInstance().getMessageFromResourceBundle(this.nomeServizio + ".dataRendicontazione.label"),this.sdf.format(rendicontazioneSenzaRpt.getDataRendicontazione()));

			SingoloVersamento singoloVersamento = rendicontazioneSenzaRpt.getSingoloVersamento(bd);
			if(singoloVersamento != null){
				SingoliVersamenti svDars = new SingoliVersamenti();
				SingoliVersamentiHandler svHandler = (SingoliVersamentiHandler) svDars.getDarsHandler();
				UriBuilder uriSVBuilder = BaseRsService.checkDarsURI(uriInfo).path(svDars.getPathServizio()).path("{id}");
				Elemento elemento = svHandler.getElemento(singoloVersamento, singoloVersamento.getId(), uriSVBuilder,bd); 
				sezioneRoot.addVoce(Utils.getInstance().getMessageFromResourceBundle(this.nomeServizio + ".singoloVersamento.label"),elemento.getTitolo());
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
	public String getTitolo(RendicontazioneSenzaRpt entry,BasicBD bd) {
		Date dataRendicontazione = entry.getDataRendicontazione();
		BigDecimal importoPagato = entry.getImportoPagato();
		StringBuilder sb = new StringBuilder();

		String pagamentoString = 
				Utils.getInstance().getMessageWithParamsFromResourceBundle(this.nomeServizio + ".label.titolo", (importoPagato.toString() + "€") , this.sdf.format(dataRendicontazione)); 
		sb.append(pagamentoString);	
		return sb.toString();
	}

	@Override
	public String getSottotitolo(RendicontazioneSenzaRpt entry,BasicBD bd) {
		StringBuilder sb = new StringBuilder();
		return sb.toString();
	}

	@Override
	public String esporta(List<Long> idsToExport, UriInfo uriInfo, BasicBD bd, ZipOutputStream zout)
			throws WebApplicationException, ConsoleException { return null;
	}
	@Override
	public String esporta(Long idToExport, UriInfo uriInfo, BasicBD bd, ZipOutputStream zout)	throws WebApplicationException, ConsoleException {
		return null;
	}
	/* Operazioni non consentite */

	@Override
	public InfoForm getInfoRicerca(UriInfo uriInfo, BasicBD bd) throws ConsoleException { 	return null;	}

	@Override
	public InfoForm getInfoCreazione(UriInfo uriInfo, BasicBD bd) throws ConsoleException {		return null;	}

	@Override
	public InfoForm getInfoModifica(UriInfo uriInfo, BasicBD bd, RendicontazioneSenzaRpt entry) throws ConsoleException {	return null;	}

	@Override
	public Object getField(UriInfo uriInfo, List<RawParamValue> values, String fieldId, BasicBD bd)	throws WebApplicationException, ConsoleException {	return null;	}

	@Override
	public void delete(List<Long> idsToDelete, UriInfo uriInfo, BasicBD bd)	throws WebApplicationException, ConsoleException {}

	@Override
	public RendicontazioneSenzaRpt creaEntry(InputStream is, UriInfo uriInfo, BasicBD bd) throws WebApplicationException, ConsoleException { return null;	}

	@Override
	public Dettaglio insert(InputStream is, UriInfo uriInfo, BasicBD bd) 	throws WebApplicationException, ConsoleException, ValidationException, DuplicatedEntryException {	return null;	}

	@Override
	public void checkEntry(RendicontazioneSenzaRpt entry, RendicontazioneSenzaRpt oldEntry) throws ValidationException {}

	@Override
	public Dettaglio update(InputStream is, UriInfo uriInfo, BasicBD bd) throws WebApplicationException, ConsoleException, ValidationException {		return null;	}
}
