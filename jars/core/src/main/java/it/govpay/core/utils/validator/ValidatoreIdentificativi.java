package it.govpay.core.utils.validator;

import org.openspcoop2.utils.json.ValidationException;

public class ValidatoreIdentificativi {
	
	private ValidatorFactory vf = null; 
	
	
	public ValidatoreIdentificativi() {
		this.vf = ValidatorFactory.newInstance();
	}
	
	public static ValidatoreIdentificativi newInstance() {
		return new ValidatoreIdentificativi();
	}

	public void validaIdIntermediario(String idIntermediario) throws ValidationException {
		this.validaIdIntermediario("idIntermediario", idIntermediario);
	}
	
	public void validaIdIntermediario(String fieldName, String idIntermediario) throws ValidationException {
		this.validaId(fieldName, idIntermediario, CostantiValidazione.PATTERN_ID_INTERMEDIARIO, 11, 11);
	}
	
	public void validaIdStazione(String idStazione) throws ValidationException {
		this.validaIdStazione("idStazione", idStazione);
	}
	
	public void validaIdStazione(String fieldName, String idStazione) throws ValidationException {
		this.validaId(fieldName, idStazione, CostantiValidazione.PATTERN_ID_STAZIONE, 1, 35);
	}
	
	public void validaIdDominio(String idDominio) throws ValidationException {
		this.validaIdDominio("idDominio", idDominio);
	}
	
	public void validaIdDominio(String fieldName, String idDominio) throws ValidationException {
		this.validaId(fieldName, idDominio, CostantiValidazione.PATTERN_ID_DOMINIO, 11, 11);
	}
	
	public void validaIdUO(String idUO) throws ValidationException {
		this.validaIdUO("idUO", idUO);
	}
	
	public void validaIdUO(String fieldName, String idUO) throws ValidationException {
		this.validaIdUO(fieldName, idUO, false);
	}
	
	public void validaIdUO(String fieldName, String idUO, boolean notNull) throws ValidationException {
		this.validaId(fieldName, idUO, CostantiValidazione.PATTERN_ID_UO, 1, 35, notNull);
	}
	
	public void validaIdApplicazione(String idA2A) throws ValidationException {
		this.validaIdApplicazione("idA2A", idA2A);
	}
	
	public void validaIdApplicazione(String fieldName, String idA2A) throws ValidationException {
		this.validaId(fieldName, idA2A, CostantiValidazione.PATTERN_ID_APPLICAZIONE, 1, 35);
	}
	
	public void validaIdEntrata(String idEntrata) throws ValidationException {
		this.validaIdEntrata("idEntrata", idEntrata);
	}
	
	public void validaIdEntrata(String fieldName, String idEntrata) throws ValidationException {
		this.validaId(fieldName, idEntrata, CostantiValidazione.PATTERN_ID_ENTRATA, 1, 35);
	}
	
	public void validaIdTipoVersamento(String idTipoVersamento) throws ValidationException {
		this.validaIdTipoVersamento("idTipoPendenza", idTipoVersamento);
	}
	
	public void validaIdTipoVersamento(String fieldName, String idTipoVersamento) throws ValidationException {
		this.validaIdTipoVersamento("idTipoPendenza", idTipoVersamento, false);
	}
	
	public void validaIdTipoVersamento(String fieldName, String idTipoVersamento, boolean notNull) throws ValidationException {
		this.validaId(fieldName, idTipoVersamento, CostantiValidazione.PATTERN_ID_TIPO_VERSAMENTO, 1, 35 ,notNull);
	}
	
	public void validaIdRuolo(String idRuolo) throws ValidationException {
		this.validaIdRuolo("idRuolo", idRuolo);
	}
	
	public void validaIdRuolo(String fieldName, String idRuolo) throws ValidationException {
		this.validaId(fieldName, idRuolo, CostantiValidazione.PATTERN_ID_RUOLO, 1, 255);
	}
	
	public void validaIdIbanAccredito(String ibanAccredito) throws ValidationException {
		this.validaIdIbanAccredito("ibanAccredito", ibanAccredito);
	}
	
