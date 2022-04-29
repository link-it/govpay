package it.govpay.core.utils.stampe;

import java.io.UnsupportedEncodingException;
import java.math.BigDecimal;
import java.text.MessageFormat;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Properties;

import org.apache.commons.lang.StringUtils;
import org.openspcoop2.generic_project.exception.ServiceException;
import org.openspcoop2.utils.UtilsException;
import org.openspcoop2.utils.service.context.ContextThreadLocal;
import org.slf4j.Logger;

import it.govpay.bd.BDConfigWrapper;
import it.govpay.bd.model.Documento;
import it.govpay.bd.model.Dominio;
import it.govpay.bd.model.SingoloVersamento;
import it.govpay.bd.model.UnitaOperativa;
import it.govpay.bd.model.Versamento;
import it.govpay.core.beans.tracciati.LinguaSecondaria;
import it.govpay.core.beans.tracciati.ProprietaPendenza;
import it.govpay.core.business.model.PrintAvvisoDocumentoDTO;
import it.govpay.core.business.model.PrintAvvisoVersamentoDTO;
import it.govpay.core.exceptions.UnprocessableEntityException;
import it.govpay.core.utils.IuvUtils;
import it.govpay.core.utils.LabelAvvisiProperties;
import it.govpay.core.utils.VersamentoUtils;
import it.govpay.model.Anagrafica;
import it.govpay.model.IbanAccredito;
import it.govpay.stampe.model.v2.AvvisoPagamentoInput;
import it.govpay.stampe.model.v2.Etichette;
import it.govpay.stampe.model.v2.PaginaAvvisoDoppia;
import it.govpay.stampe.model.v2.PaginaAvvisoSingola;
import it.govpay.stampe.model.v2.PagineAvviso;
import it.govpay.stampe.model.v2.RataAvviso;
import it.govpay.stampe.pdf.avvisoPagamento.AvvisoPagamentoCostanti;

public class AvvisoPagamentoV2Utils {

	
	public static AvvisoPagamentoInput fromVersamento(PrintAvvisoVersamentoDTO printAvviso, LinguaSecondaria secondaLinguaScelta) throws ServiceException, UtilsException {
		it.govpay.bd.model.Versamento versamento = printAvviso.getVersamento();
		AvvisoPagamentoInput input = new AvvisoPagamentoInput();
		BDConfigWrapper configWrapper = new BDConfigWrapper(ContextThreadLocal.get().getTransactionId(), true);
		
		
		it.govpay.stampe.model.v2.AvvisoPagamentoInput.Etichette etichettes = new it.govpay.stampe.model.v2.AvvisoPagamentoInput.Etichette();
		etichettes.setItaliano(getEtichetteItaliano());
		etichettes.setTraduzione(getEtichetteTraduzione(secondaLinguaScelta));
		input.setEtichette(etichettes);
		
		String causaleVersamento = "";
		if(versamento.getCausaleVersamento() != null) {
			try {
				causaleVersamento = versamento.getCausaleVersamento().getSimple();
				input.getEtichette().getItaliano().setOggettoDelPagamento(causaleVersamento);
			}catch (UnsupportedEncodingException e) {
				throw new ServiceException(e);
			}
		}
		
		// causale nella seconda lingua
		if(input.getEtichette().getTraduzione() != null && secondaLinguaScelta != null ) {
			ProprietaPendenza proprieta = versamento.getProprietaPendenza();
			if(proprieta != null && StringUtils.isNotBlank(proprieta.getLinguaSecondariaCausale())) {
				input.getEtichette().getTraduzione().setOggettoDelPagamento(proprieta.getLinguaSecondariaCausale());
			}
		}

		AvvisoPagamentoV2Utils.impostaAnagraficaEnteCreditore(versamento, versamento.getDominio(configWrapper), versamento.getUo(configWrapper), input);
		AvvisoPagamentoV2Utils.impostaAnagraficaDebitore(versamento.getAnagraficaDebitore(), input);

		PaginaAvvisoSingola pagina = new PaginaAvvisoSingola();
		pagina.setRata(getRata(versamento, input, secondaLinguaScelta, printAvviso.getSdfDataScadenza()));
		
		if(input.getPagine() == null)
			input.setPagine(new PagineAvviso());

		input.getPagine().getSingolaOrDoppia().add(pagina);
		
		input.getEtichette().getItaliano().setNota1(getLabel(LabelAvvisiProperties.DEFAULT_PROPS, LabelAvvisiProperties.LABEL_NOTA_IMPORTO));
		
		if(input.getEtichette().getTraduzione() != null && secondaLinguaScelta != null ) {
			input.getEtichette().getTraduzione().setNota1(getLabel(secondaLinguaScelta.toString(), LabelAvvisiProperties.LABEL_NOTA_IMPORTO));
		}

		return input;
	} 

