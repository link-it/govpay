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

import java.math.BigDecimal;
import java.util.Date;

import org.apache.commons.lang.ArrayUtils;
import org.openspcoop2.generic_project.exception.ServiceException;

public class Rt extends BasicModel {
	private static final long serialVersionUID = 1L;
	public static final String NO_IUR = "n/a";

	public enum StatoRt {
		ACCETTATA('A'),
		RIFIUTATA('R');

		private char codifica;

		StatoRt(char codifica) {
			this.codifica = codifica;
		}

		public char getCodifica() {
			return codifica;
		}

		public static StatoRt toEnum(char codifica) throws ServiceException {
			for(StatoRt p : StatoRt.values()){
				if(p.getCodifica() == codifica)
					return p;
			}
			throw new ServiceException("Codifica inesistente per StatoRt. Valore fornito [" + codifica + "] valori possibili " + ArrayUtils.toString(EsitoPagamento.values()));
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

	public enum FaultPa implements NdpFaultCode {
		PAA_ID_DOMINIO_ERRATO,
		PAA_ID_INTERMEDIARIO_ERRATO,
		PAA_STAZIONE_INT_ERRATA,
		PAA_RPT_SCONOSCIUTA,
		PAA_RT_DUPLICATA,
		PAA_TIPOFIRMA_SCONOSCIUTO,
		PAA_ERRORE_FORMATO_BUSTA_FIRMATA,
		PAA_FIRMA_ERRATA,
		PAA_FIRMA_INDISPONIBILE,
		PAA_PAGAMENTO_SCONOSCIUTO,
		PAA_PAGAMENTO_DUPLICATO,
		PAA_PAGAMENTO_IN_CORSO,
		PAA_PAGAMENTO_ANNULLATO,
		PAA_PAGAMENTO_SCADUTO,
		PAA_SINTASSI_XSD,
		PAA_SINTASSI_EXTRAXSD,
		PAA_SEMANTICA,
		PAA_ER_DUPLICATA, 
		PAA_ERRORE_INTERNO,
		PAA_ATTIVA_RPT_IMPORTO_NON_VALIDO;
	}

	private Long id;
	private long idRpt;
	private Long idTracciatoXML;
	private String codMsgRicevuta;
	private Date dataOraMsgRicevuta;
	private Anagrafica anagraficaAttestante;
	private EsitoPagamento esitoPagamento;
	private BigDecimal importoTotalePagato;
	private StatoRt stato;
	private String descrizioneStato;


	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public long getIdRpt() {
		return idRpt;
	}
	public void setIdRpt(long idRpt) {
		this.idRpt = idRpt;
	}
	public Anagrafica getAnagraficaAttestante() {
		return anagraficaAttestante;
	}
	public void setAnagraficaAttestante(Anagrafica anagraficaAttestante) {
		this.anagraficaAttestante = anagraficaAttestante;
	}
	public Date getDataOraMsgRicevuta() {
		return dataOraMsgRicevuta;
	}
	public void setDataOraMsgRicevuta(Date dataOraMsgRicevuta) {
		this.dataOraMsgRicevuta = dataOraMsgRicevuta;
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
	public Long getIdTracciatoXML() {
		return idTracciatoXML;
	}
	public void setIdTracciatoXML(Long idTracciatoXML) {
		this.idTracciatoXML = idTracciatoXML;
	}
	public StatoRt getStato() {
		return stato;
	}
	public void setStato(StatoRt stato) {
		this.stato = stato;
	}
	public String getDescrizioneStato() {
		return descrizioneStato;
	}
	public void setDescrizioneStato(String descrizioneStato) {
		this.descrizioneStato = descrizioneStato;
	}
	public BigDecimal getImportoTotalePagato() {
		return importoTotalePagato;
	}
	public void setImportoTotalePagato(BigDecimal importoTotalePagato) {
		this.importoTotalePagato = importoTotalePagato;
	}

	@Override
	public boolean equals(Object obj) {
		Rt rt = null;
		if(obj instanceof Rt) {
			rt = (Rt) obj;
		} else {
			return false;
		}
		boolean equal = 
				equals(anagraficaAttestante, rt.getAnagraficaAttestante()) &&
				equals(dataOraMsgRicevuta, rt.getDataOraMsgRicevuta()) &&
				equals(codMsgRicevuta, rt.getCodMsgRicevuta()) &&
				equals(esitoPagamento, rt.getEsitoPagamento()) &&
				equals(descrizioneStato, rt.getDescrizioneStato()) &&
				equals(stato, rt.getStato()) &&
				equals(idTracciatoXML, rt.getIdTracciatoXML()) &&
				equals(importoTotalePagato, rt.getImportoTotalePagato()) &&
				idRpt == rt.getIdRpt();

		return equal;
	}


}
