package it.govpay.core.utils.tracciati.operazioni;

import it.govpay.bd.model.Versamento;
import it.govpay.core.utils.Utils;
import it.govpay.model.Operazione.TipoOperazioneType;

import java.util.Date;

import org.openspcoop2.generic_project.exception.ValidationException;
import org.openspcoop2.utils.UtilsException;
import org.openspcoop2.utils.csv.Record;

public class CaricamentoRequest extends AbstractOperazioneRequest {

	public CaricamentoRequest(Record record) throws ValidationException {
		super(TipoOperazioneType.ADD);
		this.setCodDominio(Utils.validaESettaRecord(record, "codDominio", 35, null, false));
		this.setCfDebitore(Utils.validaESettaRecord(record, "cfDebitore", 35, null, false));
		this.setAnagraficaDebitore(Utils.validaESettaRecord(record, "anagraficaDebitore", 70, null, false));
		this.setDebitoreIndirizzo(Utils.validaESettaRecord(record, "debitoreIndirizzo", 70, null, false));
		this.setDebitoreCivico(Utils.validaESettaRecord(record, "debitoreCivico", 16, null, false));
		this.setDebitoreCap(Utils.validaESettaRecord(record, "debitoreCap", 16, null, false));
		this.setDebitoreLocalita(Utils.validaESettaRecord(record, "debitoreLocalita", 35, null, false));
		this.setDebitoreProvincia(Utils.validaESettaRecord(record, "debitoreProvincia", 35, null, false));		
		this.setCodTributo(Utils.validaESettaRecord(record, "codTributo", 35, null, false));
		try {
			this.setImporto(Utils.validaESettaDouble("importo", record.getMap().get("importo"), null, null, false));
			this.setScadenza(Utils.validaESettaDate("dataScadenza", record.getMap().get("dataScadenza"), true));
		} catch(UtilsException e) {
			throw new ValidationException(e);
		}
		this.setCausale(Utils.validaESettaRecord(record, "causale", 256, null, true));
		this.setBundleKey(Utils.validaESettaRecord(record, "bundleKey", 256, null, true));
		this.setIdDebito(Utils.validaESettaRecord(record, "idDebito", 35, null, true));
		this.setNote(Utils.validaESettaRecord(record, "note", 512, null, true));
		this.setCodApplicazione(Utils.validaESettaRecord(record, "codApplicazione", 35, null, false));
		this.setCodVersamentoEnte(Utils.validaESettaRecord(record, "codVersamentoEnte", 35, null, false));
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
	public String getCodApplicazione() {
		return codApplicazione;
	}

	public void setCodApplicazione(String codApplicazione) {
		this.codApplicazione = codApplicazione;
	}
	public String getCodVersamentoEnte() {
		return codVersamentoEnte;
	}

	public void setCodVersamentoEnte(String codVersamentoEnte) {
		this.codVersamentoEnte = codVersamentoEnte;
	}
	
	public String getDebitoreLocalita() {
		return debitoreLocalita;
	}

	public void setDebitoreLocalita(String debitoreLocalita) {
		this.debitoreLocalita = debitoreLocalita;
	}

	public String getDebitoreCap() {
		return debitoreCap;
	}

	public void setDebitoreCap(String debitoreCap) {
		this.debitoreCap = debitoreCap;
	}

	public String getDebitoreIndirizzo() {
		return debitoreIndirizzo;
	}

	public void setDebitoreIndirizzo(String debitoreIndirizzo) {
		this.debitoreIndirizzo = debitoreIndirizzo;
	}

	public String getDebitoreCivico() {
		return debitoreCivico;
	}

	public void setDebitoreCivico(String debitoreCivico) {
		this.debitoreCivico = debitoreCivico;
	}

	public String getDebitoreProvincia() {
		return debitoreProvincia;
	}

	public void setDebitoreProvincia(String debitoreProvincia) {
		this.debitoreProvincia = debitoreProvincia;
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
	private String codApplicazione;
	private String codVersamentoEnte;
	private String debitoreLocalita;
	private String debitoreCap;
	private String debitoreIndirizzo;
	private String debitoreCivico;
	private String debitoreProvincia;

	private Versamento versamento;
	
}
