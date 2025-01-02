/*
 * GovPay - Porta di Accesso al Nodo dei Pagamenti SPC
 * http://www.gov4j.it/govpay
 *
 * Copyright (c) 2014-2025 Link.it srl (http://www.link.it).
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
	
	private AvvisoPagamentoInputConf() {}
	
	public static AvvisoPagamentoInputConf getConfigurazioneFromVersamenti(Documento documento, List<Versamento> versamenti, Logger log) throws ServiceException {
		BDConfigWrapper configWrapper = new BDConfigWrapper(ContextThreadLocal.get().getTransactionId(), true);
		AvvisoPagamentoInputConf toRet = new AvvisoPagamentoInputConf();
		
		log.debug("Documento [{}] Numero totale di versamenti da inserire: {}", documento.getCodDocumento(), versamenti.size());
		
		// postale da https://github.com/pagopa/pagopa-api/issues/333
		// si controlla l'IBAN della prima voce
		toRet.postale = false;
		int numPendenzePostali = 0;
		for (Versamento versamento : versamenti) {
			List<SingoloVersamento> singoliVersamenti = versamento.getSingoliVersamenti(configWrapper);
			SingoloVersamento sv = singoliVersamenti.get(0);
			if((sv.getIbanAccredito(configWrapper) != null && sv.getIbanAccredito(configWrapper).isPostale())
				|| (sv.getIbanAppoggio(configWrapper) != null && sv.getIbanAppoggio(configWrapper).isPostale())	
					) {
				numPendenzePostali ++;
			}
		}
		
		toRet.postale = numPendenzePostali == versamenti.size();
		log.debug("Documento [{}] Postale: [{}]", documento.getCodDocumento(), toRet.postale);
		
		// rata unica
		for (Versamento versamento : versamenti) {
			if(versamento.getNumeroRata() == null && versamento.getTipoSoglia() == null) {
				toRet.numeroRataUnica ++;
			}
		}
		
		toRet.soloRataUnica = toRet.numeroRataUnica == versamenti.size();
		
		log.debug("Documento [{}] contiene rata unica: [{}], solo rata unica: [{}]", documento.getCodDocumento(), toRet.numeroRataUnica, toRet.soloRataUnica );
				
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
		log.debug("Documento [{}] ViolazioneCDS: [{}]", documento.getCodDocumento(), toRet.violazioneCDS);
		
		for (Versamento versamento : versamenti) {
			if(versamento.getNumeroRata() != null) {
				toRet.numeroRate ++;
			}
		}
		
		toRet.soloRate = toRet.numeroRate == versamenti.size();
		
		log.debug("Documento [{}] contiene rate: [{}], solo rate: [{}]", documento.getCodDocumento(), toRet.numeroRate, toRet.soloRate);
		
		// calcolo dei versamenti con soglia
		for (Versamento versamento : versamenti) {
			if(versamento.getTipoSoglia() != null) {
				toRet.numeroSoglie ++;
			}
		}
		
		toRet.soloSoglie = toRet.numeroSoglie == versamenti.size();
		
		log.debug("Documento [{}] contiene soglie: [{}], solo soglie: [{}]", documento.getCodDocumento(), toRet.numeroSoglie, toRet.soloSoglie);
		
		// calcolo del messaggio informativo personalizzato
		// assumo che le informazioni necessarie per pilotare il comportamento dell'avviso si trovino nel primo versamento della lista
		toRet.nascondiInformativaImportoAvviso = AvvisoPagamentoUtils.nascondiInformativaImportoAvviso(versamenti.get(0));
		toRet.informativaImportoAvviso = AvvisoPagamentoUtils.getInformativaImportoAvviso(versamenti.get(0));
		
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
	
	private boolean nascondiInformativaImportoAvviso = false;
	private String informativaImportoAvviso = null;
	
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
	public boolean isNascondiInformativaImportoAvviso() {
		return nascondiInformativaImportoAvviso;
	}
	public String getInformativaImportoAvviso() {
		return informativaImportoAvviso;
	}
}
