package it.govpay.core.utils.validator;

import java.math.BigDecimal;

import org.apache.commons.lang.ArrayUtils;
import org.openspcoop2.utils.json.ValidationException;

import it.govpay.ec.v1.beans.DatiEntrata.TipoBolloEnum;
import it.govpay.ec.v1.beans.VocePendenza;

public class VocePendenzaValidator implements IValidable{

	private VocePendenza vocePendenza = null;


	public VocePendenzaValidator(VocePendenza vocePendenza) {
		this.vocePendenza = vocePendenza;
	}

	@Override
	public void validate() throws ValidationException {
		if(this.vocePendenza != null) {

			ValidatorFactory vf = ValidatorFactory.newInstance();

			vf.getValidator("idVocePendenza", this.vocePendenza.getIdVocePendenza()).notNull().minLength(1).maxLength(35);
			vf.getValidator("importo", this.vocePendenza.getImporto()).notNull().minOrEquals(BigDecimal.ZERO).maxOrEquals(BigDecimal.valueOf(999999.99)).checkDecimalDigits();
			vf.getValidator("descrizione", this.vocePendenza.getDescrizione()).notNull().minLength(1).maxLength(255);
			vf.getValidator("descrizioneCausaleRPT", this.vocePendenza.getDescrizioneCausaleRPT()).minLength(1).maxLength(140);

			if(this.vocePendenza.getCodEntrata() != null) {
				vf.getValidator("codEntrata", this.vocePendenza.getCodEntrata()).notNull().minLength(1).maxLength(35);
				try {
					vf.getValidator("tipoBollo", this.vocePendenza.getTipoBollo()).isNull();
					vf.getValidator("hashDocumento", this.vocePendenza.getHashDocumento()).isNull();
					vf.getValidator("provinciaResidenza", this.vocePendenza.getProvinciaResidenza()).isNull();
					vf.getValidator("ibanAccredito", this.vocePendenza.getIbanAccredito()).isNull();
					vf.getValidator("ibanAppoggio", this.vocePendenza.getIbanAppoggio()).isNull();
					vf.getValidator("tipoContabilita", this.vocePendenza.getTipoContabilita()).isNull();
					vf.getValidator("codiceContabilita", this.vocePendenza.getCodiceContabilita()).isNull();
				} catch (ValidationException ve) {
					throw new ValidationException("Valorizzato codEntrata. " + ve.getMessage());
				}

				return;
			}

			else if(this.vocePendenza.getTipoBollo() != null) {
				vf.getValidator("tipoBollo", this.vocePendenza.getTipoBollo()).notNull();
				
				if(TipoBolloEnum.fromValue(this.vocePendenza.getTipoBollo()) == null) {
					throw new ValidationException("Codifica inesistente per tipoBollo. Valore fornito [" + this.vocePendenza.getTipoBollo() + "] valori possibili " + ArrayUtils.toString(TipoBolloEnum.values()));
					
				}
				
				
				vf.getValidator("hashDocumento", this.vocePendenza.getHashDocumento()).notNull().minLength(1).maxLength(70);
				vf.getValidator("provinciaResidenza", this.vocePendenza.getProvinciaResidenza()).notNull().pattern(CostantiValidazione.PATTERN_PROVINCIA);

				try {
					vf.getValidator("ibanAccredito", this.vocePendenza.getIbanAccredito()).isNull();
					vf.getValidator("ibanAppoggio", this.vocePendenza.getIbanAppoggio()).isNull();
					vf.getValidator("tipoContabilita", this.vocePendenza.getTipoContabilita()).isNull();
					vf.getValidator("codiceContabilita", this.vocePendenza.getCodiceContabilita()).isNull();
				} catch (ValidationException ve) {
					throw new ValidationException("Valorizzato tipoBollo. " + ve.getMessage());
				}

				return;
			}


			else if(this.vocePendenza.getIbanAccredito() != null) {
				vf.getValidator("ibanAccredito", this.vocePendenza.getIbanAccredito()).notNull().pattern(CostantiValidazione.PATTERN_IBAN_ACCREDITO);
				vf.getValidator("ibanAppoggio", this.vocePendenza.getIbanAppoggio()).pattern(CostantiValidazione.PATTERN_IBAN_ACCREDITO);
				vf.getValidator("tipoContabilita", this.vocePendenza.getTipoContabilita()).notNull();
				ValidatoreUtils.validaCodiceContabilita(vf, "codiceContabilita", this.vocePendenza.getCodiceContabilita());

				try {
					vf.getValidator("hashDocumento", this.vocePendenza.getHashDocumento()).isNull();
					vf.getValidator("provinciaResidenza", this.vocePendenza.getProvinciaResidenza()).isNull();
				} catch (ValidationException ve) {
					throw new ValidationException("Valorizzato ibanAccredito. " + ve.getMessage());
				}
			}

			else {
				throw new ValidationException("Uno dei campi tra ibanAccredito, tipoBollo o codEntrata deve essere valorizzato");
			}
		}
	}
}
