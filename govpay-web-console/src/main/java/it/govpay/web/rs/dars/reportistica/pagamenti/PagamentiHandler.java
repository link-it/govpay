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
package it.govpay.web.rs.dars.reportistica.pagamenti;

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
import javax.ws.rs.core.UriBuilder;
import javax.ws.rs.core.UriInfo;

import org.apache.commons.lang.StringUtils;
import org.apache.logging.log4j.Logger;
import org.jboss.resteasy.plugins.providers.multipart.MultipartFormDataInput;
import org.openspcoop2.generic_project.exception.ServiceException;
import org.openspcoop2.generic_project.expression.SortOrder;
import org.openspcoop2.utils.csv.Printer;

import it.govpay.bd.BasicBD;
import it.govpay.bd.FilterSortWrapper;
import it.govpay.bd.anagrafica.AclBD;
import it.govpay.bd.anagrafica.AnagraficaManager;
import it.govpay.bd.anagrafica.DominiBD;
import it.govpay.bd.anagrafica.filters.DominioFilter;
import it.govpay.bd.model.Dominio;
import it.govpay.bd.model.SingoloVersamento;
import it.govpay.bd.model.UnitaOperativa;
import it.govpay.bd.model.Versamento;
import it.govpay.bd.pagamento.SingoliVersamentiBD;
import it.govpay.bd.reportistica.EstrattiContoBD;
import it.govpay.bd.reportistica.filters.EstrattoContoFilter;
import it.govpay.core.utils.CSVUtils;
import it.govpay.model.Acl;
import it.govpay.model.Acl.Tipo;
import it.govpay.model.EstrattoConto;
import it.govpay.model.Operatore;
import it.govpay.model.Operatore.ProfiloOperatore;
import it.govpay.model.Versamento.StatoVersamento;
import it.govpay.web.rs.BaseRsService;
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
import it.govpay.web.rs.dars.model.input.base.InputDate;
import it.govpay.web.rs.dars.model.input.base.SelectList;
import it.govpay.web.rs.dars.monitoraggio.versamenti.Versamenti;
import it.govpay.web.utils.ConsoleProperties;
import it.govpay.web.utils.Utils;

public class PagamentiHandler extends BaseDarsHandler<EstrattoConto> implements IDarsHandler<EstrattoConto>{

	public static final String ANAGRAFICA_DEBITORE = "anagrafica";
	private static Map<String, ParamField<?>> infoRicercaMap = null;
	private SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm");  

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


			Integer offset = this.getOffset(uriInfo);
			Integer limit = this.getLimit(uriInfo);
			URI esportazione = this.getUriEsportazione(uriInfo, bd); 
			URI cancellazione = null;

			this.log.info("Esecuzione " + methodName + " in corso..."); 

			AclBD aclBD = new AclBD(bd);
			List<Acl> aclOperatore = aclBD.getAclOperatore(operatore.getId());
			List<Long> idDomini = new ArrayList<Long>();

			EstrattiContoBD pagamentiBD = new EstrattiContoBD(bd);
			EstrattoContoFilter filter = pagamentiBD.newFilter();
			filter.setOffset(offset);
			filter.setLimit(limit);
			//			FilterSortWrapper fsw = new FilterSortWrapper();
			//			fsw.setField(filter.getDataPagamentoAliasField());
			//			fsw.setSortOrder(SortOrder.DESC);
			//			filter.getFilterSortList().add(fsw);

			String idDominioId = Utils.getInstance().getMessageFromResourceBundle(this.nomeServizio + ".idDominio.id");
			String idDominio = this.getParameter(uriInfo, idDominioId, String.class);
			if(StringUtils.isNotEmpty(idDominio)){
				long idDom = -1l;
				try{
					idDom = Long.parseLong(idDominio);
				}catch(Exception e){ idDom = -1l;	}
				if(idDom > 0){
					idDomini.add(idDom);
					filter.setIdDomini(idDomini);
				}
			}

			String statoVersamentoId = Utils.getInstance().getMessageFromResourceBundle(this.nomeServizio + ".statoVersamento.id");
			String statoVersamento = this.getParameter(uriInfo, statoVersamentoId, String.class);
			if(StringUtils.isNotEmpty(statoVersamento)){
				filter.setStatoVersamento(statoVersamento);
			}


			String dataInizioId = Utils.getInstance().getMessageFromResourceBundle(this.nomeServizio + ".dataInizio.id");

