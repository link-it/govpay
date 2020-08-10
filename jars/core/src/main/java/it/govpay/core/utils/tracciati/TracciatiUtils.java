package it.govpay.core.utils.tracciati;

import java.io.BufferedWriter;
import java.io.ByteArrayOutputStream;
import java.util.Base64;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

import org.openspcoop2.utils.service.context.ContextThreadLocal;
import org.slf4j.Logger;

import it.govpay.bd.BasicBD;
import it.govpay.bd.anagrafica.AnagraficaManager;
import it.govpay.bd.model.Applicazione;
import it.govpay.bd.model.Documento;
import it.govpay.bd.model.Dominio;
import it.govpay.bd.model.Operazione;
import it.govpay.bd.model.Versamento;
import it.govpay.core.beans.EsitoOperazione;
import it.govpay.core.beans.tracciati.Avviso;
import it.govpay.core.business.model.PrintAvvisoDTOResponse;
import it.govpay.core.business.model.tracciati.CostantiCaricamento;
import it.govpay.core.business.model.tracciati.TrasformazioneDTOResponse;
import it.govpay.core.business.model.tracciati.operazioni.AbstractOperazioneResponse;
import it.govpay.core.business.model.tracciati.operazioni.AnnullamentoResponse;
import it.govpay.core.business.model.tracciati.operazioni.CaricamentoResponse;
import it.govpay.core.exceptions.GovPayException;
import it.govpay.core.utils.trasformazioni.TrasformazioniUtils;
import it.govpay.core.utils.trasformazioni.exception.TrasformazioneException;
import it.govpay.model.Operazione.StatoOperazioneType;
import it.govpay.orm.constants.StatoTracciatoType;

public class TracciatiUtils {

	public static void aggiornaCountOperazioniAdd(it.govpay.core.beans.tracciati.TracciatoPendenza beanDati, CaricamentoResponse caricamentoResponse, Operazione operazione) {
		if(operazione.getStato().equals(StatoOperazioneType.ESEGUITO_OK)) {
			beanDati.setNumAddOk(beanDati.getNumAddOk()+1);
		} else {
			if(!caricamentoResponse.getEsito().equals(CostantiCaricamento.EMPTY.toString())) {
				beanDati.setNumAddKo(beanDati.getNumAddKo()+1);
				beanDati.setDescrizioneStepElaborazione(caricamentoResponse.getDescrizioneEsito());
			}
		}
	}

	public static void aggiornaCountOperazioniDel(it.govpay.core.beans.tracciati.TracciatoPendenza beanDati, AnnullamentoResponse annullamentoResponse,
			Operazione operazione) {
		if(operazione.getStato().equals(StatoOperazioneType.ESEGUITO_OK)) {
			beanDati.setNumDelOk(beanDati.getNumDelOk()+1);
		} else {
			if(!annullamentoResponse.getEsito().equals(CostantiCaricamento.EMPTY)) {
				beanDati.setNumDelKo(beanDati.getNumDelKo()+1);
				beanDati.setDescrizioneStepElaborazione(annullamentoResponse.getDescrizioneEsito());
			}
		}
	}

	public static void setStatoDettaglioTracciato(it.govpay.core.beans.tracciati.TracciatoPendenza beanDati) {
		if((beanDati.getNumAddKo() + beanDati.getNumDelKo()) > 0) {
			beanDati.setStepElaborazione(StatoTracciatoType.CARICAMENTO_KO.getValue());
		} else {
			beanDati.setStepElaborazione(StatoTracciatoType.CARICAMENTO_OK.getValue());
		}
	}

	public static void setApplicazione(AbstractOperazioneResponse caricamentoResponse, Operazione operazione, BasicBD bd) {
		try {
			operazione.setIdApplicazione(AnagraficaManager.getApplicazione(bd, caricamentoResponse.getIdA2A()).getId());
		} catch(Exception e) {
			// CodApplicazione non censito in anagrafica.
		}
	}

	public static void setDescrizioneEsito(AbstractOperazioneResponse response, Operazione operazione) {
		if(response.getDescrizioneEsito() != null)
			operazione.setDettaglioEsito(response.getDescrizioneEsito().length() > 255 ? response.getDescrizioneEsito().substring(0, 255) : response.getDescrizioneEsito());
	}

	public static TrasformazioneDTOResponse trasformazioneInputCSV(Logger log, String codDominio, String codTipoVersamento, String lineaCSV, String tipoTemplate, String trasformazioneRichiesta) throws GovPayException {
		log.debug("Trasformazione Pendenza in formato CSV -> JSON tramite template freemarker ...");
		String name = "TrasformazionePendenzaCSVtoJSON";
		try {
			if(trasformazioneRichiesta.startsWith("\""))
				trasformazioneRichiesta = trasformazioneRichiesta.substring(1);

			if(trasformazioneRichiesta.endsWith("\""))
				trasformazioneRichiesta = trasformazioneRichiesta.substring(0, trasformazioneRichiesta.length() - 1);

			byte[] template = Base64.getDecoder().decode(trasformazioneRichiesta.getBytes());
			
			//log.debug("Template: "+ new String(template) );
			ByteArrayOutputStream baos = new ByteArrayOutputStream();
			Map<String, Object> dynamicMap = new HashMap<String, Object>();
			TrasformazioniUtils.fillDynamicMapRichiestaTracciatoCSV(log, dynamicMap, ContextThreadLocal.get(), lineaCSV, codDominio, codTipoVersamento);
			TrasformazioniUtils.convertFreeMarkerTemplate(name, template , dynamicMap , baos );
			// assegno il json trasformato
			log.debug("Trasformazione Pendenza in formato CSV -> JSON tramite template freemarker completata con successo.");
//			log.debug(baos.toString());
			return new TrasformazioneDTOResponse(baos.toString(), dynamicMap);
		} catch (TrasformazioneException e) {
			log.error("Trasformazione Pendenza in formato CSV -> JSON tramite template freemarker completata con errore: " + e.getMessage(), e);
			throw new GovPayException(e.getMessage(), EsitoOperazione.TRASFORMAZIONE, e, e.getMessage());
		}
	}

