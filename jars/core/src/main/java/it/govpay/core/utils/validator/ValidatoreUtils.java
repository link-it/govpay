package it.govpay.core.utils.validator;

import org.openspcoop2.utils.json.ValidationException;

public class ValidatoreUtils {
	
	public static void checkIsNotNull(ValidatorFactory vf, String nomeCampo, String valoreCampo) throws ValidationException {
		vf.getValidator(nomeCampo, valoreCampo).notNull();
	}
	
	public static void validaCodiceContabilita(ValidatorFactory vf, String nomeCampo, String codiceContabilita) throws ValidationException {
		validaCodiceContabilita(vf, nomeCampo, codiceContabilita, true);
	}

	public static void validaCodiceContabilita(ValidatorFactory vf, String nomeCampo, String codiceContabilita, boolean notNull) throws ValidationException {
		validaField(vf, nomeCampo, codiceContabilita, CostantiValidazione.PATTERN_COD_CONTABILITA, null, 255, notNull);
	}
	
	
	public static StringValidator validaField(ValidatorFactory vf, String fieldName, String fieldValue, String pattern, Integer minLength, Integer maxLength, boolean notNull) throws ValidationException {
		StringValidator validator = vf.getValidator(fieldName, fieldValue);
		
		if(notNull)
			validator.notNull();
		
		if(minLength != null)
			validator.minLength(minLength);
		
		if(maxLength != null)
			validator.maxLength(maxLength);
		
		if(pattern != null)
			validator.pattern(pattern);
		
		return validator;
	}
}
