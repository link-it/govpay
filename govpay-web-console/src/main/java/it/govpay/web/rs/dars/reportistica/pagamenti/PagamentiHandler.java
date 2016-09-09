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

import java.io.InputStream;
import java.net.URI;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
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
import it.govpay.bd.anagrafica.AclBD;
import it.govpay.bd.anagrafica.DominiBD;
import it.govpay.bd.anagrafica.filters.DominioFilter;
import it.govpay.bd.model.Acl;
import it.govpay.bd.model.Acl.Tipo;
import it.govpay.bd.model.Dominio;
import it.govpay.bd.model.Operatore;
import it.govpay.bd.model.Operatore.ProfiloOperatore;
import it.govpay.bd.model.Versamento.StatoVersamento;
import it.govpay.bd.model.reportistica.Pagamento;
import it.govpay.bd.reportistica.PagamentiBD;
import it.govpay.bd.reportistica.filters.PagamentoFilter;
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
import it.govpay.web.rs.dars.model.input.base.SelectList;
import it.govpay.web.utils.Utils;

public class PagamentiHandler extends BaseDarsHandler<Pagamento> implements IDarsHandler<Pagamento>{

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

			PagamentiBD pagamentiBD = new PagamentiBD(bd);
			PagamentoFilter filter = pagamentiBD.newFilter();
			filter.setOffset(offset);
			filter.setLimit(limit);
			FilterSortWrapper fsw = new FilterSortWrapper();
			fsw.setField(filter.getDataPagamentoAliasField());
			fsw.setSortOrder(SortOrder.DESC);
			filter.getFilterSortList().add(fsw);

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
    Causale
    Stato

			 * */

			List<String> valori = new ArrayList<String>();
			valori.add(Utils.getInstance().getMessageFromResourceBundle(this.nomeServizio + ".codiceVersamento.label"));
			valori.add(Utils.getInstance().getMessageFromResourceBundle(this.nomeServizio + ".iuv.label"));
			valori.add(Utils.getInstance().getMessageFromResourceBundle(this.nomeServizio + ".codiceFiscaleDebitore.label"));
			valori.add(Utils.getInstance().getMessageFromResourceBundle(this.nomeServizio + ".importoDovuto.label"));
			valori.add(Utils.getInstance().getMessageFromResourceBundle(this.nomeServizio + ".importoPagato.label"));
			valori.add(Utils.getInstance().getMessageFromResourceBundle(this.nomeServizio + ".dataPagamento.label"));
			valori.add(Utils.getInstance().getMessageFromResourceBundle(this.nomeServizio + ".causale.label"));
			valori.add(Utils.getInstance().getMessageFromResourceBundle(this.nomeServizio + ".statoVersamento.label"));

			Elemento intestazione = new Elemento(-1, valori , null);


			Elenco elenco = new Elenco(this.titoloServizio, infoRicerca,
					this.getInfoCreazione(uriInfo, bd),
					count, esportazione, cancellazione,true,intestazione ); 

			UriBuilder uriDettaglioBuilder = BaseRsService.checkDarsURI(uriInfo).path(this.pathServizio).path("{id}");

			List<Pagamento> findAll = eseguiRicerca ? pagamentiBD.findAll(filter) : new ArrayList<Pagamento>(); 

			if(findAll != null && findAll.size() > 0){
				for (Pagamento entry : findAll) {
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

		String idDominioId = Utils.getInstance().getMessageFromResourceBundle(this.nomeServizio + ".idDominio.id");

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

		}catch(Exception e){
			throw new ConsoleException(e);
		}

		return infoRicerca;
	}