	public static AvvisoPagamentoInput fromDocumento(PrintAvvisoDocumentoDTO printAvviso, List<Versamento> versamenti, LinguaSecondaria secondaLinguaScelta, Logger log) throws ServiceException, UnprocessableEntityException, UtilsException { 
		Documento documento = printAvviso.getDocumento();
		AvvisoPagamentoInput input = new AvvisoPagamentoInput();
		BDConfigWrapper configWrapper = new BDConfigWrapper(ContextThreadLocal.get().getTransactionId(), true);
		SimpleDateFormat sdfDataScadenza = printAvviso.getSdfDataScadenza();
		
		it.govpay.stampe.model.v2.AvvisoPagamentoInput.Etichette etichettes = new it.govpay.stampe.model.v2.AvvisoPagamentoInput.Etichette();
		etichettes.setItaliano(getEtichetteItaliano());
		etichettes.setTraduzione(getEtichetteTraduzione(secondaLinguaScelta));
		input.setEtichette(etichettes);
		
		
		input.getEtichette().getItaliano().setOggettoDelPagamento(documento.getDescrizione());
		
		// causale nella seconda lingua
		if(input.getEtichette().getTraduzione() != null && secondaLinguaScelta != null && versamenti.size() > 0) {
			Versamento versamento = versamenti.get(0); // leggo alcuni dati dalla prima rata
			ProprietaPendenza proprieta = versamento.getProprietaPendenza();
			if(proprieta != null && StringUtils.isNotBlank(proprieta.getLinguaSecondariaCausale())) {
				input.getEtichette().getTraduzione().setOggettoDelPagamento(proprieta.getLinguaSecondariaCausale());
			}
		}

		if(input.getPagine() == null)
			input.setPagine(new PagineAvviso());
		
		log.debug("Documento ["+documento.getCodDocumento()+"] Numero totale di versamenti da inserire: " + versamenti.size());
		
		// pagina principale
		while(versamenti.size() > 0 && versamenti.get(0).getNumeroRata() == null && versamenti.get(0).getTipoSoglia() == null) {
			Versamento versamento = versamenti.remove(0);
			log.debug("Inserisco versamento senza rata o soglia [IDA2A: "+versamento.getApplicazione(configWrapper)+", IdPendenza: "+versamento.getCodVersamentoEnte()+"]");
			AvvisoPagamentoV2Utils.impostaAnagraficaEnteCreditore(versamento, documento.getDominio(configWrapper), versamento.getUo(configWrapper), input);
			AvvisoPagamentoV2Utils.impostaAnagraficaDebitore(versamento.getAnagraficaDebitore(), input);
			PaginaAvvisoSingola pagina = new PaginaAvvisoSingola();
			pagina.setRata(getRata(versamento, input, secondaLinguaScelta, sdfDataScadenza));
			input.getPagine().getSingolaOrDoppia().add(pagina);
		}
		
		log.debug("Documento ["+documento.getCodDocumento()+"] numero di versamenti da inserire dopo la pagina principale: " + versamenti.size());
		
		boolean addNota1 = true;
		
		// calcolo il numero delle rate
		int numeroRate = 0;
		for (Versamento versamento : versamenti) {
			if(versamento.getNumeroRata() != null) {
				numeroRate ++;
			}
		}
		
		log.debug("Documento ["+documento.getCodDocumento()+"] numero di versamenti con rate da inserire: " + numeroRate);	
		
		// questo controllo bisogna farlo all'inizio perche' la procedura carica la rata unica togliendola dall'elenco versamenti.
		boolean soloRate = numeroRate == versamenti.size();
		
		// calcolo dei versamenti con soglia
		int numeroSoglia = 0;
		for (Versamento versamento : versamenti) {
			if(versamento.getTipoSoglia() != null) {
				numeroSoglia ++;
			}
		}
		
		log.debug("Documento ["+documento.getCodDocumento()+"] numero di versamenti con soglie da inserire: " + numeroSoglia);	
		
		// questo controllo bisogna farlo all'inizio perche' la procedura carica la rata unica togliendola dall'elenco versamenti.
		boolean soloSoglie = numeroSoglia == versamenti.size();

        // se ho tutte rate non sono entrato sicuramente nell'if precedente e devo aggiungere la pagina principale
		if(versamenti.size() > 0 && soloRate) {
			// numero di versamenti pari devo creara la pagina principale con i dati della prima rata
			if(versamenti.size() % 2 == 0) {
				log.debug("Documento ["+documento.getCodDocumento()+"] numero di versamenti con rate e' pari, riporto i dati della prima rata anche nella pagina principale.");	
				PaginaAvvisoSingola pagina = new PaginaAvvisoSingola();
				Versamento versamento = versamenti.get(0); // leggo alcuni dati dalla prima rata
				
				AvvisoPagamentoV2Utils.impostaAnagraficaEnteCreditore(versamento, documento.getDominio(configWrapper), versamento.getUo(configWrapper), input);
				AvvisoPagamentoV2Utils.impostaAnagraficaDebitore(versamento.getAnagraficaDebitore(), input);
				
				input.getEtichette().getItaliano().setNota1(getLabel(LabelAvvisiProperties.DEFAULT_PROPS, LabelAvvisiProperties.LABEL_PRIMA_RATA));
				
				if(input.getEtichette().getTraduzione() != null && secondaLinguaScelta != null ) {
					input.getEtichette().getTraduzione().setNota1(getLabel(secondaLinguaScelta.toString(), LabelAvvisiProperties.LABEL_PRIMA_RATA));
				}
				
				addNota1 = false;
				
				RataAvviso rata = new RataAvviso();
				if(versamento.getDataValidita() != null) {
					rata.setData(sdfDataScadenza.format(versamento.getDataValidita()));
				} else if(versamento.getDataScadenza() != null) {
					rata.setData(sdfDataScadenza.format(versamento.getDataScadenza()));
				} else {
					rata.setData("-"); 
				}
				
				// calcolo dell'importo totale
				BigDecimal importoTotale = BigDecimal.ZERO;
				for (Versamento vTmp : versamenti) {
					importoTotale = importoTotale.add(vTmp.getImportoTotale());
				}
				rata.setImporto(importoTotale.doubleValue());
				
				pagina.setRata(rata);
				
				input.getPagine().getSingolaOrDoppia().add(pagina);
			} else { // versamenti dispari la prima pagina e la prima rata coincidono
				log.debug("Documento ["+documento.getCodDocumento()+"] numero di versamenti con rate e' dispari, la prima pagina coincide con la prima rata.");	
				Versamento versamento = versamenti.remove(0);
				AvvisoPagamentoV2Utils.impostaAnagraficaEnteCreditore(versamento, documento.getDominio(configWrapper), versamento.getUo(configWrapper), input);
				AvvisoPagamentoV2Utils.impostaAnagraficaDebitore(versamento.getAnagraficaDebitore(), input);
				PaginaAvvisoSingola pagina = new PaginaAvvisoSingola();
				
				RataAvviso rata = getRata(versamento, input, secondaLinguaScelta, sdfDataScadenza);
				
				rata.setScadenza(getLabel(LabelAvvisiProperties.DEFAULT_PROPS, LabelAvvisiProperties.LABEL_PRIMA_RATA));
				if(secondaLinguaScelta != null)
					rata.setScadenzaTra(getLabel(secondaLinguaScelta.toString(), LabelAvvisiProperties.LABEL_PRIMA_RATA));
				
				input.getEtichette().getItaliano().setEntro(getLabel(LabelAvvisiProperties.DEFAULT_PROPS, LabelAvvisiProperties.LABEL_ENTRO_IL));
				
				if(input.getEtichette().getTraduzione() != null && secondaLinguaScelta != null ) {
					input.getEtichette().getTraduzione().setEntro(getLabel(secondaLinguaScelta.toString(), LabelAvvisiProperties.LABEL_ENTRO_IL));
				}
				
				pagina.setRata(rata);
				input.getPagine().getSingolaOrDoppia().add(pagina);
			}
		}
		
		if(numeroRate > 0) {
			input.getEtichette().getItaliano().setNota1(getLabel(LabelAvvisiProperties.DEFAULT_PROPS, LabelAvvisiProperties.LABEL_NOTA_PRIMA_RATA, numeroRate));
			
			if(input.getEtichette().getTraduzione() != null && secondaLinguaScelta != null ) {
				input.getEtichette().getTraduzione().setNota1(getLabel(secondaLinguaScelta.toString(), LabelAvvisiProperties.LABEL_NOTA_PRIMA_RATA, numeroRate));
			}
			addNota1 = false;
		}
		
		// ho tutti pagamenti con soglia
		if(versamenti.size() > 0 && soloSoglie) {
			// numero di versamenti pari devo creara la pagina principale con i dati della prima rata
			if(versamenti.size() % 2 == 0) {
				log.debug("Documento ["+documento.getCodDocumento()+"] numero di versamenti con soglie, riporto i dati della prima soglia anche nella pagina principale.");	
				PaginaAvvisoSingola pagina = new PaginaAvvisoSingola();
				Versamento versamento = versamenti.get(0); // leggo alcuni dati dalla prima rata
				
				AvvisoPagamentoV2Utils.impostaAnagraficaEnteCreditore(versamento, documento.getDominio(configWrapper), versamento.getUo(configWrapper), input);
				AvvisoPagamentoV2Utils.impostaAnagraficaDebitore(versamento.getAnagraficaDebitore(), input);
				
				RataAvviso rata = new RataAvviso();
				if(versamento.getDataValidita() != null) {
					rata.setData(sdfDataScadenza.format(versamento.getDataValidita()));
				} else if(versamento.getDataScadenza() != null) {
					rata.setData(sdfDataScadenza.format(versamento.getDataScadenza()));
				} else {
					rata.setData("-"); 
				}
				
				switch (versamento.getTipoSoglia()) {
				case ENTRO:
					rata.setScadenza(getLabel(LabelAvvisiProperties.DEFAULT_PROPS, LabelAvvisiProperties.LABEL_ENTRO, versamento.getGiorniSoglia()));
					if(secondaLinguaScelta != null)
						rata.setScadenzaTra(getLabel(secondaLinguaScelta.toString(), LabelAvvisiProperties.LABEL_ENTRO, versamento.getGiorniSoglia()));
					break;
				case OLTRE:
					rata.setScadenza(getLabel(LabelAvvisiProperties.DEFAULT_PROPS, LabelAvvisiProperties.LABEL_OLTRE, versamento.getGiorniSoglia()));
					if(secondaLinguaScelta != null)
						rata.setScadenzaTra(getLabel(secondaLinguaScelta.toString(), LabelAvvisiProperties.LABEL_OLTRE, versamento.getGiorniSoglia()));
					break;
				}
				
				rata.setImporto(versamento.getImportoTotale().doubleValue());
				
				pagina.setRata(rata);
				input.getPagine().getSingolaOrDoppia().add(pagina);
			} else {  // versamenti dispari la prima pagina e la prima soglia coincidono
				log.debug("Documento ["+documento.getCodDocumento()+"] numero di versamenti con soglie e' dispari, la prima pagina coincide con la prima soglia.");	
				Versamento versamento = versamenti.remove(0);
				AvvisoPagamentoV2Utils.impostaAnagraficaEnteCreditore(versamento, documento.getDominio(configWrapper), versamento.getUo(configWrapper), input);
				AvvisoPagamentoV2Utils.impostaAnagraficaDebitore(versamento.getAnagraficaDebitore(), input);
				PaginaAvvisoSingola pagina = new PaginaAvvisoSingola();
				pagina.setRata(getRata(versamento, input, secondaLinguaScelta, sdfDataScadenza));
				input.getPagine().getSingolaOrDoppia().add(pagina);
			}
		}
		
		
		log.debug("Documento ["+documento.getCodDocumento()+"] inserisco i versamenti due per pagina");	
		// 2 rate per pagina
		while(versamenti.size() > 1) {
			Versamento v1 = versamenti.remove(0);
			Versamento v2 = versamenti.remove(0);
			AvvisoPagamentoV2Utils.impostaAnagraficaEnteCreditore(v2, documento.getDominio(configWrapper), v2.getUo(configWrapper), input);
			AvvisoPagamentoV2Utils.impostaAnagraficaDebitore(v2.getAnagraficaDebitore(), input);
			PaginaAvvisoDoppia pagina = new PaginaAvvisoDoppia();
			RataAvviso rataSx = getRata(v1, input, secondaLinguaScelta, sdfDataScadenza);
			RataAvviso rataDx = getRata(v2, input, secondaLinguaScelta, sdfDataScadenza);
			
			if(v1.getNumeroRata() != null && v2.getNumeroRata() != null) {
				// Titolo della pagina con 2 Rate
				String titoloRateIta = getLabel(LabelAvvisiProperties.DEFAULT_PROPS, LabelAvvisiProperties.LABEL_ELENCO_RATE_2, v1.getNumeroRata(), v2.getNumeroRata());
				rataSx.setElencoRate(titoloRateIta);
				rataDx.setElencoRate(titoloRateIta);
				if(secondaLinguaScelta != null) {
					String titoloRateSL = getLabel(secondaLinguaScelta.toString(), LabelAvvisiProperties.LABEL_ELENCO_RATE_2, v1.getNumeroRata(), v2.getNumeroRata());
					rataSx.setElencoRateTra(titoloRateSL);
					rataDx.setElencoRateTra(titoloRateSL);
				}
			}
			
			pagina.getRata().add(rataSx);
			pagina.getRata().add(rataDx);
			input.getPagine().getSingolaOrDoppia().add(pagina);
		}

		log.debug("Documento ["+documento.getCodDocumento()+"] inserisco i versamenti residui uno per pagina");	
		// rata rimasta
		if(versamenti.size() == 1) {
			Versamento versamento = versamenti.remove(0);
			AvvisoPagamentoV2Utils.impostaAnagraficaEnteCreditore(versamento, documento.getDominio(configWrapper), versamento.getUo(configWrapper), input);
			AvvisoPagamentoV2Utils.impostaAnagraficaDebitore(versamento.getAnagraficaDebitore(), input);
			PaginaAvvisoDoppia pagina = new PaginaAvvisoDoppia();
			RataAvviso rataSx = getRata(versamento, input, secondaLinguaScelta, sdfDataScadenza);
			
			if(versamento.getNumeroRata() != null) {
				// Titolo della pagina con 2 Rate
				String titoloRateIta = getLabel(LabelAvvisiProperties.DEFAULT_PROPS, LabelAvvisiProperties.LABEL_ELENCO_RATE_1, versamento.getNumeroRata());
				rataSx.setElencoRate(titoloRateIta);
				if(secondaLinguaScelta != null) {
					String titoloRateSL = getLabel(secondaLinguaScelta.toString(), LabelAvvisiProperties.LABEL_ELENCO_RATE_1, versamento.getNumeroRata());
					rataSx.setElencoRateTra(titoloRateSL);
				}
			}
			
			pagina.getRata().add(rataSx);
			
			input.getPagine().getSingolaOrDoppia().add(pagina);
		}
		
		
		if(addNota1) {
			input.getEtichette().getItaliano().setNota1(getLabel(LabelAvvisiProperties.DEFAULT_PROPS, LabelAvvisiProperties.LABEL_NOTA_IMPORTO));
			
			if(input.getEtichette().getTraduzione() != null && secondaLinguaScelta != null ) {
				input.getEtichette().getTraduzione().setNota1(getLabel(secondaLinguaScelta.toString(), LabelAvvisiProperties.LABEL_NOTA_IMPORTO));
			}
		} else {
			input.getEtichette().getItaliano().setNota2(getLabel(LabelAvvisiProperties.DEFAULT_PROPS, LabelAvvisiProperties.LABEL_NOTA_IMPORTO));
			
			if(input.getEtichette().getTraduzione() != null && secondaLinguaScelta != null ) {
				input.getEtichette().getTraduzione().setNota2(getLabel(secondaLinguaScelta.toString(), LabelAvvisiProperties.LABEL_NOTA_IMPORTO));
			}
		}
		
		log.debug("Documento ["+documento.getCodDocumento()+"] procedura creazione pagine completata");

		return input;
	}

