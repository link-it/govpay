package it.govpay.core.utils.validator;

import org.openspcoop2.utils.json.ValidationException;

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
			ValidatoreIdentificativi vi = ValidatoreIdentificativi.newInstance();
			
			vi.validaIdVocePendenza("idVocePendenza", this.vocePendenza.getIdVocePendenza());
			ValidatoreUtils.validaImporto(vf, "importo", this.vocePendenza.getImporto());
			ValidatoreUtils.validaDescrizione(vf, "descrizione", this.vocePendenza.getDescrizione());
			ValidatoreUtils.validaDescrizioneCausaleRPT(vf, "descrizioneCausaleRPT", this.vocePendenza.getDescrizioneCausaleRPT());

			if(this.vocePendenza.getCodEntrata() != null) {
				vi.validaIdEntrata("codEntrata", this.vocePendenza.getCodEntrata());
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
				ValidatoreUtils.validaTipoBollo(vf, "tipoBollo", this.vocePendenza.getTipoBollo());
				ValidatoreUtils.validaHashDocumento(vf, "hashDocumento", this.vocePendenza.getHashDocumento());
				ValidatoreUtils.validaProvinciaResidenza(vf, "provinciaResidenza", this.vocePendenza.getProvinciaResidenza());

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
				vi.validaIdIbanAccredito("ibanAccredito", this.vocePendenza.getIbanAccredito(), true);
				vi.validaIdIbanAccredito("ibanAppoggio", this.vocePendenza.getIbanAppoggio());

				ValidatoreUtils.validaTipoContabilita(vf, "tipoContabilita", this.vocePendenza.getTipoContabilita());
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
