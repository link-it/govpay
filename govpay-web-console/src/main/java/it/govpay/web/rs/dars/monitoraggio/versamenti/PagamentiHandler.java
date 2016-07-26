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
package it.govpay.web.rs.dars.monitoraggio.versamenti;

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
import it.govpay.bd.model.FrApplicazione;
import it.govpay.bd.model.Operatore;
import it.govpay.bd.model.Operatore.ProfiloOperatore;
import it.govpay.bd.model.Pagamento;
import it.govpay.bd.model.Pagamento.EsitoRendicontazione;
import it.govpay.bd.model.Pagamento.TipoAllegato;
import it.govpay.bd.model.RendicontazioneSenzaRpt;
import it.govpay.bd.model.Rpt;
import it.govpay.bd.model.Rr;
import it.govpay.bd.model.SingoloVersamento;
import it.govpay.bd.model.Versamento;
import it.govpay.bd.pagamento.FrBD;
import it.govpay.bd.pagamento.PagamentiBD;
import it.govpay.bd.pagamento.VersamentiBD;
import it.govpay.bd.pagamento.filters.FrApplicazioneFilter;
import it.govpay.bd.pagamento.filters.PagamentoFilter;
import it.govpay.bd.pagamento.filters.VersamentoFilter;
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
import it.govpay.web.rs.dars.monitoraggio.rendicontazioni.RendicontazioniSenzaRpt;
import it.govpay.web.rs.dars.monitoraggio.rendicontazioni.RendicontazioniSenzaRptHandler;
import it.govpay.web.utils.Utils;

public class PagamentiHandler extends BaseDarsHandler<Pagamento> implements IDarsHandler<Pagamento>{

	public static final String ANAGRAFICA_DEBITORE = "anagrafica";
	private SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");  