			String dataInizio = this.getParameter(uriInfo, dataInizioId, String.class);
			if(StringUtils.isNotEmpty(dataInizio)){
				filter.setDataInizio(this.convertJsonStringToDate(dataInizio));
			}

			String dataFineId = Utils.getInstance().getMessageFromResourceBundle(this.nomeServizio + ".dataFine.id");
			String dataFine = this.getParameter(uriInfo, dataFineId, String.class);
			if(StringUtils.isNotEmpty(dataFine)){
				filter.setDataFine(this.convertJsonStringToDate(dataFine));
			}

			boolean eseguiRicerca = true; // isAdmin;
			// SE l'operatore non e' admin vede solo i versamenti associati ai domini definiti nelle ACL
			if(!isAdmin && idDomini.isEmpty()){
				boolean vediTuttiDomini = false;

				for(Acl acl: aclOperatore) {
					if(Tipo.DOMINIO.equals(acl.getTipo())) {
						if(acl.getIdDominio() == null) {
							vediTuttiDomini = true;
							break;
						} else {
							idDomini.add(acl.getIdDominio());
						}
					}
				}
				if(!vediTuttiDomini) {
					if(idDomini.isEmpty()) {
						eseguiRicerca = false;
					} else {
						filter.setIdDomini(idDomini);
					}
				}
			}

			long count = eseguiRicerca ? pagamentiBD.count(filter) : 0;
			InfoForm infoRicerca = this.getInfoRicerca(uriInfo, bd);

			/*

    Cod versamento
    IUV
    C.F. Debitore
    Importo dovuto
    Importo pagato
    Data pagamento
    Stato

			 * */ 

			List<String> valori = new ArrayList<String>();
			valori.add(Utils.getInstance().getMessageFromResourceBundle(this.nomeServizio + ".codVersamentoEnte.label"));
			valori.add(Utils.getInstance().getMessageFromResourceBundle(this.nomeServizio + ".iuv.label"));
			valori.add(Utils.getInstance().getMessageFromResourceBundle(this.nomeServizio + ".codiceFiscaleDebitore.label"));
			valori.add(Utils.getInstance().getMessageFromResourceBundle(this.nomeServizio + ".importoDovuto.label"));
			valori.add(Utils.getInstance().getMessageFromResourceBundle(this.nomeServizio + ".importoPagato.label"));
			valori.add(Utils.getInstance().getMessageFromResourceBundle(this.nomeServizio + ".dataPagamento.label"));
			valori.add(Utils.getInstance().getMessageFromResourceBundle(this.nomeServizio + ".statoVersamento.label"));

			Elemento intestazione = new Elemento(-1, valori , null);


			Elenco elenco = new Elenco(this.titoloServizio, infoRicerca,
					this.getInfoCreazione(uriInfo, bd),
					count, esportazione, cancellazione,true,intestazione ); 

			UriBuilder uriDettaglioBuilder = BaseRsService.checkDarsURI(uriInfo).path(this.pathServizio).path("{id}");

			List<EstrattoConto> findAll = eseguiRicerca ? pagamentiBD.findAll(filter) : new ArrayList<EstrattoConto>(); 

