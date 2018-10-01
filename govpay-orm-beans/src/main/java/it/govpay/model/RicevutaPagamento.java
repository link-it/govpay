package it.govpay.model;

import java.math.BigDecimal;
import java.util.Date;

public class RicevutaPagamento {

	private Dominio dominioCreditore;
	private Anagrafica anagraficaCreditore;
	private Anagrafica anagraficaVersante;
	private Anagrafica anagraficaAttestante;
	private Date dataScadenza;
	private Date dataPagamento;
	private String iuv;
	private BigDecimal importoDovuto;
	private BigDecimal importoPagato;
	private BigDecimal commissioni;
	private Anagrafica anagraficaDebitore;
	private String causale;
	private String descrizioneCausale;
	private String codVersamento;
	private String codAvviso;
	private String ccp;
	private String psp;
	private String canale;
	private String idRiscossione;
	private String codDominio;
	private byte[] logoDominioCreditore=null;
	
	public Dominio getDominioCreditore() {
		return this.dominioCreditore;
	}
	public void setDominioCreditore(Dominio dominioCreditore) {
		this.dominioCreditore = dominioCreditore;
	}
	public Anagrafica getAnagraficaCreditore() {
		return this.anagraficaCreditore;
	}
	public void setAnagraficaCreditore(Anagrafica anagraficaCreditore) {
		this.anagraficaCreditore = anagraficaCreditore;
	}
	public Date getDataScadenza() {
		return this.dataScadenza;
	}
	public void setDataScadenza(Date dataScadenza) {
		this.dataScadenza = dataScadenza;
	}
	public Date getDataPagamento() {
		return this.dataPagamento;
	}
	public void setDataPagamento(Date dataPagamento) {
		this.dataPagamento = dataPagamento;
	}
	public String getIuv() {
		return this.iuv;
	}
	public void setIuv(String iuv) {
		this.iuv = iuv;
	}
	public BigDecimal getImportoDovuto() {
		return this.importoDovuto;
	}
	public void setImportoDovuto(BigDecimal importoDovuto) {
		this.importoDovuto = importoDovuto;
	}
	public BigDecimal getImportoPagato() {
		return this.importoPagato;
	}
	public void setImportoPagato(BigDecimal importoPagato) {
		this.importoPagato = importoPagato;
	}
	public Anagrafica getAnagraficaDebitore() {
		return this.anagraficaDebitore;
	}
	public void setAnagraficaDebitore(Anagrafica anagraficaDebitore) {
		this.anagraficaDebitore = anagraficaDebitore;
	}
	public String getCausale() {
		return this.causale;
	}
	public void setCausale(String causale) {
		this.causale = causale;
	}
	public String getCodVersamento() {
		return this.codVersamento;
	}
	public void setCodVersamento(String codVersamento) {
		this.codVersamento = codVersamento;
	}
	public String getCodAvviso() {
		return this.codAvviso;
	}
	public void setCodAvviso(String codAvviso) {
		this.codAvviso = codAvviso;
	}
	public String getCcp() {
		return this.ccp;
	}
	public void setCcp(String ccp) {
		this.ccp = ccp;
	}
	public String getPsp() {
		return this.psp;
	}
	public void setPsp(String psp) {
		this.psp = psp;
	}
	public String getCanale() {
		return this.canale;
	}
	public void setCanale(String canale) {
		this.canale = canale;
	}
	public String getIdRiscossione() {
		return this.idRiscossione;
	}
	public void setIdRiscossione(String idRiscossione) {
		this.idRiscossione = idRiscossione;
	}
	public String getCodDominio() {
		return this.codDominio;
	}
	public void setCodDominio(String codDominio) {
		this.codDominio = codDominio;
	}
	public Anagrafica getAnagraficaVersante() {
		return this.anagraficaVersante;
	}
	public void setAnagraficaVersante(Anagrafica anagraficaVersante) {
		this.anagraficaVersante = anagraficaVersante;
	}
	public Anagrafica getAnagraficaAttestante() {
		return this.anagraficaAttestante;
	}
	public void setAnagraficaAttestante(Anagrafica anagraficaAttestante) {
		this.anagraficaAttestante = anagraficaAttestante;
	}
	public BigDecimal getCommissioni() {
		return this.commissioni;
	}
	public void setCommissioni(BigDecimal commissioni) {
		this.commissioni = commissioni;
	}
	public String getDescrizioneCausale() {
		return this.descrizioneCausale;
	}
	public void setDescrizioneCausale(String descrizioneCausale) {
		this.descrizioneCausale = descrizioneCausale;
	}
	public byte[] getLogoDominioCreditore() {
		return this.logoDominioCreditore;
	}
	public void setLogoDominioCreditore(byte[] logoDominioCreditore) {
		this.logoDominioCreditore = logoDominioCreditore;
	}
	
}
