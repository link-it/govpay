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
package it.govpay.utils;

import it.gov.digitpa.schemas._2011.pagamenti.CtRichiestaPagamentoTelematico;
import it.govpay.bd.anagrafica.AnagraficaManager;
import it.govpay.bd.model.Anagrafica;
import it.govpay.bd.model.IbanAccredito;
import it.govpay.bd.model.Portale;
import it.govpay.bd.model.Psp.Canale;
import it.govpay.bd.model.Rpt;
import it.govpay.bd.model.Rpt.Autenticazione;
import it.govpay.bd.model.Rpt.FirmaRichiesta;
import it.govpay.bd.model.Rpt.StatoRpt;
import it.govpay.bd.model.Rpt.TipoVersamento;
import it.govpay.bd.model.SingoloVersamento;
import it.govpay.bd.model.Tributo;
import it.govpay.bd.model.Versamento;
import it.govpay.bd.model.Versamento.StatoVersamento;
import it.govpay.bd.pagamento.RptBD;
import it.govpay.bd.pagamento.VersamentiBD;
import it.govpay.business.Pagamenti;
import it.govpay.exception.GovPayException;
import it.govpay.exception.GovPayException.GovPayExceptionEnum;

import java.util.Date;

import javax.xml.bind.JAXBException;

import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openspcoop2.generic_project.exception.MultipleResultException;
import org.openspcoop2.generic_project.exception.NotFoundException;
import org.openspcoop2.generic_project.exception.ServiceException;
import org.xml.sax.SAXException;

public class PagamentiUtils {
	
	public class RptExtended {
		private Rpt rpt;
		private byte[] rptXml;

		public RptExtended(Rpt rpt, byte[] rptXml) {
			this.rpt = rpt;
			this.rptXml = rptXml;
		}

		public byte[] getRptXml() {
			return rptXml;
		}

		public void setRptXml(byte[] rptXml) {
			this.rptXml = rptXml;
		}

		public Rpt getRpt() {
			return rpt;
		}

		public void setRpt(Rpt rpt) {
			this.rpt = rpt;
		}
	}

	private static Logger log = LogManager.getLogger();
	/**
	 * Crea su DB l'RPT e la sua versione XML.
	 * Aggiorna i singoli versamenti con IbanAccredito, CausaleVersamento, DatiSpecificiRiscossione
	 * Aggiorna il versamento in stato IN_CORSO
	 */
	public RptExtended buildRpt(Versamento versamento, long idStazione, Anagrafica anagraficaEnte, Portale portale, String ccp, String codCarrello, String ibanAddebito, Canale canale, FirmaRichiesta firma, Autenticazione autenticazione, Anagrafica versante, String callbackURL, Pagamenti pagamentiBD) throws MultipleResultException, ServiceException, GovPayException, NotFoundException, JAXBException, SAXException{
		return buildRpt(versamento, idStazione, anagraficaEnte, portale, ccp, codCarrello, ibanAddebito, null, canale, firma, autenticazione, versante, callbackURL, pagamentiBD);
	}
	
