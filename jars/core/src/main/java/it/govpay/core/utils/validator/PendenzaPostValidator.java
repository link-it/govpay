package it.govpay.core.utils.validator;

import java.math.BigDecimal;
import java.util.Date;

import org.apache.commons.lang.ArrayUtils;
import org.openspcoop2.utils.json.ValidationException;

import it.govpay.core.utils.IuvUtils;
import it.govpay.ec.v1.beans.DatiEntrata.TipoBolloEnum;
import it.govpay.core.beans.tracciati.PendenzaPost;
import it.govpay.core.beans.tracciati.Soggetto;
import it.govpay.core.beans.tracciati.VocePendenza;

public class PendenzaPostValidator  implements IValidable{

	private PendenzaPost pendenzaVerificata= null;
	private ValidatorFactory vf = null; 
	private ValidatoreIdentificativi validatoreId = null;

	public PendenzaPostValidator(PendenzaPost pendenzaVerificata){
		this.pendenzaVerificata = pendenzaVerificata;
		this.vf = ValidatorFactory.newInstance();
		this.validatoreId = ValidatoreIdentificativi.newInstance();
	}

	@Override
	public void validate() throws ValidationException {
		if(this.pendenzaVerificata != null) {

			validaIdPendenza(this.pendenzaVerificata.getIdPendenza());
			validaIdA2A(this.pendenzaVerificata.getIdA2A());
			validaIdDominio(this.pendenzaVerificata.getIdDominio());
			validaIdUnitaOperativa(this.pendenzaVerificata.getIdUnitaOperativa());
			validaIdTipoPendenza(this.pendenzaVerificata.getIdTipoPendenza());
			validaNomePendenza(this.pendenzaVerificata.getNome());
			validaCausale( this.pendenzaVerificata.getCausale());
			
			Soggetto soggetto = this.pendenzaVerificata.getSoggettoPagatore();
			if(soggetto == null)
				throw new ValidationException("Il campo soggettoPagatore non deve essere vuoto.");
			
			SoggettoPagatoreValidator soggettoPagatoreValidator = SoggettoPagatoreValidator.newInstance();
			
			soggettoPagatoreValidator.validaTipo("tipo", soggetto.getTipo() != null ? soggetto.getTipo().toString() : null);
			soggettoPagatoreValidator.validaIdentificativo("identificativo", soggetto.getIdentificativo());
			soggettoPagatoreValidator.validaAnagrafica("anagrafica", soggetto.getAnagrafica());
			soggettoPagatoreValidator.validaIndirizzo("indirizzo", soggetto.getIndirizzo());
			soggettoPagatoreValidator.validaCivico("civico", soggetto.getCivico());
			soggettoPagatoreValidator.validaCap("cap", soggetto.getCap());
			soggettoPagatoreValidator.validaLocalita("localita", soggetto.getLocalita());
			soggettoPagatoreValidator.validaProvincia("provincia", soggetto.getProvincia());
			soggettoPagatoreValidator.validaNazione("nazione", soggetto.getNazione());
			soggettoPagatoreValidator.validaEmail("email", soggetto.getEmail());
			soggettoPagatoreValidator.validaCellulare("cellulare", soggetto.getCellulare());
			
			validaImporto(this.pendenzaVerificata.getImporto());
			validaNumeroAvviso(this.pendenzaVerificata.getNumeroAvviso());
			validaDataValidita(this.pendenzaVerificata.getDataValidita()); 
			validaDataScadenza(this.pendenzaVerificata.getDataScadenza());
			validaAnnoRiferimento(this.pendenzaVerificata.getAnnoRiferimento());
			validaCartellaPagamento(this.pendenzaVerificata.getCartellaPagamento());
			validaDirezione(this.pendenzaVerificata.getDirezione());
			validaDivisione(this.pendenzaVerificata.getDivisione()); 
			
			if(this.pendenzaVerificata.getVoci() == null || this.pendenzaVerificata.getVoci().isEmpty())
				throw new ValidationException("Il campo voci non deve essere vuoto.");

			if(this.pendenzaVerificata.getVoci().size() < 1)
				throw new ValidationException("Il campo voci deve avere almeno 1 elemento.");

			if(this.pendenzaVerificata.getVoci().size() > 5)
				throw new ValidationException("Il campo voci deve avere massimo 5 elemento.");

			for (VocePendenza vocePendenza : this.pendenzaVerificata.getVoci()) {
				new VocePendenzaValidator(vocePendenza).validate();
			}
		}
	}

	public void validaIdA2A(String idA2A) throws ValidationException {
		this.validatoreId.validaId("idA2A", idA2A, CostantiValidazione.PATTERN_ID_APPLICAZIONE, 1, 35);
	}

	public void validaIdDominio(String idDominio) throws ValidationException {
		this.validatoreId.validaIdDominio("idDominio", idDominio);
	}

	public void validaIdUnitaOperativa(String idUnitaOperativa) throws ValidationException {
		this.validatoreId.validaId("idUnitaOperativa", idUnitaOperativa, CostantiValidazione.PATTERN_ID_UO, 1, 35, false);
	}
	
	public void validaIdTipoPendenza(String idTipoPendenza) throws ValidationException {
		this.validatoreId.validaId("idTipoPendenza", idTipoPendenza, CostantiValidazione.PATTERN_ID_TIPO_VERSAMENTO, 1, 35, false);
	}

	public void validaNomePendenza(String nome) throws ValidationException {
		vf.getValidator("nome", nome).minLength(1).maxLength(35);
	}

	public void validaCausale(String causale) throws ValidationException {
		vf.getValidator("causale", causale).notNull().minLength(1).maxLength(140);
	}

	public void validaImporto(BigDecimal importo) throws ValidationException {
		vf.getValidator("importo", importo).notNull().minOrEquals(BigDecimal.ZERO).maxOrEquals(BigDecimal.valueOf(999999.99)).checkDecimalDigits();
	}

	public void validaNumeroAvviso(String numeroAvviso) throws ValidationException {
		vf.getValidator("numeroAvviso", numeroAvviso).maxLength(18).pattern("[0-9]{18}");
		IuvUtils.toIuv(numeroAvviso);
	}

	public void validaDataValidita(Date date) throws ValidationException {
		vf.getValidator("dataValidita", date).isValid();
	}

	public void validaDataScadenza(Date date) throws ValidationException {
		vf.getValidator("dataScadenza", date).isValid();
	}

	public void validaAnnoRiferimento(BigDecimal annoRiferimento) throws ValidationException {
		if(annoRiferimento != null)
			vf.getValidator("annoRiferimento", annoRiferimento.toBigInteger().toString()).pattern("[0-9]{4}");
	}

	public void validaCartellaPagamento(String cartellaPagamento) throws ValidationException {
		vf.getValidator("cartellaPagamento", cartellaPagamento).minLength(1).maxLength(35);
	}

	public void validaIdPendenza(String idPendenza) throws ValidationException {
		this.validatoreId.validaIdPendenza("idPendenza", idPendenza);
	}
	
	public void validaDirezione(String direzione) throws ValidationException {
		vf.getValidator("direzione", direzione).minLength(1).maxLength(35);
	}
	
	public void validaDivisione(String divisione) throws ValidationException {
		vf.getValidator("divisione", divisione).minLength(1).maxLength(35);
	}
	
	
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
					vf.getValidator("provinciaResidenza", this.vocePendenza.getProvinciaResidenza()).notNull().pattern("[A-Z]{2,2}");

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
}
