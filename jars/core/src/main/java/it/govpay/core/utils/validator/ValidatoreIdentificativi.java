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
		this.validaId(fieldName, idUO, CostantiValidazione.PATTERN_ID_UO, 1, 35);
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
		this.validaId(fieldName, idTipoVersamento, CostantiValidazione.PATTERN_ID_TIPO_VERSAMENTO, 1, 35);
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
		this.validaId(fieldName, ibanAccredito, CostantiValidazione.PATTERN_IBAN_ACCREDITO, 1, 255);
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
