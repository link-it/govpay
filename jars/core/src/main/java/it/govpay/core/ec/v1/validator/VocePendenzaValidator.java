package it.govpay.core.ec.v1.validator;

import java.math.BigDecimal;
import java.util.List;

import org.openspcoop2.utils.json.ValidationException;

import it.govpay.core.beans.EsitoOperazione;
import it.govpay.core.exceptions.GovPayException;
import it.govpay.core.utils.validator.IValidable;
import it.govpay.core.utils.validator.ValidatorFactory;
import it.govpay.core.utils.validator.ValidatoreIdentificativi;
import it.govpay.core.utils.validator.ValidatoreUtils;
import it.govpay.ec.v1.beans.QuotaContabilita;
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
			this.validaContabilita(vf);
			if(this.vocePendenza.getIdDominio() != null)
				vi.validaIdDominio("idDominio", this.vocePendenza.getIdDominio());

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

	private void validaContabilita(ValidatorFactory vf) throws ValidationException {
		if(this.vocePendenza.getContabilita() != null) {
			List<QuotaContabilita> listaContabilita = this.vocePendenza.getContabilita().getQuote();
			if(listaContabilita != null) {
				for (QuotaContabilita contabilita : listaContabilita) {

					vf.getValidator("capitolo", contabilita.getCapitolo()).notNull().minLength(1).maxLength(64);
					
					vf.getValidator("annoEsercizio", contabilita.getAnnoEsercizio()).notNull();
					ValidatoreUtils.validaAnnoRiferimento(vf, "annoEsercizio", contabilita.getAnnoEsercizio());

					vf.getValidator("accertamento", contabilita.getAccertamento()).minLength(1).maxLength(64);
					ValidatoreUtils.validaImporto(vf, "importo", contabilita.getImporto());

					vf.getValidator("titolo", contabilita.getTitolo()).minLength(1).maxLength(64);
					vf.getValidator("tipologia", contabilita.getTipologia()).minLength(1).maxLength(64);
					vf.getValidator("categoria", contabilita.getCategoria()).minLength(1).maxLength(64);
					vf.getValidator("articolo", contabilita.getArticolo()).minLength(1).maxLength(64);
				}
			}
		}
	}

	public void validazioneSemanticaContabilita(ValidatorFactory vf, String idA2A, String idPendenza) throws ValidationException {
		if(this.vocePendenza.getContabilita() != null) {
			List<QuotaContabilita> listaContabilita = this.vocePendenza.getContabilita().getQuote();
			BigDecimal importoVocePendenza = this.vocePendenza.getImporto();
			if(listaContabilita != null) {
				try {
					BigDecimal somma = BigDecimal.ZERO;
					for (QuotaContabilita voceContabilita : listaContabilita) {
						somma = somma.add(voceContabilita.getImporto());
					}
	
					if(somma.compareTo(vocePendenza.getImporto()) != 0) {
						throw new GovPayException(EsitoOperazione.VER_035, vocePendenza.getIdVocePendenza(), idA2A, idPendenza,
								Double.toString(importoVocePendenza.doubleValue()), Double.toString(somma.doubleValue()));
					}
				}catch (GovPayException e) {
					throw new ValidationException(e.getMessage(), e);
				}
			}
		}
	}
	
}
