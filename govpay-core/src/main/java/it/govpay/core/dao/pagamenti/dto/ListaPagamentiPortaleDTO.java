package it.govpay.core.dao.pagamenti.dto;

import java.util.Date;
import java.util.List;

import it.govpay.bd.FilterSortWrapper;
import it.govpay.bd.model.PagamentoPortale.STATO;

public class ListaPagamentiPortaleDTO {

	private String principal;
	private Integer offset;
	private Integer limit;
	private Date dataA;
	private Date dataDa;
	private STATO stato;
	private String versante;
	private List<FilterSortWrapper> sort;
	public Integer getOffset() {
		return offset;
	}
	public void setOffset(Integer offset) {
		this.offset = offset;
	}
	public Integer getLimit() {
		return limit;
	}
	public void setLimit(Integer limit) {
		this.limit = limit;
	}
	public Date getDataA() {
		return dataA;
	}
	public void setDataA(Date dataA) {
		this.dataA = dataA;
	}
	public Date getDataDa() {
		return dataDa;
	}
	public void setDataDa(Date dataDa) {
		this.dataDa = dataDa;
	}
	public STATO getStato() {
		return stato;
	}
	public void setStato(STATO stato) {
		this.stato = stato;
	}
	public String getVersante() {
		return versante;
	}
	public void setVersante(String versante) {
		this.versante = versante;
	}
	public String getPrincipal() {
		return principal;
	}
	public void setPrincipal(String principal) {
		this.principal = principal;
	}
	public List<FilterSortWrapper> getSort() {
		return sort;
	}
	public void setSort(List<FilterSortWrapper> sort) {
		this.sort = sort;
	}
}