	public void validaIdIbanAccredito(String fieldName, String ibanAccredito) throws ValidationException {
		this.validaIdIbanAccredito(fieldName, ibanAccredito, false);
	}
	
	public void validaIdIbanAccredito(String fieldName, String ibanAccredito, boolean notnull) throws ValidationException {
		this.validaId(fieldName, ibanAccredito, CostantiValidazione.PATTERN_IBAN_ACCREDITO, 1, 255, notnull);
	}
	
	public void validaBicAccredito(String bicAccredito) throws ValidationException {
		this.validaIdIbanAccredito("bicAccredito", bicAccredito);
	}
	
	public void validaBicAccredito(String fieldName, String bicAccredito) throws ValidationException {
		this.validaId(fieldName, bicAccredito, CostantiValidazione.PATTERN_BIC_1, 1, 255);
	}
	
	public void validaIdOperatore(String principal) throws ValidationException {
		this.validaIdOperatore("principal", principal);
	}
	
	public void validaIdOperatore(String fieldName, String principal) throws ValidationException {
		this.validaId(fieldName, principal, CostantiValidazione.PATTERN_PRINCIPAL, 1, 4000);
	}
	
	public void validaIdPendenza(String idPendenza) throws ValidationException {
		this.validaIdPendenza("idPendenza", idPendenza);
	}
	
	public void validaIdPendenza(String fieldName, String idPendenza) throws ValidationException {
		this.validaId(fieldName, idPendenza, CostantiValidazione.PATTERN_ID_PENDENZA, 1, 35);
	}
	
	public void validaIdVocePendenza(String idVocePendenza) throws ValidationException {
		this.validaIdVocePendenza("idVocePendenza", idVocePendenza);
	}
	
	public void validaIdVocePendenza(String fieldName, String idVocePendenza) throws ValidationException {
		this.validaId(fieldName, idVocePendenza, CostantiValidazione.PATTERN_ID_VOCE_PENDENZA, 1, 35);
	}
	
	public void validaIdDirezione(String idDirezione) throws ValidationException {
		this.validaIdDirezione("idDirezione", idDirezione);
	}
	
	public void validaIdDirezione(String fieldName, String idDirezione) throws ValidationException {
		this.validaIdDirezione(fieldName, idDirezione, false);
	}
	
	public void validaIdDirezione(String fieldName, String idDirezione, boolean notnull) throws ValidationException {
		this.validaId(fieldName, idDirezione, CostantiValidazione.PATTERN_ID_DIREZIONE, 1, 35, notnull);
	}
	
	public void validaIdDivisione(String idDivisione) throws ValidationException {
		this.validaIdDivisione("idDivisione", idDivisione);
	}
	
	public void validaIdDivisione(String fieldName, String idDivisione) throws ValidationException {
		this.validaIdDivisione(fieldName, idDivisione, false);
	}
	
	public void validaIdDivisione(String fieldName, String idDivisione, boolean notnull) throws ValidationException {
		this.validaId(fieldName, idDivisione, CostantiValidazione.PATTERN_ID_DIVISIONE, 1, 35, notnull);
	}
	
	public void validaParametroGReCaptchaResponse(String nomeParametro) throws ValidationException {
		this.validaParametroGReCaptchaResponse("parametro", nomeParametro);
	}
	
	public void validaParametroGReCaptchaResponse(String fieldName, String nomeParametro) throws ValidationException {
		this.validaId(fieldName, nomeParametro, CostantiValidazione.PATTERN_G_RECAPTCHA_RESPONSE, 1, null);
	}
	
	public void validaPassword(String fieldName, String nomeParametro) throws ValidationException {
		this.validaId(fieldName, nomeParametro, CostantiValidazione.PATTERN_PASSWORD_HTTP_BASIC_DEFAULT, 1, 255, false);
	}
	
	public StringValidator validaId(String fieldName, String id, String pattern, Integer minLength, Integer maxLength) throws ValidationException {
		return validaId(fieldName, id, pattern, minLength, maxLength, true);
	}
	
	public StringValidator validaId(String fieldName, String id, String pattern, Integer minLength, Integer maxLength, boolean notNull) throws ValidationException {
		StringValidator validator = this.vf.getValidator(fieldName, id);
		
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
