/*
 * GovPay - Porta di Accesso al Nodo dei Pagamenti SPC 
 * http://www.gov4j.it/govpay
 * 
 * Copyright (c) 2014-2017 Link.it srl (http://www.link.it).
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
package it.govpay.model;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.commons.lang.ArrayUtils;
import org.openspcoop2.generic_project.exception.ServiceException;

import it.govpay.model.Canale.ModelloPagamento;
import it.govpay.model.Canale.TipoVersamento;

public class Rpt extends BasicModel{
	
	private static final long serialVersionUID = 1L;
	public static final String VERSIONE = "6.2.0";
	public static final String codIntermediarioPspWISP20 = "97735020584"; 
	public static final String codCanaleWISP20 = "97735020584_02"; 
	public static final String codPspWISP20 = "AGID_01"; 
	public static final TipoVersamento tipoVersamentoWISP20 = TipoVersamento.BONIFICO_BANCARIO_TESORERIA; 
	public static final ModelloPagamento modelloPagamentoWISP20 = ModelloPagamento.IMMEDIATO_MULTIBENEFICIARIO; 
	public static final int VERSIONE_ENCODED = 060200;
	
	public static final String CCP_NA = "n/a";
	
	public static final List<StatoRpt> stati_pendenti;
	
	static {
		stati_pendenti = new ArrayList<Rpt.StatoRpt>();
		stati_pendenti.add(StatoRpt.RPT_PARCHEGGIATA_NODO);
		stati_pendenti.add(StatoRpt.RPT_ATTIVATA);
		stati_pendenti.add(StatoRpt.RPT_RICEVUTA_NODO);
		stati_pendenti.add(StatoRpt.RPT_ACCETTATA_NODO);
		stati_pendenti.add(StatoRpt.RPT_INVIATA_A_PSP);
		stati_pendenti.add(StatoRpt.RPT_ACCETTATA_PSP);
		stati_pendenti.add(StatoRpt.RPT_ANNULLATA);
		stati_pendenti.add(StatoRpt.RT_RICEVUTA_NODO);
		stati_pendenti.add(StatoRpt.RT_RIFIUTATA_NODO);
		stati_pendenti.add(StatoRpt.RT_ACCETTATA_NODO);
		stati_pendenti.add(StatoRpt.RT_RIFIUTATA_PA);
		stati_pendenti.add(StatoRpt.RT_ESITO_SCONOSCIUTO_PA);
	}
	
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
			return this.codifica;
		}
		
		public static FirmaRichiesta toEnum(String codifica) throws ServiceException {
			// FIX Bug Nodo che imposta firma vuota in caso di NESSUNA
			if(codifica.isEmpty())
				return NESSUNA;
			
			for(FirmaRichiesta p : FirmaRichiesta.values()){
				if(p.getCodifica().equals(codifica))
					return p;
			}
			
			throw new ServiceException("Codifica inesistente per FirmaRichiesta. Valore fornito [" + codifica + "] valori possibili " + ArrayUtils.toString(FirmaRichiesta.values()));
		}
	}
	
	public enum EsitoPagamento {
		PAGAMENTO_ESEGUITO(0), 
		PAGAMENTO_NON_ESEGUITO(1),
		PAGAMENTO_PARZIALMENTE_ESEGUITO(2),
		DECORRENZA_TERMINI(3), 
		DECORRENZA_TERMINI_PARZIALE(4),
		IN_CORSO(100),
	    RIFIUTATO(101);

		private int codifica;

		EsitoPagamento(int codifica) {
			this.codifica = codifica;
		}

		public int getCodifica() {
			return this.codifica;
		}

		public static EsitoPagamento toEnum(String codifica) throws ServiceException {
			try {
				int codifica2 = Integer.parseInt(codifica);
				return toEnum(codifica2);
			} catch (NumberFormatException e) {
				throw new ServiceException("Codifica inesistente per EsitoPagamento. Valore fornito [" + codifica + "] valori possibili " + ArrayUtils.toString(EsitoPagamento.values()));
			}
		}

		public static EsitoPagamento toEnum(int codifica) throws ServiceException {
			for(EsitoPagamento p : EsitoPagamento.values()){
				if(p.getCodifica() == codifica)
					return p;
			}
			throw new ServiceException("Codifica inesistente per EsitoPagamento. Valore fornito [" + codifica + "] valori possibili " + ArrayUtils.toString(EsitoPagamento.values()));
		}
	}
	
	public enum StatoRpt {
		RPT_PARCHEGGIATA_NODO,
		RPT_ATTIVATA,
		RPT_ERRORE_INVIO_A_NODO, 
		RPT_RICEVUTA_NODO, 
		RPT_RIFIUTATA_NODO, 
		RPT_ACCETTATA_NODO, 
		RPT_RIFIUTATA_PSP, 
		RPT_ACCETTATA_PSP, 
		RPT_ERRORE_INVIO_A_PSP, 
		RPT_INVIATA_A_PSP, 
		RPT_DECORSI_TERMINI,
		RPT_ANNULLATA,
		RT_RICEVUTA_NODO,
		RT_RIFIUTATA_NODO,
		RT_ACCETTATA_NODO,
		RT_ACCETTATA_PA,
		RT_RIFIUTATA_PA,
		RT_ESITO_SCONOSCIUTO_PA,
		INTERNO_NODO;
		
		public static StatoRpt toEnum(String s) {
			try {
				return StatoRpt.valueOf(s);
			} catch (IllegalArgumentException e) {
				return INTERNO_NODO;
			}
		}
	}
	
	public enum StatoConservazione {
		ERRORE,
		RICHIESTA, 
		OK; 
	}
	
	public enum TipoIdentificativoAttestante {
		G,A,B;
	}
	
	private Long id;
	private long idVersamento;
	private Long idApplicazione;
	private String ccp;
	private String codCarrello;
	private String codStazione;
	private String codDominio;
	private String iuv;
	private String codMsgRichiesta;
	private Date dataMsgRichiesta;
	private StatoRpt stato;
	private String descrizioneStato;
	private String codSessione;
	private String codSessionePortale;
	private String pspRedirectURL;
	private byte[] xmlRpt;
	private Date dataAggiornamento;
	private String callbackURL;
	
	private String codCanale;
	private String codPsp;
	private String codIntermediarioPsp;
	private TipoVersamento tipoVersamento;
	private ModelloPagamento modelloPagamento;
	private String codMsgRicevuta;
	private Date dataMsgRicevuta;
	private EsitoPagamento esitoPagamento;
	private BigDecimal importoTotalePagato;
	private byte[] xmlRt;
	
	private String idTransazioneRpt;
	private String idTransazioneRt;
	
	private StatoConservazione statoConservazione;
	private Date dataConservazione;
	private String descrizioneStatoConservazione;
	private Long idPagamentoPortale;
	
	private TipoIdentificativoAttestante tipoIdentificativoAttestante;
	private String identificativoAttestante;
	private String denominazioneAttestante;
	private boolean bloccante;
	
	private String faultCode;
	
	public TipoIdentificativoAttestante getTipoIdentificativoAttestante() {
		return this.tipoIdentificativoAttestante;
	}
	public void setTipoIdentificativoAttestante(TipoIdentificativoAttestante tipoIdentificativoAttestante) {
		this.tipoIdentificativoAttestante = tipoIdentificativoAttestante;
	}
	public String getIdentificativoAttestante() {
		return this.identificativoAttestante;
	}
	public void setIdentificativoAttestante(String identificativoAttestante) {
		this.identificativoAttestante = identificativoAttestante;
	}
	public String getDenominazioneAttestante() {
		return this.denominazioneAttestante;
	}
	public void setDenominazioneAttestante(String denominazioneAttestante) {
		this.denominazioneAttestante = denominazioneAttestante;
	}
	@Override
	public Long getId() {
		return this.id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public long getIdVersamento() {
		return this.idVersamento;
	}
	public void setIdVersamento(long idVersamento) {
		this.idVersamento = idVersamento;
	}
	public Long getIdApplicazione() {
		return this.idApplicazione;
	}
	public void setIdApplicazione(Long idApplicazione) {
		this.idApplicazione = idApplicazione;
	}
	public String getCcp() {
		return this.ccp;
	}
	public void setCcp(String ccp) {
		this.ccp = ccp;
	}
	public String getCodCarrello() {
		return this.codCarrello;
	}
	public void setCodCarrello(String codCarrello) {
		this.codCarrello = codCarrello;
	}
	public String getCodStazione() {
		return this.codStazione;
	}
	public void setCodStazione(String codStazione) {
		this.codStazione = codStazione;
	}
	public String getCodDominio() {
		return this.codDominio;
	}
	public void setCodDominio(String codDominio) {
		this.codDominio = codDominio;
	}
	public String getIuv() {
		return this.iuv;
	}
	public void setIuv(String iuv) {
		this.iuv = iuv;
	}
	public String getCodMsgRichiesta() {
		return this.codMsgRichiesta;
	}
	public void setCodMsgRichiesta(String codMsgRichiesta) {
		this.codMsgRichiesta = codMsgRichiesta;
	}
	public Date getDataMsgRichiesta() {
		return this.dataMsgRichiesta;
	}
	public void setDataMsgRichiesta(Date dataMsgRichiesta) {
		this.dataMsgRichiesta = dataMsgRichiesta;
	}
	public StatoRpt getStato() {
		return this.stato;
	}
	public void setStato(StatoRpt stato) {
		this.stato = stato;
	}
	public String getDescrizioneStato() {
		return this.descrizioneStato;
	}
	public void setDescrizioneStato(String descrizioneStato) {
		this.descrizioneStato = descrizioneStato;
	}
	public String getCodSessione() {
		return this.codSessione;
	}
	public void setCodSessione(String codSessione) {
		this.codSessione = codSessione;
	}
	public String getPspRedirectURL() {
		return this.pspRedirectURL;
	}
	public void setPspRedirectURL(String pspRedirectURL) {
		this.pspRedirectURL = pspRedirectURL;
	}
	public byte[] getXmlRpt() {
		return this.xmlRpt;
	}
	public void setXmlRpt(byte[] xml) {
		this.xmlRpt = xml;
	}
	public Date getDataAggiornamento() {
		return this.dataAggiornamento;
	}
	public void setDataAggiornamento(Date dataAggiornamento) {
		this.dataAggiornamento = dataAggiornamento;
	}
	public String getCallbackURL() {
		return this.callbackURL;
	}
	public void setCallbackURL(String callbackURL) {
		this.callbackURL = callbackURL;
	}
	public Date getDataMsgRicevuta() {
		return this.dataMsgRicevuta;
	}
	public void setDataMsgRicevuta(Date dataMsgRicevuta) {
		this.dataMsgRicevuta = dataMsgRicevuta;
	}
	public String getCodMsgRicevuta() {
		return this.codMsgRicevuta;
	}
	public void setCodMsgRicevuta(String codMsgRicevuta) {
		this.codMsgRicevuta = codMsgRicevuta;
	}
	public EsitoPagamento getEsitoPagamento() {
		return this.esitoPagamento;
	}
	public void setEsitoPagamento(EsitoPagamento esitoPagamento) {
		this.esitoPagamento = esitoPagamento;
	}
	public BigDecimal getImportoTotalePagato() {
		return this.importoTotalePagato;
	}
	public void setImportoTotalePagato(BigDecimal importoTotalePagato) {
		this.importoTotalePagato = importoTotalePagato;
	}
	public byte[] getXmlRt() {
		return this.xmlRt;
	}
	public void setXmlRt(byte[] xml) {
		this.xmlRt = xml;
	}
	public String getIdTransazioneRt() {
		return this.idTransazioneRt;
	}
	public void setIdTransazioneRt(String idTransazioneRt) {
		this.idTransazioneRt = idTransazioneRt;
	}
	public String getIdTransazioneRpt() {
		return this.idTransazioneRpt;
	}
	public void setIdTransazioneRpt(String idTransazioneRpt) {
		this.idTransazioneRpt = idTransazioneRpt;
	}
	
	public String getCodSessionePortale() {
		return this.codSessionePortale;
	}
	public void setCodSessionePortale(String codSessionePortale) {
		this.codSessionePortale = codSessionePortale;
	}
	public StatoConservazione getStatoConservazione() {
		return this.statoConservazione;
	}
	public void setStatoConservazione(StatoConservazione statoConservazione) {
		this.statoConservazione = statoConservazione;
	}
	public Date getDataConservazione() {
		return this.dataConservazione;
	}
	public void setDataConservazione(Date dataConservazione) {
		this.dataConservazione = dataConservazione;
	}
	public String getDescrizioneStatoConservazione() {
		return this.descrizioneStatoConservazione;
	}
	public void setDescrizioneStatoConservazione(String descrizioneStatoConservazione) {
		this.descrizioneStatoConservazione = descrizioneStatoConservazione;
	}
	public Long getIdPagamentoPortale() {
		return this.idPagamentoPortale;
	}
	public void setIdPagamentoPortale(Long idPagamentoPortale) {
		this.idPagamentoPortale = idPagamentoPortale;
	}
	public String getCodCanale() {
		return this.codCanale;
	}
	public void setCodCanale(String codCanale) {
		this.codCanale = codCanale;
	}
	public String getCodPsp() {
		return this.codPsp;
	}
	public void setCodPsp(String codPsp) {
		this.codPsp = codPsp;
	}
	public String getCodIntermediarioPsp() {
		return this.codIntermediarioPsp;
	}
	public void setCodIntermediarioPsp(String codIntermediarioPsp) {
		this.codIntermediarioPsp = codIntermediarioPsp;
	}
	public TipoVersamento getTipoVersamento() {
		return this.tipoVersamento;
	}
	public void setTipoVersamento(TipoVersamento tipoVersamento) {
		this.tipoVersamento = tipoVersamento;
	}
	public ModelloPagamento getModelloPagamento() {
		return this.modelloPagamento;
	}
	public void setModelloPagamento(ModelloPagamento modelloPagamento) {
		this.modelloPagamento = modelloPagamento;
	}
	public boolean isBloccante() {
		return bloccante;
	}
	public void setBloccante(boolean bloccante) {
		this.bloccante = bloccante;
	}
	public String getFaultCode() {
		return faultCode;
	}
	public void setFaultCode(String faultCode) {
		this.faultCode = faultCode;
	}

}
