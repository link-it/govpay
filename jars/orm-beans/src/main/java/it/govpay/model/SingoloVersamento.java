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

import org.apache.commons.lang.ArrayUtils;
import org.openspcoop2.generic_project.exception.ServiceException;

public class SingoloVersamento extends BasicModel implements Comparable<SingoloVersamento> {
	private static final long serialVersionUID = 1L;

	public enum StatoSingoloVersamento {
		ESEGUITO,
		NON_ESEGUITO;
	}
	
	public enum TipoBollo {
		IMPOSTA_BOLLO("01", "Imposta di bollo");
		
		private static final String[] VALORI_POSSIBILI = { "Imposta di bollo" };
		private String codificaPagoPA, codificaJson;
		TipoBollo(String codificaPagoPA, String codificaJson) {
			this.codificaPagoPA = codificaPagoPA;
			this.codificaJson = codificaJson;
		}
		public String getCodifica() {
			return this.codificaPagoPA;
		}
		public String getCodificaJson() {
			return this.codificaJson;
		}
		public static TipoBollo toEnum(String codifica) throws ServiceException {
			for(TipoBollo p : TipoBollo.values()){
				if(p.getCodifica().equals(codifica) || p.getCodificaJson().equals(codifica))
					return p;
			}
			throw new ServiceException("Codifica inesistente per tipoBollo. Valore fornito [" + codifica + "] valori possibili " + ArrayUtils.toString(VALORI_POSSIBILI));
		}
	}
	
	private Long id;
	private Long idTributo;
	private long idVersamento;
	private String codSingoloVersamentoEnte;
	private StatoSingoloVersamento statoSingoloVersamento;
	private BigDecimal importoSingoloVersamento;
	private TipoBollo tipoBollo;
	private String hashDocumento;
	private String provinciaResidenza;
	private Long idIbanAccredito;
	private Long idIbanAppoggio;
	private Tributo.TipoContabilita tipoContabilita;
	private String codContabilita;
	private String datiAllegati;
	private String descrizione;
	private Integer indiceDati;
	private String descrizioneCausaleRPT;
	
	
	@Override
	public Long getId() {
		return this.id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Long getIdTributo() {
		return this.idTributo;
	}

	public void setIdTributo(Long idTributo) {
		this.idTributo = idTributo;
	}

	public Long getIdVersamento() {
		return this.idVersamento;
	}

	public void setIdVersamento(long idVersamento) {
		this.idVersamento = idVersamento;
	}

	public String getCodSingoloVersamentoEnte() {
		return this.codSingoloVersamentoEnte;
	}

	public void setCodSingoloVersamentoEnte(String codSingoloVersamentoEnte) {
		this.codSingoloVersamentoEnte = codSingoloVersamentoEnte;
	}

	public StatoSingoloVersamento getStatoSingoloVersamento() {
		return this.statoSingoloVersamento;
	}

	public void setStatoSingoloVersamento(
			StatoSingoloVersamento statoSingoloVersamento) {
		this.statoSingoloVersamento = statoSingoloVersamento;
	}

	public BigDecimal getImportoSingoloVersamento() {
		return this.importoSingoloVersamento;
	}

	public void setImportoSingoloVersamento(BigDecimal importoSingoloVersamento) {
		this.importoSingoloVersamento = importoSingoloVersamento;
	}

	public TipoBollo getTipoBollo() {
		return this.tipoBollo;
	}

	public void setTipoBollo(TipoBollo tipoBollo) {
		this.tipoBollo = tipoBollo;
	}

	public String getHashDocumento() {
		return this.hashDocumento;
	}

	public void setHashDocumento(String hashDocumento) {
		this.hashDocumento = hashDocumento;
	}

	public String getProvinciaResidenza() {
		return this.provinciaResidenza;
	}

	public void setProvinciaResidenza(String provinciaResidenza) {
		this.provinciaResidenza = provinciaResidenza;
	}
	
	public Long getIdIbanAccredito() {
		return this.idIbanAccredito;
	}

	public void setIdIbanAccredito(Long idIbanAccredito) {
		this.idIbanAccredito = idIbanAccredito;
	}

	public Tributo.TipoContabilita getTipoContabilita() {
		return this.tipoContabilita;
	}

	public void setTipoContabilita(Tributo.TipoContabilita tipoContabilita) {
		this.tipoContabilita = tipoContabilita;
	}

	public String getCodContabilita() {
		return this.codContabilita;
	}

	public void setCodContabilita(String codContabilita) {
		this.codContabilita = codContabilita;
	}
	
	public String getDatiAllegati() {
		return this.datiAllegati;
	}

	public void setDatiAllegati(String datiAllegati) {
		this.datiAllegati = datiAllegati;
	}

	public String getDescrizione() {
		return this.descrizione;
	}

	public void setDescrizione(String descrizione) {
		this.descrizione = descrizione;
	}

	@Override
	public int compareTo(SingoloVersamento sv) {
		return this.codSingoloVersamentoEnte.compareTo(sv.getCodSingoloVersamentoEnte());
	}

	public Long getIdIbanAppoggio() {
		return this.idIbanAppoggio;
	}

	public void setIdIbanAppoggio(Long idIbanAppoggio) {
		this.idIbanAppoggio = idIbanAppoggio;
	}

	public Integer getIndiceDati() {
		return indiceDati;
	}

	public void setIndiceDati(Integer indiceDati) {
		this.indiceDati = indiceDati;
	}

	public String getDescrizioneCausaleRPT() {
		return descrizioneCausaleRPT;
	}

	public void setDescrizioneCausaleRPT(String descrizioneCausaleRPT) {
		this.descrizioneCausaleRPT = descrizioneCausaleRPT;
	}
	
}

