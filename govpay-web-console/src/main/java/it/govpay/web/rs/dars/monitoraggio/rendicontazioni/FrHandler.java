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
import java.net.URI;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
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
import it.govpay.bd.anagrafica.AclBD;
import it.govpay.bd.anagrafica.AnagraficaManager;
import it.govpay.bd.anagrafica.DominiBD;
import it.govpay.bd.anagrafica.filters.DominioFilter;
import it.govpay.bd.model.Dominio;
import it.govpay.bd.model.Fr;
import it.govpay.bd.model.Psp;
import it.govpay.bd.pagamento.FrBD;
import it.govpay.bd.pagamento.filters.FrFilter;
import it.govpay.model.Acl;
import it.govpay.model.Operatore;
import it.govpay.model.Acl.Tipo;
import it.govpay.model.Fr.Anomalia;
import it.govpay.model.Fr.StatoFr;
import it.govpay.model.Operatore.ProfiloOperatore;
import it.govpay.web.rs.dars.BaseDarsHandler;
import it.govpay.web.rs.dars.BaseDarsService;
import it.govpay.web.rs.dars.IDarsHandler;
import it.govpay.web.rs.dars.anagrafica.domini.Domini;
import it.govpay.web.rs.dars.anagrafica.domini.DominiHandler;
import it.govpay.web.rs.dars.anagrafica.psp.PspHandler;
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
import it.govpay.web.rs.dars.model.input.base.CheckButton;
import it.govpay.web.rs.dars.model.input.base.InputText;
import it.govpay.web.rs.dars.model.input.base.SelectList;
import it.govpay.web.utils.Utils;

public class FrHandler extends BaseDarsHandler<Fr> implements IDarsHandler<Fr>{

	private Map<String, ParamField<?>> infoRicercaMap = null;
	private SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm");  
//	private SimpleDateFormat simpleDateFormatAnno = new SimpleDateFormat("yyyy");