	private void initInfoRicerca(UriInfo uriInfo, BasicBD bd) throws ConsoleException{
		if(infoRicercaMap == null){
			infoRicercaMap = new HashMap<String, ParamField<?>>();

			String idDominioId = Utils.getInstance().getMessageFromResourceBundle(this.nomeServizio + ".idDominio.id");

			List<Voce<Long>> domini = new ArrayList<Voce<Long>>();
			// idDominio
			String idDominioLabel = Utils.getInstance().getMessageFromResourceBundle(this.nomeServizio + ".idDominio.label");
			SelectList<Long> idDominio = new SelectList<Long>(idDominioId, idDominioLabel, null, false, false, true, domini);
			infoRicercaMap.put(idDominioId, idDominio);

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
			PagamentiBD pagamentiBD = new PagamentiBD(bd);
			PagamentoFilter filter = pagamentiBD.newFilter();
			boolean eseguiRicerca = true;

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
					filter.setIdPagamento(id);
					eseguiRicerca = eseguiRicerca && pagamentiBD.count(filter) > 0;
				}
			}
			// recupero oggetto
			Pagamento pagamento = eseguiRicerca ? pagamentiBD.getPagamento(id) : null;

			InfoForm infoModifica = null;
			URI cancellazione = null;
			URI esportazione = this.getUriEsportazioneDettaglio(uriInfo, bd, id);

			String titolo = pagamento != null ? this.getTitolo(pagamento,bd) : "";
			Dettaglio dettaglio = new Dettaglio(titolo, esportazione, cancellazione, infoModifica);

			it.govpay.web.rs.dars.model.Sezione root = dettaglio.getSezioneRoot();

