package it.govpay.core.loader.timers.model;

import it.govpay.bd.model.Versamento;
import it.govpay.model.loader.Operazione.TipoOperazioneType;

import java.util.Date;

import org.openspcoop2.generic_project.exception.ValidationException;
import org.openspcoop2.utils.UtilsException;
import org.openspcoop2.utils.csv.Parser;
import org.openspcoop2.utils.csv.Record;

public class CaricamentoRequest extends AbstractOperazioneRequest {

	public CaricamentoRequest(Parser parser, Record record) {
		super(TipoOperazioneType.ADD, parser, record);
		try {
			this.setCodDominio(validaESettaRecord(record, "codDominio", 35, null, false));
			this.setCfDebitore(validaESettaRecord(record, "cfDebitore", 35, null, false));
			this.setAnagraficaDebitore(validaESettaRecord(record, "anagraficaDebitore", 70, null, false));
			this.setCodTributo(validaESettaRecord(record, "codTributo", 35, null, false));
			try {
				this.setImporto(validaESettaDouble("importo", record.getMap().get("importo"), null, null, false));
				this.setScadenza(validaESettaDate("dataScadenza", record.getMap().get("dataScadenza"), true));
			} catch(UtilsException e) {
				throw new ValidationException(e);
			}
			this.setCausale(validaESettaRecord(record, "causale", 256, null, true));
			this.setBundleKey(validaESettaRecord(record, "bundleKey", 256, null, true));
			this.setIdDebito(validaESettaRecord(record, "idDebito", 35, null, true));
			this.setNote(validaESettaRecord(record, "note", 512, null, true));
		} catch(ValidationException e) {
			this.setValid(false);
		}
		
	}
	
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
	public Versamento getVersamento() {
		return versamento;
	}
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
	
	private Versamento versamento;
	
}
