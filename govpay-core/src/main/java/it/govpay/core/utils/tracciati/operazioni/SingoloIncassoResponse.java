package it.govpay.core.utils.tracciati.operazioni;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.openspcoop2.generic_project.exception.ValidationException;
import org.openspcoop2.utils.UtilsException;
import org.openspcoop2.utils.csv.Record;

import it.govpay.core.utils.Utils;

public class SingoloIncassoResponse extends AbstractOperazioneResponse {
	
	public String getTrn() {
		return trn;
	}

	public void setTrn(String trn) {
		this.trn = trn;
	}

	public String getDominio() {
		return dominio;
	}

	public void setDominio(String dominio) {
		this.dominio = dominio;
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

	public Double getImporto() {
		return importo;
	}

	public void setImporto(Double importo) {
		this.importo = importo;
	}

	public Date getDataPagamento() {
		return dataPagamento;
	}

	public void setDataPagamento(Date dataPagamento) {
		this.dataPagamento = dataPagamento;
	}

	public String getCodVersamentoEnte() {
		return codVersamentoEnte;
	}

	public void setCodVersamentoEnte(String codVersamentoEnte) {
		this.codVersamentoEnte = codVersamentoEnte;
	}

	public String getCodSingoloVersamentoEnte() {
		return codSingoloVersamentoEnte;
	}

	public void setCodSingoloVersamentoEnte(String codSingoloVersamentoEnte) {
		this.codSingoloVersamentoEnte = codSingoloVersamentoEnte;
	}

	public String getFaultCode() {
		return faultCode;
	}

	public void setFaultCode(String faultCode) {
		this.faultCode = faultCode;
	}

	public String getFaultString() {
		return faultString;
	}

	public void setFaultString(String faultString) {
		this.faultString = faultString;
	}

	public String getFaultDescription() {
		return faultDescription;
	}

	public void setFaultDescription(String faultDescription) {
		this.faultDescription = faultDescription;
	}

	public static final String ESITO_INC_OK = "INC_OK";
	public static final String ESITO_INC_KO = "INC_KO";

	public SingoloIncassoResponse() {
	}
	
	public SingoloIncassoResponse(Record record) throws ValidationException{
		this.setEsito(Utils.validaESettaRecord(record,"esito",null, null, false));
		this.trn = Utils.validaESettaRecord(record, "esito", null, null, false);

		if(this.getEsito().equals(ESITO_INC_OK)) {
			this.dominio = Utils.validaESettaRecord(record, "dominio", null, null, false);
			this.iuv = Utils.validaESettaRecord(record, "iuv", null, null, false);
			this.iur = Utils.validaESettaRecord(record, "iur", null, null, false);
			try {
				this.importo = Utils.validaESettaDouble("importo", record.getMap().get("importo"), null, null, false);
				this.dataPagamento = Utils.validaESettaDate("dataPagamento", record.getMap().get("dataPagamento"), false);
			} catch(UtilsException e) {
				throw new ValidationException(e);
			}
			this.codVersamentoEnte = Utils.validaESettaRecord(record, "codVersamentoEnte", null, null, false);
			this.codSingoloVersamentoEnte = Utils.validaESettaRecord(record, "codSingoloVersamentoEnte", null, null, false);
		} else {
			this.faultCode = Utils.validaESettaRecord(record, "faultCode", null, null, false);
			this.faultString = Utils.validaESettaRecord(record, "faultString", null, null, false);
			this.faultDescription = Utils.validaESettaRecord(record, "faultDescription", null, null, false);
		}
		
	}
	
	private String trn;
	private String dominio;
	private String iuv;
	private String iur;
	private Double importo;
	private Date dataPagamento;
	private String codVersamentoEnte;
	private String codSingoloVersamentoEnte;
	private String faultCode;
	private String faultString;
	private String faultDescription;
	
	protected List<String> listDati() {
		ArrayList<String> lst = new ArrayList<String>();
		switch(this.getStato()) {
		case ESEGUITO_KO: 	lst.add(ESITO_INC_KO);
							lst.add(this.trn); 
							lst.add(this.dominio); 
							lst.add(this.iuv); 
							lst.add(this.iur); 
							lst.add(this.importo.toString()); 
							lst.add(Utils.newSimpleDateFormat().format(this.dataPagamento));
							lst.add(this.codVersamentoEnte);
							lst.add(this.codSingoloVersamentoEnte);
							break;
							
		case ESEGUITO_OK: 	lst.add(ESITO_INC_OK); 
							lst.add(this.trn); 
							lst.add(this.faultCode); 
							lst.add(this.faultString); 
							lst.add(this.faultDescription);
							break;
		case NON_VALIDO: break;
		}
		
		return lst;
	}

	


}
