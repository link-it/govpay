package it.govpay.backoffice.utils.validazione.semantica;

import java.util.regex.Pattern;

import org.apache.commons.lang.StringUtils;
import org.openspcoop2.utils.json.ValidationException;

import it.govpay.bd.model.Applicazione;
import it.govpay.core.utils.validator.IValidable;

public class ApplicazioneValidator implements IValidable {
	
	private Applicazione applicazione;
	
	public ApplicazioneValidator(Applicazione applicazione ) {
		this.applicazione = applicazione;
	}

	@Override
	public void validate() throws ValidationException {
		
		if(StringUtils.isNotEmpty(this.applicazione.getRegExp())) {
			try {
				Pattern.compile(this.applicazione.getRegExp());
			}catch(java.util.regex.PatternSyntaxException e) {
				throw new ValidationException("Il campo regExpIuv non contiene un pattern valido.");
			}
		}
	}
}
