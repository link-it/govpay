package it.govpay.web.rs.dars.monitoraggio.versamenti;

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
import javax.ws.rs.core.UriBuilder;
import javax.ws.rs.core.UriInfo;

import org.apache.commons.lang.StringUtils;
import org.apache.logging.log4j.Logger;
import org.jboss.resteasy.plugins.providers.multipart.MultipartFormDataInput;
import org.openspcoop2.generic_project.expression.SortOrder;

import it.govpay.bd.BasicBD;
import it.govpay.bd.FilterSortWrapper;
import it.govpay.bd.anagrafica.PortaliBD;
import it.govpay.bd.pagamento.RptBD;
import it.govpay.bd.pagamento.RrBD;
import it.govpay.bd.pagamento.VersamentiBD;
import it.govpay.bd.pagamento.filters.RptFilter;
import it.govpay.bd.pagamento.filters.RrFilter;
import it.govpay.bd.pagamento.filters.VersamentoFilter;
import it.govpay.bd.model.Canale;
import it.govpay.model.Intermediario;
import it.govpay.model.Operatore;
import it.govpay.model.Portale;
import it.govpay.bd.model.Psp;
import it.govpay.bd.model.Rpt;
import it.govpay.bd.model.Rr;
import it.govpay.bd.model.Stazione;
import it.govpay.model.Canale.ModelloPagamento;
import it.govpay.model.Operatore.ProfiloOperatore;
import it.govpay.model.Rpt.EsitoPagamento;
import it.govpay.model.Rpt.FirmaRichiesta;
import it.govpay.model.Rpt.StatoRpt;
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
import it.govpay.web.rs.dars.monitoraggio.eventi.Eventi;
import it.govpay.web.utils.Utils;

public class TransazioniHandler extends BaseDarsHandler<Rpt> implements IDarsHandler<Rpt>{

	private SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm"); 

