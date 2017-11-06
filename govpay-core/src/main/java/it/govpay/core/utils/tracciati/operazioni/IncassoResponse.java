package it.govpay.core.utils.tracciati.operazioni;

import java.util.ArrayList;
import java.util.List;

import org.openspcoop2.utils.csv.Record;

public class IncassoResponse extends AbstractOperazioneResponse {

	
	private List<SingoloIncassoResponse> lstSingoloIncasso;

	public IncassoResponse() {}
	public IncassoResponse(Record record) {}

	@Override
	protected byte[] createDati() {
		StringBuffer sb = new StringBuffer();
		for(SingoloIncassoResponse r: this.lstSingoloIncasso) {
			if(sb.length() > 0)
				sb.append("\n");
			sb.append(new String(r.createDati()));
		}
		
		return sb.toString().getBytes();
	}
	
	@Override
	protected List<String> listDati() {
		return null;
	}

	public void add(SingoloIncassoResponse singoloIncassoResponse) {
		if(this.lstSingoloIncasso==null) this.lstSingoloIncasso = new ArrayList<SingoloIncassoResponse>();
		this.lstSingoloIncasso.add(singoloIncassoResponse);
	}
	public List<SingoloIncassoResponse> getLstSingoloIncasso() {
		return lstSingoloIncasso;
	}

}
