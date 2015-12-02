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
package it.govpay.bd.model;

import java.util.Date;

import org.apache.commons.lang.ArrayUtils;
import org.openspcoop2.generic_project.exception.ServiceException;

public class Rpt extends BasicModel{
	private static final long serialVersionUID = 1L;
	
	public enum FirmaRichiesta {
	    CA_DES("1"),
	    XA_DES("3"),
	    AVANZATA("4"),
	    NESSUNA("0");
	    
		private String codifica;

		FirmaRichiesta(String codifica) {
			this.codifica = codifica;
		}
		public String getCodifica() {
			return codifica;
		}
		
		public static FirmaRichiesta toEnum(String codifica) throws ServiceException {
			
			// Workaround violazione delle specifiche
			if(codifica == null || codifica.equals(""))
				return FirmaRichiesta.NESSUNA;
			
			for(FirmaRichiesta p : FirmaRichiesta.values()){
				if(p.getCodifica().equals(codifica))
					return p;
			}
			throw new ServiceException("Codifica inesistente per FirmaRichiesta. Valore fornito [" + codifica + "] valori possibili " + ArrayUtils.toString(FirmaRichiesta.values()));
		}
	}
	public static String NdPFaultId = "NodoDeiPagamentiSPC";
	
	public enum StatoRpt {
		RPT_ATTIVATA,// RPT Attivata, con tracciato XML e da spedire
		RPT_ERRORE_INVIO_A_NODO, // RPT Inviata con eccezione. Non so se e' stata consegnata
		RPT_INVIO_A_NODO_FALLITO, // RPT Inviata con eccezione e verificato che al nodo non e' arrivata 
		RPT_RICEVUTA_NODO, 
		RPT_RIFIUTATA_NODO, 
		RPT_ACCETTATA_NODO, 
		RPT_RIFIUTATA_PSP, 
		RPT_ACCETTATA_PSP, 
		RPT_ERRORE_INVIO_A_PSP, 
		RPT_INVIATA_A_PSP, 
		RPT_DECORSI_TERMINI,
		RT_RICEVUTA_NODO,
		RT_RIFIUTATA_NODO,
		RT_ACCETTATA_NODO,
		RT_ACCETTATA_PA,
		RT_RIFIUTATA_PA,
		RT_ESITO_SCONOSCIUTO_PA;
	}
	
	public enum TipoVersamento {
		BONIFICO_BANCARIO_TESORERIA("BBT"), 
		BOLLETTINO_POSTALE("BP"), 
		ADDEBITO_DIRETTO("AD"), 
		CARTA_PAGAMENTO("CP"), 
		MYBANK("OBEP"), 
		ATTIVATO_PRESSO_PSP("PO"), 
		SCONOSCIUTO ("SCONOSCIUTO");
		
		private String codifica;

		TipoVersamento(String codifica) {
			this.codifica = codifica;
		}
		public String getCodifica() {
			return codifica;
		}
		
		public static TipoVersamento toEnum(String codifica) throws ServiceException {
			for(TipoVersamento p : TipoVersamento.values()){
				if(p.getCodifica().equals(codifica))
					return p;
			}
			throw new ServiceException("Codifica inesistente per TipoVersamento. Valore fornito [" + codifica + "] valori possibili " + ArrayUtils.toString(TipoVersamento.values()));
		}
	}
	
	public enum Autenticazione {
		CNS("CNS"), 
		USR("USR"), 
		OTH("OTH"), 
		N_A("N/A");
		
		private String codifica;

		Autenticazione(String codifica) {
			this.codifica = codifica;
		}
		public String getCodifica() {
			return codifica;
		}
		
		public static Autenticazione toEnum(String codifica) throws ServiceException {
			for(Autenticazione p : Autenticazione.values()){
				if(p.getCodifica().equals(codifica))
					return p;
			}
			throw new ServiceException("Codifica inesistente per Autenticazione. Valore fornito [" + codifica + "] valori possibili " + ArrayUtils.toString(Autenticazione.values()));
		}
	}
	