	public static RataAvviso getRata(it.govpay.bd.model.Versamento versamento, AvvisoPagamentoInput input, LinguaSecondaria secondaLinguaScelta, SimpleDateFormat sdfDataScadenza) throws ServiceException, UtilsException {
		BDConfigWrapper configWrapper = new BDConfigWrapper(ContextThreadLocal.get().getTransactionId(), true);
		RataAvviso rata = new RataAvviso();
		
		rata.setScadenza(getLabel(LabelAvvisiProperties.DEFAULT_PROPS, LabelAvvisiProperties.LABEL_RATA_UNICA_ENTRO_IL));
		if(secondaLinguaScelta != null)
			rata.setScadenzaTra(getLabel(secondaLinguaScelta.toString(), LabelAvvisiProperties.LABEL_RATA_UNICA_ENTRO_IL));
		
		
		if(versamento.getNumeroRata() != null) {
			rata.setNumeroRata(getLabel(LabelAvvisiProperties.DEFAULT_PROPS, LabelAvvisiProperties.LABEL_NUMERO_RATA, versamento.getNumeroRata()));
			if(secondaLinguaScelta != null)
				rata.setNumeroRataTra(getLabel(secondaLinguaScelta.toString(), LabelAvvisiProperties.LABEL_NUMERO_RATA, versamento.getNumeroRata()));
			
			rata.setScadenza(getLabel(LabelAvvisiProperties.DEFAULT_PROPS, LabelAvvisiProperties.LABEL_RATA_ENTRO_IL, versamento.getNumeroRata()));
			if(secondaLinguaScelta != null)
				rata.setScadenzaTra(getLabel(secondaLinguaScelta.toString(), LabelAvvisiProperties.LABEL_RATA_ENTRO_IL, versamento.getNumeroRata()));
		}

		boolean addDataValidita = true;
		if(versamento.getGiorniSoglia() != null && versamento.getTipoSoglia() != null) {
			
			switch (versamento.getTipoSoglia()) {
			case ENTRO:
				rata.setScadenza(getLabel(LabelAvvisiProperties.DEFAULT_PROPS, LabelAvvisiProperties.LABEL_ENTRO, versamento.getGiorniSoglia()));
				if(secondaLinguaScelta != null)
					rata.setScadenzaTra(getLabel(secondaLinguaScelta.toString(), LabelAvvisiProperties.LABEL_ENTRO, versamento.getGiorniSoglia()));
				break;
			case OLTRE:
				rata.setScadenza(getLabel(LabelAvvisiProperties.DEFAULT_PROPS, LabelAvvisiProperties.LABEL_OLTRE, versamento.getGiorniSoglia()));
				if(secondaLinguaScelta != null)
					rata.setScadenzaTra(getLabel(secondaLinguaScelta.toString(), LabelAvvisiProperties.LABEL_OLTRE, versamento.getGiorniSoglia()));
				break;
			}
			
			addDataValidita = false; 
		}

		List<SingoloVersamento> singoliVersamenti = versamento.getSingoliVersamenti(configWrapper);
		SingoloVersamento sv = singoliVersamenti.get(0);

		IbanAccredito postale = null;

		if(sv.getIbanAccredito(configWrapper) != null && sv.getIbanAccredito(configWrapper).isPostale())
			postale = sv.getIbanAccredito(configWrapper);
		else if(sv.getIbanAppoggio(configWrapper) != null && sv.getIbanAppoggio(configWrapper).isPostale())
			postale = sv.getIbanAppoggio(configWrapper);
		
		if(versamento.getNumeroAvviso() != null) {
			// split del numero avviso a gruppi di 4 cifre
			StringBuilder sb = new StringBuilder();
			for (int i = 0; i < versamento.getNumeroAvviso().length(); i++) {
				if(sb.length() > 0 && (i % 4 == 0)) {
					sb.append(" ");
				}

				sb.append(versamento.getNumeroAvviso().charAt(i));
			}

			rata.setCodiceAvviso(sb.toString());
		}

		if(postale != null) {
			// ho gia' caricato tutte le label, spengo il pagamento standard
			input.getEtichette().getItaliano().setPagaTerritorio2(getLabel(LabelAvvisiProperties.DEFAULT_PROPS, LabelAvvisiProperties.LABEL_PAGA_TERRITORIO_POSTE));
			input.getEtichette().getItaliano().setPagaApp2(getLabel(LabelAvvisiProperties.DEFAULT_PROPS, LabelAvvisiProperties.LABEL_PAGA_APP_POSTE));
			if(input.getEtichette().getTraduzione() != null && secondaLinguaScelta != null) {
				input.getEtichette().getTraduzione().setPagaTerritorio2(getLabel(secondaLinguaScelta.toString(), LabelAvvisiProperties.LABEL_PAGA_TERRITORIO_POSTE));
				input.getEtichette().getTraduzione().setPagaApp2(getLabel(secondaLinguaScelta.toString(), LabelAvvisiProperties.LABEL_PAGA_APP_POSTE));
			}
			
			rata.setDataMatrix(AvvisoPagamentoUtils.creaDataMatrix(versamento.getNumeroAvviso(), AvvisoPagamentoUtils.getNumeroCCDaIban(postale.getCodIban()), 
					versamento.getImportoTotale().doubleValue(),
					input.getCfEnte(),
					input.getCfDestinatario(),
					input.getNomeCognomeDestinatario(),
					input.getEtichette().getItaliano().getOggettoDelPagamento()));
			rata.setNumeroCcPostale(AvvisoPagamentoUtils.getNumeroCCDaIban(postale.getCodIban()));
			if(StringUtils.isBlank(postale.getIntestatario()))
				input.setIntestatarioContoCorrentePostale(input.getEnteCreditore());
			else 
				input.setIntestatarioContoCorrentePostale(postale.getIntestatario());
			rata.setCodiceAvvisoPostale(rata.getCodiceAvviso()); 

			rata.setAutorizzazione(AvvisoPagamentoUtils.getAutorizzazionePoste(versamento.getDominio(configWrapper).getAutStampaPoste(), postale.getAutStampaPoste()));
			
			input.setPoste(true);
		} else {
			// ho gia' caricato tutte le label, spengo il pagamento poste
			input.getEtichette().getItaliano().setPagaTerritorio2(getLabel(LabelAvvisiProperties.DEFAULT_PROPS, LabelAvvisiProperties.LABEL_PAGA_TERRITORIO_STANDARD));
			input.getEtichette().getItaliano().setPagaApp2(getLabel(LabelAvvisiProperties.DEFAULT_PROPS, LabelAvvisiProperties.LABEL_PAGA_APP_STANDARD));
			if(input.getEtichette().getTraduzione() != null && secondaLinguaScelta != null ) {
				input.getEtichette().getTraduzione().setPagaTerritorio2(getLabel(secondaLinguaScelta.toString(), LabelAvvisiProperties.LABEL_PAGA_TERRITORIO_STANDARD));
				input.getEtichette().getTraduzione().setPagaApp2(getLabel(secondaLinguaScelta.toString(), LabelAvvisiProperties.LABEL_PAGA_APP_STANDARD));
			}
		}

		if(versamento.getImportoTotale() != null)
			rata.setImporto(versamento.getImportoTotale().doubleValue());

		if(addDataValidita) {
			if(versamento.getDataValidita() != null) {
				rata.setData(sdfDataScadenza.format(versamento.getDataValidita()));
			} else if(versamento.getDataScadenza() != null) {
				rata.setData(sdfDataScadenza.format(versamento.getDataScadenza()));
			} else {
				rata.setData("-"); 
			}
		}
		
		it.govpay.core.business.model.Iuv iuvGenerato = IuvUtils.toIuv(versamento, versamento.getApplicazione(configWrapper), versamento.getDominio(configWrapper));
		if(iuvGenerato.getQrCode() != null)
			rata.setQrCode(new String(iuvGenerato.getQrCode()));

		return rata;
	}
	