	public PagamentiHandler(Logger log, BaseDarsService darsService) { 
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
			List<Long> idApplicazioniOperatore = null; //operatore.getIdApplicazioni();
			List<Long> idUoOperatore = null;// operatore.getIdEnti();

			URI esportazione = null; 
			URI cancellazione = null;

			this.log.info("Esecuzione " + methodName + " in corso...");

			String versamentoId = Utils.getInstance().getMessageFromResourceBundle(this.nomeServizio + ".idVersamento.id");
			String idVersamento = this.getParameter(uriInfo, versamentoId, String.class);

			String idFrApplicazioneId = Utils.getInstance().getMessageFromResourceBundle(this.nomeServizio + ".idFrApplicazione.id");
			String idFrApplicazione = this.getParameter(uriInfo, idFrApplicazioneId, String.class);

			String idFrId = Utils.getInstance().getMessageFromResourceBundle(this.nomeServizio + ".idFr.id");
			String idFr= this.getParameter(uriInfo, idFrId, String.class);

			boolean eseguiRicerca = isAdmin;
			VersamentiBD versamentiBD = new VersamentiBD(bd);
			Versamento versamento = null;
			// Utilizzo i singoli versamenti per avere i pagamenti
			List<Long> idSingoliVersamenti = new ArrayList<Long>();

			PagamentiBD pagamentiBD = new PagamentiBD(bd);
			PagamentoFilter filter = pagamentiBD.newFilter();
			FilterSortWrapper fsw = new FilterSortWrapper();
			fsw.setField(it.govpay.orm.Pagamento.model().DATA_PAGAMENTO);
			fsw.setSortOrder(SortOrder.DESC);
			filter.getFilterSortList().add(fsw);

			long count = 0;

			// elemento correlato al versamento.
			if(StringUtils.isNotEmpty(idVersamento)){
				// SE l'operatore non e' admin vede solo i versamenti associati alle sue UO ed applicazioni
				// controllo se l'operatore ha fatto una richiesta di visualizzazione di un versamento che puo' vedere
				if(!isAdmin){
					eseguiRicerca = !Utils.isEmpty(idApplicazioniOperatore) || !Utils.isEmpty(idUoOperatore);

					VersamentoFilter versamentoFilter = versamentiBD.newFilter();
					versamentoFilter.setIdApplicazioni(idApplicazioniOperatore);
					versamentoFilter.setIdUo(idUoOperatore); 

					List<Long> idVersamentoL = new ArrayList<Long>();
					idVersamentoL.add(Long.parseLong(idVersamento));
					versamentoFilter.setIdVersamento(idVersamentoL);

					long countVersamento = eseguiRicerca ? versamentiBD.count(versamentoFilter) : 0;
					eseguiRicerca = eseguiRicerca && countVersamento > 0;

				}

				versamento = eseguiRicerca ? versamentiBD.getVersamento(Long.parseLong(idVersamento)) : null;
				if(versamento != null){
					List<SingoloVersamento> singoliVersamenti = versamento.getSingoliVersamenti(bd);
					if(singoliVersamenti != null && singoliVersamenti.size() >0){
						for (SingoloVersamento singoloVersamento : singoliVersamenti) {
							idSingoliVersamenti.add(singoloVersamento.getId());
						}
					}
				}
				eseguiRicerca = eseguiRicerca && idSingoliVersamenti.size() > 0;
				filter.setIdSingoliVersamenti(idSingoliVersamenti);
				count = eseguiRicerca ? pagamentiBD.count(filter) : 0;


			}

			long countRendicontazioniSenzaRpt = 0;
			List<Long> idFrApplicazioni = new ArrayList<Long>();
			if(StringUtils.isNotEmpty(idFrApplicazione)){
				if(!isAdmin){
					eseguiRicerca = !Utils.isEmpty(idApplicazioniOperatore);

					FrBD frBD = new FrBD(bd);
					FrApplicazioneFilter frApplicazioneFilter = frBD.newFrApplicazioneFilter();
					frApplicazioneFilter.setIdApplicazioni(idApplicazioniOperatore);
					frApplicazioneFilter.setIdFrApplicazione(Long.parseLong(idFrApplicazione));

					long countFrApplicazione = eseguiRicerca ? frBD.countFrApplicazione(frApplicazioneFilter) : 0;
					eseguiRicerca = eseguiRicerca && countFrApplicazione > 0;
				}

				// Eseguo la ricerca utilizzando l'idFrApplicazione per ricercare i pagamenti, pagamenti revocati e rndicontazioni senzwa RPT.
				if(eseguiRicerca){
					// Ricerca Pagamenti uso idFrApplicazione per filtrare
					idFrApplicazioni.add(Long.parseLong(idFrApplicazione));
					filter.setIdFrApplicazioneOrIdFrApplicazioneRevoca(idFrApplicazioni);
					count = eseguiRicerca ? pagamentiBD.count(filter) : 0;

					// Rendicontazioni 
					countRendicontazioniSenzaRpt = eseguiRicerca ? pagamentiBD.countRendicontazioniSenzaRpt(idFrApplicazioni) : 0;
					count += countRendicontazioniSenzaRpt;

				}
			}

			if(StringUtils.isNotEmpty(idFr)){
				FrBD frBD = new FrBD(bd);
				FrApplicazioneFilter frApplicazioneFilter = frBD.newFrApplicazioneFilter();
				List<Long> idFlussi = new ArrayList<Long>();
				idFlussi.add(Long.parseLong(idFr));
				frApplicazioneFilter.setIdFlussi(idFlussi);

				long countFrApplicazione = eseguiRicerca ? frBD.countFrApplicazione(frApplicazioneFilter) : 0;
				eseguiRicerca = eseguiRicerca && countFrApplicazione > 0;

				// Eseguo la ricerca utilizzando l'idFrApplicazione per ricercare i pagamenti, pagamenti revocati e rndicontazioni senzwa RPT.
				if(eseguiRicerca){
					//prendere gli id_fr_applicazione
					idFrApplicazioni = new ArrayList<Long>();
					List<FrApplicazione> findAllFrApplicazione = frBD.findAllFrApplicazione(frApplicazioneFilter);
					for (FrApplicazione frApplicazione : findAllFrApplicazione) {
						idFrApplicazioni.add(frApplicazione.getId());
					}

					// Ricerca Pagamenti uso idFrApplicazione per filtrare
					filter.setIdFrApplicazioneOrIdFrApplicazioneRevoca(idFrApplicazioni);
					count = eseguiRicerca ? pagamentiBD.count(filter) : 0;

					// Rendicontazioni 
					countRendicontazioniSenzaRpt = eseguiRicerca ? pagamentiBD.countRendicontazioniSenzaRpt(idFrApplicazioni) : 0;
					count += countRendicontazioniSenzaRpt;

				}
			}



			Elenco elenco = new Elenco(this.titoloServizio, this.getInfoRicerca(uriInfo, bd),this.getInfoCreazione(uriInfo, bd), count, esportazione, cancellazione); 

			UriBuilder uriDettaglioBuilder = BaseRsService.checkDarsURI(uriInfo).path(this.pathServizio).path("{id}");

			List<Pagamento> pagamenti = eseguiRicerca ? pagamentiBD.findAll(filter) : new ArrayList<Pagamento>();

			if(pagamenti != null && pagamenti.size() > 0){
				for (Pagamento entry : pagamenti) {
					elenco.getElenco().add(this.getElemento(entry, entry.getId(), uriDettaglioBuilder,bd));
				}
			}

			// tmp poi fare il merge
			if(countRendicontazioniSenzaRpt > 0){
				RendicontazioniSenzaRpt rendicontazioniSenzaRptDars = new RendicontazioniSenzaRpt();
				RendicontazioniSenzaRptHandler rendicontazioniSenzaRptDarsHandler = (RendicontazioniSenzaRptHandler) rendicontazioniSenzaRptDars.getDarsHandler();
				UriBuilder uriDettaglioRRBuilder = BaseRsService.checkDarsURI(uriInfo).path(rendicontazioniSenzaRptDars.getPathServizio()).path("{id}");

				List<RendicontazioneSenzaRpt> rendicontazioniSenzaRpt = eseguiRicerca ? pagamentiBD.getRendicontazioniSenzaRpt(idFrApplicazioni) : new ArrayList<RendicontazioneSenzaRpt>();

				if(rendicontazioniSenzaRpt != null && rendicontazioniSenzaRpt.size() > 0){
					for (RendicontazioneSenzaRpt entry : rendicontazioniSenzaRpt) {
						Elemento elemento = rendicontazioniSenzaRptDarsHandler.getElemento(entry, entry.getId(), uriDettaglioRRBuilder,bd);
						elenco.getElenco().add(elemento);

					}
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
			Pagamento pagamento = pagamentiBD.getPagamento(id);

			InfoForm infoModifica = null;
			URI cancellazione = null;
			URI esportazione = null; 

			String titolo = this.getTitolo(pagamento,bd);
			Dettaglio dettaglio = new Dettaglio(titolo, esportazione, cancellazione, infoModifica);

			// Sezione root coi dati del pagamento
			it.govpay.web.rs.dars.model.Sezione sezioneRoot = dettaglio.getSezioneRoot();




			SingoloVersamento singoloVersamento = pagamento.getSingoloVersamento(bd);
			if(singoloVersamento != null){
				SingoliVersamenti svDars = new SingoliVersamenti();
				SingoliVersamentiHandler svHandler = (SingoliVersamentiHandler) svDars.getDarsHandler();
				UriBuilder uriSVBuilder = BaseRsService.checkDarsURI(uriInfo).path(svDars.getPathServizio()).path("{id}");
				Elemento elemento = svHandler.getElemento(singoloVersamento, singoloVersamento.getId(), uriSVBuilder,bd); 
				sezioneRoot.addVoce(Utils.getInstance().getMessageFromResourceBundle(this.nomeServizio + ".singoloVersamento.label"),elemento.getTitolo());
			}
			if(StringUtils.isNotEmpty(pagamento.getCodSingoloVersamentoEnte()))
				sezioneRoot.addVoce(Utils.getInstance().getMessageFromResourceBundle(this.nomeServizio + ".codSingoloVersamentoEnte.label"),pagamento.getCodSingoloVersamentoEnte());

			if(pagamento.getImportoPagato() != null)
				sezioneRoot.addVoce(Utils.getInstance().getMessageFromResourceBundle(this.nomeServizio + ".importoPagato.label"),(pagamento.getImportoPagato().toString() + "€"));
			if(pagamento.getCommissioniPsp() != null)
				sezioneRoot.addVoce(Utils.getInstance().getMessageFromResourceBundle(this.nomeServizio + ".commissioniPsp.label"),(pagamento.getCommissioniPsp().toString() + "€"));
			if(StringUtils.isNotEmpty(pagamento.getIur()))
				sezioneRoot.addVoce(Utils.getInstance().getMessageFromResourceBundle(this.nomeServizio + ".iur.label"),pagamento.getIur()); 
			if(pagamento.getDataPagamento() != null)
				sezioneRoot.addVoce(Utils.getInstance().getMessageFromResourceBundle(this.nomeServizio + ".dataPagamento.label"),this.sdf.format(pagamento.getDataPagamento())); 
			TipoAllegato tipoAllegato = pagamento.getTipoAllegato();
			if(tipoAllegato!= null)
				sezioneRoot.addVoce(Utils.getInstance().getMessageFromResourceBundle(this.nomeServizio + ".tipoAllegato.label"),
						Utils.getInstance().getMessageFromResourceBundle(this.nomeServizio + ".tipoAllegato."+tipoAllegato.name()));

			//			Long idFrApplicazione = pagamento.getIdFrApplicazione();
			//			if(idFrApplicazione != null){
			//				ApplicazioniBD applicazioniBD = new ApplicazioniBD(bd);
			//				Applicazione applicazione = applicazioniBD.getApplicazione(idFrApplicazione);
			//				sezioneRoot.addVoce(Utils.getInstance().getMessageFromResourceBundle(this.nomeServizio + ".idFrApplicazione.label"),applicazione.getCodApplicazione());
			//			}
			EsitoRendicontazione esitoRendicontazione = pagamento.getEsitoRendicontazione();
			if(esitoRendicontazione != null)
				sezioneRoot.addVoce(Utils.getInstance().getMessageFromResourceBundle(this.nomeServizio + ".esitoRendicontazione.label"),
						Utils.getInstance().getMessageFromResourceBundle(this.nomeServizio + ".esitoRendicontazione."+esitoRendicontazione.name()));
			if(pagamento.getDataRendicontazione() != null)
				sezioneRoot.addVoce(Utils.getInstance().getMessageFromResourceBundle(this.nomeServizio + ".dataRendicontazione.label"),this.sdf.format(pagamento.getDataRendicontazione()));
			if(pagamento.getAnnoRiferimento()!= null)
				sezioneRoot.addVoce(Utils.getInstance().getMessageFromResourceBundle(this.nomeServizio + ".annoRiferimento.label"),pagamento.getAnnoRiferimento() + ""); 
			if(StringUtils.isNotEmpty(pagamento.getCodFlussoRendicontazione()))
				sezioneRoot.addVoce(Utils.getInstance().getMessageFromResourceBundle(this.nomeServizio + ".codFlussoRendicontazione.label"),pagamento.getCodFlussoRendicontazione());
			if(pagamento.getIndice() != null)
				sezioneRoot.addVoce(Utils.getInstance().getMessageFromResourceBundle(this.nomeServizio + ".indice.label"),pagamento.getIndice() + ""); 

			Rpt rpt = pagamento.getRpt(bd);
			if(rpt!= null){
				Transazioni transazioniDars = new Transazioni();
				TransazioniHandler transazioniDarsHandler = (TransazioniHandler) transazioniDars.getDarsHandler();
				UriBuilder uriRptBuilder = BaseRsService.checkDarsURI(uriInfo).path(transazioniDars.getPathServizio()).path("{id}");
				Elemento elemento = transazioniDarsHandler.getElemento(rpt, rpt.getId(), uriRptBuilder,bd);
				sezioneRoot.addVoce(Utils.getInstance().getMessageFromResourceBundle(this.nomeServizio + ".rpt.label"),elemento.getTitolo(),elemento.getUri());
			}

			if(pagamento.getIdRr() != null){
				String etichettaRevoca = Utils.getInstance().getMessageFromResourceBundle(this.nomeServizio + ".sezioneRevoca.titolo");
				it.govpay.web.rs.dars.model.Sezione sezioneRevoca = dettaglio.addSezione(etichettaRevoca);

				if(StringUtils.isNotEmpty(pagamento.getCausaleRevoca()))
					sezioneRevoca.addVoce(Utils.getInstance().getMessageFromResourceBundle(this.nomeServizio + ".causaleRevoca.label"),pagamento.getCausaleRevoca());
				if(StringUtils.isNotEmpty(pagamento.getDatiRevoca()))
					sezioneRevoca.addVoce(Utils.getInstance().getMessageFromResourceBundle(this.nomeServizio + ".datiRevoca.label"),pagamento.getDatiRevoca());
				if(StringUtils.isNotEmpty(pagamento.getEsitoRevoca()))
					sezioneRevoca.addVoce(Utils.getInstance().getMessageFromResourceBundle(this.nomeServizio + ".esitoRevoca.label"),pagamento.getEsitoRevoca());
				if(StringUtils.isNotEmpty(pagamento.getDatiEsitoRevoca()))
					sezioneRevoca.addVoce(Utils.getInstance().getMessageFromResourceBundle(this.nomeServizio + ".datiEsitoRevoca.label"),pagamento.getDatiEsitoRevoca());

				if(pagamento.getImportoRevocato() != null)
					sezioneRevoca.addVoce(Utils.getInstance().getMessageFromResourceBundle(this.nomeServizio + ".importoRevocato.label"),(pagamento.getImportoRevocato().toString() + "€"));

				//				Long idFrApplicazioneRevoca = pagamento.getIdFrApplicazioneRevoca();
				//				if(idFrApplicazioneRevoca != null){
				//					ApplicazioniBD applicazioniBD = new ApplicazioniBD(bd);
				//					Applicazione applicazione = applicazioniBD.getApplicazione(idFrApplicazione);
				//					sezioneRevoca.addVoce(Utils.getInstance().getMessageFromResourceBundle(this.nomeServizio + ".idFrApplicazioneRevoca.label"),applicazione.getCodApplicazione());
				//				}
				EsitoRendicontazione esitoRendicontazioneRevoca = pagamento.getEsitoRendicontazioneRevoca();
				if(esitoRendicontazioneRevoca != null)
					sezioneRevoca.addVoce(Utils.getInstance().getMessageFromResourceBundle(this.nomeServizio + ".esitoRendicontazioneRevoca.label"),
							Utils.getInstance().getMessageFromResourceBundle(this.nomeServizio + ".esitoRendicontazioneRevoca."+esitoRendicontazioneRevoca.name()));
				if(pagamento.getDataRendicontazioneRevoca() != null)
					sezioneRevoca.addVoce(Utils.getInstance().getMessageFromResourceBundle(this.nomeServizio + ".dataRendicontazioneRevoca.label"),this.sdf.format(pagamento.getDataRendicontazioneRevoca()));
				if(pagamento.getAnnoRiferimentoRevoca()!= null)
					sezioneRevoca.addVoce(Utils.getInstance().getMessageFromResourceBundle(this.nomeServizio + ".annoRiferimentoRevoca.label"),pagamento.getAnnoRiferimentoRevoca() + ""); 
				if(StringUtils.isNotEmpty(pagamento.getCodFlussoRendicontazioneRevoca()))
					sezioneRevoca.addVoce(Utils.getInstance().getMessageFromResourceBundle(this.nomeServizio + ".codFlussoRendicontazioneRevoca.label"),pagamento.getCodFlussoRendicontazioneRevoca());
				if(pagamento.getIndiceRevoca() != null)
					sezioneRevoca.addVoce(Utils.getInstance().getMessageFromResourceBundle(this.nomeServizio + ".indiceRevoca.label"),pagamento.getIndiceRevoca() + ""); 

				Rr rr = pagamento.getRr(bd);
				if(rr != null){
					Revoche revocheDars = new Revoche();
					RevocheHandler revocheDarsHandler = (RevocheHandler) revocheDars.getDarsHandler();
					UriBuilder uriDettaglioRRBuilder = BaseRsService.checkDarsURI(uriInfo).path(revocheDars.getPathServizio()).path("{id}");
					Elemento elemento = revocheDarsHandler.getElemento(rr, rr.getId(), uriDettaglioRRBuilder,bd);
					sezioneRevoca.addVoce(Utils.getInstance().getMessageFromResourceBundle(this.nomeServizio + ".rr.label"),elemento.getTitolo(),elemento.getUri());
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
	public String getTitolo(Pagamento entry,BasicBD bd) {
		Date dataPagamento = entry.getDataPagamento();
		BigDecimal importoPagato = entry.getImportoPagato();
		StringBuilder sb = new StringBuilder();

		String pagamentoString = 
				Utils.getInstance().getMessageWithParamsFromResourceBundle(this.nomeServizio + ".label.titolo", (importoPagato.toString() + "€") , this.sdf.format(dataPagamento)); 
		sb.append(pagamentoString);	
		return sb.toString();
	}

	@Override
	public String getSottotitolo(Pagamento entry,BasicBD bd) {
		StringBuilder sb = new StringBuilder();

		if(entry.getIdRr() != null){
			Date dataRevoca = entry.getDataPagamento();
			sb.append(Utils.getInstance().getMessageWithParamsFromResourceBundle(this.nomeServizio + ".label.sottotitolo.revocato", this.sdf.format(dataRevoca)));
		}

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
	public InfoForm getInfoModifica(UriInfo uriInfo, BasicBD bd, Pagamento entry) throws ConsoleException {	return null;	}

	@Override
	public Object getField(UriInfo uriInfo, List<RawParamValue> values, String fieldId, BasicBD bd)	throws WebApplicationException, ConsoleException {	return null;	}

	@Override
	public void delete(List<Long> idsToDelete, UriInfo uriInfo, BasicBD bd)	throws WebApplicationException, ConsoleException {}

	@Override
	public Pagamento creaEntry(InputStream is, UriInfo uriInfo, BasicBD bd) throws WebApplicationException, ConsoleException { return null;	}

	@Override
	public Dettaglio insert(InputStream is, UriInfo uriInfo, BasicBD bd) 	throws WebApplicationException, ConsoleException, ValidationException, DuplicatedEntryException {	return null;	}

	@Override
	public void checkEntry(Pagamento entry, Pagamento oldEntry) throws ValidationException {}

	@Override
	public Dettaglio update(InputStream is, UriInfo uriInfo, BasicBD bd) throws WebApplicationException, ConsoleException, ValidationException {		return null;	}
}
