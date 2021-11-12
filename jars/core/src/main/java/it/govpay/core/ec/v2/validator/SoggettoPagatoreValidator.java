package it.govpay.core.ec.v2.validator;

import it.govpay.core.utils.validator.CostantiValidazione;
import it.govpay.core.utils.validator.ValidatorFactory;

public class SoggettoPagatoreValidator{

	private ValidatorFactory vf = null; 

	public SoggettoPagatoreValidator() {
		this.vf = ValidatorFactory.newInstance();
	}

	public static SoggettoPagatoreValidator newInstance() {
		return new SoggettoPagatoreValidator();
	}

	/*
		Tabella campi del soggetto all'interno della tabella versamenti

		 debitore_tipo                 | character varying(1)        |           |          | 
		 debitore_identificativo       | character varying(35)       |           | not null | 
		 debitore_anagrafica           | character varying(70)       |           | not null | 
		 debitore_indirizzo            | character varying(70)       |           |          | 
		 debitore_civico               | character varying(16)       |           |          | 
		 debitore_cap                  | character varying(16)       |           |          | 
		 debitore_localita             | character varying(35)       |           |          | 
		 debitore_provincia            | character varying(35)       |           |          | 
		 debitore_nazione              | character varying(2)        |           |          | 
		 debitore_email                | character varying(256)      |           |          | 
		 debitore_telefono             | character varying(35)       |           |          | 
		 debitore_cellulare            | character varying(35)       |           |          | 
		 debitore_fax                  | character varying(35)       |           |          | 
	 
	 */ 

	public void validaCellulare(String fieldName, String cellulare) throws org.openspcoop2.utils.json.ValidationException{
		this.vf.getValidator(fieldName, cellulare).pattern(CostantiValidazione.PATTERN_CELLULARE).maxLength(35);
	}

	public void validaEmail(String fieldName, String email) throws org.openspcoop2.utils.json.ValidationException{
		this.vf.getValidator(fieldName, email).pattern(CostantiValidazione.PATTERN_EMAIL).maxLength(256);
	}

	public void validaNazione(String fieldName, String nazione) throws org.openspcoop2.utils.json.ValidationException{
		this. vf.getValidator(fieldName, nazione).pattern(CostantiValidazione.PATTERN_NAZIONE).length(2);
	}

	public void validaProvincia(String fieldName, String provincia) throws org.openspcoop2.utils.json.ValidationException{
		this.vf.getValidator(fieldName, provincia).minLength(1).maxLength(35);
	}

	public void validaLocalita(String fieldName, String localita) throws org.openspcoop2.utils.json.ValidationException{
		this.vf.getValidator(fieldName, localita).minLength(1).maxLength(35);
	}

	public void validaCap(String fieldName, String cap) throws org.openspcoop2.utils.json.ValidationException{
		this.vf.getValidator(fieldName, cap).minLength(1).maxLength(16);
	}

	public void validaCivico(String fieldName, String civico) throws org.openspcoop2.utils.json.ValidationException{
		this.vf.getValidator(fieldName, civico).minLength(1).maxLength(16);
	}

	public void validaIndirizzo(String fieldName, String indirizzo) throws org.openspcoop2.utils.json.ValidationException{
		this.vf.getValidator(fieldName, indirizzo).minLength(1).maxLength(70);
	}

	public void validaAnagrafica(String fieldName, String anagrafica) throws org.openspcoop2.utils.json.ValidationException{
		this.vf.getValidator(fieldName, anagrafica).notNull().minLength(1).maxLength(70);
	}

	public void validaIdentificativo(String fieldName, String identificativo) throws org.openspcoop2.utils.json.ValidationException{
		this.vf.getValidator(fieldName, identificativo).notNull().minLength(2).maxLength(16);
	}

	public void validaTipo(String fieldName, String tipo) throws org.openspcoop2.utils.json.ValidationException {
		this.vf.getValidator(fieldName, tipo).notNull();
	}
}

