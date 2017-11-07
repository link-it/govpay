package it.govpay.core.utils.tracciati.operazioni;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import org.openspcoop2.generic_project.exception.ValidationException;
import org.openspcoop2.utils.UtilsException;
import org.openspcoop2.utils.csv.Record;

import it.govpay.core.utils.CurrencyUtils;
import it.govpay.core.utils.Utils;

public class SingoloIncassoResponse {
	
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

	public SingoloIncassoResponse() {
	}
	
	public SingoloIncassoResponse(Record record) throws ValidationException{
		this.trn = Utils.validaESettaRecord(record, "trn", null, null, false);
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
	}
	
	private String iuv;
	private String iur;
	private Double importo;
	private Date dataPagamento;
	private String codVersamentoEnte;
	private String codSingoloVersamentoEnte;
	private String trn;
	private String dominio;

	protected byte[] createDati(String delim) {
		StringBuilder sb = new StringBuilder();

		for(String dato: this.listDati()) {
			if(sb.length() > 0) {
				sb.append(delim);
			}
			sb.append(dato);
		}
		
		return sb.toString().getBytes();
	}

	protected List<String> listDati() {
		ArrayList<String> lst = new ArrayList<String>();
		lst.add(this.trn); 
		lst.add(this.dominio); 
		lst.add(this.iuv); 
		lst.add(this.iur); 
		try {
			lst.add(CurrencyUtils.getInstance().getCurrencyAsStringWithStringFormatSenzaVirgole(this.importo, Locale.getDefault()));
		} catch (Exception e) {
			lst.add(CurrencyUtils.getInstance().getCurrencyAsString(this.importo));
		} 
		lst.add(Utils.newSimpleDateFormat().format(this.dataPagamento));
		lst.add(this.codVersamentoEnte);
		lst.add(this.codSingoloVersamentoEnte);
		
		return lst;
	}

	


}