	public FrHandler(Logger log, BaseDarsService darsService) { 
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

			FrBD frBD = new FrBD(bd);
			FrFilter filter = frBD.newFilter();
			filter.setOffset((offset != null) ? offset: 0);
			filter.setLimit(limit);
			FilterSortWrapper fsw = new FilterSortWrapper();
			fsw.setField(it.govpay.orm.FR.model().DATA_ORA_FLUSSO);
			fsw.setSortOrder(SortOrder.DESC);
			filter.getFilterSortList().add(fsw);

			String codFlussoId = Utils.getInstance(this.getLanguage()).getMessageFromResourceBundle(this.nomeServizio + ".codFlusso.id");
			String codFlusso = this.getParameter(uriInfo, codFlussoId, String.class);

			if(StringUtils.isNotEmpty(codFlusso))
				filter.setCodFlusso(codFlusso); 

			List<String> listaCodDomini =  new ArrayList<String>();
			AclBD aclBD = new AclBD(bd);
			List<Acl> aclOperatore = aclBD.getAclOperatore(operatore.getId());
			
			boolean eseguiRicerca = true;
			String idDominioId = Utils.getInstance(this.getLanguage()).getMessageFromResourceBundle(this.nomeServizio + ".idDominio.id");
			Long idDominio = this.getParameter(uriInfo, idDominioId, Long.class);

			if(idDominio != null && idDominio > 0){
				Dominio dominio = AnagraficaManager.getDominio(bd, idDominio);
				listaCodDomini = Arrays.asList(dominio.getCodDominio());
				filter.setCodDominio(listaCodDomini); 
			}

			String statoId = Utils.getInstance().getMessageFromResourceBundle(this.nomeServizio + ".stato.id");
			String stato = this.getParameter(uriInfo, statoId, String.class);

			if(StringUtils.isNotEmpty(stato)){
				filter.setStato(stato);
			}

			String trnId = Utils.getInstance().getMessageFromResourceBundle(this.nomeServizio + ".trn.id");
			String trn = this.getParameter(uriInfo, trnId, String.class);

			if(StringUtils.isNotEmpty(trn))
				filter.setTnr(trn);


			String nascondiAltriIntermediariId = Utils.getInstance().getMessageFromResourceBundle(this.nomeServizio + ".nascondiAltriIntermediari.id");
			String nascondiAltriIntermediariS = this.getParameter(uriInfo, nascondiAltriIntermediariId, String.class);

			Boolean nascondiAltriIntermediari = false;
			if(StringUtils.isNotEmpty(nascondiAltriIntermediariS)){
				try{
					nascondiAltriIntermediari = Boolean.parseBoolean(nascondiAltriIntermediariS);}catch(Exception e){
						nascondiAltriIntermediari = false;
					}
			}

			filter.setNascondiSeSoloDiAltriIntermediari(nascondiAltriIntermediari);
			
			if(!isAdmin && listaCodDomini.isEmpty()){
				boolean vediTuttiDomini = false;

				for(Acl acl: aclOperatore) {
					if(Tipo.DOMINIO.equals(acl.getTipo())) {
						if(acl.getIdDominio() == null) {
							vediTuttiDomini = true;
							break;
						} else {
							listaCodDomini.add(acl.getCodDominio());
						}
					}
				}
				if(!vediTuttiDomini) {
					if(listaCodDomini.isEmpty()) {
						eseguiRicerca = false;
					} else {
						filter.setCodDominio(listaCodDomini);
					}
				}
			}


			long count = eseguiRicerca ? frBD.countExt(filter) : 0;

			// visualizza la ricerca solo se i risultati sono > del limit
			boolean visualizzaRicerca = this.visualizzaRicerca(count, limit);
			InfoForm infoRicerca = this.getInfoRicerca(uriInfo, bd, visualizzaRicerca);

			String formatter = Utils.getInstance(this.getLanguage()).getMessageFromResourceBundle(this.nomeServizio+".elenco.formatter");
			Elenco elenco = new Elenco(this.titoloServizio, infoRicerca,
					this.getInfoCreazione(uriInfo, bd),
					count, esportazione, cancellazione); 

			List<Fr> findAll = eseguiRicerca ? frBD.findAllExt(filter) : new ArrayList<Fr>(); 

			if(findAll != null && findAll.size() > 0){
				for (Fr entry : findAll) {
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
		URI ricerca = this.getUriRicerca(uriInfo, bd);
		InfoForm infoRicerca = new InfoForm(ricerca);

		if(visualizzaRicerca) {
			String idDominioId = Utils.getInstance(this.getLanguage()).getMessageFromResourceBundle(this.nomeServizio + ".idDominio.id");
			String codFlussoId = Utils.getInstance(this.getLanguage()).getMessageFromResourceBundle(this.nomeServizio + ".codFlusso.id");
			String statoId = Utils.getInstance(this.getLanguage()).getMessageFromResourceBundle(this.nomeServizio + ".stato.id");
			String nascondiAltriIntermediariId = Utils.getInstance(this.getLanguage()).getMessageFromResourceBundle(this.nomeServizio + ".nascondiAltriIntermediari.id");
			String trnId = Utils.getInstance(this.getLanguage()).getMessageFromResourceBundle(this.nomeServizio + ".trn.id");

			if(this.infoRicercaMap == null){
				this.initInfoRicerca(uriInfo, bd);
			}
			Sezione sezioneRoot = infoRicerca.getSezioneRoot();

			// stato
			List<Voce<String>> stati = new ArrayList<Voce<String>>();
			SelectList<String> stato = (SelectList<String>) infoRicercaMap.get(statoId);
			stati.add(new Voce<String>(Utils.getInstance(this.getLanguage()).getMessageFromResourceBundle("commons.label.qualsiasi"), ""));
			stati.add(new Voce<String>(Utils.getInstance(this.getLanguage()).getMessageFromResourceBundle(this.nomeServizio + ".stato."+StatoFr.ACCETTATA), StatoFr.ACCETTATA.toString()));
			stati.add(new Voce<String>(Utils.getInstance(this.getLanguage()).getMessageFromResourceBundle(this.nomeServizio + ".stato."+StatoFr.ANOMALA), StatoFr.ANOMALA.toString()));
			stato.setDefaultValue("");
			stato.setValues(stati); 
			sezioneRoot.addField(stato);

			// idDominio
			List<Voce<Long>> domini = new ArrayList<Voce<Long>>();

			DominiBD dominiBD = new DominiBD(bd);
			DominioFilter filter;
			try {
				filter = dominiBD.newFilter();
				FilterSortWrapper fsw = new FilterSortWrapper();
				fsw.setField(it.govpay.orm.Dominio.model().COD_DOMINIO);
				fsw.setSortOrder(SortOrder.ASC);
				filter.getFilterSortList().add(fsw);
				List<Dominio> findAll = dominiBD.findAll(filter );

				domini.add(new Voce<Long>(Utils.getInstance(this.getLanguage()).getMessageFromResourceBundle("commons.label.qualsiasi"), -1L));

				Domini dominiDars = new Domini();
				DominiHandler dominiHandler = (DominiHandler) dominiDars.getDarsHandler();

				if(findAll != null && findAll.size() > 0){
					for (Dominio dominio : findAll) {
						domini.add(new Voce<Long>(dominiHandler.getTitolo(dominio,bd), dominio.getId()));  
					}
				}
			} catch (ServiceException e) {
				throw new ConsoleException(e);
			}
			SelectList<Long> idDominio = (SelectList<Long>) infoRicercaMap.get(idDominioId);
			idDominio.setDefaultValue(-1L);
			idDominio.setValues(domini); 
			sezioneRoot.addField(idDominio);

			// codFlusso
			InputText codFlusso = (InputText) infoRicercaMap.get(codFlussoId);
			codFlusso.setDefaultValue(null);
			sezioneRoot.addField(codFlusso);


			// trn
			InputText trn = (InputText) infoRicercaMap.get(trnId);
			trn.setDefaultValue(null);
			sezioneRoot.addField(trn);

			// nascondi altri intermediari
			CheckButton nascondiAltriIntermediari = (CheckButton) infoRicercaMap.get(nascondiAltriIntermediariId);
			nascondiAltriIntermediari.setDefaultValue(false);
			sezioneRoot.addField(nascondiAltriIntermediari);

		}
		return infoRicerca;
	}

	private void initInfoRicerca(UriInfo uriInfo, BasicBD bd) throws ConsoleException{
		if(this.infoRicercaMap == null){
			this.infoRicercaMap = new HashMap<String, ParamField<?>>();

			String idDominioId = Utils.getInstance(this.getLanguage()).getMessageFromResourceBundle(this.nomeServizio + ".idDominio.id");
			String codFlussoId = Utils.getInstance(this.getLanguage()).getMessageFromResourceBundle(this.nomeServizio + ".codFlusso.id");
			String statoId = Utils.getInstance(this.getLanguage()).getMessageFromResourceBundle(this.nomeServizio + ".stato.id");
			String nascondiAltriIntermediariId = Utils.getInstance(this.getLanguage()).getMessageFromResourceBundle(this.nomeServizio + ".nascondiAltriIntermediari.id");
			String trnId = Utils.getInstance(this.getLanguage()).getMessageFromResourceBundle(this.nomeServizio + ".trn.id");

			// codFlusso
			String codFlussoLabel = Utils.getInstance(this.getLanguage()).getMessageFromResourceBundle(this.nomeServizio + ".codFlusso.label");
			InputText codFlusso = new InputText(codFlussoId, codFlussoLabel, null, false, false, true, 0, 35);
			this.infoRicercaMap.put(codFlussoId, codFlusso);

			// trn
			String trnLabel = Utils.getInstance().getMessageFromResourceBundle(this.nomeServizio + ".trn.label");
			InputText trn = new InputText(trnId, trnLabel, null, false, false, true, 0, 35);
			infoRicercaMap.put(trnId, trn);

			List<Voce<Long>> domini = new ArrayList<Voce<Long>>();
			// idDominio
			String idDominioLabel = Utils.getInstance(this.getLanguage()).getMessageFromResourceBundle(this.nomeServizio + ".idDominio.label");
			SelectList<Long> idDominio = new SelectList<Long>(idDominioId, idDominioLabel, null, false, false, true, domini);
			infoRicercaMap.put(idDominioId, idDominio);


			List<Voce<String>> stati = new ArrayList<Voce<String>>();
			// stato
			String statoLabel = Utils.getInstance(this.getLanguage()).getMessageFromResourceBundle(this.nomeServizio + ".stato.label");
			SelectList<String> stato = new SelectList<String>(statoId, statoLabel, null, false, false, true, stati);
			infoRicercaMap.put(statoId, stato);

			// nascondiAltriIntermediari
			String nascondiAltriIntermediariLabel = Utils.getInstance(this.getLanguage()).getMessageFromResourceBundle(this.nomeServizio + ".nascondiAltriIntermediari.label");
			CheckButton nascondiAltriIntermediari = new CheckButton(nascondiAltriIntermediariId, nascondiAltriIntermediariLabel, false, false, false, true);
			infoRicercaMap.put(nascondiAltriIntermediariId, nascondiAltriIntermediari);
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
			Fr fr = frBD.getFrExt(id);

			InfoForm infoModifica = null;
			URI cancellazione = null;
			URI esportazione = this.getUriEsportazioneDettaglio(uriInfo, bd, id);

			String titolo = fr != null ? this.getTitolo(fr,bd) : "";
			Dettaglio dettaglio = new Dettaglio(titolo, esportazione, cancellazione, infoModifica);

			it.govpay.web.rs.dars.model.Sezione root = dettaglio.getSezioneRoot();

			if(fr != null){

				// campi da visualizzare flusso, dominio, psp, trn, bic, data flusso, data regolamento, importo totale, sezione anomalie

				if(StringUtils.isNotEmpty(fr.getCodFlusso())) {
					root.addVoce(Utils.getInstance(this.getLanguage()).getMessageFromResourceBundle(this.nomeServizio + ".codFlusso.label"), fr.getCodFlusso());
				}

				// Uo
				Dominio dominio = fr.getDominio(bd);
				if(dominio != null) {
					Domini dominiDars = new Domini();
					DominiHandler dominiDarsHandler =  (DominiHandler) dominiDars.getDarsHandler();
					Elemento elemento = dominiDarsHandler.getElemento(dominio, dominio.getId(), dominiDars.getPathServizio(), bd);
					root.addVoce(Utils.getInstance(this.getLanguage()).getMessageFromResourceBundle(this.nomeServizio + ".dominio.label"), elemento.getTitolo(),elemento.getUri());
				}

				Psp psp = fr.getPsp(bd);
				if(psp != null) {
					root.addVoce(Utils.getInstance(this.getLanguage()).getMessageFromResourceBundle(this.nomeServizio + ".psp.label"),psp.getCodPsp());
				}

				if(StringUtils.isNotEmpty(fr.getIur())) {
					root.addVoce(Utils.getInstance(this.getLanguage()).getMessageFromResourceBundle(this.nomeServizio + ".trn.label"), fr.getIur());
				}

				if(StringUtils.isNotEmpty(fr.getCodBicRiversamento())) {
					root.addVoce(Utils.getInstance(this.getLanguage()).getMessageFromResourceBundle(this.nomeServizio + ".codBicRiversamento.label"), fr.getCodBicRiversamento());
				}

				if(fr.getDataFlusso() != null) {
					root.addVoce(Utils.getInstance(this.getLanguage()).getMessageFromResourceBundle(this.nomeServizio + ".dataFlusso.label"), this.sdf.format(fr.getDataFlusso()));
				}
				if(fr.getDataRegolamento() != null) {
					root.addVoce(Utils.getInstance(this.getLanguage()).getMessageFromResourceBundle(this.nomeServizio + ".dataRegolamento.label"), this.sdf.format(fr.getDataRegolamento()));
				}

				root.addVoce(Utils.getInstance(this.getLanguage()).getMessageFromResourceBundle(this.nomeServizio + ".importoTotalePagamenti.label"), fr.getImportoTotalePagamenti().doubleValue()+ "€");

				StatoFr stato = fr.getStato();
				if(stato!= null) {
					root.addVoce(Utils.getInstance(this.getLanguage()).getMessageFromResourceBundle(this.nomeServizio + ".stato.label"),
							Utils.getInstance(this.getLanguage()).getMessageFromResourceBundle(this.nomeServizio + ".stato."+stato.name()));
				}

				if(fr.getAnomalie()!= null && fr.getAnomalie().size() > 0) {
					String etichettaSezioneAnomalie = Utils.getInstance(this.getLanguage()).getMessageFromResourceBundle(this.nomeServizio + ".sezioneAnomalie.label");
					it.govpay.web.rs.dars.model.Sezione sezioneAnomalie = dettaglio.addSezione(etichettaSezioneAnomalie );

					for (Anomalia anomalia : fr.getAnomalie()) {
						sezioneAnomalie.addVoce(anomalia.getCodice(),anomalia.getDescrizione());
					}
				}

				//				root.addVoce(Utils.getInstance(this.getLanguage()).getMessageFromResourceBundle(this.nomeServizio + ".numeroPagamenti.label"), fr.getNumeroPagamenti()+ "");
				//
				//				int annoFlusso = Integer.parseInt(simpleDateFormatAnno.format(fr.getDataFlusso()));
				//				root.addVoce(Utils.getInstance(this.getLanguage()).getMessageFromResourceBundle(this.nomeServizio + ".annoRiferimento.label"), annoFlusso+"");
				//
				//				if(StringUtils.isNotEmpty(fr.getDescrizioneStato())) {
				//					root.addVoce(Utils.getInstance(this.getLanguage()).getMessageFromResourceBundle(this.nomeServizio + ".descrizioneStato.label"), fr.getDescrizioneStato());
				//				}
				//
				//				if(StringUtils.isNotEmpty(fr.getDescrizioneStato())) 
				//					root.addVoce(Utils.getInstance().getMessageFromResourceBundle(this.nomeServizio + ".descrizioneStato.label"), fr.getDescrizioneStato());
				//
				//				root.addVoce(Utils.getInstance().getMessageFromResourceBundle(this.nomeServizio + ".numRendicontazioniOk.label"), fr.getNumOk() + "");
				//				root.addVoce(Utils.getInstance().getMessageFromResourceBundle(this.nomeServizio + ".numRendicontazioniAnomale.label"), fr.getNumAnomale() + "");
				//				root.addVoce(Utils.getInstance().getMessageFromResourceBundle(this.nomeServizio + ".numRendicontazioniAltroIntermediario.label"), fr.getNumAltroIntermediario() +"");

				Rendicontazioni rendicontazioniDars = new Rendicontazioni();
				String etichettaPagamenti = Utils.getInstance(this.getLanguage()).getMessageFromResourceBundle(this.nomeServizio + ".elementoCorrelato.rendicontazioni.titolo");
				String idFrApplicazioneId = Utils.getInstance(this.getLanguage()).getMessageFromResourceBundle(rendicontazioniDars.getNomeServizio() + ".idFr.id");

				Map<String, String> params = new HashMap<String, String>();
				params.put(idFrApplicazioneId, fr.getId()+ "");
				URI rendicontazioneDettaglio = Utils.creaUriConParametri(rendicontazioniDars.getPathServizio(), params );
				dettaglio.addElementoCorrelato(etichettaPagamenti, rendicontazioneDettaglio); 
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
				Utils.getInstance(this.getLanguage()).getMessageWithParamsFromResourceBundle(this.nomeServizio + ".label.titolo",
						codFlusso,this.sdf.format(dataFlusso)));

		return sb.toString();
	}

	@Override
	public String getSottotitolo(Fr entry,BasicBD bd) {
		StringBuilder sb = new StringBuilder();
		StatoFr stato = entry.getStato();
		long numeroPagamenti = entry.getNumeroPagamenti();
		double importoTotalePagamenti = entry.getImportoTotalePagamenti().doubleValue();

		switch (stato) {
		case ACCETTATA:
			sb.append(
					Utils.getInstance(this.getLanguage()).getMessageWithParamsFromResourceBundle(this.nomeServizio + ".label.sottotitolo.accettata",
							Utils.getInstance(this.getLanguage()).getMessageFromResourceBundle(this.nomeServizio + ".stato.ACCETTATA"),
							numeroPagamenti,(importoTotalePagamenti + "€")));
			break;
		case ANOMALA:
		default:
			sb.append(
					Utils.getInstance(this.getLanguage()).getMessageWithParamsFromResourceBundle(this.nomeServizio + ".label.sottotitolo.anomala",
							Utils.getInstance(this.getLanguage()).getMessageFromResourceBundle(this.nomeServizio + ".stato.ANOMALA")
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
	public Map<String, Voce<String>> getVoci(Fr entry, BasicBD bd) throws ConsoleException { 
		Map<String, Voce<String>> voci = new HashMap<String, Voce<String>>();
		
		
		voci.put(Utils.getInstance(this.getLanguage()).getMessageFromResourceBundle(this.nomeServizio + ".stato.id"),
				new Voce<String>(Utils.getInstance(this.getLanguage()).getMessageFromResourceBundle(this.nomeServizio + ".stato."+entry.getStato().name()),
						entry.getStato().name()));
		
		if(StringUtils.isNotEmpty(entry.getCodFlusso())){
			voci.put(Utils.getInstance(this.getLanguage()).getMessageFromResourceBundle(this.nomeServizio + ".codFlusso.id"),
					new Voce<String>(Utils.getInstance(this.getLanguage()).getMessageFromResourceBundle(this.nomeServizio + ".codFlusso.label"),
							entry.getCodFlusso()));
		}

		if(StringUtils.isNotEmpty(entry.getIur())){
			voci.put(Utils.getInstance(this.getLanguage()).getMessageFromResourceBundle(this.nomeServizio + ".trn.id"),
					new Voce<String>(Utils.getInstance(this.getLanguage()).getMessageFromResourceBundle(this.nomeServizio + ".trn.label"),
							entry.getIur()));
		}

		voci.put(Utils.getInstance(this.getLanguage()).getMessageFromResourceBundle(this.nomeServizio + ".numeroPagamenti.id"),
				new Voce<String>(Utils.getInstance(this.getLanguage()).getMessageFromResourceBundle(this.nomeServizio + ".numeroPagamenti.label"),
						entry.getNumeroPagamenti()+""));
		voci.put(Utils.getInstance(this.getLanguage()).getMessageFromResourceBundle(this.nomeServizio + ".numRendicontazioniOk.id"),
				new Voce<String>(Utils.getInstance(this.getLanguage()).getMessageFromResourceBundle(this.nomeServizio + ".numRendicontazioniOk.label"),
						entry.getNumOk()+""));
		voci.put(Utils.getInstance(this.getLanguage()).getMessageFromResourceBundle(this.nomeServizio + ".numRendicontazioniAnomale.id"),
				new Voce<String>(Utils.getInstance(this.getLanguage()).getMessageFromResourceBundle(this.nomeServizio + ".numRendicontazioniAnomale.label"),
						entry.getNumAnomale()+""));
		voci.put(Utils.getInstance(this.getLanguage()).getMessageFromResourceBundle(this.nomeServizio + ".numRendicontazioniAltroIntermediario.id"),
				new Voce<String>(Utils.getInstance(this.getLanguage()).getMessageFromResourceBundle(this.nomeServizio + ".numRendicontazioniAltroIntermediario.label"),
						entry.getNumAltroIntermediario()+""));


		try{		
			// voci disponibili logo1, logo2, id flusso, idDominio, psp, trn, numero pagamenti, numero pagamenti anomali , numero pagamenti altri intermediari
			try{
				Dominio dominio = entry.getDominio(bd);
				if(dominio != null){
					Domini dominiDars = new Domini();
					DominiHandler dominiDarsHandler =  (DominiHandler) dominiDars.getDarsHandler();
					String dominioTitolo = dominiDarsHandler.getTitolo(dominio, bd);
					voci.put(Utils.getInstance(this.getLanguage()).getMessageFromResourceBundle(this.nomeServizio + ".idDominio.id"),
							new Voce<String>(Utils.getInstance(this.getLanguage()).getMessageFromResourceBundle(this.nomeServizio + ".idDominio.label"),
									dominioTitolo));
				}
			} catch (NotFoundException e) {
				// dominio non disponibile
			}

			try{
				Psp psp = entry.getPsp(bd);
				if(psp != null) {
					it.govpay.web.rs.dars.anagrafica.psp.Psp _psp = new it.govpay.web.rs.dars.anagrafica.psp.Psp();
					PspHandler pspHandler = (PspHandler) _psp.getDarsHandler();
					voci.put(Utils.getInstance(this.getLanguage()).getMessageFromResourceBundle(this.nomeServizio + ".psp.id"),
							new Voce<String>(Utils.getInstance(this.getLanguage()).getMessageFromResourceBundle(this.nomeServizio + ".psp.label"),
									pspHandler.getTitolo(psp, bd)));
				}
			} catch (NotFoundException e) {
				// psp non disponibile
			}

		} catch (ServiceException e) {
			throw new ConsoleException(e);
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
