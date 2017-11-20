package it.govpay.core.utils.tracciati.operazioni;

import java.util.Date;

import org.openspcoop2.generic_project.exception.ValidationException;
import org.openspcoop2.utils.UtilsException;
import org.openspcoop2.utils.csv.Record;

import it.govpay.core.utils.Utils;
import it.govpay.model.Operazione.TipoOperazioneType;

public class IncassoRequest extends AbstractOperazioneRequest {

	public IncassoRequest(Record record) throws ValidationException {
		super(TipoOperazioneType.INC);
		this.trn = Utils.validaESettaRecord(record, "trn", 35, null, false);
		this.dispositivo= Utils.validaESettaRecord(record, "dispositivo", 35, null, true);
		this.dominio= Utils.validaESettaRecord(record, "dominio", 35, null, false);
		this.causale = Utils.validaESettaRecord(record, "causale", 70, null, false);
		try {
			this.importo = Utils.validaESettaDouble("importo", record.getMap().get("importo"), null, null, false);
			this.dataContabile = Utils.validaESettaDate("dataContabile", record.getMap().get("dataContabile"), true);
			this.dataValuta = Utils.validaESettaDate("dataValuta", record.getMap().get("dataValuta"), true);
		} catch(UtilsException e) {
			throw new ValidationException(e);
		}
	}

	public String getTrn() {
		return trn;
	}
	public void setTrn(String trn) {
		this.trn = trn;
	}
	public String getCausale() {
		return causale;
	}
	public void setCausale(String causale) {
		this.causale = causale;
	}
	public Double getImporto() {
		return importo;
	}
	public void setImporto(Double importo) {
		this.importo = importo;
	}
	public Date getDataContabile() {
		return dataContabile;
	}
	public void setDataContabile(Date dataContabile) {
		this.dataContabile = dataContabile;
	}

	public Date getDataValuta() {
		return dataValuta;
	}

	public void setDataValuta(Date dataValuta) {
		this.dataValuta = dataValuta;
	}

	public String getDominio() {
		return dominio;
	}

	public void setDominio(String dominio) {
		this.dominio = dominio;
	}

	public String getDispositivo() {
		return dispositivo;
	}

	public void setDispositivo(String dispositivo) {
		this.dispositivo = dispositivo;
	}

	private String trn;
	private String dominio;
	private String dispositivo;
	private String causale;
	private Double importo;
	private Date dataContabile;
	private Date dataValuta;

}
