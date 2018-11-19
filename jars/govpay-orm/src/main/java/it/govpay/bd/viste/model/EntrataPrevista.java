package it.govpay.bd.viste.model;

import java.math.BigDecimal;
import java.util.Comparator;
import java.util.Date;

import it.govpay.model.BasicModel;

public class EntrataPrevista extends BasicModel implements Comparable<EntrataPrevista>{
	
	private static final long serialVersionUID = 1L;
	private String codDominio;
	private String iuv;
	private String iur;
	private String codFlusso;
	private String frIur;
	private Date dataRegolamento;
	private long numeroPagamenti;
	private BigDecimal importoTotalePagamenti;
	private BigDecimal importoPagato;
	private Date data;
	private String codSingoloVersamentoEnte;
	private Integer indiceDati;
	private String codVersamentoEnte;
	private String codApplicazione;
	
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
	public String getIur() {
		return iur;
	}
	public void setIur(String iur) {
		this.iur = iur;
	}
	public String getCodFlusso() {
		return codFlusso;
	}
	public void setCodFlusso(String codFlusso) {
		this.codFlusso = codFlusso;
	}
	public String getFrIur() {
		return frIur;
	}
	public void setFrIur(String frIur) {
		this.frIur = frIur;
	}
	public Date getDataRegolamento() {
		return dataRegolamento;
	}
	public void setDataRegolamento(Date dataRegolamento) {
		this.dataRegolamento = dataRegolamento;
	}
	public long getNumeroPagamenti() {
		return numeroPagamenti;
	}
	public void setNumeroPagamenti(long numeroPagamenti) {
		this.numeroPagamenti = numeroPagamenti;
	}
	public BigDecimal getImportoTotalePagamenti() {
		return importoTotalePagamenti;
	}
	public void setImportoTotalePagamenti(BigDecimal importoTotalePagamenti) {
		this.importoTotalePagamenti = importoTotalePagamenti;
	}
	public BigDecimal getImportoPagato() {
		return importoPagato;
	}
	public void setImportoPagato(BigDecimal importoPagato) {
		this.importoPagato = importoPagato;
	}
	public Date getData() {
		return data;
	}
	public void setData(Date data) {
		this.data = data;
	}
	public String getCodSingoloVersamentoEnte() {
		return codSingoloVersamentoEnte;
	}
	public void setCodSingoloVersamentoEnte(String codSingoloVersamentoEnte) {
		this.codSingoloVersamentoEnte = codSingoloVersamentoEnte;
	}
	public Integer getIndiceDati() {
		return indiceDati;
	}
	public void setIndiceDati(Integer indiceDati) {
		this.indiceDati = indiceDati;
	}
	public String getCodVersamentoEnte() {
		return codVersamentoEnte;
	}
	public void setCodVersamentoEnte(String codVersamentoEnte) {
		this.codVersamentoEnte = codVersamentoEnte;
	}
	public String getCodApplicazione() {
		return codApplicazione;
	}
	public void setCodApplicazione(String codApplicazione) {
		this.codApplicazione = codApplicazione;
	}
	
	public class IUVComparator implements Comparator<EntrataPrevista> {
		@Override
		public int compare(EntrataPrevista o1, EntrataPrevista o2) {
			int result;
			result = o1.getIuv().compareTo(o2.getIuv());
			return result;
		}
	}

	@Override
	public int compareTo(EntrataPrevista o) {
		
		int result;
		
		result = this.getCodDominio().compareTo(o.getCodDominio());
		
		if(result == 0) {
			if(this.getCodFlusso() == null && o.getCodFlusso() != null)
				return 1;
			
			if(this.getCodFlusso() != null && o.getCodFlusso() == null)
				return -1;
				
			if(this.getCodFlusso() == null && o.getCodFlusso() == null)
				return this.getIuv().compareTo(o.getIuv());
			
			result = this.getCodFlusso().compareTo(o.getCodFlusso());
			
			if(result == 0) {
				result = this.getIuv().compareTo(o.getIuv());
			}
		}
		
		return result;
	}
	
}