	public enum FaultNodo implements NdpFaultCode {
		PPT_SINTASSI_XSD,
		PPT_SINTASSI_EXTRAXSD,
		PPT_SEMANTICA,
		PPT_AUTENTICAZIONE,
		PPT_AUTORIZZAZIONE,
		PPT_DOMINIO_SCONOSCIUTO,
		PPT_DOMINIO_DISABILITATO,
		PPT_STAZIONE_INT_PA_SCONOSCIUTA,
		PPT_STAZIONE_INT_PA_DISABILITATA,
		PPT_STAZIONE_INT_PA_IRRAGGIUNGIBILE,
		PPT_STAZIONE_INT_PA_SERVIZIO_NONATTIVO,
		PPT_CANALE_SCONOSCIUTO,
		PPT_CANALE_IRRAGGIUNGIBILE,
		PPT_CANALE_SERVIZIO_NONATTIVO,
		PPT_CANALE_TIMEOUT,
		PPT_CANALE_DISABILITATO,
		PPT_CANALE_NONRISOLVIBILE,
		PPT_CANALE_INDISPONIBILE,
		PPT_CANALE_ERRORE,
		PPT_CANALE_ERR_PARAM_PAG_IMM,
		PPT_CANALE_ERRORE_RESPONSE,
		PPT_PSP_SCONOSCIUTO,
		PPT_PSP_DISABILITATO,
		PPT_RPT_DUPLICATA,
		PPT_RPT_SCONOSCIUTA,
		PPT_RT_SCONOSCIUTA,
		PPT_RT_NONDISPONIBILE,
		PPT_SUPERAMENTOSOGLIA,
		PPT_TIPOFIRMA_SCONOSCIUTO,
		PPT_ERRORE_FORMATO_BUSTA_FIRMATA,
		PPT_FIRMA_INDISPONIBILE,
		PPT_CODIFICA_PSP_SCONOSCIUTA,
		PPT_ID_FLUSSO_SCONOSCIUTO,
		PPT_ERRORE_EMESSO_DA_PAA,
		PPT_SYSTEM_ERROR;
	}

	public static final String CCP_NA = "n/a";
	
	private Long id;
	private long idVersamento;
	private Long idPortale;
	private Long idStazione;
	private Long idPsp;
	private Long idCanale;
	private String ccp;
	
	private String codCarrello;
	private String codSessione;
	private String codDominio;
	private String iuv;
	private TipoVersamento tipoVersamento;
	private Anagrafica anagraficaVersante;
	private Date dataOraMsgRichiesta;
	private Date dataOraCreazione;
	private String codMsgRichiesta;
	private String ibanAddebito;
	private FirmaRichiesta firmaRichiesta;
	private Autenticazione autenticazioneSoggetto;
	private StatoRpt statoRpt;
	private FaultNodo faultCode;
	private String descrizioneStato;
	private String callbackURL;
	private String pspRedirectURL;
	private Long idTracciatoXML;
	
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public long getIdVersamento() {
		return idVersamento;
	}
	public void setIdVersamento(long idVersamento) {
		this.idVersamento = idVersamento;
	}
	public String getCodCarrello() {
		return codCarrello;
	}
	public void setCodCarrello(String codCarrello) {
		this.codCarrello = codCarrello;
	}
	public String getCcp() {
		return ccp;
	}
	public void setCcp(String ccp) {
		this.ccp = ccp;
	}
	public long getIdPsp() {
		return idPsp;
	}
	public void setIdPsp(long idPsp) {
		this.idPsp = idPsp;
	}
	public Date getDataOraMsgRichiesta() {
		return dataOraMsgRichiesta;
	}
	public void setDataOraMsgRichiesta(Date dataOraMsgRichiesta) {
		this.dataOraMsgRichiesta = dataOraMsgRichiesta;
	}
	public String getCodMsgRichiesta() {
		return codMsgRichiesta;
	}
	public void setCodMsgRichiesta(String codMsgRichiesta) {
		this.codMsgRichiesta = codMsgRichiesta;
	}
	public String getIbanAddebito() {
		return ibanAddebito;
	}
	public void setIbanAddebito(String ibanAddebito) {
		this.ibanAddebito = ibanAddebito;
	}
	public StatoRpt getStatoRpt() {
		return statoRpt;
	}
	public void setStatoRpt(StatoRpt statoRpt) {
		this.statoRpt = statoRpt;
	}
	public String getDescrizioneStato() {
		return descrizioneStato;
	}
	public void setDescrizioneStato(String descrizioneStato) {
		this.descrizioneStato = descrizioneStato;
	}
	public Long getIdTracciatoXML() {
		return idTracciatoXML;
	}
	public void setIdTracciatoXML(Long idTracciatoXML) {
		this.idTracciatoXML = idTracciatoXML;
	}
	public FirmaRichiesta getFirmaRichiesta() {
		return firmaRichiesta;
	}
	public void setFirmaRichiesta(FirmaRichiesta firmaRichiesta) {
		this.firmaRichiesta = firmaRichiesta;
	}
	public TipoVersamento getTipoVersamento() {
		return tipoVersamento;
	}
	public void setTipoVersamento(TipoVersamento tipoVersamento) {
		this.tipoVersamento = tipoVersamento;
	}
	public Autenticazione getAutenticazioneSoggetto() {
		return autenticazioneSoggetto;
	}
	public void setAutenticazioneSoggetto(Autenticazione autenticazioneSoggetto) {
		this.autenticazioneSoggetto = autenticazioneSoggetto;
	}
	public Anagrafica getAnagraficaVersante() {
		return anagraficaVersante;
	}
	public void setAnagraficaVersante(Anagrafica anagraficaVersante) {
		this.anagraficaVersante = anagraficaVersante;
	}
	public FaultNodo getFaultCode() {
		return faultCode;
	}
	public void setFaultCode(FaultNodo faultCode) {
		this.faultCode = faultCode;
	}
	public String getCodSessione() {
		return codSessione;
	}
	public void setCodSessione(String codSessione) {
		this.codSessione = codSessione;
	}
	public String getCallbackURL() {
		return callbackURL;
	}
	public void setCallbackURL(String callbackURL) {
		this.callbackURL = callbackURL;
	}
	public Long getIdPortale() {
		return idPortale;
	}
	public void setIdPortale(Long idPortale) {
		this.idPortale = idPortale;
	}
	public String getCodDominio() {
		return codDominio;
	}
	public void setCodDominio(String codDominio) {
		this.codDominio = codDominio;
	}
	public String getIuv() {
		return iuv;
	}
	public void setIuv(String iuv) {
		this.iuv = iuv;
	}
	public void setIdStazione(Long idStazione) {
		this.idStazione = idStazione;
	}
	public void setIdPsp(Long idPsp) {
		this.idPsp = idPsp;
	}
	
