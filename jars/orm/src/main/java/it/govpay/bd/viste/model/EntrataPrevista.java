package it.govpay.bd.viste.model;

import java.io.UnsupportedEncodingException;
import java.math.BigDecimal;
import java.util.Comparator;
import java.util.Date;

import it.govpay.model.BasicModel;
import it.govpay.model.Versamento;
import it.govpay.model.Versamento.Causale;
import it.govpay.model.Versamento.TipoSogliaVersamento;

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
	private String codSingoloVersamentoEnte;
	private Integer indiceDati;
	private String codVersamentoEnte;
	private String codApplicazione;
	private Date dataPagamento;
	private String codTipoVersamento;
	private String codEntrata;
	private String identificativoDebitore;
	private String anno; 
	private String anagraficaDebitore;
	private String descrizioneTipoVersamento;
	private String codPsp;
	private String ragioneSocialePsp;
	private String iuvPagamento;
	private Integer numeroRata;
	private String codDocumento;
	private Long idDocumento;
	private TipoSogliaVersamento tipoSoglia;
	private Integer giorniSoglia;
	private Causale causaleVersamento;
	private Date dataScadenza;
	private BigDecimal importoVersamento;
	private String numeroAvviso;
	private String contabilita;
	
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
	public Date getDataPagamento() {
		return dataPagamento;
	}
	public void setDataPagamento(Date dataPagamento) {
		this.dataPagamento = dataPagamento;
	}
	public String getCodTipoVersamento() {
		return codTipoVersamento;
	}
	public void setCodTipoVersamento(String codTipoVersamento) {
		this.codTipoVersamento = codTipoVersamento;
	}
	public String getCodEntrata() {
		return codEntrata;
	}
	public void setCodEntrata(String codEntrata) {
		this.codEntrata = codEntrata;
	}
	public String getIdentificativoDebitore() {
		return identificativoDebitore;
	}
	public void setIdentificativoDebitore(String identificativoDebitore) {
		this.identificativoDebitore = identificativoDebitore;
	}
	public String getAnno() {
		return anno;
	}
	public void setAnno(String anno) {
		this.anno = anno;
	}

	public String getAnagraficaDebitore() {
		return anagraficaDebitore;
	}
	public void setAnagraficaDebitore(String anagraficaDebitore) {
		this.anagraficaDebitore = anagraficaDebitore;
	}
	public String getDescrizioneTipoVersamento() {
		return descrizioneTipoVersamento;
	}
	public void setDescrizioneTipoVersamento(String descrizioneTipoVersamento) {
		this.descrizioneTipoVersamento = descrizioneTipoVersamento;
	}
	public String getCodPsp() {
		return codPsp;
	}
	public void setCodPsp(String codPsp) {
		this.codPsp = codPsp;
	}
	public String getRagioneSocialePsp() {
		return ragioneSocialePsp;
	}
	public void setRagioneSocialePsp(String ragioneSocialePsp) {
		this.ragioneSocialePsp = ragioneSocialePsp;
	}
	public String getIuvPagamento() {
		return iuvPagamento;
	}
	public void setIuvPagamento(String iuvPagamento) {
		this.iuvPagamento = iuvPagamento;
	}
	public Integer getNumeroRata() {
		return numeroRata;
	}
	public void setNumeroRata(Integer numeroRata) {
		this.numeroRata = numeroRata;
	}
	public String getCodDocumento() {
		return codDocumento;
	}
	public void setCodDocumento(String codDocumento) {
		this.codDocumento = codDocumento;
	}
	public Long getIdDocumento() {
		return idDocumento;
	}
	public void setIdDocumento(Long idDocumento) {
		this.idDocumento = idDocumento;
	}
	public TipoSogliaVersamento getTipoSoglia() {
		return tipoSoglia;
	}
	public void setTipoSoglia(TipoSogliaVersamento tipoSoglia) {
		this.tipoSoglia = tipoSoglia;
	}
	public Integer getGiorniSoglia() {
		return giorniSoglia;
	}
	public void setGiorniSoglia(Integer giorniSoglia) {
		this.giorniSoglia = giorniSoglia;
	}
	public Causale getCausaleVersamento() {
		return causaleVersamento;
	}
	public void setCausaleVersamento(String causaleVersamentoEncoded) throws UnsupportedEncodingException {
		if(causaleVersamentoEncoded == null) 
			this.causaleVersamento = null;
		else
			this.causaleVersamento = Versamento.decode(causaleVersamentoEncoded);
	}
	public Date getDataScadenza() {
		return dataScadenza;
	}
	public void setDataScadenza(Date dataScadenza) {
		this.dataScadenza = dataScadenza;
	}
	public BigDecimal getImportoVersamento() {
		return importoVersamento;
	}
	public void setImportoVersamento(BigDecimal importoVersamento) {
		this.importoVersamento = importoVersamento;
	}
	public String getNumeroAvviso() {
		return numeroAvviso;
	}
	public void setNumeroAvviso(String numeroAvviso) {
		this.numeroAvviso = numeroAvviso;
	}
	public String getContabilita() {
		return contabilita;
	}
	public void setContabilita(String contabilita) {
		this.contabilita = contabilita;
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