	public RptExtended buildRpt(Versamento versamento, long idStazione, Anagrafica anagraficaEnte, Portale portale, String ccp, String codCarrello, String ibanAddebito, String ibanAccredito, Canale canale, FirmaRichiesta firma, Autenticazione autenticazione, Anagrafica versante, String callbackURL, Pagamenti pagamentiBD) throws MultipleResultException, ServiceException, GovPayException, NotFoundException, JAXBException, SAXException{
		check(versamento, canale, ibanAddebito);
		Rpt rpt = new Rpt();
		rpt.setIdVersamento(versamento.getId());
		rpt.setCodDominio(versamento.getCodDominio());
		rpt.setIuv(versamento.getIuv());
		rpt.setCcp(ccp);
		rpt.setIdCanale(canale.getId());
		rpt.setCodMsgRichiesta(NdpUtils.buildCodMsd());
		rpt.setDataOraCreazione(new Date());
		rpt.setStatoRpt(rpt.getStatoRpt());
		rpt.setStatoRpt(StatoRpt.RPT_ATTIVATA);
		rpt.setCcp(ccp);
		rpt.setDataOraMsgRichiesta(new Date());
		rpt.setFirmaRichiesta(firma);
		rpt.setAnagraficaVersante(versante);
		rpt.setAutenticazioneSoggetto(autenticazione);
		rpt.setCodCarrello(codCarrello);
		rpt.setCallbackURL(callbackURL);
		if(portale != null) rpt.setIdPortale(portale.getId());
		rpt.setIdPsp(canale.getPsp().getId());
		rpt.setTipoVersamento(canale.getTipoVersamento());
		rpt.setIbanAddebito(ibanAddebito);
		rpt.setIdStazione(idStazione);
		
		VersamentiBD versamentiBD = new VersamentiBD(pagamentiBD);
		for(SingoloVersamento sv : versamento.getSingoliVersamenti()) {
			Tributo tributo = AnagraficaManager.getTributo(pagamentiBD, sv.getIdTributo());
			if(ibanAccredito == null){
				IbanAccredito iban = AnagraficaManager.getIbanAccredito(pagamentiBD, tributo.getIbanAccredito());
				sv.setIbanAccredito(iban.getCodIban());
			} else {
				sv.setIbanAccredito(ibanAccredito);
			}
			sv.setCausaleVersamento(JaxbUtils.buildCausaleSingoloVersamento(rpt.getIuv(), sv.getImportoSingoloVersamento()));
			sv.setDatiSpecificiRiscossione(tributo.getTipoContabilita().getCodifica() +"/"+ tributo.getCodContabilita());
			versamentiBD.updateSingoloVersamentoRPT(sv.getId(), sv.getIbanAccredito(), sv.getCausaleVersamento(), sv.getDatiSpecificiRiscossione() );
		}
		String codPortale = (portale != null) ? portale.getCodPortale() : null;
		CtRichiestaPagamentoTelematico richiestaRPT = JaxbUtils.buildRPT(rpt.getCodDominio(), anagraficaEnte, codPortale, versamento, rpt);
		byte[] rptXml = JaxbUtils.toByte(richiestaRPT);
		if(log.getLevel().isMoreSpecificThan(Level.DEBUG))
			log.trace("Rpt prodotta: " + new String(rptXml));
		new RptBD(pagamentiBD).insertRpt(rpt, rptXml);
		versamentiBD.updateStatoVersamento(versamento.getId(), StatoVersamento.IN_CORSO);
		
		RptExtended rptExtended = new RptExtended(rpt, rptXml);
		return rptExtended;
	}


	private void check(Versamento versamento, Canale canale, String ibanAddebito) throws GovPayException{
		// Controllo che lo stato sia pagabile
		if(versamento.getStato() != StatoVersamento.IN_ATTESA) {
			log.error("Il versamento risulta in stato " + versamento.getStato() + ". Pagamento non permesso. [idVersamento: " + versamento.getId() + "]");
			throw new GovPayException(GovPayExceptionEnum.ERRORE_VALIDAZIONE, "E' possibile pagare solo debiti in stato \"DA_PAGARE\"");
		}

		// Verifico che il Psp sia attivo
		if(!canale.getPsp().isAttivo()) {
			log.error("Il psp scelto non e' attivo [codPsp: " + canale.getPsp().getCodPsp() + "].");
			throw new GovPayException(GovPayExceptionEnum.PSP_NON_ATTIVO);
		}

		// Controllo che non se e' un tipo 3, ci sia un solo singolo versamento
		if(canale.getTipoVersamento().equals(TipoVersamento.ATTIVATO_PRESSO_PSP)) {
			if(versamento.getSingoliVersamenti().size() != 1)
				throw new GovPayException(GovPayExceptionEnum.ERRORE_VALIDAZIONE, "Non e' possibile pagare un versamento con piu' di un singolo versamento ad iniziativa Psp.");
		}

		// Controllo che se e' un AD ci sia l'IbanAddebito
		if(canale.getTipoVersamento().equals(TipoVersamento.ADDEBITO_DIRETTO) && ibanAddebito == null) {
			log.error("Il canale scelto e' di tipo Addebito Diretto, ma non e' stato fornito un iban di addebito. [idCanale: " + canale.getId() + "]");
			throw new GovPayException(GovPayExceptionEnum.ERRORE_VALIDAZIONE, "Iban addebito obbligatorio per Addebito Diretto.");
		}
	}
}
