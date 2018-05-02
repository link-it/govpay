package it.govpay.core.dao.pagamenti.dto;

import java.util.Date;

import org.openspcoop2.generic_project.expression.SortOrder;

import it.govpay.bd.pagamento.filters.PagamentoFilter.TIPO_PAGAMENTO;
import it.govpay.core.dao.anagrafica.dto.BasicFindRequestDTO;
import it.govpay.model.IAutorizzato;
import it.govpay.model.Pagamento.Stato;
import it.govpay.orm.Pagamento;

public class ListaRiscossioniDTO extends BasicFindRequestDTO{
	
	public ListaRiscossioniDTO(IAutorizzato user) {
		super(user);
		this.addSortField("data", Pagamento.model().DATA_PAGAMENTO);
		this.addSortField("stato", Pagamento.model().STATO);
		this.setDefaultSort(Pagamento.model().DATA_PAGAMENTO,SortOrder.DESC);
	}
	
	private Date dataRiscossioneDa;
	private Date dataRiscossioneA;
	private TIPO_PAGAMENTO tipo;
	private String idPendenza;
	private String idA2A;
	private String idDominio;
	private Stato stato;

	public Date getDataRiscossioneDa() {
		return dataRiscossioneDa;
	}

	public void setDataRiscossioneDa(Date dataRiscossioneDa) {
		this.dataRiscossioneDa = dataRiscossioneDa;
	}

	public Date getDataRiscossioneA() {
		return dataRiscossioneA;
	}

	public void setDataRiscossioneA(Date dataRiscossioneA) {
		this.dataRiscossioneA = dataRiscossioneA;
	}

	public TIPO_PAGAMENTO getTipo() {
		return tipo;
	}

	public void setTipo(TIPO_PAGAMENTO tipo) {
		this.tipo = tipo;
	}

	public String getIdPendenza() {
		return idPendenza;
	}

	public void setIdPendenza(String idPendenza) {
		this.idPendenza = idPendenza;
	}

	public String getIdA2A() {
		return idA2A;
	}

	public void setIdA2A(String idA2A) {
		this.idA2A = idA2A;
	}

	public String getIdDominio() {
		return idDominio;
	}

	public void setIdDominio(String idDominio) {
		this.idDominio = idDominio;
	}

	public Stato getStato() {
		return stato;
	}

	public void setStato(Stato stato) {
		this.stato = stato;
	}
}