	public TransazioniHandler(Logger log, BaseDarsService darsService) {
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

			URI esportazione = null;  
			URI cancellazione = null;

			this.log.info("Esecuzione " + methodName + " in corso...");

			Versamenti versamentiDars = new  Versamenti();
			String versamentoId = Utils.getInstance(this.getLanguage()).getMessageFromResourceBundle(versamentiDars.getNomeServizio() + ".idVersamento.id");
			String idVersamento = this.getParameter(uriInfo, versamentoId, String.class);

			Map<String, String> params = new HashMap<String, String>();
			
			if(StringUtils.isNotEmpty(idVersamento)){
				params.put(versamentoId, idVersamento);
			}
			
			boolean eseguiRicerca = true; // isAdmin;
			// SE l'operatore non e' admin vede solo i versamenti associati alle sue UO ed applicazioni
			// controllo se l'operatore ha fatto una richiesta di visualizzazione di un versamento che puo' vedere
			if(!isAdmin){
//				eseguiRicerca = !Utils.isEmpty(operatore.getIdApplicazioni()) || !Utils.isEmpty(operatore.getIdEnti());
				VersamentiBD versamentiBD = new VersamentiBD(bd);
				VersamentoFilter filter = versamentiBD.newFilter();
//				filter.setIdApplicazioni(operatore.getIdApplicazioni());
//				filter.setIdUo(operatore.getIdEnti()); 

				FilterSortWrapper fsw = new FilterSortWrapper();
				fsw.setField(it.govpay.orm.Versamento.model().DATA_CREAZIONE);
				fsw.setSortOrder(SortOrder.DESC);
				filter.getFilterSortList().add(fsw);

				long count = eseguiRicerca ? versamentiBD.count(filter) : 0;
				List<Long> idVersamentoL = new ArrayList<Long>();
				idVersamentoL.add(Long.parseLong(idVersamento));
				filter.setIdVersamento(idVersamentoL);

				eseguiRicerca = eseguiRicerca && count > 0;
			}

			RptBD rptBD = new RptBD(bd);
			RptFilter filter = rptBD.newFilter();
			FilterSortWrapper fsw = new FilterSortWrapper();
			fsw.setField(it.govpay.orm.RPT.model().DATA_MSG_RICHIESTA);
			fsw.setSortOrder(SortOrder.DESC);
			filter.getFilterSortList().add(fsw);
			filter.setIdVersamento(Long.parseLong(idVersamento)); 

			long count = eseguiRicerca ? rptBD.count(filter) : 0;

			Elenco elenco = new Elenco(this.titoloServizio, this.getInfoRicerca(uriInfo, bd,params),
					this.getInfoCreazione(uriInfo, bd),
					count, esportazione, cancellazione); 

			UriBuilder uriDettaglioBuilder = BaseRsService.checkDarsURI(uriInfo).path(this.pathServizio).path("{id}");

			List<Rpt> rpt = eseguiRicerca ? rptBD.findAll(filter) : new ArrayList<Rpt>();

			RrBD rrBD = new RrBD(bd);
			FilterSortWrapper rrFsw = new FilterSortWrapper();
			rrFsw.setField(it.govpay.orm.RR.model().DATA_MSG_REVOCA);
			rrFsw.setSortOrder(SortOrder.DESC);

			Revoche revocheDars = new Revoche();
			RevocheHandler revocheDarsHandler = (RevocheHandler) revocheDars.getDarsHandler();
			UriBuilder uriDettaglioRRBuilder = BaseRsService.checkDarsURI(uriInfo).path(revocheDars.getPathServizio()).path("{id}");

			if(rpt != null && rpt.size() > 0){
				for (Rpt entry : rpt) {
					elenco.getElenco().add(this.getElemento(entry, entry.getId(), uriDettaglioBuilder,bd));

					RrFilter rrFilter = rrBD.newFilter();
					rrFilter.getFilterSortList().add(rrFsw);
					rrFilter.setIdRpt(entry.getId()); 
					List<Rr> findAll = rrBD.findAll(rrFilter);
					if(findAll != null && findAll.size() > 0){
						for (Rr rr : findAll) {
							Elemento elemento = revocheDarsHandler.getElemento(rr, rr.getId(), uriDettaglioRRBuilder,bd);
							elenco.getElenco().add(elemento);
						}
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

			RptBD rptBD = new RptBD(bd);
			Rpt rpt = rptBD.getRpt(id);

			InfoForm infoModifica = null;
			URI cancellazione = null;
			URI esportazione = this.getUriEsportazioneDettaglio(uriInfo, rptBD, id);

			String titolo = this.getTitolo(rpt,bd);
			Dettaglio dettaglio = new Dettaglio(titolo, esportazione, cancellazione, infoModifica);

			// Sezione Rpt
			it.govpay.web.rs.dars.model.Sezione sezioneRpt = dettaglio.getSezioneRoot();
			String etichettaRpt = Utils.getInstance(this.getLanguage()).getMessageFromResourceBundle(this.nomeServizio + ".sezioneRPT.titolo");
			sezioneRpt.setEtichetta(etichettaRpt); 

			StatoRpt stato = rpt.getStato(); 
			sezioneRpt.addVoce(Utils.getInstance(this.getLanguage()).getMessageFromResourceBundle(this.nomeServizio + ".stato.label"),
					Utils.getInstance(this.getLanguage()).getMessageFromResourceBundle(this.nomeServizio + ".stato." + stato.name()));
			if(StringUtils.isNotEmpty(rpt.getDescrizioneStato()))
				sezioneRpt.addVoce(Utils.getInstance(this.getLanguage()).getMessageFromResourceBundle(this.nomeServizio + ".descrizioneStato.label"),rpt.getDescrizioneStato());
			sezioneRpt.addVoce(Utils.getInstance(this.getLanguage()).getMessageFromResourceBundle(this.nomeServizio + ".iuv.label"),rpt.getIuv());
			sezioneRpt.addVoce(Utils.getInstance(this.getLanguage()).getMessageFromResourceBundle(this.nomeServizio + ".ccp.label"),rpt.getCcp());
			sezioneRpt.addVoce(Utils.getInstance(this.getLanguage()).getMessageFromResourceBundle(this.nomeServizio + ".codMsgRichiesta.label"),rpt.getCodMsgRichiesta());
			if(rpt.getDataMsgRicevuta() != null)
				sezioneRpt.addVoce(Utils.getInstance(this.getLanguage()).getMessageFromResourceBundle(this.nomeServizio + ".dataMsgRichiesta.label"),this.sdf.format(rpt.getDataMsgRichiesta()));
			if(rpt.getDataAggiornamento() != null)
				sezioneRpt.addVoce(Utils.getInstance(this.getLanguage()).getMessageFromResourceBundle(this.nomeServizio + ".dataAggiornamento.label"),this.sdf.format(rpt.getDataAggiornamento()));
			sezioneRpt.addVoce(Utils.getInstance(this.getLanguage()).getMessageFromResourceBundle(this.nomeServizio + ".dominio.label"),rpt.getCodDominio());
			Intermediario intermediario = rpt.getIntermediario(bd);
			if(intermediario != null)
				sezioneRpt.addVoce(Utils.getInstance(this.getLanguage()).getMessageFromResourceBundle(this.nomeServizio + ".intermediario.label"),intermediario.getCodIntermediario());

			Stazione stazione = rpt.getStazione(bd);
			if(stazione!= null)
				sezioneRpt.addVoce(Utils.getInstance(this.getLanguage()).getMessageFromResourceBundle(this.nomeServizio + ".stazione.label"),	stazione.getCodStazione());

			Psp psp = rpt.getPsp(bd);
			if(psp != null)
				sezioneRpt.addVoce(Utils.getInstance(this.getLanguage()).getMessageFromResourceBundle(this.nomeServizio + ".psp.label"),psp.getCodPsp());

			Canale canale = rpt.getCanale(bd);
			if(canale != null)
				sezioneRpt.addVoce(Utils.getInstance(this.getLanguage()).getMessageFromResourceBundle(this.nomeServizio + ".canale.label"),canale.getCodCanale());

			Long idPortale = rpt.getIdPortale();
			if(idPortale != null){
				PortaliBD portaliBD = new PortaliBD(bd);
				Portale portale = portaliBD.getPortale(idPortale);
				sezioneRpt.addVoce(Utils.getInstance(this.getLanguage()).getMessageFromResourceBundle(this.nomeServizio + ".portale.label"),	portale.getCodPortale());
			}


			ModelloPagamento modelloPagamento = rpt.getModelloPagamento();
			if(modelloPagamento != null){
				String modelloPagamentoString = null;
				switch (modelloPagamento) {
				case ATTIVATO_PRESSO_PSP:
					modelloPagamentoString = Utils.getInstance(this.getLanguage()).getMessageFromResourceBundle(this.nomeServizio + ".modelloPagamento.ATTIVATO_PRESSO_PSP");
					break;
				case DIFFERITO:
					modelloPagamentoString = Utils.getInstance(this.getLanguage()).getMessageFromResourceBundle(this.nomeServizio + ".modelloPagamento.DIFFERITO");
					break;
				case IMMEDIATO:
					modelloPagamentoString = Utils.getInstance(this.getLanguage()).getMessageFromResourceBundle(this.nomeServizio + ".modelloPagamento.IMMEDIATO");
					break;
				case IMMEDIATO_MULTIBENEFICIARIO:
				default:
					modelloPagamentoString = Utils.getInstance(this.getLanguage()).getMessageFromResourceBundle(this.nomeServizio + ".modelloPagamento.IMMEDIATO_MULTIBENEFICIARIO");
					break;
				}

				sezioneRpt.addVoce(Utils.getInstance(this.getLanguage()).getMessageFromResourceBundle(this.nomeServizio + ".modelloPagamento.label"),modelloPagamentoString);
			}
			FirmaRichiesta firmaRichiesta = rpt.getFirmaRichiesta();
			if(firmaRichiesta != null){
				String firmaRichiestaAsString = null;

				switch (firmaRichiesta) {
				case AVANZATA:
					firmaRichiestaAsString = Utils.getInstance(this.getLanguage()).getMessageFromResourceBundle(this.nomeServizio + ".firmaRichiesta.avanzata");
					break;
				case CA_DES: 
					firmaRichiestaAsString = Utils.getInstance(this.getLanguage()).getMessageFromResourceBundle(this.nomeServizio + ".firmaRichiesta.ca_des");
					break;
				case XA_DES :
					firmaRichiestaAsString = Utils.getInstance(this.getLanguage()).getMessageFromResourceBundle(this.nomeServizio + ".firmaRichiesta.xa_des");
					break;
				case NESSUNA: 
				default:
					firmaRichiestaAsString = Utils.getInstance(this.getLanguage()).getMessageFromResourceBundle(this.nomeServizio + ".firmaRichiesta.nessuna");
					break;
				}

				sezioneRpt.addVoce(Utils.getInstance(this.getLanguage()).getMessageFromResourceBundle(this.nomeServizio + ".firmaRichiesta.label"),firmaRichiestaAsString);
			}

			if(StringUtils.isNotEmpty(rpt.getCodCarrello()))
				sezioneRpt.addVoce(Utils.getInstance(this.getLanguage()).getMessageFromResourceBundle(this.nomeServizio + ".codCarrello.label"),rpt.getCodCarrello());
			if(StringUtils.isNotEmpty(rpt.getCodSessione()))
				sezioneRpt.addVoce(Utils.getInstance(this.getLanguage()).getMessageFromResourceBundle(this.nomeServizio + ".codSessione.label"),rpt.getCodSessione());
	

			// Singoli Rt 
			String etichettaRt = Utils.getInstance(this.getLanguage()).getMessageFromResourceBundle(this.nomeServizio + ".sezioneRT.titolo");
			it.govpay.web.rs.dars.model.Sezione sezioneRt = dettaglio.addSezione(etichettaRt);
			if(rpt.getDataMsgRicevuta()!= null){
				sezioneRt.addVoce(Utils.getInstance(this.getLanguage()).getMessageFromResourceBundle(this.nomeServizio + ".dataMsgRicevuta.label"), this.sdf.format(rpt.getDataMsgRicevuta()));

				EsitoPagamento esitoPagamento = rpt.getEsitoPagamento();

				switch (esitoPagamento) {		
				case DECORRENZA_TERMINI:
					sezioneRt.addVoce(Utils.getInstance(this.getLanguage()).getMessageFromResourceBundle(this.nomeServizio + ".esitoPagamento.label"),
							Utils.getInstance(this.getLanguage()).getMessageFromResourceBundle(this.nomeServizio + ".esitoPagamento.DECORRENZA_TERMINI")); 
					break;
				case DECORRENZA_TERMINI_PARZIALE:
					sezioneRt.addVoce(Utils.getInstance(this.getLanguage()).getMessageFromResourceBundle(this.nomeServizio + ".esitoPagamento.label"),
							Utils.getInstance(this.getLanguage()).getMessageFromResourceBundle(this.nomeServizio + ".esitoPagamento.DECORRENZA_TERMINI_PARZIALE"));
					break;
				case PAGAMENTO_ESEGUITO:
					sezioneRt.addVoce(Utils.getInstance(this.getLanguage()).getMessageFromResourceBundle(this.nomeServizio + ".esitoPagamento.label"),
							Utils.getInstance(this.getLanguage()).getMessageFromResourceBundle(this.nomeServizio + ".esitoPagamento.PAGAMENTO_ESEGUITO"));
					break;
				case PAGAMENTO_NON_ESEGUITO:
					sezioneRt.addVoce(Utils.getInstance(this.getLanguage()).getMessageFromResourceBundle(this.nomeServizio + ".esitoPagamento.label"),
							Utils.getInstance(this.getLanguage()).getMessageFromResourceBundle(this.nomeServizio + ".esitoPagamento.PAGAMENTO_NON_ESEGUITO"));
					break;
				case PAGAMENTO_PARZIALMENTE_ESEGUITO:
				default:
					sezioneRt.addVoce(Utils.getInstance(this.getLanguage()).getMessageFromResourceBundle(this.nomeServizio + ".esitoPagamento.label"),
							Utils.getInstance(this.getLanguage()).getMessageFromResourceBundle(this.nomeServizio + ".esitoPagamento.PAGAMENTO_PARZIALMENTE_ESEGUITO"));
					break;
				}

				sezioneRt.addVoce(Utils.getInstance(this.getLanguage()).getMessageFromResourceBundle(this.nomeServizio + ".codMsgRicevuta.label"), rpt.getCodMsgRicevuta());
				sezioneRt.addVoce(Utils.getInstance(this.getLanguage()).getMessageFromResourceBundle(this.nomeServizio + ".importoTotalePagato.label"), rpt.getImportoTotalePagato() + "€");
			}
			else	{
				sezioneRt.addVoce(Utils.getInstance(this.getLanguage()).getMessageFromResourceBundle(this.nomeServizio + ".rtAssente"), null);
			}


			//Eventi correlati
			// Elementi correlati
			String etichettaEventi = Utils.getInstance(this.getLanguage()).getMessageFromResourceBundle(this.nomeServizio + ".elementoCorrelato.eventi.titolo");
			Eventi eventiDars = new Eventi();
			
			String codDominioId = Utils.getInstance(this.getLanguage()).getMessageFromResourceBundle(eventiDars.getNomeServizio() + ".codDominio.id");
			String iuvId = Utils.getInstance(this.getLanguage()).getMessageFromResourceBundle(eventiDars.getNomeServizio() + ".iuv.id");
			String ccpId = Utils.getInstance(this.getLanguage()).getMessageFromResourceBundle(eventiDars.getNomeServizio() + ".ccp.id");
			String idTransazioneId = Utils.getInstance(this.getLanguage()).getMessageFromResourceBundle(eventiDars.getNomeServizio() + ".idTransazione.id");

		
			UriBuilder uriBuilder = BaseRsService.checkDarsURI(uriInfo).path(eventiDars.getPathServizio())
					.queryParam(codDominioId, rpt.getCodDominio())
					.queryParam(iuvId, rpt.getIuv())
					.queryParam(ccpId, rpt.getCcp())
					.queryParam(idTransazioneId, rpt.getId());
			
			dettaglio.addElementoCorrelato(etichettaEventi, uriBuilder.build());

			this.log.info("Esecuzione " + methodName + " completata.");

			return dettaglio;
		}catch(WebApplicationException e){
			throw e;
		}catch(Exception e){
			throw new ConsoleException(e);
		}
	}

	@Override
	public String getTitolo(Rpt entry,BasicBD bd) {
		Date dataMsgRichiesta = entry.getDataMsgRichiesta();
		String iuv = entry.getIuv();
		String ccp = entry.getCcp();
		StringBuilder sb = new StringBuilder();

		String statoString = Utils.getInstance(this.getLanguage()).getMessageWithParamsFromResourceBundle(this.nomeServizio + ".label.titolo", iuv , ccp, this.sdf.format(dataMsgRichiesta)); 
		sb.append(statoString);	
		return sb.toString();
	}

	@Override
	public String getSottotitolo(Rpt entry,BasicBD bd) {
		StringBuilder sb = new StringBuilder();
		// ricevuta RT
		if(entry.getDataMsgRicevuta()!= null){
			EsitoPagamento esitoPagamento = entry.getEsitoPagamento();
			String esitoPagamentoString = null;
			switch (esitoPagamento) {		
			case DECORRENZA_TERMINI:
				esitoPagamentoString = Utils.getInstance(this.getLanguage()).getMessageFromResourceBundle(this.nomeServizio + ".esitoPagamento.DECORRENZA_TERMINI"); 
				break;
			case DECORRENZA_TERMINI_PARZIALE:
				esitoPagamentoString = Utils.getInstance(this.getLanguage()).getMessageFromResourceBundle(this.nomeServizio + ".esitoPagamento.DECORRENZA_TERMINI_PARZIALE");
				break;
			case PAGAMENTO_ESEGUITO:
				esitoPagamentoString = Utils.getInstance(this.getLanguage()).getMessageFromResourceBundle(this.nomeServizio + ".esitoPagamento.PAGAMENTO_ESEGUITO");
				break;
			case PAGAMENTO_NON_ESEGUITO:
				esitoPagamentoString = Utils.getInstance(this.getLanguage()).getMessageFromResourceBundle(this.nomeServizio + ".esitoPagamento.PAGAMENTO_NON_ESEGUITO");
				break;
			case PAGAMENTO_PARZIALMENTE_ESEGUITO:
			default:
				esitoPagamentoString = Utils.getInstance(this.getLanguage()).getMessageFromResourceBundle(this.nomeServizio + ".esitoPagamento.PAGAMENTO_PARZIALMENTE_ESEGUITO");
				break;
			}


			BigDecimal importoTotalePagato = entry.getImportoTotalePagato();
			int compareTo = importoTotalePagato.compareTo(BigDecimal.ZERO);
			if(compareTo > 0){
				sb.append(Utils.getInstance(this.getLanguage()).getMessageWithParamsFromResourceBundle(this.nomeServizio + ".label.sottotitolo.rtPresente.importoPositivo",esitoPagamentoString, ( entry.getImportoTotalePagato() + "€")));
			} else{
				sb.append(Utils.getInstance(this.getLanguage()).getMessageWithParamsFromResourceBundle(this.nomeServizio + ".label.sottotitolo.rtPresente",esitoPagamentoString));
			}

		} else {
			StatoRpt stato = entry.getStato();
			sb.append(Utils.getInstance(this.getLanguage()).getMessageWithParamsFromResourceBundle(this.nomeServizio + ".label.sottotitolo.rtAssente", Utils.getInstance(this.getLanguage()).getMessageFromResourceBundle(this.nomeServizio + ".stato." + stato.name())));
		}

		return sb.toString();
	}
	
	@Override
	public List<String> getValori(Rpt entry, BasicBD bd) throws ConsoleException {
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

		String fileName = "Transazioni.zip";
		try{
			this.log.info("Esecuzione " + methodName + " in corso...");
			this.darsService.getOperatoreByPrincipal(bd); 

			RptBD rptBD = new RptBD(bd);

			for (Long idTransazione : idsToExport) {
				Rpt rpt = rptBD.getRpt(idTransazione);
				String folderName = rpt.getCodMsgRichiesta();

				ZipEntry rptXml = new ZipEntry(folderName + "/rpt.xml");
				zout.putNextEntry(rptXml);
				zout.write(rpt.getXmlRpt());
				zout.closeEntry();

				if(rpt.getXmlRt() != null){
					ZipEntry rtXml = new ZipEntry(folderName + "/rt.xml");
					zout.putNextEntry(rtXml);
					zout.write(rpt.getXmlRt());
					zout.closeEntry();
				}
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
	public String esporta(Long idToExport, UriInfo uriInfo, BasicBD bd, ZipOutputStream zout)	throws WebApplicationException, ConsoleException {
		String methodName = "esporta " + this.titoloServizio + "[" + idToExport + "]";  


		try{
			this.log.info("Esecuzione " + methodName + " in corso...");
			this.darsService.getOperatoreByPrincipal(bd); 

			RptBD rptBD = new RptBD(bd);
			Rpt rpt = rptBD.getRpt(idToExport);

			String fileName = "Transazione_"+rpt.getCodMsgRichiesta()+".zip";

			ZipEntry rptXml = new ZipEntry("rpt.xml");
			zout.putNextEntry(rptXml);
			zout.write(rpt.getXmlRpt());
			zout.closeEntry();

			if(rpt.getXmlRt() != null){
				ZipEntry rtXml = new ZipEntry("rt.xml");
				zout.putNextEntry(rtXml);
				zout.write(rpt.getXmlRt());
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
	public InfoForm getInfoRicerca(UriInfo uriInfo, BasicBD bd, boolean visualizzaRicerca, Map<String,String> parameters) throws ConsoleException { 	
		URI ricerca =  this.getUriRicerca(uriInfo, bd, parameters);
		InfoForm formRicerca = new InfoForm(ricerca);
		return formRicerca;
	}
	
	/* Operazioni non consentite */
	@Override
	public InfoForm getInfoCreazione(UriInfo uriInfo, BasicBD bd) throws ConsoleException {		return null;	}

	@Override
	public InfoForm getInfoModifica(UriInfo uriInfo, BasicBD bd, Rpt entry) throws ConsoleException {	return null;	}

	@Override
	public Object getField(UriInfo uriInfo, List<RawParamValue> values, String fieldId, BasicBD bd)	throws WebApplicationException, ConsoleException {	return null;	}

	@Override
	public void delete(List<Long> idsToDelete, UriInfo uriInfo, BasicBD bd)	throws WebApplicationException, ConsoleException {}

	@Override
	public Rpt creaEntry(InputStream is, UriInfo uriInfo, BasicBD bd) throws WebApplicationException, ConsoleException { return null;	}

	@Override
	public Dettaglio insert(InputStream is, UriInfo uriInfo, BasicBD bd) 	throws WebApplicationException, ConsoleException, ValidationException, DuplicatedEntryException {	return null;	}

	@Override
	public void checkEntry(Rpt entry, Rpt oldEntry) throws ValidationException {}

	@Override
	public Dettaglio update(InputStream is, UriInfo uriInfo, BasicBD bd) throws WebApplicationException, ConsoleException, ValidationException {		return null;	}

	@Override
	public Object uplaod(MultipartFormDataInput input, UriInfo uriInfo, BasicBD bd)	throws WebApplicationException, ConsoleException, ValidationException { return null;}

}