	public static void impostaAnagraficaEnteCreditore(Versamento versamento, Dominio dominio, UnitaOperativa uo, AvvisoPagamentoInput input)
			throws ServiceException {

		String codDominio = dominio.getCodDominio();
		Anagrafica anagraficaDominio = dominio.getAnagrafica();
		
		Anagrafica anagraficaUO = null;
		if(uo!=null)
			anagraficaUO = uo.getAnagrafica();

		input.setEnteCreditore(dominio.getRagioneSociale());
		input.setCfEnte(codDominio);
		input.setCbill(dominio.getCbill() != null ? dominio.getCbill()  : " ");

		
		if(anagraficaUO != null) {	
			input.setSettoreEnte(anagraficaUO.getArea());
		} else if(anagraficaDominio != null) { 
			input.setSettoreEnte(anagraficaDominio.getArea());
		}
		
		StringBuilder sb = new StringBuilder();

		if(StringUtils.isNotEmpty(anagraficaUO.getUrlSitoWeb())) {
			sb.append(anagraficaUO.getUrlSitoWeb());
		} else if(StringUtils.isNotEmpty(anagraficaDominio.getUrlSitoWeb())) {
			sb.append(anagraficaDominio.getUrlSitoWeb());
		}
		
		if(sb.length() > 0)
			sb.append("<br/>");
		
		boolean line2=false;
		if(StringUtils.isNotEmpty(anagraficaUO.getTelefono())){
			sb.append("Tel: ").append(anagraficaUO.getTelefono());
			sb.append(" - ");
			line2=true;
		} else if(StringUtils.isNotEmpty(anagraficaDominio.getTelefono())) {
			sb.append("Tel: ").append(anagraficaDominio.getTelefono());
			sb.append(" - ");
			line2=true;
		} 
		
		if(StringUtils.isNotEmpty(anagraficaUO.getFax())){
			sb.append("Fax: ").append(anagraficaUO.getFax());
			line2=true;
		} else if(StringUtils.isNotEmpty(anagraficaDominio.getFax())) {
			sb.append("Fax: ").append(anagraficaDominio.getFax());
			line2=true;
		}
		
		if(line2) sb.append("<br/>");
		
		if(StringUtils.isNotEmpty(anagraficaUO.getPec())) {
			sb.append("pec: ").append(anagraficaUO.getPec());
		} else if(StringUtils.isNotEmpty(anagraficaUO.getEmail())){
			sb.append("email: ").append(anagraficaUO.getEmail());
		} else if(StringUtils.isNotEmpty(anagraficaDominio.getPec())) {
			sb.append("pec: ").append(anagraficaDominio.getPec());
		} else if(StringUtils.isNotEmpty(anagraficaDominio.getEmail())){
			sb.append("email: ").append(anagraficaDominio.getEmail());
		}

		input.setInfoEnte(sb.toString());
		// se e' presente un logo lo inserisco altrimemti verra' caricato il logo di default.
		if(dominio.getLogo() != null && dominio.getLogo().length > 0)
			input.setLogoEnte(new String(dominio.getLogo()));
		
		BDConfigWrapper configWrapper = new BDConfigWrapper(ContextThreadLocal.get().getTransactionId(), true);
		if(VersamentoUtils.isPendenzaMultibeneficiario(versamento, configWrapper)) {
			// se il versamento e' multibeneficiario inserisco anche il logo del primo dominio diverso che trovo
			for (SingoloVersamento sv : versamento.getSingoliVersamenti(configWrapper)) {
				Dominio dominioSingoloVersamento = VersamentoUtils.getDominioSingoloVersamento(sv, dominio, configWrapper);
				if(dominioSingoloVersamento != null && !dominioSingoloVersamento.getCodDominio().equals(dominio.getCodDominio())) {
					if(dominioSingoloVersamento.getLogo() != null && dominioSingoloVersamento.getLogo().length > 0)
					input.setLogoEnteSecondario(new String(dominioSingoloVersamento.getLogo()));
					break;
				}
			}
		}
		return;
	}

