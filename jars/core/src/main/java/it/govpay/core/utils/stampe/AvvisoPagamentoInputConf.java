package it.govpay.core.utils.stampe;

import java.util.List;

import org.openspcoop2.generic_project.exception.ServiceException;
import org.openspcoop2.utils.service.context.ContextThreadLocal;
import org.slf4j.Logger;

import it.govpay.bd.BDConfigWrapper;
import it.govpay.bd.model.Documento;
import it.govpay.bd.model.SingoloVersamento;
import it.govpay.bd.model.Versamento;
import it.govpay.model.Versamento.TipoSogliaVersamento;

public class AvvisoPagamentoInputConf {
	
	public static AvvisoPagamentoInputConf getConfigurazioneFromVersamenti(Documento documento, List<Versamento> versamenti, Logger log) throws ServiceException {
		BDConfigWrapper configWrapper = new BDConfigWrapper(ContextThreadLocal.get().getTransactionId(), true);
		AvvisoPagamentoInputConf toRet = new AvvisoPagamentoInputConf();
		
		log.debug("Documento ["+documento.getCodDocumento()+"] Numero totale di versamenti da inserire: " + versamenti.size());
		
		// postale
		int numeroPostaliSV = 0;
		int numeroSV = 0;
		for (Versamento versamento : versamenti) {
			List<SingoloVersamento> singoliVersamenti = versamento.getSingoliVersamenti(configWrapper);
			numeroSV += singoliVersamenti.size();
			for (SingoloVersamento sv : singoliVersamenti) {
				if(sv.getIbanAccredito(configWrapper) != null && sv.getIbanAccredito(configWrapper).isPostale())
					numeroPostaliSV++;
				else if(sv.getIbanAppoggio(configWrapper) != null && sv.getIbanAppoggio(configWrapper).isPostale())
					numeroPostaliSV++;
			}
		}
		
		toRet.postale = numeroSV == numeroPostaliSV;
		log.debug("Documento ["+documento.getCodDocumento()+"] Postale: ["+toRet.postale+"]");
		
		// rata unica
		for (Versamento versamento : versamenti) {
			if(versamento.getNumeroRata() == null && versamento.getTipoSoglia() == null) {
				toRet.numeroRataUnica ++;
			}
		}
		
		toRet.soloRataUnica = toRet.numeroRataUnica == versamenti.size();
		
		log.debug("Documento ["+documento.getCodDocumento()+"] contiene rata unica: ["+toRet.numeroRataUnica+"], solo rata unica: ["+ toRet.soloRataUnica +"]");
				
		// ViolazioneCDS
		int numeroViolazioneCDS = 0; 
		
		for (Versamento versamento : versamenti) {
			if(versamento.getTipoSoglia() != null && 
					(versamento.getTipoSoglia().equals(TipoSogliaVersamento.RIDOTTO)
					|| versamento.getTipoSoglia().equals(TipoSogliaVersamento.SCONTATO))) {
				numeroViolazioneCDS ++;
			}
		}
		
		toRet.violazioneCDS = numeroViolazioneCDS == versamenti.size();
		log.debug("Documento ["+documento.getCodDocumento()+"] ViolazioneCDS: ["+toRet.violazioneCDS+"]");
		
		for (Versamento versamento : versamenti) {
			if(versamento.getNumeroRata() != null) {
				toRet.numeroRate ++;
			}
		}
		
		toRet.soloRate = toRet.numeroRate == versamenti.size();
		
		log.debug("Documento ["+documento.getCodDocumento()+"] contiene rate: ["+toRet.numeroRate+"], solo rate: ["+ toRet.soloRate +"]");
		
		// calcolo dei versamenti con soglia
		for (Versamento versamento : versamenti) {
			if(versamento.getTipoSoglia() != null) {
				toRet.numeroSoglie ++;
			}
		}
		
		toRet.soloSoglie = toRet.numeroSoglie == versamenti.size();
		
		log.debug("Documento ["+documento.getCodDocumento()+"] contiene soglie: ["+toRet.numeroSoglie+"], solo soglie: ["+ toRet.soloSoglie +"]");
		
		return toRet;
	}
	
	
	private boolean soloRataUnica = false;
	private boolean soloRate = false;
	private boolean soloSoglie = false;
	
	private int numeroRataUnica = 0;
	private int numeroRate = 0;
	private int numeroSoglie = 0;
	
	private boolean violazioneCDS = false;
	private boolean postale = false;
	
	public boolean isSoloRataUnica() {
		return soloRataUnica;
	}
	public boolean isSoloRate() {
		return soloRate;
	}
	public boolean isSoloSoglie() {
		return soloSoglie;
	}
	public boolean isViolazioneCDS() {
		return violazioneCDS;
	}
	public boolean isPostale() {
		return postale;
	}
	public boolean isAlmenoUnaRataUnica() {
		return numeroRataUnica > 0;
	}
	public boolean isAlmenoUnaRata() {
		return numeroRate > 0;
	}
	public boolean isAlmenoUnaSoglia() {
		return numeroSoglie > 0;
	}
	public boolean isMultipla() {
		return numeroRate > 3 || numeroSoglie > 3;
	}
	public int getNumeroRataUnica() {
		return numeroRataUnica;
	}
	public int getNumeroRate() {
		return numeroRate;
	}
	public int getNumeroSoglie() {
		return numeroSoglie;
	}
	
}