			if(pagamento != null){

				// codVersamentoEnte
				if(StringUtils.isNotEmpty(pagamento.getCodSingoloVersamentoEnte())) 
					root.addVoce(Utils.getInstance().getMessageFromResourceBundle(this.nomeServizio + ".codVersamentoEnte.label"), pagamento.getCodSingoloVersamentoEnte());
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
	public String getTitolo(Pagamento entry,BasicBD bd) {
		StringBuilder sb = new StringBuilder();

		String codVersamentoEnte = entry.getCodSingoloVersamentoEnte();
		
		String cfDebitore = entry.getDebitoreIdentificativo();
		String iuv = entry.getIuv() != null ? entry.getIuv() : "--";
		String importoPagato = entry.getImportoPagato()  != null ? entry.getImportoPagato().toString()+ "€" : "";
		String importoDovuto = entry.getImportoDovuto() != null ? entry.getImportoDovuto().toString()+ "€" : "";

		sb.append(Utils.getInstance().getMessageWithParamsFromResourceBundle(this.nomeServizio + ".label.titolo", codVersamentoEnte,iuv,cfDebitore,importoPagato,importoDovuto));

		return sb.toString();
	}

	@Override
	public String getSottotitolo(Pagamento entry,BasicBD bd) {
		StringBuilder sb = new StringBuilder();
		StatoVersamento statoVersamento = entry.getStatoVersamento();
		Date dataPagamento = entry.getDataPagamento();
		
		String causale = entry.getCausale();

		sb.append(Utils.getInstance().getMessageWithParamsFromResourceBundle(this.nomeServizio + ".label.sottotitolo",
				Utils.getInstance().getMessageFromResourceBundle(this.nomeServizio + ".statoVersamento."+statoVersamento.name()),
				this.sdf.format(dataPagamento), causale));

		return sb.toString();
	} 

	@Override
	public List<String> getValori(Pagamento entry, BasicBD bd) throws ConsoleException {

		List<String> valori = new ArrayList<String>();
		/*
			Cod versamento
			IUV
			C.F. Debitore
			Importo dovuto
			Importo pagato
			Data pagamento
			Causale
			Stato
		 * */
		String codVersamentoEnte = entry.getCodSingoloVersamentoEnte();
		String iuv = entry.getIuv() != null ? entry.getIuv() : "--";
		String cfDebitore = entry.getDebitoreIdentificativo();
		StatoVersamento statoVersamento = entry.getStatoVersamento();
		Date dataPagamento = entry.getDataPagamento();
		String importoPagato = entry.getImportoPagato()  != null ? entry.getImportoPagato().toString()+ "€" : "";
		String importoDovuto = entry.getImportoDovuto() != null ? entry.getImportoDovuto().toString()+ "€" : "";
		String causale = entry.getCausale();

		valori.add(codVersamentoEnte);
		valori.add(iuv);
		valori.add(cfDebitore);
		valori.add(importoPagato);
		valori.add(importoDovuto);
		valori.add(this.sdf.format(dataPagamento));
		valori.add(causale);
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

		String methodName = "esporta " + this.titoloServizio + "[" + sb.toString() + "]";

		if(idsToExport.size() == 1)
			return this.esporta(idsToExport.get(0), uriInfo, bd, zout); 

		String fileName = "Versamenti.zip";
		try{
			//			this.log.info("Esecuzione " + methodName + " in corso...");
			//			this.darsService.getOperatoreByPrincipal(bd); 
			//
			//			VersamentiBD versamentiBD = new VersamentiBD(bd);
			//			PagamentiBD pagamentiBD = new PagamentiBD(bd);
			//			RptBD rptBD = new RptBD(bd);
			//
			//			for (Long idVersamento : idsToExport) {
			//				Versamento versamento = versamentiBD.getVersamento(idVersamento);
			//
			//				String folderName = "Versamento_" + versamento.getCodVersamentoEnte();
			//
			//				List<Long> idSingoliVersamenti = new ArrayList<Long>();
			//				List<SingoloVersamento> singoliVersamenti = versamento.getSingoliVersamenti(bd);
			//				if(singoliVersamenti != null && singoliVersamenti.size() >0){
			//					for (SingoloVersamento singoloVersamento : singoliVersamenti) {
			//						idSingoliVersamenti.add(singoloVersamento.getId());
			//					}
			//				}
			//
			//				PagamentoFilter filter = pagamentiBD.newFilter();
			//				FilterSortWrapper fsw = new FilterSortWrapper();
			//				fsw.setField(it.govpay.orm.Pagamento.model().DATA_PAGAMENTO);
			//				fsw.setSortOrder(SortOrder.DESC);
			//				filter.getFilterSortList().add(fsw);
			//				filter.setIdSingoliVersamenti(idSingoliVersamenti);
			//				// cerco i pagamenti solo se ho singoliversamenti
			//				List<Pagamento> listaPagamenti = idSingoliVersamenti.size() > 0 ?  pagamentiBD.findAll(filter) : new ArrayList<Pagamento>(); 
			//				if(listaPagamenti != null && listaPagamenti.size()> 0)
			//					for (Pagamento pagamento : listaPagamenti) {
			//						SingoloVersamento singoloVersamento = pagamento.getSingoloVersamento(bd);
			//						String folderNamepagamento = "Pagamento_" + singoloVersamento.getCodSingoloVersamentoEnte();
			//
			//						if(pagamento.getAllegato()!= null){
			//							ZipEntry rtXml = new ZipEntry(folderName + "/"+ folderNamepagamento + "/allegato.xml");
			//							zout.putNextEntry(rtXml);
			//							zout.write(pagamento.getAllegato());
			//							zout.closeEntry();
			//						}
			//					}
			//
			//				RptFilter rptFilter = rptBD.newFilter();
			//				FilterSortWrapper rptFsw = new FilterSortWrapper();
			//				rptFsw.setField(it.govpay.orm.RPT.model().DATA_MSG_RICHIESTA);
			//				rptFsw.setSortOrder(SortOrder.DESC);
			//				rptFilter.getFilterSortList().add(rptFsw);
			//				rptFilter.setIdVersamento(idVersamento); 
			//
			//				RrBD rrBD = new RrBD(bd);
			//				FilterSortWrapper rrFsw = new FilterSortWrapper();
			//				rrFsw.setField(it.govpay.orm.RR.model().DATA_MSG_REVOCA);
			//				rrFsw.setSortOrder(SortOrder.DESC);
			//
			//				List<Rpt> listaRpt = rptBD.findAll(rptFilter);
			//				if(listaRpt != null && listaRpt.size() >0 )
			//					for (Rpt rpt : listaRpt) {
			//						String folderNameRpt = "TransazionePagamento_"+rpt.getCodMsgRichiesta();
			//
			//						ZipEntry rptXml = new ZipEntry(folderName + "/"+ folderNameRpt +"/rpt.xml");
			//						zout.putNextEntry(rptXml);
			//						zout.write(rpt.getXmlRpt());
			//						zout.closeEntry();
			//
			//						if(rpt.getXmlRt() != null){
			//							ZipEntry rtXml = new ZipEntry(folderName + "/"+ folderNameRpt + "/rt.xml");
			//							zout.putNextEntry(rtXml);
			//							zout.write(rpt.getXmlRt());
			//							zout.closeEntry();
			//						}
			//
			//						RrFilter rrFilter = rrBD.newFilter();
			//						rrFilter.getFilterSortList().add(rrFsw);
			//						rrFilter.setIdRpt(rpt.getId()); 
			//						List<Rr> findAll = rrBD.findAll(rrFilter);
			//						if(findAll != null && findAll.size() > 0){
			//							for (Rr rr : findAll) {
			//								String folderNameRr = "TransazioneRevoca_"+ rr.getCodMsgRevoca();
			//
			//								ZipEntry rrXml = new ZipEntry(folderName + "/"+ folderNameRr+"/rr.xml");
			//								zout.putNextEntry(rrXml);
			//								zout.write(rr.getXmlRr());
			//								zout.closeEntry();
			//
			//								if(rr.getXmlEr() != null){
			//									ZipEntry rtXml = new ZipEntry(folderName + "/"+ folderNameRr+"/er.xml");
			//									zout.putNextEntry(rtXml);
			//									zout.write(rr.getXmlEr());
			//									zout.closeEntry();
			//								}
			//							}
			//						}
			//					}
			//			}
			//			zout.flush();
			//			zout.close();

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
			//			this.log.info("Esecuzione " + methodName + " in corso...");
			//			this.darsService.getOperatoreByPrincipal(bd); 
			//
			//			VersamentiBD versamentiBD = new VersamentiBD(bd);
			//			PagamentiBD pagamentiBD = new PagamentiBD(bd);
			//			RptBD rptBD = new RptBD(bd);
			//			Versamento versamento = versamentiBD.getVersamento(idToExport);
			//
			//			String fileName = "Versamento_"+versamento.getCodVersamentoEnte()+".zip";
			//
			//			List<Long> idSingoliVersamenti = new ArrayList<Long>();
			//			List<SingoloVersamento> singoliVersamenti = versamento.getSingoliVersamenti(bd);
			//			if(singoliVersamenti != null && singoliVersamenti.size() >0){
			//				for (SingoloVersamento singoloVersamento : singoliVersamenti) {
			//					idSingoliVersamenti.add(singoloVersamento.getId());
			//				}
			//			}
			//
			//			PagamentoFilter filter = pagamentiBD.newFilter();
			//			FilterSortWrapper fsw = new FilterSortWrapper();
			//			fsw.setField(it.govpay.orm.Pagamento.model().DATA_PAGAMENTO);
			//			fsw.setSortOrder(SortOrder.DESC);
			//			filter.getFilterSortList().add(fsw);
			//			filter.setIdSingoliVersamenti(idSingoliVersamenti);
			//			List<Pagamento> listaPagamenti = idSingoliVersamenti.size() > 0 ?  pagamentiBD.findAll(filter) : new ArrayList<Pagamento>(); 
			//			if(listaPagamenti != null && listaPagamenti.size()> 0)
			//				for (Pagamento pagamento : listaPagamenti) {
			//					SingoloVersamento singoloVersamento = pagamento.getSingoloVersamento(bd);
			//					String folderNamepagamento = "Pagamento_" + singoloVersamento.getCodSingoloVersamentoEnte();
			//
			//					if(pagamento.getAllegato()!= null){
			//						ZipEntry rtXml = new ZipEntry(folderNamepagamento + "/allegato.xml");
			//						zout.putNextEntry(rtXml);
			//						zout.write(pagamento.getAllegato());
			//						zout.closeEntry();
			//					}
			//				}
			//
			//			RptFilter rptFilter = rptBD.newFilter();
			//			FilterSortWrapper rptFsw = new FilterSortWrapper();
			//			rptFsw.setField(it.govpay.orm.RPT.model().DATA_MSG_RICHIESTA);
			//			rptFsw.setSortOrder(SortOrder.DESC);
			//			rptFilter.getFilterSortList().add(rptFsw);
			//			rptFilter.setIdVersamento(idToExport); 
			//
			//			RrBD rrBD = new RrBD(bd);
			//			FilterSortWrapper rrFsw = new FilterSortWrapper();
			//			rrFsw.setField(it.govpay.orm.RR.model().DATA_MSG_REVOCA);
			//			rrFsw.setSortOrder(SortOrder.DESC);
			//
			//			List<Rpt> listaRpt = rptBD.findAll(rptFilter);
			//			if(listaRpt != null && listaRpt.size() >0 )
			//				for (Rpt rpt : listaRpt) {
			//					String folderNameRpt = "TransazionePagamento_"+ rpt.getCodMsgRichiesta();
			//
			//					ZipEntry rptXml = new ZipEntry(  folderNameRpt +"/rpt.xml");
			//					zout.putNextEntry(rptXml);
			//					zout.write(rpt.getXmlRpt());
			//					zout.closeEntry();
			//
			//					if(rpt.getXmlRt() != null){
			//						ZipEntry rtXml = new ZipEntry(folderNameRpt + "/rt.xml");
			//						zout.putNextEntry(rtXml);
			//						zout.write(rpt.getXmlRt());
			//						zout.closeEntry();
			//					}
			//
			//					RrFilter rrFilter = rrBD.newFilter();
			//					rrFilter.getFilterSortList().add(rrFsw);
			//					rrFilter.setIdRpt(rpt.getId()); 
			//					List<Rr> findAll = rrBD.findAll(rrFilter);
			//					if(findAll != null && findAll.size() > 0){
			//						for (Rr rr : findAll) {
			//							String folderNameRr = "TransazioneRevoca_"+ rr.getCodMsgRevoca();
			//
			//							ZipEntry rrXml = new ZipEntry(folderNameRr+"/rr.xml");
			//							zout.putNextEntry(rrXml);
			//							zout.write(rr.getXmlRr());
			//							zout.closeEntry();
			//
			//							if(rr.getXmlEr() != null){
			//								ZipEntry rtXml = new ZipEntry(folderNameRr+"/er.xml");
			//								zout.putNextEntry(rtXml);
			//								zout.write(rr.getXmlEr());
			//								zout.closeEntry();
			//							}
			//						}
			//					}
			//				}
			//
			//			zout.flush();
			//			zout.close();
			//
			this.log.info("Esecuzione " + methodName + " completata.");

			return null ;// fileName;
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
	public InfoForm getInfoModifica(UriInfo uriInfo, BasicBD bd, Pagamento entry) throws ConsoleException { return null; }

	@Override
	public void delete(List<Long> idsToDelete, UriInfo uriInfo, BasicBD bd) throws WebApplicationException, ConsoleException {	}

	@Override
	public Pagamento creaEntry(InputStream is, UriInfo uriInfo, BasicBD bd) throws WebApplicationException, ConsoleException { return null; }

	@Override
	public Dettaglio insert(InputStream is, UriInfo uriInfo, BasicBD bd) throws WebApplicationException, ConsoleException, ValidationException, DuplicatedEntryException { return null; }

	@Override
	public void checkEntry(Pagamento entry, Pagamento oldEntry) throws ValidationException { }

	@Override
	public Dettaglio update(InputStream is, UriInfo uriInfo, BasicBD bd) throws WebApplicationException, ConsoleException, ValidationException { return null; }

	@Override
	public Object uplaod(MultipartFormDataInput input, UriInfo uriInfo, BasicBD bd)	throws WebApplicationException, ConsoleException, ValidationException { return null;}
}