	public static void impostaAnagraficaDebitore(Anagrafica anagraficaDebitore, AvvisoPagamentoInput input) {
		if(anagraficaDebitore != null) {
			String indirizzoDebitore = StringUtils.isNotEmpty(anagraficaDebitore.getIndirizzo()) ? anagraficaDebitore.getIndirizzo() : "";
			String civicoDebitore = StringUtils.isNotEmpty(anagraficaDebitore.getCivico()) ? anagraficaDebitore.getCivico() : "";
			String capDebitore = StringUtils.isNotEmpty(anagraficaDebitore.getCap()) ? anagraficaDebitore.getCap() : "";
			String localitaDebitore = StringUtils.isNotEmpty(anagraficaDebitore.getLocalita()) ? anagraficaDebitore.getLocalita() : "";
			String provinciaDebitore = StringUtils.isNotEmpty(anagraficaDebitore.getProvincia()) ? (" (" +anagraficaDebitore.getProvincia() +")" ) : "";
			// Indirizzo piu' civico impostati se non e' vuoto l'indirizzo
			String indirizzoDestinatario = StringUtils.isNotEmpty(indirizzoDebitore) ? indirizzoDebitore + " " + civicoDebitore : "";
			// capCittaProv impostati se e' valorizzata la localita'
			String capCittaDebitore = StringUtils.isNotEmpty(localitaDebitore) ? (capDebitore + " " + localitaDebitore + provinciaDebitore) : "";

			input.setNomeCognomeDestinatario(anagraficaDebitore.getRagioneSociale());
			input.setCfDestinatario(anagraficaDebitore.getCodUnivoco().toUpperCase());

			if(indirizzoDestinatario.length() > AvvisoPagamentoCostanti.AVVISO_LUNGHEZZA_CAMPO_INDIRIZZO_DESTINATARIO) {
				input.setIndirizzoDestinatario1(indirizzoDestinatario);
			}else {
				input.setIndirizzoDestinatario1(indirizzoDestinatario);
			}

			if(capCittaDebitore.length() > AvvisoPagamentoCostanti.AVVISO_LUNGHEZZA_CAMPO_INDIRIZZO_DESTINATARIO) {
				input.setIndirizzoDestinatario2(capCittaDebitore);
			}else {
				input.setIndirizzoDestinatario2(capCittaDebitore);
			}
		}
	}
	
