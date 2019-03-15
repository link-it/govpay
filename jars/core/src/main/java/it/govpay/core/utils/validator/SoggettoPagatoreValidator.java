package it.govpay.core.utils.validator;

import it.govpay.ec.v1.beans.Soggetto;

public class SoggettoPagatoreValidator implements IValidable {

	private Soggetto soggettoPagatore = null;
	
	public SoggettoPagatoreValidator(Soggetto soggettoPagatore) {
		this.soggettoPagatore = soggettoPagatore;
	}
	
	 @Override
	 public void validate() throws org.openspcoop2.utils.json.ValidationException {
		 if(this.soggettoPagatore != null) {
			 ValidatorFactory vf = ValidatorFactory.newInstance();
			 vf.getValidator("tipo", this.soggettoPagatore.getTipo()).notNull();
			 vf.getValidator("identificativo", this.soggettoPagatore.getIdentificativo()).notNull().minLength(1).maxLength(35);
			 vf.getValidator("anagrafica", this.soggettoPagatore.getAnagrafica()).minLength(1).maxLength(70);
			 vf.getValidator("indirizzo", this.soggettoPagatore.getIndirizzo()).minLength(1).maxLength(70);
			 vf.getValidator("civico", this.soggettoPagatore.getCivico()).minLength(1).maxLength(16);
			 vf.getValidator("cap", this.soggettoPagatore.getCap()).minLength(1).maxLength(16);
			 vf.getValidator("localita", this.soggettoPagatore.getLocalita()).minLength(1).maxLength(35);
			 vf.getValidator("provincia", this.soggettoPagatore.getProvincia()).minLength(1).maxLength(70);
			 vf.getValidator("nazione", this.soggettoPagatore.getNazione()).pattern("[A-Z]{2,2}");
			 vf.getValidator("email", this.soggettoPagatore.getEmail()).pattern("(^[a-zA-Z0-9_.+-]+@[a-zA-Z0-9-]+\\.[a-zA-Z0-9-.]+$)");
			 vf.getValidator("cellulare", this.soggettoPagatore.getCellulare()).pattern("\\+[0-9]{2,2}\\s[0-9]{3,3}\\-[0-9]{7,7}");
		 }
	 }
}