	@Override
	public boolean equals(Object obj) {
		Rpt rpt = null;
		if(obj instanceof Rpt) {
			rpt = (Rpt) obj;
		} else {
			return false;
		}
		
		boolean equal =
				equals(codCarrello, rpt.getCodCarrello()) &&
				equals(codSessione, rpt.getCodSessione()) &&
				equals(codDominio, rpt.getCodDominio()) &&
				equals(iuv, rpt.getIuv()) &&
				equals(ccp, rpt.getCcp()) &&
				equals(tipoVersamento, rpt.getTipoVersamento()) &&
				equals(anagraficaVersante, rpt.getAnagraficaVersante()) &&
				equals(dataOraMsgRichiesta, rpt.getDataOraMsgRichiesta()) &&
				equals(dataOraCreazione, rpt.getDataOraCreazione()) &&
				equals(codMsgRichiesta, rpt.getCodMsgRichiesta()) &&
				equals(ibanAddebito, rpt.getIbanAddebito()) &&
				equals(firmaRichiesta, rpt.getFirmaRichiesta()) &&
				equals(autenticazioneSoggetto, rpt.getAutenticazioneSoggetto()) &&
				equals(statoRpt, rpt.getStatoRpt()) &&
				equals(faultCode, rpt.getFaultCode()) &&
				equals(descrizioneStato, rpt.getDescrizioneStato()) &&
				equals(callbackURL, rpt.getCallbackURL()) &&
				equals(pspRedirectURL, rpt.getPspRedirectURL()) &&
				equals(idTracciatoXML, rpt.getIdTracciatoXML()) &&
				equals(idPortale, rpt.getIdPortale()) &&
				equals(idPsp, rpt.getIdPsp()) &&
				equals(idStazione, rpt.getIdStazione()) &&
				equals(idCanale, rpt.getIdCanale()) &&
				idVersamento == rpt.getIdVersamento();
		return equal;
	}
	public Date getDataOraCreazione() {
		return dataOraCreazione;
	}
	public void setDataOraCreazione(Date dataOraCreazione) {
		this.dataOraCreazione = dataOraCreazione;
	}
	public String getPspRedirectURL() {
		return pspRedirectURL;
	}
	public void setPspRedirectURL(String pspRedirectURL) {
		this.pspRedirectURL = pspRedirectURL;
	}
	public long getIdStazione() {
		return idStazione;
	}
	public void setIdStazione(long idStazione) {
		this.idStazione = idStazione;
	}
	public Long getIdCanale() {
		return idCanale;
	}
	public void setIdCanale(Long idCanale) {
		this.idCanale = idCanale;
	}

	
}
