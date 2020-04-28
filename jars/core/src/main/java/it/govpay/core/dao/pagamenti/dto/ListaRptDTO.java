package it.govpay.core.dao.pagamenti.dto;

import java.util.Date;
import java.util.List;

import org.openspcoop2.generic_project.expression.SortOrder;
import org.springframework.security.core.Authentication;

import it.govpay.core.dao.anagrafica.dto.BasicFindRequestDTO;
import it.govpay.model.Rpt.EsitoPagamento;
import it.govpay.model.Rpt.StatoRpt;
import it.govpay.orm.VistaRptVersamento;

public class ListaRptDTO extends BasicFindRequestDTO{

	public ListaRptDTO(Authentication user) {
		super(user);
		this.addSortField("dataRichiesta", VistaRptVersamento.model().DATA_MSG_RICHIESTA);
		this.addSortField("stato", VistaRptVersamento.model().STATO);
		this.addDefaultSort(VistaRptVersamento.model().DATA_MSG_RICHIESTA,SortOrder.DESC);
	}
	private Date dataA;
	private Date dataDa;
	private StatoRpt stato;
	private String idDominio;
	private String iuv;
	private String ccp;
	private String idA2A;
	private String idPendenza;
	private String idPagamento;
	private String cfCittadino;
	private String idA2APagamentoPortale;
	private EsitoPagamento esitoPagamento;
	
	private Date dataRtA;
	private Date dataRtDa;
	private String idDebitore;
	private String idUnita;
	private String idTipoPendenza;
	private List<String> direzione;
	private List<String> divisione;
	private String tassonomia;
	private String anagraficaDebitore;
	
	public Date getDataRtA() {
		return dataRtA;
	}
	public void setDataRtA(Date dataRtA) {
		this.dataRtA = dataRtA;
	}
	public Date getDataRtDa() {
		return dataRtDa;
	}
	public void setDataRtDa(Date dataRtDa) {
		this.dataRtDa = dataRtDa;
	}
	public String getIdDebitore() {
		return idDebitore;
	}
	public void setIdDebitore(String idDebitore) {
		this.idDebitore = idDebitore;
	}
	public Date getDataA() {
		return this.dataA;
	}
	public void setDataA(Date dataA) {
		this.dataA = dataA;
	}
	public Date getDataDa() {
		return this.dataDa;
	}
	public void setDataDa(Date dataDa) {
		this.dataDa = dataDa;
	}
	public StatoRpt getStato() {
		return this.stato;
	}
	public void setStato(StatoRpt stato) {
		this.stato = stato;
	}
	public String getIdDominio() {
		return this.idDominio;
	}
	public void setIdDominio(String idDominio) {
		this.idDominio = idDominio;
	}
	public String getIuv() {
		return this.iuv;
	}
	public void setIuv(String iuv) {
		this.iuv = iuv;
	}
	public String getCcp() {
		return this.ccp;
	}
	public void setCcp(String ccp) {
		this.ccp = ccp;
	}
	public String getIdA2A() {
		return this.idA2A;
	}
	public void setIdA2A(String idA2A) {
		this.idA2A = idA2A;
	}
	public String getIdPendenza() {
		return this.idPendenza;
	}
	public void setIdPendenza(String idPendenza) {
		this.idPendenza = idPendenza;
	}
	public String getIdPagamento() {
		return this.idPagamento;
	}
	public void setIdPagamento(String idPagamento) {
		this.idPagamento = idPagamento;
	}
	public String getIdA2APagamentoPortale() {
		return idA2APagamentoPortale;
	}
	public void setIdA2APagamentoPortale(String idA2APagamentoPortale) {
		this.idA2APagamentoPortale = idA2APagamentoPortale;
	}
	public String getCfCittadino() {
		return cfCittadino;
	}
	public void setCfCittadino(String cfCittadino) {
		this.cfCittadino = cfCittadino;
	}
	public EsitoPagamento getEsitoPagamento() {
		return esitoPagamento;
	}
	public void setEsitoPagamento(EsitoPagamento esitoPagamento) {
		this.esitoPagamento = esitoPagamento;
	}
	public String getIdUnita() {
		return idUnita;
	}
	public void setIdUnita(String idUnita) {
		this.idUnita = idUnita;
	}
	public String getIdTipoPendenza() {
		return idTipoPendenza;
	}
	public void setIdTipoPendenza(String idTipoPendenza) {
		this.idTipoPendenza = idTipoPendenza;
	}
	public List<String> getDirezione() {
		return direzione;
	}
	public void setDirezione(List<String> direzione) {
		this.direzione = direzione;
	}
	public List<String> getDivisione() {
		return divisione;
	}
	public void setDivisione(List<String> divisione) {
		this.divisione = divisione;
	}
	public String getTassonomia() {
		return tassonomia;
	}
	public void setTassonomia(String tassonomia) {
		this.tassonomia = tassonomia;
	}
	public String getAnagraficaDebitore() {
		return anagraficaDebitore;
	}
	public void setAnagraficaDebitore(String anagraficaDebitore) {
		this.anagraficaDebitore = anagraficaDebitore;
	}
	
}
