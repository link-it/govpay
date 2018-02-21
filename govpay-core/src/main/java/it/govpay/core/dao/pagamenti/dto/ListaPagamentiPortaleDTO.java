package it.govpay.core.dao.pagamenti.dto;

import java.util.Date;

import org.openspcoop2.generic_project.beans.IField;

import it.govpay.bd.model.PagamentoPortale.STATO;
import it.govpay.core.dao.anagrafica.dto.BasicFindRequestDTO;
import it.govpay.core.exceptions.InternalException;
import it.govpay.core.exceptions.RequestParamException;
import it.govpay.model.IAutorizzato;
import it.govpay.orm.PagamentoPortale;

public class ListaPagamentiPortaleDTO extends BasicFindRequestDTO{
	
	public enum Field {
		dataRichiestaPagamento(PagamentoPortale.model().DATA_RICHIESTA),
		stato(PagamentoPortale.model().STATO);
		
		private IField ifield;

		private Field(IField ifield) {
			this.ifield = ifield;
		}
		
		public IField getField(){
			return ifield;
		}
	}

	public ListaPagamentiPortaleDTO(IAutorizzato user) {
		super(user);
	}
	private Date dataA;
	private Date dataDa;
	private STATO stato;
	private String versante;
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
	public void setOrderBy(String orderBy) throws RequestParamException, InternalException {
		setOrderBy(Field.class, orderBy);
	}
}
