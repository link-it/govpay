package it.govpay.core.ec.v1.validator.legacy;

import org.openspcoop2.utils.json.ValidationException;

import it.govpay.core.dao.commons.Versamento.SingoloVersamento;
import it.govpay.core.utils.validator.IValidable;
import it.govpay.core.utils.validator.ValidatorFactory;
import it.govpay.core.utils.validator.ValidatoreIdentificativi;
import it.govpay.core.utils.validator.ValidatoreUtils;

public class SingoloVersamentoValidator implements IValidable{

	private SingoloVersamento vocePendenza = null;


	public SingoloVersamentoValidator(SingoloVersamento vocePendenza) {
		this.vocePendenza = vocePendenza;
	}

	@Override
	public void validate() throws ValidationException {
		if(this.vocePendenza != null) {

			ValidatorFactory vf = ValidatorFactory.newInstance();
			ValidatoreIdentificativi vi = ValidatoreIdentificativi.newInstance();

			vi.validaIdVocePendenza("codSingoloVersamentoEnte", this.vocePendenza.getCodSingoloVersamentoEnte());
			ValidatoreUtils.validaImporto(vf, "importo", this.vocePendenza.getImporto());
//			ValidatoreUtils.validaDescrizione(vf, "descrizione", this.vocePendenza.getDescrizione());
			ValidatoreUtils.validaDescrizioneCausaleRPT(vf, "descrizioneCausaleRPT", this.vocePendenza.getDescrizioneCausaleRPT());
			if(this.vocePendenza.getCodDominio() != null)
				vi.validaIdDominio("codDominio", this.vocePendenza.getCodDominio());

			if(this.vocePendenza.getCodTributo() != null) {
				vi.validaIdEntrata("codTributo", this.vocePendenza.getCodTributo());
				try {
					if(this.vocePendenza.getBolloTelematico() != null)
						throw new ValidationException("Il campo bolloTelematico deve essere vuoto.");
					
					if(this.vocePendenza.getTributo() != null)
						throw new ValidationException("Il campo tributo deve essere vuoto.");
				} catch (ValidationException ve) {
					throw new ValidationException("Valorizzato codEntrata. " + ve.getMessage());
				}

				return;
			}

			else if(this.vocePendenza.getBolloTelematico() != null) {
				ValidatoreUtils.validaTipoBollo(vf, "tipo", this.vocePendenza.getBolloTelematico().getTipo());
				ValidatoreUtils.validaHashDocumento(vf, "hash", this.vocePendenza.getBolloTelematico().getHash());
				ValidatoreUtils.validaProvinciaResidenza(vf, "provincia", this.vocePendenza.getBolloTelematico().getProvincia());

				try {
					if(this.vocePendenza.getTributo() != null)
						throw new ValidationException("Il campo tributo deve essere vuoto.");
				} catch (ValidationException ve) {
					throw new ValidationException("Valorizzato tipoBollo. " + ve.getMessage());
				}

				return;
			}


			else if(this.vocePendenza.getTributo() != null) {
				vi.validaIdIbanAccredito("ibanAccredito", this.vocePendenza.getTributo().getIbanAccredito(), true);
				vi.validaIdIbanAccredito("ibanAppoggio", this.vocePendenza.getTributo().getIbanAppoggio());

				ValidatoreUtils.validaTipoContabilita(vf, "tipoContabilita", this.vocePendenza.getTributo().getTipoContabilita());
				ValidatoreUtils.validaCodiceContabilita(vf, "codContabilita", this.vocePendenza.getTributo().getCodContabilita());

				try {
					if(this.vocePendenza.getBolloTelematico() != null)
						throw new ValidationException("Il campo bolloTelematico deve essere vuoto.");
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