			if(findAll != null && findAll.size() > 0){
				for (EstrattoConto entry : findAll) {
					elenco.getElenco().add(this.getElemento(entry, entry.getIdSingoloVersamento(), uriDettaglioBuilder,bd));
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

		String idDominioId = Utils.getInstance().getMessageFromResourceBundle(this.nomeServizio + ".idDominio.id");
		String statoVersamentoId = Utils.getInstance().getMessageFromResourceBundle(this.nomeServizio + ".statoVersamento.id");
		String dataInizioId = Utils.getInstance().getMessageFromResourceBundle(this.nomeServizio + ".dataInizio.id");
		String dataFineId = Utils.getInstance().getMessageFromResourceBundle(this.nomeServizio + ".dataFine.id");

		if(infoRicercaMap == null){
			this.initInfoRicerca(uriInfo, bd);
		}

		Sezione sezioneRoot = infoRicerca.getSezioneRoot();

		try{

			Operatore operatore = this.darsService.getOperatoreByPrincipal(bd); 
			ProfiloOperatore profilo = operatore.getProfilo();
			boolean isAdmin = profilo.equals(ProfiloOperatore.ADMIN);

			// idDominio
			List<Voce<Long>> domini = new ArrayList<Voce<Long>>();

			DominiBD dominiBD = new DominiBD(bd);
			DominioFilter filter;
			try {
				filter = dominiBD.newFilter();
				boolean eseguiRicerca = true;
				if(isAdmin){

				} else {
					AclBD aclBD = new AclBD(bd);
					List<Acl> aclOperatore = aclBD.getAclOperatore(operatore.getId());

					boolean vediTuttiDomini = false;
					List<Long> idDomini = new ArrayList<Long>();
					for(Acl acl: aclOperatore) {
						if(Tipo.DOMINIO.equals(acl.getTipo())) {
							if(acl.getIdDominio() == null) {
								vediTuttiDomini = true;
								break;
							} else {
								idDomini.add(acl.getIdDominio());
							}
						}
					}
					if(!vediTuttiDomini) {
						if(idDomini.isEmpty()) {
							eseguiRicerca = false;
						} else {
							filter.setIdDomini(idDomini);
						}
					}
				}

				if(eseguiRicerca) {
					domini.add(new Voce<Long>(Utils.getInstance().getMessageFromResourceBundle("commons.label.qualsiasi"), -1L));
					FilterSortWrapper fsw = new FilterSortWrapper();
					fsw.setField(it.govpay.orm.Dominio.model().COD_DOMINIO);
					fsw.setSortOrder(SortOrder.ASC);
					filter.getFilterSortList().add(fsw);
					List<Dominio> findAll = dominiBD.findAll(filter );

					Domini dominiDars = new Domini();
					DominiHandler dominiHandler = (DominiHandler) dominiDars.getDarsHandler();

					if(findAll != null && findAll.size() > 0){
						for (Dominio dominio : findAll) {
							domini.add(new Voce<Long>(dominiHandler.getTitolo(dominio,bd), dominio.getId()));  
						}
					}
				}else {
					domini.add(new Voce<Long>(Utils.getInstance().getMessageFromResourceBundle("commons.label.qualsiasi"), -1L));
				}
			} catch (ServiceException e) {
				throw new ConsoleException(e);
			}
			SelectList<Long> idDominio = (SelectList<Long>) infoRicercaMap.get(idDominioId);
			idDominio.setDefaultValue(-1L);
			idDominio.setValues(domini); 
			sezioneRoot.addField(idDominio);

			/// datainizio
			InputDate dataInizio = (InputDate) infoRicercaMap.get(dataInizioId);
			dataInizio.setDefaultValue(null);
			sezioneRoot.addField(dataInizio);

			/// dataFine
			InputDate dataFine = (InputDate) infoRicercaMap.get(dataFineId);
			dataFine.setDefaultValue(null);
			sezioneRoot.addField(dataFine);	

			// idDominio
			List<Voce<String>> stati = new ArrayList<Voce<String>>();
			stati.add(new Voce<String>(Utils.getInstance().getMessageFromResourceBundle("commons.label.qualsiasi"), ""));
			stati.add(new Voce<String>(Utils.getInstance().getMessageFromResourceBundle(this.nomeServizio + ".statoVersamento."+StatoVersamento.ESEGUITO.name()), StatoVersamento.ESEGUITO.name()));
			stati.add(new Voce<String>(Utils.getInstance().getMessageFromResourceBundle(this.nomeServizio + ".statoVersamento."+StatoVersamento.PARZIALMENTE_ESEGUITO.name()), StatoVersamento.PARZIALMENTE_ESEGUITO.name()));
			stati.add(new Voce<String>(Utils.getInstance().getMessageFromResourceBundle(this.nomeServizio + ".statoVersamento."+StatoVersamento.ESEGUITO_SENZA_RPT.name()), StatoVersamento.ESEGUITO_SENZA_RPT.name()));
			stati.add(new Voce<String>(Utils.getInstance().getMessageFromResourceBundle(this.nomeServizio + ".statoVersamento."+StatoVersamento.ANOMALO.name()), StatoVersamento.ANOMALO.name()));
			stati.add(new Voce<String>(Utils.getInstance().getMessageFromResourceBundle(this.nomeServizio + ".statoVersamento."+StatoVersamento.ANNULLATO.name()), StatoVersamento.ANNULLATO.name()));

			SelectList<String> statoVersamento = (SelectList<String>) infoRicercaMap.get(statoVersamentoId);
			statoVersamento.setDefaultValue("");
			statoVersamento.setValues(stati); 
			sezioneRoot.addField(statoVersamento);
		}catch(Exception e){
			throw new ConsoleException(e);
		}

		return infoRicerca;
	}

	private void initInfoRicerca(UriInfo uriInfo, BasicBD bd) throws ConsoleException{
		if(infoRicercaMap == null){
			infoRicercaMap = new HashMap<String, ParamField<?>>();

			String idDominioId = Utils.getInstance().getMessageFromResourceBundle(this.nomeServizio + ".idDominio.id");
			String statoVersamentoId = Utils.getInstance().getMessageFromResourceBundle(this.nomeServizio + ".statoVersamento.id");
			String dataInizioId = Utils.getInstance().getMessageFromResourceBundle(this.nomeServizio + ".dataInizio.id");
			String dataFineId = Utils.getInstance().getMessageFromResourceBundle(this.nomeServizio + ".dataFine.id");

			List<Voce<Long>> domini = new ArrayList<Voce<Long>>();
			// idDominio
			String idDominioLabel = Utils.getInstance().getMessageFromResourceBundle(this.nomeServizio + ".idDominio.label");
			SelectList<Long> idDominio = new SelectList<Long>(idDominioId, idDominioLabel, null, false, false, true, domini);
			infoRicercaMap.put(idDominioId, idDominio);

			// statoVersamento
			List<Voce<String>> stati = new ArrayList<Voce<String>>();
			String statoVersamentoLabel = Utils.getInstance().getMessageFromResourceBundle(this.nomeServizio + ".statoVersamento.label");
			SelectList<String> statoVersamento = new SelectList<String>(statoVersamentoId, statoVersamentoLabel, null, false, false, true, stati);
			infoRicercaMap.put(statoVersamentoId, statoVersamento);

			// dataInizio
			String dataInizioLabel = Utils.getInstance().getMessageFromResourceBundle(this.nomeServizio + ".dataInizio.label");
			InputDate dataInizio = new InputDate(dataInizioId, dataInizioLabel, null, false, false, true, null, null);
			infoRicercaMap.put(dataInizioId, dataInizio);

			// dataFine
			String dataFineLabel = Utils.getInstance().getMessageFromResourceBundle(this.nomeServizio + ".dataFine.label");
			InputDate dataFine = new InputDate(dataFineId, dataFineLabel, null, false, false, true, null, null);
			infoRicercaMap.put(dataFineId, dataFine);
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
			Operatore operatore = this.darsService.getOperatoreByPrincipal(bd); 
			ProfiloOperatore profilo = operatore.getProfilo();
			boolean isAdmin = profilo.equals(ProfiloOperatore.ADMIN);

			// idDominio
			EstrattiContoBD pagamentiBD = new EstrattiContoBD(bd);
			EstrattoContoFilter filter = pagamentiBD.newFilter();
			boolean eseguiRicerca = true;
			List<Long> ids = new ArrayList<Long>();
			ids.add(id);

			if(!isAdmin){

				AclBD aclBD = new AclBD(bd);
				List<Acl> aclOperatore = aclBD.getAclOperatore(operatore.getId());

				boolean vediTuttiDomini = false;
				List<Long> idDomini = new ArrayList<Long>();
				for(Acl acl: aclOperatore) {
					if(Tipo.DOMINIO.equals(acl.getTipo())) {
						if(acl.getIdDominio() == null) {
							vediTuttiDomini = true;
							break;
						} else {
							idDomini.add(acl.getIdDominio());
						}
					}
				}
				if(!vediTuttiDomini) {
					if(idDomini.isEmpty()) {
						eseguiRicerca = false;
					} else {
						filter.setIdDomini(idDomini);
					}
				}

				// l'operatore puo' vedere i domini associati, controllo se c'e' un versamento con Id nei domini concessi.
				if(eseguiRicerca){
					filter.setIdPagamento(ids);
					eseguiRicerca = eseguiRicerca && pagamentiBD.count(filter) > 0;
				}
			}
			// recupero oggetto
			filter.setIdPagamento(ids);
			List<EstrattoConto> findAll = eseguiRicerca ?  pagamentiBD.findAll(filter) : new ArrayList<EstrattoConto>();
			EstrattoConto pagamento = findAll.size() > 0 ? findAll.get(0) : null;

			InfoForm infoModifica = null;
			URI cancellazione = null;
			URI esportazione = this.getUriEsportazioneDettaglio(uriInfo, bd, id);

			String titolo = pagamento != null ? this.getTitolo(pagamento,bd) : "";
			Dettaglio dettaglio = new Dettaglio(titolo, esportazione, cancellazione, infoModifica);

			it.govpay.web.rs.dars.model.Sezione root = dettaglio.getSezioneRoot();

			if(pagamento != null){
				// codVersamentoEnte
				if(StringUtils.isNotEmpty(pagamento.getCodVersamentoEnte())){
					Versamenti versamentiDars = new Versamenti();
					URI uriVersamento= BaseRsService.checkDarsURI(uriInfo).path(versamentiDars.getPathServizio()).path("{id}").build(id); 
					root.addVoce(Utils.getInstance().getMessageFromResourceBundle(this.nomeServizio + ".codVersamentoEnte.label"), pagamento.getCodVersamentoEnte(), uriVersamento);
				}
				// IUV
				if(StringUtils.isNotEmpty(pagamento.getIuv())) 
					root.addVoce(Utils.getInstance().getMessageFromResourceBundle(this.nomeServizio + ".iuv.label"), pagamento.getIuv());
				// CF Debitore
				if(StringUtils.isNotEmpty(pagamento.getDebitoreIdentificativo())) 
					root.addVoce(Utils.getInstance().getMessageFromResourceBundle(this.nomeServizio + ".codiceFiscaleDebitore.label"), pagamento.getDebitoreIdentificativo());
				// Importo Dovuto
				if(pagamento.getImportoDovuto() != null)
					root.addVoce(Utils.getInstance().getMessageFromResourceBundle(this.nomeServizio + ".importoDovuto.label"), pagamento.getImportoDovuto().toString()+ "€");
				// Importo Pagato
				if(pagamento.getImportoPagato() != null)
					root.addVoce(Utils.getInstance().getMessageFromResourceBundle(this.nomeServizio + ".importoPagato.label"), pagamento.getImportoPagato().toString()+ "€");
				// Data Pagamento
				if(pagamento.getDataPagamento() != null)
					root.addVoce(Utils.getInstance().getMessageFromResourceBundle(this.nomeServizio + ".dataPagamento.label"), this.sdf.format(pagamento.getDataPagamento()));
				// causale
				if(StringUtils.isNotEmpty(pagamento.getCausale())) 
					root.addVoce(Utils.getInstance().getMessageFromResourceBundle(this.nomeServizio + ".causale.label"), pagamento.getCausale());
				// Stato
				if(pagamento.getStatoVersamento() != null)
					root.addVoce(Utils.getInstance().getMessageFromResourceBundle(this.nomeServizio + ".statoVersamento.label"),
							Utils.getInstance().getMessageFromResourceBundle(this.nomeServizio + ".statoVersamento."+pagamento.getStatoVersamento()));
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
	public String getTitolo(EstrattoConto entry,BasicBD bd) {
		StringBuilder sb = new StringBuilder();

		String codVersamentoEnte = entry.getCodVersamentoEnte();

		String cfDebitore = entry.getDebitoreIdentificativo();
		String iuv = entry.getIuv() != null ? entry.getIuv() : "--";
		String importoPagato = entry.getImportoPagato()  != null ? entry.getImportoPagato().toString()+ "€" : "";
		String importoDovuto = entry.getImportoDovuto() != null ? entry.getImportoDovuto().toString()+ "€" : "";

		sb.append(Utils.getInstance().getMessageWithParamsFromResourceBundle(this.nomeServizio + ".label.titolo", codVersamentoEnte,iuv,cfDebitore,importoPagato,importoDovuto));

		return sb.toString();
	}

	@Override
	public String getSottotitolo(EstrattoConto entry,BasicBD bd) {
		StringBuilder sb = new StringBuilder();
		StatoVersamento statoVersamento = entry.getStatoVersamento();
		Date dataPagamento = entry.getDataPagamento();
		try{
			String causale = entry.getCausale();

			sb.append(Utils.getInstance().getMessageWithParamsFromResourceBundle(this.nomeServizio + ".label.sottotitolo",
					Utils.getInstance().getMessageFromResourceBundle(this.nomeServizio + ".statoVersamento."+statoVersamento.name()),
					this.sdf.format(dataPagamento), causale));
		}catch(Exception e) {}
		return sb.toString();
	} 

	@Override
	public List<String> getValori(EstrattoConto entry, BasicBD bd) throws ConsoleException {

		List<String> valori = new ArrayList<String>();
		String codVersamentoEnte = entry.getCodSingoloVersamentoEnte();
		String iuv = entry.getIuv() != null ? entry.getIuv() : "--";
		String cfDebitore = entry.getDebitoreIdentificativo();
		StatoVersamento statoVersamento = entry.getStatoVersamento();
		Date dataPagamento = entry.getDataPagamento();
		String importoPagato = entry.getImportoPagato()  != null ? entry.getImportoPagato().toString()+ "€" : "";
		String importoDovuto = entry.getImportoDovuto() != null ? entry.getImportoDovuto().toString()+ "€" : "";

		valori.add(codVersamentoEnte);
		valori.add(iuv);
		valori.add(cfDebitore);
		valori.add(importoDovuto);
		valori.add(importoPagato);
		valori.add(this.sdf.format(dataPagamento));
		valori.add(Utils.getInstance().getMessageFromResourceBundle(this.nomeServizio + ".statoVersamento."+ statoVersamento.name()));

		return valori; 
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
		Printer printer  = null;
		String methodName = "esporta " + this.titoloServizio + "[" + sb.toString() + "]";
		int numeroZipEntries = 0;

		if(idsToExport.size() == 1)
			return this.esporta(idsToExport.get(0), uriInfo, bd, zout); 

		String fileName = "Pagamenti.zip";


		try{
			this.log.info("Esecuzione " + methodName + " in corso...");
			Operatore operatore = this.darsService.getOperatoreByPrincipal(bd); 
			ProfiloOperatore profilo = operatore.getProfilo();
			boolean isAdmin = profilo.equals(ProfiloOperatore.ADMIN);

			SingoliVersamentiBD singoliVersamentiBD = new SingoliVersamentiBD(bd);
			EstrattiContoBD estrattiContoBD = new EstrattiContoBD(bd);
			EstrattoContoFilter filter = estrattiContoBD.newFilter(); 
			boolean eseguiRicerca = true;
			List<Long> ids = idsToExport;

			if(!isAdmin){

				AclBD aclBD = new AclBD(bd);
				List<Acl> aclOperatore = aclBD.getAclOperatore(operatore.getId());

				boolean vediTuttiDomini = false;
				List<Long> idDomini = new ArrayList<Long>();
				for(Acl acl: aclOperatore) {
					if(Tipo.DOMINIO.equals(acl.getTipo())) {
						if(acl.getIdDominio() == null) {
							vediTuttiDomini = true;
							break;
						} else {
							idDomini.add(acl.getIdDominio());
						}
					}
				}
				if(!vediTuttiDomini) {
					if(idDomini.isEmpty()) {
						eseguiRicerca = false;
					} else {
						filter.setIdDomini(idDomini);
					}
				}

				// l'operatore puo' vedere i domini associati, controllo se c'e' un versamento con Id nei domini concessi.
				if(eseguiRicerca){
					filter.setIdPagamento(ids);
					eseguiRicerca = eseguiRicerca && estrattiContoBD.count(filter) > 0;
				}
			}

			if(eseguiRicerca){
				Map<String, List<Long>> mappaInputEstrattoConto = new HashMap<String, List<Long>>();
				Map<String, Dominio> mappaInputDomini = new HashMap<String, Dominio>();
				// recupero oggetto
				filter.setIdPagamento(ids);
				List<EstrattoConto> findAll = eseguiRicerca ?  estrattiContoBD.estrattoContoFromIdSingoliVersamenti(filter) : new ArrayList<EstrattoConto>();

				if(findAll != null && findAll.size() > 0){
					ByteArrayOutputStream baos  = new ByteArrayOutputStream();
					try{
						ZipEntry pagamentoCsv = new ZipEntry("pagamenti.csv");
						zout.putNextEntry(pagamentoCsv);
						printer = new Printer(this.getFormat() , baos);
						printer.printRecord(CSVUtils.getEstrattoContoCsvHeader());
						for (EstrattoConto pagamento : findAll) {
							printer.printRecord(CSVUtils.getEstrattoContoAsCsvRow(pagamento,this.sdf));
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
					zout.write(baos.toByteArray());
					zout.closeEntry();
				}
				
				for (Long idSingoloVersamento : idsToExport) {
					SingoloVersamento singoloVersamento = singoliVersamentiBD.getSingoloVersamento(idSingoloVersamento);
					Versamento versamento = singoloVersamento.getVersamento(bd);

					// Prelevo il dominio
					UnitaOperativa uo  = AnagraficaManager.getUnitaOperativa(bd, versamento.getIdUo());
					Dominio dominio  = AnagraficaManager.getDominio(bd, uo.getIdDominio());

					// Aggrego i versamenti per dominio per generare gli estratti conto
					List<Long> idSingoliVersamentiDominio = null;
					if(mappaInputEstrattoConto.containsKey(dominio.getCodDominio()))
						idSingoliVersamentiDominio = mappaInputEstrattoConto.get(dominio.getCodDominio());
					else{
						idSingoliVersamentiDominio = new ArrayList<Long>();
						mappaInputEstrattoConto.put(dominio.getCodDominio(), idSingoliVersamentiDominio);
						mappaInputDomini.put(dominio.getCodDominio(), dominio);
					}
					idSingoliVersamentiDominio.add(idSingoloVersamento);
				}
				
				List<it.govpay.core.business.model.EstrattoConto> listInputEstrattoConto = new ArrayList<it.govpay.core.business.model.EstrattoConto>();
				for (String codDominio : mappaInputEstrattoConto.keySet()) {
					it.govpay.core.business.model.EstrattoConto input =  it.govpay.core.business.model.EstrattoConto.creaEstrattoContoPagamentiPDF(mappaInputDomini.get(codDominio), mappaInputEstrattoConto.get(codDominio)); 
					listInputEstrattoConto.add(input);
				}

				String pathLoghi = ConsoleProperties.getInstance().getPathEstrattoContoPdfLoghi();
				it.govpay.core.business.EstrattoConto estrattoContoBD = new it.govpay.core.business.EstrattoConto(bd);
				List<it.govpay.core.business.model.EstrattoConto> listOutputEstattoConto = estrattoContoBD.getEstrattoContoPagamenti(listInputEstrattoConto,pathLoghi);

				for (it.govpay.core.business.model.EstrattoConto estrattoContoOutput : listOutputEstattoConto) {
					Map<String, ByteArrayOutputStream> estrattoContoVersamenti = estrattoContoOutput.getOutput(); 
					for (String nomeEntry : estrattoContoVersamenti.keySet()) {
						numeroZipEntries ++;
						ByteArrayOutputStream baos = estrattoContoVersamenti.get(nomeEntry);
						ZipEntry estrattoContoEntry = new ZipEntry(nomeEntry);
//						ZipEntry estrattoContoEntry = new ZipEntry(estrattoContoOutput.getDominio().getCodDominio() + "/" + nomeEntry);
						zout.putNextEntry(estrattoContoEntry);
						zout.write(baos.toByteArray());
						zout.closeEntry();
					}


				}
			}

			// se non ho inserito nessuna entry
			if(numeroZipEntries == 0){
				String noEntriesTxt = "/README";
				ZipEntry entryTxt = new ZipEntry(noEntriesTxt);
				zout.putNextEntry(entryTxt);
				zout.write("Non sono state trovate informazioni sui pagamenti selezionati.".getBytes());
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
		Printer printer  = null;
		int numeroZipEntries = 0;
		try{
			String fileName = "Pagamenti.zip";
			this.log.info("Esecuzione " + methodName + " in corso...");
			Operatore operatore = this.darsService.getOperatoreByPrincipal(bd); 
			ProfiloOperatore profilo = operatore.getProfilo();
			boolean isAdmin = profilo.equals(ProfiloOperatore.ADMIN);

			it.govpay.core.business.EstrattoConto estrattoContoBD = new it.govpay.core.business.EstrattoConto(bd);
			EstrattiContoBD pagamentiBD = new EstrattiContoBD(bd);
			EstrattoContoFilter filter = pagamentiBD.newFilter();
			boolean eseguiRicerca = true;
			List<Long> ids = new ArrayList<Long>();
			ids.add(idToExport);
			SingoliVersamentiBD singoliVersamentiBD = new SingoliVersamentiBD(bd);

			if(!isAdmin){

				AclBD aclBD = new AclBD(bd);
				List<Acl> aclOperatore = aclBD.getAclOperatore(operatore.getId());

				boolean vediTuttiDomini = false;
				List<Long> idDomini = new ArrayList<Long>();
				for(Acl acl: aclOperatore) {
					if(Tipo.DOMINIO.equals(acl.getTipo())) {
						if(acl.getIdDominio() == null) {
							vediTuttiDomini = true;
							break;
						} else {
							idDomini.add(acl.getIdDominio());
						}
					}
				}
				if(!vediTuttiDomini) {
					if(idDomini.isEmpty()) {
						eseguiRicerca = false;
					} else {
						filter.setIdDomini(idDomini);
					}
				}

				// l'operatore puo' vedere i domini associati, controllo se c'e' un versamento con Id nei domini concessi.
				if(eseguiRicerca){
					filter.setIdPagamento(ids);
					eseguiRicerca = eseguiRicerca && pagamentiBD.count(filter) > 0;
				}
			}

			if(eseguiRicerca ){
				// recupero oggetto
				filter.setIdPagamento(ids);
				List<EstrattoConto> findAll = eseguiRicerca ?  pagamentiBD.estrattoContoFromIdSingoliVersamenti(filter) : new ArrayList<EstrattoConto>();

				if(findAll != null && findAll.size() > 0){
					ByteArrayOutputStream baos  = new ByteArrayOutputStream();
					try{
						ZipEntry pagamentoCsv = new ZipEntry("pagamenti.csv");
						zout.putNextEntry(pagamentoCsv);
						printer = new Printer(this.getFormat() , baos);
						printer.printRecord(CSVUtils.getEstrattoContoCsvHeader());
						for (EstrattoConto pagamento : findAll) {
							printer.printRecord(CSVUtils.getEstrattoContoAsCsvRow(pagamento,this.sdf));
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
					zout.write(baos.toByteArray());
					zout.closeEntry();
				}

				SingoloVersamento singoloVersamento = singoliVersamentiBD.getSingoloVersamento(idToExport);
				Versamento versamento = singoloVersamento.getVersamento(bd);
				UnitaOperativa uo  = AnagraficaManager.getUnitaOperativa(bd, versamento.getIdUo());
				Dominio dominio  = AnagraficaManager.getDominio(bd, uo.getIdDominio());
				// Estratto conto per iban e codiceversamento.
				List<Long> idSingoliVersamentiDominio = new ArrayList<Long>();
				idSingoliVersamentiDominio.add(idToExport);
				
				it.govpay.core.business.model.EstrattoConto input =  it.govpay.core.business.model.EstrattoConto.creaEstrattoContoPagamentiPDF(dominio, idSingoliVersamentiDominio);
				List<it.govpay.core.business.model.EstrattoConto> listInputEstrattoConto = new ArrayList<it.govpay.core.business.model.EstrattoConto>();
				listInputEstrattoConto.add(input);
				String pathLoghi = ConsoleProperties.getInstance().getPathEstrattoContoPdfLoghi();
				List<it.govpay.core.business.model.EstrattoConto> listOutputEstattoConto = estrattoContoBD.getEstrattoContoPagamenti(listInputEstrattoConto,pathLoghi);

				for (it.govpay.core.business.model.EstrattoConto estrattoContoOutput : listOutputEstattoConto) {
					Map<String, ByteArrayOutputStream> estrattoContoVersamenti = estrattoContoOutput.getOutput(); 
					for (String nomeEntry : estrattoContoVersamenti.keySet()) {
						numeroZipEntries ++;
						ByteArrayOutputStream baos = estrattoContoVersamenti.get(nomeEntry);
//						ZipEntry estrattoContoEntry = new ZipEntry(estrattoContoOutput.getDominio().getCodDominio() + "/" + nomeEntry);
						ZipEntry estrattoContoEntry = new ZipEntry(nomeEntry);
						zout.putNextEntry(estrattoContoEntry);
						zout.write(baos.toByteArray());
						zout.closeEntry();
					}
				}

			}
			// se non ho inserito nessuna entry
			if(numeroZipEntries == 0){
				String noEntriesTxt = "/README";
				ZipEntry entryTxt = new ZipEntry(noEntriesTxt);
				zout.putNextEntry(entryTxt);
				zout.write("Non sono state trovate informazioni sui pagamenti selezionati.".getBytes());
				zout.closeEntry();
			}

			zout.flush();
			zout.close();

			this.log.info("Esecuzione " + methodName + " completata.");

			return  fileName;
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
	public InfoForm getInfoModifica(UriInfo uriInfo, BasicBD bd, EstrattoConto entry) throws ConsoleException { return null; }

	@Override
	public void delete(List<Long> idsToDelete, UriInfo uriInfo, BasicBD bd) throws WebApplicationException, ConsoleException {	}

	@Override
	public EstrattoConto creaEntry(InputStream is, UriInfo uriInfo, BasicBD bd) throws WebApplicationException, ConsoleException { return null; }

	@Override
	public Dettaglio insert(InputStream is, UriInfo uriInfo, BasicBD bd) throws WebApplicationException, ConsoleException, ValidationException, DuplicatedEntryException { return null; }

	@Override
	public void checkEntry(EstrattoConto entry, EstrattoConto oldEntry) throws ValidationException { }

	@Override
	public Dettaglio update(InputStream is, UriInfo uriInfo, BasicBD bd) throws WebApplicationException, ConsoleException, ValidationException { return null; }

	@Override
	public Object uplaod(MultipartFormDataInput input, UriInfo uriInfo, BasicBD bd)	throws WebApplicationException, ConsoleException, ValidationException { return null;}
}
