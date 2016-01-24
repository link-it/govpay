/*
 * GovPay - Porta di Accesso al Nodo dei Pagamenti SPC 
 * http://www.gov4j.it/govpay
 * 
 * Copyright (c) 2014-2015 Link.it srl (http://www.link.it).
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
package it.govpay.thread;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openspcoop2.generic_project.exception.NotFoundException;
import org.openspcoop2.generic_project.exception.ServiceException;

import gov.telematici.pagamenti.ws.ppthead.IntestazionePPT;
import it.gov.digitpa.schemas._2011.ws.paa.NodoInviaRPT;
import it.gov.digitpa.schemas._2011.ws.paa.NodoInviaRPTRisposta;
import it.govpay.bd.BasicBD;
import it.govpay.bd.model.Intermediario;
import it.govpay.bd.model.Psp.Canale;
import it.govpay.bd.model.Rpt;
import it.govpay.bd.model.Rpt.StatoRpt;
import it.govpay.bd.model.Stazione;
import it.govpay.bd.model.Versamento.StatoVersamento;
import it.govpay.bd.pagamento.RptBD;
import it.govpay.bd.pagamento.VersamentiBD;
import it.govpay.exception.GovPayException;
import it.govpay.exception.GovPayNdpException;
import it.govpay.exception.GovPayException.GovPayExceptionEnum;
import it.govpay.web.wsclient.NodoPerPa;

public class InviaRptThread implements Runnable {
	
	private byte[] rptXml;
	private Rpt rpt;
	private Intermediario intermediario;
	private Stazione stazione;
	private Canale canale;
	
	private static Logger log = LogManager.getLogger();
	
	public InviaRptThread(Rpt rpt, byte[] rptXml, Intermediario intermediario, Stazione stazione, Canale canale) {
		this.rptXml = rptXml;
		this.rpt = rpt;
		this.intermediario = intermediario;
		this.stazione = stazione;
		this.canale = canale;
	}
	
	@Override
	public void run() {
		BasicBD bd = null;
		NodoInviaRPTRisposta risposta;
		
		try {
			log.info("Spedizione RPT al Nodo [CodMsgRichiesta: " + rpt.getCodMsgRichiesta() + "]");
			NodoPerPa client = new NodoPerPa(intermediario);
		
			IntestazionePPT intestazione = new IntestazionePPT();
			intestazione.setCodiceContestoPagamento(this.rpt.getCcp());
			intestazione.setIdentificativoDominio(this.rpt.getCodDominio());
			intestazione.setIdentificativoIntermediarioPA(this.intermediario.getCodIntermediario());
			intestazione.setIdentificativoStazioneIntermediarioPA(this.stazione.getCodStazione());
			intestazione.setIdentificativoUnivocoVersamento(this.rpt.getIuv());

			NodoInviaRPT inviaRPT = new NodoInviaRPT();
			inviaRPT.setIdentificativoCanale(this.canale.getCodCanale());
			inviaRPT.setIdentificativoIntermediarioPSP(this.canale.getCodIntermediario());
			inviaRPT.setIdentificativoPSP(this.canale.getPsp().getCodPsp());
			inviaRPT.setPassword(this.stazione.getPassword());
			inviaRPT.setRpt(this.rptXml);
			inviaRPT.setTipoFirma(rpt.getFirmaRichiesta().getCodifica());
			risposta = client.nodoInviaRPT(intermediario, stazione, canale, rpt, inviaRPT);
			
			try {
				bd = BasicBD.newInstance();
			} catch (Exception e) {
				log.error("Impossibile stabilire una connessione con il database", e);
				return;
			}
			
			RptBD rptBD = new RptBD(bd);
			VersamentiBD versamentiBD = new VersamentiBD(bd);
			bd.setAutoCommit(false);

			if(risposta.getFault() != null) {
				log.error("Ricevuto esito KO [FaultId: " + risposta.getFault().getId() + "] [FaultCode: " + risposta.getFault().getFaultCode() + "] [FaultString: " + risposta.getFault().getFaultString() + "] [FaultDescription: " +  risposta.getFault().getDescription()+ "].");
				Rpt.FaultNodo faultCode = Rpt.FaultNodo.valueOf(Rpt.FaultNodo.class, risposta.getFault().getFaultCode());
				if(risposta.getFault().getId().equals(Rpt.NdPFaultId)) {
					log.info("Aggiorno lo stato della RPT [CodMsgRichiesta: " + rpt.getCodMsgRichiesta() + "] in RPT_RIFIUTATA_NODO.");
					rptBD.updateStatoRpt(rpt.getId(), StatoRpt.RPT_RIFIUTATA_NODO, faultCode, risposta.getFault().getFaultString() + ": " + risposta.getFault().getDescription());
					log.info("Aggiorno lo stato del versamento in StatoVersamento.IN_ATTESA.");
					versamentiBD.updateStatoVersamento(rpt.getIdVersamento(), StatoVersamento.IN_ATTESA);
				} else {
					log.info("Aggiorno lo stato della RPT [CodMsgRichiesta: " + rpt.getCodMsgRichiesta() + "] in RPT_RIFIUTATA_PSP.");
					rptBD.updateStatoRpt(rpt.getId(), StatoRpt.RPT_RIFIUTATA_PSP, faultCode, risposta.getFault().getFaultString() + ": " + risposta.getFault().getDescription());
					log.info("Aggiorno lo stato del versamento in StatoVersamento.FALLITO.");
					versamentiBD.updateStatoVersamento(rpt.getIdVersamento(), StatoVersamento.FALLITO);
				}
				bd.commit();
				bd.closeConnection();
				throw new GovPayNdpException(GovPayExceptionEnum.ERRORE_NDP, faultCode, risposta.getFault().getFaultString() + ": " + risposta.getFault().getDescription());
			} else {
				log.info("Ricevuto esito " + risposta.getEsito());
				log.info("Aggiorno lo stato della RPT [CodMsgRichiesta: " + rpt.getCodMsgRichiesta() + "] in RPT_ACCETTATA_NODO.");
				rptBD.updateStatoRpt(rpt.getId(), StatoRpt.RPT_ACCETTATA_NODO, null, null);
				log.info("Aggiorno lo stato dei versamento in StatoVersamento.AUTORIZZATO.");
				versamentiBD.updateStatoVersamento(rpt.getIdVersamento(), StatoVersamento.AUTORIZZATO);
				bd.commit();
				bd.closeConnection();
				return;
			}
		} catch (GovPayException e) {
			// ERRORE DI RETE. Non so se la RPT e' stata effettivamente consegnata.
			log.error("Errore di rete nella spedizione della RPT: " + e);
			bd.rollback();
			bd.closeConnection();
			return;
		} catch (ServiceException e) {
			// ERRORE DB
			log.error("Errore interno: " + e);
			bd.rollback();
			bd.closeConnection();
			return;
		} catch (NotFoundException e) {
			// ERRORE RPT DOPPIONE.
			log.error("Errore interno: " + e);
			bd.rollback();
			bd.closeConnection();
			return;
		} 
	}
}
