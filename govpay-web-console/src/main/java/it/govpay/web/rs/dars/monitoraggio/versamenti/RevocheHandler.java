package it.govpay.web.rs.dars.monitoraggio.versamenti;

import java.io.InputStream;
import java.math.BigDecimal;
import java.net.URI;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
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
import it.govpay.bd.model.Rr;
import it.govpay.bd.model.Rr.StatoRr;
import it.govpay.bd.pagamento.RrBD;
import it.govpay.bd.pagamento.filters.RrFilter;
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
import it.govpay.web.rs.dars.model.RawParamValue;
import it.govpay.web.utils.Utils;

public class RevocheHandler extends BaseDarsHandler<Rr> implements IDarsHandler<Rr>{

	private SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm"); 

	public RevocheHandler(Logger log, BaseDarsService darsService) {
		super(log, darsService);
	}

	@Override
	public Elenco getElenco(UriInfo uriInfo, BasicBD bd) throws WebApplicationException, ConsoleException {
		String methodName = "getElenco " + this.titoloServizio;
		try{	
			// Operazione consentita agli utenti registrati
			this.darsService.getOperatoreByPrincipal(bd); 

			URI esportazione = null; 
			URI cancellazione = null;

			this.log.info("Esecuzione " + methodName + " in corso...");

			String rptId = Utils.getInstance().getMessageFromResourceBundle(this.nomeServizio + ".idRpt.id");
			String idRpt = this.getParameter(uriInfo, rptId, String.class);

			RrBD rrBD = new RrBD(bd);
			RrFilter filter = rrBD.newFilter();
			FilterSortWrapper fsw = new FilterSortWrapper();
			fsw.setField(it.govpay.orm.RR.model().DATA_MSG_REVOCA);
			fsw.setSortOrder(SortOrder.DESC);
			filter.getFilterSortList().add(fsw);
			filter.setIdRpt(Long.parseLong(idRpt)); 

			long count = rrBD.count(filter);


			Elenco elenco = new Elenco(this.titoloServizio, this.getInfoRicerca(uriInfo, bd),
					this.getInfoCreazione(uriInfo, bd),
					count, esportazione, cancellazione); 

			UriBuilder uriDettaglioBuilder = BaseRsService.checkDarsURI(uriInfo).path(this.pathServizio).path("{id}");

			List<Rr> rr = rrBD.findAll(filter);


			if(rr != null && rr.size() > 0){
				for (Rr entry : rr) {
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

			RrBD rrBD = new RrBD(bd);
			Rr rr = rrBD.getRr(id);

			InfoForm infoModifica = null;
			URI cancellazione = null;
			URI esportazione = this.getUriEsportazioneDettaglio(uriInfo, bd, id);

			String titolo = this.getTitolo(rr,bd);
			Dettaglio dettaglio = new Dettaglio(titolo, esportazione, cancellazione, infoModifica);

			// Sezione RR
			it.govpay.web.rs.dars.model.Sezione sezioneRr = dettaglio.getSezioneRoot();
			String etichettaRr = Utils.getInstance().getMessageFromResourceBundle(this.nomeServizio + ".sezioneRR.titolo");
			sezioneRr.setEtichetta(etichettaRr); 

			StatoRr stato = rr.getStato(); 
			sezioneRr.addVoce(Utils.getInstance().getMessageFromResourceBundle(this.nomeServizio + ".stato.label"),
					Utils.getInstance().getMessageFromResourceBundle(this.nomeServizio + ".stato." + stato.name()));
			sezioneRr.addVoce(Utils.getInstance().getMessageFromResourceBundle(this.nomeServizio + ".iuv.label"),rr.getIuv());
			sezioneRr.addVoce(Utils.getInstance().getMessageFromResourceBundle(this.nomeServizio + ".ccp.label"),rr.getCcp());
			sezioneRr.addVoce(Utils.getInstance().getMessageFromResourceBundle(this.nomeServizio + ".codMsgRevoca.label"),rr.getCodMsgRevoca());
			if(rr.getDataMsgRevoca() != null)
				sezioneRr.addVoce(Utils.getInstance().getMessageFromResourceBundle(this.nomeServizio + ".dataMsgRevoca.label"),this.sdf.format(rr.getDataMsgRevoca()));
			
			sezioneRr.addVoce(Utils.getInstance().getMessageFromResourceBundle(this.nomeServizio + ".dominio.label"),rr.getCodDominio());
			sezioneRr.addVoce(Utils.getInstance().getMessageFromResourceBundle(this.nomeServizio + ".importoTotaleRichiesto.label"), rr.getImportoTotaleRichiesto() + "€");

			if(StringUtils.isNotEmpty(rr.getDescrizioneStato()))
				sezioneRr.addVoce(Utils.getInstance().getMessageFromResourceBundle(this.nomeServizio + ".descrizioneStato.label"),rr.getDescrizioneStato());

			// Singoli Er 
			String etichettaEr = Utils.getInstance().getMessageFromResourceBundle(this.nomeServizio + ".sezioneER.titolo");
			it.govpay.web.rs.dars.model.Sezione sezioneEr = dettaglio.addSezione(etichettaEr);
			if(rr.getDataMsgEsito()!= null){
				sezioneEr.addVoce(Utils.getInstance().getMessageFromResourceBundle(this.nomeServizio + ".dataMsgEsito.label"), this.sdf.format(rr.getDataMsgEsito()));
				sezioneEr.addVoce(Utils.getInstance().getMessageFromResourceBundle(this.nomeServizio + ".codMsgEsito.label"), rr.getCodMsgEsito());
				sezioneEr.addVoce(Utils.getInstance().getMessageFromResourceBundle(this.nomeServizio + ".importoTotaleRevocato.label"), rr.getImportoTotaleRevocato() + "€");
			}
			else	{
				sezioneEr.addVoce(Utils.getInstance().getMessageFromResourceBundle(this.nomeServizio + ".erAssente"), null);
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
		Date dataMsgRevoca = entry.getDataMsgRevoca();
		String iuv = entry.getIuv();
		String ccp = entry.getCcp();
		StringBuilder sb = new StringBuilder();

		String statoString = Utils.getInstance().getMessageWithParamsFromResourceBundle(this.nomeServizio + ".label.titolo", iuv , ccp, this.sdf.format(dataMsgRevoca)); 
		sb.append(statoString);	
		return sb.toString();
	}

	@Override
	public String getSottotitolo(Rr entry,BasicBD bd) {
		StringBuilder sb = new StringBuilder();
		StatoRr stato = entry.getStato();
		String statoString  = Utils.getInstance().getMessageFromResourceBundle(this.nomeServizio + ".stato." + stato.name());
		// ricevuta RT
		if(entry.getDataMsgEsito()!= null){
			BigDecimal importoTotalePagato = entry.getImportoTotaleRevocato();
			int compareTo = importoTotalePagato.compareTo(BigDecimal.ZERO);
			if(compareTo > 0){
				sb.append(Utils.getInstance().getMessageWithParamsFromResourceBundle(this.nomeServizio + ".label.sottotitolo.erPresente.importoPositivo",
						statoString, ( entry.getImportoTotaleRevocato() + "€")));
			} else{
				sb.append(Utils.getInstance().getMessageWithParamsFromResourceBundle(this.nomeServizio + ".label.sottotitolo.erPresente",statoString));
			}
				
		} else {
			sb.append(Utils.getInstance().getMessageWithParamsFromResourceBundle(this.nomeServizio + ".label.sottotitolo.erAssente",statoString));
		}
	 
		return sb.toString();
	}
	
	@Override
	public List<String> getValori(Rr entry, BasicBD bd) throws ConsoleException {
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

		String fileName = "RichiesteRevoca.zip";
		try{
			this.log.info("Esecuzione " + methodName + " in corso...");
			this.darsService.getOperatoreByPrincipal(bd); 

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
	public String esporta(Long idToExport, UriInfo uriInfo, BasicBD bd, ZipOutputStream zout)	throws WebApplicationException, ConsoleException {
		String methodName = "esporta " + this.titoloServizio + "[" + idToExport + "]";  


		try{
			this.log.info("Esecuzione " + methodName + " in corso...");
			this.darsService.getOperatoreByPrincipal(bd); 

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
	/* Operazioni non consentite */

	@Override
	public InfoForm getInfoRicerca(UriInfo uriInfo, BasicBD bd) throws ConsoleException { 	return null;	}

	@Override
	public InfoForm getInfoCreazione(UriInfo uriInfo, BasicBD bd) throws ConsoleException {		return null;	}

	@Override
	public InfoForm getInfoModifica(UriInfo uriInfo, BasicBD bd, Rr entry) throws ConsoleException {	return null;	}

	@Override
	public Object getField(UriInfo uriInfo, List<RawParamValue> values, String fieldId, BasicBD bd)	throws WebApplicationException, ConsoleException {	return null;	}

	@Override
	public void delete(List<Long> idsToDelete, UriInfo uriInfo, BasicBD bd)	throws WebApplicationException, ConsoleException {}

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