	public static String getLabel(String lingua, String nomeLabel, Object ... parameter) throws UtilsException {
		Properties labelsLingua = LabelAvvisiProperties.getInstance().getLabelsLingua(lingua);
		
		String propertyValue = labelsLingua.getProperty(nomeLabel);
		
		if(parameter != null && parameter.length > 0) {
			return MessageFormat.format(propertyValue, parameter);
		}
		
		return propertyValue;
	}
	
	public static Etichette getEtichetteItaliano() throws UtilsException { 
		Etichette etichette = new Etichette();
		
		Properties labelsLingua = LabelAvvisiProperties.getInstance().getLabelsLingua(LabelAvvisiProperties.DEFAULT_PROPS);
				
		etichette.setAvvisoPagamento(labelsLingua.getProperty(LabelAvvisiProperties.LABEL_AVVISO_PAGAMENTO));
		etichette.setCanali(labelsLingua.getProperty(LabelAvvisiProperties.LABEL_CANALI));
		etichette.setCodiceAvviso(labelsLingua.getProperty(LabelAvvisiProperties.LABEL_CODICE_AVVISO));
		etichette.setCodiceCbill(labelsLingua.getProperty(LabelAvvisiProperties.LABEL_CODICE_CBILL));
		etichette.setCodiceFiscaleEnte(labelsLingua.getProperty(LabelAvvisiProperties.LABEL_CODICE_FISCALE_ENTE));
		etichette.setCome(labelsLingua.getProperty(LabelAvvisiProperties.LABEL_COME));
		etichette.setDescrizione(labelsLingua.getProperty(LabelAvvisiProperties.LABEL_DESCRIZIONE));
		etichette.setDestinatario(labelsLingua.getProperty(LabelAvvisiProperties.LABEL_DESTINATARIO));
		etichette.setDestinatarioAvviso(labelsLingua.getProperty(LabelAvvisiProperties.LABEL_DESTINATARIO_AVVISO));
		etichette.setEnteCreditore(labelsLingua.getProperty(LabelAvvisiProperties.LABEL_ENTE_CREDITORE));
		etichette.setEntro(labelsLingua.getProperty(LabelAvvisiProperties.LABEL_ENTRO_IL));
		etichette.setImporto(labelsLingua.getProperty(LabelAvvisiProperties.LABEL_IMPORTO));
		etichette.setIntestatario(labelsLingua.getProperty(LabelAvvisiProperties.LABEL_INTESTATARIO));
		etichette.setNota(labelsLingua.getProperty(LabelAvvisiProperties.LABEL_NOTA));
//		etichette.setNota2(labelsLingua.getProperty(LabelAvvisiProperties.LABEL_NOTA_IMPORTO));
		//etichette.setNotaPrimaRata(labelsLingua.getProperty(LabelAvvisiProperties.LABEL_NOTA_PRIMA_RATA));
//		etichette.setNotaRataUnica(labelsLingua.getProperty(LabelAvvisiProperties.LABEL_NOTA_RATA_UNICA));
		etichette.setOggetto(labelsLingua.getProperty(LabelAvvisiProperties.LABEL_OGGETTO));
		etichette.setPagaApp(labelsLingua.getProperty(LabelAvvisiProperties.LABEL_PAGA_APP));
		etichette.setPagaTerritorio(labelsLingua.getProperty(LabelAvvisiProperties.LABEL_PAGA_TERRITORIO));
		etichette.setPrimaRata(labelsLingua.getProperty(LabelAvvisiProperties.LABEL_PRIMA_RATA));
		etichette.setQuantoQuando(labelsLingua.getProperty(LabelAvvisiProperties.LABEL_QUANTO_QUANDO));
		etichette.setTipo(labelsLingua.getProperty(LabelAvvisiProperties.LABEL_TIPO));
		
		return etichette;
	}
	
