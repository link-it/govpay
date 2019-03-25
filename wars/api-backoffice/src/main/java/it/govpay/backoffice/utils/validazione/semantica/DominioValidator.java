package it.govpay.backoffice.utils.validazione.semantica;

import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.openspcoop2.utils.json.ValidationException;

import it.govpay.bd.GovpayConfig;
import it.govpay.bd.model.Dominio;
import it.govpay.core.utils.PagamentoContext;
import it.govpay.core.utils.validator.IValidable;
import it.govpay.core.utils.validator.ValidatorFactory;

public class DominioValidator implements IValidable {

	private Dominio dominio;
	
	public DominioValidator(Dominio dominio) {
		this.dominio = dominio;
	}
	
	@Override
	public void validate() throws ValidationException {
		
		ValidatorFactory vf = ValidatorFactory.newInstance();

		// Dominio pluri-intermediato deve avere codice segregazione
		if(this.dominio.getAuxDigit() == 3 && this.dominio.getSegregationCode() == null) {
			throw new ValidationException("Il campo segregationCode non deve essere vuoto.");
		}
		
		// validazione semantica della string iuv prefix
		if(StringUtils.isNotEmpty(this.dominio.getIuvPrefix())) {
			Map<String,String> props = new HashMap<>();
			
			// Applicazione inserisco la dimensione massima 4 caratteri
			props.put(PagamentoContext.codApplicazioneIuvKey, "1234");
			
			// Tributo inserisco la dimensione massima 4 caratter
			props.put(PagamentoContext.codificaIuvKey, "1234");
							
			Calendar now = Calendar.getInstance(); 
			int year = now.get(Calendar.YEAR);  
			
			props.put(PagamentoContext.anno4, year + "");
			props.put(PagamentoContext.anno2, year%100 + "");
			
			String prefix = GovpayConfig.getInstance().getDefaultCustomIuvGenerator().buildPrefix(null, this.dominio.getIuvPrefix(), props);
			
			// check del risultato:
			// deve essere solo numerico e non lungo piu' di 18 caratteri
			vf.getValidator("iuvPrefix", prefix).maxLength(18).pattern("(^([0-9]){1,18}$)");
		}
	}
}
