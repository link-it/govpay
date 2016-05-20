package it.govpay.web.rs.dars.manutenzione.strumenti;

import java.io.InputStream;
import java.net.URI;
import java.util.List;
import java.util.zip.ZipOutputStream;

import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.UriBuilder;
import javax.ws.rs.core.UriInfo;

import org.apache.logging.log4j.Logger;
import org.openspcoop2.utils.resources.GestoreRisorseJMX;

import it.govpay.bd.BasicBD;
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
import it.govpay.web.utils.ConsoleProperties;
import it.govpay.web.utils.Utils;

public class StrumentiHandler extends BaseDarsHandler<Object> implements IDarsHandler<Object> {

	private static GestoreRisorseJMX gestoreJMX;

	public StrumentiHandler(Logger log, BaseDarsService darsService) {
		super(log, darsService);
		try {
			gestoreJMX = new GestoreRisorseJMX(org.apache.log4j.Logger.getLogger(StrumentiHandler.class));
		} catch (Exception e) {
			log.error("Errore nella inizializzazione del gestore JMX", e);
		}
	}

	@Override
	public Elenco getElenco(UriInfo uriInfo, BasicBD bd) throws WebApplicationException, ConsoleException {
		String methodName =  this.titoloServizio + ": getElencoOperazioni";
		try{
			this.log.info("Esecuzione " + methodName + " in corso..."); 
			// Operazione consentita solo all'amministratore
			this.darsService.checkOperatoreAdmin(bd);
			URI esportazione = null;
			URI cancellazione = null;
			long count = 0;

			Elenco elenco = new Elenco(this.titoloServizio, this.getInfoRicerca(uriInfo, bd),	this.getInfoCreazione(uriInfo, bd), count, esportazione, cancellazione); 

			UriBuilder uriDettaglioBuilder = BaseRsService.checkDarsURI(uriInfo).path(this.pathServizio).path("{id}");

			String[] listaOperazioni =  ConsoleProperties.getInstance().getOperazioniJMXDisponibili(); 

			for (int i = 0; i < listaOperazioni.length; i++) {
				String operazione = listaOperazioni[i];
				long idOperazione = i;
				String titoloOperazione = Utils.getInstance().getMessageFromResourceBundle(this.nomeServizio + "."+operazione+".titolo");
				String sottotitoloOperazione = Utils.getInstance().getMessageFromResourceBundle(this.nomeServizio + "."+operazione+".sottotitolo");
				URI urlDettaglio = uriDettaglioBuilder.build(idOperazione);
				Elemento elemento = new Elemento(idOperazione, titoloOperazione, sottotitoloOperazione, urlDettaglio);
				elenco.getElenco().add(elemento);
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
		String methodName = this.titoloServizio + "Esecuzione dell'operazione con id: "+ id;

		try{
			String[] listaOperazioni =  ConsoleProperties.getInstance().getOperazioniJMXDisponibili(); 
			String operazione = listaOperazioni[(int) id];

			String titoloOperazione = Utils.getInstance().getMessageFromResourceBundle(this.nomeServizio + "." + operazione + ".titolo");
			methodName = this.titoloServizio + "Esecuzione dell'operazione con id: "+ id +", CodiceOperazione: [" + titoloOperazione +"]";
			String nomeMetodo = Utils.getInstance().getMessageFromResourceBundle(this.nomeServizio + "." + operazione + ".nomeMetodo");

			this.log.info("Esecuzione " + methodName + " in corso...");
			// Operazione consentita solo all'amministratore
			this.darsService.checkOperatoreAdmin(bd);

			InfoForm infoModifica = null;
			URI cancellazione = null;
			URI esportazione = null;

			String dominio= ConsoleProperties.getInstance().getDominioOperazioniJMX();
			String tipo = ConsoleProperties.getInstance().getTipoOperazioniJMX();
			String nomeRisorsa = ConsoleProperties.getInstance().getNomeRisorsaOperazioniJMX();

			Dettaglio dettaglio = new Dettaglio(titoloOperazione, esportazione, cancellazione, infoModifica);
			it.govpay.web.rs.dars.model.Sezione root = dettaglio.getSezioneRoot();

			Object invoke = null;

			try{
				log.debug("Invocazione operazione ["+nomeMetodo +"] in corso..."); 
				invoke = gestoreJMX.invoke(dominio,tipo,nomeRisorsa,nomeMetodo , null, null);
				log.debug("Invocazione operazione ["+nomeMetodo +"] completata, ricevuta risposta ["+invoke+"]"); 
				
				if(invoke != null && invoke instanceof String){
					String esito = (String) invoke;
					String[] voci = esito.split("\\|");
					
					for (String string : voci) {
						String[] voce = string.split("#");
						root.addVoce(voce[0],voce[1]);
					}

				}
			}catch(Exception e){
				log.error("si e' verificato un errore durante l'esecuzione dell'operazione ["+nomeMetodo+"]: " + e.getMessage(),e); 
				root.addVoce(Utils.getInstance().getMessageFromResourceBundle(this.nomeServizio + ".operazione.esito"),
						Utils.getInstance().getMessageFromResourceBundle(this.nomeServizio + ".operazione.esito.ko"));
				root.addVoce(Utils.getInstance().getMessageFromResourceBundle(this.nomeServizio + ".operazione.esito.dettaglio"),
						e.getMessage());
			}

			this.log.info("Esecuzione " + methodName + " completata.");

			return dettaglio;
		}catch(WebApplicationException e){
			throw e;
		}catch(Exception e){
			throw new ConsoleException(e);
		}
	}

	/* Operazioni non consentite */

	@Override
	public InfoForm getInfoRicerca(UriInfo uriInfo, BasicBD bd) throws ConsoleException {	return null; }

	@Override
	public InfoForm getInfoCreazione(UriInfo uriInfo, BasicBD bd) throws ConsoleException {	return null;	}

	@Override
	public InfoForm getInfoModifica(UriInfo uriInfo, BasicBD bd, Object entry) throws ConsoleException {	return null;	}

	@Override
	public Object getField(UriInfo uriInfo, List<RawParamValue> values, String fieldId, BasicBD bd)	throws WebApplicationException, ConsoleException {		return null;	}

	@Override
	public void delete(List<Long> idsToDelete, UriInfo uriInfo, BasicBD bd)	throws WebApplicationException, ConsoleException {	}

	@Override
	public Object creaEntry(InputStream is, UriInfo uriInfo, BasicBD bd) throws WebApplicationException, ConsoleException {	return null;}

	@Override
	public Dettaglio insert(InputStream is, UriInfo uriInfo, BasicBD bd)throws WebApplicationException, ConsoleException, ValidationException, DuplicatedEntryException {	return null;	}

	@Override
	public void checkEntry(Object entry, Object oldEntry) throws ValidationException {}

	@Override
	public Dettaglio update(InputStream is, UriInfo uriInfo, BasicBD bd) throws WebApplicationException, ConsoleException, ValidationException { return null; }

	@Override
	public String getTitolo(Object entry,BasicBD bd) {	return null; }

	@Override
	public String getSottotitolo(Object entry,BasicBD bd) { return null; }

	@Override
	public String esporta(List<Long> idsToExport, UriInfo uriInfo, BasicBD bd, ZipOutputStream zout) throws WebApplicationException, ConsoleException { return null; }

	@Override
	public String esporta(Long idToExport, UriInfo uriInfo, BasicBD bd, ZipOutputStream zout) throws WebApplicationException, ConsoleException {	return null;	} 

}