	public static Etichette getEtichetteTraduzione(LinguaSecondaria linguaSecondaria) throws UtilsException {
		if(linguaSecondaria == null) {
			return null;
		}
		
		Etichette etichette = new Etichette();
		
		Properties labelsLingua = LabelAvvisiProperties.getInstance().getLabelsLingua(linguaSecondaria.toString());
				
		etichette.setAvvisoPagamento(labelsLingua.getProperty(LabelAvvisiProperties.LABEL_AVVISO_PAGAMENTO));
		etichette.setCanali(labelsLingua.getProperty(LabelAvvisiProperties.LABEL_CANALI));
		etichette.setCodiceAvviso(labelsLingua.getProperty(LabelAvvisiProperties.LABEL_CODICE_AVVISO));
		etichette.setCodiceCbill(labelsLingua.getProperty(LabelAvvisiProperties.LABEL_CODICE_CBILL));
		etichette.setCodiceFiscaleEnte(labelsLingua.getProperty(LabelAvvisiProperties.LABEL_CODICE_FISCALE_ENTE));
		etichette.setCome(labelsLingua.getProperty(LabelAvvisiProperties.LABEL_COME));
		etichette.setDescrizione(labelsLingua.getProperty(LabelAvvisiProperties.LABEL_DESCRIZIONE));
		etichette.setDestinatario(labelsLingua.getProperty(LabelAvvisiProperties.LABEL_DESTINATARIO));
		etichette.setDestinatarioAvviso(labelsLingua.getProperty(LabelAvvisiProperties.LABEL_DESTINATARIO_AVVISO));
		etichette.setEnteCreditore(labelsLingua.getProperty(LabelAvvisiProperties.LABEL_ENTE_CREDITORE));
		etichette.setEntro(labelsLingua.getProperty(LabelAvvisiProperties.LABEL_ENTRO_IL));
		etichette.setImporto(labelsLingua.getProperty(LabelAvvisiProperties.LABEL_IMPORTO));
		etichette.setIntestatario(labelsLingua.getProperty(LabelAvvisiProperties.LABEL_INTESTATARIO));
		etichette.setNota(labelsLingua.getProperty(LabelAvvisiProperties.LABEL_NOTA));
//		etichette.setNota2(labelsLingua.getProperty(LabelAvvisiProperties.LABEL_NOTA_IMPORTO));
		//etichette.setNotaPrimaRata(labelsLingua.getProperty(LabelAvvisiProperties.LABEL_NOTA_PRIMA_RATA));
//		etichette.setNotaRataUnica(labelsLingua.getProperty(LabelAvvisiProperties.LABEL_NOTA_RATA_UNICA));
		etichette.setOggetto(labelsLingua.getProperty(LabelAvvisiProperties.LABEL_OGGETTO));
		etichette.setPagaApp(labelsLingua.getProperty(LabelAvvisiProperties.LABEL_PAGA_APP));
		etichette.setPagaTerritorio(labelsLingua.getProperty(LabelAvvisiProperties.LABEL_PAGA_TERRITORIO));
		etichette.setPrimaRata(labelsLingua.getProperty(LabelAvvisiProperties.LABEL_PRIMA_RATA));
		etichette.setQuantoQuando(labelsLingua.getProperty(LabelAvvisiProperties.LABEL_QUANTO_QUANDO));
		etichette.setTipo(labelsLingua.getProperty(LabelAvvisiProperties.LABEL_TIPO));
		
		return etichette;
	}
}
