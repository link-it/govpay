package it.govpay.bd.model;

import java.util.Date;

import org.openspcoop2.generic_project.exception.NotFoundException;
import org.openspcoop2.generic_project.exception.ServiceException;

import it.govpay.bd.BasicBD;
import it.govpay.bd.anagrafica.AnagraficaManager;

public class OperazioneCaricamento extends Operazione{
	
	public OperazioneCaricamento() {
	}
	
	public OperazioneCaricamento(Operazione operazione){
		this.setCodVersamentoEnte(operazione.getCodVersamentoEnte());
		this.setDatiRichiesta(operazione.getDatiRichiesta());
		this.setDatiRisposta(operazione.getDatiRisposta());
		this.setDettaglioEsito(operazione.getDettaglioEsito());
		this.setId(operazione.getId());
		this.setIdApplicazione(operazione.getIdApplicazione());
		this.setIdOperazione(operazione.getIdOperazione());
		this.setIdTracciato(operazione.getIdTracciato());
		this.setLineaElaborazione(operazione.getLineaElaborazione());
		this.setStato(operazione.getStato());
		this.setTipoOperazione(operazione.getTipoOperazione());
	}

	// richiesta
	private String codDominio;
	private String cfDebitore;
	private String anagraficaDebitore;
	private String codTributo;
	private Double importo;
	private String causale;
	private Date scadenza;
	private String bundleKey;
	private String idDebito;
	private String note;
	private String numeroAvviso;
	
	// risposta
	private String iuv;
	private byte[] qrCode;
	private byte[] barCode;
	
	public String getCodDominio() {
		return codDominio;
	}
	public void setCodDominio(String codDominio) {
		this.codDominio = codDominio;
	}
	public String getCfDebitore() {
		return cfDebitore;
	}
	public void setCfDebitore(String cfDebitore) {
		this.cfDebitore = cfDebitore;
	}
	public String getAnagraficaDebitore() {
		return anagraficaDebitore;
	}
	public void setAnagraficaDebitore(String anagraficaDebitore) {
		this.anagraficaDebitore = anagraficaDebitore;
	}
	public String getCodTributo() {
		return codTributo;
	}
	public void setCodTributo(String codTributo) {
		this.codTributo = codTributo;
	}
	public Double getImporto() {
		return importo;
	}
	public void setImporto(Double importo) {
		this.importo = importo;
	}
	public String getCausale() {
		return causale;
	}
	public void setCausale(String causale) {
		this.causale = causale;
	}
	public Date getScadenza() {
		return scadenza;
	}
	public void setScadenza(Date scadenza) {
		this.scadenza = scadenza;
	}
	public String getBundleKey() {
		return bundleKey;
	}
	public void setBundleKey(String bundleKey) {
		this.bundleKey = bundleKey;
	}
	public String getIdDebito() {
		return idDebito;
	}
	public void setIdDebito(String idDebito) {
		this.idDebito = idDebito;
	}
	public String getNote() {
		return note;
	}
	public void setNote(String note) {
		this.note = note;
	}
	
	public String getIuv() {
		return iuv;
	}
	public void setIuv(String iuv) {
		this.iuv = iuv;
	}
	public byte[] getQrCode() {
		return qrCode;
	}
	public void setQrCode(byte[] qrCode) {
		this.qrCode = qrCode;
	}
	public byte[] getBarCode() {
		return barCode;
	}
	public void setBarCode(byte[] barCode) {
		this.barCode = barCode;
	}
	
	private transient Dominio dominio;
	private transient Tributo tributo;
	
	public Dominio getDominio(BasicBD bd) throws ServiceException, NotFoundException {
		if(dominio == null) {
			dominio = AnagraficaManager.getDominio(bd, this.getCodDominio());
		} 
		return dominio;
	}
	public Tributo getTributo(BasicBD bd,long idDominio) throws ServiceException, NotFoundException {
		if(tributo == null && this.getCodTributo() != null) {
			tributo = AnagraficaManager.getTributo(bd, idDominio, getCodTributo());
		}
		return tributo;
	}
	
	public void setTributo(Tributo tributo){
		this.tributo = tributo;
	}

	public String getNumeroAvviso() {
		return numeroAvviso;
	}

	public void setNumeroAvviso(String numeroAvviso) {
		this.numeroAvviso = numeroAvviso;
	}
	
}
