package it.govpay.core.utils.tracciati.operazioni;

import java.util.ArrayList;
import java.util.List;

import org.openspcoop2.generic_project.exception.ValidationException;
import org.openspcoop2.utils.csv.Record;

import it.govpay.core.utils.Utils;
import it.govpay.model.Operazione.StatoOperazioneType;

public class IncassoResponse extends AbstractOperazioneResponse {

	

	public static final String ESITO_INC_OK = "INC_OK";
	public static final String ESITO_INC_KO = "INC_KO";

	private List<SingoloIncassoResponse> lstSingoloIncasso;

	private String faultCode;
	private String faultString;
	private String faultDescription;
	private String trn;
	private String dominio;

	public IncassoResponse() {}
	public IncassoResponse(List<Record> recordList) throws ValidationException{
		for(Record record:recordList) {
			this.setEsito(Utils.validaESettaRecord(record,"esito",null, null, false));
			if(this.getEsito().equals(ESITO_INC_OK)) {
				this.setStato(StatoOperazioneType.ESEGUITO_OK);
				this.add(new SingoloIncassoResponse(record));
			} else {
				this.setStato(StatoOperazioneType.ESEGUITO_KO);
				this.trn = Utils.validaESettaRecord(record, "trn", 35, null, false);
				this.faultCode = Utils.validaESettaRecord(record, "faultCode", 70, null, false);
				this.faultString = Utils.validaESettaRecord(record, "faultString", 70, null, false);
				this.faultDescription = Utils.validaESettaRecord(record, "faultDescription", 255, null, false);
			}
		}
	}

	@Override
	protected byte[] createDati() {
		StringBuffer sb = new StringBuffer();
		if(this.getEsito().equals(ESITO_INC_OK)) {

			for(SingoloIncassoResponse r: this.lstSingoloIncasso) {
				if(sb.length() > 0)
					sb.append("\n");
				sb.append(ESITO_INC_OK).append(this.getDelim()).append(new String(r.createDati(this.getDelim())));
			}
		} else {
			sb.append(ESITO_INC_KO).append(this.getDelim());
			sb.append(this.trn).append(this.getDelim());
			sb.append(this.faultCode).append(this.getDelim());
			sb.append(this.faultString).append(this.getDelim());
			sb.append(this.faultDescription);
		}
		return sb.toString().getBytes();
	}
	
	@Override
	protected List<String> listDati() {
		return null;
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

	public void add(SingoloIncassoResponse singoloIncassoResponse) {
		if(this.lstSingoloIncasso==null) this.lstSingoloIncasso = new ArrayList<SingoloIncassoResponse>();
		this.lstSingoloIncasso.add(singoloIncassoResponse);
	}
	public List<SingoloIncassoResponse> getLstSingoloIncasso() {
		return lstSingoloIncasso;
	}

}
