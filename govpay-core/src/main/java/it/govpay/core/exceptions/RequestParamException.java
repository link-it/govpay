package it.govpay.core.exceptions;

public class RequestParamException extends BaseException {

	private static final long serialVersionUID = 1L;
	
	public enum FaultType {
		PARAMETRO_ORDERBY_NON_VALIDO("100001","Uno dei parametri di ordinamento non e' valido.");
		
		private String faultSubCode;
		private String description;
		
		FaultType(String faultSubCode, String description) {
			this.faultSubCode = faultSubCode;
			this.description = description;
		}
		
		public String getFaultSubCode(){
			return faultSubCode;
		}
		
		public String getDescription(){
			return description;
		}
	}
	
	public RequestParamException(FaultType faultType, String details) {
		super("Errore nella valorizzazione dei parametri della richiesta", faultType.faultSubCode, details);
	}
	
	@Override
	public int getTransportErrorCode() {
		return 422;
	}

}
