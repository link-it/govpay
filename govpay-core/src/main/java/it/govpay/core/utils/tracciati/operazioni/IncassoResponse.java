package it.govpay.core.utils.tracciati.operazioni;

import java.util.ArrayList;
import java.util.List;

import org.openspcoop2.utils.csv.Record;

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
	public IncassoResponse(Record record) {}

	@Override
	protected byte[] createDati() {
		StringBuffer sb = new StringBuffer();
		if(this.getEsito().equals(ESITO_INC_OK)) {

			for(SingoloIncassoResponse r: this.lstSingoloIncasso) {
				if(sb.length() > 0)
					sb.append("\n");
				sb.append(new String(r.createDati(this.getDelim())));
			}
		} else {
			
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
