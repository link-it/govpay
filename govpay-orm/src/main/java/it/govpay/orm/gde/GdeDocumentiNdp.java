/*
 * GovPay - Porta di Accesso al Nodo dei Pagamenti SPC 
 * http://www.gov4j.it/govpay
 * 
 * Copyright (c) 2014-2015 Link.it srl (http://www.link.it).
 * Copyright (c) 2014-2015 TAS S.p.A. (http://www.tasgroup.it).
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
package it.govpay.orm.gde;

import it.govpay.orm.BaseEntity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;

@Entity
@Table(name = "gde_documenti_ndp")
@NamedQueries({
	@NamedQuery(name = "getGdeDocNdp", query = "select d from GdeDocumentiNdp d where d.idDominio = :idDominio and d.idUnivocoVersamento = :iuv and d.codiceContestoPagamento = :ccp and d.tipo = :tipo order by d.tentativo desc")
})
public class GdeDocumentiNdp extends BaseEntity {

	private static final long serialVersionUID = 1L;

	private Long id;
	private String idDominio;
	private String idUnivocoVersamento;
	private String codiceContestoPagamento;
	private int tentativo;
	private String tipo;
	private Integer dimensione;
	private byte[] documento;

	public GdeDocumentiNdp() {
	}

	@Id
	@Column(name = "ID")
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	@Column(name = "ID_DOMINIO", nullable = false, length = 35)
	public String getIdDominio() {
		return this.idDominio;
	}

	public void setIdDominio(String idDominio) {
		this.idDominio = idDominio;
	}

	@Column(name = "ID_UNIVOCO_VERSAMENTO", nullable = false, length = 35)
	public String getIdUnivocoVersamento() {
		return this.idUnivocoVersamento;
	}

	public void setIdUnivocoVersamento(String idUnivocoVersamento) {
		this.idUnivocoVersamento = idUnivocoVersamento;
	}

	@Column(name = "CODICE_CONTESTO_PAGAMENTO", nullable = false, length = 35)
	public String getCodiceContestoPagamento() {
		return this.codiceContestoPagamento;
	}

	public void setCodiceContestoPagamento(String codiceContestoPagamento) {
		this.codiceContestoPagamento = codiceContestoPagamento;
	}

	@Column(name = "TENTATIVO", nullable = false)
	public int getTentativo() {
		return this.tentativo;
	}

	public void setTentativo(int tentativo) {
		this.tentativo = tentativo;
	}

	@Column(name = "TIPO", length = 2)
	public String getTipo() {
		return this.tipo;
	}

	public void setTipo(String tipo) {
		this.tipo = tipo;
	}

	@Column(name = "DIMENSIONE")
	public Integer getDimensione() {
		return this.dimensione;
	}

	public void setDimensione(Integer dimensione) {
		this.dimensione = dimensione;
	}

	@Column(name = "DOCUMENTO", nullable = false)
	public byte[] getDocumento() {
		return this.documento;
	}

	public void setDocumento(byte[] documento) {
		this.documento = documento;
	}

}
