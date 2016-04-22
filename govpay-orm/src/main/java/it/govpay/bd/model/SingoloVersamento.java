/*
 * GovPay - Porta di Accesso al Nodo dei Pagamenti SPC 
 * http://www.gov4j.it/govpay
 * 
 * Copyright (c) 2014-2016 Link.it srl (http://www.link.it).
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

import it.govpay.bd.BasicBD;
import it.govpay.bd.anagrafica.AnagraficaManager;
import it.govpay.bd.pagamento.VersamentiBD;

import java.math.BigDecimal;

import org.apache.commons.lang.ArrayUtils;
import org.openspcoop2.generic_project.exception.NotFoundException;
import org.openspcoop2.generic_project.exception.ServiceException;

public class SingoloVersamento extends BasicModel implements Comparable<SingoloVersamento> {
	private static final long serialVersionUID = 1L;

	public enum StatoSingoloVersamento {
		ESEGUITO,
		NON_ESEGUITO,
		ANOMALO;
	}
	
	public enum TipoBollo {
		IMPOSTA_BOLLO("01");
		
		private String codifica;
		TipoBollo(String codifica) {
			this.codifica = codifica;
		}
		public String getCodifica() {
			return codifica;
		}
		public static TipoBollo toEnum(String codifica) throws ServiceException {
			for(TipoBollo p : TipoBollo.values()){
				if(p.getCodifica().equals(codifica))
					return p;
			}
			throw new ServiceException("Codifica inesistente per TipoBollo. Valore fornito [" + codifica + "] valori possibili " + ArrayUtils.toString(TipoBollo.values()));
		}
	}
	
	private Long id;
	private long idTributo;
	private long idVersamento;
	private String codSingoloVersamentoEnte;
	private StatoSingoloVersamento statoSingoloVersamento;
	private BigDecimal importoSingoloVersamento;
	private TipoBollo tipoBollo;
	private String hashDocumento;
	private String provinciaResidenza;
	
	
	
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public long getIdTributo() {
		return idTributo;
	}

	public void setIdTributo(long idTributo) {
		this.idTributo = idTributo;
	}

	public long getIdVersamento() {
		return idVersamento;
	}

	public void setIdVersamento(long idVersamento) {
		this.idVersamento = idVersamento;
	}

	public String getCodSingoloVersamentoEnte() {
		return codSingoloVersamentoEnte;
	}

	public void setCodSingoloVersamentoEnte(String codSingoloVersamentoEnte) {
		this.codSingoloVersamentoEnte = codSingoloVersamentoEnte;
	}

	public StatoSingoloVersamento getStatoSingoloVersamento() {
		return statoSingoloVersamento;
	}

	public void setStatoSingoloVersamento(
			StatoSingoloVersamento statoSingoloVersamento) {
		this.statoSingoloVersamento = statoSingoloVersamento;
	}

	public BigDecimal getImportoSingoloVersamento() {
		return importoSingoloVersamento;
	}

	public void setImportoSingoloVersamento(BigDecimal importoSingoloVersamento) {
		this.importoSingoloVersamento = importoSingoloVersamento;
	}

	public TipoBollo getTipoBollo() {
		return tipoBollo;
	}

	public void setTipoBollo(TipoBollo tipoBollo) {
		this.tipoBollo = tipoBollo;
	}

	public String getHashDocumento() {
		return hashDocumento;
	}

	public void setHashDocumento(String hashDocumento) {
		this.hashDocumento = hashDocumento;
	}

	public String getProvinciaResidenza() {
		return provinciaResidenza;
	}

	public void setProvinciaResidenza(String provinciaResidenza) {
		this.provinciaResidenza = provinciaResidenza;
	}

	@Override
	public int compareTo(SingoloVersamento sv) {
		return codSingoloVersamentoEnte.compareTo(sv.getCodSingoloVersamentoEnte());
	}
	
	// Business
	
	private Versamento versamento;
	private Tributo tributo;
	
	public Tributo getTributo(BasicBD bd) throws ServiceException {
		if(tributo == null) {
			tributo = AnagraficaManager.getTributo(bd, getIdTributo());
		}
		return tributo;
	}

	public void setTributo(String codTributo, BasicBD bd) throws ServiceException, NotFoundException {
		this.tributo = AnagraficaManager.getTributo(bd, versamento.getUo(bd).getIdDominio(), codTributo);
		this.idTributo = tributo.getId();
	}
	
	public void setVersamento(Versamento versamento) {
		this.versamento = versamento;
		if(versamento.getId() != null)
			this.idVersamento = versamento.getId();
	}
	
	public Versamento getVersamento(BasicBD bd) throws ServiceException {
		if(this.versamento == null) {
			VersamentiBD versamentiBD = new VersamentiBD(bd);
			this.versamento = versamentiBD.getVersamento(getIdVersamento());
		}
		return this.versamento;
	}
}

