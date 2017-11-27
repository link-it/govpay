package it.govpay.web.rs.dars.monitoraggio.versamenti;

import java.io.InputStream;
import java.math.BigDecimal;
import java.net.URI;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
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
import org.openspcoop2.generic_project.expression.SortOrder;

import it.govpay.bd.BasicBD;
import it.govpay.bd.FilterSortWrapper;
import it.govpay.bd.model.Rr;
import it.govpay.bd.pagamento.RrBD;
import it.govpay.bd.pagamento.filters.RrFilter;
import it.govpay.model.Rr.StatoRr;
import it.govpay.web.rs.dars.base.DarsHandler;
import it.govpay.web.rs.dars.base.DarsService;
import it.govpay.web.rs.dars.exception.ConsoleException;
import it.govpay.web.rs.dars.exception.DeleteException;
import it.govpay.web.rs.dars.exception.DuplicatedEntryException;
import it.govpay.web.rs.dars.exception.ExportException;
import it.govpay.web.rs.dars.exception.ValidationException;
import it.govpay.web.rs.dars.handler.IDarsHandler;
import it.govpay.web.rs.dars.model.DarsResponse.EsitoOperazione;
import it.govpay.web.rs.dars.model.Dettaglio;
import it.govpay.web.rs.dars.model.Elenco;
import it.govpay.web.rs.dars.model.InfoForm;
import it.govpay.web.rs.dars.model.RawParamValue;
import it.govpay.web.rs.dars.model.Voce;
import it.govpay.web.utils.Utils;

public class RevocheHandler extends DarsHandler<Rr> implements IDarsHandler<Rr>{

	private SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm"); 

	public RevocheHandler(Logger log, DarsService darsService) {
		super(log, darsService);
	}