	public static void trasformazioneOutputCSV(Logger log, BufferedWriter bw, String codDominio, String codTipoVersamento, String jsonEsito, String tipoTemplate, byte[] template,
			String headerRisposta, Dominio dominio, Applicazione applicazione, Versamento versamento, Documento documento, String esitoOperazione, String descrizioneEsitoOperazione, String tipoOperazione) throws GovPayException {
		log.debug("Trasformazione esito caricamento pendenza in formato JSON -> CSV tramite template freemarker ...");
		String name = "TrasformazionePendenzaJSONtoCSV";
		try {
			Map<String, Object> dynamicMap = new HashMap<String, Object>();
			TrasformazioniUtils.fillDynamicMapRispostaTracciatoCSV(log, dynamicMap, ContextThreadLocal.get(), 
					headerRisposta, jsonEsito, codDominio, codTipoVersamento, dominio, applicazione, versamento, documento, esitoOperazione, descrizioneEsitoOperazione, tipoOperazione);
			TrasformazioniUtils.convertFreeMarkerTemplate(name, template , dynamicMap , bw );
			// assegno il json trasformato
			log.debug("Trasformazione esito caricamento pendenza JSON -> CSV tramite template freemarker completata con successo.");
//			log.debug("Linea ["+ baos.toString()+ "]");
		} catch (TrasformazioneException e) {
			log.error("Trasformazione esito caricamento pendenza JSON -> CSV tramite template freemarker completata con errore: " + e.getMessage(), e);
			throw new GovPayException(e.getMessage(), EsitoOperazione.TRASFORMAZIONE, e, e.getMessage());
		}
	}
	
	public static void aggiungiStampaAvviso(ZipOutputStream zos, Set<String> numeriAvviso, Set<String> numeriDocumento, CaricamentoResponse caricamentoResponse) throws java.io.IOException {
		if(caricamentoResponse.getStato().equals(StatoOperazioneType.ESEGUITO_OK)) {
			if(caricamentoResponse.getStampa() != null) {
				Avviso avviso = caricamentoResponse.getAvviso();
				String idDominio = avviso.getIdDominio();
				String numeroAvviso = avviso.getNumeroAvviso();
				String numeroDocumento = avviso.getNumeroDocumento();
				
				String pdfFileName = null;
				byte[] bytePdf = null;
				if(numeroDocumento != null) {
					// evito duplicati
					if(numeriDocumento.contains(idDominio + numeroDocumento)) return;
					
					numeriDocumento.add(idDominio + numeroDocumento);
					
					pdfFileName = idDominio + "_DOC_" + numeroDocumento + ".pdf"; 
					bytePdf = caricamentoResponse.getStampa().getPdf();
					
				} else {
					// Non tutte le pendenze caricate hanno il numero avviso
					// In questo caso posso saltare alla successiva.
					// Se lo hanno, controllo che non sia oggetto di una precedente generazione
					if(numeroAvviso == null || numeriAvviso.contains(idDominio + numeroAvviso)) return;
					
					numeriAvviso.add(idDominio + numeroAvviso);

					pdfFileName = idDominio + "_" + numeroAvviso + ".pdf"; 
					bytePdf = caricamentoResponse.getStampa().getPdf();
				}
				
				ZipEntry tracciatoOutputEntry = new ZipEntry(pdfFileName );
				zos.putNextEntry(tracciatoOutputEntry);
				zos.write(bytePdf);
				zos.flush();
				zos.closeEntry();
			}
		} 
	}
	
	public static void aggiungiStampaAvviso(ZipOutputStream zos, Set<String> numeriAvviso, Set<String> numeriDocumento, PrintAvvisoDTOResponse avviso, Logger log) throws java.io.IOException {
		String idDominio = avviso.getCodDominio();
		String numeroAvviso = avviso.getNumeroAvviso();
		String numeroDocumento = avviso.getCodDocumento();
		
		String pdfFileName = null;
		byte[] bytePdf = null;
		if(numeroDocumento != null) {
			// evito duplicati
			if(numeriDocumento.contains(idDominio + numeroDocumento)) return;
			
			log.debug("Aggiungo Documento ["+numeroDocumento+"] per il Dominio ["+idDominio+"]");
			
			numeriDocumento.add(idDominio + numeroDocumento);
			
			pdfFileName = idDominio + "_DOC_" + numeroDocumento + ".pdf"; 
			bytePdf = avviso.getAvviso().getPdf();
			
		} else {
			// Non tutte le pendenze caricate hanno il numero avviso
			// In questo caso posso saltare alla successiva.
			// Se lo hanno, controllo che non sia oggetto di una precedente generazione
			if(numeroAvviso == null || numeriAvviso.contains(idDominio + numeroAvviso)) return;
			
			numeriAvviso.add(idDominio + numeroAvviso);

			pdfFileName = idDominio + "_" + numeroAvviso + ".pdf"; 
			bytePdf = avviso.getAvviso().getPdf();
			
			log.debug("Aggiungo Avviso ["+numeroAvviso+"] per il Dominio ["+idDominio+"]");
		}
		
		ZipEntry tracciatoOutputEntry = new ZipEntry(pdfFileName );
		zos.putNextEntry(tracciatoOutputEntry);
		zos.write(bytePdf);
		zos.flush();
		zos.closeEntry();
		
		log.debug("Pdf inserito correttamente nello zip");
	}
}
