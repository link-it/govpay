package it.govpay.model;

import java.math.BigDecimal;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.TreeMap;

public class AvvisoPagamentoInput {

	byte[] qrCode= null; 
	byte[] barCode= null;
	private Dominio dominioCreditore;
	private Anagrafica anagraficaCreditore;
	private String codiceAvviso;
	private Date dataScadenza;
	private String iuv;
	private BigDecimal importo;
	private Anagrafica anagraficaDebitore;
	private String causale;
	private String codVersamento;
	
	private Map<String, TreeMap<String, String>> contenutoStatico;
	
	public byte[] getQrCode() {
		return this.qrCode;
	}
	public void setQrCode(byte[] qrCode) {
		this.qrCode = qrCode;
	}
	public byte[] getBarCode() {
		return this.barCode;
	}
	public void setBarCode(byte[] barCode) {
		this.barCode = barCode;
	}
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
	public String getCodiceAvviso() {
		return this.codiceAvviso;
	}
	public void setCodiceAvviso(String codiceAvviso) {
		this.codiceAvviso = codiceAvviso;
	}
	public Date getDataScadenza() {
		return this.dataScadenza;
	}
	public void setDataScadenza(Date dataScadenza) {
		this.dataScadenza = dataScadenza;
	}
	public String getIuv() {
		return this.iuv;
	}
	public void setIuv(String iuv) {
		this.iuv = iuv;
	}
	public BigDecimal getImporto() {
		return this.importo;
	}
	public void setImporto(BigDecimal importo) {
		this.importo = importo;
	}
	public Map<String, TreeMap<String, String>> getContenutoStatico() {
		if(this.contenutoStatico == null)
			this.contenutoStatico = new HashMap<>();
		
		return this.contenutoStatico;
	}
	public void setContenutoStatico(Map<String, TreeMap<String, String>> contenutoStatico) {
		this.contenutoStatico = contenutoStatico;
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
	
}