	@Override
	public Elenco getElenco(UriInfo uriInfo, BasicBD bd) throws WebApplicationException, ConsoleException {
		String methodName = "getElenco " + this.titoloServizio;
		try{	
			// Operazione consentita solo agli utenti che hanno almeno un ruolo consentito per la funzionalita'
			this.darsService.checkDirittiServizio(bd, this.funzionalita);

			this.log.info("Esecuzione " + methodName + " in corso...");

			String rptId = Utils.getInstance(this.getLanguage()).getMessageFromResourceBundle(this.nomeServizio + ".idRpt.id");
			String idRpt = this.getParameter(uriInfo, rptId, String.class);

			RrBD rrBD = new RrBD(bd);
			RrFilter filter = rrBD.newFilter();
			FilterSortWrapper fsw = new FilterSortWrapper();
			fsw.setField(it.govpay.orm.RR.model().DATA_MSG_REVOCA);
			fsw.setSortOrder(SortOrder.DESC);
			filter.getFilterSortList().add(fsw);
			filter.setIdRpt(Long.parseLong(idRpt)); 

			long count = rrBD.count(filter);


			Map<String, String> params = new HashMap<String, String>();
			params.put(rptId, idRpt);
			Elenco elenco = new Elenco(this.titoloServizio, this.getInfoRicerca(uriInfo, bd,params),
					this.getInfoCreazione(uriInfo, bd),
					count, this.getInfoEsportazione(uriInfo, bd), this.getInfoCancellazione(uriInfo, bd)); 

			List<Rr> rr = rrBD.findAll(filter);

			if(rr != null && rr.size() > 0){
				for (Rr entry : rr) {
					elenco.getElenco().add(this.getElemento(entry, entry.getId(), this.pathServizio,bd));
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
			// Operazione consentita solo ai ruoli con diritto di lettura
			this.darsService.checkDirittiServizioLettura(bd, this.funzionalita);

			RrBD rrBD = new RrBD(bd);
			Rr rr = rrBD.getRr(id);

			InfoForm infoModifica = null;
			InfoForm infoCancellazione = this.getInfoCancellazioneDettaglio(uriInfo, bd, rr);
			InfoForm infoEsportazione = this.getInfoEsportazioneDettaglio(uriInfo, bd, rr);

			String titolo =  Utils.getInstance(this.getLanguage()).getMessageFromResourceBundle(this.nomeServizio + ".dettaglioRevoca");
			Dettaglio dettaglio = new Dettaglio(titolo, infoEsportazione, infoCancellazione, infoModifica);

			// Sezione RR
			it.govpay.web.rs.dars.model.Sezione sezioneRr = dettaglio.getSezioneRoot();
			String etichettaRr = Utils.getInstance(this.getLanguage()).getMessageFromResourceBundle(this.nomeServizio + ".sezioneRR.titolo");
			sezioneRr.setEtichetta(etichettaRr); 

			StatoRr stato = rr.getStato(); 
			sezioneRr.addVoce(Utils.getInstance(this.getLanguage()).getMessageFromResourceBundle(this.nomeServizio + ".stato.label"),
					Utils.getInstance(this.getLanguage()).getMessageFromResourceBundle(this.nomeServizio + ".stato." + stato.name()));
			sezioneRr.addVoce(Utils.getInstance(this.getLanguage()).getMessageFromResourceBundle(this.nomeServizio + ".iuv.label"),rr.getIuv());
			sezioneRr.addVoce(Utils.getInstance(this.getLanguage()).getMessageFromResourceBundle(this.nomeServizio + ".ccp.label"),rr.getCcp());
			sezioneRr.addVoce(Utils.getInstance(this.getLanguage()).getMessageFromResourceBundle(this.nomeServizio + ".codMsgRevoca.label"),rr.getCodMsgRevoca());
			if(rr.getDataMsgRevoca() != null)
				sezioneRr.addVoce(Utils.getInstance(this.getLanguage()).getMessageFromResourceBundle(this.nomeServizio + ".dataMsgRevoca.label"),this.sdf.format(rr.getDataMsgRevoca()));
			
			sezioneRr.addVoce(Utils.getInstance(this.getLanguage()).getMessageFromResourceBundle(this.nomeServizio + ".dominio.label"),rr.getCodDominio());
			sezioneRr.addVoce(Utils.getInstance(this.getLanguage()).getMessageFromResourceBundle(this.nomeServizio + ".importoTotaleRichiesto.label"), 
					this.currencyUtils.getCurrencyAsEuro(rr.getImportoTotaleRichiesto()));

			if(StringUtils.isNotEmpty(rr.getDescrizioneStato()))
				sezioneRr.addVoce(Utils.getInstance(this.getLanguage()).getMessageFromResourceBundle(this.nomeServizio + ".descrizioneStato.label"),rr.getDescrizioneStato());

			// Singoli Er 
			String etichettaEr = Utils.getInstance(this.getLanguage()).getMessageFromResourceBundle(this.nomeServizio + ".sezioneER.titolo");
			it.govpay.web.rs.dars.model.Sezione sezioneEr = dettaglio.addSezione(etichettaEr);
			if(rr.getDataMsgEsito()!= null){
				sezioneEr.addVoce(Utils.getInstance(this.getLanguage()).getMessageFromResourceBundle(this.nomeServizio + ".dataMsgEsito.label"), this.sdf.format(rr.getDataMsgEsito()));
				sezioneEr.addVoce(Utils.getInstance(this.getLanguage()).getMessageFromResourceBundle(this.nomeServizio + ".codMsgEsito.label"), rr.getCodMsgEsito());
				sezioneEr.addVoce(Utils.getInstance(this.getLanguage()).getMessageFromResourceBundle(this.nomeServizio + ".importoTotaleRevocato.label"),
						this.currencyUtils.getCurrencyAsEuro(rr.getImportoTotaleRevocato()));
			}
			else	{
				sezioneEr.addVoce(Utils.getInstance(this.getLanguage()).getMessageFromResourceBundle(this.nomeServizio + ".erAssente"), null);
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
	public String getTitolo(Rr entry,BasicBD bd) {
//		Date dataMsgRevoca = entry.getDataMsgRevoca();
//		String iuv = entry.getIuv();
//		String ccp = entry.getCcp();
		StringBuilder sb = new StringBuilder();
//
//		String statoString = Utils.getInstance(this.getLanguage()).getMessageWithParamsFromResourceBundle(this.nomeServizio + ".label.titolo", iuv , ccp, this.sdf.format(dataMsgRevoca)); 
		sb.append(Utils.getInstance(this.getLanguage()).getMessageFromResourceBundle(this.nomeServizio + ".label.titolo"));	
		return sb.toString();
	}

	@Override
	public String getSottotitolo(Rr entry,BasicBD bd) {
		StringBuilder sb = new StringBuilder();
		StatoRr stato = entry.getStato();
		String statoString  = Utils.getInstance(this.getLanguage()).getMessageFromResourceBundle(this.nomeServizio + ".stato." + stato.name());
		// ricevuta RT
		if(entry.getDataMsgEsito()!= null){
			BigDecimal importoTotalePagato = entry.getImportoTotaleRevocato();
			int compareTo = importoTotalePagato.compareTo(BigDecimal.ZERO);
			if(compareTo > 0){
				sb.append(Utils.getInstance(this.getLanguage()).getMessageWithParamsFromResourceBundle(this.nomeServizio + ".label.sottotitolo.erPresente.importoPositivo",
						statoString, this.currencyUtils.getCurrencyAsEuro( entry.getImportoTotaleRevocato())));
			} else{
				sb.append(Utils.getInstance(this.getLanguage()).getMessageWithParamsFromResourceBundle(this.nomeServizio + ".label.sottotitolo.erPresente",statoString));
			}
				
		} else {
			sb.append(Utils.getInstance(this.getLanguage()).getMessageWithParamsFromResourceBundle(this.nomeServizio + ".label.sottotitolo.erAssente",statoString));
		}
	 
		return sb.toString();
	}
	
	@Override
	public Map<String, Voce<String>> getVoci(Rr entry, BasicBD bd) throws ConsoleException { 
		Map<String, Voce<String>> valori = new HashMap<String, Voce<String>>();

		StatoRr stato = entry.getStato();

		String statoTransazione = Utils.getInstance(this.getLanguage()).getMessageFromResourceBundle(this.nomeServizio + ".statoTransazione.inCorso");
		switch(stato){
		case ER_ACCETTATA_PA:
			statoTransazione = Utils.getInstance(this.getLanguage()).getMessageFromResourceBundle(this.nomeServizio + ".statoTransazione.finaleOk");
			break;
		case RR_RIFIUTATA_NODO:
		case ER_RIFIUTATA_PA:
			statoTransazione = Utils.getInstance(this.getLanguage()).getMessageFromResourceBundle(this.nomeServizio + ".statoTransazione.finaleKo");
			break;
		case RR_ACCETTATA_NODO:
		case RR_ATTIVATA:
		case RR_ERRORE_INVIO_A_NODO:
		default:
			statoTransazione = Utils.getInstance(this.getLanguage()).getMessageFromResourceBundle(this.nomeServizio + ".statoTransazione.inCorso");
			break;
		}
		
		valori.put(Utils.getInstance(this.getLanguage()).getMessageFromResourceBundle(this.nomeServizio + ".statoText.id"),
				new Voce<String>(Utils.getInstance(this.getLanguage()).getMessageFromResourceBundle(this.nomeServizio + ".statoText.label"),
						statoTransazione));

		valori.put(Utils.getInstance(this.getLanguage()).getMessageFromResourceBundle(this.nomeServizio + ".statoTransazione.id"),
				new Voce<String>(Utils.getInstance(this.getLanguage()).getMessageFromResourceBundle(this.nomeServizio + ".stato." + stato.name()),
						statoTransazione));

		valori.put(Utils.getInstance(this.getLanguage()).getMessageFromResourceBundle(this.nomeServizio + ".tipo.id"),
				new Voce<String>(Utils.getInstance(this.getLanguage()).getMessageFromResourceBundle(this.nomeServizio + ".tipo.rr.label"),
						Utils.getInstance(this.getLanguage()).getMessageFromResourceBundle(this.nomeServizio + ".tipo.rr.value")));

		BigDecimal importoTotalePagato = entry.getImportoTotaleRevocato() != null ? entry.getImportoTotaleRevocato() : BigDecimal.ZERO;

		valori.put(Utils.getInstance(this.getLanguage()).getMessageFromResourceBundle(this.nomeServizio + ".importo.id"),
				new Voce<String>(Utils.getInstance(this.getLanguage()).getMessageFromResourceBundle(this.nomeServizio + ".importo.label"),
						this.currencyUtils.getCurrencyAsEuro(importoTotalePagato)));

		if(entry.getDataMsgRevoca()!= null){
			valori.put(Utils.getInstance(this.getLanguage()).getMessageFromResourceBundle(this.nomeServizio + ".data.id"),
					new Voce<String>(Utils.getInstance(this.getLanguage()).getMessageFromResourceBundle(this.nomeServizio + ".data.label"),
							this.sdf.format(entry.getDataMsgRevoca())));	 
		}

		if(entry.getIuv() != null){
			valori.put(Utils.getInstance(this.getLanguage()).getMessageFromResourceBundle(this.nomeServizio + ".iuv.id"),
					new Voce<String>(Utils.getInstance(this.getLanguage()).getMessageFromResourceBundle(this.nomeServizio + ".iuv.label"),entry.getIuv()));
		}

		if(entry.getCcp() != null){
			valori.put(Utils.getInstance(this.getLanguage()).getMessageFromResourceBundle(this.nomeServizio + ".ccp.id"),
					new Voce<String>(Utils.getInstance(this.getLanguage()).getMessageFromResourceBundle(this.nomeServizio + ".ccp.label"),entry.getCcp()));
		}
		
		return valori; 
	}

	@Override
	public String esporta(List<Long> idsToExport, List<RawParamValue> rawValues, UriInfo uriInfo, BasicBD bd, ZipOutputStream zout)
			throws WebApplicationException, ConsoleException,ExportException {
		StringBuffer sb = new StringBuffer();
		if(idsToExport != null && idsToExport.size() > 0)
			for (Long long1 : idsToExport) {

				if(sb.length() > 0)
					sb.append(", ");

				sb.append(long1);
			}

		String methodName = "esporta " + this.titoloServizio + "[" + sb.toString() + "]";
		
		if(idsToExport == null || idsToExport.size() == 0) {
			List<String> msg = new ArrayList<String>();
			msg.add(Utils.getInstance(this.getLanguage()).getMessageFromResourceBundle(this.nomeServizio+".esporta.erroreSelezioneVuota"));
			throw new ExportException(msg, EsitoOperazione.ERRORE);
		}

		if(idsToExport.size() == 1)
			return this.esporta(idsToExport.get(0), rawValues, uriInfo, bd, zout); 

		String fileName = "RichiesteRevoca.zip";
		try{
			this.log.info("Esecuzione " + methodName + " in corso...");
			// Operazione consentita solo ai ruoli con diritto di lettura
			this.darsService.checkDirittiServizioLettura(bd, this.funzionalita);

			RrBD rrBD = new RrBD(bd);

			for (Long idTransazione : idsToExport) {
				Rr rr = rrBD.getRr(idTransazione);
				String folderName = rr.getCodMsgRevoca();

				ZipEntry rrXml = new ZipEntry(folderName+"/rr.xml");
				zout.putNextEntry(rrXml);
				zout.write(rr.getXmlRr());
				zout.closeEntry();

				if(rr.getXmlEr() != null){
					ZipEntry rtXml = new ZipEntry(folderName+"/er.xml");
					zout.putNextEntry(rtXml);
					zout.write(rr.getXmlEr());
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
	public String esporta(Long idToExport, List<RawParamValue> rawValues,  UriInfo uriInfo, BasicBD bd, ZipOutputStream zout)
			throws WebApplicationException, ConsoleException,ExportException {
		String methodName = "esporta " + this.titoloServizio + "[" + idToExport + "]";  


		try{
			this.log.info("Esecuzione " + methodName + " in corso...");
			// Operazione consentita solo ai ruoli con diritto di lettura
			this.darsService.checkDirittiServizioLettura(bd, this.funzionalita);

			RrBD rrBD = new RrBD(bd);
			Rr rr = rrBD.getRr(idToExport);

			String fileName = "RichiestaRevoca_"+rr.getCodMsgRevoca()+".zip";

			ZipEntry rrXml = new ZipEntry("rr.xml");
			zout.putNextEntry(rrXml);
			zout.write(rr.getXmlRr());
			zout.closeEntry();

			if(rr.getXmlEr() != null){
				ZipEntry rtXml = new ZipEntry("er.xml");
				zout.putNextEntry(rtXml);
				zout.write(rr.getXmlEr());
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
		URI ricerca = this.getUriRicerca(uriInfo, bd,parameters);
		InfoForm infoRicerca = new InfoForm(ricerca);
		return infoRicerca;
	}
	
	/* Operazioni non consentite */

	@Override
	public InfoForm getInfoCancellazione(UriInfo uriInfo, BasicBD bd, Map<String, String> parameters) throws ConsoleException {
		return null;
	}
	
	@Override
	public InfoForm getInfoCancellazioneDettaglio(UriInfo uriInfo, BasicBD bd, Rr entry) throws ConsoleException {
		return null;
	}
	
	@Override
	public InfoForm getInfoEsportazione(UriInfo uriInfo, BasicBD bd, Map<String, String> parameters) throws ConsoleException { return null; }
	
	@Override
	public InfoForm getInfoEsportazioneDettaglio(UriInfo uriInfo, BasicBD bd, Rr entry)	throws ConsoleException {	return null;	}
	
	@Override
	public InfoForm getInfoCreazione(UriInfo uriInfo, BasicBD bd) throws ConsoleException {		return null;	}

	@Override
	public InfoForm getInfoModifica(UriInfo uriInfo, BasicBD bd, Rr entry) throws ConsoleException {	return null;	}

	@Override
	public Object getField(UriInfo uriInfo, List<RawParamValue> values, String fieldId, BasicBD bd)	throws WebApplicationException, ConsoleException {	return null;	}

	@Override
	public Object getSearchField(UriInfo uriInfo, List<RawParamValue> values, String fieldId, BasicBD bd)	throws WebApplicationException, ConsoleException { 	return null; }

	@Override
	public Object getDeleteField(UriInfo uriInfo, List<RawParamValue> values, String fieldId, BasicBD bd) throws WebApplicationException, ConsoleException { return null; }
	
	@Override
	public Object getExportField(UriInfo uriInfo, List<RawParamValue> values, String fieldId, BasicBD bd) throws WebApplicationException, ConsoleException { return null; }
	
	@Override
	public Elenco delete(List<Long> idsToDelete, List<RawParamValue> rawValues, UriInfo uriInfo, BasicBD bd) throws WebApplicationException, ConsoleException, DeleteException {	return null; 	}
	
	@Override
	public Rr creaEntry(InputStream is, UriInfo uriInfo, BasicBD bd) throws WebApplicationException, ConsoleException { return null;	}

	@Override
	public Dettaglio insert(InputStream is, UriInfo uriInfo, BasicBD bd) 	throws WebApplicationException, ConsoleException, ValidationException, DuplicatedEntryException {	return null;	}

	@Override
	public void checkEntry(Rr entry, Rr oldEntry) throws ValidationException {}

	@Override
	public Dettaglio update(InputStream is, UriInfo uriInfo, BasicBD bd) throws WebApplicationException, ConsoleException, ValidationException {		return null;	}

	@Override
	public Object uplaod(MultipartFormDataInput input, UriInfo uriInfo, BasicBD bd)	throws WebApplicationException, ConsoleException, ValidationException { return null;}

}
