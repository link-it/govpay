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

public class Rpt extends BasicModel{
	
	private static final long serialVersionUID = 1L;
	public static final String VERSIONE = "6.2";
	public static final int VERSIONE_ENCODED = 060200;
	
	public static final String CCP_NA = "n/a";
	
	public static final List<StatoRpt> stati_pendenti;
	
	static {
		stati_pendenti = new ArrayList<Rpt.StatoRpt>();
		stati_pendenti.add(StatoRpt.RPT_ATTIVATA);
		stati_pendenti.add(StatoRpt.RPT_RICEVUTA_NODO);
		stati_pendenti.add(StatoRpt.RPT_ACCETTATA_NODO);
		stati_pendenti.add(StatoRpt.RPT_INVIATA_A_PSP);
		stati_pendenti.add(StatoRpt.RPT_ACCETTATA_PSP);
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
			return codifica;
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
		DECORRENZA_TERMINI_PARZIALE(4);

		private int codifica;

		EsitoPagamento(int codifica) {
			this.codifica = codifica;
		}

		public int getCodifica() {
			return codifica;
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
	
	private Long id;
	private long idVersamento;
	private long idCanale;
	private Long idPortale;
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
	private ModelloPagamento modelloPagamento;
	private FirmaRichiesta firmaRichiesta;
	
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
	public long getIdCanale() {
		return idCanale;
	}
	public void setIdCanale(long idCanale) {
		this.idCanale = idCanale;
	}
	public Long getIdPortale() {
		return idPortale;
	}
	public void setIdPortale(Long idPortale) {
		this.idPortale = idPortale;
	}
	public String getCcp() {
		return ccp;
	}
	public void setCcp(String ccp) {
		this.ccp = ccp;
	}
	public String getCodCarrello() {
		return codCarrello;
	}
	public void setCodCarrello(String codCarrello) {
		this.codCarrello = codCarrello;
	}
	public String getCodStazione() {
		return codStazione;
	}
	public void setCodStazione(String codStazione) {
		this.codStazione = codStazione;
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
	public String getCodMsgRichiesta() {
		return codMsgRichiesta;
	}
	public void setCodMsgRichiesta(String codMsgRichiesta) {
		this.codMsgRichiesta = codMsgRichiesta;
	}
	public Date getDataMsgRichiesta() {
		return dataMsgRichiesta;
	}
	public void setDataMsgRichiesta(Date dataMsgRichiesta) {
		this.dataMsgRichiesta = dataMsgRichiesta;
	}
	public StatoRpt getStato() {
		return stato;
	}
	public void setStato(StatoRpt stato) {
		this.stato = stato;
	}
	public String getDescrizioneStato() {
		return descrizioneStato;
	}
	public void setDescrizioneStato(String descrizioneStato) {
		this.descrizioneStato = descrizioneStato;
	}
	public String getCodSessione() {
		return codSessione;
	}
	public void setCodSessione(String codSessione) {
		this.codSessione = codSessione;
	}
	public String getPspRedirectURL() {
		return pspRedirectURL;
	}
	public void setPspRedirectURL(String pspRedirectURL) {
		this.pspRedirectURL = pspRedirectURL;
	}
	public byte[] getXmlRpt() {
		return xmlRpt;
	}
	public void setXmlRpt(byte[] xml) {
		this.xmlRpt = xml;
	}
	public Date getDataAggiornamento() {
		return dataAggiornamento;
	}
	public void setDataAggiornamento(Date dataAggiornamento) {
		this.dataAggiornamento = dataAggiornamento;
	}
	public String getCallbackURL() {
		return callbackURL;
	}
	public void setCallbackURL(String callbackURL) {
		this.callbackURL = callbackURL;
	}
	public ModelloPagamento getModelloPagamento() {
		return modelloPagamento;
	}
	public void setModelloPagamento(ModelloPagamento modelloPagamento) {
		this.modelloPagamento = modelloPagamento;
	}
	public FirmaRichiesta getFirmaRichiesta() {
		return firmaRichiesta;
	}
	public void setFirmaRichiesta(FirmaRichiesta firmaRichiesta) {
		this.firmaRichiesta = firmaRichiesta;
	}
	public Date getDataMsgRicevuta() {
		return dataMsgRicevuta;
	}
	public void setDataMsgRicevuta(Date dataMsgRicevuta) {
		this.dataMsgRicevuta = dataMsgRicevuta;
	}
	public String getCodMsgRicevuta() {
		return codMsgRicevuta;
	}
	public void setCodMsgRicevuta(String codMsgRicevuta) {
		this.codMsgRicevuta = codMsgRicevuta;
	}
	public EsitoPagamento getEsitoPagamento() {
		return esitoPagamento;
	}
	public void setEsitoPagamento(EsitoPagamento esitoPagamento) {
		this.esitoPagamento = esitoPagamento;
	}
	public BigDecimal getImportoTotalePagato() {
		return importoTotalePagato;
	}
	public void setImportoTotalePagato(BigDecimal importoTotalePagato) {
		this.importoTotalePagato = importoTotalePagato;
	}
	public byte[] getXmlRt() {
		return xmlRt;
	}
	public void setXmlRt(byte[] xml) {
		this.xmlRt = xml;
	}
	public String getIdTransazioneRt() {
		return idTransazioneRt;
	}
	public void setIdTransazioneRt(String idTransazioneRt) {
		this.idTransazioneRt = idTransazioneRt;
	}
	public String getIdTransazioneRpt() {
		return idTransazioneRpt;
	}
	public void setIdTransazioneRpt(String idTransazioneRpt) {
		this.idTransazioneRpt = idTransazioneRpt;
	}
	
	public String getCodSessionePortale() {
		return codSessionePortale;
	}
	public void setCodSessionePortale(String codSessionePortale) {
		this.codSessionePortale = codSessionePortale;
	}
	public StatoConservazione getStatoConservazione() {
		return statoConservazione;
	}
	public void setStatoConservazione(StatoConservazione statoConservazione) {
		this.statoConservazione = statoConservazione;
	}
	public Date getDataConservazione() {
		return dataConservazione;
	}
	public void setDataConservazione(Date dataConservazione) {
		this.dataConservazione = dataConservazione;
	}
	public String getDescrizioneStatoConservazione() {
		return descrizioneStatoConservazione;
	}
	public void setDescrizioneStatoConservazione(String descrizioneStatoConservazione) {
		this.descrizioneStatoConservazione = descrizioneStatoConservazione;
	}

}
